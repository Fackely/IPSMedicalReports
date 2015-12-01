package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IEstratoSocialDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IEstratoSocialMundo;
import com.servinte.axioma.orm.EstratosSociales;

/**
 * Esta clase se encarga de definir los m�todos para la entidad
 * Estrato Social
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class EstratoSocialMundo implements IEstratoSocialMundo {
	
	IEstratoSocialDAO dao; 
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public EstratoSocialMundo(){
		dao = ManejoPacienteDAOFabrica.crearEstratoSocialDAO();
	}
	
	
	/**
	 * Este M�todo se encarga de consultar los estratos Sociales
	 * que esten relacionados al r�gimen espec�fico
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratosSocilaesPorRegimen(
			String acronimoRegimenConvenio){
		return dao.consultarEstratosSocilaesPorRegimen(acronimoRegimenConvenio);
	}
	
	/**
	 * Este M�todo se encarga de consultar los estratos Sociales
	 * registrados
	 * 
	 * @param String
	 * @return ArrayList<EstratosSociales>
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<EstratosSociales> consultarEstratoSocial(){
		return dao.consultarEstratoSocial();
	}
}
