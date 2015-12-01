/*
* @(#)Cita.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.agenda;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesValoresPorDefecto;
import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dao.CitaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.SolicitudConsultaExterna;

/**
* Esta clase encapsula los atributos y la funcionalidad de una cita en la agenda de consultas
*
* @version 1.0, Sep 25, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
* @version 2.0, Marzo 26, 2004
* @author <a href="mailto:raul@PrincetonSA.com">Raï¿½l Cancino</a>,
* @author <a href="mailto:liliana@PrincetonSA.com">Liliana Caballero</a>
*/
public class Cita implements Serializable
{
	/**
	* Manejador de logs
	*/
	private static Logger logger = Logger.getLogger(Cita.class);
	
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;
	
	private int codigoMedico;
	private String nombreConsultorio;
	private int numeroCuenta;
	private String nombreCompletoMedico;
	/** Valores de Restricciï¿½n sobre la bï¿½squeda de las citas */
	public static final int BUSCAR_TODAS				= 0;
	public static final int BUSCAR_PARA_CANCELACION		= 1;
	public static final int BUSCAR_PARA_LIQUIDACION		= 2;
	public static final int BUSCAR_PARA_REPROGRAMACION	= 3;

	/** El DAO usado por el objeto <code>Cita</code> para acceder a la fuente de datos */
	private static CitaDao citaDao = null;

	/**
	 * Indica si la cita fue pedida con ese mï¿½dico especificamente
	 */
	private boolean perteneceAlMedico;

	/** Estado de liquidacion de la cita */
	private InfoDatos iid_estadoLiquidacion;

	/** Estado de la cita */
	private InfoDatosInt estadoCita;

	/** Unidad de consulta a la cual esta asociada la cita */
	private InfoDatosInt iidi_unidadConsulta;

	/** Codigo del ï¿½tem de agenda a la cual pertenece esta cita */
	private int ii_agenda;

	/** Cï¿½digo ï¿½nico de esta cita */
	private int codigo;
	
	private String nombreCentroAtencion="";
	
	private int codigoCentroAtencion = ConstantesBD.codigoNuncaValido;

	/** Cï¿½digo del paciente que requiere la cita */
	private int ii_paciente;

	/** Nï¿½mero de solicitud generado para la cita */
	private int ii_solicitud;

	/** Objeto para realizar acciones de registro */
	private Logger il_logger = Logger.getLogger(Cita.class);

	/** Fecha de la cita */
	private String is_fecha;

	/** Hora de finalizaciï¿½n de la cita */
	private String is_horaFin;

	/** Hora de inicio de la cita */
	private String is_horaInicio;

	/** Motivo de cancelaciï¿½n de la cita */
	private String is_motivoCancelacion;
	
	/**
	 */
	private String is_codigoMotivoCancelacion;

	/** Nombre completo del paciente que requiere la cita */
	private String is_paciente;

	/** Cï¿½digo del usuario que asigna la cita */
	private String is_usuario;

	/** Documento de identificaciï¿½n del paciente que requiere la cita */
	private TipoNumeroId itnid_paciente;
	
	/**
	 * Fecha solicitada
	 */
	private String is_fechaSolicitada;
	
	private int consecutivoCita;
	
	private int sexoPaciente=0;	
	
	private int codigoAreaPaciente;
	
	private int codigoCentroCostoSolicitado;
	
	private int codigoEspecialidadSolicitante;
	
	
	//*********Atributos usados para la validacion de los requisitos del paciente*********************
	/**
	 * Variable que indica si el paciente tiene cumplidos sus requisitos
	 */
	private boolean indicadorRequisitos;
	private int cuenta;

	//*************************************************************************
	
	/**
	 * Indicativo de PYP
	 */
	private boolean solPYP;
//	-----------------Reserva de Citas -----------------------------//
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
	
	private int naturalezaPaciente;
	
	/**
	 * Codigo del servicio asignado a la cita
	 */
	private int codigoServicioCita;
	
	/**
	 * Telefono del paciente
	 */
	private String telefono;
	

	/**
	 * 
	 */
	private String celular;
	
	/**
	 * 
	 */
	private String otrosTelefonos;
	
	
	private String origenTelefono;
	
	
	//*************************************************************************
	
	
	private String indiPO;
	
	
	
	/**
	 * campos de la cuenta 
	 */
	private String nombreConvenio;	
	private String nombreContrato;
	private String nombreClasificacionSocial;
	private String nombreTipoAfiliado;
	
	/**
	 * Mapa de los servicios
	 */
	private HashMap mapaServicios = new HashMap();
	
	/**
	 * Variable que indica si se cambiï¿½ el estado de la cita en la reprogramacion de citas
	 */
	private boolean cambioEstadoCita;
	
	/**
	 * Variable que indica si la cita ya tiene solicitudes 
	 */
	private boolean tieneSolicitudes;
	
	/**
	 * Variable que indica si se cambiï¿½ la unidad de agenda para la reprogramacion de citas
	 */
	private boolean cambioUnidadAgenda;
	
	private String motivoNoAtencion;
	
	/**
	 * Observaciones
	 * */
	private String observaciones;
	
	/**
	 * Campo para el manejo de la prioridad
	 */
	private String prioridad;
	
	/**
	 * Nï¿½mero de historia clï¿½nica
	 */
	private String numeroHistoriaClinica;
	
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
	
	//**********************************************
	// atributos de para la validacion para el manejo de multas de citas incumplidas
	/**
	 * Array de la citas incimplidad por el paciente
	 */
	private ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	private boolean estadoValidarCitasPaciente;
	private boolean realizadaVerificacion;
	
	private String motivoAutorizacionCita;
	private String usuarioAutoriza;
	private String citasIncumpl; 
	private String requiereAuto;
	private String verificarEstCitaPac;
	//**********************************************
	
	/**
	 * 
	 */
	private String mostrarCita;
	
	
	/**
	 * 
	 */
	private int codigoMedicoXAgenda; 
	
	/** *Atributo que almacena la información de Diagnóstico asociado a la orden ambulatoria generada **/
	private DtoDiagnostico dtoDiagnostico;
	
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
	
	/** Construye un cita sin ningï¿½n atributo especificado */
	public Cita()
	{
		clean();
		init();
	}

	/**
	* Asigna una cita para un paciente en un ï¿½tem de agenda disponible
	* @param ac_con			Conexiï¿½n abierta con una fuente de datos
	 * @param ai_cuenta		Cï¿½digo de la cuenta a la cual se adjuntarï¿½ la solicitud de la cita
	 * @param observaciones @todo
	 * @param ai_centroCosto	Centro de costo que solicita la cita
	 * @param ai_medico		Codigo del mï¿½dico la cual se le adjudicarï¿½ la cita
	* @return Cï¿½digo de la cita asignada
	*/
	public int asignarCita(Connection ac_con,String estado)
	{


		try
		{
			/*
				Validar si las propiedas requeridas para la asignaciï¿½n de la cita han sido establecidas
			*/
			if(
				codigo						== -1	&&
				ii_agenda						> 0		&&
				ii_paciente						> -1	&&
				iidi_unidadConsulta.getCodigo()	> 0		&&
				!is_usuario.equals("")
			)
				codigo =citaDao.asignarCita(ac_con, ii_paciente, ii_agenda, iidi_unidadConsulta.getCodigo(), is_usuario, this.mapaServicios,estado,is_fechaSolicitada,this.telefono,this.prioridad,
						this.motivoAutorizacionCita,this.usuarioAutoriza,this.citasIncumpl);
		}
		catch(Exception e)
		{
			logger.error("Error al asignar la cita: "+e);
		}

		return codigo;
	}

	/**
	* Cancelar una cita
	* @param con Conexiï¿½n abierta con una fuente de datos
	 * @param cupoLibre 
	* @return Indicador de exito de la operaciï¿½n de cancelaciï¿½n
	*/
	public boolean cancelar(Connection con, boolean cupoLibre, String loginUsuario)throws Exception
	{
		/*
			Validar si las propiedas requeridas para la cancelaciï¿½n de la cita han sido
			establecidas
		*/
		if(codigo > 0)
		{
			HashMap detalle=detalleCita(con, codigo);
			int unidadConsulta=Integer.parseInt(String.valueOf(detalle.get("codigounidadconsulta")));
			int codigoAgenda=Integer.parseInt(String.valueOf(detalle.get("codigoagenda")));
			int codigoPaciente=Integer.parseInt(String.valueOf(detalle.get("codigopaciente")));
			String estadoLiquidacion = String.valueOf(detalle.get("codigoestadoliquidacion"));
			String usuario=String.valueOf(detalle.get("usuario"));
			String fechaGeneracion=String.valueOf(detalle.get("fechageneracion"));
			String horaGeneracion=String.valueOf(detalle.get("horageneracion")).substring(0,5);
		
			UtilidadBD.iniciarTransaccion(con);
			
			boolean resultadoCancelacion=false;
			resultadoCancelacion = citaDao.cancelarCita(
					con,
					codigo,
					ii_agenda,
					estadoCita.getCodigo(),
					is_motivoCancelacion,
					is_codigoMotivoCancelacion,
					cupoLibre,
					getCodigoUsuario(),
					loginUsuario
			);
			
			if(resultadoCancelacion && estadoCita.getCodigo()==ConstantesBD.codigoEstadoCitaCanceladaInstitucion && cupoLibre)
			{
				if(citaDao.asignarCitaCanncelada(con,codigoPaciente,estadoLiquidacion,codigo,codigoAgenda,unidadConsulta,fechaGeneracion,horaGeneracion,usuario)>0)
						resultadoCancelacion=true;
				else
						resultadoCancelacion=false;
			}
			
			if(resultadoCancelacion)
				UtilidadBD.finalizarTransaccion(con);
			else
				UtilidadBD.abortarTransaccion(con);
			
			return resultadoCancelacion; 
			
		}
		return false;
	}

	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param con
	 * @param observaciones
	 * @return
	 */
	public int actualizarObservacion(Connection con, String observacion, int codigoServicio,String loginUsuario)
	{
		return citaDao.actualizarObservacion(con, codigo, observacion, codigoServicio, loginUsuario);
	}
	
	/**
	 * Mï¿½todo que realiza la inserciï¿½n del servicio a la cita
	 * @param con
	 * @param codigoAgenda 
	 * @param codigoEstadoCita 
	 * @param horaFinCita 
	 * @param horaInicioCita 
	 * @param campos
	 * @return
	 */
	public int insertarServicioCita(Connection con ,String codigoCita,String codigoServicio,String codigoCentroCosto,String codigoEspecialidad,String observaciones,String usuario,String fechaCita, String horaInicioCita, String horaFinCita, String codigoEstadoCita, String codigoAgenda)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita",codigoCita);
		campos.put("codigoServicio",codigoServicio);
		campos.put("codigoCentroCosto",codigoCentroCosto);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("observaciones",observaciones);
		campos.put("usuario",usuario);
		campos.put("fechaCita", fechaCita);
		campos.put("horaInicioCita", horaInicioCita);
		campos.put("horaFinCita", horaFinCita);
		campos.put("codigoEstadoCita", codigoEstadoCita);
		campos.put("codigoAgenda", codigoAgenda);
		
		return citaDao.insertarServicioCita(con, campos);
	}
	
	
	/** Inicializa los atributos del objeto a valores vacï¿½os (invï¿½lidos) */
	public void clean()
	{
	
		ii_agenda		= -1;
		codigo		= -1;
		ii_paciente		= -1;
		ii_solicitud	= -1;

		iid_estadoLiquidacion	= new InfoDatos();
		estadoCita			= new InfoDatosInt();
		iidi_unidadConsulta		= new InfoDatosInt();

		is_fechaSolicitada 		= "";
		is_usuario				= "";
		is_fecha				= "";
		is_horaFin				= "";
		is_horaInicio			= "";
		is_motivoCancelacion	= "";
		is_codigoMotivoCancelacion= "";
		is_paciente				= "";

		itnid_paciente = new TipoNumeroId();
		this.nombreCompletoMedico="";
		this.numeroCuenta=-1;
		this.nombreConsultorio="";		
		this.cuenta = 0;
		this.indicadorRequisitos = false;
		this.perteneceAlMedico=false;
		this.solPYP = false;
		this.cambioEstadoCita = false;
		
		this.mapaServicios = new HashMap();
		
		this.codigoEspecialidadSolicitante = ConstantesBD.codigoEspecialidadMedicaTodos;
		
		this.tieneSolicitudes = false;
		
		this.cambioUnidadAgenda = false;
		
		this.telefono="";

	    this.celular="";
	    this.otrosTelefonos="";
		this.origenTelefono="";
		
		this.motivoNoAtencion = "";
		
		this.observaciones = "";
		this.prioridad = ConstantesBD.acronimoNo;
		this.numeroHistoriaClinica = "";
		
		this.codigoConsultorioUsua = "";
		this.registroMedico  = "";
		this.loginUsuCita = "";
		
		this.estadoValidarCitasPaciente = false;
		this.realizadaVerificacion = false;
		this.motivoAutorizacionCita = "";
		this.usuarioAutoriza = "";
		this.citasIncumpl = ConstantesBD.acronimoNo;
		this.requiereAuto = ConstantesBD.acronimoNo;
		this.verificarEstCitaPac = ConstantesBD.acronimoNo;
		
		this.mostrarCita = ConstantesBD.acronimoSi;
		
		this.codigoMedicoXAgenda=ConstantesBD.codigoNuncaValido;
		this.setDtoDiagnostico(new DtoDiagnostico());
		
		
	}

	/** Genera una solicitud de consulta externa para la cita 
	 * @param loginUsuario 
	 * @param tieneCita */
	public boolean generarSolicitud(Connection ac_con,String as_estado,int ai_ocupacionMedica,int  ai_cuenta,int	ai_servicioSolicitado,int codigoAreapaciente, int codigoCCSolicictado, String loginUsuario, boolean tieneCita)
	{
		int							li_numeroSolicitud;
		SolicitudConsultaExterna	lsce_solicitud;

		lsce_solicitud = new SolicitudConsultaExterna();

		this.setCodigoAreaPaciente(codigoAreapaciente);
		this.setCodigoCentroCostoSolicitado(codigoCCSolicictado);

		lsce_solicitud.setCentroCostoSolicitante(new InfoDatosInt(codigoAreapaciente));
		lsce_solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCCSolicictado));
		lsce_solicitud.setCobrable(true);
		lsce_solicitud.setCodigoCuenta(ai_cuenta);
		lsce_solicitud.setCodigoServicioSolicitado(ai_servicioSolicitado);
		lsce_solicitud.setEspecialidadSolicitante(new InfoDatosInt(this.codigoEspecialidadSolicitante));
		lsce_solicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
		lsce_solicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(ac_con) );
		lsce_solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(ac_con) );
		//lsce_solicitud.setNumeroAutorizacion(as_numeroAutorizacion);
		lsce_solicitud.setOcupacionSolicitado(new InfoDatosInt(ai_ocupacionMedica) );
		lsce_solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCita) );
		lsce_solicitud.setUrgente(false);
		lsce_solicitud.setVaAEpicrisis(false);
		lsce_solicitud.setSolPYP(this.solPYP);
		lsce_solicitud.setTieneCita(tieneCita);
		
		
		lsce_solicitud.setEspecialidadSolicitadaOrdAmbulatorias(this.codigoEspecialidadSolicitante);
		
		//Agrego el codigo del medico para realizar la insercion del campo de datos del medico responde		
		lsce_solicitud.setCodigoMedicoResponde(this.codigoMedicoXAgenda);
		
		/** * Se agrega tipo CIE y acronimo del diagnostico almacenado en la orden ambulatoria de servicios, para realizar la insercion junto
		 * con la información de la solicitud  **/
		lsce_solicitud.setDtoDiagnostico(this.dtoDiagnostico);

		try
		{
			if(as_estado == null)
				as_estado = "";

			/* Generar la solicitud y obtener el nï¿½mero de solicitud */
			li_numeroSolicitud = lsce_solicitud.insertarSolicitudConsultaExternaTransaccional(ac_con, as_estado);

			/* Asignar la solicitud a esta cita */
			if(tieneCita)
				citaDao.asignarSolicitud(ac_con, codigo, li_numeroSolicitud, ai_servicioSolicitado,loginUsuario, ConstantesBD.continuarTransaccion);
		}
		catch(Exception lse_e)
		{
			lse_e.printStackTrace();

			if(il_logger.isDebugEnabled() )
				il_logger.debug("No se pudo generar ls solicitud para la cita");

			li_numeroSolicitud = -1;
		}

		setNumeroSolicitud(li_numeroSolicitud);

		return li_numeroSolicitud > 0;
	}
	
	/**
	 * Mï¿½todo que realiza la asignacion de una solicitud a la cita
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param estado
	 * @return
	 */
	public int asignarSolicitud(Connection con,int numeroSolicitud,int codigoServicio,String estado,String loginUsuario)
	{
		int resp0 =0;
		try
		{
			resp0 = citaDao.asignarSolicitud(con, codigo, numeroSolicitud, codigoServicio, loginUsuario,estado);
		}
		catch(Exception e)
		{
			logger.error("Error al asignar la solicitud a una cita: "+e);
		}
		return resp0;
	}

	/**
	* Obtener los datos de una cita
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo	Cï¿½digo ï¿½nico de la cita
	* @return Contedor de datos de la cita especificada
	*/
	public static HashMap detalleCita(
		Connection	ac_con,
		int			ai_codigo
	)throws Exception
	{
		return
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().detalleCita(
				ac_con, ai_codigo
			);
	}

	
	
	
	/**
	 * @return Returns the codigoAreaPaciente.
	 */
	public int getCodigoAreaPaciente()
	{
		return codigoAreaPaciente;
	}

	/**
	 * @param codigoAreaPaciente The codigoAreaPaciente to set.
	 */
	public void setCodigoAreaPaciente(int codigoAreaPaciente)
	{
		this.codigoAreaPaciente=codigoAreaPaciente;
	}

	/**
	 * @return Returns the codigoCentroCostoSolicitado.
	 */
	public int getCodigoCentroCostoSolicitado()
	{
		return codigoCentroCostoSolicitado;
	}

	/**
	 * @param codigoCentroCostoSolicitado The codigoCentroCostoSolicitado to set.
	 */
	public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado)
	{
		this.codigoCentroCostoSolicitado=codigoCentroCostoSolicitado;
	}

	/** Obtener el cï¿½digo ï¿½nico de esta cita */
	public int getCodigo()
	{
		return codigo;
	}

	/** Obtener el cï¿½digo del ï¿½tem de agenda a la que pertence la cita */
	public int getCodigoAgenda()
	{
		return ii_agenda;
	}

	/** Obtener el cï¿½digo del estado de la cita */
	public int getCodigoEstadoCita()
	{
		return estadoCita.getCodigo();
	}

	/** Obtener el cï¿½digo del estado actual de la liquidaciï¿½n de la cita */
	public String getCodigoEstadoLiquidacion()
	{
		return iid_estadoLiquidacion.getAcronimo();
	}

	/** Obtener el cï¿½digo del paciente que requiere la cita */
	public int getCodigoPaciente()
	{
		return ii_paciente;
	}

	/** Obtener el cï¿½digo de la unidad de consulta asociada a la cita */
	public int getCodigoUnidadConsulta()
	{
		return iidi_unidadConsulta.getCodigo();
	}

	/** Obtener el cï¿½digo del usuario que asigna la cita al paciente */
	public String getCodigoUsuario()
	{
		return is_usuario;
	}

	/** Obtener el estado de la cita */
	public InfoDatosInt getEstadoCita()
	{
		return estadoCita;
	}

	/** Obtener el estado actual de la liquidaciï¿½n de la cita */
	public InfoDatos getEstadoLiquidacion()
	{
		return iid_estadoLiquidacion;
	}

	/** Obtener la fecha de la cita */
	public String getFecha()
	{
		return is_fecha;
	}

	/** Obtener la hora de finalizaciï¿½n de la cita */
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/** Obtenerla hora de inicio de la cita */
	public String getHoraInicio()
	{
		return is_horaInicio;
	}
	
	

	/**
	 * @param consecutivoCita The consecutivoCita to set.
	 */
	public void setConsecutivoCita(int consecutivoCita)
	{
		this.consecutivoCita= consecutivoCita;
	}
	/** Obtener el documento de identificacion del paciente que requiere la cita */
	public TipoNumeroId getIdentificacionPaciente()
	{
		return itnid_paciente;
	}

	/** Obtener el motivo de cancelaciï¿½n de la cita */
	public String getMotivoCancelacion()
	{
		return is_motivoCancelacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoMotivoCancelacion()
	{
		return is_codigoMotivoCancelacion;
	}

	/** Obtener el nombre completo del paciente que requiere la cita */
	public String getNombreCompletoPaciente()
	{
		return is_paciente;
	}

	/** Obtener el nombre del estado de la cita */
	public String getNombreEstadoCita()
	{
		return estadoCita.getNombre();
	}

	/** Obtener el nombre del estado actual de la liquidaciï¿½n de la cita */
	public String getNombreEstadoLiquidacion()
	{
		return iid_estadoLiquidacion.getNombre();
	}

	/** Obtener el nombre de la unidad de consulta a la cual estï¿½ asociada la cita */
	public String getNombreUnidadConsulta()
	{
		return iidi_unidadConsulta.getNombre();
	}

	/** Obtener el nï¿½mero de solicitud generado para esta cita */
	public int getNumeroSolicitud()
	{
		return ii_solicitud;
	}
	
	
	/** Obtener la unidad de consulta a la cual estï¿½ asociada la cita */
	public InfoDatosInt getUnidadConsulta()
	{
		return iidi_unidadConsulta;
	}

	/** Inicializa el acceso a bases de datos de este objeto */
	public void init()
	{
		/* Obtengo el DAO que encapsula las operaciones de BD de este objeto */
		if(citaDao == null)
			citaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao();
	}

	/**
	* Lista todas las citas de un paciente
	* @param ac_con					Conexiï¿½n abierta con una fuente de datos
	* @param ai_modo				Indicador de modo de bï¿½squeda de las citas
	* @param ai_paciente			Cï¿½digo del paciente
	* @param ai_unidadConsulta		Cï¿½digo de la unidad de consulta a la cual debe estar asociada la
	*								cita
	* @param ai_consultorio			Cï¿½digo del consultoria en la cual se llevara a cabo la cita
	* @param ai_medico				Cï¿½digo del mï¿½dico
	* @param as_fechaInicio			Fecha inicial del rango de fechas de las citas
	* @param as_fechaFin			Fecha final del rango de fechas de las citas
	* @param as_horaInicio			Hora inicial del rango de horas de las citas
	* @param as_horaFin				Hora final del rango de fechas de las citas
	* @param ai_estadoCita			Estado de la cita
	* @param as_estadoLiquidacion	Estado de liquidaciï¿½n de la cita
	* @param ai_cuenta				Codigo de la cuenta del paciente
	* @param centroAtencion 
	* @return <code>Collection</code> de las citas del paciente
	*/
	public static Collection listar(Connection	ac_con,int ai_modo,int ai_paciente,int ai_unidadConsulta,int ai_consultorio,int	ai_medico,String is_fechaSolicitada,String as_fechaInicio,String as_fechaFin,String as_horaInicio,String as_horaFin,int ai_estadoCita,String as_estadoLiquidacion,int ai_cuenta, String centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().listar(ac_con,ai_modo,ai_paciente,ai_unidadConsulta,ai_consultorio,ai_medico,is_fechaSolicitada,as_fechaInicio,as_fechaFin,as_horaInicio,as_horaFin,ai_estadoCita,as_estadoLiquidacion,ai_cuenta,centroAtencion,centrosAtencion,unidadesAgenda);
	}

	/**
	 * Metodo consulta si existe Autorizacion del Paciente
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean consultarAutorizacionPaciente (Connection con, int codigoIngreso, int codigoConvenio){
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCitaDao().consultarAutorizacionPaciente(con, codigoIngreso, codigoConvenio);
	}
	
	/**
	* Reserva una cita para un paciente en un ï¿½tem de agenda disponible
	* @param ac_con Conexiï¿½n abierta con una fuente de datos
	* @return Cï¿½digo asignado a la cita registrada
	*/
	public int reservarCita(Connection ac_con)throws Exception
	{
		
		logger.info(codigo+" "+ii_agenda+" "+ii_paciente+" "+iidi_unidadConsulta.getCodigo()+" "+is_usuario);
		logger.info(this.motivoAutorizacionCita+" "+this.usuarioAutoriza+" "+this.requiereAuto+" "+this.verificarEstCitaPac);
		
		/* Validar si las propiedas requeridas para el registro de la cita han sido establecidas */
		if(codigo== -1	&&ii_agenda> 0&&	ii_paciente> -1	&&iidi_unidadConsulta.getCodigo()> 0 &&!is_usuario.equals(""))
		{
			codigo =
				citaDao.reservarCita(
					ac_con,
					ii_paciente,
					ii_agenda,
					iidi_unidadConsulta.getCodigo(),
					is_usuario,
					mapaServicios,
					is_fechaSolicitada,
					this.prioridad,
					this.motivoAutorizacionCita,
					this.usuarioAutoriza,
					this.requiereAuto,
					this.verificarEstCitaPac
				);
			
			return codigo;
		}

		return -1;
	}
	
	/**
	 * Mï¿½todo que consulta los servicios de una cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static HashMap consultarServiciosCita(Connection con,String codigoCita,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita", codigoCita);
		campos.put("institucion", institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().consultarServiciosCita(con, campos);
	}
	
	/**
	 * Mï¿½todo implementado para actualizar la prioridad de una cita
	 */
	public boolean actualizarPrioridadCita(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita", this.codigo);
		campos.put("prioritaria", this.prioridad);
		return citaDao.actualizarPrioridadCita(con, campos);
	}

	/** Establecer el cï¿½digo ï¿½nico de la cita */
	public void setCodigo(int ai_codigo)
	{
		if(ai_codigo > 0)
			codigo = ai_codigo;
	}

	/** Establecer el cï¿½digo del ï¿½tem de agenda a la que pertence la cita */
	public void setCodigoAgenda(int ai_agenda)
	{
		if(ai_agenda > 0)
			ii_agenda = ai_agenda;
	}

	/** Establecer el cï¿½digo del estado de la cita */
	public void setCodigoEstadoCita(int ai_estadoCita)
	{
		if(
			ai_estadoCita >= ConstantesBD.codigoEstadoCitaAProgramar &&
			ai_estadoCita <= ConstantesBD.codigoEstadoCitaNoCumplida
		)
			estadoCita.setCodigo(ai_estadoCita);
	}

	/** Establecer el cï¿½digo del estado actual de la liquidaciï¿½n de la cita */
	public void setCodigoEstadoLiquidacion(String as_estadoLiquidacion)
	{
		if(as_estadoLiquidacion != null)
		{
			as_estadoLiquidacion = as_estadoLiquidacion.trim().toUpperCase();

			if(as_estadoLiquidacion.equals("S") || as_estadoLiquidacion.equals("L") )
				iid_estadoLiquidacion.setAcronimo(as_estadoLiquidacion);
		}
	}

	/** Establecer el cï¿½digo del paciente que requiere la cita */
	public void setCodigoPaciente(int ai_paciente)
	{
		if(ai_paciente > 0)
			ii_paciente = ai_paciente;
	}

	/** Establecer el cï¿½digo de la unidad de consulta asociada a la cita */
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		if(ai_unidadConsulta > 0)
			iidi_unidadConsulta.setCodigo(ai_unidadConsulta);
	}

	/** Establecer el cï¿½digo del usuario que asignï¿½ la agenda */
	public void setCodigoUsuario(String as_usuario)
	{
		if(as_usuario != null)
			is_usuario = as_usuario.trim();
	}

	/** Establecer el estado de la cita */
	public void setEstadoCita(InfoDatosInt aidi_estadoCita)
	{
		if(aidi_estadoCita != null)
			estadoCita = aidi_estadoCita;
	}

	/** Establecer el estado de liquidaciï¿½n de la cita */
	public void setEstadoLiquidacion(InfoDatos aid_estadoLiquidacion)
	{
		if(aid_estadoLiquidacion != null)
			iid_estadoLiquidacion = aid_estadoLiquidacion;
	}

	/** Establecer la fecha de la cita */
	public void setFecha(String as_fecha)
	{
		if(as_fecha != null)
			is_fecha = as_fecha.trim();
	}

	/** Establecer la hora de finalizaciï¿½n de la cita */
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/** Establecer la hora de inicio de la cita */
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			is_horaInicio = as_horaInicio.trim();
	}

	/** Establecer el documento de identificacion del paciente que requiere la cita */
	public void setIdentificacionPaciente(TipoNumeroId atnid_paciente)
	{
		if(atnid_paciente != null)
			itnid_paciente = atnid_paciente;
	}

	/** Establecer el motivo de cancelaciï¿½n de la cita */
	public void setMotivoCancelacion(String as_motivoCancelacion)
	{
		if(as_motivoCancelacion != null)
			is_motivoCancelacion = as_motivoCancelacion.trim();
	}
	
	/**
	 * 
	 * @param as_codigoMotivoCancelacion
	 */
	public void setCodigoMotivoCancelacion(String as_codigoMotivoCancelacion)
	{
		if(as_codigoMotivoCancelacion != null)
			is_codigoMotivoCancelacion = as_codigoMotivoCancelacion.trim();
	}

	/** Establecer el nombre del estado de la cita */
	public void setNombreEstadoCita(String as_estadoCita)
	{
		if(as_estadoCita != null)
			estadoCita.setNombre(as_estadoCita.trim() );
	}

	/** Establecer el nombre del estado de liquidaciï¿½n de la cita */
	public void setNombreEstadoLiquidacion(String as_estadoLiquidacion)
	{
		if(as_estadoLiquidacion != null)
			iid_estadoLiquidacion.setNombre(as_estadoLiquidacion.trim() );
	}

	/** Establecer el nombre completo del paciente que requiere la cita */
	public void setNombreCompletoPaciente(String as_paciente)
	{
		if(as_paciente != null)
			is_paciente = as_paciente.trim();
	}

	/** Establecer el nombre de la unidad de consulta asociada a la cita */
	public void setNombreUnidadConsulta(String as_unidadConsulta)
	{
		if(as_unidadConsulta != null)
			iidi_unidadConsulta.setNombre(as_unidadConsulta);
	}

	/** Establecer el nï¿½mero de solicitud generado para esta cita */
	public void setNumeroSolicitud(int ai_solicitud)
	{
		if(ai_solicitud > 0)
			ii_solicitud = ai_solicitud;
	}

	/** Establecer la unidad de consulta a la cual esta asociada la cita */
	public void setUnidadConsulta(InfoDatosInt aidi_unidadConsulta)
	{
		if(aidi_unidadConsulta != null)
			iidi_unidadConsulta = aidi_unidadConsulta;
	}

	
	public int getConsecutivoCita()
	{
		return consecutivoCita;
	}
	
	/** Reprogramar un conjunto de citas  
	 * @param usuario TODO*/
	public static boolean reprogramarCitas(Connection con, Collection coleccionCitas, String usuario)throws Exception
	{
		logger.info("coleccion mundo >>>>>> "+coleccionCitas);
		if(coleccionCitas != null && !coleccionCitas.isEmpty() ){
			logger.info("entra a la funcion del dao");
			return
				DaoFactory.getDaoFactory(
					System.getProperty("TIPOBD")
				).getCitaDao().reprogramarCitas(con, coleccionCitas, usuario);
		}else
			return true;
	}

	/**
	* Valida si el paciente tiene o no citas reservada para la fecha de un ï¿½tem de agenda
	* especificado
	* @param ac_con Conexiï¿½n abierta con una fuente de datos
	* @return true si el paciente tiene citas reservadas para la fecha de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFecha(Connection ac_con)throws Exception
	{
		/* Validar si las propiedas requeridas para la validaciï¿½n de citas han sido establecidas */
		if(ii_agenda > 0 && ii_paciente > -1)
			return citaDao.validarReservaCitaFecha(ac_con, ii_paciente, ii_agenda);

		return false;
	}
	
	/**
	* Valida si el paciente tiene o no citas reservada para la fecha y hora de un ï¿½tem de agenda
	* especificado
	* @param ac_con Conexiï¿½n abierta con una fuente de datos
	* @return true si el paciente tiene citas reservadas para la fecha y hora de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public boolean validarReservaCitaFechaHora(Connection ac_con)throws Exception
	{
		/* Validar si las propiedas requeridas para la validaciï¿½n de citas han sido establecidas */
		if(ii_agenda > 0 && ii_paciente > -1)
			return citaDao.validarReservaCitaFechaHora(ac_con, ii_paciente, ii_agenda);

		return false;
	}
	
	
	/**
	 * Mï¿½todo que carga en un HashMap todos los datos
	 * necesarios para imprimir una cita dado su cï¿½digo
	 * 
	 * @param codigoACargar Cï¿½digo de la cita a cargar
	 * @return
	 */
	public HashMap cargarImpresionCodigos(int codigosACargar[])
	{
		return citaDao.listarCitas(codigosACargar);
	}

	/**
	 * Mï¿½todo que carga en un HashMap todos los datos
	 * necesarios para imprimir una cita dado su cï¿½digo
	 * 
	 * @param codigoACargar Cï¿½digo de la cita a cargar
	 * @return--
	 */
	public HashMap cargarImpresionCodigo(int codigosACargar[])
	{
		//En este caso solo nos interesa buscar por cï¿½digo,
		//al conjunto extra de restricciones les ponemos el boolean
		//en false
		Collection listaTemporal=citaDao.listarCitas(null, false, null, false, null, false, null, false, codigosACargar, true);
		if (listaTemporal==null)
		{
			return null;
		}
		Iterator i =listaTemporal.iterator();
		if (i.hasNext())
		{
			return (HashMap)i.next();
		}
		else
		{
			return null;
		}
	}
		
	/**
	 * 
	 * @param con
	 * @param codEstadoCita
	 * @param codigoCita
	 * @param estado
	 * @param usuarioModifica
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codEstadoCita, int codigoCita, String estado, String usuarioModifica)
	{
		return citaDao.actualizarEstadoCitaTransaccional(con, codigoCita, codEstadoCita,"", estado, usuarioModifica);
	}
	
	/**
	 * Sobrecarga metodo actualizarEstadoCitaTransaccional que permite modificar
	 * tambiï¿½n  el motivo de no atenciï¿½n
	 * @param con
	 * @param codEstadoCita
	 * @param codigoCita
	 * @param estado
	 * @param usuarioModifica
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codEstadoCita, int codigoCita, String motivoNoAtencion, String estado, String usuarioModifica)
	{
		return citaDao.actualizarEstadoCitaTransaccional(con, codigoCita, codEstadoCita, motivoNoAtencion, estado, usuarioModifica);
	}
	
	/**
	 * 
	 * @param con
	 * @param codEstadoCita
	 * @param motivoNoAtencion
	 * @param codigoCita
	 * @param estado
	 * @param usuarioModifica
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codEstadoCita,String motivoNoAtencion, int codigoCita, String estado, String usuarioModifica)
	{
		return citaDao.actualizarEstadoCitaTransaccional(con, codigoCita, codEstadoCita,motivoNoAtencion, estado, usuarioModifica);
	}
	
	/**
	 * Actualiza el estado a la cita con codigo de cita dado
	 * @param con
	 * @param codigoCita
	 * @param codEstadoCita
	 * @param estado
	 * @param usuarioModifica
	 * @return
	 */
	public ResultadoBoolean actualizarEstadoCitaXCodigoTransaccional(Connection con, String codigoCita, int codEstadoCita, String estado, String usuarioModifica)
	{
		return citaDao.actualizarEstadoCitaXCodigoTransaccional(con, codigoCita, codEstadoCita, estado, usuarioModifica);
	}
	
	/**
	 * Mï¿½todo implementado para consultar los campos adicionales de la reserva de cita
	 * para la posterior creaciï¿½n de la cuenta
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static HashMap consultaCamposAdicionalesReserva(Connection con,String codigoCita)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().consultaCamposAdicionalesReserva(con, codigoCita);
	}
	/**
	 * Method setNombreCompletoMedico.
	 * @param string
	 */
	public void setNombreCompletoMedico(String string) {
		this.nombreCompletoMedico=string;
	}
	/**
	 * Returns the nombreCompletoMedico.
	 * @return String
	 */
	public String getNombreCompletoMedico() {
		return nombreCompletoMedico;
	}

	/**
	 * Returns the numeroCuenta.
	 * @return int
	 */
	public int getNumeroCuenta() {
		return numeroCuenta;
	}

	/**
	 * Sets the numeroCuenta.
	 * @param numeroCuenta The numeroCuenta to set
	 */
	public void setNumeroCuenta(int numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
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
	 * Returns the codigoMedico.
	 * @return int
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * Sets the codigoMedico.
	 * @param codigoMedico The codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * Mï¿½todo para verificar si la cita fue cancelada anteriormente
	 * @param con
	 * @param codigoAgenda
	 * @return true si la cita fue cancelada anteriormente
	 */
	public static boolean fueCanceladaIns(Connection con, int codigoAgenda)
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().fueCanceladaIns(con, codigoAgenda);
	}
	
	/**
	 * Mï¿½todo para consultar los centros de costo de un medico 
	 * para un centro de atencion y una unidad de consulta dada
	 * @param con
	 * @param codigoMedico
	 * @param centroAtencion
	 * @param institucion
	 * @param unidadConsulta
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCostoXUnidadDeConsulta(Connection con, int codigoAgenda, int centroAtencion, int institucion, int unidadConsulta) throws SQLException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().consultarCentrosCostoXUnidadDeConsulta(con, codigoAgenda, centroAtencion, institucion, unidadConsulta);
	}
	
	/**
	 * Metodo que actualiza en la cita la informaci?n de la cuenta para el caso
	 * de la reserva
	 * @param con
	 * @param codigoCita
	 */
	public int actualizarInfoCuentaCita(Connection con, int codigoCita) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().actualizarInfoCuentaCita(con, codigoCita, this.convenio, this.contrato, this.estratoSocial, this.tipoAfiliado,this.naturalezaPaciente,this.telefono,this.origenTelefono, ii_paciente,this.celular,this.otrosTelefonos);
	}
	
	/**
	 * Mï¿½todo que anula el servicio de una cita
	 * @param con
	 * @param codigoCita
	 * @param codigoServicio
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @param institucion 
	 * @param validarCitaCumplida 
	 * @return
	 */
	public static int anularServicioCita(Connection con,String codigoCita,int codigoServicio,int numeroSolicitud,String loginUsuario, int institucion, boolean validarCitaCumplida)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCita", codigoCita);
		campos.put("codigoServicio", codigoServicio+"");
		campos.put("numeroSolicitud", numeroSolicitud>0?numeroSolicitud+"":"");
		campos.put("usuario",loginUsuario);
		campos.put("institucion",institucion);
		campos.put("validarCitaCumplida", validarCitaCumplida?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().anularServicioCita(con, campos);
	}
	
	/**
	 * Mï¿½todo que consulta los estados las solicitudes de una cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static HashMap consultarEstadosSolicitudesCita(Connection con,String codigoCita)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().consultarEstadosSolicitudesCita(con, codigoCita);
	}
	
	/**
	 * @return Retorna sexoPaciente.
	 */
	public int getSexoPaciente()
	{
		return sexoPaciente;
	}
	/**
	 * @param sexoPaciente Asigna sexoPaciente.
	 */
	public void setSexoPaciente(int sexoPaciente)
	{
		this.sexoPaciente = sexoPaciente;
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

	/**
	 * @return Returns the solPYP.
	 */
	public boolean isSolPYP() {
		return solPYP;
	}

	/**
	 * @param solPYP The solPYP to set.
	 */
	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String centroAtencion) {
		this.nombreCentroAtencion = centroAtencion;
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

	public int getCodigoServicioCita() {
		return codigoServicioCita;
	}

	public void setCodigoServicioCita(int codigoServicioCita) {
		this.codigoServicioCita = codigoServicioCita;
	}

	/**
	 * @return the mapaServicios
	 */
	public HashMap getMapaServicios() {
		return mapaServicios;
	}

	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(HashMap mapaServicios) {
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
	 * @return the codigoEspecialidadSolicitante
	 */
	public int getCodigoEspecialidadSolicitante() {
		return codigoEspecialidadSolicitante;
	}

	/**
	 * @param codigoEspecialidadSolicitante the codigoEspecialidadSolicitante to set
	 */
	public void setCodigoEspecialidadSolicitante(int codigoEspecialidadSolicitante) {
		this.codigoEspecialidadSolicitante = codigoEspecialidadSolicitante;
	}

	/**
	 * @return the cambioEstadoCita
	 */
	public boolean isCambioEstadoCita() {
		return cambioEstadoCita;
	}

	/**
	 * @param cambioEstadoCita the cambioEstadoCita to set
	 */
	public void setCambioEstadoCita(boolean cambioEstadoCita) {
		this.cambioEstadoCita = cambioEstadoCita;
	}

	/**
	 * @return the tieneSolicitudes
	 */
	public boolean isTieneSolicitudes() {
		return tieneSolicitudes;
	}

	/**
	 * @param tieneSolicitudes the tieneSolicitudes to set
	 */
	public void setTieneSolicitudes(boolean tieneSolicitudes) {
		this.tieneSolicitudes = tieneSolicitudes;
	}

	/**
	 * @return the cambioUnidadAgenda
	 */
	public boolean isCambioUnidadAgenda() {
		return cambioUnidadAgenda;
	}

	/**
	 * @param cambioUnidadAgenda the cambioUnidadAgenda to set
	 */
	public void setCambioUnidadAgenda(boolean cambioUnidadAgenda) {
		this.cambioUnidadAgenda = cambioUnidadAgenda;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public static String obtenerDescripcionEstadoCita(Connection con, int codigoEstadoCita) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().obtenerDescripcionEstadoCita(con, codigoEstadoCita);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public static String obtenerEstadoCita(Connection con, int codigoCita) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().obtenerEstadoCita(con, codigoCita);
	}
	
	/**
	 * Metodo encargado de consultar la peticion
	 * dependiendo del numero de solicitud
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public static String consultPetXNumSol (Connection connection, String numSol)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().consultPetXNumSol(connection, numSol);
	}
	
	/**
	 * Metodo encargado de eliminar un servicio de una cita.
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarServicioCita (Connection connection, String codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().eliminarServicioCita(connection, codigo);
	}

	/**
	 * @return the is_fechaSolicitada
	 */
	public String getIs_fechaSolicitada() {
		return is_fechaSolicitada;
	}

	/**
	 * @param is_fechaSolicitada the is_fechaSolicitada to set
	 */
	public void setIs_fechaSolicitada(String is_fechaSolicitada) {
		this.is_fechaSolicitada = is_fechaSolicitada;
	}
	
	/**
	 * Metodo encargado de verificar si un servicio indicado
	 * se encuentra en la tabla servicios_cita.
	 * @param codigoServicioCita
	 * @param connection
	 */
	public static String existeServicioEnCita (Connection connection, String codigoCita,String codigoServicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().existeServicioEnCita(connection, codigoCita,codigoServicio);
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
	 * Inserta la informacion del log de Reprogramacion de citas
	 * @param Connection con 
	 * @param String cita_reprogramada
	 * @param String fecha_reprogramacion
	 * @param String hora_reprogramacion
	 * @param String usuario_reprogramo
	 * @param String centro_atencion_anter
	 * @param String centro_atencion_nuevo
	 * @param String fecha_cita_anter
	 * @param String fecha_cita_nuevo
	 * @param String hora_cita_anter
	 * @param String hora_cita_nuevo
	 * @param String profesional_anter
	 * @param String profesional_nuevo
	 * @param String consultorio_anter
	 * @param String consultorio_nuevo
	 * @param String unidad_agenda_anter
	 * @param String unidad_agenda_nuevo
	 * */
	public static boolean guardarLogReprogramacionCita(Connection con,
			String cita_reprogramada,
			String fecha_reprogramacion,
			String hora_reprogramacion,
			String usuario_reprogramo,
			String centro_atencion_anter,
			String centro_atencion_nuevo,
			String fecha_cita_anter,
			String fecha_cita_nuevo,
			String hora_cita_anter,
			String hora_cita_nuevo,
			String profesional_anter,
			String profesional_nuevo,
			String consultorio_anter,
			String consultorio_nuevo,	
			String unidad_agenda_anter,
			String unidad_agenda_nuevo)
	{
		HashMap parametros = new HashMap();
		parametros.put("cita_reprogramada", cita_reprogramada);
		parametros.put("fecha_reprogramacion",fecha_reprogramacion);
		parametros.put("hora_reprogramacion",hora_reprogramacion);
		parametros.put("usuario_reprogramo",usuario_reprogramo);
		parametros.put("centro_atencion_anter",centro_atencion_anter);
		parametros.put("centro_atencion_nuevo",centro_atencion_nuevo);
		parametros.put("fecha_cita_anter",fecha_cita_anter);
		parametros.put("fecha_cita_nuevo",fecha_cita_nuevo);
		
		if(hora_cita_anter.toString().length() > 5)		
			parametros.put("hora_cita_anter",hora_cita_anter.substring(0,5));
		else
			parametros.put("hora_cita_anter",hora_cita_anter);
			
		if(hora_cita_nuevo.toString().length() > 5)
			parametros.put("hora_cita_nuevo",hora_cita_nuevo.substring(0,5));
		else
			parametros.put("hora_cita_nuevo",hora_cita_nuevo);
		
		parametros.put("profesional_anter",profesional_anter);
		parametros.put("profesional_nuevo",profesional_nuevo);
		parametros.put("consultorio_anter",consultorio_anter);
		parametros.put("consultorio_nuevo",consultorio_nuevo);
		parametros.put("unidad_agenda_anter",unidad_agenda_anter);
		parametros.put("unidad_agenda_nuevo",unidad_agenda_nuevo);				
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getCitaDao().guardarLogReprogramacionCita(con,parametros);
	}

	
	
	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param String codigosCitas
	 * @return
	 */
	public static HashMap getReportePdfBaseCita(Connection con,String codigosCitas, String codigoPaciente,String edad)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoCitas",codigosCitas);
		parametros.put("codigoPaciente",codigoPaciente);
		parametros.put("edad", edad);
		return citaDao.getReportePdfBaseCita(con,parametros);
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the prioridad
	 */
	public String getPrioridad() {
		return prioridad;
	}

	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
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

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	
	
	//***********************************************************************
	// Manejo de Multas por Incumplimiento de Citas
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param paciente
	 * @param cod_modulo
	 * @param parametrosModulo
	 * @param fechaIniciControlMultas
	 */
	public boolean validarEstadoCitasPaciente(Connection con, InstitucionBasica institucion, PersonaBasica paciente, UsuarioBasico usuario,
			int cod_modulo, String[] parametrosModulo){
		try
		{
			String manMultaIncCita = ConstantesBD.acronimoNo;
			String fechaIniControl = "";
			String bloqCitasRevAsgiReprog = ConstantesBD.acronimoNo;
			ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> array1 = new ArrayList<HashMap<String, Object>>();
			array1 = UtilidadesConsultaExterna.institucionManejaMultasIncumCitas(con,institucion.getCodigo(),cod_modulo,parametrosModulo);
			if(array1.size()>0)
			{
				// se verifica los parametros generales de la institucion para saber si se maneja 
				// multas por incumpimiento de citas, si se bloque la reserva, asignacion y reprogramacion de las citas
				// y la fecha inicial de control de multas
				//--------------------------------------
				for(HashMap elemento:array1)
				{
					if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento))
						manMultaIncCita = elemento.get("valor").toString();
					else if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump))
						bloqCitasRevAsgiReprog = elemento.get("valor").toString();
					else if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas))
						fechaIniControl = elemento.get("valor").toString();
				}
				//--------------------------------------
				
				if(!manMultaIncCita.equals(ConstantesBD.acronimoNo))
				{
					// si la institucion maneja multas por incumplimiento de citas
					if(!fechaIniControl.trim().equals(""))
					{
						// se carga las citas del paciente con estado no asistio y que la fecha de generacion de la cita
						// se mayor o igual a la fecha inicial de control de citas de la institucion
						ArrayList<HashMap<String, Object>> array2 = new ArrayList<HashMap<String, Object>>();
						array2 = UtilidadesConsultaExterna.estadoCitasPaciente(con, paciente.getCodigoPersona(), fechaIniControl,true);
						if(array2.size()>0)
						{
							if(!bloqCitasRevAsgiReprog.equals(ConstantesBD.acronimoNo))
							{
								// si el parametro de bloque de reservas, asignacion de citas y de reprogramancion se cumple
								for(HashMap elementocita:array2)
								{
									logger.info("valor de la solicitud"+elementocita.get("SolicitudSerCita").toString());
									if(!elementocita.get("SolicitudSerCita").toString().equals(ConstantesBD.codigoNuncaValido+""))
									{
										logger.info("hay numero de solicitud");
										if(!elementocita.get("convenioManejaMultIncCita").equals(ConstantesBD.acronimoNo))
										{
											//en el caso de que el convenio maneje multas por incumplimiento de citas
											// se adiciona al arraylist la cita incumplida 
											HashMap<String, Object> citaincumplida = new HashMap<String, Object>();
											citaincumplida.put("centroAtencion", elementocita.get("centroAtencion"));
											citaincumplida.put("nombreAgenda", elementocita.get("nombreAgenda"));
											citaincumplida.put("fechaCita", elementocita.get("fechaCita"));
											citaincumplida.put("horaInicioCita", elementocita.get("horaInicioCita"));
											citaincumplida.put("horaFinalCita", elementocita.get("horaFinalCita"));
											citaincumplida.put("nombreProfesional", elementocita.get("nombreProfesional"));
											citaincumplida.put("codConvenio", elementocita.get("codConvenio"));
											citaincumplida.put("convenioManejaMultIncCita", elementocita.get("convenioManejaMultIncCita"));
											citasIncumplidas.add(citaincumplida);
										}
									}else
									{
										logger.info("NO hay numero de solicitud");
										HashMap<String, Object> citaincumplida = new HashMap<String, Object>();
										citaincumplida.put("centroAtencion", elementocita.get("centroAtencion"));
										citaincumplida.put("nombreAgenda", elementocita.get("nombreAgenda"));
										citaincumplida.put("fechaCita", elementocita.get("fechaCita"));
										citaincumplida.put("horaInicioCita", elementocita.get("horaInicioCita"));
										citaincumplida.put("horaFinalCita", elementocita.get("horaFinalCita"));
										citaincumplida.put("nombreProfesional", elementocita.get("nombreProfesional"));
										citaincumplida.put("codConvenio", elementocita.get("codConvenio"));
										citaincumplida.put("convenioManejaMultIncCita", elementocita.get("convenioManejaMultIncCita"));
										citasIncumplidas.add(citaincumplida);
									}
								}
								
								if(citasIncumplidas.size()>0)
								{
									// se viculan todas las citas incumplidad listas para mostrar
									// y que pasaron los filtros
									this.setCitasIncumplidas(citasIncumplidas);
									this.setEstadoValidarCitasPaciente(true);
									this.setRealizadaVerificacion(true);
									return true;
								}
							}
						}
					}
				}
			}
		//logger.info("SE PERMITE RESERVAR, ASIGNAR Y/O REPROGRAMAR CITAS ASI HAYA CITAS INCUMPLIDAS\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("NO SE ENCONTRARON CITAS EN ESTADO NO ASISTIO CON O SIN MULTA\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("no existe la fecha inicial de control de multas por incumplimiento de citas");
		//logger.info("INSTITUCION NO MANEJA MULTAS!!!!!\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("no hay parametrizacion de la institucion en multas de incumplimientio de citas");
		this.setRealizadaVerificacion(true);
		this.setEstadoValidarCitasPaciente(false);
		return false;
		}catch(Exception e){
			logger.info("Ha ocurrido un error el la verificacion del estado de citas del paciente !!!!!! ");
			this.setRealizadaVerificacion(false);
			this.setEstadoValidarCitasPaciente(false);
			return false;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public static boolean esActividadAutorizada(Connection con, int unidadAgenda, int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen){
		return UtilidadesConsultaExterna.esActividadAurtorizada(con, unidadAgenda, actividad, usuario, centroAtencion, isUniAgenOrAgen);
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
	 * @return the mostrarCita
	 */
	public String getMostrarCita() {
		return mostrarCita;
	}

	/**
	 * @param mostrarCita the mostrarCita to set
	 */
	public void setMostrarCita(String mostrarCita) {
		this.mostrarCita = mostrarCita;
	}

	public int getCodigoMedicoXAgenda() {
		return codigoMedicoXAgenda;
	}

	public void setCodigoMedicoXAgenda(int codigoMedicoXAgenda) {
		this.codigoMedicoXAgenda = codigoMedicoXAgenda;
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

	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	
	
	//***********************************************************************
	
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

	
}