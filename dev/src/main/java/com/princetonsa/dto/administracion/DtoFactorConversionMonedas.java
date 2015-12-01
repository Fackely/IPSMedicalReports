package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.InfoDatosInt;

/**
 * 
 * @author wilson
 *
 */
public class DtoFactorConversionMonedas implements Serializable
{
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private InfoDatosInt codigoMoneda;
	
	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private String factor;
	
	/**
	 * 
	 */
	private int codigoInstitucion;

	/**
	 * 
	 */
	private String loginUsuario;
	
	/**
	 * @param codigo
	 * @param codigoMoneda
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factor
	 * @param codigoInstitucion
	 */
	public DtoFactorConversionMonedas(int codigo, InfoDatosInt codigoMoneda, String fechaInicial, String fechaFinal, String factor, int codigoInstitucion, String loginUsuario) 
	{
		super();
		this.codigo = codigo;
		this.codigoMoneda = codigoMoneda;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.factor = factor;
		this.codigoInstitucion = codigoInstitucion;
		this.loginUsuario= loginUsuario;
	}

	
	/**
	 *Metodo que devuelde un String con la informacion del dto 
	 * @param dto
	 * @return
	 */
	public String obtenerDtoString (DtoFactorConversionMonedas dto)
	{
		String dtoString = "";
		
		dtoString="Codigo==>"+dto.getCodigo()+"|"+"institucion ==>"+dto.getCodigoInstitucion()+"|"+"factor==>"+dto.getFactor()+"|" +
				"fechaFinal==>"+dto.getFechaFinal()+"|"+"fechaInicial==>"+dto.getFechaInicial()+"|"+"usuario==>"+dto.getLoginUsuario()+"|" +
						"CodigoTipoMoneda==>"+dto.getCodigoMoneda();
		
		return dtoString;
	}
	
	public String obtenerDtoString ()
	{
		String dtoString = "";
		
		dtoString="Codigo==>"+this.getCodigo()+"|"+"institucion ==>"+this.getCodigoInstitucion()+"|"+"factor==>"+this.getFactor()+"|" +
				"fechaFinal==>"+this.getFechaFinal()+"|"+"fechaInicial==>"+this.fechaInicial+"|"+"usuario==>"+this.getLoginUsuario()+"|" +
						"CodigoTipoMoneda==>"+this.getCodigoMoneda().getCodigo();
		
		return dtoString;
	}
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoMoneda
	 */
	public InfoDatosInt getCodigoMoneda() {
		return codigoMoneda;
	}

	/**
	 * @param codigoMoneda the codigoMoneda to set
	 */
	public void setCodigoMoneda(InfoDatosInt codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}

	/**
	 * @return the factor
	 */
	public String getFactor() {
		return factor;
	}

	/**
	 * @param factor the factor to set
	 */
	public void setFactor(String factor) {
		this.factor = factor;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	
	
	
	
}
