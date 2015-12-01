package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.InfoDatosAcronimo;
import util.InfoDatosInt;

/**
 * Clase para listar los ingresos para la factura odontologica
 * @author axioma
 *
 */
public class DtoIngresosFactura implements Serializable 
{
	/**
	 * pk de ingresos
	 */
	private BigDecimal codigoIngreso;
	
	/**
	 * codigo a mostrar de ingresos 
	 */
	private String consecutivoIngreso;
	
	/**
	 * estado del ingreso
	 */
	private InfoDatosAcronimo estadoIngreso;
	
	/**
	 * fecha de apertura del ingreso en formato app
	 */
	private String fechaAperturaIngresoApp;
	
	/**
	 * fecha del cierre del ingreso en formato app
	 */
	private String fechaCierreIngresoApp;
	
	/**
	 * cuenta
	 */
	private BigDecimal cuenta;
	
	/**
	 * estado de la cuenta
	 */
	private InfoDatosInt estadoCuenta;
	
	/**
	 * info datos de la via de ingreso 
	 */
	private InfoDatosInt viaIngresoCuenta;
	
	/**
	 * Constructor
	 * @param codigoIngreso
	 * @param consecutivoIngreso
	 * @param estadoIngreso
	 * @param fechaAperturaIngresoApp
	 * @param fechaCierreIngresoApp
	 * @param cuenta
	 * @param estadoCuenta
	 * @param viaIngresoCuenta
	 */
	public DtoIngresosFactura(	BigDecimal codigoIngreso,
								String consecutivoIngreso, 
								InfoDatosAcronimo estadoIngreso,
								String fechaAperturaIngresoApp, 
								String fechaCierreIngresoApp,
								BigDecimal cuenta, 
								InfoDatosInt estadoCuenta,
								InfoDatosInt viaIngresoCuenta) 
	{
		super();
		this.codigoIngreso = codigoIngreso;
		this.consecutivoIngreso = consecutivoIngreso;
		this.estadoIngreso = estadoIngreso;
		this.fechaAperturaIngresoApp = fechaAperturaIngresoApp;
		this.fechaCierreIngresoApp = fechaCierreIngresoApp;
		this.cuenta = cuenta;
		this.estadoCuenta = estadoCuenta;
		this.viaIngresoCuenta = viaIngresoCuenta;
	}

	/**
	 * @return the codigoIngreso
	 */
	public BigDecimal getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(BigDecimal codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	/**
	 * @return the estadoIngreso
	 */
	public InfoDatosAcronimo getEstadoIngreso() {
		return estadoIngreso;
	}

	/**
	 * @param estadoIngreso the estadoIngreso to set
	 */
	public void setEstadoIngreso(InfoDatosAcronimo estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}

	/**
	 * @return the fechaAperturaIngresoApp
	 */
	public String getFechaAperturaIngresoApp() {
		return fechaAperturaIngresoApp;
	}

	/**
	 * @param fechaAperturaIngresoApp the fechaAperturaIngresoApp to set
	 */
	public void setFechaAperturaIngresoApp(String fechaAperturaIngresoApp) {
		this.fechaAperturaIngresoApp = fechaAperturaIngresoApp;
	}

	/**
	 * @return the fechaCierreIngresoApp
	 */
	public String getFechaCierreIngresoApp() {
		return fechaCierreIngresoApp;
	}

	/**
	 * @param fechaCierreIngresoApp the fechaCierreIngresoApp to set
	 */
	public void setFechaCierreIngresoApp(String fechaCierreIngresoApp) {
		this.fechaCierreIngresoApp = fechaCierreIngresoApp;
	}

	/**
	 * @return the cuenta
	 */
	public BigDecimal getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(BigDecimal cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the estadoCuenta
	 */
	public InfoDatosInt getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(InfoDatosInt estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	/**
	 * @return the viaIngresoCuenta
	 */
	public InfoDatosInt getViaIngresoCuenta() {
		return viaIngresoCuenta;
	}

	/**
	 * @param viaIngresoCuenta the viaIngresoCuenta to set
	 */
	public void setViaIngresoCuenta(InfoDatosInt viaIngresoCuenta) {
		this.viaIngresoCuenta = viaIngresoCuenta;
	}
	
}