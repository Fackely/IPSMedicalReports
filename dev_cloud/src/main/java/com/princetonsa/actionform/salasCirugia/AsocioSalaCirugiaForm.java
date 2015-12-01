package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
public class AsocioSalaCirugiaForm extends ValidatorForm 
{
	
	//-------Atributos
	
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(AsocioSalaCirugiaForm.class);
	
	
	/**
	 * HashMap de asocio
	 * */
	private HashMap asocioMap;
	
	/**
	 * HashMap de asocio Eliminados
	 * */
	private HashMap asocioEliminadoMap;
	
	/**
	 * ArrayList de Tipos Servicios
	 * */
	private ArrayList<HashMap<String,Object>> tipoServicioArray ;
	
	/**
	 * String estado
	 * */
	private String estado;
	
	/**
	 * String indexEliminado
	 * */
	private int indexEliminado;
	
	/**
	 * String linkSiguiente
	 * */
	private String linkSiguiente;
	
	/**
	 * String patronOrdenar
	 * */
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron
	 * */
	private String utilmoPatron;
	
	
	/**
	 * ArrayList mensajes
	 * */
	private ArrayList mensajesList;
	
	/**
	 * ArrayList de centros de costo
	 * */
	private HashMap centrosCosto ;
	
	//----------Metodos
	
	
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
		System.out.print("lelga al form");
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);
		boolean vacio = false;
		
		if(estado.equals("guardar"))			
		{					 	
			int numReg=Integer.parseInt(this.asocioMap.get("numRegistros")+"");
			
			for(int i=0;i<numReg;i++)
			{
				vacio = false;
				
				if(this.asocioMap.get("codigoasocio_"+i).toString().trim().equals(""))				
					vacio = true;				
				else
				{
					if(this.asocioMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.asocioMap.get("codigoasocio_"+j).toString().toUpperCase().trim().equals(this.asocioMap.get("codigoasocio_"+i).toString().toUpperCase().trim()) && (j!=i))
							{
								errores.add("codigo",new ActionMessage("errors.yaExiste","El Codigo del registro "+(i+1)));							
							}							
						}
					}	
				}
				
				if(this.asocioMap.get("nombreasocio_"+i).toString().trim().equals(""))				
					vacio = true;								
				
				if(this.asocioMap.get("tiposservicio_"+i).toString().trim().equals("") ||
						this.asocioMap.get("tiposservicio_"+i).toString().trim().equals(ConstantesBD.codigoNuncaValido+""))				
					vacio = true;
				else
				{					
					if(this.asocioMap.get("tiposservicio_"+i).toString().trim().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
					{
						if(this.asocioMap.get("participacir_"+i).toString().equals(""))
							errores.add("descripcion",new ActionMessage("errors.required","El Indicador de Participación del registro "+(i+1)));						
					}					
					else
					{						
						this.asocioMap.put("participacir_"+i,ConstantesBD.acronimoNo);
					}
				}
				
				if(vacio)				
					errores.add("descripcion",new ActionMessage("errors.required","Los campos Código, Nombre,Tipo de Servicio son requeridos para el Registro "+(i+1)));
			}
		}
		
		return errores;		
	}
	
	
	
	public void reset()
	{
		this.asocioMap = new HashMap();
		this.asocioEliminadoMap = new HashMap();
		this.tipoServicioArray = new ArrayList<HashMap<String,Object>>();
		this.indexEliminado = 0;
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.utilmoPatron = "";
		this.mensajesList = new ArrayList();
		this.centrosCosto = new HashMap();
		this.centrosCosto.put("numRegistros", 0);
	}
		
	/**
	 * @return the asocioMap
	 */
	public HashMap getAsocioMap() {
		return asocioMap;
	}

	/**
	 * @param asocioMap the asocioMap to set
	 */
	public void setAsocioMap(HashMap asocioMap) {
		this.asocioMap = asocioMap;
	}
	
	
	/**
	 * @return the asocioMap
	 */
	public Object getAsocioMap(String key) {
		return asocioMap.get(key);
	}

	/**
	 * @param asocioMap the asocioMap to set
	 */
	public void setAsocioMap(String key, Object value) {
		this.asocioMap.put(key, value);
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
	 * @return the tipoServicioArray
	 */
	public ArrayList<HashMap<String, Object>> getTipoServicioArray() {
		return tipoServicioArray;
	}

	/**
	 * @param tipoServicioArray the tipoServicioArray to set
	 */
	public void setTipoServicioArray(
			ArrayList<HashMap<String, Object>> tipoServicioArray) {
		this.tipoServicioArray = tipoServicioArray;
	}

	/**
	 * @return the asocioEliminadoMap
	 */
	public HashMap getAsocioEliminadoMap() {
		return asocioEliminadoMap;
	}

	/**
	 * @param asocioEliminadoMap the asocioEliminadoMap to set
	 */
	public void setAsocioEliminadoMap(HashMap asocioEliminadoMap) {
		this.asocioEliminadoMap = asocioEliminadoMap;
	}
	
	/**
	 * @return the asocioEliminadoMap
	 */
	public Object getAsocioEliminadoMap(String key) {
		return asocioEliminadoMap.get(key);
	}

	/**
	 * @param asocioEliminadoMap the asocioEliminadoMap to set
	 */
	public void setAsocioEliminadoMap(String key, Object value) {
		this.asocioEliminadoMap.put(key, value);
	}
	
	/**
	 * @return the utilmoPatron
	 */
	public String getUtilmoPatron() {
		return utilmoPatron;
	}

	/**
	 * @param utilmoPatron the utilmoPatron to set
	 */
	public void setUtilmoPatron(String utilmoPatron) {
		this.utilmoPatron = utilmoPatron;
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
	/**
	 * @return the mensajesList
	 */
	public ArrayList getMensajesList() {
		return mensajesList;
	}

	/**
	 * @param mensajesList the mensajesList to set
	 */
	public void setMensajesList(ArrayList mensajesList) {
		this.mensajesList = mensajesList;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	
}