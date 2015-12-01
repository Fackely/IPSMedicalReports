/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia.administracion;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.EncaEmiTargetaClienteHome;
import com.servinte.axioma.orm.EncaEmiTarjetaCliente;

/**
 * Delegado para el acceso a la BD de emisi�n tarjeta cliente
 * @author Juan David Ram�rez
 * @since 08 Septiembre 2010
 * @version 1.0
 */
public class EncaEmiTargetaClienteDelegate extends EncaEmiTargetaClienteHome
{

	/**
	 * Consultar emisi�n de tarjeta cliente para el tipo de tarjeta y el centro
	 * de atenci�n pasados por par�metros
	 * @param tipoTarjeta Tipo de Tarjeta
	 * @param centroAtencion Centro de atenci�n consultado
	 * @return {@link DtoEmisionTarjetaCliente} Dto con los datos de la emisi�n 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoEmisionTarjetaCliente> consultarEmisionTarjeta(int tipoTarjeta, int centroAtencion)
	{
		List<DtoEmisionTarjetaCliente> listaEmision=(List<DtoEmisionTarjetaCliente>) sessionFactory.getCurrentSession()
		.createCriteria(EncaEmiTarjetaCliente.class)
		.setProjection(Projections.projectionList()
			.add(Projections.property("codigo"), "codigo")
			.add(Projections.property("serialInicial"), "serialInicialLong")
			.add(Projections.property("serialFinal"), "serialFinalLong")
		)
		.add(Restrictions.eq("tiposTarjCliente.codigoPk", new Long(tipoTarjeta)))
		.createCriteria("detEmiTarjetaClientes")
		.createCriteria("centroAtencion")
		.add(Restrictions.eq("consecutivo", centroAtencion))
		.setResultTransformer(Transformers.aliasToBean(DtoEmisionTarjetaCliente.class))
		.list();
		HibernateUtil.endTransaction();
		return new ArrayList<DtoEmisionTarjetaCliente>(listaEmision);
	}
}
