package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.servinte.axioma.orm.PresupuestoOdontologico;


/**
 * Esta clase se encarga de definir los métodos de negocio para 
 * el presupuesto odontológico
 *
 * @author Yennifer Guerrero
 */
public interface IPresupuestoOdontologicoDAO {
	
	/**
	 * Este m&eacute;todo se encarga de obtener los presupuestos en estado 
	 * precontratado que no tienen una solicitud de descuento odontol&oacute;gico.
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerPresupuestosPrecontratados(
			List<Long> presupuestos, String conSolicituDcto);
			
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por
	 * estados del presupuesto odontológico, según los filtros
	 * enviados por parámetro
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> buscarPresupuestoOdontologicoPorEstado(
			DtoReporteConsultaPacienteEstadoPresupuesto dto);
			

	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @author Diana Carolina G&oacute;mez
	 */
	
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(DtoReportePresupuestosOdontologicosContratados dto, String tSalidaReporte);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el presupuesto odontológico, 
	 * que no esté asociado a una solicitud de descuento 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<PresupuestoOdontologico> buscarPresupuestoOdontologicoSinSolicitudDescuento();
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por estado del 
	 * presupuesto con su respectivo valor totalizado.
	 * 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto dto
	 * @return ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 * 
	 *
	 */
	public ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> 
		buscarPacientesEstadoPresupuestoTotalizado(DtoReporteConsultaPacienteEstadoPresupuesto dto);
	
	
	/**
	 * Carga la entidad completa
	 * @param codigoPk
	 * @return PresupuestoOdontologico
	 */
	public PresupuestoOdontologico buscarPresupuestoId(long codigoPk);


	
}
