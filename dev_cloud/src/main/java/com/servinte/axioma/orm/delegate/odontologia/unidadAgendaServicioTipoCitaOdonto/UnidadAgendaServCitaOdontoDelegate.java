
package com.servinte.axioma.orm.delegate.odontologia.unidadAgendaServicioTipoCitaOdonto;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.ReferenciasServicio;
import com.servinte.axioma.orm.ReferenciasServicioId;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdontoHome;
import com.servinte.axioma.orm.UnidadesConsulta;


/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link UnidadAgendaServCitaOdonto}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public class UnidadAgendaServCitaOdontoDelegate  extends UnidadAgendaServCitaOdontoHome{

	/**
	 * M&eacute;todo que se encarga de realizar el registro de Unidad de Agenda - Servicio por Tipo de Cita Odontol&oacute;gica
	 * 
	 * @param unidadAgendaServCitaOdonto
	 * @return boolean indicando si el registro se realiz&oacute; satisfactoriamente 
	 */
	public long guardarRegistroUnidadAgendaPorServicioCitaOdonto(DtoUnidadAgendaServCitaOdonto dtoUnidadAgendaServCitaOdonto){
		
		try {
			
			UnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = new UnidadAgendaServCitaOdonto();
			
			unidadAgendaServCitaOdonto.setFecha(UtilidadFecha.getFechaActualTipoBD());
			unidadAgendaServCitaOdonto.setHora(UtilidadFecha.getHoraActual());
			unidadAgendaServCitaOdonto.setTipoCita(dtoUnidadAgendaServCitaOdonto.getAcronimoTipoCita());
			
			Servicios servicio = new Servicios();
			servicio.setCodigo(dtoUnidadAgendaServCitaOdonto.getCodigoServicio());
			
			ReferenciasServicio referenciasServicio = new ReferenciasServicio();
			
			ReferenciasServicioId referenciasServicioId = new ReferenciasServicioId(dtoUnidadAgendaServCitaOdonto.getCodigoServicio(), dtoUnidadAgendaServCitaOdonto.getCodigoTipoTarifario());
			
			referenciasServicio.setId(referenciasServicioId);
			
			unidadAgendaServCitaOdonto.setReferenciasServicio(referenciasServicio);
			
			UnidadesConsulta  unidadAgenda = new UnidadesConsulta();
			unidadAgenda.setCodigo(dtoUnidadAgendaServCitaOdonto.getCodigoUnidadAgenda());
			
			unidadAgendaServCitaOdonto.setUnidadesConsulta(unidadAgenda);
			
			unidadAgendaServCitaOdonto.setUsuarios(dtoUnidadAgendaServCitaOdonto.getUsuario());
			
			super.persist(unidadAgendaServCitaOdonto);
			
			return unidadAgendaServCitaOdonto.getCodigoPk();
			
		} catch (Exception e) {
		
			return ConstantesBD.codigoNuncaValidoLong;
		}
	}
	
	
	/**
	 * M&eacute;todo que obtiene un listado con las Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;ca
	 * que ya fueron registradas en el sistema
	 * 
	 * @param forma
	 * @return List<DtoUnidadAgendaServCitaOdonto>
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUnidadAgendaServCitaOdonto> obtenerListadoUnidAgenServCitaOdonRegistrados(){
			
		return (List<DtoUnidadAgendaServCitaOdonto>) sessionFactory.getCurrentSession()
		.createCriteria(UnidadAgendaServCitaOdonto.class)		
		.createAlias("referenciasServicio", "refServicio")
		.createAlias("refServicio.tarifariosOficiales", "tarifariosOficiales")
		.createAlias("refServicio.servicios", "servicio")
		.createAlias("unidadesConsulta", "unidadConsulta")

		.setProjection(Projections.projectionList()
			  .add( Projections.property("codigoPk"), "codigoRegistro" )
			  .add( Projections.property("tipoCita"), "acronimoTipoCita" )
			  .add( Projections.property("fecha"), "fecha" )
			  .add( Projections.property("hora"), "hora" )
			  .add( Projections.property("unidadConsulta.codigo"), "codigoUnidadAgenda" )
			  .add( Projections.property("unidadConsulta.descripcion"), "descripcionUnidadAgenda" )
			  .add( Projections.property("servicio.codigo"), "codigoServicio" )
			  .add( Projections.property("refServicio.descripcion"), "descripcionServicio" )
			  .add( Projections.property("refServicio.codigoPropietario"), "codigoPropietarioServicio" )
			  .add( Projections.property("tarifariosOficiales.codigo"), "codigoTipoTarifario" )
	          .add( Projections.property("tarifariosOficiales.nombre"), "nombreTipoTarifario" )
	  	)
	  
	  	.addOrder(Order.asc("codigoPk"))
		.setResultTransformer(Transformers.aliasToBean(DtoUnidadAgendaServCitaOdonto.class))
		.list();
	}
	
	

	/**
	 * M&eacute;todo que elimina un registro de Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;gica
	 * 
	 * @param codigoRegistro
	 * @return {@link Boolean} indicando si se realiz&oacute; la eliminaci&oacute; registro
	 */
	public boolean eliminarUnidAgenServCitaOdonRegistrada(long codigoRegistro){
	
		boolean delete = false;
			
		try {
			
			UnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = super.findById(codigoRegistro);
			
			if(unidadAgendaServCitaOdonto!=null){
				
				super.delete(unidadAgendaServCitaOdonto);
				delete = true;
			}

		} catch (Exception e) {
		
			return delete;
		}
	
		return delete;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de consultar si se encuentra parametrizada la Unidad de Agenda 
	 * - Servicio por un tipo de cita espec&iacute;fico.
	 * 
	 * @param acronimoTipoCita
	 * @return DtoUnidadAgendaServCitaOdonto con la informacion de la parametrizaci&oacute;n
	 */
	public DtoUnidadAgendaServCitaOdonto consultarParametricaPorTipoCita (String acronimoTipoCita){
		

		return (DtoUnidadAgendaServCitaOdonto) sessionFactory.getCurrentSession()
		.createCriteria(UnidadAgendaServCitaOdonto.class)		
		.createAlias("referenciasServicio", "refServicio")
		.createAlias("refServicio.tarifariosOficiales", "tarifariosOficiales")
		.createAlias("refServicio.servicios", "servicio")
		.createAlias("servicio.tiposSerNaturaleza", "tipoSerNaturaleza")
		.createAlias("tipoSerNaturaleza.tiposServicio", "tipoServicio")
		.createAlias("unidadesConsulta", "unidadConsulta")

		.setProjection(Projections.projectionList()
			  .add( Projections.property("codigoPk"), "codigoRegistro" )
			  .add( Projections.property("tipoCita"), "acronimoTipoCita" )
			  .add( Projections.property("fecha"), "fecha" )
			  .add( Projections.property("hora"), "hora" )
			  .add( Projections.property("unidadConsulta.codigo"), "codigoUnidadAgenda" )
			  .add( Projections.property("unidadConsulta.descripcion"), "descripcionUnidadAgenda" )
			  .add( Projections.property("servicio.codigo"), "codigoServicio" )
			  .add( Projections.property("tipoServicio.acronimo"), "tipoServicio" )
			  .add( Projections.property("tipoServicio.nombre"), "nombreTipoServicio" )
			  .add( Projections.property("refServicio.descripcion"), "descripcionServicio" )
			  .add( Projections.property("refServicio.codigoPropietario"), "codigoPropietarioServicio" )
			  .add( Projections.property("tarifariosOficiales.codigo"), "codigoTipoTarifario" )
	          .add( Projections.property("tarifariosOficiales.nombre"), "nombreTipoTarifario" )
	  	)
	  	.add(Restrictions.eq("tipoCita", acronimoTipoCita))
		.setResultTransformer(Transformers.aliasToBean(DtoUnidadAgendaServCitaOdonto.class))
		.uniqueResult();

	}
	
}

