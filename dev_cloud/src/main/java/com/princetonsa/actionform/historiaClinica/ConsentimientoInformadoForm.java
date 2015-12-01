package com.princetonsa.actionform.historiaClinica;

import com.princetonsa.dto.historiaClinica.DtoMotivosNoConsentimientoInformado;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;
import com.princetonsa.dto.odontologia.DtoPrograma;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;


/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsentimientoInformadoForm extends ValidatorForm
{

	/**
	 * Variable estado para manejar el estado de la funcionalidad
	 */
	private String estado="";
	
	/**
	 * Declaracion de variables de Consentimiento Informado
	 */
	private HashMap consentimientoInfMap;
	private HashMap detalleConsentInfMap;
	private HashMap consentimientoInfEliminadoMap;
	private HashMap detalleEliminadoMap;
	String indexEliminado;
	String indexConsentInf;
	String linkSiguiente;
	String patronOrdenar;
	String ultimoPatronOrdenar;
	String indexEliminadoDetalle;
	String indexDetalle; 
	String indeximpresion;
	ArrayList<HashMap<String, Object>> grupoServicios;
	private HashMap formatos;
	protected FormFile archivo;
	
	private ArrayList<DtoPrograma> programas;
	
	/**
	 * 
	 */
	private String tipoConsentimiento;
	
	//*********************
	//Atributos de Busqueda de Consentimiento Informado
	
	/**
	 * Codigo del servicio por el cual se realiza 
	 * la busqueda del consentimientio informado
	 * */
	private String codigoServicioBusqueda;
	
	/**
	 * 
	 */
	private String codigoPrograma;
	
	/**
	 * 
	 */
	private String codigosPHP;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	private boolean procesoExitoso;
	
	/**
	 * 
	 */
	private String planTratamiento;
	
	/**
	 * 
	 */
	private String codigoCita;
	
	
	/**
	 * HashMap listado de Consentimientos Informados
	 * */
	private HashMap listadoConsentimientoInfMap;
	
	
	/**
	 * Index del listado de consentimiento informacion
	 * */
	private String indexListadoConsentimientoInf;
	
	/**
	 * 
	 */
	private ArrayList<DtoConsentimientoInformadoOdonto> consentimientoOdonto;
	
	/**
	 * 
	 */
	private ArrayList<DtoMotivosNoConsentimientoInformado> motivos;
	
	public FormFile getArchivo() {
		return archivo;
	}




	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}




	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		if (estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.consentimientoInfMap.get("numRegistros")+"");
			
			for (int i=0;i<numReg;i++)
			{
				if(this.consentimientoInfMap.get("descripcion_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Descripcion del registro "+(i+1)));
				}
			    
				if(this.consentimientoInfMap.get("nombrearchivo_"+i).toString().trim().equals("")||this.consentimientoInfMap.get("nombreoriginal_"+i).toString().trim().equals(""))
				{
						errores.add("path",new ActionMessage("errors.required","El Archivo del registro "+(i+1)));
				}
				
			}
		}
		if(estado.equals("guardarConsentimientoOdonto"))
		{
			for(DtoConsentimientoInformadoOdonto dto:this.consentimientoOdonto)
			{
				if(dto.getRecibioConsentimiento().isEmpty())
				{
					errores.add("",new ActionMessage("errors.required","Recibio Consentimiento del programa "+dto.getDescripcionPrograma()));
				}
				if(dto.getRecibioConsentimiento().equals(ConstantesBD.acronimoNo)&&dto.getMotivoNoConsentimiento()<=0)
				{
					errores.add("",new ActionMessage("errors.required","El motivo de no recibir Consentimiento del programa "+dto.getDescripcionPrograma()));
				}
			}
		}
		
		return errores;
	}
	
	
	

	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	
	
	public ArrayList<HashMap<String, Object>> getGrupoServicios() {
		return grupoServicios;
	}




	public void setGrupoServicios(ArrayList<HashMap<String, Object>> grupoServicios) {
		this.grupoServicios = grupoServicios;
	}




	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	/**
	 * Getters y Setters para nuestros atributos
	 * */
	public HashMap getConsentimientoInfMap() {
		return consentimientoInfMap;
	}
	
	public void setConsentimientoInfMap(HashMap consentimientoInfMap) {
		this.consentimientoInfMap = consentimientoInfMap;
	}
	
	public HashMap getDetalleConsentInfMap() {
		return detalleConsentInfMap;
	}
	
	public void setDetalleConsentInfMap(HashMap detalleConsentInfMap) {
		this.detalleConsentInfMap = detalleConsentInfMap;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	/**
	 * Metodos adicionales para el manejo de los Hashmap
	 */
	
	public Object getConsentimientoInfMap (String key)
	{
		return consentimientoInfMap.get(key);
	}
	
	public void setConsentimientoInfMap (String key,Object value)
	{
		this.consentimientoInfMap.put(key, value);
	}
	
	
	public Object getDetalleConsentInfMap(String key)
	{
		return detalleConsentInfMap.get(key);
	}
	
	public void setDetalleConsentInfMap(String key,Object value)
	{
		this.detalleConsentInfMap.put(key, value);
	}
	
	public Object getConsentimientoInfEliminadoMap(String key) {
		return consentimientoInfEliminadoMap.get(key);
	}

	public void setConsentimientoInfEliminadoMap(String key, Object value) {
		this.consentimientoInfEliminadoMap.put(key, value);
	}
	
	/**
	 * Metodo para limpiar los datos de la forma
	 */
	public void reset ()
	{
		this.consentimientoInfMap = new HashMap();
		
		this.consentimientoInfEliminadoMap = new HashMap();
		this.consentimientoInfEliminadoMap.put("numRegistros", "0");
		this.formatos = new HashMap ();
		this.formatos.put("numRegistros", "0");
		this.detalleConsentInfMap = new HashMap();
		this.detalleConsentInfMap.put("numRegistros", "0");
		this.indeximpresion = "";
		this.detalleEliminadoMap = new HashMap();
		this.detalleEliminadoMap.put("numRegistros", "0");
		this.estado = "";
		this.indexEliminado = "";
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.indexConsentInf = "";
		this.indexEliminadoDetalle ="";
		this.indexDetalle = "";
		this.programas=new ArrayList<DtoPrograma>();
		this.codigoPrograma="";
		this.codigosPHP="";
		this.mensaje=new ResultadoBoolean(false);
		this.procesoExitoso=false;
		this.consentimientoOdonto=new ArrayList<DtoConsentimientoInformadoOdonto>();
		this.motivos=new ArrayList<DtoMotivosNoConsentimientoInformado>();
		
	}
	
		
	/**
	 * Reset los atributos de impresion 
	 * */
	public void resetBusqueda()
	{
		this.listadoConsentimientoInfMap = new HashMap();
	}

	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}

	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}

	public HashMap getConsentimientoInfEliminadoMap() {
		return consentimientoInfEliminadoMap;
	}

	public void setConsentimientoInfEliminadoMap(
			HashMap consentimientoInfEliminadoMap) {
		this.consentimientoInfEliminadoMap = consentimientoInfEliminadoMap;
	}

	public String getIndexConsentInf() {
		return indexConsentInf;
	}

	public void setIndexConsentInf(String indexConsentInf) {
		this.indexConsentInf = indexConsentInf;
	}




	public HashMap getDetalleEliminadoMap() {
		return detalleEliminadoMap;
	}
	
	public Object getDetalleEliminadoMap (String key)
	{
		return detalleEliminadoMap.get(key);
	}



	public void setDetalleEliminadoMap(HashMap detalleEliminadoMap) {
		this.detalleEliminadoMap = detalleEliminadoMap;
	}
	
	public void setDetalleEliminadoMap (String key, Object value)
	{
		this.detalleEliminadoMap.put(key, value);
	}




	public String getIndexEliminadoDetalle() {
		return indexEliminadoDetalle;
	}




	public void setIndexEliminadoDetalle(String indexEliminadoDetalle) {
		this.indexEliminadoDetalle = indexEliminadoDetalle;
	}




	public String getIndexDetalle() {
		return indexDetalle;
	}




	public void setIndexDetalle(String indexDetalle) {
		this.indexDetalle = indexDetalle;
	}




	/**
	 * @return the codigoServicioBusqueda
	 */
	public String getCodigoServicioBusqueda() {
		return codigoServicioBusqueda;
	}




	/**
	 * @param codigoServicioBusqueda the codigoServicioBusqueda to set
	 */
	public void setCodigoServicioBusqueda(String codigoServicioBusqueda) {
		this.codigoServicioBusqueda = codigoServicioBusqueda;
	}




	/**
	 * @return the listadoConsentimientoInfMap
	 */
	public HashMap getListadoConsentimientoInfMap() {
		return listadoConsentimientoInfMap;
	}




	/**
	 * @param listadoConsentimientoInfMap the listadoConsentimientoInfMap to set
	 */
	public void setListadoConsentimientoInfMap(HashMap listadoConsentimientoInfMap) {
		this.listadoConsentimientoInfMap = listadoConsentimientoInfMap;
	}




	/**
	 * @return the listadoConsentimientoInfMap
	 */
	public Object getListadoConsentimientoInfMap(String key) {
		return listadoConsentimientoInfMap.get(key);
	}




	/**
	 * @param listadoConsentimientoInfMap the listadoConsentimientoInfMap to set
	 */
	public void setListadoConsentimientoInfMap(String key, Object value) {
		this.listadoConsentimientoInfMap.put(key, value);
	}




	/**
	 * @return the indexListadoConsentimientoInf
	 */
	public String getIndexListadoConsentimientoInf() {
		return indexListadoConsentimientoInf;
	}




	/**
	 * @param indexListadoConsentimientoInf the indexListadoConsentimientoInf to set
	 */
	public void setIndexListadoConsentimientoInf(
			String indexListadoConsentimientoInf) {
		this.indexListadoConsentimientoInf = indexListadoConsentimientoInf;
	}




	public HashMap getFormatos() {
		return this.formatos;
	}

	public Object getFormatos(String key) {
		return this.formatos.get(key);
	}


	public void setFormatos(HashMap formatos) {
		this.formatos = formatos;
	}
	
	public void setFormatos(String key,Object value) {
		this.formatos.put(key, value);
	}




	public String getIndeximpresion() {
		return indeximpresion;
	}




	public void setIndeximpresion(String indeximpresion) {
		this.indeximpresion = indeximpresion;
	}




	public String getTipoConsentimiento() {
		return tipoConsentimiento;
	}




	public void setTipoConsentimiento(String tipoConsentimiento) {
		this.tipoConsentimiento = tipoConsentimiento;
	}




	public ArrayList<DtoPrograma> getProgramas() {
		return programas;
	}




	public void setProgramas(ArrayList<DtoPrograma> programas) {
		this.programas = programas;
	}




	public String getCodigoPrograma() {
		return codigoPrograma;
	}




	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}




	public String getPlanTratamiento() {
		return planTratamiento;
	}




	public void setPlanTratamiento(String planTratamiento) {
		this.planTratamiento = planTratamiento;
	}






	public ResultadoBoolean getMensaje() {
		return mensaje;
	}




	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}






	public ArrayList<DtoConsentimientoInformadoOdonto> getConsentimientoOdonto() {
		return consentimientoOdonto;
	}




	public void setConsentimientoOdonto(
			ArrayList<DtoConsentimientoInformadoOdonto> consentimientoOdonto) {
		this.consentimientoOdonto = consentimientoOdonto;
	}




	public String getCodigosPHP() {
		return codigosPHP;
	}




	public void setCodigosPHP(String codigosPHP) {
		this.codigosPHP = codigosPHP;
	}




	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}




	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}




	public ArrayList<DtoMotivosNoConsentimientoInformado> getMotivos() {
		return motivos;
	}




	public void setMotivos(ArrayList<DtoMotivosNoConsentimientoInformado> motivos) {
		this.motivos = motivos;
	}




	public String getCodigoCita() {
		return codigoCita;
	}




	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
	}




}