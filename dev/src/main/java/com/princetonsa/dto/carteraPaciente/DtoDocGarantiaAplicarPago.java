package com.princetonsa.dto.carteraPaciente;
import java.io.Serializable;

import util.ConstantesBD;

public class DtoDocGarantiaAplicarPago implements Serializable
{
	private String tipo;
	private String codigoGarantia;
	private int cuotas;
	private double valorInicial;
	private double saldoActual;
	private double valorAplicar;
	
	public void reset()
	{
		this.tipo="";
		this.codigoGarantia="";
		this.cuotas=ConstantesBD.codigoNuncaValido;
		this.valorInicial=ConstantesBD.codigoNuncaValido;
		this.saldoActual=ConstantesBD.codigoNuncaValido;
		this.valorAplicar=ConstantesBD.codigoNuncaValido;
	}

	public DtoDocGarantiaAplicarPago()
	{
		this.reset();
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodigoGarantia() {
		return codigoGarantia;
	}

	public void setCodigoGarantia(String codigoGarantia) {
		this.codigoGarantia = codigoGarantia;
	}

	public int getCuotas() {
		return cuotas;
	}

	public void setCuotas(int cuotas) {
		this.cuotas = cuotas;
	}

	public double getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(double valorInicial) {
		this.valorInicial = valorInicial;
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
}