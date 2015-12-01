package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.CierreIngresoDao;

/**
 * Mauricio Jaramillo
 * @author axioma
 * Fecha Mayo de 2008
 */

public class CierreIngreso
{

	/**
     * Constructor de la Clase
     */
    public CierreIngreso()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static CierreIngresoDao aplicacionDao;
	
	/**
	 * Método que limpia este objeto
	 */
	public void reset()
	{

	}
	
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
			aplicacionDao = myFactory.getCierreIngresoDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Método para obtener el dao de cierre ingreso de forma estática
	 * @return
	 */
	public static CierreIngresoDao cierreIngresoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierreIngresoDao();
	}

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean cerrarIngreso(Connection con, HashMap vo)
	{
		return aplicacionDao.cerrarIngreso(con, vo);
	}
	
	/**
     * Método para obtener los ingresos cerrados por cierre manual de un paciente
     * pendientes por facturar
     * @param con
     * @param codigoPaciente
     * @return
     */
    public static ArrayList<HashMap<String,Object>> obtenerIngresosCerradosPendientesXPaciente(Connection con,int codigoPaciente)
    {
    	HashMap campos = new HashMap();
    	campos.put("codigoPaciente", codigoPaciente);
    	return cierreIngresoDao().obtenerIngresosCerradosPendientesXPaciente(con, campos);
    }
	
}