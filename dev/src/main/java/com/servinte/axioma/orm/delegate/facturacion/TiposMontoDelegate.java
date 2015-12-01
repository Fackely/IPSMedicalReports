package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.TiposMontoHome;

/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con la entidad Tipos de Monto
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class TiposMontoDelegate extends TiposMontoHome {
	
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
		ArrayList<TiposMonto> lista = (ArrayList<TiposMonto>)sessionFactory.getCurrentSession().
			createCriteria(TiposMonto.class).list();		
		
		return lista;		 
	}

}
