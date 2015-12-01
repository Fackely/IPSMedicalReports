package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class FacturadosPorConvenioForm extends ValidatorForm
{

	private String estado;

	/**
	 * String para la fecha inicial para la busqueda de las facturas
	 */
	private String fechaInicial;
	
	/**
	 * String para la fecha final para la busqueda de las facturas
	 */
	private String fechaFinal;
	
    /**
     * Carga los datos del select de Convenios
     */
    private ArrayList<HashMap<String, Object>> convenios;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado; 
	
    /**
     * Carga los datos del select de contratos
     */
    private ArrayList<HashMap<String, Object>> contratos;
    
    /**
     * Variable que maneja el contrato seleccionado
     */
    private String contratoSeleccionado;
    
    /**
     * Check para servicios
     */
    private String checkServicios;
    
    /**
     * String para el radioButton de servicios
     */
    private String radioServicios;
    
    /**
     * Check para articulos
     */
    private String checkArticulos;
    
    /**
     * String para el radioButton de articulos
     */
    private String radioArticulos;
    
    /**
     * Tipo Salida Reporte (PDF, Archivo Plano)
     */
    private String tipoSalida;
    
    /**
     * HashMap con la informacion de la consulta de consolidados de facturacion por servicios
     */
    private HashMap facturadosPorConvenioServicios;
    
    /**
     * HashMap con la informacion de la consulta de consolidados de facturacion por Articulos
     */
    private HashMap facturadosPorConvenioArticulos;
    
    /**
	 * Mensaje generacion del archivo
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
	/**
	 * Path completo del archivo generado
	 */
	private String pathArchivoTxt;
	
	/**
	 * Controla si se genera el archivo o no?
	 */
	private boolean archivo;
	
	/**
	 * Controla si se generaron errores en validate para no mostrar el mensaje
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
	
	/**
	 * Metodo que inicializa las variables
	 */
	public void reset()
    {
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.convenioSeleccionado = "";
		this.contratos = new ArrayList<HashMap<String,Object>>();
		this.contratoSeleccionado = "";
		this.facturadosPorConvenioServicios = new HashMap();
		this.facturadosPorConvenioServicios.put("numRegistros", "0");
		this.facturadosPorConvenioArticulos = new HashMap();
		this.facturadosPorConvenioArticulos.put("numRegistros", "0");
		this.checkServicios = ConstantesBD.acronimoNo;
		this.radioServicios = "";
		this.checkArticulos = ConstantesBD.acronimoNo;
		this.radioArticulos = "";
		this.tipoSalida = "";
		this.pathArchivoTxt = "";
		this.archivo = false;
		this.errores = false;
		this.zip = false;
    }

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("generar"))
		{
			//Validamos el Campo Tipo de Salida que es Requerido
			if(this.tipoSalida.trim().equals("null") || this.tipoSalida.trim().equals(""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			//Validamos el Campo Fecha Inicial que es Requerido
			if(this.fechaInicial.trim().equals("null") || this.fechaInicial.trim().equals(""))
			{
				errores.add("fechaInicial", new ActionMessage("errors.required","La Fecha Inicial "));
				this.errores = true;
			}
			//Validamos el Campo Fecha Final que es Requerido
			if(this.fechaFinal.trim().equals("null") || this.fechaFinal.trim().equals(""))
			{
				errores.add("fechaFinal", new ActionMessage("errors.required","La Fecha Final "));
				this.errores = true;
			}
			//Hacemos las validaciones de los campos fecha
			if(!UtilidadTexto.isEmpty(this.fechaInicial) || !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
						this.errores = true;
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
					{
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Consolidado de Facturación", "3", "90"));
						this.errores = true;
					}
				}
			}
			//Validamos que sea necesario seleccionar el detalle por articulos o por servicios
			if(this.checkArticulos.trim().equals(ConstantesBD.acronimoNo) && this.checkServicios.trim().equals(ConstantesBD.acronimoNo))
			{
				errores.add("articulosServicios", new ActionMessage("error.facturacion.seleccionCriterio"));
				this.errores = true;
			}
			//Validamos que si el servicio fue seleccionado que me valide que alla un campo seleccionado
			if(this.checkServicios.trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.radioServicios.trim().equals("") || this.radioServicios.trim().equals("null"))
				{
					errores.add("grupoServicio", new ActionMessage("error.facturacion.seleccionCriterioDetalle","Servicios "));
					this.errores = true;
				}
			}
			//Validamos que si el articulo fue seleccionado que me valide que alla un campo seleccionado
			if(this.checkArticulos.trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.radioArticulos.trim().equals("") || this.radioArticulos.trim().equals("null"))
				{
					errores.add("grupoServicio", new ActionMessage("error.facturacion.seleccionCriterioDetalle","Artículos "));
					this.errores = true;
				}
			}
		}
		if(errores.isEmpty())
		{
			this.errores = false;
		}
		return errores;
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
	 * @return the contratos
	 */
	public ArrayList<HashMap<String, Object>> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<HashMap<String, Object>> contratos) {
		this.contratos = contratos;
	}

	/**
	 * @return the contratoSeleccionado
	 */
	public String getContratoSeleccionado() {
		return contratoSeleccionado;
	}

	/**
	 * @param contratoSeleccionado the contratoSeleccionado to set
	 */
	public void setContratoSeleccionado(String contratoSeleccionado) {
		this.contratoSeleccionado = contratoSeleccionado;
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
	 * @return the facturadosPorConvenioServicios
	 */
	public HashMap getFacturadosPorConvenioServicios() {
		return facturadosPorConvenioServicios;
	}

	/**
	 * @param facturadosPorConvenioServicios the facturadosPorConvenioServicios to set
	 */
	public void setFacturadosPorConvenioServicios(HashMap facturadosPorConvenioServicios) {
		this.facturadosPorConvenioServicios = facturadosPorConvenioServicios;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getFacturadosPorConvenioServicios(String key) 
	{
		return facturadosPorConvenioServicios.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setFacturadosPorConvenioServicios(String key, Object value) 
	{
		this.facturadosPorConvenioServicios.put(key, value);
	}
	
	/**
	 * @return the facturadosPorConvenioArticulos
	 */
	public HashMap getFacturadosPorConvenioArticulos() {
		return facturadosPorConvenioArticulos;
	}

	/**
	 * @param facturadosPorConvenioArticulos the facturadosPorConvenioArticulos to set
	 */
	public void setFacturadosPorConvenioArticulos(HashMap facturadosPorConvenioArticulos) {
		this.facturadosPorConvenioArticulos = facturadosPorConvenioArticulos;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getFacturadosPorConvenioArticulos(String key) 
	{
		return facturadosPorConvenioArticulos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setFacturadosPorConvenioArticulos(String key, Object value) 
	{
		this.facturadosPorConvenioArticulos.put(key, value);
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

	/**
	 * @return the checkArticulos
	 */
	public String getCheckArticulos() {
		return checkArticulos;
	}

	/**
	 * @param checkArticulos the checkArticulos to set
	 */
	public void setCheckArticulos(String checkArticulos) {
		this.checkArticulos = checkArticulos;
	}

	/**
	 * @return the checkServicios
	 */
	public String getCheckServicios() {
		return checkServicios;
	}

	/**
	 * @param checkServicios the checkServicios to set
	 */
	public void setCheckServicios(String checkServicios) {
		this.checkServicios = checkServicios;
	}

	/**
	 * @return the radioArticulos
	 */
	public String getRadioArticulos() {
		return radioArticulos;
	}

	/**
	 * @param radioArticulos the radioArticulos to set
	 */
	public void setRadioArticulos(String radioArticulos) {
		this.radioArticulos = radioArticulos;
	}

	/**
	 * @return the radioServicios
	 */
	public String getRadioServicios() {
		return radioServicios;
	}

	/**
	 * @param radioServicios the radioServicios to set
	 */
	public void setRadioServicios(String radioServicios) {
		this.radioServicios = radioServicios;
	}
	
}