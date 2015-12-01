/* Princeton S.A (Parquesoft-Manizales)
*  Andr�s Mauricio Ruiz V�lez
*  Creado 14-nov-2006 11:12:57
*/


package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.CrecimientoDesarrolloDao;

public class CrecimientoDesarrollo 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CrecimientoDesarrollo.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CrecimientoDesarrolloDao  crecimientoDesarrolloDao = null;
	
//	----------------------------------ATRIBUTOS DE LA CLASE --------------------------------------//
	/**
	 * Mapa que contiene la informaci�n de los ex�menes f�sicos de todas
	 * las valoraciones para la realizaci�n de la curva de crecimiento y desarrollo
	 */
	private HashMap mapaCrecimientoDesarrollo;
	
	/**
	 * Constructor de la clase, inicializa en vac�o todos los atributos
	 */
	public CrecimientoDesarrollo ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este m�todo inicializa los atributos de la clase con valores vac�os
	 */
	public void reset()
	{
		this.mapaCrecimientoDesarrollo = new HashMap();
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			crecimientoDesarrolloDao = myFactory.getCrecimientoDesarrolloDao();
			wasInited = (crecimientoDesarrolloDao != null);
		}
		return wasInited;
	}

	/**
	 * Metodo estandar para consultar la informacion del paciente.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap parametros) 
	{
		return crecimientoDesarrolloDao.consultarInformacion(con, parametros);
	}


	
//	------------------------------------- SETS Y GETS ---------------------------------------------------//
	
	/**
	 * 
	 * @return mapaCrecimientoDesarrollo
	 */
	public HashMap getMapaCrecimientoDesarrollo() {
		return mapaCrecimientoDesarrollo;
	}

	/**
	 * Setea mapaCrecimientoDesarrollo
	 * @param mapaCrecimientoDesarrollo
	 */
	public void setMapaCrecimientoDesarrollo(HashMap mapaCrecimientoDesarrollo) {
		this.mapaCrecimientoDesarrollo = mapaCrecimientoDesarrollo;
	}

	
}
