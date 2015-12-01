package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.Utilidades;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */
public class DtoConvencionesOdontologicas implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int consecutivo;
	private String codigo;
	private String nombre;
	private String archivoConvencion;
	private String tipo;
	private String color;
	private String borde;
	private int trama;
	private String nombreTrama;
	private String archivoTrama;
	private int imagen;
	private String nombreImagen;
	private String archivoImagen;
	private int institucion;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String activo;
	private String imagenBits;
	
	public DtoConvencionesOdontologicas()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigo = "";
		this.nombre = "";
		this.archivoConvencion = "";
		this.tipo = "";
		this.color = "";
		this.borde = "";
		this.trama = ConstantesBD.codigoNuncaValido;
		this.nombreTrama="";
		this.archivoTrama="";
		this.imagen = ConstantesBD.codigoNuncaValido;
		this.nombreImagen="";
		this.archivoImagen="";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.fechaModificacion = "";
		this.horaModificacion = "";
		this.usuarioModificacion = "";
		//Por anexo 134667 se especifica q se debe postular la convencion como activa
		this.activo = ConstantesBD.acronimoSi;
		this.imagenBits = "";
	}

	public void resetColor()
	{
		this.color= "";
	}
	
	public void resetTrama()
	{
		this.trama = ConstantesBD.codigoNuncaValido;
		this.nombreTrama="";
		this.archivoTrama="";
	}
	
	public void resetImagen()
	{
		this.imagen = ConstantesBD.codigoNuncaValido;
		this.nombreImagen="";
		this.archivoImagen="";
	}
	
	public void resetBorde()
	{
		this.borde = "";
	}
	
	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	public int getCodigoInt() {
		return Utilidades.convertirAEntero(codigo);
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
	 * @return the archivoConvencion
	 */
	public String getArchivoConvencion() {
		return archivoConvencion;
	}

	/**
	 * @param archivoConvencion the archivoConvencion to set
	 */
	public void setArchivoConvencion(String archivoConvencion) {
		this.archivoConvencion = archivoConvencion;
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
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the trama
	 */
	public int getTrama() {
		return trama;
	}

	/**
	 * @param trama the trama to set
	 */
	public void setTrama(int trama) {
		this.trama = trama;
	}

	/**
	 * @return the imagen
	 */
	public int getImagen() {
		return imagen;
	}

	/**
	 * @param imagen the imagen to set
	 */
	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}

	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the nombreTrama
	 */
	public String getNombreTrama() {
		return nombreTrama;
	}

	/**
	 * @param nombreTrama the nombreTrama to set
	 */
	public void setNombreTrama(String nombreTrama) {
		this.nombreTrama = nombreTrama;
	}

	/**
	 * @return the nombreImagen
	 */
	public String getNombreImagen() {
		return nombreImagen;
	}

	/**
	 * @param nombreImagen the nombreImagen to set
	 */
	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	/**
	 * @return the archivoTrama
	 */
	public String getArchivoTrama() {
		return archivoTrama;
	}

	/**
	 * @param archivoTrama the archivoTrama to set
	 */
	public void setArchivoTrama(String archivoTrama) {
		this.archivoTrama = archivoTrama;
	}

	/**
	 * @return the archivoImagen
	 */
	public String getArchivoImagen() {
		return archivoImagen;
	}

	/**
	 * @param archivoImagen the archivoImagen to set
	 */
	public void setArchivoImagen(String archivoImagen) {
		this.archivoImagen = archivoImagen;
	}

	public String getImagenBits() {
		return imagenBits;
	}

	public void setImagenBits(String imagenBits) {
		this.imagenBits = imagenBits;
	}
	
	/**
	 * @param borde the borde to set
	 */
	public void setBorde(String borde) {
		this.borde = borde;
	}

	/**
	 * @return the borde
	 */
	public String getBorde() {
		return borde;
	}
	
}
