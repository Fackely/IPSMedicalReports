/*
 * @(#)SqlBaseFormatoImpresionPresupuestoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */


package com.princetonsa.dao.sqlbase;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import java.util.HashMap;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dao.DaoFactory;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de los formatos de impresión del presupuesto
 */
public class SqlBaseFormatoImpresionPresupuestoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseFormatoImpresionPresupuestoDao.class);
	
	
	/**
	 * Cadena con el statement necesario para modificar un formato de impresin basico dado su codigo
	 */
	private static final String modificarFormatoImpresionBasicoStr=" UPDATE  manejopaciente.formato_imp_presupuesto" +
																   " SET nombre_formato = ?, " +
																   " titulo_formato = ?, " +
																   " cantidad = ?, " +
																   " desc_cantidad = ?, " +
																   " valor_unitario = ?, " +
																   " desc_val_unitario = ?, " +
																   " valor_total = ?, " +
																   " desc_val_total = ?, " +
																   " nota_pie_pagina = ?, " +
																   " fecha_hora = ? " +
																   " WHERE codigo= ? ";
	
	/**
	 * Cadena con el statement necesario para insertar el detalle de los servicios de un formato de impresión de presupuesto
	 */
	private static final String insertarDetalleServiciosTempStr=" INSERT INTO facturacion.det_serv_formato_imp " +
															" (codigo_formato," +
															" detalle," +
															" valores_detalle," +
															" subtotal_grupo," +
															" prioridad," +
															" grupo)" +
														    " VALUES ( ? , ? , ? , ? , ? , ?)";
	
	/**
	 * Statement para consultar los formatos de impresion existentes
	 */
	private final static String consultarFormatosExistentesStr=" SELECT fip.codigo as codigo, " +
														  	   " fip.nombre_formato as nombreFormato, " +
															   " fip.titulo_formato as tituloFormato " +
															   " FROM formato_imp_presupuesto fip " +
															   " WHERE fip.institucion=? " +
															   " ORDER BY fip.nombre_formato ";
	
	/**
	 * Cadena con el statement necesario para consultarlos grupos de servicios existentes
	 */
	private final static String consultarGruposServiciosStr=" SELECT gs.codigo as codigo, " +
															" gs.descripcion " +
															" FROM grupos_servicios gs " +
															" WHERE activo="+ValoresPorDefecto.getValorTrueParaConsultas();
	
	
	/**
	 * Cadena con el statement necesario para consultar el detalle de los servicios de un formato de impresion de presuspuesto
	 */
	private final static String consultarDetalleServiciosStr=" SELECT dsfi.codigo_formato as codigoFormato, " +
															 " dsfi.grupo as codigoGrupo, "+
															 " gs.descripcion as grupo, " +
															 " dsfi.prioridad as prioridad, " +
															 " dsfi.detalle as detalle, " +
															 " dsfi.valores_detalle as valoresDetalle, " +
															 " dsfi.subtotal_grupo as subTotalGrupo " +
															 " FROM facturacion.det_serv_formato_imp  dsfi " +
															 " INNER JOIN facturacion.grupos_servicios  gs ON(dsfi.grupo=gs.codigo) " +
															 " WHERE dsfi.codigo_formato=? ";
	
	
	/**
	 * Cadena con el statement necesario para modificar una detalle de servicios dado un codigo de formato
	 */
	private static final String modificarDetalleServiciosStr=" UPDATE det_serv_formato_imp" +
															 " SET detalle = ?," +
															 " valores_detalle = ?," +
															 " subtotal_grupo = ?, " +
															 " prioridad = ? " +
															 " WHERE codigo_formato = ? " +
															 " AND grupo = ? ";
	
	private static final String existeDetalleServicioStr="SELECT count(1) as cantidad " +
													     " FROM  det_serv_formato_imp " +
													     " where codigo_formato= ? and grupo = ?";
	
	private static final String existeDetalleArticuloStr="SELECT count(1) as cantidad " +
														 " FROM  facturacion.det_art_formato_imp " +
														 " where codigo_formato= ?";
	
	
	/**
	 * Cadena con el statement necesario para insertar el detalle de Ariticulo de un formato de impresión
	 */
	private static final String insertarDetalleArticulosTempStr=" INSERT INTO facturacion.det_art_formato_imp " +
														    " (codigo_formato," +
														    " desc_seccion_art," +
														    " detalle_articulos," +
														    " nivel," +
														    " detalle_nivel," +
														    " valores_detalle," +
														    " subtotal_nivel," +
														    " prioridad, " +
														    " medicamentos_articulos)" +
															" VALUES ( ? , ? , ? , ? , ? , ? , ? , ?, ?)";
	
	
	/**
	 * Cadena con el statement necesario para consultar el detalle de los articulos de        
 un formato de impresion de presupuesto
	 */
	private final static String consultarDetalleArticulosStr=" SELECT dafi.codigo_formato as codigoFormato, " +
															" dafi.desc_seccion_art as descSeccionArticulo, " +
															" dafi.detalle_articulos as detArticulo, " +
															" dafi.nivel as nivel, " +
															" dafi.detalle_nivel as detalleNivel, " +
															" dafi.valores_detalle as valoresDetalle, " +
															" dafi.subtotal_nivel as subTotalNivel, " +
															" dafi.prioridad as prioridad," +
															" dafi.medicamentos_articulos as medicamentosArticulos " +
															" FROM det_art_formato_imp dafi " +
															" WHERE dafi.codigo_formato=? ";
	
	
	/**
	 * Cadena con el statement necesario para consultar el formato de impresion general 
	 */
	private final static String consultarFormatoImpresionStr=" SELECT fip.codigo as codigo, " +
															 " fip.nombre_formato as nombreFormato, " +
															 " fip.titulo_formato as tituloFormato, " +
															 " fip.cantidad as cantidad, " +
															 " fip.desc_cantidad as descCantidad, " +
															 " fip.valor_unitario as valorUnitario, " +
															 " fip.desc_val_unitario as descValUnitario, " +
															 " fip.valor_total as valorTotal, " +
															 " fip.desc_val_total as descValTotal, " +
															 " fip.nota_pie_pagina as notaPiePagina, " +
															 " fip.fecha_hora as fechaHora " +
															 " FROM formato_imp_presupuesto fip " +
															 " WHERE fip.codigo=? ";
	
	/**
	 * Cadena para modificar el detetalle de articulos dado un codigo de formato
	 */
	private static final String modificarDetalleArticulosStr=" UPDATE facturacion.det_art_formato_imp " +
															 " SET desc_seccion_art = ?, " +
															 " detalle_articulos = ?, " +
															 " nivel = ?, " +
															 " detalle_nivel = ?, " +
															 " valores_detalle = ?, " +
															 " subtotal_nivel= ?, " +
															 " prioridad = ?, " +
															 " medicamentos_articulos = ? "+
															 " WHERE codigo_formato=? ";
	
	
	private static final String eliminarGrupoServiciosStr = "DELETE FROM det_serv_formato_imp WHERE  codigo_formato=? AND grupo=? ";
	
	/**
	 * Cadena con el statement necesario para eliminar un formato de impresion en su forma basica
	 */
	private final static String eliminarFormatoBasicoStr=" DELETE FROM formato_imp_presupuesto " +
														 " WHERE codigo=? ";
	
	/**
	 * Cadena con el statement necesario para eliminar todos los detalles de servicios de un formato especifico
	 */
	private final static String eliminarDetServiciosStr=" DELETE FROM det_serv_formato_imp " +
														" WHERE codigo_formato=? ";
	
	/**
	 * Cadena con el statement necesario para eliminar todos los detalles de articulos de un formato especifico
	 */
	private final static String  eliminarDetArticulosStr=" DELETE FROM det_art_formato_imp " +
														 " WHERE codigo_formato=? ";
	
	/**
	 * Método para consultar los formatos de impresión 
	 * existentes para una insititución
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarFormatosExistentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando formatos de impresion de presupesto existentes [SqlBaseFormatoImpresionPresupuestoDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los Grupos de Servicios existentes
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarGruposServicios (Connection con) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarGruposServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los Grupos Servicios existentes : [SqlBaseFormatoImpresionPresupuestoDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar todo el formato de impresion de presupuesto con 
	 * el detalle de Servicios y el detalle de Articulos
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFormatoImpresion (Connection con, int codigoFormato) throws SQLException
	{
		/**HashMap para guardar todo el formato de impresion de presupuesto**/
		HashMap general=new HashMap();
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarFormatoImpresionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			general=UtilidadBD.cargarValueObject(rs);
			rs.close();
			pst.close();
			return general;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el formato de impresión presupuesto [SqlBaseFormatoImpresionPresupuestoDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar el detalle de los servicios de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDetServicios (Connection con, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
			pst.close();
			rs.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle de servicios del formato de impresión presupuesto en general [SqlBaseFormatoImpresionPresupuestoDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar el detalle de los articulos de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDetAritculos (Connection con, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(rs);
			pst.close();
			rs.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el detalle de articulos del formato de impresión presupuesto en general [SqlBaseFormatoImpresionPresupuestoDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para insertar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public static int insertarFormatoImpresion(Connection con, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora, int institucion, HashMap mapaDetServicios, HashMap mapaDetArticulos, String insertarFormatoImpresionBasicoStr, String insertarDetalleServiciosStr, String insertarDetalleArticulosStr) throws SQLException
	{
		int resp=0;
		try
		{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarFormatoImpresionBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, nombreFormato);
				ps.setString(2, tituloFormato);
				ps.setBoolean(3, cantidad);
				ps.setString(4, descripcionCantidad);
				ps.setBoolean(5, valorUnitario);
				ps.setString(6, descripcionValUnitario);
				ps.setBoolean(7, valorTotal);
				ps.setString(8, descripcionValTotal);
				ps.setString(9, piePagina);
				ps.setBoolean(10, fechaHora);
				ps.setInt(11, institucion);
				resp=ps.executeUpdate();
				ps.close();
				if(resp>0)
				{
					/**Obtenemos el ultimo codigo de la secuencia que se inserto en el formato de impresion**/
					int codigoFormato=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_formato_imp_presupuesto");
					int numRegServicios=mapaDetServicios.containsKey("numRegistros")?Integer.parseInt(mapaDetServicios.get("numRegistros")+""):0;
					//mapaDetArticulos.containsKey("numRegistros")?Integer.parseInt(mapaDetArticulos.get("numRegistros")+""):0;
					int numRegArticulos=mapaDetArticulos.size()/7;
					
					boolean tmp;
	
					/**Si se selecciono algun servicio lo insertamos**/
					if(numRegServicios>0)
					{
						/**Insertamos la cantidad de servicios que se hayan seleccionado**/
						for(int i = 0 ; i < numRegServicios ; i++ )
						{
							PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1, codigoFormato);
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("detalle_"+i));
							if(!tmp)
							{
								pst.setBoolean(2, false);
							}
							else
							{
								pst.setBoolean(2, true);
							}
							//logger.info("mapaDetArticulos.get(valoresdetalle_0) "+mapaDetArticulos.get("valoresdetalle_0"));
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("valoresdetalle_"+i));
							if(!tmp)
							{
								pst.setBoolean(3, false);
							}
							else
							{
								pst.setBoolean(3, true);
							}
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("subtotalgrupo_"+i));
							if(!tmp)
							{
								pst.setBoolean(4, false);
							}
							else
							{
								pst.setBoolean(4, true);
							}
							pst.setInt(5, mapaDetServicios.containsKey("prioridad_"+i)?Integer.parseInt(mapaDetServicios.get("prioridad_"+i)+""):-1);
							pst.setInt(6, Integer.parseInt(mapaDetServicios.get("codigogrupo_"+i)+""));
							pst.executeUpdate();
						}
					}
					/**Insertamos el detalle de los articulos**/
					if(numRegArticulos>0)
					{
							PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1, codigoFormato);
							pst.setString(2, mapaDetArticulos.get("descseccionarticulo_0")+"");
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detarticulo_0"));
							if(!tmp)
							{
								pst.setBoolean(3, false);
							}
							else
							{
								pst.setBoolean(3, true);
							}
							if(mapaDetArticulos.get("nivel_0")==null||(mapaDetArticulos.get("nivel_0").toString()).equals(""))
							{
								pst.setObject(4,null);
							}
							else
							{
								pst.setInt(4, Integer.parseInt(mapaDetArticulos.get("nivel_0")+""));
							}
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detallenivel_0"));
							if(!tmp)
							{
								pst.setBoolean(5, false);
							}
							else
							{
								pst.setBoolean(5, true);
							}
							//logger.info("mapaDetArticulos.get(valoresdetalle_0) "+mapaDetArticulos.get("valoresdetalle_0"));
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("valoresdetalle_0"));
							if(!tmp)
							{
								pst.setBoolean(6, false);
							}
							else
							{
								pst.setBoolean(6, true);
							}
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("subtotalnivel_0"));
							if(!tmp)
							{
								pst.setBoolean(7, false);
							}
							else
							{
								pst.setBoolean(7, true);
							}
							pst.setInt(8, Integer.parseInt(mapaDetArticulos.get("prioridad_0")+""));
							pst.setString(9, mapaDetArticulos.get("medicamentosarticulos_0")+"");
							pst.executeUpdate();
					}
				}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de todos los datos del formato de impresión de presupuesto [SqlBaseFormatoImpresionPresupuestoDao]: "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	/**
	 * Método para eliminar por completo el formato de impresion de presupuesto por medio de tres pasos:
	 * 1. Eliminar el detalle de Servicion
	 * 2. Eliminar el detalle de Articulos
	 * 3. Eliminar el formato basico 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarFormatoCompleto(Connection con, int codigoFormato) throws SQLException
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarDetServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFormato);
			resp=ps.executeUpdate();
			ps.close();
			if(resp>0)
			{
				resp=0;
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarDetArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
				resp=pst.executeUpdate();
				pst.close();
				if(resp>0)
				{
					resp=0;
					PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(eliminarFormatoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst1.setInt(1, codigoFormato);
					resp=pst1.executeUpdate();
					pst1.close();
					return resp;
				}
			}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la eliminacion paso a paso de todos los datos del formato de impresión de presupuesto [SqlBaseFormatoImpresionPresupuestoDao]: "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	
	/**
	 * Eliminar un grupo de servicios dado un formato y el codigo del grupo
	 * @param con
	 * @param codigoFormato
	 * @param codigoGrupo
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarGrupo(Connection con, int codigoFormato, int codigoGrupo)
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarGrupoServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFormato);
			ps.setInt(2, codigoGrupo);
			resp=ps.executeUpdate();
			ps.close();
			return resp;
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la eliminacion de un grupo de servicios [SqlBaseFormatoImpresionPresupuestoDao]: "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	/**
	 * Método para modificar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param TipoBD 
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public static int modificarFormatoImpresion(Connection con, int codigoFormato, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora,  HashMap mapaDetServicios, HashMap mapaDetArticulos, int TipoBD) throws SQLException
	{
		
		logger.info("MAPA DET SERVICIOS-->"+mapaDetServicios);
		logger.info("MAPA DET SERVICIOS-->"+mapaDetArticulos);
		
		int resp=0;
		ResultSetDecorator rs=null;
		try
		{
				
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(modificarFormatoImpresionBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, nombreFormato);
				ps.setString(2, tituloFormato);
				ps.setBoolean(3, cantidad);
				ps.setString(4, descripcionCantidad);
				ps.setBoolean(5, valorUnitario);
				ps.setString(6, descripcionValUnitario);
				ps.setBoolean(7, valorTotal);
				ps.setString(8, descripcionValTotal);
				ps.setString(9, piePagina);
				ps.setBoolean(10, fechaHora);
				ps.setInt(11, codigoFormato);
				resp=ps.executeUpdate();
				ps.close();
				if(resp>0)
				{
					int numRegServicios=mapaDetServicios.containsKey("numRegistros")?Integer.parseInt(mapaDetServicios.get("numRegistros")+""):0;
					int numRegArticulos=mapaDetArticulos.containsKey("numRegistros")?Integer.parseInt(mapaDetArticulos.get("numRegistros")+""):0;
					boolean tmp;
					if(numRegServicios>0)
					{
						//primero eliminamos todo y lo volvemos a insertar
						String consulta= "DELETE FROM facturacion.det_serv_formato_imp";
						PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst1.executeUpdate();
						
						//ahora insertamos
						for(int i = 0 ; i < numRegServicios ; i++ )
						{
							PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleServiciosTempStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1, codigoFormato);
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("detalle_"+i));
							if(!tmp)
							{
								pst.setBoolean(2, false);
							}
							else
							{
								pst.setBoolean(2, true);
							}
							logger.info("mapaDetArticulos.get(valoresdetalle_0) "+mapaDetArticulos.get("valoresdetalle_0"));
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("valoresdetalle_"+i));
							if(!tmp)
							{
								pst.setBoolean(3, false);
							}
							else
							{
								pst.setBoolean(3, true);
							}
							tmp=UtilidadTexto.getBoolean(mapaDetServicios.get("subtotalgrupo_"+i));
							if(!tmp)
							{
								pst.setBoolean(4, false);
							}
							else
							{
								pst.setBoolean(4, true);
							}
							
							pst.setInt(5, mapaDetServicios.containsKey("prioridad_"+i)?Integer.parseInt(mapaDetServicios.get("prioridad_"+i)+""):-1);
							pst.setInt(6, Integer.parseInt(mapaDetServicios.get("codigogrupo_"+i)+""));
							pst.executeUpdate();
							pst.close();
						}
						
						/**Modificamos la cantidad de servicios que se hayan seleccionado**/
						/*for(int i = 0 ; i < numRegServicios ; i++ )
						{
							
							PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(existeDetalleServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst1.setInt(1, codigoFormato);
							pst1.setInt(2, Integer.parseInt(mapaDetServicios.get("codigogrupo_"+i)+""));
							rs=pst1.executeQuery());
							int cant=0;
							if(rs.next())
							{
								cant=rs.getInt("cantidad");
							}
							if(cant==0)
							{
								PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleServiciosTempStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								pst.setInt(1, codigoFormato);
								tmp=mapaDetServicios.get("detalle_"+i)+"";
								if(tmp.equals("false"))
								{
									pst.setBoolean(2, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(2, true);
								}
								tmp=mapaDetServicios.get("valoresdetalle_"+i)+"";
								if(tmp.equals("false"))
								{
									pst.setBoolean(3, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(3, true);
								}
								tmp=mapaDetServicios.get("subtotalgrupo_"+i)+"";
								if(tmp.equals("false"))
								{
									pst.setBoolean(4, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(4, true);
								}
								pst.setInt(5, mapaDetServicios.containsKey("prioridad_"+i)?Integer.parseInt(mapaDetServicios.get("prioridad_"+i)+""):-1);
								pst.setInt(6, Integer.parseInt(mapaDetServicios.get("codigogrupo_"+i)+""));
								pst.executeUpdate();
							}
							else
							{
								PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(modificarDetalleServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								tmp=mapaDetServicios.get("detalle_"+i)+"";
								if(tmp.equals("false"))
								{
									pst.setBoolean(1, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(1, true);
								}
								tmp=mapaDetServicios.get("valoresdetalle_"+i)+"";
								
								if(tmp.equals("false"))
								{
									pst.setBoolean(2, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(2, true);
								}
								tmp=mapaDetServicios.get("subtotalgrupo_"+i)+"";
								if(tmp.equals("false"))
								{
									pst.setBoolean(3, false);
								}
								else if(tmp.equals("true"))
								{
									pst.setBoolean(3, true);
								}
								pst.setInt(4, mapaDetServicios.containsKey("prioridad_"+i)?Integer.parseInt(mapaDetServicios.get("prioridad_"+i)+""):-1);
								pst.setInt(5, codigoFormato);
								pst.setInt(6, Integer.parseInt(mapaDetServicios.get("codigogrupo_"+i)+""));
								pst.executeUpdate();
							}
						}*/
					}
					/**Modificamos el detalle de los articulos**/
					if(numRegArticulos>0)
					{
							
						PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(existeDetalleArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst1.setInt(1, codigoFormato);
						rs=new ResultSetDecorator(pst1.executeQuery());
						int cant=0;
						if(rs.next())
						{
							cant=rs.getInt("cantidad");
						}
						rs.close();
						if(cant==0)
						{
							PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleArticulosTempStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1, codigoFormato);
							pst.setString(2, mapaDetArticulos.get("descseccionarticulo_0")+"");
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detarticulo_0"));
							if(!tmp)
							{
								pst.setBoolean(3, false);
							}
							else
							{
								pst.setBoolean(3, true);
							}
							pst.setInt(4, Integer.parseInt(mapaDetArticulos.get("nivel_0")+""));
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detallenivel_0"));
							if(!tmp)
							{
								pst.setBoolean(5, false);
							}
							else
							{
								pst.setBoolean(5, true);
							}
							//logger.info("mapaDetArticulos.get(valoresdetalle_0) "+mapaDetArticulos.get("valoresdetalle_0"));
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("valoresdetalle_0"));
							if(!tmp)
							{
								pst.setBoolean(6, false);
							}
							else
							{
								pst.setBoolean(6, true);
							}
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("subtotalnivel_0"));
							if(!tmp)
							{
								pst.setBoolean(7, false);
							}
							else
							{
								pst.setBoolean(7, true);
							}
							
							pst.setInt(8, mapaDetArticulos.containsKey("prioridad_0")?Integer.parseInt(mapaDetArticulos.get("prioridad_0")+""):-1);
							pst.setString(9, mapaDetArticulos.get("medicamentosarticulos_0")+"");
							pst.executeUpdate();
						}
						else
						{
							PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(modificarDetalleArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setString(1, mapaDetArticulos.get("descseccionarticulo_0")+"");
							
							//logger.info("modificarDetalleArticulosStr "+modificarDetalleArticulosStr);
							
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detarticulo_0"));
							
							if(!tmp)
							{
								pst.setBoolean(2, false);
							}
							else
							{
								pst.setBoolean(2, true);
							}
							if(mapaDetArticulos.get("nivel_0")!=null&&!(mapaDetArticulos.get("nivel_0")+"").equals(""))
							{
								pst.setInt(3, Integer.parseInt(mapaDetArticulos.get("nivel_0")+""));
							}
							else
							{
								pst.setObject(3, null);
							}
							if(mapaDetArticulos.get("detallenivel_0")!=null)
							{
								tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("detallenivel_0"));
								if(!tmp)
								{
									pst.setBoolean(4, false);
								}
								else
								{
									pst.setBoolean(4, true);
								}
							}
							else
							{
								pst.setBoolean(4, false);
							}
							//logger.info("mapaDetArticulos.get(valoresdetalle_0) "+mapaDetArticulos.get("valoresdetalle_0"));
							if(mapaDetArticulos.get("valoresdetalle_0")!=null)
							{
								tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("valoresdetalle_0"));
								if(!tmp)
								{
									pst.setBoolean(5, false);
								}
								else
								{
									pst.setBoolean(5, true);
								}
							}
							else
							{
								pst.setBoolean(5, false);
							}
							tmp=UtilidadTexto.getBoolean(mapaDetArticulos.get("subtotalnivel_0"));
							if(!tmp)
							{
								pst.setBoolean(6, false);
							}
							else
							{
								pst.setBoolean(6, true);
							}

							pst.setInt(7, mapaDetArticulos.containsKey("prioridad_0")?Integer.parseInt(mapaDetArticulos.get("prioridad_0")+""):-1);
							pst.setString(8, mapaDetArticulos.get("medicamentosarticulos_0")+"");
							pst.setInt(9, codigoFormato);
							pst.executeUpdate();
							pst.close();
						}
					}
				}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la modificacion de TODOS los datos del formato de impresión de presupuesto [SqlBaseFormatoImpresionPresupuestoDao]: "+e.toString() );
				e.printStackTrace();
				resp=0;
		}
		return resp;
	}
	
}