package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.InfoDatos;

import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoConsolidadoReporteIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Ingresos;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface IIngresosDAO {
	
	
	/**
	 * Retorna el ultimo ingreso en estado abierto del paciente
	 * Este metodo debe llamarse luego de validar luego que el paciente tenga ingresos 
	 * en estado abierto @see (List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso)).
	 * El valor de abonoDisponible retornado es el valor totalizado de todos los ingresos.
	 * Si el paciente no tiene un centro de atencion duenio asociado no se traeran registros de este.
	 * 
	 * @param codPaciente
	 * @param parametroManejoEspecialInstiOdontologicas
	 * @return
	 */
	
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente (int codPaciente, String parametroManejoEspecialInstiOdontologicas);
	
	
	/**
	 * Retorna los ingresos de un paciente
	 * 
	 * @param codPaciente
	 * @return
	 */
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente (int codPaciente, boolean listarPorIngreso);
	
	
	
	/**
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return
	 */
	public List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso);
	
	
	
	
	/**
	 * Retorna el ultimo ingreso en estado abierto del paciente
	 * Este metodo debe llamarse luego de validar luego que el paciente tenga ingresos 
	 * en estado abierto @see (List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso)).
	 * El valor de abonoDisponible retornado es el valor totalizado de todos los ingresos.
	 * Si el paciente no tiene un centro de atencion duenio asociado no se traeran registros de este.
	 * 
	 * @param codPaciente
	 * @param parametroManejoEspecialInstiOdontologicas
	 * @return
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente (int codPaciente, boolean parametroManejoEspecialInstiOdontologicas);
	
	/**
	 * Este m&eacute;todo se encarga de consultar los ingresos odontol&oacute;gicos
	 * en estado abierto seg&uacute;n los criterios especificados.
	 * @param dto
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> consultarIngresosOdontoEstadoAbierto(DtoReporteIngresosOdontologicos dto);
	
	/**
	 * Este m&eacute;todo se encarga de obtener los ingresos
	 * sin citas odontol&oacute;gicas.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinCitasOdonto(List<Integer> ingresos);
	
	/**
	 * Este m&eacute;todo se encarga de obtener los ingresos odontológicos sin presupuesto.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinPresupuesto(
			List<Integer> ingresosConValIni, List<Long> listaUltimaCitaPorPaciente);
	
	/**
	 * Este m&eacute;todo se encarga de obtener las ingresos de tipo
	 * valoraci&oacute;n inicial en estado atendida que tienen presupuestos
	 * odontol&oacute;gicos.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosValIniConPresupuesto(List<Integer> ingresos, DtoReporteIngresosOdontologicos filtroIngresos, List<Long> listaUltimaCitaPorPaciente);
	
	/**
	 * Este m&eacute;todo se encarga de obtener el n&uacute;mero total
	 * de pacientes que fueron ingresados.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int obtenerTotalPacientesIngresados (List<Integer> ingresosconsulta, int consecutivoCA);
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerTotalPacientes (List<Integer> ingresosConValIni, List<Integer> ingresosSinValIni, int consecutivoCA);
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int obtenerTotalPacientesPresupuesto (List<Integer> ingresosSinPresupuesto, List<Integer> ingresosConPresupuesto, int consecutivoCA);
		
	
	/**
	 * Este m&eacute;todo se encarga de obtener el totalizado por estado de 
	 * los ingresos con presupuesto.
	 * @param ingresos
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param filtroIngresos 
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerConsolidadoIngresosConPresupuesto(List<Integer> ingresosConPresupuesto, int consecutivoCA, DtoReporteIngresosOdontologicos filtroIngresos);
	
	
	
	/**
	 * Retorna el ultimo ingresodel paciente
	 * 
	 * @param codPaciente
	 * @return DtoInfoIngresoTrasladoAbonoPaciente
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente (int codPaciente);
	
	/**
	 * Método encargado de obtener el id del ingreso abierto de un paciente
	 * determinado.
	 * 
	 * @param codigoPaciente
	 * @return
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 */
	public int consultarIdIngresosPacienteEstadoAbierto(int codigoPaciente);
	
	/**
	 * Método encargado de obtener los ingresos con Vía de Ingreso = CONSULTA EXTERNA, cuya fecha de atención 
	 * de las citas se encuentre dentro de las últimas 24 horas.
	 * 
	 * @param codigoPaciente
	 * @return List<DtoIngreso> ingresos
	 * 
	 * @author Ricardo Ruiz
	 */
	public List<DtoIngresos> consultarIngresosConsultaExternaPorFechaHoraEstadoCita(int codigoPaciente, Date fecha, String hora, int estadoCita);
	
	
	/**
	 * Método encargado de obtener el último diagnostico principal correspondiente a la
	 * respuesta de procedimientos
	 * 
	 * @param codigoPaciente
	 * @return InfoDatos
	 * 
	 * @author Ricardo Ruiz
	 */
	public InfoDatos consultarUltimoDiagnosticoRespuestaSolicitudes(int codigoIngreso, int tipoSolicitud);
	
	
}