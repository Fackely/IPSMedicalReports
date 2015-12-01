package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.CierreTempServArt;
import com.servinte.axioma.orm.CierreTempServArtHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempServArtDelegate extends CierreTempServArtHome {
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo o servicio, según el contrato.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempServArt>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempServArt> buscarCierreTemporalServicioArticulo(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException {
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempServArt.class, "cierreTempServArt");
			
			criteria.createAlias("cierreTempServArt.contratos","contrato");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCierreServicio() != null && (dtoParametros.getCierreServicio().charValue()==ConstantesBD.acronimoSiChar || 
						dtoParametros.getCierreServicio().charValue()==ConstantesBD.acronimoNoChar)){
					criteria.add(Restrictions.eq("cierreTempServArt.cierreServicio",dtoParametros.getCierreServicio()));
				}
			}

			ArrayList<CierreTempServArt> listaCierre = (ArrayList<CierreTempServArt>)criteria.list();
			return listaCierre;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
		
		
		
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempServArtHome
	 * 
	 * @param CierreTempServArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempServArt cierre) throws BDException{
		boolean save = true;					
		try{
			super.attachDirty(cierre);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
		return save;				
	}




	

}
