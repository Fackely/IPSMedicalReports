package com.servinte.axioma.orm;

// Generated Sep 10, 2010 6:18:10 PM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * GrupoInventario generated by hbm2java
 */
public class GrupoInventario implements java.io.Serializable {

	private GrupoInventarioId id;
	private ClaseInventario claseInventario;
	private CuentasContables cuentasContablesByCuentaInventario;
	private Instituciones instituciones;
	private RubroPresupuestal rubroPresupuestal;
	private CuentasContables cuentasContablesByCuentaCosto;
	private String nombre;
	private String aplicaCargosDirectos;
	private Set exCoberAgruArtEntSubs = new HashSet(0);
	private Set agrupArtDescComConvconts = new HashSet(0);
	private Set agruArtIncluexcluEconts = new HashSet(0);
	private Set agrupArtExepCobConvxconts = new HashSet(0);
	private Set subgrupoInventarios = new HashSet(0);
	private Set paqAgrupacionArticuloses = new HashSet(0);
	private Set agruArtIncluexcluCcs = new HashSet(0);
	private Set agruArtExceTariConts = new HashSet(0);
	private Set cobAgrupArticuloses = new HashSet(0);

	public GrupoInventario() {
	}

	public GrupoInventario(GrupoInventarioId id,
			ClaseInventario claseInventario, Instituciones instituciones,
			String nombre) {
		this.id = id;
		this.claseInventario = claseInventario;
		this.instituciones = instituciones;
		this.nombre = nombre;
	}

	public GrupoInventario(GrupoInventarioId id,
			ClaseInventario claseInventario,
			CuentasContables cuentasContablesByCuentaInventario,
			Instituciones instituciones, RubroPresupuestal rubroPresupuestal,
			CuentasContables cuentasContablesByCuentaCosto, String nombre,
			String aplicaCargosDirectos, Set exCoberAgruArtEntSubs,
			Set agrupArtDescComConvconts, Set agruArtIncluexcluEconts,
			Set agrupArtExepCobConvxconts, Set subgrupoInventarios,
			Set paqAgrupacionArticuloses, Set agruArtIncluexcluCcs,
			Set agruArtExceTariConts, Set cobAgrupArticuloses) {
		this.id = id;
		this.claseInventario = claseInventario;
		this.cuentasContablesByCuentaInventario = cuentasContablesByCuentaInventario;
		this.instituciones = instituciones;
		this.rubroPresupuestal = rubroPresupuestal;
		this.cuentasContablesByCuentaCosto = cuentasContablesByCuentaCosto;
		this.nombre = nombre;
		this.aplicaCargosDirectos = aplicaCargosDirectos;
		this.exCoberAgruArtEntSubs = exCoberAgruArtEntSubs;
		this.agrupArtDescComConvconts = agrupArtDescComConvconts;
		this.agruArtIncluexcluEconts = agruArtIncluexcluEconts;
		this.agrupArtExepCobConvxconts = agrupArtExepCobConvxconts;
		this.subgrupoInventarios = subgrupoInventarios;
		this.paqAgrupacionArticuloses = paqAgrupacionArticuloses;
		this.agruArtIncluexcluCcs = agruArtIncluexcluCcs;
		this.agruArtExceTariConts = agruArtExceTariConts;
		this.cobAgrupArticuloses = cobAgrupArticuloses;
	}

	public GrupoInventarioId getId() {
		return this.id;
	}

	public void setId(GrupoInventarioId id) {
		this.id = id;
	}

	public ClaseInventario getClaseInventario() {
		return this.claseInventario;
	}

	public void setClaseInventario(ClaseInventario claseInventario) {
		this.claseInventario = claseInventario;
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

	public String getAplicaCargosDirectos() {
		return this.aplicaCargosDirectos;
	}

	public void setAplicaCargosDirectos(String aplicaCargosDirectos) {
		this.aplicaCargosDirectos = aplicaCargosDirectos;
	}

	public Set getExCoberAgruArtEntSubs() {
		return this.exCoberAgruArtEntSubs;
	}

	public void setExCoberAgruArtEntSubs(Set exCoberAgruArtEntSubs) {
		this.exCoberAgruArtEntSubs = exCoberAgruArtEntSubs;
	}

	public Set getAgrupArtDescComConvconts() {
		return this.agrupArtDescComConvconts;
	}

	public void setAgrupArtDescComConvconts(Set agrupArtDescComConvconts) {
		this.agrupArtDescComConvconts = agrupArtDescComConvconts;
	}

	public Set getAgruArtIncluexcluEconts() {
		return this.agruArtIncluexcluEconts;
	}

	public void setAgruArtIncluexcluEconts(Set agruArtIncluexcluEconts) {
		this.agruArtIncluexcluEconts = agruArtIncluexcluEconts;
	}

	public Set getAgrupArtExepCobConvxconts() {
		return this.agrupArtExepCobConvxconts;
	}

	public void setAgrupArtExepCobConvxconts(Set agrupArtExepCobConvxconts) {
		this.agrupArtExepCobConvxconts = agrupArtExepCobConvxconts;
	}

	public Set getSubgrupoInventarios() {
		return this.subgrupoInventarios;
	}

	public void setSubgrupoInventarios(Set subgrupoInventarios) {
		this.subgrupoInventarios = subgrupoInventarios;
	}

	public Set getPaqAgrupacionArticuloses() {
		return this.paqAgrupacionArticuloses;
	}

	public void setPaqAgrupacionArticuloses(Set paqAgrupacionArticuloses) {
		this.paqAgrupacionArticuloses = paqAgrupacionArticuloses;
	}

	public Set getAgruArtIncluexcluCcs() {
		return this.agruArtIncluexcluCcs;
	}

	public void setAgruArtIncluexcluCcs(Set agruArtIncluexcluCcs) {
		this.agruArtIncluexcluCcs = agruArtIncluexcluCcs;
	}

	public Set getAgruArtExceTariConts() {
		return this.agruArtExceTariConts;
	}

	public void setAgruArtExceTariConts(Set agruArtExceTariConts) {
		this.agruArtExceTariConts = agruArtExceTariConts;
	}

	public Set getCobAgrupArticuloses() {
		return this.cobAgrupArticuloses;
	}

	public void setCobAgrupArticuloses(Set cobAgrupArticuloses) {
		this.cobAgrupArticuloses = cobAgrupArticuloses;
	}

}
