package com.servinte.axioma.dao.impl.odontologia.tipoTarjeta;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.dao.interfaz.odontologia.tipoTarjeta.ITipoTarjetaDAO;
import com.servinte.axioma.orm.TiposTarjCliente;
import com.servinte.axioma.orm.delegate.odontologia.tipoTarjeta.TiposTarjClienteDelegate;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */

public class TipoTarjetaDAO  implements ITipoTarjetaDAO{

	
	/**
	 * Delegate Tipo Tarjeta
	 */
	private TiposTarjClienteDelegate delegate;
	
	/**
	 * Construtor
	 */
	public TipoTarjetaDAO(){}
	
	@Override
	public TiposTarjCliente buscarxId(Number id) {
		
		try {
			
			this.getDelegate().findById(id.longValue());
			
		} catch (HibernateException e) {
			
			Log4JManager.error(e);
		}
		
		return null;
	}

	@Override
	public void eliminar(TiposTarjCliente objeto) {
		try {
			this.getDelegate().delete(objeto);
			
		} catch (HibernateException e) {
			
			Log4JManager.error(e);
		}
		
	}

	@Override
	public void insertar(TiposTarjCliente objeto) {
		
		try {
			this.getDelegate().attachDirty(objeto);
			
		} catch (HibernateException e) {
			
			Log4JManager.error(e);
		}
		
	}

	@Override
	public void modificar(TiposTarjCliente objeto) {
	
		try {
			this.getDelegate().attachDirty(objeto);
		} catch (HibernateException e) {
			
			Log4JManager.error(e);
		}
		
	}
	

	public void setDelegate(TiposTarjClienteDelegate delegate) {
		this.delegate = delegate;
	}

	public TiposTarjClienteDelegate getDelegate() {
		return delegate;
	}

	@Override
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta)
	{
		return new TiposTarjClienteDelegate().consultarTipoTarjetaCliente(tipoTarjeta, claseVenta);
	}

}
