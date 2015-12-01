/**
 * 
 */
package com.servinte.axioma.dto.administracion;

import util.FuncionalidadMenu;

/**
 * @author jeilones
 * @created 30/10/2012
 *
 */
public class FuncionalidadDto extends FuncionalidadMenu {

	private int codigo;
	private String etiqueta;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1620228301744571664L;

	/**
	 * @param nombre
	 * @param archivo
	 * @param codigo
	 * @author jeilones
	 * @param etiqueta 
	 * @created 30/10/2012
	 */
	public FuncionalidadDto(int codigo,String nombre, String etiqueta, String archivo) {
		super(nombre, archivo, codigo+"");
		this.codigo=codigo;
		this.etiqueta=etiqueta;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

}
