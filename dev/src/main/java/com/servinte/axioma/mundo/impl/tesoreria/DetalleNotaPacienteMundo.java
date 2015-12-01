package com.servinte.axioma.mundo.impl.tesoreria;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetalleNotaPacienteDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo;
import com.servinte.axioma.orm.DetalleNotaPaciente;

/**
 * Define la logica de negocio relaciona con 
 * DetalleNotaPaciente
 * @author diecorqu
 * @see IDetalleNotaPacienteMundo
 */
public class DetalleNotaPacienteMundo implements IDetalleNotaPacienteMundo,
		IDetalleNotaPacienteDAO {

	IDetalleNotaPacienteDAO dao;
	
	public DetalleNotaPacienteMundo() {
		dao = TesoreriaFabricaDAO.crearDetalleNotaPacienteDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo#guardarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public boolean guardarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return dao.guardarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo#eliminarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public boolean eliminarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return dao.eliminarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo#modificarDetalleNotaPaciente(com.servinte.axioma.orm.DetalleNotaPaciente)
	 */
	@Override
	public DetalleNotaPaciente modificarDetalleNotaPaciente(
			DetalleNotaPaciente detalleNotaPaciente) {
		return dao.modificarDetalleNotaPaciente(detalleNotaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IDetalleNotaPacienteMundo#findById(long)
	 */
	@Override
	public DetalleNotaPaciente findById(long codigo) {
		return dao.findById(codigo);
	}

}
