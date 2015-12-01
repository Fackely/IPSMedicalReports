/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoCertAtenMedicaFurips 
{
	
	/**
	 * 
	 */
	private String acronimoDxIngreso;
	
	/**
	 * 
	 */
	private int tipoCieDxIngreso;
	
	/**
	 * 
	 */
	private String acronimoDxEgreso;
	
	/**
	 * 
	 */
	private int tipoCieDxEgreso;
	
	/**
	 * 
	 */
	private String acronimoDxRel1Ingreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel1Ingreso;
	
	/**
	 * 
	 */
	private String acronimoDxRel2Ingreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel2Ingreso;
	
	/**
	 * 
	 */
	private String acronimoDxRel1Egreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel1Egreso;
	
	/**
	 * 
	 */
	private String acronimoDxRel2Egreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel2Egreso;
	
	/**
	 * 
	 */
	private int codigoMedico;
	
	/**
	 * 
	 */
	private String primerApellidoMedico;
	
	/**
	 * 
	 */
	private String segundoApellidoMedico;
	
	/**
	 * 
	 */
	private String primerNombreMedico;
	
	/**
	 * 
	 */
	private String segundoNombreMedico;
	
	/**
	 * 
	 */
	private String tipoDocumentoMedico;
	
	/**
	 * 
	 */
	private String numeroDocumentoMedico;
	
	/**
	 * 
	 */
	private String numeroRegistroMedico;

	
	/**
	 * Via en que ingreso el paciente
	 */
	private int viaIngreso;
	/**
	 * 
	 */
	public DtoCertAtenMedicaFurips() 
	{
		this.acronimoDxIngreso = "";
		this.tipoCieDxIngreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxEgreso = "";
		this.tipoCieDxEgreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel1Ingreso = "";
		this.tipoCieDxRel1Ingreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel2Ingreso = "";
		this.tipoCieDxRel2Ingreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel1Egreso = "";
		this.tipoCieDxRel1Egreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel2Egreso = "";
		this.tipoCieDxRel2Egreso = ConstantesBD.codigoNuncaValido;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.primerApellidoMedico = "";
		this.segundoApellidoMedico = "";
		this.primerNombreMedico = "";
		this.segundoNombreMedico = "";
		this.tipoDocumentoMedico = "";
		this.numeroDocumentoMedico = "";
		this.numeroRegistroMedico = "";
		this.viaIngreso=ConstantesBD.codigoNuncaValido;
	}

	public String getAcronimoDxIngreso() {
		return acronimoDxIngreso;
	}

	public void setAcronimoDxIngreso(String acronimoDxIngreso) {
		this.acronimoDxIngreso = acronimoDxIngreso;
	}

	public int getTipoCieDxIngreso() {
		return tipoCieDxIngreso;
	}

	public void setTipoCieDxIngreso(int tipoCieDxIngreso) {
		this.tipoCieDxIngreso = tipoCieDxIngreso;
	}

	public String getAcronimoDxEgreso() {
		return acronimoDxEgreso;
	}

	public void setAcronimoDxEgreso(String acronimoDxEgreso) {
		this.acronimoDxEgreso = acronimoDxEgreso;
	}

	public int getTipoCieDxEgreso() {
		return tipoCieDxEgreso;
	}

	public void setTipoCieDxEgreso(int tipoCieDxEgreso) {
		this.tipoCieDxEgreso = tipoCieDxEgreso;
	}

	public String getAcronimoDxRel1Ingreso() {
		return acronimoDxRel1Ingreso;
	}

	public void setAcronimoDxRel1Ingreso(String acronimoDxRel1Ingreso) {
		this.acronimoDxRel1Ingreso = acronimoDxRel1Ingreso;
	}

	public int getTipoCieDxRel1Ingreso() {
		return tipoCieDxRel1Ingreso;
	}

	public void setTipoCieDxRel1Ingreso(int tipoCieDxRel1Ingreso) {
		this.tipoCieDxRel1Ingreso = tipoCieDxRel1Ingreso;
	}

	public String getAcronimoDxRel2Ingreso() {
		return acronimoDxRel2Ingreso;
	}

	public void setAcronimoDxRel2Ingreso(String acronimoDxRel2Ingreso) {
		this.acronimoDxRel2Ingreso = acronimoDxRel2Ingreso;
	}

	public int getTipoCieDxRel2Ingreso() {
		return tipoCieDxRel2Ingreso;
	}

	public void setTipoCieDxRel2Ingreso(int tipoCieDxRel2Ingreso) {
		this.tipoCieDxRel2Ingreso = tipoCieDxRel2Ingreso;
	}

	public String getAcronimoDxRel1Egreso() {
		return acronimoDxRel1Egreso;
	}

	public void setAcronimoDxRel1Egreso(String acronimoDxRel1Egreso) {
		this.acronimoDxRel1Egreso = acronimoDxRel1Egreso;
	}

	public int getTipoCieDxRel1Egreso() {
		return tipoCieDxRel1Egreso;
	}

	public void setTipoCieDxRel1Egreso(int tipoCieDxRel1Egreso) {
		this.tipoCieDxRel1Egreso = tipoCieDxRel1Egreso;
	}

	public String getAcronimoDxRel2Egreso() {
		return acronimoDxRel2Egreso;
	}

	public void setAcronimoDxRel2Egreso(String acronimoDxRel2Egreso) {
		this.acronimoDxRel2Egreso = acronimoDxRel2Egreso;
	}

	public int getTipoCieDxRel2Egreso() {
		return tipoCieDxRel2Egreso;
	}

	public void setTipoCieDxRel2Egreso(int tipoCieDxRel2Egreso) {
		this.tipoCieDxRel2Egreso = tipoCieDxRel2Egreso;
	}

	public int getCodigoMedico() {
		return codigoMedico;
	}

	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	public String getPrimerApellidoMedico() {
		return primerApellidoMedico;
	}

	public void setPrimerApellidoMedico(String primerApellidoMedico) {
		this.primerApellidoMedico = primerApellidoMedico;
	}

	public String getSegundoApellidoMedico() {
		return segundoApellidoMedico;
	}

	public void setSegundoApellidoMedico(String segundoApellidoMedico) {
		this.segundoApellidoMedico = segundoApellidoMedico;
	}

	public String getPrimerNombreMedico() {
		return primerNombreMedico;
	}

	public void setPrimerNombreMedico(String primerNombreMedico) {
		this.primerNombreMedico = primerNombreMedico;
	}

	public String getSegundoNombreMedico() {
		return segundoNombreMedico;
	}

	public void setSegundoNombreMedico(String segundoNombreMedico) {
		this.segundoNombreMedico = segundoNombreMedico;
	}

	public String getTipoDocumentoMedico() {
		return tipoDocumentoMedico;
	}

	public void setTipoDocumentoMedico(String tipoDocumentoMedico) {
		this.tipoDocumentoMedico = tipoDocumentoMedico;
	}

	public String getNumeroDocumentoMedico() {
		return numeroDocumentoMedico;
	}

	public void setNumeroDocumentoMedico(String numeroDocumentoMedico) {
		this.numeroDocumentoMedico = numeroDocumentoMedico;
	}

	public String getNumeroRegistroMedico() {
		return numeroRegistroMedico;
	}

	public void setNumeroRegistroMedico(String numeroRegistroMedico) {
		this.numeroRegistroMedico = numeroRegistroMedico;
	}
	
	/**
	 * Via en que ingreso el paciente
	 * 
	 * @return
	 * @author jeilones
	 * @created 13/12/2012
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
	
	

}
