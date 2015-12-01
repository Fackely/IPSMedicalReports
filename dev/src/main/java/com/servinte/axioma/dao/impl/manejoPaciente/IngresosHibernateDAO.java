package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.InfoDatos;

import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoConsolidadoReporteIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IPacientesDAO;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.delegate.manejoPaciente.IngresosDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IPacientesDAO}.
 * 
 * @author Cristhian Murillo
 * @see IngresosDelegate.
 */

public class IngresosHibernateDAO implements IIngresosDAO{

	private IngresosDelegate delegate = new IngresosDelegate();

	@Override
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente(int codPaciente, String parametroManejoEspecialInstiOdontologicas) {
		return delegate.cargarIngresosPorPaciente(codPaciente, parametroManejoEspecialInstiOdontologicas);
	}
	
	@Override
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente(int codPaciente, boolean listarPorIngreso) {
		return delegate.obtenerIngresosParaTrasladoPorPaciente(codPaciente, listarPorIngreso);
	}


	@Override
	public List<Ingresos> obtenerIngresosPacientePorEstado(int codPaciente,String[] listaEstadosIngreso) {
		return delegate.obtenerIngresosPacientePorEstado(codPaciente, listaEstadosIngreso);
	}


	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente(	int codPaciente, boolean parametroManejoEspecialInstiOdontologicas) {
		return delegate.obtenerUltimoIngresoAbiertoPaciente(codPaciente, parametroManejoEspecialInstiOdontologicas);
	}
	
	@Override
	public List<Integer> consultarIngresosOdontoEstadoAbierto(
			DtoReporteIngresosOdontologicos dto){
		return delegate.consultarIngresosOdontoEstadoAbierto(dto);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinCitasOdonto(List<Integer> ingresos){
		return delegate.obtenerIngresosSinCitasOdonto(ingresos);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinPresupuesto(
			List<Integer> ingresosConValIni, List<Long> listaUltimaCitaPorPaciente){
		return delegate.obtenerIngresosSinPresupuesto(ingresosConValIni, listaUltimaCitaPorPaciente);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosValIniConPresupuesto(
			List<Integer> ingresos, DtoReporteIngresosOdontologicos filtroIngresos, List<Long> listaUltimaCitaPorPaciente){
		
		return delegate.obtenerIngresosValIniConPresupuesto(ingresos, filtroIngresos, listaUltimaCitaPorPaciente);
	}
	
	@Override
	public int obtenerTotalPacientesIngresados (List<Integer> ingresosconsulta, int consecutivoCA){
		return delegate.obtenerTotalPacientesIngresados (ingresosconsulta, consecutivoCA);
	}
	
	@Override
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerTotalPacientes (List<Integer> ingresosConValIni, List<Integer> ingresosSinValIni, int consecutivoCA){
		return delegate.obtenerTotalPacientes(ingresosConValIni, ingresosSinValIni, consecutivoCA);
	}
	
	@Override
	public int obtenerTotalPacientesPresupuesto (List<Integer> ingresosSinPresupuesto, List<Integer> ingresosConPresupuesto, int consecutivoCA){
		return delegate.obtenerTotalPacientesPresupuesto(ingresosSinPresupuesto, ingresosConPresupuesto, consecutivoCA);
	}
	
	@Override
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerConsolidadoIngresosConPresupuesto(List<Integer> ingresosConPresupuesto, int consecutivoCA, DtoReporteIngresosOdontologicos filtroIngresos){
		return delegate.obtenerConsolidadoIngresosConPresupuesto(ingresosConPresupuesto, consecutivoCA, filtroIngresos);
	}


	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente(int codPaciente) {
		return delegate.obtenerUltimoIngresoPaciente(codPaciente);
	}
	
	@Override
	public int consultarIdIngresosPacienteEstadoAbierto(int codigoPaciente){
		return delegate.consultarIdIngresosPacienteEstadoAbierto(codigoPaciente);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO#consultarIngresosConsultaExternaPorFechaHoraEstadoCita(int, java.util.Date, java.lang.String, int)
	 */
	@Override
	public List<DtoIngresos> consultarIngresosConsultaExternaPorFechaHoraEstadoCita(
			int codigoPaciente, Date fecha, String hora, int estadoCita) {
		return delegate.consultarIngresosConsultaExternaPorFechaHoraEstadoCita(codigoPaciente, fecha, hora, estadoCita);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO#consultarUltimoDiagnosticoRespuestaSolicitudes(int, int)
	 */
	@Override
	public InfoDatos consultarUltimoDiagnosticoRespuestaSolicitudes(
			int codigoIngreso, int tipoSolicitud) {
		return delegate.consultarUltimoDiagnosticoRespuestaSolicitudes(codigoIngreso, tipoSolicitud);
	}
}
