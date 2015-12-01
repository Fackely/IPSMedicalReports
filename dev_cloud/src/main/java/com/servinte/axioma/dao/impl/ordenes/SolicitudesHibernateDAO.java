package com.servinte.axioma.dao.impl.ordenes;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.delegate.ordenes.SolicitudesDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IRegistroEntregaEntSubDAO}.
 * @author Cristhian Murillo
 */
public class SolicitudesHibernateDAO implements ISolicitudesDAO 
{
	
	private SolicitudesDelegate solicitudesDelegate;
	
	
	
	/**
	 * Constructor
	 */
	public SolicitudesHibernateDAO(){
		solicitudesDelegate	= new SolicitudesDelegate();
	}


	
	@Override
	public Solicitudes obtenerSolicitudPorId(int id) {
		return solicitudesDelegate.obtenerSolicitudPorId(id);
	}



	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado) {
		return solicitudesDelegate.obtenerSolicitudesPorCuentaORango(parametros,codigoEstado);
	}



	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros) {
		return solicitudesDelegate.obtenerSolicitudesSubcuenta(parametros);
	}

	@Override
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesEnSistema(DtoProcesoPresupuestoCapitado dtoFiltro){
		return solicitudesDelegate.consultarSolicitudesEnSistema(dtoFiltro);
	}
	
	@Override
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesOrdenAmbAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		return solicitudesDelegate.consultarSolicitudesOrdenAmbAnuladas(dtoFiltro);
	}
	
	/**
	 * Este método consulta las solicitudes de cirugias en el sistema, dependiendo de los párametros de consulta enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugias(DtoProcesoPresupuestoCapitado dtoFiltro){
		return solicitudesDelegate.consultarSolicitudesCirugias(dtoFiltro);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO#consultarSolicitudesCirugiasAnuladas(com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado)
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugiasAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		return solicitudesDelegate.consultarSolicitudesCirugiasAnuladas(dtoFiltro);
	}

	/**
	 * Este método consulta las Solicitudes Medicamentos por Cuenta con tipoSolicitud = Medicamento y 
	 * estadoSolicitud != Anulada y Administrada
	 * @param cuenta
	 * @return true si no contiene solicitudes en otros estados 
	 * @return false si contiene por lo menos una solicitud diferente a anulado o administrado
	 */
	public boolean consultarSolicitudesMedicamentosPorCuenta(int cuenta){
		return solicitudesDelegate.consultarSolicitudesMedicamentosPorCuenta(cuenta);
	}



	/**
	 * @see com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO#obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(com.princetonsa.dto.ordenes.DtoSolicitud)
	 */
	@Override
	public DtoSolicitud obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(
			DtoSolicitud solicitud) {
		return solicitudesDelegate.obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(solicitud);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO#obtenerSolciitudesAutorizadas(com.princetonsa.dto.ordenes.DtoSolicitud, java.lang.Integer[])
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadas(DtoSolicitud solicitud,Integer[] parametros){
		return solicitudesDelegate.obtenerSolciitudesAutorizadas(solicitud, parametros);
	}
	
	public ArrayList<DtoPacientesPoliconsultadores> obtenerReportePoliconsultadores( 
			String fechaInicial, 
			String fechaFinal,
			int idConvenio,
			String tipoIdentificacion, 
			String numeroIdentificacion,
			int idViaIngreso,
			int idEspecialidad,
			int idUnidadAgenda,
			String tipoServicio ,Integer codigoPersona){
		return solicitudesDelegate.obtenerReportePoliconsultadores(fechaInicial, fechaFinal, idConvenio, tipoIdentificacion, numeroIdentificacion, idViaIngreso, idEspecialidad, idUnidadAgenda, tipoServicio,codigoPersona);
	}	
	
}
