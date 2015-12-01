package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class ArticulosConsumidosPacientesForm extends ValidatorForm 
{

	private String estado="";
	
	private String centroAtencion;
	
	private String almacen;
	
	private String articulo;
	
	private String descripcionArticulo;
	
	private String clase;
		
	private String grupo;
		
	private String subGrupo;
		
	private String fechaInicial;
	
	private String fechaFinal;
		
	private String tipoCodigoArticulo;
	
	private String tipoInforme;
	
	private String descripcionClase;
	
	private String descripcionGrupo;
	
	private String descripcionSubGrupo;
	
	/**
     * Variable para manejar el mensaje
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Tipo de Salida manejado en el reporte (PDF Archivo Plano) 
	 */
	private String tipoSalida;
	
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
	 * HashMap con los resultados de la consulta de Movimientos Almacenes
	 */
	private HashMap articulosConsumidos;
	
	/**
	 * Metodo reset atributos form
	 */
	public void reset(String centroAtencion, String institucion)
	{
		this.centroAtencion=centroAtencion;
		this.almacen="";
		this.articulo="";
		this.clase="";
		this.grupo="";
		this.subGrupo="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.tipoCodigoArticulo="";
		this.tipoInforme="";
		this.descripcionArticulo="";
		this.descripcionClase="";
		this.descripcionGrupo="";
		this.descripcionSubGrupo="";
		this.tipoSalida = "";
		this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.articulosConsumidos = new HashMap();
    	this.articulosConsumidos.put("numRegistros", "0");
	}

	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("generar"))
		{
			if(this.centroAtencion==null || this.centroAtencion.equals(""))
			{
				errores.add("centroAtencion", new ActionMessage("errors.required","El Centro de Atención "));
				this.errores = true;
			}
			if(this.fechaInicial==null || this.fechaInicial.equals(""))
			{
				errores.add("fechaInicial", new ActionMessage("errors.required","La Fecha Inicial "));
				this.errores = true;
			}
			if(this.fechaFinal==null || this.fechaFinal.equals(""))
			{
				errores.add("fechaFinal", new ActionMessage("errors.required","La Fecha Final "));
				this.errores = true;
			}
			if(this.tipoCodigoArticulo==null || this.tipoCodigoArticulo.equals(""))
			{
				errores.add("tipoCodigo Articulo", new ActionMessage("errors.required","El Tipo Código Artículo "));
				this.errores = true;
			}
			if(this.tipoInforme==null || this.tipoInforme.equals(""))
			{
				errores.add("tipoInforme", new ActionMessage("errors.required","El Tipo Informe "));
				this.errores = true;
			}
			if(this.tipoSalida==null || this.tipoSalida.equals(""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo Salida "));
				this.errores = true;
			}
			
			if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
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
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=1)
					{
						errores.add("", new ActionMessage("error.inventarios.articulosConsumidosPacientes", "para consultar Artículos Consumidos por Pacientes"));
						this.errores = true;
					}
				}
			}
			
			if(errores.isEmpty())
				this.errores = false;
			
		}
		return errores;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getAlmacen() {
		return almacen;
	}

	/**
	 * 
	 * @param almacen
	 */
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	/**
	 * 
	 * @return
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * 
	 * @param articulo
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * 
	 * @param clase
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * 
	 * @param fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @param fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * 
	 * @param grupo
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getSubGrupo() {
		return subGrupo;
	}

	/**
	 * 
	 * @param subGrupo
	 */
	public void setSubGrupo(String subGrupo) {
		this.subGrupo = subGrupo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoCodigoArticulo() {
		return tipoCodigoArticulo;
	}

	/**
	 * 
	 * @param tipoCodigoArticulo
	 */
	public void setTipoCodigoArticulo(String tipoCodigoArticulo) {
		this.tipoCodigoArticulo = tipoCodigoArticulo;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoInforme() {
		return tipoInforme;
	}

	/**
	 * 
	 * @param tipoInforme
	 */
	public void setTipoInforme(String tipoInforme) {
		this.tipoInforme = tipoInforme;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	
	/**
	 * 
	 * @param descripcionArticulo
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescripcionClase() {
		return descripcionClase;
	}
	
	/**
	 * 
	 * @param descripcionClase
	 */
	public void setDescripcionClase(String descripcionClase) {
		this.descripcionClase = descripcionClase;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescripcionGrupo() {
		return descripcionGrupo;
	}
	
	/**
	 * 
	 * @param descripcionGrupo
	 */
	public void setDescripcionGrupo(String descripcionGrupo) {
		this.descripcionGrupo = descripcionGrupo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescripcionSubGrupo() {
		return descripcionSubGrupo;
	}
	
	/**
	 * 
	 * @param descripcionSubGrupo
	 */
	public void setDescripcionSubGrupo(String descripcionSubGrupo) {
		this.descripcionSubGrupo = descripcionSubGrupo;
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
	 * @return the articulosConsumidos
	 */
	public HashMap getArticulosConsumidos() {
		return articulosConsumidos;
	}

	/**
	 * @param articulosConsumidos the articulosConsumidos to set
	 */
	public void setArticulosConsumidos(HashMap articulosConsumidos) {
		this.articulosConsumidos = articulosConsumidos;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getArticulosConsumidos(String key){
		return articulosConsumidos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setArticulosConsumidos(String key, Object value){
		this.articulosConsumidos.put(key, value);
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
	
}