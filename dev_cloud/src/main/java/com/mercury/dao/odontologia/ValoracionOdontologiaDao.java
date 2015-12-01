/*
 * Created on 22/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mercury.dao.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

/**
 * @author Alejo
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ValoracionOdontologiaDao
{
    public int insertar(Connection con, int numValoracion) throws SQLException;
	        
	public ResultSetDecorator consultar(Connection con, int numValoracion) throws SQLException;
}
