package com.servinte.axioma.mundo.impl.facturacion;

import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoGeneralDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoGeneralMundo;
import com.servinte.axioma.orm.DetalleMontoGeneral;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad DetalleMontoGeneral
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoGeneralMundo implements IDetalleMontoGeneralMundo {
	
	IDetalleMontoGeneralDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMontoGeneralMundo(){
		dao = FacturacionFabricaDAO.crearDetalleMontoGeneralDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un detalle
	 * general de un monto de cobro
	 * 
	 * @param DetalleMontoGeneral detalle, Usuarios usuario
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleGeneral(DetalleMontoGeneral detalle, Usuarios usuario){		
		return dao.guardarDetalleGeneral(detalle);
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un detalle general de un monto de cobro
	 * por su id
	 * 
	 * @param int
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMontoGeneral buscarPorID(int id){
		return dao.buscarPorID(id);
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle general de un monto de
	 * cobro
	 * 
	 * @param DetalleMontoGeneral
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleGeneralMontoCobro(DetalleMontoGeneral detalleMonto){
		return dao.actualizarDetalleGeneralMontoCobro(detalleMonto);
	}

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un detalle general de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleGeneralMontoCobro(int idDetalleMonto){
		return dao.eliminarDetalleGeneralMontoCobro(idDetalleMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el valor y tipo de un monto de
	 * cobro
	 * @param int idDetalleMonto
	 * @return DTOMontosCobroDetalleGeneral
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobroDetalleGeneral obtenerValorTipoMonto(int idDetalleMonto ){
		return dao.obtenerValorTipoMonto(idDetalleMonto);
	}

}
