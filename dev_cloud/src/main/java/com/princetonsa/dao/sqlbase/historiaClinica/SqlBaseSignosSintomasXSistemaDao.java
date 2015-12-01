/*
 * @(#)SqlBaseSignosSintomasXSistemaDao.java
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la los signos sintomas x sistema
 *
 * @version 1.0, Junio 1 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseSignosSintomasXSistemaDao 
{
 /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseSignosSintomasXSistemaDao.class);

	/**
	 * listado
	 */
	private static String listado=	"select sss.codigo, " +
									"sss.consecutivo_signos_sintomas AS consecutivosignosintoma, " +
									"sss.consecutivo_categoria_triage AS consecutivocategoriatriage, " +
									"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estabd,  " +
									"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS eseliminada, " +
									"CASE WHEN sss.codigo IN (SELECT signo_sintoma FROM triage) THEN " + ValoresPorDefecto.getValorTrueParaConsultas() + " ELSE " + ValoresPorDefecto.getValorFalseParaConsultas() + " END AS estautilizada,  "+
									"ss.descripcion AS descripcionsignosintoma," +
									"ct.nombre AS  descripcioncategoriatriage," +
									"'"+ConstantesBD.acronimoNo+"' AS modificando " +
									"from " +
									"signos_sintomas_x_sistema sss " +
									"INNER JOIN signos_sintomas ss ON (ss.consecutivo=sss.consecutivo_signos_sintomas) " +
									"INNER JOIN motivo_consulta_urg mcu ON (mcu.codigo=sss.cod_mot_consulta_urg) " +
									"INNER JOIN categorias_triage ct ON (ct.consecutivo = sss.consecutivo_categoria_triage) " +
									"WHERE " +
									"sss.cod_mot_consulta_urg=? " +
									"AND sss.institucion=? ORDER BY ss.descripcion  ";
	//@todo falta el order by
	
	/**
	 * eliminar
	 */
	private static String eliminarStr="DELETE FROM signos_sintomas_x_sistema WHERE codigo=?";
	
	/**
	 * existe registro
	 */
	private static String existeRegistroStr="SELECT codigo FROM signos_sintomas_x_sistema WHERE cod_mot_consulta_urg=? and consecutivo_signos_sintomas=? and consecutivo_categoria_triage=? and institucion=? and codigo<>?";
	
	/**
	 * modifica
	 */
	private static String modificarStr="UPDATE signos_sintomas_x_sistema SET cod_mot_consulta_urg=?, consecutivo_signos_sintomas=?, consecutivo_categoria_triage=?, institucion=?	WHERE codigo=?";	
		
	/**
	 * Consulta la info de los asocios x uvr 
	 * @param con
	 * @param codigoMotivoConsulta
	 * @param codigoInstitucion
	 * @param
	 * @return
	 */
	public static HashMap listado(	Connection con, 
	        						String codigoMotivoConsulta,
	        						int codigoInstitucion
								 )
	{
		PreparedStatementDecorator cargarStatement= null;
	    try
		{
	        if(codigoMotivoConsulta.trim().equals(""))
	        	codigoMotivoConsulta=""+ConstantesBD.codigoNuncaValido;
	        logger.info("LISTADO-->"+listado+"  --> "+codigoMotivoConsulta);
			cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listado));
			cargarStatement.setDouble(1, Utilidades.convertirADouble(codigoMotivoConsulta));
			cargarStatement.setInt(2, codigoInstitucion);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la listado: SqlBaseSignosSintomaXSistemaDao "+e.toString());
			return new HashMap();
		}finally{
			try {
				if(cargarStatement!=null){
					cargarStatement.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public static int eliminar (Connection con, String codigoPK)
	{
		PreparedStatementDecorator ps=  null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarStr));
			ps.setDouble(1, Utilidades.convertirADouble(codigoPK));
			return ps.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el eliminar codigo:"+codigoPK+" SqlBaseSignosSintomaXSistemaDao "+e.toString());
			return Utilidades.convertirAEntero(e.getSQLState());
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * evalua si existe un registro unique o no
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param codigoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @param codigoPK
	 * @return
	 */
	public static boolean existeRegistro(Connection con,
										 String codigoConsultaMotivo,
										 String codigoSignoSintoma,
										 String consecutivoCalificacionTriage,
										 int codigoInstitucion,
										 String codigoPK)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			 ps=  new PreparedStatementDecorator(con.prepareStatement(existeRegistroStr));
			
			/**
			 * SELECT codigo FROM signos_sintomas_x_sistema 
			 * WHERE 
			 * cod_mot_consulta_urg=? 
			 * and consecutivo_signos_sintomas=? 
			 * and consecutivo_categoria_triage=? 
			 * and institucion=? 
			 * and codigo<>?
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoConsultaMotivo));
			ps.setDouble(2, Utilidades.convertirADouble(codigoSignoSintoma));
			ps.setDouble(3, Utilidades.convertirADouble(consecutivoCalificacionTriage));
			ps.setInt(4, codigoInstitucion);
			ps.setDouble(5, Utilidades.convertirADouble(codigoPK));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el eliminar codigo:"+codigoPK+" SqlBaseSignosSintomaXSistemaDao "+e.toString());
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		return false;
	}
	
	/**
	 * modifica
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public static int modificar (	Connection con, 
									String codigoConsultaMotivo,
									String codigoSignoSintoma,
									String consecutivoCalificacionTriage,
									int codigoInstitucion,
									String codigoPK)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(modificarStr));
			
			/**
			 * UPDATE signos_sintomas_x_sistema SET 
			 * cod_mot_consulta_urg=?, 
			 * consecutivo_signos_sintomas=?, 
			 * consecutivo_categoria_triage=?, 
			 * institucion=?	
			 * WHERE codigo=?
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoConsultaMotivo));
			ps.setDouble(2, Utilidades.convertirADouble(codigoSignoSintoma));
			ps.setDouble(3, Utilidades.convertirADouble(consecutivoCalificacionTriage));
			ps.setInt(4, codigoInstitucion);
			ps.setDouble(5, Utilidades.convertirADouble(codigoPK));
			return ps.executeUpdate();
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el eliminar codigo:"+codigoPK+" SqlBaseSignosSintomaXSistemaDao "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * inserta
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param codigoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @param insertarSql
	 * @return
	 */
	public static boolean insertar (	Connection con, 
										String codigoConsultaMotivo,
										String codigoSignoSintoma,
										String consecutivoCalificacionTriage,
										int codigoInstitucion,
										String insertarSql)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(insertarSql));
			
			/**
			 * insert into signos_sintomas_x_sistema (
			 * codigo, 
			 * cod_mot_consulta_urg, 
			 * consecutivo_signos_sintomas, 
			 * consecutivo_categoria_triage, 
			 * institucion) 
			 * values('seq_signos_sintomas_sist'), ?, ?, ?, ?) 
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoConsultaMotivo));
			ps.setDouble(2, Utilidades.convertirADouble(codigoSignoSintoma));
			ps.setDouble(3, Utilidades.convertirADouble(consecutivoCalificacionTriage));
			ps.setInt(4, codigoInstitucion);
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el insertar SqlBaseSignosSintomaXSistemaDao "+e.toString());
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseServiciosXTipoTratamientoOdontologicoDao "+sqlException.toString() );
			}
		}
		return false;
	}
	
	
}
