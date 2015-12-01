/**
 * 
 */
package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.PresupuestoCuotasEspecialidadDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoCuotasEspecialidadDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoCuotasEspecialidad;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:24:12 PM
 */
public class PostgresqlPresupuestoCuotasEspecialidadDao implements
		PresupuestoCuotasEspecialidadDao {

	@Override
	public ArrayList<DtoPresupuestoCuotasEspecialidad> cargar(Connection con,
			DtoPresupuestoCuotasEspecialidad dto) {
		return SqlBasePresupuestoCuotasEspecialidadDao.cargar(con, dto);
	}

	@Override
	public boolean eliminar(Connection con,
			BigDecimal codigoPkPresupuestoContratado) {
		return SqlBasePresupuestoCuotasEspecialidadDao.eliminar(con, codigoPkPresupuestoContratado);
	}

	@Override
	public BigDecimal insertar(Connection con,
			DtoPresupuestoCuotasEspecialidad dto) {
		return SqlBasePresupuestoCuotasEspecialidadDao.insertar(con, dto);
	}

	@Override
	public DtoPresupuestoCuotasEspecialidad proponerCargar(
			Connection con, int especialidad, int institucion) {
		return SqlBasePresupuestoCuotasEspecialidadDao.proponerCargar(con, especialidad, institucion);
	}

	@Override
	public ArrayList<Integer> cargarEspecialidadesProgramaPresupuesto(
			Connection con, BigDecimal codigoPkPresupuesto) {
		return SqlBasePresupuestoCuotasEspecialidadDao.cargarEspecialidadesProgramaPresupuesto(con, codigoPkPresupuesto);
	}
}
