package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class CentrosCostoEntidadesSubcontratadasForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(CentrosCostoEntidadesSubcontratadasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Mapa que almacena los centros de atencion  
	 */
	private HashMap<String,Object> centrosAtencionMap= new HashMap<String,Object>();
	
	/**
	 * Variable que almacena el centro de atencion seleccionado
	 */
	private int centroAtencionSel;
	
	/**
	 * Mapa que almacena los Centros De Costo parametrizados segun el Centro de Atencion seleccionado
	 */
	private HashMap<String,Object> centrosCostoPorCAtencionMap= new HashMap<String,Object>();
	
	/**
	 *Variable que almacena el centro de costo seleccionado 
	 */
	private int centroCostoSel;
	
	/**
	 * Mapa que almacena las entidades subcobcontratadas	
	 */
	private HashMap<String, Object> entidadesSubcontratadasMap = new HashMap<String, Object>();
	
	/**
	 * Variable que almacena la entidad subcontratada seleccionada
	 */
	private int entidadSubcontratadaSel;
	
	/**
	 * Variable que almacenala prioridad por centro atencion, centro de costo y entidad subcontratada 
	 */
	private String prioridad;
	
	/**
	 * Variable que indica di usuarios diferentes a profesionales de la salud pueden responder las ordenes medicas generadas
	 */
	private String respOtrosUsuariosCk;
	
	private int indiceModificar;
	
	/**
	 * Mapa que almacena la informacion a guardar modificar o consultar
	 */
	private HashMap<String,Object> centrosCostoEntiSub = new HashMap<String,Object>();
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	private boolean guardo;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.centrosAtencionMap= new HashMap<String,Object>();
		this.centrosAtencionMap.put("numRegistros", "0");
		this.centrosCostoPorCAtencionMap= new HashMap<String,Object>();
		this.centrosCostoPorCAtencionMap.put("numRegistros", "0");
		this.entidadesSubcontratadasMap= new HashMap<String, Object>();
		this.centrosCostoEntiSub= new HashMap<String,Object>();
		this.centrosCostoEntiSub.put("numRegistros", "0");
		this.indiceModificar=0;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.centroAtencionSel=-1;
		this.guardo=true;
	}
	

	public boolean isGuardo() {
		return guardo;
	}


	public void setGuardo(boolean guardo) {
		this.guardo = guardo;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public int getIndiceModificar() {
		return indiceModificar;
	}

	public void setIndiceModificar(int indiceModificar) {
		this.indiceModificar = indiceModificar;
	}

	public void resetNuevo(int codigoInstitucion)
	{
		this.centroCostoSel=-1;
		this.entidadSubcontratadaSel=-1;
		this.prioridad="";
		this.respOtrosUsuariosCk="N";
		
	}

	public HashMap<String, Object> getEntidadesSubcontratadasMap() {
		return entidadesSubcontratadasMap;
	}


	public void setEntidadesSubcontratadasMap(
			HashMap<String, Object> entidadesSubcontratadasMap) {
		this.entidadesSubcontratadasMap = entidadesSubcontratadasMap;
	}

	public Object getEntidadesSubcontratadasMap(String key) {
		return this.entidadesSubcontratadasMap.get(key);
	}
	
	public void setEntidadesSubcontratadasMap(String key, Object value){
		this.entidadesSubcontratadasMap.put(key, value);
	}

	public HashMap<String, Object> getCentrosCostoEntiSub() {
		return centrosCostoEntiSub;
	}


	public void setCentrosCostoEntiSub(HashMap<String, Object> centrosCostoEntiSub) {
		this.centrosCostoEntiSub = centrosCostoEntiSub;
	}
	
	public Object getCentrosCostoEntiSub(String key) {
		return this.centrosCostoEntiSub.get(key);
	}
	
	public void setCentrosCostoEntiSub(String key, Object value){
		this.centrosCostoEntiSub.put(key, value);
	}


	public String getRespOtrosUsuariosCk() {
		return respOtrosUsuariosCk;
	}


	public void setRespOtrosUsuariosCk(String respOtrosUsuariosCk) {
		this.respOtrosUsuariosCk = respOtrosUsuariosCk;
	}


	public String getPrioridad() {
		return prioridad;
	}

	
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	

	public int getEntidadSubcontratadaSel() {
		return entidadSubcontratadaSel;
	}


	public void setEntidadSubcontratadaSel(int entidadSubcontratadaSel) {
		this.entidadSubcontratadaSel = entidadSubcontratadaSel;
	}


	public int getCentroCostoSel() {
		return centroCostoSel;
	}


	public void setCentroCostoSel(int centroCostoSel) {
		this.centroCostoSel = centroCostoSel;
	}


	public HashMap<String, Object> getCentrosCostoPorCAtencionMap() {
		return centrosCostoPorCAtencionMap;
	}


	public void setCentrosCostoPorCAtencionMap(
			HashMap<String, Object> centrosCostoPorCAtencionMap) {
		this.centrosCostoPorCAtencionMap = centrosCostoPorCAtencionMap;
	}

	public Object getCentrosCostoPorCAtencionMap(String key) {
		return this.centrosCostoPorCAtencionMap.get(key);
	}
	
	public void setCentrosCostoPorCAtencionMap(String key, Object value){
		this.centrosCostoPorCAtencionMap.put(key, value);
	}
		
	public int getCentroAtencionSel() {
		return centroAtencionSel;
	}


	public void setCentroAtencionSel(int centroAtencionSel) {
		this.centroAtencionSel = centroAtencionSel;
	}


	public HashMap<String, Object> getCentrosAtencionMap() {
		return centrosAtencionMap;
	}


	public void setCentrosAtencionMap(HashMap<String, Object> centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	public Object getCentrosAtencionMap(String key){
		return this.centrosAtencionMap.get(key);
	}

	public void setCentrosAtencionMap(String key, Object value){
		this.centrosAtencionMap.put(key, value);
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
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);  
        
        if(this.estado.equals("guardarModificar") || this.estado.equals("guardarNuevo"))
        {
        	if(this.centroAtencionSel == -1)
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El centro de atencion "));
        	}
        	if(this.centroCostoSel == -1)
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El centro de costo que ejecuta "));
        	}   
        	if(this.entidadSubcontratadaSel == -1)
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La entidad subcontratada "));
        	}  
        	if(this.prioridad.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La prioridad "));
        	}  
        	
        	if(!errores.isEmpty())		
        	{
	        	if(this.estado.equals("guardarNuevo"))
	        		this.estado="nuevoRegistro";
	        	else
	        		this.estado="modificarRegistro";
    		}
        }
        
        return errores;
    }
}
