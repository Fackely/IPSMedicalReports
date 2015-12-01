package com.servinte.axioma.orm.delegate.capitacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.NivelAutorArticulo;
import com.servinte.axioma.orm.NivelAutorArticuloHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de articulos específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoDelegate extends
		NivelAutorArticuloHome {
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de nivel de autorización de un artículo específico
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
					"nivel de autorización de artículo específico: ",e);
		}				
		return save;		
	} 

}
