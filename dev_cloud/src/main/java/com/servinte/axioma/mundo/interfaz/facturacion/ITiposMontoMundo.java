package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposMonto;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad Tipos de Monto
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public interface ITiposMontoMundo {
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de montos
	 * de cobro manejados 
	 * 
	 * @return  ArrayList<TiposMonto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposMonto> consultarTiposMonto();

}
