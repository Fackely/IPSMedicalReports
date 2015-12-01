/*
 * Mayo 12, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.DetCargos;
import com.servinte.axioma.orm.DetCargosHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings("unchecked")
public class DetCargosDelegate extends DetCargosHome{
	
	
	
	/**
	 * Lista todos
	 */
	public ArrayList<DetCargos> listarTodos()
	{
		return (ArrayList<DetCargos>) sessionFactory.getCurrentSession()
			.createCriteria(DetCargos.class)
			.list();
	}
	
	
	
	
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro)
	{
		String sql =
			"SELECT DISTINCT "+
			  "'articulo' AS tipo, "+
			  "det_cargos.valor_unitario_tarifa AS tarifa, "+ 
			  "articulo.nivel_atencion AS nivel_atencion, "+
			  "articulo.codigo AS cod_articulo_o_servicio, "+ 
			  "articulo.descripcion AS descripcion, "+
			  "-1   AS codigo_grupo_servicio, "+
			  "null AS descripcion_servicio, "+
			  "det_cargos.cantidad_cargada AS cantidad, "+
			  "solicitudes.fecha_solicitud AS fecha, "+
			  "det_cargos.convenio AS convenio, "+
			  "det_cargos.contrato AS contrato, "+
			  "solicitudes.numero_solicitud AS numero_solicitud, "+ 
			  "articulo.subgrupo AS subgrupo "+
			"FROM "+
			  "facturacion.det_cargos, "+ 
			  "inventarios.articulo, "+
			  "ordenes.solicitudes "+
			"WHERE "+
			  "det_cargos.articulo = articulo.codigo AND "+
			  "det_cargos.solicitud = solicitudes.numero_solicitud AND "+
			  "REEMPLAZO_CONVENIO "+
			  "REEMPLAZO_CONTRATO "+
			  "det_cargos.tipo_solicitud <> ? AND "+
			  "det_cargos.estado IN (?,?) AND "+
			  "(solicitudes.fecha_solicitud BETWEEN ? AND ? OR det_cargos.fecha_modifica BETWEEN ? AND ?) "+
	
			"UNION ALL "+
	
			"SELECT DISTINCT "+
			 "'servicio' AS tipo, "+
			  "det_cargos.valor_unitario_tarifa AS tarifa, "+ 
			  "servicios.nivel AS nivel_atencion, "+
			  "servicios.codigo as cod_articulo_o_servicio, "+ 
			  "grupos_servicios.descripcion AS descripcion, "+
			  "grupos_servicios.codigo AS codigo_grupo_servicio, "+
			  "referencias_servicio.descripcion AS descripcion_servicio, "+
			  "det_cargos.cantidad_cargada AS cantidad, "+
			  "solicitudes.fecha_solicitud AS fecha, "+
			  "det_cargos.convenio AS convenio, "+
			  "det_cargos.contrato AS contrato, "+
			  "solicitudes.numero_solicitud AS numero_solicitud, "+
			  "-1 AS subgrupo "+
			"FROM "+
			  "facturacion.det_cargos, "+ 
			  "facturacion.servicios, "+
			  "facturacion.grupos_servicios, "+ 
			  "ordenes.solicitudes, "+
			  "facturacion.referencias_servicio "+
			"WHERE "+
			  "det_cargos.servicio = servicios.codigo AND "+
			  "det_cargos.solicitud = solicitudes.numero_solicitud AND "+
			  "servicios.grupo_servicio = grupos_servicios.codigo AND "+
			  "servicios.codigo = referencias_servicio.servicio AND "+
			  "REEMPLAZO_CONVENIO "+
			  "REEMPLAZO_CONTRATO "+ 
			  "det_cargos.tipo_solicitud <> ? AND "+
			  "det_cargos.estado IN (?,?) AND "+
			  "(solicitudes.fecha_solicitud BETWEEN ? AND ? OR det_cargos.fecha_modifica BETWEEN ? AND ?) AND "+
			  "(referencias_servicio.tipo_tarifario IS NULL OR referencias_servicio.tipo_tarifario = ?) ";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<DtoConsultaProcesoCargosCuenta> lista = new ArrayList<DtoConsultaProcesoCargosCuenta>();
		try
		{
			Date fechaFin=new Date();
			Calendar fechaCalendar=Calendar.getInstance();
			fechaCalendar.setTime(dtoFiltro.getFechaFin());
			fechaCalendar.add(Calendar.DAY_OF_MONTH, 1);
			fechaFin=fechaCalendar.getTime();
			
			if(dtoFiltro.getConvenio() != null)
			{
				sql = sql.replaceAll("REEMPLAZO_CONVENIO", "det_cargos.convenio = " + dtoFiltro.getConvenio() + " AND ");
			}
			else
			{
				sql = sql.replaceAll("REEMPLAZO_CONVENIO", " ");
			}
			
			if(dtoFiltro.getContrato() != null)
			{
				sql = sql.replaceAll("REEMPLAZO_CONTRATO", "det_cargos.contrato = " + dtoFiltro.getContrato() + " AND " );
			}
			else
			{
				sql = sql.replaceAll("REEMPLAZO_CONTRATO", " ");
			}
			
			con = UtilidadBD.abrirConexion();
			ps = con.prepareStatement(sql);

			ps.setInt (1,  ConstantesBD.codigoTipoSolicitudCirugia);
			ps.setInt (2,  ConstantesBD.codigoEstadoFCargada);
			ps.setInt (3,  ConstantesBD.codigoEstadoFExento);
			ps.setDate(4,  new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(5,  new java.sql.Date(fechaFin.getTime()));
			ps.setDate(6,  new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(7,  new java.sql.Date(fechaFin.getTime()));
			ps.setInt (8,  ConstantesBD.codigoTipoSolicitudCirugia);
			ps.setInt (9,  ConstantesBD.codigoEstadoFCargada);
			ps.setInt (10, ConstantesBD.codigoEstadoFExento);
			ps.setDate(11, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(12, new java.sql.Date(fechaFin.getTime()));
			ps.setDate(13, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(14, new java.sql.Date(fechaFin.getTime()));
			ps.setInt (15, Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion())));

			Log4JManager.info(sql);
			
			rs = ps.executeQuery();	
			
			while(rs.next())
			{
				DtoConsultaProcesoCargosCuenta dtoTemp = new DtoConsultaProcesoCargosCuenta();
				dtoTemp.setCantidad(rs.getInt("cantidad"));
				dtoTemp.setConvenio(rs.getInt("convenio"));
				dtoTemp.setContrato(rs.getInt("contrato"));
				dtoTemp.setFecha(rs.getDate("fecha"));
				dtoTemp.setNumeroSolicitud(rs.getInt("numero_solicitud"));
				dtoTemp.setTarifa(rs.getBigDecimal("tarifa"));
				
				if(rs.getString("tipo").equals("articulo"))
				{
					dtoTemp.setCodigoArticulo(rs.getInt("cod_articulo_o_servicio"));
					dtoTemp.setSubgrupoInventario(rs.getInt("subgrupo"));
					dtoTemp.setNivelAtencionArticulo(rs.getLong("nivel_atencion"));
					dtoTemp.setNombreArticulo(rs.getString("descripcion"));
				}
				else if(rs.getString("tipo").equals("servicio"))
				{
					dtoTemp.setCodigoServicio(rs.getInt("cod_articulo_o_servicio"));
					dtoTemp.setCodigoGrupoServicio(rs.getInt("codigo_grupo_servicio"));
					dtoTemp.setDescripcionGrupoServicio(rs.getString("descripcion"));
					dtoTemp.setNivelAtencionServicio(rs.getLong("nivel_atencion"));
					dtoTemp.setNombreServicio(rs.getString("descripcion_servicio"));
				}
				lista.add(dtoTemp);
			}
		}
		catch (Exception e)
		{
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(con!=null)
				{
					con.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
				if(rs!=null)
				{
					rs.close();
				}
			}
			catch (Exception e2)
			{
				Log4JManager.error(e2);
				e2.printStackTrace();
			}
		}
		return lista;
	}
	
	
	
	
	
	
	
	
	/**
	 * Este método consulta las solicitudes para cargos a la cuenta 
	 * Proceso 1030
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro párametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas
	 * @author Camilo Gómez
	 */
	@Deprecated
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuentaNOUSAR(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(DetCargos.class,"detCargos")
		
		.createAlias("detCargos.convenios"							, "convenio"	)
		.createAlias("detCargos.contratos"							, "contrato"	)
		.createAlias("detCargos.estadosSolFact"						, "estadoCargo"	)
		.createAlias("detCargos.subCuentas"							, "subCuenta"	)		
		.createAlias("detCargos.solicitudes"						, "solicitud"	)		
		
		.createAlias("solicitud.solicitudesSubcuentas"				, "solicitudesSubcuentas"	)
		
		//SOLICITUDES DE ORDENES MEDICAS
		.createAlias("solicitudesSubcuentas.serviciosByServicio"	, "servicio"				, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"				, "referenciasServicios"	, Criteria.LEFT_JOIN)
		.createAlias("servicio.nivelAtencion"						, "nivelAtencionServicio"	, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"						, "grupoServicio"			, Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.solicitudesMedicamentos"			, "solicitudesMedicamentos"	, Criteria.LEFT_JOIN)
		.createAlias("solicitudesMedicamentos.detalleSolicitudeses"	, "detalleSolicitudeses"	, Criteria.LEFT_JOIN)
		.createAlias("detalleSolicitudeses.articuloByArticulo"		, "articulo"				, Criteria.LEFT_JOIN)
		.createAlias("articulo.nivelAtencion"						, "nivelAtencionArticulo"	, Criteria.LEFT_JOIN)
		;
		

		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion()));
		
		Log4JManager.info("#####################  Filtros Consulta Cargos Cuenta Solicitudes #####################" 
				+"\nEstado			: "+ConstantesBD.codigoEstadoFCargada+" - "+ConstantesBD.codigoEstadoFExento
				+"\nFecha Inicio		: "+dtoFiltro.getFechaInicio()
				+"\nFecha Fin		: "+dtoFiltro.getFechaFin()
				+"\nConvenio		: "+dtoFiltro.getConvenio()
				+"\nContrato		: "+dtoFiltro.getContrato()
				+"\nTipo tarifario		: "+tipoTarifario+" -> de acuerdo al parametro general ("+dtoFiltro.getInstitucion()+")"
		);
		
		//------------------------------------FILTROS--------------------------------------
		//Estados de cargos
		ArrayList<Integer>estadosCargos=new ArrayList<Integer>();
			estadosCargos.add(ConstantesBD.codigoEstadoFCargada);
			estadosCargos.add(ConstantesBD.codigoEstadoFExento);
		criteria.add(Restrictions.in("estadoCargo.codigo", estadosCargos));
		
		criteria.add(Restrictions.ne("detCargos.tipoSolicitud", ConstantesBD.codigoTipoSolicitudCirugia));

		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(tipoTarifario));
		criteria.add(disjunction);
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null)
		{
			Date fechaFin=new Date();
			try{					
				Calendar fechaCalendar=Calendar.getInstance();
				fechaCalendar.setTime(dtoFiltro.getFechaFin());
				fechaCalendar.add(Calendar.DAY_OF_MONTH, 1);
				fechaFin=fechaCalendar.getTime();
				
			} catch (Exception e) {
				Log4JManager.error("Error cambiando el formato de la fecha", e);
				e.printStackTrace();
			}
			criteria.add(Restrictions.between("solicitud.fechaSolicitud",dtoFiltro.getFechaInicio(), fechaFin));
		}
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		
		ProjectionList projection = Projections.projectionList();
		
		
		//CARGO - SOLICITUD
		projection.add(Projections.property("solicitud.fechaSolicitud")		,"fecha");				
		projection.add(Projections.property("convenio.codigo")				,"convenio");
		projection.add(Projections.property("contrato.codigo")				,"contrato");	
		projection.add(Projections.property("solicitud.numeroSolicitud")	,"numeroSolicitud");
		
		projection.add(Projections.property("detCargos.cantidadCargada")		,"cantidad");
		projection.add(Projections.property("detCargos.valorUnitarioTarifa")	,"tarifa");
		
		//SERVICIOS
		projection.add(Projections.property("servicio.codigo")						,"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo")	,"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion")		,"nombreServicio");
		projection.add(Projections.property("grupoServicio.codigo")					,"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion")			,"descripcionGrupoServicio");
		
		//ARTICULOS
		projection.add(Projections.property("articulo.codigo")						,"codigoArticulo");
		projection.add(Projections.property("nivelAtencionArticulo.consecutivo")	,"nivelAtencionArticulo");
		projection.add(Projections.property("articulo.descripcion")					,"nombreArticulo");
		projection.add(Projections.property("articulo.subgrupo")					,"subgrupoInventario");
		
		criteria.addOrder(Order.asc("solicitud.fechaSolicitud"));
		criteria.addOrder(Order.asc("solicitud.numeroSolicitud"));
		
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoConsultaProcesoCargosCuenta.class));
		ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas=
			(ArrayList<DtoConsultaProcesoCargosCuenta>)criteria.list();
		
		Log4JManager.info(HibernateUtil.getSql(criteria));
		
		return listadoOrdenesMedicas;
		
	}	
	
	
	
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro)
	{
		String sqlServicios =
			"SELECT DISTINCT "+
			  "det_cargos.valor_unitario_tarifa AS tarifa, "+ 
			  "servicios.nivel AS nivel_atencion, "+
			  "servicios.codigo as cod_servicio, "+ 
			  "grupos_servicios.descripcion AS descripcion, "+
			  "grupos_servicios.codigo AS cod_servicio, "+
			  "referencias_servicio.descripcion AS descripcion_servicio, "+
			  "det_cargos.cantidad_cargada AS cantidad, "+
			  "solicitudes.fecha_solicitud AS fecha, "+
			  "det_cargos.convenio AS convenio, "+
			  "det_cargos.contrato AS contrato, "+
			  "solicitudes.numero_solicitud AS numero_solicitud "+
			"FROM "+
			  "facturacion.det_cargos, "+ 
			  "facturacion.servicios, "+
			  "facturacion.grupos_servicios, "+ 
			  "ordenes.solicitudes, "+
			  "facturacion.referencias_servicio "+
			"WHERE "+
			  "det_cargos.servicio = servicios.codigo AND "+
			  "det_cargos.solicitud = solicitudes.numero_solicitud AND "+
			  "servicios.grupo_servicio = grupos_servicios.codigo AND "+
			  "servicios.codigo = referencias_servicio.servicio AND "+
			  "REEMPLAZO_CONVENIO "+
			  "REEMPLAZO_CONTRATO "+ 
			  "det_cargos.servicio_cx IS NULL AND "+
			  "det_cargos.tipo_solicitud = ? AND "+
			  "det_cargos.estado IN (?,?) AND "+
			  "(solicitudes.fecha_solicitud BETWEEN ? AND ? OR det_cargos.fecha_modifica BETWEEN ? AND ?) AND "+
			  "(referencias_servicio.tipo_tarifario IS NULL OR referencias_servicio.tipo_tarifario = ?) ";
		
		String sqlCirugias =
			"SELECT SUM( det_cargos.valor_unitario_tarifa * det_cargos.cantidad_cargada) AS tarifa, "+
			  "servicios.nivel                                                           AS nivel_atencion, "+
			  "servicios.codigo                                                          AS cod_servicio, "+
			  "grupos_servicios.descripcion                                              AS descripcion, "+
			  "grupos_servicios.codigo                                                   AS codigo_grupo_servicio, "+
			  "referencias_servicio.descripcion                                          AS descripcion_servicio, "+
			  "solicitudes.fecha_solicitud                                               AS fecha, "+
			  "det_cargos.convenio                                                       AS convenio, "+
			  "det_cargos.contrato                                                       AS contrato, "+
			  "solicitudes.numero_solicitud                                              AS numero_solicitud, "+
			  "det_cargos.servicio_cx                                                    AS cirugia "+
			"FROM facturacion.det_cargos "+
			"INNER JOIN facturacion.servicios ON det_cargos.servicio_cx = servicios.codigo "+
			"INNER JOIN facturacion.grupos_servicios ON servicios.grupo_servicio = grupos_servicios.codigo "+
			"INNER JOIN ordenes.solicitudes ON det_cargos.solicitud = solicitudes.numero_solicitud "+
			"INNER JOIN facturacion.referencias_servicio ON servicios.codigo = referencias_servicio.servicio "+
			"WHERE det_cargos.servicio_cx IS NOT NULL AND "+
			"REEMPLAZO_CONVENIO "+
			"REEMPLAZO_CONTRATO "+ 
			"det_cargos.tipo_solicitud = ? AND "+
			"det_cargos.estado IN (?,?) AND "+
			"(solicitudes.fecha_solicitud BETWEEN ? AND ? OR det_cargos.fecha_modifica BETWEEN ? AND ?) AND "+
			"(referencias_servicio.tipo_tarifario IS NULL OR referencias_servicio.tipo_tarifario = ?) "+
			"GROUP BY servicios.nivel, "+
			  "servicios.codigo, "+
			  "grupos_servicios.descripcion, "+
			  "grupos_servicios.codigo, "+
			  "referencias_servicio.descripcion, "+
			  "solicitudes.fecha_solicitud, "+
			  "det_cargos.convenio, "+
			  "det_cargos.contrato, "+
			  "solicitudes.numero_solicitud, "+
			  "det_cargos.servicio_cx "+
			"ORDER BY det_cargos.servicio_cx ";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		
		ArrayList<DtoConsultaProcesoCargosCuenta> lista = new ArrayList<DtoConsultaProcesoCargosCuenta>();
		try
		{
			Date fechaFin=new Date();
			Calendar fechaCalendar=Calendar.getInstance();
			fechaCalendar.setTime(dtoFiltro.getFechaFin());
			fechaCalendar.add(Calendar.DAY_OF_MONTH, 1);
			fechaFin=fechaCalendar.getTime();
			
			if(dtoFiltro.getConvenio() != null)
			{
				sqlServicios = sqlServicios.replaceAll("REEMPLAZO_CONVENIO", "det_cargos.convenio = " + dtoFiltro.getConvenio() + " AND ");
				sqlCirugias = sqlCirugias.replaceAll("REEMPLAZO_CONVENIO", "det_cargos.convenio = " + dtoFiltro.getConvenio() + " AND ");
			}
			else
			{
				sqlServicios = sqlServicios.replaceAll("REEMPLAZO_CONVENIO", " ");
				sqlCirugias = sqlCirugias.replaceAll("REEMPLAZO_CONVENIO", " ");
			}
			
			if(dtoFiltro.getContrato() != null)
			{
				sqlServicios = sqlServicios.replaceAll("REEMPLAZO_CONTRATO", "det_cargos.contrato = " + dtoFiltro.getContrato() + " AND " );
				sqlCirugias = sqlCirugias.replaceAll("REEMPLAZO_CONTRATO", "det_cargos.contrato = " + dtoFiltro.getContrato() + " AND " );
			}
			else
			{
				sqlServicios = sqlServicios.replaceAll("REEMPLAZO_CONTRATO", " ");
				sqlCirugias = sqlCirugias.replaceAll("REEMPLAZO_CONTRATO", " ");
			}
			
			con = UtilidadBD.abrirConexion();
			ps = con.prepareStatement(sqlServicios);

			ps.setInt (1, ConstantesBD.codigoTipoSolicitudCirugia);
			ps.setInt (2, ConstantesBD.codigoEstadoFCargada);
			ps.setInt (3, ConstantesBD.codigoEstadoFExento);
			ps.setDate(4, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(5, new java.sql.Date(fechaFin.getTime()));
			ps.setDate(6, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps.setDate(7, new java.sql.Date(fechaFin.getTime()));
			ps.setInt (8, Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion())));

			Log4JManager.info(sqlServicios);
			
			rs = ps.executeQuery();	
			while(rs.next())
			{
				DtoConsultaProcesoCargosCuenta dtoTemp = new DtoConsultaProcesoCargosCuenta();

				dtoTemp.setTarifa(rs.getBigDecimal("tarifa"));
				dtoTemp.setNivelAtencionServicio(rs.getLong("nivel_atencion"));
				dtoTemp.setCodigoServicio(rs.getInt("cod_servicio"));
				dtoTemp.setDescripcionGrupoServicio(rs.getString("descripcion"));
				dtoTemp.setCodigoGrupoServicio(rs.getInt("codigo_grupo_servicio"));
				dtoTemp.setNombreServicio(rs.getString("descripcion_servicio"));
				dtoTemp.setCantidad(rs.getInt("cantidad"));
				dtoTemp.setFecha(rs.getDate("fecha"));
				dtoTemp.setConvenio(rs.getInt("convenio"));
				dtoTemp.setContrato(rs.getInt("contrato"));
				dtoTemp.setNumeroSolicitud(rs.getInt("numero_solicitud"));
				lista.add(dtoTemp);
			}
			
			ps2 = con.prepareStatement(sqlCirugias);
			
			ps2.setInt (1, ConstantesBD.codigoTipoSolicitudCirugia);
			ps2.setInt (2, ConstantesBD.codigoEstadoFCargada);
			ps2.setInt (3, ConstantesBD.codigoEstadoFExento);
			ps2.setDate(4, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps2.setDate(5, new java.sql.Date(fechaFin.getTime()));
			ps2.setDate(6, new java.sql.Date(dtoFiltro.getFechaInicio().getTime()));
			ps2.setDate(7, new java.sql.Date(fechaFin.getTime()));
			ps2.setInt (8, Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion())));

			Log4JManager.info(sqlCirugias);
			
			rs2 = ps2.executeQuery();
			while(rs2.next())
			{
				DtoConsultaProcesoCargosCuenta dtoTemp = new DtoConsultaProcesoCargosCuenta();

				dtoTemp.setTarifa(rs2.getBigDecimal("tarifa"));
				dtoTemp.setNivelAtencionServicio(rs2.getLong("nivel_atencion"));
				dtoTemp.setCodigoServicio(rs2.getInt("cod_servicio"));
				dtoTemp.setDescripcionGrupoServicio(rs2.getString("descripcion"));
				dtoTemp.setCodigoGrupoServicio(rs2.getInt("codigo_grupo_servicio"));
				dtoTemp.setNombreServicio(rs2.getString("descripcion_servicio"));
				dtoTemp.setCantidad(1);
				dtoTemp.setFecha(rs2.getDate("fecha"));
				dtoTemp.setConvenio(rs2.getInt("convenio"));
				dtoTemp.setContrato(rs2.getInt("contrato"));
				dtoTemp.setNumeroSolicitud(rs2.getInt("numero_solicitud"));
				lista.add(dtoTemp);
			}
		}
		catch (Exception e)
		{
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(con!=null)
				{
					con.close();
				}
				if(ps!=null)
				{
					ps.close();
				}
				if(rs!=null)
				{
					rs.close();
				}
				if(ps2!=null)
				{
					ps2.close();
				}
				if(rs2!=null)
				{
					rs2.close();
				}
			}
			catch (Exception e2)
			{
				Log4JManager.error(e2);
				e2.printStackTrace();
			}
		}
		return lista;
	}

	
	/**
	 * Este método consulta las solicitudes de Cirugia para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro párametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia 
	 * @author Camilo Gómez
	 */
	@Deprecated
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuentaNOUSAR(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(DetCargos.class,"detCargos")
						
		.createAlias("detCargos.convenios"			, "convenio"	)
		.createAlias("detCargos.contratos"			, "contrato"	)
		.createAlias("detCargos.estadosSolFact"		, "estadoCargo"	)
		.createAlias("detCargos.solicitudes"		, "solicitud"	)
		
		.createAlias("solicitud.solicitudesCirugia"		, "cirugia"		)
		.createAlias("cirugia.solCirugiaPorServicios"	, "solCirugia"	)
		
		.createAlias("solCirugia.servicios"							, "servicio"				, Criteria.LEFT_JOIN)
		.createAlias("servicio.nivelAtencion"						, "nivelAtencionServicio"	, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"				, "referenciasServicios"	, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"						, "grupoServicio"			, Criteria.LEFT_JOIN)
		;
		
		//------------------------------------FILTROS--------------------------------------		
		ProjectionList projection = Projections.projectionList();
		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion()));
		
		Log4JManager.info("#####################  Filtros Consulta Cargos Cuenta Solicitudes -CIRUGIAS  #####################" 
				+"\nEstado			: "+ConstantesBD.codigoEstadoFCargada+" - "+ConstantesBD.codigoEstadoFExento
				+"\nFecha Inicio		: "+dtoFiltro.getFechaInicio()
				+"\nFecha Fin		: "+dtoFiltro.getFechaFin()
				+"\nConvenio		: "+dtoFiltro.getConvenio()
				+"\nContrato		: "+dtoFiltro.getContrato()
				+"\nTipo tarifario		: "+tipoTarifario+" -> de acuerdo al parametro general ("+dtoFiltro.getInstitucion()+")"
		);
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(tipoTarifario));
		criteria.add(disjunction);
				

		//Estados de cargos
		ArrayList<Integer>estadosCargos=new ArrayList<Integer>();
			estadosCargos.add(ConstantesBD.codigoEstadoFCargada);
			estadosCargos.add(ConstantesBD.codigoEstadoFExento);
		criteria.add(Restrictions.in("estadoCargo.codigo", estadosCargos));
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			criteria.add(Restrictions.between("solicitud.fechaSolicitud",dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));
		}
		
		
		projection.add(Projections.property("solicitud.fechaSolicitud")		,"fecha");				
		projection.add(Projections.property("convenio.codigo")				,"convenio");
		projection.add(Projections.property("contrato.codigo")				,"contrato");	
		projection.add(Projections.property("solicitud.numeroSolicitud")	,"numeroSolicitud");
	
		
		projection.add(Projections.property("detCargos.cantidadCargada")		,"cantidad");
		projection.add(Projections.property("detCargos.valorUnitarioTarifa")	,"tarifa");
		
		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo")						,"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo")	,"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion")		,"nombreServicio");
		projection.add(Projections.property("grupoServicio.codigo")					,"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion")			,"descripcionGrupoServicio");
		
		criteria.addOrder(Order.asc("solicitud.fechaSolicitud"));
		criteria.addOrder(Order.asc("solicitud.numeroSolicitud"));
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoConsultaProcesoCargosCuenta.class));
		ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia=
			(ArrayList<DtoConsultaProcesoCargosCuenta>)criteria.list();
		
		return listadoOrdenesCirugia;
	}	
	

	/**
	 * Prueba consulta
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DtoProcesoPresupuestoCapitado dto=new DtoProcesoPresupuestoCapitado();
		DetCargosDelegate delegate=new DetCargosDelegate();
		dto.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate("20/03/2011"));
		dto.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate("06/04/2011"));
		dto.setInstitucion(2);
		
		
		delegate.consultarSolicitudesCargosCuenta(dto);
		delegate.consultarSolicitudesCirugiaCargosCuenta(dto);
	}

	
}
