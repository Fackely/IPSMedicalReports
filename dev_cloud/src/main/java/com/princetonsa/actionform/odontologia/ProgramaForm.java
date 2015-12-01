package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoPrograma;

/**
 * 
 * @author axioma
 *
 */
public class ProgramaForm extends ValidatorForm  {
	

	
	/**
	 * ATRIBUTOS
	 */
	private int posArray;
	private String criterioBusqueda;
	private String patronOrdenar;
	private ArrayList<DtoPrograma> listaPrograma = new ArrayList<DtoPrograma>();
	private String estado;
	private DtoPrograma dtoProgramas;
	
	private  int tamanoLista;
	
	private String borrar;
	
	private String esDescendente;
	/**
	 * TEMPORAL BUSQUEDAD AVANZADA
	 */
	private String codigoHallazgos;
	
	
	private int codigoEspecialidad=0;
	
	private String estadoAnterior;
	
	private boolean incluirInactivos;
	
	/**
	 * Almacena el valor seleccionado para realizar la búsqueda de
	 * un programa por su código.
	 */
	private String criterioBusquedaPorCodigo; 
	
	/**
	 * Almacena el valor seleccionado para realizar la búsqueda de
	 * un programa por su código.
	 */
	private String criterioBusquedaPorDescripcion; 
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		return errores;
	}
	
 
	
	
	/**
	 * 
	 */
	
	public void reset(){
		 this.posArray=0;
		 this.criterioBusqueda="";
		 this.patronOrdenar="";
		 this.listaPrograma = new ArrayList<DtoPrograma>();
		 this.estado="";
		 this.dtoProgramas = new DtoPrograma();
		 this.esDescendente="";
	 	 //this.codigoHallazgos="";
		 this.setBorrar("borrar");
		 this.tamanoLista=0;
		 this.estadoAnterior="";
		 this.incluirInactivos=false;
		 this.criterioBusquedaPorCodigo = "";
		 this.criterioBusquedaPorDescripcion = "";
	 }
	
	
	
	/**
	 * LIMPIAR EL CODIGO TEMPORAL DE LA ESPECIALIDAD 
	 */
	public void resetEspecialidad(){
		this.codigoEspecialidad=ConstantesBD.codigoNuncaValido;
	}



	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	
	


	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}



	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}



	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}



	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}



	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}



	/**
	 * @return the listaPrograma
	 */
	public ArrayList<DtoPrograma> getListaPrograma() {
		return listaPrograma;
	}



	/**
	 * @param listaPrograma the listaPrograma to set
	 */
	public void setListaPrograma(ArrayList<DtoPrograma> listaPrograma) {
		this.listaPrograma = listaPrograma;
	}



	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}



	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}



	/**
	 * @return the dtoProgramas
	 */
	public DtoPrograma getDtoProgramas() {
		return dtoProgramas;
	}



	/**
	 * @param dtoProgramas the dtoProgramas to set
	 */
	public void setDtoProgramas(DtoPrograma dtoProgramas) {
		this.dtoProgramas = dtoProgramas;
	}


	public void setCodigoHallazgos(String codigoHallazgos) {
		this.codigoHallazgos = codigoHallazgos;
	}


	public String getCodigoHallazgos() {
		return codigoHallazgos;
	}


	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	public String getEsDescendente() {
		return esDescendente;
	}


	public void setBorrar(String borrar) {
		this.borrar = borrar;
	}


	public String getBorrar() {
		return borrar;
	}




	public void setTamanoLista(int tamanoLista) {
		this.tamanoLista = tamanoLista;
	}




	public int getTamanoLista() {
		tamanoLista=this.listaPrograma.size();
		return tamanoLista;
	}




	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}




	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}




	public String getEstadoAnterior() {
		return estadoAnterior;
	}




	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}




	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  incluirInactivos
	 *
	 * @return retorna la variable incluirInactivos
	 */
	public boolean isIncluirInactivos() {
		return incluirInactivos;
	}




	/**
	 * Método que se encarga de establecer el valor
	 * del atributo incluirInactivos
	 * @param incluirInactivos es el valor para el atributo incluirInactivos 
	 */
	public void setIncluirInactivos(boolean incluirInactivos) {
		this.incluirInactivos = incluirInactivos;
	}




	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  criterioBusquedaPorCodigo
	 *
	 * @return retorna la variable criterioBusquedaPorCodigo
	 */
	public String getCriterioBusquedaPorCodigo() {
		return criterioBusquedaPorCodigo;
	}




	/**
	 * Método que se encarga de establecer el valor
	 * del atributo criterioBusquedaPorCodigo
	 * @param criterioBusquedaPorCodigo es el valor para el atributo criterioBusquedaPorCodigo 
	 */
	public void setCriterioBusquedaPorCodigo(String criterioBusquedaPorCodigo) {
		this.criterioBusquedaPorCodigo = criterioBusquedaPorCodigo;
	}




	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  criterioBusquedaPorDescripcion
	 *
	 * @return retorna la variable criterioBusquedaPorDescripcion
	 */
	public String getCriterioBusquedaPorDescripcion() {
		return criterioBusquedaPorDescripcion;
	}




	/**
	 * Método que se encarga de establecer el valor
	 * del atributo criterioBusquedaPorDescripcion
	 * @param criterioBusquedaPorDescripcion es el valor para el atributo criterioBusquedaPorDescripcion 
	 */
	public void setCriterioBusquedaPorDescripcion(
			String criterioBusquedaPorDescripcion) {
		this.criterioBusquedaPorDescripcion = criterioBusquedaPorDescripcion;
	}
	
}
