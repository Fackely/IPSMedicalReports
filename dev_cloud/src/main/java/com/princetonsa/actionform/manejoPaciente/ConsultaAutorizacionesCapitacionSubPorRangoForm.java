package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

import util.ConstantesBD;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author 
 * @since 
 */
public class ConsultaAutorizacionesCapitacionSubPorRangoForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
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
	 *Objeto que contiene los filtros de la consulta 
	 */
	private DTOBusquedaAutorizacionCapitacionRango dtoFiltros;
	
	
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSub;
	
	/**
	 * Lista los estados de las autorizaciones
	 */
	private ArrayList<DtoAutorizacionEntSubContratada> listaEstadosAutorizaciones;

	/**
	 * atributo de busqueda por rango de las auotrizaciones
	 */
	private String estadoAutorizacion;
	
	/**
	 * 
	 * lista para los estados en la seleccion del jsp
	 */
	private ArrayList<DtoCheckBox> listaOpciones;
	
	/**
	 * 
	 * lista para los tipos de Autorizacion en la seleccion del jsp
	 */
	private ArrayList<DtoCheckBox> listaTiposAutorizacion;
	
	/**
	 * 
	 * lista para los tipos de consecutivos de Autorizacion en la seleccion del jsp
	 */
	private ArrayList<DtoCheckBox> listaTiposConsecutivoAutorizacion;
	
	/**
	 * Almacena el consecutivo digitado inicial
	 */
	private Long consInAutorizacion;
	
	/**
	 * Almacena el consecutivo digitado final
	 */
	private Long consFinAutorizacion;
	
	
	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de administración de las autorizaciones de capitación por rango
	 * 
	 * @author, 
	 *
	 */
	public void reset(){	
		this.estado="";
		this.sinAutorizaciones=true;
		this.listaAutorizaciones=new ArrayList<DTOAdministracionAutorizacion>();
		this.codigoAutorizacion=ConstantesBD.codigoNuncaValidoLong;		
		this.dtoFiltros=new DTOBusquedaAutorizacionCapitacionRango();
		this.listaEntidadesSub = new ArrayList<DtoEntidadSubcontratada>();
		this.estadoAutorizacion="";
		this.setConsInAutorizacion(ConstantesBD.codigoNuncaValidoLong);
		this.setConsFinAutorizacion(ConstantesBD.codigoNuncaValidoLong);
	}
		
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
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
		return this.posArray;
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
		return this.patronOrdenar;
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


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoFiltros
	
	 * @return retorna la variable dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public DTOBusquedaAutorizacionCapitacionRango getDtoFiltros() {
		return this.dtoFiltros;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoFiltros
	
	 * @param valor para el atributo dtoFiltros 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoFiltros(DTOBusquedaAutorizacionCapitacionRango dtoFiltros) {
		this.dtoFiltros = dtoFiltros;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEntidadesSub
	
	 * @return retorna la variable listaEntidadesSub 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSub() {
		return this.listaEntidadesSub;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEntidadesSub
	
	 * @param valor para el atributo listaEntidadesSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEntidadesSub(
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSub) {
		this.listaEntidadesSub = listaEntidadesSub;
	}

	/**
	 * 
	 * @param listaEstadosAutorizaciones
	 */
	public void setListaEstadosAutorizaciones(
			ArrayList<DtoAutorizacionEntSubContratada> listaEstadosAutorizaciones) {
		this.listaEstadosAutorizaciones = listaEstadosAutorizaciones;
	}


	/**
	 * 
	 * @return lista de estados de las autorizaciones
	 */
	public ArrayList<DtoAutorizacionEntSubContratada> getListaEstadosAutorizaciones() {
		return listaEstadosAutorizaciones;
	}


	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}


	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}


	public void setListaOpciones(ArrayList<DtoCheckBox> listaOpciones) {
		this.listaOpciones = listaOpciones;
	}


	public ArrayList<DtoCheckBox> getListaOpciones() {
		return listaOpciones;
	}


	/**
	 * @return the listaTiposAutorizacion
	 */
	public ArrayList<DtoCheckBox> getListaTiposAutorizacion() {
		return listaTiposAutorizacion;
	}


	/**
	 * @param listaTiposAutorizacion the listaTiposAutorizacion to set
	 */
	public void setListaTiposAutorizacion(
			ArrayList<DtoCheckBox> listaTiposAutorizacion) {
		this.listaTiposAutorizacion = listaTiposAutorizacion;
	}


	public void setConsInAutorizacion(Long consInAutorizacion) {
		this.consInAutorizacion = consInAutorizacion;
	}


	public Long getConsInAutorizacion() {
		return consInAutorizacion;
	}


	public void setConsFinAutorizacion(Long consFinAutorizacion) {
		this.consFinAutorizacion = consFinAutorizacion;
	}


	public Long getConsFinAutorizacion() {
		return consFinAutorizacion;
	}


	/**
	 * @return the listaTiposConsecutivoAutorizacion
	 */
	public ArrayList<DtoCheckBox> getListaTiposConsecutivoAutorizacion() {
		return listaTiposConsecutivoAutorizacion;
	}


	/**
	 * @param listaTiposConsecutivoAutorizacion the listaTiposConsecutivoAutorizacion to set
	 */
	public void setListaTiposConsecutivoAutorizacion(
			ArrayList<DtoCheckBox> listaTiposConsecutivoAutorizacion) {
		this.listaTiposConsecutivoAutorizacion = listaTiposConsecutivoAutorizacion;
	}


}
