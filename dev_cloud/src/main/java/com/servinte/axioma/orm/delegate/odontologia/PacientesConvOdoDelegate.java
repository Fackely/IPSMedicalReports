package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.odontologia.DtoBusquedaPacientesConvOdo;
import com.servinte.axioma.orm.PacientesConvOdo;
import com.servinte.axioma.orm.PacientesConvOdoHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class PacientesConvOdoDelegate extends PacientesConvOdoHome{
	
	
	
	
	/**
	 * Retorna en caso de encontrar elementos que coinciden con los aprametros de busqueda enviados.
	 * Este metodo es usado para hacer la validacion en base de datos que requiere el ingreso de un paciente con convenio.
	 * 
	 * @author Cristhian Murillo
	 * @param dtoBusquedaPacientesConvOdo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PacientesConvOdo> validacionEnBaseDatosIngresoPaciente(DtoBusquedaPacientesConvOdo dtoBusquedaPacientesConvOdo)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PacientesConvOdo.class);
		
		criteria.add(Restrictions.eq("id.convenio"				, dtoBusquedaPacientesConvOdo.getCodigoConvenio()));
		criteria.add(Restrictions.eq("id.contrato"				, dtoBusquedaPacientesConvOdo.getCodContrato()));
		criteria.add(Restrictions.eq("id.tipoIdentificacion"	, dtoBusquedaPacientesConvOdo.getTipoIdPaciente()));
		criteria.add(Restrictions.eq("id.numeroIdentificacion"	, dtoBusquedaPacientesConvOdo.getNumeroIdPaciente()));
		criteria.add(Restrictions.le("id.fechaIniVigencia"		, dtoBusquedaPacientesConvOdo.getFechaActual()));
		criteria.add(Restrictions.ge("id.fechaFinVigencia"		, dtoBusquedaPacientesConvOdo.getFechaActual()));
		
		return (ArrayList<PacientesConvOdo>)criteria.list();
	}
	
}
