package com.servinte.axioma.orm;

// Generated 4/05/2011 11:22:17 AM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ClaseInventario generated by hbm2java
 */
public class ClaseInventario implements java.io.Serializable {

	private int codigo;
	private CuentasContables cuentasContablesByCuentaInventario;
	private Instituciones instituciones;
	private RubroPresupuestal rubroPresupuestal;
	private CuentasContables cuentasContablesByCuentaCosto;
	private String nombre;
	private String codigoInterfaz;
	private Set esqTarInventariosContratos = new HashSet(0);
	private Set detVigConRetClases = new HashSet(0);
	private Set tarifasInvConEntSubs = new HashSet(0);
	private Set agruArtExceTariConts = new HashSet(0);
	private Set agrupArtExepCobConvxconts = new HashSet(0);
	private Set agrupArtDescComConvconts = new HashSet(0);
	private Set parametricaValorEstandars = new HashSet(0);
	private Set cobAgrupArticuloses = new HashSet(0);
	private Set detalleValorizacionArts = new HashSet(0);
	private Set grupoInventarios = new HashSet(0);
	private Set exCoberAgruArtEntSubs = new HashSet(0);
	private Set logEsqTarInvContratos = new HashSet(0);
	private Set esqTarInvtSubCuentases = new HashSet(0);
	private Set tiposRetencionClaseInvs = new HashSet(0);
	private Set agruArtIncluexcluCcs = new HashSet(0);
	private Set agruArtIncluexcluEconts = new HashSet(0);
	private Set paqAgrupacionArticuloses = new HashSet(0);
	private Set cierreTempNivAteClInvArts = new HashSet(0);
	private Set cierreTempClaseInvArts = new HashSet(0);

	public ClaseInventario() {
	}

	public ClaseInventario(int codigo, Instituciones instituciones,
			String nombre) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.nombre = nombre;
	}

	public ClaseInventario(int codigo,
			CuentasContables cuentasContablesByCuentaInventario,
			Instituciones instituciones, RubroPresupuestal rubroPresupuestal,
			CuentasContables cuentasContablesByCuentaCosto, String nombre,
			String codigoInterfaz, Set esqTarInventariosContratos,
			Set detVigConRetClases, Set tarifasInvConEntSubs,
			Set parametricaValorEstandars,
			Set agruArtExceTariConts, Set agrupArtExepCobConvxconts,
			Set agrupArtDescComConvconts, Set cobAgrupArticuloses,
			Set detalleValorizacionArts, Set grupoInventarios,
			Set exCoberAgruArtEntSubs, Set logEsqTarInvContratos,
			Set esqTarInvtSubCuentases, Set tiposRetencionClaseInvs,
			Set agruArtIncluexcluCcs, Set agruArtIncluexcluEconts,
			Set paqAgrupacionArticuloses, Set cierreTempNivAteClInvArts,
			Set cierreTempClaseInvArts) {
		this.codigo = codigo;
		this.cuentasContablesByCuentaInventario = cuentasContablesByCuentaInventario;
		this.instituciones = instituciones;
		this.rubroPresupuestal = rubroPresupuestal;
		this.cuentasContablesByCuentaCosto = cuentasContablesByCuentaCosto;
		this.nombre = nombre;
		this.codigoInterfaz = codigoInterfaz;
		this.esqTarInventariosContratos = esqTarInventariosContratos;
		this.detVigConRetClases = detVigConRetClases;
		this.tarifasInvConEntSubs = tarifasInvConEntSubs;
		this.agruArtExceTariConts = agruArtExceTariConts;
		this.agrupArtExepCobConvxconts = agrupArtExepCobConvxconts;
		this.agrupArtDescComConvconts = agrupArtDescComConvconts;
		this.parametricaValorEstandars = parametricaValorEstandars;
		this.cobAgrupArticuloses = cobAgrupArticuloses;
		this.detalleValorizacionArts = detalleValorizacionArts;
		this.grupoInventarios = grupoInventarios;
		this.exCoberAgruArtEntSubs = exCoberAgruArtEntSubs;
		this.logEsqTarInvContratos = logEsqTarInvContratos;
		this.esqTarInvtSubCuentases = esqTarInvtSubCuentases;
		this.tiposRetencionClaseInvs = tiposRetencionClaseInvs;
		this.agruArtIncluexcluCcs = agruArtIncluexcluCcs;
		this.agruArtIncluexcluEconts = agruArtIncluexcluEconts;
		this.paqAgrupacionArticuloses = paqAgrupacionArticuloses;
		this.cierreTempNivAteClInvArts = cierreTempNivAteClInvArts;
		this.cierreTempClaseInvArts = cierreTempClaseInvArts;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public CuentasContables getCuentasContablesByCuentaInventario() {
		return this.cuentasContablesByCuentaInventario;
	}

	public void setCuentasContablesByCuentaInventario(
			CuentasContables cuentasContablesByCuentaInventario) {
		this.cuentasContablesByCuentaInventario = cuentasContablesByCuentaInventario;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public RubroPresupuestal getRubroPresupuestal() {
		return this.rubroPresupuestal;
	}

	public void setRubroPresupuestal(RubroPresupuestal rubroPresupuestal) {
		this.rubroPresupuestal = rubroPresupuestal;
	}

	public CuentasContables getCuentasContablesByCuentaCosto() {
		return this.cuentasContablesByCuentaCosto;
	}

	public void setCuentasContablesByCuentaCosto(
			CuentasContables cuentasContablesByCuentaCosto) {
		this.cuentasContablesByCuentaCosto = cuentasContablesByCuentaCosto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigoInterfaz() {
		return this.codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public Set getEsqTarInventariosContratos() {
		return this.esqTarInventariosContratos;
	}

	public void setEsqTarInventariosContratos(Set esqTarInventariosContratos) {
		this.esqTarInventariosContratos = esqTarInventariosContratos;
	}

	public Set getDetVigConRetClases() {
		return this.detVigConRetClases;
	}

	public void setDetVigConRetClases(Set detVigConRetClases) {
		this.detVigConRetClases = detVigConRetClases;
	}

	public Set getTarifasInvConEntSubs() {
		return this.tarifasInvConEntSubs;
	}

	public void setTarifasInvConEntSubs(Set tarifasInvConEntSubs) {
		this.tarifasInvConEntSubs = tarifasInvConEntSubs;
	}

	public Set getAgruArtExceTariConts() {
		return this.agruArtExceTariConts;
	}

	public void setAgruArtExceTariConts(Set agruArtExceTariConts) {
		this.agruArtExceTariConts = agruArtExceTariConts;
	}

	public Set getAgrupArtExepCobConvxconts() {
		return this.agrupArtExepCobConvxconts;
	}

	public void setAgrupArtExepCobConvxconts(Set agrupArtExepCobConvxconts) {
		this.agrupArtExepCobConvxconts = agrupArtExepCobConvxconts;
	}

	public Set getAgrupArtDescComConvconts() {
		return this.agrupArtDescComConvconts;
	}

	public void setAgrupArtDescComConvconts(Set agrupArtDescComConvconts) {
		this.agrupArtDescComConvconts = agrupArtDescComConvconts;
	}

	public Set getParametricaValorEstandars() {
		return this.parametricaValorEstandars;
	}

	public void setParametricaValorEstandars(Set parametricaValorEstandars) {
		this.parametricaValorEstandars = parametricaValorEstandars;
	}


	public Set getCobAgrupArticuloses() {
		return this.cobAgrupArticuloses;
	}

	public void setCobAgrupArticuloses(Set cobAgrupArticuloses) {
		this.cobAgrupArticuloses = cobAgrupArticuloses;
	}

	public Set getDetalleValorizacionArts() {
		return this.detalleValorizacionArts;
	}

	public void setDetalleValorizacionArts(Set detalleValorizacionArts) {
		this.detalleValorizacionArts = detalleValorizacionArts;
	}

	public Set getGrupoInventarios() {
		return this.grupoInventarios;
	}

	public void setGrupoInventarios(Set grupoInventarios) {
		this.grupoInventarios = grupoInventarios;
	}

	public Set getExCoberAgruArtEntSubs() {
		return this.exCoberAgruArtEntSubs;
	}

	public void setExCoberAgruArtEntSubs(Set exCoberAgruArtEntSubs) {
		this.exCoberAgruArtEntSubs = exCoberAgruArtEntSubs;
	}

	public Set getLogEsqTarInvContratos() {
		return this.logEsqTarInvContratos;
	}

	public void setLogEsqTarInvContratos(Set logEsqTarInvContratos) {
		this.logEsqTarInvContratos = logEsqTarInvContratos;
	}

	public Set getEsqTarInvtSubCuentases() {
		return this.esqTarInvtSubCuentases;
	}

	public void setEsqTarInvtSubCuentases(Set esqTarInvtSubCuentases) {
		this.esqTarInvtSubCuentases = esqTarInvtSubCuentases;
	}

	public Set getTiposRetencionClaseInvs() {
		return this.tiposRetencionClaseInvs;
	}

	public void setTiposRetencionClaseInvs(Set tiposRetencionClaseInvs) {
		this.tiposRetencionClaseInvs = tiposRetencionClaseInvs;
	}

	public Set getAgruArtIncluexcluCcs() {
		return this.agruArtIncluexcluCcs;
	}

	public void setAgruArtIncluexcluCcs(Set agruArtIncluexcluCcs) {
		this.agruArtIncluexcluCcs = agruArtIncluexcluCcs;
	}

	public Set getAgruArtIncluexcluEconts() {
		return this.agruArtIncluexcluEconts;
	}

	public void setAgruArtIncluexcluEconts(Set agruArtIncluexcluEconts) {
		this.agruArtIncluexcluEconts = agruArtIncluexcluEconts;
	}

	public Set getPaqAgrupacionArticuloses() {
		return this.paqAgrupacionArticuloses;
	}

	public void setPaqAgrupacionArticuloses(Set paqAgrupacionArticuloses) {
		this.paqAgrupacionArticuloses = paqAgrupacionArticuloses;
	}

	/**
	 * @return the cierreTempNivAteClInvArts
	 */
	public Set getCierreTempNivAteClInvArts() {
		return cierreTempNivAteClInvArts;
	}

	/**
	 * @param cierreTempNivAteClInvArts the cierreTempNivAteClInvArts to set
	 */
	public void setCierreTempNivAteClInvArts(Set cierreTempNivAteClInvArts) {
		this.cierreTempNivAteClInvArts = cierreTempNivAteClInvArts;
	}

	/**
	 * @return the cierreTempClaseInvArts
	 */
	public Set getCierreTempClaseInvArts() {
		return cierreTempClaseInvArts;
	}

	/**
	 * @param cierreTempClaseInvArts the cierreTempClaseInvArts to set
	 */
	public void setCierreTempClaseInvArts(Set cierreTempClaseInvArts) {
		this.cierreTempClaseInvArts = cierreTempClaseInvArts;
	}

}
