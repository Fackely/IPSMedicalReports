package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;

public class SqlBaseAnulacionCondonacionMultasDao {

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAnulacionCondonacionMultasDao.class);
	
	/**
	 * Cadena Sql que realiza la consulta de las multas asociadas a un Paciente
	 */
	private static String consultaMultasCita="SELECT " +
						  "c.codigo AS codigocita, " +
						  "c.codigo_paciente AS codigopaciente, " +
						  "c.estado_cita AS estadocita, " +
						  "getNombreEstadoCita(c.estado_cita) AS nombreestadocita,  " +
						  "c.unidad_consulta AS unidad_agenda, " +
						  "getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidadagenda, " +
						  "ag.fecha AS fecha_cita, " +
						  "ag.hora_inicio AS hora_cita, " +
						  "c.usuario AS usuario, " +
						  "ag.codigo_medico AS codigomedico, " +
						  "getnombrepersona(ag.codigo_medico) AS nombreprofesional, " +
						  "mult.consecutivo AS consecutivo_multa, " +
						  "mult.fecha_generacion AS fecha_multa, " +
						  "mult.hora_generacion AS hora_multa, " +
						  "mult.estado AS estado_multa, " +
						  "mult.valor AS valor_multa, " +
						  "mult.mot_anu_cond_multa  AS codigomotivomulta, " +
						  "mot.descripcion AS descripcionmotivo, " +
						  "ag.centro_atencion AS codcentroatencion, " +
						  "getNomCentroAtencion(ag.centro_atencion) AS nombrecentroatencion, " +
						  "c.convenio AS codconvenio, " +
						  "getNombreConvenio(c.convenio) AS nombreconvenio " +
						  "FROM multas_citas mult " +
						  "INNER JOIN cita c ON (mult.cita = c.codigo) " +
						  "INNER JOIN agenda ag ON (ag.codigo = c.codigo_agenda) " +
						  "LEFT OUTER JOIN mot_anu_cond_multas mot ON (mot.consecutivo = mult.mot_anu_cond_multa) " +
						  "WHERE c.codigo_paciente = ? AND " +
						  //"c.usuario=? "+
						  "(SELECT count(1) AS autorizado from consultaexterna.unid_agenda_usu_caten caten INNER JOIN consultaexterna.unid_agenda_act_auto auto ON(auto.unid_agenda_usu_caten=caten.codigo) WHERE usuario_autorizado=? AND actividad_autorizada IN("+ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas+", "+ConstantesBD.codigoActividadAutorizadaAnularMultasCitas+")) > 0" +
						  "  AND  mult.estado = '"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' ";
						  
				  
   /**
    * Cadena Sql que realiza la consulta de  las multas por rango
    */
	private static String consultaMultasCita2="SELECT " +
						  "c.codigo AS codigocita, " +
						  "c.codigo_paciente AS codigopaciente, " +
						  "c.estado_cita AS estadocita, " +
						  "getNombreEstadoCita(c.estado_cita) AS nombreestadocita,  " +
						  "c.unidad_consulta AS unidad_agenda, " +
						  "c.codigo_paciente AS codpaciente, " +
						  "getNombrePersona(c.codigo_paciente) AS nombrepaciente, " +
						  "per.tipo_identificacion AS tipoidpaciente, " +
						  "per.numero_identificacion AS numidentificacionpaciente, " +
						  "getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidadagenda, " +
						  "ag.fecha AS fecha_cita, " +
						  "ag.hora_inicio AS hora_cita, " +
						  "c.usuario AS usuario, " +
						  "ag.codigo_medico AS codigomedico, " +
						  "getnombrepersona(ag.codigo_medico) AS nombreprofesional, " +
						  "mult.consecutivo AS consecutivo_multa, " +
						  "mult.fecha_generacion AS fecha_multa, " +
						  "mult.hora_generacion AS hora_multa, " +
						  "mult.estado AS estado_multa, " +
						  "mult.valor AS valor_multa, " +
						  "mult.mot_anu_cond_multa  AS codigomotivomulta, " +
						  "mot.descripcion AS descripcionmotivo, " +
						  "ag.centro_atencion AS codcentroatencion, " +
						  "getNomCentroAtencion(ag.centro_atencion) AS nombrecentroatencion, " +
						  "c.convenio AS codconvenio, " +
						  "getNombreConvenio(c.convenio) AS nombreconvenio " +
						  "FROM multas_citas mult " +
						  "INNER JOIN cita c ON (mult.cita = c.codigo) " +
						  "INNER JOIN agenda ag ON (ag.codigo = c.codigo_agenda) " +
						  "INNER JOIN personas per ON (per.codigo = c.codigo_paciente) " +
						  "LEFT OUTER JOIN mot_anu_cond_multas mot ON (mot.consecutivo = mult.mot_anu_cond_multa) " +
						  "WHERE mult.estado = '"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"'  ";
						  
	
	private static String guardarCambiosMulta="UPDATE multas_citas " +
						   "SET estado = ?, " +
						   "mot_anu_cond_multa = ?, " +
						   "observaciones = ?, " +
						   "usuario_anu_cond = ?, " +
						   "fecha_anu_cond = CURRENT_DATE, " +
						   "hora_anu_cond = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
						   "WHERE consecutivo = ? ";
						  
						  
	/**
	 * Cadena Sql que realiza la consulta de los servicios asociados a una cita
	 */
	private static String consultaServiciosCitaStr="SELECT " +
						  "codigo_cita AS codcita, " +
						  "servicio  AS codservicio, " +
						  "getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups +") AS nombreservicio, " +
						  "getcodigopropservicio2(servicio,"+ConstantesBD.codigoTarifarioCups +") AS codigocups " +
						  "FROM servicios_cita " +
						  "WHERE codigo_cita = ? ";
	
	
	/**
	 * 
	 */
	private static String consultaUnidadesAgenada = "SELECT DISTINCT " +
													"unc.codigo as codigo, " +
													"unc.descripcion as nombre " +
													"FROM unidades_consulta unc " +
													"INNER JOIN  unid_agenda_usu_caten undagusu ON (undagusu.unidad_agenda = unc.codigo)" +
													"WHERE unc.activa = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "; 
	
						  
	
	/**
	 * Metodo para obtener un listado de Multas asociadas a un paciente devuelve un arreglo de DtoMultasCitas
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @param unidadesAgenda
	 * @return
	 */
	public static ArrayList<DtoMultasCitas> obtenerCitasconMultas(int codigoPersona, String loginUsuario, String unidadesAgenda, boolean conUnidadesAgenda) 
	{
		
		String consultaMultas=consultaMultasCita;
		String consultaServiciosCita=consultaServiciosCitaStr;
		ArrayList<DtoMultasCitas> arreglo=new ArrayList<DtoMultasCitas>();
		
		if(conUnidadesAgenda){
			consultaMultas+= "AND c.unidad_consulta in ( "+unidadesAgenda +" )";	
		}
		consultaMultas+= " ORDER BY mult.consecutivo ";
	
		Connection con = null;
		con = UtilidadBD.abrirConexion();
	    
			
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaMultas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPersona);
			ps.setString(2,loginUsuario);
	
			consultaMultas=consultaMultas.replace("c.codigo_paciente = ?", "c.codigo_paciente = "+codigoPersona);
			//consultaMultas=consultaMultas.replace("c.usuario = ?", "c.usuario = '"+loginUsuario+"'");
			consultaMultas=consultaMultas.replace("c.usuario = ?", "SELECT count(1) AS autorizado from consultaexterna.unid_agenda_usu_caten caten INNER JOIN consultaexterna.unid_agenda_act_auto auto ON(auto.unid_agenda_usu_caten=caten.codigo) WHERE usuario_autorizado='"+loginUsuario+"' AND actividad_autorizada IN("+ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas+", "+ConstantesBD.codigoActividadAutorizadaAnularMultasCitas+")");
			consultaMultas=consultaMultas.replace("usuario_autorizado=?", "usuario_autorizado="+loginUsuario);
			
			logger.info("\n\nCadena de consulta Multas Citas>> "+consultaMultas);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoMultasCitas dto=new DtoMultasCitas();
				dto.setCodigoMulta(rs.getInt("consecutivo_multa"));
				dto.setFechaMulta(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_multa")));
				dto.setEstadoMulta(rs.getString("estado_multa"));
				dto.setValor(rs.getInt("valor_multa"));
				dto.setUnidadAgenda(rs.getInt("unidad_agenda"));
				dto.setDescripcionUnidadAgenda(rs.getString("nombre_unidadagenda"));
				dto.setCita(rs.getInt("codigocita"));
				dto.setFechaCita(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_cita")));
				dto.setHoraCita(rs.getString("hora_cita"));
				dto.setEstadoCita(rs.getString("nombreestadocita"));
				dto.setProfesional(rs.getString("nombreprofesional"));
				dto.setCodProfesional(rs.getInt("codigomedico"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setCodCentroAtencion(rs.getInt("codcentroatencion"));
				dto.setNombreCentroAtencion(rs.getString("nombrecentroatencion"));
				dto.setCodMotivo_Cond_anul(rs.getInt("codigomotivomulta"));
				dto.setDescripcionMotivo_Cond_Anul(rs.getString("descripcionmotivo"));
			    dto.setCodConvenioCita(rs.getInt("codconvenio"));
			    dto.setConvenioCita(rs.getString("nombreconvenio"));
			    dto.setCondonar(UtilidadesConsultaExterna.esActividadAurtorizada(con, rs.getInt("unidad_agenda"), ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas, rs.getString("usuario"), rs.getInt("codcentroatencion"), true));
				dto.setAnular(UtilidadesConsultaExterna.esActividadAurtorizada(con, rs.getInt("unidad_agenda"), ConstantesBD.codigoActividadAutorizadaAnularMultasCitas, rs.getString("usuario"), rs.getInt("codcentroatencion"), true));
			    
				try{
				  PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(consultaServiciosCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				  ps2.setInt(1,dto.getCita());
				  ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
				  dto.setServiciosCita(UtilidadBD.cargarValueObject(rs2, true, false));	
				  ps2.close();
				  rs2.close();
				}
				catch (Exception e) {			
					e.printStackTrace();
				   logger.info("error en consulta Servicios Cita >>  cadena >> "+consultaServiciosCita+" ");
					
				}		
				
				arreglo.add(dto);
			}
			  ps.close();
			  
			  UtilidadBD.cerrarConexion(con);
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("error en  consulta Multas Citas >>  cadena >> "+consultaMultas+" ");
			
		}
		
		return arreglo;
	}
	
	
	/**
	 * Metodo para guardar los cambios a una multa 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap guardarMultaCita(Connection con, HashMap parametros)
	{
	
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		resultado.put("consecutivomulta",ConstantesBD.codigoNuncaValido+"");
		resultado.put("error",errores);	
		  
		String cadenaGuardar = guardarCambiosMulta;
		
		try
		{	
			logger.info("observaciones-->"+parametros.get("observaciones"));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaGuardar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1,parametros.get("estadomulta").toString());
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("motivomulta").toString()));
			ps.setString(3,parametros.get("observaciones").toString());
			ps.setString(4,parametros.get("usuario").toString());
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("consecutivomulta").toString()));
			
			if(ps.executeUpdate()>0)
			{
				resultado.put("consecutivomulta",1);
				
			}
			ps.close();
			
		}
		catch (Exception e) {			
			e.printStackTrace();
		   logger.info("error Guardando los datos de la Multa >>  cadena >> "+cadenaGuardar+" ");
			
		}
		
		return resultado;
	}
	
	
	/**
	 * Metodo para realizar la busqueda por Rango 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static  ArrayList<DtoMultasCitas> busquedaPorRangoMultasCita( String usuario, HashMap parametros)
	{
		ArrayList<DtoMultasCitas> multasCitas=new ArrayList<DtoMultasCitas>();
		String cadena = consultaMultasCita2;
		String consultaServiciosCita=consultaServiciosCitaStr;
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		if(parametros.containsKey("fechaInicialGeneracion") && 
				parametros.containsKey("fechaFinalGeneracion") && 
					!parametros.get("fechaInicialGeneracion").toString().equals("") && 
						!parametros.get("fechaFinalGeneracion").toString().equals(""))
		    {
			cadena += " AND mult.fecha_generacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialGeneracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalGeneracion").toString())+"' ";
			
		    }
		
		if(parametros.containsKey("convenio") && !parametros.get("convenio").toString().equals(""))
		  {
			cadena += " AND c.convenio = "+parametros.get("convenio").toString()+" ";
		  }
		
		if(parametros.containsKey("centroatencion") && !parametros.get("centroatencion").toString().equals(""))
		  {
			if(parametros.get("centroatencion").toString().equals("*"))
			{	
			  HashMap centrosAtencion = (HashMap)parametros.get("mapacentrosatencion");
			  String cadenaCentrosAtencion=new String("");
			  
			  for(int i=0; i<Utilidades.convertirAEntero(centrosAtencion.get("numRegistros")+""); i++)
			     {
				   cadenaCentrosAtencion += centrosAtencion.get("consecutivo_"+i)+", ";																	
			     } 
			    
			    cadenaCentrosAtencion +=ConstantesBD.codigoNuncaValido+"";
			    String aux[]= cadenaCentrosAtencion.split(", "+ConstantesBD.codigoNuncaValido);
			    cadenaCentrosAtencion = aux[0];
			    cadena += " AND ag.centro_atencion IN  ("+cadenaCentrosAtencion+")";

			}else
			{
			 cadena += " AND ag.centro_atencion = "+parametros.get("centroatencion").toString()+" ";
			}
		  }
		
		if(parametros.containsKey("unidadagenda") && !parametros.get("unidadagenda").toString().equals(""))
		  {
			if(parametros.get("unidadagenda").toString().equals("*"))
			{
			  HashMap unidadesAgenda = (HashMap)parametros.get("mapaunidadesagenda");
			  String unidadesagenda=new String("");
			 
			 for(int i=0; i<Utilidades.convertirAEntero(unidadesAgenda.get("numRegistros")+""); i++)
		     {
				 unidadesagenda += unidadesAgenda.get("codigo_"+i)+", ";																	
		     } 
		    
			  unidadesagenda +=ConstantesBD.codigoNuncaValido+"";
		      String aux[]= unidadesagenda.split(", "+ConstantesBD.codigoNuncaValido);
		      unidadesagenda = aux[0];
		    
		    cadena+=" AND c.unidad_consulta IN ("+unidadesagenda+")";
			  
			}
			else
			{
				cadena += " AND c.unidad_consulta = "+parametros.get("unidadagenda").toString()+" ";
			}
		  }
		
		if(parametros.containsKey("profesional") && !parametros.get("profesional").toString().equals(""))
		{
			cadena += " AND ag.codigo_medico = "+parametros.get("profesional").toString()+" ";
		}
		
		if(parametros.containsKey("estadocita") && !parametros.get("estadocita").toString().equals(""))
		 {
			cadena += " AND c.estado_cita = "+parametros.get("estadocita").toString()+" ";
		 }
		
		  cadena += " AND (" +
		  					"SELECT count(1) AS autorizado from consultaexterna.unid_agenda_usu_caten caten INNER JOIN consultaexterna.unid_agenda_act_auto auto ON(auto.unid_agenda_usu_caten=caten.codigo) WHERE usuario_autorizado='"+usuario+"' AND actividad_autorizada IN("+ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas+", "+ConstantesBD.codigoActividadAutorizadaAnularMultasCitas+")" +
		  				") > 0  ORDER BY mult.consecutivo ";
		 
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
		
			logger.info("\n\nCadena de consulta Multas Citas Rango>> "+cadena);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		
		
		while(rs.next())
		{
			DtoMultasCitas dto=new DtoMultasCitas();
			dto.setCodigoMulta(rs.getInt("consecutivo_multa"));
			dto.setFechaMulta(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_multa")));
			dto.setEstadoMulta(rs.getString("estado_multa"));
			dto.setValor(rs.getInt("valor_multa"));
			dto.setIdPaciente(rs.getString("tipoidpaciente")+" "+rs.getString("numidentificacionpaciente") );
			dto.setNombrePaciente(rs.getString("nombrepaciente"));
			dto.setUnidadAgenda(rs.getInt("unidad_agenda"));
			dto.setDescripcionUnidadAgenda(rs.getString("nombre_unidadagenda"));
			dto.setCita(rs.getInt("codigocita"));
			dto.setFechaCita(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_cita")));
			dto.setHoraCita(rs.getString("hora_cita"));
			dto.setEstadoCita(rs.getString("nombreestadocita"));
			dto.setProfesional(rs.getString("nombreprofesional"));
			dto.setCodProfesional(rs.getInt("codigomedico"));
			dto.setUsuario(rs.getString("usuario"));
			dto.setCodCentroAtencion(rs.getInt("codcentroatencion"));
			dto.setNombreCentroAtencion(rs.getString("nombrecentroatencion"));
			dto.setCodMotivo_Cond_anul(rs.getInt("codigomotivomulta"));
			dto.setDescripcionMotivo_Cond_Anul(rs.getString("descripcionmotivo"));
		    dto.setCodConvenioCita(rs.getInt("codconvenio"));
		    dto.setConvenioCita(rs.getString("nombreconvenio"));
		    dto.setCondonar(UtilidadesConsultaExterna.esActividadAurtorizada(con, rs.getInt("unidad_agenda"), ConstantesBD.codigoActividadAutorizadaCondonarMultasCitas, rs.getString("usuario"), rs.getInt("codcentroatencion"), true));
			dto.setAnular(UtilidadesConsultaExterna.esActividadAurtorizada(con, rs.getInt("unidad_agenda"), ConstantesBD.codigoActividadAutorizadaAnularMultasCitas, rs.getString("usuario"), rs.getInt("codcentroatencion"), true));
		    
			try{
			  PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(consultaServiciosCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			  ps2.setInt(1,dto.getCita());
			  ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
			  dto.setServiciosCita(UtilidadBD.cargarValueObject(rs2, true, false));	
			  ps2.close();
			  rs2.close();
			}
			catch (Exception e) {			
				e.printStackTrace();
			   logger.info("error en consulta Servicios Cita Rango>>  cadena >> "+consultaServiciosCita+" ");
				
			}
			
			
			multasCitas.add(dto);
		}
		  ps.close();
		  
		  UtilidadBD.cerrarConexion(con);
	}
	catch (Exception e) {			
		e.printStackTrace();
		logger.info("error en  consulta Multas Citas Rango>>  cadena >> "+cadena+" ");
		
	}
		
	return multasCitas;
		
		
	}
	

	/**
	 * Metodo para consultar las unidades Agenda asociados a uno o varios centros de atencion
	 * @param con
	 * @param centrosAtencion
	 * @return
	 */
	public static HashMap unidadesAgendaCentrosAtencion(HashMap centrosAtencion)
	{
	   Connection con = null;
	   con = UtilidadBD.abrirConexion();	 
	   String cadenaCentrosAtencion=new String("");
	   HashMap unidadesAgenda=new HashMap();
	   
	   String cadena = consultaUnidadesAgenada ;

	   
	    for(int i=0; i<Utilidades.convertirAEntero(centrosAtencion.get("numRegistros")+""); i++)
	     {
		   cadenaCentrosAtencion += centrosAtencion.get("consecutivo_"+i)+", ";																	
	     } 
	    
	    cadenaCentrosAtencion +=ConstantesBD.codigoNuncaValido+"";
	    String aux[]= cadenaCentrosAtencion.split(", "+ConstantesBD.codigoNuncaValido);
	    cadenaCentrosAtencion = aux[0];
	    
	    cadena+="AND centro_atencion IN ("+cadenaCentrosAtencion+") ";
	    cadena+= " ORDER BY nombre ASC ";
	    
	    logger.info("Consulta Unidades Agenda Centros de Atencion >>  cadena >> "+cadena+" ");
	    try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 	ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		 	unidadesAgenda= UtilidadBD.cargarValueObject(rs, true, false);
		 	ps.close();
			rs.close();
		 	UtilidadBD.cerrarConexion(con);
		 	return unidadesAgenda;
		}
		catch (Exception e) {			
				e.printStackTrace();
				
			   logger.info("error Consultando Unidades Agenda >>  cadena >> "+cadena+" ");
				
			}
	    
	   
	   return null;
	   
	}
	
	
	/**
	 * 
	 * @param centroAtencion
	 * @return
	 */
	 public static HashMap unidadesAgendaXCentroAtencion(int centroAtencion)
	 {
		 Connection con = null;
		   con = UtilidadBD.abrirConexion();	 
		   HashMap unidadesAgenda=new HashMap();
		   
		   String cadena = consultaUnidadesAgenada ;

		    
		    cadena+="AND centro_atencion = "+ centroAtencion ;
		    cadena+= " ORDER BY nombre ASC ";
		    
		    logger.info("Consulta Unidades Agenda Centros de Atencion >>  cadena >> "+cadena+" ");
		    try
			{	
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 	ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			 	unidadesAgenda= UtilidadBD.cargarValueObject(rs, true, false);
			 	ps.close();
				rs.close();
			 	UtilidadBD.cerrarConexion(con);
			 	return unidadesAgenda;
			}
			catch (Exception e) {			
					e.printStackTrace();
					
				   logger.info("error Consultando Unidades Agenda >>  cadena >> "+cadena+" ");
					
				}
		 
		 return null;
	 }
		
}
