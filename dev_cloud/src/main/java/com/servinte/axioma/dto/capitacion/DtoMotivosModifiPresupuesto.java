package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;  

import com.servinte.axioma.orm.MotivosModifiPresupuesto;


/**
 * Extiende de la entidad de motivos modificación presupuesto para contener dos valores de validación
 * 
 * @author Cristhian Murillo</a>
 * 
 */
public class DtoMotivosModifiPresupuesto extends MotivosModifiPresupuesto implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 7798873716305062575L;


	/**
	 * 
	 */
	private Boolean estado;
	
	/** *  */
	private Integer codInstitucion;
	
	/** *  */
	private String loginUsuario;
	
	/**
	 * 
	 */
	private Boolean permiteEliminar;
	
	
	
	public DtoMotivosModifiPresupuesto() {
		this.estado=true;
		this.permiteEliminar=false;
	}
	
	
	

	/**
	 * @return valor de estado
	 */
	public Boolean getEstado() {
		return estado;
	}

	/**
	 * @param estado el estado para asignar
	 */
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	/**
	 * @return valor de permiteEliminar
	 */
	public Boolean getPermiteEliminar() {
		return permiteEliminar;
	}

	/**
	 * @param permiteEliminar el permiteEliminar para asignar
	 */
	public void setPermiteEliminar(Boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
	}

	/**
	 * @return valor de codInstitucion
	 */
	public Integer getCodInstitucion() {
		return codInstitucion;
	}

	/**
	 * @param codInstitucion el codInstitucion para asignar
	 */
	public void setCodInstitucion(Integer codInstitucion) {
		this.codInstitucion = codInstitucion;
	}

	/**
	 * @return valor de loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario el loginUsuario para asignar
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	
	
}
