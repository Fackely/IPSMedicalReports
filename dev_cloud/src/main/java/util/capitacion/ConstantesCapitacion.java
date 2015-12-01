package util.capitacion;

public interface ConstantesCapitacion {

	/**=============TIPOS DE CONSULTA ORDENES CAPITACION=========================================*/
	public static final int codigoTipoConsultaNivel = 1;
	public static final int codigoTipoConsultaConvenioContrato = 2;
	public static final int codigoTipoConsultaGrupoSClaseI = 3;
	
	public static final String reporteNivel = "ReportePorNivelAtencion";
	public static final String reporteConvenioContrato = "ReportePorConvenioContrato";;
	public static final String reporteGrupoClase = "ReportePorGrupoClase";
	public static final String reporteValorizados = "ReporteSevArtValorizados";
	
	/**
	 * Cadena constante para determinar el valor Grupo de Servicio 
	 */
	public static final String grupoServicios = "Grupo de Servicios";
	
	/**
	 * Cadena constante para determinar que el valor Clase de Inventario
	 */
	public static final String claseInventario = "Clases de Inventario";
	
	/**
	 * Cadena constante para determinar el valor Servicios
	 */
	public static final String servicios = "Grupo de Servicios";
	
	/**
	 * Cadena constante para determinar que el valor Clase de Inventario
	 */
	public static final String medicamentos = "Med. / Insumos";
	
	/**
	 * Cadena constante para determinar que el valor General
	 */
	public static final String general = "General";
	
	/**
	 * Cadena constante para determinar que el valor Porcentaje General
	 */
	public static final String porcentajeGeneral = "Porcentaje General";
	
	/**
	 * Cadena constante para determinar que el valor Porcentaje General
	 */
	public static final String porcentajeGastoMensual = "Porcentaje Gasto Mensual";
	
	/**
	 * Cadena constante para determinar que el valor de la columna sub_seccion 
	 * de la tabla valorizacion_param_presup_gen es de tipo servicio 
	 */
	public static final String subSeccionServicio = "servicio";
	
	/**
	 * Cadena constante para determinar que el valor de la columna sub_seccion 
	 * de la tabla valorizacion_param_presup_gen es de tipo articulo 
	 */
	public static final String subSeccionArticulo = "articulo";
	
	/**=============CODIGOS VALDIACION PRESUPUESTO CAPITACION=========================================*/
	public static final int codigoValPresupuestoNoParametrizacion = 1;
	public static final int codigoValPresupuestoNoCierre = 2;
	public static final int codigoValPresupuestoSobrepasaTope = 3;
	public static final int codigoValPresupuestoNoCierreAutorizaciones = 4;
	public static final int codigoValPresupuestoNoNivelAtencion = 5;
	
	
	/**=============Nombres Funcionalidades que se integran=========================================*/
	public static final String INTEGRACION_ORDENES_PACIENTE="ordenesPorPaciente";
	
	
}
