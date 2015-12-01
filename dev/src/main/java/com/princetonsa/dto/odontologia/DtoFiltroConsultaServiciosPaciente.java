/**
 * 
 */
package com.princetonsa.dto.odontologia;

/**
 * @author armando
 *
 */
public class DtoFiltroConsultaServiciosPaciente 
{
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private int unidadAgenda;
	
	/**
	 * 
	 */
	private int codigoMedico;
	
	/**
	 * 
	 */
	private int codigoPaciente;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private String validadrPresupuestoContratado;
	
	/**
	 * 
	 */
	private String cambiarServicioOdontologico;

	/**
	 * 
	 */
	private boolean ordenarPorPHP;
	
	/**
	 * 
	 */
	private String casoBusquedaServicio;
	
	/**
	 * 
	 */
	private int codigoCita;
	
	/**
	 * 
	 */
	private boolean buscarPlanTratamiento;

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getUnidadAgenda() {
		return unidadAgenda;
	}

	public void setUnidadAgenda(int unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	public int getCodigoMedico() {
		return codigoMedico;
	}

	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getValidadrPresupuestoContratado() {
		return validadrPresupuestoContratado;
	}

	public void setValidadrPresupuestoContratado(
			String validadrPresupuestoContratado) {
		this.validadrPresupuestoContratado = validadrPresupuestoContratado;
	}

	public String getCambiarServicioOdontologico() {
		return cambiarServicioOdontologico;
	}

	public void setCambiarServicioOdontologico(String cambiarServicioOdontologico) {
		this.cambiarServicioOdontologico = cambiarServicioOdontologico;
	}

	public String getCasoBusquedaServicio() {
		return casoBusquedaServicio;
	}

	public void setCasoBusquedaServicio(String casoBusquedaServicio) {
		this.casoBusquedaServicio = casoBusquedaServicio;
	}

	public int getCodigoCita() {
		return codigoCita;
	}

	public void setCodigoCita(int codigoCita) {
		this.codigoCita = codigoCita;
	}

	public boolean isBuscarPlanTratamiento() {
		return buscarPlanTratamiento;
	}

	public void setBuscarPlanTratamiento(boolean buscarPlanTratamiento) {
		this.buscarPlanTratamiento = buscarPlanTratamiento;
	}

	public boolean isOrdenarPorPHP() {
		return ordenarPorPHP;
	}

	public void setOrdenarPorPHP(boolean ordenarPorPHP) {
		this.ordenarPorPHP = ordenarPorPHP;
	}

}
