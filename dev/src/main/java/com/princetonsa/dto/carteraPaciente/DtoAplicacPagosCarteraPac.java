package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

import util.ConstantesBD;

public class DtoAplicacPagosCarteraPac implements Serializable
{
	private double codigoPk;
	private String  numeroDocumento;
	private String  tipoDocumento;
	private String 	anioDocumento;
	private String  ingresoDocumento;
	
	private int institucion;
	private double datosFinanciacion;
	private double valor;
	private String fechaAplicacion;
	private String fecha;
	private String hora;
	private String usuario;
	private double consecutivo;
	private BigDecimal valorAplicacion;

	//Para el listado de la aplicación de pagos
	private int cuotas;
	private double totalAplicaciones;
	private double saldoActual;
	private double valorAplicar;
	private double codigoDatosFinanciacion;
	
	//Para la detalle de aplicación
	private String codigoGarantia;
	private String tipoDocumentoGarantia;
	
	ArrayList<DtoRecibosCaja> listaRecibosCaja;
	
	public void reset()
	{
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.numeroDocumento="";
		this.tipoDocumento="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.datosFinanciacion=ConstantesBD.codigoNuncaValido;
		this.valor=ConstantesBD.codigoNuncaValido;;
		this.fechaAplicacion="";
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.consecutivo=ConstantesBD.codigoNuncaValido;
		this.cuotas=ConstantesBD.codigoNuncaValido;
		this.saldoActual=ConstantesBD.codigoNuncaValido;
		this.valorAplicar=ConstantesBD.codigoNuncaValido;
		this.totalAplicaciones=ConstantesBD.codigoNuncaValido;
		this.datosFinanciacion=ConstantesBD.codigoNuncaValidoDouble;
		this.listaRecibosCaja= new ArrayList<DtoRecibosCaja>();
		this.codigoGarantia="";
		this.tipoDocumentoGarantia="";
		this.anioDocumento="";
		this.ingresoDocumento="";
		this.valorAplicacion=new BigDecimal(0);
	}
	
	public DtoAplicacPagosCarteraPac()
	{
		this.reset();
	}

	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public double getDatosFinanciacion() {
		return datosFinanciacion;
	}

	public void setDatosFinanciacion(double datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(String fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public double getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(double consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public int getCuotas() {
		return cuotas;
	}

	public void setCuotas(int cuotas) {
		this.cuotas = cuotas;
	}

	public double getSaldoActual() {
		return saldoActual;
	}

	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}

	public double getValorAplicar() {
		return valorAplicar;
	}

	public void setValorAplicar(double valorAplicar) {
		this.valorAplicar = valorAplicar;
	}

	public double getTotalAplicaciones() {
		return totalAplicaciones;
	}

	public void setTotalAplicaciones(double totalAplicaciones) {
		this.totalAplicaciones = totalAplicaciones;
	}

	public double getCodigoDatosFinanciacion() {
		return codigoDatosFinanciacion;
	}

	public void setCodigoDatosFinanciacion(double codigoDatosFinanciacion) {
		this.codigoDatosFinanciacion = codigoDatosFinanciacion;
	}

	public ArrayList<DtoRecibosCaja> getListaRecibosCaja() {
		return listaRecibosCaja;
	}

	public void setListaRecibosCaja(ArrayList<DtoRecibosCaja> listaRecibosCaja) {
		this.listaRecibosCaja = listaRecibosCaja;
	}

	public String getCodigoGarantia() {
		return codigoGarantia;
	}

	public void setCodigoGarantia(String codigoGarantia) {
		this.codigoGarantia = codigoGarantia;
	}

	public String getTipoDocumentoGarantia() {
		return tipoDocumentoGarantia;
	}

	public void setTipoDocumentoGarantia(String tipoDocumentoGarantia) {
		this.tipoDocumentoGarantia = tipoDocumentoGarantia;
	}

	public String getAnioDocumento() {
		return anioDocumento;
	}

	public void setAnioDocumento(String anioDocumento) {
		this.anioDocumento = anioDocumento;
	}

	public String getIngresoDocumento() {
		return ingresoDocumento;
	}

	public void setIngresoDocumento(String ingresoDocumento) {
		this.ingresoDocumento = ingresoDocumento;
	}

	public BigDecimal getValorAplicacion() {
		return valorAplicacion;
	}

	public void setValorAplicacion(BigDecimal valorAplicacion) {
		this.valorAplicacion = valorAplicacion;
	}
	
	
	
}