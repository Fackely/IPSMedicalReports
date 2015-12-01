package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import util.ConstantesBD;


/**
 * @author Diana Carolina G
 *
 */

public class DtoEstadosRecibosCaja implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	private int codigo;
	private String descripcion;
	
	/**
	 * Constructor
	 */
	
	public DtoEstadosRecibosCaja()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.descripcion="";
	}


	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
