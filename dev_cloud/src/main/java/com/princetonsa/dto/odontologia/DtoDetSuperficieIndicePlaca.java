package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */
public class DtoDetSuperficieIndicePlaca implements Serializable
{
	private int codigoPk;
	private String nombre;
	private int sector;
	
		
	public DtoDetSuperficieIndicePlaca()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.sector = ConstantesBD.codigoNuncaValido;;
		
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the sector
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(int sector) {
		this.sector = sector;
	}

	

}
