
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Collection;

import util.ResultadoBoolean;

/**
 * Interface que implementa los metodos 
 * para acceder  a las fuentes de datos
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface RegistroTransaccionesDao 
{

    
    /**
     * @param con
     * @param vo
     */
    public abstract HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo);

    
    /**
     * Metodo para consultar el detalle de
     * articulos de una transacción de inventarios
     * @param con
     * @param codTransaccion
     * @return
     */
    public abstract HashMap consultarDetalleTransaccion(Connection con, String codTransaccion);
    
    /**
     * metodo para insertar la información general
     * de la transacción
     * @param con Connection    
     * @param consecutivo int
     * @param transaccion int
     * @param fechaElaboracion String
     * @param usuario String
     * @param entidad int
     * @param obsevaciones String
     * @param estado int
     * @return boolean
     * @author jarloc
     * @param almacen 
     * @param automatica 
     */
    public abstract boolean insertarInformacionGeneralTransaccion(Connection con,int consecutivo,int transaccion,String fechaElaboracion,String usuario,int entidad,String obsevaciones,int estado, int almacen, boolean automatica);
    
    /**
     * Metodo para consultar el detalle de
     * articulos de una transacción de inventarios
     * por almacen
     * @param con Connection
     * @param codTransaccion int
     * @param almacen int
     * @param institucion int 
     * @return HashMap
     * @author jarloc
     */
    public abstract HashMap consultarDetalleTransaccion(Connection con, String codTransaccion,int almacen,int institucion);
    

    /**
     * 
     * @param con
     * @param codigo
     * @return
     */
    public abstract HashMap existeRegistroTransaccionEnBD(Connection con,int codigo);
    /**
     * metodo para actualizar la información general 
     * de la transacción en caso de ser modificada
     * @param con Connection
     * @param codigo int
     * @param observaciones String
     * @param entidad int
     * @param fechaElaboracion String
     * @return boolean
     * @author jarloc
     */
    public abstract boolean actualizarInformacionGeneral(Connection con,int codigo,String observaciones,int entidad,String fechaElaboracion);
    /**
     * metodo para insertar el detalle de 
     * la transacción
     * @param con Connection
     * @param transaccion int
     * @param articulo int
     * @param cantidad int
     * @param valorUnitario String
     * @return boolean
     * @author jarloc
     * @param fechaVencimiento 
     * @param lote 
     * @param proveedorCatalogo 
     * @param proveedorCompra 
     */
    public abstract boolean insertarDetalleTransaccion(Connection con,int transaccion,int articulo,int cantidad,String valorUnitario, String lote, String fechaVencimiento, String proveedorCompra, String proveedorCatalogo);
    

    /**
     * 
     * @param con
     * @param codigoTrans
     * @param articulo
     * @param lote
     * @param fechaVencimiento
     * @return
     */
    public abstract HashMap existeDetalleTransaccionEnBD(Connection con,String codigoDetalle);

    /**
     * metodo para actualizar la información
     * del detalle de la transacción
     * @param con Connection
     * @param codigoTrans int
     * @param articulo int
     * @param cantidad int
     * @param valorUnitario String
     * @return boolean
     * @author jarloc
     * @param fechaVencimiento 
     * @param lote 
     */
    public abstract boolean actualizarDetalleTransaccion(Connection con,int codigo,int cantidad,String valorUnitario,String lote,String fechaVencimiento);

    /**
     * metodo para obtener el ultimo codigo
     * de la transacción insertada
     * @param con Connection
     * @return int
     * @author jarloc
     */
    public abstract int obtnerCodigoTransaccionInsertada(Connection con);
    /**
     * metodo para actualizar el estado de la
     * transacción
     * @param con Connection
     * @param estado int
     * @param codigoTransaccion int
     * @return boolean
     * @author jarloc
     */
    public abstract boolean actualizarEstadoTransaccion(Connection con,int estado,int codigoTransaccion);
    /**
     * metodo para generar el registro de ajustes,
     * al costo promedio
     * @param con Connection
     * @param codigo String
     * @param articulo int
     * @param almacen int
     * @param tipoTransaccion int
     * @param transaccion String
     * @param vlrCostoAntes double
     * @param vlrCostoDespues double
     * @param tipoCosto int
     * @return boolean
     * @author jarloc
     */
    public abstract boolean generarRegistroAjuste(Connection con,String codigo,int articulo,int almacen,int tipoTransaccion,String transaccion,double vlrCostoAntes,double vlrCostoDespues,int tipoCosto);
    /**
     * metodo para insertar el registro del 
     * cierre de la transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String
     * @param fecha String
     * @param hora String
     * @return boolean
     */
    public abstract boolean generarRegistroCierreTransaccion(Connection con,String transaccion,String usuario,String fecha,String hora);
    /**
     * metodo para generar la anulación de la
     * transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String 
     * @param motivo String
     * @return boolean
     */
    public abstract boolean generarAnulacionTransaccion(Connection con,String transaccion,String usuario,String motivo);
    /**
     * Metodo para verificar si existe alguna
     * modificación en la información general
     * de una transacción
     * @param con Connection
     * @param vo HashMap, con los datos a verificar
     * @return ResultadoBoolean, true si existe modificación
     * @author jarloc
     */
    public abstract ResultadoBoolean existeModificacionInfoGeneral(Connection con,HashMap vo);
    
    
    /**
     * 
     * @param con
     * @param codigo
     * @return
     */
    public boolean eliminarDetalleTransaccion(Connection con, int codigo);
	
	/**
     * obtiene los nombres para adicionarlos a la consulta de birt
     * @param con
     * @param mapaNombresYRestricciones
     * @return
     */
    public abstract HashMap getNombresReporteBirt(Connection con, HashMap mapaNombresYRestricciones) ;
    
    /**
     * Busquedad de entidad
     * @param con
     * @param codigoEntidad
     * @param decripcionEntidad
     * @return
     */
    public Collection buscarEntidad(Connection con, String codigoEntidad, String descripcionEntidad);
    
    /**
	 * Método encargado de consultar los tipos de transacción dado un condigo de transacción
	 * @param con
	 * @param codTransaccion
	 * @return HashMap
	 */
	public HashMap consultaTipoTransaccion(Connection con, String codTransaccion);


	/**
	 * 
	 * @param con
	 * @param codigoPKTransaccion
	 * @param conversionFormatoFechaABD
	 * @param entidad
	 * @param observaciones
	 * @return
	 */
	public abstract boolean actualizarDatosBasicos(Connection con,int codigoPKTransaccion, String fecha,int entidad, String observaciones);
    
}
