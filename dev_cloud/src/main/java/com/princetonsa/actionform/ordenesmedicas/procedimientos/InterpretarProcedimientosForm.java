package com.princetonsa.actionform.ordenesmedicas.procedimientos;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;



public class InterpretarProcedimientosForm extends ValidatorForm
{
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String filtro="";
	
	/**
	 * 
	 */
	private String fechaInicialFiltro;
	
	/**
	 * 
	 */
	private String fechaFinalFiltro;
	
	/**
	 * 
	 */
	private String centroCostosSolicitanteFiltro;
	
	/**
	 * 
	 */
	private HashMap listaSolicitudes;
	
	/**
	 * 
	 * */
	private String indicadorExitoOperacion ;
	
	
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String indexRegistro;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 */
	private HashMap historicoRespuestas;
	
	
	private String areaFiltro;
	private String pisoFiltro;
	private String habitacionFiltro;
	private String camaFiltro;
	
	
	private boolean vieneEvolucion=false;
	
	
	public void reset()
	{
		this.listaSolicitudes=new HashMap();
		this.listaSolicitudes.put("numRegistros", "0");
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.indexRegistro="";
		this.numeroSolicitud="";
		this.historicoRespuestas=new HashMap();
		this.historicoRespuestas.put("numRegistros", "0");
		this.maxPageItems=20;
		this.linkSiguiente="";		
		this.areaFiltro="";
		this.pisoFiltro="";
		this.habitacionFiltro="";
		this.camaFiltro="";
		this.vieneEvolucion=false;
		this.indicadorExitoOperacion = ConstantesBD.acronimoNo;
	}
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("interpretar"))
		{
			if((this.listaSolicitudes.get("interpretacion_"+this.getIndexRegistro())+"").trim().equals(""))
			{
				errores.add("interpretacion_",new ActionMessage("errors.required","Interpretacion"));
			}
		}
		return errores;
	}
	
	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public String getCentroCostosSolicitanteFiltro()
	{
		return centroCostosSolicitanteFiltro;
	}

	public void setCentroCostosSolicitanteFiltro(
			String centroCostosSolicitanteFiltro)
	{
		this.centroCostosSolicitanteFiltro = centroCostosSolicitanteFiltro;
	}

	public String getFechaFinalFiltro()
	{
		return fechaFinalFiltro;
	}

	public void setFechaFinalFiltro(String fechaFinalFiltro)
	{
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	public String getFechaInicialFiltro()
	{
		return fechaInicialFiltro;
	}

	public void setFechaInicialFiltro(String fechaInicialFiltro)
	{
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public String getFiltro()
	{
		return filtro;
	}

	public void setFiltro(String filtro)
	{
		this.filtro = filtro;
	}

	public HashMap getListaSolicitudes()
	{
		return listaSolicitudes;
	}

	public void setListaSolicitudes(HashMap listaSolicitudes)
	{
		this.listaSolicitudes = listaSolicitudes;
	}
	public Object getListaSolicitudes(String key)
	{
		return this.listaSolicitudes.get(key);
	}

	public void setListaSolicitudes(String key,Object value)
	{
		this.listaSolicitudes.put(key, value);
	}


	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}
	public String getIndexRegistro()
	{
		return indexRegistro;
	}
	public void setIndexRegistro(String indexRegistro)
	{
		this.indexRegistro = indexRegistro;
	}
	public HashMap getHistoricoRespuestas()
	{
		return historicoRespuestas;
	}
	public void setHistoricoRespuestas(HashMap historicoRespuestas)
	{
		this.historicoRespuestas = historicoRespuestas;
	}
	public String getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(String numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}
	public int getMaxPageItems()
	{
		return maxPageItems;
	}
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}
	public String getAreaFiltro() {
		return areaFiltro;
	}
	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}
	public String getCamaFiltro() {
		return camaFiltro;
	}
	public void setCamaFiltro(String camaFiltro) {
		this.camaFiltro = camaFiltro;
	}
	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}
	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}
	public String getPisoFiltro() {
		return pisoFiltro;
	}
	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	public boolean isVieneEvolucion() {
		return vieneEvolucion;
	}
	public void setVieneEvolucion(boolean vieneEvolucion) {
		this.vieneEvolucion = vieneEvolucion;
	}
	/**
	 * @return the indicadorExitoOperacion
	 */
	public String getIndicadorExitoOperacion() {
		return indicadorExitoOperacion;
	}
	/**
	 * @param indicadorExitoOperacion the indicadorExitoOperacion to set
	 */
	public void setIndicadorExitoOperacion(String indicadorExitoOperacion) {
		this.indicadorExitoOperacion = indicadorExitoOperacion;
	}

}
