package com.servinte.axioma.dao.impl.odontologia.ventasTarjeta;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioDAO;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta.BeneficiariosDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class BeneficiarioDAO  implements IBeneficiarioDAO{
	
	/**
	 * Delegado del Home de BeneficiariosTarjetaCliente
	 */
	private BeneficiariosDelegate delegate;
	
	public BeneficiarioDAO(){
		delegate= new BeneficiariosDelegate();
	}

	@Override
	public BeneficiarioTarjetaCliente buscarxId(Number id) {
		BeneficiarioTarjetaCliente beneficiario = new BeneficiarioTarjetaCliente();
		
		try {
			beneficiario=delegate.findById(id.longValue());
		}
		catch (HibernateException e) {
			Log4JManager.error("Error al consultar el beneficiario de la tarjeta cliente", e);
		}
		
		return beneficiario;
	}

	@Override
	public void eliminar(BeneficiarioTarjetaCliente objeto) {
		try {
			delegate.delete(objeto);
			
		} catch (HibernateException e) {
			Log4JManager.error("Error al eliminar el beneficiario de la tarjeta cliente", e);
		}
		
	}
	
	@Override
	public void insertar(BeneficiarioTarjetaCliente objeto) {
		try {
			delegate.persist(objeto);
		} catch (Exception e) {
			Log4JManager.error("Error al insertar el beneficiario de la tarjeta cliente", e);
		}
	}

	@Override
	public void modificar(BeneficiarioTarjetaCliente objeto) {
		 try {
			 delegate.attachDirty(objeto);
		} catch (Exception e) {
			Log4JManager.error("Error al insertar el beneficiario de la tarjeta cliente", e);
		}
		
	}
	
	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas)
	{
		return delegate.obtenerBeneficiarioPersona(codigoPersona, filtrarTarjetasActivas);
	}

	@Override
	public boolean existeSerial(Long serial, long codigoTipoTarjeta) {
		return delegate.existeSerial(serial, codigoTipoTarjeta);
	}


	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(
			int codigoPersonas) {
		
		return delegate.obtenerBeneficiarioPersona(codigoPersonas);
	}

}
