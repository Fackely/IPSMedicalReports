package com.princetonsa.dao.sqlbase.historiaClinica;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/** 
 * @author Jose Eduardo Arias Doncel 
 * */
public class SqlBaseConsultaTerapiasGrupalesDao
{

	static Logger logger = Logger.getLogger(SqlBaseConsultaTerapiasGrupalesDao.class);
	
	//-------------Atributos
	
	/**
	 * Consulta las terapias grupales a partir del paciente
	 * */
	private static String strConsultaTGrupalPaciente = "SELECT " +
			"cc.centro_atencion AS centroatencion, " +
			"getnomcentroatencion(cc.centro_atencion) AS desccentroatencion," +
			"cc.codigo AS centrocosto, " +
			"cc.nombre AS desccentrocosto," +
			"to_char(ah.fecha_admision,'DD/MM/YYYY') || ' '  || ah.hora_admision AS fechaadmision," +
			"ah.hora_admision AS horaadmision," +
			"per.primer_apellido || ' ' || per.segundo_apellido || ' ' ||  per.primer_nombre || ' ' ||  per.segundo_nombre AS descripcionusuario, "+
 			"tg.codigo AS codigoterapia," +			
			"to_char(tg.fecha_solicitud ,'DD/MM/YYYY') || ' ' || tg.hora_solicitud AS fechasolicitud," +
			"tg.hora_solicitud AS horasolicitud," +
			"tg.codigo_servicio || ' - ' || getnombreservicio(tg.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcionservicio," +
			"tg.codigo_servicio AS codigoservicio," +
			"tg.tipo_servicio AS tiposervicio," +
			"tg.causa_externa AS causaexterna,"+
			"tg.maneja_rips AS manejarips," +
			"tg.finalidad_consulta AS finalidadconsulta," +
			"tg.finalidad_procedimiento AS fprocedimiento,"+
			"tg.observaciones," +
			"tg.usuario_modifica AS usuariomodifica "+			
			"FROM cargos_directos_hc tg " +
			"INNER JOIN cuentas c ON (c.codigo_paciente = ?) " +
			"LEFT OUTER JOIN admisiones_hospi ah ON (ah.cuenta = c.id) "+
			"INNER JOIN cuentas_terapia_grupal ctg ON (ctg.codigo_terapia = tg.codigo AND ctg.cuenta = c.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = c.area) " +
			"INNER JOIN usuarios usa ON (usa.login = tg.usuario_modifica) " +
			"INNER JOIN personas per ON (per.codigo = usa.codigo_persona) " +
			"WHERE tg.tipo = '"+ConstantesIntegridadDominio.acronimoTerapiaGrupal+"' "; //se filtra que solo sean terapias grupales
	
	
	/**
	 * Consulta General de terapias grupales
	 * */
	private static String strConsultaTGrupal = "SELECT DISTINCT " +
			"tg.codigo AS codigoterapia," +
			"getcentroatentgrupal(tg.codigo) AS centroatencion, " +			
			"getnomcentroatencion(getcentroatentgrupal(tg.codigo)) AS desccentroatencion," +			
			"getnomcentrocosto(cu.area) AS desccentrocosto," +
			"to_char(tg.fecha_solicitud,'DD/MM/YYYY') || ' ' || tg.hora_solicitud AS fechasolicitud," +
			"tg.hora_solicitud AS horasolicitud," +			
			"per.primer_apellido || ' ' || per.segundo_apellido || ' ' ||  per.primer_nombre || ' '  || per.segundo_nombre AS descripcionusuario, "+			
			"tg.codigo_servicio || ' ' || getnombreservicio(tg.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcionservicio," +
			"tg.codigo_servicio AS codigoservicio," +
			"tg.observaciones, " +
			"getnombreespecialidad(sol.especialidad_solicitada) AS especialidadprofesional " +						
			"FROM cargos_directos_hc tg " +
			"inner join cargos_directos cd on (cd.codigo_datos_hc=tg.codigo) " +
			"inner join solicitudes sol on(cd.numero_solicitud=sol.numero_solicitud) " +
			"inner join cuentas cu on (cu.id=sol.cuenta) " +						
			"INNER JOIN usuarios usa ON (usa.login = tg.usuario_modifica) " +
			"INNER JOIN personas per ON (per.codigo = usa.codigo_persona) " +
			
			"WHERE tg.tipo = '"+ConstantesIntegridadDominio.acronimoTerapiaGrupal+"' AND tg.institucion = ? " ;
	
		
	
	/**
	 * Consulta pacientes relacionados a la terapia Grupal 
	 * */
	private static String strConsultaPacientesAsocioTGrupal = "SELECT " +			
			"to_char(ah.fecha_admision,'DD/MM/YYYY') || ' ' || ah.hora_admision AS fechaadmision," +
			"ah.hora_admision AS horaadmision," +
			"per.numero_identificacion AS numeroidentificacion," +
			"per.tipo_identificacion AS tipoidentificacion," +
			"per.primer_apellido || ' ' || per.segundo_apellido || ' ' ||  per.primer_nombre || ' ' ||  per.segundo_nombre AS descripcionpaciente "+									
			"FROM cuentas_terapia_grupal ctg " +
			"INNER JOIN cuentas c ON (c.id = ctg.cuenta) " +
			"LEFT OUTER JOIN admisiones_hospi ah ON (ah.cuenta = ctg.cuenta) "+			
			"INNER JOIN personas per ON (per.codigo = c.codigo_paciente) " +
			"WHERE ctg.codigo_terapia = ? ";
	
	
	/**
	 * Consulta los responsables existentes de registrar las terapias Grupales
	 * */
	private static String strConsultaResponsablesRegistro = " SELECT " +
			"per.codigo AS codigopersona," +
			"per.primer_apellido || ' ' || per.segundo_apellido || ' ' ||  per.primer_nombre || ' ' ||  per.segundo_nombre AS descripcionresponsable " +
			"FROM cargos_directos_hc tg " +
			"INNER JOIN usuarios usa ON (usa.login = tg.usuario_modifica) " +
			"INNER JOIN personas per ON (per.codigo = usa.codigo_persona) " +
			"WHERE tg.tipo = '"+ConstantesIntegridadDominio.acronimoTerapiaGrupal+"' and tg.institucion = ? " +
			"GROUP BY per.codigo,per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre " +
			"ORDER BY per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre DESC ";		
				
	
		
	
	/**
	 * Indice Consulta terapias Grupales por paciente
	 * */
	private static final String [] indices_tGrupalPaciente = {"centroatencion_","desccentroatencion_","fechaadmision_","horaadmision_","descripcionusuario_","codigoterapia_",
		"fechasolicitud_","horasolicitud_","descripcionservicio_","codigoservicio_","tiposervicio_","causaexterna_","manejarips","finalidadconsulta_",
		"fprocedimiento_","observaciones_","usuariomodifica_"};
	
	
	/**
	 * Indice Consulta terapias
	 * */
	private static final String [] indices_tGrupal = {"codigoterapia_","centroatencion_","desccentroatencion_","fechasolicitud_","horasolicitud_",
		"descripcionusuario_","codigoservicio_","observaciones_"};
	
	/**
	 * Pacientes asociados a la Terapia Grupal
	 * */
	private static final String [] indices_TGrupalDetalle = {"fechaadmision_","horaadmision_","numeroidentificacion_","tipoidentificacion_",
		"descripcionpaciente_"};
	
	//--------------------Metodos
	
	
	/**
	 * Consulta las terapias Grupales a partir del codigo paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarTGrupalPaciente(Connection con, HashMap parametros)
	{
		HashMap respuesta;		
		PreparedStatementDecorator ps =  null;
		try
		{			
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaTGrupalPaciente));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPersona")+""));
			
			logger.info("cadena >> "+strConsultaTGrupalPaciente+" codigopersona >>"+parametros);
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);			
			respuesta.put("INDICES_MAP",indices_tGrupalPaciente);
			
			return respuesta;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaTerapiasGrupalesDao "+sqlException.toString() );
				}
			}
		}
			
		
		return null;	
	}	
	
	
	/**
	 * Consulta de la terapias Grupales
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarTerapiasGrupales(Connection con, HashMap parametros)
	{
		HashMap resultado;
		String cadena = strConsultaTGrupal;	
		PreparedStatementDecorator ps =  null;
		try
		{			
			
			if(parametros.containsKey("centroAtencion"))			
				cadena+= " AND getcentroatentgrupal(tg.codigo) = "+parametros.get("centroAtencion").toString();
			
			if(parametros.containsKey("fechaInicialTerapiaABD"))
				cadena+=" AND tg.fecha_solicitud BETWEEN '"+parametros.get("fechaInicialTerapiaABD").toString()+"' AND '"+parametros.get("fechaFinalTerapiaABD").toString()+"' ";
			
			if(parametros.containsKey("profesionalRegistra"))
				cadena+=" AND per.codigo = "+parametros.get("profesionalRegistra");
			
			if(parametros.containsKey("centroCosto"))
				cadena+=" AND cu.area = "+parametros.get("centroCosto").toString();
			
			//para que funcione oracle, se hizo distinct en la consulta ppal
			/*cadena+=" GROUP BY tg.codigo, centroatencion, desccentroatencion, desccentrocosto, tg.fecha_solicitud, tg.hora_solicitud, descripcionusuario, descripcionservicio," +
			"codigoservicio, observaciones ORDER BY tg.fecha_solicitud ";*/
						
			logger.info("cadena->"+cadena);
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
			resultado.put("INDICES_MAP",indices_tGrupal);
			
			return resultado;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaTerapiasGrupalesDao "+sqlException.toString() );
				}
			}
		}
		
		return null;
	}	
	
	
	/**
	 * Consulta los pacientes asociados a una terapia Grupal
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleTGrupal(Connection con, HashMap parametros)
	{
		HashMap respuesta;		
		PreparedStatementDecorator ps = null;
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaPacientesAsocioTGrupal));
			ps.setDouble(1,Utilidades.convertirADouble(parametros.get("codigoTerapia")+""));		
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);			
			
			respuesta.put("INDICES_MAP",indices_TGrupalDetalle);
			
			return respuesta;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaTerapiasGrupalesDao "+sqlException.toString() );
				}
			}
		}
		
		return null;				
	}	
	
	/**
	 * Consulta los Responsables del Registro de Terapias Grupales
	 * @param Connection con
	 * @param HashMao parametros
	 * */
	public static ArrayList<HashMap<String, Object>> consultarResponsableRegistro(Connection con, HashMap parametros)
	{
		
		ArrayList array = new ArrayList<HashMap<String,Object>>();
		HashMap resultado;
		ResultSetDecorator rs = null;;
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaResponsablesRegistro)); 
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				resultado = new HashMap();
				resultado.put("codigopersona",rs.getObject(1));
				resultado.put("descripcionresponsable",rs.getObject(2));
				
				array.add(resultado);
			}			
			
			return array;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaTerapiasGrupalesDao "+sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaTerapiasGrupalesDao "+sqlException.toString() );
				}
			}
		}
		
		return array;
	}
}