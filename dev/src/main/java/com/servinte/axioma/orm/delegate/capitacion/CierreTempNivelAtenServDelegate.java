package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.CierreTempNivelAtenServHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenServDelegate extends
		CierreTempNivelAtenServHome {
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenServ>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempNivelAtenServ> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CierreTempNivelAtenServ.class, "cierreTempNivelAtenServ");
		
		criteria.createAlias("cierreTempNivelAtenServ.contratos","contrato");
		criteria.createAlias("cierreTempNivelAtenServ.nivelAtencion","nivelAtencion");
		
		if(dtoParametros!=null){
			if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
				criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
			}
			if(dtoParametros.getCodigoNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
				criteria.add(Restrictions.eq("nivelAtencion.consecutivo",dtoParametros.getCodigoNivelAtencion()));
			}
		}
		ArrayList<CierreTempNivelAtenServ> listaCierre = (ArrayList<CierreTempNivelAtenServ>)criteria.list();
		
		return listaCierre;						
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenServHome
	 * 
	 * @param CierreTempServArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenServ cierre){
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

	
	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el convenio, contrato, Servicio y la fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public Double obtenerValorCierreTemporalNivelServicios(int codContrato, Date fecha, 
							long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CierreTempNivelAtenServ.class, "cierreTempNivelServicios")
			.createAlias("cierreTempNivelServicios.contratos", "contratos")
			.createAlias("cierreTempNivelServicios.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("cierreTempNivelServicios.fechaCierre", fecha))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreTempNivelServicios.valorAcumulado"),"valorAcumulado"));
		
		List<Double> lista = (List<Double>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			resultado = lista.get(0);
		}
		return resultado;
	}
}
