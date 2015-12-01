package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoDAO;
import com.servinte.axioma.orm.PresupuestoOdontologico;
import com.servinte.axioma.orm.delegate.odontologia.contrato.PresupuestoOdontologicoDelegate;

/**
 * @author Yennifer Guerrero :)
 * @since  16/09/2010
 *
 */
public class PresupuestoOdontologicoHibernateDAO implements IPresupuestoOdontologicoDAO {
	
	private PresupuestoOdontologicoDelegate delegate;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PresupuestoOdontologicoHibernateDAO() {
		delegate = new PresupuestoOdontologicoDelegate();
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerPresupuestosPrecontratados(
			List<Long> presupuestos, String conSolicituDcto){
		return delegate.obtenerPresupuestosPrecontratados(presupuestos, conSolicituDcto);
	}
	
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
			DtoReporteConsultaPacienteEstadoPresupuesto dto){
		return delegate.buscarPresupuestoOdontologicoPorEstado(dto);
	}
	
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
	public ArrayList<PresupuestoOdontologico> buscarPresupuestoOdontologicoSinSolicitudDescuento(){
		return delegate.buscarPresupuestoOdontologicoSinSolicitudDescuento();
	}
	
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
		buscarPacientesEstadoPresupuestoTotalizado(DtoReporteConsultaPacienteEstadoPresupuesto dto){
		return delegate.buscarPacientesEstadoPresupuestoTotalizado(dto);
	}

	
	@Override
	public PresupuestoOdontologico buscarPresupuestoId(long codigoPk) {
		return delegate.findById(codigoPk);
	}

	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @author Diana Carolina G&oacute;mez :)
	 */
	
	@Override
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(
			DtoReportePresupuestosOdontologicosContratados dto,
			String tSalidaReporte) {
		return delegate.obtenerPresupuestoOdoContratado(dto, tSalidaReporte);
	}


	
	
}
