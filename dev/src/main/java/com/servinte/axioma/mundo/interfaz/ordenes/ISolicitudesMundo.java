package com.servinte.axioma.mundo.interfaz.ordenes;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.servinte.axioma.orm.Solicitudes;

public interface ISolicitudesMundo {

	/**
	 * Este m�todo consulta las solicitudes en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesEnSistema(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este m�todo consulta las solicitudes anuladas de orden ambulatoria, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 * @author hermorhu
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
	 * Implementacion del findById
	 * @param id
	 * @return Solicitudes
	 */
	public Solicitudes obtenerSolicitudPorId(int id);
	
	/**
	 * Este m�todo consulta las Solicitudes Medicamentos por Cuenta con tipoSolicitud = Medicamento y 
	 * estadoSolicitud != Anulada y Administrada
	 * @param cuenta
	 * @return true si no contiene solicitudes en otros estados 
	 * @return false si contiene por lo menos una solicitud diferente a anulado o administrado
	 */
	public boolean consultarSolicitudesMedicamentosPorCuenta(int cuenta);
}
