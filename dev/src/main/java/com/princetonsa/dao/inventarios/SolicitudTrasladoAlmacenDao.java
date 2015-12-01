
/*
 * Creado   11/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Clase para manejar
 *
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface SolicitudTrasladoAlmacenDao 
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
    public abstract boolean insertarInformacionGeneralSolicitud(Connection con,int numeroSol,int almacenSolicitado,int almacenSolicita,int estado,boolean esPrioritario,String fechaElaboracion,String horaElaboracion,String usuarioElabora,String fechaGrabacion,String horaGrabacion,String observaciones);
    /**
     * metodo para insertar el detalle de la 
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param codArticulo int
     * @param cantidad int
     * @return booelan
     */
    public abstract boolean insertarDetalleSolicitud(Connection con,int numeroSolicitud,int codArticulo,int cantidad);
    /**
     * metodo para actualizar el estado de la solicitud
     * @param con Connection 
     * @param estado int
     * @param numeroSolicitud int
     * @param institucion int
     * @return boolean
     */
    public abstract boolean actualizarEstadoSolicitud(Connection con,int estado,int numeroSolicitud,int institucion);
    /**
     * metodo para eliminar articulos del detalle
     * de la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return boolean
     */
    public abstract boolean eliminarArticuloDetalleSolicitud(Connection con,int numeroSolicitud,int articulo);
    /**
     * metodo para insertar la información del cierre dela
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param usuario Stirng
     * @return boolean
     */
    public abstract boolean insertarInformacionCierre(Connection con,int numeroSolicitud,String usuario);
    /**
     * metodo para insertar la información de la 
     * anulación de la solicitud
     * @param con Connection
     * @param numeroSolicitud int 
     * @param usuario String
     * @param motivo String
     * @return boolean
     */
    public abstract boolean insertarAnulacionSolicitud(Connection con,int numeroSolicitud,String usuario,String motivo);
    /**
     * metodo para generar la busqueda avanzada
     * de solicitudes
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     */
    public abstract HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo);
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public abstract HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud);
    /**
     * metodo para actualizar la información 
     * general de la solicitud
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public abstract boolean actualizarInformacionGeneralSolicitud(Connection con,HashMap vo);
    /**
     * metodo para actualizar el detalle de
     * la solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param cantidad int
     * @param articulo int
     * @return boolean
     */
    public abstract boolean actualizarDetalleSolicitud(Connection con,int numeroSolicitud,int articulo,int cantidad);
    /**
     * metodo para consultar la cantidad de un
     * articulo para una solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return int
     */
    public abstract int consultarInfoArticuloSolicitud(Connection con,int numeroSolicitud,int articulo);
    /**
     * metodo para consultar la información
     * del detalle de una solicitud para un
     * articulo
     * @param con Connection
     * @param numeroSolicitud int
     * @param articulo int
     * @return HashMap
     */
    public abstract HashMap consultarDetalleSolicitudUnArticulo(Connection con,int numeroSolicitud,int articulo);
} 
