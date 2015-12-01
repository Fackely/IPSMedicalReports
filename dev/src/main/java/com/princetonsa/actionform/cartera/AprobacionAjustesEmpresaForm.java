
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.cartera;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos 
 * para interactuar con aprobacion ajustes empresa
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionAjustesEmpresaForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    private String accion;
    /**
     * almacena datos generales dela 
     * funcionalidad
     */
    private HashMap mapTempo;
    /**
     * código del tipo de ajuste
     */
    private int codTipoAjuste;
    /**
     * número del ajuste
     */
    private double numAjuste;
    /**
     * cosecutivo del ajuste
     */    
    private String consecutivoAjuste;
    /**
     * número de la cuenta de cobro
     */
    private double numCuentaCobro;
    /**
     * número de la factura
     */
    private double numFactura;
    /**
     * código del convenio
     */
    private int codConvenio;
    /**
     * fecha del ajuste
     */
    private String fechaAjuste;
    /**
     * almacena los datos de los ajustes
     */
    private HashMap mapAjustes;
    /**
     * login del usuario
     */
    private String usuarioAprueba;
    /**
     * fecha de la aprobacion
     */
    private String fechaAprobacion;
    /**
     * indice del registro seleccionado
     */
    private int regSeleccionado;
    /**
     * código del ajuste al cual se
     * le mostrara el detalle
     */
    private double codAjusteSeleccionado;
    
    /**
     * almacena el mensaje que se muestra
     * al usuario en caso de no encontrar 
     * resultados
     */
    private String mensajeBusqueda;
    
    /**
     * 
     */
    private boolean aplicarAjusteCuentaPagarMedico;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    { 
      this.codTipoAjuste=ConstantesBD.codigoNuncaValido;
      this.numAjuste=ConstantesBD.codigoNuncaValidoDouble;
      this.consecutivoAjuste="";
      this.numCuentaCobro=ConstantesBD.codigoNuncaValido;
      this.numFactura=ConstantesBD.codigoNuncaValido;
      this.codConvenio=ConstantesBD.codigoNuncaValido;
      this.fechaAjuste="";
      this.usuarioAprueba="";
      this.fechaAprobacion="";
      this.regSeleccionado=ConstantesBD.codigoNuncaValido;
      this.codAjusteSeleccionado=ConstantesBD.codigoNuncaValido;    
      this.aplicarAjusteCuentaPagarMedico=true;
    }
    public void resetMaps()
    {
        this.mapAjustes=new HashMap(); 
        this.mapTempo=new HashMap();
        this.mensajeBusqueda="";
    }
    
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("generarConsulta"))
		{
			if(!this.fechaAjuste.trim().equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaAjuste())){
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.getFechaAjuste()));
				}
			}
		    if(this.codTipoAjuste==ConstantesBD.codigoNuncaValido && this.consecutivoAjuste.equals("")
		       && this.numCuentaCobro==ConstantesBD.codigoNuncaValidoDouble && this.numFactura==ConstantesBD.codigoNuncaValidoDouble
		       && this.codConvenio==ConstantesBD.codigoNuncaValido && this.fechaAjuste.equals(""))
		    {
		        errores.add("falta parametro", new ActionMessage("error.cierre.minimoUnFiltroRequerido"));
		    }
		    else
		    {			    
		        if(this.codTipoAjuste!=ConstantesBD.codigoNuncaValido && this.consecutivoAjuste.equals(""))
			    {
			        errores.add("falta parametro", new ActionMessage("error.cartera.ajustes.tipoAjusteYNumeroRequerido")); 
			    }
			    if(this.codTipoAjuste==ConstantesBD.codigoNuncaValido && !this.consecutivoAjuste.equals(""))
			    {
			        errores.add("falta parametro", new ActionMessage("error.cartera.ajustes.tipoAjusteYNumeroRequerido")); 
			    }
		    }
		}
		if(this.estado.equals("generarAprobacion"))
		{
		   if(this.fechaAprobacion.equals(""))
		   {
		       errores.add("falta parametro", new ActionMessage("errors.required","La Fecha de Aprobación "));  
		   }
		   else
		   {
			   if(!UtilidadFecha.validarFecha(this.fechaAprobacion))
			   {
			       errores.add("fecha invalida", new ActionMessage("errors.formatoFechaInvalido",this.fechaAprobacion));
			   }
			   else
			   {
			    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.mapAjustes.get("fecha_elaboracion_"+this.regSeleccionado)+"",this.fechaAprobacion))
			    {
			        errores.add("fecha invalida", new ActionMessage("errors.fechaAnteriorIgualActual",this.fechaAprobacion,"de elaboración "+this.mapAjustes.get("fecha_elaboracion_"+this.regSeleccionado)+""));
			    }
			    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAprobacion,UtilidadFecha.getFechaActual()))
			    {
			        errores.add("fecha invalida", new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaAprobacion," actual "+UtilidadFecha.getFechaActual())); 
			    }
			   }
		   }
		}
		return errores;
	}
	
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }    
    /**
     * @return Retorna codConvenio.
     */
    public int getCodConvenio() {
        return codConvenio;
    }
    /**
     * @param codConvenio Asigna codConvenio.
     */
    public void setCodConvenio(int codConvenio) {
        this.codConvenio = codConvenio;
    }
    /**
     * @return Retorna codTipoAjuste.
     */
    public int getCodTipoAjuste() {
        return codTipoAjuste;
    }
    /**
     * @param codTipoAjuste Asigna codTipoAjuste.
     */
    public void setCodTipoAjuste(int codTipoAjuste) {
        this.codTipoAjuste = codTipoAjuste;
    }
    /**
     * @return Retorna fechaAjuste.
     */
    public String getFechaAjuste() {
        return fechaAjuste;
    }
    /**
     * @param fechaAjuste Asigna fechaAjuste.
     */
    public void setFechaAjuste(String fechaAjuste) {
        this.fechaAjuste = fechaAjuste;
    }
    /**
     * @return Retorna numAjuste.
     */
    public double getNumAjuste() {
        return numAjuste;
    }
    /**
     * @param numAjuste Asigna numAjuste.
     */
    public void setNumAjuste(double numAjuste) {
        this.numAjuste = numAjuste;
    }
    /**
     * @return Retorna numCuentaCobro.
     */
    public double getNumCuentaCobro() {
        return numCuentaCobro;
    }
    /**
     * @param numCuentaCobro Asigna numCuentaCobro.
     */
    public void setNumCuentaCobro(double numCuentaCobro) {
        this.numCuentaCobro = numCuentaCobro;
    }
    /**
     * @return Retorna numFactura.
     */
    public double getNumFactura() {
        return numFactura;
    }
    /**
     * @param numFactura Asigna numFactura.
     */
    public void setNumFactura(double numFactura) {
        this.numFactura = numFactura;
    }
    /**
     * @return Retorna mapAjustes.
     */
    public HashMap getMapAjustes() {
        return mapAjustes;
    }
    /**
     * @param mapAjustes Asigna mapAjustes.
     */
    public void setMapAjustes(HashMap mapAjustes) {
        this.mapAjustes = mapAjustes;
    }
    /**
     * @return Retorna mapAjustes.
     */
    public Object getMapAjustes(String key) {
        return mapAjustes.get(key);
    }
    /**
     * @param mapAjustes Asigna mapAjustes.
     */
    public void setMapAjustes(String key,Object value) {
        this.mapAjustes.put(key,value);
    }
    /**
     * @return Retorna fechaAprobacion.
     */
    public String getFechaAprobacion() {
        return fechaAprobacion;
    }
    /**
     * @param fechaAprobacion Asigna fechaAprobacion.
     */
    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }
    /**
     * @return Retorna usuarioAprueba.
     */
    public String getUsuarioAprueba() {
        return usuarioAprueba;
    }
    /**
     * @param usuarioAprueba Asigna usuarioAprueba.
     */
    public void setUsuarioAprueba(String usuarioAprueba) {
        this.usuarioAprueba = usuarioAprueba;
    }
    /**
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) {
        this.regSeleccionado = regSeleccionado;
    }
    /**
     * @return Retorna codAjusteSeleccionado.
     */
    public double getCodAjusteSeleccionado() {
        return codAjusteSeleccionado;
    }
    /**
     * @param codAjusteSeleccionado Asigna codAjusteSeleccionado.
     */
    public void setCodAjusteSeleccionado(double codAjusteSeleccionado) {
        this.codAjusteSeleccionado = codAjusteSeleccionado;
    }
    /**
     * @return Retorna mensajeBusqueda.
     */
    public String getMensajeBusqueda() {
        return mensajeBusqueda;
    }
    /**
     * @param mensajeBusqueda Asigna mensajeBusqueda.
     */
    public void setMensajeBusqueda(String mensajeBusqueda) {
        this.mensajeBusqueda = mensajeBusqueda;
    }
    /**
     * @return Retorna mapTempo.
     */
    public HashMap getMapTempo() {
        return mapTempo;
    }
    /**
     * @param mapTempo Asigna mapTempo.
     */
    public void setMapTempo(HashMap mapTempo) {
        this.mapTempo = mapTempo;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Retorna consecutivoAjuste.
     */
    public String getConsecutivoAjuste() {
        return consecutivoAjuste;
    }
    /**
     * @param consecutivoAjuste Asigna consecutivoAjuste.
     */
    public void setConsecutivoAjuste(String consecutivoAjuste) {
        this.consecutivoAjuste = consecutivoAjuste;
    }
	public boolean isAplicarAjusteCuentaPagarMedico()
	{
		return aplicarAjusteCuentaPagarMedico;
	}
	public void setAplicarAjusteCuentaPagarMedico(
			boolean aplicarAjusteCuentaPagarMedico)
	{
		this.aplicarAjusteCuentaPagarMedico = aplicarAjusteCuentaPagarMedico;
	}
}
