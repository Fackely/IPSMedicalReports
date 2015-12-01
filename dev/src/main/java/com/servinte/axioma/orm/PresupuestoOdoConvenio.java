package com.servinte.axioma.orm;

// Generated Feb 5, 2011 1:13:56 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * PresupuestoOdoConvenio generated by hbm2java
 */
public class PresupuestoOdoConvenio implements java.io.Serializable {

	private long codigoPk;
	private Contratos contratos;
	private Usuarios usuarios;
	private PresupuestoPaquetes presupuestoPaquetes;
	private PresupuestoOdoProgServ presupuestoOdoProgServ;
	private Convenios convenios;
	private BigDecimal valorUnitario;
	private Date fechaModifica;
	private String horaModifica;
	private String contratado;
	private Integer detPromocion;
	private BigDecimal porcentajeDescuentoProm;
	private BigDecimal valorHonorarioProm;
	private String advertenciaProm;
	private BigDecimal valorDescuentoProm;
	private Long serialBono;
	private BigDecimal valorDescuentoBono;
	private String adventenciaBono;
	private BigDecimal dctoComercialUnitario;
	private String errorCalculoTarifa;
	private BigDecimal porcentajeDctoBono;
	private char reservaAnticipo;
	private Character selPorcentProm;
	private Character selPorcentBono;
	private String migrado;
	private Set inclusionesPresupuestos = new HashSet(0);
	private Set exclusionesPresupuestos = new HashSet(0);
	private Set presuOdoConvDetServProgs = new HashSet(0);

	public PresupuestoOdoConvenio() {
	}

	public PresupuestoOdoConvenio(long codigoPk, Contratos contratos,
			Usuarios usuarios, PresupuestoOdoProgServ presupuestoOdoProgServ,
			BigDecimal valorUnitario, Date fechaModifica, String horaModifica,
			char reservaAnticipo) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.usuarios = usuarios;
		this.presupuestoOdoProgServ = presupuestoOdoProgServ;
		this.valorUnitario = valorUnitario;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.reservaAnticipo = reservaAnticipo;
	}

	public PresupuestoOdoConvenio(long codigoPk, Contratos contratos,
			Usuarios usuarios, PresupuestoPaquetes presupuestoPaquetes,
			PresupuestoOdoProgServ presupuestoOdoProgServ, Convenios convenios,
			BigDecimal valorUnitario, Date fechaModifica, String horaModifica,
			String contratado, Integer detPromocion,
			BigDecimal porcentajeDescuentoProm, BigDecimal valorHonorarioProm,
			String advertenciaProm, BigDecimal valorDescuentoProm,
			Long serialBono, BigDecimal valorDescuentoBono,
			String adventenciaBono, BigDecimal dctoComercialUnitario,
			String errorCalculoTarifa, BigDecimal porcentajeDctoBono,
			char reservaAnticipo, Character selPorcentProm,
			Character selPorcentBono, String migrado,
			Set inclusionesPresupuestos, Set exclusionesPresupuestos,
			Set presuOdoConvDetServProgs) {
		this.codigoPk = codigoPk;
		this.contratos = contratos;
		this.usuarios = usuarios;
		this.presupuestoPaquetes = presupuestoPaquetes;
		this.presupuestoOdoProgServ = presupuestoOdoProgServ;
		this.convenios = convenios;
		this.valorUnitario = valorUnitario;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.contratado = contratado;
		this.detPromocion = detPromocion;
		this.porcentajeDescuentoProm = porcentajeDescuentoProm;
		this.valorHonorarioProm = valorHonorarioProm;
		this.advertenciaProm = advertenciaProm;
		this.valorDescuentoProm = valorDescuentoProm;
		this.serialBono = serialBono;
		this.valorDescuentoBono = valorDescuentoBono;
		this.adventenciaBono = adventenciaBono;
		this.dctoComercialUnitario = dctoComercialUnitario;
		this.errorCalculoTarifa = errorCalculoTarifa;
		this.porcentajeDctoBono = porcentajeDctoBono;
		this.reservaAnticipo = reservaAnticipo;
		this.selPorcentProm = selPorcentProm;
		this.selPorcentBono = selPorcentBono;
		this.migrado = migrado;
		this.inclusionesPresupuestos = inclusionesPresupuestos;
		this.exclusionesPresupuestos = exclusionesPresupuestos;
		this.presuOdoConvDetServProgs = presuOdoConvDetServProgs;
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

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public PresupuestoPaquetes getPresupuestoPaquetes() {
		return this.presupuestoPaquetes;
	}

	public void setPresupuestoPaquetes(PresupuestoPaquetes presupuestoPaquetes) {
		this.presupuestoPaquetes = presupuestoPaquetes;
	}

	public PresupuestoOdoProgServ getPresupuestoOdoProgServ() {
		return this.presupuestoOdoProgServ;
	}

	public void setPresupuestoOdoProgServ(
			PresupuestoOdoProgServ presupuestoOdoProgServ) {
		this.presupuestoOdoProgServ = presupuestoOdoProgServ;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public BigDecimal getValorUnitario() {
		return this.valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getContratado() {
		return this.contratado;
	}

	public void setContratado(String contratado) {
		this.contratado = contratado;
	}

	public Integer getDetPromocion() {
		return this.detPromocion;
	}

	public void setDetPromocion(Integer detPromocion) {
		this.detPromocion = detPromocion;
	}

	public BigDecimal getPorcentajeDescuentoProm() {
		return this.porcentajeDescuentoProm;
	}

	public void setPorcentajeDescuentoProm(BigDecimal porcentajeDescuentoProm) {
		this.porcentajeDescuentoProm = porcentajeDescuentoProm;
	}

	public BigDecimal getValorHonorarioProm() {
		return this.valorHonorarioProm;
	}

	public void setValorHonorarioProm(BigDecimal valorHonorarioProm) {
		this.valorHonorarioProm = valorHonorarioProm;
	}

	public String getAdvertenciaProm() {
		return this.advertenciaProm;
	}

	public void setAdvertenciaProm(String advertenciaProm) {
		this.advertenciaProm = advertenciaProm;
	}

	public BigDecimal getValorDescuentoProm() {
		return this.valorDescuentoProm;
	}

	public void setValorDescuentoProm(BigDecimal valorDescuentoProm) {
		this.valorDescuentoProm = valorDescuentoProm;
	}

	public Long getSerialBono() {
		return this.serialBono;
	}

	public void setSerialBono(Long serialBono) {
		this.serialBono = serialBono;
	}

	public BigDecimal getValorDescuentoBono() {
		return this.valorDescuentoBono;
	}

	public void setValorDescuentoBono(BigDecimal valorDescuentoBono) {
		this.valorDescuentoBono = valorDescuentoBono;
	}

	public String getAdventenciaBono() {
		return this.adventenciaBono;
	}

	public void setAdventenciaBono(String adventenciaBono) {
		this.adventenciaBono = adventenciaBono;
	}

	public BigDecimal getDctoComercialUnitario() {
		return this.dctoComercialUnitario;
	}

	public void setDctoComercialUnitario(BigDecimal dctoComercialUnitario) {
		this.dctoComercialUnitario = dctoComercialUnitario;
	}

	public String getErrorCalculoTarifa() {
		return this.errorCalculoTarifa;
	}

	public void setErrorCalculoTarifa(String errorCalculoTarifa) {
		this.errorCalculoTarifa = errorCalculoTarifa;
	}

	public BigDecimal getPorcentajeDctoBono() {
		return this.porcentajeDctoBono;
	}

	public void setPorcentajeDctoBono(BigDecimal porcentajeDctoBono) {
		this.porcentajeDctoBono = porcentajeDctoBono;
	}

	public char getReservaAnticipo() {
		return this.reservaAnticipo;
	}

	public void setReservaAnticipo(char reservaAnticipo) {
		this.reservaAnticipo = reservaAnticipo;
	}

	public Character getSelPorcentProm() {
		return this.selPorcentProm;
	}

	public void setSelPorcentProm(Character selPorcentProm) {
		this.selPorcentProm = selPorcentProm;
	}

	public Character getSelPorcentBono() {
		return this.selPorcentBono;
	}

	public void setSelPorcentBono(Character selPorcentBono) {
		this.selPorcentBono = selPorcentBono;
	}

	public String getMigrado() {
		return this.migrado;
	}

	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}

	public Set getInclusionesPresupuestos() {
		return this.inclusionesPresupuestos;
	}

	public void setInclusionesPresupuestos(Set inclusionesPresupuestos) {
		this.inclusionesPresupuestos = inclusionesPresupuestos;
	}

	public Set getExclusionesPresupuestos() {
		return this.exclusionesPresupuestos;
	}

	public void setExclusionesPresupuestos(Set exclusionesPresupuestos) {
		this.exclusionesPresupuestos = exclusionesPresupuestos;
	}

	public Set getPresuOdoConvDetServProgs() {
		return this.presuOdoConvDetServProgs;
	}

	public void setPresuOdoConvDetServProgs(Set presuOdoConvDetServProgs) {
		this.presuOdoConvDetServProgs = presuOdoConvDetServProgs;
	}

}
