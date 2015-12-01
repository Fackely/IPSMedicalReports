package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 * Dto que contiene los valores totales por cada Nivel de atención
 * ademas de los Grupos de Servicios o Clases de inventario
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoNivelReporte implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7798873716305062575L;

	/**
	 *Atributo que representa el id del Nivel de Atención
	 */
	private long consecutivo;
	
	/**
	 *Atributo que representa el nombre del Nivel de Atención
	 */
	private String nombre;
	
	
	
	/**
	 * Atributo que representa el total presupuestado para el Nivel de Atención
	 */
	private BigDecimal totalPresupuestado;
	
	/**
	 * Atributo que representa el total ordenado para el Nivel de Atención
	 */
	private BigDecimal totalOrdenado;
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo ordenado 
	 * de acuerdo a lo presupuestado para el Nivel de Atención
	 */
	private BigDecimal porcentajeOrdenado;
	
	/**
	 * Atributo que representa el total autorizado para el Nivel de Atención
	 */
	private BigDecimal totalAutorizado;
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo Autorizado
	 * de acuerdo a lo presupuestado para el Nivel de Atención
	 */
	private BigDecimal porcentajeAutorizado;
	
	/**
	 * Atributo que representa el total de los cargos a la cuenta para el Nivel de Atención
	 */
	private BigDecimal totalCargosCuenta;
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo Cargado a la Cuenta 
	 * de acuerdo a lo presupuestado para el Nivel de Atención
	 */
	private BigDecimal porcentajeCargos;
	
	/**
	 * Atributo que representa el total facturado para el Nivel de Atención
	 */
	private BigDecimal totalFacturado;
	
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo Facturado 
	 * de acuerdo a lo presupuestado para el Nivel de Atención
	 */
	private BigDecimal porcentajeFacturado;
	
	/**
	 * Atributo que representa la lista de grupos de servicios o clases de inventarios
	 */
	private ArrayList<DtoGrupoClaseReporte> gruposClases = new ArrayList<DtoGrupoClaseReporte>();
	
	/**
	 * Constructor de la clase
	 */
	public DtoNivelReporte(){
		
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
	 * @return the totalPresupuestado
	 */
	public BigDecimal getTotalPresupuestado() {
		return totalPresupuestado;
	}

	/**
	 * @param totalPresupuestado the totalPresupuestado to set
	 */
	public void setTotalPresupuestado(BigDecimal totalPresupuestado) {
		
		this.totalPresupuestado = totalPresupuestado;
	}

	/**
	 * @return the totalOrdenado
	 */
	public BigDecimal getTotalOrdenado() {
		return totalOrdenado;
	}

	/**
	 * @param totalOrdenado the totalOrdenado to set
	 */
	public void setTotalOrdenado(BigDecimal totalOrdenado) {
		this.totalOrdenado = totalOrdenado;
	}

	/**
	 * @return the totalAutorizado
	 */
	public BigDecimal getTotalAutorizado() {
		return totalAutorizado;
	}

	/**
	 * @param totalAutorizado the totalAutorizado to set
	 */
	public void setTotalAutorizado(BigDecimal totalAutorizado) {
		this.totalAutorizado = totalAutorizado;
	}

	/**
	 * @return the totalCargosCuenta
	 */
	public BigDecimal getTotalCargosCuenta() {
		return totalCargosCuenta;
	}

	/**
	 * @param totalCargosCuenta the totalCargosCuenta to set
	 */
	public void setTotalCargosCuenta(BigDecimal totalCargosCuenta) {
		this.totalCargosCuenta = totalCargosCuenta;
	}

	/**
	 * @return the totalFacturado
	 */
	public BigDecimal getTotalFacturado() {
		return totalFacturado;
	}

	/**
	 * @param totalFacturado the totalFacturado to set
	 */
	public void setTotalFacturado(BigDecimal totalFacturado) {
		this.totalFacturado = totalFacturado;
	}

	/**
	 * @return the gruposClases
	 */
	public ArrayList<DtoGrupoClaseReporte> getGruposClases() {
		return gruposClases;
	}

	/**
	 * @param gruposClases the gruposClases to set
	 */
	public void setGruposClases(ArrayList<DtoGrupoClaseReporte> gruposClases) {
		this.gruposClases = gruposClases;
	}

	/**
	 * @return the porcentajeOrdenado
	 */
	public BigDecimal getPorcentajeOrdenado() {
		if(totalPresupuestado.compareTo(BigDecimal.ZERO)==0){
			return null;
		}
		porcentajeOrdenado=((totalOrdenado.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));
		return porcentajeOrdenado;
	}

	/**
	 * @param porcentajeOrdenado the procentajeOrdenado to set
	 */
	public void setPorcentajeOrdenado(BigDecimal porcentajeOrdenado) {
		this.porcentajeOrdenado = porcentajeOrdenado;
	}

	/**
	 * @return the porcentajeAutorizado
	 */
	public BigDecimal getPorcentajeAutorizado() {
		if(totalPresupuestado.compareTo(BigDecimal.ZERO)==0){
			return null;
		}
		porcentajeAutorizado=((totalAutorizado.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));
		return porcentajeAutorizado;
	}

	/**
	 * @param porcentajeAutorizado the porcentajeAutorizado to set
	 */
	public void setPorcentajeAutorizado(BigDecimal porcentajeAutorizado) {
		this.porcentajeAutorizado = porcentajeAutorizado;
	}

	/**
	 * @return the porcentajeCargos
	 */
	public BigDecimal getPorcentajeCargos() {
		if(totalPresupuestado.compareTo(BigDecimal.ZERO)==0){
			return null;
		}
		porcentajeCargos=((totalCargosCuenta.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));;
		return porcentajeCargos;
	}

	/**
	 * @param porcentajeCargos the porcentajeCargos to set
	 */
	public void setPorcentajeCargos(BigDecimal porcentajeCargos) {
		this.porcentajeCargos = porcentajeCargos;
	}

	/**
	 * @return the porcentajeFacturado
	 */
	public BigDecimal getPorcentajeFacturado() {
		if(totalPresupuestado.compareTo(BigDecimal.ZERO)==0){
			return null;
		}
		porcentajeFacturado=((totalFacturado.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));
		return porcentajeFacturado;
	}

	/**
	 * @param porcentajeFacturado the porcentajeFacturado to set
	 */
	public void setPorcentajeFacturado(BigDecimal porcentajeFacturado) {
		this.porcentajeFacturado = porcentajeFacturado;
	}

	/**
	 * @return the consecutivo
	 */
	public long getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}


}
