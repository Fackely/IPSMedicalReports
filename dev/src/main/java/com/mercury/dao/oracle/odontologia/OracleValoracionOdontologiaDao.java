/*
 * Created on 22/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mercury.dao.oracle.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.mercury.dao.odontologia.ValoracionOdontologiaDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseValoracionOdontologiaDao;

/**
 * @author Alejo
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OracleValoracionOdontologiaDao implements ValoracionOdontologiaDao
{

    /* (non-Javadoc)
     * @see com.mercury.dao.odontologia.ValoracionOdontologiaDao#insertar(java.sql.Connection, int, java.lang.String, boolean, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, boolean, java.lang.String)
     */
    public int insertar(Connection con, int numValoracion) throws SQLException
    {
        
        return SqlBaseValoracionOdontologiaDao.insertar(con, numValoracion);
    }

    /* (non-Javadoc)
     * @see com.mercury.dao.odontologia.ValoracionOdontologiaDao#cargar(java.sql.Connection, int)
     */
    public ResultSetDecorator consultar(Connection con, int numValoracion) throws SQLException
    {
        
        return SqlBaseValoracionOdontologiaDao.consultar(con, numValoracion);
    }
}
