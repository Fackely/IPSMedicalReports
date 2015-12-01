package com.princetonsa.dto.salas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoEventos implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private int orden;
	
	/**
	 * 
	 */
	private boolean obligatorio;
	
	/**
	 * 
	 */
	private String convencionInicio;
	
	/**
	 * 
	 */
	private String convencionFin;
	
	/**
	 * 
	 */
	private DtoColor dtoColor;

	/**
	 * 
	 */
	private boolean nRegistros;
	
	/**
	 * 
	 */
	private boolean llevaFechaFin;
	
	/**
	 * 
	 * */
	private String tieneInfo;
	
	/**
	 * 
	 * @param nombre
	 * @param orden
	 * @param obligatorio
	 * @param convencionInicio
	 * @param convencionFin
	 * @param color
	 */
	public DtoEventos(String nombre, int orden, boolean obligatorio, String convencionInicio, String convencionFin, DtoColor dtoColor, boolean nRegistros, boolean llevaFechaFin) 
	{
		super();
		this.nombre = nombre;
		this.orden = orden;
		this.obligatorio = obligatorio;
		this.convencionInicio = convencionInicio;
		this.convencionFin = convencionFin;
		this.dtoColor = dtoColor;
		this.nRegistros=nRegistros;
		this.llevaFechaFin=llevaFechaFin;
	}

	/**
	 * 
	 *
	 */
	public DtoEventos() 
	{
		super();
		this.nombre = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.obligatorio = false;
		this.convencionInicio = "";
		this.convencionFin = "";
		this.dtoColor = new DtoColor();
		this.nRegistros=false;
		this.llevaFechaFin=false;
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
	 * @return the convencionFin
	 */
	public String getConvencionFin() {
		return convencionFin;
	}

	/**
	 * @param convencionFin the convencionFin to set
	 */
	public void setConvencionFin(String convencionFin) {
		this.convencionFin = convencionFin;
	}

	/**
	 * @return the convencionInicio
	 */
	public String getConvencionInicio() {
		return convencionInicio;
	}

	/**
	 * @param convencionInicio the convencionInicio to set
	 */
	public void setConvencionInicio(String convencionInicio) {
		this.convencionInicio = convencionInicio;
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
	 * @return the obligatorio
	 */
	public boolean getObligatorio() {
		return obligatorio;
	}

	/**
	 * @param obligatorio the obligatorio to set
	 */
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the dtoColor
	 */
	public DtoColor getDtoColor() {
		return dtoColor;
	}

	/**
	 * @param dtoColor the dtoColor to set
	 */
	public void setDtoColor(DtoColor dtoColor) {
		this.dtoColor = dtoColor;
	}

	/**
	 * @return the llevaFechaFin
	 */
	public boolean getLlevaFechaFin() {
		return llevaFechaFin;
	}

	/**
	 * @param llevaFechaFin the llevaFechaFin to set
	 */
	public void setLlevaFechaFin(boolean llevaFechaFin) {
		this.llevaFechaFin = llevaFechaFin;
	}

	/**
	 * @return the nRegistros
	 */
	public boolean getNRegistros() {
		return nRegistros;
	}

	/**
	 * @param registros the nRegistros to set
	 */
	public void setNRegistros(boolean registros) {
		nRegistros = registros;
	}

	/**
	 * @return the tieneInfo
	 */
	public String getTieneInfo() {
		return tieneInfo;
	}

	/**
	 * @param tieneInfo the tieneInfo to set
	 */
	public void setTieneInfo(String tieneInfo) {
		this.tieneInfo = tieneInfo;
	}
	
}