package com.princetonsa.actionform.consultaExterna;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.Utilidades;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */

public class AsignarCitasControlPostOperatorioForm extends ValidatorForm
{

	private String estado;
	
    /**
     * HashMap que almacena el listado de las citas reservadas de control post-operatorio 
     */
    private HashMap busquedaCitasReservadas;
    
    /**
     * Posicion seleccionada del Mapa
     */
    private int posicion;
    
    /**
     * Codigo del Paciente
     */
    private String codigoPaciente;
    
    /**
     * Codigo de la Cita
     */
    private String codigoCita;
    
    /**
     * Codigo Estado de la Cita  
     */
    private String codigoEstadoCita;
    
    /**
     * Codigo del Servicio de la Cita
     */
    private String codigoServicioCita;
    
    /**
     * Codigo Estado del Servicio de la Cita
     */
    private String codigoEstadoServicioCita;
    
    /**
     * Estado Liquidacion Cita
     */
    private String estadoLiquidacionCita;
    
    /**
     * Numero de la Solicitud del Servicio Cita
     */
    private String numeroSolicitudCita;
    
    /**
     * Codigo de la Agenda de la Cita
     */
    private String codigoAgenda;
    
    /**
     * Codigo de la Especialidad del Servicio Cita
     */
    private String codigoEspecialidad;
    
    /**
     * Codigo Tipo del Servicio
     */
    private String codigoTipoServicio;
    
    /**
     * Codigo Centro de Costo
     */
    private String codigoCentroCosto;
    
    /**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * Mensaje de advertencia cuando hay citas con servicios que no son de tipo consulta
     */
    private ResultadoBoolean mensajeAdvertencia = new ResultadoBoolean(false);
    
    /**
     * Hay citas de tipo servicio consulta para visualizar
     */
    private boolean mostrarCitas;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
        this.busquedaCitasReservadas = new HashMap();
    	this.busquedaCitasReservadas.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	this.codigoPaciente = "";
    	this.codigoCita = "";
    	this.codigoEstadoCita = "";
    	this.codigoServicioCita = "";
    	this.codigoEstadoServicioCita = "";
    	this.estadoLiquidacionCita = "";
    	this.numeroSolicitudCita = "";
    	this.codigoAgenda = "";
    	this.codigoEspecialidad = "";
    	this.codigoTipoServicio = "";
    	this.codigoCentroCosto = "";
    	this.mensaje = new ResultadoBoolean(false, "");
    	this.mensajeAdvertencia = new ResultadoBoolean(false, "");
    	this.mostrarCitas = false;
    }
    
    /**
     * Metodo que inicializa solo el mapa de busqueda de citas
     */
    public void resetMapaCitas()
    {
    	this.busquedaCitasReservadas = new HashMap();
    	this.busquedaCitasReservadas.put("numRegistros", "0");
    }
	
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	boolean bandera = false;
    	if(this.estado.equals("asignar"))
    	{
    		int numRegistros = Utilidades.convertirAEntero(this.getBusquedaCitasReservadas("numRegistros")+"");
    		for(int i=0; i < numRegistros; i++)
    		{
	    		if(UtilidadCadena.noEsVacio(this.getBusquedaCitasReservadas("hdasignar_"+i)+"") && (this.getBusquedaCitasReservadas("hdasignar_"+i)).equals(ConstantesBD.acronimoSi))
	    			bandera = true;
	    	
	    		if(bandera)
	    			i=numRegistros;
    		}
    		if(!bandera)
    			errores.add("codigo", new ActionMessage("error.facturasVarias.consultaPagosFacturasVarias", "servicio de cita para ser asignado"));
    	}
    	return errores;
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
	 * @return the busquedaCitasReservadas
	 */
	public HashMap getBusquedaCitasReservadas() {
		return busquedaCitasReservadas;
	}

	/**
	 * @param busquedaCitasReservadas the busquedaCitasReservadas to set
	 */
	public void setBusquedaCitasReservadas(HashMap busquedaCitasReservadas) {
		this.busquedaCitasReservadas = busquedaCitasReservadas;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getBusquedaCitasReservadas(String key) 
	{
		return busquedaCitasReservadas.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setBusquedaCitasReservadas(String key, Object value) 
	{
		this.busquedaCitasReservadas.put(key, value);
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the codigoCita
	 */
	public String getCodigoCita() {
		return codigoCita;
	}

	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
	}

	/**
	 * @return the codigoEstadoCita
	 */
	public String getCodigoEstadoCita() {
		return codigoEstadoCita;
	}

	/**
	 * @param codigoEstadoCita the codigoEstadoCita to set
	 */
	public void setCodigoEstadoCita(String codigoEstadoCita) {
		this.codigoEstadoCita = codigoEstadoCita;
	}

	/**
	 * @return the codigoEstadoServicioCita
	 */
	public String getCodigoEstadoServicioCita() {
		return codigoEstadoServicioCita;
	}

	/**
	 * @param codigoEstadoServicioCita the codigoEstadoServicioCita to set
	 */
	public void setCodigoEstadoServicioCita(String codigoEstadoServicioCita) {
		this.codigoEstadoServicioCita = codigoEstadoServicioCita;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoServicioCita
	 */
	public String getCodigoServicioCita() {
		return codigoServicioCita;
	}

	/**
	 * @param codigoServicioCita the codigoServicioCita to set
	 */
	public void setCodigoServicioCita(String codigoServicioCita) {
		this.codigoServicioCita = codigoServicioCita;
	}

	/**
	 * @return the estadoLiquidacionCita
	 */
	public String getEstadoLiquidacionCita() {
		return estadoLiquidacionCita;
	}

	/**
	 * @param estadoLiquidacionCita the estadoLiquidacionCita to set
	 */
	public void setEstadoLiquidacionCita(String estadoLiquidacionCita) {
		this.estadoLiquidacionCita = estadoLiquidacionCita;
	}

	/**
	 * @return the numeroSolicitudCita
	 */
	public String getNumeroSolicitudCita() {
		return numeroSolicitudCita;
	}

	/**
	 * @param numeroSolicitudCita the numeroSolicitudCita to set
	 */
	public void setNumeroSolicitudCita(String numeroSolicitudCita) {
		this.numeroSolicitudCita = numeroSolicitudCita;
	}

	/**
	 * @return the codigoAgenda
	 */
	public String getCodigoAgenda() {
		return codigoAgenda;
	}

	/**
	 * @param codigoAgenda the codigoAgenda to set
	 */
	public void setCodigoAgenda(String codigoAgenda) {
		this.codigoAgenda = codigoAgenda;
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public String getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(String codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(String codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return the codigoTipoServicio
	 */
	public String getCodigoTipoServicio() {
		return codigoTipoServicio;
	}

	/**
	 * @param codigoTipoServicio the codigoTipoServicio to set
	 */
	public void setCodigoTipoServicio(String codigoTipoServicio) {
		this.codigoTipoServicio = codigoTipoServicio;
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

	public ResultadoBoolean getMensajeAdvertencia() {
		return mensajeAdvertencia;
	}

	public void setMensajeAdvertencia(ResultadoBoolean mensajeAdvertencia) {
		this.mensajeAdvertencia = mensajeAdvertencia;
	}

	public boolean isMostrarCitas() {
		return mostrarCitas;
	}

	public void setMostrarCitas(boolean mostrarCitas) {
		this.mostrarCitas = mostrarCitas;
	}
	
}