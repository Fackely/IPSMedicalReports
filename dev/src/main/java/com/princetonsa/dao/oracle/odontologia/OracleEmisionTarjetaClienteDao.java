package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.EmisionTarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseEmisionTarjetaClienteDao;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class OracleEmisionTarjetaClienteDao implements EmisionTarjetaClienteDao{

	@Override
	public ArrayList<DtoEmisionTarjetaCliente> cargar(
			DtoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseEmisionTarjetaClienteDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseEmisionTarjetaClienteDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoEmisionTarjetaCliente dto) {
		
		return SqlBaseEmisionTarjetaClienteDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoEmisionTarjetaCliente dtoNuevo,
			DtoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseEmisionTarjetaClienteDao.modificar(dtoNuevo, dtoWhere);
	}

	@Override
	public boolean existeRangoSeriales(double codigo, String convenio,
			double ValorMinimo, double ValorMaximo) {
		
		return SqlBaseEmisionTarjetaClienteDao.existeRangoSeriales(codigo, convenio, ValorMinimo, ValorMaximo);
	}
	
	@Override
	public boolean existeSerialEnEmisionTarjetaDetalle(
			double codigoPKTipoTarjeta, int centroAtencion,
			double rangoInicialSerial, double rangoFinalSerial, int codigoInstitucion) 
	{
		return SqlBaseEmisionTarjetaClienteDao.existeSerialEnEmisionTarjetaDetalle(codigoPKTipoTarjeta, centroAtencion, rangoInicialSerial, rangoFinalSerial, codigoInstitucion);
	}

	
	@Override
	public boolean existeTarjetaRegistrada(DtoTarjetaCliente dto) {
		return  SqlBaseEmisionTarjetaClienteDao.existeTarjetaRegistrada(dto);
	}

	

	
}
