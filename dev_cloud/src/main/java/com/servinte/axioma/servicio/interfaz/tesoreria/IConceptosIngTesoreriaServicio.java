package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.ConceptosIngTesoreria;

/**
 * Clase encargada de definir los m&eacute;todos
 * de negocio para la entidad ConceptosIngTesoreria
 * @author Diana Carolina G
 *
 */

public interface IConceptosIngTesoreriaServicio {
	
	/**
	 * 
	 * Lista los conceptos de ingreso de tesoreria 
	 * Tipo Ingreso = Anticipos Convenios Odontol&oacute;gicos
	 * @return ArrayList<ConceptosIngTesoreria>
	 * @author Diana Carolina G
	 * 
	 */
	
	public ArrayList<ConceptosIngTesoreria> obtenerConceptosTipoIngAnticiposConvOdont();

}
