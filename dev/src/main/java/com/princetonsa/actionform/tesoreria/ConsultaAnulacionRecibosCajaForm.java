
/*
 * Creado   24/03/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;



import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;
import util.laboratorios.InterfazLaboratorios;

/**
 *
 * @version 1.0, 24/03/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaAnulacionRecibosCajaForm extends ActionForm 
{
    
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ConsultaAnulacionRecibosCajaForm.class);
	
	/**
     * Variable para manejar el estado de la funcionalidad 
     */
	
	private String estado;
    
    private String numAnulacionInicial;
    
    private String numAnulacionFinal;
    
    private String fechaAnulacionInicial;
    
    private String fechaAnulacionFinal;
    
    private String motivoAnulacion;
    
    private String descMotivoAnulacion;
    
    private String usuarioAnulacion;
    
    private String descUsuarioAnulacion;
    
    private String numReciboCaja;
    
    private HashMap mapaAnulacionRC;
    
    private String patronOrdenar;
    
    private int indice;
    
    private String ultimoPatron;
    
    private int codigoCentroAtencion;
    
    //inicializar variables
    public void reset()
    {
       this.numAnulacionInicial="";
       this.numAnulacionFinal="";
       this.fechaAnulacionInicial="";
       this.fechaAnulacionFinal="";
       this.motivoAnulacion=ConstantesBD.codigoNuncaValido+"";
       this.usuarioAnulacion=ConstantesBD.codigoNuncaValido+"";
       this.numReciboCaja="";
       this.mapaAnulacionRC=new HashMap();
       this.patronOrdenar="";
       this.indice=ConstantesBD.codigoNuncaValido;
       this.ultimoPatron="";
       this.descMotivoAnulacion="";
       this.descUsuarioAnulacion="";
       this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
    }
    /*
     * validate
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        if(this.estado.equals("realizarBusqueda"))
        {
        	if(this.numAnulacionInicial.equals("") && this.numAnulacionFinal.equals(""))
        	{
        		logger.info(">>> Los número de anulación vienen vacíos");
        	}
        	else
        	{
        		if(this.numAnulacionInicial.equals("") && !this.numAnulacionFinal.equals(""))
        		{
        			errores.add("numeroAnulacionInicialVacio", new ActionMessage("errors.requeridoElOtro","Número Anulación Final", "Número Anulación Inicial"));
        			return errores;
        		}
        	
        		if(this.numAnulacionFinal.equals("") && !this.numAnulacionInicial.equals(""))
        		{
        			errores.add("numeroAnulacionFinalVacio", new ActionMessage("errors.requeridoElOtro","Número Anulación Inicial", "Número Anulación Inicial"));
        			return errores;
        		}
        		if(!this.numAnulacionInicial.equals("") && !this.numAnulacionFinal.equals(""))
        		{
        			try {
                		int anulaFinal = 0, anulaInic = 0;
                		anulaFinal = Integer.parseInt(this.numAnulacionFinal.trim());
        				anulaInic = Integer.parseInt(this.numAnulacionInicial.trim());
        			}
                	catch (NumberFormatException e)
                	{
                		logger.info(">>> Entramos al catch, error = "+e);
                		errores.add("",new ActionMessage("errors.notEspecific","Número de Anulación inválido, tiene mas de 10 caracteres, por favor verifique"));
                		return errores;
        			}
        		}
        	}
        	
            if( 
                    this.numAnulacionInicial.trim().equals("") && this.numAnulacionFinal.trim().equals("") && 
                    this.fechaAnulacionInicial.trim().equals("") && this.fechaAnulacionFinal.trim().equals("") &&
                    this.motivoAnulacion.equals(ConstantesBD.codigoNuncaValido+"") && this.usuarioAnulacion.equals(ConstantesBD.codigoNuncaValido+"") &&
                    this.numReciboCaja.equals("")
              )
            {
                errores.add("Fecha Requerido",new ActionMessage("errors.minimoCampos","dos parámetros","la busqueda"));
            }            
            if(!this.numAnulacionFinal.trim().equals("") && !this.numAnulacionInicial.trim().equals(""))
            {           
                if(Integer.parseInt(this.numAnulacionFinal.trim())<Integer.parseInt(this.numAnulacionInicial.trim()))
                {
                    errores.add("numero anulacion final menor",new ActionMessage("errors.debeSerNumeroMayor","El No. Anulación Final "+this.numAnulacionFinal,"El No. Anulación Inicial "+this.numAnulacionInicial));
                }
            }
            if(!this.numAnulacionFinal.trim().equals(""))
            {
            	if(this.numAnulacionInicial.trim().equals(""))
            		errores.add("fecha", new ActionMessage("errors.requeridoElOtro","número anulación final", "número anulación inicial"));
            }
            if(!this.numAnulacionInicial.trim().equals(""))
            {
	            if(this.numAnulacionFinal.trim().equals(""))
	        		errores.add("fecha", new ActionMessage("errors.requeridoElOtro","número anulación inicial", "número anulación final"));
            }    
            
            if(!this.fechaAnulacionInicial.trim().equals(""))
            {             
                if(!UtilidadFecha.validarFecha(this.fechaAnulacionInicial))
                {
                    errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaAnulacionInicial));
                }
                else
                {
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAnulacionInicial,UtilidadFecha.getFechaActual()))
                    {
                        errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Inicial ("+this.fechaAnulacionInicial+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
                    }
                }
                
                if(this.fechaAnulacionFinal.trim().equals(""))
                {
                	errores.add("fecha", new ActionMessage("errors.requeridoElOtro","la fecha Inicial", "la fecha final"));
                }
            }
            if(!this.fechaAnulacionFinal.trim().equals(""))
            {           
                if(!UtilidadFecha.validarFecha(this.fechaAnulacionFinal))
                {
                    errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaAnulacionFinal));
                }
                else
                {
                    if(UtilidadFecha.validarFecha(this.fechaAnulacionInicial))
                    {
                        if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaAnulacionFinal,this.fechaAnulacionInicial))
                        {
                            errores.add("Fecha Final mayor a fecha Inicial",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Final ("+this.fechaAnulacionFinal+")","Inicial ("+this.fechaAnulacionInicial+")"));
                        }
                    }
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaAnulacionFinal,UtilidadFecha.getFechaActual()))
                    {
                        errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Final ("+this.fechaAnulacionFinal+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
                    }
                }
                if(this.fechaAnulacionInicial.trim().equals(""))
                {
                	errores.add("fecha", new ActionMessage("errors.requeridoElOtro","la fecha final", "la fecha inicial"));
                }
            }
            //si no existen errores entonces se evalua que el rango de busqueda no supere los 3 meses
		    if(errores.isEmpty())
	        {
		    	if(!this.fechaAnulacionFinal.trim().equals(""))
	            {
		    		if(!this.fechaAnulacionInicial.trim().equals(""))
		            {
			    		int numeroMesesDiferencia=UtilidadFecha.numeroMesesEntreFechas(this.getFechaAnulacionInicial(), this.getFechaAnulacionFinal(),true);
			        	if(numeroMesesDiferencia>3)
			        	{
			        		errores.add("", new ActionMessage("errors.rangoMayorTresMeses", "PARA CONSULTAR ANULACION RECIBOS CAJA"));
			        	}
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
     * @return Retorna fechaAnulacionFinal.
     */
    public String getFechaAnulacionFinal() {
        return fechaAnulacionFinal;
    }
    /**
     * @param fechaAnulacionFinal Asigna fechaAnulacionFinal.
     */
    public void setFechaAnulacionFinal(String fechaAnulacionFinal) {
        this.fechaAnulacionFinal = fechaAnulacionFinal;
    }
    /**
     * @return Retorna fechaAnulacionInicial.
     */
    public String getFechaAnulacionInicial() {
        return fechaAnulacionInicial;
    }
    /**
     * @param fechaAnulacionInicial Asigna fechaAnulacionInicial.
     */
    public void setFechaAnulacionInicial(String fechaAnulacionInicial) {
        this.fechaAnulacionInicial = fechaAnulacionInicial;
    }
    /**
     * @return Retorna motivoAnulacion.
     */
    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion Asigna motivoAnulacion.
     */
    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
    /**
     * @return Retorna numAnulacionFinal.
     */
    public String getNumAnulacionFinal() {
        return numAnulacionFinal;
    }
    /**
     * @param numAnulacionFinal Asigna numAnulacionFinal.
     */
    public void setNumAnulacionFinal(String numAnulacionFinal) {
        this.numAnulacionFinal = numAnulacionFinal;
    }
    /**
     * @return Retorna numAnulacionInicial.
     */
    public String getNumAnulacionInicial() {
        return numAnulacionInicial;
    }
    /**
     * @param numAnulacionInicial Asigna numAnulacionInicial.
     */
    public void setNumAnulacionInicial(String numAnulacionInicial) {
        this.numAnulacionInicial = numAnulacionInicial;
    }
    /**
     * @return Retorna numReciboCaja.
     */
    public String getNumReciboCaja() {
        return numReciboCaja;
    }
    /**
     * @param numReciboCaja Asigna numReciboCaja.
     */
    public void setNumReciboCaja(String numReciboCaja) {
        this.numReciboCaja = numReciboCaja;
    }
    /**
     * @return Retorna usuarioAnulacion.
     */
    public String getUsuarioAnulacion() {
        return usuarioAnulacion;
    }
    /**
     * @param usuarioAnulacion Asigna usuarioAnulacion.
     */
    public void setUsuarioAnulacion(String usuarioAnulacion) {
        this.usuarioAnulacion = usuarioAnulacion;
    }
    /**
     * @return Retorna mapaAnulacionRC.
     */
    public HashMap getMapaAnulacionRC() {
        return mapaAnulacionRC;
    }
    /**
     * @param mapaAnulacionRC Asigna mapaAnulacionRC.
     */
    public void setMapaAnulacionRC(HashMap mapaAnulacionRC) {
        this.mapaAnulacionRC = mapaAnulacionRC;
    }
    /**
     * @return Retorna mapaAnulacionRC.
     */
    public Object getMapaAnulacionRC(String key) {
        return mapaAnulacionRC.get(key);
    }
    /**
     * @param mapaAnulacionRC Asigna mapaAnulacionRC.
     */
    public void setMapaAnulacionRC(String key,Object value) {
        this.mapaAnulacionRC.put(key, value);
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
     * @return Retorna indice.
     */
    public int getIndice() {
        return indice;
    }
    /**
     * @param indice Asigna indice.
     */
    public void setIndice(int indice) {
        this.indice = indice;
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
     * @return Retorna descMotivoAnulacion.
     */
    public String getDescMotivoAnulacion() {
        return descMotivoAnulacion;
    }
    /**
     * @param descMotivoAnulacion Asigna descMotivoAnulacion.
     */
    public void setDescMotivoAnulacion(String descMotivoAnulacion) {
        this.descMotivoAnulacion = descMotivoAnulacion;
    }
    /**
     * @return Retorna descUsuarioAnulacion.
     */
    public String getDescUsuarioAnulacion() {
        return descUsuarioAnulacion;
    }
    /**
     * @param descUsuarioAnulacion Asigna descUsuarioAnulacion.
     */
    public void setDescUsuarioAnulacion(String descUsuarioAnulacion) {
        this.descUsuarioAnulacion = descUsuarioAnulacion;
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
    
}
