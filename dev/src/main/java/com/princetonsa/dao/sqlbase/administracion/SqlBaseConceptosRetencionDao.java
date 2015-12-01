package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;


public class SqlBaseConceptosRetencionDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConceptosRetencionDao.class);
	
	
	private static String consultaTiposRetencion="SELECT consecutivo, " +
													"codigo, " +
													"institucion AS codigoInstitucion, " +
													"descripcion, " +
													"sigla, " +
													"codigo_interfaz AS codigoInterfaz, " +
													"activo, " +
													"usuario_modifica AS usuarioModificacion, " +
													"usuario_inactivacion AS usuarioAnulacion " +
													"FROM administracion.tipos_retencion " +
													"WHERE institucion = ? AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private static String consultarConceptosRetencion="SELECT c.consecutivo AS consecutivo, " +
														"c.tipo_retencion AS consecutivoTipoRet, " +
														"t.codigo AS tipoRet, " +
														"t.descripcion AS descTipo, " +
														"t.sigla AS sigla, " +
														"t.codigo_interfaz AS codInterfazTipo, " +
														"c.codigo_concepto AS codConcepto, " +
														"c.descripcion_concepto AS descripcion, " +
														"c.codigo_interfaz AS codInterfaz, " +
														"c.cuenta_retencion AS cuentaRet, " +
														"c.cuenta_db_autoretencion AS cuentaDB, " +
														"c.cuenta_cr_autoretencion AS cuentaCR, " +
														"c.activo AS activo " +
														"FROM administracion.conceptos_retencion c " +
														"INNER JOIN administracion.tipos_retencion t ON(c.tipo_retencion=t.consecutivo) ";
														
	
	private static String insertarConceptoRetencion="INSERT INTO administracion.conceptos_retencion " +
														"(consecutivo, tipo_retencion, codigo_concepto, " +
														"institucion, descripcion_concepto, codigo_interfaz, " +
														"cuenta_retencion,cuenta_db_autoretencion, " +
														"cuenta_cr_autoretencion, activo, fecha_modifica, " +
														"hora_modifica, usuario_modifica) VALUES" +
														"(?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,?,?)";
	
	private static String insertarLogConceptoRetencion="INSERT INTO administracion.log_conceptos_retencion " +
														"(consecutivo, concepto_retencion, tipo_retencion, " +
														"codigo_concepto, descripcion_concepto, codigo_interfaz, " +
														"cuenta_retencion, cuenta_db_autoretencion, cuenta_cr_autoretencion, " +
														"fecha_modifica, hora_modifica, usuario_modifica) VALUES " +
														"(?,?,?,?,?,?,?,?,?,CURRENT_DATE,?,?)";

	private static String actualizarConceptoRetencion="UPDATE administracion.conceptos_retencion SET " +
														"tipo_retencion=?, " +
														"codigo_concepto=?, " +
														"descripcion_concepto=?, " +
														"codigo_interfaz=?, " +
														"cuenta_retencion=?, " +
														"cuenta_db_autoretencion=?, " +
														"cuenta_cr_autoretencion=?, " +
														"fecha_modifica=CURRENT_DATE, " +
														"hora_modifica=?, " +
														"usuario_modifica=? " +
														"WHERE consecutivo=? ";
	
	private static String consultarDetalleConcepto="SELECT count(consecutivo) AS numreg " +
													"FROM administracion.det_conceptos_retencion " +
													"WHERE tipo_retencion=? ";

	private static String consultarVigDetConcepto="SELECT count(consecutivo) AS numreg " +
													"FROM administracion.det_vig_con_retencion " +
													"WHERE concepto_retencion=? ";
	
	private static String eliminarConceptoRetencion="UPDATE administracion.conceptos_retencion " +
													"SET activo=?, " +
													"fecha_inactivacion= CURRENT_DATE, " +
													"hora_inactivacion=?, " +
													"usuario_inactivacion=? " +
													"WHERE consecutivo=? ";
	
	private static String consultarCuentaContable="SELECT " +
													"interfaz.getdesccuentascontables(codigo) AS descCuenta " +
													"FROM interfaz.cuentas_contables " +
													"WHERE codigo=?";
	

	public static String consultarCuentaContable(int cuenta) 
	{
		String descCuenta="";
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarCuentaContable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuenta);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				descCuenta= rs.getString("descCuenta");
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO CUENTAS CONTABLES.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return descCuenta;
	}
	
	public static int consultarVigDetConcepto(int consecutivo) 
	{		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		int cont=0;
		try
		{		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarVigDetConcepto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, consecutivo);	
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nconsulta::::::: "+consultarVigDetConcepto+"  consecutivo::: "+consecutivo);
						
			if(rs.next()){
				cont = rs.getInt("numreg");
			}
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO VIGENCIA DETALLE CONCEPTOS.------>>>>>>"+e);
			e.printStackTrace();
		}			
		return cont;
	}	
	
	public static boolean eliminarConceptoRetencion(String usuario, int consecutivo)
	{			
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setString(1,ConstantesBD.acronimoNo);
			ps.setString(2,UtilidadFecha.getHoraActual());
			ps.setString(3,usuario);
			ps.setInt(4,consecutivo);

			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
				
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR ELIMINANDO CONCEPTO "+e);		
		}
		
		return false;
	}
	
	
	public static int consultarDetalleConcepto(int tipoRet) 
	{
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		int cont=0;
		try
		{		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarDetalleConcepto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, tipoRet);	
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nconsulta::::::: "+consultarDetalleConcepto+"  tipo ret:::: "+tipoRet);
						
			if(rs.next()){
				cont = rs.getInt("numreg");
			}
			ps.close();
			rs.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO DETALLE CONCEPTOS.------>>>>>>"+e);
			e.printStackTrace();
		}			
		return cont;
	}	
	
	public static boolean actualizarConceptoRetencion(int tipoRet, String codConcepto, String descConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario, int consecutivo)
	{			
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarConceptoRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
			ps.setInt(1,tipoRet);
			ps.setString(2,codConcepto);
			ps.setString(3,descConcepto);
			if(codInterfaz.equals(""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4,codInterfaz);
			if(cuentaRet.equals(""))
				ps.setNull(5, Types.VARCHAR);
			else
				ps.setString(5,cuentaRet);
			if(cuentaDB.equals(""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6,cuentaDB);
			if(cuentaCR.equals(""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7,cuentaCR);
			ps.setString(8,UtilidadFecha.getHoraActual());
			ps.setString(9,usuario);
			ps.setInt(10,consecutivo);						

			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
				
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR ACTUALIZANDO CONCEPTOS "+e);		
		}
		
		return false;
	}
	

	public static boolean insertarLogConceptoRetencion(int conceptoRet, int tipoRet, String codConcepto, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario)
	{

		Connection con;		
		con= UtilidadBD.abrirConexion();
				
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarLogConceptoRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_log_conceptos_retencion"));
			ps.setInt(2,conceptoRet);
			ps.setInt(3,tipoRet);
			ps.setString(4,codConcepto);
			ps.setString(5,desConcepto);
			if (codInterfaz.equals(""))
				ps.setNull(6, Types.VARCHAR);					
			else
				ps.setString(6, codInterfaz);
			if (cuentaRet.equals(""))
				ps.setNull(7, Types.VARCHAR);					
			else
				ps.setString(7, cuentaRet);
			if (cuentaDB.equals(""))
				ps.setNull(8, Types.VARCHAR);					
			else
				ps.setString(8, cuentaDB);
			if (cuentaCR.equals(""))
				ps.setNull(9, Types.VARCHAR);					
			else
				ps.setString(9, cuentaCR);
			ps.setString(10,UtilidadFecha.getHoraActual());
			ps.setString(11,usuario);
			
			
			if(ps.executeUpdate()>0)
			{	
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
							
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR INGRESAR LOG CONCEPTO---> "+e);
		}		
		return false;
	}
	
	public static boolean insertarConceptoRetencion(int tipoRet, String codConcepto, int institucion, String desConcepto, 
							String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario)
	{
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarConceptoRetencion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_conceptos_retencion"));
				ps.setInt(2,tipoRet);
				ps.setString(3,codConcepto);
				ps.setInt(4,institucion);
				ps.setString(5,desConcepto);
				if (codInterfaz.equals(""))
					ps.setNull(6, Types.VARCHAR);					
				else
					ps.setString(6, codInterfaz);
				if (cuentaRet.equals(""))
					ps.setNull(7, Types.VARCHAR);					
				else
					ps.setString(7, cuentaRet);
				if (cuentaDB.equals(""))
					ps.setNull(8, Types.VARCHAR);					
				else
					ps.setString(8, cuentaDB);
				if (cuentaCR.equals(""))
					ps.setNull(9, Types.VARCHAR);					
				else
					ps.setString(9, cuentaCR);
				ps.setString(10, ConstantesBD.acronimoSi);
				ps.setString(11,UtilidadFecha.getHoraActual());
				ps.setString(12,usuario);

				
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return true;
				}
				ps.close();
								
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR INGRESAR CONCEPTO---> "+e);
			}		
		return false;
	}

	public static ArrayList<DtoConceptosRetencion> consultarConceptosRetencion(int institucion, int tipoRetencion) 
	{
		ArrayList<DtoConceptosRetencion> listaDtoConceptosRetencion= new ArrayList<DtoConceptosRetencion>();
		String consulta=consultarConceptosRetencion;
		
		consulta+=" WHERE ";
		
		if (tipoRetencion>0)
			consulta+=" c.tipo_retencion="+tipoRetencion+" AND ";
		
		consulta+=" c.institucion = ? AND c.activo = '"+ConstantesBD.acronimoSi+"' " +
					"ORDER BY descripcion DESC";
		
		logger.info("LA CONSULTA ---->"+consulta);
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, institucion);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nconsulta::::::: "+consultarConceptosRetencion);
			
			while(rs.next())
			{
				DtoConceptosRetencion dto= new DtoConceptosRetencion();
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setTipoRetencion(rs.getString("tipoRet"));
				dto.setTipoRetencionDesc(rs.getString("descTipo"));
				dto.setTipoRetencionSigla(rs.getString("sigla"));
				dto.setTipoRetencionConsecutivo(rs.getInt("consecutivoTipoRet"));
				dto.setTipoRetencionInterfaz(rs.getString("codInterfazTipo"));
				dto.setCodigoConcepto(rs.getString("codConcepto"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setCodigoInterfaz(rs.getString("codInterfaz"));
				dto.setCuentaRet(rs.getString("cuentaRet"));
				dto.setCuentaDBautoRet(rs.getString("cuentaDB"));
				dto.setCuentaCRautoRet(rs.getString("cuentaCR"));
				dto.setActivo(rs.getString("activo"));				
				listaDtoConceptosRetencion.add(dto);
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO CONCEPTOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoConceptosRetencion;
	}
	
	
	public static ArrayList<DtoTiposRetencion> consultaTiposRetencion(int institucion)
	{
		ArrayList<DtoTiposRetencion> listaDtoTiposRetencion= new ArrayList<DtoTiposRetencion>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try
		{ 
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTiposRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, institucion);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoTiposRetencion dto= new DtoTiposRetencion();
				dto.setCodigo(rs.getString("codigo"));
				dto.setConsecutivo(rs.getInt("consecutivo"));
				dto.setCodigoInstitucion(rs.getInt("codigoInstitucion"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setSigla(rs.getString("sigla"));
				dto.setCodigoInterfaz(rs.getString("codigoInterfaz"));
				dto.setActivo(rs.getString("activo"));
				dto.setUsuarioModificacion(rs.getString("usuarioModificacion"));
				dto.setUsuarioAnulacion(rs.getString("usuarioAnulacion"));
				listaDtoTiposRetencion.add(dto);
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO TIPOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoTiposRetencion;
	}
}