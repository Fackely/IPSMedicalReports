/*
 *May 08, 2006 
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * @author sgomez
 *Forma para el workflow de las funcionalidades de
 *Consulta Cuentas por Rangos y Consulta Cuentas por Paciente
 */
public class ConsultaCuentasForm extends ValidatorForm 
{
	/**
	 * Estado de la aplicacion
	 */
	private String estado;
	
	/**
	 * Código de la institucion
	 */
	private String institucion;
	
	//*********ATRIBUTOS PARA LA BUSQUEDA POR RANGOS*****************
	/**
	 * Fecha Inicial de Apertura Cuenta
	 */
	private String fechaInicial;
	/**
	 * Fecha Final de Apertura Cuenta
	 */
	private String fechaFinal;
	/**
	 * Nº de Cuenta Inicial
	 */
	private String cuentaInicial;
	/**
	 * Nº de Cuenta Final
	 */
	private String cuentaFinal;
	/**
	 * Código Estado de la cuenta
	 */
	private int estadoCuenta;
	/**
	 * Usuario que creó la cuenta
	 */
	private String usuario;
	/**
	 * Código de la vía de Ingreso
	 */
	private int viaIngreso;
	/**
	 * Código del convenio
	 */
	private int convenio;
	/**
	 * Tipo evento de la cuenta
	 */
	private String codigoTipoEvento;
	
	/**
	 * Listado de Entidades SubContratadas
	 * */
	private ArrayList entidadesSubList;
	
	/**
	 * codigo de la entidad subcontratada
	 * */
	private String codigoEntidadSub;
	
	/**
	 * Tipo paciente Map select
	 */
	private HashMap tipoPacienteMap;
	
	/**
	 * Tipo paciente Valor
	 */
	private String tipoPaciente;
	
	
	
	//****************************************************************
	
	//********ATRIBUTOS DEL LISTADO CUENTAS**************************
	/**
	 * listado de cuentas
	 */
	private HashMap listado;
	/**
	 * Número de  cuentas del listado
	 */
	private int numRegistros;
	
	/**
	 * offset del pager
	 */
	private int offset;
	/**
	 * Indice de ordenacion
	 */
	private String indice;
	/**
	 * Último indice de ordenacion
	 */
	private String ultimoIndice;
	/**
	 * Link Siguiente del pager
	 */
	private String linkSiguiente;
	/**
	 * Id de la cuenta del detalle
	 */
	private int idCuenta;
	
	/**
	 * Código del paciente
	 */
	private String codigoPaciente;
	
	/**
	 * Variable donde se va editando el seguimiento de la ordenación
	 */
	private String registroOrdenacion;
	/**
	 * maxPageItems
	 */
	private int maxPageItems;
	//**************************************************************
	//*******ATRIBUTOS DEL DETALLE DE LA CUENTA**********************
	/**
	 * Mapa donde se almacena el detalle de la cuenta
	 */
	HashMap detalle = new HashMap();
	/**
	 * Mapa donde se almacena los N convenios de la cuenta
	 */
	HashMap variosConvenios = new HashMap();
	/**
	 * Mapa donde se almacena la verificacion de derechos del convenio principal
	 */
	HashMap verificacion = new HashMap();
	
	/**
	 * Mapa donde se almacena los datos del responsable de la cuenta
	 */
	HashMap responsable = new HashMap();
	//****************************************************************
	
	/**
	 * Variable para almacenar el centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Método que inicializa los datos de la forma
	 */
	public void reset()
	{
		this.estado = "";
		this.institucion = "";
		//**ATRIBUTOS DE LA BUSQUEDA POR RANGOS*****
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.cuentaInicial = "";
		this.cuentaFinal = "";
		this.estadoCuenta = -2;
		this.usuario = "";
		this.viaIngreso = 0;
		this.convenio = 0;
		this.codigoTipoEvento = "";
		this.entidadesSubList = new ArrayList();
		this.codigoEntidadSub = "";
		this.tipoPaciente="";
		this.tipoPacienteMap= new HashMap();
		//*******************************************
		//**ATRIBUTOS DEL LISTADO DE CUENTAS**********
		this.listado = new HashMap();
		this.numRegistros = 0;
		this.offset = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.idCuenta = 0;
		this.codigoPaciente = "";
		this.maxPageItems = 0;
		this.registroOrdenacion = "";
		//*******************************************
		//**ATRIBUTOS DEL DETALLE DE LA CUENTA*******
		this.detalle = new HashMap();
		this.variosConvenios = new HashMap();
		this.verificacion = new HashMap();
		this.responsable = new HashMap();
		//********************************************
		
		//-Varible para la consulta por rangos 
		this.centroAtencion = 0;
	}
	
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
		
		if(this.estado.equals("consultarRangos"))
		{
			//Validacion rango fechas
			if(!this.fechaInicial.equals("")&&!this.fechaFinal.equals(""))
			{
				if(UtilidadFecha.validarFecha(this.fechaInicial)&&UtilidadFecha.validarFecha(this.fechaFinal))
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
						errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
						errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
						errores.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
					else if(UtilidadFecha.numeroMesesEntreFechas(this.fechaInicial,this.fechaFinal,true)>3)
						errores.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","PARA CONSULTA DE CUENTAS"));
				}
				else
				{
					if(!UtilidadFecha.validarFecha(this.fechaInicial))
						errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
					if(!UtilidadFecha.validarFecha(this.fechaFinal))
						errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
					
				}
			}
			else
			{
				if(this.fechaInicial.equals(""))
					errores.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
				else if(!UtilidadFecha.validarFecha(this.fechaInicial))
					errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
					errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				
				if(this.fechaFinal.equals(""))
					errores.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
				else if(!UtilidadFecha.validarFecha(this.fechaFinal))
					errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
					errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
			}
			
			//validacion rango cuentas
			if(!this.cuentaInicial.equals("")&&!this.cuentaFinal.equals(""))
			{
				int cuentaI = Integer.parseInt(this.cuentaInicial);
				int cuentaF = Integer.parseInt(this.cuentaFinal);
				
				if(cuentaI>cuentaF)
					errores.add("cuenta inicial > a cuenta final",new ActionMessage("errors.MenorIgualQue","La Cuenta Inicial","Cuenta Final"));
			}
			else
			{
				if(this.cuentaInicial.equals("")&&!this.cuentaFinal.equals(""))
					errores.add("Cuenta Inicial requerida",new ActionMessage("errors.required","La Cuenta Inicial"));
				if(this.cuentaFinal.equals("")&&!this.cuentaInicial.equals(""))
					errores.add("Cuenta Final requerida",new ActionMessage("errors.required","La Cuenta Final"));
				
			}
			
			if(this.viaIngreso>0&&this.tipoPaciente.equals("-1"))
			{
				errores.add("Tipo Paciente ",new ActionMessage("errors.required"," Tipo Paciente "));
			}

			
			
		}
		
		return errores;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return Returns the cuentaFinal.
	 */
	public String getCuentaFinal() {
		return cuentaFinal;
	}

	/**
	 * @param cuentaFinal The cuentaFinal to set.
	 */
	public void setCuentaFinal(String cuentaFinal) {
		this.cuentaFinal = cuentaFinal;
	}

	/**
	 * @return Returns the cuentaInicial.
	 */
	public String getCuentaInicial() {
		return cuentaInicial;
	}

	/**
	 * @param cuentaInicial The cuentaInicial to set.
	 */
	public void setCuentaInicial(String cuentaInicial) {
		this.cuentaInicial = cuentaInicial;
	}

	/**
	 * @return Returns the estadoCuenta.
	 */
	public int getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta The estadoCuenta to set.
	 */
	public void setEstadoCuenta(int estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return Returns the viaIngreso.
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return Returns the listado.
	 */
	public HashMap getListado() {
		return listado;
	}

	/**
	 * @param listado The listado to set.
	 */
	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	/**
	 * @return Retorna un elemento del mapa listado.
	 */
	public Object getListado(String key) {
		return listado.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa listado .
	 */
	public void setListado(String key,Object obj) {
		this.listado.put(key,obj);
	}

	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Returns the detalle.
	 */
	public HashMap getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle The detalle to set.
	 */
	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}
	
	/**
	 * @return Retorna un elemento del mapa detalle.
	 */
	public Object getDetalle(String key) {
		return detalle.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa detalle.
	 */
	public void setDetalle(String key,Object obj) {
		this.detalle.put(key,obj);
	}

	/**
	 * @return Returns the registroOrdenacion.
	 */
	public String getRegistroOrdenacion() {
		return registroOrdenacion;
	}

	/**
	 * @param registroOrdenacion The registroOrdenacion to set.
	 */
	public void setRegistroOrdenacion(String registroOrdenacion) {
		this.registroOrdenacion = registroOrdenacion;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the responsable
	 */
	public HashMap getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(HashMap responsable) {
		this.responsable = responsable;
	}
	
	/**
	 * @return elemento del mapa responsable
	 */
	public Object getResponsable(String key) {
		return responsable.get(key);
	}

	/**
	 * @param Asigna elemento al mapa responsable 
	 */
	public void setResponsable(String key,Object obj) {
		this.responsable.put(key,obj);
	}

	/**
	 * @return the variosConvenios
	 */
	public HashMap getVariosConvenios() {
		return variosConvenios;
	}

	/**
	 * @param variosConvenios the variosConvenios to set
	 */
	public void setVariosConvenios(HashMap variosConvenios) {
		this.variosConvenios = variosConvenios;
	}
	
	/**
	 * @return Elemento del mapa variosConvenios
	 */
	public Object getVariosConvenios(String key) {
		return variosConvenios.get(key);
	}

	/**
	 * @param Asigna elemento al mapa variosConvenios 
	 */
	public void setVariosConvenios(String key,Object obj) {
		this.variosConvenios.put(key,obj);
	}

	/**
	 * @return the verificacion
	 */
	public HashMap getVerificacion() {
		return verificacion;
	}

	/**
	 * @param verificacion the verificacion to set
	 */
	public void setVerificacion(HashMap verificacion) {
		this.verificacion = verificacion;
	}
	
	/**
	 * @return retorna elemento del mapa verificacion
	 */
	public Object getVerificacion(String key) {
		return verificacion.get(key);
	}

	/**
	 * @param Asigna elemento al mapa verificacion 
	 */
	public void setVerificacion(String key,Object obj) {
		this.verificacion.put(key,obj);
	}

	/**
	 * @return the codigoTipoEvento
	 */
	public String getCodigoTipoEvento() {
		return codigoTipoEvento;
	}

	/**
	 * @param codigoTipoEvento the codigoTipoEvento to set
	 */
	public void setCodigoTipoEvento(String codigoTipoEvento) {
		this.codigoTipoEvento = codigoTipoEvento;
	}

	/**
	 * @return the entidadesSubList
	 */
	public ArrayList getEntidadesSubList() {
		return entidadesSubList;
	}

	/**
	 * @param entidadesSubList the entidadesSubList to set
	 */
	public void setEntidadesSubList(ArrayList entidadesSubList) {
		this.entidadesSubList = entidadesSubList;
	}

	/**
	 * @return the codigoEntidadSub
	 */
	public String getCodigoEntidadSub() {
		return codigoEntidadSub;
	}

	/**
	 * @param codigoEntidadSub the codigoEntidadSub to set
	 */
	public void setCodigoEntidadSub(String codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return the tipoPacienteMap
	 */
	public HashMap getTipoPacienteMap() {
		return tipoPacienteMap;
	}

	/**
	 * @param tipoPacienteMap the tipoPacienteMap to set
	 */
	public void setTipoPacienteMap(HashMap tipoPacienteMap) {
		this.tipoPacienteMap = tipoPacienteMap;
	}


}
