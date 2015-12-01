/**
 * 
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.delegate.facturacion.EntidadesSubcontratadasDelegate;

/**
 * @author armando
 *
 */
public class SqlBaseProcesoNivelAutorizacionDao 
{
	private static Logger logger = Logger.getLogger(SqlBaseProcesoNivelAutorizacionDao.class);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static DTOValidacionNivelAutoAutomatica validarNivelAutorizacion(DtoParametrosValidacionNivelAutorizacion dto) 
	{
		DTOValidacionNivelAutoAutomatica validacion=new DTOValidacionNivelAutoAutomatica();
		Connection con=UtilidadBD.abrirConexion();
		
		PreparedStatementDecorator ps=null;
		PreparedStatementDecorator psInterno= null;
		PreparedStatementDecorator psU= null;
		PreparedStatementDecorator psInterno1=  null;
		PreparedStatementDecorator psCC = null;
		PreparedStatementDecorator psES= null;
		ResultSetDecorator rsInterno= null;
		ResultSetDecorator rs=null;
		ResultSetDecorator rsU= null;
		ResultSetDecorator rsInterno1= null;
		ResultSetDecorator rsCC=null;
		ResultSetDecorator rsES=null;
		
		try
		{
			ArrayList<DtoValidacionNivelesAutorizacion> nivelAutorizacion=new ArrayList<DtoValidacionNivelesAutorizacion>();
			
			//1. mirar el nivel de autorizacion del usuario.
			//////usuario especifico
			String consulta1="select " +
									" na.codigo_pk as codigopk, " +
									" na.descripcion as descripcion, " +
									" na.via_ingreso as viaingreso, " +
									" na.tipo_autorizacion as tipoautorizacion, " +
									" naue.usuario as usuario, " +
									" naue.codigo_pk as codigonivelusuario," +
									" na.activo as activo " +
							" from nivel_autorizacion na " +
							" inner join nivel_autor_usuario nuu on (na.codigo_pk=nuu.nivel_autorizacion) " +
							" inner join nivel_autor_usu_espec naue on (naue.nivel_autor_usuario=nuu.codigo_pk) " +
							" where naue.usuario='"+dto.getLoginUsuario()+"'";
			ps=new PreparedStatementDecorator(con,consulta1);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoValidacionNivelesAutorizacion dtoTempo=new DtoValidacionNivelesAutorizacion();
				dtoTempo.setCodigoNivelAutorizacion(rs.getInt("codigopk"));
				dtoTempo.setDescripcionNivelAutorizacion(rs.getString("descripcion"));
				dtoTempo.setTipoAutorizacion(rs.getString("tipoautorizacion"));
				dtoTempo.setUsuarioAutorizacion(rs.getString("usuario"));
				dtoTempo.setViaIngreso(rs.getInt("viaingreso"));
				dtoTempo.setCodigoNivelAutorizacionUsuario(rs.getInt("codigonivelusuario"));
				dtoTempo.setActivo(rs.getBoolean("activo"));
				String consultaInterna="SELECT DISTINCT " +
												" nro_prioridad as prioridad " +
										" from prioridad_usu_esp pue " +
										" inner join centros_costo_entidades_sub cces on(pue.prioridad_entidad_sub=cces.consecutivo) " +
										" where pue.nivel_autor_usu_espec="+rs.getInt("codigonivelusuario")+" order by nro_prioridad";
				
				 psInterno=new PreparedStatementDecorator(con,consultaInterna);
				 rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				while(rsInterno.next())
				{
					dtoTempo.getPrioridades().add(rsInterno.getInt("prioridad"));
				}
				rsInterno.close();
				psInterno.close();
				nivelAutorizacion.add(dtoTempo);
			}
			//si no se carga por usuario, entonces se busca por ocupacion.
			if(nivelAutorizacion.size()<=0)
			{
				String consultaUsuario="SELECT ocupacion_medica from medicos med inner join usuarios u on (u.codigo_persona=med.codigo_medico) where u.login='"+dto.getLoginUsuario()+"'";
				 psU=new PreparedStatementDecorator(con,consultaUsuario);
				 rsU=new ResultSetDecorator(psU.executeQuery());
				if(rsU.next())
				{
					int ocupacion=rs.getInt(1);
					consulta1="select " +
											" na.codigo_pk as codigopk, " +
											" na.descripcion as descripcion, " +
											" na.via_ingreso as viaingreso, " +
											" na.tipo_autorizacion as tipoautorizacion, " +
											" naom.ocupacion_medica as ocupacion, " +
											" naom.codigo_pk as codigonivelocupacion," +
											" na.activo as activo " +
									" from nivel_autorizacion na " +
									" inner join nivel_autor_usuario nuu on (na.codigo_pk=nuu.nivel_autorizacion) " +
									" inner join nivel_autor_ocup_medica naom on (naom.nivel_autor_usuario=nuu.codigo_pk) " +
									" where naom.ocupacion_medica="+ocupacion;
						ps=new PreparedStatementDecorator(con,consulta1);
						rs=new ResultSetDecorator(ps.executeQuery());
						while(rs.next())
						{
							DtoValidacionNivelesAutorizacion dtoTempo=new DtoValidacionNivelesAutorizacion();
							dtoTempo.setCodigoNivelAutorizacion(rs.getInt("codigopk"));
							dtoTempo.setDescripcionNivelAutorizacion(rs.getString("descripcion"));
							dtoTempo.setTipoAutorizacion(rs.getString("tipoautorizacion"));
							dtoTempo.setOcupacionMedicaAutorizacion(rs.getInt("ocupacion"));
							dtoTempo.setViaIngreso(rs.getInt("viaingreso"));
							dtoTempo.setCodigoNivelAutorizacionOcupacionMedica(rs.getInt("codigonivelocupacion"));
							dtoTempo.setActivo(rs.getBoolean("activo"));
							String consultaInterna="SELECT DISTINCT " +
															" nro_prioridad as prioridad" +
													" from prioridad_ocup_medica pom " +
													" inner join centros_costo_entidades_sub cces on(pue.prioridad_entidad_sub=cces.consecutivo) " +
													" where pom.nivel_autor_ocup_medica="+rs.getInt("codigonivelocupacion")+" order by nro_prioridad";
							
							 psInterno=new PreparedStatementDecorator(con,consultaInterna);
							 rsInterno=new ResultSetDecorator(psInterno.executeQuery());
							while(rsInterno.next())
							{
								dtoTempo.getPrioridades().add(rsInterno.getInt("prioridad"));
							}
							rsInterno.close();
							psInterno.close();
							nivelAutorizacion.add(dtoTempo);
					}
				}
			}
			
			//2. dejar solo los niveles validos.
			ArrayList<Integer> eliminar=new ArrayList<Integer>();
			for(int i=0;i<nivelAutorizacion.size();i++)
			{
				DtoValidacionNivelesAutorizacion dtoTempo=(DtoValidacionNivelesAutorizacion)nivelAutorizacion.get(i);
				if(!dtoTempo.isActivo()||dto.getViaIngreso()!=dtoTempo.getViaIngreso()||!dtoTempo.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoTipoAutorizacionAuto))
				{
					eliminar.add(i);
				}
			}
			//eliminar los niveles de autorizacion que quedan.
			for(Integer i:eliminar)
			{
				nivelAutorizacion.remove(i.intValue());
			}
			
			
			//punto 3.
			//hacer la validacion en los que quedar por servicios y articulos
			//servicios
			eliminar=new ArrayList<Integer>();
			if(dto.getCodigoServicio()>0)
			{
				//se busca primero en todos los niveles de autorizacion si se encuentra el servicio especifico.
				for(int i=0;i<nivelAutorizacion.size();i++)
				{
					String consultaServicio="SELECT " +
												" nasm.codigo_pk as codigonivelservmed," +
												" nasm.nivel_autorizacion as nivelautorizacion " +
										" from nivel_autor_serv_medic nasm " +
										" inner join nivel_autor_servicio nas on (nas.nivel_autor_serv_medic=nasm.codigo_pk) " +
										" where nasm.nivel_autorizacion="+nivelAutorizacion.get(i).getCodigoNivelAutorizacion()+" " +
												"and nas.servicio="+dto.getCodigoServicio();
					 psInterno=new PreparedStatementDecorator(con,consultaServicio);
					 rsInterno=new ResultSetDecorator(psInterno.executeQuery());
					if(!rsInterno.next())
					{
						String consultaServicio1="SELECT " +
												" nasm.codigo_pk as codigonivelservmed," +
												" nasm.nivel_autorizacion as nivelautorizacion " +
										" from nivel_autor_serv_medic nasm " +
										" inner join nivel_autor_agr_serv naas on (naas.nivel_autor_serv_medic=nasm.codigo_pk) " +
										" where nasm.nivel_autorizacion="+nivelAutorizacion.get(i).getCodigoNivelAutorizacion() +
										" and ( (especialidad=(select s.especialidad from servicios s where s.codigo="+dto.getCodigoServicio()+")) or (especialidad_codigo is null)) " +
										" and ( (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo="+dto.getCodigoServicio()+")) or (acronimo_tipo_servicio is null)) " +
										" and ( (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo="+dto.getCodigoServicio()+")) or (grupo_servicio_codigo is null)) ";
						consultaServicio1+=" ORDER BY especialidad, tipo_servicio, grupo_servicio ";
						 psInterno1=new PreparedStatementDecorator(con,consultaServicio1);
						 rsInterno1=new ResultSetDecorator(psInterno1.executeQuery());
						if(!rsInterno1.next())
						{
							eliminar.add(i);
						}
						rsInterno1.close();
						psInterno1.close();
					}
					rsInterno.close();
					psInterno.close();
				}
			}
			else if(dto.getCodigoArticulo()>0)
			{
				
				//se busca primero en todos los niveles de autorizacion si se encuentra el articulo especifico.
				for(int i=0;i<nivelAutorizacion.size();i++)
				{
					String consultaArticulo="SELECT " +
												" nasm.codigo_pk as codigonivelservmed," +
												" nasm.nivel_autorizacion as nivelautorizacion " +
										" from nivel_autor_serv_medic nasm " +
										" inner join nivel_autor_articulo naa on (na.nivel_autor_serv_medic=nasm.codigo_pk) " +
										" where nasm.nivel_autorizacion="+nivelAutorizacion.get(i).getCodigoNivelAutorizacion()+" " +
												"and naa.articulo="+dto.getCodigoArticulo();
					 psInterno=new PreparedStatementDecorator(con,consultaArticulo);
					 rsInterno=new ResultSetDecorator(psInterno.executeQuery());
					if(!rsInterno.next())
					{
						String consultaArticulo1="SELECT " +
												" nasm.codigo_pk as codigonivelservmed," +
												" nasm.nivel_autorizacion as nivelautorizacion " +
										" from nivel_autor_serv_medic nasm " +
										" inner join nivel_autor_agr_art naaa on (naaa.nivel_autor_serv_medic=nasm.codigo_pk) " +
										" where nasm.nivel_autorizacion="+nivelAutorizacion.get(i).getCodigoNivelAutorizacion() +
												" and (naaa.subgrupo_inventario=(select a.subgrupo from articulo a where a.codigo="+dto.getCodigoArticulo()+") or naaa.subgrupo_inventario is null) " +
												" and (naaa.grupo_inventario=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo="+dto.getCodigoArticulo()+")) or naaa.grupo_inventario is null) " +
												" and (naaa.clase_inventario=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo="+dto.getCodigoArticulo()+")) or naaa.clase_inventario is null) " +
												" and (naaa.naturaleza_articulo=(select a.naturaleza from articulo a where a.codigo="+dto.getCodigoArticulo()+") or naaa.naturaleza_articulo is null) ";
						consultaArticulo1+=" ORDER BY naaa.subgrupo_inventario, naaa.grupo_inventario, naaa.clase_inventario, naaa.naturaleza_articulo ";
						
						 psInterno1=new PreparedStatementDecorator(con,consultaArticulo1);
						 rsInterno1=new ResultSetDecorator(psInterno1.executeQuery());
						if(!rsInterno1.next())
						{
						eliminar.add(i);
						}
						rsInterno1.close();
						psInterno1.close();
					}
					rsInterno.close();
					psInterno.close();
				}
			}
			//eliminar los niveles de autorizacion que quedan.
			int posicionesEliminadas =0;			
			for(Integer i:eliminar){
				nivelAutorizacion.remove((i.intValue())-posicionesEliminadas);
				posicionesEliminadas++;
			}
			
			//paso 4
			if(nivelAutorizacion!=null && nivelAutorizacion.size()>0)
			{
				String consultaCC="SELECT tipo_entidad_ejecuta from centros_costo where codigo="+dto.getCentroCostoEjecuta();
				String tipoEntidadEjecuta="";
				psCC=new PreparedStatementDecorator(con,consultaCC);
				rsCC=new ResultSetDecorator(psCC.executeQuery());
				DTONivelAutorizacion dtoNivelAutorizacion =null;
				
				if(rsCC.next())
				{
					tipoEntidadEjecuta=rsCC.getString(1);
				}
				psCC.close();
				rsCC.close();
				//si el centro de costo tiene tipo entidad ejecuta definida.
				if(!UtilidadTexto.isEmpty(tipoEntidadEjecuta))
				{
					if(tipoEntidadEjecuta.equals(ConstantesIntegridadDominio.acronimoInterna))
					{
						String entidad=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
						if(UtilidadTexto.getBoolean(entidad))
						{
							EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
							EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
							entidadSub=entiDao.findById(Long.parseLong(entidad));
							if(UtilidadTexto.getBoolean(entidadSub.getActivo()))
							{
								DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, entidadSub.getCodigoPk()+"", UtilidadFecha.getFechaActual());
								int codigoContrato=Utilidades.convertirAEntero(contrato.getConsecutivo());
								if(codigoContrato>0)
								{									
									int prioridad=Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));
									for(int i=0;i<nivelAutorizacion.size();i++)
									{
										for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
										{
											if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==prioridad)
											{
												Cobertura cobertura=new Cobertura();
												if(dto.getCodigoServicio()>0)
												{
													InfoCobertura info=cobertura.validacionCoberturaServicioEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoServicio(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
													{
														//cargar la autorizacion y la entidad. y salir
														validacion.setGenerarAutorizacion(true);														
														dtoNivelAutorizacion = new DTONivelAutorizacion();
														dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
														dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
														
														validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
														DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
														dtoEntidad.setCodigo(entidadSub.getCodigo());
														dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
														dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
														dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
														validacion.setDtoEntidadSubcontratada(dtoEntidad);
														j=nivelAutorizacion.get(i).getPrioridades().size();
														i=nivelAutorizacion.size();
													}
												}
												else if(dto.getCodigoArticulo()>0)
												{
													InfoCobertura info=cobertura.validacionCoberturaArticuloEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoArticulo(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
													{
														//cargar la autorizacion y la entidad. y salir
														validacion.setGenerarAutorizacion(true);
														dtoNivelAutorizacion = new DTONivelAutorizacion();
														dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
														dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
														
														validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
														DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
														dtoEntidad.setCodigo(entidadSub.getCodigo());
														dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
														dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
														dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
														validacion.setDtoEntidadSubcontratada(dtoEntidad);
														j=nivelAutorizacion.get(i).getPrioridades().size();
														i=nivelAutorizacion.size();
													}
												}
											}
										}
									}
								}
							}
						}
					}
					else
					{
						String consultaES="select distinct es.codigo_pk from centros_costo_entidades_sub cces inner join entidades_subcontratadas es on (es.codigo_pk=cces.entidad_subcontratada) inner join facturacion.contratos_entidades_sub ces on (ces.entidad_subcontratada=es.codigo_pk) where es.activo='"+ConstantesBD.acronimoSi+"' and cces.centro_costo=1 and current_date between fecha_inicial and fecha_final ";
						psES=new PreparedStatementDecorator(con,consultaES);
						rsES=new ResultSetDecorator(psES.executeQuery());
						ArrayList<Integer> entidades=new ArrayList<Integer>();
						while (rs.next())
						{
							entidades.add(rs.getInt(1));
						}
						for(int ent=0;ent<entidades.size();ent++)
						{
							int entidad=entidades.get(ent);
							EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
							EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
							entidadSub=entiDao.findById(entidad);
							if(UtilidadTexto.getBoolean(entidadSub.getActivo()))
							{
								DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, entidadSub.getCodigoPk()+"", UtilidadFecha.getFechaActual());
								int codigoContrato=Utilidades.convertirAEntero(contrato.getConsecutivo());
								if(codigoContrato>0)
								{
									int prioridad=Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));
									for(int i=0;i<nivelAutorizacion.size();i++)
									{
										for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
										{
											if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==prioridad)
											{
												Cobertura cobertura=new Cobertura();
												if(dto.getCodigoServicio()>0)
												{
													InfoCobertura info=cobertura.validacionCoberturaServicioEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoServicio(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
													{
														//cargar la autorizacion y la entidad. y salir
														validacion.setGenerarAutorizacion(true);
														dtoNivelAutorizacion = new DTONivelAutorizacion();
														dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
														dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
														
														validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
														DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
														dtoEntidad.setCodigo(entidadSub.getCodigo());
														dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
														dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
														dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
														validacion.setDtoEntidadSubcontratada(dtoEntidad);
														j=nivelAutorizacion.get(i).getPrioridades().size();
														i=nivelAutorizacion.size();
														ent=entidades.size();
													}
												}
												else if(dto.getCodigoArticulo()>0)
												{
													InfoCobertura info=cobertura.validacionCoberturaArticuloEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoArticulo(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
													{
														//cargar la autorizacion y la entidad. y salir
														validacion.setGenerarAutorizacion(true);
														dtoNivelAutorizacion = new DTONivelAutorizacion();
														dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
														dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
														
														validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);														
														DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
														dtoEntidad.setCodigo(entidadSub.getCodigo());
														dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
														dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
														dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
														validacion.setDtoEntidadSubcontratada(dtoEntidad);
														j=nivelAutorizacion.get(i).getPrioridades().size();
														i=nivelAutorizacion.size();
														ent=entidades.size();
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("Error",e);
		}
		finally
		{
			
			
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(psInterno!=null){
						psInterno.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(psU!=null){
						psU.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(psInterno1!=null){
						psInterno1.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(psCC!=null){
						psCC.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(psES!=null){
						psES.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(rsInterno!=null){
						rsInterno.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				
				try{
					if(rsU!=null){
						rsU.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(rsInterno1!=null){
						rsInterno1.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(rsCC!=null){
						rsCC.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
				try{
					if(rsES!=null){
						rsES.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseSubirPacienteDao "+sqlException.toString() );
				}
						
			
			UtilidadBD.closeConnection(con);
		}
		
		return validacion;
	}

}
