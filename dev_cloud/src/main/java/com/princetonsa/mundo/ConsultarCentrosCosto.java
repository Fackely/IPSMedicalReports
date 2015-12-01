/*
 * @(#)ConsultarCentrosCosto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.ConsultarCentrosCostoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Consulta de los Centros de Costo
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 11 /May/ 2006
 */
public class ConsultarCentrosCosto
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultarCentrosCosto.class);
	
	/**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultarCentrosCosto()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultarCentrosCostoDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultarCentrosCostoDao consultarCentrosCostoDao ;

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
			consultarCentrosCostoDao = myFactory.getConsultarCentrosCostoDao();
			wasInited = (consultarCentrosCostoDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para realizar la busqueda avanzada de los centros de costo segun los
	 * parametros ingresados
	 * @param con
	 * @param codCentroAtencion
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param acronimoUnidadFuncional
	 * @param activo
	 * @param tipoEntidad 
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCosto(Connection con, int codCentroAtencion, String identificador, String descripcion, int codigoTipoArea, String manejoCamas, String acronimoUnidadFuncional, String codigo_interfaz, String activo, String tipoEntidad) throws SQLException
	{
		return consultarCentrosCostoDao.consultarCentrosCosto(con, codCentroAtencion, identificador, descripcion, codigoTipoArea, manejoCamas, acronimoUnidadFuncional, codigo_interfaz, activo,tipoEntidad);
	}
}
	