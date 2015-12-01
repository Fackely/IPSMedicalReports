
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaImpresionTrasladosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaImpresionTrasladosDao;

public class PostgresqlConsultaImpresionTrasladosDao implements ConsultaImpresionTrasladosDao 
{
    /**
     * metodo para realizar la busqueda avanzada de
     * listado traslado almacen
     * @param con Connection
     * @param vo HashMap
     * @return HashMap
     */
    public HashMap ejecutarBusquedaAvanzadaTraslados(Connection con,HashMap vo)
    {
        return SqlBaseConsultaImpresionTrasladosDao.ejecutarBusquedaAvanzadaTraslados(con, vo);
    }
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud)
    {
        return SqlBaseConsultaImpresionTrasladosDao.consultaDetalleSolicitud(con, numeroSolicitud);
    }
    
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
    public String consultasReportes (HashMap vo)
    {
   
    	/**
    	 * Alberto Ovalle
    	 * mt4086
    	 * Solucion permita resultados independientes
    	 */
    	//return SqlBaseConsultaImpresionTrasladosDao.consultasReportes(vo);
    	return SqlBaseConsultaImpresionTrasladosDao.consultasReportespostgresql(vo);
   
    }
    
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	return SqlBaseConsultaImpresionTrasladosDao.ejecutarConsulta(connection, consulta);
    }
}
