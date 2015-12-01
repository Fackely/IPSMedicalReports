/*
 * Julio 13, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoValidacionesTrasladoAbonoPaciente;
import com.servinte.axioma.orm.TiposIdentificacion;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */

public class TrasladoAbonoPacienteForm extends ActionForm 
{
	
	private static final long serialVersionUID = 1L;
	
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	
	/** * Tipos de identificacion del sistema */
	private ArrayList<TiposIdentificacion> listaTiposIdentificacion;
	
	/** * Numero de identificacion ingresado como parametro de busqueda */
	private String identificacionBuscar;
	
	/** * Tipo de identificacion ingresado como parametro de busqueda */
	private String acronimoTipoIdentificacion;
	
	/** * Paciente que realiza el traslado  */
	private DtoPersonas pacienteOrigen;
	
	/** * Pacientes que reciben el traslado  */
	private ArrayList<DtoPersonas> listaPacientesDestino;
	
	/** * Contiene el resultado de las validaciones del paciente origen para la vista  */
	private DtoValidacionesTrasladoAbonoPaciente validacionesOrigen;
	
	/** * Contiene el resultado de las validaciones del paciente destino para la vista  */
	private DtoValidacionesTrasladoAbonoPaciente validacionesDestino;
	
	/** * Indica como se debe presentar los abonos del paciente */
	private boolean mostrarAbonosTotalizados;
	
	/** * Indica como se debe presentar los abonos del paciente */
	private boolean mostrarAbonosPorIngreso;
	
	/** * Lista de ingresos con sus abonos */
	private ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaDtoInfoIngresoTrasladoAbonoPaciente;
	
	/** * Indica como se debe presentar los abonos del paciente */
	private boolean mostrarBuscarPacienteDestino;
	
	/** * Indica como se debe presentar los abonos del paciente */
	private boolean mostrarInformacionOrigen;
	
	/** * Total de abono que se desea trasladar  */
	private double totalAbonoTrasladar;
	
	/** * Numero de identificacion ingresado como parametro de busqueda para pacientes destino */
	private String identificacionBuscarDestino;
	
	/** * Tipo de identificacion ingresado como parametro de busqueda para pacientes destino */
	private String acronimoTipoIdentificacionDestino;
	
	/** * Lista de ingresos con sus abonos */
	private ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaDtoInfoIngresoTrasladoAbonoPacienteDestino;
	
	/** * Incica la posicion de la lista de los pacientes destino  */
	private int posListaPacDestino;
	
	/** - Total de los traslados tomados del paciente origen */
	private Double totalTraslado; 
	
	/** * Indica cuando se puede mostrar el boton de traslado */
	private boolean mostrarBotonTrasladar;
	
	/** * Dto con la informacion a mostar como resumen */
	private ArrayList<DtoConsultaTrasladoAbonoPAciente> listaDtoConsultaTrasladoAbonoPAciente;
	
	
	
	
	
	
	/*-------------------------------------------------------*/
	/* RESET's
	/*-------------------------------------------------------*/
	

	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.listaTiposIdentificacion 			= new ArrayList<TiposIdentificacion>();
		this.identificacionBuscar				= "";
		this.acronimoTipoIdentificacion			= "";
		this.pacienteOrigen						= null;
		this.validacionesOrigen					= new DtoValidacionesTrasladoAbonoPaciente();
		this.validacionesDestino				= new DtoValidacionesTrasladoAbonoPaciente();
		this.mostrarAbonosTotalizados			= false;
		this.mostrarAbonosPorIngreso			= false;
		this.listaDtoInfoIngresoTrasladoAbonoPaciente			= new ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>();
		this.listaDtoInfoIngresoTrasladoAbonoPacienteDestino	= new ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>();
		this.mostrarInformacionOrigen			= false;
		this.mostrarBuscarPacienteDestino		= false;
		this.listaPacientesDestino				= new ArrayList<DtoPersonas>();
		this.totalAbonoTrasladar				= ConstantesBD.codigoNuncaValidoDouble;
		this.identificacionBuscarDestino		= "";
		this.acronimoTipoIdentificacionDestino	= "";
		this.posListaPacDestino					= ConstantesBD.codigoNuncaValido;
		this.totalTraslado						= 0d;
		this.mostrarBotonTrasladar				= false;		
		this.listaDtoConsultaTrasladoAbonoPAciente	=  new ArrayList<DtoConsultaTrasladoAbonoPAciente>();
		resetMostrarAbonos();
	}
	
	/**
	 * Reset de la forma de lo que corresponde a la forma de presentacion de los abonos
	 */
	public void resetMostrarAbonos()
	{
		this.mostrarAbonosTotalizados		= false;
		this.mostrarAbonosPorIngreso		= false;
	}
	
		
	
	/*-------------------------------------------------------*/
	/* VALIDACION
	/*-------------------------------------------------------*/
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("buscarpaciente"))
		{
			if(UtilidadTexto.isEmpty(this.getAcronimoTipoIdentificacion()))
			{
				errores.add("error_traslado_abono_pac", new ActionMessage("errors.required", " El Tipo de Identificación"));
			}
			
			if(UtilidadTexto.isEmpty(this.getIdentificacionBuscar()))
			{
				errores.add("error_traslado_abono_pac", new ActionMessage("errors.required", " El Número de identificación "));
			}
		}
		
		return errores;
	}


	
	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public ArrayList<TiposIdentificacion> getListaTiposIdentificacion() {
		return listaTiposIdentificacion;
	}


	public void setListaTiposIdentificacion(
			ArrayList<TiposIdentificacion> listaTiposIdentificacion) {
		this.listaTiposIdentificacion = listaTiposIdentificacion;
	}


	public String getIdentificacionBuscar() {
		return identificacionBuscar;
	}


	public void setIdentificacionBuscar(String identificacionBuscar) {
		this.identificacionBuscar = identificacionBuscar;
	}


	public String getAcronimoTipoIdentificacion() {
		return acronimoTipoIdentificacion;
	}


	public void setAcronimoTipoIdentificacion(String acronimoTipoIdentificacion) {
		this.acronimoTipoIdentificacion = acronimoTipoIdentificacion;
	}


	public DtoPersonas getPacienteOrigen() {
		return pacienteOrigen;
	}


	public void setPacienteOrigen(DtoPersonas pacienteOrigen) {
		this.pacienteOrigen = pacienteOrigen;
	}

	
	public boolean isMostrarAbonosTotalizados() {
		return mostrarAbonosTotalizados;
	}

	public boolean isMostrarAbonosPorIngreso() {
		return mostrarAbonosPorIngreso;
	}

	public void setMostrarAbonosTotalizados(boolean mostrarAbonosTotalizados) {
		this.mostrarAbonosTotalizados = mostrarAbonosTotalizados;
	}

	public void setMostrarAbonosPorIngreso(boolean mostrarAbonosPorIngreso) {
		this.mostrarAbonosPorIngreso = mostrarAbonosPorIngreso;
	}

	public ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> getListaDtoInfoIngresoTrasladoAbonoPaciente() {
		return listaDtoInfoIngresoTrasladoAbonoPaciente;
	}

	public void setListaDtoInfoIngresoTrasladoAbonoPaciente(
			ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaDtoInfoIngresoTrasladoAbonoPaciente) {
		this.listaDtoInfoIngresoTrasladoAbonoPaciente = listaDtoInfoIngresoTrasladoAbonoPaciente;
	}


	public boolean isMostrarBuscarPacienteDestino() {
		return mostrarBuscarPacienteDestino;
	}

	
	public void setMostrarBuscarPacienteDestino(boolean mostrarBuscarPacienteDestino) {
		this.mostrarBuscarPacienteDestino = mostrarBuscarPacienteDestino;
	}

	public boolean isMostrarInformacionOrigen() {
		return mostrarInformacionOrigen;
	}

	public void setMostrarInformacionOrigen(boolean mostrarInformacionOrigen) {
		this.mostrarInformacionOrigen = mostrarInformacionOrigen;
	}

	public double getTotalAbonoTrasladar() {
		return totalAbonoTrasladar;
	}

	public void setTotalAbonoTrasladar(double totalAbonoTrasladar) {
		this.totalAbonoTrasladar = totalAbonoTrasladar;
	}

	public ArrayList<DtoPersonas> getListaPacientesDestino() {
		return listaPacientesDestino;
	}

	public void setListaPacientesDestino(
			ArrayList<DtoPersonas> listaPacientesDestino) {
		this.listaPacientesDestino = listaPacientesDestino;
	}

	public String getIdentificacionBuscarDestino() {
		return identificacionBuscarDestino;
	}

	public String getAcronimoTipoIdentificacionDestino() {
		return acronimoTipoIdentificacionDestino;
	}

	public void setIdentificacionBuscarDestino(String identificacionBuscarDestino) {
		this.identificacionBuscarDestino = identificacionBuscarDestino;
	}

	public void setAcronimoTipoIdentificacionDestino(
			String acronimoTipoIdentificacionDestino) {
		this.acronimoTipoIdentificacionDestino = acronimoTipoIdentificacionDestino;
	}

	public DtoValidacionesTrasladoAbonoPaciente getValidacionesOrigen() {
		return validacionesOrigen;
	}

	public void setValidacionesOrigen(
			DtoValidacionesTrasladoAbonoPaciente validacionesOrigen) {
		this.validacionesOrigen = validacionesOrigen;
	}

	public DtoValidacionesTrasladoAbonoPaciente getValidacionesDestino() {
		return validacionesDestino;
	}

	public void setValidacionesDestino(
			DtoValidacionesTrasladoAbonoPaciente validacionesDestino) {
		this.validacionesDestino = validacionesDestino;
	}

	public ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> getListaDtoInfoIngresoTrasladoAbonoPacienteDestino() {
		return listaDtoInfoIngresoTrasladoAbonoPacienteDestino;
	}

	public void setListaDtoInfoIngresoTrasladoAbonoPacienteDestino(
			ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaDtoInfoIngresoTrasladoAbonoPacienteDestino) {
		this.listaDtoInfoIngresoTrasladoAbonoPacienteDestino = listaDtoInfoIngresoTrasladoAbonoPacienteDestino;
	}

	public int getPosListaPacDestino() {
		return posListaPacDestino;
	}

	public void setPosListaPacDestino(int posListaPacDestino) {
		this.posListaPacDestino = posListaPacDestino;
	}
	
	public Double getTotalTraslado() {
		return totalTraslado;
	}

	public void setTotalTraslado(Double totalTraslado) {
		this.totalTraslado = totalTraslado;
	}

	public boolean isMostrarBotonTrasladar() {
		return mostrarBotonTrasladar;
	}

	public void setMostrarBotonTrasladar(boolean mostrarBotonTrasladar) {
		this.mostrarBotonTrasladar = mostrarBotonTrasladar;
	}

	public ArrayList<DtoConsultaTrasladoAbonoPAciente> getListaDtoConsultaTrasladoAbonoPAciente() {
		return listaDtoConsultaTrasladoAbonoPAciente;
	}

	public void setListaDtoConsultaTrasladoAbonoPAciente(
			ArrayList<DtoConsultaTrasladoAbonoPAciente> listaDtoConsultaTrasladoAbonoPAciente) {
		this.listaDtoConsultaTrasladoAbonoPAciente = listaDtoConsultaTrasladoAbonoPAciente;
	}


}
