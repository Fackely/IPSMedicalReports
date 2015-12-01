package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


import org.apache.struts.validator.ValidatorForm;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;



import util.ConstantesBD;
import util.UtilidadTexto;

import util.Utilidades;

public class EquivalentesDeInventarioForm extends ValidatorForm{

	/**
	 * Codigo art equivalente a eliminar
	 */
	private String codigoEliminarPos;
	
	
	/**
	 * Variable para manejar los poUp de seleccion de articulo y articulo equivalente
	 */
	private String esArtEquiv;
	
	
	/**
	 * Manejo de las posiciones
	 */
	private int pos;
	
	private  String estado;
	
	/**
	 * cod articulo ppal
	 */
	private int articuloPpal;
	
	/**
	 *  descripcion del articulo principal
	 */
	private String descripcionArticuloPpal;
	
	/**
	 * Unidad de Medida del Art Principal
	 */
	private String umedidaP;
	
	/**
	 * Descripcion articulo equivalente
	 */
	private String descripcionArticuloEqui;
	
	/**
	 * Cod articulo equivalente
	 */
	private int articuloEquivalente;
	
	
	/**
	 * Mapa de cargado de datos
	 */
	private HashMap<String, Object> mapaEquiInventarios = new HashMap<String, Object>();
	
	/**
	 * Mapa para Campos Filtrar Busqueda Equivalentes
	 */
	private HashMap camposBusquedaMap;
	
	/**
	 * Mapa datos Adicionales Articulo Equivalente
	 */
	private HashMap datosAdMap;
	

	
	/**
	 * reset de la forma
	 *
	 */
	public void reset ()
	{
		this.codigoEliminarPos= "";
		this.estado = "";
		this.descripcionArticuloEqui="";
		this.umedidaP="";
		this.articuloEquivalente=ConstantesBD.codigoNuncaValido;
		this.articuloPpal=ConstantesBD.codigoNuncaValido;
		this.descripcionArticuloPpal="";
		this.mapaEquiInventarios = new HashMap<String, Object>();
		this.esArtEquiv = "";
		this.pos=0;
		this.camposBusquedaMap=new HashMap();
		camposBusquedaMap.put("numRegistros", 0);
		this.datosAdMap=new HashMap();
		datosAdMap.put("numRegistros", 0);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the articuloPpal
	 */
	public int getArticuloPpal() {
		return articuloPpal;
	}

	/**
	 * @param articuloPpal the articuloPpal to set
	 */
	public void setArticuloPpal(int articuloPpal) {
		this.articuloPpal = articuloPpal;
	}

	/**
	 * @return the descripcionArticuloPpal
	 */
	public String getDescripcionArticuloPpal() {
		return descripcionArticuloPpal;
	}

	/**
	 * @param descripcionArticuloPpal the descripcionArticuloPpal to set
	 */
	public void setDescripcionArticuloPpal(String descripcionArticuloPpal) {
		this.descripcionArticuloPpal = descripcionArticuloPpal;
	}

	/**
	 * @return the mapaEquiInventarios
	 */
	public HashMap<String, Object> getMapaEquiInventarios() {
		return mapaEquiInventarios;
	}

	/**
	 * @param mapaEquiInventarios the mapaEquiInventarios to set
	 */
	public void setMapaEquiInventarios(HashMap<String, Object> mapaEquiInventarios) {
		this.mapaEquiInventarios = mapaEquiInventarios;
	}
	
	
	public Object getMapaEquiInventarios(String key) {
		return mapaEquiInventarios.get(key);
	}

	public void setMapaEquiInventarios(String key,Object obj) {
		this.mapaEquiInventarios.put(key, obj);
	}

	/**
	 * @return the NumMapaEquiInventarios, numero de registros del mapa
	 */
	public int getNumMapaEquiInventarios() {
		return Utilidades.convertirAEntero(this.mapaEquiInventarios.get("numRegistros")+"", true);
	}

	/**
	 * @return the articuloEquivalente
	 */
	public int getArticuloEquivalente() {
		return articuloEquivalente;
	}

	/**
	 * @param articuloEquivalente the articuloEquivalente to set
	 */
	public void setArticuloEquivalente(int articuloEquivalente) {
		this.articuloEquivalente = articuloEquivalente;
	}

	/**
	 * @return the descripcionArticuloEqui
	 */
	public String getDescripcionArticuloEqui() {
		return descripcionArticuloEqui;
	}

	/**
	 * @param descripcionArticuloEqui the descripcionArticuloEqui to set
	 */
	public void setDescripcionArticuloEqui(String descripcionArticuloEqui) {
		this.descripcionArticuloEqui = descripcionArticuloEqui;
	}

	/**
	 * @return the codigoEliminarPos
	 */
	public String getCodigoEliminarPos() {
		return codigoEliminarPos;
	}

	/**
	 * @param codigoEliminarPos the codigoEliminarPos to set
	 */
	public void setCodigoEliminarPos(String codigoEliminarPos) {
		this.codigoEliminarPos = codigoEliminarPos;
	}

	
	/**
	 * Metodo de validacion de campos a guardar.
	 */
	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	    {
	        ActionErrors errores = new ActionErrors();
	        
	        HashMap<String, Object> tempMapArtEqui = new HashMap<String, Object>();
			tempMapArtEqui = getMapaEquiInventarios();			
			 if(this.estado.equals("guardar"))
		     {
				 for (int i=0; i < this.getNumMapaEquiInventarios(); i++)
				 {
					 if(this.getMapaEquiInventarios().containsKey("eliminar_"+i) && !UtilidadTexto.getBoolean(this.getMapaEquiInventarios("eliminar_"+i).toString()))
					 {
						 if(this.getMapaEquiInventarios().containsKey("artEquiCantidad_"+i) && this.getMapaEquiInventarios("artEquiCantidad_"+i).toString().equals(""))
						 {
							 errores.add("",new ActionMessage("errors.required","La cantidad equivalente en el registro N° "+(i+1)));
						 }
						 if(this.getMapaEquiInventarios().containsKey("DescripcionArtEquivalente_"+i) && this.getMapaEquiInventarios("DescripcionArtEquivalente_"+i).toString().equals(""))
						 {
							 errores.add("",new ActionMessage("errors.required","La Descripción del Articulo Equivalente en el registro N° "+(i+1)));
						 }
					 }
				 }
				 
				 if(!errores.isEmpty())
		        		this.estado = "";
				 
		     }
			
			return errores;
	    }

	/**
	 * @return the esArtEquivalente
	 */
	public String getEsArtEquiv() {
		return esArtEquiv;
	}

	/**
	 * @param esArtEquivalente the esArtEquivalente to set
	 */
	public void setEsArtEquiv(String esArtEquiv) {
		this.esArtEquiv = esArtEquiv;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getUmedidaP() {
		return umedidaP;
	}

	public void setUmedidaP(String umedidaP) {
		this.umedidaP = umedidaP;
	}

	public HashMap getCamposBusquedaMap() {
		return camposBusquedaMap;
	}

	public void setCamposBusquedaMap(HashMap camposBusquedaMap) {
		this.camposBusquedaMap = camposBusquedaMap;
	}

	public HashMap getDatosAdMap() {
		return datosAdMap;
	}

	public void setDatosAdMap(HashMap datosAdMap) {
		this.datosAdMap = datosAdMap;
	}
}
