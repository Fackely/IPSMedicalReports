package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuariosCapitadosDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo;
import com.servinte.axioma.orm.UsuariosCapitados;


/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad UsuariosCapitadosMundo 
 * @author Cristhian Murillo
 */
public class UsuariosCapitadosMundo implements IUsuariosCapitadosMundo
{

	IUsuariosCapitadosDAO dao;
	
	
	public UsuariosCapitadosMundo(){
		dao = CapitacionFabricaDAO.crearUsuariosCapitadosDAO();
	}
	
	
	@Override
	public ArrayList<DtoUsuariosCapitados> buscarUsuariosCapitados(DtoUsuariosCapitados parametrosBusqueda) {
		return dao.buscarUsuariosCapitados(parametrosBusqueda);
	}


	
	@Override
	public void delete(UsuariosCapitados persistentInstance) {
		dao.delete(persistentInstance);
	}


	@Override
	public UsuariosCapitados findById(long id) {
		return dao.findById(id);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo#merge(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public UsuariosCapitados merge(UsuariosCapitados instance) {
		return dao.merge(instance);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo#attachDirty(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void attachDirty(UsuariosCapitados persistentInstance) {
		dao.attachDirty(persistentInstance);
		
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo#attachClean(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void attachClean(UsuariosCapitados persistentInstance) {
		dao.attachClean(persistentInstance);
		
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo#persist(com.servinte.axioma.orm.UsuariosCapitados)
	 */
	@Override
	public void persist(UsuariosCapitados persistentInstance) {
		dao.persist(persistentInstance);
		
	}
	
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo#buscarCodigoUsuarioCapitadoPorTipoNumeroID(java.lang.String, java.lang.String)
	 */
	@Override
	public Long buscarCodigoUsuarioCapitadoPorTipoNumeroID(String tipoId, String numeroId) {
		return dao.buscarCodigoUsuarioCapitadoPorTipoNumeroID(tipoId, numeroId);
	}
}
