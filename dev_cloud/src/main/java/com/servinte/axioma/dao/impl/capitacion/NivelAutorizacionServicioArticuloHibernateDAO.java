package com.servinte.axioma.dao.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionServicioArticuloDAO;
import com.servinte.axioma.orm.NivelAutorServMedic;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionServicioArticuloDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de Servicios y Artículos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioArticuloHibernateDAO implements
		INivelAutorizacionServicioArticuloDAO {
	
	NivelAutorizacionServicioArticuloDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionServicioArticuloHibernateDAO(){
		delegate = new NivelAutorizacionServicioArticuloDelegate();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID){
		return delegate.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}

	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return delegate.guardarNivelAutorizacionServicioArticulo(registro);
	}
	

	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de servicios y artículos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		return delegate.actualizarNivelAutorizacionServicioArticulo(registro);
	}


	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicios y artículos
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario){
		return delegate.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}

}
