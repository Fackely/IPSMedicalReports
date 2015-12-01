/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jeilones
 * @created 26/06/2013
 *
 */
public class DescripcionOperatoriaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3454639618764041036L;
	
	private int codigo;
	private String descripcion;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private String usuarioGrabacion;
	/**
	 * 
	 * @author jeilones
	 * @created 26/06/2013
	 */
	public DescripcionOperatoriaDto() {
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaGrabacion() {
		return fechaGrabacion;
	}
	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}
	public String getHoraGrabacion() {
		return horaGrabacion;
	}
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}
	public String getUsuarioGrabacion() {
		return usuarioGrabacion;
	}
	public void setUsuarioGrabacion(String usuarioGrabacion) {
		this.usuarioGrabacion = usuarioGrabacion;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DescripcionOperatoriaDto clone() throws CloneNotSupportedException {
		DescripcionOperatoriaDto nuevaDescripcionOperatoria=new DescripcionOperatoriaDto();
		nuevaDescripcionOperatoria.setCodigo(this.getCodigo());
		nuevaDescripcionOperatoria.setDescripcion(this.getDescripcion());
		nuevaDescripcionOperatoria.setFechaGrabacion(this.getFechaGrabacion());
		nuevaDescripcionOperatoria.setHoraGrabacion(this.getHoraGrabacion());
		nuevaDescripcionOperatoria.setUsuarioGrabacion(this.getUsuarioGrabacion());
		
		return nuevaDescripcionOperatoria;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((fechaGrabacion == null) ? 0 : fechaGrabacion.hashCode());
		result = prime * result
				+ ((horaGrabacion == null) ? 0 : horaGrabacion.hashCode());
		result = prime
				* result
				+ ((usuarioGrabacion == null) ? 0 : usuarioGrabacion.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DescripcionOperatoriaDto)) {
			return false;
		}
		DescripcionOperatoriaDto other = (DescripcionOperatoriaDto) obj;
		if (codigo != other.codigo) {
			return false;
		}
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (fechaGrabacion == null) {
			if (other.fechaGrabacion != null) {
				return false;
			}
		} else if (!fechaGrabacion.equals(other.fechaGrabacion)) {
			return false;
		}
		if (horaGrabacion == null) {
			if (other.horaGrabacion != null) {
				return false;
			}
		} else if (!horaGrabacion.equals(other.horaGrabacion)) {
			return false;
		}
		if (usuarioGrabacion == null) {
			if (other.usuarioGrabacion != null) {
				return false;
			}
		} else if (!usuarioGrabacion.equals(other.usuarioGrabacion)) {
			return false;
		}
		return true;
	}
}
