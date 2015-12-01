package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposPaciente;

/**
 * Esta clase se encarga de definir los métodos de 
 * negocio para la entidad Tipos de Paciente
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public interface ITiposPacienteServicio {
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposPaciente> buscarTiposPaciente();
	
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados por via de ingreso
	 * 
	 * @param codigoViaIngreso 
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<TiposPaciente> buscarTiposPacienteXViaIngreso(int codigoViaIngreso);

}
