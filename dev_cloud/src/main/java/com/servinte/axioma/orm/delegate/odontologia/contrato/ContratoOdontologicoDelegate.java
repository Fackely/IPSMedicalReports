package com.servinte.axioma.orm.delegate.odontologia.contrato;

import java.util.Iterator;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.ContratoOdontologicoHome;
import com.servinte.axioma.orm.FirmasContratoOtrosiInst;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ContratoOdontologicoDelegate extends ContratoOdontologicoHome 
{

	
	/**
	 * CONSULTA AVANZADA DE CONTRATO ODONTOLOGICO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoContrato
	 * @return
	 */
	public ContratoOdontologico consultarAvanzadaContratoOdon(ContratoOdontologico dtoContrato )
	{
		
		
		ContratoOdontologico dtoTmp = new ContratoOdontologico();
		
		
		Criteria criterio=sessionFactory.getCurrentSession().createCriteria(ContratoOdontologico.class);
		
		
		if(dtoContrato.getInstituciones()!=null && dtoContrato.getInstituciones().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("instituciones.codigo", dtoContrato.getInstituciones().getCodigo()));
		}
		
		if(dtoContrato.getCodigoPk()>0)
		{
			criterio.add(Restrictions.eq("codigoPk",dtoContrato.getCodigoPk()));
		}
		
		
		/*
		 *TODO OJO MANEJAR LA EXCEPTION  
		 */
		
		try
		{
			dtoTmp=  (ContratoOdontologico) criterio.uniqueResult();
			
			
			/*
			 *CONSULTAR INFORMACION DE LA FIRMAS  
			 */
			if(dtoTmp!=null){
				if(dtoTmp.getFirmasContratoOtrosiInsts()!=null )
				{
					Iterator it =dtoTmp.getFirmasContratoOtrosiInsts().iterator();
					
					while( it.hasNext() ) 
					{
						FirmasContratoOtrosiInst dtoFirmas = (FirmasContratoOtrosiInst)it.next();
					}
				}
			}
			
		}
		catch(Exception e)
		{
			Log4JManager.error(e);
			Log4JManager.info(e);
		}
		
		
		
		
		return  dtoTmp ;
	}
	
	
	
	
	
	
	
	
	
	
}
