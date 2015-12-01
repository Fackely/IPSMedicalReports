package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * @author Jose Eduardo Arias Doncel
 * */
public class DtoArchivoPlanoColsa implements Serializable
{
	
	/**
	 * Indica el tipo de Resgistro Tipo R (Recepcion). Tipo S (Sociedades)
	 * */
	private String tipoRegistro;
	
	
	//---------Atributos Registro Tipo R
	
	/**
	 * Codigo del Paciente
	 * */
	private String codigoPaciente;
	
	/**
	 * Identificacion del paciente
	 * */
	private String identificacionPaciente;
	
	/**
	 * Nombre del paciente
	 * */
	private String nombrePaciente;
	
	/**
	 * Codigo de la Cuenta
	 * */
	private String cuenta;
	
	/**
	 * Campo Comun para tipo de Registro Tipo R - S
	 * */
	private String identificador;
	
	/**
	 * 
	 * */
	private String unidadEconomica;
	
	/**
	 * Campo Comun para tipo de Registro Tipo R - S
	 * */
	private String codigoPrestador;
	
	
	/**
	 * */
	private String nombreRazonSocial;
	
	
	/**
	 * */
	private String codigoFactura;
	
	/**
	 * */
	private String numeroFactura;
	
	
	/**
	 * 
	 * */
	private String prefijoFactura;
	
	/**
	 * */
	private double valorTotalFactura;
	
	
	/**
	 * */
	private String tipoMonto;
	
	/**
	 * */
	private double valorRecaudoCuotasModeradoras;
	
	/**
	 * */
	private double valorRecaudoCopagos;
	
	
	/**
	 * */
	private String valorRecaudoPeriodosMinimosCotizacion;
	
	/**
	 * */
	private String fechaIngreso;
	
	/**
	 * */
	private String fechaSalida;
	
	/**
	 * */
	private String numeroSolicitudVolante;
	
	/**
	 * */
	private String fechaElaboracionFactura;
	
	/**	 
	 * */
	private String swControl;
	
	/**
	 * Via de Ingreso de la Cuenta del Paciente
	 * */
	private String descripcionViaIngreso;
	
	
	//----------------Atributos Regsitro Tipo S
	
	
	/** 
	 * */
	private String tipoHonorario;
	
	/**
	 * */
	private double valorFacturadoSocio;

	
	//-------------------Metodos
	
	
	/**
	 * @return the codigoPrestador
	 */
	public String getCodigoPrestador() {
		return codigoPrestador;
	}

	/**
	 * @param codigoPrestador the codigoPrestador to set
	 */
	public void setCodigoPrestador(String codigoPrestador) {
		this.codigoPrestador = codigoPrestador;
	}

	/**
	 * @return the fechaElaboracionFactura
	 */
	public String getFechaElaboracionFactura() {
		return fechaElaboracionFactura;
	}

	/**
	 * @param fechaElaboracionFactura the fechaElaboracionFactura to set
	 */
	public void setFechaElaboracionFactura(String fechaElaboracionFactura) {
		this.fechaElaboracionFactura = fechaElaboracionFactura;
	}

	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * @return the fechaSalida
	 */
	public String getFechaSalida() {
		return fechaSalida;
	}

	/**
	 * @param fechaSalida the fechaSalida to set
	 */
	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the nombreRazonSocial
	 */
	public String getNombreRazonSocial() {
		return nombreRazonSocial;
	}

	/**
	 * @param nombreRazonSocial the nombreRazonSocial to set
	 */
	public void setNombreRazonSocial(String nombreRazonSocial) {
		this.nombreRazonSocial = nombreRazonSocial;
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the numeroSolicitudVolante
	 */
	public String getNumeroSolicitudVolante() {
		return numeroSolicitudVolante;
	}

	/**
	 * @param numeroSolicitudVolante the numeroSolicitudVolante to set
	 */
	public void setNumeroSolicitudVolante(String numeroSolicitudVolante) {
		this.numeroSolicitudVolante = numeroSolicitudVolante;
	}

	/**
	 * @return the swControl
	 */
	public String getSwControl() {
		return swControl;
	}

	/**
	 * @param swControl the swControl to set
	 */
	public void setSwControl(String swControl) {
		this.swControl = swControl;
	}

	/**
	 * @return the tipoHonorario
	 */
	public String getTipoHonorario() {
		return tipoHonorario;
	}

	/**
	 * @param tipoHonorario the tipoHonorario to set
	 */
	public void setTipoHonorario(String tipoHonorario) {
		this.tipoHonorario = tipoHonorario;
	}

	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	/**
	 * @return the unidadEconomica
	 */
	public String getUnidadEconomica() {
		return unidadEconomica;
	}

	/**
	 * @param unidadEconomica the unidadEconomica to set
	 */
	public void setUnidadEconomica(String unidadEconomica) {
		this.unidadEconomica = unidadEconomica;
	}

	/**
	 * @return the valorFacturadoSocio
	 */
	public double getValorFacturadoSocio() {
		return valorFacturadoSocio;
	}

	/**
	 * @param valorFacturadoSocio the valorFacturadoSocio to set
	 */
	public void setValorFacturadoSocio(double valorFacturadoSocio) {
		this.valorFacturadoSocio = valorFacturadoSocio;
	}

	/**
	 * @return the valorRecaudoCopagos
	 */
	public double getValorRecaudoCopagos() {
		return valorRecaudoCopagos;
	}

	/**
	 * @param valorRecaudoCopagos the valorRecaudoCopagos to set
	 */
	public void setValorRecaudoCopagos(double valorRecaudoCopagos) {
		this.valorRecaudoCopagos = valorRecaudoCopagos;
	}

	/**
	 * @return the valorRecaudoCuotasModeradoras
	 */
	public double getValorRecaudoCuotasModeradoras() {
		return valorRecaudoCuotasModeradoras;
	}

	/**
	 * @param valorRecaudoCuotasModeradoras the valorRecaudoCuotasModeradoras to set
	 */
	public void setValorRecaudoCuotasModeradoras(
			double valorRecaudoCuotasModeradoras) {
		this.valorRecaudoCuotasModeradoras = valorRecaudoCuotasModeradoras;
	}

	/**
	 * @return the valorRecaudoPeriodosMinimosCotizacion
	 */
	public String getValorRecaudoPeriodosMinimosCotizacion() {
		return valorRecaudoPeriodosMinimosCotizacion;
	}

	/**
	 * @param valorRecaudoPeriodosMinimosCotizacion the valorRecaudoPeriodosMinimosCotizacion to set
	 */
	public void setValorRecaudoPeriodosMinimosCotizacion(
			String valorRecaudoPeriodosMinimosCotizacion) {
		this.valorRecaudoPeriodosMinimosCotizacion = valorRecaudoPeriodosMinimosCotizacion;
	}

	/**
	 * @return the valorTotalFactura
	 */
	public double getValorTotalFactura() {
		return valorTotalFactura;
	}

	/**
	 * @param valorTotalFactura the valorTotalFactura to set
	 */
	public void setValorTotalFactura(double valorTotalFactura) {
		this.valorTotalFactura = valorTotalFactura;
	}

	/**
	 * @return the prefijoFactura
	 */
	public String getPrefijoFactura() {
		return prefijoFactura;
	}

	/**
	 * @param prefijoFactura the prefijoFactura to set
	 */
	public void setPrefijoFactura(String prefijoFactura) {
		this.prefijoFactura = prefijoFactura;
	}

	/**
	 * @return the codigoFactura
	 */
	public String getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(String codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the tipoMonto
	 */
	public String getTipoMonto() {
		return tipoMonto;
	}

	/**
	 * @param tipoMonto the tipoMonto to set
	 */
	public void setTipoMonto(String tipoMonto) {
		this.tipoMonto = tipoMonto;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}

	/**
	 * @param identificacionPaciente the identificacionPaciente to set
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the descripcionViaIngreso
	 */
	public String getDescripcionViaIngreso() {
		return descripcionViaIngreso;
	}

	/**
	 * @param descripcionViaIngreso the descripcionViaIngreso to set
	 */
	public void setDescripcionViaIngreso(String descripcionViaIngreso) {
		this.descripcionViaIngreso = descripcionViaIngreso;
	}

	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}	
}