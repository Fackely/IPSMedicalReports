package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class DtoExistenciaAgenOdon implements Serializable
{
	private String horaIni;
	private boolean modificarHoraIni;
	private String horaFin;
	private boolean modificarHoraFin;
	private int codigoPk;
	private boolean verificarDto;
	
	public DtoExistenciaAgenOdon()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.horaIni = "";
		this.modificarHoraIni = false;
		this.horaFin = "";
		this.modificarHoraFin = false;
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.verificarDto = false;
	}

	/**
	 * @return the horaIni
	 */
	public String getHoraIni() {
		return horaIni;
	}

	/**
	 * @param horaIni the horaIni to set
	 */
	public void setHoraIni(String horaIni) {
		this.horaIni = horaIni;
	}

	/**
	 * @return the modificarHoraIni
	 */
	public boolean isModificarHoraIni() {
		return modificarHoraIni;
	}

	/**
	 * @param modificarHoraIni the modificarHoraIni to set
	 */
	public void setModificarHoraIni(boolean modificarHoraIni) {
		this.modificarHoraIni = modificarHoraIni;
	}

	/**
	 * @return the horaFin
	 */
	public String getHoraFin() {
		return horaFin;
	}

	/**
	 * @param horaFin the horaFin to set
	 */
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * @return the modificarHoraFin
	 */
	public boolean isModificarHoraFin() {
		return modificarHoraFin;
	}

	/**
	 * @param modificarHoraFin the modificarHoraFin to set
	 */
	public void setModificarHoraFin(boolean modificarHoraFin) {
		this.modificarHoraFin = modificarHoraFin;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the verificarDto
	 */
	public boolean isVerificarDto() {
		return verificarDto;
	}

	/**
	 * @param verificarDto the verificarDto to set
	 */
	public void setVerificarDto(boolean verificarDto) {
		this.verificarDto = verificarDto;
	}
	
}
