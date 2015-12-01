package com.servinte.axioma.dto.manejoPaciente;

import java.util.List;

import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;

/**
 * @author DiaRuiPe
 * @version 1.0
 * @created 10-jul-2012 03:34:35 p.m.
 */
public class AutorizarServMedicInsuIngreEstanciaDto {

	/**Atributo que almacena el convenio responsable de la autorización de IngresoEstancia */
	private Integer codConvenioResponsable;
	
	/**Atributo que almacena el contrato del convenio responsable */
	private Integer codContratoConvenioResponsable;
	
	/**Atributo que almacena los servicios pendientes por autorizar de la orden */
	private List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar;
	
	/**Atributo que almacena los Medicamentos/Insumos pendientes por autorizar de la orden */
	private List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosPorAutorizar;
	
	/**Atributo que almacena el codigo de la vía de ingreso del paciente */
	private int viaIngreso;
	
	/**Atributo que almacena la clasificación socieconomica del paciente */
	private int clasificacionSocioecon;
	
	public AutorizarServMedicInsuIngreEstanciaDto(){
		
	}
	
	/**
	 * @return codConvenioResponsable
	 */
	public Integer getCodConvenioResponsable() {
		return codConvenioResponsable;
	}
	
	/**
	 * @param codConvenioResponsable
	 */
	public void setCodConvenioResponsable(Integer codConvenioResponsable) {
		this.codConvenioResponsable = codConvenioResponsable;
	}
	
	/**
	 * @return codContratoConvenioResponsable
	 */
	public Integer getCodContratoConvenioResponsable() {
		return codContratoConvenioResponsable;
	}
	
	/**
	 * @param codContratoConvenioResponsable
	 */
	public void setCodContratoConvenioResponsable(
			Integer codContratoConvenioResponsable) {
		this.codContratoConvenioResponsable = codContratoConvenioResponsable;
	}
	
	/**
	 * @return serviciosPorAutorizar
	 */
	public List<ServicioAutorizacionOrdenDto> getServiciosPorAutorizar() {
		return serviciosPorAutorizar;
	}
	
	/**
	 * @param serviciosPorAutorizar
	 */
	public void setServiciosPorAutorizar(List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar) {
		this.serviciosPorAutorizar = serviciosPorAutorizar;
	}
	
	/**
	 * @return medicamentosInsumosPorAutorizar
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> getMedicamentosInsumosPorAutorizar() {
		return medicamentosInsumosPorAutorizar;
	}
	
	/**
	 * @param medicamentosInsumosPorAutorizar
	 */
	public void setMedicamentosInsumosPorAutorizar(
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosPorAutorizar) {
		this.medicamentosInsumosPorAutorizar = medicamentosInsumosPorAutorizar;
	}
	
	/**
	 * @return viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}
	
	/**
	 * @param viaIngreso
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	
	/**
	 * @return clasificacionSociecon
	 */
	public int getClasificacionSocioecon() {
		return clasificacionSocioecon;
	}
	
	/**
	 * @param clasificacionSocioecon
	 */
	public void setClasificacionSocioecon(int clasificacionSocioecon) {
		this.clasificacionSocioecon = clasificacionSocioecon;
	}
}