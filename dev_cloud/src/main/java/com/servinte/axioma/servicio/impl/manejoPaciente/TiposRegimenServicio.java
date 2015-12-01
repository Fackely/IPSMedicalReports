package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposRegimenMundo;
import com.servinte.axioma.orm.TiposRegimen;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposRegimenServicio;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Tipos de R&eacute;gimen
 * 
 * @author Angela Maria Aguirre
 * @since 19/08/2010
 */
public class TiposRegimenServicio implements ITiposRegimenServicio {
	
	/**
	 * Instancia de la entidad ITiposRegimenMundo 
	 */
	ITiposRegimenMundo mundo = ManejoPacienteFabricaMundo.crearTipoRegimenMundo();
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Tipos de R&eacute;gimen.
	 * 
	 * @return ArrayList<TiposRegimen>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposRegimen> consultarTiposRegimen(){
		return  mundo.consultarTiposRegimen();
	}
	
	/** 
	 * Este M&eacute;todo se encarga de consultar un tipo de 
	 * R&eacute;gimen
	 * 
	 * @param String
	 * @return TiposRegimen
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")	
	public TiposRegimen findByID(String acronimo){
		return mundo.findByID(acronimo);
	}

}
