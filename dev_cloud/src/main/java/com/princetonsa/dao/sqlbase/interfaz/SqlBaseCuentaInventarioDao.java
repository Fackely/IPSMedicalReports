/*
 * Creado en Apr 18, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
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
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.Utilidades;

public class SqlBaseCuentaInventarioDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuentaInventarioDao.class);
	
	/**
	 * Método para consultar las clases de inventario parametrizados para el centro de costo y 
	 * la institución con la información ingresada de la cuenta de ingreso.
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param institucion
	 * @return HashMap
	 */
	public static HashMap consultarClaseInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int institucion)
	{
		
		StringBuffer consultaStr = new StringBuffer();
		
		consultaStr.append("SELECT ci.codigo AS cod_clase_inventario, ci.nombre AS nom_clase_inventario, " +
															"cici.cuenta_ingreso AS cuenta_ingreso, cici.centro_costo AS centro_costo, CASE WHEN cici.centro_costo IS NULL THEN 0 ELSE 1 END AS es_modificar , cici.cuenta_ingreso_vig_anterior as cuenta_ingreso_vig_anterior "  +
												" FROM clase_inventario ci " +
													"LEFT OUTER JOIN clase_invent_cuenta_ing cici ON (cici.clase_inventario=ci.codigo AND cici.centro_costo ="+centroCostoSeleccionado + ")");
		
		consultaStr.append(" WHERE ci.institucion="+institucion +" ORDER BY ci.nombre");
		
		//---Columnas---//
		String[] columnas = {"cod_clase_inventario","nom_clase_inventario","cuenta_ingreso","centro_costo", "es_modificar" , "cuenta_ingreso_vig_anterior"};
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarClaseInventariosCuentaIngreso de SqlBaseCuentainventarioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que carga los grupos de inventario para el centro de costo seleccionado y la 
	 * clase de inventario seleccionada
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param claseInventarioSeleccionado
	 * @return HashMap
	 */
	public static HashMap consultarGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int claseInventarioSeleccionado)
	{
		
		StringBuffer consultaStr = new StringBuffer();
		
		consultaStr.append("SELECT gi.codigo AS cod_grupo_inventario, gi.nombre AS nom_grupo_inventario, gici.cuenta_ingreso AS cuenta_ingreso, " +
															"gici.centro_costo AS centro_costo, CASE WHEN gici.centro_costo IS NULL THEN 0 ELSE 1 END AS es_modificar, gici.cuenta_ingreso_vig_anterior as cuenta_ingreso_vig_anterior " +
												" FROM grupo_inventario gi " +
													"LEFT OUTER JOIN grupo_inv_cuenta_ing gici ON (gici.grupo_inventario=gi.codigo AND gici.centro_costo ="+centroCostoSeleccionado + ")");
		
		consultaStr.append(" WHERE gi.clase="+claseInventarioSeleccionado + " ORDER BY gi.nombre");
		
		//---Columnas---//
		String[] columnas = {"cod_grupo_inventario","nom_grupo_inventario","cuenta_ingreso","centro_costo", "es_modificar", "cuenta_ingreso_vig_anterior"};
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarGrupoInventariosCuentaIngreso de SqlBaseCuentainventarioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que carga los subgrupos de inventario para el centro de costo seleccionado y el 
	 * grupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param grupoInventarioSeleccionado
	 * @return HashMap
	 */
	public static HashMap consultarSubGrupoInventariosCuentaIngreso(Connection con, int centroCostoSeleccionado, int grupoInventarioSeleccionado)
	{
		
		StringBuffer consultaStr = new StringBuffer();
		
		consultaStr.append("SELECT sgi.codigo AS cod_subgrupo_inventario, sgi.nombre AS nom_subgrupo_inventario, " +
															"sgici.cuenta_ingreso AS cuenta_ingreso, sgici.centro_costo AS centro_costo, CASE WHEN sgici.centro_costo IS NULL THEN 0 ELSE 1 END AS es_modificar , sgici.cuenta_ingreso_vig_anterior as cuenta_ingreso_vig_anterior " +
												  "FROM subgrupo_inventario sgi " +
												  		"LEFT OUTER JOIN subgrupo_inv_cuenta_ing sgici ON (sgici.subgrupo_inventario=sgi.codigo AND sgici.centro_costo="+centroCostoSeleccionado+")");
		
		consultaStr.append(" WHERE sgi.grupo="+grupoInventarioSeleccionado + " ORDER BY sgi.nombre");
		
		//---Columnas---//
		String[] columnas = {"cod_subgrupo_inventario","nom_subgrupo_inventario","cuenta_ingreso","centro_costo", "es_modificar" , "cuenta_ingreso_vig_anterior"};
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarSubGrupoInventariosCuentaIngreso de SqlBaseCuentainventarioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que carga los articulos de inventario para el centro de costo seleccionado y el 
	 * subgrupo de inventario seleccionado
	 * @param con
	 * @param centroCostoSeleccionado
	 * @param subgrupoInventarioSel
	 * @return
	 */
	public static HashMap consultarArticulosInventarioCuentaIngreso(Connection con, int centroCostoSeleccionado, int subgrupoInventarioSel)
	{
		
		StringBuffer consultaStr = new StringBuffer();
		
		consultaStr.append("SELECT ai.codigo AS cod_articulo_inventario, ai.descripcion AS nom_articulo_inventario, aici.cuenta_ingreso AS cuenta_ingreso, " +
															"aici.centro_costo AS centro_costo, CASE WHEN aici.centro_costo IS NULL THEN 0 ELSE 1 END AS es_modificar , aici.cuenta_ingreso_vig_anterior as cuenta_ingreso_vig_anterior  " +
													"FROM articulo ai " +
														"LEFT OUTER JOIN articulo_inv_cuenta_ing aici ON (aici.articulo=ai.codigo AND aici.centro_costo="+centroCostoSeleccionado + ")");
		
		consultaStr.append(" WHERE ai.subgrupo="+subgrupoInventarioSel + " ORDER BY ai.descripcion");
		
		//---Columnas---//
		String[] columnas = {"cod_articulo_inventario","nom_articulo_inventario","cuenta_ingreso","centro_costo", "es_modificar", "cuenta_ingreso_vig_anterior"};
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarArticulosInventarioCuentaIngreso de SqlBaseCuentainventarioDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método para insertar o modificar la cuenta de ingreso de una clase de 
	 * inventario para el centro de costo seleccionado
	 * @param con
	 * @param centroCosto
	 * @param claseInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @return
	 */
	@SuppressWarnings("unused")
	public static int insertarActualizarClaseInventario(Connection con, int centroCosto, int claseInventario, String cuentaIngresoNueva, int esInsertar, String cuentaVigenciaAnterior)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		int resp1=-1;
		String cad = "";
		try
		{
			//--------------Se inserta la cuenta de ingreso ------------------//
			if (esInsertar==1)
				{
					cad="INSERT INTO clase_invent_cuenta_ing (" +
											"centro_costo, " +
											"clase_inventario, " +
											"cuenta_ingreso, " +
											"cuenta_ingreso_vig_anterior ) VALUES (?, ?, ?, ?)" ;
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setInt(1, centroCosto);
					ps.setInt(2, claseInventario);
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.trim().equals(""))
						ps.setNull(3, Types.NUMERIC);
					else
						ps.setDouble(3, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(4, Types.NUMERIC);
					else
						ps.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnterior));					
				}
			else
				{
					cad="UPDATE clase_invent_cuenta_ing SET " +
												"cuenta_ingreso = ? , " +
												"cuenta_ingreso_vig_anterior = ? " +
												"WHERE centro_costo=? " +
												"AND clase_inventario=?";
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.trim().equals(""))
						ps.setNull(1, Types.NUMERIC);
					else
						ps.setDouble(1, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(2, Types.NUMERIC);
					else
						ps.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior));	
					
					ps.setInt(3, centroCosto);
					ps.setInt(4, claseInventario);
				}
			
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/actualización de datos en Clase Inventario Cuenta Ingreso : SqlBaseCuentaInventarioDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}

		return resp;
		
	
	}
	
	/**
	 * Método para insertar o modificar la cuenta de ingreso de un grupo de 
	 * inventario para el centro de costo y clase de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param grupoInventario
	 * @param claseInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @return
	 */
	public static int insertarActualizarGrupoInventario(Connection con, int centroCosto, int grupoInventario, int claseInventario, String cuentaIngresoNueva, int esInsertar , String cuentaVigenciaAnterior)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		String cad = "";
		
		try
		{
			//--------------Se inserta la cuenta de ingreso ------------------//
			if (esInsertar==1)
				{
					cad="INSERT INTO grupo_inv_cuenta_ing (" +
													"centro_costo, " +
													"grupo_inventario, " +
													"clase_inventario, " +
													"cuenta_ingreso, " +
													"cuenta_ingreso_vig_anterior ) VALUES (?, ?, ?, ?, ?)" ;
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, centroCosto);
					ps.setInt(2, grupoInventario);
					ps.setInt(3, claseInventario);
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(4, Types.NUMERIC);
					else
						ps.setDouble(4, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.equals(""))
						ps.setNull(5, Types.NUMERIC);
					else
						ps.setDouble(5, Utilidades.convertirADouble(cuentaVigenciaAnterior));
					
				}
			else
				{
					cad="UPDATE grupo_inv_cuenta_ing SET " +
											"cuenta_ingreso = ? , " +
											"cuenta_ingreso_vig_anterior = ? " +
											"WHERE centro_costo=? " +
											"AND clase_inventario=? " +
											"AND grupo_inventario=?";
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(1, Types.NUMERIC);
					else
						ps.setDouble(1, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.equals(""))
						ps.setNull(2, Types.NUMERIC);
					else
						ps.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior));
					
					ps.setInt(3, centroCosto);
					ps.setInt(4, claseInventario);
					ps.setInt(5, grupoInventario);
				}
			
			
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/actualización de datos en Grupo Inventario Cuenta Ingreso : SqlBaseCuentaInventarioDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método para insertar o modificar la cuenta de ingreso de un subgrupo de 
	 * inventario para el centro de costo, clase y grupo de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param subGrupoInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @return
	 */
	public static int insertarActualizarSubGrupoInventario(Connection con, int centroCosto, int subGrupoInventario, String cuentaIngresoNueva, int esInsertar , String cuentaVigenciaAnterior)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		String cad = "";
		
		try
		{
			//--------------Se inserta la cuenta de ingreso ------------------//
			if (esInsertar==1)
				{
					cad="INSERT INTO subgrupo_inv_cuenta_ing (" +
										"centro_costo, " +
										"subgrupo_inventario, " +
										"cuenta_ingreso, " +
										"cuenta_ingreso_vig_anterior) VALUES (?, ?, ?, ?)" ;
					
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, centroCosto);
					ps.setInt(2, subGrupoInventario);
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(3, Types.NUMERIC);
					else
						ps.setDouble(3, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(4, Types.NUMERIC);
					else
						ps.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnterior));					

				}
			else
				{
					cad="UPDATE subgrupo_inv_cuenta_ing SET " +
													"cuenta_ingreso = ? , " +
													"cuenta_ingreso_vig_anterior = ? " +
													"WHERE centro_costo=? " +
													"AND subgrupo_inventario=?";
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(1, Types.NUMERIC);
					else
						ps.setDouble(1, Utilidades.convertirADouble(cuentaIngresoNueva));
					/////////////////pilas//////////////////////////////////////////
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(2, Types.NUMERIC);
					else
						ps.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior));	

					
					ps.setInt(3, centroCosto);
					ps.setInt(4, subGrupoInventario);
				}
			
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/actualización de datos en SubGrupo Inventario Cuenta Ingreso : SqlBaseCuentaInventarioDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método para insertar o modificar la cuenta de ingreso de un articulo de 
	 * inventario para el centro de costo, clase, grupo  y subgrupo de inventario seleccionado
	 * @param con
	 * @param centroCosto
	 * @param articuloInventario
	 * @param cuentaIngresoNueva
	 * @param esInsertar
	 * @return
	 */
	public static int insertarActualizarArticuloInventario(Connection con, int centroCosto, int articuloInventario, String cuentaIngresoNueva, int esInsertar, String cuentaVigenciaAnterior)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		String cad = "";
		
		try
		{
			//--------------Se inserta la cuenta de ingreso ------------------//
			if (esInsertar==1)
				{
					cad="INSERT INTO articulo_inv_cuenta_ing (" +
												"centro_costo, " +
												"articulo, " +
												"cuenta_ingreso, " +
												"cuenta_ingreso_vig_anterior) VALUES (?, ?, ?, ?)" ;
					
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, centroCosto);
					ps.setInt(2, articuloInventario);
					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(3, Types.NUMERIC);
					else
						ps.setDouble(3, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(4, Types.NUMERIC);
					else
						ps.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnterior));					
					
				}
			else
				{
					cad="UPDATE articulo_inv_cuenta_ing SET " +
													"cuenta_ingreso = ? , " +
													"cuenta_ingreso_vig_anterior = ?  " +
													"WHERE centro_costo=? " +
													"AND articulo=?";
					
					
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

					if(cuentaIngresoNueva.equals(ConstantesBD.codigoNuncaValido+"")||cuentaIngresoNueva.equals(""))
						ps.setNull(1, Types.NUMERIC);
					else
						ps.setDouble(1, Utilidades.convertirADouble(cuentaIngresoNueva));
					
					if(cuentaVigenciaAnterior.equals(ConstantesBD.codigoNuncaValido+"")||cuentaVigenciaAnterior.trim().equals(""))
						ps.setNull(2, Types.NUMERIC);
					else
						ps.setDouble(2, Utilidades.convertirADouble(cuentaVigenciaAnterior));	

					
					ps.setInt(3, centroCosto);
					ps.setInt(4, articuloInventario);
				}
			
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/actualización de datos en Articulo Inventario Cuenta Ingreso : SqlBaseCuentaInventarioDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método que elimina de acuerdo al valor enviado en el parámetro tabla eliminar, 
	 * la cuenta contable respectiva 
	 * tablaEliminar 1 -> ClaseInventario
	 * 				 2 -> GrupoInventario
	 * 				 3 -> SubGrupoInventario
	 * 				 4 -> ArticuloInventario
	 * @param con
	 * @param tablaEliminar
	 * @param centroCosto
	 * @param claseInventario
	 * @param grupoInventario
	 * @param subGrupoInventario
	 * @param articuloInventario
	 * @return
	 */
	public static int eliminarCuentaContable(Connection con, int tablaEliminar, int centroCosto, int claseInventario, int grupoInventario, int subGrupoInventario, int articuloInventario)
	 {
		String consulta = ""; 
		PreparedStatementDecorator stm = null;
		
		try
		{
			if ( tablaEliminar == 1 ) //-Se elimina la cuenta contable de la clase inventario 
			{
				consulta = " DELETE FROM clase_invent_cuenta_ing WHERE centro_costo = ? AND clase_inventario = ? ";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, centroCosto);
				stm.setInt(2, claseInventario);
			}
			
			if ( tablaEliminar == 2 ) //-Se elimina la cuenta contable del grupo inventario 
			{
				consulta = " DELETE FROM grupo_inv_cuenta_ing WHERE centro_costo = ? AND grupo_inventario = ?";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, centroCosto);
				stm.setInt(2, grupoInventario);
			}
			
			if ( tablaEliminar == 3 ) //-Se elimina la cuenta contable del sub-grupo inventario 
			{
				consulta = " DELETE FROM subgrupo_inv_cuenta_ing WHERE centro_costo = ? AND subgrupo_inventario = ?";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, centroCosto);
				stm.setInt(2, subGrupoInventario);
			}
			
			if ( tablaEliminar == 4 ) //-Se elimina la cuenta contable del articulo inventario 
			{
				consulta = " DELETE FROM articulo_inv_cuenta_ing WHERE centro_costo = ? AND articulo = ?";
				stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setInt(1, centroCosto);
				stm.setInt(2, articuloInventario);
			}
			
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en SqlBaseCuentaInventarioDao (eliminarCuentaContable): Consulta [" + consulta + "] : Error ["+e+"]\n\n");
			return ConstantesBD.codigoNuncaValido;
		}	
	 }

	/**
	 * Método que elimina un registro si las dos cuentas contables estan nulas
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
		
				if ( tablaEliminar == 1 ) //-Se elimina la cuenta contable de la clase inventario 
				{		
						cad = " DELETE FROM  clase_invent_cuenta_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL";
				}
				else if ( tablaEliminar == 2 ) //-Se elimina la cuenta contable del grupo inventario 
				{		
						cad = " DELETE FROM  grupo_inv_cuenta_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL";
				}
				else if ( tablaEliminar == 3 ) //-Se elimina la cuenta contable del Sub-grupo inventario 
				{		
						cad = " DELETE FROM  subgrupo_inv_cuenta_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL";
				}	
				else if ( tablaEliminar == 4 ) //-Se elimina la cuenta contable del Articulo inventario 
				{		
						cad = " DELETE FROM  articulo_inv_cuenta_ing WHERE  cuenta_ingreso IS NULL AND cuenta_ingreso_vig_anterior IS NULL";
				}					
				////////////////////////////////
				try {
					ps= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
				} catch (SQLException e) {
					logger.warn(e+" Error en la eliminacion de datos en eliminarCuentasContablesNulas : SqlBaseCuentaInventarioDao "+e.toString() );
					resp1 = ConstantesBD.codigoNuncaValido;
				}
				
				try {
					resp1= ps.executeUpdate();
				} catch (SQLException e) {
					logger.warn(e+" Error en la eliminacion de datos en eliminarCuentasContablesNulas : SqlBaseCuentaInventarioDao "+e.toString() );
					resp1 = ConstantesBD.codigoNuncaValido;
				}
				return resp1;
				

	}
	
}
