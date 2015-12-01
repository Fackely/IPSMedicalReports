package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;

public class DetalleGlosasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DetalleGlosasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Mapa para los valores de la consulta de la GLosa
	 */
	private HashMap informacionGlosa;
	
	/**
	 * Variable para modificar la glosa entidad del registro de la Busqueda
	 */
	private String glosaentidad;
	
	/**
	 * Variable para modificar la fecha notificacion del registro de la Busqueda
	 */
	private String fechaNotificacion;
	
	/**
	 * Variable para modificar el valor del registro de la Busqueda
	 */
	private String valor;
	
	/**
	 * Variable para modificar las observaciones de la Busqueda
	 */
	private String observaciones;

	/**
	 * Variable para el manejo de la fecha registro de la Glosa
	 */
	private String fechaRegistro;
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String convenio;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String convenioP;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String contratoP;
	
	/**
	 * Variable que almacena el numero de mapas en el arraylist
	 */
	private int numMapas;
		
	/**
	 * Variable que almacena el numero de mapas en el arraylist Principal
	 */
	private int numMapasP;
	
	/**
	 * Arreglo para almacenar los Convenios de la Pagina Principal
	 */
	private ArrayList<HashMap<String, Object>> arregloConveniosPrincipal = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa para almacenar el detalle de la Glosa
	 */
	private HashMap mapaDetalleGlosa;
	
	/**
	 * Mapa para almacenar los conceptos de la Factura Glosa
	 */
	private HashMap conceptosDetalleFacturaMap;
	/**
	 * Variable para almacenar el codigo de la factura
	 */
	private String codFactura;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String indicativoglosa;
	
	private ArrayList<HashMap> conceptosFactura= new ArrayList<HashMap>();
		
	public void reset(int codigoInstitucion)
	{		
		this.estado="";
		this.fechaNotificacion="";
		this.fechaRegistro="";
		this.glosaentidad="";
		this.observaciones="";
		this.valor="";
		this.informacionGlosa= new HashMap();
		this.informacionGlosa.put("numRegistros", "0");
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.arregloConveniosPrincipal = new ArrayList<HashMap<String,Object>>();
		this.convenio="";
		this.convenioP="";
		this.contratoP="";
		this.numMapas=0;
		this.numMapasP=0;
		this.mapaDetalleGlosa= new HashMap();
		this.mapaDetalleGlosa.put("numRegistros", "0");
		this.conceptosDetalleFacturaMap= new HashMap();
		this.conceptosDetalleFacturaMap.put("numRegistros", "0");
		this.codFactura="";
		this.indicativoglosa="";
		this.conceptosFactura= new ArrayList<HashMap>();
	}



	public ArrayList<HashMap> getConceptosFactura() {
		return conceptosFactura;
	}



	public void setConceptosFactura(ArrayList<HashMap> conceptosFactura) {
		this.conceptosFactura = conceptosFactura;
	}



	public String getIndicativoglosa() {
		return indicativoglosa;
	}



	public void setIndicativoglosa(String indicativoglosa) {
		this.indicativoglosa = indicativoglosa;
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}	
	
	/**
	 * @return the informacionGlosa
	 */
	public HashMap getInformacionGlosa() {
		return informacionGlosa;
	}

	/**
	 * @param informacionGlosa the informacionGlosa to set
	 */
	public void setInformacionGlosa(HashMap informacionGlosa) {
		this.informacionGlosa = informacionGlosa;
	}
	
	public Object getInformacionGlosa(String key){
		return this.informacionGlosa.get(key);
	}
	
	public void setInformacionGlosa(String key, Object value){
		this.informacionGlosa.put(key, value);
	}	

	/**
	 * @return the fechaNotificacion
	 */
	public String getFechaNotificacion() {
		return fechaNotificacion;
	}

	/**
	 * @param fechaNotificacion the fechaNotificacion to set
	 */
	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return the arregloConvenios
	 */
	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}

	/**
	 * @param arregloConvenios the arregloConvenios to set
	 */
	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the numMapas
	 */
	public int getNumMapas() {
		return numMapas;
	}

	/**
	 * @param numMapas the numMapas to set
	 */
	public void setNumMapas(int numMapas) {
		this.numMapas = numMapas;
	}

	/**
	 * @return the convenioP
	 */
	public String getConvenioP() {
		return convenioP;
	}

	/**
	 * @param convenioP the convenioP to set
	 */
	public void setConvenioP(String convenioP) {
		this.convenioP = convenioP;
	}

	/**
	 * @return the numMapasP
	 */
	public int getNumMapasP() {
		return numMapasP;
	}

	/**
	 * @param numMapasP the numMapasP to set
	 */
	public void setNumMapasP(int numMapasP) {
		this.numMapasP = numMapasP;
	}

	/**
	 * @return the arregloConveniosPrincipal
	 */
	public ArrayList<HashMap<String, Object>> getArregloConveniosPrincipal() {
		return arregloConveniosPrincipal;
	}

	/**
	 * @param arregloConveniosPrincipal the arregloConveniosPrincipal to set
	 */
	public void setArregloConveniosPrincipal(
			ArrayList<HashMap<String, Object>> arregloConveniosPrincipal) {
		this.arregloConveniosPrincipal = arregloConveniosPrincipal;
	}

	
	/**
	 * @return the glosaentidad
	 */
	public String getGlosaentidad() {
		return glosaentidad;
	}

	/**
	 * @param glosaentidad the glosaentidad to set
	 */
	public void setGlosaentidad(String glosaentidad) {
		this.glosaentidad = glosaentidad;
	}
	/**
	 * @return the contratoP
	 */
	public String getContratoP() {
		return contratoP;
	}

	/**
	 * @param contratoP the contratoP to set
	 */
	public void setContratoP(String contratoP) {
		this.contratoP = contratoP;
	}	

	/**
	 * @return the mapaDetalleGlosa
	 */
	public HashMap getMapaDetalleGlosa() {
		return mapaDetalleGlosa;
	}

	/**
	 * @param mapaDetalleGlosa the mapaDetalleGlosa to set
	 */
	public void setMapaDetalleGlosa(HashMap mapaDetalleGlosa) {
		this.mapaDetalleGlosa = mapaDetalleGlosa;
	}

	public Object getMapaDetalleGlosa(String key){
		return this.mapaDetalleGlosa.get(key);
	}
	
	public void setMapaDetalleGlosa(String key, Object value){
		this.mapaDetalleGlosa.put(key, value);
	}
	
	public HashMap getConceptosDetalleFacturaMap() {
		return conceptosDetalleFacturaMap;
	}	

	public void setConceptosDetalleFacturaMap(HashMap conceptosDetalleFacturaMap) {
		this.conceptosDetalleFacturaMap = conceptosDetalleFacturaMap;
	}
	
	public Object getConceptosDetalleFacturaMap (String key){
		return this.conceptosDetalleFacturaMap.get(key);
	}
	
	public void setConceptosDetalleFacturaMap (String key, Object value){
		this.conceptosDetalleFacturaMap.put(key, value);
	}

	public String getCodFactura() {
		return codFactura;
	}

	public void setCodFactura(String codFactura) {
		this.codFactura = codFactura;
	}



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request); 
        
        if(estado.equals("detalleGlosa"))
        {
        	if(this.informacionGlosa.get("codglosa").equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Informacion de la Glosa"));
        }        
        return errores;
    }	
}