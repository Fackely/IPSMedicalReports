/**
 * 
 */
package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoPaquetesPresupuesto;

import com.princetonsa.dao.odontologia.PresupuestoPaquetesDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoPaquetesDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:30:54 PM
 */
public class OraclePresupuestoPaquetesDao implements PresupuestoPaquetesDao 
{

	@Override
	public ArrayList<DtoPresupuestoPaquetes> cargar(Connection con,
			DtoPresupuestoPaquetes dto) {
		return SqlBasePresupuestoPaquetesDao.cargar(con, dto);
	}

	@Override
	public boolean eliminar(Connection con, DtoPresupuestoPaquetes dto) {
		return SqlBasePresupuestoPaquetesDao.eliminar(con, dto);
	}

	@Override
	public BigDecimal insertar(Connection con, DtoPresupuestoPaquetes dto) {
		return SqlBasePresupuestoPaquetesDao.insertar(con, dto);
	}

	@Override
	public ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesSinTarifas(int convenio,int contrato,String fechaVigenciaApp) 
	{
		return SqlBasePresupuestoPaquetesDao.cargarInfoPaquetesSinTarifas(convenio,contrato, fechaVigenciaApp);
	}

	@Override
	public String obtenerCodigoMostrarPaqueteOdontologico(int codigoPkPaquete) 
	{
		return SqlBasePresupuestoPaquetesDao.obtenerCodigoMostrarPaqueteOdontologico(codigoPkPaquete);
	}

	@Override
	public ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesTarifas(
			BigDecimal codigoPkPresupuesto) 
	{
		return SqlBasePresupuestoPaquetesDao.cargarInfoPaquetesTarifas(codigoPkPresupuesto);
	}

	@Override
	public boolean esDetallePaqueteConvenioVigente(BigDecimal codigoPk,
			String fechaApp) {
		return SqlBasePresupuestoPaquetesDao.esDetallePaqueteConvenioVigente(codigoPk, fechaApp);
	}

}