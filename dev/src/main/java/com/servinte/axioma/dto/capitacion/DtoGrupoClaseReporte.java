package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Dto que contiene los valores totales por cada Grupo de Servicios o Clase de Inventarios
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoGrupoClaseReporte implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7752736335742255058L;

	/**
	 *Atributo que representa el nombre del Grupo de Servicio o Clase de Inventario
	 */
	private String nombre;
	
	/**
	 * Atributo que representa el total presupuestado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal totalPresupuestado;
	
	/**
	 * Atributo que representa el total ordenado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal totalOrdenado;
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo ordenado 
	 * de acuerdo a lo presupuestado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal porcentajeOrdenado;
	
	/**
	 * Atributo que representa el total autorizado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal totalAutorizado;
	
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo autorizado 
	 * de acuerdo a lo presupuestado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal porcentajeAutorizado;
	
	/**
	 * Atributo que representa el total de los cargos a la cuenta para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal totalCargosCuenta;
	
	/**
	 * Atributo que representa el calculo del porcentaje de los cargos a la cuenta 
	 * de acuerdo a lo presupuestado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal porcentajeCargos;
	
	/**
	 * Atributo que representa el total facturado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal totalFacturado;
	
	/**
	 * Atributo que representa el calculo del porcentaje de lo facturado
	 * de acuerdo a lo presupuestado para el Grupo de Servicio o Clase de Inventario
	 */
	private BigDecimal porcentajeFacturado;
	
	/**
	 * Atributo que representa la lista de productos o servicios del grupo de
	 * servicios o clase de inventarios
	 */
	private ArrayList<DtoProductoServicioReporte> productosServicios = new ArrayList<DtoProductoServicioReporte>();
	
	/**
	 * Constructor de la clase
	 */
	public DtoGrupoClaseReporte(){
		
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
	 * @return the productosServicios
	 */
	public ArrayList<DtoProductoServicioReporte> getProductosServicios() {
		return productosServicios;
	}

	/**
	 * @param productosServicios the productosServicios to set
	 */
	public void setProductosServicios(
			ArrayList<DtoProductoServicioReporte> productosServicios) {
		this.productosServicios = productosServicios;
	}

	/**
	 * @return the porcentajeOrdenado
	 */
	public BigDecimal getPorcentajeOrdenado() {
		if(totalPresupuestado.equals(new BigDecimal(0))){
			return null;
		}
		porcentajeOrdenado=((totalOrdenado.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));
		return porcentajeOrdenado;
	}

	/**
	 * @param porcentajeOrdenado the porcentajeOrdenado to set
	 */
	public void setPorcentajeOrdenado(BigDecimal porcentajeOrdenado) {
		this.porcentajeOrdenado = porcentajeOrdenado;
	}

	/**
	 * @return the porcentajeAutorizado
	 */
	public BigDecimal getPorcentajeAutorizado() {
		if(totalPresupuestado.equals(new BigDecimal(0))){
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
		if(totalPresupuestado.equals(new BigDecimal(0))){
			return null;
		}
		porcentajeCargos=((totalCargosCuenta.multiply(new BigDecimal(100))).divide(totalPresupuestado, 2, RoundingMode.HALF_UP));
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
		if(totalPresupuestado.equals(new BigDecimal(0))){
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

}
