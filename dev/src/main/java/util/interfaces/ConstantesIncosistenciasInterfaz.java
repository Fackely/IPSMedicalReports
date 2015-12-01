/*
 * 
 */
package util.interfaces;

/**
 * 
 * @author Jorge Armando Osorio, Archivo en donde se encuentran las constantes de las incosistencias en las interfaces.
 *
 */
public interface ConstantesIncosistenciasInterfaz 
{
	
	
	/*******************************************CODIGOS INCONSISTENCIAS INTERFAZ TESORERIA**********************************************************/
	/**
	 * Codigo para la inconsistencia cuando el codigo de paciente en la tabla de abonos de la interfaz no existe.
	 */
	public static final int codigoCodigoPacienteNull = 10;

	/**
	 * Codigo para la inconsistencia cuando el codigo de paciente en la tabla de abonos de la interfaz  es menor a 0.
	 */
	public static final int codigoCodigoPacienteMenorIgualCero = 11;

	/**
	 * Codigo para la inconsistencia cuando el numero de documento en la tabla de abonos de la interfaz no existe.
	 */
	public static final int codigoNumeroDocumentoNull = 12;
	
	/**
	 * codigo para la incosistencia cuando el numero de documento en la tabla de abonos de la interfaz es menor a 0
	 */
	public static final int codigoNumeroDocumentoMenorIgualCero = 13;

	/**
	 * Codigo para la incosistencia cuando el tipo de movimiento en la tabla de abonos de la interfaz no existe.
	 */
	public static final int codigoTipoMovimientoNull = 14;
	
	/**
	 * Codigo para la incosistencia cuando el tipo de movimiento en la tabla de abonos de la interfaz esta fuera de Rango.
	 */
	public static final int codigoTipoMovimientoFueraRango = 15;

	/**
	 * Codigo para la incosistencia cuando el valor en la tabla de abonos de la interfaz no existe.
	 */
	public static final int codigoValorNull = 16;
	
	/**
	 * Codigo para la incosistencia cuando el articulo cargado con ax_conprov no existe en la maestra de articulos.
	 */
	public static final int articuloNoExiste= 17;
	
	/**
	 * Codigo para la incosistencia cuando el parametro general login de usuario se encuetra vacio.
	 */
	public static final int loginUsuarioNoEspecificado= 18;
	
	
	/**
	 * Codigo para la incosistencia cuando el parametro general login de usuario se encuetra vacio.
	 */
	public static final int loginUsuarioNoExiste= 19;
	
	
	/**
	 * 
	 */
	public static final int fechaCreacionErronea= 20;
	
	/**
	 * 
	 */
	public static final int horaCreacionErronea= 21;

	/**
	 * 
	 */
	public static final int fechaModificacionErronea= 22;
	
	/**
	 * 
	 */
	public static final int horaModificacionErronea= 23;

	/**
	 * El codigo Interfaz del Tercero esta vacio es requerido
	 */
	public static final int terceroVacio=24;
	
	/**
	 * El nombre del Tercero esta vacio es requerido
	 */
	public static final int nombreTerceroVacio=25;
	
	/**
	 * El tercero no existe en Axioma, debe ser creado con anterioridad
	 */
	public static final int terceroNoExiste=26;
	
	/**
	 * La transaccion para la compra de inventario viene Vacia, se debe llenar
	 */
	public static final int transaccionVacia=27;
	
	/**
	 * La transaccion no Existe en Axioma, Se debe crear con Anterioridad
	 */
	public static final int transaccionNoExiste=28;
	
	/**
	 * El campo Numero de Documento Para la Interfaz De compras inventarios esta Vacio, debe tener algun valor
	 */
	public static final int numeroDocumentoCompraVacio=29;
	
	/**
	 * El campo Almacen no posee Informacion para la interfaz de compras
	 */
	public static final int almacenVacio=30;
	
	/**
	 * El campo Almacen no Existe en Axioma, Debe ser creado con Anterioridad
	 */
	public static final int almacenNoExiste=31;
	
	/**
	 * El campo Articulo se encuentra Vacio
	 */
	public static final int articuloVacio=32;
	
	/**
	 * El codigo Interfaz del Tercero Shaio esta vacio es requerido
	 */
	public static final int terceroShaioVacio=33;
	
	/**
	 * El tipo del costo donacion para la interfaz de compra esta vacio
	 */
	public static final int tipoCostoDonacionVacio=34;
	
	/**
	 * El tipo del costo donacion para la interfaz de compra NO es Valido
	 */
	public static final int tipoCostoDonacionNoValido=35;
	
	/**
	 * Codigo de control cuando en Documento existen registros con Inconsistencias, 
	 * se actualiza el estado para que este no sea procesado, debido a que todo el documento
	 * debe quedar en una sola transaccion
	 */
	public static final int grupodeRegistrosConInconsistencia =99;
}
