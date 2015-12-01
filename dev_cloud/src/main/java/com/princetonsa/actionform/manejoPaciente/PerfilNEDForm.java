package com.princetonsa.actionform.manejoPaciente;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;
import com.princetonsa.mundo.historiaClinica.Plantillas;

/**
 * 
 * @author axioma
 *
 */
public class PerfilNEDForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private DtoEscala escala;
	
	/**
	 * 
	 */
	private DtoPerfilNed perfilNed;
	
	/**
	 * 
	 */
	public void reset()
	{
		this.escala= new DtoEscala();
		this.perfilNed= new DtoPerfilNed();
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
	    errores=super.validate(mapping,request);
	    
	    if(this.estado.equals("guardar"))
	    {
	    	//si ve ha insertar un perfil NED nuevo
			if(this.getPerfilNed().getCodigoPk()<=0)
			{
				if(!this.tieneInformacionPerfilNed())
				{
					errores.add("", new ActionMessage("errors.required", "Al menos un valor"));
				}
			}
	    	if(errores.isEmpty())
	    	{	
	    		errores= Plantillas.validarCamposEscala(this.escala, errores, ""/*nombreSeccionFija*/, false /*vieneDeComponente*/, ""/*nombreComponente*/);
	    	}	
	    }
	    if(!errores.isEmpty())
	    	this.setEstado("continuar");
	    
	    return errores;
	}

	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean tieneInformacionPerfilNed()
	{
		boolean tiene= false;
		for(DtoSeccionParametrizable seccion: this.getEscala().getSecciones())
		{	
			for(DtoCampoParametrizable campo:seccion.getCampos())
			{	
				if(!campo.getValor().equals(""))
				{	
					tiene = true;
				}
			}
		}	
		if(escala.getTotalEscala()>0||(!escala.getObservaciones().equals("") && !escala.getObservaciones().equals("observaciones_vacias"))||escala.getNumArchivosAdjuntos()>0)
		{	
			tiene = true;
		}
		return tiene;
	}
	
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the escala
	 */
	public DtoEscala getEscala() {
		return escala;
	}

	/**
	 * @param escala the escala to set
	 */
	public void setEscala(DtoEscala escala) {
		this.escala = escala;
	}

	/**
	 * @return the perfilNed
	 */
	public DtoPerfilNed getPerfilNed() {
		return perfilNed;
	}

	/**
	 * @param perfilNed the perfilNed to set
	 */
	public void setPerfilNed(DtoPerfilNed perfilNed) {
		this.perfilNed = perfilNed;
	}

		
	
}
