package com.servinte.axioma.orm.delegate.facturacion.convenio;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoBusquedaEmisionBonos;
import com.servinte.axioma.orm.BonosConvIngPac;
import com.servinte.axioma.orm.BonosConvIngPacHome;



/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas
 * con la entidad Convenios
 * 
 * @author Cristhian Murillo
 *
 */
@SuppressWarnings("unchecked")
public class BonosConvIngPacDelegate extends BonosConvIngPacHome  {

	
	
	/**
	 * Busca registros de BonosConvIngPac por numero de serial y contrato
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<BonosConvIngPac>
	 */
	public ArrayList<BonosConvIngPac> buscarBonosConvIngPac(DtoBusquedaEmisionBonos dtoBusquedaEmisionBonos)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BonosConvIngPac.class, "bonosConvIngPac");
		
		criteria.createAlias("bonosConvIngPac.conveniosIngresoPaciente"	, "conveniosIngresoPaciente");
		criteria.createAlias("conveniosIngresoPaciente.contratos"		, "contratos"); 
		
		criteria.add(Restrictions.eq("bonosConvIngPac.numeroSerial", dtoBusquedaEmisionBonos.getNumeroSerial()));
		criteria.add(Restrictions.eq("contratos.codigo", dtoBusquedaEmisionBonos.getCodigoContrato()));
		
		return (ArrayList<BonosConvIngPac>)criteria.list();
	}
	
	
	
	
	
	/**
	 * Busca registros de BonosConvIngPac por el convenioIngresoPaciente que sean utilizado = N
	 * @author Cristhian Murillo
	 * @param convenioIngresoPaciente
	 * @return ArrayList<BonosConvIngPac>
	 */
	public ArrayList<BonosConvIngPac> buscarBonosConvIngPacPorConvenioIngreso(long convenioIngresoPaciente)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BonosConvIngPac.class, "bonosConvIngPac");
		
		criteria.createAlias("bonosConvIngPac.conveniosIngresoPaciente"	, "conveniosIngresoPaciente");
		
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.codigoPk", convenioIngresoPaciente));
		criteria.add(Restrictions.eq("bonosConvIngPac.utilizado", ConstantesBD.acronimoNoChar));
		
		return (ArrayList<BonosConvIngPac>)criteria.list();
	}
	
}
