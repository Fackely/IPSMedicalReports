package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;
import com.servinte.axioma.orm.PresupuestoContratado;
import com.servinte.axioma.orm.PresupuestoContratadoHome;

/**
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since  08/09/2010
 *
 */
public class PresupuestoContratadoDelegate extends PresupuestoContratadoHome {
	
	/**
	 * Este m&eacute;todo se encarga de obtener los n&uacute;meros de contratos
	 * para los presupuestos ingresados como par&aacute;metros.
	 * @param dto contiene los presupuestos contratados de los cuales se
	 * quere obtener el n&uacute;mero de contrato.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	
	public List<DtoPresupuestoContratado> obtenerContratosPresupuestoOdonto(DtoPresupuestoContratado dto){
		
		Criteria criteria= sessionFactory.getCurrentSession()
		.createCriteria(PresupuestoContratado.class);
		
		criteria.setProjection(Projections.projectionList()
		.add(Projections.property("consecutivo"), "consecutivoContrato"))
		
		.add(Restrictions.eq("codigoPkPresupuesto", dto.getCodigoPresupuesto()))
		.setResultTransformer(Transformers.aliasToBean(DtoPresupuestoContratado.class));
		List<DtoPresupuestoContratado> lista = criteria.list();
		
		return lista;
		
	}
	
	/*
	public static void main(String[] args) {
		PresupuestoContratadoDelegate contratadoDelegate = new PresupuestoContratadoDelegate();
		
		DtoPresupuestoContratado dto = new DtoPresupuestoContratado();
		Long codigoPresupuesto= 10L;
		dto.setCodigoPresupuesto(codigoPresupuesto);
		contratadoDelegate.obtenerContratosPresupuestoOdonto(dto);
	}
	*/
	
}
