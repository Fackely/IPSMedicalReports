package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoReporteAnticiposRecibidosConvenio implements Serializable{
	
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena la fecha inicial desde la cual se
	 * realiza la b&uacute;squeda 
	 */
	
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final hasta la cual se
	 * realiza la b&uacute;squeda 
	 */
	
	private Date fechaFinal;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la regi&oacute;n de cobertura.
	 */
	private long codigoRegion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el primer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que almacena el segundo valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoPais;
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del centro de 
	 * atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;

	/**
	 * Atributo que almacena el c&oacute;digo del Convenio
	 */
	private Integer codigoConvenio;
	
	/**
	 * Atributo que almacena el c&oacute;digo del concepto
	 * de ingresos de tesoreria con tipo ingresos = 
	 * 'Anticipos Convenios Odontologia'
	 */
	private String codigoConceptoAnticipo;

	/**
	 * Atributo que almacena el c&oacute;digo del estado
	 * del recibo de caja
	 */
	private Integer codigoEstadoRC;
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	/**
	 * Atributo que alamacena el valor de Multiempresa
	 * S o N
	 */
	private boolean esMultiempresa;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del concepto del RC
	 */
	private String descripcionConcepto;
	
	/**
	 * Atributo que almacena la ruta del logo a mostrar en el reporte.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que almacena la ubicaci&oacute;n
	 * del logo
	 */
	private String ubicacionLogo;
	
	private String fechaInicialFormateada;
	private String fechaFinalFormateada;
	private String nombreUsuarioProceso;
	
	/**
	 * Atributo que permite indicar si se debe mostrar la columna de concepto
	 * en el reporte.
	 */
	private boolean mostrarConceptos;
	
	
	public DtoReporteAnticiposRecibidosConvenio(){
		
		this.fechaInicial=null;
		this.fechaFinal=null;
		this.codigoPaisResidencia="";
		this.ciudadDeptoPais="";
		this.codigoRegion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoCiudad="";
		this.codigoPais="";
		this.codigoDpto="";
		this.consecutivoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.codigoConceptoAnticipo="";
		this.codigoEstadoRC=ConstantesBD.codigoNuncaValido;
		this.setRazonSocial("");
		this.setDescripcionConcepto("");
		this.setRutaLogo("");
		this.setUbicacionLogo("");
		this.fechaFinalFormateada="";
		this.fechaInicialFormateada="";
		this.nombreUsuarioProceso="";
		this.mostrarConceptos = true;
		
	}


	public Date getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public Date getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}


	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}


	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}


	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}


	public long getCodigoRegion() {
		return codigoRegion;
	}


	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
	}


	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}


	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}


	public String getCodigoCiudad() {
		return codigoCiudad;
	}


	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}


	public String getCodigoPais() {
		return codigoPais;
	}


	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}


	public String getCodigoDpto() {
		return codigoDpto;
	}


	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}


	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}


	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}


	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getCodigoConceptoAnticipo() {
		return codigoConceptoAnticipo;
	}


	public void setCodigoConceptoAnticipo(String codigoConceptoAnticipo) {
		this.codigoConceptoAnticipo = codigoConceptoAnticipo;
	}


	public Integer getCodigoEstadoRC() {
		return codigoEstadoRC;
	}


	public void setCodigoEstadoRC(Integer codigoEstadoRC) {
		this.codigoEstadoRC = codigoEstadoRC;
	}


	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public String getRazonSocial() {
		return razonSocial;
	}


	public void setEsMultiempresa(boolean esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}


	public boolean isEsMultiempresa() {
		return esMultiempresa;
	}


	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}


	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}


	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	public String getRutaLogo() {
		return rutaLogo;
	}


	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	public String getFechaInicialFormateada() {
		fechaInicialFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
		return fechaInicialFormateada;
	}


	public void setFechaInicialFormateada(String fechaInicialFormateada) {
		this.fechaInicialFormateada = fechaInicialFormateada;
	}


	public String getFechaFinalFormateada() {
		fechaFinalFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
		return fechaFinalFormateada;
	}


	public void setFechaFinalFormateada(String fechaFinalFormateada) {
		this.fechaFinalFormateada = fechaFinalFormateada;
	}


	public String getNombreUsuarioProceso() {
		return nombreUsuarioProceso;
	}


	public void setNombreUsuarioProceso(String nombreUsuarioProceso) {
		this.nombreUsuarioProceso = nombreUsuarioProceso;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  mostrarConceptos
	 *
	 * @return retorna la variable mostrarConceptos
	 */
	public boolean isMostrarConceptos() {
		return mostrarConceptos;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo mostrarConceptos
	 * @param mostrarConceptos es el valor para el atributo mostrarConceptos 
	 */
	public void setMostrarConceptos(boolean mostrarConceptos) {
		this.mostrarConceptos = mostrarConceptos;
	}
	
	
}
