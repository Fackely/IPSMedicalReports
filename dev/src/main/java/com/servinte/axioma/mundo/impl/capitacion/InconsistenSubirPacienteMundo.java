package com.servinte.axioma.mundo.impl.capitacion;

import java.util.List;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IInconsistenSubirPacienteDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo;
import com.servinte.axioma.orm.InconsistenSubirPaciente;

public class InconsistenSubirPacienteMundo implements IInconsistenSubirPacienteMundo{

	IInconsistenSubirPacienteDAO dao;
	
	/**
	 * Metodo constructor de la clase
	 * @author Camilo G�mez
	 */
	public InconsistenSubirPacienteMundo (){
		dao = CapitacionFabricaDAO.crearInconsistenSubirPacienteDAO();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenSubirPaciente
	 * 
	 * @param InconsistenSubirPaciente 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente){
		return dao.guardarInconsistenciaSubirPaciente(inconsistenSubirPaciente);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo#buscarInconsistenciasPorLog(long)
	 */
	@Override
	public List<InconsistenSubirPaciente> buscarInconsistenciasPorLog(long codigoLogSubirPaciente) {
		return dao.buscarInconsistenciasPorLog(codigoLogSubirPaciente);
	}
}
