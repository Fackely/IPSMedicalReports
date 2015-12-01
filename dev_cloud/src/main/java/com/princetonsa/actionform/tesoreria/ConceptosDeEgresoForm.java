/*
 * Marzo 24, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import com.servinte.axioma.orm.ConceptosDeEgreso;
import com.servinte.axioma.orm.CuentasContablesHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
public class ConceptosDeEgresoForm extends ValidatorForm 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	transient private Logger logger = Logger.getLogger(ConceptosDeEgresoForm.class);
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	
	/**
	 * DTO 
	 */
	private ConceptosDeEgreso dto = new ConceptosDeEgreso();
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<ConceptosDeEgreso> listaDto;

	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar 
	 */
	private boolean mostrarFormularioIngreso;

	/**
	 * Parametros para ordenar 
	 */
	private String patronOrdenar;
	

	/**
	 * Parametros para ordenar 
	 */
	private String esDescendente;
	
	/**
	 * Paginador 
	 */
	private int posArray;

	/**
	 * Variable para hacer el cast con el DTO cuentas_contables 
	 */
	private String strCuentaContable;
	
	
	/**
	 * Hace un parce Long a String de la variable cuentaContable 
	 * del DTO para poder modificarla en la forma
	 * @return the strCuentaContable
	 */
	public String getStrCuentaContable() {

		//strCuentaContable = this.getDto().getCuentaContable()+"";
		if(getDto().getCuentasContables() == null)
		{
			strCuentaContable = "-1";
			return strCuentaContable;
		}
		else
		{
			strCuentaContable = this.getDto().getCuentasContables().getCodigo()+"";
			return strCuentaContable;
		}
	}


	
	/**
	 * Hace un parce String a Long de la variable cuentaContable 
	 * de la forma para poder setearla en el DTO
	 * @param strCuentaContable the strCuentaContable to set
	 */
	public void setStrCuentaContable(String strCuentaContable) {
		Long cuentaContable = Utilidades.convertirALong(strCuentaContable);
		if(cuentaContable>0)
		{
			//dto.setCuentaContable(cuentaContable);
			dto.setCuentasContables(new CuentasContablesHome().findById(cuentaContable));
		}
		else
		{
			dto.setCuentasContables(null);
		}
	}

	
	
	

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
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
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}




	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * @return the dto
	 */
	public ConceptosDeEgreso getDto() {
		return dto;
	}


	/**
	 * @param dto the dto to set
	 */
	public void setDto(ConceptosDeEgreso dto) {
		this.dto = dto;
	}


	/**
	 * @return the listaDto
	 */
	public ArrayList<ConceptosDeEgreso> getListaDto() {
		return listaDto;
	}


	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<ConceptosDeEgreso> listaDto) {
		this.listaDto = listaDto;
	}

	
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		this.dto 						= new ConceptosDeEgreso();
		this.listaDto 					= new ArrayList<ConceptosDeEgreso>();
		this.patronOrdenar 				= "";
		this.esDescendente 				= "";
		this.mostrarFormularioIngreso	= false;
		this.strCuentaContable			= "";
		this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
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
		
		if(  (estado.equals("guardar")) || (estado.equals("guardarmodificar"))  )
		{
			if(UtilidadTexto.isEmpty(dto.getCodigo()))
			{
				errores.add("error codigo", new ActionMessage("errors.required", "El c&oacute;digo"));
				setMostrarFormularioIngreso(true);
			}
		}
		
		return errores;
	}
	
	

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}
	
	
	
}


