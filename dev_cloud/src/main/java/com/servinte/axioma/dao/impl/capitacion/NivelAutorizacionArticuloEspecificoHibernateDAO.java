package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionArticuloEspecificoDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionArticuloEspecificoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de articulos específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoHibernateDAO implements
		INivelAutorizacionArticuloEspecificoDAO {
	
	NivelAutorizacionArticuloEspecificoDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionArticuloEspecificoHibernateDAO(){
		delegate = new NivelAutorizacionArticuloEspecificoDelegate();
	}

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
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id) {
		return delegate.eliminarNivelAutorizacionArticuloEspecifico(id);
	}

}
