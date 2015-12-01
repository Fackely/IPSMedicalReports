/*
 * @(#)SqlBaseEsquemaTarifarioDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;


/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a EsquemaTarifario
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class SqlBaseEsquemaTarifarioDao
{
	/**
	 * Manejar logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseEsquemaTarifarioDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * un esquema tarifario para una BD Genérica
	*/
	private static final String cargarEsquemaTarifarioStr="SELECT " +
			"esqt.nombre As nombre, " +
			"esqt.tarifario_oficial as codigoTarifarioOficial, " +
			"tf.nombre as tarifarioOficial, " +
			"esqt.metodo_ajuste as acronimoMetodoAjuste, " +
			"ma.nombre as metodoAjuste, " +
			"esqt.es_inventario as esInventario, " +
			"administracion.getBooleanSiNo(activo) as activo, " +
			"activo as activoBoolean," +
			"coalesce(esqt.cantidad,0) AS cantidad " +
			"from esquemas_tarifarios esqt " +
			"LEFT OUTER JOIN tarifarios_oficiales tf ON (esqt.tarifario_oficial=tf.codigo) " +
			"INNER JOIN metodos_ajuste ma ON (esqt.metodo_ajuste=ma.acronimo)  " +
			"where esqt.codigo=? AND esqt.institucion= ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar 
	 * un esquema tarifario para una BD Genérica
	*/
	private static final String modificarEsquemaTarifarioStr="UPDATE esquemas_tarifarios set nombre=?, metodo_ajuste =?, activo=?, cantidad = ? where codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar 
	 * el último esquema tarifario insertado para una BD Genérica
	*/
	private static final String buscarUltimoEsquemaTarifarioInsertadoStr="SELECT max(codigo) as codigo from esquemas_tarifarios";
	
	/**
	 * 
	 */
	private static final String obtenerTarifarioOficialXCodigoEsquematarStr="SELECT coalesce(tarifario_oficial,0) as tarifario FROM esquemas_tarifarios WHERE codigo=?";
	
	/**
	 * Inserta un esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param nombre. String, nombre del esquema tarifario
	 * @param codigoTarifarioOficial. int, código del tarifario oficial (tipo de manual) asociado
	 * 				al esquema tarifario
	 * @param acronimoMetodoAjuste. String, método de ajuste asociado al esquema tarifario
	 * @param esInventario. boolean, si el esquema es de inventarios o de servicios
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.EsquemaTarifarioDao#insertar (	Connection , String , int , String , boolean , String , boolean , String ) throws SQLException
	 */
	public static ResultadoBoolean insertar(	Connection con,
															String nombre,
															int codigoTarifarioOficial,
															String acronimoMetodoAjuste,
															boolean esInventario,
															String codigoInstitucion,
															boolean activo,
															float cantidad,
															String insertarEsquemaTarifarioStr) throws SQLException
	{
		PreparedStatementDecorator insertarEsquemaTarifarioStatement= new PreparedStatementDecorator(con.prepareStatement(insertarEsquemaTarifarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarEsquemaTarifarioStatement.setString(1, nombre);
		if(codigoTarifarioOficial<=0)
		{
			insertarEsquemaTarifarioStatement.setObject(2,null);
		}
		else
		{	
			insertarEsquemaTarifarioStatement.setInt(2, codigoTarifarioOficial);
		}
		insertarEsquemaTarifarioStatement.setString(3, acronimoMetodoAjuste);
		if(esInventario)
			insertarEsquemaTarifarioStatement.setString(4, ValoresPorDefecto.getValorTrueParaConsultas()+"");
		else
			insertarEsquemaTarifarioStatement.setString(4, ValoresPorDefecto.getValorFalseParaConsultas()+"");
		insertarEsquemaTarifarioStatement.setString(5, codigoInstitucion);
		if(activo)
			insertarEsquemaTarifarioStatement.setString(6, ValoresPorDefecto.getValorTrueParaConsultas()+"");
		else
			insertarEsquemaTarifarioStatement.setString(6, ValoresPorDefecto.getValorFalseParaConsultas()+"");
		if(cantidad>0)
			insertarEsquemaTarifarioStatement.setFloat(7, cantidad);
		else
			insertarEsquemaTarifarioStatement.setFloat(7, 0);
		
		if (insertarEsquemaTarifarioStatement.executeUpdate()>0)
		{
			PreparedStatementDecorator buscarUltimoEsquemaTarifarioInsertadoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarUltimoEsquemaTarifarioInsertadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(buscarUltimoEsquemaTarifarioInsertadoStatement.executeQuery());
			//Como acabo de insertar me debe dar resultado
			rs.next();
			
			return new ResultadoBoolean (true, rs.getString("codigo"));
		}
		else
		{
			throw new SQLException ("La inserción de Esquema tarifario falló (SqlBaseEsquemaTarifarioDao)");
		}
	}

	/**
	 * Consulta los datos de un esquema tarifario dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo. int, código del esquema tarifario
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si 
	 * fue exitosa la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.EsquemaTarifarioDao#consultar(java.sql.Connection, int)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResultadoCollectionDB consultar(Connection con, int codigo, int institucion) throws SQLException, BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		ResultadoCollectionDB res=null;
		try{
			Log4JManager.info("############## Inicio consultar");
			pst = con.prepareStatement(cargarEsquemaTarifarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigo);
			pst.setInt(2, institucion);
		    rs=pst.executeQuery();
		    Collection coleccion=new ArrayList();
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				HashMap mapa=new HashMap();
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"",rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				coleccion.add(mapa);
			}
			res = new ResultadoCollectionDB (true, "",coleccion);
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin consultar");
		return res;
	}

	/**
	 * Método que implementa la modificación de un esquema tarifario
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.EsquemaTarifarioDao#modificar(	Connection , int , String , String , boolean)
	 */
	public static ResultadoBoolean modificar(	Connection con,
															int codigo,
															String nombre,
															String acronimoMetodoAjuste,
															float cantidad,
															boolean activo) throws SQLException
	{
	    	    
	    PreparedStatementDecorator modificarEsquemaTarifarioStatement= new PreparedStatementDecorator(con.prepareStatement(modificarEsquemaTarifarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarEsquemaTarifarioStatement.setString(1, nombre);
		modificarEsquemaTarifarioStatement.setString(2, acronimoMetodoAjuste);
		modificarEsquemaTarifarioStatement.setBoolean(3, activo);
		if(cantidad>0)
			modificarEsquemaTarifarioStatement.setFloat(4,cantidad);
		else
			modificarEsquemaTarifarioStatement.setFloat(4,0);
		modificarEsquemaTarifarioStatement.setInt(5, codigo);
		
		
		
		if (modificarEsquemaTarifarioStatement.executeUpdate()>0)
		{
			return new ResultadoBoolean (true, "Modificación Esquema Tarifario sin Problema");
		}
		else
		{
			throw new SQLException ("La Modificación de Esquema tarifario falló (SqlBaseEsquemaTarifarioDao)");
		}
		
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static Collection busqueda(Connection con, int codigo, String nombre, int tarifarioOficial, String metodoAjuste, boolean esInventario, int institucion, char inventarioAux)
	{
		String select="SELECT " +
				"et.codigo AS codigo, " +
				"et.nombre AS nombre, " +
				"ma.nombre AS metodoAjuste, " +
				"et.es_inventario AS esInventario, " +
				"t.nombre AS tarifarioOficial, " +
				"coalesce(et.cantidad,0) AS cantidad, "+
				"administracion.getBooleanSiNo(activo) as activo " +
				"from esquemas_tarifarios et " +
				"INNER JOIN metodos_ajuste ma ON(et.metodo_ajuste=ma.acronimo) ";
		String innerJoin="";
		String where=" WHERE et.institucion="+institucion;
		String orderby=" ORDER BY et.codigo";
		
		try
		{
			if(tarifarioOficial<=0)
			{
				innerJoin=" LEFT OUTER JOIN tarifarios_oficiales t ON(et.tarifario_oficial=t.codigo)";
			}
			else
			{
			    innerJoin=" INNER JOIN tarifarios_oficiales t ON(et.tarifario_oficial=t.codigo AND et.tarifario_oficial ="+tarifarioOficial+")";
			}
			if(codigo!=0)
			{
				where+=" AND et.codigo="+codigo;
			}
			if(!nombre.equals(""))
			{
				where+= " AND UPPER (et.nombre) LIKE (UPPER ('%"+nombre+"%'))";
			}
			if(!metodoAjuste.equals(" "))
			{
				where+=" AND et.metodo_ajuste='"+metodoAjuste + "'";
			}
			if(inventarioAux=='0')
			{
				if(esInventario)
				{
					where+=" AND et.es_inventario="+ValoresPorDefecto.getValorTrueParaConsultas();
				}
				else
				{
					where+=" AND et.es_inventario="+ValoresPorDefecto.getValorFalseParaConsultas();
				}
			}
			
			String consulta=select+innerJoin+where+orderby;
			logger.info("===> La consulta es: "+consulta);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultados=new ResultSetDecorator(pst.executeQuery());
			return UtilidadBD.resultSet2Collection(resultados);
		}
		catch (SQLException e)
		{
			logger.error("Error buscando el esquema tarifario" + e);
			try
			{
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error cerrando la conexión" + e1);
			}
			return null;
		}
	}
	
	/**
	 * metodo q obtiene el tarifario oficial
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static int obtenerTarifarioOficialXCodigoEsquemaTar(Connection con, int codigoEsquemaTarifario) throws BDException
	{
		int res = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerTarifarioOficialXCodigoEsquemaTar");
			pst =  con.prepareStatement(obtenerTarifarioOficialXCodigoEsquematarStr);
			pst.setInt(1, codigoEsquemaTarifario);
			rs=pst.executeQuery();
			if(rs.next()){
				res = rs.getInt("tarifario");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerTarifarioOficialXCodigoEsquemaTar");
		return res;
	
	}
	
	/**
	 * Método para obtener el esquema tarifario que aplica segun el contrato de la entidad subcontratada
	 * @param con
	 * @param codigoContrato
	 * @param servArt
	 * @param fechaCalculoVigencia
	 * @param esServicio
	 * @return
	 */
	public static EsquemaTarifario obtenerEsquemaTarifarioServicioArticuloEntidadSub(Connection con,String codigoContrato,int servArt,String fechaCalculoVigencia,boolean esServicio)
	{
		EsquemaTarifario esquema = new EsquemaTarifario(ConstantesBD.codigoNuncaValido,"",false, new InfoDatosInt(ConstantesBD.codigoNuncaValido), new InfoDatos());
		try
		{
			//****************************OBTENER ESQUEMA TARIFARIO X SERVICIO************************************
			if(esServicio)
			{
				esquema.setEsInventario(false);
				String cadena="SELECT grupo_servicio as gruposervicio from servicios where codigo = ?";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				ps.setInt(1, servArt);
				logger.info("1-->"+cadena);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					int grupo=rs.getInt(1);
					String fechaVigencia = UtilidadFecha.conversionFormatoFechaABD(fechaCalculoVigencia);					
					cadena="SELECT " +
						" t.esquema_tarifario as codigo," +
						" et.nombre as nombre," +
						" coalesce(et.tarifario_oficial,"+ConstantesBD.codigoNuncaValido+") as tarifario_oficial, " +
						" et.metodo_ajuste " +
						" from tarifas_proc_con_ent_sub t " +
						" INNER JOIN esquemas_tarifarios et ON(et.codigo = t.esquema_tarifario) " +
						" where " +
						" t.grupo_servicio="+grupo+" and t.contrato_entidad_sub="+codigoContrato + 
						" and t.fecha_vigencia <= '"+ fechaVigencia  + "'"+
						" and t.activo = '"+ConstantesBD.acronimoSi+"' order by t.fecha_vigencia desc";					
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
					logger.info("2-->"+cadena);
					rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						esquema.setCodigo(rs.getInt("codigo"));
						esquema.setNombre(rs.getString("nombre"));
						esquema.setTarifarioOficial(new InfoDatosInt(rs.getInt("tarifario_oficial")));
						esquema.setMetodoAjuste(new InfoDatos(rs.getString("metodo_ajuste")));
					}
					else
					{
						cadena="SELECT " +
							" t.esquema_tarifario as codigo," +
							" et.nombre as nombre," +
							" coalesce(et.tarifario_oficial,"+ConstantesBD.codigoNuncaValido+") as tarifario_oficial, " +
							" et.metodo_ajuste " +
							" from tarifas_proc_con_ent_sub t " +
							" INNER JOIN esquemas_tarifarios et ON(et.codigo = t.esquema_tarifario) " +
							" where " +
							" t.grupo_servicio IS NULL and t.contrato_entidad_sub="+codigoContrato + 
							" and t.fecha_vigencia <= '"+fechaVigencia  + "'"+
							" and t.activo = '"+ConstantesBD.acronimoSi+"' order by t.fecha_vigencia desc";
						ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
						logger.info("3-->"+cadena);
						rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							esquema.setCodigo(rs.getInt("codigo"));
							esquema.setNombre(rs.getString("nombre"));
							esquema.setTarifarioOficial(new InfoDatosInt(rs.getInt("tarifario_oficial")));
							esquema.setMetodoAjuste(new InfoDatos(rs.getString("metodo_ajuste")));
						}
						
					}
				}
			}
			//***************OBTENER ESQUEMA TARIFARIO X ARTICULO********************************
			else
			{
				esquema.setEsInventario(true);
				String cadena="SELECT clase from articulo a inner join subgrupo_inventario sui on(sui.codigo=a.subgrupo) where a.codigo = "+servArt;
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				logger.info("1-->"+cadena);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					int clase=rs.getInt(1);
					String fechaVigencia = UtilidadFecha.conversionFormatoFechaABD(fechaCalculoVigencia);
					cadena="SELECT "+
						" t.esquema_tarifario as codigo, "+
						" et.nombre as nombre, "+
						" coalesce(et.tarifario_oficial,"+ConstantesBD.codigoNuncaValido+") as tarifario_oficial, "+ 
						" et.metodo_ajuste "+ 
						" from tarifas_inv_con_ent_sub t "+ 
						" INNER JOIN esquemas_tarifarios et ON(et.codigo = t.esquema_tarifario) "+ 
						" where "+ 
						//"t.clase_inventario="+clase+" and t.contrato_entidad_sub="+codigoContrato+" and t.fecha_vigencia <= '"+UtilidadFecha.conversionFormatoFechaAAp(fechaCalculoVigencia)+"' and t.activo = '"+ConstantesBD.acronimoSi+"' order by t.fecha_vigencia desc";
						" t.clase_inventario="+clase+" and t.contrato_entidad_sub="+codigoContrato + 
						" and t.fecha_vigencia <= '"+ fechaVigencia  + "'"+
						" and t.activo = '"+ConstantesBD.acronimoSi+"' order by t.fecha_vigencia desc";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
					logger.info("2-->"+cadena);
					rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						esquema.setCodigo(rs.getInt("codigo"));
						esquema.setNombre(rs.getString("nombre"));
						esquema.setTarifarioOficial(new InfoDatosInt(rs.getInt("tarifario_oficial")));
						esquema.setMetodoAjuste(new InfoDatos(rs.getString("metodo_ajuste")));
					}
					else
					{
						cadena="SELECT "+
							" t.esquema_tarifario as codigo, "+
							" et.nombre as nombre, "+
							" coalesce(et.tarifario_oficial,"+ConstantesBD.codigoNuncaValido+") as tarifario_oficial, "+ 
							" et.metodo_ajuste "+ 
							" from tarifas_inv_con_ent_sub t "+ 
							" INNER JOIN esquemas_tarifarios et ON(et.codigo = t.esquema_tarifario) "+ 
							" where t.clase_inventario is null and t.contrato_entidad_sub="+codigoContrato + 
							" and t.fecha_vigencia <= '"+ fechaVigencia + "'"+
							" and t.activo = '" + ConstantesBD.acronimoSi+"' order by t.fecha_vigencia desc ";
						ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
						logger.info("3-->"+cadena);
						rs=new ResultSetDecorator(ps.executeQuery());
						if(rs.next())
						{
							esquema.setCodigo(rs.getInt("codigo"));
							esquema.setNombre(rs.getString("nombre"));
							esquema.setTarifarioOficial(new InfoDatosInt(rs.getInt("tarifario_oficial")));
							esquema.setMetodoAjuste(new InfoDatos(rs.getString("metodo_ajuste")));
						}
						
					}
				}
				
			}
			
		}
		catch(SQLException e)
		{
			logger.error("error en obtenerEsquemaTarifarioServicioArticuloEntidadSub: "+e);
		}
		return esquema;
	}
	
}
