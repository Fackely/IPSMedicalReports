package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadesDAO;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.delegate.administracion.EspecialidadesDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class EspecialidadesHibernateDAO implements IEspecialidadesDAO {
	
	EspecialidadesDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public EspecialidadesHibernateDAO(){
		delegate = new EspecialidadesDelegate();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar las especialidades
	 * registradas en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Especialidades> buscarEspecialidades(){
		return delegate.buscarEspecialidades();
	}

	/**
	 * 
	 * M&eacute;todo encargado de listar las especialidades registradas en el
	 * sistema en orden alfab&eacute;tico
	 * 
	 * @return ArrayList<Especialidades>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Especialidades> listarEspecialidadesEnOrden() {
		return delegate.listarEspecialidadesEnOrden();
	}

}
