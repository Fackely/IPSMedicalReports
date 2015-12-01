package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosEstanciaDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosEstanciaMundo;
import com.servinte.axioma.orm.IngresosEstancia;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad ingresos estancia
 * 
 * @author Angela Maria Aguirre
 * @since 14/01/2011
 */
public class IngresosEstanciaMundo implements IIngresosEstanciaMundo {
	
	IIngresosEstanciaDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public IngresosEstanciaMundo(){
		dao = ManejoPacienteDAOFabrica.crearIngresosEstancia();
		
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
		return dao.obtenerIngresoEstanciaPorId( id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el registro de un ingreso estancia
	 * 
	 * @param IngresosEstancia
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarIngresoEstancia(IngresosEstancia ingreso){
		return dao.actualizarIngresoEstancia(ingreso);
	}


	
	@Override
	public void attachDirty(IngresosEstancia instance) {
		dao.attachDirty(instance);
	}


	@Override
	public IngresosEstancia findById(long id) {
		return dao.findById(id);
	}


}
