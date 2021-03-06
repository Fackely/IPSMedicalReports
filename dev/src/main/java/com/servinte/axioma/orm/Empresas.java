package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * Empresas generated by hbm2java
 */
public class Empresas implements java.io.Serializable {

	private int codigo;
	private Terceros terceros;
	private Ciudades ciudadesByFkEmpCiuprin;
	private NivelesIngreso nivelesIngreso;
	private FormasPago formasPago;
	private Ciudades ciudadesByFkEmpCiuc;
	private String razonSocial;
	private String telefono;
	private String direccion;
	private String email;
	private boolean activo;
	private String direccionCuentas;
	private String direccionSucursal;
	private String telefonoSucursal;
	private String nombreRepresentante;
	private String observaciones;
	private String faxSedePrincipal;
	private String faxSucursalLocal;
	private String direccionTerritorial;
	private Long numeroAfiliados;
	private Set envioInfoInconsistenciases = new HashSet(0);
	private Set envioInfoAtenIniUrgs = new HashSet(0);
	private Set convenioses = new HashSet(0);
	private Set deudoreses = new HashSet(0);
	private Set contactosEmpresas = new HashSet(0);

	public Empresas() {
	}

	public Empresas(int codigo, Terceros terceros, String razonSocial,
			String telefono, String direccion, boolean activo) {
		this.codigo = codigo;
		this.terceros = terceros;
		this.razonSocial = razonSocial;
		this.telefono = telefono;
		this.direccion = direccion;
		this.activo = activo;
	}

	public Empresas(int codigo, Terceros terceros,
			Ciudades ciudadesByFkEmpCiuprin, NivelesIngreso nivelesIngreso,
			FormasPago formasPago, Ciudades ciudadesByFkEmpCiuc,
			String razonSocial, String telefono, String direccion,
			String email, boolean activo, String direccionCuentas,
			String direccionSucursal, String telefonoSucursal,
			String nombreRepresentante, String observaciones,
			String faxSedePrincipal, String faxSucursalLocal,
			String direccionTerritorial, Long numeroAfiliados,
			Set envioInfoInconsistenciases, Set envioInfoAtenIniUrgs,
			Set convenioses, Set deudoreses, Set contactosEmpresas) {
		this.codigo = codigo;
		this.terceros = terceros;
		this.ciudadesByFkEmpCiuprin = ciudadesByFkEmpCiuprin;
		this.nivelesIngreso = nivelesIngreso;
		this.formasPago = formasPago;
		this.ciudadesByFkEmpCiuc = ciudadesByFkEmpCiuc;
		this.razonSocial = razonSocial;
		this.telefono = telefono;
		this.direccion = direccion;
		this.email = email;
		this.activo = activo;
		this.direccionCuentas = direccionCuentas;
		this.direccionSucursal = direccionSucursal;
		this.telefonoSucursal = telefonoSucursal;
		this.nombreRepresentante = nombreRepresentante;
		this.observaciones = observaciones;
		this.faxSedePrincipal = faxSedePrincipal;
		this.faxSucursalLocal = faxSucursalLocal;
		this.direccionTerritorial = direccionTerritorial;
		this.numeroAfiliados = numeroAfiliados;
		this.envioInfoInconsistenciases = envioInfoInconsistenciases;
		this.envioInfoAtenIniUrgs = envioInfoAtenIniUrgs;
		this.convenioses = convenioses;
		this.deudoreses = deudoreses;
		this.contactosEmpresas = contactosEmpresas;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Terceros getTerceros() {
		return this.terceros;
	}

	public void setTerceros(Terceros terceros) {
		this.terceros = terceros;
	}

	public Ciudades getCiudadesByFkEmpCiuprin() {
		return this.ciudadesByFkEmpCiuprin;
	}

	public void setCiudadesByFkEmpCiuprin(Ciudades ciudadesByFkEmpCiuprin) {
		this.ciudadesByFkEmpCiuprin = ciudadesByFkEmpCiuprin;
	}

	public NivelesIngreso getNivelesIngreso() {
		return this.nivelesIngreso;
	}

	public void setNivelesIngreso(NivelesIngreso nivelesIngreso) {
		this.nivelesIngreso = nivelesIngreso;
	}

	public FormasPago getFormasPago() {
		return this.formasPago;
	}

	public void setFormasPago(FormasPago formasPago) {
		this.formasPago = formasPago;
	}

	public Ciudades getCiudadesByFkEmpCiuc() {
		return this.ciudadesByFkEmpCiuc;
	}

	public void setCiudadesByFkEmpCiuc(Ciudades ciudadesByFkEmpCiuc) {
		this.ciudadesByFkEmpCiuc = ciudadesByFkEmpCiuc;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getDireccionCuentas() {
		return this.direccionCuentas;
	}

	public void setDireccionCuentas(String direccionCuentas) {
		this.direccionCuentas = direccionCuentas;
	}

	public String getDireccionSucursal() {
		return this.direccionSucursal;
	}

	public void setDireccionSucursal(String direccionSucursal) {
		this.direccionSucursal = direccionSucursal;
	}

	public String getTelefonoSucursal() {
		return this.telefonoSucursal;
	}

	public void setTelefonoSucursal(String telefonoSucursal) {
		this.telefonoSucursal = telefonoSucursal;
	}

	public String getNombreRepresentante() {
		return this.nombreRepresentante;
	}

	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFaxSedePrincipal() {
		return this.faxSedePrincipal;
	}

	public void setFaxSedePrincipal(String faxSedePrincipal) {
		this.faxSedePrincipal = faxSedePrincipal;
	}

	public String getFaxSucursalLocal() {
		return this.faxSucursalLocal;
	}

	public void setFaxSucursalLocal(String faxSucursalLocal) {
		this.faxSucursalLocal = faxSucursalLocal;
	}

	public String getDireccionTerritorial() {
		return this.direccionTerritorial;
	}

	public void setDireccionTerritorial(String direccionTerritorial) {
		this.direccionTerritorial = direccionTerritorial;
	}

	public Long getNumeroAfiliados() {
		return this.numeroAfiliados;
	}

	public void setNumeroAfiliados(Long numeroAfiliados) {
		this.numeroAfiliados = numeroAfiliados;
	}

	public Set getEnvioInfoInconsistenciases() {
		return this.envioInfoInconsistenciases;
	}

	public void setEnvioInfoInconsistenciases(Set envioInfoInconsistenciases) {
		this.envioInfoInconsistenciases = envioInfoInconsistenciases;
	}

	public Set getEnvioInfoAtenIniUrgs() {
		return this.envioInfoAtenIniUrgs;
	}

	public void setEnvioInfoAtenIniUrgs(Set envioInfoAtenIniUrgs) {
		this.envioInfoAtenIniUrgs = envioInfoAtenIniUrgs;
	}

	public Set getConvenioses() {
		return this.convenioses;
	}

	public void setConvenioses(Set convenioses) {
		this.convenioses = convenioses;
	}

	public Set getDeudoreses() {
		return this.deudoreses;
	}

	public void setDeudoreses(Set deudoreses) {
		this.deudoreses = deudoreses;
	}

	public Set getContactosEmpresas() {
		return this.contactosEmpresas;
	}

	public void setContactosEmpresas(Set contactosEmpresas) {
		this.contactosEmpresas = contactosEmpresas;
	}

}
