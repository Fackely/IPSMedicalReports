package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.salasCirugia.Peticion;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Luis Gabriel Chavez
 *
 *Clase que consulta la información utilizada para la funcionalidad
 *Consulta Materiales Quirúrgicos
 */
public class ConsultaImpresionMaterialesQxForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MaterialesQxForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Variable que almacena el estado que debe direccionar el evento del boton volver
	 */
	private String estadoVolver;
	/**
	 * Variable que almacena el estado que debe direccionar el evento del bo
	 */
	private String estadoVolverListado;
	
	private HashMap ingresosPacientes=new HashMap();
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	/**
	 * indentifica el indice del mapa que se va a detallar
	 */
	private int index;
	
	/**
	 * indentifica el indice del mapa que se va a detallar paso 2
	 */
	private int index1;
	
	/**
	 * Mapa que almacena las solicitudes de cirugias del paciente
	 */
	private HashMap solicitudesQx=new HashMap();
	
	
	/**
	 * Mapa que almacena el detalle de consumo de materiales
	 */
	private HashMap consumoM= new HashMap();
	
	/**
	 * Mapa que almacena el detalle de servicios
	 */
	private HashMap consumoServiciosM= new HashMap();
	
	
	private String fechaInicial="";
	private String fechaFinal="";
	
	private HashMap filtros=new HashMap();
	private ArrayList<HashMap<String,Object>> centroAtencion;
	private ArrayList<HashMap<String,Object>> centrosCosto;
	private String codigoCentroAtencion;
	private boolean esRango;
	
	
	/****************************************************
	 * Opciones de impresion
	 ***************************************************/
	private String impMaterialesEsp="N";
	private String impCosto="N";
	private String impTarifa="N";
	
	
	
	
	
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
		ActionErrors errors= new ActionErrors();
		
		if(this.estado.equals("cargarDetalleRango"))
		{
			
			//Validacion rango fechas
			if(!this.getFechaInicial().equals("")&&!this.getFechaFinal().equals(""))
			{
				if(UtilidadFecha.validarFecha(this.getFechaInicial())&&UtilidadFecha.validarFecha(this.getFechaFinal()))
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(),UtilidadFecha.getFechaActual()))
						errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinal(),UtilidadFecha.getFechaActual()))
						errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(),this.getFechaInicial()))
						errors.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
					else if(UtilidadFecha.numeroMesesEntreFechas(this.getFechaInicial(),this.getFechaFinal(),true)>3)
						errors.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","  "));
				}
				else
				{
					if(!UtilidadFecha.validarFecha(this.getFechaInicial()))
						errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
					if(!UtilidadFecha.validarFecha(this.getFechaFinal()))
						errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
					
				}
			}
			else
			{
				if(this.getFechaInicial().equals(""))
					errors.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
				else if(!UtilidadFecha.validarFecha(this.getFechaInicial()))
					errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				
				if(this.getFechaFinal().equals(""))
					errors.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
				else if(!UtilidadFecha.validarFecha(this.getFechaFinal()))
					errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinal(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
			}			

			
			
			
		}
		return errors;
	}
	
	/**
	 * reset de los datos de la this
	 *
	 */
	public void reset()
	{	
		this.impCosto="";
		this.impTarifa="";
		this.impMaterialesEsp="";
		this.ingresosPacientes=new HashMap();
		this.ingresosPacientes.put("numRegistros", 0);
		this.estado = "";
		this.estadoVolver = "";
		this.estadoVolverListado = "";
		this.solicitudesQx=new HashMap();
		this.solicitudesQx.put("numRegistros", 0);
		this.consumoM=new HashMap();
		this.consumoM.put("numRegistros", 0);
		this.consumoServiciosM=new HashMap();
		this.consumoServiciosM.put("numRegistros", 0);
		this.fechaFinal="";
		this.fechaInicial="";
		this.filtros=new HashMap();
		this.filtros.put("numRegistros", 0);

	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the ingresosPacientes
	 */
	public HashMap getIngresosPacientes() {
		return ingresosPacientes;
	}

	/**
	 * @param ingresosPacientes the ingresosPacientes to set
	 */
	public void setIngresosPacientes(HashMap ingresosPacientes) {
		this.ingresosPacientes = ingresosPacientes;
	}
	
	
	/**
	 * @return the ingresosPacientes
	 */
	public Object getIngresosPacientes(String key) {
		return ingresosPacientes.get(key);
	}

	/**
	 * @param ingresosPacientes the ingresosPacientes to set
	 */
	public void setIngresosPacientes(String key, Object o) {
		this.ingresosPacientes.put(key, o);
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
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
	 * @return the solicitudesQx
	 */
	public HashMap getSolicitudesQx() {
		return solicitudesQx;
	}

	/**
	 * @param solicitudesQx the solicitudesQx to set
	 */
	public void setSolicitudesQx(HashMap solicitudesQx) {
		this.solicitudesQx = solicitudesQx;
	}
	
	
	/**
	 * @return the ingresosPacientes
	 */
	public Object getSolicitudesQx(String key) {
		return solicitudesQx.get(key);
	}

	/**
	 * @param ingresosPacientes the ingresosPacientes to set
	 */
	public void setSolicitudesQx(String key, Object o) {
		this.solicitudesQx.put(key, o);
	}

	/**
	 * @return the index1
	 */
	public int getIndex1() {
		return index1;
	}

	/**
	 * @param index1 the index1 to set
	 */
	public void setIndex1(int index1) {
		this.index1 = index1;
	}

	/**
	 * @return the consumoM
	 */
	public HashMap getConsumoM() {
		return consumoM;
	}

	/**
	 * @param consumoM the consumoM to set
	 */
	public void setConsumoM(HashMap consumoM) {
		this.consumoM = consumoM;
	}

	/**
	 * @return the consumoServiciosM
	 */
	public HashMap getConsumoServiciosM() {
		return consumoServiciosM;
	}

	/**
	 * @param consumoServiciosM the consumoServiciosM to set
	 */
	public void setConsumoServiciosM(HashMap consumoServiciosM) {
		this.consumoServiciosM = consumoServiciosM;
	}

	/**
	 * @return the impCosto
	 */
	public String getImpCosto() {
		return impCosto;
	}

	/**
	 * @param impCosto the impCosto to set
	 */
	public void setImpCosto(String impCosto) {
		this.impCosto = impCosto;
	}

	/**
	 * @return the impMaterialesEsp
	 */
	public String getImpMaterialesEsp() {
		return impMaterialesEsp;
	}

	/**
	 * @param impMaterialesEsp the impMaterialesEsp to set
	 */
	public void setImpMaterialesEsp(String impMaterialesEsp) {
		this.impMaterialesEsp = impMaterialesEsp;
	}

	/**
	 * @return the impTarifa
	 */
	public String getImpTarifa() {
		return impTarifa;
	}

	/**
	 * @param impTarifa the impTarifa to set
	 */
	public void setImpTarifa(String impTarifa) {
		this.impTarifa = impTarifa;
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
	 * @return the centroAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(ArrayList<HashMap<String, Object>> centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
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
	 * @return the filtros
	 */
	public HashMap getFiltros() {
		return filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(HashMap filtros) {
		this.filtros = filtros;
	}

	/**
	 * @return the esRango
	 */
	public boolean isEsRango() {
		return esRango;
	}

	/**
	 * @param esRango the esRango to set
	 */
	public void setEsRango(boolean esRango) {
		this.esRango = esRango;
	}

	/**
	 * @return the estadoVolver
	 */
	public String getEstadoVolver() {
		return estadoVolver;
	}

	/**
	 * @param estadoVolver the estadoVolver to set
	 */
	public void setEstadoVolver(String estadoVolver) {
		this.estadoVolver = estadoVolver;
	}

	/**
	 * @return the estadoVolverListado
	 */
	public String getEstadoVolverListado() {
		return estadoVolverListado;
	}

	/**
	 * @param estadoVolverListado the estadoVolverListado to set
	 */
	public void setEstadoVolverListado(String estadoVolverListado) {
		this.estadoVolverListado = estadoVolverListado;
	}

	
	
}
