/*
 * Mayo 6, 2008
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.Utilidades;

import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.historiaClinica.DtoTriage;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.princetonsa.dto.historiaClinica.DtoValoracion;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Valoraciones
 */
public class ValoracionesForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ValoracionesForm.class);
	/**
	 * Variable para manejar la dirección del workflow
	 */
	private String estado;
	
	private DtoValoracionUrgencias valoracionUrgencias = new DtoValoracionUrgencias();
	
	private DtoValoracionHospitalizacion valoracionHospitalizacion = new DtoValoracionHospitalizacion();
	
	private DtoPlantilla plantilla;
	
	private DtoPlantillasIngresos plantillasIngresos;
	
	private DtoTriage dtoTriage;
	
	/**
	 * Se toma el codigo de la funcionalidad
	 */
	private int codigoFuncionalidad;
	
	/**
	 * Vía de ingreso de la valoracion
	 */
	private InfoDatosInt viaIngreso = new InfoDatosInt();
	
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
	private boolean seccionExamenFisico;
	private boolean seccionEstadoSalida;
	private boolean seccionTipoMonitoreo;
	
	//*******************ARREGLOS*********************************************
	private ArrayList<HashMap<String, Object>> estadosConciencia;
	private ArrayList<HashMap<String, Object>> causasExternas;
	private ArrayList<HashMap<String, Object>> finalidades;
	private ArrayList<HashMap<String, Object>> conductasValoracion;
	private ArrayList<HashMap<String, Object>> tiposDiagnostico;
	private ArrayList<HashMap<String, Object>> tiposMonitoreo;
	//Arreglos para componente
	private ArrayList<HashMap<String, Object>> rangosEdadMenarquia;
	private ArrayList<HashMap<String, Object>> rangosEdadMenopausia;
	private ArrayList<HashMap<String, Object>> conceptosMenstruacion;
	//**************************************************************************
	
	/**
	 * Campo para saber si se debe abrir la referencia
	 */
	private boolean deboAbrirReferencia;
	/**
	 * Campo que contiene la instruccion Javascript para abrir la referencia
	 */
	private String rutaReferencia;
	
	/**
	 * Campo para saber si la solicitud está pendiente de cargo
	 */
	private boolean solicitudPendiente;
	
	/**
	 * Arreglo donde se manejan las advertencias
	 */
	private ArrayList<ElementoApResource> advertencias;
	
	/**
	 * Variable que indica si se puede modificar la conducta de la valoración
	 */
	private boolean puedoModificarConductaValoracion;
	
	
	//******************ATRIBUTOS USADOS PARA LE IMPRESION DE LA VALORACION************************************
	/**
	 * Método que carga la información de la institucion
	 */
	private ParametrizacionInstitucion institucion = new ParametrizacionInstitucion();
	/**
	 * Centro de Atencion
	 */
	private InfoDatosInt centroAtencion = new InfoDatosInt();
	//********************************************************************************************************
	//***************ATRIBUTOS USADOS PARA EL HISTÓRICO DEL ASOCIO**********************************************
	/**
	 * Variable que indica si es asocio
	 */
	private boolean asocio;
	/**
	 * Mapa que almacena el listado del histórico del asocio
	 */
	private HashMap<String, Object> historico = new HashMap<String, Object>();
	/**
	 * Variable que almacena el número de registros por pagina
	 */
	private int maxPageItems;
	private String linkSiguiente;
	private String indice;
	private String ultimoIndice;
	private int posHistorico;
	private DtoValoracionUrgencias valoracionUrgenciasHistorico;
	private DtoValoracionHospitalizacion valoracionHospitalizacionHistorico;
	private DtoPlantilla plantillaHistorico;
	
	//**********************************************************************************************************
	//*************ATRIBUTOS ASOCIADOS A LA VALIDACIÓN DE REINGRESO**********************************************
	private String fechaEgresoAnterior;
	private String horaEgresoAnterior;
	private Diagnostico diagnosticoEgresoAnterior = new Diagnostico();
	private boolean validacionReingreso; //indica si la validación del reingreso fue exitosa
	private boolean aceptarReingreso; // indica si se debe aceptar el reingreso
	//***********************************************************************************************************
	//************ATRIBUTOS ASOCIADOS AL FLUJO DE HISTORIA DE ATENCIONES*******************************************
	private boolean vieneDeHistoriaAtenciones;
	
	/**
	 * Atributo que almacena el estado desde donde proviene el llamado al resumen (true: cuando existe valoracion inicial y de cuidados especiales | false: cuando existe valoracion inicial o de cuidados especiales )
	 */
	private boolean vieneResumenVariasValoraciones;
	//**************************************************************************************************************
	
	//************ATRIBUTOS ASOCIADOS AL FLUJO DE EPICRISIS*******************************************
	private boolean vieneDeEpicrisis;
	//**************************************************************************************************************
	
	//Atributos usados para mostrar el resumen de la impresion
	private UsuarioBasico usuarioResumen ;
	private String fechaResumen;
	private String horaResumen;
	
	//Atributo para saber si se muestra o no el encabezado
	private boolean ocultarEncabezado; 
	
	//Atributo para saber si se muestra o no informacion del paciente en la impresion 
	private boolean ocultarInfoPaciente;
	
	private String ocultarImpIncapacidad;
	
	private Boolean checkEnlaceOrdenesAMbulatorias=false;
	
	
	
	
	
	
	
	//Atributos para el manejo de las curvas de crecimiento
	private List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla;
	private HistoricoImagenPlantillaDto curvaSeleccionada;
	private Boolean mostrarDetalles;
	private Integer indiceCurvaSeleccionada;
	private String edadCalculada;
	private String funcionalidad;
	private Boolean imprimirDetalleHC=false;
	
	
	
	/** 
	 Alberto Ovalle
	 * mt 5749
	 */
	/**
	 * atributo para lista de valoracion
	 */
	 private List <DtoValoracion> vistaobservaciones;
	
	/**
	 * atributo para set de dtovaloracion
	 */
	 private DtoValoracion dtoValoracion = new DtoValoracion();
	 
	 /**
	 * atributo para planDiagnostico
	 */
	 private String planDiagnostico;
	 /**
	 * atributo para los comentarios generales
	 */
	 private String comentariosGenerales;
	 
	 /**
	 * atributo para expliqueDosDeberesDerechos
	 */
	 private String expliqueDosDeberesDerechos;
	 
	 	
	/*************************************/
	public Boolean getImprimirDetalleHC() {
		return imprimirDetalleHC;
	}
	public void setImprimirDetalleHC(Boolean imprimirDetalleHC) {
		this.imprimirDetalleHC = imprimirDetalleHC;
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
	public String getFuncionalidad() {
		return funcionalidad;
	}
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}
	//fin curvas
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Reset de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.valoracionUrgencias = new DtoValoracionUrgencias();
		this.valoracionHospitalizacion = new DtoValoracionHospitalizacion();
		this.plantilla = new DtoPlantilla();
		this.codigoFuncionalidad = ConstantesBD.codigoNuncaValido;
		this.viaIngreso = new InfoDatosInt();
		
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		this.diagnosticosSeleccionados = "";
		
		
		this.seccionRevisionSistemas = false;
		this.seccionExamenFisico = false;
		this.seccionEstadoSalida = false;
		this.seccionTipoMonitoreo = false;
		
		this.dtoTriage=new DtoTriage();
		
		//ARREGLOS
		this.estadosConciencia = new ArrayList<HashMap<String,Object>>();
		this.causasExternas = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.conductasValoracion = new ArrayList<HashMap<String,Object>>();
		this.tiposDiagnostico = new ArrayList<HashMap<String,Object>>();
		this.tiposMonitoreo = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenarquia = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenopausia = new ArrayList<HashMap<String,Object>>();
		this.conceptosMenstruacion = new ArrayList<HashMap<String,Object>>();
		
		this.deboAbrirReferencia = false;
		this.rutaReferencia = "";
		this.solicitudPendiente = false;
		this.puedoModificarConductaValoracion = false;
		
		//Atributos para la impresion
		this.institucion = new ParametrizacionInstitucion();
		this.centroAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		
		//Atributos para el histórico del asocio
		this.asocio = false;
		this.historico = new HashMap<String, Object>();
		this.maxPageItems = 10;
		this.linkSiguiente = "";
		this.indice = "";
		this.ultimoIndice = "";
		this.posHistorico = ConstantesBD.codigoNuncaValido;
		this.valoracionUrgenciasHistorico = new DtoValoracionUrgencias();
		this.valoracionHospitalizacionHistorico = new DtoValoracionHospitalizacion();
		this.plantillaHistorico = new DtoPlantilla();
		
		//Atributos asociados a la validación del reingreso
		this.fechaEgresoAnterior = "";
		this.horaEgresoAnterior = "";
		this.diagnosticoEgresoAnterior = new Diagnostico();
		this.validacionReingreso = false;
		this.aceptarReingreso = false;
		
		//Atributos asociados al flujo de historia de atenciones
		this.vieneDeHistoriaAtenciones = false;
		this.vieneResumenVariasValoraciones = false;
		
		//Atributos asociados al flujo de historia de atenciones
		this.vieneDeEpicrisis = false;
		
		//Atributos usados para mostrar el resumen de la impresion
		this.usuarioResumen = new UsuarioBasico();
		this.fechaResumen = "";
		this.horaResumen = "";
		
		this.ocultarImpIncapacidad = ConstantesBD.acronimoNo;
		
		/*Alberto Ovalle se adiciona variables mt5749*/
		this.planDiagnostico="";
		this.comentariosGenerales="";
		this.expliqueDosDeberesDerechos="";
		
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
		logger.info("Especialidad desdwe el action form: "+this.valoracionUrgencias.getEspecialidadResponde());
		ActionErrors errores= new ActionErrors();
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
	 * @return the viaIngreso
	 */
	public int getCodigoViaIngreso() {
		return viaIngreso.getCodigo();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setCodigoViaIngreso(int viaIngreso) {
		this.viaIngreso.setCodigo(viaIngreso);
	}
	
	/**
	 * @return the viaIngreso
	 */
	public String getNombreViaIngreso() {
		return viaIngreso.getNombre();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setNombreViaIngreso(String viaIngreso) {
		this.viaIngreso.setNombre(viaIngreso);
	}

	/**
	 * @return the codigoFuncionalidad
	 */
	public int getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}

	/**
	 * @param codigoFuncionalidad the codigoFuncionalidad to set
	 */
	public void setCodigoFuncionalidad(int codigoFuncionalidad) {
		this.codigoFuncionalidad = codigoFuncionalidad;
	}

	/**
	 * @return the valoracionHospitalizacion
	 */
	public DtoValoracionHospitalizacion getValoracionHospitalizacion() {
		return valoracionHospitalizacion;
	}

	/**
	 * @param valoracionHospitalizacion the valoracionHospitalizacion to set
	 */
	public void setValoracionHospitalizacion(
			DtoValoracionHospitalizacion valoracionHospitalizacion) {
		this.valoracionHospitalizacion = valoracionHospitalizacion;
	}

	/**
	 * @return the valoracionUrgencias
	 */
	public DtoValoracionUrgencias getValoracionUrgencias() {
		return valoracionUrgencias;
	}

	/**
	 * @param valoracionUrgencias the valoracionUrgencias to set
	 */
	public void setValoracionUrgencias(DtoValoracionUrgencias valoracionUrgencias) {
		this.valoracionUrgencias = valoracionUrgencias;
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
	 * @return the estadosConciencia
	 */
	public ArrayList<HashMap<String, Object>> getEstadosConciencia() {
		return estadosConciencia;
	}

	/**
	 * @param estadosConciencia the estadosConciencia to set
	 */
	public void setEstadosConciencia(
			ArrayList<HashMap<String, Object>> estadosConciencia) {
		this.estadosConciencia = estadosConciencia;
	}

	/**
	 * @return the seccionExamenFisico
	 */
	public boolean isSeccionExamenFisico() {
		return seccionExamenFisico;
	}

	/**
	 * @param seccionExamenFisico the seccionExamenFisico to set
	 */
	public void setSeccionExamenFisico(boolean seccionExamenFisico) {
		this.seccionExamenFisico = seccionExamenFisico;
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
	 * @return the conductasValoracion
	 */
	public ArrayList<HashMap<String, Object>> getConductasValoracion() {
		return conductasValoracion;
	}

	/**
	 * @param conductasValoracion the conductasValoracion to set
	 */
	public void setConductasValoracion(
			ArrayList<HashMap<String, Object>> conductasValoracion) {
		this.conductasValoracion = conductasValoracion;
	}

	/**
	 * @return the seccionEstadoSalida
	 */
	public boolean isSeccionEstadoSalida() {
		return seccionEstadoSalida;
	}

	/**
	 * @param seccionEstadoSalida the seccionEstadoSalida to set
	 */
	public void setSeccionEstadoSalida(boolean seccionEstadoSalida) {
		this.seccionEstadoSalida = seccionEstadoSalida;
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
	 * @return the tiposMonitoreo
	 */
	public ArrayList<HashMap<String, Object>> getTiposMonitoreo() {
		return tiposMonitoreo;
	}

	/**
	 * @param tiposMonitoreo the tiposMonitoreo to set
	 */
	public void setTiposMonitoreo(ArrayList<HashMap<String, Object>> tiposMonitoreo) {
		this.tiposMonitoreo = tiposMonitoreo;
	}

	/**
	 * @return the seccionTipoMonitoreo
	 */
	public boolean isSeccionTipoMonitoreo() {
		return seccionTipoMonitoreo;
	}

	/**
	 * @param seccionTipoMonitoreo the seccionTipoMonitoreo to set
	 */
	public void setSeccionTipoMonitoreo(boolean seccionTipoMonitoreo) {
		this.seccionTipoMonitoreo = seccionTipoMonitoreo;
	}

	/**
	 * @return the deboAbrirReferencia
	 */
	public boolean isDeboAbrirReferencia() {
		return deboAbrirReferencia;
	}

	/**
	 * @param deboAbrirReferencia the deboAbrirReferencia to set
	 */
	public void setDeboAbrirReferencia(boolean deboAbrirReferencia) {
		this.deboAbrirReferencia = deboAbrirReferencia;
	}

	/**
	 * @return the solicitudPendiente
	 */
	public boolean isSolicitudPendiente() {
		return solicitudPendiente;
	}

	/**
	 * @param solicitudPendiente the solicitudPendiente to set
	 */
	public void setSolicitudPendiente(boolean solicitudPendiente) {
		this.solicitudPendiente = solicitudPendiente;
	}

	/**
	 * @return the rutaReferencia
	 */
	public String getRutaReferencia() {
		return rutaReferencia;
	}

	/**
	 * @param rutaReferencia the rutaReferencia to set
	 */
	public void setRutaReferencia(String rutaReferencia) {
		this.rutaReferencia = rutaReferencia;
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
	 * Método que retorna el número de advertencias
	 * @return
	 */
	public int getNumAdvertencias()
	{
		return this.advertencias.size();
	}

	/**
	 * @return the puedoModificarConductaValoracion
	 */
	public boolean isPuedoModificarConductaValoracion() {
		return puedoModificarConductaValoracion;
	}

	/**
	 * @param puedoModificarConductaValoracion the puedoModificarConductaValoracion to set
	 */
	public void setPuedoModificarConductaValoracion(
			boolean puedoModificarConductaValoracion) {
		this.puedoModificarConductaValoracion = puedoModificarConductaValoracion;
	}
	
	/**
	 * Método para setear el numero de la solicitud de urgencias
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitudUrgencias(String numeroSolicitud)
	{
		this.valoracionUrgencias.setNumeroSolicitud(numeroSolicitud);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public String getNumeroSolicitudUrgencias()
	{
		return this.valoracionUrgencias.getNumeroSolicitud();
	}
	
	/**
	 * Método para setear el numero de la solicitud de hospitalizacion
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitudHospitalizacion(String numeroSolicitud)
	{
		this.valoracionHospitalizacion.setNumeroSolicitud(numeroSolicitud);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public String getNumeroSolicitudHospitalizacion()
	{
		return this.valoracionHospitalizacion.getNumeroSolicitud();
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
	 * @return the centroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return centroAtencion.getCodigo();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCodigoCentroAtencion(int centroAtencion) {
		this.centroAtencion.setCodigo(centroAtencion);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public String getNombreCentroAtencion() {
		return centroAtencion.getNombre();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setNombreCentroAtencion(String centroAtencion) {
		this.centroAtencion.setNombre(centroAtencion);
	}

	/**
	 * @return the asocio
	 */
	public boolean isAsocio() {
		return asocio;
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setAsocio(boolean asocio) {
		this.asocio = asocio;
	}

	/**
	 * @return the historico
	 */
	public HashMap<String, Object> getHistorico() {
		return historico;
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(HashMap<String, Object> historico) {
		this.historico = historico;
	}
	
	/**
	 * @return the historico
	 */
	public Object getHistorico(String key) {
		return historico.get(key);
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(String key, Object obj) {
		this.historico.put(key,obj);
	}
	
	/**
	 * Método que retorna el núemro de filas del listado de historico
	 * @return
	 */
	public int getNumHistorico()
	{
		return Utilidades.convertirAEntero(this.getHistorico("numRegistros")+"",true);
	}
	
	/**
	 * Método para asignar el tamaño del mapa historico
	 * @param numRegistros
	 */
	public void setNumHistorico(int numRegistros)
	{
		this.setHistorico("numRegistros", numRegistros);
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
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
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the posHistorico
	 */
	public int getPosHistorico() {
		return posHistorico;
	}

	/**
	 * @param posHistorico the posHistorico to set
	 */
	public void setPosHistorico(int posHistorico) {
		this.posHistorico = posHistorico;
	}

	

	/**
	 * @return the plantillaHistorico
	 */
	public DtoPlantilla getPlantillaHistorico() {
		return plantillaHistorico;
	}

	/**
	 * @param plantillaHistorico the plantillaHistorico to set
	 */
	public void setPlantillaHistorico(DtoPlantilla plantillaHistorico) {
		this.plantillaHistorico = plantillaHistorico;
	}

	/**
	 * @return the valoracionHospitalizacionHistorico
	 */
	public DtoValoracionHospitalizacion getValoracionHospitalizacionHistorico() {
		return valoracionHospitalizacionHistorico;
	}

	/**
	 * @param valoracionHospitalizacionHistorico the valoracionHospitalizacionHistorico to set
	 */
	public void setValoracionHospitalizacionHistorico(
			DtoValoracionHospitalizacion valoracionHospitalizacionHistorico) {
		this.valoracionHospitalizacionHistorico = valoracionHospitalizacionHistorico;
	}

	/**
	 * @return the valoracionUrgenciasHistorico
	 */
	public DtoValoracionUrgencias getValoracionUrgenciasHistorico() {
		return valoracionUrgenciasHistorico;
	}

	/**
	 * @param valoracionUrgenciasHistorico the valoracionUrgenciasHistorico to set
	 */
	public void setValoracionUrgenciasHistorico(
			DtoValoracionUrgencias valoracionUrgenciasHistorico) {
		this.valoracionUrgenciasHistorico = valoracionUrgenciasHistorico;
	}

	/**
	 * @return the aceptarReingreso
	 */
	public boolean isAceptarReingreso() {
		return aceptarReingreso;
	}

	/**
	 * @param aceptarReingreso the aceptarReingreso to set
	 */
	public void setAceptarReingreso(boolean aceptarReingreso) {
		this.aceptarReingreso = aceptarReingreso;
	}

	/**
	 * @return the diagnosticoEgresoAnterior
	 */
	public Diagnostico getDiagnosticoEgresoAnterior() {
		return diagnosticoEgresoAnterior;
	}

	/**
	 * @param diagnosticoEgresoAnterior the diagnosticoEgresoAnterior to set
	 */
	public void setDiagnosticoEgresoAnterior(Diagnostico diagnosticoEgresoAnterior) {
		this.diagnosticoEgresoAnterior = diagnosticoEgresoAnterior;
	}

	/**
	 * @return the fechaEgresoAnterior
	 */
	public String getFechaEgresoAnterior() {
		return fechaEgresoAnterior;
	}

	/**
	 * @param fechaEgresoAnterior the fechaEgresoAnterior to set
	 */
	public void setFechaEgresoAnterior(String fechaEgresoAnterior) {
		this.fechaEgresoAnterior = fechaEgresoAnterior;
	}

	/**
	 * @return the horaEgresoAnterior
	 */
	public String getHoraEgresoAnterior() {
		return horaEgresoAnterior;
	}

	/**
	 * @param horaEgresoAnterior the horaEgresoAnterior to set
	 */
	public void setHoraEgresoAnterior(String horaEgresoAnterior) {
		this.horaEgresoAnterior = horaEgresoAnterior;
	}

	/**
	 * @return the validacionReingreso
	 */
	public boolean isValidacionReingreso() {
		return validacionReingreso;
	}

	/**
	 * @param validacionReingreso the validacionReingreso to set
	 */
	public void setValidacionReingreso(boolean validacionReingreso) {
		this.validacionReingreso = validacionReingreso;
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
	 * @return the vieneDeEpicrisis
	 */
	public boolean isVieneDeEpicrisis() {
		return vieneDeEpicrisis;
	}

	/**
	 * @param vieneDeEpicrisis the vieneDeEpicrisis to set
	 */
	public void setVieneDeEpicrisis(boolean vieneDeEpicrisis) {
		this.vieneDeEpicrisis = vieneDeEpicrisis;
	}

	/**
	 * @return the vieneDeEpicrisis
	 */
	public boolean getVieneDeEpicrisis() {
		return vieneDeEpicrisis;
	}

	/**
	 * @return the fechaResumen
	 */
	public String getFechaResumen() {
		return fechaResumen;
	}

	/**
	 * @param fechaResumen the fechaResumen to set
	 */
	public void setFechaResumen(String fechaResumen) {
		this.fechaResumen = fechaResumen;
	}

	/**
	 * @return the horaResumen
	 */
	public String getHoraResumen() {
		return horaResumen;
	}

	/**
	 * @param horaResumen the horaResumen to set
	 */
	public void setHoraResumen(String horaResumen) {
		this.horaResumen = horaResumen;
	}

	/**
	 * @return the usuarioResumen
	 */
	public UsuarioBasico getUsuarioResumen() {
		return usuarioResumen;
	}

	/**
	 * @param usuarioResumen the usuarioResumen to set
	 */
	public void setUsuarioResumen(UsuarioBasico usuarioResumen) {
		this.usuarioResumen = usuarioResumen;
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

	
	public void resetSalidaPaciente()
	{
		valoracionUrgencias.clean();
	}

	/**
	 * @return the ocultarImpIncapacidad
	 */
	public String getOcultarImpIncapacidad() {
		return ocultarImpIncapacidad;
	}

	/**
	 * @param ocultarImpIncapacidad the ocultarImpIncapacidad to set
	 */
	public void setOcultarImpIncapacidad(String ocultarImpIncapacidad) {
		this.ocultarImpIncapacidad = ocultarImpIncapacidad;
	}

	public DtoTriage getDtoTriage() {
		return dtoTriage;
	}

	public void setDtoTriage(DtoTriage dtoTriage) {
		this.dtoTriage = dtoTriage;
	}

	/**
	 * @return the checkEnlaceOrdenesAMbulatorias
	 */
	public Boolean getCheckEnlaceOrdenesAMbulatorias() {
		return checkEnlaceOrdenesAMbulatorias;
	}

	/**
	 * @param checkEnlaceOrdenesAMbulatorias the checkEnlaceOrdenesAMbulatorias to set
	 */
	public void setCheckEnlaceOrdenesAMbulatorias(
			Boolean checkEnlaceOrdenesAMbulatorias) {
		this.checkEnlaceOrdenesAMbulatorias = checkEnlaceOrdenesAMbulatorias;
	}
	
	/**
	 * @return the vieneResumenVariasValoraciones
	 */
	public boolean isVieneResumenVariasValoraciones() {
		return vieneResumenVariasValoraciones;
	}
	
	/**
	 * @param vieneResumenVariasValoraciones the vieneResumenVariasValoraciones to set
	 */
	public void setVieneResumenVariasValoraciones(
			boolean vieneResumenVariasValoraciones) {
		this.vieneResumenVariasValoraciones = vieneResumenVariasValoraciones;
	}
	
	/**
	 * Alberto Ovalle
	 * propiedades para la mt5749
	 */
	/**
	 * 
	 * @return vistaobservaciones
	 */
	public List<DtoValoracion> getVistaobservaciones() {
		return vistaobservaciones;
	}
	
	/**
	 * 
	 * @param vistaobservaciones
	 */
	public void setVistaobservaciones(
			List<DtoValoracion> vistaobservaciones) {
		this.vistaobservaciones = vistaobservaciones;
	}
	/**
	 * 
	 * @return dtoValoracion
	 */
	public DtoValoracion getDtoValoracion() {
		return dtoValoracion;
	}
	/**
	 * 
	 * @param dtoValoracion
	 */
	public void setDtoValoracion(DtoValoracion dtoValoracion) {
		this.dtoValoracion = dtoValoracion;
	}
	/**
	 * 
	 * @return planDiagnostico
	 */
	public String getPlanDiagnostico() {
		return planDiagnostico;
	}
	/**
	 * 
	 * @param planDiagnostico
	 */
	public void setPlanDiagnostico(String planDiagnostico) {
		this.planDiagnostico = planDiagnostico;
	}
	/**
	 * 
	 * @return comentariosGenerales
	 */
	public String getComentariosGenerales() {
		return comentariosGenerales;
	}
	/**
	 * 
	 * @param comentariosGenerales
	 */
	public void setComentariosGenerales(String comentariosGenerales) {
		this.comentariosGenerales = comentariosGenerales;
	}

	/**
	 * 
	 * @return expliqueDosDeberesDerechos
	 */ 
	public String getExpliqueDosDeberesDerechos() {
		return expliqueDosDeberesDerechos;
	}
	/**
	 * 
	 * @param expliqueDosDeberesDerechos
	 */
	public void setExpliqueDosDeberesDerechos(String expliqueDosDeberesDerechos) {
		this.expliqueDosDeberesDerechos = expliqueDosDeberesDerechos;
	}

}
