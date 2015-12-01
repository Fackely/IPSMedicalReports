package com.princetonsa.actionform.administracion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

import util.ResultadoBoolean;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class ConceptosRetencionForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConceptosRetencionForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Variable que almacena el Codigo Concepto a ingresar 	
	 */
	private String codConcepto;
	
	/**
	 * Variable que almacena la descripcion del Concepto
	 */
	private String descConcepto;
	
	/**
	 * Variable que almacena el Codigo Interfaz
	 */
	private String codInterfaz;

	public ArrayList<DtoTiposRetencion> listaTiposRetencion;
	
	public ArrayList<DtoConceptosRetencion> listaConceptosRetencion;
	
	public DtoConceptosRetencion dtoConceptos;
	
	private String consecutivoTipoRet;
	
	private String descripcion;
	
	private int numeroRegTipoRet;
	
	private String cuentaRet;
	
	private String descCuentaRet;
	
	private String cuentaDB;
	
	private String descCuentaDB;
		
	private String cuentaCR;

	private String descCuentaCR;
	
	private String consulta;
	
	private String indiceDetalle;
	
	private String consecutivoConceptoRet;
	
	private String modificar;
	
	private String modificarTipoRet;
	
	private int conseTipoRet;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.codConcepto="";
		this.descConcepto="";
		this.codInterfaz="";
		this.listaTiposRetencion=new ArrayList<DtoTiposRetencion>();
		this.listaConceptosRetencion=new ArrayList<DtoConceptosRetencion>();
		this.dtoConceptos= new DtoConceptosRetencion();
		this.consecutivoTipoRet="";
		this.descripcion="";
		this.numeroRegTipoRet=0;
		this.cuentaRet="";
		this.cuentaDB="";
		this.cuentaCR="";
		this.descCuentaRet="";
		this.descCuentaDB="";
		this.descCuentaCR="";
		this.consulta="f";
		this.indiceDetalle="";
		this.consecutivoConceptoRet="";
		this.modificar="";
		this.modificarTipoRet="t";
		this.conseTipoRet=0;
	}

	

	public String getDescCuentaRet() {
		return descCuentaRet;
	}



	public void setDescCuentaRet(String descCuentaRet) {
		this.descCuentaRet = descCuentaRet;
	}



	public String getDescCuentaDB() {
		return descCuentaDB;
	}



	public void setDescCuentaDB(String descCuentaDB) {
		this.descCuentaDB = descCuentaDB;
	}



	public String getDescCuentaCR() {
		return descCuentaCR;
	}



	public void setDescCuentaCR(String descCuentaCR) {
		this.descCuentaCR = descCuentaCR;
	}



	public int getConseTipoRet() {
		return conseTipoRet;
	}


	public void setConseTipoRet(int conseTipoRet) {
		this.conseTipoRet = conseTipoRet;
	}


	public String getModificarTipoRet() {
		return modificarTipoRet;
	}


	public void setModificarTipoRet(String modificarTipoRet) {
		this.modificarTipoRet = modificarTipoRet;
	}


	public String getModificar() {
		return modificar;
	}


	public void setModificar(String modificar) {
		this.modificar = modificar;
	}


	public String getConsecutivoConceptoRet() {
		return consecutivoConceptoRet;
	}


	public void setConsecutivoConceptoRet(String consecutivoConceptoRet) {
		this.consecutivoConceptoRet = consecutivoConceptoRet;
	}


	public String getIndiceDetalle() {
		return indiceDetalle;
	}


	public void setIndiceDetalle(String indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}


	public String getConsulta() {
		return consulta;
	}


	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}


	public ArrayList<DtoConceptosRetencion> getListaConceptosRetencion() {
		return listaConceptosRetencion;
	}


	public void setListaConceptosRetencion(
			ArrayList<DtoConceptosRetencion> listaConceptosRetencion) {
		this.listaConceptosRetencion = listaConceptosRetencion;
	}


	public String getCuentaRet() {
		return cuentaRet;
	}


	public String getCuentaCR() {
		return cuentaCR;
	}


	public void setCuentaCR(String cuentaCR) {
		this.cuentaCR = cuentaCR;
	}


	public void setCuentaRet(String cuentaRet) {
		this.cuentaRet = cuentaRet;
	}


	public String getCuentaDB() {
		return cuentaDB;
	}


	public void setCuentaDB(String cuentaDB) {
		this.cuentaDB = cuentaDB;
	}



	public int getNumeroRegTipoRet() {
		return numeroRegTipoRet;
	}




	public void setNumeroRegTipoRet(int numeroRegTipoRet) {
		this.numeroRegTipoRet = numeroRegTipoRet;
	}




	public DtoConceptosRetencion getDtoConceptos() {
		return dtoConceptos;
	}


	public void setDtoConceptos(DtoConceptosRetencion dtoConceptos) {
		this.dtoConceptos = dtoConceptos;
	}


	public String getCodInterfaz() {
		return codInterfaz;
	}


	public void setCodInterfaz(String codInterfaz) {
		this.codInterfaz = codInterfaz;
	}


	public String getDescConcepto() {
		return descConcepto;
	}


	public void setDescConcepto(String descConcepto) {
		this.descConcepto = descConcepto;
	}


	public String getCodConcepto() {
		return codConcepto;
	}


	public void setCodConcepto(String codConcepto) {
		this.codConcepto = codConcepto;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	

	public ArrayList<DtoTiposRetencion> getListaTiposRetencion() {
		return listaTiposRetencion;
	}


	public void setListaTiposRetencion(
			ArrayList<DtoTiposRetencion> listaTiposRetencion) {
		this.listaTiposRetencion = listaTiposRetencion;
	}


	public String getConsecutivoTipoRet() {
		return consecutivoTipoRet;
	}


	public void setConsecutivoTipoRet(String consecutivoTipoRet) {
		this.consecutivoTipoRet = consecutivoTipoRet;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("guardarNuevoReg"))
        {
        	if(this.consecutivoTipoRet.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Codigo Tipo Retencion "));
        	if(this.codConcepto.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Codigo Concepto "));
        	if(this.descConcepto.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Descripcion Concepto "));
        	if(!errores.isEmpty())
	        		this.estado="nuevoRegistro";
        }
        
        if(this.estado.equals("guardarModificar"))
        {
        	if(this.consecutivoTipoRet.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Codigo Tipo Retencion "));
        	if(this.codConcepto.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Codigo Concepto "));
        	if(this.descConcepto.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Descripcion Concepto "));
        	if(!errores.isEmpty())
        		this.estado="modificarRegistro";
        }
        
        return errores;
    }

}