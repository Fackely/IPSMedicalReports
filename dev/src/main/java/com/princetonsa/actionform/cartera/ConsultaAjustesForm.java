/*
 * Creado   9/09/2005
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
 * @version 1.0, 9/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaAjustesForm extends ActionForm 
{
    /**
     * estado del workflow
     */
    private String estado;
    /**
     * código del tipo de ajuste
     */
    private String codTipoAjuste;
    /**
     * código del convenio
     */
    private String codConvenio;
    /**
     * código del estado
     */
    private String codEstado;
    /**
     * fecha de elaboracion inicial
     */
    private String fechaInicial;
    /**
     * fecha final de elaboración
     */
    private String fechaFinal;
    /**
     * código del concepto del ajuste
     */
    private String codConceptoAjuste;
    /**
     * número del ajuste, rango inicial
     */
    private String numAjusteI;
    /**
     * número del ajuste, rango final
     */
    private String numAjusteF;
    /**
     * número de la factura
     */
    private String numFactura;
    /**
     * mapa con los resultados de las consultas
     */
    private HashMap mapAjustes;
    /**
     * código del ajuste para ver su
     * detalle de facturas correspondiente
     */    
    private double codigoVerDetalle;
    /**
     * mapa con el resultado de la consulta
     * del detalle de ajustes
     */
    private HashMap mapDetalleAjustes;
    /**
     * numero del registro selccionado
     * para ver el detalle
     */
    private int regSel;
    /**
     * numero del registro seleccionado
     * para ver el detalle de la factura
     */
    private int regSelFact;    
    /**
     * patron por el cual se ordena
     */
    private String patronOrdenar;
    /**
     * ultimo patron por el cual se ordeno
     */
    private String ultimoPatron;
    
    private String tipoOrdenamiento;
    /**
     * posicion en el mapa detalle facturas
     * para el registro seleccionado
     */
    private int regSelDetFact;
    
    /**
     * metodo para limpiar e inicializar
     * atributos.
     *
     */
    public void reset()
    {
     this.codTipoAjuste="";
     this.codConvenio="";
     this.codEstado="";
     this.fechaFinal="";
     this.fechaInicial="";
     this.codConceptoAjuste="";
     this.numAjusteI="";
     this.numAjusteF="";
     this.numFactura="";
     this.mapAjustes=new HashMap ();
     this.codigoVerDetalle=ConstantesBD.codigoNuncaValidoDouble;
     this.mapDetalleAjustes=new HashMap ();
     this.regSel=ConstantesBD.codigoNuncaValido;
     this.regSelFact=ConstantesBD.codigoNuncaValido;
     this.patronOrdenar="";
     this.ultimoPatron="";
     this.tipoOrdenamiento="";
     this.regSelDetFact=ConstantesBD.codigoNuncaValido;
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

			
			if(this.fechaFinal.trim().equals(""))
	    		this.fechaFinal=this.fechaInicial;
	    	if(this.fechaInicial.trim().equals("") && !this.fechaFinal.trim().equals(""))
	    		this.fechaInicial=this.fechaFinal;

	    	if(this.codTipoAjuste.equals("-1") && this.codConvenio.equals("-1") && this.codEstado.equals("-1") && this.fechaFinal.equals("") && 
		            this.fechaInicial.equals("") && this.codConceptoAjuste.equals("-1") && this.numAjusteF.equals("") && this.numAjusteI.equals("")
		            && this.numFactura.equals(""))
		    {
		        errores.add("faltan campos", new ActionMessage("error.cartera.ajustes.minimoUnCampoParaConsulta"));
		    }
		    else 
		    {
			    if(!this.fechaInicial.equals(""))
			    {
				    if(!UtilidadFecha.validarFecha(this.fechaInicial))
				    {
				       errores.add("fecha invalida", new ActionMessage("errors.formatoFechaInvalido","Inicial"+this.fechaInicial));
				    }  
				    else
				    {
				        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
					    {
					        errores.add("fecha invalida", new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaInicial," actual "+UtilidadFecha.getFechaActual())); 
					    }
				    }
			    }
			    else if(this.codTipoAjuste.equals("-1"))
			    {
			        errores.add("fecha requerida", new ActionMessage("errors.required","La Fecha Inicial"));
			    }
			    if(!this.fechaFinal.equals(""))
			    {
				    if(!UtilidadFecha.validarFecha(this.fechaFinal))
				    {
				       errores.add("fecha invalida", new ActionMessage("errors.formatoFechaInvalido","Final"+this.fechaFinal));
				    } 
				    else
				    {
				        if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
					    {
					        errores.add("fecha invalida", new ActionMessage("errors.fechaAnteriorIgualActual"," Final"+this.fechaFinal," Inicial"+this.fechaInicial)); 
					    } 
				        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
					    {
					        errores.add("fecha invalida", new ActionMessage("errors.fechaPosteriorIgualActual"," Final"+this.fechaFinal," actual "+UtilidadFecha.getFechaActual())); 
					    }
				    }
			    }
			    else if(this.codTipoAjuste.equals("-1"))
			    {
			        errores.add("fecha requerida", new ActionMessage("errors.required","La Fecha Final"));
			    }			    
			    if(!this.codTipoAjuste.equals("-1")&&!this.numAjusteF.equals("")&&this.numAjusteI.equals(""))
			    {
			        errores.add("fecha invalida", new ActionMessage("errors.requeridoElOtro"," el Ajuste Final"+this.numAjusteF," el ajuste Inicial" ));
			    }
			    if(this.codTipoAjuste.equals("-1")&&(!this.numAjusteF.equals("")||!this.numAjusteI.equals("")))
			    {
			        errores.add("fecha invalida", new ActionMessage("errors.requeridoElOtro"," un Número de Ajuste"," el Tipo de Ajuste" ));
			    }
			    if(!this.codTipoAjuste.equals("-1")&&!this.numAjusteI.equals("")&&this.numAjusteF.equals(""))
			    {
			        errores.add("fecha invalida", new ActionMessage("errors.requeridoElOtro"," el Número de Ajuste Inicial"," el Núemro de Ajuste Final" ));
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
     * @return Retorna codTipoAjuste.
     */
    public String getCodTipoAjuste() {
        return codTipoAjuste;
    }
    /**
     * @param codTipoAjuste Asigna codTipoAjuste.
     */
    public void setCodTipoAjuste(String codTipoAjuste) {
        this.codTipoAjuste = codTipoAjuste;
    }
    /**
     * @return Retorna codConvenio.
     */
    public String getCodConvenio() {
        return codConvenio;
    }
    /**
     * @param codConvenio Asigna codConvenio.
     */
    public void setCodConvenio(String codConvenio) {
        this.codConvenio = codConvenio;
    }
    /**
     * @return Retorna codEstado.
     */
    public String getCodEstado() {
        return codEstado;
    }
    /**
     * @param codEstado Asigna codEstado.
     */
    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }
    /**
     * @return Retorna fechaFinal.
     */
    public String getFechaFinal() {
        return fechaFinal;
    }
    /**
     * @param fechaFinal Asigna fechaFinal.
     */
    public void setFechaFinal(String fechaFinal) {
    	this.fechaFinal = fechaFinal;
    }
    /**
     * @return Retorna fechaInicial.
     */
    public String getFechaInicial() {
        return fechaInicial;
    }
    /**
     * @param fechaInicial Asigna fechaInicial.
     */
    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    /**
     * @return Retorna codConceptoAjuste.
     */
    public String getCodConceptoAjuste() {
        return codConceptoAjuste;
    }
    /**
     * @param codConceptoAjuste Asigna codConceptoAjuste.
     */
    public void setCodConceptoAjuste(String codConceptoAjuste) {
        this.codConceptoAjuste = codConceptoAjuste;
    }
    /**
     * @return Retorna numAjusteF.
     */
    public String getNumAjusteF() {
        return numAjusteF;
    }
    /**
     * @param numAjusteF Asigna numAjusteF.
     */
    public void setNumAjusteF(String numAjusteF) {
        this.numAjusteF = numAjusteF;
    }
    /**
     * @return Retorna numAjusteI.
     */
    public String getNumAjusteI() {
        return numAjusteI;
    }
    /**
     * @param numAjusteI Asigna numAjusteI.
     */
    public void setNumAjusteI(String numAjusteI) {
        this.numAjusteI = numAjusteI;
    }
    /**
     * @return Retorna numFactura.
     */
    public String getNumFactura() {
        return numFactura;
    }
    /**
     * @param numFactura Asigna numFactura.
     */
    public void setNumFactura(String numFactura) {
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
     * @return Retorna codigoVerDetalle.
     */
    public double getCodigoVerDetalle() {
        return codigoVerDetalle;
    }
    /**
     * @param codigoVerDetalle Asigna codigoVerDetalle.
     */
    public void setCodigoVerDetalle(double codigoVerDetalle) {
        this.codigoVerDetalle = codigoVerDetalle;
    }
    /**
     * @return Retorna mapDetalleAjustes.
     */
    public HashMap getMapDetalleAjustes() {
        return mapDetalleAjustes;
    }
    /**
     * @param mapDetalleAjustes Asigna mapDetalleAjustes.
     */
    public void setMapDetalleAjustes(HashMap mapDetalleAjustes) {
        this.mapDetalleAjustes = mapDetalleAjustes;
    }
    /**
     * @return Retorna mapDetalleAjustes.
     */
    public Object getMapDetalleAjustes(String key) {
        return mapDetalleAjustes.get(key);
    }
    /**
     * @param mapDetalleAjustes Asigna mapDetalleAjustes.
     */
    public void setMapDetalleAjustes(String key,Object value) {
        this.mapDetalleAjustes.put(key,value);
    }
    /**
     * @return Retorna regSel.
     */
    public int getRegSel() {
        return regSel;
    }
    /**
     * @param regSel Asigna regSel.
     */
    public void setRegSel(int regSel) {
        this.regSel = regSel;
    }
    /**
     * @return Retorna regSelFact.
     */
    public int getRegSelFact() {
        return regSelFact;
    }
    /**
     * @param regSelFact Asigna regSelFact.
     */
    public void setRegSelFact(int regSelFact) {
        this.regSelFact = regSelFact;
    }
    /**
     * @return Retorna patronOrdenar.
     */
    public String getPatronOrdenar() {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
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
     * @param ultimoPatron Asigna ultimoPatron.
     */
    public void setUltimoPatron(String ultimoPatron) {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Retorna tipoOrdenamiento.
     */
    public String getTipoOrdenamiento() {
        return tipoOrdenamiento;
    }
    /**
     * @param tipoOrdenamiento Asigna tipoOrdenamiento.
     */
    public void setTipoOrdenamiento(String tipoOrdenamiento) {
        this.tipoOrdenamiento = tipoOrdenamiento;
    }

    /**
     * @return Retorna regSelDetFact.
     */
    public int getRegSelDetFact() {
        return regSelDetFact;
    }

    /**
     * @param regSelDetFact Asigna regSelDetFact.
     */
    public void setRegSelDetFact(int regSelDetFact) {
        this.regSelDetFact = regSelDetFact;
    }
}
