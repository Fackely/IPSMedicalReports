package com.servinte.axioma.orm;

// Generated 11/06/2011 11:20:57 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Ciudades generated by hbm2java
 */
public class Ciudades implements java.io.Serializable {

	private CiudadesId id;
	private Departamentos departamentos;
	private Usuarios usuarios;
	private Paises paises;
	private String descripcion;
	private Character localidad;
	private Date fechaModifica;
	private String horaModifica;
	private Set registroAccidentesTransitosForFkRatCiu3;
	private Set registroAccidentesTransitosForFkRatCiu2;
	private Set registroAccidentesTransitosForFkRatCiu1;
	private Set usuariosCapitadoses;
	private Set institucioneses;
	private Set registroAccidentesTransitosForFkRatCiudTrans;
	private Set informeAtencionIniUrgs;
	private Set empresasesForFkEmpCiuprin;
	private Set personasesForFkPCiudadid;
	private Set centroAtencions;
	private Set movimientosChequesesForFkCiudaMcPlazaCiudades;
	private Set movimientosTarjetases;
	private Set registroAccidentesTransitosForFkRatCiudExpIdTrans;
	private Set registroAccidentesTransitosForFkRatCiu6;
	private Set registroAccidentesTransitosForFkRatCiu5;
	private Set movimientosChequesesForFkCiudaMcGiradorCiudades;
	private Set registroAccidentesTransitosForFkRatCiudProp;
	private Set empresasInstitucions;
	private Set responsablesPacientesesForFkRespPacCiudad;
	private Set localidadeses;
	private Set registroEventoCatastroficosForFkRevCiu6;
	private Set detPromocionesOdos;
	private Set registroEventoCatastroficosForFkRevCiu7;
	private Set empresasesForFkEmpCiuc;
	private Set personasesForFkPCiudadviv;
	private Set barrioses;
	private Set personasesForFkPCiudadnac;
	private Set registroEventoCatastroficosForFkRevCiu1;
	private Set registroEventoCatastroficosForFkRevCiu2;
	private Set medicoses;
	private Set logDetPromocionesOdos;
	private Set registroAccidentesTransitosForFkRatCiudExpIdProp;
	private Set responsablesPacientesesForFkRpCiudadDoc;

	public Ciudades() {
	}

	public Ciudades(CiudadesId id, Departamentos departamentos,
			Usuarios usuarios, Paises paises, String descripcion,
			Date fechaModifica, String horaModifica) {
		this.id = id;
		this.departamentos = departamentos;
		this.usuarios = usuarios;
		this.paises = paises;
		this.descripcion = descripcion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public Ciudades(CiudadesId id, Departamentos departamentos,
			Usuarios usuarios, Paises paises, String descripcion,
			Character localidad, Date fechaModifica, String horaModifica,
			Set registroAccidentesTransitosForFkRatCiu3,
			Set registroAccidentesTransitosForFkRatCiu2,
			Set registroAccidentesTransitosForFkRatCiu1,
			Set usuariosCapitadoses, Set institucioneses,
			Set registroAccidentesTransitosForFkRatCiudTrans,
			Set informeAtencionIniUrgs, Set empresasesForFkEmpCiuprin,
			Set personasesForFkPCiudadid, Set centroAtencions,
			Set movimientosChequesesForFkCiudaMcPlazaCiudades,
			Set movimientosTarjetases,
			Set registroAccidentesTransitosForFkRatCiudExpIdTrans,
			Set registroAccidentesTransitosForFkRatCiu6,
			Set registroAccidentesTransitosForFkRatCiu5,
			Set movimientosChequesesForFkCiudaMcGiradorCiudades,
			Set registroAccidentesTransitosForFkRatCiudProp,
			Set empresasInstitucions,
			Set responsablesPacientesesForFkRespPacCiudad, Set localidadeses,
			Set registroEventoCatastroficosForFkRevCiu6,
			Set detPromocionesOdos,
			Set registroEventoCatastroficosForFkRevCiu7,
			Set empresasesForFkEmpCiuc, Set personasesForFkPCiudadviv,
			Set barrioses, Set personasesForFkPCiudadnac,
			Set registroEventoCatastroficosForFkRevCiu1,
			Set registroEventoCatastroficosForFkRevCiu2, Set medicoses,
			Set logDetPromocionesOdos,
			Set registroAccidentesTransitosForFkRatCiudExpIdProp,
			Set responsablesPacientesesForFkRpCiudadDoc) {
		this.id = id;
		this.departamentos = departamentos;
		this.usuarios = usuarios;
		this.paises = paises;
		this.descripcion = descripcion;
		this.localidad = localidad;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.registroAccidentesTransitosForFkRatCiu3 = registroAccidentesTransitosForFkRatCiu3;
		this.registroAccidentesTransitosForFkRatCiu2 = registroAccidentesTransitosForFkRatCiu2;
		this.registroAccidentesTransitosForFkRatCiu1 = registroAccidentesTransitosForFkRatCiu1;
		this.usuariosCapitadoses = usuariosCapitadoses;
		this.institucioneses = institucioneses;
		this.registroAccidentesTransitosForFkRatCiudTrans = registroAccidentesTransitosForFkRatCiudTrans;
		this.informeAtencionIniUrgs = informeAtencionIniUrgs;
		this.empresasesForFkEmpCiuprin = empresasesForFkEmpCiuprin;
		this.personasesForFkPCiudadid = personasesForFkPCiudadid;
		this.centroAtencions = centroAtencions;
		this.movimientosChequesesForFkCiudaMcPlazaCiudades = movimientosChequesesForFkCiudaMcPlazaCiudades;
		this.movimientosTarjetases = movimientosTarjetases;
		this.registroAccidentesTransitosForFkRatCiudExpIdTrans = registroAccidentesTransitosForFkRatCiudExpIdTrans;
		this.registroAccidentesTransitosForFkRatCiu6 = registroAccidentesTransitosForFkRatCiu6;
		this.registroAccidentesTransitosForFkRatCiu5 = registroAccidentesTransitosForFkRatCiu5;
		this.movimientosChequesesForFkCiudaMcGiradorCiudades = movimientosChequesesForFkCiudaMcGiradorCiudades;
		this.registroAccidentesTransitosForFkRatCiudProp = registroAccidentesTransitosForFkRatCiudProp;
		this.empresasInstitucions = empresasInstitucions;
		this.responsablesPacientesesForFkRespPacCiudad = responsablesPacientesesForFkRespPacCiudad;
		this.localidadeses = localidadeses;
		this.registroEventoCatastroficosForFkRevCiu6 = registroEventoCatastroficosForFkRevCiu6;
		this.detPromocionesOdos = detPromocionesOdos;
		this.registroEventoCatastroficosForFkRevCiu7 = registroEventoCatastroficosForFkRevCiu7;
		this.empresasesForFkEmpCiuc = empresasesForFkEmpCiuc;
		this.personasesForFkPCiudadviv = personasesForFkPCiudadviv;
		this.barrioses = barrioses;
		this.personasesForFkPCiudadnac = personasesForFkPCiudadnac;
		this.registroEventoCatastroficosForFkRevCiu1 = registroEventoCatastroficosForFkRevCiu1;
		this.registroEventoCatastroficosForFkRevCiu2 = registroEventoCatastroficosForFkRevCiu2;
		this.medicoses = medicoses;
		this.logDetPromocionesOdos = logDetPromocionesOdos;
		this.registroAccidentesTransitosForFkRatCiudExpIdProp = registroAccidentesTransitosForFkRatCiudExpIdProp;
		this.responsablesPacientesesForFkRpCiudadDoc = responsablesPacientesesForFkRpCiudadDoc;
	}

	public CiudadesId getId() {
		return id;
	}

	public void setId(CiudadesId id) {
		this.id = id;
	}

	public Departamentos getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Departamentos departamentos) {
		this.departamentos = departamentos;
	}

	public Usuarios getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Paises getPaises() {
		return paises;
	}

	public void setPaises(Paises paises) {
		this.paises = paises;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Character getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Character localidad) {
		this.localidad = localidad;
	}

	public Date getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiu3() {
		if(this.registroAccidentesTransitosForFkRatCiu3 == null)
		{
			this.registroAccidentesTransitosForFkRatCiu3 = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiu3;
	}

	public void setRegistroAccidentesTransitosForFkRatCiu3(
			Set registroAccidentesTransitosForFkRatCiu3) {
		this.registroAccidentesTransitosForFkRatCiu3 = registroAccidentesTransitosForFkRatCiu3;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiu2() {
		if(this.registroAccidentesTransitosForFkRatCiu2 == null)
		{
			this.registroAccidentesTransitosForFkRatCiu2 = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiu2;
	}

	public void setRegistroAccidentesTransitosForFkRatCiu2(
			Set registroAccidentesTransitosForFkRatCiu2) {
		this.registroAccidentesTransitosForFkRatCiu2 = registroAccidentesTransitosForFkRatCiu2;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiu1() {
		if(this.registroAccidentesTransitosForFkRatCiu1 == null)
		{
			this.registroAccidentesTransitosForFkRatCiu1 = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiu1;
	}

	public void setRegistroAccidentesTransitosForFkRatCiu1(
			Set registroAccidentesTransitosForFkRatCiu1) {
		this.registroAccidentesTransitosForFkRatCiu1 = registroAccidentesTransitosForFkRatCiu1;
	}

	public Set getUsuariosCapitadoses() {
		if(this.usuariosCapitadoses == null)
		{
			this.usuariosCapitadoses = new HashSet();
		}
		return usuariosCapitadoses;
	}

	public void setUsuariosCapitadoses(Set usuariosCapitadoses) {
		this.usuariosCapitadoses = usuariosCapitadoses;
	}

	public Set getInstitucioneses() {
		if(this.institucioneses == null)
		{
			this.institucioneses = new HashSet();
		}
		return institucioneses;
	}

	public void setInstitucioneses(Set institucioneses) {
		this.institucioneses = institucioneses;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiudTrans() {
		if(this.registroAccidentesTransitosForFkRatCiudTrans == null)
		{
			this.registroAccidentesTransitosForFkRatCiudTrans = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiudTrans;
	}

	public void setRegistroAccidentesTransitosForFkRatCiudTrans(
			Set registroAccidentesTransitosForFkRatCiudTrans) {
		this.registroAccidentesTransitosForFkRatCiudTrans = registroAccidentesTransitosForFkRatCiudTrans;
	}

	public Set getInformeAtencionIniUrgs() {
		if(this.informeAtencionIniUrgs == null)
		{
			this.informeAtencionIniUrgs = new HashSet();
		}
		return informeAtencionIniUrgs;
	}

	public void setInformeAtencionIniUrgs(Set informeAtencionIniUrgs) {
		this.informeAtencionIniUrgs = informeAtencionIniUrgs;
	}

	public Set getEmpresasesForFkEmpCiuprin() {
		if(this.empresasesForFkEmpCiuprin == null)
		{
			this.empresasesForFkEmpCiuprin = new HashSet();
		}
		return empresasesForFkEmpCiuprin;
	}

	public void setEmpresasesForFkEmpCiuprin(Set empresasesForFkEmpCiuprin) {
		this.empresasesForFkEmpCiuprin = empresasesForFkEmpCiuprin;
	}

	public Set getPersonasesForFkPCiudadid() {
		if(this.personasesForFkPCiudadid == null)
		{
			this.personasesForFkPCiudadid = new HashSet();
		}
		return personasesForFkPCiudadid;
	}

	public void setPersonasesForFkPCiudadid(Set personasesForFkPCiudadid) {
		this.personasesForFkPCiudadid = personasesForFkPCiudadid;
	}

	public Set getCentroAtencions() {
		if(this.centroAtencions == null)
		{
			this.centroAtencions = new HashSet();
		}
		return centroAtencions;
	}

	public void setCentroAtencions(Set centroAtencions) {
		this.centroAtencions = centroAtencions;
	}

	public Set getMovimientosChequesesForFkCiudaMcPlazaCiudades() {
		if(this.movimientosChequesesForFkCiudaMcPlazaCiudades == null)
		{
			this.movimientosChequesesForFkCiudaMcPlazaCiudades = new HashSet();
		}
		return movimientosChequesesForFkCiudaMcPlazaCiudades;
	}

	public void setMovimientosChequesesForFkCiudaMcPlazaCiudades(
			Set movimientosChequesesForFkCiudaMcPlazaCiudades) {
		this.movimientosChequesesForFkCiudaMcPlazaCiudades = movimientosChequesesForFkCiudaMcPlazaCiudades;
	}

	public Set getMovimientosTarjetases() {
		if(this.movimientosTarjetases == null)
		{
			this.movimientosTarjetases = new HashSet();
		}
		return movimientosTarjetases;
	}

	public void setMovimientosTarjetases(Set movimientosTarjetases) {
		this.movimientosTarjetases = movimientosTarjetases;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiudExpIdTrans() {
		if(this.registroAccidentesTransitosForFkRatCiudExpIdTrans == null)
		{
			this.registroAccidentesTransitosForFkRatCiudExpIdTrans = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiudExpIdTrans;
	}

	public void setRegistroAccidentesTransitosForFkRatCiudExpIdTrans(
			Set registroAccidentesTransitosForFkRatCiudExpIdTrans) {
		this.registroAccidentesTransitosForFkRatCiudExpIdTrans = registroAccidentesTransitosForFkRatCiudExpIdTrans;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiu6() {
		if(this.registroAccidentesTransitosForFkRatCiu6 == null)
		{
			this.registroAccidentesTransitosForFkRatCiu6 = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiu6;
	}

	public void setRegistroAccidentesTransitosForFkRatCiu6(
			Set registroAccidentesTransitosForFkRatCiu6) {
		this.registroAccidentesTransitosForFkRatCiu6 = registroAccidentesTransitosForFkRatCiu6;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiu5() {
		if(this.registroAccidentesTransitosForFkRatCiu5 == null)
		{
			this.registroAccidentesTransitosForFkRatCiu5 = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiu5;
	}

	public void setRegistroAccidentesTransitosForFkRatCiu5(
			Set registroAccidentesTransitosForFkRatCiu5) {
		this.registroAccidentesTransitosForFkRatCiu5 = registroAccidentesTransitosForFkRatCiu5;
	}

	public Set getMovimientosChequesesForFkCiudaMcGiradorCiudades() {
		if(this.movimientosChequesesForFkCiudaMcGiradorCiudades == null)
		{
			this.movimientosChequesesForFkCiudaMcGiradorCiudades = new HashSet();
		}
		return movimientosChequesesForFkCiudaMcGiradorCiudades;
	}

	public void setMovimientosChequesesForFkCiudaMcGiradorCiudades(
			Set movimientosChequesesForFkCiudaMcGiradorCiudades) {
		this.movimientosChequesesForFkCiudaMcGiradorCiudades = movimientosChequesesForFkCiudaMcGiradorCiudades;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiudProp() {
		if(this.registroAccidentesTransitosForFkRatCiudProp == null)
		{
			this.registroAccidentesTransitosForFkRatCiudProp = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiudProp;
	}

	public void setRegistroAccidentesTransitosForFkRatCiudProp(
			Set registroAccidentesTransitosForFkRatCiudProp) {
		this.registroAccidentesTransitosForFkRatCiudProp = registroAccidentesTransitosForFkRatCiudProp;
	}

	public Set getEmpresasInstitucions() {
		if(this.empresasInstitucions == null)
		{
			this.empresasInstitucions = new HashSet();
		}
		return empresasInstitucions;
	}

	public void setEmpresasInstitucions(Set empresasInstitucions) {
		this.empresasInstitucions = empresasInstitucions;
	}

	public Set getResponsablesPacientesesForFkRespPacCiudad() {
		if(this.responsablesPacientesesForFkRespPacCiudad == null)
		{
			this.responsablesPacientesesForFkRespPacCiudad = new HashSet();
		}
		return responsablesPacientesesForFkRespPacCiudad;
	}

	public void setResponsablesPacientesesForFkRespPacCiudad(
			Set responsablesPacientesesForFkRespPacCiudad) {
		this.responsablesPacientesesForFkRespPacCiudad = responsablesPacientesesForFkRespPacCiudad;
	}

	public Set getLocalidadeses() {
		if(this.localidadeses == null)
		{
			this.localidadeses = new HashSet();
		}
		return localidadeses;
	}

	public void setLocalidadeses(Set localidadeses) {
		this.localidadeses = localidadeses;
	}

	public Set getRegistroEventoCatastroficosForFkRevCiu6() {
		if(this.registroEventoCatastroficosForFkRevCiu6 == null)
		{
			this.registroEventoCatastroficosForFkRevCiu6 = new HashSet();
		}
		return registroEventoCatastroficosForFkRevCiu6;
	}

	public void setRegistroEventoCatastroficosForFkRevCiu6(
			Set registroEventoCatastroficosForFkRevCiu6) {
		this.registroEventoCatastroficosForFkRevCiu6 = registroEventoCatastroficosForFkRevCiu6;
	}

	public Set getDetPromocionesOdos() {
		if(this.detPromocionesOdos == null)
		{
			this.detPromocionesOdos = new HashSet();
		}
		return detPromocionesOdos;
	}

	public void setDetPromocionesOdos(Set detPromocionesOdos) {
		this.detPromocionesOdos = detPromocionesOdos;
	}

	public Set getRegistroEventoCatastroficosForFkRevCiu7() {
		if(this.registroEventoCatastroficosForFkRevCiu7 == null)
		{
			this.registroEventoCatastroficosForFkRevCiu7 = new HashSet();
		}
		return registroEventoCatastroficosForFkRevCiu7;
	}

	public void setRegistroEventoCatastroficosForFkRevCiu7(
			Set registroEventoCatastroficosForFkRevCiu7) {
		this.registroEventoCatastroficosForFkRevCiu7 = registroEventoCatastroficosForFkRevCiu7;
	}

	public Set getEmpresasesForFkEmpCiuc() {
		if(this.empresasesForFkEmpCiuc == null)
		{
			this.empresasesForFkEmpCiuc = new HashSet();
		}
		return empresasesForFkEmpCiuc;
	}

	public void setEmpresasesForFkEmpCiuc(Set empresasesForFkEmpCiuc) {
		this.empresasesForFkEmpCiuc = empresasesForFkEmpCiuc;
	}

	public Set getPersonasesForFkPCiudadviv() {
		if(this.personasesForFkPCiudadviv == null)
		{
			this.personasesForFkPCiudadviv = new HashSet();
		}
		return personasesForFkPCiudadviv;
	}

	public void setPersonasesForFkPCiudadviv(Set personasesForFkPCiudadviv) {
		this.personasesForFkPCiudadviv = personasesForFkPCiudadviv;
	}

	public Set getBarrioses() {
		if(this.barrioses == null)
		{
			this.barrioses = new HashSet();
		}
		return barrioses;
	}

	public void setBarrioses(Set barrioses) {
		this.barrioses = barrioses;
	}

	public Set getPersonasesForFkPCiudadnac() {
		if(this.personasesForFkPCiudadnac == null)
		{
			this.personasesForFkPCiudadnac = new HashSet();
		}
		return personasesForFkPCiudadnac;
	}

	public void setPersonasesForFkPCiudadnac(Set personasesForFkPCiudadnac) {
		this.personasesForFkPCiudadnac = personasesForFkPCiudadnac;
	}

	public Set getRegistroEventoCatastroficosForFkRevCiu1() {
		if(this.registroEventoCatastroficosForFkRevCiu1 == null)
		{
			this.registroEventoCatastroficosForFkRevCiu1 = new HashSet();
		}
		return registroEventoCatastroficosForFkRevCiu1;
	}

	public void setRegistroEventoCatastroficosForFkRevCiu1(
			Set registroEventoCatastroficosForFkRevCiu1) {
		this.registroEventoCatastroficosForFkRevCiu1 = registroEventoCatastroficosForFkRevCiu1;
	}

	public Set getRegistroEventoCatastroficosForFkRevCiu2() {
		if(this.registroEventoCatastroficosForFkRevCiu2 == null)
		{
			this.registroEventoCatastroficosForFkRevCiu2 = new HashSet();
		}
		return registroEventoCatastroficosForFkRevCiu2;
	}

	public void setRegistroEventoCatastroficosForFkRevCiu2(
			Set registroEventoCatastroficosForFkRevCiu2) {
		this.registroEventoCatastroficosForFkRevCiu2 = registroEventoCatastroficosForFkRevCiu2;
	}

	public Set getMedicoses() {
		if(this.medicoses == null)
		{
			this.medicoses = new HashSet();
		}
		return medicoses;
	}

	public void setMedicoses(Set medicoses) {
		this.medicoses = medicoses;
	}

	public Set getLogDetPromocionesOdos() {
		if(this.logDetPromocionesOdos == null)
		{
			this.logDetPromocionesOdos = new HashSet();
		}
		return logDetPromocionesOdos;
	}

	public void setLogDetPromocionesOdos(Set logDetPromocionesOdos) {
		this.logDetPromocionesOdos = logDetPromocionesOdos;
	}

	public Set getRegistroAccidentesTransitosForFkRatCiudExpIdProp() {
		if(this.registroAccidentesTransitosForFkRatCiudExpIdProp == null)
		{
			this.registroAccidentesTransitosForFkRatCiudExpIdProp = new HashSet();
		}
		return registroAccidentesTransitosForFkRatCiudExpIdProp;
	}

	public void setRegistroAccidentesTransitosForFkRatCiudExpIdProp(
			Set registroAccidentesTransitosForFkRatCiudExpIdProp) {
		this.registroAccidentesTransitosForFkRatCiudExpIdProp = registroAccidentesTransitosForFkRatCiudExpIdProp;
	}

	public Set getResponsablesPacientesesForFkRpCiudadDoc() {
		if(this.responsablesPacientesesForFkRpCiudadDoc == null)
		{
			this.responsablesPacientesesForFkRpCiudadDoc = new HashSet();
		}
		return responsablesPacientesesForFkRpCiudadDoc;
	}

	public void setResponsablesPacientesesForFkRpCiudadDoc(
			Set responsablesPacientesesForFkRpCiudadDoc) {
		this.responsablesPacientesesForFkRpCiudadDoc = responsablesPacientesesForFkRpCiudadDoc;
	}
}