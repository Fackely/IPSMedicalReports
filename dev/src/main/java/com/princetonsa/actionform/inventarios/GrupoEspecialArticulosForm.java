package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class GrupoEspecialArticulosForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private String estado;
	
	private HashMap mapaGrupoArticulos;
	
	private HashMap mapaGruposEliminados;
	
	private int indiceGrupoEliminar;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.mapaGrupoArticulos= new HashMap();
		this.mapaGrupoArticulos.put("numRegistros", "0");
		this.mapaGruposEliminados = new HashMap();
		this.mapaGruposEliminados.put("numRegistros", "0");
		this.indiceGrupoEliminar=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.maxPageItems=10;
		this.linkSiguiente="";
	}
	
	
	public void resetMensaje()
	{
		this.mensaje=new ResultadoBoolean(false);
	}
	
	
	
	
	/**
	 *  Validate the properties that have been set from this HTTP request, and
	 *  return an <code>ActionErrors</code> object that encapsulates any 
	 *  validation errors that have been found. If no errors are found, return
	 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 *  error messages.
	 *  @param mapping The mapping used to select this instance
	 *  @param request The servlet request we are processing
	 */
    
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.mapaGrupoArticulos.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.mapaGrupoArticulos.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Nombre del registro "+(i+1)));
				}
				if((this.mapaGrupoArticulos.get("codigo_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.mapaGrupoArticulos.get("codigo_"+i)+"").equalsIgnoreCase(this.mapaGrupoArticulos.get("codigo_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.mapaGrupoArticulos.get("codigo_"+i)));
						}
					}
				}
			}
		}
		return errores;
	}
	
	
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaGrupoArticulos() {
		return mapaGrupoArticulos;
	}

	/**
	 * 
	 * @param mapaGrupoArticulos
	 */
	public void setMapaGrupoArticulos(HashMap mapaGrupoArticulos) {
		this.mapaGrupoArticulos = mapaGrupoArticulos;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaGrupoArticulos(String key) 
	{
		return mapaGrupoArticulos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaGrupoArticulos(String key,Object value) 
	{
		this.mapaGrupoArticulos.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceGrupoEliminar() {
		return indiceGrupoEliminar;
	}

	/**
	 * 
	 * @param indiceGrupoEliminar
	 */
	public void setIndiceGrupoEliminar(int indiceGrupoEliminar) {
		this.indiceGrupoEliminar = indiceGrupoEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaGruposEliminados() {
		return mapaGruposEliminados;
	}

	/**
	 * 
	 * @param mapaGruposEliminados
	 */
	public void setMapaGruposEliminados(HashMap mapaGruposEliminados) {
		this.mapaGruposEliminados = mapaGruposEliminados;
	}

	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * 
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * 
	 * @return
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	
	
}
