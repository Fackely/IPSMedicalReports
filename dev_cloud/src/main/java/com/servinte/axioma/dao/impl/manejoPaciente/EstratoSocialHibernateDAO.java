package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.IEstratoSocialDAO;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.delegate.manejoPaciente.EstratoSocialDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class EstratoSocialHibernateDAO implements IEstratoSocialDAO {
	
	/**
	 * Instancia de EstratoSocialDelegate
	 */
	EstratoSocialDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 * @author, Angela Maria Aguirre
	 */
	public EstratoSocialHibernateDAO(){
		delegate = new EstratoSocialDelegate();
	}
	
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
			String acronimoRegimenConvenio){
		return delegate.consultarEstratosSocilaesPorRegimen(acronimoRegimenConvenio);
	}
	
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
	public ArrayList<EstratosSociales> consultarEstratoSocial(){
		return delegate.consultarEstratoSocial();
	}

}
