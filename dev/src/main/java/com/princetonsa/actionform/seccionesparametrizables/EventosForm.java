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

import com.princetonsa.dto.salas.DtoEventos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.Eventos;

/**
 * 
 * @author wilson
 *
 */
public class EventosForm extends ValidatorForm 
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
	 * codigo pk de la tabla eventos_anest_inst_cc
	 */
	private int codigoEventoInstCC;
	
	/**
	 * 
	 */
	private int codigoEvento;
	
	/**
	 * 
	 */
	private boolean esConsulta;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapa;
	
	/**
	 * 
	 */
	private DtoEventos dtoEvento;
	
	
	
	/********DATOS A ACTUALIZAR***********/
	
	/**
	 * 
	 */
	private String fechaInicialActualizada;
	
	/**
	 * 
	 */
	private String horaInicialActualizada;
	
	/**
	 * 
	 */
	private String fechaFinalActualizada;
	
	/**
	 * 
	 */
	private String horaFinalActualizada;
	
	/**
	 * 
	 */
	private String graficarActualizado;
	
	/**
	 * 
	 */
	private boolean estaBD;
	
	/**
	 * 
	 */
	private int codigoEventoHojaAnestesia;
	
	/**
	 * indice para saber cuales signos vitales va ha modificar
	 */
	private int index;
	
	/**
	 * 
	 */
	private String nombreOtroEvento;
	
	
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
	 * Indicador ocultar menu 
	 * */
	private boolean ocultarMenu = false;
	
	/*****************************************/
	
	/**
	 * 
	 */
	private boolean puedoModificarFechaHoraCx;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros", "0");
    	this.dtoEvento= new DtoEventos();
    	
    	this.fechaInicialActualizada="";
    	this.fechaFinalActualizada="";
    	this.horaInicialActualizada="";
    	this.horaFinalActualizada="";
    	this.graficarActualizado=ConstantesBD.acronimoSi;
    	this.estaBD=false;
    	this.codigoEventoHojaAnestesia=ConstantesBD.codigoNuncaValido;
    	this.nombreOtroEvento="";
    	this.index=ConstantesBD.codigoNuncaValido;
    	if(!this.estado.equals("resumen"))
    		this.esSinEncabezado = "";
    	this.puedoModificarFechaHoraCx=true;
    }
    
    /**
     * resetea los atributos del form
     *
     */
    public void resetNuevo()
    {
    	this.fechaInicialActualizada="";
    	this.fechaFinalActualizada="";
    	this.horaInicialActualizada="";
    	this.horaFinalActualizada="";
    	this.graficarActualizado=ConstantesBD.acronimoSi;
    	this.estaBD=false;
    	this.index=ConstantesBD.codigoNuncaValido;
    	this.nombreOtroEvento="";
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
        	if(this.getDtoEvento().getLlevaFechaFin())
        		errores=validacion1Reg(errores);
        	else
        		errores=validacion1Reg2(errores);
        }	
        
        if(!errores.isEmpty())
    	{
    		if(estado.equals("guardar") && !this.getEstaBD() && this.getDtoEvento().getNRegistros())	
    			this.estado="nuevoEventoNReg";
    		else if(estado.equals("guardar") && this.getEstaBD() && this.getDtoEvento().getNRegistros())	
    			this.estado="modificarEventoNReg";
    		
    		else if(estado.equals("guardar") && !this.getEstaBD() && !this.getDtoEvento().getNRegistros())	
    			this.estado="empezarEventos";
    		else if(estado.equals("guardar") && this.getEstaBD() && !this.getDtoEvento().getNRegistros())	
    			this.estado="empezarEventos";
    	}
        return errores;
    }

    /**
     * 
     * @param errores
     * @return
     */
    public ActionErrors validacion1Reg(ActionErrors errores)
    {
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicialActualizada()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHoraInicialActualizada()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinalActualizada()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Final"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHoraFinalActualizada()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Final"));
    	}
    	if(this.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
    	{
    		if(UtilidadTexto.isEmpty(this.getNombreOtroEvento()))
    		{
    			errores.add("", new ActionMessage("errors.required", "El nombre del evento"));
    		}
    	}
    	
    	//si no existen errores entonces se valida que la fecha - hora de inicio sea >= fecha/hora ingreso a la sala y <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.compararFechas(this.getFechaFinalActualizada(), this.getHoraFinalActualizada(), this.getFechaInicialActualizada(), this.getHoraInicialActualizada()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Final"));
    		}
    		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFechaInicialActualizada(), this.getHoraInicialActualizada()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
    		}
    		else
    		{
    			if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFechaFinalActualizada(), this.getHoraFinalActualizada()).isTrue())
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
        		}
    		}
    		
    		if(errores.isEmpty())
    		{
    			SolicitudesCx solicitudCx = new SolicitudesCx();		
    			solicitudCx.cargarEncabezadoSolicitudCx(this.numeroSolicitud+"");
    			
    			
    			if(!UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()))
    			{	
	    			if(!UtilidadFecha.compararFechas( this.getFechaInicialActualizada(), this.getHoraInicialActualizada(), solicitudCx.getFechaIngresoSala(), solicitudCx.getHoraIngresoSala()).isTrue())
	        		{
	    				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Ingreso Sala "+solicitudCx.getFechaIngresoSala()+" "+solicitudCx.getHoraIngresoSala()));
	        		}
    			}
    			else
    			{
    				errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
    			}
    			
    			/*if(this.getCodigoEvento()==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
    			{
    				if(!UtilidadTexto.isEmpty(solicitudCx.getFechaSalidaSala()))
        			{	
    	    			if(!UtilidadFecha.compararFechas( solicitudCx.getFechaSalidaSala(), solicitudCx.getHoraSalidaSala(), this.getFechaFinalActualizada(), this.getHoraFinalActualizada()).isTrue())
    	        		{
    	    				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Salida Sala "+solicitudCx.getFechaSalidaSala()+" "+solicitudCx.getHoraSalidaSala(), "Final" ));
    	        		}
        			}
        			else
        			{
        				errores.add("", new ActionMessage("errors.required", "La fecha/hora egreso sala"));
        			}
    			}*/
    			
    		}
    		
    		//si son n registros no debe existir cruce
        	if(this.getDtoEvento().getNRegistros() && errores.isEmpty())
        	{
        		if(Eventos.existeCruceFechasEventos(this.getFechaInicialActualizada(), this.getHoraInicialActualizada(), this.getFechaFinalActualizada(), this.getHoraFinalActualizada(), this.getCodigoEventoInstCC(), this.getCodigoEventoHojaAnestesia(), this.getNumeroSolicitud()))
        		{
        			errores.add("", new ActionMessage("error.rangoFechasInvalido", "inicial", "final", "ya", "parametrizados"));
        		}
        	}
    	}
    	
    	return errores;
    }
    
    
    /**
     * 
     * @param errores
     * @return
     */
    public ActionErrors validacion1Reg2(ActionErrors errores)
    {
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicialActualizada()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHoraInicialActualizada()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
    	}
    	if(this.getDtoEvento().getCodigo()==ConstantesBD.codigoEventoHojaAnestesiaOtros)
    	{
    		if(UtilidadTexto.isEmpty(this.getNombreOtroEvento()))
    		{
    			errores.add("", new ActionMessage("errors.required", "El nombre del evento"));
    		}
    	}
    	
    	//si no existen errores entonces se valida que la fecha - hora de inicio sea >= fecha/hora ingreso a la sala y <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), this.getFechaInicialActualizada(), this.getHoraInicialActualizada()).isTrue())
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
    		}
    		
    		if(errores.isEmpty())
    		{
    			SolicitudesCx solicitudCx = new SolicitudesCx();		
    			solicitudCx.cargarEncabezadoSolicitudCx(this.numeroSolicitud+"");
    			
    			
    			if(!UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()))
    			{	
	    			if(!UtilidadFecha.compararFechas( this.getFechaInicialActualizada(), this.getHoraInicialActualizada(), solicitudCx.getFechaIngresoSala(), solicitudCx.getHoraIngresoSala()).isTrue())
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
	public Object getMapa(Object key) {
		return mapa.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapa(Object key, Object value) {
		this.mapa.put(key, value);
	}
	
	/**
	 * @return the dtoEvento
	 */
	public DtoEventos getDtoEvento() {
		return dtoEvento;
	}

	/**
	 * @param dtoEvento the dtoEvento to set
	 */
	public void setDtoEvento(DtoEventos dtoEvento) {
		this.dtoEvento = dtoEvento;
	}

	/**
	 * @return the codigoEventoInstCC
	 */
	public int getCodigoEventoInstCC() {
		return codigoEventoInstCC;
	}

	/**
	 * @param codigoEventoInstCC the codigoEventoInstCC to set
	 */
	public void setCodigoEventoInstCC(int codigoEventoInstCC) {
		this.codigoEventoInstCC = codigoEventoInstCC;
	}

	/**
	 * @return the codigoEvento
	 */
	public int getCodigoEvento() {
		return codigoEvento;
	}

	/**
	 * @param codigoEvento the codigoEvento to set
	 */
	public void setCodigoEvento(int codigoEvento) {
		this.codigoEvento = codigoEvento;
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
	 * @return the fechaFinalActualizada
	 */
	public String getFechaFinalActualizada() {
		return fechaFinalActualizada;
	}

	/**
	 * @param fechaFinalActualizada the fechaFinalActualizada to set
	 */
	public void setFechaFinalActualizada(String fechaFinalActualizada) {
		this.fechaFinalActualizada = fechaFinalActualizada;
	}

	/**
	 * @return the fechaInicialActualizada
	 */
	public String getFechaInicialActualizada() {
		return fechaInicialActualizada;
	}

	/**
	 * @param fechaInicialActualizada the fechaInicialActualizada to set
	 */
	public void setFechaInicialActualizada(String fechaInicialActualizada) {
		this.fechaInicialActualizada = fechaInicialActualizada;
	}

	/**
	 * @return the horaFinalActualizada
	 */
	public String getHoraFinalActualizada() {
		return horaFinalActualizada;
	}

	/**
	 * @param horaFinalActualizada the horaFinalActualizada to set
	 */
	public void setHoraFinalActualizada(String horaFinalActualizada) {
		this.horaFinalActualizada = horaFinalActualizada;
	}

	/**
	 * @return the horaInicialActualizada
	 */
	public String getHoraInicialActualizada() {
		return horaInicialActualizada;
	}

	/**
	 * @param horaInicialActualizada the horaInicialActualizada to set
	 */
	public void setHoraInicialActualizada(String horaInicialActualizada) {
		this.horaInicialActualizada = horaInicialActualizada;
	}

	/**
	 * @return the graficarActualizado
	 */
	public String getGraficarActualizado() {
		return graficarActualizado;
	}

	/**
	 * @param graficarActualizado the graficarActualizado to set
	 */
	public void setGraficarActualizado(String graficarActualizado) {
		this.graficarActualizado = graficarActualizado;
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
	 * @return the codigoEventoHojaAnestesia
	 */
	public int getCodigoEventoHojaAnestesia() {
		return codigoEventoHojaAnestesia;
	}

	/**
	 * @param codigoEventoHojaAnestesia the codigoEventoHojaAnestesia to set
	 */
	public void setCodigoEventoHojaAnestesia(int codigoEventoHojaAnestesia) {
		this.codigoEventoHojaAnestesia = codigoEventoHojaAnestesia;
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
	 * @return the nombreOtroEvento
	 */
	public String getNombreOtroEvento() {
		return nombreOtroEvento;
	}

	/**
	 * @param nombreOtroEvento the nombreOtroEvento to set
	 */
	public void setNombreOtroEvento(String nombreOtroEvento) {
		this.nombreOtroEvento = nombreOtroEvento;
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

	/**
	 * @return the puedoModificarFechaHoraCx
	 */
	public boolean isPuedoModificarFechaHoraCx() {
		return puedoModificarFechaHoraCx;
	}

	/**
	 * @param puedoModificarFechaHoraCx the puedoModificarFechaHoraCx to set
	 */
	public void setPuedoModificarFechaHoraCx(boolean puedoModificarFechaHoraCx) {
		this.puedoModificarFechaHoraCx = puedoModificarFechaHoraCx;
	}

	/**
	 * @return the puedoModificarFechaHoraCx
	 */
	public boolean getPuedoModificarFechaHoraCx() {
		return puedoModificarFechaHoraCx;
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