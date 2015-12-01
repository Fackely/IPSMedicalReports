package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoServicioEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoServicioEspecificoMundo;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoServicioEspecificoMundo implements
		IMontoServicioEspecificoMundo {
	
	IMontoServicioEspecificoDAO dao;
	
	public MontoServicioEspecificoMundo(){
		dao = FacturacionFabricaDAO.crearMontoServicioEspecificoDAO();
	}
	
	/** 
	 * Este método se encarga de consultar todos los
	 * servicios específicos relacionados al detalle del monto de cobro
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> obtenerServiciosPorDetalleID(int idDetalle,int codigoTarifario){
		return dao.obtenerServiciosPorDetalleID(idDetalle,codigoTarifario);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un servicio específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		return dao.eliminarServicioEspecifico(id);
	}

}
