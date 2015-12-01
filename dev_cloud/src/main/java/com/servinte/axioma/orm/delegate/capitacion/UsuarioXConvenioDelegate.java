/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.UsuarioXConvenioHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio para UsuarioXConvenio
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class UsuarioXConvenioDelegate extends UsuarioXConvenioHome 
{


	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<UsuarioXConvenio> listarTodos()
	{
		return (ArrayList<UsuarioXConvenio>) sessionFactory.getCurrentSession()
			.createCriteria(UsuarioXConvenio.class)
			.list();
	}
	
	
	
	@Override
	public void attachDirty(UsuarioXConvenio instance){
		super.attachDirty(instance);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro de la tabla 
	 * usuario_x_convenio a partir del código del paciente
	 * @param int codigoPersona
	 * @return UsuarioXConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public UsuarioXConvenio buscarUsuarioConvenioPorPaciente(int codigoPersona){
		UsuarioXConvenio registro = null;
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(UsuarioXConvenio.class,"usuarioXConvenio")
		.createAlias("usuarioXConvenio.personas", "persona");
				
		criteria.add(Restrictions.eq("persona.codigo", codigoPersona));		
		criteria.add(Restrictions.le("usuarioXConvenio.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
		criteria.add(Restrictions.ge("usuarioXConvenio.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));
		
		registro = (UsuarioXConvenio)criteria.uniqueResult();
		
		return registro;
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el registro mas reciente en la tabla para
	 * retornar los datos a postular
	 * @param int codigoPersona
	 * @return UsuarioXConvenio
	 * @author Ricardo Ruiz
	 *
	 */
	public UsuarioXConvenio buscarUsuarioConvenioRecientePorPaciente(int codigoPersona){
		UsuarioXConvenio registro = null;
		
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(UsuarioXConvenio.class,"usuarioXConvenio")
			.createAlias("usuarioXConvenio.personas", "persona");
				
		criteria.add(Restrictions.eq("persona.codigo", codigoPersona));		
		criteria.addOrder(Order.desc("usuarioXConvenio.fechaCargue"));
		List<UsuarioXConvenio> lista = (List<UsuarioXConvenio>)criteria.list();
		if(lista!= null && !lista.isEmpty()){
			registro=lista.get(0);
		}
		return registro;
	}
	
	/**
	 * 
	 * Este Método se encarga de obtener los registros para una persona
	 * y cuya fecha inicio y fin no esten dentro del rango de fechas ó que se traslapen o que los contenga 
	 * @param codigoPersona
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<DtoContrato>
	 * @author Ricardo Ruiz
	 *
	 */
	public List<DtoContrato> obtenerCarguesPreviosPorPersonaPorRangoFechas(int codigoPersona, Date fechaInicio, Date fechaFin){
		List<DtoContrato> contratos = null;
		String consulta="SELECT cont.codigo, conv.codigo  FROM UsuarioXConvenio uxc "+
							"INNER JOIN uxc.personas per "+
							"INNER JOIN uxc.contratos cont "+
							"INNER JOIN cont.convenios conv "+
						"WHERE per.codigo =:codigoPersona "+
							"AND (uxc.fechaInicial BETWEEN :fechaInicio AND :fechaFin "+ 
								"OR uxc.fechaFinal BETWEEN :fechaInicio AND :fechaFin "+
								"OR (uxc.fechaInicial < :fechaInicio AND uxc.fechaFinal > :fechaFin))";
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoPersona", codigoPersona, StandardBasicTypes.INTEGER);
		query.setParameter("fechaInicio", fechaInicio, StandardBasicTypes.DATE);
		query.setParameter("fechaFin", fechaFin, StandardBasicTypes.DATE);
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
	
	/**
	 * 
	 * Este Método se encarga de validar si existe un cargue vigente para un contrato
	 * y y una persona
	 * @param codigoPersona
	 * @param fechaActual
	 * @param codigoContrato
	 * @return Boolean
	 * @author Ricardo Ruiz
	 *
	 */
	public boolean existeCargueVigentePorPersonaPorContrato(int codigoPersona, Date fechaActual, int codigoContrato){
		boolean existe = false;
		String consulta="SELECT uxc.consecutivo FROM UsuarioXConvenio uxc "+
							"INNER JOIN uxc.personas per "+
							"INNER JOIN uxc.contratos con "+
						"WHERE per.codigo =:codigoPersona "+
							"AND con.codigo =:codigoContrato "+
							"AND uxc.fechaInicial <= :fechaActual AND uxc.fechaFinal >= :fechaActual";
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoPersona", codigoPersona, StandardBasicTypes.INTEGER);
		query.setParameter("codigoContrato", codigoContrato, StandardBasicTypes.INTEGER);
		query.setParameter("fechaActual", fechaActual, StandardBasicTypes.DATE);
		List<Object> lista = query.list();
		if(lista!= null && !lista.isEmpty()){
			existe=true;
		}
		return existe;
	}
	
}
