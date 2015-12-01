
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaImpresionTrasladosDao 
{
    /**
     * metodo para realizar la busqueda avanzada de
     * listado traslado almacen
     * @param con Connection
     * @param vo HashMap
     * @return HashMap
     */
    public abstract HashMap ejecutarBusquedaAvanzadaTraslados(Connection con,HashMap vo);
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public abstract HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud);
    
    /**
     * Metodo encargado armar toda la consulta Detallada por almacen
     * @param vo
     * -----------------------------------
     * KEY'S DEL MAPA VO
     * -----------------------------------
     * -- codAlmacenDespacha
     * -- codAlmacenSolicita
     * -- noTrasladoInicial
     * -- noTrasladoFinal
     * -- fechaInicialSolicitud
     * -- fechaInicialDespacho
     * -- usuarioSolicita
     * -- usuarioDespacha
     * -- codEstado
     * -- prioridad
     * -- claseInventario
     * -- articulo
     * -- tipoCodigoArticulo
     * @return String con la consulta
     */
    public String consultasReportes (HashMap vo);
    
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public HashMap ejecutarConsulta (Connection connection,String consulta);
    
}
