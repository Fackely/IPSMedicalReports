package com.servinte.axioma.orm.delegate.facturacion;

import java.lang.reflect.Constructor;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;

import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.servinte.axioma.orm.DetalleMontoGeneral;
import com.servinte.axioma.orm.DetalleMontoGeneralHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Detalle Monto General
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoGeneralDelegate extends DetalleMontoGeneralHome {
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un detalle
	 * general de un monto de cobor
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleGeneral(DetalleMontoGeneral detalle){
		boolean save = false;
		try{
			super.persist(detalle);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el detalle general del monto de cobro: ",e);
		}		
		return save;
	}	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle general de un monto de
	 * cobro
	 * 
	 * @param DetalleMontoGeneral
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleGeneralMontoCobro(DetalleMontoGeneral detalleMonto){
		boolean save = false;
		try{
			super.merge(detalleMonto);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el detalle general de monto de cobro: ",e);
		}		
		return save;
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un detalle general de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleGeneralMontoCobro(int idDetalleMonto){
		boolean save = true;					
		try{
			DetalleMontoGeneral detalleMonto = super.findById(idDetalleMonto);		
			super.delete(detalleMonto);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"detalle general de monto de cobro: ",e);
		}				
		return save;		
	} 
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el valor y tipo de un monto de
	 * cobro
	 * @param int idDetalleMonto
	 * @return DTOMontosCobroDetalleGeneral
	 * @author, Angela Maria Aguirre
	 */
	public DTOMontosCobroDetalleGeneral obtenerValorTipoMonto(int idDetalleMonto )
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DetalleMontoGeneral.class,"detalleMontoGeneral");
		
		criteria.createAlias("detalleMontoGeneral.detalleMonto"		, "detalleMonto");
		criteria.createAlias("detalleMonto.tiposMonto"				, "tipoMonto");
		
		criteria.add(Restrictions.eq("detalleMonto.detalleCodigo"	, idDetalleMonto));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("tipoMonto.codigo")					,"idTipoMonto")								
				.add(Projections.property("tipoMonto.nombre")					,"nombreTipoMonto")
				.add(Projections.property("detalleMontoGeneral.valor")			,"valorMonto")
				.add(Projections.property("detalleMontoGeneral.porcentaje")		,"porcentajeMonto")
				.add(Projections.property("detalleMontoGeneral.cantidadMonto")	,"cantidadMonto")); 
		
		Class[] parametros=new Class[5];
		parametros[0]=int.class;
		parametros[1]=String.class;
		parametros[2]=Double.class;
		parametros[3]=Double.class;
		parametros[4]=int.class;
		Constructor constructor;
		
		try {
			constructor = DTOMontosCobroDetalleGeneral.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		DTOMontosCobroDetalleGeneral dto = (DTOMontosCobroDetalleGeneral)criteria.uniqueResult();
		
		return dto;		
	}	
	
}
