package com.princetonsa.dao.oracle.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.ConsultaImpresionDevolucionDao;
import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseConsultaImpresionDevolucionDao;

public class OracleConsultaImpresionDevolucionDao implements ConsultaImpresionDevolucionDao
{
	public HashMap<String, Object> consultaCajas (Connection con)
	{
		return SqlBaseConsultaImpresionDevolucionDao.consultaCajas(con);
	}
	
	public HashMap<String, Object> consultaMotivos (Connection con)
	{
		return SqlBaseConsultaImpresionDevolucionDao.consultaMotivos(con);
	}
	
	public HashMap<String, Object> consultaDevoluciones (Connection con, String fechaIni, String fechaFin, String devolucionI, String devolucionF, String motivoD, String estadoD, String tipoId, String numeroId, String centroAtencion, String caja)
	{
		return SqlBaseConsultaImpresionDevolucionDao.consultaDevoluciones(con, fechaIni, fechaFin, devolucionI, devolucionF, motivoD, estadoD, tipoId, numeroId, centroAtencion, caja);
	}
	
	public HashMap<String, Object> consultaDetalleD (Connection con, String codigoD)
	{
		return SqlBaseConsultaImpresionDevolucionDao.consultaDetalleD(con, codigoD);
	}
}