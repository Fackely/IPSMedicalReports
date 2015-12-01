/*
 * Junio 3, 2008
 */
package com.princetonsa.actionform.ordenesmedicas.interconsultas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Utilidades;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;


/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Respuesta de Interconsulta
 */
public class RespuestaInterconsultaForm extends ValidatorForm 
{
	/**
	 * Manejo del flujo del controlador
	 */
	private String estado;
	
	
	
	
	/**
	 * Campo para saber si debo ocultar el encabezado
	 */
	private boolean ocultarEncabezado;
	
	/**
	 * Campo para saber si el resumen de la interconsulta debe ser compacto
	 */
	private boolean resumenCompacto;
	
	/**
	 * DTO de la valoración de consulta
	 */
	private DtoValoracion valoracion = new DtoValoracion();
	
	/**
	 * DTO que maneja la plantilla 
	 */
	private DtoPlantilla plantilla;
	
	/**
	 * Arreglo donde se manejan las advertencias
	 */
	private ArrayList<ElementoApResource> advertencias;
	
	
	/**
	 * Mapa para manejar los diagnosticos relacionados
	 */
	private HashMap<String, Object> diagnosticosRelacionados;
	
	/**
	 * Variable que obtiene el listado de los diagnosticos seleccionados
	 */
	private String diagnosticosSeleccionados;
	

	
	/**
	 * Atributos para manejar la apertura/cierre de secciones desplegables
	 */
	private boolean seccionRevisionSistemas;
	
	//*******************ARREGLOS*********************************************
	private ArrayList<HashMap<String, Object>> causasExternas;
	private ArrayList<HashMap<String, Object>> finalidades;
	private ArrayList<HashMap<String, Object>> tiposDiagnostico;
	private ArrayList<HashMap<String, Object>> tiposRecargo;
	//Arreglos para componente
	private ArrayList<HashMap<String, Object>> rangosEdadMenarquia;
	private ArrayList<HashMap<String, Object>> rangosEdadMenopausia;
	private ArrayList<HashMap<String, Object>> conceptosMenstruacion;
	//**************************************************************************
	
	//Atributos para la impresión HTML***********************************************
	private ParametrizacionInstitucion institucion;
	private String nombreCentroAtencion;
	
	//*******Atributos propios para el manejo de la interconsulta*******************
	private InfoDatosString estadoHistoriaClinica;
	private InfoDatosInt especialidad = new InfoDatosInt(); //especialidad del servicio
	private int codigoManejo;	
	private String aceptaCambioTratante; //para almacenar la respuesta de acepta cambio tratante
	private boolean deboMostrarAceptaCambioTratante; //variable que indica si se debe mostrar el cambio de tratante
	//*******************************************************************************
	//******************Atributos para el flujo de historia de atenciones**************
	private boolean vieneDeHistoriaAtenciones;
	//**********************************************************************************
	
	//Atributo para saber si se muestra o no informacion del paciente en la impresion 
	private boolean ocultarInfoPaciente;
	
	//**********Atributos Curvas de Crecimiento*******************
	
	private List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla;
	private HistoricoImagenPlantillaDto curvaSeleccionada;
	private Boolean mostrarDetalles;
	private Integer indiceCurvaSeleccionada;
	private String edadCalculada;
	
	/**
	 * Reset pasando parametros iniciales
	 */
	public void reset(String numeroSolicitud,boolean ocultarEncabezado,boolean resumenCompacto, boolean vieneDeHistoriaAtenciones)
	{
		this.reset();
		this.ocultarEncabezado = ocultarEncabezado;
		this.resumenCompacto = resumenCompacto;
		//Si el resumen es compacto también se oculta el encabezado
		if(this.resumenCompacto)
			this.ocultarEncabezado = true;
		this.setNumeroSolicitud(numeroSolicitud);
		this.vieneDeHistoriaAtenciones = vieneDeHistoriaAtenciones;
	}
	
	/**
	 * Reset completo
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.ocultarEncabezado = false;
		this.resumenCompacto = false;
		this.valoracion = new DtoValoracion();
		this.plantilla = new DtoPlantilla();
		this.advertencias = new ArrayList<ElementoApResource>();
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		this.diagnosticosSeleccionados = "";
		
		this.seccionRevisionSistemas = false;
		
		//Arreglos de la funcionalidad
		this.causasExternas = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.tiposDiagnostico = new ArrayList<HashMap<String,Object>>();
		this.tiposRecargo = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenarquia = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenopausia = new ArrayList<HashMap<String,Object>>();
		this.conceptosMenstruacion = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para la impresión HTML***********************************************
		this.institucion = new ParametrizacionInstitucion();
		this.nombreCentroAtencion = "";
		
		//*******Atributos propios para el manejo de la interconsulta*******************
		this.codigoManejo = ConstantesBD.codigoNuncaValido;
		this.especialidad = new InfoDatosInt();
		this.estadoHistoriaClinica = new InfoDatosString();
		this.setNombreEspecialidad("Interconsulta");		
		this.aceptaCambioTratante = ConstantesBD.acronimoSi;
		this.deboMostrarAceptaCambioTratante = false;
		
		//Atributos para el flujo de la historia de atenciones
		this.vieneDeHistoriaAtenciones = false;
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
		ActionErrors errores= new ActionErrors();
		return errores;
	}

	/**
	 * @return the advertencias
	 */
	public ArrayList<ElementoApResource> getAdvertencias() {
		return advertencias;
	}

	/**
	 * @param advertencias the advertencias to set
	 */
	public void setAdvertencias(ArrayList<ElementoApResource> advertencias) {
		this.advertencias = advertencias;
	}

	/**
	 * @return the causasExternas
	 */
	public ArrayList<HashMap<String, Object>> getCausasExternas() {
		return causasExternas;
	}

	/**
	 * @param causasExternas the causasExternas to set
	 */
	public void setCausasExternas(ArrayList<HashMap<String, Object>> causasExternas) {
		this.causasExternas = causasExternas;
	}

	/**
	 * @return the diagnosticosRelacionados
	 */
	public HashMap<String, Object> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}

	/**
	 * @param diagnosticosRelacionados the diagnosticosRelacionados to set
	 */
	public void setDiagnosticosRelacionados(
			HashMap<String, Object> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}

	/**
	 * @return the diagnosticosSeleccionados
	 */
	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}

	/**
	 * @param diagnosticosSeleccionados the diagnosticosSeleccionados to set
	 */
	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
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
	 * @return the finalidades
	 */
	public ArrayList<HashMap<String, Object>> getFinalidades() {
		return finalidades;
	}

	/**
	 * @param finalidades the finalidades to set
	 */
	public void setFinalidades(ArrayList<HashMap<String, Object>> finalidades) {
		this.finalidades = finalidades;
	}

	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}

	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}

	/**
	 * @return the plantilla
	 */
	public DtoPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * @param plantilla the plantilla to set
	 */
	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @return the seccionRevisionSistemas
	 */
	public boolean isSeccionRevisionSistemas() {
		return seccionRevisionSistemas;
	}

	/**
	 * @param seccionRevisionSistemas the seccionRevisionSistemas to set
	 */
	public void setSeccionRevisionSistemas(boolean seccionRevisionSistemas) {
		this.seccionRevisionSistemas = seccionRevisionSistemas;
	}

	/**
	 * @return the tiposDiagnostico
	 */
	public ArrayList<HashMap<String, Object>> getTiposDiagnostico() {
		return tiposDiagnostico;
	}

	/**
	 * @param tiposDiagnostico the tiposDiagnostico to set
	 */
	public void setTiposDiagnostico(
			ArrayList<HashMap<String, Object>> tiposDiagnostico) {
		this.tiposDiagnostico = tiposDiagnostico;
	}

	/**
	 * @return the valoracion
	 */
	public DtoValoracion getValoracion() {
		return valoracion;
	}

	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(DtoValoracion valoracion) {
		this.valoracion = valoracion;
	}
	
	/**
	 * @return the diagnosticosRelacionados
	 */
	public Object getDiagnosticosRelacionados(String key) {
		return diagnosticosRelacionados.get(key);
	}

	/**
	 * @param diagnosticosRelacionados the diagnosticosRelacionados to set
	 */
	public void setDiagnosticosRelacionados(String key,Object obj) {
		this.diagnosticosRelacionados.put(key,obj);
	}
	
	/**
	 * Método para asignar el número de dx relacionados
	 * @param numRegistros
	 */
	public void setNumDiagRelacionados(int numRegistros)
	{
		this.diagnosticosRelacionados.put("numRegistros",numRegistros);
	}
	
	/**
	 * Método para obtener el número de diagnósticos relacionados
	 * @return
	 */
	public int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}
	
	/**
	 * Método para obtener el numero de la solicitud
	 * @return
	 */
	public String getNumeroSolicitud()
	{
		return this.valoracion.getNumeroSolicitud();
	}
	
	/**
	 * Método para asignar el numero de la solicitud
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitud(String numeroSolicitud)
	{
		this.valoracion.setNumeroSolicitud(numeroSolicitud);
	}

	/**
	 * @return the codigoManejo
	 */
	public int getCodigoManejo() {
		return codigoManejo;
	}

	/**
	 * @param codigoManejo the codigoManejo to set
	 */
	public void setCodigoManejo(int codigoManejo) {
		this.codigoManejo = codigoManejo;
	}

	/**
	 * @return the especialidad
	 */
	public int getCodigoEspecialidad() {
		return especialidad.getCodigo();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setCodigoEspecialidad(int especialidad) {
		this.especialidad.setCodigo(especialidad);
	}
	
	/**
	 * @return the especialidad
	 */
	public String getNombreEspecialidad() {
		return especialidad.getNombre();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setNombreEspecialidad(String especialidad) {
		this.especialidad.setNombre(especialidad);
	}

	/**
	 * @return the estadoHistoriaClinica
	 */
	public InfoDatosString getEstadoHistoriaClinica() {
		return estadoHistoriaClinica;
	}

	/**
	 * @param estadoHistoriaClinica the estadoHistoriaClinica to set
	 */
	public void setEstadoHistoriaClinica(InfoDatosString estadoHistoriaClinica) {
		this.estadoHistoriaClinica = estadoHistoriaClinica;
	}
	
	/**
	 * @return the estadoHistoriaClinica
	 */
	public String getCodigoEstadoHistoriaClinica() {
		return estadoHistoriaClinica.getCodigo();
	}

	/**
	 * @param estadoHistoriaClinica the estadoHistoriaClinica to set
	 */
	public void setCodigoEstadoHistoriaClinica(String estadoHistoriaClinica) {
		this.estadoHistoriaClinica.setCodigo(estadoHistoriaClinica);
	}
	
	/**
	 * @return the estadoHistoriaClinica
	 */
	public String getNombreEstadoHistoriaClinica() {
		return estadoHistoriaClinica.getNombre();
	}

	/**
	 * @param estadoHistoriaClinica the estadoHistoriaClinica to set
	 */
	public void setNombreEstadoHistoriaClinica(String estadoHistoriaClinica) {
		this.estadoHistoriaClinica.setNombre(estadoHistoriaClinica);
	}

	/**
	 * @return the especialidad
	 */
	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}	

	/**
	 * @return the tiposRecargo
	 */
	public ArrayList<HashMap<String, Object>> getTiposRecargo() {
		return tiposRecargo;
	}

	/**
	 * @param tiposRecargo the tiposRecargo to set
	 */
	public void setTiposRecargo(ArrayList<HashMap<String, Object>> tiposRecargo) {
		this.tiposRecargo = tiposRecargo;
	}
	
	/**
	 * Método que retorna el número de advertencias
	 * @return
	 */
	public int getNumAdvertencias()
	{
		return this.advertencias.size();
	}

	/**
	 * @return the aceptaCambioTratante
	 */
	public String getAceptaCambioTratante() {
		return aceptaCambioTratante;
	}

	/**
	 * @param aceptaCambioTratante the aceptaCambioTratante to set
	 */
	public void setAceptaCambioTratante(String aceptaCambioTratante) {
		this.aceptaCambioTratante = aceptaCambioTratante;
	}

	/**
	 * @return the deboMostrarAceptaCambioTratante
	 */
	public boolean isDeboMostrarAceptaCambioTratante() {
		return deboMostrarAceptaCambioTratante;
	}

	/**
	 * @param deboMostrarAceptaCambioTratante the deboMostrarAceptaCambioTratante to set
	 */
	public void setDeboMostrarAceptaCambioTratante(
			boolean deboMostrarAceptaCambioTratante) {
		this.deboMostrarAceptaCambioTratante = deboMostrarAceptaCambioTratante;
	}

	/**
	 * @return the institucion
	 */
	public ParametrizacionInstitucion getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(ParametrizacionInstitucion institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the resumenCompacto
	 */
	public boolean isResumenCompacto() {
		return resumenCompacto;
	}

	/**
	 * @param resumenCompacto the resumenCompacto to set
	 */
	public void setResumenCompacto(boolean resumenCompacto) {
		this.resumenCompacto = resumenCompacto;
	}

	/**
	 * @return the rangosEdadMenarquia
	 */
	public ArrayList<HashMap<String, Object>> getRangosEdadMenarquia() {
		return rangosEdadMenarquia;
	}

	/**
	 * @param rangosEdadMenarquia the rangosEdadMenarquia to set
	 */
	public void setRangosEdadMenarquia(
			ArrayList<HashMap<String, Object>> rangosEdadMenarquia) {
		this.rangosEdadMenarquia = rangosEdadMenarquia;
	}

	/**
	 * @return the rangosEdadMenopausia
	 */
	public ArrayList<HashMap<String, Object>> getRangosEdadMenopausia() {
		return rangosEdadMenopausia;
	}

	/**
	 * @param rangosEdadMenopausia the rangosEdadMenopausia to set
	 */
	public void setRangosEdadMenopausia(
			ArrayList<HashMap<String, Object>> rangosEdadMenopausia) {
		this.rangosEdadMenopausia = rangosEdadMenopausia;
	}

	/**
	 * @return the conceptosMenstruacion
	 */
	public ArrayList<HashMap<String, Object>> getConceptosMenstruacion() {
		return conceptosMenstruacion;
	}

	/**
	 * @param conceptosMenstruacion the conceptosMenstruacion to set
	 */
	public void setConceptosMenstruacion(
			ArrayList<HashMap<String, Object>> conceptosMenstruacion) {
		this.conceptosMenstruacion = conceptosMenstruacion;
	}

	/**
	 * @return the vieneDeHistoriaAtenciones
	 */
	public boolean isVieneDeHistoriaAtenciones() {
		return vieneDeHistoriaAtenciones;
	}

	/**
	 * @param vieneDeHistoriaAtenciones the vieneDeHistoriaAtenciones to set
	 */
	public void setVieneDeHistoriaAtenciones(boolean vieneDeHistoriaAtenciones) {
		this.vieneDeHistoriaAtenciones = vieneDeHistoriaAtenciones;
	}

	/**
	 * @return the ocultarInfoPaciente
	 */
	public boolean isOcultarInfoPaciente() {
		return ocultarInfoPaciente;
	}

	/**
	 * @param ocultarInfoPaciente the ocultarInfoPaciente to set
	 */
	public void setOcultarInfoPaciente(boolean ocultarInfoPaciente) {
		this.ocultarInfoPaciente = ocultarInfoPaciente;
	}
	
	public List<HistoricoImagenPlantillaDto> getDtoHistoricoImagenPlantilla() {
		return dtoHistoricoImagenPlantilla;
	}
	public void setDtoHistoricoImagenPlantilla(List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla) {
		this.dtoHistoricoImagenPlantilla = dtoHistoricoImagenPlantilla;
	}
	public HistoricoImagenPlantillaDto getCurvaSeleccionada() {
		return curvaSeleccionada;
	}
	public void setCurvaSeleccionada(HistoricoImagenPlantillaDto curvaSeleccionada) {
		this.curvaSeleccionada = curvaSeleccionada;
	}
	public Boolean getMostrarDetalles() {
		return mostrarDetalles;
	}
	public void setMostrarDetalles(Boolean mostrarDetalles) {
		this.mostrarDetalles = mostrarDetalles;
	}
	public Integer getIndiceCurvaSeleccionada() {
		return indiceCurvaSeleccionada;
	}
	public void setIndiceCurvaSeleccionada(Integer indiceCurvaSeleccionada) {
		this.indiceCurvaSeleccionada = indiceCurvaSeleccionada;
	}
	public String getEdadCalculada() {
		return edadCalculada;
	}
	public void setEdadCalculada(String edadCalculada) {
		this.edadCalculada = edadCalculada;
	}
}
