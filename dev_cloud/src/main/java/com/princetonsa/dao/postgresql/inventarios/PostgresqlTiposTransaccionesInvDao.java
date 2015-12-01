
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.TiposTransaccionesInvDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseTiposTransaccionesInvDao;

/**
 * clase que implementa los metodos 
 * para acceder a la BD de postgresql
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlTiposTransaccionesInvDao implements TiposTransaccionesInvDao 
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
    public HashMap generarConsultaTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz)
    {
        return SqlBaseTiposTransaccionesInvDao.generarConsultaTiposTransaccionesInv(con, codConcepto, codCosto, codigo, descripcion, activo, indicativo_consignacion, institucion, codigoInterfaz);
    }
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
    public boolean generarInsertTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz)
    {
        /**
         * query para insertar tipos de transacciones
         */
        final String insertarTiposTransaccionesInvStr="INSERT INTO " +
															    		"tipos_trans_inventarios " +
															    		"(consecutivo," +
															    		"institucion," +
															    		"tipos_conceptos_inv," +
															    		"tipos_costo_inv," +
															    		"codigo," +
															    		"descripcion," +
															    		"activo, indicativo_consignacion,codigo_interfaz) " +
															    		"VALUES (NEXTVAL('seq_tipos_trans_inventarios'),?,?,?,?,?,?,?,?)";
        return SqlBaseTiposTransaccionesInvDao.generarInsertTiposTransaccionesInv(con, codConcepto, codCosto, codigo, descripcion, activo, indicativo_consignacion, institucion,codigoInterfaz, insertarTiposTransaccionesInvStr);
    }
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
    public boolean generarUpdateTiposTransaccionesInv(Connection con,int consecutivo,String descripcion,String activo,String indicativo_consignacion,String codigoInterfaz)
    {
        return SqlBaseTiposTransaccionesInvDao.generarUpdateTiposTransaccionesInv(con,consecutivo, descripcion, activo,indicativo_consignacion,codigoInterfaz);
    }
    /**
     * metodo para eliminar registros.
     * @param con Connection
     * @param consecutivo int
     * @return boolean
     * @see com.princetonsa.dao.inventarios.sqlbase.SqlBaseTiposTransaccionesInv#generarDeleteTiposTransaccionesInv(java.sql.Connection,int)
     */
    public boolean generarDeleteTiposTransaccionesInv(Connection con, int consecutivo)
    {
        return SqlBaseTiposTransaccionesInvDao.generarDeleteTiposTransaccionesInv(con, consecutivo);
    }
    /**
     * metodo para verifiacar si existen modificaciones.
     * @param con Connection
     * @param consecutivo int     
     * @return ResultSet
     * @see com.princetonsa.dao.inventarios.sqlbase.SqlBaseTiposTransaccionesInv#verificarModificacionesInv(java.sql.Connection,int)
     */
    public ResultSetDecorator verificarModificacionesInv(Connection con,int consecutivo)
    {
        return SqlBaseTiposTransaccionesInvDao.verificarModificacionesInv(con, consecutivo);
    }
}
