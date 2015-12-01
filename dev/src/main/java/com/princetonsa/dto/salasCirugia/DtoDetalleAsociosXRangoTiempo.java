package com.princetonsa.dto.salasCirugia;

import java.io.Serializable;


/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 * 31/10/2007
 */

public class DtoDetalleAsociosXRangoTiempo implements Serializable
{
	/*---------------------------------------------------------------------
	 * ESTOS ATRIBUTOS PERTENENCEN A LA TABLA  det_asoc_x_ran_tiem
	 ---------------------------------------------------------------------*/
	/**
	 * key de la tabla  det_asoc_x_ran_tiem y es referencia a la tabla
	 * asocios_x_rango_tiempo con el codigo
	 */
	private Integer codigoDetAsocXRangTiem;
	
	/**
	 * codigo que indica que tarifario ofical es
	 */
	private Integer codigoTarifarioOficial;
	
	/**
	 * codigo digitado para el tarifario oficial
	 */
	private String  numeroCodigoTarifarioOficial;
	
	private String  numeroCodigoTarifarioOficialOld;

		
	/*---------------------------------------------------------------------
	 *  FIN ESTOS ATRIBUTOS PERTENENCEN A LA TABLA  det_asoc_x_ran_tiem
	 ---------------------------------------------------------------------*/
	
	/*--------------------------------------------
	 * METODOS DETTERS AND SETTERS
	 -------------------------------------------*/
	
	public Integer getCodigoDetAsocXRangTiem() {
		return codigoDetAsocXRangTiem;
	}

	public void setCodigoDetAsocXRangTiem(Integer codigoDetAsocXRangTiem) {
		this.codigoDetAsocXRangTiem = codigoDetAsocXRangTiem;
	}

	public Integer getCodigoTarifarioOficial() {
		return codigoTarifarioOficial;
	}

	public void setCodigoTarifarioOficial(Integer codigoTarifarioOficial) {
		this.codigoTarifarioOficial = codigoTarifarioOficial;
	}

	public String getNumeroCodigoTarifarioOficial() {
		return numeroCodigoTarifarioOficial;
	}

	public void setNumeroCodigoTarifarioOficial(String numeroCodigoTarifarioOficial) {
		this.numeroCodigoTarifarioOficial = numeroCodigoTarifarioOficial;
	}
	
	public String getNumeroCodigoTarifarioOficialOld() {
		return numeroCodigoTarifarioOficialOld;
	}

	public void setNumeroCodigoTarifarioOficialOld(
			String numeroCodigoTarifarioOficialOld) {
		this.numeroCodigoTarifarioOficialOld = numeroCodigoTarifarioOficialOld;
	}
	
	
	/*--------------------------------------------
	 * FIN METODOS DETTERS AND SETTERS
	 -------------------------------------------*/
	
	/*-----------------------------------------------
	 * DEFINICION DE OCNSTRUCTORES
	 ----------------------------------------------*/
	/**
	 * Constructor por defecto 
	 */
	public DtoDetalleAsociosXRangoTiempo ()
	{
		this.codigoDetAsocXRangTiem = 0;
		this.codigoTarifarioOficial = 0;
		this.numeroCodigoTarifarioOficial = "";
		this.numeroCodigoTarifarioOficialOld = "";
	}
	
	
	/**
	 * Constructor  
	 * @param codigoDetAsocXRangTiem
	 * @param codigoTarifarioOficial
	 * @param numeroCodigoTarifarioOficial
	 */
	public DtoDetalleAsociosXRangoTiempo (Integer codigoDetAsocXRangTiem,Integer codigoTarifarioOficial,String numeroCodigoTarifarioOficial ,String numeroCodigoTarifarioOficialOld)
	{
		this.codigoDetAsocXRangTiem = codigoDetAsocXRangTiem;
		this.codigoTarifarioOficial = codigoTarifarioOficial;
		this.numeroCodigoTarifarioOficial = numeroCodigoTarifarioOficial;
		this.numeroCodigoTarifarioOficialOld = numeroCodigoTarifarioOficialOld;
	}

	
	/*-----------------------------------------------
	 * FIN DEFINICION DE OCNSTRUCTORES
	 ----------------------------------------------*/
}