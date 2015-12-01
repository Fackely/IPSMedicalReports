package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public class SqlBaseImagenesBaseDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseImagenesBaseDao.class);
	
	private final static String sqlConsultarImagenesBase = "select codigo_pk as codigo_pk" +
												", nombre_imagen as nombre_imagen" +
												", institucion as institucion" +
												", imagen as imagen " +
												"from historiaclinica.imagenes_base";
	
	private final static String SqlConsultarCamposParametrizables = " SELECT MOSTRAR_MODIFICACION " +
						"FROM historiaclinica.campos_parametrizables " +
						"WHERE IMAGEN_A_ASOCIAR = ?";
	
	private final static String sqlInsertarImagenesBase = "insert into historiaclinica.imagenes_base (CODIGO_PK, " +
			"			INSTITUCION, NOMBRE_IMAGEN, IMAGEN, FECHA_MODIFICA, HORA_MODIFICA, USUARIO_MODIFICA) values " +
			"			(?,?,?,?,current_date,?,?)";
	
	private final static String sqlEliminarImagenBase = "delete from historiaclinica.imagenes_base where codigo_pk = ?";
	
	private final static String sqlActualizarImagenBase = "UPDATE historiaclinica.imagenes_base " +
						"SET INSTITUCION = ?, NOMBRE_IMAGEN = ?, IMAGEN = ?, FECHA_MODIFICA = current_date," +
						"HORA_MODIFICA = ?, USUARIO_MODIFICA = ? " +
						"WHERE codigo_pk = ?";
	
	public static ArrayList<DtoImagenBase> consultarImagenesBase(Connection connection) {
		ArrayList<DtoImagenBase> arreglo = new ArrayList<DtoImagenBase>();
		DtoImagenBase imagenBase;
		logger.info("sqlConsultarImagenesBase: "+sqlConsultarImagenesBase);
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try {
			ps = new PreparedStatementDecorator(connection.prepareStatement(sqlConsultarImagenesBase));

			rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {

				imagenBase = new DtoImagenBase();
				imagenBase.clean();
				imagenBase.setCodigoPk(rs.getInt("codigo_pk"));
				imagenBase.setNombreImagen(rs.getString("nombre_imagen"));
				imagenBase.setInstitucion(rs.getString("institucion"));
				imagenBase.setImagen(rs.getString("imagen"));

				//Se realiza cambio por tarea 150647 - Se verificar si cada elemento
				UtilidadBD.iniciarTransaccion(connection);
				if (eliminarImagenBase(connection, rs.getInt("codigo_pk")).equals(ConstantesBD.acronimoSi))
				{
					imagenBase.setEsEliminar(consultarCamposParametrizablez(connection, imagenBase.getCodigoPk()));
				}
				UtilidadBD.abortarTransaccionSinMensaje(connection);
				
				
				arreglo.add(imagenBase);
			}
		}
		catch (Exception e) {
			logger.error("Se ha generado un error consultando las imágenes base---->"+e);
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return arreglo;
	}
	
	public static String insertarImagenesBase(Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario) {
		logger.info("sqlInsertarImagenesBase: "+sqlInsertarImagenesBase);
		String respuesta =  ConstantesBD.acronimoNo;
		PreparedStatementDecorator ps= null;
		
		try {
			ps = new PreparedStatementDecorator(connection.prepareStatement(sqlInsertarImagenesBase));

			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection,"historiaclinica.seq_imagenes_base");
			
			ps.setInt(1, secuencia);
			ps.setInt(2, usuario.getCodigoInstitucionInt());
			ps.setString(3, dtoImagenBase.getNombreImagen());
			ps.setString(4, dtoImagenBase.getImagen());
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setString(6, usuario.getLoginUsuario());
		
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert == 0)
				respuesta = ConstantesBD.acronimoNo;
			else
				respuesta = ConstantesBD.acronimoSi;

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error Insertando la imágen base---->"+e);
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return respuesta;
	}
	
	public static String consultarCamposParametrizablez (Connection connection, int imagenAAsociar)
	{
		String respuesta = ConstantesBD.acronimoSi;
		logger.info("SqlConsultarCamposParametrizables: "+SqlConsultarCamposParametrizables);
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try {
			ps = new PreparedStatementDecorator(
					connection.prepareStatement(SqlConsultarCamposParametrizables));
			
			ps.setInt(1, imagenAAsociar);
			
			rs = new ResultSetDecorator(ps.executeQuery());

			while (rs.next()) {
				if(rs.getString(1).equals(ConstantesBD.acronimoNo))
					respuesta = ConstantesBD.acronimoNo;
			}
		}
		catch (Exception e) {
			logger.error("Se ha producido un error----->"+e);
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return respuesta;
	}
	
	public static String  eliminarImagenBase(Connection connection, int codigo_pk)
	{
		logger.info("sqlEliminarImagenBase: "+sqlEliminarImagenBase);
		String respuesta =  ConstantesBD.acronimoNo;
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try {
			 ps = new PreparedStatementDecorator(connection.prepareStatement(sqlEliminarImagenBase));

			ps.setInt(1, codigo_pk);
					
			int resultadoDelete = ps.executeUpdate();

			if (resultadoDelete == 0)
				respuesta = ConstantesBD.acronimoNo;
			else
				respuesta = ConstantesBD.acronimoSi;

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado una excepcion. Posiblemente no se afecto el flujo.");
			//e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return respuesta;
	}
	
	public static String actualizarImagenesBase(Connection connection, DtoImagenBase dtoImagenBase, UsuarioBasico usuario) {
		logger.info("sqlActualizarImagenBase: "+sqlActualizarImagenBase);
		String respuesta =  ConstantesBD.acronimoNo;
		PreparedStatementDecorator ps= null;
		try {
			ps = new PreparedStatementDecorator(connection.prepareStatement(sqlActualizarImagenBase));
			
			ps.setInt(1, usuario.getCodigoInstitucionInt());
			ps.setString(2, dtoImagenBase.getNombreImagen());
			ps.setString(3, dtoImagenBase.getImagen());
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setString(5, usuario.getLoginUsuario());
			ps.setInt(6, dtoImagenBase.getCodigoPk());
			logger.info("getCodigoPk..."+dtoImagenBase.getCodigoPk());
			int resultadoInsert = ps.executeUpdate();

			if (resultadoInsert == 0)
				respuesta = ConstantesBD.acronimoNo;
			else
				respuesta = ConstantesBD.acronimoSi;

		} catch (Exception e) {
			respuesta = ConstantesBD.acronimoNo;
			logger.error("Se ha generado un error Actualizando la imágen base---->"+e);
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		logger.info("respuesta..."+respuesta);
		return respuesta;
	}
}
