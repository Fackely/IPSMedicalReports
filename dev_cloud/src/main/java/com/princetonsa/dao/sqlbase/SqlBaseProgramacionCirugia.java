/*
 * Creado el 18-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class SqlBaseProgramacionCirugia{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBasePreanestesiaDao.class);

	
	/**
	 * Metodo para retornar la información de peticion de cirugia 
	 * @param con
	 * @param nroPeticion
	 * @param tipoConsulta 
	 * @return
	 */
	public static Collection cargarInformacionPeticion(Connection con, int nroPeticion, int tipoConsulta)
	{
		String consulta = "";
		
		if (tipoConsulta == 0) 
		{
			   consulta = "    SELECT " +
			   			  "	   to_char(pqx.fecha_peticion, 'DD/MM/YYYY')  as fecha_peticion, " +
						  "    CASE WHEN to_char(fecha_cirugia, 'DD/MM/YYYY') IS NULL THEN '' ELSE to_char(fecha_cirugia, 'DD/MM/YYYY') END AS fecha_cirugia, " +						  
						  "	   getNombrePersona(pqx.solicitante) as solicitante, " +
						  "	   pqx.duracion as duracion ," +
						  "	   pqx.estado_peticion AS estado," +
						  "	   pqx.hora_peticion AS hora_peticion " +
						  "	   FROM peticion_qx pqx 	  " +						  
						  "	   WHERE pqx.codigo = ?	" +
						  "    AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ";
		}
		if (tipoConsulta == 1) 
		{
			   consulta = " SELECT coalesce(vp.tipo_anestesia,pqx.tipo_anestesia) as anestesia 	 	                                      	" + 
			   			  "		   FROM peticion_qx pqx 														 " +
			   			  "				LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )  " + 
			   			  "				 	 WHERE pqx.codigo = ?										  "+
			   			  " 	    		   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ";
		}
	    
		logger.info("Consulta informacion peticion=> "+consulta.replace("?", nroPeticion+""));
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, nroPeticion);
			
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarInformacionPeticion )  : "+e.toString());
			return null;
		}
	}


	/**
	 * Metodo para retornar la información de las cirugias asociadas... 
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public static Collection cargarInformacionCirugias(Connection con, int nroPeticion) 
	{
		String consulta = "";

		try
		{
			int[] solicitudYPeticion=UtilidadValidacion.estaPeticionAsociada(con, nroPeticion);
			
			//--No existe una peticion asociada a una orden
			if (solicitudYPeticion[1] == 0)  
			{
				//-Se debe retornar los servicios de la peticion
				consulta = "  	   SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
						   "				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad  "  +
						   "				  FROM peticion_qx pqx " +
						   "					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						   "					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						   "					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						   "					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						   "							 WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
						   "							   AND pqx.codigo = " + nroPeticion+
						   " 	    		 			   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ";
			}
			else
			{
				//-Se debe retornar el detalle de la cirugia  
				consulta = "  	   SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
						   "				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad  "  +
						   "				  FROM peticion_qx pqx " +
						   "					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						   "					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						   "					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						   "					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						   "							 WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
						   "							   AND pqx.codigo = " + nroPeticion +
						   " 	    		 			   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ";
			}
			
			
			
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psConsulta.setInt(1, nroPeticion);
		
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarInformacionPeticion )  : "+e.toString());
			return null;
		}
	} 


	/**
	 * Metodo para cargar el codigo y el nombre de las salas 
	 * @param con
	 * @return
	 */
	public static Collection cargarSalas(Connection con) 
	{
		String consulta = " SELECT sal.consecutivo as consecutivo, sal.codigo as codigo, sal.descripcion as nombre_sala " +
 		       			  "		   FROM salas sal " +
						  "				INNER JOIN tipos_salas tsal ON ( tsal.codigo = sal.tipo_sala ) " +
			 	      	  " 					   WHERE tsal.es_quirurgica = " + ValoresPorDefecto.getValorTrueParaConsultas() +
					  	  "							 AND sal.activo =  "  + ValoresPorDefecto.getValorTrueParaConsultas() + 			
						  "								 ORDER BY  sal.consecutivo	";
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarSalas )  : "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Metodo para listar las peticiones por paciente especifico con estado pendiente
	 * @param con
	 * @param centroAtencion 
	 * @param codigoCuenta 
	 * @return
	 */
	 public static Collection cargarListadoPeticionesPaciente(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
	 {
	 	String consulta = "";
	 	
	 		if (codigoPersona == -1) //-Listar las peticiones generales
	 		{
	 		 
	 		   //---(A) Se deben listar las peticiones de centro de atencion del usuario. (Con cuenta Abierta).
	 		   //--- Adicionalmente se debe listar los pacientes que no tienen cuenta abierta independientemente del centro de Atecion. 
	 		
	 		   //-(A)	
	 		   consulta = "SELECT * FROM (" +
	 		   			  "SELECT pqx.codigo as codigo, to_char(pqx.fecha_peticion, 'YYYY-MM-DD')  as fecha_peticion," +
			   			  " 	  CASE WHEN to_char(fecha_cirugia, 'YYYY-MM-DD') IS NULL THEN '' ELSE to_char(fecha_cirugia, 'YYYY-MM-DD') END AS fecha_cirugia, " +	 		  				  	   	  
				  	   	  "		  getNombrePersona(pqx.solicitante) as medico, " +
						  "		  per.tipo_identificacion as tipo_id,  per.numero_identificacion as id, " +
						  "       per.primer_apellido || ' ' || per.segundo_apellido ||' '|| per.primer_nombre ||' '|| per.segundo_nombre as paciente," +
						  "		  CASE WHEN tp.descripcion IS NULL THEN coalesce(getnombretipoanestesia(pqx.tipo_anestesia),'') ELSE tp.descripcion END AS tipo_anestesia,	" +
						  "		  ca.descripcion as centro_atencion	  " +
			  			  "		  FROM peticion_qx pqx " +
			  			  "     	   INNER JOIN personas per ON ( per.codigo = pqx.paciente ) " + 																			   
			  			  "     	   INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion ) " + 																			   
			  			  "     	   LEFT OUTER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo " +
                          "										  AND cu.estado_cuenta NOT IN (" + ConstantesBD.codigoEstadoCuentaFacturada + 			 "," + 
																								   + ConstantesBD.codigoEstadoCuentaCerrada +  			 "," +
																								   + ConstantesBD.codigoEstadoCuentaExcenta + 
																						    " ) " +
			  			  "										  ) " + 																			   
			 	      	  " 		   LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
		       			  "			   LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   "  +
						  "    	    	  	  WHERE pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +
						  "						AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+		
						  " 				    AND pqx.codigo NOT IN (SELECT sc.codigo_peticion as codigo FROM solicitudes_cirugia sc " + 
						  "													  INNER JOIN peticion_qx pqx ON ( pqx.codigo = sc.codigo_peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"') " +
						  "														   WHERE pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +  
						  //--------------------La parte del IN se coloco para que una vez registrada informacion sobre 
						  //--------------------Hoja Quirugica y/o Hoja de Anestesia y/o notas de recuperación y/o notas generales de enfermeria. no apareciera en el listado de
						  //--------------------petiociones en estado pendiente.
						  "															 AND sc.numero_solicitud IN ( " +
                          "																						  SELECT numero_solicitud FROM hoja_anestesia " +
                          "															 							  UNION   " +
                          "															 							  SELECT numero_solicitud FROM hoja_quirurgica  " +
                          "															 							  UNION   " +
                          "															 							  SELECT numero_solicitud FROM notas_enfermeria  " +
                          "															 							  UNION   " +
                          "															 							  SELECT numero_solicitud FROM notas_recuperacion_general  " +
                          "															 							  ) 	)  " +
                          "						AND ca.consecutivo = " + centroAtencion +
                          "  UNION  " +
           	 		   	  "	 SELECT pqx.codigo as codigo, to_char(pqx.fecha_peticion, 'YYYY-MM-DD')  as fecha_peticion," +
			   			  " 	  CASE WHEN to_char(fecha_cirugia, 'YYYY-MM-DD') IS NULL THEN '' ELSE to_char(fecha_cirugia, 'YYYY-MM-DD') END AS fecha_cirugia, " +	 		  				  	   	  
				  	   	  "		  getNombrePersona(pqx.solicitante) as medico, " +
						  "		  per.tipo_identificacion as tipo_id,  per.numero_identificacion as id, " +
						  "       per.primer_apellido || ' ' || per.segundo_apellido ||' '|| per.primer_nombre ||' '|| per.segundo_nombre as paciente," +
						  "		  CASE WHEN tp.descripcion IS NULL THEN coalesce(getnombretipoanestesia(pqx.tipo_anestesia),'') ELSE tp.descripcion END AS tipo_anestesia,	" +
						  "		  ca.descripcion as centro_atencion	  " +
			  			  "		  FROM peticion_qx pqx " +
			  			  "     	   INNER JOIN personas per ON ( per.codigo = pqx.paciente ) " + 																			   
			  			  "     	   INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion ) " + 																			   
			  			  "     	   LEFT OUTER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo " +
                          "										  AND cu.estado_cuenta NOT IN (" + ConstantesBD.codigoEstadoCuentaFacturada + 			 "," +
																								   + ConstantesBD.codigoEstadoCuentaCerrada +  			 "," +
																								   + ConstantesBD.codigoEstadoCuentaExcenta + 
																						    " ) " +
			  			  "										  ) " + 																			   
			 	      	  " 		   LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
		       			  "			   LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   "  +
						  "    	    	  	  WHERE pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +
						  "						AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+	
						  " 				    AND pqx.codigo NOT IN (SELECT sc.codigo_peticion as codigo FROM solicitudes_cirugia sc " + 
						  "													  INNER JOIN peticion_qx pqx ON ( pqx.codigo = sc.codigo_peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"') " +
						  "														   WHERE pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +  
						  //--------------------La parte del IN se coloco para que una vez registrada informacion sobre 
						  //--------------------Hoja Quirugica y/o Hoja de Anestesia y/o notas de recuperación y/o notas generales de enfermeria. no apareciera en el listado de
						  //--------------------petiociones en estado pendiente.
						  "															 AND sc.numero_solicitud IN ( " +
	                      "																						  SELECT numero_solicitud FROM hoja_anestesia " +
	                      "															 							  UNION   " +
	                      "															 							  SELECT numero_solicitud FROM hoja_quirurgica  " +
	                      "															 							  UNION   " +
	                      "															 							  SELECT numero_solicitud FROM notas_enfermeria  " +
	                      "															 							  UNION   " +
	                      "															 							  SELECT numero_solicitud FROM notas_recuperacion_general  " +
	                      "															 							  ) 	)  " +
						   "	) x ORDER BY x.fecha_peticion ";
	 		   
	 		   
	 		   
	 		}
	 		else
	 		{

	 		String cad = "";
            if (codigoCuenta > 0)
            {
            	cad = " AND ca.consecutivo = " + centroAtencion;  
            }

	 		consulta = " SELECT pqx.codigo as codigo, to_char(pqx.fecha_peticion, 'YYYY-MM-DD')  as fecha_peticion, 						" +
					  " 	           CASE WHEN to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') IS NULL THEN '' ELSE to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') END AS fecha_cirugia, " +
					  "				   getNombrePersona(pqx.solicitante) as medico, " +
					  "				   CASE WHEN tp.descripcion IS NULL THEN coalesce(getnombretipoanestesia(pqx.tipo_anestesia),'') ELSE tp.descripcion END AS tipo_anestesia," +
					  "				   ca.descripcion as centro_atencion	  " +
	       			  "		   		   FROM peticion_qx pqx			 				" +
		  			  "     	  			INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )		" + 																			   
		  			  " 				    LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
	       			  "						LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   		"  +
		 	      	  " 					WHERE pqx.paciente = " + codigoPersona +	 	      	  
		 	      	  "				 		  AND pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +
		 	      	  "						  AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+	
					  //--------------------La parte del IN se coloco para que una vez registrada informacion sobre 
					  //--------------------Hoja Quirugica y/o Hoja de Anestesia y/o notas de recuperación y/o notas generales de enfermeria. no apareciera en el listado de
					  //--------------------petiociones en estado pendiente.
					  " 					  AND pqx.codigo NOT IN (SELECT sc.codigo_peticion as codigo FROM solicitudes_cirugia sc " + 
					  "														 INNER JOIN peticion_qx pqx ON ( pqx.codigo = sc.codigo_peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ) " +
					  "															  WHERE pqx.paciente = " + codigoPersona + " " +
					  "																AND pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente +             
					  "																AND sc.numero_solicitud IN ( " +
                      "																						  SELECT numero_solicitud FROM hoja_anestesia " +
                      "															 							  UNION   " +
                      "															 							  SELECT numero_solicitud FROM hoja_quirurgica  " +
                      "															 							  UNION   " +
                      "															 							  SELECT numero_solicitud FROM notas_enfermeria  " +
                      "															 							  UNION   " +
                      "															 							  SELECT numero_solicitud FROM notas_recuperacion_general  " +
                      "															 							  ) 	)  " +
                      "						" + cad + 
					  "							  ORDER BY pqx.fecha_peticion ";
		 		   
		 		   
	 		}
	 		
	 		
	 	logger.info("Consulta de las peticiones => "+consulta);
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarListadoPeticionesPaciente )  : "+e.toString());
			return null;
		}
	}


	/**
	 * Consultar los servicios asociadas a las peticiones...
	 * @param con
	 * @param codigoPersona
	 * @param centroAtencion 
	 * @param codigoCuenta 
	 * @return
	 */
	public static Collection cargarInformacionServiciosPeticion(Connection con, int codigoPersona, int centroAtencion, int codigoCuenta)
	{
		StringBuffer consulta = new StringBuffer();
		
		 /*consulta.append(" SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups," +
		 				 "		  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad		" +
						 "		  FROM peticion_qx pqx 																						" +
			  			 "     	       INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )							" + 																			   
			  			 "			   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) 							" + 
						 "			   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  											" +
						 "			   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 								" +
						 "			   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) 									" +
						 "					WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups);
			*/

		String estado = "";  
		String codPersona = "";  
		
	 	//-- Consultar todas las peticiones de un paciente especifico.
	 	if ( codigoPersona > 0  ) 
		{
	 		codPersona = " AND pqx.paciente = " + codigoPersona;
		}
		if ( codigoPersona == -2 )
		{
			consulta.append(" SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups," +
			 				 "		  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad		" +
							 "		  FROM peticion_qx pqx 																						" +
				  			 "     	       INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )							" + 																			   
				  			 "			   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) 							" + 
							 "			   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  											" +
							 "			   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 								" +
							 "			   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) 									" +
							 "					WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
							 "					  AND ( pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +      
							 " 		 				  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + ") " +
							 "					  AND pqx.programable = '"+ConstantesBD.acronimoSi+"'");
		}
		else
		{
		    estado = " AND pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente; 
		}
		
		if ( codigoPersona == -1  )  //-Consulta general  
		{
		consulta.append( "  SELECT * FROM ( " +
						 "	SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
						 "		  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad,	" +
						 "		  pqx.fecha_peticion as fecha_peticion																		" +
						 "		  FROM peticion_qx pqx 																						" +
			  			 "     	       INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )							" + 																			   
			  			 "			   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) 							" + 
						 "			   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  											" +
						 "			   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 								" +
						 "			   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) 									" +
			 		     "     	       INNER JOIN personas per ON ( per.codigo = pqx.paciente ) 											" + 																			   
			  			 " 	    	   LEFT OUTER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo " +
                         "					  AND cu.estado_cuenta NOT IN ("  + ConstantesBD.codigoEstadoCuentaFacturada +  "," + 
																			  + ConstantesBD.codigoEstadoCuentaCerrada +  "," +
																			  + ConstantesBD.codigoEstadoCuentaExcenta + 
					     "												  )  ) " + 																			   
						 "					WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
						 "						" + estado + codPersona +
						 " 				      AND pqx.programable = '"+ConstantesBD.acronimoSi+"'" +
						 "			  		  AND pqx.codigo NOT IN (SELECT sc.codigo_peticion as codigo FROM solicitudes_cirugia sc " + 
						 "												  INNER JOIN peticion_qx pqx ON ( pqx.codigo = sc.codigo_peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ) " +
						 "													   WHERE sc.numero_solicitud IN ( " +
                         "																						  SELECT numero_solicitud FROM hoja_anestesia " +
                         "															 							  UNION   " +
                         "															 							  SELECT numero_solicitud FROM hoja_quirurgica  " +
                         "															 							  UNION   " +
                         "															 							  SELECT numero_solicitud FROM notas_enfermeria  " +
                         "															 							  UNION   " +
                         "															 							  SELECT numero_solicitud FROM notas_recuperacion_general  " +
                         "															 							  ) )	  " +
					     "					  AND ca.consecutivo = " + centroAtencion +
		 				 " UNION  																										" +
		 				 " SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups,  " +				
		 				 "		  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad,	" +
		 				 "		  pqx.fecha_peticion as fecha_peticion																		" +
						 "		  FROM peticion_qx pqx 																						" +
						 "     	       INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )							" + 																			   
						 "			   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) 							" + 
						 "			   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  											" +
						 "			   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 								" +
						 "			   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) 									" +
			 		     "     	       INNER JOIN personas per ON ( per.codigo = pqx.paciente ) 											" + 																			   
			  			 " 	    	   LEFT OUTER JOIN cuentas cu ON ( cu.codigo_paciente = per.codigo " +
                         "					  AND cu.estado_cuenta NOT IN ("  + ConstantesBD.codigoEstadoCuentaFacturada +  "," + 
																			  + ConstantesBD.codigoEstadoCuentaCerrada +  "," +
																			  + ConstantesBD.codigoEstadoCuentaExcenta + 
					     "												  )  ) " + 																			   
					     "					WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups + 
						 "						" + estado + codPersona +
						 " 				      AND pqx.programable = '"+ConstantesBD.acronimoSi+"'" +
						 "				      AND pqx.codigo NOT IN (SELECT sc.codigo_peticion as codigo FROM solicitudes_cirugia sc " + 
						 "												  INNER JOIN peticion_qx pqx ON ( pqx.codigo = sc.codigo_peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"') " +
						 "													   WHERE sc.numero_solicitud IN ( " +
				         "																						  SELECT numero_solicitud FROM hoja_anestesia " +
				         "															 							  UNION   " +
				         "															 							  SELECT numero_solicitud FROM hoja_quirurgica  " +
				         "															 							  UNION   " +
				         "															 							  SELECT numero_solicitud FROM notas_enfermeria  " +
				         "															 							  UNION   " +
				         "															 							  SELECT numero_solicitud FROM notas_recuperacion_general  " +
				         "															 							  ) )	  " +
				         "	) x ORDER BY x.numero_servicio, x.fecha_peticion  ");
		}
		
		if ( codigoPersona > 0 )  //--- Consulta por paciente
		{
		
			String cod = "";
	        if (codigoCuenta > 0) 
            {
            	cod = " AND ca.consecutivo = " + centroAtencion;  
            }
			
			 consulta.append(" SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups," +
							 "		  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad		" +
							 "		  FROM peticion_qx pqx 																						" +
				 			 "     	       INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )							" + 																			   
				 			 "			   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) 							" + 
							 "			   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  											" +
							 "			   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 								" +
							 "			   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) 									" +
							 "					WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups+ 
							 "	 			    AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
							 "  				"+ 	estado + codPersona + cod );
		}
		
		
		
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarInformacionServiciosPeticion )  : "+e.toString());
			return null;
		}
	}

	
	/**
	 * Metodo para cargar listado de servicios para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap cargarServiciosPeticion(Connection con, int codigoPersona) 
	{
		String codPac = "";
		StringBuffer consulta = new StringBuffer();

		if ( codigoPersona > 0  ) //-Consultar todas las peticiones de un paciente especifico
		{
		    codPac = "	AND pqx.paciente = " + codigoPersona;
		}
		
		
		 consulta.append("	SELECT * FROM (  " +
		 				 "	   SELECT pqx.codigo as codigo_peticion_servicio, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups,  " +
		 				 "				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad_servicio	" +
						 "				  FROM peticion_qx pqx " +
						 "					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						 "					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						 "					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						 "					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						 "							 WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
		 				 "							   AND  (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +      
		 				 " 									  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + ") " +
		 				 "  						   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
					     "							   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
					     "										  		 		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
					     "										   				 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
					     "																 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +
					     "														   		 AND pqx.paciente = "+ codigoPersona + "" +
			     		 " 																 AND pqx.programable = '"+ConstantesBD.acronimoSi+"') " + codPac +  
	     				 "	UNION ALL											" +  
	     				 "	SELECT pqx.codigo as codigo_peticion_servicio, scps.consecutivo as numero_servicio, rser.codigo_propietario as cups,  " +   
	     				 "		   rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad_servicio " + 	 
	     				 "		   FROM peticion_qx pqx  																						  " +	
	     				 "		  		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )  								 	  " +
	     				 "		   		INNER JOIN sol_cirugia_por_servicio scps ON ( sc.numero_solicitud = scps.numero_solicitud )  				  " +
	     				 "		  		INNER JOIN servicios ser ON ( ser.codigo = scps.servicio )   								 				  " +
	     				 "		   		INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) 										  " +
	     				 "		   		INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  )  								 		  " +
	     				 "		   			 WHERE rser.tipo_tarifario =   " + ConstantesBD.codigoTarifarioCups +  
	     				 " 					   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
	     				 "		   			   AND ( pqx.estado_peticion =  " + ConstantesBD.codigoEstadoPeticionProgramada  + " OR pqx.estado_peticion =  " + ConstantesBD.codigoEstadoPeticionReprogramada + " )" +
	     				 "  " + codPac + "  ) x ORDER BY x.numero_servicio "  );

				    
		try
		{
			PreparedStatementDecorator st =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery()));
			st.close();
			return mapaRetorno;
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarServiciosPeticion )  : "+e.toString());
			return null;
		}
	}	


	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public static Collection cargarListadoPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita)
	{
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("SELECT " +
				"getnomcentroatencion(pqx.centro_atencion) As centro_atencion, "+
				"	pqx.codigo as codigo, to_char(pqx.fecha_peticion, 'DD/MM/YYYY')  as fecha_peticion," +
		  	   	  		"		to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') as fecha_cirugia, getNombrePersona(pqx.solicitante) as medico, " +
						"		per.tipo_identificacion as tipo_id, per.numero_identificacion as id, " +
						"       per.primer_apellido || ' ' || per.segundo_apellido ||' '|| per.primer_nombre ||' '|| per.segundo_nombre as paciente," +
						"		CASE WHEN tp.descripcion IS NULL THEN '' ELSE tp.descripcion END AS tipo_anestesia     " +
						"		FROM peticion_qx pqx " +
			 	      	" 			 LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
		       			"			 LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   "  +
						"     	     INNER JOIN personas per ON ( per.codigo = pqx.paciente ) " + 																			   
						"    	     WHERE pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionPendiente+
						"	   	       AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ");

		try
		{
			//-Parametros de consulta
			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}

			//-Si se esta buscando por fechas de cirugia se debe adicionar a la 
			//-consulta la condicion de que se busque tambien cuando las peticiones no tienen
			//-fecha de cirugia.
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
			}
			
			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND pqx.solicitante = " + profesionalSolicita);
			}
			
			//-Ordenar por fecha de peticion .... 
			consulta.append(" ORDER BY pqx.fecha_peticion");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarListadoPeticionesBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
	}

	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @param centroAtencion 
	 * @return
	 */
	public static HashMap cargarListadoConsultaPeticionesBusqueda(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita, int centroAtencion, StringBuffer consulta)
	{
		  HashMap mapa = new HashMap();
		  
		  StringBuffer consultaC = new StringBuffer();
		  StringBuffer consultaA = new StringBuffer();
		  StringBuffer consultaO = new StringBuffer();
		
		  				  
				  
			//----La informacion de los cirujanos.
			consultaC.append(" SELECT * FROM ( " +
							 "	SELECT pqx.codigo as codigo_peticion_cirujano, " +
							 "		   pqx.fecha_peticion as fecha_peticion_c, pqx.hora_peticion as hora_peticion_c, psqx.fecha_cirugia as fecha_cirugia_c, pqx.solicitante as solicitante_c,  " + 
							 "         getNombrePersona(ppqx.codigo_medico) as cirujano, ca.consecutivo as centro_atencion_c  									" +
							 "		   FROM peticion_qx pqx																	" +
							 "		   		INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
							 "		   		INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
							 " 			    INNER JOIN prof_partici_peticion_qx ppqx ON ( ppqx.peticion_qx = pqx.codigo ) 	" +     
							 "			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = ppqx.tipo_participante   ) " +
							 "    			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteCirujano + " ) " +
							 "			   				  WHERE (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
							 "									  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
					 		 "  				   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						     "					   AND pqx.codigo NOT IN  (  SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
						     "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo AND pqx1.programable = '"+ConstantesBD.acronimoSi+"' ) " + 
						     "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
						     "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +
						     "											  ) 																			" +
						     "  UNION ALL 																												" +	
						     "				 	SELECT pqx.codigo as codigo_peticion_cirujano,												 			" +
							 "		  			   pqx.fecha_peticion as fecha_peticion_c, pqx.hora_peticion as hora_peticion_c, psqx.fecha_cirugia as fecha_cirugia_c, pqx.solicitante as solicitante_c,   " + 
						     "		               getNombrePersona(opsc.codigo_profesional) as cirujano, ca.consecutivo as centro_atencion_c  									   			" +
						     "		    		   FROM peticion_qx pqx																					" +	
							 "		   					INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
							 "		   					INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
						     "		    		   		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )						" +
						     "		     			    INNER JOIN otros_prof_sol_cx opsc ON ( opsc.numero_solicitud = sc.numero_solicitud ) 	        " +
						     "		    			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = opsc.tipo_profesional   )    	" +
						     "		        			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteCirujano + "  ) " +    
						     "		    					 WHERE  (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +  
						     "		    							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +
				     		 " 								 AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						     " ) x WHERE 1=1  ");
			
			
			//----La informacion de los anestesiologos.
			consultaA.append(" SELECT * FROM ( " +
						   "	 SELECT pqx.codigo as codigo_peticion_anestesiologo, 									" + 
						   "		   pqx.fecha_peticion as fecha_peticion_a, pqx.hora_peticion as hora_peticion_a, psqx.fecha_cirugia as fecha_cirugia_a, pqx.solicitante as solicitante_a, " + 
						   "           getNombrePersona(ppqx.codigo_medico) as anestesiologo, ca.consecutivo as centro_atencion_a								" +
						   "		   FROM peticion_qx pqx																	" +
						   "				INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
						   "		 		INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
						   " 			    INNER JOIN prof_partici_peticion_qx ppqx ON ( ppqx.peticion_qx = pqx.codigo ) 	" +     
						   "			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = ppqx.tipo_participante   ) " +
						   "    			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteAnestesiologo + " ) " +
						   "			   				  WHERE (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
						   "									  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
				   		   " 					   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						   "					   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
						   "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
						   "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
						   "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " " +
				   		   " 													   ) " +
				   		   " 														 AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						   "											   ) " +
						   "  UNION ALL 																												" +	
						   "				SELECT pqx.codigo as codigo_peticion_anestesiologo,												 			" +
						   "		  			   pqx.fecha_peticion as fecha_peticion_a, pqx.hora_peticion as hora_peticion_a, psqx.fecha_cirugia as fecha_cirugia_a, pqx.solicitante as solicitante_a,  " + 
						   "		               getNombrePersona(opsc.codigo_profesional) as anestesiologo, ca.consecutivo as centro_atencion_a		" +
						   "		    		   FROM peticion_qx pqx																					" +	
						   "		   					INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
						   "		   					INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
						   "		    		   		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )						" +
						   "		     			    INNER JOIN otros_prof_sol_cx opsc ON ( opsc.numero_solicitud = sc.numero_solicitud ) 	        " +
						   "		    			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = opsc.tipo_profesional   )    	" +
						   "		        			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteAnestesiologo + "  ) " +    
						   "		    					 WHERE (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +  
						   "		    							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada +
						   " 									   )  " +
						   "  								 AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						   " ) x WHERE 1=1  ");

			
			//----La informacion de las observaciones de los servicios.
		  consultaO.append( " SELECT * FROM (" +
							" SELECT pqx.codigo as codigo_peticion_obser,  ps.observaciones as observaciones,	" +
							"	     pqx.fecha_peticion as fecha_peticion_o, " +
							"		 pqx.hora_peticion as hora_peticion_o, psqx.fecha_cirugia as fecha_cirugia_o,  pqx.solicitante as solicitante_o, " +
							"		 ca.consecutivo as centro_atencion_o	 " + 
							"		 FROM peticion_qx pqx														" +
							"			  INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
							"	 		  INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
							"			  INNER JOIN peticiones_servicio ps ON ( ps.peticion_qx = pqx.codigo )  " +
						    "			   				  WHERE (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
							"									  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
							"                      AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " + 
						    "					   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
						    "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
						    "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
						    "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + 
						    "													   )  AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						    "											   )   " +
						    " UNION ALL																							" +
						    " SELECT pqx.codigo as codigo_peticion_obser, scps.observaciones as observaciones,	   				" + 						
							"	     pqx.fecha_peticion as fecha_peticion_o, pqx.hora_peticion as hora_peticion_o, psqx.fecha_cirugia as fecha_cirugia_o," +
							"		 pqx.solicitante as solicitante_o, ca.consecutivo as centro_atencion_o  " + 
						    "		 FROM peticion_qx pqx												" +
							"			  INNER JOIN centro_atencion ca ON ( ca.consecutivo = pqx.centro_atencion )       " +     
							"			  INNER JOIN programacion_salas_qx psqx ON (psqx.peticion = pqx.codigo )																	" +
						    "			  INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )  " +
						    "			  INNER JOIN sol_cirugia_por_servicio scps ON ( scps.numero_solicitud = sc.numero_solicitud )  " + 	        
						    "				   WHERE ( pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +
						    "					  	   OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + 
						    "						 ) AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
		  					" ) x WHERE 1=1  ");
		try
		{
			//-Parametros de consulta
			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
				consultaC.append(" AND x.codigo_peticion_cirujano >= " + nroIniServicio);
				consultaA.append(" AND x.codigo_peticion_anestesiologo >= " + nroIniServicio);
				consultaO.append(" AND x.codigo_peticion_obser >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
				consultaC.append(" AND x.codigo_peticion_cirujano <= " + nroFinServicio);
				consultaA.append(" AND x.codigo_peticion_anestesiologo <= " + nroFinServicio);
				consultaO.append(" AND x.codigo_peticion_obser <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND to_char(pqx.fecha_peticion, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
				consultaC.append(" AND to_char(x.fecha_peticion_c, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
				consultaA.append(" AND to_char(x.fecha_peticion_a, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
				consultaO.append(" AND to_char(x.fecha_peticion_o, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  to_char(pqx.fecha_peticion, 'YYYY-MM-DD') <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
				consultaC.append("  AND  to_char(x.fecha_peticion_c, 'YYYY-MM-DD') <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
				consultaA.append("  AND  to_char(x.fecha_peticion_a, 'YYYY-MM-DD') <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
				consultaO.append("  AND  to_char(x.fecha_peticion_o, 'YYYY-MM-DD') <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
				consultaC.append(" AND to_char(x.fecha_cirugia_c, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
				consultaA.append(" AND to_char(x.fecha_cirugia_a, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
				consultaO.append(" AND to_char(x.fecha_cirugia_o, 'YYYY-MM-DD') >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND to_char(pqx.fecha_cirugia, 'YYYY-MM-DD') <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
				consultaC.append(" AND to_char(x.fecha_cirugia_c, 'YYYY-MM-DD') <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
				consultaA.append(" AND to_char(x.fecha_cirugia_a, 'YYYY-MM-DD') <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
				consultaO.append(" AND to_char(x.fecha_cirugia_o, 'YYYY-MM-DD') <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}

			//-Si se esta buscando por fechas de cirugia se debe adicionar a la 
			//-consulta la condicion de que se busque tambien cuando las peticiones no tienen
			//-fecha de cirugia.
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
				consultaC.append(" OR x.fecha_cirugia_c IS NULL ");
				consultaA.append(" OR x.fecha_cirugia_a IS NULL ");
				consultaO.append(" OR x.fecha_cirugia_o IS NULL ");
			}
			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND pqx.solicitante = " + profesionalSolicita);
				consultaC.append(" AND x.solicitante_c = " + profesionalSolicita);
				consultaA.append(" AND x.solicitante_a = " + profesionalSolicita);
				consultaO.append(" AND x.solicitante_o = " + profesionalSolicita);
			}
			if ( centroAtencion != 0)
			{
				consulta.append("  AND pqx.centro_atencion = " + centroAtencion);
				consultaC.append(" AND x.centro_atencion_c = " + centroAtencion);
				consultaA.append(" AND x.centro_atencion_a = " + centroAtencion);
				consultaO.append(" AND x.centro_atencion_o = " + centroAtencion);
			}

			consulta.append("  ORDER BY pqx.fecha_peticion, pqx.hora_peticion");
			consultaC.append("  ORDER BY x.fecha_peticion_c, x.hora_peticion_c");
			consultaA.append("  ORDER BY x.fecha_peticion_a, x.hora_peticion_a");
			consultaO.append("  ORDER BY x.fecha_peticion_o, x.hora_peticion_o");
			
			
			logger.info("consulta--->"+consulta);
			logger.info("consultaC--->"+consultaC);
			logger.info("consultaA--->"+consultaA);
			logger.info("consultaO--->"+consultaO);
			
			
			//-Consultar la informacion de peticion.
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			
			//-Consultar la informacion de los cirujanos.
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaC.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaAux = new HashMap(); 
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery())); 
			mapa.put ("numRegCirujanos", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);

			//-Consultar la informacion de los anestesiologos.
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaA.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapaAux.clear(); 
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery())); 
			mapa.put ("numRegAnestesiologos", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);

			pst= new PreparedStatementDecorator(con.prepareStatement(consultaO.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapaAux.clear(); 
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery())); 
			mapa.put ("numRegObser", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);

			return mapa;
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarListadoPeticionesBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo para listar las peticiones de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public static Collection accionResultadoBusquedaAvanzadaProgramadas(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita)
	{
		StringBuffer consulta = new StringBuffer();
	
		consulta.append(" SELECT pqx.codigo as codigo, " + 
						"		 CASE WHEN to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') IS NULL THEN '' ELSE to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') END AS fecha_cirugia, " + 
						"		 psqx.hora_inicio || ' --- ' ||  psqx.hora_fin AS horas_cirugia, 				 " + 
						" 	     getNombrePersona(pqx.solicitante) as solicitante,							     " +     
						"	 	 per.tipo_identificacion || ' ' || per.numero_identificacion as id,			     " +     
						" 	  	 getNombrePersona(pqx.paciente) as paciente 									 " +
						"	  	   FROM peticion_qx pqx 														 " +    
						"     	   INNER JOIN personas per ON ( per.codigo = pqx.paciente )         		 " +      																			   
						"			   INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo )   " +      																			   
						"     	        WHERE ( pqx.estado_peticion =  " + ConstantesBD.codigoEstadoPeticionProgramada +      
						" 				   		OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + 
						"					  ) AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ");

		try
		{
			//-Parametros de consulta
			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}

			//-Si se esta buscando por fechas de cirugia se debe adicionar a la 
			//-consulta la condicion de que se busque tambien cuando las peticiones no tienen
			//-fecha de cirugia.
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
			}
			
			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND pqx.solicitante = " + profesionalSolicita);
			}
		
			
			//-Ordenar por fecha de peticion .... 
			consulta.append(" ORDER BY pqx.fecha_cirugia");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarListadoPeticionesBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
	}
	
	

	/**
	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public static Collection cargarInformacionServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita)
	{
		
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("  	   SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
						"				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad  "  +
						"				  FROM peticion_qx pqx " +
						"					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						"					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						"					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						"					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						" 					   INNER JOIN personas per ON ( per.codigo = pqx.solicitante ) " +
						"				  WHERE pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
						"  				  AND rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups); 					   

		try
		{

			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}
			
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
			}

			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND per.codigo = " + profesionalSolicita);
			}

			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionServiciosBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
		
	}

	/**
	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public static Collection cargarInformacionServiciosBusquedaProgramadas(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita)
	{
		
		StringBuffer consulta = new StringBuffer();
		
		 consulta.append(" SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
				 "				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad  "  +
				 "				  FROM peticion_qx pqx " +
				 "					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
				 "					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
				 "					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
				 "					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
				 "							 WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups + 
		 		 "							 AND pqx.programable = '"+ConstantesBD.acronimoSi+"'  							   " +
		 		 "	  						 AND  ( pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +      
				 "							   	    OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + ")");       

		try
		{

			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
			}
			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND pqx.solicitante = " + profesionalSolicita);
			}

			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionServiciosBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
		
	}
	
	/**
	 * Metodo para listar los servicios de las peticiones que se consulten de acuerdo a unos parametros de busqueda
	 * En la funcionalidad de consulta de cirugias programadas. 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinPeticion
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesionalSolicita
	 * @return
	 */
	public static HashMap cargarListadoConsultaServiciosBusqueda(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinPeticion, String fechaIniCirugia, String fechaFinCirugia, int profesionalSolicita)
	{
		
		StringBuffer consulta = new StringBuffer();
		 consulta.append("  	   SELECT pqx.codigo as codigo_peticion_servicio, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups,  " +
		 				 "				  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad_servicio	" +
						 "				  FROM peticion_qx pqx " +
						 "					   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						 "					   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						 "					   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						 "					   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						 "					   INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo )   " +
						 "							 WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +
		 				 "	   						    AND pqx.programable = '"+ConstantesBD.acronimoSi+"' " +
		 				 "								AND  (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +      
		 				 " 									  OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + ")");       
		try
		{

			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniPeticion.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_peticion >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniPeticion) +"'" );
			}
			if (!fechaFinPeticion.trim().equals(""))
			{
				consulta.append("  AND  pqx.fecha_peticion <= '" + UtilidadFecha.conversionFormatoFechaABD(fechaFinPeticion) +"'" );
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaIniCirugia.trim().equals("") && !fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" OR pqx.fecha_cirugia IS NULL ");
			}
			if ( profesionalSolicita != -1)
			{
				consulta.append(" AND pqx.solicitante = " + profesionalSolicita);
			}

			consulta.append(" ORDER BY pqx.fecha_cirugia, psqx.hora_inicio ");

			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionServiciosBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
		
	}
	
	
	/**
	 * Metodo para Cargar la información de las salas... 
	 * @param con
	 * @param fechaProgramacion
	 * @return
	 */
	
	public static Collection cargarProgramacionSalas(Connection con, String fechaProgramacion, String consulta)
	{
		
		logger.info(consulta.toString()+" ->"+UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));
		
		try
		{
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarProgramacionSalas de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
		
	}

	/**
	 * Metodo para saber Si se pùede programar la sala con ese horario determinado
	 * @param numeroPeticion
	 * @param fechaProgramacion
	 * @param nroSala
	 * @param horaInicioProgramacion
	 * @param horaFinProgramacion
	 * @return 0 Si no hay problemas
	 * 		  -1 Si hubo problemas con el motor de BD	
	 * 		  -2 Si hay una programacion para fecha = hora = sala <> paciente	
	 * 		  -3 Si hay una programacion para fecha = hora = paciente <> sala 	
	 */
	public static int verificarCronograma(Connection con, int numeroPeticion, String fechaProgramacion, int nroSala, String horaInicioProgramacion, String horaFinProgramacion)
	{
		PreparedStatementDecorator ps = null;
		
		//----consulta para saber si en sala-fecha-hora hay algo ya programado
		String consulta1 = "	SELECT peticion	 FROM programacion_salas_qx		 " +
						   "						 WHERE fecha_cirugia = ?	 " +
						   "						   AND sala = ? 			 " +
						   "						   AND hora_inicio >= ? 	 " +
						   "						   AND hora_fin	<= ?         " + 
						   "						   AND peticion	<> ?         "; 

		String consulta2 =  "  	SELECT pet.paciente FROM peticion_qx pet 														 				" +
 		  					" 							INNER JOIN (SELECT  pqx.paciente  FROM programacion_salas_qx psx 		 				" + 
			  				"				      							INNER JOIN peticion_qx pqx ON (pqx.codigo = psx.peticion AND pqx.programable = '"+ConstantesBD.acronimoSi+"') " +	
	  						"												WHERE psx.fecha_cirugia = ?	" +
							"		 	    						 		AND (																" +			
							"										 	          (psx.hora_inicio >= ? AND psx.hora_inicio <= ?)				" +
							"												 OR																	" +		
							"												 	  (psx.hora_fin >= ? AND psx.hora_fin <= ?)						" +
							"												 OR																	" +		
							"										 		     (? >= psx.hora_inicio AND ? <= psx.hora_fin)					" +
							"												 OR																	" +
							"													 (? >= psx.hora_inicio AND ? <= psx.hora_fin)					" +
							"				     								)																" +
							"												AND peticion <> ?									  				" +
							"										)  sub 																		" +  
							"							ON ( sub.paciente = pet.paciente )														" +
							"							WHERE pet.codigo = ?				" +
							"							AND pet.programable = '"+ConstantesBD.acronimoSi+"'													";
		
		try
		{	
			String cad = ""; 
		
			for (int i = 2; i < 4; i++)
			{
				switch(i)
				{
					case 2: {
								cad = consulta1; 
								ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));		
								ps.setInt(2, nroSala);
								ps.setString(3, horaInicioProgramacion);
								ps.setString(4, horaFinProgramacion);
								ps.setInt(5, numeroPeticion);
							}
					break;
					case 3: {
								cad = consulta2;
								ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));
								ps.setString(2, horaInicioProgramacion);
								ps.setString(3, horaFinProgramacion);
								ps.setString(4, horaInicioProgramacion);
								ps.setString(5, horaFinProgramacion);
								ps.setString(6, horaInicioProgramacion);
								ps.setString(7, horaInicioProgramacion);
								ps.setString(8, horaFinProgramacion);
								ps.setString(9, horaFinProgramacion);
								ps.setInt(10, numeroPeticion);
								ps.setInt(11, numeroPeticion);
							}
					break;
				}
					
				ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
				if(resultado.next())
				{
					return i*(-1);
				}					
	
			}
			
			return 0; ///-No hubo ninguna validacion que sacara problemas para el nuevo registro.   
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta (verificarCronograma) : SqlBaseProgramacionDao "+e.toString());
			return -1;
		}
	}


	/**
	 * Metodo que inserta la programacion para una sala determinada sala... 
	 * @param con
	 * @param numeroPeticion
	 * @param fechaProgramacion
	 * @param nroSala
	 * @param horaInicioProgramacion
	 * @param horaFinProgramacion
	 * @param operacion
	 * @param codigoUsuario
	 * @return
	 */
	public static int insertarProgamacion(Connection con, int numeroPeticion, String fechaProgramacion, int nroSala, String horaInicioProgramacion, String horaFinProgramacion,  String loginUsuario, int operacion)
	{       
			PreparedStatementDecorator ps;
			int resp=0; 
			String consultaStr = "";
			
			try{

					int nroError = verificarCronograma(con, numeroPeticion,  fechaProgramacion, nroSala, horaInicioProgramacion, horaFinProgramacion);
					
					if ( nroError >= 0 )
					{
						if ( operacion == 1 ) //-Se va a Reprogramar
						  {
							consultaStr = " DELETE FROM  programacion_salas_qx  WHERE  peticion = ?";
							ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, numeroPeticion);
							resp = ps.executeUpdate();
							if (resp <= 0)
							{
								return -1;
							}
						  }	
			
						consultaStr = " INSERT INTO programacion_salas_qx (peticion, fecha_cirugia, sala, estado_sala,        " +
									  "									   hora_inicio, hora_fin, fecha_programacion, hora_programacion, usuario ) " +
									  "									   VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, ?) ";
						
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, numeroPeticion);				
						ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));								
						ps.setInt(3, nroSala);
						ps.setInt(4, ConstantesBD.codigoEstadoSalaOcupada);   
						ps.setString(5, horaInicioProgramacion);
						ps.setString(6, horaFinProgramacion);
						ps.setString(7, UtilidadFecha.getHoraActual());
						ps.setString(8, loginUsuario);
	
						resp = ps.executeUpdate();
						
						if (resp > 0) //------Se debe Modificar la Peticion de la Cirugia
						{
						 
							consultaStr = " UPDATE peticion_qx SET  fecha_cirugia = ?, duracion = ?, estado_peticion = ? " +
										  "				     WHERE  codigo = ?";
	
							ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaProgramacion));
							ps.setString(2,  DuracionHoras(horaInicioProgramacion, horaFinProgramacion)); //-Duracion 06:30 a 08:30 ---> 02:00 
							
							if ( operacion == 1 ) { ps.setInt(3, ConstantesBD.codigoEstadoPeticionReprogramada); }
							else { ps.setInt(3, ConstantesBD.codigoEstadoPeticionProgramada); }
								
							ps.setInt(4, numeroPeticion);				
	
							resp = ps.executeUpdate();
							
							if ( resp > 0 )	{ 	return resp; } //-Si actualizo bien la peticion
							else {	return -1; }
						}
						else
						{
							return -1;  //-No pudo insertar el registro en la BD 
						}
					}
					else
					{
						return nroError;
					}				
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la inserción de datos en la programacion_salas_qx : SqlBaseProgramacionDao "+e.toString() );
					resp = -1;
			}
		return resp;			
	}
	
	/**
	 * Para Calcular la Duracion de la Cirugia
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	private static String DuracionHoras(String horaIni, String horaFin)
	{
	   Date dia1 = new Date();
	   Date dia2 = new Date();

	   dia1. setHours( Integer.parseInt( horaIni.split(":")[0] ) );
	   dia1.setMinutes( Integer.parseInt( horaIni.split(":")[1] ) );

	   dia2.setHours( Integer.parseInt( horaFin.split(":")[0] ) );
	   dia2.setMinutes( Integer.parseInt( horaFin.split(":")[1] ) );
	   
	   int horas = 0;

	   while ( !dia1.after(dia2) )
	    {
	   		 dia1.setMinutes(dia1.getMinutes()+30);

	   		 if ( !dia1.after(dia2) )
	   			horas++;
	    }
	   
	   horas++;
	   
	   String horaReal="";

	   if ( (horas % 2) != 0 ) 
	     horaReal = (horas < 10) ? ("0" + ((horas-1)/2)+ ":30") : ((horas-1)/2)+ ":30";
	   else
	    horaReal = (horas < 10) ? ("0" + (horas/2)+":00") : ( (horas/2) + ":00");
	    
	   return horaReal;
	 }


	/**
	 * Metodo para cargar listado de las peticiones de programadas y reprogramadas
	 * @param con
	 * @param nroPeticion
	 * @return
	 */
	public static Collection cargarListadoPeticionesProgramadas(Connection con, int centroAtencion) 
	{
	 	String consulta = " SELECT pqx.codigo as codigo, " + 
			   			  "		   CASE WHEN to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') IS NULL THEN '' ELSE to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') END AS fecha_cirugia, " + 
			   			  "		   psqx.hora_inicio || ' --- ' ||  psqx.hora_fin AS horas_cirugia, 				 " + 
				  	   	  " 	   getNombrePersona(pqx.solicitante) as solicitante,							 " +     
						  "	 	   per.tipo_identificacion || ' ' || per.numero_identificacion as id,			 " +     
   				  	   	  " 	   getNombrePersona(pqx.paciente) as paciente, CASE WHEN tp.descripcion IS NULL THEN '' ELSE tp.descripcion END AS tipo_anestesia    									 " +
						  "	  	   FROM peticion_qx pqx 														 " +    
			  			  "     	   INNER JOIN personas per ON ( per.codigo = pqx.paciente )         		 " +      																			   
						  "			   INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo )   " +
						  " 		   LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
		       			  "			   LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   "  +
						  "     	        WHERE pqx.estado_peticion =  " + ConstantesBD.codigoEstadoPeticionProgramada +      
						  " 				   OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada +
						  "					  AND pqx.centro_atencion = " + centroAtencion +
						  "					  AND pqx.programable = '"+ConstantesBD.acronimoSi+"' 			   " +
						  " 			  	  ORDER BY pqx.fecha_cirugia";
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseProgramacionCirugia en la consulta ( cargarListadoPeticionesProgramadas )  : "+e.toString());
			return null;
		}
	}


	/**
	 *  Metodo para listar las peticiones de acuerdo a unos parametros de busqueda en la Reprogramacion de Cirugias
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public static Collection cargarListadoPeticionesBusquedaReprogramacion(
			Connection con, 
			int nroIniServicio, 
			int nroFinServicio, 
			String estadoPeticion, 
			int profesional, 
			String fechaIniCirugia, 
			String fechaFinCirugia)
	{
		StringBuffer consulta = new StringBuffer();
		
		
		consulta.append("	SELECT pqx.codigo as codigo, " +
	   			  		"		   CASE WHEN to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') IS NULL THEN '' ELSE to_char(pqx.fecha_cirugia, 'DD/MM/YYYY') END AS fecha_cirugia, " +
			   			"		   psqx.hora_inicio || ' --- ' ||  psqx.hora_fin AS horas_cirugia, 						" +						
				  	   	"   	   getNombrePersona(pqx.solicitante) as solicitante,							 		" +
						"		   per.tipo_identificacion || ' ' || per.numero_identificacion as id, 			 	  	" +
 				  	    " 		   getNombrePersona(pqx.paciente) as paciente, 											" +
 				  	    "		   CASE WHEN tp.descripcion IS NULL THEN '' ELSE tp.descripcion END AS tipo_anestesia   " +				  	    
						"		   FROM peticion_qx pqx 														  		" +
						"     	    	INNER JOIN personas per ON ( per.codigo = pqx.solicitante )			  			" +
						"			    INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo )   		" +						
			 	      	" 			    LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )   " +
		       			"			    LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia  )   "  +
						"    	    	  	 WHERE true AND pqx.programable = '"+ConstantesBD.acronimoSi+"' ");
		try
		{
			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
				//-Tener en cuenta que la fecha de cirugia puede ser null
				consulta.append(" OR pqx.fecha_cirugia IS  NULL ");
			}
			if ( profesional != -1)
			{
				consulta.append(" AND per.codigo = " + profesional);
			}		
			if ( UtilidadCadena.noEsVacio(estadoPeticion) )
			{
				if ( estadoPeticion.equals("0") )
				{
					consulta.append(" AND   pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada);	
				}else
				{
					consulta.append(" AND   pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada );	
				}
			}			
			
			//-Ordenar por fecha de peticion .... 
			consulta.append(" ORDER BY pqx.fecha_peticion");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarListadoPeticionesBusquedaReprogramacion de SqlBaseProgramacionCirugia: "+e);
			return null;
		}	}


	/**
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param estadoPeticion
	 * @param profesional
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @return
	 */
	public static Collection cargarInformacionServiciosPeticionReprogramacion(
			Connection con, 
			int nroIniServicio, 
			int nroFinServicio, 
			String estadoPeticion, 
			int profesional, 
			String fechaIniCirugia, 
			String fechaFinCirugia)
	{
		
		StringBuffer consulta = new StringBuffer();
		
		consulta.append("  	   SELECT pqx.codigo as codigo_peticion, pser.numero_servicio as numero_servicio, rser.codigo_propietario as cups, " +
						"			  rser.servicio ||'  '|| rser.descripcion as servicio, esp.codigo || '  ' || esp.nombre as especialidad  "  +
						"			  FROM peticion_qx pqx " +
						"				   INNER JOIN peticiones_servicio pser ON ( pser.peticion_qx = pqx.codigo ) "+ 
						"				   INNER JOIN servicios ser ON ( ser.codigo = pser.servicio )  "+
						"				   INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) "+
						"				   INNER JOIN especialidades esp ON ( esp.codigo = ser.especialidad  ) " +
						" 				   INNER JOIN personas per ON ( per.codigo = pqx.solicitante ) " +
						"			  		    WHERE rser.tipo_tarifario = " +ConstantesBD.codigoTarifarioCups+
						" 						AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "); 					   

		try
		{

			if (nroIniServicio != 0)
			{
				consulta.append(" AND pqx.codigo >= " + nroIniServicio);
			}
			if (nroFinServicio != 0)
			{
				consulta.append(" AND pqx.codigo <= " + nroFinServicio);
			}
			if (!fechaIniCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia >= '" + UtilidadFecha.conversionFormatoFechaABD(fechaIniCirugia)+"'" );
			}
			if (!fechaFinCirugia.trim().equals(""))
			{
				consulta.append(" AND pqx.fecha_cirugia <= '"+ UtilidadFecha.conversionFormatoFechaABD(fechaFinCirugia)+"'");
			}
			if ( profesional != -1)
			{
				consulta.append(" AND per.codigo = " + profesional);
			}
			if (!estadoPeticion.trim().equals(""))
			{
				if ( estadoPeticion.equals("programada") )
				{
					consulta.append(" AND   pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada);	
				}else
				{
					consulta.append(" AND   pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada );	
				}
			}
			
	
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionServiciosPeticionReprogramacion de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
	}


	/**
	 * Metodo para dejar disponible la peticion de cirugia de nuevo y eliminar la programacion de la misma
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static int eliminarProgramacion(Connection con, int numeroPeticion)
	{
		PreparedStatementDecorator ps;
		String consultaStr = "";
		int resp=-1;
		
		try{
				if ( actualizarPeticion(con, numeroPeticion, ConstantesBD.codigoEstadoPeticionPendiente)  >  0 )
				{
					consultaStr = " DELETE FROM  programacion_salas_qx  WHERE  peticion = ?";

					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, numeroPeticion);
					resp = ps.executeUpdate();

					if (resp > 0)
						resp = numeroPeticion;
				}
		}
		catch(SQLException e)
		{
				logger.warn(" Error en la Eliminación (eliminarProgramacion) : SqlBaseProgramacionDao "+e.toString() );
				resp = -1;
		}
		return resp;
	}	
	
	/**
	 * Metodo para colocar la peticion en estado pendiente luego de una reprogramacion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static int actualizarPeticion(Connection con, int numeroPeticion, int estadoPeticion)
	{
		PreparedStatementDecorator ps;
		String consultaStr = "";
		int resp;
		
		try{
					consultaStr = " UPDATE peticion_qx SET  estado_peticion = " +  estadoPeticion +
								  "				     WHERE  codigo = ?";

					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, numeroPeticion);
					resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(" Error actualizando la peticion actualizarPeticion : SqlBaseProgramacionDao "+e.toString() );
				resp = -1;
		}
		return resp;
	}

	/**
	 * Metodo para cargar el codigo de la persona. (Cedula generalmente)
	 * @param con
	 * @param nroPeticion
	 * @return
	 */

	public static Collection cargarCodigoPersona(Connection con, int nroPeticion) 
	{
		StringBuffer consulta = new StringBuffer();
		consulta.append(" SELECT paciente as codigo FROM peticion_qx WHERE codigo = " + nroPeticion+" AND programable = '"+ConstantesBD.acronimoSi+"'");       

		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionServiciosBusqueda de SqlBaseProgramacionCirugia: "+e);
			return null;
		}
	}

	
	/**
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static HashMap consultaProgramacionSalasQx(Connection con, int numeroPeticion) {
		HashMap mapa=new HashMap();
		
		String consulta="SELECT	 peticion," +
							"	 fecha_cirugia," +
							"	 sala," +
							"	 estado_sala," +
							"	 hora_inicio," +
							"	 hora_fin," +
							"	 fecha_programacion," +
							"	 hora_programacion," +
							"	 usuario " +
							" FROM " +
							"	programacion_salas_qx " +
							" WHERE " +
							"	peticion="+numeroPeticion;

		logger.info("\n\n\n\n\n <<<<< CONSULTA DE PETICION >>>>"+consulta);
		
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		} catch (SQLException e) {
			logger.info("\n\n\n <<< ERROR EN LA CONSULTA DE PETICIONES >>> "+e+"\n\n\n\n"+consulta);
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param rep
	 * @return
	 */
	public static int insertarCancelacionProgramacionSalasQx(Connection con, HashMap rep){
		
		int y=0;
		String sentencia="INSERT INTO " +
							"	 cancelacion_prog_salas_qx ( " +
									 "	 codigo," +
									 "	 peticion," +
									 "	 fecha_cirugia," +
									 "	 sala," +
									 "	 hora_inicio," +
									 "   hora_fin," +
									 "	 fecha_programacion," +
									 "	 hora_programacion," +
									 "	 usuario_programacion," +
									 "   fecha_cancelacion," +
									 "	 hora_cancelacion, " +
									 "	 motivo_cancelacion," +
									 "	 usuario_cancelacion " +
									 " ) VALUES ( " +
									 ""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cancelacion_prog_salas_qx")+","+
									 ""+rep.get("peticion_0")+","+
									 "'"+rep.get("fecha_cirugia_0")+"',"+
									 ""+rep.get("sala_0")+","+
									 "'"+rep.get("hora_inicio_0")+"',"+
									 "'"+rep.get("hora_fin_0")+"',"+
									 "'"+rep.get("fecha_programacion_0")+"',"+
									 "'"+rep.get("hora_programacion_0")+"',"+
									 "'"+rep.get("usuario_0")+"',"+
									 " CURRENT_DATE ,"+
									 " "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ,"+
									 "'"+rep.get("motivo")+"',"+
									 "'"+rep.get("usuario")+"'"+
									 ")";
		 
			logger.info("\n\n\n\n\n <<<<< SENTENCIA ADICION PETICION >>>>"+sentencia);
			
			try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			y=ps.executeUpdate();
			
			} catch (SQLException e) {
			logger.info("\n\n\n <<< ERROR EN LA ADICION DE PETICIONES >>> "+e+"\n\n\n\n"+sentencia);
			}
			
			
		if (y>0)
		{
			y=0;
			sentencia="UPDATE  " +
					"	 peticion_qx SET " +
							 "	 estado_peticion=0 " +
							 "  WHERE " +
							 " codigo="+rep.get("peticion_0");
			 
				logger.info("\n\n\n\n\n <<<<< UPDATE PETICION_QX >>>>"+sentencia);
				
				try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				y=ps.executeUpdate();
				
				} catch (SQLException e) {
				logger.info("\n\n\n <<< ERROR EN LA ACTUALIZACION DEL ESTADO DE PETICION_QX >>> "+e+"\n\n\n\n"+sentencia);
				}
		}
			
		return y;
	}
	
	
	
	/**
	 * Metodo para cargar listado de las peticiones para la funcionalidad Consulta de programacaion de Cirugias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */

	public static HashMap cargarListadoPeticiones(Connection con, int codigoPersona) 
	{
		HashMap mapa = new HashMap();
		StringBuffer consulta = new StringBuffer();
		
		  consulta.append("    SELECT  pqx.codigo as codigo_peticion,   													" +
						  "		   	   CASE WHEN to_char(psqx.fecha_cirugia, 'DD/MM/YYYY') IS NULL THEN '' ELSE to_char(psqx.fecha_cirugia, 'DD/MM/YYYY') END AS fecha_cirugia, " +
						  "		   	   psqx.hora_inicio AS hora_cirugia, " +
						  "			   CASE WHEN tp.descripcion IS NULL THEN '' ELSE tp.descripcion END AS tipo_anestesia,  " + 
						  "			   coalesce(getempresapeticionqx(pqx.codigo),'') as empresa, " +
						  "			s.descripcion || ' -- ' || getnomcentroatencion(pqx.centro_atencion) as sala		    " +
						  "			   FROM peticion_qx pqx																	" +
						  "			   		INNER JOIN programacion_salas_qx psqx ON ( psqx.peticion = pqx.codigo )         " +     
						  "			   		INNER JOIN salas s ON ( s.consecutivo = psqx.sala ) 				   			" +
						  "			   		LEFT OUTER JOIN valoracion_preanestesia vp ON ( vp.peticion_qx = pqx.codigo )	" +   
						  "			   		LEFT OUTER JOIN tipos_anestesia tp ON ( tp.codigo = vp.tipo_anestesia )      	" + 
						  "			   				  WHERE     pqx.estado_peticion IN (" + ConstantesBD.codigoEstadoPeticionProgramada +
						  "									  , " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
						  "			   					AND pqx.paciente = " + codigoPersona + "	   						" +
						  " 							AND pqx.programable = '"+ConstantesBD.acronimoSi+"' 				"+
						  "			   		       			ORDER BY pqx.fecha_cirugia, psqx.hora_inicio					");		

		try
		{
			
			//-- Carga el listado con la informacion de la peticion en general.
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("listado peticion general: "+consulta.toString());
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado));
			
			//----La informacion de los cirujanos.
			consulta = new StringBuffer();
			consulta.append("  SELECT pqx.codigo as codigo_peticion_cirujano, 				 						" + 
				   "           getNombrePersona(ppqx.codigo_medico) as cirujano  									" +
				   "		   FROM peticion_qx pqx																	" +
				   " 			    INNER JOIN prof_partici_peticion_qx ppqx ON ( ppqx.peticion_qx = pqx.codigo ) 	" +     
				   "			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = ppqx.tipo_participante   ) " +
				   "    			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteCirujano + " ) " +
				   "					 WHERE pqx.paciente = " + codigoPersona +
				   "					   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
				   "			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
				   "							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
				   "					   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
				   "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
				   "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
				   "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +				   
				   "												   		 AND pqx.paciente = "+ codigoPersona + " " +
 		   		   "		  											 	 AND pqx1.programable = '"+ConstantesBD.acronimoSi+"') " +
				   "  UNION ALL 																												" +	
				   "				 	SELECT pqx.codigo as codigo_peticion_cirujano,												 			" +
				   "		               getNombrePersona(opsc.codigo_profesional) as cirujano  									   			" +
				   "		    		   FROM peticion_qx pqx																					" +	
				   "		    		   		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )						" +
				   "		     			    INNER JOIN otros_prof_sol_cx opsc ON ( opsc.numero_solicitud = sc.numero_solicitud ) 	        " +
				   "		    			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = opsc.tipo_profesional   )    	" +
				   "		        			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteCirujano + "  ) " +    
				   "		    					 WHERE pqx.paciente =  " + codigoPersona + 
				   "    	 						   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
				   "		    			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +  
				   "		    							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " ) ");

			stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("INFO CIRUJANOS: "+consulta.toString());
			resultado=new ResultSetDecorator(stm.executeQuery());
			HashMap mapaAux = new HashMap();
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado));
			mapaAux.put ("numRegCirujanos", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);
			

			//----La informacion de los anestesiologos.
			consulta = new StringBuffer();
			 
			consulta.append("  SELECT pqx.codigo as codigo_peticion_anestesiologo, 									" + 
						   "           getNombrePersona(ppqx.codigo_medico) as anestesiologo								" +
						   "		   FROM peticion_qx pqx																	" +
						   " 			    INNER JOIN prof_partici_peticion_qx ppqx ON ( ppqx.peticion_qx = pqx.codigo ) 	" +     
						   "			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = ppqx.tipo_participante   ) " +
						   "    			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteAnestesiologo + " ) " +
						   "					 WHERE pqx.paciente = " + codigoPersona +
						   " 					   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
						   "			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
						   "							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" +
						   "					   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
						   "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
						   "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
						   "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +
						   "												   		 AND pqx.paciente = "+ codigoPersona + "" +
						   " 								 					     AND pqx1.programable = '"+ConstantesBD.acronimoSi+"') " +
						   "  UNION ALL 																												" +	
						   "				 	SELECT pqx.codigo as codigo_peticion_anestesiologo,												 			" +
						   "		               getNombrePersona(opsc.codigo_profesional) as anestesiologo  									   			" +
						   "		    		   FROM peticion_qx pqx																					" +	
						   "		    		   		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )						" +
						   "		     			    INNER JOIN otros_prof_sol_cx opsc ON ( opsc.numero_solicitud = sc.numero_solicitud ) 	        " +
						   "		    			    INNER JOIN tipos_participantes_inst_qx tpiqx ON ( tpiqx.codigo = opsc.tipo_profesional   )    	" +
						   "		        			INNER JOIN tipos_participantes_qx tpqx ON ( tpiqx.tipo_profesional = tpqx.codigo AND tpqx.codigo = " + ConstantesBD.codigoTipoParticipanteAnestesiologo + "  ) " +    
						   "		    					 WHERE pqx.paciente =  " + codigoPersona + 
						   "							       AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
						   "		    			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +  
						   "		    							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " ) ");

			
			
			mapaAux.clear();
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=new ResultSetDecorator(stm.executeQuery());
			mapaAux = new HashMap();
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado));
			mapaAux.put ("numRegAnestesiologos", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);

			
			//----La informacion de las observaciones de los servicios.
			consulta = new StringBuffer();
			consulta.append(" SELECT pqx.codigo as codigo_peticion_obser,  ps.observaciones as observaciones	" +
							"		 FROM peticion_qx pqx														" +
							"			  INNER JOIN peticiones_servicio ps ON ( ps.peticion_qx = pqx.codigo )  " +
							"			  	   WHERE pqx.paciente = " + codigoPersona + 
							" 					 AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
 						    "			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada +
						    "							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + " )	" + 
						    "					   AND pqx.codigo NOT IN  ( SELECT pqx1.codigo FROM peticion_qx pqx1  		" + 
						    "										   		 INNER JOIN solicitudes_cirugia sc1 ON (sc1.codigo_peticion = pqx1.codigo ) " + 
						    "										   		 WHERE ( pqx1.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  + 
						    "														 OR pqx1.estado_peticion =" + ConstantesBD.codigoEstadoPeticionReprogramada + " ) " +
						    "												   		 AND pqx.paciente = "+ codigoPersona + " " +
				    		"  														 AND pqx1.programable = '"+ConstantesBD.acronimoSi+"') " +
						    " UNION ALL																							" +
						    " SELECT pqx.codigo as codigo_peticion_obser, scps.observaciones as observaciones	   				" + 						
						    "					    		   FROM peticion_qx pqx												" +
						    "					    		   		INNER JOIN solicitudes_cirugia sc ON ( sc.codigo_peticion = pqx.codigo )  " +
						    "					     			    INNER JOIN sol_cirugia_por_servicio scps ON ( scps.numero_solicitud = sc.numero_solicitud )  " + 	        
						    "					    					 WHERE pqx.paciente =  " + codigoPersona + 
						    " 					 						   AND pqx.programable = '"+ConstantesBD.acronimoSi+"' "+
						    "					    			   		   AND (    pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionProgramada  +
						    "					    							 OR pqx.estado_peticion = " + ConstantesBD.codigoEstadoPeticionReprogramada + "   )");
						    
			mapaAux.clear();
			stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=new ResultSetDecorator(stm.executeQuery());
			mapaAux = new HashMap();
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado));
			mapaAux.put ("numRegObser", mapaAux.get("numRegistros") );
			mapaAux.remove("numRegistros"); mapa.putAll(mapaAux);
			
			return mapa;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el listado de peticiones ["+ e +"]");
			return null;
		}
		
	}


		
}
