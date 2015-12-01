package com.princetonsa.dto.facturasVarias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ValoresPorDefecto;


import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;


import net.sf.jasperreports.engine.JRDataSource;



public class DtoConsolodadoFacturaVaria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el concecutivo
	 * de la factura varia.
	 */
	private String consecutivofactura;
	
	/**
	 * Atributo que almacena el nombre del
	 * usuario por el cual se genero el reporte.
	 */
	private String usuario;
	
	/**
	 * Atributo que almacena el primer nombre
	 * del profecional que contrato
	 */
	private String primerNombreProfesionalContrato;
	
	/**
	 * Atributo que almacena el segundo nombre
	 * del profecional que contrato
	 */
	private String segundoNombreProfesionalContrato;
	
	/**
	 * Atributo que almacena el primer apellido
	 * del profecional que contrato
	 */
	private String primerApellidoProfesionalContrato;
	
	/**
	 * Atributo que almacena el segundo apellido
	 * del profecional que contrato
	 */
	private String segundoApellidoProfesionalContrato;
	
	/**
	 * Atributo que almacena el nombre completo
	 * del profecional que contrato
	 */
	private String nombreCompletoProfesionalContrato;
	
	/**
	 * Atributo que almacena el estado del contrato
	 */
	private String estado;
	
	/**
	 * Atributo que almacena el concecutivo del centro de atencion
	 */
	private String consecutivo;
	
	/**
	 * Atributo que almacena la descripción del centro de atencion
	 */
	private String descripcioncentroatencion;
	
	/**
	 * Atributo que almacena la razón social de la institución
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena el codigo de la factura
	 */
	private String codigofactura;
	
	/**
	 * Atributo que almacena el concepto de la factura varia
	 */
	private String descripcion;
	
	/**
	 * Atributo que almacena la fecha de elaboración de la factura
	 */
	private String fechaelaboracion;
	
	/**
	 * Atributo que almacena el deudor
	 */
	private String deudor;
	
	/**
	 * Atributo que almacena la desripcion del deudor
	 */
	private String desdeudor;
	
	/**
	 * Atributo que almacena la identificación del deudor
	 */
	private String idtercero;
	
	/**
	 * Atributo que almacena el el tipo de deudor
	 */
	private String tipodeudor;
	
	/**
	 * Atributo que almacena el valor inicial
	 */
	private String valorinicial;
	
	/**
	 * Atributo que almacena el valor de ajustes debito
	 */
	private String ajustesdebito;
	
	/**
	 * Atributo que almacena el valor de ajustes credito
	 */
	private String ajustescredito;
	
	/**
	 * Atributo que almacena el valor de pagos aplicados
	 */
	private String pagosaplicados;
	
	/**
	 * Atributo que almacena el valor del saldo
	 */
	private String saldo;
	
	/**
	 * Atributo que almacena el valor total inicial
	 */
	private double totalValorInicial;
	
	/**
	 * Atributo que almacena el valor total de ajustes debito
	 */
	private double totalValorAjusteDebito;
	
	/**
	 * Atributo que almacena el valor total de ajustes credito
	 */
	private double totalValorAjusteCredito;
	
	/**
	 * Atributo que almacena el valor total de pagos aplicados
	 */
	private double totalPagosAplicados;
	
	/**
	 * Atributo que almacena el valor total del saldo
	 */
	private double totalSaldo;
	private String totalValorInicialFormato;
	private String totalValorAjusteDebitoFormato;
	private String totalValorAjusteCreditoFormato;
	private String totalPagosAplicadosFormato;
	private String totalSaldoFormato;
	private String descripcionInstCentroCont;
	private String descripcionCiudad;
	private String descripcionPais;
	private String descripcionRegionCobertura;
	private String ayudanteDeudor;
	private String ayudanteValorInicial;
	private String ayudanteAjustesDebito;
	private String ayudanteAjustesCredito;
	private String ayudantePagosAplicados;
	private String ayudanteSaldo;
	
	/** Objeto jasper para el subreporte de cambio Promocion */
	private JRDataSource dsResultadoConsultaFac;
    
	private ArrayList<DtoConsolodadoFacturaVaria> listadoConsolidado;
	
    public DtoConsolodadoFacturaVaria() {
		
		this.usuario="";
		this.estado="";
		
		this.descripcioncentroatencion="";
		this.razonSocial="";
		
		this.descripcion="";
		
		this.deudor="";
		this.desdeudor="";
		this.idtercero="";
		this.tipodeudor="";
		
		this.descripcionCiudad="";
		this.descripcionPais="";
		this.descripcionRegionCobertura="";
	}
    
	public String getConsecutivofactura() {
		return consecutivofactura;
	}
	public void setConsecutivofactura(String consecutivofactura) {
		this.consecutivofactura = consecutivofactura;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEstado() {
		return (String) ValoresPorDefecto.getIntegridadDominio(this.estado);
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	public String getDescripcioncentroatencion() {
		return descripcioncentroatencion;
	}
	public void setDescripcioncentroatencion(String descripcioncentroatencion) {
		this.descripcioncentroatencion = descripcioncentroatencion;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getCodigofactura() {
		return codigofactura;
	}
	public void setCodigofactura(String codigofactura) {
		this.codigofactura = codigofactura;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFechaelaboracion() {
		return fechaelaboracion;
	}
	public void setFechaelaboracion(String fechaelaboracion) {
		this.fechaelaboracion = fechaelaboracion;
	}
	public String getDeudor() {
		return deudor;
	}
	public void setDeudor(String deudor) {
		this.deudor = deudor;
	}
	public String getDesdeudor() {
		return desdeudor;
	}
	public void setDesdeudor(String desdeudor) {
		this.desdeudor = desdeudor;
	}
	public String getIdtercero() {
		return idtercero;
	}
	public void setIdtercero(String idtercero) {
		this.idtercero = idtercero;
	}
	public String getTipodeudor() {
		
		return (String) ValoresPorDefecto.getIntegridadDominio(this.tipodeudor);
	}
	public void setTipodeudor(String tipodeudor) {
		this.tipodeudor = tipodeudor;
	}
	public String getValorinicial() {
		return valorinicial;
	}
	public void setValorinicial(String valorinicial) {
		this.valorinicial = valorinicial;
	}
	public String getAjustesdebito() {
		return ajustesdebito;
	}
	public void setAjustesdebito(String ajustesdebito) {
		this.ajustesdebito = ajustesdebito;
	}
	public String getAjustescredito() {
		return ajustescredito;
	}
	public void setAjustescredito(String ajustescredito) {
		this.ajustescredito = ajustescredito;
	}
	public String getPagosaplicados() {
		return pagosaplicados;
	}
	public void setPagosaplicados(String pagosaplicados) {
		this.pagosaplicados = pagosaplicados;
	}
	public String getSaldo() {
		return saldo;
	}
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
	public double getTotalValorInicial() {
		return totalValorInicial;
	}
	public void setTotalValorInicial(double totalValorInicial) {
		this.totalValorInicial = totalValorInicial;
	}
	public double getTotalValorAjusteDebito() {
		return totalValorAjusteDebito;
	}
	public void setTotalValorAjusteDebito(double totalValorAjusteDebito) {
		this.totalValorAjusteDebito = totalValorAjusteDebito;
	}
	public double getTotalValorAjusteCredito() {
		return totalValorAjusteCredito;
	}
	public void setTotalValorAjusteCredito(double totalValorAjusteCredito) {
		this.totalValorAjusteCredito = totalValorAjusteCredito;
	}
	public double getTotalPagosAplicados() {
		return totalPagosAplicados;
	}
	public void setTotalPagosAplicados(double totalPagosAplicados) {
		this.totalPagosAplicados = totalPagosAplicados;
	}
	public double getTotalSaldo() {
		return totalSaldo;
	}
	public void setTotalSaldo(double totalSaldo) {
		this.totalSaldo = totalSaldo;
	}
	public String getTotalValorInicialFormato() {
		BigDecimal m= new BigDecimal(totalValorInicial);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			totalValorInicialFormato="-";
		}
		else{
			totalValorInicialFormato=number;
		}
		return totalValorInicialFormato;
	}
	public void setTotalValorInicialFormato(String totalValorInicialFormato) {
		this.totalValorInicialFormato = totalValorInicialFormato;
	}
	public String getTotalValorAjusteDebitoFormato() {
		BigDecimal m= new BigDecimal(totalValorAjusteDebito);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			totalValorAjusteDebitoFormato=number;
		
		return totalValorAjusteDebitoFormato;
	}
	public void setTotalValorAjusteDebitoFormato(
			String totalValorAjusteDebitoFormato) {
		this.totalValorAjusteDebitoFormato = totalValorAjusteDebitoFormato;
	}
	public String getTotalValorAjusteCreditoFormato() {
		BigDecimal m= new BigDecimal(totalValorAjusteCredito);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			totalValorAjusteCreditoFormato=number;
		
		return totalValorAjusteCreditoFormato;
	}
	public void setTotalValorAjusteCreditoFormato(
			String totalValorAjusteCreditoFormato) {
		this.totalValorAjusteCreditoFormato = totalValorAjusteCreditoFormato;
	}
	public String getTotalPagosAplicadosFormato() {
		BigDecimal m= new BigDecimal(totalPagosAplicados);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			totalPagosAplicadosFormato=number;
		
		return totalPagosAplicadosFormato;
	}
	public void setTotalPagosAplicadosFormato(String totalPagosAplicadosFormato) {
		this.totalPagosAplicadosFormato = totalPagosAplicadosFormato;
	}
	public String getTotalSaldoFormato() {
		BigDecimal m= new BigDecimal(totalSaldo);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			totalSaldoFormato="-";
		}
		else{
			totalSaldoFormato=number;
		}
		return totalSaldoFormato;
	}
	public void setTotalSaldoFormato(String totalSaldoFormato) {
		this.totalSaldoFormato = totalSaldoFormato;
	}
	public void setDsResultadoConsultaFac(JRDataSource dsResultadoConsultaFac) {
		this.dsResultadoConsultaFac = dsResultadoConsultaFac;
	}
	public JRDataSource getDsResultadoConsultaFac() {
		return dsResultadoConsultaFac;
	}
	public void setListadoConsolidado(ArrayList<DtoConsolodadoFacturaVaria> listadoConsolidado) {
		this.listadoConsolidado = listadoConsolidado;
	}
	public ArrayList<DtoConsolodadoFacturaVaria> getListadoConsolidado() {
		return listadoConsolidado;
	}

	public void setDescripcionInstCentroCont(String descripcionInstCentroCont) {
		this.descripcionInstCentroCont = descripcionInstCentroCont;
	}

	public String getDescripcionInstCentroCont() {
		this.descripcionInstCentroCont = "Centro de Atención que contrató: " + descripcioncentroatencion+ " - " +
		descripcionCiudad + " (" + descripcionPais + ") " + "- " + "Región: " + descripcionRegionCobertura;  
		return descripcionInstCentroCont;
	}
	
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	public String getDescripcionPais() {
		return descripcionPais;
	}

	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}

	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}

	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}

	public void setAyudanteDeudor(String ayudanteDeudor) {
		this.ayudanteDeudor = ayudanteDeudor ;
	}

	public String getAyudanteDeudor() {
		this.ayudanteDeudor = desdeudor +" "+ idtercero;
		return ayudanteDeudor;
	}

	public void setAyudanteValorInicial(String ayudanteValorInicial) {
		this.ayudanteValorInicial = ayudanteValorInicial;
	}

	public String getAyudanteValorInicial() {
		String n = this.valorinicial;
		BigDecimal m= new BigDecimal(n);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			ayudanteValorInicial=number;
		
		return ayudanteValorInicial;
	}

	public void setAyudanteAjustesDebito(String ayudanteAjustesDebito) {
		this.ayudanteAjustesDebito = ayudanteAjustesDebito;
	}

	public String getAyudanteAjustesDebito() {
		String n = this.ajustesdebito;
		BigDecimal m= new BigDecimal(n);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			ayudanteAjustesDebito=number;
		
		return ayudanteAjustesDebito;
	}

	public void setAyudanteAjustesCredito(String ayudanteAjustesCredito) {
		this.ayudanteAjustesCredito = ayudanteAjustesCredito;
	}

	public String getAyudanteAjustesCredito() {
		String n = this.ajustescredito;
		BigDecimal m= new BigDecimal(n);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			ayudanteAjustesCredito=number;
		
		return ayudanteAjustesCredito;
	}

	public void setAyudantePagosAplicados(String ayudantePagosAplicados) {
		this.ayudantePagosAplicados = ayudantePagosAplicados;
	}

	public String getAyudantePagosAplicados() {
		String n = this.pagosaplicados;
		BigDecimal m= new BigDecimal(n);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			ayudantePagosAplicados=number;
		
		return ayudantePagosAplicados;
	}

	public void setAyudanteSaldo(String ayudanteSaldo) {
		this.ayudanteSaldo = ayudanteSaldo;
	}

	public String getAyudanteSaldo() {
		String n = this.saldo; 
		BigDecimal m= new BigDecimal(n);
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		
			ayudanteSaldo=number;
		
		return ayudanteSaldo;
	}

	public void setPrimerNombreProfesionalContrato(
			String primerNombreProfesionalContrato) {
		this.primerNombreProfesionalContrato = primerNombreProfesionalContrato;
	}

	public String getPrimerNombreProfesionalContrato() {
		return primerNombreProfesionalContrato;
	}

	public void setSegundoNombreProfesionalContrato(
			String segundoNombreProfesionalContrato) {
		this.segundoNombreProfesionalContrato = segundoNombreProfesionalContrato;
	}

	public String getSegundoNombreProfesionalContrato() {
		return segundoNombreProfesionalContrato;
	}

	public void setPrimerApellidoProfesionalContrato(
			String primerApellidoProfesionalContrato) {
		this.primerApellidoProfesionalContrato = primerApellidoProfesionalContrato;
	}

	public String getPrimerApellidoProfesionalContrato() {
		return primerApellidoProfesionalContrato;
	}

	public void setSegundoApellidoProfesionalContrato(
			String segundoApellidoProfesionalContrato) {
		this.segundoApellidoProfesionalContrato = segundoApellidoProfesionalContrato;
	}

	public String getSegundoApellidoProfesionalContrato() {
		return segundoApellidoProfesionalContrato;
	}

	public void setNombreCompletoProfesionalContrato(
			String nombreCompletoProfesionalContrato) {
		this.nombreCompletoProfesionalContrato = nombreCompletoProfesionalContrato;
	}

	public String getNombreCompletoProfesionalContrato() {
		return nombreCompletoProfesionalContrato;
	}
}
