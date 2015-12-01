/*
 * Created on 24-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author armando
 *
 * Princeton 24-sep-2004
 * Clase para manejar la recepcion de devoluciones de pedidos
 */
public class SqlBaseRecepcionDevolucionPedidos {
	
    /**
     * Variable para manejar los errores loger de la funcionalidad
     */
    private static Logger logger = Logger.getLogger(SqlBaseRecepcionDevolucionPedidos.class);
    /**
     * Variable para realizar la consulta de todas las devoluciones que se tienen los pedidos del  centro de costo
     */
    private static String consutalDevoluciones= "SELECT " +
											    	"devp.codigo as numeroDevolucion," +
											    	"devp.estado as codigoEstadoDevolucion," +
											    	"getEstadoDevolucion(devp.estado) as estadoDevolucion," +
											    	"devp.fecha||' '||devp.hora as fechaHoraDevolucion," +
											    	"p.centro_costo_solicitante as codigoArea," +
											    	"getnomcentrocosto(p.centro_costo_solicitante) as area, " +
											    	"getnomcentroatencion(cc.centro_atencion) as centroatencion, " +
											    	"p.centro_costo_solicitado as codigoFarmacia," +
											    	"getnomcentrocosto(p.centro_costo_solicitado) as farmacia," +
											    	"devp.motivo as motivo," +
											    	"devp.usuario as usuario," +
											    	"devp.es_qx AS es_qx " +
											    "FROM " +
											    	"devolucion_pedidos devp " +
											    	"inner join detalle_devol_pedido ddp on (ddp.devolucion=devp.codigo) " +
											    	"inner join pedido p on (p.codigo=ddp.pedido)" +
											    	"inner join centros_costo cc on (p.centro_costo_solicitante=cc.codigo) " +
											    "WHERE " +
											    	"devp.estado="+ConstantesBD.codigoEstadoDevolucionGenerada+" "+
											    	"AND p.centro_costo_solicitado=? " +
											    	"AND devp.institucion = ? " +
											    	"group by devp.codigo,devp.fecha,devp.hora,devp.usuario,p.centro_costo_solicitado,p.centro_costo_solicitante,cc.centro_atencion,devp.motivo,devp.estado,devp.es_qx order by devp.codigo ";
    /**
     * Varible para realizar la consulta del detalle de una devolucion.
     */
    private static String consultarDetalleDevolucion="SELECT " +
    														" detal.codigo as codigo, " +
    														" ped.codigo as pedido," +
    														" detal.articulo as codigoArticulo," +
    														" getfulldescriptionarticulo(articulo) as articulo," +
    														" u.nombre as unidadmedida," +
    														" detal.cantidad as cantidad," +
    														" case when detal.lote is null then '' else lote end as lote," +
    														" case when detal.fecha_vencimiento is null then '' else to_char(detal.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento, " +
    														" autoriz.consecutivo as autorizacion, "+
    														" getnombrepersona(paci.codigo_paciente) AS nompaciente, "+
    														" getidpaciente(paci.codigo_paciente) AS idpaciente "+ 
    												" FROM detalle_devol_pedido detal" +
	    												" inner join articulo a on articulo=a.codigo " +
	    												" inner join unidad_medida u on (a.unidad_medida=u.acronimo) " +
	    												" inner join pedido ped ON (ped.codigo=detal.pedido) "+
	    												" left join registro_entrega_ent_sub entreg ON (entreg.codigo_pk=ped.codigo) "+
	    												" left join autorizaciones_entidades_sub autoriz ON (autoriz.consecutivo=entreg.autorizacion_ent_sub) "+
	    												" left join pacientes paci ON (paci.codigo_paciente=autoriz.codigo_paciente) "+
    												" WHERE devolucion=? and cantidad>0";
    /**
     * Varible para realizar la consulta del detalle de una devolucion Qx.
     */
    private static String consultarDetalleDevolucionQxStr = "SELECT "+ 
														    	"ddp.codigo as codigo, "+ 
														    	"ddp.pedido as pedido, "+
														    	"ddp.articulo as codigoArticulo, "+
														    	"getfulldescriptionarticulo(ddp.articulo) as articulo, "+
														    	"pq.codigo AS numero_peticion, "+
														    	"getIngresoPeticionQx(pq.codigo) AS numero_ingreso, "+
														    	"getnombrepersona(pq.paciente) AS paciente, "+
																"getidpaciente(pq.paciente) AS id_paciente, "+
														    	"u.nombre as unidadmedida, "+
														    	"ddp.cantidad as cantidad, "+
														    	"coalesce(ddp.lote,'') as lote, "+
														    	"coalesce(to_char(ddp.fecha_vencimiento,'"+ConstantesBD.formatoFechaAp+"'),'') as fechavencimiento "+ 
													    	"from " +
													    		"detalle_devol_pedido ddp "+ 
														    	"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.pedido=ddp.pedido) "+ 
														    	"INNER JOIN peticion_qx pq ON(pq.codigo=ppq.peticion) "+ 
														    	"inner join articulo a on (ddp.articulo=a.codigo) "+ 
														    	"inner join unidad_medida u on (a.unidad_medida=u.acronimo) "+ 
														    "where " +
														    	"ddp.devolucion=? and ddp.cantidad>0";
    
    /**
     *Variable para manejar la insercion del maestro de una recepcion de devolucion 
     */
    ///modificiada.
    private static String insertarRecepcionDevolucion="insert into recepciones_pedidos(devolucion,fecha,hora,usuario) values (?,?,?,?)";
    /**
     *Variable para manejar la insercion del maestro de una recepcion de devolucion 
     */
    private static String insertarRecepcionDevolucionDetalle="insert into detalle_recep_pedidos(codigo,numerodevolucion,cantidadrecibida,lote,fecha_vencimiento,articulo,tipo_recepcion,almacen_consignacion,proveedor_compra,proveedor_catalogo) values (?,?,?,?,?,?,?,?,?,?)";
    /**
     * Variable para actualizar una devolucion y pasarla de generada a recibida
     */
    private static String actualizarDevolucion="update devolucion_pedidos set estado=? where codigo=?";
    /**
     *Variable para manejar la consulta de todas las recepciones de devolucion de pedidos
     *Aun no tiene el metodo, se tienen en cuenta la consulta para futuro
     */////////////////////
    //private static String consultarRecepcionDevoluciones="select devolucion as numeroDevolucion, fecha||' '||hora as fechaHoraRecepcionDevolucion, usuario as usuarioRecibio from recepciones_pedidos";
    /**
     *Variable para manejar la consulta de una recepcion de devolucion de pedidos
     */
    //private static String consultarRecepcionDevolucion="SELECT 	rp.devolucion as numeroDevolucion,rp.fecha||' '||rp.hora as fechaHoraDevolucion, rp.areadevuelve as codigoArea,getnomcentrocosto(rp.areadevuelve) as area,dp.motivo as motivo,rp.usuario as usuario from recepciones_pedidos rp inner join devolucion_pedidos dp on (dp.codigo=rp.devolucion) where rp.devolucion=? group by numerodevolucion,fechahoradevolucion,rp.usuario,rp.areadevuelve,dp.motivo";
    private static String consultarRecepcionDevolucion="SELECT " +
    	"rp.devolucion as numeroDevolucion," +
    	"rp.fecha||' '||rp.hora as fechaHoraDevolucion, " +
    	"p.centro_costo_solicitante as codigoArea," +
    	"getnomcentrocosto( p.centro_costo_solicitante) as area," +
    	"dp.motivo as motivo," +
    	"rp.usuario as usuario," +
    	"dp.es_qx AS es_qx " +
    	"from recepciones_pedidos rp " +
    	"inner join devolucion_pedidos dp on (dp.codigo=rp.devolucion) " +
    	"inner join detalle_devol_pedido ddp on(ddp.devolucion=dp.codigo) " +
    	"inner join pedido p on(p.codigo=ddp.pedido) " +
    	"where rp.devolucion=? " +
    	"group by rp.devolucion,rp.fecha,rp.hora,rp.usuario,dp.motivo,p.centro_costo_solicitante,dp.es_qx ";
    /**
     * Variable para manejar la consulta del detalle de una recepcion.
     */
    private static String consultarDetalleRecepcionDevolucion="SELECT " +
    																" drp.codigo as codigo," +
    																" drp.numerodevolucion as devolucion, " +
    																" ddp.pedido as pedido," +
    																" drp.articulo as codigoArticulo," +
    																" getfulldescriptionarticulo(drp.articulo) as articulo," +
    																" u.nombre as unidadmedida, " +
    																" ddp.cantidad as cantidaddevuelta," +
    																" drp.cantidadrecibida as cantidadrecibida," +
    																" case when drp.lote is null then '' else drp.lote end as lote," +
    																" case when drp.fecha_vencimiento is null then '' else to_char(drp.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
    														" from detalle_recep_pedidos drp " +
    														" inner join detalle_devol_pedido ddp on (ddp.codigo= drp.codigo) " +
    														" inner join articulo a on ddp.articulo=a.codigo " +
    														" inner join unidad_medida u on (a.unidad_medida=u.acronimo) " +
    														" where numerodevolucion=?";

    /**
     * Cadena que consulta el detalle de la recepción de la devolucion Qx
     */
    private static final String consultarDetalleRecepcionDevolucionQx = "SELECT "+ 
    	"drp.codigo as codigo, "+
    	"drp.numerodevolucion as devolucion, "+ 
    	"ddp.pedido as pedido, "+
    	"drp.articulo as codigoArticulo, "+
    	"getfulldescriptionarticulo(drp.articulo) as articulo, "+
    	"pq.codigo AS numero_peticion, "+
    	"getIngresoPeticionQx(pq.codigo) AS numero_ingreso, "+
    	"p.primer_apellido || ' ' || coalesce(p.segundo_apellido,'') || ' ' || p.primer_nombre || ' ' || coalesce(p.segundo_nombre,'') AS paciente, "+
    	"p.tipo_identificacion || ' ' || p.numero_identificacion AS id_paciente, "+
    	"u.nombre as unidadmedida, "+ 
    	"ddp.cantidad as cantidaddevuelta, "+
    	"drp.cantidadrecibida as cantidadrecibida, "+
		"coalesce(drp.lote,'') as lote, "+
		"coalesce(to_char(drp.fecha_vencimiento,'"+ConstantesBD.formatoFechaAp+"'),'') as fechavencimiento "+ 
		"from detalle_recep_pedidos drp "+ 
		"inner join detalle_devol_pedido ddp on (ddp.codigo= drp.codigo) "+ 
		"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.pedido=ddp.pedido) "+ 
		"INNER JOIN peticion_qx pq ON(pq.codigo=ppq.peticion) "+ 
		"INNER JOIN personas p ON(p.codigo=pq.paciente) "+ 
		"inner join articulo a on (ddp.articulo=a.codigo) "+ 
		"inner join unidad_medida u on (u.acronimo=a.unidad_medida) "+ 
		"where numerodevolucion=?";
    
    /**
     * Variable para obtener el costo unitario del despacho
     */
    private static final String obtenerCostoUnitarioDespachoStr = "SELECT " +
    		"costo_unitario AS costo " +
    		"FROM detalle_despacho_pedido where pedido = ? and articulo = ?";
    
    /**
     * Cadena que actualiza el costo unitario de una devolucion
     */
    private static final String actualizarCostoUnitarioDevolucionStr = "UPDATE " +
																    	"detalle_recep_pedidos " +
																    	"SET costo_unitario = ? " +
																    	"where " +
																    	"codigo IN " +
																    	"(SELECT codigo FROM detalle_devol_pedido WHERE devolucion = ? and pedido = ? and articulo = ?)";
    
    /**
     * Cadena que actualiza la cantidad de devolucion del pedido
     */
    private static final String actualizarCantidadDevolucionStr = "UPDATE " +
																    	"detalle_devol_pedido " +
																  "SET cantidad = ? " +
																  "WHERE " +
																    	"devolucion = ? " +
																    	"AND pedido = ? " +
																    	"AND articulo = ? ";
    /**
     * para Manejo de transacciones y acceso a la fuente de datos
     */
	private static String tipoBD = System.getProperty("TIPOBD");
	
	private static DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
    /**
     * Metodo que realiza la consulta de todas las devoluciones en estado
     * Generada.
     * @param con, Conexxion
     * @return, colecciion, coleccion con los datos de la consulta.
     */
    
    @SuppressWarnings("rawtypes")
	public static Collection consultarDevoluciones(Connection con,int centroCostoSolicitado,int institucion)
    {
    	 Collection coleccion=null;
    	 PreparedStatementDecorator ps;
    	 try
         {  logger.info("Cconsulta RECEPCION devolucion pedidos: ---->"+consutalDevoluciones);
         	ps= new PreparedStatementDecorator(con.prepareStatement(consutalDevoluciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
         	ps.setInt(1,centroCostoSolicitado);
         	ps.setInt(2,institucion);
         	coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
         }
         catch(SQLException e)
         {
             logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DEVOLUCION"+e.toString());
         }
         return coleccion;
    }
    /**
     * Metodo para realizar la consulta de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return collection,Coleccion con los datos
     */
    @SuppressWarnings("rawtypes")
	public static Collection consultarUnaDevolucion(Connection con,int devolucion)
    {
    	Collection coleccion=null;
        PreparedStatementDecorator ps;
        try
        {	logger.info("\n\n consulta de DETALLE devolucion de pedidos: --->"+consultarDetalleDevolucion+"\n\n");
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,devolucion);
            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DETALLE DE DEVOLUCION"+e.toString());
        }
        return coleccion;
    }
    
    /**
     * Método que realiza la consulta del detalle de una devolucion quirúrgica
     * @param con
     * @param devolucion
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
	public static Collection consultarUnaDevolucionQx(Connection con,int devolucion)
    {
    	Collection coleccion=null;
        PreparedStatementDecorator ps;
        try
        {	logger.info("\n\n consulta DETALLE devolucion pedido QUIRURGICO:-->"+consultarDetalleDevolucionQxStr+"\n\n");
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleDevolucionQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,devolucion);
            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DETALLE DE DEVOLUCION QX: "+e.toString());
        }
        return coleccion;
    }
    /**
     * Metodo para realizar la consulta de una recepcion de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return Resultset,Resultado de la consulta
     */
    public static ResultSetDecorator consultarMaestroRecepcion(Connection con,int devolucion)
    {
    	ResultSetDecorator rs=null;
        PreparedStatementDecorator ps;
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarRecepcionDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,devolucion);
            rs=new ResultSetDecorator(ps.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE RECEPCION DE DETALLE DE DEVOLUCION"+e.toString());
        }
        return rs;
    }
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    
    @SuppressWarnings("rawtypes")
	public static Collection consultarDetalleRecepcion(Connection con,int devolucion)
    {
    	Collection coleccion=null;
        PreparedStatementDecorator ps;
        try
        {	logger.info("\n consulta DETALLE RECEPCION devolucion: "+consultarDetalleRecepcionDevolucion);
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleRecepcionDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,devolucion);
            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE DETALLE RECEPCION DE LA DEVOLUCION"+e.toString());
        }
        return coleccion;
    }
    
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion Qx
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    @SuppressWarnings("rawtypes")
	public static Collection consultarDetalleRecepcionQx(Connection con,int devolucion)
    {
    	Collection coleccion=null;
        PreparedStatementDecorator ps;
        try
        {	logger.info("\n consulta DETALLE RECEPCION devolucion QUIRURGICO: "+consultarDetalleRecepcionDevolucion);
            ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleRecepcionDevolucionQx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,devolucion);
            coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
        }
        catch(SQLException e)
        {
            logger.warn("NO SE PUDO REALIZAR LA CONSULTA DE DETALLE RECEPCION DE LA DEVOLUCION QX: "+e.toString());
        }
        return coleccion;
    }
    
    /**
     * Metodo que realiza la insercion de un registro
     * @param con
     * @param devolucion
     * @param area
     * @param fecha
     * @param hora
     * @param usuario
     * @throws SQLException, Se manaeja en el action para terminar la transaccion.
     */
    public static int insertarRecepcionMaestro(Connection con,int devolucion,String fecha,String hora,String usuario)
    {
    	PreparedStatementDecorator ps;
    	try 
	        {  logger.info("\n Inserta RECEPCION dev pedido: "+insertarRecepcionDevolucion);
	        ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRecepcionDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,devolucion);
	        ps.setString(2,fecha);
	        ps.setString(3,hora);
	        ps.setString(4,usuario);
	        return ps.executeUpdate();
	    } catch (SQLException e) 
	        {
	    		try {
					myFactory.abortTransaction(con);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	            logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
	        }
	   return -1;
    }
    /**
     * Metodo que realiza la insercion de un registro
     * @param con
     * @param articulo 
     * @param devolucion@param cantidadRecibida2
     * @param cantidadDevuelta
     * @param pedido
     * @param articulo
     * @param proveedorCatalog 
     * @param proveedorCompra 
     * @param almacenConsignacion 
     * @param tipoRecepcion 
     * @param cantidaddevuelta
     * @param cantidadrecibida
     * 
     */
    public static int insertarRecepcionDetalle(Connection con,int codigo,int devolucion, int cantidadRecibida,String lote,String fechaVencimiento, int articulo, String tipoRecepcion, String almacenConsignacion, String proveedorCompra, String proveedorCatalog)
	{
    	PreparedStatementDecorator ps;
    	try 
        { 	logger.info("\n Inserta DETALLE RECEPCION dev pedido: "+insertarRecepcionDevolucionDetalle);
	    	ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRecepcionDevolucionDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1,codigo);
	    	ps.setInt(2,devolucion);
	    	ps.setInt(3,cantidadRecibida);
	        if(lote.trim().equals(""))
	        {
	        	ps.setObject(4, null);
	        	ps.setObject(5, null);
	        }
	        else
	        {
	        	ps.setString(4, lote);
	        	if(fechaVencimiento.trim().equals(""))
	        	{
	        		ps.setObject(5, null);
	        	}
	        	else
	        	{
	        		ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));	
	        	}
	        }    
	        ps.setInt(6, articulo);
	        
	        if(!UtilidadTexto.isEmpty(tipoRecepcion))
	        	ps.setString(7, tipoRecepcion);
	        else 
	        	ps.setObject(7, null);
	        
	        if(!UtilidadTexto.isEmpty(almacenConsignacion))
	        	ps.setString(8, almacenConsignacion);
	        else 
	        	ps.setObject(8, null);
	        
	        if(!UtilidadTexto.isEmpty(proveedorCompra))
	        	ps.setString(9, proveedorCompra);
	        else 
	        	ps.setObject(9, null);
	        if(!UtilidadTexto.isEmpty(proveedorCatalog))
	        	ps.setString(10, proveedorCatalog);
	        else 
	        	ps.setObject(10, null);

	    	return ps.executeUpdate();
		} catch (SQLException e) 
	    {
			try {
				myFactory.abortTransaction(con);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
	    }
		return -1;
		}
	/**
	 * Metodo que actualiza el estado de una devolucion.
	 * @param con, conexion
	 * @return
	 */
	public static int actualizarEstadoDevolucion(Connection con,int devolucion,int estadoDevolucion) 
	{
		PreparedStatementDecorator ps;
		try 
	    { 	logger.info("\n Actualiza Devolucuion pedido: "+actualizarDevolucion);
	    	ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1,estadoDevolucion);
	    	ps.setInt(2,devolucion);
	    	return ps.executeUpdate();
		} catch (SQLException e) 
	    {
			try {
				myFactory.abortTransaction(con);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	        logger.warn("No se pudo realizar la insercion, la transacion se ha abortado "+e.toString());
	    }
		return -1;
	}
	
	//@todo aqui se puede realizar la actualizacion de cantidades recibidas en los saldos de la frarmaci y grabar el costo de lo recibdo. debe generarse el metodo.
	
	/**
	 * Método implementado para obtener el costo unitario del despacho del pedido
	 */
	public static double obtenerCostoUnitarioDespacho(Connection con,int pedido,int articulo)
	{
		try
		{	logger.info("\n Obtiene costo unitario despacho: "+obtenerCostoUnitarioDespachoStr);
			PreparedStatementDecorator ps = null;
			ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerCostoUnitarioDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,pedido);
			ps.setInt(2,articulo);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getDouble("costo");
			else
				return -1;
			
		}
		catch(SQLException e)
		{
			logger.warn("Error en obtenerCostoUnitarioDespacho de SqlBaseRecepcionDevolucionPedidoDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para actualizar el costo unitario de un articulo
	 * de una devolucion
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param costo
	 * @return
	 */
	public static int actualizarCostoUnitarioDevolucion(Connection con,int devolucion,int pedido,int articulo,double costo)
	{
		try
		{	logger.info("\n Actualiza costo unitario despacho: "+actualizarCostoUnitarioDevolucionStr);
			PreparedStatementDecorator ps = null;
			ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCostoUnitarioDevolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,costo);
			ps.setInt(2,devolucion);
			ps.setInt(3,pedido);
			ps.setInt(4,articulo);
			
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn("Error en actualizarCostoUnitarioDevolucion de SqlBaseRecepcionDevolucionPedidoDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para actualizar el costo unitario de un articulo
	 * de una devolucion
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param cantidadDevolucion
	 * @return
	 */
	public static int actualizarDevolucion(Connection con, int devolucion, int pedido, int articulo, int cantidadDevolucion)
	{
		try
		{	logger.info("\n Actualiza cantidad devolucion: "+actualizarCantidadDevolucionStr);
			PreparedStatementDecorator ps = null;
			ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCantidadDevolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,cantidadDevolucion);
			ps.setInt(2,devolucion);
			ps.setInt(3,pedido);
			ps.setInt(4,articulo);
			
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn("Error en actualizarDevolucion de SqlBaseRecepcionDevolucionPedidoDao: "+e);
			return -1;
		}
	}
    
}