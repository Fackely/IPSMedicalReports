package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;

import com.princetonsa.dao.sqlbase.interfaz.SqlBaseParamInterfazSistema1EDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import org.apache.log4j.Logger;
//import org.hibernate.hql.ast.tree.DotNode;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoDetConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoDetVigConRetencion;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


public class SqlBaseDetConceptosRetencionDao
{
	private static Logger logger=Logger.getLogger(SqlBaseDetConceptosRetencionDao.class);
	
	private static final String consultaDetConceptosRetencion=	"SELECT " +
																	"dcr.consecutivo," +
																	"to_char(dcr.fecha_vigencia_inicial,'"+ConstantesBD.formatoFechaAp+"') AS fechavigencia," +
																	"dcr.tipo_retencion AS tiporetencion," +
																	"dcr.institucion," +
																	"dcr.fecha_modifica AS fechamodi," +
																	"dcr.hora_modifica AS horamodi, " +
																	"dcr.usuario_modifica AS usumodi," +
																	"tr.codigo, " +
																	"tr.descripcion," +
																	"tr.sigla," +
																	"tr.codigo_interfaz AS codigointerfaz " +
																"FROM " +
																	"administracion.det_conceptos_retencion dcr " +
																"INNER JOIN " +
																	"administracion.tipos_retencion tr ON (tr.consecutivo=dcr.tipo_retencion) " +
																"ORDER BY " +
																	"fecha_vigencia_inicial DESC";
	
	private static final String insertarDetConceptosRetencion=	"INSERT INTO " +
																	"administracion.det_conceptos_retencion " +
																"(" +
																"consecutivo," +
																"fecha_vigencia_inicial," +
																"tipo_retencion," +
																"fecha_modifica," +
																"hora_modifica," +
																"usuario_modifica," +
																"institucion" +
																") " +
																"VALUES " +
																"(?,?,?,?,?,?,?)";
	
	private static final String validarExisteDetConceptosRetencion =	"SELECT *FROM " +
																			"administracion.det_conceptos_retencion dct " +
																		"WHERE " +
																			"dct.fecha_vigencia_inicial=? " +
																		"AND " +
																			"dct.tipo_retencion=? ";
	
	private static final String validarDetalleXConcepto	=		"SELECT " +
																	"dcr.consecutivo as consdetcon, " +
																	"dvcr.consecutivo AS consdet " +
																"FROM " +
																	"administracion.det_vig_con_retencion dvcr " +
																"INNER JOIN " +
																	"administracion.det_conceptos_retencion dcr  ON (dcr.consecutivo=dvcr.det_concepto_retencion) " +
																"WHERE " +
																	"dcr.consecutivo=? ";
	
	private static final String eliminarDetConcepto=	"DELETE FROM " +
															"administracion.det_conceptos_retencion " +
														"WHERE " +
															"consecutivo=? ";
	
	private static final String detPoseeDetalles	=	"SELECT " +
															"dv.consecutivo " +
														"FROM " +
															"administracion.det_vig_con_retencion dv " +
														"LEFT OUTER JOIN " +
															"administracion.det_vig_con_ret_grupo g ON (g.det_vig_con_retencion=dv.consecutivo) " +
														"LEFT OUTER JOIN " +
															"administracion.det_vig_con_ret_clase c ON (c.det_vig_con_retencion=dv.consecutivo) " +
														"LEFT OUTER JOIN " +
															"administracion.det_vig_con_ret_cfv cfv ON (cfv.det_vig_con_retencion=dv.consecutivo) " +
														"WHERE " +
															"dv.consecutivo=?";
														
														
	
	private static final String consultarDetVigenciaConceptosRetencion	=	"SELECT " +
																				"dvcr.consecutivo, " +
																				"dvcr.concepto_retencion AS conceptoretencion, " +
																				"dvcr.det_concepto_retencion AS detconceptoretencion, " +
																				"dvcr.indicativo_integral AS indicativo, " +
																				"dvcr.base_minima AS baseminima, " +
																				"dvcr.porcentaje_ret_int AS porcentaje, " +
																				"dvcr.activo," +
																				"dcr.consecutivo AS consecutivodetcon," +
																				"cr.descripcion_concepto AS descconcepto " +
																			"FROM " +
																				"administracion.det_vig_con_retencion dvcr " +
																			"INNER JOIN " +
																				"administracion.det_conceptos_retencion dcr ON(dcr.consecutivo=dvcr.det_concepto_retencion) " +
																			"INNER JOIN " +
																				"administracion.conceptos_retencion cr ON (cr.consecutivo=dvcr.concepto_retencion) " +
																			"WHERE " +
																				"dcr.consecutivo=? " +
																			"AND " +
																				"dvcr.activo='"+ConstantesBD.acronimoSi+"' "+
																			"ORDER BY " +
																				"dvcr.concepto_retencion ASC ";
	
	private static final String insertarDetVigConRetencion=	"INSERT INTO " +
																"administracion.det_vig_con_retencion " +
															"(" +
															"consecutivo," +
															"concepto_retencion," +
															"det_concepto_retencion," +
															"indicativo_integral," +
															"base_minima," +
															"porcentaje_ret_int," +
															"fecha_modifica," +
															"hora_modifica," +
															"usuario_modifica," +
															"activo " +
															")" +
															"VALUES " +
															"(?,?,?,?,?,?,?,?,?,?)";
	
	private static final String consultarDetRetXGrupoServicio	= "SELECT " +
																		"g.consecutivo, " +
																		"g.det_vig_con_retencion AS detvigconretencion, " +
																		"g.grupo, " +
																		"g.base_minima AS baseminima, " +
																		"g.porcentaje_ret_int  AS porcentaje, " +
																		"gs.descripcion AS descservicio " +
																	"FROM " +
																		"administracion.det_vig_con_ret_grupo g " +
																	"INNER JOIN " +
																		"facturacion.grupos_servicios gs ON (gs.codigo=g.grupo) " +
																	"INNER JOIN " +
																		"administracion.det_vig_con_retencion dv ON (dv.consecutivo=g.det_vig_con_retencion) " +
																	"WHERE " +
																		"g.det_vig_con_retencion=? " +
																	"AND " +
																		"g.activo='"+ConstantesBD.acronimoSi+"' " +
																	"ORDER BY " +
																		"g.grupo DESC ";
																	
	private static final String insertarDetRetXGrupoServicio	= 	"INSERT INTO " +
																		"administracion.det_vig_con_ret_grupo " +
																	"(" +
																	"consecutivo," +
																	"det_vig_con_retencion," +
																	"grupo," +
																	"base_minima," +
																	"porcentaje_ret_int," +
																	"fecha_modifica," +
																	"hora_modifica," +
																	"usuario_modifica," +
																	"activo" +
																	") " +
																	"VALUES " +
																	"(?,?,?,?,?,?,?,?,?)";
	
	private static final String inactivarDetRetXGrupoServicio	=	"UPDATE " +
																		"administracion.det_vig_con_ret_grupo " +
																	"SET " +
																		"activo='"+ConstantesBD.acronimoNo+"'," +
																		"fecha_inactivacion=?," +
																		"hora_inactivacion=?," +
																		"usuario_inactivacion=? " +
																	"WHERE " +
																		"consecutivo=?";
	
	private static final String consultarDetRetXClaseInv=	"SELECT " +
																	"c.consecutivo, " +
																	"c.det_vig_con_retencion AS detvigconretencion, " +
																	"c.clase, " +
																	"c.base_minima AS baseminima, " +
																	"c.porcentaje_ret_int  AS porcentaje, " +
																	"ci.nombre " +
																"FROM " +
																	"administracion.det_vig_con_ret_clase c " +
																"INNER JOIN " +
																	"inventarios.clase_inventario ci ON (ci.codigo=c.clase) " +
																"INNER JOIN " +
																	"administracion.det_vig_con_retencion dv ON (dv.consecutivo=c.det_vig_con_retencion) " +
																"WHERE " +
																	"c.det_vig_con_retencion=? " +
																"AND " +
																	"c.activo='"+ConstantesBD.acronimoSi+"' " +
																"ORDER BY " +
																	"c.clase DESC ";
	
	private static final String insertarDetRetXClaseInv=	"INSERT INTO " +
																"administracion.det_vig_con_ret_clase " +
																"(" +
																"consecutivo," +
																"det_vig_con_retencion," +
																"clase," +
																"base_minima," +
																"porcentaje_ret_int," +
																"fecha_modifica," +
																"hora_modifica," +
																"usuario_modifica," +
																"activo" +
																") " +
																"VALUES " +
																"(?,?,?,?,?,?,?,?,?)";
	
	private static final String inactivarDetRetXClaseInv	=	"UPDATE " +
																	"administracion.det_vig_con_ret_clase " +
																"SET " +
																	"activo='"+ConstantesBD.acronimoNo+"'," +
																	"fecha_inactivacion=?," +
																	"hora_inactivacion=?," +
																	"usuario_inactivacion=? " +
																"WHERE " +
																	"consecutivo=?";
	
	public static final String consultarDetRetXConceptos	=	"SELECT " +
																	"c.consecutivo, " +
																	"c.det_vig_con_retencion AS detvigconretencion, " +
																	"c.concepto, " +
																	"c.base_minima AS baseminima, " +
																	"c.porcentaje_ret_int  AS porcentaje, " +
																	"cfv.descripcion AS desccfv " +
																"FROM " +
																	"administracion.det_vig_con_ret_cfv c " +
																"INNER JOIN " +
																	"facturasvarias.conceptos_facturas_varias cfv ON (cfv.consecutivo=c.concepto) " +
																"INNER JOIN " +
																	"administracion.det_vig_con_retencion dv ON (dv.consecutivo=c.det_vig_con_retencion) " +
																"WHERE " +
																	"c.det_vig_con_retencion=? " +
																"AND " +
																	"c.activo='"+ConstantesBD.acronimoSi+"' " +
																"ORDER BY " +
																	"c.concepto DESC ";
	
	private static final String insertarDetRetXConcepto=	"INSERT INTO " +
																"administracion.det_vig_con_ret_cfv " +
															"(" +
																"consecutivo," +
																"det_vig_con_retencion," +
																"concepto," +
																"base_minima," +
																"porcentaje_ret_int," +
																"fecha_modifica," +
																"hora_modifica," +
																"usuario_modifica," +
																"activo" +
																") " +
															"VALUES " +
																"(?,?,?,?,?,?,?,?,?)";
	
	private static final String inactivarDetRetXConcepto	=	"UPDATE " +
																	"administracion.det_vig_con_ret_cfv " +
																"SET " +
																	"activo='"+ConstantesBD.acronimoNo+"'," +
																	"fecha_inactivacion=?," +
																	"hora_inactivacion=?," +
																	"usuario_inactivacion=? " +
																"WHERE " +
																	"consecutivo=?";
	
	private static final String inactivarDetVigConRetencion = 	"UPDATE " +
																	"administracion.det_vig_con_retencion " +
																"SET " +
																	"activo=?," +
																	"fecha_inactivacion=?," +
																	"hora_inactivacion=?," +
																	"usuario_inactivacion=? " +
																"WHERE " +
																	"consecutivo=? ";
	
	private static final String ingresarLog = 	"INSERT INTO " +
													"administracion.log_det_vig_con_retencion " +
												"( " +
													"consecutivo," +
													"det_vig_con_retencion," +
													"indicativo_integral," +
													"base_minima," +
													"porcentaje_ret_int," +
													"fecha_modifica," +
													"hora_modifica," +
													"usuario_modifica " +
													
												") " +
												"VALUES " +
													"(?,?,?,?,?,?,?,?) ";
	
	private static final String actualizarGrupo 	=	"UPDATE " +
															"administracion.det_vig_con_ret_grupo " +
														"SET " +
															"det_vig_con_retencion=? " +
														"WHERE " +
															"det_vig_con_retencion=? ";
	
	private static final String actualizarClase 	=	"UPDATE " +
															"administracion.det_vig_con_ret_clase " +
														"SET " +
															"det_vig_con_retencion=? " +
														"WHERE " +
															"det_vig_con_retencion=? ";
	
	private static final String actualizarCfv 	=		"UPDATE " +
															"administracion.det_vig_con_ret_cfv " +
														"SET " +
															"det_vig_con_retencion=? " +
														"WHERE " +
															"det_vig_con_retencion=? ";
	
	private static final String poseeDetalles = 	"SELECT  " +
														"dvcr.consecutivo " +
													"FROM " +
														"administracion.det_vig_con_retencion dvcr " +
													"LEFT OUTER JOIN " +
														"administracion.det_vig_con_ret_grupo dvcrg ON (dvcrg.det_vig_con_retencion=dvcr.consecutivo) "+
													"LEFT OUTER JOIN " +
														"administracion.det_vig_con_ret_clase dvcrc ON (dvcrc.det_vig_con_retencion=dvcr.consecutivo) "+
													"LEFT OUTER JOIN " +
														"administracion.det_vig_con_ret_cfv dvcrx ON (dvcrx.det_vig_con_retencion=dvcr.consecutivo) " +
													"WHERE " +
														"dvcr.consecutivo=? " +
													"AND (" +
														"dvcrg.activo='"+ConstantesBD.acronimoSi+"' "+
													"OR " +
														"dvcrc.activo='"+ConstantesBD.acronimoSi+"' "+
													"OR " +
														"dvcrx.activo='"+ConstantesBD.acronimoSi+"'" +
														")";
	
	
	public static ArrayList<DtoDetConceptosRetencion> consultaDetConceptosRetencion()
	{
		ArrayList <DtoDetConceptosRetencion> arrayDetConceptosRetencion=new ArrayList<DtoDetConceptosRetencion>();
		Connection con;		
		con= UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetConceptosRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetConceptosRetencion dto=new DtoDetConceptosRetencion();
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setFechaVigenciaInicial(rs.getString("fechavigencia"));
				dto.setTipoRetencion(rs.getString("tiporetencion"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setFechaModifica(rs.getString("fechamodi"));
				dto.setHoraModifica(rs.getString("horamodi"));
				dto.setUsuarioModifica(rs.getString("usumodi"));
				dto.getDtoTiposRetencion().setCodigo(rs.getString("codigo"));
				dto.getDtoTiposRetencion().setDescripcion(rs.getString("descripcion"));
				dto.getDtoTiposRetencion().setSigla(rs.getString("sigla"));
				dto.getDtoTiposRetencion().setCodigoInterfaz(rs.getString("codigointerfaz"));
				arrayDetConceptosRetencion.add(dto);
			}
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR.------>>>>>>"+e);
			e.printStackTrace();
		}	
		return arrayDetConceptosRetencion;
	}
	
	public static boolean insertarDetConceptosRetencion (DtoDetConceptosRetencion dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetConceptosRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_det_concep_retencion"));
			ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaVigenciaInicial()));
			ps.setInt(3,Utilidades.convertirAEntero(dto.getTipoRetencion()));
			ps.setString(4,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(5,dto.getHoraModifica());
			ps.setString(6,dto.getUsuarioModifica());
			ps.setInt(7,Utilidades.convertirAEntero(dto.getInstitucion()));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarDetConceptosretencion / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean validarExisteDetConceptosRetencion (DtoDetConceptosRetencion dto)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con;		
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(validarExisteDetConceptosRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaVigenciaInicial()));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getTipoRetencion()));
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
				transaccionExitosa=true;
			
			UtilidadBD.cerrarConexion(con);
		}
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / validarExisteDetConceptosRetencion / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean validarDetalleXConcepto (String consecutivo)
	{
		boolean transaccionExitosa=false;
		
		try 
		{
			Connection con;		
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(validarDetalleXConcepto, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
			
			UtilidadBD.cerrarConexion(con);
			
		}
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / validarDetalleXConcepto / "+e);
			return transaccionExitosa;
		}
		
		if (!transaccionExitosa)
		{
			try
			{
				Connection con;		
				con= UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarDetConcepto, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR / eliminarDetConcepto / "+e);
				return transaccionExitosa;
			}
		}
		
		return transaccionExitosa;
	}
	
	
	public static ArrayList<DtoDetVigConRetencion> consultarDetVigenciaConceptosRetencion(String consecutivo)
	{
	ArrayList <DtoDetVigConRetencion> arrayDetVigenciaConceptosRetencion=new ArrayList<DtoDetVigConRetencion>();
	Connection con;		
	try
	{
	con= UtilidadBD.abrirConexion();
	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDetVigenciaConceptosRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
	ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
	
	while(rs.next())
	{
		DtoDetVigConRetencion dto=new DtoDetVigConRetencion();
		dto.setConsecutivoPk(rs.getString("consecutivo"));
		dto.setConceptoRetencion(rs.getString("conceptoretencion"));
		dto.setDetConceptoretencion(rs.getString("detconceptoretencion"));
		dto.setIndicativoIntegral(rs.getString("indicativo"));
		dto.setBaseMinima(rs.getString("baseminima"));
		dto.setPorcentajeRetInt(rs.getString("porcentaje"));
		dto.setActivo(rs.getString("activo"));
		dto.setDescConceptoRetencion(rs.getString("descconcepto"));
		arrayDetVigenciaConceptosRetencion.add(dto);
	}
	
	UtilidadBD.cerrarConexion(con);
	}
	catch (SQLException e)
	{
		logger.info("ERROR  /consultarDetVigenciaConceptosRetencion/ "+e);
		e.printStackTrace();
		}	
		return arrayDetVigenciaConceptosRetencion;
	}
	
	public static boolean insertarDetVigConRetencion (DtoDetVigConRetencion dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetVigConRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_det_vig_con_ret"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getConceptoRetencion()));
			ps.setInt(3,Utilidades.convertirAEntero(dto.getDetConceptoretencion()));
			ps.setString(4,dto.getIndicativoIntegral());
			if (!dto.getBaseMinima().equals(""))
				ps.setInt(5,Utilidades.convertirAEntero(dto.getBaseMinima()));
			else
				ps.setInt(5, 0);
			if (!dto.getPorcentajeRetInt().equals(""))
				ps.setDouble(6,Utilidades.convertirADouble(dto.getPorcentajeRetInt()));
			else
				ps.setDouble(6, 0);
				
			ps.setString(7,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(8,dto.getHoraModifica());
			ps.setString(9,dto.getUsuarioModifica());
			ps.setString(10,dto.getActivo());
			
			
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarDetVigConRetencion / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
		
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXGrupoServicio(String consecutivo)
	{
	ArrayList <DtoDetVigConRet> arrayDetVigenciaConGrupo=new ArrayList<DtoDetVigConRet>();
	Connection con;		
	try
	{
		con= UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDetRetXGrupoServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("------->"+consultarDetRetXGrupoServicio);
		logger.info("------->"+consecutivo);
		ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
		ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		
		while(rs.next())
		{
			DtoDetVigConRet dto=new DtoDetVigConRet();
			dto.setConsecutivoPk(rs.getString("consecutivo"));
			dto.setDetVigconRetencion(rs.getString("detvigconretencion"));
			dto.setTipoElementoRetencion(rs.getString("grupo"));
			dto.setBaseMinima(rs.getString("baseminima"));
			dto.setPorcentaje(rs.getString("porcentaje"));
			dto.setDescServicio(rs.getString("descservicio"));
			arrayDetVigenciaConGrupo.add(dto);
		}
		
		UtilidadBD.cerrarConexion(con);
	}
	
	catch (SQLException e)
	{
		logger.info("ERROR  /consultarDetRetXGrupoServicio/ "+e);
		e.printStackTrace();
	}	
	return arrayDetVigenciaConGrupo;
	}
	
	
	public static boolean insertarDetRetXGrupoServicio (DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetRetXGrupoServicio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_det_vig_con_ret_grupo"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getDetVigconRetencion()));
			ps.setInt(3, Utilidades.convertirAEntero(dto.getTipoElementoRetencion()));
			ps.setInt(4,Utilidades.convertirAEntero(dto.getBaseMinima()));
			ps.setDouble(5, Utilidades.convertirADouble(dto.getPorcentaje()));
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(7, dto.getHoraModifica());
			ps.setString(8, dto.getUsuarioModifica());
			ps.setString(9, dto.getActivo());
		
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarDetRetXGrupoServicio / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean inactivarDetRetXGrupoServicio (DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarDetRetXGrupoServicio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(2, dto.getHoraInactivacion());
			ps.setString(3, dto.getUsuarioInactivacion());
			ps.setInt(4,Utilidades.convertirAEntero(dto.getConsecutivoPk()));
					
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / inactivarDetRetXGrupoServicio / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXClaseInv(String consecutivo)
	{
		ArrayList <DtoDetVigConRet> arrayDetVigenciaConClase=new ArrayList<DtoDetVigConRet>();
		Connection con;		
		try
		{
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDetRetXClaseInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoDetVigConRet dto=new DtoDetVigConRet();
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setDetVigconRetencion(rs.getString("detvigconretencion"));
				dto.setTipoElementoRetencion(rs.getString("clase"));
				dto.setBaseMinima(rs.getString("baseminima"));
				dto.setPorcentaje(rs.getString("porcentaje"));
				dto.setNombreInv(rs.getString("nombre"));
				arrayDetVigenciaConClase.add(dto);
			}
			
			UtilidadBD.cerrarConexion(con);
		}
		
		catch (SQLException e)
		{
			logger.info("ERROR  /consultarDetRetXClaseInv/ "+e);
			e.printStackTrace();
		}	
		return arrayDetVigenciaConClase;
	}
	
	public static boolean insertarDetRetXClaseInv (DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetRetXClaseInv, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_det_vig_con_ret_clase"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getDetVigconRetencion()));
			ps.setInt(3, Utilidades.convertirAEntero(dto.getTipoElementoRetencion()));
			ps.setInt(4,Utilidades.convertirAEntero(dto.getBaseMinima()));
			ps.setDouble(5, Utilidades.convertirADouble(dto.getPorcentaje()));
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(7, dto.getHoraModifica());
			ps.setString(8, dto.getUsuarioModifica());
			ps.setString(9, dto.getActivo());
		
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarDetRetXClaseInv / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean inactivarDetRetXClaseInv(DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarDetRetXClaseInv, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(2, dto.getHoraInactivacion());
			ps.setString(3, dto.getUsuarioInactivacion());
			ps.setInt(4,Utilidades.convertirAEntero(dto.getConsecutivoPk()));
					
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / inactivarDetRetXClaseInv / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXConceptos(String consecutivo)
	{
		ArrayList <DtoDetVigConRet> arrayDetVigenciaConcepto=new ArrayList<DtoDetVigConRet>();
		Connection con;		
		try
		{
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDetRetXConceptos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoDetVigConRet dto=new DtoDetVigConRet();
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setDetVigconRetencion(rs.getString("detvigconretencion"));
				dto.setTipoElementoRetencion(rs.getString("concepto"));
				dto.setBaseMinima(rs.getString("baseminima"));
				dto.setPorcentaje(rs.getString("porcentaje"));
				dto.setDescConcepto(rs.getString("desccfv"));
				arrayDetVigenciaConcepto.add(dto);
			}
			
			UtilidadBD.cerrarConexion(con);
		}
		
		catch (SQLException e)
		{
			logger.info("ERROR  /consultarDetRetXConceptos/ "+e);
			e.printStackTrace();
		}	
		return arrayDetVigenciaConcepto;
	}
	
	public static boolean insertarDetRetXConcepto (DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetRetXConcepto, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_det_vig_con_ret_cfv"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getDetVigconRetencion()));
			ps.setInt(3, Utilidades.convertirAEntero(dto.getTipoElementoRetencion()));
			ps.setInt(4,Utilidades.convertirAEntero(dto.getBaseMinima()));
			ps.setDouble(5, Utilidades.convertirADouble(dto.getPorcentaje()));
			ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(7, dto.getHoraModifica());
			ps.setString(8, dto.getUsuarioModifica());
			ps.setString(9, dto.getActivo());
		
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarDetRetXConcepto / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean inactivarDetRetXConcepto(DtoDetVigConRet dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarDetRetXConcepto, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(2, dto.getHoraInactivacion());
			ps.setString(3, dto.getUsuarioInactivacion());
			ps.setInt(4,Utilidades.convertirAEntero(dto.getConsecutivoPk()));
					
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / inactivarDetRetXConcepto / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean detPoseeDetalles(String consecutivo)
	{
		Connection con;
		boolean transaccionExitosa=false;
		try
		{
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(detPoseeDetalles,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(consecutivo));
		
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			
			UtilidadBD.cerrarConexion(con);
				
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / detPoseeDetalles / "+e);
			return transaccionExitosa;
		}
		return transaccionExitosa;
	}
	
	public static boolean inactivarDetVigConRetencion (DtoDetVigConRetencion dto)
	{
		Connection con;
		boolean transaccionExitosa=false;
		try
		{
			con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarDetVigConRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,ConstantesBD.acronimoNo);
			ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(3,dto.getHoraInactivacion());
			ps.setString(4,dto.getUsuarioInactivacion());
			ps.setInt(5, Utilidades.convertirAEntero(dto.getConsecutivoPk()));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
					
			UtilidadBD.cerrarConexion(con);
				
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / inactivarDetVigConRetencion / "+e);
			return transaccionExitosa;
		}
		return transaccionExitosa;
	}
	
	public static boolean ingresarLog (DtoDetVigConRetencion dto)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarLog, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_log_det_vig_con_ret"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getConsecutivoPk()));
			ps.setString(3,dto.getIndicativoIntegral());
			ps.setInt(4,Utilidades.convertirAEntero(dto.getBaseMinima()));
			ps.setDouble(5,Utilidades.convertirADouble(dto.getPorcentajeRetInt()));
			ps.setString(6,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(7,dto.getHoraModifica());
			ps.setString(8,dto.getUsuarioModifica());
			
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarLog / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean actualizarGrupo (int consecutivoNuevo, int consecutivoViejo)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarGrupo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,consecutivoNuevo);
			ps.setInt(2, consecutivoViejo);
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / actualizarGrupo / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean actualizarClase (int consecutivoNuevo, int consecutivoViejo)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarClase, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,consecutivoNuevo);
			ps.setInt(2, consecutivoViejo);
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / actualizarClase / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean actualizarCfv (int consecutivoNuevo, int consecutivoViejo)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCfv, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,consecutivoNuevo);
			ps.setInt(2, consecutivoViejo);
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
				
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / actualizarCfv / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean poseeDetalles (int consecutivo)
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		boolean transaccionExitosa=false;
		
		logger.info("LA CONSULTA ES-------->"+poseeDetalles);
		logger.info("el consecutivo es-------->"+consecutivo);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(poseeDetalles, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,consecutivo);
			
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
			
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / poseeDetalles / "+e);
			return transaccionExitosa=true;
		}
		
		return transaccionExitosa;
	}
	
}