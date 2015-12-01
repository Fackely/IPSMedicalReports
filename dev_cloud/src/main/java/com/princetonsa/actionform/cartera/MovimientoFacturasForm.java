/*
 * @(#)MovimientoFacturasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cartera;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos para generar 
 * la consulta de movimiento facturas
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Abril 3, 2004
 * @author wrios 
 */
public class MovimientoFacturasForm extends ValidatorForm
{
    /**
     * codigo de la empresa
     */
    private int codigoEmpresa;

    /**
     * Desc empresa (razon social)
     */
    private String descripcionEmpresa;
    
    /**
     * cod convenio
     */
    private int codigoConvenio;
    
    /**
     * nombre convenio
     */
    private String nombreConvenio;
    
    /**
     * Rango inicial de la factura
     */
    private String facturaRangoInicial;
    
    /**
     * Rango final de la factura
     */
    private String facturaRangoFinal;
    
    /**
     * codigo del estado de la factura
     */
    private int codigoEstadoFactura;
    
    /**
     * Rango inicial de la fecha
     */
    private String fechaRangoInicial;
    
    /**
     * Rango final de la fecha
     */
    private String fechaRangoFinal;
    
    /**
     * Rango inicial del valor (valor Cartera) factura
     */
    private String valorFacturaRangoInicial;
    
    /**
     * Rango final del valor (valor Cartera) factura
     */
    private String valorFacturaRangoFinal;

    /**
     * Rango inicial del numero de cuenta de cobro
     */
    private String numeroCuentaCobroRangoInicial;
    
    /**
     * Rango final del numero de cuenta cobro 
     */
    private String numeroCuentaCobroRangoFinal;
    
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Accion en un estado especifico
	 */
	private String accion;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	
	///////////////////////////atributos para el detalle//////////////////////////////////
	/**
	 * consecutivo de la factura
	 */
	private String consecutivoFactura;
	
	/**
	 * codigoFactura interno
	 */
	private int codigoFactura;

	///////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * codigo del centro dwe atencion
	 */
	private int codigoCentroAtencion;
	
	/**
	 * 
	 */
	private String empresaInstitucion;
	
	/**
	 * 
	 */
	private ArrayList<DtoGlosa> glosas;
	
	/**
	 * 
	 */
	private ArrayList<DtoDetalleFacturaGlosa> detFacturaGlosa;
	
	/**
	 * 
	 */
	private String codigoGlosa="";
	
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
		if(estado.equals("resultadoBusquedaAvanzada"))
		{
		    boolean existioError=false;
		    
		    errores=super.validate(mapping,request);
			if(		this.getDescripcionEmpresa().equals("") 
			        && this.getNombreConvenio().equals("") 
			        && this.getFacturaRangoInicial().equals("") && this.getFacturaRangoFinal().equals("")
			        && this.getCodigoEstadoFactura()<1 
			        && this.getFechaRangoInicial().equals("") && this.getFechaRangoFinal().equals("")
			        && this.getValorFacturaRangoInicial().equals("") && this.getValorFacturaRangoFinal().equals("")
			        && this.getNumeroCuentaCobroRangoInicial().equals("") && this.getNumeroCuentaCobroRangoFinal().equals("") &&this.getEmpresaInstitucion().equals(""))
			{
			    errores.add("errors.minimoCampos", new ActionMessage("errors.minimoCampos","dos campos","búsqueda"));
			}
			else
			{
			    ///////////////////validaciones consecutivo factura
			    if(this.getFacturaRangoInicial().equals(""))
			    {
			        if(!this.getFacturaRangoFinal().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Final de la Factura","el Rango Inicial"));
			            try
			            {
			                Integer.parseInt(this.getFacturaRangoFinal());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de la Factura"));
			            }
			        }
			    }
			    if(this.getFacturaRangoFinal().equals(""))
			    {
			        if(!this.getFacturaRangoInicial().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Inicial de la Factura","el Rango Final"));
			            try
			            {
			                Integer.parseInt(this.getFacturaRangoInicial());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de la Factura"));
			            }
			        }
			    }
			    if(!this.getFacturaRangoInicial().equals("") && !this.getFacturaRangoFinal().equals(""))
			    {
			        existioError=false;
			        int tempInicial=0, tempFinal=0;
			        try
		            {
		                tempInicial=Integer.parseInt(this.getFacturaRangoInicial());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de la Factura"));
		                existioError=true;
		            }
		            try
		            {
		                tempFinal=Integer.parseInt(this.getFacturaRangoFinal());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de la Factura"));
		                existioError=true;
		            }
		            if(!existioError)
		            {
		                if(tempInicial>tempFinal)
		                {
		                    errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El Rango Final de la Factura", "El Rango Inicial de la Factura = "+tempInicial));
		                }
		                if(tempInicial<=0)
		                {
		                    errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue", "El Rango Inicial de la Factura ", "0"));
		                }
		                if(tempFinal<=0)
		                {
		                    errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue", "El Rango Final de la Factura ", "0"));
		                }
		            }
			    }
			    //////////////////////aqui va las validaciones de fecha
			    
			    if(this.getFechaRangoInicial().equals(""))
			    {
			        if(!this.getFechaRangoFinal().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Final de la Fecha","el Rango Inicial"));
			            
			            try
			            {
			                if(!UtilidadFecha.validarFecha(this.getFechaRangoFinal()))
			                    errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Final"));
			            }
			            catch(Exception e)
			            {
			                errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Final"));
			            }    
			        }
			    }
			    if(this.getFechaRangoFinal().equals(""))
			    {
			        if(!this.getFechaRangoInicial().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Inicial de la Fecha","el Rango Final"));
			            
			            try
			            {
			                if(!UtilidadFecha.validarFecha(this.getFechaRangoInicial()))
			                    errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Inicial"));
			            }
			            catch(Exception e)
			            {
			                errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Inicial"));
			            }    
			        }
			    }
			    
			    if(!this.getFechaRangoInicial().equals("") && !this.getFechaRangoFinal().equals(""))
			    {
			        existioError=false;
			        try
		            {
		                if(!UtilidadFecha.validarFecha(this.getFechaRangoInicial()))
		                {    
		                    errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Inicial"));
		                    existioError=true;
		                }    
		            }
		            catch(Exception e)
		            {
		                errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Inicial"));
		                existioError=true;
		            }    
		            try
		            {
		                if(!UtilidadFecha.validarFecha(this.getFechaRangoFinal()))
		                {    
		                    errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Final"));
		                    existioError=true;
		                }    
		            }
		            catch(Exception e)
		            {
		                errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido","Rango Final"));
		                existioError=true;
		            }  
		            if(!existioError)
		            {
		                existioError=UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaRangoFinal(), this.getFechaRangoInicial());
		                if(existioError)
		                {
		                    errores.add("errors.fechaAnteriorAOtraDeReferencia",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Rango Final", "Rango Inicial"));
		                }
		            }
		            if(!existioError)
		            {
		            	if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaRangoInicial(), UtilidadFecha.getFechaActual()))
		                {
		                	errores.add("La fecha inicial debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "actual"));
		                }
		            	if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaRangoFinal(), UtilidadFecha.getFechaActual()))
		                {
		                	errores.add("La fecha final debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final", "actual"));
		                }
		            }
			    }
			    
			    /////////////////////validaciones del valor factura
			    if(this.getValorFacturaRangoInicial().equals(""))
			    {
			        if(!this.getValorFacturaRangoFinal().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Final del Valor Factura","el Rango Inicial"));
			            try
			            {
			                Double.parseDouble(this.getValorFacturaRangoFinal());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.float", new ActionMessage("errors.float","El Rango Final del Valor Factura"));
			            }
			        }
			    }
			    if(this.getValorFacturaRangoFinal().equals(""))
			    {
			        if(!this.getValorFacturaRangoInicial().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Inicial del Valor Factura","el Rango Final"));
			            try
			            {
			                Double.parseDouble(this.getValorFacturaRangoInicial());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.float", new ActionMessage("errors.float","El Rango Inicial del Valor Factura"));
			            }
			        }
			    }
			    if(!this.getValorFacturaRangoInicial().equals("") && !this.getValorFacturaRangoFinal().equals(""))
			    {
			        existioError=false;
			        double tempInicial=0, tempFinal=0;
			        try
		            {
		                tempInicial=Double.parseDouble(this.getValorFacturaRangoInicial());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.float", new ActionMessage("errors.float","El Rango Inicial del Valor Factura"));
		                existioError=true;
		            }
		            try
		            {
		                tempFinal=Double.parseDouble(this.getValorFacturaRangoFinal());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.float", new ActionMessage("errors.float","El Rango Final del Valor Factura"));
		                existioError=true;
		            }
		            if(!existioError)
		            {
		                if(tempInicial>tempFinal)
		                {
		                    errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El Rango Final del Valor Factura", "El Rango Inicial del Valor Factura = "+tempInicial));
		                }
		                if(tempInicial<0)
		                {
		                    errores.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "El Rango Inicial del Valor Factura ", "0"));
		                }
		                if(tempFinal<0)
		                {
		                    errores.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "El Rango Final del Valor Factura ", "0"));
		                }
		            }
			    }
			    
			    /////////////////////validaciones numero cuenta cobro
			    if(this.getNumeroCuentaCobroRangoInicial().equals(""))
			    {
			        if(!this.getNumeroCuentaCobroRangoFinal().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Final del No.Cuenta Cobro","el Rango Inicial"));
			            try
			            {
			                Double.parseDouble(this.getNumeroCuentaCobroRangoFinal());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final del No.Cuenta Cobro"));
			            }
			        }
			    }
			    if(this.getNumeroCuentaCobroRangoFinal().equals(""))
			    {
			        if(!this.getNumeroCuentaCobroRangoInicial().equals(""))
			        {
			            errores.add("errors.requeridoElOtro", new ActionMessage("errors.requeridoElOtro", "el Rango Inicial del No.Cuenta Cobro","el Rango Final"));
			            try
			            {
			                Double.parseDouble(this.getNumeroCuentaCobroRangoInicial());
			            }
			            catch(NumberFormatException e)
			            {
			                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial del No.Cuenta"));
			            }
			        }
			    }
			    if(!this.getNumeroCuentaCobroRangoInicial().equals("") && !this.getNumeroCuentaCobroRangoFinal().equals(""))
			    {
			        existioError=false;
			        double tempInicial=0, tempFinal=0;
			        try
		            {
		                tempInicial=Double.parseDouble(this.getNumeroCuentaCobroRangoInicial());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial del No.Cuenta Cobro"));
		                existioError=true;
		            }
		            try
		            {
		                tempFinal=Double.parseDouble(this.getNumeroCuentaCobroRangoFinal());
		            }
		            catch(NumberFormatException e)
		            {
		                errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final del No.Cuenta Cobro"));
		                existioError=true;
		            }
		            if(!existioError)
		            {
		                if(tempInicial>tempFinal)
		                {
		                    errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El Rango Final del No.Cuenta Cobro", "El Rango Inicial No.Cuenta Cobro = "+tempInicial));
		                }
		                if(tempInicial<=0)
		                {
		                    errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue", "El Rango Inicial del No.Cuenta Cobro ", "0"));
		                }
		                if(tempFinal<=0)
		                {
		                    errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue", "El Rango Final del No.Cuenta Cobro ", "0"));
		                }
		            }
			    }
			}
			if(!errores.isEmpty())
			    this.setEstado("empezar");
		}	
		return errores;
	}
	
	/**
	 * resetea los atributos de la forma
	 *
	 */
	public void reset()
	{
	    this.codigoEmpresa=ConstantesBD.codigoNuncaValido;
	    this.descripcionEmpresa="";
	    this.codigoConvenio=ConstantesBD.codigoNuncaValido;
	    this.nombreConvenio="";
	    this.facturaRangoInicial="";
	    this.facturaRangoFinal="";
	    this.codigoEstadoFactura=ConstantesBD.codigoNuncaValido;
	    this.fechaRangoInicial="";
	    this.fechaRangoFinal="";
	    this.valorFacturaRangoInicial="";
	    this.valorFacturaRangoFinal="";
	    this.numeroCuentaCobroRangoInicial="";
	    this.numeroCuentaCobroRangoFinal="";
	    this.accion="";
	    this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
	    this.empresaInstitucion="";
	    this.glosas = new ArrayList<DtoGlosa>();
	    this.codigoGlosa="";
	    this.detFacturaGlosa = new ArrayList<DtoDetalleFacturaGlosa>();
	}
	
	/**
	 * retorna el tamanio de la collection
	 * @return
	 */
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
    /**
     * @return Returns the codigoConvenio.
     */
    public int getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * @param codigoConvenio The codigoConvenio to set.
     */
    public void setCodigoConvenio(int codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
    /**
     * @return Returns the codigoEmpresa.
     */
    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }
    /**
     * @param codigoEmpresa The codigoEmpresa to set.
     */
    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
    /**
     * @return Returns the codigoEstadoFactura.
     */
    public int getCodigoEstadoFactura() {
        return codigoEstadoFactura;
    }
    /**
     * @param codigoEstadoFactura The codigoEstadoFactura to set.
     */
    public void setCodigoEstadoFactura(int codigoEstadoFactura) {
        this.codigoEstadoFactura = codigoEstadoFactura;
    }
    /**
     * @return Returns the col.
     */
    public Collection getCol() {
        return col;
    }
    /**
     * @param col The col to set.
     */
    public void setCol(Collection col) {
        this.col = col;
    }
    /**
     * @return Returns the descripcionEmpresa.
     */
    public String getDescripcionEmpresa() {
        return descripcionEmpresa;
    }
    /**
     * @param descripcionEmpresa The descripcionEmpresa to set.
     */
    public void setDescripcionEmpresa(String descripcionEmpresa) {
        this.descripcionEmpresa = descripcionEmpresa;
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
     * @return Returns the facturaRangoFinal.
     */
    public String getFacturaRangoFinal() {
        return facturaRangoFinal;
    }
    /**
     * @param facturaRangoFinal The facturaRangoFinal to set.
     */
    public void setFacturaRangoFinal(String facturaRangoFinal) {
        this.facturaRangoFinal = facturaRangoFinal;
    }
    /**
     * @return Returns the facturaRangoInicial.
     */
    public String getFacturaRangoInicial() {
        return facturaRangoInicial;
    }
    /**
     * @param facturaRangoInicial The facturaRangoInicial to set.
     */
    public void setFacturaRangoInicial(String facturaRangoInicial) {
        this.facturaRangoInicial = facturaRangoInicial;
    }
    /**
     * @return Returns the fechaRangoFinal.
     */
    public String getFechaRangoFinal() {
        return fechaRangoFinal;
    }
    /**
     * @param fechaRangoFinal The fechaRangoFinal to set.
     */
    public void setFechaRangoFinal(String fechaRangoFinal) {
        this.fechaRangoFinal = fechaRangoFinal;
    }
    /**
     * @return Returns the fechaRangoInicial.
     */
    public String getFechaRangoInicial() {
        return fechaRangoInicial;
    }
    /**
     * @param fechaRangoInicial The fechaRangoInicial to set.
     */
    public void setFechaRangoInicial(String fechaRangoInicial) {
        this.fechaRangoInicial = fechaRangoInicial;
    }
    /**
     * @return Returns the nombreConvenio.
     */
    public String getNombreConvenio() {
        return nombreConvenio;
    }
    /**
     * @param nombreConvenio The nombreConvenio to set.
     */
    public void setNombreConvenio(String nombreConvenio) {
        this.nombreConvenio = nombreConvenio;
    }
    /**
     * @return Returns the numeroCuentaCobroRangoFinal.
     */
    public String getNumeroCuentaCobroRangoFinal() {
        return numeroCuentaCobroRangoFinal;
    }
    /**
     * @param numeroCuentaCobroRangoFinal The numeroCuentaCobroRangoFinal to set.
     */
    public void setNumeroCuentaCobroRangoFinal(
            String numeroCuentaCobroRangoFinal) {
        this.numeroCuentaCobroRangoFinal = numeroCuentaCobroRangoFinal;
    }
    /**
     * @return Returns the numeroCuentaCobroRangoInicial.
     */
    public String getNumeroCuentaCobroRangoInicial() {
        return numeroCuentaCobroRangoInicial;
    }
    /**
     * @param numeroCuentaCobroRangoInicial The numeroCuentaCobroRangoInicial to set.
     */
    public void setNumeroCuentaCobroRangoInicial(
            String numeroCuentaCobroRangoInicial) {
        this.numeroCuentaCobroRangoInicial = numeroCuentaCobroRangoInicial;
    }
    /**
     * @return Returns the valorFacturaRangoFinal.
     */
    public String getValorFacturaRangoFinal() {
        return valorFacturaRangoFinal;
    }
    /**
     * @param valorFacturaRangoFinal The valorFacturaRangoFinal to set.
     */
    public void setValorFacturaRangoFinal(String valorFacturaRangoFinal) {
        this.valorFacturaRangoFinal = valorFacturaRangoFinal;
    }
    /**
     * @return Returns the valorFacturaRangoInicial.
     */
    public String getValorFacturaRangoInicial() {
        return valorFacturaRangoInicial;
    }
    /**
     * @param valorFacturaRangoInicial The valorFacturaRangoInicial to set.
     */
    public void setValorFacturaRangoInicial(String valorFacturaRangoInicial) {
        this.valorFacturaRangoInicial = valorFacturaRangoInicial;
    }
    /**
     * @return Returns the accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion The accion to set.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @return Returns the columna.
     */
    public String getColumna() {
        return columna;
    }
    /**
     * @param columna The columna to set.
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }
    /**
     * @return Returns the ultimaPropiedad.
     */
    public String getUltimaPropiedad() {
        return ultimaPropiedad;
    }
    /**
     * @param ultimaPropiedad The ultimaPropiedad to set.
     */
    public void setUltimaPropiedad(String ultimaPropiedad) {
        this.ultimaPropiedad = ultimaPropiedad;
    }
    /**
     * @return Returns the consecutivoFactura.
     */
    public String getConsecutivoFactura() {
        return consecutivoFactura;
    }
    /**
     * @param consecutivoFactura The consecutivoFactura to set.
     */
    public void setConsecutivoFactura(String consecutivoFactura) {
        this.consecutivoFactura = consecutivoFactura;
    }
    /**
     * @return Returns the codigoFactura.
     */
    public int getCodigoFactura() {
        return codigoFactura;
    }
    /**
     * @param codigoFactura The codigoFactura to set.
     */
    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the glosas
	 */
	public ArrayList<DtoGlosa> getGlosas() {
		return glosas;
	}

	/**
	 * @param glosas the glosas to set
	 */
	public void setGlosas(ArrayList<DtoGlosa> glosas) {
		this.glosas = glosas;
	}

	/**
	 * @return the codigoGlosa
	 */
	public String getCodigoGlosa() {
		return codigoGlosa;
	}

	/**
	 * @param codigoGlosa the codigoGlosa to set
	 */
	public void setCodigoGlosa(String codigoGlosa) {
		this.codigoGlosa = codigoGlosa;
	}

	/**
	 * @return the detFacturaGlosa
	 */
	public ArrayList<DtoDetalleFacturaGlosa> getDetFacturaGlosa() {
		return detFacturaGlosa;
	}

	/**
	 * @param detFacturaGlosa the detFacturaGlosa to set
	 */
	public void setDetFacturaGlosa(ArrayList<DtoDetalleFacturaGlosa> detFacturaGlosa) {
		this.detFacturaGlosa = detFacturaGlosa;
	}
	
	
}
