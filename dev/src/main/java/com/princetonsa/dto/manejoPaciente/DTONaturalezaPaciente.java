package com.princetonsa.dto.manejoPaciente;

import com.servinte.axioma.orm.NaturalezaPacientes;

/**
 * Esta clase se encarga de contener los datos
 * de una Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 17/08/2010
 */
public class DTONaturalezaPaciente extends NaturalezaPacientes  {
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	
	private boolean permiteEliminar;
	
	
	/**
	 * Atributo que representa el codigo de la Naturaleza Pacientes
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el nombre de la Naturaleza Pacientes
	 */
	private String nombre;

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo permiteEliminar
	
	 * @return retorna la variable permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isPermiteEliminar() {
		return permiteEliminar;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo permiteEliminar
	
	 * @param valor para el atributo permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPermiteEliminar(boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
