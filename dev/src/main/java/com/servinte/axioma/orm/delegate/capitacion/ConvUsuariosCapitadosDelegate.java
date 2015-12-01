package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.ConvUsuariosCapitados;
import com.servinte.axioma.orm.ConvUsuariosCapitadosHome;

/**
 * Clase que maneja el acceso a la base de tados de la entidad ConvUsuariosCapitados
 * 
 * @author Ricardo Ruiz
 *
 */
public class ConvUsuariosCapitadosDelegate extends ConvUsuariosCapitadosHome{

	
	@Override
	public void attachDirty(ConvUsuariosCapitados instance) {
		super.attachDirty(instance);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el registro más reciente en la tabla para
	 * retornar los datos a postular
	 * @param codigoUsuarioCapitado
	 * @return ConvUsuariosCapitados
	 * @author Ricardo Ruiz
	 *
	 */
	@SuppressWarnings("unchecked")
	public ConvUsuariosCapitados buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(long codigoUsuarioCapitado){
		ConvUsuariosCapitados registro = null;
		
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ConvUsuariosCapitados.class,"convUsuariosCapitados")
			.createAlias("convUsuariosCapitados.usuariosCapitados", "usuarioCapitado");
				
		criteria.add(Restrictions.eq("usuarioCapitado.codigo", codigoUsuarioCapitado));		
		criteria.addOrder(Order.desc("convUsuariosCapitados.fechaCargue"));
		List<ConvUsuariosCapitados> lista = (List<ConvUsuariosCapitados>)criteria.list();
		if(lista!= null && !lista.isEmpty()){
			registro=lista.get(0);
		}
		return registro;
	}
	
	/**
	 * 
	 * Este Método se encarga de obtener los registros para un usuario capitado
	 * y cuya fecha inicio y fin no esten dentro del rango de fechas ó que se traslapen o que los contenga 
	 * @param codigoUsuarioCapitado
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<DtoContrato>
	 * @author Ricardo Ruiz
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<DtoContrato> obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(long codigoUsuarioCapitado, Date fechaInicio, Date fechaFin){
		List<DtoContrato> contratos=null;
		String consulta="SELECT cont.codigo, conv.codigo FROM ConvUsuariosCapitados cuc "+
							"INNER JOIN cuc.usuariosCapitados uc "+
							"INNER JOIN cuc.contratos cont "+
							"INNER JOIN cont.convenios conv "+
						"WHERE uc.codigo =:codigoUsuarioCapitado "+
							"AND (cuc.fechaInicial BETWEEN :fechaInicio AND :fechaFin "+ 
								"OR cuc.fechaFinal BETWEEN :fechaInicio AND :fechaFin "+
								"OR (cuc.fechaInicial < :fechaInicio AND cuc.fechaFinal > :fechaFin))";
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoUsuarioCapitado", codigoUsuarioCapitado, Hibernate.LONG);
		query.setParameter("fechaInicio", fechaInicio, Hibernate.DATE);
		query.setParameter("fechaFin", fechaFin, Hibernate.DATE);
		List<Object[]> lista = query.list();
		if(lista!= null && !lista.isEmpty()){
			contratos=new ArrayList<DtoContrato>();
			for(Object[] contrato:lista){
				DtoContrato dto = new DtoContrato();
				dto.setCodigo((Integer)contrato[0]);
				dto.setConvenio((Integer)contrato[1]);
				contratos.add(dto);
			}
		}
		return contratos;
	}
}
