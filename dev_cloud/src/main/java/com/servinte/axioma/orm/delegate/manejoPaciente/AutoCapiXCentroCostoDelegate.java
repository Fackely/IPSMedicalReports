package com.servinte.axioma.orm.delegate.manejoPaciente;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.AutoCapiXCentroCosto;
import com.servinte.axioma.orm.AutoCapiXCentroCostoHome;


/**
 * Esta clase se encarga de manejar las transacciones relacionadas
 * con la entidad AutorizacionCapitacionXCentroCosto
 * 
 * @author Diana Ruiz
 * @since 14/02/2012
 */
public class AutoCapiXCentroCostoDelegate extends AutoCapiXCentroCostoHome
{
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
	public boolean sincronizarAutoCapiXCentroCosto(AutoCapiXCentroCosto autoCapiXCentroCosto){
		/**
		 * Se elimina la captura de la excepción en este nivel, ya que no se manejan excepciones por cada transacción
		 * de esta manera se deja la captura de la excepción en el mundo.
		 */
		boolean save = true;					
			super.persist(autoCapiXCentroCosto);
		return save;				
	}		
	
}
