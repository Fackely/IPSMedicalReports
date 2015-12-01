package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.CierreTempNivelAtenArtHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenArtDelegate extends CierreTempNivelAtenArtHome {
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y el nivel de atención.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenArt>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempNivelAtenArt> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		
		ArrayList<CierreTempNivelAtenArt> listaCierre=new ArrayList<CierreTempNivelAtenArt>();
		try{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempNivelAtenArt.class, "cierreTempNivelAtenArt");
			
			criteria.createAlias("cierreTempNivelAtenArt.contratos","contrato");
			criteria.createAlias("cierreTempNivelAtenArt.nivelAtencion","nivelAtencion");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato()!=ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCodigoNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
					criteria.add(Restrictions.eq("nivelAtencion.consecutivo",dtoParametros.getCodigoNivelAtencion()));
				}
			}	
					
			listaCierre = (ArrayList<CierreTempNivelAtenArt>)criteria.list();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return listaCierre;
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenArttHome
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenArt cierre) throws BDException{
		boolean save = true;					
		try{
			super.attachDirty(cierre);
		} catch (Exception e) {
			save = false;
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO);
		}				
		return save;				
	}
	
	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el convenio, contrato, Articulo y la fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public Double obtenerValorCierreTemporalNivelArticulos(int codContrato, Date fecha, 
							long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CierreTempNivelAtenArt.class, "cierreTempNivelArticulos")
			.createAlias("cierreTempNivelArticulos.contratos", "contratos")
			.createAlias("cierreTempNivelArticulos.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("cierreTempNivelArticulos.fechaCierre", fecha))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreTempNivelArticulos.valorAcumulado"),"valorAcumulado"));
		
		List<Double> lista = (List<Double>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			resultado = lista.get(0);
		}
		return resultado;
	}
}
