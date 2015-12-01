package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.actionform.odontologia.AgendaOdontologicaForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Ingresos;


public interface IIngresosServicio {
	
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
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @autor Cristhian Murillo
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return
	 */
	public List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso);
	
	/**
	 * Este m&eacute;todo se encarga de obtener los datos de la sección de consoliado
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
	 * Asigna los valores al dto de la información de ingreso
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(AgendaOdontologicaForm forma, UsuarioBasico usuario);
		
		

	/**
	 * Crea un objeto de tipo Ingresos
	 * @param dtoInfoBasica
	 * @return
	 */
	public Ingresos crearIngresos(DtoInformacionBasicaIngresoPaciente dtoInfoBasica);

	
	

	/**
	 * Asigna los valores al dto de la información de ingreso
	 * @param servicio
	 * @param usuario
	 * @return
	 */
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(DtoServicioOdontologico servicio, UsuarioBasico usuario);
	
	/**
	 * Este m&eacute;todo se encarga de organizar la información del
	 * reporte de ingresos odontológicos cuando se ha seleccionado la opción de ambos.
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
	 * Este mètodo se encarga de obtener un listado con los pacientes que tienen
	 * citas odontológicas según los ingresos que llegan por parámetro.
	 * Obtiene el último registro de cada estado de la cita de valoración inicial
	 * y su correspondiente fecha de modificación.
	 *
	 * @param filtro
	 * @param ingresos
	 * @param arrayList 
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoCitasPorPaciente(DtoReporteIngresosOdontologicos filtro,
			List<Integer> ingresos, ArrayList<DtoCentrosAtencion> listadoCentrosAtencionIngreso);
	
	
}
