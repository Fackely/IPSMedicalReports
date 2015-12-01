/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * SqlBaseCoberturasConvenioDao
 * com.princetonsa.dao.sqlbase.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;
import com.princetonsa.dto.odontologia.DtoPrograma;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public class SqlBaseCoberturasConvenioDao
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCoberturasConvenioDao.class);
	
	/**
	 * 
	 */
	private static String consultaCoberturasContrato="SELECT cc.codigo_cobertura as codigocobertura,c.descrip_cobertura as desccobertura,prioridad as prioridad,'BD' as tiporegistro from coberturas_x_contrato cc inner join cobertura c on (c.codigo_cobertura=cc.codigo_cobertura and c.institucion=cc.institucion) where cc.institucion=? and cc.codigo_contrato=? ";
	
	/**
	 * 
	 */
	private static String updateCoberturaContrato="UPDATE coberturas_x_contrato SET prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo_contrato=? and codigo_cobertura=? and institucion=?";
	
	/**
	 * 
	 */
	private static String consultaExcepcionesContrato="SELECT codigo as codigo,via_ingreso as viaingreso, getnombreviaingreso(via_ingreso) as nomviaingreso, tipo_paciente as tipopaciente, getNombreTipoPaciente(tipo_paciente) as nomtipopaciente, naturaleza_paciente as natpaciente,getnomnatpacientes(naturaleza_paciente) as nomnatpaciente,'BD' as tiporegistro from exep_para_cob_x_convcont ";
	
	/**
	 * 
	 */
	private static String cadenaEliminarExcepcion="DELETE FROM exep_para_cob_x_convcont where codigo=?";

	/**
	 * 
	 */
	private static String cadenaModificarExcepcion="UPDATE exep_para_cob_x_convcont SET via_ingreso=?, tipo_paciente=?, naturaleza_paciente=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";
	
	/**
	 * 
	 */
	private static String cadenaEliminarCobertura="DELETE FROM coberturas_x_contrato where codigo_contrato=? and codigo_cobertura=? and institucion=?";
	
	
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT " +
																	"a.codigo as codigo," +
																	"a.codigo_excepcion as codigoexcepcion," +
																	"a.clase as clase," +
																	"ci.nombre as nomclase," +
																	"a.grupo as grupo," +
																	"gi.nombre as nomgrupo," +
																	"a.subgrupo as subgrupo," +
																	"si.nombre as nomsubgrupo," +
																	"a.naturaleza as naturaleza," +
																	"'' as nomnaturaleza, "+
																	"na.nombre as nomnaturaleza, " +
																	"a.requiere_autorizacion as requiereautorizacion," +
																	"a.semanas_min_cotizacion as semanasmincotizacion," +
																	"a.cantidad_max_cub_x_ingreso as cantidad," +
																	"a.presentar_factura_compra as presfactura," +
																	"a.incluido as incluido," +
																	"'BD' as tiporegistro " +
																"from " +
																	"agrup_art_exep_cob_convxcont a " +
																	"LEFT OUTER JOIN clase_inventario ci ON(ci.codigo=a.clase) " +
																	"LEFT OUTER JOIN grupo_inventario gi ON(gi.codigo=a.grupo and gi.clase=a.clase) " +
																	"LEFT OUTER JOIN subgrupo_inventario si ON(a.clase=si.clase and a.grupo=si.grupo and a.subgrupo=si.subgrupo) " +
																	"LEFT OUTER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza and  na.institucion=a.institucion) ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_excepcion as codigoexcepcion,codigo_articulo as codigo_articulo,getdescarticulosincodigo(codigo_articulo) as descripcion_articulo,requiere_autorizacion as requiereautorizacion,semanas_min_cotizacion as semanasmincotizacion,cantidad_max_cub_x_ingreso as cantidad,presentar_factura_compra as presfactura,incluido as incluido,'BD' as tiporegistro from art_exp_cob_convxcont ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT agsexcc.codigo as codigo,agsexcc.codigo_excepcion as codigoexcepcion,agsexcc.pos as tipopos,agsexcc.possubsidiado as tipopossubsidiado, agsexcc.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,agsexcc.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, agsexcc.especialidad as especialidad,e.nombre as nomespecialidad,agsexcc.requiere_autorizacion as requiereautorizacion,agsexcc.semanas_min_cotizacion as semanasmincotizacion,agsexcc.cantidad_max_cub_x_ingreso as cantidad,incluido as incluido,'BD' as tiporegistro from  agru_ser_exep_cob_convxcont agsexcc left outer join grupos_servicios gs on (gs.codigo=agsexcc.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=agsexcc.tipo_servicio) left outer join especialidades e on (e.codigo=agsexcc.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT secc.codigo as codigo,secc.codigo_excepcion as codigoexcepcion,secc.codigo_servicio as codigo_servicio,getcodigopropservicio2(s.codigo,0)||' '||getnombreservicio(secc.codigo_servicio,0) as descripcion_servicio,secc.requiere_autorizacion as requiereautorizacion,secc.semanas_min_cotizacion as semanasmincotizacion,secc.cantidad_max_cub_x_ingreso as cantidad,secc.incluido as incluido,'BD' as tiporegistro from serv_exe_cob_convxcont secc inner join servicios s on (secc.codigo_servicio=s.codigo) ";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM agrup_art_exep_cob_convxcont WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE agrup_art_exep_cob_convxcont SET clase=?,grupo=?,subgrupo=?,naturaleza=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,presentar_factura_compra=?,incluido=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM art_exp_cob_convxcont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE art_exp_cob_convxcont SET codigo_articulo=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,presentar_factura_compra=?,incluido=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM agru_ser_exep_cob_convxcont WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE agru_ser_exep_cob_convxcont SET pos=?,possubsidiado=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,incluido=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM serv_exe_cob_convxcont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE serv_exe_cob_convxcont set codigo_servicio=?,requiere_autorizacion=?,semanas_min_cotizacion=?,cantidad_max_cub_x_ingreso=?,incluido=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String insertarProgExcepcionConvenio	=	"INSERT INTO " +
																		"facturacion.pro_exe_cob_convxcont " +
																	"(" +
																		"codigo," +
																		"programa," +
																		"incluido," +
																		"cantidad," +
																		"fecha_modifica," +
																		"usuario_modifica," +
																		"hora_modifica," +
																		"codigo_excepcion" +
																	") " +
																	"VALUES " +
																	"(" +
																		"?,?,?,?,?,?,?,?" +
																	")";
	/**
	 * 
	 */
	private static final String eliminarProgExcepcionConvenio	=	"DELETE FROM facturacion.pro_exe_cob_convxcont WHERE codigo=? ";
	
	/**
	 * 
	 */
	private static final String consultaProgExcepcionConvenio	=	"SELECT " +
																		"pecc.codigo," +
																		"pecc.programa," +
																		"pecc.incluido," +
																		"pecc.cantidad," +
																		"pecc.fecha_modifica," +
																		"pecc.usuario_modifica," +
																		"pecc.hora_modifica," +
																		"pecc.codigo_excepcion, " +
																		"p.nombre AS nomprograma," +
																		"p.codigo_programa," +
																		"coalesce(e.nombre,'') AS nomespecialidad " +
																	"FROM facturacion.pro_exe_cob_convxcont pecc " +
																	"INNER JOIN odontologia.programas p ON (p.codigo=pecc.programa) " +
																	"INNER JOIN administracion.especialidades e ON (e.codigo=p.especialidad) " +
																	"WHERE pecc.codigo_excepcion=? ";
	
	public static boolean eliminarProgExcepcionConvenio(Connection con, double codigoExcepcionPrograma)
	{
		boolean transaccion=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarProgExcepcionConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,codigoExcepcionPrograma);
			
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
			logger.error("ERROR EN eliminarProgExcepcionConvenio==> "+e);
		}
		return transaccion;
	}
	
	public static ArrayList<DtoProExeCobConvXCont> consultaProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		ArrayList<DtoProExeCobConvXCont> listadoExcepcionesConvenio= new ArrayList<DtoProExeCobConvXCont>();
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaProgExcepcionConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,dto.getCodigoExcepcion());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoProExeCobConvXCont dtoExce=new DtoProExeCobConvXCont();
				dtoExce.setCodigo(rs.getDouble("codigo"));
				dtoExce.setPrograma(rs.getDouble("programa"));
				dtoExce.setIncluido(rs.getString("incluido"));
				dtoExce.setCantidad(rs.getInt("cantidad"));
				dtoExce.setFechaModifica(rs.getString("fecha_modifica"));
				dtoExce.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoExce.setHoraModifica(rs.getString("hora_modifica"));
				dtoExce.setCodigoExcepcion(rs.getDouble("codigo_excepcion"));
				dtoExce.setNombrePrograma(rs.getString("nomprograma"));
				dtoExce.setCodigoPrograma(rs.getString("codigo_programa"));
				dtoExce.setEspecialidadPrograma(rs.getString("nomespecialidad"));
				listadoExcepcionesConvenio.add(dtoExce);	
			}
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaProgExcepcionConvenio==> "+e);
		}
		return listadoExcepcionesConvenio;
		
		
	}
	
	public static boolean insertarProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		boolean transaccion=false;
		try 
		{
			double secuenciaExce=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_pro_exe_cob_convxcont");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarProgExcepcionConvenio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuenciaExce);
			ps.setDouble(2, dto.getPrograma());
			ps.setString(3, dto.getIncluido());
			if (dto.getCantidad()>0)
				ps.setInt(4, dto.getCantidad());
			else
				ps.setNull(4, Types.INTEGER);
			ps.setString(5, dto.getFechaModifica());
			ps.setString(6, dto.getUsuarioModifica());
			ps.setString(7, dto.getHoraModifica());
			ps.setDouble(8, dto.getCodigoExcepcion());
			
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
	 * @param contrato
	 * @return
	 */
	public static HashMap obtenerListadoCoberturas(Connection con, int institucion, int contrato)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaCoberturasContrato +" order by desccobertura,prioridad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
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
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarCobertura(Connection con, String codigo,int contrato,int institucion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarCobertura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, contrato);
			ps.setString(2, codigo);
			ps.setInt(3, institucion);
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
	public static boolean insertarCobertura(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato").toString()));
			ps.setString(2, vo.get("codigoCobertura").toString());
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("prioridad").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			ps.setString(5, vo.get("usuario").toString());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
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
	 * @param contrato
	 * @return
	 */
	public static HashMap obtenerListadoExcepciones(Connection con, int institucion, int contrato)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaExcepcionesContrato+" where institucion=? and codigo_contrato =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoExcepciones");
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
	public static boolean eliminareExepciones(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agrup_art_exep_cob_convxcont WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM art_exp_cob_convxcont where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_ser_exep_cob_convxcont WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM serv_exe_cob_convxcont where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarExcepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultaExcepcionLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaExcepcionesContrato+" where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	public static boolean insertarExcepcion(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO exep_para_cob_x_convcont
			 * codigo
			 * codigo_contrato,
			 * institucion,
			 * via_ingreso,
			 * tipo_paciente,
			 * naturaleza_paciente,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values('seq_exep_para_cob_x_convcont',?,?,?,?,?,?,?,?)";
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("viaIngreso")+""));
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("tipoPaciente").toString());
			if(UtilidadTexto.isEmpty(vo.get("natPaciente")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("natPaciente")+""));
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
	public static boolean modificarExcepcion(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarExcepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE exep_para_cob_x_convcont SET 
			 * via_ingreso=?, 
			 * tipo_paciente=?, 
			 * naturaleza_paciente=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * where codigo=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso").toString()))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("viaIngreso").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipoPaciente").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("natPaciente")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("natPaciente").toString()));
			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where a.codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n consultarAgrupacionArticulos-->"+consultaAgrupacionArticulos+" where codigo_excepcion=? "+codigo);
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
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo_excepcion=? ";
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
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo=? ";
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where secc.codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			 * UPDATE agrup_art_exep_cob_convxcont SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * presentar_factura_compra=?,
			 * incluido=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("clase")+""));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("grupo")+""));
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("naturaleza")+"");
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("requiereautorizacion")+"");
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			if(UtilidadTexto.isEmpty(vo.get("presfactura")+""))
				ps.setString(8, ConstantesBD.acronimoNo);
			else
				ps.setString(8, vo.get("presfactura")+"");
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(9, ConstantesBD.acronimoNo);
			else
				ps.setString(9, vo.get("incluido")+"");
			ps.setString(10, vo.get("usuario")+"");
			ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(12, UtilidadFecha.getHoraActual());
			ps.setDouble(13, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
			 * INSERT INTO agrup_art_exep_cob_convxcont
			 * (codigo,
			 * codigo_excepcion,
			 * clase,grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * presentar_factura_compra,
			 * incluido,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values('seq_agru_art_exp_cob_convxcont'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase")+""));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo")+""));
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("naturaleza")+"");
			
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("requiereautorizacion")+"");
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(9, null);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			if(UtilidadTexto.isEmpty(vo.get("presfactura")+""))
				ps.setString(10, ConstantesBD.acronimoNo);
			else
				ps.setString(10, vo.get("presfactura")+"");
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(11, ConstantesBD.acronimoNo);
			else
				ps.setString(11, vo.get("incluido")+"");
			ps.setString(12, vo.get("usuario")+"");
			ps.setDate(13, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(14, UtilidadFecha.getHoraActual());
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
			 * UPDATE art_exp_cob_convxcont SET 
			 * codigo_articulo=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * presentar_factura_compra=?,
			 * incluido=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?";
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
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("presfactura")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("presfactura")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(6, ConstantesBD.acronimoNo);
			else
				ps.setString(6, vo.get("incluido")+"");
			
			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
			 * INSERT INTO art_exp_cob_convxcont 
			 * (codigo,
			 * codigo_excepcion,
			 * codigo_articulo,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * presentar_factura_compra,
			 * incluido,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values ('seq_art_exp_cob_convxcont'),?,?,?,?,?,?,?,?,?,?)";
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion").toString()));
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("requiereautorizacion")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("requiereautorizacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("semanasmincotizacion")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("presfactura")+""))
				ps.setString(6, ConstantesBD.acronimoNo);
			else
				ps.setString(6, vo.get("presfactura")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(7, ConstantesBD.acronimoNo);
			else
				ps.setString(7, vo.get("incluido")+"");
			
			ps.setString(8, vo.get("usuario")+"");
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
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
			 * INSERT INTO agru_ser_exep_cob_convxcont
			 * (codigo,
			 * codigo_excepcion,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * incluido,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values ('seq_agru_ser_exp_cob_convxcont'),?,?,?,?,?,?,?,?,?,?,?,?)";
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion").toString()));
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
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(9, null);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(10, ConstantesBD.acronimoNo);
			else
				ps.setString(10, vo.get("incluido")+"");
			
			ps.setString(11, vo.get("usuario")+"");
			ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(13, UtilidadFecha.getHoraActual());
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
			 * UPDATE agru_ser_exep_cob_convxcont SET 
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * incluido=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?";
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
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("semanasmincotizacion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8, Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(9, ConstantesBD.acronimoNo);
			else
				ps.setString(9, vo.get("incluido")+"");
			
			ps.setString(10, vo.get("usuario")+"");
			ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(12, UtilidadFecha.getHoraActual());
			ps.setDouble(13, Utilidades.convertirADouble(vo.get("codigo")+""));
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
			 * INSERT INTO serv_exe_cob_convxcont(
			 * codigo,
			 * codigo_excepcion,
			 * codigo_servicio,
			 * requiere_autorizacion,
			 * semanas_min_cotizacion,
			 * cantidad_max_cub_x_ingreso,
			 * incluido,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values ('seq_serv_exe_cob_convxcont'),?,?,?,?,?,?,?,?,?)";
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
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
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(6, ConstantesBD.acronimoNo);
			else
				ps.setString(6, vo.get("incluido")+"");
			
			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
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
			 * UPDATE serv_exe_cob_convxcont set 
			 * codigo_servicio=?,
			 * requiere_autorizacion=?,
			 * semanas_min_cotizacion=?,
			 * cantidad_max_cub_x_ingreso=?,
			 * incluido=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?";
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
			
			if(UtilidadTexto.isEmpty(vo.get("incluido")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("incluido")+"");
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.setDouble(9, Utilidades.convertirADouble(vo.get("codigo")+""));
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
	 * @param codigoCobertura
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarCoberturaLLave(Connection con, String codigoCobertura, int contrato, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaCoberturasContrato +" and cc.codigo_cobertura='"+codigoCobertura+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en consultarCoberturaLLave +");
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarCobertura(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(updateCoberturaContrato,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE coberturas_x_contrato SET 
			 * prioridad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * where codigo_contrato=? 
			 * and codigo_cobertura=? 
			 * and institucion=?";
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("prioridad")+""));
			ps.setString(2, vo.get("usuario")+"");
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("codigoContrato")+""));
			ps.setString(6, vo.get("codigoCobertura")+"");
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
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
	 * @return
	 */
	public static ArrayList obtenerConvenios(Connection con) 
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		try
		{
			//ordeno descendente mente por que el array list funciona como una pila . y caundo lo recorro con un iterate el primero en salir es el ultimo en entrar
			/*
			 * Hay que agregar el DISTINCT para evitar que los datos se repitan
			 * Solución Tarea 45662
			 * Felipe Pérez Granda
			 */
			String consulta = "SELECT DISTINCT " +
				"c.codigo," +
				"c.empresa," +
				"c.tipo_regimen," +
				"c.nombre," +
				"c.tipo_contrato," +
				"c.pyp," +
				"c.tipo_atencion, " +
				"case when c.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as activo " +
				"from convenios c inner join contratos co on(co.convenio=c.codigo)  where co.maneja_cobertura='"+ConstantesBD.acronimoSi+"'" ;
			
			logger.info("===> Voy a imprimir la consulta de obtener convenios: "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta+" order by c.nombre desc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigoConvenio",rs.getObject(1));
				mapa.put("codigoEmpresa",rs.getObject(2));
				mapa.put("codigoTipoRegimen",rs.getObject(3));
				mapa.put("nombreConvenio",rs.getObject(4));
				mapa.put("codigoTipoContrato",rs.getObject(5));
				mapa.put("pyp",rs.getObject(6));
				mapa.put("tipoAtencion",rs.getObject(7));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConvenios de SqlBaseUtilidadesDao: "+e);
		}
		return resultado; 
	}

}
