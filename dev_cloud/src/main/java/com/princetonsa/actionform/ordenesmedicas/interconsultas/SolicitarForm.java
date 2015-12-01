/*

 * @(#)SolicitarForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */
package com.princetonsa.actionform.ordenesmedicas.interconsultas;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase encapsula el la información necesaria para
 * trabajar con una Solicitud General, antes de pasar a la
 * opción específica
 *
 * @version 1.0, Feb 10, 2004
 */
public class SolicitarForm extends ValidatorForm
{ 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón
	 */
	private boolean mostrarImprimirAutorizacion;
	
	/**
	 * lista que contiene los nombres de los reportes de las autorzaciones 
	 * 
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Atributo que contiene el diagnóstico y tipo CIE del paciente
	 */
	private DtoDiagnostico dtoDiagnostico;
	
	/**
	 * Atributo que almacena el tipoCIE del diagnostico
	 * de la solicitud de medicamentos
	 */
	private String tipoCieDiagnostico;
	
	/**
	 * Atributo que almacena el acronimo del 
	 * diagnostico de la solicitud de medicamentos
	 */
	private String acronimoDiagnostico;
	
	//---RQF 02-0025-AUTORIZACION CAPITACION SUB---------------------------------------------------
	/**Atributo que almacena el valor si el paciente aplica para Autorización de Orden Ambulatoria*/
	private boolean ordenAmbulPendienteAutoriz;
	/**Lista que almacena los servicios que se asignaron en la cita*/
	private ArrayList<String> listaServiciosImpimirOrden;
	
    /**Variable que indica si existe autorizacion asociada a la orden entonces se debe 
     * postular el centro de costo de la autorizacion*/
    private boolean existeAutorizacionOrden;
    /**Alamacena el centro de costo de la autorizacion con su respectivo código*/
	private DtoCheckBox centroCostoAutorizacion;
	/**Variable que indica si se debe mostrar el boton de generar la solicitud*/
	private boolean botonGenerarSolicitud;
  //-----------------------------------------------------------------------------------------------
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SolicitarForm.class);
	
	/**
	 * Guarda las justificaciones de los servicios
	 */
	private HashMap<String, Object> justificacionesServicios;
	

	
	/**
	 * Para el manejo de resumen de atenciones
	 */
	private String encabezadoResumen;

	/**
	 * bandera que indica si se puede guardar la interpretacion solicitud
	 */
	private String checkEpicrisis;
	/**
	 * bandera que indica si se puede guardar la interpretacion solicitud
	 */
	private boolean guardarInterpretacionSolicitud;
	/**
	 * bandera que indica si se puede interpretar la solicitud
	 */
	private boolean interpretar;
	
	/**
	 * Variable que indica si la solicitud proviene de fichaEpidemiologica
	 */
	private boolean fichaEpidemiologica=false;

	/**
	 * Mapa para almacenar los datos múltiples, Ej,
	 * servicios
	 */
	private HashMap<String, Object> valores = new HashMap<String, Object>();
	
	
	/**
	 * 
	 */
	private boolean validarEgresoPaciente=false;
	

	/**
	 * Caracter para almacenar el código del tipo de la
	 * solicitud manejado
	 */
	private char codigoTipoSolicitud;
    
    /**
     * Int para almacenar el código del tipo de la
     * solicitud manejado
     */
    private int codigoTipoSolicitudInt;
    
    /**
     * 
     */
    private int posEliminar;
    
	/**
	 * almacena la interpretacion de la solicitud si existe
	 */
	private String ultimaInterpretacion;
	/**
	 * almacena la interpretacion de la solicitud si existe
	 */
	private int consecutivoOrdenesMedicas;
	/**
	 * String para manejar el estado en que se encuentra
	 * el flujo de Solicitud
	 */
	private String estado=null;
	
	private boolean servicioAmbUrgente;
	/**
	 * 
	 */
	private boolean indicativoOrdenAmbulatoria=false;

	/**
	 * Entero para manejar el número de esta solicitud
	 */
	private int numeroSolicitud;
	
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

	/**
	 * String para manejar la fecha de esta solicitud
	 */
	private String fechaSolicitud=null;
	
	/**
	 * String para manejar la fecha solicitud tipo Date
	 */
	private Date fechaSolicitudDate=null;

	/**
	 * String para manejar la hora de esta solicitud
	 */
	private String horaSolicitud=null;

	/**
	 * String que contendrá en
	 */
	private String fechaHoraValoracionInicial[ ];

	/**
	 * String para manejar la justificacion de una solicitud
	 */
	private String justificacionSolicitud ="";
	
	/**
	 * String para manejar la justificacion de una solicitud
	 */
	private String justificacionSolicitudNueva ="";

	/**
	 * Entero para manejar el código de la especialidad
	 * solicitante de esta solicitud
	 */
	private int especialidadSolicitante;

	/**
	 * La especialidad  que puede responder esta solicitud
	 */
	private int especialidadSolicitada;

	/**
	 * El centro de costo que está realizando esta solicitud
	 */
	private int centroCostoSolicitado;
	
	/**
	 * Descripcion del centro de costos que esta realizando la solicitud
	 * */
	private String descripcionCentroCostoSolicitado;

	/**
	 * El centro de costo que podrá responder esta solicitud
	 */
	private int ocupacionSolicitada;

	/**
	 * Indicativo de urgencia para esta solicitud
	 */
	private boolean urgenteOtros;

	/**
	 * String para manejar el nombre de un servicio, si el
	 * usuario selecciona la opción otros
	 */
	private String otroServicio=null;

	/**
	 * String para saber si la solicitud de va a realizar con
	 * un dato existente en la fuente o por el contrario a
	 * través de la opción otros
	 */
	private String seleccionarServicio=null;
	
	/**
	 * 
	 */
	private boolean requiereJustificacionServicio=false;
	
	/**
	 * Variable que me dice si estoy insertando otra solicitud o si es la primera.
	 */
	private boolean otraSolicitud;

	/**
	 * String con el comentario para esta solicitud
	 */
	private String comentario=null;

	private String nombreTipoSolicitud=null;
	private String labelUrgente=null;
	private String nombrePaciente=null;
	private String nombreMedico=null;
	private String nombreMedicoResponde=null;
	private String nombreEspecialidadSolicitante=null;
	private String nombreEspecialidadSolicitada=null;
	private String enlace=null;
	private String fechaRespuesta=null;
	private String interpretacion=null;
	private boolean vaEpicrisis;
	private boolean interpretada;
	
	private String ordenAmbulatoria="";
	
	private String fechaOrdenAmb="";

	/**
	 *  Entero que permite saber el número de servicios que
	 *  hay en el mapa
	 */
	private int numeroServicios;

	/**
	 * Entero que permite definir cuantos servicios como
	 * máximo puede tener esta solicitud
	 */
	private int numeroServiciosMaximo;

	/** Nombre del centro de costo solicitado */
	private String is_centroCostoSolicitado;

	/** Nombre del centro de costo solicitante */
	private String is_centroCostoSolicitante;

	/** Fecha de grabación de la solicitud */
	private String is_fechaGrabacion;

	/** Hora de grabación de la solicitud */
	private String is_horaGrabacion;

	/** Nombre de la ocupación solicitada */
	private String is_ocupacionSolicitada;

	/** Comentario adicional a la solicitud */
	private String is_comentarioAdicional;

	/** Indicador de anulación de la solicitud */
	private boolean ib_anularSolicitud;

	/** Motivo de la anulación de la solicitud */
	private String is_motivoAnulacion;

	/** Etado de facturación de la solicitud */
	private InfoDatosInt iidi_estadoFacturacion;

	/** Estado de historia clínica de la solicitud */
	private InfoDatosInt iidi_estadoHistoriaClinica;
	
	private final String directorioImagenes = System.getProperty("directorioImagenes");
	
	/**
	 * boolean que indica si se esta o no en el resumen de atenciones
	 */
	private boolean esResumenAtenciones=false;
	
	/**
	 * 	Variables para solicitudes multiples   
	 **/
	private boolean multiple;
	private boolean finalizar;
	private double frecuencia;
	private int tipoFrecuencia;
	
	/**
	 * Datos referentes al campo finalidad servicio
	 * 
	 */
	private int finalidad;
	private String nombreFinalidad="";
	
	private boolean unicamenteExternos;
	
	/**
	 * 
	 */
	private boolean esSolExterno;
	
	/**
	 * Centro de atencion por el cual se trabajara en las solicitudes
	 */
	private int centroAtencion=ConstantesBD.codigoNuncaValido;
	
	/**
	 * 
	 */
	private String centrosAtencionPYP="";
	
	/**
	 * boolean que indica si se debe postular un servicio
	 */
	private boolean postularServicio;
	
	/**
	 * codigo del servicio a postular
	 */
	private String codigoServicioPostular;
	
	/**
	 * indicativo que indica si la solicitud es de pyp.
	 */
	private boolean solPYP=false;
	
	/**
	 * 
	 */
	private String cantidadServicioOrdenAmbulatoria="1";
	/**
	 * 
	 */
	private int codigoCentroAtencionCuentaSol;
	
	/**
	 * 
	 */
	private String nombreCentroAtencionCuentaSol;
	
	
	/**
	 * variable que me indica desde que accion estoy llamando la funcionalidad (ejecutar - solictar - programar)
	 */
	private String accionPYP="";
	
	/**
	 * 
	 */
	private String cama="";

	/**
	 * atributo que me indica (PARA EL LISTADO) si existe o no articulos servicios incluidos
	 */
	private boolean incluyeServiciosArticulos;
	
	/**
	 * 
	 */
	private String codigoEnfermedadEpidemiologia="";
	
	///////////////////////////////////////////////////////////////////////
	//portatil
	private String portatil=ConstantesBD.codigoNuncaValido+"";
	
	private boolean portatilClone=false;
	
	private boolean capaMotivoAnulacion=false;
	///////////////////////////////////////////////////////////////////////
	
	/**
	 * servicios incluidos
	 */
	private HashMap<Object, Object> serviciosIncluidos;
	
	/**
	 * articulos incluidos
	 */
	private HashMap<Object, Object> articulosIncluidos;
	
	/**
	 * Dto de articulos incluidos solicitud de procedimiento
	 * */
	private ArrayList <DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto;	
	
	/**
	 * Variable para saber si son requeridos los comentarios generales
	 */
	private boolean requeridoComentarios;
		
	/**
	 * 
	 * */
	private String hiddens;
	

	/**
	 * HashMap de Justificacion de los articulos Incluidos
	 * */
	private HashMap<String, Object> justificacionMap;
	
	/**
     * Números Solicitudes para generar el reporte en Formato Media Carta
     */
    private HashMap<String, Object> numerosSolicitudes = new HashMap<String, Object>();
    
    private HashMap<String, Object> valAnulacion = new HashMap<String, Object>();
    
    /**
     * 
     */
    private Vector<String> consecutivosOrdenesMedicas= new Vector<String>();
    
    /**
     * mapa que contiene los mensajes de advertencia  
     * 
     */
    @SuppressWarnings("rawtypes")
	private HashMap mensajesAdvertenciaMap= new HashMap();
    
    
    /**
	 * Atributo que indica si se debe guardar registro de alerta en enfermeria
	 */
	private String generaAlertaEnfermeria; 
    
	/**Lista que almacena la respectiva cobertura de cada servicio*/
	private List<InfoResponsableCobertura> infoCoberturaServicio;
    
	private boolean escruento=false;
    
	public boolean isEscruento() {
		return escruento;
	}

	public void setEscruento(boolean escruento) {
		this.escruento = escruento;
	}
	public String getPortatil() {
		return portatil;
	}

	public void setPortatil(String portatil) {
		this.portatil = portatil;
	}

	/**
	 * Método que revisa todos los checkbox de esta forma
	 * (Por el funcionamiento de los checkbox, si no llega nada
	 * es porque el usuario no lo seleccionó)
	 * @param request
	 */
	public void limpiarCheckBox (HttpServletRequest request)
	{
		if(request.getParameter("anularSolicitud") == null)
			ib_anularSolicitud = false;

		if (request.getParameter("urgenteOtros")==null)
		{
			this.urgenteOtros=false;
		}

		for (int i=0;i<this.numeroServicios;i++)
		{
			if (request.getParameter("valores(urgente_"+ i+  ")")==null)
			{
				this.valores.put("urgente_"+i, "false");
			}
		}
	}

	/**
	 * Método que valida que los datos llenados por el usuario esten
	 * completos y sin errores de mundo (no vacios, presente, fecha/
	 * hora válida
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		
		ActionErrors errors= new ActionErrors();
		errors=super.validate(mapping,request);
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		Connection con=null;
		try {
			con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (this.otroServicio!=null&&this.otroServicio.length()>256)
		{
			this.otroServicio=this.otroServicio.substring(0,255);
			errors.add("tamanioOtroServExc", new ActionMessage("errors.maximoTamanioExcedido", " 256 ", " Otros"));
		}
		this.limpiarCheckBox(request);

		if (this.estado==null)
		{
			errors.add("estadoNulo", new ActionMessage("errors.estadoInvalido"));
		}
		else if (this.estado.equals("salir"))
		{
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
			final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

			Date fechaActual=null;
			try
			{
				fechaActual= dateTimeFormatter.parse(UtilidadFecha.getFechaActual() + ":" + UtilidadFecha.getHoraActual());
			}
			catch (ParseException e2)
			{
				e2.printStackTrace();
			}
			
			// Fecha de la Solicitud
			Date fechaSolicitud = null;
			boolean tieneErroresFechaYHora=false;
			try
			{
				fechaSolicitud = dateFormatter.parse(this.fechaSolicitud);
			}
			catch (java.text.ParseException e)
			{
				tieneErroresFechaYHora=true;
				errors.add("fechaSolicitud", new ActionMessage("errors.formatoFechaInvalido", "de solicitud"));
			}

			// Fecha y Hora de la solicitud
			Date fechaHoraEvolucion = null;

			//Fecha y Hora de la valoracion Inicial
			Date fechaValoracionInicial =null;
			//Hora de la Solicitud temporal
			try
			{
				timeFormatter.parse(this.horaSolicitud);
			}
			catch (java.text.ParseException e)
			{
				tieneErroresFechaYHora=true;
				errors.add("horaSolicitud", new ActionMessage("errors.formatoHoraInvalido", "de solicitud"));
			}
			if(!tieneErroresFechaYHora)
			{
				try 
				{
					fechaHoraEvolucion = dateTimeFormatter.parse(this.fechaSolicitud + ":" + this.horaSolicitud);
				} catch (ParseException e) 
				{
					e.printStackTrace();
				}
			}

			if (!tieneErroresFechaYHora)
			{
				// Validar que la fecha ingresada no sea superior a la fecha actual
				if (fechaActual.compareTo(fechaSolicitud) < 0)
				{
					errors.add("fechaSolicitud", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de solicitud", "actual"));
				}

				// Validar que si la fecha ingresada es igual a la fecha actual, la hora ingresada no sea superior a la hora actual
				else if (fechaActual.compareTo(fechaHoraEvolucion) < 0)
				{
					errors.add("horaSolicitud", new ActionMessage("errors.horaSuperiorA", "de solicitud", "actual"));
				}

				
				
				try {
					if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoAmbulatorios)
					if(!UtilidadValidacion.esViaIngresoConsultaExterna(con,paciente.getCodigoCuenta()))
					{
						try
						{
							if(!isFichaEpidemiologica())
							{
								if(UtilidadValidacion.obtenerFechaYHoraPrimeraValoracion(con,paciente.getCodigoCuenta())!=null)
								{
									this.setFechaHoraValoracionInicial(UtilidadValidacion.obtenerFechaYHoraPrimeraValoracion(con,paciente.getCodigoCuenta()));
									String elementos[]=this.getFechaHoraValoracionInicial();
									elementos[0]= UtilidadFecha.conversionFormatoFechaAAp(elementos[0]);
									elementos[1]= UtilidadFecha.convertirHoraACincoCaracteres(elementos[1]);
		
									fechaValoracionInicial = dateTimeFormatter.parse(elementos[0]+ ":" + elementos[1]);
		
									//Validar que si la fecha ingresada es mayor/igual a la fecha de la valoración inicial
									if(fechaHoraEvolucion.compareTo(fechaValoracionInicial) < 0)
									{
										errors.add("horaSolicitud", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de solicitud "+this.fechaSolicitud+" "+this.horaSolicitud+", ", "valoración inicial "+elementos[0]+" "+elementos[1]+"."));
									}
								}
							}
						}
						catch(Exception e)
						{
							tieneErroresFechaYHora=true;
							e.printStackTrace();
							errors.add("fechaSolicitud", new ActionMessage("errors.formatoFechaInvalido", "de solicitud"));
						}
					}
					else
					{
						/*
						if(!ValidacionesSolicitud.tieneCitasAtendidas(con,paciente.getCodigoCuenta(),medico.getCodigoPersona()))
						   errors.add("Paciente sin citas atendidas", new ActionMessage("error.cita.noRespondida"));
						*/
						
					}
					
					
					if(!UtilidadFecha.compararFechas(this.fechaSolicitud, this.horaSolicitud, paciente.getFechaIngreso(), paciente.getHoraIngreso()).isTrue())
					{
						errors.add("horaSolicitud", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de solicitud "+this.fechaSolicitud+" "+this.horaSolicitud+", ", "Apertura ingreso "+paciente.getFechaIngreso()+" "+paciente.getHoraIngreso()+"."));
					}
					
					
					
					
					
					
					
					
					
						
				} 
			catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
			
			if(esSolExterno)
				this.centroCostoSolicitado=ConstantesBD.codigoCentroCostoExternos;
			if(this.centroCostoSolicitado==ConstantesBD.codigoCentroCostoNoSeleccionado && this.codigoTipoSolicitud!=ConstantesBD.codigoServicioProcedimiento)
			{
				errors.add("centroCostoSolicitado", new ActionMessage("error.solicitudgeneral.noCampoCentroCosto"));
			}
			else if(this.codigoTipoSolicitud==ConstantesBD.codigoServicioProcedimiento)
			{
				//Validación del Centro de Costo
				for(int i=0;i<numeroServicios;i++)
				{
					//Se verifica si el servicio tenía centros de costo x grupo
					if(this.getValores("centro_costo_"+i)==null)
					{
						String nombreServicio = "("+this.getValores("codigoEspecialidad_"+i) + "-" + this.getValores("codigo_"+i)+") "+this.getValores("nombre_"+i);
						errors.add("grupo sin centros de costo",new ActionMessage("error.solicitudgeneral.procedimiento.SinCentroCostoXGrupo",nombreServicio));
					}
					else
					{
						String centroCosto=(String)this.getValores("centro_costo_"+i);
						if(Integer.parseInt(centroCosto)==ConstantesBD.codigoCentroCostoNoSeleccionado)
						{
							errors.add("centroCostoSolicitado_"+i, new ActionMessage("error.solicitudgeneral.noCampoCentroCostoServicio"));
							break;
						}
					}
				}
				//Validacion de la finalidad
				for(int i=0;i<numeroServicios;i++)
				{
					String finalidad=(String)this.getValores("finalidad_"+i);
					
					if(Integer.parseInt(finalidad)==ConstantesBD.codigoCentroCostoNoSeleccionado)
					{
						errors.add("finalidad_"+i, new ActionMessage("error.solicitudgeneral.noCampoFinalidadServicio"));
						break;
					}
				}
				//validacion de la frecuencia
				for(int i=0;i<numeroServicios;i++)
				{
					String frecuencia=(String)this.getValores("frecuencia_"+i);
					if(!frecuencia.equals(""))
					{
						if(Integer.parseInt(frecuencia)<=0)
						{
							String mensaje = "La frecuencia del servicio "+this.getValores("codigoEspecialidad_"+i)+"-"+this.getValores("codigo_"+i);
							errors.add("frecuencia_"+i, new ActionMessage("errors.integerMayorQue",mensaje,"0"));
						}
					}
				}
				
				
			}
			
			//	validacion de la justificacion no pos
			int numJustPendientes = 0;
			for(int i=0;i<numeroServicios;i++)
			{
				if (this.getValores("justificar_"+i).toString().equals("true")){
                	if(!UtilidadTexto.getBoolean(this.getJustificacionesServicios().get(this.getValores("codigo_"+i)+"_yajustifico")))
                		errors.add("No se ha diligenciado el formato de justificación No POS", new ActionMessage("errors.required","Justificación No POS para el servicio "+this.getValores("nombre_"+i)));
                }
				if (this.getValores("justificar_"+i).toString().equals("pendiente"))
					numJustPendientes++;
			}
			if (numJustPendientes==1 && numeroServicios==1)
        		errors.add("No puede solicitar el servicio No POS", new ActionMessage("errors.warnings","No puede solicitar el servicio "+this.getValores("nombre_0")+" Ya que no cumple con la validacion médico especialista"));
        	
			
			 
			if (this.ocupacionSolicitada==-1)
			{
				errors.add("ocupacionSolicitada", new ActionMessage("error.solicitudgeneral.noOcupacion"));
			}
			if (this.especialidadSolicitante==-1)
			{
				errors.add("especialidadSolicitante", new ActionMessage("error.solicitudgeneral.noEspecialidadSolicitante"));
			}
			//validacion para la cantidad en solicitud interconsulta procedimientos.
			if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioProcedimiento)
			{
				boolean errorDetalle=false;
				for(int i=0;i<numeroServicios;i++)
				{
					try
					{
						if(Integer.parseInt(getValores("cantidad_"+i)+"") <=0)
						{
							errors.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorQue","La cantidad del servicio "+valores.get("codigo_"+i)+"-"+valores.get("codigoEspecialidad_"+i),"0"));  
						}
				           
					}
					catch (NumberFormatException e)
					{
						errors.add("numeroNoEntero", new ActionMessage("errors.integer", "La cantidad del servicio "+valores.get("codigo_"+i)+"-"+valores.get("codigoEspecialidad_"+i)));
					}
					if(this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento && !errorDetalle)
					{
						errors.add("centroCostoSolicitado", new ActionMessage("error.solicitudgeneral.noCampoCentroCostoServicio"));
						errorDetalle=true;
					}
				}
			}
			
			///////validamos los servicios y articulos incluidos
			if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioProcedimiento)
			{
				for(int i=0;i<numeroServicios;i++)
				{
					String codigoServPpal=valores.get("codigo_"+i)+"";
					int numServiciosIncluidos= Utilidades.convertirAEntero(this.getServiciosIncluidos("numRegistros_"+codigoServPpal)+"");
					for(int w=0; w<numServiciosIncluidos;w++)
					{
						int cantidadSolicitada= Utilidades.convertirAEntero(this.getServiciosIncluidos("cantidad_"+w+"_"+codigoServPpal)+"");
						int cantidadMaxima= Utilidades.convertirAEntero(this.getServiciosIncluidos("cantidad_maxima_valida_"+w+"_"+codigoServPpal)+"");
						
						if(cantidadSolicitada<=0 || cantidadSolicitada>cantidadMaxima)
						{
							//errors.range = {0} debe estar entre {1} - {2}. [aa-07]
							errors.add("", new ActionMessage("errors.range", "La cantidad del servicio "+this.getServiciosIncluidos("desc_serv_incluido_"+w+"_"+codigoServPpal), "1", cantidadMaxima+" (cantidad maxima parametrizada)"));
						}
					}
					
					int numArticulosIncluidos= Utilidades.convertirAEntero(this.getArticulosIncluidos("numRegistros_"+codigoServPpal)+"");
					for(int w=0; w<numArticulosIncluidos;w++)
					{
						int cantidadSolicitada= Utilidades.convertirAEntero(this.getArticulosIncluidos("cantidad_"+w+"_"+codigoServPpal)+"");
						int cantidadMaxima= Utilidades.convertirAEntero(this.getArticulosIncluidos("cantidad_maxima_valida_"+w+"_"+codigoServPpal)+"");
						
						if(cantidadSolicitada<0 || cantidadSolicitada>cantidadMaxima)
						{
							//errors.range = {0} debe estar entre {1} - {2}. [aa-07]
							errors.add("", new ActionMessage("errors.range", "La cantidad del articulo "+this.getArticulosIncluidos("desc_art_incluido_"+w+"_"+codigoServPpal), "0", cantidadMaxima+" (cantidad maxima parametrizada)"));
						}
					}
				}
			}
			/////////////fin validamos los servicios y articulos incluidos
			
			else if(this.codigoTipoSolicitud==ConstantesBD.codigoServicioInterconsulta)
			{
				if(this.comentario.length()>3000)
					errors.add("campo comentario", new ActionMessage("errors.maxlength","El comentario","3000"));
			}
			if (this.seleccionarServicio.equals("otros"))
			{
				if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioInterconsulta)
				{
					if (this.otroServicio==null||this.otroServicio.equals(""))
					{
						errors.add("nombreOtrosVacio", new ActionMessage("error.solicitudgeneral.noCampoOtrosInterconsulta"));
					}
				}
				else if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioProcedimiento)
				{
					if (this.otroServicio==null||this.otroServicio.equals(""))
					{
						errors.add("nombreOtrosVacio", new ActionMessage("error.solicitudgeneral.noCampoOtrosProcedimiento"));
					}
				}
			}
			else
			{
				//Caso buscados
				if (this.numeroServicios<1)
				{
					if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioInterconsulta)
					{
						errors.add("sinServicioSeleccionado", new ActionMessage("error.solicitudgeneral.noSeleccionInterconsulta"));
					}
					else if (this.codigoTipoSolicitud==ConstantesBD.codigoServicioProcedimiento)
					{
						errors.add("sinServicioSeleccionado", new ActionMessage("error.solicitudgeneral.noSeleccionProcedimiento"));
					}
				}
			}
			
			//Validacion del campo requerido comentarios
			if(this.comentario.trim().equals("")&&this.requeridoComentarios)
				errors.add("",new ActionMessage("errors.required","El campo observación por caso clínico"));
		}
		else if(estado.equals("guardarModificacion") )
		{
			if(request.getParameter("anularSolicitud") == null)
				setAnularSolicitud(false);

			if(isAnularSolicitud() && getMotivoAnulacion().equals("") )
				errors.add("motivoAnulacion", new ActionMessage("errors.required","Si selecciona Anular solicitud, debe llenar el campo Motivo Anulación Solicitud que"));
			
			if(isPortatilClone() && !UtilidadTexto.getBoolean(getValores("portatilCheckBox_0")))
				if (getValores("motivoanulport_0")==null || (getValores("motivoanulport_0")+"").equals(""))
				{
					errors.add("motivoAnulacion", new ActionMessage("errors.required","Si se deschequeó Requiere Portátil, debe llenar el campo Motivo Anulación Portatil que"));
					capaMotivoAnulacion=true;
				}
			
			if(errors.isEmpty() && !isAnularSolicitud())
			{
				//validamos las cantidades de los articulos incluidos
				for(int w=0; w<Utilidades.convertirAEntero(this.getArticulosIncluidos("numRegistros")+""); w++)
				{
					int cantidad= Utilidades.convertirAEntero(this.getArticulosIncluidos("cantidad_"+w)+"");
					int cantidadMaxima= Utilidades.convertirAEntero(this.getArticulosIncluidos("cantidadMaxima_"+w)+"");
					
					if(cantidad<0 || cantidad>cantidadMaxima)
					{
						//errors.range = {0} debe estar entre {1} - {2}. [aa-07]
						errors.add("", new ActionMessage("errors.range", "La cantidad del articulo "+this.getArticulosIncluidos("descripcionArticulo_"+w), "0", cantidadMaxima+" (cantidad maxima parametrizada)"));
					}
				}
			}
		}
		if(estado.equals("modificar") )
		{
		if(Utilidades.convertirAEntero(valAnulacion.get("numRegistros")+"")>=1)
		{
			errors.add("anulacion",new ActionMessage("errors.notEspecific","La ordes tiene una Autorizacionde Servicios a "+valAnulacion.get("razonsocial")+", primero se debe anular la Autorizacion. Porfavor verifique"));
		}
		}
		
	
						
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return errors;
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

	public void resetJus()
	{
		this.justificacionesServicios=new HashMap<String, Object>();
		justificacionesServicios.put("numRegistros", 0);
	}
	
	/**
	 * reset de todos los metodos
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void reset()
	{
		
	
		
		//this.fichaEpidemiologica=false; // Se quita este campo por tarea 23300
		this.codigoEnfermedadEpidemiologia="";
		this.servicioAmbUrgente=false;
		this.ordenAmbulatoria="";
		this.fechaOrdenAmb="";
		this.esSolExterno=false;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.centrosAtencionPYP="";
		this.postularServicio=false;
		this.codigoServicioPostular=ConstantesBD.codigoNuncaValido+"";
		this.solPYP=false;
		this.accionPYP="";
		this.otroServicio="";
		encabezadoResumen="";
		codigoTipoSolicitud=' ';
        codigoTipoSolicitudInt=ConstantesBD.codigoNuncaValido;
		numeroSolicitud=0;
		fechaSolicitud="";
		horaSolicitud="";
		this.justificacionSolicitud="";
		this.justificacionSolicitudNueva = "";
		especialidadSolicitante=0;
		centroCostoSolicitado=0;
		ocupacionSolicitada=-1;
		urgenteOtros=false;
		//Por defecto debe quedar seleccionada la opción
		//de seleccionar procedimiento
		seleccionarServicio="buscado";
		otroServicio="";
		nombreTipoSolicitud="";
		labelUrgente="";
		nombrePaciente="";
		nombreMedico="";
		nombreMedicoResponde="";

		comentario="";

		this.fechaSolicitud=UtilidadFecha.getFechaActual();
		this.horaSolicitud=UtilidadFecha.getHoraActual();
		this.fechaHoraValoracionInicial= null;

		nombreEspecialidadSolicitante="";
		nombreEspecialidadSolicitada="";
		enlace="";
		vaEpicrisis=false;
		interpretacion="";
//				Al entrar el usuario no ha seleccionado servicio
		//luego el número de servicios es 0
		this.numeroServicios=0;
		//Algunos tipos de solicitud permiten selección de
		//más de un servicio, otros solo 1, por medio de
		//este parametro lo especificamos, tomamos el valor
		//con menor riesgo
		this.numeroServiciosMaximo=1;
		this.interpretada=false;
		this.valores.clear();
		this.ultimaInterpretacion="";
		this.consecutivoOrdenesMedicas=-1;
		this.interpretar=false;
		this.guardarInterpretacionSolicitud=false;

		setAnularSolicitud(false);
		setCentroCostoSolicitante("");
		setComentarioAdicional("");
		setFechaGrabacion("");
		setHoraGrabacion("");
		setMotivoAnulacion("");
		setNombreCentroCostoSolicitado("");
		setNombreOcupacionSolicitada("");

		this.checkEpicrisis="";

		iidi_estadoFacturacion		= new InfoDatosInt(-1);
		iidi_estadoHistoriaClinica	= new InfoDatosInt(-1);
		otraSolicitud=false;
		
		finalizar = false;
		
		this.finalidad = -1;
		this.nombreFinalidad = "";
		this.unicamenteExternos=false;
		this.codigoCentroAtencionCuentaSol=ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencionCuentaSol="";
		this.cama="";
		this.incluyeServiciosArticulos=false;
		
		this.portatil=ConstantesBD.codigoNuncaValido+"";
		this.portatilClone=false;
		
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		
		resetSevArtIncluidos();
		
		this.requeridoComentarios = false;
		
		this.arrayArticuloIncluidoDto = new ArrayList<DtoArticuloIncluidoSolProc>();
		this.justificacionMap = new HashMap<String, Object>();
		this.hiddens = "";
		this.descripcionCentroCostoSolicitado = "";
		this.numerosSolicitudes = new HashMap<String, Object>();
		this.numerosSolicitudes.put("numRegistros", "0");
		this.valAnulacion = new HashMap<String, Object>();
		this.valAnulacion.put("numRegistros", "0");
		this.consecutivosOrdenesMedicas= new Vector<String>();
		this.acronimoDiagnostico="";
		this.tipoCieDiagnostico="";
		this.dtoDiagnostico= new DtoDiagnostico();
		
		this.mensajesAdvertenciaMap= new HashMap();
		
		
		this.ordenAmbulPendienteAutoriz=false;
		/**Lista que almacena los servicios que se asignaron en la cita*/
		this.listaServiciosImpimirOrden=new ArrayList<String>();
		
		this.existeAutorizacionOrden=false;
		this.botonGenerarSolicitud=false;
		
		this.infoCoberturaServicio	= new ArrayList<InfoResponsableCobertura>();
		this.escruento=false;
	}
	
	/**Setea los valores para las validaciones cuando existe autorizcion asociada a Orden Ambulatoria*/
	public void resetAutorizOrden(){
		
		this.centroCostoAutorizacion=new DtoCheckBox();
		this.existeAutorizacionOrden=false;
		this.botonGenerarSolicitud=false;
	}
	
	
	public String getFechaOrdenAmb() {
		return fechaOrdenAmb;
	}

	public void setFechaOrdenAmb(String fechaOrdenAmb) {
		this.fechaOrdenAmb = fechaOrdenAmb;
	}

	/**
	 * @return the requeridoComentarios
	 */
	public boolean isRequeridoComentarios() {
		return requeridoComentarios;
	}

	/**
	 * @param requeridoComentarios the requeridoComentarios to set
	 */
	public void setRequeridoComentarios(boolean requeridoComentarios) {
		this.requeridoComentarios = requeridoComentarios;
	}

	/**
	 * 
	 */
	public void resetSevArtIncluidos()
	{
		/*******servicios articulos incluidos**********/
		this.serviciosIncluidos= new HashMap<Object, Object>();
		this.setServiciosIncluidos("numRegistros", 0);
		this.articulosIncluidos= new HashMap<Object, Object>();
		this.setArticulosIncluidos("numRegistros", 0);
	}
	
	
	public void resetCasoPYP()
	{
		this.esSolExterno=false;
		this.otroServicio="";
		encabezadoResumen="";
		numeroSolicitud=0;
		fechaSolicitud="";
		horaSolicitud="";
		justificacionSolicitud="";
		this.justificacionSolicitudNueva ="";
		especialidadSolicitante=0;
		centroCostoSolicitado=0;
		ocupacionSolicitada=-1;
		urgenteOtros=false;
		//Por defecto debe quedar seleccionada la opción
		//de seleccionar procedimiento
		seleccionarServicio="buscado";
		otroServicio="";
		nombreTipoSolicitud="";
		labelUrgente="";
		nombrePaciente="";
		nombreMedico="";
		nombreMedicoResponde="";

		comentario="";

		this.fechaSolicitud=UtilidadFecha.getFechaActual();
		this.horaSolicitud=UtilidadFecha.getHoraActual();
		this.fechaHoraValoracionInicial= null;

		nombreEspecialidadSolicitante="";
		nombreEspecialidadSolicitada="";
		enlace="";
		vaEpicrisis=false;
		interpretacion="";
//				Al entrar el usuario no ha seleccionado servicio
		//luego el número de servicios es 0
		this.numeroServicios=0;
		//Algunos tipos de solicitud permiten selección de
		//más de un servicio, otros solo 1, por medio de
		//este parametro lo especificamos, tomamos el valor
		//con menor riesgo
		this.numeroServiciosMaximo=1;
		this.interpretada=false;
		this.valores.clear();
		this.ultimaInterpretacion="";
		this.consecutivoOrdenesMedicas=-1;
		this.interpretar=false;
		this.guardarInterpretacionSolicitud=false;

		setAnularSolicitud(false);
		setCentroCostoSolicitante("");
		setComentarioAdicional("");
		setFechaGrabacion("");
		setHoraGrabacion("");
		setMotivoAnulacion("");
		setNombreCentroCostoSolicitado("");
		setNombreOcupacionSolicitada("");

		this.checkEpicrisis="";

		iidi_estadoFacturacion		= new InfoDatosInt(-1);
		iidi_estadoHistoriaClinica	= new InfoDatosInt(-1);
		otraSolicitud=false;
		
		finalizar = false;
		
		this.finalidad = -1;
		this.nombreFinalidad = "";
		this.unicamenteExternos=false;
	}

	/**
	 * Metodo para realizar el reset cuando se realiza una nueva solicitud sin salir del modulos (ej-> link: otra solicitud)
	 *
	 */
	public void resetNuevaSolicitud()
	{
			this.otroServicio="";
			encabezadoResumen="";
			numeroSolicitud=0;
			fechaSolicitud="";
			horaSolicitud="";
			justificacionSolicitud="";
			this.justificacionSolicitudNueva ="";
			especialidadSolicitante=0;
			centroCostoSolicitado=0;
			ocupacionSolicitada=-1;
			urgenteOtros=false;
			//Por defecto debe quedar seleccionada la opción
			//de seleccionar procedimiento
			seleccionarServicio="buscado";
			otroServicio="";
			labelUrgente="";
			nombrePaciente="";
			nombreMedico="";
			nombreMedicoResponde="";

			comentario="";

			this.fechaSolicitud=UtilidadFecha.getFechaActual();
			this.horaSolicitud=UtilidadFecha.getHoraActual();
			this.fechaHoraValoracionInicial= null;

			nombreEspecialidadSolicitante="";
			nombreEspecialidadSolicitada="";
			enlace="";
			vaEpicrisis=false;
			interpretacion="";
//					Al entrar el usuario no ha seleccionado servicio
			//luego el número de servicios es 0
			this.numeroServicios=0;
			//Algunos tipos de solicitud permiten selección de
			//más de un servicio, otros solo 1, por medio de
			//este parametro lo especificamos, tomamos el valor
			//con menor riesgo
			this.numeroServiciosMaximo=1;
			this.interpretada=false;
			this.valores.clear();
			this.ultimaInterpretacion="";
			this.consecutivoOrdenesMedicas=-1;
			this.interpretar=false;
			this.guardarInterpretacionSolicitud=false;

			setAnularSolicitud(false);
			setCentroCostoSolicitante("");
			setComentarioAdicional("");
			setFechaGrabacion("");
			setHoraGrabacion("");
			setMotivoAnulacion("");
			setNombreCentroCostoSolicitado("");
			setNombreOcupacionSolicitada("");
			
			this.justificacionesServicios = new HashMap<String, Object>();

			this.checkEpicrisis="";

			iidi_estadoFacturacion		= new InfoDatosInt(-1);
			iidi_estadoHistoriaClinica	= new InfoDatosInt(-1);
			otraSolicitud=false;
			this.acronimoDiagnostico="";
			this.tipoCieDiagnostico="";


	}

	/**
	 * @return
	 */
	public int getCentroCostoSolicitado() {
		return centroCostoSolicitado;
	}

	/**
	 * @param string
	 */
	public void setCentroCostoSolicitado(int i) {
		centroCostoSolicitado = i;
	}

	/**
	 * @return
	 */
	public int getEspecialidadSolicitante() {
		return especialidadSolicitante;
	}

	/**
	 * @return
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @return
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}
	

	/**
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @return
	 */
	public int getOcupacionSolicitada() {
		return ocupacionSolicitada;
	}

	/**
	 * @return
	 */
	public boolean isUrgenteOtros() {
		return urgenteOtros;
	}

	/**
	 * @param string
	 */
	public void setEspecialidadSolicitante(int i) {
		especialidadSolicitante = i;
	}

	/**
	 * @param string
	 */
	public void setFechaSolicitud(String string) {
		fechaSolicitud = string;
	}

	/**
	 * @param string
	 */
	public void setHoraSolicitud(String string) {
		horaSolicitud = string;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

	/**
	 * @param string
	 */
	public void setOcupacionSolicitada(int i) {
		ocupacionSolicitada = i;
	}

	/**
	 * @param b
	 */
	public void setUrgenteOtros(boolean b) {
		urgenteOtros = b;
	}


	/**
	 * Método que guarda un valor en el mapa
	 *
	 * @param key llave con la que quedará el
	 * valor a guardar
	 * @param value valor a guardar en el mapa
	 */
	public void setValores(String key, Object value)
	{
		valores.put(key, value);
	}

	/**
	 * Método que recupera un valor del mapa
	 *
	 * @param key llave con la que esta el valor
	 * buscado en el mapa
	 * @return
	 */
	public Object getValores(String key)
	{
		return valores.get(key);
	}

	/**
	* @return
	 */
	public HashMap<String, Object> getValores() {
		return valores;
	}

	/**
	 * @param map
	 */
	public void setValores(HashMap<String, Object> map) {
		valores = map;
	}

	/**
	 * @return
	 */
	public char getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @param i
	 */
	public void setCodigoTipoSolicitud(char i) {
		codigoTipoSolicitud = i;
	}

	/**
	 * @return
	 */
	public String getOtroServicio() {
		return otroServicio;
	}

	/**
	 * @param string
	 */
	public void setOtroServicio(String string) {
		otroServicio = string;
	}

	/**
	 * @return
	 */
	public String getSeleccionarServicio() {
		return seleccionarServicio;
	}

	/**
	 * @param string
	 */
	public void setSeleccionarServicio(String string) {
		seleccionarServicio = string;
	}

	/**
	 * @return
	 */
	public int getEspecialidadSolicitada()
	{
		return especialidadSolicitada;
	}

	/**
	 * @param i
	 */
	public void setEspecialidadSolicitada(int i)
	{
		especialidadSolicitada = i;
	}

	/**
	 * @return
	 */
	public String getEnlace() 
	{
		return "<a href=\""+enlace+"\" ><img name=\"detalle\" src=\""+ directorioImagenes+"infoAdic.gif\" border=\"0\" /></a>";
	}

	/**
	 * @return
	 */
	public String getEnlace1() 
	{
		return enlace;
	}
	
	/**
	 * @return
	 */
	public String getLabelUrgente() {
		return labelUrgente;
	}

	/**
	 * @return
	 */
	public String getNombreEspecialidadSolicitada() {
		return nombreEspecialidadSolicitada;
	}

	/**
	 * @return
	 */
	public String getNombreEspecialidadSolicitante() {
		return nombreEspecialidadSolicitante;
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
	 * @return
	 */
	public String getNombreTipoSolicitud() {
		return nombreTipoSolicitud;
	}

	/**
	 * @param string
	 */
	public void setEnlace(String string) {
		enlace = string;
	}

	/**
	 * @param string
	 */
	public void setLabelUrgente(String string) {
		labelUrgente = string;
	}

	/**
	 * @param string
	 */
	public void setNombreEspecialidadSolicitada(String string) {
		nombreEspecialidadSolicitada = string;
	}

	/**
	 * @param string
	 */
	public void setNombreEspecialidadSolicitante(String string) {
		nombreEspecialidadSolicitante = string;
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
	 * @param string
	 */
	public void setNombreTipoSolicitud(String string) {
		nombreTipoSolicitud = string;
	}

	/**
	 * @return
	 */
	public int getNumeroServicios()
	{
		return numeroServicios;
	}

	/**
	 * @param i
	 */
	public void setNumeroServicios(int i)
	{
		numeroServicios = i;
	}

	/**
	 * @return
	 */
	public int getNumeroServiciosMaximo()
	{
		return numeroServiciosMaximo;
	}

	/**
	 * @param i
	 */
	public void setNumeroServiciosMaximo(int i)
	{
		numeroServiciosMaximo = i;
	}

	/**
	 * @return
	 */
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	/**
	 * @param string
	 */
	public void setFechaRespuesta(String string) {
		fechaRespuesta = string;
	}

	/**
	 * @return
	 */
	public String getInterpretacion() {
		return interpretacion;
	}

	/**
	 * @return
	 */
	public boolean isVaEpicrisis() {
		return vaEpicrisis;
	}

	/**
	 * @param string
	 */
	public void setInterpretacion(String string) {
		interpretacion = string;
	}

	/**
	 * @param b
	 */
	public void setVaEpicrisis(boolean b) {
		vaEpicrisis = b;
	}

	/**
	 * @return
	 */
	public boolean isInterpretada() {
		return interpretada;
	}

	/**
	 * @param b
	 */
	public void setInterpretada(boolean b) {
		interpretada = b;
	}

	/**
	 * @return
	 */
	public String getUltimaInterpretacion() {
		return ultimaInterpretacion;
	}

	/**
	 * @param string
	 */
	public void setUltimaInterpretacion(String string) {
		ultimaInterpretacion = string;
	}

	/**
	 * @return
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * @param string
	 */
	public void setComentario(String string) {
		comentario = string;
	}

	/**
	 * Returns the consecutivoOrdenesMedicas.
	 * @return int
	 */
	public int getConsecutivoOrdenesMedicas() {
		return consecutivoOrdenesMedicas;
	}

	/**
	 * Sets the consecutivoOrdenesMedicas.
	 * @param consecutivoOrdenesMedicas The consecutivoOrdenesMedicas to set
	 */
	public void setConsecutivoOrdenesMedicas(int consecutivoOrdenesMedicas) {
		this.consecutivoOrdenesMedicas = consecutivoOrdenesMedicas;
	}

	/**
	 * Returns the interpretar.
	 * @return boolean
	 */
	public boolean isInterpretar() {
		return interpretar;
	}

	/**
	 * Sets the interpretar.
	 * @param interpretar The interpretar to set
	 */
	public void setInterpretar(boolean interpretar) {
		this.interpretar = interpretar;
	}
	/**
	 * Returns the guardarInterpretacionSolicitud.
	 * @return boolean
	 */
	public boolean isGuardarInterpretacionSolicitud() {
		return this.guardarInterpretacionSolicitud;
	}

	/**
	 * Sets the guardarInterpretacionSolicitud.
	 * @param guardarInterpretacionSolicitud The guardarInterpretacionSolicitud to set
	 */
	public void setGuardarInterpretacionSolicitud(boolean guardarInterpretacionSolicitud) {
		this.guardarInterpretacionSolicitud = guardarInterpretacionSolicitud;
	}

	/** Obtiene el nombre del centro de costo solicitante */
	public String getCentroCostoSolicitante()
	{
		return is_centroCostoSolicitante;
	}

	/** Obtiene la fecha de grabación de la solicitud */
	public String getFechaGrabacion()
	{
		return is_fechaGrabacion;
	}

	/** Obtiene la hora de grabación de la solicitud */
	public String getHoraGrabacion()
	{
		return is_horaGrabacion;
	}

	/** Obtiene el nombre del centro de costo solicitado */
	public String getNombreCentroCostoSolicitado()
	{
		return is_centroCostoSolicitado;
	}

	/** Obtiene el nombre de la ocupación solicitada */
	public String getNombreOcupacionSolicitada()
	{
		return is_ocupacionSolicitada;
	}

	/** Establece el nombre del centro de costo solicitante */
	public void setCentroCostoSolicitante(String as_centroCostoSolicitante)
	{
		if(as_centroCostoSolicitante != null)
			is_centroCostoSolicitante = as_centroCostoSolicitante.trim();
	}

	/** Establece la fecha de grabación de la solicitud */
	public void setFechaGrabacion(String as_fechaGrabacion)
	{
		if(as_fechaGrabacion != null)
			is_fechaGrabacion = as_fechaGrabacion.trim();
	}

	/** Establece la hora de grabación de la solicitud */
	public void setHoraGrabacion(String as_horaGrabacion)
	{
		if(as_horaGrabacion != null)
			is_horaGrabacion = as_horaGrabacion.trim();
	}

	/** Establece el nombre del centro de costo solicitado */
	public void setNombreCentroCostoSolicitado(String as_centroCostoSolicitado)
	{
		if(as_centroCostoSolicitado != null)
			is_centroCostoSolicitado = as_centroCostoSolicitado.trim();
	}

	/** Establece el nombre de la ocupación solicitada */
	public void setNombreOcupacionSolicitada(String as_ocupacionSolicitada)
	{
		if(as_ocupacionSolicitada != null)
			is_ocupacionSolicitada = as_ocupacionSolicitada.trim();
	}

	/** Retorna la fecha y hora de la valoración inicial asociada a esa solicitud
	 * @return
	 */
	public String[] getFechaHoraValoracionInicial() {
		return fechaHoraValoracionInicial;
	}

	/**Asigna la fecha y la hora de la valoración inicial asociada a esa solicitud
	 * @param strings
	 */
	public void setFechaHoraValoracionInicial(String[] fechaHora) {
		fechaHoraValoracionInicial = fechaHora;
	}

	/** Obtiene el comentario adicional a la solicitud */
	public String getComentarioAdicional()
	{
		return is_comentarioAdicional;
	}

	/** Establece el comentario adicional a la solicitud */
	public void setComentarioAdicional(String as_comentarioAdicional)
	{
		if(as_comentarioAdicional != null)
			is_comentarioAdicional = as_comentarioAdicional.trim();
	}

	/** Obtiene el indicador de anulación de la solicitud */
	public boolean isAnularSolicitud()
	{
		return ib_anularSolicitud;
	}

	/** Establece el indicador de anulación de la solicitud */
	public void setAnularSolicitud(boolean lb_anulacionSolicitud)
	{
		ib_anularSolicitud = lb_anulacionSolicitud;
	}

	/** Obtiene el motivo de anulación de la solicitud */
	public String getMotivoAnulacion()
	{
		return is_motivoAnulacion;
	}

	/** Establece el motivo de anulación de la solicitud */
	public void setMotivoAnulacion(String as_motivoAnulacion)
	{
		if(as_motivoAnulacion != null)
			is_motivoAnulacion = as_motivoAnulacion.trim();
	}

	/** Obtiene el código del estado de facturación de la solicitud */
	public int getCodigoEstadoFacturacion()
	{
		return iidi_estadoFacturacion.getCodigo();
	}

	/** Obtiene el nombre del estado de facturación de la solicitud */
	public String getNombreEstadoFacturacion()
	{
		return iidi_estadoFacturacion.getNombre();
	}

	/** Obtiene el código del estado de historia clínica de la solicitud */
	public int getCodigoEstadoHistoriaClinica()
	{
		return iidi_estadoHistoriaClinica.getCodigo();
	}

	/** Obtiene el nombre del estado de historia clínica de la solicitud */
	public String getNombreEstadoHistoriaClinica()
	{
		return iidi_estadoHistoriaClinica.getNombre();
	}

	/** Establece el código del estado de facturación de la solicitud */
	public void setCodigoEstadoFacturacion(int ai_estadoFacturacion)
	{
		iidi_estadoFacturacion.setCodigo(
			ai_estadoFacturacion < 0 ? -1 : ai_estadoFacturacion
		);
	}

	/** Establece el nombre del estado de facturación de la solicitud */
	public void setNombreEstadoFacturacion(String as_estadoFacturacion)
	{
		iidi_estadoFacturacion.setNombre(
			as_estadoFacturacion != null ? as_estadoFacturacion.trim() : ""
		);
	}

	/** Establece el código del estado de historia clínica de la solicitud */
	public void setCodigoEstadoHistoriaClinica(int ai_estadoHistoriaClinica)
	{
		iidi_estadoHistoriaClinica.setCodigo(
			ai_estadoHistoriaClinica < 0 ? -1 : ai_estadoHistoriaClinica
		);
	}

	/** Establece el nombre del estado de historia clínica de la solicitud */
	public void setNombreEstadoHistoriaClinica(String as_estadoHistoriaClinica)
	{
		iidi_estadoHistoriaClinica.setNombre(
			as_estadoHistoriaClinica != null ? as_estadoHistoriaClinica.trim() : ""
		);
	}

	/**
	 * Returns the checkEpicrisis.
	 * @return String
	 */
	public String getCheckEpicrisis() {
		return checkEpicrisis;
	}

	/**
	 * Sets the checkEpicrisis.
	 * @param checkEpicrisis The checkEpicrisis to set
	 */
	public void setCheckEpicrisis(String checkEpicrisis) {
		this.checkEpicrisis = checkEpicrisis;
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
	 * @return Retorna el iidi_estadoHistoriaClinica.
	 */
	public InfoDatosInt getIidi_estadoHistoriaClinica() {
		return iidi_estadoHistoriaClinica;
	}
	/**
	 * @param iidi_estadoHistoriaClinica Asigna el iidi_estadoHistoriaClinica.
	 */
	public void setIidi_estadoHistoriaClinica(
			InfoDatosInt iidi_estadoHistoriaClinica) {
		this.iidi_estadoHistoriaClinica = iidi_estadoHistoriaClinica;
	}
	/**
	 * @return Retorna el otraSolicitud.
	 */
	public boolean isOtraSolicitud() {
		return otraSolicitud;
	}
	/**
	 * @param otraSolicitud Asigna el otraSolicitud.
	 */
	public void setOtraSolicitud(boolean otraSolicitud) {
		this.otraSolicitud = otraSolicitud;
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
	 * @return Retorna finalizar.
	 */
	public boolean getFinalizar() {
		return finalizar;
	}
	/**
	 * @param Asigna finalizar.
	 */
	public void setFinalizar(boolean finalizar) {
		this.finalizar = finalizar;
	}
	/**
	 * @return Retorna multiple.
	 */
	public boolean getMultiple() {
		return multiple;
	}
	/**
	 * @param Asigna multiple.
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
	/**
	 * @return Retorna frecuencia.
	 */
	public double getFrecuencia() {
		return frecuencia;
	}
	/**
	 * @param Asigna frecuencia.
	 */
	public void setFrecuencia(double frecuencia) {
		this.frecuencia = frecuencia;
	}
	/**
	 * @return Retorna tipoFrecuencia.
	 */
	public int getTipoFrecuencia() {
		return tipoFrecuencia;
	}
	/**
	 * @param Asigna tipoFrecuencia.
	 */
	public void setTipoFrecuencia(int tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}
    /**
     * @return Returns the codigoTipoSolicitudInt.
     */
    public int getCodigoTipoSolicitudInt() {
        return codigoTipoSolicitudInt;
    }
    /**
     * @param codigoTipoSolicitudInt The codigoTipoSolicitudInt to set.
     */
    public void setCodigoTipoSolicitudInt(int codigoTipoSolicitudInt) {
        this.codigoTipoSolicitudInt = codigoTipoSolicitudInt;
    }

	public int getFinalidad() {
		return finalidad;
	}

	public void setFinalidad(int finalidad) {
		this.finalidad = finalidad;
	}

	public String getNombreFinalidad() {
		return nombreFinalidad;
	}

	public void setNombreFinalidad(String nombreFinalidad) {
		this.nombreFinalidad = nombreFinalidad;
	}

	/**
	 * @return Retorna unicamenteExternos.
	 */
	public boolean getUnicamenteExternos()
	{
		return unicamenteExternos;
	}

	/**
	 * @param unicamenteExternos Asigna unicamenteExternos.
	 */
	public void setUnicamenteExternos(boolean unicamenteExternos)
	{
		this.unicamenteExternos = unicamenteExternos;
	}

	public boolean isEsSolExterno() {
		return esSolExterno;
	}

	public void setEsSolExterno(boolean esSolExterno) {
		this.esSolExterno = esSolExterno;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getCodigoServicioPostular() {
		return codigoServicioPostular;
	}

	public void setCodigoServicioPostular(String codigoServicioPostular) {
		this.codigoServicioPostular = codigoServicioPostular;
	}

	public boolean isPostularServicio() {
		return postularServicio;
	}

	public void setPostularServicio(boolean postularServicio) {
		this.postularServicio = postularServicio;
	}

	public boolean isSolPYP() {
		return solPYP;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public String getAccionPYP() {
		return accionPYP;
	}

	public void setAccionPYP(String accionPYP) {
		this.accionPYP = accionPYP;
	}

	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public boolean isIndicativoOrdenAmbulatoria() {
		return indicativoOrdenAmbulatoria;
	}

	public void setIndicativoOrdenAmbulatoria(boolean indicativoOrdenAmbulatoria) {
		this.indicativoOrdenAmbulatoria = indicativoOrdenAmbulatoria;
	}

	public boolean isServicioAmbUrgente() {
		return servicioAmbUrgente;
	}

	public void setServicioAmbUrgente(boolean servicioAmbUrgente) {
		this.servicioAmbUrgente = servicioAmbUrgente;
	}

	public String getCentrosAtencionPYP() {
		return centrosAtencionPYP;
	}

	public void setCentrosAtencionPYP(String centrosAtencionPYP) {
		this.centrosAtencionPYP = centrosAtencionPYP;
	}

	public int getCodigoCentroAtencionCuentaSol() {
		return codigoCentroAtencionCuentaSol;
	}

	public void setCodigoCentroAtencionCuentaSol(int codigoCentroAtencionCuentaSol) {
		this.codigoCentroAtencionCuentaSol = codigoCentroAtencionCuentaSol;
	}

	public String getNombreCentroAtencionCuentaSol() {
		return nombreCentroAtencionCuentaSol;
	}

	public void setNombreCentroAtencionCuentaSol(
			String nombreCentroAtencionCuentaSol) {
		this.nombreCentroAtencionCuentaSol = nombreCentroAtencionCuentaSol;
	}

	public String getCantidadServicioOrdenAmbulatoria() {
		return cantidadServicioOrdenAmbulatoria;
	}

	public void setCantidadServicioOrdenAmbulatoria(
			String cantidadServicioOrdenAmbulatoria) {
		this.cantidadServicioOrdenAmbulatoria = cantidadServicioOrdenAmbulatoria;
	}

	public String getCodigoEnfermedadEpidemiologia()
	{
		return codigoEnfermedadEpidemiologia;
	}

	public void setCodigoEnfermedadEpidemiologia(
			String codigoEnfermedadEpidemiologia)
	{
		this.codigoEnfermedadEpidemiologia = codigoEnfermedadEpidemiologia;
	}

	public boolean isFichaEpidemiologica()
	{
		return fichaEpidemiologica;
	}

	public void setFichaEpidemiologica(boolean fichaEpidemiologica)
	{
		this.fichaEpidemiologica = fichaEpidemiologica;
	}

	public String getCama() {
		return cama;
	}

	public void setCama(String cama) {
		this.cama = cama;
	}

	/**
	 * @return the justificacionesServicios
	 */
	public HashMap<String, Object> getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	public void setJustificacionesServicios(HashMap<String, Object> justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionesServicios(String key) {
		return justificacionesServicios.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setJustificacionesServicios(String key, Object obj) {
		this.justificacionesServicios.put(key, obj);
	}

	public boolean isRequiereJustificacionServicio() {
		return requiereJustificacionServicio;
	}

	public void setRequiereJustificacionServicio(
			boolean requiereJustificacionServicio) {
		this.requiereJustificacionServicio = requiereJustificacionServicio;
	}

	public boolean isCapaMotivoAnulacion() {
		return capaMotivoAnulacion;
	}

	public void setCapaMotivoAnulacion(boolean capaMotivoAnulacion) {
		this.capaMotivoAnulacion = capaMotivoAnulacion;
	}

	public boolean isPortatilClone() {
		return portatilClone;
	}

	public void setPortatilClone(boolean portatilClone) {
		this.portatilClone = portatilClone;
	}

	public int getPosEliminar() {
		return posEliminar;
	}

	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

	/**
	 * @return the serviciosIncluidos
	 */
	public HashMap<Object, Object> getServiciosIncluidos() {
		return serviciosIncluidos;
	}

	/**
	 * @param serviciosIncluidos the serviciosIncluidos to set
	 */
	public void setServiciosIncluidos(HashMap<Object, Object> serviciosIncluidos) {
		this.serviciosIncluidos = serviciosIncluidos;
	}

	
	/**
	 * @return the articulosIncluidos
	 */
	public HashMap<Object, Object> getArticulosIncluidos() {
		return articulosIncluidos;
	}

	/**
	 * @param articulosIncluidos the articulosIncluidos to set
	 */
	public void setArticulosIncluidos(HashMap<Object, Object> articulosIncluidos) {
		this.articulosIncluidos = articulosIncluidos;
	}

	/**
	 * @return the articulosIncluidos
	 */
	public Object getArticulosIncluidos(String key) 
	{
		return articulosIncluidos.get(key);
	}

	/**
	 * @param articulosIncluidos the articulosIncluidos to set
	 */
	public void setArticulosIncluidos(Object key, Object value)
	{
		this.articulosIncluidos.put(key, value);
	}

	/**
	 * @return the serviciosIncluidos
	 */
	public Object getServiciosIncluidos(Object key) {
		return serviciosIncluidos.get(key);
	}

	/**
	 * @param serviciosIncluidos the serviciosIncluidos to set
	 */
	public void setServiciosIncluidos(Object key, Object value) {
		this.serviciosIncluidos.put(key, value);
	}

	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean isIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}

	/**
	 * @return the incluyeServiciosArticulos
	 */
	public boolean getIncluyeServiciosArticulos() {
		return incluyeServiciosArticulos;
	}
	
	/**
	 * @param incluyeServiciosArticulos the incluyeServiciosArticulos to set
	 */
	public void setIncluyeServiciosArticulos(boolean incluyeServiciosArticulos) {
		this.incluyeServiciosArticulos = incluyeServiciosArticulos;
	}

	/**
	 * @return the arrayArticuloIncluidoDto
	 */
	public ArrayList<DtoArticuloIncluidoSolProc> getArrayArticuloIncluidoDto() {
		return arrayArticuloIncluidoDto;
	}

	/**
	 * @param arrayArticuloIncluidoDto the arrayArticuloIncluidoDto to set
	 */
	public void setArrayArticuloIncluidoDto(
			ArrayList<DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto) {
		this.arrayArticuloIncluidoDto = arrayArticuloIncluidoDto;
	}

	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		if(hiddens==null)hiddens="";
		return hiddens;
	}

	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	} 

	/**
	 * @return the justificacionMap
	 */
	public HashMap<String, Object> getJustificacionMap() {
		return justificacionMap;
	}

	/**
	 * @param justificacionMap the justificacionMap to set
	 */
	public void setJustificacionMap(HashMap<String, Object> justificacionMap) {
		this.justificacionMap = justificacionMap;
	}

	/**
	 * @return the descripcionCentroCostoSolicitado
	 */
	public String getDescripcionCentroCostoSolicitado() {
		return descripcionCentroCostoSolicitado;
	}

	/**
	 * @param descripcionCentroCostoSolicitado the descripcionCentroCostoSolicitado to set
	 */
	public void setDescripcionCentroCostoSolicitado(
			String descripcionCentroCostoSolicitado) {
		this.descripcionCentroCostoSolicitado = descripcionCentroCostoSolicitado;
	}

	/**
	 * @return the numerosSolicitudes
	 */
	public HashMap<String, Object> getNumerosSolicitudes() {
		return numerosSolicitudes;
	}

	/**
	 * @param numerosSolicitudes the numerosSolicitudes to set
	 */
	public void setNumerosSolicitudes(HashMap<String, Object> numerosSolicitudes) {
		this.numerosSolicitudes = numerosSolicitudes;
	}

	/**
	 * @return the numerosSolicitudes
	 */
	public Object getNumerosSolicitudes(String key) {
		return numerosSolicitudes.get(key);
	}

	/**
	 * @param numerosSolicitudes the numerosSolicitudes to set
	 */
	public void setNumerosSolicitudes(String key, Object value) {
		this.numerosSolicitudes.put(key, value);
	}

	
	
	public HashMap<String, Object> getValAnulacion() {
		return valAnulacion;
	}

	public void setValAnulacion(HashMap<String, Object> valAnulacion) {
		this.valAnulacion = valAnulacion;
	}

	
	public Object getValAnulacion(String key) {
		return valAnulacion.get(key);
	}

	/**
	 * @param numerosSolicitudes the numerosSolicitudes to set
	 */
	public void setValAnulacion(String key, Object value) {
		this.valAnulacion.put(key, value);
	}
	/**
	 * @return the consecutivosOrdenesMedicas
	 */
	public Vector<String> getConsecutivosOrdenesMedicas() {
		return consecutivosOrdenesMedicas;
	}

	/**
	 * @param consecutivosOrdenesMedicas the consecutivosOrdenesMedicas to set
	 */
	public void setConsecutivosOrdenesMedicas(
			Vector<String> consecutivosOrdenesMedicas) {
		this.consecutivosOrdenesMedicas = consecutivosOrdenesMedicas;
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
	 * Método que verifica si aun existen servicios de procedimientos
	 * con cantidad pendeinte por generar solicitud
	 * @return
	 */
	public boolean existenServiciosCantidadesPendientes()
	{
		boolean existen = false;
		for(int i=0;i<this.getNumeroServicios();i++)
		{
			if(!this.getValores("justificar_"+i).toString().equals("pendiente")
			&&
			Integer.parseInt((String)this.getValores("cantidad_" + i))>0)
			{
				existen = true;
			}
		}
		return existen;
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

	public boolean isValidarEgresoPaciente() {
		return validarEgresoPaciente;
	}

	public void setValidarEgresoPaciente(boolean validarEgresoPaciente) {
		this.validarEgresoPaciente = validarEgresoPaciente;
	}

	public String getTipoCieDiagnostico() {
		return tipoCieDiagnostico;
	}

	public void setTipoCieDiagnostico(String tipoCieDiagnostico) {
		this.tipoCieDiagnostico = tipoCieDiagnostico;
	}

	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}

	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}

	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	@SuppressWarnings("rawtypes")
	public void setMensajesAdvertenciaMap(HashMap mensajesAdvertenciaMap) {
		this.mensajesAdvertenciaMap = mensajesAdvertenciaMap;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getMensajesAdvertenciaMap() {
		return mensajesAdvertenciaMap;
	}

	/**
     * Set del mapa de mensajes 
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
	public void setMensajesAdvertenciaMap(String key, Object value) 
    {
        mensajesAdvertenciaMap.put(key, value);
    }
    /**
     * Get del mapa de mensajes 
     */
    public Object getMensajesAdvertenciaMap(String key) 
    {
        return mensajesAdvertenciaMap.get(key);
    }

	public void setOrdenAmbulPendienteAutoriz(boolean ordenAmbulPendienteAutoriz) {
		this.ordenAmbulPendienteAutoriz = ordenAmbulPendienteAutoriz;
	}

	public boolean isOrdenAmbulPendienteAutoriz() {
		return ordenAmbulPendienteAutoriz;
	}

	public void setListaServiciosImpimirOrden(
			ArrayList<String> listaServiciosImpimirOrden) {
		this.listaServiciosImpimirOrden = listaServiciosImpimirOrden;
	}

	public ArrayList<String> getListaServiciosImpimirOrden() {
		return listaServiciosImpimirOrden;
	}

	public void setExisteAutorizacionOrden(boolean existeAutorizacionOrden) {
		this.existeAutorizacionOrden = existeAutorizacionOrden;
	}

	public boolean isExisteAutorizacionOrden() {
		return existeAutorizacionOrden;
	}

	public void setCentroCostoAutorizacion(DtoCheckBox centroCostoAutorizacion) {
		this.centroCostoAutorizacion = centroCostoAutorizacion;
	}

	public DtoCheckBox getCentroCostoAutorizacion() {
		return centroCostoAutorizacion;
	}

	public void setBotonGenerarSolicitud(boolean botonGenerarSolicitud) {
		this.botonGenerarSolicitud = botonGenerarSolicitud;
	}

	public boolean isBotonGenerarSolicitud() {
		return botonGenerarSolicitud;
	}

	/**
	 * @param generaAlertaEnfermeria the generaAlertaEnfermeria to set
	 */
	public void setGeneraAlertaEnfermeria(String generaAlertaEnfermeria) {
		this.generaAlertaEnfermeria = generaAlertaEnfermeria;
	}

	/**
	 * @return the generaAlertaEnfermeria
	 */
	public String getGeneraAlertaEnfermeria() {
		if(UtilidadTexto.isEmpty(generaAlertaEnfermeria)) {
			generaAlertaEnfermeria = ConstantesBD.acronimoNo;
		}
		return generaAlertaEnfermeria;
	}
	
	/**
	 * @return List<InfoResponsableCobertura>
	 */
	public List<InfoResponsableCobertura> getInfoCoberturaServicio() {
		return infoCoberturaServicio;
	}

	/**
	 * @param infoCoberturaServicio
	 */
	public void setInfoCoberturaServicio(List<InfoResponsableCobertura> infoCoberturaServicio) {
		this.infoCoberturaServicio = infoCoberturaServicio;
	}
	
	public Date getFechaSolicitudDate() {
		return fechaSolicitudDate;
	}

	public void setFechaSolicitudDate(Date fechaSolicitudDate) {
		this.fechaSolicitudDate = fechaSolicitudDate;
	}
	
}