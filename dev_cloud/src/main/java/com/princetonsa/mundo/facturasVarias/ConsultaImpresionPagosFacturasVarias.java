package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConsultaImpresionPagosFacturasVariasDao;

/**
 * Fecha: Abril de 2008
 * @author axioma
 */

public class ConsultaImpresionPagosFacturasVarias 
{

	/**
     * Constructor de la Clase
     */
    public ConsultaImpresionPagosFacturasVarias()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ConsultaImpresionPagosFacturasVariasDao aplicacionDao;
	
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
	    if ( aplicacionDao== null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getConsultaImpresionPagosFacturasVariasDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap buscarPagosFacturasVarias(Connection con,  int codigoInstitucionInt, HashMap criterios)
	{
		return aplicacionDao.buscarPagosFacturasVarias(con, codigoInstitucionInt, criterios);
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public HashMap buscarConceptos(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		return aplicacionDao.buscarConceptos(con, codigoInstitucionInt, codigoAplicacion);
	}

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public HashMap buscarFacturas(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		return aplicacionDao.buscarFacturas(con, codigoInstitucionInt, codigoAplicacion);
	}
	
}