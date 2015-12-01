package com.princetonsa.dto.odontologia;

import java.util.ArrayList;

public class DtoPaquetesOdontologicosConvenio 
{
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int convenio;
	
	/**
	 * 
	 */
	private int contrato;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String fechaModifica;
	
	/**
	 * 
	 */
	private String horaModifica;
	
	/**
	 * 
	 */
	private String activo;
	
	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * 
	 */
	private ArrayList<DtoDetallePaquetesOdontologicosConvenios> detallePaquete =new ArrayList<DtoDetallePaquetesOdontologicosConvenios>();

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getConvenio() {
		return convenio;
	}

	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	public int getContrato() {
		return contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public ArrayList<DtoDetallePaquetesOdontologicosConvenios> getDetallePaquete() {
		return detallePaquete;
	}

	public void setDetallePaquete(
			ArrayList<DtoDetallePaquetesOdontologicosConvenios> detallePaquete) {
		this.detallePaquete = detallePaquete;
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
	

}
