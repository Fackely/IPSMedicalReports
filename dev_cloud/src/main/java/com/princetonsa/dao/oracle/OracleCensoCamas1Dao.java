/*
 * Created on 19/06/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.Answer;
import com.princetonsa.dao.CensoCamas1Dao;
import com.princetonsa.dao.sqlbase.SqlBaseCensoCamas1Dao;

/**
 * @author wrios
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OracleCensoCamas1Dao implements CensoCamas1Dao
{
   
    
    
    /**
	 * Listado de camas (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator listarCamas(Connection con, int codigoInstitucion) throws SQLException
	{
	    return SqlBaseCensoCamas1Dao.listarCamas(con, codigoInstitucion);
	}
	
	/**
	 * Metodo que lista las camas por centro de costo e institucion
	 * (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param centroCosto
	 * @param codigoInstitucion
	 * @return
	 */
    public ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto, int codigoInstitucion)
    {
        return SqlBaseCensoCamas1Dao.listarCamasCentroCosto(con , centroCosto, codigoInstitucion);
    }
    
 
	
	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la base de datos PostgreSQL.
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la base de datos PostgreSQL
	 */
	public Answer cargarCamaCensoCamas (Connection con, String codigoCama, int codigoInstitucion) throws SQLException
	{
		return SqlBaseCensoCamas1Dao.cargarCamaCensoCamas(con, codigoCama, codigoInstitucion);
	}

}
