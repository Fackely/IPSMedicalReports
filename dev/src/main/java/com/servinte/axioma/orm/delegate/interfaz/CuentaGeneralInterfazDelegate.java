package com.servinte.axioma.orm.delegate.interfaz;

import org.hibernate.criterion.Expression;
import com.servinte.axioma.orm.CuentaGeneralInterfaz;
import com.servinte.axioma.orm.CuentaGeneralInterfazHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene los SQL del modelo 
 */
public class CuentaGeneralInterfazDelegate extends CuentaGeneralInterfazHome
{
	
	
	/**
	 * Retorna una el objeto en espec&iacute;fico por su instituci&oacute;n
	 * @param campoTabla
	 * @param campoDato
	 */
	public CuentaGeneralInterfaz listarUnicoPorInstitucion(int institucion) {
		try {
			return (CuentaGeneralInterfaz) sessionFactory.getCurrentSession()
					.createCriteria(CuentaGeneralInterfaz.class)
					.add(Expression.eq("instituciones.codigo", institucion))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	
	
}
