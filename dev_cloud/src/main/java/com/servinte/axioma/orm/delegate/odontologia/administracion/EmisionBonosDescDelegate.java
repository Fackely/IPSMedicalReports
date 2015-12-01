/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.administracion;

import java.sql.Connection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.EmisionBonosDescDao;
import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;
import com.servinte.axioma.orm.EmisionBonosDesc;
import com.servinte.axioma.orm.EmisionBonosDescHome;

/**
 * @author Juan David Ramírez
 * @since 02 Diciembre 2010
 */
public class EmisionBonosDescDelegate extends EmisionBonosDescHome
{

	/**
	 * Lista los bonos que correspondan a la parametrización del convenio 
	 * y estén dentro de el rango de seriales válidos ya parametrizados.
	 * 
	 * @author Cristhian Murillo
	 * @param dtoBusquedaEmisionBonos
	 * @return ArrayList<EmisionBonosDesc>
	*/
	public EmisionBonosDesc buscarEmisionXBono(DtoBusquedaEmisionBonos dtoBusquedaEmisionBonos)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmisionBonosDesc.class, "emisionBonosDesc");
		
		criteria.createAlias("emisionBonosDesc.instituciones"	, "institucion");
		criteria.createAlias("emisionBonosDesc.convenios"		, "convenios");
		
		criteria.add(Restrictions.eq("convenios.codigo"			, dtoBusquedaEmisionBonos.getCodigoConvenios()));
		criteria.add(Restrictions.eq("institucion.codigo"		, dtoBusquedaEmisionBonos.getCodInstituciones()));
		
		if(dtoBusquedaEmisionBonos.getFecha() != null)
		{
			criteria.add(Restrictions.le("emisionBonosDesc.fechaVigenciaInicial", dtoBusquedaEmisionBonos.getFecha()));
			criteria.add(Restrictions.ge("emisionBonosDesc.fechaVigenciaFinal"	, dtoBusquedaEmisionBonos.getFecha()));
		}
		
		criteria.add(Restrictions.le("emisionBonosDesc.serialInicial", dtoBusquedaEmisionBonos.getNumeroSerial()));
		criteria.add(Restrictions.ge("emisionBonosDesc.serialFinal", dtoBusquedaEmisionBonos.getNumeroSerial()));
		
		return (EmisionBonosDesc)criteria.uniqueResult();
	}

}
