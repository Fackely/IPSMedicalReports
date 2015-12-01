package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoLogTipoRetencion implements Serializable{

	private int consecutivo;
	private int codigoTipoRetencion;
	private String codigo;
	private String descripcion;
	private String sigla;
	private String codigoInterfaz;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
		
	/**
	 * Constructor
	 */
	public DtoLogTipoRetencion()
	{
		this.reset();
	}
	
	/**
	 * Método que inicia los datos Detalle tipos Retencion clase invetario
	 *
	 */
	private void reset() 
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigoTipoRetencion = ConstantesBD.codigoNuncaValido;
		this.codigo = "";
		this.descripcion = "";
		this.sigla = "";
		this.codigoInterfaz = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = "";
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigoTipoRetencion
	 */
	public int getCodigoTipoRetencion() {
		return codigoTipoRetencion;
	}

	/**
	 * @param codigoTipoRetencion the codigoTipoRetencion to set
	 */
	public void setCodigoTipoRetencion(int codigoTipoRetencion) {
		this.codigoTipoRetencion = codigoTipoRetencion;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
}
