package com.servinte.axioma.orm.delegate.odontologia.recomendaciones;



import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomSerproSerproHome;


/**
 * 
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionSerProgSerProgDelegate extends  RecomSerproSerproHome 
{
	
	
	
	 	/**
	 	 * BUSCAR  LAS RECOMENDACIONES POR SERVICIO O POR PROGRAMA
	 	 * @author Edgar Carvajal Ruiz
	 	 * @param dtoReSerProg
	 	 * @return
	 	 */
		public List<RecomSerproSerpro> listaRecomendacionesxSerProg(RecomSerproSerpro dtoReSerProg)
	
		{	
			
			boolean  validarFiltro=Boolean.FALSE;
				
	
			Criteria criterio = sessionFactory.getCurrentSession().createCriteria(RecomSerproSerpro.class);
			
			
			
			if(dtoReSerProg.getProgramas() !=null && dtoReSerProg.getProgramas().getCodigo()>0)
			{
				criterio.add(Restrictions.eq("programas.codigo", dtoReSerProg.getProgramas().getCodigo()));
				validarFiltro=Boolean.TRUE;
			}
			
			
			
			if(dtoReSerProg.getServicios()!=null && dtoReSerProg.getServicios().getCodigo()>0)
			{
				criterio.add(Restrictions.eq("servicios.codigo",dtoReSerProg.getServicios().getCodigo() ));
				validarFiltro=Boolean.TRUE;
			}
		
			
			List<RecomSerproSerpro> tmp =null;
			
			
			if(validarFiltro)
			{
				 tmp =  (List<RecomSerproSerpro>)criterio.list();
			}
			
			 
			return tmp;
		}
		
	
	
	
	
	
	
	
	

}
