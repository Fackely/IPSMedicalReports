package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosEstanciaDAO;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.orm.delegate.manejoPaciente.IngresosEstanciaDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad ingresos estancia
 * 
 * @author Angela Maria Aguirre
 * @since 14/01/2011
 */
public class IngresosEstanciaHibernateDAO implements IIngresosEstanciaDAO {
	
	 IngresosEstanciaDelegate delegate;
	 
	 /**
	  * 
	  * Método constructor de la clase
	  * @author, Angela Maria Aguirre
	  */
	 public IngresosEstanciaHibernateDAO(){
		 delegate = new IngresosEstanciaDelegate();
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
		return delegate.obtenerIngresoEstancia(id);
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
		return delegate.actualizarIngresoEstancia(ingreso);
	}

	
	@Override
	public void attachDirty(IngresosEstancia instance) {
		delegate.attachDirty(instance);
	}


	@Override
	public IngresosEstancia findById(long id) {
		return delegate.findById(id);
	}


}
