package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.orm.CierreTempNivelAteNatArt;
import com.servinte.axioma.orm.CierreTempNivelAteNatArtHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteNatArtDelegate extends  CierreTempNivelAteNatArtHome{
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * articulo según el contrato, el nivel de atención y la naturaleza dada.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteNatArt>
	 */
	public ArrayList<CierreTempNivelAteNatArt> buscarCierreTemporalNivelAtencionNaturServicio(DTOBusquedaCierreTemporalArticulo dtoParametros){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CierreTempNivelAteNatArt.class, "cierreTempNivelAteNatArt");
		
		criteria.createAlias("cierreTempNivelAteNatArt.contratos","contrato");
		criteria.createAlias("cierreTempNivelAteNatArt.nivelAtencion","niveleAtencion");
		criteria.createAlias("cierreTempNivelAteNatArt.naturalezaArticulo","naturalezaArticulo");
		
		if(dtoParametros!=null){
			if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
				criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
			}
			if(dtoParametros.getNaturalezaID()!=null){
				criteria.add(Restrictions.eq("naturalezaArticulo.id.acronimo",dtoParametros.getNaturalezaID().getAcronimo()));
				criteria.add(Restrictions.eq("naturalezaArticulo.id.institucion",dtoParametros.getNaturalezaID().getInstitucion()));		
			}
			if(dtoParametros.getCodigoNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
				criteria.add(Restrictions.eq("niveleAtencion.consecutivo",dtoParametros.getCodigoNivelAtencion()));
			}
		}				
		
		ArrayList<CierreTempNivelAteNatArt> listaCierre = (ArrayList<CierreTempNivelAteNatArt>)criteria.list();
		return listaCierre;
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteNatArtHome
	 * 
	 * @param CierreTempNivelAteNatArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteNatArt cierre){
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
