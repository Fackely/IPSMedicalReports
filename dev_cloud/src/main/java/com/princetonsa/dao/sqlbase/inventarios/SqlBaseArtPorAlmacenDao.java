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
import com.princetonsa.mundo.inventarios.ArticulosPorAlmacen;

public class SqlBaseArtPorAlmacenDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * 
	 */
	private static final String consultaStr = "SELECT "+ 	
														"det.codigo_det_pk, "+
														"det.codigo_art_por_almacen, "+
														"det.articulo, "+
														"det.ubicacion as ubicacion, "+
														"det.ubicacion as ubicacionold, "+
														"a.codigo as cod, "+
														"a.codigo_interfaz as codinterf, "+
														"getdescarticulosincodigo(a.codigo) as articulodesc, " +
														"getunidadmedidaarticulo(a.codigo) as umedida "+
													"FROM "+
														"det_articulos_por_almacen det "+
													"INNER JOIN "+
														"articulos_por_almacen apa " +
														"ON (apa.institucion = ?) " +
													"INNER JOIN " +
														"articulo a " +
														"ON (a.codigo = det.articulo)"+
														"AND apa.centro_atencion = ? " +
														"AND apa.almacen = ? " +
														"AND apa.seccion = ? " +
														"AND apa.subseccion = ? " +
													"WHERE 1=1 "; 
	
	/**
	 * 
	 */
	private static final String existeArticulosPorAlmacenStr = "SELECT codigo_pk " +
															"FROM articulos_por_almacen " +
															"WHERE institucion = ? " +
															"AND centro_atencion = ? " +
															"AND almacen = ? " +
															"AND seccion = ? " +
															"AND subseccion = ? ";
	
	/**
	 * Indices del mapa articulos por almacen 
	 * */
	private static final String [] indicesMap = {"codigo_det_pk_","codigo_art_por_almacen_","articulo_","ubicacion_","cod_","codinterf_","articulodesc_","umedida_","puedoEliminar_","ubicacionold_"};
	
	 
	  
	 
	/**
	 * 
	 */
	private static final String insertarArticulosPorAlmacenStr = "INSERT INTO articulos_por_almacen (centro_atencion, almacen, seccion, subseccion, institucion, usuario_modifica, fecha_modifica, hora_modifica, codigo_pk) VALUES(?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)"; 

	/**
	 * 
	 */
	private static final String insertarDetArticulosPorAlmacenStr = "INSERT INTO det_articulos_por_almacen (codigo_art_por_almacen, articulo, ubicacion, usuario_modifica, fecha_modifica, hora_modifica, codigo_det_pk) VALUES(?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * 
	 */
	private static final String eliminarRegistroDetStr = "DELETE FROM det_articulos_por_almacen WHERE codigo_det_pk = ? ";
	
	/**
	 * 
	 */
	private static final String modificarRegistroDetStr = "UPDATE det_articulos_por_almacen SET ubicacion = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE codigo_det_pk = ? ";
	
	/**
	 * Almacena la llave primaria de la tabla articulos por almacen
	 */
	private static int cod = ConstantesBD.codigoNuncaValido;
	
	
	
	// --------------- METODOS
	
	/**
	 * 
	 */
	public static HashMap consultarUbicaciones(Connection con, ArticulosPorAlmacen a)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
			String cadena = 	"SELECT "+
									"dapa.ubicacion as ubicacion, " +
									"getnombreseccion(apa.seccion) as seccion, " +
									"getnombresubseccion(apa.seccion, apa.subseccion) as subseccion "+
								"FROM "+
									"articulos_por_almacen apa "+
								"INNER JOIN "+
									"det_articulos_por_almacen dapa ON (apa.codigo_pk = dapa.codigo_art_por_almacen) " +
								"WHERE "+
									" apa.centro_atencion = " + a.getCentroAtencion() +
									" AND apa.almacen = " + a.getAlmacen() +
									" AND apa.seccion = " + a.getSeccion() +
									" AND dapa.articulo = " + a.getCodigoArticulo();
			
			if (a.getSubseccion()!=ConstantesBD.codigoNuncaValido)						
				cadena += " AND apa.subseccion = "+a.getSubseccion(); 		
			
			cadena +=" ORDER BY seccion, subseccion, ubicacion";
			
			logger.info("CONSULTA UBICACIONES - "+cadena);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 */
	public static boolean eliminarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		try
		{
			logger.info("codigo det art-----------> "+a.getCodigoDetArt());
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistroDetStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, a.getCodigoDetArt());
			ps.executeUpdate();
			logger.info("bien");
			
			if(ps.executeUpdate()>0)
				return true; 
		}	
		catch (SQLException e)
		{
			logger.warn("No es posible eliminar el registro -->"+e);
		}			
		return false;
	}
	
	/**
	 * 
	 */
	public static boolean modificarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		try
		{
			logger.info("ubicacion-----------> "+a.getUbicacion());
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarRegistroDetStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, a.getUbicacion());
			ps.setString(2, a.getUsuarioModifica());
			ps.setInt(3, a.getCodigoDetArt());
			
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
	 */
	public static boolean guardarNuevo(Connection con, ArticulosPorAlmacen a){
		PreparedStatementDecorator ps;
		try
		{
			cod = existeArticulosPorAlmacen(con, a);
			if (cod == ConstantesBD.codigoNuncaValido) {
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarArticulosPorAlmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO articulos_por_almacen (
				 * centro_atencion, 
				 * almacen, 
				 * seccion, 
				 * subseccion, 
				 * institucion, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica, 
				 * codigo_pk) VALUES(?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)
				 */
				
				ps.setInt(1, a.getCentroAtencion());
				ps.setInt(2, a.getAlmacen());
				ps.setInt(3, Utilidades.convertirAEntero(a.getSeccion()));
				ps.setInt(4, a.getSubseccion());
				ps.setInt(5, a.getInstitucion());
				ps.setString(6, a.getUsuarioModifica());
				ps.setInt(7, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_articulos_por_almacen"));
				ps.executeUpdate();
			}
			cod = existeArticulosPorAlmacen(con, a);
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetArticulosPorAlmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_articulos_por_almacen (
			 * codigo_art_por_almacen, 
			 * articulo, 
			 * ubicacion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica, 
			 * codigo_det_pk) VALUES(?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
			 */
			
			
			ps.setInt(1, cod);
			ps.setInt(2, a.getCodigoArticulo());
			ps.setString(3, a.getUbicacion());			
			ps.setString(4, a.getUsuarioModifica());
			ps.setInt(5, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_art_por_almacen"));
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}			
		return false;
	}
	
	public static int existeArticulosPorAlmacen (Connection con, ArticulosPorAlmacen a){
		int codigo = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existeArticulosPorAlmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, a.getInstitucion());
			ps.setInt(2, a.getCentroAtencion());
			ps.setInt(3, a.getAlmacen());
			ps.setInt(4, Utilidades.convertirAEntero(a.getSeccion()));
			ps.setInt(5, a.getSubseccion());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigo = rs.getInt("codigo_pk");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return codigo;
	}
	
	
	/**
	 * 
	 * @param codigoPkSeccion
	 * @param codigoPkSubseccion
	 * @return
	 */
	private static boolean puedoEliminarUbicacion(int codigoArticulo)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminarUbicacion;
		int consulta=consultaEliminar(con, codigoArticulo);
		
		logger.info("CONSULTA ELIMINAR ---->>>>>>>>>>>>"+consulta);
		
		if(consulta == ConstantesBD.codigoNuncaValido)
			retorna= true;
		else
			retorna=false;
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	
	
	/**
	 * 
	 */
	public static int consultaEliminar(Connection con, int CodigoDetalle)
	{
		try
		{
			String sePuedeEliminar ="SELECT codigo_dapa from preparacion_toma_inventario where codigo_dapa=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sePuedeEliminar, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, CodigoDetalle);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo_dapa");
		}	
		catch (SQLException e)
		{
			logger.info("NO HAY REGISTROS"+e);
		}			
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	/** 
	 * @param con
	 * @param seccion
	 * @return
	 */	
	public static HashMap consultar(Connection con, ArticulosPorAlmacen a){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
			cod = existeArticulosPorAlmacen(con, a);
			String cadena = consultaStr;
			if(cod>0)
				cadena+= "AND det.codigo_art_por_almacen = "+cod;
			cadena+= " ORDER BY a.descripcion";
			
			logger.info(cadena);
			logger.info("Institucion--> "+a.getInstitucion()+" Centro atencion->"+a.getCentroAtencion()+" Almacen->"+a.getAlmacen()+" Seccion->"+a.getSeccion()+" Subseccion->"+a.getSubseccion());
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, a.getInstitucion());
			ps.setInt(2, a.getCentroAtencion());
			ps.setInt(3, a.getAlmacen());
			ps.setInt(4, Utilidades.convertirAEntero(a.getSeccion()));
			ps.setInt(5, a.getSubseccion());
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("INDICES_MAPA",indicesMap);
			for(int w=0; w<Utilidades.convertirAEntero( mapa.get("numRegistros").toString()); w++)
			{
				if(puedoEliminarUbicacion(Utilidades.convertirAEntero(mapa.get("codigo_det_pk_"+w)+"")))
				{
					logger.info("&&&& PUEDO ELIMINAR S "+w);
					mapa.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					logger.info("&&&& PUEDO ELIMINAR N "+w);
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
	
}