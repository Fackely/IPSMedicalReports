/**
 * 
 */
package com.princetonsa.actionform.carteraPaciente;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dto.carteraPaciente.DtoCierreCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author armando
 *
 */
public class CierreSaldoInicialCarteraPacienteForm extends ActionForm 
{

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	private DtoCierreCarteraPaciente cierreCarteraPaciente;
	
	

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("generar")||estado.equals("generarConfirmado"))
		{
			
			if(UtilidadTexto.isEmpty(this.getCierreCarteraPaciente().getAnioCierre()))
				errores.add("NUMERO ID PACIENTE", new ActionMessage("errors.required","El año cierre"));
			else
			{
				if(Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getAnioCierre())<1900)
					errores.add("error ", new ActionMessage("error.errorEnBlanco","El año de cierre "+this.getCierreCarteraPaciente().getAnioCierre()+" no es un año valido"));
	
				if(Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getAnioCierre())>UtilidadFecha.getMesAnioDiaActual("anio"))
					errores.add("error ", new ActionMessage("error.errorEnBlanco","El año de cierre debe ser menor ingual al año actual"));
						
			}
			if(UtilidadTexto.isEmpty(this.getCierreCarteraPaciente().getMesCierre()))
				errores.add("NUMERO ID PACIENTE", new ActionMessage("errors.required","El mes cierre"));
			else
			{
				if(Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getMesCierre())<1||Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getMesCierre())>12)
					errores.add("error ", new ActionMessage("error.errorEnBlanco","El mes de cierre "+this.getCierreCarteraPaciente().getMesCierre()+" no es un mes valido"));
					
				if(Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getAnioCierre())==UtilidadFecha.getMesAnioDiaActual("anio")&&Utilidades.convertirAEntero(this.getCierreCarteraPaciente().getMesCierre())>UtilidadFecha.getMesAnioDiaActual("mes"))
					errores.add("error ", new ActionMessage("error.errorEnBlanco","El mes de cierre debe ser menor ingual al mes actual"));
				
			}
		}
		return errores;
	}
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.mensaje=new ResultadoBoolean(false);
		this.cierreCarteraPaciente=new DtoCierreCarteraPaciente();
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}


	/**
	 * @return the cierreCarteraPaciente
	 */
	public DtoCierreCarteraPaciente getCierreCarteraPaciente() {
		return cierreCarteraPaciente;
	}


	/**
	 * @param cierreCarteraPaciente the cierreCarteraPaciente to set
	 */
	public void setCierreCarteraPaciente(
			DtoCierreCarteraPaciente cierreCarteraPaciente) {
		this.cierreCarteraPaciente = cierreCarteraPaciente;
	}


	

}
