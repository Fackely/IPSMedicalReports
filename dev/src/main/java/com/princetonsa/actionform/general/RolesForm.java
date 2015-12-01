package com.princetonsa.actionform.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoModuloRolFuncionalidad;
import com.princetonsa.dto.administracion.DtoRolesFuncionalidades;
import com.princetonsa.dto.odontologia.DtoRolesTipoDeUsuario;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Funcionalidades;
import com.servinte.axioma.orm.Roles;

public class RolesForm extends ActionForm{

	/**  * Serial version UID 	 */
	private static final long serialVersionUID = 1L;
	
	/** * Estado de la funcionalidad  */
	private static String estado;
	
	
	/** * Nombre del rol a buscar  */
	private String nombreRol;
	
	
	private ArrayList<Roles> roles;
	
	private ArrayList<Funcionalidades> funcionalidades;
	
	/**
	 * 
	 */
	private int regEliminar;
	
	/**
	 * 
	 */
	private ArrayList<DtoRolesFuncionalidades> rolesFuncionalidades;
	
	/**
	 * 
	 */
	private int codigoFunc;
	
	private ResultadoBoolean mostrarMensaje ;
	
	/**
	 * 
	 */
	private String nombreRolNuevo;
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.nombreRol = "";
		this.roles=new ArrayList<Roles>();
		this.funcionalidades=new ArrayList<Funcionalidades>();
		this.codigoFunc=ConstantesBD.codigoNuncaValido;
		this.rolesFuncionalidades=new ArrayList<DtoRolesFuncionalidades>();
		this.regEliminar=ConstantesBD.codigoNuncaValido;
		this.mostrarMensaje=new ResultadoBoolean();
		this.nombreRolNuevo="";
	}
	

	
	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		RolesForm.estado = estado;
	}



	public String getNombreRol() {
		return nombreRol;
	}



	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}






	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("adicionarFuncionalidadRol"))
		{
			
			if(this.codigoFunc<=0)
			{
				this.setMostrarMensaje(new ResultadoBoolean(false));
				errores.add("codigo_func", new ActionMessage("errors.required", " La funcionalidad"));
			}else{
				
				for(DtoRolesFuncionalidades roles:this.getRolesFuncionalidades())
				{
					if(roles.getCodigoFunc()==this.getCodigoFunc())
					{
						this.setMostrarMensaje(new ResultadoBoolean(false));
						errores.add("codigo_func", new ActionMessage("errors.notEspecific", "La Funcionalidad ya ha sido asociada al rol seleccionado."));
						break;
					}
				}
			}
		}
		if(estado.equals("adicionarRol"))
		{
			
			if(UtilidadTexto.isEmpty(nombreRolNuevo))
			{
				errores.add("rol nuevo", new ActionMessage("errors.required", " El nombre del Rol"));
			}
		}
		return errores;
	}






	public ArrayList<Roles> getRoles() {
		return roles;
	}



	public void setRoles(ArrayList<Roles> roles) {
		this.roles = roles;
	}



	public ArrayList<Funcionalidades> getFuncionalidades() {
		return funcionalidades;
	}



	public void setFuncionalidades(ArrayList<Funcionalidades> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}



	public int getCodigoFunc() {
		return codigoFunc;
	}



	public void setCodigoFunc(int codigoFunc) {
		this.codigoFunc = codigoFunc;
	}



	public ArrayList<DtoRolesFuncionalidades> getRolesFuncionalidades() {
		return rolesFuncionalidades;
	}



	public void setRolesFuncionalidades(
			ArrayList<DtoRolesFuncionalidades> rolesFuncionalidades) {
		this.rolesFuncionalidades = rolesFuncionalidades;
	}



	public int getRegEliminar() {
		return regEliminar;
	}



	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}



	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}



	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}



	public String getNombreRolNuevo() {
		return nombreRolNuevo;
	}



	public void setNombreRolNuevo(String nombreRolNuevo) {
		this.nombreRolNuevo = nombreRolNuevo;
	}

	
}
