package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.TarifasEntidadSub;
import com.servinte.axioma.orm.TarifasEntidadSubHome;

/**
 * Esta clase se encarga de manejar las transacciones relacionadas
 * con la entidad TarifasEntidadSub
 * 
 * @author Diana Ruiz
 * @since 20/01/2012
 */
public class TarifasEntidadSubDelegate extends TarifasEntidadSubHome {
		
	/**
	 * 
	 * Implementación del metodo persist de la super clase TarifasEntidadSubHome
	 * 
	 * @param TarifasEntidadSub tarifasEntidadSub
	 * @return boolean
	 * 
	 * @author, Diana Ruiz
	 *
	 */
	public boolean sincronizarTarifasEntidadSub(TarifasEntidadSub tarifasEntidadSub){
		boolean save = true;					
		try{
			super.persist(tarifasEntidadSub);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"Tarifas entidades subcontratadas: ",e);
		}				
		return save;				
	}
	
	
	
	
}
