package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;
import util.UtilidadTexto;

public class DtoRecibosConceptoAnticiposRecibidosConvenio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el numero del 
	 * recibo de caja
	 */
	private String numeroRC;
	
	/**
	 * Atributo que almacena la fecha de registros
	 * del Recibo de caja
	 */
	private Date fechaRC;

	/**
	 * Atributo que almacena el consecutivo
	 * del detalle conceptos RC
	 */
	private Integer consecutivoConcepto;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del concepto del RC
	 */
	private String descripcionConcepto;
	
	/**
	 * Atributo que me almacena la lista de 
	 * formas de pago asociadas a un recibo de caja
	 */
	private ArrayList<DtoFormaPagoReport> listaFormaPago;
	
	
	private int codEstadoRC;
	
	/**
	 * Atributo que almacena el estado del RC
	 */
	private String estadoRC;
	
	/**
	 * Atributo que almacena el usuario que 
	 * registr&oacute; el RC 
	 */
	private String loginUsuario;
	
	/**
	 * Atributo que almacena el valor 
	 * total del RC
	 */
	private double valorTotal;
	
	/**
	 * Atributo que almacena el valor 
	 * del RC por forma de pago
	 */
	private double valorDetalle;
	
	/**
	 * Atributo que almacena el c&oacute;digo 
	 * de la forma de pago
	 */
	private int codFormaPago;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * de la forma de pago
	 */
	private String formaPago;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * de la Instituci&oacute;n
	 */ //OJO
	 
	private String nombreInstitucion; 
	
	/**
	 * Atributo que almacena el consecutivo
	 * del centro de atenci&oacute;n
	 * 
	 */
	private int consCentroAtencion;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del centro de atenci&oacute;n
	 *///OJO
	private String descCentroAtencion;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * del Pa&iacute;s
	 *///OJO
	private String descripcionPais;
	
	/**
	 * Atributo que almacena la descripci&oacute;n
	 * de la ciudad
	 *///OJO
	private String descripcionCiudad;
	
	/**
	 * Atributo que almacena la descripci&oacute;n de 
	 * una regi&oacute;n de cobertura.
	 *///OJO
	private String descripcionRegionCobertura;
	
	/**
	 * Atributo que almacena el n&uacute;mero
	 * Id del Convenio
	 */
	private String numeroIdConvenio;
	
	/**
	 * Atributo que almacena el 
	 * nombre del convenio
	 */
	private String nombreConvenio;
	
	/**
	 * Atributo que almacena el valor
	 * total de recibos de caja por Convenio	
	 */
	private Double totalConvenio;
	
	/**
	 * Atributo que almacena el codigo
	 * de la institucion. Depende del 
	 * par&aacute;metro Maneja Multiempresa.
	 * S = codigoEmpresaInstitucion
	 * N = codigoInstitucion 
	 */
	private long codigoIns;//OJO
	
	/**
	 * Atributo que almacena el c&oacute;digo
	 * de la Instituci&oacute;n a la cual 
	 * pertenece el Centro de Atenci&oacute; al 
	 * cual esta asociado el Recibo de Caja
	 * Hace parte de la llave primaria compuesta de 
	 * ReciboCaja
	 */
	private int codInstRC;
	
	
	/** Objeto jasper para el subreporte del detalle del consolidado*/
    transient private JRDataSource dsListaFormaPago;
    /**
     * Almacena el valor total con el formato adecuado.
     */
    private String valorTotalFormateado;
    
	
	
	
	public DtoRecibosConceptoAnticiposRecibidosConvenio(){
		reset();
	}

	public DtoRecibosConceptoAnticiposRecibidosConvenio(String numeroRC){
		reset();
		this.numeroRC=numeroRC;
	}

	private void reset() {

		this.numeroRC="";
		this.fechaRC=null;
		this.consecutivoConcepto=ConstantesBD.codigoNuncaValido;
		this.descripcionConcepto="";
		this.codEstadoRC=ConstantesBD.codigoNuncaValido;
		this.estadoRC="";
		this.loginUsuario="";
		this.valorTotal=ConstantesBD.codigoNuncaValidoDouble;
		this.valorDetalle=ConstantesBD.codigoNuncaValidoDouble;
		this.codFormaPago=ConstantesBD.codigoNuncaValido;
		this.formaPago="";
		this.nombreInstitucion="";
		this.consCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.descCentroAtencion="";
		this.descripcionPais="";
		this.descripcionCiudad="";
		this.descripcionRegionCobertura="";
		this.numeroIdConvenio="";
		this.nombreConvenio="";
		this.totalConvenio=ConstantesBD.codigoNuncaValidoDouble;
		this.setListaFormaPago(new ArrayList<DtoFormaPagoReport>());
		this.setCodigoIns(ConstantesBD.codigoNuncaValidoLong);
		this.codInstRC=ConstantesBD.codigoNuncaValido;
		this.valorTotalFormateado = "";
		
	}

	public String getNumeroRC() {
		return numeroRC;
	}

	public void setNumeroRC(String numeroRC) {
		this.numeroRC = numeroRC;
	}

	public Date getFechaRC() {
		return fechaRC;
	}

	public void setFechaRC(Date fechaRC) {
		this.fechaRC = fechaRC;
	}

	public Integer getConsecutivoConcepto() {
		return consecutivoConcepto;
	}

	public void setConsecutivoConcepto(Integer consecutivoConcepto) {
		this.consecutivoConcepto = consecutivoConcepto;
	}

	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	public String getEstadoRC() {
		return estadoRC;
	}

	public void setEstadoRC(String estadoRC) {
		this.estadoRC = estadoRC;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	
	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public int getConsCentroAtencion() {
		return consCentroAtencion;
	}

	public void setConsCentroAtencion(int consCentroAtencion) {
		this.consCentroAtencion = consCentroAtencion;
	}

	public String getDescCentroAtencion() {
		return descCentroAtencion;
	}

	public void setDescCentroAtencion(String descCentroAtencion) {
		this.descCentroAtencion = descCentroAtencion;
	}

	public String getDescripcionPais() {
		return descripcionPais;
	}

	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	public String getNumeroIdConvenio() {
		return numeroIdConvenio;
	}

	public void setNumeroIdConvenio(String numeroIdConvenio) {
		this.numeroIdConvenio = numeroIdConvenio;
	}

	public String getNombreConvenio() {
		return nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	public Double getTotalConvenio() {
		return totalConvenio;
	}

	public void setTotalConvenio(Double totalConvenio) {
		this.totalConvenio = totalConvenio;
	}

	public void setListaFormaPago(ArrayList<DtoFormaPagoReport> listaFormaPago) {
		this.listaFormaPago = listaFormaPago;
	}

	public ArrayList<DtoFormaPagoReport> getListaFormaPago() {
		return listaFormaPago;
	}

	public void setCodigoIns(long codigoIns) {
		this.codigoIns = codigoIns;
	}

	public long getCodigoIns() {
		return codigoIns;
	}

	public void setCodInstRC(int codInstRC) {
		this.codInstRC = codInstRC;
	}

	public int getCodInstRC() {
		return codInstRC;
	}

	public void setDsListaFormaPago(JRDataSource dsListaFormaPago) {
		this.dsListaFormaPago = dsListaFormaPago;
	}

	public JRDataSource getDsListaFormaPago() {
		return dsListaFormaPago;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorDetalle() {
		return valorDetalle;
	}

	public void setValorDetalle(double valorDetalle) {
		this.valorDetalle = valorDetalle;
	}

	public int getCodFormaPago() {
		return codFormaPago;
	}

	public void setCodFormaPago(int codFormaPago) {
		this.codFormaPago = codFormaPago;
	}

	public void setCodEstadoRC(int codEstadoRC) {
		this.codEstadoRC = codEstadoRC;
	}

	public int getCodEstadoRC() {
		return codEstadoRC;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  valorTotalFormateado
	 *
	 * @return retorna la variable valorTotalFormateado
	 */
	public String getValorTotalFormateado() {
		
		this.valorTotalFormateado = UtilidadTexto.formatearValores(this.valorTotal);
		return valorTotalFormateado;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo valorTotalFormateado
	 * @param valorTotalFormateado es el valor para el atributo valorTotalFormateado 
	 */
	public void setValorTotalFormateado(String valorTotalFormateado) {
		this.valorTotalFormateado = valorTotalFormateado;
	}
}
