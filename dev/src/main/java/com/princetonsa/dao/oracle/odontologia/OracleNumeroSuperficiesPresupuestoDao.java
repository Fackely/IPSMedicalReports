/**
 * 
 */
package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoNumSuperficiesPresupuesto;

import com.princetonsa.dao.odontologia.NumeroSuperficiesPresupuestoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseNumeroSuperficiesPresupuestoDao;
import com.princetonsa.dto.odontologia.DtoDetallePresupuestoPlanNumSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;

/**
 * 
 * @author Wilson Rios 
 *
 * May 7, 2010 - 2:04:40 PM
 */
public class OracleNumeroSuperficiesPresupuestoDao implements
		NumeroSuperficiesPresupuestoDao {

	@Override
	public ArrayList<DtoDetallePresupuestoPlanNumSuperficies> busquedaDetalle(
			Connection con, DtoDetallePresupuestoPlanNumSuperficies dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.busquedaDetalle(con, dto);
	}

	@Override
	public ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> busquedaEncabezado(
			Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.busquedaEncabezado(con, dto);
	}

	@Override
	public boolean eliminarDetalleXEncabezado(Connection con,
			BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.eliminarDetalleXEncabezado(con, codigoEncabezadoPresuPlanTtoNumSuperficies);
	}

	@Override
	public boolean eliminarEncabezado(Connection con, BigDecimal codigoPk) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.eliminarEncabezado(con, codigoPk);
	}

	@Override
	public boolean insertarDetalle(Connection con,
			DtoDetallePresupuestoPlanNumSuperficies dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.insertarDetalle(con, dto);
	}

	@Override
	public BigDecimal insertarEncabezado(Connection con,
			DtoPresupuestoPlanTratamientoNumeroSuperficies dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.insertarEncabezado(con, dto);
	}

	@Override
	public BigDecimal obtenerCodigoPresuPlanTtoProgSer(Connection con,
			DtoPlanTratamientoPresupuesto dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.obtenerCodigoPresuPlanTtoProgSer(con, dto);
	}

	@Override
	public ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPresupuesto(
			Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto,
			int codigoSuperficie) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.obtenerInfoNumSuperficiesPresupuesto(con, dto, codigoSuperficie);
	}

	@Override
	public ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPlanTratamiento(
			Connection con, DtoPlanTratamientoNumeroSuperficies dto,
			int codigoSuperficie) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.obtenerInfoNumSuperficiesPlanTratamiento(con, dto, codigoSuperficie);
	}

	@Override
	public DtoSuperficiesPorPrograma obtenerSuperficieXProgramaPlanTratamiento(
			Connection con, DtoSuperficiesPorPrograma dtoBusqueda) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.obtenerSuperficieXProgramaPlanTratamiento(con, dtoBusqueda);
	}

	@Override
	public DtoProgHallazgoPieza obtenerProgramaHallazgoPiezaPlanTratamiento(
			Connection con, DtoProgHallazgoPieza dtoBusqueda) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.obtenerProgramaHallazgoPiezaPlanTratamiento(con, dtoBusqueda);
	}

	@Override
	public boolean guardarSuperficiesPlanTratamiento(Connection con,
			DtoSuperficiesPorPrograma dto) {
		return SqlBaseNumeroSuperficiesPresupuestoDao.guardarSuperficiesPlanTratamiento(con, dto);
	}

}
