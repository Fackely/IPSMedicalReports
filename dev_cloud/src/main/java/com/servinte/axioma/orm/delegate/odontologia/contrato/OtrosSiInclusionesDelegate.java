package com.servinte.axioma.orm.delegate.odontologia.contrato;

import com.servinte.axioma.orm.OtrosSiInclusiones;
import com.servinte.axioma.orm.OtrosSiInclusionesHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class OtrosSiInclusionesDelegate extends OtrosSiInclusionesHome {

	
	/**
	 * Implementacion del método persist
	 */
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance) {
		super.persist(transientInstance);
	}
	
	
}
