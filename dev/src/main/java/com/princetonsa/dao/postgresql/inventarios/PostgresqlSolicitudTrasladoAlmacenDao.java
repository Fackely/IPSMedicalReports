
/*
 * Creado   11/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.SolicitudTrasladoAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseSolicitudTrasladoAlmacenDao;

/**
 * clase para implementar los metodos propios
 * de postgresql para acceder a la fuente de datos
 *
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlSolicitudTrasladoAlmacenDao implements SolicitudTrasladoAlmacenDao 
{
    /**
     * metodo para insertar la información
     * general de la solicitud
     * @param con Connection
     * @param numeroSol int 
     * @param almacenSolicitado int
     * @param almacenSolicita int
     * @param estado int 
     * @param esPrioritario boolean 
     * @param fechaElaboracion String
     * @param horaElaboracion String
     * @param usuarioElabora String
     * @param fechaGrabacion String
     * @param horaGrabacion String
     * @param observaciones String
     * @return booelan 
     */    
    public boolean insertarInformacionGeneralSolicitud(Connection con,int numeroSol,int almacenSolicitado,int almacenSolicita,int estado,boolean esPrioritario,String fechaElaboracion,String horaElaboracion,String usuarioElabora,String fechaGrabacion,String horaGrabacion,String observaciones)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.insertarInformacionGeneralSolicitud(con, numeroSol, almacenSolicitado, almacenSolicita, estado, esPrioritario, fechaElaboracion, horaElaboracion, usuarioElabora, fechaGrabacion, horaGrabacion,observaciones);   
    }
    /**
     * metodo para insertar el detalle de la 
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param codArticulo int
     * @param cantidad int
     * @return booelan
     */
    public boolean insertarDetalleSolicitud(Connection con,int numeroSolicitud,int codArticulo,int cantidad)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.insertarDetalleSolicitud(con, numeroSolicitud, codArticulo, cantidad);
    }
    /**
     * metodo para actualizar el estado de la solicitud
     * @param con Connection 
     * @param estado int
     * @param numeroSolicitud int
     * @param institucion int
     * @return boolean
     */
    public boolean actualizarEstadoSolicitud(Connection con,int estado,int numeroSolicitud,int institucion)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.actualizarEstadoSolicitud(con, estado, numeroSolicitud, institucion);
    }
    /**
     * metodo para eliminar articulos del detalle
     * de la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return boolean
     */
    public boolean eliminarArticuloDetalleSolicitud(Connection con,int numeroSolicitud,int articulo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.eliminarArticuloDetalleSolicitud(con, numeroSolicitud, articulo);
    }
    /**
     * metodo para insertar la información del cierre dela
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param usuario Stirng
     * @return boolean
     */
    public boolean insertarInformacionCierre(Connection con,int numeroSolicitud,String usuario)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.insertarInformacionCierre(con, numeroSolicitud, usuario); 
    }
    /**
     * metodo para insertar la información de la 
     * anulación de la solicitud
     * @param con Connection
     * @param numeroSolicitud int 
     * @param usuario String
     * @param motivo String
     * @return boolean
     */
    public boolean insertarAnulacionSolicitud(Connection con,int numeroSolicitud,String usuario,String motivo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.insertarAnulacionSolicitud(con, numeroSolicitud, usuario, motivo);
    }
    /**
     * metodo para generar la busqueda avanzada
     * de solicitudes
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     */
    public HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.ejecutarBusquedaAvanzada(con, vo);
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
        return SqlBaseSolicitudTrasladoAlmacenDao.consultaDetalleSolicitud(con, numeroSolicitud);
    }
    /**
     * metodo para actualizar la información 
     * general de la solicitud
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public boolean actualizarInformacionGeneralSolicitud(Connection con,HashMap vo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.actualizarInformacionGeneralSolicitud(con, vo);
    }
    /**
     * metodo para actualizar el detalle de
     * la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param cantidad int
     * @param articulo int
     * @return boolean
     */
    public boolean actualizarDetalleSolicitud(Connection con,int numeroSolicitud,int articulo,int cantidad)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.actualizarDetalleSolicitud(con, numeroSolicitud, cantidad,articulo);
    }
    /**
     * metodo para consultar la cantidad de un
     * articulo para una solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return int
     */
    public int consultarInfoArticuloSolicitud(Connection con,int numeroSolicitud,int articulo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.consultarInfoArticuloSolicitud(con, numeroSolicitud, articulo);
    }
    /**
     * metodo para consultar la información
     * del detalle de una solicitud para un
     * articulo
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return HashMap
     */
    public HashMap consultarDetalleSolicitudUnArticulo(Connection con,int numeroSolicitud,int articulo)
    {
        return SqlBaseSolicitudTrasladoAlmacenDao.consultarDetalleSolicitudUnArticulo(con, numeroSolicitud, articulo);   
    }
}
