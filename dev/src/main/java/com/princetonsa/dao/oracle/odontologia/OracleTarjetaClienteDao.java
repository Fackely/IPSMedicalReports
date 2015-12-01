package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.TarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseTarjetaClienteDao;
import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;

/**
 * 
 * @author axioma
 *
 */
public class OracleTarjetaClienteDao implements TarjetaClienteDao
{
	/**
	 * 
	 */
	public double guardar( DtoTarjetaCliente dto) 
	{
		return SqlBaseTarjetaClienteDao.guardar(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoTarjetaCliente> cargar(DtoTarjetaCliente dto) 
	{
		return SqlBaseTarjetaClienteDao.cargar(dto);
	}

	/**
	 * 
	 */
	public boolean modificar(DtoTarjetaCliente dto) 
	{
		return SqlBaseTarjetaClienteDao.modificar(dto);
    }

	/**
	 * 
	 */
	public boolean eliminar(DtoTarjetaCliente dto) 
	{
		return SqlBaseTarjetaClienteDao.eliminar(dto) ;
	}

	@Override
	public int obtenerEsquemaTarifarioTarjetaCliente(double contrato) {
		
		return SqlBaseTarjetaClienteDao.obtenerEsquemaTarifarioTarjetaCliente(contrato);
	}
	
	@Override
	public ArrayList<DtoTarjetaCliente> cargarConvenio (DtoTarjetaCliente dto, boolean modificar)
	{
		return SqlBaseTarjetaClienteDao.cargarConvenio(dto, modificar);
	}

	@Override
	public int obtenerContratoTarjetaCliente(double codigoPkTarjetaCliente) {
		
		return SqlBaseTarjetaClienteDao.obtenerContratoTarjetaCliente(codigoPkTarjetaCliente);
	}
	
	@Override
	public ArrayList<DtoResultadoReporteVentaTarjetas> consultarDatosReporte(
			DtoFiltroReporteIngresosTarjetasCliente dtoFiltro) {
		return SqlBaseTarjetaClienteDao.consultarDatosReporte(dtoFiltro);
	}
}
