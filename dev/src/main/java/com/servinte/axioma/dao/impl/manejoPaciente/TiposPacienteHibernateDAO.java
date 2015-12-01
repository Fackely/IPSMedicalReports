package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposPacienteDAO;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.delegate.manejoPaciente.TiposPacienteDelegate;

/**
 * Esta clase se encarga de de ejecutar los métodos para
 * la entidad Tipos de Paciente
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class TiposPacienteHibernateDAO implements ITiposPacienteDAO {
	
	/**
	 * Instancia de la clase TiposPacienteDelegate
	 */
	TiposPacienteDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposPacienteHibernateDAO(){
		delegate = new TiposPacienteDelegate();
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
		return delegate.buscarTiposPaciente();
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
		return delegate.buscarTiposPacienteXViaIngreso(codigoViaIngreso);
	}

}
