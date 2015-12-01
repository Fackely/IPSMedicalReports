package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatosInt;

public class DtoLogPresupuestoOdontologico implements Serializable {

	
	private BigDecimal codigoPK ;
	private BigDecimal codigoPresupuesto;
	private InfoDatosInt motivo ;
	private InfoDatosInt especialidad;
	private String estado;
	private DtoInfoFechaUsuario  usuarioModifica;
	
	
	public DtoLogPresupuestoOdontologico() {
		
		this.reset();
	}
	
	public void reset(){
		this.codigoPK = new BigDecimal(0);
		this.codigoPresupuesto = new BigDecimal(0);
		this.estado="";
		this.motivo= new InfoDatosInt();
		this.especialidad= new InfoDatosInt();
		this.usuarioModifica = new DtoInfoFechaUsuario();
	}

	/**
	 * @return the codigoPK
	 */
	public BigDecimal getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(BigDecimal codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto() {
		return codigoPresupuesto;
	}

	/**
	 * @param codigoPresupuesto the codigoPresupuesto to set
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * @return the motivo
	 */
	public InfoDatosInt getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(InfoDatosInt motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the especialidad
	 */
	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the usuarioModifica
	 */
	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
}
