package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoDetConceptosRetencion implements Serializable
{
	private String consecutivoPk;
	private String fechaVigenciaInicial;
	private String tipoRetencion;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String institucion;
	private DtoTiposRetencion dtoTiposRetencion;
	private ArrayList<DtoDetVigConRetencion> listaDetVigConceptosRetencion;
	/**
	 * Constructor
	 */
	public DtoDetConceptosRetencion()
	{
		this.reset();
	}
	
	private void reset() 
	{
		this.consecutivoPk="";
		this.fechaVigenciaInicial="";
		this.tipoRetencion="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.institucion="";
		this.dtoTiposRetencion=new DtoTiposRetencion();
		this.listaDetVigConceptosRetencion= new ArrayList<DtoDetVigConRetencion>();
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getFechaVigenciaInicial() {
		return fechaVigenciaInicial;
	}

	public void setFechaVigenciaInicial(String fechaVigenciaInicial) {
		this.fechaVigenciaInicial = fechaVigenciaInicial;
	}

	public String getTipoRetencion() {
		return tipoRetencion;
	}

	public void setTipoRetencion(String tipoRetencion) {
		this.tipoRetencion = tipoRetencion;
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

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public DtoTiposRetencion getDtoTiposRetencion() {
		return dtoTiposRetencion;
	}

	public void setDtoTiposRetencion(DtoTiposRetencion dtoTiposRetencion) {
		this.dtoTiposRetencion = dtoTiposRetencion;
	}

	public ArrayList<DtoDetVigConRetencion> getListaDetVigConceptosRetencion() {
		return listaDetVigConceptosRetencion;
	}

	public void setListaDetVigConceptosRetencion(
			ArrayList<DtoDetVigConRetencion> listaDetVigConceptosRetencion) {
		this.listaDetVigConceptosRetencion = listaDetVigConceptosRetencion;
	}
}