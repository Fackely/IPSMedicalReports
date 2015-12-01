/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;
import util.UtilidadFecha;

public class AprobacionAjustesForm extends ActionForm {

	/**
	 * Para Mantener el Flujo de La Funcionalidad
	 */
	private String estado; 

	/**
	 * Mapa para almacenar la información.
	 */
	private HashMap mapa; 

	/**
	 * Mapa para almacenar el Listado de los Ajustes, producto de la Consulta.
	 */
	private HashMap mapaAjuste; 
	
	/**
	 * Mapa para almacenar el Listado de cargues relacionados con una Cuenta de Cobro.
	 */
	private HashMap mapaCargue; 

	/**
	 * Variable para la consulta. Se almacenara el tipo de Ajuste ( Debito -- Credito. )
	 */
	private int tipoAjuste;
	
	
	/**
	 * Variable para almacenar El  nroAjuste.
	 */
	private int nroAjuste;
	
	/**
	 * Variable para almacenar El  nroConvenio.
	 */
	private int nroConvenio;

	
	/**
	 * Variable para almacenar La Fecha de Ajuste. 
	 */
	private String fechaAjuste;
	
	/**
	 * Variable para almacenar el numero nroCuentaCobro. 
	 */
	private int nroCuentaCobro;

    /**
     * Variable para manejar la navegacion desde el paginador.
     */
    private String linkSiguiente;

    
    //--------Información de Detalle del Ajuste.
    /**
     * fecha de aprobacion del ajuste. 
     */
    private String fechaAprobacionAjuste;
	
    /**
     * Variable para almacenar el codigo de la cuenta de Cobro Seleccionada. 
     */
    private int codigoCuentaCobroSel;
    
    /**
     * Variable para almacenar el estado del Ajuste. (En la Busqueda Avanzada de la Funcionalidad De Consulta de Ajustes).s
     */
    private int estadoAjuste; 
    
    /**
     * Variable para almacenar el numero de Ajuste. (En la Busqueda Avanzada de la Funcionalidad De Consulta de Ajustes).s
     */
    private int nroAjusteFinal;
    
    /**
     * Variable para almacenar el codigo del ajuste.
     */
    private int codigoConcepto; 
    
    
    /**
     * Variable para almacenar fecha del cierre mensual  
     */
    private String fechaCierreInicial;
    
    /**
     * Variables para Ordenar el Listado de ajustes.
     */
    private String patronOrdenar;
    private String ultimoPatron;
    
	/**
	 * Limpiar e inicializar la informacion de la funcionalidad.
	 *
	 */
	public void reset()
	{
		//-- De La Busqueda Avanzada
		this.nroAjuste = 0;
		this.nroConvenio = 0;
		this.nroCuentaCobro = 0;
		this.fechaAjuste = "";
		this.tipoAjuste = 0;

		this.estadoAjuste = 0;
		this.nroAjusteFinal = 0;
		
		//-- El Codigo del Convenio Seleccionado.
		this.codigoCuentaCobroSel = 0;
		this.codigoConcepto = 0;
		this.fechaAprobacionAjuste = "";

		//-- Para validar la aprobacion
		this.fechaCierreInicial = "";
		
		this.mapa = new HashMap();
		this.mapaAjuste = new HashMap();
		this.mapaCargue = new HashMap();
	}
	
	
	/**
	 * Para validar la informacion del formulario. 
	 */
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("buscar"))
		{
			/*if ( this.nroAjuste == 0 )
			{
				errores.add("nroAjuste", new ActionMessage("errors.required"," El Numero del Ajuste"));
			}
			if ( this.tipoAjuste == 0 )
			{
				errores.add("tipoAjuste", new ActionMessage("errors.required"," El Tipo de Ajuste"));
			}*/
		}
		else if(estado.equals("empezarContinuarServicio"))
		{
			
		}
		else if(estado.equals("aprobarAjuste"))
		{
			//-Validar la fecha de aprobacion del ajuste.
			if(this.fechaAprobacionAjuste.trim().equals(""))
			{	
					errores.add("Campo Fecha Aprobacion", new ActionMessage("errors.required","El Campo Fecha Aprobación Ajuste"));
			}
			else
			{
				//------Validar la fecha del ajuste.-	
				if(!UtilidadFecha.validarFecha(this.fechaAprobacionAjuste))
				{
						errores.add("Fecha Aprobacion", new ActionMessage("errors.formatoFechaInvalido", " Aprobación Ajuste "));							
				}
				else
				{
					//-- Validar que sea menor o igual que la del sistema.
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaAprobacionAjuste)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Aprobación Ajuste", "Actual"));
					}

					//-- Validar que sea mayor igual fecha ajuste. 
					String fechaAjuste = UtilidadFecha.conversionFormatoFechaAAp(this.mapaAjuste.get("d_fecha_ajuste_0")+"");
					
					if( UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaAprobacionAjuste,fechaAjuste) )
					{
						errores.add("fecha", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Aprobación Ajuste", "Generación Ajuste"));
					}
					
					//-- Verificar que exista la fecha de cierre inicial
					if ( UtilidadCadena.noEsVacio(this.fechaCierreInicial) )
					{
						String []cadA =  this.fechaAprobacionAjuste.split("/");
						String []cadC =  this.fechaCierreInicial.split("-");
						int mesA = 0, anioA = 0, mesC = 0, anioC = 0;
						
						mesA = Integer.parseInt(cadA [1]);	anioA = Integer.parseInt(cadA [2]);
						mesC = Integer.parseInt(cadC [1]);	anioC = Integer.parseInt(cadC [0]);
						
						
						//-- Validar que la fecha de ajuste sea mayor que la fecha Cierre inicial de Cartera.
						if( 
							(anioA < anioC) ||
							( (anioA == anioC) && (mesC >= mesA) ) 
						  )
						{
							errores.add("fechaCierreInicial", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", " Aprobación Ajuste ", " Fecha Cierre Inicial Cartera "));
						}
					}
					
				}
			}		
			
		}

		return errores;
	}

	//----------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------GET'S Y SET'S-----------------------------------------------
	//----------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa() {
		return this.mapa;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}


	/**
	 * @return Retorna tipoAjuste.
	 */
	public int getTipoAjuste() {
		return tipoAjuste;
	}


	/**
	 * @param Asigna tipoAjuste.
	 */
	public void setTipoAjuste(int tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}


	/**
	 * @return Retorna fechaAjuste.
	 */
	public String getFechaAjuste() {
		return fechaAjuste;
	}


	/**
	 * @param Asigna fechaAjuste.
	 */
	public void setFechaAjuste(String fechaAjuste) {
		this.fechaAjuste = fechaAjuste;
	}


	/**
	 * @return Retorna nroAjuste.
	 */
	public int getNroAjuste() {
		return nroAjuste;
	}


	/**
	 * @param Asigna nroAjuste.
	 */
	public void setNroAjuste(int nroAjuste) {
		this.nroAjuste = nroAjuste;
	}


	/**
	 * @return Retorna nroConvenio.
	 */
	public int getNroConvenio() {
		return nroConvenio;
	}


	/**
	 * @param Asigna nroConvenio.
	 */
	public void setNroConvenio(int nroConvenio) {
		this.nroConvenio = nroConvenio;
	}


	/**
	 * @return Retorna nroCuentaCobro.
	 */
	public int getNroCuentaCobro() {
		return nroCuentaCobro;
	}


	/**
	 * @param Asigna nroCuentaCobro.
	 */
	public void setNroCuentaCobro(int nroCuentaCobro) {
		this.nroCuentaCobro = nroCuentaCobro;
	}


	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaAjuste() {
		return this.mapaAjuste;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaAjuste(HashMap mapa) {
		this.mapaAjuste = mapa;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaAjuste(String key) {
		return this.mapaAjuste.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaAjuste(String key, String valor) 
	{
		this.mapaAjuste.put(key, valor); 
	}


	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * @param Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * @return Retorna fechaAprobacionAjuste.
	 */
	public String getFechaAprobacionAjuste() {
		return fechaAprobacionAjuste;
	}


	/**
	 * @param Asigna fechaAprobacionAjuste.
	 */
	public void setFechaAprobacionAjuste(String fechaAprobacionAjuste) {
		this.fechaAprobacionAjuste = fechaAprobacionAjuste;
	}


	/**
	 * @return Retorna mapaCargue.
	 */
	public HashMap getMapaCargue() {
		return mapaCargue;
	}


	/**
	 * @param Asigna mapaCargue.
	 */
	public void setMapaCargue(HashMap mapaCargue) {
		this.mapaCargue = mapaCargue;
	}

	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaCargue(String key) {
		return this.mapaCargue.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaCargue(String key, String valor) 
	{
		this.mapaCargue.put(key, valor); 
	}


	/**
	 * @return Retorna codigoCuentaCobroSel.
	 */
	public int getCodigoCuentaCobroSel() {
		return codigoCuentaCobroSel;
	}


	/**
	 * @param Asigna codigoCuentaCobroSel.
	 */
	public void setCodigoCuentaCobroSel(int codigoCuentaCobroSel) {
		this.codigoCuentaCobroSel = codigoCuentaCobroSel;
	}


	/**
	 * @return Retorna estadoAjuste.
	 */
	public int getEstadoAjuste() {
		return estadoAjuste;
	}


	/**
	 * @param Asigna estadoAjuste.
	 */
	public void setEstadoAjuste(int estadoAjuste) {
		this.estadoAjuste = estadoAjuste;
	}


	/**
	 * @return Retorna nroAjusteFinal.
	 */
	public int getNroAjusteFinal() {
		return nroAjusteFinal;
	}


	/**
	 * @param Asigna nroAjusteFinal.
	 */
	public void setNroAjusteFinal(int nroAjusteFinal) {
		this.nroAjusteFinal = nroAjusteFinal;
	}


	/**
	 * @return Retorna codigoConcepto.
	 */
	public int getCodigoConcepto() {
		return codigoConcepto;
	}


	/**
	 * @param Asigna codigoConcepto.
	 */
	public void setCodigoConcepto(int codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}


	/**
	 * @return Retorna fechaCierreInicial.
	 */
	public String getFechaCierreInicial() {
		return fechaCierreInicial;
	}


	/**
	 * @param Asigna fechaCierreInicial.
	 */
	public void setFechaCierreInicial(String fechaCierreInicial) {
		this.fechaCierreInicial = fechaCierreInicial;
	}


	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return Retorna ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param Asigna ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
}
