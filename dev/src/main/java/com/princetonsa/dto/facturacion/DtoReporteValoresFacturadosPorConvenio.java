package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

public class DtoReporteValoresFacturadosPorConvenio implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena el c&oacute;digo que identifica a una ciudad
	 * determinada.
	 */
	private String ciudadDeptoPais;

	/**
	 * Atributos que almacenan el c&oacute;digo y el nombre de la empresa-instituci&oacute;n.
	 */
	private long codigoEmpresaInstitucion;
	private String nombreEmpresaInstitucion;

	/**
	 * Atributo que almacena el primer valor de la llave compuesta que
	 * identifica una ciudad.
	 */
	private String codigoCiudad;

	/**
	 * Atributo que almacena el segundo valor de la llave compuesta que
	 * identifica una ciudad.
	 */
	private String codigoPais;

	/**
	 * Atributo que almacena el tercer valor de la llave compuesta que
	 * identifica una ciudad.
	 */
	private String codigoDpto;

	/**
	 * Atributos que almacenan el c&oacute;digo y el nombre del centro de atenci&oacute;n.
	 */
	private int consecutivoCentroAtencion;
	private String nombreCentroAtencion;

	/**
	 * Atributos que almacenan el c&oacute;digo y nombre del convenio.
	 */
	private int codigoConvenio;
	private String nombreConvenio;

	/**
	 * Atributos que almacenan el c&oacute;digo, el nit y el nombre de la
	 * empresa.
	 */
	private int codigoEmpresa;
	private String nitEmpresa;
	private String nombreEmpresa;
	private String actividadEconomica;

	/**
	 * Atributo que almacena la fecha inicial desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes.
	 */
	private Date fechaInicial;

	/**
	 * Atributo que almacena la fecha final desde la cual se realiza la 
	 * b&uacute;squeda de las solicitudes
	 */
	private Date fechaFinal;

	private boolean multiempresa;

	private Double valorFacturado;
	private Double valorFacturasAnuladas;
	private String usuarioProceso;
	private String razonSocial;
	private String nit;
	private String rutaLogo;
	private String logoDerecha;
	private Double totalFacturado;
	private Double valorNetoFacturado;
	private Double sumatoriaValorFacturado;
	private Double sumatoriaValorFacturasAnuladas;
	private Double sumatoriaValorNetoFacturadoAnulado;	
	
	/**
	 * M&eacute;todo constructor de la clase
	 */
	public DtoReporteValoresFacturadosPorConvenio() {
		this.ciudadDeptoPais = "";
		this.codigoEmpresaInstitucion = ConstantesBD.codigoNuncaValidoLong;
		this.nombreEmpresaInstitucion = "";
		this.codigoCiudad = "";
		this.codigoPais = "";
		this.codigoDpto = "";
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion = "";
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.nombreConvenio = "";
		this.codigoEmpresa = ConstantesBD.codigoNuncaValido;
		this.nitEmpresa = null;
		this.nombreEmpresa = null;
		this.setActividadEconomica(null);
		this.fechaInicial = null;
		this.fechaFinal = null;
		this.usuarioProceso = "";
		this.razonSocial = "";
		this.nit = "";
		this.rutaLogo = "";
		this.logoDerecha = "";
		
		this.valorFacturado= ConstantesBD.codigoNuncaValidoDouble;
		this.valorFacturasAnuladas= ConstantesBD.codigoNuncaValidoDouble;
		this.totalFacturado= ConstantesBD.codigoNuncaValidoDouble;
		this.valorNetoFacturado= ConstantesBD.codigoNuncaValidoDouble;
		this.setSumatoriaValorFacturado(ConstantesBD.codigoNuncaValidoDouble);
		this.setSumatoriaValorFacturasAnuladas(ConstantesBD.codigoNuncaValidoDouble);
		this.setSumatoriaValorNetoFacturadoAnulado(ConstantesBD.codigoNuncaValidoDouble);
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo ciudadDeptoPais.
	 * 
	 * @return ciudadDeptoPais
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * ciudadDeptoPais.
	 * 
	 * @param ciudadDeptoPais
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @return codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @param codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @return nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEmpresaInstitucion() {
		return nombreEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @param nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEmpresaInstitucion(String nombreEmpresaInstitucion) {
		this.nombreEmpresaInstitucion = nombreEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoCiudad.
	 * 
	 * @return codigoCiudad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo codigoCiudad.
	 * 
	 * @param codigoCiudad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoPais.
	 * 
	 * @return codigoPais
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo codigoPais.
	 * 
	 * @param codigoPais
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoDpto.
	 * 
	 * @return codigoDpto
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoDpto() {
		return codigoDpto;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo codigoDpto.
	 * 
	 * @param codigoDpto
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @return consecutivoCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @param consecutivoCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoConvenio.
	 * 
	 * @return codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoConvenio.
	 * 
	 * @param codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreConvenio.
	 * 
	 * @return nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreConvenio.
	 * 
	 * @param nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoEmpresa.
	 * 
	 * @return codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEmpresa.
	 * 
	 * @param codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nitEmpresa.
	 * 
	 * @return nitEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNitEmpresa() {
		return nitEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nitEmpresa.
	 * 
	 * @param nitEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNitEmpresa(String nitEmpresa) {
		this.nitEmpresa = nitEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreEmpresa.
	 * 
	 * @return nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nombreEmpresa.
	 * 
	 * @param nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaInicial.
	 * 
	 * @return fechaInicial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaInicial.
	 * 
	 * @param fechaInicial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaFinal.
	 * 
	 * @return fechaFinal
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaFinal.
	 * 
	 * @param fechaFinal
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo multiempresa.
	 * 
	 * @return multiempresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isMultiempresa() {
		return multiempresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo multiempresa.
	 * 
	 * @param multiempresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setMultiempresa(boolean multiempresa) {
		this.multiempresa = multiempresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valorFacturado.
	 * 
	 * @return valorFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValorFacturado() {
		return valorFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorFacturado.
	 * 
	 * @param valorFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValorFacturado(Double valorFacturado) {
		this.valorFacturado = valorFacturado;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * valorFacturasAnuladas.
	 * 
	 * @return valorFacturasAnuladas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getValorFacturasAnuladas() {
		return valorFacturasAnuladas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorFacturasAnuladas.
	 * 
	 * @param valorFacturasAnuladas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValorFacturasAnuladas(Double valorFacturasAnuladas) {
		this.valorFacturasAnuladas = valorFacturasAnuladas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo usuarioProceso.
	 * 
	 * @return usuarioProceso
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * usuarioProceso.
	 * 
	 * @param usuarioProceso
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo razonSocial.
	 * 
	 * @return razonSocial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo razonSocial.
	 * 
	 * @param razonSocial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nit.
	 * 
	 * @return nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nit.
	 * 
	 * @param nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo rutaLogo.
	 * 
	 * @return rutaLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo rutaLogo.
	 * 
	 * @param rutaLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo logoDerecha.
	 * 
	 * @return logoDerecha
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo logoDerecha.
	 * 
	 * @param logoDerecha
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo totalFacturado.
	 * 
	 * @return totalFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Double getTotalFacturado() {
		return totalFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * totalFacturado.
	 * 
	 * @param totalFacturado
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTotalFacturado(Double totalFacturado) {
		this.totalFacturado = totalFacturado;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * valorNetoFacturado.
	 * 
	 * @param valorNetoFacturado
	 * @author Diana Ruiz
	 */	

	public void setValorNetoFacturado(Double valorNetoFacturado) {
		this.valorNetoFacturado = valorNetoFacturado;
	}
	
	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valorNetoFacturado.
	 * 
	 * @return valorNetoFacturado
	 * @author Diana Ruiz
	 */

	public Double getValorNetoFacturado() {
		return valorNetoFacturado;
	}
	
	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * ActividadEconomica.
	 * 
	 * @param ActividadEconomica
	 * @author Diana Ruiz
	 */	

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}
	
	/**
	 * M&eacute;todo encargado de obtener el el valor del atributo ActividadEconomica.
	 * 
	 * @return ActividadEconomica
	 * @author Diana Ruiz
	 */

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setSumatoriaValorFacturado(Double sumatoriaValorFacturado) {
		this.sumatoriaValorFacturado = sumatoriaValorFacturado;
	}

	public Double getSumatoriaValorFacturado() {
		return sumatoriaValorFacturado;
	}

	public void setSumatoriaValorFacturasAnuladas(
			Double sumatoriaValorFacturasAnuladas) {
		this.sumatoriaValorFacturasAnuladas = sumatoriaValorFacturasAnuladas;
	}

	public Double getSumatoriaValorFacturasAnuladas() {
		return sumatoriaValorFacturasAnuladas;
	}

	public void setSumatoriaValorNetoFacturadoAnulado(
			Double sumatoriaValorNetoFacturadoAnulado) {
		this.sumatoriaValorNetoFacturadoAnulado = sumatoriaValorNetoFacturadoAnulado;
	}

	public Double getSumatoriaValorNetoFacturadoAnulado() {
		return sumatoriaValorNetoFacturadoAnulado;
	}

}
