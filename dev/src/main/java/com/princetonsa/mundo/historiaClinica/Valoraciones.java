/*
 * Mayo 6, 2008
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Collections;
import java.util.Date;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EgresoDao;
import com.princetonsa.dao.historiaClinica.ValoracionesDao;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.atencion.HistoricoEvoluciones;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.RegistroEnvioInfAtencionIniUrg;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.RegistroIncapacidades;
import com.princetonsa.mundo.pyp.ProgramasPYP;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudInterconsulta;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.servinte.axioma.bl.ordenes.impl.SolicitudesMundo;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.Ingresos;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Valoraciones
 */
@SuppressWarnings("unchecked")
public class Valoraciones 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(Valoraciones.class);
	
	/**
	 * DAO de VALORACIONES	
	 */
	private static ValoracionesDao valoracionDao = null;
	/**
	 * DAO de egresos
	 */
	private EgresoDao egresoDao = null;
	
	private DtoValoracionUrgencias valoracionUrgencias;
	private DtoValoracionHospitalizacion valoracionHospitalizacion;
	private DtoValoracion valoracion;
	private DtoPlantilla plantilla;
	/**
	 * Arreglo que contiene los códigos de los tipos de componente
	 * que aplican para la plantilla dde la valoracion
	 */
	private ArrayList<Integer> tiposComponente;
	
	/**
	 * 
	 */
	private String codEspecialidadProfesionalResponde;
	
	//Arreglo de los estados de conciencia
	private ArrayList<HashMap<String, Object>> estadosConciencia;
	private ArrayList<HashMap<String, Object>> causasExternas;
	private ArrayList<HashMap<String, Object>> finalidades;
	private ArrayList<HashMap<String, Object>> conductasValoracion;
	private ArrayList<HashMap<String, Object>> tiposDiagnostico;
	private ArrayList<HashMap<String, Object>> tiposMonitoreo;
	private ArrayList<HashMap<String, Object>> tiposRecargo;
	//Arreglos para los componentes
	private ArrayList<HashMap<String, Object>> rangosEdadMenarquia;
	private ArrayList<HashMap<String, Object>> rangosEdadMenopausia;
	private ArrayList<HashMap<String, Object>> conceptosMenstruacion;
	
	//Mapa para cargar temporalmente los diagnósticos relacionados
	private HashMap<String, Object> diagnosticosRelacionados = new HashMap<String, Object>();
	
	/**
	 * Listado de los diagnosticos seleccionados
	 */
	private String diagnosticosSeleccionados;
	
	//Atributo para manejo de errores
	private ActionErrors errores = new ActionErrors();
	
	/**
	 * Variable que indica si se debe abrir la referencia
	 */
	private boolean deboAbrirReferencia;
	
	/**
	 * Número de solicitud de la valoracion
	 */
	private String numeroSolicitud;
	
	/**
	 * Arreglo que maneja los mensajes de advertencia
	 */
	private ArrayList<ElementoApResource> advertencias;
	
	/**
	 * Variable que indica si se puede modificar la conducta de la valoracion
	 */
	private boolean puedoModificarConductaValoracion;
	
	/**
	 * Variable que indica si la valoracion tiene egreso
	 */
	private boolean existeEgreso;
	
	//Atributos para la validación del reingreso
	private String fechaEgresoAnterior;
	private String horaEgresoAnterior;
	private Diagnostico diagnosticoEgresoAnterior;
	private boolean validacionReingreso; //indica si la validación del reingreso fue exitosa
	
	
	//*******+ATRIBUTOS USADOS PARA LA VALORACIÓN DE CONSULTA********************
	private boolean pyp; //Campo para saber si es PYP
	private boolean unificarPyp; //Campo para saber si se debe unificar PYP
	private boolean vieneDePyp; //Campo para saber si el flujo viene de pyp
	//private String numeroAutorizacion;
	private String codigoCita;
	private String fechaConsulta;
	private String horaConsulta;
	
	//********ATRIBUTOS USADOS PARA LA VALORACION INTERCONSULTA*****************+
	private int codigoManejo;
	private String aceptaCambioTratante; //para almacenar la respuesta de acepta cambio tratante
	private boolean deboMostrarAceptaCambioTratante; //variable que indica si se debe mostrar el cambio de tratante
	
	
	/**
	 *Atributo para definir el estado de embarazo o no 
	 */
	private Boolean estadoEmbarazada;
	
	
	/**
	 *atributo que contiene el registro medico del profesional que modifica el embarazo 
	 */
	private String registroMedico;
	
	
	/**
	 * atributo que contiene el usuario del profesional de la salud 
	 */
	private String usuarioProfesionalSalud;
	
	
	
	//********CONSTRUCTORES E INICIALIZADORES********************
	
	/**
	 * Constructor
	 */
	public Valoraciones() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.valoracionUrgencias = new DtoValoracionUrgencias();
		this.valoracionHospitalizacion = new DtoValoracionHospitalizacion();
		this.valoracion = new DtoValoracion();
		
		this.plantilla = new DtoPlantilla();
		this.tiposComponente = new ArrayList<Integer>();
		this.estadosConciencia = new ArrayList<HashMap<String,Object>>();
		this.causasExternas = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.conductasValoracion = new ArrayList<HashMap<String,Object>>();
		this.tiposDiagnostico = new ArrayList<HashMap<String,Object>>();
		this.tiposMonitoreo = new ArrayList<HashMap<String,Object>>();
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		this.diagnosticosSeleccionados = "";
		this.tiposRecargo = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenarquia = new ArrayList<HashMap<String,Object>>();
		this.rangosEdadMenopausia = new ArrayList<HashMap<String,Object>>();
		this.conceptosMenstruacion = new ArrayList<HashMap<String,Object>>();
		
		this.errores = new ActionErrors();
		
		this.deboAbrirReferencia = false;
		
		this.numeroSolicitud = "";
		
		this.advertencias = new ArrayList<ElementoApResource>();
		this.puedoModificarConductaValoracion = false;
		this.existeEgreso = false;
		this.codEspecialidadProfesionalResponde="";
		
		//Atributos para la validación del reingreso
		this.fechaEgresoAnterior = "";
		this.horaEgresoAnterior = "";
		this.diagnosticoEgresoAnterior = new Diagnostico();
		this.validacionReingreso = false;
		
		//Atributos flujo de valoracion consulta
		this.pyp = false;
		this.unificarPyp = false;
		this.vieneDePyp = false;
		//this.numeroAutorizacion = "";
		this.codigoCita = "";
		this.fechaConsulta = "";
		this.horaConsulta = "";
		
		//Atributos flujo de valoracion interconsulta
		this.codigoManejo = ConstantesBD.codigoNuncaValido;
		this.aceptaCambioTratante = "";
		this.deboMostrarAceptaCambioTratante = false;
		this.estadoEmbarazada=false;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (Valoraciones.valoracionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			Valoraciones.valoracionDao = myFactory.getValoracionesDao();
		}
		
		if (egresoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			egresoDao = myFactory.getEgresoDao();
		}	
	}
	
	/**
	 * Método para obtener una instancia del DAO de valoraciones
	 * @return
	 */
	public static ValoracionesDao valoracionDao()
	{
		if(Valoraciones.valoracionDao==null)
		{
			Valoraciones.valoracionDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionesDao();
		}
		return Valoraciones.valoracionDao;
	}
	//************METODOS****************************************
	
	/**
	 * Método para preparar los datos de la solicitud de interconsulta
	 */
	@SuppressWarnings("deprecation")
	public void precargarBaseInterconsulta(Connection con, PersonaBasica paciente, UsuarioBasico usuario, String numeroSolicitud)
	{
		//Se hace una precarga que corresponde a los datos fijos estandar de la plantilla de valoracion consulta
		this.precargarGenericoConsulta(con,usuario,paciente,numeroSolicitud);
		
		
		//Se intenta cargar la información de la solicitud de interconsultas
		boolean exito = false;
		SolicitudInterconsulta mundoSolicitud = new SolicitudInterconsulta();
		
		try
		{
			exito = mundoSolicitud.cargar(con, Integer.parseInt(this.numeroSolicitud));
		}
		catch(Exception e)
		{
			logger.error("Error cargando la solicitud de interconsulta: "+e);
			exito = false;
			this.errores.add("",new ActionMessage("errors.problemasGenericos","cargando la solicitud "+numeroSolicitud+": "+e));
		}
		
		if(exito)
		{
			//Se asigna el número de autorización de la solicitud
			//this.numeroAutorizacion = mundoSolicitud.getNumeroAutorizacion();
			
			//Se consulta la causa externa de la valoracion
			InfoDatosInt causaExternaVal = Valoraciones.valoracionDao.obtenerCausaExternaValoracion(con, paciente.getCodigoCuenta()+"");
			if(causaExternaVal.getCodigo()>0)
			{
				this.valoracion.setCodigoCausaExterna(causaExternaVal.getCodigo());
				this.valoracion.setDescripcionCausaExterna(causaExternaVal.getDescripcion());
			}
			
			//Se consulta la finalidad de la consulta
			InfoDatosString finalidadConsultaVal = Valoraciones.valoracionDao.obtenerFinalidadConsultaValoracion(con, paciente.getCodigoCuenta()+"");
			if(!finalidadConsultaVal.getCodigo().equals(""))
				this.valoracion.getValoracionConsulta().setFinalidadConsulta(finalidadConsultaVal);
			
			//Se consultan los últimos diagnosticos del paciente
			ArrayList<Diagnostico> ultimosDiagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticosPaciente(con, paciente.getCodigoPersona()+"");
			if(ultimosDiagnosticos.size()>0)
			{
				this.valoracion.setDiagnosticos(ultimosDiagnosticos);
				this.asignarDiagnosticosRelacionados();
				//Se editan los diagnosticos seleccioandos
				for(Diagnostico diagnostico:this.valoracion.getDiagnosticos())
					this.diagnosticosSeleccionados += (this.diagnosticosSeleccionados.length()>0?",":"") + "'"+diagnostico.getAcronimo()+"'";
				
				//Se deja solo el diagnostico principal (elemento 0)
				this.valoracion.setDiagnosticos(new ArrayList<Diagnostico>());
				this.valoracion.getDiagnosticos().add(ultimosDiagnosticos.get(0));
				//Se asigna también el tipo de diagnostico encontrado
				this.valoracion.getValoracionConsulta().setCodigoTipoDiagnostico(this.valoracion.getDiagnosticos().get(0).getCodigoTipoDiagnostico());
				this.valoracion.getValoracionConsulta().setNombreTipoDiagnostico(this.valoracion.getDiagnosticos().get(0).getNombreTipoDiagnostico());
			}
			
			//***********************VALIDACION MANEJO INTERCONSULTA************************************************
			//Se obtiene el codigo del manejo de interconsulta
			this.codigoManejo = mundoSolicitud.getCodigoManejointerconsulta();
			//Inicialmente no se puede aceptar el cambio de manejo de tratante
			this.deboMostrarAceptaCambioTratante = false;
			//Si el codigo del manejo es Manejo Conjunto se agrega advertencia
			if(this.codigoManejo==ConstantesBD.codigoManejoConjunto)
			{
				ElementoApResource mensaje = new ElementoApResource("error.validacionessolicitud.manejoConjuntoActivado");
				this.advertencias.add(mensaje);
			}
			//Si el manejo es Se Transfiere manejo paciente, se verifica que haya una solicitud de cambio de paciente
			else if(this.codigoManejo==ConstantesBD.codigoSeTransfiereManejoPaciente)
			{
				if(ValidacionesSolicitud.tieneSolicitudCambioTratante(con, Integer.parseInt(this.numeroSolicitud)))
					this.deboMostrarAceptaCambioTratante = true;		
			}
			//*********************************************************************************************************
				
		}
		
		//Se consulta el arreglo de tipos de recargp
		this.tiposRecargo = UtilidadesFacturacion.obtenerTiposRecargo(con);
	}
	
	/**
	 * Método para precargar la valoración de consulta externa
	 * @param numeroSolicitud 
	 * @param finalidad 
	 * @param vieneDePyp 
	 * @param codigoCita 
	 */
	public void precargarBaseConsulta(Connection con,PersonaBasica paciente,UsuarioBasico usuario, String numeroSolicitud, boolean vieneDePyp, InfoDatosString finalidad, String codigoCita)
	{
		//Se hace una precarga que corresponde a los datos fijos estandar de la plantilla de valoracion consulta
		this.precargarGenericoConsulta(con,usuario,paciente,numeroSolicitud);
		
		
		boolean exito = false;
		//Se carga la solicitud
		Solicitud mundoSolicitud = new Solicitud();
		try 
		{
			exito = mundoSolicitud.cargar(con, Integer.parseInt(numeroSolicitud));
		} 
		catch (Exception e) 
		{
			logger.error("Error cargando solicitud en precargarBaseConsulta: "+e);
			exito = false;
			this.errores.add("",new ActionMessage("errors.problemasGenericos","cargando la solicitud "+numeroSolicitud+": "+e));
		} 
				
		if(exito)
		{
			//************MANEJO DE ADVERTENCIA FLUJO REFERENCIA**************************************
			String mensajeStr = UtilidadesHistoriaClinica.getMensajeReferenciaParaValidacion(con, 0, numeroSolicitud, ConstantesIntegridadDominio.acronimoExterna);
			//Si había mensaje se agrega a la estructura de advertencias
			if(!mensajeStr.equals(""))
			{
				ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
				mensaje.agregarAtributo(mensajeStr);
				this.advertencias.add(mensaje);
			}
			//*********************************************************************************************
			
			//***********VALIDACIONES PYP********************************************************************
			this.pyp = mundoSolicitud.isSolPYP();
			//Si la orden venía de pyp se asigna la finalidad que viene por parámetro
			if(vieneDePyp)
				this.valoracion.getValoracionConsulta().setFinalidadConsulta(finalidad);
			//Si la orden no vieneDePyp pero es de PYP de todas maneras se debe consultar la finalidad de la actividad
			else if(this.pyp)
			{
				InfoDatos finalidadPyp = ProgramasPYP.consultarFinalidadActividadConsulta(con,this.numeroSolicitud);
				this.valoracion.getValoracionConsulta().setCodigoFinalidadConsulta(finalidadPyp.getId());
				this.valoracion.getValoracionConsulta().setNombreFinalidadConsulta(finalidadPyp.getNombre());
			}
			
			//Si la solicitud es PYP se verifica si el convenio de la cuenta debe unificar pyp
			this.unificarPyp = Utilidades.tieneConvenioUnificarPYP(con, this.numeroSolicitud);
			//************************************************************************************************
			
			//**************SE ASIGNA EL NUMERO DE AUTORIZACION DE LA SOLICITUD******************
			//this.numeroAutorizacion = mundoSolicitud.getNumeroAutorizacion();
			//************************************************************************************
			
			//********SE CARGAN LOS DATOS DE LA CITA**********************************
			String[] fechaHoraCita = UtilidadesConsultaExterna.obtenerFechaHoraCita(con, codigoCita);
			this.fechaConsulta = fechaHoraCita[0];
			this.horaConsulta = fechaHoraCita[1];
			//************************************************************************
		}
	}
	
	/**
	 * Método implementado para hacer un precarga genérica de datos que tienen que ver con la plantilla de consulta
	 * @param con 
	 * @param usuario
	 * @param numeroSolicitud 
	 * @param paciente 
	 */
	private void precargarGenericoConsulta(Connection con, UsuarioBasico usuario, PersonaBasica paciente, String numeroSolicitud) 
	{
		this.numeroSolicitud = numeroSolicitud;
		this.valoracion = (DtoValoracion)precargarBase(con, paciente, usuario, null/*Ni de urgencias , ni de hospitalizacion*/);
		this.valoracion.setNumeroSolicitud(this.numeroSolicitud);
		
		
		
		//**************SECCION CONCEPTO CONSULTA*********************************************
		//Se agrega concepto de la consulta
		DtoValoracionObservaciones conceptoConsulta = new DtoValoracionObservaciones();
		conceptoConsulta.setTipo(ConstantesIntegridadDominio.acronimoConceptoConsulta);
		conceptoConsulta.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoConceptoConsulta).toString());
		conceptoConsulta.setProfesional(usuario);
		valoracion.getObservaciones().add(conceptoConsulta);
		//*******************************************************************************
		
		//***********SECCION DOCUMENTOS ADJUNTOS************************************
		//Se agrega comentario adicional historia clinica
		DtoValoracionObservaciones comentarioAdicional = new DtoValoracionObservaciones();
		comentarioAdicional.setTipo(ConstantesIntegridadDominio.acronimoComentariosAdicionalesHC);
		comentarioAdicional.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosAdicionalesHC).toString());
		comentarioAdicional.setProfesional(usuario);
		valoracion.getObservaciones().add(comentarioAdicional);
		//****************************************************************************
		
	}
	/**
	 * Método para precargar una valoracion de urgencias
	 */
	public void precargarBaseUrgencias(Connection con,PersonaBasica paciente,UsuarioBasico usuario)
	{
		this.valoracionUrgencias = (DtoValoracionUrgencias)precargarBase(con, paciente, usuario,true);
		
		//***************EXAMEN FISICOS***********************************************************************
		this.estadosConciencia = UtilidadesHistoriaClinica.cargarEstadosConciencia(con, usuario.getCodigoInstitucionInt());
		this.valoracionUrgencias.setInformacionTriage(Valoraciones.valoracionDao.consultarInformacionTriageUrgencias(con, paciente.getCodigoCuenta()+""));
		//****************************************************************************************************
		//********CONDUCTAS DE LA VALORACION****************************************************************
		this.conductasValoracion = UtilidadesHistoriaClinica.cargarConductasValoracion(con);
		//Tipos de monitoreo (Se usan cuando se selecciona conducta Traslado Cuidado Especial)
		this.tiposMonitoreo = UtilidadesManejoPaciente.obtenerTiposMonitoreo(con, usuario.getCodigoInstitucionInt());
		//**************************************************************************************************
		
		//logger.info("especialidad URGENCIAS UNO >>"+usuario.getEspecialidades1()[0].getNombre());
		//***************** Especialidad de Profesional que responde  **************************************
		   if(usuario.getEspecialidades1().length==1)
		   {
			   this.valoracionUrgencias.setEspecialidadResponde(usuario.getEspecialidades1()[0].getCodigo()+"");   
			   this.valoracionUrgencias.setNomEspecialidadResponde(usuario.getEspecialidades1()[0].getNombre()); 
		   }
		   
		//*************************************************************
	}
	
	/**
	 * Método para precargar una valoración de hospitalización
	 * @param con
	 * @param paciente
	 * @param usuario
	 */
	public void precargarBaseHospitalizacion(Connection con, PersonaBasica paciente,UsuarioBasico usuario)
	{
		this.valoracionHospitalizacion = (DtoValoracionHospitalizacion)precargarBase(con, paciente, usuario,false);
		
		//***********INFORMACIÓN GENERAL****************************************************+
		//Se consulta el origen de admisión de la cuenta
		this.valoracionHospitalizacion.setOrigenAdmision(UtilidadesManejoPaciente.obtenerOrigenAdmisionCuenta(con, paciente.getCodigoCuenta()));
		//************************************************************************************
		//***********DIAGNÓSTICOS**************************************************************
		if(this.valoracionHospitalizacion.getCodigoOrigenAdmision()==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
		{
			logger.info("PASO POR AQUI PARA CONSULTAR EL DIAGNOSTICO DE LA REFERENCIA INTERNA");
			//Diagnostico de ingreso
			String diagIngreso = UtilidadesHistoriaClinica.getDxIngresoDeReferencia(con, paciente.getCodigoCuenta()+"");
			if(!diagIngreso.equals(""))
			{
				String[] vectorDiag = diagIngreso.split(ConstantesBD.separadorSplit);
				this.valoracionHospitalizacion.getDiagnosticoIngreso().setAcronimo(vectorDiag[0]);
				this.valoracionHospitalizacion.getDiagnosticoIngreso().setTipoCIE(Integer.parseInt(vectorDiag[1]));
				this.valoracionHospitalizacion.getDiagnosticoIngreso().setNombre(vectorDiag[2]);
			}
		}
		//*************************************************************************************
		logger.info("especialidad hospitalizacion UNO >>"+usuario.getEspecialidades1()[0].getNombre());
		//***************** Especialidad de Profesional que responde  **************************************
		   if(usuario.getEspecialidades1().length==1)
		   {
			   this.valoracionHospitalizacion.setEspecialidadProfResponde(usuario.getEspecialidades1()[0].getCodigo()+"");
			   this.valoracionHospitalizacion.setNomEspecialidadProfResponde(usuario.getEspecialidades1()[0].getNombre());
		   }
		   
		//*************************************************************
	}
	
	/**
	 * Método para precargar una valoracio base
	 * @param con
	 * @param paciente
	 * @return
	 */
	private DtoValoracion precargarBase(Connection con, PersonaBasica paciente,UsuarioBasico usuario,Boolean esUrgencias)
	{
		//Inicialmente se carga el listado de tipos componente
		this.cargarTiposComponente();
		
		DtoValoracion valoracion = new DtoValoracion();
		
		if(esUrgencias!=null)
		{
			if(esUrgencias)
			{
				 valoracion = new DtoValoracionUrgencias();
			}
			else
			{
				 valoracion = new DtoValoracionHospitalizacion();
			}
		}
		
		//*******************INFORMACION GENERAL*******************************************************
		valoracion.setFechaValoracion(UtilidadFecha.getFechaActual(con));
		valoracion.setHoraValoracion(UtilidadFecha.getHoraActual(con));
		valoracion.setEdad(UtilidadFecha.calcularEdadDetallada(paciente.getFechaNacimiento(), valoracion.getFechaValoracion()));
		
		//Se consulta el tipo de evento de la cuenta
		String idCuenta = "";
		if(paciente.getCodigoCuenta()>0)
			idCuenta = paciente.getCodigoCuenta()+"";
		else
			idCuenta = Utilidades.getCuentaSolicitud(con, Utilidades.convertirAEntero(this.numeroSolicitud)) + "";
		
		/**
		 * MT 5568
		 */	
		try {
			InfoDatosInt centroCostoPaciente = this.obtenerDatosCentroCostoXSolcitud(con, Integer.parseInt(this.numeroSolicitud));
			valoracion.setDatoAreaPaciente(centroCostoPaciente);			
		} catch (Exception e) {
			logger.error(e);
		}

		/**
		 * Fin MT 5568
		 */			

		
		valoracion.setCodigoTipoEvento(Cuenta.obtenerCodigoTipoEventoCuenta(con, idCuenta));
		if(valoracion.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo))
			valoracion.setFueAccidenteTrabajo(true);
		else
			valoracion.setFueAccidenteTrabajo(null);
		
		//Se agrega una observación de tipo motivo consulta
		DtoValoracionObservaciones motivoConsulta = new DtoValoracionObservaciones();
		motivoConsulta.setTipo(ConstantesIntegridadDominio.acronimoMotivoConsulta);
		motivoConsulta.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoMotivoConsulta).toString());
		motivoConsulta.setProfesional(usuario);
		valoracion.getObservaciones().add(motivoConsulta);
		//Se agrega la observación de enfermedad actual
		/**
		 * Se deshabilitan estas observaciones por tarea [id=15985] Xplanner 2008
		 * 
		 * DtoValoracionObservaciones enfermedadActual = new DtoValoracionObservaciones();
		enfermedadActual.setTipo(ConstantesIntegridadDominio.acronimoEnfermedadActual);
		enfermedadActual.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEnfermedadActual).toString());
		enfermedadActual.setProfesional(usuario);
		valoracion.getObservaciones().add(enfermedadActual);*/
		//************************************************************************************************
		//*********************PYP**************************************************************************
		valoracion.setAbrirPYP(UtilidadValidacion.tienePacienteInformacionPYP(con, paciente, usuario.getCodigoInstitucionInt())&&Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),496));
		//***************************************************************************************************
		//*****************REVISION POR SISTEMAS*************************************************************
		valoracion.setRevisionesSistemas(UtilidadesHistoriaClinica.cargarRevisionesSistemas(con, this.tiposComponente));
		//****************************************************************************************************
		//*****************CAUSAS EXTERNAS**************************************************************
		this.causasExternas = UtilidadesHistoriaClinica.obtenerCausasExternas(con,false);
		//Se toma la causa externa de parámetros generales
		valoracion.setCodigoCausaExterna(Utilidades.convertirAEntero(ValoresPorDefecto.getCausaExterna(usuario.getCodigoInstitucionInt()), true));
		//***********************************************************************************************
		//*****************FINALIDADES CONSULTA***********************************************************
		this.finalidades = UtilidadesHistoriaClinica.obtenerFinalidadesConsulta(con);
		//Se toma la finalidad de parámetros generales
		valoracion.getValoracionConsulta().setCodigoFinalidadConsulta(ValoresPorDefecto.getFinalidad(usuario.getCodigoInstitucionInt()));
		//*************************************************************************************************
		//*****************DIAGNOSTICOS**********************************************************************
		this.tiposDiagnostico = UtilidadesHistoriaClinica.cargarTiposDiagnostico(con);
		Diagnostico diagPrincipal = new Diagnostico();
		diagPrincipal.setPrincipal(true);
		valoracion.getDiagnosticos().add(diagPrincipal); //inicialmente se empieza con un diagnóstico que es el principal
		//*****************************************************************************************************
		//*******************OBSERVACIONES**********************************************************************
		this.prepararNuevasObservaciones(usuario, valoracion);
		//*******************************************************************************************************
		//*******************PROFESIONAL RESPONSABLE***********************************************************
		valoracion.setProfesional(usuario);
		valoracion.setFechaGrabacion(valoracion.getFechaValoracion());
		valoracion.setHoraGrabacion(valoracion.getHoraValoracion());
		 
		//******************************************************************************************************
		
		
		//**********Se cargan COMPONENTES*******************************
		//Si existe en la plantilla el componente de signos vitales se cargan los signos vitales
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			valoracion.setSignosVitales(UtilidadesHistoriaClinica.cargarSignosVitales(con));
		//Si existe en la plantilla el componente de historia menstrual se cargan los antecedentes de gineco
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			valoracion.setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", ""));
			this.rangosEdadMenarquia = UtilidadesHistoriaClinica.obtenerRangosEdadMenarquia(con, false);
			this.rangosEdadMenopausia = UtilidadesHistoriaClinica.obtenerRangosEdadMenopausia(con, false);
			this.conceptosMenstruacion = UtilidadesHistoriaClinica.obtenerConceptosMenstruacion(con, false);
		}
		//Si existe en la plantilla el componente de oftalmologia se carga la parametrizacion
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
		{
			valoracion.setOftalmologia(UtilidadesHistoriaClinica.cargarOftalmologia(con, "", usuario.getCodigoInstitucionInt()));
			valoracion.getOftalmologia().setNumeroSolicitud(this.numeroSolicitud);
		}
		//Si existe en la plantilla el componente de pediatría se carga la parametrizacion
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria))
		{
			valoracion.setPediatria(UtilidadesHistoriaClinica.cargarPediatria(con, paciente.getCodigoPersona()+"", this.numeroSolicitud, paciente.getEdad(), usuario));
			//se preparan nuevas observaciones
			valoracion.getPediatria().prepararNuevasObservaciones(usuario);
		}
		
		return valoracion;	
		
	}
	
	/**
	 * MT 5568
	 * Metodo encargado de cargar la informacion del centro de costo (o Area) asociada a la solicitud.
	 * @param con
	 * @param idSolicitud
	 * @return
	 * @throws IPSException
	 */
	public InfoDatosInt obtenerDatosCentroCostoXSolcitud(Connection con, int idSolicitud) throws IPSException {

		try {
			Cuenta cuenta = new Cuenta();
			return cuenta.obtenerDatosCentroCostoXSolcitud(con, idSolicitud);
		} catch (BDException e) {			
			throw e;
		} catch (IPSException e) {
			throw e;
		} 
	}
	
	
	/**
	 * Método para preparar nuevas observaciones
	 * @param usuario
	 * @param valoracion
	 */
	public void prepararNuevasObservaciones(UsuarioBasico usuario, DtoValoracion valoracion)
	{
		//*******************OBSERVACIONES**********************************************************************
		//Se agrega una observación de tipo PLAN DIAGNOSTICOS
		DtoValoracionObservaciones planDiagnostico = new DtoValoracionObservaciones();
		planDiagnostico.setTipo(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico);
		planDiagnostico.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico).toString());
		planDiagnostico.setProfesional(usuario);
		valoracion.getObservaciones().add(planDiagnostico);
		
		
		//Se agrega una observación de tipo COMENTARIOS GENERALES
		DtoValoracionObservaciones comentarioGeneral = new DtoValoracionObservaciones();
		comentarioGeneral.setTipo(ConstantesIntegridadDominio.acronimoComentariosGenerales);
		comentarioGeneral.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosGenerales).toString());
		comentarioGeneral.setProfesional(usuario);
		valoracion.getObservaciones().add(comentarioGeneral);
		
		//Se agrega una observación de tipo PRONOSTICO
		DtoValoracionObservaciones pronostico = new DtoValoracionObservaciones();
		pronostico.setTipo(ConstantesIntegridadDominio.acronimoPronostico);
		pronostico.setLabel(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPronostico).toString());
		pronostico.setProfesional(usuario);
		valoracion.getObservaciones().add(pronostico);
		//*******************************************************************************************************
	}
	
	
	
	/**
	 * Método que carga los tipos de componente
	 *
	 */
	private void cargarTiposComponente()
	{
		for(DtoSeccionFija seccionFija:this.plantilla.getSeccionesFijas())
			for(DtoElementoParam elemento:seccionFija.getElementos())
			{
				//Si el elemento es Componente
				if(elemento.isComponente())
				{
					DtoComponente componente = (DtoComponente)elemento;
					this.tiposComponente.add(componente.getCodigoTipo());
				}
			}
	}
	
	/**
	 * Método usado para verificar si se ingresó información de un componente específico
	 * @param valoracion
	 */
	private void verificarIngresoInformacionComponentes(DtoValoracion valoracion) 
	{
		//Si la plantilla tiene el componente de Signos Vitales y se ingresó información se debe marcar
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales)&&SignoVital.ingresoInformacion(valoracion.getSignosVitales()))
			this.plantilla.marcarComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales);
		
		//Si la plantilla tiene el componente de ginecologia y se ingreso informacion se debe marcar
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia)&&valoracion.getHistoriaMenstrual().ingresoInformacion())
			this.plantilla.marcarComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia);
		
		//Si la plantilla tiene el componente de Oftalmologia y se ingresó información se debe marcar
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia)&&valoracion.getOftalmologia().ingresoInformacion())
			this.plantilla.marcarComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia);
		
		//Si la plantilla tiene el componente de Pediatria y se ingresó información se debe marcar
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria)&&valoracion.getPediatria().ingresoInformacion())
			this.plantilla.marcarComponente(ConstantesCamposParametrizables.tipoComponentePediatria);
		
	}
	
	/**
	 * Método implementado para cargar los diagnósticos relacionados del mapa
	 * a la estructura DTO de las valoraciones
	 *
	 */
	private void cargarDiagnosticosRelacionados()
	{
		int numero = 1;
		
		//Se tratan de borrar los diagnosticos relacionados que ya estaban en el arreglo
		int numDiagArray = (this.valoracionUrgencias.getDiagnosticos().size()-1);
		logger.info("tamaï¿½o diagnosticos antes de: "+this.valoracionUrgencias.getDiagnosticos().size());
		for(int i=numDiagArray;i>0;i--)
		{
			logger.info("voy a eliminar el diagnostico["+i+"]: "+this.valoracionUrgencias.getDiagnosticos().get(i).getAcronimo());
			this.valoracionUrgencias.getDiagnosticos().remove(i);
		}
		logger.info("tamaï¿½o diagnosticos despues de: "+this.valoracionUrgencias.getDiagnosticos().size());
		numDiagArray = (this.valoracionHospitalizacion.getDiagnosticos().size()-1);
		for(int i=numDiagArray;i>0;i--)
		{
			this.valoracionHospitalizacion.getDiagnosticos().remove(i);
		}
		numDiagArray = (this.valoracion.getDiagnosticos().size()-1);
		for(int i=numDiagArray;i>0;i--)
		{
			this.valoracion.getDiagnosticos().remove(i);
		}
		
		
		for(int i=0;i<this.getNumDiagRelacionados();i++)
			if(UtilidadTexto.getBoolean(this.getDiagnosticosRelacionados("checkbox_"+i).toString()))
			{
				String[] vector = this.getDiagnosticosRelacionados(i+"").toString().split(ConstantesBD.separadorSplit);
				Diagnostico diagnostico = new Diagnostico();
				diagnostico.setAcronimo(vector[0]);
				diagnostico.setTipoCIE(Integer.parseInt(vector[1]));
				diagnostico.setNombre(vector[2]);
				diagnostico.setNumero(numero);
				diagnostico.setActivo(true);
				diagnostico.setValorFicha(this.getDiagnosticosRelacionados("valorFicha_"+i)==null?"":this.getDiagnosticosRelacionados("valorFicha_"+i).toString());
				
				
				//aunque se llenen todas las estructuras de todas las valoraciones
				//solo una se usará y eso depende de donde se haya llamado el método
				this.valoracionUrgencias.getDiagnosticos().add(diagnostico);
				this.valoracionHospitalizacion.getDiagnosticos().add(diagnostico);
				this.valoracion.getDiagnosticos().add(diagnostico);
				
				numero++;
			}
	}
	
	/**
	 * Método para cargar al mapa los diagnosticos relacionados que se encuentren en la estructura de la valoración
	 *
	 */
	private void asignarDiagnosticosRelacionados()
	{
		this.diagnosticosRelacionados = new HashMap<String, Object>();
		int contador = 0;
		
		/**
		 * Nota * Cuando este método se llame solo se entrará a un FOR dependiendo
		 * de cual tipo de valoración se esté usando en ese momento
		 */
		for(Diagnostico diagnostico:this.valoracionUrgencias.getDiagnosticos())
			if(!diagnostico.isPrincipal())
			{
				this.setDiagnosticosRelacionados(contador+"", diagnostico.getValor());
				this.setDiagnosticosRelacionados("valorFicha_"+contador, ""); //se inserta ficha vacía
				this.setDiagnosticosRelacionados("checkbox_"+contador, "true"); 
				contador++;
			}
		
		for(Diagnostico diagnostico:this.valoracionHospitalizacion.getDiagnosticos())
			if(!diagnostico.isPrincipal())
			{
				this.setDiagnosticosRelacionados(contador+"", diagnostico.getValor());
				this.setDiagnosticosRelacionados("valorFicha_"+contador, ""); //se inserta ficha vacía
				this.setDiagnosticosRelacionados("checkbox_"+contador, "true");
				contador++;
			}
		
		for(Diagnostico diagnostico:this.valoracion.getDiagnosticos())
			if(!diagnostico.isPrincipal())
			{
				this.setDiagnosticosRelacionados(contador+"", diagnostico.getValor());
				this.setDiagnosticosRelacionados("valorFicha_"+contador, ""); //se inserta ficha vacía
				this.setDiagnosticosRelacionados("checkbox_"+contador, "true");
				contador++;
			}
		
		this.setNumDiagRelacionados(contador);
			
	}
	
	/**
	 * Método implementado para actualizar la fichas notificables
	 * @param con
	 * @param paciente
	 * @param usuario
	 */
	private void actualizarFichasNotificables(Connection con, PersonaBasica paciente)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		
		//Actualización para urgencias
		if(this.valoracionUrgencias.getDiagnosticos().size()>0)
		{
			for(Diagnostico diagnostico:this.valoracionUrgencias.getDiagnosticos())
			{
				resp = Utilidades.actualizarDatosEpidemiologia(con, diagnostico.getValor(), diagnostico.getValorFicha(), this.valoracionUrgencias.getNumeroSolicitud(), paciente, this.valoracionUrgencias.getProfesional());
				if(resp<=0)
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la ficha epidemiológica del diagnóstico "+diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()));
			}
			
			resp = Utilidades.actualizarDatosEpidemiologia(con, this.valoracionUrgencias.getDiagnosticoMuerte().getValor(), this.valoracionUrgencias.getDiagnosticoMuerte().getValorFicha(), this.valoracionUrgencias.getNumeroSolicitud(), paciente, this.valoracionUrgencias.getProfesional());
			if(resp<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la ficha epidemiológica del diagnóstico "+this.valoracionUrgencias.getDiagnosticoMuerte().getAcronimo()+"-"+this.valoracionUrgencias.getDiagnosticoMuerte().getTipoCIE()));
		}
		
		//Actualización para hosptilaizacion
		else if(this.valoracionHospitalizacion.getDiagnosticos().size()>0)
		{
			for(Diagnostico diagnostico:this.valoracionHospitalizacion.getDiagnosticos())
			{
				resp = Utilidades.actualizarDatosEpidemiologia(con, diagnostico.getValor(), diagnostico.getValorFicha(), this.valoracionHospitalizacion.getNumeroSolicitud(), paciente, this.valoracionHospitalizacion.getProfesional());
				if(resp<=0)
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la ficha epidemiológica del diagnóstico "+diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()));
			}
			
			if(!this.valoracionHospitalizacion.getDiagnosticoIngreso().getAcronimo().equals(""))
			{
				resp = Utilidades.actualizarDatosEpidemiologia(con, this.valoracionHospitalizacion.getDiagnosticoIngreso().getValor(), this.valoracionHospitalizacion.getDiagnosticoIngreso().getValorFicha(), this.valoracionHospitalizacion.getNumeroSolicitud(), paciente, this.valoracionHospitalizacion.getProfesional());
				if(resp<=0)
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la ficha epidemiológica del diagnóstico "+this.valoracionHospitalizacion.getDiagnosticoIngreso().getAcronimo()+"-"+this.valoracionHospitalizacion.getDiagnosticoIngreso().getTipoCIE()));
			}
		}
		
		//Actualización para vlaoracion base
		else if(this.valoracion.getDiagnosticos().size()>0)
		{
			for(Diagnostico diagnostico:this.valoracion.getDiagnosticos())
			{
				resp = Utilidades.actualizarDatosEpidemiologia(con, diagnostico.getValor(), diagnostico.getValorFicha(), this.valoracion.getNumeroSolicitud(), paciente, this.valoracion.getProfesional());
				if(resp<=0)
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la ficha epidemiológica del diagnóstico "+diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()));
			}
			
		}
	}
	
	/**
	 * Método implementado para insertar un egreso automático
	 * @param con
	 * @param paciente
	 */
	private void insertarEgresoAutomaticoHospitalizacion(Connection con,PersonaBasica paciente)
	{
		ArrayList diagnosticos=this.valoracionUrgencias.getDiagnosticos();
		Diagnostico diagnosticoPrincipal=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, true);
		Diagnostico diagnosticoRelacionado1=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		Diagnostico diagnosticoRelacionado2=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		Diagnostico diagnosticoRelacionado3=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		for(int i=0; i<diagnosticos.size();i++)
		{
			switch(i)
			{
				case 0:
					diagnosticoPrincipal=(Diagnostico)diagnosticos.get(i);
				break;
				case 1:
					diagnosticoRelacionado1=(Diagnostico)diagnosticos.get(i);
				break;
				case 2:
					diagnosticoRelacionado2=(Diagnostico)diagnosticos.get(i);
				break;
				case 3:
					diagnosticoRelacionado3=(Diagnostico)diagnosticos.get(i);
				break;
			}
		}
		
		try 
		{
			if(egresoDao.insertarEgresoAutomaticoValoracionHospitalizarEnPiso(
				con, 
				paciente.getCodigoCuenta(), 
				this.valoracionUrgencias.getProfesional().getLoginUsuario(), 
				ConstantesBD.acronimoDiagnosticoNoSeleccionado, //diagnostico muerte 
				ConstantesBD.codigoCieDiagnosticoNoSeleccionado, //cie muerte 
				diagnosticoPrincipal.getAcronimo(), 
				diagnosticoPrincipal.getTipoCIE(), 
				diagnosticoRelacionado1.getAcronimo(), 
				diagnosticoRelacionado1.getTipoCIE(), 
				diagnosticoRelacionado2.getAcronimo(), 
				diagnosticoRelacionado2.getTipoCIE(), 
				diagnosticoRelacionado3.getAcronimo(), 
				diagnosticoRelacionado3.getTipoCIE(),
				this.valoracionUrgencias.getFechaValoracion(),
				this.valoracionUrgencias.getHoraValoracion(),
				this.valoracionUrgencias.getCodigoTipoMonitoreo())<=0)
				errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático"));
		} 
		catch (SQLException e) 
		 {
			logger.error("Error en insertarEgresoAutomatico",e);
			errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático: "+e));
		}
	}
	
	/**
	 * Método implementado para insertar un egreso automático a la salida
	 * @param con
	 * @param paciente
	 */
	private void insertarEgresoAutomaticoSalida(Connection con,PersonaBasica paciente)
	{
		ArrayList diagnosticos=this.valoracionUrgencias.getDiagnosticos();
		Diagnostico diagnosticoPrincipal=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, true);
		Diagnostico diagnosticoRelacionado1=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		Diagnostico diagnosticoRelacionado2=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		Diagnostico diagnosticoRelacionado3=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
		
		for(int i=0; i<diagnosticos.size();i++)
		{
			switch(i)
			{
				case 0:
					diagnosticoPrincipal=(Diagnostico)diagnosticos.get(i);
				break;
				case 1:
					diagnosticoRelacionado1=(Diagnostico)diagnosticos.get(i);
				break;
				case 2:
					diagnosticoRelacionado2=(Diagnostico)diagnosticos.get(i);
				break;
				case 3:
					diagnosticoRelacionado3=(Diagnostico)diagnosticos.get(i);
				break;
			}
		}
		try 
		{
			if(egresoDao.insertarEgresoAutomatico(
				con, 
				paciente.getCodigoCuenta(), 
				this.valoracionUrgencias.getProfesional().getLoginUsuario(),
				this.valoracionUrgencias.getDiagnosticoMuerte().getAcronimo(),
				this.valoracionUrgencias.getDiagnosticoMuerte().getTipoCIE(),
				diagnosticoPrincipal.getAcronimo(), 
				diagnosticoPrincipal.getTipoCIE(), 
				diagnosticoRelacionado1.getAcronimo(), 
				diagnosticoRelacionado1.getTipoCIE(), 
				diagnosticoRelacionado2.getAcronimo(), 
				diagnosticoRelacionado2.getTipoCIE(), 
				diagnosticoRelacionado3.getAcronimo(), 
				diagnosticoRelacionado3.getTipoCIE(),
				this.valoracionUrgencias.getCodigoCausaExterna())<=0)
				errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático"));
		} 
		catch (SQLException e) 
		{
			logger.error("Error en insertarEgresoAutomaticoSalida: "+e);
			errores.add("",new ActionMessage("errors.problemasGenericos","insertando el egreso automático: "+e));
		}
	}
	
	/**
	 * Método implementado para actualizar los datos de la orden
	 * @param con
	 * @param numeroSolicitud
	 * @param profesional
	 * @param fechaGrabacion
	 */
	private void actualizarOrden(Connection con,String numeroSolicitud, UsuarioBasico profesional, String fechaGrabacion)
	{
		Solicitud sol=new Solicitud();
		
		//Se actualiza el estado de la solicitud
		if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(numeroSolicitud), 0 /**SIN ESTADO DE FACTURACION*/, ConstantesBD.codigoEstadoHCInterpretada).isTrue())
			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la valoración a Interpretada"));
		
		//Se actualiza el médico que responde
		try 
		{
			if(sol.actualizarMedicoRespondeTransaccional(con, Integer.parseInt(numeroSolicitud), profesional, ConstantesBD.continuarTransaccion)<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde la valoración "));
		} 
		catch (Exception e) 
		{
			logger.error("Error en actualizarOrden: "+e);
			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde la valoración "));
		} 
		
		
		//SE actualiza el pool del médico si solo tiene 1 vigente
		ArrayList array=Utilidades.obtenerPoolesMedico(con,fechaGrabacion,profesional.getCodigoPersona());
		if(array.size()==1)
			sol.actualizarPoolSolicitud(con,Integer.parseInt(numeroSolicitud),Integer.parseInt(array.get(0)+""));
		
		
	}
	
	/**
	 * Método implementado para actualizar los datos de una solicitud de consulta
	 * @param con
	 * @param numeroSolicitud
	 * @param profesional
	 */
	private void actualizarOrdenConsulta(Connection con,String numeroSolicitud, UsuarioBasico profesional,String fechaGrabacion,String horaGrabacion)
	{
		
		//1) Interpretar la solicitud
		if(Solicitud.interpretarSolicitud(con, "", profesional.getCodigoPersona(), fechaGrabacion, horaGrabacion, numeroSolicitud)<=0)
			errores.add("", new ActionMessage("errors.problemasGenericos","cambiando el estado de la solicitud a interpretada"));
			
		//2) Actualizar el medico responde
		if(Solicitud.actualizarMedicoResponde(con, Integer.parseInt(numeroSolicitud), profesional)<=0)
			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde en la solicitud"));
		
		//3) SE actualiza el pool del médico si solo tiene 1 vigente
		Solicitud sol=new Solicitud();
		ArrayList array=Utilidades.obtenerPoolesMedico(con,fechaGrabacion,profesional.getCodigoPersona());
		if(array.size()==1)
			sol.actualizarPoolSolicitud(con,Integer.parseInt(numeroSolicitud),Integer.parseInt(array.get(0)+""));
	}
	
	/**
	 * Método para insertar una valoración de urgencias
	 * @param con
	 * @param paciente
	 */
	public void insertarUrgencias(Connection con,PersonaBasica paciente) throws IPSException
	{
		
		this.valoracionUrgencias.setFechaGrabacion(UtilidadFecha.getFechaActual(con));
		this.valoracionUrgencias.setHoraGrabacion(UtilidadFecha.getHoraActual(con));
		//Se cargan los diagnósticos relacionados del mapa a la estructura
		this.cargarDiagnosticosRelacionados();
		
		if (paciente.getEstadoEmbarazada()  ==true) {
			this.valoracionUrgencias.setEstadoEmbarazada(ConstantesBD.estadoActivoChecbox);
		}else{
			this.valoracionUrgencias.setEstadoEmbarazada(ConstantesBD.estadoInActivoChecbox);
		}
		
		
		
		//***************VALIDACION DEL CONTROL POST-OPERATORIO**********************************
		this.valoracionUrgencias.setControlPostOperatorio(
			UtilidadesConsultaExterna.validarControlPostOperatorioUrgencias(
				con, 
				paciente.getCodigoPersona(), 
				this.valoracionUrgencias.getFechaValoracion(), 
				paciente.getCodigoIngreso(), 
				this.valoracionUrgencias.getDiagnosticos().get(0)
			)
		);
		//***************************************************************************************
		
		ResultadoBoolean respuesta = Valoraciones.valoracionDao.insertarUrgencias(con, this.valoracionUrgencias);
		
		if(respuesta.isTrue())
		{
		
			//***********ACTUALIZACIÓN DEL TIPO EVENTO*****************************************
			if(this.valoracionUrgencias.getCodigoTipoEvento().equals("")&&this.valoracionUrgencias.getEventoFueAccidenteTrabajo())
				if(Cuenta.actualizarTipoEventoCuenta(con, ConstantesIntegridadDominio.acronimoAccidenteTrabajo, paciente.getCodigoCuenta()+"")<=0)
					errores.add("", new ActionMessage("errors.noPudoActualizar","el tipo de evento de la cuenta. Proceso cancelado"));
			//*********************************************************************************
			
			//********ACTUALIZACION DE LAS FICHAS NOTIFICABLES********************************
			this.actualizarFichasNotificables(con, paciente);
			//**********************************************************************************
			
			//*********VALIDACIONES CONDUCTA A SEGUIR*****************************************
			this.procesoConductaValoracion(con, paciente, false);
			//********************************************************************************
			
			
			//************SE ACTUALIZAN DATOS DE LA SOLICITUD**************************************
			this.actualizarOrden(con, valoracionUrgencias.getNumeroSolicitud(), valoracionUrgencias.getProfesional(), valoracionUrgencias.getFechaGrabacion());
			//***************************************************************************************
			
			//************SE REGISTRA LA INFORMACION PARAMETRIZABLE********************************
			//Se verifica si se ingresó información fija de los componentes
			this.verificarIngresoInformacionComponentes(valoracionUrgencias);
			
			ResultadoBoolean resultado = Plantillas.guardarCamposParametrizablesIngreso(
				con, 
				this.plantilla, paciente.getCodigoIngreso(), 
				Integer.parseInt(this.valoracionUrgencias.getNumeroSolicitud()), 
				paciente.getCodigoPersona(), 
				this.valoracionUrgencias.getFechaValoracion(), 
				this.valoracionUrgencias.getHoraValoracion(), 
				this.valoracionUrgencias.getProfesional().getLoginUsuario(),
				ConstantesBD.codigoNuncaValidoDouble);
			if(!resultado.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			//**************************************************************************************
			
			//*************SE REGISTRA LA PRIMERA VERSIÓN DE EPICRISIS*******************************
			if(Valoraciones.valoracionDao.insertarPrimeraVersionEpicrisis(con, paciente.getCodigoIngreso()+"", valoracionUrgencias.getProfesional().getCodigoPersona(), true)<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","ingresando el primer registro de epicrisis"));
			//****************************************************************************************
			
			//***************************REGISTRO DE LAS INCAPACIDADES *******************************************
			DtoRegistroIncapacidades dtoIncapacidad = new DtoRegistroIncapacidades();
			dtoIncapacidad.setIngreso(paciente.getCodigoIngreso());
			dtoIncapacidad.setValoracion(Integer.parseInt(this.valoracionUrgencias.getNumeroSolicitud()));
			dtoIncapacidad.getGrabacion().setUsuarioModifica(this.valoracionUrgencias.getProfesional().getLoginUsuario());
			dtoIncapacidad.setEspecialidad(Integer.parseInt(this.valoracionUrgencias.getEspecialidadResponde()));
			dtoIncapacidad.setAcronimoDiagnostico(this.valoracionUrgencias.getDiagnosticos().get(0).getAcronimo());
			dtoIncapacidad.setTipoCie(this.valoracionUrgencias.getDiagnosticos().get(0).getTipoCIE());
			ResultadoBoolean respIncapacidad = RegistroIncapacidades.activarRegistrosIncapacidades(con, dtoIncapacidad);
			if(!respIncapacidad.isTrue())
			{
				errores.add("", new ActionMessage("errors.notEspecific",respIncapacidad.getDescripcion()));
			}
			//**************************************************************************
			
			//************SE GENERA EL CARGO PENDIENTE********************************************** 
		    Cargos cargos= new Cargos();
		    if(!cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	
		    	con, 
				this.valoracionUrgencias.getProfesional(), 
				paciente, 
				false/*dejarPendiente*/, 
				Integer.parseInt(this.valoracionUrgencias.getNumeroSolicitud()), 
				ConstantesBD.codigoTipoSolicitudInicialUrgencias /*codigoTipoSolicitudOPCIONAL*/, 
				paciente.getCodigoCuenta(), 
				ConstantesBD.codigoCentroCostoUrgencias/*codigoCentroCostoEjecutaOPCIONAL*/, 
				ConstantesBD.codigoServicioNoDefinido/*codigoServicioOPCIONAL*/, 
				1/*cantidadServicioOPCIONAL*/, 
				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
				/* "" -- numeroAutorizacionOPCIONAL*/
				"" /*esPortatil*/,
				this.valoracionUrgencias.getControlPostOperatorio().equals("")?false:true /*es Excento*/,
				this.valoracionUrgencias.getFechaValoracion(),
				"" /*subCuentaCoberturaOPCIONAL*/))
		    	errores.add("", new ActionMessage("errors.problemasGenericos","generando el cargo pendiente de la valoración"));
			//***************************************************************************************
		    
		    //***********REGISTRO DE COMPONENTES**********************************************
		    //Si existía componente de ginecología se registra una historia menstrual
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia)&&this.valoracionUrgencias.getHistoriaMenstrual().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarHistoriaMenstrual(con, this.valoracionUrgencias.getHistoriaMenstrual(), this.valoracionUrgencias.getNumeroSolicitud(), this.valoracionUrgencias.getProfesional());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de oftalmología se registra la oftalmología
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia)&&this.valoracionUrgencias.getOftalmologia().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarOftalmologia(con, this.valoracionUrgencias.getOftalmologia());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de pediatrï¿½a se regidtra su informacion
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria)&&this.valoracionUrgencias.getPediatria().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarPediatria(con, this.valoracionUrgencias.getPediatria());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //*********************************************************************************
		    		    
		    if(errores.isEmpty())
		    {		    	
			    //Se genera el reporte Inicial de Urgencias
			    if(cargos.getDtoDetalleCargo().getCodigoConvenio() > 0)
			    {			  
				    errores = generarReporteInicialUrgenciasValoraciones(
					    		con,
					    		paciente.getCodigoIngreso(),
					    		cargos.getDtoDetalleCargo().getCodigoConvenio(),
					    		this.valoracionUrgencias.getProfesional().getCodigoInstitucionInt(),
					    		this.valoracionUrgencias.getProfesional().getLoginUsuario());
			    }
			    else
			    {			    	
			    	errores.add("descripcion",new ActionMessage("errors.notEspecific","error al generar el reporte inicial de urgencias. No existe informaciï¿½n del convenio"));
			    }
		    }			
		}
		else
			errores.add("",new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
	}
	
	
	/**
	 * Método implementado para insertar una valoración de hospitalizacion
	 * @param con
	 * @param paciente
	 * @param generarCargo 
	 */
	public void insertarHospitalizacion(Connection con,PersonaBasica paciente, boolean generarCargo)
	{
		this.valoracionHospitalizacion.setFechaGrabacion(UtilidadFecha.getFechaActual(con));
		this.valoracionHospitalizacion.setHoraGrabacion(UtilidadFecha.getHoraActual(con));
		//Se cargan los diagnósticos relacionados del mapa a la estructura
		this.cargarDiagnosticosRelacionados();
		
		ResultadoBoolean respuesta = Valoraciones.valoracionDao.insertarHospitalizacion(con, this.valoracionHospitalizacion);
		
		if(respuesta.isTrue())
		{
		
			//***********ACTUALIZACIÓN DEL TIPO EVENTO*****************************************
			if(this.valoracionHospitalizacion.getCodigoTipoEvento().equals("")&&this.valoracionHospitalizacion.getEventoFueAccidenteTrabajo())
				if(Cuenta.actualizarTipoEventoCuenta(con, ConstantesIntegridadDominio.acronimoAccidenteTrabajo, paciente.getCodigoCuenta()+"")<=0)
					errores.add("", new ActionMessage("errors.noPudoActualizar","el tipo de evento de la cuenta. Proceso cancelado"));
			//*********************************************************************************
			
			//********ACTUALIZACION DE LAS FICHAS NOTIFICABLES********************************
			this.actualizarFichasNotificables(con, paciente);
			//**********************************************************************************
			
			//************SE ACTUALIZAN DATOS DE LA SOLICITUD**************************************
			this.actualizarOrden(con, valoracionHospitalizacion.getNumeroSolicitud(), valoracionHospitalizacion.getProfesional(), valoracionHospitalizacion.getFechaGrabacion());
			//***************************************************************************************
			
			//************SE REGISTRA LA INFORMACION PARAMETRIZABLE********************************
			//Se verifica si se ingresó información fija de los componentes
			this.verificarIngresoInformacionComponentes(valoracionHospitalizacion);
			
			ResultadoBoolean resultado = Plantillas.guardarCamposParametrizablesIngreso(
				con, 
				this.plantilla, paciente.getCodigoIngreso(), 
				Integer.parseInt(this.valoracionHospitalizacion.getNumeroSolicitud()), 
				paciente.getCodigoPersona(), 
				this.valoracionHospitalizacion.getFechaValoracion(), 
				this.valoracionHospitalizacion.getHoraValoracion(), 
				this.valoracionHospitalizacion.getProfesional().getLoginUsuario(),
				ConstantesBD.codigoNuncaValidoDouble);
			if(!resultado.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			//**************************************************************************************
			
			//*************SE REGISTRA LA PRIMERA VERSIÓN DE EPICRISIS*******************************
			if(Valoraciones.valoracionDao.insertarPrimeraVersionEpicrisis(con, paciente.getCodigoIngreso()+"", valoracionHospitalizacion.getProfesional().getCodigoPersona(), false)<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","ingresando el primer registro de epicrisis"));
			//****************************************************************************************
			
			//************SE GENERA EL CARGO PENDIENTE**********************************************
			if(generarCargo)
			{
			    Cargos cargos= new Cargos();
			    if(!cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	
			    	con, 
					this.valoracionHospitalizacion.getProfesional(), 
					paciente, 
					false/*dejarPendiente*/, 
					Integer.parseInt(this.valoracionHospitalizacion.getNumeroSolicitud()), 
					ConstantesBD.codigoTipoSolicitudInicialHospitalizacion /*codigoTipoSolicitudOPCIONAL*/, 
					paciente.getCodigoCuenta(), 
					this.valoracionHospitalizacion.getProfesional().getCodigoCentroCosto()/*codigoCentroCostoEjecutaOPCIONAL*/, 
					ConstantesBD.codigoServicioNoDefinido/*codigoServicioOPCIONAL*/, 
					1/*cantidadServicioOPCIONAL*/, 
					ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
					/* "" -- numeroAutorizacionOPCIONAL*/
					"" /*esPortatil*/,
					false /*esExcento*/,this.valoracionHospitalizacion.getFechaValoracion(),
					"" /*subCuentaCoberturaOPCIONAL*/))
			    	errores.add("", new ActionMessage("errors.problemasGenericos","generando el cargo pendiente de la valoración"));
			}
			//***************************************************************************************
			
			//*******************ACTUALIZACION DE LA VALORACION EN REGISTRO CUIDADO ESPECIAL*******************
			//solo aplica para pacientes con tipo paciente Hospitalizado o valoraciones de cuidado especial
			if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado)||this.valoracionHospitalizacion.isCuidadoEspecial())
			{
				resultado = Valoraciones.valoracionDao.actualizarValoracionRegistroCuidadoEspecial(con, paciente.getCodigoIngreso()+"", this.valoracionHospitalizacion.getNumeroSolicitud(), this.valoracionHospitalizacion.getProfesional().getLoginUsuario(),this.valoracionHospitalizacion.isCuidadoEspecial());
				if(!resultado.isTrue())
					errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			}
			//**************************************************************************************************
			
			//***********REGISTRO DE COMPONENTES**********************************************
		    //Si existía componente de ginecología se registra una historia menstrual
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia)&&this.valoracionHospitalizacion.getHistoriaMenstrual().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarHistoriaMenstrual(con, this.valoracionHospitalizacion.getHistoriaMenstrual(), this.valoracionHospitalizacion.getNumeroSolicitud(), this.valoracionHospitalizacion.getProfesional());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de oftalmología se registra la oftalmología
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia)&&this.valoracionHospitalizacion.getOftalmologia().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarOftalmologia(con, this.valoracionHospitalizacion.getOftalmologia());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de pediatría se regidtra su informacion
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria)&&this.valoracionHospitalizacion.getPediatria().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarPediatria(con, this.valoracionHospitalizacion.getPediatria());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //*********************************************************************************
		}
		else
			errores.add("",new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
	}
	
	/**
	 * Método implementado para insertar una valoración de consulta
	 * @param con
	 * @param paciente
	 * @param request
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	public ArrayList insertarConsulta(Connection con, PersonaBasica paciente, HttpServletRequest request, UsuarioBasico usuarioLogueado) throws NumberFormatException, IPSException
	{
		
		this.valoracion.setFechaGrabacion(UtilidadFecha.getFechaActual(con));
		this.valoracion.setHoraGrabacion(UtilidadFecha.getHoraActual(con));
		this.valoracion.setFechaValoracion(this.valoracion.getFechaGrabacion());
		this.valoracion.setHoraValoracion(this.valoracion.getHoraGrabacion());
		
		
		if (this.estadoEmbarazada!=null) {
			if (this.estadoEmbarazada==true) {
				this.valoracion.setEstadoEmbarazada(ConstantesBD.acronimoSi);
			}else{
				this.valoracion.setEstadoEmbarazada(ConstantesBD.acronimoNo);
			}
		}else{
			this.valoracion.setEstadoEmbarazada(ConstantesBD.acronimoNo);
		}

		
		if (usuarioLogueado.getNumeroRegistroMedico()!=null ) {
			this.valoracion.setRegistroMedico(usuarioLogueado.getNumeroRegistroMedico());
		}else{
			this.valoracion.setRegistroMedico("");
		}
		
		
		if (usuarioLogueado.getLoginUsuario()!=null) {
			this.valoracion.setUsuarioProfesionalSalud(usuarioLogueado.getLoginUsuario());
		}else{
			this.valoracion.setUsuarioProfesionalSalud("");
		}
	
		
		
		//Se cargan los diagnósticos relacionados del mapa a la estructura
		this.cargarDiagnosticosRelacionados();
		ArrayList alertas = new ArrayList ();
		String observacionCapitacion = null;
		ResultadoBoolean respuesta = Valoraciones.valoracionDao.insertarBase(con, this.valoracion, observacionCapitacion);
		this.numeroSolicitud = this.valoracion.getNumeroSolicitud();
		
		if(respuesta.isTrue())
		{
		
			int idCuenta = Utilidades.getCuentaSolicitud(con, Integer.parseInt(this.valoracion.getNumeroSolicitud()));
			
			//***********ACTUALIZACIÓN DEL TIPO EVENTO*****************************************
			if(this.valoracion.getCodigoTipoEvento().equals("")&&this.valoracion.getEventoFueAccidenteTrabajo())
				if(Cuenta.actualizarTipoEventoCuenta(con, ConstantesIntegridadDominio.acronimoAccidenteTrabajo, idCuenta+"")<=0)
					errores.add("", new ActionMessage("errors.noPudoActualizar","el tipo de evento de la cuenta. Proceso cancelado"));
			//*********************************************************************************
			
			//********ACTUALIZACION DE LAS FICHAS NOTIFICABLES********************************
			this.actualizarFichasNotificables(con, paciente);
			//**********************************************************************************
			
			//************SE ACTUALIZAN DATOS DE LA SOLICITUD**************************************
			//1) Actualizar numero de autorizacion			
			/*
			respuesta = Solicitud.actualizarNumeroAutorizacion(con, this.numeroAutorizacion, Integer.parseInt(this.numeroSolicitud));
			if(!respuesta.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			*/
			this.actualizarOrdenConsulta(con, this.numeroSolicitud, this.valoracion.getProfesional(), this.valoracion.getFechaGrabacion(), this.valoracion.getHoraGrabacion());
			
			
			//ANEXO 777--ACTUALIZAMOS EL DETALLE DE LA FACTURA EN EL CASO DE QUE ESTA SOLICITUD YA HALLA SIDO FACTURADA
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(usuarioLogueado.getCodigoInstitucionInt())))
			{
				logger.info("ENTRA A ACTUALIZAR LA INTERFAZ*****************************************************************");
				UtilidadBDInterfaz interfaz= new UtilidadBDInterfaz();
				ResultadoBoolean resultadoBoolean1=interfaz.actualizarDetalleFactProcedimientosFacturados( this.valoracion.getProfesional().getNumeroIdentificacion() , usuarioLogueado.getCodigoInstitucionInt(), false /*esDetallePaquete*/, Utilidades.getConsecutivoOrdenesMedicas( Utilidades.convertirAEntero(this.numeroSolicitud))+"",false,"","","");
				ResultadoBoolean resultadoBoolean2=interfaz.actualizarDetalleFactProcedimientosFacturados( this.valoracion.getProfesional().getNumeroIdentificacion() , usuarioLogueado.getCodigoInstitucionInt(), true /*esDetallePaquete*/, Utilidades.getConsecutivoOrdenesMedicas( Utilidades.convertirAEntero(this.numeroSolicitud))+"",false,"","","");
				
				
				if (!resultadoBoolean1.isTrue() && UtilidadCadena.noEsVacio(resultadoBoolean1.getDescripcion()))
					alertas.add(resultadoBoolean1.getDescripcion());
				else
					if (!resultadoBoolean2.isTrue() && UtilidadCadena.noEsVacio(resultadoBoolean2.getDescripcion()))
						alertas.add(resultadoBoolean2.getDescripcion());
				
				logger.info("-------SALE DE ACTUALIZAR LA INTERFAZ*****************************************************************");
			}	
			//----------------------------------------
			
			
			//***************************************************************************************
			
			//***********SE ACTUALIZA DATOS DE LA CITA***************************************
			//Aplica solo si existe cita asociada a la solicitud
			if(Utilidades.existeCitaParaSolicitud(con,this.getNumeroSolicitud()))
			{
				Cita cita = new Cita();
				//Se intenta actualizar el estado de la cita a CUMPLIDA si aplica
				respuesta = cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAtendida, Integer.parseInt(this.codigoCita), ConstantesBD.continuarTransaccion, usuarioLogueado.getLoginUsuario());
				
				if( !respuesta.isTrue() )
					errores.add("", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
				
				//Se actualiza el profesional de la agenda de la cita si aplica
				if(!this.pyp&&!Utilidades.actualizarMedicoAgenda(con, this.valoracion.getProfesional().getCodigoPersona(), Integer.parseInt(UtilidadesConsultaExterna.obtenerCodigoAgendaCita(con, this.codigoCita))))
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional en la agenda de la cita"));
			}
			//************************************************************************************
			
			//*********SE VERIFICA SI SE DEBE ACTUALIZAR LOS TOPES DEL PACIENTE*********************
			int codigoViaIngreso = Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con,idCuenta+""));
			int codigoEstadoCuenta = UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, idCuenta).getCodigo();
			
			//Si la cuenta es de CONSULTA EXTERNA y el estado es FACTURADA o FACTURADA PARCIAL
			if(codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna&&(codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaFacturada||codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaFacturada))
			{
				//Se actualiza el diagnpositco topes paciente pero no se valida el resultado del proceso
				if(!Utilidades.actualizarDiagnosticoTopesCita(
					con,
					Integer.parseInt(this.numeroSolicitud),
					paciente.getCodigoPersona(),
					this.valoracion.getDiagnosticos().get(0).getAcronimo(),
					this.valoracion.getDiagnosticos().get(0).getTipoCIE()+""))
					logger.warn("No se pudo actualizar el diagnostico topes por paciente");
			}
			//***************************************************************************************
			
			//************ACTUALIZACIÓN DEL FLUJO DE PYP********************************************
			if(this.pyp&&!this.vieneDePyp) //Si la solicitud es PYP y no viene de PYP se actualiza actividad
			{
				/*Nota * Es necesario verificar que no viene de pyp porque cuando viene de pyp el mismo flujo de la
				 * funcionalidad de programas PYP se encarga de hacer este proceso
				 */
				String consecutivoActividad = Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,Integer.parseInt(this.numeroSolicitud));
				
				if(Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,consecutivoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,this.valoracion.getProfesional().getLoginUsuario(),""))
				{
					if(Utilidades.actualizarAcumuladoPYP(con,numeroSolicitud+"",this.valoracion.getProfesional().getCodigoCentroAtencion()+"")<=0)
						errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el acumulado de actividades ejecutadas pyp"));
				}
				else
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado de la actividad pyp asociada a la solicitud"));
			}
			//Adicionalmente se hace el flujo de PYP CONSULTA EXTERNA donde se ejecutan actividades de un mapa
			this.ingresarInformacionPYP(con,paciente,request);
			
			//Se Acutualiza la especialidad del profesiona que responde
			if(Solicitud.actualizarEspecialidadProfResponde(con, Integer.parseInt(this.numeroSolicitud), Utilidades.convertirAEntero(this.codEspecialidadProfesionalResponde))<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la especialidad del profesional que responde en la solicitud"));
			
			//***************************************************************************************
			
			//***************************REGISTRO DE LAS INCAPACIDADES *******************************************
			DtoRegistroIncapacidades dtoIncapacidad = new DtoRegistroIncapacidades();
			dtoIncapacidad.setIngreso(Integer.parseInt(Utilidades.obtenerCodigoIngresoDadaCuenta(con, idCuenta+"")));
			dtoIncapacidad.setValoracion(Integer.parseInt(this.numeroSolicitud));
			dtoIncapacidad.getGrabacion().setUsuarioModifica(this.valoracion.getProfesional().getLoginUsuario());
			dtoIncapacidad.setEspecialidad(Integer.parseInt(this.getCodEspecialidadProfesionalResponde()));
			dtoIncapacidad.setAcronimoDiagnostico(this.valoracion.getDiagnosticos().get(0).getAcronimo());
			dtoIncapacidad.setTipoCie(this.valoracion.getDiagnosticos().get(0).getTipoCIE());
			ResultadoBoolean respIncapacidad = RegistroIncapacidades.activarRegistrosIncapacidades(con, dtoIncapacidad);
			if(!respIncapacidad.isTrue())
			{
				errores.add("", new ActionMessage("errors.notEspecific",respIncapacidad.getDescripcion()));
			}
			//**************************************************************************
			
			//************SE REGISTRA LA INFORMACION PARAMETRIZABLE********************************
			//Se verifica si se ingresó información fija de los componentes
			this.verificarIngresoInformacionComponentes(valoracion);
			
			ResultadoBoolean resultado = Plantillas.guardarCamposParametrizablesIngreso(
				con, 
				this.plantilla, paciente.getCodigoIngreso(), 
				Integer.parseInt(this.valoracion.getNumeroSolicitud()), 
				paciente.getCodigoPersona(), 
				this.valoracion.getFechaValoracion(), 
				this.valoracion.getHoraValoracion(), 
				this.valoracion.getProfesional().getLoginUsuario(),
				ConstantesBD.codigoNuncaValidoDouble);
			if(!resultado.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			//**************************************************************************************
			
			///************SE GENERA EL CARGO PENDIENTE**********************************************
			//Si la cuenta es válida se genera el cargo pendiente
			logger.info("Estado de la cuenta "+codigoEstadoCuenta);
			if(codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaActiva||
				codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaFacturadaParcial||
				codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaAsociada)
			{
				logger.info("*****************VOY A RECALCULAR EL CARGO !!!!!!!**********************");
				//GENERACION DEL CARGO DE SERVICIO CUANDO SE RESPONDE
				Cargos cargos= new Cargos();
				//como solo es 1 servicio entonces no puede tener n responsables con cargo pendiente entonces le enviamos el convenio vacio
				if(!cargos.recalcularCargoServicio(
					con, 
					Integer.parseInt(this.numeroSolicitud), 
					this.valoracion.getProfesional(), 
					ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, 
					"" /*observaciones*/, 
					ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, 
					ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, 
					ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, 
					false /*filtrarSoloCantidadesMayoresCero*/, 
					ConstantesBD.acronimoNo /*esComponentePaquete*/, 
					""/*esPortatil*/,this.valoracion.getFechaValoracion()))
			    	errores.add("", new ActionMessage("errors.problemasGenericos","generando el cargo pendiente de la valoración"));
			}
			//***************************************************************************************
			
			//***********REGISTRO DE COMPONENTES**********************************************
		    //Si existía componente de ginecología se registra una historia menstrual
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia)&&this.valoracion.getHistoriaMenstrual().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarHistoriaMenstrual(con, this.valoracion.getHistoriaMenstrual(), this.valoracion.getNumeroSolicitud(), this.valoracion.getProfesional());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de oftalmología se registra la oftalmología
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia)&&this.valoracion.getOftalmologia().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarOftalmologia(con, this.valoracion.getOftalmologia());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de pediatría se regidtra su informacion
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria)&&this.valoracion.getPediatria().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarPediatria(con, this.valoracion.getPediatria());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //*********************************************************************************
		}
		else
			errores.add("",new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
		
		return alertas;
	}
	
	/**
	 * Método implementado para insertar la solicitud de interconsulta
	 * @param con
	 * @param paciente
	 * @param request
	 * @param observacionCapitacion
	 */
	public void insertarInterconsulta(Connection con, PersonaBasica paciente, HttpServletRequest request, String observacionCapitacion) throws IPSException 
	{
		this.valoracion.setFechaGrabacion(UtilidadFecha.getFechaActual(con));
		this.valoracion.setHoraGrabacion(UtilidadFecha.getHoraActual(con));
		//Se cargan los diagnósticos relacionados del mapa a la estructura
		this.cargarDiagnosticosRelacionados();
		
		ResultadoBoolean respuesta = Valoraciones.valoracionDao.insertarBase(con, this.valoracion, observacionCapitacion);
		this.numeroSolicitud = this.valoracion.getNumeroSolicitud();
		
		if(respuesta.isTrue())
		{
		
			//***********ACTUALIZACIÓN DEL TIPO EVENTO*****************************************
			if(this.valoracion.getCodigoTipoEvento().equals("")&&this.valoracion.getEventoFueAccidenteTrabajo())
				if(Cuenta.actualizarTipoEventoCuenta(con, ConstantesIntegridadDominio.acronimoAccidenteTrabajo, paciente.getCodigoCuenta()+"")<=0)
					errores.add("", new ActionMessage("errors.noPudoActualizar","el tipo de evento de la cuenta. Proceso cancelado"));
			//*********************************************************************************
			
			//********ACTUALIZACION DE LAS FICHAS NOTIFICABLES********************************
			this.actualizarFichasNotificables(con, paciente);
			//**********************************************************************************
			
			//************SE ACTUALIZAN DATOS DE LA SOLICITUD**************************************
			
			//1) Actualizar numero de autorizacion
			/*
			respuesta = Solicitud.actualizarNumeroAutorizacion(con, this.numeroAutorizacion, Integer.parseInt(this.numeroSolicitud));
			if(!respuesta.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
			*/
			
			//2) Actualizar el medico responde
			if(Solicitud.actualizarMedicoResponde(con, Integer.parseInt(this.numeroSolicitud), this.valoracion.getProfesional())<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el profesional que responde en la solicitud"));
			
			//3)Actualizar Especilidad Medico que responde
			if(Solicitud.actualizarEspecialidadProfResponde(con, Integer.parseInt(this.numeroSolicitud), Utilidades.convertirAEntero(this.codEspecialidadProfesionalResponde))<=0)
				errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la especialidad del profesional que responde en la solicitud"));
			
			//4) SE actualiza el pool del médico si solo tiene 1 vigente
			Solicitud sol=new Solicitud();
			ArrayList array=Utilidades.obtenerPoolesMedico(con,this.valoracion.getFechaGrabacion(),this.valoracion.getProfesional().getCodigoPersona());
			if(array.size()==1)
				sol.actualizarPoolSolicitud(con,Integer.parseInt(this.numeroSolicitud),Integer.parseInt(array.get(0)+""));
			
			//5) Actualizar el centro de costo solicitado si existe una excepecion en la funcionalidad "Excepciones Centro de Costo Interconsultas"
			SolicitudInterconsulta solInter = new SolicitudInterconsulta();
			solInter.setNumeroSolicitud(Utilidades.convertirAEntero(this.numeroSolicitud));
			try {
				solInter.cargar(con, Utilidades.convertirAEntero(this.numeroSolicitud));
			} catch(SQLException e) {
				logger.info("Error cargando la informacion de interconsulta / "+e);
			}	
			int centroCostoEjecutaXExcepcion = UtilidadesOrdenesMedicas.obtenerCCEjecutaXExcepCCInter(con, paciente.getCodigoArea()+"", solInter.getCodigoServicioSolicitado()+"", this.valoracion.getProfesional().getCodigoPersona()+"");
			if(centroCostoEjecutaXExcepcion!=ConstantesBD.codigoNuncaValido){
				solInter.cambiarCentroCostoSolicitado(con, centroCostoEjecutaXExcepcion);
			}
			
			//***************************************************************************************
			
			///************SE REGISTRA LA INFORMACION PARAMETRIZABLE********************************
			//Se verifica si se ingresó información fija de los componentes
			this.verificarIngresoInformacionComponentes(valoracion);
			
			
			ResultadoBoolean resultado = Plantillas.guardarCamposParametrizablesIngreso(
				con, 
				this.plantilla, paciente.getCodigoIngreso(), 
				Integer.parseInt(this.valoracion.getNumeroSolicitud()), 
				paciente.getCodigoPersona(), 
				this.valoracion.getFechaValoracion(), 
				this.valoracion.getHoraValoracion(), 
				this.valoracion.getProfesional().getLoginUsuario(),
				ConstantesBD.codigoNuncaValidoDouble);
			if(!resultado.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			//**************************************************************************************
			
			//GENERACION DEL CARGO DE SERVICIO CUANDO SE RESPONDE***********************************
			Cargos cargos= new Cargos();
			//como solo es 1 servicio entonces no puede tener n responsables con cargo pendiente entonces le enviamos el convenio vacio
			if(!cargos.recalcularCargoServicio(
				con, 
				Integer.parseInt(this.numeroSolicitud), 
				this.valoracion.getProfesional(), 
				ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, 
				"" /*observaciones*/, 
				ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, 
				ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, 
				ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, 
				false /*filtrarSoloCantidadesMayoresCero*/, 
				ConstantesBD.acronimoNo /*esComponentePaquete*/, 
				""/*esPortatil*/,this.valoracion.getFechaValoracion()))
		    	errores.add("", new ActionMessage("errors.problemasGenericos","generando el cargo pendiente de la valoración"));
			//Se añaden los errores del cálculo del cargo
			for(Object mensajeCargo:cargos.getInfoErroresCargo().getMensajesErrorDetalle())
			{
				String[] datosMensaje = mensajeCargo.toString().split(ConstantesBD.separadorSplit);
				ElementoApResource mensaje = new ElementoApResource(datosMensaje[0]);
				if(datosMensaje.length>1)
					for(int i=1;i<datosMensaje.length;i++)
						mensaje.agregarAtributo(datosMensaje[i]);
				this.advertencias.add(mensaje);
			}
			//***************************************************************************************
		    
			logger.info("CODIOG MANEJO INTERCONSULYA=> "+this.codigoManejo);
		    //****************PROCESO DEL MANEJO DE LA INTERCONSULTA*****************************
		    switch(this.codigoManejo)
		    {
		    	/********FLUJO SE DESEA CONCEPTO SOLAMENTE*****************************************************************/
		    	case ConstantesBD.codigoSeDeseaConceptoSolamente:
		    		//Se cambia el estado de la solicitud a respondida
		    		if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(this.numeroSolicitud), 0, ConstantesBD.codigoEstadoHCRespondida).isTrue())
		    			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la orden a Respondida"));
		    	break;
		    	
		    	/********FLUJO MANEJO CONJUNTO*****************************************************************/
		    	case ConstantesBD.codigoManejoConjunto:
		    		//Se cambia el estado de la solicitud a respondida
		    		if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(this.numeroSolicitud), 0, ConstantesBD.codigoEstadoHCRespondida).isTrue())
		    			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la orden a Respondida"));
		    		//Se inserta el profesional como adjunto
	    			if(!sol.insertarAdjunto(con, Integer.parseInt(this.numeroSolicitud), this.valoracion.getProfesional().getCodigoPersona(), this.valoracion.getProfesional().getCodigoCentroCosto()).isTrue())
	    				errores.add("", new ActionMessage("errors.problemasGenericos","ingresando el profesional como adjunto"));
		    	break;
		    	
		    	
		    	/********FLUJO SE TRANSFIERE MANEJO PACIENTE*****************************************************************/
		    	case ConstantesBD.codigoSeTransfiereManejoPaciente:
		    		if(this.aceptaCambioTratante.equals(ConstantesBD.acronimoSi))
		    		{
		    			//Se cambia el estado de la solicitud a interpretada
		    			if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(this.numeroSolicitud), 0, ConstantesBD.codigoEstadoHCInterpretada).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la orden a Interpretada"));
			    		//Se finaliza el manejo conjunto en solicitudes_inter
			    		if(SolicitudInterconsulta.actualizarFlagMostrarInterconsulta(con, Integer.parseInt(this.numeroSolicitud), true)<=0)
			    			errores.add("", new ActionMessage("errors.problemasGenericos","finalizando la bandera de manejo conjunto en la tabla solicitudes_inter"));
			    		//Se actualiza el área de la cuenta (con el centro de costo del usuario)
			    		if(!Utilidades.actualizarAreaCuenta(con,paciente.getCodigoCuenta(),this.valoracion.getProfesional().getCodigoCentroCosto()))
			    			errores.add("", new ActionMessage("errors.problemasGenericos","cambiando el área del paciente a "+this.valoracion.getProfesional().getCentroCosto()));
			    		//SE cambia la entrada actual de tratante y se inserta una nueva
			    		if(!sol.insertarTratante(con, Integer.parseInt(this.numeroSolicitud), this.valoracion.getProfesional().getCodigoPersona(), this.valoracion.getProfesional().getCodigoCentroCosto(), false).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","cambiando el tratante del paciente a "+this.valoracion.getProfesional().getCentroCosto()));
			    		//Se inactiva solicitud cambio medico tratante
			    		if(!sol.inactivarSolicitudCambioTratante(con, Integer.parseInt(this.numeroSolicitud)).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","inactivando la solicitud de cambio médico tratante"));
		    		}
		    		else if(this.aceptaCambioTratante.equals(ConstantesBD.acronimoNo))
		    		{
		    			///Se cambia el estado de la solicitud a respondida
		    			if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(this.numeroSolicitud), 0, ConstantesBD.codigoEstadoHCRespondida).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la orden a Respondida"));
			    		//Se finaliza el manejo conjunto en solicitudes_inter
		    			if(SolicitudInterconsulta.actualizarFlagMostrarInterconsulta(con, Integer.parseInt(this.numeroSolicitud), true)<=0)
			    			errores.add("", new ActionMessage("errors.problemasGenericos","finalizando la bandera de manejo conjunto en la tabla solicitudes_inter"));
		    			//Se inactiva solicitud cambio medico tratante
			    		if(!sol.inactivarSolicitudCambioTratante(con, Integer.parseInt(this.numeroSolicitud)).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","inactivando la solicitud de cambio médico tratante"));
		    		}
		    		//De lo contrario fue pendiente
		    		else if(this.aceptaCambioTratante.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
		    		{
		    			
		    			///Se cambia el estado de la solicitud a respondida
		    			if(!sol.cambiarEstadosSolicitud(con, Integer.parseInt(this.numeroSolicitud), 0, ConstantesBD.codigoEstadoHCRespondida).isTrue())
			    			errores.add("", new ActionMessage("errors.problemasGenericos","actualizando el estado médico de la orden a Respondida"));
		    			//Se inserta el profesional como adjunto
		    			if(!sol.insertarAdjunto(con, Integer.parseInt(this.numeroSolicitud), this.valoracion.getProfesional().getCodigoPersona(), this.valoracion.getProfesional().getCodigoCentroCosto()).isTrue())
		    				errores.add("", new ActionMessage("errors.problemasGenericos","ingresando el profesional como adjunto"));
		    		}
		    	break;
		    }
		    //**********************************************************************************
		    
		    //***********REGISTRO DE COMPONENTES**********************************************
		    //Si existía componente de ginecología se registra una historia menstrual
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia)&&this.valoracion.getHistoriaMenstrual().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarHistoriaMenstrual(con, this.valoracion.getHistoriaMenstrual(), this.valoracion.getNumeroSolicitud(), this.valoracion.getProfesional());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de oftalmología se registra la oftalmología
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia)&&this.valoracion.getOftalmologia().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarOftalmologia(con, this.valoracion.getOftalmologia());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //Si existe componente de pediatría se regidtra su informacion
		    if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria)&&this.valoracion.getPediatria().ingresoInformacion())
		    {
		    	resultado = UtilidadesHistoriaClinica.ingresarPediatria(con, this.valoracion.getPediatria());
		    	if(!resultado.isTrue())
		    		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		    }
		    //*********************************************************************************
		}
		else
			errores.add("",new ActionMessage("errors.notEspecific",respuesta.getDescripcion()));
		
	}
	
	/**
	 * Método implementado para ingresar información de PYP
	 * @param con
	 * @param paciente
	 * @param request
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	private void ingresarInformacionPYP(Connection con, PersonaBasica paciente, HttpServletRequest request) throws NumberFormatException, IPSException 
	{
		//Se revisa si venía mapa de PYP Consulta Externa
		if(request.getSession().getAttribute("mapaPYPConsultaExterna")!=null)
		{
			logger.info("*******************************voy a registrar la informacion pyp*******************************");
			//Se toma el mapa editado en la funcionalidad de Programas PYP
			HashMap mapa = (HashMap)request.getSession().getAttribute("mapaPYPConsultaExterna");
			int numRegistros = Integer.parseInt(mapa.get("numRegistros").toString());
			
			if(numRegistros>0)
			{
				ProgramasPYP programas = new ProgramasPYP();
				//Inserción de la solicitud de ordenes ambulatorias
				OrdenesAmbulatorias orden = new OrdenesAmbulatorias();
				//registro de programas ya ingresados
				Vector registro = new Vector();
				
				boolean cuentaAbierta = UtilidadTexto.getBoolean(mapa.get("cuentaAbierta").toString());
				String numSolConsulta = mapa.get("numSolConsulta").toString();
				String numeroOrden = "";
				//Se consulta la cuenta de la consulta / para poder saber el ingreso
				int idCuenta = Utilidades.getCuentaSolicitud(con, Integer.parseInt(numSolConsulta));
				Cuenta mundoCuenta = new Cuenta();
				mundoCuenta.cargarCuenta(con, idCuenta+"");
				
				for(int i=0;i<numRegistros;i++)
				{
					int resp0 = 0,resp1= 0,resp2=0,resp3=0,resp4=0;
					programas.clean();
					String consecutivoPrograma = mapa.get("consecutivoTablaProg_"+i).toString();
					String consecutivoActividad = mapa.get("consecutivoTablaAct_"+i).toString();
					String estadoActividad = mapa.get("estadoActividad_"+i).toString();
					
					//******SE VERIFICA SI YA SE EJECUTÓ LA ACTIVIDAD********************************+
					programas.setCampos("codigoPersona",paciente.getCodigoPersona());
					programas.setCampos("codigoPrograma",mapa.get("codigoPrograma_"+i));
					programas.setCampos("institucion",mapa.get("institucionPrograma_"+i));
					programas.setCampos("codigoActividad",mapa.get("consecutivoActividad_"+i));
					
					
					
					boolean estaEjecutada = programas.estaActividadEjecutada(con);
					if(!estaEjecutada)
					{
					
						//Se verifica si es de unificar PYP
						if(UtilidadTexto.getBoolean(mapa.get("unificarPYP_"+i).toString()))
						{
							/*******UNIFICAR PYP SI*************************************************************************/
							
							//1) Se actualiza el indicativo PYP************************************
							if(UtilidadTexto.getBoolean(mapa.get("marcarPYP").toString()))
								resp0 = Solicitud.actualizarIndicativoPYP(con,numSolConsulta,true);
							else
								resp0 = 1;
							
							//2) Se inserta el programa********************************** 
							if(consecutivoPrograma.equals("0"))
							{
								String codigoPrograma = mapa.get("codigoPrograma_"+i).toString();
								String institucionPrograma = mapa.get("institucionPrograma_"+i).toString();
								
								//Se verifica si el programa ya fue insertado, si ya fue insertado
								//retorna el consecutivo del programa si no retorna una cdena vacía
								consecutivoPrograma = fueInsertadoPrograma(con,registro,codigoPrograma,institucionPrograma,paciente.getCodigoPersona()+""); 
								if(consecutivoPrograma.equals(""))
								{
									//No existe programa se debe insertar
									programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
									programas.setCampos("codigoPrograma",codigoPrograma);
									programas.setCampos("usuario",mapa.get("usuarioPrograma_"+i));
									programas.setCampos("institucion",institucionPrograma);
									resp1 = programas.insertarPrograma(con);
									
									consecutivoPrograma = resp1 + "";
									registro.add(programas.getCampos("codigoPrograma")+ConstantesBD.separadorSplit+programas.getCampos("institucion")+ConstantesBD.separadorSplit+consecutivoPrograma);
								}
								else
									resp1 = 1;
							}
							else
								resp1 = 1;
							
							//3) Se inserta la actividad************************************
							programas.clean();
							if(resp1>0)
							{
							
								//se parametrizan campos comunes
								programas.setCampos("estadoPrograma",ConstantesBD.codigoEstadoProgramaPYPEjecutado);
								programas.setCampos("numeroSolicitud",numSolConsulta);
								programas.setCampos("fechaEjecutar",UtilidadFecha.getFechaActual());
								programas.setCampos("horaEjecutar",UtilidadFecha.getHoraActual());
								programas.setCampos("usuarioEjecutar",mapa.get("usuarioActividad_"+i));
								
								logger.info("consecutivoActividad=> "+consecutivoActividad+", estadoActividad=> "+estadoActividad);
								//Se verifica si la actividad ya existe o fue ejecutada para el paciente en PYP
								if(consecutivoActividad.equals("0")||estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)||
									estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado))
								{
									//********SE DEBE INSERTAR ACTIVIDAD********************************************
									programas.setCampos("consecutivoPrograma",consecutivoPrograma);
									programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
									if(mapa.get("frecuenciaActividad_"+i)!=null)
										programas.setCampos("frecuencia",mapa.get("frecuenciaActividad_"+i));
									programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
									
									resp2 = programas.insertarActividad(con);
									consecutivoActividad = resp2 + "";
									resp3 = 1;
									
								}
								else
								{
									//******SE DEBE MODIFICAR LA ACTIVIDAD***************************************
									programas.setCampos("consecutivoActividadPYP",consecutivoActividad);
									
//									resp2 = programas.modificarActividad(con);
									logger.info("Se modificó la actividad!!!! "+resp2);
									
									//si la actividad estaba en estado programado se debe actualizar la orden ambulatoria con el numero de la solicitud
									if(estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
									{
										//******************SE ACTUALIZA LA ORDEN AMBULATORIA*******************
										HashMap campos = new HashMap();
										campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
										campos.put("numeroSolicitud",numSolConsulta);
										campos.put("numeroOrden",mapa.get("numeroOrdenActividad_"+i));
										campos.put("usuario",mapa.get("usuarioActividad_"+i));
										
										resp3 = OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,campos);
	
										//********************************************************************************
									}
									else
										resp3 = 1;
									
								}
							}
							//fin insertar actividad---------
							if(resp2>0)
							{
								//**********VERIFICACION DEL ACUMULADO********************************
								//Se debe validar la cobertura del servicio para saber a cual convenio se le debe hacer el acumulado
								DtoConvenio convenio = UtilidadesHistoriaClinica.obtenerConvenioPypSolicitud(con, numSolConsulta);
								
								
								programas.clean();
								programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
								programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
								programas.setCampos("tipoRegimen",convenio.getAcronimoTipoRegimen());
								programas.setCampos("codigoConvenio",convenio.getCodigo());
								programas.setCampos("codigoPrograma",mapa.get("codigoPrograma_"+i));
								programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
								programas.setCampos("fecha",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
								programas.setCampos("institucion",mapa.get("institucionPrograma_"+i));
								
								//como no existe acumulado para esta actividad se inserta una nueva
								resp4 = programas.actualizarAcumuladoActividades(con);
								
								
								//*******************************************************************
							}
							
							if(resp0<=0||resp1<=0||resp2<=0||resp3<=0||resp4<=0)
							{
								this.errores.add("",new ActionMessage("errors.problemasGenericos","ejecutando la actividad PYP con consecutivo "+consecutivoActividad+", del programa "+consecutivoPrograma));
								logger.error("Error actualizando informacion de PYP en ingresarInformacionPYP ");
							}
							
							/*******FIN UNIFICAR PYP SI*************************************************************************/
						}
						else
						{
							/*******UNIFICAR PYP NO*************************************************************************/
							//si no existe la actividad o está en estado diferente a solicitada
							logger.info("SECCION UNIFICAR NO===============consecutivoActividad=> "+consecutivoActividad+", estadoActividad=>"+estadoActividad);
							if(consecutivoActividad.equals("0")||!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
							{
								logger.info("El estado de la actividad es=> "+estadoActividad);
								if(cuentaAbierta)
								{
									logger.info("LA CUENTA ESTÁ ABIERTA!!!");
									//1) Se crea nueva solicitud************************************************
									try
									{
										///****************SE GENERA SOLICITUD DE CONSULTA*******************************************************
										Cargos cargos = new Cargos();
										
										Cita cita = new Cita();
										boolean resultado = false;
										
										
										cita.setCodigoAreaPaciente(paciente.getCodigoArea());
										cita.setCodigoCentroCostoSolicitado(this.valoracion.getProfesional().getCodigoCentroCosto());
										cita.setSolPYP(true);
										resultado = cita.generarSolicitud(con,ConstantesBD.continuarTransaccion,this.valoracion.getProfesional().getCodigoOcupacionMedica(),paciente.getCodigoCuenta(),Integer.parseInt(mapa.get("codigoActividad_"+i).toString()),/*"",*/paciente.getCodigoArea(),this.valoracion.getProfesional().getCodigoCentroCosto(),this.valoracion.getProfesional().getLoginUsuario(),false);
										
										
										if(resultado)
										{
											///Nota * El cargo no es esencial para la transacción, por tal motivo ya se toma como exitoso
											resp0 = 1;

											cargos.setPyp(Utilidades.esSolicitudPYP(con, cita.getNumeroSolicitud()));
											//resp0 = cargos.generarCargoCita(con,cita.getNumeroSolicitud(),usuario.getLoginUsuario());
											cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	
													con, 
													this.valoracion.getProfesional(), 
													paciente, 
													false/*dejarPendiente*/, 
													cita.getNumeroSolicitud(), 
													ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
													paciente.getCodigoCuenta(), 
													ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
													Integer.parseInt(mapa.get("codigoActividad_"+i).toString())/*codigoServicioOPCIONAL*/, 
													1/*cantidadServicioOPCIONAL*/, 
													ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
													ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
													/* "" -- numeroAutorizacionOPCIONAL*/
													""/*esPortatil*/,false,"",
													"" /*subCuentaCoberturaOPCIONAL*/
												);
											
											
										}
										else
											resp0 = 0;
										
										logger.info("estado A de resp0=> "+resp0);
										//*************************************************************************************************
										//***********SE GENERA LA RESPUESTA DE LA CONSULTA*************************************************
										if(resp0>0)
										{
											//Se retoma la valoracion de la consulta principal para crear una replica
											numSolConsulta = cita.getNumeroSolicitud()+"";
											//Se instancia un DTO de valoracion PYP
											DtoValoracion valoracionPyp = new DtoValoracion();
											//Se copian las propiedades de la valoracion a la valoracionPYP
											PropertyUtils.copyProperties(valoracionPyp,this.valoracion);
											valoracionPyp.setNumeroSolicitud(numSolConsulta);
											
											String[] vector;
											//Se verifican los datos que se tomaron desde PYP
											if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
											{
												vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
												Diagnostico diagnostico= new Diagnostico(vector[0],Integer.parseInt(vector[1]));
												diagnostico.setPrincipal(true);
												diagnostico.setNumero(0);
												diagnostico.setValorFicha(mapa.get("valorFicha_"+i)==null?"":mapa.get("valorFicha_"+i).toString());
												
												//Se reemplaza el diagnostico principal
												valoracionPyp.getDiagnosticos().set(0, diagnostico);
												
												//*****SE GENERA LA RESPUESTA DE EPIDEMIOLOGÍA********************+
												resp0 = Utilidades.actualizarDatosEpidemiologia(
													con, 
													mapa.get("diagnostico_"+i).toString(),
													diagnostico.getValor(),
													cita.getNumeroSolicitud()+"", 
													paciente, 
													this.valoracion.getProfesional());
												//******************************************************************
												
											}
											if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
											{
												vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
												valoracionPyp.getValoracionConsulta().setCodigoFinalidadConsulta(vector[0]);
											}	
											if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
											{
												vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
												valoracionPyp.setCodigoCausaExterna(Integer.parseInt(vector[0]));
												valoracionPyp.setNombreCausaExterna(vector[1]);
											}	
											
											
											//***********************SE INSERTAN LOS DATOS DE LA VALORACION*******************************************
											String observacionCapitacion = null;
											ResultadoBoolean respVal=Valoraciones.valoracionDao.insertarBase(con, valoracionPyp, observacionCapitacion);
											
											if(respVal.isTrue())
											{
												//Se interpreta la solicitud
												if(Solicitud.interpretarSolicitud(con, "", this.valoracion.getProfesional().getCodigoPersona(), this.valoracion.getFechaGrabacion(), this.valoracion.getHoraGrabacion(), valoracionPyp.getNumeroSolicitud())<=0)
													respVal.setResultado(false);
												//Actualizar el médico responde
												if(Solicitud.actualizarMedicoResponde(con, Integer.parseInt(valoracionPyp.getNumeroSolicitud()), this.valoracion.getProfesional())<=0)
													respVal.setResultado(false);
												
												//Se intenta actualizar el pool del médico
												Solicitud sol=new Solicitud();
												ArrayList array=Utilidades.obtenerPoolesMedico(con,this.valoracion.getFechaGrabacion(),this.valoracion.getProfesional().getCodigoPersona());
												if(array.size()==1)
													sol.actualizarPoolSolicitud(con,Integer.parseInt(valoracionPyp.getNumeroSolicitud()),Integer.parseInt(array.get(0)+""));
											}
											//**************************************************************************************************************
											
											logger.info("estado B de resp0=> "+resp0);
											logger.info("respVal=> "+respVal.isTrue());
											if(!respVal.isTrue())
												resp0 = 0;
											
												
										}
										//*************************************************************************************************
										
									}
									catch(Exception e)
									{
										logger.error("Error generando la solicitud de consulta externa en ingresarInformacionPYP de ValoracionInterconsultaAction: "+e);
										resp0 = 0;
									}
									//************************************************************************************************
									//***************************************************************************
									 
								}
								else
								{
									
									logger.info("SE INSERTA LA ORDEN AMBULATORIA CUENTA CERRADA");
									//1) insertar Orden Ambulatoria************************************************
									//Si ya está programada no se debe ingresar nueva orden ambulatoria
									if(!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
									{
										orden.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios+"");
										orden.setInstitucion(this.valoracion.getProfesional().getCodigoInstitucionInt());
										orden.setCodigoPaciente(paciente.getCodigoPersona()+"");
										orden.setPyp(true);
										orden.setUrgente(false);
										orden.setCentroAtencion(this.valoracion.getProfesional().getCodigoCentroAtencion()+"");
										orden.setProfesional(this.valoracion.getProfesional().getCodigoPersona()+"");
										orden.setLoginUsuario(this.valoracion.getProfesional().getLoginUsuario());
										orden.setEspecialidad(mapa.get("especialidad_"+i).toString()); 
										orden.setFechaOrden(UtilidadFecha.getFechaActual());
										orden.setHora(UtilidadFecha.getHoraActual());
										orden.setObservaciones("");
										orden.setEstadoOrden(ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+"");
										orden.setConsultaExterna(true);
										orden.setUsuarioConfirma(this.valoracion.getProfesional().getLoginUsuario());
										orden.setFechaConfirma(UtilidadFecha.getFechaActual());
										orden.setHoraConfirma(UtilidadFecha.getHoraActual());
										orden.setCentroCostoSolicita(this.valoracion.getProfesional().getCodigoCentroCosto()+"");
										
										HashMap mapaOrden=new HashMap();
										//simulacion de la parte de servicios
										mapaOrden.put("codigo_0",mapa.get("codigoActividad_"+i));
										mapaOrden.put("finalidad_0",mapa.get("finalidadServicio_"+i).toString()); 
										mapaOrden.put("cantidad_0","1");
										mapaOrden.put("urgente_0",ValoresPorDefecto.getValorFalseParaConsultas());
										mapaOrden.put("numRegistros","1");
										
										orden.setServicios(mapaOrden);
										
										if(Double.parseDouble(orden.guardarOrdenAmbulatoria(con,false, this.valoracion.getProfesional(), paciente, request, this.errores)[0])>0)
										{
											resp0=1;
											//se consulta el código del número de orden resultante
											numeroOrden = Utilidades.obtenerCodigoOrdenAmbulatoria(con,orden.getNumeroOrden(),this.valoracion.getProfesional().getCodigoInstitucionInt());
											
											//**********SE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA************************
											HashMap mapaRef = new HashMap();
											String[] vector;
											
											if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
											{
												vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
												mapaRef.put("acronimoDiag",vector[0]);
												mapaRef.put("tipoCieDiag",vector[1]);
												
												//*****SE GENERA LA RESPUESTA DE EPIDEMIOLOGÍA********************+
												resp0 = Utilidades.actualizarDatosEpidemiologia(
													con, 
													mapa.get("diagnostico_"+i).toString(),
													mapa.get("valorFicha_"+i)==null?"":mapa.get("valorFicha_"+i).toString(),
													"0", 
													paciente, 
													this.valoracion.getProfesional());
												//******************************************************************
											}
											if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
											{
												vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
												mapaRef.put("finalidad",vector[0]);
											}	
											if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
											{
												vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
												mapaRef.put("causaExterna",vector[0]);
											}	
											if(mapa.get("respuesta_"+i)!=null)
												mapaRef.put("resultados",mapa.get("respuesta_"+i));
											mapaRef.put("numeroOrden",numeroOrden);
											mapaRef.put("numSolConsulta",numSolConsulta);
											
											if(resp0>0)
												resp0 = OrdenesAmbulatorias.ingresarInformacionReferenciaExterna(con,mapaRef);
											//******************************************************************************
										}
										
										
									}
									else
									{
										numeroOrden = mapa.get("numeroOrdenActividad_"+i).toString();
										resp0 = 1;
									}
									//*****************************************************************************/**
								}
							}
							else
							{
								resp0 = 1;
								numSolConsulta = mapa.get("numeroSolicitudActividad_"+i).toString();
								//***********SE GENERA LA RESPUESTA DE LA CONSULTA*************************************************
								//Se retoma la valoracion de la consulta principal para crear una replica
								try
								{
									//Se instancia DTO de valoracion PYP
									DtoValoracion valoracionPYP = new DtoValoracion();
									//Se copian las propiedades de la valoracion principal
									PropertyUtils.copyProperties(valoracionPYP, this.valoracion);
									valoracionPYP.setNumeroSolicitud(numSolConsulta);
									
									String[] vector;
									//Se verifican los datos que se tomaron desde PYP
									if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
									{
										logger.info("\n\n\n paso por aqui DIAGNOSTICOS "+i);
										vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
										Diagnostico diagnostico= new Diagnostico(vector[0],Integer.parseInt(vector[1]));
										diagnostico.setPrincipal(true);
										diagnostico.setNumero(0);
										
										//Se reemplaza el diagnostico principal de la valoracion
										valoracionPYP.getDiagnosticos().set(0,diagnostico);
										
										//*****SE GENERA LA RESPUESTA DE EPIDEMIOLOGÍA********************+
										resp0 = Utilidades.actualizarDatosEpidemiologia(
											con, 
											mapa.get("diagnostico_"+i).toString(),
											mapa.get("valorFicha_"+i)==null?"":mapa.get("valorFicha_"+i).toString(),
											numSolConsulta, 
											paciente, 
											this.valoracion.getProfesional());
										//******************************************************************
										
									}
									if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
									{
										vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
										valoracionPYP.getValoracionConsulta().setCodigoFinalidadConsulta(vector[0]);
									}	
									if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
									{
										vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
										valoracionPYP.setCodigoCausaExterna(Integer.parseInt(vector[0]));
										valoracionPYP.setNombreCausaExterna(vector[1]);
									}	
									
									//*******************SE INSERTAN LOS DATOS DE LA VALORACION***********************************************
									String observacionCapitacion = null;
									ResultadoBoolean respVal= Valoraciones.valoracionDao.insertarBase(con, valoracionPYP, observacionCapitacion);
									
									if(respVal.isTrue())
									{
										//Se interpreta la solicitud
										if(Solicitud.interpretarSolicitud(con, "", this.valoracion.getProfesional().getCodigoPersona(), this.valoracion.getFechaGrabacion(), this.valoracion.getHoraGrabacion(), valoracionPYP.getNumeroSolicitud())<=0)
											respVal.setResultado(false);
										//Actualizar el médico responde
										if(Solicitud.actualizarMedicoResponde(con, Integer.parseInt(valoracionPYP.getNumeroSolicitud()), this.valoracion.getProfesional())<=0)
											respVal.setResultado(false);
										
										//Se intenta actualizar el pool del médico
										Solicitud sol=new Solicitud();
										ArrayList array=Utilidades.obtenerPoolesMedico(con,this.valoracion.getFechaGrabacion(),this.valoracion.getProfesional().getCodigoPersona());
										if(array.size()==1)
											sol.actualizarPoolSolicitud(con,Integer.parseInt(valoracionPYP.getNumeroSolicitud()),Integer.parseInt(array.get(0)+""));
									}
									//********************************************************************************************************
									
									if(!respVal.isTrue())
										resp0 = 0;
									
									
								}
								catch(Exception e)
								{
									logger.error("Error generando respuesta de la valoracion en ingresarInformacionPYP de ValoracionInterconsultaAction_ "+e);
									resp0 = 0;
								}
								//*************************************************************************************************/**
								
							}
							
								
							logger.info("SE INSERTA EL PROGRAMA PYP CUENTA CERRADA");
							//2) Se inserta el programa********************************** 
							if(consecutivoPrograma.equals("0"))
							{
								String codigoPrograma = mapa.get("codigoPrograma_"+i).toString();
								String institucionPrograma = mapa.get("institucionPrograma_"+i).toString();
								
								//Se verifica si el programa ya fue insertado, si ya fue insertado
								//retorna el consecutivo del programa si no retorna una cdena vacía
								consecutivoPrograma = fueInsertadoPrograma(con,registro,codigoPrograma,institucionPrograma,paciente.getCodigoPersona()+""); 
								if(consecutivoPrograma.equals(""))
								{
									//No existe programa se debe insertar
									programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
									programas.setCampos("codigoPrograma",codigoPrograma);
									programas.setCampos("usuario",mapa.get("usuarioPrograma_"+i));
									programas.setCampos("institucion",institucionPrograma);
									resp1 = programas.insertarPrograma(con);
									
									consecutivoPrograma = resp1 + "";
									registro.add(programas.getCampos("codigoPrograma")+ConstantesBD.separadorSplit+programas.getCampos("institucion")+ConstantesBD.separadorSplit+consecutivoPrograma);
								}
								else
									resp1 = 1;
								
							}
							else
								resp1 = 1;
							
							
							logger.info("SE INSERTA LA ACTIVIDAD PYP CUENTA CERRADA");
							//3) Se inserta la actividad************************************
							programas.clean();
							if(resp1>0)
							{								
								//se parametrizan campos comunes
								programas.setCampos("estadoPrograma",ConstantesBD.codigoEstadoProgramaPYPEjecutado);
								if(cuentaAbierta||estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
									programas.setCampos("numeroSolicitud",numSolConsulta);
								else
									programas.setCampos("numeroOrden",numeroOrden);
								programas.setCampos("fechaEjecutar",UtilidadFecha.getFechaActual());
								programas.setCampos("horaEjecutar",UtilidadFecha.getHoraActual());
								programas.setCampos("usuarioEjecutar",mapa.get("usuarioActividad_"+i));
								
								logger.info("consecutivoActividad=> "+consecutivoActividad+", estadoActividad=> "+estadoActividad);
								///Se verifica si la actividad ya existe o fue ejecutada para el paciente en PYP
								if(consecutivoActividad.equals("0")||estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)||
									estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado))
								{
									//********SE DEBE INSERTAR ACTIVIDAD********************************************
									programas.setCampos("consecutivoPrograma",consecutivoPrograma);
									programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
									if(mapa.get("frecuenciaActividad_"+i)!=null)
										programas.setCampos("frecuencia",mapa.get("frecuenciaActividad_"+i));
									programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
									
									resp2 = programas.insertarActividad(con);
									consecutivoActividad = resp2 + "";
									resp3 = 1;	
								}
								else
								{
									//******SE DEBE MODIFICAR LA ACTIVIDAD***************************************
									programas.setCampos("consecutivoActividadPYP",consecutivoActividad);
									
//									resp2 = programas.modificarActividad(con);
									logger.info("Se modificó la actividad!!!! "+resp2);
									
									if(cuentaAbierta)
									{
										if(estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
										{
											//******************SE ACTUALIZA LA ORDEN AMBULATORIA*******************
											HashMap campos = new HashMap();
											campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada);
											campos.put("numeroSolicitud",numSolConsulta);
											campos.put("numeroOrden",mapa.get("numeroOrdenActividad_"+i));
											campos.put("usuario",this.valoracion.getProfesional().getLoginUsuario());
											
											resp3 = OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,campos);
						
											//********************************************************************************
										}
										else
											resp3 = 1;
									}
									else if(!estadoActividad.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
									{
										//**********SE INSERTAN DATOS ADICIONALES DE LA ORDEN AMBULATORIA************************
										HashMap mapaRef = new HashMap();
										String[] vector;
										
										if(mapa.get("diagnostico_"+i)!=null&&!mapa.get("diagnostico_"+i).toString().equals(""))
										{
											vector = mapa.get("diagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
											mapaRef.put("acronimoDiag",vector[0]);
											mapaRef.put("tipoCieDiag",vector[1]);
										}
										if(mapa.get("finalidad_"+i)!=null&&!mapa.get("finalidad_"+i).toString().equals(""))
										{
											vector = mapa.get("finalidad_"+i).toString().split(ConstantesBD.separadorSplit);
											mapaRef.put("finalidad",vector[0]);
										}	
										if(mapa.get("causaExterna_"+i)!=null&&!mapa.get("causaExterna_"+i).toString().equals(""))
										{
											vector = mapa.get("causaExterna_"+i).toString().split(ConstantesBD.separadorSplit);
											mapaRef.put("causaExterna",vector[0]);
										}	
										if(mapa.get("respuesta_"+i)!=null)
											mapaRef.put("resultados",mapa.get("respuesta_"+i));
										mapaRef.put("numeroOrden",mapa.get("numeroOrdenActividad_"+i));
										mapaRef.put("numSolConsulta",numSolConsulta);
										
										
										resp3 = OrdenesAmbulatorias.ingresarInformacionReferenciaExterna(con,mapaRef);
										//******************************************************************************
									}
									
								}
							}
							//fin insertar actividad---------
							if(resp2>0)
							{
								//**********VERIFICACION DEL ACUMULADO********************************
								//Se consulta el convenio pyp asociado a la solicitud
								DtoConvenio convenio = UtilidadesHistoriaClinica.obtenerConvenioPypSolicitud(con, numSolConsulta);
								
								logger.info("CONVENIO PARA LA ACUMULACION=> "+convenio.getCodigo());
								
								programas.clean();
								programas.setCampos("codigoPaciente",paciente.getCodigoPersona());
								programas.setCampos("centroAtencion",mapa.get("centroAtencionActividad_"+i));
								programas.setCampos("tipoRegimen",convenio.getAcronimoTipoRegimen());
								programas.setCampos("codigoPrograma",mapa.get("codigoPrograma_"+i));
								programas.setCampos("consecutivoActividad",mapa.get("consecutivoActividad_"+i));
								programas.setCampos("codigoConvenio",convenio.getCodigo());
								programas.setCampos("fecha",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
								programas.setCampos("institucion",mapa.get("institucionPrograma_"+i));
								
								//como no existe acumulado para esta actividad se inserta una nueva
								resp4 = programas.actualizarAcumuladoActividades(con);
								
								
								//*******************************************************************
							}
								
							if(resp0<=0||resp1<=0||resp2<=0||resp3<=0||resp4<=0)
							{
								this.errores.add("",new ActionMessage("errors.problemasGenericos","ejecutando la actividad PYP con codigo "+consecutivoActividad+", del programa "+consecutivoPrograma));
								logger.error("Error actualizando informacion de PYP en ingresarInformacionPYP resp0=>"+resp0+", resp1=>"+resp1+", resp2=>"+resp2+", resp3=>"+resp3+", resp4=>"+resp4);
							}
								
							/*******FIN UNIFICAR PYP NO*************************************************************************/
							logger.info("FIN UNIFICAR PYP NO");
							
						}
						//Fin IF unificar PYP	
						
					}//Fin IF que verifica si actividad fue ejecutada
					
				}
				//Fin For
				
			}
			
			request.getSession().removeAttribute("mapaPYPConsultaExterna");
		}
	}
	
	/**
	 * Método qque verifica si ya fue insertado un programa
	 * @param con 
	 * @param registro
	 * @param codigoPrograma
	 * @param institucionPrograma
	 * @param codigoPaciente 
	 * @return
	 */
	private String fueInsertadoPrograma(Connection con, Vector registro, String codigoPrograma, String institucionPrograma, String codigoPaciente) 
	{
		String[] vector;
		String consecutivoPrograma = "";
		
		if(registro.size()>0)
		{
			int contador = 0;
			while(contador<registro.size())
			{
				vector = registro.get(contador).toString().split(ConstantesBD.separadorSplit);
				if(vector[0].equals(codigoPrograma)&&vector[1].equals(institucionPrograma))
					consecutivoPrograma = vector[2];
				contador++;
			}
		}
		
		if(consecutivoPrograma.equals(""))
			//si el programa no se encontro en el arreglo se debe verificar que no se haya
			//insertado en la base de datos
			consecutivoPrograma = ProgramasPYP.consultarConsecutivoProgramaExistente(con,codigoPaciente,codigoPrograma,institucionPrograma);
		
		return consecutivoPrograma;
	}
	
	/**
	 * Método implementado para realizar los procesos adicionales que implican al seleccionar una conducta de valoracion
	 * @param con
	 * @param paciente
	 * @param esModificacion 
	 * @throws IPSException 
	 */
	private void procesoConductaValoracion(Connection con, PersonaBasica paciente, boolean esModificacion) throws IPSException
	{
		///*********VALIDACIONES CONDUCTA A SEGUIR*****************************************
		switch(this.valoracionUrgencias.getCodigoConductaValoracion())
		{
			case ConstantesBD.codigoConductaSeguirSalaEspera:
			case ConstantesBD.codigoConductaSeguirInterconsulta:
			case ConstantesBD.codigoConductaSeguirSalaReanimacion:
				//Se tramita el pre-egreso
				if(egresoDao.insertarPreEgreso(con, paciente.getCodigoCuenta(), this.valoracionUrgencias.getProfesional().getCodigoPersona())<=0)
					errores.add("",new ActionMessage("errors.problemasGenericos","insertando el pre-egreso"));
				break;
				
			case ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria:
			case ConstantesBD.codigoConductaSeguirHospitalizarPiso:
			case ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial:
				if(paciente.getExisteAsocio())
					errores.add("",new ActionMessage("errors.yaExisteAmplio","Problemas registrando el asocio automático porque","un asocio registrado. Proceso Cancelado"));
				else if(Cuenta.asociarCuentaEstatico(con, paciente.getCodigoCuenta(), paciente.getCodigoIngreso(), this.valoracionUrgencias.getProfesional().getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.problemasGenericos","generando el asocio de cuentas automático"));
				else 
					//se inserta el egreso automático para hospitalización
					this.insertarEgresoAutomaticoHospitalizacion(con, paciente);
				break;
			
			case ConstantesBD.codigoConductaSeguirSalidaSinObservacion:
			case ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad:
			
				///Se realiza la validaciónm del reingreso
				this.validacionReingreso(con,paciente);
								
				//Se inserta el egreso automático a la salida
				this.insertarEgresoAutomaticoSalida(con, paciente);
				
				//Si el paciente murió se actualizan sus datos
				if(!UtilidadTexto.getBoolean(this.valoracionUrgencias.getEstadoSalida()))
				{
					if(!UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,
							paciente.getCodigoPersona(),
							false,
							this.valoracionUrgencias.getFechaMuerte(), 
							this.valoracionUrgencias.getHoraMuerte(), 
							this.valoracionUrgencias.getCertificadoDefuncion(),
							ConstantesBD.continuarTransaccion))
						errores.add("",new ActionMessage("errors.problemasGenericos","registrando la muerte del paciente"));
				}
				//Si la conducta fue remitir se verifica si se puede abrir la referencia
				else if(this.valoracionUrgencias.getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
					this.setDeboAbrirReferencia(UtilidadTexto.getBoolean(ValoresPorDefecto.getLlamadoAutomaticoReferencia(this.valoracionUrgencias.getProfesional().getCodigoInstitucionInt())));
				
				//Se verifica que no hayan solicitudes incompletas en la cuenta (solo aplica en modificación)
				if(esModificacion)
				{
					try 
					{
						ResultadoBoolean resultado = UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(),this.valoracionUrgencias.getProfesional().getCodigoInstitucionInt(), false,UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(this.valoracionUrgencias.getProfesional().getCodigoInstitucionInt())));
						if(resultado.isTrue())
							errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
					} 
					catch (SQLException e) 
					{
						logger.error("Error en procesoConductaValoracion: "+e);
					}
				}
			break;
			
			case ConstantesBD.codigoConductaSeguirCamaObservacion:
				if(!AdmisionUrgencias.actualizarFechaHoraIngresoObservacion(con, UtilidadFecha.getFechaActual(con), UtilidadFecha.getHoraActual(con), paciente.getCodigoCuenta()))
					errores.add("",new ActionMessage("errors.ingresoDatos","la fecha y hora de observación a urgencias. Por favor reportar el error al administrador del sistema"));
			break;
		}
		//********************************************************************************
	}
	
	/**
	 * Se verifica si se debe hacer la pregunta sobre el reingreso
	 * Descripcion: se consulta el último egreso médico del paciente de la vía de ingreso de urgencias
	 * y se verifica que el tiempo transcurrido en minutos hatsa la fecha actual sea menor al parámetro de 
	 * tiempo máximo reingreso urgencias para activar el campo validacionReingreso
	 * @param con
	 * @param paciente
	 */
	private void validacionReingreso(Connection con, PersonaBasica paciente) 
	{
		HashMap datosEgreso = Utilidades.consultaUltimoEgresoEvolucion(con, paciente.getCodigoPersona(), ConstantesBD.codigoViaIngresoUrgencias);
		
		//logger.info("NUMERO DE REGISTROS ENCONTRADOS DEL EGRESO=> "+datosEgreso.get("numRegistros"));
		if(Utilidades.convertirAEntero(datosEgreso.get("numRegistros").toString())>0)
		{
			//SE toma fecha/hora inicial (egreso anterior)
			this.fechaEgresoAnterior=UtilidadFecha.conversionFormatoFechaAAp(datosEgreso.get("fecha_0").toString());
			this.horaEgresoAnterior=datosEgreso.get("hora_0").toString();
			this.diagnosticoEgresoAnterior = new Diagnostico(datosEgreso.get("acronimo_0").toString(),Integer.parseInt(datosEgreso.get("cie_0").toString()));
			this.diagnosticoEgresoAnterior.setNombre(datosEgreso.get("nombre_0").toString());
			
			int minutosParametro = Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaximoReingresoUrgencias(this.valoracionUrgencias.getProfesional().getCodigoInstitucionInt()),true);
			int minutosCalculados = UtilidadFecha.numeroMinutosEntreFechas(this.fechaEgresoAnterior, this.horaEgresoAnterior, paciente.getFechaIngreso(), paciente.getHoraIngreso());
			
			/*logger.info("fechaEgresoAnterior: "+this.fechaEgresoAnterior);
			logger.info("horaEgresoAnterior: "+this.horaEgresoAnterior);
			logger.info("diagnosticoEgresoAnterior: "+this.diagnosticoEgresoAnterior.getValor());
			logger.info("minutosParametro: "+minutosParametro);
			logger.info("minutosCalculados: "+minutosCalculados);**/
			
			
			if(minutosCalculados<=minutosParametro)
				this.validacionReingreso = true;
			else
				this.validacionReingreso = false;
			
		}
		
		
	}
	
	/**
	 * Método implementado para realizar un cargar genérico de las valoraciones de consulta
	 * @param con
	 * @param paciente
	 * @param numeroSolicitud
	 */
	private void cargarGenericoConsulta(Connection con,PersonaBasica paciente,String numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
		//logger.info("NUMERO DE SOLICITUD DE LA CONSULTA=> "+numeroSolicitud);
		int codigoCuenta = Utilidades.getCuentaSolicitud(con, Integer.parseInt(this.numeroSolicitud));
		
		///SE cargan los datos de la valoración
		this.valoracion = Valoraciones.valoracionDao.cargarBase(con, this.numeroSolicitud);
		
		//se settea el valor de estado de embarazo
//		if (this.valoracion.getEstadoEmbarazada().equals(ConstantesBD.acronimoSi)) {
//			this.estadoEmbarazada= true;
//		}else{
//			this.estadoEmbarazada= false;
//		}
		
		/**
		 * MT 5568
		 */
		InfoDatosInt centroCostoPaciente;
		try {
			centroCostoPaciente = this.obtenerDatosCentroCostoXSolcitud(con, Integer.parseInt(this.numeroSolicitud));
			this.valoracion.setDatoAreaPaciente(centroCostoPaciente);
		} catch (IPSException e) {
			logger.error(e);
		}				
		/**
		 * Fin MT 5568
		 */	
		
		
		
		//Se carga el número de autorizacion
		//this.numeroAutorizacion = Solicitud.cargarNumeroAutorizacionEstatico(con, Integer.parseInt(this.numeroSolicitud));
		
		//Se verifica si se debe abrir PYP -----------------------------------------------------------------------------------
		this.valoracion.setAbrirPYP(UtilidadValidacion.tienePacienteInformacionPYP(con, paciente, this.valoracion.getProfesional().getCodigoInstitucionInt()));
		
		///Se cargan los diagnosticos relacionados al mapa
		this.asignarDiagnosticosRelacionados();
		
		//Se consulta el tipo de evento de la cuenta --------------------------------------------------------------------------
		valoracion.setCodigoTipoEvento(Cuenta.obtenerCodigoTipoEventoCuenta(con, codigoCuenta+""));
		if(valoracion.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo))
			valoracion.setFueAccidenteTrabajo(true);
		else
			valoracion.setFueAccidenteTrabajo(false);
		
		//*************SE CARGA LA INFORMACION DE COMPONENTES*************************************
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			this.valoracion.setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", this.numeroSolicitud));
			
			//Se verifica si se ingresó información del componente
			if(!this.valoracion.getHistoriaMenstrual().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
		{
			this.valoracion.setOftalmologia(UtilidadesHistoriaClinica.cargarOftalmologia(con, this.numeroSolicitud, ConstantesBD.codigoNuncaValido));
			//Se verifica si se ingresó información del componente
			if(!this.valoracion.getOftalmologia().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria))
		{
			this.valoracion.setPediatria(UtilidadesHistoriaClinica.cargarPediatria(con, paciente.getCodigoPersona()+"", this.numeroSolicitud, paciente.getEdad(), null));
			//Se verifica si se ingresó información del componente
			if(!this.valoracion.getPediatria().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponentePediatria,false);
		}
		///Se ocultan los posibles componentes que puede tener la plantilla y que no aplica para el resumen
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaObstetrica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaOftalmologica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteTratamientoOdontologia,false);
		
		//Se verifica componente de signos vitales
		if(this.valoracion.getSignosVitales().size()==0&&!this.plantilla.tieneComponenteInformacionParametrizable(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales,false);
		//*****************************************************************************************
	}
	
	
	/**
	 * Método implementado para cargar lo almacenado en la valoración de interconsulta
	 * @param con
	 * @param paciente
	 * @param numeroSolicitud
	 */
	public void cargarInterconsulta(Connection con,PersonaBasica paciente,String numeroSolicitud)
	{
		//Se realiza un cargado genérico de la consultya
		this.cargarGenericoConsulta(con, paciente, numeroSolicitud);
		
		//Se intenta cargar la información de la solicitud de interconsultas
		boolean exito = false;
		SolicitudInterconsulta mundoSolicitud = new SolicitudInterconsulta();
		
		try
		{
			exito = mundoSolicitud.cargar(con, Integer.parseInt(this.numeroSolicitud));
		}
		catch(Exception e)
		{
			logger.error("Error cargando la solicitud de interconsulta: "+e);
			exito = false;
			this.errores.add("",new ActionMessage("errors.problemasGenericos","cargando la solicitud "+numeroSolicitud+": "+e));
		}
		
		if(exito)
		{
			//Se asigna el número de autorización de la solicitud
			//this.numeroAutorizacion = mundoSolicitud.getNumeroAutorizacion();
			
			//***********************VALIDACION MANEJO CONJUNTO************************************************
			//Se obtiene el codigo del manejo de interconsulta
			this.codigoManejo = mundoSolicitud.getCodigoManejointerconsulta();
			//Si el codigo del manejo es Manejo Conjunto se agrega advertencia
			if(this.codigoManejo==ConstantesBD.codigoManejoConjunto)
			{
				ElementoApResource mensaje = new ElementoApResource("error.validacionessolicitud.manejoConjuntoActivado");
				this.advertencias.add(mensaje);
			}
			//*********************************************************************************************************
			
			
		}
	}
	
	/**
	 * Método para cargar el resumen de la valoración de consulta
	 * @param con
	 * @param paciente 
	 * @param numeroSolicitud
	 * @param codigoCita
	 */
	public void cargarConsulta(Connection con,PersonaBasica paciente, String numeroSolicitud,String codigoCita)
	{
		//Se realiza un cargado genérico de la consultya
		this.cargarGenericoConsulta(con, paciente, numeroSolicitud);
		
		//Si se tenía un código de cita se consulta la fecha/hora de agenda
		if(!this.codigoCita.equals(""))
		{
			String[] fechaHoraCita = UtilidadesConsultaExterna.obtenerFechaHoraCita(con, this.codigoCita);
			this.fechaConsulta = fechaHoraCita[0];
			this.horaConsulta = fechaHoraCita[1];
		}
		
		
	}
	
	/**
	 * Método para cargar el resumen de la valoración de urgencias
	 * @param con
	 * @param codigoPaciente 
	 * @param realizarValidaciones 
	 * @param paciente.getCodigoPersona() 
	 */
	public void cargarUrgencias(Connection con, UsuarioBasico usuario, PersonaBasica paciente, int codigoPaciente, boolean realizarValidaciones) 
	{

		int numeroSolicitudUrgencias = Integer.parseInt(this.numeroSolicitud);
		
		//MT 6218 : Se consulta el ingreso de acuerdo a la solicitud seleccionada y no por el ingreso del
		//paciente cargado en sesion
		int idIngreso = ConstantesBD.codigoNuncaValido;
		boolean esSolicitudHospitalizacionCuidadosEspeciales = Valoraciones.valoracionDao.esSolicitudDeCuidadosEspeciales(con, Integer.parseInt(this.numeroSolicitud));
		try {
			ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
			Ingresos ingreso = solicitudesMundo.obtenerIngresoPorNumeroSolicitud(numeroSolicitudUrgencias);
			idIngreso = ingreso.getId();
		} catch (IPSException e) {
			e.printStackTrace();
		}
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoracionUrgenciasEnHospitalizacion(Integer.valueOf(usuario.getCodigoInstitucion())))
				&& !esSolicitudHospitalizacionCuidadosEspeciales){
			
			//comentador por MT 6218
			//int idCuentaAsociada=util.UtilidadValidacion.tieneCuentaAsociada(con, paciente.getCodigoIngreso());
			int idCuentaAsociada=util.UtilidadValidacion.tieneCuentaAsociada(con, idIngreso);			
			
			if(idCuentaAsociada>0){
				numeroSolicitudUrgencias = obtenerNumeroSolicitudPrimeraValoracion(con,idCuentaAsociada);
			}			
		}

		int codigoCuenta = Utilidades.getCuentaSolicitud(con,numeroSolicitudUrgencias);
		this.valoracionUrgencias = Valoraciones.valoracionDao.cargarUrgencias(con, String.valueOf(numeroSolicitudUrgencias));
		
		/**
		 * MT 5568
		 */		
		
		InfoDatosInt centroCostoPaciente;
		try {
			centroCostoPaciente = this.obtenerDatosCentroCostoXSolcitud(con, numeroSolicitudUrgencias);
			this.valoracionUrgencias.setDatoAreaPaciente(centroCostoPaciente);
		} catch (IPSException e) {
			logger.error(e);
		}					
		/**
		 * Fin MT 5568
		 */			

		//Se cargan los diagnosticos relacionados al mapa
		this.asignarDiagnosticosRelacionados();
		
		this.valoracionUrgencias.setInformacionTriage(Valoraciones.valoracionDao.consultarInformacionTriageUrgencias(con, codigoCuenta+""));
		
		//si hubo conducta a seguir con egreso automático se verifica el estado ----------------------------------------------------------
		if(valoracionUrgencias.getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirSalidaSinObservacion||
			valoracionUrgencias.getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
		{
			
			boolean esMuerte = UtilidadValidacion.esPacienteMuerto(con, codigoPaciente);
			
			if(esMuerte)
			{
				valoracionUrgencias.setEstadoSalida(ConstantesBD.acronimoNo);
				
				HashMap informacionMuerte = UtilidadesHistoriaClinica.obtenerInfoMuertePaciente(con, codigoPaciente, codigoCuenta);
				valoracionUrgencias.getDiagnosticoMuerte().setAcronimo(informacionMuerte.get("diagnosticoMuerte").toString());
				valoracionUrgencias.getDiagnosticoMuerte().setTipoCIE(Integer.parseInt(informacionMuerte.get("diagnosticoMuerteCie").toString()));
				valoracionUrgencias.getDiagnosticoMuerte().setNombre(informacionMuerte.get("diagnosticoMuerteNombre").toString());
				valoracionUrgencias.setFechaMuerte(informacionMuerte.get("fechaMuerte").toString());
				valoracionUrgencias.setHoraMuerte(informacionMuerte.get("horaMuerte").toString());
				valoracionUrgencias.setCertificadoDefuncion(informacionMuerte.get("certificadoDefuncion").toString());
			}
			else
				valoracionUrgencias.setEstadoSalida(ConstantesBD.acronimoSi);
		}
		
		//Se verifica si se debe abrir PYP -----------------------------------------------------------------------------------
		valoracionUrgencias.setAbrirPYP(UtilidadValidacion.tienePacienteInformacionPYP(con, paciente, valoracionUrgencias.getProfesional().getCodigoInstitucionInt()));
		
		//Se consulta el tipo de evento de la cuenta --------------------------------------------------------------------------
		valoracionUrgencias.setCodigoTipoEvento(Cuenta.obtenerCodigoTipoEventoCuenta(con, codigoCuenta+""));
		if(valoracionUrgencias.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo))
			valoracionUrgencias.setFueAccidenteTrabajo(true);
		else
			valoracionUrgencias.setFueAccidenteTrabajo(false);
		
		//*************SE CARGA LA INFORMACION DE COMPONENTES*************************************
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			this.valoracionUrgencias.setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", String.valueOf(numeroSolicitudUrgencias)));
			//Se verifica si se ingresó información del componente
			if(!this.valoracionUrgencias.getHistoriaMenstrual().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
		{
			this.valoracionUrgencias.setOftalmologia(UtilidadesHistoriaClinica.cargarOftalmologia(con, String.valueOf(numeroSolicitudUrgencias), ConstantesBD.codigoNuncaValido));
			//Se verifica si se ingresó información del componente
			if(!this.valoracionUrgencias.getOftalmologia().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria))
		{
			this.valoracionUrgencias.setPediatria(UtilidadesHistoriaClinica.cargarPediatria(con, paciente.getCodigoPersona()+"",String.valueOf(numeroSolicitudUrgencias), paciente.getEdad(), null)); //no es necesario enviar usuario
			//Se verifica si se ingresó información del componente
			if(!this.valoracionUrgencias.getPediatria().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponentePediatria,false);
		}
		//Se ocultan los posibles componentes que puede tener la plantilla y que no aplica para el resumen
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaObstetrica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaOftalmologica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteTratamientoOdontologia,false);
		
		//Se verifica componente de signos vitales
		if(this.valoracionUrgencias.getSignosVitales().size()==0&&!this.plantilla.tieneComponenteInformacionParametrizable(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales,false);
		//*****************************************************************************************
		
		//Se verifica si se deben realizar valdaciones adicionales
		if(realizarValidaciones)
		{
			//Inicialmente se podría modificar la conducta de la valoracion
			this.puedoModificarConductaValoracion = true;
			
			//Se verifica si la cama de urgencias fue asignada
			boolean asignadaCamaUrgencias = AdmisionUrgencias.estaAsignadaCamaObservacion(con, codigoCuenta);
			//Se verifica si existen evoluciones
			boolean existeEvoluciones = HistoricoEvoluciones.existeHistoricoEvoluciones(con, codigoCuenta);
			//Se verifica si existe egreso
			this.existeEgreso = Egreso.existeEgresoEfectivo(con, codigoCuenta);
			
			if(asignadaCamaUrgencias||existeEvoluciones||existeEgreso)
				this.puedoModificarConductaValoracion = false;
			
			if(asignadaCamaUrgencias&&!existeEvoluciones)
			{
				ElementoApResource mensaje = new ElementoApResource("error.valoracion.modificarConductaConCama");
				this.advertencias.add(mensaje);
			}
			
			//Si puedo modificar la conducta a seguir se cargan los arreglosd
			if(this.puedoModificarConductaValoracion)
			{
				this.conductasValoracion = UtilidadesHistoriaClinica.cargarConductasValoracion(con);
				//Tipos de monitoreo (Se usan cuando se selecciona conducta Traslado Cuidado Especial)
				this.tiposMonitoreo = UtilidadesManejoPaciente.obtenerTiposMonitoreo(con, usuario.getCodigoInstitucionInt());
			}
			
			//Se preparan las nuevas observaciones
			this.prepararNuevasObservaciones(usuario, valoracionUrgencias);
		}
		
	}
	
	/**
	 * Método para cargar el resumen de la valoracion de hospitalizacion
	 * @param con
	 * @param usuario 
	 * @param realizarValidaciones 
	 * @param paciente 
	 */
	public void cargarHospitalizacion(Connection con, UsuarioBasico usuario, PersonaBasica paciente, boolean realizarValidaciones)
	{
		int codigoCuenta = Utilidades.getCuentaSolicitud(con, Integer.parseInt(this.numeroSolicitud));
		this.valoracionHospitalizacion = Valoraciones.valoracionDao.cargarHospitalizacion(con, this.numeroSolicitud);
		//****************************
		
		//MT 6037 : Se consulta el ingreso de acuerdo a la solicitud seleccionada y no por el ingreso del
		//paciente cargado en sesion.
		boolean esSolicitudHospitalizacionCuidadosEspeciales = Valoraciones.valoracionDao.esSolicitudDeCuidadosEspeciales(con, Integer.parseInt(this.numeroSolicitud));
		int idIngreso = ConstantesBD.codigoNuncaValido;		
		try {
			ISolicitudesMundo solicitudesMundo = new SolicitudesMundo();
			Ingresos ingreso = solicitudesMundo.obtenerIngresoPorNumeroSolicitud(Integer.parseInt(this.numeroSolicitud));
			idIngreso = ingreso.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoracionUrgenciasEnHospitalizacion(Integer.valueOf(usuario.getCodigoInstitucion())))
				&& !esSolicitudHospitalizacionCuidadosEspeciales){
			//int idCuentaAsociada=util.UtilidadValidacion.tieneCuentaAsociada(con, paciente.getCodigoIngreso());		
			int idCuentaAsociada=util.UtilidadValidacion.tieneCuentaAsociada(con, idIngreso);
			
			if(idCuentaAsociada>0){
				cargarUrgencias(con, usuario, paciente, paciente.getCodigoPersona(), realizarValidaciones);
				int numeroSolicitudAsocio = obtenerNumeroSolicitudPrimeraValoracion(con,idCuentaAsociada);				
				valoracionHospitalizacion.setObservaciones((ArrayList<DtoValoracionObservaciones>)Valoraciones.valoracionDao.cargarObservaciones(con, String.valueOf(numeroSolicitudAsocio)));				
			}			
		}
		
		
		/**
		 * MT 5568
		 */
		try {
			InfoDatosInt centroCostoPaciente = this.obtenerDatosCentroCostoXSolcitud(con, Integer.parseInt(this.numeroSolicitud));
			this.valoracionHospitalizacion.setDatoAreaPaciente(centroCostoPaciente);			
		} catch (Exception e) {
			logger.error(e);
		}		
		/**
		 * Fin MT 5568
		 */			
		
		//Se cargan los diagnosticos relacionados al mapa
		this.asignarDiagnosticosRelacionados();
		
		//Se verifica si se debe abrir PYP -----------------------------------------------------------------------------------
		valoracionHospitalizacion.setAbrirPYP(UtilidadValidacion.tienePacienteInformacionPYP(con, paciente, valoracionHospitalizacion.getProfesional().getCodigoInstitucionInt()));
		
		//Se consulta el tipo de evento de la cuenta --------------------------------------------------------------------------
		valoracionHospitalizacion.setCodigoTipoEvento(Cuenta.obtenerCodigoTipoEventoCuenta(con, codigoCuenta+""));
		if(valoracionHospitalizacion.getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTrabajo))
			valoracionHospitalizacion.setFueAccidenteTrabajo(true);
		else
			valoracionHospitalizacion.setFueAccidenteTrabajo(null);
		
		//*************SE CARGA LA INFORMACION DE COMPONENTES*************************************
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			this.valoracionHospitalizacion.setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", this.numeroSolicitud));
			
			//Se verifica si se ingresó información del componente
			if(!this.valoracionHospitalizacion.getHistoriaMenstrual().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
		{
			this.valoracionHospitalizacion.setOftalmologia(UtilidadesHistoriaClinica.cargarOftalmologia(con, this.numeroSolicitud, ConstantesBD.codigoNuncaValido));
			//Se verifica si se ingresó información del componente
			if(!this.valoracionHospitalizacion.getOftalmologia().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia,false);
		}
		if(this.plantilla.existeComponente(ConstantesCamposParametrizables.tipoComponentePediatria))
		{
			this.valoracionHospitalizacion.setPediatria(UtilidadesHistoriaClinica.cargarPediatria(con,paciente.getCodigoPersona()+"", this.numeroSolicitud, paciente.getEdad(), null)); //no es neceario enviar usuario
			//Se verifica si se ingresó información del componente
			if(!this.valoracionHospitalizacion.getPediatria().estaCargado())
				this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponentePediatria,false);
		}
		//Se ocultan los posibles componentes que puede tener la plantilla y que no aplica para el resumen
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaObstetrica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteHojaOftalmologica,false);
		this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteTratamientoOdontologia,false);
		
		//Se verifica componente de signos vitales
		if(this.valoracionHospitalizacion.getSignosVitales().size()==0&&!this.plantilla.tieneComponenteInformacionParametrizable(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			this.plantilla.ocultarComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales,false);
		//*****************************************************************************************
		
		if(realizarValidaciones)
			//Se preparan las nuevas observaciones
			this.prepararNuevasObservaciones(usuario, valoracionHospitalizacion);
	}
	
	
	/**
	 * Método estático para cargar la valoración base
	 * @param con
	 * @return
	 */
	public static DtoValoracion cargarBase(Connection con, String numeroSolicitud)
	{
		return valoracionDao().cargarBase(con, numeroSolicitud);
	}
	
	/**
	 * Método para insertar las observaciones de la valoración
	 * @param con
	 * @param valoracion
	 * @return
	 */
	public static ResultadoBoolean insertarObservaciones(Connection con,DtoValoracion valoracion)
	{
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		return valoracionDao().insertarObservaciones(con, valoracion.getObservaciones(), valoracion.getNumeroSolicitud(), fechaSistema, horaSistema);
	}
	
	/**
	 * Método implementado para realizar la modificación de la valoración de urgencias
	 * @param con
	 * @param paciente
	 * @throws IPSException 
	 */
	public void modificarUrgencias(Connection con,PersonaBasica paciente) throws IPSException
	{
		//Se modifican los datos que hacen parte de la estructura propia de valoraciones
		ResultadoBoolean resultado = Valoraciones.valoracionDao.modificarUrgencias(con, this.valoracionUrgencias);
		
		//Se reversa lo que se tenía de la conducta a seguir anterior
		if
		(
			resultado.isTrue()
			&&
			//También se verifica que se haya podido modificar la conducta de la valoración
			this.puedoModificarConductaValoracion
		)
		{
			//Si la conducta a seguir anterior fue CAMA OBSERVACION se verifica se eliminan egresos
			if(valoracionUrgencias.getCodigoConductaValoracionAnterior()!=ConstantesBD.codigoConductaSeguirCamaObservacion)
			{
			
				//Si la conducta anterior fue alguna de las que generan pre-egreso entonces se elimina
				if(valoracionUrgencias.getCodigoConductaValoracionAnterior()==ConstantesBD.codigoConductaSeguirSalaEspera||
					valoracionUrgencias.getCodigoConductaValoracionAnterior()==ConstantesBD.codigoConductaSeguirInterconsulta||
					valoracionUrgencias.getCodigoConductaValoracionAnterior()==ConstantesBD.codigoConductaSeguirSalidaSinObservacion||
					valoracionUrgencias.getCodigoConductaValoracionAnterior()==ConstantesBD.codigoConductaSeguirSalaReanimacion)
				{
					if(!Egreso.borrarPreEgreso(con, paciente.getCodigoCuenta()))
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas tratando de eliminar el pre-egreso de la cuenta del paciente");
					}
				}
				else
				{
					if(!Egreso.borrarEgresoAutomatico(con, paciente.getCodigoCuenta()))
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas tratando de eliminar el egreso automático de la cuenta del paciente");
					}
				}
			}
			else if(valoracionUrgencias.getCodigoConductaValoracion()!=ConstantesBD.codigoConductaSeguirCamaObservacion)
			{
				//Se elimina el registro de fecha/hora ingreso observacion
				if(!AdmisionUrgencias.actualizarFechaHoraIngresoObservacion(con, "", "", paciente.getCodigoCuenta()))
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas tratando de eliminar el registro de la fecha/hora ingreso a observacion de la cuenta del paciente");
				}
			}
			
			//************SE HACE LLAMADO A LOS PROCESOS ADICIONALES QUE TIENEN QUE VER CON LA CONDUCTA VALORACION***********************
			if(resultado.isTrue())
				procesoConductaValoracion(con, paciente, true);
		}
		
		if(!resultado.isTrue())
			errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		
		
		//***************************REGISTRO DE LAS INCAPACIDADES *******************************************
		DtoRegistroIncapacidades dtoIncapacidad = new DtoRegistroIncapacidades();
		dtoIncapacidad.setIngreso(paciente.getCodigoIngreso());
		dtoIncapacidad.setValoracion(Integer.parseInt(this.valoracionUrgencias.getNumeroSolicitud()));
		dtoIncapacidad.getGrabacion().setUsuarioModifica(this.valoracionUrgencias.getProfesional().getLoginUsuario());
		dtoIncapacidad.setEspecialidad(Integer.parseInt(this.valoracionUrgencias.getEspecialidadProfResponde()));
		dtoIncapacidad.setAcronimoDiagnostico(this.valoracionUrgencias.getDiagnosticos().get(0).getAcronimo());
		dtoIncapacidad.setTipoCie(this.valoracionUrgencias.getDiagnosticos().get(0).getTipoCIE());
		ResultadoBoolean respIncapacidad = RegistroIncapacidades.activarRegistrosIncapacidades(con, dtoIncapacidad);
		if(!respIncapacidad.isTrue())
		{
			errores.add("", new ActionMessage("errors.notEspecific",respIncapacidad.getDescripcion()));
		}
		//**************************************************************************
		
	}
	
	/**
	 * Método que realiza la modificación de la valoración de hospitalización
	 * @param con
	 */
	public void modificarHospitalizacion(Connection con, PersonaBasica paciente,DtoValoracionObservaciones dtoValoracionObservaciones)
	{
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		/*Alberto Ovalle mt5749 modificacion hospitalizacion*/
		//Se modifican los datos que hacen parte de la estructura propia de valoraciones
		//ResultadoBoolean resultado=Valoraciones.valoracionDao.insertarObservaciones(con, this.valoracionHospitalizacion.getObservaciones(), this.valoracionHospitalizacion.getNumeroSolicitud(), fechaSistema, horaSistema);
	    Valoraciones.valoracionDao.modificarObsevacionesHospitalizacion(con,dtoValoracionObservaciones,this.valoracionHospitalizacion,fechaSistema,horaSistema);
        //if(!resultado.isTrue())
        //errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		//********************************************************************************************//
		//*******************ACTUALIZACION DE LA VALORACION EN REGISTRO CUIDADO ESPECIAL*******************
		//solo aplicapara pacientes con tipo paciente Hospitalizado
		if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
		{
			ResultadoBoolean resultado = Valoraciones.valoracionDao.actualizarValoracionRegistroCuidadoEspecial(
				con, 
				paciente.getCodigoIngreso()+"", 
				this.valoracionHospitalizacion.getNumeroSolicitud(),
				//Se toma el login del profesional que realiza la modificación
				this.valoracionHospitalizacion.getObservaciones().get(this.valoracionHospitalizacion.getObservaciones().size()-1).getProfesional().getLoginUsuario(),
				false /*No es obligatorio*/
			);
			if(!resultado.isTrue())
				errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		}
		//**************************************************************************************************
		
	}
	
	/**
	 * Método implementado para cargar los históricos de valoraciones y/o evoluciones en caso de asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static HashMap<String, Object> cargarHistorico(Connection con,String idIngreso)
	{
		return valoracionDao().cargarHistorico(con, idIngreso);
	}
	
	/**
	 * Método implementado para obtener la fecha/hora grabacion de la valoración de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String[] obtenerFechahoraGrabacionValoracionUrgencias(Connection con,String idCuenta)
	{
		return valoracionDao().obtenerFechahoraGrabacionValoracionUrgencias(con, idCuenta);
	}
	
	/**
	 * Generar reporte inicial de urgencias 
	 * @param Connection con
	 * @param int codigo
	 * @param int codigoIngreso
	 * @param int codigoConvenio
	 * @param int institucion
	 * @param String loginUsuario
	 * */
	public ActionErrors generarReporteInicialUrgenciasValoraciones(
			Connection con,
			int codigoIngreso,
			int codigoConvenio,
			int institucion,
			String loginUsuario)
	{
		Convenio convenioInf = new Convenio();
	    convenioInf.consultarIndicadoresConvenio(con,codigoConvenio);
	    ActionErrors erroresInf = new ActionErrors();
	    
	    if(convenioInf.getReporte_atencion_ini_urg().equals(ConstantesBD.acronimoSi) && 
	    		convenioInf.getGeneracion_autom_val_urg().equals(ConstantesBD.acronimoSi))
	    {
			 erroresInf = (ActionErrors)RegistroEnvioInfAtencionIniUrg.insertarInformeAtencionIniUrg(
					con, 
					codigoIngreso+"",
					"",
					codigoConvenio+"", 
					institucion,
					loginUsuario).get("error");		
	    }
	    else	    	    	
	    	logger.info("\n\nNo se genero el reporte los indicadores del convenio no lo permiten, codigo del convenio >> "+codigoConvenio);
	    
	    return erroresInf;
	}
	
	
	/**
	 * Método implementado para insertar una valoración base de manera estática
	 * @param con
	 * @param valoracion
	 * @return
	 */
	public static ResultadoBoolean insertarBase(Connection con,DtoValoracion valoracion, String observacionCapitacion)
	{
		return valoracionDao().insertarBase(con, valoracion, observacionCapitacion);
	}
	//*****************************GETTERS & SETTERS***************************************************************************
	//***********************************************************************************************************************
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
	 * @return the tiposComponente
	 */
	public ArrayList<Integer> getTiposComponente() {
		return tiposComponente;
	}
	/**
	 * @param tiposComponente the tiposComponente to set
	 */
	public void setTiposComponente(ArrayList<Integer> tiposComponente) {
		this.tiposComponente = tiposComponente;
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
	private Object getDiagnosticosRelacionados(String key) {
		return diagnosticosRelacionados.get(key);
	}
	/**
	 * @param diagnosticosRelacionados the diagnosticosRelacionados to set
	 */
	private void setDiagnosticosRelacionados(String key,Object obj) {
		this.diagnosticosRelacionados.put(key,obj);
	}
	
	/**
	 * Método para obtener el número de diagnósticos relacionados
	 * @return
	 */
	private int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}
	
	
	/**
	 * Método para asignarle tamaño a los diagnósticos relacionados
	 * @param numRegistros
	 */
	private void setNumDiagRelacionados(int numRegistros)
	{
		this.setDiagnosticosRelacionados("numRegistros", numRegistros);
	}
	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}
	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
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
	 * @return the existeEgreso
	 */
	public boolean isExisteEgreso() {
		return existeEgreso;
	}
	/**
	 * @param existeEgreso the existeEgreso to set
	 */
	public void setExisteEgreso(boolean existeEgreso) {
		this.existeEgreso = existeEgreso;
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
	 * @return the numeroAutorizacion
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
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
	 * @return the codEspecialidadProfesionalResponde
	 */
	public String getCodEspecialidadProfesionalResponde() {
		return codEspecialidadProfesionalResponde;
	}
	/**
	 * @param codEspecialidadProfesionalResponde the codEspecialidadProfesionalResponde to set
	 */
	public void setCodEspecialidadProfesionalResponde(
			String codEspecialidadProfesionalResponde) {
		this.codEspecialidadProfesionalResponde = codEspecialidadProfesionalResponde;
	}
	
	/**
	 * Método que pone o elimina el estado del paciente "en valoracion";
	 * @param con
	 * @param paciente
	 * @param estaEnValoracion (true --> Pone en valoracion, false --> Elimina en valoracion)
	 * @param usuario Usuario que tiene bloqueado el paciente 
	 * @param sessionId Id de la sesión
	 */
	public static void cambiarPacienteEnEvaloracion(Connection con, int paciente, boolean estaEnValoracion, String usuario, String sessionId)
	{
		valoracionDao().ponerPacienteEnEvaloracion(con, paciente, estaEnValoracion, usuario, sessionId);
	}
	
	/**
	 * Método para cancelar todos los pacientes que se encuentren en valoración
	 * @param con
	 * @return numero de pacientes cancelados
	 */
	public static int cancelarPacientesEnEvaloracion(Connection con) {
		return valoracionDao().cancelarPacientesEnEvaloracion(con);
	}
	
	/**
	 * 
	 */
	public static boolean actualizarPoolSolicitudXAgenda(Connection con, int solicitud, int pool)
	{
		return valoracionDao().actualizarPoolSolicitudXAgenda(con,solicitud,pool);
	}
	
	
	

	/**
	 * @return the estadoEmbarazada
	 */
	public Boolean getEstadoEmbarazada() {
		return estadoEmbarazada;
	}
	/**
	 * @param estadoEmbarazada the estadoEmbarazada to set
	 */
	public void setEstadoEmbarazada(Boolean estadoEmbarazada) {
		this.estadoEmbarazada = estadoEmbarazada;
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
	 * @return the usuarioProfesionalSalud
	 */
	public String getUsuarioProfesionalSalud() {
		return usuarioProfesionalSalud;
	}
	/**
	 * @param usuarioProfesionalSalud the usuarioProfesionalSalud to set
	 */
	public void setUsuarioProfesionalSalud(String usuarioProfesionalSalud) {
		this.usuarioProfesionalSalud = usuarioProfesionalSalud;
	}
	
	public int obtenerNumeroSolicitudPrimeraValoracion (Connection con, int idCuenta)
	{
		try {
			return UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con,idCuenta);
		} catch (SQLException e) {
			logger.error("Error en obtenerNumeroSolicitudPrimeraValoracion de Valoraciones: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	public DtoValoracionUrgencias cargarDiagnosticosValoracion(Connection con,String numeroSolicitud)
	{
		return valoracionDao.cargarDiagnosticosValoracion(con, numeroSolicitud);
	}
	public DtoValoracionHospitalizacion cargarHospitalizacion(Connection con,String numeroSolicitud)
	{
		return valoracionDao.cargarHospitalizacion(con, numeroSolicitud);
	}
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que retorna una lista de las observaciones para ser comparadas
	 * @param con
	 * @param numeroSolicitud
	 * @return List
	 */

	public List<DtoValoracion> cargarObservacionesVistaValoracion(Connection con,int numeroSolicitud) {
		List<DtoValoracion> listObservaciones = new ArrayList<DtoValoracion>();
		List<DtoValoracion> listObservacion = new ArrayList<DtoValoracion>();
		Valoraciones valoraciones = new Valoraciones();
		Date fechaHoraOb=null;
		
	        try {
	        	 listObservaciones = Valoraciones.valoracionDao.cargarObservacionesVistaValoracion(con,String.valueOf(numeroSolicitud));
			       for (DtoValoracion dtoConductaObservaciones:listObservaciones) {
			       	   fechaHoraOb = valoraciones.fechaHoraVistaValoracion(dtoConductaObservaciones.getFechaValoracionObservacion(),dtoConductaObservaciones.getHoraValoracionObservacion());
			       	   dtoConductaObservaciones.setFechaHora(fechaHoraOb);
			    	   listObservacion.add(dtoConductaObservaciones);   
			       }
	        } catch (Exception e) {
	    	  logger.error("Exception: en el metodo cargarObservacionesVistaValoracion " + e);
	    	  e.printStackTrace();
            }
		return listObservacion;
	}
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que ordena las listas de valoraciones observaciones y urgencias
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param Solicitud
	 * @return List
	 */
	
	public List <DtoValoracion>obtenerValoracionesOrdenada(Connection con,UsuarioBasico usuario, PersonaBasica paciente,String Solicitud) {
		
		    int numeroSolicitud = Integer.parseInt(Solicitud);	
			String CadenaValoracion = null;
			List<DtoValoracion>listaOrdenada = new ArrayList<DtoValoracion>();
			List<DtoValoracion>listaObservaciones = new ArrayList<DtoValoracion>();
			List<DtoValoracion>listaUrgencias = new ArrayList<DtoValoracion>();
			Valoraciones valoraciones = new Valoraciones();
			List<DtoValoracion>cambiarVistaformatoFecha = new ArrayList<DtoValoracion>();
			
		    try {
			    listaObservaciones = valoraciones.cargarObservacionesVistaValoracion(con,numeroSolicitud);
		        CadenaValoracion = cadenaRegistroConductaValoracion(con,numeroSolicitud);
			    listaUrgencias = cargarVistaValoracionConductasUrgencias(CadenaValoracion);
   			    listaOrdenada = obtenerValoracionesComparar(listaUrgencias,listaObservaciones);
   			    cambiarVistaformatoFecha = cambiarVistaformatoFecha(listaOrdenada);
		    } catch (Exception e) {
		    	  logger.error("Exception: en  el metodo ObtenerValoracionesOrdenada " + e);
	              e.printStackTrace();
		    }
		return cambiarVistaformatoFecha;
	}
	
	
	
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que compara las listas de valoraciones observaciones urgencias
	 * @param listaValorUrgencias
	 * @param listaValorObservaciones
	 * @return List
	 */
	
	   public List <DtoValoracion>obtenerValoracionesComparar(List<DtoValoracion> listaValorUrgencias, List<DtoValoracion> listaValorObservaciones) {
		List<DtoValoracion>listaOrdenada = new ArrayList<DtoValoracion>();
		                   try {
		                	   listaOrdenada.addAll(listaValorUrgencias);
		                	   listaOrdenada.addAll(listaValorObservaciones);
		            		   SortGenerico sortG=new SortGenerico("FechaHora",true);
		            		  Collections.sort(listaOrdenada, sortG);
	
				           } catch (Exception e) {
						    	  logger.error("Exception: al iterar listaOrdenada de ObtenerValoracionesComparar " + e);
						    	  e.printStackTrace();
			               }
	   return listaOrdenada;
	   }
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * Método para extraer del registro el string de base de datos
	 * de vistaValoración
	 * @param CadenaValoracionDescripcion
	 * @return List
	 */
	    
	public List<DtoValoracion>cargarVistaValoracionConductasUrgencias(String CadenaValoracionDescripcion) 
	{
		/*
		   Antes de mostrar la cadena de observaciones se debe interpretar la informacion que esta guardada, teniendo en cuenta
		   que las observaciones se guardan en el siguiente formato :
		   <fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>_&_<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>
		*/
		Valoraciones valoraciones = new Valoraciones();
		Date fechaHoraUr =null;
		List<DtoValoracion>listRegistroVistaValoracionUrgencias = new ArrayList<DtoValoracion>(); 
		//DtoValoracion dtoValoracionObservaciones = new DtoValoracion();
		
			String observaciones = CadenaValoracionDescripcion;
			StringBuffer fechaHora = new StringBuffer();
		
		if(!observaciones.equals("")) {
			//Se reemplaza el símbolo _&_ por @
			observaciones = observaciones.replaceAll("_&_", "@");
			StringTokenizer observacionesToken = new StringTokenizer(observaciones, "@");

		  while (observacionesToken.hasMoreTokens()) {
				
			  DtoValoracion dtoConductaObservaciones = new DtoValoracion();
			   
				//Se extrae cada resumen de conducta valoracion
				String elemento = observacionesToken.nextToken();
				StringTokenizer datosElemento = new StringTokenizer(elemento, "|");
		
				//Se toma la fecha de grabacion
				String fecha = "";
				if(datosElemento.hasMoreTokens()) {
					fecha = datosElemento.nextToken();
				}
					
				//SE toma la hora de grabacion
				String hora = "";
				if(datosElemento.hasMoreTokens()) {
					hora = datosElemento.nextToken();
				}
					
				//El medico va a tener las especialidades, formato:
				//[nombre_medico]:[especi1], [espe2]
				// Estos dos puntos aparecen en caso de que tenga especialidades
				String profesional = "";
				if (datosElemento.hasMoreTokens()) {
					profesional = datosElemento.nextToken();
				}
					
				//Se verifica si el médico tenía especialidades para insertarlas mejor
				String especialidades = "";
				if (profesional.indexOf(':') != -1) {
					especialidades = "Especialidades: " + profesional.substring(profesional.indexOf(':')+1);
					profesional = profesional.substring(0, profesional.indexOf(':'));
				}
				
				///El registro del medico incluye el texto 'RM' y 'MP' que indica si el numero es un Registro medico
				//o una matriculo profesional
				String registroMedico = "";
				if (datosElemento.hasMoreTokens()) {
					registroMedico = "- R.M: " +datosElemento.nextToken();
				}
					
				
				/*Se sacan las observaciones*/
				String observacion = "";
				if (datosElemento.hasMoreTokens()) {
					observacion = "Conducta a Seguir: " +datosElemento.nextToken();
				}
					
				 fechaHoraUr = valoraciones.fechaHoraVistaValoracion(fecha,hora);
		    	
				/*observacionesFinales.append(fecha).append(", ").append(hora).append("\n").append(observacion).append("\n").append(profesional).append(". ").
				 * append(registroMedico).append(especialidades).append(". ").append("\n\n"); */
				
			    try {
			    	dtoConductaObservaciones.setFechaValoracionUrgencias(fecha);
                    dtoConductaObservaciones.setHoraValoracionUrgencias(hora);
                    dtoConductaObservaciones.setEspecialidades(especialidades);
      		    	dtoConductaObservaciones.setProfesion(profesional);
   	    	        dtoConductaObservaciones.setRegistromedico(registroMedico);
                    dtoConductaObservaciones.setObservacion(observacion); 
                    dtoConductaObservaciones.setFechaHora(fechaHoraUr);
		            listRegistroVistaValoracionUrgencias.add(dtoConductaObservaciones);
			
		       	} catch (Exception e) {
		       			logger.error("Error en el metodo cargarVistaValoracionConductasUrgencias: "+e);
		       			e.printStackTrace();
		       	}
		    }
		}
		return listRegistroVistaValoracionUrgencias;
	}
	
	
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que retorna un date fecha y hora vistaObservacion
	 * @param fechaV
	 * @param horaV
	 * @return Date
	 */
	
	public Date fechaHoraVistaValoracion(String fechaV,String horaV) {
		
		Date fechaVal=null;
		try {
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
			fechaVal= dateTimeFormatter.parse(fechaV + ":" + horaV);				
		} catch (ParseException e) {
				logger.error("Error en el metodo fechaHoraVistaValoracion: "+e);
				e.printStackTrace();
		}			
		  return fechaVal;
	}
		
		/**
		 * Alberto Ovalle
	     * mt 5749
		 * @param List
		 */
	
		public void validarFechaObservaciones(List<DtoValoracion> listaValorObservaciones) {
			try {
		         for (int j=1;j<listaValorObservaciones.size();j++) {
			       DtoValoracion val1=listaValorObservaciones.get(j-1);
			       DtoValoracion val2=listaValorObservaciones.get(j);
			               if(j==1){
			            	   val1.setImprimeFechaHora(true);
			               }
		                    if (val1.getFechaHora().compareTo(val2.getFechaHora())!=0) {
		                    	val2.setImprimeFechaHora(true);
		                        		                    }             
		                  }
		      } catch (Exception e) {
		    	  logger.error("Error en el metodo validarFechaObservaciones: "+e);
		           e.printStackTrace();
		      }
		 }
		
		/**
		 * Alberto Ovalle
		 * mt 5749 
		 * @param ad_fecha
		 * @return String
		 * @throws NumberFormatException
		 */
		
		public String conversionFormatoFechaAAp(Date ad_fecha)throws NumberFormatException
		{
			StringBuffer lsb_fecha = new StringBuffer();
		  try {
			if(ad_fecha != null)
			{
				SimpleDateFormat lsdf_sdf;
				lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				lsdf_sdf.setLenient(false);
				lsb_fecha = lsdf_sdf.format(ad_fecha, lsb_fecha, new FieldPosition(DateFormat.YEAR_FIELD) );
			}
		  } catch (Exception e) {
			  logger.error("Error en el metodo conversionFormatoFechaAAp: "+e);
			  e.printStackTrace();
		  }
			return lsb_fecha.toString();
		}
		
		/**
		 * Alberto Ovalle
		 * mt 5749 
		 * metodo para obtener la cadena registro Conducta valoracion
		 * @param con
		 * @param numeroSolicitud2
		 * @return String
		 */
		
		public String cadenaRegistroConductaValoracion(Connection con,int numeroSolicitud2) {
			String CadenaValoracionDescripcion=null;
			
			try {
				CadenaValoracionDescripcion = Valoraciones.valoracionDao.selecionarCadenaRegistroConductaValoracion(con,String.valueOf(numeroSolicitud2));
			}
			catch(Exception e){
			   logger.error("Exception: en el metodo CadenaRegistroConductaValoracion " + e);
			   e.printStackTrace();
		    }
		  return CadenaValoracionDescripcion;
		}
		
		/**
		 * Alberto Ovalle
		 * mt 5749 
		 * metodo para cambiar vista formato fecha
		 * @param listaOrdenada
		 * @return List
		 */
		
		public List<DtoValoracion> cambiarVistaformatoFecha(List<DtoValoracion>listaOrdenada) {
			List<DtoValoracion> list =null;
			try {	
				list = new ArrayList<DtoValoracion>();
				for(DtoValoracion dtoValoracion : listaOrdenada) {
			    dtoValoracion.setFormatoFechaHora(conversionFormatoFechaAAp(dtoValoracion.getFechaHora()));
				list.add(dtoValoracion);
				}
			} catch(Exception e) {
				 logger.error("Exception: en el metodo cambiarVistaformatoFecha " + e);
			  e.printStackTrace();
			}
			return list;		
		}
		
		/**
		 * Alberto Ovalle
		 * mt 5749
		 * metodo para modificar Observaciones Urgencias
		 * @param con
		 * @param dtoValoracionObservaciones
		 * 
		 */
		
		public void modificarObsevaciones(Connection con, DtoValoracionObservaciones dtoValoracionObservaciones) {
		
			String fechaActual = UtilidadFecha.getFechaActual(con);
            String horaActual = UtilidadFecha.getHoraActual(con);
            Valoraciones.valoracionDao.modificarObservaciones(con,dtoValoracionObservaciones,this.valoracionUrgencias,fechaActual,horaActual);
		
		}
		
//		/**
//		 * Alberto Ovalle
//		 * mt 5749
//		 * metodo para modificar Observaciones Hospitalizacion
//		 * @param con
//		 * @param dtoValoracionObservaciones
//		 */
//		
//		public void modificarObsevacionesHospitalizacion(Connection con, DtoValoracionObservaciones dtoValoracionObservaciones) {
//		
//			String fechaActual = UtilidadFecha.getFechaActual(con);
//            String horaActual = UtilidadFecha.getHoraActual(con);
//            Valoraciones.valoracionDao.modificarObsevacionesHospitalizacion(con,dtoValoracionObservaciones,this.valoracionHospitalizacion,fechaActual,horaActual);
//		
//		}	
		
		
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que trae las valoraciones observaciones 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param Solicitud
	 * @return List
	 */
	
	public List <DtoValoracion>obtenerValoracionesObservacion(Connection con,UsuarioBasico usuario, PersonaBasica paciente,String Solicitud) {
		
		    int numeroSolicitud = Integer.parseInt(Solicitud);	
			List<DtoValoracion>listaObservaciones = new ArrayList<DtoValoracion>();
			Valoraciones valoraciones = new Valoraciones();
			List<DtoValoracion>cambiarVistaformatoFecha = new ArrayList<DtoValoracion>();
			
		    try {
			    listaObservaciones = valoraciones.cargarObservacionesVista(con,numeroSolicitud);
		        cambiarVistaformatoFecha = cambiarVistaformatoFecha(listaObservaciones);
		    } catch (Exception e) {
		    	  logger.error("Exception: en  el metodo obtenerValoracionesObservacion " + e);
	              e.printStackTrace();
		    }
		return cambiarVistaformatoFecha;
	}
		
	/**
	 * Alberto Ovalle
	 * mt 5749
	 * metodo que retorna una lista de las observaciones 
	 * @param con
	 * @param numeroSolicitud
	 * @return List
	 */

	public List<DtoValoracion> cargarObservacionesVista(Connection con,int numeroSolicitud) {
		
		List<DtoValoracion> listObservaciones = new ArrayList<DtoValoracion>();
		List<DtoValoracion> listObservacion = new ArrayList<DtoValoracion>();
		Valoraciones valoraciones = new Valoraciones();
		Date fechaHoraOb=null;
		
	        try {
	        	 listObservaciones = Valoraciones.valoracionDao.cargarObservacionesVistaValoracion(con,String.valueOf(numeroSolicitud));
			       for (DtoValoracion dtoConductaObservaciones:listObservaciones) {
			       	   fechaHoraOb = valoraciones.fechaHoraVistaValoracion(dtoConductaObservaciones.getFechaValoracionObservacion(),dtoConductaObservaciones.getHoraValoracionObservacion());
			       	   dtoConductaObservaciones.setFechaHora(fechaHoraOb);
			    	   listObservacion.add(dtoConductaObservaciones);   
			       }
	        } catch (Exception e) {
	    	  logger.error("Exception: en el metodo cargarObservacionesVistaObservaciones " + e);
	    	  e.printStackTrace();
            }
		return listObservacion;
	}	
}
