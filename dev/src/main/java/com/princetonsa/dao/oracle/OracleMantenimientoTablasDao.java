/*
 * Created on Apr 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;


import com.princetonsa.dao.MantenimientoTablasDao;
import com.princetonsa.dao.sqlbase.SqlBaseMantenimientoTablasDao;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public  class OracleMantenimientoTablasDao implements MantenimientoTablasDao {
	
	/**	 *	 */
	public HashMap getDatosTabla(Connection con, String consulta)throws SQLException{
		return SqlBaseMantenimientoTablasDao.getDatosTabla(con,consulta);
	}
	
	
	/**	 *	 */
	public int updateDatosTabla(Connection con, String consulta)throws SQLException{
		return SqlBaseMantenimientoTablasDao.updateDatosTabla(con,consulta);
	}


	/**	 *	 */
	public int buscarDatosTabla(Connection con, String consulta)throws SQLException{
		return SqlBaseMantenimientoTablasDao.buscarDatosTabla(con,consulta);
	}
	
	
	/**	 *	 */
	public boolean existeEspecialidad(Connection con, String nombreEspecialidad) {
		return SqlBaseMantenimientoTablasDao.existeEspecialidad(con, nombreEspecialidad);
	}


	@Override
	public boolean existeExcepcionAgenda(Connection con, String fecha,	String centroAtencion) {
		
		return SqlBaseMantenimientoTablasDao.existeExcepcionAgenda(con,fecha,centroAtencion);
	}


	@Override
	public boolean existeInterconsultaPermitida(Connection con,
			String ocupacion, String especialidad) {
		
		return SqlBaseMantenimientoTablasDao.existeInterconsultaPermitida(con, ocupacion, especialidad);
	}


	@Override
	public boolean existeOcupacionMedica(Connection con, String nombreOcupacion) {
		
		return SqlBaseMantenimientoTablasDao.existeOcupacionMedica(con,nombreOcupacion);
	}


	
}