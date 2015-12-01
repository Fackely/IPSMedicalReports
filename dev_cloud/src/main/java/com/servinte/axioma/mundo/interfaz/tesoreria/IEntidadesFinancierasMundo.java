package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;


/**
 * Define la l&oacute;gica de negocio relacionada con los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.EntidadesFinancierasMundo
 */

public interface IEntidadesFinancierasMundo {

	
	/**
	 * Retorna un listado con las entidades que est&aacute;n asociadas a una instituci&oacute;n dada
	 * @param codigoInstitucion
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
