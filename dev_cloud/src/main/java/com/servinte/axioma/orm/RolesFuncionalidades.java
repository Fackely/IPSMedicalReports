package com.servinte.axioma.orm;

// Generated May 5, 2010 3:51:40 PM by Hibernate Tools 3.2.5.Beta

/**
 * RolesFuncionalidades generated by hbm2java
 */
public class RolesFuncionalidades implements java.io.Serializable {

	private RolesFuncionalidadesId id;
	private Roles roles;
	private Funcionalidades funcionalidades;
	private boolean isssl;

	public RolesFuncionalidades() {
	}

	public RolesFuncionalidades(RolesFuncionalidadesId id, Roles roles,
			Funcionalidades funcionalidades, boolean isssl) {
		this.id = id;
		this.roles = roles;
		this.funcionalidades = funcionalidades;
		this.isssl = isssl;
	}

	public RolesFuncionalidadesId getId() {
		return this.id;
	}

	public void setId(RolesFuncionalidadesId id) {
		this.id = id;
	}

	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Funcionalidades getFuncionalidades() {
		return this.funcionalidades;
	}

	public void setFuncionalidades(Funcionalidades funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public boolean isIsssl() {
		return this.isssl;
	}

	public void setIsssl(boolean isssl) {
		this.isssl = isssl;
	}

}
