/*
 * 
 * Septiembre 01 / 2005
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TipoSalasDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Tipos Salas
 */
public class TipoSalas {
	
	/**
	 * DAO para el manejo de los Tipos de Salas
	 */
	private TipoSalasDao tipoSalasDao=null;
	
	///***********ATRIBUTOS**********************************************
	/**
	 * Variable para almacenar el codigo Axioma del registro
	 */
	private int codigo;
	/**
	 * Variable para almacenar la descripción del tipo de sala
	 */
	private String nombre;
	
	/**
	 * Indica si el tipo de sala es quirurgico o no
	 */
	private boolean quirurgica;
	
	/**
	 * Indica si el tipo de sala es urgencias o no
	 */
	private boolean urgencias;
	
	
	//**********CONSTRUCTOR & MÉTODOS DE INICIALIZACIÓN*************************
	/**
	 * Constructor
	 */
	public TipoSalas() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigo=0;
		this.nombre="";
		this.quirurgica = false;
		this.urgencias = false;
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (tipoSalasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tipoSalasDao = myFactory.getTipoSalasDao();
		}	
	}
	//*****************MÉTODOS***************************************
	
	/**
	 * Método usado para cargar todos los tipos de salas existentes
	 * según la institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposSalas(Connection con,int institucion)
	{
		return tipoSalasDao.cargarTiposSalas(con,institucion);
	}
	
	/**
	 * Método usado para ingresar al sistema un nuevo tipo de sala
	 * @param con
	 * @param institucion
	 * @return
	 */
	public int insertarTipoSala(Connection con,int institucion)
	{
		return tipoSalasDao.insertarTipoSala(con,this.nombre,this.quirurgica,this.urgencias,institucion);
	}
	
	public HashMap consultaTipoSalasGruposServicios(Connection con, int codigo)
	{
		return tipoSalasDao.consultaTipoSalasGruposServicios(con, codigo);
	}
	
	
	/**
	 * Método usado para actualizar los datos de un registro
	 * en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @return
	 */
	public int actualizarTipoSala(Connection con)
	{
		return tipoSalasDao.actualizarTipoSala(con,this.codigo,this.nombre,this.quirurgica,this.urgencias);
	}
	
	/**
	 * Método usado para eliminar un registro en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoSala(Connection con)
	{
		return tipoSalasDao.eliminarTipoSala(con,this.codigo);
	}
	
	/**
	 * Método usado para cargar los datos de un tipo de sala
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoSala(Connection con)
	{
		return tipoSalasDao.cargarTipoSala(con,this.codigo);
	}
	
	
	//*************GETTERS & SETTERS ******************************************
	
	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return Returns the quirurgica.
	 */
	public boolean isQuirurgica() {
		return quirurgica;
	}
	/**
	 * @param quirurgica The quirurgica to set.
	 */
	public void setQuirurgica(boolean quirurgica) {
		this.quirurgica = quirurgica;
	}
	/**
	 * @return Returns the urgencias.
	 */
	public boolean isUrgencias()
	{
		return urgencias;
	}
	/**
	 * @param urgencias The urgencias to set.
	 */
	public void setUrgencias(boolean urgencias)
	{
		this.urgencias=urgencias;
	}
	
}
