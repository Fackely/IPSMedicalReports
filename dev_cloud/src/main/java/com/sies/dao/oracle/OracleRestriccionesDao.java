/*
 * Created on 24/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.UtilidadBD;

import com.sies.dao.RestriccionesDao;

import com.princetonsa.dao.sqlbase.SqlBaseTagDao;
import com.sies.dao.sqlbase.SqlBaseRestriccionesDao;

/**
 * @author karenth
 *
 */
public class OracleRestriccionesDao implements RestriccionesDao
{
    private Logger logger=Logger.getLogger(OracleRestriccionesDao.class);
    
    /**
     * Implementacion en Oracle  de la consulta de Restricciones 
     */
    public Collection consultarRestricciones(Connection con, int tipo)
	{
        return SqlBaseRestriccionesDao.consultarRestricciones(con, tipo);
	}
    
    /*****************Asignar restricciones a Enfermera*****************/
	
	/**
	 * M�todo que permite la consulta de las restricciones que se encuentran asociadas una 
	 * determinada Enfermera
	 */
	public Collection consultarRestriccionEnfermera(Connection con, int codigoMedico)
	{
		return SqlBaseRestriccionesDao.consultarRestriccionEnfermera(con, codigoMedico);
	}
	
	/**
	 * M�todo que actualiza la fecha de finalizaci�n de la asociaci�n de una restriccion dada,
	 * a una enfermera dada 
	 */
	public int actualizarRestriccionEnfermera(Connection con, int codigoRestriccion, int codigoMedico, boolean esRestriccion)
	{
	    return SqlBaseRestriccionesDao.actualizarRestriccionEnfermera(con, codigoRestriccion, codigoMedico, esRestriccion);
	}
	
	
	/**
	 * M�todo que inserta los registros en la tabla de enfermera_restriccion o enfermera_t_restriccion dependiendo el caso
	 */
	public int insertarRestriccionPersona(Connection con, int codigoRestriccion, int codigoMedico, String valor, boolean esRestriccion, boolean todosDias)
	{
	    return SqlBaseRestriccionesDao.insertarRestriccionPersona(con,codigoRestriccion, codigoMedico, valor, esRestriccion, todosDias);
	}

	/**
	 * M�todo para consultar las enfermeras que se encuentran activas en el sistema
	 * @param con
	 * @param restriccion
	 * @param codigoInstitucion
	 * @return@throws SQLException
	 */
	public Collection consultarPersonas(Connection con, String restriccion, String codigoInstitucion)
	{
	    try
	    {
            return UtilidadBD.resultSet2Collection(SqlBaseTagDao.consultaTagMuestraEnfermerasActivas(con, restriccion, codigoInstitucion));
        }
	    catch (SQLException e)
	    {
        	logger.error("Error consultando las enfermeras "+e);
            return null;
        }
	}
   
    

}
