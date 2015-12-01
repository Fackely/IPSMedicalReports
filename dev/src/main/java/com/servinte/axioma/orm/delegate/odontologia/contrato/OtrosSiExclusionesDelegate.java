package com.servinte.axioma.orm.delegate.odontologia.contrato;

import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiExclusionesHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class OtrosSiExclusionesDelegate extends OtrosSiExclusionesHome {

	
	
	/**
	 * Implementacion del m�todo persist
	 */
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance) {
		super.persist(transientInstance);
	}
	
}
