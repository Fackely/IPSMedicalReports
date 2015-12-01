package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
@SuppressWarnings("serial")
public class DtoContrato implements Serializable {

	/**
	 * Codigo Pk de la tabla contratos 
	 */
	private int codigo ;
	
	/**
	 * 
	 */
	private int  convenio ;
	
	/**
	 * 
	 */
	private String  fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	/**
	 * 
	 */
	private String  numeroContrato; 
	
	/**
	 * 
	 */
	private double  valor;    
	
	/**
	 * 
	 */
	private double  acumulado;
	/**
	 * 
	 */
	private int  tipoPago;
	
	private char manejaTarifasXCa;
	
	/**
	 * Contrato
	 */
	public DtoContrato() {
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.convenio = ConstantesBD.codigoNuncaValido;
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.numeroContrato = "";
		this.valor = ConstantesBD.codigoNuncaValidoDouble;
		this.acumulado = ConstantesBD.codigoNuncaValidoDouble;
		this.tipoPago = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @return the numeroContrato
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}
	/**
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}
	/**
	 * @return the acumulado
	 */
	public double getAcumulado() {
		return acumulado;
	}
	/**
	 * @return the tipoPago
	 */
	public int getTipoPago() {
		return tipoPago;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}
	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @param numeroContrato the numeroContrato to set
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}
	/**
	 * @param acumulado the acumulado to set
	 */
	public void setAcumulado(double acumulado) {
		this.acumulado = acumulado;
	}
	/**
	 * @param tipoPago the tipoPago to set
	 */
	public void setTipoPago(int tipoPago) {
		this.tipoPago = tipoPago;
	}  
	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicialDate(Date fechaInicial) {
		this.fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinalDate(Date fechaFinal) {
		this.fechaFinal = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo manejaTarifasXCa
	
	 * @return retorna la variable manejaTarifasXCa 
	 * @author Angela Maria Aguirre 
	 */
	public char getManejaTarifasXCa() {
		return manejaTarifasXCa;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo manejaTarifasXCa
	
	 * @param valor para el atributo manejaTarifasXCa 
	 * @author Angela Maria Aguirre 
	 */
	public void setManejaTarifasXCa(char manejaTarifasXCa) {
		this.manejaTarifasXCa = manejaTarifasXCa;
	}

	
	


	
	
}
