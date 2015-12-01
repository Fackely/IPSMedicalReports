package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;

/**
 * ViewFacturasInternasId generated by hbm2java
 */
public class ViewFacturasInternasId implements java.io.Serializable {

	private Integer codigo;
	private Integer consecutivofactura;
	private Double valortotal;
	private String fechahoraelaboracion;
	private Integer viaingreso;
	private String nombreviaingreso;
	private Integer codigoresponsable;
	private Boolean esresponsableparticular;
	private String nombreresponsable;
	private Integer codigopaciente;
	private String nombrepaciente;
	private Integer codigoestadofacturacion;
	private String nombreestadofacturacion;
	private Integer codigoestadopaciente;
	private String nombreestadopaciente;
	private Long empresainstitucion;
	private String nomempresainstitucion;
	private BigDecimal entidadsubcontratada;
	private String descentidadsubcontratada;
	private Integer institucion;

	public ViewFacturasInternasId() {
	}

	public ViewFacturasInternasId(Integer codigo, Integer consecutivofactura,
			Double valortotal, String fechahoraelaboracion, Integer viaingreso,
			String nombreviaingreso, Integer codigoresponsable,
			Boolean esresponsableparticular, String nombreresponsable,
			Integer codigopaciente, String nombrepaciente,
			Integer codigoestadofacturacion, String nombreestadofacturacion,
			Integer codigoestadopaciente, String nombreestadopaciente,
			Long empresainstitucion, String nomempresainstitucion,
			BigDecimal entidadsubcontratada, String descentidadsubcontratada,
			Integer institucion) {
		this.codigo = codigo;
		this.consecutivofactura = consecutivofactura;
		this.valortotal = valortotal;
		this.fechahoraelaboracion = fechahoraelaboracion;
		this.viaingreso = viaingreso;
		this.nombreviaingreso = nombreviaingreso;
		this.codigoresponsable = codigoresponsable;
		this.esresponsableparticular = esresponsableparticular;
		this.nombreresponsable = nombreresponsable;
		this.codigopaciente = codigopaciente;
		this.nombrepaciente = nombrepaciente;
		this.codigoestadofacturacion = codigoestadofacturacion;
		this.nombreestadofacturacion = nombreestadofacturacion;
		this.codigoestadopaciente = codigoestadopaciente;
		this.nombreestadopaciente = nombreestadopaciente;
		this.empresainstitucion = empresainstitucion;
		this.nomempresainstitucion = nomempresainstitucion;
		this.entidadsubcontratada = entidadsubcontratada;
		this.descentidadsubcontratada = descentidadsubcontratada;
		this.institucion = institucion;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getConsecutivofactura() {
		return this.consecutivofactura;
	}

	public void setConsecutivofactura(Integer consecutivofactura) {
		this.consecutivofactura = consecutivofactura;
	}

	public Double getValortotal() {
		return this.valortotal;
	}

	public void setValortotal(Double valortotal) {
		this.valortotal = valortotal;
	}

	public String getFechahoraelaboracion() {
		return this.fechahoraelaboracion;
	}

	public void setFechahoraelaboracion(String fechahoraelaboracion) {
		this.fechahoraelaboracion = fechahoraelaboracion;
	}

	public Integer getViaingreso() {
		return this.viaingreso;
	}

	public void setViaingreso(Integer viaingreso) {
		this.viaingreso = viaingreso;
	}

	public String getNombreviaingreso() {
		return this.nombreviaingreso;
	}

	public void setNombreviaingreso(String nombreviaingreso) {
		this.nombreviaingreso = nombreviaingreso;
	}

	public Integer getCodigoresponsable() {
		return this.codigoresponsable;
	}

	public void setCodigoresponsable(Integer codigoresponsable) {
		this.codigoresponsable = codigoresponsable;
	}

	public Boolean getEsresponsableparticular() {
		return this.esresponsableparticular;
	}

	public void setEsresponsableparticular(Boolean esresponsableparticular) {
		this.esresponsableparticular = esresponsableparticular;
	}

	public String getNombreresponsable() {
		return this.nombreresponsable;
	}

	public void setNombreresponsable(String nombreresponsable) {
		this.nombreresponsable = nombreresponsable;
	}

	public Integer getCodigopaciente() {
		return this.codigopaciente;
	}

	public void setCodigopaciente(Integer codigopaciente) {
		this.codigopaciente = codigopaciente;
	}

	public String getNombrepaciente() {
		return this.nombrepaciente;
	}

	public void setNombrepaciente(String nombrepaciente) {
		this.nombrepaciente = nombrepaciente;
	}

	public Integer getCodigoestadofacturacion() {
		return this.codigoestadofacturacion;
	}

	public void setCodigoestadofacturacion(Integer codigoestadofacturacion) {
		this.codigoestadofacturacion = codigoestadofacturacion;
	}

	public String getNombreestadofacturacion() {
		return this.nombreestadofacturacion;
	}

	public void setNombreestadofacturacion(String nombreestadofacturacion) {
		this.nombreestadofacturacion = nombreestadofacturacion;
	}

	public Integer getCodigoestadopaciente() {
		return this.codigoestadopaciente;
	}

	public void setCodigoestadopaciente(Integer codigoestadopaciente) {
		this.codigoestadopaciente = codigoestadopaciente;
	}

	public String getNombreestadopaciente() {
		return this.nombreestadopaciente;
	}

	public void setNombreestadopaciente(String nombreestadopaciente) {
		this.nombreestadopaciente = nombreestadopaciente;
	}

	public Long getEmpresainstitucion() {
		return this.empresainstitucion;
	}

	public void setEmpresainstitucion(Long empresainstitucion) {
		this.empresainstitucion = empresainstitucion;
	}

	public String getNomempresainstitucion() {
		return this.nomempresainstitucion;
	}

	public void setNomempresainstitucion(String nomempresainstitucion) {
		this.nomempresainstitucion = nomempresainstitucion;
	}

	public BigDecimal getEntidadsubcontratada() {
		return this.entidadsubcontratada;
	}

	public void setEntidadsubcontratada(BigDecimal entidadsubcontratada) {
		this.entidadsubcontratada = entidadsubcontratada;
	}

	public String getDescentidadsubcontratada() {
		return this.descentidadsubcontratada;
	}

	public void setDescentidadsubcontratada(String descentidadsubcontratada) {
		this.descentidadsubcontratada = descentidadsubcontratada;
	}

	public Integer getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ViewFacturasInternasId))
			return false;
		ViewFacturasInternasId castOther = (ViewFacturasInternasId) other;

		return ((this.getCodigo() == castOther.getCodigo()) || (this
				.getCodigo() != null
				&& castOther.getCodigo() != null && this.getCodigo().equals(
				castOther.getCodigo())))
				&& ((this.getConsecutivofactura() == castOther
						.getConsecutivofactura()) || (this
						.getConsecutivofactura() != null
						&& castOther.getConsecutivofactura() != null && this
						.getConsecutivofactura().equals(
								castOther.getConsecutivofactura())))
				&& ((this.getValortotal() == castOther.getValortotal()) || (this
						.getValortotal() != null
						&& castOther.getValortotal() != null && this
						.getValortotal().equals(castOther.getValortotal())))
				&& ((this.getFechahoraelaboracion() == castOther
						.getFechahoraelaboracion()) || (this
						.getFechahoraelaboracion() != null
						&& castOther.getFechahoraelaboracion() != null && this
						.getFechahoraelaboracion().equals(
								castOther.getFechahoraelaboracion())))
				&& ((this.getViaingreso() == castOther.getViaingreso()) || (this
						.getViaingreso() != null
						&& castOther.getViaingreso() != null && this
						.getViaingreso().equals(castOther.getViaingreso())))
				&& ((this.getNombreviaingreso() == castOther
						.getNombreviaingreso()) || (this.getNombreviaingreso() != null
						&& castOther.getNombreviaingreso() != null && this
						.getNombreviaingreso().equals(
								castOther.getNombreviaingreso())))
				&& ((this.getCodigoresponsable() == castOther
						.getCodigoresponsable()) || (this
						.getCodigoresponsable() != null
						&& castOther.getCodigoresponsable() != null && this
						.getCodigoresponsable().equals(
								castOther.getCodigoresponsable())))
				&& ((this.getEsresponsableparticular() == castOther
						.getEsresponsableparticular()) || (this
						.getEsresponsableparticular() != null
						&& castOther.getEsresponsableparticular() != null && this
						.getEsresponsableparticular().equals(
								castOther.getEsresponsableparticular())))
				&& ((this.getNombreresponsable() == castOther
						.getNombreresponsable()) || (this
						.getNombreresponsable() != null
						&& castOther.getNombreresponsable() != null && this
						.getNombreresponsable().equals(
								castOther.getNombreresponsable())))
				&& ((this.getCodigopaciente() == castOther.getCodigopaciente()) || (this
						.getCodigopaciente() != null
						&& castOther.getCodigopaciente() != null && this
						.getCodigopaciente().equals(
								castOther.getCodigopaciente())))
				&& ((this.getNombrepaciente() == castOther.getNombrepaciente()) || (this
						.getNombrepaciente() != null
						&& castOther.getNombrepaciente() != null && this
						.getNombrepaciente().equals(
								castOther.getNombrepaciente())))
				&& ((this.getCodigoestadofacturacion() == castOther
						.getCodigoestadofacturacion()) || (this
						.getCodigoestadofacturacion() != null
						&& castOther.getCodigoestadofacturacion() != null && this
						.getCodigoestadofacturacion().equals(
								castOther.getCodigoestadofacturacion())))
				&& ((this.getNombreestadofacturacion() == castOther
						.getNombreestadofacturacion()) || (this
						.getNombreestadofacturacion() != null
						&& castOther.getNombreestadofacturacion() != null && this
						.getNombreestadofacturacion().equals(
								castOther.getNombreestadofacturacion())))
				&& ((this.getCodigoestadopaciente() == castOther
						.getCodigoestadopaciente()) || (this
						.getCodigoestadopaciente() != null
						&& castOther.getCodigoestadopaciente() != null && this
						.getCodigoestadopaciente().equals(
								castOther.getCodigoestadopaciente())))
				&& ((this.getNombreestadopaciente() == castOther
						.getNombreestadopaciente()) || (this
						.getNombreestadopaciente() != null
						&& castOther.getNombreestadopaciente() != null && this
						.getNombreestadopaciente().equals(
								castOther.getNombreestadopaciente())))
				&& ((this.getEmpresainstitucion() == castOther
						.getEmpresainstitucion()) || (this
						.getEmpresainstitucion() != null
						&& castOther.getEmpresainstitucion() != null && this
						.getEmpresainstitucion().equals(
								castOther.getEmpresainstitucion())))
				&& ((this.getNomempresainstitucion() == castOther
						.getNomempresainstitucion()) || (this
						.getNomempresainstitucion() != null
						&& castOther.getNomempresainstitucion() != null && this
						.getNomempresainstitucion().equals(
								castOther.getNomempresainstitucion())))
				&& ((this.getEntidadsubcontratada() == castOther
						.getEntidadsubcontratada()) || (this
						.getEntidadsubcontratada() != null
						&& castOther.getEntidadsubcontratada() != null && this
						.getEntidadsubcontratada().equals(
								castOther.getEntidadsubcontratada())))
				&& ((this.getDescentidadsubcontratada() == castOther
						.getDescentidadsubcontratada()) || (this
						.getDescentidadsubcontratada() != null
						&& castOther.getDescentidadsubcontratada() != null && this
						.getDescentidadsubcontratada().equals(
								castOther.getDescentidadsubcontratada())))
				&& ((this.getInstitucion() == castOther.getInstitucion()) || (this
						.getInstitucion() != null
						&& castOther.getInstitucion() != null && this
						.getInstitucion().equals(castOther.getInstitucion())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodigo() == null ? 0 : this.getCodigo().hashCode());
		result = 37
				* result
				+ (getConsecutivofactura() == null ? 0 : this
						.getConsecutivofactura().hashCode());
		result = 37
				* result
				+ (getValortotal() == null ? 0 : this.getValortotal()
						.hashCode());
		result = 37
				* result
				+ (getFechahoraelaboracion() == null ? 0 : this
						.getFechahoraelaboracion().hashCode());
		result = 37
				* result
				+ (getViaingreso() == null ? 0 : this.getViaingreso()
						.hashCode());
		result = 37
				* result
				+ (getNombreviaingreso() == null ? 0 : this
						.getNombreviaingreso().hashCode());
		result = 37
				* result
				+ (getCodigoresponsable() == null ? 0 : this
						.getCodigoresponsable().hashCode());
		result = 37
				* result
				+ (getEsresponsableparticular() == null ? 0 : this
						.getEsresponsableparticular().hashCode());
		result = 37
				* result
				+ (getNombreresponsable() == null ? 0 : this
						.getNombreresponsable().hashCode());
		result = 37
				* result
				+ (getCodigopaciente() == null ? 0 : this.getCodigopaciente()
						.hashCode());
		result = 37
				* result
				+ (getNombrepaciente() == null ? 0 : this.getNombrepaciente()
						.hashCode());
		result = 37
				* result
				+ (getCodigoestadofacturacion() == null ? 0 : this
						.getCodigoestadofacturacion().hashCode());
		result = 37
				* result
				+ (getNombreestadofacturacion() == null ? 0 : this
						.getNombreestadofacturacion().hashCode());
		result = 37
				* result
				+ (getCodigoestadopaciente() == null ? 0 : this
						.getCodigoestadopaciente().hashCode());
		result = 37
				* result
				+ (getNombreestadopaciente() == null ? 0 : this
						.getNombreestadopaciente().hashCode());
		result = 37
				* result
				+ (getEmpresainstitucion() == null ? 0 : this
						.getEmpresainstitucion().hashCode());
		result = 37
				* result
				+ (getNomempresainstitucion() == null ? 0 : this
						.getNomempresainstitucion().hashCode());
		result = 37
				* result
				+ (getEntidadsubcontratada() == null ? 0 : this
						.getEntidadsubcontratada().hashCode());
		result = 37
				* result
				+ (getDescentidadsubcontratada() == null ? 0 : this
						.getDescentidadsubcontratada().hashCode());
		result = 37
				* result
				+ (getInstitucion() == null ? 0 : this.getInstitucion()
						.hashCode());
		return result;
	}

}
