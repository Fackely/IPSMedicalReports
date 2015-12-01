package com.princetonsa.dto.administracion;

import java.io.Serializable;

import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.Modulos;
import com.servinte.axioma.orm.Roles;


/**
 * Dto que contiene la informacion relacionada entre un modulo, un rol y una funcionalidad
 * 
 * @author Cristhian Murillo
 */
public class DtoModuloRolFuncionalidad implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private Modulos modulos;
	private Roles roles;
	private Funcionalidades funcionalidadesPadre;
	private Funcionalidades funcionalidadesHija;
	private boolean asignado;
	
	

	/**
	 * constructor de la clase
	 */
	public  DtoModuloRolFuncionalidad()
	{
		this.modulos = new Modulos();
		this.roles = new Roles();
		this.funcionalidadesPadre = new Funcionalidades();
		this.funcionalidadesHija = new Funcionalidades();
		this.asignado = true;
		
	}



	public Modulos getModulos() {
		return modulos;
	}



	public void setModulos(Modulos modulos) {
		this.modulos = modulos;
	}



	public Roles getRoles() {
		return roles;
	}



	public void setRoles(Roles roles) {
		this.roles = roles;
	}



	public Funcionalidades getFuncionalidadesPadre() {
		return funcionalidadesPadre;
	}



	public void setFuncionalidadesPadre(Funcionalidades funcionalidadesPadre) {
		this.funcionalidadesPadre = funcionalidadesPadre;
	}



	public Funcionalidades getFuncionalidadesHija() {
		return funcionalidadesHija;
	}



	public void setFuncionalidadesHija(Funcionalidades funcionalidadesHija) {
		this.funcionalidadesHija = funcionalidadesHija;
	}



	public boolean isAsignado() {
		return asignado;
	}



	public void setAsignado(boolean asignado) {
		this.asignado = asignado;
	}

	

}
