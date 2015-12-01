package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;


/**
 * Clase para el manejo de la parametrizacion de contratos de entidades subcontratadas
 * Date: 2009-01-26
 * @author jfhernandez@princetonsa.com
 */
@SuppressWarnings("serial")
public class ConsultarContratosEntidadesSubcontratadasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Hashmap para guardar los filtros para realizar la inserción de cotnratos
	 */
	private HashMap<String, Object> filtros;
	
	/**
	 * ArrayList para guardar el listado de la sentidades subcontratadas 
	 */
	private ArrayList<HashMap<String, Object>> entidades;
	
	/**
	 * ArrayList para guardar el listado de los inventarios
	 */
	private ArrayList<HashMap<String, Object>> inventarios;
	
	/**
	 * ArrayList para guardar el listado de los inventarios
	 */
	private ArrayList<HashMap<String, Object>> grupoServicio;
	
	/**
	 * ArrayList para guardar el listado de los esuqemas tarifarios de servicios
	 */
	private ArrayList<HashMap<String, Object>> esquemas;
	
	/**
	 * ArrayList para guardar el listado de los esuqemas tarifarios de procedimientos
	 */
	private ArrayList<HashMap<String, Object>> esquemasProcedimientos;
	
	/**
	 * HashMap para Guardar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadInv;
	
	/**
	 * HashMap para Guardar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadServ;
	
	/**
	 * HashMap para guardar los resultados d ela búsqueda avanzada 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap resultadosBusqueda;
	
	/**
	 * Indice para gaurdar la posicion de un registro a mostrar el detalle
	 */
	private int indice;
	
	/**
	 * HashMap para guardar el detalle de una glosa en el listado
	 */
	@SuppressWarnings("rawtypes")
	private HashMap detalleContrato;
	
	/**
	 * Variable que almacena el tipo de tarifa (tarifa propia o tarifa convenio persona)
	 */
	private String tipoTarifa;
	
	/**
	 * filtro para mostrar Esquemas Tarifarios de acuerdo al tipoTarifa=TarifaPropia, seleccionado. 
	 */
	private String mostrarFiltroTarifa;
	
	@SuppressWarnings("unchecked")
	public void reset()
	{
		this.estado="";
		this.filtros = new HashMap<String, Object>();
		this.filtros.put("numRegistros", "0");
		this.entidades = new ArrayList<HashMap<String,Object>>();
		this.inventarios = new ArrayList<HashMap<String,Object>>();
		this.esquemas = new ArrayList<HashMap<String,Object>>();
		this.grupoServicio = new ArrayList<HashMap<String,Object>>();
		this.esquemasProcedimientos = new ArrayList<HashMap<String,Object>>();
		this.infoEntidadInv = new HashMap<String, Object>();
		this.infoEntidadInv.put("numRegistros", "0");
		this.infoEntidadServ = new HashMap<String, Object>();
		this.infoEntidadServ.put("numRegistros", "0");
		this.resultadosBusqueda = new HashMap<String, Object>();
		this.resultadosBusqueda.put("numRegistros", "0");
		this.indice=0;
		this.detalleContrato = new HashMap<String, Object>();
		this.detalleContrato.put("numRegistros", "0");
		this.mostrarFiltroTarifa=ConstantesBD.codigoNuncaValido+"";
	}
	
	@SuppressWarnings("unused")
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        boolean banderaErrores=false;
        
        if (this.estado.equals("buscarContratos"))
        {
    
    		if (UtilidadTexto.isEmpty(this.filtros.get("fechaini").toString())&&!UtilidadTexto.isEmpty(this.filtros.get("fechafin").toString()))
    		{
    			errores.add(this.filtros.get("fechaini").toString(), new ActionMessage("errors.required","La Fecha Inicial del Contrato "));
    			banderaErrores=true;
    		}
    		
    		if (!UtilidadTexto.isEmpty(this.filtros.get("fechaini").toString())&&UtilidadTexto.isEmpty(this.filtros.get("fechafin").toString()))
    		{
    			errores.add(this.filtros.get("fechaini").toString(), new ActionMessage("errors.required","La Fecha Final del Contrato "));
    			banderaErrores=true;
    		}
    		
    		if (!UtilidadTexto.isEmpty(this.filtros.get("fechaini").toString())&&!UtilidadTexto.isEmpty(this.filtros.get("fechafin").toString()))
    		{
	    		if(!UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechaini").toString()))
    			{
    				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial del contrato "+this.filtros.get("fechaini").toString()));
    				banderaErrores=true;
    			}
	    		if(!UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechafin").toString()))
    			{
    				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final del contrato "+this.filtros.get("fechafin").toString()));
    				banderaErrores=true;
    			}	
	    		if (UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechafin").toString())&&UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechaini").toString()))
	    		{
    				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.filtros.get("fechafin").toString(),this.filtros.get("fechaini").toString()))
	    			{
	    				errores.add("", new ActionMessage("errors.fechaFinalPosteriorInicial", "Fecha Final del contrato "+this.filtros.get("fechafin").toString(),"Fecha Inicial del contrato "+this.filtros.get("fechaini").toString()));
	    				banderaErrores=true;
	    			}
	    		}
    		}
    		//VALIDA QUE SOLO CONSULTE LAS FECHAS DE LOS ESQUEMAS SOLO CUANDO EL TIPOtARIFA SEA IGUAL A TP=TARIFAPROPIA
    		if(filtros.get("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
    		{
    			if (!UtilidadTexto.isEmpty(this.infoEntidadInv.get("fechavigclaseinv").toString()))
    			{
    				if(!UtilidadFecha.esFechaValidaSegunAp(this.infoEntidadInv.get("fechavigclaseinv").toString()))
    				{
    					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+this.infoEntidadInv.get("fechavigclaseinv").toString()));
    					banderaErrores=true;
    				}
    			}
    		
    			if (!UtilidadTexto.isEmpty(this.infoEntidadServ.get("fechvigproc").toString()))
    			{			
    				if(!UtilidadFecha.esFechaValidaSegunAp(this.infoEntidadServ.get("fechvigproc").toString()))
    				{
    					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de servicios "+this.infoEntidadServ.get("fechvigproc").toString()));
    					banderaErrores=true;
    				}
    			}
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


	public HashMap<String, Object> getFiltros() {
		return filtros;
	}


	public void setFiltros(HashMap<String, Object> filtros) {
		this.filtros = filtros;
	}


	public ArrayList<HashMap<String, Object>> getEntidades() {
		return entidades;
	}


	public void setEntidades(ArrayList<HashMap<String, Object>> entidades) {
		this.entidades = entidades;
	}


	public ArrayList<HashMap<String, Object>> getInventarios() {
		return inventarios;
	}


	public void setInventarios(ArrayList<HashMap<String, Object>> inventarios) {
		this.inventarios = inventarios;
	}


	public ArrayList<HashMap<String, Object>> getGrupoServicio() {
		return grupoServicio;
	}


	public void setGrupoServicio(ArrayList<HashMap<String, Object>> grupoServicio) {
		this.grupoServicio = grupoServicio;
	}


	public ArrayList<HashMap<String, Object>> getEsquemas() {
		return esquemas;
	}


	public void setEsquemas(ArrayList<HashMap<String, Object>> esquemas) {
		this.esquemas = esquemas;
	}


	public ArrayList<HashMap<String, Object>> getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}


	public void setEsquemasProcedimientos(
			ArrayList<HashMap<String, Object>> esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadInv() {
		return infoEntidadInv;
	}


	@SuppressWarnings("rawtypes")
	public void setInfoEntidadInv(HashMap infoEntidadInv) {
		this.infoEntidadInv = infoEntidadInv;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadServ() {
		return infoEntidadServ;
	}


	@SuppressWarnings("rawtypes")
	public void setInfoEntidadServ(HashMap infoEntidadServ) {
		this.infoEntidadServ = infoEntidadServ;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getResultadosBusqueda() {
		return resultadosBusqueda;
	}


	@SuppressWarnings("rawtypes")
	public void setResultadosBusqueda(HashMap resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}

	public int getIndice() {
		return indice;
	}


	public void setIndice(int indice) {
		this.indice = indice;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getDetalleContrato() {
		return detalleContrato;
	}


	@SuppressWarnings("rawtypes")
	public void setDetalleContrato(HashMap detalleContrato) {
		this.detalleContrato = detalleContrato;
	}
	
	/**
	 * @return the filtros
	 */
	public Object getFiltros(String llave) {
		return filtros.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setFiltros(String llave, Object obj) {
		this.filtros.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadInv(String llave) {
		return infoEntidadInv.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadInv(String llave, Object obj) {
		this.infoEntidadInv.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadServ(String llave) {
		return infoEntidadServ.get(llave);
	}

	/**
	 * @param filtros the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadServ(String llave, Object obj) {
		this.infoEntidadServ.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getResultadosBusqueda(String llave) {
		return resultadosBusqueda.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setResultadosBusqueda(String llave, Object obj) {
		this.resultadosBusqueda.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getDetalleContrato(String llave) {
		return detalleContrato.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setDetalleContrato(String llave, Object obj) {
		this.detalleContrato.put(llave, obj);
	}

	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	public String getTipoTarifa() {
		return tipoTarifa;
	}

	public void setMostrarFiltroTarifa(String mostrarFiltroTarifa) {
		this.mostrarFiltroTarifa = mostrarFiltroTarifa;
	}

	public String getMostrarFiltroTarifa() {
		return mostrarFiltroTarifa;
	}
}