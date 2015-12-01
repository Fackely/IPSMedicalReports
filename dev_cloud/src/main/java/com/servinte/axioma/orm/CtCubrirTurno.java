package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * CtCubrirTurno generated by hbm2java
 */
public class CtCubrirTurno implements java.io.Serializable {

	private int ctcodigo;
	private Categoria categoria;
	private int codigomedico;
	private Date ctFecha;
	private int codigoturno;

	public CtCubrirTurno() {
	}

	public CtCubrirTurno(int ctcodigo, Categoria categoria, int codigomedico,
			Date ctFecha, int codigoturno) {
		this.ctcodigo = ctcodigo;
		this.categoria = categoria;
		this.codigomedico = codigomedico;
		this.ctFecha = ctFecha;
		this.codigoturno = codigoturno;
	}

	public int getCtcodigo() {
		return this.ctcodigo;
	}

	public void setCtcodigo(int ctcodigo) {
		this.ctcodigo = ctcodigo;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public int getCodigomedico() {
		return this.codigomedico;
	}

	public void setCodigomedico(int codigomedico) {
		this.codigomedico = codigomedico;
	}

	public Date getCtFecha() {
		return this.ctFecha;
	}

	public void setCtFecha(Date ctFecha) {
		this.ctFecha = ctFecha;
	}

	public int getCodigoturno() {
		return this.codigoturno;
	}

	public void setCodigoturno(int codigoturno) {
		this.codigoturno = codigoturno;
	}

}
