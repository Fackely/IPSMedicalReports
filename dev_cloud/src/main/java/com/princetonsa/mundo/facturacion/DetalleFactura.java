/*
 * @(#)DetalleFactura.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.mundo.facturacion;

import util.InfoDatosInt;

import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * Clase que maneja cada uno de los elementos
 * que componen una factura.
 * 
 * @version 1.0 Dec 17, 2004
 */
public class DetalleFactura 
{
    /**
     * Código de este detalle de factura
     */
    private int codigoFactura;
    
    /**
     * Número de la solicitud
     */
    private int numeroSolicitud;
    
    /**
     * Double con el valor del ajuste credito
     * médico
     */
    private double ajusteMedicoCredito;
    
    /**
     * Double con el valor del ajuste debito medico
     */
    private double ajusteMedicoDebito;
	
    /**
     * porcentaje del pool
     */
    private int porcentajePool;
	
    private double porcentajeMedico;
    private double valorMedico;
	
	/**
	 * Fecha en la que se realizó
	 * el cargo
	 */
    private String fechaCargo;
	
	/**
	 * Cantidad de elementos que
	 * incluye el cargo
	 */
    private int cantidadCargo;
	
	/**
	 * Valor del Cargo 
	 */
    private double valorCargo;
    
    /**
	 * Cantidad de elementos que
	 * incluye el cargo
	 * Utilizado en la prefactura.
	 * En todo momento debe estar inicializado en 0 para que no acumule los datos.
	 */
    private int cantidadCargoTemp=0;
	
	/**
	 * Valor del Cargo
	 * Utilizado en la prefactura.
	 * En todo momento debe estar inicializado en 0 para que no acumule los datos. 
	 */
    private double valorTemp=0;
	
	/**
	 * Valor del Iva para el cargo
	 */
    private double valorIva;
	
	/**
	 * Valor del Recargo para este
	 * cargo
	 */
    private double valorRecargo;
    private int soliPago;
    private InfoDatosInt tipoCargo;
    
    private double valorTotal;
    
    /**
     * Centro de costo que respondió / atendió 
     * esta solicitud
     */
    private InfoDatosInt centroCosto;
    
    /**
     * Información básica del servicio
     * (código y nombre)
     */
    private InfoDatosInt basicoServicio;
    
    /**
     * Información básica del artículo
     * (código y nombre)
     */
    private InfoDatosInt basicoArticulo;
    
    /**
     * Diagnostico para el servicio, cargado
     * únicamente cuando sea necesario
     */
    private Diagnostico diagnosticoServicio=new Diagnostico();
    
    /**
     * Porcentaje no cubierto
     * (Aplica para solicitudes de 
     * artículos, para el resto es 0)
     */
    private double porcentajeNoCubierto=0.0;

    /**
     * @return Retorna el/la ajusteMedicoCredito.
     */
    public double getAjusteMedicoCredito() {
        return ajusteMedicoCredito;
    }
    /**
     * El/La ajusteMedicoCredito a establecer.
     * @param ajusteMedicoCredito 
     */
    public void setAjusteMedicoCredito(double ajusteMedicoCredito) {
        this.ajusteMedicoCredito = ajusteMedicoCredito;
    }
    /**
     * @return Retorna el/la cantidadCargo.
     */
    public int getCantidadCargo() {
        return cantidadCargo;
    }
    /**
     * El/La cantidadCargo a establecer.
     * @param cantidadCargo 
     */
    public void setCantidadCargo(int cantidadCargo) {
        this.cantidadCargo = cantidadCargo;
    }
    /**
     * @return Retorna el/la codigoFactura.
     */
    public int getCodigoFactura() {
        return codigoFactura;
    }
    /**
     * El/La codigoFactura a establecer.
     * @param codigoFactura 
     */
    public void setCodigoFactura(int codigo) {
        this.codigoFactura = codigo;
    }
    /**
     * @return Retorna el/la fechaCargo.
     */
    public String getFechaCargo() {
        return fechaCargo;
    }
    /**
     * El/La fechaCargo a establecer.
     * @param fechaCargo 
     */
    public void setFechaCargo(String fechaCargo) {
        this.fechaCargo = fechaCargo;
    }
    
    /**
     * @return Retorna el/la porcentajeMedico.
     */
    public double getPorcentajeMedico() {
        return porcentajeMedico;
    }
    /**
     * El/La porcentajeMedico a establecer.
     * @param porcentajeMedico 
     */
    public void setPorcentajeMedico(double porcentajeMedico) {
        this.porcentajeMedico = porcentajeMedico;
    }
    /**
     * @return Retorna el/la soliPago.
     */
    public int getSoliPago() {
        return soliPago;
    }
    /**
     * El/La soliPago a establecer.
     * @param soliPago 
     */
    public void setSoliPago(int soliPago) {
        this.soliPago = soliPago;
    }
    /**
     * @return Retorna el/la tipoCargo.
     */
    public InfoDatosInt getTipoCargo() {
        return tipoCargo;
    }
    /**
     * El/La tipoCargo a establecer.
     * @param tipoCargo 
     */
    public void setTipoCargo(InfoDatosInt tipoCargo) {
        this.tipoCargo = tipoCargo;
    }
    /**
     * @return Retorna el/la valorCargo.
     */
    public double getValorCargo() {
        return valorCargo;
    }
    /**
     * El/La valorCargo a establecer.
     * @param valorCargo 
     */
    public void setValorCargo(double valorCargo) {
        this.valorCargo = valorCargo;
    }
    /**
     * @return Retorna el/la valorIva.
     */
    public double getValorIva() {
        return valorIva;
    }
    /**
     * El/La valorIva a establecer.
     * @param valorIva 
     */
    public void setValorIva(double valorIva) {
        this.valorIva = valorIva;
    }
    /**
     * @return Retorna el/la valorMedico.
     */
    public double getValorMedico() {
        return valorMedico;
    }
    /**
     * El/La valorMedico a establecer.
     * @param valorMedico 
     */
    public void setValorMedico(double valorMedico) {
        this.valorMedico = valorMedico;
    }
    /**
     * @return Retorna el/la valorRecargo.
     */
    public double getValorRecargo() {
        return valorRecargo;
    }
    /**
     * El/La valorRecargo a establecer.
     * @param valorRecargo 
     */
    public void setValorRecargo(double valorRecargo) {
        this.valorRecargo = valorRecargo;
    }
    /**
     * @return Retorna el/la numeroSolicitud.
     */
    public int getNumeroSolicitud() {
        return numeroSolicitud;
    }
    /**
     * El/La numeroSolicitud a establecer.
     * @param numeroSolicitud 
     */
    public void setNumeroSolicitud(int numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
    /**
     * @return Retorna el/la porcentajeNoCubierto.
     */
    public double getPorcentajeNoCubierto() {
        return porcentajeNoCubierto;
    }
    /**
     * El/La porcentajeNoCubierto a establecer.
     * @param porcentajeNoCubierto 
     */
    public void setPorcentajeNoCubierto(double porcentajeNoCubierto) {
        this.porcentajeNoCubierto = porcentajeNoCubierto;
    }
    /**
     * @return Retorna el/la centroCosto.
     */
    public InfoDatosInt getCentroCosto() {
        return centroCosto;
    }
    /**
     * El/La centroCosto a establecer.
     * @param centroCosto 
     */
    public void setCentroCosto(InfoDatosInt centroCosto) {
        this.centroCosto = centroCosto;
    }
    /**
     * @return Retorna el/la basicoArticulo.
     */
    public InfoDatosInt getBasicoArticulo() {
        return basicoArticulo;
    }
    /**
     * El/La basicoArticulo a establecer.
     * @param basicoArticulo 
     */
    public void setBasicoArticulo(InfoDatosInt basicoArticulo) {
        this.basicoArticulo = basicoArticulo;
    }
    /**
     * @return Retorna el/la basicoServicio.
     */
    public InfoDatosInt getBasicoServicio() {
        return basicoServicio;
    }
    /**
     * El/La basicoServicio a establecer.
     * @param basicoServicio 
     */
    public void setBasicoServicio(InfoDatosInt basicoServicio) {
        this.basicoServicio = basicoServicio;
    }
   
    /**
     * @return Retorna el/la diagnosticoServicio.
     */
    public Diagnostico getDiagnosticoServicio() {
        return diagnosticoServicio;
    }
    /**
     * El/La diagnosticoServicio a establecer.
     * @param diagnosticoServicio 
     */
    public void setDiagnosticoServicio(Diagnostico diagnosticoServicio) {
        this.diagnosticoServicio = diagnosticoServicio;
    }
	/**
	 * @return Retorna valorTotal.
	 */
	public double getValorTotal()
	{
		return valorTotal;
	}
	/**
	 * @param valorTotal Asigna valorTotal.
	 */
	public void setValorTotal(double valorTotal)
	{
		this.valorTotal = valorTotal;
	}
    /**
     * @return Returns the porcentajePool.
     */
    public int getPorcentajePool() {
        return porcentajePool;
    }
    /**
     * @param porcentajePool The porcentajePool to set.
     */
    public void setPorcentajePool(int porcentajePool) {
        this.porcentajePool = porcentajePool;
    }
    /**
     * @return Returns the ajusteMedicoDebito.
     */
    public double getAjusteMedicoDebito() {
        return ajusteMedicoDebito;
    }
    /**
     * @param ajusteMedicoDebito The ajusteMedicoDebito to set.
     */
    public void setAjusteMedicoDebito(double ajusteMedicoDebito) {
        this.ajusteMedicoDebito = ajusteMedicoDebito;
    }
	public int getCantidadCargoTemp() {
		return cantidadCargoTemp;
	}
	public void setCantidadCargoTemp(int cantidadCargoTemp) {
		this.cantidadCargoTemp = cantidadCargoTemp;
	}
	public double getValorTemp() {
		return valorTemp;
	}
	public void setValorTemp(double valorCargoTemp) {
		this.valorTemp = valorCargoTemp;
	}
}
