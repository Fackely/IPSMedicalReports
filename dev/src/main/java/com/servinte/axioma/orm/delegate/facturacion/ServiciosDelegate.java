package com.servinte.axioma.orm.delegate.facturacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.ServiciosHome;
import org.apache.log4j.Logger;

public class ServiciosDelegate extends ServiciosHome{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Servicios
	 */
	public Servicios obtenerServicioPorId(int id) {
		return super.findById(id);
	}
	
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(ServiciosDelegate.class);
	/**
	 * Este Método se encarga de consultar el tipo, especialidad y grupo de
	 * un servicio para comparar con la agrupación de servicios
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 */
	public DtoServicios obtenerTipoEspecialidadGrupoServicioPorID(int codigoServicio){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Servicios.class,"servicio");
		
		criteria.createAlias("servicio.tiposSerNaturaleza"		, "tipoServicioNat");
		criteria.createAlias("tipoServicioNat.tiposServicio"	, "tipoServicio");
		criteria.createAlias("servicio.especialidades"			, "especialidad");
		criteria.createAlias("servicio.gruposServicios"			, "grupoServicio");
		criteria.createAlias("grupoServicio.tiposMonto"			, "tiposMonto" , Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("servicio.codigo")			,"codigoServicio")
			.add(Projections.property("tipoServicio.acronimo")		,"acronimoTipoServicio")
			.add(Projections.property("especialidad.codigo")		,"codigoEspecialidad")
			.add(Projections.property("grupoServicio.codigo")		,"codigoGrupoServicio")
			.add(Projections.property("tiposMonto.codigo")			,"codigoTipoMonto")) // Tipo Monto Autorización Capitación de Ord. Ambulatorias
		);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
		
		DtoServicios servicio = (DtoServicios)criteria.uniqueResult();
				
		return servicio;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los servicios dependiendo
	 * del tipo tarifario oficial
	 * 
	 * @param codigoServicio si es codigo nuna valido devuelve todos los servicios por codigo tarifario
	 * @param codigoTarifarioOficial codigo tarifario
	 * @return ArrayList<DtoServicios>
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoServicios> obtenerServiciosXTipoTarifarioOficial(int codigoServicio,int codigoTarifarioOficial){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Servicios.class,"servicio");
		
		criteria.createAlias("servicio.referenciasServicios", "referenciaServicio")
				.createAlias("referenciaServicio.tarifariosOficiales", "tarifarioOficial")
		 		;
		
		if(codigoServicio!=ConstantesBD.codigoNuncaValido)
			criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
		
		criteria.add(Restrictions.eq("tarifarioOficial.codigo", codigoTarifarioOficial));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("servicio.codigo"),"codigoServicio")
			.add(Projections.property("referenciaServicio.codigoPropietario"),"codigoPropietarioServicio")
			.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio")
			
		));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
		
		ArrayList<DtoServicios>  listaServicios = 
			(ArrayList<DtoServicios> )criteria.list();
				
		return listaServicios;
	}
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente ISS de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa ISS
	 * @author, Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	public DtoServicios obtenerTarifaISSVigenteServicios(int codigoServicio,int esquemaTarifario){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Servicios.class,"servicio");
		
		criteria.createAlias("servicio.referenciasServicios", "referenciaServicio")
				.createAlias("servicio.tarifasIsses", "tarifaServicioISS")
				.createAlias("tarifaServicioISS.esquemasTarifarios", "esquemaTarifarioISS")
		 		;
		
		if(codigoServicio!=ConstantesBD.codigoNuncaValido)
			criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
		
		criteria.add(Restrictions.eq("esquemaTarifarioISS.codigo", esquemaTarifario));
		
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.le("tarifaServicioISS.fechaVigencia", UtilidadFecha.getFechaActualTipoBD()));
		disjunction.add(Restrictions.isNull("tarifaServicioISS.fechaVigencia"));
		criteria.add(disjunction);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("servicio.codigo"),"codigoServicio")
			.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio")
			.add(Projections.property("tarifaServicioISS.valorTarifa"),"valorTarifa")
			.add(Projections.property("tarifaServicioISS.fechaVigencia"),"fechaVigenciaTarifa")
			//MT 6532-6438
			.add(Projections.property("tarifaServicioISS.tiposLiquidacionSoat.codigo"),"codigoTipoLiquidacion")
		));
		
		criteria.addOrder( Order.desc("tarifaServicioISS.fechaVigencia") );
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
		
		ArrayList<DtoServicios>  listaServicios = 
			(ArrayList<DtoServicios> )criteria.list();
		
		//---- OBTENER LA TARIFA VIGENTE
		if(Utilidades.isEmpty(listaServicios)){
			return null;
		}
		else{
			//--Se toma la ultima fecha de vigencia antes de la actual  
			if(listaServicios.size()==1)//Si solo existe un registro lo devuelve
			{
				return listaServicios.get(0);
			}
			else
			{
				if(listaServicios.get(0).getFechaVigenciaTarifa()==null)//No devuelve la tarifa nula si existen mas registros
				{
					return listaServicios.get(1);
				}
				else
				{
					return listaServicios.get(0);
				}
			}
		}
	}
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente SOAT de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa SOAT
	 * @author, Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	public DtoServicios obtenerTarifaSOATVigenteServicios(int codigoServicio,int esquemaTarifario){
		try{
			Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(Servicios.class,"servicio");
			
			criteria.createAlias("servicio.referenciasServicios", "referenciaServicio")
					.createAlias("servicio.tarifasSoats", "tarifaServicioSOAT")
					.createAlias("tarifaServicioSOAT.esquemasTarifarios", "esquemaTarifarioSOAT")
			 		;
			
			if(codigoServicio!=ConstantesBD.codigoNuncaValido)
				criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
			
			criteria.add(Restrictions.eq("esquemaTarifarioSOAT.codigo", esquemaTarifario));
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.le("tarifaServicioSOAT.fechaVigencia", UtilidadFecha.getFechaActualTipoBD()));
			disjunction.add(Restrictions.isNull("tarifaServicioSOAT.fechaVigencia"));
			criteria.add(disjunction);
			
			criteria.setProjection(Projections.distinct(Projections.projectionList()				
				.add(Projections.property("servicio.codigo"),"codigoServicio")
				.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio")
				.add(Projections.property("tarifaServicioSOAT.valorTarifa"),"valorTarifa")
				.add(Projections.property("tarifaServicioSOAT.fechaVigencia"),"fechaVigenciaTarifa")
					//MT 6532-6438
				.add(Projections.property("tarifaServicioISS.tiposLiquidacionSoat.codigo"),"codigoTipoLiquidacion")
				
			));
			
			criteria.addOrder( Order.desc("tarifaServicioSOAT.fechaVigencia") );
					
			criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
			
			ArrayList<DtoServicios>  listaServicios = 
				(ArrayList<DtoServicios> )criteria.list();
			
			//---- OBTENER LA TARIFA VIGENTE
			if(Utilidades.isEmpty(listaServicios)){
				return null;
			}
			else{
				//--Se toma la ultima fecha de vigencia antes de la actual  
				if(listaServicios.size()==1)//Si solo existe un registro lo devuelve
				{
					return listaServicios.get(0);
				}
				else
				{
					if(listaServicios.get(0).getFechaVigenciaTarifa()==null)//No devuelve la tarifa nula si existen mas registros
					{
						return listaServicios.get(1);
					}
					else
					{
						return listaServicios.get(0);
					}
				}
			}
		}
		catch (Exception e) {
			Log4JManager.warning("No se encontráron tarifas");
			return null;
		}
	}
	
	
	/**
	 * Este Método se encarga de obtener los distintos servicios
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Servicios>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		Calendar fInicio=Calendar.getInstance();
		Calendar fFin=Calendar.getInstance();
		Date fechaInicio;
		Date fechaFin;
		fInicio=(Calendar)meses.get(0).clone();
		fInicio.set(Calendar.DAY_OF_MONTH, fInicio.getActualMinimum(Calendar.DAY_OF_MONTH));
		if(meses.size() > 1){
			fFin=(Calendar)meses.get(meses.size()-1).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		else{
			fFin=(Calendar)meses.get(0).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		fechaInicio=fInicio.getTime();
		fechaFin=fFin.getTime();
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Servicios.class, "servicios");
		 	criteria.createAlias("servicios.cierreNivelAteGruServs", "cierreServicios")
		 			.createAlias("servicios.nivelAtencion", "nivelAtencion")
		 			.createAlias("servicios.referenciasServicios", "referencia")
		 			.createAlias("cierreServicios.contratos", "contratos")
		 			.createAlias("contratos.convenios", "convenio");
		 	criteria.add(Restrictions.eq("convenio.codigo", codigoConvenio))
		 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
		 			.add(Restrictions.eq("cierreServicios.tipoProceso", proceso))
		 			.add(Restrictions.between("cierreServicios.fechaCierre", fechaInicio, fechaFin));
			criteria.addOrder(Order.asc("referencia.descripcion"))		
		 			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (ArrayList<Servicios>)criteria.list();
	}
	
	
	/**
	 * Este Método se encarga de obtener los distintos servicios
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Servicios>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses){
		Calendar fInicio=Calendar.getInstance();
		Calendar fFin=Calendar.getInstance();
		Date fechaInicio;
		Date fechaFin;
		fInicio=(Calendar)meses.get(0).clone();
		fInicio.set(Calendar.DAY_OF_MONTH, fInicio.getActualMinimum(Calendar.DAY_OF_MONTH));
		if(meses.size() > 1){
			fFin=(Calendar)meses.get(meses.size()-1).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		else{
			fFin=(Calendar)meses.get(0).clone();
			fFin.set(Calendar.DAY_OF_MONTH, fFin.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		fechaInicio=fInicio.getTime();
		fechaFin=fFin.getTime();
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Servicios.class, "servicios");
		 	criteria.createAlias("servicios.cierreNivelAteGruServs", "cierreServicios")
		 			.createAlias("servicios.nivelAtencion", "nivelAtencion")
		 			.createAlias("servicios.referenciasServicios", "referencia")
		 			.createAlias("cierreServicios.contratos", "contrato");
		 	criteria.add(Restrictions.eq("contrato.codigo", codigoContrato))
		 			.add(Restrictions.eq("nivelAtencion.consecutivo", consecutivoNivel))
		 			.add(Restrictions.eq("cierreServicios.tipoProceso", proceso))
		 			.add(Restrictions.between("cierreServicios.fechaCierre", fechaInicio, fechaFin));
		 	criteria.addOrder(Order.asc("referencia.descripcion"))	
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 return (ArrayList<Servicios>)criteria.list();
	}
	
	
	/**
	 * 
	 * M&eacute;todo que permite consultar el tipo de servicio y grupo 
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 07/07/2011
	 * 
	 */	
	public DtoServicios obtenerTipoGrupoServicio(int codigoServicio){
		
		/*Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Servicios.class,"servicio");
		
		criteria.createAlias("servicio.tiposSerNaturaleza"		, "tipoServicioNat");
		criteria.createAlias("tipoServicioNat.tiposServicio"	, "tipoServicio");
		criteria.createAlias("servicio.gruposServicios"			, "grupoServicio");
		criteria.createAlias("grupoServicio.tiposMonto"			, "tiposMonto", Criteria.LEFT_JOIN );
		
		criteria.add(Restrictions.eq("servicio.codigo", codigoServicio));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()			
			.add(Projections.property("tipoServicio.acronimo")		,"acronimoTipoServicio")			
			.add(Projections.property("grupoServicio.codigo")		,"codigoGrupoServicio")
			.add(Projections.property("grupoServicio.tiposMonto")			,"tipoMonto")
			//.add(Projections.property("tiposMonto.codigo")			,"tipoMonto")		
		));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
		
		//DtoServicios servicio = (DtoServicios)criteria.uniqueResult();*/
		 
		//MT 6029
		String consulta="select "+
		"new com.princetonsa.dto.facturacion.DtoServicios( tipoServicio.acronimo, " +
		"grupoServicio.codigo,  " +
		"case " +
			"when tiposMonto.codigo=null " +
			"then "+ConstantesBD.codigoNuncaValido+
			" else tiposMonto.codigo " +
		"end) "+		
		"from "+
		Servicios.class.getName()+" servicio "+
		"inner join servicio.tiposSerNaturaleza tipoServicioNat "+
		"inner join tipoServicioNat.tiposServicio tipoServicio "+
		"inner join servicio.gruposServicios grupoServicio "+
		"left join grupoServicio.tiposMonto  tiposMonto "+
		"where "+"servicio.codigo= :codigoServicio ";
				
		Query query=sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoServicio", codigoServicio);
		DtoServicios servicio = (DtoServicios)query.uniqueResult();
		//Fin  MT
				
		return servicio;
	}

	
	
	
	
	
	/**
	 * Retorna un servicio y su tipo de liquidación de acuerdo al esquema tarifario enviado.
	 * Este método solo esta implementado para los esquemas tárifarios 1 y 2, es decir SOAT e ISS
	 * @param codigoServicio
	 * @param esquemaTarifario
	 * @param listaTiposLiquidacion
	 * @param listaTiposServicio
	 * @return ArrayList<DtoServicios>
	 *
	 * @autor Cristhian Murillo
	*/
	@SuppressWarnings("unchecked")
	public ArrayList<DtoServicios> obtenerServicioLiquidacionPorEsquema(int codigoServicio, int codigoEsquemaTarifario, 
			List<Integer> listaTiposLiquidacion, List<String> listaTiposServicio)
	{
		
		Date fecha=UtilidadFecha.getFechaActualTipoBD();
		/* Para evitar que se dañe la consulta con algúno de los esquemas tárifarios no tenidos en cuenta en las validaciones */
		if(codigoEsquemaTarifario != ConstantesBD.codigoEsqTarifarioGeneralTodosIss || codigoEsquemaTarifario == ConstantesBD.codigoEsqTarifarioGeneralTodosSoat){
			return null;
		}
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Servicios.class,"servicio");
		
		//criteria.createAlias("servicio.referenciasServicios"				, "referenciaServicio");
		
		if(codigoEsquemaTarifario == ConstantesBD.codigoEsqTarifarioGeneralTodosIss){
			criteria.createAlias("servicio.tarifasIsses"					, "tarifaServicio");
		}
		else if(codigoEsquemaTarifario == ConstantesBD.codigoEsqTarifarioGeneralTodosSoat){
			criteria.createAlias("servicio.tarifasSoats"					, "tarifaServicio");
		}
		
		criteria.createAlias("tarifaServicio.esquemasTarifarios"			, "esquemasTarifarios");
		criteria.createAlias("tarifaServicio.tiposLiquidacionSoat"			, "tiposLiquidacionSoat");
		criteria.createAlias("servicio.tiposSerNaturaleza"					, "tiposSerNaturaleza");
		
		criteria.add(Restrictions.eq("servicio.codigo"						, codigoServicio));
		criteria.add(Restrictions.eq("esquemasTarifarios.codigo"			, codigoEsquemaTarifario));
		
		
		if(listaTiposLiquidacion.size()==0){
			criteria.add(Restrictions.in("tiposLiquidacionSoat.codigo"			, listaTiposLiquidacion));
		}
		
		if(listaTiposServicio.size()==0){
			criteria.add(Restrictions.in("tiposSerNaturaleza.id.tipoServicio"	, listaTiposServicio));
		}
		//MT6586
		criteria.add(Restrictions.le("tarifaServicio.fechaVigencia", fecha));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
			.add(Projections.property("servicio.codigo")					,"codigoServicio")
			//.add(Projections.property("referenciaServicio.descripcion")		,"descripcionServicio")
			.add(Projections.property("esquemasTarifarios.codigo")			,"codigoEsquemaTarifario")
			.add(Projections.property("esquemasTarifarios.nombre")			,"nombreEsquemaTarifario")
			.add(Projections.property("tiposLiquidacionSoat.codigo")		,"codigoTipoLiquidacion")
			.add(Projections.property("tiposLiquidacionSoat.nombre")		,"nombreTipoLoquidacion")
			.add(Projections.property("tiposLiquidacionSoat.acronimo")		,"acronimoTipoLoquidacion")
			.add(Projections.property("tiposSerNaturaleza.id.tipoServicio")	,"tipoServicio")
			//MT6586
			.add(Projections.property("tarifaServicio.fechaVigencia")	,"fechaVigenciaTarifa")
		));
		//MT6586
		criteria.addOrder( Order.desc("tarifaServicio.fechaVigencia") );
					
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServicios.class));
		
		return (ArrayList<DtoServicios>)criteria.list();
	}
	
	/*
	public static void main(String[] args) 
	{
		ServiciosDelegate d = new ServiciosDelegate();
		
		ArrayList<Integer> listaTiposLiquidacion = new ArrayList<Integer>();
		listaTiposLiquidacion.add(5);
		listaTiposLiquidacion.add(6);
		
		ArrayList<String> listaTiposServicio = new ArrayList<String>();
		listaTiposServicio.add(ConstantesBD.codigoServicioNoCruentos+"");
		listaTiposServicio.add(ConstantesBD.codigoServicioQuirurgico+"");
		
		d.obtenerServicioLiquidacionPorEsquema(1, 2, listaTiposLiquidacion, listaTiposServicio);
	}
	*/
	/**
	 * Retorna la tarifaIss vigente
	 * @param con
	 * @param con
	 * @param codServicio
	 * @return DtoServicios
	 * @autor Sandra Milena Barreto 
	 * Mt6587
	*/
	
public DtoServicios obtenerTarifaIssVigente( int esquemaTarifario, int codServicio){
		
	DtoServicios infoTarifa= new DtoServicios();
	PreparedStatement pst=null;
	ResultSet rs=null;
	Connection con = null;
	try{
		con = UtilidadBD.abrirConexion();
					String cadenaConsultaTarifas="(" +
							"SELECT " +
								"v.codigo AS codigo, " +
								"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
								"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
								"v.valor_tarifa AS valorTarifa, " +													
								"v.codigo_servicio AS codigoServicioCups, " +											
								"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
								"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +				
								"v.fecha_vigencia AS fechavigencia "+
							"FROM " +
								"facturacion.view_tarifas_iss v " +
							"WHERE " +
								"v.codigo_esquema_tarifario=? " +
								"AND v.codigo_servicio=? " +
								"and v.fecha_vigencia<=CURRENT_DATE " +
								
						") " +
						"UNION ALL " +
						"(" +
						"SELECT " +
							"v.codigo AS codigo, " +
							"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
							"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
							"v.valor_tarifa AS valorTarifa, " +													
							"v.codigo_servicio AS codigoServicioCups, " +
							"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
							"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +
							"v.fecha_vigencia AS fechavigencia "+
						"FROM " +
							"facturacion.view_tarifas_iss v " +
						"WHERE " +
							"v.codigo_esquema_tarifario=? " +
							"AND v.codigo_servicio=? " +
							"AND v.fecha_vigencia is null "+
						") ";
					
					if(!cadenaConsultaTarifas.equals(""))
					{
						pst= con.prepareStatement(cadenaConsultaTarifas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst.setInt(1,esquemaTarifario);
						pst.setInt(2,codServicio);
						pst.setInt(3,esquemaTarifario);
						pst.setInt(4,codServicio);
						rs = pst.executeQuery();
						logger.info(""+cadenaConsultaTarifas);
						if(rs.next())
						{	
							infoTarifa.setCodigoServicio(rs.getInt("codigoServicioCups"));
							infoTarifa.setCodigoEsquemaTarifario(rs.getInt("codigoEsquemaTarifario"));
							infoTarifa.setNombreEsquemaTarifario(rs.getString("nombreEsquemaTarifario"));
							infoTarifa.setValorTarifa(rs.getDouble("valorTarifa"));
							infoTarifa.setCodigoTipoLiquidacion(rs.getInt("codigoTipoLiquidacion"));
							infoTarifa.setNombreTipoLoquidacion(rs.getString("nombreTipoLiquidacion"));
							infoTarifa.setFechaVigenciaTarifa(rs.getDate("fechavigencia"));
							
						}	
						else{
							infoTarifa=null;
						}
					}
						
					
	}
	catch(SQLException sqe){
		logger.error("############## SQLException ERROR obtenerTarifaServicio",sqe);
	}
	catch(Exception e){
		logger.error("############## ERROR obtenerTarifaServicio", e);
	}
	finally{
		try{
			if(rs != null){
				rs.close();
			}
			if(pst != null){
				pst.close();
			}
			if(con != null){
				con.close();
			}
		}
		catch (SQLException se) {
			logger.error("###########  Error close ResultSet - PreparedStatement", se);
		}
	}
	logger.info("############## Fin obtenerTarifaServicio");
		return infoTarifa;
	}

/**
 * Retorna la tarifaIss vigente Soat
 * @param con
 * @param con
 * @param codServicio
 * @return DtoServicios
 * @autor Sandra Milena Barreto 
*/
public DtoServicios obtenerTarifaSoatVigente(int esquemaTarifario, int codServicio){
	
	DtoServicios infoTarifaSoat= new DtoServicios();
	PreparedStatement pst=null;
	ResultSet rs=null;
	Connection con = null;
	try{
		con = UtilidadBD.abrirConexion();
					String cadenaConsultaTarifas="(" +
							"SELECT " +
								"v.codigo AS codigo, " +
								"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
								"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
								"v.valor_tarifa AS valorTarifa, " +													
								"v.codigo_servicio AS codigoServicioCups, " +											
								"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
								"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +				
								"v.fecha_vigencia AS fechavigencia "+
							"FROM " +
								"facturacion.view_tarifas_soat v " +
							"WHERE " +
								"v.codigo_esquema_tarifario=? " +
								"AND v.codigo_servicio=? " +
								"and v.fecha_vigencia<=CURRENT_DATE " +
								
						") " +
						"UNION ALL " +
						"(" +
						"SELECT " +
							"v.codigo AS codigo, " +
							"v.codigo_esquema_tarifario AS codigoEsquemaTarifario, " +
							"v.nombre_esquema_tarifario AS nombreEsquemaTarifario, " +
							"v.valor_tarifa AS valorTarifa, " +													
							"v.codigo_servicio AS codigoServicioCups, " +
							"v.codigo_tipo_liquidacion AS codigoTipoLiquidacion, " +
							"v.nombre_tipo_liquidacion AS nombreTipoLiquidacion," +
							"v.fecha_vigencia AS fechavigencia "+
						"FROM " +
							"facturacion.view_tarifas_soat v " +
						"WHERE " +
							"v.codigo_esquema_tarifario=? " +
							"AND v.codigo_servicio=? " +
							"AND v.fecha_vigencia is null "+
						") ";
					
					if(!cadenaConsultaTarifas.equals(""))
					{
						pst= con.prepareStatement(cadenaConsultaTarifas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst.setInt(1,esquemaTarifario);
						pst.setInt(2,codServicio);
						pst.setInt(3,esquemaTarifario);
						pst.setInt(4,codServicio);
						rs = pst.executeQuery();
						logger.info(""+cadenaConsultaTarifas);
						if(rs.next())
						{	
							infoTarifaSoat.setCodigoServicio(rs.getInt("codigoServicioCups"));
							infoTarifaSoat.setCodigoEsquemaTarifario(rs.getInt("codigoEsquemaTarifario"));
							infoTarifaSoat.setNombreEsquemaTarifario(rs.getString("nombreEsquemaTarifario"));
							infoTarifaSoat.setValorTarifa(rs.getDouble("valorTarifa"));
							infoTarifaSoat.setCodigoTipoLiquidacion(rs.getInt("codigoTipoLiquidacion"));
							infoTarifaSoat.setNombreTipoLoquidacion(rs.getString("nombreTipoLiquidacion"));
							infoTarifaSoat.setFechaVigenciaTarifa(rs.getDate("fechavigencia"));
							
						}	
						else{
							infoTarifaSoat=null;
						}
					}
						
					
	}
	catch(SQLException sqe){
		logger.error("############## SQLException ERROR obtenerTarifaServicio",sqe);
	}
	catch(Exception e){
		logger.error("############## ERROR obtenerTarifaServicio", e);
	}
	finally{
		try{
			if(rs != null){
				rs.close();
			}
			if(pst != null){
				pst.close();
			}
			if(con != null){
				con.close();
			}
		}
		catch (SQLException se) {
			logger.error("###########  Error close ResultSet - PreparedStatement", se);
		}
	}
	logger.info("############## Fin obtenerTarifaServicio");
		return infoTarifaSoat;
	}
	
	
}
