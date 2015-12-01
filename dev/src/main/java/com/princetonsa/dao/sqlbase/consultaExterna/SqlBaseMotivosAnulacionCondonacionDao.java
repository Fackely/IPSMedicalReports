package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.mercury.dao.sqlbase.odontologia.SqlBaseAntecedentesOdontologiaDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;


public class SqlBaseMotivosAnulacionCondonacionDao {
	
	private static Logger logger=Logger.getLogger(SqlBaseMotivosAnulacionCondonacionDao.class);

	
	private static  String insertarMotivoAnulacionCondonacionStr=" insert into consultaexterna.mot_anu_cond_multas ("+
	                                                                "consecutivo, "+
	                                                                "codigo, "+
	                                                                "institucion, "+
	                                                                "descripcion, "+
	                                                                "activo )"+
	                                                             "values(?,?,?,?,?)"; 
	                                                           
	private static  String actualizarMotivoAnulacionCondonacionStr="update consultaexterna.mot_anu_cond_multas set  "+
																	"codigo=?, "+
																	"institucion=?, "+
																	"descripcion=?, "+
																	"activo=? where consecutivo = ?";
	
	private static  String eliminarMotivoAnulacionCondonacionStr="delete from consultaexterna.mot_anu_cond_multas where consecutivo =?";
	
	
	private static  String ConsultarMotivoAnulacionCondonacionStr=" SELECT motv.consecutivo, " +
																	"motv.codigo, " +
																	"motv.descripcion, " +
																	"motv.activo " +
																	"FROM consultaexterna.mot_anu_cond_multas motv " ;
																	//"INNER JOIN administracion.instituciones inst ON(inst.codigo=motv.institucion) ";
	private static String consultarParaEliminacionStr="select count(*) as rows from consultaexterna.multas_citas where mot_anu_cond_multa= ?";
	

	public static HashMap consultarMotivosAnulacionCondonacionMultas(){
		Connection con=UtilidadBD.abrirConexion();
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(ConsultarMotivoAnulacionCondonacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("\n\n ERROR. CONSULTANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
			resultados.put("numRegistros", 0);
		}
		
		/*for (int i=0;  i<Utilidades.convertirAEntero(resultados.get("numRegistros")+""); i++) {
			if(UtilidadValidacion.)
		}*/
		
		return resultados;
	}





	public static boolean EliminarMotivosAnulacionCondonacionMultas(int consecutivo) {
		
		PreparedStatementDecorator ps;
		
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarMotivoAnulacionCondonacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, consecutivo);
			
			
			ps.executeUpdate();
			
			UtilidadBD.cerrarConexion(con);
			
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
			
			e.printStackTrace();
			
			return false;
		}
		return true;
	
	}


	public static boolean InsertarMotivosAnulacionCondonacionMultas(String codigo, String descripcion,boolean check, String codigoinstitucion) 
	{
		PreparedStatementDecorator ps ;
		Connection con=UtilidadBD.abrirConexion();
		
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_mot_anu_cond_multas");
		
					try
					{
						
					ps= new PreparedStatementDecorator(con.prepareStatement(insertarMotivoAnulacionCondonacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("insert --> "+insertarMotivoAnulacionCondonacionStr);
					ps.setInt(1, consecutivo);
					
					ps.setString(2,codigo);
					
					ps.setString(3,codigoinstitucion);
					
					ps.setString(4, descripcion);
					
					
					if(check)
					{
						
						ps.setString(5, ConstantesBD.acronimoSi);	
					}else
					{
						
						ps.setString(5,  ConstantesBD.acronimoNo);
					}
					
					
					logger.info("activo : "+check);
					
					if (ps.executeUpdate() <= 0)
					{
						UtilidadBD.cerrarConexion(con);
						
						return false;
					}
					
					UtilidadBD.cerrarConexion(con);
					
					return true;
					
						}catch (SQLException e)
					{
							logger.info("\n\nERROR. INSTERTANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
						
							return false;
					}
}





	public static boolean ModificarMotivosAnulacionCondonacionMultas(
			int concecutivo, String codigo, String descripcion, boolean check, String codigoinstitucion) {
		
		PreparedStatementDecorator ps ;
		
		Connection con=UtilidadBD.abrirConexion();
		
		try{  
			
		ps= new PreparedStatementDecorator(con.prepareStatement(actualizarMotivoAnulacionCondonacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		ps.setString(1,codigo);
		
		ps.setString(2,codigoinstitucion);
		
		ps.setString(3, descripcion);
		
		if(check)
		{
			
			ps.setString(4, ConstantesBD.acronimoSi);	
		}else
		{
			
			ps.setString(4,  ConstantesBD.acronimoNo);
		}
		
		ps.setInt(5, concecutivo);
		
		if (ps.executeUpdate() <= 0)
		{
			UtilidadBD.cerrarConexion(con);
			
			return false;
		}
		UtilidadBD.cerrarConexion(con);
		
		return true;
		}
		catch(SQLException e)
		{
			logger.info("\n\nERROR. MODIFICANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
		}
		
		return false;
	}





	public static boolean consultarEliminacion(int consecutivo)  {
		
		 try
	        {
			 		Connection con=UtilidadBD.abrirConexion();
	                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseMotivosAnulacionCondonacionDao.consultarParaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	                consultarStatement.setInt(1, consecutivo);
	                logger.info("\n\n consultica"+consultarParaEliminacionStr+""+consecutivo);
	                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
	                if(rs.next())
	                {
	                    int rows = rs.getInt("rows");
	                  
	                    UtilidadBD.cerrarConexion(con);
	                    if(rows>0)
	                        return true;
	                    else
	                        return false;
	                }
	                else
	                {
	                    String mensajeError = " (no retornó ningún registro) : "+consecutivo+". \n";
	                    logger.warn(mensajeError);
	                    throw new SQLException(mensajeError);
	                }   
	        }
	        catch(SQLException e)
	        {
	            logger.warn("ERROR!!!---> :  "+e);
	            return false;
	        }
		
		
	        
	}	
	
}