package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IEstratoSocialMundo;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IEstratoSocialServicio;

/**
 * Esta clase se encarga de ejecutar los m�todos para la entidad
 * Estrato Social
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class EstratoSocialServicio implements IEstratoSocialServicio {
	
	/**
	 * Instancia de IEstratoSocialMundo
	 */
	IEstratoSocialMundo mundo;
	
	public EstratoSocialServicio(){
		mundo = ManejoPacienteFabricaMundo.crearEstratoSocialMundo();
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
		return mundo.consultarEstratosSocilaesPorRegimen(acronimoRegimenConvenio);
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
		return mundo.consultarEstratoSocial();
	}

}
