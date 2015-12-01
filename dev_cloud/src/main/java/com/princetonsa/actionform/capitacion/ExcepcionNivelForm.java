/*
 * Creado el Jun 15, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;


public class ExcepcionNivelForm extends ActionForm {

	/**
	 * Para Mantener el Flujo de La Funcionalidad
	 */
	private String estado; 

	/**
	 * Mapa para almacenar la información.
	 */
	private HashMap mapa; 
	
	/**
	 * Mapa para almacenar la información de los servicios Nuevos.
	 */
	private HashMap mapaServicio;

	/**
	 * Mapa para almacenar la información de los servicios registrados anteriormente.
	 */
	private HashMap mapaServicioReg;
	
	/**
	 * Mapa para almacenar la información la informacion de los servicios.
	 */
	private HashMap mapaContrato;
	
	
    /**
     * Variable para manejar el paginador.
     */
    private String linkSiguiente;
    private String patronOrdenar;
    private String ultimoPatronOrdenar;
    
    /**
     * Variable para almacenar el registro a eliminar
     */
    private int nroRegEliminar;


    /**
     * Variable para contabilizar los registros nuevos ingresados
     */
    private int nroRegistrosNuevos;
    

    /**
     * Variable para saber si se entra a consultar o a ingresar.
     */
    private boolean  soloConsulta;

    /**
     * Variable para almacenar el codigo del convenio seleccionado.
     */
    private int codigoConvenio;

    /**
     * Variable para almacenar el codigo del convenio seleccionado.
     */
    private String nombreConvenio;

    /**
     * Variable para almacenar el codigo del convenio seleccionado.
     */
    private int codigoContrato;
	
	/**
	 * Limpiar e inicializar la informacion de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.nroRegistrosNuevos = 0;
		this.mapa = new HashMap();
		this.mapaServicio = new HashMap();
		this.mapaContrato = new HashMap();
		
		this.ultimoPatronOrdenar = "";
		this.patronOrdenar = "";
		this.codigoConvenio = 0;
		this.nombreConvenio = "";
		this.codigoContrato = 0;
		
		/*
		 * Inicialmente no tiene códigos de los servicios insertados
		 */
		this.mapaServicio.put("codigosServiciosInsertados","");
		this.mapaServicio.put("numeroFilasMapaServicios", "0");
	}
	/**
	 * Para validar la informacion del formulario. 
	 */
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("empezarContinuarServicio"))
		{
				
			int tempoNumServicios=Integer.parseInt(this.mapaServicio.get("numeroFilasMapaServicios")+"");
			if ( UtilidadCadena.noEsVacio(this.getMapaServicio("codigoServicio_" + tempoNumServicios)+"") )
			{
				this.mapaServicio.put("numeroFilasMapaServicios", (tempoNumServicios+1)+"");
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
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa() {
		return this.mapa;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}

	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}

	/**
	 * @param Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna ultimoPatronOrdenar.
	 */
	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}

	/**
	 * @param Asigna ultimoPatronOrdenar.
	 */
	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}

	/**
	 * @return Retorna nroRegistrosNuevos.
	 */
	public int getNroRegistrosNuevos() {
		return nroRegistrosNuevos;
	}

	/**
	 * @param Asigna nroRegistrosNuevos.
	 */
	public void setNroRegistrosNuevos(int nroRegistrosNuevos) {
		this.nroRegistrosNuevos = nroRegistrosNuevos;
	}
	/**
	 * @return Retorna soloConsulta.
	 */
	public boolean getSoloConsulta() {
		return soloConsulta;
	}
	/**
	 * @param Asigna soloConsulta.
	 */
	public void setSoloConsulta(boolean soloConsulta) {
		this.soloConsulta = soloConsulta;
	}
	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Retorna nroRegEliminar.
	 */
	public int getNroRegEliminar() {
		return nroRegEliminar;
	}
	/**
	 * @param Asigna nroRegEliminar.
	 */
	public void setNroRegEliminar(int nroRegEliminar) {
		this.nroRegEliminar = nroRegEliminar;
	}
	/**
	 * @return Retorna codigoConvenio.
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}
	/**
	 * @param Asigna codigoConvenio.
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}
	/**
	 * @return Retorna nombreConvenio.
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}
	/**
	 * @param Asigna nombreConvenio.
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaServicio() {
		return this.mapaServicio;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaServicio(HashMap mapaServicio) {
		this.mapaServicio = mapaServicio;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaServicio(String key) {
		return this.mapaServicio.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaServicio(String key, String valor) 
	{
		this.mapaServicio.put(key, valor); 
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaContrato() {
		return this.mapaContrato;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaContrato(HashMap mapaContrato) {
		this.mapaContrato = mapaContrato;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaContrato(String key) {
		return this.mapaContrato.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaContrato(String key, String valor) 
	{
		this.mapaContrato.put(key, valor); 
	}
	
	
	/**
	 * @return Retorna codigoContrato.
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * @param Asigna codigoContrato.
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaServicioReg() {
		return this.mapaServicioReg;
	}

	/**
	 * @param Asignar el mapa
	 */
	public void setMapaServicioReg(HashMap mapaServicioReg) {
		this.mapaServicioReg = mapaServicioReg;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public Object getMapaServicioReg(String key) {
		return this.mapaServicioReg.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapaServicioReg(String key, String valor) 
	{
		this.mapaServicioReg.put(key, valor); 
	}

}
