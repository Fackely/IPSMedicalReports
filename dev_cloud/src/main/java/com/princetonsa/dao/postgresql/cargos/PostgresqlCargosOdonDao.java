package com.princetonsa.dao.postgresql.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dao.cargos.CargosOdonDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosOdonDao;
import com.princetonsa.dto.odontologia.DtoProgramaServicio;
import com.servinte.axioma.fwk.exception.BDException;

public class PostgresqlCargosOdonDao implements CargosOdonDao 
{
	@Override
	public InfoDatosInt obtenerEsquemaTarifario(int servicio, int contrato,
			String fechaCalculoVigencia) {
		
		return SqlBaseCargosOdonDao.obtenerEsquemaTarifario(servicio, contrato, fechaCalculoVigencia);
	}

	@Override
	public HashMap<String, String> obtenerFiltrosPromociones(BigDecimal cuenta)
	{
		
		return SqlBaseCargosOdonDao.obtenerFiltrosPromociones(cuenta);
	}

	@Override
	public int obtenerContrato(int ingreso, int convenio) {
		
		return SqlBaseCargosOdonDao.obtenerContrato(ingreso, convenio);
	}

	@Override
	public InfoDctoOdontologicoPresupuesto obtenerDescuentoOdon(
			int centroAtencion, String fechaCalculoVigencia,
			BigDecimal valorPresupuestoXConvenio)
	{
		
		return SqlBaseCargosOdonDao.obtenerDescuentoOdon(centroAtencion, fechaCalculoVigencia, valorPresupuestoXConvenio);
	}

/*	@Override
	public BigDecimal obtenerSerialBono(int convenio, int cuenta)
	{
		
		return SqlBaseCargosOdonDao.obtenerSerialBono(convenio, cuenta);
	}*/

	@Override
	public InfoBonoDcto obtenerDescuentosBono(int cuenta, int institucion,
			BigDecimal valorTarifa, double programaExcluyente, int servicioExcluyente,
			final int convenio, String fechaApp)
	{
		
		return SqlBaseCargosOdonDao.obtenerDescuentosBono(cuenta, institucion, valorTarifa, programaExcluyente, servicioExcluyente, convenio, fechaApp);
	}
	
	
	/**
	 * Método para cargar la informacion del presupuesto contratado de un servicio especifico
	 * @param con
	 * @param codigoPresuOdoProgSer
	 * @param infoRespCobertura
	 * @param infoTarifa
	 */
	@Override
	public void obtenerInfoPresupuestoContratadoProgSer(Connection con,int codigoPresuOdoProgSer,InfoResponsableCobertura infoRespCobertura,InfoTarifaServicioPresupuesto infoTarifa, boolean utilizaProgramasInstitucion, int servicio, BigDecimal codigoPkPlanTratamiento)
	{
		SqlBaseCargosOdonDao.obtenerInfoPresupuestoContratadoProgSer(con, codigoPresuOdoProgSer, infoRespCobertura, infoTarifa, utilizaProgramasInstitucion, servicio, codigoPkPlanTratamiento);
	}
	
	/**
	 * Método para saber si la cita procesda es la priemra cita del plan de tratamietno
	 * @param con
	 * @param codigoPlanTratamiento
	 * @return
	 */
	@Override
	public boolean esPrimeraCitaPlanTratamiento(Connection con,int codigoPlanTratamiento)
	{
		return SqlBaseCargosOdonDao.esPrimeraCitaPlanTratamiento(con, codigoPlanTratamiento);
	}
	
	/**
	 * Método para actualizar el número de pacientes atendidos por contrato
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	@Override
	public ResultadoBoolean actualizarNumeroPacientesAtendidosContrato(Connection con,int codigoContrato)
	{
		return SqlBaseCargosOdonDao.actualizarNumeroPacientesAtendidosContrato(con, codigoContrato);
	}

	@Override
	public InfoTarifa obtenerDescuentoComercialXConvenioPrograma(
			Connection con, int codigoViaIngreso, String tipoPaciente,
			int codigoContrato, Double codigoPrograma, int codigoInstitucion,
			String fechaCalculoVigencia) throws BDException
	{
		return SqlBaseCargosOdonDao.obtenerDescuentoComercialXConvenioPrograma(con,codigoViaIngreso,tipoPaciente,codigoContrato,codigoPrograma, codigoInstitucion,fechaCalculoVigencia);
	}
	
	@Override
	public boolean liberarSerialBono(Connection con, int convenio, int ingreso,
			BigDecimal serial, int programa)
	{
		return SqlBaseCargosOdonDao.liberarSerialBono(con, convenio, ingreso, serial, programa);
	}

	@Override
	public int obtenerPresupuestoOdoProgSer(Connection con, int presupuesto, DtoProgramaServicio programa)
	{
		return SqlBaseCargosOdonDao.obtenerPresupuestoOdoProgSer(con, presupuesto, programa);
	}
}
