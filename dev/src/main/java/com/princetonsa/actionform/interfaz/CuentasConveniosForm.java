package com.princetonsa.actionform.interfaz;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;

public class CuentasConveniosForm extends ActionForm
{
    /**
     * Estado actual dentro del flujo de la funcionalidad
     */ 
    private String estado;
    
    /**
     * Acronimo del regimen seleccionado para establecer las cuentas
     */
    private String acronimoRegimen;
    
    private String nombreRegimen;

    private String codigoConvenio;
    
    private String nombreConvenio;
	
    private HashMap cuentas;
	
	private int cantidadCuentas;
	
	private String logModificacion;
	
	private String mensaje;
	
    /**
     * @return Retorna  estado.
     */
    public String getEstado()
    {
        return estado;
    }

    /**
     * @param estado asigna estado.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
    public String getMensaje()
    {
    	return this.mensaje;
    }
    
    public void setMensaje(String mensaje)
    {
    	this.mensaje=mensaje;
    }
    
    public String getLogModificacion()
    {
    	return this.logModificacion;
    }
    
    public void setLogModificacion(String logModificacion)
    {
    	this.logModificacion=logModificacion;
    }
    
    /**
     * Retorna el regimen seleccionado
     * @return
     */
    public String getAcronimoRegimen()
    {
    	return this.acronimoRegimen;
    }
    
    public void setRegimen(String regimen)
    {
    	if(UtilidadCadena.noEsVacio(regimen))
    	{
    		String tempo[] = regimen.split("-");
    		this.setAcronimoRegimen(tempo[0]);
    		this.setNombreRegimen(tempo[1]);
    	}
    	else
    	{
    		this.setAcronimoRegimen("");
    		this.setNombreRegimen("");
    	}
    }
    
    public String getRegimen()
    {
    	if(UtilidadCadena.noEsVacio(this.getAcronimoRegimen()) && UtilidadCadena.noEsVacio(this.getNombreRegimen()))
    		return this.getAcronimoRegimen()+"-"+this.getNombreRegimen();
    	else
    		return "";
    }
    
    /**
     * Establece el regimen seleccionado
     */
    public void setAcronimoRegimen(String acronimoRegimen)
    {
    	this.acronimoRegimen = acronimoRegimen;
    }

    public String getNombreRegimen()
    {
    	return this.nombreRegimen;
    }
    
    public void setNombreRegimen(String nombreRegimen)
    {
    	this.nombreRegimen = nombreRegimen;
    }

    public String getCodigoConvenio()
    {
    	return this.codigoConvenio;
    }
    
    public void setCodigoConvenio(String codigoConvenio)
    {
    	this.codigoConvenio = codigoConvenio;
    }

    public String getNombreConvenio()
    {
    	return this.nombreConvenio;
    }
    
    public void setNombreConvenio(String nombreConvenio)
    {
    	this.nombreConvenio = nombreConvenio;
    }

	public int getCantidadCuentas()
	{
		return this.cantidadCuentas;
	}
	
	public void setCantidadCuentas(int cantidadCuentas)
	{
		this.cantidadCuentas = cantidadCuentas;
	}
	
	public void setCuentas(HashMap cuentas)
	{
		this.cuentas = cuentas;
	}
	
	public HashMap getCuentas()
	{
		return this.cuentas;
	}
	
	public void setCuenta(String key, Object value)
	{
		this.cuentas.put(key, value);
	}
	
	public Object getCuenta(String key)
	{
		return this.cuentas.get(key);
	}
	
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        return errores;
    }

    public void clean()
    {
        this.estado="empezar"; // el estado por defecto es empezar
        this.acronimoRegimen="";
        this.nombreRegimen="";
        this.codigoConvenio="";
        this.nombreConvenio="";
        this.cuentas = new HashMap();
        this.cantidadCuentas=-1;
		this.logModificacion="";
		this.mensaje="";
    }
}
