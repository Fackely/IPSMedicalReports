package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionDAO;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
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
	 * Este Método se encarga de consultar los niveles de autorización
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
	 * Este Método se encarga de consultar nivel de autorización de un usuario
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
	 * Este Método se encarga de consultar nivel de autorización por ocupación medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	@Override
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion){
		return delegate.buscarNivelAutorizacionPorOcupacionMedica(codigoOcupacion);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización
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
	 * Este Método se encarga de actualizar los niveles de autorización
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
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización
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
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelesAutorizacion(){
		return delegate.obtenerNivelesAutorizacion();
	}
}
