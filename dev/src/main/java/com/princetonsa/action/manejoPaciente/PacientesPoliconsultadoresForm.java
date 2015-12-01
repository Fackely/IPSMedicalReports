package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.ViasIngreso;

import javax.servlet.http.HttpServletRequest;

/**
 * Fecha: Febrero - 2012
 * 
 * @author cesgompe
 * 
 */

public class PacientesPoliconsultadoresForm extends ValidatorForm {

	/**
	 * Tipo de Identificacion
	 */
	private String tipoIdentificacionPac;    
	private ArrayList<HashMap<String, Object>> listTipIdent;
    private String soloNumeros;
    
    /**
     * Numero de Indentificacion     
     */
    private String numeroIdentificacion;
    
    /**
     * Cantidad de Servicios
     */
    private String cantidadServicios;
    
    /**
     * Fecha Inicial de Generacion
     */
    private String fechaInicial;
    
    /**
     * Fecha Final de Generacion
     */
    private String fechaFinal;
    	
    /**
	 * Tipo de Convenio
	 */
	private String convenio;    
    private ArrayList<DtoConvenio> convenioList;
    
    /**
	 * Via de Ingreso
	 */
	private String viaIngreso;
    private ArrayList<ViasIngreso> viaIngresoList;
    
    /**
	 * Unidad Agenda
	 */
	private String unidadAgenda;    
    private ArrayList<DtoUnidadesConsulta> unidadAgendaList;
    
    /**
  	 * Tipo de Servicio
  	 */
  	private String tipoServicio;
	private ArrayList<TiposServicio> tipoServicioList;
    
    /**
  	 * Especialidad
  	 */
  	private String especialidad;
	private ArrayList<DtoEspecialidades> especialidadList; 
    
    /**
     * Estado del Form
     */
	private String estado = "";
	/**
     * Response AJAX
     */
	private String result;
	/**
     * MaxDigitos
     */
	private int numDigCaptNumId;
	
	/**
	 * Si hace la insercion correctamente
	 */	
	private boolean insercionCorrecta; 
	
	/**
	 * Nombre del archivo generado
	 * */
    private String nombreArchivoGenerado;
    /**
	 * Nombre copia del archivo generado
	 * */
    private String nombreArchivoCopia;
    /**
     * Mapa para la factura en la busqueda Avanzada
     */
    private HashMap mapaFacturasTodos;
    
    
    private String tipoSalida="";
	
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{		
		this.tipoIdentificacionPac = "";    
		this.listTipIdent = null;
		this.soloNumeros = "";	    
		this.numeroIdentificacion = "";
		this.cantidadServicios = "";	    
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.convenio = "";    
		this.convenioList = null;
		this.viaIngreso = "";
		this.viaIngresoList = null;
		this.unidadAgenda = "";    
		this.unidadAgendaList = null;
		this.tipoServicio = "";
		this.tipoServicioList = null;
		this.especialidad = "";
		this.especialidadList = null; 
		this.estado = "";
		this.result = "";
		this.numDigCaptNumId = 0;
		this.insercionCorrecta = false; 

		this.nombreArchivoGenerado="";
		this.nombreArchivoCopia="";
		this.tipoSalida="";
	}
	
	
	
	
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		boolean validacion = false;
		
		//********************NUEVOS ESTADOS DE VALDIACION*******************************
		if(this.estado.equals("validarPaciente")) {

			if(this.tipoIdentificacionPac.equals("") && this.numeroIdentificacion.trim().equals("") ){
				if( this.cantidadServicios.trim().equals("") ){
					errores.add("", new ActionMessage("errors.required","El tipo y número de identificación de un paciente o la cantidad mínima de servicios facturados"));					
				}
			}
			if(!this.tipoIdentificacionPac.equals("") && this.numeroIdentificacion.trim().equals("") ){				
					errores.add("", new ActionMessage("errors.required","El número de identificación"));				
			}
			if( !this.cantidadServicios.trim().equals("") ){
				if(this.fechaInicial.equals("")) {
					errores.add("",new ActionMessage("errors.required", "El Campo Fecha Inicial"));
					validacion = true;
				}
				if(this.fechaFinal.equals("")) {
					errores.add("",new ActionMessage("errors.required", "El Campo Fecha Final"));
					validacion = true;
				}
				if( validacion ){
					this.nombreArchivoGenerado = "";
					this.nombreArchivoCopia = "";
					return errores;
				}				
				if(!UtilidadFecha.validarFecha(this.fechaInicial)){
					errores.add("fechaInicio",new ActionMessage("errors.formatoFechaInvalido", "Inicial"));
					validacion = true;
				}
				if(!UtilidadFecha.validarFecha(this.fechaFinal)){
					errores.add("fechaFinal",new ActionMessage("errors.formatoFechaInvalido", "Final"));
					validacion = true;
				}
				if( validacion ){
					this.nombreArchivoGenerado = "";
					this.nombreArchivoCopia = "";
					return errores;
				}
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
						UtilidadFecha.getFechaActual())) {
					errores.add("fechaInicio",new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
					validacion = true;
				}
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal), 
						UtilidadFecha.getFechaActual())) {
					errores.add("fechaFinal",new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
					validacion = true;
				}
				if( validacion ){
					this.nombreArchivoGenerado = "";
					this.nombreArchivoCopia = "";
					return errores;
				}				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
							UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal))){						
						errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
						validacion = true;
				}
				if( validacion ){
					this.nombreArchivoGenerado = "";
					this.nombreArchivoCopia = "";
					return errores;
				}
				int diferencias = UtilidadFecha.numeroDiasEntreFechas(this.fechaInicial, this.fechaFinal);
				int cantidadServicios = Integer.parseInt(this.cantidadServicios);
				switch( cantidadServicios ){
					case 1:
						if( diferencias >= 8 ){
							errores.add("Campo Fecha Final", new ActionMessage("errors.fechaSuperaOtraPorDias", "Inicial ", "7", "Final "));
							validacion = true;
						}
					break;
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						if( diferencias >= 32 ){
							errores.add("Campo Fecha Final", new ActionMessage("errors.fechaSuperaOtraPorDias", "Inicial ", "31", "Final "));
							validacion = true;
						}
					break;
					default:
						if( diferencias >= 366 ){
							errores.add("Campo Fecha Final", new ActionMessage("errors.fechaSuperaOtraPorDias", "Inicial ", "365", "Final "));
							validacion = true;
						}
						break;
				}
				if( validacion ){
					this.nombreArchivoGenerado = "";
					this.nombreArchivoCopia = "";
					return errores;
				}
				
			}			
			if(UtilidadCadena.tieneCaracteresEspecialesNumeroId(this.numeroIdentificacion))
			{
				errores.add("", new ActionMessage("errors.caracteresInvalidos","El número de identificación"));
			}
			if(!errores.isEmpty())
			{
				this.estado = "";
			}
		}		
		return errores;
	}

	
	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the soloNumeros
	 */
	public String getSoloNumeros() {
		return soloNumeros;
	}

	/**
	 * @param soloNumeros the soloNumeros to set
	 */
	public void setSoloNumeros(String soloNumeros) {
		this.soloNumeros = soloNumeros;
	}

	/**
	 * @return the cantidadServicios
	 */
	public String getCantidadServicios() {
		return cantidadServicios;
	}

	/**
	 * @param cantidadServicios the cantidadServicios to set
	 */
	public void setCantidadServicios(String cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the convenioList
	 */
	public ArrayList<DtoConvenio> getConvenioList() {
		return convenioList;
	}

	/**
	 * @param convenioList the convenioList to set
	 */
	public void setConvenioList(ArrayList<DtoConvenio> convenioList) {
		this.convenioList = convenioList;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the viaIngresoList
	 */
	public ArrayList<ViasIngreso> getViaIngresoList() {
		return viaIngresoList;
	}

	/**
	 * @param viaIngresoList the viaIngresoList to set
	 */
	public void setViaIngresoList(ArrayList<ViasIngreso> viaIngresoList) {
		this.viaIngresoList = viaIngresoList;
	}

	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the unidadAgendaList
	 */
	public ArrayList<DtoUnidadesConsulta> getUnidadAgendaList() {
		return unidadAgendaList;
	}

	/**
	 * @param unidadAgendaList the unidadAgendaList to set
	 */
	public void setUnidadAgendaList(ArrayList<DtoUnidadesConsulta> unidadAgendaList) {
		this.unidadAgendaList = unidadAgendaList;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	
	/**
	 * @return the tipoServicioList
	 */
	public ArrayList<TiposServicio> getTipoServicioList() {
		return tipoServicioList;
	}

	/**
	 * @param tipoServicioList the tipoServicioList to set
	 */
	public void setTipoServicioList(ArrayList<TiposServicio> tipoServicioList) {
		this.tipoServicioList = tipoServicioList;
	}

	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the especialidadList
	 */
	public ArrayList<DtoEspecialidades> getEspecialidadList() {
		return especialidadList;
	}

	/**
	 * @param especialidadList the especialidadList to set
	 */
	public void setEspecialidadList(ArrayList<DtoEspecialidades> especialidadList) {
		this.especialidadList = especialidadList;
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
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	

	/**
	 * @return the insercionCorrecta
	 */
	public boolean isInsercionCorrecta() {
		return insercionCorrecta;
	}

	/**
	 * @param insercionCorrecta the insercionCorrecta to set
	 */
	public void setInsercionCorrecta(boolean insercionCorrecta) {
		this.insercionCorrecta = insercionCorrecta;
	}

	/**
	 * @return the numDigCaptNumId
	 */
	public int getNumDigCaptNumId() {
		return numDigCaptNumId;
	}

	/**
	 * @param numDigCaptNumId the numDigCaptNumId to set
	 */
	public void setNumDigCaptNumId(int numDigCaptNumId) {
		this.numDigCaptNumId = numDigCaptNumId;
	}


	/**
	 * @return the tipoIdentificacionPac
	 */
	public String getTipoIdentificacionPac() {
		return tipoIdentificacionPac;
	}


	/**
	 * @param tipoIdentificacionPac the tipoIdentificacionPac to set
	 */
	public void setTipoIdentificacionPac(String tipoIdentificacionPac) {
		this.tipoIdentificacionPac = tipoIdentificacionPac;
	}


	/**
	 * @return the listTipIdent
	 */
	public ArrayList<HashMap<String, Object>> getListTipIdent() {
		return listTipIdent;
	}


	/**
	 * @param listTipIdent the listTipIdent to set
	 */
	public void setListTipIdent(ArrayList<HashMap<String, Object>> listTipIdent) {
		this.listTipIdent = listTipIdent;
	}




	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}




	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}




	/**
	 * @return the nombreArchivoCopia
	 */
	public String getNombreArchivoCopia() {
		return nombreArchivoCopia;
	}




	/**
	 * @param nombreArchivoCopia the nombreArchivoCopia to set
	 */
	public void setNombreArchivoCopia(String nombreArchivoCopia) {
		this.nombreArchivoCopia = nombreArchivoCopia;
	}




	/**
	 * @return the mapaFacturasTodos
	 */
	public HashMap getMapaFacturasTodos() {
		return mapaFacturasTodos;
	}




	/**
	 * @param mapaFacturasTodos the mapaFacturasTodos to set
	 */
	public void setMapaFacturasTodos(HashMap mapaFacturasTodos) {
		this.mapaFacturasTodos = mapaFacturasTodos;
	}




	/**
	 * @return
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}




	/**
	 * @param tipoSalida
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}
	
	
	
	
	
}