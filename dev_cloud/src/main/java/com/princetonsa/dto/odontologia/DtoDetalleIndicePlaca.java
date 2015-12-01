package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoDetalleIndicePlaca implements Serializable
{
	private int codigoPk;
	private int codigoIndicePlaca;
	private InfoDatosInt piezaDental;
	private DtoDetSuperficieIndicePlaca superficie;
	private String indicador;
	
	// Atributos accion ejecutar
	private String eliminar;
	private String modificar;
	private String nuevo;
	private String contado;
	
	public DtoDetalleIndicePlaca()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoIndicePlaca = ConstantesBD.codigoNuncaValido;
		this.piezaDental = new InfoDatosInt();
		this.superficie = new DtoDetSuperficieIndicePlaca();
		this.indicador = "";
		
		// Atributos accion ejecutar
		this.eliminar = ConstantesBD.acronimoNo;
		this.modificar = ConstantesBD.acronimoNo;
		this.nuevo = ConstantesBD.acronimoNo;
		this.contado = ConstantesBD.acronimoNo;
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
	 * @return the codigoIndicePlaca
	 */
	public int getCodigoIndicePlaca() {
		return codigoIndicePlaca;
	}

	/**
	 * @param codigoIndicePlaca the codigoIndicePlaca to set
	 */
	public void setCodigoIndicePlaca(int codigoIndicePlaca) {
		this.codigoIndicePlaca = codigoIndicePlaca;
	}

	/**
	 * @return the indicador
	 */
	public String getIndicador() {
		return indicador;
	}

	/**
	 * @param indicador the indicador to set
	 */
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	/**
	 * @return the piezaDental
	 */
	public InfoDatosInt getPiezaDental() {
		return piezaDental;
	}

	/**
	 * @param piezaDental the piezaDental to set
	 */
	public void setPiezaDental(InfoDatosInt piezaDental) {
		this.piezaDental = piezaDental;
	}

	/**
	 * @return the eliminar
	 */
	public String getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the modificar
	 */
	public String getModificar() {
		return modificar;
	}

	/**
	 * @param modificar the modificar to set
	 */
	public void setModificar(String modificar) {
		this.modificar = modificar;
	}

	/**
	 * @return the nuevo
	 */
	public String getNuevo() {
		return nuevo;
	}

	/**
	 * @param nuevo the nuevo to set
	 */
	public void setNuevo(String nuevo) {
		this.nuevo = nuevo;
	}

	/**
	 * @return the superficie
	 */
	public DtoDetSuperficieIndicePlaca getSuperficie() {
		return superficie;
	}

	/**
	 * @param superficie the superficie to set
	 */
	public void setSuperficie(DtoDetSuperficieIndicePlaca superficie) {
		this.superficie = superficie;
	}

	/**
	 * @return the contado
	 */
	public String getContado() {
		return contado;
	}

	/**
	 * @param contado the contado to set
	 */
	public void setContado(String contado) {
		this.contado = contado;
	}
	
}
