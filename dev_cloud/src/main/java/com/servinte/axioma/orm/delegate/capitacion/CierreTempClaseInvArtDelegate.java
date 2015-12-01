package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.CierreTempClaseInvArtHome;

/**
 * Esta clase se encarga de gestionar la persistencia de los objetos de
 * la entidad CierreTempClaseInvArt
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public class CierreTempClaseInvArtDelegate extends CierreTempClaseInvArtHome {
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y  la clase de inventario dada.
	 * 
	 * @author Ricardo Ruiz
	 * @param dtoParametros
	 * @return ArrayList<CierreTempNaturArt>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempClaseInvArt> buscarCierreTemporalClaseInventarioArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		
		ArrayList<CierreTempClaseInvArt> listaCierreTemp= new ArrayList<CierreTempClaseInvArt>();
		
		try{
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempClaseInvArt.class, "cierreTempClaseInvArt");
			
			criteria.createAlias("cierreTempClaseInvArt.contratos","contrato");
			criteria.createAlias("cierreTempClaseInvArt.claseInventario","claseInventario");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato() != ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCodigoClaseInventario() != ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("claseInventario.codigo",
							dtoParametros.getCodigoClaseInventario()));			
				}
			}		
			listaCierreTemp = (ArrayList<CierreTempClaseInvArt>)criteria.list();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
		return listaCierreTemp;
	}
		
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempClaseInvArtHome
	 * 
	 * @param CierreTempClaseInvArt cierre
	 * @return boolean
	 * @author Ricardo Ruiz 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempClaseInvArt cierre)throws BDException{ 
		boolean save = true;					
		try{
			super.attachDirty(cierre);
		} catch (Exception e) {
			save = false;
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}				
		return save;				
	}


}
