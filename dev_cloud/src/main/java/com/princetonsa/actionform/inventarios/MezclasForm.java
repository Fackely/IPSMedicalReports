package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.InfoDatosInt;
import util.UtilidadCadena;

public class MezclasForm extends ValidatorForm 
{
private String estado;
	
	private int consecutivo;
	
	private String codigo;
	
	private String nombre;
	
	private InfoDatosInt tipo;
	
	private String activo;
	
	private String mensaje;
	
	private String columna="nombre";
	
	private String ultimaColumna="";
	
	private HashMap mezclas;
	
	private String estadoAnterior;
	
	private String logModificacion;
	
    public String getLogModificacion()
    {
    	return this.logModificacion;
    }
    
    public void setLogModificacion(String logModificacion)
    {
    	this.logModificacion=logModificacion;
    }

    public String getEstado()
	{
		return this.estado;
	}
	
	public void setEstado(String estado)
	{
		this.estado=estado;
	}
	
	public String getEstadoAnterior()
	{
		return this.estadoAnterior;
	}
	
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior=estadoAnterior;
	}
	
	public int getConsecutivo()
	{
		return this.consecutivo;
	}
	
	public void setConsecutivo(int consecutivo)
	{
		this.consecutivo=consecutivo;
	}
	
	public String getCodigo()
	{
		return this.codigo;
	}
	
	public void setCodigo(String codigo)
	{
		this.codigo=codigo;
	}
	
	public String getNombre()
	{
		return this.nombre;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}
	
	public InfoDatosInt getTipo()
	{
		return this.tipo;
	}
	
	public void setTipo(InfoDatosInt tipo)
	{
		this.tipo = tipo;
	}
	
	public String getActivo()
	{
		return this.activo;
	}
	
	public void setActivo(String activo)
	{
		this.activo=activo;
	}
	
    public String getMensaje()
    {
    	return this.mensaje;
    }
    
    public void setMensaje(String mensaje)
    {
    	this.mensaje=mensaje;
    }
    
    public String getColumna()
    {
        return columna;
    }

    public void setColumna(String columna)
    {
        this.columna = columna;
    }

    public HashMap getMezclas()
	{
		return this.mezclas;
	}
	
	public void setMezclas(HashMap mezclas)
	{
		this.mezclas=mezclas;
	}
	
	
	public void clean()
	{
		this.consecutivo=-1;
		this.codigo="";
		this.nombre="";
		this.tipo = new InfoDatosInt();
		this.activo="";
		this.estado="empezar";
		this.estadoAnterior="";
		this.mensaje="";
		this.mezclas=new HashMap();
		this.mezclas.put("numRegistros", "0");
		this.logModificacion="";
		this.columna="nombre";
		this.ultimaColumna="";
	}
	
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        if(estado.equals("guardarNuevo"))
        {
            if( !UtilidadCadena.noEsVacio(this.getCodigo()) )
                errores.add("El código es requerido", new ActionMessage("errors.required", "El código"));
            
            if( !UtilidadCadena.noEsVacio(this.getNombre()) )
                errores.add("El nombre de la mezcla es requerido", new ActionMessage("errors.required", "La mezcla"));
            
            if( this.getTipo().getCodigo() ==0 )
                errores.add("El tipo de la mezcla es requerido", new ActionMessage("errors.required", "El tipo"));
            
            if(!errores.isEmpty())
            	this.estado="ingresar";
        }            
        else if(estado.equals("guardarCambios"))
        {
            if( !UtilidadCadena.noEsVacio(this.getNombre()) )
                errores.add("El nombre de la mezcla es requerido", new ActionMessage("errors.required", "La mezcla"));

            if(!errores.isEmpty())
            	this.estado="editar";
        }            

        return errores;
    }

	/**
	 * @return the ultimaColumna
	 */
	public String getUltimaColumna() {
		return ultimaColumna;
	}

	/**
	 * @param ultimaColumna the ultimaColumna to set
	 */
	public void setUltimaColumna(String ultimaColumna) {
		this.ultimaColumna = ultimaColumna;
	}
}
