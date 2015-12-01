
/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.TarjetasFinancierasDao;
import com.princetonsa.dao.sqlbase.SqlBaseTarjetasFinancierasDao;

/**
 * 
 *
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlTarjetasFinancierasDao implements TarjetasFinancierasDao 
{
	
    /**
     * metodo para consultar los datos de un solo registro
     * @param con Connection
     * @param consecutivo int
     * @return HashMap
     */
	public HashMap consultarInfoTarjetaFinanciera(Connection con, HashMap vo)
	{
		return SqlBaseTarjetasFinancierasDao.consultarInfoTarjetaFinanciera(con,vo);
	}
	
    /**
     * metodo para insertar los registros
     * de tarjetas financieras
     * @param con Connection     
     * @return boolean
     * @author jarloc
     */
	public boolean insertarTarjetas(Connection con, HashMap vo)
	{
		
        String cadena="INSERT INTO tarjetas_financieras" +
		"(consecutivo, " +
		"codigo," +
		"entidad_financiera," +
		"descripcion," +
		"tipo_tarjeta_financiera," +
		"base_rete," +
		"retefte," +
		"reteica," +
		"comision," +
		"directo_banco," +
		"activo,institucion) values(NEXTVAL('seq_tarjetas_financieras'), ?,?,?,?,?,?,?,?,?,?,?)"; 
		
        
		return SqlBaseTarjetasFinancierasDao.insertarTarjetas(con,vo,cadena);
	}
	
    
    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @param con
     * @return boolean
     */
	
	public boolean modificarTarjetas(Connection con, HashMap vo)
	{
		return SqlBaseTarjetasFinancierasDao.modificarTarjetas(con,vo);
	}	
    
    /**
     * metodo para eliminar un registro de una
     * tarjeta financiera
     * @param con
     * @return boolean
     */
	public boolean eliminarTarjetas(Connection con, String consecutivo)
	{
		return SqlBaseTarjetasFinancierasDao.eliminarTarjetas(con,consecutivo);
	}	

	/**
	 * metodo para llenar el combo de Entidades Financieras- Tercero
	 */
	public  HashMap consultarInfoEntidadFinacieranTercero(Connection con)
	{
		return SqlBaseTarjetasFinancierasDao.consultarInfoEntidadFinacieranTercero(con);
	}
}
