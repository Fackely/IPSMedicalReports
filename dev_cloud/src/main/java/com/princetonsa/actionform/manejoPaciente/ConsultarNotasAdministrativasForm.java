package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.orm.DetNotasAdministrativas;
import com.servinte.axioma.orm.NotasAdministrativas;
import com.servinte.axioma.orm.OtrosDiagnosticos;
import com.servinte.axioma.orm.Ingresos;

@SuppressWarnings("serial")

public class ConsultarNotasAdministrativasForm extends ValidatorForm
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	transient private Logger logger = Logger
			.getLogger(NotasAdministrativasForm.class);

	/**
	 * Variable para manejar la direcci&oacute;n del workflow
	 */
	private String estado;

	/**
	 * DTO notas
	 */
	private NotasAdministrativas dto = new NotasAdministrativas();
	
	/**
	 * DTO detalles
	 */
	private DetNotasAdministrativas dtoDet = new DetNotasAdministrativas();
	
	/**
	 * Lista DTO Notas
	 */
	private ArrayList<NotasAdministrativas> listaDto;
	
	/**
	 * Lista DTO
	 */
	private ArrayList<Ingresos> listaDtoIngresos;
	
	/**
	 * Lista Detalles DTO
	 */
	private ArrayList<DetNotasAdministrativas> listaDetDto;

	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 */
	private boolean mostrarFormularioIngreso;
	/**
	 * Indica si se debe mostrar el formulario nuevo detalle
	 */
	private boolean mostrarFormularioIngresoDet;

	/**
	 * Parametros para ordenar
	 */
	private String patronOrdenar;

	/**
	 * Parametros para ordenar
	 */
	private String esDescendente;

	/**
	 * Paginador
	 */
	private int posArray;

	/**
	 * Paciente asociado al flujo
	 */
	//private PersonaBasica persona;
		
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public NotasAdministrativas getDto() {
		return dto;
	}

	public void setDto(NotasAdministrativas dto) {
		this.dto = dto;
	}

	public DetNotasAdministrativas getDtoDet() {
		return dtoDet;
	}

	public void setDtoDet(DetNotasAdministrativas dtoDet) {
		this.dtoDet = dtoDet;
	}

	public ArrayList<NotasAdministrativas> getListaDto() {
		return listaDto;
	}
	
	public ArrayList<DetNotasAdministrativas> getListaDetDto() {
		return listaDetDto;
	}

	public void setListaDto(ArrayList<NotasAdministrativas> listaDto) {
		this.listaDto = listaDto;
	}
	
	public ArrayList<Ingresos> getListaDtoIngresos() {
		return listaDtoIngresos;
	}

	public void setListaDtoIngresos(ArrayList<Ingresos> listaDtoIngresos) {
		this.listaDtoIngresos = listaDtoIngresos;
	}

	public void setListaDetDto(ArrayList<DetNotasAdministrativas> listaDetDto) {
		this.listaDetDto = listaDetDto;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * Lista que se debe utilizar para cargar los DTOs que pueden ser
	 * eliminables
	 */
	

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public int getPosArray() {
		return posArray;
	}

	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/*public PersonaBasica getPersona() {
		return persona;
	}

	public void setPersona(PersonaBasica persona) {
		this.persona = persona;
	}*/

	/**
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {

		return mostrarFormularioIngreso;

	}
	
	/**
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngresoDet() {

		return mostrarFormularioIngresoDet;

	}

	/**
	 * @param mostrarFormularioIngreso
	 *            the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}

	
	/**
	 * @param mostrarFormularioIngresoDet
	 *            the mostrarFormularioIngresoDet to set
	 */
	public void setMostrarFormularioIngresoDetalle(boolean mostrarFormularioIngresoDet) {
		this.mostrarFormularioIngresoDet = mostrarFormularioIngresoDet;
	}
	
	
	
	
	/**
	 * Reset 1 de la forma
	 */
	public void reset() {
		this.dto = new NotasAdministrativas();
		this.dtoDet = new DetNotasAdministrativas();
		this.listaDetDto = new ArrayList<DetNotasAdministrativas>();
		// this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		this.listaDto = new ArrayList<NotasAdministrativas>();
		this.patronOrdenar = "";
		this.esDescendente = "";
		this.mostrarFormularioIngreso = false;
		this.mostrarFormularioIngresoDet = false;
			

	}
	
	
	/**
	 * Reset 2 de la forma
	 */
	public void resetDos() {
		//this.dto = new NotasAdministrativas();
		this.dtoDet = new DetNotasAdministrativas();
		this.listaDetDto = new ArrayList<DetNotasAdministrativas>();
		// this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		//this.listaDto = new ArrayList<NotasAdministrativas>();
		this.mostrarFormularioIngreso = false;
		this.mostrarFormularioIngresoDet = false;
			

	}
	
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		// // medio modificado revisar errores reales, just as example

		if ((estado.equals("guardar")) || (estado.equals("guardarmodificar"))) {

			if (UtilidadTexto.isEmpty(dto.getDescripcionNota())) {
				errores.add("error descripcion", new ActionMessage(
						"errors.required", "La descripci&oacute;n"));
			}

		}

		return errores;
	}
	
	
	
}
