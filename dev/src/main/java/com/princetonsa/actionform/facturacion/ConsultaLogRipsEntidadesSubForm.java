package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoFiltroConsultaProcesoRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;

public class ConsultaLogRipsEntidadesSubForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConsultaLogRipsEntidadesSubForm() {
	
	}
	
	private String path;
	
	/**
	 * Atributo donde se almacenan el estado o accion a realizar en las páginas
	 */
	private String estado;
	
	/**
	 * Atributo dto filtro que almacena los parametros para realizar la consulta
	 */
	private DtoFiltroConsultaProcesoRipsEntidadesSub dtoFiltroConsultaProcesoRips;
	
	/**
	 * Atributo que almacena el resultado de la consulta de log
	 */
	private ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaLogRips;
	
	/**
	 * Atributo que almacena el resultado de la consulta de log seleccionado
	 */
	private DtoResultadoConsultaLogRipsEntidadesSub logDetalleOrdenado;
	
	/**
	 * Atributo que almacena el resultado de la consulta de log seleccionado
	 */
	private DtoResultadoConsultaLogRipsEntidadesSub logDetallePorArchivo;
	
	/**
	 * Atributo donde se almacenan los usuarios existentes en el sistema.
	 */
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	/**
	 * Atributo donde se almacenan las entidades subcontratadas activas en el sistema
	 */
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSub;
	
	private long codigoPkLogSeleccionado;
	
	private long codigoPkLogArchivoSeleccionado;
	
	/**
	 * Atributo que alamcena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;
	
	/**
	 * Este método se encarga de inicializar todos los valores de la forma.
	 *  @author Fabian Becerra
     */
	public void reset() {
		this.dtoFiltroConsultaProcesoRips=new DtoFiltroConsultaProcesoRipsEntidadesSub();
		this.listaLogRips=new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		this.path="";
	}
	
	/**
	 * Este método se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Fabian Becerra
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1){

		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("consultar")){
			
			//validaciones rango de fechas
			if(this.dtoFiltroConsultaProcesoRips.getFechaInicial()==null){
				
				errores.add("La fecha Inicial es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicial"));
				
				
				
			}else{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.dtoFiltroConsultaProcesoRips.getFechaInicial());
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){
							
					 errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
									 "errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
				}
			}
			if(this.dtoFiltroConsultaProcesoRips.getFechaFinal()==null){
				
				errores.add("La fecha Fin es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Fin"));
				
			}else{
				String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
						this.dtoFiltroConsultaProcesoRips.getFechaFinal());
				
				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());
				
				
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFin, fechaActual)){
					errores.add("La fecha Fin es mayor que fecha actual", 
							new ActionMessage("errors.fechaPosteriorIgualActual", " Fin "+fechaFin," Actual "+fechaActual));
				
				}
				if(this.dtoFiltroConsultaProcesoRips.getFechaInicial()!=null){
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.dtoFiltroConsultaProcesoRips.getFechaInicial());					
					
					
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaFin,fechaInicial)){
						errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
										 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+fechaFin," Inicial "+fechaInicial));
					
					}
				}
			}
			
			
		}
		return errores;
	}
	
	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo estado
	 * 
	 * @param  valor para el atributo estado 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo filtroConsultaProcesoRips
	 * 
	 * @param  valor para el atributo filtroConsultaProcesoRips 
	 */
	public void setDtoFiltroConsultaProcesoRips(
			DtoFiltroConsultaProcesoRipsEntidadesSub dtoFiltroConsultaProcesoRips) {
		this.dtoFiltroConsultaProcesoRips = dtoFiltroConsultaProcesoRips;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo filtroConsultaProcesoRips
	 * 
	 * @return  Retorna la variable filtroConsultaProcesoRips
	 */
	public DtoFiltroConsultaProcesoRipsEntidadesSub getDtoFiltroConsultaProcesoRips() {
		return dtoFiltroConsultaProcesoRips;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo usuarios
	 * 
	 * @param  valor para el atributo usuarios
	 */
	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo usuarios
	 * 
	 * @return  Retorna la variable usuarios
	 */
	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo listaEntidadesSub
	 * 
	 * @param  valor para el atributo listaEntidadesSub 
	 */
	public void setListaEntidadesSub(ArrayList<DtoEntidadSubcontratada> listaEntidadesSub) {
		this.listaEntidadesSub = listaEntidadesSub;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaEntidadesSub
	 * 
	 * @return  Retorna la variable listaEntidadesSub
	 */
	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSub() {
		return listaEntidadesSub;
	}

	public void setListaLogRips(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaLogRips) {
		this.listaLogRips = listaLogRips;
	}

	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> getListaLogRips() {
		return listaLogRips;
	}

	public void setCodigoPkLogSeleccionado(long codigoPkLogSeleccionado) {
		this.codigoPkLogSeleccionado = codigoPkLogSeleccionado;
	}

	public long getCodigoPkLogSeleccionado() {
		return codigoPkLogSeleccionado;
	}

	public void setLogDetalleOrdenado(DtoResultadoConsultaLogRipsEntidadesSub logDetalleOrdenado) {
		this.logDetalleOrdenado = logDetalleOrdenado;
	}

	public DtoResultadoConsultaLogRipsEntidadesSub getLogDetalleOrdenado() {
		return logDetalleOrdenado;
	}

	public void setLogDetallePorArchivo(DtoResultadoConsultaLogRipsEntidadesSub logDetallePorArchivo) {
		this.logDetallePorArchivo = logDetallePorArchivo;
	}

	public DtoResultadoConsultaLogRipsEntidadesSub getLogDetallePorArchivo() {
		return logDetallePorArchivo;
	}

	public void setCodigoPkLogArchivoSeleccionado(
			long codigoPkLogArchivoSeleccionado) {
		this.codigoPkLogArchivoSeleccionado = codigoPkLogArchivoSeleccionado;
	}

	public long getCodigoPkLogArchivoSeleccionado() {
		return codigoPkLogArchivoSeleccionado;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public String getEsDescendente() {
		return esDescendente;
	}
	
	
}
