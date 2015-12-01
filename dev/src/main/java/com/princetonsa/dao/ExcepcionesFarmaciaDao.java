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
	 * Método que inserta una excepción de farmacia 
     * 
     * @param con Conexión con la fuente de datos
     * @param codigoConvenio Código del convenio
     * @param codigoCentroCosto Código del centro de 
     * costo para el que se aplica esta excepción
     * @param articulo Código del articulo
     * @param noCubre Porcentaje no cubierto
     * @return
     * @throws SQLException
     */
    public int insertarBaseExcepcionFarmacia (Connection con, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException;
    
    /**
	 * Método que modifica una excepción de farmacia 
     * 
     * @param con Conexión con la fuente de datos
     * @param codigo Código de la excepción base
     * @param codigoConvenio Código del convenio
     * @param codigoCentroCosto Código del centro de 
     * costo para el que se aplica esta excepción
     * @param articulo Código del articulo
     * @param noCubre Porcentaje no cubierto
     * @return
     * @throws SQLException
     */
    public int modificarBaseExcepcionFarmacia(Connection con, int codigo, int codigoConvenio, int codigoCentroCosto, int articulo, double noCubre) throws SQLException;
    
    /**
     * Método que elimina una base de 
     * excepción de Farmacia
     * 
     * @param con Conexión con la fuente de datos
     * @param codigo Código de la excepción base
     * a eliminar
     * @return
     * @throws SQLException
     */
    public int eliminarBaseExcepcionFarmacia (Connection con, int codigo) throws SQLException;
    
    /**
     * Método que busca todas las bases de
     * excepción de farmacia
     * 
     * @param con Conexión con la fuente de datos
     * @param codigoConvenioBusqueda Código del convenio
     * @param codigoCentroCostoBusqueda Código del centro
     * de costo
     * @param codigoArticuloBusqueda Código del artículo
     * @param nombreArticuloBusqueda Parte del nombre del 
     * artículo
     * @return
     * @throws SQLException
     */
    public ResultSetDecorator consultarBaseExcepcionFarmacia (Connection con, int codigoConvenioBusqueda, int codigoCentroCostoBusqueda, int codigoArticuloBusqueda, String nombreArticuloBusqueda) throws SQLException;
    
    /**
     * Método que revisa la existencia de un 
     * registro previo en la fuente de datos
     * 
     * @param con Conexión con la fuente de datos
     * @param codigoConvenio Código del convenio
     * @param codigoCentroCosto Código del centro de 
     * costo para el que se aplica esta excepción
     * @param codigoArticulo Código del articulo
     * @param codigo Código del registro a revisar
     * @return
     * @throws SQLException
     */
    public boolean existeBaseExcepcionFarmaciaPrevio (Connection con, int codigoConvenio, int codigoCentroCosto, int codigoArticulo, int codigo) throws SQLException;

}
