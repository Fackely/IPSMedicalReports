/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;


public class SqlBaseCuentaServicioDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuentaServicioDao.class);

	
	/**
	 * Metodo para cargar los grupos de servicios. 
	 * @param con 
	 * @param tipoServicio 
	 * @param grupoServicio 
	 * @param institucion 
	 * @return
	 */

	public static HashMap cargarGrupos(Connection con, int tipoGrupo, int centroCosto, int grupoServicio, String tipoServicio, int institucion, int especialidad)
	{
		String consulta =""; 
		
		
		switch (tipoGrupo)
		{
			case 0:  //---Consultar los grupos de servicios.   
			{
				consulta = "	SELECT * FROM ( " +
						   "	SELECT gs.codigo as codigo_grupo, gs.descripcion as nombre_grupo, 										" +
						   "		   CASE WHEN gsci.cuenta_ingreso IS NULL THEN 0 ELSE gsci.cuenta_ingreso END as cuentaingreso,	 	" +
						   "		   CASE WHEN gsci.cuenta_ingreso IS NULL THEN 0 ELSE gsci.cuenta_ingreso END as h_cuentaingreso,	 	" +
						   "		   CASE WHEN gsci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificado, 						" +
						   "		   CASE WHEN gsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE gsci.cuenta_ingreso_vig_anterior END as cuenta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN gsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE gsci.cuenta_ingreso_vig_anterior END as h_cta_ingreso_vig_anterior," +
						   "	 	   CASE WHEN gsci.cuenta_contable_costo IS NULL THEN 0 ELSE gsci.cuenta_contable_costo END AS cuentacosto, " +
						   "	 	   CASE WHEN gsci.cuenta_contable_costo IS NULL THEN 0 ELSE gsci.cuenta_contable_costo END AS cuentacostoho " +
						   "		   FROM grupos_servicios gs																			" + 
						   "				LEFT OUTER JOIN grupo_servicio_cue_ingr gsci ON ( gs.codigo = gsci.grupo_servicio AND  gsci.centro_costo = ? )	" +
						   "	UNION ALL 	" + //---Union para sacar el Grupo con codigo -1 (Que Representa Todos Los Servicios)
						   "	SELECT -1 as codigo_grupo, 'null' as nombre_grupo, 														" +
						   "		   CASE WHEN gsci.cuenta_ingreso IS NULL THEN 0 ELSE gsci.cuenta_ingreso END as cuentaingreso,	 	" +
						   "		   CASE WHEN gsci.cuenta_ingreso IS NULL THEN 0 ELSE gsci.cuenta_ingreso END as h_cuentaingreso,	" +
						   "		   CASE WHEN gsci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificadom, 						" +
						   "		   CASE WHEN gsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE gsci.cuenta_ingreso_vig_anterior END as cuenta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN gsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE gsci.cuenta_ingreso_vig_anterior END as h_cta_ingreso_vig_anterior,	 	" +
						   "	 	   CASE WHEN gsci.cuenta_contable_costo IS NULL THEN 0 ELSE gsci.cuenta_contable_costo END AS cuentacosto, " +
						   "	 	   CASE WHEN gsci.cuenta_contable_costo IS NULL THEN 0 ELSE gsci.cuenta_contable_costo END AS cuentacostoho " +
						   "		   FROM grupo_servicio_cue_ingr gsci																" +
						   "				WHERE grupo_servicio = -1 	" +
						   "				  AND centro_costo = ? 																		" + 
						   "				   ) x ORDER BY x.nombre_grupo 																"; 
			}
			break;

			case 1:  //---Consultar los tipos de servicios.  
			{
				consulta = "   SELECT * FROM ( " +
						   "   SELECT s.tipo_servicio as codigo_tipo_servicio, ts.nombre as nombre_tipo_servicio,						 	 " +
						   "  		   CASE WHEN tsci.cuenta_ingreso IS NULL THEN 0 ELSE tsci.cuenta_ingreso END as cuentaingreso_tser,	 " +	 	 
						   "  		   CASE WHEN tsci.cuenta_ingreso IS NULL THEN 0 ELSE tsci.cuenta_ingreso END as h_cuentaingreso_tser,	 " +	 	 
						   "  		   CASE WHEN tsci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificado_tser,  					 " +
						   "		   CASE WHEN tsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE tsci.cuenta_ingreso_vig_anterior END as cta_ingr_vig_ant_tser,	 	" +
						   "		   CASE WHEN tsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE tsci.cuenta_ingreso_vig_anterior END as h_cta_ing_vig_ant,	 	" +
						   "		   CASE WHEN tsci.cuenta_contable_costo IS NULL THEN 0 ELSE tsci.cuenta_contable_costo END as cuentacostotser,	 	" +
						    "		   CASE WHEN tsci.cuenta_contable_costo IS NULL THEN 0 ELSE tsci.cuenta_contable_costo END as cuentacostotserho	 	" +
						   
						   "   		   FROM tipos_servicio ts 																				 " +
						   "  			    INNER JOIN servicios s ON ( ts.acronimo = s.tipo_servicio )  									 " +
			   			   "			    LEFT OUTER JOIN tipo_servicio_cuenta_ing tsci ON ( ts.acronimo = tsci.tipo_servicio AND tsci.centro_costo = " + centroCosto + "  AND  tsci.grupo_servicio =  " + grupoServicio + " ) " +	 
						   "	 	   		     WHERE s.grupo_servicio = " + grupoServicio + "												 " +   
						   "	 	   		           GROUP BY s.tipo_servicio, ts.nombre, tsci.grupo_servicio, tsci.cuenta_ingreso, tsci.cuenta_ingreso_vig_anterior, tsci.cuenta_contable_costo		 " +
						//   "	 	   		      			 ORDER BY ts.nombre		  														 " +				
						   "	UNION ALL 	" + //---Union para sacar el tipo de servicio con codigo -1 (Que Representa Todos Los Tipos de Servicios)
						   "	SELECT '1' as codigo_tipo_servicio, 'null' as nombre_tipo_servicio,									 	 " +
						   "  		   CASE WHEN tsci.cuenta_ingreso IS NULL THEN 0 ELSE tsci.cuenta_ingreso END as cuentaingreso_tser,	 " +	 	 
						   "  		   CASE WHEN tsci.cuenta_ingreso IS NULL THEN 0 ELSE tsci.cuenta_ingreso END as h_cuentaingreso_tser,	 " +	 	 
						   "  		   CASE WHEN tsci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificado_tser,  						 " +
						   "		   CASE WHEN tsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE tsci.cuenta_ingreso_vig_anterior END as cta_ingr_vig_ant_tser,	 	" +
						   "		   CASE WHEN tsci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE tsci.cuenta_ingreso_vig_anterior END as h_cta_ing_vig_ant,	 	" +
						   "		   CASE WHEN tsci.cuenta_contable_costo IS NULL THEN 0 ELSE tsci.cuenta_contable_costo END as cuentacostotser,	 	" +
						   "		   CASE WHEN tsci.cuenta_contable_costo IS NULL THEN 0 ELSE tsci.cuenta_contable_costo END as cuentacostotserho	 	" +
						   "   		   FROM tipo_servicio_cuenta_ing tsci 																	 " +	 
						   "	 	   		     WHERE tsci.grupo_servicio = " + grupoServicio + "											 " +
						   "					   AND tsci.tipo_servicio = '1' 															 " +
						   "					   AND tsci.centro_costo = " + centroCosto + "												 " +
						   "	) x ORDER BY x.nombre_tipo_servicio																			 " ;   
			}
			break;

			case 2:  //---Consultar las especilidades de los servicios.  
			{
				consulta = "	SELECT * FROM (" +
						   "	SELECT s.especialidad as codigo_especialidad, e.nombre as nombre_especialidad,   							" +
				   		   "  		   CASE WHEN esci.cuenta_ingreso IS NULL THEN 0 ELSE esci.cuenta_ingreso END as cuentaingreso_esp, 	" +	 	 
						   "  		   CASE WHEN esci.cuenta_ingreso IS NULL THEN 0 ELSE esci.cuenta_ingreso END as h_cuentaingreso_esp,	" + 	 
						   "  		   CASE WHEN esci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificado_esp,					 	" +
						   "		   CASE WHEN esci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE esci.cuenta_ingreso_vig_anterior END as cta_ing_vig_ant_esp,	 	" +
						   "		   CASE WHEN esci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE esci.cuenta_ingreso_vig_anterior END as h_cta_ing_vig_ant_esp,	 	" +
						   "		   CASE WHEN esci.cuenta_contable_costo IS NULL THEN 0 ELSE esci.cuenta_contable_costo END as cuentacostoesp,	 	" +
						   "		   CASE WHEN esci.cuenta_contable_costo IS NULL THEN 0 ELSE esci.cuenta_contable_costo END as cuentacostoespho	 	" +
						   "  		   FROM servicios s 																					" +
						   "  		   		INNER JOIN especialidades e ON ( e.codigo = s.especialidad ) 									" +	
						   "  		   		INNER JOIN tipos_servicio ts ON ( ts.acronimo = s.tipo_servicio ) 								" +	
						   "  		   		LEFT OUTER JOIN especi_serv_cue_ing esci ON ( e.codigo = esci.especialidad_servicio AND esci.centro_costo = " +  centroCosto + " AND esci.grupo_servicio = " + grupoServicio + " AND esci.tipo_servicio = '" + tipoServicio + "' ) " +
						   "  		   				   WHERE ts.acronimo = '" + tipoServicio + "' 											" +
						   "  		   					 AND s.grupo_servicio = "+ grupoServicio + "										" +
						   "  		   					 GROUP BY s.especialidad, e.nombre, esci.cuenta_ingreso, esci.grupo_servicio, esci.cuenta_ingreso_vig_anterior, esci.cuenta_contable_costo 							" +
						   //"  		   					 ORDER BY e.nombre " +
						   "	UNION ALL	" + 
						   "	SELECT -1 as codigo_especialidad, 'null' as nombre_especialidad,   											" +
				   		   "  		   CASE WHEN esci.cuenta_ingreso IS NULL THEN 0 ELSE esci.cuenta_ingreso END as cuentaingreso_esp, 	" +	 	 
						   "  		   CASE WHEN esci.cuenta_ingreso IS NULL THEN 0 ELSE esci.cuenta_ingreso END as h_cuentaingreso_esp,	" + 	 
						   "  		   CASE WHEN esci.grupo_servicio IS NULL THEN '-1' ELSE '0' END as modificado_esp,					 	" +
						   "		   CASE WHEN esci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE esci.cuenta_ingreso_vig_anterior END as cta_ing_vig_ant_esp,	 	" +
						   "		   CASE WHEN esci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE esci.cuenta_ingreso_vig_anterior END as h_cta_ing_vig_ant_esp,	 	" +
						   "		   CASE WHEN esci.cuenta_contable_costo IS NULL THEN 0 ELSE esci.cuenta_contable_costo END as cuentacostoesp,	 	" +
						   "		   CASE WHEN esci.cuenta_contable_costo IS NULL THEN 0 ELSE esci.cuenta_contable_costo END as cuentacostoespho	 	" +
						   "  		   FROM especi_serv_cue_ing esci  " +
						   "  		   				   WHERE esci.especialidad_servicio = -1 " +
						   "							 AND esci.centro_costo = " + centroCosto + 
						   "							 AND esci.grupo_servicio = " + grupoServicio +
						   "						 	 AND esci.tipo_servicio = '" + tipoServicio + "'" +
//						   "  		   					 GROUP BY s.especialidad, e.nombre, esci.cuenta_ingreso " +
						   " 				 )	x ORDER BY x.nombre_especialidad						   ";
			}
			break;
			case 3:  //-- Consultar los servicios.
			{
				consulta = "	SELECT s.codigo as codigo_servicio,  getnombreservicio(s.codigo, " + ConstantesBD.codigoTarifarioCups +  ") as nombre_servicio,  						 " +
						   "	 	   CASE WHEN sci.cuenta_ingreso IS NULL THEN 0 ELSE sci.cuenta_ingreso END as cuentaingreso_ser,			 " +	 	 
						   "		   CASE WHEN sci.cuenta_ingreso IS NULL THEN 0 ELSE sci.cuenta_ingreso END as h_cuentaingreso_ser, 		 " +	 	 
						   "		   CASE WHEN sci.servicio IS NULL THEN '-1' ELSE '0' END as modificado_ser, 							 " +
						   "		   CASE WHEN sci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE sci.cuenta_ingreso_vig_anterior END as cta_ing_vig_ant_ser,	 	" +
						   "		   CASE WHEN sci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE sci.cuenta_ingreso_vig_anterior END as h_cta_ing_vig_ant_ser,	 	" +
						   "		   CASE WHEN sci.cuenta_contable_costo IS NULL THEN 0 ELSE sci.cuenta_contable_costo END as cuentacostoser,	 	" +
						   "		   CASE WHEN sci.cuenta_contable_costo IS NULL THEN 0 ELSE sci.cuenta_contable_costo END as cuentacostoserho	 	" +
						   "		   FROM servicios s																				 			 " +			
						   "				INNER JOIN especialidades e ON ( e.codigo = s.especialidad )										 " +
						   "				INNER JOIN tipos_servicio ts ON ( ts.acronimo = s.tipo_servicio )									 " +
						   "				LEFT OUTER JOIN servicio_cuenta_ingreso sci ON ( s.codigo = sci.servicio AND sci.centro_costo = ? )	 " +
						   "						  WHERE s.grupo_servicio = ? 																 " +
						   "							AND ts.acronimo = ? 																	 " +	
						   "				       		AND e.codigo = ? 																		 " +
						   "  		   					 ORDER BY nombre_servicio ";
			}
			break;
			/*case 4:  //--  Cargar Los Tipos De Cuentas Contable Parametrizados.
			{
				consulta = "	SELECT to_number(codigo, '999999999999999999') as codigo, descripcion as nombre	 " +
						   "		   FROM cuentas_contables 					 " +			
						   "			    WHERE institucion = " + institucion + 	
						   "				  AND activo = " + ValoresPorDefecto.getValorTrueParaConsultas()+ 
						   "					  ORDER BY descripcion	";
			}
			break;*/
		}
		
		try
		{

			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			switch (tipoGrupo)
			{
				case 0:  {
					 		logger.info("VLR DEL CENTRO DE COSTO------->"+centroCosto); 
					 		logger.info("ENTRE AL CASE 0");
						   pst.setInt(1, centroCosto);
						   pst.setInt(2, centroCosto);
						 } break;
				case 1:  {
							logger.info("ENTRE AL CASE 1");
					/*		pst.setInt(1, centroCosto);
							pst.setInt(2, grupoServicio);
							pst.setInt(3, grupoServicio);
							pst.setInt(4, grupoServicio);
							pst.setInt(5, centroCosto); */
						 } break;
				case 2:  {
					logger.info("ENTRE AL CASE 2");
						/*	pst.setInt(1, centroCosto);
							
							pst.setInt(2, grupoServicio);
							pst.setString(3, tipoServicio);
							
							pst.setString(4, tipoServicio); //-Acronimo
							pst.setInt(5, grupoServicio);*/
						 } break;
				case 3:  {
							logger.info("ENTRE AL CASE 3");
							pst.setInt(1, centroCosto);
							pst.setInt(2, grupoServicio);
							pst.setString(3, tipoServicio); //-Acronimo
							pst.setInt(4, especialidad); 
						 } break;
			}

			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarGrupos de SqlBaseCuentaServicioDao: "+e); 
			return null;
		}

	}

	/**
	 * Metodo para insertar los grupos.
	 * @param con
	 * @param centroCosto
	 * @param codigoTipoGrupo
	 * @param codigoGrupo 
	 * @param cuentaIngreso
	 * @param modificado
	 * @param acronimo 
	 * @param tipoSerSel 
	 * @return
	 */
	public static int insertarDatos(Connection con, int codigoTipoGrupo,  int centroCosto, int codigoGrupo, String cuentaIngreso, boolean modificado, String acronimo, String tipoSerSel , String cuentaVigenciaAnterior, String cuentaCosto)
	{
		StringBuffer cadIngreso = new StringBuffer(); 
		//logger.info("\n\nEL VALOR DE LA CTA DE COSTO-------->"+cuentaCosto+"<--------\n\n");
		try
		{
			
			if ( codigoTipoGrupo == 0 ) //-Se ingresan GRUPOS DE SERVICIOS 
			{
				if (!modificado)
				{ 
					cadIngreso.append(" INSERT INTO grupo_servicio_cue_ingr (" +
															"grupo_servicio, " +
															"centro_costo, " +
															"cuenta_ingreso , " +
															"cuenta_ingreso_vig_anterior," +
															"cuenta_contable_costo " +
															"  ) VALUES (?,?,?,?,?) ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, codigoGrupo);
					stm.setInt(2, centroCosto);
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					//Cambios Anexo 809
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirADouble(cuentaCosto)>0 )
					{
						logger.info("paso por aqui");
						stm.setDouble(5, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
					
					
					return stm.executeUpdate();
				}
				else
				{ 
					logger.info("\n\n\n\nENTRE A MODIFICAR!--------->\n"+cuentaIngreso+"\n"+cuentaVigenciaAnterior+"\n"+cuentaCosto);
					
					
					cadIngreso.append(" UPDATE grupo_servicio_cue_ingr SET  " +
															"cuenta_ingreso = ? , " +
															"cuenta_ingreso_vig_anterior = ?, " +
															"cuenta_contable_costo = ? " +
															"WHERE centro_costo = ? " +
															"AND grupo_servicio = ? ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC ); }
		
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(2, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0)
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }

					stm.setInt(4, centroCosto);
					stm.setInt(5, codigoGrupo);

					return stm.executeUpdate();
				}
				
			}

			if ( codigoTipoGrupo == 1 ) //-Se ingresan TIPOS DE SERVICIOS 
			{
				logger.info("voy a ingresar la cta de costo insert--->"+cuentaCosto);
				if (!modificado)
				{ 
					cadIngreso.append(" INSERT INTO tipo_servicio_cuenta_ing ( grupo_servicio, tipo_servicio, centro_costo, cuenta_ingreso, cuenta_ingreso_vig_anterior, cuenta_contable_costo  ) VALUES (?, ?, ?, ? , ?, ?) ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, codigoGrupo);
					stm.setString(2, acronimo);
					stm.setInt(3, centroCosto);
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(5, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(6, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(6, Types.NUMERIC ); }
					
					
					return stm.executeUpdate();
				}
				else
				{ 
					logger.info("voy a ingresar la cta de costo update--->"+cuentaCosto);
					cadIngreso.append(" UPDATE tipo_servicio_cuenta_ing  SET  cuenta_ingreso = ?, cuenta_ingreso_vig_anterior = ?, cuenta_contable_costo = ? WHERE centro_costo = ? AND tipo_servicio = ? AND grupo_servicio = ? ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(2, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					stm.setInt(4, centroCosto);
					stm.setString(5, acronimo);
					stm.setInt(6, codigoGrupo);
					
					return stm.executeUpdate();
				}
			}
			if ( codigoTipoGrupo == 2 ) //-Se ingresan ESPECIALIDADES.
			{
				if (!modificado)
				{ 
					cadIngreso.append(" INSERT INTO especi_serv_cue_ing ( grupo_servicio, tipo_servicio, especialidad_servicio, centro_costo, cuenta_ingreso , cuenta_ingreso_vig_anterior, cuenta_contable_costo ) VALUES (?,?,?,?,?, ?,?) ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1,Utilidades.convertirAEntero(acronimo));  //-El grupo...
					stm.setString (2, tipoSerSel);
					stm.setInt(3, codigoGrupo);
					stm.setInt(4, centroCosto);
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(5, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(6, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(6, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(7, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(7, Types.NUMERIC ); }
		
					return stm.executeUpdate();
				}
				else
				{ 
					cadIngreso.append(" UPDATE especi_serv_cue_ing  SET  cuenta_ingreso = ?, cuenta_ingreso_vig_anterior = ?, cuenta_contable_costo = ?  WHERE centro_costo = ? AND especialidad_servicio = ? AND grupo_servicio = ? AND  tipo_servicio = ? ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(2, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					
					stm.setInt(4, centroCosto);
					stm.setInt(5, codigoGrupo);
					stm.setInt(6,Utilidades.convertirAEntero(acronimo));  //-El grupo...
					stm.setString(7, tipoSerSel);
						
					return stm.executeUpdate();
				}
				
			}
			if ( codigoTipoGrupo == 3 ) //-Se ingresan Servicios.
			{
				if (!modificado)
				{ 
					cadIngreso.append(" INSERT INTO servicio_cuenta_ingreso (servicio, centro_costo, cuenta_ingreso, cuenta_ingreso_vig_anterior, cuenta_contable_costo  ) VALUES (?,?,?,?,?) ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, codigoGrupo);
					stm.setInt(2, centroCosto);
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(5, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
	
					return stm.executeUpdate();
				}
				else
				{ 
					cadIngreso.append(" UPDATE servicio_cuenta_ingreso  SET  cuenta_ingreso = ?, cuenta_ingreso_vig_anterior = ?, cuenta_contable_costo = ?  WHERE centro_costo = ? AND servicio = ? ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC ); }

					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(2, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaCosto) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					stm.setInt(4, centroCosto);
					stm.setInt(5, codigoGrupo);

					
					return stm.executeUpdate();
				}
				
			}
			
			
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando EN (insertarDatos) EN SqlBaseCuentaServicioDao  consutal [" + cadIngreso.toString() + "] : "+e);
			return ConstantesBD.codigoNuncaValido;
		}		return 0;
	}

	/**
	 * Metodo para eliminar la cuenta contable.
	 * @param con
	 * @param tablaDestino
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @param servicio
	 * @return
	 */
	public static int eliminar(Connection con, int tablaDestino, int centroCosto, int grupoServicio, String tipoServicio, int especialidad, int servicio) 
	{
		String consulta = ""; 
		PreparedStatementDecorator stm = null;

		try
		{
			
			if ( tablaDestino == 0 ) //-Se eliminan GRUPOS DE SERVICIOS 
			{
				consulta = " DELETE FROM grupo_servicio_cue_ingr WHERE grupo_servicio = ? AND centro_costo = ? ";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, grupoServicio);
				stm.setInt(2, centroCosto);
			}

			if ( tablaDestino == 1 ) //-Se eliminan TIPOS DE SERVICIOS 
			{
				consulta = " DELETE FROM tipo_servicio_cuenta_ing WHERE grupo_servicio = ? AND tipo_servicio = ? AND centro_costo = ? ";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, grupoServicio);
				stm.setString(2, tipoServicio);
				stm.setInt(3, centroCosto); 
			}
			
			if ( tablaDestino == 2 ) //-Se eliminan ESPECIALIDADES.
			{
				consulta = " DELETE FROM especi_serv_cue_ing WHERE grupo_servicio = ? AND tipo_servicio = ? AND especialidad_servicio = ? AND centro_costo = ? ";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, grupoServicio);
				stm.setString(2, tipoServicio);
				stm.setInt(3, especialidad);
				stm.setInt(4, centroCosto);
			}
			
			if ( tablaDestino == 3 ) //-Se eliminan Servicios.
			{
				consulta = " DELETE FROM servicio_cuenta_ingreso WHERE servicio = ? AND centro_costo = ? ";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, servicio);
				stm.setInt(2, centroCosto);
			}

			
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en (Eliminar) EN SqlBaseCuentaServicioDao: Consulta [" + consulta + "] : Error ["+e+"]\n\n");
			return ConstantesBD.codigoNuncaValido;
		}		
	}

	/**
	 * Metodo para eliminar un registro cuando las dos cuentas contables estan en nulo
	 * @param con
	 * @param tablaEliminar
	 * @return
	 */
	public static int eliminarCuentasContablesNulas(Connection con, int tablaEliminar)
	 {
		String cad = ""; 
		PreparedStatementDecorator ps = null;
		int resp1=-1;
			//Borrar los registros que quedaron sin cuenta_ingreso y cuenta_ingreso_vig_anterior
			//-Se elimina la cuenta contable de la clase inventario
		
				if ( tablaEliminar == 0 ) //-Se elimina la cuenta contable de la clase inventario 
				{		
						cad = " DELETE FROM  grupo_servicio_cue_ingr WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL and cuenta_contable_costo is null";
				}
				else if ( tablaEliminar == 1 ) //-Se elimina la cuenta contable del grupo inventario 
				{		
						cad = " DELETE FROM  tipo_servicio_cuenta_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL and cuenta_contable_costo is null";
				}
				else if ( tablaEliminar == 2 ) //-Se elimina la cuenta contable del Sub-grupo inventario 
				{		
						cad = " DELETE FROM  especi_serv_cue_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL and cuenta_contable_costo is null";
				}	
				else if ( tablaEliminar == 3 ) //-Se elimina la cuenta contable del Articulo inventario 
				{		
						cad = " DELETE FROM  servicio_cuenta_ingreso WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL and cuenta_contable_costo is null";
				}					
				////////////////////////////////
				try {
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
				} catch (SQLException e) {
					logger.warn(e+" Error en la eliminacion de datos en eliminarCuentasContablesNulas : SqlBaseCuentaServicioDao "+e.toString() );
					resp1 = ConstantesBD.codigoNuncaValido;
				}
				
				try {
					resp1= ps.executeUpdate();
				} catch (SQLException e) {
					logger.warn(e+" Error en la eliminacion de datos en eliminarCuentasContablesNulas : SqlBaseCuentaServicioDao "+e.toString() );
					resp1 = ConstantesBD.codigoNuncaValido;
				}
				return resp1;
				

	}
	
}
