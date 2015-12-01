/*
 * Creado   22/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.orm.ContratoOdontologico;

/**
 * Clase para manejar
 * 
 * @version 1.0, 22/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class ParamInstitucionForm extends ActionForm {

	/**
	 * String Logger
	 */
	// transient private Logger logger =
	// Logger.getLogger(RecargoTarifasAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*********************************************************
	 * ATRIBUTOS PARA EL MANEJO DE EMPRESAS
	 *********************************************************/

	private String[] indisesEmpresa = ParametrizacionInstitucion.indicesEmpresa;

	private static final String mensajeErrorContrato = "Es Requerido que por lo menos se adicione una firma";

	private String esMultiempresa;

	private HashMap listadoEmpresasMap = new HashMap();

	private HashMap listadoEmpresasElimMap = new HashMap();

	private HashMap empresa = new HashMap();

	private HashMap empresaClone = new HashMap();

	private String index = "";

	private ArrayList<HashMap<String, Object>> tiposIdent = new ArrayList<HashMap<String, Object>>();

	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String, Object>>();

	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String, Object>>();

	protected FormFile archivo;

	private ArrayList<DtoFirmasContOtrsiMultiEmpresa> listaDtoFirmEmpresa;

	private String imprimeFirmaEmpresaInstitucion;

	private int postArrayMultiEmpresa;

	private boolean listaMayorCuatro;

	/*---------------------------------------
	 * 	atributos del pager
	 ---------------------------------------*/
	/**
	 * para la nevagación del pager, cuando se ingresa un registro nuevo.
	 */
	private String linkSiguiente;

	/**
	 * indica cual es el index dentro del hashmap que se va a eliminar
	 */
	private String indexEliminado;

	/**
	 * 
	 */
	private String patronOrdenar;

	/*---------------------------------------
	 * 	fin atributos del pager
	 ---------------------------------------*/
	/*********************************************************
	 * FIN ATRIBUTOS PARA EL MANEJO DE EMPRESAS
	 *********************************************************/

	/**
	 * almacena el log
	 */
	private String log;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado;

	/**
	 * Encabezado factura
	 */
	private String encabezado;

	/**
	 * Pie de página factura
	 */
	private String pie;

	/**
	 * Pie de página Historia Clinica
	 */
	private String pieHistoriaClinica;

	/**
	 * Pie de página de Ordenes ambulatorias de Medicamentos
	 */
	private String pieAmbMedicamentos;
	
	/**
	 * Código de la institucion temporal.
	 */
	private int codigoT;

	/**
	 * Codigo de la Institucion en la BD.
	 */
	private String codigo;

	/**
	 * raz&oacute;n social de la instituci&oacute;n.
	 */
	private String razonSocial;

	/**
	 * identificaci&oacute;n de la instituci&oacute;n
	 */
	private String identificacion;

	/**
	 * digito de verificacion de la institucion
	 */
	private String digitoVerificacion;

	/**
	 * departamento de ubicaci&oacute;n de la instituci&oacute;n
	 */
	private String departamento;

	/**
	 * ciudad de ubicaci&oacute;n de la instituci&oacute;n
	 */
	private String ciudad;

	/**
	 * pais de ubicacion de la institucion
	 */
	private String pais;

	/**
	 * Direcci&oacute;n de la instituci&oacute;n
	 */
	private String direccion;

	/**
	 * Telefono de la instituci&oacute;n
	 */
	private String telefono;

	/**
	 * c&oacute;digo de minsalud
	 */
	private String codMinSalud;

	/**
	 * Actividad ec&oacute;nomica de la instituci&oacute;n
	 */
	private String actividadEconomica;

	/**
	 * N&uacute;mero de resoluci&oacute;n
	 */
	private String resolucion;

	/**
	 * prefijo de las facturas
	 */
	private String prefijoFacturas;

	/**
	 * N&uacute;mero inicial de la factura autorizado por la Dian
	 */
	private String rangoInicialFactura;

	/**
	 * N&uacute;mero final de la factura autorizado por la Dian
	 */
	private String rangoFinalFactura;

	/**
	 * Contiene los datos de la consulta.
	 */
	private Collection colInstitucion;

	private String accion;

	/**
	 *almacena el tipo de identificación
	 */
	private String acronimoNit;

	/**
	 * almacena la ruta para almacenar los RIPS
	 * 
	 */
	private String path;

	/**
	 * Mapa que contiene la informacion de los tipos de moneda por institucion
	 */
	private HashMap tiposMonedaInstitucionMap;

	/**
	 * Indice para la posicion del mapa a ser eliminada
	 */
	private int indiceEliminado;

	/**
	 * Mapa para los tipos de moneda eliminados
	 */
	private HashMap tiposMonedaInstitucionEliminadosMap;

	/**
	 * Mensaje
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);

	/**
	 * Logo
	 * */
	private String logo;

	private String ubicacionLogo = "";

	/**
	 * Tipo Institucion
	 */
	private String tipoIns;

	private String codEmpTransEsp = "";

	/**
	     * 
	     * */
	private String indicativo;

	/**
	     * 
	     * */
	private String extension;

	/**
	     * 
	     * */
	private String celular;

	/**
	     * 
	     */
	private String codigoInterfaz;
	/**
	 * REPRESENTANTE LEGAL
	 */
	private String representanteLegal;
	/**
	    * 
	    */
	private String piePaginaHistoriClinica;

	/**
	    * 
	    */
	private String niveLogo;

	/**
	 * entidad contrato
	 */
	private ContratoOdontologico dtoContrato;

	/**
	 * lista firmas
	 */
	private ArrayList<DtoFirmasContOtrosInstOdont> listaFirmasContrato;

	/**
	 * postArray listas
	 */
	private int postArraylist;

	/**
	 * IMPRIME MULTIEMPRESA
	 */
	private String imprimeFirmaEmpresa;

	/**
	 * REPRESENTANTE LEGAL MULTIEMPRESA
	 */
	private String representanteLegalMultiempresa;

	/**
	 * MANEJA CONSECUTIVOS FACTURA CENTRO ATENCION
	 */
	private boolean manejaConsecutivosFactCent;

	/**
	 * MANEJA ESPECIALIDAD INSTICUION ODONTOLOGICA
	 */
	private boolean manejaEspecialidadInsticionOdon;

	/**
	 * ATRIBUTO DE VALIDACION SIRVE PARA VALIDAR SI EXISTE POR LO MENOS UNA
	 * FIRMA
	 */

	private boolean existePorUnaFirma;

	/**
	 * N&uacute;mero de resoluci&oacute;n de la Factura Varia
	 */
	private String resolucionFacturaVaria;

	/**
	 * Prefijo de las facturas Varias
	 */
	private String prefijoFacturaVaria;

	/**
	 * N&uacute;mero inicial de la Factura Varia autorizado por la Dian
	 */
	private BigDecimal rangoInicialFacturaVaria;

	/**
	 * N&uacute;mero final de la Factura Varia autorizado por la Dian
	 */
	private BigDecimal rangoFinalFacturaVaria;

	/**
	 * Encabezado factura Varia
	 */
	private String encabezadoFacturaVaria;

	/**
	 * Pie de página factura Varia
	 */
	private String pieFacturaVaria;

	/**
	 * Indica si se esta manejando el consecutivo propio por Facturas Varias
	 */
	private boolean consecutivoPropioFacturasVarias;
	
	/**
	 * Indica si se maneja consecutivo para Facturas Varias por Centro de Atención
	 */
	private boolean manejaConsecutivoFacturasVariasPorCentroAtencion;
	
	/**
	 * limpiar e inicializar atributos de la clase
	 * 
	 */
	public void reset() {
		
		this.tipoIns = "" + ConstantesBD.codigoNuncaValido + "";
		this.log = "";
		this.codigoT = 0;
		this.codigo = "";
		this.razonSocial = "";
		this.identificacion = "";
		this.digitoVerificacion = "";
		this.departamento = "0";
		this.ciudad = "0";
		this.pais = "0";
		this.direccion = "";
		this.telefono = "";
		this.codMinSalud = "";
		this.actividadEconomica = "";
		this.resolucion = "";
		this.prefijoFacturas = "";
		this.rangoInicialFactura = "";
		this.rangoFinalFactura = "";
		this.colInstitucion = new ArrayList();
		this.acronimoNit = "";
		this.path = "";
		this.encabezado = "";
		this.pie = "";
		this.pieHistoriaClinica = "";
		this.pieAmbMedicamentos = "";
		this.indiceEliminado = ConstantesBD.codigoNuncaValido;
		this.tiposMonedaInstitucionMap = new HashMap();
		this.tiposMonedaInstitucionMap.put("numRegistros", "0");
		this.tiposMonedaInstitucionEliminadosMap = new HashMap();
		this.tiposMonedaInstitucionEliminadosMap.put("numRegistros", "0");
		this.logo = "";
		this.archivo = null;
		this.codEmpTransEsp = "";
		this.ubicacionLogo = "";
		this.extension = "";
		this.indicativo = "";
		this.celular = "";
		this.codigoInterfaz = "";
		this.representanteLegal = "";
		this.piePaginaHistoriClinica = "";
		this.niveLogo = ConstantesBD.acronimoNo;
		this.setDtoContrato(new ContratoOdontologico());
		this
				.setListaFirmasContrato(new ArrayList<DtoFirmasContOtrosInstOdont>());

		this.dtoContrato = new ContratoOdontologico();
		this.dtoContrato.setImprimirFirmasInsti(ConstantesBD.acronimoNo);

		this.postArraylist = ConstantesBD.codigoNuncaValido;

		this.imprimeFirmaEmpresa = "";
		this.representanteLegalMultiempresa = "";

		this.listaDtoFirmEmpresa = new ArrayList<DtoFirmasContOtrsiMultiEmpresa>();
		this.listaMayorCuatro = Boolean.FALSE;

		this.manejaConsecutivosFactCent = Boolean.FALSE;
		this.manejaEspecialidadInsticionOdon = Boolean.FALSE;

		this.existePorUnaFirma = Boolean.FALSE;

		this.imprimeFirmaEmpresaInstitucion = ConstantesBD.acronimoNo;
		
		
		this.resolucionFacturaVaria = "";
		this.prefijoFacturaVaria = "";
		this.setRangoInicialFacturaVaria(null);
		this.setRangoFinalFacturaVaria(null);
		this.encabezadoFacturaVaria = "";
		this.pieFacturaVaria = "";
		
		this.setConsecutivoPropioFacturasVarias(false);
		this.setManejaConsecutivoFacturasVariasPorCentroAtencion(false);
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void resetEmpresas() {
		this.empresa = new HashMap();
		this.setEmpresa("numRegistros", 0);
		this.empresaClone = new HashMap();
		this.archivo = null;
		this.imprimeFirmaEmpresa = "";
		this.representanteLegalMultiempresa = "";
		this.postArrayMultiEmpresa = ConstantesBD.codigoNuncaValido;
		this.listaDtoFirmEmpresa = new ArrayList<DtoFirmasContOtrsiMultiEmpresa>();
	}

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void resetListadoEmpresas() {
		this.listadoEmpresasMap = new HashMap();
		this.index = "";
		this.paises = new ArrayList<HashMap<String, Object>>();
		this.tiposIdent = new ArrayList<HashMap<String, Object>>();
		this.ciudades = new ArrayList<HashMap<String, Object>>();
		this.listadoEmpresasElimMap = new HashMap();
		this.setListadoEmpresasElimMap("numRegistros", 0);
		this.setPostArrayMultiEmpresa(ConstantesBD.codigoNuncaValido);
		this.listaDtoFirmEmpresa = new ArrayList<DtoFirmasContOtrsiMultiEmpresa>();
		this.imprimeFirmaEmpresa = "";
		this.representanteLegalMultiempresa = "";
		this.postArrayMultiEmpresa = ConstantesBD.codigoNuncaValido;
		this.listaDtoFirmEmpresa = new ArrayList<DtoFirmasContOtrsiMultiEmpresa>();

	}

	/**
	 * inicializar la coleccion
	 * 
	 */
	public void resetCollection() {
		this.colInstitucion = new ArrayList();
	}

	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		HashMap consultaEmpresas = new HashMap();
		HashMap criterios = new HashMap();
		// institucion
		criterios.put("institucion3_0", this.getEmpresa("institucion3_0"));
		Connection con = null;
		try {
			con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
					.getConnection();
		} catch (SQLException e) {
			Log4JManager
					.warning("No se pudo abrir la conexión -ParamInstituciónAction"
							+ e.toString());
		}
		consultaEmpresas = ParametrizacionInstitucion.consultarEmpresas(con,
				criterios);

		boolean hayError = false;
		boolean errorRazon = false, errorIdentificacion = false;
		boolean hayErrorFacturaVaria = false;
		boolean hayErrorFactura = false;
		
		if (estado.equals("guardarEmpresa")) {
			// razon social
			if (!this.getEmpresa().containsKey(indisesEmpresa[4])
					|| (this.getEmpresa(indisesEmpresa[4]) + "").equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"La Razón Social "));

			// identificacion
			if (!this.getEmpresa().containsKey(indisesEmpresa[2])
					|| (this.getEmpresa(indisesEmpresa[2]) + "").equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"La Identificación "));

			// tipo identificacion
			if (!this.getEmpresa().containsKey(indisesEmpresa[15])
					|| (this.getEmpresa(indisesEmpresa[15]) + "").equals("")
					|| (this.getEmpresa(indisesEmpresa[15]) + "").equals("-1"))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Tipo Identificación "));

			// pais
			if (!this.getEmpresa().containsKey(indisesEmpresa[21])
					|| (this.getEmpresa(indisesEmpresa[21]) + "").equals("")
					|| (this.getEmpresa(indisesEmpresa[21]) + "").equals("-1"))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El País "));

			// ciudad
			if (!this.getEmpresa().containsKey(indisesEmpresa[29])
					|| (this.getEmpresa(indisesEmpresa[29]) + "").equals("")
					|| (this.getEmpresa(indisesEmpresa[29]) + "").equals("-1"))
				errores.add("descripcion", new ActionMessage("errors.required",
						"La Ciudad "));

			// direccion
			if (!this.getEmpresa().containsKey(indisesEmpresa[7])
					|| (this.getEmpresa(indisesEmpresa[7]) + "").equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"La Dirección "));

			// telefono
			if (!this.getEmpresa().containsKey(indisesEmpresa[8])
					|| (this.getEmpresa(indisesEmpresa[8]) + "").equals(""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Teléfono "));

			// Cod. Habitación Empresa Transporte Especial (cod_emp_trans_esp)
			if (!UtilidadCadena.noEsVacio(this.getEmpresa(indisesEmpresa[34])
					+ ""))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Código Habilitación Empresa Transporte Especial "));

			// logo
			if (!this.getEmpresa().containsKey(indisesEmpresa[31])
					|| !(this.getEmpresa(indisesEmpresa[31]) + "")
							.equals("true"))
				errores.add("descripcion", new ActionMessage("errors.required",
						"El Logo"));

			if (Utilidades.convertirAEntero(consultaEmpresas
					.get("numRegistros")
					+ "") > 0) {
				for (int i = 0; i < Utilidades
						.convertirAEntero(consultaEmpresas.get("numRegistros")
								+ ""); i++) {
					if (!(consultaEmpresas.get("codigo1_" + i) + "")
							.equals((this.getEmpresa(indisesEmpresa[1]) + ""))) {
						if ((consultaEmpresas.get("razonSocial4_" + i) + "")
								.equals((this.getEmpresa(indisesEmpresa[4]) + "")))
							errorRazon = true;
						if ((consultaEmpresas.get("nit2_" + i) + "")
								.equals((this.getEmpresa(indisesEmpresa[2]) + ""))
								&& (consultaEmpresas.get("tipoNit15_" + i) + "")
										.equals(((this
												.getEmpresa(indisesEmpresa[15]) + ""))))
							errorIdentificacion = true;
					}
				}
			}

			if (!UtilidadTexto
					.isEmpty(this.getEmpresa(indisesEmpresa[39]) + "")) {

				if (this.getEmpresa(indisesEmpresa[39]).toString().equals(
						ConstantesBD.acronimoSi)) {
					if (this.listaDtoFirmEmpresa.size() <= 0) {
						errores
								.add(
										"",
										new ActionMessage(
												"errors.notEspecific",
												"Es Requerido que por lo menos se adicione una firma "));
					}
				}
			}

			if (errorRazon)
				errores.add("descripcion", new ActionMessage(
						"error.ingresoTurno.repetido", "La Razón Social "));

			if (errorIdentificacion)
				errores.add("descripcion", new ActionMessage(
						"error.ingresoTurno.repetido", "La Identificación "));


			if(!manejaConsecutivosFactCent){
				
				hayErrorFactura = validarDatosRangosFacturacion(errores, 0);
			}
			
			if(consecutivoPropioFacturasVarias && !manejaConsecutivoFacturasVariasPorCentroAtencion){
				
				hayErrorFacturaVaria = validarDatosRangosFacturacion(errores, 2);
			}
		}

		if (estado.equals("modificar")) {
			if (Integer.parseInt(this.codigo) != this.codigoT) {
				errores.add("", new ActionMessage("errors.noModificado",
						"El Código de la Institución "));
				hayError = true;
			}
		}

		if (estado.equals("insertar")) {
			if (Integer.parseInt(this.codigo) == this.codigoT) {
				errores.add("", new ActionMessage("errors.yaExiste",
						"El Código de la Institución "));
				hayError = true;
			}
		}

		if (estado.equals("insertar") || estado.equals("modificar")) {

			if (this.razonSocial.equals("")
					|| !Utilidades.validarEspacios(this.razonSocial)) {
				errores.add("", new ActionMessage("errors.required",
						"La Razón Social "));
				hayError = true;
			}

			if (this.getEsMultiempresa().equals(ConstantesBD.acronimoNo))
				if (this.identificacion.equals("")
						|| !Utilidades.validarEspacios(this.identificacion)) {
					errores.add("", new ActionMessage("errors.required",
							"La Identificación "));
					hayError = true;
				}
			if (this.direccion.equals("")
					|| !Utilidades.validarEspacios(this.direccion)) {
				errores.add("", new ActionMessage("errors.required",
						"La Dirección "));
				hayError = true;
			}
			if (this.pais.equals("") || !Utilidades.validarEspacios(this.pais)) {
				errores.add("",
						new ActionMessage("errors.required", "El País "));
				hayError = true;
			}
			if (this.telefono.equals("")
					|| !Utilidades.validarEspacios(this.telefono)) {
				errores.add("", new ActionMessage("errors.required",
						"El Número de Teléfono "));
				hayError = true;
			}
			/*
			 * Solución de la tarea: 54807 Se solicita que el campo Cód.
			 * Habilitación Empresa Transporte Especial no se requerido en esta
			 * funcionalidad, debido a que pueden existir instituciones a las
			 * cuales no les aplica. Favor Ajustar, Germán Aguilera Acceptor:
			 * Luis Felipe Perez Granda
			 */
			/*
			 * if(!UtilidadCadena.noEsVacio(this.codEmpTransEsp)) {
			 * errores.add("", newActionMessage("errors.required",
			 * "El Cod. Habitación Empresa Transporte Especial  ")); hayError =
			 * true; }
			 */

			if (this.departamento.equals("-")
					|| !Utilidades.validarEspacios(this.departamento)) {
				errores.add("", new ActionMessage("errors.required",
						"El Departamento "));
				hayError = true;
			}
			if (this.ciudad.equals("-")
					|| !Utilidades.validarEspacios(this.ciudad)
					|| this.ciudad == ConstantesBD.codigoNuncaValido + ""
					|| this.ciudad.equals("")) {
				errores.add("", new ActionMessage("errors.required",
						"La Ciudad "));
				hayError = true;
			}

			try {
				if (Integer.parseInt(this.codigo) < 0) {
					errores.add("numero menor/igual que 0", new ActionMessage(
							"errors.integerMayorIgualQue",
							"El Código de la Institución ", "0"));
					hayError = true;
				}
			} catch (NumberFormatException e) {
				errores.add("numeroNoEntero", new ActionMessage(
						"errors.integer", "El Código de la Institución"));
				hayError = true;
			}

			/*
			 * if(!this.codMinSalud.equals("")) { try {
			 * 
			 * if(Integer.parseInt(this.codMinSalud) < 0) {
			 * errores.add("numero menor/igual que 0", new
			 * ActionMessage("errors.integerMayorIgualQue"
			 * ,"El código MinSalud ","0")); hayError = true; } }
			 * catch(NumberFormatException e) { errores.add("numeroNoEntero",
			 * new ActionMessage("errors.integer", "El código MinSalud"));
			 * hayError = true; } }
			 */
			
			if(!manejaConsecutivosFactCent){
				
				hayErrorFactura = validarDatosRangosFacturacion(errores, 0);
			}
			
			if(consecutivoPropioFacturasVarias && !manejaConsecutivoFacturasVariasPorCentroAtencion){
				
				hayErrorFacturaVaria = validarDatosRangosFacturacion(errores, 1);
			}
		
			
			for (int i = 0; i < Utilidades.convertirAEntero(this
					.getTiposMonedaInstitucionMap("numRegistros")
					+ ""); i++) {
				if (Utilidades.convertirAEntero(this.tiposMonedaInstitucionMap
						.get("tipo_moneda_" + i)
						+ "") == ConstantesBD.codigoNuncaValido) {
					errores.add("", new ActionMessage("errors.required",
							"El Tipo de Moneda"));
					hayError = true;
				}
				if (this.tiposMonedaInstitucionMap.get("fecha_inicial_" + i)
						.equals("")) {
					errores.add("", new ActionMessage("errors.required",
							"La Fecha Inicial del Tipo de Moneda"));
					hayError = true;
				}
				for (int k = 0; k < i; k++) {
					if (this.getTiposMonedaInstitucionMap("estabd_" + k)
							.equals(ConstantesBD.acronimoSi)
							&& (this
									.getTiposMonedaInstitucionMap("fecha_inicial_"
											+ i)
									.equals(this
											.getTiposMonedaInstitucionMap("fecha_inicial_"
													+ k)))) {
						errores
								.add(
										"",
										new ActionMessage(
												"errors.yaExiste",
												"La Fecha Inicial "
														+ this
																.getTiposMonedaInstitucionMap("fecha_inicial_"
																		+ i)));
						hayError = true;
					}
				}
				
				if (!UtilidadTexto.isEmpty(this.getTiposMonedaInstitucionMap(
						"fecha_inicial_" + i).toString())) {
					boolean centinelaErrorFechas = false;

					if (!centinelaErrorFechas) {
						if (!UtilidadFecha
								.esFechaMenorIgualQueOtraReferencia(
										this
												.getTiposMonedaInstitucionMap("fecha_inicial_"
														+ i)
												+ "", UtilidadFecha
												.getFechaActual())) {
							errores
									.add(
											"",
											new ActionMessage(
													"errors.fechaPosteriorIgualActual",
													"Inicial "
															+ this
																	.getTiposMonedaInstitucionMap("fecha_inicial_"
																			+ i),
													"actual "
															+ UtilidadFecha
																	.getFechaActual()));
						}
					}
				}
			}

		
			if (hayError || hayErrorFacturaVaria || hayErrorFactura) {

				this.setEstado("error");
			}

		}

		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return errores;
	}


	/**
	 * Método que valida los rangos Inicial y Final ingresados para la parametrización
	 * de los datos de las Facturas y Facturas Varias.
	 * 
	 * @param errores
	 * @param caso
	 * @return
	 */
	private boolean validarDatosRangosFacturacion(ActionErrors errores, int caso) {
		
		boolean hayError = false;
		boolean hayErrorRango= false;
		String rangoInicialFactura = "";
		String rangoFinalFactura = "";
		String mensaje = "";
		
		if(caso == 0){
			
			rangoInicialFactura = this.rangoInicialFactura;
			rangoFinalFactura = this.rangoFinalFactura;
			mensaje = "Factura";
			
		}else if (caso == 1){
			
			if(this.rangoInicialFacturaVaria!=null){
				
				rangoInicialFactura = this.rangoInicialFacturaVaria.toString();
			}
			
			if(this.rangoFinalFacturaVaria!=null){
				
				rangoFinalFactura = this.rangoFinalFacturaVaria.toString();
			}
			
			mensaje = "Factura Varia";
			
		}else if (caso == 2){
			
			if(this.getEmpresa(indisesEmpresa[42])!=null && 
					!UtilidadTexto.isEmpty(this.getEmpresa(indisesEmpresa[42]).toString())){
				
				rangoInicialFactura = this.getEmpresa(indisesEmpresa[42]).toString();
			}
			
			if(this.getEmpresa(indisesEmpresa[43])!=null && 
					!UtilidadTexto.isEmpty(this.getEmpresa(indisesEmpresa[43]).toString())){
				
				rangoFinalFactura = this.getEmpresa(indisesEmpresa[43]).toString();
			}
			
			mensaje = "Factura Varia";
		}

		if (!rangoInicialFactura.equals("")) {
			try {

				if (Integer.parseInt(rangoInicialFactura) <= 0) {
					errores
							.add(
									"numero menor/igual que 0",
									new ActionMessage(
											"errors.integerMayorQue",
											"El Rango Inicial de la " + mensaje,
											"0"));
					hayError = true;
					hayErrorRango = true;
				}
			} catch (NumberFormatException e) {
				errores
						.add("numeroNoEntero", new ActionMessage(
								"errors.integer",
								"El Rango Inicial de la " + mensaje));
				hayError = true;
				hayErrorRango = true;
			}
		}

		if (!rangoFinalFactura.equals("")) {
			try {
				if (Integer.parseInt(rangoFinalFactura) <= 0) {
					errores.add("numero menor/igual que 0",
							new ActionMessage("errors.integerMayorQue",
									"El Rango Final de la " + mensaje, "0"));
					hayError = true;
					hayErrorRango = true;
				}
			} catch (NumberFormatException e) {
				errores.add("numeroNoEntero", new ActionMessage(
						"errors.integer", "El Rango Final de la " + mensaje));
				hayError = true;
				hayErrorRango = true;
			}
		}
		
		if (!hayErrorRango) {
			hayErrorRango = false;
			if (!rangoInicialFactura.equals("")) {
				if (rangoFinalFactura.equals("")) {
					errores.add("rango final requerido", new ActionMessage(
							"errors.requeridoElOtro",
							"El Rango Inicial de la " + mensaje,
							"El Rango Final de la " + mensaje));
					hayError = true;
					hayErrorRango = true;
				}
			} else {
				if (!rangoFinalFactura.equals("")) {
					errores.add("rango inicial requerido",
							new ActionMessage("errors.requeridoElOtro",
									"El Rango Final de la " + mensaje,
									"El Rango Inicial de la " + mensaje));
					hayError = true;
					hayErrorRango = true;
				}
			}

			if (!hayErrorRango) {
				if (!rangoInicialFactura.equals("")) {
					if (!rangoFinalFactura.equals("")) {
						if (Integer.parseInt(rangoInicialFactura) > Integer
								.parseInt(rangoFinalFactura)) {
							errores.add("dif rangos", new ActionMessage(
									"errors.MayorIgualQue",
									"El Rango Final de la " + mensaje, 
									"El Rango Inicial de la " + mensaje));
							hayError = true;
						}
					}
				}
			}
		}
		
		return hayError;
	}
	
	public String getCodEmpTransEsp() {
		return codEmpTransEsp;
	}

	public void setCodEmpTransEsp(String codEmpTransEsp) {
		this.codEmpTransEsp = codEmpTransEsp;
	}
	

	/**
	 * @return Retorna actividadEconomica.
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}

	/**
	 * @param actividadEconomica
	 *            Asigna actividadEconomica.
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	/**
	 * @return Retorna ciudad.
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad
	 *            Asigna ciudad.
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return Retorna codMinSalud.
	 */
	public String getCodMinSalud() {
		return codMinSalud;
	}

	/**
	 * @param codMinSalud
	 *            Asigna codMinSalud.
	 */
	public void setCodMinSalud(String codMinSalud) {
		this.codMinSalud = codMinSalud;
	}

	/**
	 * @return Retorna departamento.
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            Asigna departamento.
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return Retorna direccion.
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            Asigna direccion.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return Retorna identificacion.
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * @param identificacion
	 *            Asigna identificacion.
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	/**
	 * @return Retorna prefijoFacturas.
	 */
	public String getPrefijoFacturas() {
		return prefijoFacturas;
	}

	/**
	 * @param prefijoFacturas
	 *            Asigna prefijoFacturas.
	 */
	public void setPrefijoFacturas(String prefijoFacturas) {
		this.prefijoFacturas = prefijoFacturas;
	}

	/**
	 * @return Retorna rangoFinalFactura.
	 */
	public String getRangoFinalFactura() {
		return rangoFinalFactura;
	}

	/**
	 * @param rangoFinalFactura
	 *            Asigna rangoFinalFactura.
	 */
	public void setRangoFinalFactura(String rangoFinalFactura) {
		this.rangoFinalFactura = rangoFinalFactura;
	}

	/**
	 * @return Retorna rangoInicialFactura.
	 */
	public String getRangoInicialFactura() {
		return rangoInicialFactura;
	}

	/**
	 * @param rangoInicialFactura
	 *            Asigna rangoInicialFactura.
	 */
	public void setRangoInicialFactura(String rangoInicialFactura) {
		this.rangoInicialFactura = rangoInicialFactura;
	}

	/**
	 * @return Retorna razonSocial.
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial
	 *            Asigna razonSocial.
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return Retorna resolucion.
	 */
	public String getResolucion() {
		return resolucion;
	}

	/**
	 * @param resolucion
	 *            Asigna resolucion.
	 */
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna codigo.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            Asigna codigo.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Retorna colInstitucion.
	 */
	public Collection getColInstitucion() {
		return colInstitucion;
	}

	/**
	 * @param colInstitucion
	 *            Asigna colInstitucion.
	 */
	public void setColInstitucion(Collection colInstitucion) {
		this.colInstitucion = colInstitucion;
	}

	/**
	 * @return Retorna telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            Asigna telefono.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return Retorna codigoT.
	 */
	public int getCodigoT() {
		return codigoT;
	}

	/**
	 * @param codigoT
	 *            Asigna codigoT.
	 */
	public void setCodigoT(int codigoT) {
		this.codigoT = codigoT;
	}

	/**
	 * @return Retorna log.
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param log
	 *            Asigna log.
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * @return Retorna accion.
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion
	 *            Asigna accion.
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
	 * @return Retorna acronimoNit.
	 */
	public String getAcronimoNit() {
		return acronimoNit;
	}

	/**
	 * @param acronimoNit
	 *            Asigna acronimoNit.
	 */
	public void setAcronimoNit(String acronimoNit) {
		this.acronimoNit = acronimoNit;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return Retorna encabezado.
	 */
	public String getEncabezado() {
		return encabezado;
	}

	/**
	 * @param encabezado
	 *            Asigna encabezado.
	 */
	public void setEncabezado(String encabezado) {
		this.encabezado = encabezado;
	}

	/**
	 * @return Retorna pie.
	 */
	public String getPie() {
		return pie;
	}

	/**
	 * @param pie
	 *            Asigna pie.
	 */
	public void setPie(String pie) {
		this.pie = pie;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the tiposMonedaInstitucionMap
	 */
	public HashMap getTiposMonedaInstitucionMap() {
		return tiposMonedaInstitucionMap;
	}

	/**
	 * @param tiposMonedaInstitucionMap
	 *            the tiposMonedaInstitucionMap to set
	 */
	public void setTiposMonedaInstitucionMap(HashMap tiposMonedaInstitucionMap) {
		this.tiposMonedaInstitucionMap = tiposMonedaInstitucionMap;
	}

	public void setTiposMonedaInstitucionMap(String key, Object value) {
		this.tiposMonedaInstitucionMap.put(key, value);
	}

	public Object getTiposMonedaInstitucionMap(String key) {
		return tiposMonedaInstitucionMap.get(key);
	}

	/**
	 * @return the indiceEliminado
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado
	 *            the indiceEliminado to set
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

	/**
	 * @return the tiposMonedaInstitucionEliminadosMap
	 */
	public HashMap getTiposMonedaInstitucionEliminadosMap() {
		return tiposMonedaInstitucionEliminadosMap;
	}

	/**
	 * @param tiposMonedaInstitucionEliminadosMap
	 *            the tiposMonedaInstitucionEliminadosMap to set
	 */
	public void setTiposMonedaInstitucionEliminadosMap(
			HashMap tiposMonedaInstitucionEliminadosMap) {
		this.tiposMonedaInstitucionEliminadosMap = tiposMonedaInstitucionEliminadosMap;
	}

	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	public HashMap getListadoEmpresasMap() {
		return listadoEmpresasMap;
	}

	public void setListadoEmpresasMap(HashMap listadoEmpresasMap) {
		this.listadoEmpresasMap = listadoEmpresasMap;
	}

	public Object getListadoEmpresasMap(String key) {
		return listadoEmpresasMap.get(key);
	}

	public void setListadoEmpresasMap(String key, Object value) {
		this.listadoEmpresasMap.put(key, value);
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public HashMap getEmpresa() {
		return empresa;
	}

	public void setEmpresa(HashMap empresa) {
		this.empresa = empresa;
	}

	public Object getEmpresa(String key) {
		return empresa.get(key);
	}

	public void setEmpresa(String key, Object value) {
		this.empresa.put(key, value);
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public ArrayList<HashMap<String, Object>> getTiposIdent() {
		return tiposIdent;
	}

	public void setTiposIdent(ArrayList<HashMap<String, Object>> tiposIdent) {
		this.tiposIdent = tiposIdent;
	}

	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}

	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}

	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}

	public FormFile getArchivo() {
		return archivo;
	}

	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}

	public HashMap getEmpresaClone() {
		return empresaClone;
	}

	public void setEmpresaClone(HashMap empresaClone) {
		this.empresaClone = empresaClone;
	}

	public HashMap getListadoEmpresasElimMap() {
		return listadoEmpresasElimMap;
	}

	public void setListadoEmpresasElimMap(HashMap listadoEmpresasElimMap) {
		this.listadoEmpresasElimMap = listadoEmpresasElimMap;
	}

	public Object getListadoEmpresasElimMap(String key) {
		return listadoEmpresasElimMap.get(key);
	}

	public void setListadoEmpresasElimMap(String key, Object value) {
		this.listadoEmpresasElimMap.put(key, value);
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the tipoIns
	 */
	public String getTipoIns() {
		return tipoIns;
	}

	/**
	 * @param tipoIns
	 *            the tipoIns to set
	 */
	public void setTipoIns(String tipoIns) {
		this.tipoIns = tipoIns;
	}

	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * @return the pieHistoriaClinica
	 */
	public String getPieHistoriaClinica() {
		return pieHistoriaClinica;
	}

	/**
	 * @param pieHistoriaClinica
	 *            the pieHistoriaClinica to set
	 */
	public void setPieHistoriaClinica(String pieHistoriaClinica) {
		this.pieHistoriaClinica = pieHistoriaClinica;
	}

	public String getIndicativo() {
		return indicativo;
	}

	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public void setRepresentanteLegal(String representanteLegal) {
		this.representanteLegal = representanteLegal;
	}

	public String getRepresentanteLegal() {
		return representanteLegal;
	}

	public void setPiePaginaHistoriClinica(String piePaginaHistoriClinica) {
		this.piePaginaHistoriClinica = piePaginaHistoriClinica;
	}

	public String getPiePaginaHistoriClinica() {
		return piePaginaHistoriClinica;
	}

	public void setNiveLogo(String niveLogo) {
		this.niveLogo = niveLogo;
	}

	public String getNiveLogo() {
		return niveLogo;
	}

	public void setDtoContrato(ContratoOdontologico dtoContrato) {
		this.dtoContrato = dtoContrato;
	}

	public ContratoOdontologico getDtoContrato() {
		return dtoContrato;
	}

	public void setListaFirmasContrato(
			ArrayList<DtoFirmasContOtrosInstOdont> listaFirmasContrato) {
		this.listaFirmasContrato = listaFirmasContrato;
	}

	public ArrayList<DtoFirmasContOtrosInstOdont> getListaFirmasContrato() {
		return listaFirmasContrato;
	}

	public void setPostArraylist(int postArraylist) {
		this.postArraylist = postArraylist;
	}

	public int getPostArraylist() {
		return postArraylist;
	}

	/**
	 * @return the imprimeFirmaEmpresa
	 */
	public String getImprimeFirmaEmpresa() {
		return imprimeFirmaEmpresa;
	}

	/**
	 * @param imprimeFirmaEmpresa
	 *            the imprimeFirmaEmpresa to set
	 */
	public void setImprimeFirmaEmpresa(String imprimeFirmaEmpresa) {
		this.imprimeFirmaEmpresa = imprimeFirmaEmpresa;
	}

	/**
	 * @return the representanteLegalMultiempresa
	 */
	public String getRepresentanteLegalMultiempresa() {
		return representanteLegalMultiempresa;
	}

	/**
	 * @param representanteLegalMultiempresa
	 *            the representanteLegalMultiempresa to set
	 */
	public void setRepresentanteLegalMultiempresa(
			String representanteLegalMultiempresa) {
		this.representanteLegalMultiempresa = representanteLegalMultiempresa;
	}

	public void setPostArrayMultiEmpresa(int postArrayMultiEmpresa) {
		this.postArrayMultiEmpresa = postArrayMultiEmpresa;
	}

	public int getPostArrayMultiEmpresa() {
		return postArrayMultiEmpresa;
	}

	public void setListaDtoFirmEmpresa(
			ArrayList<DtoFirmasContOtrsiMultiEmpresa> listaDtoFirmEmpresa) {
		this.listaDtoFirmEmpresa = listaDtoFirmEmpresa;
	}

	public ArrayList<DtoFirmasContOtrsiMultiEmpresa> getListaDtoFirmEmpresa() {
		return listaDtoFirmEmpresa;
	}

	public void setListaMayorCuatro(boolean listaMayorCuatro) {
		this.listaMayorCuatro = listaMayorCuatro;
	}

	public boolean isListaMayorCuatro() {
		return listaMayorCuatro;
	}

	public void setManejaConsecutivosFactCent(boolean manejaConsecutivosFactCent) {
		this.manejaConsecutivosFactCent = manejaConsecutivosFactCent;
	}

	public Boolean getManejaConsecutivosFactCent() {
		return manejaConsecutivosFactCent;
	}

	public void setManejaEspecialidadInsticionOdon(
			boolean manejaEspecialidadInsticionOdon) {
		this.manejaEspecialidadInsticionOdon = manejaEspecialidadInsticionOdon;
	}

	public boolean isManejaEspecialidadInsticionOdon() {
		return manejaEspecialidadInsticionOdon;
	}

	public void setExistePorUnaFirma(boolean existePorUnaFirma) {
		this.existePorUnaFirma = existePorUnaFirma;
	}

	public boolean isExistePorUnaFirma() {
		return existePorUnaFirma;
	}

	public static String getMensajeErrorcontrato() {
		return mensajeErrorContrato;
	}

	public void setImprimeFirmaEmpresaInstitucion(
			String imprimeFirmaEmpresaInstitucion) {
		this.imprimeFirmaEmpresaInstitucion = imprimeFirmaEmpresaInstitucion;
	}

	public String getImprimeFirmaEmpresaInstitucion() {
		return imprimeFirmaEmpresaInstitucion;
	}

	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}

	/**
	 * @param resolucionFacturaVaria the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}

	/**
	 * @return the prefijoFacturaVaria
	 */
	public String getPrefijoFacturaVaria() {
		return prefijoFacturaVaria;
	}

	/**
	 * @param prefijoFacturaVaria the prefijoFacturaVaria to set
	 */
	public void setPrefijoFacturaVaria(String prefijoFacturaVaria) {
		this.prefijoFacturaVaria = prefijoFacturaVaria;
	}

	
	

	/**
	 * @return the encabezadoFacturaVaria
	 */
	public String getEncabezadoFacturaVaria() {
		return encabezadoFacturaVaria;
	}

	/**
	 * @param encabezadoFacturaVaria the encabezadoFacturaVaria to set
	 */
	public void setEncabezadoFacturaVaria(String encabezadoFacturaVaria) {
		this.encabezadoFacturaVaria = encabezadoFacturaVaria;
	}

	/**
	 * @return the pieFacturaVaria
	 */
	public String getPieFacturaVaria() {
		return pieFacturaVaria;
	}

	/**
	 * @param pieFacturaVaria the pieFacturaVaria to set
	 */
	public void setPieFacturaVaria(String pieFacturaVaria) {
		this.pieFacturaVaria = pieFacturaVaria;
	}


	/**
	 * @param consecutivoPropioFacturasVarias the consecutivoPropioFacturasVarias to set
	 */
	public void setConsecutivoPropioFacturasVarias(
			boolean consecutivoPropioFacturasVarias) {
		this.consecutivoPropioFacturasVarias = consecutivoPropioFacturasVarias;
	}


	/**
	 * @return the consecutivoPropioFacturasVarias
	 */
	public boolean isConsecutivoPropioFacturasVarias() {
		return consecutivoPropioFacturasVarias;
	}


	/**
	 * @param manejaConsecutivoFacturasVariasPorCentroAtencion the manejaConsecutivoFacturasVariasPorCentroAtencion to set
	 */
	public void setManejaConsecutivoFacturasVariasPorCentroAtencion(
			boolean manejaConsecutivoFacturasVariasPorCentroAtencion) {
		this.manejaConsecutivoFacturasVariasPorCentroAtencion = manejaConsecutivoFacturasVariasPorCentroAtencion;
	}


	/**
	 * @return the manejaConsecutivoFacturasVariasPorCentroAtencion
	 */
	public boolean isManejaConsecutivoFacturasVariasPorCentroAtencion() {
		return manejaConsecutivoFacturasVariasPorCentroAtencion;
	}


	/**
	 * @param rangoInicialFacturaVaria the rangoInicialFacturaVaria to set
	 */
	public void setRangoInicialFacturaVaria(BigDecimal rangoInicialFacturaVaria) {
		this.rangoInicialFacturaVaria = rangoInicialFacturaVaria;
	}


	/**
	 * @return the rangoInicialFacturaVaria
	 */
	public BigDecimal getRangoInicialFacturaVaria() {
		return rangoInicialFacturaVaria;
	}


	/**
	 * @param rangoFinalFacturaVaria the rangoFinalFacturaVaria to set
	 */
	public void setRangoFinalFacturaVaria(BigDecimal rangoFinalFacturaVaria) {
		this.rangoFinalFacturaVaria = rangoFinalFacturaVaria;
	}


	/**
	 * @return the rangoFinalFacturaVaria
	 */
	public BigDecimal getRangoFinalFacturaVaria() {
		return rangoFinalFacturaVaria;
	}


	public void setPieAmbMedicamentos(String pieAmbMedicamentos) {
		this.pieAmbMedicamentos = pieAmbMedicamentos;
	}


	public String getPieAmbMedicamentos() {
		return pieAmbMedicamentos;
	}

}
