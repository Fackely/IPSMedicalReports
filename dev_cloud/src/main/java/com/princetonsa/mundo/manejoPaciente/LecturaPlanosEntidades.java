/*
 * Enero 2, 2008
 */
package com.princetonsa.mundo.manejoPaciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.ConstantesBDHistoriaClinica;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.LecturaPlanosEntidadesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.LecturaPlanosEntidadesDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoLogLecturaPlanosEntidades;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.MontosCobro;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.GeneracionExcepcionesFarmacia;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Lectura de Planos Pacientes Entidades Subcontratadas
 */
public class LecturaPlanosEntidades 
{
	private static Logger logger = Logger.getLogger(LecturaPlanosEntidades.class);
	/**
	 * DAO para el manejo de LecturaPlanosEntidades
	 */
	LecturaPlanosEntidadesDao lecturaDao = null;
	
	//***************ATRIBUTOS********************************************************************
	//Campos que tienen que ver con el manejo de los archivos
	private String ubicacionPlanosEntidadesSubcontratadas;
	private String directorioArchivos;
	private ArrayList<File> archivos = new ArrayList<File>();
	
	//Campos que tienen que ver con los parámetros del proceso
	private Connection con;
	private int codigoManual;
	private String consecutivoEntidadSubcontratada;
	private String numeroFactura;
	private String validacionNumeroCarnet;
	private UsuarioBasico usuarioSession;
	
	//Campos que se usan durante la ejecución del proceso
	private String numeroRemision;
	private int numeroIngresosRegistrados; //para reportar el número de ingresos registrados
	private DtoLogLecturaPlanosEntidades logLectura; //para almacenar la información del log de ejecucion
	
	//Edicion de errores, indicadores e inconsistencias
	private ActionErrors errores = new ActionErrors();
	private boolean huboInconsistencias;
	private boolean huboInconsistenciaXRegistro; //avisa sobre la presencia de una inconsistencia en un registro
	private boolean cancelarProceso;
	private ArrayList<HashMap<String, Object>> inconsistencias = new ArrayList<HashMap<String,Object>>();
	private String pathArchivoInconsistencias;
	private ArrayList<String[]> archivoCT = new ArrayList<String[]>();
	private HashMap<String, Object> totalRegistros = new HashMap<String, Object>();
	
	//Atributos para el flujo de registro de información
	private HashMap<String, Object> listadoFacturas = new HashMap<String, Object>(); 
	private int tipoCie; //almacena el tipo cie actual
	private String nombreTipoCie;
	private String numeroFacturaCiclo; //identifica a  la factura tomada en una iteracion del listado de facturas
	private String tipoIdentificacionCiclo; //identifica el tipo de id del paciente de la factura tomada en la iteracion
	private String numeroIdentificacionCiclo; //identifica el nro id del paciente de la factuta tomada en la iteracion
	private String fechaExpedicionCiclo; //identifica la fecha de expedicion de la factura tomada de la iteración
	private String idCuentaCiclo; //identifica el id de la cuenta de ka factura tomada de la iteracion
	private String idIngresoCiclo; //identifica el id del ingreso de la factura tomada de la iteración
	private int codigoAreaCiclo; //identifica el área de la cuenta de la factura tomada de la iteracion
	private String nombreFarmaciaCiclo; //identifica el nombre de la farmacia de la factura tomada de la iteracion
	private int codigoConvenioCiclo; //identifica el convenio de la factura tomada de la iteracion
	private int codigoPacienteCiclo; //identifica el paciente de la factura tomada de la iteracion
	private String consecutivoRegistroPacEntidadSub; //identifica el consecutivo del registro de paciente entidad subcontratada de la iteracion
	//**********************************************************************************************
	//****************INICIALIZADORES & CONSTRUCTORES***********************************************
	/**
	 * Constructor
	 */
	public LecturaPlanosEntidades()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.ubicacionPlanosEntidadesSubcontratadas = "";
		this.directorioArchivos = "";
		this.archivos = new ArrayList<File>();
		
		this.con = null;
		this.codigoManual = ConstantesBD.codigoNuncaValido;
		this.consecutivoEntidadSubcontratada = "";
		this.numeroFactura = "";
		this.validacionNumeroCarnet = "";
		this.usuarioSession = new UsuarioBasico();
		
		this.numeroRemision = "";
		this.numeroIngresosRegistrados = 0;
		this.logLectura = new DtoLogLecturaPlanosEntidades();
		
		this.errores = new ActionErrors();
		this.huboInconsistencias = false;
		this.huboInconsistenciaXRegistro = false;
		this.cancelarProceso = false;
		this.inconsistencias = new ArrayList<HashMap<String,Object>>();
		this.pathArchivoInconsistencias = "";
		
		
		//Atributos del proceso de registro de información
		this.listadoFacturas = new HashMap<String, Object>();
		this.tipoCie = ConstantesBD.codigoNuncaValido;
		this.nombreTipoCie = "";
		this.numeroFacturaCiclo = "";
		this.tipoIdentificacionCiclo = "";
		this.numeroIdentificacionCiclo = "";
		this.fechaExpedicionCiclo = "";
		this.idCuentaCiclo = "";
		this.codigoAreaCiclo = ConstantesBD.codigoNuncaValido;
		this.nombreFarmaciaCiclo = "";
		this.codigoConvenioCiclo = ConstantesBD.codigoNuncaValido;
		this.codigoPacienteCiclo = ConstantesBD.codigoNuncaValido;
		this.consecutivoRegistroPacEntidadSub = "";
		
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (lecturaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			lecturaDao = myFactory.getLecturaPlanosEntidadesDao();
		}	
	}
	//*****************************************************************************************************
	//**************************METODOS******************************************************************
	/**
	 * Método principal que realiza la lectura de los archivos planos
	 * @param usuario 
	 */
	public void ejecutar(Connection con,LecturaPlanosEntidadesForm forma, UsuarioBasico usuario) throws IPSException
	{
		this.llenarMundo(con,forma,usuario);
		
		//antes de comenzar se eliminan todos los registros ingresados en base de datos
		lecturaDao.eliminarRegistrosRips(con,usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
		
		//1) *****************VALIDACIONES DE LOS TIPOS DE ARCHIVOS************************************
		this.validacionesTiposArchivos();
		//**********************************************************************************************
		
		//Si no hubo inconsistencias se continúa
		if(!this.huboInconsistencias)
		{
			//2) ***************VALIDACIONES GENERALES**********************************************
			// - Validaciones generales en la información de los campos de los archivos.
			// - Validaciones estructura archivo de control y validación de información que reporta versus los demás archivos
			// - Validaciones estructura de los demás archivos reportados
			this.validacionesGeneralesEstructuraArchivos();
			//***************************************************************************************
			
			//Se verifica si se debe cancelar el proceso (por inconsistencias graves)
			if(!this.cancelarProceso)
			{
				logger.info("---------------PASÓ POR REGISTRO DE LA INFORMACIÓN---------------------------");
				//3) **********REGISTRO DE LA INFORMACION*******************************************
				this.registroInformacion();
				//***********************************************************************************
				
				if(this.huboInconsistencias)
					this.generarArchivoInconsistencias(con);
			}
			else
				this.generarArchivoInconsistencias(con);
		}
		else
			this.generarArchivoInconsistencias(con);
		
		//Al final se eliminan todos los registros ingresados en base de datos
		lecturaDao.eliminarRegistrosRips(con,usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
		//Se registra el log de ejecucion del proceso
		lecturaDao.insertarLogEjecucionProceso(con, this.logLectura);
	}
	
	/**
	 * Método que realiza el registro de la información
	 *
	 */
	private void registroInformacion() throws IPSException 
	{
		HashMap<String, Object> pacientes = new HashMap<String, Object>();
		//Se consultan las facturas leídas de los planos RIPS
		this.listadoFacturas = lecturaDao.cargarListadoFacturas(con,usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
		this.tipoCie = Utilidades.codigoCieActual(con); //se toma el tipo cie actual
		this.nombreTipoCie = UtilidadesManejoPaciente.obtenerDescripcionTipoCie(con, this.tipoCie);
		boolean exito = false;
		
		logger.info("NÚMERO DE FACTURAS ENCONTRADAS=> "+Integer.parseInt(this.listadoFacturas.get("numRegistros").toString()));
		for(int i=0;i<Integer.parseInt(this.listadoFacturas.get("numRegistros").toString());i++)
		{
			//Se toma factura y fecha de expedicion de la iteracion
			this.numeroFacturaCiclo = this.listadoFacturas.get("numeroFactura_"+i).toString();
			this.fechaExpedicionCiclo = this.listadoFacturas.get("fechaExpedicion_"+i).toString().trim();
			
			//Se consultan los pacientes de la factura
			pacientes = lecturaDao.consultarPacientesFactura(con, this.numeroFacturaCiclo,usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
			
			logger.info("NUMERO DE PACIENTES ENTONCTRADOS DE LA FACTURA=> "+Integer.parseInt(pacientes.get("numRegistros").toString()));
			//Iteración de cada paciente encontrado dentro de la factura
			for(int j=0;j<Integer.parseInt(pacientes.get("numRegistros").toString());j++)
			{
				//Se toman los datos del paciente en la iteracion
				this.tipoIdentificacionCiclo = pacientes.get("tipoIdentificacion_"+j).toString();
				this.numeroIdentificacionCiclo = pacientes.get("numeroIdentificacion_"+j).toString();
				
				
				UtilidadBD.iniciarTransaccion(con);
				//***********CREACIÓN INGRESO,CUENTA Y/O EGRESO*********************************************
				exito = this.crearIngresoCuentaEgreso();
				//******************************************************************************************
				
				//**********GENERACIÓN DE LOS CARGOS DIRECTOS***********************************************
				if(exito)
					exito = this.generarCargosDirectos();
				//*******************************************************************************************
				
				//*********REGISTRO DE INFORMACION DEL PARTO**********************************************
				if(exito)
					exito = this.registrarInformacionParto();
				//*****************************************************************************************
				
				//***********FINALIZACION DE EL REGISTRO PACIENTE ENTIDAD SUBCONTRATADA******************
				if(exito)
					exito = this.finalizarRegistroPacienteEntidadSubcontratada();
				//****************************************************************************************
				
				if(exito)
				{
					UtilidadBD.finalizarTransaccion(con);
					this.numeroIngresosRegistrados++;
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					this.huboInconsistencias = true;
				}
			}
		}
	}
	
	
	/**
	 * Método implementado para finalizar 
	 * @param con
	 * @param campos
	 * @return
	 */
	private boolean finalizarRegistroPacienteEntidadSubcontratada()
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",this.idIngresoCiclo);
		campos.put("usuario",usuarioSession.getLoginUsuario());
		logger.info("consecutivo paciente entidad subcintratada => "+this.consecutivoRegistroPacEntidadSub);
		campos.put("consecutivo",this.consecutivoRegistroPacEntidadSub);
		return lecturaDao.finalizarRegistroPacienteEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método implementado para registrar la información del parto
	 * @return
	 */
	private boolean registrarInformacionParto() 
	{
		boolean exito = true, puedoIngresar = true;
		//Se consulta la información del archivo AN
		HashMap<String, Object> datosAN = consultarInformacionArchivo(con, ConstantesBD.ripsAN, true);
		
		//DTO para ingresar la información del parto
		DtoInformacionParto infoParto = new DtoInformacionParto();
		infoParto.setCodigoIngreso(this.idIngresoCiclo);
		infoParto.setCodigoPaciente(this.codigoPacienteCiclo);
		infoParto.setLoginUsuario(usuarioSession.getLoginUsuario());
		infoParto.setInstitucion(usuarioSession.getCodigoInstitucionInt());
		infoParto.setFinalizado(true);
		
		//DTO para ingresar la información de recién nacido
		ArrayList<DtoInformacionRecienNacido> arregloRecienNacido = new ArrayList<DtoInformacionRecienNacido>();
		
		for(int i=0;i<Utilidades.convertirAEntero(datosAN.get("numRegistros").toString(), true);i++)
		{
			//Se llena la informacion del parto
			if(i==0)
			{
				infoParto.setSemanasGestacional(Integer.parseInt(datosAN.get("edadGestacional_"+i).toString()));
				infoParto.setControlPrenatal(datosAN.get("controlPrenatal_"+i).toString().equals("1")?true:false);
			}
			
			//Se llena la informacion de cada recién nacido
			DtoInformacionRecienNacido infoRecienNacido = new DtoInformacionRecienNacido();
			
			infoRecienNacido.setCodigoIngreso(this.idIngresoCiclo);
			infoRecienNacido.setConsecutivoHijo(i+1);
			infoRecienNacido.setFechaNacimiento(datosAN.get("fechaNacimiento_"+i).toString());
			infoRecienNacido.setHoraNacimiento(datosAN.get("horaNacimiento_"+i).toString());
			infoRecienNacido.setCodigoSexo(datosAN.get("sexo_"+i).toString().equals("M")?ConstantesBD.codigoSexoMasculino:ConstantesBD.codigoSexoFemenino);
			infoRecienNacido.setVivo(datosAN.get("causaMuerte_"+i).toString().equals("")?true:false);
			if(!datosAN.get("diagnostico_"+i).toString().equals(""))
			{
				infoRecienNacido.setAcronimoDiagnosticoRecienNacido(datosAN.get("diagnostico_"+i).toString().toUpperCase());
				infoRecienNacido.setCieDiagnosticoRecienNacido(this.tipoCie);
				
				//Se verifica que el diagnostico exista
				if(!validarDiagnostico(infoRecienNacido.getAcronimoDiagnosticoRecienNacido(), ConstantesBD.ripsAN, "diagnóstico recién nacido"))
					puedoIngresar = false;
					
			}
			infoRecienNacido.setFechaMuerte(datosAN.get("fechaMuerte_"+i).toString());
			infoRecienNacido.setHoraMuerte(datosAN.get("horaMuerte_"+i).toString());
			if(!datosAN.get("causaMuerte_"+i).toString().equals(""))
			{
				infoRecienNacido.setAcronimoDiagnosticoMuerte(datosAN.get("causaMuerte_"+i).toString().toUpperCase());
				infoRecienNacido.setCieDiagnosticoMuerte(this.tipoCie);
				
				//Se verifica que el diagnostico exista
				if(!validarDiagnostico(infoRecienNacido.getAcronimoDiagnosticoMuerte(), ConstantesBD.ripsAN, "diagnóstico muerte"))
					puedoIngresar = false;
			}
			infoRecienNacido.setPesoEgreso(Integer.parseInt(datosAN.get("peso_"+i).toString()));
			infoRecienNacido.setLoginUsuarioProceso(usuarioSession.getLoginUsuario());
			infoRecienNacido.setFinalizada(true);
			
			arregloRecienNacido.add(infoRecienNacido);
			
		}
		
		if(arregloRecienNacido.size()>0)
		{
			if(puedoIngresar)
			{
				exito = UtilidadesHistoriaClinica.insertarInformacionPartoParaRips(con, infoParto, arregloRecienNacido);
				
				if(!exito)
				{
					editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"error al registrar la información de recién nacidos");
				}
			}
			else
				exito = false;
		}
		
		return exito;
	}
	/**
	 * Método que realiza la generación de los cargos directos de la factura
	 * @return
	 */
	private boolean generarCargosDirectos() throws IPSException 
	{
		boolean exito = true;
		boolean[] vectorExito = new boolean[4];
		
		//Se consulta la información de cada archivo
		HashMap<String, Object> datosAP = consultarInformacionArchivo(con, ConstantesBD.ripsAP, true);
		HashMap<String, Object> datosAM = consultarInformacionArchivo(con, ConstantesBD.ripsAM, true);
		HashMap<String, Object> datosAT = consultarInformacionArchivo(con, ConstantesBD.ripsAT, true);
		HashMap<String, Object> datosAC = consultarInformacionArchivo(con, ConstantesBD.ripsAC, true);
		
		HashMap<String,Object> datosParametros = this.cargarParametrosCargosDirectos(datosAP,datosAM,datosAT,datosAC);
		
		//Se verifica éxito de la consulta de parámetros generales de cargos directos
		exito = UtilidadTexto.getBoolean(datosParametros.get("procesoExitoso").toString());
		
		
		//Si todo ha sido exitoso hasta ahora se continúa
		if(exito)
		{
			//*******************GENERACION DE LOS CARGOS AP*********************************************
			vectorExito[0] = generarCargosAP(datosAP,datosParametros);
			//********************************************************************************************
			//*******************GENERACION DE LOS CARGOS AM*********************************************
			vectorExito[1] = generarCargosAM(datosAM,datosParametros);
			//********************************************************************************************
			//*******************GENERACION DE LOS CARGOS AT*********************************************
			vectorExito[2] = generarCargosAT(datosAT,datosParametros);
			//********************************************************************************************
			//*******************GENERACION DE LOS CARGOS AT*********************************************
			vectorExito[3] = generarCargosAC(datosAC,datosParametros);
			//********************************************************************************************
		}
		
		//Se revisa el éxito de la generación de cargos
		for(int i=0;i<vectorExito.length;i++)
			if(!vectorExito[i])
				exito = false;
		
		return exito;
	}
	
	/**
	 * Método implementado para la generación de cargos del archivo AC
	 * @param datosAC
	 * @param datosParametros
	 * @return
	 */
	private boolean generarCargosAC(HashMap<String, Object> datosAC, HashMap<String, Object> datosParametros) throws IPSException 
	{
		boolean exito = true;
		int numeroSolicitud = 0, codigoServicio = ConstantesBD.codigoNuncaValido, codigoCentroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		UsuarioBasico user=new UsuarioBasico();
		
		for(int i=0;i<Utilidades.convertirAEntero(datosAC.get("numRegistros").toString(), true);i++)
		{
			//1) Consultar el servicio del cargo
			codigoServicio = consultarServicioCargo(datosAC.get("codigoConsulta_"+i).toString());
			
			if(codigoServicio>0)
			{
				//2) Consultar el centro de costo que ejecuta
				codigoCentroCostoEjecuta = consultarCentroCostoEjecuta(codigoServicio,datosAC.get("codigoConsulta_"+i).toString(),ConstantesBD.ripsAC);
				
				if(codigoCentroCostoEjecuta>0)
				{
					//3) Se inserta la solicitud general
					numeroSolicitud = insertarSolicitudGeneral(
						con, 
						datosAC.get("fechaConsulta_"+i).toString(), 
						ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, 
						codigoCentroCostoEjecuta,
						/*datosAC.get("numeroAutorizacion_"+i).toString(),*/
						Integer.parseInt(datosParametros.get(ConstantesIntegridadDominio.acronimoProfecionalResponde).toString()));
					
					if(numeroSolicitud>0)
					{
						//4) Se inserta la información del cargo directo
						boolean insertoCargoDirecto = insertarCargoDirecto(
							numeroSolicitud, 
							codigoServicio, 
							datosAC.get("fechaConsulta_"+i).toString(), 
							Integer.parseInt(datosAC.get("causaExterna_"+i).toString()), 
							datosAC.get("finalidadConsulta_"+i).toString().equals("")?ConstantesBDHistoriaClinica.codigoFinalidadConsultaNoAplica:datosAC.get("finalidadConsulta_"+i).toString(),  
							ConstantesBD.codigoNuncaValido, // No aplica finalidad procedimiento 
							ConstantesBD.codigoNuncaValido, // No aplica personal atiende
							ConstantesBD.codigoNuncaValido, // No aplica forma realizacion 
							datosAC.get("codigoDiagPpal_"+i).toString(),
							Integer.parseInt(datosAC.get("tipoDiagnostico_"+i).toString()), 
							datosAC.get("codigoDiagRel1_"+i).toString(), 
							datosAC.get("codigoDiagRel2_"+i).toString(),
							datosAC.get("codigoDiagRel3_"+i).toString(),
							"", // No aplica diagnostico de complicacion 
							true,
							ConstantesBD.ripsAC,
							datosAC.get("codigoConsulta_"+i).toString());
						
						if(insertoCargoDirecto)
						{
							//5) Se registra la tarifa del servicio
						    double valorTarifaOpcional= ConstantesBD.codigoNuncaValidoDouble;
						    
						    //Se pregunta por el tipo de tarifa
						    if(datosParametros.get(ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos).toString().equals(ConstantesIntegridadDominio.acronimoValorArchivoPlano))
						    	valorTarifaOpcional = Double.parseDouble(datosAC.get("valorConsulta_"+i).toString());
						    
						    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
						    Cargos cargos= new Cargos();
						    logger.info("***************TARIFA OPCIONAL--------->"+valorTarifaOpcional);
						    //Se simula el paciente pues solo se necesita el codigo del ingreso
						  
						    boolean insertoCargo= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
								usuarioSession,
								Integer.parseInt(this.idCuentaCiclo),
								Integer.parseInt(this.idIngresoCiclo),
								false/*dejarPendiente*/, 
								numeroSolicitud, 
								ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
								codigoCentroCostoEjecuta/*codigoCentroCostoEjecutaOPCIONAL*/, 
								codigoServicio/*codigoServicioOPCIONAL*/, 
								1/*cantidadServicioOPCIONAL*/, 
								valorTarifaOpcional/*valorTarifaOPCIONAL*/, 
								ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
								/* datosAC.get("numeroAutorizacion_"+i).toString() -- numeroAutorizacionOPCIONAL ,*/
								"" /*esPortatil*/,datosAC.get("fechaConsulta_"+i)+"");
						    
						    //Se verifica si se insertó el cargo
						    if(insertoCargo)
						    {
						    	//6) Validación de la autorización de servicios
						    	if(esValidoAutorizacionesServicio(
						    		codigoServicio, 
						    		consultarCantidadServicio(ConstantesBD.ripsAC, datosAC.get("codigoConsulta_"+i).toString()), 
						    		datosAC.get("numeroAutorizacion_"+i).toString()))
						    	{
						    		//7) Se actualiza la solicitud en la detalle del registro de entidades subcontratadas
						    		if(this.actualizarDetalleRegEntidadSubcontratada(numeroSolicitud, codigoServicio,datosAC.get("numeroAutorizacion_"+i).toString())<=0)
						    		{
						    			exito = false;
								    	editarInconsistenciaEncabezado(
											this.numeroFacturaCiclo, 
											this.tipoIdentificacionCiclo, 
											this.numeroIdentificacionCiclo, 
											ConstantesBD.ripsAC, 
											"--", 
											"error al actualizar el numero de solicitud del servicio "+datosAC.get("codigoConsulta_"+i)+" en el registro de entidad subcontratada");
						    		}
						    	}
						    	else
						    	{
						    		exito = false;
							    	editarInconsistenciaEncabezado(
										this.numeroFacturaCiclo, 
										this.tipoIdentificacionCiclo, 
										this.numeroIdentificacionCiclo, 
										ConstantesBD.ripsAC, 
										"--", 
										"la cantidad y numero de autorización del servicio "+datosAC.get("codigoConsulta_"+i)+" son erróneos");
						    	}
						    	
						    	if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoServicio, false, false, cargos.getDtoDetalleCargo().getCodigoConvenio()))
								{
									if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoServicio, 1, user.getLoginUsuario(), false, false, Utilidades.convertirAEntero(cargos.getDtoDetalleCargo().getCodigoSubcuenta()+""),""))
									{
										logger.error("No se logro insertar la Justificacion pendiente para el Servicio >> "+codigoServicio);
									}
								}
						    }
						    else
						    {
						    	exito = false;
						    	editarInconsistenciaEncabezado(
									this.numeroFacturaCiclo, 
									this.tipoIdentificacionCiclo, 
									this.numeroIdentificacionCiclo, 
									ConstantesBD.ripsAC, 
									"--", 
									"error al generar cargo de la solicitud del servicio "+datosAC.get("codigoConsulta_"+i));
						    }
						} //Fin if validacion insercion cargo directo
						else
							exito = false;
						
					} //Fin if validacion insercion solicitud general
					else
					{
						exito = false;
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								ConstantesBD.ripsAC, 
								"--", 
								"error al generar solicitud para el servicio "+datosAC.get("codigoConsulta_"+i));
						
					}
				} //fin if validacion centro costo ejecuta
				else
					exito = false;
			} //Fin if validacion servicio
			else
				exito = false;
				
		}
		
		return exito;
	}
	/**
	 * Método implementado para generar los cargos del archivo AT
	 * @param datosAM
	 * @param datosParametros
	 * @return
	 */
	private boolean generarCargosAT(HashMap<String, Object> datosAT, HashMap<String, Object> datosParametros) throws IPSException 
	{
		//************************************************************************************************
		//******************GENERACIÓN DE LOS CARGOS DE SERVICIOS AT**************************************
		//************************************************************************************************
		boolean exitoServicios = this.generacionCargosServiciosAT(datosAT,datosParametros);
		//************************************************************************************************
		//******************GENERACIÓN DE LOS CARGOS DE ARTICULOS AT**************************************
		//************************************************************************************************
		boolean exitoArticulos = this.generacionCargosArticulosAT(datosAT, datosParametros);
		//************************************************************************************************
		//***********************************************************************************************
		
		if(exitoServicios&&exitoArticulos)
			return true;
		else
			return false;
	}
	
	/**
	 * Método para generar los cargos de artículos del archivo AT
	 * @param datosAT
	 * @param datosParametros
	 * @return
	 */
	private boolean generacionCargosArticulosAT(HashMap<String, Object> datosAT, HashMap<String, Object> datosParametros) throws IPSException 
	{
		/**
		 * EN ESTE MÉTODO SE DEBEN AGRUPAR LOS ARTÍCULOS POR NUMERO DE AUTORIZACION PARA INGRESARLOS EN UNA SOLA
		 * SOLICITUD
		 */
		
		boolean exito = true;
		int codigoArticulo = ConstantesBD.codigoNuncaValido, numArticulos = 0;
		int numRegistros = Utilidades.convertirAEntero(datosAT.get("numRegistros").toString(), true);
		String numeroAutorizacion = "";
		HashMap<String, Object> articulos = new HashMap<String, Object>();
		
		for(int i=0;i<numRegistros;i++)
		{
			//Solo se toman los registros que no tengan codigoServicio y que correspondan al tipo servicio 'Materiales e insumos'
			if(datosAT.get("codigoServicio_"+i).toString().equals("")&&
				datosAT.get("tipoServicio_"+i).toString().equals("1"))
			{
				if(!numeroAutorizacion.equals("")&&
					!numeroAutorizacion.equals(datosAT.get("numeroAutorizacion_"+i).toString())&&
					numArticulos>0)
				{
					//Se genera la solicitud de cargo directos de artículos
					if(!generarSolicitudMedicamentos(/*numeroAutorizacion,*/articulos,datosParametros,ConstantesBD.ripsAT))
						exito = false;
					
					//Se reinicia la información 
					articulos = new HashMap<String, Object>();
					numArticulos = 0;
				}
				numeroAutorizacion = datosAT.get("numeroAutorizacion_"+i).toString();
				
				//Se consulta el código del articulo
				codigoArticulo = this.validacionCodigoArticulo(datosAT.get("nombreServicio_"+i).toString(), datosParametros,ConstantesBD.ripsAT);
				
				//Si el código es válido entonces se agrega al mapa
				if(codigoArticulo>0)
				{
					articulos.put("codigo_"+numArticulos,codigoArticulo);
					articulos.put("nombre_"+numArticulos,datosAT.get("nombreServicio_"+i).toString());
					articulos.put("cantidad_"+numArticulos,datosAT.get("cantidad_"+i));
					articulos.put("valorUnitario_"+numArticulos,datosAT.get("valorUnitario_"+i));
					articulos.put("valorTotal_"+numArticulos,datosAT.get("valorTotal_"+i));
					numArticulos++;
					articulos.put("numRegistros",numArticulos);
				}
				else
					exito = false;
			}
		}
		
		//Se hace por última vez para el último número de autorizacion
		if(numArticulos>0&&!generarSolicitudMedicamentos(/*numeroAutorizacion,*/articulos,datosParametros,ConstantesBD.ripsAT))
			exito = false;
		
		return exito;
	}
	/**
	 * Método implementado para la generación de cargos de servicios en el archivo AT
	 * @param datosAT
	 * @param datosParametros
	 * @return
	 */
	private boolean generacionCargosServiciosAT(HashMap<String, Object> datosAT, HashMap<String, Object> datosParametros) throws IPSException 
	{
		boolean exito = true;
		int numRegistros = Utilidades.convertirAEntero(datosAT.get("numRegistros").toString(), true), numeroSolicitud = ConstantesBD.codigoNuncaValido;
		UsuarioBasico user=new UsuarioBasico();
		
		int codigoServicio = ConstantesBD.codigoNuncaValido, codigoCentroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<numRegistros;i++)
		{
			//Solo se toman los registro que contengan codigo de procedimientos
			if(!datosAT.get("codigoServicio_"+i).toString().equals(""))
			{
				//1) Consultar el servicio del cargo
				codigoServicio = consultarServicioCargo(datosAT.get("codigoServicio_"+i).toString());
			
				if(codigoServicio>0)
				{
					//2) Consultar el centro de costo que ejecuta
					codigoCentroCostoEjecuta = consultarCentroCostoEjecuta(codigoServicio,datosAT.get("codigoServicio_"+i).toString(),ConstantesBD.ripsAT);
					
					if(codigoCentroCostoEjecuta>0)
					{
						//3) Se inserta la solicitud general
						numeroSolicitud = insertarSolicitudGeneral(
							con, 
							this.fechaExpedicionCiclo, 
							ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, 
							codigoCentroCostoEjecuta,
							/*datosAT.get("numeroAutorizacion_"+i).toString(),*/
							Integer.parseInt(datosParametros.get(ConstantesIntegridadDominio.acronimoProfecionalResponde).toString()));
						
						if(numeroSolicitud>0)
						{
							//4) Se inserta la información del cargo directo
							boolean insertoCargoDirecto = insertarCargoDirecto(
								numeroSolicitud, 
								codigoServicio, 
								"", //No aplica fecha procedimiento 
								ConstantesBD.codigoNuncaValido, //No aplica Causa Externa
								"", //No aplica finalidad consulta 
								ConstantesBD.codigoNuncaValido, //No palica finalidad procedimiento 
								ConstantesBD.codigoNuncaValido, //No aplica personal atiende
								ConstantesBD.codigoNuncaValido, //No aplica forma realizacion 
								"", // No aplica diagnostico principal
								ConstantesBD.codigoNuncaValido, //No aplica tipo diagnostico 
								"", "", "", // no aplica diagnosticos relacionados 
								"", false,ConstantesBD.ripsAT,datosAT.get("codigoServicio_"+i).toString()); // no viene información de rips
							
							if(insertoCargoDirecto)
							{
								//5) Se registra la tarifa del servicio
							    double valorTarifaOpcional= ConstantesBD.codigoNuncaValidoDouble;
							    
							    //Se pregunta por el tipo de tarifa
							    if(datosParametros.get(ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos).toString().equals(ConstantesIntegridadDominio.acronimoValorArchivoPlano))
							    	valorTarifaOpcional = Double.parseDouble(datosAT.get("valorUnitario_"+i).toString());
							    
							    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
							    Cargos cargos= new Cargos();
							    logger.info("***************TARIFA OPCIONAL--------->"+valorTarifaOpcional);
							    //Se simula el paciente pues solo se necesita el codigo del ingreso
							  
							    boolean insertoCargo= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
									usuarioSession,
									Integer.parseInt(this.idCuentaCiclo),
									Integer.parseInt(this.idIngresoCiclo),
									false/*dejarPendiente*/, 
									numeroSolicitud, 
									ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
									codigoCentroCostoEjecuta/*codigoCentroCostoEjecutaOPCIONAL*/, 
									codigoServicio/*codigoServicioOPCIONAL*/, 
									1/*cantidadServicioOPCIONAL*/, 
									valorTarifaOpcional/*valorTarifaOPCIONAL*/, 
									ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
									/*datosAT.get("numeroAutorizacion_"+i).toString() -- numeroAutorizacionOPCIONAL , */
									"" /*esPortatil*/,"");
							    
							    //Se verifica si se insertó el cargo
							    if(insertoCargo)
							    {
							    	//6) Validación de la autorización de servicios
							    	if(esValidoAutorizacionesServicio(
							    		codigoServicio, 
							    		consultarCantidadServicio(ConstantesBD.ripsAT, datosAT.get("codigoServicio_"+i).toString()), 
							    		datosAT.get("numeroAutorizacion_"+i).toString()))
							    	{
							    		//7) Se actualiza la solicitud en la detalle del registro de entidades subcontratadas
							    		if(this.actualizarDetalleRegEntidadSubcontratada(numeroSolicitud, codigoServicio,datosAT.get("numeroAutorizacion_"+i).toString())<=0)
							    		{
							    			exito = false;
									    	editarInconsistenciaEncabezado(
												this.numeroFacturaCiclo, 
												this.tipoIdentificacionCiclo, 
												this.numeroIdentificacionCiclo, 
												ConstantesBD.ripsAT, 
												"--", 
												"error al actualizar el numero de solicitud del servicio "+datosAT.get("codigoServicio_"+i)+" en el registro de entidad subcontratada");
							    		}
							    	}
							    	else
							    	{
							    		exito = false;
								    	editarInconsistenciaEncabezado(
											this.numeroFacturaCiclo, 
											this.tipoIdentificacionCiclo, 
											this.numeroIdentificacionCiclo, 
											ConstantesBD.ripsAT, 
											"--", 
											"la cantidad y numero de autorización del servicio "+datosAT.get("codigoServicio_"+i)+" son erróneos");
							    	}
							    }
							    else
							    {
							    	exito = false;
							    	editarInconsistenciaEncabezado(
										this.numeroFacturaCiclo, 
										this.tipoIdentificacionCiclo, 
										this.numeroIdentificacionCiclo, 
										ConstantesBD.ripsAT, 
										"--", 
										"error al generar cargo de la solicitud del servicio "+datosAT.get("codigoServicio_"+i));
							    }
							    if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoServicio, false, false, cargos.getDtoDetalleCargo().getCodigoConvenio()))
								{
									if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoServicio, 1, user.getLoginUsuario(), false, false, Utilidades.convertirAEntero(cargos.getDtoDetalleCargo().getCodigoSubcuenta()+""),""))
									{
										logger.error("No se logro insertar la Justificacion pendiente para el Servicio >> "+codigoServicio);
									}
								}
							} //Fin if validacion insercion cargo directo
							else
								exito = false;
							
						} //Fin if validacion insercion solicitud general
						else
						{
							exito = false;
							editarInconsistenciaEncabezado(
									this.numeroFacturaCiclo, 
									this.tipoIdentificacionCiclo, 
									this.numeroIdentificacionCiclo, 
									ConstantesBD.ripsAT, 
									"--", 
									"error al generar solicitud para el servicio "+datosAT.get("codigoProcedimiento_"+i));
							
						}
					} //fin if validacion centro costo ejecuta
					else
						exito = false;
				} //Fin if validacion servicio
				else
					exito = false;
				//---------------------------------------------------
			}
		}
		return exito;
	}
	/**
	 * Método implementado para generar los cargos del archivo AM
	 * @param datosAM
	 * @param datosParametros
	 * @return
	 */
	private boolean generarCargosAM(HashMap<String, Object> datosAM, HashMap<String, Object> datosParametros) throws IPSException 
	{
		/**
		 * EN ESTE MÉTODO SE DEBEN AGRUPAR LOS ARTÍCULOS POR NUMERO DE AUTORIZACION PARA INGRESARLOS EN UNA SOLA
		 * SOLICITUD
		 */
		
		boolean exito = true;
		int codigoArticulo = ConstantesBD.codigoNuncaValido, numArticulos = 0;
		int numRegistros = Utilidades.convertirAEntero(datosAM.get("numRegistros").toString(), true);
		//String numeroAutorizacion = "";
		HashMap<String, Object> articulos = new HashMap<String, Object>();
		
		for(int i=0;i<numRegistros;i++)
		{
			/*
			if(!numeroAutorizacion.equals("")&&
				!numeroAutorizacion.equals(datosAM.get("numeroAutorizacion_"+i).toString())&&
				numArticulos>0)
			{
				//Se genera la solicitud de cargo directos de artículos
				if(!generarSolicitudMedicamentos(numeroAutorizacion,articulos,datosParametros,ConstantesBD.ripsAM))
					exito = false;
				
				//Se reinicia la información 
				articulos = new HashMap<String, Object>();
				numArticulos = 0;
			}
			numeroAutorizacion = datosAM.get("numeroAutorizacion_"+i).toString();
			*/
			
			//Se consulta el código del articulo
			codigoArticulo = this.validacionCodigoArticulo(datosAM.get("nombreMedicamento_"+i).toString(), datosParametros,ConstantesBD.ripsAM);
			
			//Si el código es válido entonces se agrega al mapa
			if(codigoArticulo>0)
			{
				articulos.put("codigo_"+numArticulos,codigoArticulo);
				articulos.put("nombre_"+numArticulos,datosAM.get("nombreMedicamento_"+i).toString());
				articulos.put("cantidad_"+numArticulos,datosAM.get("numeroUnidades_"+i));
				articulos.put("valorUnitario_"+numArticulos,datosAM.get("valorUnitario_"+i));
				articulos.put("valorTotal_"+numArticulos,datosAM.get("valorTotal_"+i));
				numArticulos++;
				articulos.put("numRegistros",numArticulos);
			}
			else
				exito = false;
			
		}
		
		//Se hace por última vez para el último número de autorizacion
		if(numArticulos>0&&!generarSolicitudMedicamentos(/*numeroAutorizacion,*/articulos,datosParametros,ConstantesBD.ripsAM))
			exito = false;
		
		return exito;
	}
	
	/**
	 * Método implementado para generar la solicitud de medicamentos
	 * @param numeroAutorizacion
	 * @param articulos
	 * @param datosParametros
	 * @param tipoArchivo 
	 * @return
	 */
	private boolean generarSolicitudMedicamentos(/*String numeroAutorizacion, */HashMap<String, Object> articulos, HashMap<String, Object> datosParametros, String tipoArchivo) throws IPSException 
	{
		boolean exito = true;
		String afectaInventarios = datosParametros.get(ConstantesIntegridadDominio.acronimoAfectaInventariosCargosInventarios).toString();
		int codigoFarmacia = Utilidades.convertirAEntero(datosParametros.get(ConstantesIntegridadDominio.acronimoFarmaciaCargosDirectosArticulos)+"", true);
		int numArticulos = Utilidades.convertirAEntero(articulos.get("numRegistros")+"", true), codigoArticulo = 0, cantidadArticulo = 0;
		UsuarioBasico user=new UsuarioBasico();
		
		
		//1) Se inserta la solicitud general
		int numeroSolicitud = insertarSolicitudGeneral(
				con, 
				this.fechaExpedicionCiclo, 
				ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos, 
				codigoFarmacia,
				/*numeroAutorizacion,*/ 
				Integer.parseInt(datosParametros.get(ConstantesIntegridadDominio.acronimoProfecionalResponde).toString()));
		
		if(numeroSolicitud>0)
		{
			if(exito)
	    	{
				//*********SE INSERTA UNA SOLICITUD DE MEDICAMENTOS BÁSICA***************************************
				 SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
			    objetoSolicitudMedicamentos.setNumeroSolicitud(numeroSolicitud);
			    objetoSolicitudMedicamentos.setObservacionesGenerales("");
			    objetoSolicitudMedicamentos.setCentroCostoPrincipal(codigoFarmacia+"");
			    int resultado=objetoSolicitudMedicamentos.insertarUnicamenteSolMedicamentosTransaccional(con);
				//***********************************************************************************************
			    
			    if(resultado>0)
			    {
			    	//*********SE INSERTA EL DETALLE DE LA SOLICITUD DE MEDICAMENTOS*********************
			    	resultado = this.insertarDetalleSolicitudMedicamentos(numeroSolicitud,articulos,tipoArchivo);
			    	//************************************************************************************
			    	
			    	if(resultado>0)
			    	{
			    		//******SE INSERTA LAS AUTORIZACIONES DE CADA ARTÍCULO**************************
			    		//Si el numero de autorización no es vacío se ingresan para cada artículo
			    		//if(!numeroAutorizacion.equals(""))
			    		//	resultado = this.insertarAutorizacionesArticulos(numeroSolicitud,articulos,numeroAutorizacion,tipoArchivo);
			    		//******************************************************************************
			    		
			    		if(resultado>0)
			    		{
			    			//**********SE INSERTA EL DESPACHO DE MEDICAMENTOS BASICO*******************************
			    			resultado = this.insertarDespachoMedicamentosBasico(numeroSolicitud,tipoArchivo);
			    			//**************************************************************************************
			    			
			    			if(resultado>0)
			    			{
			    				//*********SE INSERTA EL DETALLE DEL DESPACHO DE MEDICAMENTOS*****************************
			    				resultado = this.insertarDetalleDespachoMedicamentos(resultado,articulos,tipoArchivo,numeroSolicitud,afectaInventarios,codigoFarmacia); //resultado es el codigo del despacho
			    				//*****************************************************************************************
			    				
			    				if(resultado>0)
			    				{
			    					//*******SE ACTUALIZA EL ESTADO MÉDICO DE LA SOLICITUD DE MEDICAMENTOS*********************
			    					resultado = this.cambiarEstadoMedicoSolicitudMedicamentos(numeroSolicitud,/*numeroAutorizacion,*/tipoArchivo);
			    					//*****************************************************************************************
			    					
			    					if(resultado>0)
			    					{
			    						//*****SE GENERA LA INFORMACIÓN DE CARGO Y SUBCUENTA******************************
			    						resultado = this.generarInfoSubCuentaCargoMedicamentos(numeroSolicitud,/*numeroAutorizacion,*/articulos,datosParametros,tipoArchivo);
			    						//*******************************************************************************
			    						
			    						if(resultado>0)
			    						{
			    							//******SE GENERA LA INFORMACION DE CARGOS DIRECTOS**************************
			    							CargosDirectos cargo= new CargosDirectos();
			    						    cargo.llenarMundoCargoDirecto(numeroSolicitud,usuarioSession.getLoginUsuario(),ConstantesBD.codigoTipoRecargoSinRecargo,ConstantesBD.codigoNuncaValido,"",UtilidadTexto.getBoolean(afectaInventarios),"");
			    						    resultado=cargo.insertar(con);
			    							//**************************************************************************
			    							
			    						    //**************Si se afectan inventarios se deben realizar las validaciones cuando no hay existencias negativas 
			    							if(UtilidadTexto.getBoolean(afectaInventarios)&&
			    								!AlmacenParametros.manejaExistenciasNegativas(con, codigoFarmacia, usuarioSession.getCodigoInstitucionInt()))
			    					        {
			    						    	for (int i=0; i<numArticulos; i ++)
			    						        {
			    						    		codigoArticulo = Integer.parseInt(articulos.get("codigo_"+i).toString());
			    						    		cantidadArticulo = Integer.parseInt(articulos.get("cantidad_"+i).toString());
			    						    		
			    						    		int exArticulo=Integer.parseInt(UtilidadInventarios.getExistenciasXArticulo(codigoArticulo,codigoFarmacia,usuarioSession.getCodigoInstitucionInt()));
			    						    		int codigoConvenio = ConstantesBD.codigoNuncaValido;
			    							        if(cantidadArticulo>exArticulo)
			    						    		{
			    							        	exito = false;
			    							        	editarInconsistenciaEncabezado(
			    												this.numeroFacturaCiclo, 
			    												this.tipoIdentificacionCiclo, 
			    												this.numeroIdentificacionCiclo, 
			    												tipoArchivo, 
			    												"--", 
			    												"el artículo "+articulos.get("nombre_"+i)+" con existencia "+exArticulo+" para el almacen "+this.nombreFarmaciaCiclo+" es insuficiente");
			    						                articulos.put("existenciaXAlmacen_"+i,exArticulo);
			    						    		}
			    							        /*
			    							         * Se hizo la adición del codigo del convenio por la Tarea 59745. Se hizo necesario 
			    							         * obtener el convenio del cargo generado, para ello se consulta el método realizado
			    							         * en el objeto de Cargos (obtenerCodigoConvenioDetalleCargo). Dicho codigoConvenio
			    							         * es envado al método validarNOPOS
			    							         */
			    							        codigoConvenio = Cargos.obtenerCodigoConvenioDetalleCargo(con, codigoArticulo, numeroSolicitud, true);
			    							        double codigoSubcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoArticulo, numeroSolicitud, codigoConvenio, true);
			    							        if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoArticulo, true, false, codigoConvenio))
			    					        		{
			    					        			if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoArticulo, 1, user.getLoginUsuario(), true, false, Utilidades.convertirAEntero(codigoSubcuenta+""),""))
			    					        			{
			    					        				logger.error("No se pudo insertar la Justificacion No Pos del Articulo con codigo >>"+codigoArticulo);
			    					        			}
			    					        		}
			    						        }
			    					        }
			    							//****************************************************************************************************************
			    							
			    						    if(resultado>0)
			    						    {
			    						    	//**********SE ACTUALIZAN LAS EXISTENCIAS DEL ARTÍCULO*********************
			    						    	//Solo aplica si afecta inventarios
			    						    	if(UtilidadTexto.getBoolean(afectaInventarios))
			    						    		resultado = this.actualizarExistenciasArticulosAlmacen(articulos,codigoFarmacia,/*numeroAutorizacion,*/tipoArchivo);
			    						    	//***************************************************************************
			    						    	
			    						    	if(resultado>0)
			    						    	{
			    						    		//SE VERIFICA SI EL ARTICULO TIENE EXCEPCIONES DE FARMACIA, EN CASO DE SER TRUE ENTONCES
											        //SE ACTUALIZA LA EXCEPCIONES_FARMACIA_GEN (excepciones de farmacia generadas)
											        // También se debe verificar el parámetro "Generar excepciones de farmacia automáticamente"
			    						    		/// XPLANNER 37023 SEPTIEMBRE 1/08
			    						    		
											        /*if(UtilidadTexto.getBoolean(ValoresPorDefecto.getGenExcepcionesFarmAut(usuarioSession.getCodigoInstitucionInt())))
											        	resultado=this.insertarExcepcionesFarmaciaGeneradas(articulos, numeroSolicitud,numeroAutorizacion,tipoArchivo);*/
											        //***********************************************************************************************
											        
											        if(resultado<=0)
											        	exito = false;
			    						    	}
			    						    	else
			    						    		exito = false;
			    						    } //Fin if validacion inserción del cargo directo de articulos
			    						    else
			    						    {
			    						    	exito = false;
			    						    	editarInconsistenciaEncabezado(
		    										this.numeroFacturaCiclo, 
		    										this.tipoIdentificacionCiclo, 
		    										this.numeroIdentificacionCiclo, 
		    										tipoArchivo, 
		    										"--", 
		    										"error al generar cargo directo de artículos ");
			    						    	//+(numeroAutorizacion.equals("")?"":"con la autorización "+numeroAutorizacion)
			    						    }
			    							
			    						} //Fin if validacion inserción cargo del despacho de medicamentos
			    						else
			    							exito = false;
			    					}
			    				} //Fin if validacion inserción detalle del despacho de medicamentos
			    				else
			    					exito = false;
			    			} //Fin if validacion inserción despacho de medicamentos
			    			else
			    				exito = false;
			    		} //Fin if validacion insercion de autorizacion articulo
			    		else
			    			exito = false;
			    	} //Fin if validacion inserción detalle solicitud medicamentos
			    	else
			    		exito = false;
			    	
			    } //Fin if validacion inserción solicitud medicamentos básica
			    else
			    {
			    	exito = false;
		        	editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							tipoArchivo, 
							"--", 
							"error al generar solicitud medicamentos ");
		        	//+(numeroAutorizacion.equals("")?"":"con la autorización "+numeroAutorizacion)
			    }
				
	    	}
			
		} //Fin if validacion al generar solicitud
		else
		{
			exito = false;
			editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"error al generar solicitud de medicamentos ");
			//+(numeroAutorizacion.equals("")?"":"con la autorización "+numeroAutorizacion)
		}
		
		return exito;
	}
	
	/**
	 * Método implementado para insertar excepciones de farmacia generadas
	 * @param articulos
	 * @param numeroSolicitud
	 * @param numeroAutorizacion
	 * @param tipoArchivo 
	 * @return
	 */
	private int insertarExcepcionesFarmaciaGeneradas(HashMap<String, Object> articulos, int numeroSolicitud, /*String numeroAutorizacion, */String tipoArchivo) 
	{
		GeneracionExcepcionesFarmacia mundoGEF= new GeneracionExcepcionesFarmacia();
	    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
	    float porcentajeNoCubierto= 0;
	    int resp=ConstantesBD.codigoNuncaValido, resultado = 1;
	    for (int i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i ++)
        {    
            porcentajeNoCubierto=0;
            try
            {
                temporalCodigoArticulo= Integer.parseInt(articulos.get("codigo_"+i)+"");
            }
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                resultado = 0;
            }
            try
            {
                ResultSetDecorator rs=mundoGEF.consultaExcepcionesFarmacia(con, this.codigoConvenioCiclo, this.codigoAreaCiclo, temporalCodigoArticulo, false, true);
                if(rs.next())
                {
                    porcentajeNoCubierto=rs.getFloat("no_cubreEF");
                    mundoGEF.setNumeroSolicitud(numeroSolicitud);
                    mundoGEF.setCodigoArticulo(temporalCodigoArticulo);
                    mundoGEF.setPorcentajeNoCubierto(porcentajeNoCubierto);
                    resp=mundoGEF.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, usuarioSession.getLoginUsuario());
                    if(resp<=0)
                    {
                        logger.warn("Error en el insercion de las  excepciones de farmacia gen con el indice "+i );
                        resultado = 0;
                    }
                }
            }
            catch(SQLException sqle)
            {
                logger.warn("Error en el consulta de las excepciones de farmacia con el indice "+i +sqle);
                resultado = 0;
            }
            
        } 
	    
	    if(resultado<=0)
	    {
	    	editarInconsistenciaEncabezado(
				this.numeroFacturaCiclo, 
				this.tipoIdentificacionCiclo, 
				this.numeroIdentificacionCiclo, 
				tipoArchivo, 
				"--", 
				"error al insertar excepción de farmacia de la solicitud de medicamentos ");
	    	//+(numeroAutorizacion.equals("")?"":"con número de autorización "+numeroAutorizacion)
	    }
	    
        return resultado;
	}
	/**
	 * Método implementado para actualizar las existencias de los articulo por almacen
	 * @param articulos
	 * @param codigoFarmacia
	 * @param numeroAutorizacion 
	 * @param tipoArchivo 
	 * @return
	 */
	private int actualizarExistenciasArticulosAlmacen(HashMap<String, Object> articulos, int codigoFarmacia, /*String numeroAutorizacion,*/ String tipoArchivo) 
	{
		boolean insertoBien= false;
        for (int i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i ++)
        {    
            
            try
            {
                insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(  
                	con, 
                    Integer.parseInt(articulos.get("codigo_"+i).toString()),  
                    codigoFarmacia, 
                    false, 
                    Integer.parseInt(articulos.get("cantidad_"+i).toString()), 
                    usuarioSession.getCodigoInstitucionInt(), 
                    ConstantesBD.continuarTransaccion );
            	
                if(!insertoBien)    
                    logger.warn("Error en el insertar el valor existencias del articulo==="+articulos.get("codigo_"+i));
            }  
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                insertoBien = false;
            }
            catch(SQLException sqle)
            {
                logger.warn("Error en el insert del valor existencias articulos solicitud con indice ="+i +"   error-->"+sqle);
                insertoBien = false;
            }
            
        }
        
        if(!insertoBien)
        {
        	editarInconsistenciaEncabezado(
				this.numeroFacturaCiclo, 
				this.tipoIdentificacionCiclo, 
				this.numeroIdentificacionCiclo, 
				tipoArchivo, 
				"--", 
				"error al actualizar las existencias de la solicitud de medicamentos ");
        	// +(numeroAutorizacion.equals("")?"":"con número de autorización "+numeroAutorizacion)
        }
        
        if(insertoBien)
        	return 1;
        else
        	return 0;
	}
	/**
	 * Método implementado para generar el cargo de medicamentos
	 * @param numeroSolicitud
	 * @param articulos
	 * @param datosParametros
	 * @param tipoArchivo 
	 * @return
	 */
	private int generarInfoSubCuentaCargoMedicamentos(int numeroSolicitud,/*String numeroAutorizacion,*/ HashMap<String, Object> articulos, HashMap<String, Object> datosParametros, String tipoArchivo) throws IPSException 
	{
		boolean generoCargo=false;
	    int codigoArticulo=ConstantesBD.codigoNuncaValido, resultado = 1;
	    int cantidadArticulo=ConstantesBD.codigoNuncaValido;
	    double valorTarifa=ConstantesBD.codigoNuncaValidoDouble;
	    
	    for (int i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i ++)
	    {    
	        
            try
            {
                codigoArticulo= Integer.parseInt(articulos.get("codigo_"+i)+"");
                cantidadArticulo= Integer.parseInt(articulos.get("cantidad_"+i).toString());
                
                valorTarifa= ConstantesBD.codigoNuncaValidoDouble;
                
                //Se pregunta por el tipo de tarifa
			    if(datosParametros.get(ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos).toString().equals(ConstantesIntegridadDominio.acronimoValorArchivoPlano))
			    	valorTarifa = Double.parseDouble(articulos.get("valorUnitario_"+i).toString());
                
        	    Cargos cargoArticulos= new Cargos();
        	    generoCargo=	cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(		
        	    		con, 
						usuarioSession, 
						Integer.parseInt(this.idIngresoCiclo),
						Integer.parseInt(this.idCuentaCiclo),
						numeroSolicitud, 
						codigoArticulo, 
						cantidadArticulo, 
						false/*dejarPendiente*/, 
						ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos/*codigoTipoSolicitudOPCIONAL*/, 
						ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
						this.codigoAreaCiclo, 
						valorTarifa /*valorTarifaOPCIONAL*/,
						false,"");
        	    if(!generoCargo)
        		{
        			logger.warn("Error generando el cargo de la solicitud= "+ numeroSolicitud +" y articulo ->"+ codigoArticulo+" y valorUnitario-> "+valorTarifa);
        			resultado = 0;
        		}
        	    
        	}
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+i +"   error-->"+e);
                resultado = 0;
            }
	        
	    }   
	    
	    if(resultado<=0)
	    {
	    	editarInconsistenciaEncabezado(
				this.numeroFacturaCiclo, 
				this.tipoIdentificacionCiclo, 
				this.numeroIdentificacionCiclo, 
				tipoArchivo, 
				"--", 
				"error al generar el cargo de la solicitud de medicamentos ");
	    	// +(numeroAutorizacion.equals("")?"":"con número de autorización "+numeroAutorizacion)
	    }
	    return resultado;
	}
	/**
	 * Método implementado para cambiar el estado médico de la solicitud de medicamentos
	 * @param numeroSolicitud
	 * @param numeroAutorizacion
	 * @param tipoArchivo 
	 * @return
	 */
	private int cambiarEstadoMedicoSolicitudMedicamentos(int numeroSolicitud, /*String numeroAutorizacion, */String tipoArchivo) 
	{
		int i=0;
	    int inserto= ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    try
	    {
	       inserto =despacho.cambiarEstadoMedicoSolicitudTransaccional(con, ConstantesBD.continuarTransaccion, ConstantesBD.codigoEstadoHCCargoDirecto/*, numeroAutorizacion*/); 
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del cambiar esatdo de la solicitud transaccional con indice ="+i +"   error-->"+sqle);
	        inserto = 0;
	    }
	    if (inserto<1)
	    {
	    	editarInconsistenciaEncabezado(
				this.numeroFacturaCiclo, 
				this.tipoIdentificacionCiclo, 
				this.numeroIdentificacionCiclo, 
				tipoArchivo, 
				"--", 
				"error al cambiar el estado médico de la solicitud de medicamentos ");
	    	//+(numeroAutorizacion.equals("")?"":"con número de autorización "+numeroAutorizacion)
		}
		return inserto;
	}
	/**
	 * Método implementado para insertar el detalle del despacho de medicamentos
	 * @param codigoDespacho
	 * @param articulos
	 * @param tipoArchivo 
	 * @param afectaInventarios 
	 * @param numeroSolicitud 
	 * @param codigoFarmacia 
	 * @return
	 */
	private int insertarDetalleDespachoMedicamentos(int codigoDespacho, HashMap<String, Object> articulos, String tipoArchivo, int numeroSolicitud, String afectaInventarios, int codigoFarmacia) throws IPSException 
	{
	    int temporalCodigoArticulo=0, temporalCantidad;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    int resp=ConstantesBD.codigoNuncaValido, resultado = 1;
	    boolean insertoBienExcepcionFarmacia=false;
	    for (int i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i ++)
	    {   
            try
            {
                temporalCodigoArticulo= Integer.parseInt(articulos.get("codigo_"+i)+"");
                temporalCantidad= Integer.parseInt(articulos.get("cantidad_"+i)+"");
                resp=despacho.insertarDetalleDespachoUnicamenteTransaccional(		
                		con, 
                		ConstantesBD.continuarTransaccion, 
						temporalCodigoArticulo, 
						temporalCodigoArticulo, 
						codigoDespacho, 
						temporalCantidad, 
						"","","","","","");
                
                if (resp<1)
    			{
                	 resultado = 0;
                     editarInconsistenciaEncabezado(
     					this.numeroFacturaCiclo, 
     					this.tipoIdentificacionCiclo, 
     					this.numeroIdentificacionCiclo, 
     					tipoArchivo, 
     					"--", 
     					"error al registrar despacho del artículo "+articulos.get("nombre_"+i));
    			}
                else
                {
                	//***************GENERACIÓN DEL CARGO DE LA ENTIDAD SUBCONTRATADAS*******************************
                	CargosEntidadesSubcontratadas cargos = new CargosEntidadesSubcontratadas();
                	cargos.generarCargoArticulo(
                			con, 
                			codigoFarmacia, 
                			temporalCodigoArticulo, 
                			ConstantesBD.codigoNuncaValido, 
                			numeroSolicitud+"", 
                			"", 
                			UtilidadFecha.getFechaActual(con), 
                			UtilidadFecha.getHoraActual(con), 
                			false, 
                			this.usuarioSession,"","");
                	//**********************************************************************************************
                }
                /*String valorPorDefecto= ValoresPorDefecto.getGenExcepcionesFarmAut(usuarioSession.getCodigoInstitucionInt());
                if(valorPorDefecto.trim().equals("true"))
                {    
	                insertoBienExcepcionFarmacia= despacho.ingresarExcepcionesFarmaciaXConvenioSinPaciente(con, usuarioSession, this.codigoConvenioCiclo, this.codigoAreaCiclo, temporalCodigoArticulo);
					if(!insertoBienExcepcionFarmacia)
					{
		                resultado = 0;
		                editarInconsistenciaEncabezado(
		    					this.numeroFacturaCiclo, 
		    					this.tipoIdentificacionCiclo, 
		    					this.numeroIdentificacionCiclo, 
		    					tipoArchivo, 
		    					"--", 
		    					"error al registrar la excepción de farmacia del artículo "+articulos.get("nombre_"+i));
					}
                }*/	
            }
            catch(NumberFormatException e)
            {
                resultado = 0;
                editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"error al registrar despacho del artículo "+articulos.get("nombre_"+i));
            }
            catch(SQLException sqle)
            {
                resultado = 0;
                editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"error al registrar despacho del artículo "+articulos.get("nombre_"+i));
            }
	        
	    }  
	   	return resultado; 
	}
	/**
	 * Método para insertar el despacho de medicamentos básico
	 * @param numeroSolicitud
	 * @param tipoArchivo 
	 * @return
	 */
	private int insertarDespachoMedicamentosBasico(int numeroSolicitud, String tipoArchivo) 
	{
		 int codigoDespacho=ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho = new DespachoMedicamentos();
	    despacho.setUsuario(usuarioSession.getLoginUsuario());
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    //este se puso en true para que pudiera generar el cargo, pero en realidad debia ser false, 
	    //esto se acordo con Margarita y Nury el 2005-07-01
	    despacho.setEsDirecto(true);
	    try
	    {
	        codigoDespacho=despacho.insertarDespachoBasicoUnicamenteTransaccional(con, ConstantesBD.continuarTransaccion);
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del despacho básico" );
	        codigoDespacho = ConstantesBD.codigoNuncaValido;
	    }
	    
	    if(codigoDespacho<=0)
	    {
	    	editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"error al registrar el despacho de medicamentos");
	    }
	    
	    return codigoDespacho;
	}
	/**
	 * Método implementado para insertar las autorizaciones de los artículos
	 * @param numeroSolicitud
	 * @param articulos
	 * @param numeroAutorizacion
	 * @param tipoArchivo 
	 * @return
	 */
	/*
	private int insertarAutorizacionesArticulos(int numeroSolicitud, HashMap<String, Object> articulos, String numeroAutorizacion, String tipoArchivo) 
	{
		int resp = 1;
		SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
	    
	    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
	    int resultado=ConstantesBD.codigoNuncaValido;
	    int codigoAtributoNumeroAutorizacion=UtilidadValidacion.getCodNumeroAutorizacionAtributosSolicitud(con, usuarioSession.getCodigoInstitucionInt());
	    
	    for (int i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i++)
	    {
	    	temporalCodigoArticulo= Integer.parseInt(articulos.get("codigo_"+i)+"");
            try
            {
            	
                resultado=objetoSolicitudMedicamentos.insertarUnicamenteAtributoSolicitudMedicamentos(con, numeroSolicitud,temporalCodigoArticulo,  codigoAtributoNumeroAutorizacion, numeroAutorizacion);
                if(resultado<=0)
                {
                	resp = 0;
                	editarInconsistenciaEncabezado(
    						this.numeroFacturaCiclo, 
    						this.tipoIdentificacionCiclo, 
    						this.numeroIdentificacionCiclo, 
    						tipoArchivo, 
    						"--", 
    						"error al registrar el número de autorización del artículo "+articulos.get("nombre_"+i));
                }
            	
            }
            catch (Exception e)
            {
                resp = 0;
                editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						tipoArchivo, 
						"--", 
						"error al registrar el número de autorización del artículo "+articulos.get("nombre_"+i));
            }
	        
	    }    
	    return resp;
	}
	*/
	/**
	 * Método implementado para insertar el detalle de una solicitud de medicamentos
	 * @param numeroSolicitud
	 * @param articulos
	 * @param tipoArchivo 
	 * @return
	 */
	private int insertarDetalleSolicitudMedicamentos(int numeroSolicitud, HashMap<String, Object> articulos, String tipoArchivo) 
	{
		int resultado = 1;
		SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
	    int temporalCodigoArticulo=ConstantesBD.codigoNuncaValido;
	    int resp=ConstantesBD.codigoNuncaValido; 
	    int i=0;
	    for (i=0; i<Utilidades.convertirAEntero(articulos.get("numRegistros").toString(), true); i ++)
	    {    
	        
            try
            {
                temporalCodigoArticulo= Integer.parseInt(articulos.get("codigo_"+i)+"");
            }
            catch(NumberFormatException e)
            {
                resultado = 0;
                editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						tipoArchivo, 
						"--", 
						"error al insertar detalle de medicamentos para el artículo "+articulos.get("nombre_"+i));
                
            }
            resp=objetoSolicitudMedicamentos.insertarUnicamenteDetalleSolicitudMedicamentos(
            	con, 
            	numeroSolicitud, 
            	temporalCodigoArticulo, 
            	ValoresPorDefecto.getNumDiasTratamientoMedicamentos(usuarioSession.getCodigoInstitucionInt()),
            	0
            );
            if (resp<1)
			{
            	resultado = 0;
                editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						tipoArchivo, 
						"--", 
						"error al insertar detalle de medicamentos para el artículo "+articulos.get("nombre_"+i));
                
			}
	        
	    }  
	    return resultado;
	}
	/**
	 * Método implementado para generar los cargos del archivo AP
	 * @param datosAP
	 * @param datosParametros 
	 * @return
	 */
	private boolean generarCargosAP(HashMap<String, Object> datosAP, HashMap<String, Object> datosParametros) throws IPSException 
	{
		
		boolean exito = true;
		int numeroSolicitud = 0, codigoServicio = ConstantesBD.codigoNuncaValido, codigoCentroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		UsuarioBasico user=new UsuarioBasico();
		for(int i=0;i<Utilidades.convertirAEntero(datosAP.get("numRegistros").toString(), true);i++)
		{
			//1) Consultar el servicio del cargo
			codigoServicio = consultarServicioCargo(datosAP.get("codigoProcedimiento_"+i).toString());
			
			if(codigoServicio>0)
			{
				//2) Consultar el centro de costo que ejecuta
				codigoCentroCostoEjecuta = consultarCentroCostoEjecuta(codigoServicio,datosAP.get("codigoProcedimiento_"+i).toString(),ConstantesBD.ripsAP);
				
				if(codigoCentroCostoEjecuta>0)
				{
					//3) Se inserta la solicitud general
					numeroSolicitud = insertarSolicitudGeneral(
						con, 
						datosAP.get("fechaProcedimiento_"+i).toString(), 
						ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, 
						codigoCentroCostoEjecuta,
						/*datosAP.get("numeroAutorizacion_"+i).toString(),*/
						Integer.parseInt(datosParametros.get(ConstantesIntegridadDominio.acronimoProfecionalResponde).toString()));
					
					if(numeroSolicitud>0)
					{
						//4) Se inserta la información del cargo directo
						boolean insertoCargoDirecto = insertarCargoDirecto(
							numeroSolicitud, 
							codigoServicio, 
							datosAP.get("fechaProcedimiento_"+i).toString(), 
							ConstantesBD.codigoNuncaValido, //No aplica Causa Externa
							"", //No aplica finalidad consulta 
							Integer.parseInt(datosAP.get("finalidadProcedimiento_"+i).toString()), 
							Utilidades.convertirAEntero(datosAP.get("personalAtiende_"+i).toString(),true),
							Utilidades.convertirAEntero(datosAP.get("formaRealizacion_"+i).toString(),true), 
							datosAP.get("diagPpal_"+i).toString(),
							ConstantesBD.codigoNuncaValido, //No aplica tipo diagnostico 
							datosAP.get("diagRelacionado_"+i).toString(), "", "", 
							datosAP.get("complicacion_"+i).toString(), true,
							ConstantesBD.ripsAP,
							datosAP.get("codigoProcedimiento_"+i).toString());
						
						if(insertoCargoDirecto)
						{
							//5) Se registra la tarifa del servicio
						    double valorTarifaOpcional= ConstantesBD.codigoNuncaValidoDouble;
						    
						    //Se pregunta por el tipo de tarifa
						    if(datosParametros.get(ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos).toString().equals(ConstantesIntegridadDominio.acronimoValorArchivoPlano))
						    	valorTarifaOpcional = Double.parseDouble(datosAP.get("valorProcedimiento_"+i).toString());
						    
						    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
						    Cargos cargos= new Cargos();
						    logger.info("***************TARIFA OPCIONAL--------->"+valorTarifaOpcional);
						    //Se simula el paciente pues solo se necesita el codigo del ingreso
						  
						    boolean insertoCargo= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
								usuarioSession,
								Integer.parseInt(this.idCuentaCiclo),
								Integer.parseInt(this.idIngresoCiclo),
								false/*dejarPendiente*/, 
								numeroSolicitud, 
								ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
								codigoCentroCostoEjecuta/*codigoCentroCostoEjecutaOPCIONAL*/, 
								codigoServicio/*codigoServicioOPCIONAL*/, 
								1/*cantidadServicioOPCIONAL*/, 
								valorTarifaOpcional/*valorTarifaOPCIONAL*/, 
								ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
								/*datosAP.get("numeroAutorizacion_"+i).toString() -- numeroAutorizacionOPCIONAL , */
								"" /*esPortatil*/,datosAP.get("fechaProcedimiento_"+i)+"" );
						    
						    //Se verifica si se insertó el cargo
						    if(insertoCargo)
						    {
						    	//6) Validación de la autorización de servicios
						    	if(esValidoAutorizacionesServicio(
						    		codigoServicio, 
						    		consultarCantidadServicio(ConstantesBD.ripsAP, datosAP.get("codigoProcedimiento_"+i).toString()), 
						    		datosAP.get("numeroAutorizacion_"+i).toString()))
						    	{
						    		//7) Se actualiza la solicitud en la detalle del registro de entidades subcontratadas
						    		if(this.actualizarDetalleRegEntidadSubcontratada(numeroSolicitud, codigoServicio, datosAP.get("numeroAutorizacion_"+i).toString() )<=0)
						    		{
						    			exito = false;
								    	editarInconsistenciaEncabezado(
											this.numeroFacturaCiclo, 
											this.tipoIdentificacionCiclo, 
											this.numeroIdentificacionCiclo, 
											ConstantesBD.ripsAP, 
											"--", 
											"error al actualizar el numero de solicitud del servicio "+datosAP.get("codigoProcedimiento_"+i)+" en el registro de entidad subcontratada");
						    		}
						    	}
						    	else
						    	{
						    		exito = false;
							    	editarInconsistenciaEncabezado(
										this.numeroFacturaCiclo, 
										this.tipoIdentificacionCiclo, 
										this.numeroIdentificacionCiclo, 
										ConstantesBD.ripsAP, 
										"--", 
										"la cantidad y numero de autorización del servicio "+datosAP.get("codigoProcedimiento_"+i)+" son erróneos");
						    	}
						    	if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoServicio, false, false, cargos.getDtoDetalleCargo().getCodigoConvenio()))
								{
									if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoServicio, 1, user.getLoginUsuario(), false, false, Utilidades.convertirAEntero(cargos.getDtoDetalleCargo().getCodigoSubcuenta()+""),""))
									{
										logger.error("No se logro insertar la Justificacion pendiente para el Servicio >> "+codigoServicio);
									}
								}
						    }
						    else
						    {
						    	exito = false;
						    	editarInconsistenciaEncabezado(
									this.numeroFacturaCiclo, 
									this.tipoIdentificacionCiclo, 
									this.numeroIdentificacionCiclo, 
									ConstantesBD.ripsAP, 
									"--", 
									"error al generar cargo de la solicitud del servicio "+datosAP.get("codigoProcedimiento_"+i));
						    }
						} //Fin if validacion insercion cargo directo
						else
							exito = false;
						
					} //Fin if validacion insercion solicitud general
					else
					{
						exito = false;
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								ConstantesBD.ripsAP, 
								"--", 
								"error al generar solicitud para el servicio "+datosAP.get("codigoProcedimiento_"+i));
						
					}
				} //fin if validacion centro costo ejecuta
				else
					exito = false;
			} //Fin if validacion servicio
			else
				exito = false;
				
		}
		
		return exito;
	}
	
	/**
	 * Método para consultar la cantidad de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	private int consultarCantidadServicio(String archivo,String codigoServicio)
	{
		HashMap campos = new HashMap();
		campos.put("archivo",archivo);
		campos.put("codigoServicio",codigoServicio);
		campos.put("numeroFactura",this.numeroFacturaCiclo);
		campos.put("tipoIdentificacion",this.tipoIdentificacionCiclo);
		campos.put("numeroIdentificacion",this.numeroIdentificacionCiclo);
		campos.put("usuario",usuarioSession.getLoginUsuario());
		campos.put("codigoCentroAtencion",usuarioSession.getCodigoCentroAtencion());
		return lecturaDao.consultarCantidadServicio(con, campos);
	}
	
	/**
	 * Método implementado para actualizar el detalle del registro de la entidad subcontratada
	 * @param numeroAutorizacion 
	 * @param campos
	 * @return
	 */
	private int actualizarDetalleRegEntidadSubcontratada(int numeroSolicitud,int codigoServicio, String numeroAutorizacion)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoEntidadesSubcontratadas", this.consecutivoRegistroPacEntidadSub);
		campos.put("codigoServicio", codigoServicio);
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("numeroAutorizacion", numeroAutorizacion);
		return lecturaDao.actualizarDetalleRegEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método implementado para insertar información de cargo directo
	 * @param tipoArchivo 
	 * @param codigoPropietario 
	 * @return
	 */
	private boolean insertarCargoDirecto(int numeroSolicitud,int codigoServicio,String fecha,int causaExterna,String finalidadConsulta,int finalidadProcedimiento,int personalAtiende,int formaRealizacion,String diagPrincipal,int tipoDiagnostico,String diagRel1,String diagRel2,String diagRel3,String diagComplicacion,boolean conRips, String tipoArchivo, String codigoPropietario) 
	{
		CargosDirectos mundoCargosDirectos = new CargosDirectos();
		boolean exito = true, puedoInsertar = true;
		
		DtoCargoDirecto dtoCargoDirecto = new DtoCargoDirecto();
		dtoCargoDirecto.setNumeroSolicitud(numeroSolicitud+"");
		dtoCargoDirecto.setLoginUsuario(usuarioSession.getLoginUsuario());
		dtoCargoDirecto.setCodigoTipoRecargo(ConstantesBD.codigoTipoRecargoSinRecargo);
		dtoCargoDirecto.setCodigoServicioSolicitado(codigoServicio);
		
		//Si es servicio se llena la información RIPS de servicios
		if(conRips)
		{
			DtoCargoDirectoHC dtoCargoDirectoHC = new DtoCargoDirectoHC();
			dtoCargoDirectoHC.setCargado(true);
			dtoCargoDirectoHC.setManejaRips(true);
			dtoCargoDirectoHC.setFechaSolicitud(fecha);
			dtoCargoDirectoHC.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
			dtoCargoDirectoHC.setCodigoServicio(codigoServicio);
			dtoCargoDirectoHC.setCodigoTipoServicio(Utilidades.obtenerTipoServicio(con, codigoServicio+""));
			dtoCargoDirectoHC.setCodigoCausaExterna(causaExterna);
			dtoCargoDirectoHC.setCodigoFinalidadConsulta(finalidadConsulta);
			dtoCargoDirectoHC.setCodigoFinalidadProcedimiento(finalidadProcedimiento);
			dtoCargoDirectoHC.setCodigoInstitucion(usuarioSession.getCodigoInstitucionInt());
			dtoCargoDirectoHC.setLoginUsuarioModifica(usuarioSession.getLoginUsuario());
			dtoCargoDirectoHC.setTipo(ConstantesIntegridadDominio.acronimoNormal);
			dtoCargoDirectoHC.setPersonalAtiende(personalAtiende);
			dtoCargoDirectoHC.setFormaRealizacion(formaRealizacion);
			
			//Se añade el diagnostico principal
			if(!diagPrincipal.equals(""))
			{
				DtoDiagnosticosCargoDirectoHC dtoDiagPrincipal = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagPrincipal.setAcronimoDiagnostico(diagPrincipal.toUpperCase());
				dtoDiagPrincipal.setCieDiagnostico(this.tipoCie);
				switch(tipoDiagnostico)
				{
					case 1: //Impresion diagnostica
					dtoDiagPrincipal.setCodigoTipoDiagnostico(ConstantesIntegridadDominio.acronimoTipoDiagnosticoImpresionDiagnostica);
					break;
					case 2: //Cofirmado nuevo
					dtoDiagPrincipal.setCodigoTipoDiagnostico(ConstantesIntegridadDominio.acronimoTipoDiagnosticoConfirmadoNuevo);
					break;
					case 3: //Confirmado repetido
					dtoDiagPrincipal.setCodigoTipoDiagnostico(ConstantesIntegridadDominio.acronimoTipoDiagnosticoConfirmadoRepetido);
					break;	
				}
				dtoDiagPrincipal.setPrincipal(true);
				dtoDiagPrincipal.setComplicacion(false);
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagPrincipal);
				
				//Se verifica que el diagnóstico exista
				if(!validarDiagnostico(diagPrincipal, tipoArchivo, "diagnóstico principal"))
					puedoInsertar = false;
				
			}
			
			//Se añaden diagnosticos relacionados
			if(!diagRel1.equals(""))
			{
				DtoDiagnosticosCargoDirectoHC dtoDiagRel1 = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagRel1.setAcronimoDiagnostico(diagRel1.toUpperCase());
				dtoDiagRel1.setCieDiagnostico(this.tipoCie);
				dtoDiagRel1.setPrincipal(false);
				dtoDiagRel1.setComplicacion(false);
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagRel1);
				
				//Se verifica que el diagnóstico exista
				if(!validarDiagnostico(diagRel1, tipoArchivo, "diagnóstico relacionado 1"))
					puedoInsertar = false;
					
			}
			if(!diagRel2.equals(""))
			{
				DtoDiagnosticosCargoDirectoHC dtoDiagRel2 = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagRel2.setAcronimoDiagnostico(diagRel2.toUpperCase());
				dtoDiagRel2.setCieDiagnostico(this.tipoCie);
				dtoDiagRel2.setPrincipal(false);
				dtoDiagRel2.setComplicacion(false);
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagRel2);
				
				//Se verifica que el diagnóstico exista
				if(!validarDiagnostico(diagRel2, tipoArchivo, "diagnóstico relacionado 2"))
					puedoInsertar = false;
				
			}
			if(!diagRel3.equals(""))
			{
				DtoDiagnosticosCargoDirectoHC dtoDiagRel3 = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagRel3.setAcronimoDiagnostico(diagRel3.toUpperCase());
				dtoDiagRel3.setCieDiagnostico(this.tipoCie);
				dtoDiagRel3.setPrincipal(false);
				dtoDiagRel3.setComplicacion(false);
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagRel3);
				
				//Se verifica que el diagnóstico exista
				if(!validarDiagnostico(diagRel3, tipoArchivo, "diagnóstico relacionado 3"))
					puedoInsertar = false;
					
				
			}
			if(!diagComplicacion.equals(""))
			{
				DtoDiagnosticosCargoDirectoHC dtoDiagComplicacion = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagComplicacion.setAcronimoDiagnostico(diagComplicacion.toUpperCase());
				dtoDiagComplicacion.setCieDiagnostico(this.tipoCie);
				dtoDiagComplicacion.setPrincipal(false);
				dtoDiagComplicacion.setComplicacion(true);
				dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagComplicacion);
				
				//Se verifica que el diagnóstico exista
				if(!validarDiagnostico(diagComplicacion, tipoArchivo, "diagnóstico complicación"))
					puedoInsertar = false;
						
			}
			mundoCargosDirectos.setCargoDirectoHC(dtoCargoDirectoHC);
		}
		
		mundoCargosDirectos.getCargoDirecto().add(dtoCargoDirecto);
		
		if(puedoInsertar)
		{
			if(mundoCargosDirectos.insertar(con)>0)
				exito = true;
			else
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						tipoArchivo, 
						"--", 
						"error al insertar la información RIPS del cargo del servicio "+codigoPropietario);
			}
		}
		else
			exito = false;
		
		return exito;
	}
	
	/**
	 * Método para realizar la validación de los diagnósticos
	 * @param acronimo
	 * @param nombreArchivo
	 * @param mensaje
	 * @return
	 */
	private boolean validarDiagnostico(String acronimo,String nombreArchivo,String mensaje)
	{
		 boolean exito = true;
		 
		 if(!UtilidadValidacion.diagnosticoExistente(acronimo, this.tipoCie))
		 {
			 exito = false;
			 editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						nombreArchivo, 
						mensaje, 
						"no existe parametrización para: "+acronimo+" sobre el cie: "+this.nombreTipoCie);
		 }
		 
		 return exito;
	}
	/**
	 * Método para insertar una solicitud estancia en la tabla solicitudes
	 * @param con
	 * @param estancia
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private int insertarSolicitudGeneral(Connection con, String fechaSolicitud,int tipoSolicitud,int codigoCentroCostoSolicitado,/*String numeroAutorizacion,*/int codigoMedicoResponde) {
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(fechaSolicitud);
		solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
		solicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		solicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		solicitud.setCentroCostoSolicitante(new InfoDatosInt(this.codigoAreaCiclo));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCostoSolicitado));
	    solicitud.setCodigoCuenta(Integer.parseInt(this.idCuentaCiclo));
	    //solicitud.setNumeroAutorizacion(numeroAutorizacion);
	    solicitud.setCobrable(true);
	    solicitud.setVaAEpicrisis(false);
	    solicitud.setUrgente(false);
	    
	    try
	    {
	    	
	    	//1) Se inserta la solicitud
	        int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	        
	        //2) Se cambia el estado de la solicitud de cargo directo
	        if(solicitud.cambiarEstadosSolicitud(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCCargoDirecto).isTrue())
	        {
	        	UsuarioBasico medico = new UsuarioBasico();
	        	medico.setCodigoPersona(codigoMedicoResponde);
	        	medico.cargarUsuarioBasico(con, codigoMedicoResponde);
	        	
	        	//3) Se actualiza el médico que responde
	        	int resp = solicitud.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, medico, ConstantesBD.continuarTransaccion);
	        	
	        	if(resp<=0)
		        	return 0;
		        else
		        	return numeroSolicitud;
	        }
	        
	        
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en la transaccion del insert en la solicitud básica");
			
	    }
	    return 0;
	}
	
	
	/**
	 * Método que realiza la consulta del servicio de cargo de servicios
	 * @param codigoPropietario
	 * @return
	 */
	private int consultarServicioCargo(String codigoPropietario)
	{
		int codigoServicio = ConstantesBD.codigoNuncaValido;
		
		HashMap campos = new HashMap();
		campos.put("codigoPropietario",codigoPropietario);
		campos.put("tarifarioOficial",this.codigoManual);
		codigoServicio = lecturaDao.consultarServicioCargo(con, campos);
		
		if(codigoServicio<=0)
		{
			editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					"--", 
					"--", 
					"no se encontró un servicio para el código "+UtilidadesFacturacion.obtenerNombreTarifarioOficial(this.codigoManual+"")+": "+codigoPropietario);
		}
		
		return codigoServicio;
	}
	
	/**
	 * Método implementado para consultar el centro de costo que ejecuta
	 * @param codigoServicio
	 * @param tipoArchivo 
	 * @param codigoServiciosArc 
	 * @return
	 */
	private int consultarCentroCostoEjecuta(int codigoServicio, String codigoServiciosArc, String tipoArchivo)
	{
		int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
		
		//Se consulta el grupo del servicio
		int codigoGrupoServicio = UtilidadesFacturacion.consultarGrupoServicio(con, codigoServicio);
		//Se consultan los centros de costo del grupo de servicio
		ArrayList<HashMap<String, Object>> centrosCosto = UtilidadesFacturacion.consultarCentrosCostoGrupoServicio(
				con, 
				codigoGrupoServicio, 
				usuarioSession.getCodigoCentroAtencion(),
				false, false);
		
		//Se toma el primer centro de costo que se encuentra
		if(centrosCosto.size()>0)
		{
			HashMap<String, Object> elemento = (HashMap<String, Object>)centrosCosto.get(0);
			codigoCentroCosto = Integer.parseInt(elemento.get("codigoCentroCosto").toString());
		}
		
		///Si no se encuentra el centro de costo se genera inconcistencias
		if(codigoCentroCosto<=0)
		{
			editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"no se encontró el centro de costo que ejecuta en la generación de cargos del servicio "+codigoServiciosArc);
		}
		
		return codigoCentroCosto;
	}
	
	
	
	/**
	 * Método que verifica si es válida la autorizacion del servicio
	 * @param codigoServicio
	 * @param cantidad
	 * @param numeroAutorizacion
	 * @return
	 */
	private boolean esValidoAutorizacionesServicio(int codigoServicio,int cantidad,String numeroAutorizacion)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoEntidadesSubcontratadas",this.consecutivoRegistroPacEntidadSub);
		campos.put("codigoServicio",codigoServicio);
		campos.put("cantidad",cantidad);
		campos.put("autorizacion",numeroAutorizacion);
		return lecturaDao.esValidoAutorizacionesServicio(con, campos);
	}
	
	/**
	 * Método que consulta el articulo para un cargo de artículos
	 * @param nombreGenerico
	 * @param datosParametros
	 * @param tipoArchivo 
	 * @return
	 */
	private int validacionCodigoArticulo(String nombreGenerico,HashMap<String,Object> datosParametros, String tipoArchivo)
	{
		int codigoArticulo = ConstantesBD.codigoNuncaValido;
		String codArticulo = "", parametro = datosParametros.get(ConstantesIntegridadDominio.acronimoCodigoUtilizadoArticulos).toString();
		
		for(int i=0;i<nombreGenerico.length();i++)
		{
			if(nombreGenerico.charAt(i)==32) //si es igual a espacio se termina el ciclo
				i = nombreGenerico.length(); 
			else
				codArticulo += nombreGenerico.charAt(i); //se va editando el código
		}
		
		//Se verifica el codigo del articulo bien sea si es por codigo axioma o por codigo itnerfaz
		codigoArticulo = lecturaDao.consultarArticuloCargo(con, codArticulo, parametro);
		
		if(codigoArticulo<=0)
		{
			editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					tipoArchivo, 
					"--", 
					"no se encontró un artículo con el código "+ValoresPorDefecto.getIntegridadDominio(parametro)+": "+codArticulo);
		}
		
		return codigoArticulo;
		
	}
	
	/**
	 * Método que consulta los parámetros usados para la generación de los cargos directos
	 * @param datosAP
	 * @param datosAM
	 * @param datosAT
	 * @param datosAC
	 * @return
	 */
	private HashMap<String, Object> cargarParametrosCargosDirectos(HashMap<String, Object> datosAP, HashMap<String, Object> datosAM, HashMap<String, Object> datosAT, HashMap<String, Object> datosAC) 
	{
		HashMap<String, Object> datosParametros = new HashMap<String, Object>();
		
		//Se averigua el total de servicios y el total de articulos de la factura, tipo id y numero id paciente 
		int totalServicios = Utilidades.convertirAEntero(datosAP.get("numRegistros")+"", true) + Utilidades.convertirAEntero(datosAC.get("numRegistros")+"", true);
		int totalArticulos = Utilidades.convertirAEntero(datosAM.get("numRegistros")+"", true);
		String valor = "";
		boolean exito = true;
		
		//Se calculan cuántos servicios y cuántos artículos hay en el archivo AT
		for(int i=0;i<Utilidades.convertirAEntero(datosAT.get("numRegistros")+"", true);i++)
		{
			//Se suman los servicios
			if(!datosAT.get("codigoServicio_"+i).toString().equals(""))
				totalServicios ++;
			//Se suman los articulos
			if(datosAT.get("codigoServicio_"+i).toString().equals("")&&datosAT.get("tipoServicio_"+i).toString().equals("1"))
				totalArticulos ++;
		}
		//-------------------------------------------------------------------------------------------------------------------
		
		//Si habían servicios o artículos se consultan los parámetros que aplican para los servicios
		if(totalServicios>0||totalArticulos>0)
		{
			//1) Se consulta el parámetros profesional responde----------------------------------------------------

			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoProfecionalResponde, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"el parámetro profesional que responde es requerido");
			}
			else
				datosParametros.put(ConstantesIntegridadDominio.acronimoProfecionalResponde, valor);
			//------------------------------------------------------------------------------------------------------------
			//2) Se consulta el parámetros tipo tarifa de cargos directos --------------------------------------------------
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"el parámetro tipo de tarifa de los cargos directos es requerido");
			}
			else
				datosParametros.put(ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos, valor);
			//-------------------------------------------------------------------------------------------------------------
			
			//Modificacion Anexo 809
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoEspecialidadResponde, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"la especialidad del profesional es requerida");
			}
			else
				datosParametros.put(ConstantesIntegridadDominio.acronimoEspecialidadResponde, valor);
			
			
		}
		
		//Si habían artículos se consultan los parámetros que aplican para los artículos
		if(totalArticulos>0)
		{
			//3) Se consulta el parámetros código utilizado para artículos----------------------------------------------------
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoCodigoUtilizadoArticulos, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"el parámetro código utilizado para artículos es requerido");
			}
			else
				datosParametros.put(ConstantesIntegridadDominio.acronimoCodigoUtilizadoArticulos, valor);
			//------------------------------------------------------------------------------------------------------------
			//4) Se consulta el parámetros Farmacia para cargos directos de artículos----------------------------------------------------
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoFarmaciaCargosDirectosArticulos, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					usuarioSession.getCodigoCentroAtencion());
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"el parámetro farmacia para cargos directos de artículos es requerido");
			}
			else
			{
				datosParametros.put(ConstantesIntegridadDominio.acronimoFarmaciaCargosDirectosArticulos, valor);
				//Se carga el nombre de la farmacia
				this.nombreFarmaciaCiclo = Utilidades.obtenerNombreCentroCosto(con, 
					Integer.parseInt(datosParametros.get(ConstantesIntegridadDominio.acronimoFarmaciaCargosDirectosArticulos).toString()), 
					usuarioSession.getCodigoInstitucionInt());
			}
			//------------------------------------------------------------------------------------------------------------
			//5) Se consulta el parámetros afecta inventarios en cargos de inventarios----------------------------------------------------
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.tipoCargosDirectos, 
					ConstantesIntegridadDominio.acronimoAfectaInventariosCargosInventarios, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"el parámetro afecta inventarios para cargos de inventarios es requerido");
			}
			else
				datosParametros.put(ConstantesIntegridadDominio.acronimoAfectaInventariosCargosInventarios, valor);
			//------------------------------------------------------------------------------------------------------------
		}
		
		datosParametros.put("procesoExitoso",exito?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return datosParametros;
	}
	/**
	 * Método que crea el ingreso la cuenta y/o el egreso de cada factura, tipo id y numero identificacion
	 * @return
	 */
	private boolean crearIngresoCuentaEgreso() throws IPSException 
	{
		boolean exito = false;
		HashMap<String,Object> datosCuenta = new HashMap<String, Object>(); //mapa donde se manejan los datos de la cuenta
		HashMap<String,Object> datosRegPacEntSubcontratada = new HashMap<String, Object>(); //mapa donde se manejan los datos del registro entidad subcontratada
		HashMap<String,Object> datosPaciente = new HashMap<String, Object>(); //mapa donde se manejan los datos del paciente
		HashMap<String,Object> datosParametrosGenerales = new HashMap<String, Object>(); //mapa donde se manejan los parámetros generales
		int viaIngreso = ConstantesBD.codigoNuncaValido;
		
		//1)*************VALIDACION VÍA INGRESO Y REGISTRO ENTIDAD SUBCONTRATADA****************************************
		/**
		 * En esta parte se verifica cuál va ser la vía de ingreso de la cuenta que se va a crear consultando la información
		 * léida de los planos y se verifica que también exista el correspondiente registro paciente de la entidad subcontratada
		 */
		//Se verifica cual es la vía de ingreso de la factura
		datosCuenta = this.verificarViaIngresoFactura();
		//se toma la vía de ingreso encontrada
		viaIngreso = Utilidades.convertirAEntero(datosCuenta.get("codigoViaIngreso").toString(),true);
		
		if(viaIngreso!=ConstantesBD.codigoNuncaValido)
		{
			String numeroAutorizacion = "";
			if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion||viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
				numeroAutorizacion = datosCuenta.get("numeroAutorizacion").toString();
			//Se verifica la existencia del registro de paciente entidad subcontratada 
			datosRegPacEntSubcontratada = getDatosRegPacEntidadSubcontratada(con, numeroAutorizacion);
			
			if(Integer.parseInt(datosRegPacEntSubcontratada.get("numRegistros").toString())<=0)
				editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					"--", 
					"--", 
					"paciente sin registro de entidad subcontratada abierto en el centro de atención: "+usuarioSession.getCentroAtencion());
			else
			{
				exito = true;
				this.consecutivoRegistroPacEntidadSub = datosRegPacEntSubcontratada.get("consecutivo").toString();
			}
		}
		//****************************************************************************************************************
		
		if(exito)
		{
			int resp0 = 0;
			boolean desplazado = false;
			
			//2)**************ACTUALIZAR LA INFORMACIÓN DEL PACIENTE***************************************************
			datosPaciente = consultarInformacionArchivo(this.con, ConstantesBD.ripsUS, false);
			
			//SE verifica que se haya ingresado información del paciente en el archivo US
			if(Integer.parseInt(datosPaciente.get("numRegistros").toString())>0)
			{
				Paciente mundoPaciente = new Paciente();
				//Se consulta el código del paciente
				datosCuenta.put("codigoPaciente",Paciente.obtenerCodigoPersona(con, this.numeroIdentificacionCiclo, this.tipoIdentificacionCiclo));
				this.codigoPacienteCiclo = Integer.parseInt(datosCuenta.get("codigoPaciente").toString());
				try 
				{
					mundoPaciente.cargarPaciente(con, Integer.parseInt(datosCuenta.get("codigoPaciente").toString()));
					
					//Se cargan los datos del paciente al mundo
					mundoPaciente.setPrimerApellidoPersona(datosPaciente.get("primerApellido").toString());
					mundoPaciente.setSegundoApellidoPersona(datosPaciente.get("segundoApellido").toString());
					mundoPaciente.setPrimerNombrePersona(datosPaciente.get("primerNombre").toString());
					mundoPaciente.setSegundoNombrePersona(datosPaciente.get("segundoNombre").toString());
					mundoPaciente.setCodigoSexo(datosPaciente.get("sexo").toString().equals("M")?ConstantesBD.codigoSexoMasculino+"":ConstantesBD.codigoSexoFemenino+"");
					
					/*Si el campo tipo de usuario del archivo US es 6, 7 y 8.
					Se actualiza en el campo Grupo Poblacional la opcion Desplazado
					y en la cuenta en el campo Desplazado asignar el valor 'S'. Anexo 600
					*/
					if(datosPaciente.get("tipoUsuario").toString().equals("6") || datosPaciente.get("tipoUsuario").toString().equals("7") || datosPaciente.get("tipoUsuario").toString().equals("8"))
					{
						mundoPaciente.setCodigoGrupoPoblacional(ConstantesIntegridadDominio.acronimoDesplazados);
						desplazado = true;
						resp0 = mundoPaciente.actualizarGrupoPoblacional(con);
					}
					else
					{
						desplazado = false;
						resp0 = 1;
					}
					
					resp0 = mundoPaciente.modificarPersona(this.con, ConstantesBD.continuarTransaccion, ConstantesBD.tipoPersonaPaciente, usuarioSession.getCodigoInstitucionInt());
				} 
				catch (Exception e) 
				{
					logger.info("Error al actualizar los datos del paciente: "+e);
					resp0 = 0;
				}
				
				if(resp0<=0)
				{
					exito = false;
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"problemas en base de datos al actualizar datos del paciente");
				}
			}
			else
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						ConstantesBD.ripsUS, 
						"--", 
						"el paciente no está registrado en el archivo");
			}
				
			//*********************************************************************************************************
			
			
			if(resp0>0)
			{
				//3)****************CREACIÓN DEL INGRESO DEL PACIENTE**************************************************
				//Se valida que se pueda tomar e consecutivo del ingreso-----------------------------------------------
//				Se valida que se pueda tomar e consecutivo del ingreso-----------------------------------------------
				String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt());
				String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt(),valorConsecutivoIngreso);
				
				if(!UtilidadCadena.noEsVacio(valorConsecutivoIngreso) || valorConsecutivoIngreso.equals("-1"))
				{
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"falta definir el consecutivo de ingreso para generar nuevos ingresos de paciente");
					exito = false;
				}
				else
				{
					try
					{
						Integer.parseInt(valorConsecutivoIngreso);
					}
					catch(Exception e)
					{
						exito = false;
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								"--", 
								"--", 
								"el consecutivo de ingresos debe ser numérico");
					}
				}
				//-----------------------------------------------------------------------------------------------------
				
				//Se intenta carga la informacion del paciente ---------------------------------------------------
				PersonaBasica mundoPersonaBasica = new PersonaBasica();
				if(exito)
				{
					try 
					{
						mundoPersonaBasica.cargar(con,Integer.parseInt(datosCuenta.get("codigoPaciente").toString()));
					} 
					catch (Exception e) 
					{
						exito = false;
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								"--", 
								"--", 
								"error al tratar de cargar la información del paciente: "+e);
					} 
				}
				//------------------------------------------------------------------------------------------------
				
				if(exito)
				{
					//Se consulta la fecha/hora del ingreso dependiendo de la via de ingreso
					String[] fechaHoraIngreso = obtenerFechaHoraIngreso(datosCuenta);
					
					IngresoGeneral ingreso = new IngresoGeneral(
						usuarioSession.getCodigoInstitucion(), 
						mundoPersonaBasica,
						ConstantesIntegridadDominio.acronimoEstadoAbierto,
						usuarioSession.getLoginUsuario(),
						valorConsecutivoIngreso+"",
						anioConsecutivoIngreso,
						usuarioSession.getCodigoCentroAtencion(),
						datosRegPacEntSubcontratada.get("consecutivo").toString(),
						fechaHoraIngreso[0],
						fechaHoraIngreso[1],""); 
					ingreso.init(System.getProperty("TIPOBD"));
				
				
					RespuestaValidacion resp1;
					//Se valida que el paciente no tenga un ingreso abierto
					resp1 = UtilidadValidacion.validacionIngresarIngreso(con, ingreso.getCodigoTipoIdentificacionPaciente(), ingreso.getNumeroIdentificacionPaciente(), ingreso.getInstitucion() );
	
					if (resp1.puedoSeguir)
					{
						//Se realiza el bloqueo del consecutivo de ingreso disponible
												//Se inserta el ingreso del paciente----------------------------------------------------------------
					    int idIngreso=ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
					    this.idIngresoCiclo = idIngreso+"";
					    
					    //Se valida si el ingreso se insertó correctamente
					    if(idIngreso>0)
					    {
					    	//Se cargan los parámetros que se necesitan para creación de la cuenta
					    	datosParametrosGenerales = cargarParametrosCreacionCuenta(
					    		Integer.parseInt(datosRegPacEntSubcontratada.get("codigoConvenio").toString()),
					    		datosRegPacEntSubcontratada.get("nombreConvenio").toString(),
					    		Integer.parseInt(datosCuenta.get("codigoViaIngreso").toString()));
					    	
					    	//Se verifica si la carga de los parámetros fue exitosa
					    	exito = UtilidadTexto.getBoolean(datosParametrosGenerales.get("procesoExitoso").toString());
					    	
					    	if(exito)
					    	{
					    	
						    	//4) ******************* SE INSERTA LA CUENTA************************************************
						    	DtoCuentas dtoCuenta = cargarDtoCuenta(datosCuenta,datosRegPacEntSubcontratada,datosParametrosGenerales,desplazado);
						    	
							    //se añade el ingreso al resto de la estructura
								dtoCuenta.setIdIngreso(this.idIngresoCiclo);
								for(int i=0;i<dtoCuenta.getConvenios().length;i++)
									dtoCuenta.getConvenios()[i].setIngreso(idIngreso);
								
								//Se realiza la inserción de la cuenta
								Cuenta mundoCuenta = new Cuenta(dtoCuenta);
								ResultadoBoolean resp2 = mundoCuenta.guardar(con);
								
								this.idCuentaCiclo = resp2.getDescripcion(); //se toma el id de la cuenta recién ingresada
								//Se verifica éxito inserción
								if(resp2.isTrue())
								{
//									Se incrementa el consecutivo de ingreso---------------------------------------------------------------------
									UtilidadBD.cambiarUsoFinalizadoConsecutivo( con, ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
									
									//Se marcan como atendidos los registros de pacientes triage pendientes-----------------------------------
									Utilidades.actualizarPacienteParaTriageVencido(con, mundoPersonaBasica.getCodigoPersona()+"");
									
									//****************INGRESO DE LA ADMISION/EGRESO DE URGENCIAS *************************
									if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
									{
										exito = insertarAdmisionUrgencias(datosCuenta,mundoCuenta,datosParametrosGenerales);
									}
									//********INGRESO DE LA ADMISION DE HOSPITALIZACION ***************************
									else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
									{
										exito = insertarAdmisionHospitalaria(datosCuenta,datosParametrosGenerales);
									}
									//***********************************************************************************************************	
								}
								else
								{
									UtilidadBD.cambiarUsoFinalizadoConsecutivo( con, ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
									exito = false;
									editarInconsistenciaEncabezado(
											this.numeroFacturaCiclo, 
											this.tipoIdentificacionCiclo, 
											this.numeroIdentificacionCiclo, 
											"--", 
											"--", 
											resp2.getDescripcion());
								}
					    	}//Fin if exito de la carga de parámetros generales
					    	//***********************************************************************************************************
					    } //Fin if IdIngreso
					    else
					    {
					    	exito = false;
							UtilidadBD.cambiarUsoFinalizadoConsecutivo( con, ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
							editarInconsistenciaEncabezado(
									this.numeroFacturaCiclo, 
									this.tipoIdentificacionCiclo, 
									this.numeroIdentificacionCiclo, 
									"--", 
									"--", 
									"error en base de datos al registrar el ingreso");
					    }
						
					}
					else
					{
						exito = false;
						UtilidadBD.cambiarUsoFinalizadoConsecutivo( con, ConstantesBD.nombreConsecutivoIngresos, usuarioSession.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								"--", 
								"--", 
								resp1.textoRespuesta);
					}
					///*****************************************************************************************************
				} //fin if exito
			} //fin if resp0
		}//fin if exito
		
		return exito;
	}
	
	/**
	 * 
	 * @param con2
	 * @param datosCuenta
	 * @param datosParametrosGenerales
	 * @return
	 */
	private boolean insertarAdmisionHospitalaria(HashMap<String, Object> datosCuenta, HashMap<String, Object> datosParametrosGenerales) 
	{
		boolean exito = validarDiagnostico(datosCuenta.get("diagPpalIngreso").toString(),ConstantesBD.ripsAH, "diagnóstico principal ingreso");
		
		//Se verifica que el diagnostico exista
		if(exito)
		{
		
		
			//Ingresando la admision de hospitalizacion
			AdmisionHospitalaria admisionHospitalaria = new AdmisionHospitalaria(
					Integer.parseInt(datosCuenta.get("viaIngreso").toString()), 
					Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoHospitalizacion).toString()), //se ingresa profesional de la salud de parametros generales 
					"", //numero de identificacion del medico (No se necesita)
					"", //tipo de identificacion del medico (No se necesita) 
					0, // no se ingresa cama 
					datosCuenta.get("diagPpalIngreso").toString().toUpperCase(), //Diagnostico ingreso 
					tipoCie+"", 
					Integer.parseInt(datosCuenta.get("causaExterna").toString()), //Causa externa   
					/*datosCuenta.get("numeroAutorizacion").toString(),*/ 
					usuarioSession, 
					this.idCuentaCiclo, 
					datosCuenta.get("horaIngreso").toString(),
					datosCuenta.get("fechaIngreso").toString());
			//***************************************************************************************************************
			
			//Se realiza la validacion de la admision hospitalaria
			RespuestaValidacion resp3=UtilidadValidacion.validacionIngresarAdmisionHospitalaria(con, Integer.parseInt(datosCuenta.get("codigoPaciente").toString()), Integer.parseInt(this.idCuentaCiclo), usuarioSession.getCodigoInstitucion());
			
			if (resp3.puedoSeguir)
			{
				//*******************INSERTAR ADMISION HOSPITALARIA********************************************************
				int codigoAdmision = admisionHospitalaria.insertarTransaccional(con, ConstantesBD.continuarTransaccion,Integer.parseInt(usuarioSession.getCodigoInstitucion()));
				//*********************************************************************************************************
				
				if(codigoAdmision>0)
				{
					//****************INGRESAR EL EGRESO DE LA ADMISION DE HOSPITALIZACION************************************
					datosCuenta.put("codigoAdmision",codigoAdmision); //se añade el código de la admision
					exito = generarEgreso(datosCuenta,datosParametrosGenerales,ConstantesBD.ripsAH);				
					//***************************************************************************************************
					
				}
				else
				{
					exito = false;
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"error en base de datos al ingresar la admisión de hospitalización");
				}
					
			} //fin if validacion ingreso admision
			else
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						resp3.textoRespuesta);
			}
		}
		
		
		return exito;
	}
	/**
	 * Insertar admision de urgencias 
	 * @param datosParametrosGenerales 
	 * @return
	 */
	private boolean insertarAdmisionUrgencias(HashMap<String, Object> datosCuenta, Cuenta mundoCuenta, HashMap<String, Object> datosParametrosGenerales) 
	{
		boolean exito = true;
		
		//Verificando que no haya una admision hospitalaria abierta para el paciente en la institucion actual
		RespuestaValidacion resp3=UtilidadValidacion.validacionIngresarAdmisionHospitalaria(
			con, 
			Integer.parseInt(mundoCuenta.getCuenta().getCodigoPaciente()), 
			Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()), 
			usuarioSession.getCodigoInstitucion());
		
		
		if (resp3.puedoSeguir)
		{
			// Ingresando la admisión de urgencias
			AdmisionUrgencias admisionUrg = new AdmisionUrgencias();
		
			admisionUrg.setCodigoOrigen(mundoCuenta.getCuenta().getCodigoOrigenAdmision());
			admisionUrg.setOrigen(mundoCuenta.getCuenta().getDescripcionOrigenAdmision());
			admisionUrg.setCodigoCausaExterna(Integer.parseInt(datosCuenta.get("causaExterna").toString()));
			admisionUrg.setLoginUsuario(usuarioSession);
			admisionUrg.setIdCuenta(Integer.parseInt(mundoCuenta.getCuenta().getIdCuenta()));
			admisionUrg.setFecha(datosCuenta.get("fechaIngreso").toString());
			admisionUrg.setHora(datosCuenta.get("horaIngreso").toString());
			//admisionUrg.setNumeroAutorizacion(datosCuenta.get("numeroAutorizacion").toString());
			admisionUrg.setConsecutivoTriage("");
			admisionUrg.setConsecutivoFechaTriage("");
			admisionUrg.setFechaObservacion(datosCuenta.get("fechaIngreso").toString());
			admisionUrg.setHoraObservacion(datosCuenta.get("horaIngreso").toString());
			admisionUrg.setFechaEgresoObservacion(datosCuenta.get("fechaSalida").toString());
			admisionUrg.setHoraEgresoObservacion(datosCuenta.get("horaSalida").toString());
			
			
			
			int codigoAdmision = admisionUrg.insertarTransaccional(con, ConstantesBD.continuarTransaccion,Integer.parseInt(usuarioSession.getCodigoInstitucion()));
			
			//Se verifica si se insertó la admision correctamente
			if(codigoAdmision>0)
			{
			
				//****************INGRESAR EL EGRESO DE LA ADMISION DE URGENCIAS************************************
				datosCuenta.put("codigoAdmision",codigoAdmision); //se añade el código de la admision
				exito = generarEgreso(datosCuenta,datosParametrosGenerales,ConstantesBD.ripsAU);				
				//***************************************************************************************************
				
			}
			else
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"error en base de datos al registrar la admisión de urgencias");
			}
		}					
		else
		{
			exito = false;
			editarInconsistenciaEncabezado(
					this.numeroFacturaCiclo, 
					this.tipoIdentificacionCiclo, 
					this.numeroIdentificacionCiclo, 
					"--", 
					"--", 
					resp3.textoRespuesta);
		}
		
		return exito;
	}
	
	/**
	 * Método implementado para generar el egreso de urgencias/hospitalizacion
	 * @param datosCuenta
	 * @param datosParametrosGenerales
	 * @param nombreArchivo 
	 * @return
	 */
	private boolean generarEgreso(HashMap<String, Object> datosCuenta, HashMap<String, Object> datosParametrosGenerales, String nombreArchivo) 
	{
		boolean exito = true, puedoIngresar = true;
		int viaIngreso = Integer.parseInt(datosCuenta.get("codigoViaIngreso").toString());
		int  resp = 0;
		UsuarioBasico medico = new UsuarioBasico();
		boolean pacienteMuerto = false;
		
		Egreso mundoEgreso = new Egreso();
		
		//******************************SE CARGA EL MUNDO DE EGRESO*******************************************************
		mundoEgreso.setNumeroCuenta(Integer.parseInt(this.idCuentaCiclo));
		mundoEgreso.setEstadoSalida(datosCuenta.get("estadoSalida").toString().equals("1")?false:true);
		if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Para hospitalizacion se asigna otro
			mundoEgreso.setDestinoSalida(new InfoDatos(ConstantesBD.codigoDestinoSalidaOtro+"","Otro"));
			
			if(!datosCuenta.get("diagMuerte").toString().equals(""))
			{
				pacienteMuerto = true;
				mundoEgreso.setDiagnosticoCausaMuerte(new Diagnostico(datosCuenta.get("diagMuerte").toString().toUpperCase(),tipoCie));
			}
			else
				mundoEgreso.setDiagnosticoCausaMuerte(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			
			mundoEgreso.setDiagnosticoDefinitivoPrincipal(new Diagnostico(datosCuenta.get("diagPpalEgreso").toString().toUpperCase(),tipoCie));
			
			if(!datosCuenta.get("diagRelEgreso1").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_1(new Diagnostico(datosCuenta.get("diagRelEgreso1").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_1(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			if(!datosCuenta.get("diagRelEgreso2").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_2(new Diagnostico(datosCuenta.get("diagRelEgreso2").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_2(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			if(!datosCuenta.get("diagRelEgreso3").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_3(new Diagnostico(datosCuenta.get("diagRelEgreso3").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_3(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			medico.setCodigoPersona(Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoHospitalizacion).toString()));
			mundoEgreso.setMedicoResponsable(medico);
			
			//Se pregunta adicionalmente por el diagnóstico de complicacion
			if(!datosCuenta.get("diagComplicacion").toString().equals(""))
				mundoEgreso.setDiagnosticoComplicacion(new Diagnostico(datosCuenta.get("diagComplicacion").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoComplicacion(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			//Se agrega la fecha/hora de egreso
			mundoEgreso.setFechaEgreso(datosCuenta.get("fechaEgreso").toString());
			mundoEgreso.setHoraEgreso(datosCuenta.get("horaEgreso").toString());
			
		}
		else
		{
			//Para urgencias se debe analizar cual fue el destino de salida de RIPS para traducirlo al valor de Axioma
			int destinoSalida = ConstantesBD.codigoDestinoSalidaOtro;
			int destinoSalidaArchivo = Integer.parseInt(datosCuenta.get("destinoSalida").toString());
			
			if(destinoSalidaArchivo==2) //si el destino salida es Remision a nivel de mayor complejidad 
				destinoSalida = ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad;
			else if(destinoSalidaArchivo==3) //Si el destino salida es hospitalizacion
				destinoSalida = ConstantesBD.codigoDestinoSalidaHospitalizacion;
			
			mundoEgreso.setDestinoSalida(new InfoDatos(destinoSalida+"",""));
			
			if(!datosCuenta.get("causaMuerte").toString().equals(""))
			{
				mundoEgreso.setDiagnosticoCausaMuerte(new Diagnostico(datosCuenta.get("causaMuerte").toString().toUpperCase(),tipoCie));
				pacienteMuerto = true;
				
			}
			else
				mundoEgreso.setDiagnosticoCausaMuerte(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			mundoEgreso.setDiagnosticoDefinitivoPrincipal(new Diagnostico(datosCuenta.get("diagSalida").toString().toUpperCase(),tipoCie));
			
			if(!datosCuenta.get("diagRelSalida1").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_1(new Diagnostico(datosCuenta.get("diagRelSalida1").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_1(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			if(!datosCuenta.get("diagRelSalida2").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_2(new Diagnostico(datosCuenta.get("diagRelSalida2").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_2(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			if(!datosCuenta.get("diagRelSalida3").toString().equals(""))
				mundoEgreso.setDiagnosticoRelacionado_3(new Diagnostico(datosCuenta.get("diagRelSalida3").toString().toUpperCase(),tipoCie));
			else
				mundoEgreso.setDiagnosticoRelacionado_3(new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado));
			
			medico.setCodigoPersona(Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoUrgencias).toString()));
			mundoEgreso.setMedicoResponsable(medico);
			
			//Se agrega la fecha/hora de egreso
			mundoEgreso.setFechaEgreso(datosCuenta.get("fechaSalida").toString());
			mundoEgreso.setHoraEgreso(datosCuenta.get("horaSalida").toString());
		}
		
		//**************VALIDACION DE LOS DIAGNOSTICOS*****************************************************
		//Diagnostico de muerte
		if(!mundoEgreso.getDiagnosticoCausaMuerte().getAcronimo().equals("")&&!mundoEgreso.getDiagnosticoCausaMuerte().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
			if(!validarDiagnostico(mundoEgreso.getDiagnosticoCausaMuerte().getAcronimo(), nombreArchivo, "diagnóstico muerte"))
				puedoIngresar = false;
		
		//Diagnostico de salida
		if(!validarDiagnostico(mundoEgreso.getDiagnosticoDefinitivoPrincipal().getAcronimo(), nombreArchivo, nombreArchivo.equals(ConstantesBD.ripsAH)?"diagnóstico principal egreso":"diagnóstico a la salida"))
			puedoIngresar = false;
			
		//Diagnostico complicacion
		if(!mundoEgreso.getDiagnosticoComplicacion().getAcronimo().equals("")&&!mundoEgreso.getDiagnosticoComplicacion().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
			if(!validarDiagnostico(mundoEgreso.getDiagnosticoComplicacion().getAcronimo(), nombreArchivo, "diagnóstico complicación"))
				puedoIngresar = false;
		
		//Diagnostico relacionado 1
		if(!mundoEgreso.getDiagnosticoRelacionado_1().getAcronimo().equals("")&&!mundoEgreso.getDiagnosticoRelacionado_1().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
			if(!validarDiagnostico(mundoEgreso.getDiagnosticoRelacionado_1().getAcronimo(), nombreArchivo, "diagnóstico relacionado 1"))
				puedoIngresar = false;
		
		//Diagnostico relacionado 2
		if(!mundoEgreso.getDiagnosticoRelacionado_2().getAcronimo().equals("")&&!mundoEgreso.getDiagnosticoRelacionado_2().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
			if(!validarDiagnostico(mundoEgreso.getDiagnosticoRelacionado_2().getAcronimo(), nombreArchivo, "diagnóstico relacionado 2"))
				puedoIngresar = false;
		
		//Diagnostico relacionado 3
		if(!mundoEgreso.getDiagnosticoRelacionado_3().getAcronimo().equals("")&&!mundoEgreso.getDiagnosticoRelacionado_3().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
			if(!validarDiagnostico(mundoEgreso.getDiagnosticoRelacionado_3().getAcronimo(), nombreArchivo, "diagnóstico relacionado 3"))
				puedoIngresar = false;
		
		//**************************************************************************************************
		
		mundoEgreso.setOtroDestinoSalida(""); //no aplica
		mundoEgreso.setCausaExterna(new InfoDatos("0","No seleccionada"));
		mundoEgreso.setFechaGrabacionEgreso(UtilidadFecha.getFechaActual(con));
		mundoEgreso.setHoraGrabacionEgreso(UtilidadFecha.getHoraActual(con));
		mundoEgreso.setUsuarioGrabaEgreso(usuarioSession);
		//****************************************************************************************************************************
		
		if(puedoIngresar)
		{
			//Se ingresa el registro inicial del egreso
			resp = mundoEgreso.crearEgresoSinEvolucionTransaccional(con, ConstantesBD.continuarTransaccion);
			
			if(resp>0)
			{
				//Se actualiza el paciente como muerto si se murió
				if(pacienteMuerto&&!UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,Integer.parseInt(datosCuenta.get("codigoPaciente").toString()),false,"", "", "",ConstantesBD.continuarTransaccion))
					resp = 0;
				
				
				
				if(resp>0)
				{
					//Se realiza la finalización del egreso
					try 
					{
						resp = mundoEgreso.modificarEgresoUsuarioFinalizar(con, ConstantesBD.continuarTransaccion);
					} 
					catch (SQLException e) 
					{
						resp = 0;
						
					}
					
					
					//Se verifica si se insertó correctamente
					if(resp>0)
					{
						//Si es hospitalizacion se debe finalizar la admision
						if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
						{
							AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
							admisionHospitalaria.init(System.getProperty("TIPOBD"));
							try 
							{
								resp=admisionHospitalaria.actualizarPorEgresoTransaccional(con, Integer.parseInt(datosCuenta.get("codigoAdmision").toString()), ConstantesBD.continuarTransaccion, usuarioSession.getCodigoInstitucionInt());
							} 
							catch (Exception e) 
							{
								resp = 0;
								exito = false;
								editarInconsistenciaEncabezado(
										this.numeroFacturaCiclo, 
										this.tipoIdentificacionCiclo, 
										this.numeroIdentificacionCiclo, 
										"--", 
										"--", 
										"error en base de datos al cerrar admision hospitalaria "+e);
							} 
							
							if(resp<=0)
							{
								exito = false;
								editarInconsistenciaEncabezado(
										this.numeroFacturaCiclo, 
										this.tipoIdentificacionCiclo, 
										this.numeroIdentificacionCiclo, 
										"--", 
										"--", 
										"error en base de datos al cerrar admision hospitalaria ");
							}
						}
						
					}
					else
					{
						exito = false;
						editarInconsistenciaEncabezado(
								this.numeroFacturaCiclo, 
								this.tipoIdentificacionCiclo, 
								this.numeroIdentificacionCiclo, 
								"--", 
								"--", 
								"error en base de datos al registrar el egreso");
					}
					
				}
				else
				{
					exito = false;
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"error en base de datos al actualizar el paciente como muerto");
				}
			}
			else
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						"error en base de datos al registrar el egreso");
			}
		}
		else
			exito = false;
		
		return exito;
	}
	/**
	 * Método para cargar los parámetros generales necesarios para la creación de la cuenta
	 * @param codigoConvenio
	 * @param nombreConvenio 
	 * @param codigoViaIngreso
	 * @return
	 */
	private HashMap<String, Object> cargarParametrosCreacionCuenta(int codigoConvenio, String nombreConvenio, int codigoViaIngreso) 
	{
		HashMap<String, Object> parametrosGenerales = new HashMap<String, Object>();
		String valor = "";
		boolean exito = true;
		String nombreViaIngreso = Utilidades.obtenerNombreViaIngreso(con, codigoViaIngreso);
		//Se listan los parámtros que se necesitan
		String[] parametros = {
			ConstantesIntegridadDominio.acronimoMontoCobroXViaIngreso+ConstantesBD.separadorSplit+"el monto de cobro", //A) monto de cobro
			ConstantesIntegridadDominio.acronimoTipoPacienteXViaIngreso+ConstantesBD.separadorSplit+"el tipo de paciente", //B) tipo paciente
			ConstantesIntegridadDominio.acronimoOrigenAdminisionXViaIngreso+ConstantesBD.separadorSplit+"el origen de admisión", //C) origen admision
			ConstantesIntegridadDominio.acronimoAreaIngresoPacientesXViaIngreso+ConstantesBD.separadorSplit+"el área" //D) Área
		};
		
		//Iteracion de cada uno de los parámetros
		for(int i=0;i<parametros.length;i++)
		{
			//Se toma el acrónimo y mensaje de cada parámetro
			String[] parametro = parametros[i].split(ConstantesBD.separadorSplit);
			
			//Se consulta el valor
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
				con, 
				ConstantesBD.tipoInformacionIngresoCuenta, 
				parametro[0], 
				usuarioSession.getCodigoInstitucionInt(), 
				codigoConvenio, 
				codigoViaIngreso,
				parametro[0].equals(ConstantesIntegridadDominio.acronimoAreaIngresoPacientesXViaIngreso)?usuarioSession.getCodigoCentroAtencion():0);
			
			if(parametro[0].equals(ConstantesIntegridadDominio.acronimoAreaIngresoPacientesXViaIngreso))
			{
				logger.info("EL ÁREA DEL INGRESO TIENE EL CÓDIGO=> "+valor);
			}
			
			//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
			if(valor.equals(""))
			{
				exito = false;
				editarInconsistenciaEncabezado(
						this.numeroFacturaCiclo, 
						this.tipoIdentificacionCiclo, 
						this.numeroIdentificacionCiclo, 
						"--", 
						"--", 
						parametro[1]+" es requerido para el convenio "+nombreConvenio+" y vía de ingreso "+nombreViaIngreso);
			}
			else
				parametrosGenerales.put(parametro[0], valor);
		}	
		
		//Se busca parámetro del medico para el egreso de urgencias
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//Se consulta el valor
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.parametrosGenerales, 
					ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoUrgencias, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
				//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
				if(valor.equals(""))
				{
					exito = false;
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"el médico del egreso es requerido");
				}
				else
					parametrosGenerales.put(ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoUrgencias, valor);
		}
		
		//Se busca parámetro del medico para la admision/egreso de hospitalizacion
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Se consulta el valor
			valor = UtilidadesManejoPaciente.getValorParametroGeneralPlanosEntidadSubcontratada(
					con, 
					ConstantesBD.parametrosGenerales, 
					ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoHospitalizacion, 
					usuarioSession.getCodigoInstitucionInt(), 
					0, 
					0,
					0);
				
				//Si el valor no existe se genera inconsistencia porque son datos requeridos para la cuenta
				if(valor.equals(""))
				{
					exito = false;
					editarInconsistenciaEncabezado(
							this.numeroFacturaCiclo, 
							this.tipoIdentificacionCiclo, 
							this.numeroIdentificacionCiclo, 
							"--", 
							"--", 
							"el médico de la admisión/egreso es requerido");
				}
				else
					parametrosGenerales.put(ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoHospitalizacion, valor);
		}
		
		parametrosGenerales.put("procesoExitoso",exito?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return parametrosGenerales;
	}
	/**
	 * Método implementado para cargar el dto de la cuenta
	 * @param datosCuenta
	 * @param datosRegPacEntSubcontratada
	 * @param datosParametrosGenerales 
	 * @param desplazado 
	 * @return
	 */
	private DtoCuentas cargarDtoCuenta(HashMap<String, Object> datosCuenta, HashMap<String, Object> datosRegPacEntSubcontratada, HashMap<String, Object> datosParametrosGenerales, boolean desplazado) throws IPSException 
	{
		DtoCuentas dtoCuenta = new DtoCuentas();
		Contrato mundoContrato = new Contrato();
		MontosCobro mundoMontoCobro = new MontosCobro();
		String[] fechaHoraApertura = obtenerFechaHoraIngreso(datosCuenta);
		
		DtoSubCuentas[] dtoSubCuentas = new DtoSubCuentas[1];
		
		//1) Se llenan los datos de la cuenta
		dtoCuenta.setCodigoViaIngreso(Integer.parseInt(datosCuenta.get("codigoViaIngreso").toString()));
		dtoCuenta.setHospitalDia(false);
		dtoCuenta.setCodigoPaciente(datosCuenta.get("codigoPaciente").toString());
		dtoCuenta.setCodigoEstado(ConstantesBD.codigoEstadoCuentaActiva);
		dtoCuenta.setDesplazado(false);
		dtoCuenta.setFechaApertura(fechaHoraApertura[0]);
		dtoCuenta.setHoraApertura(fechaHoraApertura[1]);
		dtoCuenta.setLoginUsuario(usuarioSession.getLoginUsuario());
		dtoCuenta.setCodigoTipoEvento(""); //no se asigna información
		/*Se añade en la cuenta el campo Desplazado. Por Anexo 600.
		Se valida que venga en true para actualizar el campo Desplazado.
		Sino se deja tal y como esta*/
		if(desplazado)
			dtoCuenta.setDesplazado(desplazado);
		
		dtoCuenta.setCodigoTipoPaciente(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoTipoPacienteXViaIngreso).toString());
		if(dtoCuenta.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
			dtoCuenta.setCodigoOrigenAdmision(Integer.parseInt(datosCuenta.get("viaIngreso").toString()));
		else
			dtoCuenta.setCodigoOrigenAdmision(Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoOrigenAdminisionXViaIngreso).toString()));
		dtoCuenta.setCodigoArea(Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoAreaIngresoPacientesXViaIngreso).toString()));
		this.codigoAreaCiclo = dtoCuenta.getCodigoArea(); //se almacena el codigo del área de la cuenta porque sirve para generar cargos
		logger.info("CODIGO AREA CICLO==============> "+this.codigoAreaCiclo);
		
		//2) Se llena los datos del convenio principal
		dtoSubCuentas[0] = new DtoSubCuentas();
		this.codigoConvenioCiclo = Integer.parseInt(datosRegPacEntSubcontratada.get("codigoConvenio").toString()); //se almacena el codigo del convenio porque sirve para generar cargos
		String[] datosTipoRegimen = Utilidades.obtenerTipoRegimenConvenio(con, this.codigoConvenioCiclo+"").split("-");
		dtoSubCuentas[0].setConvenio(new InfoDatosInt(this.codigoConvenioCiclo,datosRegPacEntSubcontratada.get("nombreConvenio").toString()));
		dtoSubCuentas[0].setCodigoTipoRegimen(datosTipoRegimen[0]);
		
		dtoSubCuentas[0].setContrato(Integer.parseInt(datosRegPacEntSubcontratada.get("contrato").toString()));
		
		//crear subcuenta.
		mundoContrato.cargar(con, dtoSubCuentas[0].getContrato()+"");
		
		
		dtoSubCuentas[0].setFechaAfiliacion(""); //no se asigna informacion
		
		mundoMontoCobro.setCodigo(Integer.parseInt(datosParametrosGenerales.get(ConstantesIntegridadDominio.acronimoMontoCobroXViaIngreso).toString()));
		mundoMontoCobro.cargar(con);
		dtoSubCuentas[0].setClasificacionSocioEconomica(mundoMontoCobro.getEstratoSocial()); 
		dtoSubCuentas[0].setTipoAfiliado(mundoMontoCobro.getTipoAfiliado());
		dtoSubCuentas[0].setMontoCobro(mundoMontoCobro.getCodigo());
		dtoSubCuentas[0].setNaturalezaPaciente(ConstantesBD.codigoNaturalezaPacientesNinguno);
		dtoSubCuentas[0].setNroCarnet("");
		dtoSubCuentas[0].setNroPoliza("");
		dtoSubCuentas[0].setNroAutorizacion(datosRegPacEntSubcontratada.get("numeroAutorizacion").toString());
		dtoSubCuentas[0].setCodigoPaciente(Integer.parseInt(dtoCuenta.getCodigoPaciente()));
		dtoSubCuentas[0].setFacturado(ConstantesBD.acronimoNo);
		dtoSubCuentas[0].setNroPrioridad(1);
		dtoSubCuentas[0].setLoginUsuario(usuarioSession.getLoginUsuario());
		dtoSubCuentas[0].setNumeroSolicitudVolante("");
		dtoSubCuentas[0].setSubCuentaPoliza(false);
		dtoSubCuentas[0].setSubCuentaVerificacionDerechos(false);
		dtoCuenta.setTieneResponsablePaciente(false);
		
		dtoCuenta.setConvenios(dtoSubCuentas);
		
		return dtoCuenta;
	}
	/**
	 * Método para obtener la fecha de ingreso dependiendo de la vía de ingreso
	 * @param datosCuenta
	 * @return
	 */
	private String[] obtenerFechaHoraIngreso(HashMap<String, Object> datosCuenta) 
	{
		String fechaIngreso = "";
		String horaIngreso = "";
		int viaIngreso = Utilidades.convertirAEntero(datosCuenta.get("codigoViaIngreso").toString(),true);
		
		if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion||viaIngreso == ConstantesBD.codigoViaIngresoUrgencias)
		{
			fechaIngreso = datosCuenta.get("fechaIngreso").toString().trim();
			horaIngreso = datosCuenta.get("horaIngreso").toString().trim();
		}
		else if(viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			fechaIngreso = datosCuenta.get("fechaConsulta").toString().trim();
			horaIngreso = UtilidadFecha.getHoraActual(con);
		}
		else
		{
			if(UtilidadCadena.noEsVacio(datosCuenta.get("fechaProcedimiento")+""))
				fechaIngreso = datosCuenta.get("fechaProcedimiento").toString().trim();
			else
				fechaIngreso = this.fechaExpedicionCiclo;
			
			horaIngreso = UtilidadFecha.getHoraActual(con);
		}
			
		
			
	
		String[] fechaHoraIngreso = {fechaIngreso,horaIngreso};
 		return fechaHoraIngreso;
	}
	/**
	 * Método que verifica cual es la vía de ingreso de una factura
	 * @return
	 */
	private HashMap<String, Object> verificarViaIngresoFactura() 
	{
		HashMap<String, Object> datosCuenta = new HashMap<String, Object>();
		int viaIngreso = ConstantesBD.codigoNuncaValido;
		
		//Se verifica si la factura es una admision hopsitalaria
		datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAH, false);
		
		//Si no es hospitalaria se verifica si es urgencias
		if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
		{
			//Se verifica si la factura es una admision de urgencias
			datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAU, false);
			
			//Si no es urgencias se verifica si es de consulta externa
			if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
			{
				//Se verifica si la factura es de consulta externa si tiene registros en el AC
				datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAC, false);
				
				//Si no es consulta externa se verifica si es de ambulatorios
				if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
				{
					//Se verifica si la factura es de ambulatorios si tiene registros en el AP
					datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAP, false);
					
					//Si no está en el AP se verifica en el AT
					if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
					{
						//Se verifica si la factura es de ambulatorios si tiene registros en el AT
						datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAT, false);
						
						//Si no está en el AT se verifica en el AM
						if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
						{
							//Se verifica si la factura es de ambulatorios si tiene registros en el AM
							datosCuenta = consultarInformacionArchivo(con, ConstantesBD.ripsAM, false);
							
							//Si no está en el AM se genera inconsistencia
							if(Integer.parseInt(datosCuenta.get("numRegistros").toString())<=0)
							{
								editarInconsistenciaEncabezado(
									this.numeroFacturaCiclo,
									this.tipoIdentificacionCiclo, 
									this.numeroIdentificacionCiclo, 
									"--", 
									"--", 
									"factura sin detalle de servicios/articulos");
							}
							else
								viaIngreso = ConstantesBD.codigoViaIngresoAmbulatorios;
						}
						else
							viaIngreso = ConstantesBD.codigoViaIngresoAmbulatorios;
					}
					else
						viaIngreso = ConstantesBD.codigoViaIngresoAmbulatorios;
				}
				else
					viaIngreso = ConstantesBD.codigoViaIngresoConsultaExterna;
			}
			else
				viaIngreso = ConstantesBD.codigoViaIngresoUrgencias;
		}
		else
			viaIngreso = ConstantesBD.codigoViaIngresoHospitalizacion;
		
		
		datosCuenta.put("codigoViaIngreso", viaIngreso+"");
		return datosCuenta;
	}
	/**
	 * Consultar información AH
	 * @param con
	 * @param archivo
	 * @param manejarIndices
	 * @return
	 */
	private HashMap<String, Object> consultarInformacionArchivo(Connection con,String archivo,boolean manejarIndices)
	{
		HashMap campos = new HashMap();
		campos.put("numeroFactura", this.numeroFacturaCiclo);
		campos.put("tipoIdentificacion", this.tipoIdentificacionCiclo);
		campos.put("numeroIdentificacion", this.numeroIdentificacionCiclo);
		campos.put("archivo", archivo);
		campos.put("manejarIndices", manejarIndices+"");
		campos.put("usuario", usuarioSession.getLoginUsuario());
		campos.put("codigoCentroAtencion", usuarioSession.getCodigoCentroAtencion());
		return lecturaDao.consultarInformacionArchivo(con, campos);
	}
	
	/**
	 * Método que consulta el consecutivo del registro del paciente en entidad subcontratada
	 * que concuerda con los datos encontrados en los archivos RIPS de la factura
	 * @param con
	 * @param numeroAutorizacion
	 * @return
	 */
	private HashMap<String, Object> getDatosRegPacEntidadSubcontratada(Connection con,String numeroAutorizacion)
	{
		HashMap campos = new HashMap();
		campos.put("tipoIdentificacion",this.tipoIdentificacionCiclo);
		campos.put("consecutivoEntidadSubcontratada", this.consecutivoEntidadSubcontratada);
		campos.put("numeroIdentificacion",this.numeroIdentificacionCiclo);
		campos.put("numeroAutorizacion",numeroAutorizacion);
		campos.put("validarConCarnet", this.validacionNumeroCarnet);
		campos.put("codigoCentroAtencion", usuarioSession.getCodigoCentroAtencion());
		return lecturaDao.getDatosRegPacEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método implementado para realizar las validaciones generales y de estructura de los arhicvos
	 * de la lectura de planos pacientes entidades subcontratadas
	 *
	 */
	private void validacionesGeneralesEstructuraArchivos() 
	{
		this.cancelarProceso = false;
		for(int i=0;i<this.archivos.size();i++)
		{
			logger.info("CODIGO DE ARCHIVO A VALIDAR=> "+i);
			//Para diferenciar que archivo se está validando
			switch(i)
			{
				case 0: //Archivo CT
					this.validacionesGeneralesCT(i);
				break;
				case 1: //Archivo AF
					this.validacionesGeneralesAF(i);
				break;
				case 2: //Archivo AD
					this.validacionesGeneralesAD(i);
				break;
				case 3: //Archivo AC
					this.validacionesGeneralesAC(i);
				break;
				case 4: //Archivo AP
					this.validacionesGeneralesAP(i);
				break;
				case 5: //Archivo AH
					this.validacionesGeneralesAH(i);
				break;
				case 6: //Archivo AU
					this.validacionesGeneralesAU(i);
				break;
				case 7: //Archivo AM
					this.validacionesGeneralesAM(i);
				break;
				case 8: //Archivo AN
					this.validacionesGeneralesAN(i);
				break;
				case 9: //Archivo AT
					this.validacionesGeneralesAT(i);
				break;
				case 10: //Archivo US
					this.validacionesGeneralesUS(i);
				break;
			}
		}
		
		//Se verifica si hubo inconsistencias
		if(this.inconsistencias.size()>0)
			this.huboInconsistencias = true;
		
	}
	
	/**
	 * Método para realizar las validaciones del archivo US
	 * @param pos
	 */
	private void validacionesGeneralesUS(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		String numeroFactura = ""; //factura relacionada al paciente
		
		File archivo = (File)this.archivos.get(pos);
		
		try
		{
			String cadena="";
			int contador = 0;
			String[] campos = new String[0];
			boolean continuar = false;
			//******SE INICIALIZA ARCHIVO*************************
			
			FileReader stream=new FileReader(archivo); 
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				continuar = false;
				numeroFactura = "";
				
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<14||campos.length>14||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=13)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
					elementoInconsistencia.put("campo_"+posIncon,"---");
					elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
					posIncon++;
				}
				else
				{
					//Se consulta alguna factura relacionada con el paciente
					ArrayList<String> facturas = lecturaDao.consultarFacturasPaciente(con, campos[0], campos[1],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
					
					//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
					if(!this.numeroFactura.equals(""))
					{
						for(int i=0;i<facturas.size();i++)
						{
							numeroFactura = (String)facturas.get(i);
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(numeroFactura))
								continuar = true;
						}
						
					}
					else
					{
						//En el caso de que exista factura se toma el numero de la factura
						for(int i=0;i<facturas.size();i++)
						{
							numeroFactura = (String)facturas.get(i);
							i = facturas.size(); //se toma siempre la primera factura
						}
						continuar = true;
					}
					
					//Se inicializa el indicativo de inconsistencia x registro
					this.huboInconsistenciaXRegistro = false;
					
					//Si no se puede continuar se salta el registro
					if(continuar)
					{
						//1) ******** TIPO DE IDENTIFICACION ********************************************
						elementoInconsistencia = validacionGeneralCampo(campos[0],"tipo de identificación",2,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
						if(!campos[0].trim().equals("")&&!campos[0].equals("CC")&&!campos[0].equals("RC")&&!campos[0].equals("CE")&&!campos[0].equals("PA")&&
							!campos[0].equals("TI")&&!campos[0].equals("AS")&&!campos[0].equals("MS")&&!campos[0].equals("NU"))
						{
							//Si no hay factura para el registro es inconsistencia sin encabezado
							if(numeroFactura.equals(""))
							{
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"tipo de identificación");
								elementoInconsistencia.put("observaciones_"+posIncon,"datos inválidos (linea "+contador+")");
								posIncon++;
							}
							//de lo contrario es inconsistencia con encabezado
							else
								editarInconsistenciaEncabezado(numeroFactura, campos[0], campos[1], ConstantesBD.ripsUS, "tipo de identificación", "datos inválidos (linea "+contador+")");
						}
						
						//2) ******** NÚMERO DE IDENTIFICACIÓN************************************
						elementoInconsistencia = validacionGeneralCampo(campos[1],"número de identificación",20,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//3) ******* CÓDIGO ENTIDAD ADMINISTRADORA **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[2],"código entidad administradora",6,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//4) ******* TIPO DE USUARIO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[3],"tipo de usuario",1,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que el tipo de usuario sea válido
						if(!campos[3].trim().equals("")&&!campos[3].equals("1")&&!campos[3].equals("2")&&!campos[3].equals("3")&&!campos[3].equals("4")&&!campos[3].equals("5")&&!campos[3].equals("6")&&!campos[3].equals("7")&&!campos[3].equals("8"))
						{
							//Si no hay factura para el registro es inconsistencia sin encabezado
							if(numeroFactura.equals(""))
							{
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"tipo de usuario");
								elementoInconsistencia.put("observaciones_"+posIncon,"datos inválidos (linea "+contador+")");
								posIncon++;
							}
							//de lo contrario es inconsistencia con encabezado
							else
								editarInconsistenciaEncabezado(numeroFactura, campos[0], campos[1], ConstantesBD.ripsUS, "tipo de usuario", "datos inválidos (linea "+contador+")");
						}
						
						//5) ******* PRIMER APELLIDO USUARIO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[4],"primer apellido usuario",30,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//6) ******* SEGUNDO APELLIDO USUARIO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[5],"segundo apellido usuario",30,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,false,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//7) ******* PRIMER NOMBRE USUARIO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[6],"primer nombre usuario",20,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//8) ******* SEGUNDO NOMBRE USUARIO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[7],"segundo nombre usuario",20,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,false,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//9) ******* EDAD **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[8],"edad",3,ConstantesBD.ripsUS,contador,true,false,false,false,elementoInconsistencia,posIncon,false,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//10) ******* UNIDAD DE MEDIDA EDAD **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[9],"unidad de medida edad",1,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,false,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica la unidad de medida de la edad si se ingresó sea válida
						if(!campos[9].trim().equals("")&&!campos[9].equals("1")&&!campos[9].equals("2")&&!campos[9].equals("3"))
						{
							//Si no hay factura para el registro es inconsistencia sin encabezado
							if(numeroFactura.equals(""))
							{
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"unidad de medida edad");
								elementoInconsistencia.put("observaciones_"+posIncon,"datos inválidos (linea "+contador+")");
								posIncon++;
							}
							//de lo contrario es inconsistencia con encabezado
							else
								editarInconsistenciaEncabezado(numeroFactura, campos[0], campos[1], ConstantesBD.ripsUS, "unidad de medida edad", "datos inválidos (linea "+contador+")");
						}
						
						//11) ******* SEXO **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[10],"sexo",1,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que el sexo sea válido
						if(!campos[10].trim().equals("")&&!campos[10].equals("M")&&!campos[10].equals("F"))
						{
							//Si no hay factura para el registro es inconsistencia sin encabezado
							if(numeroFactura.equals(""))
							{
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"sexo");
								elementoInconsistencia.put("observaciones_"+posIncon,"datos inválidos (línea "+contador+")");
								posIncon++;
							}
							//de lo contrario es inconsistencia con encabezado
							else
								editarInconsistenciaEncabezado(numeroFactura, campos[0], campos[1], ConstantesBD.ripsUS, "sexo", "datos inválidos (línea "+contador+")");
						}
						
						//12) ******* CODIGO DEPTO RESIDENCIA **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[11],"código depto residencia",2,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//13) ******* CODIGO MUNICIPIO RESIDENCIA **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[12],"código municipio residencia",3,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//14) ******* ZONA DE RESIDENCIA **************************************
						elementoInconsistencia = validacionGeneralCampo(campos[13],"zona de residencia",1,ConstantesBD.ripsUS,contador,false,false,false,false,elementoInconsistencia,posIncon,true,numeroFactura.equals("")?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi,numeroFactura,campos[0],campos[1]);
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que la zona de residencia sea válida
						if(!campos[13].trim().equals("")&&!campos[13].equals("U")&&!campos[13].equals("R"))
						{
							//Si no hay factura para el registro es inconsistencia sin encabezado
							if(numeroFactura.equals(""))
							{
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"zona de residencia");
								elementoInconsistencia.put("observaciones_"+posIncon,"datos inválidos (línea "+contador+")");
								posIncon++;
							}
							//de lo contrario es inconsistencia con encabezado
							else
								editarInconsistenciaEncabezado(numeroFactura, campos[0], campos[1], ConstantesBD.ripsUS, "zona de residencia", "datos inválidos (línea "+contador+")");
						}
						
						//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
						if(!this.huboInconsistenciaXRegistro)
						{
							//Antes de hacer la insercion del registro se verifica si el usuario tenía facturas asociadas
							if(facturas.size()>0)
							{
								if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsUS,false)<=0)
									errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo US"));
							}
							else
							{
								//Se genera inconsistencia
								elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
								elementoInconsistencia.put("campo_"+posIncon,"---");
								elementoInconsistencia.put("observaciones_"+posIncon,"el paciente "+campos[0].trim()+"-"+campos[1].trim()+" no tiene facturas asociadas (línea "+contador+")");
								posIncon++;
							}
								
						}
						//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
						else
							lecturaDao.actualizarFacturaComoInconsistencia(con, numeroFactura,usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
					} //Fin IF continuar
				}  //fin else IF 
					
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
			if(this.numeroFactura.equals(""))
			{
				//Si no son los mismos registros del CT se genera inconsistencia
				if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsUS)+"", true)!=contador)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
					elementoInconsistencia.put("campo_"+posIncon,"total registros");
					elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsUS);
					posIncon++;
					this.cancelarProceso = true;
				}
			}
			
			//Se verifica si hubo inconsistencias sin encabezado
			elementoInconsistencia.put("numRegistros", posIncon+"");
			if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
				this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
		
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AT
	 * @param pos
	 */
	private void validacionesGeneralesAT(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<11||campos.length>11||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=10)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAT);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAT, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** NÚMERO DE AUTORIZACIÓN ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"número de autorización",15,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** TIPO DE SERVICIO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"tipo de servicio",1,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se valida que el tipo de servicio sea correcto
							if(!campos[5].trim().equals("")&&!campos[5].equals("1")&&!campos[5].equals("2")&&!campos[5].equals("3")&&!campos[5].equals("4"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAT, "tipo de servicio", "datos inválidos (línea "+contador+")");
							
							//7) ******** CÓDIGO DEL SERVICIO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"código del servicio",20,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,(campos[5].equals("2")||campos[5].equals("3")||campos[5].equals("4"))?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** NOMBRE DEL SERVICIO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"nombre del servicio",60,ConstantesBD.ripsAT,contador,false,false,false,false,elementoInconsistencia,posIncon,(campos[6].trim().equals("")&&campos[5].equals("1"))?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//9) ******** CANTIDAD ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"cantidad",5,ConstantesBD.ripsAT,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//10) ******** VALOR UNITARIO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"valor unitario",15,ConstantesBD.ripsAT,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** VALOR TOTAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"valor total",15,ConstantesBD.ripsAT,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//Para hacer la inserción del registro en la tabla de AT la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{	
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAT,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AT"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAT);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAT)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAT);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AN
	 * @param pos
	 */
	private void validacionesGeneralesAN(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<14||campos.length>14||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=13)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAN);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION DE LA MADRE********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación de la madre",2,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAN, "tipo de identificación de la madre", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN DE LA MADRE************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"nro de identificación de la madre",20,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** FECHA NACIMIENTO RECIÉN NACIDO ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"fecha nacimiento recién nacido",10,ConstantesBD.ripsAN,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** HORA NACIMIENTO RECIÉN NACIDO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"hora nacimiento recién nacido",5,ConstantesBD.ripsAN,contador,false,false,false,true,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** EDAD GESTACIONAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"edad gestacional",2,ConstantesBD.ripsAN,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** CONTROL PRENATAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"control prenatal",1,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se valida que el control prental sea correcto
							if(!campos[7].trim().equals("")&&!campos[7].equals("1")&&!campos[7].equals("2"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAN, "control prenatal", "datos inválidos (línea "+contador+")");
							
							//9) ******** SEXO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"sexo",1,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se valida que el sexo sea correcto
							if(!campos[8].trim().equals("")&&!campos[8].equals("M")&&!campos[8].equals("F"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAN, "sexo", "datos inválidos (línea "+contador+")");
							
							//10) ******** PESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"peso",4,ConstantesBD.ripsAN,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** DIAGNÓSTICO RECIÉN NACIDO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"diagnóstico recién nacido",4,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** CAUSA BÁSICA DE MUERTE ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"causa básica de muerte",4,ConstantesBD.ripsAN,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** FECHA MUERTE RECIÉN NACIDO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"fecha muerte recién nacido",10,ConstantesBD.ripsAN,contador,false,false,true,false,elementoInconsistencia,posIncon,campos[11].trim().equals("")?false:true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//14) ******** HORA MUERTE RECIÉN NACIDO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"hora muerte recién nacido",5,ConstantesBD.ripsAN,contador,false,false,false,true,elementoInconsistencia,posIncon,campos[11].trim().equals("")?false:true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AN? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AN la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAN,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AN"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAN);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
									
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAN)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAN);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AU
	 * @param pos
	 */
	private void validacionesGeneralesAM(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<14||campos.length>14||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=13)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAM);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAM, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** NÚMERO DE AUTORIZACION ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"número de autorización",15,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** CODIGO DEL MEDICAMENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"código del medicamento",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[6].equals("1")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** TIPO DEL MEDICAMENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"tipo del medicamento",1,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de medicamento sea válido
							if(!campos[6].trim().equals("")&&!campos[6].equals("1")&&!campos[6].equals("2"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAM, "tipo del medicamento", "datos inválidos (línea "+contador+")");
							
							//8) ******** NOMBRE GENÉRICO DEL MEDICAMENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"nombre del medicamento",30,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//9) ******** FORMA FARMACEUTICA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"forma farmacéutica",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[6].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//10) ******** CONCENTRACIÓN DEL MEDICAMENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"concentración del medicamento",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[6].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** UNIDAD DE MEDIDA DEL MEDICAMENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"unidad de medida",20,ConstantesBD.ripsAM,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[6].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** NÚMERO DE UNIDADES ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"número de unidades",5,ConstantesBD.ripsAM,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** VALOR UNITARIO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"valor unitario",15,ConstantesBD.ripsAM,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//17) ******** VALOR TOTAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"valor total",15,ConstantesBD.ripsAM,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AM? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AC la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAM,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AM"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAM);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAM)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAM);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	
	/**
	 * Método para realizar las validaciones del archivo AU
	 * @param pos
	 */
	private void validacionesGeneralesAU(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAU);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAU, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** FECHA DE INGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"fecha de ingreso",10,ConstantesBD.ripsAU,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** HORA DE INGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"hora de ingreso",5,ConstantesBD.ripsAU,contador,false,false,false,true,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** NÚMERO DE AUTORIZACION ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"número de autorización",15,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** CAUSA EXTERNA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"causa externa",2,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la causa externa sea válida
							if(!campos[7].trim().equals("")&&!campos[7].equals("01")&&!campos[7].equals("02")&&!campos[7].equals("03")&&!campos[7].equals("04")&&
									!campos[7].equals("05")&&!campos[7].equals("06")&&!campos[7].equals("07")&&!campos[7].equals("08")&&
									!campos[7].equals("09")&&!campos[7].equals("10")&&!campos[7].equals("11")&&!campos[7].equals("12")&&
									!campos[7].equals("13")&&!campos[7].equals("14")&&!campos[7].equals("15"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAU, "causa externa", "datos inválidos (línea "+contador+")");
							
							//9) ******** DIAGNOSTICO A LA SALIDA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"diagnóstico a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//10) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"diagnóstico rel. 1 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** DIAGNÓSTICO RELACIONADO 2 A LA SALIDA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"diagnóstico rel. 2 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"diagnóstico rel. 3 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** DESTINO A LA SALIDA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"destino a la salida",1,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se valida que el destino de la salida sea valido
							if(!campos[12].trim().equals("")&&!campos[12].equals("1")&&!campos[12].equals("2")&&!campos[12].equals("3"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAU, "destino a la salida", "datos inválidos (línea "+contador+")");
							
							//14) ******** ESTADO A LA SALIDA ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"estado a la salida",1,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el estado a la salida de realización sea válido
							if(!campos[13].trim().equals("")&&!campos[13].equals("1")&&!campos[13].equals("2"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAU, "estado a la salida", "datos inválidos (línea "+contador+")");
							
							//15) ******** CAUSA BÁSICA DE MUERTE ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[14],"causa básica de muerte",4,ConstantesBD.ripsAU,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[13].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//16) ******** FECHA DE SALIDA********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[15],"fecha de salida",10,ConstantesBD.ripsAU,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//17) ******** HORA DE SALIDA********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[16],"hora de salida",5,ConstantesBD.ripsAU,contador,false,false,false,true,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AU? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AH la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAU,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AU"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAU);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAU)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAU);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AH
	 * @param pos
	 */
	private void validacionesGeneralesAH(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<19||campos.length>19||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=18)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAH);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAH, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** VÍA DE INGRESO************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"vía de ingreso",1,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la vía de ingreso sea válida
							if(!campos[4].trim().equals("")&&!campos[4].equals("1")&&!campos[4].equals("2")&&!campos[4].equals("3")&&!campos[4].equals("4"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAH, "vía de ingreso", "datos inválidos (línea "+contador+")");
							
							//6) ******** FECHA DE INGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"fecha de ingreso",10,ConstantesBD.ripsAH,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** HORA DE INGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"hora de ingreso",5,ConstantesBD.ripsAH,contador,false,false,false,true,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** NÚMERO DE AUTORIZACION ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"número de autorización",15,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//9) ******** CAUSA EXTERNA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"causa externa",2,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la causa externa sea válida
							if(!campos[8].trim().equals("")&&!campos[8].equals("01")&&!campos[8].equals("02")&&!campos[8].equals("03")&&!campos[8].equals("04")&&
									!campos[8].equals("05")&&!campos[8].equals("06")&&!campos[8].equals("07")&&!campos[8].equals("08")&&
									!campos[8].equals("09")&&!campos[8].equals("10")&&!campos[8].equals("11")&&!campos[8].equals("12")&&
									!campos[8].equals("13")&&!campos[8].equals("14")&&!campos[8].equals("15"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAH, "causa externa", "datos inválidos (línea "+contador+")");
							
							//10) ******** DIAGNOSTICO PRINCIPAL INGRESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"diagnóstico principal ingreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** DIAGNOSTICO PRINCIPAL EGRESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"diagnóstico principal egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** DIAGNÓSTICO RELACIONADO 1 EGRESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"diagnóstico rel. 1 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** DIAGNÓSTICO RELACIONADO 2 EGRESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"diagnóstico rel. 2 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//14) ******** DIAGNÓSTICO RELACIONADO 1 EGRESO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"diagnóstico rel. 3 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//15) ******** DIAGNÓSTICO COMPLICACIÓN ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[14],"diagnóstico de complicación",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//16) ******** ESTADO A LA SALIDA ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[15],"estado a la salida",1,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el estado a la salida de realización sea válido
							if(!campos[15].trim().equals("")&&!campos[15].equals("1")&&!campos[15].equals("2"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAH, "estado a la salida", "datos inválidos (línea "+contador+")");
							
							//17) ******** DIAGNÓSTICO MUERTE ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[16],"diagnóstico de muerte",4,ConstantesBD.ripsAH,contador,false,false,false,false,elementoInconsistencia,posIncon,campos[15].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//18) ******** FECHA DE EGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[17],"fecha de egreso",10,ConstantesBD.ripsAH,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//19) ******** HORA DE EGRESO********************************************************
							elementoInconsistencia = validacionGeneralCampo(campos[18],"hora de egreso",5,ConstantesBD.ripsAH,contador,false,false,false,true,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AH? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AH la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAH,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AH"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAH);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAH)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAH);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AP
	 * @param pos
	 */
	private void validacionesGeneralesAP(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//solo si el archivo fue seleccionado se prosigue a la validacion
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<15||campos.length>15||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=14)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAP);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].trim().equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAP, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** FECHA DEL PROCEDIMIENTO************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"fecha del procedimiento",10,ConstantesBD.ripsAP,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** NÚMERO DE AUTORIZACION ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"número de autorización",15,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** CODIGO DEL PROCEDIMIENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"código del procedimiento",8,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** AMBITO DE REALIZACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"ámbito de realización",1,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el ámbito de realización sea válido
							if(!campos[7].trim().equals("")&&!campos[7].equals("1")&&!campos[7].equals("2")&&!campos[7].equals("3"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAP, "ámbito de realización", "datos inválidos (línea "+contador+")");
							
							//9) ******** FINALIDAD DEL PROCEDIMIENTO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"finalidad del procedimiento",1,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que en el caso que exista finalidad del procedimiento tenga el código válido
							if(!campos[8].trim().equals("")&&!campos[8].trim().equals("1")&&!campos[8].trim().equals("2")&&!campos[8].trim().equals("3")&&
								!campos[8].trim().equals("4")&&!campos[8].trim().equals("5")
								)
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAP, "finalidad del procedimiento", "datos inválidos (línea "+contador+")");
							
							//10) ******** PERSONAL QUE ATIENDE ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"personal que atiende",1,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la causa externa tenga el código válido
							if(!campos[9].trim().equals("")&&!campos[9].trim().equals("1")&&!campos[9].trim().equals("2")&&!campos[9].trim().equals("3")&&
								!campos[9].trim().equals("4")&&!campos[9].trim().equals("5")&&!campos[9].trim().equals("")
								)
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAP, "personal que atiende", "datos inválidos (línea "+contador+")");
							
							//11) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"código diagnóstico principal",4,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"código diagnóstico relacionado",4,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** DIAGNÓSTICO COMPLICACION ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"diagnóstico complicación",4,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//14) ******** FORMA DE REALIZACIÓN ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"forma de realización",1,ConstantesBD.ripsAP,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la causa externa tenga el código válido
							if(!campos[13].trim().equals("")&&!campos[13].trim().equals("1")&&!campos[13].trim().equals("2")&&!campos[13].trim().equals("3")&&
								!campos[13].trim().equals("4")&&!campos[13].trim().equals("5")&&!campos[13].trim().equals("")
								)
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAP, "forma de realización", "datos inválidos (línea "+contador+")");
							
							
							//15) ******** VALOR NETO A PAGAR ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[14],"valor procedimiento",15,ConstantesBD.ripsAP,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AP? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AP la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAP,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AP"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAP);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAP)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAP);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AC
	 * @param pos
	 */
	private void validacionesGeneralesAC(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		//Solo si el archivo fue seleccionado se prosigue con la operación
		if(archivo!=null)
		{
			try
			{
				String cadena="";
				int contador = 0;
				String[] campos = new String[0];
				boolean continuar = false;
				//******SE INICIALIZA ARCHIVO*************************
				
				FileReader stream=new FileReader(archivo); 
				BufferedReader buffer=new BufferedReader(stream);
				
				//********SE RECORRE LÍNEA POR LÍNEA**************
				cadena=buffer.readLine();
				while(cadena!=null)
				{
					contador++;
					continuar = false;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAC);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
						posIncon++;
					}
					else
					{
						//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
						if(!this.numeroFactura.equals(""))
						{
							//Se verifica que la factura del archivo corresponda a la factura capturada
							if(this.numeroFactura.equals(campos[0].trim()))
								continuar = true;
							else
								continuar = false;
						}
						else
							continuar = true;
						
						//Se inicializa el indicativo de inconsistencia x registro
						this.huboInconsistenciaXRegistro = false;
						
						//Si no se puede continuar se salta el registro
						if(continuar)
						{
						
							//1) ******** NÚMERO DE FACTURA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de identificacion corresponda a: CC,CE,PA,RC,TI,AS,MS,NU
							if(!campos[2].equals("")&&!campos[2].equals("CC")&&!campos[2].equals("RC")&&!campos[2].equals("CE")&&!campos[2].equals("PA")&&
								!campos[2].equals("TI")&&!campos[2].equals("AS")&&!campos[2].equals("MS")&&!campos[2].equals("NU"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAC, "tipo de identificación", "datos inválidos (línea "+contador+")");
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//5) ******** FECHA DE LA CONSULTA************************************
							elementoInconsistencia = validacionGeneralCampo(campos[4],"fecha de consulta",10,ConstantesBD.ripsAC,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//6) ******** NÚMERO DE AUTORIZACION ************************************
							elementoInconsistencia = validacionGeneralCampo(campos[5],"número de autorización",15,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//7) ******** CODIGO DE CONSULTA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[6],"código de consulta",8,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//8) ******** FINALIDAD DE CONSULTA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[7],"finalidad consulta",2,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que en el caso que exista finalidad de consulta tenga el código válido
							if(!campos[7].trim().equals("")&&!campos[7].trim().equals("01")&&!campos[7].trim().equals("02")&&!campos[7].trim().equals("03")&&
								!campos[7].trim().equals("04")&&!campos[7].trim().equals("05")&&!campos[7].trim().equals("06")&&!campos[7].trim().equals("07")&&
								!campos[7].trim().equals("08")&&!campos[7].trim().equals("09")&&!campos[7].trim().equals("10")
								)
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAC, "finalidad consulta", "datos inválidos (línea "+contador+")");
							
							//9) ******** CAUSA EXTERNA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[8],"causa externa",2,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que la causa externa tenga el código válido
							if(!campos[8].trim().equals("")&&!campos[8].trim().equals("01")&&!campos[8].trim().equals("02")&&!campos[8].trim().equals("03")&&
								!campos[8].trim().equals("04")&&!campos[8].trim().equals("05")&&!campos[8].trim().equals("06")&&
								!campos[8].trim().equals("07")&&!campos[8].trim().equals("08")&&!campos[8].trim().equals("09")&&
								!campos[8].trim().equals("10")&&!campos[8].trim().equals("11")&&!campos[8].trim().equals("12")&&
								!campos[8].trim().equals("13")&&!campos[8].trim().equals("14")&&!campos[8].trim().equals("15")
								)
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAC, "causa externa", "datos inválidos (línea "+contador+")");
							
							//10) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[9],"código diagnóstico principal",4,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//11) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 1 ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[10],"código diagnóstico relacionado 1",4,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 2 ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[11],"código diagnóstico relacionado 2",4,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//13) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 3 ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[12],"código diagnóstico relacionado 3",4,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//14) ******** TIPO DIAGNOSTICO PRINCIPAL ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[13],"tipo diagnóstico principal",1,ConstantesBD.ripsAC,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Se verifica que el tipo de diagnóstico tenga los valores 1, 2 o 3
							if(!campos[13].trim().equals("")&&!campos[13].equals("1")&&!campos[13].equals("2")&&!campos[13].equals("3"))
								editarInconsistenciaEncabezado(campos[0], campos[2], campos[3], ConstantesBD.ripsAC, "tipo diagnóstico principal", "datos inválidos (línea "+contador+")");
							
							//15) ******** VALOR CONSULTA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[14],"valor consulta",15,ConstantesBD.ripsAC,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//16) ******** VALOR CUOTA MODERADORA ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[15],"valor cuota moderadora",15,ConstantesBD.ripsAC,contador,true,true,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//17) ******** VALOR NETO A PAGAR ***************************************
							elementoInconsistencia = validacionGeneralCampo(campos[16],"valor neto a pagar",15,ConstantesBD.ripsAC,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]);
							posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
							
							//Si no hubo inconsistencia por ese registro se realiza la inserción en la tabla AF
							if(!this.huboInconsistenciaXRegistro)
							{
								//logger.info("¿existe factura en AC? "+lecturaDao.existeFacturaEnAF(con, campos[0]));
								//Para hacer la inserción del registro en la tabla de AC la factura debe existir en la tabla de AF
								if(lecturaDao.existeFacturaEnAF(con, campos[0], usuarioSession.getLoginUsuario(), usuarioSession.getCodigoCentroAtencion()))
								{
									if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAC,false)<=0)
										errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AC"));
								}
								else
								{
									elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAC);
									elementoInconsistencia.put("campo_"+posIncon,"número de factura");
									elementoInconsistencia.put("observaciones_"+posIncon,"no se encuentra en archivo "+ConstantesBD.ripsAF+":"+campos[0].trim()+" (linea "+contador+")");
									posIncon++;
								}
							}
							//Como hay inconsistencias ya no se debe tomar en cuenta la factura para la generación del ingreso (si existe)
							else
								lecturaDao.actualizarFacturaComoInconsistencia(con, campos[0],usuarioSession.getLoginUsuario(),usuarioSession.getCodigoCentroAtencion());
						} //Fin IF continuar
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
				//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
				if(this.numeroFactura.equals(""))
				{
					//Si no son los mismos registros del CT se genera inconsistencia
					if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAC)+"", true)!=contador)
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"total registros");
						elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAC);
						posIncon++;
						this.cancelarProceso = true;
					}
				}
				
				//Se verifica si hubo inconsistencias sin encabezado
				elementoInconsistencia.put("numRegistros", posIncon+"");
				if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
					this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
			
				
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
				errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
			}
		}//Fin IF
		
	}
	/**
	 * Método para realizar las validaciones del archivo AD
	 * @param pos
	 */
	private void validacionesGeneralesAD(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		
		
		try
		{
			String cadena="";
			int contador = 0;
			String[] campos = new String[0];
			boolean continuar = false;
			//******SE INICIALIZA ARCHIVO*************************
			
			FileReader stream=new FileReader(archivo); 
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				continuar = false;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<6||campos.length>6||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=5)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAD);
					elementoInconsistencia.put("campo_"+posIncon,"---");
					elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
					posIncon++;
				}
				else
				{
					//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
					if(!this.numeroFactura.equals(""))
					{
						//Se verifica que la factura del archivo corresponda a la factura capturada
						if(this.numeroFactura.equals(campos[0].trim()))
							continuar = true;
						else
							continuar = false;
					}
					else
						continuar = true;
					
					//Se inicializa el indicativo de inconsistencia x registro
					this.huboInconsistenciaXRegistro = false;
					
					//Si no se puede continuar se salta el registro
					if(continuar)
					{
						//1) ******** NÚMERO DE FACTURA************************************
						elementoInconsistencia = validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAD,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
						elementoInconsistencia = validacionGeneralCampo(campos[1],"código prestador de servicios salud",12,ConstantesBD.ripsAD,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//3) ******** CÓDIGO DEL CONCEPTO ********************************************
						elementoInconsistencia = validacionGeneralCampo(campos[2],"código del concepto",2,ConstantesBD.ripsAD,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que el código del concepto sea válido
						if(!campos[2].equals("")&&!campos[2].equals("01")&&!campos[2].equals("02")&&!campos[2].equals("03")&&!campos[2].equals("04")&&
							!campos[2].equals("05")&&!campos[2].equals("06")&&!campos[2].equals("07")&&!campos[2].equals("08")&&
							!campos[2].equals("09")&&!campos[2].equals("10")&&!campos[2].equals("11")&&!campos[2].equals("12")&&
							!campos[2].equals("13")&&!campos[2].equals("14")
							)
							editarInconsistenciaEncabezado(campos[0], "", "", ConstantesBD.ripsAD, "código del concepto", "datos inválidos (línea "+contador+")");
						
						//4) ******** CANTIDAD************************************
						elementoInconsistencia = validacionGeneralCampo(campos[3],"cantidad",15,ConstantesBD.ripsAD,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//5) ******** VALOR UNITARIO************************************
						elementoInconsistencia = validacionGeneralCampo(campos[4],"valor unitario",15,ConstantesBD.ripsAD,contador,true,true,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//6) ******** VALOR TOTAL************************************
						elementoInconsistencia = validacionGeneralCampo(campos[5],"valor concepto",15,ConstantesBD.ripsAD,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[0],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						
					} //Fin IF continuar
				}  //fin else IF 
					
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
			if(this.numeroFactura.equals(""))
			{
				//Si no son los mismos registros del CT se genera inconsistencia
				if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAD)+"", true)!=contador)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
					elementoInconsistencia.put("campo_"+posIncon,"total registros");
					elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAD);
					posIncon++;
					this.cancelarProceso = true;
				}
			}
			
			//Se verifica si hubo inconsistencias sin encabezado
			elementoInconsistencia.put("numRegistros", posIncon+"");
			if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
				this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
		
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo AF
	 * @param pos
	 */
	private void validacionesGeneralesAF(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		boolean encontroFactura = false;
		
		File archivo = (File)this.archivos.get(pos);
		
		try
		{
			String cadena="";
			int contador = 0;
			String[] campos = new String[0];
			boolean continuar = false;
			//******SE INICIALIZA ARCHIVO*************************
			
			FileReader stream=new FileReader(archivo); 
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				continuar = false;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
					elementoInconsistencia.put("campo_"+posIncon,"---");
					elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (linea "+contador+")");
					posIncon++;
				}
				else
				{
					//************FILTRO POR SI SE INGRESÓ UN NÚMERO DE FACTURA ESPECÍFICA***************************
					if(!this.numeroFactura.equals(""))
					{
						//Se verifica que la factura del archivo corresponda a la factura capturada
						if(this.numeroFactura.equals(campos[4].trim()))
						{
							continuar = true;
							encontroFactura = true;
						}
						else
							continuar = false;
					}
					else
						continuar = true;
					
					//Se inicializa el indicativo de inconsistencia x registro
					this.huboInconsistenciaXRegistro = false;
					
					//Si no se puede continuar se salta el registro
					if(continuar)
					{
					
						//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
						elementoInconsistencia = validacionGeneralCampo(campos[0],"código prestador de servicios salud",12,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[0].trim().length()>12)
							campos[0] = campos[0].substring(0, 12);
						
						//2) ******** RAZÓN SOCIAL **********************************************
						elementoInconsistencia = validacionGeneralCampo(campos[1],"razón social",60,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[1].trim().length()>60)
							campos[1] = campos[1].substring(0, 60);
						
						//3) ******** TIPO DE IDENTIFICACION ********************************************
						elementoInconsistencia = validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						//Se verifica que el tipo de identificacion corresponda a: NI,CC,CE,PA
						if(!campos[2].equals("CC")&&!campos[2].equals("NI")&&!campos[2].equals("CE")&&!campos[2].equals("PA"))
							editarInconsistenciaEncabezado(campos[4], "", "", ConstantesBD.ripsAF, "tipo de identificación", "datos inválidos, debe ser CC, NI, CE o PA");
						
						//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
						elementoInconsistencia = validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[3].trim().length()>20)
							campos[3] = campos[3].substring(0, 20);
						
						//5) ******** NÚMERO DE FACTURA************************************
						elementoInconsistencia = validacionGeneralCampo(campos[4],"número de factura",20,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[4].trim().length()>20)
							campos[4] = campos[4].substring(0, 20);
						
						//6) ******** FECHA DE EXPEDICIÓN************************************
						elementoInconsistencia = validacionGeneralCampo(campos[5],"fecha de expedición",10,ConstantesBD.ripsAF,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[5].trim().length()>10)
							campos[5] = campos[5].substring(0, 10);
						
						//7) ******** FECHA DE INICIO ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[6],"fecha de inicio",10,ConstantesBD.ripsAF,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[6].trim().length()>10)
							campos[6] = campos[6].substring(0, 10);
						
						//8) ******** FECHA FINAL ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[7],"fecha final",10,ConstantesBD.ripsAF,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[7].trim().length()>10)
							campos[7] = campos[7].substring(0, 10);
						
						//9) ******** CODIGO ENTIDAD ADMINISTRADORA ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[8],"código entidad administradora",6,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[8].trim().length()>6)
							campos[8] = campos[8].substring(0, 6);
						
						//10) ******** NOMBRE ENTIDAD ADMINISTRADORA ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[9],"nombre entidad administradora",30,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[9].trim().length()>30)
							campos[9] = campos[9].substring(0, 30);
						
						//11) ******** NÚMERO DEL CONTRATO ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[10],"número del contrato",15,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[10].trim().length()>15)
							campos[10] = campos[10].substring(0, 15);
						
						//12) ******** PLAN DE BENEFICIOS ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[11],"plan de beneficios",30,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[11].trim().length()>30)
							campos[11] = campos[11].substring(0, 30);
						
						//13) ******** NÚMERO DE LA PÓLIZA ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[12],"número de la póliza",10,ConstantesBD.ripsAF,contador,false,false,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[12].trim().length()>10)
							campos[12] = campos[12].substring(0, 10);
						
						//14) ******** VALOR COPAGO ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[13],"valor copago",15,ConstantesBD.ripsAF,contador,true,true,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[13].trim().length()>15)
							campos[13] = campos[13].substring(0, 15);
						
						//15) ******** VALOR COMISIÓN ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[14],"valor comisión",15,ConstantesBD.ripsAF,contador,true,true,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[14].trim().length()>15)
							campos[14] = campos[14].substring(0, 15);
						
						//16) ******** VALOR DESCUENTOS ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[15],"valor descuentos",15,ConstantesBD.ripsAF,contador,true,true,false,false,elementoInconsistencia,posIncon,false,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[15].trim().length()>15)
							campos[15] = campos[15].substring(0, 15);
						
						//17) ******** VALOR NETO A PAGAR ***************************************
						elementoInconsistencia = validacionGeneralCampo(campos[16],"valor neto a pagar",15,ConstantesBD.ripsAF,contador,true,true,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoSi,campos[4],"","");
						posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
						
						if(campos[16].trim().length()>15)
							campos[16] = campos[16].substring(0, 15);
						
						//Se registra la factura indicando si hubo inconsistencia o no
						if(this.insercionRegistroArchivo(con, campos, ConstantesBD.ripsAF,this.huboInconsistenciaXRegistro)<=0)
							errores.add("",new ActionMessage("errors.problemasGenericos","durante el proceso de validación del archivo AF"));
						
					} //Fin IF continuar
				}  //fin else IF 
					
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			//Si no se parametrizó factura se prosigue a seguir la validacion de registros con el CT
			if(this.numeroFactura.equals(""))
			{
				//Si no son los mismos registros del CT se genera inconsistencia
				if(Utilidades.convertirAEntero(this.totalRegistros.get(ConstantesBD.ripsAF)+"", true)!=contador)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
					elementoInconsistencia.put("campo_"+posIncon,"total registros");
					elementoInconsistencia.put("observaciones_"+posIncon,"diferente a los registros del archivo "+ConstantesBD.ripsAF);
					posIncon++;
					this.cancelarProceso = true;
				}
			}
			//Si se parametrizó factura se verifica si se encontró
			else if(!encontroFactura)
			{
				elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
				elementoInconsistencia.put("campo_"+posIncon,"número de la factura");
				elementoInconsistencia.put("observaciones_"+posIncon,"no se encontró la factura N° "+this.numeroFactura);
				posIncon++;
				this.cancelarProceso = true;
			}
			
			//Se verifica si hubo inconsistencias sin encabezado
			elementoInconsistencia.put("numRegistros", posIncon+"");
			if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0)
				this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
		
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		
	}
	/**
	 * Método para realizar las validaciones del archivo CT
	 * @param pos
	 */
	private void validacionesGeneralesCT(int pos) 
	{
		//Atributos para el manejo de inconsistencias
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		int posIncon = 0;
		
		File archivo = (File)this.archivos.get(pos);
		this.archivoCT = new ArrayList<String[]>();
		try
		{
			String cadena="";
			int contador = 0;
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			
			FileReader stream=new FileReader(archivo); 
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				//Se añaden los campos al archivo
				archivoCT.add(campos);
				
				//Validación de que todo esté separado por comas
				if(campos.length<4||campos.length>4||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=3)
				{
					elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
					elementoInconsistencia.put("campo_"+posIncon,"---");
					elementoInconsistencia.put("observaciones_"+posIncon,"campos con separación de comas errónea (línea "+contador+")");
					posIncon++;
				}
				else
				{
					//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
					elementoInconsistencia = validacionGeneralCampo(campos[0],"código prestador de servicios salud",12,ConstantesBD.ripsCT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoNo,"","","");
					posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
					
					//Validacion con la entidad subcontratada
					if(!campos[0].equals(lecturaDao.consultarCodMinSaludEntidadSubcontratada(this.con, this.consecutivoEntidadSubcontratada)))
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"código prestador de servicios salud");
						elementoInconsistencia.put("observaciones_"+posIncon,"no concuerda con el codigo minsalud de la entidad subcontratada (línea "+contador+")");
						posIncon++;
						//Si no se seleccionó factura se cancela el proceso
						if(this.numeroFactura.equals(""))
							this.cancelarProceso = true;
					}
					
					//2) ******** FECHA DE REMISION **********************************************
					elementoInconsistencia = validacionGeneralCampo(campos[1],"fecha de remisión",10,ConstantesBD.ripsCT,contador,false,false,true,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoNo,"","","");
					posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
					
					//3) ******** CODIGO DEL ARCHIVO ********************************************
					elementoInconsistencia = validacionGeneralCampo(campos[2],"código del archivo",8,ConstantesBD.ripsCT,contador,false,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoNo,"","","");
					posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
					
					//Se verifica que el codigo del archivo concuerde con algun archivo leido
					boolean existe = false;
					for(int i=0;i<this.archivos.size();i++)
					{
						File arc = (File)this.archivos.get(i);
						//se verifica que en el caso de que haya un archivo existente su nombre concuerde
						//con el codigo del archivo leido en el CT
						if
						(
							arc!=null&&//que no este nulo
							arc.getName().substring(0, arc.getName().indexOf(".")).equals(campos[2].trim())
						)
							existe = true;
						
					}
					
					//Si no se encontró el nombre del archivo y no está vacío se genera inconsistencia
					if(!existe&&!campos[2].trim().equals(""))
					{
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"código del archivo");
						elementoInconsistencia.put("observaciones_"+posIncon,"no se ha leído archivo con el nombre "+campos[2]+" (línea "+contador+")");
						posIncon++;
					}
					
					//4) ******** TOTAL REGISTROS************************************
					elementoInconsistencia = validacionGeneralCampo(campos[3],"total de registros",10,ConstantesBD.ripsCT,contador,true,false,false,false,elementoInconsistencia,posIncon,true,ConstantesBD.acronimoNo,"","","");
					posIncon = Integer.parseInt(elementoInconsistencia.get("numRegistros").toString());
					
					//Se toman los registros del archivo registrado en CT
					//Se toma no mas el acrónimo
					this.totalRegistros.put(campos[2].substring(0, 2), campos[3].trim());
				}
					
				cadena=buffer.readLine();
				
			}
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			//Se verifica si hubo inconsistencias
			elementoInconsistencia.put("numRegistros", posIncon+"");
			//Si no hay factura se registran las inconsistencias, de lo contrario aunque hayan no importa porque solo se esta 
			//haciendo lectura por una sola factura
			if(Utilidades.convertirAEntero(elementoInconsistencia.get("numRegistros").toString(), true)>0&&this.numeroFactura.equals(""))
				this.inconsistencias.add(elementoInconsistencia); //se añade la inconsistencia
		
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","No se pudo encontrar el archivo "+archivo.getName()+" al tratar de cargarlo"));
			
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+archivo.getName()+" al cargarlo: "+e);
			errores.add("",new ActionMessage("errors.notEspecific","Error en los streams del archivo "+archivo.getName()+" al tratar de cargarlo"));
		}
		
	}
	
	/**
	 * Método que realiza la validacion sintáctica de los campos de los archivos RIPS
	 * @param campo
	 * @param nombreArchivo
	 * @param numeroLinea
	 * @param esNumerico
	 * @param elementoInconsistencia
	 * @param posIncon
	 * @param esRequerido
	 * @return
	 */
	private HashMap<String, Object> validacionGeneralCampo(
			String campo, //contenido del campo 
			String nombreCampo, //nombre del campo 
			int tamanio, //tamaño que debe tener el campo 
			String nombreArchivo, //nombre del archivo que se está evaluando
			int numeroLinea, //numero de línea del registro del archivo que se está evaluando
			boolean esNumerico, //para saber si el campo es numérico 
			boolean esDecimal, //para saber si es decimal
			boolean esFecha, //para saber si es cmapo tipo fecha
			boolean esHora, // para saber si es campo tipo hora
			HashMap<String, Object> elementoInconsistencia, // mapa donde se guarda inconsistencias 
			int posIncon, //posicion del registro de inconsistencia
			boolean esRequerido, //para saber si el campo es requerido 
			String inconsistenciaEncabezado, //para saber el tipo de inconsistencia es por encabezado o no
			String numeroFactura, //Número de la factura
			String codigoTipoIdentificacion, //Tipo identificacion del paciente
			String numeroIdentificacion //Numero de identificacion del paciente
			) 
	{
		ArrayList<String> observaciones = new ArrayList<String>();
		
		//verificacion de caracteres especiales dependiendo del tipo de campo
		if(esNumerico) //solo aplica para los numéricos
		{
			if(UtilidadCadena.tieneCaracteresEspecialesNumericoRips(campo))
				observaciones.add("caracteres inválidos (línea "+numeroLinea+")");
			else
			{
				String[] numerico = campo.split("\\.");
				
				if(esDecimal)
				{
					if(numerico.length>1)
					{
						if(numerico.length!=2)
							observaciones.add("dato numérico inválido (línea "+numeroLinea+")");
						else
						{
							if(numerico[1].trim().length()>2)
								observaciones.add("la parte decimal debe ser unicamente de 2 caracteres (línea "+numeroLinea+")");
							
							try
							{
								Integer.parseInt(numerico[0].trim());
							}
							catch(Exception e)
							{
								observaciones.add("la parte entera es inválida (línea "+numeroLinea+") ");
							}
						}
					}
				}
				else if(!campo.trim().equals("")) //si no esta vacío se verifica que sea entero
				{
					try
					{
						Integer.parseInt(campo.trim());
					}
					catch(Exception e)
					{
						observaciones.add("número entero inválido (línea "+numeroLinea+") ");
					}
				}
				
			}
			
		}
		else if(!esFecha&&!esHora) //solo aplica para los de tipo texto
		{
			if(UtilidadCadena.tieneCaracteresEspecialesTextoRips(campo))
				observaciones.add("caracteres inválidos (línea "+numeroLinea+")");
		}
		//se verifica la longitud
		if((campo.length()>tamanio))
			observaciones.add("tamaño del campo inválido: "+campo.length()+", debe ser de "+tamanio+" (línea "+numeroLinea+")");
		
		//Se verifica la alineacion si no es de tipo fecha , ni hora
		if(!esFecha&&!esHora&&!UtilidadCadena.alineacionEspaciosValidaCampoRips(campo, esNumerico))
			observaciones.add("alineación de campo inválida, debe ser a la "+(esNumerico?"derecha":"izquierda")+" (línea "+numeroLinea+")");
		
		//Si es Fecha se valida el formato de la fecha
		if(esFecha&&!UtilidadFecha.validarFecha(campo)&&!campo.trim().equals(""))
			observaciones.add("formato de fecha inválido, debe ser dd/mm/aaaa (línea "+numeroLinea+")");
		
		//Si es hora se valida el formato de la hora
		if(esHora&&!UtilidadFecha.validacionHora(campo).puedoSeguir&&!campo.trim().equals(""))
			observaciones.add("formato de hora inválido, debe ser hh:mm (línea "+numeroLinea+")");
		
		//Validacion para saber si es requerido
		if(campo.trim().equals("")&&esRequerido)
			observaciones.add("es requerido (línea "+numeroLinea+")");
		
		
		//Se edita cada observacion como inconsistencia dependiendo si es con o sin encabezado
		for(int i=0;i<observaciones.size();i++)
		{
			if(UtilidadTexto.getBoolean(inconsistenciaEncabezado))
			{
				editarInconsistenciaEncabezado(numeroFactura,codigoTipoIdentificacion,numeroIdentificacion,nombreArchivo,nombreCampo,observaciones.get(i).toString());
			}
			else
			{
				elementoInconsistencia.put("archivo_"+posIncon,nombreArchivo);
				elementoInconsistencia.put("campo_"+posIncon,nombreCampo);
				elementoInconsistencia.put("observaciones_"+posIncon,observaciones.get(i).toString());
				posIncon++;
				this.huboInconsistenciaXRegistro = true;
			}
		}
		
		elementoInconsistencia.put("numRegistros", posIncon);
		return elementoInconsistencia;
	}
	
	
	/**
	 * Método implementado para editar una inconsistencia con encabezado
	 * @param numeroFactura
	 * @param codigoTipoIdentificacion
	 * @param numeroIdentificacion
	 * @param nombreArchivo
	 * @param nombreCampo
	 * @param observacion
	 */
	private void editarInconsistenciaEncabezado(String numeroFactura, String codigoTipoIdentificacion, String numeroIdentificacion, String nombreArchivo, String nombreCampo, String observacion) 
	{
		HashMap nuevoElemento = new HashMap();
		int posicion = ConstantesBD.codigoNuncaValido;
		int numRegistros = 0;
		
		//********SE REVISA SI YA SE TIENEN INCONSISTENCIAS PARA LA FACTURA/TIPO ID/NUMERO ID************
		for(int i=0;i<this.inconsistencias.size();i++)
		{
			HashMap elemento = (HashMap)inconsistencias.get(i);
			
			if(UtilidadTexto.getBoolean(elemento.get("encabezado").toString())&&
			  numeroFactura.trim().equals(elemento.get("numeroFactura").toString().trim())&&
			  codigoTipoIdentificacion.trim().equals(elemento.get("tipoIdentificacion").toString().trim())&&
			  numeroIdentificacion.trim().equals(elemento.get("numeroIdentificacion").toString().trim()))
			{
				posicion = i;
			}
				
		}
		//***********************************************************************************************
		
		
		if(posicion!=ConstantesBD.codigoNuncaValido)
		{
			nuevoElemento = (HashMap)inconsistencias.get(posicion);
			numRegistros = Integer.parseInt(nuevoElemento.get("numRegistros").toString());
		}
		else
		{
			nuevoElemento.put("encabezado", ConstantesBD.acronimoSi);
			nuevoElemento.put("numeroFactura",numeroFactura);
			nuevoElemento.put("tipoIdentificacion",codigoTipoIdentificacion);
			nuevoElemento.put("numeroIdentificacion",numeroIdentificacion);
		}
		
		nuevoElemento.put("archivo_"+numRegistros,nombreArchivo);
		nuevoElemento.put("campo_"+numRegistros,nombreCampo);
		nuevoElemento.put("observaciones_"+numRegistros,observacion);
		numRegistros++;
		nuevoElemento.put("numRegistros", numRegistros+"");
		
		if(posicion==ConstantesBD.codigoNuncaValido)
			this.inconsistencias.add(this.inconsistencias.size(),nuevoElemento);
		this.huboInconsistenciaXRegistro = true;
		
		
		
	}
	/**
	 * Método implementado para generar el archivo de inconsistencias
	 * @param con 
	 *
	 */
	private void generarArchivoInconsistencias(Connection con) 
	{
		try
		{
			String registroIncon = "";
			String[] fecha = UtilidadFecha.getFechaActual(con).split("/");
			String[] hora = UtilidadFecha.getHoraActual(con).split(":");
			boolean tieneEncabezado = false;
			//apertura de archivo Incon
			String nombreArchivoIncon=ConstantesBD.ripsInconsistencias+this.numeroRemision+fecha[0]+fecha[1]+fecha[2].substring(2)+hora[0]+hora[1]+".txt";
			
			File archivoIncon=new File(this.directorioArchivos,nombreArchivoIncon);
			FileWriter streamIncon=new FileWriter(archivoIncon,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			
			//Primero se generan las inconsistencias sin encabezado
			for(int i=0;i<this.inconsistencias.size();i++)
			{
				//Se toma el elemento
				HashMap<String, Object> elemento = (HashMap)this.inconsistencias.get(i);
				int numRegistros = Integer.parseInt(elemento.get("numRegistros").toString());
				//se limpia registro
				registroIncon="";
				
				//Se verifica que tipo de inconsistencia es 
				if(!UtilidadTexto.getBoolean(elemento.get("encabezado").toString()))
				{
					this.huboInconsistencias = true;
					
					if(!tieneEncabezado)
					{
						registroIncon = "\n\n"+UtilidadCadena.editarEspacios("Archivo RIPS", 12, 40, false)+UtilidadCadena.editarEspacios("Campo", 5, 40, false)+"Observaciones";
						tieneEncabezado = true;
					}
					
					for(int j=0;j<numRegistros;j++)
						registroIncon += "\n"+
							UtilidadCadena.editarEspacios(elemento.get("archivo_"+j).toString().trim(),elemento.get("archivo_"+j).toString().trim().length(),40,false)+
							UtilidadCadena.editarEspacios(elemento.get("campo_"+j).toString(),elemento.get("campo_"+j).toString().length(),40,false)+
							elemento.get("observaciones_"+j);
					
					bufferIncon.write(registroIncon);
				}
				
				
				
			}
			//Segundo se generan las inconsistencias con encabezado
			for(int i=0;i<this.inconsistencias.size();i++)
			{
				//Se toma el elemento
				HashMap<String, Object> elemento = (HashMap)this.inconsistencias.get(i);
				int numRegistros = Integer.parseInt(elemento.get("numRegistros").toString());
				//se limpia registro
				registroIncon="";
				
				//Se verifica que tipo de inconsistencia es 
				if(UtilidadTexto.getBoolean(elemento.get("encabezado").toString()))
				{
					this.huboInconsistencias = true;
					
					registroIncon = "\n\n\nN° FACTURA: "+elemento.get("numeroFactura").toString().trim();
					
					//Si se editó tipo y numero id paciente se postula
					if(!elemento.get("tipoIdentificacion").toString().equals("")&&!elemento.get("numeroIdentificacion").toString().equals(""))
						registroIncon+=", TIPO ID PACIENTE: "+elemento.get("tipoIdentificacion")+", N° ID PACIENTE: "+elemento.get("numeroIdentificacion");
					
					registroIncon += "\n\n"+UtilidadCadena.editarEspacios("Archivo RIPS", 12, 18, false)+UtilidadCadena.editarEspacios("Campo", 5, 40, false)+"Observaciones";
					
					for(int j=0;j<numRegistros;j++)
						registroIncon += "\n"+
							UtilidadCadena.editarEspacios(elemento.get("archivo_"+j).toString().trim(),elemento.get("archivo_"+j).toString().trim().length(),18,false)+
							UtilidadCadena.editarEspacios(elemento.get("campo_"+j).toString(),elemento.get("campo_"+j).toString().length(),40,false)+
							elemento.get("observaciones_"+j);
					bufferIncon.write(registroIncon);
				}
				
				
				
			}
			
			bufferIncon.close();
			
			if(huboInconsistencias)
			{
				this.pathArchivoInconsistencias = this.directorioArchivos + nombreArchivoIncon;
				//Se llena el archivo de inconsistencias en el log de lectura
				this.logLectura.setArchivoInconsistencias(nombreArchivoIncon);
			}
		}
		catch(FileNotFoundException e)
		{
			errores.add("",new ActionMessage("errors.notEspecific","Error al tratar de editar el archivo de inconsistencias"));
			logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
			this.pathArchivoInconsistencias = "";
		}
		catch(IOException e)
		{
			errores.add("",new ActionMessage("errors.notEspecific","Error en los streams al tratar de editar el archivo de inconsistencias"));
			logger.error("Error en los streams del archivo Incon: "+e);
			this.pathArchivoInconsistencias = "";
		}
		
	}
	/**
	 * Método implementado para realizar las validaciones de los tipos de archivos
	 *
	 */
	private void validacionesTiposArchivos() 
	{
		if(this.ubicacionPlanosEntidadesSubcontratadas.equals(ConstantesIntegridadDominio.acronimoServidor))
		{
			this.validacionTiposArchivosServidor();		
		}
		else if(this.ubicacionPlanosEntidadesSubcontratadas.equals(ConstantesIntegridadDominio.acronimoCliente))
		{
			this.validacionTiposArchivosCliente();
		}
		
		if(!this.errores.isEmpty())
			this.huboInconsistencias = true;
	}
	
	/**
	 * Método que realiza la validación de los tipos de archivos cuando la ubicación
	 * de los planos se encuentra en el cliente
	 *
	 */
	private void validacionTiposArchivosCliente() 
	{
		String numeroRemisionAnterior = "";
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		boolean diferenteNumeroRemision = false;
		int posIncon = 0;
		
		for(int i=0;i<this.archivos.size();i++)
		{
			File prueba = (File)this.archivos.get(i);
			String nombreArchivo = "";
			String acronimoArchivo = "";
			
			if(prueba!=null)
			{
				nombreArchivo = prueba.getName();
				String numeroRemision = nombreArchivo.substring(2, nombreArchivo.indexOf("."));
				acronimoArchivo = nombreArchivo.substring(0, 2);
				
				//1) Se verifica que el numero de remision sea igual para todos los archivos
				if(!numeroRemisionAnterior.equals("")&&!numeroRemision.equals(numeroRemisionAnterior))
					diferenteNumeroRemision = true;
				numeroRemisionAnterior = numeroRemision;
			}
				
			//2) Se verifica que el archivo tenga el nombre apropiado
			switch(i)
			{
				case 0: //archivo CT
					if(prueba==null)
					{
						errores.add("",new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsCT));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
						posIncon++;
					}
					
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsCT))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsCT+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 1: //archivo AF
					if(prueba==null)
					{
						errores.add("",new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsAF));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
						posIncon++;
					}
					
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAF))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAF+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				
				case 2: //archivo AD
					if(prueba==null)
					{
						errores.add("",new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsAD));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAD);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
						posIncon++;
					}
					
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAD))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAD+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAD);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 3: //archivo AC
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAC))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAC+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAC);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 4: //archivo AP
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAP))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAP+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAP);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 5: //archivo AH
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAH))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAH+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAH);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 6: //archivo AU
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAU))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAU+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAU);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 7: //archivo AM
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAM))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAM+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAM);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 8: //archivo AN
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAN))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAN+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAN);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 9: //archivo AT
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsAT))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsAT+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAT);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				case 10: //archivo US
					if(prueba==null)
					{
						errores.add("",new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsUS));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
						posIncon++;
					}
					
					if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(ConstantesBD.ripsUS))
					{
						errores.add("",new ActionMessage("errors.invalid","El nombre el archivo "+ConstantesBD.ripsUS+": "+nombreArchivo));
						//Se genera la inconsistencia
						elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
						elementoInconsistencia.put("campo_"+posIncon,"---");
						elementoInconsistencia.put("observaciones_"+posIncon,"tiene un nombre inválido ("+nombreArchivo+")");
						posIncon++;
					}
				break;
				
			}
			
		}
		
		//2) Si los archivos encontrados no tenían el mismo número de remisión se genera error
		if(diferenteNumeroRemision)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Los archivos encontrados no poseen el mismo número de remisión. Proceso Cancelado"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"Los archivos no poseen el mismo número de remisión");
			posIncon++;
		}
		else if(numeroRemisionAnterior.length()>6)
		{
			errores.add("", new ActionMessage("errors.maxlength","El número de remisión","6"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"El número de remisión supera 6 caracteres");
			posIncon++;
		}
		
		//3) Se verifica que como mínimo se haya ingresado el archivo AC, AP, AM o AT
		if((File)this.archivos.get(3)==null&&(File)this.archivos.get(4)==null&&(File)this.archivos.get(7)==null&&(File)this.archivos.get(9)==null)
		{
			errores.add("",new ActionMessage("errors.minimoCampos","el archivo AC, AP, AM o AT","lectura de los planos"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido como mínimo el archivo AC, AP, AM o AT");
			posIncon++;
		}
		
		if(!this.errores.isEmpty())
		{
			//Se adiciona inconsistencia
			elementoInconsistencia.put("numRegistros",posIncon+"");
			this.inconsistencias.add(elementoInconsistencia);
		}
		
		//Se asigna el numero de remision
		this.numeroRemision = numeroRemisionAnterior;
		
	}
	/**
	 * Método que realiza la validacion de tipos de archivos cuando la ubicación
	 * de los planos se encuentra en el servidor
	 *
	 */
	private void validacionTiposArchivosServidor() 
	{
		ArrayList<File> nuevoArreglo = new ArrayList<File>();
		HashMap contadorArchivos = new HashMap();
		HashMap<String, Object> elementoInconsistencia = new HashMap<String, Object>();
		elementoInconsistencia.put("encabezado", ConstantesBD.acronimoNo); //Será una inconsistencia sin encabezado
		String numeroRemisionAnterior = "";
		boolean diferenteNumeroRemision = false;
		int contador = 0, posicion = 0,posIncon = 0;
		
		for(int i=0;i<this.archivos.size();i++)
		{
			File prueba = (File)this.archivos.get(i);
			String nombreArchivo = prueba.getName();
			String acronimoArchivo = "";
			String numeroRemision = "";
			
			//---------se toma información del archivo------------------------------
			try
			{
				acronimoArchivo = nombreArchivo.substring(0, 2);
				numeroRemision = nombreArchivo.substring(2, nombreArchivo.indexOf("."));
			}
			catch(Exception e)
			{
				acronimoArchivo = "";
				numeroRemision = "";
			}
			
			//1) Se verifica que el numero de remision sea igual para todos los archivos
			if(!numeroRemisionAnterior.equals("")&&!numeroRemision.equals(numeroRemisionAnterior))
				diferenteNumeroRemision = true;
			numeroRemisionAnterior = numeroRemision;
			
			//2) Se realiza la cuenta de los archivos RIPS que se hayan encontrado
			contador = 0;
			if(acronimoArchivo.equals(ConstantesBD.ripsCT))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsCT)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsCT,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsCT,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAF))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAF)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAF,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAF,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsUS))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsUS)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsUS,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsUS,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAD))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAD)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAD,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAD,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAC))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAC)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAC,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAC,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAP))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAP)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAP,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAP,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAH))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAH)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAH,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAH,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAU))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAU)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAU,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAU,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAM))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAM)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAM,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAM,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAN))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAN)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAN,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAN,i+"");
			}
			else if(acronimoArchivo.equals(ConstantesBD.ripsAT))
			{
				contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAT)+"", true);
				contador++;
				contadorArchivos.put(ConstantesBD.ripsAT,contador+"");
				contadorArchivos.put("posicion"+ConstantesBD.ripsAT,i+"");
			}
			//Si el archivo no tiene ningun acrónimo de RIPS válido se considera como un archivo erróneo
			else
			{
				errores.add("",new ActionMessage("errors.invalid","Se encontró el archivo "+nombreArchivo+" que"));
				//Se genera la inconsistencia
				elementoInconsistencia.put("archivo_"+posIncon,nombreArchivo);
				elementoInconsistencia.put("campo_"+posIncon,"---");
				elementoInconsistencia.put("observaciones_"+posIncon,"es inválido");
				posIncon++;
				
			}
			
			
		}
		
		///Se asigna el numero de remision
		this.numeroRemision = numeroRemisionAnterior;
		
		//3) Se verifica el número de ocurrencias de cada archivo
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsCT)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsCT));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		else if(contador==0)
		{
			errores.add("", new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsCT));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsCT);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAF)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAF));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		else if(contador==0)
		{
			errores.add("", new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsAF));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAF);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsUS)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsUS));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		else if(contador==0)
		{
			errores.add("", new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsUS));
			///Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsUS);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAD)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAD));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAD);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		else if(contador==0)
		{
			errores.add("", new ActionMessage("errors.required","El archivo "+ConstantesBD.ripsAD));
			///Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAD);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAC)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAC));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAC);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAP)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAP));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAP);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAH)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAH));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAH);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAU)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAU));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAU);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAM)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAM));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAM);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAN)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAN));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAN);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		contador = Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAT)+"", true);
		if(contador>1)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Existen varios archivos de tipo "+ConstantesBD.ripsAT));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,ConstantesBD.ripsAT);
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"existen varios de este tipo");
			posIncon++;
		}
		
		//4) Si los archivos encontrados no tenían el mismo número de remisión se genera error
		if(diferenteNumeroRemision)
		{
			errores.add("", new ActionMessage("error.errorEnBlanco","Los archivos encontrados no poseen el mismo número de remisión. Proceso Cancelado"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"Los archivos no poseen el mismo número de remisión");
			posIncon++;
		}
		else if(numeroRemisionAnterior.length()>6)
		{
			errores.add("", new ActionMessage("errors.maxlength","El número de remisión","6"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"El número de remisión supera 6 caracteres");
			posIncon++;
		}
		
		//5) Se debe validar que al menos estén los archivos AC, AP, AM o AT
		if(
				Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAC)+"", true)==0&&
				Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAP)+"", true)==0&&
				Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAM)+"", true)==0&&
				Utilidades.convertirAEntero(contadorArchivos.get(ConstantesBD.ripsAT)+"", true)==0
			)
		{
			errores.add("",new ActionMessage("errors.minimoCampos","el archivo AC, AP, AM o AT","lectura de los planos"));
			//Se genera la inconsistencia
			elementoInconsistencia.put("archivo_"+posIncon,"---");
			elementoInconsistencia.put("campo_"+posIncon,"---");
			elementoInconsistencia.put("observaciones_"+posIncon,"es requerido como mínimo el archivo AC, AP, AM o AT");
			posIncon++;
		}
		
		//Si no hay errores entonces se reordenan los archivos en el arreglo
		if(errores.isEmpty())
		{
			posicion = Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsCT)+"", true);
			nuevoArreglo.add(this.archivos.get(posicion));
			
			posicion = Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAF)+"", true);
			nuevoArreglo.add(this.archivos.get(posicion));
			
			
			
			posicion = Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAD)+"", true);
			nuevoArreglo.add(this.archivos.get(posicion));
			
			//Para el resto de los archivos puede que venga vacío
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAC)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAC)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAP)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAP)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAH)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAH)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAU)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAU)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAM)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAM)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAN)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAN)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			posicion = UtilidadTexto.isEmpty(contadorArchivos.get("posicion"+ConstantesBD.ripsAT)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsAT)+"", true);
			if(posicion!=ConstantesBD.codigoNuncaValido)
				nuevoArreglo.add(this.archivos.get(posicion));
			else
				nuevoArreglo.add(null);
			
			//El archivo US se deja de último
			posicion = Utilidades.convertirAEntero(contadorArchivos.get("posicion"+ConstantesBD.ripsUS)+"", true);
			nuevoArreglo.add(this.archivos.get(posicion));
			
			this.archivos = nuevoArreglo;
			
			
		}
		else
		{
			//Se añade la inconsistencia
			elementoInconsistencia.put("numRegistros", posIncon+"");
			this.inconsistencias.add(elementoInconsistencia);
		}
		
	}
	/**
	 * Método implementado para cargar el mundo desde la forma
	 * @param con 
	 * @param forma
	 * @param usuario 
	 */
	private void llenarMundo(Connection con, LecturaPlanosEntidadesForm forma, UsuarioBasico usuario)
	{
		this.con = con;
		this.consecutivoEntidadSubcontratada = forma.getEntidadSubcontratada();
		this.codigoManual = forma.getCodigoManual();
		this.numeroFactura = forma.getNumeroFactura();
		this.validacionNumeroCarnet = forma.getValidacionNumeroCarnet();
		this.usuarioSession = usuario;
		
		//********************TOMA DE LOS ARCHIVOS A LEER***********************************************+
		this.ubicacionPlanosEntidadesSubcontratadas = forma.getUbicacionPlanosEntidadesSubcontratadas();
		if(this.ubicacionPlanosEntidadesSubcontratadas.equals(ConstantesIntegridadDominio.acronimoServidor))
		{
			this.directorioArchivos = forma.getDirectorioArchivos();
			File directorio = new File(this.directorioArchivos);
			//Se toman los archivos del directorio
			File[] arregloArchivo = directorio.listFiles();
			if(arregloArchivo!=null)
			{
				for(int i=0;i<arregloArchivo.length;i++)
				{
					logger.info("archivo["+i+"]: "+arregloArchivo[i].getName());
					if((
						arregloArchivo[i].getName().startsWith("CT")||
						arregloArchivo[i].getName().startsWith("AD")||
						arregloArchivo[i].getName().startsWith("AF")||
						arregloArchivo[i].getName().startsWith("AC")||
						arregloArchivo[i].getName().startsWith("US")||
						arregloArchivo[i].getName().startsWith("AM")||
						arregloArchivo[i].getName().startsWith("AP")||
						arregloArchivo[i].getName().startsWith("AN")||
						arregloArchivo[i].getName().startsWith("AH")||
						arregloArchivo[i].getName().startsWith("AU")||
						arregloArchivo[i].getName().startsWith("AT")
						)
						&&
						arregloArchivo[i].getName().toLowerCase().endsWith("txt"))
					{
						this.archivos.add(arregloArchivo[i]);
					}
				}
			}
			
			if(!this.directorioArchivos.endsWith(System.getProperty("file.separator")))
				this.directorioArchivos += System.getProperty("file.separator");
			
		}
		else if(this.ubicacionPlanosEntidadesSubcontratadas.equals(ConstantesIntegridadDominio.acronimoCliente))
		{
			this.directorioArchivos = forma.getDirectorioArchivos();
			
			File prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsCT)!=null&&!forma.getArchivos(ConstantesBD.ripsCT).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsCT);
			this.archivos.add(0,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAF)!=null&&!forma.getArchivos(ConstantesBD.ripsAF).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAF);
			this.archivos.add(1,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAD)!=null&&!forma.getArchivos(ConstantesBD.ripsAD).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAD);
			this.archivos.add(2,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAC)!=null&&!forma.getArchivos(ConstantesBD.ripsAC).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAC);
			this.archivos.add(3,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAP)!=null&&!forma.getArchivos(ConstantesBD.ripsAP).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAP);
			this.archivos.add(4,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAH)!=null&&!forma.getArchivos(ConstantesBD.ripsAH).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAH);
			this.archivos.add(5,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAU)!=null&&!forma.getArchivos(ConstantesBD.ripsAU).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAU);
			this.archivos.add(6,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAM)!=null&&!forma.getArchivos(ConstantesBD.ripsAM).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAM);
			this.archivos.add(7,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAN)!=null&&!forma.getArchivos(ConstantesBD.ripsAN).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAN);
			this.archivos.add(8,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsAT)!=null&&!forma.getArchivos(ConstantesBD.ripsAT).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsAT);
			this.archivos.add(9,prueba);
			
			prueba = null;
			if(forma.getArchivos(ConstantesBD.ripsUS)!=null&&!forma.getArchivos(ConstantesBD.ripsUS).toString().equals(""))
				prueba = (File)forma.getArchivos("archivo"+ConstantesBD.ripsUS);
			this.archivos.add(10,prueba);
			
		}
		//*****************************************************************************************
		
		//********SE LLENA LA INFORMACION DEL LOG DE LECTURA PLANOS****************************
		this.logLectura.setLoginUsuarioEjecucion(usuarioSession.getLoginUsuario());
		this.logLectura.setFechaEjecucion(UtilidadFecha.getFechaActual(con));
		this.logLectura.setHoraEjecucion(UtilidadFecha.getHoraActual(con));
		this.logLectura.setCodigoManual(this.codigoManual);
		this.logLectura.setCodigoEntidadSubcontratada(this.consecutivoEntidadSubcontratada);
		this.logLectura.setNumeroFactura(this.numeroFactura);
		if(!this.validacionNumeroCarnet.equals(""))
			this.logLectura.setValidarArchivosCarnet(UtilidadTexto.getBoolean(this.validacionNumeroCarnet));
		this.logLectura.setUbicacionPlanos(this.ubicacionPlanosEntidadesSubcontratadas);
		this.logLectura.setCodigoInstitucion(usuarioSession.getCodigoInstitucionInt());
		this.logLectura.setCodigoCentroAtencion(usuarioSession.getCodigoCentroAtencion());
		this.logLectura.setDirectorioArchivos(this.directorioArchivos);
	}
	
	
	/**
	 * Método usado para cargar el archivo de inconsistencias
	 * @param path
	 * @return
	 */
	public static HashMap<String, Object> cargarArchivoInconsistencias(String path)
	{
		///objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		
		try
		{
			int contador=0;
			String cadena="";
			//******SE INICIALIZA ARCHIVO*************************
			File archivo=new File(path);
			FileReader stream=new FileReader(archivo); 
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				//se almacena cada línea en el hashmap
				contenido.put(contador+"",cadena);
				contador++;
				cadena=buffer.readLine();
			}
			contenido.put("numElementos",contador+"");
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
		
			return contenido;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+path+" al cargarlo: "+e);
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+path+" al cargarlo: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para realizar la inserción en el archivo AF
	 * @param con
	 * @param campos
	 * @param inconsistencia 
	 * @return
	 */
	private int insercionRegistroArchivo(Connection con,String[] campos,String archivo, boolean inconsistencia)
	{
		HashMap parametros = new HashMap();
		for(int i=0;i<campos.length;i++)
			parametros.put("campo"+(i+1), campos[i]);
		parametros.put("archivo",archivo);
		parametros.put("numeroCampos", campos.length);
		parametros.put("usuario", usuarioSession.getLoginUsuario());
		parametros.put("codigoCentroAtencion", usuarioSession.getCodigoCentroAtencion());
		parametros.put("inconsistencia", inconsistencia?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return lecturaDao.insercionRegistroArchivo(con, parametros);
	}
	//*****************************************************************************************************
	/**
	 * @return the archivos
	 */
	public ArrayList<File> getArchivos() {
		return archivos;
	}
	/**
	 * @param archivos the archivos to set
	 */
	public void setArchivos(ArrayList<File> archivos) {
		this.archivos = archivos;
	}
	/**
	 * @return the codigoManual
	 */
	public int getCodigoManual() {
		return codigoManual;
	}
	/**
	 * @param codigoManual the codigoManual to set
	 */
	public void setCodigoManual(int codigoManual) {
		this.codigoManual = codigoManual;
	}
	/**
	 * @return the consecutivoEntidadSubcontratada
	 */
	public String getConsecutivoEntidadSubcontratada() {
		return consecutivoEntidadSubcontratada;
	}
	/**
	 * @param consecutivoEntidadSubcontratada the consecutivoEntidadSubcontratada to set
	 */
	public void setConsecutivoEntidadSubcontratada(
			String consecutivoEntidadSubcontratada) {
		this.consecutivoEntidadSubcontratada = consecutivoEntidadSubcontratada;
	}
	/**
	 * @return the directorioArchivos
	 */
	public String getDirectorioArchivos() {
		return directorioArchivos;
	}
	/**
	 * @param directorioArchivos the directorioArchivos to set
	 */
	public void setDirectorioArchivos(String directorioArchivos) {
		this.directorioArchivos = directorioArchivos;
	}
	/**
	 * @return the ubicacionPlanosEntidadesSubcontratadas
	 */
	public String getUbicacionPlanosEntidadesSubcontratadas() {
		return ubicacionPlanosEntidadesSubcontratadas;
	}
	/**
	 * @param ubicacionPlanosEntidadesSubcontratadas the ubicacionPlanosEntidadesSubcontratadas to set
	 */
	public void setUbicacionPlanosEntidadesSubcontratadas(
			String ubicacionPlanosEntidadesSubcontratadas) {
		this.ubicacionPlanosEntidadesSubcontratadas = ubicacionPlanosEntidadesSubcontratadas;
	}
	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	/**
	 * @return the validacionNumeroCarnet
	 */
	public String getValidacionNumeroCarnet() {
		return validacionNumeroCarnet;
	}
	/**
	 * @param validacionNumeroCarnet the validacionNumeroCarnet to set
	 */
	public void setValidacionNumeroCarnet(String validacionNumeroCarnet) {
		this.validacionNumeroCarnet = validacionNumeroCarnet;
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
	 * @return the huboInconsistencias
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	/**
	 * @param huboInconsistencias the huboInconsistencias to set
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	/**
	 * @return the inconsistencias
	 */
	public ArrayList<HashMap<String, Object>> getInconsistencias() {
		return inconsistencias;
	}
	/**
	 * @param inconsistencias the inconsistencias to set
	 */
	public void setInconsistencias(
			ArrayList<HashMap<String, Object>> inconsistencias) {
		this.inconsistencias = inconsistencias;
	}
	/**
	 * @return the numeroRemision
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}
	/**
	 * @param numeroRemision the numeroRemision to set
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}
	/**
	 * @return the pathArchivoInconsistencias
	 */
	public String getPathArchivoInconsistencias() {
		return pathArchivoInconsistencias;
	}
	/**
	 * @param pathArchivoInconsistencias the pathArchivoInconsistencias to set
	 */
	public void setPathArchivoInconsistencias(String pathArchivoInconsistencias) {
		this.pathArchivoInconsistencias = pathArchivoInconsistencias;
	}
	/**
	 * @return the numeroIngresosRegistrados
	 */
	public int getNumeroIngresosRegistrados() {
		return numeroIngresosRegistrados;
	}
	/**
	 * @param numeroIngresosRegistrados the numeroIngresosRegistrados to set
	 */
	public void setNumeroIngresosRegistrados(int numeroIngresosRegistrados) {
		this.numeroIngresosRegistrados = numeroIngresosRegistrados;
	}
}
