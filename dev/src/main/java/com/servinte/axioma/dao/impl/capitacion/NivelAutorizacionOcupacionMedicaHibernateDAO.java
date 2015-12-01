/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionOcupacionMedicaDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionOcupacionMedicaDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
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
	 * Este método se encarga de consultar los niveles de autorización de
	 * las ocupaciones médicas 
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
	 * Este método se encarga de eliminar los niveles de autorización de
	 * usuario específico 
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
