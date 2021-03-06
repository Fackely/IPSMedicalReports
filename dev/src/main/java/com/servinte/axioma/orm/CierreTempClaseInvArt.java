package com.servinte.axioma.orm;

// Generated 14/01/2012 12:39:52 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

/**
 * CierreTempClaseInvArt generated by hbm2java
 */
public class CierreTempClaseInvArt implements java.io.Serializable {

	private long codigoPk;
	private Contratos contratos;
	private ClaseInventario claseInventario;
	private BigDecimal valorAcumulado;
	private Date fechaCierre;

	public CierreTempClaseInvArt() {
	}

	public CierreTempClaseInvArt(long codigoPk, Contratos contratos,
			ClaseInventario claseInventario, BigDecimal valorAcumulado,
			Date fechaCierre) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.claseInventario = claseInventario;
		this.valorAcumulado = valorAcumulado;
		this.fechaCierre = fechaCierre;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public ClaseInventario getClaseInventario() {
		return this.claseInventario;
	}

	public void setClaseInventario(ClaseInventario claseInventario) {
		this.claseInventario = claseInventario;
	}

	public BigDecimal getValorAcumulado() {
		return this.valorAcumulado;
	}

	public void setValorAcumulado(BigDecimal valorAcumulado) {
		this.valorAcumulado = valorAcumulado;
	}

	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

}
