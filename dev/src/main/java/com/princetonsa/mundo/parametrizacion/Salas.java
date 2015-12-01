/*
 * Sep 05/2005
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SalasDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Salas
 */
public class Salas {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(Salas.class);
	
	/**
	 * DAO para el manejo de las Salas
	 */
	private SalasDao salasDao=null;
	
	//***************ATRIBUTOS*********************************************
	
	/**
	 * Almacena el consecutivo interno Axioma de la Sala
	 */
	private int consecutivo;
	/**
	 * Almacena el código de la sala parametrizado por el usuario
	 */
	private String codigo;
	
	/**
	 * Almacena el código del tipo de sala 
	 */
	private int tipo;
	
	/**
	 * Almacena la descripción de la sala
	 */
	private String descripcion;
	
	/**
	 * Variable que indica si la sala está activa o desactiva
	 */
	private boolean activo;
	
	/**
	 * variable para almacenar el centro de atencion 
	 */
	private int centroAtencion;
	
	
	//********************* Disponibilidad de la sala *******************************//
	/**
	 * Hora inicial del rango de disponibilidad
	 */
	private String rangoInicial;
	
	/**
	 * Hora finall del rango de disponibilidad
	 */
	private String rangoFinal;

	/**
	 * Código del medico
	 */
	private String medico;
	
	
	
	//**********CONSTRUCTOR & MÉTODOS DE INICIALIZACIÓN*************************
	/**
	 * Constructor
	 */
	public Salas() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.consecutivo=0;
		this.codigo="";
		this.tipo=0;
		this.activo=false;
		this.descripcion="";
		this.rangoInicial ="";
		this.rangoFinal = "";
		this.centroAtencion = 0;
		this.medico="";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (salasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			salasDao = myFactory.getSalasDao();
		}	
	}
	//*****************MÉTODOS***************************************
	/**
	 * Método usado para cargar el listado de salas existentes
	 * por institucion
	 * @param con
	 * @param institucion
	 * @param centroAtencion 
	 * @return
	 */
	public HashMap cargarSalas(Connection con,int institucion, int centroAtencion)
	{
		return salasDao.cargarSalas(con,institucion, centroAtencion);
	}
	
	/**
	 * Método para insertar una nueva sala
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param centroAtencion 
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param insertarSalasStr
	 * @return
	 */
	public int insertarSala(Connection con,int institucion)
	{
		return salasDao.insertarSala(con,this.codigo,institucion,this.tipo,this.activo,this.descripcion, this.centroAtencion, this.medico);
	}
	
	/**
	 * Método usado para actualizar los datos de una sala
	 * @param con
	 * @param codigo
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param consecutivo
	 * @return
	 */
	public String actualizarSala(Connection con)
	{
		return salasDao.actualizarSala(con,this.codigo,this.tipo,this.activo,this.descripcion,this.consecutivo, this.centroAtencion, this.medico);
	}
	
	/**
	 * Método usado para eliminar una sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int eliminarSala(Connection con)
	{
		return salasDao.eliminarSala(con,this.consecutivo);
	}
	
	/**
	 * Método usado para cargar una sala de las tablas existentes
	 * de acuerdo a su consecutivo interno de Axioma
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarSala(Connection con)
	{
		return salasDao.cargarSala(con,this.consecutivo);
	}
	

	
	/**
	 * Método usado para cargar la disponiblidad de la sala
	 * @param con
	 * @param consecutivoSala
	 * @return
	 */
	public HashMap cargarDisponibilidadSala(Connection con, String consecutivoSala)
	{
		return salasDao.cargarDisponibilidadSala(con, consecutivoSala);
	}
	
	/**
	 * Método usado para actualizar un rango de disponibilidad para una sala específica
	 * @param con
	 * @param rangoInicialAnterior
	 * @param rangoFinalAnterior
	 * @return 
	 */
	public int actualizarDisponibilidadSala (Connection con, String rangoInicialAnterior, String rangoFinalAnterior)
	{
		return salasDao.actualizarDisponibilidadSala(con, this.codigo, this.rangoInicial, this.rangoFinal, rangoInicialAnterior, rangoFinalAnterior);
	}
	
	/**
	 * Método para insertar un nuevo rango de disponibilidad de la sala
	 * @param con
	 * @return
	 */
	public String insertarDisponibilidadSala(Connection con)
	{
		return salasDao.insertarDisponibilidadSala(con,this.codigo, this.rangoInicial, this.rangoFinal);
	}
	
	/**
	 * Metodo para eliminar un rango de disponibilidad
	 * de una sala especifica.
	 * @param con
	 */
	public int accionEliminarRango(Connection con)
	{
		return salasDao.accionEliminarRango(con, this.codigo, this.rangoInicial, this.rangoFinal);
	}

	//****************GETTERS & SETTERS*****************************
	/**
	 * @return Returns the activo.
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public int getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the tipo.
	 */
	public int getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Returns the rangoFinal.
	 */
	public String getRangoFinal()
	{
		return rangoFinal;
	}
	/**
	 * @param rangoFinal The rangoFinal to set.
	 */
	public void setRangoFinal(String rangoFinal)
	{
		this.rangoFinal = rangoFinal;
	}
	/**
	 * @return Returns the rangoInicial.
	 */
	public String getRangoInicial()
	{
		return rangoInicial;
	}
	/**
	 * @param rangoInicial The rangoInicial to set.
	 */
	public void setRangoInicial(String rangoInicial)
	{
		this.rangoInicial = rangoInicial;
	}
	public int getCentroAtencion() {
		return centroAtencion;
	}
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the medico
	 */
	public String getMedico() {
		return medico;
	}
	/**
	 * @param medico the medico to set
	 */
	public void setMedico(String medico) {
		this.medico = medico;
	}
	
}
