package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import util.UtilidadTexto;

import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.EspecialidadesHome;


/**
 * 
 * @author Cristian Murillo
 *
 */
public class EspecialidadesDelegate extends EspecialidadesHome{
	
	
	//TODO OJO CON ESTE METODO SIEMPRE HAY QUE BUSCAR POR INSTITUCION 
	@SuppressWarnings("unchecked")
	public  List<Especialidades>  listarEspecialidades (String tipoEspecialidad)
	{
		if (!tipoEspecialidad.equals(""))
		{
			return sessionFactory.getCurrentSession().createCriteria(Especialidades.class).add(Expression.eq("tipoEspecialidad", tipoEspecialidad)).list();
		}
		
		else
		{
			return sessionFactory.getCurrentSession().createCriteria(Especialidades.class).list();
		}
	}
	
	
	

	/**
	 * lista las Especialidades de la institucion que no tienen una cuenta  por especialidad asociada
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Especialidades> listarEspecialidadesSinCuentasXEspecialidad(int institucion)
	{
		return (ArrayList<Especialidades>) sessionFactory.getCurrentSession()
			.createCriteria(Especialidades.class)
			.add(Expression.isEmpty("cuentasXEspOdontos"))
			.add(Expression.eq("instituciones.codigo", institucion))
			.list();
	}
	
	
	
	
	
	/**
	 * LISTA LAS ESPECIALIDADES POR: TIPO, INSTITUCION , CODIGO PK
	 * @author Edgar Carvajal Ruiz
	 * @param dtoEspec
	 * @return
	 */
	public List<Especialidades> listarEspe(Especialidades dtoEspec )
	{
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(Especialidades.class);
		
		
		if(!UtilidadTexto.isEmpty(dtoEspec.getTipoEspecialidad() ))
		{
			criterio.add(Restrictions.eq("tipoEspecialidad",dtoEspec.getTipoEspecialidad()));
		}
		
		if(dtoEspec.getInstituciones()!=null && dtoEspec.getInstituciones().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("instituciones.codigo", dtoEspec.getInstituciones().getCodigo()));
		}
		
		if(dtoEspec.getCodigo()>0)
		{
			criterio.add(Restrictions.eq("codigo",dtoEspec.getCodigo()));
		}
		
		
		criterio.add(Restrictions.gt("codigo", 0));
		criterio.addOrder(Order.asc("nombre"));
		
		
		List<Especialidades> tmp= criterio.list(); 
		
		
		return tmp;
	}	
	
	
	/**
	 * 	
	 * Este Método se encarga de consultar las especialidades
	 * registradas en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Especialidades> buscarEspecialidades(){
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(Especialidades.class);
		 
		 return (ArrayList<Especialidades>)criteria.list();
	}
	
	/**
	 * 
	 * M&eacute;todo encargado de listar las especialidades registradas en el
	 * sistema en orden alfab&eacute;tico
	 * 
	 * @return ArrayList<Especialidades>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Especialidades> listarEspecialidadesEnOrden() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Especialidades.class);

		criteria.addOrder(Property.forName("nombre").asc());
		return (ArrayList<Especialidades>) criteria.list();

	}
}
