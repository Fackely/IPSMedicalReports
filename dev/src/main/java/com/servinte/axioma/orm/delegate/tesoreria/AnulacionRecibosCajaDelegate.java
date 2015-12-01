package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.AnulacionRecibosCajaHome;
import com.servinte.axioma.orm.TurnoDeCaja;

public class AnulacionRecibosCajaDelegate extends AnulacionRecibosCajaHome {

	
	
	@SuppressWarnings("unchecked")
	public List<AnulacionRecibosCaja> obtenerAnulacionesRecibosCajaXTurnoCaja (TurnoDeCaja turnoDeCaja){
		
		List<AnulacionRecibosCaja>  listaAnulacionRecibosCaja = sessionFactory.getCurrentSession().
		createCriteria(AnulacionRecibosCaja.class)
		.createAlias("recibosCaja","rbCaja")	
		.createAlias("rbCaja.recibosCajaXTurnos","rcxt")
		
        .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
        .addOrder(Order.asc("id.numeroAnulacionRc"))
        .list();
		
		return listaAnulacionRecibosCaja;
	}
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * Anulaci&oacute;n de un recibo de caja que esta asociado a un turno de caja espec&iacute;fico.
	 * Se toman en cuenta las Anulaciones en estado aprobado.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoAnulaciones(TurnoDeCaja turnoDeCaja){
		
		return (Date) sessionFactory.getCurrentSession().createCriteria(AnulacionRecibosCaja.class)
		  .createAlias("recibosCaja", "reciboCaja")
		  .createAlias("reciboCaja.recibosCajaXTurnos","rcxt")
		  .setProjection(Projections.max("fecha"))
		  .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja)).uniqueResult();
	}

}
