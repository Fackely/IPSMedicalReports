/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.mundo.odontologia.MotivosCambioServicioMundo;
import com.princetonsa.mundo.odontologia.PaquetesOdontologicosMundo;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

/**
 * @author armando
 *
 */
public class MotivosCambioServicioForm extends ValidatorForm 
{
	/**
	 * 
	 */

	private String estado;
	
	/**
	 * 
	 */
	private int codigoInstitucion;

	/**
	 * 
	 */
	private ArrayList<DtoMotivosCambioServicio> motivos;
	
	
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private String tipo;
	
	/**
	 * 
	 */
	private int registroSeleccionado;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
	{
      ActionErrors errores = new ActionErrors();
      if(this.estado.equals("guardar"))
      {
	      if(UtilidadTexto.isEmpty(this.codigo))
		  {
			  errores.add("", new ActionMessage("errors.required","El codigo "));  
		  }
		  else
		  {
			  if(codigoPk<=0)
			  {
		    	  if(MotivosCambioServicioMundo.existeMotivoCambioServicio(this.codigo,this.codigoInstitucion))
		    	  {
		    		  errores.add("", new ActionMessage("errors.yaExiste","El código "+this.codigo));          
		    	  }
			  }
		  }
		  if(UtilidadTexto.isEmpty(this.descripcion))
		  {
			  errores.add("", new ActionMessage("errors.required","La descripcion "));  
		  }
		  if(UtilidadTexto.isEmpty(this.tipo))
		  {
			  errores.add("", new ActionMessage("errors.required","El tipo "));  
		  }
		  if(!errores.isEmpty())
			  this.estado="nuevo";
      }
	  
      return errores;
	}
	
	/**
	 * 
	 */
	public void resetNuevo()
	{
		this.codigo="";
		this.descripcion="";
		this.tipo="";
	}
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.codigoInstitucion=ConstantesBD.codigoNuncaValido;
		this.motivos=new ArrayList<DtoMotivosCambioServicio>();
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigo="";
		this.descripcion="";
		this.tipo="";
		this.registroSeleccionado=ConstantesBD.codigoNuncaValido;
		this.mensaje=new ResultadoBoolean(false);
		
	}
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public ArrayList<DtoMotivosCambioServicio> getMotivos() {
		return motivos;
	}

	public void setMotivos(ArrayList<DtoMotivosCambioServicio> motivos) {
		this.motivos = motivos;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(int registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

}
