/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionUsuarioEspecificoDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionUsuarioEspecificoHibernateDAO implements
		INivelAutorizacionUsuarioEspecificoDAO {
	
	NivelAutorizacionUsuarioEspecificoDelegate delegate;
	
	public NivelAutorizacionUsuarioEspecificoHibernateDAO(){
		delegate = new NivelAutorizacionUsuarioEspecificoDelegate();
	}
	
	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> 
			buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(int id){
		return delegate.buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(id);		
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionUsuarioEspecifico(int id){
		return delegate.eliminarNivelAutorizacionUsuarioEspecifico(id);
	}

}
