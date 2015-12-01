package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ITiposContratoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposContratoMundo;
import com.servinte.axioma.orm.TiposContrato;

/**
 * Esta clase se encarga de implementar los métodos de
 * negocio para la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public class TiposContratoMundo implements ITiposContratoMundo {
	
	ITiposContratoDAO dao;
	
	public TiposContratoMundo() {
		dao = FacturacionFabricaDAO.crearTiposContratoDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de contrato
	 * registrados en el sistema
	 *  
	 * @return ArrayList<TiposContrato>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposContrato> consultarTiposContrato(){
		return dao.consultarTiposContrato();
	}

}
