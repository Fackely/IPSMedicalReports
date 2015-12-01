package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Clase para el manejo de secciones y subsecciones x almacen
 * Date: 2008-01-16
 * @author garias@princetonsa.com - lgchavez@princetonsa.com
 */
public class SqlBaseSeccionesDao 
{
	//*********************** Atributos
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * Cadena de insercion de secciones
	 * */
	private static final String cadenaInsertarStr = "INSERT INTO secciones (centro_atencion, almacen, codigo_seccion, descripcion, institucion, usuario_modifica, fecha_modifica, hora_modifica, codigo_pk) VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?) ";
	
	/**
	 * Cadena de insercion de subsecciones
	 * */
	private static final String cadenaInsertarSubStr = "INSERT INTO subsecciones (codigo_pk_seccion, codigo_subseccion, descripcion, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	/**
	 * Cadena de modificar secciones
	 * */
	private static final String cadenaModificacionStr = "UPDATE secciones SET codigo_seccion=?, descripcion=? WHERE codigo_pk=?";
	
	/**
	 * Cadena para eliminar una seccion segun su codigoPK
	 */
	private static final String cadenaEliminacionStr = "DELETE FROM secciones WHERE codigo_pk=?";
	
	/**
	 *  Cadena para consultar todas las secciones
	 */
	private static final String cadenaConsultaStr = "select " +
															"s.codigo_pk, " +
															"s.centro_atencion as codcentroatencion, " +
															"c.descripcion as desccentroatencion, " +
															"s.almacen as codalmacen, " +
															"cc.nombre as descalmacen, " +
															"s.codigo_seccion as codseccion, " +
															"s.descripcion as descseccion, " +
															"s.institucion as institucion " +
														"FROM " +
															"secciones s " +
															"INNER JOIN centro_atencion c ON(c.consecutivo=s.centro_atencion) " +
															"INNER JOIN centros_costo cc ON (cc.codigo=s.almacen) " +
														"WHERE 1=1 "; 
	
	/**
	 * 
	 */
	private static final String cadenaConsultaSubseccionStr= "SELECT " +
																"codigo_pk_seccion as codigopkseccion, " +
																"codigo_subseccion, " +
																"descripcion, " +
																"institucion " +
															"FROM " +
																"subsecciones " +
																"where codigo_pk_seccion=? order by descripcion ";
	
	/**
	 * Cadena de modificar subsecciones
	 * */
	private static final String cadenaModificacionSubStr = "UPDATE subsecciones SET codigo_subseccion=?, descripcion=? WHERE codigo_pk_seccion =? and codigo_subseccion =?";
	
	/**
	 * Cadena para eliminar una subseccion segun su codigoPK
	 */
	private static final String cadenaEliminacionSubStr = "DELETE FROM subsecciones WHERE codigo_pk_seccion=? ";
	
	/**
	 * Indices del mapa seccionesMap 
	 * */
	private static final String [] indicesMap = {"codigo_pk_","codcentroatencion_","desccentroatencion_","codalmacen_","descalmacen_","codseccion_","descseccion_","institucion_"};
	

	/**
	 * Indices del mapa subseccionesMap 
	 * */
	private static final String [] indicesMap1 = {"codigopkseccion_","codigo_subseccion_","descripcion_","institucion_","puedoEliminar_"};
	
	
	

	/**
	 * 
	 * @param con
	 * @param secciones
	 */
	public static boolean insertar(Connection con, Secciones secciones)
	{
		try
		{
			logger.info("Insertar-->"+cadenaInsertarStr+" centro atencion->"+secciones.getCentroAtencion()+" almacen->"+secciones.getAlmacen()+" codsec->"+secciones.getCodigoSeccion()+" desc->"+secciones.getDescripcionSeccion()+" inst->"+secciones.getInstitucion()+" usu->"+secciones.getUsuarioModifica());
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO secciones (
			 * centro_atencion, 
			 * almacen, 
			 * codigo_seccion, 
			 * descripcion, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica, 
			 * codigo_pk) VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?) 
			 */
			
			ps.setInt(1, secciones.getCentroAtencion());
			ps.setInt(2, secciones.getAlmacen());
			ps.setInt(3, secciones.getCodigoSeccion());
			ps.setString(4, secciones.getDescripcionSeccion());
			ps.setInt(5, secciones.getInstitucion());
			ps.setString(6, secciones.getUsuarioModifica());
			ps.setInt(7, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_secciones"));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.warn("NO ES POSIBLE INSERTAR ->"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param secciones
	 */
	public static boolean modificar(Connection con, Secciones secciones)
	{
		try
		{
			logger.info("\n modificar-->"+cadenaModificacionStr+" secciones.getCodigoSeccion()->"+secciones.getCodigoSeccion()+" secciones.getDescripcionSeccion()->"+secciones.getDescripcionSeccion()+" secciones.getCodigoPk()->"+secciones.getCodigoPk());
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, secciones.getCodigoSeccion());
			ps.setString(2, secciones.getDescripcionSeccion());
			ps.setInt(3,secciones.getCodigoPk());
			

			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.warn("NO ES POSIBLE REALIZAR LA MODIFICACION ->>"+e);
		}
		return false;
	}	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminar(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			logger.warn("No se puede eliminar el registro: "+codigo);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultar(Connection con, Secciones secciones) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String cadena= cadenaConsultaStr;
		
		if(secciones.getAlmacen()>0)
			cadena+= " AND s.almacen = "+secciones.getAlmacen();
		
		if(secciones.getCentroAtencion()>0)
			cadena+= " AND s.centro_atencion = "+secciones.getCentroAtencion();
		
		if(secciones.getCodigoPk()>0)
			cadena+= " AND s.codigo_pk = "+secciones.getCodigoPk();
		
		if(secciones.getCodigoSeccion()>0)
			cadena+= " AND s.codigo_seccion = "+secciones.getCodigoSeccion();
		
		if(!UtilidadTexto.isEmpty(secciones.getDescripcionSeccion()))
			cadena+= " AND s.descripcion = '"+secciones.getDescripcionSeccion()+"' ";
		
		if(secciones.getInstitucion()>0)
			cadena+= " AND s.institucion = '"+secciones.getInstitucion()+"' ";
		
		cadena+=" ORDER BY descseccion ";
		
		logger.info("\n CONSULTA SECCIONES-->"+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("INDICES_MAPA",indicesMap);	
			for(int w=0; w<Utilidades.convertirAEntero( mapa.get("numRegistros").toString()); w++)
			{
				logger.info("entra w->"+w);
				if(puedoEliminarSeccion(con, Utilidades.convertirAEntero(mapa.get("codigo_pk_"+w)+"")))
				{
					logger.info("asigna puedoelim s "+w);
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					logger.info("asigna puedoelim n "+w);
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param codigoPkSeccion
	 * @param codigoPkSubseccion
	 * @return
	 */
	private static boolean puedoEliminarSeccion(Connection con, int codigoPkSeccion)
	{
		boolean retorna=false;
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminarSeccion=eliminar(con, codigoPkSeccion);
		if(puedoEliminarSeccion)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarSubsecciones(Connection con, int codigoseccion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSubseccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoseccion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("INDICES_MAPA",indicesMap1);	
			
			for(int w=0; w<Utilidades.convertirAEntero( mapa.get("numRegistros").toString()); w++)
			{
				logger.info("entra w->"+w);
				if(puedoEliminarSubSeccion(Utilidades.convertirAEntero(mapa.get("codigopkseccion_"+w)+"") , Utilidades.convertirAEntero(mapa.get("codigo_subseccion_"+w)+"")))
				{
					logger.info("asigna puedoelim s "+w);
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					logger.info("asigna puedoelim n "+w);
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
		}
		catch (SQLException e)
		{
			logger.warn("No se puede eliminar el registro: "+codigoseccion+", "+e);
		}
		return mapa;
	}
	
	
	
	
	public static HashMap cargarSubsecciones(Connection con, Secciones subseccion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSubseccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, subseccion.getCodigoPk());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}	
	
	/**
	 * 
	 * @param codigoPkSeccion
	 * @param codigoPkSubseccion
	 * @return
	 */
	private static boolean puedoEliminarSubSeccion(int codigoPkSeccion, int codigoPkSubseccion)
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		Secciones seccion= new Secciones();
		seccion.setCodigoPk(codigoPkSeccion);
		seccion.setCodigoSubseccion(codigoPkSubseccion+"");
		boolean puedoElim=eliminarSubseccion(con, seccion);
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		if(puedoElim)
			return true;
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public static boolean insertarSubsecciones(Connection con, Secciones secciones){
		try
		{
			//logger.info("Insertar-->"+cadenaInsertarSubStr+" centro atencion->"+secciones.getCentroAtencion()+" almacen->"+secciones.getAlmacen()+" codsec->"+secciones.getCodigoSeccion()+" desc->"+secciones.getDescripcionSeccion()+" inst->"+secciones.getInstitucion()+" usu->"+secciones.getUsuarioModifica());
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSubStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO subsecciones (
			 * codigo_pk_seccion, 
			 * codigo_subseccion, 
			 * descripcion, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") 
			 */
			
			ps.setInt(1, secciones.getCodigoPk());
			ps.setInt(2, Utilidades.convertirAEntero(secciones.getCodigoSubseccion()));
			ps.setString(3, secciones.getDescripcionSubseccion());
			ps.setInt(4, secciones.getInstitucion());
			ps.setString(5, secciones.getUsuarioModifica());
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.warn("NO ES POSIBLE INSERTAR LA SUBSECCION ->>"+e);
		}
		return false;	
	}
	
	/**
	 * 
	 * @param con
	 * @param secciones
	 */
	public static boolean modificarSubseccion(Connection con, Secciones secciones)
	{
		try
		{
			logger.info("\n modificar88-->"+cadenaModificacionSubStr+" secciones.getCodigoSubseccion()->"+secciones.getCodigoSubseccion()+" secciones.getDescripcionSubseccion()->"+secciones.getDescripcionSubseccion()+" secciones.getCodigoPk()->"+secciones.getCodigoPk()+"+secciones.getCodigoSubseccion()+-->"+secciones.getCodigoSubseccionTemp());
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionSubStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE subsecciones SET codigo_subseccion=?, descripcion=? WHERE codigo_pk_seccion =? and codigo_subseccion =?
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(secciones.getCodigoSubseccion()));
			ps.setString(2,secciones.getDescripcionSubseccion());
			ps.setInt(3,secciones.getCodigoPk());
			ps.setInt(4,Utilidades.convertirAEntero(secciones.getCodigoSubseccionTemp()));
			

			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.warn("NO ES POSIBLE MODIFICAR LA SUBSECCION ->>"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param secciones
	 * @return
	 */
	public static boolean eliminarSubseccion(Connection con, Secciones secciones)
	{
		try
		{
			String cadena= cadenaEliminacionSubStr;
			if(!UtilidadTexto.isEmpty(secciones.getCodigoSubseccion()))
				cadena+="and codigo_subseccion ="+secciones.getCodigoSubseccion();
			
			//logger.info("\n Eliminar subseccion ------ secciones.getCodigoSubseccion()->"+secciones.getCodigoSubseccion()+" secciones.getCodigoPk()->"+secciones.getCodigoPk());
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,secciones.getCodigoPk());
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			logger.warn("No se puede eliminar el registro: ");
		}
		return false;
	}
	
}