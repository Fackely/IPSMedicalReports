package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.UtilidadFecha;

import com.princetonsa.dao.administracion.ConsecutivosCentroAtencionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseConsecutivosCentroAtencionDao;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;

/**
 * @author axioma
 */
public class PostgresqlConsecutivosCentroAtencionDao implements ConsecutivosCentroAtencionDao {

	
	@Override
	public boolean eliminar(DtoConsecutivoCentroAtencion dto) {
		
		return SqlBaseConsecutivosCentroAtencionDao.eliminar(dto);
	}

	@Override
	public ArrayList<DtoConsecutivoCentroAtencion> cargar(
			DtoConsecutivoCentroAtencion dto) {
		
		return SqlBaseConsecutivosCentroAtencionDao.cargar(dto);
	}

	@Override
	public boolean guardar(DtoConsecutivoCentroAtencion dto, Connection con) {
		
		return SqlBaseConsecutivosCentroAtencionDao.guardar(dto,con);
	}

	@Override
	public boolean modificar(Connection con, DtoConsecutivoCentroAtencion dto) {
		
		return SqlBaseConsecutivosCentroAtencionDao.modificar(con, dto);
	}
	
	@Override
	public BigDecimal incrementarConsecutivo(int centroAtencion, String nombreConsecutivo,
			int institucion) {
		String consulta="select administracion.siguienteConsdisponibleCentro('"+nombreConsecutivo+"',"+institucion+",'"+UtilidadFecha.getMesAnioDiaActual("anio")+"', "+centroAtencion+") as valor";
		return SqlBaseConsecutivosCentroAtencionDao.incrementarConsecutivo(centroAtencion, nombreConsecutivo, consulta, institucion);
	}

	@Override
	public BigDecimal obtenerValorActualConsecutivo(Connection con, int centroAtencion,
			String nombreConsecutivo, int anio) {
		return SqlBaseConsecutivosCentroAtencionDao.obtenerValorActualConsecutivo(con, centroAtencion, nombreConsecutivo, anio);
	}

	@Override
	public boolean esConsecutivoAsignado(DtoConsecutivoCentroAtencion dto) 
	{
		return SqlBaseConsecutivosCentroAtencionDao.esConsecutivoAsignado(dto);
	}
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.administracion.ConsecutivosCentroAtencionDao#cambiarUsoFinalizadoConsecutivo(java.sql.Connection, int, java.math.BigDecimal, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean cambiarUsoFinalizadoConsecutivo(Connection con,
			String consecutivo, int centroAtencion, BigDecimal valor,
			String finalizado, String usado, int anio)
	{
		return SqlBaseConsecutivosCentroAtencionDao.cambiarUsoFinalizadoConsecutivo(con, consecutivo, centroAtencion, valor, finalizado, usado, anio);
	}

}
