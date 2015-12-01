package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.cache.CacheFacade;

import com.servinte.axioma.dto.administracion.DtoCiudad;
import com.servinte.axioma.dto.administracion.DtoCiudades;
import com.servinte.axioma.dto.administracion.DtoDepartamentos;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.CiudadesHome;

/**
 * 
 * @author axioma
 */
public class CiudadesDelegate extends CiudadesHome {
	

	/**
	 * Lista todas las Ciudades
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Ciudades> listarCiudades ()
	{	
//		long ini = System.currentTimeMillis();
		CacheFacade fachada = new CacheFacade();
		ArrayList<Ciudades> listaCiudades  = null;
		Object objetosEnChache = fachada.obtenerDeCache("listadoCiudades", Ciudades.class);
		if (objetosEnChache != null )
		{
			listaCiudades = (ArrayList<Ciudades>)objetosEnChache;
		}
		else
		{
			listaCiudades = (ArrayList<Ciudades>) sessionFactory.getCurrentSession()
			.createCriteria(Ciudades.class)
			.addOrder(Property.forName("descripcion").asc())
			.list();
	
			//NO borrar este ciclo ya que es necesario para inicializar los objetos que llegan de 
			//la base de datos como PersistentBag debido a que son de tipo lazyinitialization
			for (Ciudades ciudades : listaCiudades)
			{
				ciudades.getPaises().getDescripcion();
				ciudades.getDepartamentos().getDescripcion();
			}
			fachada.guardarEnCache("listadoCiudades", listaCiudades);
		}
//		long fin = System.currentTimeMillis();
//		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTIEMPO: " + (fin - ini) + "\n\n\n");
		return listaCiudades;
	}
	
	
	/**
	 * Lista todas las Ciudades de un país
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Ciudades> listarCiudadesPorPais (String codigoPais){
		  
		 Criteria criteria= sessionFactory.getCurrentSession()
			.createCriteria(Ciudades.class)
			.createAlias("paises", "paises")
			.createAlias("departamentos", "departamento")
			.add(Restrictions.eq("paises.codigoPais", codigoPais))
			.addOrder(Order.asc("descripcion"));
		 
		 ArrayList<Ciudades> listaCiudades = (ArrayList<Ciudades>)criteria.list();
		
		//NO borrar este ciclo ya que es necesario para inicializar los objetos que llegan de 
		//la base de datos como PersistentBag debido a que son de tipo lazyinitialization
		for (Ciudades ciudades : listaCiudades) {
			ciudades.getPaises().getDescripcion();
			ciudades.getDepartamentos().getDescripcion();
		}
		return listaCiudades;
	}
	
	
	/**
	 * retorna una ciudad dependiento de su departamento y pais
	 * @param codigoCiudad
	 * @param codigoDepto
	 * @param codigoPais
	 * 
	 * @return Ciudades
	 */
	public Ciudades buscarCiudad(String codigoCiudad, String codigoDepto, String codigoPais)
	{
		Ciudades ciudad =  (Ciudades) sessionFactory.getCurrentSession()
			.createCriteria(Ciudades.class, "ciudad")
			.add(Restrictions.eq("id.codigoCiudad", codigoCiudad))
			.add(Restrictions.eq("id.codigoDepartamento", codigoDepto))
			.add(Restrictions.eq("id.codigoPais", codigoPais))
			
			.uniqueResult();
		
		ciudad.getId();
		
		return ciudad;
	}


	/**
	 * 
	 * @param codigoPais
	 * @return
	 */
	public ArrayList<DtoCiudades> listarCiudadesDtoPorPais(String codigoPais) 
	{
		ArrayList<DtoCiudades> resultado=new ArrayList<DtoCiudades>();
		ArrayList<Ciudades> ciudades=listarCiudadesPorPais(codigoPais);
		for(Ciudades ciud:ciudades )
		{
			DtoCiudades dto=new DtoCiudades();
			dto.setCodigoCiudad(ciud.getId().getCodigoCiudad());
			dto.setDepartamento(new DtoDepartamentos(ciud.getDepartamentos().getId().getCodigoDepartamento(), ciud.getDepartamentos().getDescripcion(), new DtoPaises(ciud.getPaises().getCodigoPais(), ciud.getPaises().getDescripcion())));
			dto.setDescripcionCiudad(ciud.getDescripcion());
			dto.setLocalidad(ciud.getLocalidad()+"");
			resultado.add(dto);
		}
		return resultado;
	}
	
	/**
	 * Lista todas las Ciudades parametrizadas en el sistema
	 * @return La lista de todas las ciudades en la estructura DtoCiudad
	 */
	@SuppressWarnings("unchecked")
	public List<DtoCiudad> listarAllCiudades(){
		  
		 Criteria criteria= sessionFactory.getCurrentSession()
			.createCriteria(Ciudades.class, "ciudad");
		 ProjectionList projection = Projections.projectionList();		
			projection.add(Projections.property("ciudad.id.codigoCiudad"),"codigoCiudad");
			projection.add(Projections.property("ciudad.id.codigoDepartamento"),"codigoDepartamento");
			projection.add(Projections.property("ciudad.id.codigoPais"),"codigoPais");
			criteria.setProjection(projection);
			criteria.setResultTransformer(Transformers.aliasToBean(DtoCiudad.class));
			List<DtoCiudad> listaCiudades =  criteria.list();
		 
		return listaCiudades;
	}
	
}
