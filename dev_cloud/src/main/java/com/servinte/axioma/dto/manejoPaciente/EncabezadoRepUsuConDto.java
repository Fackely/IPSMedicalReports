package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;

/**
 * @author ricruico
 *
 */
public class EncabezadoRepUsuConDto implements Serializable{


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6951706907991508820L;
	
	private long codigoEmpresaInstitucion;
	private String rutaLogo;
	private String ubicacionLogo;
	private String logoIzquierda;
	private String logoDerecha;
	private String nombreArchivoGenerado;
    private boolean saltoPaginaReporte;
    private String tipoImpresion;
	private int institucion;
	private String razonSocial;
	private String nit;
	private String direccion;	
	private String telefono;
	private String indicativo;
	private String actividadEconomica;	
	private String usuario;	
	private String centroAtencion;
	

	public EncabezadoRepUsuConDto(){
	this.reset();
		}
	public void reset()
	{
				
	}
	/**
	 * @return the codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}
	/**
	 * @param codigoEmpresaInstitucion the codigoEmpresaInstitucion to set
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}
	/**
	 * @return the rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}
	/**
	 * @param rutaLogo the rutaLogo to set
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	/**
	 * @return the ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}
	/**
	 * @param ubicacionLogo the ubicacionLogo to set
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}
	/**
	 * @return the logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	/**
	 * @param logoIzquierda the logoIzquierda to set
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	/**
	 * @return the logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}
	/**
	 * @param logoDerecha the logoDerecha to set
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}
	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}
	/**
	 * @return the saltoPaginaReporte
	 */
	public boolean isSaltoPaginaReporte() {
		return saltoPaginaReporte;
	}
	/**
	 * @param saltoPaginaReporte the saltoPaginaReporte to set
	 */
	public void setSaltoPaginaReporte(boolean saltoPaginaReporte) {
		this.saltoPaginaReporte = saltoPaginaReporte;
	}
	/**
	 * @return the tipoImpresion
	 */
	public String getTipoImpresion() {
		return tipoImpresion;
	}
	/**
	 * @param tipoImpresion the tipoImpresion to set
	 */
	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
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
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}
	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
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
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	/**
	 * @return the indicativo
	 */
	public String getIndicativo() {
		return indicativo;
	}
	/**
	 * @param indicativo the indicativo to set
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}
	/**
	 * @return the actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}
	/**
	 * @param actividadEconomica the actividadEconomica to set
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


}