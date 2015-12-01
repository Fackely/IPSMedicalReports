/*
 * Creado   08/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */

package com.princetonsa.actionform.capitacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.mundo.capitacion.CierresCarteraCapitacion;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos 
 * para interactuar con cierres de  cartera capitacion
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * @version 1.0
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class CierresCarteraCapitacionForm extends ActionForm  
{
	 /**
     * estado del workflow
     */
    private String estado;
    
    /**
     * año del cierre
     */
    private String yearCierre;
    
    /**
     * mes del cierre
     */
    private String mesCierre;
    
    /**
     * observaciones del cierre
     */
    private String observaciones;
    
    /**
     * fecha de generacion de el cierre
     */
    private String fechaGeneracion;
    
    /**
     * hora de generacion del cierre
     */
    private String horaGeneracion;
    
    /**
     * usuario que gero el cierre
     */
    private String usuario;
    
    /**
     * codigoInstitucion
     */
    private int codigoInstitucion;
    
    /**
     * en caso de que quede con "no" entonces
	 * se muestra el popup de confimacion continuar proceso 
	 * "No existen cuentas cobro radicadas por lo tanto no existe informacion para saldo inicial, se genera en CERO, Continua S/N
     */
    private String centinelaExisteSaldoInicial;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {
    	this.yearCierre = "";
    	this.mesCierre = "";
    	this.observaciones = "";
    	this.fechaGeneracion="";
    	this.horaGeneracion="";
    	this.usuario="";
    	this.centinelaExisteSaldoInicial="si";
    	this.codigoInstitucion=-1;
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
		final Date fechaActual = new Date();
	    Date fechaCorte=null;
	    final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
	    String fecha = this.getMesCierre()+"/"+this.getYearCierre();
	    
		if(this.estado.equals("guardarCierreInicial") || this.estado.equals("guardarCierreInicialSaldoEnCero"))
		{		   
		    try 
		    {
                fechaCorte = dateFormatter.parse(fecha);
                if(fechaCorte.compareTo(fechaActual) > 0)
                {
                    errores.add("fecha corte", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Cierre", "Actual - VERIFICAR EN PARAMETROS"));  
                }                
            } catch (ParseException e) 
            {
                errores.add("fechaCorte", new ActionMessage("errors.formatoFechaInvalido", "de Corte Saldo Inicial"));                
                e.printStackTrace();
            }
    	}
		
		else if(this.estado.equals("guardarCierreAnual"))
		{
			try 
		    {
				String yearActual = UtilidadFecha.getFechaActual().substring(6);
				int yearActualInt= Integer.parseInt(yearActual);
				int yearFormaInt=Integer.parseInt(this.getYearCierre());
				if(yearFormaInt>=yearActualInt)
				{
					errores.add("fecha corte", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Cierre "+yearFormaInt, "Actual "+yearActual));
				}
		    } catch (Exception e) 
            {
                errores.add("fechaCorte", new ActionMessage("errors.formatoAnioInvalido", "de Cierre"));                
                e.printStackTrace();
            }
		    if(errores.isEmpty())
		    {
		    	if(CierresCarteraCapitacion.existeCierre(this.yearCierre, ConstantesBD.codigoTipoCierreAnualStr, this.codigoInstitucion))
		    	{
		    		errores.add("", new ActionMessage("error.cierresCarteraCapitacion.anioYaBD", this.yearCierre));
		    	}
		    	else
		    	{
		    		//verificar que el anio para el cual se esta ejecutando el cierre anual tenga registrados las cxc en estado radicada
		    		
		    		HashMap mapaCxCNoRadicadas= CierresCarteraCapitacion.estanCxCRadicadasParaCierreAnual(this.yearCierre, this.codigoInstitucion);
		    		String cxcNoRadicadas="";
		    		if(mapaCxCNoRadicadas.containsKey("numRegistros"))
		    		{
		    			for(int w=0; w<Integer.parseInt(mapaCxCNoRadicadas.get("numRegistros").toString()); w++)
		    			{
		    				cxcNoRadicadas+=mapaCxCNoRadicadas.get("numerocuentacobro_"+w);
		    				if(w!=(Integer.parseInt(mapaCxCNoRadicadas.get("numRegistros").toString())-1))
		    					cxcNoRadicadas+=",";
		    			}
		    		}
		    		if(!cxcNoRadicadas.equals(""))
		    		{	
		    			errores.add("", new ActionMessage("error.cierresCarteraCapitacion.cxcNoRadicadas", cxcNoRadicadas, this.yearCierre));
		    		}
		    		
		    		//verificar que exista el cierre de saldos iniciales para el anio es que se esta realizando cierre o que exista
		    		// cierre anual para el anio inmediatamanete anteriror
		    		
		    		if(!CierresCarteraCapitacion.existeCierre(this.yearCierre, ConstantesBD.codigoTipoCierreSaldoInicialStr, this.codigoInstitucion))
		    		{
		    			if(!CierresCarteraCapitacion.existeCierre((Integer.parseInt(this.yearCierre)-1)+"", ConstantesBD.codigoTipoCierreAnualStr, this.codigoInstitucion))
		    				errores.add("", new ActionMessage("error.cierresCarteraCapitacion.noExisteCierre", this.yearCierre, (Integer.parseInt(this.yearCierre)-1)+""));
		    		}
		    	}
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
	 * @return Returns the mesCierre.
	 */
	public String getMesCierre() {
		return mesCierre;
	}

	/**
	 * @param mesCierre The mesCierre to set.
	 */
	public void setMesCierre(String mesCierre) {
		this.mesCierre = mesCierre;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the yearCierre.
	 */
	public String getYearCierre() {
		return yearCierre;
	}

	/**
	 * @param yearCierre The yearCierre to set.
	 */
	public void setYearCierre(String yearCierre) {
		this.yearCierre = yearCierre;
	}

	/**
	 * @return Returns the fechaGeneracion.
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	/**
	 * @param fechaGeneracion The fechaGeneracion to set.
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	/**
	 * @return Returns the horaGeneracion.
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	/**
	 * @param horaGeneracion The horaGeneracion to set.
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
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
	 * @return Returns the centinelaExisteSaldoInicial.
	 */
	public String getCentinelaExisteSaldoInicial() {
		return centinelaExisteSaldoInicial;
	}

	/**
	 * @param centinelaExisteSaldoInicial The centinelaExisteSaldoInicial to set.
	 */
	public void setCentinelaExisteSaldoInicial(String centinelaExisteSaldoInicial) {
		this.centinelaExisteSaldoInicial = centinelaExisteSaldoInicial;
	}

	/**
	 * @return Returns the codigoInstitucion.
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion The codigoInstitucion to set.
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
}
