
/*
 * Creado   23/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.DespachoTrasladoAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseDespachoTrasladoAlmacenDao;

/**
 * Clase que implementa los metodos para
 * accesar a la fuente de datos postgreSQL
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlDespachoTrasladoAlmacenDao implements DespachoTrasladoAlmacenDao 
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
    public HashMap consultaSolicitudesTraslados(Connection con,int estado,int almacenSolicitado)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.consultaSolicitudesTraslados(con, estado, almacenSolicitado);
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
        return SqlBaseDespachoTrasladoAlmacenDao.consultaDetalleSolicitud(con, numeroSolicitud);
    }
    /**
     * metodo para insertar la información del 
     * despacho
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public  boolean insertarDespacho(Connection con,HashMap vo)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.insertarDespacho(con, vo);
    }
    /**
     * metodo para insertar el detalle del 
     * despacho
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public boolean insertarDetalleDespacho(Connection con,HashMap vo)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.insertarDetalleDespacho(con, vo);
    }
    /**
     * metodo para actualizar el detalle de un
     * despacho
     * @param con Connection
     * @param vo Hashmap
     * @return boolean
     */
    public boolean actualizarDetalleDespacho(Connection con,HashMap vo)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.actualizarDetalleDespacho(con, vo);
    }
    /**
     * metodo para consultar la cantidad
     * @param con Connection
     * @param vo HashMap
     * @return boolean
     */
    public HashMap consultarCantidadRegistroDetalleDespacho(Connection con,HashMap vo)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.consultarCantidadRegistroDetalleDespacho(con, vo);
    }
    /**
     * metodo para verificar si un registro
     * se encuentra insertado en la BD
     * @param con Connection
     * @param numeroSolicitud int 
     * @return boolean 
     */
    public boolean existeRegistroDespachoBD(Connection con,int numeroSolicitud)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.existeRegistroDespachoBD(con, numeroSolicitud);
    }
    /**
     * metodo para insertar el usuario que realiza
     * el despacho
     * @param con Connection
     * @param usuario String
     * @param numeroSolicitud int
     * @return boolean
     */
    public boolean actualizarUsuarioDespacho(Connection con,String usuario,int numeroSolicitud)
    {
        return SqlBaseDespachoTrasladoAlmacenDao.actualizarUsuarioDespacho(con, usuario, numeroSolicitud);
    }
}
