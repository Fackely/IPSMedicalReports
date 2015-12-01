/*
 *  @author armando
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author armando
 *
 */
public class SqlBaseProgramasSaludPYPDao 
{
	private static Logger logger =Logger.getLogger(SqlBaseProgramasSaludPYPDao.class);

	/**
	 * 
	 */
	private static String cadenaConsultaProgramasSalud="SELECT " +
														"	psp.codigo as codigo," +
														"	psp.institucion as institucion," +
														"	psp.descripcion as descripcion," +
														"	psp.tipo_programa as tipoprograma," +
														"	tpp.descripcion as desctipoprograma," +
														"	psp.grupo_etareo as grupoetareo," +
														"	psp.embarazo as embarazo," +
														"	psp.formato as formato," +
														"	psp.archivo as archivo, " +
														"	psp.activo as activo, " +
														"	'BD' as tiporegistro " +
														" from programas_salud_pyp psp" +
														" inner join tipos_programa_pyp tpp on (psp.tipo_programa=tpp.codigo and psp.institucion=tpp.institucion)" +
														" where psp.institucion=?";
	
	
	/**
	 * 
	 */
	private static String cadenaDeletePrograma="DELETE FROM programas_salud_pyp WHERE codigo=? and institucion=?";
	
	/**
	 * 
	 */
	private static String cadenaUpdatePrograma="UPDATE programas_salud_pyp SET descripcion=?,tipo_programa=?,grupo_etareo=?,embarazo=?,formato=?,archivo=?,activo=? where codigo=? and institucion=?";
	
	
	private static String cadenaInsertaPrograma="INSERT INTO programas_salud_pyp (codigo,institucion,descripcion,tipo_programa,grupo_etareo,embarazo,formato,archivo,activo) values (?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaInsertaDiagnosticosPrograma="insert into diag_prog_sal_pyp (codigo,institucion,acronimo,tipo_cie) values(?,?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaEliminarDiagnosticosPrograma="delete from diag_prog_sal_pyp where codigo=? and institucion=? and acronimo=? and tipo_cie=?";
	
	/**
	 * 
	 */
	private static String cadenaConsultarDiagnosticosPrograma="select dp.acronimo,dp.tipo_cie as cie,d.nombre,'BD' as tiporegistro from diag_prog_sal_pyp dp inner join diagnosticos d on(dp.acronimo=d.acronimo and dp.tipo_cie=d.tipo_cie) where dp.codigo=? and dp.institucion=?";
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	public static HashMap cargarInfomacionBD(Connection con, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaProgramasSalud+" order by psp.codigo ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramasSaludPYPDao[cargarInfomacionBD] -->");
			e.printStackTrace();
		}
		//ps.set
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, String codigo, int institucion) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("delete from diag_prog_sal_pyp where codigo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,institucion);
			ps.executeUpdate();
			ps.close();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDeletePrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,institucion);
			return ps.executeUpdate()>0;
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramasSaludPYPDao[eliminarRegistro] -->");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarPrograma(Connection con, String codigo, int institucion) 
	{
		String cadena=cadenaConsultaProgramasSalud+" and psp.codigo=?";
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setString(2,codigo);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	    	logger.error("ERROR EN SqlBaseProgramasSaludPYPDao[cargarPrograma] -->"+e);
	    }
		return null;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param tipoPrograma
	 * @param grupoEtareo
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param embarazo
	 * @param formato
	 * @param archivo
	 * @param activo
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo, boolean embarazo, String formato, String archivo, boolean activo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdatePrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE programas_salud_pyp SET 
			 * descripcion=?,
			 * tipo_programa=?,
			 * grupo_etareo=?,
			 * embarazo=?,
			 * formato=?,
			 * archivo=?,
			 * activo=? 
			 * where codigo=? 
			 * and institucion=?
			 */
			
			ps.setString(1,descripcion);
			ps.setDouble(2,Utilidades.convertirADouble(tipoPrograma));
			ps.setDouble(3,Utilidades.convertirADouble(grupoEtareo));
			
			String embarazada= null;
			if (embarazo)
			{ embarazada="1";}
			else
			{ embarazada=null;}
	        ps.setString(4,embarazada);
	        if(formato.equals("")||formato.equals("null"))
	        {
	        	ps.setNull(5,Types.NUMERIC);
	        }
	        else
	        {
	        	ps.setDouble(5,Utilidades.convertirADouble(formato));
	        }
	        if(archivo.equals(""))
	        {
	        	ps.setNull(6,Types.VARCHAR);
	        }
	        else 
	        {
	        	ps.setString(6,archivo);
	        }
			ps.setBoolean(7,activo);
			ps.setString(8,codigo);
			ps.setInt(9,institucion);
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseProgramasSaludPYPDao[modificarRegistro] -->");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param tipoPrograma
	 * @param grupoEtareo
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param embarazo
	 * @param formato
	 * @param archivo
	 * @param activo
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo, boolean embarazo, String formato, String archivo, boolean activo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertaPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO programas_salud_pyp (
			 * codigo,
			 * institucion,
			 * descripcion,
			 * tipo_programa,
			 * grupo_etareo,
			 * embarazo,
			 * formato,
			 * archivo,activo) values (?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1,codigo+"");
			ps.setInt(2,institucion);
			ps.setString(3,descripcion);
			ps.setDouble(4,Utilidades.convertirADouble(tipoPrograma));
			ps.setDouble(5,Utilidades.convertirADouble(grupoEtareo));
			String embarazada;
			if (embarazo)
			{ embarazada="1";}
			else
			{ embarazada=null;}
	        ps.setString(6,embarazada);
	        if(formato.equals(""))
	        {
	        	ps.setNull(7,Types.NUMERIC);
	        }
	        else
	        {
	        	ps.setDouble(7,Utilidades.convertirADouble(formato));
	        }
	        if(archivo.equals(""))
	        {
	        	ps.setNull(8,Types.VARCHAR);
	        }
	        else 
	        {
	        	ps.setString(8,archivo);
	        }
			ps.setBoolean(9,activo);
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[insertarRegistro] -->");
			e.printStackTrace();
		}
		return false;
	}


	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public static boolean guardarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertaDiagnosticosPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * insert into diag_prog_sal_pyp (codigo,institucion,acronimo,tipo_cie) values(?,?,?,?)
			 */
			
			ps.setString(1,codigo+"");
			ps.setInt(2,institucion);
			ps.setString(3,acronimo);
			ps.setInt(4,Utilidades.convertirAEntero(cie));
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[guardarDiagnostico] -->");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public static boolean eliminarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie)
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarDiagnosticosPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * delete from diag_prog_sal_pyp where codigo=? and institucion=? and acronimo=? and tipo_cie=?
			 */
			
			ps.setString(1,codigo+"");
			ps.setInt(2,institucion);
			ps.setString(3,acronimo);
			ps.setInt(4,Utilidades.convertirAEntero(cie));
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[eliminarDiagnostico] -->");
			e.printStackTrace();
		}
		return false;
	}


	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarDiagnosticosPrograma(Connection con, String codigo, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultarDiagnosticosPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo+"");
			ps.setInt(2,institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[cargarDiagnosticosPrograma] -->");
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Método para saber si puedo eliminar un programa
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean puedoEliminarPrograma(Connection con,String codigo,int institucion)
	{
		boolean puedoEliminar = true;
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("delete from diag_prog_sal_pyp where codigo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,institucion);
			ps.executeUpdate();
			ps.close();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDeletePrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,institucion);
			ps.executeUpdate();
			ps.close();
			
		} 
		catch (SQLException e) 
		{
			logger.info("NO SE PUEDE ELIMINAR EL PROGRAMA");
			puedoEliminar = false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return puedoEliminar;
	}
	

}
