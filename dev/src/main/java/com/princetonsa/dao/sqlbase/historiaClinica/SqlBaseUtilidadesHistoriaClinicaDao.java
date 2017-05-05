/*
 *  Mayo 10, 2007
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.historiaClinica.DtoEspecialidad;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.dto.historiaClinica.DtoMedicamentosOriginales;
import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.componentes.DtoDesarrolloPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoEdadAlimentacionPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoObservacionesPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * 
 * @author sgomez
 * Objeto usado para el acceso común a la fuente de datos
 * de utilidades propias del módulo de HISTORIA CLINICA
 */
public class SqlBaseUtilidadesHistoriaClinicaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesHistoriaClinicaDao.class);
	
	/**
	 * Cadena que consulta los datos del accidente de trabajo que se ingresaron en el triage
	 */
	private static final String consultaDatosAccidenteTrabajoTriageStr = "SELECT " +
		"accidente_trabajo AS accidente_trabajo," +
		"CASE WHEN convenio_arp_afiliado IS NULL THEN '' ELSE convenio_arp_afiliado || '' END AS codigo_convenio_arp," +
		"CASE WHEN convenio_arp_afiliado IS NULL THEN '' ELSE getnombreconvenio(convenio_arp_afiliado) END AS nombre_convenio_arp " +
		"FROM triage " +
		"WHERE consecutivo = ? AND consecutivo_fecha = ?";
	
	/**
	 * Cadena que consulta la ultima evolucion de un ingreso
	 */
	private static final String consultarUltimaEvolucionIngresoStr = "SELECT "+
		"max(e.valoracion) AS evolucion "+ 
		"FROM cuentas c "+
		"INNER JOIN solicitudes s ON(s.cuenta=c.id AND s.tipo in (" +
			ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"," +
			ConstantesBD.codigoTipoSolicitudInterconsulta+")) "+
		"INNER JOIN evoluciones e ON(e.valoracion=s.numero_solicitud) "+
		"WHERE ";
		
	/**
	 * Cadena que consulta la especialidad de una evolucion o valoracion
	 */
	private static final String ultimaEspecialidadEvolucionValoracionIngresoStr = "SELECT "+
	"especialidad_solicitada AS especialidad "+ 
	"FROM ordenes.solicitudes sol "+
	"WHERE numero_solicitud = ?";
	
	/**
	 * Cadena que consulta la ultima valoracion del ingreso
	 */
	private static final String consultarUltimaValoracionIngresoStr = "SELECT "+
		"max(s.numero_solicitud) As valoracion "+ 
		"FROM cuentas c "+
		"INNER JOIN solicitudes s ON(s.cuenta=c.id AND s.tipo in (" +
			ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"," +
			ConstantesBD.codigoTipoSolicitudInterconsulta+","+ConstantesBD.codigoTipoSolicitudCita+")) "+
		"WHERE ";
	
	/**
	 * Cadena que consulta la ultima evolucion de un ingreso
	 */
	private static final String consultarUltimaEvolucionIngresoStrNoPos = "SELECT "+
		"max(e.codigo) AS evolucion "+ 
		"FROM cuentas c "+
		"INNER JOIN solicitudes s ON(s.cuenta=c.id AND s.tipo in (" +
			ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"," +
			ConstantesBD.codigoTipoSolicitudInterconsulta+")) "+
		"INNER JOIN evoluciones e ON(e.valoracion=s.numero_solicitud) "+
		"WHERE ";
	
	/**
	 * Cadena que consulta los diagnosticos de la evolucion
	 */
	private static final String consultarDiagnosticosEvolucionStr = "SELECT "+
		"ed.acronimo_diagnostico AS acronimo_diagnostico, "+
		"ed.tipo_cie_diagnostico AS tipo_cie, "+
		"getnombrediagnostico(ed.acronimo_diagnostico,ed.tipo_cie_diagnostico) AS nombre_diagnostico, "+
		"CASE WHEN ed.principal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"'  ELSE '"+ConstantesBD.acronimoNo+"' END AS principal "+
		"FROM evol_diagnosticos ed "+ 
		"WHERE "+
		"ed.evolucion = ? "+  
		"order by ed.numero";
	
	/**
	 * Cadena que consulta el diagnostico de complicacion de la evolucion
	 */
	private static final String consultarDiagnosticoComplicacionEvolucionStr = "SELECT " +
		"e.diagnostico_complicacion as acronimo_diagnostico," +
		"e.diagnostico_complicacion_cie as tipo_cie," +
		"getnombrediagnostico(e.diagnostico_complicacion,e.diagnostico_complicacion_cie) as nombre_diagnostico " +
		"from evoluciones e where e.codigo = ?";
	
	/**
	 * Cadena que consulta los diagnosticos de la valoracion
	 */
	private static final String consultarDiagnosticosValoracionStr = "SELECT "+
		"vd.acronimo_diagnostico AS acronimo_diagnostico, "+
		"vd.tipo_cie_diagnostico AS tipo_cie, "+
		"getnombrediagnostico(vd.acronimo_diagnostico,vd.tipo_cie_diagnostico) AS nombre_diagnostico, "+
		"CASE WHEN vd.principal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"'  ELSE '"+ConstantesBD.acronimoNo+"' END AS principal "+ 
		"FROM val_diagnosticos vd "+ 
		"WHERE "+
		"vd.valoracion = ? "+ 
		"order by vd.numero";
	
	/**
	 * Método que consulta los datos de accidente de trabajo del triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivoFecha
	 * @return
	 */
	public static HashMap consultaDatosAccidenteTrabajoTriage(Connection con,String consecutivo,String consecutivoFecha)
	{
		PreparedStatementDecorator pst = null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosAccidenteTrabajoTriageStr));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			pst.setString(2,consecutivoFecha);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
		
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaDatosAccidenteTrabajoTriage: "+e);
			return null;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta la última evolucion de un ingreso
	 * @param con
	 * @param ingreso
	 * @param seccionWHERE
	 * @return
	 */
	public static int consultarUltimaEvolucionIngreso(Connection con,int ingreso,String seccionWHERE)
	{
		PreparedStatementDecorator pst = null;
		try
		{
			int evolucion = 0;
			String consulta = consultarUltimaEvolucionIngresoStr + seccionWHERE;
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,ingreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				evolucion = rs.getInt("evolucion");
			
			return evolucion;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUltimaEvolucionIngreso: "+e);
			return 0;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	
	
	
	/**
	 * Método que consula la última valoracion de un ingreso
	 * @param con
	 * @param ingreso
	 * @param seccionWHERE
	 * @return
	 */
	public static int consultarUltimaValoracionIngresoNoPos(Connection con,int ingreso,String seccionWHERE)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs  = null;
		try
		{
			int valoracion = 0;
			String consulta = consultarUltimaEvolucionIngresoStrNoPos + seccionWHERE;
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,ingreso);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				valoracion = rs.getInt("evolucion");
			}
			return valoracion;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUltimaValoracionIngreso: "+e);
			return 0;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	
	/**
	 * Método que consula la última valoracion de un ingreso
	 * @param con
	 * @param ingreso
	 * @param seccionWHERE
	 * @return
	 */
	public static int consultarUltimaValoracionIngreso(Connection con,int ingreso,String seccionWHERE)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs  = null;
		try
		{
			int valoracion = 0;
			String consulta = consultarUltimaValoracionIngresoStr + seccionWHERE;
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,ingreso);
			
			 rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				valoracion = rs.getInt("valoracion");
			return valoracion;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUltimaValoracionIngreso: "+e);
			return 0;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta los motivos sirc parametrizados por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerMotivosSirc(Connection con,HashMap campos)
	{
		Statement st  = null;
		try
		{
			String consulta = "SELECT consecutivo,codigo,descripcion,tipo_motivo,activo,institucion FROM motivos_sirc  " +
					"WHERE institucion = "+campos.get("institucion");
			
			if(!campos.get("tipoMotivo").toString().equals(""))
				consulta += " AND tipo_motivo IN ('"+campos.get("tipoMotivo")+"')";
			
			if(!campos.get("activo").toString().equals(""))
				consulta += " AND activo = "+(UtilidadTexto.getBoolean(campos.get("activo").toString())?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas());
			
			consulta += " ORDER BY descripcion "; 
			
			st = con.createStatement( );
			

			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMotivosSirc: "+e);
			return null;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	
	}
	
	/**
	 * Método que consulta los servicios SIRC de una institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerServiciosSirc(Connection con,HashMap campos)
	{
		Statement st  = null;
		try
		{
			String consulta = "SELECT " +
				"s.codigo," +
				"s.descripcion," +
				"d.servicio," +
				"getcodigopropservicio2(d.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, " +
				"getnombreservicio(d.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_cups " +
				"FROM servicios_sirc s " +
				"INNER JOIN det_servicios_sirc d ON(d.servicio_sirc=s.codigo AND d.institucion=s.institucion) " +
				"WHERE s.institucion = "+campos.get("institucion");
			
			if(!campos.get("activo").toString().equals(""))
				consulta += " AND s.activo = '"+(UtilidadTexto.getBoolean(campos.get("activo").toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo)+"' ";
			
			consulta += " ORDER BY descripcion,codigo_cups";
			
			 st = con.createStatement();
			
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerServiciosSirc: "+e);
			return null;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta los estados de conciencia 
	 * @param con
	 * @param incluyeNoDefinido (si es true se incluirá el registro 'No definido')
	 * @return
	 */
	public static HashMap obtenerEstadosConciencia(Connection con,boolean incluyeNoDefinido)
	{
		Statement st = null;
		try
		{
			String consulta = "select codigo, nombre from estados_conciencia ";
			if(!incluyeNoDefinido)
				consulta += " WHERE codigo != -1  ";
			consulta += " order by nombre";
			
			 st = con.createStatement( );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadosConciencia: "+e);
			return null;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta las interpretaciones y la descripcion de los procedimientos de un ingreso
	 * validando el parámetro general "Valida en egreso solicitudes interpretadas:"
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerInterpretacionProcedimientosIngreso(Connection con,HashMap campos)
	{
		Statement st = null;
		try
		{
			String ingreso = campos.get("ingreso").toString();
			int institucion = Utilidades.convertirAEntero(campos.get("institucion").toString());
			boolean interpretadas = UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(institucion));
			
			String consulta = "SELECT "+ 
				"s.numero_solicitud, "+
				"to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud, "+
				"substr(s.hora_solicitud,0,6) AS hora_solicitud, " +
				"sp.codigo_servicio_solicitado AS codigo_servicio, "+
				"getnombreservicio(sp.codigo_servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") AS descripcion, "+
				"CASE WHEN s.interpretacion IS NULL THEN '' ELSE s.interpretacion END AS interpretacion " + 
				"FROM ingresos i "+ 
				"INNER JOIN cuentas c ON(c.id_ingreso=i.id) "+ 
				"INNER JOIN solicitudes s ON(s.cuenta=c.id AND " +
					"s.tipo="+ConstantesBD.codigoTipoSolicitudProcedimiento+" AND " +
					"s.estado_historia_clinica "+(interpretadas?" = "+ConstantesBD.codigoEstadoHCInterpretada:" IN ("+ConstantesBD.codigoEstadoHCInterpretada+","+ConstantesBD.codigoEstadoHCRespondida+")")+") "+ 
				"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) "+
				"WHERE "+ 
				"i.id = "+ingreso+" ORDER BY s.fecha_solicitud,s.hora_solicitud";
			
			 st = con.createStatement( );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerInterpretacionProcedimientosIngreso: "+e);
			return null;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta la última anamnesis del ingreso
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String getUltimaAnamnesisIngreso(Connection con,String numeroSolicitud)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String anamnesis = "";
			String consulta = "SELECT valor as valor, to_char(fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, hora as hora, usuario as usuario FROM valoracion_observaciones WHERE numero_solicitud = "+numeroSolicitud+" and tipo = '"+ConstantesIntegridadDominio.acronimoMotivoConsulta+"' order by fecha desc, hora desc";
			 st = con.createStatement( );
			
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				String observaciones = rs.getString("valor") + "\n";
				//observaciones += rs.getString("fecha") + " - " + rs.getString("hora") + "\n";
				//UsuarioBasico usuario = new UsuarioBasico();
				//usuario.cargarUsuarioBasico(con,rs.getString("usuario"));
				//observaciones += usuario.getInformacionGeneralPersonalSalud() + "\n\n";
				
				anamnesis += observaciones;
			}
				
				
			return anamnesis;
		}
		catch(SQLException e)
		{
			logger.error("Error en getUltimaAnamnesisValoracion: "+e);
			return "";
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta la ultima referencia  del paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static String getUltimaReferenciaPaciente(Connection con,String codigoPaciente,String tipoReferencia,String estado)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			String numeroReferencia = "";
			String consulta = "SELECT " +
				"numero_referencia " +
				"FROM referencia " +
				"WHERE codigo_paciente = ? and " +
				"referencia = '"+tipoReferencia+"' and " +
				"estado = '"+estado+"' " +
				"ORDER BY fecha_referencia desc, hora_referencia desc";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
			
			 rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numeroReferencia = rs.getString("numero_referencia");
			
			return numeroReferencia;
		}
		catch(SQLException e)
		{
			logger.error("Error en getUltimaReferenciaEnTramitePaciente: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	
	}
	
	/**
	 * Método que actualiza el estado de la referencia
	 * @param con
	 * @param numeroReferencia
	 * @param ingreso
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public static boolean actualizarEstadoReferencia(Connection con,String numeroReferencia,String ingreso,String estado,String loginUsuario)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			boolean exito = false;
			String consulta = "UPDATE " +
				"referencia SET " +
				"estado = '"+estado+"' , " +
				"usuario_modifica = '"+loginUsuario+"' , " +
				"fecha_modifica = CURRENT_DATE, " +
				"hora_modifica = '"+UtilidadFecha.getHoraActual()+"', " +
				"ingreso = " +ingreso +" "+ 
				"where numero_referencia = "+numeroReferencia;
			
			 st = con.createStatement( );
			
			if(st.executeUpdate(consulta)>0)
				exito = true;
			
			return exito;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarEstadoReferencia: "+e);
			return false;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta el estado de la referencia de un ingreso
	 * Nota* Si retorna cadena vacía quiere decir que ese ingreso no tiene
	 * referencia asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getEstadoReferenciaXIngreso(Connection con,HashMap campos)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String estado = "";
			String consulta = "SELECT estado FROM referencia WHERE ingreso = "+campos.get("idIngreso")+" AND referencia = '"+campos.get("tipoReferencia")+"'";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				estado = rs.getString("estado");
			
			return estado;
		}
		catch(SQLException e)
		{
			logger.error("Error en getEstadoReferenciaXIngreso: "+e);
			return "";
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso que se debe postular
	 * en la valoracion de hospitalizacion en el caso de que se haya ingresado
	 * una referencia externa y el origen de la admision sea remitido
	 * @param con
	 * @param codigoCuenta
	 * Se retorna acronimo + separadorSplit + tipoCie + separadorSplit + nombre
	 * Si no encuentra nada retorna cadena vacía
	 */
	public static String getDxIngresoDeReferencia(Connection con,String codigoCuenta)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			int idIngreso = 0;
			int origenAdmision = 0;
			String numeroReferencia = "";
			String diagnostico = "";
			
			//1) Consultar el origen de admision y el ingreso de la cuenta----------------------
			String consulta = "SELECT origen_admision, id_ingreso FROM cuentas WHERE id = "+codigoCuenta;
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				idIngreso = rs.getInt("id_ingreso");
				origenAdmision = rs.getInt("origen_admision");
				
				//2) El origen de admision debe ser remitido
				if(origenAdmision == ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
				{
					//3) Consultar la referencia del ingreso
					numeroReferencia = getNumeroReferenciaIngreso(con, idIngreso+"", ConstantesIntegridadDominio.acronimoExterna);
					
					if(!numeroReferencia.equals(""))
					{
						//4) Se consulta el diagnostico de la referencia
						consulta = "SELECT " +
							"acronimo_diagnostico, " +
							"tipo_cie," +
							"getnombrediagnostico(acronimo_diagnostico,tipo_cie) AS nombre_diagnostico " +
							"FROM diagnosticos_referencia " +
							"WHERE numero_referencia = "+numeroReferencia+" AND principal = '"+ConstantesBD.acronimoSi+"'";
						st = con.createStatement( );
						rs =  new ResultSetDecorator(st.executeQuery(consulta));
						if(rs.next())
							diagnostico = rs.getString("acronimo_diagnostico") + ConstantesBD.separadorSplit + rs.getInt("tipo_cie") + ConstantesBD.separadorSplit + rs.getString("nombre_diagnostico");
					}
				}
			}
			
			return diagnostico;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getDxIngresoDeReferencia: "+e);
			return "";
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	
	/**
	 * Método que consulta el número de referencia de un ingreso dependiendo del tipo de referencia
	 * Nota: En el caso de que no encuentre nada retorna una cadena vacía
	 * @param con
	 * @param idIngreso
	 * @param tipoReferencia
	 * @return
	 */
	public static String getNumeroReferenciaIngreso(Connection con,String idIngreso,String tipoReferencia)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String numeroReferencia = "";
			String consulta = "SELECT numero_referencia FROM referencia WHERE " +
				"ingreso = "+idIngreso+" and " +
				"referencia = '"+tipoReferencia+"' and " +
				"estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'";
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				numeroReferencia = rs.getString("numero_referencia");
			
			return numeroReferencia;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getNumeroReferenciaIngreso: "+e);
			return "";
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que verifica si hay una contrarreferencia para dado un número de referencia
	 * @param con
	 * @param numeroReferencia
	 * @return
	 */
	public static boolean existeContrarreferencia(Connection con,String numeroReferencia)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			boolean existe = false;
			String consulta = "SELECT count(1) AS cuenta FROM contrarreferencia WHERE numero_referencia_contra = "+numeroReferencia;
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeContrarreferencia: "+e);
			return false;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/***
	 * Método que consulta los datos de la referencia de un ingreso para postular un mensaje
	 * de advertencia 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getMensajeReferenciaParaValidacion(Connection con,HashMap campos)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			HashMap resultados = new HashMap();
			String tipoReferencia = campos.get("tipoReferencia").toString();
			String consulta = "";
			 st = null;
			 rs = null;
			
			//Si no hay ingreso, se consulta a partir de la solicitud
			if(Utilidades.convertirAEntero(campos.get("idIngreso").toString())<=0)
			{
				consulta = "SELECT c.id_ingreso As ingreso FROM cuentas c INNER JOIN solicitudes s ON(s.cuenta=c.id) WHERE s.numero_solicitud = "+campos.get("numeroSolicitud");
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				if(rs.next())
					campos.put("idIngreso",rs.getInt("ingreso")+"");
			}
			
			consulta = "SELECT "+ 
				"r.numero_referencia, "+
				"r.profesional_salud, "+
				"r.estado, "+ 
				"CASE WHEN r.codigo_especialidad IS NULL THEN '' ELSE getnombreespecialidad(r.codigo_especialidad) END AS especialidad, "+ 
				"s.descripcion AS institucion_origen, " +
				"r.tipo_referencia AS tipo_referencia "+  
				"FROM referencia r "+ 
				"INNER JOIN instituciones_sirc s ON(s.codigo=r.institucion_sirc_solicita AND s.institucion=r.institucion) "+ 
				"WHERE " +
				"r.ingreso = "+campos.get("idIngreso")+" AND " +
				"r.referencia = '"+tipoReferencia+"' AND " +
				"r.estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'";
			st = con.createStatement( );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
			
			if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())>0&&tipoReferencia.equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				//Se consulta su contrarreferencia
				consulta = " SELECT numero_referencia_contra,estado from contrarreferencia WHERE numero_referencia_contra = "+resultados.get("numeroReferencia");
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				if(rs.next())
				{
					resultados.put("estadoContra",rs.getString("estado"));
					resultados.put("tieneContra",ConstantesBD.acronimoSi);
				}
				else
				{
					resultados.put("estadoContra","");
					resultados.put("tieneContra",ConstantesBD.acronimoNo);
				}
			}
			else
			{
				//Le referencia no tiene contrarreferencia
				resultados.put("estadoContra","");
				resultados.put("tieneContra",ConstantesBD.acronimoNo);
				
			}
			
			//*******EDICION DEL MENSAJE******************************************************
			String mensaje = "";
			
			//Solo se muestra mensaje para referencias internas y externas que tienen pendiente la contrarreferencia
			if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())>0)
			{
				mensaje = "Paciente con Referencia "+ValoresPorDefecto.getIntegridadDominio(tipoReferencia)+
					" en estado "+ValoresPorDefecto.getIntegridadDominio(resultados.get("estado").toString())+" de "+
					resultados.get("institucionOrigen").toString()+", "+resultados.get("profesionalSalud")+", "+resultados.get("especialidad");
				
				//Para la referencia externa hay un mensaje adicional
				if(tipoReferencia.equals(ConstantesIntegridadDominio.acronimoExterna))
				{
					if(UtilidadTexto.getBoolean(resultados.get("tieneContra").toString()))
					{
						if(resultados.get("estadoContra").toString().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
							mensaje += ". Favor finalizar Nota de Contrarreferencia.";
						else
							mensaje = ""; //si ya está todo diligenciado en la referencia externa no se muestra mensaje
					}
					else
						mensaje += ". Favor diligenciar Nota de Contrarreferencia.";
				}
			}
			//**********************************************************************************
			
			return mensaje;
		}
		catch(SQLException e)
		{
			logger.error("Error en getMensajeReferenciaParaValidacion: "+e);
			return "";
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerEstadoCuenta(Connection con, int idCuenta) 
	{
		String cadena="SELECT estado_cuenta,nombre from cuentas c inner join estados_cuenta ec on(ec.codigo=c.estado_cuenta) where c.id=?";
		InfoDatosInt resultado=new InfoDatosInt();
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, idCuenta);
			 rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=new InfoDatosInt(rs.getInt(1),rs.getString(2));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionCuenta(Connection con, int idCuenta) 
	{ 
		String cadena="SELECT   ca.consecutivo,ca.descripcion from cuentas c inner join centros_costo cc on(c.area=cc.codigo) inner join centro_atencion ca on(ca.consecutivo=cc.centro_atencion) where c.id=?";
		InfoDatosInt resultado=new InfoDatosInt();
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, idCuenta);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=new InfoDatosInt(rs.getInt(1),rs.getString(2));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}

	/**
	 * 
	 * @param codigoIngreso
	 * @param todos 
	 * @param excluirResponsables 
	 * @param codigoViaIngreso 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<DtoSubCuentas> obtenerResponsablesIngreso(Connection con,int codigoIngreso, boolean todos, String[] excluirResponsables,boolean pyp, String subCuenta, int codigoViaIngreso)  throws BDException
	{
		ArrayList resultado=new ArrayList();
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		
		try	{
			//logger.info("############## Inicio obtenerResponsablesIngreso");
			String cadenaConsultaResponsable="SELECT " +
													" sc.sub_cuenta as subcuenta," +
													" sc.ingreso as ingreso," +
													" sc.convenio as codigoconvenio," +
													" CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then c.nombre else carterapaciente.getNomDeudorIngreso(sc.ingreso) end AS descripcionconvenio, " +													
													" sc.tipo_afiliado as tipoafiliado," +
													" sc.clasificacion_socioeconomica as clasificacion," +
													" sc.nro_autorizacion as nroautorizacion," +
													" sc.monto_cobro as montocobro," +
													" sc.naturaleza_paciente as naturalezapaciente," +
													" sc.nro_carnet as nrocarnet," +
													" sc.nro_poliza as nropoliza," +
													" sc.fecha_afiliacion as fechaafiliacion," +
													" sc.semanas_cotizacion as semanascotizacion," +
													" sc.meses_cotizacion as mesescotizacion," +
													" sc.codigo_paciente as codigopaciente," +
													" sc.contrato as contrato," +
													" con.numero_contrato as numerocontrato," +
													" c.maneja_presup_capitacion as convmanejapresup," +
													" sc.valor_utilizado_soat as valorutilizadosoat," +
													" sc.nro_prioridad as nroprioridad," +
													" sc.porcentaje_autorizado as porcentajeautorizado," +
													" sc.porcentaje_monto_cobro as porcentajemontocobro," +
													" sc.monto_autorizado as montoautorizado," +
												 	" dm.tipo_detalle as tipodetallemonto," +
												 	" dm.tipo_monto_codigo as tipomontocobro," +
												 	" dmg.valor as valormontogeneral," +
												 	" dmg.cantidad_monto as cantidadmontogeneral," +
												 	" dmg.porcentaje as porcentajemontogeneral," +		 
													" sc.obs_parametros_distribucion as obsparamdistribucion," +
													" sc.facturado as facturado, " +
													" coalesce(sc.numero_solicitud_volante||'','') as numero_solicitud_volante, " +
													" coalesce(sc.tipo_cobertura,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_cobertura, " +
													" coalesce(getnombrecobertura(sc.tipo_cobertura),'') as nombre_tipo_cobertura, " +
													" vrat.codigo AS codigo_accidente_transito " +
												" from sub_cuentas sc " +
												" INNER JOIN convenios c ON (c.codigo=sc.convenio) " +
												" INNER JOIN contratos con ON (con.codigo=sc.contrato) " +
												" LEFT OUTER JOIN manejopaciente.view_registro_accid_transito vrat ON(sc.convenio=vrat.aseguradora AND sc.ingreso=vrat.ingreso) " + 
												" LEFT OUTER JOIN detalle_monto dm ON (dm.detalle_codigo=sc.monto_cobro) " +
												" LEFT OUTER JOIN detalle_monto_general dmg ON (dmg.detalle_codigo=sc.monto_cobro)";
				
			cadenaConsultaResponsable+=" where sc.ingreso=?  ";
			
			//if(!todos)
		//	{
			//	cadenaConsultaResponsable+=" and sc.facturado='"+ConstantesBD.acronimoNo+"'";
		//	}
			
			if(excluirResponsables.length>0)
			{
				cadenaConsultaResponsable+=" and sc.sub_cuenta NOT IN("+Utilidades.convertirALong(excluirResponsables[0])+"";
				for(int i=1;i<excluirResponsables.length;i++)
				{
					cadenaConsultaResponsable+=","+Utilidades.convertirALong(excluirResponsables[i]);
				}
				cadenaConsultaResponsable+=")";
			}
			
			if(!UtilidadTexto.isEmpty(subCuenta))
			{
				cadenaConsultaResponsable+=" and sc.sub_cuenta = "+Utilidades.convertirALong(subCuenta)+" ";
			}
			
			if(pyp)
				cadenaConsultaResponsable+=" AND c.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			
			
			pst= con.prepareStatement(cadenaConsultaResponsable+" order by nroprioridad");
			pst.setInt(1, codigoIngreso);
			rs=pst.executeQuery();
			while(rs.next())
			{
				DtoSubCuentas dto=new DtoSubCuentas();
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"),rs.getString("descripcionconvenio")==null?"":rs.getString("descripcionconvenio")));
				dto.setTipoAfiliado(rs.getString("tipoafiliado")==null?"":rs.getString("tipoafiliado"));
				dto.setClasificacionSocioEconomica(rs.getInt("clasificacion"));
				dto.setNroAutorizacion(rs.getString("nroautorizacion")==null?"":rs.getString("nroautorizacion"));
				dto.setMontoCobro(rs.getInt("montocobro"));
				dto.setNaturalezaPaciente(rs.getInt("naturalezapaciente"));
				dto.setNroCarnet(rs.getString("nrocarnet")==null?"":rs.getString("nrocarnet"));
				dto.setNroPoliza(rs.getString("nropoliza")==null?"":rs.getString("nropoliza"));
				dto.setFechaAfiliacion(rs.getString("fechaafiliacion")==null?"":UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaafiliacion")));
				dto.setSemanasCotizacion(rs.getInt("semanascotizacion"));
				dto.setMesesCotizacion(rs.getInt("mesescotizacion"));
				dto.setCodigoPaciente(rs.getInt("codigopaciente"));
				dto.setContrato(rs.getInt("contrato"));
				dto.setNumeroContrato(rs.getString("numerocontrato"));
				dto.getDatosConvenio().setManejaPreupuestoCapitacion(rs.getString("convmanejapresup"));
				dto.setValorUtilizadoSoat(rs.getString("valorutilizadosoat")==null?"":rs.getString("valorutilizadosoat"));
				dto.setNroPrioridad(rs.getInt("nroprioridad"));
				dto.setPorcentajeAutorizado(rs.getString("porcentajeautorizado")==null?"":rs.getString("porcentajeautorizado"));
				dto.setMontoAutorizado(rs.getString("montoautorizado")==null?"":rs.getString("montoautorizado"));
				dto.setObsParametrosDistribucion(rs.getString("obsparamdistribucion")==null?"":rs.getString("obsparamdistribucion"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				dto.setNumeroSolicitudVolante(rs.getString("numero_solicitud_volante"));
				dto.setCodigoTipoCobertura(rs.getInt("codigo_tipo_cobertura"));
				dto.setNombreTipoCobertura(rs.getString("nombre_tipo_cobertura"));
				dto.setCodigoRegistroAccidenteTransito(rs.getLong("codigo_accidente_transito"));
				dto.setPorcentajeMontoCobro(rs.getDouble("porcentajemontocobro"));
				dto.setTipoDetalleMonto(rs.getString("tipodetallemonto"));
				dto.setValorMontoGeneral(rs.getDouble("valormontogeneral"));
				dto.setCantidadMontoGeneral(rs.getInt("cantidadmontogeneral"));
				dto.setPorcentajeMontoGeneral(rs.getDouble("porcentajemontogeneral"));
				dto.setTipoMonto(rs.getInt("tipomontocobro"));
			
				
				
				//*******************************SE CARGAN LOS REQUISITOS DEL PACIENTE DE CADA RESPONSABLE***********************************
				if(codigoViaIngreso<=0)
					codigoViaIngreso = obtenerViaIngresoCuentaActivaIngreso(con,dto.getIngreso());
				
				//Se consulta primero los requisitos registrados del responsables
				String consultaRequisitos="SELECT " +
					"rp.codigo as codigo, " +
					"rp.descripcion as descripcion," +
					"rp.tipo_requisito as tiporegistro," +
					"rps.cumplido as cumplido," +
					"'"+ConstantesBD.acronimoSi+"' as asignado " +
					"FROM requisitos_pac_subcuenta rps "+
					"inner join requisitos_paciente rp on(rp.codigo=rps.requisito_paciente)  " +
					"where rps.subcuenta = ?";
				
				pst2= con.prepareStatement(consultaRequisitos);
				pst2.setInt(1, Utilidades.convertirAEntero(dto.getSubCuenta()));
				rs2=pst2.executeQuery();
				ArrayList<DtoRequsitosPaciente> arrayRequisitos=new ArrayList<DtoRequsitosPaciente>();
				String listadoRequisitos = "";
				while(rs2.next())
				{
					DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
					requisito.setSubCuenta(dto.getSubCuenta());
					requisito.setCodigo(rs2.getInt("codigo"));
					listadoRequisitos += (listadoRequisitos.equals("")?"":",") + requisito.getCodigo(); 
					requisito.setDescripcion(rs2.getString("descripcion"));
					requisito.setTipo(rs2.getString("tiporegistro"));
					requisito.setCumplido(UtilidadTexto.getBoolean(rs2.getString("cumplido")));
					requisito.setAsignado(UtilidadTexto.getBoolean(rs2.getString("asignado")));
					arrayRequisitos.add(requisito);
				}
				rs2.close();
				pst2.close();
				
				//Se consultan los requisitos parametrizados que no están registrados aún en la subcuenta
				consultaRequisitos = "SELECT " +
					"rp.codigo as codigo, " +
					"rp.descripcion as descripcion," +
					"rp.tipo_requisito as tiporegistro," +
					ValoresPorDefecto.getValorFalseParaConsultas()+" as cumplido," +
					"'"+ConstantesBD.acronimoNo+"' as asignado " +
					"from requisitos_pac_convenio rpc " +
					"inner join requisitos_paciente rp on(rp.codigo=rpc.requisito_paciente)  " + 
					"where rpc.convenio=? and rpc.via_ingreso = ? ";
				if(!listadoRequisitos.equals(""))
					consultaRequisitos += "and rpc.requisito_paciente NOT IN ("+listadoRequisitos+") ";
				pst3= con.prepareStatement(consultaRequisitos);
				pst3.setInt(1,dto.getConvenio().getCodigo());
				pst3.setInt(2,codigoViaIngreso);
				rs3=pst3.executeQuery();
				while(rs3.next())
				{
					DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
					requisito.setSubCuenta(dto.getSubCuenta());
					requisito.setCodigo(rs3.getInt("codigo")); 
					requisito.setDescripcion(rs3.getString("descripcion"));
					requisito.setTipo(rs3.getString("tiporegistro"));
					requisito.setCumplido(UtilidadTexto.getBoolean(rs3.getString("cumplido")));
					requisito.setAsignado(UtilidadTexto.getBoolean(rs3.getString("asignado")));
					arrayRequisitos.add(requisito);
				}
				rs3.close();
				pst3.close();
				
				dto.setRequisitosPaciente(arrayRequisitos);
				//*******************************************************************************************				
				resultado.add(dto);
			}
		}
		catch(SQLException sqe)	{
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}finally{
			try {
				if(pst!=null ){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst2!=null){
					pst2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst3!=null){
					pst3.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs2!=null){
					rs2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		Log4JManager.info("############## Fin obtenerResponsablesIngreso");
		return resultado;
	}
	
	/**
	 * Método para consulta el cdodigo de la via de ingreso de la cuenta activa 
	 * del ingreso
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	private static int obtenerViaIngresoCuentaActivaIngreso(Connection con,int codigoIngreso) throws BDException
	{
		int viaIngreso = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			Log4JManager.info("############## Inicio obtenerViaIngresoCuentaActivaIngreso");
			String consulta = "SELECT " +
				"via_ingreso " +
				"from cuentas " +
				"where id_ingreso = ? and " +
				"getcuentafinalasocio(id_ingreso,id) is null and " +
				"estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada;
			
			pst = con.prepareStatement(consulta);
			pst.setInt(1,codigoIngreso);
			rs = pst.executeQuery();
			if(rs.next()){
				viaIngreso = rs.getInt("via_ingreso");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		Log4JManager.info("############## Fin obtenerViaIngresoCuentaActivaIngreso");
		return viaIngreso;
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static DtoSubCuentas obtenerResponsable(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		DtoSubCuentas dto=new DtoSubCuentas();
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		try
		{
			logger.info("############## Inicio obtenerResponsable");
			String cadenaConsultaResponsable="SELECT " +
													" sub_cuenta as subcuenta," +
													" ingreso as ingreso," +
													" convenio as codigoconvenio," +
													" getnombreconvenio(convenio) as descripcionconvenio," +
													" tipo_afiliado as tipoafiliado," +
													" clasificacion_socioeconomica as clasificacion," +
													" nro_autorizacion as nroautorizacion," +
													" monto_cobro as montocobro," +
													" naturaleza_paciente as naturalezapaciente," +
													" nro_carnet as nrocarnet," +
													" nro_poliza as nropoliza," +
													" fecha_afiliacion as fechaafiliacion," +
													" semanas_cotizacion as semanascotizacion," +
													" codigo_paciente as codigopaciente," +
													" contrato as contrato," +
													" valor_utilizado_soat as valorutilizadosoat," +
													" nro_prioridad as nroprioridad," +
													" porcentaje_autorizado as porcentajeautorizado," +
													" monto_autorizado as montoautorizado," +
													" obs_parametros_distribucion as obsparamdistribucion," +
													" facturado as facturado " +
												" from sub_cuentas " +
												" where ingreso=? and convenio=?";
			pst= con.prepareStatement(cadenaConsultaResponsable);
			pst.setInt(1, codigoIngreso);
			pst.setInt(2, codigoConvenio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"),rs.getString("descripcionconvenio")==null?"":rs.getString("descripcionconvenio")));
				dto.setTipoAfiliado(rs.getString("tipoafiliado")==null?"":rs.getString("tipoafiliado"));
				dto.setClasificacionSocioEconomica(rs.getInt("clasificacion"));
				dto.setNroAutorizacion(rs.getString("nroautorizacion")==null?"":rs.getString("nroautorizacion"));
				dto.setMontoCobro(rs.getInt("montocobro"));
				dto.setNaturalezaPaciente(rs.getInt("naturalezapaciente"));
				dto.setNroCarnet(rs.getString("nrocarnet")==null?"":rs.getString("nrocarnet"));
				dto.setNroPoliza(rs.getString("nropoliza")==null?"":rs.getString("nropoliza"));
				dto.setFechaAfiliacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaafiliacion")==null?"":rs.getString("fechaafiliacion")));
				dto.setSemanasCotizacion(rs.getInt("semanascotizacion"));
				dto.setCodigoPaciente(rs.getInt("codigopaciente"));
				dto.setContrato(rs.getInt("contrato"));
				dto.setValorUtilizadoSoat(rs.getString("valorutilizadosoat")==null?"0":rs.getString("valorutilizadosoat"));
				dto.setNroPrioridad(rs.getInt("nroprioridad"));
				dto.setPorcentajeAutorizado(rs.getString("porcentajeautorizado")==null?"0":rs.getString("porcentajeautorizado"));
				dto.setMontoAutorizado(rs.getString("montoautorizado")==null?"0":rs.getString("montoautorizado"));
				dto.setObsParametrosDistribucion(rs.getString("obsparamdistribucion")==null?"":rs.getString("obsparamdistribucion"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				
				String consultaRequisitos="SELECT rp.codigo as codigo, rp.descripcion as descripcion,rp.tipo_requisito as tiporegistro,rps.cumplido as cumplido,case when rps.subcuenta is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end  as asignado from requisitos_pac_convenio rpc inner join requisitos_paciente rp on(rp.codigo=rpc.requisito_paciente)  left outer join requisitos_pac_subcuenta rps on(rps.requisito_paciente=rpc.requisito_paciente and subcuenta=?) where convenio=?";
				
				pst2= con.prepareStatement(consultaRequisitos);
				pst2.setString(1, dto.getSubCuenta());
				pst2.setInt(2, dto.getConvenio().getCodigo());
				rs2=pst2.executeQuery();
				ArrayList<DtoRequsitosPaciente> arrayRequisitos=new ArrayList<DtoRequsitosPaciente>();
				while(rs2.next())
				{
					DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
					requisito.setSubCuenta(dto.getSubCuenta());
					requisito.setCodigo(rs2.getInt("codigo"));
					requisito.setDescripcion(rs2.getString("descripcion"));
					requisito.setTipo(rs2.getString("tiporegistro"));
					requisito.setCumplido(UtilidadTexto.getBoolean(rs2.getString("cumplido")));
					requisito.setAsignado(UtilidadTexto.getBoolean(rs2.getString("asignado")));
					arrayRequisitos.add(requisito);
				}
				dto.setRequisitosPaciente(arrayRequisitos);
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerResponsable",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerResponsable", e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		logger.info("############## Fin obtenerResponsable");
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param codigoSubcuenta
	 * @return
	 */
	public static DtoSubCuentas obtenerResponsable(Connection con, int codigoSubcuenta) throws BDException
	{
		DtoSubCuentas dto=new DtoSubCuentas();
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerResponsable");
			String cadenaConsultaResponsable="SELECT " +
													" sub_cuenta as subcuenta," +
													" ingreso as ingreso," +
													" convenio as codigoconvenio," +
													" getnombreconvenio(convenio) as descripcionconvenio," +
													" tipo_afiliado as tipoafiliado," +
													" clasificacion_socioeconomica as clasificacion," +
													" nro_autorizacion as nroautorizacion," +
													" monto_cobro as montocobro," +
													" naturaleza_paciente as naturalezapaciente," +
													" nro_carnet as nrocarnet," +
													" nro_poliza as nropoliza," +
													" fecha_afiliacion as fechaafiliacion," +
													" semanas_cotizacion as semanascotizacion," +
													" codigo_paciente as codigopaciente," +
													" contrato as contrato," +
													" valor_utilizado_soat as valorutilizadosoat," +
													" nro_prioridad as nroprioridad," +
													" porcentaje_autorizado as porcentajeautorizado," +
													" monto_autorizado as montoautorizado," +
													" obs_parametros_distribucion as obsparamdistribucion," +
													" facturado as facturado " +
												" from sub_cuentas " +
												" where sub_cuenta=?";
			pst= con.prepareStatement(cadenaConsultaResponsable);
			pst.setInt(1, codigoSubcuenta);
			
			rs=pst.executeQuery();
			if(rs.next())
			{
				
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"),rs.getString("descripcionconvenio")==null?"":rs.getString("descripcionconvenio")));
				dto.setTipoAfiliado(rs.getString("tipoafiliado")==null?"":rs.getString("tipoafiliado"));
				dto.setClasificacionSocioEconomica(rs.getInt("clasificacion"));
				dto.setNroAutorizacion(rs.getString("nroautorizacion")==null?"":rs.getString("nroautorizacion"));
				dto.setMontoCobro(rs.getInt("montocobro"));
				dto.setNaturalezaPaciente(rs.getInt("naturalezapaciente"));
				dto.setNroCarnet(rs.getString("nrocarnet")==null?"":rs.getString("nrocarnet"));
				dto.setNroPoliza(rs.getString("nropoliza")==null?"":rs.getString("nropoliza"));
				dto.setFechaAfiliacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaafiliacion")==null?"":rs.getString("fechaafiliacion")));
				dto.setSemanasCotizacion(rs.getInt("semanascotizacion"));
				dto.setCodigoPaciente(rs.getInt("codigopaciente"));
				dto.setContrato(rs.getInt("contrato"));
				dto.setValorUtilizadoSoat(rs.getString("valorutilizadosoat")==null?"0":rs.getString("valorutilizadosoat"));
				dto.setNroPrioridad(rs.getInt("nroprioridad"));
				dto.setPorcentajeAutorizado(rs.getString("porcentajeautorizado")==null?"0":rs.getString("porcentajeautorizado"));
				dto.setMontoAutorizado(rs.getString("montoautorizado")==null?"0":rs.getString("montoautorizado"));
				dto.setObsParametrosDistribucion(rs.getString("obsparamdistribucion")==null?"":rs.getString("obsparamdistribucion"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				
				String consultaRequisitos="SELECT rp.codigo as codigo, rp.descripcion as descripcion,rp.tipo_requisito as tiporegistro,rps.cumplido as cumplido,case when rps.subcuenta is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end  as asignado from requisitos_pac_convenio rpc inner join requisitos_paciente rp on(rp.codigo=rpc.requisito_paciente)  left outer join requisitos_pac_subcuenta rps on(rps.requisito_paciente=rpc.requisito_paciente and subcuenta=?) where convenio=?";
				
				pst2= con.prepareStatement(consultaRequisitos);
				pst2.setString(1, dto.getSubCuenta());
				pst2.setInt(2, dto.getConvenio().getCodigo());
				rs2=pst2.executeQuery();
				ArrayList<DtoRequsitosPaciente> arrayRequisitos=new ArrayList<DtoRequsitosPaciente>();
				while(rs2.next())
				{
					DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
					requisito.setSubCuenta(dto.getSubCuenta());
					requisito.setCodigo(rs2.getInt("codigo"));
					requisito.setDescripcion(rs2.getString("descripcion"));
					requisito.setTipo(rs2.getString("tiporegistro"));
					requisito.setCumplido(UtilidadTexto.getBoolean(rs2.getString("cumplido")));
					requisito.setAsignado(UtilidadTexto.getBoolean(rs2.getString("asignado")));
					arrayRequisitos.add(requisito);
				}
				dto.setRequisitosPaciente(arrayRequisitos);
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}		finally{
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs2!=null){
					rs2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst2!=null){
					pst2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		Log4JManager.info("############## Fin obtenerResponsable");
		return dto;
	}
	
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuenta(Connection con, String subCuenta, int[] estados,boolean incluirPaquetizadas, boolean agruparPortatiles) 
	{
		ArrayList resultado=new ArrayList();
		String cadenaConsultaSolSubcuenta="";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			if(!agruparPortatiles)
			{
				cadenaConsultaSolSubcuenta="SELECT " +
												" ss.codigo as codigo," +
												" to_char(getFechaSolicitud(ss.solicitud),'dd/mm/yyyy') as fechasolicitud," +
												" ss.solicitud as solicitud," +
												" ss.sub_cuenta as subcuenta," +
												" ss.servicio as servicio," +
												" getnombreservicio(ss.servicio,"+ConstantesBD.codigoTarifarioCups+") as nomservicio," +
												" gettieneportatilsolicitud(ss.solicitud,dc.servicio) AS codigoportatil,"+
												" getnombreservicio(gettieneportatilsolicitud(ss.solicitud,dc.servicio),"+ConstantesBD.codigoTarifarioCups+") as nomservicioportatil," +
												" ss.articulo as articulo," +
												" getdescripcionarticulo(ss.articulo) as nomarticulo," +
												" ss.cuenta as cuenta," +
												" ss.cantidad as cantidad," +
												" ss.cubierto as cubierto," +
												" ss.monto as monto," +
												" ss.tipo_solicitud as tiposolicitud," +
												" case when ss.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" then getintegridaddominio(getindicadorqxsolicitud(ss.solicitud)) else getnomtiposolicitud(ss.tipo_solicitud) end  as nomtiposolicitud," +
												" ss.paquetizada as paquetizada, " +
												" ss.sol_subcuenta_padre as solsubcuentapadre," +
												" ss.servicio_cx as serviciocx," +
												" getnombreservicio(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as nomserviciocx," +
												" ss.tipo_asocio as tipoasocio," +
												" getnombretipoasocio(ta.codigo) as nomtipoasocio," +
												" ss.porcentaje as porcentaje," +
												" getConsecutivoSolicitud(ss.solicitud) as consecutivosolicitud," +
												" getCodigoCCSolicita(ss.solicitud) as codccsolicita," +
												" getnomcentrocosto(getCodigoCCSolicita(ss.solicitud)) as nomccsolicita," +
												" getCodigoCCEjecuta(ss.solicitud) as codccejecuta," +
												" getnomcentrocosto(getCodigoCCEjecuta(ss.solicitud)) as nomccejecuta," +
												" getNumRespSolServicio(ss.solicitud,ss.servicio) as numresservicio, " +
												" getNumRespFacSolServicio(ss.solicitud,ss.servicio) as numresfactservicio," +
												" getNumRespSolArticulo(ss.solicitud,ss.articulo) as numresarticulo," +
												" getNumRespFacSolArticulo(ss.solicitud,ss.articulo) as numresfactarticulo," +
												" ss.tipo_distribucion as tipodistribucion, " +
												" dc.cubierto as cubierto," +
												//campos de cargos.
												" dc.codigo_detalle_cargo as codigodetallecargo, " +
												" dc.convenio as convenio," +
												" dc.esquema_tarifario as esquematarifario," +
												" dc.cantidad_cargada as cantidadcargada," +
												" dc.valor_unitario_tarifa as valunitariotarifa," +
												" dc.valor_unitario_cargado as valunitariocargado," +
												" dc.valor_total_cargado as valtotalcargado," +
												" dc.porcentaje_cargado as porcentajecargado," +
												" dc.porcentaje_recargo as porcentajerecargo," +
												" dc.valor_unitario_recargo as valunitariorecargo," +
												" dc.porcentaje_dcto as porcentajedcto, " +
												" dc.valor_unitario_dcto as valunitariodcto," +
												" dc.valor_unitario_iva as valunitarioiva," +
												" dc.nro_autorizacion as nroautorizacion," +
												" dc.estado as estado," +
												" getestadosolfac(dc.estado) as descestado," +
												" dc.cargo_padre as cargopadre," +
												
												//Cambio por que en la Anulacion de la Factura no se esta Actualizando este campo
												//Se presento el error en la Distribución. Tarea 52550, la cual indica que después
												//de anular una factura se sigue presentando el indicativo "FACTURADO"
												//" dc.facturado as facturado," +
												"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
													"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
														"'"+ConstantesBD.acronimoSi+"'" + 
													"ELSE " +
														"'"+ConstantesBD.acronimoNo+"'" + 
													"END) " +
												"ELSE " + 
													"'"+ConstantesBD.acronimoNo+"'" +
												"END AS facturado, " +
												
												" dc.observaciones as observacionescargo " +
											" from solicitudes_subcuenta ss " +
											" inner join det_cargos dc on (dc.cod_sol_subcuenta=ss.codigo) " +
											" left outer join facturas fac on (dc.codigo_factura = fac.codigo) " +
											" left outer join tipos_asocio ta on (ta.codigo=ss.tipo_asocio) " +
											" where ss.sub_cuenta=? ";
			}
			else
			{
								cadenaConsultaSolSubcuenta="SELECT " +
												" ss.codigo as codigo," +
												" to_char(getFechaSolicitud(ss.solicitud),'dd/mm/yyyy') as fechasolicitud," +
												" ss.solicitud as solicitud," +
												" ss.sub_cuenta as subcuenta," +
												" ss.servicio as servicio," +
												" getnombreservicio(ss.servicio,"+ConstantesBD.codigoTarifarioCups+") as nomservicio," +
												" gettieneportatilsolicitud(ss.solicitud,dc.servicio) AS codigoportatil,"+
												" getnombreservicio(gettieneportatilsolicitud(ss.solicitud,dc.servicio),"+ConstantesBD.codigoTarifarioCups+") as nomservicioportatil," +
												" ss.articulo as articulo," +
												" getdescripcionarticulo(ss.articulo) as nomarticulo," +
												" ss.cuenta as cuenta," +
												" ss.cantidad as cantidad," +
												" ss.cubierto as cubierto," +
												" ss.monto as monto," +
												" ss.tipo_solicitud as tiposolicitud," +
												" case when ss.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" then getintegridaddominio(getindicadorqxsolicitud(ss.solicitud)) else getnomtiposolicitud(ss.tipo_solicitud) end  as nomtiposolicitud," +
												" ss.paquetizada as paquetizada, " +
												" ss.sol_subcuenta_padre as solsubcuentapadre," +
												" ss.servicio_cx as serviciocx," +
												" getnombreservicio(ss.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") as nomserviciocx," +
												" ss.tipo_asocio as tipoasocio," +
												" getnombretipoasocio(ta.codigo) as nomtipoasocio," +
												" ss.porcentaje as porcentaje," +
												" getConsecutivoSolicitud(ss.solicitud) as consecutivosolicitud," +
												" getCodigoCCSolicita(ss.solicitud) as codccsolicita," +
												" getnomcentrocosto(getCodigoCCSolicita(ss.solicitud)) as nomccsolicita," +
												" getCodigoCCEjecuta(ss.solicitud) as codccejecuta," +
												" getnomcentrocosto(getCodigoCCEjecuta(ss.solicitud)) as nomccejecuta," +
												" getNumRespSolServicio(ss.solicitud,ss.servicio) as numresservicio, " +
												" getNumRespFacSolServicio(ss.solicitud,ss.servicio) as numresfactservicio," +
												" getNumRespSolArticulo(ss.solicitud,ss.articulo) as numresarticulo," +
												" getNumRespFacSolArticulo(ss.solicitud,ss.articulo) as numresfactarticulo," +
												" ss.tipo_distribucion as tipodistribucion, " +
												" dc.cubierto as cubierto," +
												//campos de cargos.
												" dc.codigo_detalle_cargo as codigodetallecargo, " +
												" dc.convenio as convenio," +
												" dc.esquema_tarifario as esquematarifario," +
												" dc.cantidad_cargada as cantidadcargada," +
												" dc.valor_unitario_tarifa as valunitariotarifa," +
												" dc.valor_unitario_cargado as valunitariocargado," +
												" dc.valor_total_cargado as valtotalcargado," +
												" dc.porcentaje_cargado as porcentajecargado," +
												" dc.porcentaje_recargo as porcentajerecargo," +
												" dc.valor_unitario_recargo as valunitariorecargo," +
												" dc.porcentaje_dcto as porcentajedcto, " +
												" dc.valor_unitario_dcto as valunitariodcto," +
												" dc.valor_unitario_iva as valunitarioiva," +
												" dc.nro_autorizacion as nroautorizacion," +
												" dc.estado as estado," +
												" getestadosolfac(dc.estado) as descestado," +
												" dc.cargo_padre as cargopadre," +
												
												//Cambio por que en la Anulacion de la Factura no se esta Actualizando este campo
												//Se presento el error en la Distribución. Tarea 52550, la cual indica que después
												//de anular una factura se sigue presentando el indicativo "FACTURADO"
												//" dc.facturado as facturado," +
												"CASE WHEN fac.estado_facturacion IS NOT NULL THEN " + 
													"(CASE WHEN fac.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" THEN " +
														"'"+ConstantesBD.acronimoSi+"'" + 
													"ELSE " +
														"'"+ConstantesBD.acronimoNo+"'" + 
													"END) " +
												"ELSE " + 
													"'"+ConstantesBD.acronimoNo+"'" +
												"END AS facturado, " +
												
												" dc.observaciones as observacionescargo " +
											" from solicitudes_subcuenta ss " +
											" inner join det_cargos dc on (dc.cod_sol_subcuenta=ss.codigo) " +
											" left outer join facturas fac on (dc.codigo_factura = fac.codigo) " +
											" left outer join tipos_asocio ta on (ta.codigo=ss.tipo_asocio) " +
											" where ss.sub_cuenta=? and es_portatil='"+ConstantesBD.acronimoNo+"'";
				
			}
											
											
			String filtroEstados="";
			if(estados.length>0)
			{
				filtroEstados=estados[0]+"";
				for(int i=1;i<estados.length;i++)
				{
					filtroEstados="','"+estados[i];
				}
				filtroEstados="";
			}
			
			if(!incluirPaquetizadas)
				cadenaConsultaSolSubcuenta+=" AND  ss.paquetizada='"+ConstantesBD.acronimoNo+"' ";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSolSubcuenta+filtroEstados+" order by 2"));
			ps.setLong(1, Utilidades.convertirALong(subCuenta));
			logger.info("===>Consulta: "+cadenaConsultaSolSubcuenta+filtroEstados+" order by 2");
			logger.info("===>SubCuenta: "+subCuenta);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("codigo")==null?"":rs.getString("codigo"));
				dto.setNumeroSolicitud(rs.getString("solicitud")==null?"":rs.getString("solicitud"));
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setServicio(new InfoDatosString(rs.getString("servicio")==null?"":rs.getString("servicio").trim(),rs.getString("nomservicio")==null?"":rs.getString("nomservicio").trim()));
				dto.setPortatil(new InfoDatosString((rs.getString("codigoportatil")==null&&Utilidades.convertirAEntero(rs.getString("codigoportatil")+"")<=0)?"":(rs.getObject("codigoportatil")+"").trim(),rs.getString("nomservicioportatil")==null?"":rs.getString("nomservicioportatil").trim()));
				dto.setArticulo(new InfoDatosString(rs.getString("articulo")==null?"":rs.getString("articulo").trim(),rs.getString("nomarticulo")==null?"":rs.getString("nomarticulo").trim()));
				dto.setCuenta(rs.getString("cuenta")==null?"":rs.getString("cuenta"));
				dto.setCantidad(rs.getString("cantidad")==null?"0":rs.getString("cantidad"));
				dto.setCubierto(rs.getString("cubierto")==null?"":rs.getString("cubierto"));
				dto.setMonto(rs.getString("monto")==null?"0":rs.getString("monto"));
				dto.setTipoSolicitud(new InfoDatosInt(rs.getInt("tiposolicitud"),rs.getString("nomtiposolicitud")==null?"":rs.getString("nomtiposolicitud")));
				dto.setPaquetizada(rs.getString("paquetizada")==null?"":rs.getString("paquetizada"));
				dto.setSolicitudPadre(rs.getString("solsubcuentapadre")==null?"":rs.getString("solsubcuentapadre"));
				dto.setServicioCX(new InfoDatosString(rs.getString("serviciocx")==null?"":rs.getString("serviciocx").trim(),rs.getString("nomserviciocx")==null?"":rs.getString("nomserviciocx").trim()));
				dto.setTipoAsocio(new InfoDatosInt(rs.getInt("tipoasocio"),rs.getString("nomtipoasocio")==null?"":rs.getString("nomtipoasocio")));
				dto.setProcentaje(rs.getString("porcentaje")==null?"0":rs.getString("porcentaje"));
				dto.setEstadoCargo(new InfoDatosInt(rs.getInt("estado"),rs.getString("descestado")==null?"":rs.getString("descestado")));
				dto.setTipoDistribucion(new InfoDatosString(rs.getString("tipodistribucion"),ValoresPorDefecto.getIntegridadDominio(rs.getString("tipodistribucion"))+""));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setConsecutivoSolicitud(rs.getString("consecutivosolicitud"));
				dto.setCentroCostoSolicita(new InfoDatosInt(rs.getInt("codccsolicita"),rs.getString("nomccsolicita")==null?"":rs.getString("nomccsolicita").trim()));
				dto.setCentroCostoEjecuta(new InfoDatosInt(rs.getInt("codccejecuta"),rs.getString("nomccejecuta")==null?"":rs.getString("nomccejecuta").trim()));
				dto.setNumResponsablesMismoServicio(rs.getInt("numresservicio"));
				dto.setNumResponsablesMismoArticulo(rs.getInt("numresarticulo"));
				dto.setNumResponsablesFacturadosMismoServicio(rs.getInt("numresfactservicio"));
				dto.setNumResponsablesFacturadosMismoArticulo(rs.getInt("numresfactarticulo"));
				dto.setCodigoDetalleCargo(UtilidadTexto.isEmpty(rs.getString("codigodetallecargo"))?"":rs.getString("codigodetallecargo"));
				dto.setConvenio(UtilidadTexto.isEmpty(rs.getString("convenio"))?"":rs.getString("convenio"));
				dto.setEsquemaTarifario(UtilidadTexto.isEmpty(rs.getString("esquematarifario"))?"":rs.getString("esquematarifario"));
				dto.setCantidadCargada(UtilidadTexto.isEmpty(rs.getString("cantidadcargada"))?"":rs.getString("cantidadcargada"));
				dto.setValorUnitarioTarifa(UtilidadTexto.isEmpty(rs.getString("valunitariotarifa"))?"":rs.getString("valunitariotarifa"));
				dto.setValorUnitarioCargado(UtilidadTexto.isEmpty(rs.getString("valunitariocargado"))?"":rs.getString("valunitariocargado"));
				dto.setValorTotalCargado(UtilidadTexto.isEmpty(rs.getString("valtotalcargado"))?"":rs.getString("valtotalcargado"));
				dto.setPorcentajeCargado(UtilidadTexto.isEmpty(rs.getString("porcentajecargado"))?"":rs.getString("porcentajecargado"));
				dto.setPorcentajeRecargo(UtilidadTexto.isEmpty(rs.getString("porcentajerecargo"))?"":rs.getString("porcentajerecargo"));
				dto.setValorUnitarioRecargo(UtilidadTexto.isEmpty(rs.getString("valunitariorecargo"))?"":rs.getString("valunitariorecargo"));
				dto.setPorcentajeDcto(UtilidadTexto.isEmpty(rs.getString("porcentajedcto"))?"":rs.getString("porcentajedcto"));
				dto.setValorUnitarioDcto(UtilidadTexto.isEmpty(rs.getString("valunitariodcto"))?"":rs.getString("valunitariodcto"));
				dto.setValorUnitarioIva(UtilidadTexto.isEmpty(rs.getString("valunitarioiva"))?"":rs.getString("valunitarioiva"));
				dto.setNroAutorizacion(UtilidadTexto.isEmpty(rs.getString("nroautorizacion"))?"":rs.getString("nroautorizacion"));
				dto.setCargoPadre(UtilidadTexto.isEmpty(rs.getString("cargopadre"))?"":rs.getString("cargopadre"));
				dto.setObservacionesCargo(UtilidadTexto.isEmpty(rs.getString("observacionescargo"))?"":rs.getString("observacionescargo"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				resultado.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static InfoDatosString obtenerEstadoIngreso(Connection con, int codigoIngreso) 
	{
		logger.info("\n entre a obtenerEstadoIngreso --> "+codigoIngreso);
		String cadena="SELECT  estado from ingresos where id=?";
		InfoDatosString resultado=new InfoDatosString();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			pst= con.prepareStatement(cadena);
			pst.setInt(1, codigoIngreso);
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=new InfoDatosString(rs.getString(1),ValoresPorDefecto.getIntegridadDominio(rs.getString(1))+"");
			}
		} 
		catch(Exception e){
			logger.error("############## ERROR obtenerEstadoIngreso", e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int cuentaTieneAsocioCompleto(Connection con, int codigoCuenta) {

		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			int cuenta = 0;
			String cadena="SELECT  c.id as id from cuentas c inner join ingresos i  on(c.id_ingreso=i.id) inner join asocios_cuenta ac on(ac.cuenta_final=c.id) where c.estado_cuenta= '"+ConstantesBD.codigoEstadoCuentaAsociada+"' ";  
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			pst.setInt(1,codigoCuenta);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				cuenta = rs.getInt("id");
			return cuenta;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuenta: "+e);
			return 0;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCargo
	 * @return
	 */
	public static InfoDatosInt obtenerEstadoDetalleCargo(Connection con, double codigoDetalleCargo) 
	{

		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		String cadena="SELECT estado as codigoestado,getestadosolfac(estado) as nombreestado from det_cargos where codigo_detalle_cargo =?";
		InfoDatosInt resultado=new InfoDatosInt();
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1, codigoDetalleCargo);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=new InfoDatosInt(rs.getInt(1),rs.getString(2));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @return
	 */
	public static boolean esResponsableFacturado(Connection con, int codigoSubCuenta) 
	{

		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT sub_cuenta as subCuenta from sub_cuentas where facturado='"+ConstantesBD.acronimoSi+"' ";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			return rs.next();
		}
		catch(SQLException e)
		{
			logger.error("Error: En esResponsableFacturado "+e);
			return false;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esResponsableFacturado(Connection con, int codigoIngreso, int codigoConvenio) 
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			
			String consulta = "SELECT ingreso as ingreso, convenio as convenio from sub_cuentas where facturado='"+ConstantesBD.acronimoSi+"' ";
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			return rs.next();
			
		}
		catch(SQLException e)
		{
			logger.error("Error: En esResponsableFacturado "+e);
			return false;
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso) {
		
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static ArrayList obtenerCodigoSolicitudesPendienteCargo(Connection con, int codigoIngreso, int codigoConvenio) {
		
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoViaIngreso 
	 * @return
	 */
	public static ArrayList<DtoRequsitosPaciente> obtenerRequisitosPacienteConvenio(Connection con, int codigoConvenio, int codigoViaIngreso) 
	{

		String consultaRequisitos="SELECT rp.codigo as codigo, rp.descripcion as descripcion,rp.tipo_requisito as tiporegistro,'"+ConstantesBD.acronimoNo+"' as cumplido,'"+ConstantesBD.acronimoNo+"' as asignado " +
			"from requisitos_pac_convenio rpc " +
			"inner join requisitos_paciente rp on(rp.codigo=rpc.requisito_paciente) " +
			"where rpc.convenio=? and rpc.via_ingreso = ?";
		ArrayList<DtoRequsitosPaciente> arrayRequisitos=new ArrayList<DtoRequsitosPaciente>();
		PreparedStatementDecorator psReq= null;
		ResultSetDecorator rsReq = null;
		try
		{
			psReq= new PreparedStatementDecorator(con.prepareStatement(consultaRequisitos));
			psReq.setInt(1,codigoConvenio);
			psReq.setInt(2,codigoViaIngreso);
			rsReq=new ResultSetDecorator(psReq.executeQuery());
			while(rsReq.next())
			{
				DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
				requisito.setSubCuenta(ConstantesBD.codigoNuncaValido+"");
				requisito.setCodigo(rsReq.getInt("codigo"));
				requisito.setDescripcion(rsReq.getString("descripcion"));
				requisito.setTipo(rsReq.getString("tiporegistro"));
				requisito.setCumplido(UtilidadTexto.getBoolean(rsReq.getString("cumplido")));
				requisito.setAsignado(UtilidadTexto.getBoolean(rsReq.getString("asignado")));
				arrayRequisitos.add(requisito);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(psReq!=null){
					psReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rsReq!=null){
					rsReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return arrayRequisitos;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codArtiServ
	 * @param esServicio
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> obtenerResponsablesSolServArt(Connection con, int numeroSolicitud, int codArtiServ, boolean esServicio) 
	{
		ArrayList resultado=new ArrayList();
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			String cadenaConsultaResponsable="SELECT " +
													" sc.sub_cuenta as subcuenta," +
													" sc.ingreso as ingreso," +
													" sc.convenio as codigoconvenio," +
													" getnombreconvenio(sc.convenio) as descripcionconvenio," +
													" sc.tipo_afiliado as tipoafiliado," +
													" sc.clasificacion_socioeconomica as clasificacion," +
													" sc.nro_autorizacion as nroautorizacion," +
													" sc.monto_cobro as montocobro," +
													" sc.naturaleza_paciente as naturalezapaciente," +
													" sc.nro_carnet as nrocarnet," +
													" sc.nro_poliza as nropoliza," +
													" sc.fecha_afiliacion as fechaafiliacion," +
													" sc.semanas_cotizacion as semanascotizacion," +
													" sc.codigo_paciente as codigopaciente," +
													" sc.contrato as contrato," +
													" sc.valor_utilizado_soat as valorutilizadosoat," +
													" sc.nro_prioridad as nroprioridad," +
													" sc.porcentaje_autorizado as porcentajeautorizado," +
													" sc.monto_autorizado as montoautorizado," +
													" sc.obs_parametros_distribucion as obsparamdistribucion," +
													" sc.facturado as facturado " +
												" from sub_cuentas sc " +
												" inner join solicitudes_subcuenta ss on (ss.sub_cuenta=sc.sub_cuenta) " +
												" where ss.solicitud=? AND ss.eliminado='"+ConstantesBD.acronimoNo+"' " ;
			
			if(esServicio)
			{
				if(codArtiServ>0)
					cadenaConsultaResponsable+=" and ss.servicio="+codArtiServ;
				else
					cadenaConsultaResponsable+=" and ss.servicio is null ";
			}
			else
			{
				if(codArtiServ>0)
					cadenaConsultaResponsable+=" and ss.articulo="+codArtiServ;
				else
					cadenaConsultaResponsable+=" and ss.articulo is null ";
			}
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaResponsable+" order by nro_prioridad"));
			ps.setInt(1, numeroSolicitud);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoSubCuentas dto=new DtoSubCuentas();
				dto.setSubCuenta(rs.getString("subcuenta")==null?"":rs.getString("subcuenta"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"),rs.getString("descripcionconvenio")==null?"":rs.getString("descripcionconvenio")));
				dto.setTipoAfiliado(rs.getString("tipoafiliado")==null?"":rs.getString("tipoafiliado"));
				dto.setClasificacionSocioEconomica(rs.getInt("clasificacion"));
				dto.setNroAutorizacion(rs.getString("nroautorizacion")==null?"":rs.getString("nroautorizacion"));
				dto.setMontoCobro(rs.getInt("montocobro"));
				dto.setNaturalezaPaciente(rs.getInt("naturalezapaciente"));
				dto.setNroCarnet(rs.getString("nrocarnet")==null?"":rs.getString("nrocarnet"));
				dto.setNroPoliza(rs.getString("nropoliza")==null?"":rs.getString("nropoliza"));
				dto.setFechaAfiliacion(rs.getString("fechaafiliacion")==null?"":UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaafiliacion")));
				dto.setSemanasCotizacion(rs.getInt("semanascotizacion"));
				dto.setCodigoPaciente(rs.getInt("codigopaciente"));
				dto.setContrato(rs.getInt("contrato"));
				dto.setValorUtilizadoSoat(rs.getString("valorutilizadosoat")==null?"":rs.getString("valorutilizadosoat"));
				dto.setNroPrioridad(rs.getInt("nroprioridad"));
				dto.setPorcentajeAutorizado(rs.getString("porcentajeautorizado")==null?"":rs.getString("porcentajeautorizado"));
				dto.setMontoAutorizado(rs.getString("montoautorizado")==null?"":rs.getString("montoautorizado"));
				dto.setObsParametrosDistribucion(rs.getString("obsparamdistribucion")==null?"":rs.getString("obsparamdistribucion"));
				dto.setFacturado(rs.getString("facturado")==null?"":rs.getString("facturado"));
				
				String consultaRequisitos="SELECT rp.codigo as codigo, rp.descripcion as descripcion,rp.tipo_requisito as tiporegistro,rps.cumplido as cumplido,case when rps.subcuenta is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end  as asignado from requisitos_pac_convenio rpc inner join requisitos_paciente rp on(rp.codigo=rpc.requisito_paciente)  left outer join requisitos_pac_subcuenta rps on(rps.requisito_paciente=rpc.requisito_paciente and subcuenta=?) where convenio=?";
				PreparedStatementDecorator psReq= new PreparedStatementDecorator(con.prepareStatement(consultaRequisitos));
				psReq.setString(1, dto.getSubCuenta());
				psReq.setInt(2, dto.getConvenio().getCodigo());
				ResultSetDecorator rsReq=new ResultSetDecorator(psReq.executeQuery());
				ArrayList<DtoRequsitosPaciente> arrayRequisitos=new ArrayList<DtoRequsitosPaciente>();
				while(rsReq.next())
				{
					DtoRequsitosPaciente requisito=new DtoRequsitosPaciente();
					requisito.setSubCuenta(dto.getSubCuenta());
					requisito.setCodigo(rsReq.getInt("codigo"));
					requisito.setDescripcion(rsReq.getString("descripcion"));
					requisito.setTipo(rsReq.getString("tiporegistro"));
					requisito.setCumplido(UtilidadTexto.getBoolean(rsReq.getString("cumplido")));
					requisito.setAsignado(UtilidadTexto.getBoolean(rsReq.getString("asignado")));
					arrayRequisitos.add(requisito);
				}
				dto.setRequisitosPaciente(arrayRequisitos);
				
				resultado.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}
	
	/**
	 * Método que consulta los convenios pyp de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConveniosPypIngreso(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT " +
				"c.codigo, " +
				"c.nombre," +
				"c.tipo_regimen " +
				"FROM sub_cuentas sc " +
				"INNER JOIN convenios c ON(c.codigo=sc.convenio) " +
				"WHERE sc.ingreso = "+campos.get("idIngreso")+" AND c.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				elemento.put("tipoRegimen", rs.getObject("tipo_regimen"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConveniosPypIngreso: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método que consulta el convenio PYP que tenga asociada la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoConvenio obtenerConvenioPypSolicitud(Connection con,String numeroSolicitud)
	{
		DtoConvenio convenio = new DtoConvenio();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			//Se consulta el convenio pyp asociado a la solicitud
			String consulta = "SELECT "+ 
			"conv.codigo As codigo_convenio, " +
			"conv.nombre AS nombre_convenio, "+
			"conv.tipo_regimen As tipo_regimen," +
			"getnomtiporegimen(conv.tipo_regimen) AS nombre_tipo_regimen "+ 
			"FROM solicitudes s " +
			"INNER JOIN solicitudes_subcuenta ss ON(ss.solicitud=s.numero_solicitud) " +
			"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=ss.sub_cuenta) "+
			"INNER JOIN convenios conv ON(conv.codigo=sc.convenio AND conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
			"WHERE s.numero_solicitud = ? AND ss.eliminado='"+ConstantesBD.acronimoNo+"' ";
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			 rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				convenio.setCodigo(rs.getInt("codigo_convenio"));
				convenio.setDescripcion(rs.getString("nombre_convenio"));
				convenio.setAcronimoTipoRegimen(rs.getString("tipo_regimen"));
				convenio.setDescripcionTipoRegimen(rs.getString("nombre_tipo_regimen"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenioPypSolicitud: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return convenio;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresosValidos(Connection con, int codigoPersona, int centroAtencion) 
	{
		PreparedStatementDecorator ps= null;

		String cadenaConsultaIngresoValidosDistribucion="SELECT " +
																			" i.id as idingreso," +
																			" i.estado as estadoingreso," +
																			" i.fecha_ingreso as fechaingreso," +
																			" i.fecha_egreso as fechaegreso," +
																			" c.id as cuenta," +
																			" c.estado_cuenta as codestadocuenta," +
																			" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
																			" c.via_ingreso as codviaingreso, " +
																			" getnombreviaingreso(c.via_ingreso) as nomviaingreso " +
																	" from cuentas c " +
																	" inner join ingresos i on(c.id_ingreso=i.id) " +
																	" inner join centros_costo cc on(cc.codigo=c.area) " +
																	" where " +
																			" c.codigo_paciente= ? and " +
																			" cc.centro_atencion=?  and " +
																			" i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') and " +
																			" (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") or (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" and getEsCuentaAsocioincompleto(c.id)='S') ) " +
																	" order by c.fecha_apertura ";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIngresoValidosDistribucion));
			ps.setInt(1, codigoPersona);
			ps.setInt(2, centroAtencion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE INGRESOS VALIDOS PARA LA DISTRIBUCION "+e);
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		
		}
		return mapa;

	}
	
	/**
	 * Método que consulta los tipos de consulta
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposConsulta(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo,nombre FROM for_res_consulta ORDER BY nombre";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposConsulta: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return resultados;
	}
	
	/**
	 * Metodo que retorna un entero como codigo de centro de costo dada una id de cuenta
	 * @param con
	 * @param id
	 * @return
	 */
	public static int obtenerCentroCostoCuenta(Connection con,int id)
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			int codigoCentroCosto=ConstantesBD.codigoNuncaValido;
			 ps= new PreparedStatementDecorator(con.prepareStatement("SELECT area AS centro_costo FROM cuentas WHERE id=?"));
			ps.setInt(1, id);
			 rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigoCentroCosto=rs.getInt("centro_costo");
			return codigoCentroCosto;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
	}

	/**
	 * 
	 * @param con
	 * @param informacionParametrizable
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean insertarInformacionParametrizableValoraciones(Connection con, HashMap infoParametrizable, int numeroSolicitud) 
	{
		PreparedStatementDecorator ps= null;

		try
		{
			//*********************************************************************************************
			//********INGRESAR/MODIFICAR CAMPOS PARAMETRIZABLES.******************************************
			for(int i=0;i<Utilidades.convertirAEntero(infoParametrizable.get("numRegistros")+"");i++)
			{
				if(!UtilidadTexto.getBoolean(infoParametrizable.get("existe_"+i)+""))
				{
					if(!UtilidadTexto.isEmpty(infoParametrizable.get("valor_"+i)+""))
					{
						 ps= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO info_parametrizable_val" +
																				"(codigo_funsecpar," +
																				"numero_solicitud," +
																				"valor)" +
																				"values(?,?,?)"));
						ps.setInt(1,Utilidades.convertirAEntero(infoParametrizable.get("codigofsp_"+i)+""));
						ps.setInt(2,Utilidades.convertirAEntero(numeroSolicitud+""));
						ps.setString(3,infoParametrizable.get("valor_"+i)+"");
						ps.executeUpdate();
					}
				}
				
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		//*********************************************************************************************
		return true;
	}
	
	/**
	 * Método implementado para insertar información de parto para RIPS
	 * @param con
	 * @param infoParto
	 * @param arregloRecienNacido
	 * @return
	 */
	public static boolean insertarInformacionPartoParaRips(Connection con,DtoInformacionParto infoParto, ArrayList<DtoInformacionRecienNacido> arregloRecienNacido)
	{
		PreparedStatementDecorator pst= null;
		
		boolean exito = false;
		try
		{
		
			int consecutivo = 0, resp0 = 1, resp1 = 0, cantidadHijosVivos = 0, cantidadHijosMuertos = 0;
			
			//1) Se inserta la informacion del recién nacido
			String consulta = "insert into info_parto_hijos (" +
				"consecutivo, " +
				"consecutivo_hijo, " +
				"fecha_nacimiento, " +
				"hora_nacimiento," +
				"sexo, " +
				"vivo," +
				"diagnostico_rn, " +
				"cie_rn," +
				"fecha_muerte, " +
				"hora_muerte, " +
				"diagnostico_muerte, " +
				"cie_muerte, " +
				"ingreso, " +
				"peso_egreso, " +
				"fecha_proceso," +
				"hora_proceso, " +
				"usuario_proceso, " +
				"finalizada," +
				"numero_solicitud) " +
				"values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			//Se eliminan de ante mano los niños si ya existían
			String consulta2 = "DELETE FROM info_parto_hijos WHERE "+(!infoParto.getNumeroSolicitud().equals("")?"numero_solicitud = ?":"ingreso = ?");;
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta2));
			if(!infoParto.getNumeroSolicitud().equals(""))
				pst.setInt(1,Utilidades.convertirAEntero(infoParto.getNumeroSolicitud()));
			else
				pst.setInt(1,Utilidades.convertirAEntero(infoParto.getCodigoIngreso()));
			pst.executeUpdate();
			
			
			for(DtoInformacionRecienNacido infoRecienNacido:arregloRecienNacido)
			{
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				
				consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_info_parto_hijos");
				pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
				pst.setInt(2,infoRecienNacido.getConsecutivoHijo());
				
				if(!infoRecienNacido.getFechaNacimiento().equals(""))
					pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoRecienNacido.getFechaNacimiento())));
				else
					pst.setNull(3,Types.DATE);
				
				if(!infoRecienNacido.getHoraNacimiento().equals(""))
					pst.setString(4,infoRecienNacido.getHoraNacimiento());
				else
					pst.setNull(4,Types.VARCHAR);
				
				if(infoRecienNacido.getCodigoSexo()>0)
					pst.setInt(5,infoRecienNacido.getCodigoSexo());
				else
					pst.setNull(5,Types.INTEGER);
				
				pst.setBoolean(6, infoRecienNacido.isVivo());
				if(!infoRecienNacido.getAcronimoDiagnosticoRecienNacido().equals(""))
					pst.setString(7, infoRecienNacido.getAcronimoDiagnosticoRecienNacido());
				else
					pst.setNull(7, Types.VARCHAR);
				
				if(infoRecienNacido.getCieDiagnosticoRecienNacido()>0)
					pst.setInt(8, infoRecienNacido.getCieDiagnosticoRecienNacido());
				else
					pst.setNull(8, Types.INTEGER);
				
				if(!infoRecienNacido.getFechaMuerte().equals(""))
					pst.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoRecienNacido.getFechaMuerte())));
				else
					pst.setNull(9,Types.DATE);
				
				if(!infoRecienNacido.getHoraMuerte().equals(""))
					pst.setString(10, infoRecienNacido.getHoraMuerte());
				else
					pst.setNull(10,Types.VARCHAR);
				
				if(!infoRecienNacido.getAcronimoDiagnosticoMuerte().equals(""))
					pst.setString(11, infoRecienNacido.getAcronimoDiagnosticoMuerte());
				else
					pst.setNull(11, Types.VARCHAR);
				
				if(infoRecienNacido.getCieDiagnosticoMuerte()>0)
					pst.setInt(12, infoRecienNacido.getCieDiagnosticoMuerte());
				else
					pst.setNull(12, Types.INTEGER);
				
				pst.setInt(13,Utilidades.convertirAEntero(infoRecienNacido.getCodigoIngreso()));
				
				pst.setInt(14,infoRecienNacido.getPesoEgreso());
				
				if(!infoRecienNacido.getFechaProceso().equals(""))
					pst.setDate(15,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoRecienNacido.getFechaProceso())));
				else
					pst.setDate(15, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				
				if(!infoRecienNacido.getHoraProceso().equals(""))
					pst.setString(16, infoRecienNacido.getHoraProceso());
				else
					pst.setString(16, UtilidadFecha.getHoraActual(con));
				
				pst.setString(17,infoRecienNacido.getLoginUsuarioProceso());
				pst.setString(18,infoRecienNacido.isFinalizada()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setInt(19,Utilidades.convertirAEntero(infoRecienNacido.getNumeroSolicitud()));
				
				
				if(pst.executeUpdate()<=0)
					resp0 = 0;
				
				//Se realiza la cuenta de los hijos vivos y los hijos muertos
				if(infoRecienNacido.isVivo())
					cantidadHijosVivos++;
				else
					cantidadHijosMuertos++;
				
			}
			
			if(arregloRecienNacido.size()>0)
			{
				infoParto.setCantidadHijosVivos(cantidadHijosVivos);
				infoParto.setCantidadHijosMuertos(cantidadHijosMuertos);
				if(infoParto.getCantidadHijosVivos()>0)
					infoParto.setParto(true);
				else
					infoParto.setParto(false);
					
			}
			
			//2) Se inserta la información del parto
			consulta = "insert into informacion_parto " +
				"(consecutivo, " +
				"codigo_paciente, " +
				"ingreso, " +
				"fecha_proceso, " +
				"usuario, " +
				"institucion, " +
				"hora_proceso, " +
				"semana_gestacional," +
				"control_prenatal, " +
				"cantidad_hijos_vivos, " +
				"cantidad_hijos_muertos, " +
				"parto, " +
				"finalizado," +
				"numero_solicitud) " +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			consulta2 = "UPDATE informacion_parto SET " +
				"semana_gestacional = ?, " +
				"control_prenatal = ?, " +
				"cantidad_hijos_vivos = ?, " +
				"cantidad_hijos_muertos = ?, " +
				"parto = ? " +
				"WHERE " +
				(!infoParto.getNumeroSolicitud().equals("")?"numero_solicitud = ?":"ingreso = ?");
			
			if(!infoParto.isExisteBaseDatos())
			{
			
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				
				consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_informacion_parto");
				
				pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
				pst.setInt(2,infoParto.getCodigoPaciente());
				pst.setInt(3,Utilidades.convertirAEntero(infoParto.getCodigoIngreso()));
				if(!infoParto.getFechaProceso().equals(""))
					pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.getFechaProceso())));
				else
					pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				
				pst.setString(5,infoParto.getLoginUsuario());
				pst.setInt(6, infoParto.getInstitucion());
				if(!infoParto.getHoraProceso().equals(""))
					pst.setString(7,infoParto.getHoraProceso());
				else
					pst.setString(7,UtilidadFecha.getHoraActual(con));
				
				pst.setInt(8,infoParto.getSemanasGestacional());
				pst.setString(9,infoParto.isControlPrenatal()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setInt(10,infoParto.getCantidadHijosVivos());
				pst.setInt(11,infoParto.getCantidadHijosMuertos());
				pst.setString(12,infoParto.isParto()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setString(13,infoParto.isFinalizado()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setInt(14,Utilidades.convertirAEntero(infoParto.getNumeroSolicitud()));
			}
			else
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta2));
				pst.setInt(1,infoParto.getSemanasGestacional());
				pst.setString(2,infoParto.isControlPrenatal()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				pst.setInt(3,infoParto.getCantidadHijosVivos());
				pst.setInt(4,infoParto.getCantidadHijosMuertos());
				pst.setString(5,infoParto.isParto()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				if(!infoParto.getNumeroSolicitud().equals(""))
					pst.setInt(6,Utilidades.convertirAEntero(infoParto.getNumeroSolicitud()));
				else
					pst.setInt(6,Utilidades.convertirAEntero(infoParto.getCodigoIngreso()));
			}
			
			resp1 = pst.executeUpdate();
			
			if(resp0>0&&resp1>0)
				exito = true;
			
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarInformacionPartoParaRips: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		return exito;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Obtiene el listado de Causas Externas	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public static ArrayList obtenerCausasExternas(Connection con,HashMap campos) 
	{
		
		//*****************SE TOMAN LOS PARÁMETROS*****************************************
		boolean solicitudAutorizacion = UtilidadTexto.getBoolean(campos.get("solicitudAutorizacion").toString());
		//*********************************************************************************
		
		String cadena="SELECT codigo, nombre FROM causas_externas ";
		
		if(solicitudAutorizacion)
		{
			cadena += " WHERE sol_autorizacion = '"+ConstantesBD.acronimoSi+"' ";
		}
		
		cadena += " ORDER BY nombre ASC ";
		
		ArrayList array=new ArrayList();
		PreparedStatementDecorator psReq= null;
		ResultSetDecorator rsReq = null;
		try
		{
			psReq= new PreparedStatementDecorator(con.prepareStatement(cadena));			
			rsReq=new ResultSetDecorator(psReq.executeQuery());
			
			while(rsReq.next())
			{
				HashMap datos = new HashMap();
				datos.put("codigo",rsReq.getString(1));
				datos.put("descripcion",rsReq.getString(2));				
				array.add(datos);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(psReq!=null){
					psReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rsReq!=null){
					rsReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}	

		return array;
	}
	
	//***************************************************************************************************************************
	
	/**
	 * Obtiene el listado de Finalidades de la consulta	  
	 * @param con	 
	 * @return llaves(codigo,descripcion)
	 */
	public static ArrayList obtenerFinalidadesConsulta(Connection con) 
	{
		String cadena="SELECT acronimo, nombre FROM finalidades_consulta ORDER BY nombre ASC ";
		
		ArrayList array=new ArrayList();
		PreparedStatementDecorator psReq= null;
		ResultSetDecorator rsReq = null;
		try
		{
			 psReq= new PreparedStatementDecorator(con.prepareStatement(cadena));			
			 rsReq=new ResultSetDecorator(psReq.executeQuery());
			
			while(rsReq.next())
			{
				HashMap datos = new HashMap();
				datos.put("codigo",rsReq.getString(1));
				datos.put("descripcion",rsReq.getString(2));				
				array.add(datos);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}finally{
			try {
				if(psReq!=null){
					psReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rsReq!=null){
					rsReq.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return array;		
	}
	
	//***************************************************************************************************************************
	/**
	 * Método implementado para cargar la información del parto ingresada para RIPS
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoInformacionParto cargarInformacionPartoParaRips(Connection con,HashMap campos)
	{
		DtoInformacionParto infoParto = new DtoInformacionParto();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			//Se consulta la información del parto básica
			String consulta = "SELECT " +
				"consecutivo," +
				"codigo_paciente," +
				"getnombrepersona(codigo_paciente) as nombre_paciente," +
				"ingreso as codigo_ingreso," +
				"numero_solicitud," +
				"to_char(fecha_proceso,'"+ConstantesBD.formatoFechaAp+"') as fecha_proceso," +
				"hora_proceso," +
				"usuario as login_usuario," +
				"getnombreusuario2(usuario) as nombre_usuario," +
				"institucion," +
				"coalesce(semana_gestacional,0) as semana_gestacional," +
				"coalesce(control_prenatal,'') as control_prenatal," +
				"coalesce(parto,'') as parto," +
				"coalesce(cantidad_hijos_vivos,0) as cantidad_hijos_vivos," +
				"coalesce(cantidad_hijos_muertos,0) as cantidad_hijos_muertos," +
				"coalesce(finalizado,'') as finalizado " +
				"from informacion_parto WHERE ";
			String seccionWHERE = "";
			
			if(!UtilidadTexto.isEmpty(campos.get("numeroSolicitud").toString()))
				seccionWHERE +=  (seccionWHERE.equals("")?"":" AND ") + "numero_solicitud = "+campos.get("numeroSolicitud");
			if(!UtilidadTexto.isEmpty(campos.get("codigoIngreso").toString()))
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + "ingreso = "+campos.get("codigoIngreso");
			
			consulta = consulta + seccionWHERE;
			
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				infoParto.setConsecutivo(rs.getString("consecutivo"));
				infoParto.setCodigoPaciente(rs.getInt("codigo_paciente"));
				infoParto.setNombrePaciente(rs.getString("nombre_paciente"));
				infoParto.setCodigoIngreso(rs.getString("codigo_ingreso"));
				infoParto.setNumeroSolicitud(rs.getString("numero_solicitud"));
				infoParto.setFechaProceso(rs.getString("fecha_proceso"));
				infoParto.setHoraProceso(rs.getString("hora_proceso"));
				infoParto.setLoginUsuario(rs.getString("login_usuario"));
				infoParto.setNombreUsuario(rs.getString("nombre_usuario"));
				infoParto.setInstitucion(rs.getInt("institucion"));
				infoParto.setSemanasGestacional(rs.getInt("semana_gestacional"));
				infoParto.setControlPrenatal(UtilidadTexto.getBoolean(rs.getString("control_prenatal")));
				infoParto.setParto(UtilidadTexto.getBoolean(rs.getString("parto")));
				infoParto.setCantidadHijosVivos(rs.getInt("cantidad_hijos_vivos"));
				infoParto.setCantidadHijosMuertos(rs.getInt("cantidad_hijos_muertos"));
				infoParto.setFinalizado(UtilidadTexto.getBoolean(rs.getString("finalizado")));
				infoParto.setExisteBaseDatos(true);
			}
			
			//Se consulta la información de recién nacido
			consulta = "SELECT " +
				"consecutivo," +
				"consecutivo_hijo," +
				"coalesce(to_char(fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_nacimiento," +
				"coalesce(hora_nacimiento,'') as hora_nacimiento," +
				"coalesce(sexo,0) as codigo_sexo," +
				"coalesce(getdescripcionsexo(sexo),'') as nombre_sexo," +
				"vivo,coalesce(diagnostico_rn,'') as diagnostico_rn," +
				"coalesce(cie_rn,0) as cie_rn," +
				"coalesce(getnombrediagnostico(diagnostico_rn,cie_rn),'') as nombre_diagnostico_rn," +
				"coalesce(to_char(fecha_muerte,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_muerte," +
				"coalesce(hora_muerte,'') as hora_muerte," +
				"coalesce(diagnostico_muerte,'') as diagnostico_muerte," +
				"coalesce(cie_muerte,0) as cie_muerte," +
				"coalesce(getnombrediagnostico(diagnostico_muerte,cie_muerte),'') as nombre_diagnostico_muerte," +
				"coalesce(cirugia,0) as codigo_cirugia," +
				"coalesce(numero_solicitud,0) as numero_solicitud," +
				"coalesce(ingreso,0) as ingreso," +
				"coalesce(peso_egreso,0) as peso_egreso," +
				"coalesce(to_char(fecha_proceso,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_proceso," +
				"coalesce(hora_proceso,'') as hora_proceso," +
				"coalesce(usuario_proceso,'') as usuario_proceso," +
				"coalesce(getnombreusuario2(usuario_proceso),'') as nombre_usuario_proceso," +
				"coalesce(finalizada,'') as finalizada " +
				"from info_parto_hijos WHERE ";
			seccionWHERE = "";
			
			if(!UtilidadTexto.isEmpty(campos.get("numeroSolicitud").toString()))
				seccionWHERE +=  (seccionWHERE.equals("")?"":" AND ") + "numero_solicitud = "+campos.get("numeroSolicitud");
			if(!UtilidadTexto.isEmpty(campos.get("codigoIngreso").toString()))
				seccionWHERE += (seccionWHERE.equals("")?"":" AND ") + "ingreso = "+campos.get("codigoIngreso");
			
			consulta = consulta + seccionWHERE;
			
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				DtoInformacionRecienNacido infoRecienNacido = new DtoInformacionRecienNacido();
				infoRecienNacido.setConsecutivo(rs.getString("consecutivo"));
				infoRecienNacido.setConsecutivoHijo(rs.getInt("consecutivo_hijo"));
				infoRecienNacido.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				infoRecienNacido.setHoraNacimiento(rs.getString("hora_nacimiento"));
				infoRecienNacido.setCodigoSexo(rs.getInt("codigo_sexo"));
				infoRecienNacido.setNombreSexo(rs.getString("nombre_sexo"));
				infoRecienNacido.setAcronimoDiagnosticoRecienNacido(rs.getString("diagnostico_rn"));
				infoRecienNacido.setCieDiagnosticoRecienNacido(rs.getInt("cie_rn"));
				infoRecienNacido.getDiagnosticoRecienNacido().setNombre(rs.getString("nombre_diagnostico_rn"));
				infoRecienNacido.setFechaMuerte(rs.getString("fecha_muerte"));
				infoRecienNacido.setHoraMuerte(rs.getString("hora_muerte"));
				infoRecienNacido.setAcronimoDiagnosticoMuerte(rs.getString("diagnostico_muerte"));
				infoRecienNacido.setCieDiagnosticoMuerte(rs.getInt("cie_muerte"));
				infoRecienNacido.getDiagnosticoMuerte().setNombre(rs.getString("nombre_diagnostico_muerte"));
				infoRecienNacido.setCodigoCirugia(rs.getString("codigo_cirugia"));
				infoRecienNacido.setNumeroSolicitud(rs.getString("numero_solicitud"));
				infoRecienNacido.setCodigoIngreso(rs.getString("ingreso"));
				infoRecienNacido.setPesoEgreso(rs.getInt("peso_egreso"));
				infoRecienNacido.setFechaProceso(rs.getString("fecha_proceso"));
				infoRecienNacido.setHoraProceso(rs.getString("hora_proceso"));
				infoRecienNacido.setLoginUsuarioProceso(rs.getString("usuario_proceso"));
				infoRecienNacido.setNombreUsuarioProceso(rs.getString("nombre_usuario_proceso"));
				infoRecienNacido.setFinalizada(UtilidadTexto.getBoolean(rs.getString("finalizada")));
				infoRecienNacido.setExisteBaseDatos(true);
				
				infoParto.getRecienNacidos().add(infoRecienNacido);
				
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionPartoParaRips: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return infoParto;
	}
	
	/**
	 * Método para obtener el nombre de uan causa externa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreCausaExterna(Connection con,int codigo)
	{
		String nombre = "";

		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT nombre AS nombre from causas_externas where codigo = "+codigo;
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreCausaExterna: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return nombre;
	}
	
	/**
	 * Método para obtener el nombre de la finalidad de la consulta
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreFinalidadConsulta(Connection con,String codigo)
	{
		String nombre = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT nombre As nombre from finalidades_consulta WHERE acronimo =  '"+codigo+"'";
			 st = con.createStatement( );
			 rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreFinalidadConsulta: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return nombre;
	}
	
	/**
	 * Método que consulta el id del ingreso a partir de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerIdIngresoSolicitud(Connection con,String numeroSolicitud)
	{
		String idIngreso = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT c.id_ingreso As ingreso FROM solicitudes s INNER JOIN cuentas c ON(c.id=s.cuenta) WHERE s.numero_solicitud = "+numeroSolicitud;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				idIngreso = rs.getString("ingreso"); 
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerIdIngresoSolicitud: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return idIngreso;
	}
	
	/**
	 * Método para obtener el codigo del paciente de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoPacienteSolicitud(Connection con,String numeroSolicitud)
	{
		int codigoPaciente = ConstantesBD.codigoNuncaValido;
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT c.codigo_paciente As paciente FROM solicitudes s INNER JOIN cuentas c ON(c.id=s.cuenta) WHERE s.numero_solicitud = "+numeroSolicitud;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				codigoPaciente = rs.getInt("paciente");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoPacienteSolicitud: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return codigoPaciente;
	}
		
	//------------------------------------------------------------------------------------
	/**
	 * Obtiene la informacion de la muerte del paciente 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap obtenerInfoMuertePaciente(Connection con, HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		logger.info("valor del mapa >> "+parametros);
		//Consulta la informacion de muerte del paciente		
		String cadena = "SELECT " +
				"esta_vivo, " +
				"to_char(fecha_muerte,'DD/MM/YYYY') AS fecha_muerte," +
				"hora_muerte," +
				"certificado_defuncion " +
				"FROM pacientes " +
				"WHERE codigo_paciente = ? AND esta_vivo = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			
			respuesta  = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			
			//Consulta la informacion de los diagnosticos si existe informacion de la cuenta
			if(parametros.containsKey("codigoCuenta") && !parametros.get("codigoCuenta").toString().equals(""))
			{
				cadena =
						"SELECT " +
						"diagnostico_muerte," +
						"diagnostico_muerte_cie," +
						"getnombrediagnostico(diagnostico_muerte,diagnostico_muerte_cie) AS diagnostico_muerte_nombre " +
						"FROM egresos " +
						"WHERE cuenta = ? ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoCuenta").toString()));
				
				respuesta.put("diagnosticoMuerte","");					
				respuesta.put("diagnosticoMuerteCie","");
				respuesta.put("diagnosticoMuerteNombre","");
				
				 rs =new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					respuesta.put("diagnosticoMuerte",rs.getString(1));					
					respuesta.put("diagnosticoMuerteCie",rs.getString(2));
					respuesta.put("diagnosticoMuerteNombre",rs.getString(3));
				}				
			}			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return respuesta;
	}
	
	/**
	 * Metodo validacion Medico Especialista justificacion No Pos y Ocupacion Medico Epecialista
	 * @param con
	 * @param usuario
	 * @param paraArticulo
	 * @return
	 */
	public static boolean validarEspecialidadProfesionalSalud(Connection con, UsuarioBasico usuario, boolean paraArticulo)
	{
		String valorParametro="";
		String valorOcupacion="";
		if(paraArticulo == true){
			valorParametro=ValoresPorDefecto.getValidacionOcupacionJustificacionNoPosArticulos(usuario.getCodigoInstitucionInt());
		}
		else{
			valorParametro=ValoresPorDefecto.getValidacionOcupacionJustificacionNoPosServicios(usuario.getCodigoInstitucionInt());
		}
		if(valorParametro.equals("S")){
			valorOcupacion=ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(usuario.getCodigoInstitucionInt(), true);
			if(usuario.getCodigoOcupacionMedica() == Utilidades.convertirAEntero(valorOcupacion)){
				return true;
			}
			else
				return false;
		}
		return true;
	}
	
	/**
	 * Consultar Ingresos por paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap consultarIngresosXPaciente(Connection con, int codigoPersona)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");

		PreparedStatementDecorator ps= null;

		try
		{
			String consulta = "SELECT " +
								"i.centro_atencion AS centro_atencion, " +
								"getnomcentroatencion(i.centro_atencion) AS nom_centro_atencion, " +
								"getnombreviaingresotipopac(c.id) AS via_ingreso, " +
								"i.consecutivo AS num_ingreso, " +
								"i.id as codigo_ingreso, " +
								"i.fecha_ingreso AS fecha_ingreso, " +
								"i.estado AS estado_ingreso, " +
								"getintegridaddominio(i.estado) AS nom_estado_ingreso, " +
								"c.id AS num_cuenta, " +
								"getnombreestadocuenta(c.estado_cuenta) AS nom_estado_cuenta, " +
								"c.estado_cuenta AS estado_cuenta, " +
								//"coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') as fecha_egreso " +
								"CASE WHEN c.via_ingreso IN (3, 1) " +
									"THEN coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') " +
								"ELSE " +
									"coalesce(to_char(c.fecha_apertura, 'YYYY-MM-DD'), '') " +
								"END AS  fecha_egreso "+
							"FROM " +
								"ingresos i " +
							"INNER JOIN " +
								"cuentas c ON (c.id_ingreso = i.id) " +
							"LEFT OUTER JOIN " +
								"egresos e on (e.cuenta=c.id and e.usuario_responsable IS NOT NULL) " +	
							"WHERE " +
								"c.estado_cuenta != "+ConstantesBD.codigoEstadoCuentaCerrada+" AND i.codigo_paciente="+codigoPersona+" AND getcuentafinalasocio(i.id,c.id) IS NULL "+
							"ORDER BY " +
								"i.fecha_ingreso "; 
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("\nConsultar ingresos x paciete --> " + consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarIngresosXPaciente: "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		
		}
		return mapa;
	}
	
	
	/**
	 * Metodo encargado de consultar todos los ingresos con su informacion
	 * no importa el estado del ingreso o de la cuenta.
	 * @param con
	 * @param codigoPersona
	 * @return HashMap
	 *------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 *------------------------------------
	 * centroAtencion_,nomCentroAtencion_,
	 * viaIngreso_, numIngreso_,codigoIngreso_,
	 * fechaIngreso_, estadoIngreso_,
	 * nomEstadoIngreso_,numCuenta_, nomEstadoCuenta_,
	 * estadoCuenta_, fechaEgreso_
	 */
	public static HashMap consultarTodosIngresosXPaciente(Connection con, int codigoPersona)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try
		{
			String consulta = "SELECT " +
								"i.centro_atencion AS centro_atencion, " +
								"getnomcentroatencion(i.centro_atencion) AS nom_centro_atencion, " +
								"getnombreviaingresotipopac(c.id) AS via_ingreso, " +
								"i.consecutivo AS num_ingreso, " +
								"i.id as codigo_ingreso, " +
								"to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fecha_ingreso, " +
								"i.hora_ingreso As hora_ingreso," +
								"i.estado AS estado_ingreso, " +
								"getintegridaddominio(i.estado) AS nom_estado_ingreso, " +
								"c.id AS num_cuenta, " +
								"getnombreestadocuenta(c.estado_cuenta) AS nom_estado_cuenta, " +
								"c.via_ingreso As codigo_via_ingreso, " +
								"c.estado_cuenta AS estado_cuenta, " +
								"c.area As area,  " +
								//"coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') as fecha_egreso " +
								"CASE WHEN c.via_ingreso IN (3, 1) " +
									"THEN coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') " +
								"ELSE " +
									"coalesce(to_char(c.fecha_apertura, 'YYYY-MM-DD'), '') " +
								"END AS  fecha_egreso "+
							"FROM " +
								"ingresos i " +
							"INNER JOIN " +
								"cuentas c ON (c.id_ingreso = i.id) " +
							"LEFT OUTER JOIN " +
								"egresos e on (e.cuenta=c.id and e.usuario_responsable IS NOT NULL) " +	
							"WHERE " +
								"i.codigo_paciente="+codigoPersona+" "+ 
							"ORDER BY " +
								"i.fecha_ingreso,c.id "; 
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			logger.info("\nConsultar ingresos Todos x paciete --> " + consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarIngresosTodosXPaciente: "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
			
		}
		return mapa;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap consultarIngresosCuentaXPaciente(Connection con, int codigoPersona)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try
		{
			String consulta = "SELECT " +
								"i.centro_atencion AS centro_atencion, " +
								"getnomcentroatencion(i.centro_atencion) AS nom_centro_atencion, " +
								"getnombreviaingresotipopac(c.id) AS via_ingreso, " +
								"i.consecutivo AS num_ingreso, " +
								"i.id as codigo_ingreso, " +
								"i.fecha_ingreso AS fecha_ingreso, " +
								"i.estado AS estado_ingreso, " +
								"getintegridaddominio(i.estado) AS nom_estado_ingreso, " +
								"c.id AS num_cuenta, " +
								"getnombreestadocuenta(c.estado_cuenta) AS nom_estado_cuenta, " +
								"c.estado_cuenta AS estado_cuenta, " +
								//"coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') as fecha_egreso " +
								"CASE " +
									"WHEN c.via_ingreso = 3 AND e.destino_salida IS NOT NULL " +
										"THEN " +
											"coalesce(to_char(ev.fecha_evolucion, 'YYYY-MM-DD'), '') " +
									"WHEN c.via_ingreso = 3 AND e.usuario_responsable IS NOT NULL AND e.evolucion IS NULL " +
										"THEN " +
											"coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '') " +
									"WHEN c.via_ingreso = 1 AND c.tipo_paciente='C' AND e.destino_salida IS NOT NULL " +
										"THEN " +
											"coalesce(to_char(ev.fecha_evolucion, 'YYYY-MM-DD'), '') " +
									"WHEN c.via_ingreso IN (2,4) AND e.usuario_responsable IS NOT NULL " +
										"THEN " +
											"coalesce(to_char(c.fecha_apertura, 'YYYY-MM-DD'), '') " +
									"ELSE " +
										"coalesce(to_char(e.fecha_egreso, 'YYYY-MM-DD'), '' )" +
								"END AS  fecha_egreso "+
							"FROM " +
								"ingresos i " +
							"INNER JOIN " +
								"cuentas c ON (c.id_ingreso = i.id) " +
							"LEFT OUTER JOIN " +
								"egresos e on (e.cuenta=c.id) " +
							"LEFT OUTER JOIN evoluciones ev ON (e.evolucion = ev.codigo) " +	
							"WHERE " +
								"c.estado_cuenta != "+ConstantesBD.codigoEstadoCuentaCerrada+" AND i.codigo_paciente="+codigoPersona+" "+
							"ORDER BY " +
								"i.fecha_ingreso "; 
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("\nConsultar ingresos cuenta x paciete --> " + consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarIngresosCuentaXPaciente: "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	/**
	 * Método que consulta los signos vitales activos
	 * @param con
	 * @return
	 */
	public static ArrayList<SignoVital> cargarSignosVitales(Connection con)
	{
		Statement st = null;
		ResultSetDecorator rs = null;
		ArrayList<SignoVital> resultados = new ArrayList<SignoVital>();
		try
		{
			String consulta = "SELECT " +
				"codigo," +
				"nombre," +
				"unidad_medida," +
				"obligatorio " +
				"from signos_vitales " +
				"WHERE codigo_especialidad = "+ConstantesBD.codigoEspecialidadValoracionGeneral+" and activo = '"+ConstantesBD.acronimoSi+"' order by orden";
			
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				SignoVital signo = new SignoVital();
				signo.setCodigo(rs.getInt("codigo"));
				signo.setNombre(rs.getString("nombre"));
				signo.setUnidadMedida(rs.getString("unidad_medida"));
				signo.setActivo(true);
				signo.setRequerido(UtilidadTexto.getBoolean(rs.getString("obligatorio")));
				
				resultados.add(signo);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSignosVitales: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Metodo que consulta las clasificaciones de eventos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarClasificacionesEventos(Connection con, int institucion){
		HashMap mapa = new HashMap();
		PreparedStatementDecorator ps= null;
		mapa.put("numRegistros", "0");
		try
		{
			String consulta = "SELECT codigo, nombre FROM clasificaciones_eventos WHERE institucion="+institucion; 
			logger.info("consulta-->"+consulta);
		    ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarClasificacionesEventos: "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		logger.info("mapa->"+mapa);
		return mapa;
	}
	
	/**
	 * Método implementado para cargar la revision x sistemas comun y la del un listado de componentes
	 * @param con
	 * @param tiposComponente
	 * @return
	 */
	public static ArrayList<DtoRevisionSistema> cargarRevisionesSistemas(Connection con,ArrayList<Integer> tiposComponente)
	{
		ArrayList<DtoRevisionSistema> resultados = new ArrayList<DtoRevisionSistema>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			/**
			 * Se edita el listado de los tipos de componente
			 */
			String listadoTiposComponente = "";
			if(tiposComponente.size()>0)
			{
				listadoTiposComponente = " IN ("+ConstantesBD.codigoNuncaValido;
				for(Integer tipo:tiposComponente)
					listadoTiposComponente += ","+tipo.intValue();
				listadoTiposComponente += ")";
			}
			//***********SE CARGAN LAS REVISIONES SISTEMAS QUE SON ÚNICAS***********************************
			String consulta = "SELECT " +
				"codigo," +
				"nombre," +
				"coalesce(unidad_medida,'') as unidad_medida," +
				"verdadero," +
				"falso," +
				"coalesce(tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente " +
				"FROM revisiones_sistemas WHERE activo = '"+ConstantesBD.acronimoSi+"' AND tipo_componente "+(listadoTiposComponente.length()>0?listadoTiposComponente:"is null")+" order by orden";
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			 rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoRevisionSistema revision = new DtoRevisionSistema();
				revision.setCodigo(rs.getInt("codigo"));
				revision.setNombre(rs.getString("nombre"));
				revision.setUnidadMedida(rs.getString("unidad_medida"));
				revision.setValorVerdadero(rs.getString("verdadero"));
				revision.setValorFalso(rs.getString("falso"));
				revision.setTipoComponente(rs.getInt("tipo_componente"));
				revision.setMultiple(false);
				resultados.add(revision);
				
			}
			
			
			//**********************************************************************************************
			//***********SE CARGAN LAS REVISIONES SISTEMAS QUE SON ÚNICAS***********************************
			consulta = "select " +
				"codigo," +
				"coalesce(nombre,'') as nombre," +
				"verdadero," +
				"falso," +
				"coalesce(tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente " +
				"from tipos_rev_sistemas WHERE activo = '"+ConstantesBD.acronimoSi+"' and (tipo_componente "+(listadoTiposComponente.length()>0?listadoTiposComponente:"is null")+") order by orden";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoRevisionSistema revision = new DtoRevisionSistema();
				revision.setCodigo(rs.getInt("codigo"));
				revision.setNombre(rs.getString("nombre"));
				revision.setValorVerdadero(rs.getString("verdadero"));
				revision.setValorFalso(rs.getString("falso"));
				revision.setTipoComponente(rs.getInt("tipo_componente"));
				revision.setMultiple(true);
				
				//*************	SE CONSULTAN LAS OPCIONES DE LA REVISION X SISTEMA********************
				consulta = "select codigo,coalesce(nombre,'') as nombre from rev_sistemas_op WHERE tipo_rev_sistema = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,revision.getCodigo());
				ResultSetDecorator rs2 = new ResultSetDecorator(pst.executeQuery());
				while(rs2.next())
				{
					InfoDatosInt opcion = new InfoDatosInt(rs2.getInt("codigo"),rs2.getString("nombre"));
					revision.getOpciones().add(opcion);
				}
				//************************************************************************************
				
				resultados.add(revision);
			}
			//**********************************************************************************************
			
			
			///Si no se encontraron revisiones sistemas enviando tipos de componentes específicos se vuelve a realizar la consulta sin tipo componente 
			if(resultados.size()==0&&listadoTiposComponente.length()>0)
			{
				//********************NUEVA CONSULTA SIN TIPO DE COMPONENTE****************************************
				//Es lo mismo que lo anterior solo sin tipo de componente
				//Revisiones sistemas unicas ----------------------------------------------------------------------
				consulta = "SELECT " +
				"codigo," +
				"nombre," +
				"coalesce(unidad_medida,'') as unidad_medida," +
				"verdadero," +
				"falso," +
				"coalesce(tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente " +
				"FROM revisiones_sistemas WHERE activo = '"+ConstantesBD.acronimoSi+"' AND tipo_componente is null order by orden";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoRevisionSistema revision = new DtoRevisionSistema();
					revision.setCodigo(rs.getInt("codigo"));
					revision.setNombre(rs.getString("nombre"));
					revision.setUnidadMedida(rs.getString("unidad_medida"));
					revision.setValorVerdadero(rs.getString("verdadero"));
					revision.setValorFalso(rs.getString("falso"));
					revision.setTipoComponente(rs.getInt("tipo_componente"));
					revision.setMultiple(false);
					resultados.add(revision);
					
				}
				//REvisiones sistemas multiples -------------------------------------------------------
				consulta = "select " +
					"codigo," +
					"coalesce(nombre,'') as nombre," +
					"verdadero," +
					"falso," +
					"coalesce(tipo_componente,"+ConstantesBD.codigoNuncaValido+") as tipo_componente " +
					"from tipos_rev_sistemas WHERE activo = '"+ConstantesBD.acronimoSi+"' and tipo_componente is null order by orden";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoRevisionSistema revision = new DtoRevisionSistema();
					revision.setCodigo(rs.getInt("codigo"));
					revision.setNombre(rs.getString("nombre"));
					revision.setValorVerdadero(rs.getString("verdadero"));
					revision.setValorFalso(rs.getString("falso"));
					revision.setTipoComponente(rs.getInt("tipo_componente"));
					revision.setMultiple(true);
					
					//*************	SE CONSULTAN LAS OPCIONES DE LA REVISION X SISTEMA********************
					consulta = "select codigo,coalesce(nombre,'') as nombre from rev_sistemas_op WHERE tipo_rev_sistema = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1,revision.getCodigo());
					ResultSetDecorator rs2 = new ResultSetDecorator(pst.executeQuery());
					while(rs2.next())
					{
						InfoDatosInt opcion = new InfoDatosInt(rs2.getInt("codigo"),rs2.getString("nombre"));
						revision.getOpciones().add(opcion);
					}
					//************************************************************************************
					
					resultados.add(revision);
				}
				//**********************************************************************************************
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarRevisionesSistemas: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	/**
	 * Método implementado para cargar estados de conciencia
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarEstadosConciencia(Connection con,int institucion)
	{
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo,nombre FROM estados_conciencia WHERE institucion = "+institucion+" order by codigo";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarEstadosConciencia: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método para cargar las conductas de la valoracion
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> cargarConductasValoracion(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo,nombre FROM conductas_valoracion WHERE activo = '"+ConstantesBD.acronimoSi+"'";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				resultados.add(elemento);
			
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConductasValoracion: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método implementado para cargar los tipos de diagnostico
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarTiposDiagnostico(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo,nombre,acronimo FROM tipos_diagnostico";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				elemento.put("acronimo",rs.getObject("acronimo"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposDiagnostico: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método que consuta el nombre de una conducta de valoración
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreConductaValoracion(Connection con,int codigo)
	{
		String nombre = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "select nombre as nombre from conductas_valoracion where codigo = "+codigo;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				nombre = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombreConductaValoracion: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return nombre;
	}
	
	/**
	 * Método usado para obtener el codigo de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoConductaValoracionUrgenciasCuenta(Connection con,String idCuenta)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT vu.codigo_conducta_valoracion FROM solicitudes s INNER JOIN valoraciones_urgencias vu on(vu.numero_solicitud=s.numero_solicitud) WHERE s.cuenta = ?";
			pst = con.prepareStatement(consulta);
			pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
			rs = pst.executeQuery();
			if(rs.next()){
				resultado = rs.getInt("codigo_conducta_valoracion");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigoConductaValoracionUrgenciasCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoConductaValoracionUrgenciasCuenta", e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultado;
	}
	
	/**
	 * Método implementado para consultar los ultimos diagnosticos del paciente, primero se busca por la evolucion,
	 * luego se busca por las valoraciones.
	 * Nota * En el caso de encontrar diagnosticos, el primer elemento del arreglo corresponderá a el diagnóstico principal
	 * @param con
	 * @param codigoPaciente
	 * @param seccionOrderBy
	 * @return
	 */
	public static ArrayList<Diagnostico> obtenerUltimosDiagnosticosPaciente(Connection con,String codigoPaciente)
	{
		ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			
			//****************CONSULTA DE LA ÚLTIMA EVOLUCION****************************
			String consulta = "SELECT " +
				"e.codigo AS codigo " +
				"FROM cuentas c " +
				"INNER JOIN solicitudes s ON(s.cuenta = c.id) " +
				"INNER JOIN evoluciones e ON(e.valoracion = s.numero_solicitud) " +
				"WHERE c.codigo_paciente = "+codigoPaciente+" ORDER BY codigo desc";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			logger.info("consulta de la última evolucion=> "+consulta);
			
			if(rs.next())
			{
				
				//En el caso de encontrar evolucion se consultan sus diagnosticos
				String codigoEvolucion = rs.getString("codigo");
				logger.info("SE ENCONTRO EVOLUCION=> "+codigoEvolucion);
				consulta = "select " +
					"acronimo_diagnostico AS acronimo," +
					"tipo_cie_diagnostico as tipo_cie," +
					"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) as nombre," +
					"principal " +
					"from evol_diagnosticos " +
					"where " +
					"evolucion = "+codigoEvolucion+" order by principal desc";
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					Diagnostico diagTemp = new Diagnostico();
					diagTemp.setAcronimo(rs.getString("acronimo"));
					diagTemp.setTipoCIE(rs.getInt("tipo_cie"));
					diagTemp.setNombre(rs.getString("nombre"));
					diagTemp.setPrincipal(rs.getBoolean("principal"));
					diagnosticos.add(diagTemp);
				}
				
				//Si hubo al menos diagnostico principal se consulta el tipo de diagnostico
				if(diagnosticos.size()>=1)
				{
					consulta = "SELECT " +
						"tipo_diagnostico_principal as codigo_tipo_diagnostico, " +
						"getnombretipodiag(tipo_diagnostico_principal) as nombre_tipo_diagnostico " +
						"from evoluciones WHERE codigo = "+codigoEvolucion;
					
					st = con.createStatement( );
					rs =  new ResultSetDecorator(st.executeQuery(consulta));
					if(rs.next())
					{
						//Se añade el tipo de diagnostico al diagnostico principal
						Diagnostico diagTemp = diagnosticos.get(0);
						diagTemp.setCodigoTipoDiagnostico(rs.getInt("codigo_tipo_diagnostico"));
						diagTemp.setNombreTipoDiagnostico(rs.getString("nombre_tipo_diagnostico"));
					}
				}
			}
			//*****************************************************************************
			
			logger.info("numero de diagnosticos encontrados => "+diagnosticos.size());
			//***************CONSULTA DE LA ÚLTIMA VALORACION*******************************
			//Si no se han encontrado diagnosticos se busca la última valoracion
			if(diagnosticos.size()<=0)
			{
				consulta = "SELECT " +
					"v.numero_solicitud as codigo " +
					"FROM cuentas c " +
					"INNER JOIN solicitudes s ON(s.cuenta = c.id) " +
					"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) " +
					"WHERE c.codigo_paciente = "+codigoPaciente+" ORDER BY codigo desc";
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				logger.info("consulta de la última valoracion=> "+consulta);
				if(rs.next())
				{
					//En el caso de encontrar valoracion se consultan sus diagnosticos
					String codigoValoracion = rs.getString("codigo");
					logger.info("SE ECONTRO VALORACION=> "+codigoValoracion);
					consulta = "SELECT " +
						"acronimo_diagnostico as acronimo, " +
						"tipo_cie_diagnostico as tipo_cie," +
						"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) as nombre," +
						"principal " +
						"from val_diagnosticos " +
						"WHERE valoracion = "+codigoValoracion+" order by principal desc";
					st = con.createStatement( );
					rs =  new ResultSetDecorator(st.executeQuery(consulta));
					while(rs.next())
					{
						Diagnostico diagTemp = new Diagnostico();
						diagTemp.setAcronimo(rs.getString("acronimo"));
						diagTemp.setTipoCIE(rs.getInt("tipo_cie"));
						diagTemp.setNombre(rs.getString("nombre"));
						diagTemp.setPrincipal(rs.getBoolean("principal"));
						diagnosticos.add(diagTemp);
					}
					
					//Si hubo al menos diagnostico principal se consulta el tipo de diagnostico
					if(diagnosticos.size()>=1)
					{
						consulta = "SELECT " +
							"tipo_diagnostico_principal as codigo_tipo_diagnostico, " +
							"getnombretipodiag(tipo_diagnostico_principal) as nombre_tipo_diagnostico " +
							"from valoraciones_consulta WHERE numero_solicitud = "+codigoValoracion;
						st = con.createStatement( );
						rs =  new ResultSetDecorator(st.executeQuery(consulta));
						if(rs.next())
						{
							//Se añade el tipo de diagnostico al diagnostico principal
							Diagnostico diagTemp = diagnosticos.get(0);
							diagTemp.setCodigoTipoDiagnostico(rs.getInt("codigo_tipo_diagnostico"));
							diagTemp.setNombreTipoDiagnostico(rs.getString("nombre_tipo_diagnostico"));
						}
					}
				}
			}
			//*****************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimosDiagnosticosPaciente: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return diagnosticos;
	}
	
	/**
	 * Método que realiza la consulta de la historia menstrual 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoHistoriaMenstrual cargarHistoriaMenstrual(Connection con,String codigoPaciente,String numeroSolicitud,int tipoBD)
	{
		DtoHistoriaMenstrual historiaMenstrual = new DtoHistoriaMenstrual();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			//****************CONSUÑTA DE LOS ANTECEENTES GINECO DEL PACIENTE*******************************
			String consulta = "SELECT "+ 
				"ago.codigo_paciente AS codigo_paciente, "+
				"coalesce(ago.edad_menarquia,"+ConstantesBD.codigoNuncaValido+") AS codigo_edad_menarquia, "+
				"coalesce(ago.otra_edad_menarquia,'') AS otra_edad_menarquia, "+
				//Nota *  si el rango no es la opcion seleccione u otra, se agrega la palabra años
				"coalesce(re.nombre,'') || CASE WHEN re.nombre is not null AND re.codigo NOT IN (-1,0) then ' años' ELSE '' END  AS nombre_edad_menarquia, "+
				"coalesce(ago.edad_menopausia,"+ConstantesBD.codigoNuncaValido+") AS codigo_edad_menopausia, "+
				"coalesce(ago.otra_edad_menopausia,'') AS otra_edad_menopausia, "+ 
				//Nota *  si el rango no es la opcion seleccione u otra, se agrega la palabra años
				"coalesce(rm.nombre,'') || CASE WHEN rm.nombre is not null AND rm.codigo NOT IN (-1,0) then ' años' ELSE '' END AS nombre_edad_menopausia "+ 
				"FROM ant_gineco_obste ago "+ 
				"LEFT OUTER JOIN rangos_edad_menarquia re ON(re.codigo=ago.edad_menarquia) "+ 
				"LEFT OUTER JOIN rangos_edad_menopausia rm ON(rm.codigo=ago.edad_menopausia) "+ 
				"WHERE "+ 
				"ago.codigo_paciente = "+codigoPaciente;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				historiaMenstrual.setCodigoPaciente(rs.getString("codigo_paciente"));
				historiaMenstrual.setCodigoEdadMenarquia(rs.getInt("codigo_edad_menarquia"));
				//Se verifica si existe edad de menarquia
				if(historiaMenstrual.getCodigoEdadMenarquia()!=ConstantesBD.codigoNuncaValido)
					historiaMenstrual.setExisteEdadMenarquia(true);
				historiaMenstrual.setOtraEdadMenarquia(rs.getString("otra_edad_menarquia"));
				historiaMenstrual.setNombreEdadMenarquia(rs.getString("nombre_edad_menarquia"));
				
				historiaMenstrual.setCodigoEdadMenopausia(rs.getInt("codigo_edad_menopausia"));
				//Se verifica si existe edad de menopausia
				if(historiaMenstrual.getCodigoEdadMenopausia()!=ConstantesBD.codigoNuncaValido)
					historiaMenstrual.setExisteEdadMenopausia(true);
				historiaMenstrual.setOtraEdadMenopausia(rs.getString("otra_edad_menopausia"));
				historiaMenstrual.setNombreEdadMenopausia(rs.getString("nombre_edad_menopausia"));
			}
			else
				historiaMenstrual.setCodigoPaciente(codigoPaciente);
			//***********************************************************************************************
			//***********CONSULTA DEL ULTIMO HISTORICO MENSTRUAL*******************************************
			consulta = "SELECT "+ 
				"coalesce(agh.ciclo_menstrual||'','') AS ciclo_menstrual, "+
				"coalesce(agh.duracion_menstruacion||'','') AS duracion_menstruacion, "+
				"coalesce(to_char(agh.fecha_ultima_regla,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_ultima_regla, "+
				"CASE WHEN agh.dolor_menstruacion is null then '' when agh.dolor_menstruacion = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS dolor_menstruacion, "+
				"agh.concepto_menstruacion AS codigo_concepto_menstruacion, "+ 
				"cm.nombre AS nombre_concepto_menstruacion, "+
				"coalesce(agh.observaciones_menstruacion,'') AS observaciones_menstruacion "+ 
				"FROM ant_gineco_histo agh "+ 
				"INNER JOIN conceptos_menstruacion cm ON(cm.codigo=agh.concepto_menstruacion) "; 
			
			//Si la búsqueda es por numero de solicitud 
			if(!numeroSolicitud.equals(""))
			{
				consulta += "INNER JOIN val_historia_menstrual vh ON(vh.codigo_paciente = agh.codigo_paciente AND vh.historico_ant = agh.codigo) "+
					"WHERE vh.valoracion = "+numeroSolicitud;
				
			}
			else
			{
				//Dependiendo del tipo de base de datos se completa la consulta
				if(tipoBD==DaoFactory.ORACLE)
					consulta += "WHERE agh.codigo_paciente = "+codigoPaciente+" AND rownum = 1 order by agh.codigo desc";
				else if(tipoBD==DaoFactory.POSTGRESQL)
					consulta += "WHERE agh.codigo_paciente = "+codigoPaciente+" order by agh.codigo desc "+ValoresPorDefecto.getValorLimit1()+" 1";
			}
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				historiaMenstrual.setCicloMenstrual(rs.getString("ciclo_menstrual"));
				historiaMenstrual.setCicloMenstrualAnterior(historiaMenstrual.getCicloMenstrual());
				historiaMenstrual.setDuracionMenstruacion(rs.getString("duracion_menstruacion"));
				historiaMenstrual.setDuracionMenstruacionAnterior(historiaMenstrual.getDuracionMenstruacion());
				historiaMenstrual.setFechaUltimaRegla(rs.getString("fecha_ultima_regla"));
				historiaMenstrual.setFechaUltimaReglaAnterior(historiaMenstrual.getFechaUltimaRegla());
				String dolor = rs.getString("dolor_menstruacion");
				if(dolor.equals(ConstantesBD.acronimoSi))
					historiaMenstrual.setDolorMenstruacion(true);
				else if(dolor.equals(ConstantesBD.acronimoNo))
					historiaMenstrual.setDolorMenstruacion(false);
				historiaMenstrual.setDolorMenstruacionAnterior(historiaMenstrual.getDolorMenstruacion());
				historiaMenstrual.setCodigoConceptoMenstruacion(rs.getInt("codigo_concepto_menstruacion"));
				historiaMenstrual.setCodigoConceptoMenstruacionAnterior(historiaMenstrual.getCodigoConceptoMenstruacion());
				historiaMenstrual.setNombreConceptoMenstruacion(rs.getString("nombre_concepto_menstruacion"));
				historiaMenstrual.setNombreConceptoMenstruacionAnterior(historiaMenstrual.getNombreConceptoMenstruacion());
				historiaMenstrual.setObservacionesMenstruales(rs.getString("observaciones_menstruacion"));
				historiaMenstrual.setObservacionesMenstrualesAnteriores(historiaMenstrual.getObservacionesMenstruales());
				
				
			}
			//**********************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarHistoriaMenstrual: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return historiaMenstrual;
	}
	
	/**
	 * Método para obtener los rangos edad menarquia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerRangosEdadMenarquia(Connection con,boolean incluirOpcionInvalida)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "select codigo,nombre from rangos_edad_menarquia "+(incluirOpcionInvalida?"":" WHERE codigo <> "+ConstantesBD.codigoNuncaValido);
			
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerRangosEdadMenarquia: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return resultados;
	}
	
	/**
	 * Método para obtener los rangos de la edad de menopausia
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerRangosEdadMenopausia(Connection con,boolean incluirOpcionInvalida)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "select codigo,nombre from rangos_edad_menopausia "+(incluirOpcionInvalida?"":" WHERE codigo <> "+ConstantesBD.codigoNuncaValido);
			
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerRangosEdadMenopausia: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		return resultados;
	} 
	
	/**
	 * Método para obtener los conceptos de menstruacion
	 * @param con
	 * @param incluirOpcionInvalida
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosMenstruacion(Connection con,boolean incluirOpcionInvalida)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "select codigo,nombre from conceptos_menstruacion "+(incluirOpcionInvalida?"":" WHERE codigo <> "+ConstantesBD.codigoNuncaValido);
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getString("codigo"));
				elemento.put("nombre", rs.getString("nombre"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConceptosMenstruacion: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método para cargar toda la información del componente de oftalmología
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @return
	 */
	public static DtoOftalmologia cargarOftalmologia(Connection con,String numeroSolicitud,int codigoInstitucion)
	{
		DtoOftalmologia oftalmologia = new DtoOftalmologia();
		
			
		//**************************SECCION SISTEMA MOTIVO CONSULTA************************************************
		cargarSeccionMotivoConsultaOftalmologia(con,numeroSolicitud,codigoInstitucion,oftalmologia);
		//*********************************************************************************************************
		//**************************SECCION EXAMEN OFTALMOLOGICO****************************************************
		cargarSeccionExamenOftalmologico(con,numeroSolicitud,codigoInstitucion,oftalmologia);
		//***********************************************************************************************************
		
		
		return oftalmologia;
		
	}
	
	
	/**
	 * Método implementado para cargar la informacion de la seccion examen oftalmológico
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @param oftalmologia
	 */
	private static void cargarSeccionExamenOftalmologico(Connection con, String numeroSolicitud, int codigoInstitucion, DtoOftalmologia oftalmologia) 
	{
		ResultSetDecorator rs = null;
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "";
			Statement st = null;
			
			//Si no se envía numero de solicitud quiere decir que se va a consultar lo parametrizable
			//es decir, como si apenas se fuera capturar la informacion
			if(numeroSolicitud.equals(""))
			{
				//SE cargan los tipos de agudeza por institucion (LEJOS/CERCA)
				consulta = "SELECT " +
					"av.codigo AS codigo," +
					"ta.nombre AS nombre " +
					"FROM agudeza_visual_inst av " +
					"INNER JOIN tipos_agudeza_visual ta ON(ta.codigo=av.tipo_agudeza_visual) " +
					"WHERE av.institucion = ? AND av.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND ta.cerca = ?";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,codigoInstitucion);
				pst.setBoolean(2,false);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				while(rs.next())
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo", rs.getObject("codigo"));
					elemento.put("nombre", rs.getObject("nombre"));
					oftalmologia.getTiposAgudezaLejos().add(elemento);
				}
				
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,codigoInstitucion);
				pst.setBoolean(2,true);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				while(rs.next())
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo", rs.getObject("codigo"));
					elemento.put("nombre", rs.getObject("nombre"));
					oftalmologia.getTiposAgudezaCerca().add(elemento);
				}
				
				//SE carga e equipo de tonometria
				consulta = "SELECT " +
					"eti.codigo As codigo, " +
					"et.nombre " +
					"FROM equipo_tonometria_inst eti " +
					"INNER JOIN equipo_tonometria et ON(et.codigo=eti.equipo_tonometria) " +
					"WHERE " +
					"eti.institucion = ? AND eti.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY et.nombre";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				while(rs.next())
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigo", rs.getObject("codigo"));
					elemento.put("nombre", rs.getObject("nombre"));
					oftalmologia.getEquipos().add(elemento);
				}
				
			}
			//Si se envió numero de solicitud quiere decir que se desea consultar el resumen de la informacion
			else
			{
				//Se consulta el resumen de la agudeza visual
				consulta = "SELECT " +
					"vav.ojo_derecho AS ojo_derecho," +
					"vav.correccion AS correccion," +
					"tav.cerca AS cerca," +
					"avi.codigo AS codigo," +
					"tav.nombre AS nombre " +
					"FROM val_agudeza_visual vav " +
					"INNER JOIN agudeza_visual_inst avi ON(avi.codigo=vav.agudeza_visual_inst) " +
					"INNER JOIN tipos_agudeza_visual tav ON(tav.codigo=avi.tipo_agudeza_visual) " +
					"WHERE vav.numero_solicitud = "+numeroSolicitud;
				
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					boolean ojoDerecho = rs.getBoolean("ojo_derecho");
					boolean cerca = rs.getBoolean("cerca"); 
					boolean correccion = rs.getBoolean("correccion");
					
					if(cerca)
					{
						if(ojoDerecho&&!correccion)
						{
							oftalmologia.setCodigoOjoDerechoCercaSinCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoDerechoCercaSinCorrecion(rs.getString("nombre"));
						}
						else if(ojoDerecho&&correccion)
						{
							oftalmologia.setCodigoOjoDerechoCercaConCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoDerechoCercaConCorrecion(rs.getString("nombre"));
						}
						else if(!ojoDerecho&&!correccion)
						{
							oftalmologia.setCodigoOjoIzquierdoCercaSinCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoIzquierdoCercaSinCorrecion(rs.getString("nombre"));
						}
						else if(!ojoDerecho&&correccion)
						{
							oftalmologia.setCodigoOjoIzquierdoCercaConCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoIzquierdoCercaConCorrecion(rs.getString("nombre"));
						}
					}
					else
					{
						if(ojoDerecho&&!correccion)
						{
							oftalmologia.setCodigoOjoDerechoLejosSinCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoDerechoLejosSinCorrecion(rs.getString("nombre"));
						}
						else if(ojoDerecho&&correccion)
						{
							oftalmologia.setCodigoOjoDerechoLejosConCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoDerechoLejosConCorrecion(rs.getString("nombre"));
						}
						else if(!ojoDerecho&&!correccion)
						{
							oftalmologia.setCodigoOjoIzquierdoLejosSinCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoIzquierdoLejosSinCorrecion(rs.getString("nombre"));
						}
						else if(!ojoDerecho&&correccion)
						{
							oftalmologia.setCodigoOjoIzquierdoLejosConCorrecion(rs.getInt("codigo"));
							oftalmologia.setNombreOjoIzquierdoLejosConCorrecion(rs.getString("nombre"));
						}
					}
					
					
				}
				
				//Se consulta el resumen de la refracción y la queratometría
				consulta = "SELECT " +
					"veo.ojoderecho as ojoderecho," +
					"veo.valor as valor," +
					"veo.tipo_refraccion_querato AS tipo " +
					"FROM val_examen_oftal veo " +
					"WHERE veo.numero_solicitud = "+numeroSolicitud;
				
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				while(rs.next())
				{
					boolean ojoDerecho = rs.getBoolean("ojoderecho");
					String valor = rs.getString("valor");
					int tipo = rs.getInt("tipo");
					
					switch(tipo)
					{
						case ConstantesBD.codigoExamenOftalADD:
							oftalmologia.setAdd(valor);
						break;
						case ConstantesBD.codigoExamenOftalDIP:
							oftalmologia.setDip(valor);
						break;
						case ConstantesBD.codigoExamenOftalSK:
							if(ojoDerecho)
								oftalmologia.setOjoDerechoSK(valor);
							else
								oftalmologia.setOjoIzquierdoSK(valor);
						break;
						case ConstantesBD.codigoExamenOftalSKCiclo:
							if(ojoDerecho)
								oftalmologia.setOjoDerechoSKCiclo(valor);
							else
								oftalmologia.setOjoIzquierdoSKCiclo(valor);
						break;
						case ConstantesBD.codigoExamenOftalSKSubj:
							if(ojoDerecho)
								oftalmologia.setOjoDerechoSKSubj(valor);
							else
								oftalmologia.setOjoIzquierdoSKSubj(valor);
						break;
						case ConstantesBD.codigoExamenOftalQueratometria:
							if(ojoDerecho)
								oftalmologia.setOjoDerechoQueratometria(valor);
							else
								oftalmologia.setOjoIzquierdoQueratometria(valor);
						break;
					}
				}
				
				
				//Se consulta el resumen de la tonometria
				consulta = "SELECT " +
					"coalesce(vot.od,'') AS ojo_derecho, " +
					"coalesce(vot.os,'') AS ojo_izquierdo, " +
					"vot.equipo_tonometria AS codigo_equipo," +
					"getnombreequipotonometriaoft(eti.equipo_tonometria) AS nombre_equipo " +
					"FROM valoracion_of_tonometria vot " +
					"INNER JOIN equipo_tonometria_inst eti ON(eti.codigo = vot.equipo_tonometria)  " +
					"WHERE vot.numero_solicitud = "+numeroSolicitud;
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					oftalmologia.setOjoDerechoTonometria(rs.getString("ojo_derecho"));
					oftalmologia.setOjoIzquierdoTonometria(rs.getString("ojo_izquierdo"));
					oftalmologia.setCodigoEquipoTonometria(rs.getInt("codigo_equipo"));
					oftalmologia.setNombreEquipoTonometria(rs.getString("nombre_equipo"));
				}
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSeccionExamenOftalmologico: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
	}

	/**
	 * Método implementado para cargar la seccion motivo consulta de oftalmologia
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @param oftalmologia
	 */
	private static void cargarSeccionMotivoConsultaOftalmologia(Connection con, String numeroSolicitud, int codigoInstitucion, DtoOftalmologia oftalmologia) 
	{
		ResultSetDecorator rs = null;
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "";
			Statement st = null;
			
			//Si no se envía numero de solicitud quiere decir que se va a consultar lo parametrizable
			//es decir, como si apenas se fuera capturar la informacion
			if(numeroSolicitud.equals(""))
			{
				//1) Se consultan los tipos sintoma que aplican por institucion -------------------------------------------------
				consulta = "SELECT " +
					"tsi.codigo AS codigo, " +
					"ts.nombre AS nombre " +
					"FROM tipo_sintoma_institucion tsi " +
					"INNER JOIN tipos_sintoma_oftal ts ON(ts.codigo = tsi.tipo_sintoma) " +
					"WHERE tsi.institucion = "+codigoInstitucion+" AND tsi.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				while(rs.next())
				{
					InfoDatosInt tipoSintoma = new InfoDatosInt();
					tipoSintoma.setCodigo(rs.getInt("codigo"));
					tipoSintoma.setNombre(rs.getString("nombre"));
					oftalmologia.getTiposSintoma().add(tipoSintoma);
				}
				
				//2) Se consultan los sintomas que aplican para cada tipo de sintoma
				consulta = "SELECT " +
					"spt.codigo AS codigo," +
					"so.nombre " +
					"FROM sintoma_por_tipo spt " +
					"INNER JOIN sintoma_oftalmologia so ON(so.codigo=spt.sintoma) " +
					"WHERE spt.tipo_sintoma = ?";
				
				for(InfoDatosInt tipoSintoma:oftalmologia.getTiposSintoma())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1,tipoSintoma.getCodigo());
					rs = new ResultSetDecorator(pst.executeQuery());
					
					ArrayList<HashMap<String,Object>> opcionesSintomas = new ArrayList<HashMap<String,Object>>();
					
					while(rs.next())
					{
						HashMap<String, Object> elemento = new HashMap<String, Object>();
						elemento.put("codigo",rs.getObject("codigo"));
						elemento.put("nombre", rs.getObject("nombre"));
						opcionesSintomas.add(elemento);
					}
					
					//Se agrega el grupo de opciones del sintoma correspondiente al tipo de sintoma tomado
					oftalmologia.getOpcionesSintoma().add(opcionesSintomas);
				}
				
				//3) Se consultan las localizaciones que aplican para cada tipo de sintoma
				consulta = "SELECT " +
					"lpt.codigo AS codigo, " +
					"lo.nombre " +
					"FROM localizacion_por_tipo lpt " +
					"INNER JOIN localizacion_oftal lo ON(lo.codigo=lpt.localizacion) " +
					"WHERE lpt.tipo_sintoma = ?";
				
				for(InfoDatosInt tipoSintoma:oftalmologia.getTiposSintoma())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1,tipoSintoma.getCodigo());
					rs = new ResultSetDecorator(pst.executeQuery());
					
					ArrayList<HashMap<String,Object>> opcionesLocalizacion = new ArrayList<HashMap<String,Object>>();
					
					while(rs.next())
					{
						HashMap<String, Object> elemento = new HashMap<String, Object>();
						elemento.put("codigo",rs.getObject("codigo"));
						elemento.put("nombre", rs.getObject("nombre"));
						opcionesLocalizacion.add(elemento);
					}
					
					//Se agrega el grupo de opciones de la localizacion correspondiente al tipo de sintoma tomado
					oftalmologia.getOpcionesLocalizacion().add(opcionesLocalizacion);
				}
				
				//4) Se consultan las severidades que aplican para cada tipo de sintoma
				consulta = "SELECT " +
					"spt.codigo AS codigo, " +
					"so.nombre_severidad As nombre " +
					"from severidad_por_tipo spt " +
					"INNER JOIN severidad_oftalmologia so ON(so.codigo=spt.severidad) " +
					"WHERE spt.tipo_sintoma = ?";
				
				for(InfoDatosInt tipoSintoma:oftalmologia.getTiposSintoma())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1,tipoSintoma.getCodigo());
					rs = new ResultSetDecorator(pst.executeQuery());
					
					ArrayList<HashMap<String,Object>> opcionesSeveridad = new ArrayList<HashMap<String,Object>>();
					
					while(rs.next())
					{
						HashMap<String, Object> elemento = new HashMap<String, Object>();
						elemento.put("codigo",rs.getObject("codigo"));
						elemento.put("nombre", rs.getObject("nombre"));
						opcionesSeveridad.add(elemento);
					}
					
					//Se agrega el grupo de opciones de la severidad correspondiente al tipo de sintoma tomado
					oftalmologia.getOpcionesSeveridad().add(opcionesSeveridad);
					
					
				}
				
				//Se agrega un solo registro por cada tipo sintoma
				for(int i=0;i<oftalmologia.getTiposSintoma().size();i++)
					oftalmologia.setRegistroSintomas("numRegistros_"+i, "1");
				
			}
			//Si se envió numero de solicitud quiere decir que se desea consultar el resumen de la informacion
			else
			{
				//Se consulta lo que se registró con los sintomas parametrizados
				consulta = "SELECT "+
					"vo.codigo AS codigo, "+
					"vo.numero_solicitud AS numero_solicitud, "+ 
					"getnombretiposintomaoft(tsi.tipo_sintoma) AS nombre_tipo_sintoma, "+
					"vo.sintoma AS codigo_sintoma, "+
					"getnombresintomaoft(spt.sintoma) AS nombre_sintoma, "+ 
					"coalesce(vo.localizacion,"+ConstantesBD.codigoNuncaValido+") AS codigo_localizacion, "+ 
					"coalesce(getnombrelocalizacionoft(lpt.localizacion),'') AS nombre_localizacion, "+ 
					"coalesce(vo.severidad,"+ConstantesBD.codigoNuncaValido+") AS codigo_severidad, "+
					"coalesce(getnombreseveridadoft(st.severidad),'') AS nombre_severidad "+ 
					"FROM val_oftal_sintoma vo "+ 
					"INNER JOIN sintoma_por_tipo spt ON(spt.codigo=vo.sintoma) "+ 
					"INNER JOIN tipo_sintoma_institucion tsi ON(tsi.codigo=spt.tipo_sintoma) "+ 
					"LEFT OUTER JOIN localizacion_por_tipo lpt ON(lpt.codigo=vo.localizacion) "+ 
					"LEFT OUTER JOIN severidad_por_tipo st ON(st.codigo=vo.severidad) "+ 
					"WHERE "+ 
					"vo.numero_solicitud = "+numeroSolicitud+" ORDER BY nombre_tipo_sintoma";
				st = con.createStatement( );
				oftalmologia.setRegistroSintomas(UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true));
				
				//Se consultan los otros sintomas
				consulta = "SELECT " +
					"numero_solicitud," +
					"nombre_sintoma," +
					"coalesce(severidad,'') as nombre_severidad," +
					"coalesce(localizacion,'') as nombre_localizacion " +
					"FROM otro_sintoma_oftal WHERE numero_solicitud = "+numeroSolicitud;
				st = con.createStatement( );
				oftalmologia.setRegistroOtrosSintomas(UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarOftalmologia: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método implementado para ingrear informacion de oftalmología (Componente)
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	public static ResultadoBoolean ingresarOftalmologia(Connection con,DtoOftalmologia oftalmologia)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		//***************SECCION MOTIVO CONSULTA***********************************************
		resultado = insertarSeccionMotivoConsultaOftalmologia(con,oftalmologia);
		//**************************************************************************************
		//**************SECCION EXAMEN OFTALMOLOGIA**********************************************
		if(resultado.isTrue())
			resultado = insertarSeccionExamenOftalmologia(con,oftalmologia);
		//****************************************************************************************
		
		return resultado;
	}

	/**
	 * Método para insertar los datos de la seccion examen oftalmología
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	private static ResultadoBoolean insertarSeccionExamenOftalmologia(Connection con, DtoOftalmologia oftalmologia) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "";
			
			//Se inserta la agudeza visual
			consulta = "INSERT INTO val_agudeza_visual (numero_solicitud,agudeza_visual_inst,ojo_derecho,correccion) VALUES (?,?,?,?)";
			logger.info("opaso por aqui 1 "+oftalmologia.getCodigoOjoDerechoLejosSinCorrecion());
			if(oftalmologia.getCodigoOjoDerechoLejosSinCorrecion()>0)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoDerechoLejosSinCorrecion());
				pst.setBoolean(3,true);
				pst.setBoolean(4, false);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OD (lejos - sin corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 2 "+oftalmologia.getCodigoOjoDerechoLejosConCorrecion());
			if(oftalmologia.getCodigoOjoDerechoLejosConCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoDerechoLejosConCorrecion());
				pst.setBoolean(3,true);
				pst.setBoolean(4, true);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OD (lejos - con corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 3 "+oftalmologia.getCodigoOjoDerechoCercaSinCorrecion());
			if(oftalmologia.getCodigoOjoDerechoCercaSinCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoDerechoCercaSinCorrecion());
				pst.setBoolean(3,true);
				pst.setBoolean(4, false);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OD (cerca - sin corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 4 "+oftalmologia.getCodigoOjoDerechoCercaConCorrecion());
			if(oftalmologia.getCodigoOjoDerechoCercaConCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoDerechoCercaConCorrecion());
				pst.setBoolean(3,true);
				pst.setBoolean(4, true);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OD (cerca - con corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 4 "+oftalmologia.getCodigoOjoIzquierdoLejosSinCorrecion());
			if(oftalmologia.getCodigoOjoIzquierdoLejosSinCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoIzquierdoLejosSinCorrecion());
				pst.setBoolean(3,false);
				pst.setBoolean(4, false);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OS (lejos - sin corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 5 "+oftalmologia.getCodigoOjoIzquierdoLejosConCorrecion());
			if(oftalmologia.getCodigoOjoIzquierdoLejosConCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoIzquierdoLejosConCorrecion());
				pst.setBoolean(3,false);
				pst.setBoolean(4, true);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OS (lejos - con corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 6 "+oftalmologia.getCodigoOjoIzquierdoCercaSinCorrecion());
			if(oftalmologia.getCodigoOjoIzquierdoCercaSinCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoIzquierdoCercaSinCorrecion());
				pst.setBoolean(3,false);
				pst.setBoolean(4, false);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OS (cerca - sin corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 7 "+oftalmologia.getCodigoOjoIzquierdoCercaConCorrecion());
			if(oftalmologia.getCodigoOjoIzquierdoCercaConCorrecion()>0&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,oftalmologia.getCodigoOjoIzquierdoCercaConCorrecion());
				pst.setBoolean(3,false);
				pst.setBoolean(4, true);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la agudeza visual en OS (cerca - con corrección) (Oftalmología)");
				}
			}
			logger.info("opaso por aqui 8");
			//Se inserta la refraccion y la queratometría
			consulta = "INSERT INTO val_examen_oftal (numero_solicitud,tipo_refraccion_querato,ojoderecho,valor) VALUES (?,?,?,?)";
			
			if(!oftalmologia.getOjoDerechoSK().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSK);
				pst.setBoolean(3,true);
				pst.setString(4,oftalmologia.getOjoDerechoSK());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK en OD (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoDerechoSKCiclo().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSKCiclo);
				pst.setBoolean(3,true);
				pst.setString(4,oftalmologia.getOjoDerechoSKCiclo());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK Ciclo en OD (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoDerechoSKSubj().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSKSubj);
				pst.setBoolean(3,true);
				pst.setString(4,oftalmologia.getOjoDerechoSKSubj());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK Subj en OD (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoDerechoQueratometria().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalQueratometria);
				pst.setBoolean(3,true);
				pst.setString(4,oftalmologia.getOjoDerechoQueratometria());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la queratometría en OD (Oftalmología)");
				}
			}
			
			
			
			if(!oftalmologia.getOjoIzquierdoSK().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSK);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getOjoIzquierdoSK());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK en OS (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoIzquierdoSKCiclo().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSKCiclo);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getOjoIzquierdoSKCiclo());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK Ciclo en OS (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoIzquierdoSKSubj().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalSKSubj);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getOjoIzquierdoSKSubj());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la refracción SK Subj en OS (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getOjoIzquierdoQueratometria().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalQueratometria);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getOjoIzquierdoQueratometria());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la queratometría en OS (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getAdd().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalADD);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getAdd());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar el campo Add dela refracción (Oftalmología)");
				}
			}
			
			if(!oftalmologia.getDip().trim().equals("")&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				pst.setInt(2,ConstantesBD.codigoExamenOftalDIP);
				pst.setBoolean(3,false);
				pst.setString(4,oftalmologia.getDip());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar el campo DIP dela refracción (Oftalmología)");
				}
			}
			
			
			//Se inserta la tonometría
			consulta = "INSERT INTO valoracion_of_tonometria (numero_solicitud,od,os,equipo_tonometria) VALUES (?,?,?,?)";
			if((!oftalmologia.getOjoDerechoTonometria().trim().equals("")||!oftalmologia.getOjoIzquierdoTonometria().trim().equals(""))&&resultado.isTrue())
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
				if(!oftalmologia.getOjoDerechoTonometria().trim().equals(""))
					pst.setString(2,oftalmologia.getOjoDerechoTonometria());
				else
					pst.setNull(2,Types.VARCHAR);
				if(!oftalmologia.getOjoIzquierdoTonometria().trim().equals(""))
					pst.setString(3,oftalmologia.getOjoIzquierdoTonometria());
				else
					pst.setNull(3,Types.VARCHAR);
				pst.setInt(4,oftalmologia.getCodigoEquipoTonometria());
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Error al ingresar la información de tonometría (Oftalmología)");
				}
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Erro en insertarSeccionExamenOftalmologia: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error ingresando la información del exámen de oftalmología: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		return resultado;
	}

	/**
	 * Método implementado para insertar datos de la seccion motivo consulta oftalmologia
	 * @param con
	 * @param oftalmologia
	 * @return
	 */
	private static ResultadoBoolean insertarSeccionMotivoConsultaOftalmologia(Connection con, DtoOftalmologia oftalmologia) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		PreparedStatementDecorator pst= null;

		try
		{
			//Se insertan los sintomas parametrizados
			String consulta = "INSERT INTO val_oftal_sintoma (codigo,numero_solicitud,sintoma,localizacion,severidad) values (?,?,?,?,?)";
			for(int i=0;i<oftalmologia.getTiposSintoma().size();i++)
				for(int j=0;j<oftalmologia.getNumRegistroSintomasTipo(i);j++)
					//Se verifica que se haya seleccionado un sintoma
					if(!oftalmologia.getRegistroSintomas("codigoSintoma_"+i+"_"+j).toString().equals(""))
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						int secuenciaValOftalSintoma = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_val_oftal_sintoma");
						pst.setInt(1,secuenciaValOftalSintoma);
						pst.setInt(2,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
						pst.setInt(3,Utilidades.convertirAEntero(oftalmologia.getRegistroSintomas("codigoSintoma_"+i+"_"+j).toString()));
						//Se pregunta si se ingresó localizacion
						if(!oftalmologia.getRegistroSintomas("codigoLocalizacion_"+i+"_"+j).toString().equals(""))
							pst.setInt(4,Utilidades.convertirAEntero(oftalmologia.getRegistroSintomas("codigoLocalizacion_"+i+"_"+j).toString()));
						else
							pst.setNull(4,Types.INTEGER);
						//Se pregunta si se ingresó severidad
						if(!oftalmologia.getRegistroSintomas("codigoSeveridad_"+i+"_"+j).toString().equals(""))
							pst.setInt(5,Utilidades.convertirAEntero(oftalmologia.getRegistroSintomas("codigoSeveridad_"+i+"_"+j).toString()));
						else
							pst.setNull(5,Types.INTEGER);
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Ocurrió error ingresando un síntoma de oftalmología");
							//Se cierra ciclo
							j = oftalmologia.getNumRegistroSintomasTipo(i);
							i = oftalmologia.getTiposSintoma().size();
						}
					}
			
			//Se insertan los otros sintomas
			if(resultado.isTrue())
			{
				consulta = "INSERT INTO otro_sintoma_oftal (numero_solicitud,nombre_sintoma,severidad,localizacion) VALUES (?,?,?,?)";
				for(int i=0;i<oftalmologia.getNumRegistroOtrosSintomas();i++)
					//Se verifica que se haya ingresado un sintoma y una localizacion
					if(!oftalmologia.getRegistroOtrosSintomas("nombreSintoma_"+i).toString().trim().equals("")&&
						!oftalmologia.getRegistroOtrosSintomas("nombreLocalizacion_"+i).toString().trim().equals(""))
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst.setInt(1,Utilidades.convertirAEntero(oftalmologia.getNumeroSolicitud()));
						pst.setString(2,oftalmologia.getRegistroOtrosSintomas("nombreSintoma_"+i).toString());
						if(!oftalmologia.getRegistroOtrosSintomas("nombreSeveridad_"+i).toString().trim().equals(""))
							pst.setString(3,oftalmologia.getRegistroOtrosSintomas("nombreSeveridad_"+i).toString());
						else
							pst.setNull(3,Types.VARCHAR);
						pst.setString(4,oftalmologia.getRegistroOtrosSintomas("nombreLocalizacion_"+i).toString());
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Ocurrió error ingresando un síntoma adicional de oftalmología");
							//Se cierra ciclo
							i = oftalmologia.getNumRegistroOtrosSintomas();
						}
					}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarSeccionMotivoConsultaOftalmologia: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error ingresando la información del motivo de la consulta de oftalmología: "+e);
			
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		
		return resultado;
	}
	
	/**
	 * Método implementado para cargar la informacion del componente de pediatría
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @param edadPaciente
	 * @return
	 */
	public static DtoPediatria cargarPediatria(Connection con,String codigoPaciente,String numeroSolicitud,int edadPaciente,UsuarioBasico usuario)
	{
		DtoPediatria pediatria = new DtoPediatria();


		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			pediatria.setCodigoPaciente(Utilidades.convertirAEntero(codigoPaciente));
			pediatria.setNumeroSolicitud(numeroSolicitud);
			pediatria.setEdadPaciente(edadPaciente);
			
			String consulta = "";
			
			//************SE CONSULTA EL DESARROLLO*********************************************
			consulta = "SELECT " +
				"vdp.codigo AS codigo," +
				"vdp.nombre AS nombre," +
				"tdp.codigo AS codigo_tipo," +
				"tdp.nombre AS nombre_tipo," +
				"coalesce(vvdp.descripcion,'') AS descripcion," +
				"coalesce(vvdp.edad_texto,'') AS edad_texto," +
				"coalesce(to_char(vvdp.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_grabacion," +
				"coalesce(substr(vvdp.hora_grabacion,0,6),'') AS hora_grabacion," +
				"coalesce(vvdp.codigo_medico,"+ConstantesBD.codigoNuncaValido+") AS codigo_medico " +
				"FROM val_des_pediatrico vdp " +
				"INNER JOIN tipos_des_ped tdp ON(tdp.codigo=vdp.codigo_tipo_desarrollo) " +
				"LEFT OUTER JOIN val_valores_des_ped vvdp ON(vvdp.codigo_valores_desarrollo=vdp.codigo AND vvdp.codigo_paciente = "+codigoPaciente+") ";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				DtoDesarrolloPediatria desarrollo = new DtoDesarrolloPediatria();
				desarrollo.setCodigo(rs.getInt("codigo"));
				desarrollo.setNombre(rs.getString("nombre"));
				desarrollo.setCodigoTipo(rs.getInt("codigo_tipo"));
				desarrollo.setNombreTipo(rs.getString("nombre_tipo"));
				desarrollo.setDescripcion(rs.getString("descripcion"));
				//Si ya existe informacion guardada se marca como existente
				if(!desarrollo.getDescripcion().equals(""))
					desarrollo.setExisteBD(true);
				desarrollo.setEdadTexto(rs.getString("edad_texto"));
				if(!desarrollo.getEdadTexto().equals(""))
					desarrollo.setExisteBD(true);
				desarrollo.setFechaGrabacion(rs.getString("fecha_grabacion"));
				desarrollo.setHoraGrabacion(rs.getString("hora_grabacion"));
				int codigoMedico = rs.getInt("codigo_medico");
				if(codigoMedico>0)
					desarrollo.getProfesional().cargarUsuarioBasico(con, codigoMedico);
				else if(usuario!=null)
					desarrollo.setProfesional(usuario);
				pediatria.getDesarrollos().add(desarrollo);
				
			}
			//************************************************************************************
			//**********SE CONSULTAN LAS OBSERVACIONES********************************************
			consulta = "SELECT " +
				"op.consecutivo AS consecutivo," +
				"op.codigo_paciente," +
				"op.tipo AS codigo_tipo," +
				"getintegridaddominio(op.tipo) as nombre_tipo," +
				"op.valor as valor," +
				"coalesce(to_char(op.fecha,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha," +
				"coalesce(substr(op.hora,0,6),'') as hora," +
				"coalesce(op.usuario,'') as usuario " +
				"FROM observaciones_pediatria op WHERE op.codigo_paciente = "+codigoPaciente;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				DtoObservacionesPediatria observacion = new DtoObservacionesPediatria();
				observacion.setConsecutivo(rs.getString("consecutivo"));
				observacion.setCodigoTipo(rs.getString("codigo_tipo"));
				observacion.setNombreTipo(rs.getString("nombre_tipo"));
				observacion.setValor(rs.getString("valor"));
				observacion.setFecha(rs.getString("fecha"));
				observacion.setHora(rs.getString("hora"));
				String login = rs.getString("usuario");
				if(!login.equals(""))
					observacion.getUsuario().cargarUsuarioBasico(con, login);
				else if(usuario!=null)
					observacion.setUsuario(usuario);
				
				pediatria.getObservaciones().add(observacion);
			}
			//************************************************************************************
			
			//**********SE CONSULTAN LAS EDADES DE ALIMENTACION***********************************
			//Solo aplica para pacientes de una edad mayor igual a 2 años
			if(pediatria.getEdadPaciente()>=2)
			{
				consulta = "SELECT " +
					"ea.codigo AS codigo, " +
					"ea.nombre AS nombre, " +
					"coalesce(vea.edad_texto,'') as edad_texto, " +
					"coalesce(to_char(vea.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_grabacion, " +
					"coalesce(substr(vea.hora_grabacion,0,6),'') AS hora_grabacion, " +
					"coalesce(vea.codigo_medico,"+ConstantesBD.codigoNuncaValido+") AS codigo_medico " +
					"FROM edades_alimentacion  ea " +
					"LEFT OUTER JOIN val_edades_alimentacion vea ON(vea.edad_alimentacion=ea.codigo AND vea.codigo_paciente = "+codigoPaciente+") WHERE ea.codigo > 0";
				st = con.createStatement( );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					DtoEdadAlimentacionPediatria edad = new DtoEdadAlimentacionPediatria();
					edad.setCodigoEdad(rs.getInt("codigo"));
					edad.setNombreEdad(rs.getString("nombre"));
					edad.setEdadTexto(rs.getString("edad_texto"));
					//Si ya se había registrado informacion se marca como existe BD = true
					if(!edad.getEdadTexto().equals(""))
						edad.setExisteBD(true);
					edad.setFechaGrabacion(rs.getString("fecha_grabacion"));
					edad.setHoraGrabacion(rs.getString("hora_grabacion"));
					int codigoMedico = rs.getInt("codigo_medico");
					if(codigoMedico>0)
						edad.getProfesional().cargarUsuarioBasico(con, codigoMedico);
					else
						edad.setProfesional(usuario);
					
					pediatria.getEdadesAlimentacion().add(edad);
				}
			}
			//*************************************************************************************
			//*********SE CONSULTAN LOS ESTADOS NUTRICIONALES**************************************
			consulta = "select codigo,nombre from estados_nutricionales";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				
				pediatria.getEstadosNutricionales().add(elemento);
			}
			//**************************************************************************************
			//********SE CONSULTAN LOS DEMAS CAMPOS************************************************
			consulta = "SELECT " +
				"CASE WHEN vp.lactancia_materna IS NULL THEN '' WHEN vp.lactancia_materna = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS lactancia_materna," +
				"CASE WHEN vp.otras_leches IS NULL THEN '' WHEN vp.otras_leches = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS otras_leches," +
				"vp.estado_nutricional AS codigo_estado_nutricional," +
				"en.nombre AS nombre_estado_nutricional," +
				"coalesce(vp.desc_otras_leches,'') AS desc_otras_leches," +
				"CASE WHEN vp.alimentacion_complementaria IS NULL THEN '' WHEN vp.alimentacion_complementaria = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS alimentacion_complementaria," +
				"coalesce(vp.desc_alim_comple,'') AS desc_alim_comple " +
				"FROM valoraciones_pediatricas vp " +
				"inner join estados_nutricionales en ON(en.codigo=vp.estado_nutricional) " +
				"WHERE vp.numero_solicitud = "+numeroSolicitud;
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				String valorBoolean = rs.getString("lactancia_materna");
				if(!valorBoolean.equals(""))
					pediatria.setLactanciaMaterna(UtilidadTexto.getBoolean(valorBoolean));
				valorBoolean = rs.getString("otras_leches");
				if(!valorBoolean.equals(""))
					pediatria.setOtrasLeches(UtilidadTexto.getBoolean(valorBoolean));
				pediatria.setCodigoEstadoNutricional(rs.getInt("codigo_estado_nutricional"));
				pediatria.setNombreEstadoNutricional(rs.getString("nombre_estado_nutricional"));
				pediatria.setDescripcionOtrasLeches(rs.getString("desc_otras_leches"));
				valorBoolean = rs.getString("alimentacion_complementaria");
				if(!valorBoolean.equals(""))
					pediatria.setAlimentacionComplementaria(UtilidadTexto.getBoolean(valorBoolean));
				pediatria.setDescripcionAlimentacionComplementaria(rs.getString("desc_alim_comple"));
				
			}
			//***************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPediatria: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		return pediatria;
	}
	
	/**
	 * Método implementado para insertar informacion del componente de pediatría
	 * @param con
	 * @param pediatria
	 * @return
	 */
	public static ResultadoBoolean ingresarPediatria(Connection con,DtoPediatria pediatria)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "";
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			String horaSistema = UtilidadFecha.getHoraActual(con);
			
			//************INSERTAR DESARROLLO **************************************************
			consulta = "INSERT INTO val_valores_des_ped " +
				"(codigo_paciente," +
				"codigo_valores_desarrollo," +
				"descripcion," +
				"edad_texto," +
				"fecha_grabacion," +
				"hora_grabacion," +
				"codigo_medico) " +
				"values (?,?,?,?,?,?,?)";
			for(DtoDesarrolloPediatria desarrollo:pediatria.getDesarrollos())
				//Se verifica que se haya ingresado información de desarrollo
				if(!desarrollo.isExisteBD()&&!desarrollo.getEdadTexto().trim().equals("")&&resultado.isTrue())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					pst.setInt(1,pediatria.getCodigoPaciente());
					pst.setInt(2,desarrollo.getCodigo());
					if(!desarrollo.getDescripcion().trim().equals(""))
						pst.setString(3,desarrollo.getDescripcion());
					else
						pst.setNull(3,Types.VARCHAR);
					pst.setString(4,desarrollo.getEdadTexto());
					if(!desarrollo.getFechaGrabacion().trim().equals(""))
						pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(desarrollo.getFechaGrabacion())));
					else
						pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaSistema)));
					if(!desarrollo.getHoraGrabacion().trim().equals(""))
						pst.setString(6,desarrollo.getHoraGrabacion());
					else
						pst.setString(6,horaSistema);
					pst.setInt(7,desarrollo.getProfesional().getCodigoPersona());
					
					if(pst.executeUpdate()<=0)
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Ocurrió error al registrar el desarrollo "+desarrollo.getNombre()+" (Pediatría)");
					}
				}
			//**********************************************************************************
			
			//*****************INSERTAR OBSERVACIONES*********************************************
			//Solo se insertan observaciones si hasta ahora todo ha marchado bien
			if(resultado.isTrue())
			{
				for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
					//Se verifica que se haya ingreso informacion en la observacion
					if(observacion.getConsecutivo().equals("")&&!observacion.getValor().trim().equals("")&&resultado.isTrue())
					{
						consulta = "INSERT INTO observaciones_pediatria (consecutivo,codigo_paciente,tipo,valor,fecha,hora,usuario) values (?,?,?,?,?,?,?)";
						
						int consecutivoObservacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_observaciones_pediatria");
						
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						
						pst.setDouble(1,Utilidades.convertirADouble(consecutivoObservacion+""));
						pst.setInt(2,pediatria.getCodigoPaciente());
						pst.setString(3,observacion.getCodigoTipo());
						pst.setString(4,observacion.getValor());
						if(!observacion.getFecha().trim().equals(""))
							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(observacion.getFecha())));
						else
							pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaSistema)));
						if(!observacion.getHora().trim().equals(""))
							pst.setString(6,observacion.getHora());
						else
							pst.setString(6,horaSistema);
						pst.setString(7,observacion.getUsuario().getLoginUsuario());
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Ocurrió error al registrar "+observacion.getNombreTipo()+" (Pediatría)");
						}
						
						
					}
			}
			//************************************************************************************
			
			//************INSERTAR EDADES ALIMENTACION********************************************
			if(resultado.isTrue())
			{
				for(DtoEdadAlimentacionPediatria edad:pediatria.getEdadesAlimentacion())
					//Se verifica que se haya ingresado informacion
					if(!edad.isExisteBD()&&!edad.getEdadTexto().trim().equals(""))
					{
						consulta = "INSERT INTO val_edades_alimentacion (codigo_paciente,edad_alimentacion,edad_texto,fecha_grabacion,hora_grabacion,codigo_medico) values (?,?,?,?,?,?)";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						pst.setInt(1, pediatria.getCodigoPaciente());
						pst.setInt(2,edad.getCodigoEdad());
						pst.setString(3,edad.getEdadTexto());
						if(!edad.getFechaGrabacion().trim().equals(""))
							pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(edad.getFechaGrabacion())));
						else
							pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaSistema)));
						if(!edad.getHoraGrabacion().trim().equals(""))
							pst.setString(5,edad.getHoraGrabacion());
						else
							pst.setString(5,horaSistema);
						pst.setInt(6,edad.getProfesional().getCodigoPersona());
						
						if(pst.executeUpdate()<=0)
						{
							resultado.setResultado(false);
							resultado.setDescripcion("Ocurrió error al registrar la edad alimentaria "+edad.getNombreEdad()+" (Pediatría)");
						}
						
					}
			}
			//************************************************************************************
			
			//**********INSERTAR REGISTRO DE VALORACIÓN PEDIÁTRICA*********************************
			if(resultado.isTrue())
			{
				consulta = "INSERT INTO valoraciones_pediatricas " +
					"(numero_solicitud," +
					"lactancia_materna," +
					"otras_leches," +
					"estado_nutricional," +
					"desc_otras_leches," +
					"alimentacion_complementaria," +
					"desc_alim_comple) " +
					"VALUES (?,?,?,?,?,?,?)";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				
				pst.setInt(1,Utilidades.convertirAEntero(pediatria.getNumeroSolicitud()));
				
				if(pediatria.getLactanciaMaterna()!=null)
					pst.setBoolean(2,pediatria.getLactanciaMaterna());
				else
					pst.setObject(2,null);
				
				if(pediatria.getOtrasLeches()!=null)
					pst.setBoolean(3,pediatria.getOtrasLeches());
				else
					pst.setObject(3,null);
				
				pst.setInt(4,pediatria.getCodigoEstadoNutricional());
				
				if(!pediatria.getDescripcionOtrasLeches().trim().equals(""))
					pst.setString(5,pediatria.getDescripcionOtrasLeches());
				else
					pst.setNull(5,Types.VARCHAR);
				
				if(pediatria.getAlimentacionComplementaria()!=null)
					pst.setBoolean(6,pediatria.getAlimentacionComplementaria());
				else
					pst.setObject(6,null);
				
				if(!pediatria.getDescripcionAlimentacionComplementaria().trim().equals(""))
					pst.setString(7,pediatria.getDescripcionAlimentacionComplementaria());
				else
					pst.setNull(7,Types.VARCHAR);
				
				if(pst.executeUpdate()<=0)
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Ocurrió error al registrar información de la valoración nutricional (Pediatría)");
				}
			}
			//**************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en ingresarPediatria: "+e);
			resultado.setResultado(false);
			resultado.setDescripcion("Ocurrió error al registrar información de pediatría (Pediatría)");
			
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		
		}
		return resultado;
	}
	
	/**
	* Método para obtener los signos y sintomas
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerSignosSintomas(Connection con,int institucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT consecutivo, descripcion FROM signos_sintomas WHERE institucion="+institucion+" ORDER BY descripcion";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("consecutivo", rs.getString("consecutivo"));
				elemento.put("descripcion", rs.getString("descripcion"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSignosSintomas: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	* Método para obtener las categorias triage
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerCategoriasTriage(Connection con,int institucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT consecutivo, nombre FROM categorias_triage WHERE institucion="+institucion+" ORDER BY nombre";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("consecutivo", rs.getString("consecutivo"));
				elemento.put("nombre", rs.getString("nombre"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCategoriasTriage: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	* Método para obtener los motivos de consulta urgencias
	* @param con
	* @param institucion
	* @return
	*/
	public static ArrayList<HashMap<String, Object>> obtenerMotivosConsultaUrgencias(Connection con,int institucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo, descripcion FROM motivo_consulta_urg WHERE institucion="+institucion+" ORDER BY descripcion";
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getString("codigo"));
				elemento.put("descripcion", rs.getString("descripcion"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMotivosConsultaUrgencias: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	
	/**
	 * Metodo encargado de consultar la instituciones Sirc
	 * pudiendo filtrar por los diferentes criterios.
	 * @author Felipe Perez
	 * @param con
	 * @param criterios
	 * ------------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------------------
	 * -- institucion --> Requerido
	 * -- codigo --> Opcional
	 * -- descripcion --> Opcional
	 * -- nivelservicio --> Opcional
	 * -- tipored --> Opcional
	 * -- tipoinstreferencia --> Opcional
	 * -- tipoinstambulancia --> Opcional
	 * -- activo --> Opcional
	 * -- centroAtencion --> Opcional
	 * @return ArrayListHashMap
	 *-----------------------------------------
	 *KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 *----------------------------------------- 
	 * codigo,descripcion,nivelservicio,tipored,
	 * tipoinstreferencia,tipoinstambulancia,activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection con,HashMap criterios)
	{
		logger.info("\n entre a  obtenerInstitucionesSirc -->"+criterios);
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String cadena = "SELECT " +
					" isi.codigo AS codigo," +
					" isi.descripcion As descripcion," +
					" isi.nivel_servicio As nivelservicio, " +
					" isi.tipo_red As tipored, " +
					" isi.tipo_inst_referencia As tipoinstreferencia, " +
					" isi.tipo_inst_ambulancia As tipoinstambulancia, " +
					" isi.activo As activo " +
				
				" FROM historiaclinica.instituciones_sirc isi " ;
			String where =" WHERE isi.institucion="+criterios.get("institucion");
			String inner=" ";
			if(criterios.containsKey("codigo"))
				where+=" AND isi.codigo ='"+criterios.get("codigo")+"'";
			if(criterios.containsKey("descripcion"))
				where+=" AND isi.descripcion LIKE '%"+criterios.get("descripcion")+"%' ";
			if(criterios.containsKey("nivelservicio"))
				where+=" AND isi.nivel_servicio= "+criterios.get("nivelservicio");
			if(criterios.containsKey("tipored"))
				where+=" AND isi.tipo_red ='"+criterios.get("tipored")+"' ";
			if(criterios.containsKey("tipoinstreferencia"))
				where+=" AND isi.tipo_inst_referencia = '"+criterios.get("tipoinstreferencia")+"'";
			if(criterios.containsKey("tipoinstambulancia"))
				where+=" AND isi.tipo_inst_ambulancia = '"+criterios.get("tipoinstambulancia")+"' ";
			if(criterios.containsKey("activo"))
				where+=" AND isi.activo = '"+criterios.get("activo")+"' ";
			if(criterios.containsKey("centroAtencion"))
			{
				//inner+=" INNER JOIN centro_atencion ca on(ca.codigo_inst_sirc=isi.codigo and ca.cod_institucion=isi.institucion) ";
				//where+=" AND ca.consecutivo="+criterios.get("centroAtencion");
				where+=" AND isi.codigo not in (SELECT codigo_inst_sirc from centro_atencion where consecutivo="+criterios.get("centroAtencion")+")";
			}
			
			cadena+=inner+=where;
			logger.info("\n cadena -->"+cadena);
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(cadena));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion", rs.getObject("descripcion"));
				elemento.put("nivelservicio", rs.getObject("nivelservicio"));
				elemento.put("tipored",rs.getObject("tipored"));
				elemento.put("tipoinstreferencia", rs.getObject("tipoinstreferencia"));
				elemento.put("tipoinstambulancia", rs.getObject("tipoinstambulancia"));
				elemento.put("activo", rs.getObject("activo"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Problema consultando las intituciones Sirc: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return resultados;
	}
	
	/**
	 * Método para consultar el diagnostico de ingreso del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerDescripcionDxIngresoPaciente(Connection con,HashMap campos)
	{
		String diagnostico = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS****************************
			int codigoCuenta = Integer.parseInt(campos.get("codigoCuenta").toString());
			int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
			int tipoBD = Integer.parseInt(campos.get("tipoBD").toString());
			//**************************************************************
			
			String consulta = "SELECT coalesce(getdiagnosticopaciente("+codigoCuenta+","+codigoViaIngreso+"),'') as diagnostico "+(tipoBD==DaoFactory.ORACLE?" FROM dual ":"");
			st = con.createStatement( );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				diagnostico = rs.getString("diagnostico");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDescripcionDxIngresoPaciente: "+e);
		}finally{
			try {
				if(st!=null){
					st.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return diagnostico;
	}
	
	/**
	 * Método para consultar el pérfil de farmacoterapia
	 * El HashMap resultante contiene las siguientes llaves:
	 * 	-- cod_axioma_art_"i"
	 *  -- cod_interfaz_art_"i"
	 *  -- nombre_art_"i"
	 *  -- dosis_"i"
	 *  -- frecuencia_"i"
	 *  -- via_"i"
	 *  -- dia_"i"
	 *  -- mes_"i"
	 *  -- paciente
	 *  -- entidad
	 *  -- nro_ingreso
	 *  -- fecha ingreso
	 *  -- nro_doc
	 *  -- sexo
	 *  -- edad
	 *  -- cama
	 *  -- numRegistros
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static HashMap obtenerPerfilFarmacoterapia(Connection con, int ingreso)
	{
		String consultaSQL="";
		//String fechaIngreso="";
		//String mesIngreso="";
		HashMap resultado = new HashMap();
		resultado.put("numRegistros", "0");
		
		//Consultar Fecha de Ingreso
		/*consultaSQL = "SELECT " +
							"to_char(getfechaingreso(c.id, c.via_ingreso), 'DD-MM-YYYY') AS fecha_ingreso, " +
							"getnombremes(to_char(getfechaingreso(c.id, c.via_ingreso), 'MM')) AS mes_ingreso " +
						"FROM " +
							"cuentas c " +
						"INNER JOIN " +
							"ingresos i ON (i.id = c.id_ingreso) " +
						"WHERE " +
							"c.id_ingreso = "+ingreso;
		logger.info("obtenerFechaIngreso [ "+consultaSQL+" ]");
		
		try
		{
			Statement st = con.createStatement( );
			ResultSetDecorator rs = st.executeQuery(consultaSQL);
			if(rs.next()){
				fechaIngreso = rs.getString("fecha_ingreso");
				mesIngreso = rs.getString("mes_ingreso")+" "+rs.getString("fecha_ingreso").split("-")[2];
			}	
		} catch (SQLException e) {
			logger.error("Error en obtenerPerfilFarmacoterapia (Consulta Fecha de Ingreso): "+e);
		}	*/
		
		//Consulta Perfil Farmacoterapia
		consultaSQL = "SELECT " +
							"getdescarticulosincodigo(a.codigo) AS nombre_art, " +
							"a.codigo AS codigo_art, " +
							"a.codigo_interfaz AS cod_interfaz_art, " +
							"ds.frecuencia||' '||ds.tipo_frecuencia AS frecuencia, " +
							"ds.dosis || coalesce(' '||uxa.unidad_medida,'') AS dosis, " +
							"ds.via AS via, " +
							"getnombremes(to_char(da.fecha, 'MM'))||' '||to_char(da.fecha, 'YYYY') AS mes, " +
							"to_char(da.fecha, 'DD') AS dia, " +
							"da.cantidad AS cantidad " +
							//"diasdelmes(to_char(da.fecha,'MM')::numeric,to_char(da.fecha,'YYYY')) AS num_dias " +
						"FROM " +
							"cuentas c " +
						"INNER JOIN " +
							"solicitudes s ON (s.cuenta = c.id) " +
						"INNER JOIN " +
							"detalle_solicitudes ds ON (ds.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"admin_medicamentos am ON (am.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"detalle_admin da ON (da.administracion = am.codigo AND da.articulo=ds.articulo) " +
						"INNER JOIN " +
							"articulo a ON (a.codigo = da.articulo) " +
						"LEFT OUTER JOIN " +
							"unidosis_x_articulo uxa on(uxa.codigo = ds.unidosis_articulo) " +
						"WHERE " +
							"c.id_ingreso = " + ingreso +
							"AND s.tipo= " + ConstantesBD.codigoTipoSolicitudMedicamentos + " " +
							"AND estado_historia_clinica <> " + ConstantesBD.codigoEstadoHCAnulada + " " +
							"AND da.cantidad <> 0 " +
						/*"GROUP BY " +
							"a.codigo, " +
							"a.codigo_interfaz, " +
							"ds.frecuencia, " +
							"ds.tipo_frecuencia, " +
							"ds.frecuencia, " +
							"ds.tipo_frecuencia, " +
							"ds.dosis, " +
							"ds.via, " +
							"da.fecha, " +
							"uxa.unidad_medida " +*/
						"ORDER BY " +
							"da.fecha, " +
							"nombre_art, " +
							"dosis, " +
							"frecuencia, " +
							"via ";
		
		logger.info("obtenerPerfilFarmacoterapia [ "+consultaSQL+" ]");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSQL));
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			//String diasMesActual="";
			String mesActual="";
			String meses="";
			String mesAux="";
			int numMeses=0;
			for(int i=0; i<Utilidades.convertirAEntero(resultado.get("numRegistros").toString()); i++){
				if(mesAux.equals("")){
					mesActual = resultado.get("mes_"+i).toString();
					//diasMesActual = resultado.get("num_dias_"+i).toString();
				}
				if(!mesAux.equals(resultado.get("mes_"+i).toString())){
					meses = meses+resultado.get("mes_"+i)+ConstantesBD.separadorSplit;
					numMeses++;
				}
				mesAux = resultado.get("mes_"+i).toString();
			}
			resultado.put("meses", meses);
			resultado.put("num_meses", numMeses);
			resultado.put("mes_actual", mesActual);
			//resultado.put("fecha_ingreso", fechaIngreso);
			//resultado.put("num_dias_mes_actual", diasMesActual);
			//resultado.put("mes_ingreso", mesIngreso);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPerfilFarmacoterapia: "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		return resultado;
	}
	
	/**
	 * Método para obtener los ultimos diagnósticos de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<Diagnostico> obtenerUltimosDiagnosticoIngreso(Connection con,HashMap campos)
	{
		ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;

		try
		{
			//*************************SE TOMAN PARÁMETROS*************************************+
			int codigoUltimaEvolucion = Utilidades.convertirAEntero(campos.get("codigoUltimaEvolucion").toString());
			int codigoUltimaValoracion = Utilidades.convertirAEntero(campos.get("codigoUltimaValoracion").toString());
			boolean complicacion = UtilidadTexto.getBoolean(campos.get("complicacion").toString());
			//***********************************************************************************
			
			if(codigoUltimaEvolucion>0)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosEvolucionStr));
				pst.setInt(1,codigoUltimaEvolucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					Diagnostico diagnostico = new Diagnostico();
					diagnostico.setAcronimo(rs.getString("acronimo_diagnostico"));
					diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
					diagnostico.setNombre(rs.getString("nombre_diagnostico"));
					diagnostico.setPrincipal(UtilidadTexto.getBoolean(rs.getString("principal")));
					diagnosticos.add(diagnostico);
				}
				
				if(complicacion)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticoComplicacionEvolucionStr));
					pst.setInt(1,codigoUltimaEvolucion);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						Diagnostico diagnostico = new Diagnostico();
						diagnostico.setAcronimo(rs.getString("acronimo_diagnostico"));
						diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
						diagnostico.setNombre(rs.getString("nombre_diagnostico"));
						diagnostico.setComplicacion(true);
						diagnosticos.add(diagnostico);
					}
				}
			}
			
			if(diagnosticos.size()==0&&codigoUltimaValoracion>0)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosValoracionStr));
				pst.setInt(1,codigoUltimaValoracion);
				 rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					Diagnostico diagnostico = new Diagnostico();
					diagnostico.setAcronimo(rs.getString("acronimo_diagnostico"));
					diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
					diagnostico.setNombre(rs.getString("nombre_diagnostico"));
					diagnostico.setPrincipal(UtilidadTexto.getBoolean(rs.getString("principal")));
					diagnosticos.add(diagnostico);
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimosDiagnosticoIngreso: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return diagnosticos;
	}

	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @return
	 */
	public static HashMap obtenerInfoInstitucionXIngreso(Connection con, String numeroIngreso) 
	{
		HashMap infoInstitucion = new HashMap();
		String sql = 	"SELECT " +
							"i.nit AS nitInstitucion, " +
							"i.razon_social AS razonSocialInstitucion, " +
							"i.direccion AS direccionInstitucion, " +
							"i.telefono AS telefonoInstitucion, " +
							"i.logo AS logoInstitucion, " +
							"ei.nit AS nitEmpresa, " +
							"ei.razon_social AS razonSocialEmpresa, " +
							"ei.direccion AS direccionEmpresa, " +
							"ei.telefono AS telefonoEmpresa, " +
							"ei.logo AS logoEmpresa, " +
							"ca.direccion AS direccionCentroAtencion " +
						"FROM " +
							"manejopaciente.ingresos ing " +
						"INNER JOIN  " +
							"manejopaciente.sub_cuentas s ON (s.ingreso=ing.id AND nro_prioridad=1) " +
						"INNER JOIN " +
							"facturacion.convenios c ON (c.codigo=s.convenio) " +
						"INNER JOIN " +
							"administracion.instituciones i ON (i.codigo=c.institucion) " +
						"LEFT OUTER JOIN " +
							"facturacion.empresas_institucion ei ON (ei.codigo=c.empresa_institucion) " +
						"LEFT OUTER JOIN  " +
							"administracion.centro_atencion ca ON (ca.consecutivo=ing.centro_atencion) " +
						"WHERE " +
							"ing.consecutivo= '" + numeroIngreso + "'";
		
		logger.info("\n\n\n SQL obtenerInfoInstitucionXIngreso / "+sql);
		PreparedStatementDecorator pst= null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(sql));
			infoInstitucion = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		} catch(SQLException e) {
			logger.error("Error en obtenerInfoInstitucionXIngreso: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			
		}
		Utilidades.imprimirMapa(infoInstitucion);
		return infoInstitucion;
	}
	
	/**
	 * Método para consultar los datos de la especialidad
	 * @param con
	 * @param especialidad
	 * @return
	 */
	public static void consultarEspecialidad(Connection con,DtoEspecialidad especialidad)
	{
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			String consultar = "SELECT " +
				"coalesce(cc.unidad_funcional,'') as unidad_funcional," +
				"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion," +
				"coalesce(cc.codigo,"+ConstantesBD.codigoNuncaValido+") as codigo," +
				"coalesce(cc.identificador,'') as identificador, " +
				"coalesce(cc.codigo_interfaz,'') as codigo_interfaz, " +
				"coalesce(cc.nombre,'') as nombre," +
				"e.codigo as codigo_especialidad," +
				"e.consecutivo as consecutivo_especialidad," +
				"e.nombre as nombre_especialidad " +
				"from especialidades e " +
				"left outer join centros_costo cc on (cc.codigo = e.centro_costo_honorario) " +
				"where e.codigo = ?";
			pst = new PreparedStatementDecorator(con.prepareStatement(consultar));
			pst.setInt(1,especialidad.getCodigo());
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				especialidad.getCentroCostoHonorarios().setAcronimoUnidadFuncional(rs.getString("unidad_funcional"));
				especialidad.getCentroCostoHonorarios().setInstitucionUnidadFuncional(rs.getString("institucion"));
				especialidad.getCentroCostoHonorarios().setCodigo(rs.getString("codigo"));
				especialidad.getCentroCostoHonorarios().setIdentificador(rs.getString("identificador"));
				especialidad.getCentroCostoHonorarios().setCodigoInterfaz(rs.getString("codigo_interfaz"));
				especialidad.getCentroCostoHonorarios().setNombre(rs.getString("nombre"));
				especialidad.setCodigo(rs.getInt("codigo_especialidad"));
				especialidad.setConsecutivo(rs.getString("consecutivo_especialidad"));
				especialidad.setNombre(rs.getString("nombre_especialidad"));
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error tratando de cargar os datros dela especiaildad: "+e);
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}

	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneHisotircosOtrosSistemas(int codigoPersona) 
	{
		boolean tieneHistoricos = false;
		PreparedStatementDecorator pt= null;
		ResultSetDecorator rs = null;
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			String consulta = "SELECT count(1) AS historicos FROM historiaclinica.historicos_hc where codigo_paciente="+codigoPersona;
			pt =new PreparedStatementDecorator(con.prepareStatement(consulta ));
			rs =  new ResultSetDecorator(pt.executeQuery());
			
			if(rs.next())
				if(rs.getInt("historicos")>0)
					tieneHistoricos = true;

			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en existeContrarreferencia: "+e);
		}finally{
			try {
				if(pt!=null){
					pt.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return tieneHistoricos;
	}
	
	public static int ultimaEspecialidadEvolucionValoracionIngreso(Connection con,int solicitud)
	{
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			int especialidad = 0;
			String consulta = ultimaEspecialidadEvolucionValoracionIngresoStr;
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,solicitud);
			
			 rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				especialidad = rs.getInt("especialidad");
			logger.info("especialidad::::"+especialidad);
			return especialidad;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarUltimaEvolucionIngreso: "+e);
			return 0;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	
	/**
	 * @param con
	 * @return estado de funcionalidad 
	 * @throws Exception
	 */
	public static Integer consultarEstadoFuncionalidadAntecedentes(Connection con) throws Exception{
		Integer estado= new Integer(0);
		PreparedStatement pst =  null;
		ResultSet res =  null;
		try
		{
			String consulta=" select psc.ESTADO  from ADMINISTRACION.PARAM_SECCION_CLIENTE psc  where psc.NOMBRE_SECCION=?";
			 pst = con.prepareStatement(consulta);
			pst.setString(1,ConstantesBD.seccionAntecedentesPorCLientes );
			res = pst.executeQuery();
			if(res.next()){
				estado= res.getInt("ESTADO");
			}

			pst.close();
			res.close();
		}
		catch(SQLException e)
		{
			logger.error("error consultando estado de funcionalidad de antecedentes "+e.getMessage());
			throw new Exception("error consultando estado de funcionalidad de antecedentes "+e.getMessage());

		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(res!=null){
					res.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return estado;
	}


	/**
	 * @param con
	 * @return estado de funcionalidad 
	 * @throws Exception
	 */
	public static Integer consultarEstadoFuncionalidadHistoriaClincia(Connection con) throws Exception{
		Integer estado= new Integer(0);
		PreparedStatement pst =  null;
		ResultSet res =  null;
		try
		{
			String consulta=" select psc.ESTADO  from ADMINISTRACION.PARAM_SECCION_CLIENTE psc  where psc.NOMBRE_SECCION=?";
			 pst = con.prepareStatement(consulta);
			pst.setString(1,ConstantesBD.seccionHistoriaClinicaPorCLientes );
			 res = pst.executeQuery();
			if(res.next()){
				estado= res.getInt("ESTADO");
			}

			pst.close();
			res.close();

		}
		catch(SQLException e)
		{
			logger.error("error consultando estado de funcionalidad de historia clinica "+e.getMessage());
			throw new Exception("error consultando estado de funcionalidad de historia clinica "+e.getMessage());

		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(res!=null){
					res.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return estado;
	}

	/**
	 * @param codigoTipoMonitoreo
	 * @param con
	 * @return El nombre del tipo de monitoreo 
	 * @throws Exception
	 */
	public static String consultarTipoMonitoreoXCodigo(Integer codigoTipoMonitoreo,Connection con)throws Exception{
		String nombreTipoMonitoreo="";
		PreparedStatement pst =  null;
		ResultSet res =  null;
		try
		{
			//consulta que obtiene el nombre del monitoreo a partir del codigo  
			String consulta=" select tm.nombre NOMBRE_TIPO_MONITOREO from  tipo_monitoreo tm  where tm.codigo=? ";
			 pst = con.prepareStatement(consulta);

			//set del parametro que contiene el codigo del tipo de monitoreo
			pst.setInt(1, codigoTipoMonitoreo);

			// ejecucion del query
			 res = pst.executeQuery();

			//solo debe traer un solo registro 
			if(res.next()){
				nombreTipoMonitoreo= res.getString("NOMBRE_TIPO_MONITOREO");
			}

			pst.close();
			res.close();
		}//control de errores 
		catch(SQLException e)
		{
			logger.error("error consultando el tipo de monitoreo por codigo en impresion HC "+e.getMessage());
			throw new Exception("error consultando el tipo de monitoreo por codigo en impresion HC "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(res!=null){
					res.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		//retorno esperado
		return nombreTipoMonitoreo;

	}

	
	/**
	 * @param codigoCentroCostos
	 * @param con
	 * @return El nombre del centro de costos 
	 * @throws Exception
	 * mt5887 para que presente el centro de costos del monitoreo
	 */
	
	public static String consultarCentroCostosMonitoreoXCodigo(Integer codigoCentroCostos,Connection con)throws Exception{
		String nombreCentroCostosMonitoreo="";
		PreparedStatement pst =  null;
		ResultSet res =  null;
		try
		{
			//consulta que obtiene el nombre del monitoreo a partir del codigo  
			String consulta=" select cc.nombre Nombre_Centro_Costos from centros_costo cc where cc.codigo=? ";
			 pst = con.prepareStatement(consulta);

			//set del parametro que contiene el codigo del tipo de monitoreo
			pst.setInt(1, codigoCentroCostos);

			// ejecucion del query
			 res = pst.executeQuery();

			//solo debe traer un solo registro 
			if(res.next()){
				nombreCentroCostosMonitoreo= res.getString("Nombre_Centro_Costos");
			}

		
		}//control de errores 
		catch(SQLException e)
		{
			logger.error("error consultando el centro de costos de monitoreo por codigo en impresion HC "+e.getMessage());
			throw new Exception("error consultando el centro de costos de monitoreo por codigo en impresion HC "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(res!=null){
					res.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		//retorno esperado
		return nombreCentroCostosMonitoreo;

	}

	/**
	 * @param codigoEquivalente
	 * @param codigoAdm TODO
	 * @param con
	 * @param ingreso
	 * @return medicamento original
	 * @throws Exception
	 */
	public static DtoMedicamentosOriginales consultarMedicamentosOriginales(Integer codigoEquivalente, Integer codigoAdm,Connection con, Integer ingreso )throws Exception{
		Integer res = new Integer(0);
		DtoMedicamentosOriginales medicamentoOriginal = new DtoMedicamentosOriginales();
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try
		{	
			String consulta1 = " SELECT da.art_principal AS artppal " +
					" FROM ENFERMERIA.DETALLE_ADMIN da " +
					" WHERE da.art_principal IS NOT NULL " +
					" AND da.articulo = ? " +
					" AND da.administracion = ? ";

			String consulta2 = " SELECT  " +
					"	a.codigo                                 	  AS articulo," +
					" 	a.descripcion                                 AS medicamento, " +
					" 	a.concentracion                               AS concentracion, " +
					" 	getnomformafarmaceutica(a.forma_farmaceutica) AS forma_farmaceutica, " +
					" 	getnomunidadmedida(a.unidad_medida)           AS unidad_medida " +
					" FROM INVENTARIOS.ARTICULO a  " +
					" WHERE a.codigo = ? ";
				
			pst = con.prepareStatement(consulta1);
			pst.setInt(1, codigoEquivalente);
			pst.setInt(2, codigoAdm);
			
			rs = pst.executeQuery();
			if(rs.next()){
				res = rs.getInt("artppal");
			}

			if(res > new Integer(0)){
				pst = con.prepareStatement(consulta2);
				pst.setInt(1, res);
				rs = pst.executeQuery();
	
				if (rs.next()) {
					medicamentoOriginal.setCodigo(rs.getInt("articulo"));
					medicamentoOriginal.setMedicamento (rs.getString("medicamento"));
					medicamentoOriginal.setFormaFarmaceutica(rs.getString("forma_farmaceutica"));
					medicamentoOriginal.setConcentracion(rs.getString("concentracion"));
					medicamentoOriginal.setUnidadMedida(rs.getString("unidad_medida"));
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("error consultando el tipo de monitoreo por codigo en impresion HC "+e.getMessage());
			throw new Exception("error consultando el tipo de monitoreo por codigo en impresion HC "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return medicamentoOriginal;
	}


	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return Unidad de medida 
	 * @throws Exception
	 */
	public static String obtenerUnidadMedidaMedicamentoPrincipal(Connection con, Integer numeroSolicitud, Integer codigoArt) throws Exception{

		String unidadMedida="";
		PreparedStatement pste =  null;
		ResultSet rse =  null;
		ResultSet rs =  null;
		ResultSet rs2 =  null;
		PreparedStatement pst2 = null;
		try {

			//CONSULTA DE LA UNIDAD DE MEDIDA 
			String consulta=" select ua.unidad_medida  as unidad_medida " +
			"from  detalle_solicitudes ds inner   join unidosis_x_articulo ua " +
			"on(ds.unidosis_articulo=ua.codigo) and ds.numero_solicitud = ? and  ds.articulo=?";

			//CREACION DE PREPAREDSTATEMENT
			PreparedStatement pst = con.prepareStatement(consulta);
			pst.setInt(1, numeroSolicitud);
			pst.setInt(2, codigoArt);
			 rs = pst.executeQuery();

			//SE OBTIENE EL VALOR 
			if(rs.next()){
				unidadMedida=rs.getString("unidad_medida");
			}

			pst.close();
			rs.close();
			
			
			if(unidadMedida.equals(""))
			{boolean error=false;
			int art=0;
				try {
			String equivalente="   select ds.articulo_principal as articu from detalle_solicitudes ds where ds.numero_solicitud = ? and  ds.articulo=?";
			pste = con.prepareStatement(equivalente);
			pste.setInt(1, numeroSolicitud);
			pste.setInt(2, codigoArt);
			 rse = pste.executeQuery();
			if(rse.next()){
			art= rse.getInt("articu");
			}
			pste.close();
			rse.close();
				}
				catch(Exception e){
					error=true;
				}
				
			if (!error)
			{
				 pst2 = con.prepareStatement(consulta);
				pst2.setInt(1, numeroSolicitud);
				pst2.setInt(2, art);
				rs2 = pst2.executeQuery();
				if(rs2.next()){
					unidadMedida=rs2.getString("unidad_medida");
				}
				pst2.close();
				rs2.close();
			}
				
			}

		}//CONTROL DE ERRORES  
		catch (SQLException e) {
			logger.error("error consultando unidad de medida ",e);
			throw new Exception("error consultando unidad de medida "+e.getMessage());
		}finally{
	
			try {
				if(pste!=null){
					pste.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(pst2!=null){
					pst2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rse!=null){
					rse.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs2!=null){
					rs2.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		//RETORNO ESPERADO
		return unidadMedida;

	}


	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return servicios asociados 
	 * @throws Exception
	 */
	public static ArrayList<String> serviciosHojaQuirurgica(Connection con, Integer numeroSolicitud)throws Exception{

		ArrayList<String> listaServicios= new ArrayList<String>();
		String consulta=" SELECT "+
		" (s.especialidad "+
		" ||'-' "+
		" ||s.codigo "+
		" ||' ' "+
		" ||getnombreservicio(scxs.servicio,0)) AS servicio "+
		" FROM sol_cirugia_por_servicio scxs "+
		" INNER JOIN servicios s "+
		" ON (scxs.servicio          =s.codigo) "+
		" WHERE scxs.numero_solicitud=? "+
		" ORDER BY scxs.consecutivo ASC "; 
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {


			 pst = con.prepareStatement(consulta);
			pst.setInt(1, numeroSolicitud);
			 rs = pst.executeQuery();

			while(rs.next()){
				listaServicios.add(rs.getString("servicio"));
			}

			pst.close();
			rs.close();
		}//CONTROL DE ERRORES  
		catch (SQLException e) {
			logger.error("error consultando servicios  ",e);
			throw new Exception("error consultando servicios "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}



		return listaServicios;
	}

	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public static String obtenerTipoPaciente(Connection con,String numeroCuenta) throws Exception{
		String tipoPaciente="";
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {

			String cosnulta = " select tp.nombre from  tipos_paciente tp join cuentas cu "+
			" on(tp.acronimo = cu.TIPO_PACIENTE) "+
			" where  "+
			" cu.id = ?";

			pst = con.prepareStatement(cosnulta);
			pst.setInt(1, Integer.valueOf(numeroCuenta));
			rs = pst.executeQuery();
			while(rs.next()){
				tipoPaciente=rs.getString("nombre");
			}

	

		} catch (SQLException e) {
			logger.error("error consultando servicios  ",e);
			throw new Exception("error consultando tipopaciente "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return tipoPaciente;


	}


	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public static String especialidadUrgencia(Connection con,String numeroCuenta) throws Exception{
		int interconsultas=0;
		String res="";
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {

			String cosnulta = " select count(1)cant  from solicitudes soli "+
			" where soli.tipo= 4 "+
			" and soli.cuenta=?  ";

			pst = con.prepareStatement(cosnulta);
			pst.setInt(1, Integer.valueOf(numeroCuenta));
			rs = pst.executeQuery();
			while(rs.next()){
				interconsultas=rs.getInt("cant");
			}
			if(interconsultas<=0){
				res= "Urgencias";
			}else{


				String consulta2=" select cc.nombre  from solicitudes soli  join CENTROS_COSTO cc "+
				" on (soli.centro_costo_solicitado= cc.codigo) join cuentas cu  "+
				" on (cu.id=soli.cuenta)  "+
				" where soli.tipo= 4 and cu.via_ingreso=3 "+
				" and soli.cuenta=? "+
				" order by soli.fecha_grabacion desc,soli.hora_grabacion desc ";


				PreparedStatement pst2 = con.prepareStatement(consulta2);
				pst2.setInt(1, Integer.valueOf(numeroCuenta));
				ResultSet rs2 = pst2.executeQuery();
				if(rs2.next()){
					res=rs2.getString("nombre");
				}
			}


		
		} catch (SQLException e) {
			logger.error("error consultando servicios  ",e);
			throw new Exception("error consultando tipopaciente "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return res;


	}

	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public static String obtenerTipoPacienteHosp(Connection con,String numeroCuenta) throws Exception{
		String tipoPaciente="";
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {

			String cosnulta = " select cc.nombre  from solicitudes soli  join CENTROS_COSTO cc "+
			" on (soli.centro_costo_solicitado= cc.codigo) join cuentas cu  "+
			" on (cu.id=soli.cuenta)  "+
			" where cu.via_ingreso= 1 "+
			" and soli.cuenta=? "+
			" order by soli.fecha_grabacion desc,soli.hora_grabacion desc";

			pst = con.prepareStatement(cosnulta);
			pst.setInt(1, Integer.valueOf(numeroCuenta));
			rs = pst.executeQuery();
			if(rs.next()){
				tipoPaciente=rs.getString("nombre");
			}

		

		} catch (SQLException e) {
			logger.error("error consultando servicios  ",e);
			throw new Exception("error consultando tipopaciente "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return tipoPaciente;


	}

	/**
	 * @param con
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public static String obtenerTipoPacienteConsultaExterna(Connection con,String numeroCuenta) throws Exception{
		String tipoPaciente="";
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {

			String cosnulta = " select uc.descripcion nombre from solicitudes soli join servicios_cita servc "+
			" on(servc.NUMERO_SOLICITUD = soli.NUMERO_SOLICITUD) join cita  ci "+
			" on (servc.codigo_cita = ci.codigo) join unidades_consulta uc  "+
			" on (ci.UNIDAD_CONSULTA = uc.codigo) join cuentas cu  "+
			" on (soli.cuenta=cu.id) "+
			" where soli.cuenta=?  "+
			" and cu.via_ingreso=4";

			pst = con.prepareStatement(cosnulta);
			pst.setInt(1, Integer.valueOf(numeroCuenta));
			rs = pst.executeQuery();
			if(rs.next()){
				tipoPaciente=rs.getString("nombre");
			}

			pst.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("error consultando servicios  ",e);
			throw new Exception("error consultando tipopaciente "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return tipoPaciente;


	}


	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws Exception
	 */
	public static String obtenerFechaMuerte(Connection con,String codigoPaciente) throws Exception{

		String consulta = " select  pa.fecha_muerte||' '||pa.hora_muerte fecha_muerte from  personas p join pacientes pa "+
		" on (p.codigo=pa.codigo_paciente) "+
		" where p.codigo=?";
		String fechaMuerte="";
		PreparedStatement pst =  null;
		ResultSet rs =  null;
		try {
			pst = con.prepareStatement(consulta);
			pst.setInt(1, Integer.valueOf(codigoPaciente));
			rs = pst.executeQuery();

			if(rs.next()){
				fechaMuerte=rs.getString("fecha_muerte");
			}

			pst.close();
			rs.close();

		} catch (SQLException e) {
			logger.error("error consultando fecha muerte  ",e);
			throw new Exception("error consultando fecha muerte "+e.getMessage());
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}

		return fechaMuerte;
	}

	
}