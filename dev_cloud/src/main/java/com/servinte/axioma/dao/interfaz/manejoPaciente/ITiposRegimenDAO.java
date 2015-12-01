package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposRegimen;


/**
 * Esta clase se encarga de definir los m&eacute;todos de
 * negocio para la entidad Tipos de R&eacute;gimen
 * 
 * @author Angela Maria Aguirre
 * @since 19/08/2010
 */
public interface ITiposRegimenDAO {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Tipos de R&eacute;gimen del sistema
	 * 
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposRegimen> consultarTiposRegimen();

	
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
	public TiposRegimen findByID(String acronimo);
}
