package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

import com.princetonsa.action.historiaClinica.TiposTratamientosOdontologicosAction;

public class TiposTratamientosOdontologicosForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(TiposTratamientosOdontologicosAction.class);
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String indexMap; 
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;
	
	/**
	 * Mapa para los Tipoos de Tratam
	 */
	private HashMap tiposTMap;
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
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
	
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("insertar"))
        {
        	if(this.getCodigo().equals(""))
        		errores.add("Código requerido",new ActionMessage("errors.required", "El código del tipo de Tratamiento "));
           	
        	
        	if(this.getDescripcion().equals(""))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del tipo de Tratamiento "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("guardarNuevo");
        }
        if(this.estado.equals("guardarModificacion"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigo()))
        		errores.add("código requerido",new ActionMessage("errors.required", "El código del tipo de Tratamiento "));
        	
           	if(UtilidadTexto.isEmpty(this.getDescripcion()))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del tipo de Tratamiento "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
        }
        return errores;
    }
	
	
	public void reset( int codigoInstitucion)
	{
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.tiposTMap= new HashMap();
		tiposTMap.put("numRegistros", 0);
		this.activo="1";
		this.codigo="";
		this.descripcion="";
	}
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
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

	public HashMap getTiposTMap() {
		return tiposTMap;
	}

	public void setTiposTMap(HashMap tiposTMap) {
		this.tiposTMap = tiposTMap;
	}
	
	public Object getTiposTMap(String key) {
		return tiposTMap.get(key);
	}


	public void setTiposTMap(String key, Object value) {
		this.tiposTMap.put(key, value);
	}



	public String getIndexMap() {
		return indexMap;
	}

	public String getActivo() {
		return activo;
	}



	public void setActivo(String activo) {
		this.activo = activo;
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