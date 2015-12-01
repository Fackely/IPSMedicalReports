/*
 * @(#)ConsultaFacturas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.pooles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.ConsultaProfesionalPoolDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Consulta Profesional po Pool
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 17 /Mar/ 2006
 */
public class ConsultaProfesionalPool
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaProfesionalPool.class);
	
	 /**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public ConsultaProfesionalPool()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultaProfesionalPoolDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaProfesionalPoolDao consultaProfesionalPoolDao ;

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			consultaProfesionalPoolDao = myFactory.getConsultaProfesionalPoolDao();
			wasInited = (consultaProfesionalPoolDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Metodo para consultar los datos de los pooles de un medico dado 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public HashMap consultaProfesionalPool(Connection con,int codigoMedico) throws SQLException
    {
		return consultaProfesionalPoolDao.consultaProfesionalPool(con, codigoMedico);
    }
}