package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionDAO;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionHibernateDAO implements INivelAutorizacionDAO {
	
	NivelAutorDelegate delegate;
	
	public NivelAutorizacionHibernateDAO(){
		delegate = new NivelAutorDelegate();
	}

	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrada en el sistema
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTONivelAutorizacion> buscarNivelAutorizacion(){
		return delegate.buscarNivelAutorizacion();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n de un usuario
	 * 
	 * @return ArrayList<DtoValidacionNivelesAutorizacion>
	 * @author, Fabian Becerra
	 *
	 */
	@Override	
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorUsuario(String login){
		return delegate.buscarNivelAutorizacionPorUsuario(login);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n por ocupaci�n medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabi�n Becerra
	 *
	 */
	@Override
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion){
		return delegate.buscarNivelAutorizacionPorOcupacionMedica(codigoOcupacion);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacion(NivelAutorizacion registro){
		return delegate.guardarNivelAutorizacion(registro);
	}

	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarNivelAutorizacion(NivelAutorizacion registro){
		return delegate.actualizarNivelAutorizacion(registro);
	}

	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean eliminarNivelAutorizacion(int idNivelAutorizacion){
		return delegate.eliminarNivelAutorizacion(idNivelAutorizacion);
	}

	/**
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelesAutorizacion(){
		return delegate.obtenerNivelesAutorizacion();
	}
}
