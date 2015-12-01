/*
 * Created on 19/06/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.Answer;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author wrios
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CensoCamas1Dao 
{
    /**
	 * Listado de camas (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator listarCamas(Connection con, int codigoInstitucion) throws SQLException;
	
	/**
	 * Metodo que lista las camas por centro de costo e institucion
	 * (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param centroCosto
	 * @param codigoInstitucion
	 * @return
	 */
    public ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto, int codigoInstitucion);
    
    
	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la fuente de datos.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la fuente de datos
	 */
	public Answer cargarCamaCensoCamas (Connection con, String codigoCama, int codigoInstitucion) throws SQLException;
}
