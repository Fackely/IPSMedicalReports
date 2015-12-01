package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * InformeInconsistencias generated by hbm2java
 */
public class InformeInconsistencias implements java.io.Serializable {

	private long codigoPk;
	private Ingresos ingresos;
	private TiposInconsistencias tiposInconsistencias;
	private SubCuentas subCuentas;
	private Cuentas cuentas;
	private Pacientes pacientes;
	private ViasIngreso viasIngreso;
	private Usuarios usuarios;
	private TiposIdentificacion tiposIdentificacion;
	private CoberturasSalud coberturasSalud;
	private Convenios convenios;
	private String nombreConvenio;
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String numeroIdentificacion;
	private Date fechaNacimiento;
	private String direccionResidencia;
	private String telefono;
	private String departamento;
	private String codigoDepartamento;
	private String municipio;
	private String codigoMunicipio;
	private String observaciones;
	private String consecutivo;
	private String anioConsecutivo;
	private String estado;
	private Date fechaGeneracion;
	private String horaGeneracion;
	private String usuarioGeneracion;
	private Date fechaModificacion;
	private String horaModificacion;
	private Set envioInfoInconsistenciases = new HashSet(0);
	private Set infoInconVarIncorrectas = new HashSet(0);

	public InformeInconsistencias() {
	}

	public InformeInconsistencias(long codigoPk, Ingresos ingresos,
			TiposInconsistencias tiposInconsistencias, SubCuentas subCuentas,
			Cuentas cuentas, Pacientes pacientes, ViasIngreso viasIngreso,
			Usuarios usuarios, TiposIdentificacion tiposIdentificacion,
			CoberturasSalud coberturasSalud, Convenios convenios,
			String nombreConvenio, String primerApellido, String primerNombre,
			String numeroIdentificacion, Date fechaNacimiento,
			String direccionResidencia, String telefono, String departamento,
			String codigoDepartamento, String municipio,
			String codigoMunicipio, String consecutivo, String estado,
			Date fechaGeneracion, String horaGeneracion,
			String usuarioGeneracion, Date fechaModificacion,
			String horaModificacion) {
		this.codigoPk = codigoPk;
		this.ingresos = ingresos;
		this.tiposInconsistencias = tiposInconsistencias;
		this.subCuentas = subCuentas;
		this.cuentas = cuentas;
		this.pacientes = pacientes;
		this.viasIngreso = viasIngreso;
		this.usuarios = usuarios;
		this.tiposIdentificacion = tiposIdentificacion;
		this.coberturasSalud = coberturasSalud;
		this.convenios = convenios;
		this.nombreConvenio = nombreConvenio;
		this.primerApellido = primerApellido;
		this.primerNombre = primerNombre;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaNacimiento = fechaNacimiento;
		this.direccionResidencia = direccionResidencia;
		this.telefono = telefono;
		this.departamento = departamento;
		this.codigoDepartamento = codigoDepartamento;
		this.municipio = municipio;
		this.codigoMunicipio = codigoMunicipio;
		this.consecutivo = consecutivo;
		this.estado = estado;
		this.fechaGeneracion = fechaGeneracion;
		this.horaGeneracion = horaGeneracion;
		this.usuarioGeneracion = usuarioGeneracion;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
	}

	public InformeInconsistencias(long codigoPk, Ingresos ingresos,
			TiposInconsistencias tiposInconsistencias, SubCuentas subCuentas,
			Cuentas cuentas, Pacientes pacientes, ViasIngreso viasIngreso,
			Usuarios usuarios, TiposIdentificacion tiposIdentificacion,
			CoberturasSalud coberturasSalud, Convenios convenios,
			String nombreConvenio, String primerApellido,
			String segundoApellido, String primerNombre, String segundoNombre,
			String numeroIdentificacion, Date fechaNacimiento,
			String direccionResidencia, String telefono, String departamento,
			String codigoDepartamento, String municipio,
			String codigoMunicipio, String observaciones, String consecutivo,
			String anioConsecutivo, String estado, Date fechaGeneracion,
			String horaGeneracion, String usuarioGeneracion,
			Date fechaModificacion, String horaModificacion,
			Set envioInfoInconsistenciases, Set infoInconVarIncorrectas) {
		this.codigoPk = codigoPk;
		this.ingresos = ingresos;
		this.tiposInconsistencias = tiposInconsistencias;
		this.subCuentas = subCuentas;
		this.cuentas = cuentas;
		this.pacientes = pacientes;
		this.viasIngreso = viasIngreso;
		this.usuarios = usuarios;
		this.tiposIdentificacion = tiposIdentificacion;
		this.coberturasSalud = coberturasSalud;
		this.convenios = convenios;
		this.nombreConvenio = nombreConvenio;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.numeroIdentificacion = numeroIdentificacion;
		this.fechaNacimiento = fechaNacimiento;
		this.direccionResidencia = direccionResidencia;
		this.telefono = telefono;
		this.departamento = departamento;
		this.codigoDepartamento = codigoDepartamento;
		this.municipio = municipio;
		this.codigoMunicipio = codigoMunicipio;
		this.observaciones = observaciones;
		this.consecutivo = consecutivo;
		this.anioConsecutivo = anioConsecutivo;
		this.estado = estado;
		this.fechaGeneracion = fechaGeneracion;
		this.horaGeneracion = horaGeneracion;
		this.usuarioGeneracion = usuarioGeneracion;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
		this.envioInfoInconsistenciases = envioInfoInconsistenciases;
		this.infoInconVarIncorrectas = infoInconVarIncorrectas;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public TiposInconsistencias getTiposInconsistencias() {
		return this.tiposInconsistencias;
	}

	public void setTiposInconsistencias(
			TiposInconsistencias tiposInconsistencias) {
		this.tiposInconsistencias = tiposInconsistencias;
	}

	public SubCuentas getSubCuentas() {
		return this.subCuentas;
	}

	public void setSubCuentas(SubCuentas subCuentas) {
		this.subCuentas = subCuentas;
	}

	public Cuentas getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(Cuentas cuentas) {
		this.cuentas = cuentas;
	}

	public Pacientes getPacientes() {
		return this.pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public ViasIngreso getViasIngreso() {
		return this.viasIngreso;
	}

	public void setViasIngreso(ViasIngreso viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public TiposIdentificacion getTiposIdentificacion() {
		return this.tiposIdentificacion;
	}

	public void setTiposIdentificacion(TiposIdentificacion tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}

	public CoberturasSalud getCoberturasSalud() {
		return this.coberturasSalud;
	}

	public void setCoberturasSalud(CoberturasSalud coberturasSalud) {
		this.coberturasSalud = coberturasSalud;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public String getNombreConvenio() {
		return this.nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
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

	public String getDireccionResidencia() {
		return this.direccionResidencia;
	}

	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = direccionResidencia;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDepartamento() {
		return this.departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCodigoDepartamento() {
		return this.codigoDepartamento;
	}

	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	public String getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getCodigoMunicipio() {
		return this.codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnioConsecutivo() {
		return this.anioConsecutivo;
	}

	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return this.horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public String getUsuarioGeneracion() {
		return this.usuarioGeneracion;
	}

	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}

	public Date getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return this.horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public Set getEnvioInfoInconsistenciases() {
		return this.envioInfoInconsistenciases;
	}

	public void setEnvioInfoInconsistenciases(Set envioInfoInconsistenciases) {
		this.envioInfoInconsistenciases = envioInfoInconsistenciases;
	}

	public Set getInfoInconVarIncorrectas() {
		return this.infoInconVarIncorrectas;
	}

	public void setInfoInconVarIncorrectas(Set infoInconVarIncorrectas) {
		this.infoInconVarIncorrectas = infoInconVarIncorrectas;
	}

}
