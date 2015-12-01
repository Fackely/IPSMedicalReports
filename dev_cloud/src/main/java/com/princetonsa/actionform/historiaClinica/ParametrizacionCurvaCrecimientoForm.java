package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;

public class ParametrizacionCurvaCrecimientoForm extends ValidatorForm 
{
	private static final long serialVersionUID = 1L;
	private List<CurvaCrecimientoParametrizabDto> curvasExistentes;
	private String tituloGrafica;
	private String colorTitulo="#000000";
	private String descripcion;
	private String colorDescripcion="#000000";
	private Integer edadInicial;
	private Integer edadFinal;
	private Boolean activo;
	private Boolean indicadorError;
	private Date fechaCreacion;
	private FormFile imagenIzquierda;
	private FormFile imagenDerecha;
	private FormFile imagenCurva;
	private List<String> listaSexos;
	private String sexoSeleccionado;
	private CurvaCrecimientoParametrizabDto curvaSeleccionada;
	private Integer idCurvaSeleccionada;
	private List<String> mensajesError = new ArrayList<String>();
	private List<String> mensajesInformacion = new ArrayList<String>();
	private Boolean hayMensajes;
	private Boolean hayMensajesInformacion;
	private int ubicacion;
	private List<String> listaSexosBusqueda = new ArrayList<String>();
	private List<String> activoBusqueda = new ArrayList<String>();
	private List<String> indicadorErrorBusqueda = new ArrayList<String>();
	private String sexoSeleccionadoBusqueda;
	private String activoSeleccionadoBusqueda;
	private String indicadorErrorSeleccionadoBusqueda;
	private String criterioOrden;
	
	public List<CurvaCrecimientoParametrizabDto> getCurvasExistentes() {
		return curvasExistentes;
	}
	public void setCurvasExistentes(
			List<CurvaCrecimientoParametrizabDto> curvasExistentes) {
		this.curvasExistentes = curvasExistentes;
	}
	public String getTituloGrafica() {
		return tituloGrafica;
	}
	public void setTituloGrafica(String tituloGrafica) {
		this.tituloGrafica = tituloGrafica;
	}
	public String getColorTitulo() {
		return colorTitulo;
	}
	public void setColorTitulo(String colorTitulo) {
		this.colorTitulo = colorTitulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getColorDescripcion() {
		return colorDescripcion;
	}
	public void setColorDescripcion(String colorDescripcion) {
		this.colorDescripcion = colorDescripcion;
	}
	public Integer getEdadInicial() {
		return edadInicial;
	}
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
	}
	public Integer getEdadFinal() {
		return edadFinal;
	}
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	public Boolean getIndicadorError() {
		return indicadorError;
	}
	public void setIndicadorError(Boolean indicadorError) {
		this.indicadorError = indicadorError;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public FormFile getImagenIzquierda() {
		return imagenIzquierda;
	}
	public void setImagenIzquierda(FormFile imagenIzquierda) {
		this.imagenIzquierda = imagenIzquierda;
	}
	public FormFile getImagenDerecha() {
		return imagenDerecha;
	}
	public void setImagenDerecha(FormFile imagenDerecha) {
		this.imagenDerecha = imagenDerecha;
	}
	public FormFile getImagenCurva() {
		return imagenCurva;
	}
	public void setImagenCurva(FormFile imagenCurva) {
		this.imagenCurva = imagenCurva;
	}
	public List<String> getListaSexos() {
		return listaSexos;
	}
	public void setListaSexos(List<String> listaSexos) {
		this.listaSexos = listaSexos;
	}
	public String getSexoSeleccionado() {
		return sexoSeleccionado;
	}
	public void setSexoSeleccionado(String sexoSeleccionado) {
		this.sexoSeleccionado = sexoSeleccionado;
	}
	public CurvaCrecimientoParametrizabDto getCurvaSeleccionada() {
		return curvaSeleccionada;
	}
	public void setCurvaSeleccionada(
			CurvaCrecimientoParametrizabDto curvaSeleccionada) {
		this.curvaSeleccionada = curvaSeleccionada;
	}
	public Integer getIdCurvaSeleccionada() {
		return idCurvaSeleccionada;
	}
	public void setIdCurvaSeleccionada(Integer idCurvaSeleccionada) {
		this.idCurvaSeleccionada = idCurvaSeleccionada;
	}
	public List<String> getMensajesError() {
		return mensajesError;
	}
	public void setMensajesError(List<String> mensajesError) {
		this.mensajesError = mensajesError;
	}
	public void addMensajeError(String mensaje){
		mensajesError.add(""+mensaje);
		hayMensajes = true;
	}
	public void addMensajeInformacion(String mensaje){
		mensajesInformacion.add(""+mensaje);
		hayMensajesInformacion = true;
	}
	public Boolean getHayMensajes() {
		return hayMensajes;
	}
	public void setHayMensajes(Boolean hayMensajes) {
		this.hayMensajes = hayMensajes;
	}
	public int getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(int ubicacion) {
		this.ubicacion = ubicacion;
	}
	public List<String> getListaSexosBusqueda() {
		return listaSexosBusqueda;
	}
	public void setListaSexosBusqueda(List<String> listaSexosBusqueda) {
		this.listaSexosBusqueda = listaSexosBusqueda;
	}
	public List<String> getActivoBusqueda() {
		return activoBusqueda;
	}
	public void setActivoBusqueda(List<String> activoBusqueda) {
		this.activoBusqueda = activoBusqueda;
	}
	public List<String> getIndicadorErrorBusqueda() {
		return indicadorErrorBusqueda;
	}
	public void setIndicadorErrorBusqueda(List<String> indicadorErrorBusqueda) {
		this.indicadorErrorBusqueda = indicadorErrorBusqueda;
	}
	public String getSexoSeleccionadoBusqueda() {
		return sexoSeleccionadoBusqueda;
	}
	public void setSexoSeleccionadoBusqueda(String sexoSeleccionadoBusqueda) {
		this.sexoSeleccionadoBusqueda = sexoSeleccionadoBusqueda;
	}
	public String getActivoSeleccionadoBusqueda() {
		return activoSeleccionadoBusqueda;
	}
	public void setActivoSeleccionadoBusqueda(String activoSeleccionadoBusqueda) {
		this.activoSeleccionadoBusqueda = activoSeleccionadoBusqueda;
	}
	public String getIndicadorErrorSeleccionadoBusqueda() {
		return indicadorErrorSeleccionadoBusqueda;
	}
	public void setIndicadorErrorSeleccionadoBusqueda(
			String indicadorErrorSeleccionadoBusqueda) {
		this.indicadorErrorSeleccionadoBusqueda = indicadorErrorSeleccionadoBusqueda;
	}
	public List<String> getMensajesInformacion() {
		return mensajesInformacion;
	}
	public void setMensajesInformacion(List<String> mensajesInformacion) {
		this.mensajesInformacion = mensajesInformacion;
	}
	public Boolean getHayMensajesInformacion() {
		return hayMensajesInformacion;
	}
	public void setHayMensajesInformacion(Boolean hayMensajesInformacion) {
		this.hayMensajesInformacion = hayMensajesInformacion;
	}
	public String getCriterioOrden() {
		return criterioOrden;
	}
	public void setCriterioOrden(String criterioOrden) {
		this.criterioOrden = criterioOrden;
	}
	public boolean getHayImagenIzq(){
		if(curvaSeleccionada!=null && curvaSeleccionada.getDtoImagenesParametrizadas()!=null && curvaSeleccionada.getDtoImagenesParametrizadas().getImagenIzquierda()!=null && !curvaSeleccionada.getDtoImagenesParametrizadas().getImagenIzquierda().equals(""))
			return true;
		return false;
	}
	public boolean getHayImagenDer(){
		if(curvaSeleccionada!=null && curvaSeleccionada.getDtoImagenesParametrizadas()!=null && curvaSeleccionada.getDtoImagenesParametrizadas().getImagenDerecha()!=null && !curvaSeleccionada.getDtoImagenesParametrizadas().getImagenDerecha() .equals(""))
			return true;
		return false;
	}
	public boolean getHayImagenCurv(){
		if(curvaSeleccionada!=null && curvaSeleccionada.getDtoImagenesParametrizadas()!=null && curvaSeleccionada.getDtoImagenesParametrizadas().getImagenCurva()!=null && !curvaSeleccionada.getDtoImagenesParametrizadas().getImagenCurva().equals(""))
			return true;
		return false;
	}
	
	public void reset1() {
		curvasExistentes = null;
		idCurvaSeleccionada = null;
		tituloGrafica = null;
		colorTitulo = null;
		descripcion = null;
		colorDescripcion = null;
		edadInicial = null;
		edadFinal = null;
		activo = false;
		indicadorError = false;
		fechaCreacion = null;
		imagenIzquierda = null;
		imagenDerecha = null;
		imagenCurva = null;
		sexoSeleccionado = null;
		curvaSeleccionada = null;
	}
	
	public void reset2() {
		curvasExistentes = null;
		idCurvaSeleccionada = null;
		tituloGrafica = null;
		colorTitulo = "#000000";
		descripcion = null;
		colorDescripcion = "#000000";
		edadInicial = null;
		edadFinal = null;
		activo = false;
		indicadorError = false;
		fechaCreacion = null;
		imagenIzquierda = null;
		imagenDerecha = null;
		imagenCurva = null;
		sexoSeleccionado = null;
		mensajesError = new ArrayList<String>();
		mensajesInformacion = new ArrayList<String>();
		curvaSeleccionada = null;
		hayMensajes = false;
		hayMensajesInformacion = false;
		listaSexosBusqueda = new ArrayList<String>();
		activoBusqueda = new ArrayList<String>();
		indicadorErrorBusqueda = new ArrayList<String>();
		sexoSeleccionadoBusqueda = null;
		activoSeleccionadoBusqueda = null;
		indicadorErrorSeleccionadoBusqueda = null;
	}
	
	public void resetBusqueda() {
		curvasExistentes = null;
		idCurvaSeleccionada = null;
		colorTitulo = "#000000";
		colorDescripcion = "#000000";
		activo = false;
		indicadorError = false;
		fechaCreacion = null;
		imagenIzquierda = null;
		imagenDerecha = null;
		imagenCurva = null;
		sexoSeleccionado = null;
		mensajesError = new ArrayList<String>();
		mensajesInformacion = new ArrayList<String>();
		curvaSeleccionada = null;
		hayMensajes = false;
		hayMensajesInformacion = false;
		listaSexosBusqueda = new ArrayList<String>();
		activoBusqueda = new ArrayList<String>();
		indicadorErrorBusqueda = new ArrayList<String>();
	}
	
	public void resetMensajes() {
		mensajesError = new ArrayList<String>();
		mensajesInformacion = new ArrayList<String>();
		hayMensajes = false;
		hayMensajesInformacion = false;
	}
}