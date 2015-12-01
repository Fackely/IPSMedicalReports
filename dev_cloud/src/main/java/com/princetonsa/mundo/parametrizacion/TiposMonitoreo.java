/*
 * Created on Aug 30, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TiposMonitoreoDao;

/**
 * @author Sebastián Gómez
 *
 * Objeto que representa la parametrización de los tipos de monitoreo
 */
public class TiposMonitoreo {
	/**
	 * DAO para el manejo de los Tipos de Monitoreo
	 */
	private TiposMonitoreoDao tiposMonitoreoDao=null;
	
	//***********ATRIBUTOS**********************************************
	/**
	 * Variable para almacenar el codigo Axioma del registro
	 */
	private int codigo;
	/**
	 * Variable para almacenar la descripción del tipo monitoreo
	 */
	private String nombre;
	/**
	 * Variable para almacenar la prioridad de cobro
	 */
	private int prioridad;
	/**
	 * Variable para almacenar el servicio
	 */
	private int servicio;
	
	/**
	 * Variable para el centro de costo
	 */
	private int centroCosto;
	
	/**
	 * Variable para El Check Requiere Valoracion
	 */
	private String check;
	
	//**********CONSTRUCTOR & MÉTODOS DE INICIALIZACIÓN*************************
	/**
	 * Constructor
	 */
	public TiposMonitoreo() {
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
		this.prioridad=0;
		this.servicio=0;
		this.centroCosto=0;
		this.check="";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (tiposMonitoreoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tiposMonitoreoDao = myFactory.getTiposMonitoreoDao();
		}	
	}
	//******************MÉTODOS*******************************
	/**
	 * Método usado para cargar los tipos de monitoreo por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposMonitoreo(Connection con,int institucion)
	{
		return tiposMonitoreoDao.cargarTiposMonitoreo(con,institucion);
	}
	/**
	 * Método usado para la actualización de los datos de un registros 
	 * en tipos monitoreos
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @return
	 */
	public int actualizarTiposMonitoreo(Connection con)
	{
		return tiposMonitoreoDao.actualizarTipoMonitoreo(con,this.codigo,this.nombre,this.prioridad,this.servicio,this.centroCosto,this.check);
	}
	/**
	 * Método para insertar un nuevo tipo de monitoreo
	 * @param con
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @param institucion
	 * @param consulta
	 * @return
	 */
	public int insertarTipoMonitoreo(Connection con,int institucion)
	{
		return tiposMonitoreoDao.insertarTipoMonitoreo(con,this.nombre,this.prioridad,this.servicio,this.check,institucion);
	}
	
	/**
	 * Metodo para isertar los centros de Costo por cada tipo de Monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public boolean insertarCentros(Connection con, int tipo, int centro)
	{
		return tiposMonitoreoDao.insertarCentros(con, tipo, centro);
	}
	
	/**
	 * Método para eliminar un registro de la tabla tipo_monitoreo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoMonitoreo(Connection con)
	{
		return tiposMonitoreoDao.eliminarTipoMonitoreo(con,this.codigo);
	}
	
	/**
	 * Metodo para eliminar un centros de costo por tipo de monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public int eliminarCentro(Connection con, int tipo, int centro)
	{
		return tiposMonitoreoDao.eliminarCentro(con, tipo, centro);
	}
	
	/**
	 * Método usado para cargar un tipo de monitoreo por su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoMonitoreo(Connection con)
	{
		return tiposMonitoreoDao.cargarTipoMonitoreo(con,this.codigo);
	}
	
	/**
	 * Método usado para revisar si el tipo de monitoreo se está utilizando
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean revisarUsoTipoMonitoreo(Connection con)
	{
		return tiposMonitoreoDao.revisarUsoTipoMonitoreo(con,this.codigo);
	}
	
	/**
	 * Metodo que consulta los centros por cada tipo de Monitoreo
	 * @param con
	 * @return
	 */
	public HashMap cargarCentrosPorTipo(Connection con)
	{
		return tiposMonitoreoDao.cargarCentrosPorTipo(con);
	}
	
	//***********GETTERS & SETTERS****************************+
	
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
	 * @return Returns the prioridad.
	 */
	public int getPrioridad() {
		return prioridad;
	}
	/**
	 * @param prioridad The prioridad to set.
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
	/**
	 * @return Returns the servicio.
	 */
	public int getServicio() {
		return servicio;
	}
	/**
	 * @param servicio The servicio to set.
	 */
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}
	public int getCentroCosto() {
		return centroCosto;
	}
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
}