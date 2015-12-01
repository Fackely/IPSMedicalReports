package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseEscalasDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseEscalasDao.class);
	
	
	/**
	 * 
	 */
	public static String consultaEscalas="SELECT codigo_pk as codigopk, codigo as codigo, nombre as nombre FROM escalas WHERE mostrar_modificacion='"+ConstantesBD.acronimoSi+"' order by nombre ";
	
	/**
	 * 
	 */
	public static String consultaDetalleEscala="SELECT e.codigo_pk as codigopkescala, e.codigo as codigoescala, e.nombre as nombreescala, e.nombre as nombreescalaantiguo, e.observaciones_requeridas as observrequeridasesc "+
												"FROM escalas e "+
												"where e.codigo_pk=? and e.institucion=? and e.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
	
	
	/**
	 * 
	 */
	public static String consultaSecciones="SELECT es.codigo_pk as codigopkseccion, es.nombre as nombreseccion, es.nombre as nombreseccionantiguo, es.tipo_respuesta as tiporespuesta, es.activo as activo "+
												"FROM escalas_secciones es "+
												"where es.escala=? and es.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
	
	
	/**
	 * 
	 */
	public static String consultaFactores="SELECT efp.codigo_pk as codigopkfactor, efp.codigo as codigofactor, efp.nombre as nombrefactor, efp.nombre as nombrefactorantiguo, efp.valor_inicial as valorinicial, efp.valor_final as valorfinal, efp.activo as activofactor "+
												"FROM escalas_factores_pred efp "+
												"where efp.escala=? and efp.institucion=? and efp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
	
	
	/**
	 *  cadenas para las inserciones
	 */
	private static final String cadenaInsertarEscalasStr="INSERT INTO escalas (codigo_pk, codigo, nombre, observaciones_requeridas, mostrar_modificacion, institucion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarSeccionesStr="INSERT INTO escalas_secciones (codigo_pk, nombre, escala, tipo_respuesta, activo, mostrar_modificacion) VALUES (?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarFactoresStr="INSERT INTO escalas_factores_pred (codigo_pk, codigo, nombre, escala, valor_inicial, valor_final, institucion, activo, mostrar_modificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarCamposStr="INSERT INTO campos_parametrizables (codigo_pk, codigo, nombre, etiqueta, tipo, tamanio, signo, unidad, valor_predeterminado, maximo, minimo, decimales, columnas_ocupadas, orden, unico_fila, requerido, formula, institucion, tipo_html, mostrar, mostrar_modificacion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, 4, null, null, '', ?, ?, 2, null, null, ?, ?, '', ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarCamposSeccionStr="INSERT INTO escalas_campos_seccion (codigo_pk, campo_parametrizable, escala_seccion, observaciones_requeridas) VALUES (?, ?, ?, ?)";
	
	
	/**
	 * cadenas para las modificaciones
	 */
	
	private static final String cadenaModificacionCamposStr="UPDATE campos_parametrizables SET nombre=?, requerido=?, minimo=?, maximo=?, mostrar=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? and institucion=?";
	
	/**
	 * 
	 */
	private static final String cadenaModificacionFactoresStr="UPDATE escalas_factores_pred SET codigo=?, nombre=?, valor_inicial=?, valor_final=?, activo=?  WHERE codigo_pk=? and institucion=?";
	
	/**
	 * 
	 */
	private static final String cadenaModificacionSeccionStr="UPDATE escalas_secciones SET nombre=?, tipo_respuesta=?, activo=?  WHERE codigo_pk=? ";
	
	/**
	 * 
	 */
	private static final String cadenaModificacionCamposSeccion="UPDATE escalas_campos_seccion SET observaciones_requeridas=?  WHERE codigo_pk=? ";
	
	/**
	 * 
	 */
	private static final String cadenaModificacionEscalaStr="UPDATE escalas SET codigo=?, nombre=?, observaciones_requeridas=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? and institucion=?";
	
	
	////////
	private static final String cadenaModificacionMostrarSeccionStr="UPDATE escalas_secciones SET mostrar_modificacion=?  WHERE codigo_pk=? ";
	
	private static final String cadenaModificacionMostrarCamposStr="UPDATE campos_parametrizables SET mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? ";
	
	private static final String cadenaModificacionMostrarEscalaStr="UPDATE escalas SET mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? ";
	
	private static final String cadenaModificacionMostrarFactorStr="UPDATE escalas_factores_pred SET mostrar_modificacion=?  WHERE codigo_pk=? ";
	
	
	////////
	private static final String cadenaEliminacionFactorStr="DELETE FROM escalas_factores_pred WHERE codigo_pk=? ";
	
	
	
	/**
	 * 
	 */
	private static final String cadenaEliminacionStr="DELETE FROM escalas WHERE codigo_pk=?";
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaEscalas(Connection con) 
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaEscalas));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @param nombreEscala
	 * @param requiereObservaciones
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEscala(Connection con, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarEscalasStr));
			
			/**
			 * INSERT INTO escalas (
			 * codigo_pk, 
			 * codigo, 
			 * nombre, 
			 * observaciones_requeridas, 
			 * mostrar_modificacion, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_escalas");
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			
			ps.setString(2, codigo+"");
			
			ps.setString(3, nombreEscala);
			
			if(UtilidadTexto.isEmpty(requiereObservaciones))
				ps.setString(4, ConstantesBD.acronimoNo);
			else
				ps.setString(4, requiereObservaciones);
			
			ps.setString(5, ConstantesBD.acronimoSi);
			
			ps.setInt(6, codigoInstitucion);
			
			ps.setString(7, loginUsuario);
			
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(9, UtilidadFecha.getHoraActual());
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @param nombreEscala
	 * @param requiereObservaciones
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEscala(Connection con, String codigoEscala, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarEscalasStr));
			
			/**
			 * INSERT INTO escalas (
			 * codigo_pk, 
			 * codigo, 
			 * nombre, 
			 * observaciones_requeridas, 
			 * mostrar_modificacion, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_escalas");
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			
			ps.setString(2, codigoEscala+"");
			
			ps.setString(3, nombreEscala);
			
			if(UtilidadTexto.isEmpty(requiereObservaciones))
				ps.setString(4, ConstantesBD.acronimoNo);
			else
				ps.setString(4, requiereObservaciones);
			
			ps.setString(5, ConstantesBD.acronimoSi);
			
			ps.setInt(6, codigoInstitucion);
			
			ps.setString(7, loginUsuario);
			
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(9, UtilidadFecha.getHoraActual());
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarSecciones(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSeccionesStr));
			
			int codigoSeccion=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_escalas_secciones");
			
			/**
			 * INSERT INTO escalas_secciones (codigo_pk, nombre, escala, tipo_respuesta, activo, mostrar_modificacion) VALUES (?, ?, ?, ?, ?, ?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSeccion+""));
			
			ps.setString(2, vo.get("nombre")+"");
			
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("escala")+""));
			
			ps.setString(4, vo.get("tipo_respuesta")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("activo")+"");
			
			ps.setString(6, ConstantesBD.acronimoSi);
			
			ps.executeUpdate();
			
			return codigoSeccion;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarFactores(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarFactoresStr));
			
			/**
			 * INSERT INTO escalas_factores_pred (
			 * codigo_pk, 
			 * codigo, 
			 * nombre, 
			 * escala, 
			 * valor_inicial, 
			 * valor_final, 
			 * institucion, 
			 * activo, 
			 * mostrar_modificacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_escalas_fac_pred");
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			
			ps.setString(2, codigo+"");
			
			ps.setString(3, vo.get("nombre")+"");
			
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("escala")+""));
			
			ps.setDouble(5, Utilidades.convertirADouble(vo.get("valor_inicial")+""));
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("valor_final")+""));
			
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(8, ConstantesBD.acronimoNo);
			else
				ps.setString(8, vo.get("activo")+"");
			
			ps.setString(9, ConstantesBD.acronimoSi);
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarCampos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarCamposStr));
			
			int codigoCampo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_campos_param");
			
			/**
			 * INSERT INTO campos_parametrizables (
			 * codigo_pk, 
			 * codigo, 
			 * nombre, 
			 * etiqueta, 
			 * tipo, 
			 * tamanio, 
			 * signo, 
			 * unidad, 
			 * valor_predeterminado, 
			 * maximo, 
			 * minimo, 
			 * decimales, 
			 * columnas_ocupadas, 
			 * orden, 
			 * unico_fila, 
			 * requerido, 
			 * formula, 
			 * institucion, 
			 * tipo_html, 
			 * mostrar, 
			 * mostrar_modificacion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, 4, null, null, '', ?, ?, 2, null, null, ?, ?, '', ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoCampo+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(codigoCampo+""));
			
			ps.setString(3, vo.get("nombre")+"");
			
			ps.setString(4, vo.get("nombre")+"");
			
			ps.setInt(5, ConstantesCamposParametrizables.tipoCampoNumericoDecimal);
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("maximo")+""));
			
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("minimo")+""));
			
			ps.setString(8, ConstantesBD.acronimoSi);
			
			if(UtilidadTexto.isEmpty(vo.get("requerido")+""))
				ps.setString(9, ConstantesBD.acronimoNo);
			else
				ps.setString(9, vo.get("requerido")+"");
			
			ps.setInt(10, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(11, ConstantesCamposParametrizables.campoTipoText);
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(12, ConstantesBD.acronimoNo);
			else
				ps.setString(12, vo.get("mostrar")+"");
			
			ps.setString(13, ConstantesBD.acronimoSi);
			
			ps.setString(14, vo.get("usuario_modifica")+"");
			
			ps.setDate(15, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			ps.setString(16, UtilidadFecha.getHoraActual());
			
			ps.executeUpdate();
			
			return codigoCampo;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarCamposSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarCamposSeccionStr));
			
			/**
			 * INSERT INTO escalas_campos_seccion (codigo_pk, campo_parametrizable, escala_seccion, observaciones_requeridas) VALUES (?, ?, ?, ?)
			 */
			
			
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_escalas_camp_seccion")+""));
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("campo_parametrizable")+""));
			
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("escala_seccion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("observaciones_requeridas")+""))
				ps.setString(4, ConstantesBD.acronimoNo);
			else
				ps.setString(4, vo.get("observaciones_requeridas")+"");
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> consultaDetalleEscala(Connection con, String escala, String institucion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleEscala));
			
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public static HashMap<String, Object> detalleCampos(Connection con, String escala) 
	{
		String consultaCampos="SELECT 	ecs.escala_seccion as seccion, " +
										"ecs.codigo_pk as codigopkcamposseccion, " +
										"ecs.observaciones_requeridas as obsevreq, " +
										"cp.codigo_pk as codigopkcampo, " +
										"cp.nombre as nombrecampo, " +
										"cp.nombre as nombrecampoantiguo, " +
										"cp.requerido as indrequerido, " +
										"cp.minimo as valorminimo, " +
										"cp.maximo as valormaximo, " +
										"cp.mostrar as activo "+
								"FROM escalas_secciones es " +
								"inner join escalas_campos_seccion ecs on(ecs.escala_seccion=es.codigo_pk) " +
								"inner join campos_parametrizables cp on(cp.codigo_pk=ecs.campo_parametrizable) "+
								"where es.escala=? and cp.mostrar_modificacion='"+ConstantesBD.acronimoSi+"' ";
		
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaCampos));
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> detalleFactores(Connection con, String escala, String institucion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaFactores));
			
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public static HashMap<String, Object> detalleSeccion(Connection con, String escala) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaSecciones));
			
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarCampos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionCamposStr));
			
			/**
			 * UPDATE campos_parametrizables SET 
			 * nombre=?, 
			 * requerido=?, 
			 * minimo=?, 
			 * maximo=?, 
			 * mostrar=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?  
			 * WHERE codigo_pk=? and institucion=?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("requerido")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("requerido")+"");
			
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("minimo")+""));
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("maximo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("mostrar")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("mostrar")+"");
			
			ps.setString(6, vo.get("usuario_modifica")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.setDouble(9, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			ps.setInt(10, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarCamposSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionCamposSeccion));
			
			if(UtilidadTexto.isEmpty(vo.get("observaciones_requeridas")+""))
				ps.setString(1, ConstantesBD.acronimoNo);
			else
				ps.setString(1, vo.get("observaciones_requeridas")+"");
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarEscala(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionEscalaStr));
			
			/**
			 * UPDATE escalas SET 
			 * codigo=?, 
			 * nombre=?, 
			 * observaciones_requeridas=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?  
			 * WHERE codigo_pk=? and institucion=?
			 */
			
			ps.setString(1, vo.get("codigo")+"");
			ps.setString(2, vo.get("nombre")+"");
			if(UtilidadTexto.isEmpty(vo.get("observaciones_requeridas")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("observaciones_requeridas")+"");
			
			ps.setString(4, vo.get("usuario_modifica")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarFactores(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionFactoresStr));
			
			/**
			 * UPDATE escalas_factores_pred SET codigo=?, nombre=?, valor_inicial=?, valor_final=?, activo=?  WHERE codigo_pk=? and institucion=?
			 */
			
			ps.setString(1, vo.get("codigo")+"");
			ps.setString(2, vo.get("nombre")+"");
			ps.setDouble(3, Utilidades.convertirADouble(vo.get("valor_inicial")+""));
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("valor_final")+""));
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(5, ConstantesBD.acronimoNo);
			else
				ps.setString(5, vo.get("activo")+"");
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionSeccionStr));
			
			/**
			 * UPDATE escalas_secciones SET nombre=?, tipo_respuesta=?, activo=?  WHERE codigo_pk=? 
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			ps.setString(2, vo.get("tipo_respuesta")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("activo")+"");
			
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	public static boolean eliminarEscala(Connection con, String escala) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr));
			ps.setDouble(1, Utilidades.convertirADouble(escala));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarCampos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarCamposStr));
			
			/**
			 * UPDATE campos_parametrizables SET mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? 
			 */
			
			ps.setString(1, vo.get("mostrar_modificacion")+"");
			
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4, UtilidadFecha.getHoraActual());
			
			ps.setDouble(5, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarEscala(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarEscalaStr));
			
			ps.setString(1, vo.get("mostrar_modificacion")+"");
			
			ps.setString(2, vo.get("usuario_modifica")+"");
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4, UtilidadFecha.getHoraActual());
			
			ps.setDouble(5, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarFactor(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarFactorStr));
			
			ps.setString(1, vo.get("mostrar_modificacion")+"");
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMostrarSeccion(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarSeccionStr));
			
			ps.setString(1, vo.get("mostrar_modificacion")+"");
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigo_pk")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactor
	 * @return
	 */
	public static boolean eliminarFactor(Connection con, String codigoFactor) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarFactorStr));
			
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setDouble(2, Utilidades.convertirADouble(codigoFactor));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	public static boolean eliminarCampos(Connection con, String codigoCampo, String loginUsuario) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarCamposStr));
			
			/**
			 * UPDATE campos_parametrizables SET mostrar_modificacion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? 
			 */
			
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setString(2, loginUsuario);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setDouble(5, Utilidades.convertirADouble(codigoCampo));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public static boolean eliminarSecciones(Connection con, String codigoSeccion) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionMostrarSeccionStr));
			
			ps.setString(1, ConstantesBD.acronimoNo);
			ps.setDouble(2, Utilidades.convertirADouble(codigoSeccion));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEscalasDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	

}
