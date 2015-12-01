package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseValoresTipoReporteDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseValoresTipoReporteDao.class);
	
	
	/**
	 * 
	 */
	private static final String consultaInfoTipoReporteStr="SELECT " +
														   "tipo_rango as tiporango, " +
														   "unidad_medida as unidadmedida, " +
														   "posicion as posicion " +
														   "FROM reportes_rangos_estadisticos " +
														   "WHERE codigo=? ";
	
	
	/**
	 * 
	 */
	private static final String consultaParametrizacionRangosStr="SELECT " +
																 "codigo as codigo, " +
																 "orden as orden, " +
																 "nombre_etiqueta as nombreetiqueta, " +
																 "rango_inicial as rangoinicial, " +
																 "rango_final as rangofinal, " +
																 "activo as activo, " +
																 "'BD' as tiporegistro " +
																 "FROM rangos_estadisticos " +
																 "WHERE reporte=? order by orden";
	
	
	
	
	/**
	 * Cadena para la Eliminacion
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM rangos_estadisticos WHERE codigo=?";
	
	
	
	/**
	 * Cadena de modificacion, permite modificar codigo
	 * */ 
	private static final String cadenaModificacionTStr = "UPDATE rangos_estadisticos SET orden=?, nombre_etiqueta=?, rango_inicial=?, rango_final=?, activo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? ";
	
	
	
	/**
	 * cadena para la insercion
	 */
	private static final String cadenaInsertarStr="INSERT INTO rangos_estadisticos (codigo, reporte, orden, nombre_etiqueta, rango_inicial, rango_final, activo, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	public static HashMap consultarInfoTipoReporte(Connection con, String codigoReporte) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInfoTipoReporteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaInfoTipoReporteStr);
				logger.info("detalleFactura >>>>>>>>"+codigoReporte);
				
				ps.setObject(1, codigoReporte);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LOS PAQUETES FACTURADOS "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	public static HashMap consultarParametrizacion(Connection con, String codigoReporte) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionRangosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaParametrizacionRangosStr);
				logger.info("detalleFactura >>>>>>>>"+codigoReporte);
				
				ps.setObject(1, codigoReporte);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LOS PAQUETES FACTURADOS "+e);
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
	public static boolean eliminarRegistro(Connection con, String codigo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("CADENA >>>>>>>>>>>>"+cadenaEliminacionStr);
			logger.info("CODIGO >>>>>>>>>>>>"+codigo);
			
			ps.setString(1, codigo);
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
	 * @param vo
	 * @return
	 */
	public static boolean insertar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_rangos_estadisticos");
			
			/**
			 * INSERT INTO rangos_estadisticos (
			 * codigo, 
			 * reporte, 
			 * orden, 
			 * nombre_etiqueta, 
			 * rango_inicial, 
			 * rango_final, 
			 * activo, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("reporte")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("orden")+""));
			ps.setString(4, vo.get("nombre")+"");
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("rangoinicial")+""));
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("rangofinal")+""));
			ps.setString(7, vo.get("activo")+"");
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(9, vo.get("usuariomodifica")+"");
			ps.setDate(10, Date.valueOf(vo.get("fechamodifica")+""));
			ps.setString(11, vo.get("horamodifica")+""); 
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
	 * @param vo
	 * @return
	 */
	public static boolean modificar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionTStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE rangos_estadisticos SET 
			 * orden=?, 
			 * nombre_etiqueta=?, 
			 * rango_inicial=?, 
			 * rango_final=?, 
			 * activo=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?  
			 * WHERE codigo=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("orden")+""));
			ps.setString(2, vo.get("nombre")+"");
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("rangoinicial")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("rangofinal")+""));
			ps.setString(5, vo.get("activo")+"");
			ps.setString(6, vo.get("usuariomodifica")+"");
			ps.setDate(7, Date.valueOf(vo.get("fechamodifica")+""));
			ps.setString(8, vo.get("horamodifica")+"");
			ps.setDouble(9, Utilidades.convertirADouble(vo.get("codigo")+""));
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Método implementado para verificar si existe parametrizacion de los valores
	 * de un tipo de reporte específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existeParametrizacionValoresTipoReporte(Connection con,HashMap campos)
	{
		boolean existe = false;
		try
		{
			//********SE TOMAN LOS PARÁMETROS*************************************
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoTipoReporte = Utilidades.convertirAEntero(campos.get("codigoTipoReporte").toString());
			//********************************************************************
			
			String consulta = "SELECT count(1) AS cuenta " +
				"from rangos_estadisticos re " +
				"WHERE " +
				"re.reporte = "+codigoTipoReporte+" and " +
				"re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"'";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));;
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeParametrizacionValoresTipoReporte: "+e);
			existe = false;
		}
		return existe;
	}

	
	
}
