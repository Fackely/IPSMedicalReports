package com.servinte.axioma.orm.delegate.capitacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.NivelAutorArticulo;
import com.servinte.axioma.orm.NivelAutorArticuloHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de articulos espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoDelegate extends
		NivelAutorArticuloHome {
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de nivel de autorizaci�n de un art�culo espec�fico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id){
		boolean save = true;					
		try{
			NivelAutorArticulo registro = super.findById(id);		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de autorizaci�n de art�culo espec�fico: ",e);
		}				
		return save;		
	} 

}
