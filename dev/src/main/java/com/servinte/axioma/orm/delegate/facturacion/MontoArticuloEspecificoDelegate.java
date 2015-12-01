package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.MontoArticuloEspecifico;
import com.servinte.axioma.orm.MontoArticuloEspecificoHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoArticuloEspecificoDelegate extends
		MontoArticuloEspecificoHome {
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un artículo específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id){
		boolean save = true;					
		try{
			MontoArticuloEspecifico registro = super.findById(id);		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"artículo específico del detalle de monto de cobro: ",e);
		}				
		return save;		
	} 

}
