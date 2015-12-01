package com.princetonsa.actionform.seccionesparametrizables;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.AccesosVascularesHA;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class AccesosVascularesHAForm extends ValidatorForm  
{
	Logger logger = Logger.getLogger(AccesosVascularesHAForm.class);
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
	private int articulo;
	
	/**
	 * 
	 */
	private String descArticulo;
	
	/**
	 * 
	 */
	private int cantidadArticulo;
	
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaListado;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTiposAccesosVasculares;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaLocalizacionesMap;
	
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
	private int tipoAccesoVascular;
	
	/**
	 * 
	 */
	private int localizacion;
	
	
	/**
	 * 
	 */
	private int index;
	
	
	/**
	 * 
	 */
	private double codigoPKAccesoVAscularHA;
	
	/**
	 * 
	 */
	private boolean generoConsumo;
	
	/**
	 * 
	 * */
	private String codigoDetMateQx;
	
	/**
	 * 
	 */
	private boolean generoOrden;
	
//	*****************************************************************************************
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
	
	/**
	 * 
	 */
	private String espos;
	
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
	 * Indicador de ocultar menu
	 * */
	private boolean ocultarMenu = false;
	
	
	//********************************************************************************************************
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.articulo=ConstantesBD.codigoNuncaValido;
    	this.descArticulo="";
    	this.cantidadArticulo=1;//por defecto debe ser uno
    	
    	this.mapaListado= new HashMap<Object, Object>();
    	this.mapaListado.put("numRegistros", "0");
    	this.mapaTiposAccesosVasculares= new HashMap<Object, Object>();
    	this.mapaTiposAccesosVasculares.put("numRegistros", "0");
    	
    	this.mapaLocalizacionesMap= new HashMap<Object, Object>();
    	this.mapaLocalizacionesMap.put("numRegistros", "0");
    	
    	this.estaBD=false;
    	this.fecha=UtilidadFecha.getFechaActual();
    	this.hora=UtilidadFecha.getHoraActual();
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.tipoAccesoVascular=ConstantesBD.codigoNuncaValido;
    	this.localizacion=ConstantesBD.codigoNuncaValido;
    	this.codigoPKAccesoVAscularHA=ConstantesBD.codigoNuncaValidoDouble; 
    	this.generoConsumo=false;
    	this.generoOrden=false;
    	this.codigoDetMateQx = ConstantesBD.codigoNuncaValido+"";
    	
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
		this.espos="";
		//*****
		
		this.mostrarDatosInfo = new InfoDatosString("","","",false);
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
    		if(AccesosVascularesHA.existeAccesoVascularHAFechaHora(this.getFecha(), this.getHora(), this.codigoPKAccesoVAscularHA))
    		{
    			errores.add("", new ActionMessage("errors.yaExiste", "El acceso vascular con fecha y hora "+this.getFecha()+" "+this.getHora()));
    		}
    	}
    	
    	//tipo dispositivo es requerido
    	if(this.tipoAccesoVascular<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "El tipo acceso vascular"));
    	}
    	
    	if(articulo<=0)
    	{
    		errores.add("", new ActionMessage("errors.required","El articulo"));
    	}
    	else
    	{
    		//la cantidad debe ser requerida
        	if(cantidadArticulo<=0)
        	{
        		errores.add("", new ActionMessage("errors.integerMayorQue","La cantidad del articulo "+this.descArticulo, "0"));
        	}
    	}
    	
    	//tipo dispositivo es requerido
    	if(this.localizacion<=0)
    	{
    		errores.add("", new ActionMessage("errors.required", "La localizacion acceso"));
    	}
    	
    	//******************Validacion Justificaciones No Pos**********//
		if(this.justificacionMap != null)
		{
			logger.info("Siiii lleva el mapa Jus>>>>>>>>>>>");
			if(this.espos.equals("NOPOS"))
			{
				logger.info("Entro al articulo no pos>>>>>>>>>>>");
				if(!this.justificacionMap.containsKey(this.articulo+"_yajustifico") || this.justificacionMap.get(this.articulo+"_yajustifico").equals("false"))
				{
					logger.info("Entro al no contiene llave>>>>>>>>>>>");
    				errores.add("", new ActionMessage("errors.required", "La justificacion no pos del articulo "+this.articulo));
    			}
			}
		}
		//***************************************************************//
    	
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
	 * @return the localizacion
	 */
	public int getLocalizacion() {
		return localizacion;
	}


	/**
	 * @param localizacion the localizacion to set
	 */
	public void setLocalizacion(int localizacion) {
		this.localizacion = localizacion;
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
	 * @return the mapaLocalizacionesMap
	 */
	public HashMap<Object, Object> getMapaLocalizacionesMap() {
		return mapaLocalizacionesMap;
	}


	/**
	 * @param mapaLocalizacionesMap the mapaLocalizacionesMap to set
	 */
	public void setMapaLocalizacionesMap(
			HashMap<Object, Object> mapaLocalizacionesMap) {
		this.mapaLocalizacionesMap = mapaLocalizacionesMap;
	}

	/**
	 * @return the mapaLocalizacionesMap
	 */
	public Object getMapaLocalizacionesMap(Object key) {
		return mapaLocalizacionesMap.get(key);
	}


	/**
	 * @param mapaLocalizacionesMap the mapaLocalizacionesMap to set
	 */
	public void setMapaLocalizacionesMap(
			Object key, Object value) {
		this.mapaLocalizacionesMap.put(key, value);
	}
	
	/**
	 * @return the mapaTiposAccesosVasculares
	 */
	public HashMap<Object, Object> getMapaTiposAccesosVasculares() {
		return mapaTiposAccesosVasculares;
	}


	/**
	 * @param mapaTiposAccesosVasculares the mapaTiposAccesosVasculares to set
	 */
	public void setMapaTiposAccesosVasculares(
			HashMap<Object, Object> mapaTiposAccesosVasculares) {
		this.mapaTiposAccesosVasculares = mapaTiposAccesosVasculares;
	}

	/**
	 * @return the mapaTiposAccesosVasculares
	 */
	public Object getMapaTiposAccesosVasculares(Object key) {
		return mapaTiposAccesosVasculares.get(key);
	}

	/**
	 * @param mapaTiposAccesosVasculares the mapaTiposAccesosVasculares to set
	 */
	public void setMapaTiposAccesosVasculares(
			Object key, Object value) {
		this.mapaTiposAccesosVasculares.put(key, value);
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
	 * @return the tipoAccesoVascular
	 */
	public int getTipoAccesoVascular() {
		return tipoAccesoVascular;
	}


	/**
	 * @param tipoAccesoVascular the tipoAccesoVascular to set
	 */
	public void setTipoAccesoVascular(int tipoAccesoVascular) {
		this.tipoAccesoVascular = tipoAccesoVascular;
	}

	/**
	 * @return the codigoPKAccesoVAscularHA
	 */
	public double getCodigoPKAccesoVAscularHA() {
		return codigoPKAccesoVAscularHA;
	}

	/**
	 * @param codigoPKAccesoVAscularHA the codigoPKAccesoVAscularHA to set
	 */
	public void setCodigoPKAccesoVAscularHA(double codigoPKAccesoVAscularHA) {
		this.codigoPKAccesoVAscularHA = codigoPKAccesoVAscularHA;
	}

	/**
	 * @return the articulo
	 */
	public int getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(int articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the cantidadArticulo
	 */
	public int getCantidadArticulo() {
		return cantidadArticulo;
	}

	/**
	 * @param cantidadArticulo the cantidadArticulo to set
	 */
	public void setCantidadArticulo(int cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}

	/**
	 * @return the descArticulo
	 */
	public String getDescArticulo() {
		return descArticulo;
	}

	/**
	 * @param descArticulo the descArticulo to set
	 */
	public void setDescArticulo(String descArticulo) {
		this.descArticulo = descArticulo;
	}

	/**
	 * @return the generoConsumo
	 */
	public boolean getGeneroConsumo() {
		return generoConsumo;
	}

	/**
	 * @param generoConsumo the generoConsumo to set
	 */
	public void setGeneroConsumo(boolean generoConsumo) {
		this.generoConsumo = generoConsumo;
	}

	/**
	 * @return the generoOrden
	 */
	public boolean getGeneroOrden() {
		return generoOrden;
	}

	/**
	 * @param generoOrden the generoOrden to set
	 */
	public void setGeneroOrden(boolean generoOrden) {
		this.generoOrden = generoOrden;
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

	public String getEspos() {
		return espos;
	}

	public void setEspos(String espos) {
		this.espos = espos;
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
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		if(this.codigoPeticion.equals(""))
			return ConstantesBD.codigoNuncaValido+"";
		else
			return codigoPeticion;
	}

	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}

	/**
	 * @return the mostrarDatosInfo
	 */
	public InfoDatosString getMostrarDatosInfo() {
		return mostrarDatosInfo;
	}

	/**
	 * @param mostrarDatosInfo the mostrarDatosInfo to set
	 */
	public void setMostrarDatosInfo(InfoDatosString mostrarDatosInfo) {
		this.mostrarDatosInfo = mostrarDatosInfo;
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
		
	public void setMostrarDatosInfoActivo(boolean valor){
		this.mostrarDatosInfo.setActivo(valor);
	}
	public boolean getMostrarDatosInfoActivo(){
		return this.mostrarDatosInfo.getActivo();
	}

	/**
	 * @return the codigoDetMateQx
	 */
	public String getCodigoDetMateQx() {
		return codigoDetMateQx;
	}

	/**
	 * @param codigoDetMateQx the codigoDetMateQx to set
	 */
	public void setCodigoDetMateQx(String codigoDetMateQx) {
		this.codigoDetMateQx = codigoDetMateQx;
	}

}