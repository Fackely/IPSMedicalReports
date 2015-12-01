package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IViasIngresoMundo;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class ViasIngresoServicio implements IViasIngresoServicio {
	
	/**
	 * Instancia de IViasIngresoMundo
	 */
	IViasIngresoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ViasIngresoServicio(){
		mundo = ManejoPacienteFabricaMundo.crearViasIngresoMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ViasIngreso> buscarViasIngreso(){
		return mundo.buscarViasIngreso();
	}


	@Override
	public ViasIngreso findbyId(int id) {
		return mundo.findbyId(id);
	}


	

}
