package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author 
 * @since 
 */
public class ConsultaAutorizacionesCapitacionSubPorPacienteForm extends
		ActionForm {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
	private String integracion="";
	
	/**
	 * Atributo usado para indicar cuando no se tienen autorizaciones de capitación
	 */
	private boolean sinAutorizaciones;
	
	
	/**
	 * Atributo que contiene las autorizaciones de capitación existentes
	 */
	private ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones;
	
	/**
	 * Atributo que almacena el código de la autorización seleccionada para ver su detalle
	 */
	private long codigoAutorizacion;
	
	
	/**
	 * atributo usado para la paginación del listado de autorizaciones.
	 */
	private int posArray;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	
	
	/**
	 * Atributo que contiene los servicios de la autorización
	 */	
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	/**
	 * Atributo que contiene los artículos de la autorización
	 */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	
	private int numeroSolicitud;
	
	private boolean esServicio;
	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de administración de las autorizaciones de capitación por paciente
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		this.estado="";
		this.sinAutorizaciones=true;
		this.listaAutorizaciones=new ArrayList<DTOAdministracionAutorizacion>();
		this.codigoAutorizacion=ConstantesBD.codigoNuncaValidoLong;
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author, 
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=null;
		errores=new ActionErrors();
		
		
		return errores;
		
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sinAutorizaciones
	
	 * @return retorna la variable sinAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isSinAutorizaciones() {
		return sinAutorizaciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sinAutorizaciones
	
	 * @param valor para el atributo sinAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setSinAutorizaciones(boolean sinAutorizaciones) {
		this.sinAutorizaciones = sinAutorizaciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAutorizaciones
	
	 * @return retorna la variable listaAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOAdministracionAutorizacion> getListaAutorizaciones() {
		return listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAutorizaciones
	
	 * @param valor para el atributo listaAutorizaciones 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAutorizaciones(
			ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones) {
		this.listaAutorizaciones = listaAutorizaciones;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAutorizacion
	
	 * @return retorna la variable codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoAutorizacion() {
		return codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAutorizacion
	
	 * @param valor para el atributo codigoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoAutorizacion(long codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Angela Maria Aguirre 
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Angela Maria Aguirre 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	public void setListaServicios(ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}


	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}


	public void setListaArticulos(ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}


	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}


	public boolean isEsServicio() {
		return esServicio;
	}


	public String getIntegracion() {
		return integracion;
	}


	public void setIntegracion(String integracion) {
		this.integracion = integracion;
	}
	

}
