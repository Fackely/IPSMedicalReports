
package com.servinte.axioma.orm.delegate.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DtoProcesoFacturacionPresupuestoCapitacion;
import com.servinte.axioma.orm.Facturas;
import com.servinte.axioma.orm.FacturasHome;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link Facturas}.
 * 
 * @author Cristhian Murillo
 *
 */
@SuppressWarnings("unchecked")
public class FacturasDelegate extends FacturasHome
{

	
	/**
	 * Método que se encarga de listar todas als facturas {@link Facturas}
	 * 
	 * @param historicoEncabezado
	 * @author Cristhian Murillo
	 * @return ArrayList<Facturas>
	 */
	public ArrayList<Facturas> listarTodas ()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Facturas.class, "facturas");
		return (ArrayList<Facturas>)criteria.list();
	}
	
	
	

	/**
	 * Método que se encarga de listar todas als facturas {@link Facturas}
	 * 
	 * @param historicoEncabezado
	 * @author Cristhian Murillo
	 * @return ArrayList<DtoFacturasCapitacion>
	 */
	public ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> buscarFacturasPorRangoFecha (DtoProcesoFacturacionPresupuestoCapitacion parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Facturas.class, "facturas");
		
		criteria.createAlias("facturas.estadosFacturaF"			, "estadosFacturaF");
		criteria.createAlias("facturas.instituciones"			, "instituciones");
		criteria.createAlias("facturas.detCargoses"				, "detCargoses"				,Criteria.INNER_JOIN);
		
		criteria.createAlias("detCargoses.articulo"				, "articulo"				,Criteria.LEFT_JOIN); 
		criteria.createAlias("articulo.nivelAtencion"	   		, "nivelAtencionArt"		,Criteria.LEFT_JOIN);  
		
		criteria.createAlias("detCargoses.serviciosByServicio"	, "servicio"				,Criteria.LEFT_JOIN); 
		criteria.createAlias("servicio.nivelAtencion"			, "nivelAtencionServ"		,Criteria.LEFT_JOIN);			 
		criteria.createAlias("servicio.referenciasServicios"	, "referenciaServicio"		,Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"			, "gruposServicios"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("facturas.convenios"				, "convenios" 				,Criteria.INNER_JOIN);
		//criteria.createAlias("convenios.contratoses"			, "contratoses" 			,Criteria.INNER_JOIN);
		criteria.createAlias("facturas.contratos"				, "contratoses" 			,Criteria.INNER_JOIN);
		
		
		Log4JManager.info("___ LOG_DEBUG___ FechaInicial y FechaFinal "+parametros.getFechaInicial()+" "+parametros.getFechafinal());
		criteria.add(Restrictions.between("facturas.fecha"			, parametros.getFechaInicial(), parametros.getFechafinal()));
		if(parametros.isExcluiranuladas()){
			Log4JManager.info("___ LOG_DEBUG___ codigoEstadoFAnulada "+ConstantesBD.codigoEstadoFAnulada);
			//criteria.add(Restrictions.ne("estadosFacturaF.codigo"	, ConstantesBD.codigoEstadoFAnulada));
			criteria.add(Restrictions.eq("estadosFacturaF.codigo"	, ConstantesBD.codigoEstadoFAnulada));
		}
		if(parametros.getConvenio() != null){
			Log4JManager.info("___ LOG_DEBUG___ convenio "+parametros.getConvenio());
			criteria.add(Restrictions.eq("convenios.codigo"			, parametros.getConvenio()));
		}
		if(parametros.getContrato() != null){
			Log4JManager.info("___ LOG_DEBUG___ contrato "+parametros.getContrato());
			criteria.add(Restrictions.eq("contratoses.codigo"		, parametros.getContrato()));
		}
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(parametros.getInstitucion());
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciaServicio.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciaServicio.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("facturas.codigo")					,"codigo")
				.add(Projections.property("facturas.fecha")						,"fecha")								
				.add(Projections.property("facturas.hora")						,"hora")
				.add(Projections.property("estadosFacturaF.codigo")				,"estado")
				.add(Projections.property("facturas.consecutivoFactura")		,"consecutivoFactura")
				.add(Projections.property("instituciones.codigo")				,"institucion")
				.add(Projections.property("detCargoses.cantidadCargada")		,"cantidadCargada")
				.add(Projections.property("detCargoses.valorUnitarioCargado")	,"valorUnitarioCargado")
				
				.add(Projections.property("articulo.codigo")					,"articuloDetCargo")
				.add(Projections.property("articulo.descripcion")				,"nombreArticuloDetCargo")
				.add(Projections.property("nivelAtencionArt.consecutivo")		,"nivelAtencionArticulo")
				.add(Projections.property("articulo.subgrupo")					,"subGrupoArticulo")

				.add(Projections.property("servicio.codigo")					,"servicioDetCargo")
				.add(Projections.property("referenciaServicio.descripcion")		,"nombreServicioDetCargo")
				.add(Projections.property("nivelAtencionServ.consecutivo")		,"nivelAtencionServicio")
				.add(Projections.property("gruposServicios.codigo")				,"grupoServicio")
				
				.add(Projections.property("convenios.codigo")					,"convenio")
				.add(Projections.property("contratoses.codigo")					,"contrato")
		);  
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoProcesoFacturacionPresupuestoCapitacion.class));
		
		ArrayList<DtoProcesoFacturacionPresupuestoCapitacion> listaResultado = (ArrayList<DtoProcesoFacturacionPresupuestoCapitacion>)criteria.list();
		
		
		for (DtoProcesoFacturacionPresupuestoCapitacion dtoProcesoFacturacionPresupuestoCapitacion : listaResultado) {
			if(dtoProcesoFacturacionPresupuestoCapitacion.getValorUnitarioCargado()==null)
			{
				dtoProcesoFacturacionPresupuestoCapitacion.setValorUnitarioCargado(new BigDecimal(0));
			}
				dtoProcesoFacturacionPresupuestoCapitacion.setTotalCantidadCargadaXvalorUnitarioCargado
				(
						dtoProcesoFacturacionPresupuestoCapitacion.getValorUnitarioCargado().multiply
							(new BigDecimal(dtoProcesoFacturacionPresupuestoCapitacion.getCantidadCargada()))
				);
			
		}
		
		return listaResultado;
	}
	
	
	
	/*
	public static void main(String[] args) {
		DtoProcesoFacturacionPresupuestoCapitacion parametros = new DtoProcesoFacturacionPresupuestoCapitacion();
		parametros.setFechaInicial(new Date());
		parametros.setFechaInicial(new Date());
		
		FacturasDelegate d = new FacturasDelegate();
		d.buscarFacturasPorRangoFecha(parametros);
	}
	*/
	
}