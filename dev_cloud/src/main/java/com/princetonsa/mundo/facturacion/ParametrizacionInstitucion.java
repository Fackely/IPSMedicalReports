/*
 * Creado   19/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.facturacion;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosString;
import util.Listado;
import util.RangosConsecutivos;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ParamInstitucionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.contrato.ContratoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresasServicio;

/**
 * Clase para el manejo de la información general de la institución, información
 * básica para tener en cuenta en diferentes módulos/funcionalidades.
 * 
 * @version 1.0, 19/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ParametrizacionInstitucion {
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger
			.getLogger(ParametrizacionInstitucion.class);

	/**
	 * Indices para el manejo de Empresas
	 */
	public static final String[] indicesEmpresa = { "null0_", "codigo1_",
			"nit2_", "institucion3_", "razonSocial4_", "depto5_", "ciudad6_",
			"direccion7_", "telefono8_", "minSalud9_", "actividad10_",
			"resolucion11_", "prefijo12_", "rangoInicial13_", "rangoFinal14_",
			"tipoNit15_", "nombreTipoNit16_", "logo17_", "vigente18_",
			"encabezado19_", "pie20_", "pais21_", "desTipoIdent22_",
			"nombreDepto23_", "nombreCiudad24_", "nombrePais25_", "estaBd26_",
			"usuarioModifica27_", "descEnt28_", "nomCiudad29_", "esUsada30_",
			"existLogo31_", "digv32_", "tipoins33_", "codEmpTransEsp34_",
			"ubicacionLogoReportes35_", "pieHisCli36_", "codinterfaz37_",
			"representante38_", "imprimirFirmasEmp39_",
			"resolucionFacturaVaria_", "prefFacturaVaria_", //40 , 41
			"rgoInicFactVaria_", "rgoFinFactVaria_", //42 , 43
			"encabezadoFacturaVaria_", "pieFacturaVaria_",  //44 , 45
			"pieAmbMed_" };//46 FIXME

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ParamInstitucionDao institucionDao;

	/**
	 * Código Axioma de la institucion
	 */
	private int codigo;
	/**
	 * raz&oacute;n social de la instituci&oacute;n.
	 */
	private String razonSocial;

	/**
	 * identificaci&oacute;n de la instituci&oacute;n
	 */
	private String identificacion;

	/**
	 * Pais de la insttucion
	 */
	private InfoDatosString pais;

	/**
	 * departamento de ubicaci&oacute;n de la instituci&oacute;n (c&oacute;digo
	 * y descripci&oacute;n)
	 */
	private InfoDatosString departamento;

	/**
	 * ciudad de ubicaci&oacute;n de la instituci&oacute;n (c&oacute;digo y
	 * descripci&oacute;n)
	 */
	private InfoDatosString ciudad;

	/**
	 * Direcci&oacute;n de la instituci&oacute;n
	 */
	private String direccion;

	/**
	 * Telefono de la instituci&oacute;n
	 */
	private String Telefono;

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
	 * nit de la institucion
	 */
	private String nit;

	/**
	 * Digito Verificación
	 */
	private String digitoVerificacion;

	/**
	 * Campo que almacena el formato impresion de la cuenta de cobro
	 */
	private String formatoImpresion;

	private String indicativo;
	private String extension;
	/**
	 * Path para la generación de archivos rips
	 */
	private String path;
	private String pie;

	private String pieHisCli;
	private String pieAmbMed;

	private String encabezado;
	private String descripcionTipoIdentificacion;

	private String logoJsp;
	
	private String logoReportes;

	private String ubicacionLogo = "";

	private String codigoInterfaz;

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
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	private String tipoIns;

	/**
	 * @return Retorna descripcionTipoIdentificacion.
	 */
	public String getDescripcionTipoIdentificacion() {
		return descripcionTipoIdentificacion;
	}

	/**
	 * @return Retorna encabezado.
	 */
	public String getEncabezado() {
		return encabezado;
	}

	/**
	 * @return Retorna pie.
	 */
	public String getPie() {
		return pie;
	}

	/**
	 * limpiar e inicializar atributos de la clase
	 * 
	 */
	public void reset() {
		this.razonSocial = "";
		this.identificacion = "";
		this.pais = new InfoDatosString();
		this.departamento = new InfoDatosString();
		this.ciudad = new InfoDatosString();
		this.pais = new InfoDatosString();
		this.direccion = "";
		this.Telefono = "";
		this.codMinSalud = "";
		this.actividadEconomica = "";
		this.resolucion = "";
		this.prefijoFacturas = "";
		this.rangoInicialFactura = "";
		this.rangoFinalFactura = "";
		this.path = "";
		this.nit = "";
		this.digitoVerificacion = "";
		this.formatoImpresion = "";
		this.codigo = 0;
		this.pie = "";
		this.encabezado = "";
		this.descripcionTipoIdentificacion = "";
		this.setLogoJsp("");
		this.setLogoReportes("");
		this.tipoIns = "" + ConstantesBD.codigoNuncaValido;
		this.ubicacionLogo = "";
		this.indicativo = "";
		this.extension = "";
		this.codigoInterfaz = "";

		this.resolucionFacturaVaria = "";
		this.prefijoFacturaVaria = "";
		this.rangoInicialFacturaVaria = null;
		this.rangoFinalFacturaVaria = null;
		this.encabezadoFacturaVaria = "";
		this.pieFacturaVaria = "";
		this.pieAmbMed="";
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su
	 * respectivo DAO.
	 * 
	 * @param tipoBD
	 *            el tipo de base de datos que va a usar este objeto (e.g.,
	 *            Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD son
	 *            los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code>
	 *         si no.
	 */
	public boolean init(String tipoBD) {
		boolean wasInited = false;

		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null) {
			institucionDao = myFactory.getParamInstitucionDao();
			wasInited = (institucionDao != null);
		}
		return wasInited;
	}

	/**
	 * Constructor de la clase
	 * 
	 */
	public ParametrizacionInstitucion() {
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerPrefijoFacturas(Connection con,
			int codigoInstitucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().obtenerPrefijoFacturas(con,
						codigoInstitucion);
	}

	/**
	 * Consultar los tipos de moneda por institucion
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerEmpresasInstitucion(Connection con,
			int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().obtenerEmpresasInstitucion(con,
						institucion);
	}

	/**
	 * Consultar los tipos de moneda por institucion
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerEmpresasInstitucion(int institucion) {
		Connection con = UtilidadBD.abrirConexion();
		HashMap mapa = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().obtenerEmpresasInstitucion(con,
						institucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	/**
	 * Metodo encargado de insertar Empresas En la BD
	 * 
	 * @param connection
	 * @param datos
	 *            ------------------------------------------ KEY'S HASHMAP DATOS
	 *            ------------------------------------------ -- nit2_ -->
	 *            Requerido -- institucion3_ --> Requerido -- razonSocial4_ -->
	 *            Requerido -- depto5_ --> Requerido -- ciudad6_ --> Requerido
	 *            -- direccion7_ --> Requerido -- telefono8_ --> Requerido --
	 *            minSalud9_ --> Opcional -- actividad10_ --> Opcional --
	 *            resolucion11_ --> Opcional -- prefijo12_ --> Opcional --
	 *            rangoInicial13_ --> Opcional -- rangoFinal14_ --> Opcional --
	 *            tipoNit15_ --> Requerido -- logo17_ --> Requerido --
	 *            vigente18_ --> Requerido -- encabezado19_ --> Opcional --
	 *            pie20_ --> Opcional -- pais21_ --> Requerido --
	 *            usuarioModifica27_ --> Requerido
	 * @return
	 */
	public static double insertarEmpresas(Connection connection, HashMap datos) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().insertarEmpresas(connection, datos);
	}

	/**
	 * Metodo encargado de modificar los datos de la Empresas En la BD
	 * 
	 * @param connection
	 * @param datos
	 *            ------------------------------------------ KEY'S HASHMAP DATOS
	 *            ------------------------------------------ -- nit2_ -->
	 *            Requerido -- razonSocial4_ --> Requerido -- depto5_ -->
	 *            Requerido -- ciudad6_ --> Requerido -- direccion7_ -->
	 *            Requerido -- telefono8_ --> Requerido -- minSalud9_ -->
	 *            Opcional -- actividad10_ --> Opcional -- resolucion11_ -->
	 *            Opcional -- prefijo12_ --> Opcional -- rangoInicial13_ -->
	 *            Opcional -- rangoFinal14_ --> Opcional -- tipoNit15_ -->
	 *            Requerido -- logo17_ --> Requerido -- vigente18_ --> Requerido
	 *            -- encabezado19_ --> Opcional -- pie20_ --> Opcional --
	 *            pais21_ --> Requerido -- usuarioModifica27_ --> Requerido --
	 *            codigo1_ --> Requerido
	 * 
	 * @return
	 */
	public static boolean modificarEmpresas(Connection connection, HashMap datos) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().modificarEmpresas(connection, datos);
	}

	/**
	 * Metodo encargado de consultar las empresas pertenecientes a una
	 * institucion.
	 * 
	 * @param connection
	 * @param criterios
	 *            -------------------------------------- KEY'S DEL HASHMAP
	 *            CRITERIOS -------------------------------------- -- codigo1_
	 *            -- institucion3_
	 * 
	 * @return HashMap mapa ---------------------------------- KEY'S DEL HASHMAP
	 *         MAPA ---------------------------------- codigo1_, nit2_ ,
	 *         institucion3_ , razonSocial4_, depto5_, ciudad6_, direccion7_,
	 *         telefono8_, minSalud9_, actividad10_, resolucion11_, prefijo12_,
	 *         rangoInicial13_, rangoFinal14_, tipoNit15_, nombreTipoNit16_,
	 *         logo17_, vigente18_, encabezado19_, pie20_, pais21_,
	 *         desTipoIdent22_, nombreDepto23_, nombreCiudad24_, nombrePais25_,
	 *         estaBd26_
	 */
	public static HashMap consultarEmpresas(Connection connection,
			HashMap criterios) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().consultarEmpresas(connection,
						criterios);
	}

	/**
	 * Método para llenar este objeto Mundo de Institucion Dado su codigo
	 * 
	 * @param con
	 *            conexión
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public boolean cargar(Connection con, int codigoInstitucion) {
		ResultSetDecorator rs=null;
		try {
			rs = institucionDao.consultaInstituciones(con,
					codigoInstitucion, "0", "0", "0", false);
			this.codigo = codigoInstitucion;
			if (rs.next()) {
				this.razonSocial = rs.getString("razon");
				this.departamento.setCodigo(rs.getString("depto"));
				this.departamento.setNombre(rs.getString("nombreDepto"));
				this.ciudad.setCodigo(rs.getString("ciudad"));
				this.ciudad.setNombre(rs.getString("nombreCiudad"));
				this.pais.setCodigo(rs.getString("pais"));
				this.pais.setNombre(rs.getString("nombrePais"));
				this.direccion = rs.getString("direccion");
				this.Telefono = rs.getString("telefono");
				this.codMinSalud = rs.getString("minSalud");
				this.actividadEconomica = rs.getString("actividad");
				this.prefijoFacturas = rs.getString("prefijo");
				this.identificacion = rs.getString("tipo_nit");
				// this.identificacion=rs.getString("nombre_nit");
				this.resolucion = rs.getString("resolucion");
				this.path = rs.getString("path");
				this.nit = rs.getString("nit");
				this.digitoVerificacion = rs.getString("digv");
				this.pie = rs.getString("pie");
				this.pieHisCli = rs.getString("pie_his_cli");
				this.encabezado = rs.getString("encabezado");
				this.descripcionTipoIdentificacion = rs
						.getString("descripcionTipoIdentificacion");
				this.rangoInicialFactura = rs.getString("rango_inicial");
				this.rangoFinalFactura = rs.getString("rango_final");
				this.formatoImpresion = rs.getString("formato_impresion");
				this.tipoIns = rs.getString("tipo_institucion");
				this.ubicacionLogo = rs.getString("ubicacion_logo_reportes");
				this.codigoInterfaz = rs.getString("codinterfaz");

				this.indicativo = rs.getString("indicativo");
				this.extension = rs.getString("extension");

				this.resolucionFacturaVaria = rs.getString("resolucion_factura_varia");
				this.prefijoFacturaVaria = rs.getString("pref_factura_varia");
				this.rangoInicialFacturaVaria = rs.getBigDecimal("rgo_inic_fact_varia");
				this.rangoFinalFacturaVaria = rs.getBigDecimal("rgo_fin_fact_varia");
				this.encabezadoFacturaVaria = rs.getString("encabezado_factura_varia");
				this.pieFacturaVaria = rs.getString("pie_factura_varia");
				this.pieAmbMed = rs.getString("pie_amb_med");
				 

				if (!UtilidadFileUpload.existeArchivoRutaCompelta(rs.getString("logo"))) {
					this.setLogoJsp("");
					this.setLogoReportes("");
				} else {
					//mt6829
					String logoJsp = rs.getString("logo").substring(
							rs.getString("logo").lastIndexOf(File.separator));
					this.setLogoJsp(System.getProperty("directorioImagenes")
							+ System.getProperty("file.separator") + logoJsp);
					this.setLogoReportes(rs.getString("logo"));
				}

				return true;
			} else
				return false;
		} catch (SQLException e) {
			logger
					.error("Error en el método cargar de ParametrizacionInstituciones: "
							+ e);
			return false;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(null, rs, null);
		}
	}

	/**
	 * Inserta los datos de una instituciï¿½n
	 * 
	 * @param path
	 * @param encabezado
	 *            @todo
	 * @param pie
	 *            @todo
	 * @param string4
	 * @param string3
	 * @param j
	 * @param i
	 * @param string2
	 * @param resolucionFacturaVaria
	 * @param con
	 *            , Connection con la fuente de datos
	 * @param codigo
	 *            , Cï¿½digo de la instituciï¿½n
	 * @param nit
	 *            , Nit de la instituciï¿½n
	 * @param razon
	 *            , Razon Social de la instituciï¿½n
	 * @param depto
	 *            , Departamento al que pertenece la ciudad
	 * @param ciudad
	 *            , Ciudad de la instituciï¿½n
	 * @param direccion
	 *            , Direcciï¿½n de la instituciï¿½n
	 * @param telefono
	 *            , Telefono de la instituciï¿½n
	 * @param codMinSalud
	 *            , Codigo del Ministerio de Salud
	 * @param actividadEco
	 *            , Actividad Economica
	 * @param resolucion
	 *            , Nï¿½mero de Resoluciï¿½n
	 * @param prefijo
	 *            , Prefijo de la factura
	 * @param rangoInic
	 *            , Rango inicial de la factura
	 * @param rangoFin
	 *            , Rango Final de la factura
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.ParamInstitucionDao#insertar(java.sql.Connection,
	 *      int,String,String,int,int,String,String,int,String,String,int,String,int,int)
	 */
	public boolean insertarDatosInst(Connection con, int codigo, String nit,
			String razon, String depto, String ciudad, String direccion,
			String telefono, String codMinSalud, String actividadEco,
			String resolucion, String prefijo, int rangoInic, int rangoFin,
			String path, String encabezado, String pie, String pais,
			String logo, String tipoins, String codEmpTransEsp,
			String ubicacionLogo, String indicativo, String extension,
			String celular, String codigoInterfaz, String representanteLegal,
			String nivelLogo, String resolucionFacturaVaria,
			String prefijoFacturaVaria, BigDecimal rangoInicFacturaVaria,
			BigDecimal rangoFinFacturaVaria, String encabezadoFacturaVaria,
			String pieFacturaVaria) throws SQLException {
		
		boolean inserto = false;
		int resp = 0;
		
		if (institucionDao == null) {
			
			throw new SQLException(
					"No se pudo inicializar la conexiï¿½n con la fuente de datos (ParamInstitucionDao - insertarDatosInst)");
		}

		resp = institucionDao.insertarInstitucion(con, codigo, nit, razon,
				depto, ciudad, direccion, telefono, codMinSalud, actividadEco,
				resolucion, prefijo, rangoInic, rangoFin, path, encabezado,
				pie, pais, logo, tipoins, codEmpTransEsp, ubicacionLogo,
				indicativo, extension, celular, codigoInterfaz,
				representanteLegal, nivelLogo, resolucionFacturaVaria,
				prefijoFacturaVaria, rangoInicFacturaVaria,
				rangoFinFacturaVaria, encabezadoFacturaVaria, pieFacturaVaria);
		
		if (resp > 0)
			inserto = true;

		else if (resp < 1) {
			logger.warn("Error insertando institucion");
			return false;
		}

		return inserto;
	}

	/**
	 * Metodo que realiza la consulta de uno ï¿½ varios registros de
	 * instituciones.
	 * 
	 * @param con
	 *            , Connection con la fuente de datos.
	 * @param codigo
	 *            , Codigo de la instituciï¿½n.
	 * @param codigo_depto
	 *            , Codigo del departamento.
	 * @param codigo_ciudad
	 *            , Codigo de la ciudad
	 * @param consultaUno
	 *            , Boolean indica que tipo de consulta se realiza.
	 * @return Collection, con el resultado.
	 * @see com.princetonsa.dao.sqlbase.SqlBaseParamInstitucionDao#consultaInstituciones(java.sql.Connection,int,int,int,boolean)
	 */
	public Collection consultarInstitucion(Connection con, int codigo,
			String codigo_pais, String codigo_depto, String codigo_ciudad,
			boolean consultaUno) {
		Collection col = null;
		institucionDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao();
		try {
			col = UtilidadBD.resultSet2Collection(institucionDao
					.consultaInstituciones(con, codigo, codigo_pais,
							codigo_depto, codigo_ciudad, consultaUno));

			/*
			 * Iterator<HashMap> it = col.iterator(); while(it.hasNext()) {
			 * HashMap d = (HashMap)it.next();
			 * if(!UtilidadFileUpload.existeArchivoRutaCompelta
			 * (d.get("logo").toString())) d.set("logo",""); }
			 */
		} catch (Exception e) {
			logger
					.warn("Error mundo ParametrizacionInstitucion con el codigo->"
							+ codigo + " " + e.toString());
			col = null;
		}

		return col;
	}

	/**
	 * modifica los datos de una instituciï¿½n
	 * 
	 * @param path
	 * @param encabezado
	 *            @todo
	 * @param pie
	 *            @todo
	 * @param con
	 *            , Connection con la fuente de datos
	 * @param codigo
	 *            , Cï¿½digo de la instituciï¿½n
	 * @param nit
	 *            , Nit de la instituciï¿½n
	 * @param nombreNit
	 *            , String con el nombre del tipo de identificacion
	 * @param razon
	 *            , Razon Social de la instituciï¿½n
	 * @param depto
	 *            , Departamento al que pertenece la ciudad
	 * @param ciudad
	 *            , Ciudad de la instituciï¿½n
	 * @param direccion
	 *            , Direcciï¿½n de la instituciï¿½n
	 * @param telefono
	 *            , Telefono de la instituciï¿½n
	 * @param codMinSalud
	 *            , Codigo del Ministerio de Salud
	 * @param actividadEco
	 *            , Actividad Economica
	 * @param resolucion
	 *            , Nï¿½mero de Resoluciï¿½n
	 * @param prefijo
	 *            , Prefijo de la factura
	 * @param rangoInic
	 *            , Rango inicial de la factura
	 * @param rangoFin
	 *            , Rango Final de la factura
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.ParamInstitucionDao#insertar(java.sql.Connection,
	 *      int,String,String,int,int,String,String,int,String,String,int,String,int,int)
	 */
	public boolean modificarDatosInst(Connection con, int codigo, String nit,
			String digv, String nombreNit, String razon, String depto,
			String ciudad, String direccion, String telefono,
			String codMinSalud, String actividadEco, String resolucion,
			String prefijo, int rangoInic, int rangoFin, String path,
			String encabezado, String pie, String pieHisCli, String pais,
			String logo, String tipoins, String codEmpTransEsp,
			String ubicacionLogo, String indicativo, String extension,
			String celular, String codigoInterfaz, String representanteLegal,
			String nivelLogo, String resolucionFacturaVaria,
			String prefijoFacturaVaria, BigDecimal rangoInicFacturaVaria,
			BigDecimal rangoFinFacturaVaria, String encabezadoFacturaVaria,
			String pieFacturaVaria,String pieAmbMedicamentos) {
		
		boolean resp = false;
		boolean resp1 = false;
		
		resp = institucionDao.modificar(con, codigo, nit, digv, nombreNit,
				razon, depto, ciudad, direccion, telefono, codMinSalud,
				actividadEco, resolucion, prefijo, rangoInic, rangoFin, path,
				encabezado, pie, pieHisCli, pais, logo, tipoins,
				codEmpTransEsp, ubicacionLogo, indicativo, extension, celular,
				codigoInterfaz, representanteLegal, nivelLogo,
				resolucionFacturaVaria, prefijoFacturaVaria,
				rangoInicFacturaVaria, rangoFinFacturaVaria,
				encabezadoFacturaVaria, pieFacturaVaria,pieAmbMedicamentos);

		if (resp)
			resp1 = true;

		else if (!resp) {
			logger.warn("Error modificando institución");
			resp1 = false;
		}

		return resp1;
	}

	/**
	 * Insertar tipos de monedas por institucin
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTiposMonedaInstitucion(Connection con, HashMap vo) {
		return institucionDao.insertarTiposMonedaInstitucion(con, vo);
	}

	/**
	 * Consultar tipos de monedas por institucion
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarTiposMonedaInstitucion(Connection con,
			int institucion) {
		return institucionDao.consultarTiposMonedaInstitucion(con, institucion);
	}

	/**
	 * Modificar tipos de monedas por institucion
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarTiposMonedaInstitucion(Connection con, HashMap vo) {
		return institucionDao.modificarTiposMonedaInstitucion(con, vo);
	}

	/**
	 * Elimianr los tipos de monedas por institucion
	 * 
	 * @param con
	 * @param tipoMoneda
	 * @return
	 */
	public boolean eliminarTiposMonedaInstitucion(Connection con, int tipoMoneda) {
		return institucionDao.eliminarTiposMonedaInstitucion(con, tipoMoneda);
	}

	/**
	 * Metodo que mustra si una empresa esta siendo usada o no, para poderla
	 * eliminar.
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esEmpresaUsada(Connection con, String codigo) {
		return institucionDao.esEmpresaUsada(con, codigo);
	}

	/**
	 * Metodo encargado de eliminar una empresa
	 * 
	 * @param connection
	 * @param datos
	 *            ----------------------- KEY'S HASHMAP DATOS
	 *            ----------------------- -- codigo1_
	 * @return
	 */
	public static boolean eliminarEmpresas(Connection connection, HashMap datos) {
		return institucionDao.eliminarEmpresas(connection, datos);
	}

	/**
	 * Metodo utilizado para cargar el listado de las empresas ya
	 * parametrizadas.
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward listarEmpresas(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		HashMap criterios = new HashMap();
		forma.resetListadoEmpresas();

		// institucion
		criterios.put(indicesEmpresa[3], usuario.getCodigoInstitucionInt());

		forma.setListadoEmpresasMap(consultarEmpresas(connection, criterios));

		initSelects(connection, forma, usuario);

		if (Integer.parseInt(forma.getListadoEmpresasMap().get("numRegistros")
				+ "") > 0) {
			for (int i = 0; i < Integer.parseInt(forma.getListadoEmpresasMap()
					.get("numRegistros")
					+ ""); i++)
				forma.setListadoEmpresasMap(indicesEmpresa[30] + i,
						esEmpresaUsada(connection, forma
								.getListadoEmpresasMap(indicesEmpresa[1] + i)
								+ ""));
			return mapping.findForward("listadoEmpresas");
		} else
			return accionNuevo(connection, forma, mapping);
	}

	/**
	 * Metodo encargado de cargar una empresa utilizando el index
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static String cargarEmpresa(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping) {
		String codigoEmpresa = "";

		forma.resetEmpresas();

		for (int i = 0; i < indicesEmpresa.length; i++) {
			if (forma.getListadoEmpresasMap().containsKey(
					indicesEmpresa[i] + forma.getIndex())) {

				if (i == 1) {
					codigoEmpresa = forma.getListadoEmpresasMap(
							indicesEmpresa[i] + forma.getIndex()).toString();
				}

				Log4JManager.info("indice -->"
						+ indicesEmpresa[i]
						+ " Mapa--->"
						+ forma.getListadoEmpresasMap(indicesEmpresa[i]
								+ forma.getIndex()));

				forma.setEmpresa(indicesEmpresa[i], forma
						.getListadoEmpresasMap(indicesEmpresa[i]
								+ forma.getIndex()));

			}
		}

		if (!forma.getEmpresa(indicesEmpresa[21]).equals("-1"))
			cargarCiudadXpais(connection, forma);

		// se pregunta si el logo si existe o no.
		forma.setEmpresa(indicesEmpresa[31], UtilidadFileUpload.existeArchivo(
				ValoresPorDefecto.getFilePath() + "logos"
						+ System.getProperty("file.separator"), (forma
						.getEmpresa(indicesEmpresa[17]) + "")));
		// se saca una copia el hashmap original
		forma.setEmpresaClone(Listado.copyMap(forma.getEmpresa(),
				indicesEmpresa));

		return codigoEmpresa;
	}

	/**
	 * Metodo encargado de inicializar los select's
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void initSelects(Connection connection,
			ParamInstitucionForm forma, UsuarioBasico usuario) {
		logger.info("\n entro a initselects ");
		forma.setTiposIdent(Utilidades.obtenerTiposIdentificacion(connection,
				"", usuario.getCodigoInstitucionInt()));
		forma.setPaises(Utilidades.obtenerPaises(connection));
		if (forma.getEmpresa().containsKey(indicesEmpresa[21])
				&& !(forma.getEmpresa(indicesEmpresa[21]) + "").equals("")
				&& !(forma.getEmpresa(indicesEmpresa[21]) + "").equals("-1"))
			cargarCiudadXpais(connection, forma);
	}

	/**
	 * Metodo encargado de cargar la cuidad dependiendo del pais seleccionadpo
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarCiudadXpais(Connection connection,
			ParamInstitucionForm forma) {
		logger.info("\n entro a cargarCiudadXPais ");
		forma.setCiudades(Utilidades.obtenerCiudadesXPais(connection, forma
				.getEmpresa(indicesEmpresa[21])
				+ ""));
	}

	public static ActionForward accionEliminarCampo(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request,
			HttpServletResponse response) {
		// logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
		int numRegMapEliminados = Integer.parseInt(forma
				.getListadoEmpresasElimMap("numRegistros")
				+ "");

		// logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa = (Integer.parseInt(forma
				.getListadoEmpresasMap("numRegistros")
				+ "") - 1);

		for (int i = 0; i < indicesEmpresa.length; i++) {
			// solo pasar al mapa los registros que son de BD
			if ((forma.getListadoEmpresasMap(indicesEmpresa[26]
					+ forma.getIndex()) + "").trim().equals(
					ConstantesBD.acronimoSi)) {
				forma.setListadoEmpresasElimMap(indicesEmpresa[i] + ""
						+ numRegMapEliminados, forma
						.getListadoEmpresasMap(indicesEmpresa[i] + ""
								+ forma.getIndex()));
			}
		}

		if ((forma.getListadoEmpresasMap(indicesEmpresa[26] + forma.getIndex()) + "")
				.trim().equals(ConstantesBD.acronimoSi)) {
			forma.setListadoEmpresasElimMap("numRegistros",
					(numRegMapEliminados + 1));
		}

		// acomodar los registros del mapa en su nueva posicion
		for (int i = Integer.parseInt(forma.getIndex().toString()); i < ultimaPosMapa; i++) {
			for (int j = 0; j < indicesEmpresa.length; j++) {
				forma.setListadoEmpresasMap(indicesEmpresa[j] + "" + i,
						forma.getListadoEmpresasMap(indicesEmpresa[j] + ""
								+ (i + 1)));
			}
		}

		// ahora eliminamos el ultimo registro del mapa.
		for (int j = 0; j < indicesEmpresa.length; j++) {
			forma.getListadoEmpresasMap().remove(
					indicesEmpresa[j] + "" + ultimaPosMapa);
		}

		// ahora actualizamos el numero de registros en el mapa.
		forma.setListadoEmpresasMap("numRegistros", ultimaPosMapa);
		return UtilidadSesion
				.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto
						.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),
						Integer.parseInt(forma.getListadoEmpresasMap(
								"numRegistros").toString()), response, request,
						"listadoEmpresas.jsp", Integer.parseInt(forma
								.getIndex().toString()) == ultimaPosMapa);

	}

	/**
	 * Metodo encargado de eliminar las empresas
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward accionEliminarEmpresa(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping) {
		for (int i = 0; i < Integer.parseInt(forma
				.getListadoEmpresasElimMap("numRegistros")
				+ ""); i++) {
			HashMap datos = new HashMap();
			// se carga el codigo de la empresa que se va a eliminar
			datos.put(indicesEmpresa[1], forma
					.getListadoEmpresasElimMap(indicesEmpresa[1] + i));

			boolean transacction = UtilidadBD.iniciarTransaccion(connection);

			transacction = eliminarEmpresas(connection, datos);

			if (transacction)
				transacction = UtilidadFileUpload.borrrarArchivo(forma
						.getListadoEmpresasElimMap(indicesEmpresa[17] + i)
						+ "", ValoresPorDefecto.getFilePath() + "logos"
						+ System.getProperty("file.separator"));

			if (transacction) {
				UtilidadBD.finalizarTransaccion(connection);
				logger.info("----->INSERTO 100% ");
			} else {
				UtilidadBD.abortarTransaccion(connection);
			}
		}
		return mapping.findForward("listadoEmpresas");
	}

	/**
	 * Metodo encargado de guardar los logos en la ruta deseada.
	 * 
	 * @param connection
	 * @param forma
	 * @return True/False
	 */
	public static boolean accionGuardarArchivo(Connection connection,
			ParamInstitucionForm forma) {
		logger
				.info("\n\n************************************nentro a guardarArchivo********************************");
		// logger.info("****************  VALOR FORMA"+forma.getEmpresa());

		if (forma.getArchivo() != null && forma.getArchivo().getFileSize() > 0) {
			// logger.info("****************  NOMBRE ARCHIVO "+forma.getArchivo().getFileName());
			// logger.info("****************  size ARCHIVO "+forma.getArchivo().getFileSize());
			// se organoza el nuevo nombre para el archivo
			// el nombre queda de la siguiente forma:
			// tipoNIT+NIT+Pais
			String[] tmp = { "", "" };
			String nombre = forma.getArchivo().getFileName();
			tmp = nombre.split("\\.");
			String nombreArchivo = forma.getEmpresa(indicesEmpresa[15]) + ""
					+ forma.getEmpresa(indicesEmpresa[2])
					+ forma.getEmpresa(indicesEmpresa[21]);
			// se ingresa el nombre del logo a la empresa a guardarse.
			// forma.setEmpresa(indicesEmpresa[17], nombreArchivo+"."+tmp[1]);
			logger.info("\n el valor de estaBD es "
					+ forma.getEmpresa(indicesEmpresa[26]));
			if ((forma.getEmpresa(indicesEmpresa[26]) + "")
					.equals(ConstantesBD.acronimoSi)) {
				if (UtilidadFileUpload.borrrarArchivo(forma
						.getEmpresa(indicesEmpresa[17])
						+ "", ValoresPorDefecto.getFilePath() + "logos"
						+ System.getProperty("file.separator"))) {
					forma.setEmpresa(indicesEmpresa[17], nombreArchivo + "."
							+ tmp[1]);
					return UtilidadFileUpload.guadarArchivo(forma.getArchivo(),
							nombreArchivo + "." + tmp[1], ValoresPorDefecto
									.getFilePath()
									+ "logos");
				}
			} else if ((forma.getEmpresa(indicesEmpresa[26]) + "")
					.equals(ConstantesBD.acronimoNo)) {
				forma.setEmpresa(indicesEmpresa[17], nombreArchivo + "."
						+ tmp[1]);
				return UtilidadFileUpload.guadarArchivo(forma.getArchivo(),
						nombreArchivo + "." + tmp[1], ValoresPorDefecto
								.getFilePath()
								+ "logos");
			} else
				return false;
		} else if (UtilidadFileUpload.existeArchivo(ValoresPorDefecto
				.getFilePath()
				+ "logos" + System.getProperty("file.separator"), (forma
				.getEmpresa(indicesEmpresa[17]) + "")))
			return true;

		return false;
	}

	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward accionGuardar(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		HashMap criterios = new HashMap();
		String estadoEliminar = "ninguno";
		// institucion
		criterios.put(indicesEmpresa[3], usuario.getCodigoInstitucionInt());

		// modificar
		// logger.info("pa ver como va el hashmap "+forma.getFactorMap());
		if ((forma.getEmpresa(indicesEmpresa[26]) + "")
				.equals(ConstantesBD.acronimoSi)) {
			if (existeModificacion(forma)) {
				logger.info("ENTRO A MODIFICAR::::::::..");
				transacction = modificarBD(connection, forma, usuario);
			}
			if (transacction) {

				forma.setMensaje(new ResultadoBoolean(true, "EMPRESA "
						+ forma.getEmpresa(indicesEmpresa[4])
						+ " INGRESADA/MODIFICADA CORRECTAMENTE!!!"));
				// cargarEmpresa(connection, forma, mapping);
				logger.info("----->INSERTO 100% ");
			} else {
				try {
					UtilidadBD.abortarTransaccion(connection);
					UtilidadTransaccion.getTransaccion().rollback();
				} catch (Exception e) {
					Log4JManager.info("Error" + e);
				}
				forma.setMensaje(new ResultadoBoolean(true,
						"SE PRESENTO INCONVENIENTES EN EL INGRESO/MODIFICACIÓN DE LA EMPRESA "
								+ forma.getEmpresa(indicesEmpresa[4])));
			}

		}

		// insertar
		else if ((forma.getEmpresa(indicesEmpresa[26]) + "")
				.equals(ConstantesBD.acronimoNo)) {
			// logger.info("Entro a insertar ");

			double codigoEmpresainstitucion = insertarBD(connection, forma,
					usuario);
			String codigoEmpresainstitucionS = String
					.valueOf(codigoEmpresainstitucion);
			transacction = codigoEmpresainstitucion > 0;

			if (transacction) {
				UtilidadBD.finalizarTransaccion(connection);
				IFirmaContratoMultiEmpresasServicio serFirmaMultiempresa = ContratoFabricaServicio
						.crearFirmaMultiempresa();
				serFirmaMultiempresa.modificarFirmasMultiEmpresa(forma
						.getListaDtoFirmEmpresa(), codigoEmpresainstitucionS,
						usuario, connection);
			}

			estadoEliminar = "operacionTrue";
		}

		// logger.info("\n\n valor i >> "+i);

		/*
		 * GUARADAR CONTRATO
		 */

		listarEmpresas(connection, forma, mapping, usuario);

		return mapping.findForward("listadoEmpresas");
	}

	/**
	 * Metodo que examina si existe modificacion en los datos o no.
	 * 
	 * @param forma
	 * @return
	 */
	public static boolean existeModificacion(ParamInstitucionForm forma) {
		for (int i = 0; i < indicesEmpresa.length; i++) {
			if (!(forma.getEmpresa(indicesEmpresa[i]) + "").equals(forma
					.getEmpresaClone().get(indicesEmpresa[i])
					+ ""))
				return true;
		}

		if (forma.getArchivo() != null && forma.getArchivo().getFileSize() > 0)
			return true;

		return false;
	}

	/**
	 * Metodo encargado de organizar los datos para ser modificados.
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static boolean modificarBD(Connection connection,
			ParamInstitucionForm forma, UsuarioBasico usuario) {
		logger.info("\n ********** entroa modificarBD *************** ");
		HashMap datos = new HashMap();
		// se guarda el archivo
		if (accionGuardarArchivo(connection, forma)) {
			/**********************************************
			 * DATOS OBLIGATORIOS
			 ***********************************************/
			// razon social
			datos.put(indicesEmpresa[4], forma.getEmpresa(indicesEmpresa[4]));
			// nit
			datos.put(indicesEmpresa[2], forma.getEmpresa(indicesEmpresa[2]));
			// tipoNit
			datos.put(indicesEmpresa[15], forma.getEmpresa(indicesEmpresa[15]));
			// digito verificacion
			datos.put(indicesEmpresa[32], forma.getEmpresa(indicesEmpresa[32]));
			// pais
			datos.put(indicesEmpresa[21], forma.getEmpresa(indicesEmpresa[21]));

			String[] tmp = { "-1", "-1" };
			try {
				tmp = (forma.getEmpresa(indicesEmpresa[29]) + "")
						.split(ConstantesBD.separadorSplit);
			} catch (Exception e) {
				logger
						.error("\n problema separando el codigo del departamento del de la ciudad");

			}

			// Departamento
			datos.put(indicesEmpresa[5], tmp[1]);
			// Ciudad
			datos.put(indicesEmpresa[6], tmp[0]);
			// direccion
			datos.put(indicesEmpresa[7], forma.getEmpresa(indicesEmpresa[7]));
			// telefono
			datos.put(indicesEmpresa[8], forma.getEmpresa(indicesEmpresa[8]));
			// logo
			datos.put(indicesEmpresa[17], forma.getEmpresa(indicesEmpresa[17]));
			// vigencia
			datos.put(indicesEmpresa[18], forma.getEmpresa(indicesEmpresa[18]));

			// institucion
			datos.put(indicesEmpresa[3], usuario.getCodigoInstitucionInt());
			// usuariomodifica
			datos.put(indicesEmpresa[27], usuario.getLoginUsuario());

			/**********************************************
			 * FIN DATOS OBLIGATORIOS
			 ***********************************************/

			// codigo minsalud
			datos.put(indicesEmpresa[9], forma.getEmpresa(indicesEmpresa[9]));
			// actividad economica
			datos.put(indicesEmpresa[10], forma.getEmpresa(indicesEmpresa[10]));
			// resolucion
			datos.put(indicesEmpresa[11], forma.getEmpresa(indicesEmpresa[11]));
			// prefijo facturas
			datos.put(indicesEmpresa[12], forma.getEmpresa(indicesEmpresa[12]));
			// ramgo inicial facturas
			datos.put(indicesEmpresa[13], forma.getEmpresa(indicesEmpresa[13]));
			// rango final facturas
			datos.put(indicesEmpresa[14], forma.getEmpresa(indicesEmpresa[14]));
			// encabezado factura
			datos.put(indicesEmpresa[19], forma.getEmpresa(indicesEmpresa[19]));
			// pie de pagina factura
			datos.put(indicesEmpresa[20], forma.getEmpresa(indicesEmpresa[20]));
			// codigo empresa_institucion
			datos.put(indicesEmpresa[1], forma.getEmpresa(indicesEmpresa[1]));
			// Tipo Institucion
			datos.put(indicesEmpresa[33], forma.getEmpresa(indicesEmpresa[33]));
			// cod_emp_trans_esp
			datos.put(indicesEmpresa[34], forma.getEmpresa(indicesEmpresa[34]));
			// ubicacion_logo_reportes
			datos.put(indicesEmpresa[35], forma.getEmpresa(indicesEmpresa[35]));
			// pie_his_cli
			datos.put(indicesEmpresa[36], forma.getEmpresa(indicesEmpresa[36]));
			// codigo Interfaz
			datos.put(indicesEmpresa[37], forma.getEmpresa(indicesEmpresa[37]));

			datos.put(indicesEmpresa[38], forma.getEmpresa(indicesEmpresa[38]));

			datos.put(indicesEmpresa[39], forma.getEmpresa(indicesEmpresa[39]));
			
			datos.put(indicesEmpresa[40], forma.getEmpresa(indicesEmpresa[40]));
			datos.put(indicesEmpresa[41], forma.getEmpresa(indicesEmpresa[41]));
			datos.put(indicesEmpresa[42], forma.getEmpresa(indicesEmpresa[42]));
			datos.put(indicesEmpresa[43], forma.getEmpresa(indicesEmpresa[43]));
			datos.put(indicesEmpresa[44], forma.getEmpresa(indicesEmpresa[44]));
			datos.put(indicesEmpresa[45], forma.getEmpresa(indicesEmpresa[45]));
			datos.put(indicesEmpresa[46], forma.getEmpresa(indicesEmpresa[46]));//FIXME
			

			// GUARDAR FIRMA MULTIEMPRESA

			boolean modifica = modificarEmpresas(connection, datos);

			if (modifica) {
				UtilidadBD.finalizarTransaccion(connection);

				IFirmaContratoMultiEmpresasServicio serFirmaMultiempresa = ContratoFabricaServicio
						.crearFirmaMultiempresa();
				serFirmaMultiempresa.modificarFirmasMultiEmpresa(forma
						.getListaDtoFirmEmpresa(), forma
						.getEmpresa(indicesEmpresa[1])
						+ "", usuario, connection);
			}

			return modifica;

		}

		return false;
	}

	/**
	 * Metodo encargado de organizar los datos para insertarlos en la BD
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static double insertarBD(Connection connection,
			ParamInstitucionForm forma, UsuarioBasico usuario) {
		logger.info("\n ********** entroa insertarBD *************** ");

		HashMap datos = new HashMap();
		// se guarda el archivo
		if (accionGuardarArchivo(connection, forma)) {
			/**********************************************
			 * DATOS OBLIGATORIOS
			 ***********************************************/
			// razon social
			datos.put(indicesEmpresa[4], forma.getEmpresa(indicesEmpresa[4]));
			// nit
			datos.put(indicesEmpresa[2], forma.getEmpresa(indicesEmpresa[2]));
			// tipoNit
			datos.put(indicesEmpresa[15], forma.getEmpresa(indicesEmpresa[15]));
			// digito Verificacion
			datos.put(indicesEmpresa[32], forma.getEmpresa(indicesEmpresa[32]));
			// pais
			datos.put(indicesEmpresa[21], forma.getEmpresa(indicesEmpresa[21]));

			String[] tmp = { "-1", "-1" };
			try {
				tmp = (forma.getEmpresa(indicesEmpresa[29]) + "")
						.split(ConstantesBD.separadorSplit);
			} catch (Exception e) {
				logger
						.error("\n problema separando el codigo del departamento del de la ciudad");

			}

			// Departamento
			datos.put(indicesEmpresa[5], tmp[1]);
			// Ciudad
			datos.put(indicesEmpresa[6], tmp[0]);
			// direccion
			datos.put(indicesEmpresa[7], forma.getEmpresa(indicesEmpresa[7]));
			// telefono
			datos.put(indicesEmpresa[8], forma.getEmpresa(indicesEmpresa[8]));
			// logo
			datos.put(indicesEmpresa[17], forma.getEmpresa(indicesEmpresa[17]));
			// vigencia
			datos.put(indicesEmpresa[18], forma.getEmpresa(indicesEmpresa[18]));

			// institucion
			datos.put(indicesEmpresa[3], usuario.getCodigoInstitucionInt());
			// usuariomodifica
			datos.put(indicesEmpresa[27], usuario.getLoginUsuario());

			/**********************************************
			 * FIN DATOS OBLIGATORIOS
			 ***********************************************/

			// codigo minsalud
			datos.put(indicesEmpresa[9], forma.getEmpresa(indicesEmpresa[9]));
			// actividad economica
			datos.put(indicesEmpresa[10], forma.getEmpresa(indicesEmpresa[10]));
			// resolucion
			datos.put(indicesEmpresa[11], forma.getEmpresa(indicesEmpresa[11]));
			// prefijo facturas
			datos.put(indicesEmpresa[12], forma.getEmpresa(indicesEmpresa[12]));
			// ramgo inicial facturas
			datos.put(indicesEmpresa[13], forma.getEmpresa(indicesEmpresa[13]));
			// rango final facturas
			datos.put(indicesEmpresa[14], forma.getEmpresa(indicesEmpresa[14]));
			// encabezado factura
			datos.put(indicesEmpresa[19], forma.getEmpresa(indicesEmpresa[19]));
			// pie de pagina factura
			datos.put(indicesEmpresa[20], forma.getEmpresa(indicesEmpresa[20]));

			// Tipo Institucion
			datos.put(indicesEmpresa[33], forma.getEmpresa(indicesEmpresa[33]));

			// Tipo Institucion
			datos.put(indicesEmpresa[35], forma.getEmpresa(indicesEmpresa[35]));

			// codigo Interfaz
			datos.put(indicesEmpresa[37], forma.getEmpresa(indicesEmpresa[37]));
			
			datos.put(indicesEmpresa[40], forma.getEmpresa(indicesEmpresa[40]));
			datos.put(indicesEmpresa[41], forma.getEmpresa(indicesEmpresa[41]));
			datos.put(indicesEmpresa[42], forma.getEmpresa(indicesEmpresa[42]));
			datos.put(indicesEmpresa[43], forma.getEmpresa(indicesEmpresa[43]));
			datos.put(indicesEmpresa[44], forma.getEmpresa(indicesEmpresa[44]));
			datos.put(indicesEmpresa[45], forma.getEmpresa(indicesEmpresa[45]));
			datos.put(indicesEmpresa[46], forma.getEmpresa(indicesEmpresa[46]));//FIXME
			

			return insertarEmpresas(connection, datos);
		} else
			return ConstantesBD.codigoNuncaValidoDouble;

	}

	/**
	 * crea una nueva empresa
	 * 
	 * @param forma
	 */
	public static ActionForward accionNuevo(Connection connection,
			ParamInstitucionForm forma, ActionMapping mapping) {
		UtilidadBD.closeConnection(connection);

		forma.resetEmpresas();
		// inicializamos todos los indices en ""
		for (int i = 0; i < indicesEmpresa.length; i++) {
			forma.setEmpresa(indicesEmpresa[i], "");
		}
		// ahora cambiamos los que son mas espesificos
		// tipoNit
		forma.setEmpresa(indicesEmpresa[15], "-1");
		// pais
		forma.setEmpresa(indicesEmpresa[21], "-1");
		// ciudadDepartamento
		forma.setEmpresa(indicesEmpresa[29], "-1");
		// vigencia
		forma.setEmpresa(indicesEmpresa[18], "N");
		// estaBD
		forma.setEmpresa(indicesEmpresa[26], "N");

		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean esInstitucionPublica(Connection con, int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao()
				.esInstitucionPublica(con, institucion);
	}

	/**
	 * Metodo que consulta el rango inicial y final de los consecutivos de
	 * facturacion
	 * 
	 * @param centroAtencion
	 * @return
	 */
	public static RangosConsecutivos obtenerRangosFacturacionXInstitucion(
			int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().obtenerRangosFacturacionXInstitucion(
						institucion);
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
	public InfoDatosString getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad
	 *            Asigna ciudad.
	 */
	public void setCiudad(InfoDatosString ciudad) {
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
	public InfoDatosString getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            Asigna departamento.
	 */
	public void setDepartamento(InfoDatosString departamento) {
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
	 * @return Retorna telefono.
	 */
	public String getTelefono() {
		return Telefono;
	}

	/**
	 * @param telefono
	 *            Asigna telefono.
	 */
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}

	/**
	 * @return Returns the nit.
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            The nit to set.
	 */
	public void setNit(String nit) {
		this.nit = nit;
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
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the formatoImpresion
	 */
	public String getFormatoImpresion() {
		return formatoImpresion;
	}

	/**
	 * @param formatoImpresion
	 *            the formatoImpresion to set
	 */
	public void setFormatoImpresion(String formatoImpresion) {
		this.formatoImpresion = formatoImpresion;
	}

	/**
	 * @param descripcionTipoIdentificacion
	 *            the descripcionTipoIdentificacion to set
	 */
	public void setDescripcionTipoIdentificacion(
			String descripcionTipoIdentificacion) {
		this.descripcionTipoIdentificacion = descripcionTipoIdentificacion;
	}

	public InfoDatosString getPais() {
		return pais;
	}

	public void setPais(InfoDatosString pais) {
		this.pais = pais;
	}



	/**
	 * @param logoJsp the logoJsp to set
	 */
	public void setLogoJsp(String logoJsp) {
		this.logoJsp = logoJsp;
	}

	/**
	 * @param logoReportes the logoReportes to set
	 */
	public void setLogoReportes(String logoReportes) {
		this.logoReportes = logoReportes;
	}

	/**
	 * @return the logoReportes
	 */
	public String getLogoReportes() {
		return logoReportes;
	}

	/**
	 * @return the logoJsp
	 */
	public String getLogoJsp() {
		return logoJsp;
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

	/**
	 * @return the digitoVerificacion
	 */
	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	/**
	 * @param digitoVerificacion
	 *            the digitoVerificacion to set
	 */
	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	/**
	 * @return the pieHisCli
	 */
	public String getPieHisCli() {
		return pieHisCli;
	}

	/**
	 * @param pieHisCli
	 *            the pieHisCli to set
	 */
	public void setPieHisCli(String pieHisCli) {
		this.pieHisCli = pieHisCli;
	}

	/**
	 * @return the indicativo
	 */
	public String getIndicativo() {
		return indicativo;
	}

	/**
	 * @param indicativo
	 *            the indicativo to set
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa(
			DtoEmpresasInstitucion dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao().listaInstitucionEmpresa(dto);
	}

	/**
	 * @param resolucionFacturaVaria
	 *            the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}

	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}

	/**
	 * @param prefijoFacturaVaria
	 *            the prefijoFacturaVaria to set
	 */
	public void setPrefijoFacturaVaria(String prefijoFacturaVaria) {
		this.prefijoFacturaVaria = prefijoFacturaVaria;
	}

	/**
	 * @return the prefijoFacturaVaria
	 */
	public String getPrefijoFacturaVaria() {
		return prefijoFacturaVaria;
	}

	/**
	 * @param rangoInicialFacturaVaria
	 *            the rangoInicialFacturaVaria to set
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
	 * @param rangoFinalFacturaVaria
	 *            the rangoFinalFacturaVaria to set
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

	/**
	 * @param encabezadoFacturaVaria
	 *            the encabezadoFacturaVaria to set
	 */
	public void setEncabezadoFacturaVaria(String encabezadoFacturaVaria) {
		this.encabezadoFacturaVaria = encabezadoFacturaVaria;
	}

	/**
	 * @return the encabezadoFacturaVaria
	 */
	public String getEncabezadoFacturaVaria() {
		return encabezadoFacturaVaria;
	}

	/**
	 * @param pieFacturaVaria
	 *            the pieFacturaVaria to set
	 */
	public void setPieFacturaVaria(String pieFacturaVaria) {
		this.pieFacturaVaria = pieFacturaVaria;
	}

	/**
	 * @return the pieFacturaVaria
	 */
	public String getPieFacturaVaria() {
		return pieFacturaVaria;
	}

	public String getPieAmbMed() {
		return pieAmbMed;
	}

	public void setPieAmbMed(String pieAmbMed) {
		this.pieAmbMed = pieAmbMed;
	}
	
	
	
	/**
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static boolean obtenerNivelLogo(Connection con, int institucion) throws SQLException {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getParamInstitucionDao()
				.obtenerNivelLogo(con, institucion);
	}

}
