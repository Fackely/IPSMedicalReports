package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;


/**
 * Servicio que le delega al negocio las operaciones relacionados con 
 * las Entidades Financieras 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.servicio.impl.tesoreria.
 */


public interface IEntidadesFinancierasServicio {

	
	/**
	 * 
	 * Retorna un listado con las entidades que est&aacute;n asociadas a una instituci&oacute;n dada
	 * y dependiendo del valor del parametro activo, si lo estan o no
	 * 
	 * @param codigoInstitucion
	 * @param activo
	 * @return listado con las entidades que est&aacute;n asociadas a una instituci&oacute;n dada
	 */
	public List<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo);
	
	
	/**
	 * Retorna un listado con las Entidades Financieras que se encuentran registradas en el sistema
	 * 
	 * @param todas
	 * @return Listado con las Entidades Financieras que se encuentran registradas en el sistema
	 * @see DtoEntidadesFinancieras
	 */
	public ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) ;
	
}
