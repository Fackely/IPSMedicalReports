package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.mundo.manejoPaciente.MotivosSatisfaccionInsatisfaccion;



public class SqlBaseMotivosSatisfaccionInsatisfaccionDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseMotivoCierreAperturaIngresosDao.class);
	
	/**
	 * Consultar los motivos de satisfaccion e insatisfaccion segun la institucion
	 */
	private static String consultarStr ="SELECT " +
											"codigopk, codigo, descripcion, tipo, administracion.getintegridaddominio(tipo) as desctipo " +
										"FROM " +
											"mot_satisfaccion_pac " +
										"WHERE " +
											"institucion=? " +
										"ORDER BY " +
											"codigo ";
	
	/**
	 * Modificar motivo de satisfaccion e insatisfaccion
	 */
	private static String modificarStr = "UPDATE " +
											"mot_satisfaccion_pac " +
										"SET " +
											"codigo=?, descripcion=?, tipo=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica=substr('"+UtilidadFecha.getHoraActual()+"',1,5) " +
										"WHERE " +
											"codigopk=? ";
	
	/**
	 * Ingresar motivo de satisfaccion e insatisfaccion
	 */
	private static String ingresarStrPos = "INSERT INTO " +
											"mot_satisfaccion_pac (codigopk, codigo, institucion, descripcion, tipo, usuario_modifica, fecha_modifica, hora_modifica) " +
										"VALUES " +
											"(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
	
	private static String ingresarStrOra = "INSERT INTO " +
											"mot_satisfaccion_pac (codigopk, codigo, institucion, descripcion, tipo, usuario_modifica, fecha_modifica, hora_modifica) " +
										"VALUES " +
											"(?,?,?,?,?,?,CURRENT_DATE,substr(to_char(sysdate, 'hh:mm'),1,5))";
	
	/**
	 * Consultar si se puede eliminar el motivo
	 */
	private static String sePuedeEliminarStr = "SELECT codigo FROM " +
											"encuesta_calidad " +
										"WHERE " +
											"motivo_calificacion=?";
	
	/**
	 * Eliminar motivo de satisfaccion e insatisfaccion
	 */
	private static String eliminarStr = "DELETE FROM " +
											"mot_satisfaccion_pac " +
										"WHERE " +
											"codigopk=?";
											
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, MotivosSatisfaccionInsatisfaccion mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getInstitucion()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
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
	 * @param i 
	 * @return
	 */
	public static boolean ingresar(Connection con, MotivosSatisfaccionInsatisfaccion mundo, int tipoBD){
		boolean operacionExitosa=false;
		String ingresarStr = "";
		switch(tipoBD)
		{
		case DaoFactory.ORACLE:
			ingresarStr =  ingresarStrOra;
			break;
		case DaoFactory.POSTGRESQL:
			ingresarStr =  ingresarStrPos;
			break;
		default:
			break;
		}
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ingresarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
							"mot_satisfaccion_pac 
							(codigopk, codigo, institucion, descripcion, tipo, usuario_modifica, fecha_modifica, hora_modifica) " +
						"VALUES " +
							"(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") 
			 */
			
			ps.setDouble(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_mot_satisfaccion_pac"));
			ps.setString(2, mundo.getMotivosMap("codigo").toString());
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(4, mundo.getMotivosMap("descripcion").toString());
			ps.setString(5, mundo.getMotivosMap("tipo").toString());
			ps.setString(6, mundo.getUsuario());
			if(ps.executeUpdate()>0)
				operacionExitosa = true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return operacionExitosa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean modificar(Connection con, MotivosSatisfaccionInsatisfaccion mundo){
		boolean operacionExitosa = false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE " +
					"mot_satisfaccion_pac " +
				"SET " +
					"codigo=?, descripcion=?, tipo=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
				"WHERE " +
					"codigopk=?
			 */
			
			ps.setString(1, mundo.getMotivosMap("codigo").toString());
			ps.setString(2, mundo.getMotivosMap("descripcion").toString());
			ps.setString(3, mundo.getMotivosMap("tipo").toString());
			ps.setString(4, mundo.getUsuario());
			ps.setDouble(5, Utilidades.convertirADouble(mundo.getMotivosMap("codigopk").toString()));
			if(ps.executeUpdate()>0)
				operacionExitosa = true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
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
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sePuedeEliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigopk);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigopk
	 * @return
	 */
	public static boolean eliminar(Connection con, MotivosSatisfaccionInsatisfaccion mundo){
		boolean operacionExitosa;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(mundo.getMotivosMap("codigopk").toString()));
			
			logger.info("Sentencia SQL -> "+eliminarStr);
			logger.info("Codigo del motivo a eliminar ->"+mundo.getMotivosMap("codigopk").toString());
			
			ps.executeUpdate();
			operacionExitosa = true;
		}
		catch (SQLException e)
		{
			operacionExitosa=false;
			e.printStackTrace();
		}
		return operacionExitosa;
	}
}