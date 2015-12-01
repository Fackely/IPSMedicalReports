
/*
 * Creado   6/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package util.inventarios;

/**
 * Constantes para el modulo de inventarios
 *
 * @version 1.0, 6/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface ConstantesBDInventarios 
{
    /************************************************************************************
	 *  CONSTANTES CONSECUTIVOS DISPONIBLES CASO ESPECIAL INVENTARIOS
	 ************************************************************************************/
    public static String codigoConsecutivoTransXAlmacen="dummy_trans_inv_x_almacen";
    public static String codigoConsecutivoTransUnicoSistema="dummy_trans_inv_unico_sistema";
    
    /*************************************************************************************
     *    MANEJO DE CONSECUTIVOS EN LAS TRANSACCIONES DE INVENTARIOS
     *************************************************************************************/
    public static String valorConsecutivoTransInventariosPorAlmacen="Por Almacen";
    public static String valorConsecutivoTransInventariosUnicoSistema="Unico en el Sistema";
    
    /*************************************************************************************
     *    TIPOS COSTO INVENTARIO [tabla->tipos_costo_inv]
     *************************************************************************************/
    public static int codigoTipoCostoInventarioAutomatico=1;
    public static int codigoTipoCostoInventarioManual=2;
    
    /*************************************************************************************
     *    ESTADOS TRANSACCIONES INVENTARIO [tabla->estados_trans_inv]
     *************************************************************************************/
    public static int codigoEstadoTransaccionInventarioPendiente=1;
    public static int codigoEstadoTransaccionInventarioCerrada=2;
    public static int codigoEstadoTransaccionInventarioAnulada=3;
    
    /*************************************************************************************
     *    NOMBRES ESTADOS TRANSACCIONES INVENTARIO [tabla->estados_trans_inv]
     *************************************************************************************/
    public static String nombreEstadoTransaccionInventarioPendiente="Pendiente";
    public static String nombreEstadoTransaccionInventarioCerrada="Cerrada";
    public static String nombreEstadoTransaccionInventarioAnulada="Anulada";
    
    /*************************************************************************************
     *    TIPOS CIERRES INVENTARIOS
     *************************************************************************************/
    public static int tipoCierreAnual=1;
    public static int tipoCierreInicial=2;
    public static int tipoCierreMensual=3;
    
    /*************************************************************************************
     *    TIPOS CONCEPTOS INVENTARIOS [tabla->tipos_conceptos_inv]
     *************************************************************************************/
    /**código tipo concepto inventarios salida [tabla->tipos_conceptos_inv]*/
    public static int codigoTipoConceptoInventarioSalida=2;
    /**código tipo concepto inventarios entrada [tabla->tipos_conceptos_inv]*/
    public static int codigoTipoConceptoInventarioEntrada=1;
    /*************************************************************************************
     *		TIPOS AJUSTES COSTO INVENTARIOS [tabla->tipos_ajuste_costo_inv]      
     *************************************************************************************/
    /**código tipo ajuste costo inventarios automatico [tabla->tipos_ajuste_costo_inv]*/
    public static int codigoTipoAjusteInventarioAutomatico=1;
    /**código tipo ajuste costo inventarios manual [tabla->tipos_ajuste_costo_inv]*/
    public static int codigoTipoAjusteInventarioManual=2;
    /*************************************************************************************
     *    NOMBRES ESTADOS TRASLADOS ALMACEN INVENTARIO [tabla->estados_traslado_almacen]
     *************************************************************************************/
    public static int codigoEstadoTrasladoInventarioPendiente=1;
    public static int codigoEstadoTrasladoInventarioCerrada=2;
    public static int codigoEstadoTrasladoInventarioAnulada=3;
    public static int codigoEstadoTrasladoInventarioDespachada=4;
    /*************************************************************************************
     *    NOMBRES CONSECUTIVOS INVENTARIOS [tabla->consecutivos]
     *************************************************************************************/
    public static String nombreConsecutivoInventarioTrasladoAlmacen="consecutivo_traslados_almacenes";
    
}
