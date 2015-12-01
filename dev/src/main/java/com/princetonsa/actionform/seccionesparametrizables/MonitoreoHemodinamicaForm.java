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

import com.princetonsa.dto.salas.DtoMonitoreoHemoDinamica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.MonitoreoHemodinamica;

/**
 * 
 * @author wilson
 *
 */
public class MonitoreoHemodinamicaForm extends ValidatorForm 
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
	private boolean esConsulta;
	
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
	private int index;
	
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
	private int codigoMonHemoHojaAnestesia;
	
	/**
	 * 
	 */
	private HashMap<String, Object> mapaCamposOtrasSecciones;
	
	/**
	 * 
	 */
	private boolean estaBD;
	
	
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
    	
    	this.index=ConstantesBD.codigoNuncaValido;
    	
    	this.fecha=UtilidadFecha.getFechaActual();
    	this.hora=UtilidadFecha.getHoraActual();
    	this.mapaCamposOtrasSecciones= new HashMap<String, Object>();
    	
    	this.estaBD=false;
    	this.codigoMonHemoHojaAnestesia=ConstantesBD.codigoNuncaValido;
    	
    	if(!this.estado.equals("resumen"))
    	{	
    		this.ocultarMenu = false;
    		this.esSinEncabezado = "";
    	}	
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
        	errores=validacionesFecha(errores);
        	errores=validacionMonitoreoExistente(errores);
        	
        	for(int w=0; w<Integer.parseInt(this.getMapa("numRegistros")+""); w++)
        	{
        		DtoMonitoreoHemoDinamica dto= (DtoMonitoreoHemoDinamica) mapa.get("DTO_"+w);
        		
        		errores= validacionesSelect(errores,dto,this.mapa.get("valor_"+w)+"");
        		errores= validacionesNumericos(errores, dto, this.mapa.get("valor_"+w)+"");
        		
        	}
        	
        	//si no existen errores entonces se evaluan los campos que deben ser menores o mayores a otros
        	for(int w=0; w<Integer.parseInt(this.getMapa("numRegistros")+""); w++)
        	{
        		DtoMonitoreoHemoDinamica dto= (DtoMonitoreoHemoDinamica) mapa.get("DTO_"+w);
        		errores= validacionesMenoresMayores(errores,dto,this.mapa.get("valor_"+w)+"");
        	}
        }
        
        if(!errores.isEmpty())
        {
        	if(estado.equals("guardar"))
        		this.setEstado("continuar");
        }
        
        return errores;
    }

    /**
     * 
     * @param errores
     * @return
     */
    private ActionErrors validacionMonitoreoExistente(ActionErrors errores) 
    {
		if(errores.isEmpty())
		{
			if(MonitoreoHemodinamica.existeMonitoreoHojaAnestesia(this.numeroSolicitud, this.fecha, this.hora, this.codigoMonHemoHojaAnestesia))
			{
				errores.add("", new ActionMessage("errors.yaExiste", "El monitoreo hemodinamica para la fecha "+this.fecha+" hora "+this.hora));
			}
		}
		return errores;
	}

	/**
     * 
     * @param errores
     * @param dto
     * @param string
     * @return
     */
    private ActionErrors validacionesSelect(ActionErrors errores, DtoMonitoreoHemoDinamica dto, String valor) 
    {
    	if(dto.getObligatorio() && dto.getTipoCampo().equals("SELE"))
		{
			if(UtilidadTexto.isEmpty(valor) || valor.equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("", new ActionMessage("errors.required", dto.getNombre()));
			}
		}
		return errores;
	}

	/**
     * 
     * @param errores
     * @param dto
     * @return
     */
    private ActionErrors validacionesMenoresMayores(ActionErrors errores, DtoMonitoreoHemoDinamica dto, String valor) 
    {
		if(!UtilidadTexto.isEmpty(dto.getMenor()) && !UtilidadTexto.isEmpty(valor))
		{
			String vectorStr[]=dto.getMenor().replace("_", "").split(",");
			double valorDouble= Utilidades.convertirADouble(valor);
			
			for(int w=0; w<vectorStr.length; w++)
			{
				int posicion= obtenerPosicionMapa( Integer.parseInt(vectorStr[w].trim()));
				DtoMonitoreoHemoDinamica dtoComparar= (DtoMonitoreoHemoDinamica) mapa.get("DTO_"+posicion);
				
				String valorAComparar= this.getMapa("valor_"+posicion)+"";
				
				if(UtilidadTexto.isNumber(valorAComparar))
				{
					if(Utilidades.convertirADouble(valorAComparar)<valorDouble)
					{
						errores.add("", new ActionMessage("errors.integerMenorQue", dto.getNombre(), dtoComparar.getNombre()));
					}	
				}
				else
				{
					errores.add("", new ActionMessage("errors.notEspecific", dtoComparar.getNombre()+" es requerido para validar que el "+dto.getNombre()+" sea menor"));
				}
			}
		}
		return errores;
	}
    
    /**
     * 
     * @param codigoMonitoreoAnestesia
     * @return
     */
    private int obtenerPosicionMapa(int codigoMonitoreoAnestesia)
    {
    	for(int w=0; w<Integer.parseInt(this.getMapa("numRegistros")+""); w++)
		{
    		DtoMonitoreoHemoDinamica dto= (DtoMonitoreoHemoDinamica) mapa.get("DTO_"+w);
    		if(dto.getCodigo()==codigoMonitoreoAnestesia)
    			return w;
		}
    	return ConstantesBD.codigoNuncaValido;
    }
    
    
	/**
     * 
     * @param errores
     * @param dto
     * @return
     */
    private ActionErrors validacionesNumericos(ActionErrors errores, DtoMonitoreoHemoDinamica dto, String valor) 
    {
    	if(dto.getEsNumerico() && (!UtilidadTexto.isEmpty(valor) || dto.getObligatorio()))
		{
    		if(!UtilidadTexto.isNumber(valor))
			{	
				if(dto.getNumeroDecimales()<=0)
					errores.add("", new ActionMessage("errors.integer", dto.getNombre()));
				else
					errores.add("", new ActionMessage("errors.float", dto.getNombre()));
			}
			if(errores.isEmpty())
			{
				double valorDouble= Utilidades.convertirADouble(valor);
				
				if(dto.getValorMinimo()!=ConstantesBD.codigoNuncaValido && dto.getValorMaximo()!=ConstantesBD.codigoNuncaValido)
				{
					if(valorDouble>dto.getValorMaximo() || valorDouble<dto.getValorMinimo())
					{
						errores.add("", new ActionMessage("errors.range", dto.getNombre(), dto.getValorMinimo(), dto.getValorMaximo()));
					}
				}
				
				if(!dto.getPermiteNegativos() && valorDouble<0)
				{
					errores.add("", new ActionMessage("errors.notEspecific", dto.getNombre()+" debe ser positivo"));
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
	private ActionErrors validacionesFecha(ActionErrors errores) 
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
	 * @return the mapa
	 */
	public Object getMapaListado(Object key) {
		return mapaListado.get(key);
	}

	/**
	 * @param mapa the mapa to set
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
	 * @return the mapaCamposOtrasSecciones
	 */
	public HashMap<String, Object> getMapaCamposOtrasSecciones() {
		return mapaCamposOtrasSecciones;
	}

	/**
	 * @param mapaCamposOtrasSecciones the mapaCamposOtrasSecciones to set
	 */
	public void setMapaCamposOtrasSecciones(
			HashMap<String, Object> mapaCamposOtrasSecciones) {
		this.mapaCamposOtrasSecciones = mapaCamposOtrasSecciones;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getMapaCamposOtrasSecciones(Object key) {
		return mapaCamposOtrasSecciones.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setMapaCamposOtrasSecciones(String key, Object value) {
		this.mapaCamposOtrasSecciones.put(key, value);
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
	 * @return the codigoMonHemoHojaAnestesia
	 */
	public int getCodigoMonHemoHojaAnestesia() {
		return codigoMonHemoHojaAnestesia;
	}

	/**
	 * @param codigoMonHemoHojaAnestesia the codigoMonHemoHojaAnestesia to set
	 */
	public void setCodigoMonHemoHojaAnestesia(int codigoMonHemoHojaAnestesia) {
		this.codigoMonHemoHojaAnestesia = codigoMonHemoHojaAnestesia;
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
