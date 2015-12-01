package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposRegimenDAO;
import com.servinte.axioma.orm.TiposRegimen;
import com.servinte.axioma.orm.delegate.manejoPaciente.TiposRegimenDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad  Tipos de R&eacute;gimen.
 * 
 * @author Angela Maria Aguirre
 * @since 19/08/2010
 */
public class TiposRegimenHibernateDAO implements ITiposRegimenDAO {
	
	/**
	 * Instancia de la entidad TiposRegimenDelegate 
	 */
	TiposRegimenDelegate delegate = new TiposRegimenDelegate();
	
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
		return  delegate.consultarTiposRegimen();
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
	public TiposRegimen findByID(String acronimo){
		return delegate.findById(acronimo);
	}

}
