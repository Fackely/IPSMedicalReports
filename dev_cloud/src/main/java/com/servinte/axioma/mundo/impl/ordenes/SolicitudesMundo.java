package com.servinte.axioma.mundo.impl.ordenes;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.servinte.axioma.dao.fabrica.ordenes.OrdenesFabricaDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesDAO;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;
import com.servinte.axioma.orm.Solicitudes;

public class SolicitudesMundo implements ISolicitudesMundo{
	
	ISolicitudesDAO dao;
	
	/**
	  * 
	  * M�todo constructor de la clase
	  * @author, Fabi�n Becerra
	 */
	public SolicitudesMundo() {
		dao = OrdenesFabricaDAO.crearSolicitudesDAO();
	}
	
	/**
	 * Este m�todo consulta las solicitudes en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesEnSistema(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesEnSistema(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las solicitudes anuladas de orden ambulatoria, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesOrdenAmbAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesOrdenAmbAnuladas(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las solicitudes de cirugias en el sistema, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugias(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesCirugias(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las solicitudes de cirugias en el sistema anuladas, dependiendo de los p�rametros de consulta enviados
	 * @param dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugiasAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesCirugiasAnuladas(dtoFiltro);
	}
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Solicitudes
	 */
	public Solicitudes obtenerSolicitudPorId(int id){
		return dao.obtenerSolicitudPorId(id);
	}

	/**
	 * Este m�todo consulta las Solicitudes Medicamentos por Cuenta con tipoSolicitud = Medicamento y 
	 * estadoSolicitud != Anulada y Administrada
	 * @param cuenta
	 * @return true si no contiene solicitudes en otros estados 
	 * @return false si contiene por lo menos una solicitud diferente a anulado o administrado
	 */
	public boolean consultarSolicitudesMedicamentosPorCuenta(int cuenta){
		return dao.consultarSolicitudesMedicamentosPorCuenta(cuenta);
	}
}
