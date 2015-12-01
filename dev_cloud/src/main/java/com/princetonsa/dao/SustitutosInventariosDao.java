/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>SustitutosInventario</code>.
 * 
 * @author armando
 *
 * Princeton 31-ago-2004
 */
public interface SustitutosInventariosDao 
{
    /**
     * Metodo que inserta un registro en la tabla Sustitutos Inventario
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public int insertarSustitutoInventario(Connection con,int codigoPrincipal,int codigoSustituto);
    /**
     * Metodo que modifica un registro en la tabla Sustitutos Inventario,
     * solo se pueden modificar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal a modificar.
     * @param codigoSustitutoOld,<b>int</b>, Codigo del antiguo articulo Sustituto.
     * @param codigoSustituto,<b>int</b>, Codigo del nuevo articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public int modificarSustitutoInventario(Connection con,int codigoPrincipal,int codigoSustituto,int codigoSustitutoOld);
    /**
     * Metodo que elimina un registro en la tabla Sustitutos Inventario,
     * solo se pueden eliminar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto que sera eliminado.
     * @return -1 si se produjo un error.
     */
    public int eliminarSustitutoInventario(Connection con,int codigoPrincipal,int codigoSustituto);
    /**
     * Metodo que realiza la consulta de un registro en la tabla Sustitutos Inventario,
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @return Collection con los datos
     */
    public Collection consultarSustitutoInventario(Connection con,int codigoPrincipal);
    
    /**
     * Metodo que realiza la consulta de un registro especifico.
     * @param con, Conexion
     * @param codigoPrincipal, Codigo Principal
     * @param codigoSustituto, Codigo Sustituto
     * @return ResultSet, resultado de la consulta
     */
    public ResultSetDecorator consultarSustitutoInventarioEspecifico(Connection con, int codigoPrincipal, int codigoSustituto);

}
