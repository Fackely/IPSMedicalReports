/**
 * 
 */
package com.princetonsa.dto.enfermeria;

/**
 * @author axioma
 *
 */
public class DtoAdministracionMedicamentosBasico 
{
	
	/**
	 * 
	 */
	private int codigoArticulo;
	
	/**
	 * 
	 */
	private String descripcionArticulo;
	
	/**
	 * 
	 */
	private int unidadesConsumidasXPaciente;
	
	/**
	 * 
	 */
	private int unidadesConsumidasXFarmacia;
	
	/**
	 * 
	 */
	private String fechaHoraUltimaAdministracion;
	
	/**
	 * 
	 */
	private String fechaHoraUsuarioUltimoRegAdministracion;
	
	/**
	 * 
	 */
	private String observacionesUltimaAdministracion;
	
	/**
	 * 
	 */
	private String esMedicamento;

	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public int getUnidadesConsumidasXPaciente() {
		return unidadesConsumidasXPaciente;
	}

	public void setUnidadesConsumidasXPaciente(int unidadesConsumidasXPaciente) {
		this.unidadesConsumidasXPaciente = unidadesConsumidasXPaciente;
	}

	public int getUnidadesConsumidasXFarmacia() {
		return unidadesConsumidasXFarmacia;
	}

	public void setUnidadesConsumidasXFarmacia(int unidadesConsumidasXFarmacia) {
		this.unidadesConsumidasXFarmacia = unidadesConsumidasXFarmacia;
	}

	public String getFechaHoraUltimaAdministracion() {
		return fechaHoraUltimaAdministracion;
	}

	public void setFechaHoraUltimaAdministracion(
			String fechaHoraUltimaAdministracion) {
		this.fechaHoraUltimaAdministracion = fechaHoraUltimaAdministracion;
	}

	public String getFechaHoraUsuarioUltimoRegAdministracion() {
		return fechaHoraUsuarioUltimoRegAdministracion;
	}

	public void setFechaHoraUsuarioUltimoRegAdministracion(
			String fechaHoraUsuarioUltimoRegAdministracion) {
		this.fechaHoraUsuarioUltimoRegAdministracion = fechaHoraUsuarioUltimoRegAdministracion;
	}

	public String getObservacionesUltimaAdministracion() {
		return observacionesUltimaAdministracion;
	}

	public void setObservacionesUltimaAdministracion(
			String observacionesUltimaAdministracion) {
		this.observacionesUltimaAdministracion = observacionesUltimaAdministracion;
	}

	public String getEsMedicamento() {
		return esMedicamento;
	}

	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}

}
