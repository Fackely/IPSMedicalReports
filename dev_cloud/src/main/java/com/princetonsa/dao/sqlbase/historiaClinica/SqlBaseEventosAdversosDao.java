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

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;



public class SqlBaseEventosAdversosDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseEventosAdversosDao.class);
	
	/**
	 * Consultar los eventos adversos
	 */
	private static String consultarStr ="SELECT " +
											"ea.codigo ||' - '|| ea.descripcion as descripcioncod,ea.codigopk, ea.codigo, ea.descripcion, ea.tipo, getintegridaddominio(ea.tipo) as desctipo, ea.clasificacion_evento, ce.nombre as descclasificacion " +
										"FROM " +
											"eventos_adversos ea " +
										"INNER JOIN " +
											"clasificaciones_eventos ce ON (ea.clasificacion_evento=ce.codigo) " +
										"WHERE " +
											"ea.institucion=? AND ea.activo=? " +
										"ORDER BY " +
											"ea.codigo ";
	
	/**
	 * Modificar evento adverso
	 */
	private static String modificarStr = "UPDATE " +
											"eventos_adversos " +
										"SET " +
											"codigo=?, descripcion=?, tipo=?, clasificacion_evento=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
										"WHERE " +
											"codigopk=? ";
	
	/**
	 * Ingresar evento adverso
	 */
	private static String ingresarStr = "INSERT INTO " +
											"eventos_adversos (codigo, institucion, descripcion, tipo, clasificacion_evento, usuario_modifica, fecha_modifica, hora_modifica, activo, codigopk) " +
										"VALUES " +
											"(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Eliminar evento adverso
	 */
	private static String sePuedeEliminarStr = "SELECT codigo FROM " +
											"registro_eventos_adversos " +
										"WHERE " +
											"evento_adverso=?";
											
	/**
	 * Eliminar evento adverso
	 */
	private static String inactivarStr = "UPDATE " +
											"eventos_adversos " +
										"SET " +
											"activo=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
										"WHERE " +
											"codigopk=?";
	
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, EventosAdversos mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
	
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(consultarStr));
			ps.setString(1, mundo.getInstitucion());
			ps.setString(2, ConstantesBD.acronimoSi);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
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
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++){
			if(sePuedeEliminar(con, Utilidades.convertirAEntero((mapa.get("codigopk_"+i).toString()))))
				mapa.put("sepuedeeliminar_"+i, ConstantesBD.acronimoSi);
			else
				mapa.put("sepuedeeliminar_"+i, ConstantesBD.acronimoNo);
			mapa.put("eliminado_"+i, ConstantesBD.acronimoNo);
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean ingresar(Connection con, EventosAdversos mundo){
		boolean operacionExitosa=false;
		PreparedStatementDecorator ps= null;
		try
		{
			if (!existeCodigoEventoAdverso(con, Utilidades.convertirAEntero(mundo.getInstitucion()), mundo.getEventosMap("codigo").toString(), ConstantesBD.codigoNuncaValido)){
				ps= new PreparedStatementDecorator(con.prepareStatement(ingresarStr));
				
				/**
				 * INSERT INTO eventos_adversos 
				 * (codigo, 
				 * institucion, 
				 * descripcion, 
				 * tipo, 
				 * clasificacion_evento, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica, 
				 * activo, 
				 * codigopk) VALUES (?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)
				 */
				
				ps.setString(1, mundo.getEventosMap("codigo").toString());
				ps.setInt(2, Utilidades.convertirAEntero(mundo.getInstitucion()));
				ps.setString(3, mundo.getEventosMap("descripcion").toString());
				ps.setString(4, mundo.getEventosMap("tipo").toString());
				ps.setInt(5, Utilidades.convertirAEntero(mundo.getEventosMap("clasificacion").toString()));
				ps.setString(6, mundo.getUsuario());
				ps.setString(7, ConstantesBD.acronimoSi);
				ps.setDouble(8, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_eventos_adversos")+""));
				if(ps.executeUpdate()>0)
					operacionExitosa = true;
			}
		}
		catch (SQLException e)
		{
			logger.warn("EL CODIGO YA EXISTE FAVOR VERIFICAR.-->"+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return operacionExitosa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean modificar(Connection con, EventosAdversos mundo){
		boolean operacionExitosa = false;
		PreparedStatementDecorator ps= null;
		try
		{
			if (!existeCodigoEventoAdverso(con, Utilidades.convertirAEntero(mundo.getInstitucion()), mundo.getEventosMap("codigo").toString(), Utilidades.convertirAEntero(mundo.getEventosMap("codigopk").toString()))){
				ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr));
				
				/**
				 * UPDATE eventos_adversos SET codigo=?, 
				 * descripcion=?, 
				 * tipo=?, 
				 * clasificacion_evento=?, 
				 * usuario_modifica=?, 
				 * fecha_modifica=CURRENT_DATE, 
				 * hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" 
				 * WHERE codigopk=?
				 */
				
				ps.setString(1, mundo.getEventosMap("codigo").toString());
				ps.setString(2, mundo.getEventosMap("descripcion").toString());
				ps.setString(3, mundo.getEventosMap("tipo").toString());
				ps.setInt(4, Utilidades.convertirAEntero(mundo.getEventosMap("clasificacion").toString()));
				ps.setString(5, mundo.getUsuario());
				ps.setDouble(6, Utilidades.convertirADouble(mundo.getEventosMap("codigopk").toString()));
				if(ps.executeUpdate()>0)
					operacionExitosa = true;
			}	
		}
		catch (SQLException e)
		{
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
		return operacionExitosa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigopk
	 * @return
	 */
	public static boolean sePuedeEliminar(Connection con, int codigopk){
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sePuedeEliminarStr));
			ps.setDouble(1, Utilidades.convertirADouble(codigopk+""));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return false;
		}
		catch (SQLException e)
		{
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
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigopk
	 * @return
	 */
	public static boolean inactivar(Connection con, EventosAdversos mundo){
		boolean operacionExitosa;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(inactivarStr));
			
			/**
			 * UPDATE eventos_adversos SET activo=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=CURRENT_DATE, 
			 * hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" 
			 * WHERE codigopk=?
			 */
			
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, mundo.getUsuario());
			ps.setDouble(3, Utilidades.convertirADouble(mundo.getEventosMap("codigopk").toString()));
			ps.executeUpdate();
			operacionExitosa = true;
		}
		catch (SQLException e)
		{
			operacionExitosa=false;
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
		return operacionExitosa;
	}
	
	public static boolean existeCodigoEventoAdverso(Connection con, int institucion, String codigo, int codigopk){		
		boolean operacionExitosa = false;
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigopk FROM eventos_adversos WHERE codigo='"+codigo+"' AND institucion="+institucion+" AND activo='"+ConstantesBD.acronimoSi+"'";
			if (codigopk != ConstantesBD.codigoNuncaValido)
				consulta += " AND codigopk!="+codigopk;
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		}
		catch (SQLException e)
		{
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
		return false;
	}
}