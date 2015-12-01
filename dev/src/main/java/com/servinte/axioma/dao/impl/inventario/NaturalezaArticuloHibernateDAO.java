/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.inventario.INaturalezaArticuloDAO;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.delegate.inventario.NaturalezaArticuloDelegate;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class NaturalezaArticuloHibernateDAO implements INaturalezaArticuloDAO {
	
	NaturalezaArticuloDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public NaturalezaArticuloHibernateDAO(){
		delegate = new NaturalezaArticuloDelegate();
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar las naturalezas de art�culos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo(){
		return delegate.buscarNaturalezaArticulo();
	}

}
