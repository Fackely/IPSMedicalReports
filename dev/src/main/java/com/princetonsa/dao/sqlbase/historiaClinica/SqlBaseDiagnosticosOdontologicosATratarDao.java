package com.princetonsa.dao.sqlbase.historiaClinica;


import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;




public class SqlBaseDiagnosticosOdontologicosATratarDao
{
	/**
	 *Mensajes de error 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDiagnosticosOdontologicosATratarDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsulta="SELECT codigo as codigo,activo as activo, " +
			"cod_institucion as institucion,num_fotograma as numfotograma, " +
			"nombre as nombre,coalesce(acronimo,'') as acronimo, " +
			"carta_dental as cartadental,'BD' as tiporegistro from est_sec_diente_inst where cod_institucion=? order by nombre";
	
	/**
	 * 
	 */
	private static String cadenaConsultaEspecifico="SELECT codigo as codigo,activo as activo,cod_institucion as institucion,num_fotograma as numfotograma,nombre as nombre,coalesce(acronimo,'') as acronimo,carta_dental as cartadental,'BD' as tiporegistro from est_sec_diente_inst where codigo=?";
	
	/**
	 * 
	 */
	private static String cadenaInsertar="INSERT INTO est_sec_diente_inst(codigo,activo,cod_institucion,num_fotograma,nombre,acronimo,carta_dental,usuario_modifica,fecha_modifica,hora_modifica)values(?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 
	 */
	private static String cadenaUpdate="UPDATE est_sec_diente_inst SET activo=?,cod_institucion=?,num_fotograma=?,nombre=?,acronimo=?,carta_dental=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";
	/**
	 * 
	 * @param institucion
	 */
	public static HashMap consultarDiagnosticosATratar(int institucion) 
	{
		Connection con=UtilidadBD.abrirConexion();
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try
		{
			logger.info("consulta -->"+cadenaConsulta);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta));
			ps.setInt(1, institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.info("error -->"+e.getMessage());
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
		}
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarDiagnosticosATratarEspecifico(Connection con, int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEspecifico));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.info("error -->"+e.getMessage());
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertar(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps=  null;
		try
		{
			Utilidades.imprimirMapa(vo);
			 ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar));
			
			/**
			 * INSERT INTO est_sec_diente_inst(
			 * codigo,
			 * activo,
			 * cod_institucion,
			 * num_fotograma,
			 * nombre,
			 * acronimo,
			 * carta_dental,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * values(?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_est_sec_diente_inst"));
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
			{
				ps.setObject(2, null);
			}
			else
			{
				ps.setBoolean(2, UtilidadTexto.getBoolean(vo.get("activo")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("institucion")+""))
			{
				ps.setNull(3, Types.INTEGER);
			}
			else
			{
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("institucion")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("numfotograma")+""))
			{
				ps.setNull(4, Types.INTEGER);
			}
			else
			{
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("numfotograma")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("nombre")+""))
			{
				ps.setNull(5, Types.VARCHAR);
			}
			else
			{
				ps.setString(5, vo.get("nombre")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("acronimo")+""))
			{
				ps.setNull(6, Types.VARCHAR);
			}
			else
			{
				ps.setString(6, vo.get("acronimo")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("cartadental")+""))
			{
				ps.setString(7, ConstantesBD.acronimoNo);
			}
			else
			{
				ps.setString(7, vo.get("cartadental")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("usuariomodifica")+""))
			{
				ps.setNull(8, Types.VARCHAR);
			}
			else
			{
				ps.setString(8, vo.get("usuariomodifica")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("fechamodifica")+""))
			{
				ps.setNull(9, Types.DATE);
			}
			else
			{
				ps.setDate(9, Date.valueOf(vo.get("fechamodifica")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("horamodifica")+""))
			{
				ps.setNull(10, Types.VARCHAR);
			}
			else
			{
				ps.setString(10, vo.get("horamodifica")+"");
			}
			

			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.info("error -->"+e.getMessage());
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
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
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdate));
			
			/**
			 * UPDATE est_sec_diente_inst SET 
			 * activo=?,
			 * cod_institucion=?,
			 * num_fotograma=?,
			 * nombre=?,
			 * acronimo=?,
			 * carta_dental=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
	         * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
			{
				ps.setObject(1, null);
			}
			else
			{
				ps.setBoolean(1, UtilidadTexto.getBoolean(vo.get("activo")+""));
			}
			if(UtilidadTexto.isEmpty(vo.get("institucion")+""))
			{
				ps.setNull(2, Types.INTEGER);
			}
			else
			{
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			}
			if(UtilidadTexto.isEmpty(vo.get("numfotograma")+""))
			{
				ps.setNull(3, Types.INTEGER);
			}
			else
			{
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("numfotograma")+""));
			}
			if(UtilidadTexto.isEmpty(vo.get("nombre")+""))
			{
				ps.setNull(4, Types.VARCHAR);
			}
			else
			{
				ps.setString(4, vo.get("nombre")+"");
			}
			if(UtilidadTexto.isEmpty(vo.get("acronimo")+""))
			{
				ps.setNull(5, Types.VARCHAR);
			}
			else
			{
				ps.setString(5, vo.get("acronimo")+"");
			}
			if(UtilidadTexto.isEmpty(vo.get("cartadental")+""))
			{
				ps.setString(6, ConstantesBD.acronimoNo);
			}
			else
			{
				ps.setString(6, vo.get("cartadental")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("usuariomodifica")+""))
			{
				ps.setNull(7, Types.VARCHAR);
			}
			else
			{
				ps.setString(7, vo.get("usuariomodifica")+"");
			}
			
			if(UtilidadTexto.isEmpty(vo.get("fechamodifica")+""))
			{
				ps.setNull(8, Types.DATE);
			}
			else
			{
				ps.setDate(8, Date.valueOf(vo.get("fechamodifica")+""));
			}
			
			if(UtilidadTexto.isEmpty(vo.get("horamodifica")+""))
			{
				ps.setNull(9, Types.VARCHAR);
			}
			else
			{
				ps.setString(9, vo.get("horamodifica")+"");
			}
			
			
			ps.setInt(10, Utilidades.convertirAEntero(vo.get("codigo")+""));
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.info("error -->"+e.getMessage());
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseDiagnosticosOdontologicosATratarDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	
}