package com.princetonsa.dto.interfaz;

import java.io.Serializable;

public class DtoDocumentoDesmarcar implements Serializable{

	private String codigo;
	private String nombre;
	private String desmarcado;
	
	public DtoDocumentoDesmarcar()
	{
		this.reset();
	}
	
	public void  reset()
	{
		this.codigo=new String("");
		this.nombre=new String("");
		this.desmarcado=new String("");
		
	}

	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDesmarcado() {
		return desmarcado;
	}

	public void setDesmarcado(String desmarcado) {
		this.desmarcado = desmarcado;
	}
	
	
	
	
	
}
