package com.servinte.axioma.dao.interfaz.ordenes;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.orm.Solicitudes;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de ISolicitudesDAO 
 * 
 * @author Cristhian Murillo 
 */
public interface ISolicitudesDAO 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Solicitudes
	 */
	public Solicitudes obtenerSolicitudPorId(int id);
	
	
	/**
	 * Retorna las solicitudes por cuenta.
	 * Hace un filtro por los parametros recibidos de DtoSolicitud.
	 * Si la Solicitud No tenga asociadas Autorizaciones Capitaci�n Subcontrada o si tienen  se encuentre en estado Anulada.
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado);
	
	
	
	/**
	 * Retorna las solicitudes por el n�mero de esta.
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros);
	
	
	/**
	 * Este m�todo consulta las solicitudes en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesEnSistema(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este m�todo consulta las solicitudes en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesOrdenAmbAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este m�todo consulta las solicitudes de cirugias en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugias(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este m�todo consulta las solicitudes de cirugias en el sistema anuladas, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugiasAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	
	/**
	 * Este m�todo consulta las Solicitudes Medicamentos por Cuenta con tipoSolicitud = Medicamento y 
	 * estadoSolicitud != Anulada y Administrada
	 * @param cuenta
	 * @return true si no contiene solicitudes en otros estados 
	 * @return false si contiene por lo menos una solicitud diferente a anulado o administrado
	 */
	public boolean consultarSolicitudesMedicamentosPorCuenta(int cuenta);
	
	
	
	/**
	 * @param solicitud
	 * @return la solicitud con articulos ya autorizados
	 */
	public DtoSolicitud obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(DtoSolicitud solicitud);
	
	/**
	 * @param solicitud
	 * @param parametros
	 * @return lista de solcitudes 
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadas(DtoSolicitud solicitud,Integer[] parametros);
	
	/**
	 * Reporte de Policonsultadores
	 * 
	 * @author Cesar Gomez
	 * @return ArrayList<DtoPacientesPoliconsultadores>
	*/
	public ArrayList<DtoPacientesPoliconsultadores> obtenerReportePoliconsultadores( 
					String fechaInicial, 
					String fechaFinal, 
					int idConvenio, 
					String tipoIdentificacion, 
					String numeroIdentificacion, 
					int idViaIngreso,
					int idEspecialidad,
					int idUnidadAgenda,
					String tipoServicio,Integer codigoPersona );	
}
