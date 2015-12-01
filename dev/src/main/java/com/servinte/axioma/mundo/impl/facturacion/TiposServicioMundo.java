package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposServicioDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposServicioMundo;
import com.servinte.axioma.orm.TiposServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class TiposServicioMundo implements ITiposServicioMundo {
	ITiposServicioDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public TiposServicioMundo(){
		dao = FacturacionFabricaDAO.crearTipoServicioDAO();
	}	
	
	/**
	 * 	
	 * Este Método se encarga de consultar los tipos de servicios
	 * en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposServicio> buscarTiposServicio(){
		return dao.buscarTiposServicio();
	}

}
