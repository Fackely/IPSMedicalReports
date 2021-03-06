package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LecturaPlanosApId generated by hbm2java
 */
public class LecturaPlanosApId implements java.io.Serializable {

	private String numeroFactura;
	private String codigoPrestador;
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String fechaProcedimiento;
	private String numeroAutorizacion;
	private String codigoProcedimiento;
	private String ambitoRealizacion;
	private String finalidadProcedimiento;
	private String personalAtiende;
	private String diagPpal;
	private String diagRelacionado;
	private String complicacion;
	private String formaRealizacion;
	private String valorProcedimiento;
	private String usuario;
	private int centroAtencion;

	public LecturaPlanosApId() {
	}

	public LecturaPlanosApId(String numeroFactura, String codigoPrestador,
			String tipoIdentificacion, String numeroIdentificacion,
			String fechaProcedimiento, String codigoProcedimiento,
			String ambitoRealizacion, String valorProcedimiento,
			String usuario, int centroAtencion) {
		this.numeroFactura = numeroFactura;
		this.codigoPrestador = codigoPrestador;
		this.tipoIdentificacion = tipoIdentificacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaProcedimiento = fechaProcedimiento;
		this.codigoProcedimiento = codigoProcedimiento;
		this.ambitoRealizacion = ambitoRealizacion;
		this.valorProcedimiento = valorProcedimiento;
		this.usuario = usuario;
		this.centroAtencion = centroAtencion;
	}

	public LecturaPlanosApId(String numeroFactura, String codigoPrestador,
			String tipoIdentificacion, String numeroIdentificacion,
			String fechaProcedimiento, String numeroAutorizacion,
			String codigoProcedimiento, String ambitoRealizacion,
			String finalidadProcedimiento, String personalAtiende,
			String diagPpal, String diagRelacionado, String complicacion,
			String formaRealizacion, String valorProcedimiento, String usuario,
			int centroAtencion) {
		this.numeroFactura = numeroFactura;
		this.codigoPrestador = codigoPrestador;
		this.tipoIdentificacion = tipoIdentificacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaProcedimiento = fechaProcedimiento;
		this.numeroAutorizacion = numeroAutorizacion;
		this.codigoProcedimiento = codigoProcedimiento;
		this.ambitoRealizacion = ambitoRealizacion;
		this.finalidadProcedimiento = finalidadProcedimiento;
		this.personalAtiende = personalAtiende;
		this.diagPpal = diagPpal;
		this.diagRelacionado = diagRelacionado;
		this.complicacion = complicacion;
		this.formaRealizacion = formaRealizacion;
		this.valorProcedimiento = valorProcedimiento;
		this.usuario = usuario;
		this.centroAtencion = centroAtencion;
	}

	public String getNumeroFactura() {
		return this.numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getCodigoPrestador() {
		return this.codigoPrestador;
	}

	public void setCodigoPrestador(String codigoPrestador) {
		this.codigoPrestador = codigoPrestador;
	}

	public String getTipoIdentificacion() {
		return this.tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return this.numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getFechaProcedimiento() {
		return this.fechaProcedimiento;
	}

	public void setFechaProcedimiento(String fechaProcedimiento) {
		this.fechaProcedimiento = fechaProcedimiento;
	}

	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public String getCodigoProcedimiento() {
		return this.codigoProcedimiento;
	}

	public void setCodigoProcedimiento(String codigoProcedimiento) {
		this.codigoProcedimiento = codigoProcedimiento;
	}

	public String getAmbitoRealizacion() {
		return this.ambitoRealizacion;
	}

	public void setAmbitoRealizacion(String ambitoRealizacion) {
		this.ambitoRealizacion = ambitoRealizacion;
	}

	public String getFinalidadProcedimiento() {
		return this.finalidadProcedimiento;
	}

	public void setFinalidadProcedimiento(String finalidadProcedimiento) {
		this.finalidadProcedimiento = finalidadProcedimiento;
	}

	public String getPersonalAtiende() {
		return this.personalAtiende;
	}

	public void setPersonalAtiende(String personalAtiende) {
		this.personalAtiende = personalAtiende;
	}

	public String getDiagPpal() {
		return this.diagPpal;
	}

	public void setDiagPpal(String diagPpal) {
		this.diagPpal = diagPpal;
	}

	public String getDiagRelacionado() {
		return this.diagRelacionado;
	}

	public void setDiagRelacionado(String diagRelacionado) {
		this.diagRelacionado = diagRelacionado;
	}

	public String getComplicacion() {
		return this.complicacion;
	}

	public void setComplicacion(String complicacion) {
		this.complicacion = complicacion;
	}

	public String getFormaRealizacion() {
		return this.formaRealizacion;
	}

	public void setFormaRealizacion(String formaRealizacion) {
		this.formaRealizacion = formaRealizacion;
	}

	public String getValorProcedimiento() {
		return this.valorProcedimiento;
	}

	public void setValorProcedimiento(String valorProcedimiento) {
		this.valorProcedimiento = valorProcedimiento;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LecturaPlanosApId))
			return false;
		LecturaPlanosApId castOther = (LecturaPlanosApId) other;

		return ((this.getNumeroFactura() == castOther.getNumeroFactura()) || (this
				.getNumeroFactura() != null
				&& castOther.getNumeroFactura() != null && this
				.getNumeroFactura().equals(castOther.getNumeroFactura())))
				&& ((this.getCodigoPrestador() == castOther
						.getCodigoPrestador()) || (this.getCodigoPrestador() != null
						&& castOther.getCodigoPrestador() != null && this
						.getCodigoPrestador().equals(
								castOther.getCodigoPrestador())))
				&& ((this.getTipoIdentificacion() == castOther
						.getTipoIdentificacion()) || (this
						.getTipoIdentificacion() != null
						&& castOther.getTipoIdentificacion() != null && this
						.getTipoIdentificacion().equals(
								castOther.getTipoIdentificacion())))
				&& ((this.getNumeroIdentificacion() == castOther
						.getNumeroIdentificacion()) || (this
						.getNumeroIdentificacion() != null
						&& castOther.getNumeroIdentificacion() != null && this
						.getNumeroIdentificacion().equals(
								castOther.getNumeroIdentificacion())))
				&& ((this.getFechaProcedimiento() == castOther
						.getFechaProcedimiento()) || (this
						.getFechaProcedimiento() != null
						&& castOther.getFechaProcedimiento() != null && this
						.getFechaProcedimiento().equals(
								castOther.getFechaProcedimiento())))
				&& ((this.getNumeroAutorizacion() == castOther
						.getNumeroAutorizacion()) || (this
						.getNumeroAutorizacion() != null
						&& castOther.getNumeroAutorizacion() != null && this
						.getNumeroAutorizacion().equals(
								castOther.getNumeroAutorizacion())))
				&& ((this.getCodigoProcedimiento() == castOther
						.getCodigoProcedimiento()) || (this
						.getCodigoProcedimiento() != null
						&& castOther.getCodigoProcedimiento() != null && this
						.getCodigoProcedimiento().equals(
								castOther.getCodigoProcedimiento())))
				&& ((this.getAmbitoRealizacion() == castOther
						.getAmbitoRealizacion()) || (this
						.getAmbitoRealizacion() != null
						&& castOther.getAmbitoRealizacion() != null && this
						.getAmbitoRealizacion().equals(
								castOther.getAmbitoRealizacion())))
				&& ((this.getFinalidadProcedimiento() == castOther
						.getFinalidadProcedimiento()) || (this
						.getFinalidadProcedimiento() != null
						&& castOther.getFinalidadProcedimiento() != null && this
						.getFinalidadProcedimiento().equals(
								castOther.getFinalidadProcedimiento())))
				&& ((this.getPersonalAtiende() == castOther
						.getPersonalAtiende()) || (this.getPersonalAtiende() != null
						&& castOther.getPersonalAtiende() != null && this
						.getPersonalAtiende().equals(
								castOther.getPersonalAtiende())))
				&& ((this.getDiagPpal() == castOther.getDiagPpal()) || (this
						.getDiagPpal() != null
						&& castOther.getDiagPpal() != null && this
						.getDiagPpal().equals(castOther.getDiagPpal())))
				&& ((this.getDiagRelacionado() == castOther
						.getDiagRelacionado()) || (this.getDiagRelacionado() != null
						&& castOther.getDiagRelacionado() != null && this
						.getDiagRelacionado().equals(
								castOther.getDiagRelacionado())))
				&& ((this.getComplicacion() == castOther.getComplicacion()) || (this
						.getComplicacion() != null
						&& castOther.getComplicacion() != null && this
						.getComplicacion().equals(castOther.getComplicacion())))
				&& ((this.getFormaRealizacion() == castOther
						.getFormaRealizacion()) || (this.getFormaRealizacion() != null
						&& castOther.getFormaRealizacion() != null && this
						.getFormaRealizacion().equals(
								castOther.getFormaRealizacion())))
				&& ((this.getValorProcedimiento() == castOther
						.getValorProcedimiento()) || (this
						.getValorProcedimiento() != null
						&& castOther.getValorProcedimiento() != null && this
						.getValorProcedimiento().equals(
								castOther.getValorProcedimiento())))
				&& ((this.getUsuario() == castOther.getUsuario()) || (this
						.getUsuario() != null
						&& castOther.getUsuario() != null && this.getUsuario()
						.equals(castOther.getUsuario())))
				&& (this.getCentroAtencion() == castOther.getCentroAtencion());
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getNumeroFactura() == null ? 0 : this.getNumeroFactura()
						.hashCode());
		result = 37
				* result
				+ (getCodigoPrestador() == null ? 0 : this.getCodigoPrestador()
						.hashCode());
		result = 37
				* result
				+ (getTipoIdentificacion() == null ? 0 : this
						.getTipoIdentificacion().hashCode());
		result = 37
				* result
				+ (getNumeroIdentificacion() == null ? 0 : this
						.getNumeroIdentificacion().hashCode());
		result = 37
				* result
				+ (getFechaProcedimiento() == null ? 0 : this
						.getFechaProcedimiento().hashCode());
		result = 37
				* result
				+ (getNumeroAutorizacion() == null ? 0 : this
						.getNumeroAutorizacion().hashCode());
		result = 37
				* result
				+ (getCodigoProcedimiento() == null ? 0 : this
						.getCodigoProcedimiento().hashCode());
		result = 37
				* result
				+ (getAmbitoRealizacion() == null ? 0 : this
						.getAmbitoRealizacion().hashCode());
		result = 37
				* result
				+ (getFinalidadProcedimiento() == null ? 0 : this
						.getFinalidadProcedimiento().hashCode());
		result = 37
				* result
				+ (getPersonalAtiende() == null ? 0 : this.getPersonalAtiende()
						.hashCode());
		result = 37 * result
				+ (getDiagPpal() == null ? 0 : this.getDiagPpal().hashCode());
		result = 37
				* result
				+ (getDiagRelacionado() == null ? 0 : this.getDiagRelacionado()
						.hashCode());
		result = 37
				* result
				+ (getComplicacion() == null ? 0 : this.getComplicacion()
						.hashCode());
		result = 37
				* result
				+ (getFormaRealizacion() == null ? 0 : this
						.getFormaRealizacion().hashCode());
		result = 37
				* result
				+ (getValorProcedimiento() == null ? 0 : this
						.getValorProcedimiento().hashCode());
		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result + this.getCentroAtencion();
		return result;
	}

}
