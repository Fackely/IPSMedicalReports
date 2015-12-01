package com.servinte.axioma.dto.tesoreria;

import java.math.BigDecimal;
import java.sql.Date;

import util.UtilidadFecha;
import util.UtilidadTexto;



public class DtoResumenNotasPaciente {
	
	private long codigoEmpresaInstitucion;
	private String nombreEmpresaInstitucion;
	private long codigoPkNotaPaciente;
	private BigDecimal nroNotaPaciente;
	private String naturalezaNotaPaciente;
	private Date fechaNotaPaciente;
	private String fecha;
	private String horaNotaPaciente;
	private String observaciones;
	private int consecutivoCentroAtencionOrigen; 
	private String nombreCentroAtencionOrigen;
	private Integer consecutivoCentroAtencionByCentroAtencionDuenio;
	private String nombreCentroAtencionByCentroAtencionDuenio;
	private String descripcionConcepto;
	private String primerNombrePaciente;
	private String segundoNombrePaciente;
	private String primerApellidoPaciente;
	private String segundoApellidoPaciente;
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String login;
	private String ingresos;
	private BigDecimal valorNota;
	private String nombreInstitucionCentroAtencionDuenio;
	
	
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}
	public String getNombreEmpresaInstitucion() {
		return nombreEmpresaInstitucion;
	}
	public void setNombreEmpresaInstitucion(String nombreEmpresaInstitucion) {
		this.nombreEmpresaInstitucion = nombreEmpresaInstitucion;
	}
	/**
	 * @param codigoPkNotaPaciente the codigoPkNotaPaciente to set
	 */
	public void setCodigoPkNotaPaciente(long codigoPkNotaPaciente) {
		this.codigoPkNotaPaciente = codigoPkNotaPaciente;
	}
	/**
	 * @return the codigoPkNotaPaciente
	 */
	public long getCodigoPkNotaPaciente() {
		return codigoPkNotaPaciente;
	}
	public BigDecimal getNroNotaPaciente() {
		return nroNotaPaciente;
	}
	public void setNroNotaPaciente(BigDecimal nroNotaPaciente) {
		this.nroNotaPaciente = nroNotaPaciente;
	}
	public String getNaturalezaNotaPaciente() {
		return naturalezaNotaPaciente;
	}
	public void setNaturalezaNotaPaciente(String naturalezaNotaPaciente) {
		this.naturalezaNotaPaciente = naturalezaNotaPaciente;
	}
	public Date getFechaNotaPaciente() {
		return fechaNotaPaciente;
	}
	public void setFechaNotaPaciente(Date fechaNotaPaciente) {
		this.fechaNotaPaciente = fechaNotaPaciente;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return UtilidadFecha.conversionFormatoFechaAAp(fechaNotaPaciente.toString());
	}
	public String getHoraNotaPaciente() {
		return horaNotaPaciente;
	}
	public void setHoraNotaPaciente(String horaNotaPaciente) {
		this.horaNotaPaciente = horaNotaPaciente;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public int getConsecutivoCentroAtencionOrigen() {
		return consecutivoCentroAtencionOrigen;
	}
	public void setConsecutivoCentroAtencionOrigen(
			int consecutivoCentroAtencionOrigen) {
		this.consecutivoCentroAtencionOrigen = consecutivoCentroAtencionOrigen;
	}
	public String getNombreCentroAtencionOrigen() {
		return nombreCentroAtencionOrigen;
	}
	public void setNombreCentroAtencionOrigen(String nombreCentroAtencionOrigen) {
		this.nombreCentroAtencionOrigen = nombreCentroAtencionOrigen;
	}
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getIngresos() {
		return ingresos;
	}
	public void setIngresos(String ingresos) {
		this.ingresos = ingresos;
	}
	public BigDecimal getValorNota() {
		return valorNota;
	}
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	} 
	
	public String getValorNotaFormateado() {
		if (valorNota != null) {
			return UtilidadTexto.formatearValores(valorNota.doubleValue()); 
		} 
		return "";
	}
	
	public String getNombreCompletoPaciente() {
		String nombreCompleto = "";
		nombreCompleto = (!UtilidadTexto.isEmpty(primerNombrePaciente)) ? UtilidadTexto.convertirPrimeraLetraCapital(primerNombrePaciente) + " " : "";
		nombreCompleto += (!UtilidadTexto.isEmpty(segundoNombrePaciente)) ? UtilidadTexto.convertirPrimeraLetraCapital(segundoNombrePaciente) + " " : "";
		nombreCompleto += (!UtilidadTexto.isEmpty(primerApellidoPaciente)) ? UtilidadTexto.convertirPrimeraLetraCapital(primerApellidoPaciente) + " " : "";
		nombreCompleto += (!UtilidadTexto.isEmpty(segundoApellidoPaciente)) ? UtilidadTexto.convertirPrimeraLetraCapital(segundoApellidoPaciente) : "";
		return nombreCompleto;
	}
	
	public int getNumeroNotaOrdenar() {
		if (nroNotaPaciente != null) {
			return nroNotaPaciente.intValue(); 
		} 
		return 0;
	}
	
	public String getFechaHora() {
		if (fechaNotaPaciente != null) {
			return UtilidadFecha.conversionFormatoFechaAAp(fechaNotaPaciente.toString()) + " - " + horaNotaPaciente; 
		} 
		return "";
	}
	/**
	 * @param consecutivoCentroAtencionByCentroAtencionDuenio the consecutivoCentroAtencionByCentroAtencionDuenio to set
	 */
	public void setConsecutivoCentroAtencionByCentroAtencionDuenio(
			Integer consecutivoCentroAtencionByCentroAtencionDuenio) {
		this.consecutivoCentroAtencionByCentroAtencionDuenio = consecutivoCentroAtencionByCentroAtencionDuenio;
	}
	/**
	 * @return the consecutivoCentroAtencionByCentroAtencionDuenio
	 */
	public Integer getConsecutivoCentroAtencionByCentroAtencionDuenio() {
		return consecutivoCentroAtencionByCentroAtencionDuenio;
	}
	/**
	 * @param nombreCentroAtencionByCentroAtencionDuenio the nombreCentroAtencionByCentroAtencionDuenio to set
	 */
	public void setNombreCentroAtencionByCentroAtencionDuenio(
			String nombreCentroAtencionByCentroAtencionDuenio) {
		this.nombreCentroAtencionByCentroAtencionDuenio = nombreCentroAtencionByCentroAtencionDuenio;
	}
	/**
	 * @return the nombreCentroAtencionByCentroAtencionDuenio
	 */
	public String getNombreCentroAtencionByCentroAtencionDuenio() {
		return nombreCentroAtencionByCentroAtencionDuenio;
	}
	/**
	 * @param nombreInstitucionCentroAtencionDuenio the nombreInstitucionCentroAtencionDuenio to set
	 */
	public void setNombreInstitucionCentroAtencionDuenio(
			String nombreInstitucionCentroAtencionDuenio) {
		this.nombreInstitucionCentroAtencionDuenio = nombreInstitucionCentroAtencionDuenio;
	}
	/**
	 * @return the nombreInstitucionCentroAtencionDuenio
	 */
	public String getNombreInstitucionCentroAtencionDuenio() {
		return nombreInstitucionCentroAtencionDuenio;
	}	
}
