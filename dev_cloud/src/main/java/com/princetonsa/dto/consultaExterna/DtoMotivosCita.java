package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;

import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;


public class DtoMotivosCita implements Serializable
{
	private int codigoPk;
	private String codigo;
	private int institucion;
	private String descripcion;
	private String activo;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	// variable para campo anexado tipo_motivo
	private String tipoMotivo;
	
	/**
	 * Constructor
	 */
	public DtoMotivosCita()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.codigoPk=0;
		this.codigo="";
		this.institucion=0;
		this.descripcion="";
		this.activo="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.tipoMotivo="";
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
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

	/**
	 * @return the tipoMotivo
	 */
	public String getTipoMotivo() {
		return tipoMotivo;
	}

	/**
	 * @param tipoMotivo the tipoMotivo to set
	 */
	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}
	
	
}
