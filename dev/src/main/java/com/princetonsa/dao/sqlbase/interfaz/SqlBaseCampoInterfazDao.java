/*
 * Creado en Apr 26, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;

public class SqlBaseCampoInterfazDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCampoInterfazDao.class);
	
	/**
	 * Método para cargar la información general de la parametrización de campos interfaz 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Collection cargarInformacionGral(Connection con, int institucion)
	{
		String consultaStr="SELECT ciig.institucion,  ciig.nombre_archivo_salida,  ciig.path_archivo_salida,  ciig.nombre_archivo_incons,  " +
															"ciig.path_archivo_incons,  ciig.separador_campos,  ciig.separador_decimales,  ciig.identificador_fin_archivo," +
															" ciig.pres_devolucion_paciente,  ciig.valor_negativo_devol_pac,  ciig.descripcion_debito," +
															" ciig.descripcion_credito, tsd.nombre AS nom_separador_decimales " +
												" FROM campos_interfaz_info_gral ciig " +
													" INNER JOIN tipos_separador_decimal tsd ON (tsd.codigo=ciig.separador_decimales) " +
														" WHERE institucion="+institucion;
		try
		{
         	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			
 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado la Información general de la parametrización CamposInterfaz (SqlBaseCampoInterfazDao) :"+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para guardar la información general de la parametrización de los campos de interfaz
	 * @param con
	 * @param esInsertar
	 * @param institucion
	 * @param nombreArchivoSalida
	 * @param pathArchivoSalida
	 * @param nombreArchivoInconsistencias
	 * @param pathArchivoInconsistencias
	 * @param separadorCampos
	 * @param separadorDecimales
	 * @param identificadorFinArchivo
	 * @param presentaDevolucionPaciente
	 * @param valorNegativoDevolPaciente
	 * @param descripcionDebito
	 * @param descripcionCredito
	 * @return
	 */
		public static int insertarActualizarInfoGral(Connection con, boolean esInsertar, int institucion, String nombreArchivoSalida, String pathArchivoSalida, 
																			String nombreArchivoInconsistencias, String pathArchivoInconsistencias, String separadorCampos, 
																			int separadorDecimales, String identificadorFinArchivo, String presentaDevolucionPaciente, String valorNegativoDevolPaciente, 
																			String descripcionDebito, String descripcionCredito)
		{
			PreparedStatementDecorator ps;
			int resp=-1;
			String consultaStr = "";
			
			try
			{
				//--------------Se inserta la información general ------------------//
				if (esInsertar)
					{
						consultaStr="INSERT INTO campos_interfaz_info_gral " +
														"(institucion,nombre_archivo_salida,path_archivo_salida,nombre_archivo_incons," +
														 "path_archivo_incons,separador_campos,separador_decimales,identificador_fin_archivo," +
														 "pres_devolucion_paciente,valor_negativo_devol_pac,descripcion_debito," +
														 "descripcion_credito)" +
												" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
						
						ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, institucion);
						ps.setString(2, nombreArchivoSalida);
						ps.setString(3, pathArchivoSalida);
						ps.setString(4, nombreArchivoInconsistencias);
						ps.setString(5, pathArchivoInconsistencias);
						ps.setString(6, separadorCampos);
						ps.setInt(7, separadorDecimales);
						ps.setString(8, identificadorFinArchivo);
						//ps.setBoolean(9, UtilidadTexto.getBoolean(facturasAnuladas));
						ps.setBoolean(9, UtilidadTexto.getBoolean(presentaDevolucionPaciente));
						ps.setBoolean(10, UtilidadTexto.getBoolean(valorNegativoDevolPaciente));
						ps.setString(11, descripcionDebito);
						ps.setString(12, descripcionCredito);
						//ps.setBoolean(14, UtilidadTexto.getBoolean(agruparFacturasValor));
					}
				else
					{
					consultaStr="UPDATE campos_interfaz_info_gral SET " +
												"nombre_archivo_salida=?,  " +
												"path_archivo_salida=?,  " +
												"nombre_archivo_incons=?,  " +
												"path_archivo_incons=?,  " +
												"separador_campos=?,  " +
												"separador_decimales=?,  " +
												"identificador_fin_archivo=?,  " +
												"pres_devolucion_paciente=?,  " +
												"valor_negativo_devol_pac=?,  " +
												"descripcion_debito=?,  " +
												"descripcion_credito=?  " +
										  "WHERE institucion=?";

					ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setString(1, nombreArchivoSalida);
					ps.setString(2, pathArchivoSalida);
					ps.setString(3, nombreArchivoInconsistencias);
					ps.setString(4, pathArchivoInconsistencias);
					ps.setString(5, separadorCampos);
					ps.setInt(6, separadorDecimales);
					ps.setString(7, identificadorFinArchivo);
					//ps.setBoolean(8, UtilidadTexto.getBoolean(facturasAnuladas));
					ps.setBoolean(8, UtilidadTexto.getBoolean(presentaDevolucionPaciente));
					ps.setBoolean(9, UtilidadTexto.getBoolean(valorNegativoDevolPaciente));
					ps.setString(10, descripcionDebito);
					ps.setString(11, descripcionCredito);
					//ps.setBoolean(13, UtilidadTexto.getBoolean(agruparFacturasValor));
					ps.setInt(12, institucion);	
					}
				
				resp = ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción/actualización de datos en insertarActualizarInfoGral : SqlBaseCampoInterfazDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
		}
		
		/**
		 *  Método para consultar los campos de interfaz parametrizados hasta el momento para 
		 * la institución
		 * @param con
		 * @param institucion
		 * @return
		 */
		public static HashMap consultarCamposInterfaz(Connection con, int institucion)
		{
			
			String consultaStr="SELECT cri.codigo AS codigo, cri.tipo_campo_interfaz AS tipo_campo_interfaz, tci.nombre AS nombre_tipo_campo, " +
																"cri.orden_campo AS orden_campo, cri.indicativo_requerido AS indicativo_requerido, 1 AS es_actualizar " +
													"FROM campos_regis_interfaz cri " +
														"INNER JOIN tipos_campo_interfaz tci ON (cri.tipo_campo_interfaz=tci.codigo) " +
															"WHERE cri.institucion="+institucion + "ORDER BY cri.orden_campo";	
						
			//---Columnas---//
			String[] columnas = {"codigo","tipo_campo_interfaz","nombre_tipo_campo","orden_campo", "indicativo_requerido", "es_actualizar"};
			
			try
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
				return listado.getMapa();
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarCamposInterfaz de SqlBaseCampoInterfazDao: "+e);
				return null;
			}
		}
		
		/**
		 * Método para insertar/actualizar la información ingresada en la segunda sección
		 * de campos de interfaz
		 * @param con
		 * @param esInsertar
		 * @param institucion
		 * @param tipoCampoInterfaz
		 * @param ordenCampo
		 * @param indicativoRequerido
		 * @param ordenCampoAnt
		 * @return
		 */
		public static int insertarActualizarCamposInterfaz(Connection con, boolean esInsertar, int institucion, int tipoCampoInterfaz, int ordenCampo, String indicativoRequerido, int ordenCampoAnt)
		{
			PreparedStatementDecorator ps;
			int resp=-1;
			String consultaStr = "";
			
			
			try
			{
				//--------------Se inserta el nuevo campo de interfaz parametrizado ------------------//
				if (esInsertar)
					{
						consultaStr="INSERT INTO campos_regis_interfaz (codigo," +
																		"tipo_campo_interfaz," +
																		"orden_campo," +
																		"indicativo_requerido," +
																		"institucion) " +
																	"VALUES (?, ?, ?, ?, ?) " ;
						
						DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						int codigo=myFactory.incrementarValorSecuencia(con, "seq_campos_regis_interfaz");
						
						ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigo);
						ps.setInt(2, tipoCampoInterfaz);
						ps.setInt(3, ordenCampo);
						ps.setBoolean(4, UtilidadTexto.getBoolean(indicativoRequerido));
						ps.setInt(5, institucion);
					}
				else
					{
					consultaStr="UPDATE campos_regis_interfaz SET " +
															"tipo_campo_interfaz=?, " +
															"orden_campo=?, " +
															"indicativo_requerido=? " +
													" WHERE orden_campo=? " +
													"AND institucion=? "; 

					ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setInt(1, tipoCampoInterfaz);
					ps.setInt(2, ordenCampo);
					ps.setBoolean(3, UtilidadTexto.getBoolean(indicativoRequerido));
					ps.setInt(4, ordenCampoAnt);
					ps.setInt(5, institucion);
					}
				
				resp = ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción/actualización de datos en insertarActualizarCamposInterfaz : SqlBaseCampoInterfazDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
		}
}
