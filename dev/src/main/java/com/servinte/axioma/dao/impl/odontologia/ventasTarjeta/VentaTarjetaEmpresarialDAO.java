package com.servinte.axioma.dao.impl.odontologia.ventasTarjeta;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentaTarjetaEmpresarialDAO;
import com.servinte.axioma.orm.VentaEmpresarial;
import com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta.VentaEmpresarialDelegate;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class VentaTarjetaEmpresarialDAO  implements IVentaTarjetaEmpresarialDAO  {
	
	/**
	 * Delegate
	 */
	private VentaEmpresarialDelegate delegate;
	
	
	/**
	 * Construtor
	 */
	public VentaTarjetaEmpresarialDAO(){
		delegate= new VentaEmpresarialDelegate();
	}
	
	
	

	@Override
	public VentaEmpresarial buscarxId(Number id) {
	
		VentaEmpresarial ventaEmpresarial= new VentaEmpresarial();
		
		try {
			
			ventaEmpresarial=delegate.findById(id.longValue());
			
		} catch (HibernateException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return ventaEmpresarial;
	}

	
	
	@Override
	public void eliminar(VentaEmpresarial objeto) {
	try {
			delegate.delete(objeto);
			
		} catch (HibernateException e) {
				Log4JManager.info(e);
				Log4JManager.error(e);
				
			}
	}

	@Override
	public void insertar(VentaEmpresarial objeto) {
	
		try {
			delegate.attachDirty(objeto);
			
		} catch (HibernateException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
			
	}

	@Override
	public void modificar(VentaEmpresarial objeto) {
		
		try {
			delegate.attachDirty(objeto);
			
		} catch (HibernateException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
	}
	
	
	
	
	
	
	

}
