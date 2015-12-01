package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposPacienteDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposPacienteMundo;
import com.servinte.axioma.orm.TiposPaciente;

/**
 * Esta clase se encarga de de ejecutar los métodos para
 * la entidad Tipos de Paciente
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class TiposPacienteMundo implements ITiposPacienteMundo {
	
	/**
	 * Instancia de la clase ITiposPacienteDAO
	 */
	ITiposPacienteDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposPacienteMundo(){
		dao = ManejoPacienteDAOFabrica.crearTiposPacienteDAO();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposPaciente> buscarTiposPaciente(){
		return dao.buscarTiposPaciente();
	}

	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados por via de ingreso
	 * 
	 * @param codigoViaIngreso 
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<TiposPaciente> buscarTiposPacienteXViaIngreso(int codigoViaIngreso){
		return dao.buscarTiposPacienteXViaIngreso(codigoViaIngreso);
	}
}
