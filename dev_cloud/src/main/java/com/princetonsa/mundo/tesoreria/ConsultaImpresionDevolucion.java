package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.ConsultaImpresionDevolucionDao;
import com.princetonsa.dao.DaoFactory;

public class ConsultaImpresionDevolucion
{
	private static Logger logger = Logger.getLogger(ConsultaImpresionDevolucion.class);
	
	private static ConsultaImpresionDevolucionDao getConsultaImpresionDevolucionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaImpresionDevolucionDao();		
	}
	
	public static HashMap<String, Object> consultaCajas (Connection con)
	{
		return getConsultaImpresionDevolucionDao().consultaCajas(con);
	}
	
	public static HashMap<String, Object> consultaMotivos (Connection con)
	{
		return getConsultaImpresionDevolucionDao().consultaMotivos(con);
	}
	
	public static HashMap<String, Object> consultaDevoluciones (Connection con, String fechaIni, String fechaFin, String devolucionI, String devolucionF, String motivoD, String estadoD, String tipoId, String numeroId, String centroAtencion, String caja)
	{
		return getConsultaImpresionDevolucionDao().consultaDevoluciones(con, fechaIni, fechaFin, devolucionI, devolucionF, motivoD, estadoD, tipoId, numeroId, centroAtencion, caja);
	}
	
	public static HashMap<String, Object> consultaDetalleD (Connection con, String codigoD)
	{
		return getConsultaImpresionDevolucionDao().consultaDetalleD(con, codigoD);
	}
}