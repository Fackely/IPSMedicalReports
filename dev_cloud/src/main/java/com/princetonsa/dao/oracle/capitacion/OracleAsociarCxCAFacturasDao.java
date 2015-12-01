package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.capitacion.AsociarCxCAFacturasDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseAsociarCxCAFacturasDao;

public class OracleAsociarCxCAFacturasDao implements AsociarCxCAFacturasDao 
{
	/**
	 * busqueda de las cuentas de cobro a asociar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException
	 */	
	public HashMap busquedaCuentasCobroAAsociar(	Connection con,
													HashMap criteriosBusquedaMap
												 )throws SQLException
	{
		return SqlBaseAsociarCxCAFacturasDao.busquedaCuentasCobroAAsociar(con, criteriosBusquedaMap);
	}
	
	/**
	 * insertar
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoConvenio
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	public boolean insertar (	Connection con, String numeroCuentaCobro, String contabilizado,
								int codigoConvenio, String loginUsuario,
								int institucion, int cantidadFacturasAsociadas)
	{
		return SqlBaseAsociarCxCAFacturasDao.insertar(con, numeroCuentaCobro, contabilizado, codigoConvenio, loginUsuario, institucion, cantidadFacturasAsociadas);
	}
	
	/**
	 * selecciona las facturas a asociar a una cuenta de cobro, recibe un vector con las facturas ya seleccionadas para que
	 * no existan problemas con n cuentas de cobro que tengan la misma factura, es decir, se asigna al primero que llegue
	 * @param con
	 * @param fechaInicialCuentaCobro
	 * @param fechaFinalCuentaCobro
	 * @param codigoConvenio
	 * @param facturasYaSelecionadasVector
	 * @return
	 */
	public HashMap seleccionFacturasAAsociar(	Connection con , String fechaInicialCuentaCobro, String fechaFinalCuentaCobro, int codigoConvenio, Vector facturasYaSelecionadasVector)
	{
		return SqlBaseAsociarCxCAFacturasDao.seleccionFacturasAAsociar(con, fechaInicialCuentaCobro, fechaFinalCuentaCobro, codigoConvenio, facturasYaSelecionadasVector);
	}
}
