package com.princetonsa.actionform.parametrizacion;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.TiposRegimen;


/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada uno.
 *
 * @version 2.0
 * @author Angela Aguirre
 */
public class ExcepcionesNaturalezaPacienteForm extends ActionForm{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Almacena la acci&oacute;n a realizar desde las p&aacute;gina
	 */
	private String estado;
	
	/**
	 * Atributo que almacena el &iacute;ndice de la posici&oacute;n 
	 * en donde se encuentra el registro seleccionado.
	 */
	private int index;

	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * atributo usado para la paginaci&oacute;n del listado de faltantes sobrantes encontrados.
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

	private String mostrarMensaje;
	
	/**
	 * Atributo que almacena el tama�o inicial de la 
	 * lista que contiene los registros de naturalezas
	 * pacientes obtenida de la base de datos
	 */
	private int longitudListaInical;
	
	/**
	 * Indica la cantidad de registros por p&aacute;gina
	 */
	private Integer offset;
	
	/**
	 * Objeto que guarda la informaci�n de las naturalezas paciente
	 */
	private ArrayList<DTOExcepcionNaturalezaPaciente> listaExcepcionesNaturalezaPaciente;
	
	/**
	 * Objeto que guarda las naturalezas pacientes
	 */
	private ArrayList<NaturalezaPacientes> listaNaturalezasPaciente;
	
	/**
	 * Objeto que guarda los tipos de r&eacute;gimen
	 */
	private ArrayList<TiposRegimen> listaTiposRegimen;
	
	
	public ExcepcionesNaturalezaPacienteForm () {
		listaExcepcionesNaturalezaPaciente = new ArrayList<DTOExcepcionNaturalezaPaciente>();
		offset = new Integer(0);
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
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		this.setMostrarMensaje("");
		ActionErrors errores=new ActionErrors();
		DTOExcepcionNaturalezaPaciente nuevoRegistro =null;
		
		if(estado.equals("guardar")){
			for(int i =0; i<listaExcepcionesNaturalezaPaciente.size();i++){
				nuevoRegistro = new DTOExcepcionNaturalezaPaciente(); 
				nuevoRegistro=listaExcepcionesNaturalezaPaciente.get(i);			
				
				if(UtilidadTexto.isEmpty(nuevoRegistro.getAcronimoTipoRegimen())){			
					errores.add("El tipo de Regimen es requerido", new 
							ActionMessage("errores.modManejoPacienteExcepcionNaturaleza.regimenRequerido"));
				}
				if(nuevoRegistro.getCodigoNaturalezaPaciente()==ConstantesBD.codigoNuncaValido){
					errores.add("La naturaleza de paciente es requerida", 
							new ActionMessage("errores.modManejoPacienteExcepcionNaturaleza.naturalezaPacienteRequerido"));
				}			
			}
		}		
		return errores;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo index
	
	 * @return retorna la variable index 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo index
	
	 * @param valor para el atributo index 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndex(int index) {
		this.index = index;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo path
	
	 * @return retorna la variable path 
	 * @author Angela Maria Aguirre 
	 */
	public String getPath() {
		return path;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo path
	
	 * @param valor para el atributo path 
	 * @author Angela Maria Aguirre 
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo posArray
	
	 * @return retorna la variable posArray 
	 * @author Angela Maria Aguirre 
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo posArray
	
	 * @param valor para el atributo posArray 
	 * @author Angela Maria Aguirre 
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo patronOrdenar
	
	 * @return retorna la variable patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo patronOrdenar
	
	 * @param valor para el atributo patronOrdenar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo esDescendente
	
	 * @return retorna la variable esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo esDescendente
	
	 * @param valor para el atributo esDescendente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo mostrarMensaje
	
	 * @return retorna la variable mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMostrarMensaje() {
		return mostrarMensaje;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo mostrarMensaje
	
	 * @param valor para el atributo mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarMensaje(String mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo longitudListaInical
	
	 * @return retorna la variable longitudListaInical 
	 * @author Angela Maria Aguirre 
	 */
	public int getLongitudListaInical() {
		return longitudListaInical;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo longitudListaInical
	
	 * @param valor para el atributo longitudListaInical 
	 * @author Angela Maria Aguirre 
	 */
	public void setLongitudListaInical(int longitudListaInical) {
		this.longitudListaInical = longitudListaInical;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo offset
	
	 * @return retorna la variable offset 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getOffset() {
		return offset;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo offset
	
	 * @param valor para el atributo offset 
	 * @author Angela Maria Aguirre 
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaExcepcionesNaturalezaPaciente
	
	 * @return retorna la variable listaExcepcionesNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOExcepcionNaturalezaPaciente> getListaExcepcionesNaturalezaPaciente() {
		return listaExcepcionesNaturalezaPaciente;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaExcepcionesNaturalezaPaciente
	
	 * @param valor para el atributo listaExcepcionesNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaExcepcionesNaturalezaPaciente(
			ArrayList<DTOExcepcionNaturalezaPaciente> listaExcepcionesNaturalezaPaciente) {
		this.listaExcepcionesNaturalezaPaciente = listaExcepcionesNaturalezaPaciente;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaNaturalezasPaciente
	
	 * @return retorna la variable listaNaturalezasPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<NaturalezaPacientes> getListaNaturalezasPaciente() {
		return listaNaturalezasPaciente;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaNaturalezasPaciente
	
	 * @param valor para el atributo listaNaturalezasPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNaturalezasPaciente(
			ArrayList<NaturalezaPacientes> listaNaturalezasPaciente) {
		this.listaNaturalezasPaciente = listaNaturalezasPaciente;
	}


	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo listaTiposRegimen
	
	 * @return retorna la variable listaTiposRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposRegimen> getListaTiposRegimen() {
		return listaTiposRegimen;
	}


	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo listaTiposRegimen
	
	 * @param valor para el atributo listaTiposRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposRegimen(ArrayList<TiposRegimen> listaTiposRegimen) {
		this.listaTiposRegimen = listaTiposRegimen;
	}	
	
}