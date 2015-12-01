package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.UtilidadTexto;

import com.servinte.axioma.orm.FormasPago;

public class DtoConsolidadoCierreReporte  implements Serializable, Comparable<DtoConsolidadoCierreReporte>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Intitucion;
	private String centroAtencion;
	private BigDecimal efectivo;
	private BigDecimal cheques;
	private BigDecimal tarjetaCredito;
	private BigDecimal tarjetaDebito;
	private BigDecimal letra;
	private BigDecimal pagare;
	private BigDecimal saldoTotalCentroAtencion;
	private String tipoDeMovimiento;
	private BigDecimal totalCentroAtencion;
	private String Cajero;
	private String login;
	private String horaTurnoCajero;
	private String caja;
	private String caja2;
	private String horaApertura;
	private ArrayList<BigDecimal> totalesPorFormaPago;
	private boolean esTotalJsp=false;
	private Long consecutivo;
	private Long movimientoCaja;
	private Long trasladoCajaPrincipalMayor;
	private String cajaInicialDescripcion;
	private Integer cajaInicialCodigo;
	private String cajaFinalDescripcion;
	private Integer cajaFinalCodigo;
	private Date fechaTraslado;
	private String horaTraslado;
	private String primerNombreTraslado;
	private String segundonombreTraslado;
	private String primerApellidoTraslado;
	private String segundoApellidoTraslado;
	private String nombreTrasladoCompleto;
	private String loginTraslado;
	private Date fecha;
	
	/* Parametros Búsqueda */
	private Date fechaSeleccionada;
	private String empresaInstitucion;
	private List<FormasPago> formasPago;
	private Date fechaInicio;
	private Date fechaFin;
	private String numeroInicioTraslado;
	private String numeroFinTraslado;
	private Long consecutivoTraslado;
	private boolean agruparSumadoPorConsecutivoTraslado;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoConsolidadoCierreReporte() 
	{
		this.totalesPorFormaPago				= new ArrayList<BigDecimal>();
		this.fechaSeleccionada					= null;
		this.empresaInstitucion					= null;
		this.formasPago							= null;
		this.consecutivo						= null;
		this.movimientoCaja						= null;
		this.trasladoCajaPrincipalMayor 		= null;
		this.fechaInicio						= null;
		this.fechaFin							= null;
		this.numeroInicioTraslado 				= null;
		this.numeroFinTraslado					= null;
		this.caja								= null;
		this.caja2								= null;
		this.consecutivoTraslado				= null;
		this.agruparSumadoPorConsecutivoTraslado=false;
		this.cajaInicialDescripcion				= null;
		this.cajaInicialCodigo					= null;
		this.cajaFinalDescripcion				= null;
		this.cajaFinalCodigo					= null;
		this.fechaTraslado						= null;
		this.horaTraslado						= null;
		this.primerNombreTraslado				= null;
		this.segundonombreTraslado				= null;
		this.primerApellidoTraslado				= null;
		this.segundoApellidoTraslado			= null;
		this.loginTraslado						= null;
		this.nombreTrasladoCompleto				= null;
		this.fecha								= null;
	}

	/**
	 * @return the intitucion
	 */
	public String getIntitucion() {
		return Intitucion;
	}

	/**
	 * @param intitucion the intitucion to set
	 */
	public void setIntitucion(String intitucion) {
		Intitucion = intitucion;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the efectivo
	 */
	public BigDecimal getEfectivo() {
		return efectivo;
	}

	/**
	 * @param efectivo the efectivo to set
	 */
	public void setEfectivo(BigDecimal efectivo) {
		this.efectivo = efectivo;
	}

	/**
	 * @return the cheques
	 */
	public BigDecimal getCheques() {
		return cheques;
	}

	/**
	 * @param cheques the cheques to set
	 */
	public void setCheques(BigDecimal cheques) {
		this.cheques = cheques;
	}

	/**
	 * @return the tarjetaCredito
	 */
	public BigDecimal getTarjetaCredito() {
		return tarjetaCredito;
	}

	/**
	 * @param tarjetaCredito the tarjetaCredito to set
	 */
	public void setTarjetaCredito(BigDecimal tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}

	/**
	 * @return the tarjetaDebito
	 */
	public BigDecimal getTarjetaDebito() {
		return tarjetaDebito;
	}

	/**
	 * @param tarjetaDebito the tarjetaDebito to set
	 */
	public void setTarjetaDebito(BigDecimal tarjetaDebito) {
		this.tarjetaDebito = tarjetaDebito;
	}

	/**
	 * @return the saldoTotalCentroAtencion
	 */
	public BigDecimal getSaldoTotalCentroAtencion() {
		return saldoTotalCentroAtencion;
	}

	/**
	 * @param saldoTotalCentroAtencion the saldoTotalCentroAtencion to set
	 */
	public void setSaldoTotalCentroAtencion(BigDecimal saldoTotalCentroAtencion) {
		this.saldoTotalCentroAtencion = saldoTotalCentroAtencion;
	}

	/**
	 * @return the tipoDeMovimiento
	 */
	public String getTipoDeMovimiento() {
		return tipoDeMovimiento;
	}

	/**
	 * @param tipoDeMovimiento the tipoDeMovimiento to set
	 */
	public void setTipoDeMovimiento(String tipoDeMovimiento) {
		this.tipoDeMovimiento = tipoDeMovimiento;
	}

	/**
	 * @return the totalCentroAtencion
	 */
	public BigDecimal getTotalCentroAtencion() {
		return totalCentroAtencion;
	}

	/**
	 * @param totalCentroAtencion the totalCentroAtencion to set
	 */
	public void setTotalCentroAtencion(BigDecimal totalCentroAtencion) {
		this.totalCentroAtencion = totalCentroAtencion;
	}

	/**
	 * @return the cajero
	 */
	public String getCajero() {
		return Cajero;
	}

	/**
	 * @param cajero the cajero to set
	 */
	public void setCajero(String cajero) {
		Cajero = cajero;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the horaTurnoCajero
	 */
	public String getHoraTurnoCajero() {
		return horaTurnoCajero;
	}

	/**
	 * @param horaTurnoCajero the horaTurnoCajero to set
	 */
	public void setHoraTurnoCajero(String horaTurnoCajero) {
		this.horaTurnoCajero = horaTurnoCajero;
	}

	/**
	 * @return the caja
	 */
	public String getCaja() {
		return caja;
	}

	/**
	 * @param caja the caja to set
	 */
	public void setCaja(String caja) {
		this.caja = caja;
	}

	/**
	 * @return the letra
	 */
	public BigDecimal getLetra() {
		return letra;
	}

	/**
	 * @param letra the letra to set
	 */
	public void setLetra(BigDecimal letra) {
		this.letra = letra;
	}

	/**
	 * @return the pagare
	 */
	public BigDecimal getPagare() {
		return pagare;
	}

	/**
	 * @param pagare the pagare to set
	 */
	public void setPagare(BigDecimal pagare) {
		this.pagare = pagare;
	}

	/**
	 * @return the totalesPorFormaPago
	 */
	public ArrayList<BigDecimal> getTotalesPorFormaPago() {
		return totalesPorFormaPago;
	}

	/**
	 * @param totalesPorFormaPago the totalesPorFormaPago to set
	 */
	public void setTotalesPorFormaPago(ArrayList<BigDecimal> totalesPorFormaPago) {
		this.totalesPorFormaPago = totalesPorFormaPago;
	}

	/**
	 * @return the esTotalJsp
	 */
	public boolean isEsTotalJsp() {
		return esTotalJsp;
	}

	/**
	 * @param esTotalJsp the esTotalJsp to set
	 */
	public void setEsTotalJsp(boolean esTotalJsp) {
		this.esTotalJsp = esTotalJsp;
	}

	@Override
	public int compareTo(DtoConsolidadoCierreReporte arg0) {
		return this.horaTurnoCajero.compareToIgnoreCase(arg0.horaTurnoCajero); 
	}

	/**
	 * @return the horaApertura
	 */
	public String getHoraApertura() {
		return horaApertura;
	}

	/**
	 * @param horaApertura the horaApertura to set
	 */
	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}


	/**
	 * @return the empresaInstitucion
	 */
	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}


	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}


	/**
	 * @return the formasPago
	 */
	public List<FormasPago> getFormasPago() {
		return formasPago;
	}


	/**
	 * @param formasPago the formasPago to set
	 */
	public void setFormasPago(List<FormasPago> formasPago) {
		this.formasPago = formasPago;
	}

	/**
	 * @return the fechaSeleccionada
	 */
	public Date getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	/**
	 * @param fechaSeleccionada the fechaSeleccionada to set
	 */
	public void setFechaSeleccionada(Date fechaSeleccionada) {
		this.fechaSeleccionada = fechaSeleccionada;
	}

	/**
	 * @return the consecutivo
	 */
	public Long getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Long consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the movimientoCaja
	 */
	public Long getMovimientoCaja() {
		return movimientoCaja;
	}

	/**
	 * @param movimientoCaja the movimientoCaja to set
	 */
	public void setMovimientoCaja(Long movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}

	/**
	 * @return the trasladoCajaPrincipalMayor
	 */
	public Long getTrasladoCajaPrincipalMayor() {
		return trasladoCajaPrincipalMayor;
	}

	/**
	 * @param trasladoCajaPrincipalMayor the trasladoCajaPrincipalMayor to set
	 */
	public void setTrasladoCajaPrincipalMayor(Long trasladoCajaPrincipalMayor) {
		this.trasladoCajaPrincipalMayor = trasladoCajaPrincipalMayor;
	}

	/**
	 * @return valor de fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio el fechaInicio para asignar
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return valor de fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin el fechaFin para asignar
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return valor de numeroInicioTraslado
	 */
	public String getNumeroInicioTraslado() {
		return numeroInicioTraslado;
	}

	/**
	 * @param numeroInicioTraslado el numeroInicioTraslado para asignar
	 */
	public void setNumeroInicioTraslado(String numeroInicioTraslado) {
		this.numeroInicioTraslado = numeroInicioTraslado;
	}

	/**
	 * @return valor de numeroFinTraslado
	 */
	public String getNumeroFinTraslado() {
		return numeroFinTraslado;
	}

	/**
	 * @param numeroFinTraslado el numeroFinTraslado para asignar
	 */
	public void setNumeroFinTraslado(String numeroFinTraslado) {
		this.numeroFinTraslado = numeroFinTraslado;
	}

	/**
	 * @return valor de caja2
	 */
	public String getCaja2() {
		return caja2;
	}

	/**
	 * @param caja2 el caja2 para asignar
	 */
	public void setCaja2(String caja2) {
		this.caja2 = caja2;
	}

	/**
	 * @return valor de consecutivoTraslado
	 */
	public Long getConsecutivoTraslado() {
		return consecutivoTraslado;
	}

	/**
	 * @param consecutivoTraslado el consecutivoTraslado para asignar
	 */
	public void setConsecutivoTraslado(Long consecutivoTraslado) {
		this.consecutivoTraslado = consecutivoTraslado;
	}

	/**
	 * @return valor de agruparSumadoPorConsecutivoTraslado
	 */
	public boolean isAgruparSumadoPorConsecutivoTraslado() {
		return agruparSumadoPorConsecutivoTraslado;
	}

	/**
	 * @param agruparSumadoPorConsecutivoTraslado el agruparSumadoPorConsecutivoTraslado para asignar
	 */
	public void setAgruparSumadoPorConsecutivoTraslado(
			boolean agruparSumadoPorConsecutivoTraslado) {
		this.agruparSumadoPorConsecutivoTraslado = agruparSumadoPorConsecutivoTraslado;
	}

	/**
	 * @return valor de cajaInicialDescripcion
	 */
	public String getCajaInicialDescripcion() {
		return cajaInicialDescripcion;
	}

	/**
	 * @param cajaInicialDescripcion el cajaInicialDescripcion para asignar
	 */
	public void setCajaInicialDescripcion(String cajaInicialDescripcion) {
		this.cajaInicialDescripcion = cajaInicialDescripcion;
	}

	/**
	 * @return valor de cajaInicialCodigo
	 */
	public Integer getCajaInicialCodigo() {
		return cajaInicialCodigo;
	}

	/**
	 * @param cajaInicialCodigo el cajaInicialCodigo para asignar
	 */
	public void setCajaInicialCodigo(Integer cajaInicialCodigo) {
		this.cajaInicialCodigo = cajaInicialCodigo;
	}

	/**
	 * @return valor de cajaFinalDescripcion
	 */
	public String getCajaFinalDescripcion() {
		return cajaFinalDescripcion;
	}

	/**
	 * @param cajaFinalDescripcion el cajaFinalDescripcion para asignar
	 */
	public void setCajaFinalDescripcion(String cajaFinalDescripcion) {
		this.cajaFinalDescripcion = cajaFinalDescripcion;
	}

	/**
	 * @return valor de cajaFinalCodigo
	 */
	public Integer getCajaFinalCodigo() {
		return cajaFinalCodigo;
	}

	/**
	 * @param cajaFinalCodigo el cajaFinalCodigo para asignar
	 */
	public void setCajaFinalCodigo(Integer cajaFinalCodigo) {
		this.cajaFinalCodigo = cajaFinalCodigo;
	}

	/**
	 * @return valor de fechaTraslado
	 */
	public Date getFechaTraslado() {
		return fechaTraslado;
	}

	/**
	 * @param fechaTraslado el fechaTraslado para asignar
	 */
	public void setFechaTraslado(Date fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

	/**
	 * @return valor de horaTraslado
	 */
	public String getHoraTraslado() {
		return horaTraslado;
	}

	/**
	 * @param horaTraslado el horaTraslado para asignar
	 */
	public void setHoraTraslado(String horaTraslado) {
		this.horaTraslado = horaTraslado;
	}

	/**
	 * @return valor de primerNombreTraslado
	 */
	public String getPrimerNombreTraslado() {
		return primerNombreTraslado;
	}

	/**
	 * @param primerNombreTraslado el primerNombreTraslado para asignar
	 */
	public void setPrimerNombreTraslado(String primerNombreTraslado) {
		this.primerNombreTraslado = primerNombreTraslado;
	}

	/**
	 * @return valor de segundonombreTraslado
	 */
	public String getSegundonombreTraslado() {
		return segundonombreTraslado;
	}

	/**
	 * @param segundonombreTraslado el segundonombreTraslado para asignar
	 */
	public void setSegundonombreTraslado(String segundonombreTraslado) {
		this.segundonombreTraslado = segundonombreTraslado;
	}

	/**
	 * @return valor de primerApellidoTraslado
	 */
	public String getPrimerApellidoTraslado() {
		return primerApellidoTraslado;
	}

	/**
	 * @param primerApellidoTraslado el primerApellidoTraslado para asignar
	 */
	public void setPrimerApellidoTraslado(String primerApellidoTraslado) {
		this.primerApellidoTraslado = primerApellidoTraslado;
	}

	/**
	 * @return valor de segundoApellidoTraslado
	 */
	public String getSegundoApellidoTraslado() {
		return segundoApellidoTraslado;
	}

	/**
	 * @param segundoApellidoTraslado el segundoApellidoTraslado para asignar
	 */
	public void setSegundoApellidoTraslado(String segundoApellidoTraslado) {
		this.segundoApellidoTraslado = segundoApellidoTraslado;
	}

	/**
	 * @return valor de loginTraslado
	 */
	public String getLoginTraslado() {
		return loginTraslado;
	}

	/**
	 * @param loginTraslado el loginTraslado para asignar
	 */
	public void setLoginTraslado(String loginTraslado) {
		this.loginTraslado = loginTraslado;
	}

	/**
	 * @return valor de nombreTrasladoCompleto
	 */
	public String getNombreTrasladoCompleto() 
	{
		StringBuilder nombreCompleto = new StringBuilder();
		nombreCompleto.append(this.primerNombreTraslado).append(" ");
		
		if(!UtilidadTexto.isEmpty(this.segundonombreTraslado)){
			nombreCompleto.append(this.segundonombreTraslado).append(" ");
		}
		
		nombreCompleto.append(this.primerApellidoTraslado).append(" ");
		
		if(!UtilidadTexto.isEmpty(this.segundoApellidoTraslado)){
			nombreCompleto.append(this.segundoApellidoTraslado).append(" ");
		}
		
		return nombreCompleto.toString();
	}

	/**
	 * @param nombreTrasladoCompleto el nombreTrasladoCompleto para asignar
	 */
	public void setNombreTrasladoCompleto(String nombreTrasladoCompleto) {
		this.nombreTrasladoCompleto = nombreTrasladoCompleto;
	}

	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

}
