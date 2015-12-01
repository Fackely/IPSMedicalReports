package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.ViasAereas;

/**
 * 
 * @author wilson
 *
 */
public class ViasAereasForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 * */
	private String codigoPeticion;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapa;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaListado;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTiposDispositivo;
	
	/**
	 * 
	 */
	private boolean esConsulta;
	
	/**
	 * 
	 */
	private boolean estaBD;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private int tipoDispositivo;
	
	/**
	 * 
	 */
	private int index;
	
	/**
	 * 
	 */
	private int codigoViaAerea;
	
	
	
	
	
	
	
	//*****************************************************************************************
	//***** CAMBIOS JUSTIFICACION NO POS **************
	
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();
	
	
	/**
	 * numero de justificacion
	 */
	private int numjus=0;
	
	/**
	 * numero solicitud
	 */
	private int solicitud=0;
	
	
	/**
	 * mapa de alertas justificacion pendiente
	 */
	private HashMap justificacionNoPosMap=new HashMap();
	
	/**
	 * Strign hiddens resultadoi de la generacin de hiddens
	 */
	private String hiddens="";
	
	
	//********************************************************************************************************
	
	
	
	//Campos Funcionalidad Dummy
	
	/**
	 * String esSinEncabezado
	 * */
	private String esSinEncabezado;
	
	/**
	 * Indicador para mostrar informacion solo cuando posea datos
	 * */
	private InfoDatosString mostrarDatosInfo = new InfoDatosString("","","",false);
	
	/**
	 * indicador de ocultar menu
	 * */
	private boolean ocultarMenu = false;
	
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset1()
    {
    	this.mapa= new HashMap<Object, Object>();
    	this.mapa.put("numRegistros", "0");
    	this.mapaListado= new HashMap<Object, Object>();
    	this.mapaListado.put("numRegistros", "0");
    	this.mapaTiposDispositivo= new HashMap<Object, Object>();
    	this.mapaTiposDispositivo.put("numRegistros", "0");
    	
    	this.estaBD=false;
    	this.fecha=UtilidadFecha.getFechaActual();
    	this.hora=UtilidadFecha.getHoraActual();
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.tipoDispositivo=ConstantesBD.codigoNuncaValido;
    	this.codigoViaAerea=ConstantesBD.codigoNuncaValido;
    	
    	if(!this.estado.equals("resumen"))
    	{	
    		this.esSinEncabezado = "";
        	this.ocultarMenu = false;
    	}	
    	
    	
		//***** formato justificacion no pos
		this.justificacionMap=new HashMap();
		this.justificacionMap.put("numRegistros", 0);
		this.medicamentosNoPosMap=new HashMap();
		this.medicamentosNoPosMap.put("numRegistros", 0);
		this.medicamentosPosMap=new HashMap();
		this.medicamentosPosMap.put("numRegistros", 0);
		this.sustitutosNoPosMap=new HashMap();
		this.sustitutosNoPosMap.put("numRegistros", 0);
		this.numjus=0;
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosDefinitivos.put("numRegistros", 0);
		this.diagnosticosPresuntivos=new HashMap();
		this.diagnosticosPresuntivos.put("numRegistros", 0);
		this.justificacionNoPosMap=new HashMap();
		this.justificacionNoPosMap.put("numRegistros", 0);
		this.hiddens="";
		//*****
		
		mostrarDatosInfo = new InfoDatosString("","","",false);
    }
    
    
    
    
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.mapa= new HashMap<Object, Object>();
    	this.mapa.put("numRegistros", "0");
    	this.mapaListado= new HashMap<Object, Object>();
    	this.mapaListado.put("numRegistros", "0");
    	this.mapaTiposDispositivo= new HashMap<Object, Object>();
    	this.mapaTiposDispositivo.put("numRegistros", "0");
    	
    	this.estaBD=false;
    	this.fecha=UtilidadFecha.getFechaActual();
    	this.hora=UtilidadFecha.getHoraActual();
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.tipoDispositivo=ConstantesBD.codigoNuncaValido;
    	this.codigoViaAerea=ConstantesBD.codigoNuncaValido;
    	
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
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        
        if(estado.equals("guardar"))
        {
        	errores=validacion(errores);
        }	
        
        if(!errores.isEmpty())
    	{
    		if(estado.equals("guardar") && !this.getEstaBD())	
    			this.estado="nuevo";
    		else if(estado.equals("guardar") && this.getEstaBD())	
    			this.estado="modificar";
    	}
        return errores;
    }
    
    /**
     * 
     * @param errores
     * @return
     */
    public ActionErrors validacion(ActionErrors errores)
    {
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFecha()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHora()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
    	}
    	
    	//si no existen errores entonces se valida que la fecha - hora de inicio sea >= fecha/hora ingreso a la sala y <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFecha(), this.getHora()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
    		}
    		if(errores.isEmpty())
    		{
    			SolicitudesCx solicitudCx = new SolicitudesCx();		
    			solicitudCx.cargarEncabezadoSolicitudCx(this.numeroSolicitud+"");
    			
    			
    			if(!UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()))
    			{	
	    			if(!UtilidadFecha.compararFechas( this.getFecha(), this.getHora(), solicitudCx.getFechaIngresoSala(), solicitudCx.getHoraIngresoSala()).isTrue())
	        		{
	    				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Ingreso Sala "+solicitudCx.getFechaIngresoSala()+" "+solicitudCx.getHoraIngresoSala()));
	        		}
    			}
    			else
    			{
    				errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
    			}
    		}
    	}
    	
    	if(errores.isEmpty())
    	{
    		if(ViasAereas.existeViaAereaFechaHora(this.getFecha(), this.getHora(), this.codigoViaAerea))
    		{
    			errores.add("", new ActionMessage("errors.yaExiste", "La via aerea con fecha y hora "+this.getFecha()+" "+this.getHora()));
    		}
    	}
    	
    	//tipo dispositivo es requerido
    	if(this.tipoDispositivo<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "El tipo dispositivo"));
    	}
    	
    	//al menos un articulo es requerido
    	boolean existeArticulos=false;
    	for(int w=0; w<Integer.parseInt(this.mapa.get("numRegistros")+"");w++)
    	{
    		if(!UtilidadTexto.getBoolean(this.getMapa("fueeliminadoarticulo_"+w)+""))
    			existeArticulos=true;
    	}
    	
    	if(!existeArticulos)
    	{
    		errores.add("", new ActionMessage("errors.required","Al menos un articulo"));
    	}
    	else
    	{
    		for(int w=0; w<Integer.parseInt(this.mapa.get("numRegistros")+"");w++)
        	{
        		if(!UtilidadTexto.getBoolean(this.getMapa("fueeliminadoarticulo_"+w)+""))
        		{
        			//la cantidad debe ser requerida
        			if(!UtilidadTexto.isNumber(this.mapa.get("cantidad_"+w)+""))
        			{
        				errores.add("", new ActionMessage("errors.integerMayorQue","La cantidad del articulo "+this.getMapa("codigoarticulo_"+w), "0"));
        			}
        			else
        			{
        				if(Integer.parseInt(this.mapa.get("cantidad_"+w)+"")<=0)
        				{
        					errores.add("", new ActionMessage("errors.integerMayorQue","La cantidad del articulo "+this.getMapa("codigoarticulo_"+w), "0"));
        				}
        			}
        			//la via de insercion tambien es requerida
        			if(Integer.parseInt(this.mapa.get("viainsercion_"+w)+"")<=0)
        			{
        				errores.add("", new ActionMessage("errors.required", "La via de insercion del articulo "+this.getMapa("codigoarticulo_"+w)));
        			}
        			if(this.mapa.get("tipoposarticulo_"+w).equals("NOPOS") || this.mapa.get("tipoposarticulo_"+w).equals("f"))
        			{
        				if(!this.justificacionMap.containsKey(this.getMapa("codigoarticulo_"+w)+"_yajustifico") || this.justificacionMap.get(this.getMapa("codigoarticulo_"+w)+"_yajustifico").equals("false"))
        				{
	        				errores.add("", new ActionMessage("errors.required", "La justificacion no pos del articulo "+this.getMapa("codigoarticulo_"+w)));
	        			}
        			}
        		}
        	}
    	}
    	
    	return errores;
    }

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the estaBD
	 */
	public boolean getEstaBD() {
		return estaBD;
	}

	/**
	 * @param estaBD the estaBD to set
	 */
	public void setEstaBD(boolean estaBD) {
		this.estaBD = estaBD;
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the mapa
	 */
	public HashMap<Object, Object> getMapa() {
		return mapa;
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(HashMap<Object, Object> mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return the mapa
	 */
	public Object getMapa(Object key) 
	{	
		return mapa.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(Object key, Object value) {
		this.mapa.put(key, value);
	}

	
	
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the esConsulta
	 */
	public boolean getEsConsulta() {
		return esConsulta;
	}

	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * @return the mapaListado
	 */
	public HashMap<Object, Object> getMapaListado() {
		return mapaListado;
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(HashMap<Object, Object> mapaListado) {
		this.mapaListado = mapaListado;
	}
    
	/**
	 * @return the mapaListado
	 */
	public Object getMapaListado(Object key) {
		return mapaListado.get(key);
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(Object key, Object value) {
		this.mapaListado.put(key, value);
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the mapaTiposDispositivo
	 */
	public HashMap<Object, Object> getMapaTiposDispositivo() {
		return mapaTiposDispositivo;
	}

	/**
	 * @param mapaTiposDispositivo the mapaTiposDispositivo to set
	 */
	public void setMapaTiposDispositivo(HashMap<Object, Object> mapaTiposDispositivo) {
		this.mapaTiposDispositivo = mapaTiposDispositivo;
	}

	/**
	 * @return the tipoDispositivo
	 */
	public int getTipoDispositivo() {
		return tipoDispositivo;
	}

	/**
	 * @param tipoDispositivo the tipoDispositivo to set
	 */
	public void setTipoDispositivo(int tipoDispositivo) {
		this.tipoDispositivo = tipoDispositivo;
	}

	/**
	 * @return the codigoViaAerea
	 */
	public int getCodigoViaAerea() {
		return codigoViaAerea;
	}

	/**
	 * @param codigoViaAerea the codigoViaAerea to set
	 */
	public void setCodigoViaAerea(int codigoViaAerea) {
		this.codigoViaAerea = codigoViaAerea;
	}

	/**
	 * @return the esSinEncabezado
	 */
	public String getEsSinEncabezado() {
		return esSinEncabezado;
	}

	/**
	 * @param esSinEncabezado the esSinEncabezado to set
	 */
	public void setEsSinEncabezado(String esSinEncabezado) {
		this.esSinEncabezado = esSinEncabezado;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public HashMap getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the justificacionMap
	 */
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionMap(HashMap justificacionMap) {
		this.justificacionMap = justificacionMap;
	}

	/**
	 * @return the justificacionNoPosMap
	 */
	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	/**
	 * @param justificacionNoPosMap the justificacionNoPosMap to set
	 */
	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}

	/**
	 * @return the medicamentosNoPosMap
	 */
	public HashMap getMedicamentosNoPosMap() {
		return medicamentosNoPosMap;
	}

	/**
	 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
	 */
	public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
		this.medicamentosNoPosMap = medicamentosNoPosMap;
	}

	/**
	 * @return the medicamentosPosMap
	 */
	public HashMap getMedicamentosPosMap() {
		return medicamentosPosMap;
	}

	/**
	 * @param medicamentosPosMap the medicamentosPosMap to set
	 */
	public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
		this.medicamentosPosMap = medicamentosPosMap;
	}

	/**
	 * @return the numjus
	 */
	public int getNumjus() {
		return numjus;
	}

	/**
	 * @param numjus the numjus to set
	 */
	public void setNumjus(int numjus) {
		this.numjus = numjus;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the sustitutosNoPosMap
	 */
	public HashMap getSustitutosNoPosMap() {
		return sustitutosNoPosMap;
	}

	/**
	 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
	 */
	public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
		this.sustitutosNoPosMap = sustitutosNoPosMap;
	}




	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		return hiddens;
	}




	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}

	/**
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}
	
	public InfoDatosString getMostrarDatosInfo(){
		return mostrarDatosInfo;
	}	
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo){
		this.mostrarDatosInfo = mostrarDatosInfo;
	}	
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
	}




	/**
	 * @return the ocultarMenu
	 */
	public boolean isOcultarMenu() {
		return ocultarMenu;
	}




	/**
	 * @param ocultarMenu the ocultarMenu to set
	 */
	public void setOcultarMenu(boolean ocultarMenu) {
		this.ocultarMenu = ocultarMenu;
	}
    
}
