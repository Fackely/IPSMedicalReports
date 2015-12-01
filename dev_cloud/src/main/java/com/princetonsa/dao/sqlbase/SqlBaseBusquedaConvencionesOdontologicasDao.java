package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;

public class SqlBaseBusquedaConvencionesOdontologicasDao {

	private static Logger logger=Logger.getLogger(SqlBaseBusquedaConvencionesOdontologicasDao.class);
	
    /**
     * Cadena Sql para consultar Convenciones Odontologicas ( imagenes )
     */
	private static final String consultaConvencionesOdontologicasGenerica=" SELECT " +
			             "consecutivo AS consecutivo, " +
			             "codigo AS codigo, " +
			             "nombre AS nombre, " +
			             "tipo AS tipo, " +
			             "color AS color, " +
			             "trama AS trama, " +
			             "imagen AS imagen, " +
			             "archivo_convencion AS patharchivo " +
			             "FROM odontologia.convenciones_odontologicas " +
			             "WHERE institucion = ? AND  activo = '"+ConstantesBD.acronimoSi+ "' " +
	             		 "ORDER BY nombre ";
	
	/**
	 * Cadena Sql para consultar Convencioens odontologicas por tipo
	 */
	private static final String consultaConvencionesOdontologicasXTipo=" SELECT " +
    "consecutivo AS consecutivo, " +
    "codigo AS codigo, " +
    "nombre AS nombre, " +
    "tipo AS tipo, " +
    "color AS color, " +
    "trama AS trama, " +
    "imagen AS imagen, " +
    "archivo_convencion AS patharchivo " +
    "FROM odontologia.convenciones_odontologicas " +
    "WHERE institucion = ? AND  activo = '"+ConstantesBD.acronimoSi+"' AND tipo = '"+ConstantesBD.separadorSplit+"' " +
	"ORDER BY nombre ";
	
	
	/**
	 * cadena que consulta las imagenes base de una institucion 
	 */
	private static final String strConsultaImagenesBase = "SELECT " +
						"codigo_pk AS codigo_pk," +
						"institucion AS institucion," +
						"nombre_imagen AS nomImagen," +
						"imagen AS pathImagen," +
						"usuario_modifica AS usuMod " +
						"FROM historiaclinica.imagenes_base " +
						"WHERE institucion = ? ";
	
	/**
	 * Cadena Sal para consultar las tramas existentes en el sistema, ulilizadas para las convenciones Odonotologicas (tramas)
	 */
	private static final String consultaTramasConvencionOdontologica= "SELECT " +
			             "codigo AS codigo, " +
			             "nombre AS nombre, " +
			             "archivo AS patharchivo " +
			             "FROM odontologia.elementos_graficos " +
			             "WHERE tipo = '"+ConstantesIntegridadDominio.acronimoTipoElementGraficoTrama+"' ";
	
	/**
	 * Cadena Sql para consultar las Imágenes existentes en el sistema ulilizadas para las convenciones odontologicas (imagenes) 
	 */
	private static final String consultaImagenesConvencionOdontologica= "SELECT " +
						"codigo AS codigo, " +
					    "nombre AS nombre, " +
					    "archivo AS patharchivo " +
					    "FROM odontologia.elementos_graficos " +
					    "WHERE tipo = '"+ConstantesIntegridadDominio.acronimoTipoElementGraficoImagen+"' ";

	
	/**
	 * Metodo que realiza la busqueda de las Convenciones Existentes en Sistema
	 * Si la busqueda se filtra por tipo se debe especificar el tipo mandando porTipo = true y tipo = acronimo
	 * @param institucion
	 * @param filtro
	 * @return
	 */
	public static ArrayList<InfoDatosStr> consultarConvencionesOdontologicas(int institucion,boolean porTipo,String tipo)
	{
		ArrayList<InfoDatosStr> datosConvencion=new ArrayList<InfoDatosStr>();
		String consulta= consultaConvencionesOdontologicasGenerica;
		
		if(porTipo)
		 {
			consulta= consultaConvencionesOdontologicasXTipo;
			consulta=consulta.replace(ConstantesBD.separadorSplit,tipo);
		 }
		
		

		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try 
		{
			logger.info("\n\n\n\n\n SQL Busqueda Convenciones / " + consulta +"  Institucion : "+ institucion +" porTipo: "+porTipo+" tipo: "+tipo);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) 
			{
				InfoDatosStr tmpConvencion = new InfoDatosStr();
				tmpConvencion.setCodigo(rs.getString("consecutivo"));
				tmpConvencion.setNombre(rs.getString("nombre"));
				tmpConvencion.setDescripcion(rs.getString("patharchivo"));
				datosConvencion.add(tmpConvencion);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Convenciones==> " + e);
		}
		
		UtilidadBD.closeConnection(con);		
		return datosConvencion; 
	}
	
	/**
	 * Metodo que realiza la busqued de las Tramas utilizadas para la configuracion de la Convencion
	 * @return
	 */
	public static ArrayList<InfoDatosStr> consultarTramasConvencionesOdontologicas()
	{
		ArrayList<InfoDatosStr> datosTrama=new ArrayList<InfoDatosStr>();
		
		String consulta= consultaTramasConvencionOdontologica;
		

		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar usuarios centro / " + consulta);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) 
			{
				InfoDatosStr tmpConvencion = new InfoDatosStr();
				
				tmpConvencion.setCodigo(rs.getString("codigo"));
				tmpConvencion.setNombre(rs.getString("nombre"));
				tmpConvencion.setDescripcion(rs.getString("patharchivo"));
				datosTrama.add(tmpConvencion);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Convenciones==> " + e);
		}
		
		UtilidadBD.closeConnection(con);		
		return datosTrama; 
	}
	
	
	/**
	 * Metodo que realiza la busqueda de las imagenes utilizadas para configurar la Convencion
	 * @return
	 */
	public static ArrayList<InfoDatosStr> consultarImagenesConvencionesOdontologicas()
	{
		ArrayList<InfoDatosStr> datosImagen=new ArrayList<InfoDatosStr>();
		
		String consulta= consultaImagenesConvencionOdontologica;
		

		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Imagenes / " + consulta );
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) 
			{
				InfoDatosStr tmpConvencion = new InfoDatosStr();
				
				tmpConvencion.setCodigo(rs.getString("codigo"));
				tmpConvencion.setNombre(rs.getString("nombre"));
				tmpConvencion.setDescripcion(rs.getString("patharchivo"));
				datosImagen.add(tmpConvencion);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Imagenes==> " + e);
		}
		
		UtilidadBD.closeConnection(con);		
		return datosImagen; 
	}
	
	/**
	 * cadena que consulta las imagenes base de una institucion
	 * @param institucion
	 * @return
	 */
	public static ArrayList<InfoDatosStr> consultarImagenesBase(int institucion)
	{
		ArrayList<InfoDatosStr> datosImgBase = new ArrayList<InfoDatosStr>();
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar usuarios centro / " + strConsultaImagenesBase +"  Institucion : "+ institucion);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(strConsultaImagenesBase, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				InfoDatosStr tmpConvencion = new InfoDatosStr();
				tmpConvencion.setCodigo(rs.getString("codigo_pk"));
				tmpConvencion.setNombre(rs.getString("nomImagen"));
				tmpConvencion.setDescripcion(rs.getString("pathImagen"));
				datosImgBase.add(tmpConvencion);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Imagenes Base==> " + e);
			logger.info("Error Consulta: "+strConsultaImagenesBase);
		}
		UtilidadBD.closeConnection(con);		
		return datosImgBase; 
	}
	
	
}
