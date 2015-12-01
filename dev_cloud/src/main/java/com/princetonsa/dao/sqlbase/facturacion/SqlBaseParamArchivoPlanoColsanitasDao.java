package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseParamArchivoPlanoColsanitasDao {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseParamArchivoPlanoColsanitasDao.class);
	
	/**
	 * Cadena para la insercion de la parametrizacion de los archivos planos de colsanitas
	 */
	private static final String insertarArchivoPlanoColsanitasStr="INSERT INTO archivo_plano_colsanitas (convenio,unidad_economica,identif_compania,identif_plan,institucion,fecha_modificacion,hora_modificacion,usuario_modificacion)";
	private static final String insertarArchivoPlanoColsanitasPos=" VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	private static final String insertarArchivoPlanoColsanitasOra=" VALUES (?,?,?,?,?, (select to_date(sysdate) from dual),substr((select to_char(sysdate, 'hh24:mi:ss') from dual), 1,5),?)";
	
	/**
	 * Cadena para la consulta de la parametrizacion de los archivos planos de colsanitas
	 */
	private static final String consultarArchivosPlanosColsanitasStr="SELECT convenio as convenio,unidad_economica as unidad,identif_compania as compania,identif_plan as plan,'"+ConstantesBD.acronimoSi+"' as estabd"+
																		" FROM archivo_plano_colsanitas";
	
	/**
	 * Cadena para la actualizacion de la parametrizacion de archivos planos colsanitas
	 */
	private static final String modificarArchivosPlanosColsanitasStr="UPDATE archivo_plano_colsanitas SET unidad_economica=?,identif_compania=?,identif_plan=?,institucion=?,fecha_modificacion=CURRENT_DATE,hora_modificacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_modificacion=? WHERE convenio=?";
	
	/**
	 * Cadena para la eliminacion de una parametrizacion de archivo plano de colsanitas
	 */
	private static final String eliminarArchivoPlanoColsanitasStr="DELETE FROM archivo_plano_colsanitas WHERE convenio=?";
	
	/**
	 * Metodo que inserta la parametrizacion de los archivos planos de colsanitas
	 * @param con
	 * @param vo
	 * @param Tipo_BD 
	 * @return
	 */
	public static boolean insertarArchivoPlanoColsanitas(Connection con,HashMap<String, Object> vo, int Tipo_BD)
	{
	//	logger.info("\n ::::: ENTRE A INSERTAR ARCHIVO :::::::");
		try
		{
			String insertArch=insertarArchivoPlanoColsanitasStr;
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
				insertArch+=insertarArchivoPlanoColsanitasOra;
				break;
			case DaoFactory.POSTGRESQL:
				insertArch+=insertarArchivoPlanoColsanitasPos;
				break;
				default:
					break;
						}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertArch,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO archivo_plano_colsanitas (
			 * convenio,
			 * unidad_economica
			 * ,identif_compania,
			 * identif_plan,
			 * institucion,
			 * fecha_modificacion,
			 * hora_modificacion,
			 * usuario_modificacion)"+VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("convenio")+""));
			ps.setString(2, vo.get("unidad")+"");
			ps.setString(3, vo.get("compania")+"");
			ps.setString(4, vo.get("plan")+"");
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(6, vo.get("usuario")+"");
			if(ps.executeUpdate()>0)
			{
				logger.info("\n\nINSERTANDO");
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
	 * Metodo que consulta las parametrizaciondes de archivos planos de colsanitas
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static HashMap consultarArchivoPlanoColsanitas(Connection con,int convenio)
	{
		//logger.info("\n ::::: ENTRE A CONSULTAR ARCHIVO :::::::");
		
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
		String cadena=consultarArchivosPlanosColsanitasStr;
		if(convenio!=ConstantesBD.codigoNuncaValido)
			cadena+=" WHERE convenio="+convenio;
		try
		{
			cadena+=" ORDER BY convenio";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo para modificar la parametrizacion de los archivos planos de colsanitas
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarArchivoPlanoColsanitas(Connection con,HashMap vo)
	{
		//logger.info("\n ::::: ENTRE A MODIFICAR ARCHIVO :::::::");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarArchivosPlanosColsanitasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE archivo_plano_colsanitas SET 
			 * unidad_economica=?,
			 * identif_compania=?,
			 * identif_plan=?,
			 * institucion=?,
			 * fecha_modificacion=CURRENT_DATE,
			 * hora_modificacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+",
			 * usuario_modificacion=? 
			 * WHERE convenio=?
			 */
			
			ps.setString(1, vo.get("unidad")+"");
			ps.setString(2, vo.get("compania")+"");
			ps.setString(3, vo.get("plan")+"");
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(5, vo.get("usuario")+"");
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("convenio")+""));
			if(ps.executeUpdate()>0)
			{
				logger.info("\n\nMODIFICANDO");
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
	 * Metodo para eliminar la parametrizacion de archivos planos de colsanitas
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean eliminarArchivoPlanoColsanitas(Connection con,int convenio)
	{
		//logger.info("\n ::::: ENTRE A ELIMINAR ARCHIVO :::::::");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarArchivoPlanoColsanitasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, convenio);
			if(ps.executeUpdate()>0)
			{
				logger.info("\n\nELIMINANDO");
				return true;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

}