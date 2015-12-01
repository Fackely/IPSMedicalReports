/**
 * 
 */
package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.EsquemasTarifariosHome;
import com.servinte.axioma.orm.TarifasIss;

/**
 * @author armando
 *
 */
public class EsquemasTarifariosDelegate extends EsquemasTarifariosHome
{

	/**
	 * listar Esquemas Tarifarios activos
	 * @param todos
	 * @param procedimientos
	 * @return ArrayList<EsquemasTarifarios>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EsquemasTarifarios> listarEsquemasTarifarios(boolean todos, boolean procedimientos) 
	{		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(EsquemasTarifarios.class);
		if(!todos)
		{
			if(procedimientos)
			{
				criteria.add(Expression.eq("esInventario", false));
			}
			else
			{
				criteria.add(Expression.eq("esInventario", true));
			}
		}
		
		criteria.add(Expression.eq("activo", true));
		criteria.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<EsquemasTarifarios>)criteria.list();
	}
	
	
	/**
	 * Metodo que permite listar los esquemas tarifarios que se encuentren activos y que sean de servicios
	 * @author Diana Ruiz
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EsquemasTarifarios> listaEsquemaTarifarioServicios(){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(EsquemasTarifarios.class);
		criteria.add(Expression.eq("esInventario", false));	
		criteria.add(Expression.eq("activo", true));
		criteria.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<EsquemasTarifarios>)criteria.list();	
	}
	

	/**
	 * 
	 * M&eacute;todo encargado de consultar todas la tarifas de servicios
	 * definidas para un esquema tarifario seleccionado, con una especialidad y
	 * un programa o servicio determinado, de acuerdo a lo definido en el
	 * m&oacute;dulo de Odontolog&iacute;a si se utiliza programas
	 * odontol&oacute;gicos en la Instituci&oacute;n.
	 * 
	 * @param dtoFiltros
	 * @return ArrayList<DTOTarifasServicios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTOTarifasServicios> obtenerTarifasEsquemaTarifario(
			DtoReporteTarifasPorEsquemaTarifario dtoFiltros) {

		ProjectionList projectionList =  Projections.projectionList();
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(TarifasIss.class, "tarifasIss")
				.createAlias("tarifasIss.esquemasTarifarios", "esquemaTarifario")
				.createAlias("tarifasIss.servicios", "servicio")
				.createAlias("servicio.referenciasServicios", "referenciaServicio");
		criteria.add(Restrictions.eq("esquemaTarifario.codigo", dtoFiltros.getCodigoEsquemaTarifario()));

		projectionList.add(Projections.property("referenciaServicio.codigoPropietario"),"codigoServicio");
		projectionList.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio");
		projectionList.add(Projections.property("tarifasIss.valorTarifa"),"tarifaServicio");

		if (dtoFiltros.getCodigoEspecialidad() != ConstantesBD.codigoNuncaValido) {
			criteria.createAlias("servicio.especialidades", "especialidad");
			criteria.add(Restrictions.eq("especialidad.codigo", dtoFiltros.getCodigoEspecialidad()));
		}

		if(dtoFiltros.isUtilizaProgramasOdontologicos()){
			criteria.createAlias("servicio.detalleProgramases", "detallePrograma");
			criteria.createAlias("detallePrograma.programas", "programa");
			projectionList.add(Projections.property("programa.codigo"),"codigoPrograma");
			projectionList.add(Projections.property("programa.nombre"),"nombrePrograma");

			if (dtoFiltros.getProgramas().getCodigo() != ConstantesBD.codigoNuncaValido) {
				criteria.add(Restrictions.eq("programa.codigo", dtoFiltros.getCodigoPrograma()));
			}
			criteria.addOrder(Order.asc("programa.codigo"));
		} else {
			if (!(dtoFiltros.getServicio().getCodigo().equals(String.valueOf(ConstantesBD.codigoNuncaValido)))) {
				criteria.add(Restrictions.eq("servicio.codigo", 
						Integer.parseInt(dtoFiltros.getServicio().getCodigo())));
			}
			criteria.addOrder(Order.asc("referenciaServicio.descripcion"));
		}
		if (dtoFiltros.getCodigoEspecialidad() == ConstantesBD.codigoNuncaValido) {
			criteria.createAlias("servicio.especialidades", "especialidad");
		}
		projectionList.add(Projections.property("especialidad.nombre"),"nombreEspecialdad");

		criteria.setProjection(projectionList);

		criteria.addOrder(Order.asc("referenciaServicio.codigoPropietario"));

		criteria.setResultTransformer(Transformers.aliasToBean(DTOTarifasServicios.class));

		ArrayList<DTOTarifasServicios> listaTarifasEsquemaTarifario = 
			(ArrayList<DTOTarifasServicios>)criteria.list();

		return listaTarifasEsquemaTarifario;

	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar todas la tarifas de un esquema
	 * tarifario determinado.
	 * 
	 * @param codigoEsquemaTarifario
	 * @return ArrayList<DtoTarifasPorEsquemaTarifario>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<DtoTarifasPorEsquemaTarifario> obtenerTarifas(
			int codigoEsquemaTarifario) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(TarifasIss.class, "tarifasIss")
				.createAlias("tarifasIss.esquemasTarifarios", "esquemaTarifario")
				.createAlias("tarifasIss.servicios", "servicio")
				.createAlias("servicio.detalleProgramases", "detallePrograma")
				.createAlias("detallePrograma.programas", "programa")
				.setProjection(Projections.projectionList()
						.add(Projections.property("esquemaTarifario.codigo"), "codigoEsquemaTarifario")
						.add(Projections.property("programa.codigo"), "codigoPrograma")
						.add(Projections.property("tarifasIss.valorTarifa"),"valorTarifa"));

		criteria.add(Restrictions.eq("esquemaTarifario.codigo",codigoEsquemaTarifario));

		criteria.addOrder(Order.asc("programa.codigo"));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoTarifasPorEsquemaTarifario.class));

		ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas = (ArrayList)criteria.list();

		return listaTarifas;

	}

}
