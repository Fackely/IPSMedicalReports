package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposAfiliadoDAO;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.delegate.manejoPaciente.TiposAfiliadoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad  Tipos de Afiliado
 * 
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
public class TiposAfiliadoHibernateDAO implements ITiposAfiliadoDAO {
	
	/**
	 * Instancia de la entidad TiposAfiliadoDelegate
	 */
	TiposAfiliadoDelegate delegate;
	
	public TiposAfiliadoHibernateDAO(){
		delegate = new TiposAfiliadoDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado(){
		return delegate.obtenerTiposAfiliado();
		
	}

	/**
	 * Metodo que se encarga de obtener la descripcion del
	 * acronimo de tipo Afiliado
	 * 
	 * @param tipoAfiliado
	 * @return String
	 */
	public String obtenerDescripcionTipoAfiliado(Character tipoAfiliado){
		return delegate.obtenerDescripcionTipoAfiliado(tipoAfiliado);
	}
}
