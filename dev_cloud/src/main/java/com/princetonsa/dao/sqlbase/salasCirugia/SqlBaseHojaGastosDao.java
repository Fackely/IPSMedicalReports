/*
 * Marzo 21 del 2007
 */
package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de hoja de gastos
 */
public class SqlBaseHojaGastosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseHojaGastosDao.class);
	
	/**
	 * Cadena que consulta el consecutivo de la hoja de gastos x servicio
	 */
	private static final String consultarConsecutivoXServicioStr = "SELECT "+ 
		"hgs.consecutivo "+ 
		"FROM paquetes_qx_servicio hgs "+ 
		"INNER JOIN paquetes_qx hg ON(hg.consecutivo=hgs.consecutivo) "+ 
		"WHERE hgs.servicio = ? AND hg.institucion = ?";
	
	/**
	 * Cadena implementada para consultar los servicios parametrizados por consecutivo de hoja de gastos
	 */
	private static final String consultarServiciosXConsecutivoStr = "SELECT "+ 
		"servicio AS codigo, "+
		"getcodigoespecialidad(servicio) || '-' || servicio || ' ' || getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio," +
		"'true' AS existe_bd, " +
		"'false' AS eliminar "+
		"FROM paquetes_qx_servicio "+ 
		"WHERE consecutivo = ?";
	
	/**
	 * Cadena implementada para consultar los articulos parametrizados por consecutivo de hoja de gastos
	 */
	private static final String consultarArticulosXConsecutivoStr =" SELECT " +
																		  " hg.consecutivo AS consecutivo, " +
																		  " hg.articulo AS codigo_articulo, " +
																		  " hg.cantidad AS cantidad, " +
																		  " hg.cantidad AS cantidad_original, " +
																		  "	a.descripcion ||'CONC:'|| coalesce(a.concentracion,'') ||' F.F:'||" +
																		  " coalesce(getNomFormaFarmaceutica(a.forma_farmaceutica),'')  || ' NAT:' || coalesce(na.nombre,'')  || " +
																		  " CASE WHEN na.es_pos= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - POS' " +
																		  " WHEN na.es_pos ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - NOPOS' ELSE '' END AS descripcion_articulo, " +
																		  " getNomUnidadMedida(a.unidad_medida) AS unidad_medida_articulo, " +
																		  "	'false' as eliminar " +
																	" FROM paquetes_qx_articulo hg " +
																	" INNER JOIN articulo a on(a.codigo=hg.articulo) " +
																	" INNER JOIN naturaleza_articulo na ON na.acronimo = a.naturaleza " +
																	" WHERE hg.consecutivo =  ? " +
																	" ORDER BY a.descripcion ";
		
		/* se cambia esta consulta por ser muy pesada
		"SELECT " +
		"hg.consecutivo AS consecutivo, "+ 
		"hg.articulo AS codigo_articulo, "+
		"hg.cantidad AS cantidad, "+
		"hg.cantidad AS cantidad_original, "+
		"va.descripcion " +
			"||' CONC:'|| coalesce(va.concentracion,'') " +
			"||' F.F:'|| coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'')  " +
			"|| ' NAT:' || coalesce(va.descripcionnaturaleza,'')  || " +
			"CASE WHEN va.es_pos= '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN ' - POS' WHEN va.es_pos ='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' THEN ' - NOPOS' ELSE '' END AS descripcion_articulo, "+ 
		"getNomUnidadMedida(va.unidad_medida) AS unidad_medida_articulo," +
		"'false' as eliminar " +
		"FROM paquetes_qx_articulo hg "+ 
		"INNER JOIN view_articulos va ON(va.codigo=hg.articulo) "+ 
		"WHERE hg.consecutivo = ? ORDER BY va.descripcion";
	*/

	
	
	/**
	 * Cadena que inserta el encabezado de la hoja de gastos
	 */
	private static final String insertarEncabezadoHojaGastosStr = "INSERT INTO paquetes_qx (consecutivo,institucion) VALUES ";
	
	/**
	 * Cadena que eliminar el encabezado de la hoja de gastos
	 */
	private static final String eliminarEncabezadoHojaGastosStr = "DELETE FROM paquetes_qx WHERE consecutivo = ? ";
	
	/**
	 * Cadena usada para insertar un servicio en la hoja de gastos
	 */
	private static final String insertarServicioStr = "INSERT INTO paquetes_qx_servicio (servicio,consecutivo) VALUES (?,?)";
	
	/**
	 * Cadena usada para eliminar un servicio en la hoja de gastos
	 */
	private static final String eliminarServicioStr = "DELETE FROM paquetes_qx_servicio WHERE servicio = ? AND consecutivo = ?";
	
	/**
	 * Cadena usada para insertar un articulo en la hoja de gastos
	 */
	private static final String insertarArticuloStr = "INSERT INTO paquetes_qx_articulo (articulo,cantidad,consecutivo) VALUES (?,?,?)";
	
	/**
	 * Cadena usada para modificar un articulo en la hoja de gastos
	 */
	private static final String modificarArticuloStr = "UPDATE paquetes_qx_articulo SET cantidad = ? WHERE articulo = ? AND consecutivo = ?";
	
	/**
	 * Cadena usada para eliminar un artículo en la hoja de gastos
	 */
	private static final String eliminarArticuloStr = "DELETE FROM paquetes_qx_articulo WHERE articulo = ? AND consecutivo = ?";
	
	/**
	 * Cadena que elimina los servicios de una parametrizacion de hoja de gastos
	 */
	private static final String eliminarServiciosXConsecutivoStr = "DELETE FROM paquetes_qx_servicio WHERE consecutivo = ?";
	
	/**
	 * Cadena que elimina los articulo de una parametrizacion de hoja de gastos
	 */
	private static final String eliminarArticulosXConsecutivoStr = "DELETE FROM paquetes_qx_articulo WHERE consecutivo = ?";
	
	/**
	 * Cadena que consulta los paquetes materiales quirúrgicos por institucion
	 */
	private static final String consultarPaquetesMaterialesQxStr = "SELECT " +
		"consecutivo AS consecutivo, " +
		"institucion AS institucion, " +
		"codigo as codigo, " +
		"descripcion as descripcion," +
		"puedoEliminarPaqQx(consecutivo) As puedo_eliminar ";
		
	
	/**
	 * Cadena que inserta un paquete
	 */
	private static final String insertarPaqueteMaterialesQxStr = "INSERT INTO paquetes_qx (consecutivo,codigo,descripcion,institucion,fecha_modificacion,hora_modificacion,usuario_modificacion) VALUES ";

	/**
	 * Cadena que modifica un paquete
	 */
	private static final String modificarPaqueteMaterialesQxStr = "UPDATE paquetes_qx SET descripcion = ?, fecha_modificacion = CURRENT_DATE, hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modificacion = ? WHERE consecutivo = ?";
	
	/**
	 * Cadena que elimina un paquete
	 */
	private static final String eliminarPaqueteMaterialesQxStr = "DELETE FROM paquetes_qx WHERE consecutivo = ?";
	
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int consultarConsecutivoXServicio(Connection con,HashMap campos)
	{
		try
		{
			int consecutivo = 0;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarConsecutivoXServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoServicio")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				consecutivo = rs.getInt("consecutivo");
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando el consecutivo x servicio :"+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una hoja de gastos por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarHojaGastosXConsecutivo(Connection con,HashMap campos)
	{
		try
		{
			HashMap respuesta = new HashMap();
			
			//Consulta de los servicios asociados a la hoja de gastos
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			respuesta.put("mapaServicios",UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			
			//Consulta de los artículos asociados a la hoja de gastos
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultarArticulosXConsecutivoStr));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			respuesta.put("mapaArticulos",UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			
			return respuesta;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarHojaGastosXConsecutivo: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método implementado para guardar la hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int guardar(Connection con,HashMap campos)
	{
		try
		{
			//********SE TOMA LA INFORMACION ENVIADA**************************
			int consecutivo = Integer.parseInt(campos.get("consecutivo").toString());
			int institucion = Integer.parseInt(campos.get("institucion").toString());
			String secuencia = campos.get("secuencia").toString();
			HashMap mapaServicios = (HashMap) campos.get("mapaServicios");
			HashMap mapaArticulos = (HashMap) campos.get("mapaArticulos");
			int numServicios = Integer.parseInt(campos.get("numServicios").toString());
			int numArticulos = Integer.parseInt(campos.get("numArticulos").toString());
			int resp0 = 0, resp1 = 0, resp2 = 0;
			//****************************************************************
			PreparedStatementDecorator pst = null;
			
			//se inicia la transaccion---------------------------------------------------
			boolean inicioTransaccion = UtilidadBD.iniciarTransaccion(con);
			if(!inicioTransaccion)
			{
				logger.error("Error al iniciar la transacción en método guardar");
				return 0;
			}
			
			int numServiciosAEliminar = obtenerNumRegistrosAEliminar(mapaServicios,numServicios);
			int numArticulosAEliminar = obtenerNumRegistrosAEliminar(mapaArticulos,numArticulos);
			
			//Se verifica si es eliminacion de toda la parametrizacion------------------------------------------------------------------
			if(consecutivo>0&&numServicios==numServiciosAEliminar&&numArticulos==numArticulosAEliminar)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarArticulosXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
				resp0 = pst.executeUpdate();
				
				if(resp0>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosXConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
					resp1 = pst.executeUpdate();
				}
				
				if(resp1>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarEncabezadoHojaGastosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
					resp2 = pst.executeUpdate();
				}
			}
			else
			{
				///si no hay consecutivo quiere decir que es una parametrizacion nueva---------------------
				if(consecutivo<=0)
				{
					String consulta = insertarEncabezadoHojaGastosStr + " ("+secuencia+",?)"; 
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, institucion);
					resp0 = pst.executeUpdate();
					if(resp0>0)
						consecutivo = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_paquetes_qx");
				}
				else
					resp0 = 1;
				
				//ingresar/eliminar servicios-------------------------------------
				if(resp0>0)
				{
					resp1 = guardarServicios(con,mapaServicios,numServicios,consecutivo);
				}
				
				//ingresar/modificar/eliminar articulos-------------------------------
				if(resp1>0)
				{
					resp2 = guardarArticulos(con,mapaArticulos,numArticulos,consecutivo);
				}
			}
			
			
			if(resp0<=0||resp1<=0||resp2<=0)
			{
				UtilidadBD.abortarTransaccion(con);
				consecutivo = 0;
			}
			else
				UtilidadBD.finalizarTransaccion(con);
			
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en el método guardar: "+e);
			return 0;
		}
	}

	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private static int obtenerNumRegistrosAEliminar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}

	/**
	 * Método implementado para guardar los articulos
	 * @param con
	 * @param mapaArticulos
	 * @param numArticulos
	 * @param consecutivo
	 * @return
	 */
	private static int guardarArticulos(Connection con, HashMap mapaArticulos, int numArticulos, int consecutivo) 
	{
		int resp = numArticulos>0?1:0;
		PreparedStatementDecorator pst = null;
		
		try
		{
			for(int i=0;i<numArticulos;i++)
			{
				//se verifica si es un registro nuevo para insertarlo
				if(mapaArticulos.get("consecutivo_"+i).toString().equals(""))
				{
					//**************INSERCION DE ARTICULO**************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaArticulos.get("codigoArticulo_"+i)+""));
					pst.setInt(2,Utilidades.convertirAEntero(mapaArticulos.get("cantidad_"+i)+""));
					pst.setDouble(3, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numArticulos;
					}
					//************************************************************
				}
				else if(UtilidadTexto.getBoolean(mapaArticulos.get("eliminar_"+i).toString()))
				{
					//************ELIMINAR ARTICULO******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaArticulos.get("codigoArticulo_"+i)+""));
					pst.setDouble(2, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numArticulos;
					}
					//***********************************************************
				}
				else
				{
					//************MODIFICAR ARTICULO******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(modificarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaArticulos.get("cantidad_"+i)+""));
					pst.setDouble(2,Utilidades.convertirADouble(mapaArticulos.get("codigoArticulo_"+i)+""));
					pst.setInt(3, consecutivo);
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numArticulos;
					}
					//************************************************************
				}
					
			}
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardar servcios: "+e);
			return 0;
		}
	}

	/**
	 * Método implementado para guardar los servicios 
	 * @param con
	 * @param mapaServicios
	 * @param numServicios
	 * @param consecutivo 
	 * @return
	 */
	private static int guardarServicios(Connection con, HashMap mapaServicios, int numServicios, int consecutivo) 
	{
		int resp = numServicios>0?1:0;
		PreparedStatementDecorator pst = null;
		
		try
		{
			for(int i=0;i<numServicios;i++)
			{
				//se verifica si es un registro nuevo para insertarlo
				if(!UtilidadTexto.getBoolean(mapaServicios.get("existeBd_"+i).toString()))
				{
					//**************INSERCION DE SERVICIO**************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaServicios.get("codigo_"+i)+""));
					pst.setDouble(2, Utilidades.convertirADouble(consecutivo+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numServicios;
					}
					//************************************************************
				}
				else if(UtilidadTexto.getBoolean(mapaServicios.get("eliminar_"+i).toString()))
				{
					//************ELIMINAR SERVICIO******************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(mapaServicios.get("codigo_"+i)+""));
					if(pst.executeUpdate()<=0)
					{
						resp = 0;
						i = numServicios;
					}
					//***********************************************************
				}
					
			}
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardar servcios: "+e);
			return 0;
		}
		
		
	}
	
	/**
	 * Método que consulta los paquetes de materiales quirúrgicos
	 * @param con
	 * @param institucion
	 * @param conProcedimientos 
	 * @param sinProcedimientos 
	 * @return
	 */
	public static HashMap<String, Object> consultarPaquetesMaterialesQx (Connection con,int institucion, boolean conProcedimientos, boolean sinProcedimientos)
	{
		logger.info("\n-------------------- entro a  consultarPaquetesMaterialesQx ----------------------");	
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros","0");
		try
		{
			String consulta = consultarPaquetesMaterialesQxStr;
			
			
			consulta += " FROM paquetes_qx WHERE institucion = ? ";
			
			//Se verifica si solo se deben filtrar los que tengan procedimientos
			if(conProcedimientos)
				consulta += " AND consecutivo IN (SELECT hgs.consecutivo FROM paquetes_qx_servicio hgs WHERE hgs.consecutivo = consecutivo) ";
			
			//Se verifica si solo se deben filtrar lo que no tengan procedimientos
			if(sinProcedimientos)
				consulta += " AND consecutivo NOT IN (SELECT hgs.consecutivo FROM paquetes_qx_servicio hgs WHERE hgs.consecutivo = consecutivo) ";
			
			
			consulta += "ORDER BY descripcion";
			logger.info("\n cadena -->"+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPaquetesMaterialesQx: "+e);
		}
		
		resultados.put("indices", "consecutivo_,codigo_,descripcion_,institucion_,puedoEliminar_");
		return resultados;
	}
	
	/**
	 * Método que realiza la inserción de un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		try
		{
			String consulta = insertarPaqueteMaterialesQxStr + " ("+campos.get("secuencia")+",?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paquetes_qx (consecutivo,codigo,descripcion,institucion,fecha_modificacion,hora_modificacion,usuario_modificacion) VALUES 
			 */
			
			pst.setString(1,campos.get("codigo")+"");
			pst.setString(2,campos.get("descripcion")+"");
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setString(4,campos.get("usuario")+"");
			
			if(pst.executeUpdate()>0)
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_paquetes_qx"); 
		}
		catch(SQLException e)
		{
			logger.error("error en insertarPaqueteMaterialesQx: "+e);
			
		}
		return 0;
	}
	
	/**
	 * Método que realiza la modificacion de un paquete de materiales qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarPaqueteMaterialesQxStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE paquetes_qx SET 
			 * descripcion = ?, 
			 * fecha_modificacion = CURRENT_DATE, 
			 * hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", 
			 * usuario_modificacion = ? 
			 * WHERE consecutivo = ?
			 */
			
			pst.setString(1,campos.get("descripcion")+"");
			pst.setString(2,campos.get("usuario")+"");
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarPaqueteMaterialesQx: "+e);
		}
		return 0; 
	}
	
	/**
	 * Método que consulta un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String,Object> consultarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros","0");
		
		try
		{
			String consulta = consultarPaquetesMaterialesQxStr + " FROM paquetes_qx WHERE consecutivo = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPaqueteMaterialesQx: "+e);
		}
		resultados.put("indices", "consecutivo_,codigo_,descripcion_,institucion_,puedoEliminar_");
		return resultados;
	}
	
	/**
	 * Método que realiza la eliminación de un paquete
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static int eliminarPaqueteMaterialesQx(Connection con,String consecutivo)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarPaqueteMaterialesQxStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarPaqueteMaterialesQx: "+e);
		}
		return 0;
	}
	
	/**
	 * Método que consulta los articulos de un paquete de materiales quirúrgicos
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap<String, Object> consultarArticulosXConsecutivo(Connection con,String consecutivo)
	{
		logger.info("\n -------------------- entre a consultarArticulosXConsecutivo ------------- consecutivo -->"+consecutivo);
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			logger.info("\n consulta -->"+consultarArticulosXConsecutivoStr) ;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarArticulosXConsecutivoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			
			//logger.info("CONSULTA DE LOS ARTICULOS DEL PAQUETE=> "+consultarArticulosXConsecutivoStr.replace("?", consecutivo));
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosXConsecutivo: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que consulta los servicios del paquete material quirúrgico
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap<String, Object> consultarServiciosXConsecutivo(Connection con,String consecutivo)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosXConsecutivoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(consecutivo));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarServiciosXConsecutivo: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para insertar un artículo a un paquete de materiales quirúrgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarArticulo (Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paquetes_qx_articulo (articulo,cantidad,consecutivo) VALUES (?,?,?)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoArticulo")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("cantidad")+""));
			pst.setDouble(3, Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarArticulo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para insertar un servicio a un paquete de materiales quirúrgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarServicio (Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paquetes_qx_servicio (servicio,consecutivo) VALUES (?,?)
			 */
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoServicio")+""));
			pst.setDouble(2, Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarServicio: "+e);
			return 0;
		}
	}
		
	
	/**
	 * Método implementado para modificar un articulo de un paquete de materiales quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarArticulo (Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("cantidad")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoArticulo")+""));
			pst.setDouble(3, Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error modificarArticulo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para eliminar un art{iculo de un paquete de materiales quirúrgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminarArticulo(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoArticulo")+""));
			pst.setDouble(2, Utilidades.convertirADouble(campos.get("consecutivo")+""));
			return pst.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarArticulo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para eliminar un art{iculo de un paquete de materiales quirúrgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int eliminarServicio(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoServicio")+""));
			pst.setDouble(2, Utilidades.convertirADouble(campos.get("consecutivo")+""));
			return pst.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarArticulo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que realiza la busqueda avanzada de los paquetes materiales Qx.
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap busquedaGenericaPaquetesMateriales(Connection con,HashMap campos)
	{
		HashMap resultado = new HashMap();
		resultado.put("numRegistros","0");
		String[] indices = {"consecutivo_","codigo_","descripcion_","insertado_"};
		
		try
		{
			String consulta = "SELECT DISTINCT "+ 
				"hg.consecutivo, "+
				"hg.codigo, "+
				"hg.descripcion "+ 
				"FROM paquetes_qx hg ";
			
			//Se verifica si la busqueda de paquetes será por servicio para añadir el inner
			if(!campos.get("codigoServicio").toString().equals(""))
				consulta += " INNER JOIN paquetes_qx_servicio hgs on(hgs.consecutivo=hg.consecutivo)  ";
			
			consulta += " INNER JOIN paquetes_qx_articulo hga ON(hga.consecutivo=hg.consecutivo)  ";
			
			//Se verifica si se buscará por pareja de clase - grupo de inventarios para añadir e inner
			if(!campos.get("parejasClaseGrupo").toString().equals(""))
				consulta += " INNER JOIN articulo va ON(va.codigo=hga.articulo) " +
						"INNER JOIN subgrupo_inventario s ON (s.codigo=va.subgrupo) ";
			
			consulta += " WHERE hg.institucion = "+campos.get("codigoInstitucion");
			
			if(!campos.get("codigoServicio").toString().equals(""))
				consulta += " AND hgs.servicio = "+campos.get("codigoServicio");
			
			if(!campos.get("parejasClaseGrupo").toString().equals(""))
				consulta += " AND s.clase ||'-'|| s.grupo IN("+campos.get("parejasClaseGrupo")+") ";
			
			consulta += " ORDER BY hg.codigo,hg.descripcion ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\n\nBusqueda Avanzada=>"+consulta+"\n\n\n");
			resultado =  UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaGenericaPaquetesMateriales: "+e);
		}
		resultado.put("INDICES_MAPA", indices);
		return resultado;
	}
	
}
