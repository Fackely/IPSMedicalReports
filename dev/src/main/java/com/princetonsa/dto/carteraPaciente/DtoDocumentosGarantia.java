package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;


public class DtoDocumentosGarantia implements Serializable
{
	private int ingreso;
	private int codigoPaciente;
	private int institucion;
	private String centroAtencion;
	private String consecutivo;
	private String anioConsecutivo;
	private String tipoDocumento;
	private String estado;
	private String numeroCuenta;
	private String numeroDocumento;
	private String valor;
	private String garantiaIngreso;
	private String cartera;
	private String nomPaciente;
	private String idPaciente;
	private String fechaGen;
	private String fechaDoc;
	private BigDecimal valorDoc;
	private int codigoCentroAtencion;
	
	//Elementos para el dato de financiacion asociado al documento de garantia
	private String fechaInicioDatoFin;
	private int nroCuotasDatoFin;
	private int diasCuota;
	
	//Atributos de Consulta para  datos de Deudor
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private int consecutivoFactura;
	private BigDecimal valorTotal;
	private String horaIngreso;
	private String fechaIngreso;
	private double datoFinanciacion;
	
	//Atributos para la búsqueda de Documetnos
	private String fechaGenFinal;
	
	
	
	/**
	 * Constructor
	 */
	public DtoDocumentosGarantia()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.consecutivo="";
		this.anioConsecutivo="";
		this.tipoDocumento="";
		this.estado="";
		this.numeroCuenta="";
		this.numeroDocumento="";
		this.valor="";
		this.garantiaIngreso="";
		this.cartera="";
		this.nomPaciente="";
		this.idPaciente="";
		this.fechaGen="";
		this.centroAtencion= "";
		this.fechaDoc="";
		this.valorDoc=new BigDecimal(0);
		this.fechaInicioDatoFin="";
		this.nroCuotasDatoFin=ConstantesBD.codigoNuncaValido;
		this.diasCuota=ConstantesBD.codigoNuncaValido;
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.consecutivoFactura=ConstantesBD.codigoNuncaValido;
		this.valorTotal=new BigDecimal(0.0);
		this.horaIngreso="";
		this.fechaIngreso="";
		this.datoFinanciacion=ConstantesBD.codigoNuncaValidoDouble;
		this.fechaGenFinal="";
		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getFechaGen() {
		return fechaGen;
	}

	public void setFechaGen(String fechaGen) {
		this.fechaGen = fechaGen;
	}

	public String getNomPaciente() {
		return nomPaciente;
	}

	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getGarantiaIngreso() {
		return garantiaIngreso;
	}

	public void setGarantiaIngreso(String garantiaIngreso) {
		this.garantiaIngreso = garantiaIngreso;
	}

	public String getCartera() {
		return cartera;
	}

	public void setCartera(String cartera) {
		this.cartera = cartera;
	}

	public String getFechaDoc() {
		return fechaDoc;
	}

	public void setFechaDoc(String fechaDoc) {
		this.fechaDoc = fechaDoc;
	}

	public BigDecimal getValorDoc() {
		return valorDoc;
	}

	public void setValorDoc(BigDecimal valorDoc) {
		this.valorDoc = valorDoc;
	}

	public String getFechaInicioDatoFin() {
		return fechaInicioDatoFin;
	}

	public void setFechaInicioDatoFin(String fechaInicioDatoFin) {
		this.fechaInicioDatoFin = fechaInicioDatoFin;
	}

	public int getNroCuotasDatoFin() {
		return nroCuotasDatoFin;
	}

	public void setNroCuotasDatoFin(int nroCuotasDatoFin) {
		this.nroCuotasDatoFin = nroCuotasDatoFin;
	}

	public int getDiasCuota() {
		return diasCuota;
	}

	public void setDiasCuota(int diasCuota) {
		this.diasCuota = diasCuota;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public int getConsecutivoFactura() {
		return consecutivoFactura;
	}

	public void setConsecutivoFactura(int consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public double getDatoFinanciacion() {
		return datoFinanciacion;
	}

	public void setDatoFinanciacion(double datoFinanciacion) {
		this.datoFinanciacion = datoFinanciacion;
	}

	public String getFechaGenFinal() {
		return fechaGenFinal;
	}

	public void setFechaGenFinal(String fechaGenFinal) {
		this.fechaGenFinal = fechaGenFinal;
	}

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
}