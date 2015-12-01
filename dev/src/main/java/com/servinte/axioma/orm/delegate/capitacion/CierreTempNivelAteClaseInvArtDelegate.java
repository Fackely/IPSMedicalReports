package com.servinte.axioma.orm.delegate.capitacion;

import java.math.BigDecimal;
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
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.CierreTempNivAteClInvArtHome;


/**
 * Esta clase se encarga de manejar la logica de acceso a datos
 * de la entidad CierreTempNivelAteClaseInvArtHome
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public class CierreTempNivelAteClaseInvArtDelegate extends  CierreTempNivAteClInvArtHome{
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * articulo según el contrato, el nivel de atención y la clase de inventario dada.
	 * 
	 * @author Ricardo Ruiz
	 * @param dtoParametros
	 * @return ArrayList<CierreTempNivelAteClaseInvArt>
	 * @throws BDException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CierreTempNivAteClInvArt> buscarCierreTemporalNivelAtencionClaseInventarioArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		
		ArrayList<CierreTempNivAteClInvArt> listaCierre=new ArrayList<CierreTempNivAteClInvArt>();
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					CierreTempNivAteClInvArt.class, "cierreTempNivelAteClaseInvArt");
			
			criteria.createAlias("cierreTempNivelAteClaseInvArt.contratos","contrato");
			criteria.createAlias("cierreTempNivelAteClaseInvArt.nivelAtencion","niveleAtencion");
			criteria.createAlias("cierreTempNivelAteClaseInvArt.claseInventario","claseInventario");
			
			if(dtoParametros!=null){
				if(dtoParametros.getCodigoContrato() != ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("contrato.codigo",dtoParametros.getCodigoContrato()));
				}
				if(dtoParametros.getCodigoClaseInventario() != ConstantesBD.codigoNuncaValido){
					criteria.add(Restrictions.eq("claseInventario.codigo",dtoParametros.getCodigoClaseInventario()));		
				}
				if(dtoParametros.getCodigoNivelAtencion() != ConstantesBD.codigoNuncaValidoLong){
					criteria.add(Restrictions.eq("niveleAtencion.consecutivo",dtoParametros.getCodigoNivelAtencion()));
				}
			}				
			listaCierre = (ArrayList<CierreTempNivAteClInvArt>)criteria.list();
		} catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
		return listaCierre;
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteClaseInvArtHome
	 * 
	 * @param CierreTempNivelAteClaseInvArt cierre
	 * @return boolean
	 * @author Ricardo Ruiz 
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivAteClInvArt cierre) throws BDException{
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
	 * para el Contrato, Clase de Inventario del Articulo, Nivel de Atención
	 * y la Fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param codigoClaseInventario
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public Double obtenerValorCierreTemporalNivelClaseInventarioArticulo(int codContrato, Date fecha, 
							int codigoClaseInventario, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(CierreTempNivAteClInvArt.class, "cierreTempNivelClaseInventario")
			.createAlias("cierreTempNivelClaseInventario.contratos", "contratos")
			.createAlias("cierreTempNivelClaseInventario.nivelAtencion", "nivelAtencion")
			.createAlias("cierreTempNivelClaseInventario.claseInventario", "claseInventario");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("cierreTempNivelClaseInventario.fechaCierre", fecha))
				.add(Restrictions.eq("claseInventario.codigo", codigoClaseInventario))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("cierreTempNivelClaseInventario.valorAcumulado"),"valorAcumulado"));
		
		List<BigDecimal> lista = (List<BigDecimal>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			BigDecimal valorAcumulado = lista.get(0);
			resultado = valorAcumulado.doubleValue();
		}
		return resultado;
	}

}
