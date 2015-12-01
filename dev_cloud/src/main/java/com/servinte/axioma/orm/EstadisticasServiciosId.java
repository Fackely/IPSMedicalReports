package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * EstadisticasServiciosId generated by hbm2java
 */
public class EstadisticasServiciosId implements java.io.Serializable {

	private BigDecimal codigo;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;

	public EstadisticasServiciosId() {
	}

	public EstadisticasServiciosId(BigDecimal codigo, String usuarioModifica,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public BigDecimal getCodigo() {
		return this.codigo;
	}

	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EstadisticasServiciosId))
			return false;
		EstadisticasServiciosId castOther = (EstadisticasServiciosId) other;

		return ((this.getCodigo() == castOther.getCodigo()) || (this
				.getCodigo() != null
				&& castOther.getCodigo() != null && this.getCodigo().equals(
				castOther.getCodigo())))
				&& ((this.getUsuarioModifica() == castOther
						.getUsuarioModifica()) || (this.getUsuarioModifica() != null
						&& castOther.getUsuarioModifica() != null && this
						.getUsuarioModifica().equals(
								castOther.getUsuarioModifica())))
				&& ((this.getFechaModifica() == castOther.getFechaModifica()) || (this
						.getFechaModifica() != null
						&& castOther.getFechaModifica() != null && this
						.getFechaModifica()
						.equals(castOther.getFechaModifica())))
				&& ((this.getHoraModifica() == castOther.getHoraModifica()) || (this
						.getHoraModifica() != null
						&& castOther.getHoraModifica() != null && this
						.getHoraModifica().equals(castOther.getHoraModifica())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodigo() == null ? 0 : this.getCodigo().hashCode());
		result = 37
				* result
				+ (getUsuarioModifica() == null ? 0 : this.getUsuarioModifica()
						.hashCode());
		result = 37
				* result
				+ (getFechaModifica() == null ? 0 : this.getFechaModifica()
						.hashCode());
		result = 37
				* result
				+ (getHoraModifica() == null ? 0 : this.getHoraModifica()
						.hashCode());
		return result;
	}

}
