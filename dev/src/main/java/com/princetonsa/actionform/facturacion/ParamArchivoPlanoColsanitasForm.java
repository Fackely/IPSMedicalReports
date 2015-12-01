package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ParamArchivoPlanoColsanitasForm extends ValidatorForm {
	
	/**
	 * Estado en que se encuetra la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa que contiene la informacion de la parametrizacion de los archivos planos de colsanitas
	 */
	private HashMap archivoPlanoColsanitasMap;
	
	/**
	 * Mapa que contiene la informacion para verificar si hay modificaciones y para el log
	 */
	private HashMap archivoPlanoColsanitasEliminadosMap;
	
	/**
	 * Se utiliza para la navegacion de pager
	 */
	private String linkSiguiente;
	
	/**
	 * Indice del mapa que se desea eliminar
	 */
	private int indiceEliminado;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * 
	 */
	private ArrayList convenios;
	
	private int convenioCodigo;
	
	/**
	 * Metodo que resetea los atributos de la forma
	 *
	 */
	public void reset()
	{
		this.archivoPlanoColsanitasEliminadosMap=new HashMap();
		this.archivoPlanoColsanitasEliminadosMap.put("numRegistros", "0");
		
		this.archivoPlanoColsanitasMap=new HashMap();
		this.archivoPlanoColsanitasMap.put("numRegistros", "0");
		
		this.linkSiguiente="";
    	this.indiceEliminado=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.maxPageItems=10;
    	
	}
	
	
	public void resetConvenios ()
	{
		this.convenios=new ArrayList();
    	this.convenioCodigo=ConstantesBD.codigoNuncaValido;
	}
	/**
	 * Metodo para la validacion de la forma
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			int numReg=Utilidades.convertirAEntero(this.archivoPlanoColsanitasMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(this.archivoPlanoColsanitasMap.get("unidad_"+i).equals(""))
					errores.add("convenio",new ActionMessage("errors.required","La Unidad Economica del registro "+(i+1)));
				
				if(this.archivoPlanoColsanitasMap.get("compania_"+i).equals(""))
					errores.add("convenio",new ActionMessage("errors.required","El Identificador de Compañia del registro "+(i+1)));
				
				if(this.archivoPlanoColsanitasMap.get("plan_"+i).equals(""))
					errores.add("convenio",new ActionMessage("errors.required","El Identificador de Plan del registro "+(i+1)));
				
				
				for(int j=(i+1);j<numReg;j++)
					if ((this.archivoPlanoColsanitasMap.get("convenio_"+i)+"").equals((this.archivoPlanoColsanitasMap.get("convenio_"+j)+"")))
						errores.add("convenio",new ActionMessage("error.noRegistroMismaInformacion"," convenio en el registro "+(j+1)));
			}
		}
		return errores;
	}

	/**
	 * @return the archivoPlanoColsanitasEliminadosMap
	 */
	public HashMap getArchivoPlanoColsanitasEliminadosMap() {
		return archivoPlanoColsanitasEliminadosMap;
	}

	/**
	 * @param archivoPlanoColsanitasEliminadosMap the archivoPlanoColsanitasEliminadosMap to set
	 */
	public void setArchivoPlanoColsanitasEliminadosMap(
			HashMap archivoPlanoColsanitasEliminadosMap) {
		this.archivoPlanoColsanitasEliminadosMap = archivoPlanoColsanitasEliminadosMap;
	}
	
	public Object getArchivoPlanoColsanitasEliminadosMap(String key)
	{
		return archivoPlanoColsanitasEliminadosMap.get(key);
	}
	
	public void setArchivoPlanoColsanitasEliminadosMap(String key,Object value)
	{
		this.archivoPlanoColsanitasEliminadosMap.put(key,value);
	}

	/**
	 * @return the archivoPlanoColsanitasMap
	 */
	public HashMap getArchivoPlanoColsanitasMap() {
		return archivoPlanoColsanitasMap;
	}

	/**
	 * @param archivoPlanoColsanitasMap the archivoPlanoColsanitasMap to set
	 */
	public void setArchivoPlanoColsanitasMap(HashMap archivoPlanoColsanitasMap) {
		this.archivoPlanoColsanitasMap = archivoPlanoColsanitasMap;
	}
	
	public Object getArchivoPlanoColsanitasMap(String key)
	{
		return archivoPlanoColsanitasMap.get(key);
	}
	
	public void setArchivoPlanoColsanitasMap(String key,Object value)
	{
		this.archivoPlanoColsanitasMap.put(key,value);
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the indiceEliminado
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado the indiceEliminado to set
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenioCodigo
	 */
	public int getConvenioCodigo() {
		return convenioCodigo;
	}

	/**
	 * @param convenioCodigo the convenioCodigo to set
	 */
	public void setConvenioCodigo(int convenioCodigo) {
		this.convenioCodigo = convenioCodigo;
	}

}
