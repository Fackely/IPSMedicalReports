/**
 * 
 */
package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.PresupuestoContratadoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoContratadoDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:13:11 PM
 */
public class PostgresqlPresupuestoContratadoDao implements PresupuestoContratadoDao
{

	@Override
	public ArrayList<DtoPresupuestoContratado> cargar(Connection con,
			DtoPresupuestoContratado dto) {
		return SqlBasePresupuestoContratadoDao.cargar(con, dto);
	}

	@Override
	public boolean eliminar(Connection con, DtoPresupuestoContratado dto) {
		return SqlBasePresupuestoContratadoDao.eliminar(con, dto);
	}

	@Override
	public BigDecimal insertar(Connection con, DtoPresupuestoContratado dto) {
		return SqlBasePresupuestoContratadoDao.insertar(con, dto);
	}
	
}
