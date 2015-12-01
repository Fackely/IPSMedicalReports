/*
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoCoberturaServicios;
import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public class SqlBaseDetalleCoberturaDao
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseDetalleCoberturaDao.class);

	/**
	 * 
	 */
	private static String cadenaConsultaCoberturas="SELECT " +
															" dc.codigo_detalle_cob as codigo," +
															" (coalesce(dc.codigo_cobertura,' ')||'"+ConstantesBD.separadorSplit+"'||coalesce(c.tipo_cobertura,' ')) as codigocobertura," +
															" c.descrip_cobertura as desccobertura," +
															" dc.institucion as institucion," +
															" dc.via_ingreso as viaingreso," +
															" getnombreviaingreso(dc.via_ingreso) as nomviaingreso," +
															" dc.naturaleza_paciente as natpaciente," +
															" getnomnatpacientes(dc.naturaleza_paciente) as nomnatpaciente," +
															" dc.usuario_modifica as usuario," +
															" dc.fecha_modifica as fecha," +
															" dc.hora_modifica as hora," +
															" dc.tipo_paciente as tipopaciente," +
															" getNombreTipoPaciente(dc.tipo_paciente) as nomtipopaciente,"+
															" 'BD' as tiporegistro," +
															" c.tipo_cobertura AS tipocobertura " +
												" from detalle_cobertura dc " +
												" inner join cobertura c on(dc.codigo_cobertura=c.codigo_cobertura and dc.institucion=c.institucion) " +
												" where 1=1 ";

	/**
	 * 
	 */
	private static String modificarDetalleCobertura="UPDATE detalle_cobertura SET codigo_cobertura=?,via_ingreso=?,tipo_paciente=?,naturaleza_paciente=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo_detalle_cob=?";

	
	/**
	 * 
	 */
	private static final String eliminarDetalleCobertura="DELETE FROM detalle_cobertura WHERE codigo_detalle_cob=?";
	
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT " +
																"a.codigo as codigo, " +
																"a.codigo_detalle_cob as codigodetallecob, " +
																"a.clase as clase, " +
																"ci.nombre as nomclase," +
																"a.grupo as grupo," +
																"gi.nombre as nomgrupo," +
																"a.subgrupo as subgrupo," +
																"si.nombre as nomsubgrupo," +
																"naturaleza as naturaleza," +
																"na.nombre as nomnaturaleza, " +
																"requiere_autorizacion as requiereautorizacion," +
																"semanas_min_cotizacion as semanasmincotizacion," +
																"cantidad_max_cub_x_ingreso as cantidad," +
																"'BD' as tiporegistro " +
															"from " +
																"cob_agrup_articulos a " +
																"LEFT OUTER JOIN clase_inventario ci ON(ci.codigo=a.clase) " +
																"LEFT OUTER JOIN grupo_inventario gi ON(gi.codigo=a.grupo and gi.clase=a.clase) " +
																"LEFT OUTER JOIN subgrupo_inventario si ON(a.clase=si.clase and a.grupo=si.grupo and a.subgrupo=si.subgrupo) " +
																"LEFT OUTER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza and  na.institucion=a.institucion) ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_detalle_cob as codigodetallecob,codigo_articulo as codigo_articulo,getdescarticulo(codigo_articulo) as descripcion_articulo,requiere_autorizacion as requiereautorizacion,semanas_min_cotizacion as semanasmincotizacion,cantidad_max_cub_x_ingreso as cantidad,'BD' as tiporegistro from cobertura_articulos ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT cas.codigo as codigo,cas.codigo_detalle_cob as codigodetallecob,cas.pos as tipopos,cas.possubsidiado as tipopossubsidiado, cas.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,cas.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, cas.especialidad as especialidad,e.nombre as nomespecialidad,cas.requiere_autorizacion as requiereautorizacion,cas.semanas_min_cotizacion as semanasmincotizacion,cas.cantidad_max_cub_x_ingreso as cantidad,'BD' as tiporegistro from  cob_agrup_servicios cas left outer join grupos_servicios gs on (gs.codigo=cas.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=cas.tipo_servicio) left outer join especialidades e on (e.codigo=cas.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT codigo as codigo,codigo_detalle_cob as codigodetallecob,codigo_servicio as codigo_servicio,getcodigopropservicio2(codigo_servicio,0)||' '||getnombreservicio(codigo_servicio,0) as descripcion_servicio,requiere_autorizacion as requiereautorizacion,semanas_min_cotizacion as semanasmincotizacion,cantidad_max_cub_x_ingreso as cantidad,'BD' as tiporegistro from cobertura_servicios ";
	

	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM cob_agrup_articulos WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE cob_agrup_articulos SET clase=?,grupo=?,subgrupo=?,naturaleza=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM cobertura_articulos where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE cobertura_articulos SET codigo_articulo=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM cob_agrup_servicios WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE cob_agrup_servicios SET pos=?,possubsidiado=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM cobertura_servicios where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE cobertura_servicios set codigo_servicio=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String insertarCoberturaProgramas=	"INSERT INTO " +
																"facturacion.cobertura_programas " +
															"(" +
															"codigo," +
															"codigo_detalle_cob," +
															"programa," +
															"cantidad," +
															"usuario_modifica," +
															"fecha_modifica," +
															"hora_modifica " +
															") " +
															"VALUES " +
															"(?,?,?,?,?,?,?) ";
	
	private static final String eliminarCoberturaProgramas	=	"DELETE FROM facturacion.cobertura_programas WHERE codigo=? ";
	
	private static final String consultarCoberturaProgramas	=	"SELECT " +
																	"cp.codigo," +
																	"cp.codigo_detalle_cob," +
																	"cp.programa," +
																	"cp.cantidad," +
																	"cp.usuario_modifica," +
																	"cp.fecha_modifica," +
																	"cp.hora_modifica," +
																	"p.nombre AS nomprograma," +
																	"p.codigo_programa," +
																	"coalesce(e.nombre,'') AS nomespecialidad " +
																"FROM " +
																	"facturacion.cobertura_programas cp " +
																	"INNER JOIN odontologia.programas p ON (p.codigo=cp.programa) " +
																	"INNER JOIN administracion.especialidades e ON (e.codigo=p.especialidad) " +
																"WHERE " +
																	"codigo_detalle_cob=? ";
	
	private static final String insertarCoberturaServicios=		"INSERT INTO " +
																	"facturacion.cobertura_servicios " +
																"(" +
																	"codigo," +
																	"codigo_detalle_cob," +
																	"codigo_servicio," +
																	"requiere_autorizacion," +
																	"usuario_modifica," +
																	"fecha_modifica," +
																	"hora_modifica " +
																") " +
																"VALUES " +
																"(" +
																	"?,?,?,?,?,?,?" +
																")";
	
	private static final String consultarCoberturaServicios	=	"SELECT " +
																	"cs.codigo," +
																	"cs.codigo_detalle_cob," +
																	"cs.codigo_servicio," +
																	"cs.requiere_autorizacion, " +
																	"getnombreservicio(cs.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descservicio," +
																	"getcodigocups(cs.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigocups " +
																"FROM " +
																	"facturacion.cobertura_servicios cs " +
																"WHERE " +
																	"codigo_detalle_cob=? ";
	
	private static final String eliminarCoberturaServicios	=	"DELETE FROM facturacion.cobertura_servicios WHERE codigo=?";
	
	
	//Modificacion Anexo 945
	
	public static boolean eliminarCoberturaServicios(Connection con, double codigoCoberturaServicios)
	{
		boolean transaccion=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarCoberturaServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,codigoCoberturaServicios);
			
			if(ps.executeUpdate()>=0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null,null);
				transaccion=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null,null);
				transaccion=false;
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarCoberturaServicios==> "+e);
		}
		return transaccion;
	}
	
	
	public static ArrayList<DtoCoberturaServicios> consultarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		ArrayList<DtoCoberturaServicios> listadoCoberturaServicios= new ArrayList<DtoCoberturaServicios>();
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCoberturaServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,dto.getCodigoDetalleCob());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoCoberturaServicios dtoCob=new DtoCoberturaServicios();
				dtoCob.setCodigo(rs.getDouble("codigo"));
				dtoCob.setCodigoDetalleCob(rs.getDouble("codigo_detalle_cob"));
				dtoCob.setCodigoServicio(rs.getInt("codigo_servicio"));
				dtoCob.setRequiereAutorizacion(rs.getString("requiere_autorizacion"));
				dtoCob.setNombreServicio(rs.getString("descservicio"));
				dtoCob.setCodigoCUPSServicio(rs.getString("codigocups"));
				listadoCoberturaServicios.add(dtoCob);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,rs, null);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN cargarDetallePrograma==> "+e);
		}
		
		return listadoCoberturaServicios;
	}
	
	public static boolean insertarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		boolean transaccion=false;
		try 
		{
			double secuenciaCobertura=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_cobertura_servicios");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarCoberturaServicios, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuenciaCobertura);
			ps.setDouble(2, dto.getCodigoDetalleCob());
			ps.setInt(3, dto.getCodigoServicio());
			ps.setString(4, dto.getRequiereAutorizacion());
			ps.setString(5, dto.getUsuarioModifica());
			ps.setString(6, dto.getFechaModifica());
			ps.setString(7, dto.getHoraModifica());
		
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, null);
				return transaccion=true;
			}
			else
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, null);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarCoberturaServicios==> "+e);
		}
		
		return transaccion;
	}
	
	//Fin Modificacion Anexo 945
	
	
	
	public static boolean eliminarCoberturaProgramas(Connection con, double codigoCoberturaProgramas)
	{
		boolean transaccion=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarCoberturaProgramas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,codigoCoberturaProgramas);
			
			if(ps.executeUpdate()>=0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null,null);
				transaccion=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null,null);
				transaccion=false;
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarCoberturaProgramas==> "+e);
		}
		return transaccion;
	}
	
	public static ArrayList<DtoCoberturaProgramas> consultarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto)
	{
		ArrayList<DtoCoberturaProgramas> listadoCoberturaProgramas= new ArrayList<DtoCoberturaProgramas>();
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCoberturaProgramas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,dto.getCodigoDetalleCob());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoCoberturaProgramas dtoCob=new DtoCoberturaProgramas();
				dtoCob.setCodigo(rs.getDouble("codigo"));
				dtoCob.setCodigoDetalleCob(rs.getDouble("codigo_detalle_cob"));
				dtoCob.setPrograma(rs.getDouble("programa"));
				dtoCob.setCantidad(rs.getInt("cantidad"));
				dtoCob.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoCob.setFechaModifica(rs.getString("fecha_modifica"));
				dtoCob.setHoraModifica(rs.getString("hora_modifica"));
				dtoCob.setNombrePrograma(rs.getString("nomprograma"));
				dtoCob.setEspecialidadPrograma(rs.getString("nomespecialidad"));
				dtoCob.setCodigoPrograma(rs.getString("codigo_programa"));
				listadoCoberturaProgramas.add(dtoCob);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,rs, null);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN cargarDetallePrograma==> "+e);
		}
		
		return listadoCoberturaProgramas;
	}
	
	public static boolean insertarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto)
	{
		boolean transaccion=false;
		try 
		{
			double secuenciaCobertura=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_cob_prog");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarCoberturaProgramas, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuenciaCobertura);
			ps.setDouble(2, dto.getCodigoDetalleCob());
			ps.setDouble(3, dto.getPrograma());
			if (dto.getCantidad()>0)
				ps.setInt(4, dto.getCantidad());
			else
				ps.setNull(4, Types.INTEGER);
			ps.setString(5, dto.getUsuarioModifica());
			ps.setString(6, dto.getFechaModifica());
			ps.setString(7, dto.getHoraModifica());
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, null);
				return transaccion=true;
			}
			else
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, null);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarCoberturaProgramas==> "+e);
		}
		
		return transaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerListadoCoberturas(Connection con, int institucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaCoberturas+" and dc.institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoCoberturas");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetCobertura
	 * @return
	 */
	public static boolean eliminarDetalleCobertura(Connection con, String codigoDetCobertura)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM cob_agrup_articulos WHERE codigo_detalle_cob=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCobertura));
			ps.executeUpdate();

			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM cob_agrup_servicios WHERE codigo_detalle_cob=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCobertura));
			ps.executeUpdate();

			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM cobertura_articulos WHERE codigo_detalle_cob=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCobertura));
			ps.executeUpdate();

			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM cobertura_servicios WHERE codigo_detalle_cob=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCobertura));
			ps.executeUpdate();

			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleCobertura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetCobertura));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetallecobertura
	 * @return
	 */
	public static HashMap consultarDetalleCoberturaLLave(Connection con, String codigoDetallecobertura)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaCoberturas+" and dc.codigo_detalle_cob=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDetallecobertura));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoCoberturas");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena 
	 * @return
	 */
	public static boolean insertarDetalleCobertura(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO detalle_cobertura 
			 * (codigo_detalle_cob,
			 * codigo_cobertura,
			 * institucion,
			 * via_ingreso,
			 * tipo_paciente,
			 * naturaleza_paciente,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica)  values ('seq_detalle_cobertura'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("codigoCobertura").toString());
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso")+""))
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("viaIngreso").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("tipoPaciente").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("natPaciente")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("natPaciente").toString()));
			
			ps.setString(6, vo.get("usuario").toString());
			
			ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarDetalleCobertura(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarDetalleCobertura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("naturaleza >>>("+vo.get("natPaciente")+")");
			logger.info("Tipo paciente >>>("+vo.get("tipoPaciente")+")");
			
			/**
			 * UPDATE detalle_cobertura SET 
			 * codigo_cobertura=?,
			 * via_ingreso=?,
			 * tipo_paciente=?,
			 * naturaleza_paciente=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * where codigo_detalle_cob=?
			 */
			
			ps.setString(1, vo.get("codigoCobertura").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso")+""))
				ps.setNull(2, Types.INTEGER);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("viaIngreso")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, vo.get("tipoPaciente").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("natPaciente")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("natPaciente").toString()));
			
			ps.setString(5, vo.get("usuario").toString());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo").toString()));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT codigo_cobertura,descrip_cobertura,tipo_cobertura from cobertura where institucion = ? and activo='"+ConstantesBD.acronimoSi+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2),rs.getString(3)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where a.codigo_detalle_cob=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			logger.info("\n\n\nconsultaAgrupacionArticulos-> "+consultaAgrupacionArticulos+" where a.codigo_detalle_cob="+codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where a.codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadena=consultaAgrupacionServicios+" where cas.codigo_detalle_cob=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadena=consultaAgrupacionServicios+" where cas.codigo=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_detalle_cob=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarServicios(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_detalle_cob=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public static boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cob_agrup_articulos SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("clase").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("naturaleza").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("requiereautorizacion").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(8, vo.get("usuario").toString());
			
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			ps.setDouble(11, Utilidades.convertirADouble(vo.get("codigo").toString()));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarAgrupacionArticulos(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO cob_agrup_articulos
			 * (codigo,
			 * codigo_detalle_cob,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values('seq_cob_agrup_articulo'),?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigodetallecob").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("naturaleza").toString());
			
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("requiereautorizacion").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(9, null);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(10, vo.get("usuario").toString());
			ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(12, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public static boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cobertura_articulos SET 
			 * codigo_articulo=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("articulo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo")+""));
			
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarArticulos(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO cobertura_articulos (
			 * codigo,
			 * codigo_detalle_cob,
			 * codigo_articulo,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_cobertura_articulo'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigodetallecob")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("articulo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public static boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionSevicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarAgrupacionServicios(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO cob_agrup_servicios(
			 * codigo,
			 * codigo_detalle_cob,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_cob_agrup_servicios'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigodetallecob")+""));
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipopos")+"");
			if(UtilidadTexto.isEmpty(vo.get("tipopossubsidiado")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("tipopossubsidiado")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("gruposervicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("tiposervicio")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(6, null);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("especialidad")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(9, null);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(10, vo.get("usuario")+"");
			ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(12, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cob_agrup_servicios SET 
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(1, null);
			else
				ps.setString(1, vo.get("tipopos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("tipopossubsidiado")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipopossubsidiado")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("gruposervicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("tiposervicio")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("especialidad")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(6, ConstantesBD.acronimoNo);
			else
				ps.setString(6, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(9, vo.get("usuario")+"");
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(11, UtilidadFecha.getHoraActual());
			ps.setDouble(12, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public static boolean eliminarServicios(Connection con, String codigoComponente)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarServicios(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO cobertura_servicios(
			 * codigo,
			 * codigo_detalle_cob,
			 * codigo_servicio,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_cobertura_servicios'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigodetallecob")+""));
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("servicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarServicios(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cobertura_servicios set 
			 * codigo_servicio=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("servicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("semanasmincotizacion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}


}