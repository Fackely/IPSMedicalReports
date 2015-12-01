/*
 * Ago 08, 2006
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ProgramasActividadesConvenioDao;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Programas y Actividades por Convenio 
 */
public class ProgramasActividadesConvenio 
{
	/**
	 * DAO para el manejo de ProgramasActividadesConvenioDao
	 */
	ProgramasActividadesConvenioDao programasDao = null;
	
	//**********ATRIBUTOS*****************************************
	/**
	 *Objeto usado para pasar parámetros al dao
	 */
	HashMap campos = new HashMap();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ProgramasActividadesConvenio() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (programasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			programasDao = myFactory.getProgramasActividadesConvenioDao();
		}	
	}
	//****************************************************************************
	//*******************MÉTODOS*************************************************************
	/**
	 * Método implementado para consultar los programas y actividades por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con)
	{
		return programasDao.consultar(con,campos);
	}
	
	/**
	 * Método implementado para insertar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con)
	{
		return programasDao.insertar(con,campos);
	}
	
	/**
	 * Método implementado para modificar un programa y actividad por convenio
	 * 
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return programasDao.modificar(con,campos);
	}
	
	/**
	 * Método implementado para eliminar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return programasDao.eliminar(con,campos);
	}
	
	/**
	 * Método implementado para cargar  las actividades de un programa
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesPrograma(Connection con)
	{
		return programasDao.cargarActividadesPrograma(con,campos);
	}
	//*****************************************************************************************
	
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
	public void setCampos(String key,Object obj) {
		this.campos.put(key,obj);
	}

}
