package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;

public class DtoDetalleAliadoOdontologico implements Serializable{
	
	
	/**
	 * atributos
	 */
	
	private double codigoPk;
	private String codigo;
	private int institucion;
	private int tercero;
	private String descripcion;
	private String direccion;
	private String telefono;
	private String observaciones;
	private String estado;
	private String eliminado;
	private String fechaInactivacion;
	private String horaInactivacion;
	private String usuarioInactivacion;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
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
	 * @return the tercero
	 */
	public int getTercero() {
		return tercero;
	}
	/**
	 * @param tercero the tercero to set
	 */
	public void setTercero(int tercero) {
		this.tercero = tercero;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono the telefono to set
	 */
	
	public void setTelefono(String string) {
		
		this.telefono = telefono;
		
	}
	
	
	
	
	
	
	
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	 * @return the eliminado
	 */
	public String getEliminado() {
		return eliminado;
	}
	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}
	/**
	 * @return the fechaInactivacion
	 */
	public String getFechaInactivacion() {
		return fechaInactivacion;
	}
	/**
	 * @param fechaInactivacion the fechaInactivacion to set
	 */
	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}
	/**
	 * @return the horaInactivacion
	 */
	public String getHoraInactivacion() {
		return horaInactivacion;
	}
	/**
	 * @param horaInactivacion the horaInactivacion to set
	 */
	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}
	/**
	 * @return the usuarioInactivacion
	 */
	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}
	/**
	 * @param usuarioInactivacion the usuarioInactivacion to set
	 */
	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}
	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	
	

	
	
	
}
