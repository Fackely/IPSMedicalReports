package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposAfiliadoHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad  Tipos de Afiliado
 * 
 * @author Angela Maria Aguirre
 * @since 30/08/2010
 */
@SuppressWarnings("unchecked")
public class TiposAfiliadoDelegate extends TiposAfiliadoHome 
{
	

	/**
	 * 
	 * Este Método se encarga de consultar los tipos de 
	 * afiliados registrados en el sistema
	 * 
	 * @return ArrayList<TiposAfiliado>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposAfiliado> obtenerTiposAfiliado()
	{
		return (ArrayList<TiposAfiliado>)sessionFactory.getCurrentSession()
			.createCriteria(TiposAfiliado.class,"tipoAfiliado")
			.addOrder(Order.asc("tipoAfiliado.nombre"))
			.list();	
	}
	
	/**
	 * Metodo que se encarga de obtener la descripcion del
	 * acronimo de tipo Afiliado
	 * 
	 * @param tipoAfiliado
	 * @return String
	 */
	public String obtenerDescripcionTipoAfiliado(Character tipoAfiliado){
					String consulta = 	"SELECT "+
											"ta.nombre " +
										"FROM TiposAfiliado ta " +
										"WHERE (ta.acronimo = :tipoAfiliado) ";
					
			Query query =sessionFactory.getCurrentSession().createQuery(consulta);
			query.setParameter("tipoAfiliado", tipoAfiliado, Hibernate.CHARACTER);
						
			Log4JManager.info(	"\n -------------------------------------------\n" +
			  	"PARAMETROS DE LA CONSULTA - Obtener Descripcion TipoAfiliado -" +
			  	"\n -----------------------------------------------------------\n" +
			  	"AcronimoTipoAfiliado:	" +tipoAfiliado);
			
			String descripcionTipoAfiliado = (String)query.uniqueResult();
			return descripcionTipoAfiliado;
	}
	
}
