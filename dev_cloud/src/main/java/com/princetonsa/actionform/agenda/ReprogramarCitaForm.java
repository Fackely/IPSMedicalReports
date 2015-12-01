/*
* @(#)ReprogramarCitaForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.2_01
*/
package com.princetonsa.actionform.agenda;

import java.io.Serializable;

import java.lang.String;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Iterator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
 
/**
* ActionForm, tiene la función de bean dentro de la forma, que contiene todos los datos necesarios
* para la reprogramación de una cita de consulta. Adicionalmente hace el manejo de limpieza de la
* orma y de validación de datos de entrada.
*
* @version 1.0, Abr 05, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class ReprogramarCitaForm extends ValidatorForm implements Serializable
{
	/** Objeto que permite el manejo de archivos de registro */
	private Logger logger=Logger.getLogger(ReprogramarCitaForm.class);
	
	/**
	 * Manejo de errores de citas repetidas
	 */
	private String error;
	/**
	 * Nombre de la unidad de consulta
	 */
	private String nombreUnidadConsulta;
	
	/**
	 * Nombre del médico
	 */
	private String nombreMedico;
	
	/**
	 * Nombre del consultorio
	 */
	private String nombreConsultorio;
	
	/** Contenedor de datos de un ítem de agenda de consulta */
	private transient HashMap idb_item;

	/** Código del ítem de agenda de consultas */
	private int ii_agenda;

	/** Código de la cita */
	private int ii_codigo;

	/** Código del consultorio sobre el cual realizar la búsqueda de las citas */
	private int ii_consultorio;

	/** Código del consultorio sobre el cual realizar la búsqueda de la agenda disponible */
	private int ii_consultorioAgenda;

	/** Número de la cuenta del paciente */
	private int ii_cuenta;

	/** Código del día de la semana utilizado para la busqueda de la agenda disponible
	* 1 - Lunes
	* 2 - Martes
	* 3 - Miércoles
	* 4 - Jueves
	* 5 - Viernes
	* 6 - Sábado
	* 7 - Domingo
	*/
	private int ii_diaSemanaAgenda;

	/** Código del estado de la cita */
	private int ii_estadoCita;

	/** Código del médico sobre el cual realizar la búsqueda de las citas */
	private int ii_medico;

	/** Código del médico sobre el cual realizar la búsqueda de la agenda disponible */
	private int ii_medicoAgenda;

	/** Código del paciente de la cita */
	private int ii_paciente;

	/** Indice del ítem seleccionado */
	private int ii_seleccionado;

	/** Número de ítems en la lista de citas a reprogramar */
	private int ii_seleccionados;

	/** Código del sexo del paciente */
	private int ii_sexoPaciente;

	/** Código de la unidad de consulta sobre la cual realizar la búsqueda de las citas */
	private int ii_unidadConsulta;

	/**
	* Código de la unidad de consulta sobre la cual realizar la búsqueda de la agenda disponible
	*/
	private int ii_unidadConsultaAgenda;

	/** Datos de los citas seleccionados para modificación */
	private transient Map im_citas;

	/** Estado actual del flujo */
	private String is_estado;

	/** Estado de liquidación sobre el cual realizar la búsqueda de las citas */
	private String is_estadoLiquidacionCita;

	/** Fecha de finalización sobre la cual realizar la búsqueda de las citas */
	private String is_fechaFin;

	/** Fecha de finalización sobre la cual realizar la búsqueda de la agenda disponible */
	private String is_fechaFinAgenda;

	/** Fecha de inicio sobre la cual realizar la búsqueda de las citas */
	private String is_fechaInicio;

	/** Fecha de inicio sobre la cual realizar la búsqueda de la agenda disponible */
	private String is_fechaInicioAgenda;

	/** Hora de finalización sobre la cual realizar la búsqueda de las citas */
	private String is_horaFin;

	/** Hora de finalización sobre la cual realizar la búsqueda de la agenda disponible */
	private String is_horaFinAgenda;

	/** Hora de inicio sobre la cual realizar la búsqueda de las cita */
	private String is_horaInicio;

	/** Hora de inicio sobre la cual realizar la búsqueda de la agenda disponible */
	private String is_horaInicioAgenda;

	/** Listado de ítems de agenda de consultas para listar */
	private transient Vector iv_agenda;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    /**
     * 
     */
    private HashMap justificacionNoPosMap  = new HashMap();
    
    /**
     * 
     */
    private String mostrarSoloDisponibles;
    
    /**
	 * Nombre de la unidad de consulta
	 */
	private String nombreCentroAtencion;
	
	
	private int posCita;
	private int posServicio;
	
	/**
	 * Fecha solicitada por el paciente
	 */
	private String fechaSolicitada;
	
	/**
	 * Arreglo de Mensajes que informen sobre las incosistencias de 
	 * la Resrva de Citas de un Paciente 
	 */
	private ArrayList<ResultadoBoolean> lisMensaje = new ArrayList<ResultadoBoolean>(); 
	
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
	 * Variables temporales para realizar la consulta de
	 * las condiciones para la toma
	 */
	private int codigoServicio;
	private String nombreServicio;
	
	/**
	 * Obtener el codigo de la agenda como variable temporal
	 */
	private String codigoUnidadAgendaTemp;
	
	/**
	 * 
	 */
	private String centrosAtencion;
	
	/**
	 * 
	 */
	private String unidadesAgenda;
	
	/**
	 * Arreglo que almacena las condiciones para la toma de un servicio
	 */
	private ArrayList<HashMap<String, Object>> condicionesToma = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa que almacena los servicios consultados por la unidad de agenda
	 */
	private HashMap<String, Object> mapaServicios = new HashMap<String, Object>();
	
	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * convenio
	 */
	private int convenio;
	
	/**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;
	
	/**
	 * 
	 * */
	private String consultorio; 
	
	/**
	 * 
	 * */
	private String codigomedicor;

	
	//**********************************************
	// atributos de para la validacion para el manejo de multas de citas incumplidas
	/**
	 * Array de la citas incimplidad por el paciente
	 */
	private ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	private boolean estadoValidarCitasPaciente;
	private boolean realizadaVerificacion;
	private String mostrarFormAuto;
	private String preguntarAutorizacion;
	private String motivoAutorizacionCita;
	private String usuarioAutoriza;
	private String citasIncumpl; 
	private String requiereAuto;
	private String verificarEstCitaPac;
	private String cargarAgenda;
	private String linkSiguiente;
	private String respAutorizacionUsu;
	//**********************************************
	
	private String ordenAmbulatoria;
	private String servicioOrdenAmbulatoria;
	private boolean indicativoOrdenAmbulatoria;
	
	/** Elimina la información de la agenda asociada a la cita */
	public void aReprogramarCita()
	{
		cambiarCita(
			new Integer(this.getCodigoAgenda()),
			new Integer(ConstantesBD.codigoEstadoCitaAReprogramar),
			new Integer(this.getCodigoSexoPaciente()),
			new Integer(this.getCodigoUnidadConsulta()),
			new Integer(this.getCentroAtencion()),
			this.getFechaInicio(),
			this.getHoraFinAgenda(),
			this.getHoraInicioAgenda(),
			this.getNombreConsultorio(),
			this.getNombreMedico(),
			this.getNombreUnidadConsulta(),
			this.getNombreCentroAtencion(),
			this.getConsultorio(),
			this.getCodigomedicor()
		);
	}

	/** Cambia la información de una cita */
	private void cambiarCita(
		Object ao_codigoAgenda,
		Object ao_codigoEstadoCita,
		Object ao_codigoSexo,
		Object ao_codigoUnidadConsulta,
		Object ao_codigoCentroAtencion,
		Object ao_fecha,
		Object ao_horaFin,
		Object ao_horaInicio,
		Object ao_nombreConsultorio,
		Object ao_nombreMedico,
		Object ao_nombreUnidadConsulta,
		Object ao_nombreCentroAtencion,
		Object ao_consultorio,
		Object ao_codigomedico
	)
	{
		
		int		li_i;
		String	ls_estadoLiquidacion;

		li_i = getIndiceItemSeleccionado();
		ls_estadoLiquidacion = (String)getItemSeleccionado("codigoEstadoLiquidacion_" + li_i);

		if(!getItemsSeleccionados().containsKey("codigoAgendaOriginal_" + li_i) )
		{
			setItemSeleccionado(
				"codigoAgendaOriginal_" + li_i, getItemSeleccionado("codigoAgenda_" + li_i)
			);
			setItemSeleccionado(
				"codigoUnidadAgendaOriginal_" + li_i,
				getItemSeleccionado("codigoUnidadAgenda_" + li_i)
			);
			setItemSeleccionado(
					"codigoCentroAtencionOriginal_" + li_i,
					getItemSeleccionado("codigoCentroAtencion_" + li_i)
				);
									
			setItemSeleccionado("fechaOriginal_" + li_i, getItemSeleccionado("fecha_" + li_i) );	
			
			setItemSeleccionado(
				"horaFinOriginal_" + li_i, getItemSeleccionado("horaFin_" + li_i)
			);
			setItemSeleccionado(
				"horaInicioOriginal_" + li_i, getItemSeleccionado("horaInicio_" + li_i)
			);
			setItemSeleccionado(
				"nombreConsultorioOriginal_" + li_i,
				getItemSeleccionado("nombreConsultorio_" + li_i)
			);
			setItemSeleccionado(
					"consultorioOriginal_" + li_i,
					getItemSeleccionado("consultorio_" + li_i)
				);
			setItemSeleccionado(
				"nombreMedicoOriginal_" + li_i, getItemSeleccionado("nombreMedico_" + li_i)
			);
			setItemSeleccionado(
					"codigomedicoOriginal_" + li_i, getItemSeleccionado("codigomedico_" + li_i)
				);
			setItemSeleccionado(
				"nombreUnidadAgendaOriginal_" + li_i,
				getItemSeleccionado("nombreUnidadAgenda_" + li_i)
			);
			setItemSeleccionado(
					"nombreCentroAtencionOriginal_" + li_i,
					getItemSeleccionado("nombreCentroAtencion_" + li_i)
				);
		}
		
		//--------Si es Reprogramada se realiza esta asignación ---------//
		if(Integer.parseInt(ao_codigoEstadoCita+"")==ConstantesBD.codigoEstadoCitaReprogramada)
		{
			setItemSeleccionado("fecha_" + li_i, ao_fecha);
			setItemSeleccionado("horaFin_" + li_i, ao_horaFin);
			setItemSeleccionado("horaInicio_" + li_i, ao_horaInicio);
			setItemSeleccionado("consultorio_" + li_i, ao_consultorio);
			setItemSeleccionado("nombreConsultorio_" + li_i, ao_nombreConsultorio);
			setItemSeleccionado("nombreMedico_" + li_i, ao_nombreMedico);
			setItemSeleccionado("codigomedico_" + li_i, ao_codigomedico);
			//setItemSeleccionado("codigoServicio_" + li_i, codigoServicio );
			logger.info("codigo unidad agenda mapa: *"+getItemSeleccionado("codigoUnidadAgenda_"+li_i)+"*");
			logger.info("codigo unidad agenda parámetro: *"+ao_codigoUnidadConsulta+"*");
			//Si la cita se cambia a reprogramada se borran sus servicios pero solo si cambió su unidad de agenda
			if(!getItemSeleccionado("codigoUnidadAgenda_"+li_i).toString().equals(ao_codigoUnidadConsulta.toString()))
			{
				for(int i=0;i<Integer.parseInt(getItemSeleccionado("numServicios_"+li_i).toString());i++)
				{
					getItemsSeleccionados().remove("codigoServicio_"+li_i+"_"+i);
					getItemsSeleccionados().remove("codigoCentroCosto_"+li_i+"_"+i);
					getItemsSeleccionados().remove("codigoCentroCostoOriginal_"+li_i+"_"+i);
					getItemsSeleccionados().remove("estadoServicio_"+li_i+"_"+i);
				}
				setItemSeleccionado("numServicios_"+li_i, "0");
			}
		}

		setItemSeleccionado("cambioEstadoCita_" + li_i, new Boolean(true) );
		setItemSeleccionado("codigoAgenda_" + li_i, ao_codigoAgenda);
		setItemSeleccionado("codigoEstadoCita_" + li_i, ao_codigoEstadoCita);
		setItemSeleccionado("codigoSexo_" + li_i, ao_codigoSexo);
		//setItemSeleccionado("fecha_" + li_i, ao_fecha);
		//setItemSeleccionado("horaFin_" + li_i, ao_horaFin);
		//setItemSeleccionado("horaInicio_" + li_i, ao_horaInicio);
		//setItemSeleccionado("nombreConsultorio_" + li_i, ao_nombreConsultorio);
		//setItemSeleccionado("nombreMedico_" + li_i, ao_nombreMedico);
		setItemSeleccionado("validarCitaFecha_" + li_i, new Boolean(true) );
		//setItemSeleccionado("codigoServicio_" + li_i, codigoServicio );
		
		logger.info("CODIGO DE NUEVA UNIDAD AGENDA!!! "+ao_codigoUnidadConsulta);
		logger.info("NUMERO DE SERVICIOS CITA: "+getItemSeleccionado("numServicios_"+li_i));
		
		if(ls_estadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar) )
		{
			logger.info("SE ASIGNO NUEVO CODIGO DE UNIDAD AGENDA!!!");
			setItemSeleccionado("codigoUnidadAgenda_" + li_i, ao_codigoUnidadConsulta);
			setItemSeleccionado("nombreUnidadAgenda_" + li_i, ao_nombreUnidadConsulta);
			setItemSeleccionado("codigoCentroAtencion_" + li_i, ao_codigoCentroAtencion);
			setItemSeleccionado("nombreCentroAtencion_" + li_i, ao_nombreCentroAtencion);
		}
	}

	/** Obtener el código del ítem de agenda de consulta */
	public int getCodigoAgenda()
	{
		return ii_agenda;
	}

	/** Obtener el código de la cita */
	public int getCodigoCita()
	{
		return ii_codigo;
	}

	/** Obtener el código del consultorio */
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/** Obtener el código del consultorio de la agenda disponible */
	public int getCodigoConsultorioAgenda()
	{
		return ii_consultorioAgenda;
	}

	/** Obtener el código del día semana de la agenda disponible */
	public int getCodigoDiaSemanaAgenda()
	{
		return ii_diaSemanaAgenda;
	}

	/** Obtener código del estado de la cita */
	public int getCodigoEstadoCita()
	{
		return ii_estadoCita;
	}

	/** Obtener el código del médico */
	public int getCodigoMedico()
	{
		return ii_medico;
	}

	/** Obtener el código del médico de la agenda disponible*/
	public int getCodigoMedicoAgenda()
	{
		return ii_medicoAgenda;
	}

	/** Obtener el código del paciente */
	public int getCodigoPaciente()
	{
		return ii_paciente;
	}

	/** Obtener el código del sexo del paciente */
	public int getCodigoSexoPaciente()
	{
		return ii_sexoPaciente;
	}

	/** Obtener la unidad de consulta */
	public int getCodigoUnidadConsulta()
	{
		return ii_unidadConsulta;
	}

	/** Obtener la unidad de consulta de la agenda disponible */
	public int getCodigoUnidadConsultaAgenda()
	{
		return ii_unidadConsultaAgenda;
	}

	/** Obtener la cuenta del paciente */
	public int getCuentaPaciente()
	{
		return ii_cuenta;
	}

	/** Obtener el estado actual del flujo */
	public String getEstado()
	{
		return is_estado;
	}

	/** Obtener el estado de liquidación de la cita */
	public String getEstadoLiquidacionCita()
	{
		return is_estadoLiquidacionCita;
	}

	/** Obtener la fecha de finalización */
	public String getFechaFin()
	{
		return is_fechaFin;
	}

	/** Obtener la fecha de finalización de la agenda disponible */
	public String getFechaFinAgenda()
	{
		return is_fechaFinAgenda;
	}

	/** Obtener la fecha de inicio */
	public String getFechaInicio()
	{
		return is_fechaInicio;
	}

	/** Obtener la fecha de inicio de la agenda disponible */
	public String getFechaInicioAgenda()
	{
		return is_fechaInicioAgenda;
	}

	/** Obtener la hora de finalización */
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/** Obtener la hora de finalización de la agenda disponible */
	public String getHoraFinAgenda()
	{
		return is_horaFinAgenda;
	}

	/** Obtener la hora de inicio */
	public String getHoraInicio()
	{
		return is_horaInicio;
	}

	/** Obtener la hora de inicio de la agenda disponible */
	public String getHoraInicioAgenda()
	{
		return is_horaInicioAgenda;
	}

	/** Obtener el índice del ítem seleccionado */
	public int getIndiceItemSeleccionado()
	{
		return ii_seleccionado;
	}

	/** Obtener el contenedor de datos de un ítem de agenda de consulta */
	public HashMap getItem()
	{
		return idb_item;
	}

	/** Obtener la colección de ítems de agenda de consulta */
	public Collection getItems()
	{
		return iv_agenda;
	}

	/** Obtener un dato de un ítem de citas a modificar */
	public Object getItemSeleccionado(String as_dato)
	{
		if(im_citas.get(as_dato)==null)
			im_citas.put(as_dato,"");
		return im_citas.get(as_dato);
	}

	/** Obtener los datos de varios ítems de citas a modificar */
	public Map getItemsSeleccionados()
	{
		return im_citas;
	}

	/** Obtener el número de citas seleccionadas para modificación */
	public int getNumeroItemsSeleccionados()
	{
		return ii_seleccionados;
	}

	/** Obtener el número de ítems de agenda de consulta a listar */
	public Integer getSize()
	{
		return new Integer(size() );
	}

	/** Elimina la información de la agenda asociada a la cita 
	 * @param con 
	 * @param medico */
	public void reprogramarCita(Connection con, UsuarioBasico medico)
	{
		boolean		conflicto;
		boolean		encontrado;
		boolean		repetido;
		HashMap	ldb_agenda;
		int			tamanio;
		Iterator	li_agenda;
		String		fecha;
		String		horaFin;
		String		horaInicio;

		/*
			Obtener el ítem de agenda que se quiere incluir en la lista de reprogramación
			de citas
		*/
		conflicto	= false;
		encontrado	= false;
		repetido	= false;
		ldb_agenda	= null;
		li_agenda	= getItems().iterator();
		fecha		= "";
		horaFin		= "";
		horaInicio	= "";

		while(li_agenda.hasNext() && !encontrado)
		{
			ldb_agenda=(HashMap)li_agenda.next();
			encontrado=( Utilidades.convertirAEntero((ldb_agenda.get("codigo") ) +"")) == getCodigoAgenda();
		}

		/* Obtener el número de ítems seleccionados */
		tamanio = getNumeroItemsSeleccionados();

		/* Se encontro el ítem de agenda solicitado */
		if(encontrado)
		{
			Date				ld_horaFinActual;
			Date				ld_horaFinNueva;
			Date				ld_horaInicioActual;
			Date				ld_horaInicioNueva;
			int					codigoAgendaAProgramar;
			SimpleDateFormat	dateFormato;
			String				ls_fechaActual;

			fecha = UtilidadFecha.conversionFormatoFechaAAp( (String)ldb_agenda.get("fecha") );

			horaFin		= ldb_agenda.get("horafin").toString();
			horaInicio	= ldb_agenda.get("horainicio").toString();
			
			/* El formato de la fecha y hora que se espera es HH:mm en 24 horas */
			dateFormato = new SimpleDateFormat("dd/MM/yyyy H:mm");

			try
			{
				ld_horaFinNueva		= dateFormato.parse(fecha + " " + horaFin);
				ld_horaInicioNueva	= dateFormato.parse(fecha + " " + horaInicio);
			}
			catch(ParseException lpe_fechaInicio)
			{
				ld_horaFinNueva		= new Date();
				ld_horaInicioNueva	= new Date();
			}

			/* Validar que el ítem no este presente en la lista */
			for(int i = 0; i < tamanio && !repetido && !conflicto; i++)
			{
				codigoAgendaAProgramar = ( Utilidades.convertirAEntero(getItemSeleccionado("codigoAgenda_" + i)+"") );

				if(!(repetido = (codigoAgendaAProgramar == getCodigoAgenda() ) ) )
				{
					try
					{
						/*
							Determinar si este ítem de agenda tiene conflictos de horario con los ítems ya
							existentes
						*/
						ls_fechaActual = (String)getItemSeleccionado("fecha_" + i);

						ld_horaFinActual =
							dateFormato.parse(
								ls_fechaActual + " " + (String)getItemSeleccionado("horaFin_" + i)
							);
						ld_horaInicioActual =
							dateFormato.parse(
								ls_fechaActual + " " + (String)getItemSeleccionado("horaInicio_" + i)
							);

						/*
						 * Nunca va a existir conflicto de este tipo ya que no se
						 * puede seleccionar la misma que ya está seleccionada
						 * y si se puede seleccionar a la misma hora en otro
						 * consultorio con un médico diferente
						 */
						/*
						conflicto =
							((ld_horaInicioNueva.compareTo(ld_horaInicioActual)	<= 0	&&
								ld_horaInicioActual.compareTo(ld_horaFinNueva)	<= 0)	||
							(ld_horaInicioNueva.compareTo(ld_horaFinActual)		<= 0	&&
								ld_horaFinActual.compareTo(ld_horaFinNueva)		<= 0)	||
							(ld_horaInicioActual.compareTo(ld_horaInicioNueva)	<= 0	&&
								ld_horaInicioNueva.compareTo(ld_horaFinActual)	<= 0));
						*/
							
					}
					catch(ParseException lpe_fechaInicio)
					{
					}
				}
			}
		}

		logger.info("¿encontrado? "+encontrado);
		logger.info("¿repetido? "+repetido);
		logger.info("¿conflicto? "+conflicto);
		
				
		/* El ítem de agenda se puede incluir */
		if(encontrado && !repetido && !conflicto)
		{
			Integer	li_codigoAgenda;
			Integer	li_sexo;

			/* Obtener el código de agenda a inlcuir */
			li_codigoAgenda = new Integer(getCodigoAgenda());

			/* Obtener el sexo del procedimiento */
			if( (li_sexo = Utilidades.convertirAEntero(ldb_agenda.get("codigo_sexo")+"") ) == null)
				li_sexo = new Integer(ConstantesBD.codigoSexoTodos);
			
			logger.info("numServicios antes de cambiar cita: "+this.getItemSeleccionado("numServicios_"+getIndiceItemSeleccionado()));
			
			cambiarCita(
				li_codigoAgenda,
				new Integer(ConstantesBD.codigoEstadoCitaReprogramada),
				li_sexo,
				Utilidades.convertirAEntero(ldb_agenda.get("codigounidadconsulta")+""),
				Utilidades.convertirAEntero(ldb_agenda.get("codigocentroatencion")+""),
				fecha,
				horaFin,
				horaInicio,
				(String)ldb_agenda.get("nombreconsultorio"),
				(String)ldb_agenda.get("nombremedico"),
				(String)ldb_agenda.get("nombreunidadconsulta"),
				(String)ldb_agenda.get("nombrecentroatencion"),
				ldb_agenda.get("consultorio")+"",
				ldb_agenda.get("codigomedico")+""
			);
			
			logger.info("numServicios despues de cambiar cita: "+this.getItemSeleccionado("numServicios_"+getIndiceItemSeleccionado()));
			logger.info("¿Puedo modificar servicios? "+getItemSeleccionado("puedoModificarServicios_"+getIndiceItemSeleccionado()).toString());
			
			//Se vuelven a consultar los centros de costo de la nueva unidad de agenda
			int pos = getIndiceItemSeleccionado();
			if(UtilidadTexto.getBoolean(getItemSeleccionado("puedoModificarServicios_"+pos).toString()))
			{
				try
				{
					Cita cita = new Cita();
					setItemSeleccionado("mapaCentrosCosto_"+pos, cita.consultarCentrosCostoXUnidadDeConsulta(
						con, 
						Integer.parseInt(getItemSeleccionado("codigoAgenda_"+pos).toString()), 
						Integer.parseInt(getItemSeleccionado("codigoCentroAtencion_"+pos).toString()), 
						medico.getCodigoInstitucionInt(), 
						Integer.parseInt(getItemSeleccionado("codigoUnidadAgenda_"+pos).toString())));
				}
				catch(SQLException e)
				{
					logger.error("Error consultan los centros de costo de la cita");
				}
			}
			
		}
		else
		{
			if(repetido || conflicto)
				this.setErrorForm("La cita ya fue asignada previamente");
			else
				this.setErrorForm("");
			restaurarCita();
		}
	}

	/** Limpia la forma */
	public void reset()
	{
		this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
       
        this.mostrarSoloDisponibles=ConstantesBD.acronimoSi;
		idb_item			= null;
		this.mensaje=new ResultadoBoolean(false);
		ii_agenda				= -1;
		ii_codigo				= -1;
		ii_consultorio			= -1;
		ii_consultorioAgenda	= -1;
		ii_cuenta				= -1;
		ii_diaSemanaAgenda		= -1;
		ii_estadoCita			= -1;
		ii_medico				= -1;
		ii_medicoAgenda			= -1;
		ii_paciente				= -1;
		ii_seleccionado			= -1;
		ii_seleccionados		= 0;
		ii_sexoPaciente			= -1;
		ii_unidadConsulta		= -1;
		ii_unidadConsultaAgenda	= -1;
		centroAtencion = -1;

		is_estado					= "";
		is_estadoLiquidacionCita	= "";
		is_fechaFin					= "";
		is_fechaFinAgenda			= "";
		is_fechaInicio				= "";
		is_fechaInicioAgenda		= "";
		is_horaFin					= "";
		is_horaFinAgenda			= "";
		is_horaInicio				= "";
		is_horaInicioAgenda			= "";
		nombreConsultorio ="";
		codigomedicor = "";
		consultorio = "";
		nombreUnidadConsulta="";
		nombreCentroAtencion="";
		nombreMedico="";
		error="";
		
		this.fechaSolicitada="";
		
		im_citas	= new HashMap();
		iv_agenda	= new Vector();
		
		this.posCita = 0;
		this.posServicio = 0;
		
		this.codigoServicio = 0;
		this.nombreServicio = "";
		
		this.condicionesToma = new ArrayList<HashMap<String,Object>>();
		
		this.codigoUnidadAgendaTemp = "";
		
		this.mapaServicios = new HashMap<String, Object>();
		
		this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
        
        this.unidadAgendaMap=new HashMap();
        this.unidadAgendaMap.put("numRegistros", 0);
        this.centrosAtencion="";
        this.unidadesAgenda="";
        
    	this.centrosAtencionAutorizados=new HashMap();
    	this.centrosAtencionAutorizados.put("numRegistros", "0");
    	this.unidadesAgendaAutorizadas=new HashMap();
    	this.unidadesAgendaAutorizadas.put("numRegistros", "0");
    	
    	//**********************************************
    	// atributos de para la validacion para el manejo de multas de citas incumplidas
    	this.estadoValidarCitasPaciente = false;
		this.realizadaVerificacion = false;
		this.mostrarFormAuto = ConstantesBD.acronimoNo;
		this.preguntarAutorizacion = ConstantesBD.acronimoNo;
		this.motivoAutorizacionCita = "";
		this.usuarioAutoriza = "";
		this.citasIncumpl = ConstantesBD.acronimoNo;
		this.requiereAuto = ConstantesBD.acronimoNo;
		this.verificarEstCitaPac = ConstantesBD.acronimoNo;
		this.cargarAgenda = ConstantesBD.acronimoNo;
		this.linkSiguiente = "" ;
		this.respAutorizacionUsu = ConstantesBD.acronimoNo;
    	//**********************************************
		
		this.ordenAmbulatoria="";
		this.servicioOrdenAmbulatoria="";
		this.indicativoOrdenAmbulatoria=false;
	}

	/** Restaurar la información original de la cita */
	public void restaurarCita()
	{
		int li_i;

		li_i = getIndiceItemSeleccionado();

		setItemSeleccionado(
			"codigoEstadoCita_" + li_i, getItemSeleccionado("codigoEstadoCitaOriginal_" + li_i)
		);

		if(getItemsSeleccionados().containsKey("codigoAgendaOriginal_" + li_i) )
		{
			setItemSeleccionado("cambioEstadoCita_" + li_i, new Boolean(false) );
			setItemSeleccionado(
				"codigoAgenda_" + li_i, getItemSeleccionado("codigoAgendaOriginal_" + li_i)
			);
			setItemSeleccionado(
				"codigoSexo_" + li_i, getItemSeleccionado("codigoSexoOriginal_" + li_i)
			);
			setItemSeleccionado(
				"codigoUnidadAgenda_" + li_i,
				getItemSeleccionado("codigoUnidadAgendaOriginal_" + li_i)
			);
			setItemSeleccionado(
					"codigoCentroAtencion_" + li_i,
					getItemSeleccionado("codigoCentroAtencionOriginal_" + li_i)
				);
			setItemSeleccionado("fecha_" + li_i, getItemSeleccionado("fechaOriginal_" + li_i) );
			setItemSeleccionado(
				"horaFin_" + li_i, getItemSeleccionado("horaFinOriginal_" + li_i)
			);
			setItemSeleccionado(
				"horaInicio_" + li_i, getItemSeleccionado("horaInicioOriginal_" + li_i)
			);
			setItemSeleccionado(
				"nombreConsultorio_" + li_i,
				getItemSeleccionado("nombreConsultorioOriginal_" + li_i)
			);
			setItemSeleccionado(
				"nombreMedico_" + li_i, getItemSeleccionado("nombreMedicoOriginal_" + li_i)
			);
			setItemSeleccionado(
				"nombreUnidadAgenda_" + li_i,
				getItemSeleccionado("nombreUnidadAgendaOriginal_" + li_i)
			);
			setItemSeleccionado(
					"nombreCentroAtencion_" + li_i,
					getItemSeleccionado("nombreCentroAtencionOriginal_" + li_i)
				);
			
			setItemSeleccionado(
					"consultorio_" + li_i,
					getItemSeleccionado("consultorio_" + li_i)
				);
			setItemSeleccionado(
					"codigomedico_" + li_i,
					getItemSeleccionado("codigomedico_" + li_i)
				);

			getItemsSeleccionados().remove("codigoAgendaOriginal_" + li_i);
			getItemsSeleccionados().remove("codigoSexoOriginal_" + li_i);
			getItemsSeleccionados().remove("codigoUnidadConsultaOriginal_" + li_i);
			getItemsSeleccionados().remove("codigoCentroAtencionOriginal_" + li_i);
			getItemsSeleccionados().remove("fechaOriginal_" + li_i);
			getItemsSeleccionados().remove("horaFinOriginal_" + li_i);
			getItemsSeleccionados().remove("horaInicioOriginal_" + li_i);
			getItemsSeleccionados().remove("nombreConsultorioOriginal_" + li_i);
			getItemsSeleccionados().remove("nombreMedicoOriginal_" + li_i);
			getItemsSeleccionados().remove("nombreUnidadConsultaOriginal_" + li_i);
			getItemsSeleccionados().remove("nombreCentroAtencionOriginal_" + li_i);
			getItemsSeleccionados().remove("consultorioOriginal_" + li_i);
			getItemsSeleccionados().remove("codigomedicoOriginal_" + li_i);
		}
	}

	/** Establecer el código del ítem de agenda */
	public void setCodigoAgenda(int ai_agenda)
	{
		ii_agenda = ai_agenda;
	}

	/** Establecer el código de la cita */
	public void setCodigoCita(int ai_codigo)
	{
		ii_codigo = ai_codigo;
	}

	/** Establecer el código del consultorio */
	public void setCodigoConsultorio(int ai_consultorio)
	{
		ii_consultorio = ai_consultorio;
	}

	/** Establecer el código del consultorio de la agenda disponible */
	public void setCodigoConsultorioAgenda(int ai_consultorioAgenda)
	{
		ii_consultorioAgenda = ai_consultorioAgenda;
	}

	/** Establecer el código del día de la semana de la agenda disponible */
	public void setCodigoDiaSemanaAgenda(int ai_diaSemanaAgenda)
	{
		ii_diaSemanaAgenda = ai_diaSemanaAgenda;
	}

	/** Establecer el código del estado de la cita */
	public void setCodigoEstadoCita(int ai_estadoCita)
	{
		ii_estadoCita = ai_estadoCita;
	}

	/** Establecer el código del médico */
	public void setCodigoMedico(int ai_medico)
	{
		ii_medico = ai_medico;
	}

	/** Establecer el código del médico de la agenda disponible */
	public void setCodigoMedicoAgenda(int ai_medicoAgenda)
	{
		ii_medicoAgenda = ai_medicoAgenda;
	}

	/** Establecer el código del paciente */
	public void setCodigoPaciente(int ai_paciente)
	{
		ii_paciente = ai_paciente;
	}

	/** Obtener el código del sexo del paciente */
	public void setCodigoSexoPaciente(int ai_sexoPaciente)
	{
		ii_sexoPaciente = ai_sexoPaciente;
	}

	/** Establecer la unidad de consulta */
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		ii_unidadConsulta = ai_unidadConsulta;
	}

	/** Establecer la unidad de consulta de la agenda disponible */
	public void setCodigoUnidadConsultaAgenda(int ai_unidadConsultaAgenda)
	{
		ii_unidadConsultaAgenda = ai_unidadConsultaAgenda;
	}

	/** Establecer la la cuenta del paciente */
	public void setCuentaPaciente(int ai_cuenta)
	{
		ii_cuenta = ai_cuenta;
	}

	/** Establecer el estado actual del flujo */
	public void setEstado(String as_estado)
	{
		if(as_estado != null)
			is_estado = as_estado.trim();
	}

	/** Establecer el estado de liquidación de la cita */
	public void setEstadoLiquidacionCita(String as_estadoLiquidacionCita)
	{
		if(as_estadoLiquidacionCita != null)
			is_estadoLiquidacionCita = as_estadoLiquidacionCita.trim();
	}

	/** Establecer la fecha de finalización */
	public void setFechaFin(String as_fechaFin)
	{
		if(as_fechaFin != null)
			is_fechaFin = as_fechaFin.trim();
	}

	/** Establecer la fecha de finalización de la agenda disponible */
	public void setFechaFinAgenda(String as_fechaFinAgenda)
	{
		if(as_fechaFinAgenda != null)
			is_fechaFinAgenda = as_fechaFinAgenda.trim();
	}

	/** Establecer la fecha de inicio */
	public void setFechaInicio(String as_fechaInicio)
	{
		if(as_fechaInicio != null)
			is_fechaInicio = as_fechaInicio.trim();
	}

	/** Establecer la fecha de inicio de la agenda disponible */
	public void setFechaInicioAgenda(String as_fechaInicioAgenda)
	{
		if(as_fechaInicioAgenda != null)
			is_fechaInicioAgenda = as_fechaInicioAgenda.trim();
	}

	/** Establecer la hora de finalización */
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/** Establecer la hora de finalización de la agenda disponible */
	public void setHoraFinAgenda(String as_horaFinAgenda)
	{
		if(as_horaFinAgenda != null)
			is_horaFinAgenda = as_horaFinAgenda.trim();
	}

	/** Establecer la hora de inicio */
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			is_horaInicio = as_horaInicio.trim();
	}

	/** Establecer la hora de inicio de la agenda disponible */
	public void setHoraInicioAgenda(String as_horaInicioAgenda)
	{
		if(as_horaInicioAgenda != null)
			is_horaInicioAgenda = as_horaInicioAgenda.trim();
	}

	/** Establecer el índice del ítem seleccionado */
	public void setIndiceItemSeleccionado(int ai_seleccionado)
	{
		ii_seleccionado = ai_seleccionado;
	}

	/**
	* Establecer el contenedor de datos de un ítem de agenda de consulta
	* @param adb_item <code>HashMap<code> contenedor de datos de un ítem de agenda de consulta
	*/
	public void setItem(HashMap adb_item)
	{
		idb_item = adb_item;
	}

	/** Adicionar un dato de un ítem de cita a la lista de citas a modificar */
	public void setItemSeleccionado(String as_dato, Object ao_valor)
	{
		if(as_dato != null && as_dato.length() > 0 && ao_valor != null)
			im_citas.put(as_dato, ao_valor);
	}

	/** Adicionar los datos de varios ítems de cita a la lista citas a modificar 
	 * @param lc_con 
	 * @param medico */
	public void setItemsSeleccionados(Connection lc_con, Collection ac_citas, UsuarioBasico medico)
	{
		Iterator li_iterator;

		/* Eliminar los ítems seleccionados para reprogración existentes */
		getItemsSeleccionados().clear();
		setNumeroItemsSeleccionados(0);

		if(ac_citas != null)
		{
			HashMap	ldb_cita;
			Integer		li_estadoCita;
			String		ls_aux;
			String		ls_apellidosMedico;
			String		ls_nombresMedico;

			li_iterator = ac_citas.iterator();

			for(int li_i = 0; li_iterator.hasNext(); li_i++)
			{
				ldb_cita			= (HashMap)li_iterator.next();
				
				
				/* Obtener el nombre del médico de la cita */
				ls_apellidosMedico =
					(ls_aux = (String)ldb_cita.get("primerapellidomedico") ) != null ?
						ls_aux.trim() : "";

				if( (ls_aux = (String)ldb_cita.get("segundoapellidomedico") ) != null)
					ls_apellidosMedico = (ls_apellidosMedico + " " + ls_aux).trim();

				ls_nombresMedico =
					(ls_aux = (String)ldb_cita.get("primernombremedico") ) != null ?
					ls_aux.trim() : "";

				if( (ls_aux = (String)ldb_cita.get("segundonombremedico") ) != null)
					ls_nombresMedico = (ls_nombresMedico + " " + ls_aux).trim();

				if(!ls_apellidosMedico.equals("") && !ls_nombresMedico.equals("") )
					ls_apellidosMedico += ", ";

				/* Obtener el estado de la cita */
				li_estadoCita = Utilidades.convertirAEntero(ldb_cita.get("codigoestadocita")+"");

				setItemSeleccionado("cambioEstadoCita_" + li_i, new Boolean(false) );
				setItemSeleccionado(
					"codigoAgenda_" + li_i, Utilidades.convertirAEntero(ldb_cita.get("codigoagenda")+"")
				);
				setItemSeleccionado("codigoCita_" + li_i, Utilidades.convertirAEntero(ldb_cita.get("codigo")+"") );
				setItemSeleccionado("codigoEstadoCita_" + li_i, li_estadoCita);
				setItemSeleccionado("codigoEstadoCitaOriginal_" + li_i, li_estadoCita);
				setItemSeleccionado(
					"codigoEstadoLiquidacion_" + li_i,
					(String)ldb_cita.get("codigoestadoliquidacion")
				);
				setItemSeleccionado(
						"nombreEstadoLiquidacion_" + li_i,
						Utilidades.getNombreEstadoLiquidacion(lc_con, ldb_cita.get("codigoestadoliquidacion").toString())
					);
				setItemSeleccionado(
					"codigoUnidadAgenda_" + li_i, Utilidades.convertirAEntero(ldb_cita.get("codigounidadconsulta")+"")
				);
				setItemSeleccionado(
						"codigoUnidadAgendaOriginal_" + li_i, Utilidades.convertirAEntero(ldb_cita.get("codigounidadconsulta")+"")
					);
				setItemSeleccionado(
						"codigoCentroAtencion_" + li_i, Utilidades.convertirAEntero(ldb_cita.get("codigocentroatencion")+"")
					);
				setItemSeleccionado(
					"fecha_" + li_i,
					UtilidadFecha.conversionFormatoFechaAAp(
						(String)ldb_cita.get("fecha")
					)
				);
				//this.setHoraInicioAgenda((String)ldb_cita.get("horainicio"));
				setItemSeleccionado("horaFin_" + li_i, (String)ldb_cita.get("horafin") );
				if(this.getEstado().equals("listar"))
					setItemSeleccionado("horaInicio_" + li_i, UtilidadFecha.convertirHoraACincoCaracteres((String)ldb_cita.get("horainicio") ));
				else
					setItemSeleccionado("horaInicio_" + li_i, this.getHoraInicioAgenda());
				
				nombreConsultorio=(String)ldb_cita.get("nombreconsultorio");
				setItemSeleccionado(
					"nombreConsultorio_" + li_i, nombreConsultorio
				);
				
				consultorio=ldb_cita.get("consultorio")+"";
				setItemSeleccionado(
					"consultorio_" + li_i, consultorio
				);
				
				nombreMedico = ls_apellidosMedico + ls_nombresMedico;
				setItemSeleccionado("nombreMedico_" + li_i, nombreMedico);
				codigomedicor = ldb_cita.get("codigomedico")+"";
				setItemSeleccionado("codigomedico_" + li_i, codigomedicor);
				nombreUnidadConsulta=(String)ldb_cita.get("nombreunidadconsulta");
				setItemSeleccionado(
					"nombreUnidadAgenda_" + li_i, nombreUnidadConsulta
				);
				nombreCentroAtencion=(String)ldb_cita.get("nombrecentroatencion");
				setItemSeleccionado(
					"nombreCentroAtencion_" + li_i, nombreCentroAtencion
				);
				
				setItemSeleccionado("validarCitaFecha_" + li_i, new Boolean(true) );
				setItemSeleccionado("agendaOriginal_"+li_i, ldb_cita.get("codigoagenda")+"");
				
				setItemSeleccionado("ordenAmb_"+li_i, ldb_cita.get("ordenamb")+"");
				setItemSeleccionado("servOrdenAmb_"+li_i, ldb_cita.get("servordenamb")+"");
				
				try
				{
					Cita cita = new Cita();
					setItemSeleccionado("mapaCentrosCosto_"+li_i, cita.consultarCentrosCostoXUnidadDeConsulta(
						lc_con, 
						Integer.parseInt(getItemSeleccionado("codigoAgenda_"+li_i).toString()), 
						Integer.parseInt(getItemSeleccionado("codigoCentroAtencion_"+li_i).toString()), 
						medico.getCodigoInstitucionInt(), 
						Integer.parseInt(getItemSeleccionado("codigoUnidadAgenda_"+li_i).toString())));
				}
				catch(SQLException e)
				{
					logger.error("Error consultan los centros de costo de la cita");
				}
				
				//********************SE CONSULTAN LOS SERVICIOS DE LA CITA******************************************************************
				HashMap servicios = Cita.consultarServiciosCita(lc_con, getItemSeleccionado("codigoCita_"+li_i).toString(), medico.getCodigoInstitucionInt());
				
				int contador = 0;
				int numServConSolicitud = 0; //variable que cuenta el número de servicios que tiene solicitud
				
				for(int j=0;j<Integer.parseInt(servicios.get("numRegistros").toString());j++)
				{
					//Solo se toman los activos
					if(servicios.get("estadoServicio_"+j).toString().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
					{
					
						setItemSeleccionado("codigoServicio_" + li_i + "_"+contador, servicios.get("codigoServicio_"+j));
						setItemSeleccionado("numeroSolicitud_" + li_i + "_"+contador, servicios.get("numeroSolicitud_"+j));
						setItemSeleccionado("codigoEspecialidad_" + li_i + "_"+contador, servicios.get("codigoEspecialidad_"+j));
						setItemSeleccionado("nombreServicio_" + li_i + "_"+contador, servicios.get("nombreServicio_"+j));
						setItemSeleccionado("codigoCentroCosto_" + li_i + "_"+contador, servicios.get("codigoCentroCosto_"+j));
						setItemSeleccionado("codigoCentroCostoOriginal_" + li_i + "_"+contador, servicios.get("codigoCentroCosto_"+j));
						setItemSeleccionado("nombreCentroCosto_" + li_i + "_"+contador, servicios.get("nombreCentroCosto_"+j));
						setItemSeleccionado("estadoServicio_" + li_i + "_"+contador, servicios.get("estadoServicio_"+j));
						setItemSeleccionado("observaciones_" + li_i + "_"+contador, servicios.get("observaciones_"+j));
						setItemSeleccionado("observacionesOriginal_" + li_i + "_"+contador, servicios.get("observaciones_"+j));
						setItemSeleccionado("codigoSexo_" + li_i + "_"+contador, servicios.get("codigoSexo_"+j));
						setItemSeleccionado("esPos_" + li_i + "_"+contador, servicios.get("esPos_"+j));
						setItemSeleccionado("tieneCondiciones_" + li_i + "_"+contador, servicios.get("tieneCondiciones_"+j));
						setItemSeleccionado("convenio_" + li_i + "_"+contador, servicios.get("convenio_"+j));
						contador++;
						
						if(!servicios.get("numeroSolicitud_"+j).toString().equals(""))
							numServConSolicitud ++;
					}
				}
				setItemSeleccionado("numServicios_"+li_i, contador+"");
				
				if(numServConSolicitud>0)
					setItemSeleccionado("puedoModificarServicios_"+li_i, ConstantesBD.acronimoNo);
				else
					setItemSeleccionado("puedoModificarServicios_"+li_i, ConstantesBD.acronimoSi);
				//************************************************************************************************************************
				
				
			}

			/* Establecer el número de items seleccionados para modificación */
			setNumeroItemsSeleccionados(ac_citas.size() );
		}
	}

	/** Adicionar los datos de varios ítems de cita a la lista citas a modificar */
	public void setItemsSeleccionados(Map am_citas)
	{
		if(am_citas != null)
			im_citas = am_citas;
		else
			im_citas.clear();
	}

	/**
	* Establecer el conjunto de ítems de agenda de consulta a listar
	* @param ac_agenda <code>Collection<code> de ítems de agenda de consulta a listar
	*/
	public void setItems(Collection ac_agenda)
	{
		iv_agenda = (ac_agenda != null) ? new Vector(ac_agenda) : new Vector(0);
	}

	/** Establecer el número de citas seleccionadas para modificación */
	private void setNumeroItemsSeleccionados(int ai_seleccionados)
	{
		ii_seleccionados = ai_seleccionados < 0 ? 0 : ai_seleccionados;
	}

	/** Obtener el número de ítems de agenda de consulta a listar */
	public int size()
	{
		return iv_agenda == null ? 0 : iv_agenda.size();
	}

	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping aam_m, HttpServletRequest hsr_r)
	{
		ActionErrors lae_errors;

		lae_errors = new ActionErrors();;

		lae_errors.add(super.validate(aam_m, hsr_r) );

		if(lae_errors.isEmpty() )
		{
			if(is_estado.equals("listar") )
			{
				/* Validar esto sí y solo si no se han encontrado errores */
				Date				ld_fechaInicio;
				Date				ld_fechaFin;
				SimpleDateFormat	lsdf_sdfFecha;

				/* Iniciar variables. El formato de fecha que se espera es dd/MM/yyyy */
				lsdf_sdfFecha	= new SimpleDateFormat("dd/MM/yyyy");
				ld_fechaInicio	= null;
				ld_fechaFin		= null;;

				/* Exije una interpretación estricta del formato de fecha esperado */
				lsdf_sdfFecha.setLenient(false);

				if(!is_fechaInicio.equals("") )
				{
					try
					{
						Calendar lc_cal;

						/* Obtener la fecha de inicio */
						ld_fechaInicio = lsdf_sdfFecha.parse(is_fechaInicio);

						lc_cal = new GregorianCalendar();
						lc_cal.set(Calendar.HOUR_OF_DAY, 0);
						lc_cal.set(Calendar.MILLISECOND, 0);
						lc_cal.set(Calendar.MINUTE, 0);
						lc_cal.set(Calendar.SECOND, 0);

						/*
							Verificar que el listado de citas solo traiga citas cuya fecha sea mayor
							o igual a la fecha actual
						*//*
						if(lc_cal.getTime().after(ld_fechaInicio) )
							lae_errors.add(
								"fechaInicio",
								new ActionMessage(
									"errors.fechaAnteriorAOtraDeReferencia", "Inicial", "actual"
								)
							);*/
					}
					catch(ParseException lpe_fechaInicio)
					{
						lae_errors.add(
							"fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Inicial")
						);
						ld_fechaInicio = null;
					}
				}
				/*
				 * No es requerido
				else
				{
					lae_errors.add("fechaInicio", new ActionMessage("errors.required", "Fecha Inicial"));
				}*/

				if(!is_fechaFin.equals("") )
				{
					try
					{
						/* Obtener la fecha de finalización */
						ld_fechaFin = lsdf_sdfFecha.parse(is_fechaFin);
					}
					catch(ParseException lpe_fechaFin)
					{
						lae_errors.add(
							"fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Final")
						);
						ld_fechaFin = null;
					}
				}
				/*
				 * No es requerido
				else
				{
					lae_errors.add("fechaFin", new ActionMessage("errors.required", "Fecha Final"));
				}
				*/
				if(
					ld_fechaFin != null		&&
					ld_fechaInicio != null	&&
					ld_fechaFin.before(ld_fechaInicio)
				)
				{
					/* La fecha de inicio debe ser anterior a la fecha de finalización */
					lae_errors.add(
						"fechaFin",
						new ActionMessage(
							"errors.fechaAnteriorAOtraDeReferencia", "Fecha Final", "Fecha Inicial"
						)
					);
				}
				
				if (UtilidadCadena.noEsVacio(is_fechaInicio) && UtilidadCadena.noEsVacio(is_fechaFin))
						{
						//-----Se valida que no se genere una agenda para un rango de fechas mayor de tres meses-----//
						if (lae_errors.isEmpty())
							{
							int nroMeses = UtilidadFecha.numeroMesesEntreFechas(is_fechaInicio, is_fechaFin,true);
							if (nroMeses > 3)
								{
									lae_errors.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "REPROGRAMAR CITAS"));
								}
							}
						}

				/* Validar esto sí y solo si no se han encontrado errores */
				/* Fechas no requeridas
				if(!is_horaInicio.equals("") || !is_horaFin.equals("") )
				{*/
					Date				ld_horaInicio;
					Date				ld_horaFin;
					SimpleDateFormat	lsdf_sdfHora;

					/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
					lsdf_sdfHora	= new SimpleDateFormat("H:mm");
					ld_horaInicio	= null;
					ld_horaFin		= null;

					/* Exije una interpretación estricta del formato de hora esperado */
					lsdf_sdfHora.setLenient(false);

					if(!is_horaInicio.equals("") )
					{
						try
						{
							/* Obtener la hora de inicio */
							ld_horaInicio = lsdf_sdfHora.parse(is_horaInicio);
						}
						catch(ParseException lpe_horaInicio)
						{
							lae_errors.add(
								"horaInicio",
								new ActionMessage("errors.formatoHoraInvalido", "de inicio")
							);
							ld_horaInicio = null;
						}
					}

					if(!is_horaFin.equals("") )
					{
						try
						{
							/* Obtener la hora de finalización */
							ld_horaFin = lsdf_sdfHora.parse(is_horaFin);
						}
						catch(ParseException lpe_horaFin)
						{
							lae_errors.add(
								"horaFin",
								new ActionMessage("errors.formatoHoraInvalido", "de finalización")
							);
							ld_horaFin = null;
						}
					}

					/* La hora de inicio debe ser anterior a la hora de finalización */
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
				//}
			}
			else if(is_estado.equals("reprogramar"))
			{
				/*
					Validar que la fecha y hora de inicio de la agenda a reprogramar  sea
					mayor que la fecha actual
				*/
				Boolean				lb_cambio;
				Date				ld_cita;
				Object				lo_o;
				SimpleDateFormat	lsdf_sdf;
				String				ls_fecha;
				String				ls_hora;

				/*
					Iniciar variables. El formato de tiempo que se espera esdd/MM/yyyy HH:mm en 24
					horas
				*/
				lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");

				/* Exije una interpretación estricta del formato de hora esperado */
				lsdf_sdf.setLenient(false);

				for(int li_i = 0, li_tam =getNumeroItemsSeleccionados(); li_i < li_tam; li_i++)
				{
					try
					{
						lb_cambio = (Boolean)getItemSeleccionado("cambioEstadoCita_" + li_i);

						if(lb_cambio.booleanValue() )
						{
							ls_fecha	= (String)getItemSeleccionado("fechaOriginal_" + li_i);
							ls_hora		= (String)getItemSeleccionado("horaInicioOriginal_" + li_i);
							
							int agendaAnterior=Integer.parseInt((String)getItemSeleccionado("agendaOriginal_" + li_i));

							/* Obtener la fecha y hora de inicio */
							ld_cita = lsdf_sdf.parse(ls_fecha + " " + ls_hora);

							Connection con;
							try
                            {
                                con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
                                if(!Cita.fueCanceladaIns(con, agendaAnterior))
                                {
    								if(ld_cita.before(new Date() ) )
    								{
    									lae_errors.add(
    										"",
    										new ActionMessage(
    											"error.cita.agendaVencida",
    											(String)getItemSeleccionado(
    												"nombreUnidadAgendaOriginal_" + li_i
    											),
    											ls_fecha,
    											ls_hora,
    											"reprogamada"
    										)
    									);
    		
    									setIndiceItemSeleccionado(li_i);
    									restaurarCita();
    								}
                                }
                                UtilidadBD.closeConnection(con);
                            }
                            catch (SQLException e)
                            {
                                return null;
                            }
						}

						/* Convertir el estado de la cita en un objeto entero */
						lo_o = getItemSeleccionado("codigoEstadoCita_" + li_i);

						if(lo_o instanceof String)
							setItemSeleccionado(
								"codigoEstadoCita_" + li_i, Integer.valueOf( (String)lo_o)
							);
						
						
					}
					catch(ParseException lpe_horaInicio)
					{
					}
				}
				///Se verifican los servicios de cada cita
				lae_errors = validacionDetalleCitas(lae_errors);
			}

		}

		/* Si no hay errores limpiar el contenedor */
		if(lae_errors.isEmpty() )
			lae_errors = null;

		this.setErrorForm("");
		return lae_errors;
	}
	
	/**
	 * Validacion del detalle de las citas
	 * @param lae_errors
	 * @return
	 */
	private ActionErrors validacionDetalleCitas(ActionErrors lae_errors) 
	{
		for(int i=0;i<getNumeroItemsSeleccionados();i++)
		{
			//Validamos que se selccione un centro de costo solicitado por cada una de las citas
			int numServicios = Integer.parseInt(getItemSeleccionado("numServicios_"+i).toString());
			int contador = 0;
			
			logger.info("numServicios=> "+numServicios);
			if(numServicios>0)
			{
				for(int j=0;j<numServicios;j++)
				{
					logger.info("codigoServicio=> "+getItemSeleccionado("codigoServicio_"+i+"_"+j));
					logger.info("estadoServicio=> *"+getItemSeleccionado("estadoServicio_"+i+"_"+j)+"*");
					
					//Se excluyen los servicios eliminados o anulados 
					if(getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null&&
						(getItemSeleccionado("estadoServicio_"+i+"_"+j)==null||!getItemSeleccionado("estadoServicio_"+i+"_"+j).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)))
					{
					
						
						//Se verifica el centro de costo
						if(getItemSeleccionado("codigoCentroCosto_"+i+"_"+j)==null||getItemSeleccionado("codigoCentroCosto_"+i+"_"+j).toString().equals(""))
							lae_errors.add("CC Solicitado", 
								new ActionMessage(
									"errors.required",
									"El centro de costo solicitado para la cita N° "+(i+1)+" y el servicio "+getItemSeleccionado("nombreServicio_"+i+"_"+j)));
						
						
						//se toma sexo del servicio
						int li_sexo =Integer.parseInt(getItemSeleccionado("codigoSexo_" + i + "_" + j)+"");
						if(li_sexo!=ConstantesBD.codigoSexoTodos&&
								li_sexo != getCodigoSexoPaciente())
						{
							lae_errors.add(
									"codigoSexoPaciente",
									new ActionMessage(
										"error.cita.sexoPaciente",
										getItemSeleccionado("nombreServicio_" + i + "_" + j),
										getItemSeleccionado("nombreUnidadAgenda_" + i),
										getItemSeleccionado("fecha_" + i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(getItemSeleccionado("horaInicio_" + i).toString()),
										"reservada"
									)
								);
								
						}
						
						contador ++;
						
					}
				}
			}
			
			
			if(contador<=0)
			{
			
				//Se valida que se haya seleccionado al menos un servicio para la cita
				lae_errors.add("Mínimo un servicio",new ActionMessage("errors.paciente.requeridoIngresoDe","mínimo un servicio para la cita N° "+(i+1)));
			}
			
		}
		
		return lae_errors;
	}
	
	/**
	 * @return nombre del consultorio
	 */
	public String getNombreConsultorio()
	{
		return nombreConsultorio;
	}

	/**
	 * asigna el nombre del consultorio
	 * @param string
	 */
	public void setNombreConsultorio(String string)
	{
		nombreConsultorio = string;
	}

	/**
	 * @return nombre del médico
	 */
	public String getNombreMedico()
	{
		return nombreMedico;
	}

	/**
	 * Asignar el nombre del médico
	 * @param string
	 */
	public void setNombreMedico(String string)
	{
		nombreMedico = string;
	}

	/**
	 * @return nombre de la unidad de consulta
	 */
	public String getNombreUnidadConsulta()
	{
		return nombreUnidadConsulta;
	}

	/**
	 * Asignar el nombre de la unidad de consulta
	 * @param string
	 */
	public void setNombreUnidadConsulta(String string)
	{
		nombreUnidadConsulta = string;
	}

	/**
	 * @return
	 */
	public String getErrorForm()
	{
		return error;
	}

	/**
	 * @param string
	 */
	public void setErrorForm(String string)
	{
		error = string;
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

	/**
	 * @return Retorna the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion()
	{
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion)
	{
		this.nombreCentroAtencion = nombreCentroAtencion;
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
	 * @return the codigoUnidadAgendaTemp
	 */
	public String getCodigoUnidadAgendaTemp() {
		return codigoUnidadAgendaTemp;
	}

	/**
	 * @param codigoUnidadAgendaTemp the codigoUnidadAgendaTemp to set
	 */
	public void setCodigoUnidadAgendaTemp(String codigoUnidadAgendaTemp) {
		this.codigoUnidadAgendaTemp = codigoUnidadAgendaTemp;
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
	 * @return the mapaServicios
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}

	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(String key,Object obj) {
		this.mapaServicios.put(key,obj);
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
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the im_citas
	 */
	public Map getIm_citas() {
		return im_citas;
	}

	/**
	 * @param im_citas the im_citas to set
	 */
	public void setIm_citas(Map im_citas) {
		this.im_citas = im_citas;
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
	 * @return the codigomedicor
	 */
	public String getCodigomedicor() {
		return codigomedicor;
	}

	/**
	 * @param codigomedicor the codigomedicor to set
	 */
	public void setCodigomedicor(String codigomedicor) {
		this.codigomedicor = codigomedicor;
	}

	/**
	 * @return the consultorio
	 */
	public String getConsultorio() {
		return consultorio;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(String consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the realizadaVerificacion
	 */
	public boolean isRealizadaVerificacion() {
		return realizadaVerificacion;
	}

	/**
	 * @param realizadaVerificacion the realizadaVerificacion to set
	 */
	public void setRealizadaVerificacion(boolean realizadaVerificacion) {
		this.realizadaVerificacion = realizadaVerificacion;
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
	 * @return the requiereAuto
	 */
	public String getRequiereAuto() {
		return requiereAuto;
	}

	/**
	 * @param requiereAuto the requiereAuto to set
	 */
	public void setRequiereAuto(String requiereAuto) {
		this.requiereAuto = requiereAuto;
	}

	/**
	 * @return the verificarEstCitaPac
	 */
	public String getVerificarEstCitaPac() {
		return verificarEstCitaPac;
	}

	/**
	 * @param verificarEstCitaPac the verificarEstCitaPac to set
	 */
	public void setVerificarEstCitaPac(String verificarEstCitaPac) {
		this.verificarEstCitaPac = verificarEstCitaPac;
	}
	
	/**
	 * @return the estadoValidarCitasPaciente
	 */
	public boolean isEstadoValidarCitasPaciente() {
		return estadoValidarCitasPaciente;
	}

	/**
	 * @param estadoValidarCitasPaciente the estadoValidarCitasPaciente to set
	 */
	public void setEstadoValidarCitasPaciente(boolean estadoValidarCitasPaciente) {
		this.estadoValidarCitasPaciente = estadoValidarCitasPaciente;
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
	 * @return the cargarAgenda
	 */
	public String getCargarAgenda() {
		return cargarAgenda;
	}

	/**
	 * @param cargarAgenda the cargarAgenda to set
	 */
	public void setCargarAgenda(String cargarAgenda) {
		this.cargarAgenda = cargarAgenda;
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
	 * @return the mostrarFormAuto
	 */
	public String getMostrarFormAuto() {
		return mostrarFormAuto;
	}

	/**
	 * @param mostrarFormAuto the mostrarFormAuto to set
	 */
	public void setMostrarFormAuto(String mostrarFormAuto) {
		this.mostrarFormAuto = mostrarFormAuto;
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
	 * @return the ordenAmbulatoria
	 */
	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	/**
	 * @param ordenAmbulatoria the ordenAmbulatoria to set
	 */
	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	/**
	 * @return the servicioOrdenAmbulatoria
	 */
	public String getServicioOrdenAmbulatoria() {
		return servicioOrdenAmbulatoria;
	}

	/**
	 * @param servicioOrdenAmbulatoria the servicioOrdenAmbulatoria to set
	 */
	public void setServicioOrdenAmbulatoria(String servicioOrdenAmbulatoria) {
		this.servicioOrdenAmbulatoria = servicioOrdenAmbulatoria;
	}

	/**
	 * @return the indicativoOrdenAmbulatoria
	 */
	public boolean isIndicativoOrdenAmbulatoria() {
		return indicativoOrdenAmbulatoria;
	}

	/**
	 * @param indicativoOrdenAmbulatoria the indicativoOrdenAmbulatoria to set
	 */
	public void setIndicativoOrdenAmbulatoria(boolean indicativoOrdenAmbulatoria) {
		this.indicativoOrdenAmbulatoria = indicativoOrdenAmbulatoria;
	}

	public String getMostrarSoloDisponibles() {
		return mostrarSoloDisponibles;
	}

	public void setMostrarSoloDisponibles(String mostrarSoloDisponibles) {
		this.mostrarSoloDisponibles = mostrarSoloDisponibles;
	}
	
	
}