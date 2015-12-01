/*
 * Sep 28, 06
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad Traslado Centros de Atencion
 */
public class SqlBaseTrasladoCentroAtencionDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTrasladoCentroAtencionDao.class);

	/**
	 * Cadena que consulta el nombre del centro de atencion donde
	 * se encuentra el ingreso abierto del paciente
	 */
	private static final String consultarCentroAtencionIngresoStr = "SELECT "+ 
		"getnomcentroatencion(cc.centro_atencion) as centro_atencion "+ 
		"FROM cuentas c " +
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
		"WHERE "+ 
		"c.codigo_paciente = ? AND "+ 
		"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' AND " +
		"cc.centro_atencion != ?";
	
	/**
	 * Cadena que consulta la fecha del egreso
	 */
	private static final String consultarFechaEgresoStr = " SELECT " +
		"CASE WHEN fecha_egreso IS NULL THEN '' ELSE to_char(fecha_egreso,'DD/MM/YYYY') END as fecha_egreso " +
		"FROM egresos " +
		"WHERE cuenta = ? AND usuario_responsable IS NOT NULL ";
	
	/**
	 * Cadena que verifica que la valoracion inicial haya sido respondida
	 */
	private static final String verificarValoracionRespondidaStr = "SELECT " +
		"count(1) AS cuenta " +
		"FROM solicitudes " +
		"WHERE " +
		"cuenta = ? and " +
		"tipo in ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") and " +
		"estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+")";
	
	/**
	 * Cadena que consulta la fecha de orden de salida de un paciente
	 */
	private static final String obtenerFechaOrdenSalidaStr = "SELECT e.fecha_evolucion AS fecha "+
		"FROM solicitudes s "+ 
		"INNER JOIN evoluciones e ON(s.numero_solicitud=e.valoracion) "+
		"INNER JOIN cuentas c ON(c.id=s.cuenta) "+
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
		"LEFT OUTER JOIN egresos eg ON(eg.cuenta=c.id) "+
		"WHERE " +
		" s.cuenta=? AND " +
		"(s.tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" OR " +
			"s.tipo="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") AND " +
		"e.orden_salida="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND "+
		"(eg.fecha_reversion_egreso is null OR " +
		"e.fecha_evolucion>eg.fecha_reversion_egreso OR " +
		"(e.fecha_evolucion=eg.fecha_reversion_egreso AND e.hora_evolucion > eg.hora_reversion_egreso))";
	
	/**
	 * Cadena que desasigna la cama de una admision de urgencias
	 */
	private static final String desasignarCamaUrgenciasStr = "UPDATE admisiones_urgencias SET " +
		"fecha_ingreso_observacion = null,hora_ingreso_observacion = null, cama_observacion = null, fecha_egreso_observacion=null,hora_egreso_observacion = null " +
		"WHERE cuenta = ?";
	
	/**
	 * Cadena que actualiza el area de la cuenta
	 */
	private static final String actualizarAreaCuentaStr = "UPDATE cuentas SET area = ? WHERE id = ?";
	
	/**
	 * Cadena que finaliza adjunto cuenta en el caso de que haya
	 */
	private static final String actualizarAdjuntoCuentaStr = "UPDATE adjuntos_cuenta SET fecha_fin = CURRENT_DATE, hora_fin = "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" WHERE cuenta = ? AND fecha_fin IS NULL and hora_fin IS NULL";
	
	/**
	 * Cadena que finaliza tratante cuenta
	 */
	private static final String actualizarTratanteCuentaStr = "UPDATE tratantes_cuenta SET fecha_fin = CURRENT_DATE, hora_fin = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE cuenta = ? AND fecha_fin IS NULL and hora_fin IS NULL";
	
	/**
	 * Cadena que inserta un nuevo tratante
	 */
	private static final String insertarTratanteStr = "INSERT INTO tratantes_cuenta " +
		"(codigo, solicitud, codigo_medico, fecha_inicio, fecha_fin, hora_inicio, hora_fin, centro_costo,cuenta) " +
		"VALUES " ;
		
	
	/**
	 * Cadena que inserta el traslado centro de atencion
	 */
	private static final String insertarTrasladoCentroAtencionStr = "INSERT INTO traslado_centro_atencion " +
		"(consecutivo,cuenta,centro_atencion_inicial,area_inicial,nuevo_centro_atencion,nueva_area,observaciones,fecha_traslado,hora_traslado,traslado_cama,usuario_responsable,institucion) " +
		"VALUES " ;
		
	
	/**
	 * Método implementado para realizar las validaciones de ingreso
	 * a la funcionalidad Traslado Centro Atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static Vector validaciones(Connection con,HashMap campos)
	{
		try
		{
			Vector mensaje = new Vector();
			PersonaBasica paciente = (PersonaBasica)campos.get("pacienteActivo");
			UsuarioBasico usuario = (UsuarioBasico)campos.get("usuarioBasico");
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			
			RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
			//1.1) Verifica si el ingreso está cargado en sesion y vuenta válida
			if(!resp.puedoSeguir)
			{
				//Se verifica si el paciente tiene ingresos abiertos en otros centros de atencion
				//y en el caso de que haya un ingreso se consulta el nombre de su centro de atencion asociado
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCentroAtencionIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,paciente.getCodigoPersona());
				pst.setInt(2,usuario.getCodigoCentroAtencion());
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					mensaje.add("errores.paciente.ingresoAbiertoCentroAtencion"+ConstantesBD.separadorSplit+rs.getString("centro_atencion"));
				else
					mensaje.add(resp.textoRespuesta);
			}
			//1.2) Se verifica si es hospital día
			else if(paciente.isHospitalDia())
			{
				mensaje.add("errores.paciente.ingresoHospitalDia");
			}
			//1.3) Verificar ingreso ABIERTO en el centro de atencion de la sesión ****************
			else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{	
				//definitivamente el paciente no tiene ingreso abierto
				mensaje.add("errors.paciente.noIngreso");
			}
			else
			{
				int idCuenta = paciente.getCodigoCuenta();
				String fechaEgreso = "";
				
				//2) Verificar que la cuenta corresponda a la vía de ingreso Urgencias u Hospitalizacion *****************
				if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion&&
					paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
					mensaje.add("errors.paciente.noCuentaHospitalizacionUrgencias");
				else
				{
					//3) Se verifica que el paciente no tenga orden de salida en las evoluciones************************************
					String fechaOrdenSalida = "";
					pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerFechaOrdenSalidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,idCuenta);
					
					rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
						fechaOrdenSalida = rs.getString("fecha");
					
					if(!fechaOrdenSalida.equals(""))
						mensaje.add("error.evolucion.pacienteConOrdenSalida"+ConstantesBD.separadorSplit+UtilidadFecha.conversionFormatoFechaAAp(fechaOrdenSalida));
					//********************************************************************************************************
					
					//4) Se verifica que el paciente no tenga egreso**********************************************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,idCuenta);
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
						fechaEgreso = rs.getString("fecha_egreso");
					
					if(!fechaEgreso.equals(""))
						mensaje.add("egreso.pacienteConEgreso"+ConstantesBD.separadorSplit+fechaEgreso);
					//*******************************************************************************************
					
					if(mensaje.size()<=0)
					{
						//5) Se verifica que la valoracion se haya respondido************************************
						boolean existe = false;
						pst =  new PreparedStatementDecorator(con.prepareStatement(verificarValoracionRespondidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setInt(1,idCuenta);
						
						rs = new ResultSetDecorator(pst.executeQuery());
						
						if(rs.next())
							if(rs.getInt("cuenta")>0)
								existe = true;
						
						if(!existe)
							mensaje.add("error.valoracion.sinValoracion");
						//********************************************************************************************
					}
					
				}
				//******************************************************************************
				
			}
			//***********************************************************************************
			
			
			
			return mensaje;
		}
		catch(SQLException e)
		{
			logger.error("Error en validaciones de SqlBaseTrasladoCentroAtencionDao: "+e);
			return new Vector();
		}
	}
	
	/**
	 * Método que desasigna la cama de una admision de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int desasignarCamaUrgencias(Connection con,String idCuenta)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(desasignarCamaUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en desasignarCamaUrgencias de SqlBaseTrasladoCentroAtencionDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para realizar el traslado por centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int realizarTraslado(Connection con,HashMap campos)
	{
		int resp = 0;
		try
		{
			int resp0 = 0,resp1 = 0, resp2 = 0, resp3 = 0;
			String idCuenta = campos.get("idCuenta").toString();
			String nuevaArea = campos.get("nuevaArea").toString();
			String observaciones = campos.get("observaciones").toString();
			int codigoTraslado = Integer.parseInt(campos.get("codigoTraslado").toString());
			String consulta = "";
			
			//1) Se actualiza el area de la cuenta ********************************+
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarAreaCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(nuevaArea));
			pst.setInt(2,Utilidades.convertirAEntero(idCuenta));
			
			resp0 = pst.executeUpdate();
			
			//2) Se actualiza registro adjunto cuenta (si existe)***********************
			pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarAdjuntoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			pst.executeUpdate();
			
			//3) Se inserta y actualiza registro tratante cuenta *****************************
			pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarTratanteCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			resp1 = pst.executeUpdate();
			//---------------------------------------------------------
			consulta = insertarTratanteStr + " ("+campos.get("secuenciaTratante")+",?,?,CURRENT_DATE,null,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",null,?,?)";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tratantes_cuenta " +
				"(codigo, solicitud, codigo_medico, fecha_inicio, fecha_fin, hora_inicio, hora_fin, centro_costo,cuenta)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("valoracion")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoMedico")+""));
			pst.setInt(3,Utilidades.convertirAEntero(nuevaArea));
			pst.setInt(4,Utilidades.convertirAEntero(idCuenta));
			resp2 = pst.executeUpdate();
			
			
			//4) se inserta el traslado centro de atencion *****************************
			consulta = insertarTrasladoCentroAtencionStr + " ("+campos.get("secuencia")+",?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?) "; 
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO traslado_centro_atencion " +
		"			(consecutivo,
					cuenta,
					centro_atencion_inicial,
					area_inicial,
					nuevo_centro_atencion,
					nueva_area,
					observaciones,
					fecha_traslado,
					hora_traslado,
					traslado_cama,
					usuario_responsable,
					institucion)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("centroAtencionInicial")+""));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("areaInicial")+""));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("nuevoCentroAtencion")+""));
			pst.setInt(5,Utilidades.convertirAEntero(nuevaArea));
			if(observaciones.equals(""))
				pst.setNull(6,Types.CHAR);
			else
				pst.setString(6,observaciones);
			if(codigoTraslado<=0)
				pst.setNull(7,Types.INTEGER);
			else
				pst.setInt(7,codigoTraslado);
			pst.setString(8,campos.get("usuario")+"");
			pst.setInt(9,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			resp3 = pst.executeUpdate();
			//*********************************************************************************************
			
			if(resp0>0&&resp1>0&&resp2>0&&resp3>0)
				resp = 1;
			else
				resp = 0;
			
			return resp;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en realizarTraslado de SQlBaseTrasladoCentroAtencionDao: "+e);
			return resp;
		}
		
	}


}
