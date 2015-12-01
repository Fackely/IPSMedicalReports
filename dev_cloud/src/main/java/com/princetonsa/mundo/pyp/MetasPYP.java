/*
 * Ago 09, 2006
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.MetasPYPDao;

/**
 * @author Sebasti�n G�mez 
 *
 *Clase que representa el Mundo con sus atributos y m�todos de la funcionalidad
 * Parametrizaci�n de Metas PYP 
 */
public class MetasPYP {
	/**
	 * DAO para el manejo de MetasPYPDao
	 */
	MetasPYPDao metasDao = null;
	
	//**********ATRIBUTOS*****************************************
	/**
	 *Objeto usado para pasar par�metros al dao
	 */
	HashMap campos = new HashMap();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public MetasPYP() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * m�todo para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (metasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			metasDao = myFactory.getMetasPYPDao();
		}	
	}
	//****************************************************************************
	//***************METODOS*******************************************************
	/**
	 * M�todo implementado para consultar las actividades del programa, convenio, y a�o espec�ficos
	 * @param con
	 * @return
	 */
	public HashMap consultarActividades(Connection con)
	{
		return metasDao.consultarActividades(con,campos);
	}
	
	/**
	 * M�todo implementado para consultar los centros de atencion de una actividad, programa, convenio y a�o espec�ficos
	 * @param con
	 * @return
	 */
	public HashMap consultarCentrosAtencion(Connection con)
	{
		return metasDao.consultarCentrosAtencion(con,campos);
	}
	
	/**
	 * M�todo que consulta las ocupaciones del centro atencion, actividad, programa, convenio y a�o espec�fico
	 * @param con
	 * @return
	 */
	public HashMap consultarOcupaciones(Connection con)
	{
		return metasDao.consultarOcupaciones(con,campos);
	}
	
	/**
	 * M�todo implementado para insertar una actividad de un programa, convenio y a�o espec�fico
	 * @param con
	 * @return
	 */
	public int insertarActividades(Connection con)
	{
		return metasDao.insertarActividades(con,campos);
	}
	
	/**
	 * M�todo que insertar un centro de atencion para una actividad, programa, convenio y anio espec�fico
	 * @param con
	 * @return
	 */
	public int insertarCentrosAtencion(Connection con)
	{
		return metasDao.insertarCentrosAtencion(con,campos);
	}
	
	/**
	 * M�todo implementado para insertar un ocupacion de una actividad, programa, convenio, a�o
	 * espec�ficos
	 * @param con
	 * @return
	 */
	public int insertarOcupaciones(Connection con)
	{
		return metasDao.insertarOcupaciones(con,campos);
	}
	
	/**
	 * M�todo implementado para modificar una actividad, centro atencion o ocupacion m�dica
	 * pertenecientes a un programa, convenio y a�o espec�ficos.
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valor pueden ser : actividad, centroAtencion y ocupacionMedica
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return metasDao.modificar(con,campos);
	}
	
	/**
	 * M�todo que elimina un centro de atencion o una ocupacion pertenecientes a
	 * una actividad, programa , convenio y a�o espec�ficos
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valores pueden ser : centroAtencion o ocupacionMedica
	 * @param con
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return metasDao.eliminar(con,campos);
	}
	
	/**
	 * M�todo que consulta las actividades de un programa por convenio
	 * @param con
	 * @return
	 */
	public HashMap cargarActividadesProgramaConvenio(Connection con)
	{
		return metasDao.cargarActividadesProgramaConvenio(con,campos);
	}
	//******************************************************************************
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	/**
	 * @return Retorna en elemento del mapa campos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa campos.
	 */
	public void setCampos(String key, Object obj) {
		this.campos.put(key,obj);
	}
}
