
/*
 * Creado   23/01/2006
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
 * Interface comunicación con los objetos DAO para
 * acceder a la fuente de datos
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface DespachoTrasladoAlmacenDao 
{
    /**
     * metodo para consultar el listado de 
     * traslados almacen para un almacen
     * dado y en estad cerrado
     * @param con Connection
     * @param estado int
     * @param almacenSolicitado int
     * @return HashMap
     */
    public abstract HashMap consultaSolicitudesTraslados(Connection con,int estado,int almacenSolicitado);
    /**
     * metodo para consultar el detalle de la
     * solicitud
     * @param con Connection
     * @param numeroSolicitud int
     * @return HashMap
     */
    public abstract HashMap consultaDetalleSolicitud(Connection con,int numeroSolicitud);
    /**
     * metodo para insertar la información del 
     * despacho
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public abstract boolean insertarDespacho(Connection con,HashMap vo);
    /**
     * metodo para insertar el detalle del 
     * despacho
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public abstract boolean insertarDetalleDespacho(Connection con,HashMap vo);
    /**
     * metodo para actualizar el detalle de un
     * despacho
     * @param con Connection
     * @param vo Hashmap
     * @return boolean
     */
    public abstract boolean actualizarDetalleDespacho(Connection con,HashMap vo);
    /**
     * metodo para consultar la cantidad
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public abstract HashMap consultarCantidadRegistroDetalleDespacho(Connection con,HashMap vo);
    /**
     * metodo para verificar si un registro
     * se encuentra insertado en la BD
     * @param con Connection
     * @param numeroSolicitud int 
     * @return boolean 
     */
    public boolean existeRegistroDespachoBD(Connection con,int numeroSolicitud);
    /**
     * metodo para insertar el usuario que realiza
     * el despacho
     * @param con Connection
     * @param usuario String
     * @param numeroSolicitud int
     * @return boolean
     */
    public abstract boolean actualizarUsuarioDespacho(Connection con,String usuario,int numeroSolicitud);
}
