package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;


public class DtoPazYSalvoCarteraPaciente implements Serializable
{
	private String codigoPk;
	private String consecutivo;
	private String anioConsecutivo;
	private DtoDatosFinanciacion datosFinanciacion;
	private DtoDocumentosGarantia documentosGarantia;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	
	/**
	 * Constructor
	 */
	public DtoPazYSalvoCarteraPaciente()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.codigoPk="";
		this.consecutivo="";
		this.anioConsecutivo="";
		this.datosFinanciacion=new DtoDatosFinanciacion();
		this.documentosGarantia=new DtoDocumentosGarantia();
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
	}

	
	public DtoDocumentosGarantia getDocumentosGarantia() {
		return documentosGarantia;
	}

	public void setDocumentosGarantia(DtoDocumentosGarantia documentosGarantia) {
		this.documentosGarantia = documentosGarantia;
	}

	public DtoDatosFinanciacion getDatosFinanciacion() {
		return datosFinanciacion;
	}

	public void setDatosFinanciacion(DtoDatosFinanciacion datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	public String getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
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

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}	
}
