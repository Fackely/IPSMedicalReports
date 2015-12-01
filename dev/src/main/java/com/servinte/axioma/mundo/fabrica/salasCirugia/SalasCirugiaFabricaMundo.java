package com.servinte.axioma.mundo.fabrica.salasCirugia;

import com.servinte.axioma.mundo.impl.salasCirugia.PeticionQxMundo;
import com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo;


/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del m�dulo SalasCirugia
 * 
 * @author Fabi�n Becerra
 * @since 30 Junio 2011
 */
public abstract class SalasCirugiaFabricaMundo {

	/**
	 * M�todo constructor de la clase
	 */
	private SalasCirugiaFabricaMundo(){ }
	
	/**
	 * M�todo que retorna una de instancia de {@link PeticionQxMundo}
	 * 
	 * @author Fabi�n Becerra
	 * @return {@link IPeticionQxMundo}
	 */
	public static IPeticionQxMundo crearPeticionQxMundo(){
		return new PeticionQxMundo();				
	}
	
}
