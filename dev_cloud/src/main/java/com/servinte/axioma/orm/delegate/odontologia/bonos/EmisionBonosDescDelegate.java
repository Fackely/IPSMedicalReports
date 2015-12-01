package com.servinte.axioma.orm.delegate.odontologia.bonos;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.servinte.axioma.orm.EmisionBonosDesc;
import com.servinte.axioma.orm.EmisionBonosDescHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene lógica del negocio sobre el modelo 
 */
public class EmisionBonosDescDelegate extends EmisionBonosDescHome {

	
	/**
	 * Lista los bonos que correspondan a la parametrización del convenio 
	 * y esten dentro de el rango de seriales validos ya parametrizados.
	 * 
	 * @author Cristhian Murillo
	 * @param dtoBusquedaEmisionBonos
	 * @return ArrayList<EmisionBonosDesc>
	*/
	public EmisionBonosDesc buscarSerialBono(DtoBusquedaEmisionBonos dtoBusquedaEmisionBonos)
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
