/*
 * Creado en Oct 28, 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class SqlBasePreanestesiaDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBasePreanestesiaDao.class);
	
	/**
	 * Método para saber si existe o no una una preanestesia dada
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @return peticionCirugia si existe sino retorna -1
	 */
	public static int existePreanestesia (Connection con, int peticionCirugia)
	{
		String consultaStr="SELECT peticion_qx as peticion_qx " +
												"FROM valoracion_preanestesia " +
													"WHERE peticion_qx = ? ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, peticionCirugia);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("peticion_qx");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Vericando Existencia de la Preanestesia : SqlBasePreanestesiaDao "+e.toString());
			return -1;
		}
	}
	
	/**
	 * Método para saber si existe o no una la conclusión de preanestesia
	 * @param con -> conexion
	 * @param valPreanestesia
	 * @param concluPreInst
	 * @return valPreanestesia si existe sino retorna -1
	 */
	public static int existeConclusion (Connection con, int valPreanestesia, int concluPreInst)
	{
		String consultaStr="SELECT val_preanestesia AS val_preanestesia " +
												"FROM val_preanestesia_conclu " +
													"WHERE val_preanestesia = ? AND tipo_conclu_pre_inst = ? ";

		try
			{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, valPreanestesia);
			ps.setInt(2, concluPreInst);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
			if(resultado.next())
				{
				return resultado.getInt("val_preanestesia");
				}
			else
				{
				return -1;
				}	
			}
		catch(SQLException e)
			{
			logger.warn(e+" Vericando Existencia de la Conclusión de Preanestesia : SqlBasePreanestesiaDao "+e.toString());
			return -1;
			}
	}
	
	/**
	 * Método para consultar el código del encabezado del exámen de laboratorio  cuando es parametrizado (Sobrecargado)
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @param examLabInst
	 * @return codigo
	 */
	public static int consultaCodigoEncabezadoExamenLab (Connection con, int peticionCirugia, int examLabInst)
	{
		String consultaStr="SELECT vpel.codigo as codigo FROM val_preanestesia_exam_lab vpel " +
												"INNER JOIN val_pre_exam_lab_par vpelp ON (vpelp.val_exam_preanestesia=vpel.codigo) " +
													"WHERE vpel.val_preanestesia=? AND vpelp.examen_lab_pre_inst=? ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, peticionCirugia);
			ps.setInt(2, examLabInst);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Consultando el codigo del encabezado del examen de laboratorio : SqlBasePreanestesiaDao "+e.toString());
			return -1;
		}
	}
	
	/**
	 * Método para consultar el código del encabezado del exámen de laboratorio cuando es otro (Sobrecargado)
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @param descripcionOtro
	 * @return codigo
	 */
	public static int consultaCodigoEncabezadoExamenLab (Connection con, int peticionCirugia, String descripcionOtro)
	{
		String consultaStr="SELECT vpel.codigo as codigo FROM val_preanestesia_exam_lab vpel " +
												"INNER JOIN val_pre_exam_lab_otro vpelo ON (vpelo.val_exam_preanestesia=vpel.codigo) " +
													"WHERE vpel.val_preanestesia=? AND vpelo.nombre=?";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, peticionCirugia);
			ps.setString(2, descripcionOtro);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Consultando el codigo del encabezado del examen de laboratorio : SqlBasePreanestesiaDao "+e.toString());
			return -1;
		}
	}
	
	/**
	 * Método para insertar la valoración de preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionCirugia
	 * @param fechaPreanestesia
	 * @param horaPreanestesia
	 * @param datosMedico
	 * @param tipoAnestesia
	 * @param observacionesGrales
	 * @param login
	 * @return peticionCirugia
	 */
	public static int insertarValoracionPreanestesia (Connection con, int peticionCirugia, String fechaPreanestesia, String horaPreanestesia, 
																					String datosMedico, int tipoAnestesia, String observacionesGrales, String login)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String consultaStr = "";
		
		try
		{
			codigo = existePreanestesia(con,peticionCirugia);
			if ( codigo != -1  )  
			{  
				//-Si existe se modifica
				consultaStr = " UPDATE valoracion_preanestesia SET fecha_preanestesia = ?, hora_preanestesia = ?, " +
															"observaciones_grales = ?, tipo_anestesia = ? " +
																" WHERE peticion_qx = ?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, fechaPreanestesia);
				ps.setString(2, horaPreanestesia);					
				ps.setString(3, observacionesGrales);
				if (tipoAnestesia == 0)	{ ps.setNull(4,Types.INTEGER); } else { ps.setInt(4,tipoAnestesia);  }
				ps.setInt (5, peticionCirugia);
			}
			else
			{
				consultaStr = " INSERT INTO valoracion_preanestesia (peticion_qx, " +
																	  "fecha_preanestesia, " +
																	  "hora_preanestesia, " +
																	  "datos_medico, " +
																	  "usuario, " +
																	  "tipo_anestesia, " +
																	  "observaciones_grales, " +
																	  "fecha_grabacion, " +
																	  "hora_grabacion )" +
																	  " VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, peticionCirugia);				
				ps.setString(2, fechaPreanestesia);								
				ps.setString(3, horaPreanestesia);
				ps.setString(4, datosMedico);
				ps.setString(5, login);
				if (tipoAnestesia == 0)	{ ps.setNull(6,Types.INTEGER); } else { ps.setInt(6,tipoAnestesia);  }
				ps.setString(7, observacionesGrales);

			}
			resp = ps.executeUpdate();
			
			if (resp > 0)
			{
				resp = peticionCirugia;
			}
					
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos en la Preanestesia : SqlBasePreanestesiaDao "+e.toString() );
				resp = 0;
		}
		return resp;
	}
	
	/**
	 * Método para insertar el encabezado del exámen de laboratorio de preanestesia, que se utiliza para
	 * los exámes parametrizados por institución así como los ingresados.
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionQx
	 * @param resultados
	 * @param observaciones
	 * @param esInsertar
	 * 	@param examenLabPreInst
	 * @param descripcionOtro
	 * @param datosMedico
	 * @return codEncaExamenLab
	 */
	public static int insertarEncabezadoExamenLab (Connection con, int peticionQx , String resultados, String observaciones, boolean esInsertar, int examenLabPreInst, String descripcionOtro, String datosMedico)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String consultaStr="";
		
	
		if(esInsertar)
		{
		consultaStr = 	"INSERT INTO val_preanestesia_exam_lab(codigo, " +
																									  			  "val_preanestesia, " +
																												  "resultados, " +
																												  "observaciones," +
																												  "datos_medico) " +
																												  " VALUES " +
																												  " (?, ?, ?, ?, ?) ";
		}
		else
		{
			consultaStr = "UPDATE val_preanestesia_exam_lab SET resultados=?, observaciones=?, datos_medico=? " +
														" WHERE val_preanestesia = ? AND codigo=?";
		}

		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						if (esInsertar)
						{
						DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						codigo=myFactory.incrementarValorSecuencia(con, "seq_val_preanestesia_exam_lab");
						
						ps.setInt(1, codigo);
						ps.setInt(2, peticionQx);
						ps.setString(3, resultados);
						ps.setString(4, observaciones);
						ps.setString(5, datosMedico);
						}
						else
						{
							//Si es un examen de laboratorio parametrizado
							if(examenLabPreInst!=-1)
								{
								//Se consulta el código del encabezado del examen de laboratorio parametrizado sobrecargando el método
								codigo=consultaCodigoEncabezadoExamenLab(con, peticionQx, examenLabPreInst);
								}
							else
							{
								//Se consulta el código del encabezado del otro examen de laboratorio sobrecargando el método
								codigo=consultaCodigoEncabezadoExamenLab(con, peticionQx, descripcionOtro);
							}
							
							ps.setString(1, resultados);
							ps.setString(2, observaciones);
							ps.setString(3, datosMedico);
							ps.setInt(4, peticionQx);
							ps.setInt(5, codigo);
							
						}//else
						
					
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codigo;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos en el Encabezado del Examen de Laboratorio de Preanestesia : insertarEncabezadoExamenLab "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Método para insertar el detalle del examen de laboratorio parametrizado en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param examenLabPreInst
	 * @return valExamenLabPre
	 */
	public static int insertarExamenLabParametrizado (Connection con, int valExamenLabPre, int  examenLabPreInst)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO val_pre_exam_lab_par	(	val_exam_preanestesia, " +
																								  	  					"examen_lab_pre_inst) " +
																														" VALUES " +
																														" (?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, valExamenLabPre);
						ps.setInt(2, examenLabPreInst);
											
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = valExamenLabPre;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos del Examen de Laboratorio parametrizado : insertarExamenLabParametrizado "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Método para insertar el detalle del otro exámen de laboratorio en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param nombreOtro
	 * @return valExamenLabPre
	 */
	public static int insertarExamenLabOtro (Connection con, int valExamenLabPre, String  nombreOtro)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = 	"INSERT INTO val_pre_exam_lab_otro	(	val_exam_preanestesia, " +
																								  	  						"nombre) " +
																															" VALUES " +
																															" (?, ?) ";
		
		
		try	{					
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, valExamenLabPre);
						ps.setString(2, nombreOtro);
											
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = valExamenLabPre;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos del Otro Examen de Laboratorio : insertarExamenLabOtro "+e.toString() );
						resp = 0;
				}
								
				return resp;
	}
	
	/**
	 * Método para insertar el documento adjunto del exámen de laboratorio
	 * @param con una conexion abierta con una fuente de datos
	 * @param codValExamenLab
	 * @param docRealAdj
	 * @param docGeneradoAdj
	 * @return codValExamenLab
	 */
	public static int insertarAdjuntoExamenLabPreanestesia (Connection con, int codValExamenLab, String  docRealAdj, String docGeneradoAdj)
	{
		PreparedStatementDecorator ps;
		int resp=0;
		
		String insertarStr = 	"INSERT INTO adjunto_exam_lab_pre	( 	val_exam_preanestesia, " +
																													  	"nombre_real, " +
																														"nombre_generico) " +
																														" VALUES " +
																														" (?, ?, ?) ";
		
		try
			{
					ps=  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codValExamenLab);
					ps.setString(2, docRealAdj);
					ps.setString(3, docGeneradoAdj);
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del archivo adjunto de Examenes de Laboratorio de Preanestesia: SqlBasePreanestesiaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * Método para consultar los tipos aplicables para la Preanestesia según la institución
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @param nroConsulta
	 * @return Collection
	 */
	public static Collection consultarTipoParametrizado (Connection con, int institucion, int peticionQx, int nroConsulta)
	{
		String consultaStr="";
		
		String consultaExamFisicos="SELECT efpi.codigo AS codigo,tefp.nombre AS nombre " +
																"FROM tipos_examen_fisico_pre tefp " +
																	" INNER JOIN examen_fisico_pre_inst efpi ON (efpi.examen_fisico_pre=tefp.codigo)" +
																		" WHERE efpi.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
																			" AND efpi.institucion = ? ";
		
		//Seleccionar el tipo de informacion a consultar
		
	    switch(nroConsulta)
		{
	    	//Consulta de Tabla de parametrización de Examenes de Laboratorio de Preanestesia por institución
	        case 1:
	        	consultaStr="SELECT * FROM (SELECT elpi.codigo AS codigo, telp.nombre AS nombre, 0 AS orden, elpi.codigo AS cod_exam  " +
	        								"FROM tipos_examen_lab_pre telp " +
												"INNER JOIN examen_lab_pre_inst elpi ON (elpi.examen_lab_pre=telp.codigo) " +
													" WHERE elpi.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
														" AND elpi.institucion = ? " +
										"UNION SELECT -1 AS codigo, vpelo.nombre AS nombre, 1 AS orden, vpelo.val_exam_preanestesia AS cod_exam " +
											"FROM val_pre_exam_lab_otro vpelo INNER JOIN val_preanestesia_exam_lab vpel ON (vpelo.val_exam_preanestesia=vpel.codigo) " +
												"WHERE vpel.val_preanestesia = ?)x ORDER BY x.orden, x.cod_exam";
	        	break;
	        
	        //Consulta de Tabla de parametrización de los Exámenes Físicos de Preanestesia por institucion de tipo text
			case 2:
			       {
				     consultaStr = "  SELECT efpi.codigo || '' AS codigo, tefp.nombre AS nombre, '' as valor " + 
					 			   " 		 FROM tipos_examen_fisico_pre tefp "+ 
								   "			  INNER JOIN examen_fisico_pre_inst efpi ON (efpi.examen_fisico_pre=tefp.codigo) " +
								   "					WHERE efpi.activo = "  + ValoresPorDefecto.getValorTrueParaConsultas() +
								   " 					  AND efpi.institucion = ? " + 
								   "					  AND efpi.es_text_area = " + ValoresPorDefecto.getValorFalseParaConsultas() +
								   "  UNION ALL " +  
								   "  SELECT vp.codigo || '_p' as codigo , vp.nombre_otro as nombre, vp.valor as valor " +
								   "		 FROM  val_pre_exam_fisico_otro vp " +
								   "		 	   INNER JOIN  valoracion_preanestesia valpre ON (valpre.peticion_qx = vp.val_preanestesia ) " + 
								   "			  		 WHERE valpre.peticion_qx = ? ";  
			       }
				break;
				
			//Consulta de Tabla de parametrización de los Exámenes Físicos de Preanestesia por institucion de tipo text area
			case 3:
				consultaStr+= consultaExamFisicos +" AND efpi.es_text_area = " + ValoresPorDefecto.getValorTrueParaConsultas();
				break;		
				
		     //Consulta de Tabla de parametrización de las Conclusiones de Preanestesia por institucion
			case 4:
				consultaStr="SELECT pci.codigo AS codigo,tcp.nombre AS nombre " +
											"FROM tipos_conclusion_pre tcp " +
												" INNER JOIN preanestesia_conclu_inst pci ON (pci.tipo_conclusion=tcp.codigo)" +
													" WHERE pci.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
														" AND pci.institucion = ?";
				break;
				
			default :
					{
						logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-4] El valor recibido es "+ nroConsulta + "\n\n" );
						return null;
					}
		}
	    
		try
		{
	     	//System.out.print("\n\n\n La sentencia SQL BASE (tipos)" + consultaStr + " \n\n\n");
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if((nroConsulta == 1) || (nroConsulta == 2)) 
				{
					psConsulta.setInt(1, institucion);
					psConsulta.setInt(2, peticionQx);
				}
			else
				psConsulta.setInt(1, institucion);
				
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error en la consulta nro ->"+nroConsulta+" de tablas de parametrización por institución : "+e.toString());
			return null;
		}
	}
	
	/**
	 * Metodo para consultar los examenes de laboratorio de la preanestesia de acuerdo a la institución y la petición de cirugía
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @return
	 */
	public static HashMap consultarExamenesLaboratorio(Connection con, int institucion, int peticionQx)
	{
		String consultaStr="SELECT * FROM (SELECT elpi.codigo AS codigo, telp.nombre AS nombre, 0 AS orden, elpi.codigo AS cod_exam  " +
	        								"FROM tipos_examen_lab_pre telp " +
												"INNER JOIN examen_lab_pre_inst elpi ON (elpi.examen_lab_pre=telp.codigo) " +
													" WHERE elpi.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
														" AND elpi.institucion = ? " +
										"UNION SELECT -1 AS codigo, vpelo.nombre AS nombre, 1 AS orden, vpelo.val_exam_preanestesia AS cod_exam " +
											"FROM val_pre_exam_lab_otro vpelo INNER JOIN val_preanestesia_exam_lab vpel ON (vpelo.val_exam_preanestesia=vpel.codigo) " +
												"WHERE vpel.val_preanestesia = ?)x ORDER BY x.orden, x.cod_exam";
		
//		columnas
		String[] columnas={	"codigo", "nombre", "orden", "cod_exam"};
			
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			pst.setInt(2,peticionQx);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar examenes de laboratorio de SqlBasePreanestesiaDao: "+e);
			return null;
		}
		
	}
	
	/**
     * Metodo para consultar la preanestesia asociada a la petición generada 
     * @param con
     * @param peticionQx
     * @return Collection con los datos de la Preanestesia
     */
	public static Collection cargarPreanestesia (Connection con, int peticionQx)
	{
		String consultaStr = "SELECT fecha_preanestesia AS fecha_preanestesia, hora_preanestesia AS hora_preanestesia, 	" +
							 "		 tipo_anestesia as tipo_anestesia,  observaciones_grales AS observaciones_grales 	" +
							 "		 FROM valoracion_preanestesia														" +
							 "			  WHERE peticion_qx = ?    														";
		try
		{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			psConsulta.setInt(1, peticionQx);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado la Preanestesia :"+e.toString());
			return null;
		}
	}
	
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes de Laboratorio
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
    public static Collection cargarExamenesLabPre(Connection con, int peticionQx)
    {
    	String consultaStr="";
    	
    	consultaStr = "SELECT * FROM " +
    							 "(SELECT vpelp.examen_lab_pre_inst AS codigo, vpel.resultados AS resultados, vpel.observaciones AS observaciones, 0 AS orden, vpelp.examen_lab_pre_inst AS cod_exam, vpel.datos_medico AS datos_medico " +
    								"FROM val_preanestesia_exam_lab vpel " +
										"INNER JOIN val_pre_exam_lab_par vpelp ON (vpelp.val_exam_preanestesia=vpel.codigo) " +
										"INNER JOIN examen_lab_pre_inst elpi ON (elpi.codigo=vpelp.examen_lab_pre_inst) " +
											"WHERE vpel.val_preanestesia=? AND elpi.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
											" UNION " +
								  "SELECT -1 AS codigo, vpel.resultados AS resultados, vpel.observaciones AS observaciones, 1 AS orden, vpelo.val_exam_preanestesia AS cod_exam,vpel.datos_medico AS datos_medico " +
								  	"FROM val_preanestesia_exam_lab vpel " +
								  		"INNER JOIN val_pre_exam_lab_otro vpelo ON (vpelo.val_exam_preanestesia=vpel.codigo) " +
								  			"WHERE vpel.val_preanestesia=?)x " +
							 	"ORDER BY x.orden, x.cod_exam";
    	
    	
    	try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, peticionQx);
		ps.setInt(2, peticionQx);
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
    	}
		
		catch (SQLException e)
		{
		logger.error("Error Consultado los exámenes de laboratorio de Preanestesia en SqlBase :"+e.toString());
		return null;
		}    	
    	
    }

 /**
     * Metodo para consultar y cargar la información de la sección de conclusiones
     * @param con
     * @param valPreanestesia
     * @return Collection -> Con la información de las conclusiones
     */
    public static Collection cargarConclusiones(Connection con, int valPreanestesia)
    {
    	String consultaStr="";
    	
    	consultaStr = "SELECT vpc.tipo_conclu_pre_inst AS codigo,vpc.conclusion AS valor_conclusion " +
    									"FROM val_preanestesia_conclu vpc " +
    										"INNER JOIN valoracion_preanestesia vp ON (vpc.val_preanestesia=vp.peticion_qx) " +
											"INNER JOIN preanestesia_conclu_inst pci ON (vpc.tipo_conclu_pre_inst=pci.codigo) " +
												"WHERE vpc.val_preanestesia=? AND pci.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
    	
    	try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, valPreanestesia);
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
    	}
		
		catch (SQLException e)
		{
		logger.error("Error Consultado las conclusiones de Preanestesia en SqlBase :"+e.toString());
		return null;
		}    	
    }
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Fisicos
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
    public static Collection cargarExamenesFisicos(Connection con, int peticionQx)
    {
    	String consultaStr="";
    	
    	consultaStr = " SELECT examen_fisico_pre_inst || '' as codigo, valor as valor " +
 					  "		   FROM  val_pre_exam_fisico_text" +
 					  "			 	 WHERE val_preanestesia = ?" +
    				  "	UNION ALL " +      
					  "	SELECT vp.codigo || '_p' as codigo,  vp.valor as valor " +
					  "		   FROM  val_pre_exam_fisico_otro vp " +    
	    		 	  "				 INNER JOIN  valoracion_preanestesia valpre ON (valpre.peticion_qx = vp.val_preanestesia ) " +     
	    			  "					   WHERE valpre.peticion_qx = ? ";

    	
    	
    	
    	try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, peticionQx);
			ps.setInt(2, peticionQx);
		
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
    	}
		
		catch (SQLException e)
		{
		logger.error("Error Consultado los exámenes de laboratorio de Preanestesia en SqlBase :"+e.toString());
		return null;
		}    	
    }

    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Fisicos (de Tipo Text Area)
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
    public static Collection cargarExamenesFisicosArea(Connection con, int peticionQx)
    {
    	String consultaStr="";
    	
    	consultaStr = " SELECT examen_fisico_pre_inst as codigo, valor as valor " +
 					  "		   FROM  val_pre_exam_fisico_area" +
 					  "			 	 WHERE val_preanestesia = ?" +
 					  "					   ORDER BY examen_fisico_pre_inst ";
    	
    	
    	try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, peticionQx);
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
    	}
		
		catch (SQLException e)
		{
		logger.error("Error Consultado los exámenes de laboratorio de Preanestesia en SqlBase :"+e.toString());
		return null;
		}    	
    }
    
    
    /**
     * Metodo para insertar los examenes fisicos 
     * @param con
     * @param tablaDestino : indicador para saber sobre cual tabla se inserta
     * @param codigoOtro
     * @param val_preanestesia
     * @param nombreOtro
     * @param valor
     * @return
     */
    public static int insertarExamenFisico(Connection con, int tablaDestino, int codigoOtro, int val_preanestesia, String nombreOtro, String  valor)
    {
    	String sentencia="";
    	PreparedStatementDecorator ps = null;
		
    	try 
		{
    		switch (tablaDestino)
			{
    		  case 0:
    		  		  sentencia = "	INSERT INTO val_pre_exam_fisico_text ( val_preanestesia, examen_fisico_pre_inst, valor ) " +
								  "								  VALUES ( ?, ?, ? )";
    		  		  ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		  		  ps.setInt(1, val_preanestesia);	
					  ps.setInt(2, codigoOtro);	
					  ps.setString(3, valor);
			  break;
    		  case 1:
    		  		sentencia = "	INSERT INTO val_pre_exam_fisico_area ( val_preanestesia, examen_fisico_pre_inst, valor ) " +
						   		  "								  VALUES ( ?, ?, ? )";
    		  		  ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		  		  ps.setInt(1, val_preanestesia);	
					  ps.setInt(2, codigoOtro);	
					  ps.setString(3, valor);
		      break;
    		  case 2:
    		  		 DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
    		  		 int codigo = myFactory.incrementarValorSecuencia(con, "seq_val_pre_exam_fisico_otro");

    		  		 sentencia = "	INSERT INTO val_pre_exam_fisico_otro ( codigo, val_preanestesia, nombre_otro, valor ) " +
								  "								  VALUES ( ?, ?, ?, ? )";
		    		  ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    		  ps.setInt(1, codigo);	
					  ps.setInt(2, val_preanestesia);	
					  ps.setString(3, nombreOtro);
					  ps.setString(4, valor);
			  break;
    		  case 3:
		  		 sentencia = "	UPDATE val_pre_exam_fisico_area SET valor = ? " +
							 "							     WHERE examen_fisico_pre_inst = ? " +
							 "								   AND val_preanestesia = ?";
	    		  ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    		  ps.setString(1, valor);
	    		  ps.setInt(2, codigoOtro);	
				  ps.setInt(3, val_preanestesia);	
			  break;
			}
    		
		return ps.executeUpdate();
    	}
		catch (SQLException e)
		{
			logger.error(" Error Insertando (insertarExamenFisico) en SqlBase :"+e.toString());
			return 0;
		}    	
    	
    }
    
    /**
	 * Método para insertar una conclusión parametrizada de la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valPreanestesia
	 * @param concluPreInst
	 * @param valorConclusion
	 * @return valPreanestesia
	 */
	public static int insertarConclusionPreanestesia (Connection con, int valPreanestesia, int  concluPreInst, String valorConclusion)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String consultaStr = "";
		
		try
		{
			codigo = existeConclusion (con,valPreanestesia, concluPreInst);
			
			if ( codigo != -1  )  
			{  
				 //-Si existe se modifica
				consultaStr = " UPDATE val_preanestesia_conclu SET conclusion = ? " +
																" WHERE val_preanestesia = ? AND tipo_conclu_pre_inst= ?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, valorConclusion);
				ps.setInt(2, valPreanestesia);					
				ps.setInt(3, concluPreInst);
			}
			else
			{
				consultaStr = " INSERT INTO val_preanestesia_conclu (val_preanestesia, " +
																													  "tipo_conclu_pre_inst, " +
																													  "conclusion) " +
																													   " VALUES (?, ?, ?) ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, valPreanestesia);				
				ps.setInt(2, concluPreInst);								
				ps.setString(3, valorConclusion);
			}
			resp = ps.executeUpdate();
			
			if (resp > 0)
			{
				resp = valPreanestesia;
			}
					
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos en la Conclusión de preanestesia : SqlBasePreanestesiaDao "+e.toString() );
				resp = 0;
		}
		return resp;
	}
	
	 /**
     * Metodo para consultar la información general de la petición de cirugía 
     * @param con
     * @param nroPeticion
     * @return Collection con los datos generales de la Petición
     */
	public static Collection cargarInfoPeticion (Connection con, int nroPeticion)
	{
		String consultaStr = "SELECT pet.codigo AS codigo_peticion, pet.fecha_peticion AS fecha_peticion, pet.hora_peticion AS hora_peticion, " +
															   "pet.fecha_cirugia AS fecha_cirugia, pet.duracion AS duracion_cirugia, ep.nombre AS nom_estado_peticion, ep.codigo AS cod_estado_peticion, " +
															   "getNombrePersona(pet.solicitante) AS nombre_solicitante " +
															   		"FROM peticion_qx pet " +
															   			"INNER JOIN estados_peticion ep ON (pet.estado_peticion=ep.codigo) " +
															   				"WHERE pet.codigo=?";
		
		try
			{
			PreparedStatementDecorator psConsulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			psConsulta.setInt(1, nroPeticion);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(psConsulta.executeQuery()));
			
			} 
		catch (SQLException e)
			{
			logger.error("Error Consultado la información general de la petición :"+e.toString());
			return null;
			}
	}
	
	/**
     * Metodo para obtener el último codigo de los exámenes de laboratorio parametrizados para la institución
     * en la preanestesia  
     * @param con
     * @param institucion
     * @return int -> Ultimo codigo del exámen de laboratorio parametrizado
     */
	public static int obtenerUltimoExamenLabPar (Connection con, int institucion)
	{
		String consultaStr="SELECT MAX(elpi.codigo) AS codigo " +
												"FROM tipos_examen_lab_pre telp " +
													"INNER JOIN examen_lab_pre_inst elpi ON (elpi.examen_lab_pre=telp.codigo) " +
														"WHERE elpi.institucion = ? AND elpi.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			return 0;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del exámen de laboratorio: SqlBasePreanestesiaDao "+e.toString());
			return 0;
		}
	}
		
}
