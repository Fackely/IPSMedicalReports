/*
 * Abr 16, 2007
 */
package com.princetonsa.actionform.interfaz;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

public class CuentaInvUnidadFunForm extends ValidatorForm 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CuentaInvUnidadFunForm.class);
	/**
	 * Control del estado de la aplicacion
	 */
	private String estado;
	
	/**
	 * Acrónimo + separadorSplit + Nombre de la unidad Funcional elegida
	 */
	private String unidadFuncional;
	
	/**
	 * Acrónimo de la unidad funcional
	 */
	private String acronimoUnidadFuncional;
	
	/**
	 * Nombre de la unidad funcional
	 */
	private String nombreUnidadFuncional;
	
	/**
	 * Código de la clase elegida
	 */
	private int clase;
	
	/**
	 * Nombre de la clase elegida
	 */
	private String nombreClase;
	
	/**
	 * posicion del registro seleccionado en el mapa clases
	 */
	private int posClase;
	
	/**
	 * Código del grupo elegido
	 */
	private int grupo;
	
	/**
	 * Nombre del grupo Elegido
	 */
	private String nombreGrupo;
	
	/**
	 * posicion del registro seleccionado en el mapa grupo
	 */
	private int posGrupo;
	
	/**
	 * Código del subgrupo elegido
	 */
	private int subgrupo;
	
	/**
	 * Nombre del subgrupo elegido
	 */
	private String nombreSubgrupo;
	

	/**
	 * Consecutivo del subgrupo elegido
	 */
	private int codigo;
	
	/**
	 * Arreglo donde se almacenan las unidades funcionales parametrizadas
	 */
	private HashMap unidadesFun = new HashMap();
	
	/**
	 * Número de registros del mapa unidadesFun
	 */
	private int numUnidadesFun;
	
	//*****************ATRIBUTOS DE LOS REGISTROS**************************
	/**
	 * Manejo de las clases
	 */
	private HashMap clases = new HashMap();
	private int numClases;
	private HashMap clasesHistorico = new HashMap();
	
	/**
	 * Manejo de los grupos
	 */
	private HashMap grupos = new HashMap();
	private int numGrupos;
	private HashMap gruposHistorico = new HashMap();
	
	/**
	 * Manejo de los subgrupos
	 */
	private HashMap subgrupos = new HashMap();
	private int numSubgrupos;
	private HashMap subgruposHistorico = new HashMap();
	//*********************************************************************
	
	//*************ATRIBUTOS DE PAGINACION Y ORDENACION**********************
	private String indice;
	private String ultimoIndice;
	private String linkSiguiente;
	private int offset;
	private int maxPageItems;
	//**********************************************************************
	
	
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
		
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.unidadFuncional = "";
		this.acronimoUnidadFuncional = "";
		this.nombreUnidadFuncional = "";
		this.clase = 0;
		this.nombreClase = "";
		this.posClase = 0;
		this.grupo = 0;
		this.nombreGrupo = "";
		this.posGrupo = 0;
		this.subgrupo = 0;
		this.nombreSubgrupo = "";
		this.codigo = 0;
		this.unidadesFun = new HashMap();
		this.numUnidadesFun = 0;
		//atributos de los registros
		this.clases = new HashMap();
		this.numClases = 0;
		this.clasesHistorico = new HashMap();
		this.grupos = new HashMap();
		this.numGrupos = 0;
		this.gruposHistorico = new HashMap();
		this.subgrupos = new HashMap();
		this.numSubgrupos = 0;
		this.subgruposHistorico = new HashMap();
		//atributos de la paginación y ordenación
		this.indice = "";
		this.ultimoIndice = "";
		this.linkSiguiente = "";
		this.offset = 0;
		this.maxPageItems = 0;
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
	 * @return the clase
	 */
	public int getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(int clase) {
		this.clase = clase;
	}

	/**
	 * @return the clases
	 */
	public HashMap getClases() {
		return clases;
	}

	/**
	 * @param clases the clases to set
	 */
	public void setClases(HashMap clases) {
		this.clases = clases;
	}
	
	/**
	 * @return Retorna elemento del mapa clases
	 */
	public Object getClases(String key) {
		return clases.get(key);
	}

	/**
	 * @param Asigna elemento al mapa clases
	 */
	public void setClases(String key,Object obj) {
		this.clases.put(key,obj);
	}
	

	/**
	 * @return the clasesHistorico
	 */
	public HashMap getClasesHistorico() {
		return clasesHistorico;
	}

	/**
	 * @param clasesHistorico the clasesHistorico to set
	 */
	public void setClasesHistorico(HashMap clasesHistorico) {
		this.clasesHistorico = clasesHistorico;
	}
	
	/**
	 * @return Retorna elemento del mapa clasesHistorico
	 *
	public Object getClasesHistorico(String key) {
		return clasesHistorico.get(key);
	}

	/**
	 * @param Asigna elemento al mapa clasesHistorico
	 *
	public void setClasesHistorico(String key,Object obj) {
		this.clasesHistorico.put(key,obj);
	}
	

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the grupo
	 */
	public int getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(int grupo) 
	{
		this.grupo = grupo;
	}

	/**
	 * @return the grupos
	 */
	public HashMap getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos the grupos to set
	 */
	public void setGrupos(HashMap grupos) {
		this.grupos = grupos;
	}
	
	/**
	 * @return Retorna elemento del mapa grupos
	 */
	public Object getGrupos(String key) {
		return grupos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa grupos 
	 */
	public void setGrupos(String key,Object obj) {
		this.grupos.put(key,obj);
	}

	/**
	 * @return the gruposHistorico
	 */
	public HashMap getGruposHistorico() {
		return gruposHistorico;
	}

	/**
	 * @param gruposHistorico the gruposHistorico to set
	 */
	public void setGruposHistorico(HashMap gruposHistorico) {
		this.gruposHistorico = gruposHistorico;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
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
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the subgrupo
	 */
	public int getSubgrupo() {
		return subgrupo;
	}

	/**
	 * @param subgrupo the subgrupo to set
	 */
	public void setSubgrupo(int subgrupo) 
	{
		this.subgrupo = subgrupo;
	}

	/**
	 * @return the subgrupos
	 */
	public HashMap getSubgrupos() {
		return subgrupos;
	}

	/**
	 * @param subgrupos the subgrupos to set
	 */
	public void setSubgrupos(HashMap subgrupos) {
		this.subgrupos = subgrupos;
	}
	
	/**
	 * @return Retorna elemento del mapa subgrupos
	 */
	public Object getSubgrupos(String key) {
		return subgrupos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa subgrupos 
	 */
	public void setSubgrupos(String key,Object obj) {
		this.subgrupos.put(key,obj);
	}
	

	/**
	 * @return the subgruposHistorico
	 */
	public HashMap getSubgruposHistorico() {
		return subgruposHistorico;
	}

	/**
	 * @param subgruposHistorico the subgruposHistorico to set
	 */
	public void setSubgruposHistorico(HashMap subgruposHistorico) {
		this.subgruposHistorico = subgruposHistorico;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the unidadFuncional
	 */
	public String getUnidadFuncional() {
		return unidadFuncional;
	}

	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setUnidadFuncional(String unidadFuncional) 
	{
		this.unidadFuncional = unidadFuncional;
		String[] vector = unidadFuncional.split(ConstantesBD.separadorSplit);
		this.acronimoUnidadFuncional = vector[0];
		this.nombreUnidadFuncional = vector[1];
	}

	/**
	 * @return the numClases
	 */
	public int getNumClases() {
		return numClases;
	}

	/**
	 * @param numClases the numClases to set
	 */
	public void setNumClases(int numClases) {
		this.numClases = numClases;
	}

	/**
	 * @return the numGrupos
	 */
	public int getNumGrupos() {
		return numGrupos;
	}

	/**
	 * @param numGrupos the numGrupos to set
	 */
	public void setNumGrupos(int numGrupos) {
		this.numGrupos = numGrupos;
	}

	/**
	 * @return the numSubgrupos
	 */
	public int getNumSubgrupos() {
		return numSubgrupos;
	}

	/**
	 * @param numSubgrupos the numSubgrupos to set
	 */
	public void setNumSubgrupos(int numSubgrupos) {
		this.numSubgrupos = numSubgrupos;
	}

	/**
	 * @return the nombreClase
	 */
	public String getNombreClase() {
		return nombreClase;
	}

	/**
	 * @param nombreClase the nombreClase to set
	 */
	public void setNombreClase(String nombreClase) {
		this.nombreClase = nombreClase;
	}

	/**
	 * @return the nombreGrupo
	 */
	public String getNombreGrupo() {
		return nombreGrupo;
	}

	/**
	 * @param nombreGrupo the nombreGrupo to set
	 */
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	/**
	 * @return the nombreSubgrupo
	 */
	public String getNombreSubgrupo() {
		return nombreSubgrupo;
	}

	/**
	 * @param nombreSubgrupo the nombreSubgrupo to set
	 */
	public void setNombreSubgrupo(String nombreSubgrupo) {
		this.nombreSubgrupo = nombreSubgrupo;
	}

	/**
	 * @return the nombreUnidadFuncional
	 */
	public String getNombreUnidadFuncional() {
		return nombreUnidadFuncional;
	}

	/**
	 * @param nombreUnidadFuncional the nombreUnidadFuncional to set
	 */
	public void setNombreUnidadFuncional(String nombreUnidadFuncional) {
		this.nombreUnidadFuncional = nombreUnidadFuncional;
	}

	/**
	 * @return the posClase
	 */
	public int getPosClase() {
		return posClase;
	}

	/**
	 * @param posClase the posClase to set
	 */
	public void setPosClase(int posClase) {
		this.posClase = posClase;
	}

	/**
	 * @return the posGrupo
	 */
	public int getPosGrupo() {
		return posGrupo;
	}

	/**
	 * @param posGrupo the posGrupo to set
	 */
	public void setPosGrupo(int posGrupo) {
		this.posGrupo = posGrupo;
	}

	

	/**
	 * @return the numUnidadesFun
	 */
	public int getNumUnidadesFun() {
		return numUnidadesFun;
	}

	/**
	 * @param numUnidadesFun the numUnidadesFun to set
	 */
	public void setNumUnidadesFun(int numUnidadesFun) {
		this.numUnidadesFun = numUnidadesFun;
	}

	/**
	 * @return the unidadesFun
	 */
	public HashMap getUnidadesFun() {
		return unidadesFun;
	}

	/**
	 * @param unidadesFun the unidadesFun to set
	 */
	public void setUnidadesFun(HashMap unidadesFun) {
		this.unidadesFun = unidadesFun;
	}
	
	/**
	 * @return Retorna elemento del mapa unidadesFun
	 */
	public Object getUnidadesFun(String key) {
		return unidadesFun.get(key);
	}

	/**
	 * @param Asigna elemento al mapa unidadesFun 
	 */
	public void setUnidadesFun(String key,Object obj) {
		this.unidadesFun.put(key,obj);
	}

	/**
	 * @return the acronimoUnidadFuncional
	 */
	public String getAcronimoUnidadFuncional() {
		return acronimoUnidadFuncional;
	}

	/**
	 * @param acronimoUnidadFuncional the acronimoUnidadFuncional to set
	 */
	public void setAcronimoUnidadFuncional(String acronimoUnidadFuncional) {
		this.acronimoUnidadFuncional = acronimoUnidadFuncional;
	}
}
