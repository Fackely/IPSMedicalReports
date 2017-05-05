/*
 * Creado en 17/08/2004
 *
 */
package util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ValoresPorDefectoDao;
import com.princetonsa.dto.administracion.DtoAreaAperturaCuentaAutoPYP;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.servinte.axioma.cron.AdministradorCron;

/**
 * @author Juan David Ram&iacute;rez L&oacute;pez
 * 
 * Princeton S.A.
 */
@SuppressWarnings("unchecked")
public class ValoresPorDefecto {
	
	/**
	 * Interacci&oacute;n con la fuente de datos
	 */
	private static ValoresPorDefectoDao valoresPorDefectoDao;

	/**
	 * Mapa para manejar los valores por defecto definidos en la funcionalidad
	 * de par&aacute;metros generales del modulo Manejo Paciente Direcci&oacute;n por defecto
	 * en el ingreso del paciente
	 * 
	 * C&oacute;digo del estado de la cama en el que queda despu&eacute;s de hacer un egreso
	 * 
	 * Las solicitudes de farmacia no tienen un servicio, por esta raz&oacute;n se debe
	 * tener un c&oacute;digo de servicio parametrizado para estandarizar los listados
	 * de solicitudes Valor que se tomar&oacute; como c&oacute;digo de servicio al momento de
	 * listar todas las solicitudes de farmacia
	 * 
	 * Par&aacute;metro que indica el tipo de validaci&oacute;n del estado m&eacute;dico de las
	 * solicitudes para dar egreso (Sin importar el tipo de egreso) Si el
	 * par&aacute;metro se encuentra en "SI" quiere decir que se deve validar el estado
	 * "Interpretada" Si el par&aacute;metro se encuentra en "NO" quiere decir que se
	 * debe validar el estado "Interpretada" o "Respondida"
	 * 
	 * Par&aacute;metro que indica si se debe validar en facturaci&oacute;n el hecho que los
	 * contratos est&aacute;n vencidos
	 * 
	 * El String "key" del mapa se encuentra definido en ConstantesBD con los
	 * nombres nombreValoresDefectoXxxxx en adici&oacute;n al c&oacute;digo de la instituci&oacute;n
	 * para la cual se parametriz&oacute; este par&aacute;metro. Ej.
	 * ContantesBD.nombreValoresDefectoXxxxx+"_"+codigoInstitucion
	 */
	private static HashMap<String, Object> parametrosGenerales;

	/**
	 * C&oacute;digo del tipo cie actual
	 */
	private static int codigoTipoCieActual;

	/**
	 * Variable para manejar el directorio base de axioma
	 */
	private static String directorioAxiomaBase;

	/**
	 * Par&aacute;metro definido en el web.xml, en el cual se indica si se deben
	 * mostrar o no los nombres de los JSP
	 */
	private static boolean mostrarNombreJSP;

	
	/**
	 * Par&aacute;metro definido en el web.xml, en el cual se indica si la navegaci&oacute;n
	 * de la aplicaci&oacute;n se hace a trav&eacute;s de un popUp
	 */
	private static boolean popUpNavegacion;

	
	/**
	 * Al tener problemas con el System.getProperty() teniendo varios contextos,
	 * se decidi&oacute; incluir en esta clase las propiedades del sistema Una de ellas
	 * es filePath (Definido en el web.xml), este es una ruta en la cual quedan
	 * los archivos que se adjuntan a la historia cl&iacute;nica. Ej.
	 * /var/tomcat5/webapps/axioma/upload
	 */
	private static String filePath;
	

	
	/**
	 * parametro para manejar el file path de reportes de archivos planos.
	 */
	private static String filePathReporteArchivosPlanos;


	public static String getFilePathReporteArchivosPlanos() {
		return filePathReporteArchivosPlanos;
	}

	public static void setFilePathReporteArchivosPlanos(
			String filePathReporteArchivosPlanos) {
		ValoresPorDefecto.filePathReporteArchivosPlanos = filePathReporteArchivosPlanos;
	}

	/**
	 * Al tener problemas con el System.getProperty() teniendo varios contextos,
	 * se decidi&oacute; incluir en esta clase las propiedades del sistema Una de ellas
	 * es reportPath (Definido en el web.xml), este es una ruta en la cual quedan
	 * los archivos tipo reportes. Ej.
	 * /var/tomcat5/webapps/axioma/reportes
	 */
	private static String reportPath;
	
	/**
	 * Al tener problemas con el System.getProperty() teniendo varios contextos,
	 * se decidi&oacute; incluir en esta clase las propiedades del sistema Una de ellas
	 * es reportUrl (Definido en el web.xml), la cual permite acceder via url por
	 * el navegador 
	 * ../reportes
	 */
	private static String reportUrl;
	
	
	/**
	 * Al tener problemas con el System.getProperty() teniendo varios contextos,
	 * se decidi&oacute; incluir en esta clase las propiedades del sistema Una de ellas
	 * es fotosPath (Definido en el web.xml), este es una ruta en la cual quedan
	 * las fotos de los pacientes ingresados. Ej.
	 * /var/tomcat5/webapps/axioma/fotos
	 */
	private static String fotosPath;

	/**
	 * Al tener problemas con el System.getProperty() teniendo varios contextos,
	 * se decidi&oacute; incluir en esta clase las propiedades del sistema Una de ellas
	 * es el folder con las imagenes de la aplicaci&oacute;n (Definido en el web.xml),
	 * este es una ruta en la cual se guardan todas las imagenes utilizadas por
	 * la aplicaci&oacute;n Ej. /var/tomcat5/webapps/axioma/imagenes
	 */
	private static String directorioImagenes;

	/**
	 * Par&aacute;metro que indica el valor a usar como true en las consultas
	 */
	private static String valorTrueParaConsultas;
	
	/**
	 * Equivalente rownum o limit para oracle y postgres respectivamente.
	 */
	private static String valorLimit1;

	/**
	 * Sentencia para manejar el nextval dependiendo de la base de datos
	 * Ejemplo:
	 * 	Oracle:
	 * 		seq_ejemplo.nextval
	 * 	Postgres:
	 * 		nextval('seq_ejemplo')
	 */
	private static String sentenciaNextvalBD;

	/**
	 * Parametro que indica la sentencia a utilizar dependiendo de la BD
	 * para tomar la hora,
	 * 
	 * Ejemplo: Oracle to_char(sysdate, 'dd/mm/yyyy'), Postgres substr(CURRENT_TIME, 1,5);
	 */
	private static String sentenciaHoraActualBD;
	
	/**
	 * Par&aacute;metro que indica la sentencia a utilizar dependiendo de la BD
	 * para tomar la hora,
	 * 
	 * Ejemplo: Oracle to_char(sysdate, 'HH24:MI'), Postgres CURRENT_TIME
	 */
	private static String sentenciaHoraActualBDTipoTime;

	/**
	 * Par&aacute;metro que indica la sentencia a utilizar dependiendo de la BD
	 * para tomar la hora,
	 * 
	 * Ejemplo: Oracle to_char(sysdate, 'HH24:MI:SS'), Postgres CURRENT_TIME
	 */
	private static String sentenciaHoraActualBDTipoTimeConSegundos;
	
	@SuppressWarnings("unused")
	private static String numMaxDiasGenOrdenesAmbServ;
	
	/**
	 * Es la m&iacute;nima duraci&oacute;n que podr&aacute; tener una cita
	 * Agenda Odontol&oacute;gica / Sonria
	 */
	@SuppressWarnings("unused")
	private static String multiploMinGeneracionCita;

	/**
	 * Se utiliza para validar las fechas en los criterios de b&uacute;squeda de la agenda odontol&oacute;gica
	 * Agenda Odontol&oacute;gica / Sonria
	 */
	@SuppressWarnings("unused")
	private static String numDiasAntFActualAgendaOd;
	
	/**
	 * Par&aacute;metro que indica el valor a usar como false en las consultas
	 */
	private static String valorFalseParaConsultas;

	/**
	 * Variable para tomar el numero de una fila en una consulta
	 */
	private static String numeroFilaParaConsultas;

	/**
	 * Almacena el c&oacute;digo del modulo, por el cual se filtra la consulta de los
	 * par&aacute;metros
	 */
	private static int modulo;

	private static HashMap integridadDominio;

	/**
	 * Almacena los 'n' centros de costo de terceros
	 */
	private static HashMap centroCostoTerceros = new HashMap();
	
	/**
	 * 
	 */
	private static ArrayList<Integer> serviciosManejoTransPrimario =new ArrayList<Integer>();
	
	/**
	 * 
	 */
	private static ArrayList<Integer> serviciosManejoTransSecundario =new ArrayList<Integer>();
	

	/**
	 * Almacena las 'n' horas de reproceso
	 */
	private static HashMap horasReproceso = new HashMap();
	
	/**
	 * Campo para saber el cliente de la aplicaci&oacute;n
	 */
	private static String cliente = "";

	/**
	 * Almacena las N Clases de Inventarios para Paquetes Materiales Qx  
	 */
	private static HashMap clasesInventariosPaqMatQx = new HashMap();
	
	/**
	 * Almacena el path de los archivos para la interfaz de laboratorio
	 */
	private static String filePathInterfazLaboratorios = "";
	
	private static ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo = new ArrayList<HashMap<String,Object>>();

	/** 
	 * Para el manejo de FROM DUAL y vac&iacute;o para Oracle y Postgres
	 */
	private static String valorOrigenPL;
	
	/**
	 * Constructor vac&iacute;o
	 */
	public static void iniciarValoresPorDefecto()
	{
		parametrosGenerales = new HashMap<String, Object>();
		integridadDominio = new HashMap();
		centroCostoTerceros = new HashMap();
		serviciosManejoTransPrimario=new ArrayList<Integer>();
		serviciosManejoTransSecundario=new ArrayList<Integer>();
		clasesInventariosPaqMatQx = new HashMap();
		horasReproceso = new HashMap();
		codigoTipoCieActual = ConstantesBD.codigoNuncaValido;
		valorTrueParaConsultas = "true";
		valorLimit1="limit ";
		sentenciaNextvalBD="nextval('?')";
		sentenciaHoraActualBD= " substr(CURRENT_TIME,1,5) ";
		sentenciaHoraActualBDTipoTime= " CURRENT_TIME ";
		valorFalseParaConsultas = "false";
		numeroFilaParaConsultas = "oid";
		modulo = ConstantesBD.codigoNuncaValido;
		valorOrigenPL = "";		
		init();
	}

	/**
	 * M&eacute;todo para iniciar el DAO
	 */
	private static void init() {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));
		valoresPorDefectoDao = daoFactory.getValoresPorDefectoDao();
		if (valoresPorDefectoDao == null) {
			Log4JManager.error("Error obteniendo la clase valoresPorDefectoDao");
		}
	}
	
	/**
	 * M&eacute;todo que carga en memoria todos los valores por defecto del sistema
	 * 
	 * @param con Conexi&oacute;n con la Base de datos
	 * @return true en caso de cargar todos los valores correctamente
	 */
	public static boolean cargarValoresIniciales()
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=cargarValoresIniciales(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * M&eacute;todo que carga en memoria todos los valores por defecto del sistema
	 * 
	 * @param con Conexi&oacute;n con la Base de datos
	 * @return true en caso de cargar todos los valores correctamente
	 */
	public static boolean cargarValoresIniciales(Connection con) {
		// El valor boolean del boolean depende del tipo de BD

		if (System.getProperty("TIPOBD").equals("ORACLE")) {
			valorTrueParaConsultas = "1";
			valorFalseParaConsultas = "0";
			numeroFilaParaConsultas = "rowid";
			valorLimit1 =" AND ROWNUM <= ";
			sentenciaNextvalBD="?.nextval";
			sentenciaHoraActualBD=" to_char(sysdate,'HH24:MI') ";
			sentenciaHoraActualBDTipoTime= " to_char(sysdate,'HH24:MI') ";
			sentenciaHoraActualBDTipoTimeConSegundos= " to_char(sysdate,'HH24:MI:SS') ";
			valorOrigenPL = " FROM DUAL ";			
		} else {
			// Para el resto de BD (Hasta ahora Postgresql)
			// el valor es true
			valorTrueParaConsultas = "true";
			valorLimit1 =" LIMIT ";
			valorFalseParaConsultas = "false";
			numeroFilaParaConsultas = "oid";
			sentenciaNextvalBD="nextval('?')";
			sentenciaHoraActualBD=" substr(CURRENT_TIME,1,5) ";
			sentenciaHoraActualBDTipoTime=" CURRENT_TIME ";
			sentenciaHoraActualBDTipoTimeConSegundos=" CURRENT_TIME ";
			valorOrigenPL = " FROM DUAL ";			
		}

		integridadDominio = valoresPorDefectoDao.cargarIntegridadDominio(con);

		Log4JManager.info("CARGAR VALORES POR DEFECTO.");
		Collection valores = valoresPorDefectoDao.cargar(con);
		if (valores == null || valores.isEmpty()) {
			return false;
		}
		Iterator iterador = valores.iterator();
		while (iterador.hasNext()) {
			HashMap valor = (HashMap) iterador.next();
			String param = valor.get("parametro") + "";
			
			if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDireccion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoDireccion + "_"
								+ valor.get("institucion"), valor.get("valor").toString().trim()
								+ "@@" + valor.get("nombre").toString().trim());

			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
				
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPath)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoPath
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));	
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));			
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));					
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTipoId)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTipoId
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCentinela)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCentinela + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
				
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente				
			/*
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			 */
				
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoVigilarAccRabico)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoVigilarAccRabico + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion
								+ "_" + valor.get("institucion"), valor
								.get("valor")
								+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoTerceros)) {
				centroCostoTerceros = valoresPorDefectoDao
						.consultarCentrosCostoTerceros(con);
				Log4JManager.info("\n\n carga los centros de terceros-->"
						+ centroCostoTerceros + "\n\n");
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoServicioManejoTransportePrimario)) {
				serviciosManejoTransPrimario = valoresPorDefectoDao.consultarServiciosManejoTransPrimario(Utilidades.convertirAEntero(valor.get("institucion")+""),con);
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoServicioManejoTransporteSecundario)) {
				serviciosManejoTransSecundario = valoresPorDefectoDao.consultarServiciosManejoTransSecundario(Utilidades.convertirAEntero(valor.get("institucion")+""),con);
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHorasReproceso)) {
				horasReproceso = valoresPorDefectoDao
						.consultarHorasReproceso(con);
				Log4JManager.info("\n\n carga las horas reproceso-->"
						+ horasReproceso + "\n\n");
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			} else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita)) 
			{
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos)) 
			{
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos)) 
			{
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias)) 
			{
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario+ "_" + valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if (param
				.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente)) {
			parametrosGenerales
					.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente
									+ "_" + valor.get("institucion"), valor
									.get("valor")
									+ "@@" + valor.get("nombre"));
			}
			else if(param
				.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente)) {
			parametrosGenerales
					.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente
									+ "_" + valor.get("institucion"), valor
									.get("valor")
									+ "@@" + valor.get("nombre"));
			}
			else if(param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			
			
			else if(param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips)) {
				parametrosGenerales
						.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips
										+ "_" + valor.get("institucion"), valor
										.get("valor")
										+ "@@" + valor.get("nombre"));
			}
			
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			
			
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico)) 
			{
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico + "_"+ valor.get("institucion"), valor.get("valor")+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValidarPoolesFact)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValidarPoolesFact + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}

			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}

			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}

			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior)) {
				parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior + "_" 
					+ valor.get("institucion"), valor.get("valor")
					+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoClasesInventariosPaqMatQx)) {
					clasesInventariosPaqMatQx = valoresPorDefectoDao.consultarClasesInventariosPaqMatQx(con);
					Log4JManager.info("\n===> Cargar las Clases de Inventarios Paq. Mat. Qx.-->"+ clasesInventariosPaqMatQx + "\n");
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			//70006 - Numero Digitos Captura Numero Identificacion Pacientes
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac )) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin )) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			
			}		
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			
			}	
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc )) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc + "_" 
							+ valor.get("institucion"), valor.get("valor")
							+ "@@" + valor.get("nombre"));
			}
			
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto
										+ "_" + valor.get("institucion"), valor.get("valor")
										+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.multiploMinGeneracionCita)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.multiploMinGeneracionCita
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreOcupacionOdontologo)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreOcupacionOdontologo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreEdadFinalNinez)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEdadFinalNinez
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreEdadInicioAdulto)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEdadInicioAdulto
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado)) {
				
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo)) {
				conveniosAMostrarPresupuestoOdo = Utilidades.obtenerConveniosAMostrarPresupuestoOdo(con, Utilidades.convertirAEntero(valor.get("institucion")+""));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if (param
					.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			//Anexo 992
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			//Fin Anexo 992
			
			//Anexo 958
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoIngresoFacturasVarias)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoIngresoFacturasVarias
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			//Fin anexo 958
			
			
			//Anexo 888
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			//Fin anexo 888
			
			
			//Anexo 888 Pt II
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
				
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			//Fin Anexo 888 Pt II
			
			
			//Anexo 959
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			//Fin Anexo 959
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			//-------------------------------
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			//**
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			//**
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}		
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}		
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}	
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
				
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso)) {
                parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso
                                + "_" + valor.get("institucion"), valor.get("valor")
                                + "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada)) {
				parametrosGenerales.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada + "_"
								+ valor.get("institucion"), valor.get("valor")
								+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasProrrogaAutorizacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasProrrogaAutorizacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}		
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
						
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}	

			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoNaturalezaNotasPacientesManejar)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNaturalezaNotasPacientesManejar
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			} else if (param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}else if(param.equals(ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas)) {
				parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas
						+ "_" + valor.get("institucion"), valor.get("valor")
						+ "@@" + valor.get("nombre"));
			}
			
			
			
			
		}
		recargarCieActual(con);
		return true;
	}
	
	
	

	/**
	 * Obtiene la sentencia para poner un l&iacute;mite a las consultas
	 * @return Sentencia que se desea consultar
	 */
	public static String getValorLimit1() {
		return valorLimit1;
	}

	/**
	 * Asigna la sentencia para poner un l&iacute;mite a las consultas
	 * @param valorLimit1 Sentencia que se desea asignar
	 */
	public static void setValorLimit1(String valorLimit1) {
		ValoresPorDefecto.valorLimit1 = valorLimit1;
	}

	/**
	 * Obtiene la sentencia para consultas SQL las cuales no dependen de una tabla
	 * 
	 * Ejemplo: <code>SELECT CURRENT_DATE <b>FROM DUAL<b></code>
	 * @return
	 */
	public static String getValorOrigenPL() {
		return valorOrigenPL;
	}
	
	/**
	 * Asigna la sentencia para consultas SQL las cuales no dependen de una tabla
	 * @param valorOrigenPL
	 */
	public static void setValorOrigenPL(String valorOrigenPL) {
		ValoresPorDefecto.valorOrigenPL = valorOrigenPL;
	}

	/**
	 * 
	 * @param con Coneci&oacute;n con la Base de datos
	 * @return
	 */
	public static String codigosClasesValorPorDefecto(Connection con)
	{
		String codigosClases = "";
		clasesInventariosPaqMatQx = valoresPorDefectoDao.consultarClasesInventariosPaqMatQx(con);
		//Guardamos el numRegistros del mapa
		int numReg = Utilidades.convertirAEntero(clasesInventariosPaqMatQx.get("numRegistros")+"");
		if(numReg == 1)
		{
			if (!(clasesInventariosPaqMatQx.get("codigoclase_0")+"").equals(""))
				codigosClases = clasesInventariosPaqMatQx.get("codigoclase_0")+"";
		}
		else if(numReg == 0)
		{
			codigosClases = "";
		}
		else
		{
			if(numReg > 1)
			{
				for(int i=0; i<numReg; i++)
				{
					if (i == 0)
						codigosClases = clasesInventariosPaqMatQx.get("codigoclase_"+i)+"";
					else
						codigosClases += ","+clasesInventariosPaqMatQx.get("codigoclase_"+i)+"";
				}
			}
		}
		return codigosClases;
	}
	
	
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarClasesInventariosPaqMatQx(Connection con, HashMap mapa)
	{
		return valoresPorDefectoDao.insertarClasesInventariosPaqMatQx(con, mapa);
	}
	
				
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarServiciosManejoTransPrimario(Connection con, ArrayList<Integer> servicios,int institucion)
	{
		return valoresPorDefectoDao.insertarServiciosManejoTransPrimario(con, servicios,institucion);
	}

	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarServiciosManejoTransSecundario(Connection con, ArrayList<Integer> servicios,int institucion)
	{
		return valoresPorDefectoDao.insertarServiciosManejoTransSecundario(con, servicios,institucion);
	}

	
	
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarCentrosCostoTercero(Connection con, HashMap mapa)
	{
		return valoresPorDefectoDao.insertarCentrosCostoTercero(con, mapa);
	}
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarHorasReproceso(Connection con, HashMap mapa) {
		return valoresPorDefectoDao.insertarHorasReproceso(con, mapa);
	}

	/**
	 * M&eacute;todo para modificar un parametro de valores por defecto
	 * @param con
	 * @param parametro
	 * @param nombre
	 * @param valor
	 * @param usuario
	 * @return true si se modifico correctamente, false de lo contrario
	 */
	public static boolean modificar(Connection con, int parametro,
			String nombre, String valor, String usuario, int institucion) {
		boolean resultado = true;
		String valorNuevo = valor + "@@" + nombre;
		String log;

		switch (parametro) {
		case ConstantesValoresPorDefecto.codigoValoresDefectoDireccion:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoDireccion))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoDireccion, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tdireccionPaciente [ "
							+ getDireccionPaciente(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoDireccion + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getDireccionPaciente(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoPath:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoPath))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoPath, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tPath Repotes [ "
							+ getArchivosPlanosReportes(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoPath + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getArchivosPlanosReportes(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoPathInventarios:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tPath Repotes [ "
							+ getArchivosPlanosReportesInventarios(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getArchivosPlanosReportesInventarios(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoConsultaExterna:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tcentroCostoConsultaExterna [ "
							+ getCentroCostoConsultaExterna(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCentroCostoConsultaExterna(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCausaExterna:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tcausaExterna [ "
							+ getCausaExterna(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCausaExterna(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoFinalidadConsulta:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tfinalidad [ "
							+ getFinalidad(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getFinalidad(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCiudadNacimiento:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tciudadNacimiento [ "
							+ getCiudadNacimiento(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCiudadNacimiento(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCiudadResidencia:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tciudadVivienda [ "
							+ getCiudadVivienda(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCiudadVivienda(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoPaisResidencia:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tpais residencia [ "
							+ getPaisResidencia(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getPaisResidencia(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoPaisNacimiento:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tpais Nacimiento [ "
							+ getPaisNacimiento(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getPaisNacimiento(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoDomicilio:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tzonaDomicilio [ "
							+ getZonaDomicilio(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getZonaDomicilio(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoOcupacion:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tocupacion [ "
							+ getOcupacion(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getOcupacion(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoTipoId:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoTipoId))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoTipoId, nombre, valor,
						institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
							+ getTipoId(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoTipoId + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getTipoId(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		//FIXME	
		case ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos, nombre, valor,
						institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
							+ getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica, nombre, valor,
						institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
							+ getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoIngresoEdad:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tfechaNacimiento [ "
							+ getFechaNacimiento(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getFechaNacimiento(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoHistoriaClinica:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\thistoriaClinica [ "
							+ getHistoriaClinica(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getHistoriaClinica(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCentinela:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCentinela))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCentinela, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tflagCentinela [ "
							+ getFlagCentinela(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCentinela + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getFlagCentinela(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoUrgencias:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tcentroCostoUrgencias [ "
							+ getCentroCostoUrgencias(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCentroCostoUrgencias(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoOcupacionSolicitada:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tocupacionSolicitada [ "
							+ getOcupacionSolicitada(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getOcupacionSolicitada(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoEstadoCamaEgreso:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tcodigoEstadoCama [ "
							+ getCodigoEstadoCama(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoEstadoCama(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoValorUVR:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tvalorUVR [ "
							+ getValorUVR(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ " + getValorUVR(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			
			
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente
			/**
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCarnetRequerido:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tcarnetRequerido [ "
							+ getCarnetRequerido(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCarnetRequerido(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			*/
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarInterpretadas:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tvalidarEstadoSolicitudesInterpretadas [ "
							+ getValidarEstadoSolicitudesInterpretadas(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getValidarEstadoSolicitudesInterpretadas(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarContratosVencidos:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tvalidarContratosVencidos [ "
							+ getValidarContratosVencidos(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getValidarContratosVencidos(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoManejoTopesPaciente:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tmanejoTopesPaciente [ "
							+ getManejoTopesPaciente(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getManejoTopesPaciente(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoAmbulatorios:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tcentroCostoAmbulatorios [ "
							+ getCentroCostoAmbulatorios(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCentroCostoAmbulatorios(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoJustificacionServiciosRequerida:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tJustificacion Servicios Requerida [ "
							+ getJustificacionServiciosRequerida(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getJustificacionServiciosRequerida(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoIngresoCantidadFarmacia:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tIngreso Cantidad Farmacia [ "
							+ getIngresoCantidadFarmacia(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getIngresoCantidadFarmacia(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoRipsPorFactura:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tripsPorFactura [ "
							+ getRipsPorFactura(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getRipsPorFactura(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoFechaCorteSaldoInicialC:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tFecha Corte Saldo Inicial Cartera [ "
							+ getFechaCorteSaldoInicialC(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getFechaCorteSaldoInicialC(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoFechaCorteSaldoInicialCCapitacion:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tFecha Corte Saldo Inicial Cartera Capitacion [ "
							+ getFechaCorteSaldoInicialCCapitacion(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getFechaCorteSaldoInicialCCapitacion(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoTopeConsecutivoCxCSaldoI:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tTope Consecutivo CxC Saldo Inicial [ "
							+ getTopeConsecutivoCxCSaldoI(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getTopeConsecutivoCxCSaldoI(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoTopeConsecutivoCxCSaldoICapitacion:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tTope Consecutivo CxC Saldo Inicial Capitacion [ "
							+ getTopeConsecutivoCxCSaldoICapitacion(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getTopeConsecutivoCxCSaldoICapitacion(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoMaxPageItems:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tNumero de Lineas Pager [ "
							+ getMaxPageItems(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getMaxPageItems(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoMaxPageItemsEpicrisis:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tNumero de Lineas Pager Epicrisis [ "
							+ getMaxPageItemsEpicrisis(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis + "_"
									+ institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getMaxPageItemsEpicrisis(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
		case ConstantesValoresPorDefecto.codigoValoresDefectoExcepcionRipsConsultorios:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tExcepción Rips Consultorios [ "
							+ getExcepcionRipsConsultorios(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getExcepcionRipsConsultorios(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoAjustarCuentaCobroRadicada:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tAjustar Cuenta de Cobro Radicada [ "
							+ getAjustarCuentaCobroRadicada(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getAjustarCuentaCobroRadicada(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoCerrarCuentaAnulacionFactura:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCerrar Cuenta Anulación Factura [ "
							+ getCerrarCuentaAnulacionFactura(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCerrarCuentaAnulacionFactura(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoBarrioResidencia:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tBarrio de Residencia [ "
							+ getBarrioResidencia(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getBarrioResidencia(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoMaterialesPorActo:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tMateriales por Acto [ "
							+ getMaterialesPorActo(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getMaterialesPorActo(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoHoraInicioProgramacionSalas:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tHora Inicial Programación salas [ "
							+ getHoraInicioProgramacionSalas(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getHoraInicioProgramacionSalas(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoHoraFinProgramacionSalas:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tHora Fin Programación salas [ "
							+ getHoraFinProgramacionSalas(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getHoraFinProgramacionSalas(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoEspecialidadAnestesiologia:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tEspecialidad Anestesiología [ "
							+ getEspecialidadAnestesiologia(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getEspecialidadAnestesiologia(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoManejoConsecutivoTransInv:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tConsecutivo Transacciones Inventarios [ "
							+ getManejoConsecutivoTransInv(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getManejoConsecutivoTransInv(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoPorcentajeAlertaCostosInv:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tPorcentaje de Alerta en Diferencia en los Costos de Inventarios [ "
							+ getPorcentajeCostosInv(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getPorcentajeCostosInv(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransSoliPacientes:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para las Solicitudes de Pacientes [ "
							+ getCodigoTransSoliPacientes(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransSoliPacientes(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionesPaciente:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para las Devoluciones de Pacientes [ "
							+ getCodigoTransDevolPacientes(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransDevolPacientes(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransPedidos:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para los Pedidos [ "
							+ getCodigoTransaccionPedidos(institucion, true)
							+ " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransaccionPedidos(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionesPedidos:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para las Devoluciones de Pedidos [ "
							+ getCodigoTransDevolucionPedidos(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransDevolucionPedidos(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransCompras:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras))) 
			{
				resultado = valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para las Compras [ "
							+ getCodigoTransCompra(institucion, true)
							+ " ]";
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransCompra(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionCompras:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo de Transacción Utilizado para las Devoluciones de Compras [ "
							+ getCodigoTransDevolCompra(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransDevolCompra(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoModificarFechaaInventarios:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tPermitir Modificar la Fecha de Elaboración de Inventarios [ "
							+ getModificacionFechaInventario(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getModificacionFechaInventario(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoPorcentajePuntoPedido:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tPorcentaje Para Alerta de Punto de Pedido [ "
							+ getPorcentajePuntoPedido(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getPorcentajePuntoPedido(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoDiasAlertaVigencia:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tDias de alerta de la vigencia Catalogo [ "
							+ getDiasAlertaVigencia(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getDiasAlertaVigencia(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoDiasPreviosNotificacionProximoControl:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tDías Previos Notificación próxima Cita de Control [ "
							+ getDiasPreviosNotificacionProximoControl(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getDiasPreviosNotificacionProximoControl(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransTrasladoAlmacenes:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo Transacción Utilizado para el Translado de Almacenes [ "
							+ getCodigoTransTrasladoAlmacenes(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoTransTrasladoAlmacenes(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoInfoAdicIngresoConvenios:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tChequeo Automático del campo Información Adcional en el ingreso de Convenios [ "
							+ getInfoAdicIngresoConvenios(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getInfoAdicIngresoConvenios(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionMedicoEspecialista:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo Ocupación Médico Especialista [ "
							+ getCodigoOcupacionMedicoEspecialista(institucion,
									true) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoOcupacionMedicoEspecialista(institucion,
									true) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionMedicoGeneral:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo Ocupación Médico General [ "
							+ getCodigoOcupacionMedicoGeneral(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoOcupacionMedicoGeneral(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionEnfermera:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo Ocupación Enfermera [ "
							+ getCodigoOcupacionEnfermera(institucion, true)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoOcupacionEnfermera(institucion, true)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionAuxiliarEnfermeria:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCódigo Ocupación Auxiliar Enfermería [ "
							+ getCodigoOcupacionAuxiliarEnfermeria(institucion,
									true) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getCodigoOcupacionAuxiliarEnfermeria(institucion,
									true) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoHoraInicioPrimerTurno:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tHora Inicial Primer Turno Enfermería [ "
							+ getHoraInicioPrimerTurno(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getHoraInicioPrimerTurno(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoHoraFinUltimoTurno:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tHora Fin Último Turno Enfermeria [ "
							+ getHoraFinUltimoTurno(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getHoraFinUltimoTurno(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoVigilarAccRabico:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoVigilarAccRabico))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoVigilarAccRabico,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tAlertar cuando el usuario tiene notificaciones pendientes [ "
							+ getVigilarAccRabico(institucion) + " ]";
					log += "\n\tModificado por: [ "
							+ getVigilarAccRabico(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosEsperaCitaCaduca:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Minutos De Espera Para Asignar Citas Caducas [ "
							+ getMinutosEsperaCitaCaduca(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getMinutosEsperaCitaCaduca(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoTiempoMaximoGrabacion:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion,
						nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Tiempo Máximo Grabación de Registros [ "
							+ getTiempoMaximoGrabacion(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getTiempoMaximoGrabacion(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoIngresoCantidadSolMedicamentos:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Ingreso Cantidad Farmacia [ "
							+ getIngresoCantidadSolMedicamentos(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getIngresoCantidadSolMedicamentos(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoTipoConsecutivoCapitacion:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Tipo de consecutivo capitacion [ "
							+ getTipoConsecutivoCapitacion(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getTipoConsecutivoCapitacion(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;


		case ConstantesValoresPorDefecto.codigoValoresDefectoModificarMinutosEsperaCuentasProcFact:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Modificar Minutos en espera para restaurar cuentas en proc facturacion [ "
							+ getModificarMinutosEsperaCuentasProcFact(institucion) + " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getModificarMinutosEsperaCuentasProcFact(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoDatosCuentaRequeridoReservaCitas:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tDatos de la Cuenta Requeridos en la Reserva de Citas [ "
							+ getDatosCuentaRequeridoReservaCitas(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getDatosCuentaRequeridoReservaCitas(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;

		case ConstantesValoresPorDefecto.codigoValoresDefectoRedNoAdscrita:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tRed no adscrita [ "
							+ getRedNoAdscrita(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getRedNoAdscrita(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasTratamientoMedicamentos:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tNúmero días tratamiento en solicitudes de medicamentos [ "
							+ getNumDiasTratamientoMedicamentos(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getNumDiasTratamientoMedicamentos(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasGenerarOrdenesArticulos:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tNúmero máximo días para generar solicitudes de órdenes ambulatorias artículos [ "
							+ getNumDiasGenerarOrdenesArticulos(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getNumDiasGenerarOrdenesArticulos(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasEgresoOrdenesAmbulatorias:
			if (!valorNuevo.equals(parametrosGenerales
					.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias))) {
				resultado = valoresPorDefectoDao.modificar(con,
						ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias, nombre,
						valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n" + "\tNúmero máximo días de egreso para generar órdenes ambulatorias [ "
							+ getNumDiasEgresoOrdenesAmbulatorias(institucion) + " ]";
					parametrosGenerales.put(
							ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias
									+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getNumDiasEgresoOrdenesAmbulatorias(institucion) + " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoConvenioFisalud:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tconvenio FISALUD [ "
							+ getConvenioFisalud(institucion) + " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getConvenioFisalud(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoFormaPagoEfectivo:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tForma pago Efectivo [ "
							+ getFormaPagoEfectivo(institucion) + " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getFormaPagoEfectivo(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoManualEstandarBusquedaServicios:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tCodigo manual estandar busqueda servicios [ "
							+ getCodigoManualEstandarBusquedaServicios(institucion) + " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getCodigoManualEstandarBusquedaServicios(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoConfimraAjustePool:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tconfirmar_ajustes_pooles [ "
							+ getConfirmarAjustesPooles(institucion) + " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getConfirmarAjustesPooles(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;	
		case ConstantesValoresPorDefecto.codigoValoresDefectoHorasCaducidadReferenciasExternas:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tHoras Caducidad Referencias Externas [ "
							+ getHorasCaducidadReferenciasExternas(institucion) + " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getHorasCaducidadReferenciasExternas(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoLlamadoAutomaticoReferencia:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getLlamadoAutomaticoReferencia(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getLlamadoAutomaticoReferencia(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoMostrarAntecedentesEpicrisis:
			if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis))) 
			{
				resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis,nombre,valor,institucion);
				if(resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Mostrar antecedentes parametrizado epicrisis [ "
							+ getMostrarAntecedentesParametrizadosEpicrisis(institucion)
							+ " ]";
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMostrarAntecedentesParametrizadosEpicrisis(institucion)
						+ " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;	

		case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirConsultarEpicrisisSoloProf:
			if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales))) 
			{
				resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales,nombre,valor,institucion);
				if(resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Permitir consultar epicrisis solo a profesionales de la salud [ "
							+ getPermitirConsultarEpicrisisSoloProfesionales(institucion)
							+ " ]";
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getPermitirConsultarEpicrisisSoloProfesionales(institucion)
						+ " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazPaciente:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getInterfazPaciente(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getInterfazPaciente(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazAbonosTesoreria:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getInterfazAbonosTesoreria(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getInterfazAbonosTesoreria(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazCompras:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getInterfazCompras(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getInterfazCompras(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoArticuloInventario:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getArticuloInventario(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getArticuloInventario(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoLoginUsuario:
			if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario))) 
			{
				resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario,nombre, valor, institucion);
				if (resultado) 
				{
					log = "PARAMETRO MODIFICADO\n"
							+ "\tLlamado Automatico Funcionalidad Referencia [ "
							+ getLoginUsuario(institucion)+ " ]";
					
					parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "+ getLoginUsuario(institucion) + " ]";
					LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
		break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarEdadResponsablePaciente:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tValidar Edad del Responsable de Paciente [ "
							+ getValidarEdadResponsablePaciente(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getValidarEdadResponsablePacienteLargo(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarEdadDeudorPaciente:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con, 
								ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente, 
								nombre, valor, institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tValidar Edad del Deudor Paciente [ "
							+ getValidarEdadDeudorPaciente(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getValidarEdadDeudorPacienteLargo(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoAniosBaseEdadAdulta:
			if (!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta))) {
				resultado = valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta,
								nombre, valor, institucion);
				if (resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tAños de Base de la Edad Adulta [ "
							+ getAniosBaseEdadAdulta(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getAniosBaseEdadAdultaLargo(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log,
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoValidarEgresoAdministrativoPaquetizar:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\tValidar Egreso Administrativo para Paquetizar [ "
							+ getValidarEgresoAdministrativoPaquetizar(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
							+ getValidarEgresoAdministrativoPaquetizarLargo(institucion)
							+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoMaxCantidPaquetesValidosIngresoPaciente:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente,
								nombre,
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "\t Maxima Cantidad de Paquetes Validos por Ingreso Paciente [ "
							+ getMaxCantidPaquetesValidosIngresoPaciente(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMaxCantidPaquetesValidosIngresoPacienteLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoAsignarValorPacienteValorCargos:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Asignar Valor Paciente con el Valor de los Cargos [ "
							+ getAsignarValorPacienteValorCargos(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getAsignarValorPacienteValorCargosLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazCarteraPacientes:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Interfaz Cartera Pacientes [ "
							+ getInterfazCarteraPacientes(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getInterfazCarteraPacientesLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazContableFacturas:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Interfaz Contable Facturas [ "
							+ getInterfazContableFacturas(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getInterfazContableFacturasLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazTerceros:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Interfaz Terceros [ "
							+ getInterfazTerceros(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getInterfazTercerosLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoConsolidarCargos:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Consolidar Cargos [ "
							+ getConsolidarCargos(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getConsolidarCargosLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoManejaConversionMonedaExtranjera:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Maneja Conersion Moneda Extranjera [ "
							+ getManejaConversionMonedaExtranjera(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getManejaConversionMonedaExtranjeraLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
		
		case ConstantesValoresPorDefecto.codigoValoresDefectoImpresionMediaCarta:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Impresion Reportes Media Carta [ "
							+ getImpresionMediaCarta(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getImpresionMediaCartaLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoHoraCorteHistoricoCamas:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Hora corte historico camas [ "
							+ getHoraCorteHistoricoCamas(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getHoraCorteHistoricoCamasLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaReserva:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Minutos limite alerta Reserva [ "
							+ getMinutosLimiteAlertaReserva(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMinutosLimiteAlertaReservaLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Minutos limite alerta Paciente Con salida Hospitalizacion [ "
							+ getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMinutosLimiteAlertaPacienteConSalidaHospitalizacionLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;	
			
			
		case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Minutos limite alerta Paciente Con salida Urgencias [ "
							+ getMinutosLimiteAlertaPacienteConSalidaUrgencias(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMinutosLimiteAlertaPacienteConSalidaUrgenciasLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
		case ConstantesValoresPorDefecto.codigoValoresDefectominutosLimiteAlertaPacientePorRemitirHospitalizacion:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Minutos limite alerta Paciente por Remitir Hospitalizacion [ "
							+ getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMinutosLimiteAlertaPacientePorRemitirHospitalizacionLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias:
			if(!valorNuevo
					.equals(parametrosGenerales
							.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias))) {
				resultado=valoresPorDefectoDao
						.modificar(
								con,
								ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias, 
								nombre, 
								valor, 
								institucion);
				if(resultado) {
					log = "PARAMETRO MODIFICADO\n"
							+ "'t Minutos limite alerta Paciente por Remitir Urgencias [ "
							+ getMinutosLimiteAlertaPacientePorRemitirUrgencias(institucion)
							+ " ]";
					parametrosGenerales
							.put(
									ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias
											+ "_" + institucion, valorNuevo);
					log += "\n\tModificado por: [ "
						+ getMinutosLimiteAlertaPacientePorRemitirUrgenciasLargo(institucion)
						+ " ]";
					LogsAxioma.enviarLog(
							ConstantesBD.logParametrosGeneralesCodigo, log, 
							ConstantesBD.tipoRegistroLogModificacion, usuario);
				}
			}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoIdentificadorInstitucionArchivosColsanitas:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Identificador Institución para reportar en archivos Colsanitas [ "
								+ getIdentificadorInstitucionArchivosColsanitas(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getIdentificadorInstitucionArchivosColsanitasLargo(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazRips:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Interfaz Rips [ "
									+ getInterfazRips(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getInterfazRips(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoGeneracionForecatEnRips:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Interfaz Rips [ "
									+ getInterfazRips(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getInterfazRips(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoEntidadManejaHospitalDia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Entidad Maneja Hospital Día [ "
									+ getEntidadManejaHospitalDia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getEntidadManejaHospitalDia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoEntidadManejaRips:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Entidad Maneja RIPS [ "
									+ getEntidadManejaRips(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getEntidadManejaRips(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoValoracionUrgenciasEnHospitalizacion:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
								+ "'t Valoración de urgencias en hospitalización [ "
									+ getValoracionUrgenciasEnHospitalizacion(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getValoracionUrgenciasEnHospitalizacion(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoLiquidacionAutomaticaCirugias:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Liquidación automática cirugías [ "
									+ getLiquidacionAutomaticaCirugias(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getLiquidacionAutomaticaCirugias(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoLiquidacionAutomaticaNoQx:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Liquidacion automática No Qx [ "
									+ getLiquidacionAutomaticaNoQx(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getLiquidacionAutomaticaNoQx(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoManejoProgramacionSalasSolicitudes:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Manejo programación salas solicitudes [ "
									+ getManejoProgramacionSalasSolicitudesDyt(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getManejoProgramacionSalasSolicitudesDyt(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoRequridaDescripcionEspecialidadCirugias:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Requerida Descripcion especialidad cirugías [ "
									+ getRequeridaDescripcionEspecialidadCirugias(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getRequeridaDescripcionEspecialidadCirugias(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoRequridaDescripcionEspecialidadNoCruentos:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Requerida descripcion especialidad no cruentos [ "
									+ getRequeridaDescripcionEspecialidadNoCruentos(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getRequeridaDescripcionEspecialidadNoCruentos(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAyudantia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Asocio ayudantía [ "
									+ getAsocioAyudantia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getAsocioAyudantia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoIndicativoCobrableHonorariosCirugia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Indicativo cobrable honorarios cirugía [ "
									+ getIndicativoCobrableHonorariosCirugia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getIndicativoCobrableHonorariosCirugia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoIndicativoCobrableHonorariosNoCruento:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t indicatico cobrable honorarios no cruento [ "
									+ getIndicativoCobrableHonorariosNoCruento(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getIndicativoCobrableHonorariosNoCruento(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoAsocioCirujano:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Asocio cirujano [ "
									+ getAsocioCirujano(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getAsocioCirujano(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAnestesia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t asocio anestesia [ "
									+ getAsocioAnestesia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getAsocioAnestesia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Modificar información descripcion quirurgica y diagnosticos [ "
									+ getModificarInformacionDescripcionQuirurgica(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getModificarInformacionDescripcionQuirurgica(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosRegistroNotasCirugia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Minutos registro notas cirugía [ "
									+ getMinutosRegistroNotasCirugia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getMinutosRegistroNotasCirugia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosRegistroNotasNoCruentos:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Minutos registro notas no cruentos [ "
									+ getMinutosRegistroNotasNoCruentos(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getMinutosRegistroNotasNoCruentos(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoModificarInformacionQuirurgica:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Modificar información quirúrgica [ "
									+ getModificarInformacionQuirurgica(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getModificarInformacionQuirurgica(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosMaximosRegistroAnestesia:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Minutos máximos registro de anestesia [ "
									+ getMinutosMaximosRegistroAnestesia(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getMinutosMaximosRegistroAnestesia(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				
				case ConstantesValoresPorDefecto.codigoValoresDefectoUbicacionPlanosEntidadesSubcontratadas:
					if(!valorNuevo
							.equals(parametrosGenerales
									.get(ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas))) {
						resultado=valoresPorDefectoDao
								.modificar(
										con,
										ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas, 
										nombre, 
										valor, 
										institucion);
						if(resultado) {
							log = "PARAMETRO MODIFICADO\n"
									+ "'t Ubicación planos entidades subcontratadas [ "
									+ getUbicacionPlanosEntidadesSubcontratadas(institucion)
									+ " ]";
							parametrosGenerales
									.put(
											ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas
													+ "_" + institucion, valorNuevo);
							log += "\n\tModificado por: [ "
								+ getUbicacionPlanosEntidadesSubcontratadas(institucion)
								+ " ]";
							LogsAxioma.enviarLog(
									ConstantesBD.logParametrosGeneralesCodigo, log, 
									ConstantesBD.tipoRegistroLogModificacion, usuario);
						}
					}
				break;
				

			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionMultiempresa:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Institucion Multiempresa [ "
								+ getInstitucionMultiempresa(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getInstitucionMultiempresa(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			case ConstantesValoresPorDefecto.codigoCrearCuentaAtencionCitas:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t ¿Crear cuenta en atencion de citas? [ "
								+ getCrearCuentaAtencionCitas(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getCrearCuentaAtencionCitas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			case ConstantesValoresPorDefecto.codigoValoresDefectoConteosValidosAjustarInvFisico:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Conteos Válidos para Ajustar Inventario Físico [ "
								+ getConteosValidosAjustarInventarioFisico(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getConteosValidosAjustarInventarioFisico(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEntidadControlaDespachoSaldosMultidosis:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Entidad Controla Despacho Saldos Multidosis [ "
								+ getEntidadControlaDespachoSaldosMultidosis(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getEntidadControlaDespachoSaldosMultidosis(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasControMedOrdenados:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Numero Dias Control Medicamentos Ordenados [ "
								+ getNumeroDiasControlMedicamentosOrdenados(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getNumeroDiasControlMedicamentosOrdenados(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			case ConstantesValoresPorDefecto.codigoValoresDefectoGenerarEstanciaAutomatica:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Genera estancia automática S/N [ "
								+ getGenerarEstanciaAutomatica(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getGenerarEstanciaAutomatica(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraGenerarEstanciaAutomatica:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Hora Generación proceso estancia automática [ "
								+ getHoraGenerarEstanciaAutomatica(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getHoraGenerarEstanciaAutomatica(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Incluir tipo de pacientes Cirugía Ambulatoria para generación del cargo automático de la estancia? [ "
								+ getIncluirTipoPacienteCirugiaAmbulatoria(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getIncluirTipoPacienteCirugiaAmbulatoria(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTipoConsecutivoManejar:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t TipoConsecutivoManejar? [ "
								+ getTipoConsecutivoManejar(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getTipoConsecutivoManejar(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequeridaInfoRipsCargosDirectos:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Hacer Requerida Información RIPS en Cargos Directos ? [ "
								+ getRequeridaInfoRipsCagosDirectos(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getRequeridaInfoRipsCagosDirectos(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidacionOcupacionJustificacionNoPosArticulos:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Validar Ocupacion Medico Especialista para Ingreso Justificacion No Pos de ARticulos ? [ "
								+ getValidacionOcupacionJustificacionNoPosArticulos(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidacionOcupacionJustificacionNoPosArticulos(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidacionOcupacionJustificacionNoPosServicios:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Validar Ocupacion Medico Especialista para Ingreso Justificacion No Pos de Servicios ? [ "
								+ getValidacionOcupacionJustificacionNoPosServicios(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidacionOcupacionJustificacionNoPosServicios(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado))) 
				{				
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado,nombre,valor,institucion);
					
					
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Asigna Valoración de Cirugia Ambulatoria a Hospitalizado ? [ "
								+ getAsignaValoracionCxAmbulaHospita(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getAsignaValoracionCxAmbulaHospita(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazContableRecibosCajaERP:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Entidad Controla Despacho Saldos Multidosis [ "
								+ getEntidadControlaDespachoSaldosMultidosis(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getEntidadControlaDespachoSaldosMultidosis(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidarAdministracionMedicamentosEgresoMedico:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Numero Dias Control Medicamentos Ordenados [ "
								+ getNumeroDiasControlMedicamentosOrdenados(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getNumeroDiasControlMedicamentosOrdenados(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Manejo Interfaz Consecutivo de Facturas Otro Sistema [ "
								+ getInterfazConsecutivoFacturasOtroSistema(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getInterfazConsecutivoFacturasOtroSistema(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoInterfazNutricion:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Manejo Interfaz Consecutivo de Facturas Otro Sistema [ "
								+ getInterfazConsecutivoFacturasOtroSistema(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getInterfazConsecutivoFacturasOtroSistema(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoUrgencias:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Tiempo maximo para reingreso en urgencias [ "
								+ getTiempoMaximoReingresoUrgencias(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getTiempoMaximoReingresoUrgencias(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoHospitalizacion:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Tiempo maximo para reingreso en hospitalización [ "
								+ getTiempoMaximoReingresoHospitalizacion(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getTiempoMaximoReingresoHospitalizacion(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoPermitirFacturarReingresosIndependientes:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Permitir facturar reingresos independientes [ "
								+ getPermitirFacturarReingresosIndependientes(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getPermitirFacturarReingresosIndependientes(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoLiberarCamaHospitalizacionDespuesFacturar:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Liberar cama de hospitalizaion despues de faturar [ "
								+ getLiberarCamaHospitalizacionDespuesFacturar(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getLiberarCamaHospitalizacionDespuesFacturar(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoControlaInterpretacionProcedimientosParaEvolucionar:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Controla Interpretacion Procedimientos Evoluciones [ "
								+ getControlaInterpretacionProcedimientosEvoluciones(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getControlaInterpretacionProcedimientosEvoluciones(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValidarRegistroEvolucionesParaGenerarOrdenes:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Validar Registro Evoluciones Para Generar Ordenes [ "
								+ getValidarRegistroEvolucionesGenerarOrdenes(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidarRegistroEvolucionesGenerarOrdenes(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFacturacion:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Path Archivos Planos Reportes Facturacion[ "
								+ getValoresDefectoPathArchivosPlanosFacturacion(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoPathArchivosPlanosFacturacion(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFurips:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Path Archivos Planos Reportes Furips[ "
								+ getValoresDefectoPathArchivosPlanosFurips(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoPathArchivosPlanosFurips(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosManejoPaciente:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Path Archivos Planos Reportes Manejo Paciente[ "
								+ getValoresDefectoPathArchivosPlanosManejoPaciente(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoPathArchivosPlanosManejoPaciente(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidarPoolesFact:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValidarPoolesFact))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValidarPoolesFact, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Se valida información de pooles en la facturación?[ "
								+ getValoresDefectoValidarPoolesFact(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValidarPoolesFact
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoValidarPoolesFact(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMostrarEnviarEpicrisisEvol:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Mostrar Enviar Epicrisis Evol?[ "
								+ getValoresDefectoMostrarEnviarEpicrisisEvol(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoMostrarEnviarEpicrisisEvol(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAutorizarAnularFacturas:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Requiere Autorizacion Anular Facturas[ "
								+ getValoresDefectoRequiereAutorizarAnularFacturas(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoRequiereAutorizarAnularFacturas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoComprobacionDerCapitacionObliga:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Comprobacion Derechos Capitacion Obligatoria[ "
								+ getValoresDefectoComprobacionDerechosCapitacionObligatoria(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoComprobacionDerechosCapitacionObligatoria(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoConceptoParaAjusteEntrada:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Concepto para ajuste de entrada [ "
								+ getValoresDefectoConceptoParaAjusteEntrada(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoConceptoParaAjusteEntrada(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoConceptoParaAjusteSalida:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Concepto para ajuste de salida [ "
								+ getValoresDefectoConceptoParaAjusteSalida(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoConceptoParaAjusteSalida(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarConceptosAjuste:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Permitir modificar conceptos de ajuste [ "
								+ getValoresDefectoPermitirModificarConceptosAjuste(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoPermitirModificarConceptosAjuste(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasRestriccionCitasIncumplidas:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Días restricción de citas incumplidas [ "
								+ getValoresDefectoDiasRestriccionCitasIncumplidas(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValoresDefectoDiasRestriccionCitasIncumplidas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoInstitucionManejaMultasPorIncumplimiento:
				Log4JManager.info("valor nuevo: "+valorNuevo);
				Log4JManager.info("valor anteior: "+parametrosGenerales.get(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento));
				Log4JManager.info("valor: "+valor);
				Log4JManager.info("nombre:"+nombre);
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Institucion Maneja Multas Por Incumplimiento [ "
								+ getInstitucionManejaMultasPorIncumplimiento(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getInstitucionManejaMultasPorIncumplimiento(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoBloqueaCitasReservaAsignReprogPorIncump:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Bloquea CitasReservadas Asignadas Reprogramadas Por Incumplidas [ "
								+ getBloqueaCitasReservaAsignReprogPorIncump(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getBloqueaCitasReservaAsignReprogPorIncump(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoBloqueaAtencionCitasPorIncump:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Bloquea Atencion de Citas Por Incumplimiento [ "
								+ getBloqueaAtencionCitasPorIncump(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getBloqueaAtencionCitasPorIncump(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoFechaInicioControlMultasIncumplimientoCitas:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Fecha Inicio Control de Multas Incumplimiento de Citas [ "
								+ getFechaInicioControlMultasIncumplimientoCitas(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getFechaInicioControlMultasIncumplimientoCitas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValorMultaPorIncumplimientoCitas:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Valor Multa Por Incumplimiento de Citas [ "
								+ getValorMultaPorIncumplimientoCitas(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValorMultaPorIncumplimientoCitas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarFechaSolicitudPedidos:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Permitir Modificar Fecha Solicitud Pedidos [ "
								+ getPermitirModificarFechaSolicitudPedidos(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getPermitirModificarFechaSolicitudPedidos(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarFechaSolicitudTraslado:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'Permitir Modificar Fecha Solicitud Traslado [ "
								+ getPermitirModificarFechaSolicitudTraslado(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getPermitirModificarFechaSolicitudTraslado(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoCodigoManualEstandarBusquedaArticulos:
				if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos))) 
				{
					resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos,nombre, valor, institucion);
					if (resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "\tCódigo Manual Estándar Búsqueda Artículos [ "
								+ getCodigoManualEstandarBusquedaServicios(institucion) + " ]";
						
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "+ getCodigoManualEstandarBusquedaServicios(institucion) + " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoComentariosSolicitar:
				if (!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar))) 
				{
					resultado = valoresPorDefectoDao.modificar(	con,ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar,nombre, valor, institucion);
					if (resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "\t¿Hacer requerido comentarios al solicitar ? [ "
								+ getRequeridoComentariosSolicitar(institucion) + " ]";
						
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "+ getRequeridoComentariosSolicitar(institucion) + " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos:
				if(!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos))) {
					resultado=valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos, 
									nombre, 
									valor, 
									institucion);
					if(resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Permitir Modificar Tiempo Tratamiento Justificacion No POS [ "
								+ getPermitirModificarTiempoTratamientoJustificacionNopos(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidarRegistroEvolucionesGenerarOrdenes(institucion)
							+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log, 
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasResponderGlosas:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
								+ "'t Número Días Responder Glosas [ "
								+ getNumeroDiasResponderGlosas(institucion)
								+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getNumeroDiasResponderGlosas(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoGenerarAjusteAutoRegRespuesta:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Generar Ajuste Automatico desde el Registro de la Respuesta [ "
							+ getGenerarAjusteAutoRegRespuesta(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getGenerarAjusteAutoRegRespuesta(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;


			case ConstantesValoresPorDefecto.codigoValoresDefectoGenerarAjusteAutoRegRespuesConciliacion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Generar Ajuste Automatico desde el Registro de la Respuesta para Respuestas de Conciliación [ "
							+ getGenerarAjusteAutoRegRespuesConciliacion(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getGenerarAjusteAutoRegRespuesConciliacion(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;



			case ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpresionRespuesGlosa:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Formato Impresion Respuesta Glosa [ "
							+ getFormatoImpresionRespuesGlosa(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getFormatoImpresionRespuesGlosa(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
		
			case ConstantesValoresPorDefecto.codigoFormatoImpresionConciliacion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Formato Impresion Conciliacion [ "
							+ getFormatoImpresionConciliacion(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getFormatoImpresionConciliacion(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;

			case ConstantesValoresPorDefecto.codigoValidarGlosaReiterada:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValidarGlosaReiterada))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValidarGlosaReiterada,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Validar Glosa Reiterada [ "
							+ getValidarGlosaReiterada(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValidarGlosaReiterada+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidarGlosaReiterada(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;

			case ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasImpresionRespuesGlosa:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Imprimir Firmas en Impresion Respuesta de Glosa [ "
							+ getImprimirFirmasImpresionRespuesGlosa(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getImprimirFirmasImpresionRespuesGlosa(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidarAuditor:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Validar Auditor [ "
							+ getValidarAuditor(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidarAuditor(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidarUsuarioGlosa:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Validar Usuario Glosa [ "
							+ getValidarUsuarioGlosa(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getValidarUsuarioGlosa(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroGlosasRegistradasXFactura:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Número de Glosas Registradas Por Factura [ "
							+ getNumeroGlosasRegistradasXFactura(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getNumeroGlosasRegistradasXFactura(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//**
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejoOxigenoFurips:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getManejoOxigenoFurips(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejoOxigenoFurips(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasNotificarGlosa:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'t Número de Días Para Notificar Glosa [ "
							+ getNumeroDiasNotificarGlosa(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getNumeroDiasNotificarGlosa(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosCaducaCitaReservada:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Minutos Caduca Cita Reservada [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosCaducaCitaAsignadasReprogramadas:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Minutos Caduca Cita Asignadas y Reprogramadas [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEjecutarProcAutoActualizacionCitasOdo:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Ejecutar Proceso Automatico Actualizacion Citas Odontologicas [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecutarProcAutoActualizacionCitasOdo:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Producción En Paralelo Con Sistema Anterior [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMinutosEsperaAsignarCitasOdoCaducadas:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Minutos Espera Asignar Citas Odontologicas Caducadas [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoProduccionEnParaleloConSistemaAnterior:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Producción En Paralelo Con Sistema Anterior [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
							+ getProduccionEnParaleloConSistemaAnterior(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPostularFechasEnRespuestasDyT:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Postular Fechas en Respuestas DyT [ "
							+ getPostularFechasEnRespuestaDyT(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getPostularFechasEnRespuestaDyT(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasBusquedaReportes:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Numero de dias Busqueda Reportes [ "
							+ getNumeroDiasBusquedaReportes(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getNumeroDiasBusquedaReportes(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			


			/* 
			 * Tarea 70006
			 * Número de Dígitos Captura en Número de Identificación Pacientes
			 * num_digitos_captura_num_id_paciente
			 */
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumDigitosCaptNumIdPac:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Número de Dígitos Captura en Número de Identificación Pacientes [ "
							+ getNumDigCaptNumIdPac(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getNumDigCaptNumIdPac(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermIntOrdRespMulSinFin:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Permitir Interpretar Ordeens de Respuesta Múltiple sin Finalizar [ "
							+ getPermIntOrdRespMulSinFin(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getPermIntOrdRespMulSinFin(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequiereGlosaInactivar:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Requiere Glosa para Inactivar Factura [ "
							+ getRequiereGlosaInactivar(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getRequiereGlosaInactivar(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoAprobarGlosaRegistro:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t AprobarGlosaRegistro [ "
							+ getAprobarGlosaRegistro(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getAprobarGlosaRegistro(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;

			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTiemSegVeriInterShaioProc:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Tiempo en segundos que indica cada cuanto verificar interfaz Shaio por procesar [ "
							+ getTiemSegVeriInterShaioProc(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getTiemSegVeriInterShaioProc(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoUsuarioaReportarenSolicitAuto:
				if (!valorNuevo.equals(parametrosGenerales
						.get(ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto))) {
					resultado = valoresPorDefectoDao.modificar(con,
							ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto, nombre,
							valor, institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n" + "\tdireccionPaciente [ "
								+ getDireccionPaciente(institucion) + " ]";
						parametrosGenerales.put(
								ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto + "_"
										+ institucion, valorNuevo);
						log += "\n\tModificado por: [ "
								+ getDireccionPaciente(institucion) + " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
				
			case ConstantesValoresPorDefecto.codigonumeroMaximoDiasGenOrdenesAmbServicios:
			
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Numero Maximo de días para Generar Solicitudes de órdenes Ambulatorias de Servicios [ "
							+ getNumeroMaximoDiasGenOrdenesAmbServicios(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getNumeroMaximoDiasGenOrdenesAmbServicios(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break; 
			
			case ConstantesValoresPorDefecto.codigoMultiploMinGeneracionCita:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.multiploMinGeneracionCita))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.multiploMinGeneracionCita,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Múltiplo en minutos para generación de citas [ "
							+ getMultiploMinGeneracionCita(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getMultiploMinGeneracionCita(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoNumDiasAntFActualAgendaOd:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
							+ "'\t Número de días anteriores a la fecha actual para mostrar agenda odontolígica [ "
							+ getNumDiasAntFActualAgendaOd(institucion)
							+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
							+ getNumDiasAntFActualAgendaOd(institucion)
							+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoManejoEspecialInstitucionesOdontologia:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Manejo Especial Instituciones Odontología [ "
						+ getManejoEspecialInstitucionesOdontologia(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequiereGlosaInactivar(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoMaximoNumeroCuotasFinanciacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Máximo número de cuotas que se financia [ "
						+ getMaximoNumeroCuotasFinanciacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMaximoNumeroCuotasFinanciacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoMaximoNumeroDiasFinanciacionPorCuota:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Máximo número de días que se financia una cuota [ "
						+ getMaximoNumeroDiasFinanciacionPorCuota(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMaximoNumeroDiasFinanciacionPorCuota(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoFormatoDocumentosGarantia_Pagare:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Formato manejado como pagare [ "
						+ getFormatoDocumentosGarantia_Pagare(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoDocumentosGarantia_Pagare(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoOcupacionOdontologo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreOcupacionOdontologo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreOcupacionOdontologo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Ocupación Odontólogo [ "
						+ getOcupacionOdontologo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreOcupacionOdontologo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getOcupacionOdontologo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoOcupacionAuxiliarOdontologo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Ocupación Auxiliar de Odontólogo [ "
						+ getOcupacionAuxiliarOdontologo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getOcupacionAuxiliarOdontologo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoEdadFinalNinez:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreEdadFinalNinez))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreEdadFinalNinez,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Edad Final Niñez [ "
						+ getEdadFinalNinez(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEdadFinalNinez+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEdadFinalNinez(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoEdadInicioAdulto:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreEdadInicioAdulto))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreEdadInicioAdulto,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Edad Inicial Adulto [ "
						+ getEdadInicioAdulto(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreEdadFinalNinez+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEdadInicioAdulto(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoObligarRegIncapaPacienteHospitalizado:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Obligar Registrar Incapacidad Paciente Hospitalizado [ "
						+ getObligarRegIncapaPacienteHospitalizado(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getObligarRegIncapaPacienteHospitalizado(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEscalaPerfilPaciente:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Escala Paciente Perfil (NED) [ "
						+ getEscalaPacientePerfil(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEscalaPacientePerfil(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoUtilizanProgramasOdontologicosEnInstitucion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Utilizan programas Odontológicos en la Institución? [ "
						+ getUtilizanProgramasOdontologicosEnInstitucion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getUtilizanProgramasOdontologicosEnInstitucion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTiempoVigenciaPresupuestoOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TiempoVigenciaPresupuestoOdo [ "
						+ getTiempoVigenciaPresupuestoOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getTiempoVigenciaPresupuestoOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermiteCambiarServiciosCitaAtencionOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PermiteCambiarServiciosCitaAtencionOdo [ "
						+ getPermiteCambiarServiciosCitaAtencionOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPermiteCambiarServiciosCitaAtencionOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidaPresupuestoOdoContratado:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ValidaPresupuestoOdoContratado [ "
						+ getValidaPresupuestoOdoContratado(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getValidaPresupuestoOdoContratado(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ConveniosAMostrarXDefectoPresupuestoOdo [ "
						+ getConveniosAMostrarXDefectoPresupuestoOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getConveniosAMostrarXDefectoPresupuestoOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TiempoMaxEsperaInactivarPresupuestoOdoSuspTemp [ "
						+ getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EjecutarProcesoAutoActualizacionEstadosOdo [ "
						+ getEjecutarProcesoAutoActualizacionEstadosOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEjecutarProcesoAutoActualizacionEstadosOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t HoraEjecutarProcesoAutoActualizacionEstadosOdo [ "
						+ getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHoraEjecutarProcesoAutoActualizacionEstadosOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t MotivoCancelacionPresupuestoSuspendidoTemp [ "
						+ getMotivoCancelacionPresupuestoSuspendidoTemp(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMotivoCancelacionPresupuestoSuspendidoTemp(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t MaximoTiempoSinEvolucionarParaInactivarPlanTratamiento [ "
						+ getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPrioridadParaAplicarPromocionesOdo:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PrioridadParaAplicarPromocionesOdo [ "
						+ getPrioridadParaAplicarPromocionesOdo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPrioridadParaAplicarPromocionesOdo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasParaDefinirMoraXDeudaPacientes:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasParaDefinirMoraXDeudaPacientes [ "
						+ getDiasParaDefinirMoraXDeudaPacientes(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasParaDefinirMoraXDeudaPacientes(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion [ "
						+ getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoCuentaContablePagare:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t CuentaContablePagare [ "
						+ getCuentaContablePagare(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getCuentaContablePagare(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionRegistraAtencionExterna:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t Institución Registra Atención Externa [ "
						+ getInstitucionRegistraAtencionExterna(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getInstitucionRegistraAtencionExterna(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoCuentaContableLetra:
	
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t CuentaContableLetra [ "
						+ getCuentaContableLetra(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getCuentaContableLetra(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//Anexo 992
			case ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasImpresionCCCapitacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ImprimirFirmasImpresionCCCapitacion [ "
						+ getImprimirFirmasImpresionCCCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getImprimirFirmasImpresionCCCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EncabezadoFormatoImpresionFacturaOCCCapitacion [ "
						+ getEncabezadoFormatoImpresionFacturaOCCCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEncabezadoFormatoImpresionFacturaOCCCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PiePaginaFormatoImpresionFactuaOCCCapitacion [ "
						+ getPiePaginaFormatoImpresionFacturaOCCCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPiePaginaFormatoImpresionFacturaOCCCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasEnImpresionCC:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ImprimirFirmasEnImpresionCC [ "
						+ getImprimirFirmasEnImpresionCC(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getImprimirFirmasEnImpresionCC(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t NumeroMesesAMostrarEnReportesPresupuestoCapitacion [ "
						+ getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//Fin anexo 992
			
			
			//Anexo 958
			case ConstantesValoresPorDefecto.codigoValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ReciboCajaAutomaticoGeneracionFacturaVaria [ "
						+ getReciboCajaAutomaticoGeneracionFacturaVaria(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getReciboCajaAutomaticoGeneracionFacturaVaria(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoConceptoIngresoFacturasVarias:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoConceptoIngresoFacturasVarias))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoConceptoIngresoFacturasVarias,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ConceptoIngresoFacturasVarias [ "
						+ getConceptoIngresoFacturasVarias(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getConceptoIngresoFacturasVarias(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;

			//Fin anexo 958
			
			
			//Anexo 888
			case ConstantesValoresPorDefecto.codigoValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EsRequeridoProgramarCitaAlContratarPresupuestoOdon [ "
						+ getReciboCajaAutomaticoGeneracionFacturaVaria(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t MotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico [ "
						+ getReciboCajaAutomaticoGeneracionFacturaVaria(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			//Fin anexo 888
			
			//Anexo 888 Pt II
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon , nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t RequeridoProgramarCitaControlAlTerminarPresupuestoOdon [ "
						+ getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t RequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio [ "
						+ getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon , nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon [ "
						+ getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaFacturacionAutomatica:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t InstitucionManejaFacturacionAutomatica [ "
						+ getInstitucionManejaFacturacionAutomatica(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getInstitucionManejaFacturacionAutomatica(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			//Fin anexo 888 Pt II
			
			//Anexo 959
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivoFacturaPorCentroAtencion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ManejaConsecutivoFacturaPorCentroAtencion [ "
						+ getManejaConsecutivoFacturaPorCentroAtencion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaConsecutivoFacturaPorCentroAtencion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ManejaConsecutivosTesoreriaPorCentroAtencion [ "
						+ getManejaConsecutivosTesoreriaPorCentroAtencion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaConsecutivosTesoreriaPorCentroAtencion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoTamanioImpresionRC:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getTamanioImpresionRC(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getTamanioImpresionRC(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAperturaCierreCaja:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getRequiereAperturaCierreCaja(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequiereAperturaCierreCaja(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarProximaCitaEnAtencion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t RequeridoProgramarProximaCitaEnAtencion [ "
						+ getRequeridoProgramarProximaCitaEnAtencion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequeridoProgramarProximaCitaEnAtencion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			
			
			
			//-----------------------------
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getActivarBotonGenerarSolicitudOrdenAmbulatora(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getActivarBotonGenerarSolicitudOrdenAmbulatora(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaCajaPrincipal:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getInstitucionManejaCajaPrincipal(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getInstitucionManejaCajaPrincipal(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getInstitucionManejaTrasladoOtraCajaRecaudo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getInstitucionManejaTrasladoOtraCajaRecaudo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaEntregaATransportadoraValores:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getInstitucionManejaEntregaATransportadoraValores(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getInstitucionManejaEntregaATransportadoraValores(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoControlarAbonoPacientePorNroIngreso:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getControlarAbonoPacientePorNroIngreso(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getControlarAbonoPacientePorNroIngreso(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getValidaEstadoContratoNominaALosProfesionalesSalud(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getValidaEstadoContratoNominaALosProfesionalesSalud(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaInterfazUsuariosSistemaERP:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getManejaInterfazUsuariosSistemaERP(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaInterfazUsuariosSistemaERP(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequierGenerarSolicitudCambioServicio:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getRequierGenerarSolicitudCambioServicio(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequierGenerarSolicitudCambioServicio(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			//**
			
			case ConstantesValoresPorDefecto.codigoManejaVentaTarjetaClienteOdontosinEmision:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getManejaVentaTarjetaClienteOdontosinEmision(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaVentaTarjetaClienteOdontosinEmision(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//**
			case ConstantesValoresPorDefecto.codigoValoresDefectoLasCitasDeControlSePermitenAsignarA:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getLasCitasDeControlSePermitenAsignarA(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getLasCitasDeControlSePermitenAsignarA(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//**
			case ConstantesValoresPorDefecto.codigoValorDefectoMostrarGraficaCalculoIndicePlaca:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getMostrarGraficaCalculoIndicePlaca(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMostrarGraficaCalculoIndicePlaca(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//**
			case ConstantesValoresPorDefecto.codigoValoresDefectoValidarPacienteParaVentaTarjeta:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getValidarPacienteParaVentaTarjeta(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getValidarPacienteParaVentaTarjeta(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			//**
			case ConstantesValoresPorDefecto.codigoValoresDefectoReciboCajaAutomaticoVentaTarjeta:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getReciboCajaAutomaticoVentaTarjeta(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getReciboCajaAutomaticoVentaTarjeta(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				
				
				
				
				
			break;
		    

            
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas:
                if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas)))
                {
                        resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas, nombre,valor,institucion);
                        if(resultado)
                        {
                                log = "PARAMETRO MODIFICADO\n"
                                + "'\t TamanioImpresionRC [ "
                                + getPermitirRegistrarReclamacionCuentasNoFacturadas(institucion)
                                + " ]";
                                parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas+ "_" + institucion, valorNuevo);
                                log += "\n\t Modificado por: [ "
                                + getPermitirRegistrarReclamacionCuentasNoFacturadas(institucion)
                                + " ]";
                                LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
                        }
                }
                
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes:
                if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes)))
                {
                        resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes, nombre,valor,institucion);
                        if(resultado)
                        {
                                log = "PARAMETRO MODIFICADO\n"
                                + "'\t TamanioImpresionRC [ "
                                + getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)
                                + " ]";
                                parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes+ "_" + institucion, valorNuevo);
                                log += "\n\t Modificado por: [ "
                                + getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)
                                + " ]";
                                LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
                        }
                }
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso:
                if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso)))
                {
                        resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso, nombre,valor,institucion);
                        if(resultado)
                        {
                                log = "PARAMETRO MODIFICADO\n"
                                + "'\t TamanioImpresionRC [ "
                                + getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(institucion)
                                + " ]";
                                parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso+ "_" + institucion, valorNuevo);
                                log += "\n\t Modificado por: [ "
                                + getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(institucion)
                                + " ]";
                                LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
                        }
                }
			case ConstantesValoresPorDefecto.codigoValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero:
                if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero)))
                {
                        resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero, nombre,valor,institucion);
                        if(resultado)
                        {
                                log = "PARAMETRO MODIFICADO\n"
                                + "'\t TamanioImpresionRC [ "
                                + getMostrarAdminMedicamentosArticulosDespachoCero(institucion)
                                + " ]";
                                parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero+ "_" + institucion, valorNuevo);
                                log += "\n\t Modificado por: [ "
                                + getPermitirRegistrarReclamacionCuentasNoFacturadas(institucion)
                                + " ]";
                                LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
                        }
                }
			
			//**
			case ConstantesValoresPorDefecto.codigoValoresDefectoCancelaCitasOdontologicasReprogramar:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con, ConstantesValoresPorDefecto.nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar, nombre,valor,institucion);
					
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getAlCancelarCitasOdontoDejarAutoEstadoReprogramar(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getAlCancelarCitasOdontoDejarAutoEstadoReprogramar(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				
			break;
			
			
			//** Case Formato factura vara
			case ConstantesValoresPorDefecto.codigoValoresDefectoFormatoFacturaVaria:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria))) 
				{
					resultado=valoresPorDefectoDao.modificar(con, ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria, nombre,valor,institucion);
					
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getFormatoFacturaVaria(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoFacturaVaria(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoModificarFechaHoraInicioAtencionOdonto:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ModificarFechaHoraInicioAtencionOdonto [ "
						+ getModificarFechaHoraInicioAtencionOdonto(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getModificarFechaHoraInicioAtencionOdonto(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada:
				
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada,nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada [ "
						+ getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
		
			case ConstantesValoresPorDefecto.codigoValoresDefectoPrioridadEntidadSubcontratada:
				if (!valorNuevo
						.equals(parametrosGenerales
								.get(ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada))) {
					resultado = valoresPorDefectoDao
							.modificar(
									con,
									ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada,
									nombre, valor, institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n"
								+ "\tPrioridad entidad subcontratada [ "
								+ getPrioridadEntidadSubcontratada(institucion)
								+ " ]";
						parametrosGenerales
								.put(
										ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada
												+ "_" + institucion, valorNuevo);
						log += "\n\tModificado por: [ "
								+ getPrioridadEntidadSubcontratada(institucion)
								+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t RequiereAutorizacionCapitacionSubcontratadaParaFacturar [ "
						+ getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
				
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ManejaConsecutivoFacturasVariasPorCentroAtencion [ "
						+ getManejaConsecutivoFacturasVariasPorCentroAtencion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaConsecutivoFacturasVariasPorCentroAtencion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpresionAutorEntidadSub:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t FormatoImpresionAutorizacionEntidadSubcontratada [ "
						+ getFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;		
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEncFormatoImpresionAutorEntidadSub:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EncabezadoFormatoImpresionAutorizacionEntidadSubcontratada [ "
						+ getFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPiePagFormatoImpresionAutorEntidadSub:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PiePaginaFormatoImpresionAutorizacionEntidadSubcontratada [ "
						+ getPiePagFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPiePagFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigenciaAutorIndicativoTemp:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigenciaAutorIndicativoTemp [ "
						+ getPiePagFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPiePagFormatoImpresionAutorEntidadSub(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasProrrogaAutorizacion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasProrrogaAutorizacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasProrrogaAutorizacion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasProrrogaAutorizacion [ "
						+ getDiasProrrogaAutorizacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasProrrogaAutorizacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasCalcularFechaVencAutorizacionServicio:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasCalcularFechaVencimientoAutorizacionServicio [ "
						+ getDiasCalcularFechaVencAutorizacionServicio(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasCalcularFechaVencAutorizacionServicio(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasCalcularFechaVencAutorizacionArticulo:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasCalcularFechaVencimientoAutorizacionArticulo [ "
						+ getDiasCalcularFechaVencAutorizacionArticulo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasCalcularFechaVencAutorizacionArticulo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigentesNuevaAutorizacionEstanciaServiciosArticulos [ "
						+ getDiasVigentesNuevaAutorizacionEstanciaSerArt(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasVigentesNuevaAutorizacionEstanciaSerArt(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraProcesoCierreCapitacion:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t HoraProcesoCierreCapitacion [ "
						+ getHoraProcesoCierreCapitacion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHoraProcesoCierreCapitacion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
						
						AdministradorCron.modificarProcesoCierrePresupuestoAutomatico(institucion);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirfacturarReclamCuentaRATREC:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PermitirfacturarReclamacionCuentasRAT_REC [ "
						+ getPermitirfacturarReclamCuentaRATREC(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPermitirfacturarReclamCuentaRATREC(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;		
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoMostrarDetalleGlosasFacturaSolicFactura:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t MostrarDetalleGlosasFacturaSolicFactura [ "
						+ getMostrarDetalleGlosasFacturaSolicFactura(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMostrarDetalleGlosasFacturaSolicFactura(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;	
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpReservaCitaOdonto:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t FormatoImpReservaCitaOdonto [ "
						+ getFormatoImpReservaCitaOdonto(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoImpReservaCitaOdonto(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpAsignacionCitaOdonto:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t FormatoImpAsignacionCitaOdonto [ "
						+ getFormatoImpAsignacionCitaOdonto(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFormatoImpAsignacionCitaOdonto(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoFechaInicioCierreOrdenMedica:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t FechaInicioCierreOrdenMedica [ "
						+ getFechaInicioCierreOrdenMedica(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getFechaInicioCierreOrdenMedica(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTariServiciosValorizarOrden:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EsquemaTariServiciosValorizarOrden [ "
						+ getEsquemaTariServiciosValorizarOrden(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEsquemaTariServiciosValorizarOrden(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t SolicitudCitaInterconsultaOdontoCitaProgramada [ "
						+ getSolicitudCitaInterconsultaOdontoCitaProgramada(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getSolicitudCitaInterconsultaOdontoCitaProgramada(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t HoraEjecProcesoInactivarUsuarioInacSistema [ "
						+ getHoraEjecProcesoInactivarUsuarioInacSistema(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHoraEjecProcesoInactivarUsuarioInacSistema(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjeProcesoCaduContraInacSistema:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t HoraEjeProcesoCaduContraInacSistema [ "
						+ getHoraEjeProcesoCaduContraInacSistema(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHoraEjeProcesoCaduContraInacSistema(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigenciaContraUsuario:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigenciaContraseñaUsuario [ "
						+ getDiasVigenciaContraUsuario(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasVigenciaContraUsuario(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasFinalesVigenciaContraMostrarAlerta:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigenciaContraseñaUsuario [ "
						+ getDiasFinalesVigenciaContraMostrarAlerta(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasFinalesVigenciaContraMostrarAlerta(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoNumMaximoReclamacionesAccEventoXFactura:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigenciaContraseñaUsuario [ "
						+ getNumMaximoReclamacionesAccEventoXFactura(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getNumMaximoReclamacionesAccEventoXFactura(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasVigenciaContraseñaUsuario [ "
						+ getHacerRequeridoValorAbonoAplicadoAlFacturar(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHacerRequeridoValorAbonoAplicadoAlFacturar(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTariMedicamentosValorizarOrden:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t EsquemaTariMedicamentosValorizarOrden [ "
						+ getEsquemaTariMedicamentosValorizarOrden(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getEsquemaTariMedicamentosValorizarOrden(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarDatosUsuariosCapitados:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PermitirModificarDatosUsuariosCapitados [ "
						+ getPermitirModificarDatosUsuariosCapitados(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPermitirModificarDatosUsuariosCapitados(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t PermitirModificarDatosUsuariosCapitadosModificarCuenta [ "
						+ getPermitirModificarDatosUsuariosCapitadosModificarCuenta(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPermitirModificarDatosUsuariosCapitadosModificarCuenta(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaHojaAnestesia:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ManejaHojaAnestesia [ "
						+ getManejaHojaAnestesia(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaHojaAnestesia(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t HacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual [ "
						+ getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion:
				if(!valorNuevo.equals(parametrosGenerales.get		(ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t ManejaConsecutivosNotasPacientesCentroAtencion [ "
						+ getManejaConsecutivosNotasPacientesCentroAtencion(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getManejaConsecutivosNotasPacientesCentroAtencion(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;

			case ConstantesValoresPorDefecto.codigoValoresDefectoNaturalezaNotasPacientesManejar:
				if(!valorNuevo.equals(parametrosGenerales.get		(ConstantesValoresPorDefecto.codigoValoresDefectoNaturalezaNotasPacientesManejar))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoNaturalezaNotasPacientesManejar, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t NaturalezaNotasPacientesManejar [ "
						+ getNaturalezaNotasPacientesManejar(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoNaturalezaNotasPacientesManejar+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getNaturalezaNotasPacientesManejar(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoPermitirRecaudosCajaMayor:
				if(!valorNuevo.equals(parametrosGenerales.get		(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getPermitirRecaudosCajaMayor(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getPermitirRecaudosCajaMayor(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			
			case ConstantesValoresPorDefecto.codigoValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias:
				if (!valorNuevo.equals(parametrosGenerales
						.get(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias))) {
					resultado = valoresPorDefectoDao.modificar(con,
							ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias, valor, nombre,
							institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
								+ getViaIngresoValidacionesOrdenesAmbulatorias(institucion) + " ]";
						parametrosGenerales.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias + "_"
										+ institucion, valorNuevo);
						log += "\n\tModificado por: [ " + getViaIngresoValidacionesOrdenesAmbulatorias(institucion)
								+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
				
				
			case ConstantesValoresPorDefecto.codigoValoresDefectoViaIngresoValidacionesPeticiones:
				if (!valorNuevo.equals(parametrosGenerales
						.get(ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones))) {
					resultado = valoresPorDefectoDao.modificar(con,
							ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones, valor, nombre, 
							institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
								+ getViaIngresoValidacionesPeticiones(institucion) + " ]";
						parametrosGenerales.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones + "_"
										+ institucion, valorNuevo);
						log += "\n\tModificado por: [ " + getViaIngresoValidacionesPeticiones(institucion)
								+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
				
			case ConstantesValoresPorDefecto.codigoValoresDefectoMaximoDiasConsultarIngresosHistoriaClinica:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso, valor,nombre,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getMaximoDiasConsultarIngresos(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMaximoDiasConsultarIngresos(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasMaxProrrogaAutorizacionArticulo:
				if(!valorNuevo.equals(parametrosGenerales.get		(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t DiasMaxProrrogaAutorizacionArticulo [ "
						+ getDiasMaxProrrogaAutorizacionArticulo(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasMaxProrrogaAutorizacionArticulo(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoDiasMaxProrrogaAutorizacionServicio:
				if(!valorNuevo.equals(parametrosGenerales.get		(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio, nombre,valor,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getDiasMaxProrrogaAutorizacionServicio(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getDiasMaxProrrogaAutorizacionServicio(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias:
				if (!valorNuevo.equals(parametrosGenerales
						.get(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias))) {
					resultado = valoresPorDefectoDao.modificar(con,
							ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias, valor, nombre,
							institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
								+ getTipoPacienteValidacionesOrdenesAmbulatorias(institucion) + " ]";
						parametrosGenerales.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias + "_"
										+ institucion, valorNuevo);
						log += "\n\tModificado por: [ " + getTipoPacienteValidacionesOrdenesAmbulatorias(institucion)
								+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
				
				
			case ConstantesValoresPorDefecto.codigoValoresDefectoTipoPacienteValidacionesPeticiones:
				if (!valorNuevo.equals(parametrosGenerales
						.get(ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones))) {
					resultado = valoresPorDefectoDao.modificar(con,
							ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones, valor, nombre, 
							institucion);
					if (resultado) {
						log = "PARAMETRO MODIFICADO\n" + "\ttipoId [ "
								+ getTipoPacienteValidacionesPeticiones(institucion) + " ]";
						parametrosGenerales.put(
								ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones + "_"
										+ institucion, valorNuevo);
						log += "\n\tModificado por: [ " + getTipoPacienteValidacionesPeticiones(institucion)
								+ " ]";
						LogsAxioma.enviarLog(
								ConstantesBD.logParametrosGeneralesCodigo, log,
								ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
				break;
			case ConstantesValoresPorDefecto.codigoValoresDefectoMesesMaxAdminAutoCapVencidas:
				if(!valorNuevo.equals(parametrosGenerales.get(ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas))) 
				{
					resultado=valoresPorDefectoDao.modificar(con,ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas, valor,nombre,institucion);
					if(resultado) 
					{
						log = "PARAMETRO MODIFICADO\n"
						+ "'\t TamanioImpresionRC [ "
						+ getMesesMaxAdminAutoCapVencidas(institucion)
						+ " ]";
						parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas+ "_" + institucion, valorNuevo);
						log += "\n\t Modificado por: [ "
						+ getMesesMaxAdminAutoCapVencidas(institucion)
						+ " ]";
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, log,ConstantesBD.tipoRegistroLogModificacion, usuario);
					}
				}
			break;
		}// switch
		if (!resultado) {
			Log4JManager.error("Error modificando el parámetro " + parametro);
		}
		return resultado;
	}

	

	

	/**
	 * Metodo para cargar las etiquetas de los parametros segun el modulo
	 * 
	 * @param con
	 *            Connection
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap cargarEtiquetas(Connection con) {
		HashMap mapa = new HashMap();
		mapa = valoresPorDefectoDao.cargarEtiquetas(con, modulo);
		return mapa;
	}

	/**
	 * metodo para generar el resumen
	 * 
	 * @param con
	 *            Connection
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap cargarResumen(Connection con, int institucion) {
		HashMap mapa = new HashMap();
		mapa = valoresPorDefectoDao.cargarResumenValores(con, modulo,
				institucion);
		return mapa;
	}

	/**
	 * @param con
	 * @return
	 */
	public static HashMap cargarResumenCentrosCostoTerceros(Connection con) {
		HashMap mapa = new HashMap();
		mapa = valoresPorDefectoDao.consultarCentrosCostoTerceros(con);
		return mapa;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList<Integer> cargarResumenServiciosManejoTransPrimario(int institucion,Connection con) {
		ArrayList<Integer>  resultado = new ArrayList<Integer>();
		resultado = valoresPorDefectoDao.consultarServiciosManejoTransPrimario(institucion,con);
		return resultado;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList<Integer> cargarResumenServiciosManejoTransSecundario(int institucion,Connection con) {
		ArrayList<Integer>  resultado = new ArrayList<Integer>();
		resultado = valoresPorDefectoDao.consultarServiciosManejoTransSecundario(institucion,con);
		return resultado;
	}
	
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap cargarResumenHorasReproceso(Connection con) {
		HashMap mapa = new HashMap();
		mapa = valoresPorDefectoDao.consultarHorasReproceso(con);
		return mapa;
	}

	/**
	 * M&eacute;todo que recarga el c&oacute;digo del cie actual
	 * 
	 * @param con
	 */
	public static void recargarCieActual(Connection con) {
		codigoTipoCieActual = valoresPorDefectoDao
				.cargarCodigoTipoCieActual(con);
	}

	/**
	 * @param con
	 * @return
	 */
	public static HashMap cargarResumenClasesInventariosPaqMatQx(Connection con) {
		HashMap mapa = new HashMap();
		mapa = valoresPorDefectoDao.consultarClasesInventariosPaqMatQx(con);
		return mapa;
	}
	
	/*
	 * -============================================Sets=====================================-
	 */

	/**
	 * @param causaExterna
	 *            Asigna causaExterna.
	 */
	public static void setCausaExterna(String causaExterna, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_"
				+ institucion, causaExterna);
	}

	/**
	 * @param centroCostoConsultaExterna
	 *            Asigna centroCostoConsultaExterna.
	 */
	public static void setCentroCostoConsultaExterna(
			String centroCostoConsultaExterna, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna
						+ "_" + institucion, centroCostoConsultaExterna);
	}
	
	/**
	 * @param convenioFisalud
	 *            Asigna convenioFisalud
	 */
	public static void setConvenioFisalud(String convenioFisalud, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud+ "_" + institucion, convenioFisalud);
	}

	/**
	 * @param 
	 *        
	 */
	public static void setFormaPagoEfectivo(String formaPagoEfectivo, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo+ "_" + institucion, formaPagoEfectivo);
	}
	
	/**
	 * 
	 * @param codigoManual
	 * @param institucion
	 */
	public static void setCodigoManualEstandarBusquedaServicios(String codigoManual, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios+ "_" + institucion, codigoManual);
	}
	
	/**
	 * @param centroCostoAmbulatorios
	 *            Asigna centroCostoAmbulatorios.
	 */
	public static void setCentroCostoAmbulatorios(
			String centroCostoAmbulatorios, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios + "_"
						+ institucion, centroCostoAmbulatorios);
	}

	/**
	 * @param ciudadVivienda
	 *            Asigna ciudadVivienda.
	 */
	public static void setCiudadVivienda(String ciudadVivienda, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia + "_"
						+ institucion, ciudadVivienda);
	}
	
	/**
	 * 
	 * @param paisResidencia
	 * @param institucion
	 */
	public static void setPaisResidencia(String paisResidencia,int institucion){
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia + "_"
						+ institucion, paisResidencia);
	}
	
	/**
	 * 
	 * @param paisNacimiento
	 * @param institucion
	 */
	public static void setPaisNacimiento(String paisNacimiento,int institucion){
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento + "_"
						+ institucion, paisNacimiento);
	}
	

	/**
	 * @param ciudadNacimiento
	 *            Asigna ciudadNacimiento.
	 */
	public static void setCiudadNacimiento(String ciudadNacimiento,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento + "_"
						+ institucion, ciudadNacimiento);
	}

	/**
	 * @param direccionPaciente
	 *            Asigna direccionPaciente.
	 */
	public static void setDireccionPaciente(String direccionPaciente,
			int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDireccion
				+ "_" + institucion, direccionPaciente);
	}

	/**
	 * @param fechaNacimiento
	 *            Asigna fechaNacimiento.
	 */
	public static void setFechaNacimiento(String fechaNacimiento,
			int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad
				+ "_" + institucion, fechaNacimiento);
	}

	/**
	 * @param finalidad
	 *            Asigna finalidad.
	 */
	public static void setFinalidad(String finalidad, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta + "_"
						+ institucion, finalidad);
	}

	/**
	 * @param flagCentinela
	 *            Asigna flagCentinela.
	 */
	public static void setFlagCentinela(String flagCentinela, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCentinela
				+ "_" + institucion, flagCentinela);
	}

	/**
	 * @param historiaClinica
	 *            Asigna historiaClinica.
	 */
	public static void setHistoriaClinica(String historiaClinica,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica + "_"
						+ institucion, historiaClinica);
	}

	/**
	 * @param ocupacion
	 *            Asigna ocupacion.
	 */
	public static void setOcupacion(String ocupacion, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion
				+ "_" + institucion, ocupacion);
	}

	/**
	 * @param tipoId
	 *            Asigna tipoId.
	 */
	public static void setTipoId(String tipoId, int institucion) { //FIXME
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoTipoId + "_"
				+ institucion, tipoId);
	}

	/**
	 * @param codigoEstadoCama
	 *            Asigna codigoEstadoCama.
	 */
	public static void setCodigoEstadoCama(String codigoEstadoCama,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso + "_"
						+ institucion, codigoEstadoCama);
	}
	
	
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente
	/**
	 * @param carnetRequerido
	 *            Asigna carnetRequerido.
	 */
	/**
	public static void setCarnetRequerido(String carnetRequerido,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido + "_"
						+ institucion, carnetRequerido);
	}
	*/

	/**
	 * @param valorUVR
	 *            Asigna valorUVR.
	 */
	public static void setValorUVR(String valorUVR, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_"
				+ institucion, valorUVR);
	}

	/**
	 * @param ocupacionSolicitada
	 *            Asigna ocupacionSolicitada.
	 */
	public static void setOcupacionSolicitada(String ocupacionSolicitada,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada + "_"
						+ institucion, ocupacionSolicitada);
	}

	/**
	 * @param zonaDomicilio
	 *            Asigna zonaDomicilio.
	 */
	public static void setZonaDomicilio(String zonaDomicilio, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio
				+ "_" + institucion, zonaDomicilio);
	}

	/**
	 * @param centroCostoUrgencias
	 *            Asigna centroCostoUrgencias.
	 */
	public static void setCentroCostoUrgencias(String centroCostoUrgencias,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias + "_"
						+ institucion, centroCostoUrgencias);
	}

	/**
	 * @param validarEstadoSolicitudesInterpretadas
	 *            Asigna validarEstadoSolicitudesInterpretadas.
	 */
	public static void setValidarEstadoSolicitudesInterpretadas(
			String validarEstadoSolicitudesInterpretadas, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas + "_"
						+ institucion, validarEstadoSolicitudesInterpretadas);
	}

	/**
	 * El/La validarContratosVencidos a establecer.
	 * 
	 * @param validarContratosVencidos
	 */
	public void setValidarContratosVencidos(String validarContratosVencidos,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos + "_"
						+ institucion, validarContratosVencidos);
	}

	/**
	 * @param manejoTopesPaciente
	 *            Asigna manejoTopesPaciente.
	 */
	public static void setManejoTopesPaciente(String manejoTopesPaciente,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente + "_"
						+ institucion, manejoTopesPaciente);
	}

	/**
	 * @param justificacionServiciosRequerida
	 *            Asigna justificacionServiciosRequerida.
	 */
	public static void setJustificacionServiciosRequerida(
			String justificacionServiciosRequerida, int institucion) {
		parametrosGenerales
				.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida
								+ "_" + institucion,
						justificacionServiciosRequerida);
	}

	/**
	 * @param ingresoCantidadFarmacia
	 *            Asigna ingresoCantidadFarmacia.
	 */
	public static void setIngresoCantidadFarmacia(
			String ingresoCantidadFarmacia, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia + "_"
						+ institucion, ingresoCantidadFarmacia);
	}

	/**
	 * @param ripsPorFactura
	 *            Asigna ripsPorFactura.
	 */
	public static void setRipsPorFactura(String ripsPorFactura, int institucion) {
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura
				+ "_" + institucion, ripsPorFactura);
	}

	/**
	 * @param ExcepcionesRipsConsultorios
	 *            Asigna ExcepcionesRipsConsultorios.
	 */
	public static void setExcepcionesRipsConsultorios(
			String excepcionesRipsConsultorios, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios
						+ "_" + institucion, excepcionesRipsConsultorios);
	}

	/**
	 * @param AjustarCuentaCobroRadicada
	 *            Asigna AjustarCuentaCobroRadicada
	 */
	public static void setAjustarCuentaCobroRadicada(
			String ajustarCuentaCobroRadicada, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada
						+ "_" + institucion, ajustarCuentaCobroRadicada);
	}

	/**
	 * @param CerrarCuentaAnulacionFactura
	 *            Asigna CerrarCuentaAnulacionFactura
	 */
	public static void setCerrarCuentaAnulacionFactura(
			String cerrarCuentaAnulacionFactura, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura
						+ "_" + institucion, cerrarCuentaAnulacionFactura);
	}

	/**
	 * @param BarrioResidencia
	 *            Asigna BarrioResidencia
	 */
	public static void setBarrioResidencia(String barrioResidencia,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia + "_"
						+ institucion, barrioResidencia);
	}

	/**
	 * @param MaterialesPorActo
	 *            Asigna MaterialesPorActo
	 */
	public static void setMaterialesPorActo(String materialesPorActo,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo + "_"
						+ institucion, materialesPorActo);
	}

	/**
	 * @param CodigoOcupacionMedicoEspecialista
	 *            Asigna CodigoOcupacionMedicoEspecialista
	 */
	public static void setCodigoOcupacionMedicoEspecialista(
			String codigoOcupacion, int institucion) {
		parametrosGenerales
				.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista
								+ "_" + institucion, codigoOcupacion);
	}

	/**
	 * @param CodigoOcupacionMedicoGeneral
	 *            Asigna CodigoOcupacionMedicoGeneral
	 */
	public static void setCodigoOcupacionMedicoGeneral(String codigoOcupacion,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral
						+ "_" + institucion, codigoOcupacion);
	}

	/**
	 * @param CodigoOcupacionEnfermera
	 *            Asigna CodigoOcupacionEnfermera
	 */
	public static void setCodigoOcupacionEnfermera(String codigoOcupacion,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera + "_"
						+ institucion, codigoOcupacion);
	}

	/**
	 * @param CodigoOcupacionAuxiliarEnfermeria
	 *            Asigna CodigoOcupacionAuxiliarEnfermeria
	 */
	public static void setCodigoOcupacionAuxiliarEnfermeria(
			String codigoOcupacion, int institucion) {
		parametrosGenerales
				.put(
						ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria
								+ "_" + institucion, codigoOcupacion);
	}

	/**
	 * @param tipoConsecutivoCapitacion
	 *            Asigna tipoConsecutivoCapitacion
	 */
	public static void setTipoConsecutivoCapitacion(
			String tipoConsecutivoCapitacion, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion
						+ "_" + institucion, tipoConsecutivoCapitacion);
	}
	
	/**
	 * @param NumDiasTratamientoMedicamentos
	 *            Asigna NumDiasTratamientoMedicamentos
	 */
	public static void setNumDiasTratamientoMedicamentos(
			String numDiasTratamientoMedicamentos, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos
						+ "_" + institucion, numDiasTratamientoMedicamentos);
	}
	
	/**
	 * @param NumDiasEgresoOrdenesAmbulatorias
	 *            Asigna NumDiasEgresoOrdenesAmbulatorias
	 */
	public static void setNumDiasEgresoOrdenesAmbulatorias(
			String numDiasEgresoOrdenesAmbulatorias, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias
						+ "_" + institucion, numDiasEgresoOrdenesAmbulatorias);
	}
	
	/**
	 * @param NumDiasGenerarOrdenesArticulos
	 *            Asigna NumDiasGenerarOrdenesArticulos
	 */
	public static void setNumDiasGenerarOrdenesArticulos(
			String numDiasGenerarOrdenesArticulos, int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos
						+ "_" + institucion, numDiasGenerarOrdenesArticulos);
	}
	
	/**
	 * @param NumDiasEgresoOrdenesAmbulatorias Asigna interfazAbonosTesoreria
	 */
	public static void setInterfazAbonosTesoreria(String interfazAbonosTesoreria, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria+ "_" + institucion, interfazAbonosTesoreria);
	}
	
	/**
	 * @param NumDiasGenerarOrdenesArticulos Asigna interfazPaciente
	 */
	public static void setInterfazPaciente(String interfazPaciente, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente+ "_" + institucion, interfazPaciente);
	}
	
	/**
	 * 
	 * @param interfazCompras
	 * @param institucion
	 */
	public static void setInterfazCompras(String interfazCompras,int institucion)
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras+ "_" + institucion, interfazCompras);
	}
	
	/**
	 * 
	 * @param articuloInventario
	 * @param institucion
	 */
	public static void setArticuloInventario(String articuloInventario, int institucion)
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario+ "_" + institucion, articuloInventario);
	}
	
	/**
	 * 
	 * @param loginUsuario
	 * @param institucion
	 */
	public static void setLoginUsuario(String loginUsuario, int institucion)
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario+ "_" + institucion, loginUsuario);
	}
	
	/**
	 * @return Retorna Datos de Validar Edad Responsable Paciente
	 */
	public static String setValidarEdadResponsablePaciente(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadResponsablePaciente);
	}
	
	/**
	 * @return Retorna Datos de Validar Edad Deudor Paciente 
	 */
	public static String setValidarEdadDeudorPaciente(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente 
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadDeudorPaciente);
	}
	
	/**
	 * @return Retorna Datos de Validar Edad Deudor Paciente 
	 */
	public static String setAniosBaseEdadAdulta(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta 
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoAniosBaseEdadAdulta);
	}
	
	/**
	 * @return Retorna Datos de Validar Egreso Administrativo para Paquetizar
	 */
	public static String setValidarEgresoAdministrativoPaquetizar(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEgresoAdministrativoPaquetizar);
	}

	/**
	 * @return Retorna la Maxima Cantidad de Paquetes Validos por Ingreso Paciente
	 */
	public static String setMaxCantidPaquetesValidosIngresoPaciente(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMaxCantidPaquetesValidosIngresoPaciente);
	}
	
	/**
	 * @return Retorna lo referente a Asignar valor paciente con el valor de los cargos
	 */
	public static String setAsignarValorPacienteValorCargos(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos
						+ "_" + institucion, 
				ConstantesValoresPorDefecto.valorValoresDefectoAsignarValorPacienteValorCargos);
	}
	
	/**
	 * @return Retorna lo referente a Interfaz Cartera Pacientes 
	 */
	public static String setInterfazCarteraPacientes(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazCarteraPacientes);
	}
	
	/**
	 * @return Retorna lo referente a Interfaz Contables Facturas 
	 */
	public static String setInterfazContableFacturas(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazContableFacturas);
	}
	
	/**
	 * @return Retorna lo referente la Interfaz Terceros 
	 */
	public static String setInterfazTerceros(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazTerceros);
	}
	
	/**
	 * @return Retorna lo referente la Consolidar Cargos
	 */
	public static String setConsolidarCargos(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConsolidarCargos);
	}
	
	/**
	 * @return Retorna lo referente la Interfaz Terceros 
	 */
	public static String setManejaConversionMonedaExtranjera(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoManejaConversionMonedaExtranjera);
	}
	
	/**
	 * @return Retorna 
	 */
	public static String setImpresionMediaCarta(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneralNo);
	}
	
	
	/*
	 * -============================================Gets
	 * largos=====================================-
	 */
	/**
	 * @return Retorna manejoTopesPaciente.
	 */
	public static String getManejoTopesPacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoManejoTopesPaciente);
	}
	
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente
	/**
	 * @return Retorna carnetRequerido.
	 */
	/**
	public static String getValidarContratosVencidosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarContratosVencidos);
	}
	*/

	/**
	 * @return Retorna centroCostoUrgencias.
	 */
	public static String getCentroCostoUrgenciasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoUrgencias);
	}

	/**
	 * @return Retorna causaExterna.
	 */
	public static String getCausaExternaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCausaExterna);
	}

	/**
	 * @return Retorna centroCostoConsultaExterna.
	 */
	public static String getCentroCostoConsultaExternaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoConsultaExterna);
	}

	/**
	 * @return Retorna convenioFisalud
	 */
	public static String getConvenioFisaludLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConvenioFisalud);
	}
	
	/**
	 * @return Retorna formaPagoEfectivo
	 */
	public static String getFormaPagoEfectivoLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneral);
	}
	
	/**
	 * @return Retorna 
	 */
	public static String getCodigoManualEstandarBusquedaServiciosLargo(int institucion) 
	{
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneral);
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getNombreManualEstandarBusquedaServicios(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral)
					.split("@@")[1];
		} catch (Exception e) {
			valor = "CUPS";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getConfirmarAjustesPoolesLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConfirmarAjustesPooles);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getConceptoParaAjusteEntradaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteEntrada);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getConceptoParaAjusteSalidaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteSalida);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirModificarConceptosAjusteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarConceptosAjuste);
	}
	
	/**
	 * @return Retorna centroCostoAmbulatorios.
	 */
	public static String getCentroCostoAmbulatoriosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoAmbulatorios);
	}

	/**
	 * @return Retorna ciudadNacimiento.
	 */
	public static String getCiudadNacimientoLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCiudadNacimiento);
	}

	/**
	 * @return Retorna ciudadVivienda.
	 */
	public static String getCiudadViviendaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCiudadResidencia);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPaisResidenciaLargo(int institucion)
	{
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoPaisResidencia);
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPaisNacimientoLargo(int institucion)
	{
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoPaisNacimiento);
	}

	/**
	 * @return Retorna direccionPaciente.
	 */
	public static String getDireccionPacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoDireccion + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoDireccion);
	}

	/**
	 * @return Retorna fechaNacimiento.
	 */
	public static String getFechaNacimientoLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoIngresoEdad);
	}

	/**
	 * @return Retorna finalidad.
	 */
	public static String getFinalidadLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoFinalidadConsulta);
	}

	/**
	 * @return Retorna flagCentinela.
	 */
	public static String getFlagCentinelaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCentinela + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCentinela);
	}

	/**
	 * @return Retorna historiaClinica.
	 */
	public static String getHistoriaClinicaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoHistoriaClinica);
	}

	/**
	 * @return Retorna ocupacion.
	 */
	public static String getOcupacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoOcupacion);
	}

	/**
	 * @return Retorna tipoId.
	 */
	public static String getTipoIdLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoTipoId + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoTipoId);
	}

	/**
	 * @return Retorna Datos Cuenta Requerido Reserva Citas
	 */
	public static String setDatosCuentaRequeridoReservaCitas(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoDatosCuentaRequeridoReservaCitas);
	}

	/**
	 * @return Retorna red no adscrita
	 */
	public static String setRedNoAdscrita(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoRedNoAdscrita);
	}

	/**
	 * @return Retorna valorUVR.
	 */
	public static String getValorUVRLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValorUVR);
	}

	/**
	 * @return Retorna validarEstadoSolicitudesInterpretadas.
	 */
	public static String getValidarEstadoSolicitudesInterpretadasLargo(
			int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarInterpretadas);
	}

//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido
	/**
	 * @return Retorna carnetRequerido.
	 */
	/**
	public static String getCarnetRequeridoLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCarnetRequerido);
	}
	*/
	
	
	/**
	 * @return Retorna esquemaTarifarioAutocapitaSubCirugiasNoCurentos.
	 */
	public static String getEsquemaTarifarioAutocapitaSubCirugiasNoCurentosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos);
	}

	/**
	 * @return Retorna codigoEstadoCama.
	 */
	public static String getCodigoEstadoCamaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoEstadoCamaEgreso);
	}

	/**
	 * @return Retorna ocupacionSolicitada.
	 */
	public static String getOcupacionSolicitadaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoOcupacionSolicitada);
	}

	/**
	 * @return Retorna Datos Cuenta Requerido Reserva Citas
	 */
	public static String getDatosCuentaRequeridoReservaCitasLargo(
			int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoDatosCuentaRequeridoReservaCitas);
	}

	/**
	 * @return Retorna red no adscrita
	 */
	public static String getRednoAdscritaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoRedNoAdscrita);
	}
	
	/**
	 * @return Retorna zonaDomicilio.
	 */
	public static String getZonaDomicilioLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoDomicilio);
	}

	/**
	 * @param mostrarNombreJSP
	 *            Asigna mostrarNombreJSP.
	 */
	public static void setMostrarNombreJSP(boolean mostrarNombreJSP) {
		ValoresPorDefecto.mostrarNombreJSP = mostrarNombreJSP;
	}
	
	
	/**
	 * @param popUpNavegacion
	 *            Asigna popUpNavegacion.
	 */
	public static void setPopUpNavegacion(boolean popUpNavegacion) {
		ValoresPorDefecto.popUpNavegacion = popUpNavegacion;
	}
	

	/**
	 * @return Retorna justificacionServiciosRequerida
	 */
	public static String getJustificacionServiciosRequeridaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoJustificacionServiciosRequerida);
	}

	/**
	 * @return Retorna ingresoCantidadFarmacia
	 */
	public static String getIngresoCantidadFarmaciaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoIngresoCantidadFarmacia);
	}

	/**
	 * @return Retorna rips por factura
	 */
	public static String getRipsPorFacturaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoRipsPorFactura);
	}

	/**
	 * @return Retorna Excepciones rips consultorios
	 */
	public static String getExcepcionesRipsConsultoriosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoExcepcionRipsConsultorios);
	}

	/**
	 * @return Retorna Ajustar Cuenta Cobro Radicada
	 */
	public static String getAjustarCuentaCobroRadicadaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoAjustarCuentaCobroRadicada);
	}

	/**
	 * @return Retorna Indicativo de Cerrar Cuenta en Anulaci&oacute;n de la Factura
	 */
	public static String getCerrarCuentaAnulacionFacturaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCerrarCuentaAnulacionFactura);
	}

	/**
	 * @return Retorna Barrio de Residencia
	 */
	public static String getBarrioResidenciaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoBarrioResidencia);
	}

	/**
	 * @return Retorna Materiales por Acto
	 */
	public static String getMaterialesPorActoLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMaterialesPorActo);
	}

	/**
	 * @return Retorna tipoConsecutivoCapitacion
	 */
	public static String getTipoConsecutivoCapitacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoTipoConsecutivoCapitacion);
	}
	
	/**
	 * @return Retorna numDiasTratamientoMedicamentos
	 */
	public static String getNumDiasTratamientoMedicamentosLargo(int institucion) {
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasTratamientoMedicamentos).split("@@")[0];
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * @return Retorna numDiasGenerarOrdenesArticulos
	 */
	public static String getNumDiasGenerarOrdenesArticulosLargo(int institucion) 
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasGenerarOrdenesArticulos).split("@@")[0];
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * @return Retorna numDiasEgresoOrdenesAmbulatorias
	 */
	public static String getNumDiasEgresoOrdenesAmbulatoriasLargo(int institucion) 
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasEgresoOrdenesAmbulatorias).split("@@")[0];
		}
		catch(Exception e)
		{
			return "";
		}
		
	}
	
	/**
	 * @return Retorna horasCaducidadReferenciasExternas
	 */
	public static String getHorasCaducidadReferenciasExternasLargo(int institucion) 
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHorasCaducidadReferenciasExternas);
		}
		catch(Exception e)
		{
			return "";
		}
		
	}
	
	/**
	 * @return Retorna llamadoAutomaticoFuncionalidadReferencia
	 */
	public static String getLlamadoAutomaticoReferenciaLargo(int institucion) 
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLlamadoAutomaticoReferencia);
		}
		catch(Exception e)
		{
			return "";
		}
		
	}
	
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static String getInterfazPacientesLargo(int institucion) {
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazPaciente);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	

	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static String getInterfazAbonosTesoreriaLargo(int institucion) {
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazAbonosTesoreria);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static String getCrearCuentaAtencionCitasLargo(int institucion) {
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorCrearCuentaAtencionCitas);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazComprasLargo(int institucion)
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazCompras);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getArticuloInventarioLargo(int institucion)
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoArticuloInventario);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	public static String getLoginUsuarioLargo(int institucion)
	{
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLoginUsuario);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValidarEdadResponsablePacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadResponsablePaciente);
	}
	
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValidarEdadDeudorPacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente + "_"
						+ institucion, 
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadDeudorPaciente);
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getAniosBaseEdadAdultaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta + "_"
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoAniosBaseEdadAdulta);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getValidarEgresoAdministrativoPaquetizarLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar + "_"
						+ institucion, 
				ConstantesValoresPorDefecto.valorValoresDefectoValidarEgresoAdministrativoPaquetizar);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMaxCantidPaquetesValidosIngresoPacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMaxCantidPaquetesValidosIngresoPaciente);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getAsignarValorPacienteValorCargosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos + "_"
						+ institucion, 
				ConstantesValoresPorDefecto.valorValoresDefectoAsignarValorPacienteValorCargos);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazCarteraPacientesLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazCarteraPacientes);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazContableFacturasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazContableFacturas);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazTercerosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazTerceros);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getConsolidarCargosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoConsolidarCargos);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getManejaConversionMonedaExtranjeraLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoManejaConversionMonedaExtranjera);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getImpresionMediaCartaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneralNo);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHoraCorteHistoricoCamasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoHoraCorteHistoricoCamas);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMinutosLimiteAlertaReservaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaReserva);
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMinutosLimiteAlertaPacienteConSalidaUrgenciasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMinutosLimiteAlertaPacienteConSalidaHospitalizacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion);
	}
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMinutosLimiteAlertaPacientePorRemitirUrgenciasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMinutosLimiteAlertaPacientePorRemitirHospitalizacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getIdentificadorInstitucionArchivosColsanitasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoIdentificadorInstitucionArchivosColsanitas);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazRipsLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoInterfazRips);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getEntidadManejaHospitalDiaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoEntidadManejaHospitalDia);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getEntidadManejaRipsLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoEntidadManejaRips);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getUbicacionPlanosEntidadesSubcontratadasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoUbicacionPlanosEntidadesSubcontratadas);
	}
	
	/**	
	 * @param institucion
	 * @return
	 */
	public static String getRequeridaInfoRipsCagosDirectosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoRequeridaInfoRipsCargosDirectos);
	}
	
	/**	
	 * @param institucion
	 * @return
	 */
	public static String getAsignaValoracionCxAmbulaHospitaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado);
	}		
	
	
	
	/**
	 * @return Retorna
	 */
	public static String getValidacionOcupacionJustificacionNoPosArticulosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidacionOcupacionJustificacionNoPosArticulos);	
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValidacionOcupacionJustificacionNoPosServiciosLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoValidacionOcupacionJustificacionNoPosServicios);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getTiempoMaximoReingresoUrgenciasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorTiempoMaximoReingresoUrgencias);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getTiempoMaximoReingresoHospitalizacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorTiempoMaximoReingresoHospitalizacion);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getPermitirFacturarReingresosIndependientesLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorPermitirFacturarReingresosIndependientes);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getLiberarCamaHospitalizacionDespuesFacturarLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorLiberarCamaHospitalizacionDespuesFacturar);		
	}
	
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoPathArchivosPlanosFacturacionLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoPathArchivosPlanosFacturacion);		
	}

	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoPathArchivosPlanosFuripsLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneral);		
	}

	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoPathArchivosPlanosManejoPacienteLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoPathArchivosPlanosManejoPaciente);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoComprobacionDerechosCapitacionObligaLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoComprobacionDerCapitacionObliga);		
	}
	
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoRequiereAutorizarAnularFacturasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoRequiereAutorizarAnularFacturas);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoValidarPoolesFactLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValidarPoolesFact + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneral);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoMostrarEnviarEpicrisisEvolLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoGeneral);		
	}
	
	/**
	 * @param institucion
	 * @return
	 */
	public static String getTiemSegVeriInterShaioProcLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc + "_" 
						+ institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoTiemSegVeriInterShaioProc);		
	}
	
	/*
	 * -=======================================Gets==============================================-
	 */

	/**
	 * @return Retorna causaExterna.
	 */
	public static String getCausaExterna(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCausaExterna + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCausaExterna).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna centroCostoConsultaExterna.
	 */
	public static String getCentroCostoConsultaExterna(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoConsultaExterna
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoConsultaExterna)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna centroCostoConsultaExterna.
	 */
	public static String getConvenioFisalud(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConvenioFisalud
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConvenioFisalud)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna centroCostoConsultaExterna.
	 */
	public static String getFormaPagoEfectivo(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormaPagoEfectivo
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getCodigoManualEstandarBusquedaServicios(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaServicios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		
		if(UtilidadTexto.isEmpty(valor))
			valor=ConstantesBD.codigoTarifarioCups+"";
		
		return "0";
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getRequeridoComentariosSolicitar(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoComentariosSolicitar
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirModificarTiempoTratamientoJustificacionNopos(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		
		return valor;
	}


	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getConfirmarAjustesPooles(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConfirmarAjustesPooles
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConfirmarAjustesPooles)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHorasCaducidadReferenciasExternas(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHorasCaducidadReferenciasExternas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHorasCaducidadReferenciasExternas)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getLlamadoAutomaticoReferencia(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLlamadoAutomaticoReferencia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLlamadoAutomaticoReferencia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirConsultarEpicrisisSoloProfesionales(int institucion) 
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirConsultarEpicrisisSoloProfesionales + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirConsultarEpicrisisSoloProf).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMostrarAntecedentesParametrizadosEpicrisis(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAntecedentesParametrizadosEpicrisis + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMostrarAntecedentesParametrizadosEpicrisis).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazPaciente(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazPaciente
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazPaciente)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazAbonosTesoreria(int institucion) 
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazAbonosTesoreria
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazAbonosTesoreria)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazCompras(int institucion)
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCompras
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazCompras)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getArticuloInventario(int institucion)
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoArticuloInventario
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoArticuloInventario)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;	
	}
	
	public static String getLoginUsuario(int institucion)
	{
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLoginUsuario
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLoginUsuario)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna centroCostoAmbulatorios.
	 */
	public static String getCentroCostoAmbulatorios(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoAmbulatorios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoAmbulatorios)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna centroCostoUrgencias.
	 */
	public static String getCentroCostoUrgencias(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCentroCostoUrgencias + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCentroCostoUrgencias)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ciudadNacimiento.
	 */
	public static String getCiudadNacimiento(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCiudadNacimiento + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCiudadNacimiento).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ciudadVivienda.
	 */
	public static String getCiudadVivienda(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCiudadResidencia + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCiudadResidencia).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPaisResidencia(int institucion)
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPaisResidencia + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPaisResidencia).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPaisNacimiento(int institucion)
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPaisNacimiento + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPaisNacimiento).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	

	/**
	 * @return Retorna direccionPaciente.
	 */
	public static String getDireccionPaciente(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDireccion + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDireccion).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getAlCancelarCitasOdontoDejarAutoEstadoReprogramar(int institucion){
		
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresPorDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar+ "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoAlCancelarCitasOdontoDejarAutoEstadoReprogramar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
		
		
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getArchivosPlanosReportes(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPath + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPath).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getArchivosPlanosReportesInventarios(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPathInventarios + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPathInventarios).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna fechaNacimiento.
	 */
	public static String getFechaNacimiento(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIngresoEdad + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIngresoEdad).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna finalidad.
	 */
	public static String getFinalidad(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFinalidadConsulta + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoFinalidadConsulta).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna flagCentinela.
	 */
	public static String getFlagCentinela(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCentinela + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCentinela).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna historiaClinica.
	 */
	public static String getHistoriaClinica(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHistoriaClinica + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHistoriaClinica)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ocupacion.
	 */
	public static String getOcupacion(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoOcupacion + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoOcupacion).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna tipoId.
	 */
	public static String getTipoId(int institucion) { //FIXME
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTipoId + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTipoId).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna EsquemaTarifarioAutocapitaSubCirugiasNoCurentos.
	 */
	public static String getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(int institucion) { 
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica.
	 */
	public static String getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(int institucion) { 
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna zonaDomicilio.
	 */
	public static String getZonaDomicilio(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDomicilio + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDomicilio).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna valorUVR.
	 */
	public static String getValorUVR(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValorUVR + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValorUVR).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
//	Se deshabilita debido a q ya no se esta utilizando el parametro Carnet_Requerido en el modulo Manejo Paciente
	/**
	 * @return Retorna carnetRequerido.
	 */
	/**
	public static String getCarnetRequerido(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCarnetRequerido + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCarnetRequerido)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	*/

	/**
	 * @return Retorna codigoEstadoCama.
	 */
	public static String getCodigoEstadoCama(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEstadoCamaEgreso + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEstadoCamaEgreso).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ocupacionSolicitada.
	 */
	public static String getOcupacionSolicitada(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoOcupacionSolicitada + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoOcupacionSolicitada).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ocupacionSolicitada.
	 */
	public static String getDatosCuentaRequeridoReservaCitas(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDatosCuentaRequeridoReservaCitas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDatosCuentaRequeridoReservaCitas)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna red no adscrita.
	 */
	public static String getRedNoAdscrita(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRedNoAdscrita + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRedNoAdscrita);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna numero d&iacute;as tratamiento en solicitudes de medicamentos.
	 */
	public static String getNumDiasTratamientoMedicamentos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasTratamientoMedicamentos + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasTratamientoMedicamentos).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna n&uacute;mero m&aacute;ximo de d&iacute;as para generar solicitudes de ordenes ambulatorias de art&iacute;culos
	 */
	public static String getNumDiasGenerarOrdenesArticulos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasGenerarOrdenesArticulos + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasGenerarOrdenesArticulos).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna n&uacute;mero d&iacute;as egreso para generar ordenes ambulatorias
	 */
	public static String getNumDiasEgresoOrdenesAmbulatorias(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDiasEgresoOrdenesAmbulatorias + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDiasEgresoOrdenesAmbulatorias).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el/la validarContratosVencidos.
	 */
	public static String getValidarContratosVencidos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarContratosVencidos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarContratosVencidos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna manejoTopesPaciente.
	 */
	public static String getManejoTopesPaciente(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejoTopesPaciente + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoManejoTopesPaciente).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna validarEstadoSolicitudesInterpretadas.
	 */
	public static String getValidarEstadoSolicitudesInterpretadas(
			int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarInterpretadas + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarInterpretadas)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna justificacionServiciosRequerida
	 */
	public static String getJustificacionServiciosRequerida(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoJustificacionServiciosRequerida
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoJustificacionServiciosRequerida)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ingresoCantidadFarmacia
	 */
	public static String getIngresoCantidadFarmacia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadFarmacia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIngresoCantidadFarmacia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna ripsPorFactura
	 */
	public static String getRipsPorFactura(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRipsPorFactura + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRipsPorFactura).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna FechaCorteSaldoInicialC
	 */
	public static String getFechaCorteSaldoInicialC(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialC
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoFechaCorteSaldoInicialC)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna FechaCorteSaldoInicialCCapitacion
	 */
	public static String getFechaCorteSaldoInicialCCapitacion(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFechaCorteSaldoInicialCCapitacion
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoFechaCorteSaldoInicialCCapitacion)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna FechaCorteSaldoInicialC
	 */
	public static String getTopeConsecutivoCxCSaldoI(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoI
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTopeConsecutivoCxCSaldoI)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna
	 */
	public static String getTopeConsecutivoCxCSaldoICapitacion(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTopeConsecutivoCxCSaldoICapitacion
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTopeConsecutivoCxCSaldoICapitacion)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna MaxPageItems
	 */
	public static String getMaxPageItems(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMaxPageItems).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	
	/**
	 * @return Retorna MaxPageItems
	 */
	public  void setMaxPageItems(int institucion) {
			@SuppressWarnings("unused")
			String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItems + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMaxPageItems).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
	}
	
	/**
	 * @return Retorna MaxPageItems
	 */
	public static String getMaxPageItemsEpicrisis(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaxPageItemsEpicrisis + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMaxPageItemsEpicrisis).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna MaxPageItems
	 */
	public static int getMaxPageItemsInt(int institucion) 
	{
		int resultado=10;
		try
		{
			resultado=Integer.parseInt(getMaxPageItems(institucion));
		}
		catch(Exception e)
		{
			resultado=10;
		}
		return resultado;
	}
	
	/**
	 * @return Retorna MaxPageItems
	 */
	public static int getMaxPageItemsIntEpicrisis(int institucion) 
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		try
		{
			resultado=Integer.parseInt(getMaxPageItemsEpicrisis(institucion));
		}
		catch(Exception e)
		{
			resultado=ConstantesBD.codigoNuncaValido;
		}
		return resultado;
	}
	
	/**
	 * @return Retorna genExcepcionRipsConsultorios
	 */
	public static String getExcepcionRipsConsultorios(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoExcepcionRipsConsultorios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoExcepcionRipsConsultorios)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna getAjustarCuentaCobroRadicada
	 */
	public static String getAjustarCuentaCobroRadicada(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAjustarCuentaCobroRadicada
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAjustarCuentaCobroRadicada)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna getCerrarCuentaAnulacionFactura
	 */
	public static String getCerrarCuentaAnulacionFactura(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCerrarCuentaAnulacionFactura
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCerrarCuentaAnulacionFactura)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna getBarrioResidencia
	 */
	public static String getBarrioResidencia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoBarrioResidencia + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoBarrioResidencia).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna materialesPorActo
	 */
	public static String getMaterialesPorActo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaterialesPorActo + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMaterialesPorActo).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna La Hora Inicio Programaci&oacute;n Salas
	 */
	public static String getHoraInicioProgramacionSalas(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioProgramacionSalas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraInicialProgramacionSalas)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna Liquidacion automatica de cirugias
	 */
	public static String getLiquidacionAutomaticaCirugias(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaCirugias 
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLiquidacionAutomaticaCirugias)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getLiquidacionAutomaticaNoQx(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLiquidacionAutomaticaNoQx
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoLiquidacionAutomaticaNoQx)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getManejoProgramacionSalasSolicitudesDyt(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejoProgramacionSalasSolicitudes
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoManejoProgramacionSalasSolicitudes)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getRequeridaDescripcionEspecialidadCirugias(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadCirugias
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRequridaDescripcionEspecialidadCirugias)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getRequeridaDescripcionEspecialidadNoCruentos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequridaDescripcionEspecialidadNoCruentos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRequridaDescripcionEspecialidadNoCruentos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getAsocioAyudantia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioAyudantia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getAsocioAyudantiaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAyudantia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioAyudantia);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getIndicativoCobrableHonorariosCirugia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosCirugia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIndicativoCobrableHonorariosCirugia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getIndicativoCobrableHonorariosNoCruento(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIndicativoCobrableHonorariosNoCruento
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIndicativoCobrableHonorariosNoCruento)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getEntidadControlaDespachoSaldosMultidosis(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEntidadControlaDespachoSaldosMultidosis + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEntidadControlaDespachoSaldosMultidosis).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getNumeroDiasControlMedicamentosOrdenados(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasControMedOrdenados + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasControMedOrdenados).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * Return el getNumeroDiasResponderGlosas
	 * @param institucion
	 * @return
	 */
	public static String getNumeroDiasResponderGlosas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasResponderGlosas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * Return el getNumeroDiasResponderGlosasLargo
	 * @param institucion
	 * @return
	 */
	public static String getNumeroDiasResponderGlosasLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasResponderGlosas + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasResponderGlosas);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**	 * Return el getGenerarAjusteAutoRegRespuesta
	 * @param institucion
	 * @return	 */
	public static String getGenerarAjusteAutoRegRespuesta(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGenerarAjusteAutoRegRespuesta).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** * Return el getGenerarAjusteAutoRegRespuestaLargo
	 * @param institucion
	 * @return  */
	public static String getGenerarAjusteAutoRegRespuestaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesta + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGenerarAjusteAutoRegRespuesta);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}


	/**	 * Return el getGenerarAjusteAutoRegRespuesConciliacion
	 * @param institucion
	 * @return	 */
	public static String getGenerarAjusteAutoRegRespuesConciliacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGenerarAjusteAutoRegRespuesConciliacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** * Return el getGenerarAjusteAutoRegRespuesConciliacionLargo
	 * @param institucion
	 * @return  */
	public static String getGenerarAjusteAutoRegRespuesConciliacionLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGenerarAjusteAutoRegRespuesConciliacion + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGenerarAjusteAutoRegRespuesConciliacion);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}


	/**	 * Return el getFormatoImpresionRespuesGlosa
	 * @param institucion
	 * @return	 */
	public static String getFormatoImpresionRespuesGlosa(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoFormatoImpresionRespuesGlosa).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 * Return el getFormatoImpresionConciliacion
	 * @param institucion
	 * @return	 */
	public static String getFormatoImpresionConciliacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreFormatoImpresionConciliacion + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorFormatoImpresionConciliacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 * Return el getValidarGlosaReiterada
	 * @param institucion
	 * @return	 */
	public static String getValidarGlosaReiterada(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValidarGlosaReiterada + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValidarGlosaReiterada).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** * Return el getFormatoImpresionRespuesGlosaLargo
	 * @param institucion
	 * @return  */
	public static String getFormatoImpresionRespuesGlosaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionRespuesGlosa + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoFormatoImpresionRespuesGlosa);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}


	/**	 * Return el getImprimirFirmasImpresionRespuesGlosa
	 * @param institucion
	 * @return	 */
	public static String getImprimirFirmasImpresionRespuesGlosa(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoImprimirFirmasImpresionRespuesGlosa).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** * Return el getImprimirFirmasImpresionRespuesGlosaLargo
	 * @param institucion
	 * @return  */
	public static String getImprimirFirmasImpresionRespuesGlosaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionRespuesGlosa + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoImprimirFirmasImpresionRespuesGlosa);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 
	 * Return el getValidarAuditor
	 * @param institucion
	 * @return valor	 
	 * */
	public static String getValidarAuditor(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarAuditor).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** 
	 * Return el getValidarAuditorLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getValidarAuditorLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarAuditor + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarAuditor);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 
	 * Return el getValidarUsuarioGlosa
	 * @param institucion
	 * @return valor	 
	 * */
	public static String getValidarUsuarioGlosa(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarUsuarioGlosa).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** 
	 * Return el getValidarUsuarioGlosaLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getValidarUsuarioGlosaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarUsuarioGlosa + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarUsuarioGlosa);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 
	 * Return el getNumeroGlosasRegistradasXFactura
	 * @param institucion
	 * @return valor	 
	 * */
	public static String getNumeroGlosasRegistradasXFactura(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroGlosasRegistradasXFactura).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** 
	 * Return el getNumeroGlosasRegistradasXFacturaLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getNumeroGlosasRegistradasXFacturaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroGlosasRegistradasXFactura + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroGlosasRegistradasXFactura);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 
	 * Return el getNumeroDiasNotificarGlosa
	 * @param institucion
	 * @return valor	 
	 * */
	public static String getNumeroDiasNotificarGlosa(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasNotificarGlosa).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** 
	 * Return el getNumeroDiasNotificarGlosaLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getNumeroDiasNotificarGlosaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasNotificarGlosa + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasNotificarGlosa);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**	 
	 * Return el getProduccionEnParaleloConSistemaAnterior
	 * @param institucion
	 * @return valor	 
	 * */
	public static String getProduccionEnParaleloConSistemaAnterior(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoProduccionEnParaleloConSistemaAnterior).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/** 
	 * Return el getProduccionEnParaleloConSistemaAnteriorLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getProduccionEnParaleloConSistemaAnteriorLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoProduccionEnParaleloConSistemaAnterior + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoProduccionEnParaleloConSistemaAnterior);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	public static String getAsocioCirujano(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioCirujano)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getAsocioCirujanoLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioCirujano
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioCirujano);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getAsocioAnestesia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioAnestesia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getAsocioAnestesiaLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsocioAnestesia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsocioAnestesia);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getModificarInformacionDescripcionQuirurgica(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getHoraFinProgramacionSalas(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinProgramacionSalas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraFinalProgramacionSalas)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getMinutosRegistroNotasCirugia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasCirugia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosRegistroNotasCirugia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getMinutosRegistroNotasNoCruentos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosRegistroNotasNoCruentos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosRegistroNotasNoCruentos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getMinutosMaximosRegistroAnestesia(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosMaximosRegistroAnestesia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosMaximosRegistroAnestesia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getModificarInformacionQuirurgica(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoModificarInformacionQuirurgica
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoModificarInformacionQuirurgica)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	/**
	 * @return Retorna la Especialidad de anestesiolog&iacute;a
	 */
	public static String getEspecialidadAnestesiologia(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEspecialidadAnestesiologia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEspecialidadAnestesiologia);
			if (obtenerValorCorto) {
				valor = valor.split("@@")[0];
			}
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el manejo del consecutivo de transacciones inventario
	 */
	public static String getManejoConsecutivoTransInv(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejoConsecutivoTransInv
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoManejoConsecutivoTransInv)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el porcentaje de alerta en diferencia en los costos de
	 *         inventarios inventarios
	 */
	public static String getPorcentajeCostosInv(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajeAlertaCostosInv
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPorcentajeAlertaCostosInv)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para las
	 *         solicitudes de pacientes inventarios
	 */
	public static String getCodigoTransSoliPacientes(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransSoliPacientes
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransSoliPacientes);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para las
	 *         devoluciones de pacientes inventarios
	 */
	public static String getCodigoTransDevolPacientes(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPaciente
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransDevolucionesPaciente);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para los pedidos
	 *         inventarios
	 */
	public static String getCodigoTransaccionPedidos(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransPedidos + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransPedidos);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para la devoluci&oacute;n
	 *         de pedidos inventarios
	 */
	public static String getCodigoTransDevolucionPedidos(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionesPedidos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransDevolucionesPedidos);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para la comptra
	 */
	public static String getCodigoTransCompra(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransCompras
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransCompra);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	

	/**
	 * @return Retorna el c&oacute;digo de la transacci&oacute;n utilizado para la devoluci&oacute;n
	 *         de comptras
	 */
	public static String getCodigoTransDevolCompra(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransDevolucionCompras
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransDevolucionesPaciente);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el estado de la modificaci&oacute;n de la fecha de elaboraci&oacute;n
	 *         del inventario
	 */
	public static String getModificacionFechaInventario(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaaInventarios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoModificarFechaaInventarios)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el porcentaje de alerta del punto de pedido inventarios
	 */
	public static String getPorcentajePuntoPedido(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPorcentajePuntoPedido
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPorcentajePuntoPedido)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasAlertaVigencia(int institucion)
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasAlertaVigencia
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasAlertaVigencia)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el manejo de dias previos notificacion proxima cita de
	 *         control Consulta Externa
	 */
	public static String getDiasPreviosNotificacionProximoControl(
			int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasPreviosNotificacionProximoControl
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasPreviosNotificacionProximoControl)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el porcentaje de alerta del punto de pedido inventarios
	 */
	public static String getCodigoTransTrasladoAlmacenes(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoTransTrasladoAlmacenes
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTransTrasladoAlmacenes);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el cheuqeo autom&aacute;tico del campo "Informaci&oacute;n Adicional" en el
	 *         ingreso de Convenios
	 */
	public static String getInfoAdicIngresoConvenios(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInfoAdicIngresoConvenios
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInfoAdicIngresoConvenios)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el c&oacute;digo de la ocupaci&oacute;n m&eacute;dico especialista
	 */
	public static String getCodigoOcupacionMedicoEspecialista(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoEspecialista
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoOcupacionMedicoEspecialista);

			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el c&oacute;digo de la ocupaci&oacute;n m&eacute;dico general
	 */
	public static String getCodigoOcupacionMedicoGeneral(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionMedicoGeneral
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoOcupacionMedicoGeneral);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el c&oacute;digo de la ocupaci&oacute;n enfermera
	 */
	public static String getCodigoOcupacionEnfermera(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionEnfermera
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoOcupacionEnfermera);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return el c&oacute;digo de la ocupaci&oacute;n auxiliar enfermer&iacute;a
	 */
	public static String getCodigoOcupacionAuxiliarEnfermeria(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoOcupacionAuxiliarEnfermeria
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoOcupacionAuxiliarEnfermeria);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna historiaClinica.
	 */
	public static String getMinutosEsperaCitaCaduca(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosEsperaCitaCaduca
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoMinutosEsperaCitaCaduca)
					.split("@@")[0];

		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param institucion
	 * @return Retorna tiempo m&aacute;ximo grabaci&oacute;n de registros.
	 */
	public static String getTiempoMaximoGrabacion(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaximoGrabacion
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoTiempoMaximoGrabacion)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param institucion
	 * @return Retorna IngresoCantidadSolMedicamentos
	 */
	public static String getIngresoCantidadSolMedicamentos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIngresoCantidadSolMedicamentos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoIngresoCantidadSolMedicamentos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		if (!UtilidadCadena.noEsVacio(valor)) {
			valor = "true";
		}
		return valor;
	}

	/**
	 * @param institucion
	 * @return Retorna getModificarMinutosEsperaCuentasProcFact
	 */
	public static String getModificarMinutosEsperaCuentasProcFact(
			int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoModificarMinutosEsperaCuentasProcFact
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoModificarMinutosEsperaCuentasProcFact)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @param institucion
	 * @return Retorna tipo de consecutivo de capitaci&oacute;n
	 */
	public static String getTipoConsecutivoCapitacion(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoCapitacion
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTipoConsecutivoCapitacion)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna Validar edad responsable paciente.
	 */
	public static String getValidarEdadResponsablePaciente(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadResponsablePaciente
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadResponsablePaciente)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Validar edad deudor paciente
	 */
	public static String getValidarEdadDeudorPaciente(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarEdadDeudorPaciente
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarEdadDeudorPaciente)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna A&ntilde;os Base Edad Adulta
	 */
	public static String getAniosBaseEdadAdulta(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAniosBaseEdadAdulta + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAniosBaseEdadAdulta).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Validar Egreso Administrativo para Paquetizar
	 */
	public static String getValidarEgresoAdministrativoPaquetizar(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarEgresoAdministrativoPaquetizar + "_"
							+ institucion, 
					ConstantesValoresPorDefecto.valorValoresDefectoValidarEgresoAdministrativoPaquetizar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Maxima Cantidad de Paquetes Validos por Ingreso Paciente
	 */
	public static String getMaxCantidPaquetesValidosIngresoPaciente(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaxCantidPaquetesValidosIngresoPaciente + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMaxCantidPaquetesValidosIngresoPaciente).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Asignar Valor Paciente con el Valor de los Cargos
	 */
	public static String getAsignarValorPacienteValorCargos(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsignarValorPacienteValorCargos + "_"
							+ institucion, 
					ConstantesValoresPorDefecto.valorValoresDefectoAsignarValorPacienteValorCargos).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Interfaz Cartera Pacientes
	 */
	public static String getInterfazCarteraPacientes(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazCarteraPacientes + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazCarteraPacientes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Interfaz Contable Facturas
	 */
	public static String getInterfazContableFacturas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableFacturas + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazContableFacturas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Interfaz Terceros
	 */
	public static String getInterfazTerceros(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazTerceros + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazTerceros).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna Consolidar Cargos
	 */
	public static String getConsolidarCargos(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConsolidarCargos + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConsolidarCargos).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getManejaConversionMonedaExtranjera(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaConversionMonedaExtranjera + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoManejaConversionMonedaExtranjera).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getImpresionMediaCarta(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoImpresionMediaCarta + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneralNo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getInstitucionMultiempresa(int institucion) {
		String valor="";
		
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionMultiempresa + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInstitucionMultiempresa).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getConteosValidosAjustarInventarioFisico(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConteosValidosAjustarInvFisico + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConteosValidosAjustarInvFisico).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @param obtenerValorCorto
	 * @return
	 */
	public static String getConceptoParaAjusteEntrada(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteEntrada);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @param obtenerValorCorto
	 * @return
	 */
	public static String getConceptoParaAjusteSalida(int institucion,
			boolean obtenerValorCorto) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteSalida);
			if (obtenerValorCorto)
				valor = valor.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getPermitirModificarConceptosAjuste(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarConceptosAjuste).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	
	
	/**
	 * @return Retorna
	 */
	public static String getGenerarEstanciaAutomatica(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGenerarEstanciaAutomatica +"_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGenerarEstanciaAutomatica).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	/**
	 * @return Retorna
	 */
	public static String getHoraGenerarEstanciaAutomatica(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraGenerarEstanciaAutomatica + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraGenerarEstanciaAutomatica).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	/**
	 * @return Retorna
	 */
	public static String getIncluirTipoPacienteCirugiaAmbulatoria(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValidacionOcupacionJustificacionNoPosArticulos(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosArticulos + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidacionOcupacionJustificacionNoPosArticulos).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValidacionOcupacionJustificacionNoPosServicios(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidacionOcupacionJustificacionNoPosServicios + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidacionOcupacionJustificacionNoPosServicios).split("@@") [0];
		} catch (Exception e) {
			valor = "";
			Log4JManager.info("No se pudo leer el valor del paramtro" + e);
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getHoraCorteHistoricoCamas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraCorteHistoricoCamas + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraCorteHistoricoCamas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getMinutosLimiteAlertaReserva(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaReserva+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaReserva).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getMinutosLimiteAlertaPacienteConSalidaUrgencias(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getMinutosLimiteAlertaPacientePorRemitirUrgencias(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoMinutosLimiteAlertaPacientePorRemitirHospitalizacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getIdentificadorInstitucionArchivosColsanitas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoIdentificadorInstitucionArchivosColsanitas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoIdentificadorInstitucionArchivosColsanitas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getInterfazRips(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazRips+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazRips).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	/**
	 * @return Retorna
	 */
	public static String getGenForecatRips(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoGeneracionForecatEnRips+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneracionForecatEnRips).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	/**
	 * @return Retorna
	 */
	public static String getEntidadManejaHospitalDia(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaHospitalDia+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEntidadManejaHospitalDia).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getEntidadManejaRips(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEntidadManejaRips+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoEntidadManejaRips).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoracionUrgenciasEnHospitalizacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValoracionUrgenciasEnHospitalizacion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValoracionUrgenciasEnHospitalizacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getUbicacionPlanosEntidadesSubcontratadas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoUbicacionPlanosEntidadesSubcontratadas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoUbicacionPlanosEntidadesSubcontratadas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Si es Requerida o no la Informaci&oacute;n de los RIPS 
	 * */
	public static String getRequeridaInfoRipsCagosDirectos(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequeridaInfoRipsCargosDirectos + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRequeridaInfoRipsCargosDirectos
					).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}	
	
	/**
	 * @return  
	 * */
	public static String getAsignaValoracionCxAmbulaHospita(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado
					).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getInterfazConsecutivoFacturasOtroSistema (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazConsecutivoFacturasOtroSistema).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getInterfazNutricion (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getTiempoMaximoReingresoUrgencias (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoUrgencias+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorTiempoMaximoReingresoUrgencias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getTiempoMaximoReingresoHospitalizacion (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreTiempoMaximoReingresoHospitalizacion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorTiempoMaximoReingresoHospitalizacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getPermitirFacturarReingresosIndependientes (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombrePermitirFacturarReingresosIndependientes+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorPermitirFacturarReingresosIndependientes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getLiberarCamaHospitalizacionDespuesFacturar (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreLiberarCamaHospitalizacionDespuesFacturar+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorLiberarCamaHospitalizacionDespuesFacturar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public static String getCrearCuentaAtencionCitas(int institucion) {
		try
		{
			return obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreCrearCuentaAtencionCitas
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorCrearCuentaAtencionCitas).split("@@")[0];
		}
		catch(Exception e)
		{
			return "";
		}
	}
	

	/**
	 * @return Retorna
	 */
	public static String getControlaInterpretacionProcedimientosEvoluciones (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreControlaInterpretacionProcedimientosParaEvolucionar+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoControlaInterpretacionProcedimientosEvolucion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getValidarRegistroEvolucionesGenerarOrdenes (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarRegistroEvolucionesParaOrdenes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoPathArchivosPlanosFacturacion (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFacturacion+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPathArchivosPlanosFacturacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		
		//Modificado ya que se esta repitiendo al final otro eslas
		/*if(!valor.isEmpty())
			valor+=System.getProperty("file.separator");*/
			
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoPathArchivosPlanosFurips (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosFurips+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		
		//Modificado ya que se esta repitiendo al final otro eslas
		/*if(!valor.isEmpty())
			valor+=System.getProperty("file.separator");*/
			
		return valor;
	}
	
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoPathArchivosPlanosManejoPaciente (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPathArchivosPlanosManejoPaciente+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPathArchivosPlanosManejoPaciente).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoComprobacionDerechosCapitacionObligatoria (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreComprobacionDerechosCapitacionObligatoria+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoComprobacionDerCapitacionObliga).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoRequiereAutorizarAnularFacturas (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreRequiereAutorizarAnularFacturas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRequiereAutorizarAnularFacturas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoValidarPoolesFact (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValidarPoolesFact+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoMostrarEnviarEpicrisisEvol (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMostrarEnviarEpicrisisEvol+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoConceptoParaAjusteEntrada (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteEntrada+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteEntrada).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoConceptoParaAjusteSalida (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConceptoParaAjusteSalida+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoConceptoParaAjusteSalida).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoPermitirModificarConceptosAjuste (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarConceptosAjuste+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarConceptosAjuste).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValoresDefectoDiasRestriccionCitasIncumplidas (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasRestriccionCitasIncumplidas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasRestriccionCitasIncumplidas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getInstitucionManejaMultasPorIncumplimiento (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorInstitucionManejaMultasPorIncumplimiento).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getBloqueaCitasReservaAsignReprogPorIncump (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorBloqueaCitasReservaAsignReprogPorIncump).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getBloqueaAtencionCitasPorIncump (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorBloqueaAtencionCitasPorIncump).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getFechaInicioControlMultasIncumplimientoCitas (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorFechaInicioControlMultasIncumplimientoCitas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getValorMultaPorIncumplimientoCitas (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValorMultaPorIncumplimientoCitas+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValorMultaPorIncumplimientoCitas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getPermitirModificarFechaSolicitudPedidos (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudPedidos+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarFechaSolicitudPedidos).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getPermitirModificarFechaSolicitudTraslado (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarFechaSolicitudTraslado+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarFechaSolicitudTraslado).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getPostularFechasEnRespuestaDyT(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPostularFechasEnRespuestaDyT).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/** 
	 * Return el getProduccionEnParaleloConSistemaAnteriorLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getPostularFechasEnRespuestaDyTLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPostularFechasEnRespuestasDyT + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPostularFechasEnRespuestaDyT);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna
	 */
	public static String getNumeroDiasBusquedaReportes(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasBusquedaReportes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	/** 
	 * Return el getNumeroDiasBusquedaReportesLargo
	 * @param institucion
	 * @return valor
	 */
	public static String getNumeroDiasBusquedaReportesLargo(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroDiasBusquedaReportes + "_" 
						+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumeroDiasBusquedaReportes);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	
	/**
	 * 70006
	 * @param institucion
	 * @return
	 */
	public static String getNumDigCaptNumIdPac(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumDigitosCaptNumIdPac + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoNumDigCaptNumIdPac).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * Permitir interpretar ordenes de respuesta m&uacute;ltiple sin finalizar
	 * @param institucion
	 * @return
	 */
	public static String getPermIntOrdRespMulSinFin(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermIntOrdRespMulSinFin + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPermIntOrdRespMulSinFin).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	* @param institucion
	 * @return
	 */
	public static String getRequiereGlosaInactivar(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequiereGlosaInactivar + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoRequiereGlosaInactivar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * Permitir interpretar ordenes de respuesta m&uacute;ltiple sin finalizar
	 * @param institucion
	 * @return
	 */
	public static String getTiemSegVeriInterShaioProc(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTiemSegVeriInterShaioProc + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTiemSegVeriInterShaioProc).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	//----------------------------------------------------------------------------------------------------------
	
	//------------------------------------Getters And Setters-------------------------------------------------
	/**
	 * @return Retorna codigoTipoCieActual.
	 */
	public static int getCodigoTipoCieActual() {
		return codigoTipoCieActual;
	}
	
	/**
	 * @param codigoTipoCieActual
	 *            Asigna codigoTipoCieActual.
	 */
	public static void setCodigoTipoCieActual(int codigoTipoCieActual) {
		ValoresPorDefecto.codigoTipoCieActual = codigoTipoCieActual;
	}

	/**
	 * @return Retorna filePath.
	 */
	public static String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            Asigna filePath.
	 */
	public static void setFilePath(String filePath) {
		ValoresPorDefecto.filePath = filePath;
	}

		
	public static String getReportPath() {
		return reportPath;
	}

	public static void setReportPath(String reportPath) {
		ValoresPorDefecto.reportPath = reportPath;
	}

	public static String getReportUrl() {
		return reportUrl;
	}

	public static void setReportUrl(String reportUrl) {
		ValoresPorDefecto.reportUrl = reportUrl;
	}

	/**
	 * @return Retorna fotosPath.
	 */
	public static String getFotosPath() {
		return fotosPath;
	}

	/**
	 * @param fotosPath
	 *            Asigna fotosPath.
	 */
	public static void setFotosPath(String fotosPath) {
		ValoresPorDefecto.fotosPath = fotosPath;
	}

	/**
	 * @return Retorna directoriImagenes.
	 */
	public static String getDirectorioImagenes() {
		return directorioImagenes;
	}

	/**
	 * @param directoriImagenes
	 *            Asigna directoriImagenes.
	 */
	public static void setDirectorioImagenes(String directorioImagenes) {
		ValoresPorDefecto.directorioImagenes = directorioImagenes;
	}

	/**
	 * @return Retorna mostrarNombreJSP.
	 */
	public static boolean getMostrarNombreJSP() {
		return mostrarNombreJSP;
	}
	
	/**
	 * @return Retorna popUpNavegacion.
	 */
	public static boolean getPopUpNavegacion() {
		return popUpNavegacion;
	}

	/**
	 * M&eacute;todo para obtener el valor true aceptado por la BD actual
	 * 
	 * @return
	 */
	public static String getValorTrueParaConsultas() {
		return valorTrueParaConsultas;
	}

	/**
	 * M&eacute;todo para obtener el valor false aceptado por la BD actual
	 * 
	 * @return
	 */
	public static String getValorFalseParaConsultas() {
		return valorFalseParaConsultas;
	}

	/**
	 * @param string
	 *            con el nombre del valor por defecto
	 * @return
	 */
	private static String obtenerNombreValorDefectoLargo(String nombre,
			String valorPorDefecto) {
		String valor = (String) parametrosGenerales.get(nombre);
		if (valor != null) {
			if (!valor.equals("") && !valor.equals("@@")) {
				return valor.trim();
			}
		} else {
			return valorPorDefecto.trim();
		}
		return valor.trim();
	}

	/**
	 * @param modulo
	 *            Asigna modulo.
	 */
	public static void setModulo(int modulo) {
		ValoresPorDefecto.modulo = modulo;
	}

	/**
	 * @return Returns the valorFalseCortoParaConsultas.
	 */
	public static String getValorFalseCortoParaConsultas() {
		if (valorFalseParaConsultas.equals("false"))
			return "f";
		else
			return valorFalseParaConsultas;
	}

	/**
	 * @return Returns the valorTrueCortoParaConsultas.
	 */
	public static String getValorTrueCortoParaConsultas() {
		if (valorTrueParaConsultas.equals("true"))
			return "t";
		else
			return valorTrueParaConsultas;
	}

	/**
	 * @return Retorna La Hora Inicio Programaci&oacute;n Salas
	 */
	public static String getHoraInicioPrimerTurno(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraInicioPrimerTurno
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraInicioPrimerTurno)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna La Hora Fin Programaci&oacute;n Salas
	 */
	public static String getHoraFinUltimoTurno(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraFinUltimoTurno + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoHoraFinUltimoTurno).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return Retorna el tipo de consecutivo a manejar en facturas varias.
	 */
	public static String getTipoConsecutivoManejar(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoTipoConsecutivoManejar
					).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String getNumeroFilaParaConsultas() {
		return numeroFilaParaConsultas;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getVigilarAccRabico(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoVigilarAccRabico + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoVigilarAccRabico).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getInterfazContableRecibosCajaERP(int institucion)
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInterfazContableRecibosCajaERP + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoInterfazContableRecibosCajaERP).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}


	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getValidarAdministracionMedEgresoMedico(int institucion)
	{
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarAdministracionMedicamentosEgresoMedico + "_"
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoValidarAdministracionMedicamentosEgresoMedico).split(
					"@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	public static String getDirectorioAxiomaBase() {
		return directorioAxiomaBase;
	}

	public static void setDirectorioAxiomaBase(String directorioAxiomaBase) {
		ValoresPorDefecto.directorioAxiomaBase = directorioAxiomaBase;
	}

	public static HashMap getIntegridadDominio() {
		return integridadDominio;
	}

	public static void setIntegridadDominio(HashMap integridadDominio) {
		ValoresPorDefecto.integridadDominio = integridadDominio;
	}

	public static Object getIntegridadDominio(String key) {
		Object objeto = integridadDominio.get(key);
		if (objeto == null)
			return "";
		else
			return objeto;
	}

	public static void setIntegridadDominio(String key, Object value) {
		ValoresPorDefecto.integridadDominio.put(key, value);
	}

	/**
	 * @return Returns the centroCostoTerceros.
	 */
	public static HashMap getCentroCostoTerceros() {
		return centroCostoTerceros;
	}

	/**
	 * @param centroCostoTerceros
	 *            The centroCostoTerceros to set.
	 */
	public static void setCentroCostoTerceros(HashMap centroCostoTerceros) {
		ValoresPorDefecto.centroCostoTerceros = centroCostoTerceros;
	}

	/**
	 * @return Returns the centroCostoTerceros.
	 */
	public static Object getCentroCostoTerceros(Object key) {
		return centroCostoTerceros.get(key);
	}

	/**
	 * @param centroCostoTerceros
	 *            The centroCostoTerceros to set.
	 */
	public static void setCentroCostoTerceros(Object key, Object value) {
		ValoresPorDefecto.centroCostoTerceros.put(key, value);
	}

	/**
	 * @return Returns the horasReproceso.
	 */
	public static HashMap getHorasReproceso() {
		return horasReproceso;
	}

	/**
	 * @param horasReproceso
	 *            The horasReproceso to set.
	 */
	public static void setHorasReproceso(HashMap horasReproceso) {
		ValoresPorDefecto.horasReproceso = horasReproceso;
	}

	/**
	 * @return Returns the horasReproceso.
	 */
	public static Object getHorasReproceso(Object key) {
		return horasReproceso.get(key);
	}

	/**
	 * @param horasReproceso
	 *            The horasReproceso to set.
	 */
	public static void setHorasReproceso(Object key, Object value) {
		ValoresPorDefecto.horasReproceso.put(key, value);
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getCodigoManualEstandarBusquedaArticulos(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoManualEstandarBusquedaArticulos)
					.split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getNombreManualEstandarBusquedaArticulos(int institucion) {
		String valor = "";
		try 
		{
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos
							+ "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoCodigoManualEstandarBusquedaArticulos)
					.split("@@")[1];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * @return Retorna 
	 */
	public static String getCodigoManualEstandarBusquedaArticulosLargo(int institucion) 
	{
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos
						+ "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoCodigoManualEstandarBusquedaArticulos);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getValoresDefectoUsuarioaReportarenSolicitAuto (int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresporDefectoUsuarioaReportarenSolicitAuto+ "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoGeneral).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
			
		return valor;
	}
	
	
	
	/**
	 * 
	 * @param codigoManual
	 * @param institucion
	 */
	public static void setCodigoManualEstandarBusquedaArticulos(String codigoManual, int institucion) 
	{
		parametrosGenerales.put(ConstantesValoresPorDefecto.nombreValoresDefectoCodigoManualEstandarBusquedaArticulos+ "_" + institucion, codigoManual);
	}

	/**
	 * @return the cliente
	 */
	public static String getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public static void setCliente(String cliente) {
		ValoresPorDefecto.cliente = cliente;
	}
	
	/**
	 * M&eacute;todo encargado de ejecutar al consulta consultarSiExisteFacturaVaria
	 * y retorna el value object si hay registros en la tabla: facturas_varias
	 * @author Felipe P&eacute;rez Granda
	 * @param con
	 * @return HashMap Cargar Value Object
	 */
	public static HashMap consultarFacturasVarias(Connection con) {
		return valoresPorDefectoDao.consultarFacturasVarias(con);
	}

	/**
	 * @return the clasesInventariosPaqMatQx
	 */
	public static HashMap getClasesInventariosPaqMatQx() {
		return clasesInventariosPaqMatQx;
	}

	/**
	 * @param clasesInventariosPaqMatQx the clasesInventariosPaqMatQx to set
	 */
	public static void setClasesInventariosPaqMatQx(HashMap clasesInventariosPaqMatQx) {
		ValoresPorDefecto.clasesInventariosPaqMatQx = clasesInventariosPaqMatQx;
	}
	
	/**
	 * @return Returns the clasesInventariosPaqMatQx
	 */
	public static Object getClasesInventariosPaqMatQx(Object key) {
		return ValoresPorDefecto.clasesInventariosPaqMatQx.get(key);
	}

	/**
	 * @param clasesInventariosPaqMatQx The clasesInventariosPaqMatQx to set.
	 */
	public static void setClasesInventariosPaqMatQx(Object key, Object value) {
		ValoresPorDefecto.clasesInventariosPaqMatQx.put(key, value);
	}
	
	/**
	 * M&eacute;todo encargado de consultar si existe el datos seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente
	 * @author Felipe P&eacute;rez Granda
	 * @param con
	 * @param numSolicitudOrden
	 * @param codArticulo
	 * @param esOrdenAmbulatoria
	 * @return HashMap
	 */
	public static HashMap utilizaMedicamentosTratamientoPaciente(Connection con, String numSolicitudOrden, String codArticulo, boolean esOrdenAmbulatoria)
	{
		return valoresPorDefectoDao.utilizaMedicamentosTratamientoPaciente(con, numSolicitudOrden, codArticulo, esOrdenAmbulatoria);
	}

	/**
	 * retorna para postgres substr(CURRENT_TIME,1,5)
	 * retorna para oracle " to_char(sysdate,'HH24:MI') "
	 * @return the sentenciaHoraActualBD
	 */
	public static String getSentenciaHoraActualBD() {
		return sentenciaHoraActualBD;
	}

	/**
	 * retorna para postgres CURRENT_TIME
	 * retorna para oracle " to_char(sysdate,'HH24:MI') "
	 * @return the sentenciaHoraActualBD
	 */
	public static String getSentenciaHoraActualBDTipoTime() {
		return sentenciaHoraActualBDTipoTime;
	}

	/**
	 * retorna para postgres CURRENT_TIME
	 * retorna para oracle " to_char(sysdate,'HH24:MI:SS') "
	 * @return the sentenciaHoraActualBD
	 */
	public static String getSentenciaHoraActualBDTipoTimeConSegundos() {
		return sentenciaHoraActualBDTipoTimeConSegundos;
	}

	
	/**
	 * @param sentenciaHoraActualBD the sentenciaHoraActualBD to set
	 */
	public static void setSentenciaHoraActualBD(String sentenciaHoraActualBD) {
		ValoresPorDefecto.sentenciaHoraActualBD = sentenciaHoraActualBD;
	}

	/**
	 * @return the filePathInterfazLaboratorios
	 */
	public static String getFilePathInterfazLaboratorios() {
		return filePathInterfazLaboratorios;
	}

	/**
	 * @param filePathInterfazLaboratorios the filePathInterfazLaboratorios to set
	 */
	public static void setFilePathInterfazLaboratorios(
			String filePathInterfazLaboratorios) {
		ValoresPorDefecto.filePathInterfazLaboratorios = filePathInterfazLaboratorios;
	}

	/**
	 * @param sentenciaHoraActualBDTipoTime the sentenciaHoraActualBDTipoTime to set
	 */
	public static void setSentenciaHoraActualBDTipoTime(
			String sentenciaHoraActualBDTipoTime) {
		ValoresPorDefecto.sentenciaHoraActualBDTipoTime = sentenciaHoraActualBDTipoTime;
	}

	/**
	 * @param sentenciaHoraActualBDTipoTime the sentenciaHoraActualBDTipoTime to set
	 */
	public static void setNumeroMaximoDiasGenOrdenesAmbServicios(
			String numMaxDiasGenOrdenesAmbServ) {
		ValoresPorDefecto.numMaxDiasGenOrdenesAmbServ = numMaxDiasGenOrdenesAmbServ;
	}
	
	public static String getNumeroMaximoDiasGenOrdenesAmbServicios(
			int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.numMaxDiasGenOrdenesAmbServ + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoNumMaxDiasGenOrdenesAmbServ).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
		
	}
	
	/**
	 * @param sentenciaHoraActualBDTipoTime the sentenciaHoraActualBDTipoTime to set
	 */
	public static void setNumDiasAntFActualAgendaOd(
			String numDiasAntFActualAgendaOd) {
		ValoresPorDefecto.numDiasAntFActualAgendaOd = numDiasAntFActualAgendaOd;
	}
	
	public static String getNumDiasAntFActualAgendaOd(
			int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.numDiasAntFActualAgendaOd + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorNumDiasAntFActualAgendaOd).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
		
	}
	
	/**
	 * @param multiploMinGeneracionCita the multiploMinGeneracionCita to set
	 */
	public static void setMultiploMinGeneracionCita(
			String multiploMinGeneracionCita) {
		ValoresPorDefecto.multiploMinGeneracionCita = multiploMinGeneracionCita;
	}
	
	public static String getMultiploMinGeneracionCita(
			int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.multiploMinGeneracionCita + "_" 
							+ institucion, 
							ConstantesValoresPorDefecto.valorMultiploMinGeneracionCita).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
		
	}
	
	/**
	 * 
	* @param institucion
	 * @return
	 */
	public static String getManejoEspecialInstitucionesOdontologia(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreManejoEspecialInstitucionesOdontologia + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorManejoEspecialInstitucionesOdontologia).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	* @param institucion
	 * @return
	 */
	public static String getMaximoNumeroCuotasFinanciacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMaximoNumeroCuotasFinanciacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorMaximoNumeroCuotasFinanciacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	* @param institucion
	 * @return
	 */
	public static String getMaximoNumeroDiasFinanciacionPorCuota(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMaximoNumeroDiasFinanciacionPorCuota + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorMaximoNumeroDiasFinanciacionPorCuota).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	* @param institucion
	 * @return
	 */
	public static String getFormatoDocumentosGarantia_Pagare(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreFormatoDocumentosGarantia_Pagare + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorFormatoDocumentosGarantia_Pagare).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getOcupacionOdontologo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreOcupacionOdontologo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorOcupacionOdontologo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getOcupacionOdontologoLargo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreOcupacionOdontologo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorOcupacionOdontologo);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	public static String getOcupacionAuxiliarOdontologo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorOcupacionAuxiliarOdontologo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getOcupacionAuxiliarOdontologoLargo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreOcupacionAuxiliarOdontologo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorOcupacionAuxiliarOdontologo);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	public static String getEdadFinalNinez(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreEdadFinalNinez + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorEdadFinalNinez).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getEdadInicioAdulto(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreEdadInicioAdulto + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorEdadInicioAdulto).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getObligarRegIncapaPacienteHospitalizado(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreObligarRegIncapaPacienteHospitalizado + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorObligarRegIncapaPacienteHospitalizado).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	public static String getEscalaPacientePerfil(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEscalaPacientePerfil).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getEscalaPacientePerfilLargo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEscalaPerfilPaciente + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEscalaPacientePerfil);
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMinutosCaducaCitasReservadas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMinutosCaducaCitaReservada + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorMinutosCaducaCitaReservada).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMinutosCaducaCitasAsignadasReprogramadas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMinutosCaducaCitaAsignadasReprogramadas + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorMinutosCaducaCitaAsignadasReprogramadas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getEjecutarProcAutoActualizacionCitasOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreEjecutarProcAutoActualizacionCitasOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorEjecutarProcAutoActualizacionCitasOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getHoraEjecutarProcAutoActualizacionCitasOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreHoraEjecutarProcAutoActualizacionCitasOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorHoraEjecutarProcAutoActualizacionCitasOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMinutosEsperaAsignarCitasOdoCaducadas(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreMinutosEsperaAsignarCitasOdoCaducadas + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorMinutosEsperaAsignarCitasOdoCaducadas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getUtilizanProgramasOdontologicosEnInstitucion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoUtilizanProgramasOdontologicosEnInstitucion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoUtilizanProgramasOdontologicosEnInstitucion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static String getTiempoVigenciaPresupuestoOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTiempoVigenciaPresupuestoOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTiempoVigenciaPresupuestoOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPermiteCambiarServiciosCitaAtencionOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermiteCambiarServiciosCitaAtencionOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermiteCambiarServiciosCitaAtencionOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getValidaPresupuestoOdoContratado(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidaPresupuestoOdoContratado + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoValidaPresupuestoOdoContratado).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static boolean getValidaPresupuestoOdoContratadoBoolean(int institucion) 
	{
		return UtilidadTexto.getBoolean(getValidaPresupuestoOdoContratado(institucion));
	}
	
	public static String getConveniosAMostrarXDefectoPresupuestoOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoConveniosAMostrarXDefectoPresupuestoOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getEjecutarProcesoAutoActualizacionEstadosOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getHoraEjecutarProcesoAutoActualizacionEstadosOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMotivoCancelacionPresupuestoSuspendidoTemp(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPrioridadParaAplicarPromocionesOdo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadParaAplicarPromocionesOdo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPrioridadParaAplicarPromocionesOdo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getDiasParaDefinirMoraXDeudaPacientes(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasParaDefinirMoraXDeudaPacientes + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoDiasParaDefinirMoraXDeudaPacientes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getCuentaContablePagare(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContablePagare + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoCuentaContablePagare).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getCuentaContableLetra(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCuentaContableLetra + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoCuentaContableLetra).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getInstitucionRegistraAtencionExterna(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionRegistraAtencionExterna + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoInstitucionRegistraAtencionExterna).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * @return the conveniosAMostrarPresupuestoOdo
	 */
	public static ArrayList<HashMap<String, Object>> getConveniosAMostrarPresupuestoOdo() {
		return conveniosAMostrarPresupuestoOdo;
	}

	/**
	 * @param conveniosAMostrarPresupuestoOdo the conveniosAMostrarPresupuestoOdo to set
	 */
	public static void setConveniosAMostrarPresupuestoOdo(
			ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo) {
		ValoresPorDefecto.conveniosAMostrarPresupuestoOdo = conveniosAMostrarPresupuestoOdo;
	}

	//Anexo 992
	/**
	 * 
	 */
	public static String getImprimirFirmasImpresionCCCapitacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoImprimirFirmasImpresionCCCapitacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getEncabezadoFormatoImpresionFacturaOCCCapitacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPiePaginaFormatoImpresionFacturaOCCCapitacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getImprimirFirmasEnImpresionCC(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoImprimirFirmasEnImpresionCC).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	

	//Anexo 958
	
	public static String getReciboCajaAutomaticoGeneracionFacturaVaria(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getConceptoIngresoFacturasVarias(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoConceptoIngresoFacturasVarias + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoConceptoIngresoFacturasVarias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	//Fin anexo 958
	
	
	//Anexo 888
	
	public static String getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static int getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologicoInt(int institucion) 
	{
		return Utilidades.convertirAEntero(getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(institucion));
	}
	
	//Fin anexo 888
	
	public static ResultadoBoolean insertarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		return valoresPorDefectoDao.insertarFirmasValoresPorDefecto(dto);
	}
	
	public static ArrayList<DtoFirmasValoresPorDefecto> consultarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		return valoresPorDefectoDao.consultarFirmasValoresPorDefecto(dto);
	}
	
	public static ResultadoBoolean eliminarFirma(DtoFirmasValoresPorDefecto dto)
	{
		return valoresPorDefectoDao.eliminarFirma(dto);
	}
	
	//Fin Anexo 992
	
	
	//Anexo 888 Pt II
	
	public static String getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getInstitucionManejaFacturacionAutomatica(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaFacturacionAutomatica + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoInstitucionManejaFacturacionAutomatica).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	//Fin Anexo Pt. II
	
	//Anexo 959
	public static String getManejaConsecutivoFacturaPorCentroAtencion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturaPorCentroAtencion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaConsecutivoFacturaPorCentroAtencion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static boolean getManejaConsecutivoFacturaPorCentroAtencionBool(int institucion) 
	{
		return UtilidadTexto.getBoolean(getManejaConsecutivoFacturaPorCentroAtencion(institucion));
	}
	
	
	public static String getManejaConsecutivosTesoreriaPorCentroAtencion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getTamanioImpresionRC(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTamanioImpresionRC + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTamanioImpresionRC).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getRequiereAperturaCierreCaja(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAperturaCierreCaja + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequiereAperturaCierreCaja).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getRequeridoProgramarProximaCitaEnAtencion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequeridoProgramarProximaCitaEnAtencion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequeridoProgramarProximaCitaEnAtencion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	
	
	
	public static String getActivarBotonGenerarSolicitudOrdenAmbulatora(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getInstitucionManejaCajaPrincipal(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaCajaPrincipal + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoInstitucionManejaCajaPrincipal).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getInstitucionManejaTrasladoOtraCajaRecaudo(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getInstitucionManejaEntregaATransportadoraValores(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoInstitucionManejaEntregaATransportadoraValores + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoInstitucionManejaEntregaATransportadoraValores).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getControlarAbonoPacientePorNroIngreso(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoControlarAbonoPacientePorNroIngreso + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoControlarAbonoPacientePorNroIngreso).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getValidaEstadoContratoNominaALosProfesionalesSalud(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getManejaInterfazUsuariosSistemaERP(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaInterfazUsuariosSistemaERP + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaInterfazUsuariosSistemaERP).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	public static String getRequierGenerarSolicitudCambioServicio(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequierGenerarSolicitudCambioServicio + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequierGenerarSolicitudCambioServicio).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	//*
	public static String getManejaVentaTarjetaClienteOdontosinEmision(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaVentaTarjetaClienteOdontosinEmision + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaVentaTarjetaClienteOdontosinEmision).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	

	public static String getAprobarGlosaRegistro(int institucion) 
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoAprobarGlosaRegistro + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoAprobarGlosaRegistro).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	//*
	public static String getLasCitasDeControlSePermitenAsignarA(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoLasCitasDeControlSePermitenAsignarA + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoLasCitasDeControlSePermitenAsignarA).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	//Fin Anexo 959
	
	/**
	 * Obtiene el par&aacute;metro que indica si se debe mostrar o no
	 * el componente de flash en el cual se marcan las superficies con
	 * &iacute;ndice de placa 
	 * @param institucion Instituci&oacute;n del usuario
	 * @return Cadena con el valor booleano del par&aacute;metro
	 */
	public static String getMostrarGraficaCalculoIndicePlaca(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMostrarGraficaCalculoIndicePlaca + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMostrarGraficaCalculoIndicePlaca).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getValidarPacienteParaVentaTarjeta(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoValidarPacienteParaVentaTarjeta + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoValidarPacienteParaVentaTarjeta).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getManejoOxigenoFurips(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejoOxigenoFurips + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejoOxigenoFurips).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getNumMaximoReclamacionesAccEventoXFactura(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNumMaximoReclamacionesAccEventoXFactura + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoNumMaximoReclamacionesAccEventoXFactura).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHacerRequeridoValorAbonoAplicadoAlFacturar(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getReciboCajaAutomaticoVentaTarjeta(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoReciboCajaAutomaticoVentaTarjeta + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoReciboCajaAutomaticoVentaTarjeta).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPermitirRegistrarReclamacionCuentasNoFacturadas(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPermitirFacturarReclamarCuentasConRegistroPendientes(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMostrarAdminMedicamentosArticulosDespachoCero(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	public static String getFormatoFacturaVaria(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoFacturaVaria+ "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoFormatoFacturaVaria).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	public static boolean existeAgendaOdon()
	{
		return valoresPorDefectoDao.existeAgendaOdon();
	}
	
	public static boolean existePresupesto()
	{
		return valoresPorDefectoDao.existePresupesto();
	}
	
	public static String obtenerSentenciaNextval(String secuencia)
	{
		return sentenciaNextvalBD.replace("?", secuencia);
	}
	
	public static boolean guardarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto) {
		return valoresPorDefectoDao.guardarAreaAperturaCuentaAutoPYP(dto);
	}

	public static boolean eliminarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto) {
		return valoresPorDefectoDao.eliminarAreaAperturaCuentaAutoPYP(dto);
	}
	
	public static ArrayList<DtoAreaAperturaCuentaAutoPYP> consultarAreasAperturaCuentaAutoPYP(int institucion, int centroAtencion) {
		return valoresPorDefectoDao.consultarAreasAperturaCuentaAutoPYP(institucion, centroAtencion);
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getModificarFechaHoraInicioAtencionOdonto(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoModificarFechaHoraInicioAtencionOdonto + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoModificarFechaHoraInicioAtencionOdonto).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @param ciudadNacimiento
	 *            Asigna entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada.
	 */
	public static void setEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(String entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada,
			int institucion) {
		parametrosGenerales.put(
				ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada + "_"
						+ institucion, entidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada);
	}

	/**
	 * getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada

	 * @param institucion
	 * @return valor
	 */
	public static String getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna A&ntilde;os Base Edad Adulta
	 */
	public static String getPrioridadEntidadSubcontratada(int institucion) {
		String valor = "";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPrioridadEntidadSubcontratada + "_" 
							+ institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPrioridadEntidadSubcontratada).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getManejaConsecutivoFacturasVariasPorCentroAtencion(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getFormatoImpresionAutorEntidadSub(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpresionAutorEntidadSub + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoFormatoImpresionAutorEntidadSub).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getEncFormatoImpresionAutorEntidadSub(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEncFormatoImpresionAutorEntidadSub + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoEncFormatoImpresionAutorEntidadSub).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPiePagFormatoImpresionAutorEntidadSub(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPiePagFormatoImpresionAutorEntidadSub + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoPiePagFormatoImpresionAutorEntidadSub).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasVigenciaAutorIndicativoTemp(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaAutorIndicativoTemp + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasVigenciaAutorIndicativoTemp).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasProrrogaAutorizacion(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasProrrogaAutorizacion + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasProrrogaAutorizacion).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasCalcularFechaVencAutorizacionServicio(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionServicio + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasCalcularFechaVencAutorizacionServicio).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasCalcularFechaVencAutorizacionArticulo(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasCalcularFechaVencAutorizacionArticulo + "_" + institucion,
					ConstantesValoresPorDefecto.valorValoresDefectoDiasCalcularFechaVencAutorizacionArticulo).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasVigentesNuevaAutorizacionEstanciaSerArt(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHoraProcesoCierreCapitacion(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraProcesoCierreCapitacion).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirfacturarReclamCuentaRATREC(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirfacturarReclamCuentaRATREC).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getMostrarDetalleGlosasFacturaSolicFactura(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoMostrarDetalleGlosasFacturaSolicFactura).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
		
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getFormatoImpReservaCitaOdonto(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpReservaCitaOdonto).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getFormatoImpAsignacionCitaOdonto(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoFormatoImpAsignacionCitaOdonto).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getFechaInicioCierreOrdenMedica(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoFechaInicioCierreOrdenMedica).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getEsquemaTariServiciosValorizarOrden(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariServiciosValorizarOrden).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getSolicitudCitaInterconsultaOdontoCitaProgramada(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHoraEjecProcesoInactivarUsuarioInacSistema(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHoraEjeProcesoCaduContraInacSistema(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoHoraEjeProcesoCaduContraInacSistema).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasVigenciaContraUsuario(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasVigenciaContraUsuario).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getDiasFinalesVigenciaContraMostrarAlerta(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasFinalesVigenciaContraMostrarAlerta).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getEsquemaTariMedicamentosValorizarOrden(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden + "_" + institucion,
					ConstantesValoresPorDefecto.nombreValoresDefectoEsquemaTariMedicamentosValorizarOrden).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirModificarDatosUsuariosCapitados(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitados + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarDatosUsuariosCapitados).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getPermitirModificarDatosUsuariosCapitadosModificarCuenta(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getManejaHojaAnestesia(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaHojaAnestesia + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaHojaAnestesia).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getManejaConsecutivosNotasPacientesCentroAtencion(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static String getNaturalezaNotasPacientesManejar(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoNaturalezaNotasPacientesManejar + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoNaturalezaNotasPacientesManejar).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}

	public static ArrayList<Integer> getServiciosManejoTransPrimario() {
		return serviciosManejoTransPrimario;
	}

	public static void setServiciosManejoTransPrimario(
			ArrayList<Integer> serviciosManejoTransPrimario) {
		ValoresPorDefecto.serviciosManejoTransPrimario = serviciosManejoTransPrimario;
	}

	public static ArrayList<Integer> getServiciosManejoTransSecundario() {
		return serviciosManejoTransSecundario;
	}

	public static void setServiciosManejoTransSecundario(
			ArrayList<Integer> serviciosManejoTransSecundario) {
		ValoresPorDefecto.serviciosManejoTransSecundario = serviciosManejoTransSecundario;
	}

	public static String getPermitirRecaudosCajaMayor(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoPermitirRecaudosCajaMayor + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoPermitirRecaudosCajaMayor).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna viaIngresoValidacionesOrdenesAmbulatorias.
	 */
	public static String getViaIngresoValidacionesOrdenesAmbulatorias(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
							ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias + "_" + institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna viaIngresoValidacionesOrdenesAmbulatorias.
	 */
	public static String getViaIngresoValidacionesOrdenesAmbulatoriasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias);
	}
	
	/**
	 * @return Retorna viaIngresoValidacionesPeticiones.
	 */
	public static String getViaIngresoValidacionesPeticiones(int institucion) {
		String valor="";
		try {
			valor =  obtenerNombreValorDefectoLargo(
							ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones + "_" + institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoViaIngresoValidacionesPeticiones).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna viaIngresoValidacionesPeticiones.
	 */
	public static String getViaIngresoValidacionesPeticionesLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoViaIngresoValidacionesPeticiones + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoViaIngresoValidacionesPeticiones);
	}
	
	
	public static String getMaximoDiasConsultarIngresos(int institucion){
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoCantidadMaximoDiasConsultaIngreso + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoCantidadMaximoDiasConsultaIngreso).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getMesesMaxAdminAutoCapVencidas(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoMesesMaxAdminAutoCapVencidas + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoMesesMaxAdminAutoCapVencidas).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getDiasMaxProrrogaAutorizacionArticulo(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionArticulo + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoDiasMaxProrrogaAutorizacionArticulo).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	public static String getDiasMaxProrrogaAutorizacionServicio(int institucion)
	{
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
					ConstantesValoresPorDefecto.nombreValoresDefectoDiasMaxProrrogaAutorizacionServicio + "_" 
							+ institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoDiasMaxProrrogaAutorizacionServicio).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna tipoPacienteValidacionesOrdenesAmbulatorias.
	 */
	public static String getTipoPacienteValidacionesOrdenesAmbulatorias(int institucion) {
		String valor="";
		try {
			valor = obtenerNombreValorDefectoLargo(
							ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias + "_" + institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias).split("@@") [0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna tipoPacienteValidacionesOrdenesAmbulatorias.
	 */
	public static String getTipoPacienteValidacionesOrdenesAmbulatoriasLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias);
	}
	
	/**
	 * @return Retorna tipoPacienteValidacionesPeticiones.
	 */
	public static String getTipoPacienteValidacionesPeticiones(int institucion) {
		String valor="";
		try {
			valor =  obtenerNombreValorDefectoLargo(
							ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones + "_" + institucion,
							ConstantesValoresPorDefecto.valorValoresDefectoTipoPacienteValidacionesPeticiones).split("@@")[0];
		} catch (Exception e) {
			valor = "";
		}
		return valor;
	}
	
	/**
	 * @return Retorna tipoPacienteValidacionesPeticiones.
	 */
	public static String getTipoPacienteValidacionesPeticionesLargo(int institucion) {
		return obtenerNombreValorDefectoLargo(
				ConstantesValoresPorDefecto.nombreValoresDefectoTipoPacienteValidacionesPeticiones + "_" + institucion,
				ConstantesValoresPorDefecto.valorValoresDefectoTipoPacienteValidacionesPeticiones);
	}
	
}