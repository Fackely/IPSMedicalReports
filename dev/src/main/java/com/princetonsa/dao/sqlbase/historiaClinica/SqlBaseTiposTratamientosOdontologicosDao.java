package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.TiposTratamientosOdontologicos;
import com.princetonsa.mundo.inventarios.SustitutosNoPos;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

public class SqlBaseTiposTratamientosOdontologicosDao
{
	private static Logger logger = Logger.getLogger(SqlBaseTiposTratamientosOdontologicosDao.class);
	
	private static String[] indicesMapP={"codigo_","nombre_","activo_","puedoEliminar_"};
	
	private static String consultaStrP="SELECT codigo, nombre, activo " +
										"FROM tipo_tratamiento_odo_inst " +
										"WHERE cod_institucion = ? ORDER BY nombre ";
	
	private static String eliminarStr="DELETE FROM tipo_tratamiento_odo_inst WHERE codigo = ?";
	
	private static String insertarStr="INSERT INTO tipo_tratamiento_odo_inst VALUES (?,?,?,?)";
	
	private static String modificarStr="UPDATE tipo_tratamiento_odo_inst " +
										"SET codigo=?, " +
											"activo=?, " +
											"nombre=? " +
										"WHERE " +
											"codigo=? ";
	
	
	/**
	 * Metodo de consulta de los tipos de Tratamientos Odontologicos
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap<String, Object> consultaTiposT (Connection con, int codigoInstitucion)
	{
		HashMap<String, Object> resultadosTTO = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst=null;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrP, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setInt(1, codigoInstitucion);
			
			resultadosTTO = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosTTO.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultadosTTO.get("codigo_"+w)+""))
					resultadosTTO.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				else
					resultadosTTO.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
			}
			
			resultadosTTO.put("INDICES",indicesMapP);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Tipos Tratamientos Odontologicos");
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return resultadosTTO;
	}
	
	public static boolean eliminar(Connection con, String tipoT)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStr));
			ps.setInt(1, Utilidades.convertirAEntero(tipoT));
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("NO SE PUEDE ELIMINAR, AQUE SE HACE LA VALIDACION DEL PUEDO ELIMINAR");
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return false;
	}

	
	private static boolean puedoEliminar(String tipoT)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=eliminar(con, tipoT);
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	public static boolean insertar(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos){
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr));			
			ps.setInt(1, Utilidades.convertirAEntero(tiposTratamientosOdontologicos.getCodigo()));
			ps.setInt(2, Utilidades.convertirAEntero(tiposTratamientosOdontologicos.getInstitucion()));
			if(tiposTratamientosOdontologicos.getActivo().equals("1")){
				ps.setBoolean(3, true);
			}
			else{
				ps.setBoolean(3, false);				
			}
			ps.setString(4, tiposTratamientosOdontologicos.getDescripcion());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return false;
	}
	
	public static boolean modificar(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos){
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr));			
			
			/**
			 * UPDATE tipo_tratamiento_odo_inst " +
										"SET codigo=?, " +
											"activo=?, " +
											"nombre=? " +
										"WHERE " +
											"codigo=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(tiposTratamientosOdontologicos.getCodigo()));
			if(tiposTratamientosOdontologicos.getActivo().equals("1")){
				ps.setBoolean(2, true);
			}
			else{
				ps.setBoolean(2, false);				
			}
			ps.setString(3, tiposTratamientosOdontologicos.getDescripcion());
			ps.setInt(4, Utilidades.convertirAEntero(tiposTratamientosOdontologicos.getCodigoAux()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return false;
	}
}