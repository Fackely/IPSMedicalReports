package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.salasCirugia.ServiciosViaAccesoDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseServiciosViaAccesoDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class OracleServiciosViaAccesoDao implements ServiciosViaAccesoDao {
	
	private static final String insertarServiciosViaAccesoStr="INSERT INTO servicios_via_acceso (codigo,servicio,fecha_modifica,hora_modifica,usuario_modifica,institucion)" +
																" VALUES (SEQ_SERVICIOS_VIA_ACCESO.NEXTVAL,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";

	public HashMap consultarServiciosViaAcceso(Connection con) 
	{
		return SqlBaseServiciosViaAccesoDao.consultarServiciosViaAcceso(con);
	}

	public boolean eliminarServiciosViaAcceso(Connection con, int codigo) 
	{
		return SqlBaseServiciosViaAccesoDao.eliminarServiciosViaAcceso(con, codigo);
	}

	public boolean insertarServiciosViaAcceso(Connection con,HashMap vo)
	{
		return SqlBaseServiciosViaAccesoDao.insertarServiciosViaAcceso(con, vo, insertarServiciosViaAccesoStr);
	}

}
