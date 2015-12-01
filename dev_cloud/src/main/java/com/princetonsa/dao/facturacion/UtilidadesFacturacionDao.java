/*
 * Abr 27, 2007
 */
package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoIgualConsecutivoFactura;
import com.princetonsa.dto.facturacion.DtoServiPpalIncluido;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 *	Interface usada para gestionar los m�todos de acceso a la base
 * de datos en utilidades del modulo de FACTURACION
 */
public interface UtilidadesFacturacionDao 
{
	/**
	 * M�todo implementado para cargar los convenios por una clasificacion espec�fica
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarConveniosXClasificacion(Connection con,HashMap campos);
	
	/**
	 * Metodo que verifica que todos los cargos de las solicitudes asociadas estan en estado excento o anulado 
	 * retorna true si puede cerrar el ingreso false si no.
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean puedoCerrarIngreso(Connection con, int ingreso);
	
	/**
	 * M�todo que consulta los tarifarios oficiales
	 * @param con
	 * @return
	 */
	public HashMap cargarTarifariosOficiales(Connection con);
	
	/**
	 * M�todo implementado para obtener el valor del salario m�nimo vigente
	 * @param con
	 * @return
	 */
	public double obtenerValorSalarioMinimoVigente(Connection con);
	
	/**
	 * M�todo que carga los estratos sociales
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarEstratosSociales(Connection con,HashMap campos);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap obtenerPaquetesValidosResponsable(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param servicio
	 * @return
	 */
	public String obtenerTarfiaServicio(Connection con, int esquemaTarifario, int servicio, String fechaCalculoVigencia);
	
	/**
	 * M�todo que consulta los montos de cobro por convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMontosCobroXConvenio(Connection con,String codigoConvenio, String fechaReferencia);

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @param servicio
	 * @return
	 */
	public boolean esServicioPrincComponentePaquete(Connection con, String codigoPaquete, int institucion, String servicio);
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerServiciosPrincipalesPaquete(Connection con, String codigoPaquete, int institucion);
	
	/**
	 * M�todo implementado para eliminar los registros en solicitudes_subcuenta por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean eliminarRegistrosSolicitudesSubCuenta(Connection con,String numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numerSolicitud
	 * @param codServArti
	 * @param servicioCX 
	 * @param esServicio
	 * @param cargoFacturado 
	 * @return
	 */
	public double obtenerValorUnitarioCargadoConvenioBase(Connection con, String numerSolicitud, String codServArti, String servicioCX,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException; 

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
	public int obtenerEsquemaTarifarioBase(Connection con, String numerSolicitud, String codServArti, String serviciocx,String detCxHonorarios,String detAsCxSalMat, boolean esServicio, String cargoFacturado) throws BDException;

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean responsableTieneSolPaquetes(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerResposablePaquetizadoDadaSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * M�todo que verifica si una solicitud/servicio/asocio ha sido distribuido
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioDistribuido(Connection con,HashMap campos);
	
	/**
	 * Verifica su una solicitud/servicio/asocio ha sido facturado
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioFacturado(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si una solicitud es paquetizada
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudPaquetizada(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si una solicitud/servicio/asocio est� cargado
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esSolicitudServicioAsocioCargado(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los convenios asociados a una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarConveniosSolicitud(Connection con,HashMap campos);
	
	
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
	public ArrayList<HashMap<String, Object>> obtenerPooles(Connection connection,HashMap parametros);
	
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
	public ArrayList<HashMap<String, Object>> obtenerConceptosPagoPooles(Connection connection,HashMap parametros);
	
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
	public ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,HashMap parametros);

	
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
	public ArrayList<HashMap<String, Object>> obtenerTiposServicio(Connection connection,String acronimos, String esqx);
	
	/**
	 * 
	 * @param con
	 * @param codigoDetCargo
	 * @return
	 */
	public String obtenerconsecutivoFacturaDetCargo(Connection con, String codigoDetCargo);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param estadoFactura
	 * @return
	 */
	public boolean subCuentaConFacturas(Connection con, String subCuenta, int estadoFactura);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean responsableTieneSolAsociadas(Connection con, String subCuenta);
	
	
	/**
	 * Verifica si el convenio requiere justificacion de servicio
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean requiereJustificacioServ(Connection con, int convenio);

	/**
	 * Verifica si el convenio requiere justificacion de articulo
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean requiereJustificacioArt(Connection con, int convenio, int art);
	
	
	
	/**
	 * Consulta el nombre del tipo de regimen segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public String consultarNombreTipoRegimen(Connection con, int convenio);
	
	/**
	 * Consulta el nombre del convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public String consultarNombreConvenio(Connection con, int convenio);
	
	/**
	 * Consulta el codigo del convenio 
	 * @param con
	 * @param codigo factura
	 * @return
	 */
	public String consultarCodigoConvenio(Connection con, String codigoFactura);
	
	/**
	 * M�todo que verifica si un convenio es de colsanitas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esConvenioColsanitas(Connection con,HashMap campos);
	
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
	public ArrayList<HashMap<String, Object>> obtenerTiposLiquidacion(Connection connection,String acronimos);
	
	
	/**
	 * Metodo que verifica si un indicador de calidad si se encuentra parametrizado
	 * @param con
	 * @param indicador
	 * @return
	 */
	public boolean indicadorParametrizado(Connection con, String indicador);
	
	/**
	 * M�todo para consultar el c�digo del grupo del servicio de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int consultarGrupoServicio(Connection con,int codigoServicio);
	
	/**
	 * M�todo implementado para consultar los centros de costo de un grupo de servicio por el centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarCentrosCostoGrupoServicio(Connection con, HashMap campos);
	
	/**
	 * M�todo para obtener los pooles vigentes de un profesional
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPoolesProfesional(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para obtener la descripcion de un tipo de servicio
	 * @param con
	 * @param tipoServicio
	 * @return
	 */
	public String obtenerNombreTipoServicio(Connection con,String tipoServicio);
	
	/**
	 * Metodo implementado para obtener toda la informaci�n de la tabla tipo_tercero
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> tipoTercero(Connection con);
	
	/**
	 * M�todo que verifica que un diagn�stico es requerido
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public boolean esRequeridoDiagnosticoServicio(Connection con,int codigoServicio);
	
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
	public ArrayList<HashMap<String, Object>> obtenerConseptosFacturasVarias(Connection connection,HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int ccPlanEspecialConvenio(Connection con, int codigo);

	
	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Deudor
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public String esConvenioExcentoDeudor(Connection con, int codigoConvenio, int codigoInstitucionInt);

	/**
	 * Utilidad para consultar si el convenio tiene activado el Excento Documento de Garantia
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public String esConvenioExcentoDocGarantia(Connection con, int codigoConvenio, int codigoInstitucionInt);

	/**
	 * Metodo para insertar el registro log de la Interfaz Facturas Varias en la tabla log_adconsta
	 * @param con
	 * @param loginUsuario
	 * @param resultado
	 */
	public void insertarLogAdconsta(Connection con, String loginUsuario, String resultado);

	/**
	 * Utilidad para consultar si el convenio tiene Activado el Sin Contrato
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public String esSinContrato(Connection con, int codigoContrato);
	
	/**
	 * Utilidad para consultar si el convenio tiene Activado el Controla Anticipos
	 * @param con
	 * @param contrato
	 * @return
	 */
	public String esControlAnticipos(Connection con, int contrato);
	
	/**
	 * M�todo que verifica si una solicitud est� totalmente pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudTotalPendiente(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo que retorna si un servicio es nopos
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public boolean esServicioPos(Connection con, int servicio);
	
	/**
	 * M�todo que retorna si hay ventas por facturar por centro de costo
	 * @param con
	 * @param Codigo Servicio
	 * @return
	 */
	public boolean hayVentasXFacturar(Connection con, String anoCorte, String mesCorte, String centroAtencion, String centroCosto);
	
	/**
	 * M�todo implementado para obtener los tipos de recargo
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposRecargo(Connection con);
	
	/**
	 * M�todo para obtener la especialidad del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public InfoDatosInt obtenerEspecialidadServicio(Connection con,int codigoServicio);

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public int obtenerContratoSubCuenta(Connection con, String idSubCuenta);

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public String obtenerFechaFacturaResponsable(Connection con, String idSubCuenta);
	
	/**
	 * M�todo para obtener las facturas asociadas a la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerConsecutivoFacturaXSolicitud(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para obtener las empresas parametrizadas en el sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con);
	
	/**
	 * M�todo implementado para obtener la descripci�n de un requisito de paciente
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerDescripcionRequisitoPaciente(Connection con,int codigo);
	
	/**
	 * M�todo para obtener el nit de la empresa del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNitEmpresaConvenio(Connection con,int codigoConvenio);

	/**
	 * M�todo que retorna si el rango de fechas ya existe en la
	 * parametrizaci�n de salarios m�nimos
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public boolean existeFechasEnSalarioMinimo(Connection con, String fechaInicial, String fechaFinal, Integer idRegistroParaNoTenerEnCuenta);
	
	/**
	 * M�todo para obtener los estados de paciente que puede tener una factura
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEstadosPacienteFactura(Connection con);
	
	/**
	 * M�todo encargado de obtener los convenios por usuario
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConvenioPorUsuario (Connection con, 
			String loginUsuario, String tipoUsuario, boolean activo);
	
	/**
	 * M�todo para retornar el nombre del tarifario oficial
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreTarifarioOficial(Connection con,int codigo);

	/**
	 * 
	 * @param con
	 * @param valorConsecutivo
	 * @param institucion
	 * @return
	 */
	public boolean esConsecutvioAsignadoFactura(Connection con,String valorConsecutivo, int institucion);
	
	/**
	 * M�todo para verificar si un convenio es de reporte inicial de urgencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioReporteAtencionInicialUrgencias(Connection con,int codigoConvenio);
    
	/**
	 * Metodo para verificar que este activo el reporte de inconsistencias
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioReporteInconsistenciasenBD(Connection con,int codigoConvenio);
	
	/**
	 * M�todo para obtener la entidad subcontrata por el codigo pk
	 * @param con
	 * @param consecutivoEntidad
	 * @return
	 */
	public DtoEntidadSubcontratada obtenerEntidadSubcontratada(Connection con,String consecutivoEntidad);
	
	/**
	 * Metodo para obtener los contratos parametrizados para una entidad subcontratada segun su codigo
	 * @param entidadSubcontratada
	 * @return
	 */
	public HashMap obtenerContratosPorEntidadSubcontratada(String entidadSubcontratada);

	
	/**
	 * Metodo para determinar si un Usuario tiene permisos para Responder Consultas para una Entidad Subcontratada
	 * @param entidadSubcontratada
	 * @param usuario
	 * @return
	 */
	public boolean esUsuarioconPermisoResponderConsultasEntSubcont(String entidadSubcontratada, String usuario);

	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param tipoUsuario
	 * @param activo
	 * @param convenio
	 * @return
	 */
	public boolean esUsuarioGlosaConvenio(Connection con, String loginUsuario,
			String tipoUsuario, boolean activo, int convenio);
	
	/**
	 * M�todo para consultar la cuenta contable de un tipo especifico del tipo convenio
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
	public DtoCuentaContable consultarCuentaContableTipoConvenio(Connection con,String codigo,int codigoInstitucion,boolean cuentaConvenio,boolean cuentaValorConvenio,boolean cuentaValorPaciente,boolean cuentaDescuentoPaciente,boolean cuentaCxCCapitacion);
	
	/**
	 * M�todo para consultar la cuenta contable de la apramtrizacion de participacion pool tarifas x convenio
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
	public DtoCuentaContable consultarCuentaContableParticipacionPoolTarifasConvenio(Connection con,int codigoPool,int codigoConvenio,int codigoEsquemaTarifario,int codigoInstitucion,boolean cuentaPool,boolean cuentaInstitucion,boolean cuentaVigenciaAnterior);
	
	/**
	
	/**
	 * M�todo para consultar la cuenta contable del paquete de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param cuentaMayorValor
	 * @param cuentaMenorValor
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContablePaquetexSolicitud(Connection con,String numeroSolicitud,boolean cuentaMayorValor,boolean cuentaMenorValor);


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoDeudor> obtenerDeudores(DtoDeudor dto);
	
	
	/**
	 * 
	 * @param consecutivoFac
	 * @param institucion
	 * @return
	 */
	public String obtenerPrefijoFacturaXConsecutivo(String consecutivoFac,int institucion);

	public double consultarValorAnticipoDisponible(Connection con, int codigoContrato);
	
	/**
	 * M�todo para verificar si una solicitud ya fue facturada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudFacturada(Connection con,BigDecimal numeroSolicitud);
	
	/**
	 * M�todo implementado para actualizar el pool en la factura de la solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean actualizarPoolFacturaSolicitud(Connection con,HashMap campos);
	
	/**
	 * M�todo para cargar los servicios/articulos incluidos de un servicio principal
	 * @param con
	 * @param servicioPrincipal
	 */
	public void cargarServiciosArticulosIncluidos(Connection con,DtoServiPpalIncluido servicioPrincipal);

	public String obtenerPrefijoFacturaXInstitucion(Connection con,int institucion);
	
	/**
	 * metodo para consultar las facturas que tienen el mismo consecutivo de factura
	 * @param con
	 * @param factura
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ArrayList<DtoIgualConsecutivoFactura> consultarFacturasMismoConsecutivo(BigDecimal consecutivoFactura, int codigoInstitucionInt); 
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap obtenerInfoUltimaCuentaFacturada(Connection con, HashMap filtros);

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarTiposAfiliado(Connection con, HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarTiposAfiliadoxEstrato(Connection con, HashMap campos);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerTamanioImpresionFactura(Connection con,
			String codigoFactura);

	/**
	 * 
	 * @param con
	 * @param codigoServicioSolicitado
	 * @return
	 */
	public boolean esServicioOdontologico(Connection con,
			int codigoServicioSolicitado);
	

	/**
	 * 
	 * @param detalleCodigoMonto
	 * @return
	 */
	public boolean montoCobroAsociadoSubcuenta(int detalleCodigoMonto);
	
	
	/**
	 * @param codigoArticulo
	 * @return
	 */
	public  Integer tipoMedicamento(int codigoArticulo);
	
	/**
	 * Metodo que consulta el tipo de codigo articulo segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 */
	public int consultarTipoConvenioArticulo(Connection con, String convenio); 
	
	/**
	 * Metodo que consulta el tipo de codigo servicio segun el convenio
	 * @param con
	 * @param convenio
	 * @return
	 * @author leoquico
	 */
	public int consultarTipoConvenioServicio(Connection con, String convenio); 
	
}