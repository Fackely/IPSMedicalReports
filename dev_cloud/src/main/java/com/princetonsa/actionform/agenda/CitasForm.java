/*
 * @(#)ConstantesBD.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package com.princetonsa.actionform.agenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;

/**
 * Form para manejo de citas
 *
 * @version 1.0, Feb 11, 2004
 * @author <a href="mailto:Raul@PrincetonSA.com">Raúl Cancino</a>
 */

public class CitasForm extends ActionForm 
{
	/**
	 * ultima propiedad por la que se busco
	 */
	private String ultimaPropiedad;
	/**
	 * orden en que se desea ordenar el listado, ascendente o descendente
	 */
	private String orden;
	/**
	 * columna por la cual se quiere ordenar el listado
	 */
	private String columna;
	/**
	 * arreglo para manejar la busqueda multiple de estados de cita
	 */
	private String estadosCita[];
	/**
	 * codigo del consultorio
	 */
	private int codigoConsultorio;
	/**
	 * numero de cuenta del paciente
	 */
	private int cuenta;
	/**
	 * variable que indica busqueda por paciente
	 */
	private boolean busquedaPaciente;
	/**
	 * variable que indica busqueda general
	 */
	private boolean busquedaGeneral;

	/**
	 * hora inicio de la busqueda
	 */
	private String codigoEstadoLiquidacion;
	
	/**
	 * 
	 */
	private String codigoTipoCita;
	/**
	 * hora inicio de la busqueda
	 */
	private int codigoUnidadConsulta;	
	/**
	 * hora inicio de la busqueda
	 */
	private String horaFin;	
	/**
	 * hora inicio de la busqueda
	 */
	private String horaInicio;	
	/**
	 * fecha fin de la busqueda
	 */
	private String fechaFin;
	/**
	 * fecha de inicio de la busqueda
	 */
	private String fechaInicio;
	/**
	 * @author rcancino
	 *  indica  el numero de resultados de la busqueda 
	 * */
	private int codigoCentroCosto;
	
	private int codigoAgenda;
	
	/**
	 * Código de la especialdiad 
	 */
	private int codigoEspecialidad;
	
	/**
	 * Mapa con informacion de justificacion nopos para un servicio
	 */
	private HashMap justificacionesServicios = new HashMap();
	
	//atributos usados para la atencion de la cita----------------
	/**
	 * Código de la cita
	 */
	private String codigoCita;
	
	private boolean tieneMedico;
	
	/**
	 * Código del estado de la liquidacion de la cita
	 */
	private String estadoLiquidacionCita;
	
	/**
	 * Arreglo de Citas incumplidas con Multas
	 */
	private ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	
	/**
	 * Link para redireccionar pagina... en el page de citas incumplidas
	 */
	private String linkSiguiente;
	
	//------------------------------------------------------------
	
	/**
	 * @author rcancino
	 *  indica  el numero de resultados de la busqueda 
	 * */
	private int numeroResultados;
	
	/**
	 * @author rcancino
	 *  indica    si la solicitud de interconsulta tiene una respuesta
	 * pediatrica 
	 * */	
	private boolean pediatrica;
	
	/**
	 * 
	 */
	private String centroAtencion;
	
	/**
	 * @author rcancino
	 *  codigo correspondiente al paciente del que se listan las solicitudes
	 */	
	private int codigoPaciente;
	
	/**
	 * @author rcancino
	 * codigo correspondiente al medico que lista las solicitudes
	 */
	private int codigoMedico;
	
	/**
	 * Còdigo médico anterior, se utiliza en la búsqueda de citas por fecha para el
	 * volver
	 */
	private int codigoMedicoAnterior;
	
	private ArrayList listaCitas;

	/** @author rcancino
		* numero de la solicitud
		*/
	private int numeroSolicitud;
	
	
	/**
	 * encabezado parametrizable de la lista
	 * @return
	 */
	private Vector encabezado;


	/**
	 * Definición Atributos (Liliana)
	 * @version 1.0, Marzo 29 / 2004
	 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
	 */

	/**
	 * Estado actual del flujo
	 */
	private String estado;
	
	/**
	 * Número de citas existentes
	 */
	//private int numCitas;	

	/**
	 * Fecha sobre la cual se están consultando las citas
	 */
	private String fechaConsulta;
	
	/**
	 * Fecha de busqueda de las citas
	 */
	private String fechaBusqueda;
	
	/**
	 * Hora de la consulta seleccionada
	 */
	private String horaConsulta;
	
	/**
	 * Formato de respuesta de la cita
	 */
	private String formatoRespuesta;
	
	/**
	 * Nombre de la unidad de consulta seleccionada
	 */
	private String nombreUnidadConsulta;

	/**
	 * Código del estado de la cita seleccionada
	 */
	private int codigoEstadoCita;
	
	/**
	 * Si es la primera vez que entra a la funcionalidad y no viene de ninguna busqueda interna
	 */
	private boolean primeraVez;
	/*
	 *Variable que permite saber en donde se está haciendo la ordenación del listado de citas
	 *	0 => se hace en consulta de citas
	 *	1 => se hace en resumen de atenciones 
	 */
	private String indicador;
	
	/**
	 * Variable para indicar si ya entro a la busqueda general de fechas
	 */
	private boolean yaEntroABusquedaXFecha;
	
	
	/**
	 * Mapa para manejar el flujo de la seleccion y anulacion de los servicios
	 * de cada cita
	 */
	private HashMap mapaCitas = new HashMap();
	
	
	/**
	 * Variable para almacenar el codigo y nombre del servicio seleccionado
	 */
	private int codigoServicio;
	private String nombreServicio;
	private String codigoTipoServicio ;
	private String estadoServicio;
	
	private ArrayList<HashMap<String, Object>> condicionesToma = new ArrayList<HashMap<String,Object>>();

	/**
	 * Variables para seleccionar el servicio de la cita
	 */
	private int posCita;
	private int posServicio;
	
	//****************ATRIBUTOS PARA EL FLUJO DE OTROS SERVICIOS**************************
	private HashMap<String, Object> otrosServicios = new HashMap<String, Object>();
	private ArrayList<HashMap<String, Object>> centrosCosto = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Indicador de la cita para ver el detalle 
	 * */
	private String indexCita;
	
	
	/**
	 * Indica la posicion del servio asociado a la cita
	 * */
	private String indexServicio;
	
	/**
	 * HashMap de Servicios de la cita
	 * */
	private HashMap serviciosCita;
	
	/**
	 * Mensaje Proceso Realizado
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);

	
	private String checkCitasControlPO;
	
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
	 * */
	private InfoDatosString  infoCvsArchivo; 
	
	/**
	 * 
	 * */
	private String telefono ;
	
	
	//**************ATRIBUTOS PARA EL RECORD DE CITAS*****************************************************************
	/**
	 * Atributo para identificar si la consulta de citas viene del flujo normalde la consulta
	 * de citas o viene del record de citas llamado desde la reserva de citas
	 */
	private boolean recordCitas;
	//Información del paciente
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	//********************************************************************************************
	
	/*
	 * ATRIBUTOS CONSULTAR CITAS ODONTOLOGICO
	 * atributo para determinar el tipo de agenda por la cual se realizaran las operaciones
	 */
	private String tipoAgendaSeleccionada;
	private ArrayList<DtoCitaOdontologica> listCitas;
	private DtoPlanTratamientoOdo detCitasOdoPlanTrata;
	private String entidadManejaProgramas;
	private ArrayList<DtoProgramasServiciosPlanT> listDetalleCita;
	private String programaCita;
	
	private int institucion;
	
	/*
	 * Atributos para ordenar odonto
	 */
	private String patronOrdenar;
	private String esDescendente;
	
	private Date ayudanteFechaInicio;
	private Date ayudanteFechaFin;
	
	
	/**
	 * Fin Definición Atributos (Liliana)
	 * @version 1.0, Marzo 29 / 2004
	 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
	 */
	
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
		
		
		if( estado != null && estado.equals("listarCitasPorMedico") ) 
		{
			if( UtilidadCadena.noEsVacio(fechaBusqueda) && !fechaBusqueda.equals(UtilidadFecha.getFechaActual()) )
			{
				// Fecha actual y patrón de fecha a utilizar en las validaciones
				final Date fechaActualD = new Date();
				final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		
				// Valida que la fecha de ejecución tenga el formato de fecha establecido
				Date fechaListadoD = null;
				try 
				{
					fechaListadoD = dateFormatter.parse(this.fechaBusqueda);
					// Valida que la fecha de ejecución no sea mayor que la fecha actual
					if ( fechaActualD.compareTo(fechaListadoD) < 0 ) 
					{
						this.setListaCitas(new ArrayList());
						errores.add("fechaListado", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de consulta", "actual"));
					}							
				}	
				catch (java.text.ParseException e) 
				{
					this.setListaCitas(new ArrayList());
					errores.add("fechaListado", new ActionMessage("errors.formatoFechaInvalido", "de consulta"));
				}
			}						
		}	
		
		else if(estado!=null && (estado.equals("busquedaGeneral") || estado.equals("busquedaPaciente")))
		{
			/* Validar esto sí y solo si no se han encontrado errores */
			Date				fechaInicioD=new Date();
			Date				fechaFinD=new Date();
			SimpleDateFormat	sdfFecha;
			
			if (estado.equals("busquedaGeneral")){
				if(this.tipoAgendaSeleccionada.equals(ConstantesBD.codigoNuncaValido+"")){
					errores.add("tipoAgendaSeleccionada", new ActionMessage("errors.required", "El Tipo de Agenda"));
				}
			}
			
			//Si el estado es búsqueda paciente, las fechas NO deben
			//ser requeridas, para no cambiar la lógica anterior, si alguna
			// de ellas es vacía le vamos a dar un valor por defecto muy
			//pequeño (o grande, dependiendo el caso). Definido en 
			//ConstantesBD
			if (this.fechaInicio==null||this.fechaInicio.equals(""))
			{
				//fechaInicio=ConstantesBD.fechaMinimaBusquedaFormatoAp;
			
				//this.fechaInicio = UtilidadFecha.incrementarMesesAFecha(UtilidadFecha.getFechaActual(), -3, false) + "";
				//Utilidades que retornan la fecha actual menos 3 meses
				//UtilidadFecha.incrementarMesesAFecha(UtilidadFecha.getFechaActual(), -3, false)
				//UtilidadFecha.calcularFechaSobreFechaReferencia(90, UtilidadFecha.getFechaActual(), false)				
				errores.add("fechaInicio", new ActionMessage("errors.required", "Fecha Inicial"));
			}
			if (this.fechaFin==null||this.fechaFin.equals(""))
			{
				//fechaFin=ConstantesBD.fechaMaximaBusquedaFormatoAp;
				//this.fechaFin = UtilidadFecha.getFechaActual() + "";
				errores.add("fechaFin", new ActionMessage("errors.required", "Fecha Final"));
			}
			
			//las modificaciones anteriores se hicieron por cambio de la tarea 71083 la cual pedia cambiar las 
			//fechas que se postulaban en el sistema se desactiva el nuevo postulado por que solo piden mostrar
			//mensaje de error
			
			
		
			/* Iniciar variables. El formato de fecha que se espera es dd/MM/yyyy */
			sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
			/* Exije una interpretación estricta del formato de fecha esperado */
			sdfFecha.setLenient(false);
			
			try
			{
				/* Obtener la fecha de inicio */
				if(fechaInicio.equals(""))
				{
					
					fechaInicio = null;
				}
				else if(!UtilidadFecha.validarFecha(fechaInicio))
					errores.add("", new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else
				{
					fechaInicioD = sdfFecha.parse(this.fechaInicio);
				}
			}
			catch(ParseException lpe_fechaInicio)
			{
				errores.add(
					"fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Inicial")
				);
				fechaInicio = null;
			}
			try
			{
				/* Obtener la fecha de finalización */
				if(fechaFin.equals(""))
				{
					
					fechaFin = null;
				}
				else if(!UtilidadFecha.validarFecha(fechaFin))
					errores.add("", new ActionMessage("errors.formatoFechaInvalido","final"));
				else
				{
					fechaFinD = sdfFecha.parse(this.fechaFin);
				}
			}
			catch(ParseException lpe_fechaFin)
			{
				errores.add(
					"fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Final")
				);
				fechaFin = null;
			}
				/* La fecha de inicio debe ser anterior a la fecha de finalización */
			if(fechaFinD.before(fechaInicioD) ){
				errores.add("fechaFin",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Fecha Final","Fecha Inicial"));
			}
			
			//-----Se valida que no se genere una agenda para un rango de fechas mayor de tres meses-----//
			if (errores.isEmpty())
				{
				int nroMeses = UtilidadFecha.numeroMesesEntreFechas(fechaInicio, fechaFin,true);
				if (nroMeses > 3)
					{
					errores.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "CONSULTAR CITAS"));
					}
				}
			
			/* Validar esto sí y solo si no se han encontrado errores */
			if(!horaInicio.equals("") && !horaFin.equals("") )
			{
				Date				horaInicioD;
				Date				horaFinD;
				SimpleDateFormat	sdfHora;

				/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
				sdfHora = new SimpleDateFormat("H:mm");

				/* Exije una interpretación estricta del formato de hora esperado */
				sdfHora.setLenient(false);

				try
				{
					/* Obtener la hora de inicio */
					horaInicioD = sdfHora.parse(horaInicio);
				}
				catch(ParseException lpe_horaInicio)
				{
					errores.add("horaInicio",new ActionMessage("errors.formatoHoraInvalido", "de inicio"));
					horaInicioD = null;
				}

				try
				{
					/* Obtener la hora de finalización */
					horaFinD = sdfHora.parse(horaFin);
				}
				catch(ParseException lpe_horaFin)
				{
					errores.add(
						"horaFin",
						new ActionMessage("errors.formatoHoraInvalido", "de finalización")
					);
					horaFinD = null;
				}

				/* La hora de inicio debe ser anterior a la hora de finalización */
				if(horaFinD != null &&horaInicioD != null &&horaFinD.before(horaInicioD)){
					errores.add("horaFin",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "final", "inicial"));
				}
			}
							

		}
		else if(estado!=null && estado.equals("cambiarEstadoCita"))
		{
			if(Integer.parseInt(this.getMapaCitas("codigoEstadoCita_"+this.posCita).toString())==(ConstantesBD.codigoEstadoCitaNoAtencion))
			{
				String motivoNoAtencion=this.getMapaCitas().containsKey("motivoNoAtencion_"+this.getPosCita())?this.getMapaCitas("motivoNoAtencion_"+this.getPosCita())+"":"";
				if(UtilidadTexto.isEmpty(motivoNoAtencion))
				{
					errores.add("motivo no atencion",  new ActionMessage("errors.required","El Motivo No Atencion para la cita de "+this.getMapaCitas("nombreUnidadAgenda_"+this.posCita)+" a las "+this.getMapaCitas("horaInicio_"+this.posCita)));
				}
			}
		}
			
		return errores;
	}	

	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{		
		this.checkCitasControlPO=ConstantesBD.acronimoNo+"";
		this.ultimaPropiedad="";
		this.columna="";
		this.orden="ASC";
		this.estadosCita=new String[]{""};
		this.codigoConsultorio=-1;
		this.codigoMedico=-1;
		this.codigoMedicoAnterior=-1;
		this.codigoPaciente=-1;
		this.encabezado=new Vector();
		this.numeroSolicitud=-1;
		this.pediatrica=false;
		this.codigoCentroCosto=-1;
		this.codigoAgenda=0;
		this.codigoCita = "";
		this.estadoLiquidacionCita = "";
	
		this.busquedaGeneral=false;
		this.busquedaPaciente=false;
		this.codigoEstadoLiquidacion="";
		this.codigoTipoCita = "";
		this.codigoUnidadConsulta=0;
		this.fechaInicio="";
		this.fechaFin="";
		this.horaInicio="";
		this.horaFin="";
		// Liliana
		//this.numCitas = 0;
		this.formatoRespuesta = "";
		this.codigoEstadoCita = -1;
		this.horaConsulta = "";
		this.primeraVez = false;
		this.fechaBusqueda = "";
		// Fin Liliana
		this.indicador="";
		this.centroAtencion=ConstantesBD.codigoNuncaValido+"";
		this.yaEntroABusquedaXFecha=false;
		
		this.mapaCitas = new HashMap();
		
		this.codigoServicio = 0;
		this.nombreServicio = "";
		this.codigoTipoServicio = "";
		this.estadoServicio = "";
		
		this.condicionesToma = new ArrayList<HashMap<String,Object>>();
		
		this.posCita = 0;
		this.posServicio = 0;
		
		this.otrosServicios = new HashMap<String, Object>();
		this.centrosCosto = new ArrayList<HashMap<String,Object>>();
		
		this.codigoEspecialidad = 0;
		
		this.justificacionesServicios = new HashMap();
		
		this.tipoIdentificacion = "";
		this.numeroIdentificacion = "";
		
		this.codigoConsultorioUsua = "";		
		this.registroMedico = "";		
		this.loginUsuCita = "";	
		this.telefono = "";
		
		this.infoCvsArchivo = new InfoDatosString();
		this.linkSiguiente="";
		
		this.tipoAgendaSeleccionada = "";
		this.listCitas = new ArrayList<DtoCitaOdontologica>();
		this.detCitasOdoPlanTrata = new DtoPlanTratamientoOdo();
		this.entidadManejaProgramas = "";
		this.listDetalleCita = new ArrayList<DtoProgramasServiciosPlanT>();
		this.programaCita = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		
//		 * Atributos para ordenar
		this.patronOrdenar = "";
		this.esDescendente = "";
	}
	
	
	/**
	 * @return the recordCitas
	 */
	public boolean isRecordCitas() {
		return recordCitas;
	}

	/**
	 * @param recordCitas the recordCitas to set
	 */
	public void setRecordCitas(boolean recordCitas) {
		this.recordCitas = recordCitas;
	}

	public void resetEstado()
	{
		this.estado="";
	}

	/**
	 * @return
	 */
	public int getCodigoMedico() {
		return codigoMedico;
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
	 * @return
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
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
	 * Definición Métodos (Liliana)
	 * @version 1.0, Marzo 29 / 2004
	 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
	 */	
	/**
	 * Retorna el estado actual del flujo
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual del flujo
	 * @param string
	 */
	public void setEstado(String string)
	{
		estado = string;
	}

	/**
	 * Retorna el número de citas existentes
	 * @return
	 */
	public int getNumCitas()
	{
		//return this.listaCitas.size();
		if(this.mapaCitas.get("numRegistros")!=null)
			return Integer.parseInt(this.getMapaCitas("numRegistros").toString());
		else 
			return 0;
	}
	
	/**
	 * Retorna la fecha sobre la cual se están consultando las citas
	 * @return
	 */
	public String getFechaConsulta()
	{
		return fechaConsulta;
	}

	/**
	 * Asigna la fecha sobre la cual se están consultando las citas
	 * @param string
	 */
	public void setFechaConsulta(String string)
	{
		fechaConsulta = string;
	}
	
	/**
	 * Retorna la fecha de busqueda de las citas
	 * @return
	 */
	public String getFechaBusqueda()
	{
		return fechaBusqueda;
	}

	/**
	 * Asigna la fecha de busqueda de las citas
	 * @param string
	 */
	public void setFechaBusqueda(String string)
	{
		fechaBusqueda = string;
	}
	
	/**
	 * Adiciona una cita a la lista
	 * @param cita
	 */
	public void addCita(CitaForm cita)
	{
		this.listaCitas.add(cita);
	}
	
	/**
	 * Retorna el formato de respuesta de la cita
	 * @return
	 */
	public String getFormatoRespuesta()
	{
		return formatoRespuesta;
	}

	/**
	 * Asigna el formato de respuesta de la cita
	 * @param i
	 */
	public void setFormatoRespuesta(String i)
	{
		formatoRespuesta = i;
	}	

	/**
	 * Retorna el nombre de la unidad de consulta seleccionada
	 * @return
	 */
	public String getNombreUnidadConsulta()
	{
		return nombreUnidadConsulta;
	}

	/**
	 * Asigna el nombre de la unidad de consulta seleccionada
	 * @param string
	 */
	public void setNombreUnidadConsulta(String string)
	{
		nombreUnidadConsulta = string;
	}

	/**
	 * Retorna el código del estado de la cita seleccionada
	 * @return
	 */
	public int getCodigoEstadoCita()
	{
		return codigoEstadoCita;
	}

	/**
	 * Asigna el código del estado de la cita seleccionada
	 * @param i
	 */
	public void setCodigoEstadoCita(int i)
	{
		codigoEstadoCita = i;
	}
	
	/**
	 * Retorna la hora de la consulta seleccionada
	 * @return
	 */
	public String getHoraConsulta()
	{
		return horaConsulta;
	}

	/**
	 * Asigna la hora de la consulta seleccionada
	 * @param string
	 */
	public void setHoraConsulta(String string)
	{
		horaConsulta = string;
	}	

	/**
	 * Dice si es la primera vez que entra a la funcionalidad y no viene de ninguna busqueda interna
	 * @return
	 */
	public boolean isPrimeraVez()
	{
		return primeraVez;
	}

	/**
	 * Asigna si es la primera vez que entra a la funcionalidad y no viene de ninguna busqueda interna
	 * @param string
	 */
	public void setPrimeraVez(boolean primeraVez)
	{
		this.primeraVez = primeraVez;
	}	
	/**
	 * Fin Definición Métodos (Liliana)
	 * @version 1.0, Marzo 29 / 2004
	 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
	 */

	/**
	 * @return
	 */
	public ArrayList getListaCitas() {
		return listaCitas;
	}

	/**
	 * @param list
	 */
	public void setListaCitas(ArrayList list) {
		listaCitas = list;
	}

	/**
	 * Returns the fechaInicio.
	 * @return String
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Sets the fechaInicio.
	 * @param fechaInicio The fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Returns the fechaFin.
	 * @return String
	 */
	public String getFechaFin() {
		return fechaFin;
	}

	/**
	 * Sets the fechaFin.
	 * @param fechaFin The fechaFin to set
	 */
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Returns the codigoEstadoLiquidacion.
	 * @return int
	 */
	public String getCodigoEstadoLiquidacion() {
		return codigoEstadoLiquidacion;
	}

	/**
	 * Returns the codigoUnidadConsulta.
	 * @return int
	 */
	public int getCodigoUnidadConsulta() {
		return codigoUnidadConsulta;
	}

	/**
	 * Returns the horaFin.
	 * @return String
	 */
	public String getHoraFin() {
		return horaFin;
	}

	/**
	 * Returns the horaInicio.
	 * @return String
	 */
	public String getHoraInicio() {
		return horaInicio;
	}

	/**
	 * Sets the codigoEstadoLiquidacion.
	 * @param codigoEstadoLiquidacion The codigoEstadoLiquidacion to set
	 */
	public void setCodigoEstadoLiquidacion(String codigoEstadoLiquidacion) {
		this.codigoEstadoLiquidacion = codigoEstadoLiquidacion;
	}

	/**
	 * Sets the codigoUnidadConsulta.
	 * @param codigoUnidadConsulta The codigoUnidadConsulta to set
	 */
	public void setCodigoUnidadConsulta(int codigoUnidadConsulta) {
		this.codigoUnidadConsulta = codigoUnidadConsulta;
	}

	/**
	 * Sets the horaFin.
	 * @param horaFin The horaFin to set
	 */
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * Sets the horaInicio.
	 * @param horaInicio The horaInicio to set
	 */
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Sets the numCitas.
	 * @param numCitas The numCitas to set
	 */
	/*public void setNumCitas(int numCitas) {
		this.numCitas = numCitas;
	}*/

	/**
	 * Returns the busquedaGeneral.
	 * @return boolean
	 */
	public boolean isBusquedaGeneral() {
		return busquedaGeneral;
	}

	/**
	 * Returns the busquedaPaciente.
	 * @return boolean
	 */
	public boolean isBusquedaPaciente() {
		return busquedaPaciente;
	}

	/**
	 * Sets the busquedaGeneral.
	 * @param busquedaGeneral The busquedaGeneral to set
	 */
	public void setBusquedaGeneral(boolean busquedaGeneral) {
		this.busquedaGeneral = busquedaGeneral;
	}

	/**
	 * Sets the busquedaPaciente.
	 * @param busquedaPaciente The busquedaPaciente to set
	 */
	public void setBusquedaPaciente(boolean busquedaPaciente) {
		this.busquedaPaciente = busquedaPaciente;
	}

	/**
	 * Returns the cuenta.
	 * @return int
	 */
	public int getCuenta() {
		return cuenta;
	}

	/**
	 * Sets the cuenta.
	 * @param cuenta The cuenta to set
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * Returns the codigoConsultorio.
	 * @return int
	 */
	public int getCodigoConsultorio() {
		return codigoConsultorio;
	}

	/**
	 * Sets the codigoConsultorio.
	 * @param codigoConsultorio The codigoConsultorio to set
	 */
	public void setCodigoConsultorio(int codigoConsultorio) {
		this.codigoConsultorio = codigoConsultorio;
	}

	/**
	 * Returns the estadosCita.
	 * @return String[]
	 */
	public String[] getEstadosCita() {
		return estadosCita;
	}

	/**
	 * Sets the estadosCita.
	 * @param estadosCita The estadosCita to set
	 */
	public void setEstadosCita(String[] estadosCita) {
		this.estadosCita = estadosCita;
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
	 * Returns the orden.
	 * @return String
	 */
	public String getOrden()
	{
		return orden;
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
	 * Sets the orden.
	 * @param orden The orden to set
	 */
	public void setOrden(String orden)
	{
		this.orden = orden;
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
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return Retorna codigoAgenda.
	 */
	public int getCodigoAgenda()
	{
		return codigoAgenda;
	}

	/**
	 * @param codigoAgenda Asigna codigoAgenda.
	 */
	public void setCodigoAgenda(int codigoAgenda)
	{
		this.codigoAgenda = codigoAgenda;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public int getCodigoMedicoAnterior() {
		return codigoMedicoAnterior;
	}

	public void setCodigoMedicoAnterior(int codigoMedicoAnterior) {
		this.codigoMedicoAnterior = codigoMedicoAnterior;
	}

	public boolean isYaEntroABusquedaXFecha() {
		return yaEntroABusquedaXFecha;
	}

	public void setYaEntroABusquedaXFecha(boolean yaEntroABusquedaXFecha) {
		this.yaEntroABusquedaXFecha = yaEntroABusquedaXFecha;
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
	 * @return the estadoLiquidacionCita
	 */
	public String getEstadoLiquidacionCita() {
		return estadoLiquidacionCita;
	}

	/**
	 * @param estadoLiquidacionCita the estadoLiquidacionCita to set
	 */
	public void setEstadoLiquidacionCita(String estadoLiquidacionCita) {
		this.estadoLiquidacionCita = estadoLiquidacionCita;
	}

	/**
	 * @return the mapaCitas
	 */
	public HashMap getMapaCitas() {
		return mapaCitas;
	}

	/**
	 * @param mapaCitas the mapaCitas to set
	 */
	public void setMapaCitas(HashMap mapaCitas) {
		this.mapaCitas = mapaCitas;
	}


	/**
	 * @return the mapaCitas
	 */
	public Object getMapaCitas(String key) {
		return mapaCitas.get(key);
	}

	/**
	 * @param mapaCitas the mapaCitas to set
	 */
	public void setMapaCitas(String key,Object obj) {
		this.mapaCitas.put(key,obj);
	}

	

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the nombreServicio
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * @param nombreServicio the nombreServicio to set
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
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
	 * @return the codigoTipoServicio
	 */
	public String getCodigoTipoServicio() {
		return codigoTipoServicio;
	}

	/**
	 * @param codigoTipoServicio the codigoTipoServicio to set
	 */
	public void setCodigoTipoServicio(String codigoTipoServicio) {
		this.codigoTipoServicio = codigoTipoServicio;
	}

	/**
	 * @return the estadoServicio
	 */
	public String getEstadoServicio() {
		return estadoServicio;
	}

	/**
	 * @param estadoServicio the estadoServicio to set
	 */
	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}

	/**
	 * @return the otrosServicios
	 */
	public HashMap<String, Object> getOtrosServicios() {
		return otrosServicios;
	}

	/**
	 * @param otrosServicios the otrosServicios to set
	 */
	public void setOtrosServicios(HashMap<String, Object> otrosServicios) {
		this.otrosServicios = otrosServicios;
	}
	
	/**
	 * @return Retornar elemento del mapa otrosServicios
	 */
	public Object getOtrosServicios(String key) {
		return otrosServicios.get(key);
	}

	/**
	 * @param Asigna elemento al mapa otrosServicios 
	 */
	public void setOtrosServicios(String key,Object obj) {
		this.otrosServicios.put(key, obj);
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
	 * Método para obtener el tamaño del centro de costo
	 * @return
	 */
	public int getNumCentrosCosto()
	{
		return this.centrosCosto.size();
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the indexCita
	 */
	public String getIndexCita() {
		return indexCita;
	}

	/**
	 * @param indexCita the indexCita to set
	 */
	public void setIndexCita(String indexCita) {
		this.indexCita = indexCita;
	}

	/**
	 * @return the serviciosCita
	 */
	public HashMap getServiciosCita() {
		return serviciosCita;
	}

	/**
	 * @param serviciosCita the serviciosCita to set
	 */
	public void setServiciosCita(HashMap serviciosCita) {
		this.serviciosCita = serviciosCita;
	}
	
	
	/**
	 * @return the serviciosCita
	 */
	public Object getServiciosCita(String key) {
		return serviciosCita.get(key);
	}

	/**
	 * @param serviciosCita the serviciosCita to set
	 */
	public void setServiciosCita(String key, Object value ) {
		this.serviciosCita.put(key, value);
	}

	/**
	 * @return the indexServicio
	 */
	public String getIndexServicio() {
		return indexServicio;
	}

	/**
	 * @param indexServicio the indexServicio to set
	 */
	public void setIndexServicio(String indexServicio) {
		this.indexServicio = indexServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the checkCitasControlPO
	 */
	public String getCheckCitasControlPO() {
		return checkCitasControlPO;
	}

	/**
	 * @param checkCitasControlPO the checkCitasControlPO to set
	 */
	public void setCheckCitasControlPO(String checkCitasControlPO) {
		this.checkCitasControlPO = checkCitasControlPO;
	}



	/**
	 * @return the justificacionMap
	 */
	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionesServicios(HashMap justificacionMap) {
		this.justificacionesServicios = justificacionMap;
	}
	
	/**
	 * @return the justificacionMap
	 */
	public Object getJustificacionesServicios(String llave) {
		return justificacionesServicios.get(llave);
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionesServicios(String llave, Object obj) {
		this.justificacionesServicios.put(llave, obj);
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
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

	/**
	 * @return the infoCvsArchivo
	 */
	public InfoDatosString getInfoCvsArchivo() {
		return infoCvsArchivo;
	}

	/**
	 * @param infoCvsArchivo the infoCvsArchivo to set
	 */
	public void setInfoCvsArchivo(InfoDatosString infoCvsArchivo) {
		this.infoCvsArchivo = infoCvsArchivo;
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

	public ArrayList<HashMap<String, Object>> getCitasIncumplidas() {
		return citasIncumplidas;
	}

	public void setCitasIncumplidas(
			ArrayList<HashMap<String, Object>> citasIncumplidas) {
		this.citasIncumplidas = citasIncumplidas;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the tipoAgendaSeleccionada
	 */
	public String getTipoAgendaSeleccionada() {
		return tipoAgendaSeleccionada;
	}

	/**
	 * @param tipoAgendaSeleccionada the tipoAgendaSeleccionada to set
	 */
	public void setTipoAgendaSeleccionada(String tipoAgendaSeleccionada) {
		this.tipoAgendaSeleccionada = tipoAgendaSeleccionada;
	}

	/**
	 * @return the codigoTipoCita
	 */
	public String getCodigoTipoCita() {
		return codigoTipoCita;
	}

	/**
	 * @param codigoTipoCita the codigoTipoCita to set
	 */
	public void setCodigoTipoCita(String codigoTipoCita) {
		this.codigoTipoCita = codigoTipoCita;
	}

	/**
	 * @return the listCitas
	 */
	public ArrayList<DtoCitaOdontologica> getListCitas() {
		return listCitas;
	}

	/**
	 * @param listCitas the listCitas to set
	 */
	public void setListCitas(ArrayList<DtoCitaOdontologica> listCitas) {
		this.listCitas = listCitas;
	}

	/**
	 * @return the detCitasOdoPlanTrata
	 */
	public DtoPlanTratamientoOdo getDetCitasOdoPlanTrata() {
		return detCitasOdoPlanTrata;
	}

	/**
	 * @param detCitasOdoPlanTrata the detCitasOdoPlanTrata to set
	 */
	public void setDetCitasOdoPlanTrata(DtoPlanTratamientoOdo detCitasOdoPlanTrata) {
		this.detCitasOdoPlanTrata = detCitasOdoPlanTrata;
	}

	/**
	 * @return the entidadManejaProgramas
	 */
	public String getEntidadManejaProgramas() {
		return entidadManejaProgramas;
	}

	/**
	 * @param entidadManejaProgramas the entidadManejaProgramas to set
	 */
	public void setEntidadManejaProgramas(String entidadManejaProgramas) {
		this.entidadManejaProgramas = entidadManejaProgramas;
	}

	/**
	 * @return the listDetalleCita
	 */
	public ArrayList<DtoProgramasServiciosPlanT> getListDetalleCita() {
		return listDetalleCita;
	}

	/**
	 * @param listDetalleCita the listDetalleCita to set
	 */
	public void setListDetalleCita(
			ArrayList<DtoProgramasServiciosPlanT> listDetalleCita) {
		this.listDetalleCita = listDetalleCita;
	}

	/**
	 * @return the programaCita
	 */
	public String getProgramaCita() {
		return programaCita;
	}

	/**
	 * @param programaCita the programaCita to set
	 */
	public void setProgramaCita(String programaCita) {
		this.programaCita = programaCita;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ayudanteFechaInicio
	 *
	 * @return retorna la variable ayudanteFechaInicio
	 */
	public Date getAyudanteFechaInicio() {
		return ayudanteFechaInicio;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ayudanteFechaInicio
	 * @param ayudanteFechaInicio es el valor para el atributo ayudanteFechaInicio 
	 */
	public void setAyudanteFechaInicio(Date ayudanteFechaInicio) {
		this.ayudanteFechaInicio = ayudanteFechaInicio;
		
		this.fechaInicio = UtilidadFecha.conversionFormatoFechaAAp(this.ayudanteFechaInicio);
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ayudanteFechaFin
	 *
	 * @return retorna la variable ayudanteFechaFin
	 */
	public Date getAyudanteFechaFin() {
		return ayudanteFechaFin;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ayudanteFechaFin
	 * @param ayudanteFechaFin es el valor para el atributo ayudanteFechaFin 
	 */
	public void setAyudanteFechaFin(Date ayudanteFechaFin) {
		this.ayudanteFechaFin = ayudanteFechaFin;
		
		this.fechaFin = UtilidadFecha.conversionFormatoFechaAAp(this.ayudanteFechaFin);
	}

	/**
	 * @return the tieneMedico
	 */
	public boolean isTieneMedico() {
		return tieneMedico;
	}

	/**
	 * @param tieneMedico the tieneMedico to set
	 */
	public void setTieneMedico(boolean tieneMedico) {
		this.tieneMedico = tieneMedico;
	}


}