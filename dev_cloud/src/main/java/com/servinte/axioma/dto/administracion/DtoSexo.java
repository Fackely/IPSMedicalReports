package com.servinte.axioma.dto.administracion;

/**
 * Clase para manejar la informacion relacionada con la entidad Sexo
 * @author Juan Camilo Gaviria Acosta
 */
public class DtoSexo {
	
	private int codigoSexo;
	private String nombre;
	
	public DtoSexo() {
	}
	
	public DtoSexo(int codigoSexo, String nombre) {
		this.codigoSexo = codigoSexo;
		this.nombre = nombre;
	}
	
	public int getCodigoSexo() {
		return codigoSexo;
	}
	public void setCodigoSexo(int codigoSexo) {
		this.codigoSexo = codigoSexo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}