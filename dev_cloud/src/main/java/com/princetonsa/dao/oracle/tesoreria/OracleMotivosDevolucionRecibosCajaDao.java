package com.princetonsa.dao.oracle.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.MotivosDevolucionRecibosCajaDao;
import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseMotivosDevolucionRecibosCajaDao;
import com.princetonsa.mundo.tesoreria.MotivosDevolucionRecibosCaja;

public class OracleMotivosDevolucionRecibosCajaDao implements MotivosDevolucionRecibosCajaDao
{
	/**
	 * Metodo de consulta de los motivos devoluciones recibos de caja
	 * @param con
	 * @param codigoInst
	 * @return
	 */
	public HashMap<String, Object> consultaMotivosD (Connection con, int codigoInstitucion)
	{
		return SqlBaseMotivosDevolucionRecibosCajaDao.consultaMotivosD(con, codigoInstitucion);
	}
	
	public boolean insertarMotivosD(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		return SqlBaseMotivosDevolucionRecibosCajaDao.insertar(con, motivosDevolucionRecibosCaja);
	}
	
	public boolean modificarMotivosD(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		return SqlBaseMotivosDevolucionRecibosCajaDao.modificar(con, motivosDevolucionRecibosCaja);
	}
	
	public boolean eliminarMotivosD(Connection con, String motivoD){
		return SqlBaseMotivosDevolucionRecibosCajaDao.eliminar(con, motivoD);
	}
}