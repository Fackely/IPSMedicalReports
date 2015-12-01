package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetalleHallazgoProgramaServicioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleHallazgoVsProgramaServicioDao;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;

public class OracleDetalleHallazgoProgramaServicioDao implements DetalleHallazgoProgramaServicioDao{

	
	@Override
	public ArrayList<DtoEquivalentesHallazgoProgramaServicio> cargarEquivalentes(
			DtoEquivalentesHallazgoProgramaServicio dtoWhere , String tipo) {
		
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.cargarEquivalentes(dtoWhere, tipo);
	}

	@Override
	public boolean eliminar(DtoDetalleHallazgoProgramaServicio dtoWhere) {
		
		 return SqlBaseDetalleHallazgoVsProgramaServicioDao.eliminar(dtoWhere);
	}

	@Override
	public boolean eliminarEquivalentes(
			DtoEquivalentesHallazgoProgramaServicio dtoWhere) {
		
		 return SqlBaseDetalleHallazgoVsProgramaServicioDao.eliminarEquivalentes(dtoWhere);
	}

	@Override
	public double guardar(DtoDetalleHallazgoProgramaServicio dto , Connection con) {
		
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.guardar(dto , con);
	}

	@Override
	public double guardarEquivalente(DtoEquivalentesHallazgoProgramaServicio dto , Connection con) {
		
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.guardarEquivalente(dto, con);
	}

	@Override
	public boolean modificar(DtoDetalleHallazgoProgramaServicio dtoNuevo,
			DtoDetalleHallazgoProgramaServicio dtoWhere) {
		
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.modificar(dtoNuevo, dtoWhere);
	}

	@Override
	public ArrayList<DtoDetalleHallazgoProgramaServicio> cargar(	DtoDetalleHallazgoProgramaServicio dtoWhere , int codigoInstitucion)
	{
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.cargar(dtoWhere, codigoInstitucion);
	}

	@Override
	public ArrayList<Double> obtenerEquivalentesProgServ(
			BigDecimal programaServicio, double hallazgo, boolean utilizaPrograma) {
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.obtenerEquivalentesProgServ(programaServicio, hallazgo, utilizaPrograma);
	}

	@Override
	public boolean programasServiciosEquivalentes(BigDecimal programaServicio1, BigDecimal programaServicio2, double hallazgo, boolean utilizaPrograma)
	{
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.programasServiciosEquivalentes(programaServicio1, programaServicio2, hallazgo, utilizaPrograma);
	}

	@Override
	public int consultarNumeroSuperficiesAplica(DtoDetalleHallazgoProgramaServicio detalleHallazgoProgramaServicio, int codigoHallazgo)
	{
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.consultarNumeroSuperficiesAplica(detalleHallazgoProgramaServicio, codigoHallazgo);
	}
	
	
	@Override
	public boolean existenHallazgosPlanTratamiento(
													DtoDetalleHallazgoProgramaServicio dto) 
	{
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.existenHallazgosPlanTratamiento(dto);
	}

	
	@Override
	public boolean existeDetalleAsociadoPlanTratamiento(DtoDetalleHallazgoProgramaServicio detalleHallazgo, boolean aplicaProgramas) 
	{
		return SqlBaseDetalleHallazgoVsProgramaServicioDao.existeDetalleAsociadoPlanTratamiento(detalleHallazgo, aplicaProgramas);
	}
	
}
