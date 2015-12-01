package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.tesoreria.MotivosDevolucionRecibosCaja;

public interface MotivosDevolucionRecibosCajaDao
{
	/**
	 * Metodo de consulta de los motivos devoluciones recibos de caja
	 * @param con
	 * @param codigoInst
	 * @return
	 */
	public HashMap<String, Object> consultaMotivosD (Connection con, int codigoInstitucion);
	
	/**
	 * 
	 */
	public boolean insertarMotivosD(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja);
	
	/**
	 * 
	 */
	public boolean modificarMotivosD(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja);
	
	/**
	 * 
	 */
	public boolean eliminarMotivosD(Connection con, String motivoD);
}