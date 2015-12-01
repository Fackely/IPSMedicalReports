package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.orm.AdjuntosMovimientosCajaHome;


/**
 * 

 * @author Carolina Gomez
 * @since 25/07/2010
 */
@SuppressWarnings("unchecked")
public class AdjuntosMovimientosCajaDelegate extends AdjuntosMovimientosCajaHome {
	
	
	/**
	 * 
	 * @param codigoMovimiento
	 * @return
	 */
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(long codigoMovimiento ){
		
		Criteria criterio =sessionFactory.getCurrentSession().createCriteria(AdjuntosMovimientosCaja.class );
		
		criterio.add(Restrictions.eq("movimientosCaja.codigoPk", codigoMovimiento));
	
		return criterio.list();
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de consultar los documentos de soporte
	 * del detalle de un movimiento de caja.
	 * @param idMovimiento identificador del movimiento de caja.
	 * @return DTOArchivoAdjunto
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DTOArchivoAdjunto> consultarDocumentosSoporteAdjuntos(long idMovimiento){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AdjuntosMovimientosCaja.class, "adjunto")
		.createAlias("adjunto.movimientosCaja", "movimiento");
		criteria.add(Restrictions.eq("movimiento.codigoPk",idMovimiento));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("adjunto.fecha"),"fecha")
				.add(Projections.property("adjunto.nombreOriginal"),"nombreOriginal")
				.add(Projections.property("adjunto.nombreGenerado"),"nombreGenerado")
		)
		
		.setResultTransformer( Transformers.aliasToBean(DTOArchivoAdjunto.class));
		ArrayList<DTOArchivoAdjunto> listaAdjuntos=(ArrayList)criteria.list();
		
		return listaAdjuntos;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de almacenar el archivo adjunto que el usuario ha 
	 * confirmado.
	 * @param dtoAdjuntos
	 * @return Boolean retorna true si el adjunto ha sido guardado con &eacute;xito.
	 * 
	 * @author Yennifer Guerrero
	 */
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja adjunto){
		
		boolean save = false;
		try{
			super.persist(adjunto);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar el adjunto ",e);
		}		
		return save;
		
	}
}
