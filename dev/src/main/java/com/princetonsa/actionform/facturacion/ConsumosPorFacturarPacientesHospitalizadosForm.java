package com.princetonsa.actionform.facturacion;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008
 */

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class ConsumosPorFacturarPacientesHospitalizadosForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConsumosPorFacturarPacientesHospitalizadosForm.class);
	
	
	private String estado;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de ¿pagos facturas varias?
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * Fecha Inicial de Ingreso
     */
    private String fechaInicial;
    
    /**
     * Fecha Final de Ingreso
     */
    private String fechaFinal;
    
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el codigo del convenio seleccionado
     */
    private String convenioSeleccionado;
    
    /**
     * Maneja el tipo de salida que se desea ejecutar (Imprimir y Archivo Plano)
     */
    private String tipoSalida;
    
    /**
     * Float que tiene el valor base inicial
     */
    private String montoBaseInicial;
    
    /**
     * Float que tiene el valor base final
     */
    private String montoBaseFinal;
    
    /**
     * Valor que maneja el tope por que se desea realizar el filtrado
     */
    private String tope;
    
    /**
     * HashMap para almacenar los datos que arroja la consulta de Consumos Por Facturar Pacientes Hospitalizados
     */
    private HashMap consumosPorFacturarPacientesHospitalizados;
    
    /**
	 * Path completo del archivo generado
	 */
	private String pathArchivoTxt;
	
	/**
	 * Controla si se genera el archivo o no?
	 */
	private boolean archivo;
 	
	/**
	 * Sin errores en el validate
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
    
	/**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.convenios = new ArrayList<HashMap<String,Object>>();
    	this.convenioSeleccionado = "";
    	this.tipoSalida = "";
    	this.montoBaseInicial = "";
    	this.montoBaseFinal = "";
    	this.tope = "";
    	this.consumosPorFacturarPacientesHospitalizados = new HashMap();
    	this.consumosPorFacturarPacientesHospitalizados.put("numRegistros", "0");
    	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
    }
	
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
    	ActionErrors errores = new ActionErrors();
    	UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		String path = ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(usuario.getCodigoInstitucionInt());
		logger.info("Llego al validate con el estado: "+estado);
		
    	if(this.estado.equals("buscar") || this.estado.equals("imprimir"))
    	{
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals(null)){
				errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
				this.errores = true;
			}
			
			if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null")){
				errores.add("Tipo Salida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			
			//se valida ke la ruta de reportes este definida en administracion si se escogio generar un archivo plano 
			if(this.tipoSalida.trim().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo) && UtilidadTexto.isEmpty(path)) { 
				errores.add("Tipo Salida", new ActionMessage("errors.required","Para generar este tipo de reporte debe especificar primero la ruta para reportes en Parametros Generales en el modulo de Facturación. "));
				this.errores = true;
			}
			
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null")){
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
				this.errores = true;
			}
			
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null")){
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
				this.errores = true;
			}
			
			if(Utilidades.convertirADouble((this.montoBaseFinal)+"") < Utilidades.convertirADouble((this.montoBaseInicial)+"")) {
				errores.add("", new ActionMessage("error.facturacion.montoInicialMenorIgual", "Inicial "+this.montoBaseInicial, "Final "+this.montoBaseFinal));
				this.errores = true;
			}
			
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
					{
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Medicamentos Controlados Administrados ", "3", "90"));
						this.errores = true;
					}
				}
			}
			
			if(errores.isEmpty()) {
				this.errores = false;
			}
		}
    	
    	logger.info("UHBO ERRORES? "+(!errores.isEmpty()));
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
	 * @return the centroAtencion
	 */
	public HashMap getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(HashMap centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroAtencion(String key) 
	{
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) 
	{
		this.centroAtencion.put(key, value);
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenioSeleccionado
	 */
	public String getConvenioSeleccionado() {
		return convenioSeleccionado;
	}

	/**
	 * @param convenioSeleccionado the convenioSeleccionado to set
	 */
	public void setConvenioSeleccionado(String convenioSeleccionado) {
		this.convenioSeleccionado = convenioSeleccionado;
	}

	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}

	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * @return the montoBaseFinal
	 */
	public String getMontoBaseFinal() {
		return montoBaseFinal;
	}

	/**
	 * @param montoBaseFinal the montoBaseFinal to set
	 */
	public void setMontoBaseFinal(String montoBaseFinal) {
		this.montoBaseFinal = montoBaseFinal;
	}

	/**
	 * @return the montoBaseInicial
	 */
	public String getMontoBaseInicial() {
		return montoBaseInicial;
	}

	/**
	 * @param montoBaseInicial the montoBaseInicial to set
	 */
	public void setMontoBaseInicial(String montoBaseInicial) {
		this.montoBaseInicial = montoBaseInicial;
	}

	/**
	 * @return the tope
	 */
	public String getTope() {
		return tope;
	}

	/**
	 * @param tope the tope to set
	 */
	public void setTope(String tope) {
		this.tope = tope;
	}
	
	/**
	 * @return the ConsumosPorFacturarPacientesHospitalizados
	 */
	public HashMap getConsumosPorFacturarPacientesHospitalizados() {
		return consumosPorFacturarPacientesHospitalizados;
	}

	/**
	 * @param consumosPorFacturarPacientesHospitalizados the consumosPorFacturarPacientesHospitalizados to set
	 */
	public void setConsumosPorFacturarPacientesHospitalizados(HashMap consumosPorFacturarPacientesHospitalizados) {
		this.consumosPorFacturarPacientesHospitalizados = consumosPorFacturarPacientesHospitalizados;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getConsumosPorFacturarPacientesHospitalizados(String key) 
	{
		return consumosPorFacturarPacientesHospitalizados.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsumosPorFacturarPacientesHospitalizados(String key, Object value) 
	{
		this.consumosPorFacturarPacientesHospitalizados.put(key, value);
	}

	/**
	 * @return the archivo
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return the pathArchivoTxt
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * @param pathArchivoTxt the pathArchivoTxt to set
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * @return the zip
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}
	
}