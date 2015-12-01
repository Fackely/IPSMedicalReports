package util.odontologia;

import java.io.Serializable;

import util.InfoDatosInt;
import util.InfoDatosString;

public class InfoDetDefinirSolucitudDsctOdon implements Serializable {
	
	
	private InfoDatosInt centroAtencion;
	private String  fechaPresupuesto;
	private String  horaPresupuesto;
	private String  estadoAutorizacionDescuento;
	private String  usuario; // por definir?
	private double descuento;
	private String tipo; // por validar
	private InfoDatosString motivo;
	private String observacion;
	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the fechaPresupuesto
	 */
	public String getFechaPresupuesto() {
		return fechaPresupuesto;
	}
	/**
	 * @param fechaPresupuesto the fechaPresupuesto to set
	 */
	public void setFechaPresupuesto(String fechaPresupuesto) {
		this.fechaPresupuesto = fechaPresupuesto;
	}
	/**
	 * @return the horaPresupuesto
	 */
	public String getHoraPresupuesto() {
		return horaPresupuesto;
	}
	/**
	 * @param horaPresupuesto the horaPresupuesto to set
	 */
	public void setHoraPresupuesto(String horaPresupuesto) {
		this.horaPresupuesto = horaPresupuesto;
	}
	/**
	 * @return the estadoAutorizacionDescuento
	 */
	public String getEstadoAutorizacionDescuento() {
		return estadoAutorizacionDescuento;
	}
	/**
	 * @param estadoAutorizacionDescuento the estadoAutorizacionDescuento to set
	 */
	public void setEstadoAutorizacionDescuento(String estadoAutorizacionDescuento) {
		this.estadoAutorizacionDescuento = estadoAutorizacionDescuento;
	}
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the descuento
	 */
	public double getDescuento() {
		return descuento;
	}
	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the motivo
	 */
	public InfoDatosString getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(InfoDatosString motivo) {
		this.motivo = motivo;
	}
	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}
	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
}
