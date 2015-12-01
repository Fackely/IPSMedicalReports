package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoAgrupacionHallazgoSuperficie implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7583562393987920761L;
	private int hallazgo;
	private BigDecimal programaServicio;
	private int numeroSuperficies;
	private int contador;
	
	/**
	 * 
	 */
	public DtoAgrupacionHallazgoSuperficie() 
	{
		super();
		this.hallazgo = ConstantesBD.codigoNuncaValido;
		this.programaServicio = BigDecimal.ZERO;
		this.numeroSuperficies = 0;
		this.contador = 0;
	}
	
	/**
	 * 
	 * @param hallazgo
	 * @param programaServicio
	 * @param numeroSuperficies
	 * @param contador
	 */
	public DtoAgrupacionHallazgoSuperficie(int hallazgo,
			BigDecimal programaServicio, int numeroSuperficies, int contador) {
		super();
		this.hallazgo = hallazgo;
		this.programaServicio = programaServicio;
		this.numeroSuperficies = numeroSuperficies;
		this.contador = contador;
	}

	/**
	 * @return the hallazgo
	 */
	public int getHallazgo() {
		return hallazgo;
	}
	/**
	 * @param hallazgo the hallazgo to set
	 */
	public void setHallazgo(int hallazgo) {
		this.hallazgo = hallazgo;
	}
	/**
	 * @return the programaServicio
	 */
	public BigDecimal getProgramaServicio() {
		return programaServicio;
	}
	/**
	 * @param programaServicio the programaServicio to set
	 */
	public void setProgramaServicio(BigDecimal programaServicio) {
		this.programaServicio = programaServicio;
	}
	/**
	 * @return the numeroSuperficies
	 */
	public int getNumeroSuperficies() {
		return numeroSuperficies;
	}
	/**
	 * @param numeroSuperficies the numeroSuperficies to set
	 */
	public void setNumeroSuperficies(int numeroSuperficies) {
		this.numeroSuperficies = numeroSuperficies;
	}
	/**
	 * @return the contador
	 */
	public int getContador() {
		return contador;
	}
	/**
	 * @param contador the contador to set
	 */
	public void setContador(int contador) {
		this.contador = contador;
	}
	
	/**
	 * 
	 */
	public void incremetarContador() 
	{
		this.setContador(this.getContador()+1);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCantidadCALCULADA()
	{
		if( this.getNumeroSuperficies()<=1 || ((this.contador%this.getNumeroSuperficies())!=0) || this.contador<this.getNumeroSuperficies() )
		{
			return this.contador;
		}
		else
		{
			return ((this.contador/this.getNumeroSuperficies()));
		}
	}
	
}
