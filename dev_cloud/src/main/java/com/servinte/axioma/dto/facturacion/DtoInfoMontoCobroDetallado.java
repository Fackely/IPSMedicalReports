/**
 * 
 */
package com.servinte.axioma.dto.facturacion;

import util.ConstantesBD;

/**
 * @author armando
 *
 */
public class DtoInfoMontoCobroDetallado 
{
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private int articulo;
	
	/**
	 * 
	 */
	private int grupoServicio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private int especialidad;
	
	/**
	 * 
	 */
	private int claseArticulo;
	
	/**
	 * 
	 */
	private int grupoArticulo;

	/**
	 * 
	 */
	private int subGrupoArticulo;
	
	/**
	 * 
	 */
	private int codigoSubgrupoArticulo;
	
	/**
	 * 
	 */
	private String naturalezaArticulo;

	/**
	 * 
	 */
	private int servicio;
	
	/**
	 * 
	 */
	private int cantidadArticuloServicio;
	
	/**
	 * 
	 */
	private int cantidadMonto;
	
	/**
	 * 
	 */
	private double valorMonto;
	
	/**
	 * 
	 */
	private boolean porAgrupacion;
	
	
	

	/**
	 * 
	 */
	public DtoInfoMontoCobroDetallado() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.articulo = ConstantesBD.codigoNuncaValido;
		this.grupoServicio = ConstantesBD.codigoNuncaValido;
		this.tipoServicio = "";
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.claseArticulo = ConstantesBD.codigoNuncaValido;
		this.grupoArticulo = ConstantesBD.codigoNuncaValido;
		this.subGrupoArticulo = ConstantesBD.codigoNuncaValido;
		this.codigoSubgrupoArticulo = ConstantesBD.codigoNuncaValido;
		this.servicio = ConstantesBD.codigoNuncaValido;
		this.cantidadArticuloServicio = ConstantesBD.codigoNuncaValido;
		this.cantidadMonto = ConstantesBD.codigoNuncaValido;
		this.valorMonto = 0.0;
		this.porAgrupacion = false;
		this.naturalezaArticulo="";
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getArticulo() {
		return articulo;
	}

	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	public int getGrupoServicio() {
		return grupoServicio;
	}

	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public int getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}

	public int getClaseArticulo() {
		return claseArticulo;
	}

	public void setClaseArticulo(int claseArticulo) {
		this.claseArticulo = claseArticulo;
	}

	public int getGrupoArticulo() {
		return grupoArticulo;
	}

	public void setGrupoArticulo(int grupoArticulo) {
		this.grupoArticulo = grupoArticulo;
	}

	public int getSubGrupoArticulo() {
		return subGrupoArticulo;
	}

	public void setSubGrupoArticulo(int subGrupoArticulo) {
		this.subGrupoArticulo = subGrupoArticulo;
	}

	public int getServicio() {
		return servicio;
	}

	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	

	public int getCantidadMonto() {
		return cantidadMonto;
	}

	public void setCantidadMonto(int cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}

	public double getValorMonto() {
		return valorMonto;
	}

	public void setValorMonto(double valorMonto) {
		this.valorMonto = valorMonto;
	}

	public boolean isPorAgrupacion() {
		return porAgrupacion;
	}

	public void setPorAgrupacion(boolean porAgrupacion) {
		this.porAgrupacion = porAgrupacion;
	}

	public int getCodigoSubgrupoArticulo() {
		return codigoSubgrupoArticulo;
	}

	public void setCodigoSubgrupoArticulo(int codigoSubgrupoArticulo) {
		this.codigoSubgrupoArticulo = codigoSubgrupoArticulo;
	}

	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}

	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}
	
	public boolean isExiste()
	{
		return this.codigo>0;
	}

	public int getCantidadArticuloServicio() {
		return cantidadArticuloServicio;
	}

	public void setCantidadArticuloServicio(int cantidadArticuloServicio) {
		this.cantidadArticuloServicio = cantidadArticuloServicio;
	}
	
}
