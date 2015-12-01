package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.MotivoCierreAperturaIngresos;



public class SqlBaseMotivoCierreAperturaIngresosDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseMotivoCierreAperturaIngresosDao.class);
	
	/**
	 * 
	 */
	private static String consultaStr="SELECT * " +
											"FROM " +
												"mot_cierre_apertura_ingresos " +
													"ORDER BY codigo ";
													
	/**
	 * 
	 */
	private static String insertarStr="INSERT INTO mot_cierre_apertura_ingresos values (?,?,?,?,?,?,CURRENT_DATE,substr('"+UtilidadFecha.getHoraActual()+"',1,5))";
	
	/**
	 * 
	 */
	private static String modificarStr="UPDATE mot_cierre_apertura_ingresos " +
										"SET codigo=?, " +
											"descripcion=?, " +
											"tipo=?, " +
											"activo=?, " +
											"institucion=?, " +
											"usuario_modifica=?, " +
											"fecha_modifica=CURRENT_DATE, " +
											"hora_modifica=substr('"+UtilidadFecha.getHoraActual()+
											"',1,5) " +
										"WHERE " +
											"codigo=?";
	/**
	 * 
	 */
	private static String eliminarStr="DELETE FROM mot_cierre_apertura_ingresos WHERE codigo=?";
	
	
	
	private static String [] indicesMap={"codigo_","descripcion_","tipo_","activo_","institucion_","usuario_modifica_","fecha_modifica_","hora_modifica_","puedoEliminar_"};
	
	
	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public static boolean insertar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO mot_cierre_apertura_ingresos values (?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")
			 */
			
			ps.setString(1, motivoCierreAperturaIngresos.getCodigo());
			ps.setString(2, motivoCierreAperturaIngresos.getDescripcion());
			ps.setString(3, motivoCierreAperturaIngresos.getTipo());
			ps.setString(4, motivoCierreAperturaIngresos.getActivo());
			ps.setInt(5, motivoCierreAperturaIngresos.getInstitucion());
			ps.setString(6, motivoCierreAperturaIngresos.getUsuarioModifica());
			
			if(ps.executeUpdate()>0)
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
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public static boolean modificar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, motivoCierreAperturaIngresos.getCodigo());
			ps.setString(2, motivoCierreAperturaIngresos.getDescripcion());
			ps.setString(3, motivoCierreAperturaIngresos.getTipo());
			ps.setString(4, motivoCierreAperturaIngresos.getActivo());
			ps.setInt(5, motivoCierreAperturaIngresos.getInstitucion());
			ps.setString(6, motivoCierreAperturaIngresos.getUsuarioModifica());
			ps.setString(7, motivoCierreAperturaIngresos.getCodigom());
		
			if(ps.executeUpdate()>0)
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
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public static boolean eliminar(Connection con, String motivoCierreAperturaIngresos)
	{

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, motivoCierreAperturaIngresos);
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;			
	}
	
	/**
	 * Consulta si existen relaciones entre los motivos de los cierres/aperturas con los cierres de los ingresos
	 * 
	 * @param con
	 * @param codigoMotivoCierreAperturaIngresos
	 * @return
	 * @author jeilones
	 * @created 9/05/2013
	 */
	public static boolean puedoEliminar(Connection con, String codigoMotivoCierreAperturaIngresos)
	{
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			StringBuffer consultarMotivosCierre=new StringBuffer("SELECT count(mca.codigo) as total ")
					.append("FROM manejopaciente.mot_cierre_apertura_ingresos mca ")
					.append("inner join manejopaciente.cierre_ingresos ci ")
					.append("on (ci.motivo_cierre=mca.codigo or ci.motivo_apertura=mca.codigo) ")
					.append("WHERE mca.codigo=? ");
			ps= con.prepareStatement(consultarMotivosCierre.toString());
			ps.setString(1, codigoMotivoCierreAperturaIngresos);
	
			rs=ps.executeQuery();
			if(rs.next()){
				long total=rs.getLong("total");
				if(total==0){
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;			
	}
	
	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public static HashMap consultar(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos)
	{
		HashMap mapa = new HashMap();
		
		try{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero( mapa.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(mapa.get("codigo_"+w)+""))
				{
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			
			mapa.put("INDICES_MAPA",indicesMap);
			
		
		}
		
		catch(SQLException e)
			{
			e.printStackTrace();
			}
			
		
		return  mapa;
		
	}

	
	private static boolean puedoEliminar(String codigo)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=puedoEliminar(con, codigo);
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	
	
	
}