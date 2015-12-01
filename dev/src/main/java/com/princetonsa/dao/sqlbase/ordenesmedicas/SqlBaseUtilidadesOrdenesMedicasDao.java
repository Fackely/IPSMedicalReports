/*
 * Junio 3, 2008
 */
package com.princetonsa.dao.sqlbase.ordenesmedicas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author sgomez
 * Objeto usado para el acceso común a la fuente de datos
 * de utilidades propias del módulo de ORDENES MEDICAS
 */
public class SqlBaseUtilidadesOrdenesMedicasDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesOrdenesMedicasDao.class);
	
	/**
	 * Método para obtener la especialidad solicitada de una interconsulta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosInt obtenerEspecialidadSolicitadaInterconsulta(Connection con,String numeroSolicitud)
	{
		InfoDatosInt especialidad = new InfoDatosInt();
		try
		{
			String consulta = "SELECT " +
				"s.especialidad AS codigo_especialidad," +
				"getnombreespecialidad(s.especialidad) AS nombre_especialidad " +
				"FROM solicitudes_inter si " +
				"INNER JOIN servicios s ON(s.codigo = si.codigo_servicio_solicitado) " +
				"WHERE si.numero_solicitud = "+numeroSolicitud;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				especialidad.setCodigo(rs.getInt("codigo_especialidad"));
				especialidad.setNombre(rs.getString("nombre_especialidad"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidadSolicitadaInterconsulta: "+e);
		}
		return especialidad;
	}

	
	/**
	 * Método para obtener la fecha valoracion inicial de la solicitud
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String consultarFechaValoracionInicial(Connection con, int codigoCuenta, String consultarFechaValoracion) 
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaValoracion, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				return rs.getString("fecha_valoracion");	
			}			
		}
		catch (SQLException e) 
		{
			logger.error("Error en consultar Fecha Valoracion Inicial");
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Método para obtener la fecha / hora de la solicitud
	 * [0] fecha
	 * [1] hora
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public static String[] obtenerFechaHoraSolicitud(Connection con,String numeroSolicitud)
	{
		String[] fechaHora = {"",""};
		try
		{
			String consulta = "SELECT " +
				"to_char(fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud," +
				"substr(hora_solicitud,0,6) as hora_solicitud " +
				"from solicitudes where numero_solicitud = "+numeroSolicitud;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				fechaHora[0] = rs.getString("fecha_solicitud");
				fechaHora[1] = rs.getString("hora_solicitud");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaHoraSolicitud: "+e);
		}
		return fechaHora;
	}
	
	/**
	 * Método para obtener el servicio de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * 
	 */
	public static int obtenerServicioSolicitud(Connection con,String numeroSolicitud)
	{
		int servicio=ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "SELECT codigo_servicio_solicitado as servicio from sol_procedimientos where  numero_solicitud = "+numeroSolicitud;
			logger.info("\n\n\n [ConsultaServiciosolicitud]"+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				servicio = rs.getInt("servicio");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaHoraSolicitud: "+e);
		}
		return servicio;
	}
	
	/**
	 * Método para obtener el centro de costos solicitante / solicitado servicio de la solicitud
	 * @param con
	 * @param parametros
	 * @return 
	 */
	public static InfoDatosInt obtenerCentroCostoSoli(Connection con,HashMap parametros)
	{
		InfoDatosInt info = new InfoDatosInt();		
		try
		{
			String consulta = "SELECT centro_costo_solicitante, centro_costo_solicitado from solicitudes where numero_solicitud = "+parametros.get("numeroSolicitud").toString();			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				info.setCodigo(rs.getInt(1));
				info.setDescripcion(rs.getString(2));
			}			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentrosCostosSoli: "+e);
		}
		
		return info;
	}
	
	
	/**
	 * Método para obtener la subcuenta de una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerSubCuentaSolicitudCirugia(Connection con,String numeroSolicitud)
	{
		String subCuenta = "";
		try
		{
			String consulta = "select coalesce(sub_cuenta||'','') as sub_cuenta from solicitudes_cirugia WHERE numero_solicitud = "+numeroSolicitud;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				subCuenta = rs.getString("sub_cuenta");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSubCuentaSolicitudCirugia: "+e);
		}
		return subCuenta;
	}
	
	
	/**
	 * Método para consultar el codigo pk de solicitud de cirugia por servicio
	 * @param Connection con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap obtenerSolCirugiaServicio(Connection con,HashMap parametros)
	{		
		try
		{
			String consulta = "select codigo from sol_cirugia_por_servicio WHERE numero_solicitud = "+parametros.get("numeroSolicitud").toString()+" AND servicio =  "+parametros.get("servicio").toString();
			logger.info("valor de la consulta >> "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			st.close();
			return mapaRetorno;			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSolCirugiaServicio: "+e);
		}
		
		return new HashMap();
	}
	
	/**
	 * Método que verifica si se puede abrir la seccion de prescripción diálisis
	 * validando por codigo de institucion y codigo de centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean deboAbrirPrescripcionDialisis(Connection con,HashMap campos)
	{
		boolean revision = false;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS***********************************
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			int codigoCentroCosto = Integer.parseInt(campos.get("codigoCentroCosto").toString());
			//********************************************************************
			
			String consulta = "SELECT count(1) as cuenta from prescrip_dialisis_inst_cc " +
				"where " +
				" (centro_costo = "+codigoCentroCosto+" or centro_costo is null) "+
				" and institucion = "+codigoInstitucion+
				" and activo = '"+ConstantesBD.acronimoSi+"' ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					revision = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en deboAbrirPrescripcionDialisis: "+e);
		}
		
		return revision;
	}
	
	
	/**
	 * Método para obtener el codigo del médico que responde la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerCodigoMedicoRespondeSolicitud(Connection con,HashMap campos)
	{
		int codigoMedico = ConstantesBD.codigoNuncaValido;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS*******************************
			int numeroSolicitud = Utilidades.convertirAEntero(campos.get("numeroSolicitud").toString());
			//****************************************************************
			
			String consulta = "select " +
				"coalesce(codigo_medico_responde,"+ConstantesBD.codigoNuncaValido+") as codigo_medico_responde " +
				"from solicitudes where numero_solicitud = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				codigoMedico = rs.getInt("codigo_medico_responde");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMedicoRespondeSolicitud: "+e);
		}
		return codigoMedico; 
	}
	
	/**
	 * Método para cargar las opciones de manejo de la interconsulta
	 * 
	 * @param con
	 * @param filtroOpciones: Si se desea filtrar por unas opciones específicas entonces se mandan los codigos separados por comas
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerOpcionesManejoInterconsulta(Connection con,String filtroOpciones)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "select codigo,nombre from op_man_intercon";
			if(!filtroOpciones.equals(""))
				consulta += " WHERE codigo in ("+filtroOpciones+") ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
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
			logger.error("Error obtenerOpcionesManejoInterconsulta: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para verificar si existen solicitudes de medicamentos pendientes para despachar
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean existenSolicitudesMedicamentosPendientesDespachar(Connection con,int idCuenta)
	{
		boolean existe = false;
		try
		{
			String consulta = "select count(1) as cuenta " +
				"from solicitudes s " +
				"where " +
				"s.cuenta = "+idCuenta+" and " +
				"s.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" and " +
				"s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" and " +
				"s.numero_solicitud not in (select d.numero_solicitud from despacho d where d.numero_solicitud = s.numero_solicitud)";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en existenSolicitudesMedicamentosPendientesDespachar: "+e);
		}
		
		return existe;
	}
	
	
	/**
	 * Método que devuelve el centro de costo que ejecuta parametrizado en la funcionalidad
	 * de "Excepciones Centros de Costo Interconsultas" si este existe
	 * @param con
	 * @param centroCostoSolicita
	 * @param servicio
	 * @param medico
	 * @return
	 */
	public static int obtenerCCEjecutaXExcepCCInter( Connection con, String centroCostoSolicita, String servicio, String medico) {
		int centroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		String consulta = 	"SELECT " +
								"centro_costo_ejecuta " +
							"FROM " +
								"exc_cc_interconsulta " +
							"WHERE " +
								"centro_costo_solicita="+centroCostoSolicita+" "+
								"AND servicio="+servicio+" " +
								"AND medico_ejecuta="+medico;
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				centroCostoEjecuta = rs.getInt("centro_costo_ejecuta");
		}
		catch(SQLException e)
		{
			logger.error("Error en existeExcepcionCentroCostoInterconsulta: "+e);
		}
		return centroCostoEjecuta;
	}
	
	
	/**
	 * 
	 * @param servicio
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoSolicita
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerCentrosCostoEjecuta(int servicio, int institucion, int centroAtencion, int centroCostoSolicita)
	{
		ArrayList<InfoDatosInt> arrayCC= new ArrayList<InfoDatosInt>();
		arrayCC.add(new InfoDatosInt(ConstantesBD.codigoNuncaValido, "Seleccione"));
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			String tipoServicio=Utilidades.obtenerTipoServicio(con, servicio+"");
			
			String consulta = 	"select tabla.* from " +
									"(" +
										"select " +
											"codigo_centro_costo as codigo, " +
											"desc_centro_costo ||' ['|| desc_centro_atencion_cc||']' as nombre " +
										"from " +
											"view_centros_costo_serv " +
										"where " +
											"codigo_servicio="+servicio+" " +
											"and institucion="+institucion+" " +
											"and centro_atencion_grupo_serv="+centroAtencion+" " ;
											//Modificado por tarea 97406
											//"and centro_atencion_centro_costo="+centroAtencion+" "; 
									if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"") 
										|| tipoServicio.equals(ConstantesBD.codigoServicioAntiguoInterconsulta+""))
									{	
										consulta+=
										"union " +
										"select " +
											"centro_costo_ejecuta as codigo, " +
											"getnomcentrocosto(centro_costo_ejecuta) || ' ['|| getnomcentroatencion(centro_atencion) ||']' as nombre " +
										"from " +
											"exc_cc_interconsulta " +
										"where " +
											"servicio="+servicio+" " +
											"and institucion="+institucion+" " +
											"and centro_atencion="+centroAtencion+" " +
											"and centro_costo_solicita="+centroCostoSolicita+" ";
									}	
									consulta+=")tabla order by tabla.nombre ";

			logger.info("\n\n\n obtenerCentrosCostoEjecuta--->"+consulta+"    servicio->"+servicio+" instit->"+institucion+" centroAten->"+centroAtencion+" ccsolicita->"+centroCostoSolicita+"\n\n\n");
			Statement pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery(consulta));
			
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("codigo"));
				info.setNombre(rs.getString("nombre"));
				arrayCC.add(info);
			}
			
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentrosCostoEjecuta: "+e);
		}
		
		if(arrayCC.size()==2)
			arrayCC.remove(0);//quitamos el seleccione
		
		return arrayCC;
	}
	
	/**
	 * 
	 * @param institucion
	 * @param centroAtencion
	 * @param centroCostoEjecuta
	 * @param centroCostoSolicita
	 * @param servicio
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerProfesionalesEjecutan(int institucion, int centroAtencion, int centroCostoEjecuta, int centroCostoSolicita, int servicio)
	{
		ArrayList<InfoDatosInt> arrayProfesionales= new ArrayList<InfoDatosInt>();
		arrayProfesionales.add(new InfoDatosInt(ConstantesBD.codigoNuncaValido, "Seleccione"));
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			String tipoServicio=Utilidades.obtenerTipoServicio(con, servicio+"");
			
			String consulta = 	"select tabla.* from " +
									"(" +
										"select " +
											"vpa.codigo_medico as codigo, " +
											"getnombrepersona(vpa.codigo_medico) as nombre " +
										"from " +
											"view_profesionales_activos vpa " +
											"INNER JOIN usuarios u ON(u.codigo_persona=vpa.codigo_medico) " +
											"INNER JOIN centros_costo_usuario ccu ON(ccu.usuario=u.login) " +
										"where " +
											"u.institucion="+institucion+" " +
											"and ccu.centro_costo="+centroCostoEjecuta+" ";
									if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"") 
										|| tipoServicio.equals(ConstantesBD.codigoServicioAntiguoInterconsulta+""))
									{	
										consulta+=	
										"union " +
										"select " +
											"eci.medico_ejecuta as codigo, " +
											"getnombrepersona(eci.medico_ejecuta) as nombre " +
										"from " +
											"exc_cc_interconsulta eci " +
										"where " +
											"eci.servicio="+servicio+" " +
											"and eci.institucion="+institucion+" " +
											"and eci.centro_atencion="+centroAtencion+" " +
											"and eci.centro_costo_solicita="+centroCostoSolicita+" " +
											"and eci.centro_costo_ejecuta="+centroCostoEjecuta+" ";
									}	
									consulta+=")tabla order by tabla.nombre ";

			logger.info("\n\n\n obtenerProfesionalesEjecutan--->"+consulta+"    servicio->"+servicio+" instit->"+institucion+" centroAten->"+centroAtencion+" ccsolicita->"+centroCostoSolicita+"\n\n\n");
			Statement pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery(consulta));
			
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("codigo"));
				info.setNombre(rs.getString("nombre"));
				arrayProfesionales.add(info);
			}
			
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerProfesionalesEjecutan: "+e);
		}
		
		return arrayProfesionales;
	}
	
	
	public static HashMap obtenerConvenioEstadoSolicitud(int numSolicitud)
	{
		HashMap convenios= new HashMap();
		String consulta="SELECT " +
						"detauto.numero_solicitud AS numerosolucitud, " +
						"detauto.estado AS estado, " +
						"coalesce (detauto.activo,'"+ConstantesBD.acronimoNo+"') AS activo, " +
						"getnombreconvenio(auto.convenio) AS descripcionconvenio " +
						"FROM det_autorizaciones detauto " +
						"INNER JOIN manejopaciente.autorizaciones auto ON (auto.codigo = detauto.autorizacion) " +
						"WHERE detauto.numero_solicitud = ?  AND detauto.activo = '"+ConstantesBD.acronimoSi+"'";
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet)));
		    pst.setInt(1, numSolicitud);
		    consulta= consulta.replace("detauto.numero_solicitud = ?", "detauto.numero_solicitud = "+numSolicitud);
		    logger.info("La consulta solicitud es :>>> "+ consulta );
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			convenios= UtilidadBD.cargarValueObject(rs, true, false);
			
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
			{
				logger.error("Error en obtener Estados Convenios Autorizaciones:  >>  SqlBaseUtilidadesOrdenesMedicasDao "+e);
			}
		return convenios;
		
	}
	
	/**
	 * 
	 */
	public static boolean tieneUsuarioEntidadSubcontratada(HashMap parametros)
	{
		String consulta = "SELECT COUNT(consecutivo) AS cuenta FROM usuarios_entidad_sub WHERE entidad_subcontratada = ? AND usuario = ? ";
		boolean respuesta = false;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			//logger.info("valor del mapa >> "+parametros);
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet)));
		    pst.setInt(1, Utilidades.convertirAEntero(parametros.get("entidadSub").toString()));
		    pst.setString(2,parametros.get("usuario").toString());
		    
		    logger.info("SQL / tieneUsuarioEntidadSubcontratada / "+consulta);
		    logger.info("entidadSub "+parametros.get("entidadSub").toString());
		    logger.info("usuario "+parametros.get("usuario").toString());
		    
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta") > 0)
					respuesta =  true;
			}
			
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneUsuarioEntidadSubcontratada:  >>  "+e);
		}
		
		return respuesta;
	}
	
	/**
	 * Obtiene las especialidades de un profesional
	 * @param codigoMedico
	 * @return ArrayList<InfoDatosInt>
	 */
	public static ArrayList<InfoDatosInt> obtenerEspecialidadProfesionalEjecutan(int codigoMedico)
	{
		ArrayList<InfoDatosInt> arrayEspecialidadProfesional= new ArrayList<InfoDatosInt>();
		//arrayEspecialidadProfesional.add(new InfoDatosInt(ConstantesBD.codigoNuncaValido, "Seleccione"));
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			String consulta = "SELECT " +
					"sm.codigo_especialidad AS codigo_espm, " +
					"esp.nombre AS especialidad " +
					"FROM especialidades_medicos sm " +
					"INNER JOIN especialidades esp ON (esp.codigo = sm.codigo_especialidad) " +
					"WHERE activa_sistema="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND codigo_medico = ? " +
					"ORDER BY esp.nombre ";
			logger.info(">>>>>> Consulta obtenerEspecialidadProfesionalEjecutan > "+consulta+"  codigoMedico -> "+codigoMedico);
			PreparedStatementDecorator pst = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet)));
		    pst.setInt(1, codigoMedico);
		    ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				InfoDatosInt info= new InfoDatosInt();
				info.setCodigo(rs.getInt("codigo_espm"));
				info.setNombre(rs.getString("especialidad"));
				arrayEspecialidadProfesional.add(info);
			}
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEspecialidadProfesionalEjecutan: "+e);
		}
		return arrayEspecialidadProfesional;
	}
	
	/**
	 * Método para obtener el ingreso de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerIngresoXNumeroSolicitud(Connection con,String numeroSolicitud)
	{
		String idIngreso = "";
		try
		{
			String consulta = "Select c.id_ingreso as ingreso from solicitudes s inner join cuentas c on (c.id = s.cuenta) WHERE s.numero_solicitud = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				idIngreso = rs.getString("ingreso");
			}
			rs.close();
			pst.close();
		}
		catch(SQLException  e)
		{
			logger.error("Error en obtenerIngresoXNumeroSolicitud: "+e);
		}
		return idIngreso;
	}
	
}