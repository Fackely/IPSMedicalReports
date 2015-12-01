package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.servinte.axioma.orm.MovimientosAbonos;
import com.servinte.axioma.orm.MovimientosAbonosHome;

/**
 * Clase que contiene lógica del negocio sobre el modelo para movimientos de
 * abonos de devolución.
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 08/03/2011
 */
public class MovimientosAbonosDelegate extends MovimientosAbonosHome {

	/**
	 * Método de validar si un paciente tiene movimientos abonos
	 * 
	 * @param codigoPaciente
	 * @return String S/N
	 * 
	 * @author Sonia Amaya
	 */
	public String validarSiTieneMovimientosAbonos(int codigoPaciente) {

		String existeMovimientoAbono = ConstantesBD.acronimoNo;
		
		ProjectionList projectionList =  Projections.projectionList();
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(MovimientosAbonos.class, "movimientoAbono")
				.createAlias("movimientoAbono.personas", 		"persona")
				.createAlias("persona.pacientes", 		"paciente");
				

		projectionList.add(Projections.property("movimientoAbono.codigoDocumento"), "codigoDocumento1");
		
		criteria.add(Restrictions.eq("paciente.codigoPaciente", codigoPaciente ));
		criteria.setProjection(projectionList);
		
		ArrayList<DtoMovmimientosAbonos> listadoMovimientos = 
			(ArrayList<DtoMovmimientosAbonos>) criteria.list();
		
		if(listadoMovimientos.size() > 0)
			existeMovimientoAbono = ConstantesBD.acronimoSi;
		
		return existeMovimientoAbono;

	}
	
	/**
	 * Este Método se encarga de obtener los movimientos de
	 * abono generados en una institución específica.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerMovimientosAbonosPorInstitucion(int codigoInstitucion) {

		ProjectionList projectionList =  Projections.projectionList();
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(MovimientosAbonos.class, "movimientoAbono")
				.createAlias("movimientoAbono.instituciones", "institucion");
				

		projectionList.add(Projections.property("movimientoAbono.codigo"), "codigo");
		
		criteria.add(Restrictions.eq("institucion.codigo", codigoInstitucion ));
		criteria.setProjection(projectionList);
		
		ArrayList<DtoMovmimientosAbonos> listadoMovimientos = 
			(ArrayList<DtoMovmimientosAbonos>) criteria.list();
		
		return listadoMovimientos;

	}
	
	/**
	 * Este Método se encarga de obtener el consolidado de los tipos de movimientos de abonos 
	 * de un paciente.
	 * @param codigoPaciente
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerConsolidadoPorTipoMovimiento(int codigoPaciente){
		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(MovimientosAbonos.class, "movimientoAbono")
				.createAlias("movimientoAbono.tiposMovAbonos", "tipoMovAbono")
				.createAlias("tipoMovAbono.nombreMovAbonos", "nombreMovAbono")
				.createAlias("movimientoAbono.personas", "paciente");
				

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("nombreMovAbono.nombre"), "nombreTipo")
				.add( Projections.property("tipoMovAbono.codigo"))
				.add( Projections.property("tipoMovAbono.operacion"), "operacion")
				.add( Projections.property("nombreMovAbono.orden"), "orden")
				.add(Projections.sum("movimientoAbono.valor"), "valor")
				
				.add(Projections.groupProperty("tipoMovAbono.codigo"))
				.add(Projections.groupProperty("nombreMovAbono.nombre"))
				.add(Projections.groupProperty("tipoMovAbono.operacion"))
				.add(Projections.groupProperty("nombreMovAbono.orden"))
				);
		
		criteria.add(Restrictions.eq("paciente.codigo", codigoPaciente));
		criteria.setResultTransformer( Transformers.aliasToBean(DtoMovmimientosAbonos.class));
		
		ArrayList<DtoMovmimientosAbonos> listadoMovimientos = 
			(ArrayList<DtoMovmimientosAbonos>) criteria.list();
		
		return listadoMovimientos;
	}
}
