package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.servinte.axioma.orm.IngresosEstancia;

/**
 * Esta clase se encarga de definir los métodos de 
 * negocio de la entidad ingresos estancia
 * 
 * @author Angela Maria Aguirre
 * @since 14/01/2011
 */
public interface IIngresosEstanciaServicio 
{
	
	/**
	 * 
	 * Este Método se encarga de buscar un ingreso estancia
	 * por su ID 
	 * @param long id
	 * @return IngresosEstancia
	 * @author, Angela Maria Aguirre
	 *
	 */
	public IngresosEstancia obtenerIngresoEstanciaPorId(long id);
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el registro de un ingreso estancia
	 * 
	 * @param IngresosEstancia
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarIngresoEstancia(IngresosEstancia ingreso);


	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(IngresosEstancia instance);
	
	
	
	/**
	 * findById
	 * @param id
	 * @return IngresosEstancia
	 */
	public IngresosEstancia findById(long id);
	
	
}
