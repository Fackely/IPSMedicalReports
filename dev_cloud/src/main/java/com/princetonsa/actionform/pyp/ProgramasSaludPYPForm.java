/*
 * @author armando
 */
package com.princetonsa.actionform.pyp;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadValidacion;



/**
 * 
 * @author armando
 *
 */
public class ProgramasSaludPYPForm extends ValidatorForm 
{
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
	private String tipoPrograma;
	
	/**
	 * 
	 */
	private String grupoEtareo;
	
	/**
	 * 
	 */
	private HashMap diagnosticos;
	
	/**
	 * 
	 */
	private int diagEliminar;
	
	/**
	 * 
	 */
	private boolean embarazo;
	
	/**
	 * 
	 */
	private String formato;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * 
	 */
	private HashMap programas;
	
	/**
	 * 
	 */
	private String accion;
	
	/**
	 * 
	 */
	private HashMap programasEliminados;
	
	
	/**
	 * Indice del registro que se desea eliminar
	 */
	private int index;
	
	/**
	 * 
	 */
	private String descArchivo;
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * para controlar la página actual
     * del pager.
     */
    private int offset;
    
    /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
    /**
     * el numero de registros por pager
     */
    private int maxPageItems;

	
	/**
	 * Archivo
	 */
	private transient FormFile archivo;
	
	/**
	 * Para saber si se puede eliminar el programa
	 */
	private boolean puedoEliminar;

    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		
		formatearDescripcion();
		
		
		if(estado.equals("guardarNuevo")||estado.equals("modificar"))
		{
			if(estado.equals("guardarNuevo"))
			{
				if(this.codigo.trim().equals(""))
				{
					errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El codigo"));  
				}
				else
				{
					if(UtilidadValidacion.existeProgramaSaludPYP(this.codigo,this.institucion))
					{
						errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Código "+this.codigo));
					}
				}
			}
			
			if(this.descripcion.trim().equals(""))
			{
				errores.add("descripcion REQUERIDO", new ActionMessage("errors.required","La descripcion"));  
			}
			if(this.tipoPrograma.trim().equals(""))
			{
				errores.add("Tipo Programa", new ActionMessage("errors.required","El tipo de programa"));  
			}
			if(this.grupoEtareo.trim().equals(""))
			{
				errores.add("Grupo Etareo", new ActionMessage("errors.required","El grupo etareo"));  
			}
		}
		return errores;
	}


	/**
	 * Método implementado para formatear la descripcion del programa
	 * 
	 *
	 */
	private void formatearDescripcion() 
	{
		if(this.descripcion!=null&&this.descripcion.length()>0)
		{
			this.descripcion = this.descripcion.replaceAll("&Ntilde;","Ñ");
			this.descripcion = this.descripcion.replaceAll("&ntilde;","ñ");
			this.descripcion = this.descripcion.replaceAll("&aacute;","á");
			this.descripcion = this.descripcion.replaceAll("&eacute;","é");
			this.descripcion =  this.descripcion.replaceAll("&iacute;","í");
			this.descripcion = this.descripcion.replaceAll("&oacute;","ó");
			this.descripcion =  this.descripcion.replaceAll("&uacute;","ú");
			this.descripcion = this.descripcion.replaceAll("&Aacute;","Á");
			this.descripcion = this.descripcion.replaceAll("&Eacute;","É");
			this.descripcion = this.descripcion.replaceAll("&Iacute;","Í");
			this.descripcion =  this.descripcion.replaceAll("&Oacute;","Ó");
			this.descripcion = this.descripcion.replaceAll("&Uacute;","Ú");
		}
		
	}


	/**
     * inicializar atributos de esta forma
     *
     */
    public void reset ()
    {
    	this.programas=new HashMap();
    	this.programas.put("numRegistros","0");
    	this.programasEliminados=new HashMap();
    	this.programasEliminados.put("numRegistros","0");
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=ConstantesBD.codigoNuncaValido;
    	this.linkSiguiente="";
    	this.maxPageItems=10;
    	this.codigo="";
    	this.descripcion="";
    	this.tipoPrograma="";
    	this.grupoEtareo="";
    	this.diagnosticos=new HashMap();
    	this.diagnosticos.put("numRegistros","0");
    	this.diagEliminar=ConstantesBD.codigoNuncaValido;
    	this.embarazo=false;
    	this.formato="";
    	this.activo=false;
    	this.descArchivo="";
    	this.puedoEliminar = true;
    }


	/**
	 * @return the puedoEliminar
	 */
	public boolean isPuedoEliminar() {
		return puedoEliminar;
	}


	/**
	 * @param puedoEliminar the puedoEliminar to set
	 */
	public void setPuedoEliminar(boolean puedoEliminar) {
		this.puedoEliminar = puedoEliminar;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getProgramas() {
		return programas;
	}


	public void setProgramas(HashMap programas) {
		this.programas = programas;
	}
	
	public Object getProgramas(String key) {
		return programas.get(key);
	}


	public void setProgramas(String key,Object value) {
		this.programas.put(key,value);
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public HashMap getProgramasEliminados() {
		return programasEliminados;
	}


	public void setProgramasEliminados(HashMap programasEliminados) {
		this.programasEliminados = programasEliminados;
	}
	
	public Object getProgramasEliminados(String key) {
		return programasEliminados.get(key);
	}


	public void setProgramasEliminados(String key,Object value) {
		this.programasEliminados.put(key,value);
	}



	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
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


	public FormFile getArchivo() {
		return archivo;
	}


	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}

	public boolean isActivo() {
		return activo;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
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


	public boolean isEmbarazo() {
		return embarazo;
	}


	public void setEmbarazo(boolean embarazo) {
		this.embarazo = embarazo;
	}


	public String getFormato() {
		return formato;
	}


	public void setFormato(String formato) {
		this.formato = formato;
	}


	public String getGrupoEtareo() {
		return grupoEtareo;
	}


	public void setGrupoEtareo(String grupoEtareo) {
		this.grupoEtareo = grupoEtareo;
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	public String getTipoPrograma() {
		return tipoPrograma;
	}


	public void setTipoPrograma(String tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}


	public String getAccion() {
		return accion;
	}


	public void setAccion(String accion) {
		this.accion = accion;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getDescArchivo() {
		return descArchivo;
	}


	public void setDescArchivo(String descArchivo) {
		this.descArchivo = descArchivo;
	}


	public HashMap getDiagnosticos() {
		return diagnosticos;
	}


	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}


	public void setDiagnosticos(String key,Object value) {
		this.diagnosticos.put(key,value);
	}


	public int getDiagEliminar() {
		return diagEliminar;
	}


	public void setDiagEliminar(int diagEliminar) {
		this.diagEliminar = diagEliminar;
	}

}
