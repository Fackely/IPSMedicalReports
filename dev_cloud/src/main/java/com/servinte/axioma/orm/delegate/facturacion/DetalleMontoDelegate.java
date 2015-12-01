package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.DetalleMontoHome;

/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con la entidad Detalle Monto
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoDelegate extends DetalleMontoHome {	
		
	/**
	 * 
	 * Este Método se encarga de guardar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(DetalleMonto detalleMonto){
		boolean save = true;					
		try{
			super.persist(detalleMonto);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"detalle de montos de cobro: ",e);
		}				
		return save;				
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleMontoCobro(DetalleMonto detalleMonto){
		boolean save = false;
		try{
			super.merge(detalleMonto);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el detalle de monto de cobro: ",e);
		}		
		return save;
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un detalle de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleMontoCobro(int idDetalleMonto){
		boolean save = true;					
		try{
			DetalleMonto detalleMonto = super.findById(idDetalleMonto);			
			detalleMonto.setActivo(false);
			super.merge(detalleMonto);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo inactivar el registro de " +
					"detalle de monto de cobro: ",e);
		}				
		return save;		
	} 
	
	
	/**
	 * Este Método se encarga de buscar un detalle de monto de cobro
	 * por su id
	 * @param int id 
	 * @return DetalleMonto
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMonto buscarDetalleMontoPorID(int id)
	{
		DetalleMonto detalleMonto = new DetalleMonto();
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DetalleMonto.class,"detalle")
			.createAlias("detalle.montosCobro"			, "montoCobro")		
			.createAlias("detalle.estratosSociales"		, "estratoSocial")
			.createAlias("detalle.tiposPaciente"		, "tiposPaciente")
			.createAlias("detalle.tiposMonto"			, "tiposMonto")
			.createAlias("detalle.naturalezaPacientes"	, "naturalezaPacientes",Criteria.LEFT_JOIN)
			.createAlias("detalle.viasIngreso"			, "viasIngreso")
			.createAlias("detalle.tiposAfiliado"		, "tiposAfiliado");
		
		criteria.add(Restrictions.eq("detalle.detalleCodigo", id));				
		detalleMonto = (DetalleMonto)criteria.uniqueResult();
		
		if(detalleMonto!=null)
		{			
			detalleMonto.getMontosCobro().getCodigo();
			detalleMonto.getEstratosSociales().getCodigo();
			
			detalleMonto.getTiposMonto().getCodigo();
			detalleMonto.getTiposPaciente().getAcronimo();
			detalleMonto.getViasIngreso().getCodigo();
			
			if(detalleMonto.getNaturalezaPacientes()!=null){
				detalleMonto.getNaturalezaPacientes().getCodigo();
			}
		}	
			
		return detalleMonto;
	}
	
}
