/*
 * Dic 06, 2005
 *
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Consulta Liquidación Qx.
 */
public class ConsultaLiquidacionQxForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConsultaLiquidacionQxForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	//***********ATRIBUTOS GENERALES************************************
	
	/**
	 * Codigo de la orden
	 */
	private int numeroSolicitud;
	
	//***********ATRIBUTOS DEL LISTADO DE ORDENES***************************
	/**
	 * Mapa de las ordenes de cirugia asociadas a la cuenta
	 * que se va a liquidar
	 */
	private HashMap ordenes = new HashMap();
	
	/**
	 * Número de registros del mapa ordenes
	 */
	private int numOrdenes;
	
	/**
	 * columna a ordenar del pager
	 */
	private String indice;
	
	/**
	 * última columna por la cual se ordenó
	 */
	private String ultimoIndice;
	
	/**
	 * offset
	 */
	private int offset;
	
	/**
	 * linkSiguiente en la paginación
	 */
	private String linkSiguiente;
	
	/**
	 * Nº Máximo de registros por página
	 */
	private int maxPageItems;
	//************************************************************************
	//************ATRIBUTOS DEL DETALLE***************************************
	
	/**
	 * Nombre del responsable de la cuenta
	 */
	private String convenio;
	
	/**
	 *ID de la cuenta del paciente 
	 */
	private int idCuenta ;
	
	/**
	 * Nombre del paciente de la cirugía
	 */
	private String nombrePaciente;
	
	/**
	 * Arreglo que almacena los convenios de una solicitud
	 */
	private HashMap conveniosSol = new HashMap();
	private int posConvenio;
	
	/**
	 * Almacena datos generales de la orden
	 */
	private HashMap encabezadoSolicitud = new HashMap();
	
	/**
	 * Almacena datos de las cirugias de la orden
	 */
	private HashMap cirugiasSolicitud = new HashMap();
	
	/**
	 * Número de cirugias de la orden
	 */
	private int numCirugiasSolicitud;
	
	/**
	 * Asocios de la orden
	 */
	private HashMap asocios = new HashMap();
	
	/**
	 * Datos sobre la hoja quirurgica
	 */
	private HashMap hojaQx = new HashMap();
	
	/**
	 * Datos de la hoja de anestesia
	 */
	private HashMap hojaAnestesia = new HashMap();
	
	/**
	 * Objeto que muestra los materiales especiales
	 */
	private ArrayList<HashMap<String, Object>> materialesEspeciales = new ArrayList<HashMap<String,Object>>();
	//************************************************************************
	//***************ATRIBUTOS PROPIOS DE OPCION POR TODOS*******************
	
	/**
	 * Rango de ordenes Qx.
	 */
	private String ordenInicial;
	private String ordenFinal;
	
	/**
	 * Rango de fechas de orden
	 */
	private String fechaOrdenInicial;
	private String fechaOrdenFinal;
	
	/**
	 * Rango de fechas de cirugias
	 */
	private String fechaCxInicial;
	private String fechaCxFinal;
	
	/**
	 * código del medico que responde
	 */
	private int medico;
	
	/**
	 * código de la institucion
	 */
	private String institucion;
	//*************************************************************************
	
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
		
		if (this.estado.equals("consultar"))
		{
			//revision de fechas
			errores = this.revisionFechas(errores,this.fechaOrdenInicial,this.fechaOrdenFinal,"Orden");
			errores = this.revisionFechas(errores,this.fechaCxInicial,this.fechaCxFinal,"Cirugía");
			
			
			
			//revision rango ordenes
			if(!ordenInicial.equals("")&&!ordenFinal.equals(""))
			{
				if(Utilidades.convertirAEntero(this.ordenInicial)==ConstantesBD.codigoNuncaValido)
					errores.add("", new ActionMessage("errors.integer","El N° orden inicial"));
				if(Utilidades.convertirAEntero(this.ordenFinal)==ConstantesBD.codigoNuncaValido)
					errores.add("", new ActionMessage("errors.integer","El N° orden final"));
				if(Utilidades.convertirAEntero(ordenFinal)<Utilidades.convertirAEntero(ordenInicial))
					errores.add("orden Final menor que la orden inicial",new ActionMessage("errors.integerMenorIgualQue","El Nº de orden inicial","el Nº de orden final"));
			}
			else if(ordenInicial.equals("")&&!ordenFinal.equals(""))
				errores.add("orden inicial requerido",new ActionMessage("errors.required","El Nº de orden inicial"));
			else if(!ordenInicial.equals("")&&ordenFinal.equals(""))
				errores.add("orden final requerido",new ActionMessage("errors.required","El Nº de orden final"));
			
			//que se haya parametrizado al menos uno
			if(this.fechaCxInicial.equals("")&&this.fechaCxFinal.equals("")&&
				this.fechaOrdenInicial.equals("")&&this.fechaOrdenFinal.equals("")&&
				this.ordenInicial.equals("")&&this.ordenFinal.equals("")&&this.medico<=0)
				errores.add("Campos Requeridos",new ActionMessage("errors.minimoCampos","un campo","búsqueda"));
			
			
		}
		return errores;
	}
	
	
	/**
	 * Método para la revisión de los rangos de las fechas
	 * @param errores
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	private ActionErrors revisionFechas(ActionErrors errores,String fechaInicial,String fechaFinal,String tipoFecha) {
		int resp1=0;
		int resp2=0;
		
		if(!fechaInicial.equals(""))
		{
			resp1=1;
			if(UtilidadFecha.validarFecha(fechaInicial))
				resp1=2;
			else
				errores.add("fecha inicial", new ActionMessage("errors.formatoFechaInvalido",fechaInicial));
		}
		
		if(!fechaFinal.equals(""))
		{
			resp2=1;
			if(UtilidadFecha.validarFecha(fechaFinal))
				resp2=2;
			else
				errores.add("fecha final", new ActionMessage("errors.formatoFechaInvalido",fechaFinal));
		}
		
		//revisar si las fechas son válidas
		if(resp1==2&&resp2==2)
		{
			//si la fecha inicial es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial " + tipoFecha, "del sistema"));
			}
			
			//si la fecha final es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(fechaFinal)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","final "+tipoFecha, "del sistema"));
			}
			
			//si la fecha inicial es mayor a la fecha final
			if((UtilidadFecha.conversionFormatoFechaABD(fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinal))>0)
			{
				errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial "+tipoFecha, "final "+tipoFecha));
			}
		}
		else
		{
			//caso en el que falte alguna fecha del rango
			if(resp1==0&&resp2>0)
			{
				errores.add("La Fecha Inicial", new ActionMessage("errors.required", "La fecha inicial "+tipoFecha));
			}
			
			if(resp2==0&&resp1>0)
			{
				errores.add("La Fecha Final", new ActionMessage("errors.required", "La fecha final "+tipoFecha));
			}
			
			
		}
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		
		//atributos generales
		this.numeroSolicitud = 0;
		
		//atributos del listado de ordenes
		this.ordenes = new HashMap();
		this.numOrdenes = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.offset = 0;
		this.linkSiguiente = "";
		this.maxPageItems = 0;
		
		//atributos del detalle
		this.conveniosSol = new HashMap();
		this.posConvenio = 0;
		this.encabezadoSolicitud = new HashMap();
		this.cirugiasSolicitud = new HashMap();
		this.numCirugiasSolicitud = 0;
		this.asocios = new HashMap();
		this.hojaQx = new HashMap();
		this.hojaAnestesia = new HashMap();
		this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
		this.idCuenta = 0;
		this.convenio = "";
		this.nombrePaciente = "";
		
		//atributos opcion POR TODOS
		this.ordenInicial = "";
		this.ordenFinal = "";
		this.fechaOrdenInicial = "";
		this.fechaOrdenFinal = "";
		this.fechaCxInicial = "";
		this.fechaCxFinal = "";
		this.medico = -1;
		this.institucion = "";
		
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
	 * @return Returns the numOrdenes.
	 */
	public int getNumOrdenes() {
		return numOrdenes;
	}
	/**
	 * @param numOrdenes The numOrdenes to set.
	 */
	public void setNumOrdenes(int numOrdenes) {
		this.numOrdenes = numOrdenes;
	}
	/**
	 * @return Returns the ordenes.
	 */
	public HashMap getOrdenes() {
		return ordenes;
	}
	/**
	 * @param ordenes The ordenes to set.
	 */
	public void setOrdenes(HashMap ordenes) {
		this.ordenes = ordenes;
	}
	
	/**
	 * @return Retorna un elemento del mapa ordenes.
	 */
	public Object getOrdenes(String key) {
		return ordenes.get(key);
	}
	/**
	 * @param asigna un elemento al mapa ordenes.
	 */
	public void setOrdenes(String key,Object obj) {
		this.ordenes.put(key,obj);
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the encabezadoSolicitud.
	 */
	public HashMap getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}
	/**
	 * @param encabezadoSolicitud The encabezadoSolicitud to set.
	 */
	public void setEncabezadoSolicitud(HashMap encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
	}
	
	/**
	 * @return Retorna un elemento del mapa encabezadoSolicitud.
	 */
	public Object getEncabezadoSolicitud(String key) {
		return encabezadoSolicitud.get(key);
	}
	/**
	 * @param asigna un elemento al mapa encabezadoSolicitud.
	 */
	public void setEncabezadoSolicitud(String key,Object obj) {
		this.encabezadoSolicitud.put(key,obj);
	}
	/**
	 * @return Returns the cirugiasSolicitud.
	 */
	public HashMap getCirugiasSolicitud() {
		return cirugiasSolicitud;
	}
	/**
	 * @param cirugiasSolicitud The cirugiasSolicitud to set.
	 */
	public void setCirugiasSolicitud(HashMap cirugiasSolicitud) {
		this.cirugiasSolicitud = cirugiasSolicitud;
	}
	/**
	 * @return Returns the numCirugiasSolicitud.
	 */
	public int getNumCirugiasSolicitud() {
		return numCirugiasSolicitud;
	}
	/**
	 * @param numCirugiasSolicitud The numCirugiasSolicitud to set.
	 */
	public void setNumCirugiasSolicitud(int numCirugiasSolicitud) {
		this.numCirugiasSolicitud = numCirugiasSolicitud;
	}
	
	/**
	 * @return Retorna un elemento del mapa cirugiasSolicitud.
	 */
	public Object getCirugiasSolicitud(String key) {
		return cirugiasSolicitud.get(key);
	}
	/**
	 * @param asigna un elemento al mapa cirugiasSolicitud .
	 */
	public void setCirugiasSolicitud(String key,Object obj) {
		this.cirugiasSolicitud.put(key,obj);
	}
	/**
	 * @return Returns the asocios.
	 */
	public HashMap getAsocios() {
		return asocios;
	}
	/**
	 * @param asocios The asocios to set.
	 */
	public void setAsocios(HashMap asocios) {
		this.asocios = asocios;
	}
	/**
	 * @return Retorna un elemento del mapa asocios.
	 */
	public Object getAsocios(String key) {
		return asocios.get(key);
	}
	/**
	 * @param asigna un elemento al mapa asocios.
	 */
	public void setAsocios(String key,Object obj) {
		this.asocios.put(key,obj);
	}
	/**
	 * @return Returns the hojaQx.
	 */
	public HashMap getHojaQx() {
		return hojaQx;
	}
	/**
	 * @param hojaQx The hojaQx to set.
	 */
	public void setHojaQx(HashMap hojaQx) {
		this.hojaQx = hojaQx;
	}
	
	/**
	 * @return Retorna un elemento del mapa hojaQx.
	 */
	public Object getHojaQx(String key) {
		return hojaQx.get(key);
	}
	/**
	 * @param asigna un elemento al mapa hojaQx .
	 */
	public void setHojaQx(String key,Object obj) {
		this.hojaQx.put(key,obj);
	}
	/**
	 * @return Returns the hojaAnestesia.
	 */
	public HashMap getHojaAnestesia() {
		return hojaAnestesia;
	}
	/**
	 * @param hojaAnestesia The hojaAnestesia to set.
	 */
	public void setHojaAnestesia(HashMap hojaAnestesia) {
		this.hojaAnestesia = hojaAnestesia;
	}
	
	/**
	 * @return Retorna un elemento del mapa hojaAnestesia.
	 */
	public Object getHojaAnestesia(String key) {
		return hojaAnestesia.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa hojaAnestesia .
	 */
	public void setHojaAnestesia(String key,Object obj) {
		this.hojaAnestesia.put(key,obj);
	}
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the fechaCxFinal.
	 */
	public String getFechaCxFinal() {
		return fechaCxFinal;
	}
	/**
	 * @param fechaCxFinal The fechaCxFinal to set.
	 */
	public void setFechaCxFinal(String fechaCxFinal) {
		this.fechaCxFinal = fechaCxFinal;
	}
	/**
	 * @return Returns the fechaCxInicial.
	 */
	public String getFechaCxInicial() {
		return fechaCxInicial;
	}
	/**
	 * @param fechaCxInicial The fechaCxInicial to set.
	 */
	public void setFechaCxInicial(String fechaCxInicial) {
		this.fechaCxInicial = fechaCxInicial;
	}
	/**
	 * @return Returns the fechaOrdenFinal.
	 */
	public String getFechaOrdenFinal() {
		return fechaOrdenFinal;
	}
	/**
	 * @param fechaOrdenFinal The fechaOrdenFinal to set.
	 */
	public void setFechaOrdenFinal(String fechaOrdenFinal) {
		this.fechaOrdenFinal = fechaOrdenFinal;
	}
	/**
	 * @return Returns the fechaOrdenInicial.
	 */
	public String getFechaOrdenInicial() {
		return fechaOrdenInicial;
	}
	/**
	 * @param fechaOrdenInicial The fechaOrdenInicial to set.
	 */
	public void setFechaOrdenInicial(String fechaOrdenInicial) {
		this.fechaOrdenInicial = fechaOrdenInicial;
	}
	/**
	 * @return Returns the medico.
	 */
	public int getMedico() {
		return medico;
	}
	/**
	 * @param medico The medico to set.
	 */
	public void setMedico(int medico) {
		this.medico = medico;
	}
	/**
	 * @return Returns the ordenFinal.
	 */
	public String getOrdenFinal() {
		return ordenFinal;
	}
	/**
	 * @param ordenFinal The ordenFinal to set.
	 */
	public void setOrdenFinal(String ordenFinal) {
		this.ordenFinal = ordenFinal;
	}
	/**
	 * @return Returns the ordenInicial.
	 */
	public String getOrdenInicial() {
		return ordenInicial;
	}
	/**
	 * @param ordenInicial The ordenInicial to set.
	 */
	public void setOrdenInicial(String ordenInicial) {
		this.ordenInicial = ordenInicial;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	/**
	 * @return Returns the nombrePaciente.
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}


	/**
	 * @param nombrePaciente The nombrePaciente to set.
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}


	/**
	 * @return the conveniosSol
	 */
	public HashMap getConveniosSol() {
		return conveniosSol;
	}


	/**
	 * @param conveniosSol the conveniosSol to set
	 */
	public void setConveniosSol(HashMap conveniosSol) {
		this.conveniosSol = conveniosSol;
	}
	
	/**
	 * @return Retorna elemento del mapa conveniosSol
	 */
	public Object getConveniosSol(String key) {
		return conveniosSol.get(key);
	}


	/**
	 * @param Asigna elemento al mapa conveniosSol 
	 */
	public void setConveniosSol(String key,Object obj) {
		this.conveniosSol.put(key,obj);
	}


	/**
	 * @return the posConvenio
	 */
	public int getPosConvenio() {
		return posConvenio;
	}


	/**
	 * @param posConvenio the posConvenio to set
	 */
	public void setPosConvenio(int posConvenio) {
		this.posConvenio = posConvenio;
	}


	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}


	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
	/**
	 * Método para retornar el tamanio de los materiales especiales
	 * @return
	 */
	public int getNumMaterialesEspeciales()
	{
		return this.materialesEspeciales.size();
	}
}
