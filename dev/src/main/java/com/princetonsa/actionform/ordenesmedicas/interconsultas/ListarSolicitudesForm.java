/*
 * Created on Feb 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.actionform.ordenesmedicas.interconsultas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.InfoDatosInt;

/**
 * @author rcancino
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ListarSolicitudesForm extends ActionForm {
	
	/**
	 * Para el manejo de resumen de atenciones
	 */
	private String encabezadoResumen;

	/**
	 * Ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	/**
			 * @author rcancino
			 *  indica  el numero de resultados de la busqueda */
	private int codigoCentroCosto;
	
	/**
		 * @author rcancino
		 *  indica  el numero de resultados de la busqueda */
	private int numeroResultados;
	/**
	 * @author rcancino
	 *  indica     si la solicitud se debe incluir en la epicrisis */
	private boolean vaEpicrisis;
	/**
	 * @author rcancino
	 *  indica    si la solicitud de interconsulta tiene una respuesta
	 * pediatrica */
	private boolean pediatrica;
	/**
	 * @author rcancino
	 *  indica  el tipo de solicitud que estyara en el resumen */
	private int codigoTipoSolicitud;
	/**
	 * @author rcancino
	 *  tipo de servicio solicitado, interpretar, responder o consultar/modificar 
	 *  */
	private String tipoServicio;
	/**
	 * @author rcancino
	 *  codigo correspondiente al paciente del que se listan las solicitudes
	 */
	private int codigoPaciente;
	 
	private String filtro="";
	
	/**
	 * 
	 */
	private String fechaInicialFiltro;
	
	/**
	 * 
	 */
	private String fechaFinalFiltro;
	/**
	 * 
	 */
	private String centroCostosSolicitanteFiltro;
	
	
	/**
	 * 
	 */
	private String estadoHCFiltro;
	
	/**
	 * 
	 */
	private String tipoOrdenFiltro;
	
	/**
	 * 
	 */
	private String areaFiltro;
	
	/**
	 * 
	 */
	private String centroCostoSolicitadoFiltro;
	
	public String getCentroCostoSolicitadoFiltro() {
		return centroCostoSolicitadoFiltro;
	}
	public void setCentroCostoSolicitadoFiltro(String centroCostoSolicitadoFiltro) {
		this.centroCostoSolicitadoFiltro = centroCostoSolicitadoFiltro;
	}
	/**
	 * 
	 */
	private String pisoFiltro;
	
	/**
	 * 
	 */
	private String habitacionFiltro;
	
	/**
	 * 
	 */
	private String camaFiltro;
	
	private boolean requierePortatilFiltro; 
	/**
	 * @author rcancino
	 * codigo correspondiente al medico que lista las solicitudes
	 */
	private int codigoMedico;
	/**
	 * @author rcancino
	 * tipo de solicitudes  a listar
	 */
	
	private InfoDatosInt tipoSolicitud;
	/**
	 * @author rcancino
	 * lista de solicitudes a mostrar
	 */
	private ArrayList listaSolicitudes;
	/**
	 * @author rcancino
	 * estado del form
	 */
	private String estado;
	/**
	 * @author rcancino
	 * estado de HC de la solicitud
	 */
	private String estadoHistoriaClinicaResumen;
	/**
		 * @author rcancino
		 * numero de la solicitud
		 */
	private int numeroSolicitud;
	
	/**
	 * encabezado parametrizable de la lista
	 * @return
	 */
	private Vector encabezado;
	/**
	 * bandera que indica si se puede modificar la solicitud
	 */
	private boolean modificar;
	/**
	 * bandera que indica si se puede responder la solicitud
	 */
	private boolean responder;
	/**
	 * bandera que indica si se puede interpretar la solicitud
	 */
	private boolean interpretar;
	/**
	 * indica el tipo de cambio que se desea realizar sobre la solicitud
	 */
	private String tipoModificacion;
	/**
	 * interpretacion guardada en BD de la solicitud
	 */
	private String ultimaInterpretacion;
	/**
	 * informacion parea adicionar a la interpretacion de la solicitud
	 */
	private String interpretacion;
	/**
	 * indica si se quiere obtener un listado para epicrisis
	 */
	private boolean epicrisis;
	/**
	 * Boolean que me indica si el listado de las solicitudes es por medico.
	 */
	private boolean listarPorMedico;
	
	/**
	 * Variables para manejar los mensajes de advertencia.
	 */
	private boolean mostrarMensaje;
	
	/**
	 * Variable que indica si el usuario del sistema
	 * pertenece al centro de costo en el que se 
	 * encuentra el paciente cargado
	 * 
	 */
	private boolean medicoMismoCentroCosto;
	
	/**
	 * Contiene el mapa de estados de Autorizacion 
	 */
	private HashMap estadoAuto;
	

	
	/**
	 * Contiene el mapa con las justificaciones dinamicas.
	 */
	public  HashMap justificacionDinamicaMap = new HashMap();
	
	/**
	 * Indica si es resumen de atenciones
	 */
	private boolean esResumenAtenciones=false;
	
	/**
	 * Indica si viene de PYP
	 */
	private boolean vieneDePyp = false;
	
	/**
	 * Adición de sebastián
	 * Variable que me permite saber lo siguiente:
	 * 0 => se van a listar las interconsultas o procedimientos del módulo órdenes médicas
	 * 1 => se van a listar las interconsultas o procedimientos de resumen de atenciones
	 * Esto se hace para que al ordenar no aparezca "volver al menú anterior"
	 */
	private String indicador;
	
	
	private int ind;

	/**
	 * Centro de costo Solicitante
	 */
	
	private int centroCostoSolicitante;
	
	/**
	 * Manejo solicitudes múltiples de procedimientos
	 */
	private boolean multiple;
	
    /**
     * restriccion
     */
	private String restriccion;
	
	/**
	 * Collection con las interconsultas y procedimientos
	 * asociadas a cada solicitud y mostrarlas en la ventana
	 */
	private Collection interconsultasProcedimientosSolicitud=null;
	
	/**
	 * 
	 * */
	private String cadenaFiltroBusqueda = "";
	
	
	
	
	
	
	// Anexo 37 - Cambio 1.50
	private boolean sinAutorizacionEntidadsubcontratada = false;
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	
	
	
	
	/**
	 * @return Returns the ind.
	 */
	public int getInd()
	{
		return ind;
	}
	/**
	 * @param ind The ind to set.
	 */
	public void setInd(int ind)
	{
		this.ind= ind;
	}
	/**
	 * @return
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @return
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @return
	 */
	public Collection getListaSolicitudes() {
		return listaSolicitudes;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param i
	 */
	public void setCodigoMedico(int i) {
		codigoMedico = i;
	}

	/**
	 * @param i
	 */
	public void setCodigoPaciente(int i) {
		codigoPaciente = i;
	}

	/**
	 * @param list
	 */
	public void setListaSolicitudes(ArrayList list) {
		listaSolicitudes = list;
	}

	/**
	 * @param int1
	 */
	public void setTipoSolicitud(InfoDatosInt int1) {
		tipoSolicitud = int1;
	}

	/**
	 * 
	 */
	
	public void reset() {
		//this.fechaInicialFiltro="";
		//this.fechaFinalFiltro="";
		//this.centroCostosSolicitanteFiltro="";
		this.encabezadoResumen="";
		this.codigoMedico=-1;
		this.codigoPaciente=-1;
		this.listaSolicitudes=new ArrayList();
		this.tipoSolicitud=new InfoDatosInt();
		this.encabezado=new Vector();
		this.estado="";
		this.estadoHistoriaClinicaResumen="";
		this.responder=false;
		this.interpretar=false;
		this.modificar=false;
		this.numeroSolicitud=-1;
		this.tipoServicio="";
		this.pediatrica=false;
		this.codigoTipoSolicitud=-1;
		this.tipoModificacion="";
		this.interpretacion="";
		this.ultimaInterpretacion="";
		this.vaEpicrisis=false;
		this.epicrisis=false;
		this.codigoCentroCosto=-1;
		this.ultimaPropiedad="";
		this.columna="";
		this.listarPorMedico=false;
		this.mostrarMensaje=false;
		this.medicoMismoCentroCosto=true;
		this.justificacionDinamicaMap= new HashMap();
		this.multiple=false;
		this.estadoHCFiltro="";
		this.tipoOrdenFiltro="";
		this.areaFiltro="";
		this.centroCostoSolicitadoFiltro="";
		this.pisoFiltro="";
		this.habitacionFiltro="";
		this.camaFiltro="";
		this.requierePortatilFiltro=false;	
		this.estadoAuto=new HashMap();
		
		//this.sinAutorizacionEntidadsubcontratada = false;
		this.listaAdvertencias = new ArrayList<String>();
   }

	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @return
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param string
	 */
	public void setTipoServicio(String string) {
		tipoServicio = string;
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
		// Perform validator framework validations

		ActionErrors errores = new ActionErrors();
			
		return errores;
	}	
	/**
	 * @return
	 */
	public Vector getEncabezado() {
		return encabezado;
	}

	/**
	 * @param listas
	 */
	public void setEncabezado(Vector encabezado) {
		this.encabezado = encabezado;
	}

	/**
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

	/**
	 * @return
	 */
	public String getEstadoHistoriaClinicaResumen() {
		return estadoHistoriaClinicaResumen;
	}

	/**
	 * @param string
	 */
	public void setEstadoHistoriaClinicaResumen(String string) {
		estadoHistoriaClinicaResumen = string;
	}

	/**
	 * Returns the interpretar.
	 * @return boolean
	 */
	public boolean isInterpretar() {
		return interpretar;
	}

	/**
	 * Returns the modificar.
	 * @return boolean
	 */
	public boolean isModificar() {
		return modificar;
	}

	/**
	 * Returns the responder.
	 * @return boolean
	 */
	public boolean isResponder() {
		return responder;
	}

	/**
	 * Sets the interpretar.
	 * @param interpretar The interpretar to set
	 */
	public void setInterpretar(boolean interpretar) {
		this.interpretar = interpretar;
	}

	/**
	 * Sets the modificar.
	 * @param modificar The modificar to set
	 */
	public void setModificar(boolean modificar) {
		this.modificar = modificar;
	}

	/**
	 * Sets the responder.
	 * @param responder The responder to set
	 */
	public void setResponder(boolean responder) {
		this.responder = responder;
	}

	/**
	 * Returns the pediatrica.
	 * @return boolean
	 */
	public boolean isPediatrica() {
		return pediatrica;
	}

	/**
	 * Sets the pediatrica.
	 * @param pediatrica The pediatrica to set
	 */
	public void setPediatrica(boolean pediatrica) {
		this.pediatrica = pediatrica;
	}

	/**
	 * Returns the codigoTipoSolicitud.
	 * @return int
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * Sets the codigoTipoSolicitud.
	 * @param codigoTipoSolicitud The codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}

	

	/**
	 * Returns the tipoModificacion.
	 * @return String
	 */
	public String getTipoModificacion() {
		return tipoModificacion;
	}

	/**
	 * Sets the tipoModificacion.
	 * @param tipoModificacion The tipoModificacion to set
	 */
	public void setTipoModificacion(String tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	/**
	 * Returns the interpretacion.
	 * @return String
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * Returns the ultimaInterpretacion.
	 * @return String
	 */
	public String getUltimaInterpretacion() {
		return ultimaInterpretacion;
	}

	/**
	 * Sets the interpretacion.
	 * @param interpretacion The interpretacion to set
	 */
	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	/**
	 * Sets the ultimaInterpretacion.
	 * @param ultimaInterpretacion The ultimaInterpretacion to set
	 */
	public void setUltimaInterpretacion(String ultimaInterpretacion) {
		this.ultimaInterpretacion = ultimaInterpretacion;
	}

	/**
	 * Returns the vaEpicrisis.
	 * @return boolean
	 */
	public boolean isVaEpicrisis() {
		return vaEpicrisis;
	}

	/**
	 * Sets the vaEpicrisis.
	 * @param vaEpicrisis The vaEpicrisis to set
	 */
	public void setVaEpicrisis(boolean vaEpicrisis) {
		this.vaEpicrisis = vaEpicrisis;
	}

	/**
	 * Returns the epicrisis.
	 * @return boolean
	 */
	public boolean isEpicrisis() {
		return epicrisis;
	}

	/**
	 * Sets the epicrisis.
	 * @param epicrisis The epicrisis to set
	 */
	public void setEpicrisis(boolean epicrisis) {
		this.epicrisis = epicrisis;
	}

	/**
		 * Método que guarda un valor en el mapa
		 * 
		 * @param key llave con la que quedará el
		 * valor a guardar
		 * @param value valor a guardar en el mapa
		 
		public void setValores(String key, Object value) 
		{
			valores.put(key, value);
		}

		** Método que recupera un valor del mapa
		 * 
		 * @param key llave con la que esta el valor
		 * buscado en el mapa
		 * @return
		 *
		public Object getValores(String key) 
		{
			return valores.get(key);
		}*/

	/**
	 * Returns the numeroResultados.
	 * @return int
	 */
	public int getNumeroResultados() {
		return numeroResultados;
	}

	/**
	 * Sets the numeroResultados.
	 * @param numeroResultados The numeroResultados to set
	 */
	public void setNumeroResultados(int numeroResultados) {
		this.numeroResultados = numeroResultados;
	}
	/**
	 * Method getValores.
	 * @return HashMap
	 *
	public HashMap getValores() {
		return valores;
	}*/

	/**
	 * Returns the codigoCentroCosto.
	 * @return int
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * Sets the codigoCentroCosto.
	 * @param codigoCentroCosto The codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}

	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}

	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return
	 */
	public String getEncabezadoResumen()
	{
		return encabezadoResumen;
	}

	/**
	 * @param string
	 */
	public void setEncabezadoResumen(String string)
	{
		encabezadoResumen = string;
	}

	/**
	 * @return Retorna el listarPorMedico.
	 */
	public boolean isListarPorMedico() {
		return listarPorMedico;
	}
	/**
	 * @param listarPorMedico Asigna el listarPorMedico.
	 */
	public void setListarPorMedico(boolean listarPorMedico) {
		this.listarPorMedico = listarPorMedico;
	}
	/**
	 * @return Returns the indicador.
	 */
	public String getIndicador() {
		return indicador;
	}
	/**
	 * @param indicador The indicador to set.
	 */
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

    /**
     * @return Retorna mostrarMensaje.
     */
    public boolean isMostrarMensaje() {
        return mostrarMensaje;
    }
    /**
     * @param mostrarMensaje Asigna mostrarMensaje.
     */
    public void setMostrarMensaje(boolean mostrarMensaje) {
        this.mostrarMensaje = mostrarMensaje;
    }
    /**
     * @return Retorna medicoMismoCentroCosto.
     */
    public boolean isMedicoMismoCentroCosto() {
        return medicoMismoCentroCosto;
    }
    /**
     * @param medicoMismoCentroCosto Asigna medicoMismoCentroCosto.
     */
    public void setMedicoMismoCentroCosto(boolean medicoMismoCentroCosto) {
        this.medicoMismoCentroCosto = medicoMismoCentroCosto;
    }
    /**
     * @return Returns the justificacionDinamicaMap.
     */
    public HashMap getJustificacionDinamicaMap() {
        return justificacionDinamicaMap;
    }
    /**
     * @param justificacionDinamicaMap The justificacionDinamicaMap to set.
     */
    public void setJustificacionDinamicaMap(HashMap justificacionDinamicaMap) {
        this.justificacionDinamicaMap = justificacionDinamicaMap;
    }
    /**
	 * Set del mapa de justificacion dinamica
	 * @param key
	 * @param value
	 */
	public void setJustificacionDinamicaMap(String key, Object value) 
	{
		justificacionDinamicaMap.put(key, value);
	}
	/**
	 * Get del mapa de justificacion dinamica
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getJustificacionDinamicaMap(String key) 
	{
		return justificacionDinamicaMap.get(key);
	}
    
    /**
     * @return Returns the esResumenAtenciones.
     */
    public boolean getEsResumenAtenciones() {
        return esResumenAtenciones;
    }
    /**
     * @param esResumenAtenciones The esResumenAtenciones to set.
     */
    public void setEsResumenAtenciones(boolean esResumenAtenciones) {
        this.esResumenAtenciones = esResumenAtenciones;
    }
    
	/**
	 * @return Returns the vieneDePyp.
	 */
	public boolean isVieneDePyp() {
		return vieneDePyp;
	}
	/**
	 * @param vieneDePyp The vieneDePyp to set.
	 */
	public void setVieneDePyp(boolean vieneDePyp) {
		this.vieneDePyp = vieneDePyp;
	}
	/**
	 * @return Retorna multiple.
	 */
	public boolean getMultiple()
	{
		return multiple;
	}
	/**
	 * @param multiple Asigna multiple.
	 */
	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}
	
	/**
	 * @return Retorna centroCostoSolicitante.
	 */
	public int getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}
	/**
	 * @param Asigna centroCostoSolicitante.
	 */
	public void setCentroCostoSolicitante(int centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}
    /**
     * @return Returns the restriccion.
     */
    public String getRestriccion() {
        return restriccion;
    }
    /**
     * @param restriccion The restriccion to set.
     */
    public void setRestriccion(String restriccion) {
        this.restriccion = restriccion;
    }
	/**
	 * @return Retorna the interconsultasProcedimientosSolicitud.
	 */
	public Collection getInterconsultasProcedimientosSolicitud()
	{
		return interconsultasProcedimientosSolicitud;
	}
	/**
	 * @param interconsultasProcedimientosSolicitud The interconsultasProcedimientosSolicitud to set.
	 */
	public void setInterconsultasProcedimientosSolicitud(
			Collection interconsultasProcedimientosSolicitud)
	{
		this.interconsultasProcedimientosSolicitud = interconsultasProcedimientosSolicitud;
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	public String getCentroCostosSolicitanteFiltro() {
		return centroCostosSolicitanteFiltro;
	}
	public void setCentroCostosSolicitanteFiltro(
			String centroCostosSolicitanteFiltro) {
		this.centroCostosSolicitanteFiltro = centroCostosSolicitanteFiltro;
	}
	public String getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}
	/**
	 * @return the estadoHCFiltro
	 */
	public String getEstadoHCFiltro() {
		return estadoHCFiltro;
	}
	/**
	 * @param estadoHCFiltro the estadoHCFiltro to set
	 */
	public void setEstadoHCFiltro(String estadoHCFiltro) {
		this.estadoHCFiltro = estadoHCFiltro;
	}
	/**
	 * @return the tipoOrdenFiltro
	 */
	public String getTipoOrdenFiltro() {
		return tipoOrdenFiltro;
	}
	/**
	 * @param tipoOrdenFiltro the tipoOrdenFiltro to set
	 */
	public void setTipoOrdenFiltro(String tipoOrdenFiltro) {
		this.tipoOrdenFiltro = tipoOrdenFiltro;
	}
	public void setFechaFinalFiltro(String fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}
	public String getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}
	public void setFechaInicialFiltro(String fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}
	public String getAreaFiltro() {
		return areaFiltro;
	}
	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}
	public String getCamaFiltro() {
		return camaFiltro;
	}
	public void setCamaFiltro(String camaFiltro) {
		this.camaFiltro = camaFiltro;
	}
	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}
	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}
	public String getPisoFiltro() {
		return pisoFiltro;
	}
	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	public boolean isRequierePortatilFiltro() {
		return requierePortatilFiltro;
	}
	public void setRequierePortatilFiltro(boolean requierePortatilFiltro) {
		this.requierePortatilFiltro = requierePortatilFiltro;
	}
	/**
	 * @return the cadenaFiltroBusqueda
	 */
	public String getCadenaFiltroBusqueda() {
		return cadenaFiltroBusqueda;
	}
	/**
	 * @param cadenaFiltroBusqueda the cadenaFiltroBusqueda to set
	 */
	public void setCadenaFiltroBusqueda(String cadenaFiltroBusqueda) {
		this.cadenaFiltroBusqueda = cadenaFiltroBusqueda;
	}
	/**
	 * 
	 * @return
	 */
	public HashMap getEstadoAuto() {
		return estadoAuto;
	}
	/**
	 * 
	 * @param estadoAuto
	 */
	public void setEstadoAuto(HashMap estadoAuto) {
		this.estadoAuto = estadoAuto;
	}
	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}
	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}
	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}
	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}
	
}