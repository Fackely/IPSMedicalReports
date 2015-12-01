package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.EstratosSociales;

/**
 * Esta clase se encarga de definir los métodos para la entidad
 * Estrato Social
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public interface IEstratoSocialDAO {
	
		
	/**
	 * Este Método se encarga de consultar los estratos Sociales
	 * que esten relacionados al régimen específico
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratosSocilaesPorRegimen(
			String acronimoRegimenConvenio);
	
	/**
	 * Este Método se encarga de consultar los estratos Sociales
	 * registrados
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratoSocial();

}
