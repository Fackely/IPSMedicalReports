package com.servinte.axioma.dao.impl.odontologia.ventasTarjeta;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentasTarjetaClienteDAO;
import com.servinte.axioma.orm.VentaTarjetaCliente;
import com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta.VentaTarjetaDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class VentasTarjetaClienteDAO  implements IVentasTarjetaClienteDAO{
	
	/**
	 * Delegate 
	 */
	private VentaTarjetaDelegate delegate;
	
	/**
	 * Constructor vacío
	 */
	public VentasTarjetaClienteDAO(){
		delegate=new VentaTarjetaDelegate();
	}
		
	@Override
	public VentaTarjetaCliente buscarxId(Number id) {
		
		try {
			delegate.findById(id.longValue());
		} catch (HibernateException e) {
			Log4JManager.error("Error consultando venta tarjeta cliente", e);
		}
		return null;
	}

	@Override
	public void eliminar(VentaTarjetaCliente objeto) {
		try {
			delegate.delete(objeto);
			
		} catch (HibernateException e) {
			Log4JManager.error("Error eliminando venta tarjeta cliente", e);
		}
		
	}

	@Override
	public void insertar(VentaTarjetaCliente objeto) {
		try {
			delegate.persist(objeto);
		} catch (HibernateException e) {
			Log4JManager.error("Error insertando venta tarjeta cliente", e);
		}
		
	}

	@Override
	public void modificar(VentaTarjetaCliente objeto) {
		try {
			delegate.attachDirty(objeto);
		} catch (HibernateException e) {
			Log4JManager.error("Error modificando venta tarjeta cliente", e);
		}
		
	}

	
}
