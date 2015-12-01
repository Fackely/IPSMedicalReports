/*
* @(#)CitaForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.2_01
*/
package com.princetonsa.actionform.agenda;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.dto.agenda.DTOAdministrarSolicitudesAutorizar;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;

/**
* ActionForm, tiene la funciï¿½n de bean dentro de la forma, que contiene todos los datos necesarios
* para la asignaciï¿½n y reserva de una cita de consulta. Adicionalmente hace el manejo de limpieza de
* la forma y de validaciï¿½n de datos de entrada.
*
* @version 1.0, Mar 24, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class CitaForm extends ValidatorForm implements Serializable
{
	//*************************Atributos empleados en las Autorizaciones de Capitación Subcontratada 
	/** *Atributo que almacena la lista de numero de solicitudes que se validan para generar
	 * proceso de autorización de capitación **/
	private ArrayList<DTOAdministrarSolicitudesAutorizar> listaSolicitudesServiciosAutorizar = new ArrayList<DTOAdministrarSolicitudesAutorizar>();
	
	/** * Este atributo se usa para determinar cuando se generó una autorización de solicitudes y mostrar el respectivo botón */
	private boolean mostrarImprimirAutorizacion;
	
	/**	 * lista que contiene los nombres de los reportes de las autorzaciones **/
	private ArrayList<String> listaNombresReportes;
	
	/** *Atributo que almacena la información de Diagnóstico asociado a la orden ambulatoria generada **/
	private DtoDiagnostico dtoDiagnostico;
	
	/** * Contiene un listado de advertencias a mostrar */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	
	/**Lista que almacena los servicios que se asignaron en la cita*/
	private ArrayList<String> listaServiciosImpimirOrden;
	
	//*************************Atributos empleados en las Autorizaciones de Capitación Subcontratada
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CitaForm.class);
	
	/**
	 * Serial versionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Descricpiï¿½n del tipo de documento
	 */
	private String nombreTipoDocumento;
	
	/**
	 * Descricpiï¿½n del consultorio
	 */
	private String nombreConsultorio;
	
	/**
	 * Nombre del mï¿½dico
	 */
	private String nombreCompletoMedico;
	
	/**
	 * Indica si la cita fue pedida con ese mï¿½dico especificamente
	 */
	private boolean perteneceAlMedico;
	
	/**
	 * Indica si la cita estï¿½ asociada una valoracion pedï¿½atrica
	 */
	private boolean pediatrica;

	/**
	 * Contenedor de datos de un ï¿½tem de agenda de consulta
	 */
	private transient HashMap idb_item;

	/**
	 * Cï¿½digo del ï¿½tem de agenda de consultas
	 */
	private int ii_agenda;

	/**
	 * Cï¿½digo de la cita
	 */
	private int ii_codigo;

	/**
	 * Cï¿½digo del consultorio sobre el cual realizar la bï¿½squeda de la agenda para la
	 * reserva / asignaciï¿½n de la cita
	 */
	private int ii_consultorio;

	/**
	 * 
	 */
	private String nombreCentroAtencion;
	
    /**
     * Cï¿½digo del Centro de atenciï¿½n que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atenciï¿½n del usuario  
     */
    private int centroAtencion;

	/**
	 * Nï¿½mero de la cuenta del paciente
	 */
	private int cuentaPaciente;
	
	/**
	 * Nï¿½mero de citas temporal (Para no perder los
	 * valores entre pï¿½ginas)
	 */
	private int numeroCitasTemporal=0;
	
	/**
	 * Mapa para los centros de costo del usuario que asigna la cita
	 */
	private HashMap mapaCentrosCosto;
	
	/**
	 * HashMap con el codigo del centro de costo seleccionado
	 * en el momento de la asignacion de la cita (centro_costo_solicitado)
	 */
	private HashMap mapaCodCentroCosto;
	
	/**
	 * Temporal de Numero de Servicios utilizado para generar las
	 * finalidades de los servicios de la unidad de agenda a la cual
	 * se desea reservar/asignar la cita
	 */
	private int tempNumServicios;
	
	/**
	 * 
	 */
	private boolean solPYP=false;
	
	/**
	 * 
	 */
	private boolean indicativoOrdenAmbulatoria=false;
	
	/**
	 * 
	 */
	private String ordenAmbulatoria="";
	
	/**
	 * 
	 */
	private String servicioAmbulatorioPostular="";

	/**Variable que almacena por request el codigo del centro de costo seleccionado por Ordenes AMbulatorias*/
	private String centroCostoAmbulatorioSel="";
	
	/**
	* Cï¿½digo del dï¿½a de la semana sobre el cual realizar la bï¿½squeda de la agenda para la
	* reserva / asignaciï¿½n de la cita
	* 1 - Lunes
	* 2 - Martes
	* 3 - Miï¿½rcoles
	* 4 - Jueves
	* 5 - Viernes
	* 6 - Sï¿½bado
	* 7 - Domingo
	*/
	private int ii_diaSemana;

	/** Cï¿½digo del estado de la cita */
	private int ii_estadoCita;

	/**
	* Cï¿½digo del mï¿½dico sobre el cual realizar la bï¿½squeda de la agenda para la reserva / asignaciï¿½n
	* de la cita
	*/
	private int ii_medico;

	/** Cï¿½digo del paciente de la cita */
	private int ii_paciente;

	/** Indice del ï¿½tem seleccionado */
	private int ii_seleccionado;

	/** Cï¿½digo del sexo del paciente */
	private int ii_sexoPaciente;

	/**
	* Cï¿½digo de la unidad de consulta sobre la cual realizar la bï¿½squeda de la agenda para la
	* reserva / asignaciï¿½n de la cita
	*/
	private int ii_unidadConsulta;

	/** Datos de los ï¿½tems de agenda seleccionados para reserva / asiganciï¿½n de cita */
	private transient Map im_agenda;

	/** Identificaciï¿½n del usuario que reservï¿½ / asigno la cita */
	private String is_usuario;

	/** Enlace al detalle de la respuesta de valoracion */
	private String is_enlace;

	/** Estado actual del flujo */
	private String is_estado;
	
	/**Estado del flujo anterior para validar campos del paciente de reserva cita*/
	private String estadoAnterior;

	/** Estado de liquidaciï¿½n de la cita */
	private String estadoLiquidacionCita;
	
	/**Nombre del estado de liquidacion de la cita**/
	private String nombreEstadoLiquidacionCita;

	/** Nombre del estado de la cita */
	private String nombreEstadoCita;

	/**
	* Fecha de finalizaciï¿½n sobre la cual realizar la bï¿½squeda de la agenda para la
	* reserva / asignaciï¿½n de la cita
	*/
	private String is_fechaFin;

	/**
	* Fecha de inicio sobre la cual realizar la bï¿½squeda de la agenda para la reserva / asignaciï¿½n
	* de la cita
	*/
	private String fechaInicio;
	/**
	* Mismo campo de  fechaInicio pero como tipo de Objeto Date, el cual se utilizara para
	* ordenamiento de la coleccion	* 
	*/
	private Date fechaInicioDate;
	
	/**
	 * fecha de la cita solicitada por el paciente
	 */
	private String fechaSolicitada; 
	
	/** Tipo de flujo del formulario */
	private String is_flujo;

	/**
	* Hora de finalizaciï¿½n sobre la cual realizar la bï¿½squeda de la agenda para la
	* reserva / asignaciï¿½n de la cita
	*/
	private String is_horaFin;

	/**
	* Hora de inicio sobre la cual realizar la bï¿½squeda de la agenda para la reserva / asignaciï¿½n de
	* la cita
	*/
	private String horaInicio;

	/** Nombre completo del paciente */
	private String nombreCompletoPaciente;

	/** Nombre de la unidad de consulta */
	private String nombreUnidadConsulta;

	/** Nï¿½mero de identificaciï¿½n del paciente */
	private String numeroIdentificacionPaciente;

	/** Tipo de identificaciï¿½n del paciente */
	private String tipoIdentificacionPaciente;

	/** Listado de ï¿½tems de agenda de consultas para listar */
	private transient Vector iv_agenda;

	/**
	* Nï¿½mero de solicitud generado por la cita
	*/
	private int numeroSolicitudCita;
	
	/**
	 * Entero para manejar el codigo del detalle cargo ( Orden Ambulatoria - detalle Autorizacion)
	 * 	 */
	private int codigoDetalleCargo;
	
	/**
	 * Entero que contiene el codigo del Convenio del Cargo (Autorizaciones) 
	 */
	private int codigoConvenioCargo;
	
    /**
     * Entero qeu contiene  el codido de la Sub_cuenta del cargo ( Autorizaciones)
     */
	private int codigoSubCuentaCargo;
	
	
	
	
	private int consecutivoCita;

	/**
	* Nombre del tipo de la consutla
	*/
	private String nombreTipoConsulta;

	private String servicioTemporal;
	
	private String nombreServicioTemporal;
	
	private boolean tieneErrores;	
	
	/**
	 * campos de la cuenta 
	 */
	private String nombreConvenio;	
	private String nombreContrato;
	private String nombreClasificacionSocial;
	private String nombreTipoAfiliado;
	private String nombreNaturaleza;
	private int codigoEstadoCita;  

	
	//******ATRIBUTOS para la validacion de requisitos paciente***********+
	/**
	 * Variable que indica si los requisitos del paciente estan complidos o no
	 */
	private boolean indicadorRequisitos;
	private int cuenta;
	//*********************************************************************
	
	//-----------------Reserva de Citas -----------------------------//
	/**
	 * Campo para el convenio en la reserva de cita
	 */
	private int convenio;
	
	/**
	 * Campo para el contrato en la reserva de cita
	 */
	private int contrato;
	
	/**
	 * Campo para el estrato social o clasificaci?n socioecon?mica en
	 * la reserva de cita
	 */
	private int estratoSocial;
	
	/**
	 * Campo para tipo de afiliado en la reserva de cita
	 */
	private String tipoAfiliado;
	
	/**
	 * 
	 */
	private int naturalezaPaciente;
	
	/**
	 * Campo para el tipo de regimen del convenio seleccionado
	 */
	private String tipoRegimenConvenio;
	
	/**
	 * Campo para indicar si es requerido capturar los datos de la cuenta
	 * en la reserva de la cita
	 */
	private String datosCuentaReserva;
	
	/**
	 * Campo que indica si ya se realizï¿½ la verificaciï¿½n del usuario
	 * capitado
	 */
	private boolean verificadoUsuarioCapitado;
	
	/**
	 * Campo que guarda el codigo del usuario capitado, si no existe
	 * guarda -1
	 */
	private int codigoUsuarioCapitado;

	/**
	 * Campo que guarda el nombre del convenio capitado
	 */
	private String nombreConvenioCapitado;
	
	/**
	 * Campo que guarda el codigo del convenio capitado
	 */
	private String convenioCapitado;
	
	/**
	 * 
	 * */
	private String esUsuarioCapitado;
	
	/**
	 * Campo que me indica si sï¿½lo debo listar las citas disponibles
	 * en la reserva
	 */
	private boolean soloDisponibles;
	
	/**
	 * telefono del paciente
	 */
	private String telefono;
	
	/**
	 * 
	 */
	private String celular;
	
	/**
	 * 
	 */
	private String nombrePaciente;
	
	/**
	 * 
	 */
	private String otrosTelefonos;
	
	//Atributos para la busqueda avanzada de pacientes en la reserva
	private HashMap busquedaAvanzada = new HashMap();
	
	/**
     * Mapa para el manejo de los mensajes de los servicios en Justificacion Pendiente
     */
    private HashMap justificacionNoPosMap  = new HashMap();
    
    /**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;
	

	/**
	 * Arreglo de Mensajes que informen sobre las incosistencias de 
	 * la Resrva de Citas de un Paciente 
	 */
	private ArrayList<ResultadoBoolean> lisMensaje = new ArrayList<ResultadoBoolean>(); 
 	
	//*************************************************************************
	
    
    private ResultadoBoolean mensaje;
    
    
	/**
	 * Valor que define la posicion del registro de citas
	 */
	private int posCita = 0;
	private int posServicio = 0;
	
	/**
	 * Valor que define la posicion del registro servicio en la ventana de Unidad de Agenda
	 */
	private int posServicioUnidad = 0;
	
	/**
	 * Mapa para listar los servicios de una unidad consulta
	 */
	private HashMap<String, Object> mapaServicios = new HashMap<String, Object>();
	
	/**
	 * Arreglo para listar las condiciones para la toma del servicio
	 */
	private ArrayList<HashMap<String, Object>> condicionesToma = new ArrayList<HashMap<String,Object>>();

	
	private boolean cierreIngresoPostOperatorio=false;
	
	private String motivoNoAtencion;
	
	private String observacion;
	
	/**
	 * Nï¿½mero de la historia clï¿½nica del paciente
	 */
	private String numeroHistoriaClinica;
	
	/**
     * Almacena el numero de autorizaciï¿½n pedido para el caso en que  
     * el parametros 'Realizar Comprobacion solo en cargues vigentes' este en Si 
     * */
    private InfoDatosString autorizacionIngEvento = new InfoDatosString("","");
	
    /**
     * Cï¿½digos de las Citas en el momento de la Reprogramaciï¿½n 
     */
    private String codigosCitasReprogramacion;
    
    /**
     * Arraylist utilitario 
     * */
    private ArrayList arrayListUtilitario_2;
    
    /**
	 * 
	 * */
	private String codigoConsultorioUsua;
	
	/**
	 * 
	 * */
	private String registroMedico;
	
	/**
	 * 
	 * */
	private String loginUsuCita;
	
	/**
	 *
	 */
	private boolean mantenerPacienteCargado;
	
	/**
	 *
	 */
	private boolean cargarInfoPacienteSesion;
    
	/**
	 *
	 */
	private boolean filtrarAgendasXServicio;
	//*******************************************************************************
	
	/**
	 * Dto que contiene los datos de la respuesta de autorizacion relacionada a la asignacion de cita
	 * */
	private DtoRespAutorizacion dtoRespAuto = new DtoRespAutorizacion();
	
	/**
	 * Tipos de vigencia paramitrizados por al institcion
	 * */
	private ArrayList<HashMap<String, Object>> arrayTiposVigencia = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Cargos parametrizables de la institucion para la persona que recibe 
	 * */
	private ArrayList<HashMap<String, Object>> arrayCargosPerRecAuto = new ArrayList<HashMap<String,Object>>();;
	
	private String codigoServicio = "" ;
	
	private String linkSiguiente;
	
	private String bloqueoUsuario;
	
	private String respAutorizacionUsu;
	
	/**
	 * Fecha en que se modifica el estado de la cita
	 */
	private String fechaModifica;
	
	/**
	 * Hora en que se modifica el estado de la cita
	 */
	private String horaModifica;
	
	/**
	 * Nombre del usuario que modifica el estado de la cita
	 */
	private String nombreUsuario;
	
	/**Atributo que almacena la infoResponsable de la cobertura de la cita*/
	private InfoResponsableCobertura infoResponsableCobertura;
	
	/**Atributo quer indica si ocurrieron errores en la generacion de autorizacion*/
	private boolean errorGeneracionAutorizacion;
	
	/** Variable que verifica que no se ejecute el codigo para recardar los contratos cuando se hace clicl en listar las citas
	 * ya que esto ocasionaba que se cayera lña funcionalidad
	 */
     private boolean listando=false;
     private String estadoant="";

	
	//*******************************************************************************
	public DtoRespAutorizacion getDtoRespAuto() {
		return dtoRespAuto;
	}

	public void setDtoRespAuto(DtoRespAutorizacion dtoRespAuto) {
		this.dtoRespAuto = dtoRespAuto;
	}

	public ArrayList<HashMap<String, Object>> getArrayTiposVigencia() {
		return arrayTiposVigencia;
	}

	public void setArrayTiposVigencia(
			ArrayList<HashMap<String, Object>> arrayTiposVigencia) {
		this.arrayTiposVigencia = arrayTiposVigencia;
	}

	public ArrayList<HashMap<String, Object>> getArrayCargosPerRecAuto() {
		return arrayCargosPerRecAuto;
	}

	public void setArrayCargosPerRecAuto(
			ArrayList<HashMap<String, Object>> arrayCargosPerRecAuto) {
		this.arrayCargosPerRecAuto = arrayCargosPerRecAuto;
	}
	
	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	
	/*public int getPosAdjuntos()
	{
		return dtoRespAuto.getPosAdjuntos();
	}
	
	public void setPosAdjuntos(int posAdjuntos)
	{
		dtoRespAuto.setPosAdjuntos(posAdjuntos);
	}*/
	
	
	//*******************************************************************************
	//*******************************************************************************
	
	//***************************************
	// manejo de mmultas por incumplimiento de citas
	private ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	private String preguntarAutorizacionAux;
	private String preguntarAutorizacion;
	private String mostrarFormAutoriCita;
	private String motivoAutorizacionCita;
	private String usuarioAutoriza;
	private String citasIncumpl;
	private String verificarEstCitasPac;
	
	
	private String origenTelefono;
	
		
	
	/**
	 * @return the verificarEstCitasPac
	 */
	public String getVerificarEstCitasPac() {
		return verificarEstCitasPac;
	}

	/**
	 * @param verificarEstCitasPac the verificarEstCitasPac to set
	 */
	public void setVerificarEstCitasPac(String verificarEstCitasPac) {
		this.verificarEstCitasPac = verificarEstCitasPac;
	}

	/**
	 * @return the usuarioAutoriza
	 */
	public String getUsuarioAutoriza() {
		return usuarioAutoriza;
	}

	/**
	 * @param usuarioAutoriza the usuarioAutoriza to set
	 */
	public void setUsuarioAutoriza(String usuarioAutoriza) {
		this.usuarioAutoriza = usuarioAutoriza;
	}

	/**
	 * @return the citasIncumpl
	 */
	public String getCitasIncumpl() {
		return citasIncumpl;
	}

	/**
	 * @param citasIncumpl the citasIncumpl to set
	 */
	public void setCitasIncumpl(String citasIncumpl) {
		this.citasIncumpl = citasIncumpl;
	}

	/**
	 * @return the citasIncumplidas
	 */
	public ArrayList<HashMap<String, Object>> getCitasIncumplidas() {
		return citasIncumplidas;
	}

	/**
	 * @param citasIncumplidas the citasIncumplidas to set
	 */
	public void setCitasIncumplidas(
			ArrayList<HashMap<String, Object>> citasIncumplidas) {
		this.citasIncumplidas = citasIncumplidas;
	}

	/**
	 * @return the motivoAutorizacionCita
	 */
	public String getMotivoAutorizacionCita() {
		return motivoAutorizacionCita;
	}

	/**
	 * @param motivoAutorizacionCita the motivoAutorizacionCita to set
	 */
	public void setMotivoAutorizacionCita(String motivoAutorizacionCita) {
		this.motivoAutorizacionCita = motivoAutorizacionCita;
	}

	/**
	 * @return the preguntarAutorizacion
	 */
	public String getPreguntarAutorizacion() {
		return preguntarAutorizacion;
	}

	/**
	 * @param preguntarAutorizacion the preguntarAutorizacion to set
	 */
	public void setPreguntarAutorizacion(String preguntarAutorizacion) {
		this.preguntarAutorizacion = preguntarAutorizacion;
	}

	/**
	 * @return the mostrarFormAutoriCita
	 */
	public String getMostrarFormAutoriCita() {
		return mostrarFormAutoriCita;
	}

	/**
	 * @param mostrarFormAutoriCita the mostrarFormAutoriCita to set
	 */
	public void setMostrarFormAutoriCita(String mostrarFormAutoriCita) {
		this.mostrarFormAutoriCita = mostrarFormAutoriCita;
	}
	
	/**
	 * @return the preguntarAutorizacionAux
	 */
	public String getPreguntarAutorizacionAux() {
		return preguntarAutorizacionAux;
	}

	/**
	 * @param preguntarAutorizacionAux the preguntarAutorizacionAux to set
	 */
	public void setPreguntarAutorizacionAux(String preguntarAutorizacionAux) {
		this.preguntarAutorizacionAux = preguntarAutorizacionAux;
	}
	
	//***************************************
	
	/** Elimina un ï¿½tem de agenda de la lista para reservar / asignar citas */
	public void eliminarItemSeleccionado()
	{
		int li_items;

		li_items = getNumeroItemsSeleccionados() - 1;

		/* Actualizar el ï¿½ndice de selecciï¿½n de los otros ï¿½tems de agenda seleccionados */
		for(int li_i = getPosCita(); li_i < li_items; li_i++)
		{
			setItemSeleccionado("codigoAgenda_" + li_i, getItemSeleccionado("codigoAgenda_" + (li_i + 1) ));
			setItemSeleccionado("codigoCita_" + li_i, getItemSeleccionado("codigoCita_" + (li_i + 1) ));
			
			setItemSeleccionado("codigoEstadoCita_" + li_i, getItemSeleccionado("codigoEstadoCita_" + (li_i + 1) ));
			
			setItemSeleccionado("codigoEstadoLiquidacion_" + li_i,getItemSeleccionado("codigoEstadoLiquidacion_" + (li_i + 1)));
			setItemSeleccionado("codigoServicio_" + li_i, getItemSeleccionado("codigoServicio_" + (li_i + 1) ));
			setItemSeleccionado("servicioUnidadConsulta_" + li_i, getItemSeleccionado("servicioUnidadConsulta_" + (li_i + 1) ));
			setItemSeleccionado("codigoSexo_" + li_i, getItemSeleccionado("codigoSexo_" + (li_i + 1) ));
			setItemSeleccionado("codigoUnidadConsulta_" + li_i,getItemSeleccionado("codigoUnidadConsulta_" + (li_i + 1) ));
			setItemSeleccionado("codigoCentroAtencion_" + li_i,getItemSeleccionado("codigoCentroAtencion_" + (li_i + 1) ));
			setItemSeleccionado("esPos_" + li_i, getItemSeleccionado("esPos_" + (li_i + 1) ) );
			setItemSeleccionado("fecha_" + li_i, getItemSeleccionado("fecha_" + (li_i + 1) ) );
			setItemSeleccionado("horaFin_" + li_i, getItemSeleccionado("horaFin_" + (li_i + 1) ) );
			setItemSeleccionado("horaInicio_" + li_i, getItemSeleccionado("horaInicio_" + (li_i + 1) ));
			setItemSeleccionado("nombreConsultorio_" + li_i, getItemSeleccionado("nombreConsultorio_" + (li_i + 1) ));
			setItemSeleccionado("nombreMedico_" + li_i, getItemSeleccionado("nombreMedico_" + (li_i + 1) ));
			setItemSeleccionado("nombreUnidadConsulta_" + li_i,getItemSeleccionado("nombreUnidadConsulta_" + (li_i + 1) ));
			setItemSeleccionado("nombreCentroAtencion_" + li_i,getItemSeleccionado("nombreCentroAtencion_" + (li_i + 1) ));
			setItemSeleccionado("numeroAutorizacion_" + li_i,getItemSeleccionado("numeroAutorizacion_" + (li_i + 1) ));
			setItemSeleccionado("numeroSolicitud_" + li_i,getItemSeleccionado("numeroSolicitud_" + (li_i + 1) ));
			setItemSeleccionado("ocupacionMedica_" + li_i,getItemSeleccionado("ocupacionMedica_" + (li_i + 1) ));
			setItemSeleccionado("planControl_" + li_i,getItemSeleccionado("planControl_" + (li_i + 1) ));
			setItemSeleccionado("tipoCita_" + li_i, getItemSeleccionado("tipoCita_" + (li_i + 1) ));
			setItemSeleccionado("validarCitaFecha_" + li_i, getItemSeleccionado("validarCitaFecha_" + (li_i + 1) ));
			setItemSeleccionado("observaciones_" + li_i, getItemSeleccionado("observaciones_" + (li_i + 1) ));
			setItemSeleccionado("numServicios_" + li_i, getItemSeleccionado("numServicios_" + (li_i + 1) ));
			setItemSeleccionado("motivoAuto_" + li_i, getItemSeleccionado("motivoAuto_" + (li_i + 1) ));
			setItemSeleccionado("usuarioAuto_" + li_i, getItemSeleccionado("usuarioAuto_" + (li_i + 1) ));
			setItemSeleccionado("requiereAuto_" + li_i, getItemSeleccionado("requiereAuto_" + (li_i + 1) ));
			
			for(int i=0;i<Integer.parseInt(getItemSeleccionado("numServicios_"+li_i).toString());i++)
			{
				setItemSeleccionado("codigoServicio_" + li_i+"_"+i, getItemSeleccionado("codigoServicio_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("codigoCentroCosto_" + li_i+"_"+i, getItemSeleccionado("codigoCentroCosto_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("observaciones_" + li_i+"_"+i, getItemSeleccionado("observaciones_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("nombreServicio_" + li_i+"_"+i, getItemSeleccionado("nombreServicio_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("codigoSexo_" + li_i+"_"+i, getItemSeleccionado("codigoSexo_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("esPos_" + li_i+"_"+i, getItemSeleccionado("esPos_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("codigoEspecialidad_" + li_i+"_"+i, getItemSeleccionado("codigoEspecialidad_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("tieneCondiciones_" + li_i+"_"+i, getItemSeleccionado("tieneCondiciones_" + (li_i + 1)+"_"+i ));
				setItemSeleccionado("dtoRespAuto_" + li_i+"_"+i, getItemSeleccionado("dtoRespAuto_" + (li_i + 1)+"_"+i ));
			}
		}

		/* Eliminar los datos del ï¿½tem de agenda */
		getItemsSeleccionados().remove("codigoAgenda_" + li_items);
		getItemsSeleccionados().remove("codigoCita_" + li_items);
		getItemsSeleccionados().remove("codigoEstadoCita_" + li_items);
		getItemsSeleccionados().remove("codigoEstadoLiquidacion_" + li_items);
		getItemsSeleccionados().remove("codigoServicio_" + li_items);
		getItemsSeleccionados().remove("servicioUnidadConsulta_" + li_items);
		getItemsSeleccionados().remove("codigoSexo_" + li_items);
		getItemsSeleccionados().remove("codigoUnidadConsulta_" + li_items);
		getItemsSeleccionados().remove("codigoCentroAtencion_" + li_items);
		getItemsSeleccionados().remove("esPos_" + li_items);
		getItemsSeleccionados().remove("fecha_" + li_items);
		getItemsSeleccionados().remove("horaFin_" + li_items);
		getItemsSeleccionados().remove("horaInicio_" + li_items);
		getItemsSeleccionados().remove("nombreConsultorio_" + li_items);
		getItemsSeleccionados().remove("nombreUnidadConsulta_" + li_items);
		getItemsSeleccionados().remove("nombreCentroAtencion_" + li_items);
		getItemsSeleccionados().remove("nombreMedico_" + li_items);
		getItemsSeleccionados().remove("numeroAutorizacion_" + li_items);
		getItemsSeleccionados().remove("numeroSolicitud_" + li_items);
		getItemsSeleccionados().remove("ocupacionMedica_" + li_items);
		getItemsSeleccionados().remove("planControl_" + li_items);
		getItemsSeleccionados().remove("tipoCita_" + li_items);
		getItemsSeleccionados().remove("validarCitaFecha_" + li_items);
		getItemsSeleccionados().remove("observaciones_" + li_items);
		getItemsSeleccionados().remove("numServicios_" + li_items);
		getItemsSeleccionados().remove("dtoRespAuto_" + li_items);
		getItemsSeleccionados().remove("motivoAuto_" + li_items);
		getItemsSeleccionados().remove("usuarioAuto_" + li_items);
		getItemsSeleccionados().remove("requiereAuto_" + li_items);
		
		im_agenda.put("numRegistros", li_items);
	}

	/** Elimina un ï¿½tem de agenda de la lista para reservar / asignar citas */
	public void eliminarItemSeleccionado(int ai_codigoAgenda)
	{
		setCodigoAgenda(ai_codigoAgenda);
		eliminarItemSeleccionado();
	}

	/** Obtener el cï¿½digo del ï¿½tem de agenda de consulta */
	public int getCodigoAgenda()
	{
		return ii_agenda;
	}

	/** Obtener el cï¿½digo de la cita */
	public int getCodigoCita()
	{
		return ii_codigo;
	}

	/** Obtener el cï¿½digo del consultorio */
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/** Obtener el cï¿½digo del dï¿½a de la semana */
	public int getCodigoDiaSemana()
	{
		return ii_diaSemana;
	}

	/** Obtener cï¿½digo del estado de la cita */
	public int getCodigoEstadoCita()
	{
		return ii_estadoCita;
	}

	/** Obtener el cï¿½digo del mï¿½dico */
	public int getCodigoMedico()
	{
		return ii_medico;
	}

	/** Obtener el cï¿½digo del paciente */
	public int getCodigoPaciente()
	{
		return ii_paciente;
	}

	/** Obtener el cï¿½digo del sexo del paciente */
	public int getCodigoSexoPaciente()
	{
		return ii_sexoPaciente;
	}

	/** Obtener la unidad de consulta */
	public int getCodigoUnidadConsulta()
	{
		return ii_unidadConsulta;
	}

	/** Obtener el cï¿½digo del usuario */
	public String getCodigoUsuario()
	{
		return is_usuario;
	}

	/** Obtener la cuenta del paciente */
	public int getCuentaPaciente()
	{
		return cuentaPaciente;
	}

	/** Obtener el enlace al detalle de la respuesta de la valoraciï¿½n */
	public String getEnlace()
	{
		if(!is_enlace.equals("")){
			//return "<a href=\"" + is_enlace + "\" >Detalle</a>";
			return is_enlace;
		}else{
			return "";
		}
	}

	/** Obtener el estado actual del flujo */
	public String getEstado()
	{
		return is_estado;
	}

	/** Obtener el estado de liquidaciï¿½n de la cita */
	public String getEstadoLiquidacionCita()
	{
		return estadoLiquidacionCita;
	}

	/** Obtener la fecha de finalizaciï¿½n */
	public String getFechaFin()
	{
		return is_fechaFin;
	}

	/** Obtener la fecha de inicio */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/** Obtener la hora de finalizaciï¿½n */
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/** Obtener el tipo de flujo del formulario*/
	public String getFlujo()
	{
		return is_flujo;
	}

	/** Obtener la hora de inicio */
	public String getHoraInicio()
	{
		return horaInicio;
	}

	/** Obtener el ï¿½ndice del ï¿½tem seleccionado */
	public int getIndiceItemSeleccionado()
	{
		return ii_seleccionado;
	}

	/** Obtener el contenedor de datos de un ï¿½tem de agenda de consulta */
	public HashMap getItem()
	{
		return idb_item;
	}

	/** Obtener la colecciï¿½n de ï¿½tems de agenda de consulta */
	public Collection getItems()
	{
		return iv_agenda;
	}

	/**
	* Obtener un dato de un ï¿½tem de agenda de la lista de agenda de consulta a
	* reservar / asignar citas
	*/
	public Object getItemSeleccionado(String as_dato)
	{
		return im_agenda.get(as_dato);
	}

	/**
	* Obtener los datos de varios ï¿½tems de agenda de la lista de agenda de consulta a
	* reservar/ asignar citas
	*/
	public Map getItemsSeleccionados()
	{
		return im_agenda;
	}

	/** Obtener el nombre completo del paciente */
	public String getNombreCompletoPaciente()
	{
		return nombreCompletoPaciente;
	}

	/** Obtener el nombre del estado de la cita */
	public String getNombreEstadoCita()
	{
		return nombreEstadoCita;
	}

	/** Obtener el nombre de la unidad de consulta */
	public String getNombreUnidadConsulta()
	{
		return nombreUnidadConsulta;
	}

	/** Obtener el nï¿½mero de identificaciï¿½n del paciente */
	public String getNumeroIdentificacionPaciente()
	{
		return numeroIdentificacionPaciente;
	}

	/** Obtener el nï¿½mero de ï¿½tems de agenda seleccionados para reservar / asignar citas */
	public int getNumeroItemsSeleccionados()
	{
		return (Integer)im_agenda.get("numRegistros");
	}

	/** Obtener el indicador de asociaciï¿½n a una valoracion pedï¿½atrica */
	public boolean isPediatrica()
	{
		return pediatrica;
	}

	/** Obtener el tipo de identificaciï¿½n del paciente */
	public String getTipoIdentificacionPaciente()
	{
		return tipoIdentificacionPaciente;
	}

	/** Obtener el nï¿½mero de ï¿½tems de agenda de consulta a listar */
	public Integer getSize()
	{
		return new Integer(size() );
	}
	
	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * 
	 */
	private String centrosAtencion;
	
	/**
	 * 
	 */
	private String unidadesAgenda;
	
	
	private int numeroCitas;
	
	private int [] codigosCitas=new int[0];
	
	private String indiPO;
	
	
	/**
	 * Variable para validar si viene de ordenes ambulatorias cuando es una solicitud de interconsulta, para ocultar cabezote 
	 */
	private String ocultarEncabezado;
	
	/** Limpia la forma */
	public void reset()
	{
		this.nombreCentroAtencion="";
		//this.ordenAmbulatoria="";
		//this.solPYP=false;
		//this.servicioAmbulatorioPostular="";
		this.cierreIngresoPostOperatorio=false;
		this.mensaje=new ResultadoBoolean(false);
		this.nombreConsultorio="";
		this.tieneErrores=false;
		pediatrica		= false;
		idb_item			= null;

		ii_agenda			= -1;
		ii_codigo			= -1;
		ii_consultorio		= -1;
		cuentaPaciente			= -1;
		ii_diaSemana		= -1;
		ii_estadoCita		= -1;
		ii_medico			= -1;
		ii_paciente			= -1;
		ii_seleccionado		= -1;
		ii_sexoPaciente		= -1;
		ii_unidadConsulta	= -1;
		centroAtencion = ConstantesBD.codigoNuncaValido;

		is_enlace						= "";
		is_estado						= "";
		estadoAnterior					= "";
		estadoLiquidacionCita		= "";
		this.nombreEstadoLiquidacionCita = "";
		is_fechaFin						= "";
		fechaInicio					= "";
		fechaSolicitada="";
		is_flujo						= "";
		is_horaFin						= "";
		horaInicio					= "";
		nombreUnidadConsulta				= "";
		nombreCompletoPaciente						= "";
		numeroIdentificacionPaciente	= "";
		tipoIdentificacionPaciente	= "";
		is_usuario						= "";

		im_agenda	= new HashMap();
		im_agenda.put("numRegistros", 0);
		iv_agenda	= new Vector();

		this.nombreTipoConsulta = "";
		this.nombreCompletoMedico="";
		this.numeroCitasTemporal=0;
		this.servicioTemporal="";
		this.nombreServicioTemporal = "";	
		
		this.indicadorRequisitos = false;
		this.cuenta = 0;
		
		this.mapaCentrosCosto = new HashMap();
		this.mapaCodCentroCosto = new HashMap();
		
		this.perteneceAlMedico=false;
		
		//-------- Reserva de Cita ------------//
		this.convenio=ConstantesBD.codigoNuncaValido;
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.estratoSocial=ConstantesBD.codigoNuncaValido;
		this.tipoAfiliado=ConstantesBD.codigoNuncaValido+"";
		this.naturalezaPaciente=ConstantesBD.codigoNuncaValido;
		this.nombreClasificacionSocial="";
		this.nombreTipoAfiliado="";
		this.nombreNaturaleza="";
		this.tipoRegimenConvenio="";
		this.convenioCapitado = ConstantesBD.codigoNuncaValido+"";
		this.nombreConvenioCapitado = "";
		this.esUsuarioCapitado = ConstantesBD.acronimoSi;
		
		this.soloDisponibles = true;
		
		this.busquedaAvanzada = new HashMap();
		
		this.posCita = 0;
		this.posServicio = 0;
		this.mapaServicios = new HashMap<String, Object>();
		this.condicionesToma = new ArrayList<HashMap<String,Object>>();
		this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
       
        this.centrosAtencion="";
        this.unidadesAgenda="";
        
        this.telefono="";
        this.celular="";
        this.nombrePaciente="";
        
    	this.otrosTelefonos="";
        
        this.motivoNoAtencion = "";
        this.observacion = "";
        this.numeroHistoriaClinica = "";
        this.arrayListUtilitario_2 = new ArrayList();
        		
		this.codigoConsultorioUsua = "";		
		this.registroMedico = "";		
		this.loginUsuCita = "";	
		
		this.preguntarAutorizacionAux = ConstantesBD.acronimoNo;
		this.preguntarAutorizacion = ConstantesBD.acronimoNo;
		this.mostrarFormAutoriCita = ConstantesBD.acronimoNo;
		this.motivoAutorizacionCita = "";
		this.usuarioAutoriza = "";
		this.citasIncumpl = ConstantesBD.acronimoNo;
		this.verificarEstCitasPac = ConstantesBD.acronimoNo;
		this.ocultarEncabezado="N";
		
		this.linkSiguiente = "";
		this.bloqueoUsuario = ConstantesBD.acronimoNo;
		this.respAutorizacionUsu = ConstantesBD.acronimoNo;
		this.mantenerPacienteCargado = false;
		this.cargarInfoPacienteSesion = false;
		this.filtrarAgendasXServicio = false;
		
		this.listaSolicitudesServiciosAutorizar  = new ArrayList<DTOAdministrarSolicitudesAutorizar>();
		this.listaNombresReportes = new ArrayList<String>();
		this.mostrarImprimirAutorizacion= false;		
		//this.dtoDiagnostico = new DtoDiagnostico();
		
		this.listaServiciosImpimirOrden=new ArrayList<String>();
		
		this.nombreUsuario="";
		this.fechaModifica="";
		this.horaModifica="";
		
			
	}
	
	public void resetMensajeAdvertencia()
    {
        this.listaAdvertencias = new ArrayList<String>();
    }


	public String getOcultarEncabezado() {
		return ocultarEncabezado;
	}

	public void setOcultarEncabezado(String ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}

	/**
	 * @return Returns the mapaCodCentroCosto
	 */
	public HashMap getMapaCodCentroCosto()
	{
		return mapaCodCentroCosto;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCodCentroCosto to set.
	 */
	public void setMapaCodCentroCosto(HashMap mapaCodCentroCosto)
	{
		this.mapaCodCentroCosto= mapaCodCentroCosto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCodCentroCosto(String key, Object value) 
	{
		mapaCodCentroCosto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCodCentroCosto(String key) 
	{
		return mapaCodCentroCosto.get(key);
	}
	
	
	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCosto()
	{
		return mapaCentrosCosto;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCosto to set.
	 */
	public void setMapaCentrosCosto(HashMap mapaCentrosCosto)
	{
		this.mapaCentrosCosto= mapaCentrosCosto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCosto(String key, Object value) 
	{
		mapaCentrosCosto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCosto(String key) 
	{
		return mapaCentrosCosto.get(key);
	}
	
	/** Establecer el cï¿½digo del ï¿½tem de agenda */
	public void setCodigoAgenda(int ai_agenda)
	{
		ii_agenda = ai_agenda;
	}

	/** Establecer el cï¿½digo de la cita */
	public void setCodigoCita(int ai_codigo)
	{
		ii_codigo = ai_codigo;
	}

	/** Establecer el cï¿½digo del consultorio */
	public void setCodigoConsultorio(int ai_consultorio)
	{
		ii_consultorio = ai_consultorio;
	}

	/** Establecer el cï¿½digo del dï¿½a de la semana */
	public void setCodigoDiaSemana(int ai_diaSemana)
	{
		ii_diaSemana = ai_diaSemana;
	}

	/** Establecer el cï¿½digo del estado de la cita */
	public void setCodigoEstadoCita(int ai_estadoCita)
	{
		ii_estadoCita = ai_estadoCita;
	}

	/** Establecer el cï¿½digo del mï¿½dico */
	public void setCodigoMedico(int ai_medico)
	{
		ii_medico = ai_medico;
	}

	/** Establecer el cï¿½digo del paciente */
	public void setCodigoPaciente(int ai_paciente)
	{
		ii_paciente = ai_paciente;
	}

	/** Obtener el cï¿½digo del sexo del paciente */
	public void setCodigoSexoPaciente(int ai_sexoPaciente)
	{
		ii_sexoPaciente = ai_sexoPaciente;
	}

	/** Establecer la unidad de consulta */
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		ii_unidadConsulta = ai_unidadConsulta;
	}

	/** Establecer el cï¿½digo del usuario */
	public void setCodigoUsuario(String as_usuario)
	{
		if(as_usuario != null)
			is_usuario = as_usuario.trim();
	}

	/** Establecer la la cuenta del paciente */
	public void setCuentaPaciente(int ai_cuenta)
	{
		cuentaPaciente = ai_cuenta;
	}

	/** Establecer el enlace al detalle de la respuesta de la valoraciï¿½n */
	public void setEnlace(String as_enlace)
	{
		if(as_enlace != null)
			is_enlace = as_enlace.trim();
	}

	/** Establecer el estado actual del flujo */
	public void setEstado(String as_estado)
	{
		if(as_estado != null)
			is_estado = as_estado.trim();
	}

	/** Establecer el estado de liquidaciï¿½n de la cita */
	public void setEstadoLiquidacionCita(String as_estadoLiquidacionCita)
	{
		if(as_estadoLiquidacionCita != null)
			estadoLiquidacionCita = as_estadoLiquidacionCita.trim();
	}

	/** Establecer la fecha de finalizaciï¿½n */
	public void setFechaFin(String as_fechaFin)
	{
		if(as_fechaFin != null)
			is_fechaFin = as_fechaFin.trim();
	}

	/** Establecer la fecha de inicio */
	public void setFechaInicio(String as_fechaInicio)
	{
		if(as_fechaInicio != null)
			fechaInicio = as_fechaInicio.trim();
	}

	/** Establecer el tipo de flujo del formulario*/
	public void setFlujo(String as_flujo)
	{
		if(as_flujo != null)
			is_flujo = as_flujo.trim();
	}

	/** Establecer la hora de finalizaciï¿½n */
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/** Establecer la hora de inicio */
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			horaInicio = as_horaInicio.trim();
	}

	/** Establecer el ï¿½ndice del ï¿½tem seleccionado */
	public void setIndiceItemSeleccionado(int ai_seleccionado)
	{
		ii_seleccionado = ai_seleccionado;
	}

	/**
	* Establecer el contenedor de datos de un ï¿½tem de agenda de consulta
	* @param adb_item <code>HashMap<code> contenedor de datos de un ï¿½tem de agenda de consulta
	*/
	public void setItem(HashMap adb_item)
	{
		idb_item = adb_item;
	}

	

	/**
	* Adicionar un dato de un ï¿½tem de agenda a la lista de agenda de consulta a
	* reservar / asignar citas
	*/
	public void setItemSeleccionado(String as_dato, Object ao_valor)
	{
		if(as_dato != null && as_dato.length() > 0 && ao_valor != null)
			im_agenda.put(as_dato, ao_valor);
	}

	/**
	* Adicionar todos los datos de un ï¿½tem de agenda a la lista de agenda de consulta a
	* reservar / asignar citas
	 * @throws SQLException 
	*
	*/
	public ActionErrors setItemSeleccionado(UsuarioBasico usuario) throws SQLException
	{
		ActionErrors	lae_errors;
		boolean			lb_conflicto;
		boolean			lb_encontrado;
		boolean			lb_repetido;
		HashMap		ldb_agenda;
		int				tamanio;
		Iterator		li_agenda;
		String			ls_fecha;
		String			ls_horaFin;
		String			ls_horaInicio;

		/*
			Obtener el ï¿½tem de agenda que se quiere incluir en la lista de reserva / asignaciï¿½n
			de citas
		*/
		lae_errors		= null;
		lb_conflicto	= false;
		lb_encontrado	= false;
		lb_repetido		= false;
		ldb_agenda		= null;
		li_agenda		= getItems().iterator();
		ls_fecha		= "";
		ls_horaFin		= "";
		ls_horaInicio	= "";

		while(li_agenda.hasNext() && !lb_encontrado)
		{
			ldb_agenda		= (HashMap)li_agenda.next();
			lb_encontrado =
				(Utilidades.convertirAEntero(ldb_agenda.get("codigo")+"")) == getCodigoAgenda();
		}

		/* Obtener el nï¿½mero de ï¿½tems seleccionados */
		tamanio = getNumeroItemsSeleccionados();

		
		/* Se encontro el ï¿½tem de agenda solicitado */
		if(lb_encontrado)
		{
			Date				ld_horaFinActual;
			Date				ld_horaFinNueva;
			Date				ld_horaInicioActual;
			Date				ld_horaInicioNueva;
			int					li_aux;
			SimpleDateFormat	lsdf_sdf;
			String				ls_fechaActual;

			ls_fecha = UtilidadFecha.conversionFormatoFechaAAp( (String)ldb_agenda.get("fecha") );

			ls_horaFin		= ldb_agenda.get("horafin").toString();
			ls_horaInicio	= ldb_agenda.get("horainicio").toString();

			/* El formato de la fecha y hora que se espera es HH:mm en 24 horas */
			lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");

			try
			{
				ld_horaFinNueva		= lsdf_sdf.parse(ls_fecha + " " + ls_horaFin);
				ld_horaInicioNueva	= lsdf_sdf.parse(ls_fecha + " " + ls_horaInicio);
			}
			catch(ParseException lpe_fechaInicio)
			{
				ld_horaFinNueva		= new Date();
				ld_horaInicioNueva	= new Date();
			}

			/* Validar que el ï¿½tem no este presente en la lista */
			for(int i = 0; i < tamanio && !lb_repetido && !lb_conflicto; i++)
			{
				try
				{
					ls_fechaActual = (String)getItemSeleccionado("fecha_" + i);

					ld_horaFinActual = lsdf_sdf.parse(ls_fechaActual + " " + (String)getItemSeleccionado("horaFin_" + i));
					ld_horaInicioActual = lsdf_sdf.parse(ls_fechaActual + " " + (String)getItemSeleccionado("horaInicio_" + i));
				}
				catch(ParseException lpe_fechaInicio)
				{
					ld_horaFinActual	= new Date();
					ld_horaInicioActual	= new Date();
				}
				
				logger.info("\n\nGIOOOOOOOO > "+getItemSeleccionado("codigoAgenda_" + i));
				li_aux		= Utilidades.convertirAEntero(getItemSeleccionado("codigoAgenda_" + i)+"");
				lb_repetido	= (li_aux == getCodigoAgenda() );


				/*
					Determinar si este ï¿½tem de agenda tiene conflictos de horario con los ï¿½tems ya
					existentes
				*/
				if(!lb_repetido)
					lb_conflicto =
						(ld_horaInicioNueva.compareTo(ld_horaInicioActual)	<= 0	&&
							ld_horaInicioActual.compareTo(ld_horaFinNueva)	<= 0)	||
						(ld_horaInicioNueva.compareTo(ld_horaFinActual)		<= 0	&&
							ld_horaFinActual.compareTo(ld_horaFinNueva)		<= 0)	||
						(ld_horaInicioActual.compareTo(ld_horaInicioNueva)	<= 0	&&
							ld_horaInicioNueva.compareTo(ld_horaFinActual)	<= 0);
			}
		}

		if(lb_encontrado && lb_repetido)
		{
			lae_errors = new ActionErrors();
			lae_errors.add(
				"flujo", new ActionMessage("error.cita.itemSeleccionadoRepetido", is_flujo)
			);
		}
		else if(lb_encontrado && lb_conflicto)
		{
			lae_errors = new ActionErrors();
			lae_errors.add(
				"flujo", new ActionMessage("error.cita.itemSeleccionadoConflicto", is_flujo)
			);
		}

		/* El ï¿½tem de agenda se puede incluir */
		if(lb_encontrado && !lb_repetido && !lb_conflicto)
		{
			Integer	li_codigoAgenda;
			//Integer	li_sexo;
			String	ls_tipoCita;
			String codigoEstadoLiquidacion ;

			/* Obtener el cï¿½digo de agenda a inlcuir */
			li_codigoAgenda = new Integer(getCodigoAgenda() );


			/* Determinar si la cita es de reserva o de asignaciï¿½n */
			if(is_estado.equals("validarListadoAsignar") )
			{
				ls_tipoCita = "A";
				codigoEstadoLiquidacion = ConstantesBD.codigoEstadoLiquidacionLiquidada;
			}
			else
			{
				ls_tipoCita = "R";
				codigoEstadoLiquidacion = ConstantesBD.codigoEstadoLiquidacionSinLiquidar;
			}

			/* Obtener el sexo del procedimiento */
			/**if( (li_sexo = (Integer)ldb_agenda.get("codigo_sexo") ) == null)
				li_sexo = new Integer(ConstantesBD.codigoSexoTodos);**/

			setItemSeleccionado("codigoAgenda_" + tamanio, li_codigoAgenda);
			setItemSeleccionado("codigoCita_" + tamanio, new Integer(-1) );
			setItemSeleccionado(
				"codigoEstadoCita_" + tamanio, new Integer(ConstantesBD.codigoEstadoCitaAProgramar)
			);
			setItemSeleccionado(
				"codigoEstadoLiquidacion_" + tamanio, codigoEstadoLiquidacion
			);
			
			java.sql.Connection con=UtilidadBD.abrirConexion();
			setItemSeleccionado(
					"nombreEstadoCita_" + tamanio, Utilidades.getNombreEstadoCita(con, new Integer(ConstantesBD.codigoEstadoCitaAProgramar))
				);
			setItemSeleccionado(
					"nombreEstadoLiquidacion_" + tamanio, Utilidades.getNombreEstadoLiquidacion(con,codigoEstadoLiquidacion)
				);
				
			//setItemSeleccionado(
				//"codigoServicio_" + tamanio, (Integer)ldb_agenda.get("codigoservicio")
			//);
			//setItemSeleccionado("codigoSexo_" + tamanio, li_sexo);
			setItemSeleccionado("codigoUnidadConsulta_" + tamanio, Utilidades.convertirAEntero(ldb_agenda.get("codigounidadconsulta")+""));
			setItemSeleccionado("codigoCentroAtencion_" + tamanio, Utilidades.convertirAEntero(ldb_agenda.get("codigocentroatencion")+""));
			
			//setItemSeleccionado("esPos_" + tamanio, (Boolean)ldb_agenda.get("espos") );
			setItemSeleccionado("fecha_" + tamanio, ls_fecha);
			setItemSeleccionado("horaFin_" + tamanio, ls_horaFin);
			setItemSeleccionado("horaInicio_" + tamanio, ls_horaInicio);
			setItemSeleccionado("nombreConsultorio_" + tamanio, (String)ldb_agenda.get("nombreconsultorio"));
			setItemSeleccionado("nombreMedico_" + tamanio, (String)ldb_agenda.get("nombremedico"));
			setItemSeleccionado("nombreUnidadConsulta_" + tamanio,(String)ldb_agenda.get("nombreunidadconsulta"));
			setItemSeleccionado("nombreCentroAtencion_" + tamanio,(String)ldb_agenda.get("nombrecentroatencion"));
			//setItemSeleccionado("numeroAutorizacion_" + tamanio, "");
			//setItemSeleccionado("numeroSolicitud_" + tamanio, "");
			setItemSeleccionado("ocupacionMedica_" + tamanio, Utilidades.convertirAEntero(ldb_agenda.get("ocupacionmedica")+""));
			setItemSeleccionado("planControl_" + tamanio, "");
			setItemSeleccionado("tipoCita_" + tamanio, ls_tipoCita);
			setItemSeleccionado("validarCitaFecha_" + tamanio, new Boolean(true) );
			setItemSeleccionado("prioritaria_" + tamanio, ConstantesBD.acronimoNo);
			setItemSeleccionado("numServicios_"+ tamanio, "0");
			setItemSeleccionado("citaProcesada_"+ tamanio, ConstantesBD.acronimoNo);
			
			setItemSeleccionado("numRegistros", (tamanio+1) );
			//Se carga los centros de costo propios para cada agenda
			Cita cita = new Cita ();
			
			setItemSeleccionado("mapaCentrosCosto_"+tamanio, (HashMap)cita.consultarCentrosCostoXUnidadDeConsulta(con, li_codigoAgenda, Utilidades.convertirAEntero(ldb_agenda.get("codigocentroatencion")+""), usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(ldb_agenda.get("codigounidadconsulta")+"")));
			UtilidadBD.cerrarConexion(con); 
			
		}

		return lae_errors;
	}

	/**
	* Adicionar los datos de varios ï¿½tems de agenda a la lista de agenda de consulta a
	* reservar/ asignar citas
	*/
	public void setItemsSeleccionados(Map am_agenda)
	{
		if(am_agenda != null)
			im_agenda = am_agenda;
		else
			im_agenda.clear();
	}

	/**
	* Establecer el conjunto de ï¿½tems de agenda de consulta a listar
	* @param ac_agenda <code>Collection<code> de ï¿½tems de agenda de consulta a listar
	*/
	public void setItems(Collection ac_agenda)
	{
		iv_agenda = (ac_agenda != null) ? new Vector(ac_agenda) : new Vector(0);
	}

	/** Establecer el nombre completo del paciente */
	public void setNombreCompletoPaciente(String as_paciente)
	{
		if(as_paciente != null)
			nombreCompletoPaciente = as_paciente.trim();
	}

	/** Establecer el nombre del estado de la cita */
	public void setNombreEstadoCita(String as_estadoCita)
	{
		if(as_estadoCita != null)
			nombreEstadoCita = as_estadoCita.trim();
	}

	/** Establecer el nombre de la unidad de consulta */
	public void setNombreUnidadConsulta(String as_unidadConsulta)
	{
		if(as_unidadConsulta != null)
			nombreUnidadConsulta = as_unidadConsulta.trim();
	}

	/** Establecer el nï¿½mero de identificaciï¿½n del paciente */
	public void setNumeroIdentificacionPaciente(String as_numeroIdentificacionPaciente)
	{
		if(as_numeroIdentificacionPaciente != null)
			numeroIdentificacionPaciente = as_numeroIdentificacionPaciente.trim();
	}

	/** Establecer el indicador de asociaciï¿½n a una valoraciï¿½n pedï¿½atrica */
	public void setPediatrica(boolean ab_pediatrica)
	{
		pediatrica = ab_pediatrica;
	}

	/** Establecer el tipo de identificaciï¿½n del paciente */
	public void setTipoIdentificacionPaciente(String as_tipoIdentificacionPaciente)
	{
		if(as_tipoIdentificacionPaciente != null)
			tipoIdentificacionPaciente = as_tipoIdentificacionPaciente.trim();
	}

	/** Obtener el nï¿½mero de ï¿½tems de agenda de consulta a listar */
	public int size()
	{
		return iv_agenda == null ? 0 : iv_agenda.size();
	}

	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validaciï¿½n encontrados. Si no se
	* encontraron errores de validaciï¿½n, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que estï¿½ siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping aam_m, HttpServletRequest request)
	{
		ActionErrors lae_errors;

		lae_errors = new ActionErrors();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//modificado por tarea Nï¿½ 751
		if (is_estado.equals("asignar"))
		{
			if (this.getDatosCuentaReserva().equals(ConstantesBD.acronimoSi))
			{
				int numReg= Utilidades.convertirAEntero(im_agenda.get("numRegistros")+"");
			
				for (int i=0;i<numReg;i++)
				{
					System.out.print("\n el valor de numServicios --> "+im_agenda.get("numServicios_"+i));
					if (Utilidades.convertirAEntero(im_agenda.get("numServicios_"+i)+"")<1)
						lae_errors.add("", new ActionMessage("error.cita.asignacion.MinServicio", "un","asignar la cita Nï¿½ "+(i+1)));
				}
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/* Realizar la validaciï¿½n especificada en el archivo validation.xml */
		if(
			!is_estado.equals("prepararListadoAsignar") &&
			!is_estado.equals("prepararListadoReservar")
		)
			lae_errors.add(super.validate(aam_m, request) );

		if(lae_errors.isEmpty() )
		{
			if(
				is_estado.equals("listadoAsignar")			||
				is_estado.equals("listadoReservar")			||
				is_estado.equals("validarListadoAsignar")	||
				is_estado.equals("validarListadoAsignarOA")	||
				is_estado.equals("validarListadoReservar")
			)
			{
				/* Validar esto sï¿½ y solo si no se han encontrado errores */
				Date				ld_fechaInicio;
				Date				ld_fechaFin;
				Date				ld_fechaSolicitada = new Date();
				SimpleDateFormat	lsdf_sdfFecha;

				/* Iniciar variables. El formato de fecha que se espera es dd/MM/yyyy */
				lsdf_sdfFecha = new SimpleDateFormat("dd/MM/yyyy");

				/* Exije una interpretaciï¿½n estricta del formato de fecha esperado */
				lsdf_sdfFecha.setLenient(false);

				try
				{
					/* Obtener la fecha de inicio */
					if(fechaInicio.equals(""))
					{
						lae_errors.add(
										"fechaInicio", new ActionMessage("errors.required", "Fecha Inicial")
									);
						ld_fechaInicio = null;
					}
					else
					{
						ld_fechaInicio = lsdf_sdfFecha.parse(fechaInicio);
					}
				}
				catch(ParseException lpe_fechaInicio)
				{
					lae_errors.add(
						"fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Inicial")
					);
					ld_fechaInicio = null;
				}
				
				
				
				//Validaciones Fecha Solicitada por el paciente
				try
				{
					/* Obtener la fecha solicitada por el paciente */
					if(!fechaSolicitada.equals(""))
						ld_fechaSolicitada = lsdf_sdfFecha.parse(fechaSolicitada);
				}
				catch(ParseException lpe_fechaInicio)
				{
					lae_errors.add("fechaSolicitada", new ActionMessage("errors.formatoFechaInvalido", "Fecha Solicitada de la Cita"));
					ld_fechaSolicitada = null;
				}
				try
				{
					//si la fecha es valida se continua validando ->
					if (ld_fechaSolicitada!=null)
						if (ld_fechaSolicitada.compareTo(lsdf_sdfFecha.parse(UtilidadFecha.getFechaActual()))<0) 
							lae_errors.add("fechaAsignarMenorActual", new ActionMessage("errors.fechaAnteriorIgualActual", "solicitada", "actual"));
				}	
				catch(ParseException lpe_fechaInicio)
				{
					System.out.print(lpe_fechaInicio);
				}
				
				
				
				
				
				
				
				
				
				if (is_estado!=null&&is_estado.equals("validarListadoAsignar"))
				{
					String fechaActual=UtilidadFecha.getFechaActual();
					Date fechaActualDate;
					//No manejo el error porque fecha Actual siempre me da las cosas bien
					try
					{ 
						fechaActualDate=lsdf_sdfFecha.parse(fechaActual);
						if (ld_fechaInicio.compareTo(fechaActualDate)<0)
						{
							lae_errors.add("fechaAsignarMenorActual", new ActionMessage("errors.fechaAnteriorIgualActual", "inicial", "actual"));
						}
					}
					catch (Exception e)
					{
						ld_fechaInicio = null;
					}
				}

				

				try
				{
					/* Obtener la fecha de finalizaciï¿½n */
					if(is_fechaFin.equals(""))
					{
						lae_errors.add(
										"fechaFin", new ActionMessage("errors.required", "Fecha Final")
									);
						ld_fechaFin = null;
					}
					else
					{
						ld_fechaFin = lsdf_sdfFecha.parse(is_fechaFin);
					}
				}
				catch(ParseException lpe_fechaFin)
				{
					lae_errors.add(
						"fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Final")
					);
					ld_fechaFin = null;
				}

				if(ld_fechaFin != null && ld_fechaInicio != null)
				{
					/*
						Verificar que el listado de citas solo traiga citas cuya fecha sea mayor o
						igual a la fecha actual
					*/
					if(is_estado.equals("validarListadoReservar") )
					{
						Calendar lc_cal;

						lc_cal = new GregorianCalendar();
						lc_cal.set(Calendar.HOUR_OF_DAY, 0);
						lc_cal.set(Calendar.MILLISECOND, 0);
						lc_cal.set(Calendar.MINUTE, 0);
						lc_cal.set(Calendar.SECOND, 0);

						if(lc_cal.getTime().after(ld_fechaInicio) )
							lae_errors.add(
								"fechaInicio",
								new ActionMessage(
									"errors.fechaAnteriorAOtraDeReferencia", "Inicial", "actual"
								)
							);
					}

					/* La fecha de inicio debe ser anterior a la fecha de finalizaciï¿½n */
					if(ld_fechaFin.before(ld_fechaInicio) )
						lae_errors.add(
							"fechaFin",
							new ActionMessage(
								"errors.fechaAnteriorAOtraDeReferencia",
								"Fecha Final",
								"Fecha Inicial"
							)
						);
				}

				//-----Se valida que no se genere una agenda para un rango de fechas mayor de tres meses-----//
				if (lae_errors.isEmpty())
					{
					int nroMeses = UtilidadFecha.numeroMesesEntreFechas(fechaInicio, is_fechaFin,true);
					if (nroMeses > 3)
						{
						if (is_estado.equals("validarListadoAsignar"))
							lae_errors.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "ASIGNAR CITA"));
						else if (is_estado.equals("validarListadoReservar"))
							lae_errors.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "RESERVAR CITAS"));
						}
					}
				
				/* Validar esto sï¿½ y solo si no se han encontrado errores */
				if(!horaInicio.equals("") && !is_horaFin.equals("") )
				{
					Date				ld_horaInicio;
					Date				ld_horaFin;
					SimpleDateFormat	lsdf_sdfHora;

					/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
					lsdf_sdfHora = new SimpleDateFormat("H:mm");

					/* Exije una interpretaciï¿½n estricta del formato de hora esperado */
					lsdf_sdfHora.setLenient(false);

					try
					{
						/* Obtener la hora de inicio */
						ld_horaInicio = lsdf_sdfHora.parse(horaInicio);
					}
					catch(ParseException lpe_horaInicio)
					{
						lae_errors.add(
							"horaInicio",
							new ActionMessage("errors.formatoHoraInvalido", "de inicio")
						);
						ld_horaInicio = null;
					}

					try
					{
						/* Obtener la hora de finalizaciï¿½n */
						ld_horaFin = lsdf_sdfHora.parse(is_horaFin);
					}
					catch(ParseException lpe_horaFin)
					{
						lae_errors.add(
							"horaFin",
							new ActionMessage("errors.formatoHoraInvalido", "de finalizaciï¿½n")
						);
						ld_horaFin = null;
					}

					/* La hora de inicio debe ser anterior a la hora de finalizaciï¿½n */
					if(
						ld_horaFin != null &&
						ld_horaInicio != null &&
						ld_horaFin.before(ld_horaInicio)
					)
						lae_errors.add(
							"horaFin",
							new ActionMessage(
								"errors.fechaAnteriorAOtraDeReferencia", "final", "inicial"
							)
						);
				}
			}

			if(is_estado.equals("reservar") || is_estado.equals("verificarEstCitasPac"))
			{
				if(getTipoIdentificacionPaciente().length() < 1)
					lae_errors.add(
						"tipoIdentificacionPaciente",
						new ActionMessage("errors.required", "Tipo de Identificaciï¿½n del Paciente")
					);

				if(getNumeroIdentificacionPaciente().length() < 1)
					lae_errors.add(
						"numeroIdentificacionPaciente",
						new ActionMessage("errors.required", "Nï¿½mero de Identificaciï¿½n del Paciente")
					);

				if(getNumeroItemsSeleccionados() < 1)
					lae_errors.add(
						"numeroIdentificacionPaciente",
						new ActionMessage("error.cita.noAgendaSeleccionada", "reservar")
					);
				if(!this.telefono.trim().equals(""))
				{
					try
					{
						Integer.parseInt(this.telefono);
					}
					catch(Exception e)
					{
						lae_errors.add("telefono",	new ActionMessage("errors.integer", "Telefono"));
					}
				}
				else
				{
					lae_errors.add("telefono",new ActionMessage("errors.required", "El teléfono"));
				}
				if(!this.celular.trim().equals(""))
				{
					try
					{
						Double.parseDouble(this.celular);
					}
					catch(Exception e)
					{
						lae_errors.add("celular",	new ActionMessage("errors.integer", "Celular"));
					}
				}
				if(getVerificarEstCitasPac().equals(ConstantesBD.acronimoNo))
				{
				
					
					///Si datosCuentaReserva esta en true se debe validar como requerido los campos 
					//de informacion de cuenta
					if(UtilidadTexto.getBoolean(this.datosCuentaReserva))
					{
						//Se verifica que se haya seleccionado el convenio 
						if(this.convenio==ConstantesBD.codigoNuncaValido)
						{
							lae_errors.add(
									"convenio",
									new ActionMessage("errors.required", "El Convenio")
								);
						}
						/**MT 2163- Validar cuando el convenio no maneja Montos Cobro, 
						 * no es requerido la clasificacion S.E. ni el tipo de Afiliado
						 * se traslada validacion al action*/
						//Se verifica que se haya seleccionado la clasificacion socioeconomico o estrato social
						/*if(this.estratoSocial==ConstantesBD.codigoNuncaValido)
						{
							lae_errors.add(
									"estratoSocial",
									new ActionMessage("errors.required", "La Clasificaciï¿½n Socioeconï¿½mica")
								);
						}
						
						//Se verifica que se haya seleccionado el tipo de afiliado
						if(this.tipoAfiliado.equals(ConstantesBD.codigoNuncaValido+""))
						{
							lae_errors.add(
									"tipoAfiliado",
									new ActionMessage("errors.required", "El Tipo de Afiliado")
								);
						}*/
					lae_errors = validacionDetalleCitas(lae_errors);
					}//if datosCuentaReserva
				}
			}

			if(is_estado.equals("reservar") || is_estado.equals("asignar") )
			{
				/*
					Validar que la fecha y hora de inicio de la agenda a reservar / asignar sea
					mayor que la fecha actual
				*/
				Date				ld_agenda;
				int					li_estadoCita;
				SimpleDateFormat	lsdf_sdf;
				String				ls_fecha;
				String				ls_hora;
				//String 				numeroAutorizacion;

				/*
					Iniciar variables. El formato de tiempo que se espera esdd/MM/yyyy HH:mm en 24
					horas
				*/
				lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");

				/* Exije una interpretaciï¿½n estricta del formato de hora esperado */
				lsdf_sdf.setLenient(false);

				for(int li_i = 0, li_tam =getNumeroItemsSeleccionados(); li_i < li_tam; li_i++)
				{
					logger.info("CAMPO PRIORIDAD=> "+getItemSeleccionado("prioritaria_"+li_i));
					try
					{
						ls_fecha		= (String)getItemSeleccionado("fecha_" + li_i);
						ls_hora			= (String)getItemSeleccionado("horaInicio_" + li_i);
						li_estadoCita	= Integer.parseInt(getItemSeleccionado("codigoEstadoCita_" + li_i).toString() );
						/*verificar numero autorizacion sin espacios*/
						/**numeroAutorizacion=(String)getItemSeleccionado("numeroAutorizacion_" + li_i);
						numeroAutorizacion=numeroAutorizacion.trim();
						//if(numeroAutorizacion.matches("[a-zA-Z0-9]*[^a-zA-Z0-9]{1,}[a-zA-Z0-9]*[^a-zA-Z0-9]{1,}[a-zA-Z0-9]*") || numeroAutorizacion.matches("[^a-zA-Z0-9]*[a-zA-Z0-9]*[^a-zA-Z0-9]{1,}[a-zA-Z0-9]*[^a-zA-Z0-9]*") ){
						if( numeroAutorizacion.matches("([a-zA-Z0-9]*[^a-zA-Z0-9-]+[a-zA-Z0-9]*){1,}") )
						{
							lae_errors.add("", new ActionMessage("prompt.numeroAutorizacion.maskmsg"));
						}**/
						/* Obtener la fecha y hora de inicio */
						ld_agenda = lsdf_sdf.parse(ls_fecha + " " + ls_hora);
						UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
						
						int minutosCitaCaducada=0;
						String minutosCaduca=ValoresPorDefecto.getMinutosEsperaCitaCaduca(usuario.getCodigoInstitucionInt());
						
						if (UtilidadCadena.noEsVacio(minutosCaduca))
							minutosCitaCaducada=Integer.parseInt(ValoresPorDefecto.getMinutosEsperaCitaCaduca(usuario.getCodigoInstitucionInt()));
						
						//Fecha Hora Actual menos minutosCitaCaducada 
						String[] fechaHoraActualModificada = UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), -minutosCitaCaducada, false);
						logger.info("minutosCitaCaducada -> "+minutosCitaCaducada);
						logger.info("fechaHoraActualModificada - > "+fechaHoraActualModificada[0]+" "+fechaHoraActualModificada[1]);
						
						Date fechaActual;
						/*if(is_estado.equals("reservar"))
						{
							fechaActual=lsdf_sdf.parse(UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual());
						}
						else
						{
							fechaActual=lsdf_sdf.parse(UtilidadFecha.getFechaActual()+" "+horaActualModificada);
						}*/
						
						fechaActual = lsdf_sdf.parse(fechaHoraActualModificada[0]+" "+fechaHoraActualModificada[1]);
						
						if( ( is_estado.equals("reservar")||(	is_estado.equals("asignar")	&&	li_estadoCita == ConstantesBD.codigoEstadoCitaAProgramar	) ) && ld_agenda.before(fechaActual))
						{
							lae_errors.add(
								"",
								new ActionMessage(
									"error.cita.agendaVencida",
									(String)getItemSeleccionado("nombreUnidadConsulta_" + li_i),
									ls_fecha,
									ls_hora,
									"programada"
								)
							);
						}
						
						//SE verifica el centro de costo es requerido
						/**for(int j=0;j<Integer.parseInt(this.getItemSeleccionado("numServicios_"+li_i).toString());j++)
						{
							if(UtilidadTexto.isEmpty(this.getItemSeleccionado("codigoCentroCosto_"+li_i+"_"+j)+""))
								lae_errors.add("", new ActionMessage("errors.required","El centro de costo del servicio "+
										this.getItemSeleccionado("nombreServicio_" + li_i + "_" + j)+" de "+
										this.getItemSeleccionado("nombreUnidadConsulta_" + li_i)+" en la fecha "+
										this.getItemSeleccionado("fecha_" + li_i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(this.getItemSeleccionado("horaInicio_" + li_i).toString())));
						}**/
					}
					catch(ParseException lpe_horaInicio)
					{
					}
				}
			}
		}

		/* Si no hay errores limpiar el contenedor */
		if(this.is_estado.equals("asignar"))
		{
			lae_errors = validacionDetalleCitas(lae_errors);
		}
		this.tieneErrores=!lae_errors.isEmpty();
		if(lae_errors.isEmpty() )
			lae_errors = null;
		else
		{
			if(is_estado.equals("listadoAsignar") || is_estado.equals("validarListadoAsignar") )
				setEstado("validarListadoAsignar");
			else if(
				is_estado.equals("listadoReservar") || is_estado.equals("validarListadoReservar")
			)
				setEstado("validarListadoReservar");
		}
		
		
		return lae_errors;
	}
	
	
	
	
	/**
	 * Validacion del detalle de las citas
	 * @param lae_errors
	 * @return
	 */
	private ActionErrors validacionDetalleCitas(ActionErrors lae_errors) 
	{
		String numeroAutorizacion = "";
		
		
		
		for(int i=0;i<getNumeroItemsSeleccionados();i++)
		{
			//Validamos que se selccione un centro de costo solicitado por cada una de las citas
			int numServicios = Utilidades.convertirAEntero(im_agenda.get("numServicios_"+i)+"");
			logger.info("numServicios -->"+numServicios);
			int contador = 0;
			if(numServicios>0)
			{
				for(int j=0;j<numServicios;j++)
				{
					logger.info("codigoServicio=> "+getItemSeleccionado("codigoServicio_"+i+"_"+j));
					
					//Se excluyen los servicios eliminados
					if(getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
					{
					
						this.getItemsSeleccionados().remove("generada_"+i);
						
						//Se verifica el centro de costo
						if(getItemSeleccionado("codigoCentroCosto_"+i+"_"+j)==null||getItemSeleccionado("codigoCentroCosto_"+i+"_"+j).toString().equals(""))
							lae_errors.add("CC Solicitado", 
								new ActionMessage(
									"errors.required",
									"El centro de costo solicitado para la cita Nï¿½ "+(j+1)+" y el servicio "+getItemSeleccionado("nombreServicio_"+i+"_"+j)));
						
						//Se verifica las finalidades del servicio de procedimientos y noCruentos
						/*if(getItemSeleccionado("codigoFinalidades_"+i+"_"+j)==null||getItemSeleccionado("codigoFinalidades_"+i+"_"+j).toString().equals(""))
							lae_errors.add("Finalidad", 
								new ActionMessage(
									"errors.required",
									"La finalidad para la cita Nï¿½ "+(j+1)+" y el servicio "+getItemSeleccionado("nombreServicio_"+i+"_"+j)));*/
						
						//Se verifica al nï¿½mero de autorizacion
						if(this.is_estado.equals("asignar"))
						{
							numeroAutorizacion=getItemSeleccionado("numeroAutorizacion_" + i +"_"+j)==null?"":getItemSeleccionado("numeroAutorizacion_" + i +"_"+j).toString();
							numeroAutorizacion=numeroAutorizacion.trim();
							if(!numeroAutorizacion.equals("")&& numeroAutorizacion.matches("([a-zA-Z0-9]*[^a-zA-Z0-9-]+[a-zA-Z0-9]*){1,}") )
							{
								lae_errors.add("", new ActionMessage("prompt.numeroAutorizacion.maskmsg2","para la cita Nï¿½ "+(i+1)+" y cï¿½digo de servicio "+getItemSeleccionado("codigoServicio_"+i+"_"+j)));
							}
						}
						
						
						contador ++;
						
					}
				}
			}
			
			
			if(
					contador<=0&&
					(
							this.is_estado.equals("reservar")||
							(this.is_estado.equals("asignar")&&!this.getItemSeleccionado("codigoEstadoLiquidacion_"+i).toString().equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar))
					)
				)
			{
				if(this.is_estado.equals("asignar"))
					this.setItemSeleccionado("generada_"+i,"false");
				
				//Se valida que se haya seleccionado al menos un servicio para la cita
				lae_errors.add("Mï¿½nimo un servicio",new ActionMessage("errors.paciente.requeridoIngresoDe","mï¿½nimo un servicio para la cita Nï¿½ "+(i+1)));
			}
			
		}
		
		return lae_errors;
	}

	/**
	 * @return Returns the consecutivoCita.
	 */
	public int getConsecutivoCita()
	{
		return consecutivoCita;
	}
	/**
	 * @param consecutivoCita The consecutivoCita to set.
	 */
	public void setConsecutivoCita(int consecutivoCita)
	{
		this.consecutivoCita= consecutivoCita;
	}
	/**
	* Retorna el nï¿½mero de solicitud generado por la cita
	* @return
	*/
	public int getNumeroSolicitudCita()
	{
		return numeroSolicitudCita;
	}

	/**
	* Asigna el nï¿½mero de solicitud generado por la cita
	* @param i
	*/
	public void setNumeroSolicitudCita(int i)
	{
		numeroSolicitudCita = i;
	}

	/**
	* Retorna el nombre del tipo de la consutla
	* @return
	*/
	public String getNombreTipoConsulta()
	{
		return nombreTipoConsulta;
	}

	/**
	* Asigna el nombre del tipo de la consutla
	* @param string
	*/
	public void setNombreTipoConsulta(String string)
	{
		nombreTipoConsulta = string;
	}
	/**
	 * Returns the nombreCompletoMedico.
	 * @return String
	 */
	public String getNombreCompletoMedico() {
		return nombreCompletoMedico;
	}

	/**
	 * Sets the nombreCompletoMedico.
	 * @param nombreCompletoMedico The nombreCompletoMedico to set
	 */
	public void setNombreCompletoMedico(String nombreCompletoMedico) {
		this.nombreCompletoMedico = nombreCompletoMedico;
	}

	/**
	 * @return
	 */
	public int getNumeroCitasTemporal() {
		return numeroCitasTemporal;
	}

	/**
	 * @param i
	 */
	public void setNumeroCitasTemporal(int i) {
		numeroCitasTemporal = i;
	}

	/**
	 * Returns the nombreConsultorio.
	 * @return String
	 */
	public String getNombreConsultorio() {
		return nombreConsultorio;
	}

	/**
	 * Sets the nombreConsultorio.
	 * @param nombreConsultorio The nombreConsultorio to set
	 */
	public void setNombreConsultorio(String nombreConsultorio) {
		this.nombreConsultorio = nombreConsultorio;
	}

	/**
	 * Returns the nombreTipoDocumento.
	 * @return String
	 */
	public String getNombreTipoDocumento()
	{
		return nombreTipoDocumento;
	}

	/**
	 * Sets the nombreTipoDocumento.
	 * @param nombreTipoDocumento The nombreTipoDocumento to set
	 */
	public void setNombreTipoDocumento(String nombreTipoDocumento)
	{
		this.nombreTipoDocumento = nombreTipoDocumento;
	}

	/**
	 * @return Returns the servicionTemporal.
	 */
	public String getServicioTemporal() {
		return servicioTemporal;
	}
	/**
	 * @param servicionTemporal The servicionTemporal to set.
	 */
	public void setServicioTemporal(String servicioTemporal) {
		this.servicioTemporal = servicioTemporal;
	}
	/**
	 * @return Returns the im_agenda.
	 */
	public Map getIm_agenda() {
		return im_agenda;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getIm_agenda(String key) 
	{
		return im_agenda.get(key);
	}
	/**
	 * @param im_agenda The im_agenda to set.
	 */
	public void setIm_agenda(Map im_agenda) {
		this.im_agenda = im_agenda;
	}
	/**
	 * @return Returns the tieneErrores.
	 */
	public boolean isTieneErrores() {
		return tieneErrores;
	}
	/**
	 * @param tieneErrores The tieneErrores to set.
	 */
	public void setTieneErrores(boolean tieneErrores) {
		this.tieneErrores = tieneErrores;
	}	

	public int getCuenta() {
		return cuenta;
	}

	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	public boolean isIndicadorRequisitos() {
		return indicadorRequisitos;
	}

	public void setIndicadorRequisitos(boolean indicadorRequisitos) {
		this.indicadorRequisitos = indicadorRequisitos;
	}

	public boolean getPerteneceAlMedico()
	{
		return perteneceAlMedico;
	}

	public void setPerteneceAlMedico(boolean perteneceAlMedico)
	{
		this.perteneceAlMedico = perteneceAlMedico;
	}

	public boolean isIndicativoOrdenAmbulatoria() {
		return indicativoOrdenAmbulatoria;
	}

	public void setIndicativoOrdenAmbulatoria(boolean indicativoOrdenAmbulatoria) {
		this.indicativoOrdenAmbulatoria = indicativoOrdenAmbulatoria;
	}

	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public boolean isSolPYP() {
		return solPYP;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public String getServicioAmbulatorioPostular() {
		return servicioAmbulatorioPostular;
	}

	public void setServicioAmbulatorioPostular(String servicioAmbulatorioPostular) {
		this.servicioAmbulatorioPostular = servicioAmbulatorioPostular;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String centroAtencion) {
		this.nombreCentroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
	public int getContrato() {
		return contrato;
	}

	public void setContrato(int contrato) {
		this.contrato = contrato;
	}

	public int getConvenio() {
		return convenio;
	}

	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	public String getDatosCuentaReserva() {
		return datosCuentaReserva;
	}

	public void setDatosCuentaReserva(String datosCuentaReserva) {
		this.datosCuentaReserva = datosCuentaReserva;
	}

	public int getEstratoSocial() {
		return estratoSocial;
	}

	public void setEstratoSocial(int estratoSocial) {
		this.estratoSocial = estratoSocial;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public String getTipoRegimenConvenio() {
		return tipoRegimenConvenio;
	}

	public void setTipoRegimenConvenio(String tipoRegimenConvenio) {
		this.tipoRegimenConvenio = tipoRegimenConvenio;
	}
	

	public String getNombreClasificacionSocial() {
		return nombreClasificacionSocial;
	}

	public void setNombreClasificacionSocial(String clasificacionSocial) {
		this.nombreClasificacionSocial = clasificacionSocial;
	}

	public String getNombreContrato() {
		return nombreContrato;
	}

	public void setNombreContrato(String contrato) {
		this.nombreContrato = contrato;
	}

	public String getNombreConvenio() {
		return nombreConvenio;
	}

	public void setNombreConvenio(String convenio) {
		this.nombreConvenio = convenio;
	}

	
	public String getNombreTipoAfiliado() {
		return nombreTipoAfiliado;
	}

	public void setNombreTipoAfiliado(String tipoAfiliado) {
		this.nombreTipoAfiliado = tipoAfiliado;
	}

	public int getCodigoUsuarioCapitado() {
		return codigoUsuarioCapitado;
	}

	public void setCodigoUsuarioCapitado(int codigoUsuarioCapitado) {
		this.codigoUsuarioCapitado = codigoUsuarioCapitado;
	}

	public boolean isVerificadoUsuarioCapitado() {
		return verificadoUsuarioCapitado;
	}

	public void setVerificadoUsuarioCapitado(boolean verificadoUsuarioCapitado) {
		this.verificadoUsuarioCapitado = verificadoUsuarioCapitado;
	}

	public String getNombreConvenioCapitado() {
		return nombreConvenioCapitado;
	}

	public void setNombreConvenioCapitado(String nombreConvenioCapitado) {
		this.nombreConvenioCapitado = nombreConvenioCapitado;
	}
	
	

	/**
	 * @return the convenioCapitado
	 */
	public String getConvenioCapitado() {
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado the convenioCapitado to set
	 */
	public void setConvenioCapitado(String convenioCapitado) {
		this.convenioCapitado = convenioCapitado;
	}

	/**
	 * @return the soloDisponibles
	 */
	public boolean isSoloDisponibles() {
		return soloDisponibles;
	}

	/**
	 * @param soloDisponibles the soloDisponibles to set
	 */
	public void setSoloDisponibles(boolean soloDisponibles) {
		this.soloDisponibles = soloDisponibles;
	}

	/**
	 * @return the posCita
	 */
	public int getPosCita() {
		return posCita;
	}

	/**
	 * @param posCita the posCita to set
	 */
	public void setPosCita(int posCita) {
		this.posCita = posCita;
	}

	/**
	 * @return the mapaServicios
	 */
	public HashMap<String, Object> getMapaServicios() {
		return mapaServicios;
	}

	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(HashMap<String, Object> mapaServicios) {
		this.mapaServicios = mapaServicios;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaServicios
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaServicios 
	 */
	public void setMapaServicios(String key,Object obj) {
		this.mapaServicios.put(key,obj);
	}

	/**
	 * @return the posServicio
	 */
	public int getPosServicio() {
		return posServicio;
	}

	/**
	 * @param posServicio the posServicio to set
	 */
	public void setPosServicio(int posServicio) {
		this.posServicio = posServicio;
	}

	/**
	 * @return the condicionesToma
	 */
	public ArrayList<HashMap<String, Object>> getCondicionesToma() {
		return condicionesToma;
	}

	/**
	 * @param condicionesToma the condicionesToma to set
	 */
	public void setCondicionesToma(
			ArrayList<HashMap<String, Object>> condicionesToma) {
		this.condicionesToma = condicionesToma;
	}

	/**
	 * @return the nombreServicioTemporal
	 */
	public String getNombreServicioTemporal() {
		return nombreServicioTemporal;
	}

	/**
	 * @param nombreServicioTemporal the nombreServicioTemporal to set
	 */
	public void setNombreServicioTemporal(String nombreServicioTemporal) {
		this.nombreServicioTemporal = nombreServicioTemporal;
	}

	/**
	 * @return the nombreEstadoLiquidacionCita
	 */
	public String getNombreEstadoLiquidacionCita() {
		return nombreEstadoLiquidacionCita;
	}

	/**
	 * @param nombreEstadoLiquidacionCita the nombreEstadoLiquidacionCita to set
	 */
	public void setNombreEstadoLiquidacionCita(String nombreEstadoLiquidacionCita) {
		this.nombreEstadoLiquidacionCita = nombreEstadoLiquidacionCita;
	}

	/**
	 * @return the busquedaAvanzada
	 */
	public HashMap getBusquedaAvanzada() {
		return busquedaAvanzada;
	}

	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(HashMap busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}
	
	/**
	 * @return the busquedaAvanzada
	 */
	public Object getBusquedaAvanzada(String key) {
		return busquedaAvanzada.get(key);
	}

	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(String key,Object obj) {
		this.busquedaAvanzada.put(key,obj);
	}
	
	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}
	
	public Object getJustificacionNoPosMap(String key) {
		return justificacionNoPosMap.get(key);
	}

	public void setJustificacionNoPosMap(String key,Object value) {
		this.justificacionNoPosMap.put(key, value);
	}

	/**
	 * @return the fechaSolicitada
	 */
	public String getFechaSolicitada() {
		return fechaSolicitada;
	}

	/**
	 * @param fechaSolicitada the fechaSolicitada to set
	 */
	public void setFechaSolicitada(String fechaSolicitada) {
		this.fechaSolicitada = fechaSolicitada;
	}

	/**
	 * @return the centrosAtencion
	 */
	public String getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(String centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the unidadAgendaMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}
	
	/**
	 * @return the unidadAgendaMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(String llave, Object obj) {
		this.unidadAgendaMap.put(llave, obj);
	}

	/**
	 * @return the unidadesAgenda
	 */
	public String getUnidadesAgenda() {
		return unidadesAgenda;
	}

	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public int getPosServicioUnidad() {
		return posServicioUnidad;
	}

	public void setPosServicioUnidad(int posServicioUnidad) {
		this.posServicioUnidad = posServicioUnidad;
	}

	public int getTempNumServicios() {
		return tempNumServicios;
	}

	public void setTempNumServicios(int tempNumServicios) {
		this.tempNumServicios = tempNumServicios;
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

	/**
	 * @return the cierreIngresoPostOperatorio
	 */
	public boolean isCierreIngresoPostOperatorio() {
		return cierreIngresoPostOperatorio;
	}

	/**
	 * @param cierreIngresoPostOperatorio the cierreIngresoPostOperatorio to set
	 */
	public void setCierreIngresoPostOperatorio(boolean cierreIngresoPostOperatorio) {
		this.cierreIngresoPostOperatorio = cierreIngresoPostOperatorio;
	}

	/**
	 * @return the numeroCitas
	 */
	public int getNumeroCitas() {
		return numeroCitas;
	}

	/**
	 * @param numeroCitas the numeroCitas to set
	 */
	public void setNumeroCitas(int numeroCitas) {
		this.numeroCitas = numeroCitas;
	}

	/**
	 * @return the codigosCitas
	 */
	public int[] getCodigosCitas() {
		return codigosCitas;
	}

	/**
	 * @param codigosCitas the codigosCitas to set
	 */
	public void setCodigosCitas(int[] codigosCitas) {
		this.codigosCitas = codigosCitas;
	}

	/**
	 * @return the indiPO
	 */
	public String getIndiPO() {
		return indiPO;
	}

	/**
	 * @param indiPO the indiPO to set
	 */
	public void setIndiPO(String indiPO) {
		this.indiPO = indiPO;
	}

	/**
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public Object getCentrosAtencionAutorizados(String llave) {
		return centrosAtencionAutorizados.get(llave);
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(String llave, Object obj) {
		this.centrosAtencionAutorizados.put(llave, obj);
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public HashMap getUnidadesAgendaAutorizadas() {
		return unidadesAgendaAutorizadas;
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(HashMap unidadesAgendaAutorizadas) {
		this.unidadesAgendaAutorizadas = unidadesAgendaAutorizadas;
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public Object getUnidadesAgendaAutorizadas(String llave) {
		return unidadesAgendaAutorizadas.get(llave);
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(String llave, Object obj) {
		this.unidadesAgendaAutorizadas.put(llave, obj);
	}

	/**
	 * @return the motivoNoAtencion
	 */
	public String getMotivoNoAtencion() {
		return motivoNoAtencion;
	}

	/**
	 * @param motivoNoAtencion the motivoNoAtencion to set
	 */
	public void setMotivoNoAtencion(String motivoNoAtencion) {
		this.motivoNoAtencion = motivoNoAtencion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return the autorizacionIngEvento
	 */
	public InfoDatosString getAutorizacionIngEvento() {
		return autorizacionIngEvento;
	}

	/**
	 * @param autorizacionIngEvento the autorizacionIngEvento to set
	 */
	public void setAutorizacionIngEvento(InfoDatosString autorizacionIngEvento) {
		this.autorizacionIngEvento = autorizacionIngEvento;
	}

	/**
	 * @return the numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}

	/**
	 * @param numeroHistoriaClinica the numeroHistoriaClinica to set
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}

	/**
	 * @return the codigosCitasReprogramacion
	 */
	public String getCodigosCitasReprogramacion() {
		return codigosCitasReprogramacion;
	}

	/**
	 * @param codigosCitasReprogramacion the codigosCitasReprogramacion to set
	 */
	public void setCodigosCitasReprogramacion(String codigosCitasReprogramacion) {
		this.codigosCitasReprogramacion = codigosCitasReprogramacion;
	}	

	/**
	 * @return the arrayListUtilitario_2
	 */
	public ArrayList getArrayListUtilitario_2() {
		return arrayListUtilitario_2;
	}
	
	public int getSizeArrayListUtilitario_2(){
		return arrayListUtilitario_2.size();
	}

	/**
	 * @param arrayListUtilitario_2 the arrayListUtilitario_2 to set
	 */
	public void setArrayListUtilitario_2(ArrayList arrayListUtilitario_2) {
		this.arrayListUtilitario_2 = arrayListUtilitario_2;
	}

	/**
	 * @return the esUsuarioCapitado
	 */
	public String getEsUsuarioCapitado() {
		return esUsuarioCapitado;
	}

	/**
	 * @param esUsuarioCapitado the esUsuarioCapitado to set
	 */
	public void setEsUsuarioCapitado(String esUsuarioCapitado) {
		this.esUsuarioCapitado = esUsuarioCapitado;
	}

	/**
	 * @return the codigoConsultorioUsua
	 */
	public String getCodigoConsultorioUsua() {
		return codigoConsultorioUsua;
	}

	/**
	 * @param codigoConsultorioUsua the codigoConsultorioUsua to set
	 */
	public void setCodigoConsultorioUsua(String codigoConsultorioUsua) {
		this.codigoConsultorioUsua = codigoConsultorioUsua;
	}

	/**
	 * @return the registroMedico
	 */
	public String getRegistroMedico() {
		return registroMedico;
	}

	/**
	 * @param registroMedico the registroMedico to set
	 */
	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	/**
	 * @return the loginUsuCita
	 */
	public String getLoginUsuCita() {
		return loginUsuCita;
	}

	/**
	 * @param loginUsuCita the loginUsuCita to set
	 */
	public void setLoginUsuCita(String loginUsuCita) {
		this.loginUsuCita = loginUsuCita;
	}

	public int getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	public void setCodigoDetalleCargo(int codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	public int getCodigoConvenioCargo() {
		return codigoConvenioCargo;
	}

	public void setCodigoConvenioCargo(int codigoConvenioCargo) {
		this.codigoConvenioCargo = codigoConvenioCargo;
	}

	public int getCodigoSubCuentaCargo() {
		return codigoSubCuentaCargo;
	}

	public void setCodigoSubCuentaCargo(int codigoSubCuentaCargo) {
		this.codigoSubCuentaCargo = codigoSubCuentaCargo;
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
	 * @return the bloqueoUsuario
	 */
	public String getBloqueoUsuario() {
		return bloqueoUsuario;
	}

	/**
	 * @param bloqueoUsuario the bloqueoUsuario to set
	 */
	public void setBloqueoUsuario(String bloqueoUsuario) {
		this.bloqueoUsuario = bloqueoUsuario;
	}

	/**
	 * @return the respAutorizacionUsu
	 */
	public String getRespAutorizacionUsu() {
		return respAutorizacionUsu;
	}

	/**
	 * @param respAutorizacionUsu the respAutorizacionUsu to set
	 */
	public void setRespAutorizacionUsu(String respAutorizacionUsu) {
		this.respAutorizacionUsu = respAutorizacionUsu;
	}

	/**
	 * @return the lisMensaje
	 */
	public ArrayList<ResultadoBoolean> getLisMensaje() {
		return lisMensaje;
	}

	/**
	 * @param lisMensaje the lisMensaje to set
	 */
	public void setLisMensaje(ArrayList<ResultadoBoolean> lisMensaje) {
		this.lisMensaje = lisMensaje;
	}

	/**
	 * @return the mantenerPacienteCargado
	 */
	public boolean isMantenerPacienteCargado() {
		return mantenerPacienteCargado;
	}

	/**
	 * @param mantenerPacienteCargado the mantenerPacienteCargado to set
	 */
	public void setMantenerPacienteCargado(boolean mantenerPacienteCargado) {
		this.mantenerPacienteCargado = mantenerPacienteCargado;
	}

	/**
	 * @return the cargarInfoPacienteSesion
	 */
	public boolean isCargarInfoPacienteSesion() {
		return cargarInfoPacienteSesion;
	}

	/**
	 * @param cargarInfoPacienteSesion the cargarInfoPacienteSesion to set
	 */
	public void setCargarInfoPacienteSesion(boolean cargarInfoPacienteSesion) {
		this.cargarInfoPacienteSesion = cargarInfoPacienteSesion;
	}

	/**
	 * @return the filtrarAgendasXServicio
	 */
	public boolean isFiltrarAgendasXServicio() {
		return filtrarAgendasXServicio;
	}

	/**
	 * @param filtrarAgendasXServicio the filtrarAgendasXServicio to set
	 */
	public void setFiltrarAgendasXServicio(boolean filtrarAgendasXServicio) {
		this.filtrarAgendasXServicio = filtrarAgendasXServicio;
	}

	public String getOrigenTelefono() {
		return origenTelefono;
	}

	public void setOrigenTelefono(String origenTelefono) {
		this.origenTelefono = origenTelefono;
	}

	public int getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	public void setNaturalezaPaciente(int naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	public void setListaSolicitudesServiciosAutorizar(
			ArrayList<DTOAdministrarSolicitudesAutorizar> listaSolicitudesServiciosAutorizar) {
		this.listaSolicitudesServiciosAutorizar = listaSolicitudesServiciosAutorizar;
	}

	public ArrayList<DTOAdministrarSolicitudesAutorizar> getListaSolicitudesServiciosAutorizar() {
		return listaSolicitudesServiciosAutorizar;
	}

	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}

	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}

	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}

	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}
	

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getOtrosTelefonos() {
		return otrosTelefonos;
	}

	public void setOtrosTelefonos(String otrosTelefonos) {
		this.otrosTelefonos = otrosTelefonos;
	}

	public void setListaServiciosImpimirOrden(
			ArrayList<String> listaServiciosImpimirOrden) {
		this.listaServiciosImpimirOrden = listaServiciosImpimirOrden;
	}

	public ArrayList<String> getListaServiciosImpimirOrden() {
		return listaServiciosImpimirOrden;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	public void setCentroCostoAmbulatorioSel(String centroCostoAmbulatorioSel) {
		this.centroCostoAmbulatorioSel = centroCostoAmbulatorioSel;
	}

	public String getCentroCostoAmbulatorioSel() {
		return centroCostoAmbulatorioSel;
	}

	/**
	 * @return Retorna fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica Asigna el atributo fechaModifica
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return Retorna horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica Asigna el atributo horaModifica
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return Retorna nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param nombreUsuario Asigna el atributo nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public void setNombreNaturaleza(String nombreNaturaleza) {
		this.nombreNaturaleza = nombreNaturaleza;
	}

	public String getNombreNaturaleza() {
		return nombreNaturaleza;
	}

	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	/**
	 * @return infoResponsableCobertura
	 */
	public InfoResponsableCobertura getInfoResponsableCobertura() {
		return infoResponsableCobertura;
	}

	/**
	 * @param infoResponsableCobertura
	 */
	public void setInfoResponsableCobertura(InfoResponsableCobertura infoResponsableCobertura) {
		this.infoResponsableCobertura = infoResponsableCobertura;
	}

	public boolean isErrorGeneracionAutorizacion() {
		return errorGeneracionAutorizacion;
	}

	public void setErrorGeneracionAutorizacion(boolean errorGeneracionAutorizacion) {
		this.errorGeneracionAutorizacion = errorGeneracionAutorizacion;
	}

	/**
	 * @return the fechaInicioDate
	 */
	public Date getFechaInicioDate() {
		return fechaInicioDate;
	}

	/**
	 * @param fechaInicioDate the fechaInicioDate to set
	 */
	public void setFechaInicioDate(Date fechaInicioDate) {
		this.fechaInicioDate = fechaInicioDate;
	}

	/**
	 * @return the listando
	 */
	public boolean isListando() {
		return listando;
	}

	/**
	 * @param listando the listando to set
	 */
	public void setListando(boolean listando) {
		this.listando = listando;
	}

	/**
	 * @return the estadoant
	 */
	public String getEstadoant() {
		return estadoant;
	}

	/**
	 * @param estadoant the estadoant to set
	 */
	public void setEstadoant(String estadoant) {
		this.estadoant = estadoant;
	}


}