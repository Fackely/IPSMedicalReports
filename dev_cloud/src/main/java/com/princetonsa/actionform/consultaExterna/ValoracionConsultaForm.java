/*
 * Mayo 31, 2008 
 */
package com.princetonsa.actionform.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.comun.DtoDatosGenericos;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

/**
 * @author Sebasti谩n G贸mez 
 *
 * Clase que almacena y carga la informaci贸n utilizada para la funcionalidad
 * Valoracion Consulta
 */
public class ValoracionConsultaForm extends ValidatorForm 
{
	
	/** *  */
	private static final long serialVersionUID = 1L;


	/**
	 * Manejo del flujo del controlador
	 */
	private String estado;
	
	
	/**
	 * Campo para saber si debo ocultar el encabezado
	 */
	private boolean ocultarEncabezado;
	
	/**
	 * DTO de la valoraci贸n de consulta
	 */
	private DtoValoracion valoracion;
	
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
	//Arreglos para componente
	private ArrayList<HashMap<String, Object>> rangosEdadMenarquia;
	private ArrayList<HashMap<String, Object>> rangosEdadMenopausia;
	private ArrayList<HashMap<String, Object>> conceptosMenstruacion;
	//**************************************************************************
	
	//Atributos flujo PYP***********************************************************
	private boolean vieneDePyp; //Campo que permite saber si una consulta viene de pyp
	private InfoDatosString finalidad; //Campo que almacena la finalidad que puede venir ya definida antes de guardar la valoraci贸n
	private boolean pyp; //Campo que indica si una solicitud es de pyp
	private boolean unificarPyp; //Campo que indica si el convenio de la cuenta tiene activado el campo unificar pyp
	
	//Datos Solicitud/Cita**************************************************************
	private String codigoCita="";
	private String numeroSolicitud;
	private InfoDatosString estadoHistoriaClinica;
	private InfoDatosInt especialidad = new InfoDatosInt(); //especialidad del servicio
	private String numeroAutorizacion1;
	private String fechaConsulta;
	private String horaConsulta;
	private boolean deboMostrarOtrosServiciosCita; //indica si se debe mostrar el bot贸n de otros servicios
	private boolean deboMostrarVolverlistadoCitas;//indica si se debe mostrar el boton Volver listado Citas
	
	
	//Atributos para la impresi贸n HTML***********************************************
	private ParametrizacionInstitucion institucion;
	private String nombreCentroAtencion;
	
	//Atributos para la modificacion
	private boolean puedoModificar;
	
	/**
	 * Variables para identificar el tipo de resumen que se est谩 realizando
	 */
	private boolean esResumenInsercion;
	private boolean esResumenModificacion;
	
	//Atributos para el flujo de historia de atenciones***********************************
	private boolean vieneDeHistoriaAtenciones;
	
	
	//Variable para determinar si viene de Consulta Externa
	private boolean vieneDeOrdenesAmbulatorias;	
	
	
	//Variable para determinar si viene del Flujo Entidades Subcontratadas
	private boolean vienedeEntidadesSubcontratadas;
	
	//
	private String codigoPacienteSolicitudEntSub;
	//----------------------------------------------------------------
	
	//*************************************************************************************************
	//Modificado por anexo 779
	
	@SuppressWarnings("rawtypes")
	private ArrayList mensajes = new ArrayList();

	
	/** * Lista de antecedentes a mostrar para imprimir */
	ArrayList<DtoDatosGenericos> listaAntecedentes;
	
	/**
	 * 
	 */
	private Boolean checkVisibilidadEnlaceOrdenesAmbulatorias=false;
	
	
	
	
	
	
	
	
	
	
	
	
	//Atributos para el manejo de las curvas de crecimiento
	private List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla;
	private HistoricoImagenPlantillaDto curvaSeleccionada;
	private Boolean mostrarDetalles;
	private Integer indiceCurvaSeleccionada;
	private String edadCalculada;
	private String funcionalidad;
	
	/*
	* Tipo Modificacion: Segun incidencia MT6713
	* Autor: Jes鷖 Dar韔 R韔s
	* usuario: jesrioro
	* Fecha: 18/03/2013
	* Descripcion: Atributos y metodos para el flujo de consultar citas          
	*/
	private boolean vieneDeConsultaExterna=false;
	
	/**
	 * @return the vieneDeConsultaExterna
	 */
	public boolean getVieneDeConsultaExterna() {
		return vieneDeConsultaExterna;
	}

	/**
	 * @param vieneDeConsultaExterna the vieneDeConsultaExterna to set
	 */
	public void setVieneDeConsultaExterna(boolean vieneDeConsultaExterna) {
		this.vieneDeConsultaExterna = vieneDeConsultaExterna;
	}
	
	//Fin MT6713
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
	
	
	
	
	
	
	
	
	//*****************************************************************************************************
	//----------------------------------------------------------------
	
	
	
	/**
	 * M閠odo que limpia los datos de la forma
	 * @param numeroSolicitud
	 * @param vieneDeHistoriaAtenciones 
	 */
	public void reset(String numeroSolicitud,String codigoCita, boolean ocultarEnzabezado,boolean vieneDePyp,InfoDatosString finalidad,InfoDatosInt especialidad, boolean vieneDeHistoriaAtenciones,boolean vieneDeEntidadesSubcontratadas, String codigoPacienteSolicitudEntSub )
	{
		this.reset();
		this.numeroSolicitud = numeroSolicitud;
		this.codigoCita = codigoCita;
		this.ocultarEncabezado = ocultarEnzabezado;
		this.vieneDePyp = vieneDePyp;
		this.finalidad = finalidad;
		this.especialidad = especialidad;
		this.vieneDeHistoriaAtenciones = vieneDeHistoriaAtenciones;
		this.vienedeEntidadesSubcontratadas=vieneDeEntidadesSubcontratadas;
		this.codigoPacienteSolicitudEntSub=codigoPacienteSolicitudEntSub;
	}
	
	@SuppressWarnings("rawtypes")
	public void resetInterfaz ()
	{
		this.mensajes = new ArrayList();
	}
	
	
	/**
	 * M茅todo que limpia los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.valoracion = new DtoValoracion();
		this.plantilla = new DtoPlantilla();
		this.numeroSolicitud = "";
		this.codigoCita = "";
		this.ocultarEncabezado = false;
		this.vieneDePyp = false;
		this.finalidad = new InfoDatosString();
		this.especialidad = new InfoDatosInt();
		this.advertencias = new ArrayList<ElementoApResource>();
		
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		this.diagnosticosSeleccionados = "";
		this.seccionRevisionSistemas = false;
		
		this.causasExternas = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.tiposDiagnostico = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenarquia = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenopausia = new ArrayList<HashMap<String,Object>>();
		this.conceptosMenstruacion = new ArrayList<HashMap<String,Object>>();
		
		this.pyp = false;
		this.unificarPyp = false;
		
		this.especialidad = new InfoDatosInt();
		this.setNombreEspecialidad("Consulta");
		this.fechaConsulta = "";
		this.horaConsulta = "";
		this.deboMostrarOtrosServiciosCita = false;
		this.deboMostrarVolverlistadoCitas =false;
		
		this.institucion = new ParametrizacionInstitucion();
		this.nombreCentroAtencion = "";
		
		this.puedoModificar = false;
		
		this.esResumenInsercion = false;
		this.esResumenModificacion = false;
		
		this.vieneDeHistoriaAtenciones = false;
		
		this.vieneDeOrdenesAmbulatorias = false;
		this.vienedeEntidadesSubcontratadas=false;
		this.codigoPacienteSolicitudEntSub="";
		
		this.listaAntecedentes = new ArrayList<DtoDatosGenericos>();
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
		
		if(this.estado.equals("insertar"))
		{
			//Se verifica PrimeraVez/Control
			if(this.getValoracion().getValoracionConsulta().getPrimeraVez()==null)
				errores.add("",new ActionMessage("errors.required","El campo Primera Vez/Control"));
			
			///Causa externa
			if(this.getValoracion().getCodigoCausaExterna()==0)
				errores.add("", new ActionMessage("errors.required","La causa externa"));
			
			//Finalidad de la consulta
			if(this.getValoracion().getValoracionConsulta().getCodigoFinalidadConsulta().equals(""))
				errores.add("", new ActionMessage("errors.required","La finalidad de la consulta"));
			
			//Diagn贸stico Principal
			if(this.getValoracion().getDiagnosticos().get(0).getAcronimo().equals(""))
				errores.add("", new ActionMessage("errors.required","El diagn贸stico principal"));
			
			//Tipo Diagn贸stico principal
			if(this.getValoracion().getValoracionConsulta().getCodigoTipoDiagnostico()==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.required","El tipo de diagn贸stico principal"));
			
			//FEcha proximo control
			if(!this.getValoracion().getValoracionConsulta().getFechaProximoControl().equals("")&&
				!UtilidadFecha.validarFecha(this.getValoracion().getValoracionConsulta().getFechaProximoControl()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","de la fecha de control"));
			
			//Numero de d铆as incapacidad
			if(!this.getValoracion().getValoracionConsulta().getNumeroDiasIncapacidad().equals("")&&
				Utilidades.convertirAEntero(this.getValoracion().getValoracionConsulta().getNumeroDiasIncapacidad())==ConstantesBD.codigoNuncaValido)
				errores.add("",new ActionMessage("errors.integer","El n煤mero de d铆as de incapacidad"));
			
			//SE realizan las validaciones de los campos de la plantilla
			errores = Plantillas.validacionCamposPlantilla(this.plantilla, errores);
			
			//************VALIDACIONES COMPONENTES***********************************
			//Se realizan las validaciones de los campos de la historia mentrual
			if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
				errores = this.valoracion.getHistoriaMenstrual().validate(errores);
			//Se realizan las validaciones de los campos de signos vitales
			if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
				errores = SignoVital.validate(errores, this.valoracion.getSignosVitales());
			//Se realizan las validaciones de los campos de oftalmolog铆a
			if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
				errores = this.valoracion.getOftalmologia().validate(errores);
			//************************************************************************
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
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
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
	 * @return the codigoCita
	 */
	public String getCodigoCita() {
		return codigoCita;
	}

	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
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
	 * @return the ocultarEnzabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}

	/**
	 * @param ocultarEnzabezado the ocultarEnzabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}

	/**
	 * @return the finalidad
	 */
	@SuppressWarnings("deprecation")
	public String getCodigoFinalidad() {
		return finalidad.getCodigo();
	}

	/**
	 * @param finalidad the finalidad to set
	 */
	public void setCodigoFinalidad(String finalidad) {
		this.finalidad.setCodigo(finalidad);
	}
	
	/**
	 * @return the finalidad
	 */
	public String getNombreFinalidad() {
		return finalidad.getNombre();
	}

	/**
	 * @param finalidad the finalidad to set
	 */
	public void setNombreFinalidad(String finalidad) {
		this.finalidad.setNombre(finalidad);
	}

	/**
	 * @return the vieneDePyp
	 */
	public boolean isVieneDePyp() {
		return vieneDePyp;
	}

	/**
	 * @param vieneDePyp the vieneDePyp to set
	 */
	public void setVieneDePyp(boolean vieneDePyp) {
		this.vieneDePyp = vieneDePyp;
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
	 * M茅todo que retorna el n煤mero de advertencias
	 * @return
	 */
	public int getNumAdvertencias()
	{
		return this.advertencias.size();
	}

	/**
	 * @return the finalidad
	 */
	public InfoDatosString getFinalidad() {
		return finalidad;
	}

	/**
	 * @param finalidad the finalidad to set
	 */
	public void setFinalidad(InfoDatosString finalidad) {
		this.finalidad = finalidad;
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
	 * M茅todo para asignar el n煤mero de dx relacionados
	 * @param numRegistros
	 */
	public void setNumDiagRelacionados(int numRegistros)
	{
		this.diagnosticosRelacionados.put("numRegistros",numRegistros);
	}
	
	/**
	 * M茅todo para obtener el n煤mero de diagn贸sticos relacionados
	 * @return
	 */
	public int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}

	/**
	 * @return the pyp
	 */
	public boolean isPyp() {
		return pyp;
	}

	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(boolean pyp) {
		this.pyp = pyp;
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
	 * @return the unificarPyp
	 */
	public boolean isUnificarPyp() {
		return unificarPyp;
	}

	/**
	 * @param unificarPyp the unificarPyp to set
	 */
	public void setUnificarPyp(boolean unificarPyp) {
		this.unificarPyp = unificarPyp;
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
	 * @return the fechaConsulta
	 */
	public String getFechaConsulta() {
		return fechaConsulta;
	}

	/**
	 * @param fechaConsulta the fechaConsulta to set
	 */
	public void setFechaConsulta(String fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	/**
	 * @return the horaConsulta
	 */
	public String getHoraConsulta() {
		return horaConsulta;
	}

	/**
	 * @param horaConsulta the horaConsulta to set
	 */
	public void setHoraConsulta(String horaConsulta) {
		this.horaConsulta = horaConsulta;
	}	

	/**
	 * @return the deboMostrarOtrosServiciosCita
	 */
	public boolean isDeboMostrarOtrosServiciosCita() {
		return deboMostrarOtrosServiciosCita;
	}

	/**
	 * @param deboMostrarOtrosServiciosCita the deboMostrarOtrosServiciosCita to set
	 */
	public void setDeboMostrarOtrosServiciosCita(
			boolean deboMostrarOtrosServiciosCita) {
		this.deboMostrarOtrosServiciosCita = deboMostrarOtrosServiciosCita;
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
	 * @return the puedoModificar
	 */
	public boolean isPuedoModificar() {
		return puedoModificar;
	}

	/**
	 * @param puedoModificar the puedoModificar to set
	 */
	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}

	/**
	 * @return the esResumenInsercion
	 */
	public boolean isEsResumenInsercion() {
		return esResumenInsercion;
	}

	/**
	 * @param esResumenInsercion the esResumenInsercion to set
	 */
	public void setEsResumenInsercion(boolean esResumenInsercion) {
		this.esResumenInsercion = esResumenInsercion;
	}

	/**
	 * @return the esResumenModificacion
	 */
	public boolean isEsResumenModificacion() {
		return esResumenModificacion;
	}

	/**
	 * @param esResumenModificacion the esResumenModificacion to set
	 */
	public void setEsResumenModificacion(boolean esResumenModificacion) {
		this.esResumenModificacion = esResumenModificacion;
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
	 * @return the vieneDeOrdenesAmbulatorias
	 */
	public boolean isVieneDeOrdenesAmbulatorias() {
		return vieneDeOrdenesAmbulatorias;
	}

	/**
	 * @param vieneDeOrdenesAmbulatorias the vieneDeOrdenesAmbulatorias to set
	 */
	public void setVieneDeOrdenesAmbulatorias(boolean vieneDeOrdenesAmbulatorias) {
		this.vieneDeOrdenesAmbulatorias = vieneDeOrdenesAmbulatorias;
	}
	public boolean isVienedeEntidadesSubcontratadas() {
		return vienedeEntidadesSubcontratadas;
	}
	public void setVienedeEntidadesSubcontratadas(
			boolean vienedeEntidadesSubcontratadas) {
		this.vienedeEntidadesSubcontratadas = vienedeEntidadesSubcontratadas;
	}
	
	public boolean isDeboMostrarVolverlistadoCitas() {
		return deboMostrarVolverlistadoCitas;
	}
	
	public void setDeboMostrarVolverlistadoCitas(
			boolean deboMostrarVolverlistadoCitas) {
		this.deboMostrarVolverlistadoCitas = deboMostrarVolverlistadoCitas;
	}
	public String getCodigoPacienteSolicitudEntSub() {
		return codigoPacienteSolicitudEntSub;
	}
	public void setCodigoPacienteSolicitudEntSub(
			String codigoPacienteSolicitudEntSub) {
		this.codigoPacienteSolicitudEntSub = codigoPacienteSolicitudEntSub;
	}
	

	/**
	 * 
	 */
	public int getSizeMensajes() {
		return mensajes.size();
	}
	/**
	 * @return the mensajes
	 */
	@SuppressWarnings({ "rawtypes"})
	public ArrayList getMensajes() {
		return mensajes;
	}


	/**
	 * @param mensajes the mensajes to set
	 */
	@SuppressWarnings({ "rawtypes"})
	public void setMensajes(ArrayList mensajes) {
		this.mensajes = mensajes;
	}

	/**
	 * @return valor de numeroAutorizacion1
	 */
	public String getNumeroAutorizacion1() {
		return numeroAutorizacion1;
	}

	/**
	 * @param numeroAutorizacion1 el numeroAutorizacion1 para asignar
	 */
	public void setNumeroAutorizacion1(String numeroAutorizacion1) {
		this.numeroAutorizacion1 = numeroAutorizacion1;
	}

	/**
	 * @return valor de listaAntecedentes
	 */
	public ArrayList<DtoDatosGenericos> getListaAntecedentes() {
		return listaAntecedentes;
	}

	/**
	 * @param listaAntecedentes el listaAntecedentes para asignar
	 */
	public void setListaAntecedentes(ArrayList<DtoDatosGenericos> listaAntecedentes) {
		this.listaAntecedentes = listaAntecedentes;
	}

	/**
	 * @return the checkVisibilidadEnlaceOrdenesAmbulatorias
	 */
	public Boolean getCheckVisibilidadEnlaceOrdenesAmbulatorias() {
		return checkVisibilidadEnlaceOrdenesAmbulatorias;
	}

	/**
	 * @param checkVisibilidadEnlaceOrdenesAmbulatorias the checkVisibilidadEnlaceOrdenesAmbulatorias to set
	 */
	public void setCheckVisibilidadEnlaceOrdenesAmbulatorias(
			Boolean checkVisibilidadEnlaceOrdenesAmbulatorias) {
		this.checkVisibilidadEnlaceOrdenesAmbulatorias = checkVisibilidadEnlaceOrdenesAmbulatorias;
	}
	
	
	
}
