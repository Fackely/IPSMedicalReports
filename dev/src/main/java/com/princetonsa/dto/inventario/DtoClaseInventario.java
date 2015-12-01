package com.princetonsa.dto.inventario;

import java.io.Serializable;


/**
 * @author Cristhian Murillo 
 */
public class DtoClaseInventario implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Integer institucion;
	private String nombre;
	
	
	/**
	 * Constructor
	 */
	public DtoClaseInventario()
	{
		this.codigo			= null;
		this.institucion	= null;
		this.nombre			= null;
		
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo codigo
	 * @return retorna la variable codigo 
	 * @author Cristhian Murillo
	 */
	public Integer getCodigo() {
		return codigo;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo codigo
	 * @param valor para el atributo codigo 
	 * @author Cristhian Murillo
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo institucion
	 * @return retorna la variable institucion 
	 * @author Cristhian Murillo
	 */
	public Integer getInstitucion() {
		return institucion;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo institucion
	 * @param valor para el atributo institucion 
	 * @author Cristhian Murillo
	 */
	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nombre
	 * @return retorna la variable nombre 
	 * @author Cristhian Murillo
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nombre
	 * @param valor para el atributo nombre 
	 * @author Cristhian Murillo
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	

}
