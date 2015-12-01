/*
 * @(#)ConsultaGruposEtareos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.ConsultaGruposEtareosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * la consulta de Grupos Etareos
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 26 /May/ 2006
 */
public class ConsultaGruposEtareos
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaGruposEtareos.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaGruposEtareos()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>ConsultaGruposEtareos</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaGruposEtareosDao consultaGruposEtareosDao ;

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
			consultaGruposEtareosDao = myFactory.getConsultaGruposEtareosDao();
			wasInited = (consultaGruposEtareosDao != null);
		}

		return wasInited;
	}
	
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun unos parametros de busqueda
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposEtareos (Connection con, String fechaIncial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException
	{
		return consultaGruposEtareosDao.consultarGruposEtareos(con, fechaIncial, fechaFinal, codigoConvenio, institucion);
	}
}