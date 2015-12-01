package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.ITiposMontoDAO;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.delegate.facturacion.TiposMontoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos para
 * la entidad Tipos de Monto
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class TiposMontoHibernateDAO implements ITiposMontoDAO {
	
	TiposMontoDelegate delegate = new TiposMontoDelegate();
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de montos
	 * de cobro manejados 
	 * 
	 * @return  ArrayList<TiposMonto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMonto> consultarTiposMonto(){
		return delegate.consultarTiposMonto();
	}

}
