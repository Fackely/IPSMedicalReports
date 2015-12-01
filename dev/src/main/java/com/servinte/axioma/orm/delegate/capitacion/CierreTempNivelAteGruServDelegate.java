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
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.CierreTempNivelAteGruServHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteGruServDelegate extends
		CierreTempNivelAteGruServHome {
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato, el nivel de atención y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteGruServ>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempNivelAteGruServ> buscarCierreTemporalNivelAtencionGrupoServicio(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		
		ArrayList<CierreTempNivelAteGruServ> listaCierre=new ArrayList<CierreTempNivelAteGruServ>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempNivelAteGruServ.class, "cierreTempNivelAteGruServ");
			
			criteria.createAlias("cierreTempNivelAteGruServ.contratos","contrato");
			criteria.createAlias("cierreTempNivelAteGruServ.nivelAtencion","nivelAtencion");
			criteria.createAlias("cierreTempNivelAteGruServ.gruposServicios","grupoServicio");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCodigoGrupoServicio()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("grupoServicio.codigo",dtoParametros.getCodigoGrupoServicio()));
				}
				if(dtoParametros.getCodigoNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
					criteria.add(Restrictions.eq("nivelAtencion.consecutivo",dtoParametros.getCodigoNivelAtencion()));
				}
			}			

			listaCierre = (ArrayList<CierreTempNivelAteGruServ>)criteria.list();
			return listaCierre;
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO);
		}
		
	}
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteGruServHome
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteGruServ cierre){
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
	 * para el Contrato, Grupo de Servicio, Nivel de Atención y la Fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public Double obtenerValorCierreTemporalNivelGrupoServicios(int codContrato, Date fecha, 
							int codigoGrupoServicio, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CierreTempNivelAteGruServ.class, "cierreTempNivelGrupoServicios")
			.createAlias("cierreTempNivelGrupoServicios.contratos", "contratos")
			.createAlias("cierreTempNivelGrupoServicios.nivelAtencion", "nivelAtencion")
			.createAlias("cierreTempNivelGrupoServicios.gruposServicios", "grupoServicio");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("cierreTempNivelGrupoServicios.fechaCierre", fecha))
				.add(Restrictions.eq("grupoServicio.codigo", codigoGrupoServicio))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreTempNivelGrupoServicios.valorAcumulado"),"valorAcumulado"));
		
		List<Double> lista = (List<Double>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			resultado = lista.get(0);
		}
		return resultado;
	}

}
