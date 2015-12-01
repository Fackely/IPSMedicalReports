
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.inventarios.RegistroTransaccionesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseRegistroTransaccionesDao;

/**
 * Clase que implementa los metodos propios de la
 * BD Oracle
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class OracleRegistroTransaccionesDao implements RegistroTransaccionesDao 
{
    /**
     * @param con
     * @param vo
     */
    public HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo)
    {
        return SqlBaseRegistroTransaccionesDao.ejecutarBusquedaAvanzada(con,vo);
    }
    
    
    /**
     * @param con
     * @param codTransaccion
     * @return
     */
    public HashMap consultarDetalleTransaccion(Connection con, String codTransaccion)
    {
        return SqlBaseRegistroTransaccionesDao.consultarDetalleTransaccion(con,codTransaccion);
    }
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
     */
    public boolean insertarInformacionGeneralTransaccion(Connection con,int consecutivo,int transaccion,String fechaElaboracion,String usuario,int entidad,String obsevaciones,int estado, int almacen,boolean automatica)
    {
        /**
         * query para ingresar el encabezado de información general
         */
        final String insertarRegistroTransaccionesStr=" INSERT INTO " +
    			    																"transacciones_x_almacen " +
    																	    		"(codigo,consecutivo," +
    																	    		"transaccion,fecha_elaboracion," +
    																	    		"usuario,entidad,observaciones,estado,almacen,automatica) " +
    																	    		"VALUES (seq_transacciones_x_almacen.NEXTVAL,?,?,?,?,?,?,?,?,?)";
        return SqlBaseRegistroTransaccionesDao.insertarInformacionGeneralTransaccion(con, insertarRegistroTransaccionesStr, consecutivo, transaccion, fechaElaboracion, usuario, entidad, obsevaciones, estado, almacen,automatica); 
    }
    
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
    public HashMap consultarDetalleTransaccion(Connection con, String codTransaccion,int almacen,int institucion)
    {
        return SqlBaseRegistroTransaccionesDao.consultarDetalleTransaccion(con, codTransaccion, almacen, institucion); 
    }
    /**
     * metodo para verificar si un registro
     * posee entrda en la BD
     * @param con Connection
     * @param codigo int
     * @return HashMap
     * @author jarloc
     */
    public HashMap existeRegistroTransaccionEnBD(Connection con,int codigo)
    {
        return SqlBaseRegistroTransaccionesDao.existeRegistroTransaccionEnBD(con, codigo);
    }
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
    public boolean actualizarInformacionGeneral(Connection con,int codigo,String observaciones,int entidad,String fechaElaboracion)
    {
        return SqlBaseRegistroTransaccionesDao.actualizarInformacionGeneral(con, codigo, observaciones, entidad, fechaElaboracion); 
    }
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
     */
    public boolean insertarDetalleTransaccion(Connection con,int transaccion,int articulo,int cantidad,String valorUnitario, String lote, String fechaVencimiento, String proveedorCompra, String proveedorCatalogo)
    {
        return SqlBaseRegistroTransaccionesDao.insertarDetalleTransaccion(con, transaccion, articulo, cantidad, valorUnitario,lote,fechaVencimiento,proveedorCompra,proveedorCatalogo);
    }
    /**
     * metodo para insertar el detalle de la
     * transacción
     * @param con Connection
     * @param codigoTrans int
     * @param articulo int
     * @return HashMap
     * @author jarloc
     */
    public HashMap existeDetalleTransaccionEnBD(Connection con,String codigoDetalle)
    {
        return SqlBaseRegistroTransaccionesDao.existeDetalleTransaccionEnBD(con, codigoDetalle);
    }
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
     */
    public boolean actualizarDetalleTransaccion(Connection con,int codigo,int cantidad,String valorUnitario,String lote,String fechaVencimiento)
    {
        return SqlBaseRegistroTransaccionesDao.actualizarDetalleTransaccion(con, codigo, cantidad, valorUnitario,lote,fechaVencimiento);
    } 
    /**
     * metodo para obtener el ultimo codigo
     * de la transacción insertada
     * @param con Connection
     * @return int
     * @author jarloc
     */
    public int obtnerCodigoTransaccionInsertada(Connection con)
    {
        return SqlBaseRegistroTransaccionesDao.obtnerCodigoTransaccionInsertada(con);   
    }
    /**
     * metodo para actualizar el estado de la
     * transacción
     * @param con Connection
     * @param estado int
     * @param codigoTransaccion int
     * @return boolean
     * @author jarloc
     */
    public boolean actualizarEstadoTransaccion(Connection con,int estado,int codigoTransaccion)
    {
        return SqlBaseRegistroTransaccionesDao.actualizarEstadoTransaccion(con, estado, codigoTransaccion);  
    }
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
    public boolean generarRegistroAjuste(Connection con,String codigo,int articulo,int almacen,int tipoTransaccion,String transaccion,double vlrCostoAntes,double vlrCostoDespues,int tipoCosto)
    {
        return SqlBaseRegistroTransaccionesDao.generarRegistroAjuste(con, codigo, articulo, almacen, tipoTransaccion, transaccion, vlrCostoAntes, vlrCostoDespues, tipoCosto); 
    }
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
    public boolean generarRegistroCierreTransaccion(Connection con,String transaccion,String usuario,String fecha,String hora)
    {
    	return SqlBaseRegistroTransaccionesDao.generarRegistroCierreTransaccion(con, transaccion, usuario, fecha, hora);
    }
    /**
     * metodo para generar la anulación de la
     * transacción
     * @param con Connection
     * @param transaccion String
     * @param usuario String 
     * @param motivo String
     * @return boolean
     */
    public boolean generarAnulacionTransaccion(Connection con,String transaccion,String usuario,String motivo)
    {
    	return SqlBaseRegistroTransaccionesDao.generarAnulacionTransaccion(con, transaccion, usuario, motivo);
    }
    /**
     * Metodo para verificar si existe alguna
     * modificación en la información general
     * de una transacción
     * @param con Connection
     * @param vo HashMap, con los datos a verificar
     * @return ResultadoBoolean, true si existe modificación
     * @author jarloc
     */
    public ResultadoBoolean existeModificacionInfoGeneral(Connection con,HashMap vo)
    {
        return SqlBaseRegistroTransaccionesDao.existeModificacionInfoGeneral(con, vo);   
    }
    
    /**
     * 
     * @param con
     * @param transaccion
     * @param codArt
     * @return
     */
	public boolean eliminarDetalleTransaccion(Connection con, int codigo)
	{
		return SqlBaseRegistroTransaccionesDao.eliminarDetalleTransaccion(con,codigo);
	}
	
	/**
     * obtiene los nombres para adicionarlos a la consulta de birt
     * @param con
     * @param mapaNombresYRestricciones
     * @return
     */
    public HashMap getNombresReporteBirt(Connection con, HashMap mapaNombresYRestricciones) 
    {
    	return SqlBaseRegistroTransaccionesDao.getNombresReporteBirt(con, mapaNombresYRestricciones);
    }
    
    /**
     * Busqueda de entidad
     * @param con
     * @param codigoEntidad
     * @param descripcionEntidad
     * @return
     */
    public Collection buscarEntidad(Connection con, String codigoEntidad, String descripcionEntidad)
    {
    	return SqlBaseRegistroTransaccionesDao.buscarEntidad(con, codigoEntidad, descripcionEntidad);
    }
    
    /**
	 * Método encargado de consultar los tipos de transacción dado un condigo de transacción
	 * @param con
	 * @param codTransaccion
	 * @return HashMap
	 */
	public HashMap consultaTipoTransaccion(Connection con, String codTransaccion)
    {
    	return SqlBaseRegistroTransaccionesDao.consultaTipoTransaccion(con, codTransaccion);
    }
	
	@Override
	public boolean actualizarDatosBasicos(Connection con,int codigoPKTransaccion, String fecha,int entidad, String observaciones) 
	{
		return SqlBaseRegistroTransaccionesDao.actualizarInformacionGeneral(con, codigoPKTransaccion,observaciones,entidad,fecha);
	}
}
