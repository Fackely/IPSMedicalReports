package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.CierreTempGrupoServicioHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempGrupoServicioDelegate extends
		CierreTempGrupoServicioHome {
	
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempGrupoServicio>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempGrupoServicio> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		
		ArrayList<CierreTempGrupoServicio> listaCierre= new ArrayList<CierreTempGrupoServicio>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempGrupoServicio.class, "cierreTempGrupoServicio");
			
			criteria.createAlias("cierreTempGrupoServicio.contratos","contrato");
			criteria.createAlias("cierreTempGrupoServicio.gruposServicios","grupo");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCodigoGrupoServicio()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("grupo.codigo",dtoParametros.getCodigoGrupoServicio()));
				}
			}	
			
			listaCierre = (ArrayList<CierreTempGrupoServicio>)criteria.list();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
		
		return listaCierre;
		
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempGrupoServicioHome
	 * 
	 * @param CierreTempGrupoServicio cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempGrupoServicio cierre){
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
