package com.princetonsa.actionform.facturacion;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

public class CoberturaForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6724500034609002550L;

	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(CoberturaForm.class);
	
	//-------------------Atributos
	
	/**
	 * Estado en el que se encuentra el proceso 
	 * */		
	private String estado;
	
	/**
	 * Mapa de coberura
	 * */
	private HashMap coberturaMap;
	
	/**
	 * Mapa de cobertura que almacena las datos de consulta
	 * */
	private HashMap coberturaConsultaMap;
	
	/**
	 * clon de consultoriosmap al momento de cargarlos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	private HashMap coberturaEliminadosMap;
	
	/**
	 * 
	 * */
	private String patronOrdenar;
	
	/**
	 * 
	 * */
	private String ultimoPatron;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
    /**
     * indice del mapa que se desea eliminar
     */
    private int indexEliminado;	
	
	//------------------Metodos  
	private String manejoEspecial;
	
	//-------------------------------------------
    
	/**
	 * Resetea todos los atributos de la clase 
	 * */
	public void reset()
	{		
		this.coberturaMap = new HashMap();			
		this.coberturaConsultaMap = new HashMap();
		this.coberturaEliminadosMap = new HashMap();
		this.indexEliminado = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.linkSiguiente = "";
		this.ultimoPatron = "";	
		this.manejoEspecial = "";
	}	
	
	

	
	




	/**
	 * @return the manejoEspecial
	 */
	public String getManejoEspecial() {
		return manejoEspecial;
	}









	/**
	 * @param manejoEspecial the manejoEspecial to set
	 */
	public void setManejoEspecial(String manejoEspecial) {
		this.manejoEspecial = manejoEspecial;
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
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);
		logger.info("mapa desde la forma=> "+this.coberturaConsultaMap);
		
		if(estado.equals("guardar"))			
		{					 	
			int numReg=Integer.parseInt(this.coberturaMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				/*
				if(this.coberturaMap.get("codigo_"+i).toString().trim().equals(""))
				{
					errores.add("codigo",new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					if(this.coberturaMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.coberturaMap.get("codigo_"+j).toString().trim().equals(this.coberturaMap.get("codigo_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigo",new ActionMessage("errors.yaExiste","El Codigo del registro "+(i+1)));							
							}							
						}
					}	
				}
				*/
				if(this.coberturaMap.get("descripcion_"+i).toString().trim().equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Descripcion del registro "+(i+1)));
				}				
			}
		}
		
		return errores;		
	}
	
	//---------------Fin Validate 

	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void setCoberturaMap(HashMap coberturaMap) {
		this.coberturaMap = coberturaMap;
	}
	
	public void setCoberturaMap(String key, Object value) {
		this.coberturaMap.put(key, value);
	}
	
	public HashMap getCoberturaMap(){
		return coberturaMap;
	}
	
	public Object getCoberturaMap(String key) {
		return coberturaMap.get(key);
	}
	
	public void setCoberturaConsultaMap(String key, Object value){
		this.coberturaConsultaMap.put(key, value);
	}
	
	
	/**
	 * 
	 * */
	public Object getCoberturaConsultaMap(String key){
		return coberturaConsultaMap.get(key);
	}	
	
	/**
	 * @return the coberturaConsultaMap
	 */
	public HashMap getCoberturaConsultaMap() {
		return coberturaConsultaMap;
	}

	/**
	 * @param coberturaConsultaMap the coberturaConsultaMap to set
	 */
	public void setCoberturaConsultaMap(HashMap coberturaConsultaMap) {
		this.coberturaConsultaMap = coberturaConsultaMap;
	}

	public String getPatronOrdenar(){
		return patronOrdenar;
	}
	
	public void setPatronOrdenar(String patronOrdenar){
		this.patronOrdenar=patronOrdenar;
	}
	
	public String getUltimoPatron(){
		return ultimoPatron;
	}
	
	public void setUltimoPatron(String ultimoPatron){
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the consultoriosEliminadosMap
	 */
	public HashMap getCoberturaEliminadosMap() {
		return coberturaEliminadosMap;
	}

	/**
	 * @param consultoriosEliminadosMap the consultoriosEliminadosMap to set
	 */
	public void setCoberturaEliminadosMap(HashMap consultoriosEliminadosMap) {
		this.coberturaEliminadosMap = consultoriosEliminadosMap;
	}	
	
	/**
	 * @param key 
	 * */
	public Object getCoberturaEliminadosMap(String key){
		return coberturaEliminadosMap.get(key);
	}
	
	/**
	 * @param key
	 * @param value 
	 */
	public void setCoberturaEliminadosMap(String key, Object value)
	{
		this.coberturaEliminadosMap.put(key,value);
	}	
	
	/**
	 * @return the indexEliminado
	 */
	public int getIndexEliminado() {
		return indexEliminado;
	}

	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(int indexEliminado) {
		this.indexEliminado = indexEliminado;
	}
}