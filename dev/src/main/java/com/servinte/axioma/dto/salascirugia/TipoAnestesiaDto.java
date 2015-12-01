/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class TipoAnestesiaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2998404978609422188L;

	private int codigo;
	private String acronimo;
	private String descripcion;
	
	private boolean mostrarEnHQx;
	
	public TipoAnestesiaDto()
	{
		
	}

	public TipoAnestesiaDto(int codigo, String acronimo, String descripcion, boolean mostrarEnHQx)
	{
		this.codigo = codigo;
		this.acronimo = acronimo;
		this.descripcion = descripcion;
		this.mostrarEnHQx = mostrarEnHQx;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isMostrarEnHQx() {
		return mostrarEnHQx;
	}

	public void setMostrarEnHQx(boolean mostrarEnHQx) {
		this.mostrarEnHQx = mostrarEnHQx;
	}
}
