/*
 * Created on May 27, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo;

import java.sql.Connection;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.JustificacionDinamicaDao;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JustificacionDinamica {
	
	private static Logger logger = Logger.getLogger(JustificacionDinamica.class);
	
	/**
	 * variable que almacena el numero de la solcitiud
	 */
	private int numeroSolicitud;
	
	/**
	 * Variable que almacena el codigo del servicio o articulo
	 */
	private int parametro;
	
	/**
	 * Variable que almacena el código del atributo de la justificacion
	 */
	private int atributo;
	
	/**
	 * Motivo de la justificacion
	 */
	private String descripcion;
	
	/**
	 * Variable que indica si la solciitud es de medicamentos o de servicios
	 */
	private boolean esArticulo;
	
	/**
	 * Variable para manejar el Dao de justificacionDinámica
	 */
	private JustificacionDinamicaDao justificacionDinamicaDao=null;
	
	/**
	 * Constructor
	 *
	 */
	public JustificacionDinamica(){
		this.init();
		this.clean();
	}
	
	
	/** Inicializa el acceso a bases de datos de este objeto */
	public void init()
	{
		/* Obtener el DAO que encapsula las operaciones de BD de este objeto */
		if(justificacionDinamicaDao == null)
			justificacionDinamicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getJustificacionDinamicaDao();
	}
	
	/**
	 * Método para inicializar datos
	 *
	 */
	public void clean()
	{
		this.numeroSolicitud=0;
		this.parametro=0;
		this.atributo=0;
		this.descripcion="";
		this.esArticulo=false;
	}
	
	
	/**
	 * Método para insertar un atributo de una justificacion de servicio o medicamento.
	 * Se debe setear el numeroSolicitud, atributo y parametro(codigo servicio o articulo)
	 * , descripcion y esArticulo(true=> articulo , false=> servicio)
	 * @param con
	 * @return
	 */
	public int insertarAtributoJustificacion(Connection con)
	{
		return justificacionDinamicaDao.insertarAtributoJustificacion(con,numeroSolicitud,parametro,atributo,descripcion,esArticulo);
	}
	
	/**
	 * Metodo para modificar el atributo de la justificacion de una solicitud de servicios o medicamento
	 * Se debe setear el numeroSolicitud, atributo y parametro(codigo servicio o articulo)
	 * , descripcion y esArticulo(true=> articulo , false=> servicio)
	 * @param con
	 * @return
	 */
	public int modificarAtributoJustificacion(Connection con)
	{
	    return justificacionDinamicaDao.modificarAtributoJustificacion(con,numeroSolicitud,parametro,atributo,descripcion,esArticulo);		
	}
	
	

	/**
	 * @return Returns the atributo.
	 */
	public int getAtributo() {
		return atributo;
	}
	
	/**
	 * @param atributo The atributo to set.
	 */
	public void setAtributo(int atributo) {
		this.atributo = atributo;
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
	 * @return Returns the esArticulo.
	 */
	public boolean isEsArticulo() {
		return esArticulo;
	}
	/**
	 * @param esArticulo The esArticulo to set.
	 */
	public void setEsArticulo(boolean esArticulo) {
		this.esArticulo = esArticulo;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the parametro.
	 */
	public int getParametro() {
		return parametro;
	}
	/**
	 * @param parametro The parametro to set.
	 */
	public void setParametro(int parametro) {
		this.parametro = parametro;
	}
}
