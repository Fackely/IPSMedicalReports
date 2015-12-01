package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioEspecificoDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionServicioEspecificoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de servicios específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioEspecificoHibernateDAO implements
		INivelAutorizacionServicioEspecificoDAO {
	
	NivelAutorizacionServicioEspecificoDelegate delegate;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicioEspecificoHibernateDAO(){
		delegate = new NivelAutorizacionServicioEspecificoDelegate();
	}
		
	
	/**
	 * 
	 * Este método se encarga de consultar todos los
	 * niveles de autorización por servicios específicos 
	 * a través del id del nivel de autorización de serivicios -
	 * artículos relacionado
	 * 
	 * @param int id
	 * @return ArrayList<DTOBusquedaNivelAutorServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> buscarNivelAutorizacionServicios(int id,int codigoTarifario){
		return delegate.buscarNivelAutorizacionServicios(id, codigoTarifario);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicio específico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		
		return delegate.eliminarServicioEspecifico(id);
	}

}
