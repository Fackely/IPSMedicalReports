package com.princetonsa.actionform.interfaz;

/*
 * Abril 26, 2010
 */

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.interfaz.DtoCuentasInterfazEmpresasInsti;
import com.servinte.axioma.orm.CuentaGeneralInterfaz;
import com.servinte.axioma.orm.EmpresasInstitucion;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
@SuppressWarnings("serial")
public class CuentaGeneralInterfazForm extends ValidatorForm 
{
	
	/* ATRIBUTOS*/
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	
	/**
	 * DTO 
	 */
	private CuentaGeneralInterfaz dto;
	
	
	/**
	 * Indica si se debe mostrar el registro de la cuenta encontrada
	 */
	private boolean mostrarCuenta;

	
	/**
	 * Lista de Empresas Institucion
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresasPorInstitucion;
	
	/**
	 * Lista de cuentas por Empresas Institucion
	 */
	private ArrayList<DtoCuentasInterfazEmpresasInsti> listaCuentasInterfazEmpresasInsti;

	
	/**
	 * Valor del parametro de la institucion. Indica si es multiempresa o no
	 */
	private boolean intitucionMultiEmpresa;
	
	
	
	/* SETS Y GETS */
	
	public boolean isMostrarCuenta() {
		return mostrarCuenta;
	}

	public void setMostrarCuenta(boolean mostrarCuenta) {
		this.mostrarCuenta = mostrarCuenta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public CuentaGeneralInterfaz getDto() {
		return dto;
	}

	public void setDto(CuentaGeneralInterfaz dto) {
		this.dto = dto;
	}
	
	public ArrayList<EmpresasInstitucion> getListaEmpresasPorInstitucion() {
		return listaEmpresasPorInstitucion;
	}

	public void setListaEmpresasPorInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresasPorInstitucion) {
		this.listaEmpresasPorInstitucion = listaEmpresasPorInstitucion;
	}
	
	public boolean isIntitucionMultiEmpresa() {
		return intitucionMultiEmpresa;
	}

	public void setIntitucionMultiEmpresa(boolean intitucionMultiEmpresa) {
		this.intitucionMultiEmpresa = intitucionMultiEmpresa;
	}
	
	public ArrayList<DtoCuentasInterfazEmpresasInsti> getListaCuentasInterfazEmpresasInsti() {
		return listaCuentasInterfazEmpresasInsti;
	}

	public void setListaCuentasInterfazEmpresasInsti(
			ArrayList<DtoCuentasInterfazEmpresasInsti> listaCuentasInterfazEmpresasInsti) {
		this.listaCuentasInterfazEmpresasInsti = listaCuentasInterfazEmpresasInsti;
	}
	

	/* METODOS */
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto								= new CuentaGeneralInterfaz();
		this.mostrarCuenta 						= false;
		this.listaEmpresasPorInstitucion 		= new ArrayList<EmpresasInstitucion>();
		this.intitucionMultiEmpresa				= false;
		this.listaCuentasInterfazEmpresasInsti 	= new ArrayList<DtoCuentasInterfazEmpresasInsti>();
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
		return new ActionErrors();
	}

	
}
