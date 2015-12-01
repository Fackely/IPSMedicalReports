package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoAgrupacionServiciosDAO;
import com.servinte.axioma.orm.delegate.facturacion.MontoAgrupacionServiciosDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoAgrupacionServiciosHibernateDAO implements
		IMontoAgrupacionServiciosDAO {
	
	MontoAgrupacionServiciosDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public MontoAgrupacionServiciosHibernateDAO(){
		delegate = new MontoAgrupacionServiciosDelegate();
	}
	
	/**
	 * 
	 * Este método se encarga de consultar todos las agrupaciones
	 * de servicios relacionadas a un detalle específico
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoAgrupacionServicio>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionServicio> obtenerServiciosPorDetalleID(int idDetalle){
		return delegate.obtenerServiciosPorDetalleID(idDetalle);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de una agrupación de servicios de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarAgrupacionServicio(int id){
		return delegate.eliminarAgrupacionServicio(id);
	}

}
