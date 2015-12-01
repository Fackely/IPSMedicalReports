package com.princetonsa.actionform.facturasVarias;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.Utilidades;

import com.princetonsa.dto.facturasVarias.DtoDeudor;

/**
 * 
 * @author Juan Sebastian Castaño
 * Clase form de la funcionalidad Deudores
 */

public class DeudoresForm  extends ActionForm{
	
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	/**
	 * variable que guarda el codigo del deudor a modificar
	 */
	String codigoDeudorModificar;
	
	

	/**
	 * Estado
	 */
	String estado;
	
	

	/**
	 * Codigo tercero seleccionado
	 */
	int codigoTerceroSeleccionado;
	

	

	
	/**
	 * String tipoDeudorSeleccionado
	 */
	String tipoDeudorSeleccionado;
	

	
	

	
	

	//***************CAMBIO ANEXO 811**************************+
	private DtoDeudor deudor;
	/**
	 * Usado para log tipo archivo
	 */
	private DtoDeudor deudorAnterior;
	

	/**
	 * limpiador de objeto
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.codigoDeudorModificar = "";
		this.tipoDeudorSeleccionado = "";
		this.codigoTerceroSeleccionado = 0;
		
		this.deudor = new DtoDeudor();
		this.deudorAnterior = new DtoDeudor();
		
		
	}
	
	/**
	 * @return the deudor
	 */
	public DtoDeudor getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(DtoDeudor deudor) {
		this.deudor = deudor;
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
	 * @return the codigoTerceroSeleccionado
	 */
	public int getCodigoTerceroSeleccionado() {
		return codigoTerceroSeleccionado;
	}

	/**
	 * @param codigoTerceroSeleccionado the codigoTerceroSeleccionado to set
	 */
	public void setCodigoTerceroSeleccionado(int codigoTerceroSeleccionado) {
		this.codigoTerceroSeleccionado = codigoTerceroSeleccionado;
	}

	



	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	 {
	    ActionErrors errores = new ActionErrors();
	    
	    if(this.estado.equals("guardar") || this.estado.equals("modificarDeudor") )
	     {
	    	
	    	if(this.deudor.getObservaciones().length()>4000)
	    	{
	    		errores.add("",new ActionMessage("errors.maxlength","El campo Observaciones, ","4000"));
	    	}
	    	// Anexo 809
	    	if(!this.deudor.getDiasVencimientoFac().equals(""))
	    	{
		    	if(Utilidades.convertirAEntero(this.deudor.getDiasVencimientoFac())<0 
		    			|| Utilidades.convertirAEntero(this.deudor.getDiasVencimientoFac())>359)
		    	{
		    		errores.add("",new ActionMessage("errors.range","El campo Número Días Vencimiento de Facturación ","0","359"));
		    	}
	    	}
	    	// Anexo 809
	     }
	    
	    if(!errores.isEmpty())
		 {
	    	 // el formulario ha sido modificado, cargar los nuevos valores		    
	    	if (this.estado.equals("guardar") )
	    	{
	    		this.estado = "consultarDeudor";
	    	}
	    	else if (this.estado.equals("modificarDeudor") )
	    	{
	    		this.estado = "consultarDeudorSeleccionado";
	    	}
		 }
	   
	    return errores;
	 }


	/**
	 * @return the tipoDeudorSeleccionado
	 */
	public String getTipoDeudorSeleccionado() {
		return tipoDeudorSeleccionado;
	}

	/**
	 * @param tipoDeudorSeleccionado the tipoDeudorSeleccionado to set
	 */
	public void setTipoDeudorSeleccionado(String tipoDeudorSeleccionado) {
		this.tipoDeudorSeleccionado = tipoDeudorSeleccionado;
	}

	
	
	


	

	/**
	 * @return the codigoDeudorModificar
	 */
	public String getCodigoDeudorModificar() {
		return codigoDeudorModificar;
	}

	/**
	 * @param codigoDeudorModificar the codigoDeudorModificar to set
	 */
	public void setCodigoDeudorModificar(String codigoDeudorModificar) {
		this.codigoDeudorModificar = codigoDeudorModificar;
	}

	/**
	 * @return the deudorAnterior
	 */
	public DtoDeudor getDeudorAnterior() {
		return deudorAnterior;
	}

	/**
	 * @param deudorAnterior the deudorAnterior to set
	 */
	public void setDeudorAnterior(DtoDeudor deudorAnterior) {
		this.deudorAnterior = deudorAnterior;
	}

	
	

}
	


