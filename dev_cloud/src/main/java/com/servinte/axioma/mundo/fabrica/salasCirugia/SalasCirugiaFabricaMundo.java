package com.servinte.axioma.mundo.fabrica.salasCirugia;

import com.servinte.axioma.mundo.impl.salasCirugia.PeticionQxMundo;
import com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo;


/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del módulo SalasCirugia
 * 
 * @author Fabián Becerra
 * @since 30 Junio 2011
 */
public abstract class SalasCirugiaFabricaMundo {

	/**
	 * Método constructor de la clase
	 */
	private SalasCirugiaFabricaMundo(){ }
	
	/**
	 * Método que retorna una de instancia de {@link PeticionQxMundo}
	 * 
	 * @author Fabián Becerra
	 * @return {@link IPeticionQxMundo}
	 */
	public static IPeticionQxMundo crearPeticionQxMundo(){
		return new PeticionQxMundo();				
	}
	
}
