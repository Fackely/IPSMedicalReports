package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo;
import com.servinte.axioma.orm.UsuarioXConvenio;


/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad UsuariosCapitadosMundo 
 * @author Cristhian Murillo
 */
public class UsuarioXConvenioMundo implements IUsuarioXConvenioMundo
{

	IUsuarioXConvenioDAO dao;
	
	
	public UsuarioXConvenioMundo(){
		dao = CapitacionFabricaDAO.crearUsuarioXConvenioDAO();
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo#attachDirty(com.servinte.axioma.orm.UsuarioXConvenio)
	 */
	@Override
	public void attachDirty(UsuarioXConvenio instance) {
		dao.attachDirty(instance);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo#listarTodos()
	 */
	@Override
	public ArrayList<UsuarioXConvenio> listarTodos() {
		return dao.listarTodos();
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo#buscarUsuarioConvenioRecientePorPaciente(int)
	 */
	@Override
	public UsuarioXConvenio buscarUsuarioConvenioRecientePorPaciente(
			int codigoPersona) {
		return dao.buscarUsuarioConvenioRecientePorPaciente(codigoPersona);
	}


	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo#obtenerCarguesPreviosPorPersonaPorRangoFechas(int, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoContrato> obtenerCarguesPreviosPorPersonaPorRangoFechas(
			int codigoPersona, Date fechaInicio, Date fechaFin) {
		return dao.obtenerCarguesPreviosPorPersonaPorRangoFechas(codigoPersona, fechaInicio, fechaFin);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo#existeCargueVigentePorPersonaPorContrato(int, java.util.Date, int)
	 */
	@Override
	public boolean existeCargueVigentePorPersonaPorContrato(int codigoPersona,
			Date fechaActual, int codigoContrato) {
		return dao.existeCargueVigentePorPersonaPorContrato(codigoPersona, fechaActual, codigoContrato);
	}
	
	
}
