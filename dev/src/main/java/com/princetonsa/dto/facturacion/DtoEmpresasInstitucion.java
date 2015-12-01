package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

public class DtoEmpresasInstitucion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7281065268693214359L;

	private BigDecimal codigo;
	private int institucion;
	private String razonSocial;
	private String pais;
	private String departamento;
	private String direccion;
	private String telefono;
	private String codMinSalud;
	private String actividadEco;
	private String resolucion;
	private String prefFactura;
	private BigDecimal rgoInicFact;
	private BigDecimal rgoFinFact;
	private String encabezado;
	private String pie;
	private String logo;
	private String vigente;
	private String usuarioModifica;
	private String fecha_modifica;
	private String hora_modifica;
	private String nit;
	private String ciudad;
	private String tipoIdentificacion;
	private double valorConsecutivoFact;
	private String anioVigencia;
	private int digitoVerificacion;
	private String institucionPublica;
	private String codEmpTransEsp;
	private String ubicacionLogoReportes;
	private String pieHisCli;
	private int codigoInterfaz;

	private String resolucionFacturaVaria;
	private String prefFacturaVaria;
	private BigDecimal rgoInicFactVaria;
	private BigDecimal rgoFinFactVaria;
	private String encabezadoFacturaVaria;
	private String pieFacturaVaria;

	public DtoEmpresasInstitucion() {

		super();
		this.codigo = BigDecimal.ZERO;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.razonSocial = "";
		this.pais = "";
		this.departamento = "";
		this.direccion = "";
		this.telefono = "";
		this.codMinSalud = "";
		this.actividadEco = "";
		this.resolucion = "";
		this.prefFactura = "";
		this.rgoInicFact = BigDecimal.ZERO;
		this.rgoFinFact = BigDecimal.ZERO;
		this.encabezado = "";
		this.pie = "";
		this.logo = "";
		this.vigente = "";
		this.usuarioModifica = "";
		fecha_modifica = "";
		hora_modifica = "";
		this.nit = "";
		this.ciudad = "";
		this.tipoIdentificacion = "";
		this.valorConsecutivoFact = ConstantesBD.codigoNuncaValidoDouble;
		this.anioVigencia = "";
		this.digitoVerificacion = ConstantesBD.codigoNuncaValido;
		this.institucionPublica = "";
		this.codEmpTransEsp = "";
		this.ubicacionLogoReportes = "";
		this.pieHisCli = "";
		this.codigoInterfaz = ConstantesBD.codigoNuncaValido;

		this.resolucionFacturaVaria = "";
		this.prefFacturaVaria = "";
		this.rgoInicFactVaria = BigDecimal.ZERO;
		this.rgoFinFactVaria = BigDecimal.ZERO;
		this.encabezadoFacturaVaria = "";
		this.pieFacturaVaria = "";
	}

	public BigDecimal getRgoInicFact() {
		return rgoInicFact;
	}

	public void setRgoInicFact(BigDecimal rgoInicFact) {
		this.rgoInicFact = rgoInicFact;
	}

	public BigDecimal getRgoFinFact() {
		return rgoFinFact;
	}

	public void setRgoFinFact(BigDecimal rgoFinFact) {
		this.rgoFinFact = rgoFinFact;
	}

	public BigDecimal getCodigo() {
		return codigo;
	}

	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
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

	public String getCodMinSalud() {
		return codMinSalud;
	}

	public void setCodMinSalud(String codMinSalud) {
		this.codMinSalud = codMinSalud;
	}

	public String getActividadEco() {
		return actividadEco;
	}

	public void setActividadEco(String actividadEco) {
		this.actividadEco = actividadEco;
	}

	public String getResolucion() {
		return resolucion;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public String getPrefFactura() {
		return prefFactura;
	}

	public void setPrefFactura(String prefFactura) {
		this.prefFactura = prefFactura;
	}

	public String getEncabezado() {
		return encabezado;
	}

	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	public String getPie() {
		return pie;
	}

	public void setPie(String pie) {
		this.pie = pie;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getVigente() {
		return vigente;
	}

	public void setVigente(String vigente) {
		this.vigente = vigente;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFecha_modifica() {
		return fecha_modifica;
	}

	public void setFecha_modifica(String fechaModifica) {
		fecha_modifica = fechaModifica;
	}

	public String getHora_modifica() {
		return hora_modifica;
	}

	public void setHora_modifica(String horaModifica) {
		hora_modifica = horaModifica;
	}

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public double getValorConsecutivoFact() {
		return valorConsecutivoFact;
	}

	public void setValorConsecutivoFact(double valorConsecutivoFact) {
		this.valorConsecutivoFact = valorConsecutivoFact;
	}

	public String getAnioVigencia() {
		return anioVigencia;
	}

	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	public int getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(int digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getInstitucionPublica() {
		return institucionPublica;
	}

	public void setInstitucionPublica(String institucionPublica) {
		this.institucionPublica = institucionPublica;
	}

	public String getCodEmpTransEsp() {
		return codEmpTransEsp;
	}

	public void setCodEmpTransEsp(String codEmpTransEsp) {
		this.codEmpTransEsp = codEmpTransEsp;
	}

	public String getUbicacionLogoReportes() {
		return ubicacionLogoReportes;
	}

	public void setUbicacionLogoReportes(String ubicacionLogoReportes) {
		this.ubicacionLogoReportes = ubicacionLogoReportes;
	}

	public String getPieHisCli() {
		return pieHisCli;
	}

	public void setPieHisCli(String pieHisCli) {
		this.pieHisCli = pieHisCli;
	}

	public int getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(int codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}

	/**
	 * @param resolucionFacturaVaria
	 *            the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}

	/**
	 * @return the prefFacturaVaria
	 */
	public String getPrefFacturaVaria() {
		return prefFacturaVaria;
	}

	/**
	 * @param prefFacturaVaria
	 *            the prefFacturaVaria to set
	 */
	public void setPrefFacturaVaria(String prefFacturaVaria) {
		this.prefFacturaVaria = prefFacturaVaria;
	}

	/**
	 * @return the rgoInicFactVaria
	 */
	public BigDecimal getRgoInicFactVaria() {
		return rgoInicFactVaria;
	}

	/**
	 * @param rgoInicFactVaria
	 *            the rgoInicFactVaria to set
	 */
	public void setRgoInicFactVaria(BigDecimal rgoInicFactVaria) {
		this.rgoInicFactVaria = rgoInicFactVaria;
	}

	/**
	 * @return the rgoFinFactVaria
	 */
	public BigDecimal getRgoFinFactVaria() {
		return rgoFinFactVaria;
	}

	/**
	 * @param rgoFinFactVaria
	 *            the rgoFinFactVaria to set
	 */
	public void setRgoFinFactVaria(BigDecimal rgoFinFactVaria) {
		this.rgoFinFactVaria = rgoFinFactVaria;
	}

	/**
	 * @return the encabezadoFacturaVaria
	 */
	public String getEncabezadoFacturaVaria() {
		return encabezadoFacturaVaria;
	}

	/**
	 * @param encabezadoFacturaVaria
	 *            the encabezadoFacturaVaria to set
	 */
	public void setEncabezadoFacturaVaria(String encabezadoFacturaVaria) {
		this.encabezadoFacturaVaria = encabezadoFacturaVaria;
	}

	/**
	 * @return the pieFacturaVaria
	 */
	public String getPieFacturaVaria() {
		return pieFacturaVaria;
	}

	/**
	 * @param pieFacturaVaria
	 *            the pieFacturaVaria to set
	 */
	public void setPieFacturaVaria(String pieFacturaVaria) {
		this.pieFacturaVaria = pieFacturaVaria;
	}

}
