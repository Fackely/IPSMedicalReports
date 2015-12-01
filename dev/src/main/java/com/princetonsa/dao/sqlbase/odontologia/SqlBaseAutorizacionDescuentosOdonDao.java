package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
/**
 *
 * @author Julio
 * Modificado Carvajal
 *
 */
public class SqlBaseAutorizacionDescuentosOdonDao
{
	private static Logger logger = Logger.getLogger(SqlBaseAutorizacionDescuentosOdonDao.class);
	
	private static String consultarHistoricoSolAutorizacionDcto		=	"SELECT " +
																			"codigo_pk," +
																			"to_char(fecha_modifica,'dd/mm/yyyy') AS fecha_modifica," +
																			"usuario_modifica," +
																			"hora_modifica," +
																			"observaciones," +
																			"getintegridaddominio(estado) AS estado " +
																		"FROM " +
																			"odontologia.log_presupuesto_dcto_odon " +
																		"WHERE " +
																			"presupuesto_dcto_odon=? " +
																		"ORDER BY " +
																			"fecha_modifica,hora_solicita ";
	
	
	
	/**
	 * SQL VALIDACION DE AUTORIZACION POR ATENCION
	 */
	
	private static String tieneNivelAutorizacionAtencion="" +
			"" +
			" SELECT DISTINCT ru.login  from odontologia.descuentos_odon_aten doa  INNER JOIN odontologia.tipos_usuarios tu on(doa.nivel_autorizacion=tu.codigo) "+ 
			"" +
			" INNER JOIN" +
			" " +
			" administracion.roles r ON (r.nombre_rol=tu.rol) " +
			" " +
			" INNER JOIN" +
			" " +
			" administracion.roles_usuarios ru ON (ru.nombre_rol=r.nombre_rol) " +
			"" +
			" WHERE " +
			"" +
			" ru.login=? AND ru.nombre_rol=tu.rol and doa.institucion=?"+
			""+
			" AND tu.actividad = ?";
	
		
	
	
	/**
	 * SQL PARA VALIDACION DE AUTORIZACION POR PRESUPUESTO
	 * TODO FALTA INSTITUCION
	 */
	private static String tieneNivelAutorizacion	=	"SELECT DISTINCT " +
															"ru.login," +
															"ru.nombre_rol " +
														"FROM " +
														"" +
														"odontologia.descuentos_odon d " +
														"" +
														" INNER JOIN " +
														" " +
															"odontologia.det_descuentos_odon ddo on(d.consecutivo=ddo.consecutivo_descuento) " +
															"" +
														"INNER JOIN " +
														"" +
															"odontologia.tipos_usuarios tu ON (tu.codigo=ddo.tipo_usuario_autoriza) " +
															"" +
														"INNER JOIN" +
														" " +
															"administracion.roles r ON (r.nombre_rol=tu.rol) " +
															"" +
														"INNER JOIN" +
														" " +
															"administracion.roles_usuarios ru ON (ru.nombre_rol=r.nombre_rol) " +
															"" +
														"WHERE " +
															" ru.login=? AND ru.nombre_rol=tu.rol"+
															" AND tu.actividad = ?";
															
	
	
	
	
	
	/**
	 * VALIDACION DE ROL PARA LOS CONSECUTIVOS DESCUENTO POR PRESUPUESTO
	 * 
	 * ESTE METODO VALIDA SI UN USUARIO TIENE PERMISOS PARA UN DESCUENTO ODONTOLOGICO.
	 * PARA REALIZAR EL PROCESO REALIZA LOS SIGUIENTE PASOS:  
	 * 
	 * 1. BUSCA LA AUTORIZACION EN DESCUENTOS. (odontologia.descuentos_odon)
	 * 2. SE REALIZA UN ENLACE DETALLE DE AUTORIZACION. (odontologia.det_descuentos_odon)
	 * EN LA TABLA TIPO DE USUARIO SE GUARDAR LAS AUTORIZACIONES PARA LOS RESPECTIVAS AUTORIZACIONES. (odontologia.tipos_usuarios)
	 * 
	 * 3. EN LA TABLA ROLES SE COMPARA CON LA AUTORIZACION PARAMETRIZADA EN EL DETALLE AUTORIZACION. (administracion.roles)
	 * 4. SE COMPARA SI EL ROL LO TIENE EL USUARIO LOGIADO EN SESSION, ESTA INFOMACION LO TIENE LAS TABLAS ROLES USUARIOS (administracion.roles_usuarios)    
	 * 
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @author Julio-> modificado Edgar Carvajal
	 * @return
	 */
	public static boolean tieneNivelAutorizacion(String loginUsuario, BigDecimal codigoDetDescuento, String actividadTipoUsuario)
	{
		boolean tieneNivel=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
		
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, tieneNivelAutorizacion);
			psd.setString(1,loginUsuario);
			psd.setString(2,actividadTipoUsuario);
			//psd.setBigDecimal(2,codigoDetDescuento);
			logger.info("\n\nCONSULTO EL NIVEL DE AUTORIZACION PARA EL USUARIO------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
				tieneNivel=true;
			else
				tieneNivel=false;
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN tieneNivelAutorizacion==> "+e);
		}
		
		return tieneNivel;
	}
	
	/**
	 *     
	 * Retorna los dias de Vigencia para presupuesto
	 * @param consecutivo
	 * @return los dias de vigencia para presupuesto
	 */
	
	public static int vigenciaDiasPresupuesto(BigDecimal codigoPkPresupuestoDctoOdon)
	{
		boolean tieneNivel=false;
        int diasVigenciaPresupuesto = Integer.parseInt(ConstantesBD.codigoNuncaValidoLong+"");
		
		try 
		{

			Connection con = UtilidadBD.abrirConexion();
			
		    String queryVigenciaDiasPresupuesto = " SELECT dias_vigencia_descuento FROM presupuesto_dcto_odon pdo"+
		    									  " INNER JOIN det_descuentos_odon ddo ON(pdo.det_dcto_odo=ddo.codigo)"+
		    									  "	WHERE pdo.codigo_pk = ?";
		    
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, queryVigenciaDiasPresupuesto);
			psd.setBigDecimal(1,codigoPkPresupuestoDctoOdon);

			logger.info("\n\nCONSULTO LOS DIAS VIGENTES EN PRESUPUESTO------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
			{
				diasVigenciaPresupuesto = rs.getInt(1);
			}
				
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN vigenciaDiasPresupuesto==> "+e);
		}
		
		return diasVigenciaPresupuesto;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * VALIDACION DE ROL PARA LOS CONSECUTIVOS DESCUENTO POR PRESUPUESTO
	 * TODO FALTA LA INSTITUCION
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @return
	 */
	public static boolean tieneNivelAutorizacionCentroAtencion(String loginUsuario, BigDecimal codigoDetDescuento, int codigoInstitucion, String actividadTipoUsuario)
	{
		boolean tieneNivel=false;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
		
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, tieneNivelAutorizacionAtencion);
			psd.setString(1,loginUsuario);
			psd.setInt(2, codigoInstitucion);
			psd.setString(3, actividadTipoUsuario);
			
			logger.info("\n\nCONSULTO EL NIVEL DE AUTORIZACION PARA EL USUARIO------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			if (rs.next())
				tieneNivel=true;
			else
				tieneNivel=false;
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN tieneNivelAutorizacion==> "+e);
		}
		
		return tieneNivel;
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param presupuestoDctoOdon
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologicoDescuento> consultarHistoricoSolAutorizacionDcto(BigDecimal presupuestoDctoOdon)
	{
		ArrayList<DtoPresupuestoOdontologicoDescuento> listadoPresupuesto=new ArrayList<DtoPresupuestoOdontologicoDescuento>();
		
		
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultarHistoricoSolAutorizacionDcto);
			psd.setBigDecimal(1,presupuestoDctoOdon);
			logger.info("LA CONSULTA--------->"+psd);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			while(rs.next())
			{
				DtoPresupuestoOdontologicoDescuento dto=new DtoPresupuestoOdontologicoDescuento();
				dto.setCodigoPkLog(rs.getBigDecimal("codigo_pk"));
				dto.getUsuarioFechaSolicitud().setFechaModifica(rs.getString("fecha_modifica"));
				dto.getUsuarioFechaSolicitud().setHoraModifica(rs.getString("hora_modifica"));
				dto.getUsuarioFechaSolicitud().setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setEstado(rs.getString("estado"));
				listadoPresupuesto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rs, con);
		 }
		 catch (SQLException e) 
		{
				logger.error("ERROR EN consultarHistoricoSolAutorizacionDcto==> "+e);
		}
		 return listadoPresupuesto;
	}
	
	
	 /**
	  * 
	  * @param presupuesto
	  * @param institucion
	  * @return
	  */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleProgramasPresupuesto(double presupuesto, int institucion)
	{
		ArrayList<DtoServicioOdontologico> listadoServicios= new ArrayList<DtoServicioOdontologico>();
		
		String detalleProgramasPresupuestoStr	=	"SELECT DISTINCT " +
														"p.codigo AS codigoprograma, " +
														"p.nombre AS nomprograma, " +
														"getcodigopropservicio(dp.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") ||' - '||  getnombreservicio(dp.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS nomservicio "+
													"FROM " +
														"odontologia.detalle_programas dp " +
													"INNER JOIN " +
														"odontologia.programas p ON (p.codigo=dp.programas) " +
													"INNER JOIN " +
														"odontologia.presupuesto_odo_prog_serv po ON (po.programa=p.codigo) " +
													"WHERE po.presupuesto=? ORDER BY p.codigo";
		
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, detalleProgramasPresupuestoStr);
			psd.setDouble(1,presupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			
			while(rs.next())
			{
				DtoServicioOdontologico dtoServicioOdontologico= new DtoServicioOdontologico();
				dtoServicioOdontologico.setCodigoPrograma(rs.getInt("codigoprograma"));
				dtoServicioOdontologico.setNombrePrograma(rs.getString("nomprograma"));
				dtoServicioOdontologico.setDescripcionServicio(rs.getString("nomservicio"));
				listadoServicios.add(dtoServicioOdontologico);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rs, con);
		 }
		 catch (SQLException e) 
			{
				logger.error("ERROR EN consultarDetalleProgramasPresupuesto==> "+e);
			}
		{
			
		}
		return listadoServicios;
	}
	
	/**
	 * 
	 * @param presupuesto
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleServiciosPresupuesto(double presupuesto, int institucion)
	{
		ArrayList<DtoServicioOdontologico> listadoServicios= new ArrayList<DtoServicioOdontologico>();

		String detalleServiciosPresupuestoStr	=	"SELECT DISTINCT " +
													"s.codigo AS codigoservicio," +
													"getcodigopropservicio(s.codigo,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") ||' - '||  getnombreservicio(s.codigo,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS nombreservicio "+
													"FROM " +
													"odontologia.presupuesto_odo_prog_serv po " +
													"INNER JOIN " +
													"facturacion.servicios s ON (s.codigo=po.servicio) "+
													"WHERE " +
													"po.presupuesto=?";

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, detalleServiciosPresupuestoStr);
			psd.setDouble(1,presupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoServicioOdontologico dtoServicio= new DtoServicioOdontologico();
				dtoServicio.setCodigoServicio(rs.getInt("codigoservicio"));
				dtoServicio.setDescripcionServicio(rs.getString("nombreservicio"));
				listadoServicios.add(dtoServicio);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rs, con);

		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarDetalleServiciosPresupuesto==> "+e);
		}
		return listadoServicios;
	}
	
	 /**
	  * 
	  * @param inclusion
	  * @param institucion
	  * @return
	  */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleProgramasInclusion(double presupuesto, int institucion)
	{
		ArrayList<DtoServicioOdontologico> listadoServicios= new ArrayList<DtoServicioOdontologico>();
		
	
		String detalleProgramasPresupuestoStrInclusion	= 	" SELECT DISTINCT p.codigo AS codigoPrograma,"+
	       										  	" p.nombre AS nomprograma,"+
	       										  	" getcodigopropservicio(isc.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") ||' - '||  getnombreservicio(isc.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS nomservicio"+ 
	       										  	" FROM odontologia.inclu_presu_convenio ipc "+
	       										  	" INNER JOIN odontologia.inclu_programa_convenio iprog ON (iprog.inclu_presu_convenio=ipc.codigo_pk)"+ 
													" INNER JOIN odontologia.programas p ON (iprog.programa=p.codigo) "+
													" INNER JOIN odontologia.inclu_programa_servicio ips ON (ips.inclu_programa_convenio=iprog.codigo_pk)"+ 
													" INNER JOIN odontologia.inclu_servicio_convenio isc ON (ips.inclu_servicio_convenio=isc.codigo_pk)" +
													" WHERE ipc.inclu_presu_encabezado=?"+
													" ORDER BY p.codigo ";

		
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, detalleProgramasPresupuestoStrInclusion);
			psd.setDouble(1,presupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			
			while(rs.next())
			{
				DtoServicioOdontologico dtoServicioOdontologico= new DtoServicioOdontologico();
				dtoServicioOdontologico.setCodigoPrograma(rs.getInt("codigoprograma"));
				dtoServicioOdontologico.setNombrePrograma(rs.getString("nomprograma"));
				dtoServicioOdontologico.setDescripcionServicio(rs.getString("nomservicio"));
				listadoServicios.add(dtoServicioOdontologico);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rs, con);
		 }
		 catch (SQLException e) 
			{
				logger.error("ERROR EN consultarDetalleProgramasPresupuesto==> "+e);
			}
		{
			
		}
		return listadoServicios;
	}
	
	/**
	 * 
	 * @param inclusion
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleServiciosInclusion(double presupuesto, int institucion)
	{
		ArrayList<DtoServicioOdontologico> listadoServicios= new ArrayList<DtoServicioOdontologico>();

		String detalleServiciosPresupuestoStrInclusion	=	" SELECT DISTINCT isc.servicio AS codigoservicio, "+
													" getcodigopropservicio(isc.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") ||' - '||  getnombreservicio(isc.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS nomservicio"+ 
													" FROM            inclu_servicio_convenio isc "+
													" INNER JOIN odontologia.inclu_presu_convenio ipc ON (isc.inclu_presu_convenio=ipc.codigo_pk)"+ 
													" INNER JOIN odontologia.inclu_presu_encabezado ipe ON (ipc.inclu_presu_encabezado=ipe.codigo_pk)"+ 
													" INNER JOIN odontologia.inclusiones_presupuesto ip ON (ip.inclu_presu_encabezado=ipe.codigo_pk) "+
													" WHERE ip.inclu_presu_encabezado=?"+
													" ORDER BY isc.servicio ";

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, detalleServiciosPresupuestoStrInclusion);
			psd.setDouble(1,presupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoServicioOdontologico dtoServicio= new DtoServicioOdontologico();
				dtoServicio.setCodigoServicio(rs.getInt("codigoservicio"));
				dtoServicio.setDescripcionServicio(rs.getString("nomservicio"));
				listadoServicios.add(dtoServicio);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rs, con);

		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarDetalleServiciosPresupuesto==> ",e);
		}
		return listadoServicios;
	}
	
	public static ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuentoPresupuesto(InfoDefinirSolucitudDsctOdon dto ){
		logger.info("**************************************************************************************************************");
		logger.info("------------------------------------CARGAR DEFINICION DE SOLICITUDES PRESUPUESTO------------------------------");
	
		ArrayList<InfoDefinirSolucitudDsctOdon> listaDescuento = new ArrayList<InfoDefinirSolucitudDsctOdon>();


		String consultaStr="select " +
								"pe.numero_identificacion as numeroIdentificacion, " +
								"pe.tipo_identificacion as tipoIdentificacion, " +
								"pe.primer_nombre as primerNombre, " +
								"pe.segundo_nombre as segundoNombre, " +
								"pe.primer_apellido as primerApellido, " +
								"pe.segundo_apellido as segundoApellido, " +
								"getintegridaddominio(pdo.estado) AS estadoDctOdo," +
								"pdo.det_dcto_odo as detDctoOdo, " +
								"pdo.det_dcto_odo_aten as detDctoOdoAten, " +
								"pdo.codigo_pk as codigoPkPresupuestoDctOdo, " +
								"pdo.valor_descuento as valorDescuento, " +
								"p.codigo_pk as codigoPresupuesto, " +
								"p.consecutivo as consecutivo, " +
								"pdo.codigo_pk AS codigoSolicitud, " +
								"cu.via_ingreso as viaIngreso, " +
								"getnombreviaingreso(cu.via_ingreso) as nombreViaIngreso, " +
								"to_char(pdo.fecha_solicitad,'dd/mm/yyyy')||' - '||pdo.hora_solicita AS fechahora, " +
								"p.estado AS acronimoEstadoPresupuesto, " +
								"getintegridaddominio(p.estado) AS estadoPresupuesto, " +
								"pdo.estado AS acronimoEstadoSolicitud," +
								"getintegridaddominio(pdo.estado) AS estadoSolicitud, " +
								"p.usuario_generacion AS usuario, " +
								" coalesce(pdo.porcentaje_dcto,0) AS porcentaje, " +
								"getnombreviaingreso(cu.via_ingreso) AS nomViaIngreso, " +
								"md.descripcion AS descripcionMotivo, " +
								"coalesce(md.codigo_pk,0) AS codigoMotivoDescuento, " +
								"getintegridaddominio(md.tipo) AS nombreTipoMotivo, " +
								"getintegridaddominio(md.indicativo) AS nombreIndicativoMotivo, " +
								"md.tipo AS acronimoTipoMotivo, " +
								"md.indicativo AS acronimoIndicativoMotivo, " +
								"p.centro_atencion AS codigoCentroAtencion, " +
								"p.cuenta as cuenta, " +
								"getnomcentroatencion(p.centro_atencion) AS nombreCentroAtencion, " +
								"coalesce(pdo.observaciones,'') AS observaciones, " +
								"pdo.inclusion AS inclusion, " +
								"null AS codigo_pk_inclusion, " +
								"auto.codigo_pk AS codigoAutorizacionPresuDctoOdon, " +
								"auto.fecha_autorizacion AS fechaAutorizacion, " +
								"auto.dias_vigencia AS diasVigencia, " +
								"p.ingreso AS ingresoSoliDescuento " +
						"FROM " +
								"odontologia.presupuesto_dcto_odon pdo " +
						"INNER JOIN " +
							"odontologia.presupuesto_odontologico p " +
								"ON (pdo.presupuesto=p.codigo_pk)" +
						"LEFT OUTER JOIN " +
							"odontologia.autorizacion_presu_dcto_odon auto " +
								"ON (auto.codigo_pk = pdo.autorizacion_presu_dcto_odon) " +
						"INNER JOIN " +
							"manejopaciente.cuentas cu " +
								"ON (cu.id= p.cuenta) " +
						"INNER JOIN " +
							"manejopaciente.pacientes pa " +
								"ON(pa.codigo_paciente=p.codigo_paciente) " +
						"INNER JOIN " +
							"administracion.personas pe " +
								"ON (pa.codigo_paciente=pe.codigo)	" +
						"LEFT OUTER JOIN " +
							"odontologia.motivos_descuentos_odon md " +
								"ON (md.codigo_pk=pdo.motivo) " +
				
						"WHERE " +
							"pdo.inclusion='"+ConstantesBD.acronimoNo+"'";
		
		
		if(dto.getCentroAtencion().getCodigo()>ConstantesBD.codigoNuncaValido && dto.getCentroAtencion().getCodigo() != 0) 
		{
				consultaStr+=" and p.centro_atencion="+dto.getCentroAtencion().getCodigo();
		}

		//TRAE ALGO LA LISTA DE PRESUPUESTO
		if(!dto.getEstadoPresupuestos().isEmpty()){
			
			consultaStr+= " and p.estado= '"+dto.getEstadoPresupuestos()+"' ";
		}
			
		consultaStr = agregarFiltrosConsultaDescuentos(dto, consultaStr);
		
		consultaStr += " ORDER BY p.centro_atencion, consecutivo, nombreIndicativoMotivo ";
		
		logger.info("\n\n\n\n Consulta \n\n\n");
		logger.info(consultaStr);
		
		try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				InfoDefinirSolucitudDsctOdon newdto = new InfoDefinirSolucitudDsctOdon();
				
				newdto.setConsecutivoPresupuesto(rs.getBigDecimal("consecutivo"));
				newdto.setCodigoPkPresupuesto(rs.getBigDecimal("codigoPresupuesto"));
				newdto.setIngresoSoliDescuento(rs.getInt("ingresoSoliDescuento"));
				newdto.setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
				newdto.setTipoIndentificacion(rs.getString("tipoIdentificacion"));
				newdto.setPrimerApellido(rs.getString("primerApellido"));
				newdto.setSegundoApellido(rs.getString("segundoApellido"));
				newdto.setPrimerNombre(rs.getString("primerNombre"));
				newdto.setSegundoNombre(rs.getString("segundoNombre"));
				newdto.getIngreso().setNombre(rs.getString("nombreViaIngreso"));
				newdto.getIngreso().setCodigo(rs.getInt("viaIngreso"));
				newdto.setFechaHora(rs.getString("fechahora"));
				newdto.setEstadoPresupuesto(rs.getString("estadoPresupuesto"));
				newdto.setAcronimoEstado(rs.getString("acronimoEstadoPresupuesto"));
				newdto.setUsuarioGeneracion(rs.getString("usuario"));
				newdto.setCodigoPkDetDescuentoOdon(rs.getBigDecimal("detDctoOdo"));
				newdto.setCodigoPkDetDescuentoOdonAten(rs.getBigDecimal("detDctoOdoAten"));
				newdto.setPorcentaje(rs.getBigDecimal("porcentaje"));
				newdto.setNombreViaIngreso(rs.getString("nomViaIngreso"));
				newdto.setDescripcionMotivo(rs.getString("descripcionMotivo"));
				newdto.setAcronimoTipoMotivo(rs.getString("acronimoTipoMotivo"));
				newdto.setTipoMotivo(rs.getString("nombreTipoMotivo"));
				newdto.setNombreIndicativoMotivo(rs.getString("nombreIndicativoMotivo"));
				newdto.setAcronimoIndicativoMotivo(rs.getString("acronimoIndicativoMotivo"));
				newdto.setCodigoMotivoDescuento(rs.getBigDecimal("codigoMotivoDescuento"));
				newdto.getCentroAtencion().setCodigo(rs.getInt("codigoCentroAtencion"));
				newdto.getCentroAtencion().setNombre(rs.getString("nombreCentroAtencion"));
				newdto.setEstadoPresupuestoDcto(rs.getString("estadoDctOdo"));
				newdto.setCodigoPkPresupuestoDctoOdon(rs.getBigDecimal("codigoPkPresupuestoDctOdo"));
				newdto.setEstadoSolicitud(rs.getString("estadoSolicitud"));
				newdto.setAcronimoEstadoSolicitud(rs.getString("acronimoEstadoSolicitud"));
				newdto.setCodigoSolicitud(rs.getBigDecimal("codigoSolicitud"));
				newdto.setObservaciones(rs.getString("observaciones"));
				newdto.setEstadoAutorizacionDescuento(rs.getString("estadoDctOdo"));
				newdto.setValorDescuento(rs.getBigDecimal("valorDescuento"));
				newdto.setCuenta(rs.getInt("cuenta"));
				newdto.setInclusion(UtilidadTexto.getBoolean(rs.getString("inclusion")));
				newdto.setCodigoPkEncabezadoInclusion(rs.getLong("codigo_pk_inclusion"));
				newdto.setCodigoPkAutorizacionPresuDctoOdon(rs.getLong("codigoAutorizacionPresuDctoOdon"));
				newdto.setFechaAutorizacion(rs.getDate("fechaAutorizacion"));
				newdto.setDiasVigencia(rs.getInt("diasVigencia"));
				
				listaDescuento.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return listaDescuento;
	}
	
	public static ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuentoInclusiones(InfoDefinirSolucitudDsctOdon dto ){
		
		logger.info("**************************************************************************************************************");
		logger.info("------------------------------------CARGAR DEFINICION DE SOLICITUDES INCLUSIONES------------------------------");
	
		ArrayList<InfoDefinirSolucitudDsctOdon> listaDescuento = new ArrayList<InfoDefinirSolucitudDsctOdon>();
		
		String consultaStr="select " +
								"pe.numero_identificacion as numeroIdentificacion, " +
								"pe.tipo_identificacion as tipoIdentificacion, " +
								"pe.primer_nombre as primerNombre, " +
								"pe.segundo_nombre as segundoNombre, " +
								"pe.primer_apellido as primerApellido, " +
								"pe.segundo_apellido as segundoApellido, " +
								"getintegridaddominio(pdo.estado) AS estadoDctOdo," +
								"pdo.det_dcto_odo as detDctoOdo, " +
								"pdo.det_dcto_odo_aten as detDctoOdoAten, " +
								"pdo.codigo_pk as codigoPkPresupuestoDctOdo, " +
								"pdo.valor_descuento as valorDescuento, " +
								"p.codigo_pk as codigoPresupuesto, " +
								"ipe.consecutivo as consecutivo, " +
								"pdo.codigo_pk AS codigoSolicitud, " +
								"cu.via_ingreso as viaIngreso, " +
								"getnombreviaingreso(cu.via_ingreso) as nombreViaIngreso, " +
								"to_char(pdo.fecha_solicitad,'dd/mm/yyyy')||' - '||pdo.hora_solicita AS fechahora, " +
								//"p.estado AS acronimoEstadoPresupuesto, " +
								//"getintegridaddominio(p.estado) AS estadoPresupuesto, " +
								"ipe.estado AS acronimoEstadoPresupuesto, " +
								"getintegridaddominio(ipe.estado) AS estadoPresupuesto, " +
								"pdo.estado AS acronimoEstadoSolicitud," +
								"getintegridaddominio(pdo.estado) AS estadoSolicitud, " +
								//"p.usuario_generacion AS usuario, " +
								"ipe.usuario AS usuario, " +
								" coalesce(pdo.porcentaje_dcto,0) AS porcentaje, " +
								"getnombreviaingreso(cu.via_ingreso) AS nomViaIngreso, " +
								"md.descripcion AS descripcionMotivo, " +
								"coalesce(md.codigo_pk,0) AS codigoMotivoDescuento, " +
								"getintegridaddominio(md.tipo) AS nombreTipoMotivo, " +
								"getintegridaddominio(md.indicativo) AS nombreIndicativoMotivo, " +
								"md.tipo AS acronimoTipoMotivo, " +
								"md.indicativo AS acronimoIndicativoMotivo, " +
								//"p.centro_atencion AS codigoCentroAtencion, " +
								"ipe.centro_atencion AS codigoCentroAtencion, "+
								"p.cuenta as cuenta, " +
								"getnomcentroatencion(ipe.centro_atencion) AS nombreCentroAtencion, " +
								"coalesce(pdo.observaciones,'') AS observaciones, " +
								"pdo.inclusion AS inclusion, " +
								"ipe.codigo_pk AS codigo_pk_inclusion, " +
								"auto.codigo_pk AS codigoAutorizacionPresuDctoOdon, " +
								"auto.fecha_autorizacion AS fechaAutorizacion, " +
								"auto.dias_vigencia AS diasVigencia, " +
								"p.ingreso AS ingresoSoliDescuento " +
						"FROM " +
								"odontologia.presupuesto_dcto_odon pdo " +
						"INNER JOIN " +
							"inclu_dcto_odontologico ido " +
								"ON(ido.presupuesto_dcto_odon=pdo.codigo_pk) "+
						"LEFT OUTER JOIN " +
							"odontologia.autorizacion_presu_dcto_odon auto " +
								"ON (auto.codigo_pk = pdo.autorizacion_presu_dcto_odon) " +
						"INNER JOIN " +
							"inclu_presu_encabezado ipe " +
								"ON(ipe.codigo_pk=ido.inclu_presu_encabezado) "+
						"INNER JOIN " +
							"odontologia.presupuesto_odontologico p " +
								"ON (pdo.presupuesto=p.codigo_pk)" +
						"INNER JOIN " +
							"manejopaciente.cuentas cu " +
								"ON (cu.id= p.cuenta) " +
						"INNER JOIN " +
							"manejopaciente.pacientes pa " +
								"ON(pa.codigo_paciente=p.codigo_paciente) " +
						"INNER JOIN " +
							"administracion.personas pe " +
								"ON (pa.codigo_paciente=pe.codigo)	" +
						"LEFT OUTER JOIN " +
							"odontologia.motivos_descuentos_odon md " +
								"ON (md.codigo_pk=pdo.motivo) " +
						"WHERE " +
							"pdo.inclusion='"+ConstantesBD.acronimoSi+"'";
						
						if(dto.getCodigoPkEncabezadoInclusion() > 0){
						
							consultaStr +=" AND ipe.codigo_pk = " + dto.getCodigoPkEncabezadoInclusion() + " ";
						}
						
						consultaStr = agregarFiltrosConsultaDescuentos(dto, consultaStr);
						
						if(dto.getCentroAtencion().getCodigo()>ConstantesBD.codigoNuncaValido && dto.getCentroAtencion().getCodigo() != 0) 
						{
								consultaStr+="AND ipe.centro_atencion="+dto.getCentroAtencion().getCodigo();
						}

						//TRAE ALGO LA LISTA DE INCLUSIONES    
						if(!dto.getEstadoInclusion().isEmpty()){
							
							consultaStr+= " and ipe.estado= '"+dto.getEstadoInclusion()+"' ";
							
						}
						
						if(dto.getOrdenamiento()!=null && !dto.getOrdenamiento().equals("")){
						
							consultaStr += " ORDER BY ipe.centro_atencion, consecutivo, nombreIndicativoMotivo " + dto.getOrdenamiento();
						}
						else
						{
							consultaStr += " ORDER BY ipe.centro_atencion, consecutivo, nombreIndicativoMotivo ";	
						}
						

						logger.info("\n\n\n\n Consulta \n\n\n");
						logger.info(consultaStr);
						
						try 
						{
							Connection con = UtilidadBD.abrirConexion();
							PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
							ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
							while(rs.next())
							{
								InfoDefinirSolucitudDsctOdon newdto = new InfoDefinirSolucitudDsctOdon();
								
								newdto.setConsecutivoPresupuesto(rs.getBigDecimal("consecutivo"));
								newdto.setCodigoPkPresupuesto(rs.getBigDecimal("codigoPresupuesto"));
								newdto.setIngresoSoliDescuento(rs.getInt("ingresoSoliDescuento"));
								newdto.setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
								newdto.setTipoIndentificacion(rs.getString("tipoIdentificacion"));
								newdto.setPrimerApellido(rs.getString("primerApellido"));
								newdto.setSegundoApellido(rs.getString("segundoApellido"));
								newdto.setPrimerNombre(rs.getString("primerNombre"));
								newdto.setSegundoNombre(rs.getString("segundoNombre"));
								newdto.getIngreso().setNombre(rs.getString("nombreViaIngreso"));
								newdto.getIngreso().setCodigo(rs.getInt("viaIngreso"));
								newdto.setFechaHora(rs.getString("fechahora"));
								newdto.setEstadoPresupuesto(rs.getString("estadoPresupuesto"));
								newdto.setAcronimoEstado(rs.getString("acronimoEstadoPresupuesto"));
								newdto.setUsuarioGeneracion(rs.getString("usuario"));
								newdto.setCodigoPkDetDescuentoOdon(rs.getBigDecimal("detDctoOdo"));
								newdto.setCodigoPkDetDescuentoOdonAten(rs.getBigDecimal("detDctoOdoAten"));
								newdto.setPorcentaje(rs.getBigDecimal("porcentaje"));
								newdto.setNombreViaIngreso(rs.getString("nomViaIngreso"));
								newdto.setDescripcionMotivo(rs.getString("descripcionMotivo"));
								newdto.setAcronimoTipoMotivo(rs.getString("acronimoTipoMotivo"));
								newdto.setTipoMotivo(rs.getString("nombreTipoMotivo"));
								newdto.setNombreIndicativoMotivo(rs.getString("nombreIndicativoMotivo"));
								newdto.setAcronimoIndicativoMotivo(rs.getString("acronimoIndicativoMotivo"));
								newdto.setCodigoMotivoDescuento(rs.getBigDecimal("codigoMotivoDescuento"));
								newdto.getCentroAtencion().setCodigo(rs.getInt("codigoCentroAtencion"));
								newdto.getCentroAtencion().setNombre(rs.getString("nombreCentroAtencion"));
								newdto.setEstadoPresupuestoDcto(rs.getString("estadoDctOdo"));
								newdto.setCodigoPkPresupuestoDctoOdon(rs.getBigDecimal("codigoPkPresupuestoDctOdo"));
								newdto.setEstadoSolicitud(rs.getString("estadoSolicitud"));
								newdto.setAcronimoEstadoSolicitud(rs.getString("acronimoEstadoSolicitud"));
								newdto.setCodigoSolicitud(rs.getBigDecimal("codigoSolicitud"));
								newdto.setObservaciones(rs.getString("observaciones"));
								newdto.setEstadoAutorizacionDescuento(rs.getString("estadoDctOdo"));
								newdto.setValorDescuento(rs.getBigDecimal("valorDescuento"));
								newdto.setCuenta(rs.getInt("cuenta"));
								newdto.setInclusion(UtilidadTexto.getBoolean(rs.getString("inclusion")));
								newdto.setCodigoPkEncabezadoInclusion(rs.getLong("codigo_pk_inclusion"));
								newdto.setCodigoPkAutorizacionPresuDctoOdon(rs.getLong("codigoAutorizacionPresuDctoOdon"));
								newdto.setFechaAutorizacion(rs.getDate("fechaAutorizacion"));
								newdto.setDiasVigencia(rs.getInt("diasVigencia"));
								
								listaDescuento.add(newdto);
							}
							SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
						}
						
						catch (SQLException e) 
						{
						logger.error("error en carga==> "+e);
						}
						
						return listaDescuento;

		
	}
	
	public static ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuento(InfoDefinirSolucitudDsctOdon dto ){
		
		ArrayList<InfoDefinirSolucitudDsctOdon> listaDescuentoPresupuesto = new ArrayList<InfoDefinirSolucitudDsctOdon>();
		ArrayList<InfoDefinirSolucitudDsctOdon> listaDescuentoInclusiones = new ArrayList<InfoDefinirSolucitudDsctOdon>();
		ArrayList<InfoDefinirSolucitudDsctOdon> listaDescuentoDefinitiva = new ArrayList<InfoDefinirSolucitudDsctOdon>();
		
		//TRAE INCLUSIONES Y PRESUPUESTO
		if(dto.isSoliGeneraInclusiones() == false && dto.isSoliGeneraPresupuesto() == false )
		{
			listaDescuentoPresupuesto = cargarDefinicionSolicitudesDescuentoPresupuesto(dto);
			listaDescuentoInclusiones = cargarDefinicionSolicitudesDescuentoInclusiones(dto);	
		}
		//TRAE SOLO INCLUSIONES		
		if(dto.isSoliGeneraInclusiones() == true && dto.isSoliGeneraPresupuesto() == false )
		{
			listaDescuentoInclusiones = cargarDefinicionSolicitudesDescuentoInclusiones(dto);	
		}
		//TRAE SOLO PRESUPUESTO
		if(dto.isSoliGeneraInclusiones() == false && dto.isSoliGeneraPresupuesto() == true )
		{
			listaDescuentoPresupuesto = cargarDefinicionSolicitudesDescuentoPresupuesto(dto);	
		}

		listaDescuentoDefinitiva.addAll(listaDescuentoPresupuesto);
		listaDescuentoDefinitiva.addAll(listaDescuentoInclusiones);
		
		/*
		 *Para llamar el mentodo sort, se debe implementar la interfaz y el método compareTo en el DTO InfoDefinirSolucitudDsctOdon 
		 *que es el incargado de comparar el objeto por NOMBRE del centro de atención a medida que se genera un registro lo com
		 */
	
		Collections.sort(listaDescuentoDefinitiva);
		return listaDescuentoDefinitiva;
	
	}
	


	/**
	 * Filtros asociados a la consulta de solicitudes de descuento Odontológico
	 * @param dto
	 * @param consultaStr
	 * @return
	 */
	private static String agregarFiltrosConsultaDescuentos(
			InfoDefinirSolucitudDsctOdon dto, String consultaStr)
	{
		
		if(dto.getCodigoPkPresupuestoDctoOdon()!=null){
			
			consultaStr+= (dto.getCodigoPkPresupuestoDctoOdon().longValue()>0 )?" AND pdo.codigo_pk="+dto.getCodigoPkPresupuestoDctoOdon().longValue(): "";
		}
		
		consultaStr+= !UtilidadTexto.isEmpty(dto.getEstadoAutorizacionDescuento()) ?" and pdo.estado='"+dto.getEstadoAutorizacionDescuento()+"'": ""; // ->  FUNCIONA CON EL TIPO
		consultaStr+= (dto.getCodigoPaciente()>0) ?" and p.codigo_paciente="+dto.getCodigoPaciente()+"": "";
		consultaStr+= !UtilidadTexto.isEmpty(dto.getEstadoSolicitud()) ? " and pdo.estado='"+dto.getEstadoSolicitud()+"' ": " ";  
		consultaStr+= !UtilidadTexto.isEmpty(dto.getEstado())?" and pdo.estado='"+dto.getEstado()+"' ": " ";
		
		//TRAE ALGO LA LISTA DE PRESUPUESTO  -  EL CHECK DE PRESUPUESTO ESTA SELECCIONADO (Busqueda por rango) -    EL CHECK DE INCLUSIONES NO ESTA SELECCIONADO  
		//consultaStr+= ((!dto.getEstadoPresupuestos().isEmpty()) && dto.isSoliGeneraPresupuesto() && dto.isSoliGeneraInclusiones() == false) ?" and p.estado= '"+dto.getEstadoPresupuestos()+"' ": "";
		
		//TRAE ALGO LA LISTA DE INCLUSIONES  -  EL CHECK DE INCLUSIONES ESTA SELECCIONADO (Busqueda por rango) -    EL CHECK DE PRESUPUESTO NO ESTA SELECCIONADO  
		//consultaStr+= ((!dto.getEstadoInclusion().isEmpty()) && dto.isSoliGeneraInclusiones() && dto.isSoliGeneraPresupuesto() == false) ?" and ipe.estado= '"+dto.getEstadoInclusion()+"' ": "";
		
	
		consultaStr+= (dto.getCodigoPaciente()>0) ?" and p.codigo_paciente="+dto.getCodigoPaciente()+"": "";
		//setEstadoAutorizacionDescuento
		
		
		if ( !UtilidadTexto.isEmpty(dto.getFechaInicialBD()) && !UtilidadTexto.isEmpty(dto.getFechaFinalBD()))
		{
		 consultaStr+= "	and  ( pdo.fecha_modifica between '"+dto.getFechaInicialBD()+"' and '"+dto.getFechaFinalBD()+"' )";
		}
		
		if(! UtilidadTexto.isEmpty(dto.getTipoBusqueda()))
		{
			
			if ( dto.getTipoBusqueda().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto) )
			{
				consultaStr+=" and pdo.det_dcto_odo_aten is null ";
			}
		
			else if ( dto.getTipoBusqueda().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
			{
				consultaStr+=" and pdo.det_dcto_odo is null ";
			}
		}
		return consultaStr;
	}
	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuestoDescuento(DtoPresupuestoOdontologicoDescuento dto ){

		logger.info("***************************************************************************************");
		logger.info("------------------------------------MODIFICAR PRESUPUESTO DESCUENTO---------------------");
		logger.info("***************************************************************************************");
		boolean retorna= false;
		String updateStr="update  odontologia.presupuesto_dcto_odon set codigo_pk=codigo_pk "; 
		

		updateStr+=!UtilidadTexto.isEmpty(dto.getEstado())?",  estado='"+dto.getEstado()+"' ": "";
		updateStr+=(dto.getMotivo()>ConstantesBD.codigoNuncaValido)? " , motivo="+dto.getMotivo():"";
		updateStr+=!UtilidadTexto.isEmpty(dto.getObservaciones())?" , observaciones= '"+dto.getObservaciones()+"' ": "";
		updateStr+=(dto.getDetalleDescuentoOdontologico()>0)? ", det_dcto_odo="+dto.getDetalleDescuentoOdontologico(): "" ;
		updateStr+=(dto.getPorcentajeDcto()>0)?",  porcentaje_dcto="+dto.getPorcentajeDcto(): "";
		updateStr+=(dto.getValorDescuento().doubleValue()>0)?" , valor_descuento="+dto.getValorDescuento().doubleValue():" ";
		updateStr+=(dto.getDetalleDescuentoOdontologicoAtencion()>0) ?",  det_dcto_odo_aten="+dto.getDetalleDescuentoOdontologicoAtencion(): "";
		updateStr+=(dto.getCodigoAutorizacionPresuDctoOdon()>0) ? ",  autorizacion_presu_dcto_odon="+dto.getCodigoAutorizacionPresuDctoOdon(): "";
		updateStr+=" , usuario_modifica= '"+ dto.getUsuarioFechaModifica().getUsuarioModifica()+"'"; 
		updateStr+=" , fecha_modifica = '"+ dto.getUsuarioFechaModifica().getFechaModificaFromatoBD()+"'";
		updateStr+=" , hora_modifica = '"+ dto.getUsuarioFechaModifica().getHoraModifica()+"'";
		updateStr+=" where codigo_pk="+dto.getCodigo()+"";
		
		logger.info("UPDATE DE MODIFICACION");
		logger.info("\n\n\n\n\n\n");
		logger.info(updateStr);
		logger.info("\n\n\n\n\n\n");
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
		
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con , updateStr);
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma "+e);
		}
		return retorna;
	}
}