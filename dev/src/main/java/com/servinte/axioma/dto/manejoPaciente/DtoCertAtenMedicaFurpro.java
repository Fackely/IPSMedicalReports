/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoCertAtenMedicaFurpro 
{
	
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
	private String acronimoDxRel3Egreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel3Egreso;
	
	/**
	 * 
	 */
	private String acronimoDxRel4Egreso;
	
	/**
	 * 
	 */
	private int tipoCieDxRel4Egreso;

	/**
	 * 
	 */
	public DtoCertAtenMedicaFurpro() 
	{
		this.acronimoDxEgreso = "";
		this.tipoCieDxEgreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel1Egreso = "";
		this.tipoCieDxRel1Egreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel2Egreso = "";
		this.tipoCieDxRel2Egreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel3Egreso = "";
		this.tipoCieDxRel3Egreso = ConstantesBD.codigoNuncaValido;
		this.acronimoDxRel4Egreso = "";
		this.tipoCieDxRel4Egreso = ConstantesBD.codigoNuncaValido;
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

	public String getAcronimoDxRel3Egreso() {
		return acronimoDxRel3Egreso;
	}

	public void setAcronimoDxRel3Egreso(String acronimoDxRel3Egreso) {
		this.acronimoDxRel3Egreso = acronimoDxRel3Egreso;
	}

	public int getTipoCieDxRel3Egreso() {
		return tipoCieDxRel3Egreso;
	}

	public void setTipoCieDxRel3Egreso(int tipoCieDxRel3Egreso) {
		this.tipoCieDxRel3Egreso = tipoCieDxRel3Egreso;
	}

	public String getAcronimoDxRel4Egreso() {
		return acronimoDxRel4Egreso;
	}

	public void setAcronimoDxRel4Egreso(String acronimoDxRel4Egreso) {
		this.acronimoDxRel4Egreso = acronimoDxRel4Egreso;
	}

	public int getTipoCieDxRel4Egreso() {
		return tipoCieDxRel4Egreso;
	}

	public void setTipoCieDxRel4Egreso(int tipoCieDxRel4Egreso) {
		this.tipoCieDxRel4Egreso = tipoCieDxRel4Egreso;
	}

	
}
