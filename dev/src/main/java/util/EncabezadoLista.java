/*
 * Created on Feb 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package util;

import java.io.Serializable;

/**
 * @author rcancino
 *
 */
public class EncabezadoLista implements Serializable {
	private String titulo;
	private String propiedad;
	private boolean enlace;
	private boolean sort;
	private boolean autolink;
	/**
	 * @return
	 */
	public boolean isAutolink() {
		return autolink;
	}

	/**
	 * @return
	 */
	public boolean isEnlace() {
		return enlace;
	}

	/**
	 * @return
	 */
	public String getPropiedad() {
		return propiedad;
	}

	/**
	 * @return
	 */
	public boolean isSort() {
		return sort;
	}

	/**
	 * @return
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param b
	 */
	public void setAutolink(boolean b) {
		autolink = b;
	}

	/**
	 * @param b
	 */
	public void setEnlace(boolean b) {
		enlace = b;
	}

	/**
	 * @param string
	 */
	public void setPropiedad(String string) {
		propiedad = string;
	}

	/**
	 * @param b
	 */
	public void setSort(boolean b) {
		sort = b;
	}

	/**
	 * @param string
	 */
	public void setTitulo(String string) {
		titulo = string;
	}
	
	public EncabezadoLista(String propiedad, String titulo, boolean sort, boolean enlace, boolean autolink){
		this.autolink=autolink;
		this.enlace=enlace;
		this.propiedad=propiedad;

		this.sort=sort;
		this.titulo=titulo;
	}
	public void reset() {
		this.autolink=false;
		this.enlace=false;
		this.propiedad="";
		this.sort=false;
		this.titulo="";
	}

}
