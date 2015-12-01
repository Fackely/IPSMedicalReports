package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionArticuloEspecificoDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionArticuloEspecificoDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de articulos espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionArticuloEspecificoHibernateDAO implements
		INivelAutorizacionArticuloEspecificoDAO {
	
	NivelAutorizacionArticuloEspecificoDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionArticuloEspecificoHibernateDAO(){
		delegate = new NivelAutorizacionArticuloEspecificoDelegate();
	}

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
	public boolean eliminarNivelAutorizacionArticuloEspecifico(int id) {
		return delegate.eliminarNivelAutorizacionArticuloEspecifico(id);
	}

}
