package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;

public class PostgresqlDetalleEmisionTarjetaClienteDao implements DetalleEmisionTarjetaClienteDao {

	@Override
	public ArrayList<DtoDetalleEmisionesTarjetaCliente> cargar(
			DtoDetalleEmisionesTarjetaCliente dtoWhere) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoDetalleEmisionesTarjetaCliente dtoWhere) {
		
	   return SqlBaseDetalleEmisionTarjetaClienteDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoDetalleEmisionesTarjetaCliente dto) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDetalleEmisionesTarjetaCliente dtoNuevo,
			DtoDetalleEmisionesTarjetaCliente dtoWhere) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.modificar(dtoNuevo, dtoWhere);
	}

	@Override
	public boolean existeRangoSeriales(double codigo, int institucion,
			double ValorMinimo, double ValorMaximo) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.existeRangoSeriales(codigo, institucion, ValorMinimo, ValorMaximo);
	}
	
	@Override
	public boolean fueraRango(int institucion, double encabezado,
			double ValorMinimo, double ValorMaximo) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.fueraRango(institucion, encabezado, ValorMinimo, ValorMaximo);
	}

	@Override
	public BigDecimal cargarSerialMayor(DtoDetalleEmisionesTarjetaCliente dto) {
		
		return SqlBaseDetalleEmisionTarjetaClienteDao.cargarSerialMayor(dto);
	}

}
