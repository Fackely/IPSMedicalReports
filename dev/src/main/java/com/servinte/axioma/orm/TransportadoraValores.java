package com.servinte.axioma.orm;

// Generated Jun 8, 2010 2:12:12 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TransportadoraValores generated by hbm2java
 */
public class TransportadoraValores implements java.io.Serializable {

	private int codigoPk;
	private Terceros terceros;
	private Usuarios usuarios;
	private String codigo;
	private char activo;
	private String direccion;
	private String telefono;
	private String personaContacto;
	private String observaciones;
	private Date fecha;
	private String hora;
	private Set centrosAtenTransportadoras = new HashSet(0);
	private Set entregaTransportadoras = new HashSet(0);

	public TransportadoraValores() {
	}

	public TransportadoraValores(int codigoPk, Terceros terceros,
			Usuarios usuarios, String codigo, char activo,
			String observaciones, Date fecha, String hora) {
		this.codigoPk = codigoPk;
		this.terceros = terceros;
		this.usuarios = usuarios;
		this.codigo = codigo;
		this.activo = activo;
		this.observaciones = observaciones;
		this.fecha = fecha;
		this.hora = hora;
	}

	public TransportadoraValores(int codigoPk, Terceros terceros,
			Usuarios usuarios, String codigo, char activo, String direccion,
			String telefono, String personaContacto, String observaciones,
			Date fecha, String hora, Set centrosAtenTransportadoras,
			Set entregaTransportadoras) {
		this.codigoPk = codigoPk;
		this.terceros = terceros;
		this.usuarios = usuarios;
		this.codigo = codigo;
		this.activo = activo;
		this.direccion = direccion;
		this.telefono = telefono;
		this.personaContacto = personaContacto;
		this.observaciones = observaciones;
		this.fecha = fecha;
		this.hora = hora;
		this.centrosAtenTransportadoras = centrosAtenTransportadoras;
		this.entregaTransportadoras = entregaTransportadoras;
	}

	public int getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Terceros getTerceros() {
		return this.terceros;
	}

	public void setTerceros(Terceros terceros) {
		this.terceros = terceros;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
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

	public String getPersonaContacto() {
		return this.personaContacto;
	}

	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Set getCentrosAtenTransportadoras() {
		return this.centrosAtenTransportadoras;
	}

	public void setCentrosAtenTransportadoras(Set centrosAtenTransportadoras) {
		this.centrosAtenTransportadoras = centrosAtenTransportadoras;
	}

	public Set getEntregaTransportadoras() {
		return this.entregaTransportadoras;
	}

	public void setEntregaTransportadoras(Set entregaTransportadoras) {
		this.entregaTransportadoras = entregaTransportadoras;
	}

}
