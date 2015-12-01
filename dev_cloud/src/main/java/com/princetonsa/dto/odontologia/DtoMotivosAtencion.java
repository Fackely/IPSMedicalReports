/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import com.princetonsa.dto.interfaz.DtoCuentaContable;

import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo del concepto de retencion
 * 
 * @author Angela María Angel.
 *
 */
public class DtoMotivosAtencion implements Serializable
{
	private int codigoPk;
	private String codigo;
	private String institucion;
	private String nombre;
	private int tipo;
	private String descTipo;
	private String fecha;
	private String hora;
	private String usuario;
		
		
	/**
	 * Constructor
	 */
	public DtoMotivosAtencion()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTo
	 */
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigo= "";
		this.nombre = "";
		this.tipo = ConstantesBD.codigoNuncaValido;
		this.fecha ="";
		this.hora ="";
		this.usuario = "";	
		this.descTipo="";
	}

	public String getDescTipo() {
		return descTipo;
	}

	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
