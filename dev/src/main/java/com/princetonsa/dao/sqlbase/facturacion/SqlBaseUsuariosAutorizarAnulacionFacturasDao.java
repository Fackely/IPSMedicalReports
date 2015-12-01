package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class SqlBaseUsuariosAutorizarAnulacionFacturasDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseUsuariosAutorizarAnulacionFacturasDao.class);
	
	
	private static final String consultaUsuariosAutorizadosStr="SELECT uf.codigo as codigo, " +
															   "uf.usuario as loginusuario, " +
															   "getnombrepersona(u.codigo_persona) as nombrepersona " +
															   "FROM usu_autorizan_anul_fac uf " +
															   "INNER JOIN usuarios u ON(u.login=uf.usuario) " +
															   "WHERE uf.centro_atencion=? AND uf.institucion=? order by nombrepersona ";
	
	
	/**
	 * cadena para la insercion
	 */
	private static final String cadenaInsertarStr="INSERT INTO usu_autorizan_anul_fac (codigo, centro_atencion, usuario, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * Cadena para la Eliminacion
	 */
	private static final String cadenaEliminacionStr="DELETE FROM usu_autorizan_anul_fac WHERE codigo=?";
	
	
	/**
	 * Cadena de modificacion, permite modificar codigo
	 * */ 
	private static final String cadenaModificacionStr = "UPDATE usu_autorizan_anul_fac SET usuario=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? ";
	
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarUsuariosAutorizados(Connection con, String centroAtencion, String codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaUsuariosAutorizadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaUsuariosAutorizadosStr);
				logger.info("centroAtencion >>>>>>>>"+centroAtencion);
				logger.info("codigoInstitucion >>>>>>>>"+codigoInstitucion);
				
				ps.setInt(1, Utilidades.convertirAEntero(centroAtencion));
				ps.setInt(2, Utilidades.convertirAEntero(codigoInstitucion));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE USUARIOS AUTORIZADOS PARA AUTORIZAR "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param usuarioAutorizado
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean insertarUsuarios(Connection con, String centroAtencion, String usuarioAutorizado, String codigoInstitucion, String loginUsuario) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_usu_autorizan_anul_fac");
			
			/**
			 * INSERT INTO usu_autorizan_anul_fac (
			 * codigo, 
			 * centro_atencion, 
			 * usuario, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setInt(2, Utilidades.convertirAEntero(centroAtencion));
			ps.setString(3, usuarioAutorizado);
			ps.setInt(4, Utilidades.convertirAEntero(codigoInstitucion));
			ps.setString(5, loginUsuario);
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			return ps.executeUpdate()>0;
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
	 * @param codigoPk
	 * @return
	 */
	public static boolean eliminarUsuario(Connection con, int codigoPk) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("CADENA >>>>>>>>>>>>"+cadenaEliminacionStr);
			logger.info("CODIGO >>>>>>>>>>>>"+codigoPk);
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoPk+""));
			return ps.executeUpdate()>0;
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
	 * @param codigoPk
	 * @param usuarioAutorizado
	 * @return
	 */
	public static boolean modificarUsuario(Connection con, int codigoPk, String usuarioAutorizado, String loginUsuario) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE usu_autorizan_anul_fac SET usuario=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? 
			 */
			
			ps.setString(1, usuarioAutorizado);
			ps.setString(2, loginUsuario);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setDouble(5, Utilidades.convertirADouble(codigoPk+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	
}
