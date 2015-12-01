package com.princetonsa.actionform.facturasVarias;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class AprobacionAnulacionFacturasVariasForm extends ValidatorForm 
{
	private String estado;
	
	private ResultadoBoolean mostrarMensaje;
	
	private HashMap mapaConsulta;
	
	private String estadoFactura;
	
	private String fechaAprobAnulacion;
	
	private String motivoAnulacion;
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private String factura;
	
	private String centroAtencion;
	
	private String deudor;
	
	private String tipoDeudor;
	
	private String codigoDeudor;
	
	private String descripDeudor;
	
	/**
	 * para la navegacion del pager, cuando se ingresa
	 * un registro nuevo.
	 */
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	
	 
	 private int maxItems;
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.mapaConsulta= new HashMap();
		this.mapaConsulta.put("numRegistros","0");
		this.estadoFactura="";
		this.fechaAprobAnulacion=UtilidadFecha.getFechaActual();
		this.motivoAnulacion="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.factura="";
		this.centroAtencion="";
		this.tipoDeudor="";
		this.deudor="";
		linkSiguiente="";
       	this.maxPageItems=20;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	this.codigoDeudor = "";
    	this.descripDeudor = "";
    	this.maxItems = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 *
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
	}

	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			
			int numReg=Integer.parseInt(this.mapaConsulta.get("numRegistros")+"");
			int pagina = 0;
			for(int i=0;i<numReg;i++)
			{
				
				if( ((i+1)%this.getMaxItems())==0)
        			pagina = ((i+1)/this.getMaxItems());
        		else{
        			pagina = ((i+1)/this.getMaxItems())+1;
        		}
				
				if(this.getMapaConsulta("estado_"+i).toString().trim().equals("ANU")&&this.getMapaConsulta("motivoanulacion_"+i).toString().trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Campo Motivo de la factura No "+this.getMapaConsulta("consecutivo_"+i)));
				}
				if((this.getMapaConsulta("estado_"+i).toString().trim().equals("ANU")||this.getMapaConsulta("estado_"+i).toString().trim().equals("APR"))&&this.getMapaConsulta("fechaaprobanulacion_"+i).toString().trim().equals(""))
				{
					{
						errores.add("codigo", new ActionMessage("errors.required","La Fecha de Aprobacion/Anulacion de la factua No "+this.getMapaConsulta("consecutivo_"+i)));
					}
					
				}
				if(!UtilidadTexto.isEmpty(this.getMapaConsulta("fechaaprobanulacion_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaAprobAnulacion))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "aprobacion/anulacion "+this.fechaInicial));
						centinelaErrorFechas=true;
					}
					
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getMapaConsulta("fechaelaboracion_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(this.getMapaConsulta("fechaaprobanulacion_"+i)+"")))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "aprobacion/anulacion "+this.getMapaConsulta("fechaaprobanulacion_"+i).toString(), "Elaboracion "+UtilidadFecha.conversionFormatoFechaAAp(this.getMapaConsulta("fechaelaboracion_"+i).toString())));
						}
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia( UtilidadFecha.conversionFormatoFechaAAp(this.getMapaConsulta("fechaaprobanulacion_"+i)+""), UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "aprobacion/anulacion "+this.getMapaConsulta("fechaaprobanulacion_"+i)+"", "Actual "+UtilidadFecha.getFechaActual()));
						}
					}
				}
				
				if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.getMapaConsulta("motivoanulacion_"+i)+""))
        			errores.add("consecutivo",new ActionMessage("errors.caracteresInvalidos","El Campo Motivo [Pag - "+pagina+"] [Fila - "+(i+1)+"] "));
				
			}
			
		}
		
		if(this.estado.equals("buscar"))
		{			
			if(this.fechaInicial.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			}
			if(this.fechaFinal.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
			}			
			if(!UtilidadTexto.isEmpty(this.fechaFinal) || !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.fechaInicial, "final "+this.fechaFinal));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
				if(!centinelaErrorFechas)
				{
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=6)
					{
						errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias", "para consultar Facturas Varias"));
					}
				}				
			}			
		}
		return errores;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsulta() {
		return mapaConsulta;
	}

	/**
	 * 
	 * @param mapaConsulta
	 */
	public void setMapaConsulta(HashMap mapaConsulta) {
		this.mapaConsulta = mapaConsulta;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstadoFactura() {
		return estadoFactura;
	}

	/**
	 * 
	 * @param estadoFactura
	 */
	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaAprobAnulacion() {
		return fechaAprobAnulacion;
	}

	/**
	 * 
	 * @param fechaAprobAnulacion
	 */
	public void setFechaAprobAnulacion(String fechaAprobAnulacion) {
		this.fechaAprobAnulacion = fechaAprobAnulacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * 
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * 
	 * @return
	 */
	public String getDeudor() {
		return deudor;
	}

	/**
	 * 
	 * @param deudor
	 */
	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}

	/**
	 * 
	 * @return
	 */
	public String getFactura() {
		return factura;
	}

	/**
	 * 
	 * @param factura
	 */
	public void setFactura(String factura) {
		this.factura = factura;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * 
	 * @param tipoDeudor
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	public Object getMapaConsulta(String key) {
		return mapaConsulta.get(key);
	}

	
	
	/**
	 * 
	 * @param paquetesMap the paquetesMap to set
	 */
	
	public void setMapaConsulta(String key,Object value) {
		this.mapaConsulta.put(key, value);
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public int getMaxPageItems() {
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}

	public String getDescripDeudor() {
		return descripDeudor;
	}

	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
	}

	/**
	 * @return the maxItems
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * @param maxItems the maxItems to set
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}
}
