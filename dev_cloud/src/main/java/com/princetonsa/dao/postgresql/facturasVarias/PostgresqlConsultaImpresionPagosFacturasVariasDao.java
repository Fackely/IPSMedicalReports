package com.princetonsa.dao.postgresql.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.ConsultaImpresionPagosFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConsultaImpresionPagosFacturasVariasDao;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class PostgresqlConsultaImpresionPagosFacturasVariasDao implements ConsultaImpresionPagosFacturasVariasDao
{
	
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap buscarPagosFacturasVarias(Connection con, int codigoInstitucionInt, HashMap criterios)
    {
        return SqlBaseConsultaImpresionPagosFacturasVariasDao.buscarPagosFacturasVarias(con, codigoInstitucionInt, criterios);
    }
	
	/**
	 * 
	 */
	public HashMap buscarConceptos(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		return SqlBaseConsultaImpresionPagosFacturasVariasDao.buscarConceptos(con, codigoInstitucionInt, codigoAplicacion);
	}
	
	/**
	 * 
	 */
	public HashMap buscarFacturas(Connection con, int codigoInstitucionInt, int codigoAplicacion)
	{
		return SqlBaseConsultaImpresionPagosFacturasVariasDao.buscarFacturas(con, codigoInstitucionInt, codigoAplicacion);
	}
}
