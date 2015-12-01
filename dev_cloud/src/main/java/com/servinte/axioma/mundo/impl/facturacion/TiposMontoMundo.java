package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposMontoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposMontoMundo;
import com.servinte.axioma.orm.TiposMonto;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Tipos de Monto
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class TiposMontoMundo implements ITiposMontoMundo {
	
	ITiposMontoDAO dao = FacturacionFabricaDAO.crearTiposMontoDAO();
	
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
		return dao.consultarTiposMonto();
	}

}
