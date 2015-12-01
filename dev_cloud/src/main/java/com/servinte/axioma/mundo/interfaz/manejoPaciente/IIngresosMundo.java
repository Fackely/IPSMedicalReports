package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.actionform.odontologia.AgendaOdontologicaForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Ingresos;


/**
 * Define la l&oacute;gica de negocio relacionada con los ingresos
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.IngresosMundo
 */
public interface IIngresosMundo {
	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro General Controlar Abono Pacientes X Ingreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @param parametroGeneralControlarAbonoPacientesXIngreso
	 * @return
	 */
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente (int codigoPaciente, String parametroGeneralControlarAbonoPacientesXIngreso);

	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro listarPorIngreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @param listarPorIngreso
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
	public List<Integer> consultarIngresosOdontoEstadoAbierto(
			DtoReporteIngresosOdontologicos dto);
	
	/**
	 * Este m&eacute;todo se encarga de obtener los datos de la secci�n de consoliado
	 * del reporte de ingresos odontol&oacute;gicos.
	 * @param listadoIngresosOdonto
	 * @param ingresosconsulta
	 * @param listadoIngresosConValIni
	 * @param listadoIngresosSinValIni
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param filtroIngresos 
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoReporte (ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto, 
			List<Integer> ingresosconsulta, DtoReporteIngresosOdontologicos filtroIngresos);
	
	
		
	/**
	 * Crea un objeto de tipo Ingresos
	 * @param dtoInfoBasica
	 * @return
	 */
	public Ingresos crearIngresos(DtoInformacionBasicaIngresoPaciente dtoInfoBasica);
	
	
	
	/**
	 * Asigna los valores al dto de la informaci�n de ingreso
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(AgendaOdontologicaForm forma, UsuarioBasico usuario);
	
	
	
	/**
	 * Asigna los valores al dto de la informaci�n de ingreso
	 * @param servicio
	 * @param usuario
	 * @return
	 */
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(DtoServicioOdontologico servicio, UsuarioBasico usuario);
		
	
	/**
	 * Este m&eacute;todo se encarga de organizar la informaci�n del
	 * reporte de ingresos odontol�gicos cuando se ha seleccionado la opci�n de ambos.
	 * @param listadoIngresosOdontoConValIni
	 * @param listadoIngresosOdontoSinValIni
	 * @param listadoCentroAtencionIngreso
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> ordenarInfoReporteAmbos(
			ArrayList<DtoCentrosAtencion> listadoCentroAtencionIngreso, 
			ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto);
	
	
	
	
	/**
	 * Retorna el ultimo ingresodel paciente
	 * 
	 * @param codPaciente
	 * @return DtoInfoIngresoTrasladoAbonoPaciente
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente (int codPaciente);
	
	/**
	 * Este m�todo se encarga de obtener un listado con los pacientes que tienen
	 * citas odontol�gicas seg�n los ingresos que llegan por par�metro.
	 * Obtiene el �ltimo registro de cada estado de la cita de valoraci�n inicial
	 * y su correspondiente fecha de modificaci�n.
	 *
	 * @param filtro
	 * @param ingresos
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoCitasPorPaciente(DtoReporteIngresosOdontologicos filtro,
			List<Integer> ingresos, ArrayList<DtoCentrosAtencion> listadoCentrosAtencionIngreso);
	
	/**
	 * M�todo encargado de obtener el id del ingreso abierto de un paciente
	 * determinado.
	 * 
	 * @param codigoPaciente
	 * @return
	 * 
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public int consultarIdIngresosPacienteEstadoAbierto(int codigoPaciente);

	/**
	 * M�todo encargado de obtener los ingresos con V�a de Ingreso = CONSULTA EXTERNA, cuya fecha de atenci�n 
	 * de las citas se encuentre dentro de las �ltimas 24 horas.
	 * 
	 * @param codigoPaciente
	 * @return List<DtoIngreso> ingresos
	 * 
	 * @author Ricardo Ruiz
	 */
	public List<DtoIngresos> consultarIngresosConsultaExternaPorFechaHoraEstadoCita(int codigoPaciente, Date fecha, String hora, int estadoCita);
}

