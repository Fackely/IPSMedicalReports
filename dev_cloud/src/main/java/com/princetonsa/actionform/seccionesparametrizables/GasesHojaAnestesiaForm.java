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
import util.Utilidades;

import com.princetonsa.dto.salas.DtoGases;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.GasesHojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class GasesHojaAnestesiaForm extends ValidatorForm  
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
	 * codigo pk de la tabla gases_anest_inst_cc
	 */
	private int codigoGasInstCC;
	
	/**
	 * 
	 */
	private int codigoGas;
	
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
	private DtoGases dtoGases;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaTagTiposGasesAnestesicos;
	
	
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
	private String suspendidoActualizado;
	
	/**
	 * 
	 */
	private String cantidadLitrosActualizada;
	
	/**
	 * 
	 */
	private int gasAnestesicoActualizada;
	
	/**
	 * 
	 */
	private String fio2Actualizada;
	
	
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
	private int codigoGasHojaAnestesia;
	
	/**
	 * indice para saber cuales signos vitales va ha modificar
	 */
	private int index;
	
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
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.mapa= new HashMap<Object, Object>();
    	this.mapa.put("numRegistros", "0");
    	this.dtoGases= new DtoGases();
    	
    	this.fechaInicialActualizada="";
    	this.horaInicialActualizada="";
    	this.suspendidoActualizado=ConstantesBD.acronimoNo;
    	this.cantidadLitrosActualizada="";
    	this.gasAnestesicoActualizada=ConstantesBD.codigoNuncaValido;
    	this.fio2Actualizada="";
    	this.graficarActualizado=ConstantesBD.acronimoSi;
    	this.estaBD=false;
    	this.codigoGasHojaAnestesia=ConstantesBD.codigoNuncaValido;
    	this.index=ConstantesBD.codigoNuncaValido;
    	
    	if(!this.getEstado().equals("resumen"))
    		this.esSinEncabezado = "";
    	
    	cargarTagMap();
    }
    
    /**
     * 
     *
     */
    private void cargarTagMap() 
    {
    	this.mapaTagTiposGasesAnestesicos= GasesHojaAnestesia.obtenerTiposGasesAnestesicos();
	}

	/**
     * resetea los atributos del form
     *
     */
    public void resetNuevo()
    {
    	this.fechaInicialActualizada="";
    	this.horaInicialActualizada="";
    	this.suspendidoActualizado=ConstantesBD.acronimoNo;
    	this.cantidadLitrosActualizada="";
    	this.gasAnestesicoActualizada=ConstantesBD.codigoNuncaValido;
    	this.fio2Actualizada="";
    	this.graficarActualizado=ConstantesBD.acronimoSi;
    	this.estaBD=false;
    	this.codigoGasHojaAnestesia=ConstantesBD.codigoNuncaValido;
    	this.index=ConstantesBD.codigoNuncaValido;
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
    			this.estado="nuevoGas";
    		else if(estado.equals("guardar") && this.getEstaBD())	
    			this.estado="modificarGas";
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
    	if(Utilidades.convertirAEntero(this.getCantidadLitrosActualizada())<=ConstantesBD.codigoNuncaValido)
    	{	
    		errores.add("", new ActionMessage("errors.range", "Cantidad Litros", this.dtoGases.getCantidadMinLitros(), this.dtoGases.getCantidadMaxLitros()));
    	}
    	else
    	{
    		int cantidadLitros= Integer.parseInt(this.getCantidadLitrosActualizada());
    		if(cantidadLitros>this.dtoGases.getCantidadMaxLitros() || cantidadLitros<this.getDtoGases().getCantidadMinLitros())
    		{
    			errores.add("", new ActionMessage("errors.range", "Cantidad litros", this.dtoGases.getCantidadMinLitros(), this.dtoGases.getCantidadMaxLitros()));
    		}
    	}
    	
    	if(this.dtoGases.getLlevaFio2())
    	{
    		if(Utilidades.convertirAEntero(this.getFio2Actualizada())<=ConstantesBD.codigoNuncaValido)
    		{
    			errores.add("", new ActionMessage("errors.range", "Fio2", 0, 100));
    		}
    		else
    		{
    			int fio2= Integer.parseInt(this.getFio2Actualizada());
    			if(fio2>100 || fio2<0)
    			{
    				errores.add("", new ActionMessage("errors.range", "Fio2", 0, 100));
    			}
    		}
    	}
    	
    	if(this.dtoGases.getLlevaGasAnestesico())
    	{
    		if(this.getGasAnestesicoActualizada()<=0)
    		{
    			errores.add("", new ActionMessage("errors.required", "El Gas Anestesico"));
    		}
    	}
    	
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicialActualizada()))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.validacionHora(this.getHoraInicialActualizada()).puedoSeguir)
    	{
    		errores.add("", new ActionMessage("errors.formatoHoraInvalido","Inicial"));
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
	 * @return the cantidadLitrosActualizada
	 */
	public String getCantidadLitrosActualizada() {
		return cantidadLitrosActualizada;
	}

	/**
	 * @param cantidadLitrosActualizada the cantidadLitrosActualizada to set
	 */
	public void setCantidadLitrosActualizada(String cantidadLitrosActualizada) {
		this.cantidadLitrosActualizada = cantidadLitrosActualizada;
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
	 * @return the codigoGas
	 */
	public int getCodigoGas() {
		return codigoGas;
	}

	/**
	 * @param codigoGas the codigoGas to set
	 */
	public void setCodigoGas(int codigoGas) {
		this.codigoGas = codigoGas;
	}

	/**
	 * @return the codigoGasHojaAnestesia
	 */
	public int getCodigoGasHojaAnestesia() {
		return codigoGasHojaAnestesia;
	}

	/**
	 * @param codigoGasHojaAnestesia the codigoGasHojaAnestesia to set
	 */
	public void setCodigoGasHojaAnestesia(int codigoGasHojaAnestesia) {
		this.codigoGasHojaAnestesia = codigoGasHojaAnestesia;
	}

	/**
	 * @return the codigoGasInstCC
	 */
	public int getCodigoGasInstCC() {
		return codigoGasInstCC;
	}

	/**
	 * @param codigoGasInstCC the codigoGasInstCC to set
	 */
	public void setCodigoGasInstCC(int codigoGasInstCC) {
		this.codigoGasInstCC = codigoGasInstCC;
	}

	/**
	 * @return the dtoGases
	 */
	public DtoGases getDtoGases() {
		return dtoGases;
	}

	/**
	 * @param dtoGases the dtoGases to set
	 */
	public void setDtoGases(DtoGases dtoGases) {
		this.dtoGases = dtoGases;
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
	 * @return the fio2Actualizada
	 */
	public String getFio2Actualizada() {
		return fio2Actualizada;
	}

	/**
	 * @param fio2Actualizada the fio2Actualizada to set
	 */
	public void setFio2Actualizada(String fio2Actualizada) {
		this.fio2Actualizada = fio2Actualizada;
	}

	/**
	 * @return the gasAnestesicoActualizada
	 */
	public int getGasAnestesicoActualizada() {
		return gasAnestesicoActualizada;
	}

	/**
	 * @param gasAnestesicoActualizada the gasAnestesicoActualizada to set
	 */
	public void setGasAnestesicoActualizada(int gasAnestesicoActualizada) {
		this.gasAnestesicoActualizada = gasAnestesicoActualizada;
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
	 * @return the suspendidoActualizado
	 */
	public String getSuspendidoActualizado() {
		return suspendidoActualizado;
	}

	/**
	 * @param suspendidoActualizado the suspendidoActualizado to set
	 */
	public void setSuspendidoActualizado(String suspendidoActualizado) {
		this.suspendidoActualizado = suspendidoActualizado;
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
	 * @return the mapaTagTiposGasesAnestesicos
	 */
	public HashMap<Object, Object> getMapaTagTiposGasesAnestesicos() {
		return mapaTagTiposGasesAnestesicos;
	}

	/**
	 * @param mapaTagTiposGasesAnestesicos the mapaTagTiposGasesAnestesicos to set
	 */
	public void setMapaTagTiposGasesAnestesicos(
			HashMap<Object, Object> mapaTagTiposGasesAnestesicos) {
		this.mapaTagTiposGasesAnestesicos = mapaTagTiposGasesAnestesicos;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapaTagTiposGasesAnestesicos(Object key) {
		return mapaTagTiposGasesAnestesicos.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapaTagTiposGasesAnestesicos(Object key, Object value) {
		this.mapaTagTiposGasesAnestesicos.put(key, value);
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

}
