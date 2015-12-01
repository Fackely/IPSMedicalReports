package com.servinte.axioma.orm.delegate.capitacion;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.Utilidades;

import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.ParamPresupuestosCapHome;


/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link ParamPresupuestosCap} de la relación param_presupuestos_cap
 * @author diecorqu
 *  
 */
@SuppressWarnings("unchecked")
public class ParametrizacionPresupuestoCapDelegate extends ParamPresupuestosCapHome {
	

	/**
	 * Este método retorna la parametrizacion de presupuesto capitado 
	 * por contrato y fecha de vigencia del contrato
	 * 
	 * @param codigo del contrato
	 * @param año de vigencia del contrato
	 * @return PametrizacionPresupuesto
	 * @author diecorqu
	 */
	public ParamPresupuestosCap obtenerParametrizacionPresupuestoCapitado(int codContrato, String anioVigencia) {
		ParamPresupuestosCap paramPresupuestosCap = null;
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap");
		criteria.createAlias("paramPresupuestosCap.contratos", "contratos").
			add(Restrictions.eq("contratos.codigo",codContrato)).
			add(Restrictions.eq("paramPresupuestosCap.anioVigencia",anioVigencia));
		paramPresupuestosCap = (ParamPresupuestosCap)criteria.uniqueResult();
		return paramPresupuestosCap;
	}	
	
	/**
	 * Este método retorna la parametrizacion de presupuesto capitado 
	 * por codigo
	 * 
	 * @param codigo de parametrizacion de presupuesto
	 * @return PametrizacionPresupuesto
	 * @author diecorqu
	 */
	public ParamPresupuestosCap findById(int codParametrizacion) {
		ParamPresupuestosCap paramPresupuestosCap = null;
		paramPresupuestosCap = super.findById(codParametrizacion);
		paramPresupuestosCap.getContratos().getCodigo();
		return paramPresupuestosCap;
	}	
	
	/**
	 * Este método lista todas las parametrizaciones de presupuesto 
	 * capitado creadas
	 * 
	 * @return ArrayList<ParamPresupuestosCap>
	 * @author diecorqu
	 */
	public ArrayList<ParamPresupuestosCap> listarParametrizacionesPresupuestoCap() {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap");
		return (ArrayList<ParamPresupuestosCap>)criteria.list();
	}
	
	/**
	 * Este método lista las parametrizaciones de presupuesto capitado 
	 * por contrato
	 * 
	 * @param codigo del contrato
	 * @return ArrayList<DtoParamPresupCap>
	 * @author diecorqu
	 */
	public ArrayList<DtoParamPresupCap> listarParametrizacionesPresupuestoCapxContrato(int codContrato) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap");
		criteria.createAlias("paramPresupuestosCap.contratos", "contratos").
			add(Restrictions.eq("contratos.codigo",codContrato)).
			addOrder(Property.forName("anioVigencia").asc()).
			addOrder(Property.forName("contratos").asc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("paramPresupuestosCap.codigo") ,"codigoParametrizacionPresupuesto");
		projectionList.add(Projections.property("paramPresupuestosCap.anioVigencia") ,"anioVigencia");
		projectionList.add(Projections.property("paramPresupuestosCap.fechaParam") ,"fechaParam");
		projectionList.add(Projections.property("paramPresupuestosCap.contratos") ,"contrato");
		projectionList.add(Projections.property("contratos.numeroContrato") ,"numeroContrato");
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoParamPresupCap.class));
		
		ArrayList<DtoParamPresupCap> resultado = (ArrayList<DtoParamPresupCap>)criteria.list();
		
		for (DtoParamPresupCap dtoParamPresupCap : resultado) {
			dtoParamPresupCap.getContrato().getNumeroContrato();
		}
		return resultado;
	}	
	
	/**
	 * Este método verifica si existe una parametrizacion de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @param codigo del contrato
	 * @param año de vigencia del contrato
	 * @return boolean con el resultado de la operación
	 * @author diecorqu
	 */
	public boolean existeParametrizacionPresupuesto(int codContrato, String anioVigencia) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap");
		criteria.createAlias("paramPresupuestosCap.contratos", "contratos").
			add(Restrictions.eq("contratos.codigo",codContrato)).
			add(Restrictions.eq("paramPresupuestosCap.anioVigencia",anioVigencia));
		ArrayList<ParamPresupuestosCap> lista = (ArrayList<ParamPresupuestosCap>)criteria.setMaxResults(1).list();
		return (lista == null || lista.size() == 0) ? false : true;
	}
	
	/**
	 * Este método se encarga de consultar los grupos de servicio y 
	 * clases de inventario que existen por nivel de atención para un 
	 * contrato seleccionado, retornando un arreglo de dto's  con 
	 * esta información
	 * 
	 * @param ArrayList<NivelAtencion> lista de niveles de un contrato determinado 
	 * @return ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
	 * @author diecorqu
	 */
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
							obtenerNivelesAtencionPresupuestoParametrizacionGen
									(ArrayList<NivelAtencion> listaNivelesContrato) {
		
		ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesPresupuesto = 
						new ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral>();
		
		/*
		 * Se itera por cada nivel de atención para buscar los grupo de servicio y
		 * clases de inventarios presente para dicho nivel
		 */
		for (NivelAtencion nivelAtencion : listaNivelesContrato) {
			DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNiveles = 
							new DtoNivelesAtencionPresupuestoParametrizacionGeneral();
			ArrayList<ClaseInventario> listaClaseInventarios = new ArrayList<ClaseInventario>();
			ArrayList<GruposServicios> listaGruposServicios = new ArrayList<GruposServicios>();
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio = new ArrayList<DetalleValorizacionServ>();
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulos = new ArrayList<DetalleValorizacionArt>();
			boolean existenServicios = false;
			boolean existenArticulos = false;
			
			/*
			 * Se obtienen los grupos de servicio asociados a un nivel de atención
			 */
			Criteria criteriaServ = sessionFactory.getCurrentSession()
			.createCriteria(GruposServicios.class,"grupoServicio").
			createAlias("grupoServicio.servicioses", "servicio").
			createAlias("servicio.nivelAtencion", "nivelAtencion").
				add(Restrictions.eq("nivelAtencion.consecutivo", nivelAtencion.getConsecutivo()));
			criteriaServ.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("servicio.gruposServicios")));
			listaGruposServicios = (ArrayList<GruposServicios>)criteriaServ.list();
			existenServicios = (!listaGruposServicios.isEmpty()) ? true : false;
			
			/*
			 * Se obtiene los articulos asociados a un nivel y se agrupan por subgrupos
			 * para pasarselos a la siguiente consulta
			 */
			Criteria criteriaSubGrupoInventario = sessionFactory.getCurrentSession()
				.createCriteria(Articulo.class, "articulo").
				add(Restrictions.eq("articulo.nivelAtencion", nivelAtencion));
			criteriaSubGrupoInventario.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("articulo.subgrupo")));
			
			/*
			 * se obtienen las clases de inventario mediante la relación 
			 * Clase de inventario - Grupo - subgrupo
			 * pasandole a la consulta la lista de subgrupos obtenidos anteriormente 
			 */
			if(!criteriaSubGrupoInventario.list().isEmpty()) {
				Criteria criteriaArt = sessionFactory.getCurrentSession()
				.createCriteria(ClaseInventario.class,"claseInventario").
				createAlias("claseInventario.grupoInventarios", "grupoInventario").
				createAlias("grupoInventario.subgrupoInventarios", "subGrupoInventario").
					add(Restrictions.in("subGrupoInventario.codigo", criteriaSubGrupoInventario.list()));
				criteriaArt.setProjection(Projections.projectionList()
							.add(Projections.groupProperty("grupoInventario.claseInventario")));
				listaClaseInventarios = (ArrayList<ClaseInventario>)criteriaArt.list();
			}
			
			existenArticulos = (!listaClaseInventarios.isEmpty()) ? true : false;
			
			dtoNiveles.setNivelAtencion(nivelAtencion);
			dtoNiveles.setExistenServicios(existenServicios);
			dtoNiveles.setExistenArticulos(existenArticulos);
			dtoNiveles.setListaGruposServicios(listaGruposServicios);
			dtoNiveles.setListaClasesInventario(listaClaseInventarios);
			dtoNiveles.setDetalleValorizacionArticulos(detallesValorizacionArticulos);
			dtoNiveles.setDetalleValorizacionServicios(detallesValorizacionServicio);
			listaDtoNivelesPresupuesto.add(dtoNiveles);
			
			for (GruposServicios gruposServicios : listaGruposServicios) {
				gruposServicios.getDescripcion();
			}
			
			for (ClaseInventario claseInventario : listaClaseInventarios) {
				claseInventario.getNombre();
			}
		}
		
		return listaDtoNivelesPresupuesto;
	}	
	
	/**
	 * Este método se encarga de consultar los grupos de servicio y 
	 * clases de inventario que existen por nivel de atención para un 
	 * contrato seleccionado de una parametrizacion de presupuesto de 
	 * capitación existen, retornando un arreglo de dto's  con 
	 * esta información
	 * 
	 * @param ArrayList<NivelAtencion> lista de niveles de un contrato determinado 
	 * @param long código Parametrizacion
	 * @return ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
	 * @author diecorqu
	 */
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
							obtenerNivelesAtencionPresupuestoParametrizacionExistentes
									(ArrayList<NivelAtencion> listaNivelesContrato, long codigoParametrizacion) {
		ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesPresupuesto = 
						new ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral>();
		
		/*
		 * Se itera por cada nivel de atención para buscar los grupo de servicio y
		 * clases de inventarios presente para dicho nivel
		 */
		for (NivelAtencion nivelAtencion : listaNivelesContrato) {
			DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNiveles = 
							new DtoNivelesAtencionPresupuestoParametrizacionGeneral();
			ArrayList<ClaseInventario> listaClaseInventarios = new ArrayList<ClaseInventario>();
			ArrayList<GruposServicios> listaGruposServicios = new ArrayList<GruposServicios>();
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio = new ArrayList<DetalleValorizacionServ>();
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulos = new ArrayList<DetalleValorizacionArt>();
			boolean existenServicios = false;
			boolean existenArticulos = false;
			
			Log4JManager.info("Nivel: " + nivelAtencion.getConsecutivo() + " " + nivelAtencion.getDescripcion());
			
			/*
			 * Se obtienen los grupos de servicio asociados a un nivel de atención 
			 * de una parametrizacion de presupuesto de capitación existente
			 */
			Criteria criteriaServ = sessionFactory.getCurrentSession().
				createCriteria(GruposServicios.class,"grupoServicio").
					createAlias("grupoServicio.detalleValorizacionServs", "detalleValServicio").
					createAlias("detalleValServicio.nivelAtencion", "nivelAtencion").
					createAlias("detalleValServicio.paramPresupuestosCap", "paramPresupCap").
						add(Restrictions.eq("nivelAtencion.consecutivo", nivelAtencion.getConsecutivo())).
						add(Restrictions.eq("paramPresupCap.codigo", codigoParametrizacion));
			criteriaServ.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("detalleValServicio.gruposServicios")));
			listaGruposServicios = (ArrayList<GruposServicios>)criteriaServ.list();
			existenServicios = (!listaGruposServicios.isEmpty()) ? true : false;
			
			/*
			 * Se obtienen las clases de inventario asociados a un nivel de atención
			 * de una parametrizacion de presupuesto de capitación existente
			 */
			Criteria criteriaSubGrupoInventario = sessionFactory.getCurrentSession().
				createCriteria(ClaseInventario.class,"claseInventario").
					createAlias("claseInventario.detalleValorizacionArts", "detalleValClaseInventario").
					createAlias("detalleValClaseInventario.nivelAtencion", "nivelAtencion").
					createAlias("detalleValClaseInventario.paramPresupuestosCap", "paramPresupCap").
						add(Restrictions.eq("nivelAtencion.consecutivo", nivelAtencion.getConsecutivo())).
						add(Restrictions.eq("paramPresupCap.codigo", codigoParametrizacion));
			criteriaSubGrupoInventario.setProjection(Projections.projectionList()
						.add(Projections.groupProperty("detalleValClaseInventario.claseInventario")));
			listaClaseInventarios = (ArrayList<ClaseInventario>)criteriaSubGrupoInventario.list();
			existenArticulos = (!listaClaseInventarios.isEmpty()) ? true : false;
			
			dtoNiveles.setNivelAtencion(nivelAtencion);
			dtoNiveles.setExistenServicios(existenServicios);
			dtoNiveles.setExistenArticulos(existenArticulos);
			dtoNiveles.setListaGruposServicios(listaGruposServicios);
			dtoNiveles.setListaClasesInventario(listaClaseInventarios);
			dtoNiveles.setDetalleValorizacionArticulos(detallesValorizacionArticulos);
			dtoNiveles.setDetalleValorizacionServicios(detallesValorizacionServicio);
			listaDtoNivelesPresupuesto.add(dtoNiveles);
			
			for (GruposServicios gruposServicios : listaGruposServicios) {
				gruposServicios.getDescripcion();
			}
			
			for (ClaseInventario claseInventario : listaClaseInventarios) {
				claseInventario.getNombre();
			}
		}
		
		return listaDtoNivelesPresupuesto;
	}	
	
	
	/**
	 * Este método guarda la parametrizacion de presupuesto capitado ingresada
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 * @author diecorqu
	 */
	public boolean guardarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		boolean save = false;
		try{
			super.persist(parametrizacionPresupuesto);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar el adjunto ",e);
		}		
		return save;
	}	
	
	/**
	 * Este método modifica la parametrizacion de presupuesto capitado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 * @author diecorqu
	 */
	public ParamPresupuestosCap modificarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		return super.merge(parametrizacionPresupuesto);
	}	
	
	/**
	 * Este método elimina la parametrizacion de presupuesto capitado
	 * 
	 * @param código parametrización a eliminar
	 * @author diecorqu
	 */
	public void eliminarParametrizacionPresupuesto(ParamPresupuestosCap parametrizacion) {
		super.delete(parametrizacion);
	}	
	
	/**
	 * Este método verifica si existe una parametrizacion de presupuesto 
	 * capitado para el convenio y la fecha de vigencia ingresados
	 * 
	 * @param codigo del convenio
	 * @param año de vigencia del contrato
	 * @return boolean con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public boolean existeParametrizacionPresupuestoConvenio(int codConvenio, String anioVigencia) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap")
			.createAlias("paramPresupuestosCap.contratos", "contratos")
			.createAlias("contratos.convenios", "convenio");
			criteria.add(Restrictions.eq("convenio.codigo",codConvenio)).
					add(Restrictions.eq("paramPresupuestosCap.anioVigencia",anioVigencia));
		ArrayList<ParamPresupuestosCap> lista = (ArrayList<ParamPresupuestosCap>)criteria.list();
		return (lista == null || lista.size() == 0) ? false : true;
	}
	
	/**
	 * @author diecorqu
	 * 
	 * Verifica si el nivel pasado por parámetro esta asociado a una Parametrización
	 * del Presupuesto de Capitación
	 * @param codNivelAtencion
	 * @return boolean
	 */
	public boolean existeNivelAtencionPresupuestoCapitacion(long codNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession().
		createCriteria(ParamPresupuestosCap.class, "paramPresupCap");
		criteria.createAlias("paramPresupCap.contratos", "contratos");
		criteria.createAlias("paramPresupCap.valorizacionPresCapGens", "valorizacion");
		criteria.createAlias("valorizacion.nivelAtencion", "nivelAtencion").
			add(Restrictions.gt("contratos.fechaFinal", Date.valueOf(Utilidades.capturarFechaBD()))).
			add(Restrictions.eq("nivelAtencion.codigo", codNivelAtencion));
		criteria.setProjection(Projections.projectionList().
			add(Projections.groupProperty("paramPresupCap.codigo")));
		ArrayList<NivelAtencion> lista = (ArrayList<NivelAtencion>)criteria.setMaxResults(1).list();
		return (lista == null || lista.size() == 0) ? false : true;
	}
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado detallado para el convenio, contrato, servicio y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoDetalladoServicios(int codContrato, String anioVigencia, 
				int mes, int codigoGrupoServicio, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap")
			.createAlias("paramPresupuestosCap.contratos", "contratos")
			.createAlias("paramPresupuestosCap.detalleValorizacionServs", "valorizacionServ")
			.createAlias("valorizacionServ.gruposServicios", "grupoServicio")
			.createAlias("valorizacionServ.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("contratos.codigo",codContrato))
				.add(Restrictions.eq("paramPresupuestosCap.anioVigencia",anioVigencia))
				.add(Restrictions.eq("valorizacionServ.mes",mes))
				.add(Restrictions.eq("grupoServicio.codigo",codigoGrupoServicio))
				.add(Restrictions.eq("nivelAtencion.consecutivo",consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("valorizacionServ.valorGasto"),"valorPresupuestado"));
		
		List<BigDecimal> lista = (List<BigDecimal>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			BigDecimal valorPresupuestado = lista.get(0);
			resultado = valorPresupuestado.doubleValue();
		}
		return resultado;
	}
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado General para el convenio, contrato, Servicio/Articulo y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(int codContrato, String anioVigencia, 
				int mes, long consecutivoNivelAtencion, String servicioArticulo) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap")
			.createAlias("paramPresupuestosCap.contratos", "contratos")
			.createAlias("paramPresupuestosCap.valorizacionPresCapGens", "valorizacionGeneral")
			.createAlias("valorizacionGeneral.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("paramPresupuestosCap.anioVigencia", anioVigencia))
				.add(Restrictions.eq("valorizacionGeneral.mes", mes))
				.add(Restrictions.eq("valorizacionGeneral.subSeccion", servicioArticulo))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("valorizacionGeneral.valorGastoSubSeccion"),"valorPresupuestado"));
		
		List<BigDecimal> lista = (List<BigDecimal>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			BigDecimal valorPresupuestado = lista.get(0);
			resultado = valorPresupuestado.doubleValue();
		}
		return resultado;
	}
	
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado detallado para el convenio, contrato, articulo y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoClaseInventario
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoDetalladoArticulos(int codContrato, String anioVigencia, 
				int mes, int codigoClaseInventario, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ParamPresupuestosCap.class,"paramPresupuestosCap")
			.createAlias("paramPresupuestosCap.contratos", "contratos")
			.createAlias("paramPresupuestosCap.detalleValorizacionArts", "valorizacionArt")
			.createAlias("valorizacionArt.claseInventario", "claseInventario")
			.createAlias("valorizacionArt.nivelAtencion", "nivelAtencion");
		
		criteria.add(Restrictions.eq("contratos.codigo", codContrato))
				.add(Restrictions.eq("paramPresupuestosCap.anioVigencia", anioVigencia))
				.add(Restrictions.eq("valorizacionArt.mes", mes))
				.add(Restrictions.eq("claseInventario.codigo", codigoClaseInventario))
				.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivelAtencion));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("valorizacionArt.valorGasto"),"valorPresupuestado"));
		
		List<BigDecimal> lista = (List<BigDecimal>)criteria.list();
		Double resultado= null;
		if(lista != null && !lista.isEmpty()){
			BigDecimal valorPresupuestado = lista.get(0);
			resultado = valorPresupuestado.doubleValue();
		}
		return resultado;
	}
	
}
