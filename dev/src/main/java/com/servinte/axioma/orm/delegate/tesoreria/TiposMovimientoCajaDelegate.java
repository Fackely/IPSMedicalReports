package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TiposMovimientoCajaHome;

/**
 * Clase que se encarga de realizar las consultas espec&iacute;ficas de Tipos Movimiento Caja
 * 
 * @author Jorge Armando Agudelo
 * 
 */

public class TiposMovimientoCajaDelegate extends TiposMovimientoCajaHome{

	@SuppressWarnings("unchecked")
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo()
	{
		return (List<TiposMovimientoCaja>)sessionFactory.getCurrentSession().
						createCriteria(TiposMovimientoCaja.class).
						add(Restrictions.le("codigo", 3)).
						addOrder(Property.forName("codigo").asc()).
						list(); // Solamente muestro los primeros 3 ya que estos son tipos de arqueo
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(Object[] filtro){		
		ArrayList<TiposMovimientoCaja> listaTipos = new ArrayList<TiposMovimientoCaja>();
		
		listaTipos = (ArrayList<TiposMovimientoCaja>) sessionFactory.getCurrentSession()
				.createCriteria(TiposMovimientoCaja.class)
				.add(Restrictions.in("codigo", filtro))
				.list();
		
		
		return listaTipos;
			
	}
	
}
