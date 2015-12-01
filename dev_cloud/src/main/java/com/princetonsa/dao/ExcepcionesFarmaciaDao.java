/*
 * @(#)ExcepcionesFarmaciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>ExcepcionesFarmacia</code>.
 *
 * @version 1.0 Nov 29, 2004
 */
public interface ExcepcionesFarmaciaDao 
{
    /**
	 * M�todo que inserta una excepci�n de farmacia 
     * 
     * @param con Conexi�n con la fuente de datos
     * @param codigoConvenio C�digo del convenio
     * @param codigoCentroCosto C�digo del centro de 
     * costo para el que se aplica esta excepci�n
     * @param articulo C�digo del articulo
     * @param noCubre Porcentaje no cubierto
     * @return
     * @throws SQLException
     */
    public int insertarBaseExcepcionFarmacia (Connection con, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException;
    
    /**
	 * M�todo que modifica una excepci�n de farmacia 
     * 
     * @param con Conexi�n con la fuente de datos
     * @param codigo C�digo de la excepci�n base
     * @param codigoConvenio C�digo del convenio
     * @param codigoCentroCosto C�digo del centro de 
     * costo para el que se aplica esta excepci�n
     * @param articulo C�digo del articulo
     * @param noCubre Porcentaje no cubierto
     * @return
     * @throws SQLException
     */
    public int modificarBaseExcepcionFarmacia(Connection con, int codigo, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException;
    
    /**
     * M�todo que elimina una base de 
     * excepci�n de Farmacia
     * 
     * @param con Conexi�n con la fuente de datos
     * @param codigo C�digo de la excepci�n base
     * a eliminar
     * @return
     * @throws SQLException
     */
    public int eliminarBaseExcepcionFarmacia (Connection con, int codigo) throws SQLException;
    
    /**
     * M�todo que busca todas las bases de
     * excepci�n de farmacia
     * 
     * @param con Conexi�n con la fuente de datos
     * @param codigoConvenioBusqueda C�digo del convenio
     * @param codigoCentroCostoBusqueda C�digo del centro
     * de costo
     * @param codigoArticuloBusqueda C�digo del art�culo
     * @param nombreArticuloBusqueda Parte del nombre del 
     * art�culo
     * @return
     * @throws SQLException
     */
    public ResultSetDecorator consultarBaseExcepcionFarmacia (Connection con, int codigoConvenioBusqueda, int codigoCentroCostoBusqueda, int codigoArticuloBusqueda, String nombreArticuloBusqueda) throws SQLException;
    
    /**
     * M�todo que revisa la existencia de un 
     * registro previo en la fuente de datos
     * 
     * @param con Conexi�n con la fuente de datos
     * @param codigoConvenio C�digo del convenio
     * @param codigoCentroCosto C�digo del centro de 
     * costo para el que se aplica esta excepci�n
     * @param codigoArticulo C�digo del articulo
     * @param codigo C�digo del registro a revisar
     * @return
     * @throws SQLException
     */
    public boolean existeBaseExcepcionFarmaciaPrevio (Connection con, int codigoConvenio, int codigoCentroCosto, int codigoArticulo, int codigo) throws SQLException;

}
