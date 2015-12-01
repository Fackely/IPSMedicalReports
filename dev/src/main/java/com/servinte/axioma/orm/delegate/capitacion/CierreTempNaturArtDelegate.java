package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.orm.CierreTempNaturArt;
import com.servinte.axioma.orm.CierreTempNaturArtHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNaturArtDelegate extends CierreTempNaturArtHome {
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y  la naturaleza de artículo dada.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNaturArt>
	 */
	public ArrayList<CierreTempNaturArt> buscarCierreTemporalNaturalezaArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CierreTempNaturArt.class, "cierreTempNaturArt");
		
		criteria.createAlias("cierreTempNaturArt.contratos","contrato");
		criteria.createAlias("cierreTempNaturArt.naturalezaArticulo","naturaleza");
		
		if(dtoParametros!=null){
			if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
				criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
			}
			if(dtoParametros.getNaturalezaID()!=null){
				criteria.add(Restrictions.eq("naturaleza.id.acronimo",
						dtoParametros.getNaturalezaID().getAcronimo()));
				criteria.add(Restrictions.eq("naturaleza.id.institucion",
						dtoParametros.getNaturalezaID().getInstitucion()));				
			}
		}		
	
		ArrayList<CierreTempNaturArt> listaCierre = (ArrayList<CierreTempNaturArt>)criteria.list();
		return listaCierre;
	}
		
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNaturArtHome
	 * 
	 * @param CierreTempNaturArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNaturArt cierre){
		boolean save = true;					
		try{
			super.attachDirty(cierre);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro de " +
					"cierre temporal de servicios y artículos: ",e);
		}				
		return save;				
	}


}
