/*
 * SqlBaseExcepcionesNaturalezaPaciente.java 
 * Autor			:  mdiaz
 * Creado el	:  17-ago-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.sqlbase;


import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 17-ago-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class SqlBaseExcepcionesNaturalezaPacienteDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseExcepcionesNaturalezaPacienteDao.class);
	
	public static final String insertQuery = "INSERT INTO excepciones_naturaleza (codigo, acr_regimen, cod_naturaleza, activo) VALUES ";
	
	private static final String updateQuery = "UPDATE excepciones_naturaleza set acr_regimen = ?, cod_naturaleza = ?, activo = ? WHERE codigo = ? ";
	
	private static final String deleteQuery = "DELETE FROM excepciones_naturaleza WHERE codigo = ? "; 
	
	private static final String buscarExcepcionStr="SELECT activo AS tieneExcepcion FROM excepciones_naturaleza WHERE acr_regimen=? AND cod_naturaleza=?";
  
	public static Connection refreshDBConnection(Connection con){
		
		try{
		  if (con == null || con.isClosed()){
			  DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			  con = myFactory.getConnection();
		  }
		}
		catch(SQLException e){
				logger.warn(" Error al intentar reabrir la conexion con la base de datos [Clase: SqlBaseExcepcionesNaturalezaPaciente ]" + e);
		}
    
		return con;		 		
	}
	
	
	
	public static int modificar(Connection con, int codigo, String codigoRegimen, int codigoNaturaleza, boolean indicativoExcepcion){
		int resultado = 0; 		
		
		try
		{
			con = refreshDBConnection(con);
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(updateQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificar.setString   (1, codigoRegimen);
			modificar.setInt        (2, codigoNaturaleza);
			modificar.setBoolean(3, indicativoExcepcion);
			modificar.setInt    (4, codigo);
			resultado = modificar.executeUpdate();
			if( resultado <= 0 ){
				logger.error("Error modificando Excepciones Naturaleza Paciente [ Tabla: excepciones_naturaleza ] --> ningún registro fue modificado ");
			}
		}
		catch (SQLException e){
			logger.error("Error modificando Excepciones Naturaleza Paciente [ Tabla: excepciones_naturaleza ] " + codigo + ": " + e);
		}
	  
		return resultado;
	}
		
	
	public static int eliminar(Connection con, int codigo){
		int resultado = 0; 		
		
		try
		{
			con = refreshDBConnection(con);
			PreparedStatementDecorator eliminar =  new PreparedStatementDecorator(con.prepareStatement(deleteQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminar.setInt(1, codigo);
			resultado = eliminar.executeUpdate();
			if( resultado <= 0 ){
				logger.error("Error eliminando Excepciones Naturaleza Paciente [ Tabla: excepciones_naturaleza ] --> ningún registro fue eliminado ");
			}
		}
		catch (SQLException e){
			logger.error("Error modificando Excepciones Naturaleza Paciente [ Tabla: excepciones_naturaleza ] " + codigo + ": " + e);
		}
	  
		return resultado;
	}

	
	
	public static HashMap buscar(Connection con, String[] selectedColumns, String where, String orderBy){
		PreparedStatementDecorator busqueda;
    HashMap lhmap = null;
		String selectQuery = "";
		int k;
		
	  try
		{
			con = refreshDBConnection(con);
			selectQuery = "SELECT ";
			for(k=0; k<(selectedColumns.length-1); k++){
				selectQuery += selectedColumns[k] + ", ";
			}
			selectQuery  += selectedColumns[k];
			selectQuery = selectQuery +" FROM vista_excepciones_naturaleza " +(( ( where.equals("") )? " " : " WHERE " +where)  +(( ( orderBy.equals("") )? " " : " ORDER BY ( " +orderBy + ") ")));

			
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(selectQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lhmap = UtilidadBD.resultSet2HashMap(selectedColumns, new ResultSetDecorator(busqueda.executeQuery()), false, false).getMapa();
		}
		catch (Exception e)
		{
			logger.error("Error realizando la búsqueda de Excepciones Naturaleza Paciente [ Vista: vista_excepciones_naturaleza | Tabla: excepciones_naturaleza ]: ");
			logger.error(selectQuery);
			logger.error(e);
			lhmap = null;
		}
    
		return lhmap;
	}


	public static HashMap buscar(Connection con, int codigo){
		PreparedStatementDecorator busqueda;
    HashMap lhmap = null;
		ResultSetDecorator rs = null;
    String selectQuery = "";
		
	  try
		{
			con = refreshDBConnection(con);
			selectQuery = "SELECT codigo, acr_regimen, nom_regimen, cod_naturaleza, nom_naturaleza, activo FROM vista_excepciones_naturaleza WHERE codigo = ?";
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(selectQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			busqueda.setInt(1, codigo );
			rs = new ResultSetDecorator(busqueda.executeQuery());
		
			if(rs.next() == true){
				lhmap = new HashMap();
				lhmap.put("codigo", rs.getString("codigo"));
				lhmap.put("acr_regimen", rs.getString("acr_regimen"));
				lhmap.put("nom_regimen", rs.getString("nom_regimen"));
				lhmap.put("cod_naturaleza", rs.getString("cod_naturaleza"));
				lhmap.put("nom_naturaleza", rs.getString("nom_naturaleza"));
				lhmap.put("indicativo_excepcion", rs.getString("activo"));				
			}
		}
		catch (Exception e)
		{
			logger.error("Error realizando la búsqueda de Excepciones Naturaleza Paciente [ Vista: vista_excepciones_naturaleza | Tabla: excepciones_naturaleza ]: ");
			logger.error(selectQuery);
			logger.error(e);
			lhmap = null;
		}
   
		return lhmap;
	}

	
	/*
	 * *************************************** OJO *********************************************
	 * @author Juan David Ramírez López
	 *
	 * Princeton S.A.
	 * 
	 * Este método es utilizado por facturación, favor consultar para su modificación
	 * Atte: Juan David
	 */
	/**
	 * Método para buscar la excepcion de farmacia de acuerdo
	 * al tipo de regimen y la naturaleza del paciente
	 * @param con Conexión con la BD
	 * @param tipoRegimen Tipo del regimen del paciente
	 * @param naturalezaPaciente Naturaleza del paciente
	 * @return true si tiene excepción, false de lo contrario, false con descripción
	 * (Codificada ApplicationReources) en caso de error
	 */
	public static ResultadoBoolean buscarExcepcionPorNaturaleza(Connection con, String tipoRegimen, int naturalezaPaciente)
	{
		logger.info("buscarExcepcionPorNaturaleza->"+buscarExcepcionStr+" tipoReg->"+tipoRegimen+" natopac->"+naturalezaPaciente);
		try
		{
			PreparedStatementDecorator buscarExcepcion= new PreparedStatementDecorator(con.prepareStatement(buscarExcepcionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarExcepcion.setString(1, tipoRegimen);
			buscarExcepcion.setInt(2, naturalezaPaciente);
			
			ResultSetDecorator resultado=new ResultSetDecorator(buscarExcepcion.executeQuery());
			if(resultado.next())
			{
				if(resultado.getBoolean("tieneExcepcion"))
				{
					return new ResultadoBoolean(true);
				}
				else
				{
					return new ResultadoBoolean(false);
				}
			}
			else
			{
				return new ResultadoBoolean(false, "error.facturacion.faltaParametrizarExcepcionNaturaleza");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la excepcion por naturaleza "+e);
			return new ResultadoBoolean(false, "errors.problemasBD");
		}
	}

}
