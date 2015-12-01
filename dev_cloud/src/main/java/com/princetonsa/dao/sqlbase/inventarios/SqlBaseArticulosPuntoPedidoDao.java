/*
 * Created on 2/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseArticulosPuntoPedidoDao
{

    /**
     * Variable para manejar los errores de la clase.
     */
    private static Logger logger=Logger.getLogger(SqlBaseArticulosPuntoPedidoDao.class);
    
    /**
     * Cadena que realiza la consulta de los motivods devoulicon de inventario.
     */
    private static String cadenaConsultaGeneral=" SELECT aa.articulo as codigo,a.descripcion as descripcion,a.concentracion as concentracion,a.unidad_medida as unidadmedida,u.nombre as nomunidadmedida,a.stock_minimo as stockminimo,a.stock_maximo as stockmaximo,a.punto_pedido as puntopedido,gettotalexisarticulos(aa.articulo,aa.institucion) as existencias,case when gettotalexisarticulos(aa.articulo,aa.institucion) < a.punto_pedido then 'true' else 'false' end as menorpuntopedido from (SELECT articulo,institucion from articulos_almacen group by articulo,institucion) aa inner join articulo a on(aa.articulo=a.codigo) inner join unidad_medida u on (a.unidad_medida=u.acronimo) ";
    private static String cadenaWhereGeneral=" where aa.institucion=? and ( gettotalexisarticulos(aa.articulo,aa.institucion) <= a.punto_pedido ";
    //private static String group=" group by aa.articulo,a.descripcion,a.concentracion,a.unidad_medida,u.nombre,a.stock_minimo,a.stock_maximo,a.punto_pedido,aa.institucion  order by a.descripcion ";
    private static String cadenaDetalle="select aa.almacen as almacen,c.nombre as nombrealmacen,getnomcentroatencion(c.centro_atencion) as centroatencion, aa.existencias as existencias from articulos_almacen aa inner join centros_costo c on(aa.almacen=c.codigo) where articulo=? order by nombrealmacen";
    
    
    /**
     * @param con
     * @param institucion
     * @param porcAleta
     * @return
     */
    public static HashMap consultarArticulosGeneral(Connection con, String porcAlerta, int institucion)
    {
        HashMap mapa=new HashMap();
        HashMap mapaDetalle=new HashMap();
        String wherePorcAlerta=" ";
        if(!porcAlerta.trim().equals(""))
        {
            wherePorcAlerta=" or (gettotalexisarticulos(aa.articulo,aa.institucion)-a.punto_pedido) <= (a.punto_pedido*"+porcAlerta+"/100) ) ";
        }
        else
        {
        	wherePorcAlerta=" ) ";
        }
        String cadena=cadenaConsultaGeneral+cadenaWhereGeneral+wherePorcAlerta;
        
        logger.info(cadena);
        
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by a.descripcion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
            {
                mapaDetalle.clear();
                ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codigo_"+i)+""));
                mapaDetalle=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
                mapa.put("detalle_"+i, mapaDetalle.clone());
            }
        }
        catch (SQLException e)
        {
            logger.error("ERROR CONSULTANDO LOS PUNTO PEDIDO");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }


    
    /**
     * @param con
     * @param porcAlerta
     * @param codBusqueda
     * @param desBusqueda
     * @return
     */
    public static HashMap consultarArticulosAvanzada(Connection con, String porcAlerta, String codBusqueda, String desBusqueda, int institucion)
    {
        HashMap mapa=new HashMap();
        HashMap mapaDetalle=new HashMap();
        String wherePorcAlerta=" ";
        
        String codigoArticuloBusqueda = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
		logger.info("===>Tipo de Codigo Articulo para Busqueda: "+codigoArticuloBusqueda);
		
        if(!porcAlerta.trim().equals(""))
        {
            wherePorcAlerta=" or (gettotalexisarticulos(aa.articulo,aa.institucion)-a.punto_pedido) <= (a.punto_pedido*"+porcAlerta+"/100) )";
        }
        else
        {
        	wherePorcAlerta=" ) ";
        }
        
        String cadena=cadenaConsultaGeneral+cadenaWhereGeneral+wherePorcAlerta;
        if(!codBusqueda.trim().equals(""))
        {
            if(codigoArticuloBusqueda.equals(ConstantesIntegridadDominio.acronimoAxioma))
            	cadena = cadena+" AND aa.articulo = "+Utilidades.convertirAEntero(codBusqueda);
            else if(codigoArticuloBusqueda.equals(ConstantesIntegridadDominio.acronimoInterfaz))
            	cadena = cadena+" AND a.codigo_interfaz = '"+codBusqueda+"'";
        }
        if(!desBusqueda.trim().equals(""))
        {
            cadena=cadena+" AND UPPER(a.descripcion) LIKE UPPER('%"+desBusqueda+"%')";
        }
        
        //cadena=cadena+group;
        logger.info(cadena);
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
            {
                mapaDetalle.clear();
                ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codigo_"+i)+""));
                mapaDetalle=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
                mapa.put("detalle_"+i, mapaDetalle.clone());
            }
        }
        catch (SQLException e)
        {
            logger.error("ERROR CONSULTANDO LOS PUNTO PEDIDO");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

}
