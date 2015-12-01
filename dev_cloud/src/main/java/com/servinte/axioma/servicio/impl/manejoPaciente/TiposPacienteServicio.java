package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposPacienteMundo;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposPacienteServicio;

/**
 * Esta clase se encarga de de ejecutar los métodos para
 * la entidad Tipos de Paciente
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class TiposPacienteServicio implements ITiposPacienteServicio {
	
	/**
	 * Instancia de la clase ITiposPacienteMundo
	 */
	ITiposPacienteMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposPacienteServicio(){
		mundo = ManejoPacienteFabricaMundo.crearTiposPacienteMundo();
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
		return mundo.buscarTiposPaciente();
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
		return mundo.buscarTiposPacienteXViaIngreso(codigoViaIngreso);
	}

}
