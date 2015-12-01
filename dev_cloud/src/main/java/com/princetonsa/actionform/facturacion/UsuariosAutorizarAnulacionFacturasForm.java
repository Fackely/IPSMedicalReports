package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadValidacion;
import util.Utilidades;

public class UsuariosAutorizarAnulacionFacturasForm extends ValidatorForm 
{

	
	private String estado;
	
	private String centroAtencion;
	
	private String usuarioAutorizado;
	
	private HashMap mapaConsultaUsuarios;
	
	private int codigoPk;
	
	
	/**
	 * 
	 */
	public void reset(String centroAtencion, String institucion)
	{
		this.estado="";
		this.centroAtencion=centroAtencion;
		this.usuarioAutorizado="";
		this.mapaConsultaUsuarios= new HashMap();
		this.mapaConsultaUsuarios.put("numRegistros", "0");
		this.codigoPk=ConstantesBD.codigoNuncaValido;
	}

	
	
	/**
	 *  Validate the properties that have been set from this HTTP request, and
	 *  return an <code>ActionErrors</code> object that encapsulates any 
	 *  validation errors that have been found. If no errors are found, return
	 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 *  error messages.
	 *  @param mapping The mapping used to select this instance
	 *  @param request The servlet request we are processing
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardarNuevo"))
		{
			if(this.usuarioAutorizado.equals(""))
			{
				errores.add("codigo paquete", new ActionMessage("errors.required","El Usuario "));
			}
			
			int numRegistros= Utilidades.convertirAEntero(this.mapaConsultaUsuarios.get("numRegistros")+"");
			
			for(int i=0;i<numRegistros;i++)
			{
				if((mapaConsultaUsuarios.get("loginusuario_"+i)+"").equals(this.getUsuarioAutorizado()))
        		{
        			errores.add("codigo ya existe",new ActionMessage("errors.yaExiste", "El Usuario "));
        		}
			}
			
			if(!errores.isEmpty())
        		this.setEstado("nuevoRegistro");
			
		}
		else if(this.estado.equals("guardarModificacion"))
		{
			if(this.usuarioAutorizado.equals(""))
			{
				errores.add("codigo paquete", new ActionMessage("errors.required","El Usuario "));
			}
			
			int numRegistros= Utilidades.convertirAEntero(this.mapaConsultaUsuarios.get("numRegistros")+"");
			
			for(int i=0;i<numRegistros;i++)
			{
				if((mapaConsultaUsuarios.get("loginusuario_"+i)+"").equals(this.getUsuarioAutorizado()))
        		{
        			errores.add("codigo ya existe",new ActionMessage("errors.yaExiste", "El Usuario "));
        		}
			}
			
			if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
			
		}
		return errores;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuarioAutorizado() {
		return usuarioAutorizado;
	}

	/**
	 * 
	 * @param usuarioAutorizado
	 */
	public void setUsuarioAutorizado(String usuarioAutorizado) {
		this.usuarioAutorizado = usuarioAutorizado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaUsuarios() {
		return mapaConsultaUsuarios;
	}

	/**
	 * 
	 * @param mapaConsultaUsuarios
	 */
	public void setMapaConsultaUsuarios(HashMap mapaConsultaUsuarios) {
		this.mapaConsultaUsuarios = mapaConsultaUsuarios;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * 
	 * @param codigoPk
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	
	
}
