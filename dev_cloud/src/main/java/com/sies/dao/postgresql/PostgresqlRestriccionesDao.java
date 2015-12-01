/*
 * Created on 24/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import util.UtilidadBD;

import com.princetonsa.dao.sqlbase.SqlBaseTagDao;
import com.sies.dao.RestriccionesDao;
import com.sies.dao.sqlbase.SqlBaseRestriccionesDao;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlRestriccionesDao implements RestriccionesDao
{
    /**
     * implementacion en postgresql de la consulta de las restricciones
     */
    public Collection consultarRestricciones(Connection con, int tipo)
	{
        return SqlBaseRestriccionesDao.consultarRestricciones(con, tipo);
	}
        
    
    /**
	 * Metodo que permite la consulta de las restricciones que se encuentran asociadas una 
	 * determinada Enfermera
	 */
	public Collection consultarRestriccionEnfermera(Connection con, int codigoMedico)
	{
		return SqlBaseRestriccionesDao.consultarRestriccionEnfermera(con, codigoMedico);
	}
	
	
	/**
	 * Método que actualiza la fecha de finalización de la asociación de una restriccion dada,
	 * a una enfermera dada 
	 */
	public int actualizarRestriccionEnfermera(Connection con, int codigoRestriccion, int codigoMedico, boolean esRestriccion)
	{
	    return SqlBaseRestriccionesDao.actualizarRestriccionEnfermera(con, codigoRestriccion, codigoMedico, esRestriccion);
	}
	
	/**
	 * Método que inserta los registros en la tabla de enfermera_restriccion o enfermera_t_restriccion dependiendo el caso
	 */
	public int insertarRestriccionPersona(Connection con, int codigoRestriccion, int codigoMedico, String valor, boolean esRestriccion, boolean todosDias)
	{
	    return SqlBaseRestriccionesDao.insertarRestriccionPersona(con,codigoRestriccion, codigoMedico, valor, esRestriccion, todosDias);
	}
	
	
	/**
	 * Método para consultar las personas que se encuentran activas en el sistema
	 * @param con
	 * @param restriccion
	 * @param codigoInstitucion
	 * @return@throws SQLException
	 */
	public Collection consultarPersonas(Connection con, String restriccion, String codigoInstitucion)
	{
	    try {
            return UtilidadBD.resultSet2Collection(SqlBaseTagDao.consultaTagMuestraEnfermerasActivas(con, restriccion, codigoInstitucion));
        } catch (SQLException e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
    
}
