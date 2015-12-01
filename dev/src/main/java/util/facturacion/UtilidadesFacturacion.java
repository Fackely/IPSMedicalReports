/*
 * Abr 27, 2007
 */
package util.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.UtilidadesFacturacionDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoIgualConsecutivoFactura;
import com.princetonsa.dto.facturacion.DtoServiPpalIncluido;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author sgomez
 * Clase que contiene las utilidades del módulo de facturacion
 */
public class UtilidadesFacturacion 
{
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesFacturacionDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesFacturacionDao();
	}
	
	/**
	 * Método implementado para cargar los convenios por una clasificacion específica
	 * @param con
	 * @param codigoClasificacion
	 * @return
	 */
	public static HashMap cargarConveniosXClasificacion(Connection con, int codigoClasificacion) 
	{
		HashMap campos = new HashMap();
		campos.put("clasificacion", codigoClasificacion+"");
		return utilidadesDao().cargarConveniosXClasificacion(con, campos);
	}
	
	/**
	 * Metodo que verifica que todos los cargos de las solicitudes asociadas estan en estado excento o anulado 
	 * retorna true si puede cerrar el ingreso false si no.
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean puedoCerrarIngreso(Connection con, int ingreso)
	{
		return utilidadesDao().puedoCerrarIngreso(con, ingreso);
	}
	
	/**
	 * Método que consulta los tarifarios oficiales
	 * @param con
	 * @return
	 */
	public static HashMap cargarTarifariosOficiales(Connection con)
	{
		return utilidadesDao().cargarTarifariosOficiales(con);
	}
	
	/**
	 * Método que consulta los tarifarios oficiales
	 * @param con
	 * @return
	 */
	public static String obtenerNombreTarifarioOficial(String codigo)
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa= utilidadesDao().cargarTarifariosOficiales(con);
		UtilidadBD.closeConnection(con);
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros")+""); w++)
		{
			if(mapa.get("codigo_"+w).toString().equals(codigo))
			{
				return mapa.get("nombre_"+w).toString();
			}
		}
		return "";
	}
	
	/**
	 * Método implementado para obtener el valor del salario mínimo vigente
	 * @param con
	 * @return
	 */
	public static double obtenerValorSalarioMinimoVigente(Connection con)
	{
		return utilidadesDao().obtenerValorSalarioMinimoVigente(con);
	}
	
	/**
	 * Método que carga los estratos sociales
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param tipoRegimen
	 * @param codigoConvenio: el codigo del convenio sirve para filtrar los estratos sociales del  monto de cobro
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap cargarEstratosSociales(Connection con,int institucion,String activo,String tipoRegimen,int codigoConvenio,int codigoViaIngreso, String fechaReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion+"");
		campos.put("activo",activo);
		campos.put("tipoRegimen", tipoRegimen);
		campos.put("codigoConvenio", codigoConvenio);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("fechaReferencia", fechaReferencia);
		return utilidadesDao().cargarEstratosSociales(con, campos);
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoViaIngreso
	 * @param fechaFiltro
	 * @return HashMap
	 * indices  ---> codigo_,institucion_,convenio_,contrato_,paquete_,descpaquete_,codserviciopaquete_,nomserviciopaquete_,viaingreso_,fechainicialvenc_,fechafinalvenc_
	 * 
	 */
	public static HashMap obtenerPaquetesValidosResponsable(Connection con, int codigoConvenio, String codigoViaIngreso, String fechaFiltro) 
	{
		HashMap vo = new HashMap();
		vo.put("convenio",codigoConvenio+"");
		vo.put("viaingreso",codigoViaIngreso);
		vo.put("fechafiltro",UtilidadFecha.conversionFormatoFechaABD(fechaFiltro));
		return utilidadesDao().obtenerPaquetesValidosResponsable(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public static String obtenerTarfiaServicio(Connection con, int esquemaTarifario, int servicio, String fechaCalculoVigencia) 
	{
		return utilidadesDao().obtenerTarfiaServicio(con, esquemaTarifario,servicio, fechaCalculoVigencia);
	}
	
	/**
	 * Método que consulta los montos de cobro por convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobroXConvenio(Connection con,String codigoConvenio, String fechaReferencia)
	{
		return utilidadesDao().obtenerMontosCobroXConvenio(con, codigoConvenio, fechaReferencia);
	}
	
	/**
	 * Metodo que consulta si un servicio esta definido como principal en las componentes de un paquete.
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @param servicio
	 * @return
	 */
	public static boolean esServicioPrincComponentePaquete(Connection con,String codigoPaquete,int institucion,String servicio)
	{
		return utilidadesDao().esServicioPrincComponentePaquete(con, codigoPaquete,institucion,servicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static ArrayList obtenerServiciosPrincipalesPaquete(Connection con,String codigoPaquete,int institucion)
	{
		return utilidadesDao().obtenerServiciosPrincipalesPaquete(con, codigoPaquete,institucion);
	}
	
	/**
	 * Método implementado para eliminar los registros en solicitudes_subcuenta por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean eliminarRegistrosSolicitudesSubCuenta(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().eliminarRegistrosSolicitudesSubCuenta(con, numeroSolicitud);
	}

	/**
	 * Metodo que retorna el valorUnitarioCargado de un servicio/articulo para un responsable determinado.
	 * @param con
	 * @param subCuenta
	 * @param numerSolicitud
	 * @param codServArti
	 * @param servicioCX 
	 * @param esServicio
	 * @param cargoFacturado 
	 * @return
	 */
	public static double obtenerValorUnitarioCargadoConvenioBase(Connection con, String numerSolicitud, String codServArti, String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws IPSException
	{
		return utilidadesDao().obtenerValorUnitarioCargadoConvenioBase(con, numerSolicitud,codServArti,servicioCX,detCxHonorarios,detAsCxSalMat,esServicio,cargoFacturado);
	}

	/**
	 * 
	 * @param con
	 * @param numerSolicitud
	 * @param codServArti
	 * @param serviciocx 
	 * @param esServicio
	 * @param cargoFacturado 
	 * @return
	 */
	public static int obtenerEsquemaTarifarioBase(Connection con, String numerSolicitud, String codServArti, String serviciocx,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws IPSException 
	{
		return utilidadesDao().obtenerEsquemaTarifarioBase(con, numerSolicitud,codServArti,serviciocx,detCxHonorarios,detAsCxSalMat,esServicio,cargoFacturado);
	}
	
	public static boolean responsableTieneSolPaquetes(Connection con,String subCuenta)
	{
		return utilidadesDao().responsableTieneSolPaquetes(con,subCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean responsableTieneSolAsociadas(Connection con,String subCuenta)
	{
		return utilidadesDao().responsableTieneSolAsociadas(con,subCuenta);
	}
	
	/**
	 * Verifica si el convenio requiere justificacion de servicio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean requiereJustificacioServ(Connection con, int convenio)
	{
		return utilidadesDao().requiereJustificacioServ(con,convenio);
	}
	
	/**
	 * Verifica si el convenio requiere justificacion de articulo
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean requiereJustificacioArt(Connection con, int convenio, int art)
	{
		return utilidadesDao().requiereJustificacioArt(con,convenio, art);
	}
	
	
	
	/**
	 * Consulta el nombre del tipo de regimen segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static String consultarNombreTipoRegimen(Connection con, int convenio)
	{
		return utilidadesDao().consultarNombreTipoRegimen(con,convenio);
	}
	
	/**
	 * Consulta el nombre del convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static String consultarNombreConvenio(Connection con, int convenio)
	{
		return utilidadesDao().consultarNombreConvenio(con,convenio);
	}
	
	/**
	 * Consulta el codigo del convenio 
	 * @param con
	 * @param codigo factura
	 * @return
	 */
	public static String consultarCodigoConvenio(Connection con, String codigoFactura){
		
		return utilidadesDao().consultarCodigoConvenio(con, codigoFactura);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerResposablePaquetizadoDadaSolicitud(Connection con, String numeroSolicitud) 
	{
		return utilidadesDao().obtenerResposablePaquetizadoDadaSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio ha sido distribuido
	 * @param con
	 * @param campos
	 * Nota * El consecutivo Asocio se refiere a la secuencia de las tablas det_cx_honorarioa o det_asocio_cx_salas_mat dependiendo del tipo de servicio
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioDistribuido(Connection con,String consecutivoAsocio,String tipoServicio)
	{ 
		HashMap campos = new HashMap();
		campos.put("consecutivoAsocio", consecutivoAsocio);
		campos.put("tipoServicio",tipoServicio);
		return utilidadesDao().esSolicitudServicioAsocioDistribuido(con, campos);
	}
	
	/**
	 * Verifica su una solicitud/servicio/asocio ha sido facturado
	 * @param con
	 * @param campos
	 * Nota * El consecutivo Asocio se refiere a la secuencia de las tablas det_cx_honorarioa o det_asocio_cx_salas_mat dependiendo del tipo de servicio
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioFacturado(Connection con,String consecutivoAsocio,String tipoServicio)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoAsocio", consecutivoAsocio);
		campos.put("tipoServicio",tipoServicio);
		return utilidadesDao().esSolicitudServicioAsocioFacturado(con, campos);
	}
	
	/**
	 * Método que verifica si una solicitud es paquetizada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudPaquetizada(Connection con,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		return utilidadesDao().esSolicitudPaquetizada(con, campos);
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio está cargado
	 * Nota * El consecutivo Asocio se refiere a la secuencia de las tablas det_cx_honorarioa o det_asocio_cx_salas_mat dependiendo del tipo de servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudServicioAsocioCargado(Connection con,String consecutivoAsocio,String tipoServicio)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoAsocio", consecutivoAsocio);
		campos.put("tipoServicio",tipoServicio);
		return utilidadesDao().esSolicitudServicioAsocioCargado(con, campos);
	}
	
	/**
	 * Método que consulta los convenios asociados a una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> cargarConveniosSolicitud(Connection con,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		return utilidadesDao().cargarConveniosSolicitud(con, campos);
	}
	
	public static boolean esUsuarioconPermisoResponderConsultasEntSubcont(String entidadSubcontratada, String usuario) 
	{
		return utilidadesDao().esUsuarioconPermisoResponderConsultasEntSubcont(entidadSubcontratada, usuario);
	}
	
	/**
	 * Metodo que puede devolver todos los pooles o
	 * los puede filtar mediante el HashMap parametros.
	 * ----------------------------------------
	 * 		KEY'S DEL HASHMAP PARAMETROS
	 * ----------------------------------------
	 * --codigopool --> Opcional
	 * --activopool --> Opcional
	 * --terceroresponsablepool --> Opcional
	 * 
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * codigopool, descripcionpool, terceroresponsablepool,
	 * activopool
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPooles(Connection connection,HashMap parametros)
	{
		return utilidadesDao().obtenerPooles(connection, parametros);
	}
	
	/**
	 * Metodo que puede devuelver todos los conceptos
	 * de pago de pooles o puede ser filtrado
	 * por el codigo de concepto y/o por el 
	 * tipo del concepto.
	 * El HahMap parametros tiene los siguientes
	 * Key's:
	 * -----------------------------------
	 * 		  KEY'S DE PARAMETROS
	 * -----------------------------------
	 * --institucion --> Requerido
	 * --codigoconcepto --> Opcional
	 * --tipoconcepto --> Opcional
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * codigoconcepto, descripcionconcepto, tipoconcepto
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosPagoPooles(Connection connection,HashMap parametros)
	{
		return utilidadesDao().obtenerConceptosPagoPooles(connection, parametros);
	}
	
	/**
	 * Metodo que puede devuelver todos los
	 * tipos de servicios o puede ser filtrado
	 * por el acronimo y/o por el esqx y/o por
	 * el solicitar.
	 * El HahMap parametros tiene los siguientes
	 * Key's:
	 * -----------------------------------
	 * 		  KEY'S DE PARAMETROS
	 * -----------------------------------
	 * --acronimotiposervicio --> Opcional
	 * --esqx --> Opcional
	 * --solicitar --> Opcional
	 * Este metodo devuelve un arrayList de HashMap
	 * con los siguientes Key's.
	 * -------------------------------------------
	 * 	 KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * -------------------------------------------
	 * acronimotiposervicio, nombretiposervicio,
	 * descripciontiposervicio, esqx, solicitar.
	 * @param connection
	 * @param codigo
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,HashMap parametros)
	{
		return utilidadesDao().obtenerTiposServicio(connection, parametros);
	}
	
	/**
	 * Metodo encargado de consultar todos los tipos 
	 * de servicios, pudiendolos filtrar por sus diferentes
	 * acronimos y por el indicativo esqx:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimos --> ejemplo, este es el valor de esta variable  'R','Q','D',
	 * --esqx --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,String acronimos, String esqx)
	{
		return utilidadesDao().obtenerTiposServicio(connection, acronimos, esqx);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetCargo
	 * @return
	 */
	public static String obtenerconsecutivoFacturaDetCargo(Connection con,String codigoDetCargo)
	{
		return utilidadesDao().obtenerconsecutivoFacturaDetCargo(con, codigoDetCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estadoFactura
	 * @return
	 */
	public static boolean subCuentaConFacturas(Connection con,String subCuenta,int estadoFactura)
	{
		return utilidadesDao().subCuentaConFacturas(con, subCuenta,estadoFactura);
	}
	
	/**
	 * Método que verifica si un convenio es de colsanitas
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean esConvenioColsanitas(Connection con,int codigoConvenio, int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio", codigoConvenio+"");
		campos.put("codigoInstitucion", codigoInstitucion+"");
		return utilidadesDao().esConvenioColsanitas(con, campos);
	}
	
	/**
	 * Metodo encargado de consultar todos los tipos 
	 * de liquidacion, pudiendolos filtrar por sus diferentes
	 * acronimos:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimos --> ejemplo, este es el valor de esta variable  'U','V','P',
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposLiquidacion(Connection connection,String acronimos)
	{
		return utilidadesDao().obtenerTiposLiquidacion(connection, acronimos);
	}
	
	
	
	/**
	 * Metodo que verifica si un indicador de calidad si se encuentra parametrizado
	 * @param connection
	 * @param acronimoIndicador
	 * @return
	 */
	public static boolean indicadorParametrizado(Connection connection,String indicador)
	{
		return utilidadesDao().indicadorParametrizado(connection, indicador);
	}	
	
		
	/**
	 * Método para consultar el código del grupo del servicio de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int consultarGrupoServicio(Connection con,int codigoServicio)
	{
		return utilidadesDao().consultarGrupoServicio(con, codigoServicio);
	}
	
	/**
	 * Método implementado para consultar los centros de costo de un grupo de servicio por el centro de atencion
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarCentrosCostoGrupoServicio(Connection con,int codigoGrupoServicio,int codigoCentroAtencion, boolean todos, boolean interno)
	{
		HashMap campos = new HashMap();
		campos.put("codigoGrupoServicio", codigoGrupoServicio+"");
		campos.put("codigoCentroAtencion", codigoCentroAtencion+"");
		campos.put("todos", todos);
		campos.put("interno", interno);
		return utilidadesDao().consultarCentrosCostoGrupoServicio(con, campos);
	}
	
	/**
	 * Método para obtener los pooles vigentes de un profesional
	 * @param con
	 * @param codigoProfesional
	 * @param fechaReferencia
	 * @param todosPooles si se envia el valor true, retorna todos los pooles, si es false, se envian solo los activos.
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPoolesProfesional(Connection con,int codigoProfesional,String fechaReferencia,boolean todosPooles)
	{
		HashMap campos = new HashMap();
		campos.put("codigoProfesional", codigoProfesional);
		campos.put("fechaReferencia", fechaReferencia);
		campos.put("todosPooles", todosPooles);
		return utilidadesDao().obtenerPoolesProfesional(con, campos);
	}
	
	/**
	 * Método implementado para obtener la descripcion de un tipo de servicio
	 * @param con
	 * @param tipoServicio
	 * @return
	 */
	public static String obtenerNombreTipoServicio(Connection con,String tipoServicio)
	{
		return utilidadesDao().obtenerNombreTipoServicio(con, tipoServicio);
	}
	
	
	public static ArrayList<HashMap<String, Object>> tipoTercero(Connection con)
	{
		
		return utilidadesDao().tipoTercero(con);
	}
	
	/**
	 * Método que verifica que un diagnóstico es requerido
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static boolean esRequeridoDiagnosticoServicio(Connection con,int codigoServicio)
	{
		return utilidadesDao().esRequeridoDiagnosticoServicio(con, codigoServicio);
	}
	
	/**
	 * Metodo encargado de consultar los conceptos
	 * de facturas varias.
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * KEY'S DEL MAPA PARAMETROS
	 * -----------------------------------------
	 * --institucion --> Requerido
	 * --tipoCartera --> Opcional ejem. 1,-1,2
	 * --naturaleza --> Opcional ejem.  5,6
	 * -----------------------------------------
	 * @param connection
	 * @param parametros
	 * Key's del mapa resultado
	 * --------------------------------------------
	 * codigo,naturaleza,descripcion,tipoCartera
	 * 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConseptosFacturasVarias(Connection connection,HashMap parametros)
	{
		return utilidadesDao().obtenerConseptosFacturasVarias(connection, parametros);
	}

	public static int ccPlanEspecialConvenio(int codigo) 
	{
		Connection con;
		con=UtilidadBD.abrirConexion();
		int ccplane = utilidadesDao().ccPlanEspecialConvenio(con , codigo);
		UtilidadBD.closeConnection(con);
		return ccplane;
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Deudor
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String esConvenioExcentoDeudor(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		return utilidadesDao().esConvenioExcentoDeudor(con, codigoConvenio, codigoInstitucionInt);
	}

	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Documento de Garantia
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String esConvenioExcentoDocGarantia(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		return utilidadesDao().esConvenioExcentoDocGarantia(con, codigoConvenio, codigoInstitucionInt);
	}

	public static void insertarLogAdconsta(Connection con, String loginUsuario, String resultado) 
	{
		utilidadesDao().insertarLogAdconsta(con, loginUsuario, resultado);
	}

	/**
	 * Utilidad para consultar si el convenio tiene Activado el Sin Contrato
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String esSinContrato(Connection con, int codigoContrato) 
	{
		return utilidadesDao().esSinContrato(con, codigoContrato);
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Controla Anticipos
	 * @param con
	 * @param contrato
	 * @return
	 */
	public static String esControlAnticipos(Connection con, int contrato) 
	{
		return utilidadesDao().esControlAnticipos(con, contrato);
	}
	
	/**
	 * Método que verifica si una solicitud está totalmente pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudTotalPendiente(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().esSolicitudTotalPendiente(con, numeroSolicitud);
	}
	
	/**
	 * Método que retorna si un servicio es nopos
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public static boolean esServicioPos(Connection con, int servicio)
	{
		return utilidadesDao().esServicioPos(con, servicio);
	}
	
	/**
	 * Método que retorna si hay ventas por facturar por centro de costo
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public static boolean hayVentasXFacturar(Connection con, String anoCorte, String mesCorte, String centroAtencion, String centroCosto)
	{
		return utilidadesDao().hayVentasXFacturar(con, anoCorte, mesCorte, centroAtencion, centroCosto);
	}
	
	/**
	 * Método implementado para obtener los tipos de recargo
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposRecargo(Connection con)
	{
		return utilidadesDao().obtenerTiposRecargo(con);
	}
	
	/**
	 * Método para obtener la especialidad del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static InfoDatosInt obtenerEspecialidadServicio(Connection con,int codigoServicio)
	{
		return utilidadesDao().obtenerEspecialidadServicio(con, codigoServicio);
	}

	/**
	 * Método para obtener la especialidad del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static InfoDatosInt obtenerEspecialidadServicio(int codigoServicio)
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoDatosInt info= utilidadesDao().obtenerEspecialidadServicio(con, codigoServicio);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	
	public static int obtenerContratoSubCuenta(Connection con, String idSubCuenta) 
	{
		return utilidadesDao().obtenerContratoSubCuenta(con, idSubCuenta);
	}
	
	/**
	 * Metodo que retoran la fecha de factura de una subCuenta, en caso de que las sub_cuenta no tenga facturas retorna "", en caso de tener varias facturas retorna la fecha de la ultima factura.
	 * @param idSubCuenta
	 * @return
	 */
	public static String obtenerFechaFacturaResponsable(Connection con,String idSubCuenta) 
	{
		return utilidadesDao().obtenerFechaFacturaResponsable(con, idSubCuenta);
	}
	
	/**
	 * Método para obtener las facturas asociadas a la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerConsecutivoFacturaXSolicitud(Connection con,String idSubCuenta,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("idSubCuenta",idSubCuenta);
		campos.put("numeroSolicitud",numeroSolicitud);
		return utilidadesDao().obtenerConsecutivoFacturaXSolicitud(con, campos);
	}
	
	/**
	 * Método implementado para obtener las empresas parametrizadas en el sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con){
		return utilidadesDao().obtenerEmpresas(con);
	}
	
	/**
	 * Método implementado para obtener la descripción de un requisito de paciente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerDescripcionRequisitoPaciente(Connection con,int codigo)
	{
		return utilidadesDao().obtenerDescripcionRequisitoPaciente(con, codigo);
	}
	
	/**
	 * Método para obtener el nit de la empresa del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String obtenerNitEmpresaConvenio(Connection con,int codigoConvenio)
	{
		return utilidadesDao().obtenerNitEmpresaConvenio(con, codigoConvenio);
	}

	/**
	 * Método que retorna si el rango de fechas ya existe en la
	 * parametrización de salarios mínimos
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static boolean existeFechasEnSalarioMinimo(String fechaInicial, String fechaFinal, Integer idRegistroParaNoTenerEnCuenta)
	{
		boolean resultado=false;
		Connection con = null;
	    //Abrimos la conexion con la fuente de Datos 
		con = util.UtilidadBD.abrirConexion();
		if(con == null)
			return resultado;
		else
			 resultado=utilidadesDao().existeFechasEnSalarioMinimo(con, fechaInicial, fechaFinal, idRegistroParaNoTenerEnCuenta);
		util.UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Método para obtener los estados de paciente que puede tener una factura
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosPacienteFactura(Connection con)
	{
		return utilidadesDao().obtenerEstadosPacienteFactura(con);
	}
	
	/**
	 * Método encargado de obtener los convenios por usuario
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConvenioPorUsuario (Connection con, String loginUsuario, String tipoUsuario, 
			boolean activo)
	{
		return utilidadesDao().obtenerConvenioPorUsuario(con, loginUsuario, tipoUsuario, activo);
	}
	
	/**
	 * Método encargado de obtener los convenios por usuario
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 */
	public static boolean esUsuarioGlosaConvenio (String loginUsuario, String tipoUsuario,boolean activo, int convenio)
	{
		boolean esUsuarioValido=false;
		Connection con= UtilidadBD.abrirConexion();
		esUsuarioValido=utilidadesDao().esUsuarioGlosaConvenio(con, loginUsuario, tipoUsuario, activo, convenio);
		UtilidadBD.closeConnection(con);
		return esUsuarioValido;
	}
	
	/**
	 * Método para retornar el nombre del tarifario oficial
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreTarifarioOficial(Connection con,int codigo)
	{
		return utilidadesDao().obtenerNombreTarifarioOficial(con, codigo);
	}

	/**
	 * 
	 * @param institucion
	 * @param valorConsecutivo
	 * @return
	 */
	public static boolean esConsecutvioAsignadoFactura(int institucion,String valorConsecutivo) 
	{
		boolean resultado=false;
		Connection con = null;
	    //Abrimos la conexion con la fuente de Datos 
		con = util.UtilidadBD.abrirConexion();
		if(con == null)
			return resultado;
		else
			 resultado=utilidadesDao().esConsecutvioAsignadoFactura(con, valorConsecutivo, institucion);
		util.UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Método para verificar si un convenio es de reporte inicial de urgencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static  boolean esConvenioReporteAtencionInicialUrgencias(Connection con,int codigoConvenio)
	{
		return utilidadesDao().esConvenioReporteAtencionInicialUrgencias(con, codigoConvenio);
	}
	
	/**
	 * Método para verificar si un convenio es de reporte inicial de urgencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static  boolean esConvenioReporteInconsistenciasenBD(Connection con,int codigoConvenio)
	{
		return utilidadesDao().esConvenioReporteInconsistenciasenBD(con, codigoConvenio);
	}
	
	/**
	 * Método para obtener la entidad subcontrata por el codigo pk
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public static DtoEntidadSubcontratada obtenerEntidadSubcontratada(Connection con,String consecutivoEntidad)
	{
		return utilidadesDao().obtenerEntidadSubcontratada(con, consecutivoEntidad);
	}
	
	/**
	 * Metodo para obtener los contratos parametrizados para una entidad subcontratada segun su codigo
	 * @param entidadSubcontratada
	 * @return
	 */
	public static HashMap obtenerContratosPorEntidadSubcontratada(String entidadSubcontratada)
	{
		return utilidadesDao().obtenerContratosPorEntidadSubcontratada(entidadSubcontratada);
	}
	
	/**
	 * Método para consultar la cuenta contable de un tipo especifico del tipo convenio
	 * @param con
	 * @param codigo
	 * @param codigoInstitucion
	 * @param cuentaConvenio
	 * @param cuentaValorConvenio
	 * @param cuentaValorPaciente
	 * @param cuentaDescuentoPaciente
	 * @param cuentaCxCCapitacion
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableTipoConvenio(Connection con,String codigo,int codigoInstitucion,boolean cuentaConvenio,boolean cuentaValorConvenio,boolean cuentaValorPaciente,boolean cuentaDescuentoPaciente,boolean cuentaCxCCapitacion)
	{
		return utilidadesDao().consultarCuentaContableTipoConvenio(con, codigo, codigoInstitucion, cuentaConvenio, cuentaValorConvenio, cuentaValorPaciente, cuentaDescuentoPaciente, cuentaCxCCapitacion);
	}
	
	
	public static DtoCuentaContable consultarCuentaContableParticipacionPoolTarifasConvenio(Connection con,int codigoPool,int codigoConvenio,int codigoEsquemaTarifario,int codigoInstitucion,boolean cuentaPool,boolean cuentaInstitucion,boolean cuentaVigenciaAnterior)
	{
		return utilidadesDao().consultarCuentaContableParticipacionPoolTarifasConvenio(con, codigoPool, codigoConvenio, codigoEsquemaTarifario, codigoInstitucion, cuentaPool, cuentaInstitucion, cuentaVigenciaAnterior);
	}
	
	/**
	 * Método para consultar la cuenta contable del paquete de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param cuentaMayorValor
	 * @param cuentaMenorValor
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContablePaquetexSolicitud(Connection con,String numeroSolicitud,boolean cuentaMayorValor,boolean cuentaMenorValor)
	{
		return utilidadesDao().consultarCuentaContablePaquetexSolicitud(con, numeroSolicitud, cuentaMayorValor, cuentaMenorValor);
	}


	
	/**
	 * 
	 * @param dtoDeudor
	 * @return
	 */
	public static ArrayList<DtoDeudor> obtenerDeudores(DtoDeudor dtoDeudor)
	{
		return utilidadesDao().obtenerDeudores(dtoDeudor);
	}
	
	
	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public static String obtenerPrefijoFacturaXConsecutivo(String consecutivoFac,int institucion) 
	{
		return utilidadesDao().obtenerPrefijoFacturaXConsecutivo(consecutivoFac,institucion);
	}

	public static double consultarValorAnticipoDisponible(Connection con, int codigoContrato)
	{
		return utilidadesDao().consultarValorAnticipoDisponible(con, codigoContrato);
		
	}
	
	/**
	 * Método para verificar si una solicitud ya fue facturada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudFacturada(Connection con,BigDecimal numeroSolicitud)
	{
		return utilidadesDao().esSolicitudFacturada(con, numeroSolicitud);
	}
	
	/**
	 * Método implementado para actualizar el pool en la factura de la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean actualizarPoolFacturaSolicitud(Connection con,String numeroSolicitud,int codigoPool,int codigoMedico)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoPool", codigoPool);
		campos.put("codigoMedico", codigoMedico);
		return utilidadesDao().actualizarPoolFacturaSolicitud(con, campos);
	}
	
	/**
	 * Método para cargar los servicios/articulos incluidos de un servicio principal
	 * @param con
	 * @param servicioPrincipal
	 */
	public static void cargarServiciosArticulosIncluidos(Connection con,DtoServiPpalIncluido servicioPrincipal)
	{
		utilidadesDao().cargarServiciosArticulosIncluidos(con, servicioPrincipal);
	}

	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public static String obtenerPrefijoFacturaXInstitucion(Connection con,int institucion) 
	{
		return utilidadesDao().obtenerPrefijoFacturaXInstitucion(con,institucion);
	}
	
	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public static String obtenerPrefijoFacturaXInstitucion(int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String returna= utilidadesDao().obtenerPrefijoFacturaXInstitucion(con,institucion);
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	/**
	 * metodo para consultar las facturas que tienen el mismo consecutivo de factura
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ArrayList<DtoIgualConsecutivoFactura> consultarFacturasMismoConsecutivo(BigDecimal consecutivoFactura, int codigoInstitucionInt) 
	{
		return utilidadesDao().consultarFacturasMismoConsecutivo(consecutivoFactura, codigoInstitucionInt);
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap obtenerInfoUltimaCuentaFacturada(Connection con,int codigoPaciente, boolean filtrarConvenioPYP) {
		HashMap filtros = new HashMap();
		filtros.put("codigoPaciente", codigoPaciente);
		filtros.put("filtrarConvenioPYP", filtrarConvenioPYP);
		return utilidadesDao().obtenerInfoUltimaCuentaFacturada(con,filtros);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap cargarTiposAfiliado(Connection con,int institucion,String activo,int codigoConvenio,int codigoViaIngreso, String fechaReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion+"");
		campos.put("activo",activo);
		campos.put("codigoConvenio", codigoConvenio);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("fechaReferencia", fechaReferencia);
		return utilidadesDao().cargarTiposAfiliado(con, campos);
	}
	
	/**
	 * Metod encargado de consultar los tipos de afiliado por estrato_social seleccionado
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param codigoConvenio
	 * @param codigoViaIngreso
	 * @return HashMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarTiposAfiliadoXEstrato(Connection con,int institucion,String activo,
			int codigoConvenio,int codigoViaIngreso, int codigoEstratoSocial, String fechaReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",institucion+"");
		campos.put("activo",activo);
		campos.put("codigoConvenio", codigoConvenio);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		campos.put("codigoEstratoSocial", codigoEstratoSocial);
		campos.put("fechaReferencia", fechaReferencia);
		return utilidadesDao().cargarTiposAfiliadoxEstrato(con, campos);
	}

	/**
	 * s
	 * @param con
	 * @param codigoFactura
	 */
	public static String obtenerTamanioImpresionFactura(Connection con,String codigoFactura) 
	{
		return utilidadesDao().obtenerTamanioImpresionFactura(con,codigoFactura);
	}

	/***
	 * 
	 * @param con
	 * @param codigoServicioSolicitado
	 * @return
	 */
	public static boolean esServicioOdontologico(Connection con,
			int codigoServicioSolicitado) {
		return utilidadesDao().esServicioOdontologico(con,codigoServicioSolicitado);
	}

	/**
	 * 
	 * @param detalleCodigo
	 * @return
	 */
	public static boolean montoCobroAsociadoSubcuenta(int detalleCodigoMonto) 
	{
		return utilidadesDao().montoCobroAsociadoSubcuenta(detalleCodigoMonto);
	}
	
	
	/**
	 * @param codigoArticulo
	 * @return
	 */
	public  static Integer tipoMedicamento(int codigoArticulo)
	{
		return utilidadesDao().tipoMedicamento(codigoArticulo);
	}
	
	/**
	 * Metodo que consulta el tipo de codigo articulo segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 */
	public static int consultarTipoConvenioArticulo(Connection con, String convenio) {
		
		return utilidadesDao().consultarTipoConvenioArticulo(con, convenio);
	}
	
	/**
	 * Metodo que consulta el tipo de codigo servicio segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 */
	public static int consultarTipoConvenioServicio(Connection con, String convenio) {
		
		return utilidadesDao().consultarTipoConvenioServicio(con, convenio);
	}
}