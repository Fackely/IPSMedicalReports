package com.princetonsa.mundo.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.consultaExterna.AsignarCitasControlPostOperatorioForm;
import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dao.consultaExterna.AsignarCitasControlPostOperatorioDao;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */

public class AsignarCitasControlPostOperatorio
{

	/**
     * Constructor de la Clase
     */
    public AsignarCitasControlPostOperatorio()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static AsignarCitasControlPostOperatorioDao aplicacionDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao == null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getAsignarCitasControlPostOperatorioDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que llena el HashMap con los datos de Asignar Citas Reservadas por Control Post-Operatorio
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap consultarCitasReservadas(Connection con, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsignarCitasControlPostOperatorioDao().consultarCitasReservadas(con, codigoPaciente);
	}

	/**
	 * @param con
	 * @param usuario
	 * @param mapaSubcuenta 
	 * @return
	 */
	public int insertarSubCuenta(Connection con, String usuario, HashMap datosSubCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsignarCitasControlPostOperatorioDao().insertarSubCuenta(con, usuario, datosSubCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param b
	 * @return
	 */
	public boolean controlPostOperatorio(Connection con, int idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsignarCitasControlPostOperatorioDao().controlPostOperatorio(con, idIngreso);
	}
	
}