/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;

import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.servinte.axioma.orm.EsqTarProcedimientoContrato;
import com.servinte.axioma.orm.EsqTarProcedimientoContratoHome;


public class EsqTarProcedimientoContratoDelegate extends EsqTarProcedimientoContratoHome {
	
	
	/**
	 * 
	 * Este método se encarga de buscar un esquema tarifario
	 * por el id del contrato
	 * 
	 * @param int idContrato
	 * @return EsqTarProcedimientoContrato
	 * @Author Angela Aguirre
	 */
	@SuppressWarnings("unchecked")
	public DTOEsqTarProcedimientoContrato buscarEsquematarifarioPorContrato(DTOEsqTarProcedimientoContrato dto){
		
		DTOEsqTarProcedimientoContrato esquema = new DTOEsqTarProcedimientoContrato();
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
	
				
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(EsqTarProcedimientoContrato.class,"esquema")
		.createAlias("esquema.contratos", "contrato")
		.createAlias("esquema.esquemasTarifarios", "esquemasTarifarios");
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("esquema.codigo")							,"codigo")
				.add(Projections.property("contrato.codigo")						,"contrato")
				.add(Projections.property("esquemasTarifarios.codigo")              ,"codigoEsquema")
				.add(Projections.property("esquema.fechaVigencia")              ,"fechaVigencia")))	;	
		
		criteria.add(Restrictions.eq("contrato.codigo", dto.getContrato().getCodigo()));				
		criteria.add(Restrictions.le("esquema.fechaVigencia", fechaActual));
		
		criteria.addOrder(Order.desc("esquema.fechaVigencia"));
		
				
		Class[] parametros=new Class[4];
		parametros[0]=Long.class;
		parametros[1]=Integer.class;
		parametros[2]=Integer.class;
		parametros[3]=Date.class;
		
		
		Constructor constructor;
		try {
			constructor = DTOEsqTarProcedimientoContrato.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		ArrayList<DTOEsqTarProcedimientoContrato> listaEsquema = (ArrayList<DTOEsqTarProcedimientoContrato>)criteria.list();
		
		if(!Utilidades.isEmpty(listaEsquema)){
			esquema = (DTOEsqTarProcedimientoContrato)criteria.list().get(0);
		}
		//esquema = (DTOEsqTarProcedimientoContrato)criteria.uniqueResult();
						
		return esquema;
	}

}
