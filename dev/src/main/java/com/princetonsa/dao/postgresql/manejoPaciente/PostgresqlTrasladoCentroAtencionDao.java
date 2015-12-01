/*
 * Sep 28,06
 */
package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.manejoPaciente.TrasladoCentroAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTrasladoCentroAtencionDao;

/**
 * @author Sebasti�n G�mez 
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Traslado Centro de Atenci�n
 */
public class PostgresqlTrasladoCentroAtencionDao implements
		TrasladoCentroAtencionDao 
{
	/**
	 * M�todo implementado para realizar las validaciones de ingreso
	 * a la funcionalidad Traslado Centro Atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public Vector validaciones(Connection con,HashMap campos)
	{
		return SqlBaseTrasladoCentroAtencionDao.validaciones(con,campos);
	}
	
	/**
	 * M�todo que desasigna la cama de una admision de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int desasignarCamaUrgencias(Connection con,String idCuenta)
	{
		return SqlBaseTrasladoCentroAtencionDao.desasignarCamaUrgencias(con,idCuenta);
	}
	
	/**
	 * M�todo implementado para realizar el traslado por centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public int realizarTraslado(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_tras_centro_atencion')");
		campos.put("secuenciaTratante","nextval('seq_tratantes_cuenta')");
		return SqlBaseTrasladoCentroAtencionDao.realizarTraslado(con,campos);
	}
}
