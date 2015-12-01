package com.servinte.axioma.orm.delegate.odontologia.cuotaOdontologica;



import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.orm.DetalleCuotasOdontoEspHome;



/**
 * 
 * @author Edgar Augusto Carvajal
 *
 */
public class DetalleCuotasOdontoEspsDelegate  extends DetalleCuotasOdontoEspHome {
	
	
	
	/**
	 * METODO QUE RECIBE UN DETALLE CUOTAS ODON EL CUAL TIENE CARGADO LA CUOTA ODONTOLOGICA
	 * @author Edgar Carvajal Ruiz
	 * @param dtoDetalleCuotas
	 * @return
	 */
	public List<DetalleCuotasOdontoEsp> consultaAvanzadaDetalleCuota(DetalleCuotasOdontoEsp dtoDetalleCuotas)
	{
	
		Criteria criterio=sessionFactory.getCurrentSession().createCriteria(DetalleCuotasOdontoEsp.class);
		
		
		if(dtoDetalleCuotas.getCuotasOdontEspecialidad()!=null && dtoDetalleCuotas.getCuotasOdontEspecialidad().getCodigoPk()>0)
		{
			criterio.add(Restrictions.eq("cuotasOdontEspecialidad.codigoPk",dtoDetalleCuotas.getCuotasOdontEspecialidad().getCodigoPk()));
		}
		
		
		List<DetalleCuotasOdontoEsp> lista= criterio.list();

		return lista;
	}

	
	

	/**
	 * 	ELIMINAR DETALLES POR CUOTA ODONTOLOGICA 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoCuota
	 */
	public void eliminarDetallesxCuota( CuotasOdontEspecialidad dtoCuota)
	{
		String hql="delete  from   DetalleCuotasOdontoEsp   where cuotasOdontEspecialidad.codigoPk="+dtoCuota.getCodigoPk() ;
		Session sess=  sessionFactory.getCurrentSession();
		Query query= sess.createQuery(hql);
		query.executeUpdate();
	}
	
	
	
	
	
	
	
	
	/**
	 * TODO BORRAR ESTE METODO
	 * @author Edgar Carvajal Ruiz
	 * @param args
	 */
	public static void main(String[] args) {
		
		CuotasOdontEspecialidad dtoCuota= new CuotasOdontEspecialidad();
		dtoCuota.setCodigoPk(27);
		
		DetalleCuotasOdontoEspsDelegate d = new DetalleCuotasOdontoEspsDelegate();
		d.eliminarDetallesxCuota(dtoCuota);
		
		
		HibernateUtil.endTransaction();
		
	}
}
