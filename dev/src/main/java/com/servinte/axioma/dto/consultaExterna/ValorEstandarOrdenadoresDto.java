/**
 * 
 */
package com.servinte.axioma.dto.consultaExterna;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ValorEstandarOrdenadoresDto implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 560404626723759184L;
	/**
	 * 
	 */
	private int codigo;
	/**
	 * 
	 */
	private int unidadAgenda;
	/**
	 * 
	 */
	private String nombreUnidadAgenda;
	/**
	 * 
	 */
	private BigDecimal valorEstOrdCita;
	/**
	 * 
	 */
	private BigDecimal valorEstSermedOrden;
	/**
	 * 
	 */
	private BigDecimal valorEstSermedCita;
	/**
	 * 
	 */
	private List<ValorEstandarOrdenadoresDto> listaOrdenadores;
	/**
	 * 
	 */
	private Integer codigoGruposServicio;
	/**
	 * 
	 */
	private String nombreGrupoServicio;
	/**
	 * 
	 */
	private Integer codigoClaseInventarios;
	/**
	 * 
	 */
	private String nombreClaseInventarios;
	/**
	 * 
	 */
	private boolean esServicio;
	
	private int tipoOrden;
	
	
	public ValorEstandarOrdenadoresDto(){
		listaOrdenadores = new ArrayList<ValorEstandarOrdenadoresDto>();
	}
	
	public ValorEstandarOrdenadoresDto(int codigo, int codigoGrupoClase, String nombreGrupoClase, int unidadAgenda, String nombreUnidadAgenda, BigDecimal valorEstOrdCita, 
			BigDecimal valorEstSermedOrden,BigDecimal valorEstSermedCita, boolean esServicio){
		this.codigo=codigo;
		if(esServicio){
			this.codigoGruposServicio=codigoGrupoClase;
			this.nombreGrupoServicio=nombreGrupoClase;
		}else{
			this.codigoClaseInventarios=codigoGrupoClase;
			this.nombreClaseInventarios=nombreGrupoClase;
		}
		this.unidadAgenda=unidadAgenda;
		this.nombreUnidadAgenda=nombreUnidadAgenda;
		this.valorEstOrdCita=valorEstOrdCita;
		this.valorEstSermedOrden=valorEstSermedOrden;
		this.valorEstSermedCita=valorEstSermedCita;
		this.esServicio=esServicio;
		
		
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getUnidadAgenda() {
		return unidadAgenda;
	}

	public void setUnidadAgenda(int unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	public String getNombreUnidadAgenda() {
		return nombreUnidadAgenda;
	}

	public void setNombreUnidadAgenda(String nombreUnidadAgenda) {
		this.nombreUnidadAgenda = nombreUnidadAgenda;
	}

	public BigDecimal getValorEstOrdCita() {
		return valorEstOrdCita;
	}

	public void setValorEstOrdCita(BigDecimal valorEstOrdCita) {
		this.valorEstOrdCita = valorEstOrdCita;
	}

	public BigDecimal getValorEstSermedOrden() {
		return valorEstSermedOrden;
	}

	public void setValorEstSermedOrden(BigDecimal valorEstSermedOrden) {
		this.valorEstSermedOrden = valorEstSermedOrden;
	}

	public BigDecimal getValorEstSermedCita() {
		return valorEstSermedCita;
	}

	public void setValorEstSermedCita(BigDecimal valorEstSermedCita) {
		this.valorEstSermedCita = valorEstSermedCita;
	}

	public List<ValorEstandarOrdenadoresDto> getListaOrdenadores() {
		return listaOrdenadores;
	}

	public void setListaOrdenadores(
			List<ValorEstandarOrdenadoresDto> listaOrdenadores) {
		this.listaOrdenadores = listaOrdenadores;
	}

	public Integer getCodigoGrupoServicio() {
		return codigoGruposServicio;
	}

	public void setCodigoGruposServicio(Integer codigoGruposServicio) {
		this.codigoGruposServicio = codigoGruposServicio;
	}

	
	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}

	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
	}

	public Integer getCodigoGruposServicio() {
		return codigoGruposServicio;
	}

	public Integer getCodigoClaseInventarios() {
		return codigoClaseInventarios;
	}

	public void setCodigoClaseInventarios(Integer codigoClaseInventarios) {
		this.codigoClaseInventarios = codigoClaseInventarios;
	}

	public String getNombreClaseInventarios() {
		return nombreClaseInventarios;
	}

	public void setNombreClaseInventarios(String nombreClaseInventarios) {
		this.nombreClaseInventarios = nombreClaseInventarios;
	}

	public boolean isEsServicio() {
		return esServicio;
	}

	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}

	public int getTipoOrden() {
		return tipoOrden;
	}

	public void setTipoOrden(int tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	
	
	

	
	
}