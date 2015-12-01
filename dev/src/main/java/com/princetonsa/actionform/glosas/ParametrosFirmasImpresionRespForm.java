package com.princetonsa.actionform.glosas;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;


public class ParametrosFirmasImpresionRespForm extends ValidatorForm {

	/**	 * MANEJO DE LA FORMA	 */
		private String estado;		//Bandera para Manejar el estado de la Forma
		private ResultadoBoolean mensaje = new ResultadoBoolean(false);	//Mensaje que informa sobre la generacion de la aplicacion 
	
	
	/**	 * VALIDACIONES DE LA FORMA	 */
		private boolean errores; 	 // Bandera para el manejo de errores en el validate - sin
		
		
	/** * DATOS DE LA FORMA */
		private HashMap frmFirmasImpresion;		// HashMap para Almacenar las Firmas en Impresion Respuesta de Glosa
		private int frmCodSecuen;				// int ke almacena el valor de la secuencia seleccionada para eliminar o modificar
		

	/**	 * RESET DE LA FORMA */
		public void reset() {
			this.frmFirmasImpresion = new HashMap();
			this.frmFirmasImpresion.put("numRegistros", "0");
			this.frmFirmasImpresion.put("tmpUsuario", "");
			this.frmFirmasImpresion.put("tmpCargo", "");
			this.frmCodSecuen = ConstantesBD.codigoNuncaValido;
		}


    /**  * VALIDACION DE CAMPOS + ERRORES */
	    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    	ActionErrors errores = new ActionErrors();
	    	
	    	// si se va a adicionar una firma
	    	if(this.estado.equals("addFirma"))  {

	    		//Se Valida el Usuario
	    		if(this.frmFirmasImpresion.get("tmpUsuario").equals("") || this.frmFirmasImpresion.get("tmpUsuario").equals(null)) {
					errores.add("Usuario ", new ActionMessage("errors.required","El Nombre de Usuario "));
					this.errores = true;
				}

	    		//Se Valida el Cargo
	    		if(( this.frmFirmasImpresion.get("tmpCargo")).equals("") || this.frmFirmasImpresion.get("tmpCargo").equals(null)) {
					errores.add("Cargo ", new ActionMessage("errors.required","El Cargo del Usuario "));
					this.errores = true;
				}
	    	}
	    	
	    	
			if(errores.isEmpty()) {
				this.errores = false;
			}

	    	return errores;
	    }
		
		
	
	/** * GETS AND SETS	 */
		
		//estado
		public String getEstado() {	return estado;	}
		public void setEstado(String estado) {	this.estado = estado;	}


		// mensaje
		public ResultadoBoolean getMensaje() {	return mensaje;	}
		public void setMensaje(ResultadoBoolean mensaje) {	this.mensaje = mensaje;	}

		
		// frmListadoBusqueda 
		public HashMap getFrmFirmasImpresion() {	return frmFirmasImpresion;		}
		public void setFrmFirmasImpresion(HashMap frmFirmasImpresion) {	this.frmFirmasImpresion = frmFirmasImpresion;	}
		public Object getFrmFirmasImpresion(String key) {	return frmFirmasImpresion.get(key);	}
		public void setFrmFirmasImpresion(String key,Object value) { this.frmFirmasImpresion.put(key, value);	}


		 // frmCodSecuen
		public int getFrmCodSecuen() {		return frmCodSecuen;		}
		public void setFrmCodSecuen(int frmCodSecuen) {		this.frmCodSecuen = frmCodSecuen;		}
}