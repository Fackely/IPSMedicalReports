/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.util.ArrayList;

/**
 * @author armando
 *
 */
public class DtoConsentimientoInformadoOdonto 
{
	
	private int codigoPk;
	
	private int ingreso;
	
	private int planTratamiento;
	
	private int codigoProgramaHallazgoPieza;
	
	private String recibioConsentimiento;
	
	private int motivoNoConsentimiento;
	
	private String descripcionMotivoNoConsentimiento;
	
	private String usuarioModifica;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	private int codigoPrograma;
	
	private String descripcionPrograma;
	
	private boolean existeBd;
	
	private String fechaImpresionConsentimiento;
	
	private String horaImpresionConsentimiento;
	
	private String usuarioImpresionConsentimiento;
	
	/**
	 * 
	 */
	private ArrayList<Integer> codigosPHPRelacionados;
	

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public int getPlanTratamiento() {
		return planTratamiento;
	}

	public void setPlanTratamiento(int planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	public int getCodigoProgramaHallazgoPieza() {
		return codigoProgramaHallazgoPieza;
	}

	public void setCodigoProgramaHallazgoPieza(int codigoProgramaHallazgoPieza) {
		this.codigoProgramaHallazgoPieza = codigoProgramaHallazgoPieza;
	}

	public String getRecibioConsentimiento() {
		return recibioConsentimiento;
	}

	public void setRecibioConsentimiento(String recibioConsentimiento) {
		this.recibioConsentimiento = recibioConsentimiento;
	}

	public int getMotivoNoConsentimiento() {
		return motivoNoConsentimiento;
	}

	public void setMotivoNoConsentimiento(int motivoNoConsentimiento) {
		this.motivoNoConsentimiento = motivoNoConsentimiento;
	}

	public String getDescripcionMotivoNoConsentimiento() {
		return descripcionMotivoNoConsentimiento;
	}

	public void setDescripcionMotivoNoConsentimiento(
			String descripcionMotivoNoConsentimiento) {
		this.descripcionMotivoNoConsentimiento = descripcionMotivoNoConsentimiento;
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

	public int getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(int codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public String getDescripcionPrograma() {
		return descripcionPrograma;
	}

	public void setDescripcionPrograma(String descripcionPrograma) {
		this.descripcionPrograma = descripcionPrograma;
	}

	public boolean isExisteBd() {
		return existeBd;
	}

	public void setExisteBd(boolean existeBd) {
		this.existeBd = existeBd;
	}

	
	public String getFechaImpresionConsentimiento() {
		return fechaImpresionConsentimiento;
	}

	public void setFechaImpresionConsentimiento(String fechaImpresionConsentimiento) {
		this.fechaImpresionConsentimiento = fechaImpresionConsentimiento;
	}

	public String getHoraImpresionConsentimiento() {
		return horaImpresionConsentimiento;
	}

	public void setHoraImpresionConsentimiento(String horaImpresionConsentimiento) {
		this.horaImpresionConsentimiento = horaImpresionConsentimiento;
	}

	public String getUsuarioImpresionConsentimiento() {
		return usuarioImpresionConsentimiento;
	}

	public void setUsuarioImpresionConsentimiento(
			String usuarioImpresionConsentimiento) {
		this.usuarioImpresionConsentimiento = usuarioImpresionConsentimiento;
	}

	public ArrayList<Integer> getCodigosPHPRelacionados() {
		return codigosPHPRelacionados;
	}

	public void setCodigosPHPRelacionados(ArrayList<Integer> codigosPHPRelacionados) {
		this.codigosPHPRelacionados = codigosPHPRelacionados;
	} 

}
