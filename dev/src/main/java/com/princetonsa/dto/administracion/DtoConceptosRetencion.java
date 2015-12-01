/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.administracion;

import java.io.Serializable;

import com.princetonsa.dto.interfaz.DtoCuentaContable;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo del concepto de retencion
 * 
 * @author Angela María Angel.
 *
 */
public class DtoConceptosRetencion implements Serializable
{
	private String consecutivo;
	private String codigoConcepto;
	private String descripcion;
	private String codigoInterfaz;
	private String activo;
	
	//Datos adiconales para manejo en objetos
	private DtoTiposRetencion tipoRet;
	private DtoCuentaContable cuentaRetencion;
	private DtoCuentaContable cuentaDBAutoretencion;
	private DtoCuentaContable cuentaCRAutoretencion;
	
		
	/**
	 * Constructor
	 */
	public DtoConceptosRetencion()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.codigoConcepto = "";
		this.descripcion = "";
		this.tipoRet = new DtoTiposRetencion();		
		this.codigoInterfaz="";
		this.activo="";
		this.cuentaRetencion = new DtoCuentaContable();
		this.cuentaDBAutoretencion = new DtoCuentaContable();
		this.cuentaCRAutoretencion = new DtoCuentaContable();
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public String getCuentaRet() {
		return this.cuentaRetencion.getCodigo();
	}

	public void setCuentaRet(String cuentaRet) {
		this.cuentaRetencion.setCodigo(cuentaRet);
	}

	public String getCuentaDBautoRet() {
		return this.cuentaDBAutoretencion.getCodigo();
	}

	public void setCuentaDBautoRet(String cuentaDBautoRet) {
		this.cuentaDBAutoretencion.setCodigo(cuentaDBautoRet);
	}

	public String getCuentaCRautoRet() {
		return cuentaCRAutoretencion.getCodigo();
	}

	public void setCuentaCRautoRet(String cuentaCRautoRet) {
		this.cuentaCRAutoretencion.setCodigo(cuentaCRautoRet);
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the cuentaCRAutoretencion
	 */
	public DtoCuentaContable getCuentaCRAutoretencion() {
		return cuentaCRAutoretencion;
	}

	/**
	 * @param cuentaCRAutoretencion the cuentaCRAutoretencion to set
	 */
	public void setCuentaCRAutoretencion(DtoCuentaContable cuentaCRAutoretencion) {
		this.cuentaCRAutoretencion = cuentaCRAutoretencion;
	}

	/**
	 * @return the cuentaRetencion
	 */
	public DtoCuentaContable getCuentaRetencion() {
		return cuentaRetencion;
	}

	/**
	 * @param cuentaRetencion the cuentaRetencion to set
	 */
	public void setCuentaRetencion(DtoCuentaContable cuentaRetencion) {
		this.cuentaRetencion = cuentaRetencion;
	}

	/**
	 * @return the cuentaDBAutoretencion
	 */
	public DtoCuentaContable getCuentaDBAutoretencion() {
		return cuentaDBAutoretencion;
	}

	/**
	 * @param cuentaDBAutoretencion the cuentaDBAutoretencion to set
	 */
	public void setCuentaDBAutoretencion(DtoCuentaContable cuentaDBAutoretencion) {
		this.cuentaDBAutoretencion = cuentaDBAutoretencion;
	}

	public DtoTiposRetencion getTipoRet() {
		return tipoRet;
	}

	public void setTipoRet(DtoTiposRetencion tipoRet) {
		this.tipoRet = tipoRet;
	}	

	public String getTipoRetencion() {
		return this.tipoRet.getCodigo();
	}

	public void setTipoRetencion(String tipoRet) {
		this.tipoRet.setCodigo(tipoRet);
	}	
	
	public String getTipoRetencionDesc() {
		return this.tipoRet.getDescripcion();
	}

	public void setTipoRetencionDesc(String descripcion) {
		this.tipoRet.setDescripcion(descripcion);
	}	
	
	public String getTipoRetencionSigla() {
		return this.tipoRet.getSigla();
	}

	public void setTipoRetencionSigla(String sigla) {
		this.tipoRet.setSigla(sigla);
	}	
	
	public String getTipoRetencionInterfaz() {
		return this.tipoRet.getSigla();
	}

	public void setTipoRetencionInterfaz(String codigoInterfaz) {
		this.tipoRet.setCodigoInterfaz(codigoInterfaz);
	}		
	
	public int getTipoRetencionConsecutivo() {
		return this.tipoRet.getConsecutivo();
	}
	
	public void setTipoRetencionConsecutivo(int consecutivo) {
		this.tipoRet.setConsecutivo(consecutivo);
	}	
	
}
