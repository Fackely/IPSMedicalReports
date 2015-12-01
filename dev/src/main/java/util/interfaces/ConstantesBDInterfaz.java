/**
 * 
 */
package util.interfaces;

/**
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public interface ConstantesBDInterfaz 
{
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de pacientes.
	 */
	public static final String identificadorTablaPacientes="interfaz_pacientes";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de Abonos.
	 */
	public static final String identificadorTablaAbonos="interfaz_abonos";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de articulos
	 */
	public static final String identificadorTablaArticulos="interfaz_articulos";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de terceros
	 */
	public static final String identificadorTablaTerceros="interfaz_terceros";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de Convenios x proveedor
	 */
	public static final String identificadorTablaConvenios="interfaz_convenios";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de saldos
	 */
	public static final String identificadorTablaSaldos="interfaz_saldos";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de transacciones
	 */
	public static final String identificadorTablaTransacciones="interfaz_transacciones";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de facturas
	 */
	public static final String identificadorTablaFacturas="interfaz_facturas";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de detalle facturas
	 */
	public static final String identificadorTablaDetFacturas="interfaz_det_facturas";
	
		
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de detalle facturas
	 */
	public static final String identificadorTablaDetFacturasPaquetes="interfaz_det_fact_paq";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de los tipos de documentos.
	 */
	public static final String identificadorTablaTipDocumentos="interfaz_tip_documentos";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de equivalencia de transacciones.
	 */
	public static final String identificadorTablaTransInventarios="interfaz_trans_inventarios";
	
	/**
	 * indentificador del registro que contiene el nombre real de la tabla para el manejo de ax_rips
	 * */
	public static final String identificadorTablaAxRips = "interfaz_rips";
	
	/**
	 * indentificador del registro que contiene el nombre real de la tabla para InterfazConsecutivos
	 * */
	public static final String identificadorTablaInterfazConsecutivos = "interfaz_consecutivos";
	
	
	/**
	 * indentificador del registro que contiene el nombre real de la tabla para InterfazNutricion
	 * */
	public static final String identificadorTablaInterfazNutricion = "interfaz_nutricion";
	
	/**
	 * identificador del registro que contiene los Saldos de Cartera de los Pacientes 
	 * */
	public static final String identificadorTablaInterfazSaldosCarteraPacientes = "interfaz_saldocarterapaciente";
	
	/**
	 * identificador del registro que contiene los Saldos de Cartera de los Pacientes 
	 * */
	public static final String identificadorTablaInterfazFactuCuentaMedicas = "interfaz_factucuenmedicas";	
	
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de facturas
	 */
	public static final String identificadorTablaConsumosXFacturar="interfaz_consumos_x_facturar";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de laboratorio
	 */
	public static final String identificadorTablaLaboratorio="interfaz_laboratorio";
	
	/**
	 * identificador del registro que contiene el nombre real de la tabla para el manejo de ingresos de pacientes en Axioma vs Sistema Anterior
	 */
	public static final String identificadorTablaInterfazProduccionParalelo = "interfaz_paralelo";
	
	/**
	 * Codigo del estado de aborno Procesado. 
	 */
	public static final int codigoEstadoAbonoNoProcesado=0;
	
	/**
	 * Codigo del estado de aborno No Procesado.
	 */
	public static final int codigoEstadoAbonoProcesado=1;
	
	
	
	/**
	 * Codigo del estado de aborno Procesado. 
	 */
	public static final int codigoEstadoNoProcesado=0;
	
	/**
	 * Codigo del estado de aborno No Procesado.
	 */
	public static final int codigoEstadoProcesado=1;
	
	/**
	 * Codigo del estado Procesado Axioma.
	 */
	public static final int codigoEstadoProcesadoAxioma=2;
	
	/**
	 * Codigo del estado Activo del estado ingreso en la tabla interfaz_paciente
	 */
	public static final String codigoEstadoIngresoActivoPaciente = "A";
	
	/**
	 * Codigo del estado Ina|ctivo del estado ingreso en la tabla interfaz_paciente
	 */
	public static final String codigoEstadoIngresoInactivoPaciente = "I";
	
	/**
	 * Codigo del estado de paciente No Procesado. 
	 */
	public static final int codigoEstadoPacienteNoProcesado=0;
	
	/**
	 * Codigo del estado de paciente Procesado.
	 */
	public static final int codigoEstadoPacienteProcesado=1;
	
	/**
	 * Codigo de tipo de transaccion de entrada por compra
	 */
	public static final int codigoEntradaPorCompraInterafaz=1;
	
	/**
	 * Codigo de tipo de transaccion de salida por devolucion de compras
	 */
	public static final int codigoSalidaDevolucionCompraInterfaz=2;
	
	/**
	 * Codigo de tipo de transaccion de entrada por donacion
	 */
	public static final int codigoEntradaDonacionInterfaz=11;
	
	/**
	 * Codigo tipo de transaccion de salida por devolucion de donacion
	 */
	public static final int codigoSalidaDevolucionDonacionInterfaz=12;
	
	/**
	 * 
	 */
	public static final int codigoTipoMovimientoGenerarFactura=1;
	
	/**
	 * 
	 */
	public static final int codigoTipoMovimientoAnularfactura=2;
	
	/**
	 * Variable para comparar el estado del paciente en el sistema anterior -Interfaz en Paralelo
	 */
	public static int pacienteProcesadoSistemaAnterior=2;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR LA VIA INGRESO PARA LA INTERFAZ NUTRICION
	 * ************************************************************************************************/
	
	public static String interfazNutricionHospitalizacion = "HX";
	public static String interfazNutricionUrgenciasObserva = "UO";
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR EL ESTADO DE LA DIETA PARA LA INTERFAZ NUTRICION
	 * ************************************************************************************************/
	
	public static final int estadoDietaActivo=0;
	public static final int estadoDietaSuspendido=1;
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR EL PACIENTE VIP PARA LA INTERFAZ NUTRICION
	 * ************************************************************************************************/
	
	public static String vipConvenioSi = "S";
	public static String vipConvenioNo = "N";
	
	/**************************************************************************************************
	 * 	CONSTATES PARA MANEJAR EL SEXO PACIENTE PARA LA INTERFAZ PACIENTE
	 * ************************************************************************************************/
	
	public static String sexoMasculino = "M";
	public static String sexoFemenino = "F";
	public static String sexoAmbos = "A";
}
