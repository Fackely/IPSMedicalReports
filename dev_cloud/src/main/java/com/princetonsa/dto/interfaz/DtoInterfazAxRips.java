package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import util.InfoDatosString;

/**
 * @author Jose Eduardo Arias Doncel
 * **/
public class DtoInterfazAxRips implements Serializable
{
	
	/**
	 * indicador de Posicion dentro de un ArrayList
	 * */
	private int indicadorPos;
	
	/** 
	 * */
	private String numeroEnvio;
	
	/** 
	 * */
	private String numeroFactura;
	
	/**
	 * */
	private String codigoConvenio;
	
	
	/**
	 * */
	private String descripcionConvenio;
	
	/**
	 * */
	private String estadoRegistro;
	
	/** 
	 * */
	private String fechaReistro;
	
	/**
	 * */
	private String horaRegistro;
	
	/**	 
	 * */
	private ArrayList<InfoDatosString> FacturaConvenio;
	
	
	
	
	/**
	 * @param String numeroEnvio
	 * @param String numeroFactura
	 * @param String codigoConvenio
	 * @param String estadoRegistro
	 * @param String fechaRegistro
	 * @param String horaRegistro
	 * */
	public void DtoInterfazRips(
			String numeroEnvio,
			String numeroFactura,
			String codigoConvenio,
			String descripcionConvenio,
			String estadoRegistro,
			String fechaRegistro,
			String horaRegistro,
			ArrayList<InfoDatosString> infoDatos)
	{
		this.numeroEnvio = numeroEnvio;
		this.numeroFactura = numeroFactura;
		this.codigoConvenio = codigoConvenio;
		this.descripcionConvenio = descripcionConvenio;
		this.estadoRegistro = estadoRegistro;
		this.fechaReistro  = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.FacturaConvenio = infoDatos;
	}

	/**
	 * @return the numeroEnvio
	 */
	public String getNumeroEnvio() {
		return numeroEnvio;
	}

	/**
	 * @param numeroEnvio the numeroEnvio to set
	 */
	public void setNumeroEnvio(String numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}

	/**
	 * @return the codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the estadoRegistro
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	/**
	 * @return the fechaReistro
	 */
	public String getFechaReistro() {
		return fechaReistro;
	}

	/**
	 * @param fechaReistro the fechaReistro to set
	 */
	public void setFechaReistro(String fechaReistro) {
		this.fechaReistro = fechaReistro;
	}

	/**
	 * @return the horaRegistro
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}

	/**
	 * @param horaRegistro the horaRegistro to set
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the facturaConvenio
	 */
	public ArrayList<InfoDatosString> getFacturaConvenio() {
		return FacturaConvenio;
	}

	/**
	 * @param facturaConvenio the facturaConvenio to set
	 */
	public void setFacturaConvenio(ArrayList<InfoDatosString> facturaConvenio) {
		FacturaConvenio = facturaConvenio;
	}	
	
	public String getCodigoConvenioFromArrayList(int index)
	{
		return FacturaConvenio.get(index).getCodigo();						
	}
	
	public String getCodigoFacturaFromArrayList(int index)
	{
		return FacturaConvenio.get(index).getNombre();						
	}

	/**
	 * @return the descripcionConvenio
	 */
	public String getDescripcionConvenio() {
		return descripcionConvenio;
	}

	/**
	 * @param descripcionConvenio the descripcionConvenio to set
	 */
	public void setDescripcionConvenio(String descripcionConvenio) {
		this.descripcionConvenio = descripcionConvenio;
	}

	/**
	 * @return the indicadorPos
	 */
	public int getIndicadorPos() {
		return indicadorPos;
	}

	/**
	 * @param indicadorPos the indicadorPos to set
	 */
	public void setIndicadorPos(int indicadorPos) {
		this.indicadorPos = indicadorPos;
	}	
}