/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionOcupacionMedicaDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionOcupacionMedicaDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * para la entidad NivelAutorizacionOcupacionMedica
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionOcupacionMedicaHibernateDAO implements
		INivelAutorizacionOcupacionMedicaDAO {
	
	NivelAutorizacionOcupacionMedicaDelegate delegate;
	
	public NivelAutorizacionOcupacionMedicaHibernateDAO(){
		delegate = new NivelAutorizacionOcupacionMedicaDelegate();
	}

	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * las ocupaciones m�dicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	@Override
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id){
		return delegate.buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(id);
		
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
	@Override
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id){
		return delegate.eliminarNivelAutorizacionOcupacionMedica(id);
	}


}
