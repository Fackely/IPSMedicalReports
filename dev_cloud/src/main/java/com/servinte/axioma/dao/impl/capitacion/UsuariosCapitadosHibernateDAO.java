/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO;
import com.servinte.axioma.orm.UsuariosCapitados;
import com.servinte.axioma.orm.delegate.capitacion.UsuariosCapitadosDelegate;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad UsuariosCapitados
 * @author Cristhian Murillo
 */
public class UsuariosCapitadosHibernateDAO implements IUsuariosCapitadosDAO
{
	
	UsuariosCapitadosDelegate delegate;
	
	
	/**
	 * Método constructor de la clase
	 */
	public UsuariosCapitadosHibernateDAO(){
		delegate = new UsuariosCapitadosDelegate();
	}
	
	
	@Override
	public ArrayList<DtoUsuariosCapitados> buscarUsuariosCapitados(DtoUsuariosCapitados parametrosBusqueda) {
		return delegate.buscarUsuariosCapitados(parametrosBusqueda);
	}


	@Override
	public void delete(UsuariosCapitados persistentInstance) {
		delegate.delete(persistentInstance);
	}


	@Override
	public UsuariosCapitados findById(long id) {
		return delegate.findById(id);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO#merge(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public UsuariosCapitados merge(UsuariosCapitados instance) {
		return delegate.merge(instance);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO#attachDirty(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void attachDirty(UsuariosCapitados persistentInstance) {
		delegate.attachDirty(persistentInstance);
		
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO#attachClean(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void attachClean(UsuariosCapitados persistentInstance) {
		delegate.attachClean(persistentInstance);
		
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO#persist(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void persist(UsuariosCapitados persistentInstance) {
		delegate.persist(persistentInstance);
		
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO#buscarCodigoUsuarioCapitadoPorTipoNumeroID(java.lang.String, java.lang.String)
	 */
	@Override
	public Long buscarCodigoUsuarioCapitadoPorTipoNumeroID(String tipoId,
			String numeroId) {
		return delegate.buscarCodigoUsuarioCapitadoPorTipoNumeroID(tipoId, numeroId);
	}

}
