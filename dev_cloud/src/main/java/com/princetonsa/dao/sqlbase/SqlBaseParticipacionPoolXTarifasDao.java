/*
 * @(#)SqlBaseParticipacionPoolXTarifasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la participación pool X tarifa
 *
 * @version 1.0, Nov 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseParticipacionPoolXTarifasDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseParticipacionPoolXTarifasDao.class);

	/**
	 * Consulta la información de la participación del pool X tarifa
	 */
	private static String listadoParticipacionPoolXTarifaStr= 	"SELECT pet.esquema_tarifario AS codigoEsquemaTarifario, " +
																								"pet.institucion AS institucion," +
																								"et.nombre AS nombreEsquemaTarifario, " +
																								"pet.porcentaje_participacion AS porcentajeParticipacion, " +
																								"pet.valor_participacion AS valorParticipacion ," +
																								"CASE WHEN pet.cuenta_contable_pool IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pet.cuenta_contable_pool END AS cuentaPool, " +
																								"CASE WHEN pet.cuenta_contable_ins IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pet.cuenta_contable_ins END AS cuentaInstitucion, " +
																								"CASE WHEN pet.cue_cont_inst_vig_anterior IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pet.cue_cont_inst_vig_anterior END AS cuentaInstitucionAnterior, " +
																								"'" + ConstantesBD.valorTrueEnString + "' AS estaBD,  " +
																								"'" + ConstantesBD.valorFalseEnString + "' AS esEliminada " +
																								"FROM pooles_esquema_tarifario pet, " +
																								"esquemas_tarifarios et " +
																								"WHERE pet.esquema_tarifario=et.codigo "+
																								"AND pet.pool = ? AND pet.institucion=?";
	
	/**
	 * Order by del listado
	 */
	private static String orderDelListado=" ORDER BY nombreEsquemaTarifario ";
	
	/**
	 * Inserta una participación del pool X tarifas
	 */
	private static String insertarPoolXTarifasStr= 	"INSERT INTO pooles_esquema_tarifario " +
																		"(pool, esquema_tarifario, porcentaje_participacion, " +
																		"cuenta_contable_pool, cuenta_contable_ins, cue_cont_inst_vig_anterior, institucion, usuario_modifica, fecha_modifica, hora_modifica, valor_participacion ) " +
																		"VALUES(?, ?, ?, ?, ? , ? ,? ,? ,? ,? ,? ) "; //11

	/**
	 * Inserta una participación del pool X Convenios
	 */
	private static String strCadenaInsercionPoolesXConvenio= 	"INSERT INTO pooles_convenio " +
																		"(pool, convenio, porcentaje_participacion, " +
																		"cuenta_contable_pool, cuenta_contable_ins, cue_cont_inst_vig_anterior, institucion, usuario_modifica, fecha_modifica, hora_modifica, valor_participacion) " +
																		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	
	/**
	 * Modifica una participación del pool X tarifas
	 */
	private static String modificarPoolXtarifasStr= 	"UPDATE pooles_esquema_tarifario SET " +
																			"esquema_tarifario = ?, " +
																			"porcentaje_participacion = ?, " +
																			"cuenta_contable_pool = ?, " +
																			"cuenta_contable_ins = ?," +
																			"usuario_modifica = ?," +
																			"fecha_modifica = ?," +
																			"hora_modifica = ?," +
																			"valor_participacion= ?," +
																			"cue_cont_inst_vig_anterior=?  WHERE  pool = ? AND esquema_tarifario = ? AND institucion = ? ";
	
	
	
	/**
	 * Modifica una participación del pool X tarifas
	 */
	private static String strCadenaModificaPoolXConvenio= 	"UPDATE pooles_convenio SET " +
																			"convenio = ?, " +
																			"porcentaje_participacion = ?, " +
																			"cuenta_contable_pool = ?, " +
																			"cuenta_contable_ins = ?," +
																			"usuario_modifica = ?," +
																			"fecha_modifica = ?," +
																			"hora_modifica = ?," +
																			"cue_cont_inst_vig_anterior=?," +
																			"valor_participacion=? " +
																			"WHERE  pool = ? AND convenio = ? AND institucion = ? ";
	
	/**
	 * Elimina una participación del pool X tarifas
	 */
	private final static String eliminarPoolXTarifasStr=	"DELETE FROM pooles_esquema_tarifario WHERE " +
																				"pool = ? AND esquema_tarifario = ? AND institucion = ?";
	
	
	/**
	 * Elimina una participación del pool X Convenio
	 */
	private final static String strCadenaEliminaPoolXConvenio=	"DELETE FROM pooles_convenio WHERE " +
																				"pool = ? AND convenio = ? AND institucion = ?";
	
	/**
	 * participacion pool por tarifario
	 */
	private final static String consultarParticipacionPoolXTarifaStr="SELECT porcentaje_participacion AS porcentaje FROM pooles_esquema_tarifario WHERE pool=? AND esquema_tarifario=? AND institucion = ?";
	
	/**
	 * Consula participacion pool por convenio
	 * Adicionado por Jhony Alexander Duque A.
	 */
	private static String strCadenaConsultaPartPoolXConv= 	"SELECT pc.convenio AS codigo_convenio, " +
						"c.nombre AS nombre_convenio, " +
						"pc.institucion AS institucion, " +
						"pc.pool AS codigo_pool," +
						"pc.porcentaje_participacion AS porcentaje_participacion, " +
						"pc.valor_participacion AS valor_participacion, " +
						"CASE WHEN pc.cuenta_contable_pool IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pc.cuenta_contable_pool END AS cuenta_pool, " +
						"CASE WHEN pc.cuenta_contable_ins IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pc.cuenta_contable_ins END AS cuenta_institucion, " +
						"CASE WHEN pc.cue_cont_inst_vig_anterior IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE pc.cue_cont_inst_vig_anterior END AS cuenta_institucion_anterior, " +
						 "'"+ConstantesBD.valorFalseEnString+"' AS es_eliminada, " +
						 "'" +ConstantesBD.valorTrueEnString+"' AS esta_b_d  " +
						"FROM pooles_convenio pc " +
						"INNER JOIN convenios c ON (c.codigo=pc.convenio) " +
						"WHERE pc.pool=? AND pc.institucion=?";
						
	
	public static String [] indicesPoolXConv = {"codigoConvenio_","nombreConvenio_","institucion_","porcentajeParticipacion_", "valorParticipacion_" ,"cuentaPool_","cuentaInstitucion_","cuentaInstitucionAnterior_","estaBD_","esEliminada_","codigoPool_"};
	
	/**
	 * Este metodo se encarga de Consultar los datos de la tabla participacion_pooles_x_tarifas
	 * Los key's del HashMap parametros son:
	 * ---------------------------------
	 * 		KEY'S DE PARAMETROS
	 * ---------------------------------
	 * --institucion --> Requerido
	 * --pool --> Requerido
	 * --convenio --> Opcional
	 * 
	 * Los key del mapa que devuelve son:
	 * -------------------------------
	 *  KEY'S DEL MAPA QUE DEVUELVE
	 * ------------------------------
	 * --codigoConvenio, nombreConvenio, institucion,
	 * porcentajeParticipacion, cuentaPool, cuentaInstitucion,
	 * cuentaInstitucionAnterior, estabd.
	 * @param connection
	 * @param parametros
	 * @return
	 **/
	public static HashMap consultarParticipacionPoolesXConvenio (Connection connection, HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsultaPartPoolXConv;
		
		if (parametros.containsKey("convenio") && !parametros.get("convenio").toString().equals(""))
			cadena += " AND convenio="+Integer.parseInt(parametros.get("convenio").toString());
			
		logger.info("cadena de consutla de consultarParticipacionPoolesXConvenio ==> "+cadena);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Integer.parseInt( parametros.get("pool").toString()));
			ps.setInt(2,Integer.parseInt( parametros.get("institucion").toString()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);	
			
		}
		catch (Exception e)
		{
			e.printStackTrace(); 	
		}
		
				
		mapa.put("INDICES_MAPA",indicesPoolXConv);
		
		return mapa;
	}
	
	
	/**
	 * Modifica una participación del pool X tarifas
	 * @param con
	 * @param codigoEsquemaTarifarioNuevo
	 * @param porcentajeParticipacion
	 * @param cuentaContablePool
	 * @param cuentaContableInstitucion
	 * @param codigoPool
	 * @param codigoEsquemaTarifarioAntiguo
	 * @return
	 */
	public static int modificar(			Connection con, 
													int codigoEsquemaTarifarioNuevo,
													double porcentajeParticipacion, 
													int cuentaContablePool,
													int cuentaContableInstitucion,
													int codigoPool,
													int codigoEsquemaTarifarioAntiguo,
													int cuentaContableInstitucionAnterior,
													int institucion,
													double valorParticipacion,
													String usuario) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarPoolXtarifasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setInt(1,codigoEsquemaTarifarioNuevo );
				
				if(porcentajeParticipacion<0)
				{
				ps.setNull(2,Types.DOUBLE);	
				}
				else
				{
				ps.setDouble(2, porcentajeParticipacion);
				}
				
				if(cuentaContablePool==ConstantesBD.codigoNuncaValido)
				    ps.setObject(3, null);
				else
				    ps.setInt(3, cuentaContablePool);
							
				if(cuentaContableInstitucion== ConstantesBD.codigoNuncaValido)
				    ps.setObject(4,null);
				else
				    ps.setInt(4, cuentaContableInstitucion);
				
				ps.setString(5, usuario);
				ps.setObject(6,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				ps.setObject(7,UtilidadFecha.getHoraActual());
				
				if( valorParticipacion>=0)
				{
					ps.setDouble(8, valorParticipacion);
				}
				else
				{
					ps.setNull(8, Types.DOUBLE);
				}
				if(cuentaContableInstitucionAnterior== ConstantesBD.codigoNuncaValido)
				    ps.setObject(9,null);
				else
				    ps.setInt(9, cuentaContableInstitucionAnterior);
				
				ps.setInt(10, codigoPool);
				ps.setInt(11, codigoEsquemaTarifarioAntiguo);
				ps.setInt(12, institucion);
				
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Modifica Los datos de participacion pooles por convenio
	 * el HashMap parametros contiene los siguientes key's
	 * -------------------------------------
	 * 			KEY'S PARAMETROS
	 * -------------------------------------
	 * --codigoConvenio --> Requerido
	 * --porcentajeParticipacion --> Requerido
	 * --cuentaPool --> Requerido
	 * --cuentaInstitucion --> Requerido
	 * --usuario --> Requerido
	 * --cuentaInstitucionAnterior --> Requerido
	 * --codigoPool --> Requerido
	 * --institucion --> Requerido
	 * -----------------------------------------
	 * Connection con
	 * HashMap parametros
	 * @return true si es exitoso.
	 * */
	public static boolean ModificaConvenio(Connection con, HashMap parametros)
	{		
		logger.info("\n\n *********************entre al sqlbase a ModificaConvenio ****************************\n\n");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaModificaPoolXConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
					
			
			ps.setInt(1,Integer.parseInt(parametros.get("codigoConvenio").toString()));	
			if(Utilidades.convertirADouble(parametros.get("porcentajeParticipacion").toString())>=0)
			{
				ps.setDouble(2,Double.parseDouble(parametros.get("porcentajeParticipacion").toString()));	
			}
			else
			{
				ps.setNull(2, Types.DOUBLE);
			}
			
			if ( parametros.get("cuentaPool")!=null && !parametros.get("cuentaPool").toString().equals("-1"))
				ps.setInt(3,Integer.parseInt(parametros.get("cuentaPool").toString()));
			else
				ps.setNull(3, Types.NULL);
			
			if (parametros.get("cuentaInstitucion")!=null && !parametros.get("cuentaInstitucion").toString().equals("-1"))
				ps.setInt(4,Integer.parseInt(parametros.get("cuentaInstitucion").toString()));
			else
				ps.setNull(4, Types.NULL);
			
			ps.setString(5, parametros.get("usuario").toString());
			ps.setObject(6,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(7,UtilidadFecha.getHoraActual());
			
			if (parametros.get("cuentaInstitucionAnterior") !=null && !parametros.get("cuentaInstitucionAnterior").toString().equals("-1"))
				ps.setInt(8,Integer.parseInt(parametros.get("cuentaInstitucionAnterior").toString()));		
			else
				ps.setNull(8, Types.NULL);
			
			if(Utilidades.convertirADouble(parametros.get("valorParticipacion").toString())>=0)
			{
				ps.setDouble(9,Double.parseDouble(parametros.get("valorParticipacion").toString()));	
					
			}
			else
			{
				ps.setNull(9, Types.DOUBLE);
			}
			ps.setInt(10,Integer.parseInt(parametros.get("codigoPool").toString()));
			ps.setInt(11,Integer.parseInt(parametros.get("codigoConvenioOld").toString()));	
			ps.setInt(12,Integer.parseInt(parametros.get("institucion").toString()));	
			
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
	 * inserta Los datos de participacion pooles por convenio
	 * el HashMap parametros contiene los siguientes key's
	 * -------------------------------------
	 * 			KEY'S PARAMETROS
	 * -------------------------------------
	 * --codigoConvenio --> Requerido
	 * --porcentajeParticipacion --> Requerido
	 * --cuentaPool --> Opcional
	 * --cuentaInstitucion --> Opcional
	 * --usuario --> Requerido
	 * --cuentaInstitucionAnterior --> Opcional
	 * --codigoPool --> Requerido
	 * --institucion --> Requerido
	 * -----------------------------------------
	 * Connection con
	 * HashMap parametros
	 * @return true si es exitoso.
	 * */
	
	public static boolean insertarPoolXConvenio(Connection connection, HashMap parametros)
	{
		try
		{   
			logger.info("\n\n entro a insertarPoolXConvenio "+parametros);
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("--"+Utilidades.convertirADouble(parametros.get("porcentajeParticipacion").toString()));
			logger.info("--"+Utilidades.convertirADouble(parametros.get("valorParticipacion").toString()));
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			logger.info("********************************************************************************");
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(strCadenaInsercionPoolesXConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Integer.parseInt(parametros.get("codigoPool").toString()));
			ps.setInt(2,Integer.parseInt(parametros.get("codigoConvenio").toString()));	
			if(Utilidades.convertirADouble(parametros.get("porcentajeParticipacion").toString())>=0)
			{
				ps.setDouble(3,Double.parseDouble(parametros.get("porcentajeParticipacion").toString()));	
			}
			else
			{
				ps.setNull(3, Types.NUMERIC);
			}
			
			if ( parametros.get("cuentaPool")!=null && !parametros.get("cuentaPool").toString().equals("-1"))
				ps.setInt(4,Integer.parseInt(parametros.get("cuentaPool").toString()));
			else
				ps.setNull(4, Types.NULL);
			
			if (parametros.get("cuentaInstitucion")!=null && !parametros.get("cuentaInstitucion").toString().equals("-1"))
				ps.setInt(5,Integer.parseInt(parametros.get("cuentaInstitucion").toString()));
			else
				ps.setNull(5, Types.NULL);
			
			if (parametros.get("cuentaInstitucionAnterior") !=null && !parametros.get("cuentaInstitucionAnterior").toString().equals("-1"))
				ps.setInt(6,Integer.parseInt(parametros.get("cuentaInstitucionAnterior").toString()));		
			else
				ps.setNull(6, Types.NULL);
			
			ps.setInt(7,Integer.parseInt(parametros.get("institucion").toString()));		
			ps.setString(8, parametros.get("usuario").toString());
			ps.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(10,UtilidadFecha.getHoraActual());
			
			if(Utilidades.convertirADouble(parametros.get("valorParticipacion").toString())>=0)
			{
				ps.setDouble(11,Double.parseDouble(parametros.get("valorParticipacion").toString()));	
			}
			else
			{
				ps.setNull(11, Types.NUMERIC);
			}
			
		if (ps.executeUpdate()>0)
				return true;
			}catch (SQLException e)
			{
				e.printStackTrace();
			}
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Eliminar una participación del pool X tarifas
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static int eliminar(	Connection con, 
	        								int codigoPool,
											int codigoEsquemaTarifario,
											int institucion) 
	{
		int resp=0;	
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarPoolXTarifasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1, codigoPool);
			ps.setInt(2, codigoEsquemaTarifario);
			ps.setInt(3, institucion);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Eliminar una participación del pool X Convenio
	 * @param con
	 * @param codigoPool
	 * @param codigoConvenio
	 * @return
	 */
	public static int eliminarXConvenio(	Connection con, 
	        								int codigoPool,
											int convenio,
											int institucion) 
	{
		int resp=0;	
		
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strCadenaEliminaPoolXConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1, codigoPool);
			ps.setInt(2, convenio);
			ps.setInt(3, institucion);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 *  Inserta una participación de pool por tarifas
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @param cuentaContablePool
	 * @param cuentaContableInstitucion
	 * @return
	 */
	public static int  insertar(		Connection con,
												int codigoPool,
												int codigoEsquemaTarifario,
												double porcentajeParticipacion,
												int cuentaContablePool,
												int cuentaContableInstitucion, 
												int cuentaContableInstitucionAnterior,
												int institucion,
												double valorParticipacion,
												String usuario)
	{
	    int resp=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarPoolXTarifasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoPool);
			ps.setInt(2, codigoEsquemaTarifario);
			
			if(porcentajeParticipacion<0)
			{
			ps.setNull(3,Types.DOUBLE);	
			}
			else
			{
			ps.setDouble(3, porcentajeParticipacion);
			}
						
			if(cuentaContablePool==ConstantesBD.codigoNuncaValido)
			    ps.setObject(4, null);
			else
			    ps.setInt(4, cuentaContablePool);
			
			
			if(cuentaContableInstitucion==ConstantesBD.codigoNuncaValido)
			    ps.setObject(5, null);
			else
			    ps.setInt(5, cuentaContableInstitucion);
			
			if(cuentaContableInstitucionAnterior==ConstantesBD.codigoNuncaValido)
			    ps.setObject(6, null);
			else
			    ps.setInt(6, cuentaContableInstitucionAnterior);
			
			ps.setInt(7, institucion);
			
			if(usuario=="")
			    ps.setString(8, null);
			else
				 ps.setString(8, usuario);
			ps.setObject(9,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(10,UtilidadFecha.getHoraActual());
			if(valorParticipacion>=0)
			{
				ps.setDouble(11, valorParticipacion);
			}
			else
			{
				ps.setNull(11, Types.DOUBLE);
			}
			
			logger.info("INSERTAR POOLES");
			logger.info("********************************************************************************************");
			logger.info(ps);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseParticipacionPoolXTarifasDao "+e.toString() );
			resp=0;
		}
		return resp;
	}
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public static ResultSetDecorator listadoParticipacionPoolXTarifa(Connection con, int codigoPool,int institucion)
	{
	    try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoParticipacionPoolXTarifaStr + orderDelListado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPool);
			cargarStatement.setInt(2, institucion);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de requisitos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Búsqueda Avanzada de la participación del pool X tarifa dados los criterios
	 * (pool- esquemaTarifario- %participacion)  
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public static ResultSetDecorator busquedaAvanzadaPoolXTarifa(	Connection con, int codigoPool, 
	        																			int codigoEsquemaTarifario, double porcentajeParticipacion,int institucion )
	{
	    String consulta=listadoParticipacionPoolXTarifaStr;
	    
	    if(codigoEsquemaTarifario>0)
	        consulta+=" AND pet.esquema_tarifario=? ";
	    
	    if(porcentajeParticipacion>=0)
	        consulta+="AND  pet.porcentaje_participacion=? ";
	    
	    consulta+=orderDelListado;
	    
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPool);
			cargarStatement.setInt(2, institucion);
			if(codigoEsquemaTarifario>0)
			    cargarStatement.setInt(3,codigoEsquemaTarifario);
			if(codigoEsquemaTarifario>0 && porcentajeParticipacion>=0)
			    cargarStatement.setDouble(4,porcentajeParticipacion);
			else if(codigoEsquemaTarifario<=0 && porcentajeParticipacion>=0)
			    cargarStatement.setDouble(3,porcentajeParticipacion);
			    
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la busqueda Avanzada de la descripción de requisitos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método que busca los codigos de los esquemas tarifarios del pool que no han sido
	 * consultados en la búsqueda avanzada y son necesarios para la comparación de los
	 * mapas ya que el cod del esq hace parte del primary key.
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public static ResultSetDecorator datosNoCargadosConBusquedaAvanzada( 	Connection con, int codigoPool, 
	        																							int codigoEsquemaTarifario, double porcentajeParticipacion )
	{
	    String consulta= "SELECT esquema_tarifario AS codigoEsquemaTarifario " +
	    						"FROM pooles_esquema_tarifario WHERE esquema_tarifario  " +
	    						"NOT IN (" +
	    								"SELECT pet.esquema_tarifario AS codigoEsquemaTarifario " +
	    								"FROM pooles_esquema_tarifario pet,  " +
	    								"esquemas_tarifarios et " +
	    								"WHERE pet.esquema_tarifario=et.codigo " +
	    								"AND pet.pool =? ";
	    								//"AND pet.esquema_tarifario=10) GROUP BY esquema_tarifario"
	    if(codigoEsquemaTarifario>0)
	        consulta+=" AND pet.esquema_tarifario=? ";
	    
	    if(porcentajeParticipacion>=0)
	        consulta+="AND  pet.porcentaje_participacion=? ";
	    
	    consulta+=") GROUP BY esquema_tarifario ";
	    
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPool);
			if(codigoEsquemaTarifario>0)
			    cargarStatement.setInt(2,codigoEsquemaTarifario);
			if(codigoEsquemaTarifario>0 && porcentajeParticipacion>=0)
			    cargarStatement.setDouble(3,porcentajeParticipacion);
			else if(codigoEsquemaTarifario<=0 && porcentajeParticipacion>=0)
			    cargarStatement.setDouble(2,porcentajeParticipacion);
			    
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la busqueda Avanzada de la descripción de requisitos: SqlBaseParticipacionPoolXTarifasDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método que consulta un porcentaje de participación para un
	 * pool y un esquema tarifario específico
	 * @param con
	 * @param pool
	 * @param esquemaTarifario
	 */
	public static double consultarParticipacionPoolXTarifa(Connection con, int pool, int esquemaTarifario,int institucion)
	{
		PreparedStatementDecorator stm;
		try
		{
			stm =  new PreparedStatementDecorator(con.prepareStatement(consultarParticipacionPoolXTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, pool);
			stm.setInt(2, esquemaTarifario);
			stm.setInt(3, institucion);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getDouble("porcentaje");
			}
			else
			{
				return -1;
			}
		}
		catch (SQLException e)
		{
			logger.error("error consultando la participacion del pool "+pool+" para el esquema tarifario "+esquemaTarifario+" "+e);
			return -2;
		}
		
	}

}
