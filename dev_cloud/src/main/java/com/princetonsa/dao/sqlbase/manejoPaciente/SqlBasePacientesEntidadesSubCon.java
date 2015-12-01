package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * @author Jose Eduardo Arias Doncel
 * Enero 2008
 * */
public class SqlBasePacientesEntidadesSubCon
{
	
	/**
	 * para manejar el log en la clase
	 */
	static Logger logger = Logger.getLogger(SqlBasePacientesEntidadesSubCon.class);
	//Atributos
	
	/**
	 * Cadena sentencia sql para insertar registros en la tabla Pacientes Entidades SubContratadas
	 * */
	private static String strInsertarPacientesEntidadesSubcontratadas = "INSERT INTO " +
																		"pac_entidades_subcontratadas(" +
																		"consecutivo," +
																		"anio_consecutivo," +
																		"institucion," +
																		"centro_atencion," +
																		"entidad_subcontratada," +
																		"ingreso," +
																		"convenio," +
																		"contrato," +
																		"nro_carnet,"+
																		"autorizacion_ingreso," +
																		"fecha_autorizacion," +
																		"hora_autorizacion," +
																		"observaciones," +																		
																		"estado," +
																		"codigo_paciente," +
																		"usuario_modifica," +
																		"fecha_modifica," +
																		"hora_modifica) " +
																		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	
	
	/**
	 * Cadena sql de actualizacion de datos de la tabla pac_entidades_subcontratadas
	 * */
	private static String strActualizarPacienteEntidades = 	"UPDATE pac_entidades_subcontratadas " +
														   	"SET " +
														   	"centro_atencion = ?," +
														   	"entidad_subcontratada = ?, " +
														   	"convenio = ?," +
														   	"contrato = ?," +
														   	"nro_carnet = ?," +
														   	"autorizacion_ingreso = ?," +
														   	"fecha_autorizacion = ?," +
														   	"hora_autorizacion = ?," +
														   	"observaciones = ?," +
														   	"obser_anulacion = ?," +														   	
														   	"estado = ?," +
														   	"usuario_modifica = ?," +
														   	"fecha_modifica = ?," +
														   	"hora_modifica = ? " +
														   	"WHERE " +
														   	"consecutivo = ? ";
	
	/**
	 * Cadena sql de actualizacion de datos de la tabla pac_entidades_subcontratadas en su campo estado
	 * */
	private static String strActualizarEstadoPacienteEntidades = "UPDATE pac_entidades_subcontratadas " +
			"SET estado = ?," +
			"obser_anulacion = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE consecutivo = ? ";
	
	/**
	 * Cadena sql de consulta de datos de la tabla pac_entidades_subcontratadas
	 * */
	private static String strConsultaPacientesEntidades   = "SELECT " +
															"pes.consecutivo," +
															"pes.anio_consecutivo," +
															"pes.institucion," +
															"pes.centro_atencion AS centro_atencion," +
															"getnomcentroatencion(pes.centro_atencion) AS descripcion_centro_atencion," +
															"pes.entidad_subcontratada AS entidad_subcontratada," +
															"getDescEntitadSubContratada(pes.entidad_subcontratada) AS descripcion_nombre_entidad_sub," +															
															"pes.ingreso," +
															"pes.convenio AS codigo_convenio," +
															"getnombreconvenio(pes.convenio) AS descripcion_convenio," +
															"pes.contrato," +
															"pes.nro_carnet," +
															"pes.autorizacion_ingreso AS autorizacion_ingreso," +															
															"TO_CHAR(pes.fecha_autorizacion,'DD/MM/YYYY') AS fecha_autorizacion," +
															"pes.hora_autorizacion," +
															"pes.observaciones," +
															"pes.obser_anulacion," +
															"pes.estado," +
															"pes.codigo_paciente," +
															"facturacion.getidpaciente(pes.codigo_paciente) AS id_paciente," +
															"getnombrepersona(pes.codigo_paciente) AS nombre_paciente," +
															"to_char(pes.fecha_modifica,'DD/MM/YYYY') AS fecha_modifica," +
															"getnombreusuario(pes.usuario_modifica) AS usuario_modifica," +
															"pes.hora_modifica," +
															"'"+ConstantesBD.acronimoSi+"' AS estabd " +
															"FROM pac_entidades_subcontratadas pes " +
															"WHERE pes.institucion = ? ";
	
	/**
	 * Cadena sql de actualizacion de datos de la tabla det_serv_autorizados
	 * */
	private static String strActualizarServiciosAutorizados = "UPDATE det_serv_autorizados " +
															  "SET " +
															  "servicio = ?," +
															  "cantidad = ?," +
															  "autorizacion = ?," +
															  "fecha_autorizacion = ?," +
															  "responsable = ?," +
															  "observaciones = ?," +
															  "usuario_modifica = ?," +
															  "fecha_modifica = ?," +
															  "hora_modifica = ? " +
															  "WHERE " +
															  "consecutivo = ? ";
	
	/**
	 * Cadena sql de consulta de datos de la tabla det_serv_autorizados
	 * */
	private static String strConsultaServiciosAutorizados = "SELECT " +
															"dsa.consecutivo, " +
															"dsa.consecutivo_pac_entidades_sub AS consecutivo_pac_entidades_sub," +
															"dsa.anio_consecutivo_pac_entidades," +															
															"dsa.institucion," +
															"dsa.solicitud," +
															"dsa.servicio," +
															"getnombreservicio(dsa.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio," +
															"dsa.cantidad," +
															"dsa.autorizacion AS autorizacion_servicio," +
															"TO_CHAR(dsa.fecha_autorizacion,'DD/MM/YYYY') AS fecha_autorizacion," +
															"dsa.responsable," +
															"getnombreusuario(dsa.responsable) AS nombre_responsable," +
															"dsa.observaciones, " +
															"dsa.fecha_modifica," +
															"'"+ConstantesBD.acronimoSi+"' AS estabd " +															
															"FROM det_serv_autorizados dsa " +
															"WHERE dsa.institucion = ? ";
	
	/**
	 * Cadena sql de Eliminaciom de datps de la tabla det_serv_autorizados
	 * */
	private static String strEliminarServiciosAutorizados = "DELETE FROM det_serv_autorizados WHERE consecutivo = ? ";
	
	//Fin Atributos
	
	
	//Metodos
	
	
	/**
	 * Consulta la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public static HashMap consultarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		HashMap datos  = new HashMap();
		String cadena = strConsultaPacientesEntidades;
		
		try
		{
			if(parametros.containsKey("consecutivo") 
					&& !parametros.get("consecutivo").equals(""))
				cadena += " AND pes.consecutivo = "+parametros.get("consecutivo");
			
			if(parametros.containsKey("anioConsecutivo") 
					&& !parametros.get("anioConsecutivo").equals(""))
				cadena += " AND pes.anio_consecutivo = "+parametros.get("anioConsecutivo");
			
			if(parametros.containsKey("estado") 
					&& !parametros.get("estado").equals("") )
				cadena+=" AND pes.estado = '"+parametros.get("estado")+"' ";
			
			if(parametros.containsKey("codigoPaciente") && 
					!parametros.get("codigoPaciente").equals(""))
				cadena+="AND pes.codigo_paciente = "+parametros.get("codigoPaciente");
			
			//Parametros de la Consulta*************
			
			if(parametros.containsKey("codigoEntidadSub") 
					&& !parametros.get("codigoEntidadSub").equals(""))
				cadena += " AND pes.entidad_subcontratada = "+parametros.get("codigoEntidadSub");
			
			if(parametros.containsKey("codigoConvenio") 
					&& !parametros.get("codigoConvenio").equals(""))
				cadena += " AND pes.convenio = "+parametros.get("codigoConvenio");
			
			if(parametros.containsKey("fechaInicialBusqueda") 
					&& !parametros.get("fechaInicialBusqueda").equals(""))
				cadena += " AND pes.fecha_modifica BETWEEN '"+parametros.get("fechaInicialBusqueda")+"' AND '"+parametros.get("fechaFinalBusqueda")+"' ";
			
			if(parametros.containsKey("acronimoEstado") 
					&& !parametros.get("acronimoEstado").equals(""))
				cadena += " AND pes.estado = '"+parametros.get("acronimoEstado")+"' ";
			
			if(parametros.containsKey("loginUsuario") 
					&& !parametros.get("loginUsuario").equals(""))
				cadena += " AND pes.usuario_modifica = '"+parametros.get("loginUsuario")+"' ";
			
			//**************************************			
			
			
			cadena +=" ORDER BY descripcion_nombre_entidad_sub, pes.fecha_modifica ||  pes.hora_modifica DESC ";
			
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			if(parametros.get("listado").toString().equals(ConstantesBD.acronimoSi))
				datos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			if(parametros.get("listado").toString().equals(ConstantesBD.acronimoNo))
				datos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return datos;
	}
	
	
	/**
	 * Actualiza la informacion de la tabla Pacientes Entidades Subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean actualizarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		String cadena = strActualizarPacienteEntidades;
		
		if(parametros.containsKey("anioConsecutivo") 
				&& !parametros.get("anioConsecutivo").toString().equals(""))
			cadena+= " AND anio_consecutivo = '"+parametros.get("anioConsecutivo").toString()+"' ";
			
			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE pac_entidades_subcontratadas " +
														   	"SET " +
														   	"centro_atencion = ?," +
														   	"entidad_subcontratada = ?, " +
														   	"convenio = ?," +
														   	"contrato = ?," +
														   	"nro_carnet = ?," +
														   	"autorizacion_ingreso = ?," +
														   	"fecha_autorizacion = ?," +
														   	"hora_autorizacion = ?," +
														   	"observaciones = ?," +
														   	"obser_anulacion = ?," +														   	
														   	"estado = ?," +
														   	"usuario_modifica = ?," +
														   	"fecha_modifica = ?," +
														   	"hora_modifica = ? " +
														   	"WHERE " +
														   	"consecutivo = ? 
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("centroAtencion")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("entidadSubcontratada")+""));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("codigoConvenio")+""));
			ps.setInt(4,Utilidades.convertirAEntero(parametros.get("contrato")+""));
			ps.setString(5,parametros.get("nroCarnet")+"");
			
			if(!parametros.get("autorizacionIngreso").equals(""))
				ps.setString(6,parametros.get("autorizacionIngreso")+"");
			else
				ps.setNull(6,Types.VARCHAR);
				
			ps.setDate(7,Date.valueOf(parametros.get("fechaAutorizacionBD")+""));
			
			ps.setString(8,parametros.get("horaAutorizacion")+"");			
			
			if(!parametros.get("observaciones").equals(""))
				ps.setString(9,parametros.get("observaciones")+"");
			else
				ps.setNull(9,Types.VARCHAR);
			
			if(!parametros.get("obserAnulacion").equals(""))
				ps.setString(10,parametros.get("obserAnulacion")+"");
			else
				ps.setNull(10,Types.VARCHAR);
				
			ps.setString(11,parametros.get("estado")+"");
			ps.setString(12,parametros.get("usuarioModifica")+"");
			ps.setDate(13,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(14,parametros.get("horaModifica")+"");
			ps.setDouble(15,Utilidades.convertirADouble(parametros.get("consecutivo")+""));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}	
		
		return false;
	}
	
	
	/**
	 * Actualiza el estado del registro paciente entidades subcontratadas
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean actualizarEstadoPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		String cadena = strActualizarEstadoPacienteEntidades;
		
		if(parametros.containsKey("anioConsecutivo") 
				&& !parametros.get("anioConsecutivo").toString().equals(""))
			cadena+= " AND anio_consecutivo = '"+parametros.get("anioConsecutivo").toString()+"'";
			
			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));				
			
			/**
			 * UPDATE pac_entidades_subcontratadas " +
			"SET estado = ?," +
			"obser_anulacion = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE consecutivo = ? 
			 */
			
			ps.setString(1,parametros.get("estado")+"");
			ps.setString(2,parametros.get("observAnulacion")+"");			
			
			ps.setString(3,parametros.get("usuarioModifica")+"");
			ps.setDate(4,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(5,parametros.get("horaModifica")+"");
			
			ps.setDouble(6,Utilidades.convertirADouble(parametros.get("consecutivo")+""));
			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}	
		
		return false;
	}
	
	
	
	/**
	 * Inserta informacion en la tabla Pacientes Entidades Subcontratadas 
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String cadena
	 * @return boolean
	 * */
	public static boolean insertarPacientesEntidadesSubcontratadas(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarPacientesEntidadesSubcontratadas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
							"pac_entidades_subcontratadas(" +
							"consecutivo," +
							"anio_consecutivo," +
							"institucion," +
							"centro_atencion," +
							"entidad_subcontratada," +
							"ingreso," +
							"convenio," +
							"contrato," +
							"nro_carnet,"+
							"autorizacion_ingreso," +
							"fecha_autorizacion," +
							"hora_autorizacion," +
							"observaciones," +																		
							"estado," +
							"codigo_paciente," +
							"usuario_modifica," +
							"fecha_modifica," +
							"hora_modifica) " +
							"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			
			ps.setDouble(1,Utilidades.convertirADouble(parametros.get("consecutivo")+""));
			
			
			if(parametros.containsKey("anioConsecutivo") 
					&& !parametros.get("anioConsecutivo").toString().equals(""))			
				ps.setString(2,parametros.get("anioConsecutivo")+"");
			else
				ps.setNull(2,Types.VARCHAR);
				
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ps.setInt(4,Utilidades.convertirAEntero(parametros.get("centroAtencion")+""));
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("entidadSubcontratada")+""));
			
			if(!parametros.get("ingreso").equals(""))
				ps.setInt(6,Utilidades.convertirAEntero(parametros.get("ingreso")+""));
			else
				ps.setNull(6,Types.INTEGER);
			
			ps.setInt(7,Utilidades.convertirAEntero(parametros.get("codigoConvenio")+""));
			ps.setInt(8,Utilidades.convertirAEntero(parametros.get("contrato")+""));
			
			if(!parametros.get("nroCarnet").equals(""))			
				ps.setString(9,parametros.get("nroCarnet")+"");
			else
				ps.setNull(9,Types.VARCHAR);
			
			if(!parametros.get("autorizacionIngreso").equals(""))			
				ps.setString(10,parametros.get("autorizacionIngreso")+"");
			else
				ps.setNull(10,Types.VARCHAR);
				
			ps.setDate(11,Date.valueOf(parametros.get("fechaAutorizacionBD")+""));
			
			ps.setString(12,parametros.get("horaAutorizacion")+"");	
			
			if(!parametros.get("observaciones").equals(""))
				ps.setString(13,parametros.get("observaciones")+"");
			else
				ps.setNull(13,Types.VARCHAR);				
			
			ps.setString(14,parametros.get("estado")+"");
			ps.setInt(15,Utilidades.convertirAEntero(parametros.get("codigoPaciente")+""));
			ps.setString(16,parametros.get("usuarioModifica")+"");
			ps.setDate(17,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(18,parametros.get("horaModifica")+"");
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e )
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Consulta la informacion de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return HashMap datos
	 * */
	public static HashMap consultarServiciosAutorizados(Connection con, HashMap parametros)
	{
		HashMap datos  = new HashMap();
		String cadena = strConsultaServiciosAutorizados;
		
		try
		{
			if(parametros.containsKey("consecutivo") 
					&& !parametros.get("consecutivo").equals(""))
				cadena += " AND dsa.consecutivo = "+parametros.get("consecutivo");
			
			if(parametros.containsKey("consecutivoPacEntidadesSub") 
					&& !parametros.get("consecutivoPacEntidadesSub").equals(""))
				cadena += " AND dsa.consecutivo_pac_entidades_sub = "+parametros.get("consecutivoPacEntidadesSub");
			
			if(parametros.containsKey("anioConsecutivoPacEntidades") 
					&& !parametros.get("anioConsecutivoPacEntidades").equals(""))
				cadena += " AND dsa.anio_consecutivo_pac_entidades = '"+parametros.get("anioConsecutivoPacEntidades")+"' ";
				
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			datos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return datos;
	}
	
	
	/**
	 * Actuliza los datos de la tabla Detalle Servicios Autorizados
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean actualizarServiciosAutorizados(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarServiciosAutorizados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE det_serv_autorizados " +
										  "SET " +
										  "servicio = ?," +
										  "cantidad = ?," +
										  "autorizacion = ?," +
										  "fecha_autorizacion = ?," +
										  "responsable = ?," +
										  "observaciones = ?," +
										  "usuario_modifica = ?," +
										  "fecha_modifica = ?," +
										  "hora_modifica = ? " +
										  "WHERE " +
										  "consecutivo = ? 
			 */
			
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("cantidad")+""));
			
			if(parametros.containsKey("autorizacionServicio")
					&& !parametros.get("autorizacionServicio").equals(""))
				ps.setString(3,parametros.get("autorizacionServicio")+"");
			else
				ps.setNull(3,Types.VARCHAR);
			
			ps.setDate(4,Date.valueOf(parametros.get("fechaAutorizacion")+""));			
			ps.setString(5,parametros.get("responsable")+"");
			
			if(parametros.containsKey("observaciones") 
					&& !parametros.get("observaciones").equals(""))
				ps.setString(6,parametros.get("observaciones")+"");
			else
				ps.setNull(6,Types.VARCHAR);
			
			ps.setString(7,parametros.get("usuarioModifica")+"");
			ps.setDate(8,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(9,parametros.get("horaModifica")+"");		
			
			ps.setInt(10,Utilidades.convertirAEntero(parametros.get("consecutivo")+""));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Inserta datos en la tabla Detalle Servicios Autorizados
	 * @param Conenction con
	 * @param HashMap parametros
	 * @param String cadena
	 * */
	public static boolean insertarServiciosAutorizados(Connection con, HashMap parametros, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
								"det_serv_autorizados(consecutivo," +
								"					  consecutivo_pac_entidades_sub," +
								" 					  anio_consecutivo_pac_entidades," +
								"					  institucion," +
								"					  servicio," +
								"					  cantidad," +
								"				 	  autorizacion," +
								"					  fecha_autorizacion," +
								"					  responsable," +
								"					  observaciones," +
								"					  usuario_modifica," +
								"					  fecha_modifica," +
								"					  hora_modifica) " +
								"VALUES('seq_det_serv_auto'),?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(parametros.get("consecutivoPacEntidadesSub")+""));
			ps.setString(2,parametros.get("anioConsecutivoPacEntidades")+"");
			
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ps.setInt(4,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("cantidad")+""));
			
			if(parametros.containsKey("autorizacionServicio") 
					&& !parametros.get("autorizacionServicio").equals(""))
			ps.setString(6,parametros.get("autorizacionServicio")+"");
			
			ps.setDate(7,Date.valueOf(parametros.get("fechaAutorizacion")+""));
			ps.setString(8,parametros.get("responsable")+"");
			
			if(parametros.containsKey("observaciones") 
					&& !parametros.get("observaciones").equals(""))
				ps.setString(9,parametros.get("observaciones")+"");
			else
				ps.setNull(9,Types.VARCHAR);
			
			ps.setString(10,parametros.get("usuarioModifica")+"");
			ps.setDate(11,Date.valueOf(parametros.get("fechaModifica")+""));
			ps.setString(12,parametros.get("horaModifica")+"");
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * Elimina Servcios Autorizados 
	 * @param Connection con
	 * @param HashMap parametros
	 * @return boolean 
	 * */
	public static boolean eliminarServiciosAutorizados(Connection con, HashMap parametros)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarServiciosAutorizados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("consecutivo")+""));			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}	
		
		return false;
	}
	
	/**
	 * Método implementado para reversar el registro de entidades subcontratadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int reversarPacienteEntidadSubcontratada(Connection con,HashMap campos)
	{
		int resp = 0;
		try
		{
			//Reversión del encabezado del registro
			String consulta = "UPDATE pac_entidades_subcontratadas SET " +
				"estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', " +
				"ingreso = ?," +
				"usuario_modifica = ?," +
				"fecha_modifica = current_date, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
				"WHERE consecutivo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setNull(1,Types.INTEGER);
			pst.setString(2,campos.get("usuario")+"");
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			resp = pst.executeUpdate();
			
			if(resp>0)
			{
				//Reversión del detalle del registro
				consulta = "UPDATE det_serv_autorizados SET " +
					"solicitud = ?, " +
					"usuario_modifica = ?," +
					"fecha_modifica = current_date, " +
					"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
					"WHERE consecutivo_pac_entidades_sub = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setNull(1,Types.INTEGER);
				pst.setString(2,campos.get("usuario")+"");
				pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivo")+""));
				pst.executeUpdate();
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en reversarPacienteEntidadSubcontratada: "+e);
			resp = 0;
		}
		return resp;
	}
	
	
	//Fin Metodos
}