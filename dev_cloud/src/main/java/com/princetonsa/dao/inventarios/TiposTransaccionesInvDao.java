
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * Interfaz para acceder a la fuente de datos de 
 * tipos de transacciones inventarios
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface TiposTransaccionesInvDao 
{
    /**
     * metodo para realizar la consulta/busqueda avanzada 
     * de registros tipos transacciones inventario 
     * @param con Connection 
     * @param codConcepto int 
     * @param codCosto int 
     * @param codigo String
     * @param descripcion String
     * @param activo String
     * @param institucion int 
     * @return HashMap
     * @see com.princetonsa.dao.sqlbase.inventarios.SqlBaseTiposTransaccionesInv#generarConsultaTiposTransaccionesInv(java.sql.Connection,int,int,String,String,String,int)
     */
    public HashMap generarConsultaTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz);
    /**
     * metodo para insertar registros de tipos de 
     * transacciones inventario 
     * @param con Connection 
     * @param codConcepto int
     * @param codCosto int
     * @param codigo String
     * @param descripcion String
     * @param activo String
     * @param institucion int
     * @return boolean
     * @see com.princetonsa.dao.sqlbase.inventarios.SqlBaseTiposTransaccionesInv#generarActualizacionTiposTransaccionesInv(java.sql.Connection,int,int,String,String,String,int)
     */
    public boolean generarInsertTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz);
    /**
     * metodo para generar la modificación 
     * de registros.
     * @param con Connection     
     * @param consecutivo int
     * @param descripcion String
     * @param activo String    
     * @return boolean
     * @see com.princetonsa.dao.sqlbase.inventarios.SqlbaseTiposTransaccionesInv#generarUpdateTiposTransaccionesInv(java.sql.Connection,int,String,String)
     */
    public boolean generarUpdateTiposTransaccionesInv(Connection con,int consecutivo,String descripcion,String activo,String indicativo_consignacion,String codigoInterfaz);
    /**
     * metodo para eliminar registros.
     * @param con Connection
     * @param consecutivo int
     * @return boolean
     * @see com.princetonsa.dao.inventarios.sqlbase.SqlBaseTiposTransaccionesInv#generarDeleteTiposTransaccionesInv(java.sql.Connection,int)
     */
    public boolean generarDeleteTiposTransaccionesInv(Connection con, int consecutivo);
    /**
     * metodo para verifiacar si existen modificaciones.
     * @param con Connection
     * @param consecutivo int     
     * @return ResultSet
     * @see com.princetonsa.dao.inventarios.sqlbase.SqlBaseTiposTransaccionesInv#verificarModificacionesInv(java.sql.Connection,int)
     */
    public ResultSetDecorator verificarModificacionesInv(Connection con,int consecutivo);

}
