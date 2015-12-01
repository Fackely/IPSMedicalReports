package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosEstanciaMundo;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosEstanciaServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad ingresos estancia
 * 
 * @author Angela Maria Aguirre
 * @since 14/01/2011
 */
public class IngresosEstanciaServicio implements IIngresosEstanciaServicio {
	
	IIngresosEstanciaMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public IngresosEstanciaServicio(){
		mundo = ManejoPacienteFabricaMundo.crearIngresosEstancia();
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un ingreso estancia
	 * por su ID 
	 * @param long id
	 * @return IngresosEstancia
	 * @author, Angela Maria Aguirre
	 *
	 */
	public IngresosEstancia obtenerIngresoEstanciaPorId(long id){
		return mundo.obtenerIngresoEstanciaPorId(id);
		
	}
	
	 /** 
	 * Este Método se encarga de actualizar el registro de un ingreso estancia
	 * 
	 * @param IngresosEstancia
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarIngresoEstancia(IngresosEstancia ingreso){
		return mundo.actualizarIngresoEstancia(ingreso);
	}


	@Override
	public void attachDirty(IngresosEstancia instance) {
		mundo.attachDirty(instance);
	}


	
	@Override
	public IngresosEstancia findById(long id) {
		return mundo.findById(id);
	}

}
