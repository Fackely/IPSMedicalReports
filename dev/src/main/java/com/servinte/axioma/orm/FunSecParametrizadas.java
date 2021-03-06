package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * FunSecParametrizadas generated by hbm2java
 */
public class FunSecParametrizadas implements java.io.Serializable {

	private int codigo;
	private Especialidades especialidades;
	private Instituciones instituciones;
	private FuncionalParametrizables funcionalParametrizables;
	private CentrosCosto centrosCosto;
	private int codigoSeccion;
	private int codigoCampo;
	private String tipoCampo;
	private Integer orden;
	private Character requerido;
	private Character horizontal;
	private Integer numeroFilas;
	private Integer numeroColumnas;
	private Integer tamanioCampo;
	private Integer numeroCaracteres;
	private Set infoParametrizableVals = new HashSet(0);

	public FunSecParametrizadas() {
	}

	public FunSecParametrizadas(int codigo, Instituciones instituciones,
			FuncionalParametrizables funcionalParametrizables,
			int codigoSeccion, int codigoCampo) {
		this.codigo = codigo;
		this.instituciones = instituciones;
		this.funcionalParametrizables = funcionalParametrizables;
		this.codigoSeccion = codigoSeccion;
		this.codigoCampo = codigoCampo;
	}

	public FunSecParametrizadas(int codigo, Especialidades especialidades,
			Instituciones instituciones,
			FuncionalParametrizables funcionalParametrizables,
			CentrosCosto centrosCosto, int codigoSeccion, int codigoCampo,
			String tipoCampo, Integer orden, Character requerido,
			Character horizontal, Integer numeroFilas, Integer numeroColumnas,
			Integer tamanioCampo, Integer numeroCaracteres,
			Set infoParametrizableVals) {
		this.codigo = codigo;
		this.especialidades = especialidades;
		this.instituciones = instituciones;
		this.funcionalParametrizables = funcionalParametrizables;
		this.centrosCosto = centrosCosto;
		this.codigoSeccion = codigoSeccion;
		this.codigoCampo = codigoCampo;
		this.tipoCampo = tipoCampo;
		this.orden = orden;
		this.requerido = requerido;
		this.horizontal = horizontal;
		this.numeroFilas = numeroFilas;
		this.numeroColumnas = numeroColumnas;
		this.tamanioCampo = tamanioCampo;
		this.numeroCaracteres = numeroCaracteres;
		this.infoParametrizableVals = infoParametrizableVals;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public FuncionalParametrizables getFuncionalParametrizables() {
		return this.funcionalParametrizables;
	}

	public void setFuncionalParametrizables(
			FuncionalParametrizables funcionalParametrizables) {
		this.funcionalParametrizables = funcionalParametrizables;
	}

	public CentrosCosto getCentrosCosto() {
		return this.centrosCosto;
	}

	public void setCentrosCosto(CentrosCosto centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public int getCodigoSeccion() {
		return this.codigoSeccion;
	}

	public void setCodigoSeccion(int codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public int getCodigoCampo() {
		return this.codigoCampo;
	}

	public void setCodigoCampo(int codigoCampo) {
		this.codigoCampo = codigoCampo;
	}

	public String getTipoCampo() {
		return this.tipoCampo;
	}

	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Character getRequerido() {
		return this.requerido;
	}

	public void setRequerido(Character requerido) {
		this.requerido = requerido;
	}

	public Character getHorizontal() {
		return this.horizontal;
	}

	public void setHorizontal(Character horizontal) {
		this.horizontal = horizontal;
	}

	public Integer getNumeroFilas() {
		return this.numeroFilas;
	}

	public void setNumeroFilas(Integer numeroFilas) {
		this.numeroFilas = numeroFilas;
	}

	public Integer getNumeroColumnas() {
		return this.numeroColumnas;
	}

	public void setNumeroColumnas(Integer numeroColumnas) {
		this.numeroColumnas = numeroColumnas;
	}

	public Integer getTamanioCampo() {
		return this.tamanioCampo;
	}

	public void setTamanioCampo(Integer tamanioCampo) {
		this.tamanioCampo = tamanioCampo;
	}

	public Integer getNumeroCaracteres() {
		return this.numeroCaracteres;
	}

	public void setNumeroCaracteres(Integer numeroCaracteres) {
		this.numeroCaracteres = numeroCaracteres;
	}

	public Set getInfoParametrizableVals() {
		return this.infoParametrizableVals;
	}

	public void setInfoParametrizableVals(Set infoParametrizableVals) {
		this.infoParametrizableVals = infoParametrizableVals;
	}

}
