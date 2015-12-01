/*
 * @(#)ConsultaTarifasServiciosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.facturacion;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta de las tarifas de servicios. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 24 /Mar/ 2006
 */
public class ConsultaTarifasServiciosForm extends ActionForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaServicios;
	
	/**
	 * almacena el numero de filas en el HashMap mapaActualizacion
	 */
	private int numeroElementos;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;

          
     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
      * String para el codigo interno del servicio (consecutivo-especialidad)
      */
     private String codigoInterno;
     
     /**
      * Codigo de la especialidad para la busqueda
      */
     private int codigoEspecialidad;
     
     /**
      * Acronimo para le tipo de servicio en la busqueda
      */
     private String acronimoTipoServicio;
     
     /**
      * Acronimo para la naturaleza del servicio es la busqueda
      */
     private String acronimoNaturaleza="";
     
     /**
      * Codigo del grupo de servicios para la busqueda
      */
     private int codigoGrupoServicio;
     
     /**
      * Mapa con los datos del encabezado del resultado de el detalle de un servicio
      */
     private HashMap mapaEncabezadoDetalle;
     
     /**
      * Mapa con los datos de las tarifas de un servicio del resultado del detalle de servicio
      */
     private HashMap mapaCuerpoDetalle;
     
     /**
      * Codigo del servicio para ver el detalle
      */
     private int codigoDetalle;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private String tipoTarifario;
    
    /**
     * 
     */
    private String codigoServicio;
    
    /**
     * descripcion de la busqueda
     */
    private String descripcionServicio;
 	
 	
	public void reset (int codigoInstitucion)
	{
		this.mapaServicios = new HashMap ();
		this.mapaEncabezadoDetalle = new HashMap ();
		this.mapaCuerpoDetalle = new HashMap ();
		this.estado = "";
		this.linkSiguiente = "";
		this.codigoInterno = "";
		this.codigoEspecialidad = -1;
		this.acronimoTipoServicio = "";
		this.acronimoNaturaleza = "";
		this.codigoGrupoServicio=0;
		this.codigoDetalle=0;
		this.index=ConstantesBD.codigoNuncaValido;
		this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
		this.inicializarTagMap(codigoInstitucion);
		this.tipoTarifario="";
		this.codigoServicio="";
		this.descripcionServicio="";
	}
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaServicios = new HashMap ();
		this.mapaEncabezadoDetalle = new HashMap ();
		this.mapaCuerpoDetalle = new HashMap ();
	}
	
	
	
	
	
	
	/**
	 * @return Returns the codigoDetalle.
	 */
	public int getCodigoDetalle()
	{
		return codigoDetalle;
	}


	/**
	 * @param codigoDetalle The codigoDetalle to set.
	 */
	public void setCodigoDetalle(int codigoDetalle)
	{
		this.codigoDetalle=codigoDetalle;
	}


	/**
	 * @return Returns the acronimoNaturaleza.
	 */
	public String getAcronimoNaturaleza()
	{
		return acronimoNaturaleza;
	}


	/**
	 * @param acronimoNaturaleza The acronimoNaturaleza to set.
	 */
	public void setAcronimoNaturaleza(String acronimoNaturaleza)
	{
		this.acronimoNaturaleza=acronimoNaturaleza;
	}


	/**
	 * @return Returns the acronimoTipoServicio.
	 */
	public String getAcronimoTipoServicio()
	{
		return acronimoTipoServicio;
	}


	/**
	 * @param acronimoTipoServicio The acronimoTipoServicio to set.
	 */
	public void setAcronimoTipoServicio(String acronimoTipoServicio)
	{
		this.acronimoTipoServicio=acronimoTipoServicio;
	}


	/**
	 * @return Returns the codigoEspecialidad.
	 */
	public int getCodigoEspecialidad()
	{
		return codigoEspecialidad;
	}


	/**
	 * @param codigoEspecialidad The codigoEspecialidad to set.
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.codigoEspecialidad=codigoEspecialidad;
	}


	/**
	 * @return Returns the codigoGrupoServicio.
	 */
	public int getCodigoGrupoServicio()
	{
		return codigoGrupoServicio;
	}


	/**
	 * @param codigoGrupoServicio The codigoGrupoServicio to set.
	 */
	public void setCodigoGrupoServicio(int codigoGrupoServicio)
	{
		this.codigoGrupoServicio=codigoGrupoServicio;
	}


	
	/**
	 * @return Returns the codigoInterno.
	 */
	public String getCodigoInterno()
	{
		return codigoInterno;
	}


	/**
	 * @param codigoInterno The codigoInterno to set.
	 */
	public void setCodigoInterno(String codigoInterno)
	{
		this.codigoInterno=codigoInterno;
	}


	

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}


	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}


	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}


	/**
	 * @return Returns the numeroElementos.
	 */
	public int getNumeroElementos()
	{
		return numeroElementos;
	}


	/**
	 * @param numeroElementos The numeroElementos to set.
	 */
	public void setNumeroElementos(int numeroElementos)
	{
		this.numeroElementos=numeroElementos;
	}


	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}


	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset=offset;
	}


	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}


	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}


	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
	}


	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}


	/**
	 * @return Returns the mapaServicios.
	 */
	public HashMap getMapaServicios()
	{
		return mapaServicios;
	}
	
	/**
	 * @param mapaServicios The mapaServicios to set.
	 */
	public void setMapaServicios(HashMap mapaServicios)
	{
		this.mapaServicios= mapaServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaServicios(String key, Object value) 
	{
		mapaServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaServicios(String key) 
	{
		return mapaServicios.get(key);
	}

	
	/**
	 * @return Returns the mapaEncabezadoDetalle.
	 */
	public HashMap getMapaEncabezadoDetalle()
	{
		return mapaEncabezadoDetalle;
	}
	
	/**
	 * @param mapaEncabezadoDetalle The mapaEncabezadoDetalle to set.
	 */
	public void setMapaEncabezadoDetalle(HashMap mapaEncabezadoDetalle)
	{
		this.mapaEncabezadoDetalle= mapaEncabezadoDetalle;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaEncabezadoDetalle(String key, Object value) 
	{
		mapaEncabezadoDetalle.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaEncabezadoDetalle(String key) 
	{
		return mapaEncabezadoDetalle.get(key);
	}
	
	/**
	 * @return Returns the mapaCuerpoDetalle.
	 */
	public HashMap getMapaCuerpoDetalle()
	{
		return mapaCuerpoDetalle;
	}
	
	/**
	 * @param mapaCuerpoDetalle The mapaCuerpoDetalle to set.
	 */
	public void setMapaCuerpoDetalle(HashMap mapaCuerpoDetalle)
	{
		this.mapaCuerpoDetalle= mapaCuerpoDetalle;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCuerpoDetalle(String key, Object value) 
	{
		mapaCuerpoDetalle.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCuerpoDetalle(String key) 
	{
		return mapaCuerpoDetalle.get(key);
	}
	
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		/*******************************************************************************/
		if(estado.equals("resultadoBusqueda"))
		{
			if(this.getCodigoInterno().trim().equals("")&&this.tipoTarifario.trim().equals("")&&this.getDescripcionServicio().trim().equals("")&&this.getCodigoServicio().trim().equals("")&&this.getCodigoEspecialidad()==-1&&this.getAcronimoTipoServicio().trim().equals("-1")&&this.getAcronimoNaturaleza().trim().equals("-1")&&this.getCodigoGrupoServicio()==-1)
			{
				errores.add("error.facturacion.ConsultaTarifasServicios.camposBusquedaNoDefinidos", new ActionMessage("error.facturacion.ConsultaTarifasServicios.camposBusquedaNoDefinidos"));
			}
		}
		return errores;
	}

	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	public String getTipoTarifario() {
		return tipoTarifario;
	}

	public void setTipoTarifario(String tipoTarifario) {
		this.tipoTarifario = tipoTarifario;
	}
	
}
