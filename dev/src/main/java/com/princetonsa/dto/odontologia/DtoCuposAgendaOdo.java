/*
 * FEb 04, 2010
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * DTO para el manejo del registro de cupos agenda odo ocupados
 * @author Sebastián Gómez
 *
 */
public class DtoCuposAgendaOdo implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigoPk;
	private BigDecimal agendaOdontologica;
	private String hora;
	private int cuposDisponibles;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	
	public DtoCuposAgendaOdo()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.agendaOdontologica = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.hora = "";
		this.cuposDisponibles = 0;
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the agendaOdontologica
	 */
	public BigDecimal getAgendaOdontologica() {
		return agendaOdontologica;
	}

	/**
	 * @param agendaOdontologica the agendaOdontologica to set
	 */
	public void setAgendaOdontologica(BigDecimal agendaOdontologica) {
		this.agendaOdontologica = agendaOdontologica;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the cuposDisponibles
	 */
	public int getCuposDisponibles() {
		return cuposDisponibles;
	}

	/**
	 * @param cuposDisponibles the cuposDisponibles to set
	 */
	public void setCuposDisponibles(int cuposDisponibles) {
		this.cuposDisponibles = cuposDisponibles;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

}
