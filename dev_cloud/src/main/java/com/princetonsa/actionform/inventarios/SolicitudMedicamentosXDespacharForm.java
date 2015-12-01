package com.princetonsa.actionform.inventarios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.inventarios.SolicitudMedicamentosXDespacharAction;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.historiaClinica.UtilidadesHistoriaClinica;


/**
 * Clase para el manejo de los reportes
 * de solicitud medicamentos por despachar
 * Date: 2008-09-24
 * @author cgarias@princetonsa.com
 */
public class SolicitudMedicamentosXDespacharForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * llaves con los filtros del reporte
	 * ----------------------------------
	 * 	- tipoReporte
	 * 	- centroAtencion
	 *  - almacen
	 *  - centroCostoSolicitante
	 *  - piso
	 * 	- fechaInicial
	 * 	- fechaFinal
	 * 	- tipoCodigo
	 */
	private HashMap filtros;
	
	/**
	 * Centros de Atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Almacenes
	 */
	private HashMap almacenes;
	
	/**
	 * Centros Costo Solicitantes
	 */
	private HashMap centrosCostoSolicitantes;
	
	/**
	 * Ingresos Por Paciente Map
	 */
	private HashMap ingresosPorPacienteMap;
	
	/**
	 * Convenios
	 */
	private ArrayList<HashMap<String,Object>> pisos;
	
	/**
	 * Codigo del Paciente cargado
	 */
	private int codPacienteCargado;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Indicador de posición en un mapa Cualkiera
	 */
	private int posMap; 
	
	/* ***************************************
	 * Atributos para manejar el archivo plano
	 * ***************************************/
	
	/**
	 * Atributo que le indica a la vista si se
	 * genero el archivo plano
	 */
	private boolean operacionTrue;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	private String pathArchivoPlano;
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivoPlano;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 
	
	/**
	 * idLlave es la posición i en el mapa que contiene el numero de ingreso del paciente
	 */
	private int idLlave = 0;
	/* *******************************************
	 * Fin Atributos para manejar el archivo plano
	 * *******************************************/
	
	/**
	 *
	 */
	public void reset()
	{
		this.filtros = new HashMap();
		this.filtros.put("tipoReporte", "");
		this.almacenes = new HashMap();
		this.almacenes.put("numRegistros", "0");
		this.centrosCostoSolicitantes = new HashMap();
		this.centrosCostoSolicitantes.put("numRegistros", "0");
		this.ingresosPorPacienteMap = new HashMap();
		this.ingresosPorPacienteMap.put("numRegistros", "0");
		this.centrosAtencion=new ArrayList();
		this.pisos=new ArrayList();
		this.codPacienteCargado = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.pathArchivoPlano = "";
		this.urlArchivoPlano = "";
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
        ActionErrors errores = new ActionErrors();
        errores = super.validate(mapping,request);
        
        if (this.estado.equals("cambiarTipoReporte"))
        {
        	if (this.filtros.get("tipoReporte").equals("3"))
        	{
        		if(this.codPacienteCargado<=0)
        		{
        			/*
        			 * Solución Tarea 62163
        			 * Solucionada por Felipe Pérez Granda
        			 * Se solicita cambiar la validación que se muestra cuando no existe un paciente cargado en sesión de manera que sea mas clara
        			 * Actualmente se está mostrando Paciente Cargado es requerido, por favor cambiarla por la que es sugerida en el anexo: 
        			 * No hay ningún paciente cargado. Para acceder a esta funcionalidad debe cargar un paciente. 
        			 */
        			//errores.add("Paciente Cargado", new ActionMessage("errors.required","Paciente Cargado"));
        			errores.add("Paciente Cargado", new ActionMessage("error.errorEnBlanco",
        				"No hay ningún paciente cargado en sesión. Para acceder a esta funcionalidad debe cargar un paciente"));
        			
        		}	
        	}
        }
       
        if (this.estado.equals("generarReporte")){
        	
        	// Validacion del almacen
        	if(filtros.get("almacen").equals(""))
				errores.add("almacen", new ActionMessage("errors.required", "Almacen"));
        	
        	// Validacion del tipo de codigo
        	if(filtros.get("tipoCodigo").equals(""))
				errores.add("tipoCodigo", new ActionMessage("errors.required", "Tipo Código"));
        	
        	// Validacion ingreso fecha inicial
        	if(filtros.get("fechaInicial").equals(""))
				errores.add("fechaInicial", new ActionMessage("errors.required", "Fecha Inicial"));
				
	        // Validacion ingreso fecha final
			if(filtros.get("fechaFinal").equals(""))
				errores.add("fechaFinal", new ActionMessage("errors.required", "Fecha Final"));
			
			/* *************************
			 * Validacion tipo de salida
			 ***************************/
			if(filtros.get("tipoSalida").equals(""))
				errores.add("tipoSalida", new ActionMessage("errors.required", "El Tipo de Salida"));
			
			if(errores.isEmpty()){
				// Validacion formato fecha inicial
	        	if(!UtilidadFecha.validarFecha(filtros.get("fechaInicial").toString()))
	        		errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido", "Inicial"));
					
		        // Validacion formato fecha final
				if(!UtilidadFecha.validarFecha(filtros.get("fechaFinal").toString()))
					errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido", "Final"));
			}
			
			if(errores.isEmpty()){
				// Validacion Fecha final menor igual a la actual
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(filtros.get("fechaFinal").toString(), UtilidadFecha.getFechaActual())){
		        	errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
		        }
		        
		        // Validacion fecha inicial menor o igual a la fecha final
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(filtros.get("fechaInicial").toString(), filtros.get("fechaFinal").toString())){
		        	errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Final"));
		        }
	        }
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
	 * @return the filtros
	 */
	public Object getFiltros(String llave) {
		return filtros.get(llave);
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(String llave, Object obj) {
		this.filtros.put(llave, obj);
	}

	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}

	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}

	/**
	 * @return the almacenes
	 */
	public HashMap getAlmacenes() {
		return almacenes;
	}

	/**
	 * @param almacenes the almacenes to set
	 */
	public void setAlmacenes(HashMap almacenes) {
		this.almacenes = almacenes;
	}
	
	/**
	 * @return the almacenes
	 */
	public Object getAlmacenes(String llave) {
		return almacenes.get(llave);
	}

	/**
	 * @param almacenes the almacenes to set
	 */
	public void setAlmacenes(String llave, Object obj) {
		this.almacenes.put(llave, obj);
	}

	/**
	 * @return the centrosCostoSolicitantes
	 */
	public HashMap getCentrosCostoSolicitantes() {
		return centrosCostoSolicitantes;
	}

	/**
	 * @param centrosCostoSolicitantes the centrosCostoSolicitantes to set
	 */
	public void setCentrosCostoSolicitantes(HashMap centrosCostoSolicitantes) {
		this.centrosCostoSolicitantes = centrosCostoSolicitantes;
	}
	
	/**
	 * @return the centrosCostoSolicitantes
	 */
	public Object getCentrosCostoSolicitantes(String llave) {
		return centrosCostoSolicitantes.get(llave);
	}

	/**
	 * @param centrosCostoSolicitantes the centrosCostoSolicitantes to set
	 */
	public void setCentrosCostoSolicitantes(String llave, Object obj) {
		this.centrosCostoSolicitantes.put(llave, obj);
	}

	/**
	 * @return the codPacienteCargado
	 */
	public int getCodPacienteCargado() {
		return codPacienteCargado;
	}

	/**
	 * @param codPacienteCargado the codPacienteCargado to set
	 */
	public void setCodPacienteCargado(int codPacienteCargado) {
		this.codPacienteCargado = codPacienteCargado;
	}

	/**
	 * @return the ingresosPorPacienteMap
	 */
	public HashMap getIngresosPorPacienteMap() {
		return ingresosPorPacienteMap;
	}

	/**
	 * @param ingresosPorPacienteMap the ingresosPorPacienteMap to set
	 */
	public void setIngresosPorPacienteMap(HashMap ingresosPorPacienteMap) {
		this.ingresosPorPacienteMap = ingresosPorPacienteMap;
	}

	/**
	 * @return the ingresosPorPacienteMap
	 */
	public Object getIngresosPorPacienteMap(String llave) {
		return ingresosPorPacienteMap.get(llave);
	}

	/**
	 * @param ingresosPorPacienteMap the ingresosPorPacienteMap to set
	 */
	public void setIngresosPorPacienteMap(String llave, Object obj) {
		this.ingresosPorPacienteMap.put(llave, obj);
	}
	
	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
	}
	
	/*
	 * Métodos para manejar los archivos planos
	 */
	//---------------operacion true ------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//--------------------------------------------------------------------------
	//-------Path Archivo Plano ------------------------------------------------
	/**
	 * @return the pathArchivoPlano
	 */
	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}

	/**
	 * @param pathArchivoPlano the pathArchivoPlano to set
	 */
	public void setPathArchivoPlano(String pathArchivoPlano) {
		this.pathArchivoPlano = pathArchivoPlano;
	}

	/**
	 * @return the urlArchivoPlano
	 */
	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}

	/**
	 * @param urlArchivoPlano the urlArchivoPlano to set
	 */
	public void setUrlArchivoPlano(String urlArchivoPlano) {
		this.urlArchivoPlano = urlArchivoPlano;
	}
	//-----------------------------------------------------------------------------
	//--------existe archivo ------------------------------------------------------
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	//------------------------------------------------------------------------------

	public void setIdLlave(int idLlave) {
		this.idLlave = idLlave;
	}

	public int getIdLlave() {
		return idLlave;
	}
	
	/*
	 * Fin Métodos para manejar los archivos planos
	 */
}	