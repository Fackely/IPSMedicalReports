package com.servinte.axioma.orm;

// Generated 15/06/2011 05:09:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Personas generated by hbm2java
 */
public class Personas implements java.io.Serializable {

	private int codigo;
	private Ciudades ciudadesByFkPCiudadid;
	private Sexo sexo;
	private Ciudades ciudadesByFkPCiudadviv;
	private TiposPersonas tiposPersonas;
	private Ciudades ciudadesByFkPCiudadnac;
	private Barrios barrios;
	private TiposIdentificacion tiposIdentificacion;
	private EstadosCiviles estadosCiviles;
	private String numeroIdentificacion;
	private Date fechaNacimiento;
	private String libretaMilitar;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String direccion;
	private String telefono;
	private String email;
	private String codigoLocalidadVivienda;
	private Long telefonoCelular;
	private Integer telefonoFijo;
	private String indicativoInterfaz;
	private String migrado;
	private Medicos medicos;
	private Set movimientosAbonoses = new HashSet(0);
	private Set autorizacioneses = new HashSet(0);
	private Set novedadEnfermeras = new HashSet(0);
	private Set usuarioses = new HashSet(0);
	private Set inconsistenciaPersonas = new HashSet(0);
	private Set facturases = new HashSet(0);
	private Set egresoses = new HashSet(0);
	private Set vacacioneses = new HashSet(0);
	private Set enfermeraRestriccions = new HashSet(0);
	private Set categoriaEnfermeras = new HashSet(0);
	private Set usuarioXConvenios = new HashSet(0);
	private Set enfermeraTRestriccions = new HashSet(0);
	private Set logAutorizacionIngEvens = new HashSet(0);
	private Set ctTurnoGenerals = new HashSet(0);
	private Set peticionQxes = new HashSet(0);
	private Set beneficiariosPacientes = new HashSet(0);
	private Pacientes pacientes;

	public Personas() {
	}

	public Personas(int codigo, TiposPersonas tiposPersonas,
			TiposIdentificacion tiposIdentificacion,
			String numeroIdentificacion, String primerNombre,
			String primerApellido, String indicativoInterfaz) {
		this.codigo = codigo;
		this.tiposPersonas = tiposPersonas;
		this.tiposIdentificacion = tiposIdentificacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.primerNombre = primerNombre;
		this.primerApellido = primerApellido;
		this.indicativoInterfaz = indicativoInterfaz;
	}

	public Personas(int codigo, Ciudades ciudadesByFkPCiudadid, Sexo sexo,
			Ciudades ciudadesByFkPCiudadviv, TiposPersonas tiposPersonas,
			Ciudades ciudadesByFkPCiudadnac, Barrios barrios,
			TiposIdentificacion tiposIdentificacion,
			EstadosCiviles estadosCiviles, String numeroIdentificacion,
			Date fechaNacimiento, String libretaMilitar, String primerNombre,
			String segundoNombre, String primerApellido,
			String segundoApellido, String direccion, String telefono,
			String email, String codigoLocalidadVivienda, Long telefonoCelular,
			Integer telefonoFijo, String indicativoInterfaz, String migrado,
			Medicos medicos, Set movimientosAbonoses, Set autorizacioneses,
			Set novedadEnfermeras, Set usuarioses, Set inconsistenciaPersonas,
			Set facturases, Set egresoses, Set vacacioneses,
			Set enfermeraRestriccions, Set categoriaEnfermeras,
			Set usuarioXConvenios, Set enfermeraTRestriccions,
			Set logAutorizacionIngEvens, Set ctTurnoGenerals, Set peticionQxes,
			Set beneficiariosPacientes, Pacientes pacientes) {
		this.codigo = codigo;
		this.ciudadesByFkPCiudadid = ciudadesByFkPCiudadid;
		this.sexo = sexo;
		this.ciudadesByFkPCiudadviv = ciudadesByFkPCiudadviv;
		this.tiposPersonas = tiposPersonas;
		this.ciudadesByFkPCiudadnac = ciudadesByFkPCiudadnac;
		this.barrios = barrios;
		this.tiposIdentificacion = tiposIdentificacion;
		this.estadosCiviles = estadosCiviles;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaNacimiento = fechaNacimiento;
		this.libretaMilitar = libretaMilitar;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.direccion = direccion;
		this.telefono = telefono;
		this.email = email;
		this.codigoLocalidadVivienda = codigoLocalidadVivienda;
		this.telefonoCelular = telefonoCelular;
		this.telefonoFijo = telefonoFijo;
		this.indicativoInterfaz = indicativoInterfaz;
		this.migrado = migrado;
		this.medicos = medicos;
		this.movimientosAbonoses = movimientosAbonoses;
		this.autorizacioneses = autorizacioneses;
		this.novedadEnfermeras = novedadEnfermeras;
		this.usuarioses = usuarioses;
		this.inconsistenciaPersonas = inconsistenciaPersonas;
		this.facturases = facturases;
		this.egresoses = egresoses;
		this.vacacioneses = vacacioneses;
		this.enfermeraRestriccions = enfermeraRestriccions;
		this.categoriaEnfermeras = categoriaEnfermeras;
		this.usuarioXConvenios = usuarioXConvenios;
		this.enfermeraTRestriccions = enfermeraTRestriccions;
		this.logAutorizacionIngEvens = logAutorizacionIngEvens;
		this.ctTurnoGenerals = ctTurnoGenerals;
		this.peticionQxes = peticionQxes;
		this.beneficiariosPacientes = beneficiariosPacientes;
		this.pacientes = pacientes;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Ciudades getCiudadesByFkPCiudadid() {
		return this.ciudadesByFkPCiudadid;
	}

	public void setCiudadesByFkPCiudadid(Ciudades ciudadesByFkPCiudadid) {
		this.ciudadesByFkPCiudadid = ciudadesByFkPCiudadid;
	}

	public Sexo getSexo() {
		return this.sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Ciudades getCiudadesByFkPCiudadviv() {
		return this.ciudadesByFkPCiudadviv;
	}

	public void setCiudadesByFkPCiudadviv(Ciudades ciudadesByFkPCiudadviv) {
		this.ciudadesByFkPCiudadviv = ciudadesByFkPCiudadviv;
	}

	public TiposPersonas getTiposPersonas() {
		return this.tiposPersonas;
	}

	public void setTiposPersonas(TiposPersonas tiposPersonas) {
		this.tiposPersonas = tiposPersonas;
	}

	public Ciudades getCiudadesByFkPCiudadnac() {
		return this.ciudadesByFkPCiudadnac;
	}

	public void setCiudadesByFkPCiudadnac(Ciudades ciudadesByFkPCiudadnac) {
		this.ciudadesByFkPCiudadnac = ciudadesByFkPCiudadnac;
	}

	public Barrios getBarrios() {
		return this.barrios;
	}

	public void setBarrios(Barrios barrios) {
		this.barrios = barrios;
	}

	public TiposIdentificacion getTiposIdentificacion() {
		return this.tiposIdentificacion;
	}

	public void setTiposIdentificacion(TiposIdentificacion tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	public EstadosCiviles getEstadosCiviles() {
		return this.estadosCiviles;
	}

	public void setEstadosCiviles(EstadosCiviles estadosCiviles) {
		this.estadosCiviles = estadosCiviles;
	}

	public String getNumeroIdentificacion() {
		return this.numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getLibretaMilitar() {
		return this.libretaMilitar;
	}

	public void setLibretaMilitar(String libretaMilitar) {
		this.libretaMilitar = libretaMilitar;
	}

	public String getPrimerNombre() {
		return this.primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return this.segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return this.primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return this.segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodigoLocalidadVivienda() {
		return this.codigoLocalidadVivienda;
	}

	public void setCodigoLocalidadVivienda(String codigoLocalidadVivienda) {
		this.codigoLocalidadVivienda = codigoLocalidadVivienda;
	}

	public Long getTelefonoCelular() {
		return this.telefonoCelular;
	}

	public void setTelefonoCelular(Long telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	public Integer getTelefonoFijo() {
		return this.telefonoFijo;
	}

	public void setTelefonoFijo(Integer telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	public String getIndicativoInterfaz() {
		return this.indicativoInterfaz;
	}

	public void setIndicativoInterfaz(String indicativoInterfaz) {
		this.indicativoInterfaz = indicativoInterfaz;
	}

	public String getMigrado() {
		return this.migrado;
	}

	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public Set getMovimientosAbonoses() {
		return this.movimientosAbonoses;
	}

	public void setMovimientosAbonoses(Set movimientosAbonoses) {
		this.movimientosAbonoses = movimientosAbonoses;
	}

	public Set getAutorizacioneses() {
		return this.autorizacioneses;
	}

	public void setAutorizacioneses(Set autorizacioneses) {
		this.autorizacioneses = autorizacioneses;
	}

	public Set getNovedadEnfermeras() {
		return this.novedadEnfermeras;
	}

	public void setNovedadEnfermeras(Set novedadEnfermeras) {
		this.novedadEnfermeras = novedadEnfermeras;
	}

	public Set getUsuarioses() {
		return this.usuarioses;
	}

	public void setUsuarioses(Set usuarioses) {
		this.usuarioses = usuarioses;
	}

	public Set getInconsistenciaPersonas() {
		return this.inconsistenciaPersonas;
	}

	public void setInconsistenciaPersonas(Set inconsistenciaPersonas) {
		this.inconsistenciaPersonas = inconsistenciaPersonas;
	}

	public Set getFacturases() {
		return this.facturases;
	}

	public void setFacturases(Set facturases) {
		this.facturases = facturases;
	}

	public Set getEgresoses() {
		return this.egresoses;
	}

	public void setEgresoses(Set egresoses) {
		this.egresoses = egresoses;
	}

	public Set getVacacioneses() {
		return this.vacacioneses;
	}

	public void setVacacioneses(Set vacacioneses) {
		this.vacacioneses = vacacioneses;
	}

	public Set getEnfermeraRestriccions() {
		return this.enfermeraRestriccions;
	}

	public void setEnfermeraRestriccions(Set enfermeraRestriccions) {
		this.enfermeraRestriccions = enfermeraRestriccions;
	}

	public Set getCategoriaEnfermeras() {
		return this.categoriaEnfermeras;
	}

	public void setCategoriaEnfermeras(Set categoriaEnfermeras) {
		this.categoriaEnfermeras = categoriaEnfermeras;
	}

	public Set getUsuarioXConvenios() {
		return this.usuarioXConvenios;
	}

	public void setUsuarioXConvenios(Set usuarioXConvenios) {
		this.usuarioXConvenios = usuarioXConvenios;
	}

	public Set getEnfermeraTRestriccions() {
		return this.enfermeraTRestriccions;
	}

	public void setEnfermeraTRestriccions(Set enfermeraTRestriccions) {
		this.enfermeraTRestriccions = enfermeraTRestriccions;
	}

	public Set getLogAutorizacionIngEvens() {
		return this.logAutorizacionIngEvens;
	}

	public void setLogAutorizacionIngEvens(Set logAutorizacionIngEvens) {
		this.logAutorizacionIngEvens = logAutorizacionIngEvens;
	}

	public Set getCtTurnoGenerals() {
		return this.ctTurnoGenerals;
	}

	public void setCtTurnoGenerals(Set ctTurnoGenerals) {
		this.ctTurnoGenerals = ctTurnoGenerals;
	}

	public Set getPeticionQxes() {
		return this.peticionQxes;
	}

	public void setPeticionQxes(Set peticionQxes) {
		this.peticionQxes = peticionQxes;
	}

	public Set getBeneficiariosPacientes() {
		return this.beneficiariosPacientes;
	}

	public void setBeneficiariosPacientes(Set beneficiariosPacientes) {
		this.beneficiariosPacientes = beneficiariosPacientes;
	}

	public Pacientes getPacientes() {
		return this.pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

}
