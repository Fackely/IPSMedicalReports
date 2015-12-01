package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

public class DtoParamCamposJusNoPos implements Serializable
{
	private String codigoParam;
	private String codigo;
	private String nombre;
	private String etiqueta;
	private String tipo;
	private String tipoHtml;
	private String orden;
	private String requerido;
	private String mostrar;
	private String columnas;
	private String valor;
	private String etiquetaValor;
	private String tamanio;
	private String longitud;
	private String alineacion;
	private String fijo;
	private boolean tieneAccion=false;
	private boolean disabled=false;
	private boolean tieneDatos=false;
	
	private ArrayList<DtoParamOpcionesCamposJusNoPos> opciones;
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.codigoParam="";
		this.codigo="";
		this.nombre="";
		this.etiqueta="";
		this.tipo="";
		this.tipoHtml="";
		this.orden="";
		this.requerido="";
		this.mostrar="";
		this.columnas="";
		this.valor="";
		this.etiquetaValor="";
		this.tamanio="";
		this.longitud="";
		this.alineacion="";
		this.fijo="";
		this.opciones = new ArrayList<DtoParamOpcionesCamposJusNoPos>();
		this.tieneDatos=false;
	}
	
	/**
	 * 
	 */
	public DtoParamCamposJusNoPos()
	{
		this.clean();
	}
	
	/**
	 * Método que devuelve el nombre de la funcion javascript que debe ejecutar una opcion para mostrar una seccion
	 * @return
	 */
	public String onChangeMostrarSeccion(){
		String onChangeMostrarSeccion="";
		boolean ponerComa = false;
		for(int i=0; i<this.opciones.size() ; i++){
			if (!this.opciones.get(i).getMostrarSeccion().isEmpty()){
				if(ponerComa)
					onChangeMostrarSeccion+=",";
				onChangeMostrarSeccion += "mostrarSeccion('divSeccion_"+this.opciones.get(i).getMostrarSeccion()+"', 'campo_"+this.codigo+"_opcion_"+this.opciones.get(i).getCodigo()+"')";
				ponerComa=true;
			}
		}
		return onChangeMostrarSeccion;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the orden
	 */
	public String getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}

	/**
	 * @return the requerido
	 */
	public String getRequerido() {
		return requerido;
	}

	/**
	 * @param requerido the requerido to set
	 */
	public void setRequerido(String requerido) {
		this.requerido = requerido;
	}

	/**
	 * @return the mostrar
	 */
	public String getMostrar() {
		return mostrar;
	}

	/**
	 * @param mostrar the mostrar to set
	 */
	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}

	/**
	 * @return the columnas
	 */
	public String getColumnas() {
		return columnas;
	}

	/**
	 * @param columnas the columnas to set
	 */
	public void setColumnas(String columnas) {
		this.columnas = columnas;
	}



	/**
	 * @return the codigoParam
	 */
	public String getCodigoParam() {
		return codigoParam;
	}

	/**
	 * @param codigoParam the codigoParam to set
	 */
	public void setCodigoParam(String codigoParam) {
		this.codigoParam = codigoParam;
	}

	/**
	 * @return the tipoHtml
	 */
	public String getTipoHtml() {
		return tipoHtml;
	}

	/**
	 * @param tipoHtml the tipoHtml to set
	 */
	public void setTipoHtml(String tipoHtml) {
		this.tipoHtml = tipoHtml;
	}

	/**
	 * @return the opciones
	 */
	public ArrayList<DtoParamOpcionesCamposJusNoPos> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(ArrayList<DtoParamOpcionesCamposJusNoPos> opciones) {
		this.opciones = opciones;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the etiquetaValor
	 */
	public String getEtiquetaValor() {
		return etiquetaValor;
	}

	/**
	 * @param etiquetaValor the etiquetaValor to set
	 */
	public void setEtiquetaValor(String etiquetaValor) {
		this.etiquetaValor = etiquetaValor;
	}

	/**
	 * @return the tamanio
	 */
	public String getTamanio() {
		return tamanio;
	}

	/**
	 * @param tamanio the tamanio to set
	 */
	public void setTamanio(String tamanio) {
		this.tamanio = tamanio;
	}

	/**
	 * @return the longitud
	 */
	public String getLongitud() {
		return longitud;
	}

	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	/**
	 * @return the alineacion
	 */
	public String getAlineacion() {
		return alineacion;
	}

	/**
	 * @param alineacion the alineacion to set
	 */
	public void setAlineacion(String alineacion) {
		this.alineacion = alineacion;
	}

	/**
	 * @return the fijo
	 */
	public String getFijo() {
		return fijo;
	}

	/**
	 * @param fijo the fijo to set
	 */
	public void setFijo(String fijo) {
		this.fijo = fijo;
	}

	public boolean isTieneAccion() {
		return tieneAccion;
	}

	public void setTieneAccion(boolean tieneAccion) {
		this.tieneAccion = tieneAccion;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the tieneDatos
	 */
	public boolean isTieneDatos() {
		return tieneDatos;
	}

	/**
	 * @param tieneDatos the tieneDatos to set
	 */
	public void setTieneDatos(boolean tieneDatos) {
		this.tieneDatos = tieneDatos;
	}
	
	
	
}
