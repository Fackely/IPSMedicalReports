/*
 * Created on 25/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mercury.mundo.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.mercury.dao.odontologia.ValoracionOdontologiaDao;

/**
 * @author Alejo
 *
 */
public class ValoracionOdontologia
{
	private int numeroValoracion;
	
	private static ValoracionOdontologiaDao valoracionesodontologia;
	
	private static ValoracionOdontologiaDao getValoracionOdontologiaDao()
	{
		if(valoracionesodontologia==null)
		{
			String tipoBD = System.getProperty("TIPOBD" );
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			valoracionesodontologia=myFactory.getValoracionOdontologiaDao();
		}
		return valoracionesodontologia;
	}
	
	public ValoracionOdontologia()
	{
	    clean();
	}
	
	public void clean()
	{
        this.numeroValoracion=-1;
	}
	
	public int getNumeroValoracion()
	{
	    return this.numeroValoracion;
	}
    
    public void setNumeroValoracion(int numeroValoracion)
    {
        this.numeroValoracion = numeroValoracion;
    }
    
	public ResultadoBoolean insertar(Connection con)
	{
	    try
	    {
	        ValoracionOdontologia.getValoracionOdontologiaDao().insertar(
                    con, 
                    this.getNumeroValoracion());
                
	        return new ResultadoBoolean(true, "Se adiciono exitosamente la valoracion odontologia para el num. sol. "+this.getNumeroValoracion());
	    }
	    catch(SQLException e)
	    {	        
	        return new ResultadoBoolean(false, "Error insertando la valoración de odontologia para el num. sol. "+this.getNumeroValoracion());
	    }
	}
	
	public ResultadoBoolean consultar(Connection con, int numeroValoracion)
	{
		try
		{
			ResultSetDecorator rs=ValoracionOdontologia.getValoracionOdontologiaDao().consultar(con, numeroValoracion);
			if(rs.next())
			{
			    this.setNumeroValoracion(numeroValoracion);
                
				return new ResultadoBoolean(true, "Se cargó exitosamente la valoracion de odontologia");
			}
			else
			{
			    return new ResultadoBoolean(false, "No existe valoración odontología para el núm. de sol " + numeroValoracion + " consultarValoracionOdontologia ");
			}
		}
		catch (SQLException e)
		{
		    return new ResultadoBoolean(false, "Se presentó error cargando valoración odontologia para el núm. de sol " + numeroValoracion + " consultarValoracionOdontologia " + e.toString());
		}
	}
}
