package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.action.tesoreria.MotivosDevolucionRecibosCajaAction;

public class MotivosDevolucionRecibosCajaForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(MotivosDevolucionRecibosCajaAction.class);
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String indexMap;
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private String activo="1";
	
	/**
	 * 
	 */
	private HashMap motDevRCMap;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}




	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}




	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("insertar"))
        {
        	if(this.getCodigo().equals(""))
        		errores.add("Código requerido",new ActionMessage("errors.required", "El código del Motivo de Devolucion "));
           	
        	
        	if(this.getDescripcion().equals(""))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del Motivo de Devolucion "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("guardarNuevo");
        }
        if(this.estado.equals("guardarModificacion"))
        {
           	if(UtilidadTexto.isEmpty(this.getDescripcion()))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del Motivo de Devolucion "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
        }
        return errores;
    }
	
	
	

	public void reset( int codigoInstitucion)
	{
		this.codigo="";
		this.descripcion="";
		this.activo="1";
		this.indexMap="";
	}
	
	public HashMap getMotDevRCMap() {
		return motDevRCMap;
	}


	public void setMotDevRCMap(HashMap motDevRCMap) {
		this.motDevRCMap = motDevRCMap;
	}
	
	public Object getMotDevRCMap(String key) {
		return motDevRCMap.get(key);
	}


	public void setMotDevRCMap(String key, Object value) {
		this.motDevRCMap.put(key, value);
	}	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}


	public String getIndexMap() {
		return indexMap;
	}


	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}