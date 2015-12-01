package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;
/**
 * Clase para el manejo de Farmcias x centro de costo
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */
public class SqlBaseFarmaciaCentroCostoDao 
{
	//*********************** Atributos
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseFarmaciaCentroCostoDao.class);
	
	/**
	 * Cadena de insercion de farmacia_x_centro_costo
	 * */
	private static final String cadenaInsertarStr = "INSERT INTO farmacia_x_centro_costo(codigo, centro_atencion, centro_costo, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	private static final String cadenaInsertarStrDet = "INSERT INTO det_farmacia_cc(codigo_farmacia_cc, farmacia, usuario_modifica, fecha_modifica, hora_modifica) VALUES(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	private static final String cadenaValida="SELECT count (*) as cuantos from farmacia_x_centro_costo where centro_atencion=? and  centro_costo=? and institucion=? ";
	
	
	/**
	 * Cadena para eliminar una relacion farmacia_x_centro_costo segun su codigo
	 */
	private static final String cadenaEliminacionStr = "DELETE FROM det_farmacia_cc WHERE codigo_farmacia_cc=? and farmacia=?";
	private static final String cadenaEliminacionStr1 = "DELETE FROM farmacia_x_centro_costo WHERE codigo=?";
	
	/**
	 *  Cadena para consultar todas las farmacia_x_centro_costo
	 */
	private static final String cadenaConsultaStr = " select " +
															"f.codigo, " +
															"f.centro_atencion as codcentroatencion, " +
															"f.centro_costo as codcentrocosto, " +
															"f.institucion as institucion, " +
															"getnomcentroatencion(f.centro_atencion) AS nombreCA, " +
															"getnomcentrocosto(f.centro_costo) AS nombreCC, " +
															"getnomcentrocosto(detf.codigo_farmacia_cc) AS nomFarmacia, " +
															"detf.codigo_farmacia_cc as codigodet "+
														"FROM "+
															"farmacia_x_centro_costo f INNER JOIN det_farmacia_cc detf ON (detf.farmacia=f.codigo) " +
															"WHERE 1=1 "; 
	
	
	/**
	 *  Cadena para consultar Codigo farmacia_x_centro_costo
	 */
	private static final String cadenafcc = " select " +
													"f.codigo as codigofcc " +
													"FROM  farmacia_x_centro_costo f " +
													"WHERE 1=1 "; 
	
	
	/**
	 * 
	 * @param con
	 * @param secciones
	 */
	public static boolean insertar(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		try
		{
			//logger.info("Insertar-->"+cadenaInsertarStr+" centro atencion->"+farmaciacentrocosto.getCentroAtencion()+" Centro costo->"+farmaciacentrocosto.getCentroCosto()()+" codins->"+farmaciacentrocosto.getInstitucion()+" usu->"+secciones.getUsuarioModifica());
		
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO farmacia_x_centro_costo(
				 * codigo, 
				 * centro_atencion, 
				 * centro_costo, 
				 * institucion, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica) VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") 
				 */
				
				ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_farmacia_x_centro_costo"));
				ps.setInt(2, farmaciacentrocosto.getCentroAtencion());
				ps.setInt(3, farmaciacentrocosto.getCentroCosto());
				ps.setInt(4, farmaciacentrocosto.getInstitucion());
				ps.setString(5, farmaciacentrocosto.getUsuarioModifica());
				

				
				if (ps.executeUpdate()>0)	
				{
					HashMap mapa=new HashMap();
					
					mapa=consultarcod(con, farmaciacentrocosto);
					
					String codigo=mapa.get("codigofcc_"+0)+"";
				
				PreparedStatementDecorator psdet= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStrDet,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO det_farmacia_cc(
				 * codigo_farmacia_cc, 
				 * farmacia, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica) VALUES(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") 
				 */
				
				psdet.setInt(1, farmaciacentrocosto.getFarmacia());
				psdet.setInt(2, Utilidades.convertirAEntero(codigo));
				psdet.setString(3, farmaciacentrocosto.getUsuarioModifica());
				
				if (psdet.executeUpdate()>0)	
					return true;
							
				}
		
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
 * @param farmaciacentrocosto
 * @return
 */
	public static boolean insertardet(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		HashMap mapa=new HashMap();
			
		mapa=consultarcod(con, farmaciacentrocosto);
		
		String codigo=mapa.get("codigofcc_"+0)+"";
		logger.info("codigo--->"+mapa.get("codigofcc_"+0));
		
		try
		{
			logger.info("Insertar detalle codigo-->"+farmaciacentrocosto.getFarmacia()+"               "+Utilidades.convertirAEntero(codigo));		
			
			
			
			PreparedStatementDecorator psdet= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStrDet,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psdet.setInt(1, farmaciacentrocosto.getFarmacia());
			psdet.setInt(2, Utilidades.convertirAEntero(codigo));
			psdet.setString(3, farmaciacentrocosto.getUsuarioModifica());
			
			if (psdet.executeUpdate()>0)	
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
	 * @param farmaciacentrocosto
	 * @return
	 */
	public static HashMap consultarcod(Connection con, FarmaciaCentroCosto farmaciacentrocosto) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String cadena=cadenafcc;
				
		if(farmaciacentrocosto.getCentroCosto()>0)
			cadena+= " AND f.centro_costo = "+farmaciacentrocosto.getCentroCosto();
		
		if(farmaciacentrocosto.getCentroAtencion()>0)
			cadena+= " AND f.centro_atencion = "+farmaciacentrocosto.getCentroAtencion();
		
		cadena+=" ORDER BY centro_costo ";
		
		logger.info("\n CONSULTA CODIGO FARMACIA CENTRO COSTO-->"+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	

	
	
	public static boolean insertartrans(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		HashMap mapa=new HashMap();
		try
		{
			
			UtilidadBD.iniciarTransaccion(con);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaValida,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * SELECT count (*) as cuantos from farmacia_x_centro_costo where centro_atencion=? and  centro_costo=? and institucion=? 
			 */
			
			ps.setInt(1, farmaciacentrocosto.getCentroAtencion());
			ps.setInt(2, farmaciacentrocosto.getCentroCosto());
			ps.setInt(3, farmaciacentrocosto.getInstitucion());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("[CentroAtencion]"+farmaciacentrocosto.getCentroAtencion());
			logger.info("[CetroCosto]"+farmaciacentrocosto.getCentroCosto());
			logger.info("[Institucion]"+farmaciacentrocosto.getInstitucion());
			
			logger.info("[Consulta Valida]"+cadenaValida);
			Utilidades.imprimirMapa(mapa);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	
		UtilidadBD.abortarTransaccion(con);
		if (Utilidades.convertirAEntero(mapa.get("cuantos_0").toString())>0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminar(Connection con, int codigo, int farmacia)
	{
		
		try
		{
			logger.info("\nCADENA ELIMINAR FAR X CC -->"+cadenaEliminacionStr+"  CODIGO  "+codigo+"  FARMACIA  "+farmacia);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, farmacia);
			
			if(ps.executeUpdate()>0)
			{
				int borrar=consultadetalles(con, farmacia);
					if (borrar==0)
					{
						logger.info("\nCADENA ELIMINAR FAR X CC 1-->"+cadenaEliminacionStr1+"   FARMACIA  "+farmacia);
						PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps1.setInt(1, farmacia);
						if(ps1.executeUpdate()>0)
								return true;
					}
					else
						return true;
			}
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
	 * @param codigo
	 * @return
	 */
	public static int consultadetalles(Connection con, int codigo)
	{
	String cadena="select count(*) as cont from det_farmacia_cc where farmacia="+codigo;
	HashMap mapa =new HashMap();
	try
	{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
		return Utilidades.convertirAEntero(mapa.get("cont_0")+"");
	}
	catch (SQLException e)
	{
		e.printStackTrace();
	}
		
		return 1;		
	}
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultar(Connection con, FarmaciaCentroCosto farmaciacentrocosto) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String cadena= cadenaConsultaStr;
		
		if(farmaciacentrocosto.getCentroCosto()>0)
			cadena+= " AND f.centro_costo = "+farmaciacentrocosto.getCentroCosto();
		
		if(farmaciacentrocosto.getCentroAtencion()>0)
			cadena+= " AND f.centro_atencion = "+farmaciacentrocosto.getCentroAtencion();
		
		if(farmaciacentrocosto.getCodigo()>0)
			cadena+= " AND f.codigo = "+farmaciacentrocosto.getCodigo();
		
		if(farmaciacentrocosto.getInstitucion()>0)
			cadena+= " AND f.institucion = '"+farmaciacentrocosto.getInstitucion()+"' ";
		
		cadena+=" ORDER BY centro_costo ";
		
		logger.info("\n CONSULTA FARMACIA CENTRO COSTO-->"+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
		
}