/*
 * Abr 27, 2007
 */
package com.princetonsa.dao.oracle.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.UtilidadesFacturacionDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseUtilidadesFacturacionDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoIgualConsecutivoFactura;
import com.princetonsa.dto.facturacion.DtoServiPpalIncluido;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 * Métodos de oracle para el acceso a la fuente de datos para las utilidades
 * del módulo de FACTURACION
 */
public class OracleUtilidadesFacturacionDao implements UtilidadesFacturacionDao 
{

	/**
	 * Método implementado para cargar los convenios por una clasificacion específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarConveniosXClasificacion(Connection con, HashMap campos) 
	{
		return SqlBaseUtilidadesFacturacionDao.cargarConveniosXClasificacion(con, campos);
	}
	
	/**
	 * Método que consulta los tarifarios oficiales
	 * @param con
	 * @return
	 */
	public HashMap cargarTarifariosOficiales(Connection con)
	{
		return SqlBaseUtilidadesFacturacionDao.cargarTarifariosOficiales(con);
	}
	
	/**
	 * Método implementado para obtener el valor del salario mínimo vigente
	 * @param con
	 * @return
	 */
	public double obtenerValorSalarioMinimoVigente(Connection con)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerValorSalarioMinimoVigente(con);
	}
	
	/**
	 * Método que carga los estratos sociales
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarEstratosSociales(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.cargarEstratosSociales(con, campos, DaoFactory.ORACLE);
	}
	
	/**
	 * Consulta el nombre del tipo de regimen segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public String consultarNombreTipoRegimen(Connection con, int convenio)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarNombreTipoRegimen(con, convenio);
	}
	
	/**
	 * Consulta el nombre del convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public String consultarNombreConvenio(Connection con, int convenio)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarNombreConvenio(con, convenio);
	}
	
	/**
	 * Consulta el codigo del convenio 
	 * @param con
	 * @param codigo factura
	 * @return
	 */
	public String consultarCodigoConvenio(Connection con, String codigoFactura){
		
		return SqlBaseUtilidadesFacturacionDao.consultarCodigoConvenio(con, codigoFactura);
	}
	
	/**
	 * Verifica si el convenio requiere justificacion de servicio
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean requiereJustificacioServ(Connection con, int convenio)
	{
		return SqlBaseUtilidadesFacturacionDao.requiereJustificacioServ(con, convenio);
	}

	/**
	 * Verifica si el convenio requiere justificacion de articulo
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean requiereJustificacioArt(Connection con, int convenio, int art)
	{
		return SqlBaseUtilidadesFacturacionDao.requiereJustificacioArt(con, convenio, art);
	}

	/**
	 * 
	 */
	public HashMap obtenerPaquetesValidosResponsable(Connection con, HashMap vo) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerPaquetesValidosResponsable(con, vo);
	}

	/**
	 * 
	 */
	public String obtenerTarfiaServicio(Connection con, int esquemaTarifario, int servicio, String fechaCalculoVigencia) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTarfiaServicio(con, esquemaTarifario,servicio, fechaCalculoVigencia);
	}
	
	/**
	 * Método que consulta los montos de cobro por convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMontosCobroXConvenio(Connection con, String codigoConvenio, String fechaReferencia)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerMontosCobroXConvenio(con, codigoConvenio, fechaReferencia);
	}

	/***
	 * 
	 */
	public boolean esServicioPrincComponentePaquete(Connection con, String codigoPaquete, int institucion, String servicio) 
	{
		return SqlBaseUtilidadesFacturacionDao.esServicioPrincComponentePaquete(con, codigoPaquete,institucion,servicio);
	}
	
	/**
	 * 
	 */
	public ArrayList obtenerServiciosPrincipalesPaquete(Connection con, String codigoPaquete, int institucion) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerServiciosPrincipalesPaquete(con,codigoPaquete,institucion);
	}
	
	/**
	 * Método implementado para eliminar los registros en solicitudes_subcuenta por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean eliminarRegistrosSolicitudesSubCuenta(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesFacturacionDao.eliminarRegistrosSolicitudesSubCuenta(con, numeroSolicitud);
	}
	

	/**
	 * 
	 */
	public double obtenerValorUnitarioCargadoConvenioBase(Connection con, String numerSolicitud, String codServArti,String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerValorUnitarioCargadoConvenioBase(con, numerSolicitud, codServArti,servicioCX,detCxHonorarios,detAsCxSalMat, esServicio,cargoFacturado);
	}
	
	/**
	 * 
	 */
	public int obtenerEsquemaTarifarioBase(Connection con, String numerSolicitud, String codServArti, String serviciocx,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerEsquemaTarifarioBase(con, numerSolicitud, codServArti,serviciocx,detCxHonorarios,detAsCxSalMat, esServicio,cargoFacturado);
	}
	

	/**
	 * 
	 */
	public boolean responsableTieneSolPaquetes(Connection con, String subCuenta) 
	{
		return SqlBaseUtilidadesFacturacionDao.responsableTieneSolPaquetes(con, subCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerResposablePaquetizadoDadaSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerResposablePaquetizadoDadaSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio ha sido distribuido
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioDistribuido(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudServicioAsocioDistribuido(con, campos);
	}
	
	/**
	 * Verifica su una solicitud/servicio/asocio ha sido facturado
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioFacturado(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudServicioAsocioFacturado(con, campos);
	}
	
	/**
	 * Método que verifica si una solicitud es paquetizada
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudPaquetizada(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudPaquetizada(con, campos);
	}
	
	/**
	 * Método que verifica si una solicitud/servicio/asocio está cargado
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioCargado(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudServicioAsocioCargado(con, campos);
	}
	
	/**
	 * Método que consulta los convenios asociados a una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarConveniosSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.cargarConveniosSolicitud(con, campos);
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
	public ArrayList<HashMap<String, Object>> obtenerPooles(Connection connection,HashMap parametros)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerPooles(connection, parametros);
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
	public ArrayList<HashMap<String, Object>> obtenerConceptosPagoPooles(Connection connection,HashMap parametros)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerConceptosPagoPooles(connection, parametros);
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
	public ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,HashMap parametros)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTiposServicio(connection, parametros);
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
	public ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,String acronimos, String esqx)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTiposServicio(connection, acronimos, esqx);
	}

	/**
	 * 
	 */
	public String obtenerconsecutivoFacturaDetCargo(Connection con, String codigoDetCargo) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerconsecutivoFacturaDetCargo(con, codigoDetCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estadoFactura
	 * @return
	 */
	public boolean subCuentaConFacturas(Connection con, String subCuenta, int estadoFactura)
	{
		return SqlBaseUtilidadesFacturacionDao.subCuentaConFacturas(con, subCuenta,estadoFactura);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean responsableTieneSolAsociadas(Connection con, String subCuenta)
	{
		return SqlBaseUtilidadesFacturacionDao.responsableTieneSolAsociadas(con, subCuenta);
	}
	
	/**
	 * Método que verifica si un convenio es de colsanitas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esConvenioColsanitas(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.esConvenioColsanitas(con, campos);
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
	public ArrayList<HashMap<String, Object>> obtenerTiposLiquidacion(Connection connection,String acronimos)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTiposLiquidacion(connection, acronimos);
	}
	
	/**
	 * Método para consultar el código del grupo del servicio de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int consultarGrupoServicio(Connection con,int codigoServicio)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarGrupoServicio(con, codigoServicio);
	}
	
	/**
	 * Método implementado para consultar los centros de costo de un grupo de servicio por el centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarCentrosCostoGrupoServicio(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarCentrosCostoGrupoServicio(con, campos);
	}
	
	/**
	 * Método para obtener los pooles vigentes de un profesional
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPoolesProfesional(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerPoolesProfesional(con, campos);
	}
	
	/**
	 * Método implementado para obtener la descripcion de un tipo de servicio
	 * @param con
	 * @param tipoServicio
	 * @return
	 */
	public String obtenerNombreTipoServicio(Connection con,String tipoServicio)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerNombreTipoServicio(con, tipoServicio);
	}
	
	/**
	 * Metodo Implementado para Extraer toda la informacion de la tabla tipo_tercero
	 */
	public ArrayList<HashMap<String, Object>> tipoTercero(Connection con) 
	{
		return SqlBaseUtilidadesFacturacionDao.tipoTercero(con);
	}
	
	/**
	 * Método que verifica que un diagnóstico es requerido
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public boolean esRequeridoDiagnosticoServicio(Connection con,int codigoServicio)
	{
		return SqlBaseUtilidadesFacturacionDao.esRequeridoDiagnosticoServicio(con, codigoServicio);
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
	public ArrayList<HashMap<String, Object>> obtenerConseptosFacturasVarias(Connection connection,HashMap parametros)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerConseptosFacturasVarias(connection, parametros);
	}
	
	/**
	 * 
	 */
	public int ccPlanEspecialConvenio(Connection con, int codigo) 
	{
		return SqlBaseUtilidadesFacturacionDao.ccPlanEspecialConvenio(con, codigo);
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Deudor
	 */
	public String esConvenioExcentoDeudor(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		return SqlBaseUtilidadesFacturacionDao.esConvenioExcentoDeudor(con, codigoConvenio, codigoInstitucionInt);
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Documento de Garantia
	 */
	public String esConvenioExcentoDocGarantia(Connection con, int codigoConvenio, int codigoInstitucionInt) 
	{
		return SqlBaseUtilidadesFacturacionDao.esConvenioExcentoDocGarantia(con, codigoConvenio, codigoInstitucionInt);
	}
	
	/**
	 * Metodo para insertar el registro log de la Interfaz Facturas Varias en la tabla log_adconsta
	 */
	public void insertarLogAdconsta(Connection con, String loginUsuario, String resultado) 
	{
		String consulta="INSERT INTO log_adconsta " +
						"(codigo, " +
						"consecutivo_anterior, " +
						"consecutivo_actualizado, " +
						"fecha, " +
						"hora, " +
						"usuario, " +
						"indicativo)" +
						"VALUES (seq_log_adconsta.nextval,?,?,?,?,?,? ) ";
		SqlBaseUtilidadesFacturacionDao.insertarLogAdconsta(con, loginUsuario, resultado, consulta);
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Sin Contrato
	 */
	public String esSinContrato(Connection con, int codigoContrato) 
	{
		return SqlBaseUtilidadesFacturacionDao.esSinContrato(con, codigoContrato);
	}
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Controla Anticipos
	 */
	public String esControlAnticipos(Connection con, int contrato) 
	{
		return SqlBaseUtilidadesFacturacionDao.esControlaAnticipos(con, contrato);
	}
	
	/**
	 * Método que verifica si una solicitud está totalmente pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudTotalPendiente(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudTotalPendiente(con, numeroSolicitud);
	}
	
	/**
	 * Método que retorna si hay ventas por facturar por centro de costo
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public boolean hayVentasXFacturar(Connection con, String anoCorte, String mesCorte, String centroAtencion, String centroCosto)
	{
		return SqlBaseUtilidadesFacturacionDao.hayVentasXFacturar(con, anoCorte, mesCorte, centroAtencion, centroCosto);
	}
	
	/**
	 * Método que retorna si un servicio es nopos
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public boolean esServicioPos(Connection con, int servicio){
		return SqlBaseUtilidadesFacturacionDao.esServicioPos(con, servicio);
	}
	
	/**
	 * Método implementado para obtener los tipos de recargo
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposRecargo(Connection con)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTiposRecargo(con);
	}
	
	/**
	 * Método para obtener la especialidad del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public InfoDatosInt obtenerEspecialidadServicio(Connection con,int codigoServicio)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerEspecialidadServicio(con, codigoServicio);
	}
	

	/**
	 * 
	 */
	public int obtenerContratoSubCuenta(Connection con, String idSubCuenta) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerContratoSubCuenta(con, idSubCuenta);
	}

	/**
	 * Metodo que verifica que todos los cargos de las solicitudes asociadas estan en estado excento o anulado 
	 * retorna true si puede cerrar el ingreso false si no.
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean puedoCerrarIngreso(Connection con, int ingreso) {
		
		return SqlBaseUtilidadesFacturacionDao.puedoCerrarIngreso(con,ingreso);
	}

	/**
	 * 
	 */
	public boolean indicadorParametrizado(Connection con, String indicador) {
		
		return SqlBaseUtilidadesFacturacionDao.indicadorParametrizado(con, indicador);
	}
	

	/**
	 * 
	 */
	public String obtenerFechaFacturaResponsable(Connection con, String idSubCuenta) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerFechaFacturaResponsable(con, idSubCuenta);
	}
	
	/**
	 * Método para obtener las facturas asociadas a la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerConsecutivoFacturaXSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerConsecutivoFacturaXSolicitud(con, campos);
	}
	
	/**
	 * Método implementado para obtener las empresas parametrizadas en el sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con){
		return SqlBaseUtilidadesFacturacionDao.obtenerEmpresas(con);
	}
	
	/**
	 * Método implementado para obtener la descripción de un requisito de paciente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerDescripcionRequisitoPaciente(Connection con,int codigo)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerDescripcionRequisitoPaciente(con, codigo);
	}
	
	/**
	 * Método para obtener el nit de la empresa del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNitEmpresaConvenio(Connection con,int codigoConvenio)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerNitEmpresaConvenio(con, codigoConvenio);
	}
	
	/**
	 * Método que retorna si el rango de fechas ya existe en la
	 * parametrización de salarios mínimos
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public boolean existeFechasEnSalarioMinimo(Connection con, String fechaInicial, String fechaFinal,Integer idRegistroParaNoTenerEnCuenta)
	{
		return SqlBaseUtilidadesFacturacionDao.existeFechasEnSalarioMinimo(con, fechaInicial, fechaFinal,idRegistroParaNoTenerEnCuenta);
	}
	
	/**
	 * Método para obtener los estados de paciente que puede tener una factura
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEstadosPacienteFactura(Connection con)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerEstadosPacienteFactura(con);
	}
	
	/**
	 * Método encargado de obtener los convenios por usuario
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConvenioPorUsuario (Connection con, 
			String loginUsuario, String tipoUsuario, boolean activo)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerConvenioPorUsuario(con, loginUsuario, tipoUsuario, 
				activo);
	}
	
	/**
	 * Método para retornar el nombre del tarifario oficial
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreTarifarioOficial(Connection con,int codigo)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerNombreTarifarioOficial(con, codigo);
	}
	

	/**
	 * 
	 */
	public boolean esConsecutvioAsignadoFactura(Connection con,String valorConsecutivo, int institucion) 
	{
		return SqlBaseUtilidadesFacturacionDao.esConsecutvioAsignadoFactura(con, valorConsecutivo,institucion);
	}
	
	/**
	 * Método para verificar si un convenio es de reporte inicial de urgencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioReporteAtencionInicialUrgencias(Connection con,int codigoConvenio)
	{
		return SqlBaseUtilidadesFacturacionDao.esConvenioReporteAtencionInicialUrgencias(con, codigoConvenio);
	}
	
	/**
	 * Método para verificar si un convenio es de reporte inconsistencias en Base de datos
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioReporteInconsistenciasenBD(Connection con,int codigoConvenio)
	{
		return SqlBaseUtilidadesFacturacionDao.esConvenioReporteInconsistenciasenBD(con, codigoConvenio);
	}
	
	/**
	 * Método para obtener la entidad subcontrata por el codigo pk
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public DtoEntidadSubcontratada obtenerEntidadSubcontratada(Connection con,String consecutivoEntidad)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerEntidadSubcontratada(con, consecutivoEntidad);
	}
	
	/**
	 * Metodo para obtener los contratos parametrizados para una entidad subcontratada segun su codigo
	 * @param entidadSubcontratada
	 * @return
	 */
	public HashMap obtenerContratosPorEntidadSubcontratada(String entidadSubcontratada)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerContratosPorEntidadSubcontratada(entidadSubcontratada);
	}

	/**
	 * Metodo para determinar si un Usuario tiene permisos para Responder Consultas para una Entidad Subcontratada
	 */
	public boolean esUsuarioconPermisoResponderConsultasEntSubcont(String entidadSubcontratada, String usuario) {
		
		return SqlBaseUtilidadesFacturacionDao.esUsuarioconPermisoResponderConsultasEntSubcont(entidadSubcontratada,usuario);
	}
	
	@Override
	public boolean esUsuarioGlosaConvenio(Connection con, String loginUsuario,
			String tipoUsuario, boolean activo, int convenio) 
	{
		return SqlBaseUtilidadesFacturacionDao.esUsuarioGlosaConvenio(con, loginUsuario, tipoUsuario, activo, convenio);
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
	public DtoCuentaContable consultarCuentaContableTipoConvenio(Connection con,String codigo,int codigoInstitucion,boolean cuentaConvenio,boolean cuentaValorConvenio,boolean cuentaValorPaciente,boolean cuentaDescuentoPaciente,boolean cuentaCxCCapitacion)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarCuentaContableTipoConvenio(con, codigo, codigoInstitucion, cuentaConvenio, cuentaValorConvenio, cuentaValorPaciente, cuentaDescuentoPaciente, cuentaCxCCapitacion);
	}
	
	/**
	 * Método para consultar la cuenta contable de la apramtrizacion de participacion pool tarifas x convenio
	 * @param con
	 * @param codigoPool
	 * @param codigoConvenio
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @param cuentaPool
	 * @param cuentaInstitucion
	 * @param cuentaVigenciaAnterior
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableParticipacionPoolTarifasConvenio(Connection con,int codigoPool,int codigoConvenio,int codigoEsquemaTarifario,int codigoInstitucion,boolean cuentaPool,boolean cuentaInstitucion,boolean cuentaVigenciaAnterior)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarCuentaContableParticipacionPoolTarifasConvenio(con, codigoPool, codigoConvenio, codigoEsquemaTarifario, codigoInstitucion, cuentaPool, cuentaInstitucion, cuentaVigenciaAnterior);
	}
	
	/**
	 * Método para consultar la cuenta contable del paquete de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param cuentaMayorValor
	 * @param cuentaMenorValor
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContablePaquetexSolicitud(Connection con,String numeroSolicitud,boolean cuentaMayorValor,boolean cuentaMenorValor)
	{
		return SqlBaseUtilidadesFacturacionDao.consultarCuentaContablePaquetexSolicitud(con, numeroSolicitud, cuentaMayorValor, cuentaMenorValor);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoDeudor> obtenerDeudores (DtoDeudor dto){
		return SqlBaseUtilidadesFacturacionDao.obtenerDeudores(dto);
	}
	

	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public String obtenerPrefijoFacturaXConsecutivo(String consecutivoFac,int institucion)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerPrefijoFacturaXConsecutivo(consecutivoFac,institucion);
	}

	@Override
	public double consultarValorAnticipoDisponible(Connection con, int codigoContrato) {
		return SqlBaseUtilidadesFacturacionDao.consultarValorAnticipoDisponible(con, codigoContrato);
	}
	
	/**
	 * Método para verificar si una solicitud ya fue facturada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudFacturada(Connection con,BigDecimal numeroSolicitud)
	{
		return SqlBaseUtilidadesFacturacionDao.esSolicitudFacturada(con, numeroSolicitud);
	}
	
	/**
	 * Método implementado para actualizar el pool en la factura de la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean actualizarPoolFacturaSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesFacturacionDao.actualizarPoolFacturaSolicitud(con, campos);
	}
	
	/**
	 * Método para cargar los servicios/articulos incluidos de un servicio principal
	 * @param con
	 * @param servicioPrincipal
	 */
	public void cargarServiciosArticulosIncluidos(Connection con,DtoServiPpalIncluido servicioPrincipal)
	{
		SqlBaseUtilidadesFacturacionDao.cargarServiciosArticulosIncluidos(con, servicioPrincipal);
	}
	
	public String obtenerPrefijoFacturaXInstitucion(Connection con,int institucion)
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerPrefijoFacturaXInstitucion(con,institucion);
	}

	@Override
	public ArrayList<DtoIgualConsecutivoFactura> consultarFacturasMismoConsecutivo(
			BigDecimal consecutivoFactura, int codigoInstitucionInt) {
		return SqlBaseUtilidadesFacturacionDao.consultarFacturasMismoConsecutivo(consecutivoFactura, codigoInstitucionInt);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public HashMap obtenerInfoUltimaCuentaFacturada(Connection con, HashMap filtros){
		return SqlBaseUtilidadesFacturacionDao.obtenerInfoUltimaCuentaFacturada(con,filtros);
	}

	@Override
	public HashMap cargarTiposAfiliado(Connection con, HashMap campos) {
		return SqlBaseUtilidadesFacturacionDao.cargarTiposAfiliado(con,campos, DaoFactory.ORACLE);
	}
	
	@Override
	public HashMap cargarTiposAfiliadoxEstrato(Connection con, HashMap campos) {
		return SqlBaseUtilidadesFacturacionDao.cargarTiposAfiliadoXEstrato(con,campos, DaoFactory.ORACLE);
	}

	@Override
	public String obtenerTamanioImpresionFactura(Connection con,String codigoFactura) 
	{
		return SqlBaseUtilidadesFacturacionDao.obtenerTamanioImpresionFactura(con,codigoFactura);
	}
	
	@Override
	public boolean esServicioOdontologico(Connection con,
			int codigoServicioSolicitado) {
		return SqlBaseUtilidadesFacturacionDao.esServicioOdontologico(con,codigoServicioSolicitado);
	}
	

	public boolean montoCobroAsociadoSubcuenta(int detalleCodigoMonto)
	{
		return SqlBaseUtilidadesFacturacionDao.montoCobroAsociadoSubcuenta(detalleCodigoMonto);
	}
	
	
	/**
	 * @see com.princetonsa.dao.facturacion.UtilidadesFacturacionDao#tipoMedicamento(int)
	 */
	public  Integer tipoMedicamento(int codigoArticulo)
	{
		return SqlBaseUtilidadesFacturacionDao.tipoMedicamento(codigoArticulo);
	}
	
	/**
	 * Metodo que consulta el tipo de codigo articulo segun el convenio
	 * @param con
	 * @param convenio
	 * @author leoquico
	 * @return
	 */
	public int consultarTipoConvenioArticulo(Connection con, String convenio) {
		
		return SqlBaseUtilidadesFacturacionDao.consultarTipoConvenioArticulo(con, convenio);
	}
	
	/**
	 * Metodo que consulta el tipo de codigo servicio segun el convenio
	 * @param con
	 * @param convenio
	 * @author leoquico
	 * @return
	 */
	public int consultarTipoConvenioServicio(Connection con, String convenio) {
		
		return SqlBaseUtilidadesFacturacionDao.consultarTipoConvenioServicio(con, convenio);
	}

}