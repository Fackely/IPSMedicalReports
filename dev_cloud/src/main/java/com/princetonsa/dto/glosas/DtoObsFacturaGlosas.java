/**
 * 
 */
package com.princetonsa.dto.glosas;

import java.math.BigDecimal;



/**
 * @author axioma
 *
 */
public class DtoObsFacturaGlosas 
{
	
	private BigDecimal codigo;
	
	private Integer codigoFactura;
	
	private String observacion;
	
	private String fechaObservacion;
	
	private String horaObservacion;
	
	private String usuarioObservacion;
	
	private String nombreUsuario;

	public BigDecimal getCodigo() {
		return codigo;
	}

	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getFechaObservacion() {
		return fechaObservacion;
	}

	public void setFechaObservacion(String fechaObservacion) {
		this.fechaObservacion = fechaObservacion;
	}

	public String getHoraObservacion() {
		return horaObservacion;
	}

	public void setHoraObservacion(String horaObservacion) {
		this.horaObservacion = horaObservacion;
	}

	public String getUsuarioObservacion() {
		return usuarioObservacion;
	}

	public void setUsuarioObservacion(String usuarioObservacion) {
		this.usuarioObservacion = usuarioObservacion;
	}

	public Integer getCodigoFactura() {
		return codigoFactura;
	}

	public void setCodigoFactura(Integer codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

}
