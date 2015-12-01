package com.servinte.axioma.dao.fabrica.salasCirugia;

import com.servinte.axioma.dao.impl.salasCirugia.PeticionQxHibernateDAO;
import com.servinte.axioma.dao.interfaz.salasCirugia.IPeticionQxDAO;

/**
 * Esta clase se encarga de crear las 
 * instancias necesarias del DAO para SalasCirugia
 * 
 * @author Fabi�n Becerra
 * @since 30 Junio 2011
 *
 */
public abstract class SalasCirugiaFabricaDAO {
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * 
	 */
	private SalasCirugiaFabricaDAO(){ }	
	
	/**
	 * M�todo que retorna una de instancia de {@link PeticionQxDAO }
	 * 
	 * @author Fabi�n Becerra
	 * @return IPeticionQxDAO
	 */
	public static IPeticionQxDAO crearPeticionQxDAO(){
		return new PeticionQxHibernateDAO();				
	}

}
