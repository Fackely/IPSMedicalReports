package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.dao.interfaz.tesoreria.IAdjuntoMovimientosCajaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.AdjuntosMovimientosCajaDelegate;


/**
 * 
 * Implementaci&oacute;n de la interfaz {@link IAdjuntoMovimientosCajaDAO}.
 * 
 * @since 25/07/2010
 */
public class AdjuntoMovimientosCajaDAO implements IAdjuntoMovimientosCajaDAO {
	

	private AdjuntosMovimientosCajaDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 */
	public AdjuntoMovimientosCajaDAO(){
		delegate= new AdjuntosMovimientosCajaDelegate();
		
	}
	

	@Override
	public void insertar(AdjuntosMovimientosCaja objeto) {
		try {
			delegate.attachDirty(objeto);
			
		} catch (HibernateException e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		
		
	}

	@Override
	public void modificar(AdjuntosMovimientosCaja objeto) {
		delegate.attachDirty(objeto);
		
	}

	@Override
	public void eliminar(AdjuntosMovimientosCaja objeto) {
		delegate.delete(objeto);
	}

	@Override
	public AdjuntosMovimientosCaja buscarxId(Number id) {
		
		return delegate.findById(id.longValue());
	}

	@Override
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(
			long codigoMovimiento) {
		
		return delegate.listaAdjuntoMovimientosCaja(codigoMovimiento);
	}
	
	@Override
	public ArrayList<DTOArchivoAdjunto> consultarDocumentosSoporteAdjuntos(long idMovimiento){
		return delegate.consultarDocumentosSoporteAdjuntos(idMovimiento);
	}
	
	@Override
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja adjunto){
		
		return delegate.guardarAdjuntosMovimientosCaja(adjunto);
	}
	
	

}
