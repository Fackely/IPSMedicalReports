package com.servinte.axioma.dao.impl.facturacion;

import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoGeneralDAO;
import com.servinte.axioma.orm.DetalleMontoGeneral;
import com.servinte.axioma.orm.delegate.facturacion.DetalleMontoGeneralDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad DetalleMontoGeneral
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoGeneralHibernateDAO implements IDetalleMontoGeneralDAO {
	
	DetalleMontoGeneralDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMontoGeneralHibernateDAO(){
		delegate = new DetalleMontoGeneralDelegate();		
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un detalle
	 * general de un monto de cobor
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleGeneral(DetalleMontoGeneral detalle){
		return delegate.guardarDetalleGeneral(detalle);
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
		return delegate.findById(id);
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
		return delegate.actualizarDetalleGeneralMontoCobro(detalleMonto);
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
		return delegate.eliminarDetalleGeneralMontoCobro(idDetalleMonto);
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
		return delegate.obtenerValorTipoMonto(idDetalleMonto);
	}

}
