package com.princetonsa.dao.sqlbase.laboratorios;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBaseUtilidadLaboratoriosDao
{
	public static Logger logger=Logger.getLogger(SqlBaseUtilidadLaboratoriosDao.class);
	
	
	/**
	 * Cadena que consulta la Interfaz de Laboratorio que no se ha leido
	 */
	private static final String consultarInterfazLaboratoriosSELECT_Str =  
		 "SELECT i.consecutivo   as consecutivo          , " +
		 "i.num_solicitud        as num_solicitud        , " +
		 "i.num_documento        as num_documento        , " +
		 "i.estado               as estado               , " +
		 "i.fecha                as fecha                , " +
		 "i.hora                 as hora                 , " +
		 "CASE WHEN i.respuesta IS NULL THEN '' ELSE i.respuesta END as respuesta            , " +
		 "CASE WHEN i.path_pdf IS NULL THEN '' ELSE i.path_pdf END as path_pdf             , " +
		 "CASE WHEN i.profesional_responde IS NULL THEN 0 ELSE i.profesional_responde END as profesional_responde , " +
		 "i.reproceso            as reproceso            , " +
		 "CASE WHEN i.motivo_reproceso IS NULL THEN '' ELSE i.motivo_reproceso END as motivo_reproceso     , " +
		 "i.leido                as leido                , " +
		 "i.usuario              as usuario              ,  " +
		 "CASE WHEN u.login IS NULL THEN '' ELSE u.login END as login_usuario           " +
		 "FROM interfaz_laboratorio i                     " +
		 "LEFT OUTER JOIN usuarios u ON(u.codigo_persona=i.usuario) " +
		 "WHERE i.leido = " + ValoresPorDefecto.getValorFalseParaConsultas() ; //'false'                          ";
		
	/**
	 * Cadena que actualiza los registros no leidos en 'true'
	 */
	private static final String actualizarLeidoInterfazLaboratorios_Str =
		"UPDATE interfaz_laboratorio " +
		"SET    leido = " + ValoresPorDefecto.getValorTrueParaConsultas() + 
		" WHERE  consecutivo = ? " ;
	
	/**
	 * Método que consulta informacion adicional de la solicitud
	 */
	private static final String getInformacionAdicionalSolicitudStr = "SELECT "+ 
		"s.centro_costo_solicitado AS centro_costo, "+
		"ca.consecutivo as centro_atencion, "+
		"s.cuenta AS cuenta, "+
		"sp.codigo_servicio_solicitado AS codigo_servicio, "+
		"cc.institucion as institucion, " +
		"c.via_ingreso as codigo_via_ingreso "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) "+
		"INNER JOIN centro_atencion ca ON(ca.consecutivo = cc.centro_atencion) "+
		"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) " +
		"INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
		"WHERE s.numero_solicitud = ?";
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario
	 * @param estadoHistoriaClinica 
	 * @return
	 */
	public static boolean pasarSolicitudATomaMuestras(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario,int estadoHistoriaClinica)
	{
		String cadenaActualizarEstadoSol="UPDATE solicitudes SET estado_historia_clinica ='"+estadoHistoriaClinica+"' where numero_solicitud='"+numeroSolicitud+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizarEstadoSol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
			{
				ps.close();
				String cadena="UPDATE sol_procedimientos set fecha_toma_muestra=? , hora_toma_muestra = ? , usuario_toma_muestra=? where numero_solicitud='"+numeroSolicitud+"'";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCambio)));
				ps.setString(2, UtilidadFecha.convertirHoraACincoCaracteres(horaCambio));
				ps.setString(3, loginUsuario);
				int resp = ps.executeUpdate();
				ps.close();
				return resp>0;
			}
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERRO EN pasarSolicitudATomaMuestras, cuando se intenta actualizar el estado de la solicitud");
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaCambio
	 * @param horaCambio
	 * @param loginUsuario 
	 * @return
	 */
	public static boolean pasarSolicitudAEnProceso(Connection con, String numeroSolicitud, String fechaCambio, String horaCambio, String loginUsuario)
	{
		String cadenaActualizarEstadoSol="UPDATE solicitudes SET estado_historia_clinica ='"+ConstantesBD.codigoEstadoHCEnProceso+"' where numero_solicitud='"+numeroSolicitud+"'";
		try
		{
			logger.info("Cadena de modificación: "+cadenaActualizarEstadoSol);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizarEstadoSol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
			{
				ps.close();
				String cadena="UPDATE sol_procedimientos set fecha_proceso=? , hora_proceso = ? , usuario_proceso=? where numero_solicitud='"+numeroSolicitud+"'";
				logger.info("cadena de Consulta: "+cadena);
				logger.info("Prametros: fechacambio->"+UtilidadFecha.conversionFormatoFechaABD(fechaCambio)+" horaCambio->"+UtilidadFecha.convertirHoraACincoCaracteres(horaCambio)+" loginUsuario->"+loginUsuario);
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCambio)));
				ps.setString(2, UtilidadFecha.convertirHoraACincoCaracteres(horaCambio));
				ps.setString(3, loginUsuario);
				int resp = ps.executeUpdate();
				ps.close();
				return resp>0;
			}
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERRO EN pasarSolicitudAEnProceso, cuando se intenta actualizar el estado de la solicitud");
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Método implementado para consultar los registros de la tabla de interfaz laboratorios que no se hayan leido
	 * @param con
	 * @return
	 */
	public static HashMap consultarInterfazLaboratorios(Connection con )
	{
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultarInterfazLaboratoriosSELECT_Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		} catch (SQLException e) {
			logger.error("Error en consultarInterfazLaboratorios de SqlBaseInterfazLaboratoriosDao: "+e);
			return null;
		}
	}

	/**
	 * Método implementado para actualizar a leidos los registros que no se hayan leido
	 * de la tabla interfaz_laboratorio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static int actualizarLeidoInterfazLaboratorios( Connection con, String consecutivo)
	{
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarLeidoInterfazLaboratorios_Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			return pst.executeUpdate();
		} catch (SQLException e) {
			logger.error("error en actualizarLeidoInterfazLaboratorios de SqlBaseInterfazLaboratoriosDao: "+e);
			return 0;			
		}
		
		
	}
	
	/**
	 * Método que inserta una respuesta de procedimientos
	 * @param con
	 * @param campos
	 * @param finalizar
	 * @return
	 */
	public static boolean insertarRespuestaProcedimientos(Connection con,HashMap campos,boolean finalizar)
	{
		try
		{
			UsuarioBasico medico = new UsuarioBasico();
			medico.cargarUsuarioBasico(con,Utilidades.convertirAEntero(campos.get("codigoMedico").toString()));
			campos.put("resultados",campos.get("resultados")+"\n"+UtilidadTexto.agregarTextoAObservacion(null, null, medico, false));
			
			int resp0 = 0,resp1=0,resp2 = 0,resp3=0;
			
			//********************SE INSERTA LA RESPUESTA DEL PROCEDIMIENTO***********************************
			String consulta = "INSERT INTO " +
				"res_sol_proc " +
				"(codigo," +
				"numero_solicitud," +
				"fecha_grabacion," +
				"hora_grabacion," +
				"fecha_ejecucion," +
				"hora_ejecucion," +
				"resultados," +
				"tipo_recargo," +
				"observaciones," +
				"comentario_historia_clinica," +
				"codigo_medico_responde," +
				"usuario_registra_respuesta" +
				") " +
				"VALUES("+campos.get("secuencia")+",?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ,?,?,?,?,?,?,?,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaEjecucion").toString())+""));
			pst.setString(3,UtilidadFecha.convertirHoraACincoCaracteres(campos.get("horaEjecucion").toString()));
			pst.setString(4,campos.get("resultados")+"");
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("tipoRecargo")+""));
			pst.setString(6,campos.get("observaciones")+"");
			pst.setString(7,campos.get("comentario")+"");
						
			pst.setInt(8,medico.getCodigoPersona());
			
			if(campos.containsKey("usuarioRegistraRespuesta") 
					&& !campos.get("usuarioRegistraRespuesta").toString().equals(""))
				pst.setString(9,campos.get("usuarioRegistraRespuesta").toString());
			else
				pst.setNull(9,Types.VARCHAR);
			
			resp0 = pst.executeUpdate();
			
			//*******************************************************************************************
			
			//*************SE FINALIZA RESPUESTA MULTIPLE (si aplica)**********************************
			if(resp0>0)
			{
				if(finalizar)
				{
					consulta = "UPDATE sol_procedimientos SET finalizada_respuesta = '"+ConstantesBD.acronimoSi+"' WHERE numero_solicitud = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
					
					resp1 = pst.executeUpdate();
				}
				else
					resp1 = 1;
			}
			
			//********SE ACTUALIZA LA SOLICITUD ***********************************************
			if(resp1>0)
			{
				
				consulta = "UPDATE solicitudes SET " +
										"estado_historia_clinica = "+ConstantesBD.codigoEstadoHCRespondida+", " +
										"codigo_medico_responde=?, " +
										"datos_medico_responde=? " +
										"WHERE numero_solicitud = ?";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoMedico")+""));
				pst.setString(2,UtilidadTexto.agregarTextoAObservacion(null, null, medico, false));
				pst.setInt(3,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
				
				resp2 = pst.executeUpdate();
			}
			//******************************************************************************************
			
			//*******SE INSERTA EL ARCHIVO PDF**********************************************************
			//se lee path PDF
			String pathPDF = campos.get("pathPDF").toString();
			if(resp2>0&&!pathPDF.equals(""))
			{
				
				String[] vector = pathPDF.split("/");
				String nombreOriginal = "";
				if(vector.length>0)
					nombreOriginal = vector[vector.length-1];
				else
					nombreOriginal = pathPDF;
				logger.info("pathPDF=> "+pathPDF);
				logger.info("nombreOriginal=> "+nombreOriginal);
				
				String codigoRespuesta = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_res_sol_proc") + "";
				
				consulta ="INSERT INTO doc_adj_solicitud (" +
								"numero_solicitud, " +
								"nombre_archivo, " +
								"nombre_original, " +
								"es_solicitud, " +
								"codigo_medico, " +
								"es_codigo_resp_sol) VALUES ( ? , ? , ? , ?, ?, ?)";
				
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
				pst.setString(2,"laboratorios"+System.getProperty("file.separator")+pathPDF);
				pst.setString(3,nombreOriginal);
				pst.setBoolean(4, false);
				pst.setInt(5,Utilidades.convertirAEntero(campos.get("codigoMedico")+""));
				pst.setObject(6,codigoRespuesta);
				resp3 = pst.executeUpdate();
			}
			//******************************************************************************************
			
			if(resp0>0&&resp1>0&&resp2>0&&resp3>0)
				return true;
			else
				return false;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarRespuestaProcedimientos de SQlBaseUtilidadLaboratoriosDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que valida el tiempo de reproceso
	 * Si es True es válido
	 * Si es Falso se superó el tiempo de reproceso
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaReproceso
	 * @param horaReproceso
	 * @return
	 */
	public static boolean validarTiempoReproceso(Connection con,String numeroSolicitud,String fechaReproceso,String horaReproceso)
	{
		try
		{
			
			boolean exito = false;
			//**********SE CONSULTA INFORMACION NECESARIA***************************************************
			String institucion = "", viaIngreso = "", fechaSolicitud = "", horaSolicitud = "",horasReproceso = "";
			String consulta = "SELECT "+ 
				"cc.institucion AS institucion, "+
				"c.via_ingreso AS via_ingreso, "+
				"to_char(s.fecha_solicitud,'DD/MM/YYYY') AS fecha_solicitud, "+
				"s.hora_solicitud AS hora_solicitud "+  
				"FROM centros_costo cc "+ 
				"INNER JOIN cuentas c ON(c.area=cc.codigo) "+  
				"INNER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
				"WHERE s.numero_solicitud = ?"; 
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				exito = true;
				institucion = rs.getString("institucion");
				viaIngreso = rs.getString("via_ingreso");
				fechaSolicitud = rs.getString("fecha_solicitud");
				horaSolicitud = UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("hora_solicitud"));
			}
			logger.info("institucion=>"+institucion+", viaIngreso=>"+viaIngreso+", fechaSolicitud=>"+fechaSolicitud+", horaSolicitud=> "+horaSolicitud);
			//**************************************************************************************************
			//*********SE CONSULTA HORAS REPROCESO****************************************************************
			consulta = "SELECT hora FROM horas_reproceso WHERE via_ingreso = ? and institucion = ?";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(viaIngreso));
			pst.setInt(2,Utilidades.convertirAEntero(institucion));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				horasReproceso = rs.getString("hora");
				exito = true;
			}
			else
				exito = false;
			
			logger.info("horasReproceso=>"+horasReproceso);
			//****************************************************************************************************
			//***********SE VERIFICA RANGO********************************************************************
			if(exito)
			{
				exito = validarFormatoHoraReproceso(horasReproceso);
				logger.info("Validar el formato de reproceso=> "+exito);
				if(exito)
				{
					String[] fechaHora;
					int minutos = 0;
					int horas = 0;
					exito = false;
					logger.info("Fecha hora reproceso=> "+fechaReproceso+" "+horaReproceso);
					while(!UtilidadFecha.compararFechas(fechaSolicitud,horaSolicitud,fechaReproceso,UtilidadFecha.convertirHoraACincoCaracteres(horaReproceso)).isTrue())
					{
						
						exito = true; //entro al ciclo
						fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaSolicitud,horaSolicitud,1,false);
						minutos ++;
						if(minutos==60)
						{
							minutos = 0;
							horas ++;
						}
						fechaSolicitud =  fechaHora[0];
						horaSolicitud = fechaHora[1];
						logger.info("horas calculadad=> "+horas+":"+minutos+", fechaSolicitud=>"+fechaSolicitud+", horaSolicitud=>"+horaSolicitud);
					}
					
					//debió haber entrado al ciclo
					if(exito)
					{
						
						String[] horasR = horasReproceso.split(":");
						if(horas<Utilidades.convertirAEntero(horasR[0])||(horas==Utilidades.convertirAEntero(horasR[0])&&minutos<Utilidades.convertirAEntero(horasR[1])))
							exito = true;
						else
							exito = false;
						
					}	
				}
				
			}
			//***********************************************************************************************
			
			return exito;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en superaTiempoReproceso de SQlBaseUtilidadLaboratoriosDao: "+e);
			return false;
		}
	}

	/**
	 * Método que valida si el formato de la hora de reproceso es válido
	 * @param horasReproceso
	 * @return
	 */
	private static boolean validarFormatoHoraReproceso(String horasReproceso) 
	{
		boolean valido = false;
		
		if(horasReproceso.length()==5)
		{
			String[] vector=horasReproceso.split(":");
			if(vector.length==2)
			{
				if(Utilidades.convertirAEntero(vector[1])<60)
					valido = true;
			}
		}
		
		return valido;
	}
	
	/**
	 * Método para obtener informacion adicional de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap getInformacionAdicionalSolicitud(Connection con,String numeroSolicitud)
	{
		try
		{
			String consulta = getInformacionAdicionalSolicitudStr;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),false,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en getInformacionAdicionalSolicitud de SqlBaseUtilidadLaboratoriosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar el log de inconsistencias
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarLogInconsistencias(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "INSERT INTO incon_interfaz_lab " +
				"(consecutivo,num_solicitud,estado,fecha,hora,descripcion,consecutivo_interfaz) VALUES " +
				"("+campos.get("secuencia")+",?,?,?,?,?,?)";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("estado")+""));
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fecha").toString())));
			pst.setString(4,UtilidadFecha.convertirHoraACincoCaracteres(campos.get("hora").toString()));
			pst.setString(5,campos.get("descripcion").toString());
			if(Utilidades.convertirADouble(campos.get("consecutivo")+"")>0)
				pst.setDouble(6,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			else
				pst.setNull(6,Types.NUMERIC);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogInconsistencias de SqlBaseUtilidadLaboratoriosDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método para obtener el codigo de laboratorio del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerCodigoLaboratorioServicio(Connection con,int codigoServicio)
	{
		int codigoLaboratorio = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "SELECT codigo_laboratorio FROM codigos_laboratorios WHERE codigo_servicio = "+codigoServicio;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				codigoLaboratorio = rs.getInt("codigo_laboratorio");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoLaboratorioServicio: "+e);
		}
		
		return codigoLaboratorio;
	}


}
