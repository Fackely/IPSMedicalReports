/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Cristhian Murillo, Fabian Becerra, Camilo gomez
 */
public class DtoInconsistenciasProcesoPresupuestoCapitado implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** * */
	private String servicioMedicamento;
	
	/** * */
	private String tipoInconsistencia;
	
	/** * */
	private String descripcion;
	
	/** Fecha del proceso */
	private Date fecha;
	
	/** * Convenio */
	private Integer codigoConvenio;
	
	/** * contrato */
	private Integer codigoContrato;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoInconsistenciasProcesoPresupuestoCapitado() {
		this.reset();
	}
	
	/** * */
	private void reset() 
	{ 
		this.servicioMedicamento="";
		this.descripcion="";
		this.tipoInconsistencia="";
		this.fecha = null;
		this.codigoContrato=null;
		this.codigoConvenio=null;
		
	}

	public String getServicioMedicamento() {
		return servicioMedicamento;
	}

	public void setServicioMedicamento(String servicioMedicamento) {
		this.servicioMedicamento = servicioMedicamento;
	}

	public String getTipoInconsistencia() {
		return tipoInconsistencia;
	}

	public void setTipoInconsistencia(String tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	
}
