package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.util.ArrayList;
import util.ConstantesBD;

/**
 * Data Transfer Object: 
 * Usado para el manejo de los modulos
 * @author Gio.
 */
public class DtoModulo implements Serializable
{
	/**
	 * Verisón serial
	 */
	private static final long serialVersionUID = 1L;
	private int codigo;
	private String nombre;
	private ArrayList<DtoFuncionalidad> funcionalidades;
		
	/**
	 * Constructor
	 */
	public DtoModulo()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTO
	 */
	public void clean()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.funcionalidades = new ArrayList<DtoFuncionalidad>();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<DtoFuncionalidad> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(ArrayList<DtoFuncionalidad> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}
	
	
}
