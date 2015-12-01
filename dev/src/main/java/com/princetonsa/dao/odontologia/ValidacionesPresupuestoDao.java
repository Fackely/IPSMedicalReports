package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.odontologia.InfoConvenioContratoPresupuesto;

/**
 * 
 * @author axioma
 *
 */
public interface ValidacionesPresupuestoDao
{
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public boolean existeConvenioEnIngreso(int ingreso, int convenio);
	
	/**
	 * 
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean existeCancelacionContratoXIncumplimiento(BigDecimal codigoPlanTratamiento, int ingreso);
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public boolean existenPresupuestosDadosEstados(int ingreso, ArrayList<String> estados);
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public boolean esPlanTratamientoPorConfirmar(BigDecimal planTratamiento);
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @return
	 */
	public boolean tienePlanTratamientoEstados(BigDecimal planTratamiento, ArrayList<String> estados);
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @param utilizaProgramas
	 * @return
	 */
	public boolean tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(BigDecimal planTratamiento, ArrayList<String> estados, boolean utilizaProgramas);
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratosPacientePresupuesto(int ingreso);
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal existeProgramaServicioPresupuestoEnPlanTratamiento(BigDecimal codigoPkPresupuestoPieza, String estadoOPCIONAL, boolean utilizaProgramas);

	/**
	 * 
	 * @param codigoPkDetallePlan
	 * @param codigoPkPrograma
	 * @param servicio
	 * @return
	 */
	public BigDecimal obtenerCodigoPkProgramaServicioPlanTratamiento(BigDecimal codigoPkDetallePlan, BigDecimal codigoPkPrograma,int servicio);
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal esProgramaServicioAdicionPlanTtoPresupuesto(BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo, String seccion, BigDecimal presupuesto, boolean utilizaProgramas, BigDecimal programaOServicio);
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal existeProgramaServicioAdicionPlanTtoPresupuesto(BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo, String seccion, BigDecimal presupuesto, boolean utilizaProgramas, BigDecimal programaOServicio);

	/**
	 * Verifica si el paciente tiene tarjeta activa
	 * @param codigoPersona código de la persona a verificar
	 * @return true en caso de tener tarjeta activa, false de lo contrario
	 */
	public boolean pacienteTienetarjetaActiva(int codigoPersona);
}
