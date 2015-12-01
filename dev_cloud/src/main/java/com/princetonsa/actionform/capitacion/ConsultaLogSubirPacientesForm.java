package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.princetonsa.dto.capitacion.DtoInconsistenciasArchivoPlano;


public class ConsultaLogSubirPacientesForm extends ValidatorForm
{
	/**
	 * Manejo del flujo
	 */
	private String estado;

	/**
	 * Convenio seleccionado para la busqueda de los logs
	 */
	private InfoDatos convenio;
	
	/**
	 * Fecha inicial para la busqueda de los logs
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final para la búsqueda de los logs
	 */
	private String fechaFinal;
	
	/**
	 * Para buscar por usuario en el log
	 */
	private String usuario;
	
	/**
	 * lista de las inconsistencias generales
	 */
	private ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasGenerales;
	
	/**
	 * lista de las inconsistencias de validaciones de campos
	 */
	private List<DtoInconsistenciasArchivoPlano> listaInconsitenciasCampos;
	
	/**
	 * lista de las inconsistencias de personas con cargue previo
	 */
	private ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasCarguePrevio;
	
	/**
	 * lista de las inconsistencias de personas con mismos datos
	 */
	private ArrayList<DtoInconsistenciasArchivoPlano> listaInconsitenciasPersonas;
	
	/**
	 * lista de los logs de subir paciente
	 */
	private ArrayList<DTOConsultaLogSubirPacientes> listaConsultaLogsubirPacientes;
	
		
	/***PARAMETRO DE BUSQUEDA PARA LAS INCONSISTENCIAS POR LA TABLA (inconsisten_subir_paciente)*/
	private Long codigoPkSubirPaciente;
	
	private int indexLista;
	
	/**
	 * 
	 */
	private String columnaOrden;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;
	
	

	public void cleanCompleto()
	{
		this.estado="empezar";
		this.convenio=new InfoDatos();
		this.fechaInicial="";
		this.fechaFinal="";
		this.columnaOrden="nombreconvenio";
		this.usuario = "";
		this.setListaConsultaLogsubirPacientes(new ArrayList<DTOConsultaLogSubirPacientes>());
		this.codigoPkSubirPaciente=ConstantesBD.codigoNuncaValidoLong;
		this.listaInconsistenciasGenerales= new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.listaInconsistenciasCarguePrevio= new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.listaInconsitenciasCampos= new ArrayList<DtoInconsistenciasArchivoPlano>();
		this.listaInconsitenciasPersonas= new ArrayList<DtoInconsistenciasArchivoPlano>();
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();

		if(this.getEstado().equals("buscar"))
		{
			boolean hayFechaInicial=false;
            if(UtilidadCadena.noEsVacio(this.getFechaInicial()))
            {
                if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
                {
                    errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
                }
                else
                {
                	hayFechaInicial=true;
            		if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFechaInicial()))
            		{
        	    		errores.add("La fecha inicial debe ser menor a la actual", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial", UtilidadFecha.getFechaActual()));
            		}
                }
            }

            if(UtilidadCadena.noEsVacio(this.getFechaFinal()))
            {
                if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
                {
                    errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
                }
                else
                {
            		if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getFechaFinal()))
            		{
        	    		errores.add("La fecha inicial debe ser menor a la actual", new ActionMessage("errors.fechaPosteriorIgualActual", "final", UtilidadFecha.getFechaActual()));
            		}
            		
            		if(hayFechaInicial && UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(), this.getFechaInicial()))
            		{
        	    		errores.add("La fecha final debe ser mayor o igual a la actual", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "final", "inicial"));
            		}
                }
            }
        
            if(!UtilidadCadena.noEsVacio(this.getFechaInicial()) && !UtilidadCadena.noEsVacio(this.getFechaFinal()) && 
            		!UtilidadCadena.noEsVacio(this.getUsuario()) && !UtilidadCadena.noEsVacio(this.getConvenio().getId()))
            {
	    		errores.add("Requerido por lo menos un parámetro", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "1"));
            }
            if(!errores.isEmpty())
            {
            	this.setEstado("buscando");
            }
		}
		
		return errores;
	}

	/**
	 * @return Returns the convenio.
	 */
	public InfoDatos getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatos convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{	
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the columnaOrden.
	 */
	public String getColumnaOrden()
	{
		return columnaOrden;
	}

	/**
	 * @param columnaOrden The columnaOrden to set.
	 */
	public void setColumnaOrden(String columnaOrden)
	{
		this.columnaOrden = columnaOrden;
	}

	
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}
	
   	public void setListaConsultaLogsubirPacientes(
			ArrayList<DTOConsultaLogSubirPacientes> listaConsultaLogsubirPacientes) {
		this.listaConsultaLogsubirPacientes = listaConsultaLogsubirPacientes;
	}

	public ArrayList<DTOConsultaLogSubirPacientes> getListaConsultaLogsubirPacientes() {
		return listaConsultaLogsubirPacientes;
	}

	public void setCodigoPkSubirPaciente(Long codigoPkSubirPaciente) {
		this.codigoPkSubirPaciente = codigoPkSubirPaciente;
	}

	public Long getCodigoPkSubirPaciente() {
		return codigoPkSubirPaciente;
	}

	public void setIndexLista(int indexLista) {
		this.indexLista = indexLista;
	}

	public int getIndexLista() {
		return indexLista;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the listaInconsistenciasGenerales
	 */
	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsistenciasGenerales() {
		return listaInconsistenciasGenerales;
	}

	/**
	 * @param listaInconsistenciasGenerales the listaInconsistenciasGenerales to set
	 */
	public void setListaInconsistenciasGenerales(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasGenerales) {
		this.listaInconsistenciasGenerales = listaInconsistenciasGenerales;
	}

	/**
	 * @return the listaInconsitenciasCampos
	 */
	public List<DtoInconsistenciasArchivoPlano> getListaInconsitenciasCampos() {
		return listaInconsitenciasCampos;
	}

	/**
	 * @param listaInconsitenciasCampos the listaInconsitenciasCampos to set
	 */
	public void setListaInconsitenciasCampos(
			List<DtoInconsistenciasArchivoPlano> listaInconsitenciasCampos) {
		this.listaInconsitenciasCampos = listaInconsitenciasCampos;
	}

	/**
	 * @return the listaInconsistenciasCarguePrevio
	 */
	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsistenciasCarguePrevio() {
		return listaInconsistenciasCarguePrevio;
	}

	/**
	 * @param listaInconsistenciasCarguePrevio the listaInconsistenciasCarguePrevio to set
	 */
	public void setListaInconsistenciasCarguePrevio(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsistenciasCarguePrevio) {
		this.listaInconsistenciasCarguePrevio = listaInconsistenciasCarguePrevio;
	}

	/**
	 * @return the listaInconsitenciasPersonas
	 */
	public ArrayList<DtoInconsistenciasArchivoPlano> getListaInconsitenciasPersonas() {
		return listaInconsitenciasPersonas;
	}

	/**
	 * @param listaInconsitenciasPersonas the listaInconsitenciasPersonas to set
	 */
	public void setListaInconsitenciasPersonas(
			ArrayList<DtoInconsistenciasArchivoPlano> listaInconsitenciasPersonas) {
		this.listaInconsitenciasPersonas = listaInconsitenciasPersonas;
	}

	
}
