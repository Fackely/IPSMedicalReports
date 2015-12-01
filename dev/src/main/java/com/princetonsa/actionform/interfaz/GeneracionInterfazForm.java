/*
 * Junio 27, 2006
 */
package com.princetonsa.actionform.interfaz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.sysmedica.mundo.saludpublica.FichaEventosAdversosVacunacion;

import util.ConstantesBD;
import util.UtilidadFecha;

public class GeneracionInterfazForm extends ValidatorForm 
{
	
	/**
	 * Control del estado de la aplicacion
	 */
	private String estado;
	
	/**
	 * Codigo de la institucion del usuario en sesión
	 */
	private String institucion;
	
	/**
	 * Código del tipo de interfaz a generar
	 */
	private int tipoInterfaz;
	/**
	 * Descripcion del tipo de interfaz
	 */
	private String nombreTipoInterfaz;
	/**
	 * Fecha Inicial
	 */
	private String fechaInicial;
	/**
	 * Fecha Final
	 */
	private String fechaFinal;
	/**
	 * Fecha generacion interfaz
	 */
	private String fechaGeneracion;
	/**
	 * Hora generacion interfaz
	 */
	private String horaGeneracion;
	/**
	 * Documento Inicial
	 */
	private String documentoInicial;
	/**
	 * Documento Final
	 */
	private String documentoFinal;
	
	/**
	 * Codigo del convenio
	 */
	private int convenio;
	/**
	 * Nombre del convenio
	 */
	private String nombreConvenio;
	/**
	 * Variable que almacena la ruta y el archivo
	 * de salida
	 */
	private String pathSalida;
	/**
	 * Variable que almacena la ruta y el archivo
	 * de inconsistencias
	 */
	private String pathInconsistencias;
	
	
	/**
	 * Registros parametrizados
	 */
	private HashMap registros = new HashMap();
	
	/**
	 * Número de registros del mapa registros
	 */
	private int numRegistros;
	
	//******ATRIBUTOS PARA VALIDACION DE GENERACION INTERFAZ***********
	/**
	 * Mensajes de confirmacion
	 */
	private ArrayList confirmaciones;
	/**
	 * Indica resultado de confirmación de archivos existentes
	 */
	private boolean confirmacionArchivosExistentes;
	/**
	 * Indica resultado de confirmacion de generaciones de interfaz anteriores
	 * existentes
	 */
	private boolean confirmacionGeneracionesAnteriores;
	/**
	 * Variable que define que tipo de confirmacion se desea realizar
	 * sus posibles valores son: "archivo" y "generacion"
	 */
	private String tipoConfirmacion;
	
	//**************************************************************
	
	/**
	 * Mapa donde se almacena la informacion de las facturas/anulaciones
	 */
	private HashMap facturas = new HashMap();
	
	/**
	 * Variable que contiene el numero de registros del mapa facturas
	 */
	private int numFacturas;
	
	/**
	 * Variable que define el éxito de la generación
	 */
	private boolean exitoGeneracion;
	
	/**
	 * Objeto donde se almacena el contenido de una archivo
	 * (puede ser el de salida o el de inconsistencias)
	 */
	private HashMap mapArchivo = new HashMap();
	/**
	 * Variable donde se almacena el tamaño del archivo
	 */
	private int numMapArchivo;
	
	//atributos para el pager*****************************************
	private int maxPageItems;
	private String linkSiguiente;
	private int offset;
	private String indice;
	private String ultimoIndice;
	//*****************************************************************
	
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
		
		if(this.estado.equals("validar"))
		{
			//Validacion rango fechas***************************************************************
			if(!this.fechaInicial.equals("")&&!this.fechaFinal.equals(""))
			{
				if(UtilidadFecha.validarFecha(this.fechaInicial)&&UtilidadFecha.validarFecha(this.fechaFinal))
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
						errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
						errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
						errores.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
					
				}
				else
				{
					if(!UtilidadFecha.validarFecha(this.fechaInicial))
						errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
					if(!UtilidadFecha.validarFecha(this.fechaFinal))
						errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
					
				}
			}
			else
			{
				if(this.fechaInicial.equals(""))
					errores.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
				else if(!UtilidadFecha.validarFecha(this.fechaInicial))
					errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
					errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				
				if(this.fechaFinal.equals(""))
					errores.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
				else if(!UtilidadFecha.validarFecha(this.fechaFinal))
					errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
					errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
			}
			
			//Validar rango de documentos
			if(this.tipoInterfaz!=ConstantesBD.tipoInterfazAmbos)
			{
				if(!this.documentoInicial.equals("")&&!this.documentoFinal.equals(""))
				{
					int cuentaI = Integer.parseInt(this.documentoInicial);
					int cuentaF = Integer.parseInt(this.documentoFinal);
					
					if(cuentaI>cuentaF)
						errores.add("documento inicial > a documento final",new ActionMessage("errors.MenorIgualQue","El documento inicial","documento final"));
				}
				else
				{
					if(this.documentoInicial.equals("")&&!this.documentoFinal.equals(""))
						errores.add("Documento Inicial requerida",new ActionMessage("errors.required","El documento inicial"));
					if(this.documentoFinal.equals("")&&!this.documentoInicial.equals(""))
						errores.add("Documento Final requerida",new ActionMessage("errors.required","El documento Final"));
					
				}
			}
			
			//Validar PATHs
			errores = validarPATH(this.pathSalida,"El PATH y archivo de salida","el archivo de salida",errores);
			errores = validarPATH(this.pathInconsistencias,"El PATH y archivo de inconsistencias","el archivo de inconsistencias",errores);
			
			if(!this.pathSalida.equals("")&&!this.pathInconsistencias.equals(""))
				errores = validarNombreArchivo(this.pathSalida,this.pathInconsistencias,errores);
			
			if(!errores.isEmpty())
				this.estado = "parametrizar";
		}
		return errores;
	}
	
	/**
	 * Método implementado para verificar que los nombres de archivos
	 * sean diferentes
	 * @param salida
	 * @param inconsistencias
	 * @param errores 
	 * @return
	 */
	private ActionErrors validarNombreArchivo(String salida, String inconsistencias, ActionErrors errores) 
	{
		//Se toma nombre archivo de salida
		String aux0 = "";
		int indice = salida.lastIndexOf(System.getProperty("file.separator"));
		aux0 = salida.substring(indice+1,salida.length());
		
		//Se toma nombre archivo de inconsistencias
		String aux1 = "";
		indice = inconsistencias.lastIndexOf(System.getProperty("file.separator"));
		aux1 = inconsistencias.substring(indice+1,inconsistencias.length());
		
		if(aux0.equals(aux1))
			errores.add("Nombres de archivos iguales",new ActionMessage("error.interfaz.generacionInterfaz.nombreArchivoIgual"));
			
		return errores;
	}

	/**
	 * Método implementado para validar el path
	 * @param path
	 * @param mensaje1
	 * @param mensaje2
	 * @param errores
	 * @return
	 */
	private ActionErrors validarPATH(String path, String mensaje1,String mensaje2, ActionErrors errores) 
	{
		if(path.equals(""))
			errores.add("PATH y nombre archivo requerido",new ActionMessage("errors.required",mensaje1));
		else
		{
			String[] aux = new String[2];
			int indice = path.lastIndexOf(System.getProperty("file.separator"));
			aux[0] = path.substring(0,indice);
			aux[1] = path.substring(indice+1,path.length());
			
			File archivo = new File(aux[0]);
			
			if(!archivo.isDirectory()||!archivo.exists()||!archivo.isAbsolute())
			{
				errores.add("PATH y nombre archivo inválido",new ActionMessage("error.interfaz.generacionInterfaz.rutaInvalida",mensaje2));
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
		this.institucion = "";
		this.tipoInterfaz = 0;
		this.nombreTipoInterfaz = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.fechaGeneracion = "";
		this.horaGeneracion = "";
		this.documentoInicial = "";
		this.documentoFinal = "";
		this.convenio = 0;
		this.nombreConvenio = "";
		this.pathSalida = "";
		this.pathInconsistencias = "";
		
		this.registros = new HashMap();
		this.numRegistros = 0;
		
		this.confirmaciones = new ArrayList();
		this.confirmacionArchivosExistentes = false;
		this.confirmacionGeneracionesAnteriores = false;
		this.tipoConfirmacion = "";
		
		this.facturas = new HashMap();
		this.numFacturas = 0;
		
		this.exitoGeneracion = false;
		
		this.mapArchivo = new HashMap();
		this.numMapArchivo = 0;
		
		//atributos del paginador
		this.maxPageItems = 0;
		this.linkSiguiente = "";
		this.offset = 0;
		this.indice = "";
		this.ultimoIndice = "";
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the tipoInterfaz.
	 */
	public int getTipoInterfaz() {
		return tipoInterfaz;
	}

	/**
	 * @param tipoInterfaz The tipoInterfaz to set.
	 */
	public void setTipoInterfaz(int tipoInterfaz) {
		this.tipoInterfaz = tipoInterfaz;
	}

	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return Returns the documentoFinal.
	 */
	public String getDocumentoFinal() {
		return documentoFinal;
	}

	/**
	 * @param documentoFinal The documentoFinal to set.
	 */
	public void setDocumentoFinal(String documentoFinal) {
		this.documentoFinal = documentoFinal;
	}

	/**
	 * @return Returns the documentoInicial.
	 */
	public String getDocumentoInicial() {
		return documentoInicial;
	}

	/**
	 * @param documentoInicial The documentoInicial to set.
	 */
	public void setDocumentoInicial(String documentoInicial) {
		this.documentoInicial = documentoInicial;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the pathInconsistencias.
	 */
	public String getPathInconsistencias() {
		return pathInconsistencias;
	}

	/**
	 * @param pathInconsistencias The pathInconsistencias to set.
	 */
	public void setPathInconsistencias(String pathInconsistencias) {
		this.pathInconsistencias = pathInconsistencias;
	}

	/**
	 * @return Returns the pathSalida.
	 */
	public String getPathSalida() {
		return pathSalida;
	}

	/**
	 * @param pathSalida The pathSalida to set.
	 */
	public void setPathSalida(String pathSalida) {
		this.pathSalida = pathSalida;
	}

	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return Returns the registros.
	 */
	public HashMap getRegistros() {
		return registros;
	}

	/**
	 * @param registros The registros to set.
	 */
	public void setRegistros(HashMap registros) {
		this.registros = registros;
	}
	
	/**
	 * @return Retorna un elemento del mapa registros.
	 */
	public Object getRegistros(String key) {
		return registros.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa registros.
	 */
	public void setRegistros(String key,Object obj) {
		this.registros.put(key,obj);
	}

	/**
	 * @return Returns the confirmacionArchivosExistentes.
	 */
	public boolean isConfirmacionArchivosExistentes() {
		return confirmacionArchivosExistentes;
	}

	/**
	 * @param confirmacionArchivosExistentes The confirmacionArchivosExistentes to set.
	 */
	public void setConfirmacionArchivosExistentes(
			boolean confirmacionArchivosExistentes) {
		this.confirmacionArchivosExistentes = confirmacionArchivosExistentes;
	}

	/**
	 * @return Returns the confirmaciones.
	 */
	public ArrayList getConfirmaciones() {
		return confirmaciones;
	}

	/**
	 * @param confirmaciones The confirmaciones to set.
	 */
	public void setConfirmaciones(ArrayList confirmaciones) {
		this.confirmaciones = confirmaciones;
	}

	/**
	 * @return Returns the confirmacionGeneracionesAnteriores.
	 */
	public boolean isConfirmacionGeneracionesAnteriores() {
		return confirmacionGeneracionesAnteriores;
	}

	/**
	 * @param confirmacionGeneracionesAnteriores The confirmacionGeneracionesAnteriores to set.
	 */
	public void setConfirmacionGeneracionesAnteriores(
			boolean confirmacionGeneracionesAnteriores) {
		this.confirmacionGeneracionesAnteriores = confirmacionGeneracionesAnteriores;
	}

	/**
	 * @return Returns the tipoConfirmacion.
	 */
	public String getTipoConfirmacion() {
		return tipoConfirmacion;
	}

	/**
	 * @param tipoConfirmacion The tipoConfirmacion to set.
	 */
	public void setTipoConfirmacion(String tipoConfirmacion) {
		this.tipoConfirmacion = tipoConfirmacion;
	}

	/**
	 * @return Returns the facturas.
	 */
	public HashMap getFacturas() {
		return facturas;
	}

	/**
	 * @param facturas The facturas to set.
	 */
	public void setFacturas(HashMap facturas) {
		this.facturas = facturas;
	}
	
	/**
	 * @return Retorna un elemento de facturas.
	 */
	public Object getFacturas(String key) {
		return facturas.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa facturas.
	 */
	public void setFacturas(String key,Object obj) {
		this.facturas.put(key,obj);
	}

	/**
	 * @return Returns the numFacturas.
	 */
	public int getNumFacturas() {
		return numFacturas;
	}

	/**
	 * @param numFacturas The numFacturas to set.
	 */
	public void setNumFacturas(int numFacturas) {
		this.numFacturas = numFacturas;
	}



	/**
	 * @return Returns the exitoGeneracion.
	 */
	public boolean isExitoGeneracion() {
		return exitoGeneracion;
	}

	/**
	 * @param exitoGeneracion The exitoGeneracion to set.
	 */
	public void setExitoGeneracion(boolean exitoGeneracion) {
		this.exitoGeneracion = exitoGeneracion;
	}

	/**
	 * @return Returns the mapArchivo.
	 */
	public HashMap getMapArchivo() {
		return mapArchivo;
	}

	/**
	 * @param mapArchivo The mapArchivo to set.
	 */
	public void setMapArchivo(HashMap mapArchivo) {
		this.mapArchivo = mapArchivo;
	}
	
	/**
	 * @return Retorna un elemento de mapArchivo.
	 */
	public Object getMapArchivo(String key) {
		return mapArchivo.get(key);
	}

	/**
	 * @param Asigna un elemento a mapArchivo.
	 */
	public void setMapArchivo(String key,Object obj) {
		this.mapArchivo.put(key,obj);
	}

	/**
	 * @return Returns the numMapArchivo.
	 */
	public int getNumMapArchivo() {
		return numMapArchivo;
	}

	/**
	 * @param numMapArchivo The numMapArchivo to set.
	 */
	public void setNumMapArchivo(int numMapArchivo) {
		this.numMapArchivo = numMapArchivo;
	}

	/**
	 * @return Returns the fechaGeneracion.
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	/**
	 * @param fechaGeneracion The fechaGeneracion to set.
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	/**
	 * @return Returns the horaGeneracion.
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	/**
	 * @param horaGeneracion The horaGeneracion to set.
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	/**
	 * @return Returns the nombreTipoInterfaz.
	 */
	public String getNombreTipoInterfaz() {
		return nombreTipoInterfaz;
	}

	/**
	 * @param nombreTipoInterfaz The nombreTipoInterfaz to set.
	 */
	public void setNombreTipoInterfaz(String nombreTipoInterfaz) {
		this.nombreTipoInterfaz = nombreTipoInterfaz;
	}

	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
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
	
	
}
