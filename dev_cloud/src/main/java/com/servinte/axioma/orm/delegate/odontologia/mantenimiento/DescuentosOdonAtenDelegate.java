/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.mantenimiento;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.servinte.axioma.orm.DescuentosOdonAten;
import com.servinte.axioma.orm.DescuentosOdonAtenHome;

/**
 * @author Juan David Ramírez
 * @since Jan 13, 2011
 */
public class DescuentosOdonAtenDelegate extends DescuentosOdonAtenHome
{

	/**
	 * Valida si existen descuentos odontológicos por atención
	 * @param centroAtencion Centro de atención donde se desean consultar
	 * @return Lista con los descuentos existentes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoDescuentosOdontologicos> validarExistenciaDescuentosOdontologicosXAtencion(int centroAtencion)
	{
		return (ArrayList<DtoDescuentosOdontologicos>)sessionFactory.getCurrentSession().createCriteria(DescuentosOdonAten.class)
		
		.add(Expression.eq("centroAtencion.consecutivo",centroAtencion))

		.setProjection(Projections.projectionList().add(Projections.property("consecutivo"), "consecutivo"))
		.setResultTransformer( Transformers.aliasToBean(DtoDescuentosOdontologicos.class)).list();
	}

}
