/*
 * @(#)PostgresqlExcepcionesFarmaciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.UtilidadTexto;

import com.princetonsa.dao.ExcepcionesFarmaciaDao;
import com.princetonsa.dao.sqlbase.SqlBaseExcepcionesFarmacia;

/**
 * Esta clase implementa el contrato estipulado en 
 * <code>ExcepcionesFarmaciaDao</code>, proporcionando 
 * los servicios de acceso a una base de datos PostgreSQL 
 * requeridos por <code>ExcepcionesFarmacia</code>
 *
 * @version 1.0 Nov 30, 2004
 */
public class PostgresqlExcepcionesFarmaciaDao implements ExcepcionesFarmaciaDao
{

	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 * insertar una base de excepción de farmacia
	 */
    private final static String insertarBaseExcepcionFarmaciaStr="INSERT INTO excepciones_farmacia (codigo, convenio, centro_costo, articulo, no_cubre) values (nextval('seq_excepciones_farmacia'), ?, ? , ?, ?)";
    
	/**
	 * Implementación del método que inserta
	 * una excepción de farmacia en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#insertarBaseExcepcionFarmacia (Connection , int , int , int , double ) throws SQLException
	 */
    public int insertarBaseExcepcionFarmacia (Connection con, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException
    {
        return SqlBaseExcepcionesFarmacia.insertarBaseExcepcionFarmacia (con, codigoConvenio, codigoCentroCosto, articulo, noCubre, insertarBaseExcepcionFarmaciaStr) ;
    }
    
	/**
	 * Implementación del método que modifica
	 * una excepción de farmacia en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#modificarBaseExcepcionFarmacia(Connection , int , int , int , int , double ) throws SQLException
	 */
    public int modificarBaseExcepcionFarmacia(Connection con, int codigo, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException
    {
        return SqlBaseExcepcionesFarmacia.modificarBaseExcepcionFarmacia(con, codigo, codigoConvenio, codigoCentroCosto, articulo, noCubre) ;
    }
    
	/**
	 * Implementación del método que elimina
	 * una excepción de farmacia en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#eliminarBaseExcepcionFarmacia (Connection , int ) throws SQLException
	 */
    public int eliminarBaseExcepcionFarmacia (Connection con, int codigo) throws SQLException
    {
        return SqlBaseExcepcionesFarmacia.eliminarBaseExcepcionFarmacia (con, codigo) ;
    }
    
	/**
	 * Implementación del método que busca todas 
	 * las excepciones de farmacia en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#consultarBaseExcepcionFarmacia (Connection , int, int,  int ) throws SQLException
	 */
    public ResultSetDecorator consultarBaseExcepcionFarmacia (Connection con, int codigoConvenioBusqueda, int codigoCentroCostoBusqueda, int codigoArticuloBusqueda, String nombreArticuloBusqueda) throws SQLException
    {
        //La restricción se pone en la implementación 
        //(Postgresql), para dejar el método en el base
        //y para poder aprovechar las posibilidades
        //de expresiones regulares para cada base de datos
        String restriccionPorNombreArticulo="";

        if (nombreArticuloBusqueda!=null&&!nombreArticuloBusqueda.equals(""))
        {
            restriccionPorNombreArticulo=" and UPPER(art.descripcion) ~* UPPER('" + UtilidadTexto.convertToRegexp(nombreArticuloBusqueda) + "')";
        }
        return SqlBaseExcepcionesFarmacia.consultarBaseExcepcionFarmacia (con, codigoConvenioBusqueda, codigoCentroCostoBusqueda, codigoArticuloBusqueda, restriccionPorNombreArticulo) ;
    }
    
	/**
	 * Implementación del método que revisa la existencia 
	 * de un registro previo en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ExcepcionesFarmaciaDao#existeBaseExcepcionFarmaciaPrevio (Connection , int , int , int , int ) throws SQLException
	 */
    public boolean existeBaseExcepcionFarmaciaPrevio (Connection con, int codigoConvenio, int codigoCentroCosto, int codigoArticulo, int codigo) throws SQLException
    {
        return SqlBaseExcepcionesFarmacia.existeBaseExcepcionFarmaciaPrevio (con, codigoConvenio, codigoCentroCosto, codigoArticulo, codigo); 
    }
    
}
