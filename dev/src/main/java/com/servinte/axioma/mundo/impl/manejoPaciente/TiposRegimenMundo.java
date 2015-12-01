package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposRegimenDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposRegimenMundo;
import com.servinte.axioma.orm.TiposRegimen;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 19/08/2010
 */
public class TiposRegimenMundo implements ITiposRegimenMundo {
		
	/**
	 *Instancia de la entidad  ITiposRegimenDAO
	 */
	ITiposRegimenDAO dao = ManejoPacienteDAOFabrica.crearTipoRegimenDAO();
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Tipos de R&eacute;gimen.
	 * 
	 * @return ArrayList<TiposRegimen>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposRegimen> consultarTiposRegimen(){
		return  dao.consultarTiposRegimen();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar un tipo de 
	 * R&eacute;gimen
	 * 
	 * @param String
	 * @return TiposRegimen
	 * @author, Angela Maria Aguirre
	 *
	 */
	public TiposRegimen findByID(String acronimo){
		return dao.findByID(acronimo);
	}

}
