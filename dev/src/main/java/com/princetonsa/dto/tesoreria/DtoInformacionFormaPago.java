package com.princetonsa.dto.tesoreria;

import java.io.Serializable;


/**
 * Dto para controlar la información de cada una de las formas de
 * pago para los recibos de caja
 * @author Juan David Ramírez
 * @version 1.0.0
 * @since 17 Septiembre 2010
 */
@SuppressWarnings("serial")
public class DtoInformacionFormaPago implements Serializable{
	private String numeroCheque;
	
	private int codigoBanco;
	
	private String numeroCuenta;
	
	private int ciudadCheque;
	
	private String nombre;
	
	private String direccion;
	
	private String telefono;
	
	private double valor;
	
	private int ciudadGirador;
	
	private String numeroAutorizacion;

	private String observaciones;
	
	private String fecha;
	
	private DtoFormaPago formaPago;

	private int entidadFinanciera;

	private int codigoTarjetaFinanciera;
	
	private String numeroTarjeta;

	private String numeroComprobante;
	
	private String fechaVencimiento;

	private String codigoSeguridad;
	
	/**
	 * Detalle de los bonos ingresados para la forma de pago Bonos
	 */
	private DtoDetallePagosBonos detalleBonos;

	/**
	 * Ciudad cheque
	 * Almacena los 3 valores separados por -
	 */
	private String cuidadDepartamentoPaisCheque;

	/**
	 * Ciudad girador
	 * Almacena los 3 valores separados por -
	 */
	private String cuidadDepartamentoPaisGirador;
	
	/**
	 * Atributo que únicamente sirve para
	 * mostrar el libro de modificación en la sección formas de pago
	 */
	private boolean valido;
	
	/**
	 * Crear un nuevo detalle para información
	 */
	public DtoInformacionFormaPago() {
		formaPago=new DtoFormaPago();
		detalleBonos=new DtoDetallePagosBonos();
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public int getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(int codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public int getCiudadCheque() {
		return ciudadCheque;
	}

	public void setCiudadCheque(int ciudadCheque) {
		this.ciudadCheque = ciudadCheque;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getCiudadGirador() {
		return ciudadGirador;
	}

	public void setCiudadGirador(int ciudadGirador) {
		this.ciudadGirador = ciudadGirador;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCuidadDepartamentoPaisCheque() {
		return cuidadDepartamentoPaisCheque;
	}

	public void setCuidadDepartamentoPaisCheque(String cuidadDepartamentoPais) {
		this.cuidadDepartamentoPaisCheque = cuidadDepartamentoPais;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCuidadDepartamentoPaisGirador() {
		return cuidadDepartamentoPaisGirador;
	}

	public void setCuidadDepartamentoPaisGirador(
			String cuidadDepartamentoPaisGirador) {
		this.cuidadDepartamentoPaisGirador = cuidadDepartamentoPaisGirador;
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public DtoFormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(DtoFormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public int getEntidadFinanciera() {
		return entidadFinanciera;
	}

	public void setEntidadFinanciera(int entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public int getCodigoTarjetaFinanciera() {
		return codigoTarjetaFinanciera;
	}

	public void setCodigoTarjetaFinanciera(int codigoTarjetaFinanciera) {
		this.codigoTarjetaFinanciera = codigoTarjetaFinanciera;
	}

	public String getCodigoSeguridad() {
		return codigoSeguridad;
	}

	public void setCodigoSeguridad(String codigoSeguridad) {
		this.codigoSeguridad = codigoSeguridad;
	}

	public DtoDetallePagosBonos getDetalleBonos() {
		return detalleBonos;
	}

	public void setDetalleBonos(DtoDetallePagosBonos detalleBonos) {
		this.detalleBonos = detalleBonos;
	}
	
	
}
