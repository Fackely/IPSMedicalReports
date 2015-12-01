package com.princetonsa.dto.capitacion;

import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;

/**
 * Esta clase se encarga de contener los datos de la validación de 
 * los niveles de autorización de una autorización automática
 * 
 * @author Angela Maria Aguirre
 * @since 1/12/2010
 */
public class DTOValidacionNivelAutoAutomatica {
	
	private DTONivelAutorizacion dtoNivelAutorizacion;
	private DtoEntidadSubcontratada dtoEntidadSubcontratada;
	private boolean generarAutorizacion;
	private ActionErrors erroresValidacion;
	
	/** * lista Centros Costo de las validaciones (punto 4) */
	private ArrayList<DtoCentroCosto> listaCentrosCosto;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOValidacionNivelAutoAutomatica() 
	{
		this.dtoNivelAutorizacion = new DTONivelAutorizacion();
		this.dtoEntidadSubcontratada = new DtoEntidadSubcontratada();
		this.generarAutorizacion = false;
		this.listaCentrosCosto = new ArrayList<DtoCentroCosto>();
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoEntidadSubcontratada
	
	 * @return retorna la variable dtoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getDtoEntidadSubcontratada() {
		return dtoEntidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoEntidadSubcontratada
	
	 * @param valor para el atributo dtoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoEntidadSubcontratada(
			DtoEntidadSubcontratada dtoEntidadSubcontratada) {
		this.dtoEntidadSubcontratada = dtoEntidadSubcontratada;
	}
	public boolean isGenerarAutorizacion() {
		return generarAutorizacion;
	}
	public void setGenerarAutorizacion(boolean generarAutorizacion) {
		this.generarAutorizacion = generarAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoNivelAutorizacion
	
	 * @return retorna la variable dtoNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public DTONivelAutorizacion getDtoNivelAutorizacion() {
		return dtoNivelAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoNivelAutorizacion
	
	 * @param valor para el atributo dtoNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoNivelAutorizacion(DTONivelAutorizacion dtoNivelAutorizacion) {
		this.dtoNivelAutorizacion = dtoNivelAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo erroresValidacion
	 * @param erroresValidacion errores en el proceso de validacion autorizaciones  
	 */
	public void setErroresValidacion(ActionErrors erroresValidacion) {
		this.erroresValidacion = erroresValidacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo erroresValidacion
	 * @return erroresValidacion errores en el proceso de validacion autorizaciones 
	 */
	public ActionErrors getErroresValidacion() {
		return erroresValidacion;
	}


	/**
	 * @return valor de listaCentrosCosto
	 */
	public ArrayList<DtoCentroCosto> getListaCentrosCosto() {
		return listaCentrosCosto;
	}


	/**
	 * @param listaCentrosCosto el listaCentrosCosto para asignar
	 */
	public void setListaCentrosCosto(ArrayList<DtoCentroCosto> listaCentrosCosto) {
		this.listaCentrosCosto = listaCentrosCosto;
	}	
	
}
