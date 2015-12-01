/**
 * 
 */
package com.princetonsa.dto.historiaClinica;

/**
 * @author armando
 *
 */
public class DtoMotivosNoConsentimientoInformado 
{
	/**
	 * 
	 */
	 private int codigoPk;
	 
	/**
	 * 
	 */
	 private String descripcion;
	 
	/**
	 * 
	 */
	 private String activo;
	 
	/**
	 * 
	 */
	 private int institucion;
	 
	/**
	 * 
	 */
	 private String usuarioModifica;
	 
	/**
	 * 
	 */
	 private String fechaModifica;
	 
	/**
	 * 
	 */
	 private String horaModifica;
	 
	 /**
	  * 
	  */
	 private boolean puedoEliminar;

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public boolean isPuedoEliminar() {
		return puedoEliminar;
	}

	public void setPuedoEliminar(boolean puedoEliminar) {
		this.puedoEliminar = puedoEliminar;
	}

}
