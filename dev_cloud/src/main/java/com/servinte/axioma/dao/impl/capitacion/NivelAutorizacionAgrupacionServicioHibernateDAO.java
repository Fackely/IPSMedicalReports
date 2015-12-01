package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorizacionAgrupacionServicioDAO;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorizacionAgrupacionServicioDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de agrupación de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionServicioHibernateDAO implements
		INivelAutorizacionAgrupacionServicioDAO {

	
	NivelAutorizacionAgrupacionServicioDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public NivelAutorizacionAgrupacionServicioHibernateDAO(){
		delegate= new NivelAutorizacionAgrupacionServicioDelegate();
	}
	
	/** 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de agrupación de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(
			int nivelAutorizacionID){			
		return delegate.buscarNivelAutorizacionServicioArticulo(nivelAutorizacionID);
	}

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de agrupación de servicios
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(
			int idNivelAutorizacionUsuario) {
		return delegate.eliminarNivelAutorizacionServicioArticulo(idNivelAutorizacionUsuario);
	}

}
