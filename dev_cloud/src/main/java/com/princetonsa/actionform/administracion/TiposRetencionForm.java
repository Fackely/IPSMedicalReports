package com.princetonsa.actionform.administracion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;

import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
/**
 * @author Víctor Hugo Gómez L.
 */

public class TiposRetencionForm extends ValidatorForm 
{
	/**
     * Objeto para manejar los logs de esta clase
     */
    private Logger logger = Logger.getLogger(TiposRetencionForm.class);
    
    /**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * posicion
	 */
	private int posTipoRetencion;
	
	/**
	 * ArrayList Tipos de Retencion
	 */
	private ArrayList<DtoTiposRetencion> dtoTiposRetencion = new ArrayList<DtoTiposRetencion>();
	
	/**
	 * ArrayList Grupo de Servicios Activos y pertenecinetes a una Intitucion Especifica  
	 */
	private ArrayList<HashMap<String, Object>> arrayGrupoSer = new ArrayList<HashMap<String, Object>>(); 
	
	/**
	 * ArrayList Clase Inventario Activos y Perteneciente a una Institucion Especifica
	 */
	private ArrayList<HashMap<String, Object>> arrayClaseInv = new ArrayList<HashMap<String, Object>>();
	
	/**
	 * ArrayList Concepto Factura Varia Activos y Perteneciente a una Institucion Especifica
	 */
	private ArrayList<HashMap<String, Object>> arrayConcFV = new ArrayList<HashMap<String, Object>>();
	
	//****************************************
	// Atributos Tipo Retencion Grupo Servicio
	/**
	 * Codigo Grupo Servicio
	 */
	private int codigoGrpServicio;
	/**
	 * Descripcion Grupo Servicio
	 */
	private String descripcionGrupoSer;
	/**
	 * Acronimo Grupo Servicio
	 */
	private String acronimoGrupoSer;
	/**
	 * posicion Detalle Grupo Servicio
	 */
	private int posicionGrpSer;
	//****************************************
	
	//****************************************
	// Atributos Tipo Retencion Clase Inventario
	/**
	 * Codigo Clase Inventario
	 */
	private int codigoClaseInv;
	/**
	 * Nombre Clase inventario
	 */
	private String nombreClaseInv;
	
	/**
	 * posicion Detalle Clase Inventario
	 */
	private int posicionClaseInv;
	//****************************************
	
	//************************************************
	// Atributos Tipo Retencion Concepto factura Varia
	/**
	 * Codigo Concepto Factura Varia
	 */
	private int codigoConcFraVaria;
	/**
	 * Descripcion Concepto Factura Varia
	 */
	private String nombreConcFraVaria;
	
	/**
	 * posicion Detalle Concepto Factura Varia
	 */
	private int posicionConcFV;
	//************************************************
	
	/**
	 * mensaje de operacion
	 */
	private boolean tieneError;
	
	/**
	 * Patron de Ordenamiento
	 */
	private String patronOrdenar;
	
	/**
     * resetea los atributos del form
     */
	public void reset()
    {
		this.posTipoRetencion = ConstantesBD.codigoNuncaValido;
		this.dtoTiposRetencion = new ArrayList<DtoTiposRetencion>();
		this.arrayGrupoSer = new ArrayList<HashMap<String, Object>>();
		this.arrayClaseInv = new ArrayList<HashMap<String, Object>>();
		this.arrayConcFV = new ArrayList<HashMap<String, Object>>();
		//****************************************
		// Atributos Tipo Retencion Grupo Servicio
		this.codigoGrpServicio = ConstantesBD.codigoNuncaValido;
		this.descripcionGrupoSer = "";
		this.acronimoGrupoSer = "";
		this.posicionGrpSer = ConstantesBD.codigoNuncaValido;
		//****************************************
		
		//****************************************
		// Atributos Tipo Retencion Clase Inventario
		this.codigoClaseInv = ConstantesBD.codigoNuncaValido;
		this.nombreClaseInv = "";
		this.posicionClaseInv = ConstantesBD.codigoNuncaValido;
		//****************************************
		
		//************************************************
		// Atributos Tipo Retencion Concepto factura Varia
		this.codigoConcFraVaria = ConstantesBD.codigoNuncaValido;
		this.nombreConcFraVaria = "" ;
		this.posicionConcFV = ConstantesBD.codigoNuncaValido;
		//************************************************
		this.tieneError = false;
    }

	/**
	    * Valida  las propiedades que han sido establecidas para este request HTTP,
	    * y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	    * validación encontrados. Si no se encontraron errores de validación,
	    * retorna <code>null</code>.
	    * @param mapping el mapeado usado para elegir esta instancia
	    * @param request el <i>servlet request</i> que está siendo procesado en este momento
	    * @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	    * o <code>null</code> si no se encontraron errores.
	    */
	    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	    {
	    	ActionErrors errores = new ActionErrors();
	        if(estado.equals("grabar"))
	        {
	        	for(int i=0;i<dtoTiposRetencion.size();i++)
	        	{
	        		for(int j=0;j<dtoTiposRetencion.size();j++)
	        		{
	        			if(this.dtoTiposRetencion.get(i).getEliminar().equals(ConstantesBD.acronimoNo)
	        					&&(this.dtoTiposRetencion.get(i).getModificar().equals(ConstantesBD.acronimoSi) || this.dtoTiposRetencion.get(i).getIngresar().equals(ConstantesBD.acronimoSi)))
	        			{
		        			if(j!=i)
		        			{
		        				if(this.dtoTiposRetencion.get(j).getEliminar().equals(ConstantesBD.acronimoNo))
		        				{
			        				if(this.dtoTiposRetencion.get(i).getCodigo().equalsIgnoreCase(this.dtoTiposRetencion.get(j).getCodigo()))
			        					errores.add("codigo", new ActionMessage("errors.notEspecific","El Campo Código de la Posición["+i+"] está Repetido. "));
			        				if(this.dtoTiposRetencion.get(i).getSigla().equalsIgnoreCase(this.dtoTiposRetencion.get(j).getSigla()))
			        					errores.add("sigla", new ActionMessage("errors.notEspecific","El Campo Sigla de la Posición["+i+"] está Repetido. "));
			        				if(this.dtoTiposRetencion.get(i).getCodigoInterfaz().equalsIgnoreCase(this.dtoTiposRetencion.get(j).getCodigoInterfaz()))
			        					errores.add("codigo", new ActionMessage("errors.notEspecific","El Campo Código Interfaz de la Posición["+i+"] está Repetido. "));
		        				}
		        			}
	        			}else
	        				j=this.dtoTiposRetencion.size()+1;
	        		}
	        		
	        		if(this.dtoTiposRetencion.get(i).getEliminar().equals(ConstantesBD.acronimoNo))
	        		{
		        		if(this.dtoTiposRetencion.get(i).getCodigo().equals(""))
		        			errores.add("codigo", new ActionMessage("errors.required","El Campo Código de la Posición["+i+"] "));
		        		if(this.dtoTiposRetencion.get(i).getCodigo().length()>10)
		        			errores.add("codigo",new ActionMessage("errors.maxlength","Código de la Posición["+i+"] ","10"));
		        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.dtoTiposRetencion.get(i).getCodigo()))
		        			errores.add("codigo",new ActionMessage("errors.caracteresInvalidos","El Campo Código de la Posición["+i+"] "));
		        		
		        		if(this.dtoTiposRetencion.get(i).getDescripcion().equals(""))
		        			errores.add("descripcion", new ActionMessage("errors.required","El Campo Descripción de la Posición["+i+"] "));
		        		if(this.dtoTiposRetencion.get(i).getDescripcion().length()>128)
		        			errores.add("descripcion",new ActionMessage("errors.maxlength","Descripción de la Posición["+i+"] ","128"));
		        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.dtoTiposRetencion.get(i).getDescripcion()))
		        			errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Descripción de la Posición["+i+"] "));
		        		
		        		if(this.dtoTiposRetencion.get(i).getSigla().equals(""))
		        			errores.add("sigla", new ActionMessage("errors.required","El Campo Sigla de la Posición["+i+"] "));
		        		if(this.dtoTiposRetencion.get(i).getSigla().length()>10)
		        			errores.add("sigla",new ActionMessage("errors.maxlength","Sigla de la Posición["+i+"] ","10"));
		        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.dtoTiposRetencion.get(i).getSigla()))
		        			errores.add("sigla",new ActionMessage("errors.caracteresInvalidos","El Campo Sigla de la Posición["+i+"] "));
		        		
		        		if(this.dtoTiposRetencion.get(i).getCodigoInterfaz().equals(""))
		        			errores.add("codigo interfaz", new ActionMessage("errors.required","El Campo Código Interfaz de la Posición["+i+"] "));
		        		if(this.dtoTiposRetencion.get(i).getCodigoInterfaz().length()>10)
		        			errores.add("codigo interfaz",new ActionMessage("errors.maxlength","Código Interfaz de la Posición["+i+"] ","10"));
		        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.dtoTiposRetencion.get(i).getCodigoInterfaz()))
		        			errores.add("codigo interfaz",new ActionMessage("errors.caracteresInvalidos","El Campo Código Interfaz de la Posición["+i+"] "));
	        		}
	        	}
	        }
	    	return errores;
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
	 * @return the dtoTiposRetencion
	 */
	public ArrayList<DtoTiposRetencion> getDtoTiposRetencion() {
		return dtoTiposRetencion;
	}

	/**
	 * @param dtoTiposRetencion the dtoTiposRetencion to set
	 */
	public void setDtoTiposRetencion(ArrayList<DtoTiposRetencion> dtoTiposRetencion) {
		this.dtoTiposRetencion = dtoTiposRetencion;
	}

	/**
	 * @return the posTipoRetencion
	 */
	public int getPosTipoRetencion() {
		return posTipoRetencion;
	}

	/**
	 * @param posTipoRetencion the posTipoRetencion to set
	 */
	public void setPosTipoRetencion(int posTipoRetencion) {
		this.posTipoRetencion = posTipoRetencion;
	}

	/**
	 * @return the arrayGrupoSer
	 */
	public ArrayList<HashMap<String, Object>> getArrayGrupoSer() {
		return arrayGrupoSer;
	}

	/**
	 * @param arrayGrupoSer the arrayGrupoSer to set
	 */
	public void setArrayGrupoSer(ArrayList<HashMap<String, Object>> arrayGrupoSer) {
		this.arrayGrupoSer = arrayGrupoSer;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoGrpServicio() {
		return codigoGrpServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoGrpServicio(int codigoGrpServicio) {
		this.codigoGrpServicio = codigoGrpServicio;
	}

	/**
	 * @return the descripcionGrupoSer
	 */
	public String getDescripcionGrupoSer() {
		return descripcionGrupoSer;
	}

	/**
	 * @param descripcionGrupoSer the descripcionGrupoSer to set
	 */
	public void setDescripcionGrupoSer(String descripcionGrupoSer) {
		this.descripcionGrupoSer = descripcionGrupoSer;
	}

	/**
	 * @return the acronimoGrupoSer
	 */
	public String getAcronimoGrupoSer() {
		return acronimoGrupoSer;
	}

	/**
	 * @param acronimoGrupoSer the acronimoGrupoSer to set
	 */
	public void setAcronimoGrupoSer(String acronimoGrupoSer) {
		this.acronimoGrupoSer = acronimoGrupoSer;
	}
	
	public void resetTRGrupoServicio()
	{
		this.codigoGrpServicio=ConstantesBD.codigoNuncaValido;
		this.descripcionGrupoSer="";
		this.acronimoGrupoSer="";
		this.arrayGrupoSer.clear();
	}

	/**
	 * @return the posicionGrpSer
	 */
	public int getPosicionGrpSer() {
		return posicionGrpSer;
	}

	/**
	 * @param posicionGrpSer the posicionGrpSer to set
	 */
	public void setPosicionGrpSer(int posicionGrpSer) {
		this.posicionGrpSer = posicionGrpSer;
	}

	/**
	 * @return the arrayClaseInv
	 */
	public ArrayList<HashMap<String, Object>> getArrayClaseInv() {
		return arrayClaseInv;
	}

	/**
	 * @param arrayClaseInv the arrayClaseInv to set
	 */
	public void setArrayClaseInv(ArrayList<HashMap<String, Object>> arrayClaseInv) {
		this.arrayClaseInv = arrayClaseInv;
	}

	/**
	 * @return the codigoClaseInv
	 */
	public int getCodigoClaseInv() {
		return codigoClaseInv;
	}

	/**
	 * @param codigoClaseInv the codigoClaseInv to set
	 */
	public void setCodigoClaseInv(int codigoClaseInv) {
		this.codigoClaseInv = codigoClaseInv;
	}

	/**
	 * @return the nombreClaseInv
	 */
	public String getNombreClaseInv() {
		return nombreClaseInv;
	}

	/**
	 * @param nombreClaseInv the nombreClaseInv to set
	 */
	public void setNombreClaseInv(String nombreClaseInv) {
		this.nombreClaseInv = nombreClaseInv;
	}

	/**
	 * @return the posicionClaseInv
	 */
	public int getPosicionClaseInv() {
		return posicionClaseInv;
	}

	/**
	 * @param posicionClaseInv the posicionClaseInv to set
	 */
	public void setPosicionClaseInv(int posicionClaseInv) {
		this.posicionClaseInv = posicionClaseInv;
	}
	
	public void resetTRClaseInventario()
	{
		this.codigoClaseInv=ConstantesBD.codigoNuncaValido;
		this.nombreClaseInv="";
		this.arrayClaseInv.clear();
	}

	/**
	 * @return the arrayConcFV
	 */
	public ArrayList<HashMap<String, Object>> getArrayConcFV() {
		return arrayConcFV;
	}

	/**
	 * @param arrayConcFV the arrayConcFV to set
	 */
	public void setArrayConcFV(ArrayList<HashMap<String, Object>> arrayConcFV) {
		this.arrayConcFV = arrayConcFV;
	}

	/**
	 * @return the codigoConcFraVaria
	 */
	public int getCodigoConcFraVaria() {
		return codigoConcFraVaria;
	}

	/**
	 * @param codigoConcFraVaria the codigoConcFraVaria to set
	 */
	public void setCodigoConcFraVaria(int codigoConcFraVaria) {
		this.codigoConcFraVaria = codigoConcFraVaria;
	}

	/**
	 * @return the nombreConcFraVaria
	 */
	public String getNombreConcFraVaria() {
		return nombreConcFraVaria;
	}

	/**
	 * @param nombreConcFraVaria the nombreConcFraVaria to set
	 */
	public void setNombreConcFraVaria(String nombreConcFraVaria) {
		this.nombreConcFraVaria = nombreConcFraVaria;
	}

	/**
	 * @return the posicionConcFV
	 */
	public int getPosicionConcFV() {
		return posicionConcFV;
	}

	/**
	 * @param posicionConcFV the posicionConcFV to set
	 */
	public void setPosicionConcFV(int posicionConcFV) {
		this.posicionConcFV = posicionConcFV;
	}
	
	public void resetTRConceptoFraVaria()
	{
		this.codigoConcFraVaria=ConstantesBD.codigoNuncaValido;
		this.nombreConcFraVaria="";
		this.arrayConcFV.clear();
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
	 * @return the tieneError
	 */
	public boolean isTieneError() {
		return tieneError;
	}

	/**
	 * @param tieneError the tieneError to set
	 */
	public void setTieneError(boolean tieneError) {
		this.tieneError = tieneError;
	}
	
}
