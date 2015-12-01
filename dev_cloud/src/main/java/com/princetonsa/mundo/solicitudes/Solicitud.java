/*
 * @(#)Solicitud.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Esta clase encapsula maneja los datos y funcionalidades comunes
 * a todas las solicitudes
 *
 * @version 1.0, Dic 15, 2003
 */

public class Solicitud
{
	/**
	* Fecha de respuesta de la solicitud. Si es vacía entonces la solicitud no ha sido respondida
	*/
	private String fechaRespuesta;
	
	/**
	 * Hora de la respuesta de la solicitud
	 */
	private String horaRespuesta;

	/** Código del paciente */
	private int ii_codigoPaciente;

	/**
	 * numero de la solicitud
	 */
	private int numeroSolicitud;

	/**
	 * fecha grabacion de la solicitud
	 */
	private String fechaGrabacion;

	/**
	 * hora grabacion de la solicitud
	 */
	private String horaGrabacion;

	/**
	 * fecha de solicitud
	 */
	private String fechaSolicitud;
	
	/**
	 * fecha de solicitud Date
	 */
	private Date fechaSolicitudDate;

	/**
	 * hora  de solicitud
	 */
	private String horaSolicitud;

	/**
	 * numero de autorizacion de la solicitud
	 */
	//private String numeroAutorizacion;
	/**
	 * consecutivo ordenes medicas
	 */
	private int consecutivoOrdenesMedicas;
	
	/**
	 * 
	 */
	private String ordenAmbulatoria="";
	
	/**
	 * 
	 */
	private String loginMedico="";

	/**
	 * codigo de cuenta a la cual hace parte la solicitud
	 */
	private int codigoCuenta;

	/**
	 * indicador de si es cobrable o no
	 */
	private boolean cobrable;

	/**
	 * texto de interpretacion de la solicitud
	 */
	private String interpretacion;

	/**
	 * fecha interpretacion de la solicitud
	 */
	private String fechaInterpretacion;

	/**
	 * hora interpretacion de la solicitud
	 */
	private String horaInterpretacion;

	/**
	 * indicador de si va o no a la epicrisis
	 */
	private boolean vaAEpicrisis;

	/**
	 * indicador de si es o no urgente
	 */
	private boolean urgente;

	/**
	 * motivo de la anulacion
	 */
	private String motivoAnulacion;

	/**
	 * fecha de la anulacion
	 */
	private String fechaAnulacion;

	/**
	 * hora de la anulacion
	 */
	private String horaAnulacion;

	/**
	 * medico que interpreta la solicitud
	 */
	private int  codigoMedicoInterpretacion;

	/**
	 * medico que realiza la solicitud
	 */
	private int codigoMedicoSolicitante;

	/**
	 * medico que anula la solicitud
	 */
	private int codigoMedicoAnulacion;

	/**
	 * Tipo de la solicitud
	 */
	private InfoDatosInt tipoSolicitud;

	/**
	 *especialidad del solicitante
	 */
	private InfoDatosInt especialidadSolicitante;

	/**
	 *ocupacion del solicitado
	 */
	private InfoDatosInt ocupacionSolicitado;

	/**
	 *centro de costo solicitante
	 */
	private InfoDatosInt centroCostoSolicitante;

	/**
	 *centro de costo de quien debe atender la solicitud
	 */
	private InfoDatosInt centroCostoSolicitado;

	/**
	 *estado Historia Clinica
	 */
	private InfoDatosInt estadoHistoriaClinica;

	/**
	 * Atributo que permite saber si ya se llenaron los
	 * datos necesarios para insertar solicitud de
	 * valoración inicial
	 */
	private boolean yaSeLlenoParaValoracionInicial;
	
	/**
	 * Atributo que indica si una solicitud tiene cita
	 */
	private boolean tieneCita;

	/**
	 * El DAO usado por el objeto <code>Solicitud</code> para acceder
	 * a la fuente de datos.
	 */
	private static SolicitudDao solicitudDao;
	
	static 
	{
		solicitudDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao();
	}

	/**
	 * nombre del paciente
	 *
	 */
	private String nombrePaciente;
	/**
	 * apellido del paciente
	 *
	 */
	private String apellidoPaciente;
	/**
	 * nombre del medico
	 *
	 */
	private String nombreMedico;
	/**
	 * apellido del medico
	 *
	 */
	private String apellidoMedico;

	/**
	 * nombre del medico Responde
	 *
	 */
	private String nombreMedicoResponde;
	/**
	 * apellido del medico Responde
	 *
	 */
	private String apellidoMedicoResponde;

	/**
	 *especialidad del solicitante
	 */
	private InfoDatosInt especialidadSolicitada;
	
	/**
	 * Almacenar los datos del médico que hizo la solicitud
	 */
	private String datosMedico;

	/**
	 * Almacenar los datos del medico que responde
	 */
	private String datosMedicoResponde;
	
	
	private String impresion;

	/**
	 * Vector para almacenar los atributos de la justificación
	 */
	@SuppressWarnings("rawtypes")
	private Vector justificacion;
	
	private int poolMedico;
	
	/**
	 * Maneja los logs del módulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(Solicitud.class);

	/**
	 * 
	 */
	private int codigoCentroAtencionCuentaSol;
	
	/**
	 * 
	 */
	private String nombreCentroAtencionCuentaSol;
	
	/**
	 * 
	 */
	private boolean solPYP=false;
	
	/**
	 * 
	 */
	private String cama;
	
	/**
	 * 
	 * */
	private String liquidarAsocio;
	
	/**
	 * Almacena el portatil asociado al procedimiento
	 */
	private String portatil=ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * Mapa con las justificaciones No Pos
	 */
	@SuppressWarnings("rawtypes")
	private HashMap justificacionesMap;
	
	/**
	 * 
	 */
	private String motivoAnulacionPort="";
	
	/**
	 * indica si incluye o no servicios o articulos
	 */
	private boolean incluyeServiciosArticulos;
	
	/**
	 * 
	 * */
	private String justificacionSolicitud;
	
	/**
	 * 
	 * */
	private String justificacionSolicitudNueva;
	
	/**
	 * Constructor vacio del objeto Solicitud
	 */
	
	private String entidadSubcontratada;
	
	/**
	 * almacena la especialidad solicitada para las ordenes ambulatorias
	 */
	private int especialidadSolicitadaOrdAmbulatorias;
	
	/**
	 * Objeto que almacena los datos del último diagnóstico relacionado a la solicitud
	 */
	private DtoDiagnostico dtoDiagnostico;
	
	
	/**
	 * 
	 */
	private int codigoMedicoResponde;
	
	public Solicitud ()
	{
		this.clean();
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			solicitudDao = myFactory.getSolicitudDao();
			wasInited = (solicitudDao != null);
		}
		return wasInited;
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int getCodigoTipoSolicitud(Connection con, String numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().getCodigoTipoSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Método para limpiar este objeto
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void clean()
	{
		ii_codigoPaciente = -1;
		numeroSolicitud=0;
		fechaRespuesta = "";
		fechaGrabacion="";
		horaGrabacion="";
		fechaSolicitud="";
		horaSolicitud="";
		//numeroAutorizacion="";
		consecutivoOrdenesMedicas=0;
		codigoCuenta=0;
		cobrable=true;
		interpretacion="";
		fechaInterpretacion="";
		horaInterpretacion="";
		vaAEpicrisis=true;
		urgente=true;
		motivoAnulacion="";
		fechaAnulacion="";
		horaAnulacion="";
		codigoMedicoInterpretacion=-1;
		codigoMedicoSolicitante=-1;
		codigoMedicoAnulacion=-1;
		especialidadSolicitante=new InfoDatosInt();
		especialidadSolicitada=new InfoDatosInt();
		ocupacionSolicitado=new InfoDatosInt();
		centroCostoSolicitante=new InfoDatosInt();
		centroCostoSolicitado=new InfoDatosInt();
		estadoHistoriaClinica=new InfoDatosInt();
		tipoSolicitud=new InfoDatosInt();
		yaSeLlenoParaValoracionInicial=false;
		datosMedico="";
		datosMedicoResponde="";
		justificacion=null;
		poolMedico=ConstantesBD.codigoNuncaValido;
		this.solPYP=false;
		this.codigoCentroAtencionCuentaSol=ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencionCuentaSol="";
		this.tieneCita = false;
		this.cama="";
		this.incluyeServiciosArticulos=false;
		this.liquidarAsocio = ConstantesBD.acronimoNo;
		this.motivoAnulacionPort="";
		this.portatil=ConstantesBD.codigoNuncaValido+"";
		this.justificacionesMap = new HashMap();
		this.justificacionesMap.put("numRegistros", "0");
		this.justificacionSolicitud = "";
		this.justificacionSolicitudNueva = "";
		this.especialidadSolicitadaOrdAmbulatorias = ConstantesBD.codigoNuncaValido;
		this.codigoMedicoResponde=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método que permite insertar una solicitud de valoración
	 * inicial (Asume que la fecha / hora de solicitud es la misma
	 * que la fecha de inserción, la esp. solicitante es todos porque
	 * en general este dato lo llena per. administrativo, no médico,
	 * Siempre debe rastrearse- Consecutivos Diferentes para
	 * casos 'otros' y al ser la inicial siempre va a la epicrisis y
	 * es urgente) . Es transaccional de forma atómica
	 *
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudValoracionInicial (Connection con) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (yaSeLlenoParaValoracionInicial)
		{

			if (solicitudDao==null)
			{
				this.init(System.getProperty("TIPOBD"));
			}
			if (solicitudDao==null)
			{
				throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao)");
			}

			//Iniciamos la transacción
			boolean inicioTrans=myFactory.beginTransaction(con);
			
			// this.numeroAutorizacion,
			// esta relacionado a la interfaz solicitudDao 
			resp1=solicitudDao.insertarSolicitudValoracionInicial(con, this.centroCostoSolicitante.getCodigo(), this.centroCostoSolicitado.getCodigo(), codigoCuenta, cobrable, this.tipoSolicitud.getCodigo());

			if (!inicioTrans||resp1<1)
			{
				myFactory.abortTransaction(con);
			}
			else
			{
				myFactory.endTransaction(con);
			}
			yaSeLlenoParaValoracionInicial=false;
			return resp1;

		}
		else
		{
			throw new Exception ("Los datos de la solicitud de valoración inicial deben ser llenados por el método llenarSolicitudValoracionInicial antes de realizar la inserción");
		}
	}

	/**
	 * Método que permite insertar una solicitud de valoración
	 * inicial (Asume que la fecha / hora de solicitud es la misma
	 * que la fecha de inserción, la esp. solicitante es todos porque
	 * en general este dato lo llena per. administrativo, no médico,
	 * Siempre debe rastrearse- Consecutivos Diferentes para
	 * casos 'otros' y al ser la inicial siempre va a la epicrisis y
	 * es urgente) . Permite definir su estado dentro de una
	 * transacción
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción (empezar,
	 * continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudValoracionInicialTransaccional (Connection con, String estado) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new Exception ("El estado de la transacción (Solicitud) no esta especificado");
		}

		if (yaSeLlenoParaValoracionInicial)
		{
			if (solicitudDao==null)
			{
				throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao)");
			}

			//Iniciamos la transacción, si el estado es empezar

			boolean inicioTrans;

			if (estado.equals("empezar"))
			{
				inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
				inicioTrans=true;
			}

			// this.numeroAutorizacion,
			// esta relacionado a la interfaz solicitudDao  
			resp1=solicitudDao.insertarSolicitudValoracionInicial(con, this.centroCostoSolicitante.getCodigo(), this.centroCostoSolicitado.getCodigo(), codigoCuenta, cobrable, this.tipoSolicitud.getCodigo());

			if (!inicioTrans||resp1<1)
			{
				myFactory.abortTransaction(con);
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
				}
			}
			yaSeLlenoParaValoracionInicial=false;
			return resp1;

		}
		else
		{
			myFactory.abortTransaction(con);
			throw new Exception ("Los datos de la solicitud de valoración inicial deben ser llenados por el método llenarSolicitudValoracionInicial antes de realizar la inserción");
		}
	}


	/**
	 * Método que llena todos los datos necesarios para insertar
	 * la solicitud de la valoración inicial
	 *
	 * @param numeroAutorizacion Número de autorización de esta
	 * Solicitud
	 * @param codigoCentroCostoSolicitante Código del centro de
	 * costo que hace esta solicitud
	 * @param codigoCentroCostoSolicitado Código del centro de
	 * costo que puede responder esta solicitud
	 * @param codigoCuenta Código de la cuenta a la que pertenece
	 * esta solicitud
	 * @param cobrable Define si esta solicitud se puede cobrar o no
	 */
	// quitar
	// String numeroAutorizacion, 
	public void llenarSolicitudValoracionInicial (InfoDatosInt centroCostoSolicitante, InfoDatosInt centroCostoSolicitado, int codigoCuenta, boolean cobrable, InfoDatosInt tipoSolicitud)
	{
		if (centroCostoSolicitante!=null&&centroCostoSolicitado!=null&&tipoSolicitud!=null)
		{
			//this.numeroAutorizacion=numeroAutorizacion;
			this.centroCostoSolicitante=centroCostoSolicitante;
			this.centroCostoSolicitado=centroCostoSolicitado;
			this.codigoCuenta=codigoCuenta;
			this.cobrable=cobrable;
			this.tipoSolicitud=tipoSolicitud;
			yaSeLlenoParaValoracionInicial=true;
		}
		else
		{
			yaSeLlenoParaValoracionInicial=false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCuenta
	 * @param codigoResponsable
	 * @param detAsocioCxSalasMat 
	 * @param detCxHonorarios 
	 * @param  
	 * @param temporalCodigoServicio
	 * @param string
	 * @param temporalCantidadServicio
	 * @return
	 */
	public static double insertarSolicitudSubCuenta(Connection con, int numeroSolicitud, int codigoCuenta, String codigoResponsable, String codigoServicio, String codigoArticulo, int cantidad, String cubierto,int tipoSolicitud, String codigoServicioCX, int codigoTipoAsocio,String tipoDistribucion,String usuario, int detCxHonorarios, int detAsocioCxSalasMat) throws IPSException 
	{
		DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
		
		try {
			dto.setCuenta(codigoCuenta+"");
			dto.setSubCuenta(codigoResponsable);
			dto.setNumeroSolicitud(numeroSolicitud+"");
			dto.setServicio(new InfoDatosString(codigoServicio));
			dto.setArticulo(new InfoDatosString(codigoArticulo));
			dto.setCantidad(cantidad+"");
			dto.setCubierto(cubierto);
			dto.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
			dto.setPaquetizada(ConstantesBD.acronimoNo);
			if(UtilidadTexto.isEmpty(tipoDistribucion))
				dto.setTipoDistribucion(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad));
			else
				dto.setTipoDistribucion(new InfoDatosString(tipoDistribucion));
			dto.setServicioCX(new InfoDatosString(codigoServicioCX+""));
			dto.setTipoAsocio(new InfoDatosInt(codigoTipoAsocio));
			dto.setUsuarioModifica(usuario);
			dto.setDetcxhonorarios(detCxHonorarios);
			dto.setDetasicxsalasmat(detAsocioCxSalasMat);
			return Solicitud.insertarSolicitudSubCuenta(con, dto, "continuar");
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * Método que inserta este objeto como solicitud general,
	 * soportando el parametro "estado" que define la
	 * transaccionalidad de esta operación
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción "empezar",
	 * "continuar" y "finalizar"
	 * @return
	 * @throws Exception
	 */
	public int insertarSolicitudGeneralTransaccional(Connection con, String estado) throws SQLException
	{
		logger.info("\n entre a insertarSolicitudGeneralTransaccional ");
		
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud) no esta especificado");
		}

		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao)");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		
		/*ArrayList<DtoEntidadSubcontratada> entidades =null;
		
		UsuarioBasico usuario=new UsuarioBasico();

		String tipoEntidad = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con, this.centroCostoSolicitado.getCodigo());
		
		
		
		if(tipoEntidad.equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			int codigo=this.centroCostoSolicitado.getCodigo();
			logger.info(""+this.centroCostoSolicitado.getCodigo());
			
			
			entidades = solicitudDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigo, usuario.getCodigoInstitucionInt());
						
		}  
		logger.info("\n\n\n ENTIDADES ----> "+entidades+"\n\n");
		for(int i=0;i<entidades.size();i++)
		{
			entidadSubcontratada=entidades.get(i).getConsecutivo();
		}
		logger.info("\n\n\n Centro de costo solicitado"+	this.centroCostoSolicitado.getCodigo());
		logger.info("\n\n\n entidadSubcontratada : "+entidadSubcontratada);*/
		// this.numeroAutorizacion,
		// esta relacionado a la interfaz solicitudDao 
		resp1=solicitudDao.insertarSolicitudGeneral(
													con, 
													this.fechaSolicitud, 
													this.horaSolicitud, 
													this.tipoSolicitud.getCodigo(), 
													this.cobrable, 													
													this.especialidadSolicitante.getCodigo(), 
													this.ocupacionSolicitado.getCodigo(), 
													this.centroCostoSolicitante.getCodigo(), 
													this.centroCostoSolicitado.getCodigo(), 
													this.codigoMedicoSolicitante, 
													this.codigoCuenta, 
													this.vaAEpicrisis, 
													this.urgente, 
													ConstantesBD.codigoEstadoHCSolicitada, 
													this.datosMedico,
													this.solPYP,
													this.tieneCita,
													this.getLiquidarAsocio(),
													this.justificacionSolicitud,
													this.especialidadSolicitadaOrdAmbulatorias,
													this.codigoMedicoResponde,
													this.dtoDiagnostico);
		
		if(this.solPYP)
		{
			//hacer lo específico de la solicitud de pyp
			
		}
		
		/* Cargar nuevamente los datos de la solicitud */
		if(resp1 > 0)
			cargar(con, resp1);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}

	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento).
	 * Maneja transaccionalidad especificando un parámetro
	 * estado (empezar, continuar y finalizar)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @param codigoTipoIdentificacionMedico Código del tipo de
	 * identificación del médico que será tratante
	 * @param numeroIdentificacionMedico Número de identificación
	 * del médico que será tratante
	 * @return
	 * @throws Exception
	 */
	public int insertarTratanteInicialTransaccional (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoMedico, String estado) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new Exception ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - insertarTratanteInicialTransaccional )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}

		resp1=solicitudDao.insertarTratante(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;

	}

	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento).
	 * Maneja Transaccionalidad de forma atómica
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @param codigoTipoIdentificacionMedico Código del tipo de
	 * identificación del médico que será tratante
	 * @param numeroIdentificacionMedico Número de identificación
	 * del médico que será tratante
	 * @return
	 * @throws Exception
	 */
	public int insertarTratanteInicial (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoMedico, boolean esTransaccional) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;

		if (solicitudDao==null)
		{
			this.init(System.getProperty("TIPOBD"));
		}
		if (solicitudDao==null)
		{
			throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao-InsertarMedicoTratanteInicial)");
		}

		//Iniciamos la transacción
		boolean inicioTrans=true;
		
		if(esTransaccional)
			inicioTrans = myFactory.beginTransaction(con);

		resp1=solicitudDao.insertarTratante(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico);

		if (!inicioTrans||resp1<1)
		{
			if(esTransaccional)
				myFactory.abortTransaction(con);
		}
		else
		{
			if(esTransaccional)
				myFactory.endTransaction(con);
		}

		return resp1;
	}

	/**
	/**
	 * Método que quita el anterior tratante (dejando un registro
	 * en la fuente de datos) y agrega el nuevo tratante. Maneja
	 * Transaccionalidad a través del parametro estado
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * que creo este nuevo tratante
	 * @param idCuenta Código de la cuenta del nuevo
	 * tratante
	 * @param codigoTipoIdentificacionMedico Código
	 * del tipo de identificación del médico que queda de
	 * tratante responsable
	 * @param numeroIdentificacionMedico Número de
	 * identificación del médico que queda de tratante
	 * responsable
	 * @param estado Estado de la transacción de la que
	 * hace parte esta operación de persistencia
	 * @return
	 * @throws Exception
	 */
	public int cambiarTratanteTransaccional (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, int codigoCentroCostoMedico, String estado) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0, resp2=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new Exception ("El estado de la transacción (Solicitud - cambiarTratanteTransaccional ) no esta especificado");
		}

			if (solicitudDao==null)
			{
				throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - cambiarTratanteTransaccional )");
			}

			//Iniciamos la transacción, si el estado es empezar

			boolean inicioTrans;

			if (estado.equals("empezar"))
			{
				inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
				inicioTrans=true;
			}

			resp1=solicitudDao.cerrarAntiguaEntradaTratante(con, idCuenta);
			resp2=solicitudDao.insertarTratante(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico);

			if (!inicioTrans||resp1<1||resp2<1)
			{
				myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
				}
			}
			return resp1;
	}

	/**
	 * Método que quita el anterior tratante (dejando un registro
	 * en la fuente de datos) y agrega el nuevo tratante. Maneja
	 * Transaccionalidad de forma atómica
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * que creo este nuevo tratante
	 * @param idCuenta Código de la cuenta del nuevo
	 * tratante
	 * @param codigoTipoIdentificacionMedico Código
	 * del tipo de identificación del médico que queda de
	 * tratante responsable
	 * @param numeroIdentificacionMedico Número de
	 * identificación del médico que queda de tratante
	 * responsable
	 * @return
	 * @throws Exception
	 */
	public int cambiarTratante (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, int codigoCentroCostoMedico) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0, resp2=0;

		if (solicitudDao==null)
		{
			this.init(System.getProperty("TIPOBD"));
		}
		if (solicitudDao==null)
		{
			throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao-cambiarTratante)");
		}

		//Iniciamos la transacción
		boolean inicioTrans=myFactory.beginTransaction(con);

		resp1=solicitudDao.cerrarAntiguaEntradaTratante(con, idCuenta);
		resp2=solicitudDao.insertarTratante(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico);

		if (!inicioTrans||resp1<1||resp2<1)
		{
			myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
			myFactory.endTransaction(con);
		}

		return resp1;
	}

	/**
	 * Método que dice si hay una solicitud de cambio de tratante
	 * activa en el sistema para una cuenta
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Cuenta en la que se quiere revisar
	 * si hay una solicitud de cambio de tratante activa
	 * @return
	 * @throws SQLException
	 */
	public boolean haySolicitudCambioTratantePrevia (Connection con) throws SQLException
	{
		return solicitudDao.haySolicitudCambioTratantePrevia (con, this.codigoCuenta) ;
	}

	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento)
	 * A diferencia de insertarTratante, este método inserta el
	 * tratante de urgencias, que a diferencia del resto NO maneja
	 * médico. Maneja transaccionalidad especificando un parámetro
	 * estado (empezar, continuar y finalizar)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @return
	 * @throws Exception
	 */
	public int insertarTratanteInicialUrgenciasTransaccional (Connection con, int numeroSolicitud, int area, String estado) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new Exception ("El estado de la transacción (Solicitud - insertarTratanteInicialUrgenciasTransaccional  ) no esta especificado");
		}

			if (solicitudDao==null)
			{
				throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - insertarTratanteInicialUrgenciasTransaccional  )");
			}

			//Iniciamos la transacción, si el estado es empezar

			boolean inicioTrans;

			if (estado.equals("empezar"))
			{
				inicioTrans=myFactory.beginTransaction(con);
			}
			else
			{
				inicioTrans=true;
			}

			resp1=solicitudDao.insertarTratanteUrgencias(con, numeroSolicitud,area);

			if (!inicioTrans||resp1<1)
			{
				myFactory.abortTransaction(con);
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
				}
			}
			return resp1;
	}

	/**
	 * Método que inserta un tratante (No revisa si el tratante
	 * ya está, simplemente inserta uno nuevo, el mundo debe
	 * encargarse de combinar los métodos de almacenamiento)
	 * A diferencia de insertarTratante, este método inserta el
	 * tratante de urgencias, que a diferencia del resto NO maneja
	 * médico. Maneja Transaccionalidad de forma atómica
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud con la que se
	 * solicita la inserción del tratante
	 * @return
	 * @throws Exception
	 */
	public int insertarTratanteInicialUrgencias (Connection con, int numeroSolicitud,int area) throws Exception
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;

		if (solicitudDao==null)
		{
			this.init(System.getProperty("TIPOBD"));
		}
		if (solicitudDao==null)
		{
			throw new Exception ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao-insertarTratanteInicialUrgencias)");
		}

		//Iniciamos la transacción
		boolean inicioTrans=myFactory.beginTransaction(con);

		resp1=solicitudDao.insertarTratanteUrgencias(con, numeroSolicitud,area);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			myFactory.endTransaction(con);
		}

		return resp1;
	}

	/**
	 * Método que crea un Adjunto (NO revisa repetidos)
	 * para el centro de costo solicitante y la cuenta de
	 * esta solicitud. Soporta el manejo del parametro
	 * especificando transaccionalidad
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro
	 * de una transacción más grande ("empezar", "continuar",
	 * "finalizar" )
	 * @return
	 * @throws Exception
	 */
	public int crearAdjuntoTransaccional (Connection con, int codigoCentroCostoSolicitante, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - insertarTratanteInicialTransaccional )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}

		resp1=solicitudDao.crearAdjuntoCuenta (con, this.numeroSolicitud, this.codigoMedicoSolicitante, codigoCentroCostoSolicitante);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}

	public ResultadoBoolean insertarAdjunto(Connection con, int numeroSolicitud, int codigoMedicoSolicitante, int codigoCentroCostoSolicitante)
	{
		try
		{
			int respuesta = solicitudDao.crearAdjuntoCuenta (con, numeroSolicitud, codigoMedicoSolicitante, codigoCentroCostoSolicitante);
			if( respuesta < 1 )
				return new ResultadoBoolean(false, "No se insertó el médico adjunto ");
			else
				return new ResultadoBoolean(true);
		}
		catch( SQLException sqe )
		{
			return new ResultadoBoolean(false, "Problemas en la base de datos insertando un médico adjunto "+sqe.getMessage());
		}
	}

	/**
	 * Método que cambia la solicitud de tratante para una cuenta
	 * en particular.
	 * Se debe encargar de cancelar la anterior entradada (debe quedar
	 * inactiva) y manejar los dos casos especificados en Anexo35.
	 * Solicitud de Interconsulta (Se haya respondido la solicitud que
	 * genero la petición de nuevo tratante o no). Debe soportar los
	 * métodos transaccionales.
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción (empezar, continuar
	 * o finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarSolicitudMedicoTratante (Connection con, String estado) throws SQLException
	{
		return solicitudDao.cambiarSolicitudMedicoTratante(con, this.numeroSolicitud, this.codigoCuenta, this.codigoMedicoSolicitante, estado);
	}

	
	/**
	 * Metodo que carga todas las solicitudes de una cuenta, y retorna una colleccion de solicitudes
	 * @armando
	 * @param con, conexion
	 * @param cuenta, cuenta que se desea consultar
	 * @return  collection, datos de la consulta.
	 */
	
		 
	@SuppressWarnings("rawtypes")
	public Collection cargarSolicitudesInternas(Connection con,int cuenta)
	{
		return solicitudDao.cargarSolicitudesInternas(con,cuenta);	
	}
	
	
	/**
	 * Método para cargar una solicitud dado su
	 * código
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Código de la solicitud a
	 * cargar
	 * @return
	 * @throws SQLException
	 */
	public boolean cargar (Connection con, int numeroSolicitud) throws SQLException
	{
		this.numeroSolicitud=numeroSolicitud;
		ResultSetDecorator rs=solicitudDao.cargarSolicitud(con, numeroSolicitud);
		if (rs.next())
		{
			this.fechaGrabacion=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaGrabacion") );
			if(!UtilidadTexto.isEmpty(rs.getString("horaGrabacion")) && 
					rs.getString("horaGrabacion").length()>=5	){
			this.horaGrabacion=rs.getString("horaGrabacion").substring(0, 5);
			}
			this.fechaSolicitud=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaSolicitud") );				
			this.horaSolicitud=rs.getString("horaSolicitud").substring(0, 5);				
			this.tipoSolicitud.setCodigo(rs.getInt("codigoTipoSolicitud"));
			this.tipoSolicitud.setNombre(rs.getString("tipoSolicitud"));
			
			//this.numeroAutorizacion=rs.getString("numeroAutorizacion");
			this.especialidadSolicitante.setCodigo(rs.getInt("codigoEspecialidadSolicitante"));
			this.especialidadSolicitante.setNombre(rs.getString("especialidadSolicitante"));
			this.ocupacionSolicitado.setCodigo(rs.getInt("codigoOcupacionSolicitada"));
			this.ocupacionSolicitado.setNombre(rs.getString("ocupacionSolicitada"));
			
			this.centroCostoSolicitado.setCodigo(rs.getInt("codigoCentroCostoSolicitado"));
			this.centroCostoSolicitado.setNombre(rs.getString("centroCostoSolicitado"));
			this.centroCostoSolicitante.setCodigo(rs.getInt("codigoCentroCostoSolicitante"));
			this.centroCostoSolicitante.setNombre(rs.getString("centroCostoSolicitante"));
			this.codigoMedicoSolicitante=rs.getInt("codigoMedico");
			this.consecutivoOrdenesMedicas=rs.getInt("consecutivoOrdenesMedicas");
			
			this.codigoCuenta=rs.getInt("cuenta");
			this.cobrable=rs.getBoolean("cobrable");

			this.interpretacion=rs.getString("interpretacion");
			this.fechaInterpretacion=rs.getString("fechaInterpretacion");
			this.horaInterpretacion=rs.getString("horaInterpretacion");
			this.codigoMedicoInterpretacion=rs.getInt("codigoMedicoInterpretacion");
			this.vaAEpicrisis=rs.getBoolean("vaEpicrisis");
			this.urgente=rs.getBoolean("urgente");
			this.estadoHistoriaClinica.setCodigo(rs.getInt("codigoEstadoHistoriaClinica"));
			this.estadoHistoriaClinica.setNombre(rs.getString("estadoHistoriaClinica"));
			this.setDatosMedico(rs.getString("datosMedico"));
			this.setDatosMedicoResponde(rs.getString("datosMedicoResponde"));
			this.setNombreMedicoResponde(rs.getString("nombreMedicoResponde"));
			this.setLiquidarAsocio(rs.getString("liquidarAsocio"));
			this.setSolPYP(rs.getBoolean("pyp"));			
			this.justificacionSolicitud = rs.getString("justificacion_solicitud");
			this.especialidadSolicitadaOrdAmbulatorias = rs.getInt("especialidad_solicitada");
			logger.info("especialidadSolicitadaOrdAmbulatorias"+especialidadSolicitadaOrdAmbulatorias);
			//this.setCama(rs.getString("cama"));
			
			if (this.estadoHistoriaClinica.getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
			{
				this.cargarMotivoAnulacionInterconsulta(con, this.numeroSolicitud);
			}
			return true;
		}
		else
		{
            return false;
		}

	}

	/**
	 * @return
	 */
	public InfoDatosInt getCentroCostoSolicitado()
	{
		return centroCostoSolicitado;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getCentroCostoSolicitante()
	{
		return centroCostoSolicitante;
	}

	/**
	 * @return
	 */
	public boolean isCobrable()
	{
		return cobrable;
	}

	/**
	 * @return
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}

	/**
	 * @return
	 */
	public int getConsecutivoOrdenesMedicas()
	{
		return consecutivoOrdenesMedicas;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getEspecialidadSolicitante()
	{
		return especialidadSolicitante;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getEstadoHistoriaClinica()
	{
		return estadoHistoriaClinica;
	}

	/**
	 * @return
	 */
	public String getFechaAnulacion()
	{
		return fechaAnulacion;
	}

	/**
	 * @return
	 */
	public String getFechaGrabacion()
	{
		return fechaGrabacion;
	}

	/**
	 * @return
	 */
	public String getFechaInterpretacion()
	{
		return fechaInterpretacion;
	}

	/**
	 * @return
	 */
	public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}

	/**
	 * @return
	 */
	public String getHoraAnulacion()
	{
		return horaAnulacion;
	}

	/**
	 * @return
	 */
	public String getHoraGrabacion()
	{
		return horaGrabacion;
	}

	/**
	 * @return
	 */
	public String getHoraInterpretacion()
	{
		return horaInterpretacion;
	}

	/**
	 * @return
	 */
	public String getHoraSolicitud()
	{
		return horaSolicitud;
	}

	/**
	 * @return
	 */
	public String getInterpretacion()
	{
		return interpretacion;
	}

	/**
	 * @return
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @return
	 */
	/*
	public String getNumeroAutorizacion()
	{
		return numeroAutorizacion;
	}
	*/
	/**
	 * @return
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getOcupacionSolicitado()
	{
		return ocupacionSolicitado;
	}

	/**
	 * @return
	 */
	public boolean isUrgente()
	{
		return urgente;
	}

	/**
	 * @return
	 */
	public boolean getUrgente()
	{
		return urgente;
	}

	/**
	 * @return
	 */
	public boolean isVaAEpicrisis()
	{
		return vaAEpicrisis;
	}

	
	/**
	 * @param int1
	 */
	public void setCentroCostoSolicitado(InfoDatosInt int1)
	{
		centroCostoSolicitado = int1;
	}
	
	public void setNombreCentroCostoSolicitado(String nombre)
	{
		this.centroCostoSolicitado.setNombre(nombre);
	}
	
	public String getNombreCentroCostoSolicitado()
	{
		return this.centroCostoSolicitado.getNombre();
	}

	/**
	 * @param int1
	 */
	public void setCentroCostoSolicitante(InfoDatosInt int1)
	{
		centroCostoSolicitante = int1;
	}

	/**
	 * @param b
	 */
	public void setCobrable(boolean b)
	{
		cobrable = b;
	}

	/**
	 * @param i
	 */
	public void setCodigoCuenta(int i)
	{
		codigoCuenta = i;
	}

	/**
	 * @param i
	 */
	public void setConsecutivoOrdenesMedicas(int i)
	{
		consecutivoOrdenesMedicas = i;
	}

	/**
	 * @param int1
	 */
	public void setEspecialidadSolicitante(InfoDatosInt int1)
	{
		especialidadSolicitante = int1;
	}


	/**
	 * @param int1
	 */
	public void setEstadoHistoriaClinica(InfoDatosInt int1)
	{
		estadoHistoriaClinica = int1;
	}

	/**
	 * @param string
	 */
	public void setFechaAnulacion(String string)
	{
		fechaAnulacion = string;
	}

	/**
	 * @param string
	 */
	public void setFechaGrabacion(String string)
	{
		fechaGrabacion = string;
	}

	/**
	 * @param string
	 */
	public void setFechaInterpretacion(String string)
	{
		fechaInterpretacion = string;
	}

	/**
	 * @param string
	 */
	public void setFechaSolicitud(String string)
	{
		fechaSolicitud = string;
	}

	/**
	 * @param string
	 */
	public void setHoraAnulacion(String string)
	{
		horaAnulacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraGrabacion(String string)
	{
		horaGrabacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraInterpretacion(String string)
	{
		horaInterpretacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraSolicitud(String string)
	{
		horaSolicitud = string;
	}

	/**
	 * @param string
	 */
	public void setInterpretacion(String string)
	{
		interpretacion = string;
	}

	/**
	 * @param string
	 */
	public void setMotivoAnulacion(String string)
	{
		motivoAnulacion = string;
	}

	/**
	 * @param string
	 */
	/*
	public void setNumeroAutorizacion(String string)
	{
		numeroAutorizacion = string;
	}
	*/
	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i)
	{
		numeroSolicitud = i;
	}

	/**
	 * @param int1
	 */
	public void setOcupacionSolicitado(InfoDatosInt int1)
	{
		ocupacionSolicitado = int1;
	}

	/**
	 * @param b
	 */
	public void setUrgente(boolean b)
	{
		urgente = b;
	}

	/**
	 * @param b
	 */
	public void setVaAEpicrisis(boolean b)
	{
		vaAEpicrisis = b;
	}
	/**
	 * @return (Metodo GET) Returns the impresion.
	 */
	public String getImpresion()
	{
		return impresion;
	}
	/**
	 * @param impresion The impresion to set (Metodo SET).
	 */
	public void setImpresion(String impresion)
	{
		this.impresion= impresion;
	}
	/**
	 * @return
	 */
	public InfoDatosInt getTipoSolicitud()
	{
		return tipoSolicitud;
	}

	/**
	 * @param int1
	 */
	public void setTipoSolicitud(InfoDatosInt int1)
	{
		tipoSolicitud = int1;
	}

	/**
	 * @return
	 */
	public int getCodigoMedicoAnulacion() {
		return codigoMedicoAnulacion;
	}

	/**
	 * @return
	 */
	public int getCodigoMedicoInterpretacion() {
		return codigoMedicoInterpretacion;
	}

	/**
	 * @return
	 */
	public int getCodigoMedicoSolicitante() {
		return codigoMedicoSolicitante;
	}

	/**
	 * @param i
	 */
	public void setCodigoMedicoAnulacion(int i) {
		codigoMedicoAnulacion = i;
	}

	/**
	 * @param i
	 */
	public void setCodigoMedicoInterpretacion(int i) {
		codigoMedicoInterpretacion = i;
	}

	/**
	 * @param i
	 */
	public void setCodigoMedicoSolicitante(int i) {
		codigoMedicoSolicitante = i;
	}

	/**
	 * @return
	 */
	public String getApellidoMedico() {
		return apellidoMedico;
	}

	/**
	 * @return
	 */
	public String getApellidoPaciente() {
		return apellidoPaciente;
	}

	/**
	 * @return
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @return
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param string
	 */
	public void setApellidoMedico(String string) {
		apellidoMedico = string;
	}

	/**
	 * @param string
	 */
	public void setApellidoPaciente(String string) {
		apellidoPaciente = string;
	}

	/**
	 * @param string
	 */
	public void setNombreMedico(String string) {
		nombreMedico = string;
	}

	/**
	 * @param string
	 */
	public void setNombrePaciente(String string) {
		nombrePaciente = string;
	}

	/**
	 * @return
	 */
	public InfoDatosInt getEspecialidadSolicitada() {
		return especialidadSolicitada;
	}

	/**
	 * @param int1
	 */
	public void setEspecialidadSolicitada(InfoDatosInt int1) {
		especialidadSolicitada = int1;
	}
	/**
	 * Interpretar una solicitud
	 * @param int1
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int interpretarSolicitud(Connection con,int codigoMedico, String interpretacion) 
	{
		//Primero debemos interpretar las evoluciones asociadas (puede
		//que no exista ninguna
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		SolicitudEvolucion sol=new SolicitudEvolucion ();

		UtilidadBD.iniciarTransaccion(con);

		try
		{
			//No está dentro del try porque el mismo maneja sus transacciones
			sol.interpretarSolicitudesEvolucionDadaValoracionTransaccional(con, this.numeroSolicitud, "empezar");

		
			//-Iniciar el Bloqueo 	
            ArrayList filtro=new ArrayList();
           	filtro.add(this.numeroSolicitud+"");
			if ( UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitud,filtro) )
			{
				int resp=solicitudDao.interpretarSolicitud(con,this.numeroSolicitud,codigoMedico,interpretacion);

				if (resp!=0)
				{
					myFactory.endTransaction(con);
					return resp;
				}
				else
				{
					myFactory.abortTransaction(con);
					throw new SQLException ("Error en intrepretación solicitud");
				}
			}	
			else
			{
				myFactory.abortTransaction(con);
				logger.error("Error al tratar de Bloquear la tabla");
				return -1;
			} 
		}
		catch (SQLException e)
		{
			UtilidadBD.abortarTransaccion(con);
			logger.error("No se pudo interpretar la solicitud. Excepción " + e);
			return -1;
		}
	}
	
	/**
	 * Método que inserta la interpretacion de una solicitud de procedimientos,
	 * se diferencia del otro método de que la fecha/hora interpretacion se mandan como parámetros
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int interpretarSolicitud(Connection con,HashMap campos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().interpretarSolicitud(con,campos);
	}
	
	/**
	 * Método implementado para insertar una interpertacion de una solicitud mandándole los parámetros
	 * de manera seprada y con llamado estático
	 * @param con
	 * @param interpretacion
	 * @param codigoMedico
	 * @param fechaInterpretacion
	 * @param horaInterpretacion
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int interpretarSolicitud(Connection con,String interpretacion,int codigoMedico,String fechaInterpretacion,String horaInterpretacion,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("interpretacion", interpretacion);
		campos.put("codigoMedico", codigoMedico);
		campos.put("fechaInterpretacion", fechaInterpretacion);
		campos.put("horaInterpretacion", horaInterpretacion);
		campos.put("numeroSolicitud", numeroSolicitud);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().interpretarSolicitud(con,campos);
	}

	/**
	 * Interpretar una solicitud que este en estadoHC respondida dado el codigo de la valoracion
	 * @param 
	 */
	public ResultadoBoolean interpretarSolicitudInterconsulta(Connection con,String interpretacion, UsuarioBasico user) throws SQLException
	{
		//Primero debemos interpretar las evoluciones asociadas (puede
		//que no exista ninguna
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		interpretacion=UtilidadCadena.cargarObservaciones(interpretacion,"",user);
		SolicitudEvolucion sol=new SolicitudEvolucion ();
		//No está dentro del try porque el mismo maneja sus transacciones
		sol.interpretarSolicitudesEvolucionDadaValoracionYEstadoHCTransaccional(con, this.numeroSolicitud, ConstantesBD.continuarTransaccion);
		ResultadoBoolean respRB=new ResultadoBoolean(false);
		
		try
		{
		    int resp=solicitudDao.interpretarSolicitud(con,this.numeroSolicitud,user.getCodigoPersona(),interpretacion);
			if (resp>0)
			{
				respRB.setResultado(true);
				respRB.setDescripcion("INTERPRETACION EXITOSA");
				return respRB;
			}
			else
			{
				myFactory.abortTransaction(con);
			}
		}
		catch (SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.error("No se pudo interpretar la solicitud. Excepción " + e);
		}
		return respRB;
	}
	
	/**
	 * Modificar la interpretacion de una solicitud
	 *
	 */
	public int modificarInterpretacionSolicitud(Connection con, String interpretacion) throws SQLException{
		return solicitudDao.modificarInterpretacionSolicitud(con,this.numeroSolicitud,interpretacion);
	}

	public ResultadoBoolean inactivarSolicitudCambioTratante(Connection con, int numeroSolicitud)
	{
		return solicitudDao.inactivarSolicitudCambioTratante(con, numeroSolicitud);
	}

	/**
	 * Método que inserta un tratante, teniendo cuidado de desactivar
	 * todos los anteriores (más no borrar) todos las entradas que
	 * definan otro/s centro de costo tratante
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de Solicitud que inserta este
	 * tratante
	 * @param codigoMedico
	 * @return
	 */
	public ResultadoBoolean insertarTratante(Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoMedico,boolean esTransaccional)
	{
		try
		{
			int resultado2 = solicitudDao.cerrarAntiguaEntradaTratanteDadaSolicitud(con, numeroSolicitud);
			int resultado = this.insertarTratanteInicial(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico, esTransaccional);

			if( resultado < 1 ||resultado2<1)
				return new ResultadoBoolean(false, "No se pudo insertar el tratante ");
			else
				return new ResultadoBoolean(true);
		}
		catch(Exception e)
		{
			return new ResultadoBoolean(false, "No se pudo insertar el tratante. Problemas en la base de datos :  "+e.getMessage());
		}
	}

	/**
	 * Método que inserta un tratante, sin desactivar
	 * todos los anteriores (más no borrar) todos las entradas que
	 * definan otro/s centro de costo tratante porque no existen (Ej,. caso consulta externa que no existe tratante)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de Solicitud que inserta este
	 * tratante
	 * @param codigoMedico
	 * @return
	 */
	public ResultadoBoolean insertarTratante2(Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoMedico,boolean esTransaccional)
	{
		try
		{
			solicitudDao.cerrarAntiguaEntradaTratanteDadaSolicitud(con, numeroSolicitud);
			int resultado = this.insertarTratanteInicial(con, numeroSolicitud, codigoMedico, codigoCentroCostoMedico, esTransaccional);

			if( resultado < 1 )
				return new ResultadoBoolean(false, "No se pudo insertar el tratante ");
			else
				return new ResultadoBoolean(true);
		}
		catch(Exception e)
		{
			return new ResultadoBoolean(false, "No se pudo insertar el tratante. Problemas en la base de datos :  "+e.getMessage());
		}
	}

	/**
	 * Método que anula una solicitud, manejando los estados
	 * de transacción (empezar, continuar, finalizar)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estados de la transaccion (empezar,
	 * continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int anularSolicitudTransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - insertarTratanteInicialTransaccional )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar")){
			inicioTrans=myFactory.beginTransaction(con);
		}else{
			inicioTrans=true;
		}

		//-Iniciar el Bloqueo 	
        ArrayList filtro=new ArrayList();
       	filtro.add(this.numeroSolicitud+"");
		if ( UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitud,filtro) )
			
		{
			try { ///-Try para el bloque de las transacciones.
				
				resp1=solicitudDao.anularSolicitud(con, this.numeroSolicitud, this.motivoAnulacion, this.codigoMedicoAnulacion);
				
			} catch (SQLException ex) {
				
				if ( Integer.parseInt(ex.getSQLState().toString()) == 23505 ){
				 return -1;
				}
			}
			
			if (!inicioTrans||resp1<1){
				myFactory.abortTransaction(con);
			}else{
				if (estado.equals("finalizar")){
					myFactory.endTransaction(con);
				}
			}
		}else{
		}
		return resp1;
	}

	/**
	 * Método que finaliza la atención conjunta de un centro
	 * de costo adjunto. Soporta definir el estado dentro
	 * de una transacción en que se encuentra esta operación
	 * (empezar, continuar, finalizar)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Código de la cuenta a la
	 * que se va a quitar el centro de costo adjunto
	 * @param codigoCentroCostoAdjunto Código
	 * del centro de costo que hasta ahora era adjunto
	 * @param estado Identifica en que parte de una
	 * transacción se encuentra este método
	 * (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int finalizarAtencionConjuntaTransaccional (Connection con, int codigoCuenta, int codigoCentroCostoAdjunto, String notaFinalizacion, int codigoValoracionAsociada, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - insertarTratanteInicialTransaccional )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}

		resp1=solicitudDao.finalizarAtencionConjunta(con, codigoCuenta, codigoCentroCostoAdjunto, notaFinalizacion);
		//No importa si no hay update porque puede ser que 
		//estemos asociando a algo sin interconsulta - Ej la inicial
		if(resp1!=ConstantesBD.codigoNuncaValido)
			SolicitudInterconsulta.actualizarFlagMostrarInterconsulta(con, resp1, true);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}

	/**
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de solicitud
	 * @param idMedico Código del médico
	 * @param detalleMedico Detalle de especialidades
	 * y demás datos en un momento particular
	 * @param estado Estado en la transacción
	 * @return
	 * @throws SQLException
	 */
	public int actualizarMedicoRespondeTransaccional (Connection con, int numeroSolicitud, UsuarioBasico medico, String estado) throws SQLException
	{
		logger.info("entre a actualizarMedicoRespondeTransaccional");
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - actualizarMedicoRespondeTransaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - actualizarMedicoRespondeTransaccional )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		String datosMedico=UtilidadTexto.agregarTextoAObservacion(null, null, medico, false);

		resp1=solicitudDao.actualizarMedicoResponde(con,numeroSolicitud, medico.getCodigoPersona(), datosMedico);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}
	
	
	public int actualizarEspecialidadTransaccional (Connection con, int numeroSolicitud, int codEspecialidad, String estado) throws SQLException
	{
		logger.info("entre a actualizarMedicoRespondeTransaccional");
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - actualizarEspecialidadTansaccional ) no esta especificado");
		}
		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao - actualizarEspecialidad )");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		
		resp1=solicitudDao.actualizarEspecialidadProfResponde(con, numeroSolicitud, codEspecialidad);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}
	
	
	
	/**
	 * Método estático para actualizar el médico que responde en una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param medico
	 * @return
	 */
	public static int actualizarMedicoResponde(Connection con, int numeroSolicitud, UsuarioBasico medico)
	{
		String datosMedico=UtilidadTexto.agregarTextoAObservacion(null, null, medico, false);
		int resp = 0;

		try 
		{
			resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarMedicoResponde(con,numeroSolicitud, medico.getCodigoPersona(), datosMedico);
		} 
		catch (SQLException e) 
		{
			logger.error("Error en actualizarMedicoResponde: "+e);
			resp = 0;
		} 
		return resp;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codEspecialidad
	 * @return
	 */
	public static int actualizarEspecialidadProfResponde(Connection con, int numeroSolicitud, int codEspecialidad)
	{
		int resp=0;
		try 
		{
			resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarEspecialidadProfResponde(con,numeroSolicitud,codEspecialidad);
		} 
		catch (SQLException e) 
		{
			logger.error("Error en actualizar Especialidad Profesional que responde: "+e);
			resp = 0;
		} 
		return resp;
 
		
	}
	
	

	/**
	 * Método para cargar el motivo de una anulación de interconsulta
	 *
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Código de la solicitud a cargar
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarMotivoAnulacionInterconsulta(Connection con, int numeroSolicitud)throws SQLException
	{
			ResultSetDecorator rs=solicitudDao.cargarMotivoAnulacionSolicitudInterconsulta(con,numeroSolicitud);

			if(rs.next())
			{
					this.setMotivoAnulacion(rs.getString("motivo"));
					this.setCodigoMedicoAnulacion(rs.getInt("codigo_medico"));
					if(rs.getDate("fecha") != null) {
						this.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fecha")));
					}
					this.setHoraAnulacion(rs.getString("hora"));
					return true;
			}
			else
			{
					return false;
			}
	}


	/**
	* Obtiene la fecha de respuesta de la solicitud. Si es vacía la solicitud no ha sido
	* respondida
	*/
	public String getFechaRespuesta()
	{
		return fechaRespuesta;
	}

	/** Estable la fecha de respuesta de la solicitud */
	public void setFechaRespuesta(String as_fecha)
	{
		if(as_fecha != null)
		fechaRespuesta = as_fecha.trim();
	}

	/** Obtiene el código del paciente */
	public int getCodigoPaciente()
	{
		return ii_codigoPaciente;
	}

	/** Establece el código del paciente */
	public void setCodigoPaciente(int ai_codigoPaciente)
	{
		ii_codigoPaciente = ai_codigoPaciente < 0 ? -1: ai_codigoPaciente;
	}

	/**
	 * para cambiar los estados de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param estadoHistoriaClinica
	 * @return ResultadoBoolean
	 */
   public ResultadoBoolean cambiarEstadosSolicitud(Connection con, int numeroSolicitud, int estadoFacturacion, int estadoHistoriaClinica)
   {
	   return solicitudDao.cambiarEstadosSolicitud(con, numeroSolicitud, estadoFacturacion, estadoHistoriaClinica);
   }
   
	/**
	 * para cambiar los estados de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param estadoFacturacion
	 * @param estadoHistoriaClinica
	 * @return ResultadoBoolean
	 */
  public static ResultadoBoolean cambiarEstadosSolicitudStatico(Connection con, int numeroSolicitud, int estadoFacturacion, int estadoHistoriaClinica)
  {
	   return solicitudDao.cambiarEstadosSolicitud(con, numeroSolicitud, estadoFacturacion, estadoHistoriaClinica);
  }

	/**
	 * para cambiar los estados de la solicitud dentro de una transaccion
	 * @param con
	 * @param numeroSolicitud
	 * @param estadoFacturacion
	 * @param estadoHistoriaClinica
	 * @param estado
	 * @return
	 */
	public ResultadoBoolean cambiarEstadosSolicitudTransaccional(Connection con, int numeroSolicitud, int estadoFacturacion, int estadoHistoriaClinica, String estado)
	{
		return solicitudDao.cambiarEstadosSolicitudTransaccional(con, numeroSolicitud, estadoFacturacion, estadoHistoriaClinica, estado);
	}

	/**
	 * Método que realiza los cambios necesarios en caso de aceptar
	 * ser tratante en caso de asocio. En el futuro, si es necesario que
	 * la cuenta no se mueva, se puede crear una solicitud anulada
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroSolicitud Número de la solicitud
	 * @param idCuentaDestino
	 * @return
	 * @throws SQLException
	 */
	public boolean cambiosRequeridosAceptacionTratanteCasoAsocio (Connection con, int numeroSolicitud, int idCuentaDestino) throws SQLException
	{
		return solicitudDao.cambiosRequeridosAceptacionTratanteCasoAsocio (con, numeroSolicitud, idCuentaDestino);
	}

	/**
	 * Retorna el número de autorización de la solicitud
	 * @param con
	 * @return ResultadoCollectionDBcon el número de Autorización,
	 * si hay algun error, devuelve la colección vacía la descripción del error
	 */
	/*
	public ResultadoCollectionDB cargarNumeroAutorizacion(Connection con, int numeroSolicitud){
		return solicitudDao.cargarNumeroAutorizacion(con, numeroSolicitud);
	}
	*/
	/**
	 * Método para obtener el numero de autorizacion estático
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	/*
	public static String cargarNumeroAutorizacionEstatico(Connection con, int numeroSolicitud)
	{
		String numeroAutorizacion = "";
		ResultadoCollectionDB resultado = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().cargarNumeroAutorizacion(con, numeroSolicitud);
		
		Iterator iterador = resultado.getFilasRespuesta().iterator();
		
		while(iterador.hasNext())
		{
			HashMap elemento = (HashMap)iterador.next();
			numeroAutorizacion = elemento.get("numeroautorizacion").toString();
		}
		
		return numeroAutorizacion;
	}
	*/
	/**
	 * Actualiza el numero de autorizacion
	 * @param con
	 * @param numeroAutorizacion
	 * @param estado
	 * @return
	 */
	/*public ResultadoBoolean actualizarNumeroAutorizacionTransaccional(Connection con, String numeroAutorizacion, int numeroSolicitud, String estado)
	{
		return solicitudDao.actualizarNumeroAutorizacionTransaccional(con, numeroSolicitud,numeroAutorizacion, estado);
	}*/	
	/**
	 * Método estático para actualizar el numero de autorización de una solicitud
	 * @param con
	 * @param numeroAutorizacion
	 * @param numeroSolicitud
	 * @return
	 */
	/*public static ResultadoBoolean actualizarNumeroAutorizacion(Connection con, String numeroAutorizacion, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarNumeroAutorizacionTransaccional(con, numeroSolicitud,numeroAutorizacion, ConstantesBD.continuarTransaccion);
	}*/	
	/**
	 * Método para actualizar la fecha y la hora de la solicitud
	 * @param con Conección con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param fecha Fecha actualizada
	 * @param hora Hora actualizada
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación 
	 */
	public int actualizarFechaHoraTransaccional(Connection con, int numeroSolicitud, String fecha, String hora, String estado)
	{
		return solicitudDao.actualizarFechaHoraTransaccional(con, numeroSolicitud, fecha, hora, estado);
	}


	public void modificarEpicrisis(Connection con, boolean estado) throws SQLException{
		solicitudDao.modificarEpicrisis(con,numeroSolicitud,estado);
	}

	public ResultadoBoolean cambiarCentroCostoSolicitado(Connection con, int centroCostoSolicitado)
	{
		return solicitudDao.cambiarCentroCostoSolicitado(con, this.numeroSolicitud, centroCostoSolicitado);
	}
	
	public ResultadoBoolean cambiarCentroCostoSolicitante(Connection con, int centroCostoSolicitante)
	{
		return solicitudDao.cambiarCentroCostoSolicitante(con, this.numeroSolicitud, centroCostoSolicitante);
	}

	/**
	 * Método que inserta este objeto como solicitud general,
	 * soportando el parametro "estado" que define la
	 * transaccionalidad de esta operación
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción "empezar",
	 * "continuar" y "finalizar"
	 * @return
	 * @throws Exception
	 */
	public int insertarSolicitudTransaccional(Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud) no esta especificado");
		}

		if (solicitudDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (SolicitudDao)");
		}

		//Iniciamos la transacción, si el estado es empezar

		boolean inicioTrans;

		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}

		// this.numeroAutorizacion,
		// este metodo esta relacionado en la interfaz solicitud
		resp1=solicitudDao.insertarSolicitudGeneral(
				con, 
				this.fechaSolicitud, 
				this.horaSolicitud, 
				this.tipoSolicitud.getCodigo(), 
				this.cobrable, 				 
				this.especialidadSolicitante.getCodigo(), 
				this.ocupacionSolicitado.getCodigo(), 
				this.centroCostoSolicitante.getCodigo(), 
				this.centroCostoSolicitado.getCodigo(), 
				this.codigoMedicoSolicitante, 
				this.codigoCuenta, 
				this.vaAEpicrisis, 
				this.urgente, 
				this.estadoHistoriaClinica.getCodigo(), 
				this.datosMedico,
				this.solPYP, 
				this.tieneCita,
				this.getLiquidarAsocio(),
				this.justificacionSolicitud,
				this.especialidadSolicitadaOrdAmbulatorias,
				this.codigoMedicoResponde,
				this.dtoDiagnostico);

		/* Cargar nuevamente los datos de la solicitud */
		if(resp1 > 0)
			cargar(con, resp1);

		if (!inicioTrans||resp1<1)
		{
			myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
				myFactory.endTransaction(con);
			}
		}
		return resp1;
	}
	
	/**
	 * Método que inserta una solicitud subcuenta.,
	 * soportando el parametro "estado" que define la
	 * transaccionalidad de esta operación
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de la transacción "empezar",
	 * "continuar" y "finalizar"
	 * @return
	 * @throws Exception
	 */
	public static int insertarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dto,String estado) throws IPSException
	{
		int resp1=0;
		
		try {
			if (estado==null)
			{
				UtilidadBD.abortarTransaccion(con);
				Log4JManager.error("El estado de la transacción (Solicitud) no esta especificado");
			}
	
			SolicitudDao solDao=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getSolicitudDao();
	
			boolean inicioTrans;
	
			if (estado.equals("empezar"))
			{
				inicioTrans=UtilidadBD.iniciarTransaccion(con);
			}
			else
			{
				inicioTrans=true;
			}
	
			resp1=solDao.insertarSolicitudSubCuenta(con,dto);
	
			if (!estado.equals("continuar"))
			{
				if (!inicioTrans||resp1<1)
				{
					UtilidadBD.abortarTransaccion(con);
				}
				else
				{
					if (estado.equals("finalizar"))
					{
						UtilidadBD.finalizarTransaccion(con);
					}
				}
			}
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		Log4JManager.info("*************     resp1 ---->"+resp1);
		return resp1;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoSolicitudesSubCuenta
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta)
	{
		return (DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getSolicitudDao().eliminarSolicitudSubCuenta(con, dtoSolicitudesSubCuenta);
	}
	
	/**
	 * Método que mueve / copia todo lo relacionado con solicitudes 
	 * (solicitudes adjuntos y tratantes) cuando ocurre un asocio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param cuentaOrigen Cuenta de la que se sacan los datos
	 * (cuenta de urgencias)
	 * @param cuentaDestino Cuenta en la que se ponen los datos
	 * (cuenta de hospitalización)
	 * @param institución del médico tratante
	 * @return
	 * @throws SQLException
	 */
	public int moverPorAsocio (Connection con, int cuentaOrigen, int cuentaDestino, int codigoMedicoTratante, int codigoCentroCostoMedico, String estado, int institucion) throws SQLException
	{
		int resultadoMovimiento=0;
		if (estado==null)
		{
			logger.error("El estado de la transacción no puede ser nulo (moverPorAsocio - Solicitud)");
			throw new SQLException ("El estado de la transacción no puede ser nulo (moverPorAsocio - Solicitud)");
		}
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (estado.equals("empezar"))
		{
			myFactory.beginTransaction(con);
		}

		resultadoMovimiento=solicitudDao.moverPorAsocio (con, cuentaOrigen, cuentaDestino, codigoMedicoTratante, codigoCentroCostoMedico, institucion) ;

		if (resultadoMovimiento<=0)
		{
			myFactory.abortTransaction(con);
			throw new SQLException("No se pudieron mover las solicitudes por asocio");
		}
		else if (estado.equals("finalizar"))
		{
			myFactory.endTransaction(con);
		}
		
		return resultadoMovimiento;
	}
	
	/**
	 * Método para actualizar la prioridad de la solicitud
	 * @param con Conección con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param urgente Prioridad (true --> urgente)
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación
	 */
	public int actualizarPrioridadUrgenteTransaccional(Connection con, int numeroSolicitud, boolean urgente, String estado)
	{
		return solicitudDao.actualizarPrioridadUrgenteTransaccional(con, numeroSolicitud, urgente, estado);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCentroCostoSolicitado(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerCodigoCentroCostoSolicitado(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCentroCostoSolicitante(Connection con, String numeroSolicitud) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerCodigoCentroCostoSolicitante(con, numeroSolicitud);
	}
	
	/**
	 * @return Retorna horaRespuesta.
	 */
	public String getHoraRespuesta()
	{
		return horaRespuesta;
	}
	/**
	 * @param horaRespuesta Asigna horaRespuesta.
	 */
	public void setHoraRespuesta(String horaRespuesta)
	{
		this.horaRespuesta = horaRespuesta;
	}
	/**
	 * @return Retorna datosMedico.
	 */
	public String getDatosMedico()
	{
		return datosMedico;
	}
	
	/**
	 * @return Retorna datosMedico.
	 */
	public String getDatosMedicoResponde()
	{
		return datosMedicoResponde;
	}
	
	
	/**
	 * @param datosMedicoResponde Asigna datosMedicoResponde.
	 */
	public void setDatosMedicoResponde(String datosMedicoResponde)
	{
		this.datosMedicoResponde= datosMedicoResponde;
	}
	/**
	 * @param datosMedico Asigna datosMedico.
	 */
	public void setDatosMedico(String datosMedico)
	{
		this.datosMedico = datosMedico;
	}

	/**
	 * @return Returns the apellidoMedicoResponde.
	 */
	public String getApellidoMedicoResponde() {
		return apellidoMedicoResponde;
	}
	/**
	 * @param apellidoMedicoResponde The apellidoMedicoResponde to set.
	 */
	public void setApellidoMedicoResponde(String apellidoMedicoResponde) {
		this.apellidoMedicoResponde = apellidoMedicoResponde;
	}
	/**
	 * @return Returns the nombreMedicoResponde.
	 */
	public String getNombreMedicoResponde() {
		return nombreMedicoResponde;
	}
	/**
	 * @param nombreMedicoResponde The nombreMedicoResponde to set.
	 */
	public void setNombreMedicoResponde(String nombreMedicoResponde) {
		this.nombreMedicoResponde = nombreMedicoResponde;
	}
	/**
	 * @return Retorna justificacion.
	 */
	@SuppressWarnings("rawtypes")
	public Vector getJustificacion()
	{
		return justificacion;
	}
	/**
	 * @param justificacion Asigna justificacion.
	 */
	@SuppressWarnings("rawtypes")
	public void setJustificacion(Vector justificacion)
	{
		this.justificacion = justificacion;
	}

	/**
	 * Metodo para cargar los procedimientos con su codgio, descripcion, especialidad, 
	 * cantidad y si es pos segun el numero de Documento que reciba
	 * @param con
	 * @param codigoServicioSolicitado
	 * @param atributo
	 * @param descripcion
	 * @param estado Estado de la transacción
	 * @return entero con el número de inserciones
	 */
	public int ingresarAtributoTransaccional(Connection con, int codigoServicioSolicitado, int atributo, String descripcion, String estado)
	{
		return solicitudDao.ingresarAtributoTransaccional(con, this.numeroSolicitud, codigoServicioSolicitado, atributo, descripcion, estado);
	}

	/**
	 * Método que actualiza el tipo de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudValoracionGinecoObstetrica
	 * @return Numero de registros modificados
	 */
	public static int actualizarTipoSolicitud(Connection con, int numeroSolicitud, int tipoSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarTipoSolicitud(con, numeroSolicitud, tipoSolicitud);
	}
	/**
	 * @return Returns the poolMedico.
	 */
	public int getPoolMedico() {
		return poolMedico;
	}
	/**
	 * @param poolMedico The poolMedico to set.
	 */
	public void setPoolMedico(int poolMedico) {
		this.poolMedico = poolMedico;
	}

	/**
	 * @param con
	 * @param numeroSolicitud2
	 * @param i
	 */
	public int actualizarPoolSolicitud(Connection con, int numeroSolicitud, int pool) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarPoolSolicitud(con, numeroSolicitud, pool);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap obteneInfoSolicitudInterfaz(Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obteneInfoSolicitudInterfaz(con, numeroSolicitud);
	}
	
	/**
	 * Método que actualiza el indicativo pyp de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param pyp
	 * @return
	 */
	public static int actualizarIndicativoPYP(Connection con,String numeroSolicitud,boolean pyp)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarIndicativoPYP(con,numeroSolicitud,pyp);
	}

	
	/**
	 * Actualiza el valor de liquidar Asocios de la tabla Solicitudes
	 * @param Connection con
	 * @param int NumeroSolicitud
	 * @param String liqudarAsocios
	 * */
	public  boolean cambiarLiquidacionAsociosSolicitud(Connection con, int numeroSolicitud, String liquidarAsocios)	
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().cambiarLiquidacionAsociosSolicitud(con, numeroSolicitud, liquidarAsocios);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerFirmaDigitalMedico(int numeroSolicitud)
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerFirmaDigitalMedico(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * metodo que actualiza el numero de orden de medicamentos con diferente dosificacion
	 * @param con
	 * @param vectorNumeroSolicitudes
	 * @return
	 */
	public static boolean actualizarNumeroOrdenMedDiferenteDosificacion(Connection con, Vector<String> vectorNumeroSolicitudes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarNumeroOrdenMedDiferenteDosificacion(con, vectorNumeroSolicitudes);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerInfoMedicoSolicita(int numeroSolicitud)
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerInfoMedicoSolicita(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	/**
	 * Actualiza la informacion de la solicitud
	 * @param Connection con
	 * @param String justificacionSolicitud
	 * @param String numeroSolicitud 
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean actualizarJustificacionSolicitud(Connection con,String justificacionSolicitud,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("justificacionSolicitud",justificacionSolicitud);
		parametros.put("numeroSolicitud",numeroSolicitud);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().actualizarInfoSolicitud(con, parametros);
	}
	
	public boolean isSolPYP() {
		return solPYP;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public int getCodigoCentroAtencionCuentaSol() {
		return codigoCentroAtencionCuentaSol;
	}

	public void setCodigoCentroAtencionCuentaSol(
			int codigoCentroAtencionCuentaSol) {
		this.codigoCentroAtencionCuentaSol = codigoCentroAtencionCuentaSol;
	}

	public String getNombreCentroAtencionCuentaSol() {
		return nombreCentroAtencionCuentaSol;
	}

	public void setNombreCentroAtencionCuentaSol(
			String nombreCentroAtencionCuentaSol) {
		this.nombreCentroAtencionCuentaSol = nombreCentroAtencionCuentaSol;
	}

	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public String getLoginMedico() {
		return loginMedico;
	}

	public void setLoginMedico(String loginMedico) {
		this.loginMedico = loginMedico;
	}

	/**
	 * @return the tieneCita
	 */
	public boolean isTieneCita() {
		return tieneCita;
	}

	/**
	 * @param tieneCita the tieneCita to set
	 */
	public void setTieneCita(boolean tieneCita) {
		this.tieneCita = tieneCita;
	}

	public String getCama() {
		return cama;
	}

	public void setCama(String cama) {
		this.cama = cama;
	}

	/**
	 * @return the liquidarAsocio
	 */
	public String getLiquidarAsocio() {		
		if(UtilidadTexto.isEmpty(this.liquidarAsocio))
			return ConstantesBD.acronimoNo;
		return liquidarAsocio;
	}

	/**
	 * @param liquidarAsocio the liquidarAsocio to set
	 */
	public void setLiquidarAsocio(String liquidarAsocio) {
		this.liquidarAsocio = liquidarAsocio;
	}

	public String getPortatil() {
		return portatil;
	}

	public void setPortatil(String portatil) {
		this.portatil = portatil;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public int getPortatilInt() 
	{
		return Utilidades.convertirAEntero(portatil);
	}

	public String getMotivoAnulacionPort() {
		return motivoAnulacionPort;
	}

	public void setMotivoAnulacionPort(String motivoAnulacionPort) {
		this.motivoAnulacionPort = motivoAnulacionPort;
	}

	/**
	 * Metodo para consultar el Valor del Parametro General de Validar Registro Evoluciones
	 * @param con
	 * @return
	 */
	public static String consultarParametroEvoluciones(Connection con) 
	{
		return solicitudDao.consultarParametroEvoluciones(con);
	}

	/**
	 * @return the justificacionesMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getJustificacionesMap() {
		return justificacionesMap;
	}

	/**
	 * @param justificacionesMap the justificacionesMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setJustificacionesMap(HashMap justificacionesMap) {
		this.justificacionesMap = justificacionesMap;
	}

	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean isIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}

	/**
	 * @param incluyeServiciosArticulos the incluyeServiciosArticulos to set
	 */
	public void setIncluyeServiciosArticulos(boolean incluyeServiciosArticulos) {
		this.incluyeServiciosArticulos = incluyeServiciosArticulos;
	}
	
	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean getIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}

	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	/**
	 * 
	 * @param numerosSolicitudes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Vector<String> obtenerConsecutivosOrdenesMedicas(
			HashMap numerosSolicitudes) 
	{
		Vector<String> vector= new Vector<String>();
		for(int w=0; w<Utilidades.convertirAEntero(numerosSolicitudes.get("numRegistros")+"");w++)
		{
			vector.add(Utilidades.getConsecutivoOrdenesMedicas(Utilidades.convertirAEntero(numerosSolicitudes.get("numeroSolicitud_"+w)+""))+"");
		}
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public static String obtenerLoginMedicoSolicita(Connection con, int numeroSolicitud, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerLoginMedicoSolicita(con, numeroSolicitud, institucion);
	}	
	
	/**
	 * Método para actualizar los centros de costo de medicamentos x despachar
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarCentrosCostoMedicamentosXDespachar(Connection con,int idCuenta,int codigoCentroCostoSolicitante,int codigoCentroCostoSolicitado)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("idCuenta", idCuenta);
		campos.put("codigoCentroCostoSolicitante", codigoCentroCostoSolicitante);
		campos.put("codigoCentroCostoSolicitado", codigoCentroCostoSolicitado);
		return solicitudDao.actualizarCentrosCostoMedicamentosXDespachar(con, campos);
	}

	public String getJustificacionSolicitud() {
		return justificacionSolicitud;
	}

	public void setJustificacionSolicitud(String justificacionSolicitud) {
		this.justificacionSolicitud = justificacionSolicitud;
	}

	public String getJustificacionSolicitudNueva() {
		return justificacionSolicitudNueva;
	}

	public void setJustificacionSolicitudNueva(String justificacionSolicitudNueva) {
		this.justificacionSolicitudNueva = justificacionSolicitudNueva;
	}

	/**
	 * @return the especialidadSolicitadaOrdAmbulatorias
	 */
	public int getEspecialidadSolicitadaOrdAmbulatorias() {
		return especialidadSolicitadaOrdAmbulatorias;
	}

	/**
	 * @param especialidadSolicitadaOrdAmbulatorias the especialidadSolicitadaOrdAmbulatorias to set
	 */
	public void setEspecialidadSolicitadaOrdAmbulatorias(
			int especialidadSolicitadaOrdAmbulatorias) {
		this.especialidadSolicitadaOrdAmbulatorias = especialidadSolicitadaOrdAmbulatorias;
	}
	
	// Cambios Segun Anexo 809
	/**
	  * Obtener la especilidad solicitada de un solicitud
	  * @param Connection con
	  * @param HashMap parametros
	  * @return HashMap<String, Object>
	  */
	public static HashMap<String, Object> obtenerEspecilidadSolicitada(Connection con, String numeroSolicitud)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("numero_solicitud", numeroSolicitud);
		return solicitudDao.obtenerEspecilidadSolicitada(con, campos);
	}
	// Fin Cambios Segun Anexo 809

	public int getCodigoMedicoResponde() {
		return codigoMedicoResponde;
	}

	public void setCodigoMedicoResponde(int codigoMedicoResponde) {
		this.codigoMedicoResponde = codigoMedicoResponde;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoDiagnostico
	
	 * @return retorna la variable dtoDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoDiagnostico
	
	 * @param valor para el atributo dtoDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}
	
	/**
	 * M&eacute;todo encargado de buscar los articulos asociados a 
	 * una solicitud de medicamentos generada desde un cargo directo de 
	 * articulos
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoArticulos> buscarArticulosCargosDirectosArticulos (Connection conn,
			int numeroSolicitud){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().buscarArticulosCargosDirectosArticulos(conn, numeroSolicitud);
	}
	
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerInterpretacionSolicitud(int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerInterpretacionSolicitud(numeroSolicitud);
	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCita(Connection con, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerCodigoCita(con,numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerEspecilidadSolicitadaCita(Connection con,int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().obtenerEspecilidadSolicitadaCita(con,numeroSolicitud);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return si es tipo de solicitud de valoracion consulta externa
	 * @throws Exception
	 */
	public static Boolean esConsultaExterna(Connection con,int numeroSolicitud) throws Exception 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudDao().esConsultaExterna(con,numeroSolicitud);
	}
	
	/**
	 * Método finaliza o aborta la transaccion para la anulacion de la solicitud, manejando los estados
	 * de transacción (empezar, continuar, finalizar)
	 *
	 * @param con Conexión con la fuente de datos
	 * @param estado Estados de la transaccion (empezar,
	 * continuar, finalizar)
	 * @throws SQLException
	 */
	public void cerrarTransaccionAnularSolicitudTransaccional(Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (Solicitud - insertarTratanteInicialTransaccional ) no esta especificado");
		}

		if (estado.equals("finalizar")){
			myFactory.endTransaction(con);
		}else{
			myFactory.abortTransaction(con);
		}
	}

	public Date getFechaSolicitudDate() {
		return fechaSolicitudDate;
	}

	public void setFechaSolicitudDate(Date fechaSolicitudDate) {
		this.fechaSolicitudDate = fechaSolicitudDate;
	}	
}