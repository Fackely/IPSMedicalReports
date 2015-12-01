package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.odontologia.InfoConvenioContratoPresupuesto;

import com.princetonsa.dao.odontologia.ValidacionesPresupuestoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseValidacionesPresupuestoDao;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlValidacionesPresupuestoDao implements ValidacionesPresupuestoDao
{
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public boolean existeConvenioEnIngreso(int ingreso, int convenio)
	{
		return SqlBaseValidacionesPresupuestoDao.existeConvenioEnIngreso(ingreso, convenio);
	}
	
	/**
	 * 
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean existeCancelacionContratoXIncumplimiento(BigDecimal codigoPlanTratamiento, int ingreso)
	{
		return SqlBaseValidacionesPresupuestoDao.existeCancelacionContratoXIncumplimiento(codigoPlanTratamiento, ingreso);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public boolean existenPresupuestosDadosEstados(int ingreso, ArrayList<String> estados)
	{
		return SqlBaseValidacionesPresupuestoDao.existenPresupuestosDadosEstados(ingreso, estados);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public boolean esPlanTratamientoPorConfirmar(BigDecimal planTratamiento)
	{
		return SqlBaseValidacionesPresupuestoDao.esPlanTratamientoPorConfirmar(planTratamiento);
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @return
	 */
	public boolean tienePlanTratamientoEstados(BigDecimal planTratamiento, ArrayList<String> estados)
	{
		return SqlBaseValidacionesPresupuestoDao.tienePlanTratamientoEstados(planTratamiento, estados);
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @param utilizaProgramas
	 * @return
	 */
	public boolean tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(BigDecimal planTratamiento, ArrayList<String> estados, boolean utilizaProgramas)
	{
		return SqlBaseValidacionesPresupuestoDao.tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(planTratamiento, estados, utilizaProgramas);
	}
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratosPacientePresupuesto(int ingreso)
	{
		return SqlBaseValidacionesPresupuestoDao.cargarConveniosContratosPacientePresupuesto(ingreso);
	}
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal existeProgramaServicioPresupuestoEnPlanTratamiento(BigDecimal codigoPkPresupuestoPieza, String estadoOPCIONAL, boolean utilizaProgramas)
	{
		return SqlBaseValidacionesPresupuestoDao.existeProgramaServicioPresupuestoEnPlanTratamiento(codigoPkPresupuestoPieza, estadoOPCIONAL, utilizaProgramas);
	}
	
	@Override
	public BigDecimal obtenerCodigoPkProgramaServicioPlanTratamiento(
			BigDecimal codigoPkDetallePlan, BigDecimal codigoPkPrograma,
			int servicio) 
	{
		return SqlBaseValidacionesPresupuestoDao.obtenerCodigoPkProgramaServicioPlanTratamiento(codigoPkDetallePlan, codigoPkPrograma, servicio);
	}

	@Override
	public BigDecimal esProgramaServicioAdicionPlanTtoPresupuesto(	BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo,
																	String seccion, BigDecimal presupuesto, boolean utilizaProgramas,
																	BigDecimal programaOServicio) 
	{
		return SqlBaseValidacionesPresupuestoDao.esProgramaServicioAdicionPlanTtoPresupuesto(pieza, superficie, hallazgo, seccion, presupuesto, utilizaProgramas, programaOServicio);
	}

	@Override
	public BigDecimal existeProgramaServicioAdicionPlanTtoPresupuesto(
			BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo,
			String seccion, BigDecimal presupuesto, boolean utilizaProgramas,
			BigDecimal programaOServicio) 
	{
		return SqlBaseValidacionesPresupuestoDao.existeProgramaServicioAdicionPlanTtoPresupuesto(pieza, superficie, hallazgo, seccion, presupuesto, utilizaProgramas, programaOServicio);
	}
	
	@Override
	public boolean pacienteTienetarjetaActiva(int codigoPersona) {
		return SqlBaseValidacionesPresupuestoDao.pacienteTienetarjetaActiva(codigoPersona);
	}

}
