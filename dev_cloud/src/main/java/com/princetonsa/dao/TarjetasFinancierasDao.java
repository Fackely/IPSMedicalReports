
/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 *
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface TarjetasFinancierasDao 
{
    /**
     * metodo para insertar los registros
     * de tarjetas financieras
     * @param con Connection     
     */
    public abstract boolean insertarTarjetas(Connection con,HashMap vo);
    
    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @param con
     * @return boolean
     */

    public abstract boolean modificarTarjetas(Connection con,HashMap vo);
    
    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @return boolean
     */
    
    public abstract boolean eliminarTarjetas(Connection con, String consecutivo);    
    /**
     * metodo para consultar los datos de un solo registro
     * @param con Connection
     * @return HashMap
     */
    
    public abstract HashMap consultarInfoTarjetaFinanciera(Connection con,HashMap vo);
    
    /**
     * 
     * @param con
     * @param vo
     * @return
     */
    public abstract HashMap consultarInfoEntidadFinacieranTercero(Connection con);
    
}
