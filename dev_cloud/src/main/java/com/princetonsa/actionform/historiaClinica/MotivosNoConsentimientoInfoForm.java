/**
 * 
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.DtoMotivosNoConsentimientoInformado;
import com.princetonsa.mundo.odontologia.MotivosCambioServicioMundo;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

/**
 * @author armando
 *
 */
public class MotivosNoConsentimientoInfoForm extends ValidatorForm 
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
	private int registroSeleccionado;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	private ArrayList<DtoMotivosNoConsentimientoInformado> motivos;
	

	private ArrayList<DtoMotivosNoConsentimientoInformado> motivosEliminados;
	
	public ArrayList<DtoMotivosNoConsentimientoInformado> getMotivosEliminados() {
		return motivosEliminados;
	}

	public void setMotivosEliminados(
			ArrayList<DtoMotivosNoConsentimientoInformado> motivosEliminados) {
		this.motivosEliminados = motivosEliminados;
	}

	/**
	 * 
	 */
	public void reset()
	{
		this.registroSeleccionado=ConstantesBD.codigoNuncaValido;
		this.mensaje=new ResultadoBoolean(false);
		this.motivos=new ArrayList<DtoMotivosNoConsentimientoInformado>();
		this.motivosEliminados=new ArrayList<DtoMotivosNoConsentimientoInformado>();
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
	{
      ActionErrors errores = new ActionErrors();
      if(this.estado.equals("guardar"))
      {
	      for(int i=0;i<this.motivos.size();i++)
	      {
	    	  DtoMotivosNoConsentimientoInformado motivo=motivos.get(i);
	    	  if(motivo.getDescripcion().isEmpty())
	    	  {
	    		  errores.add("", new ActionMessage("errors.required","La descripción del registro "+(i+1)));  
	    	  }
	    	  for(int j=0;j<i;j++)
	    	  {
	    		  DtoMotivosNoConsentimientoInformado motivoInterno=motivos.get(j);
	    		  if(motivo.getDescripcion().equals(motivoInterno.getDescripcion()))
	    		  {
	    			  errores.add("", new ActionMessage("errors.yaExiste","La descripción \""+motivo.getDescripcion()+"\""));          
	    		  }
	    	  }
	      }
      }
	  
      return errores;
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

	public ArrayList<DtoMotivosNoConsentimientoInformado> getMotivos() {
		return motivos;
	}

	public void setMotivos(ArrayList<DtoMotivosNoConsentimientoInformado> motivos) {
		this.motivos = motivos;
	}
	
	

}
