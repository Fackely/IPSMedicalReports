/*
 * Feb 06, 2008
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;
import util.salas.ConstantesBDSalas;
import util.salas.UtilidadesSalas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.LiquidacionServiciosDao;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.cargos.TarifaISS;
import com.princetonsa.mundo.cargos.TarifaSOAT;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.Servicio;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

public class LiquidacionServicios 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(LiquidacionServicios.class);
	/**
	 * DAO para el manejo de LiquidacionServiciosDao
	 */
	private LiquidacionServiciosDao liquidacionDao=null;
	
	private String numeroSolicitud, numeroPeticion, indQx;
	private HashMap<String, Object> cirugias = new HashMap<String, Object>();
	private HashMap<String, Object> datosActoQx = new HashMap<String, Object>();
	private HashMap<String, Object> encabezadoOrden = new HashMap<String, Object>();
	private HashMap<String, Object> otrosProfesionales = new HashMap<String, Object>();
	private ArrayList<HashMap<String, Object>> materialesEspeciales = new ArrayList<HashMap<String,Object>>();
	
	//numeroAutorizacion,
	private String idIngreso, fechaFinalizacionHQ, codigoTipoPaciente, idCuenta;
	private int codigoViaIngreso, tarifarioOficialGeneral, codigoCentroCostoEjecuta, codigoTipoSala, codigoTipoAnestesia, codigoEspecialidadInterviene;
	boolean politraumatismo, debeLiquidarConsumoMateriales, esTarifaLiquidacionMateriales, debeLiquidarSalaXRangoTiempo, porDuracionCirugia, debeLiquidarTodosXRangoTiempos;
	boolean debeLiquidarMaterialesEspeciales;
	String logOriginal, logModificado; //atributos usados para la generación del log
	private UsuarioBasico usuario;
	private InfoResponsableCobertura infoCobertura;
	private Convenio mundoConvenio;
	private Connection con;
	
	//Atributos para el manejo de los mensajes
	private ArrayList<ElementoApResource> mensajesError = new ArrayList<ElementoApResource>();
	
	//Atributos para la ordenación de cirugías
	public static final String[] indicesCirugias = {"codigo_","codigoServicio_","numeroServicio_","codigoCups_","descripcionServicio_",
		"codigoEspecialidad_","descripcionEspecialidad_","esPos_","codigoTipoCirugia_","nombreTipoCirugia_","acronimoTipoCirugia_","codigoCirujano_",
		"nombreCirujano_","codigoAyudante_","nombreAyudante_","codigoEsquemaTarifario_","nombreEsquemaTarifario_","grupoUvr_","codigoViaCx_",
		"nombreViaCx_",/*"autorizacion_","autorizacionAntigua_",*/"valor_","codigoFinalidadCx_","nombreFinalidadCx_","fueEliminadoServicio_","estaBD_","observaciones_",
		"indBilateral_","indViaAcceso_","esViaAcceso_","codigoEspecialidadInterviene_","nombreEspecialidadInterviene_","liquidarServicio_","tarifarioOficial_",
		"codigoTipoLiquidacion_","liquidarAsocios_","cantidadEsquemaTarifario_","numProfesionales_","codigoFormulario_"};
	private HashMap<String, Object> cirugiasOrdenadas = new HashMap<String, Object>();
	
	//Atributos para la consulta de profesionales de la cirugia
	public static final String[] indicesProfesionalesCirugia = {"consecutivo_","consecutivoAsocio_","nombreAsocio_","tipoAsocio_",
		"nombreTipoAsocio_","codigoEspecialidad_","nombreEspecialidad_","codigoProfesional_","nombreProfesional_","cobrable_","pool_","nombrePool_",
		"tipoEspecialista_","historiaClinica_","existeBd_","eliminar_"};
	private int codigoCirujano;
	private String nombreCirujano;
	
	//Indices de los asocios liquidados
	public static final String[] indicesAsociosLiquidados = {"consecutivo_","codSolCxServicio_","codigoEspecialidad_","nombreEspecialidad_",
		"codigoProfesional_","nombreProfesional_","codigoServicio_","nombreServicio_","codigoPool_","nombrePool_","codigoAsocio_","nombreAsocio_",
		"codigoPropietario_","valor_"};
	
	//Atributos propios del proceso de liquidacion
	private HashMap<String, Object> asociosOrden = new HashMap<String, Object>(); //asocios de la orden 
	private HashMap<String, Object> asociosCirugia = new HashMap<String, Object>(); //asocios x cirugia
	private boolean tieneTituloMensajeCirugia;
	private String tituloMensajeCirugia01;
	private String tituloMensajeCirugia02;
	private String tipoServicio, nombreTipoServicio, via, consecutivoServicio, acronimoTipoCirugia, acronimoTipoCirugiaEspecialidad, nombreTipoCirugia, nombreTipoCirugiaEspecialidad, tipoEspecialistaCirujano;
	private int codigoEsquemaTarifario, codigoEspecialidad, codigoGrupoServicio, codigoServicio, modoLiquidacion, numeroCirugia;
	private double grupoUvrServicio, valorServicio;
	private boolean esBilateral, esCirugiaPorConsumo;
	private EsquemaTarifario mundoEsquemaTarifario ;
	///*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public LiquidacionServicios() 
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
		this.numeroSolicitud = "";
		this.numeroPeticion = "";
		this.indQx = "";
		this.cirugias = new HashMap<String, Object>();
		this.datosActoQx = new HashMap<String, Object>();
		this.encabezadoOrden = new HashMap<String, Object>();
		this.otrosProfesionales = new HashMap<String, Object>();
		this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
		
		this.idIngreso = "";
		this.idCuenta = "";
		//this.numeroAutorizacion = "";
		this.fechaFinalizacionHQ = "";
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido;
		this.tarifarioOficialGeneral = ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoEjecuta = ConstantesBD.codigoNuncaValido;
		this.codigoTipoSala = ConstantesBD.codigoNuncaValido;
		this.codigoTipoAnestesia = ConstantesBD.codigoNuncaValido;
		this.codigoEspecialidadInterviene = ConstantesBD.codigoNuncaValido;
		this.politraumatismo = false;
		this.logOriginal = "";
		this.logModificado = "";
		this.debeLiquidarMaterialesEspeciales = false;
		this.debeLiquidarConsumoMateriales = false;
		this.esTarifaLiquidacionMateriales = false;
		this.debeLiquidarSalaXRangoTiempo = false;
		this.debeLiquidarTodosXRangoTiempos = false;
		this.porDuracionCirugia = false;
		this.usuario = new UsuarioBasico();
		this.infoCobertura = new InfoResponsableCobertura();
		this.mundoConvenio = new Convenio();
		this.con = null;
		
		//atributos para los mensajes de error
		this.mensajesError = new ArrayList<ElementoApResource>();
		
		//atributos para la ordenación de cirugías
		this.cirugiasOrdenadas = new HashMap<String, Object>();
		
		//atributos para la consulta de los profesionales de la cirugía
		this.codigoCirujano = ConstantesBD.codigoNuncaValido;
		this.nombreCirujano = "";
		
		//atributos propios del proceso de liquidacion
		this.asociosOrden = new HashMap<String, Object>();
		this.asociosCirugia = new HashMap<String, Object>();
		this.tieneTituloMensajeCirugia = false;
		this.tituloMensajeCirugia01 = "";
		this.tituloMensajeCirugia02 = "";
		this.tipoServicio = "";
		this.nombreTipoServicio = "";
		this.acronimoTipoCirugia = "";
		this.acronimoTipoCirugiaEspecialidad = "";
		this.nombreTipoCirugia = "";
		this.nombreTipoCirugiaEspecialidad = "";
		this.tipoEspecialistaCirujano = "";
		this.codigoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.codigoGrupoServicio = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.numeroCirugia = ConstantesBD.codigoNuncaValido;
		this.grupoUvrServicio = ConstantesBD.codigoNuncaValidoDouble;
		this.valorServicio = ConstantesBD.codigoNuncaValidoDouble;
		this.modoLiquidacion = ConstantesBD.codigoNuncaValido;
		this.esBilateral = false;
		this.esCirugiaPorConsumo = false;
		this.via = "";
		this.consecutivoServicio = "";
		mundoEsquemaTarifario = new EsquemaTarifario();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (liquidacionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			liquidacionDao = myFactory.getLiquidacionServiciosDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Hoja Gastos
	 * @return
	 */
	public static LiquidacionServiciosDao liquidacionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getLiquidacionServiciosDao();
	}
	
	//*******************************************************************************************************************************
	//************************METODOS MANEJO INFORMACION ORDEN***********************************************************************
	//*******************************************************************************************************************************
	/**
	 * Método que realiza la consulta las ordenes de cirugia para liquidar
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> consultarListadoOrdenes(Connection con,String codigoIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoIngreso",codigoIngreso);
		return liquidacionDao().consultarListadoOrdenes(con, campos);
	}
	
	/**
	 * Método implementado para cargar el detalle de una orden
	 *
	 */
	public void cargarDetalleOrden()
	{
		//Solicitud General
		Solicitud solicitud = new Solicitud();
		try
		{
			solicitud.cargar(con,Integer.parseInt(this.numeroSolicitud));
		}
		catch(SQLException e)
		{
			logger.error("Hubo error aqui=> "+e);
			
		}
		//Solicitud Cirugia
		SolicitudesCx solicitudCx = new SolicitudesCx();
		solicitudCx.cargarEncabezadoSolicitudCx(con, this.numeroSolicitud);
		
		this.numeroPeticion = solicitudCx.getCodigoPeticion();
		this.indQx = solicitudCx.getIndQx();
		this.codigoViaIngreso = Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con, solicitud.getCodigoCuenta()+""));
		this.codigoTipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, solicitud.getCodigoCuenta()+"").getAcronimo();
		
		this.codigoCentroCostoEjecuta = solicitud.getCentroCostoSolicitado().getCodigo();
		this.idCuenta = solicitud.getCodigoCuenta()+"";
		this.encabezadoOrden.put("orden",solicitud.getConsecutivoOrdenesMedicas()+"");
		this.encabezadoOrden.put("fechaOrden",solicitud.getFechaSolicitud());
		//this.encabezadoOrden.put("autorizacion",solicitud.getNumeroAutorizacion());
		//this.encabezadoOrden.put("autorizacionAntigua",solicitud.getNumeroAutorizacion());
		this.encabezadoOrden.put("horaOrden",solicitud.getHoraSolicitud());
		this.encabezadoOrden.put("nombreCentroCostoSolicitado",solicitud.getCentroCostoSolicitado().getNombre());
		//***************************************************************************************************************
		
		//********************SE CONSULTA LA INFORMACIÓN DEL ACTO QUIRURGICO*******************************************
		//Se consultan los datos encabezado de la hoja quirurgica
		this.datosActoQx = liquidacionDao.cargarDatosActoQuirurgico(con, this.numeroSolicitud,usuario.getCodigoInstitucionInt());
		this.encabezadoOrden.put("medicoResponde",datosActoQx.get("medicoResponde"));
		this.datosActoQx.put("fechaInicialCx", solicitudCx.getFechaInicialCx());
		this.datosActoQx.put("horaInicialCx", solicitudCx.getHoraInicialCx());
		this.datosActoQx.put("fechaFinalCx", solicitudCx.getFechaFinalCx());
		this.datosActoQx.put("horaFinalCx", solicitudCx.getHoraFinalCx());
		this.datosActoQx.put("fechaIngresoSala", solicitudCx.getFechaIngresoSala());
		this.datosActoQx.put("fechaSalidaSala", solicitudCx.getFechaSalidaSala());
		this.datosActoQx.put("horaIngresoSala", solicitudCx.getHoraIngresoSala());
		this.datosActoQx.put("horaSalidaSala", solicitudCx.getHoraSalidaSala());
		this.datosActoQx.put("codigoSalidaSalaPaciente", solicitudCx.getCodigoSalidaSalaPaciente());
		
		try
		{
			this.datosActoQx.put("duracion","");
			
			if(!solicitudCx.getFechaIngresoSala().equals("") && !solicitudCx.getHoraIngresoSala().equals("") &&
					!solicitudCx.getFechaSalidaSala().equals("") && !solicitudCx.getHoraSalidaSala().equals(""))
			{			
				this.datosActoQx.put("duracion", 
					UtilidadFecha.calcularDuracionEntreFechas(
						solicitudCx.getFechaIngresoSala(), 
						solicitudCx.getHoraIngresoSala(), 
						solicitudCx.getFechaSalidaSala(), 
						solicitudCx.getHoraSalidaSala()));
			}
			
			this.datosActoQx.put("duracionCirugia","");
			
			if(!solicitudCx.getFechaInicialCx().equals("") && !solicitudCx.getHoraInicialCx().equals("") &&
					!solicitudCx.getFechaFinalCx().equals("") && !solicitudCx.getHoraFinalCx().equals(""))
			{
				this.datosActoQx.put("duracionCirugia", 
						UtilidadFecha.calcularDuracionEntreFechas(
							solicitudCx.getFechaInicialCx(), 
							solicitudCx.getHoraInicialCx(), 
							solicitudCx.getFechaFinalCx(), 
							solicitudCx.getHoraFinalCx()));
			}
		}
		catch(Exception e)
		{
			logger.error("Error al calcular la duracion de la sala o cirugia: "+e);
			this.datosActoQx.put("duracion","");
			this.datosActoQx.put("duracionCirugia","");
		}
		
		//Si ya se había registrado el indicador de liquidar anestesia se agrega en solicitudes cirugia
		if(!solicitudCx.getLiquidarAnestesia().equals(""))
			this.datosActoQx.put("cobrarAnestesia", solicitudCx.getLiquidarAnestesia());
		
		//Se consultan los otros profesionales
		
		this.otrosProfesionales =  liquidacionDao.consultarOtrosProfesionales(con, numeroSolicitud);
		//Utilidades.imprimirMapa(this.otrosProfesionales);
		//***************************************************************************************************************
	}
	
	
	
	
	
	
	/**
	 * Método para realizar el reordenamiento de los servicios de la solicitud de cirugías
	 *
	 */
	public void reordenarCirugiasSolicitud(boolean deboConsultar, boolean deboOrdenar) throws IPSException
	{
		//Mapa par almacenar la ordenación de las cirugías
		this.cirugiasOrdenadas = new HashMap<String, Object>();
		
		//Se inicializan los mensajes de error
		this.mensajesError = new ArrayList<ElementoApResource>();
		
		//Se consultan las cirugias de la solicitud
		if(deboConsultar)
		{
			cargarServiciosOrden();
			this.consultarProfesionalesXCirugia(ConstantesBD.codigoNuncaValido);
		}
		
		//***********************PREPARACION DEL ESQUEMA TARIFARIO ************************************
		int esquemaTarifario = ConstantesBD.codigoNuncaValido, esquemaTarifarioDefault = ConstantesBD.codigoNuncaValido;
		
		//Si ya hay al menos unoa cirugía y ya se tiene su servicio ingresado, se calcula la cobertura
		if(this.getNumCirugias()>0&&
			this.cirugias.get("codigoServicio_0")!=null&&
			Integer.parseInt(this.cirugias.get("codigoServicio_0").toString())>0)
		{
			//**********VALIDACION PARA EL CONTROL POST-OPERATORIO****************************************
			/**
			 * Si el ingreso de la orden es de control-postoperatorio, no se puede validar la cobertura
			 * sino que se debe liquidar con el convenio que venga de la solicitud de cirugia
			 */
			String subCuentaCobertura = "";
			if(UtilidadesManejoPaciente.esIngresoControlPostOperatorioCx(con, this.idIngreso))
				subCuentaCobertura = UtilidadesOrdenesMedicas.obtenerSubCuentaSolicitudCirugia(con, this.numeroSolicitud);
			//********************************************************************************************
			
			//Se consulta el convenio por validación de cobertura
			//La cobertura se calcula de acuerd con la primera cirugia
			InfoResponsableCobertura infoResponsable = Cobertura.validacionCoberturaServicio(
				con, 
				this.idIngreso, 
				this.codigoViaIngreso,
				UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con,this.numeroSolicitud+"").getCodigo(),
				Integer.parseInt(this.cirugias.get("codigoServicio_0").toString()), 
				usuario.getCodigoInstitucionInt(),
				Utilidades.esSolicitudPYP(con, Integer.parseInt(this.numeroSolicitud)),
				subCuentaCobertura /*subCuentaCoberturaOPCIONAL*/);
			
			//Se asigna el convenio
			esquemaTarifarioDefault = infoResponsable.getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,infoResponsable.getDtoSubCuenta().getSubCuenta(),infoResponsable.getDtoSubCuenta().getContrato(),Integer.parseInt(this.cirugias.get("codigoServicio_0").toString()),this.datosActoQx.get("fechaFinalCx").toString(), Cargos.obtenerCentroAtencionCargoSolicitud(con,Utilidades.convertirAEntero(this.numeroSolicitud)));
			this.setInfoCobertura(infoResponsable);
		}
		else
		{
			this.setInfoCobertura(new InfoResponsableCobertura());
		}
		
		//***************************************************************************
		//preparacion del tarifario oficial ********************************************
		int tarifarioOficial = ConstantesBD.codigoTarifarioCups;
		this.tarifarioOficialGeneral = ConstantesBD.codigoTarifarioCups;
		tarifarioOficialGeneral = this.cargarTarifarioOficial(con,esquemaTarifarioDefault);
		
		
		//******************************************************************************
		
		
		//**********************SE CONSULTA EL TIPO DE LIQUIDACION DE CADA SERVICIO***************************************
		for(int i=0;i<this.getNumCirugias();i++)
		{
			boolean liquidarServicio = UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString()); 
			
			//asignacion de la autorizacion *************************************
			/*String autorizacion = this.numeroAutorizacion;
			String autServicio = this.cirugias.get("autorizacion_"+i).toString();
			this.cirugias.put("autorizacionAntigua_"+i,autServicio);
			if(!autorizacion.equals("")&&!autorizacion.equals("null")&&autServicio.equals(""))
			{
				this.cirugias.put("autorizacion_"+i, autorizacion);
				this.cirugias.put("autorizacionAntigua_"+i, autorizacion);
			}
			*/
			//********************************************************************
			
			//asignación del esquema tarifario por defecto ********************************************
			String esqTarStr = this.cirugias.get("codigoEsquemaTarifario_"+i)+"";
			if(Utilidades.convertirAEntero(esqTarStr, true)<=0)
			{
				esquemaTarifario = esquemaTarifarioDefault;
				tarifarioOficial = tarifarioOficialGeneral;
				this.cirugias.put("codigoEsquemaTarifario_"+i,esquemaTarifario+"");
			}
			else
			{
				esquemaTarifario = Integer.parseInt(this.cirugias.get("codigoEsquemaTarifario_"+i)+"");
				tarifarioOficial = this.cargarTarifarioOficial(con,esquemaTarifario);
				
				//Puede suceder que no se tenga tarifario oficial por default
				if(tarifarioOficialGeneral == ConstantesBD.codigoTarifarioCups)
					tarifarioOficialGeneral = tarifarioOficial; //entonces se asigna el que se encuentre en los demás servicios
				//Si la cirugia ya tenía un esqeuma tarifario con diferetne tipo de tarifario calculado en la cobertura
				//entonces se reasigna el esquema tarifario de la cobertura
				else if(tarifarioOficialGeneral != tarifarioOficial)
				{
					esquemaTarifario = esquemaTarifarioDefault;
					tarifarioOficial = tarifarioOficialGeneral;
					this.cirugias.put("codigoEsquemaTarifario_"+i, esquemaTarifario);
				}
				
			}
			//****************************************************************************************
			
			
			//asignacion del tarifario Oficial *************************************
			this.cirugias.put("tarifarioOficial_"+i,tarifarioOficial+"");
			//*****************************************************************************
			
			//consultar el codigo propietario del servicio segun esquema tarifario****************************
			int codigoServicio = Utilidades.convertirAEntero(this.cirugias.get("codigoServicio_"+i)+"",true);
			Servicio servicio = new Servicio(codigoServicio);
			try 
			{
				servicio.cargarCodigosParticulares(con, usuario.getCodigoInstitucionInt());
			} catch (SQLException e1) {
				logger.error("Error cargando servicio en prepararDatosCirugiasSolicitud de LiquidacionQxAction: "+e1);
			}
			if(tarifarioOficial==ConstantesBD.codigoTarifarioISS&&!servicio.getInformacionISS().getNombre().equals(""))
				this.cirugias.put("codigoCups_"+i,servicio.getInformacionISS().getNombre());
			else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat&&!servicio.getInformacionSoat().getNombre().equals(""))
				this.cirugias.put("codigoCups_"+i,servicio.getInformacionSoat().getNombre());
			//*****************************************************************************************************
			
			
			//consultar el grupo / UVR / Unidades / Valor **************************************************************************
			
			if(codigoServicio>0)
			{
				if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
				{
					TarifaSOAT tarifaSOAT = new TarifaSOAT();
					
					String fechaVigencia=UtilidadFecha.getFechaActual();
					if(UtilidadFecha.esFechaValidaSegunAp(this.datosActoQx.get("fechaFinaliza")+""))
					{	
						fechaVigencia=this.datosActoQx.get("fechaFinaliza").toString();
						logger.info("FECHA VALIDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
					}
					tarifaSOAT.cargar(con,codigoServicio,esquemaTarifario, fechaVigencia);
					this.cirugias.put("codigoTipoLiquidacion_"+i,tarifaSOAT.getCodigoTipoLiquidacion()+"");
					//se revisa si el GRUPO es correcto
					if(tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatGrupo)
					{
						if(tarifaSOAT.getGrupo()>0)
							this.cirugias.put("grupoUvr_"+i,UtilidadTexto.formatearValores(tarifaSOAT.getGrupo()+"",0,false,false));
						else
						{
							this.cirugias.put("grupoUvr_"+i,"");
							if(liquidarServicio)
								this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "grupo SOAT válido");
						}
						
						this.cirugias.put("liquidarAsocios_"+i,ConstantesBD.acronimoSi); //Por defecto SI aunque no aplica
					}
					//Se revisa si las UNIDADES O VALOR son correctas
					else if(tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades||tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
					{
						if(tarifaSOAT.getValorTarifa()>0)
							this.cirugias.put("grupoUvr_"+i,UtilidadTexto.formatearValores(tarifaSOAT.getValorTarifa(),"0.00"));
						else
						{
							this.cirugias.put("grupoUvr_"+i,"");
							if(liquidarServicio)
								this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "unidades/valor SOAT parametrizado");
						}
						
						this.cirugias.put("liquidarAsocios_"+i,UtilidadTexto.getBoolean(tarifaSOAT.getLiquidarAsocios())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);						
					}
					//De lo contrario no hay parametrización
					else
					{
						this.cirugias.put("grupoUvr_"+i,"");
						this.cirugias.put("liquidarAsocios_"+i,""); 
						this.cirugias.put("codigoTipoLiquidacion_"+i,ConstantesBD.codigoNuncaValido+"");
						if(liquidarServicio)
							this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "parametrización de tarifa SOAT");
					}
				}
				else if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
				{
					TarifaISS tarifaISS = new TarifaISS();
					
					String fechaVigencia=UtilidadFecha.getFechaActual();
					if(UtilidadFecha.esFechaValidaSegunAp(this.datosActoQx.get("fechaFinaliza")+""))
					{	
						fechaVigencia=this.datosActoQx.get("fechaFinaliza").toString();
						logger.info("FECHA VALIDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
					}	
					tarifaISS.cargar(con,codigoServicio,esquemaTarifario, fechaVigencia);
					this.cirugias.put("codigoTipoLiquidacion_"+i,tarifaISS.getCodigoTipoLiquidacion()+"");
					//se revisa si el UVR es correcto
					if(tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
					{
						if(tarifaISS.getUnidades()>0)
							this.cirugias.put("grupoUvr_"+i,UtilidadTexto.formatearValores(tarifaISS.getUnidades()+"","0.00"));
						else
						{
							this.cirugias.put("grupoUvr_"+i,"");
							if(liquidarServicio)
								this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "valor UVR válido");
						}
						
						this.cirugias.put("liquidarAsocios_"+i,ConstantesBD.acronimoSi); //Por defecto SI aunque no aplica
					}
					//Se revisa si las UNIDADES O VALOR son correctas
					else if(tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades||tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
					{
						if(tarifaISS.getValorTarifa()>0)
						{
							if(tarifaISS.getUnidades()>0)
							{
								this.cirugias.put("grupoUvr_"+i,UtilidadTexto.formatearValores(tarifaISS.getValorTarifa(),"0.00"));
							}
							else
							{
								this.cirugias.put("grupoUvr_"+i,"");
								if(liquidarServicio)
									this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "parametrización correcta de unidades ISS");
							}
						}
						else
						{
							this.cirugias.put("grupoUvr_"+i,"");
							if(liquidarServicio)
								this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "unidades/valor ISS parametrizado");
						}
						
						this.cirugias.put("liquidarAsocios_"+i,UtilidadTexto.getBoolean(tarifaISS.getLiquidarAsocios())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);						
					}
					//De lo contrario no hay parametrización
					else
					{
						this.cirugias.put("grupoUvr_"+i,"");
						this.cirugias.put("liquidarAsocios_"+i,""); 
						this.cirugias.put("codigoTipoLiquidacion_"+i,ConstantesBD.codigoNuncaValido+"");
						if(liquidarServicio)
							this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "parametrización de tarifa ISS");
					}
					
				}
				else
				{
					this.cirugias.put("codigoTipoLiquidacion_"+i,ConstantesBD.codigoNuncaValido);
					this.cirugias.put("grupoUvr_"+i,"");
					this.cirugias.put("liquidarAsocios_"+i,"");
					if(liquidarServicio)
						this.agregarErrorApResource("error.salasCirugia.noTiene", "la cirugía "+this.cirugias.get("descripcionServicio_"+i), "esquema tarifario definido");
					
				}
				
				//Se verifica si el servicio es de vía de acceso
				this.cirugias.put("esViaAcceso_"+i, UtilidadesSalas.esServicioViaAcceso(con, codigoServicio, usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			}
			else
			{
				this.cirugias.put("codigoTipoLiquidacion_"+i,ConstantesBD.codigoNuncaValido);
				this.cirugias.put("grupoUvr_"+i,"");
				this.cirugias.put("liquidarAsocios_"+i,"");
				this.cirugias.put("esViaAcceso_"+i,ConstantesBD.acronimoNo);
				this.cirugias.put("indViaAcceso_"+i,"");
			}
			//*********************************************************************************************************
			
			//***obtener cantidad del esquema tarifario***********************************************
			EsquemaTarifario mundoEsqTar = new EsquemaTarifario();
			try 
			{
				mundoEsqTar.cargarXcodigo(con,esquemaTarifario,usuario.getCodigoInstitucionInt());
				this.cirugias.put("cantidadEsquemaTarifario_"+i,mundoEsqTar.getCantidad()+"");
			} 
			catch (SQLException e) 
			{
				logger.error("Error al cargar la cantidad del Esquema tarifario en prepararDatosCirugiasSolicitud de SqlBaseLiquidacionQxDao: "+e);
				this.cirugias.put("cantidadEsquemaTarifario_"+i,"0");
			}
			//**************************************************************************************
			
		}//Fin for cirugías
		//***********************************************************************************************
		
		//Solo si se debe ordenar
		if(deboOrdenar)
		{
			HashMap<String, Object> cirugiasOrdenadasEspecialidad = new HashMap<String, Object>();
		
			//Primero se realiza ordenación por especialidad
			while(this.getNumCirugias()>0)
			{
				//SE OBTIENE EL SERVICIO DE MAYOR GRUPO/UVR**********************************
				this.obtenerMayorServicio(true);
				//SE AGRUPAN LOS SERVICIO POR ESPECIALIDAD
				this.agruparServiciosPorEspecialidad();
				//********************************************************************************************
			}
			///Ya las cirugias ordenadas pasarán a ser las cirugías normales
			this.cirugias = this.cirugiasOrdenadas;
			cirugiasOrdenadasEspecialidad = new HashMap<String, Object>(cirugiasOrdenadas);
			this.cirugiasOrdenadas = new HashMap<String, Object>(); //se limpia el mapa de ordenación
			
			
			logger.info("**************************MAPA ORDENACIÓN POR ESPECIALIDAD************************");
			for(int i=0;i<Utilidades.convertirAEntero(cirugiasOrdenadasEspecialidad.get("numRegistros")+"", true);i++)
			{
				logger.info("codigo ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("codigo_"+i));
				logger.info("codigo tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("codigoTipoCirugia_"+i));
				logger.info("acronimo tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("acronimoTipoCirugia_"+i));
				logger.info("nombre tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("nombreTipoCirugia_"+i));
			}
			logger.info("**************************FIN MAPA ORDENACIÓN POR ESPECIALIDAD************************");
			
			//Segundo, se realiza ordenación general y definitiva  sin especialidad
			while(this.getNumCirugias()>0)
			{
				//SE OBTIENE EL SERVICIO DE MAYOR GRUPO/UVR**********************************
				this.obtenerMayorServicio(false);
				//********************************************************************************************
			}
			
			logger.info("**************************MAPA ORDENACIÓN NORMAL************************");
			for(int i=0;i<this.getNumCirugiasOrdenadas();i++)
			{
				logger.info("codigo ["+i+"]: "+this.cirugiasOrdenadas.get("codigo_"+i));
				logger.info("codigo tipo cirugia ["+i+"]: "+this.cirugiasOrdenadas.get("codigoTipoCirugia_"+i));
				logger.info("acronimo tipo cirugia ["+i+"]: "+this.cirugiasOrdenadas.get("acronimoTipoCirugia_"+i));
				logger.info("nombre tipo cirugia ["+i+"]: "+this.cirugiasOrdenadas.get("nombreTipoCirugia_"+i));
			}
			logger.info("**************************FIN MAPA ORDENACIÓN NORMAL************************");
			logger.info("**************************MAPA ORDENACIÓN POR ESPECIALIDAD (por segunda vez)************************");
			for(int i=0;i<Utilidades.convertirAEntero(cirugiasOrdenadasEspecialidad.get("numRegistros")+"", true);i++)
			{
				logger.info("codigo ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("codigo_"+i));
				logger.info("codigo tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("codigoTipoCirugia_"+i));
				logger.info("acronimo tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("acronimoTipoCirugia_"+i));
				logger.info("nombre tipo cirugia ["+i+"]: "+cirugiasOrdenadasEspecialidad.get("nombreTipoCirugia_"+i));
			}
			logger.info("**************************FIN MAPA ORDENACIÓN POR ESPECIALIDAD (por segunda vez)************************");
			
			//Ya las cirugias ordenadas pasarán a ser las cirugías normales
			this.cirugias = this.cirugiasOrdenadas;
			
			
			//SE toma el tipo de cirugia que se calculó en la ordenación por especialidad
			for(int i=0;i<this.getNumCirugias();i++)
				for(int j=0;j<Utilidades.convertirAEntero(cirugiasOrdenadasEspecialidad.get("numRegistros")+"", true);j++)
					if(Integer.parseInt(this.cirugias.get("codigoServicio_"+i).toString())==Integer.parseInt(cirugiasOrdenadasEspecialidad.get("codigoServicio_"+j).toString()))
					{
						this.cirugias.put("codigoTipoCirugiaEspecialidad_"+i,cirugiasOrdenadasEspecialidad.get("codigoTipoCirugia_"+j).toString());
						this.cirugias.put("nombreTipoCirugiaEspecialidad_"+i,cirugiasOrdenadasEspecialidad.get("nombreTipoCirugia_"+j).toString());
						this.cirugias.put("acronimoTipoCirugiaEspecialidad_"+i,cirugiasOrdenadasEspecialidad.get("acronimoTipoCirugia_"+j).toString());
					}
			
			logger.info("**************************MAPA ORDENACIÓN FINAL CIRUGÍAS************************");
			for(int i=0;i<this.getNumCirugias();i++)
			{
				logger.info("codigo servicio ["+i+"]: "+this.cirugias.get("codigoServicio_"+i));
				logger.info("codigo tipo cirugia ["+i+"]: "+this.cirugias.get("codigoTipoCirugia_"+i));
				logger.info("acronimo tipo cirugia ["+i+"]: "+this.cirugias.get("acronimoTipoCirugia_"+i));
				logger.info("nombre tipo cirugia ["+i+"]: "+this.cirugias.get("nombreTipoCirugia_"+i));
				logger.info("codigo tipo cirugia especialidad ["+i+"]: "+this.cirugias.get("codigoTipoCirugiaEspecialidad_"+i));
				logger.info("acronimo tipo cirugia especialidad ["+i+"]: "+this.cirugias.get("acronimoTipoCirugiaEspecialidad_"+i));
				logger.info("nombre tipo cirugia especialidad ["+i+"]: "+this.cirugias.get("nombreTipoCirugiaEspecialidad_"+i));
			}
			logger.info("**************************FIN MAPA ORDENACIÓN FINAL CIRUGÍAS************************");
			
					
		}
		
		
		
		
		//SE genera la llave de los codigosServiciosInsertados
		String codigosServiciosInsertados = "";
		for(int i=0;i<this.getNumCirugias();i++)
			codigosServiciosInsertados += this.cirugias.get("codigoServicio_"+i)+",";
		this.cirugias.put("codigosServiciosInsertados",codigosServiciosInsertados);
	}
	
	
	/**
	 * Método implementado para cargar los servicios de una orden
	 *
	 */
	public void cargarServiciosOrden() 
	{
		SolicitudesCx mundoSolCx = new SolicitudesCx();
		this.cirugias = mundoSolCx.cargarServiciosXSolicitudCx(con, this.numeroSolicitud,false);
		
	}
	
	/**
	 * Método implementado para cargar toda la información de la orden
	 * incluyendo la información de los asocios liquidacion
	 * 
	 * Nota * Se necesita tener cargado los siguientes atributos:
	 * 
	 * numeroSolicitud : String
	 * con : Connection
	 *
	 */
	public void cargarTodaInformacionOrdenConAsociosLiquidados()
	{
		this.cargarDetalleOrden();
		this.cargarServiciosOrden();
		
		for(int i=0;i<this.getNumCirugias();i++)
		{
			HashMap<String, Object> asociosLiquidados = liquidacionDao.consultarAsociosLiquidados(this.con, this.cirugias.get("codigo_"+i).toString());
			
			this.cirugias.put("numAsocios_"+i, asociosLiquidados.get("numRegistros"));
			double valorCirugia = 0;
			
			
			for(int j=0;j<Integer.parseInt(asociosLiquidados.get("numRegistros").toString());j++)
				for(int k=0;k<indicesAsociosLiquidados.length;k++)
				{
					this.cirugias.put(indicesAsociosLiquidados[k]+i+"_"+j, asociosLiquidados.get(indicesAsociosLiquidados[k]+j));
					
					if(k==13)
					{
						valorCirugia += Utilidades.convertirADouble(asociosLiquidados.get(indicesAsociosLiquidados[k]+j)+"",true );
					}
				}
			this.cirugias.put("valor_"+i, valorCirugia);
		}
		
		//Se cargan los materiales especiales
		this.materialesEspeciales = UtilidadesSalas.obtenerListadoMaterialesEspeciales(this.con, this.numeroSolicitud,"",false,false);
		
	}
	/**
	 * Método para obtener el mayor servicio de una solicitud
	 *
	 */
	private void obtenerMayorServicio(boolean porEspecialidad) 
	{
		double mayorGrupoUvr = 0;
		int posicion = ConstantesBD.codigoNuncaValido;
		
		//***********************************************************************************************************
		//*********************************OBTENER EL MAYOR SERVICIO*************************************************
		//***********************************************************************************************************
		//1) Se toma el servicio que tenga el mayor GRUPO/UVR dependiendo del tarifario oficial
		for(int i=0;i<this.getNumCirugias();i++)
		{
			//Solo se realiza para servicios que se van a liquidar
			if(UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
					!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())) //no puede ser de via de acceso
			{
				//Dependiendo del tarifario oficial se realiza la verificacion del mayor servicio
				if(tarifarioOficialGeneral==ConstantesBD.codigoTarifarioSoat)
				{
					if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatGrupo)
						if(Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr)
						{
							mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
							posicion = i;
						}
				}
				else if(tarifarioOficialGeneral==ConstantesBD.codigoTarifarioISS)
				{
					if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUvr)
						if(Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr)
						{
							mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
							posicion = i;
						}
				}
			}
		}
		
		//2) Si todavía no se ha encontrado una posición se verifica por Unidades/Valor
		if(posicion == ConstantesBD.codigoNuncaValido)
		{
			mayorGrupoUvr = 0;
			for(int i=0;i<this.getNumCirugias();i++)
			{
				//Solo se realiza para servicios que se van a liquidar
				if
				(
					!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&& //no puede ser de via de acceso
					UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
					(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUnidades||Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatValor)&&
					Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr
				)
				{
					mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
					posicion = i;
				}
			}
		}
		
		//3) Si todavía no se ha encontrado una posición se toma entonces el primer servicio que se vaya a liquidar
		// y no sea de vía de acceso
		if(posicion == ConstantesBD.codigoNuncaValido)
		{
			mayorGrupoUvr = 0;
			for(int i=0;i<this.getNumCirugias();i++)
			{
				if(UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
						!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())) 
					posicion = i;
			}
		}
		
		//4) Si todavía no se ha encontrado una posición se toma entonces el primero servicio que se vaya a liquidar y sea de vía de acceso 
		if(posicion == ConstantesBD.codigoNuncaValido)
		{
			mayorGrupoUvr = 0;
			for(int i=0;i<this.getNumCirugias();i++)
			{
				if(UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
					UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())) 
					posicion = i;
			}
		}
		
		//5) Si todavía no se ha encontrado una posición se toma entonces el primero servicio que no se vaya a liquidar y sea de vía de acceso 
		if(posicion == ConstantesBD.codigoNuncaValido)
		{
			mayorGrupoUvr = 0;
			for(int i=0;i<this.getNumCirugias();i++)
			{
				if(!UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
					UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())) 
					posicion = i;
			}
		}
		
		//6) Si todavía no se ha encontrado una posición se toma el primer servicio del listado sin importar si se va a liquidar o no
		if(posicion == ConstantesBD.codigoNuncaValido)
			posicion = 0;
		
		this.cirugias.put("numeroServicio_"+posicion, (this.getNumCirugiasOrdenadas()+1)); //se asigna consecutivo del servicio
		//***********************************************************************************************************
		//*****************************FIN OBTENER EL MAYOR SERVICIO*************************************************
		//***********************************************************************************************************
		
		//Obtención del tipo de cirugia tanto por ordenación de especialidad como por ordenación normal
		int codigoTipoCirugia = ConstantesBD.codigoNuncaValido;
		
		//Si la ordenación es por especialidad el mayor servicio siempre será principal principal o principal bilateral
		//Si la ordenación No es por especialidad el tipo de cirugía dependerá de los servicios que se lleven de la ordenación
		if(porEspecialidad)
		{
		
			if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion)+""))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral, usuario.getCodigoInstitucionInt());
				this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral);
			}
			else
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal, usuario.getCodigoInstitucionInt());
				this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal);
			}
			
			if(codigoTipoCirugia==ConstantesBD.codigoNuncaValido)
				this.agregarErrorApResource("error.salasCirugia.noTiene", "La institución", "no tiene parametrización para el tipo de cirugía "+this.cirugias.get("acronimoTipoCirugia_"+posicion)+". Favor comunicarse con el adminsitrador del sistema");
			else
				this.cirugias.put("nombreTipoCirugia_"+posicion,UtilidadesSalas.obtenerDescripcionTipoCirugia(con, codigoTipoCirugia));
			this.cirugias.put("codigoTipoCirugia_"+posicion,codigoTipoCirugia);
			
			logger.info("nombre del tipo de cirugia por ordenacion especialidad en posicion ["+posicion+"]: "+this.cirugias.get("nombreTipoCirugia_"+posicion));
		}
		else
		{
			String acronimoTipoCirugia = "";
			//Si hasta ahora no han habido servicios ordenados se considera como principal
			if(this.getNumCirugiasOrdenadas()==0)
			{
				if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion)+""))
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral;
				else
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal;
			}
			//Si ya se lleva un servicio ordenado entonces el proximo se considera como mayor
			else if(this.getNumCirugiasOrdenadas()==1)
			{
				if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion)+""))
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral;
				else
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional;
			}
			//Si ya habian 2 o mas servicios el próximo será adicional
			else if(this.getNumCirugiasOrdenadas()>=2)
			{
				if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion)+""))
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral;
				else
					acronimoTipoCirugia = ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional;
			}
			
			codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, acronimoTipoCirugia, usuario.getCodigoInstitucionInt());
			this.cirugias.put("acronimoTipoCirugia_"+posicion,acronimoTipoCirugia);
			
			if(codigoTipoCirugia==ConstantesBD.codigoNuncaValido)
				this.agregarErrorApResource("error.salasCirugia.noTiene", "La institución", "no tiene parametrización para el tipo de cirugía "+this.cirugias.get("acronimoTipoCirugia_"+posicion)+". Favor comunicarse con el adminsitrador del sistema");
			else
				this.cirugias.put("nombreTipoCirugia_"+posicion,UtilidadesSalas.obtenerDescripcionTipoCirugia(con, codigoTipoCirugia));
			this.cirugias.put("codigoTipoCirugia_"+posicion,codigoTipoCirugia);
			
			//logger.info("nombre del tipo de cirugia por ordenacion especialidad en posicion ["+posicion+"]: "+this.cirugias.get("nombreTipoCirugia_"+posicion));
		}
		
		//Se agrega el servicio mayor al nuevo mapa de cirugias ordenadas
		this.agregarServicioOrdenado(posicion);
	}
	
	/**
	 * Método usado para agrupar los servicios por especialidad
	 *
	 */
	private void agruparServiciosPorEspecialidad()
	{
		//Se toma el código de la especialidad que interviene del servicio anterior de la ordenación
		int codigoEspecialidad = Utilidades.convertirAEntero(this.cirugiasOrdenadas.get("codigoEspecialidadInterviene_"+(this.getNumCirugiasOrdenadas()-1))+"", true);
		int numEspecialidades = 0, contadorCirugias = 0;
		
		logger.info("codigo especialidad a validar=> "+codigoEspecialidad);
		//Se cuenta el número de servicios adicionales que tienen las misma especialidad
		for(int i=0;i<this.getNumCirugias();i++)
			if(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
			{
				logger.info("posicion encontrada: "+i);
				numEspecialidades++;
			}
		
		//Mientras que ya no hayan numero de especialidades se siguen validando
		while(numEspecialidades>0)
		{
			double mayorGrupoUvr = 0;
			int posicion = ConstantesBD.codigoNuncaValido;
			numEspecialidades = 0;
			contadorCirugias ++;
			
			//1) Se toma el servicio que tenga el mayor GRUPO/UVR dependiendo del tarifario oficial
			for(int i=0;i<this.getNumCirugias();i++)
			{
				//Solo se realiza para servicios que se van a liquidar y que tengan las misma especialidad del servicio principal
				if(UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
					Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad&&
					!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())) //no puede ser de via de acceso
				{
					//Dependiendo del tarifario oficial se realiza la verificacion del mayor servicio
					if(tarifarioOficialGeneral==ConstantesBD.codigoTarifarioSoat)
					{
						if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatGrupo)
							if(Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr)
							{
								mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
								posicion = i;
							}
					}
					else if(tarifarioOficialGeneral==ConstantesBD.codigoTarifarioISS)
					{
						if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUvr)
							if(Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr)
							{
								mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
								posicion = i;
							}
					}
				}
			}
			
			//2) Si todavía no se ha encontrado una posición se verifica por Unidades/Valor
			if(posicion == ConstantesBD.codigoNuncaValido)
			{
				mayorGrupoUvr = 0;
				for(int i=0;i<this.getNumCirugias();i++)
				{
					//Solo se realiza para servicios que se van a liquidar y que tengan la misma especialidad del servicio principal
					if
					(
						UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
						!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&& //no puede ser de vía de acceso
						(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUnidades||Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatValor)&&
						Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad&&
						Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+i).toString(),true)>mayorGrupoUvr
					)
					{
						mayorGrupoUvr = Double.parseDouble(this.cirugias.get("grupoUvr_"+i).toString());
						posicion = i;
					}
				}
			}
			
			//3) Si todavía no se ha encontrado una posición se toma entonces el primer servicio que se vaya a liquidar
			// que no sea de vía de acceso y que pertenezca a la misma especialidad
			if(posicion == ConstantesBD.codigoNuncaValido)
			{
				mayorGrupoUvr = 0;
				for(int i=0;i<this.getNumCirugias();i++)
				{
					if(
						UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
						!UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&& //no puede ser de vía de acceso
						Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
						posicion = i;
				}
			}
			
			//4) Si todavía no se ha encontrado una posición se toma entonces el primer servicio que se vaya a liquidar
			// que sea de vía de acceso y que pertenezca a la misma especialidad
			if(posicion == ConstantesBD.codigoNuncaValido)
			{
				mayorGrupoUvr = 0;
				for(int i=0;i<this.getNumCirugias();i++)
				{
					if(
						UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
						UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&& //no puede ser de vía de acceso
						Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
						posicion = i;
				}
			}
			
			//5) Si todavía no se ha encontrado una posición se toma entonces el primer servicio que no se vaya a liquidar
			// que sea de vía de acceso y que pertenezca a la misma especialidad
			if(posicion == ConstantesBD.codigoNuncaValido)
			{
				mayorGrupoUvr = 0;
				for(int i=0;i<this.getNumCirugias();i++)
				{
					if(
						!UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString())&&
						UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&& //no puede ser de vía de acceso
						Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
						posicion = i;
				}
			}
			
			//6) Si todavía no se ha encontrado una posición se toma el primer servicio de la especialidad del servicio principal
			if(posicion == ConstantesBD.codigoNuncaValido)
			{
				mayorGrupoUvr = 0;
				for(int i=0;i<this.getNumCirugias();i++)
				{
					if(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
						posicion = i;
				}
			}
			
			this.cirugias.put("numeroServicio_"+posicion, this.getNumCirugiasOrdenadas()+1); //es el servicio con el consecutivo siguiente
			
			
			//Obtención del tipo de cirugia
			int codigoTipoCirugia = ConstantesBD.codigoNuncaValido;
			if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion)+""))
			{
				//Si es la primera cirugía es la mayor adicional
				if(contadorCirugias==1)
				{
					codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral, usuario.getCodigoInstitucionInt());
					this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral);
				}
				//de lo contrario es la adicional adicional
				else
				{
					codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral, usuario.getCodigoInstitucionInt());
					this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral);
				}
			}
			else
			{
				//Si es la primera cirugía es la mayor adicional
				if(contadorCirugias==1)
				{
					codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional, usuario.getCodigoInstitucionInt());
					this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional);
				}
				//de lo contrario es la adicional adicional
				else
				{
					codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional, usuario.getCodigoInstitucionInt());
					this.cirugias.put("acronimoTipoCirugia_"+posicion,ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional);
				}
			}
			
			if(codigoTipoCirugia==ConstantesBD.codigoNuncaValido)
				this.agregarErrorApResource("error.salasCirugia.noTiene", "La institución", "no tiene parametrización para el tipo de cirugía "+this.cirugias.get("acronimoTipoCirugia_"+posicion)+". Favor comunicarse con el adminsitrador del sistema");
			else
				this.cirugias.put("nombreTipoCirugia_"+posicion,UtilidadesSalas.obtenerDescripcionTipoCirugia(con, codigoTipoCirugia));
			this.cirugias.put("codigoTipoCirugia_"+posicion,codigoTipoCirugia);
			
			//Se agrega el servicio mayor al nuevo mapa de cirugias ordenadas
			logger.info("posicion a eliminar=> "+posicion);
			this.agregarServicioOrdenado(posicion);
			
			//Se cuenta de nuevo el número de servicios adicionales que tienen las misma especialidad
			for(int i=0;i<this.getNumCirugias();i++)
				if(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)==codigoEspecialidad)
					numEspecialidades++;
			logger.info("nuevo número de especialidades=> "+numEspecialidades);
		} // Fin While 
		
	}
	/**
	 * Método que carga el tarifario oficial dle esquema tarifariom definido
	 * @param con
	 * @param esquemaTarifario
	 * @return
	 */
	private int cargarTarifarioOficial(Connection con, int esquemaTarifario) throws IPSException 
	{
		int tarifarioOficial = ConstantesBD.codigoTarifarioCups;
		EsquemaTarifario esquema = new EsquemaTarifario();
		esquema.setCodigo(esquemaTarifario);
		try
		{
			
			
			esquema.cargar(con,usuario.getCodigoInstitucionInt());
			tarifarioOficial = esquema.getTarifarioOficial().getCodigo();
			
		}
		catch(SQLException e)
		{
			logger.error("Error cargando tarifario oficial en prepararDatosCirugiasSolicitud de LiquidacionQxAction: "+e);
			tarifarioOficial = ConstantesBD.codigoTarifarioCups;
		}
		return tarifarioOficial;
	}
	
	/**
	 * Método usado para agregar al vectro de mensajes error un error de tipo application.resource
	 * @param llave
	 * @param atributo1
	 * @param atributo2
	 */
	private void agregarErrorApResource(String llave,String atributo1,String atributo2)
	{
		ElementoApResource elemento = new ElementoApResource(llave);
		if(!atributo1.equals(""))
			elemento.agregarAtributo(atributo1);
		if(!atributo2.equals(""))
			elemento.agregarAtributo(atributo2);
		this.mensajesError.add(elemento);
	}
	
	/**
	 * Método usado para agregar al vectro de mensajes error un error de tipo application.resource
	 * @param llave
	 * @param atributo1
	 * @param atributo2
	 */
	private void agregarErrorApResourceXCirugia(String llave,String atributo1,String atributo2)
	{
		if(!this.tieneTituloMensajeCirugia)
		{
			this.tieneTituloMensajeCirugia = true;
			this.agregarErrorApResource("prompt.generico", this.tituloMensajeCirugia01+"@","");
			this.agregarErrorApResource("prompt.generico", this.tituloMensajeCirugia02+"@","");
		}
		
		this.agregarErrorApResource(llave, atributo1, atributo2);
	}
	
	/**
	 * Método para agregar un servicio ordenado de la estructura de cirugias a la nueva
	 * @param posicion
	 */
	private void agregarServicioOrdenado(int posicion)
	{
		//posicion actual del mapa ordenado
		int posOrd = Utilidades.convertirAEntero(this.cirugiasOrdenadas.get("numRegistros")+"", true);
		this.cirugiasOrdenadas.put("numRegistros",posOrd);
		
		//Se agrega el registro del mapa cirugias elegido al final del mapa de cirugiasOrdenadas
		this.cirugiasOrdenadas = Listado.copyMapOnIndexMap(cirugias, cirugiasOrdenadas, indicesCirugias, posOrd, posicion);
		
		//Se copian los datos de los profesionales de la cirugía
		for(int i=0;i<Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true);i++)
		{
			for(int j=0;j<indicesProfesionalesCirugia.length;j++)
				this.cirugiasOrdenadas.put(indicesProfesionalesCirugia[j]+posOrd+"_"+i, this.cirugias.get(indicesProfesionalesCirugia[j]+posicion+"_"+i));
		}
		
		//Se elimina servicio del mapa de cirugías
		for(int i = posicion ; i< (this.getNumCirugias()-1);i++)
		{
			//luego se eliminan sus campos principales
			for(int j=0;j<indicesCirugias.length;j++)
				this.cirugias.put(indicesCirugias[j]+i, this.cirugias.get(indicesCirugias[j]+(i+1)));
			
			//primero se elimina su detalle de profesionales
			for(int j=0;j<Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+i)+"", true);j++)
				for(int k=0;k<indicesProfesionalesCirugia.length;k++)
					this.cirugias.put(indicesProfesionalesCirugia[k]+i+"_"+j,this.cirugias.get(indicesProfesionalesCirugia[k]+(i+1)+"_"+j));
			
		}
		
		for(int i=0;i<indicesCirugias.length;i++)
			this.cirugias.remove(indicesCirugias[i]+(this.getNumCirugias()-1));
		
		this.cirugias.put("numRegistros",(this.getNumCirugias()-1));
		
		//Se calcula el cirujano principal de la orden
		if(this.getNumCirugiasOrdenadas()==1)
			for(int j=0;j<Utilidades.convertirAEntero(this.cirugiasOrdenadas.get("numProfesionales_0")+"", true);j++)
				if(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()).equals(this.cirugiasOrdenadas.get("consecutivoAsocio_0_"+j).toString()))
				{
					this.codigoCirujano = Utilidades.convertirAEntero(this.cirugiasOrdenadas.get("codigoProfesional_0_"+j)+"", true);
					this.nombreCirujano = this.cirugiasOrdenadas.get("nombreProfesional_0_"+j).toString();
				}
			
		
	}
	
	/**
	 * Método que consulta los profesionales de cada cirugía cirugía
	 */
	public void consultarProfesionalesXCirugia(int posicion)
	{
		//Se realiza la validación del tipo de especialista de acuerdo a la especialidad interviene del primer servicio
		for(int i=0;i<this.getNumCirugias();i++)
		{
			if(posicion==ConstantesBD.codigoNuncaValido||posicion==i)
			{
			
				HashMap<String, Object> profesionales = liquidacionDao.consultarProfesionalesCirugia(con, this.cirugias.get("codigo_"+i).toString());
				
				for(int j=0;j<Utilidades.convertirAEntero(profesionales.get("numRegistros")+"", true);j++)
				{
					///Se agrega datos de los profesionales de la cirugia al mapa de cirugías
					for(int k=0;k<indicesProfesionalesCirugia.length;k++)
						this.cirugias.put(indicesProfesionalesCirugia[k]+i+"_"+j, profesionales.get(indicesProfesionalesCirugia[k]+j));
				}
				
				this.cirugias.put("numProfesionales_"+i,profesionales.get("numRegistros"));
				//Se redefine los tipos de especialistas
				redefinirTipoEspecialista(i);
			}
		}
	}
	
	/**
	 * Método implementado para redefinir el tipo de cirugía de un servicio específico 
	 * @param posicion
	 */
	public void redefinirTipoCirugia(int posicion, boolean esEspecialidad)
	{
		String acronimoTipoCx = esEspecialidad?this.cirugias.get("acronimoTipoCirugiaEspecialidad_"+posicion).toString():this.cirugias.get("acronimoTipoCirugia_"+posicion).toString();
		String nuevoAcronimoTipoCx = "";
		int codigoTipoCirugia = ConstantesBD.codigoNuncaValido;
		
		//***************VERIFICACION DEL INDICATIVO BILATERAL*****************************************************************
		if(UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+posicion).toString()))
		{
			if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral;
			}
			else if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral;
			}
			else if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral;
			}
		}
		else
		{
			if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaPrincipalBilateral))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaPrincipalPrincipal;
			}
			else if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaMayorBilateral))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional;
			}
			else if(acronimoTipoCx.equals(ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral))
			{
				codigoTipoCirugia = UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional, usuario.getCodigoInstitucionInt());
				nuevoAcronimoTipoCx = ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional;
			}
		}
		//***************************************************************************************
		
		if(codigoTipoCirugia!=ConstantesBD.codigoNuncaValido)
		{
			if(esEspecialidad)
			{
				this.cirugias.put("codigoTipoCirugiaEspecialidad_"+posicion, codigoTipoCirugia);
				this.cirugias.put("acronimoTipoCirugiaEspecialidad_"+posicion, nuevoAcronimoTipoCx);
				this.cirugias.put("nombreTipoCirugiaEspecialidad_"+posicion, UtilidadesSalas.obtenerDescripcionTipoCirugia(con, codigoTipoCirugia));
			}
			else
			{
				this.cirugias.put("codigoTipoCirugia_"+posicion, codigoTipoCirugia);
				this.cirugias.put("acronimoTipoCirugia_"+posicion, nuevoAcronimoTipoCx);
				this.cirugias.put("nombreTipoCirugia_"+posicion, UtilidadesSalas.obtenerDescripcionTipoCirugia(con, codigoTipoCirugia));
			}
			
		}
	}
	
	/**
	 * Método implementado para redefinir el tipo de especialista de una cirugía específica
	 * @param posicion
	 */
	public void redefinirTipoEspecialista(int posicion)
	{
		//Se toma la especialidad que interviene
		int codigoEspecialidadInterviene = Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_0").toString());
		
		for(int i=0;i<Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true);i++)
		{
			//REDIFINICION DEL TIPO DE ESPECIALISTA
			//Si el servicio tiene la misma especialidad del servicio N° 1 y el honorario es diferente de ayudantía entonces el tipo especialiste es IGUAL
			//También aplica si solo se tiene una cirugía
			if((codigoEspecialidadInterviene==Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+posicion)+"", true)&&
				!ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt()).equals(this.cirugias.get("consecutivoAsocio_"+posicion+"_"+i).toString()))
					||this.getNumCirugias()==1)
				this.cirugias.put("tipoEspecialista_"+posicion+"_"+i, ConstantesIntegridadDominio.acronimoIgual);
			//MT 5727 Se elimina validación segun cambio en el documento DCU 532
			
			else
				this.cirugias.put("tipoEspecialista_"+posicion+"_"+i, ConstantesIntegridadDominio.acronimoIgual);
			     //Si el profesional es ayudante y tienen la misma especialidad de la especialidad que interviene del servicio el tipo especialista es IGUAL
			
//				if(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+posicion)+"", true)==Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidad_"+posicion+"_"+i)+"", true)&&
//				ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt()).equals(this.cirugias.get("consecutivoAsocio_"+posicion+"_"+i).toString()))
//				this.cirugias.put("tipoEspecialista_"+posicion+"_"+i, ConstantesIntegridadDominio.acronimoIgual);
//			//De lo contrario es diferente
//			
//			else
//				this.cirugias.put("tipoEspecialista_"+posicion+"_"+i, ConstantesIntegridadDominio.acronimoDiferente);
			
			//CONSULTA DEL ASOCIO DE CIRUJANO
			if(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()).equals(this.cirugias.get("consecutivoAsocio_"+posicion+"_"+i).toString()))
			{
				this.cirugias.put("esAsocioCirujano_"+posicion+"_"+i, ConstantesBD.acronimoSi);
				
				if(posicion==0)
				{
					this.codigoCirujano = Utilidades.convertirAEntero(this.cirugias.get("codigoProfesional_0_"+i)+"", true);
					this.nombreCirujano = this.cirugias.get("nombreProfesional_0_"+i).toString();
				}
			}
			else
				this.cirugias.put("esAsocioCirujano_"+posicion+"_"+i, ConstantesBD.acronimoNo);
				
		}
	}
	
	
	/**
	 * Método implementado para insertar el detalle de un servicio de una orden
	 * @param posicion
	 * @return
	 */
	public boolean insertarDatosServicio(int posicion)
	{
		boolean exito = false;
		boolean esNuevo = false;
		
		SolicitudesCx mundoSolCx = new SolicitudesCx();
		
		
		
		//Se redefine el tipo de especialista de la cirugía
		this.redefinirTipoEspecialista(posicion);
		
		//Si el servicio no tiene consecutivo quiere decir que se debe insertar desde el principio
		if(!UtilidadCadena.noEsVacio(this.cirugias.get("codigo_"+posicion)+""))
		{
			esNuevo = true; //paera indicar que el servicio es nuevo
			String consecServicioExistente = ""; //Consecutivo del servicio existente
			
			//Se obtiene el servicio existente que tenga la misma especialidad del nuevo servicio para copiarle sus datos de historia clinica
			for(int i=0;i<this.getNumCirugias();i++)
				if(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+posicion)+"", true)==Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidadInterviene_"+i)+"", true)&&
					i!=posicion&&
					UtilidadCadena.noEsVacio(this.cirugias.get("codigo_"+i)+""))
					consecServicioExistente = this.cirugias.get("codigo_"+i).toString();
			
			//Se cargan los diagnósticos por servicio
			HashMap diagnosticos = HojaQuirurgica.cargarDiagnosticosPorServicio(con, Integer.parseInt(consecServicioExistente), 0);
			
			
			//Se cargan las descripciones quirurgicas
			HashMap descripcionesQx = HojaQuirurgica.consultarDescripcionesQx2(con, consecServicioExistente);
			
			//Se inserta el nuevo servicio
			this.cirugias.put("codigo_"+posicion,mundoSolCx.insertarSolicitudCxXServicioTransaccional(
				con, 
				this.numeroSolicitud, 
				this.cirugias.get("codigoServicio_"+posicion).toString(), 
				Integer.parseInt(this.cirugias.get("codigoTipoCirugia_"+posicion).toString()), 
				Utilidades.convertirAEntero(this.cirugias.get("numeroServicio_"+posicion).toString()),
				Integer.parseInt(this.cirugias.get("codigoEsquemaTarifario_"+posicion).toString()),
				Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+posicion).toString(),true),
				usuario.getCodigoInstitucionInt(),
				/*this.cirugias.get("autorizacion_"+posicion).toString(),*/
				Integer.parseInt(this.cirugias.get("codigoFinalidadCx_"+posicion).toString()),
				this.cirugias.get("observaciones_"+posicion).toString(),
				this.cirugias.get("codigoViaCx_"+posicion).toString(),
				this.cirugias.get("indBilateral_"+posicion).toString(),
				this.cirugias.get("indViaAcceso_"+posicion).toString(),
				Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+posicion).toString()),
				this.cirugias.get("liquidarServicio_"+posicion).toString(),
				ConstantesBD.continuarTransaccion,"",null));
			
			if(Utilidades.convertirAEntero(this.cirugias.get("codigo_"+posicion)+"", true)>0)
			{
			
				//Se insertan los diagnosticos
				for(int i=0;i<Utilidades.convertirAEntero(diagnosticos.get("numRegistros")+"", true);i++)
				{
					if(HojaQuirurgica.insertarDiagnosticoPostOperatorio(
						con, 
						this.cirugias.get("codigo_"+posicion).toString(), 
						diagnosticos.get("diagnostico_"+i).toString(), 
						Integer.parseInt(diagnosticos.get("tipoCie_"+i).toString()), 
						UtilidadTexto.getBoolean(diagnosticos.get("principal_"+i).toString()), 
						UtilidadTexto.getBoolean(diagnosticos.get("complicacion_"+i).toString()),
						this.usuario.getLoginUsuario())<=0)
					{
						i = Utilidades.convertirAEntero(diagnosticos.get("numRegistros")+"");
						this.agregarErrorApResource("errors.noSeGraboInformacion", "DE LOS DIAGNÓSTICOS PARA "+this.cirugias.get("descripcionServicio_"+posicion), "");
						
					}
				}
				
				
				//Se insertan las descripciones
				for(int i=0;i<Utilidades.convertirAEntero(descripcionesQx.get("numRegistros").toString(), true);i++)
				{
					if(HojaQuirurgica.insertarDescripcionQx(con, this.cirugias.get("codigo_"+posicion).toString(), descripcionesQx.get("descripcion_"+i).toString(), usuario.getLoginUsuario())<=0)
					{
						i = Utilidades.convertirAEntero(descripcionesQx.get("numRegistros").toString(), true);
						this.agregarErrorApResource("errors.noSeGraboInformacion", "DE LA DESCRIPCIÓN QX PARA "+this.cirugias.get("descripcionServicio_"+posicion), "");
					}
				}
			}
			else
				this.agregarErrorApResource("errors.noSeGraboInformacion", "DEL SERVICIO "+this.cirugias.get("descripcionServicio_"+posicion), "");
			
				
		}
		//De lo contrario el servicio existe por lo tanto se modifica su informa
		else
		{
			if(mundoSolCx.modificarServiciosXSolicitudCx(con, 
				Integer.parseInt(this.cirugias.get("codigoServicio_"+posicion).toString()), 
				Integer.parseInt(this.cirugias.get("codigoTipoCirugia_"+posicion).toString()), 
				Utilidades.convertirAEntero(this.cirugias.get("numeroServicio_"+posicion).toString()), 
				Utilidades.convertirAEntero(this.cirugias.get("codigoEsquemaTarifario_"+posicion).toString()), 
				Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+posicion).toString(),true), 
				/*this.cirugias.get("autorizacion_"+posicion).toString(),*/ 
				Integer.parseInt(this.cirugias.get("codigoFinalidadCx_"+posicion).toString()), 
				this.cirugias.get("observaciones_"+posicion).toString(), 
				this.cirugias.get("codigoViaCx_"+posicion).toString(), 
				this.cirugias.get("indBilateral_"+posicion).toString(), 
				this.cirugias.get("indViaAcceso_"+posicion).toString(), 
				Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+posicion).toString()), 
				this.cirugias.get("liquidarServicio_"+posicion).toString(), 
				this.cirugias.get("codigo_"+posicion).toString(), 
				ConstantesBD.continuarTransaccion)<=0)
				this.agregarErrorApResource("errors.noSeGraboInformacion", "DEL SERVICIO "+this.cirugias.get("descripcionServicio_"+posicion), "");
		}
		
		
		if(Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true)>0)
		{
			for(int i=0;i<Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true);i++)
			{
				int resp = ConstantesBD.codigoNuncaValido;
				
				//Se actualiza la información de los profesionales
				DtoProfesionalesCirugia dtoProfesional = new DtoProfesionalesCirugia();
				dtoProfesional.setCodigo(this.cirugias.get("consecutivo_"+posicion+"_"+i).toString().trim());
				dtoProfesional.setCodigoAsocio(Utilidades.convertirAEntero(this.cirugias.get("consecutivoAsocio_"+posicion+"_"+i).toString()));
				dtoProfesional.setCodSolCxServ(this.cirugias.get("codigo_"+posicion).toString());
				dtoProfesional.setCodigoProfesional(Utilidades.convertirAEntero(this.cirugias.get("codigoProfesional_"+posicion+"_"+i).toString()));
				dtoProfesional.setCodigoEspecialidad(Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidad_"+posicion+"_"+i).toString()));
				dtoProfesional.setCobrable(this.cirugias.get("cobrable_"+posicion+"_"+i).toString());
				dtoProfesional.setCodigoPool(Utilidades.convertirAEntero(this.cirugias.get("pool_"+posicion+"_"+i).toString(),true));
				dtoProfesional.setTipoEspecialista(this.cirugias.get("tipoEspecialista_"+posicion+"_"+i).toString());
				dtoProfesional.setExisteBD(UtilidadTexto.getBoolean(this.cirugias.get("existeBd_"+posicion+"_"+i).toString()));
				dtoProfesional.setEliminar(UtilidadTexto.getBoolean(this.cirugias.get("eliminar_"+posicion+"_"+i).toString()));
				dtoProfesional.setHistoriaClinica(this.cirugias.get("historiaClinica_"+posicion+"_"+i).toString());
				
				
				//Si no existe registro de profesional, se inserta
				if(!dtoProfesional.isExisteBD())
				{
					//Sin embargo hay que verificar que no se haya eliminado
					if(!dtoProfesional.isEliminar())
					{
						resp = HojaQuirurgica.insertarProfesionalCirugia(con, dtoProfesional,this.usuario.getLoginUsuario());
						
						
						if(resp>0)
						{
							//Se consultan las descripciones del profesional y especialidad
							this.cirugias.put("nombreAsocio_"+posicion+"_"+i, Utilidades.obtenerNombreTipoAsocio(con, dtoProfesional.getCodigoAsocio())[0]);
							this.cirugias.put("nombreProfesional_"+posicion+"_"+i, Persona.obtenerApellidosNombresPersona(con, dtoProfesional.getCodigoProfesional()));
							this.cirugias.put("nombreEspecialidad_"+posicion+"_"+i, Utilidades.getNombreEspecialidad(con, dtoProfesional.getCodigoEspecialidad()));
							this.cirugias.put("consecutivo_"+posicion+"_"+i,resp);
							this.cirugias.put("existeBd_"+posicion+"_"+i,ConstantesBD.acronimoSi);
							
						}
					}
					else
						resp = 1;
				}
				//Si se debía eliminar se elimina 
				else if(dtoProfesional.isEliminar())
				{
					resp = HojaQuirurgica.eliminarProfesionalesCx(this.con, dtoProfesional.getCodSolCxServ(), dtoProfesional.getCodigo())?1:0;
					
					if(resp>0)
						this.cirugias.put("existeBd_"+posicion+"_"+i,ConstantesBD.acronimoNo); //Se marca como si ya no existe en la base de datos
				}
				//De lo contrario se modifica el profesional
				else
					resp = HojaQuirurgica.modificarProfesionalCirugia(con, dtoProfesional,this.usuario.getLoginUsuario());
				
				if(resp<=0)
				{
					this.agregarErrorApResource("errors.noSeGraboInformacion", "DE LOS HONORARIOS PARA "+this.cirugias.get("descripcionServicio_"+posicion), "");
					i = Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true); //se rompe ciclo
				}
			}
		}
		else
			this.agregarErrorApResource("error.salasCirugia.noTiene", "el servicio "+this.cirugias.get("descripcionServicio_"+posicion), "información de honorarios. Por favor verifique");
		
		
		if(this.mensajesError.size()>0)
		{
			exito = false;
			if(esNuevo)
			{
				this.cirugias.put("codigo_"+posicion, ""); //si el servicio es nuevo vuelve y se retorna a vacío su consecutivo
				for(int i=0;i<Utilidades.convertirAEntero(this.cirugias.get("numProfesionales_"+posicion)+"", true);i++)
					this.cirugias.put("consecutivo_"+posicion+"_"+i, ""); //lo mismo para los honorarios
			}
		}
		else
			exito = true;
		
		
		return exito;
	}
	
	/**
	 * Método implementado para actualizar los datos de la orden
	 * @return
	 */
	public boolean actualizarDatosOrden()
	{
		boolean exito = false;
		this.logOriginal = "";
		this.logModificado = "";
		
		//1) ACTUALIZACIÓN DEL NÚMERO DE AUTORIZACION
		/*if(!this.encabezadoOrden.get("autorizacion").toString().trim().equals(this.encabezadoOrden.get("autorizacionAntigua").toString().trim()))
		{
			//Se llena la información del log
			logOriginal = "\n*  Número Autorizacion ["+this.encabezadoOrden.get("autorizacionAntigua")+"]";
			logModificado = "\n*  Número Autorizacion ["+this.encabezadoOrden.get("autorizacion")+"]";
			
			Solicitud mundoSolicitud = new Solicitud();
			if(!mundoSolicitud.actualizarNumeroAutorizacionTransaccional(con, this.encabezadoOrden.get("autorizacion").toString(), Integer.parseInt(numeroSolicitud), ConstantesBD.continuarTransaccion).isTrue())
				this.agregarErrorApResource("errors.noSeGraboInformacion", "DEL NÚMERO DE AUTORIZACIÓN", "");
		}
		*/
		
		//2) ACTUALIZACIÓN DE LOS DATOS DEL ACTO QUIRÚRGICO
		//Se verifica si los datos del acto quirurgico fueron modificados para editar el log
		fueModificadoDatosActoQuirurgico();
		
		int resp = SolicitudesCx.actualizarEncabezadoSolicitudCx(
			con, 
			this.datosActoQx.get("fechaInicialCx").toString(), 
			this.datosActoQx.get("horaInicialCx").toString(), 
			this.datosActoQx.get("fechaFinalCx").toString(), 
			this.datosActoQx.get("horaFinalCx").toString(), 
			this.datosActoQx.get("fechaIngresoSala").toString(), 
			this.datosActoQx.get("horaIngresoSala").toString(), 
			this.datosActoQx.get("fechaSalidaSala").toString(), 
			this.datosActoQx.get("horaSalidaSala").toString(), 
			this.datosActoQx.get("cobrarAnestesia").toString(), 
			this.numeroSolicitud);
		
		if(resp<=0)
			this.agregarErrorApResource("errors.noSeGraboInformacion", "DEL ACTO QUIRÚRGICO", "");
		
		resp = HojaQuirurgica.modificarTransaccional(con, 
			Integer.parseInt(this.numeroSolicitud), 
			"", // Duracion (No Aplica) 
			Integer.parseInt(this.datosActoQx.get("codigoSala").toString()), 
			Integer.parseInt(this.datosActoQx.get("codigoTipoSala").toString()), 
			this.datosActoQx.get("politraumatismo").toString(), 
			ConstantesBD.codigoNuncaValido, //Tipo Herida (No Aplica) 
			"", //finalizada (No Aplica) 
			"", //usuario Finaliza (No Aplica) 
			"", //fecha Fianliza (No Aplica) 
			"", //hora finaliza (No Aplica)
			"", //datos medico (No Aplica) 
			ConstantesBD.continuarTransaccion);
		
		if(resp<=0)
			this.agregarErrorApResource("errors.noSeGraboInformacion", "DEL ACTO QUIRÚRGICO", "");
		
		//3) ACTUALIZACIÓN DE OTROS PROFESIONALES
		//Se verifica si los datos de otros profesionales fueron modificados para editar el log
		fueModificadoOtrosProfesionales();
		
		
		//Utilidades.imprimirMapa(this.otrosProfesionales);
		for(int i=0;i<this.getNumOtrosProfesionales();i++)
		{
			if(!UtilidadTexto.getBoolean(this.otrosProfesionales.get("eliminar_"+i).toString()))
			{
				//Si el otro profesional existe en la base de datos se debe modificar
				if(UtilidadTexto.getBoolean(this.otrosProfesionales.get("existeBd_"+i).toString()))
				{
					logger.info("voy a actualizar=> "+i);
					//------------------MODIFICAR OTRO PROFESIONAL-------------------------------------
					resp = HojaQuirurgica.actualizarProfesionalActoQx(con, 
							Integer.parseInt(this.otrosProfesionales.get("codigoAsocio_"+i).toString()), 
							Integer.parseInt(this.otrosProfesionales.get("codigoProfesional_"+i).toString()), 
							this.otrosProfesionales.get("cobrable_"+i).toString(), 
							usuario.getLoginUsuario(), 
							this.otrosProfesionales.get("consecutivo_"+i).toString());
					//----------------------------------------------------------------------------------
				}
				else
				{
					logger.info("voy a insertar=> "+i);
					//-----------------INGRESAR OTRO PROFESIONAL----------------------------------------
					resp = HojaQuirurgica.insertarProfInfoQx(
						con, 
						this.numeroSolicitud, 
						Integer.parseInt(this.otrosProfesionales.get("codigoAsocio_"+i).toString()), 
						Integer.parseInt(this.otrosProfesionales.get("codigoProfesional_"+i).toString()), 
						this.otrosProfesionales.get("cobrable_"+i).toString(), 
						this.usuario.getLoginUsuario(), 
						this.otrosProfesionales.get("historiaClinica_"+i).toString());
					//----------------------------------------------------------------------------------
				}
			}
			//Si el registro se debe eliminar y existe en la base de datos se debe eliminar
			else if(UtilidadTexto.getBoolean(this.otrosProfesionales.get("existeBd_"+i).toString()))
			{
				logger.info("voy a eliminar=> "+i);
				//------------ELIMINAR OTRO PROFESIONAL----------------------------------------------
				resp = HojaQuirurgica.eliminarProfInfoQx(con, this.otrosProfesionales.get("consecutivo_"+i).toString())?1:0;
				//-----------------------------------------------------------------------------------
			}
			
			if(resp<=0)
			{
				this.agregarErrorApResource("errors.noSeGraboInformacion", "DE LOS PROFESIONALES DEL ACTO QUIRURGICO", "");
				resp = this.getNumOtrosProfesionales();
			}
				
		}
		
		//4) SE ACTUALIZA CADA CIRUGÍA CON SU DETALLE
		fueronModificadasCirugia(ConstantesBD.codigoNuncaValido);
		
		for(int i=0;i<this.getNumCirugias();i++)
			this.insertarDatosServicio(i);
		
		if(this.mensajesError.size()>0)
			exito = false;
		else
		{
			exito = true;
			//Se realiza el llamado al log de modificacion
			this.generarLogModificacion();
			
		}
		
		return exito;
	}
	
	/**
	 * Método implementado para generar el log de modificación
	 *
	 */
	public void generarLogModificacion() 
	{
		//SE verifica si se debe generar log
		if(!this.logOriginal.equals("")&&!this.logModificado.equals(""))
		{
			//****************GENERACIÓN DE LOG MODIFICACION*********************************************************************
			int consecutivoOrden = Utilidades.getConsecutivoOrdenesMedicas(this.con, Integer.parseInt(this.numeroSolicitud));
			this.logOriginal = "\n            ====INFORMACION ORIGINAL DE LA ORDEN "+consecutivoOrden+" ===== " + this.logOriginal+"\n";
			this.logModificado = "\n            ====INFORMACION DESPUES DE LA MODIFICACIÓN DE LA ORDEN "+consecutivoOrden+" ===== " + this.logModificado+"\n\n";
			this.logOriginal += this.logModificado;
			LogsAxioma.enviarLog(ConstantesBD.logLiquidacionServiciosCodigo, this.logOriginal, ConstantesBD.tipoRegistroLogModificacion, this.usuario.getLoginUsuario());
			//**********************************************************************************************************************
			
		}
		
	}
	/**
	 * Método que verifica si fueron modificadas las cirugías
	 *
	 */
	public void fueronModificadasCirugia(int posicion) 
	{
		String logOriginal = "", logModificado = "", logOriginalProfesionales = "", logModificadoProfesionales = "";
		
		SolicitudesCx mundoSolCx = new SolicitudesCx();
		//SE carga el mapa de los servicios como estaban anteriormente
		HashMap<String, Object> cirugiasAnteriores =  mundoSolCx.cargarServiciosXSolicitudCx(con, this.numeroSolicitud,false);
		HashMap<String, Object> profesionalesAnteriores = new HashMap<String, Object>(); 
		
		for(int i=0;i<Integer.parseInt(cirugiasAnteriores.get("numRegistros").toString());i++)
		{
			logOriginal = "";
			logModificado = "";
			for(int j=0;j<this.getNumCirugias();j++)
			{
				//Se verifica que la cirugia anterior se encuentre en el mapa de las cirugías a modificar
				if((posicion==j||posicion==ConstantesBD.codigoNuncaValido)&&cirugiasAnteriores.get("codigo_"+i).toString().equals(this.cirugias.get("codigo_"+j).toString()))
				{
					//*********************VALIDACION DE LOS DATOS ESPECÍFICOS DEL SERVICIO**********************************
					//1) Se verifica si se modificó el servicio
					if(!cirugiasAnteriores.get("codigoServicio_"+i).toString().equals(this.cirugias.get("codigoServicio_"+j).toString()))
					{
						logOriginal += "\n*  Servicio ["+cirugiasAnteriores.get("descripcionServicio_"+i)+"] ";
						logModificado += "\n*  Servicio ["+this.cirugias.get("descripcionServicio_"+j)+"]";
					}
					//2) Se verifica si se modifico el tipo de cirugia
					if(!cirugiasAnteriores.get("codigoTipoCirugia_"+i).toString().equals(this.cirugias.get("codigoTipoCirugia_"+j).toString()))
					{
						logOriginal += "\n*  Tipo cirugía ["+cirugiasAnteriores.get("nombreTipoCirugia_"+i)+"] ";
						logModificado += "\n*  Tipo cirugía ["+this.cirugias.get("nombreTipoCirugia_"+j)+"]";
					}
					//3) Se verifica si se modificó el número del servicio
					if(!cirugiasAnteriores.get("numeroServicio_"+i).toString().equals(this.cirugias.get("numeroServicio_"+j).toString()))
					{
						logOriginal += "\n*  Consecutivo ["+cirugiasAnteriores.get("numeroServicio_"+i)+"] ";
						logModificado += "\n*  Consecutivo ["+this.cirugias.get("numeroServicio_"+j)+"]";
					}
					//4) Se verirfica si se modificó el esquema tarifario
					if(!cirugiasAnteriores.get("codigoEsquemaTarifario_"+i).toString().equals(this.cirugias.get("codigoEsquemaTarifario_"+j).toString()))
					{
						logOriginal += "\n*  Esquema tarifario ["+cirugiasAnteriores.get("nombreEsquemaTarifario_"+i)+"] ";
						logModificado += "\n*  Esquema tarifario ["+Utilidades.getNombreEsquemaTarifario(this.con, Utilidades.convertirAEntero(this.cirugias.get("codigoEsquemaTarifario_"+j).toString(),true))+"]";
					}
					//5) Se verifica si se modificó grupo o uvr
					if(Utilidades.convertirADouble(cirugiasAnteriores.get("grupoUvr_"+i).toString(),true)!=Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+j).toString(),true))
					{
						logOriginal += "\n*  Grupo, uvr o tarifa ["+cirugiasAnteriores.get("grupoUvr_"+i)+"] ";
						logModificado += "\n*  Grupo, uvr o tarifa ["+this.cirugias.get("grupoUvr_"+j)+"]";
					}
					
					//6) Se verifica si se modificó autorizacion
					/*
					if(!cirugiasAnteriores.get("autorizacion_"+i).toString().equals(this.cirugias.get("autorizacion_"+j).toString()))
					{
						logOriginal += "\n*  Autorización ["+cirugiasAnteriores.get("autorizacion_"+i)+"] ";
						logModificado += "\n*  Autorización ["+this.cirugias.get("autorizacion_"+j)+"]";
					}
					*/
					
					//7) Se verifica si se modifico vía
					if(!cirugiasAnteriores.get("codigoViaCx_"+i).toString().equals(this.cirugias.get("codigoViaCx_"+j).toString()))
					{
						logOriginal += "\n*  Vía ["+ValoresPorDefecto.getIntegridadDominio(cirugiasAnteriores.get("codigoViaCx_"+i).toString())+"] ";
						logModificado += "\n*  Vía ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("codigoViaCx_"+j).toString())+"]";
					}
					//8) Se verifica si se modificó el indicativo bilateral
					if(!cirugiasAnteriores.get("indBilateral_"+i).toString().equals(this.cirugias.get("indBilateral_"+j).toString()))
					{
						logOriginal += "\n*  Ind. bilateral ["+ValoresPorDefecto.getIntegridadDominio(cirugiasAnteriores.get("indBilateral_"+i).toString())+"] ";
						logModificado += "\n*  Ind. bilateral ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("indBilateral_"+j).toString())+"]";
					}
					//9) Se verifica si se modificó el indicativo de vía de acceso
					if(!cirugiasAnteriores.get("indViaAcceso_"+i).toString().equals(this.cirugias.get("indViaAcceso_"+j).toString()))
					{
						logOriginal += "\n*  Ind. vía acceso ["+ValoresPorDefecto.getIntegridadDominio(cirugiasAnteriores.get("indViaAcceso_"+i).toString())+"] ";
						logModificado += "\n*  Ind. vía acceso ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("indViaAcceso_"+j).toString())+"]";
					}
					//10) Se verifica si se modificó la especialidad que interviene
					if(!cirugiasAnteriores.get("codigoEspecialidadInterviene_"+i).toString().equals(this.cirugias.get("codigoEspecialidadInterviene_"+j).toString()))
					{
						logOriginal += "\n*  Especialidad interviene ["+cirugiasAnteriores.get("nombreEspecialidadInterviene_"+i)+"] ";
						logModificado += "\n*  Especialidad interviene ["+Utilidades.getNombreEspecialidad(this.con, Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+j).toString()))+"]";
					}
					//11) Se verifica si se modificó liquidar servicio
					if(!cirugiasAnteriores.get("liquidarServicio_"+i).toString().equals(this.cirugias.get("liquidarServicio_"+j).toString()))
					{
						logOriginal += "\n*  Liquidar servicio ["+ValoresPorDefecto.getIntegridadDominio(cirugiasAnteriores.get("liquidarServicio_"+i).toString())+"] ";
						logModificado += "\n*  Liquidar servicio ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("liquidarServicio_"+j).toString())+"]";
					}
					//************************************************************************************************************************
					//***************VALIDACION DE LOS DATOS DE CADA ASOCIO (PROFESIONAL)*******************************************************
					profesionalesAnteriores = liquidacionDao.consultarProfesionalesCirugia(this.con, this.cirugias.get("codigo_"+j).toString());
					boolean huboModificacionProfesionales = false; //indica si hubo modificacion de profesionales
					
					
					//Se verifica si los existentes fueron modificados
					for(int k=0;k<Integer.parseInt(profesionalesAnteriores.get("numRegistros").toString());k++)
					{
						for(int l=0;l<Integer.parseInt(this.cirugias.get("numProfesionales_"+j).toString());l++)
						{
							if(profesionalesAnteriores.get("consecutivo_"+k).toString().equals(this.cirugias.get("consecutivo_"+j+"_"+l).toString()))
							{
								//1) SE verifica si se modificó el profesional
								if(!profesionalesAnteriores.get("codigoProfesional_"+k).toString().equals(this.cirugias.get("codigoProfesional_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
								//2) Se verifica si se modificó la especialidad
								if(!profesionalesAnteriores.get("codigoEspecialidad_"+k).toString().equals(this.cirugias.get("codigoEspecialidad_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
								//3) Se verifica si se modificó pool
								if(!profesionalesAnteriores.get("pool_"+k).toString().equals(this.cirugias.get("pool_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
								//4) Se verifica si se modificó tipo especialista
								if(!profesionalesAnteriores.get("tipoEspecialista_"+k).toString().equals(this.cirugias.get("tipoEspecialista_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
								//5) Se verifica si se modificó cobrable
								if(!profesionalesAnteriores.get("cobrable_"+k).toString().equals(this.cirugias.get("cobrable_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
								//6) Se verifica si se modificó asocio
								if(!profesionalesAnteriores.get("consecutivoAsocio_"+k).toString().equals(this.cirugias.get("consecutivoAsocio_"+j+"_"+l).toString()))
									huboModificacionProfesionales = true;
								
							}
						}
						
					}
					
					
					for(int l=0;l<Integer.parseInt(this.cirugias.get("numProfesionales_"+j).toString());l++)
					{
						//Se verifica si hubo alguno añadido
						if(!UtilidadTexto.getBoolean(this.cirugias.get("existeBd_"+j+"_"+l).toString())&&!UtilidadTexto.getBoolean(this.cirugias.get("eliminar_"+j+"_"+l).toString()))
							huboModificacionProfesionales = true;
						
						//Se verifica si hubo alguno eliminado
						if(UtilidadTexto.getBoolean(this.cirugias.get("existeBd_"+j+"_"+l).toString())&&UtilidadTexto.getBoolean(this.cirugias.get("eliminar_"+j+"_"+l).toString()))
							huboModificacionProfesionales = true;
						
					}
					
					if(huboModificacionProfesionales)
					{
						for(int k=0;k<Integer.parseInt(profesionalesAnteriores.get("numRegistros").toString());k++)
						{
							logOriginalProfesionales = "";
							logOriginalProfesionales += "\n*  Profesional ["+profesionalesAnteriores.get("nombreProfesional_"+k)+"] ";
							logOriginalProfesionales += "\n*  Especialidad ["+profesionalesAnteriores.get("nombreEspecialidad_"+k)+"] ";
							logOriginalProfesionales += "\n*  Pool ["+Utilidades.getNombrePool(this.con, Utilidades.convertirAEntero(profesionalesAnteriores.get("pool_"+k).toString(),true))+"] ";
							logOriginalProfesionales += "\n*  Tipo especialista ["+ValoresPorDefecto.getIntegridadDominio(profesionalesAnteriores.get("tipoEspecialista_"+k).toString())+"] ";
							logOriginalProfesionales += "\n*  Cobrable ["+ValoresPorDefecto.getIntegridadDominio(profesionalesAnteriores.get("cobrable_"+k).toString())+"] ";
							//Se agrega un título al log por honorario
							logOriginal += "\n\n     *** Honorario "+profesionalesAnteriores.get("nombreAsocio_"+k)+" ***" + logOriginalProfesionales;
							
							
						}
						
						/*logger.info("ASOCIOS DE LA CIRUGIA********************************************************");
						for(int l=0;l<Integer.parseInt(this.cirugias.get("numProfesionales_"+j).toString());l++)
							for(int m=0;m<indicesProfesionalesCirugia.length;m++)
								logger.info(indicesProfesionalesCirugia[m]+j+"_"+l+": "+this.cirugias.get(indicesProfesionalesCirugia[m]+j+"_"+l));
						//logger.info("FIN ASOCIOS DE LA CIRUGIA********************************************************");*/
						
						for(int l=0;l<Integer.parseInt(this.cirugias.get("numProfesionales_"+j).toString());l++)
							if(!UtilidadTexto.getBoolean(this.cirugias.get("eliminar_"+j+"_"+l).toString()))
							{
								logModificadoProfesionales = "";
								logModificadoProfesionales += "\n*  Profesional ["+Persona.obtenerApellidosNombresPersona(con, Integer.parseInt(this.cirugias.get("codigoProfesional_"+j+"_"+l).toString()))+"]";
								logModificadoProfesionales += "\n*  Especialidad ["+Utilidades.getNombreEspecialidad(con, Utilidades.convertirAEntero(this.cirugias.get("codigoEspecialidad_"+j+"_"+l).toString()))+"]";
								logModificadoProfesionales += "\n*  Pool ["+Utilidades.getNombrePool(con, Utilidades.convertirAEntero(this.cirugias.get("pool_"+j+"_"+l).toString(),true))+"]";
								logModificadoProfesionales += "\n*  Tipo especialista ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("tipoEspecialista_"+j+"_"+l).toString())+"]";
								logModificadoProfesionales += "\n*  Cobrable ["+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("cobrable_"+j+"_"+l).toString())+"]";
								//Se agrega un título al log por honorario
								logModificado += "\n\n     *** Honorario "+Utilidades.obtenerNombreTipoAsocio(con, Integer.parseInt(this.cirugias.get("consecutivoAsocio_"+j+"_"+l).toString()))[0]+" ***" + logModificadoProfesionales;
								
							}
					}
					//*****************************************************************************************************************************
				}
			}
			
			//Se verifica si hubo modificación den total del servicio
			if(!logOriginal.equals("")&&!logModificado.equals(""))
			{
				//Se agrega un título al log por servicio
				this.logOriginal += "\n\n     ****** "+cirugiasAnteriores.get("descripcionServicio_"+i)+" ******" + logOriginal;
				this.logModificado += "\n\n     ****** "+cirugiasAnteriores.get("descripcionServicio_"+i)+" ******" + logModificado;
			}
		}
		
		
	}
	/**
	 * Método implementado para verificar si fueron modificados los otros profesionales
	 * @return
	 */
	private void fueModificadoOtrosProfesionales() 
	{
		String logOriginal = "", logModificado = "";
		boolean huboCambio = false;
		HashMap<String, Object> otrosProfesionalesAnteriores = liquidacionDao.consultarOtrosProfesionales(this.con, this.numeroSolicitud);
		
		
		//***************************PRIMERO SE VERIFICA SI HUBO CAMBIO EN LOS OTROS PROFESIONALES******************************
		for(int i=0;i<Integer.parseInt(otrosProfesionalesAnteriores.get("numRegistros")+"");i++)
		{
			for(int j=0;j<this.getNumOtrosProfesionales();j++)
			{
				//Se muestra los cambios de los ya existentes
				if(
					otrosProfesionalesAnteriores.get("consecutivo_"+i).toString().equals(this.otrosProfesionales.get("consecutivo_"+j).toString())&&
					!UtilidadTexto.getBoolean(this.otrosProfesionales.get("eliminar_"+j).toString())&&
					(
						UtilidadTexto.getBoolean(otrosProfesionalesAnteriores.get("cobrable_"+i).toString())!=UtilidadTexto.getBoolean(this.otrosProfesionales.get("cobrable_"+j).toString())
						||
						!otrosProfesionalesAnteriores.get("codigoProfesional_"+i).toString().equals(this.otrosProfesionales.get("codigoProfesional_"+j).toString())
						||
						!otrosProfesionalesAnteriores.get("codigoAsocio_"+i).toString().equals(this.otrosProfesionales.get("codigoAsocio_"+j).toString())
					)
				)
					huboCambio = true;
				
				//Se muestra los eliminados
				if(
					
						otrosProfesionalesAnteriores.get("consecutivo_"+i).toString().equals(this.otrosProfesionales.get("consecutivo_"+j).toString())&&
						UtilidadTexto.getBoolean(this.otrosProfesionales.get("eliminar_"+j).toString())
				)
					huboCambio = true;
				
				
			}
		}
		
		for(int j=0;j<this.getNumOtrosProfesionales();j++)
			///Se agregó un nuevo profesional
			if(!UtilidadTexto.getBoolean(this.otrosProfesionales.get("existeBd_"+j).toString())&&!UtilidadTexto.getBoolean(this.otrosProfesionales.get("eliminar_"+j).toString()))
				huboCambio = true;
		//***************************************************************************************************************************
			
		//Se verifica si hubo cambio
		if(huboCambio)
		{
			//Se agregan los otros profesionales ya existentese
			for(int i=0;i<Integer.parseInt(otrosProfesionalesAnteriores.get("numRegistros")+"");i++)
				logOriginal += "\n* "+otrosProfesionalesAnteriores.get("nombreProfesional_"+i)+" ("+otrosProfesionalesAnteriores.get("nombreAsocio_"+i)+") [Cobrable: "+(UtilidadTexto.getBoolean(otrosProfesionalesAnteriores.get("cobrable_"+i).toString())?"Sí":"No")+"]";
			
			
			//Se agrega los profesionales que hay en el momento tomando los nuevos y los antiguos
			for(int j=0;j<this.getNumOtrosProfesionales();j++)
				if(!UtilidadTexto.getBoolean(this.otrosProfesionales.get("eliminar_"+j).toString()))
					logModificado += "\n* "+Persona.obtenerApellidosNombresPersona(this.con, Integer.parseInt(this.otrosProfesionales.get("codigoProfesional_"+j).toString()))+" ("+Utilidades.obtenerNombreTipoAsocio(con, Integer.parseInt(this.otrosProfesionales.get("codigoAsocio_"+j).toString()))[0]+") [Cobrable: "+(UtilidadTexto.getBoolean(this.otrosProfesionales.get("cobrable_"+j).toString())?"Sí":"No")+"] ";
		}
		
		if(!logOriginal.equals("")&&!logModificado.equals(""))
		{
			
			logOriginal = "\n\n     *** Otros Profesionales *** " + logOriginal;
			logModificado = "\n\n     *** Otros Profesionales *** " + logModificado;
		}
		
		if(!logOriginal.equals("")&&!logModificado.equals(""))
		{
			this.logOriginal += logOriginal;
			this.logModificado += logModificado;
		}
		
	}
	/**
	 * Método implementado para verificar si fue modificado los datos del acto quirurgico
	 * @return
	 */
	private void fueModificadoDatosActoQuirurgico() 
	{
		String logOriginal = "", logModificado = "";
		///Solicitud Cirugia
		SolicitudesCx solicitudCx = new SolicitudesCx();
		solicitudCx.cargarEncabezadoSolicitudCx(con, this.numeroSolicitud);
		
		
		//********************SE CONSULTA LA INFORMACIÓN DEL ACTO QUIRURGICO*******************************************
		//Se consultan los datos encabezado de la hoja quirurgica
		HashMap<String, Object> datosActoQuirurgicoAnteriores = liquidacionDao.cargarDatosActoQuirurgico(con, this.numeroSolicitud,usuario.getCodigoInstitucionInt());
		datosActoQuirurgicoAnteriores.put("fechaInicialCx", solicitudCx.getFechaInicialCx());
		datosActoQuirurgicoAnteriores.put("horaInicialCx", solicitudCx.getHoraInicialCx());
		datosActoQuirurgicoAnteriores.put("fechaFinalCx", solicitudCx.getFechaFinalCx());
		datosActoQuirurgicoAnteriores.put("horaFinalCx", solicitudCx.getHoraFinalCx());
		datosActoQuirurgicoAnteriores.put("fechaIngresoSala", solicitudCx.getFechaIngresoSala());
		datosActoQuirurgicoAnteriores.put("fechaSalidaSala", solicitudCx.getFechaSalidaSala());
		datosActoQuirurgicoAnteriores.put("horaIngresoSala", solicitudCx.getHoraIngresoSala());
		datosActoQuirurgicoAnteriores.put("horaSalidaSala", solicitudCx.getHoraSalidaSala());
		
		//Si ya se había registrado el indicador de liquidar anestesia se agrega en solicitudes cirugia
		if(!solicitudCx.getLiquidarAnestesia().equals(""))
			datosActoQuirurgicoAnteriores.put("cobrarAnestesia", solicitudCx.getLiquidarAnestesia());
		
		//1) Se verifica diferente fecha inicial cirugía
		if(!datosActoQuirurgicoAnteriores.get("fechaInicialCx").toString().equals(this.datosActoQx.get("fechaInicialCx").toString()))
		{
			logOriginal += "\n*  Fecha Inicial Cirugía ["+datosActoQuirurgicoAnteriores.get("fechaInicialCx")+"] ";
			logModificado += "\n*  Fecha Inicial Cirugía ["+this.datosActoQx.get("fechaInicialCx")+"] ";
		}
		//2) Se verifica diferente hora inicial cirugía
		if(!datosActoQuirurgicoAnteriores.get("horaInicialCx").toString().equals(this.datosActoQx.get("horaInicialCx").toString()))
		{
			logOriginal += "\n*  Hora Inicial Cirugía ["+datosActoQuirurgicoAnteriores.get("horaInicialCx")+"] ";
			logModificado += "\n*  Hora Inicial Cirugía ["+this.datosActoQx.get("horaInicialCx")+"] ";
		}
		//3) Se verifica diferente fecha final cirugía
		if(!datosActoQuirurgicoAnteriores.get("fechaFinalCx").toString().equals(this.datosActoQx.get("fechaFinalCx").toString()))
		{
			logOriginal += "\n*  Fecha Final Cirugía ["+datosActoQuirurgicoAnteriores.get("fechaFinalCx")+"] ";
			logModificado += "\n*  Fecha Final Cirugía ["+this.datosActoQx.get("fechaFinalCx")+"] ";
		}
		//4) Se verifica diferente hora final cirugía
		if(!datosActoQuirurgicoAnteriores.get("horaFinalCx").toString().equals(this.datosActoQx.get("horaFinalCx").toString()))
		{
			logOriginal += "\n*  Hora Final Cirugía ["+datosActoQuirurgicoAnteriores.get("horaFinalCx")+"] ";
			logModificado += "\n*  Hora Final Cirugía ["+this.datosActoQx.get("horaFinalCx")+"] ";
		}
		//5) Se verifica diferente fecha ingreso sala
		if(!datosActoQuirurgicoAnteriores.get("fechaIngresoSala").toString().equals(this.datosActoQx.get("fechaIngresoSala").toString()))
		{
			logOriginal += "\n*  Fecha Ingreso Sala ["+datosActoQuirurgicoAnteriores.get("fechaIngresoSala")+"] ";
			logModificado += "\n*  Fecha Ingreso Sala ["+this.datosActoQx.get("fechaIngresoSala")+"] ";
		}
		//6) Se verifica diferente hora ingreso sala
		if(!datosActoQuirurgicoAnteriores.get("horaIngresoSala").toString().equals(this.datosActoQx.get("horaIngresoSala").toString()))
		{
			logOriginal += "\n*  Hora Ingreso Sala ["+datosActoQuirurgicoAnteriores.get("horaIngresoSala")+"] ";
			logModificado += "\n*  Hora Ingreso Sala ["+this.datosActoQx.get("horaIngresoSala")+"] ";
		}
		//7) Se verifica diferente fecha salida sala
		if(!datosActoQuirurgicoAnteriores.get("fechaSalidaSala").toString().equals(this.datosActoQx.get("fechaSalidaSala").toString()))
		{
			logOriginal += "\n*  Fecha Salida Sala ["+datosActoQuirurgicoAnteriores.get("fechaSalidaSala")+"] ";
			logModificado += "\n*  Fecha Salida Sala ["+this.datosActoQx.get("fechaSalidaSala")+"] ";
		}
		//8) Se verifica diferente hora salida sala
		if(!datosActoQuirurgicoAnteriores.get("horaSalidaSala").toString().equals(this.datosActoQx.get("horaSalidaSala").toString()))
		{
			logOriginal += "\n*  Hora Salida Sala ["+datosActoQuirurgicoAnteriores.get("horaSalidaSala")+"] ";
			logModificado += "\n*  Hora Salida Sala ["+this.datosActoQx.get("horaSalidaSala")+"] ";
		}
		//9) Se verifica diferente tipo sala
		if(!datosActoQuirurgicoAnteriores.get("codigoTipoSala").toString().equals(this.datosActoQx.get("codigoTipoSala").toString()))
		{
			logOriginal += "\n*  Tipo Sala ["+Utilidades.obtenerNombreTipoSala(this.con, Utilidades.convertirAEntero(datosActoQuirurgicoAnteriores.get("codigoTipoSala").toString(),true))+"] ";
			logModificado += "\n*  Tipo Sala ["+Utilidades.obtenerNombreTipoSala(this.con,Utilidades.convertirAEntero(this.datosActoQx.get("codigoTipoSala").toString(),true))+"] ";
		}
		//10) Se verifica diferente sala
		if(!datosActoQuirurgicoAnteriores.get("codigoSala").toString().equals(this.datosActoQx.get("codigoSala").toString()))
		{
			logOriginal += "\n*  Sala ["+UtilidadesSalas.obtenerDescripcionSala(this.con, Utilidades.convertirAEntero(datosActoQuirurgicoAnteriores.get("codigoSala").toString(),true))+"] ";
			logModificado += "\n*  Sala ["+UtilidadesSalas.obtenerDescripcionSala(this.con, Utilidades.convertirAEntero(this.datosActoQx.get("codigoSala").toString(),true))+"] ";
		}
		//11) Se verifica diferente politraumatismo
		if(!datosActoQuirurgicoAnteriores.get("politraumatismo").toString().equals(this.datosActoQx.get("politraumatismo").toString()))
		{
			logOriginal += "\n*  Politraumatismo ["+(UtilidadTexto.getBoolean(datosActoQuirurgicoAnteriores.get("politraumatismo").toString())?"Sí":"No")+"] ";
			logModificado += "\n*  Politraumatismo ["+(UtilidadTexto.getBoolean(this.datosActoQx.get("politraumatismo").toString())?"Sí":"No")+"] ";
		}
		//12) Se verifica diferente cobrar anestesia
		if(!datosActoQuirurgicoAnteriores.get("cobrarAnestesia").toString().equals(this.datosActoQx.get("cobrarAnestesia").toString()))
		{
			logOriginal += "\n*  Cobrar anestesia ["+(UtilidadTexto.getBoolean(datosActoQuirurgicoAnteriores.get("cobrarAnestesia").toString())?"Sí":"No")+"] ";
			logModificado += "\n*  Cobrar anestesia ["+(UtilidadTexto.getBoolean(this.datosActoQx.get("cobrarAnestesia").toString())?"Sí":"No")+"] ";
		}
		
		if(!logOriginal.equals("")&&!logModificado.equals(""))
		{
			this.logOriginal += logOriginal;
			this.logModificado += logModificado;
		}
	}
	//*******************************************************************************************************************************
	//************************FIN - METODOS MANEJO INFORMACION ORDEN*****************************************************************
	//*******************************************************************************************************************************
	//*******************************************************************************************************************************
	//************************METODOS PROCESO LIQUIDACION****************************************************************************
	//*******************************************************************************************************************************
	
	/**
	 * Método para obtener la parametrización de los valores de los asocios x grupos
	 */
	private HashMap<String, Object> obtenerValorAsociosXGrupo(int grupo, int codigoAsocio)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("grupo", grupo);
		campos.put("tipoServicio",tipoServicio);
		campos.put("codigoAsocio",codigoAsocio);
		
		return liquidacionDao.obtenerValorAsociosXGrupo(con, campos);
	}
	
	/**
	 * Método que realiza la consulta de la excepcion Qx de un asocio
	 * @param con
	 * @param codigoTipoAsocio
	 * @param tipoEspecialista
	 * @param liquidacionPorEspecialidad 
	 * @return
	 */
	private HashMap<String, Object> obtenerExcepcionQxAsocio(int codigoTipoAsocio,String tipoEspecialista, boolean liquidacionPorEspecialidad)
	{
	
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoContrato",infoCobertura.getDtoSubCuenta().getContrato());
		campos.put("codigoCentroCosto",this.codigoCentroCostoEjecuta);
		campos.put("codigoViaIngreso",this.codigoViaIngreso);
		campos.put("codigoTipoPaciente",this.codigoTipoPaciente);
		campos.put("acronimoTipoCirugia",liquidacionPorEspecialidad?this.acronimoTipoCirugiaEspecialidad:this.acronimoTipoCirugia);
		campos.put("codigoTipoAsocio",codigoTipoAsocio);
		campos.put("codigoServicio",codigoServicio);
		campos.put("tipoServicio",tipoServicio);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("codigoGrupoServicio",codigoGrupoServicio);
		campos.put("codigoTipoSala",this.codigoTipoSala);
		campos.put("tipoEspecialista",tipoEspecialista);
		campos.put("via",via);
		
		return liquidacionDao.obtenerExcepcionQxAsocio(con, campos);
	}
	
	/**
	 * Método que retorna el servicio de un asocio
	 * @param con
	 * @return
	 */
	private void obtenerServicioAsocios()
	{
		int codigoServicioAsocio = ConstantesBD.codigoNuncaValido; 
		
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
		
			HashMap campos = new HashMap();
			campos.put("tipoServicio",tipoServicio);
			campos.put("codigoAsocio",this.asociosCirugia.get("codigoAsocio_"+i));
			campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
			
			codigoServicioAsocio = liquidacionDao.obtenerServicioAsocio(con, campos);
			
			if(codigoServicioAsocio<=0)
			{
				
				if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+"")&&
						this.asociosCirugia.get("tipoEspecialista_"+i).toString().equals(""))
					this.agregarErrorApResourceXCirugia("errors.notEspecific", "Para el asocio "+this.asociosCirugia.get("nombreAsocio_"+i)+" aún no se ha definido el profesional de la salud.","");
				else
					this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+this.asociosCirugia.get("nombreAsocio_"+i), "parametrizacion de servicio para el tipo servicio "+this.nombreTipoServicio+". Verificar en funcionalidad Servicios Asocios");
				
				this.asociosCirugia.put("codigoServicio_"+i, ConstantesBD.codigoNuncaValido);
			}
			else
				this.asociosCirugia.put("codigoServicio_"+i, codigoServicioAsocio);
			
		}
	}
	
	/**
	 * Método implementado para consultar la excepcion tarifa quirurgica de un asocio
	 * @param con
	 * @param grupoUvr
	 * @param codigoAsocio
	 * @param liquidacionPorEspecialidad 
	 * @return
	 */
	private HashMap<String, Object> obtenerExcepcionTarifaQxAsocio(double grupoUvr,int codigoAsocio, boolean liquidacionPorEspecialidad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("codigoViaIngreso",this.codigoViaIngreso);
		campos.put("codigoTipoPaciente",this.codigoTipoPaciente);
		campos.put("codigoCentroCosto",this.codigoCentroCostoEjecuta);
		campos.put("grupoUvr", grupoUvr);
		campos.put("codigoServicio",codigoServicio);
		campos.put("codigoGrupoServicio",codigoGrupoServicio);
		campos.put("tipoServicio",tipoServicio);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("acronimoTipoCirugia",liquidacionPorEspecialidad?this.acronimoTipoCirugiaEspecialidad:this.acronimoTipoCirugia);
		campos.put("codigoAsocio",codigoAsocio);
		
		return liquidacionDao.obtenerExcepcionTarifaQxAsocio(con, campos);
	}
	
	/**
	 * Método para realizar la consulta una excepcion tipo sala del asocio
	 * @param con
	 * @param codigoAsocio
	 * @param liquidacionPorEspecialidad 
	 * @return
	 */
	private HashMap<String, Object> obtenerExcepcionXTipoSala(int codigoAsocio, boolean liquidacionPorEspecialidad)
	{
		HashMap campos = new HashMap();
		campos.put("codigoTipoSala",this.codigoTipoSala);
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("tarifarioOficial",this.tarifarioOficialGeneral);
		campos.put("codigoAsocio",codigoAsocio);
		campos.put("tipoServicio",tipoServicio);
		campos.put("acronimoTipoCirugia",liquidacionPorEspecialidad?this.acronimoTipoCirugiaEspecialidad:this.acronimoTipoCirugia);
		
		return liquidacionDao.obtenerExcepcionXTipoSala(con, campos);
	}
	
	/**
	 * Método para consultar los porcentajes de cx multi de un asocio
	 * @param codigoAsocio
	 * @param tipoEspecialista
	 * @param liquidacionPorEspecialidad 
	 * @param via
	 * @return
	 */
	private HashMap<String, Object> obtenerPorcentajeCirugiaMultiple(int codigoAsocio,String tipoEspecialista, boolean liquidacionPorEspecialidad)
	{
		
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("tarifarioOficial", this.tarifarioOficialGeneral);
		campos.put("codigoInstitucion", usuario.getCodigoInstitucion());
		campos.put("codigoAsocio", codigoAsocio);
		campos.put("tipoServicio", tipoServicio);
		campos.put("codigoTipoSala", codigoTipoSala);
		campos.put("acronimoTipoCirugia", liquidacionPorEspecialidad?this.acronimoTipoCirugiaEspecialidad:this.acronimoTipoCirugia);
		campos.put("tipoEspecialista", tipoEspecialista);
		campos.put("via", via);
		
		return liquidacionDao.obtenerPorcentajeCirugiaMultiple(con, campos);
	}
	
	/**
	 * Método que consulta los valores de los asocios por uvr
	 * @param con
	 * @param uvr
	 * @param codigoOcupacion
	 * @param codigoEspecialidad
	 * @param tipoEspecialista
	 * @return
	 */
	private HashMap<String, Object> obtenerValorAsociosXUvr(double uvr,int codigoOcupacion,int codigoEspecialidad,String tipoEspecialista,int codigoAsocio)
	{
		
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion", usuario.getCodigoInstitucion());
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("codigoTipoSala",this.codigoTipoSala);
		campos.put("tipoServicio",tipoServicio);
		campos.put("uvr",uvr);
		campos.put("codigoTipoAnestesia",this.codigoTipoAnestesia);
		campos.put("codigoOcupacion",codigoOcupacion);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("tipoEspecialista",tipoEspecialista);
		campos.put("codigoAsocio",codigoAsocio);
		
		return liquidacionDao.obtenerValorAsociosXUvr(con, campos);
	}
	
	/**
	 * Método que realiza la consulta de los asocios por rango de tiempo
	 * @param porDuracionCirugia
	 * @param duracion
	 * @param codigoAsocio
	 * @return
	 */
	private HashMap<String, Object> obtenerValorAsociosXRangoTiempo(boolean porDuracionCirugia,int duracion,int codigoAsocio)
	{
		
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("tarifarioOficial",this.tarifarioOficialGeneral);
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("tipoTiempoBase",porDuracionCirugia?ConstantesIntegridadDominio.acronimoDuracionCirugia:ConstantesIntegridadDominio.acronimoDuracionUsoSala);
		campos.put("duracion", duracion);
		campos.put("codigoAsocio", codigoAsocio);
		campos.put("codigoServicio", codigoServicio);
		campos.put("tipoServicio", tipoServicio);
		campos.put("acronimoTipoCirugia", acronimoTipoCirugia);
		campos.put("codigoTipoAnestesia", codigoTipoAnestesia);
		
		return liquidacionDao.obtenerValorAsociosXRangoTiempo(this.con, campos);
	}
	
	/**
	 * Método que realiza la consulta de lso asocios del servicio x tarifa
	 * @param campos
	 * @return
	 */
	private HashMap<String, Object> obtenerAsociosServicioXTarifa()
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",usuario.getCodigoInstitucion());
		campos.put("codigoConvenio",infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
		campos.put("fechaReferencia",this.fechaFinalizacionHQ);
		campos.put("codigoEsquemaTarifario",codigoEsquemaTarifario);
		campos.put("tarifarioOficial",this.tarifarioOficialGeneral);
		campos.put("codigoServicio",codigoServicio);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("tipoServicio",tipoServicio);
		campos.put("codigoGrupoServicio",codigoGrupoServicio);
		
		return liquidacionDao.obtenerAsociosServicioXTarifa(con, campos);
	}
	
	/**
	 * Método que realiza la evaluación de inclusiones exclusiones para el consumo de materiales
	 *
	 */
	private void evaluacionInclusionesExclusionesConsumoMateriales() throws IPSException
	{
		Cargos cargos= new Cargos();
		int codigoArticulo = ConstantesBD.codigoNuncaValido;
		String nombreArticulo = "";
		double valorUnitario = 0;
		HashMap<String, Object> articulos = liquidacionDao.obtenerListaArticulosConsumoMateriales(this.con, this.numeroSolicitud);
		
		for(int i=0;i<Utilidades.convertirAEntero(articulos.get("numRegistros")+"", true);i++)
		{
			codigoArticulo = Integer.parseInt(articulos.get("codigoArticulo_"+i).toString());
			nombreArticulo = articulos.get("nombreArticulo_"+i).toString();
			//1) Validacion de la inclusion
			String incluye= cargos.obtenerInclusionExclusionXConvenioArticulo(con, infoCobertura.getDtoSubCuenta().getContrato(), this.codigoCentroCostoEjecuta, codigoArticulo, this.usuario.getCodigoInstitucionInt(), this.datosActoQx.get("fechaFinalCx").toString()).getAcronimo(); 

			int esquemaTarifario=this.infoCobertura.getDtoSubCuenta().getEsquemaTarifarioArticuloPpalOoriginal(con,this.infoCobertura.getDtoSubCuenta().getSubCuenta(),this.infoCobertura.getDtoSubCuenta().getContrato(),codigoArticulo,this.datosActoQx.get("fechaFinalCx").toString(), Cargos.obtenerCentroAtencionCargoSolicitud(con,Utilidades.convertirAEntero(this.numeroSolicitud)));
			
			//2) Se realiza el cálculo de la tarifa del artículo
			if(!UtilidadTexto.getBoolean(incluye))
				valorUnitario = Cargos.obtenerValorTarifaYExcepcion(con, this.infoCobertura.getDtoSubCuenta(), esquemaTarifario, this.usuario, codigoArticulo, false, Integer.parseInt(this.numeroSolicitud),this.datosActoQx.get("fechaFinalCx").toString());
			else
				valorUnitario = 0;
			
			String nombrEsquemaTarifarioArt = Utilidades.obtenerNomEsquemaTarifario(con, "false", esquemaTarifario);
			
			//Si el artículo no tiene tarifa base y no está incluido se genera error
			if(Cargos.obtenerTarifaBaseArticulo(this.con, codigoArticulo, esquemaTarifario, this.datosActoQx.get("fechaFinalCx").toString())<=0&&!UtilidadTexto.getBoolean(incluye))
				this.agregarErrorApResource("error.salasCirugia.noTiene", "el material "+codigoArticulo+" - "+nombreArticulo.toLowerCase(), "tarifa para el esquema tarifario "+nombrEsquemaTarifarioArt);
			//Se actualiza la información de inclusión/exclusión y tarifa en la finalización del consumo de materiales
			else if(liquidacionDao.actualizarInclusionYTarifaArticulo(con, this.numeroSolicitud, codigoArticulo, incluye, valorUnitario)<=0)
				this.agregarErrorApResource("errors.noPudoActualizar", "la información de inclusión/exclusión y tarifa para el consumo de "+codigoArticulo+" - "+nombreArticulo, "");
			
		}
	}
	
	/**
	 * Método que realiza el llamado de la liquidación desde otras funcionalidades
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param deboConsultar
	 * @return
	 */
	public boolean realizarLiquidacion(Connection con,String numeroSolicitud,UsuarioBasico usuario,boolean deboConsultar) throws IPSException
	{
		return realizarLiquidacion(con, numeroSolicitud, usuario,deboConsultar,false);
	}
	
	/**
	 * Método que realiza el proceso de la liquidación de cirugías
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param deboConsultar
	 * @return
	 */
	public boolean realizarLiquidacion(Connection con,String numeroSolicitud,UsuarioBasico usuario,boolean deboConsultar,boolean vieneDeFuncionalidad) throws IPSException
	{
		this.mensajesError = new ArrayList<ElementoApResource>();
		this.con = con;
		this.numeroSolicitud = numeroSolicitud;
		this.usuario = usuario;
		boolean exito = false, deboLiquidar = false;
		
		
		//**********VERIFICACIÓN DEL CAMPO DE LIQUIDACION AUTOMÁTICA**********************************
		//Se carga el indicativo cargo Qx
		this.indQx = SolicitudesCx.obtenerIndicativoCargoSolicitud(con, this.numeroSolicitud);
		if(this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia)||this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto))
			deboLiquidar = UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaCirugias(this.usuario.getCodigoInstitucionInt()));
		else
			deboLiquidar = UtilidadTexto.getBoolean(ValoresPorDefecto.getLiquidacionAutomaticaNoQx(this.usuario.getCodigoInstitucionInt()));
		
		//Si el proceso de liquidación se está corriendo desde la funcionalidad propia de luiquidacion 
		//quiere decir que la liquidación es normal (no es automática)
		//por lo tanto siempre se debe permitir liquidar
		if(vieneDeFuncionalidad)
			deboLiquidar = true;
		//**********************************************************************************************
		
		if(deboLiquidar)
		{
			//********VALIDACIONES INICIALES******************************************************
			//Se valida que se haya parametrizado el asocio de cirujano
			if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt())))
				this.agregarErrorApResource("error.parametrosGenerales.faltaDefinirParametro","Asocio Cirujano","");
			//Se valida que se haya parametrizado el asocio de anestesiología
			if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAnestesia(usuario.getCodigoInstitucionInt())))
				this.agregarErrorApResource("error.parametrosGenerales.faltaDefinirParametro","Asocio Anestesia","");
			//Se valida que se haya parametrizado el asocio de ayudante
			if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
				this.agregarErrorApResource("error.parametrosGenerales.faltaDefinirParametro","Asocio Ayudantía","");
			//Se valida que se haya parametrizado el parámetro materiales quirurgicos por acto
			if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())))
				this.agregarErrorApResource("error.parametrosGenerales.faltaDefinirParametro","¿Materiales por Acto Qx.?","");
			//Se valida que se haya parametrizado la especialidad del anestesiologo
			if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(), true)))
				this.agregarErrorApResource("error.parametrosGenerales.faltaDefinirParametro","Especialidad Anestesiología","");
			//Se valida que la solicitud no tenga consumos de materiales pendientes
			if(deboConsultar&&UtilidadesSalas.existeConsumoMaterialesPendiente(this.con, this.numeroSolicitud))
				this.agregarErrorApResource("errors.noExiste2","finalización para el consumo de materiales","");
			//*************************************************************************************
			
			//Si no hay errores en las validaciones iniciales se inicia el proceso de liquidacion
			if(this.mensajesError.size()<=0)
			{
				//********SE CARGA EL DETALLE DE LA ORDEN SI ES NECESARIO*******************************
				if(deboConsultar)
					this.cargarDetalleOrden();
				else
				{
					this.codigoCentroCostoEjecuta = Solicitud.obtenerCodigoCentroCostoSolicitado(con, this.numeroSolicitud);
					this.idCuenta = Utilidades.getCuentaSolicitud(con, Integer.parseInt(this.numeroSolicitud))+"";
					this.codigoViaIngreso = Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con,this.idCuenta));
					this.codigoTipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con,this.idCuenta).getAcronimo();
					
				}
				this.idIngreso = UtilidadValidacion.obtenerIngreso(this.con, Integer.parseInt(this.idCuenta))+"";
				//*************************************************************************************
				
				logger.info("NUMERO SOLICITUD=> "+this.numeroSolicitud);
				//Solo si la orden ya está finalizada se continua con la liquidación y que esté totalmente pendiente
				if(
					(
						(this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto)||this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto))
						||
						(
							(this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia)||this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
							&&
							UtilidadTexto.getBoolean(this.datosActoQx.get("hojaQxFinalizada")+"")
							&&
							UtilidadTexto.getBoolean(this.datosActoQx.get("hojaAnesFinalizada")+"")
						)
					)
					&&
					UtilidadesFacturacion.esSolicitudTotalPendiente(con,this.numeroSolicitud)
				)
				{
				
					//*******SE REALIZA LA ORDENACIÓN DE LOS SERVICIOS*************************************
					this.reordenarCirugiasSolicitud(deboConsultar,true);
					//*************************************************************************************
					
					//******SE REALIZA EL CÁLCULO DE LAS INCLUSIONES EXCLUSIONES DEl CONSUMO**************
					this.evaluacionInclusionesExclusionesConsumoMateriales();
					//************************************************************************************
					
					//*******SE GUARDA LA INFORMACIÓN**************************************************
					this.actualizarDatosOrden();
					//***********************************************************************************
					
					//*****SE CARGAN LOS PARÁMETROS DEL CONVENIO**************************************
					this.mundoConvenio.cargarResumen(this.con, infoCobertura.getDtoSubCuenta().getConvenio().getCodigo());
					//*********************************************************************************
					
					//******VALIDACION DE LA OCUPACIÓN DE LA SALA*************************************					
					if(this.datosActoQx.containsKey("fechaIngresoSala") && this.datosActoQx.containsKey("fechaSalidaSala") && 
							UtilidadFecha.validarFecha(this.datosActoQx.get("fechaIngresoSala").toString()) 
								&& UtilidadFecha.validacionHora(this.datosActoQx.get("horaIngresoSala").toString()).puedoSeguir
									&& UtilidadFecha.validarFecha(this.datosActoQx.get("fechaSalidaSala").toString())
										&& UtilidadFecha.validacionHora(this.datosActoQx.get("horaSalidaSala").toString()).puedoSeguir)
					{
						
						//******VALIDACION DE LA OCUPACIÓN DE LA SALA*************************************
						HashMap datosSala=HojaQuirurgica.estaSalaOcupada(
								con, 
								Integer.parseInt(this.numeroSolicitud), 
								Integer.parseInt(this.datosActoQx.get("codigoSala").toString()), 
								this.datosActoQx.get("fechaIngresoSala").toString(), 
								this.datosActoQx.get("horaIngresoSala").toString(), 
								this.datosActoQx.get("fechaSalidaSala").toString(), 
								this.datosActoQx.get("horaSalidaSala").toString());
						
						if(UtilidadTexto.getBoolean(datosSala.get("estaSalaOcupada")+""))
						{
							int numReg=Utilidades.convertirAEntero(datosSala.get("numRegistros")+"");
							for (int i=0;i<numReg;i++)
								this.agregarErrorApResource("error.salasCirugia.salaOcupada", "entre el rango de fecha/hora cirugía: "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaIngresoSala_"+i)+"") +" "+datosSala.get("horaIngresoSala_"+i)+" - "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaSalidaSala_"+i)+"") +" "+datosSala.get("horaSalidaSala_"+i), "");
						}
					}
					//********************************************************************************
					
					//**********PREPARAR INFORMACIÓN ORDEN PARA LIQUIDACIÓN*****************************
					this.prepararInfoOrdenParaLiquidacion();
					//**********************************************************************************
					
					//**********ITERACION DE CADA CIRUGÍA***********************************************
					this.modoLiquidacion = ConstantesBD.codigoNuncaValido;
					this.debeLiquidarConsumoMateriales = false;
					int numCirugiasAdicionalesLiquidar = 0;
					this.codigoEspecialidadInterviene = ConstantesBD.codigoNuncaValido;
					boolean puedoCobrar = false;
					
					for(int i=0;i<this.getNumCirugias();i++)
					{
						//Solo se toman los servicios que se vayan a liquidar
						if(UtilidadTexto.getBoolean(this.cirugias.get("liquidarServicio_"+i).toString()))
						{
							//Preparación de los mensajes de error
							this.tieneTituloMensajeCirugia = false;
							this.tituloMensajeCirugia01 = "Cirugía N°"+this.cirugias.get("numeroServicio_"+i)+". "+this.cirugias.get("descripcionServicio_"+i).toString().toLowerCase()+" - "+this.cirugias.get("nombreTipoCirugia_"+i).toString().toLowerCase();
							this.tituloMensajeCirugia02 = "Esq. Tar: "+Utilidades.getNombreEsquemaTarifario(con, Integer.parseInt(this.cirugias.get("codigoEsquemaTarifario_"+i).toString()))+", Vía: "+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("codigoViaCx_"+i).toString())+", Bilateral: "+ValoresPorDefecto.getIntegridadDominio(this.cirugias.get("indBilateral_"+i).toString());
							
							puedoCobrar = true;
							if(codigoEspecialidadInterviene==ConstantesBD.codigoNuncaValido)
								codigoEspecialidadInterviene = Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+i).toString());
							
							//**************SE PREPARAN LOS DATOS DEL SERVICIO (INCLUYENDO ASOCIOS)*******************************************************
							this.prepararDatosServicio(i);
							//********************************************************************************************************************
							
							//************CUENTA Y VALIDACION DE LAS CIRUGÍAS ADICIONALES POR COBRAR********************************************
							if(this.cirugias.get("acronimoTipoCirugiaEspecialidad_"+i).toString().equals(ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional)||
									this.cirugias.get("acronimoTipoCirugiaEspecialidad_"+i).toString().equals(ConstantesBDSalas.acronimoTipoCirugiaAdicionalBilateral))
							{
								if(codigoEspecialidadInterviene==Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+i).toString()))
									numCirugiasAdicionalesLiquidar++;
								else
								{
									numCirugiasAdicionalesLiquidar = 1;
									codigoEspecialidadInterviene = Integer.parseInt(this.cirugias.get("codigoEspecialidadInterviene_"+i).toString());
								}
								
								int cantidadMaxCirugias = Utilidades.convertirAEntero(this.mundoConvenio.getCantidadMaxCirugia());
								if(cantidadMaxCirugias==ConstantesBD.codigoNuncaValido)
									cantidadMaxCirugias = 1;
								
								if(numCirugiasAdicionalesLiquidar>cantidadMaxCirugias)
									puedoCobrar = false;
							}
							//*******************************************************************************************************************
							//*************SE VERIFICA SI SE DEBE COBRAR SERVICIO POR VALIDACIÓN DE VÍA DE ACCESO*******************************
							if(UtilidadTexto.getBoolean(this.cirugias.get("indViaAcceso_"+i).toString())&&this.getNumCirugias()>1)
								puedoCobrar = false;
							//*****************************************************************************************************************
							
							/**Si la cirugía se puede cobrar se prosigue, de lo contrario se ponen todos sus asocios en 0 **/
							if(puedoCobrar)
							{
								//logger.info("MAPA DE ASOCIOS CIRUGIA=> "+this.asociosCirugia);
								//************************VERIFICACIÓN DEL TIPO DE LIQUIDACION A USAR A NIVEL GENERAL*******************************
								//Se verifica el modo de liquidación que se debe usar
								//A) SOAT
								if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatGrupo)
									modoLiquidacion = ConstantesBDSalas.codigoLiquidacionSoat;
								//B) ISS
								else if(Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUvr)
									modoLiquidacion = ConstantesBDSalas.codigoLiquidacionIss;
								//C) MIXTA
								else if((Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUnidades||Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatValor)&&
										UtilidadTexto.getBoolean(this.cirugias.get("liquidarAsocios_"+i).toString()))
									modoLiquidacion = ConstantesBDSalas.codigoLiquidacionMixta;
								//D) TARIFA
								else if((Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatUnidades||Integer.parseInt(this.cirugias.get("codigoTipoLiquidacion_"+i).toString())==ConstantesBD.codigoTipoLiquidacionSoatValor)&&
										!UtilidadTexto.getBoolean(this.cirugias.get("liquidarAsocios_"+i).toString()))
									modoLiquidacion = ConstantesBDSalas.codigoLiquidacionTarifa;
								
								//Si el modoLiqudacion es diferente a Tarifa se verifica si es por rango de tiempos
								if(modoLiquidacion!=ConstantesBDSalas.codigoLiquidacionTarifa&&modoLiquidacion!=ConstantesBDSalas.codigoLiquidacionMixta&&this.debeLiquidarTodosXRangoTiempos)
									//E) RANGO DE TIEMPOS
									modoLiquidacion = ConstantesBDSalas.codigoLiquidacionRangoTiempos;
								//**************************************************************************************************************
								switch(modoLiquidacion)
								{
									case ConstantesBDSalas.codigoLiquidacionSoat:
										this.liquidacionSoat(i);
									break;
									case ConstantesBDSalas.codigoLiquidacionIss:
										this.liquidacionIss(i);
									break;
									case ConstantesBDSalas.codigoLiquidacionRangoTiempos:
										this.liquidacionRangoTiempos(ConstantesBD.codigoNuncaValido); //sin posicion de asocio específico
									break;
									case ConstantesBDSalas.codigoLiquidacionMixta:
										this.liquidacionMixta(i);
									break;
									case ConstantesBDSalas.codigoLiquidacionTarifa:
										this.liquidacionTarifa(i);
									break;
									default:
										this.agregarErrorApResourceXCirugia("errors.invalid", "El tipo de liquidación encontrado en la parametrización de la tarifa del servicio", "");
									break;
									
								}
								
								
							}
							//No se puede cobrar, se ponen todos los asocios en valor 0
							else
							{
								for(int j=0;j<this.getNumAsociosCirugia();j++)
								{
									this.asociosCirugia.put("valor_"+j, "0");
									this.asociosCirugia.put("codigoPropietario_"+j, "");
								}
								
								//Se obtienen los servicios de los asocios
								this.obtenerServicioAsocios();
								//Se obtiene el tipo de servicio de cada asocio
								this.consultarTipoServicioAsocios();
							}
							
							//Si no hubo titulo del mensaje de la cirugía, quiere decir que no hubo errores y se puede proseguir a la inserción del asocio
							if(!this.tieneTituloMensajeCirugia)
							{
								//***************INSERCIÓN DE LOS ASOCIOS************************************************
								int resultadoInsercion = ConstantesBD.codigoNuncaValido;
								int consecutivoHonorarios = ConstantesBD.codigoNuncaValido, consecutivoSalasMateriales = ConstantesBD.codigoNuncaValido;
								String tipoServicioAsocio = "";
								double valorAsocio = 0;
								double valorCirugia = 0;
								
								for(int j=0;j<this.getNumAsociosCirugia();j++)
								{
									if(this.asociosCirugia.get("cobrable_"+j).toString().equals(ConstantesBD.acronimoSi))
									{
										Cargos mundoCargos = new Cargos();
										valorAsocio = Math.ceil(Double.parseDouble(this.asociosCirugia.get("valor_"+j).toString()));
										tipoServicioAsocio = this.asociosCirugia.get("tipoServicio_"+j).toString();
										
										//*********Aplicacion del método de ajuste**********************************************
										if(valorAsocio>0&&UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+j).toString()))
										{
											String metodoAjuste = Cargos.obtenerMetodoAjuste(con, this.infoCobertura.getDtoSubCuenta().getConvenio().getCodigo(), codigoEsquemaTarifario, this.usuario.getCodigoInstitucionInt(), true);
									    	logger.info("MÉTODO AJUSTE ENCONTRADO: "+metodoAjuste+", valorTarifa: "+valorAsocio);
									    	if(!UtilidadTexto.isEmpty(metodoAjuste))
									    	{
									    		logger.info("paso por aqui !!");
									    		valorAsocio = UtilidadValidacion.aproximarMetodoAjuste(metodoAjuste, valorAsocio);
									    	}
									    	logger.info("NUEVO VALOR TARIFA: "+valorAsocio);
										}
										valorCirugia += valorAsocio;
								    	//***************************************************************************************
										
										resultadoInsercion = this.insertarAsocio(
											Utilidades.convertirAEntero(this.asociosCirugia.get("codigoEspecialidad_"+j).toString()), // código de la especialidad 
											Integer.parseInt(this.asociosCirugia.get("codigoProfesional_"+j).toString()), //codigo profesional
											Integer.parseInt(this.asociosCirugia.get("codigoServicio_"+j).toString()), // codigo del servicio
											Integer.parseInt(this.asociosCirugia.get("codigoPool_"+j).toString()), // codigo pool
											Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString()), // codigo asocio
											this.asociosCirugia.get("codigoPropietario_"+j).toString(), //codigo propietario
											valorAsocio, //valor
											tipoServicioAsocio, //tipo servicio
											this.asociosCirugia.get("cobrable_"+j).toString(), //cobrable
											//*********anexo 777
											UtilidadesSalas.obtenerCentroCostoEjecutaHonorarios(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString())).getCodigo(),  //centro Costo Ejecuta
											UtilidadesSalas.obtenerMedicoSalaMaterialesCx(this.datosActoQx.get("codigoSala")+"").getCodigo() //medico
											//*******************
											); 
										
										if(resultadoInsercion>0)
										{
											if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
											{
												consecutivoHonorarios = resultadoInsercion;
												consecutivoSalasMateriales = ConstantesBD.codigoNuncaValido;
											}
											else
											{
												consecutivoHonorarios = ConstantesBD.codigoNuncaValido;
												consecutivoSalasMateriales = resultadoInsercion;
											}
											
											//Generación del cargo
											resultadoInsercion = mundoCargos.generarCargoSolicitudesCxYSolSubCuenta(
													con, 
													Integer.parseInt(this.numeroSolicitud),
													this.idCuenta, 
													this.infoCobertura.getDtoSubCuenta().getSubCuenta(), 
													this.asociosCirugia.get("codigoServicio_"+j).toString(), 
													this.infoCobertura.getInfoCobertura().getIncluidoStr(), 
													this.codigoServicio+"", 
													Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString()), 
													this.codigoEsquemaTarifario, 
													this.usuario, 
													valorAsocio, 
													this.infoCobertura.getInfoCobertura().getRequiereAutorizacionStr(),
													this.usuario.getLoginUsuario(),
													consecutivoHonorarios,
													consecutivoSalasMateriales,
													this.usuario.getCodigoInstitucionInt(),
													false,
													this.esCirugiaPorConsumo,
													tipoServicioAsocio,
													0,0)?1:0;
											
											if(resultadoInsercion<=0)
												this.agregarErrorApResourceXCirugia("errors.problemasGenericos", "al registrar el cargo del asocio "+this.asociosCirugia.get("nombreAsocio_"+j), "");
										}
										else
											this.agregarErrorApResourceXCirugia("errors.problemasGenericos", "al registrar el valor del asocio "+this.asociosCirugia.get("nombreAsocio_"+j), "");
									}
								}
								this.cirugias.put("valor_"+i, valorCirugia);
								//****************************************************************************************
							}
							else
							{
								//Se asigna todo el valor de los asocios en 0
								for(int j=0;j<this.getNumAsociosCirugia();j++)
									this.asociosCirugia.put("valor_"+j,"0");
								
								this.cirugias.put("valor_"+i, "0");
							}
						} //Fin IF que verifica si se debe liquidar el servicio
					} //Fin FOR de iteracion de servicios
					//*********************************************************************************
					
					//***************REGISTRO DE LOS MATERIALES ESPECIALES******************************
					this.registrarMaterialesEspeciales();
					//***********************************************************************************
					//*************SI ES POR CONSUMO SE DEBE ACTUALIZAR EL INDICADOR**********************
					logger.info("********** ¿ACTULAIZAR INDICADOR DE CONSUMO DE MATERIALES? "+this.debeLiquidarConsumoMateriales+"***************");
					if(liquidacionDao.actualizarIndicadorConsumoMaterialesSolicitud(con,this.numeroSolicitud, this.debeLiquidarConsumoMateriales)<=0)
						this.agregarErrorApResource("errors.problemasGenericos", "al actualizar el indicativo de consumo de materiales en la orden", "");
					//*************************************************************************************
					
					
				}
				else
					this.agregarErrorApResource("errors.notEspecific", "Para realizar la liquidación, la solicitud debe tener finalizada la hoja quirurgica y hoja de anestesia y estar pendiente para liquidar","");
			}
			
			
			
			if(this.mensajesError.size()>0)
				exito = false;
			else
			{
				exito = true;
				
				//SE realiza el registro de la liquidacion de servicios
				if(liquidacionDao.insertarLiquidacionServicios(this.con, this.numeroSolicitud, this.usuario.getLoginUsuario())<=0)
				{
					exito = false;
					this.agregarErrorApResource("errors.problemasGenericos", "registrando el log base de datos del proceso", "");
				}
			}
		}
		
		return exito;
	}
	
	/**
	 * Método que carga al mapa asociosCirugia los asocios iniciales a liquidar de un servicio
	 * @param pos
	 */
	private void prepararDatosServicio(int pos) throws IPSException 
	{
		//Se prepara la información del servicio
		this.codigoServicio = Integer.parseInt(this.cirugias.get("codigoServicio_"+pos).toString());
		this.consecutivoServicio = this.cirugias.get("codigo_"+pos).toString();
		this.tipoServicio = Utilidades.obtenerTipoServicio(con, this.codigoServicio+"");
		this.nombreTipoServicio = UtilidadesFacturacion.obtenerNombreTipoServicio(con, this.tipoServicio);
		this.codigoEsquemaTarifario = Integer.parseInt(this.cirugias.get("codigoEsquemaTarifario_"+pos).toString());
		this.codigoEspecialidad = Integer.parseInt(this.cirugias.get("codigoEspecialidad_"+pos).toString());
		this.acronimoTipoCirugiaEspecialidad = this.cirugias.get("acronimoTipoCirugiaEspecialidad_"+pos).toString();;
		this.acronimoTipoCirugia = this.cirugias.get("acronimoTipoCirugia_"+pos).toString();
		this.nombreTipoCirugiaEspecialidad = this.cirugias.get("nombreTipoCirugiaEspecialidad_"+pos).toString();;
		this.nombreTipoCirugia = this.cirugias.get("nombreTipoCirugia_"+pos).toString();
		this.codigoGrupoServicio = UtilidadesFacturacion.consultarGrupoServicio(con, this.codigoServicio);
		this.numeroCirugia =  Integer.parseInt(this.cirugias.get("numeroServicio_"+pos).toString());
		this.grupoUvrServicio = Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+pos)+"", true);
		this.esBilateral = UtilidadTexto.getBoolean(this.cirugias.get("indBilateral_"+pos).toString());
		this.esCirugiaPorConsumo = false;
		this.mundoEsquemaTarifario = new EsquemaTarifario();
		try 
		{
			mundoEsquemaTarifario.cargarXcodigo(con, codigoEsquemaTarifario, usuario.getCodigoInstitucionInt());
		} 
		catch (SQLException e) 
		{
			logger.error("Error al cargar el esquema tarifario del servicio "+codigoServicio+": "+e);
			this.agregarErrorApResourceXCirugia("errors.problemasGenericos", "al cargar la información del esquema tarifario", "");
		}
		
		this.via = this.cirugias.get("codigoViaCx_"+pos).toString();
		
		
		//Se incializan los asocios de la cirugpía
		this.asociosCirugia = new HashMap<String, Object>();
		int numAsociosCirugia = this.getNumAsociosCirugia();
		this.tipoEspecialistaCirujano = "";
		
		//Se llenan los asocios de la cirugía------------------------------------------------------------------------
		for(int j=0;j<Integer.parseInt(this.cirugias.get("numProfesionales_"+pos).toString());j++)
			if(UtilidadTexto.getBoolean(this.cirugias.get("existeBd_"+pos+"_"+j)+""))
			{
				this.asociosCirugia.put("codigoAsocio_"+numAsociosCirugia, this.cirugias.get("consecutivoAsocio_"+pos+"_"+j));
				this.asociosCirugia.put("nombreAsocio_"+numAsociosCirugia, this.cirugias.get("nombreAsocio_"+pos+"_"+j));
				this.asociosCirugia.put("codigoProfesional_"+numAsociosCirugia, this.cirugias.get("codigoProfesional_"+pos+"_"+j));
				this.asociosCirugia.put("codigoEspecialidad_"+numAsociosCirugia, this.cirugias.get("codigoEspecialidad_"+pos+"_"+j));
				this.asociosCirugia.put("codigoPool_"+numAsociosCirugia, Utilidades.convertirAEntero(this.cirugias.get("pool_"+pos+"_"+j).toString(),true));
				this.asociosCirugia.put("cobrable_"+numAsociosCirugia, this.cirugias.get("cobrable_"+pos+"_"+j));
				this.asociosCirugia.put("tipoEspecialista_"+numAsociosCirugia, this.cirugias.get("tipoEspecialista_"+pos+"_"+j));
				
				//Si el asocio es cirujano se almacena temporalmente el tipo especialista
				if(Integer.parseInt(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()))==Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+numAsociosCirugia).toString()))
					tipoEspecialistaCirujano = this.asociosCirugia.get("tipoEspecialista_"+numAsociosCirugia).toString();
				
				if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+numAsociosCirugia).toString()))
					this.asociosCirugia.put("valor_"+numAsociosCirugia, "");
				else
					this.asociosCirugia.put("valor_"+numAsociosCirugia, "0");
				numAsociosCirugia++;
			}
		//Se adicionan los otros profesionales y anesteisiologo
		for(int j=0;j<this.getNumAsociosOrden();j++)
		{
			this.asociosCirugia.put("codigoAsocio_"+numAsociosCirugia, this.asociosOrden.get("codigoAsocio_"+j));
			this.asociosCirugia.put("nombreAsocio_"+numAsociosCirugia, this.asociosOrden.get("nombreAsocio_"+j));
			this.asociosCirugia.put("codigoProfesional_"+numAsociosCirugia, this.asociosOrden.get("codigoProfesional_"+j));
			this.asociosCirugia.put("codigoEspecialidad_"+numAsociosCirugia, this.asociosOrden.get("codigoEspecialidad_"+j));
			this.asociosCirugia.put("codigoPool_"+numAsociosCirugia, this.asociosOrden.get("codigoPool_"+j));
			this.asociosCirugia.put("cobrable_"+numAsociosCirugia, this.asociosOrden.get("cobrable_"+j));
			this.asociosCirugia.put("valor_"+numAsociosCirugia, this.asociosOrden.get("valor_"+j));
			//el tipo especialista de los asocios de la orden será el tipo especialista del cirujano
			this.asociosCirugia.put("tipoEspecialista_"+numAsociosCirugia, tipoEspecialistaCirujano);
			numAsociosCirugia++;
		}
		this.asociosCirugia.put("numRegistros",numAsociosCirugia);
		//---------------------------------------------------------------------------------------------------------------
		
	}
	/**
	 * Método implementado para realizar la liquidación Soat
	 * @param pos
	 */
	private void liquidacionSoat(int pos) throws IPSException 
	{
		int grupo = Utilidades.convertirAEntero(this.cirugias.get("grupoUvr_"+pos).toString(), true);
		//Se verifica que el grupo sea válido
		if(grupo<=0)
			this.agregarErrorApResourceXCirugia("errors.faltaParametrizacion", "grupo correctamente. Por favor verifique", "");
		else
		{
			//Se consultan los asocios del grupo
			HashMap<String, Object> asociosGrupo = obtenerValorAsociosXGrupo( grupo, ConstantesBD.codigoNuncaValido);
			
			if(Utilidades.convertirAEntero(asociosGrupo.get("numRegistros")+"", true)<=0)
				this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "grupo "+grupo, "parametrización de asocios en la funcionalidad de Grupos ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
			else
			{
				//*****************************SE CONSULTAN LAS TARIFAS DE CADA ASOCIO X GRUPO***************************************
				boolean encontro = false;
				//Se asigna el valor para cada asocio de la cirugia-------------------------------------------------------------------
				for(int i=0;i<this.getNumAsociosCirugia();i++)
				{
					encontro = false;
					for(int j=0;j<Integer.parseInt(asociosGrupo.get("numRegistros").toString());j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())==Integer.parseInt(asociosGrupo.get("asocio_"+j).toString()))
						{
							//Sólo si el asocio es cobrable se le asigna valor
							if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
								this.asociosCirugia.put("valor_"+i, asociosGrupo.get("valor_"+j));
							this.asociosCirugia.put("codigoParametrizacion_"+i, asociosGrupo.get("codigoParametrizacion_"+j));
							this.asociosCirugia.put("liquidarPorEspecialidad_"+i, asociosGrupo.get("liquidarPor_"+j).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
							
							encontro = true;
						}
					}
					
					//Si no se encontró parametrización se genera error
					if(!encontro)
					{
						if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+this.asociosCirugia.get("nombreAsocio_"+i), "parametrización para el grupo "+grupo+" en la funicionalidad de Grupos ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
						this.asociosCirugia.put("valor_"+i, "0");
					}
				}
				//-------------------------------------------------------------------------------------------------------------------------------------------
				//Se asignan los asocios adicionales que se encontraron en la parametrización de grupo y 
				//no están en el listado de asocios de la cirugia----------------------
				for(int i=0;i<Integer.parseInt(asociosGrupo.get("numRegistros").toString());i++)
				{
					encontro = false;
					for(int j=0;j<this.getNumAsociosCirugia();j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString())==Integer.parseInt(asociosGrupo.get("asocio_"+i).toString()))
							encontro = true;	
					}
					//Si el asocio del grupo no está en los asocios de la cirugía, se agrega
					//Nota * El asocio debe ser diferente a Honorarios
					if(!encontro&&!asociosGrupo.get("codigoTipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
					{
						this.asociosCirugia.put("codigoAsocio_"+this.getNumAsociosCirugia(), asociosGrupo.get("asocio_"+i));
						this.asociosCirugia.put("nombreAsocio_"+this.getNumAsociosCirugia(), asociosGrupo.get("nombreAsocio_"+i));
						this.asociosCirugia.put("codigoProfesional_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("codigoEspecialidad_"+this.getNumAsociosCirugia(), ConstantesBD.codigoEspecialidadMedicaNinguna);
						this.asociosCirugia.put("codigoPool_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("cobrable_"+this.getNumAsociosCirugia(), ConstantesBD.acronimoSi);
						this.asociosCirugia.put("valor_"+this.getNumAsociosCirugia(), asociosGrupo.get("valor_"+i));
						this.asociosCirugia.put("tipoEspecialista_"+this.getNumAsociosCirugia(), "");
						this.asociosCirugia.put("codigoParametrizacion_"+this.getNumAsociosCirugia(), asociosGrupo.get("codigoParametrizacion_"+i));
						this.asociosCirugia.put("liquidarPorEspecialidad_"+this.getNumAsociosCirugia(), asociosGrupo.get("liquidarPor_"+i).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						this.asociosCirugia.put("numRegistros", this.getNumAsociosCirugia()+1);
					}
				}
				//-----------------------------------------------------------------------------------------------------------
				//************************************************************************************************************
				//**************************************************************************************************************
				//***************SE CONSULTAN LOS TIPOS DE SERVICIO DE CADA ASOCIO**************************
				this.consultarTipoServicioAsocios();
				//*******************************************************************************************
				//**************SE OBTIENE EL SERVICIO DE CADA ASOCIO***********************************************************
				this.obtenerServicioAsocios();
				//*************************************************************************************************************
				
				//Si no se ha agregado titulo de cirugia para los mensajes de error, quiere decir que no ha habido error x cirugia y se prosigue
				if(!this.tieneTituloMensajeCirugia)
				{
					
					
					//******************VALIDACION DEL VALOR X RANGO DE TIEMPOS PARA ASOCIOS SALAS*********************************
					if(this.debeLiquidarSalaXRangoTiempo)
					{
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioSalaCirugia+"")&&
								UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
								this.liquidacionRangoTiempos(i); //Se vuelve a calcular el valor del asocio de salas
					}
					//********************************************************************************************************
					
					//****************SE CALCULAN LAS EXCEPCIONES Y PORCENTAJES DE LOS ASOCIOS******************
					this.aplicarExcepcionesYPorcentajesAsocios();
					//*******************************************************************************************
					
					//***********SE CONSULTAN LOS CODIGOS PROPIETARIOS DE CADA ASOCIO***************************
					this.consultarCodigoPropietarioAsocios();
					//********************************************************************************************
					
				}
			}
			
		}
		
		
		
		
	}
	
	/**
	 * Método implementado para realizar la liquidación ISS
	 * @param pos
	 */
	private void liquidacionIss(int pos) throws IPSException 
	{
		this.valorServicio = Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+pos).toString(), true);
		//Se verifica que el uvr sea válido
		if(valorServicio<=0)
			this.agregarErrorApResourceXCirugia("errors.faltaParametrizacion", "uvr's correctamente. Por favor verifique", "");
		else
		{
			logger.info("inicio de la consulta de los asocios x uvr!!!!");
			//Se consultan primero los asocios a nivel general
			HashMap<String, Object> asociosUvr = obtenerValorAsociosXUvr(valorServicio, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, ConstantesIntegridadDominio.acronimoIgual+"','"+ConstantesIntegridadDominio.acronimoDiferente, ConstantesBD.codigoNuncaValido);
			
			if(Utilidades.convertirAEntero(asociosUvr.get("numRegistros")+"", true)<=0)
				this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "el valor uvr: "+valorServicio, "parametrización de asocios ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+"). Por favor verificar la funcionalidad Asocios por UVR");
			else
			{
				logger.info("************MAPA ASOCIO X UVR**************************");
				Utilidades.imprimirMapa(asociosUvr);
				logger.info("************FIN MAPA ASOCIO X UVR**************************");
				
				//*****************************SE CONSULTAN LAS TARIFAS DE CADA ASOCIO X GRUPO***************************************
				boolean encontro = false;
				int codigoOcupacion = ConstantesBD.codigoNuncaValido , codigoEspecialidad = ConstantesBD.codigoNuncaValido, codigoAsocio = ConstantesBD.codigoNuncaValido;
				int nroConsulta = 1;
				String tipoEspecialista = "";
				//Se asigna el valor para cada asocio de la cirugia-------------------------------------------------------------------
				for(int i=0;i<this.getNumAsociosCirugia();i++)
				{
					encontro = false;
					nroConsulta = 1;
					//Se consulta la ocupacion médica
					codigoOcupacion = Utilidades.obtenerOcupacionMedica(this.con, Integer.parseInt(this.asociosCirugia.get("codigoProfesional_"+i).toString()));
					codigoEspecialidad = Integer.parseInt(this.asociosCirugia.get("codigoEspecialidad_"+i).toString());
					codigoAsocio = Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString());
					tipoEspecialista = this.asociosCirugia.get("tipoEspecialista_"+i).toString();
					
					String nombreTipoSala = "";
					
					//Se verifica si existe parametrización de cada asocio segun su información médica
					while(!encontro&&nroConsulta<=4)
					{	
						
						for(int j=0;j<Integer.parseInt(asociosUvr.get("numRegistros").toString());j++)
						{
							if(nombreTipoSala.equals(""))
								nombreTipoSala = asociosUvr.get("nombreTipoSala_"+j).toString();
							//Dependiendo de tipo de consulta se realiza el filtro
							switch(nroConsulta)
							{
								//Filtro específico por ocupacion, especialidad y tipoEspecialista
								case 1:
									if(codigoAsocio==Integer.parseInt(asociosUvr.get("asocio_"+j).toString())&&codigoOcupacion==Integer.parseInt(asociosUvr.get("codigoOcupacion_"+j).toString())&&codigoEspecialidad==Integer.parseInt(asociosUvr.get("codigoEspecialidad_"+j).toString())&&tipoEspecialista.equals(asociosUvr.get("tipoEspecialista_"+j).toString()))
										encontro = true;
								break;
								//Filtro específico por ocupacion, especialidad
								case 2:
									if(codigoAsocio==Integer.parseInt(asociosUvr.get("asocio_"+j).toString())&&codigoOcupacion==Integer.parseInt(asociosUvr.get("codigoOcupacion_"+j).toString())&&codigoEspecialidad==Integer.parseInt(asociosUvr.get("codigoEspecialidad_"+j).toString())&&(asociosUvr.get("tipoEspecialista_"+j).toString().equals("")||tipoEspecialista.equals(asociosUvr.get("tipoEspecialista_"+j).toString())))
										encontro = true;
								break;
								//Filtro específico por ocupacion
								case 3:
									if(codigoAsocio==Integer.parseInt(asociosUvr.get("asocio_"+j).toString())&&codigoOcupacion==Integer.parseInt(asociosUvr.get("codigoOcupacion_"+j).toString())&&(asociosUvr.get("tipoEspecialista_"+j).toString().equals("")||tipoEspecialista.equals(asociosUvr.get("tipoEspecialista_"+j).toString())))
										encontro = true;
								break;
								//Sin filtro
								case 4:
									if(codigoAsocio==Integer.parseInt(asociosUvr.get("asocio_"+j).toString())&&(asociosUvr.get("tipoEspecialista_"+j).toString().equals("")||tipoEspecialista.equals(asociosUvr.get("tipoEspecialista_"+j).toString())))
										encontro = true;
								break;
							}
							
							if(encontro)
							{
								//Se verifica si el asocio es cobrable para asignarle el valor
								if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
									this.asociosCirugia.put("valor_"+i, calculoTarifaBaseAsocioXUvr(asociosUvr, valorServicio, j));
								//Se asigna la información restante de la parametrización
								this.asociosCirugia.put("codigoParametrizacion_"+i, asociosUvr.get("codigoParametrizacion_"+j));
								this.asociosCirugia.put("liquidarPorEspecialidad_"+i, asociosUvr.get("liquidarPor_"+j).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
								this.asociosCirugia.put("rangoInicial_"+i, asociosUvr.get("rangoInicial_"+j));
								this.asociosCirugia.put("rangoFinal_"+i, asociosUvr.get("rangoFinal_"+j));
								j = Integer.parseInt(asociosUvr.get("numRegistros").toString()); //se rompe el ciclo
							}
							
						}
						
						nroConsulta++;
					}
					
					//Si no se encontró parametrización se genera error
					if(!encontro)
					{
						if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+this.asociosCirugia.get("nombreAsocio_"+i), "parametrización para las uvr's "+valorServicio+" "+(nombreTipoSala.equals("")?"":" para el tipo de sala "+nombreTipoSala)+" en la funcionalidad de Asocios por UVR ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
						this.asociosCirugia.put("valor_"+i, "0");
					}
				}
				if(this.tieneTituloMensajeCirugia)
				{
					return;
				}
				//-------------------------------------------------------------------------------------------------------------------------------------------
				//Se asignan los asocios adicionales que se encontraron en la parametrización de uvr y 
				//no están en el listado de asocios de la cirugia----------------------
				for(int i=0;i<Integer.parseInt(asociosUvr.get("numRegistros").toString());i++)
				{
					encontro = false;
					for(int j=0;j<this.getNumAsociosCirugia();j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString())==Integer.parseInt(asociosUvr.get("asocio_"+i).toString()))
							encontro = true;	
					}
					//Si el asocio de las uvr no está en los asocios de la cirugía, se agrega
					//Nota * El asocio debe ser distinto a Honorarios
					if(!encontro&&!asociosUvr.get("codigoTipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
					{
						this.asociosCirugia.put("codigoAsocio_"+this.getNumAsociosCirugia(), asociosUvr.get("asocio_"+i));
						this.asociosCirugia.put("nombreAsocio_"+this.getNumAsociosCirugia(), asociosUvr.get("nombreAsocio_"+i));
						this.asociosCirugia.put("codigoProfesional_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("codigoEspecialidad_"+this.getNumAsociosCirugia(), asociosUvr.get("codigoEspecialidad_"+i));
						this.asociosCirugia.put("codigoPool_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("cobrable_"+this.getNumAsociosCirugia(), ConstantesBD.acronimoSi);
						this.asociosCirugia.put("valor_"+this.getNumAsociosCirugia(), calculoTarifaBaseAsocioXUvr(asociosUvr, valorServicio, i));
						this.asociosCirugia.put("tipoEspecialista_"+this.getNumAsociosCirugia(), asociosUvr.get("tipoEspecialista_"+i));
						this.asociosCirugia.put("codigoParametrizacion_"+this.getNumAsociosCirugia(), asociosUvr.get("codigoParametrizacion_"+i));
						this.asociosCirugia.put("liquidarPorEspecialidad_"+this.getNumAsociosCirugia(), asociosUvr.get("liquidarPor_"+i).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						this.asociosCirugia.put("rangoInicial_"+this.getNumAsociosCirugia(), asociosUvr.get("rangoInicial_"+i));
						this.asociosCirugia.put("rangoFinal_"+this.getNumAsociosCirugia(), asociosUvr.get("rangoFinal_"+i));
						this.asociosCirugia.put("numRegistros", this.getNumAsociosCirugia()+1);
					}
				}
				//-----------------------------------------------------------------------------------------------------------
				//************************************************************************************************************
				//***************SE CONSULTAN LOS TIPOS DE SERVICIO DE CADA ASOCIO**************************
				this.consultarTipoServicioAsocios();
				//*******************************************************************************************
				//**************SE OBTIENE EL SERVICIO DE CADA ASOCIO***********************************************************
				this.obtenerServicioAsocios();
				//*************************************************************************************************************
				
				//Si no se ha agregado titulo de cirugia para los mensajes de error, quiere decir que no ha habido error x cirugia y se prosigue
				if(!this.tieneTituloMensajeCirugia)
				{
					
					logger.info("PASÓ POR AQUÍ LIQUIDACIÓN ISS!!!!!!");
					//******************VALIDACION DEL VALOR X RANGO DE TIEMPOS PARA ASOCIOS SALAS*********************************
					if(this.debeLiquidarSalaXRangoTiempo)
					{
						for(int i=0;i<this.getNumAsociosCirugia();i++)
						{
							
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioSalaCirugia+"")&&UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
								this.liquidacionRangoTiempos(i); //Se vuelve a calcular el valor del asocio de salas
						}
					}
					//********************************************************************************************************
					
					//****************SE CALCULAN LAS EXCEPCIONES Y PORCENTAJES DE LOS ASOCIOS******************
					this.aplicarExcepcionesYPorcentajesAsocios();
					//*******************************************************************************************
					
					//***********SE CONSULTAN LOS CODIGOS PROPIETARIOS DE CADA ASOCIO***************************
					this.consultarCodigoPropietarioAsocios();
					//********************************************************************************************
					
				}
			}
		}
	}
	
	/**
	 * Método implementado para realizar la liquidacion x rango de tiempos
	 * @param pos
	 */
	private void liquidacionRangoTiempos(int posAsocio) throws IPSException
	{
		int duracion = 0;
		
		//********SE CALCULA LA DURACIÓN DEL USO SALA/ CIRUGIA**************************************
		if(this.porDuracionCirugia)
			duracion = UtilidadFecha.numeroMinutosEntreFechas(this.datosActoQx.get("fechaInicialCx").toString(), this.datosActoQx.get("horaInicialCx").toString(), this.datosActoQx.get("fechaFinalCx").toString(), this.datosActoQx.get("horaFinalCx").toString());
		else
			duracion = UtilidadFecha.numeroMinutosEntreFechas(this.datosActoQx.get("fechaIngresoSala").toString(), this.datosActoQx.get("horaIngresoSala").toString(), this.datosActoQx.get("fechaSalidaSala").toString(), this.datosActoQx.get("horaSalidaSala").toString());
		//*******************************************************************************************
		
		//********SE CONSULTA LA TARIFA DE LOS ASOCIOS X RANGO DE TIEMPOS*******************************
		//Nota * Si la variable posAsocio viene con un codigo válido, se realizará la busqueda por un asocio específico
		HashMap<String, Object> asociosRangoTiempo = obtenerValorAsociosXRangoTiempo(this.porDuracionCirugia, duracion, Utilidades.convertirAEntero(this.asociosCirugia.get("codigoAsocio_"+posAsocio)+"", true));
		
		if(Utilidades.convertirAEntero(asociosRangoTiempo.get("numRegistros")+"", true)<=0)
		{
			//Se edita el mensaje dependiendo si la busqueda es por todos los asocios o por un asocio específico de salas
			this.agregarErrorApResourceXCirugia(
				"error.salasCirugia.noTiene", 
				"la duración de "+(this.porDuracionCirugia?"cirugía":"uso sala")+" de "+duracion+" minutos", "parametrización "+(posAsocio!=ConstantesBD.codigoNuncaValido?"para el asocio "+this.asociosCirugia.get("nombreAsocio_"+posAsocio):"de asocios")+" en la funcionalidad de Asocios por Rango de Tiempo ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
		}
		else
		{
			boolean encontro = false;
			//Se asigna el valor para cada asocio de la cirugia-------------------------------------------------------------------
			for(int i=0;i<this.getNumAsociosCirugia();i++)
			{
				encontro = false;
				
				for(int j=0;j<Integer.parseInt(asociosRangoTiempo.get("numRegistros").toString());j++)
				{
					if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())==Integer.parseInt(asociosRangoTiempo.get("asocio_"+j).toString())&&
						(posAsocio==ConstantesBD.codigoNuncaValido||posAsocio==i))
					{
						//Sólo si el asocio es cobrable se le asigna valor
						if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							this.asociosCirugia.put("valor_"+i, calculoTarifaBaseAsocioXRangoTiempo(asociosRangoTiempo, duracion, j));
						//Se llena la información de parametrización
						this.asociosCirugia.put("codigoParametrizacion_"+i, asociosRangoTiempo.get("codigoParametrizacion_"+j));
						this.asociosCirugia.put("rangoFinal_"+i, asociosRangoTiempo.get("minutosRangoFinal_"+j));
						this.asociosCirugia.put("liquidarPorEspecialidad_"+i, asociosRangoTiempo.get("liquidarPor_"+j).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						
						encontro = true;
					}
				}
				
				//Si no se encontró parametrización se genera error
				if(!encontro&&(posAsocio==ConstantesBD.codigoNuncaValido||posAsocio==i))
				{
					if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
						this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+this.asociosCirugia.get("nombreAsocio_"+i), "parametrización para la duración de "+(this.porDuracionCirugia?"cirugía":"uso sala")+" de "+duracion+" minutos en la funcionalidad de Asocios por Rango de Tiempo ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
					this.asociosCirugia.put("valor_"+i, "0");
				}
			}
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//Si la liquidación se está haciendo por todos los asocios se continua con la carga de los nuevos asocios parametrizados
			if(posAsocio==ConstantesBD.codigoNuncaValido)
			{
				//Se asignan los asocios adicionales que se encontraron en la parametrización de rango de tiempos y 
				//no están en el listado de asocios de la cirugia----------------------
				for(int i=0;i<Integer.parseInt(asociosRangoTiempo.get("numRegistros").toString());i++)
				{
					encontro = false;
					for(int j=0;j<this.getNumAsociosCirugia();j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString())==Integer.parseInt(asociosRangoTiempo.get("asocio_"+i).toString()))
							encontro = true;	
					}
					//Si el asocio del grupo no está en los asocios de la cirugía, se agrega
					if(!encontro&&!asociosRangoTiempo.get("codigoTipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
					{
						this.asociosCirugia.put("codigoAsocio_"+this.getNumAsociosCirugia(), asociosRangoTiempo.get("asocio_"+i));
						this.asociosCirugia.put("nombreAsocio_"+this.getNumAsociosCirugia(), asociosRangoTiempo.get("nombreAsocio_"+i));
						this.asociosCirugia.put("codigoProfesional_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("codigoEspecialidad_"+this.getNumAsociosCirugia(), ConstantesBD.codigoEspecialidadMedicaNinguna);
						this.asociosCirugia.put("codigoPool_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("cobrable_"+this.getNumAsociosCirugia(), ConstantesBD.acronimoSi);
						this.asociosCirugia.put("valor_"+this.getNumAsociosCirugia(), calculoTarifaBaseAsocioXRangoTiempo(asociosRangoTiempo, duracion, i));
						this.asociosCirugia.put("tipoEspecialista_"+this.getNumAsociosCirugia(), "");
						this.asociosCirugia.put("codigoParametrizacion_"+this.getNumAsociosCirugia(), asociosRangoTiempo.get("codigoParametrizacion_"+i));
						this.asociosCirugia.put("liquidarPorEspecialidad_"+this.getNumAsociosCirugia(), asociosRangoTiempo.get("liquidarPor_"+i).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						this.asociosCirugia.put("rangoFinal_"+this.getNumAsociosCirugia(), asociosRangoTiempo.get("minutosRangoFinal_"+i));
						this.asociosCirugia.put("numRegistros", this.getNumAsociosCirugia()+1);
					}
					
		
					
				}
				//-----------------------------------------------------------------------------------------------------------
				//***************SE CONSULTAN LOS TIPOS DE SERVICIO DE CADA ASOCIO**************************
				this.consultarTipoServicioAsocios();
				//*******************************************************************************************
				//**************SE OBTIENE EL SERVICIO DE CADA ASOCIO***********************************************************
				this.obtenerServicioAsocios();
				//*************************************************************************************************************
				//Si no se ha agregado titulo de cirugia para los mensajes de error, quiere decir que no ha habido error x cirugia y se prosigue
				if(!this.tieneTituloMensajeCirugia)
				{
					
					//****************SE CALCULAN LAS EXCEPCIONES Y PORCENTAJES DE LOS ASOCIOS******************
					this.aplicarExcepcionesYPorcentajesAsocios();
					//*******************************************************************************************
					
					//***********SE CONSULTAN LOS CODIGOS PROPIETARIOS DE CADA ASOCIO***************************
					this.consultarCodigoPropietarioAsocios();
					//********************************************************************************************
					
				}
			}
			
		}
		//************************************************************************************************************
		
		
		
	}
	
	/**
	 * Método implementado para realizar la liquidación mixta
	 * @param pos 
	 *
	 */
	private void liquidacionMixta(int pos) throws IPSException
	{
		//Se obtiene el valor del servicio
		this.valorServicio = Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+pos).toString(), true);
		
		if(valorServicio<=0)
			this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El servicio", "tarifa parametrizada de acuerdo al esquema tarifario "+this.mundoEsquemaTarifario.getNombre()+" en la funcionalidad de Tarifas de Servicio (Facturación)");
		else
		{
			//Se aplican las excepciones del valor del servicio
			this.valorServicio = Cargos.obtenerValorTarifaYExcepcion(this.con, this.infoCobertura.getDtoSubCuenta(), this.codigoEsquemaTarifario, this.usuario, this.codigoServicio, true, Integer.parseInt(this.numeroSolicitud),this.datosActoQx.get("fechaFinalCx").toString());
			
			
			//Se consultan los asocios por tarifa
			HashMap<String, Object> asociosXTarifa = this.obtenerAsociosServicioXTarifa();
			
			if(Utilidades.convertirAEntero(asociosXTarifa.get("numRegistros")+"", true)<=0)
				this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "el servicio", "parametrización de asocios en la funcionalidad de Asocios Servicios de Tarifa ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
			else
			{
				//*****************************SE ASIGNAN LOS ASOCIOS VALIDANDO LOS EXISTENTES**********************************************
				boolean encontro = false;
				//Se asigna el asocio del servicio-----------------------------------------------------------------------------------------------
				for(int i=0;i<this.getNumAsociosCirugia();i++)
				{
					encontro = false;
					for(int j=0;j<Integer.parseInt(asociosXTarifa.get("numRegistros").toString());j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())==Integer.parseInt(asociosXTarifa.get("codigoAsocio_"+j).toString()))
						{
							encontro = true;
							this.asociosCirugia.put("modoLiquidacion_"+i,ConstantesBD.codigoNuncaValido); //Se asigna un modo de liquidación inválido
							this.asociosCirugia.put("liquidarPorEspecialidad_"+i,asociosXTarifa.get("liquidarPor_"+j).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo); 
						}
					}
					
					//Si no se encontró parametrización se genera error
					if(!encontro)
					{
						if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+this.asociosCirugia.get("nombreAsocio_"+i), "parametrización para el servicio en la funcionalidad de Asocios Servicios de Tarifa ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")");
						this.asociosCirugia.put("valor_"+i, "0");
					}
				}
				//-------------------------------------------------------------------------------------------------------------------------------------------
				//Se asignan los asocios adicionales que se encontraron en la parametrización de asocios x tarifa y 
				//no están en el listado de asocios de la cirugia----------------------
				for(int i=0;i<Integer.parseInt(asociosXTarifa.get("numRegistros").toString());i++)
				{
					encontro = false;
					for(int j=0;j<this.getNumAsociosCirugia();j++)
					{
						if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+j).toString())==Integer.parseInt(asociosXTarifa.get("codigoAsocio_"+i).toString()))
							encontro = true;	
					}
					//Si el asocio del grupo no está en los asocios de la cirugía, se agrega
					//Nota * El asocio debe ser diferente a honorarios
					if(!encontro&&!asociosXTarifa.get("codigoTipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioHonorariosCirugia+""))
					{
						this.asociosCirugia.put("codigoAsocio_"+this.getNumAsociosCirugia(), asociosXTarifa.get("codigoAsocio_"+i));
						this.asociosCirugia.put("nombreAsocio_"+this.getNumAsociosCirugia(), asociosXTarifa.get("nombreAsocio_"+i));
						this.asociosCirugia.put("codigoProfesional_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("codigoEspecialidad_"+this.getNumAsociosCirugia(), ConstantesBD.codigoEspecialidadMedicaNinguna);
						this.asociosCirugia.put("codigoPool_"+this.getNumAsociosCirugia(), ConstantesBD.codigoNuncaValido);
						this.asociosCirugia.put("cobrable_"+this.getNumAsociosCirugia(), ConstantesBD.acronimoSi);
						this.asociosCirugia.put("valor_"+this.getNumAsociosCirugia(), "");
						this.asociosCirugia.put("tipoEspecialista_"+this.getNumAsociosCirugia(), "");
						this.asociosCirugia.put("modoLiquidacion_"+this.getNumAsociosCirugia(),ConstantesBD.codigoNuncaValido); //Se asigna un modo de liquidación inválido
						this.asociosCirugia.put("liquidarPorEspecialidad_"+this.getNumAsociosCirugia(),asociosXTarifa.get("liquidarPor_"+i).toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						this.asociosCirugia.put("numRegistros", this.getNumAsociosCirugia()+1);
					}
				}
				//-----------------------------------------------------------------------------------------------------------
				//************************************************************************************************************
				//***************SE CONSULTAN LOS TIPOS DE SERVICIO DE CADA ASOCIO**************************
				this.consultarTipoServicioAsocios();
				//*******************************************************************************************
				//**************SE OBTIENE EL SERVICIO DE CADA ASOCIO***********************************************************
				this.obtenerServicioAsocios();
				//*************************************************************************************************************
				
				//Si no se ha agregado titulo de cirugia para los mensajes de error, quiere decir que no ha habido error x cirugia y se prosigue
				if(!this.tieneTituloMensajeCirugia)
				{
					
					
					//******************VALIDACION DEL VALOR X RANGO DE TIEMPOS PARA ASOCIOS *********************************
					if(this.debeLiquidarTodosXRangoTiempos)
					{
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							{
								this.liquidacionRangoTiempos(i);
								this.asociosCirugia.put("modoLiquidacion_"+i,ConstantesBDSalas.codigoLiquidacionRangoTiempos); 
							}
					}
					else if(this.debeLiquidarSalaXRangoTiempo)
					{
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioSalaCirugia+"")&&UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
							{
								this.liquidacionRangoTiempos(i); //Se vuelve a calcular el valor del asocio de salas
								this.asociosCirugia.put("modoLiquidacion_"+i,ConstantesBDSalas.codigoLiquidacionRangoTiempos);
							}
					}
					//********************************************************************************************************
					
					//****************SE CALCULAN LAS EXCEPCIONES Y PORCENTAJES DE LOS ASOCIOS******************
					this.aplicarExcepcionesYPorcentajesAsocios();
					//*******************************************************************************************
					
					//***********SE CONSULTAN LOS CODIGOS PROPIETARIOS DE CADA ASOCIO***************************
					this.consultarCodigoPropietarioAsocios();
					//********************************************************************************************
					
				}
				
				
			}
			
		}
	}
	
	/**
	 * Método implementado para realizar la liquidación x tarifa
	 * @param pos 
	 *
	 */
	private void liquidacionTarifa(int pos) throws IPSException
	{
		//Se obtiene el valor del servicio
		this.valorServicio = Utilidades.convertirADouble(this.cirugias.get("grupoUvr_"+pos).toString(), true);
		//logger.info("ENTRO AQUÍ A LIQUIDAR SERVICIO POR TARIFA=> "+this.valorServicio);
		if(valorServicio<=0)
			this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El servicio", "tarifa parametrizada de acuerdo al esquema tarifario "+this.mundoEsquemaTarifario.getNombre());
		else
		{
			//Se aplican las excepciones del valor del servicio
			this.valorServicio = Cargos.obtenerValorTarifaYExcepcion(this.con, this.infoCobertura.getDtoSubCuenta(), this.codigoEsquemaTarifario, this.usuario, this.codigoServicio, true, Integer.parseInt(this.numeroSolicitud),this.datosActoQx.get("fechaFinalCx").toString());
			
			//Se obtiene el servicio de procedimientos
			InfoDatosInt asocioProcedimiento = liquidacionDao.obtenerAsocioProcedimientos(this.con, this.usuario.getCodigoInstitucionInt());
			
			if(asocioProcedimiento.getCodigo()>0)
			{
				//Mapa donde se almacenará el único asocio de la liquidación por tarifa
				HashMap<String, Object> asocio = new HashMap<String, Object>();
				asocio.put("numRegistros","0");
				
				//Se consulta el asocio cirujano del servicio
				for(int i=0;i<this.getNumAsociosCirugia();i++)
				{
					if(Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())==Integer.parseInt(ValoresPorDefecto.getAsocioCirujano(this.usuario.getCodigoInstitucionInt())))
					{
						asocio.put("numRegistros", "1");
						
						asocio.put("codigoAsocio_0", asocioProcedimiento.getCodigo());
						asocio.put("nombreAsocio_0", asocioProcedimiento.getNombre());
						asocio.put("codigoServicio_0", this.codigoServicio);
						asocio.put("tipoServicio_0", ConstantesBD.codigoServicioProcedimiento+"");
						asocio.put("codigoProfesional_0", this.asociosCirugia.get("codigoProfesional_"+i));
						asocio.put("codigoEspecialidad_0", this.asociosCirugia.get("codigoEspecialidad_"+i));
						asocio.put("codigoPool_0", this.asociosCirugia.get("codigoPool_"+i));
						asocio.put("codigoEspecialidad_0", this.asociosCirugia.get("codigoEspecialidad_"+i));
						asocio.put("codigoPropietario_0", Utilidades.obtenerCodigoPropietarioServicio(this.con, this.codigoServicio+"", this.tarifarioOficialGeneral));
						asocio.put("cobrable_0", ConstantesBD.acronimoSi);
						asocio.put("valor_0", this.valorServicio);
						asocio.put("tipoEspecialista_0", this.asociosCirugia.get("tipoEspecialista_"+i));
						
					}
				}
				
				if(Integer.parseInt(asocio.get("numRegistros")+"")>0)
					//Los asocios definitivos de la orden será el asocio de la tarifa del servicio
					this.asociosCirugia = asocio;
				else
					this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "el servicio", "un honorario cirujano para asignarle la tarifa");
			}
			else
				this.agregarErrorApResourceXCirugia("errors.problemasGenericos", "debido a que no existe el asocio de tipo procedimientos en la tabla tipos_asocio", "");
		}
	}
			
	
	/**
	 * Método implementado para preparar la información de la orden para la liquidacion
	 *
	 */
	private void prepararInfoOrdenParaLiquidacion() 
	{
		this.codigoTipoSala = Integer.parseInt(this.datosActoQx.get("codigoTipoSala").toString());
		this.fechaFinalizacionHQ = this.datosActoQx.get("fechaFinaliza").toString();
		this.politraumatismo = UtilidadTexto.getBoolean(this.datosActoQx.get("politraumatismo").toString());
		
		this.asociosOrden = new HashMap<String, Object>();
		int pos = this.getNumAsociosOrden();
		
		//Se toma el asocio de Anestesia
		this.codigoTipoAnestesia = Integer.parseInt(this.datosActoQx.get("codigoTipoAnestesia").toString());
		this.asociosOrden.put("codigoAsocio_"+pos,ValoresPorDefecto.getAsocioAnestesia(usuario.getCodigoInstitucionInt()));
		this.asociosOrden.put("nombreAsocio_"+pos,Utilidades.obtenerNombreTipoAsocio(con, Integer.parseInt(this.asociosOrden.get("codigoAsocio_"+pos).toString()))[0]);
		this.asociosOrden.put("codigoProfesional_"+pos,UtilidadTexto.isEmpty(this.datosActoQx.get("codigoAnestesiologo").toString())?this.codigoCirujano:this.datosActoQx.get("codigoAnestesiologo"));
		this.asociosOrden.put("codigoEspecialidad_"+pos,ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(), true));
		this.asociosOrden.put("cobrable_"+pos,this.datosActoQx.get("cobrarAnestesia"));
		this.asociosOrden.put("codigoPool_"+pos,ConstantesBD.codigoNuncaValido);
		if(UtilidadTexto.getBoolean(this.datosActoQx.get("cobrarAnestesia").toString()))
			this.asociosOrden.put("valor_"+pos,""); //pendiente de asignar
		else
			this.asociosOrden.put("valor_"+pos,"0");
		pos++;
		
		//Se toman los asocios de otros profesionales
		for(int i=0;i<this.getNumOtrosProfesionales();i++)
		{
			this.asociosOrden.put("codigoAsocio_"+pos, this.otrosProfesionales.get("codigoAsocio_"+i));
			this.asociosOrden.put("nombreAsocio_"+pos, this.otrosProfesionales.get("nombreAsocio_"+i));
			this.asociosOrden.put("codigoProfesional_"+pos, this.otrosProfesionales.get("codigoProfesional_"+i));
			this.asociosOrden.put("codigoEspecialidad_"+pos, ConstantesBD.codigoEspecialidadMedicaNinguna);
			this.asociosOrden.put("cobrable_"+pos, this.otrosProfesionales.get("cobrable_"+i));
			if(UtilidadTexto.getBoolean(this.otrosProfesionales.get("cobrable_"+i).toString()))
				this.asociosOrden.put("valor_"+pos,""); //pendiente de asignar
			else
				this.asociosOrden.put("valor_"+pos,"0");
			this.asociosOrden.put("codigoPool_"+pos,ConstantesBD.codigoNuncaValido);
			pos++;
		}
		
		this.asociosOrden.put("numRegistros",pos);
		
		//********************VALIDACIONES PARÁMETROS CONVENIO**************************************
		//Segpun indicador de cargo se verifica el parámetro
		if(this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia)||this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto))
		{
			//Se verifica el tipo de tarifa para la liquidacion de materiales
			if(mundoConvenio.getTipoTarifaLiqMateCx() != null && mundoConvenio.getTipoTarifaLiqMateCx().equals(ConstantesIntegridadDominio.acronimoTarifa))
				this.esTarifaLiquidacionMateriales = true;
			else
				this.esTarifaLiquidacionMateriales = false;
			
			//SE verifica si la duracion para rango de tiempos es por cirugia o por uso sala
			if(mundoConvenio.getTipoFechaLiqTiemPcx() != null && mundoConvenio.getTipoFechaLiqTiemPcx().equals(ConstantesIntegridadDominio.acronimoDuracionCirugia))
				this.porDuracionCirugia = true;
			else
				this.porDuracionCirugia = false;
			
			//Se verifica si se va arealizar liquidacion por rango de tiempos para todos los asocios
			if(this.mundoConvenio.getTipoLiquidacionGcx() != null && this.mundoConvenio.getTipoLiquidacionGcx().equals(ConstantesIntegridadDominio.acronimoRangoTiempo))
				this.debeLiquidarTodosXRangoTiempos = true;
			else
				this.debeLiquidarTodosXRangoTiempos = false;
			
			//Se verifica si se va a realizar liquidacion por rango de tiempos solo para salas
			if(this.mundoConvenio.getTipoLiquidacionScx() != null && this.mundoConvenio.getTipoLiquidacionScx().equals(ConstantesIntegridadDominio.acronimoRangoTiempo))
				this.debeLiquidarSalaXRangoTiempo = true;
			else
				this.debeLiquidarSalaXRangoTiempo = false;
		}
		else if(this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento)||this.indQx.equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto))
		{
			//Se verifica el tipo de tarifa para la liquidacion de materiales
			if(mundoConvenio.getTipoTarifaLiqMateDyt() != null && mundoConvenio.getTipoTarifaLiqMateDyt().equals(ConstantesIntegridadDominio.acronimoTarifa))
				this.esTarifaLiquidacionMateriales = true;
			else
				this.esTarifaLiquidacionMateriales = false;
			
			//SE verifica si la duracion para rango de tiempos es por cirugia o por uso sala
			if(mundoConvenio.getTipoFechaLiqTiempDyt() != null && mundoConvenio.getTipoFechaLiqTiempDyt().equals(ConstantesIntegridadDominio.acronimoDuracionCirugia))
				this.porDuracionCirugia = true;
			else
				this.porDuracionCirugia = false;
			
			//Se verifica si se va arealizar liquidacion por rango de tiempos para todos los asocios
			if(this.mundoConvenio.getTipoLiquidacionGdyt() != null  && this.mundoConvenio.getTipoLiquidacionGdyt().equals(ConstantesIntegridadDominio.acronimoRangoTiempo))
				this.debeLiquidarTodosXRangoTiempos = true;
			else
				this.debeLiquidarTodosXRangoTiempos = false;
			
			//Se verifica si se va a realizar liquidacion por rango de tiempos solo para salas
			if(this.mundoConvenio.getTipoLiquidacionDyt() != null &&  this.mundoConvenio.getTipoLiquidacionDyt().equals(ConstantesIntegridadDominio.acronimoRangoTiempo))
				this.debeLiquidarSalaXRangoTiempo = true;
			else
				this.debeLiquidarSalaXRangoTiempo = false;
		}
		//****************************************************************************************************
		
		
	}
	
	/**
	 * Método implementado para pasar los errores encontrados a un objeto ActionErrors
	 * @param errores
	 * @return
	 */
	public ActionErrors llenarMensajesError(ActionErrors errores)
	{
		for(ElementoApResource elemento:this.getMensajesError())
		{
			switch(elemento.getAtributosArrayList().size())
			{
				case 0:
					errores.add("",new ActionMessage(elemento.getLlave()));
				break;
				case 1:
					errores.add("",new ActionMessage(elemento.getLlave(),elemento.getAtributo(0)));
				break;
				case 2:
					errores.add("",new ActionMessage(elemento.getLlave(),elemento.getAtributo(0),elemento.getAtributo(1)));
				break;
					
			}
		}
		return errores;
	}
	
	/**
	 * Método implementado para aplicar las excepciones de los porcentajes en los asocios de cada cirugía
	 * 
	 *
	 */
	private void aplicarExcepcionesYPorcentajesAsocios()
	{
		boolean huboExcepcion = false, aplicarPorcentajes = false, puedoContinuar = true, liquidacionPorEspecialidad = false, excepcionQxNuevaTarifa = false;
		int codigoAsocio = ConstantesBD.codigoNuncaValido, tipoLiquidacion = ConstantesBD.codigoNuncaValido, codigoProfesional = ConstantesBD.codigoNuncaValido, codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		double valorAsocio = ConstantesBD.codigoNuncaValidoDouble, valorExcepcion = ConstantesBD.codigoNuncaValidoDouble;
		String tipoEspecialista = "", nombreAsocio = "", tipoServicio = "";
		
		logger.info("TIPO CIRUGIA NORMAL=> "+this.acronimoTipoCirugia);
		logger.info("TIPO CIRUGIA ESPECIALIDAD=> "+this.acronimoTipoCirugiaEspecialidad);
		
		/**logger.info("**********IMPRIMIR ASOCIOS CIRUGIA***************************");
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
			logger.info("Asocio["+i+"] "+this.asociosCirugia.get("nombreAsocio_"+i));
			logger.info("Liquidar X Especialidad["+i+"] "+this.asociosCirugia.get("liquidarPorEspecialidad_"+i));
		}
		logger.info("**********FIN IMPRIMIR ASOCIOS CIRUGIA***************************");**/
		
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
			//Solo se toman los asocio cobrables
			if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
			{
				huboExcepcion = false;
				aplicarPorcentajes = true;
				excepcionQxNuevaTarifa = false;
				codigoAsocio = Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString());
				codigoProfesional = Integer.parseInt(this.asociosCirugia.get("codigoProfesional_"+i).toString());
				codigoEspecialidad = Integer.parseInt(this.asociosCirugia.get("codigoEspecialidad_"+i).toString());
				nombreAsocio = this.asociosCirugia.get("nombreAsocio_"+i).toString();
				tipoEspecialista = this.asociosCirugia.get("tipoEspecialista_"+i).toString();
				liquidacionPorEspecialidad = UtilidadTexto.getBoolean(this.asociosCirugia.get("liquidarPorEspecialidad_"+i).toString());
				
				valorAsocio = Utilidades.convertirADouble(this.asociosCirugia.get("valor_"+i).toString(),true);
				
				logger.info("¿Se aplicarán porcentajes y excepciones por especialidad ? "+(liquidacionPorEspecialidad?"Sí":"No"));
				logger.info("*******************APLICACION EXCEPCIONES 1° PARTE "+nombreAsocio+"*****************************************"+valorAsocio);
				
				//1)***********VERIFICACIÓN DE EXCEPCIONES QX***************************
				HashMap<String, Object> mapaExcepcionQx = this.obtenerExcepcionQxAsocio(codigoAsocio, tipoEspecialista, liquidacionPorEspecialidad);
				
				if(Utilidades.convertirAEntero(mapaExcepcionQx.get("numRegistros")+"", true)>0)
				{
					//Según tipo de liquidacion se aplica un cálculo
					tipoLiquidacion = Integer.parseInt(mapaExcepcionQx.get("tipoLiquidacion_0").toString());
					valorExcepcion = Double.parseDouble(mapaExcepcionQx.get("valor_0").toString());
					aplicarPorcentajes = UtilidadTexto.getBoolean(mapaExcepcionQx.get("liquidarXPorcentaje_0").toString());
					switch(tipoLiquidacion)
					{
						case ConstantesBD.codigoTipoLiquidacionSoatPorcentaje:
							logger.info("Encontré escepcion Qx porcentaje ("+valorExcepcion+"), con el valor "+valorAsocio+"\n\n");
							//Dependiendo del modo de liquidación se aplica el porcentaje de distinta manera
							if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
							{
								if(UtilidadTexto.getBoolean(mapaExcepcionQx.get("liquidarSobreTarifa_0").toString()))
									valorAsocio = valorServicio * (valorExcepcion/100);
								else
									valorAsocio = valorAsocio * (valorExcepcion/100);
							}
							else
								valorAsocio = valorAsocio * (valorExcepcion/100);
							huboExcepcion = true;
							//aplicarPorcentajes = false;
						break;
						
						case ConstantesBD.codigoTipoLiquidacionSoatNuevaTarifa:
							logger.info("Encontré escepcion Qx nueva tarifs ("+valorExcepcion+"), con el valor "+valorAsocio+"\n\n");
							valorAsocio = valorExcepcion;
							huboExcepcion = true;
							excepcionQxNuevaTarifa = true;
							//aplicarPorcentajes = false;
						break;
						
						case ConstantesBD.codigoTipoLiquidacionSoatGrupo:
							logger.info("Encontré escepcion Qx nuevo grupo ("+valorExcepcion+"), con el valor "+valorAsocio+"\n\n");
							//Se consulta nuevo grupo
							Double aux = new Double(valorExcepcion);
							int nuevoGrupo = aux.intValue();
							HashMap nuevoValorGrupo = obtenerValorAsociosXGrupo(nuevoGrupo, codigoAsocio);
							if(Utilidades.convertirAEntero(nuevoValorGrupo.get("numRegistros")+"", true)>0)
							{
								valorAsocio = Double.parseDouble(nuevoValorGrupo.get("valor_0").toString());
								if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
									//Se toma el nuevo código de la parametrización
									this.asociosCirugia.put("codigoParametrizacion_"+i, nuevoValorGrupo.get("codigoParametrizacion_0").toString());
							}
							else
							{
								valorAsocio = 0;
								this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+nombreAsocio+" tiene excepción Qx. por nuevo grupo pero", "valor parametrizado para el grupo "+nuevoGrupo+" en la funcionalidad de Grupos");
							}
							huboExcepcion = true;
							
							//Si la liquidacion es mixta se cambia el modo de liquidacion del asocio a Soat
							if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
								this.asociosCirugia.put("modoLiquidacion_"+i, ConstantesBDSalas.codigoLiquidacionSoat);
						break;
						
						case ConstantesBD.codigoTipoLiquidacionSoatUvr:
							logger.info("Encontré escepcion Qx nuevo uvr ("+valorExcepcion+"), con el valor "+valorAsocio+"\n\n");
							int codigoOcupacion = Utilidades.obtenerOcupacionMedica(con, codigoProfesional);
							HashMap nuevoValorUvr = obtenerValorAsociosXUvr(valorExcepcion, codigoOcupacion, codigoEspecialidad, tipoEspecialista, codigoAsocio);
							if(Utilidades.convertirAEntero(nuevoValorUvr.get("numRegistros")+"",true)>0)
							{
								//hermorhu - MT6569
								if(Integer.parseInt(nuevoValorUvr.get("tipoLiquidacion_"+0).toString()) == ConstantesBD.codigoTipoLiquidacionSoatValor 
										&& Integer.parseInt(nuevoValorUvr.get("unidades_"+0).toString()) != ConstantesBD.codigoNuncaValido) {
									valorAsocio = valorExcepcion * valorServicio;
								} else {
									valorAsocio = calculoTarifaBaseAsocioXUvr(nuevoValorUvr, valorExcepcion, 0);
								}
							
								if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
									//Se toma el nuevo código de la parametrización
									this.asociosCirugia.put("codigoParametrizacion_"+i, nuevoValorUvr.get("codigoParametrizacion_0").toString());
								//Se toma el nuevo rango final (Sirve para verificar el consumo de materiales)
								this.asociosCirugia.put("rangoFinal_"+i, nuevoValorUvr.get("rangoFinal_0").toString());
								//Se toma el nuevo indicador para saber si es por Especialidad o por Cx Mayor
								this.asociosCirugia.put("liquidarPorEspecialidad_"+i, nuevoValorUvr.get("liquidarPor_0").toString().equals(ConstantesIntegridadDominio.acronimoPorEspecialidad)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
							}
							else
							{
								valorAsocio = 0;
								this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "El asocio "+nombreAsocio+" tiene excepción Qx. por nuevo UVR pero", "valor parametrizado para las UVR "+valorExcepcion+" en la funcionalidad de Asocios por UVR");
							}
							huboExcepcion = true;
							
							//Si la liquidacion es mixta se cambia el modo de liquidacion del asocio a ISS
							if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
								this.asociosCirugia.put("modoLiquidacion_"+i, ConstantesBDSalas.codigoLiquidacionIss);
								
						break;
					}
				}
				//***********************************************************************************
				//***********VERIFICACIÓN DE EXCEPCIONES TIPO SALA*******************************************
				//Solo aplican si no hubo excepcion y es un modo de liquidación diferente a Mixta
				if(!huboExcepcion&&this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
					valorAsocio = aplicarExcepcionTipoSala(i, valorAsocio);
				//***********************************************************************************************
				
				//Se almacena temporalmente si hubo excepcion y aplicación de porcentajes y valor
				this.asociosCirugia.put("huboExcepcion_"+i, huboExcepcion);
				this.asociosCirugia.put("aplicarPorcentajes_"+i, aplicarPorcentajes);
				this.asociosCirugia.put("excepcionQxNuevaTarifa_"+i, excepcionQxNuevaTarifa);
				//Si no había valor, se revisa si hubo excepcion antes de actualizarle el valor
				if(this.asociosCirugia.get("valor_"+i).toString().equals(""))
				{
					//Si hubo excepcion Qx o excepcion tipo Sala
					if(huboExcepcion||UtilidadTexto.getBoolean(this.asociosCirugia.get("huboExcepcionTipoSala_"+i)+""))
						this.asociosCirugia.put("valor_"+i, valorAsocio);
				}
				else
					this.asociosCirugia.put("valor_"+i, valorAsocio);
			}
		}
		
		//************FILTRO DE LA MÁXIMA CANTIDAD DE AYUDANTES QUE PAGA EL CONVENIO**********************
		int numeroAyudantes = 0;
		int numMaxAyudantes = Utilidades.convertirAEntero(this.mundoConvenio.getCantidadMaxAyudpag());
		//Cuando el parámetro no esté diligenciado se toma por defecto 1
		if(numMaxAyudantes==ConstantesBD.codigoNuncaValido)
			numMaxAyudantes = 1;
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
			if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString())&&
					Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())==Integer.parseInt(ValoresPorDefecto.getAsocioAyudantia(this.usuario.getCodigoInstitucionInt())))
			{
				numeroAyudantes++;
				
				if(numeroAyudantes>numMaxAyudantes)
					this.asociosCirugia.put("valor_"+i, "0");
			}
		}
		//*************************************************************************************************
		
		
		//************VALIDACION DEL VALOR DE CADA ASOCIO PARA LA LIQUIDACION MIXTA*************************
		if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
		{
			puedoContinuar = true;
			//Se verifica que todos los asocios tengan un valor
			for(int i=0;i<this.getNumAsociosCirugia();i++)
				if(UtilidadTexto.isEmpty(this.asociosCirugia.get("valor_"+i).toString()))
				{
					puedoContinuar = false;
					nombreAsocio = this.asociosCirugia.get("nombreAsocio_"+i).toString();
					tipoEspecialista = this.asociosCirugia.get("tipoEspecialista_"+i).toString();
					liquidacionPorEspecialidad = UtilidadTexto.getBoolean(this.asociosCirugia.get("liquidarPorEspecialidad_"+i).toString());
					
					if(!tipoEspecialista.equals(""))
						this.agregarErrorApResourceXCirugia("errors.noExiste2", "tarifa base para el asocio "+this.asociosCirugia.get("nombreAsocio_"+i)+" con tipo cirugía "+(liquidacionPorEspecialidad?nombreTipoCirugiaEspecialidad:nombreTipoCirugia).toLowerCase()+(!tipoEspecialista.equals("")?" y tipo especialista "+ValoresPorDefecto.getIntegridadDominio(tipoEspecialista).toString().toLowerCase():" y sin tipo especialista")+" ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")", "");
					else
						this.agregarErrorApResourceXCirugia("errors.notEspecific", "Para el asocio "+nombreAsocio+" aún no se ha definido el profesional de la salud.","");
					
				}
		}
		else
			puedoContinuar = true;
		//************************************************************************************************
		
		if(puedoContinuar)
		{
			
			//*****************APLICACION DE EXCEPCIONES FALTANTES***********************************************
			for(int i=0;i<this.getNumAsociosCirugia();i++)
			{
				
				//Solo se toman los asocio cobrables
				if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
				{	
					//Se vuelven a sacar los valores necesarios de cada iteración
					huboExcepcion = UtilidadTexto.getBoolean(this.asociosCirugia.get("huboExcepcion_"+i).toString());
					excepcionQxNuevaTarifa = UtilidadTexto.getBoolean(this.asociosCirugia.get("excepcionQxNuevaTarifa_"+i).toString());
					valorAsocio = Double.parseDouble(this.asociosCirugia.get("valor_"+i).toString());
					codigoAsocio = Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString());
					nombreAsocio = this.asociosCirugia.get("nombreAsocio_"+i).toString();
					liquidacionPorEspecialidad = UtilidadTexto.getBoolean(this.asociosCirugia.get("liquidarPorEspecialidad_"+i).toString());
					
					logger.info("*******************APLICACION EXCEPCIONES 2° PARTE "+nombreAsocio+"*****************************************"+valorAsocio);
					//2) ***********VERIFICACION DE EXCEPCIONES TARIFAS QX **************************************
					//Solo aplican si no hubo excepcion o si se aplicó excepcion Qx con nueva tarifa
					if((!huboExcepcion || excepcionQxNuevaTarifa)&&valorAsocio>0)
						valorAsocio = aplicarExcepcionTarifaQx(codigoAsocio,valorAsocio,liquidacionPorEspecialidad);
					//*********************************************************************************************
					
					//3) ***********VERIFICACIÓN DE EXCEPCIONES TIPO SALA*******************************************
					//Solo aplican si no hubo excepcion y es un modo de liquidación diferente a Mixta
					if(!huboExcepcion&&this.modoLiquidacion!=ConstantesBDSalas.codigoLiquidacionMixta)
						valorAsocio = aplicarExcepcionTipoSala(i, valorAsocio);
					//***********************************************************************************************
					
					this.asociosCirugia.put("valor_"+i, valorAsocio);
				}
			}
			//**********************************************************************************************
			
			///***************(INTERMEDIO) SE CALCULAN EL CONSUMO DE MATERIALES***********************
			this.calcularConsumoMateriales();
			//***************************************************************************************
			
			//*******************APLICACION DE PORCENTAJES********************************************************
			for(int i=0;i<this.getNumAsociosCirugia();i++)
			{
				
				//Solo se toman los asocio cobrables
				if(UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString()))
				{	
					//Se vuelven a sacar los valores necesarios de cada iteración
					aplicarPorcentajes = UtilidadTexto.getBoolean(this.asociosCirugia.get("aplicarPorcentajes_"+i).toString());
					valorAsocio = Double.parseDouble(this.asociosCirugia.get("valor_"+i).toString());
					codigoAsocio = Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString());
					nombreAsocio = this.asociosCirugia.get("nombreAsocio_"+i).toString();
					tipoEspecialista = this.asociosCirugia.get("tipoEspecialista_"+i).toString();
					tipoServicio = this.asociosCirugia.get("tipoServicio_"+i).toString();
					liquidacionPorEspecialidad = UtilidadTexto.getBoolean(this.asociosCirugia.get("liquidarPorEspecialidad_"+i).toString());
					
					logger.info("*******************APLICACION PORCENTAJES PARTE "+nombreAsocio+"*****************************************"+valorAsocio);
					//4) *********** APLICACIÓN DE PORCENTAJES CIRUGÍAS MULTIPLES************************************
					//Solo se aplican se se debe hacerlo
					if(aplicarPorcentajes)
					{
						HashMap<String, Object> mapaPorcentajes = obtenerPorcentajeCirugiaMultiple(codigoAsocio, tipoEspecialista,liquidacionPorEspecialidad);
						
						if(Utilidades.convertirAEntero(mapaPorcentajes.get("numRegistros")+"", true)>0)
						{
							double liquidacion = Double.parseDouble(mapaPorcentajes.get("liquidacion_0").toString());
							double adicional = Double.parseDouble(mapaPorcentajes.get("adicional_0").toString());
							double politraumatismo = Double.parseDouble(mapaPorcentajes.get("politraumatismo_0").toString());
							double valorBase = 0, valorAdicional = 0;
							
							logger.info("Encontró porcentaje Cx Multiple para a con %Liquidacion=> "+liquidacion+", %adicional=> "+adicional+", %politraumatismo=> "+politraumatismo);
							
							//Dependiendo del estado de politraumatismo de la orden se calcula el porcentaje
							if(!this.politraumatismo)
							{
								valorBase = valorAsocio * (liquidacion/100);
								//Solo aplica si el servicio es bilateral
								if(esBilateral)
									valorAdicional = valorBase * (adicional/100);
								valorAsocio = valorBase + valorAdicional;
							}
							else
							{
								valorBase = valorAsocio * (politraumatismo/100);
								//Solo aplica si el servicio es bilateral
								if(esBilateral)
									valorAdicional = valorBase * (adicional/100);
								valorAsocio = valorBase + valorAdicional;
							}
						}
						else
						{
							if(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")&&tipoEspecialista.equals(""))
								this.agregarErrorApResourceXCirugia("errors.notEspecific", "Para el asocio "+nombreAsocio+" aún no se ha definido el profesional de la salud.","");
							else
								this.agregarErrorApResourceXCirugia(
									"errors.noExiste2", 
									"parametrización de porcentajes cirugías múltiples para "+nombreAsocio+" con tipo cirugía "+(liquidacionPorEspecialidad?nombreTipoCirugiaEspecialidad:nombreTipoCirugia).toLowerCase()+" y tipo especialista "+ValoresPorDefecto.getIntegridadDominio(tipoEspecialista).toString().toLowerCase()+" ("+this.infoCobertura.getDtoSubCuenta().getConvenio().getNombre().toLowerCase()+")", "");
								
								
						}
					}
					//************************************************************************************************
					this.asociosCirugia.put("valor_"+i,valorAsocio);
					
					logger.info("**********VALOR FINAL ASOCIO "+nombreAsocio+"****************"+valorAsocio);
					
				} //Fin if pregunta de asocio cobrable
			}
			//************************************************************************************************
		}
	}
	
	/**
	 * Método para aplicar la excepcion tarifa Qx
	 * @param codigoAsocio
	 * @param valorAsocio
	 * @param liquidacionPorEspecialidad 
	 * @return
	 */
	private double aplicarExcepcionTarifaQx(int codigoAsocio, double valorAsocio, boolean liquidacionPorEspecialidad) 
	{
		HashMap<String, Object> mapaExcepcionTarifa = obtenerExcepcionTarifaQxAsocio(this.grupoUvrServicio, codigoAsocio,liquidacionPorEspecialidad);
		
		if(Utilidades.convertirAEntero(mapaExcepcionTarifa.get("numRegistros")+"", true)>0)
		{
			//Se toma tipo incremento
			String tipoIncremento = mapaExcepcionTarifa.get("tipoExcepcion_0").toString(); 
			String porcentaje = mapaExcepcionTarifa.get("porcentaje_0").toString();
			String valor = mapaExcepcionTarifa.get("valor_0").toString();
			double valorBase = 0;
			double incremento = tipoIncremento.equals(ConstantesIntegridadDominio.acronimoIncremento)?1:-1;
			
			if(!porcentaje.equals(""))
			{
				logger.info("Encontró excepcion tarifa qx con % "+porcentaje+" en "+(tipoIncremento.equals(ConstantesIntegridadDominio.acronimoIncremento)?"incremento":"decremento"));
				valorBase = (valorAsocio*(Double.parseDouble(porcentaje.replace(",", "."))/100));
				logger.info("valor base a incrementar: "+valorBase);
				logger.info("valor asocio antes de incrementar: "+valorAsocio);
				valorAsocio =  valorAsocio + (incremento*valorBase);
			}
			else if(!valor.equals(""))
			{
				logger.info("Encontró excepcion tarifa qx con valor "+valor+" en "+(tipoIncremento.equals(ConstantesIntegridadDominio.acronimoIncremento)?"incremento":"decremento"));
				valorAsocio += (incremento*Double.parseDouble(valor));
			}
		}
		return valorAsocio;
	}
	
	/**
	 * Método para aplicar las excepciones tipo sala
	 * @param codigoAsocio
	 * @param valorAsocio
	 * @return
	 */
	private double aplicarExcepcionTipoSala(int posAsocio,double valorAsocio)
	{
		int tipoLiquidacion = ConstantesBD.codigoNuncaValido;
		double valorExcepcion = ConstantesBD.codigoNuncaValidoDouble;
		boolean huboExcepcion = false;
		int codigoAsocio = Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+posAsocio).toString());
		boolean liquidacionPorEspecialidad = UtilidadTexto.getBoolean(this.asociosCirugia.get("liquidarPorEspecialidad_"+posAsocio).toString());
		HashMap<String, Object> mapaExcepcionTipoSala = obtenerExcepcionXTipoSala(codigoAsocio,liquidacionPorEspecialidad);
		
		if(Utilidades.convertirAEntero(mapaExcepcionTipoSala.get("numRegistros")+"", true)>0)
		{
			tipoLiquidacion = Integer.parseInt(mapaExcepcionTipoSala.get("tipoLiquidacion_0").toString());
			valorExcepcion = Double.parseDouble(mapaExcepcionTipoSala.get("cantidad_0").toString());
			
			switch(tipoLiquidacion)
			{
				case ConstantesBD.codigoTipoLiquidacionSoatUnidades:
					logger.info("Encontró excepcion tipo sala por unidades: "+valorExcepcion);
					//Se calcula el valor base
					valorExcepcion = valorExcepcion * mundoEsquemaTarifario.getCantidad();
					valorAsocio = valorExcepcion;
					//como es nuevo valor se vuelve a verificar si tiene excepciones tarifas quirurgicas (SOLO APLICA PARA LIQUIDACIONES DIFERENTES A MIXTA)
					if(this.modoLiquidacion!=ConstantesBDSalas.codigoLiquidacionMixta)
						valorAsocio = aplicarExcepcionTarifaQx(codigoAsocio, valorAsocio,liquidacionPorEspecialidad);
						
					huboExcepcion = true;
				break;
				case ConstantesBD.codigoTipoLiquidacionSoatValor:
					logger.info("Encontró excepcion tipo sala por valors : "+valorExcepcion);
					valorAsocio = valorExcepcion;
					//como es nuevo valor se vuelve a verificar si tiene excepciones tarifas quirurgicas (SOLO APLICA PARA LIQUIDACIONES DIFERENTES A MIXTA)
					if(this.modoLiquidacion!=ConstantesBDSalas.codigoLiquidacionMixta)
						valorAsocio = aplicarExcepcionTarifaQx(codigoAsocio, valorAsocio,liquidacionPorEspecialidad);
					huboExcepcion = true;
				break;
				case ConstantesBD.codigoTipoLiquidacionSoatPorcentaje:
					logger.info("Encontró excepcion tipo sala por porcentaje : "+valorExcepcion);
					//Si el modo de liquidación es mixta el cálculo del porcentaje debe hacer una validación adicional
					if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
					{
						if(UtilidadTexto.getBoolean(mapaExcepcionTipoSala.get("liquidarSobreTarifa_0").toString()))
							valorAsocio = this.valorServicio * (valorExcepcion/100);
						else
							valorAsocio = valorAsocio * (valorExcepcion/100); 
					}
					else
						valorAsocio = valorAsocio * (valorExcepcion/100);
					huboExcepcion = true;
				break;
			}
		}
		
		this.asociosCirugia.put("huboExcepcionTipoSala_"+posAsocio,huboExcepcion);
		
		return valorAsocio;
	}
	
	
	/**
	 * Método para obtener el tipo de servicio de cada asocio
	 *
	 */
	private void consultarTipoServicioAsocios() throws IPSException
	{
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
			//Se consulta el tipo de servicio del asocio
			this.asociosCirugia.put("tipoServicio_"+i, UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, Integer.parseInt(this.asociosCirugia.get("codigoAsocio_"+i).toString())));
			
			//Si el tipo de servicio es salas o materiales entonces se agrega como tipo Especialista el tipo Especialista del cirujano
			if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")||
					this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioSalaCirugia+""))
				this.asociosCirugia.put("tipoEspecialista_"+i,tipoEspecialistaCirujano);
		}
		
		
	}
	
	/**
	 * Método para obtener el codigo propietario de cada asocio
	 *
	 */
	private void consultarCodigoPropietarioAsocios()
	{
		String codigoParametrizacion = "", tipoServicio = "";
		
		for(int i=0;i<this.getNumAsociosCirugia();i++)
		{
			//Adicionalmente se consulta el codigo propietario de la parametrización (Si existe)
			codigoParametrizacion = this.asociosCirugia.get("codigoParametrizacion_"+i)+"";
			//Se toma el tipo de servicio del asocio
			tipoServicio = this.asociosCirugia.get("tipoServicio_"+i).toString();
			
			if(Utilidades.convertirAEntero(codigoParametrizacion, true)>0)
			{
				//1) Si el asocio es de sala y se debía liquidar por tiempos entonces se busca su código propietario en la parametrización de tiempos
				if(tipoServicio.equals(ConstantesBD.codigoServicioSalaCirugia+"")&&this.debeLiquidarSalaXRangoTiempo)
					this.asociosCirugia.put("codigoPropietario_"+i, liquidacionDao.obtenerCodigoPropietarioAsocio(this.con,codigoParametrizacion , this.tarifarioOficialGeneral, ConstantesBDSalas.codigoLiquidacionRangoTiempos));
				//2) Si el modo liquidación es Mixta se debe consultar por el modo de liquidación de cada asocio
				else if(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta)
					this.asociosCirugia.put("codigoPropietario_"+i, liquidacionDao.obtenerCodigoPropietarioAsocio(this.con,codigoParametrizacion , this.tarifarioOficialGeneral, Integer.parseInt(this.asociosCirugia.get("modoLiquidacion_"+i).toString())));
				//3) Por último se toma el modo de liquidacion principal
				else
					this.asociosCirugia.put("codigoPropietario_"+i, liquidacionDao.obtenerCodigoPropietarioAsocio(this.con,codigoParametrizacion , this.tarifarioOficialGeneral, this.modoLiquidacion));
			}
			else
				this.asociosCirugia.put("codigoPropietario_"+i,"");
		}
	}
	
	/**
	 * Método implementado para realizar el cálculo del consumo de materiales
	 *
	 */
	private void calcularConsumoMateriales()
	{
		double valorConsumo = 0;
		
		//Si es la cirugía #1 se verifica si es por consumo
		if(this.numeroCirugia==1)
		{
			logger.info("VERIFICACION CONSUMO DE MATERIALES:");
			//Se verifica si hay algun asocio Materiales con el valor en 0
			for(int i=0;i<this.getNumAsociosCirugia();i++)
				if(
					UtilidadTexto.getBoolean(this.asociosCirugia.get("cobrable_"+i).toString())&&
					this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&
					(
					//Verificación para liquidacion Soat
					(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionSoat&&Double.parseDouble(this.asociosCirugia.get("valor_"+i).toString())==0)||
					//Verificación para liquidación ISS
					(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionIss&&Utilidades.convertirAEntero(this.asociosCirugia.get("rangoFinal_"+i)+"",true)==999999)||
					//Verificación para liquidacion Rango Tiempos
					(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionRangoTiempos&&Utilidades.convertirAEntero(this.asociosCirugia.get("rangoFinal_"+i)+"",true)==999999)||
					//Verificación para liquidación MIXTA
					(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta&&esPorConsumoMaterialesXMixta(i))
					)
				)
				{
					this.debeLiquidarConsumoMateriales = true;
					this.esCirugiaPorConsumo = true;
				}
			
			logger.info("VERIFICACION CONSUMO DE MATERIALES: ¿deboLiquidarConsumoMateriales? "+this.debeLiquidarConsumoMateriales+" ¿Es cirugía por consumo? "+this.esCirugiaPorConsumo);
			
			//Si no se debe liquidar consumo de materiales entonces se liquida materiales especiales
			if(!debeLiquidarConsumoMateriales)
				this.debeLiquidarMaterialesEspeciales = true;
		}
		
		//Si se debe liquidar Materiales x consumo se verifica si es por Acto o or cirugia
		if(debeLiquidarConsumoMateriales)
		{
			//***********************CONSUMO POR ACTO*******************************************************************
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())))
			{
				//Si los materiales son por acto se verifica si es la cirugía #1 para calcularlos
				if(this.numeroCirugia==1)
				{
					//Se verifica si existe consumo de materiales por orden
					if(liquidacionDao.existeConsumoMateriales(this.con, this.numeroSolicitud, ConstantesBD.codigoNuncaValido))
					{
						valorConsumo = liquidacionDao.obtenerTotalConsumoMateriales(con, this.numeroSolicitud, ConstantesBD.codigoNuncaValido, this.esTarifaLiquidacionMateriales);
						
						//Se asigna el valor del consumo al primer asocio de materiales encontrado
						int posAsignada = ConstantesBD.codigoNuncaValido;
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+""))
							{
								this.asociosCirugia.put("valor_"+i, valorConsumo);
								this.asociosCirugia.put("aplicarPorcentajes_"+i, ConstantesBD.acronimoNo); //no se aplican porcentajes
								posAsignada = i;
								i = this.getNumAsociosCirugia(); 
							}
						
						//Para el resto se asigna 0
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&posAsignada!=i)
							{
								this.asociosCirugia.put("valor_"+i, "0");
								this.asociosCirugia.put("aplicarPorcentajes_"+i, ConstantesBD.acronimoNo); //no se aplican porcentajes
							}
							
						
					}
					else
						this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "cirugía parametrizada por consumo de materiales pero", "registro de consumo de materiales por acto. Por favor verifique");
					
				}
				//De lo contrario se ponen los valores del asocio de materiales en 0 y no se ponen como cobrables
				else
				{
					for(int i=0;i<this.getNumAsociosCirugia();i++)
						if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+""))
						{
							this.asociosCirugia.put("valor_"+i, "0");
							this.asociosCirugia.put("cobrable_"+i, ConstantesBD.acronimoNo);
							this.asociosCirugia.put("aplicarPorcentajes_"+i, ConstantesBD.acronimoNo); //no se aplican porcentajes
							
						}
				}
			}
			//**************************************************************************************************************
			//***********************CONSUMO POR SERVICIO********************************************************************
			else
			{
				//Se verifica si hay algun asocio Materiales con el valor en 0
				for(int i=0;i<this.getNumAsociosCirugia();i++)
					if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&
						(
						//Verificacion para la liquidacion Soat
						(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionSoat&&Double.parseDouble(this.asociosCirugia.get("valor_"+i).toString())==0)||
						//Verificación para liquidación ISS
						(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionIss&&Utilidades.convertirAEntero(this.asociosCirugia.get("rangoFinal_"+i)+"",true)==999999)||
						//Verificación para liquidacion Rango Tiempos
						(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionRangoTiempos&&Utilidades.convertirAEntero(this.asociosCirugia.get("rangoFinal_"+i)+"",true)==999999)||
						//Verificación para liquidación MIXTA
						(this.modoLiquidacion==ConstantesBDSalas.codigoLiquidacionMixta&&esPorConsumoMaterialesXMixta(i))
						)
					)
						this.esCirugiaPorConsumo = true;
				
				//Si la cirugía es por consumo
				if(this.esCirugiaPorConsumo)
				{
					
					//Se verifica si existe consumo de materiales por el servicio
					if(liquidacionDao.existeConsumoMateriales(this.con, this.numeroSolicitud, this.codigoServicio))
					{
						valorConsumo = liquidacionDao.obtenerTotalConsumoMateriales(con, this.numeroSolicitud, this.codigoServicio, this.esTarifaLiquidacionMateriales);
						
						//Se asigna el valor del consumo al primer asocio de materiales encontrado
						int posAsignada = ConstantesBD.codigoNuncaValido;
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+""))
							{
								this.asociosCirugia.put("valor_"+i, valorConsumo);
								this.asociosCirugia.put("aplicarPorcentajes_"+i, ConstantesBD.acronimoNo); //no se aplican porcentajes
								
								posAsignada = i;
								i = this.getNumAsociosCirugia(); 
							}
						
						//Para el resto se asigna 0
						for(int i=0;i<this.getNumAsociosCirugia();i++)
							if(this.asociosCirugia.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioMaterialesCirugia+"")&&posAsignada!=i)
							{
								this.asociosCirugia.put("valor_"+i, "0");
								this.asociosCirugia.put("aplicarPorcentajes_"+i, ConstantesBD.acronimoNo); //no se aplican porcentajes
							}
							
						
					}
					else
						this.agregarErrorApResourceXCirugia("error.salasCirugia.noTiene", "cirugía parametrizada por consumo de materiales pero", "registro de consumo de materiales. Por favor verifique");
				}
			}
			//****************************************************************************************************************
		}
	}
	
	/**
	 * Método que valida se un asocio de materiales tiene definición de consumo de materiales
	 * @param posAsocio
	 * @return
	 */
	private boolean esPorConsumoMaterialesXMixta(int posAsocio)
	{
		boolean esPorConsumo = false;
		//modo liquidacion del asocio
		int modoLiquidacionAsocio = Integer.parseInt(this.asociosCirugia.get("modoLiquidacion_"+posAsocio).toString());
		
		switch(modoLiquidacionAsocio)
		{
			//MODO LIQUIDACION SOAT
			case ConstantesBDSalas.codigoLiquidacionSoat:
				if(Double.parseDouble(this.asociosCirugia.get("valor_"+posAsocio).toString())==0)
					esPorConsumo = true;
			break;
			//MODO LIQUIDACION ISS O RANGO TIEMPOS
			case ConstantesBDSalas.codigoLiquidacionIss:
			case ConstantesBDSalas.codigoLiquidacionRangoTiempos:
				if(Utilidades.convertirAEntero(this.asociosCirugia.get("rangoFinal_"+posAsocio)+"",true)==999999)
					esPorConsumo = true;
			break;
		}
		
		return esPorConsumo;
	}
	
	/**
	 * Método implementado para insertar un asocio
	 * @param cobrable 
	 * @param con
	 * @return
	 */
	private int insertarAsocio(int codigoEspecialidad, int codigoProfesional,int codigoServicio, int codigoPool,int codigoAsocio,String codigoPropietario,double valorAsocio,String tipoServicio, String cobrable, int centroCostoEjecuta, int medico)
	{
		HashMap campos = new HashMap();
		
		campos.put("consecutivoServicio", this.consecutivoServicio);
		campos.put("codigoEspecialidad", codigoEspecialidad);
		campos.put("codigoInstitucion", this.usuario.getCodigoInstitucion());
		campos.put("codigoProfesional", codigoProfesional);
		campos.put("codigoServicio", codigoServicio);
		campos.put("codigoPool", codigoPool);
		campos.put("codigoAsocio", codigoAsocio);
		campos.put("codigoPropietario", codigoPropietario);
		campos.put("valorAsocio", valorAsocio);
		campos.put("tipoServicio", tipoServicio);
		campos.put("cobrable", cobrable);
		/***ANEXO 777****/
		campos.put("medico", medico);
		campos.put("centroCostoEjecuta", centroCostoEjecuta);
		/****************/
		return liquidacionDao.insertarAsocio(this.con, campos);
	}
	
	/**
	 * Método para realizar el registro de los materiales especiales
	 *
	 */
	private void registrarMaterialesEspeciales() throws IPSException
	{
		double valorUnitario = ConstantesBD.codigoNuncaValidoDouble, costoUnitario = ConstantesBD.codigoNuncaValidoDouble;
		boolean exito = false;
		
		//Si se debe liquidar materiales especiales, se hace el cargo de los materiales especiales
		if(this.debeLiquidarMaterialesEspeciales&&this.mensajesError.size()==0)
		{
			Cargos mundoCargos = new Cargos();
			//Se consultan los materiales especiales
			HashMap<String, Object> materialesEspeciales = liquidacionDao.consultarMaterialesEspeciales(this.con, this.numeroSolicitud);
			
			for(int i=0;i<Integer.parseInt(materialesEspeciales.get("numRegistros").toString());i++)
			{
				valorUnitario = Double.parseDouble(materialesEspeciales.get("valorUnitario_"+i).toString());
				costoUnitario = Double.parseDouble(materialesEspeciales.get("costoUnitario_"+i).toString());
				exito = false;
				
				
				if(valorUnitario!=ConstantesBD.codigoNuncaValidoDoubleNegativo||costoUnitario!=ConstantesBD.codigoNuncaValidoDoubleNegativo)
				{
				
					exito = mundoCargos.generarSolicitudSubCuentaCargoMaterialesEspeciales(
						this.con, 
						this.usuario, 
						Integer.parseInt(this.numeroSolicitud), 
						Integer.parseInt(materialesEspeciales.get("codigoArticulo_"+i).toString()), 
						Integer.parseInt(materialesEspeciales.get("cantidad_"+i).toString()), 
						Integer.parseInt(this.idCuenta), 
						this.codigoCentroCostoEjecuta, 
						this.esTarifaLiquidacionMateriales?valorUnitario:costoUnitario, 
						this.codigoViaIngreso, 
						this.infoCobertura,this.datosActoQx.get("fechaFinalCx").toString());
					
					if(!exito)
							this.agregarErrorApResource("errors.problemasGenericos", "al generar el cargo del material especial "+materialesEspeciales.get("nombreArticulo_"+i), "");
				}
			}
			
		}
	}
	
	/**
	 * Método para realizar el cálculo de la tarifa base del asocio x rango de tiempo
	 * @param asociosRangoTiempo
	 * @param duracion
	 * @param posAsocio
	 * @return
	 */
	private double calculoTarifaBaseAsocioXRangoTiempo(HashMap<String, Object> asociosRangoTiempo,int duracion, int posAsocio)
	{
		double valorAsocio = 0, valorBaseDecimal = 0;
		int diferenciaDuracion = 0, valorBaseEntera = 0;
		int minutosFraccionAdicional = Integer.parseInt(asociosRangoTiempo.get("minFracAdicional_"+posAsocio).toString());
		int minutosRangoFinal = Integer.parseInt(asociosRangoTiempo.get("minutosRangoFinal_"+posAsocio).toString());
		double valorFraccionAdicional = Double.parseDouble(asociosRangoTiempo.get("valorFracAdicional_"+posAsocio).toString());
		double valorRango = Double.parseDouble(asociosRangoTiempo.get("valor_"+posAsocio).toString());
		
		logger.info("*****************CALCULO VALOR RANGO TIEMPO ASOCIO : "+asociosRangoTiempo.get("nombreAsocio_"+posAsocio)+"**************");
		logger.info("minutosFraccionAdicional: "+minutosFraccionAdicional);
		logger.info("duracion: "+duracion);
		logger.info("minutosRangoFinal: "+minutosRangoFinal);
		
		//Cálculos del valor x fracción adicional, sólo si la duración de la cirugía es mayor a los minutos del rango final
		if(minutosFraccionAdicional>0&&duracion>minutosRangoFinal)
		{
			diferenciaDuracion = duracion - minutosRangoFinal;
			logger.info("Liquidacion de tiempos por fraccion adicional? "+UtilidadTexto.getBoolean(mundoConvenio.getLiquidacionTmpFracAdd()));
			//Se pregunta si la liquidación de tiempos se hace por fracción adicional
			if(UtilidadTexto.getBoolean(mundoConvenio.getLiquidacionTmpFracAdd()))
			{
				if(diferenciaDuracion<minutosFraccionAdicional)
					valorAsocio = valorRango;
				else
				{
					valorBaseEntera = diferenciaDuracion / minutosFraccionAdicional;
					valorFraccionAdicional = valorFraccionAdicional * valorBaseEntera;
					valorAsocio = valorRango + valorFraccionAdicional;
				}
			}
			else
			{
				
				valorBaseDecimal = ((double)diferenciaDuracion) / ((double)minutosFraccionAdicional);
				valorFraccionAdicional = valorFraccionAdicional * Math.ceil(valorBaseDecimal);
				valorAsocio = valorRango + valorFraccionAdicional;
				
				
			}
			
			
			logger.info("diferenciaDuracion: "+diferenciaDuracion);
			logger.info("valorBaseDecimal: "+valorBaseDecimal);
			logger.info("valorFraccionAdicional: "+valorFraccionAdicional);
			
			
		}
		else
			valorAsocio = valorRango;
		
		logger.info("valorRango: "+valorRango);
		logger.info("valorAsocio: "+valorAsocio+"\n");
		
		return valorAsocio;
	}
	
	/**
	 * Método que calcula la tarifa base del asocio x uvr
	 * @param asociosUvr
	 * @param uvr
	 * @param posAsocio
	 * @return
	 */
	private double calculoTarifaBaseAsocioXUvr(HashMap<String, Object> asociosUvr,double uvr,int posAsocio)
	{
		double valorAsocio = 0;
		int tipoLiquidacion = Integer.parseInt(asociosUvr.get("tipoLiquidacion_"+posAsocio).toString());
		double valor =  Double.parseDouble(asociosUvr.get("valor_"+posAsocio).toString());
		int unidades = Integer.parseInt(asociosUvr.get("unidades_"+posAsocio).toString());
		
		switch(tipoLiquidacion)
		{
			//TIPO LIQUIDACION POR NÚMERO DE UNIDADES
			case ConstantesBD.codigoTipoLiquidacionSoatUnidades:
				valorAsocio = valor;
			break;
			case ConstantesBD.codigoTipoLiquidacionSoatValor:
				if(unidades!=ConstantesBD.codigoNuncaValido)
					//TIPO LIQUIDACION POR VALOR DE UNIDADES
					valorAsocio = uvr * unidades;
				else
					//TIPO LIQUDIACION POR VALOR
					valorAsocio = valor;
			break;
			default:
				this.agregarErrorApResourceXCirugia("error.salasCirugia.tiene", "el asocio "+asociosUvr.get("nombreAsocio_"+posAsocio), "parametrización de tarifa errónea para las uvr's "+uvr);
			break;
		}
		
		return valorAsocio;
	}
	
	/**
	 * Método para consultar los asocios liquidados
	 * @param con
	 * @param consecutivoServicio
	 * @return
	 */
	public static HashMap<String, Object> consultarAsociosLiquidados(Connection con,String consecutivoServicio)
	{
		return liquidacionDao().consultarAsociosLiquidados(con, consecutivoServicio);
	}
	
	/**
	 * Método para actualizar el indicador de consumo de materiales en la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param indicador
	 * @return
	 */
	public static int actualizarIndicadorConsumoMaterialesSolicitud(Connection con,String numeroSolicitud,boolean indicador)
	{
		return liquidacionDao().actualizarIndicadorConsumoMaterialesSolicitud(con, numeroSolicitud, indicador);
	}
	//*******************************************************************************************************************************
	//************************FIN - METODOS PROCESO LIQUIDACION****************************************************************************
	//*******************************************************************************************************************************
	//**********************GETTERS & SETTERS**********************************************************************************
	//**************************************************************************************************************************
	
	
	
	/**
	 * Método que retorna el número de otros profesionales
	 */
	public int getNumOtrosProfesionales()
	{
		return Utilidades.convertirAEntero(this.otrosProfesionales.get("numRegistros")+"", true);
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
	 * @return the cirugias
	 */
	public HashMap<String, Object> getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias the cirugias to set
	 */
	public void setCirugias(HashMap<String, Object> cirugias) {
		this.cirugias = cirugias;
	}
	
	/**
	 * Número de cirugias
	 * @return
	 */
	public int getNumCirugias()
	{
		return Utilidades.convertirAEntero(this.cirugias.get("numRegistros")+"", true);
	}
	
	/**
	 * Número de cirugias
	 * @return
	 */
	public int getNumCirugiasOrdenadas()
	{
		return Utilidades.convertirAEntero(this.cirugiasOrdenadas.get("numRegistros")+"", true);
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}
	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}
	/**
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}
	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}
	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the infoCobertura
	 */
	public InfoResponsableCobertura getInfoCobertura() {
		return infoCobertura;
	}
	/**
	 * @param infoCobertura the infoCobertura to set
	 */
	public void setInfoCobertura(InfoResponsableCobertura infoCobertura) {
		this.infoCobertura = infoCobertura;
	}
	/**
	 * @return the mensajesError
	 */
	public ArrayList<ElementoApResource> getMensajesError() {
		return mensajesError;
	}
	/**
	 * @param mensajesError the mensajesError to set
	 */
	public void setMensajesError(ArrayList<ElementoApResource> mensajesError) {
		this.mensajesError = mensajesError;
	}
	/**
	 * @return the tarifarioOficialGeneral
	 */
	public int getTarifarioOficialGeneral() {
		return tarifarioOficialGeneral;
	}
	/**
	 * @param tarifarioOficialGeneral the tarifarioOficialGeneral to set
	 */
	public void setTarifarioOficialGeneral(int tarifarioOficialGeneral) {
		this.tarifarioOficialGeneral = tarifarioOficialGeneral;
	}
	/**
	 * @return the con
	 */
	public Connection getCon() {
		return con;
	}
	/**
	 * @param con the con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
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
	 * @return the codigoCirujano
	 */
	public int getCodigoCirujano() {
		return codigoCirujano;
	}
	/**
	 * @param codigoCirujano the codigoCirujano to set
	 */
	public void setCodigoCirujano(int codigoCirujano) {
		this.codigoCirujano = codigoCirujano;
	}
	/**
	 * @return the nombreCirujano
	 */
	public String getNombreCirujano() {
		return nombreCirujano;
	}
	/**
	 * @param nombreCirujano the nombreCirujano to set
	 */
	public void setNombreCirujano(String nombreCirujano) {
		this.nombreCirujano = nombreCirujano;
	}
	/**
	 * @return the datosActoQx
	 */
	public HashMap<String, Object> getDatosActoQx() {
		return datosActoQx;
	}
	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(HashMap<String, Object> datosActoQx) {
		this.datosActoQx = datosActoQx;
	}
	/**
	 * @return the encabezadoOrden
	 */
	public HashMap<String, Object> getEncabezadoOrden() {
		return encabezadoOrden;
	}
	/**
	 * @param encabezadoOrden the encabezadoOrden to set
	 */
	public void setEncabezadoOrden(HashMap<String, Object> encabezadoOrden) {
		this.encabezadoOrden = encabezadoOrden;
	}
	/**
	 * @return the otrosProfesionales
	 */
	public HashMap<String, Object> getOtrosProfesionales() {
		return otrosProfesionales;
	}
	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(HashMap<String, Object> otrosProfesionales) {
		this.otrosProfesionales = otrosProfesionales;
	}
	/**
	 * @return the indQx
	 */
	public String getIndQx() {
		return indQx;
	}
	/**
	 * @param indQx the indQx to set
	 */
	public void setIndQx(String indQx) {
		this.indQx = indQx;
	}
	/**
	 * @return the numeroPeticion
	 */
	public String getNumeroPeticion() {
		return numeroPeticion;
	}
	/**
	 * @param numeroPeticion the numeroPeticion to set
	 */
	public void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	
	/**
	 * Método que retorna el tamaño de los asocios de la orden
	 * @return
	 */
	private int getNumAsociosOrden()
	{
		return Utilidades.convertirAEntero(this.asociosOrden.get("numRegistros")+"", true);
	}
	
	/**
	 * Método que retorna el tamaño de los asocios de una cirugia
	 * @return
	 */
	private int getNumAsociosCirugia()
	{
		return Utilidades.convertirAEntero(this.asociosCirugia.get("numRegistros")+"", true);
	}
	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}
	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
}
