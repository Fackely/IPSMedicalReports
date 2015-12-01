package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.dao.odontologia.AutorizacionDescuentosOdonDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseAutorizacionDescuentosOdonDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

public class PostgresqlAutorizacionDescuentosOdonDao implements AutorizacionDescuentosOdonDao
{
	public ArrayList<DtoServicioOdontologico> consultarDetalleProgramasPresupuesto (double presupuesto, int institucion)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.consultarDetalleProgramasPresupuesto(presupuesto, institucion);
	}
	
	public ArrayList<DtoServicioOdontologico> consultarDetalleServiciosPresupuesto (double presupuesto, int institucion)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.consultarDetalleServiciosPresupuesto(presupuesto, institucion);
	}
	
	public ArrayList<DtoServicioOdontologico> consultarDetalleProgramasInclusion (double presupuesto, int institucion)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.consultarDetalleProgramasInclusion(presupuesto, institucion);
	}
	
	public ArrayList<DtoServicioOdontologico> consultarDetalleServiciosInclusion (double presupuesto, int institucion)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.consultarDetalleServiciosInclusion(presupuesto, institucion);
	}	
	
	@Override
	public ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuento(
			InfoDefinirSolucitudDsctOdon dto) {
		
		return SqlBaseAutorizacionDescuentosOdonDao.cargarDefinicionSolicitudesDescuento(dto);
	}

	@Override
	public boolean modificarPresupuestoDescuento(
			DtoPresupuestoOdontologicoDescuento dto) {
		
		return SqlBaseAutorizacionDescuentosOdonDao.modificarPresupuestoDescuento(dto);
	}
	
	@Override
	public ArrayList<DtoPresupuestoOdontologicoDescuento> consultarHistoricoSolAutorizacionDcto (BigDecimal presupuestoDctoOdon)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.consultarHistoricoSolAutorizacionDcto(presupuestoDctoOdon);
	}

	public boolean tieneNivelAutorizacion(String loginUsuario, BigDecimal codigoDetDescuento, String actividadTipoUsuario)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.tieneNivelAutorizacion(loginUsuario, codigoDetDescuento, actividadTipoUsuario );
	}
	
	public int vigenciaDiasPresupuesto(BigDecimal tmpCodigoPresupuesto)
	{
		return SqlBaseAutorizacionDescuentosOdonDao.vigenciaDiasPresupuesto(tmpCodigoPresupuesto);
	}
	
	
	
	@Override
	public boolean tieneNivelAutorizacionCentroAtencion(String loginUsuario,
			BigDecimal codigoDetDescuento, int codigoInstitucion, String actividadTipoUsuario) 
	{
		return SqlBaseAutorizacionDescuentosOdonDao.tieneNivelAutorizacionCentroAtencion(loginUsuario, codigoDetDescuento, codigoInstitucion, actividadTipoUsuario);
	}

}