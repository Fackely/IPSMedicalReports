/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.mantenimiento;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.servinte.axioma.orm.DescuentosOdon;
import com.servinte.axioma.orm.DescuentosOdonHome;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class DescuentosOdonDelegate extends DescuentosOdonHome
{

	/**
	 * Valida si existen descuentos odontológicos por presupuesto
	 * @param centroAtencion Centro de atención donde se desean consultar
	 * @param fechaVigencia Fecha de vigencia de las promociones
	 * @return Lista con los descuentos vigentes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXPresupuesto(int centroAtencion, Date fechaVigencia)
	{
		return (ArrayList<DtoDescuentosOdontologicos>)sessionFactory.getCurrentSession().createCriteria(DescuentosOdon.class)
		
		.add(Expression.eq("centroAtencion.consecutivo",centroAtencion))
		.add(Expression.le("fechaIniVigencia", fechaVigencia))
		.add(Expression.ge("fechaFinVigencia", fechaVigencia))

		
		.setProjection(Projections.projectionList().add(Projections.property("fechaIniVigencia"), "fechaInicioVigencia"))
		.setProjection(Projections.projectionList().add(Projections.property("fechaFinVigencia"), "fechaFinVigencia"))
		.setProjection(Projections.projectionList().add(Projections.property("consecutivo"), "consecutivo"))
		.setResultTransformer( Transformers.aliasToBean(DtoDescuentosOdontologicos.class)).list();
	}
	
}
