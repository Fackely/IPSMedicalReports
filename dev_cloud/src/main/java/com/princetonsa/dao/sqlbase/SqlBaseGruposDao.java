/*
 * @(#)SqlBaseGruposDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.dao.DaoFactory;


/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para grupos
 *
 * @version 1.0, Sep 06 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseGruposDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseGruposDao.class);
	
	/**
	 * Consulta la información de grupos
	 */
	private static String listadoGruposStr= 	"SELECT " +
																"dg.codigo AS codigo_p_k_grupo, " +
																"g.esquema_tarifario AS codigo_esquema_tarifario, " +
																"et.nombre AS nombre_esquema_tarifario, " +
																"dg.grupo AS grupo, " +
																"dg.asocio AS codigo_asocio, " +
																"ta.codigo_asocio AS acronimo_tipo_servicio, " +
																"ta.nombre_asocio AS nombre_tipo_servicio, " +
																"getnombretipoasocio(ta.codigo) AS desc_tipo_servicio, " +
																"dg.tipo_liquidacion AS codigo_tipo_liquidacion, " +
																"tl.nombre AS nombre_tipo_liquidacion, " +
																"CASE WHEN dg.unidades<=0 THEN '' ELSE dg.unidades ||'' END AS unidades, " +
																"dg.valor, " +
																"g.convenio as convenio, " +
																"getnombreconvenio(g.convenio) AS nombreconvenio," +
																"dg.tipo_servicio as tipo_servicio, " +
																"getnombretiposervicio(dg.tipo_servicio) as nom_tipo_servicio," +
																"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS esta_b_d,  " +
																"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS es_eliminada, " +
																"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS usando, "+
																" dg.liquidar_por As liquidarpor "+
																"FROM grupos g " +
																"INNER JOIN detalle_grupos dg on (g.codigo=dg.codigo_grupo) " +
																"LEFT OUTER JOIN esquemas_tarifarios et ON (et.codigo=g.esquema_tarifario) " +
																"INNER JOIN tipos_asocio ta ON (ta.codigo=dg.asocio) " +
																"INNER JOIN tipos_liquidacion_soat tl ON (tl.codigo=dg.tipo_liquidacion) ";

	private static String listadoGruposStr2= 	"SELECT " +
																"dg.codigo AS codigo_p_k_grupo, " +
																
																// para todo soat
																"coalesce(g.esq_tar_general,"+ConstantesBD.codigoNuncaValido+") AS codigo_esq_tar_general, " +
																"coalesce(getnomesqporcentaje(g.esq_tar_general,'true'),'') AS nombre_esq_tar_general, " +
																
																"coalesce(g.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") AS codigo_esquema_tarifario, " +
																"coalesce(getnomesqporcentaje(g.esquema_tarifario,'false'),'') AS nombre_esquema_tarifario, " +
																"dg.grupo AS grupo, " +
																"dg.asocio AS codigo_asocio, " +
																"ta.codigo_asocio AS acronimo_tipo_servicio, " +
																"ta.nombre_asocio AS nombre_tipo_servicio, " +
																"getnombretipoasocio(ta.codigo) AS desc_tipo_servicio, " +
																"dg.tipo_liquidacion AS codigo_tipo_liquidacion, " +
																"tl.nombre AS nombre_tipo_liquidacion, " +
																"CASE WHEN dg.unidades<=0 THEN '' ELSE dg.unidades ||'' END AS unidades, " +
																"dg.valor, " +
																"g.convenio as convenio, " +
																"getnombreconvenio(g.convenio) AS nombreconvenio," +
																"dg.tipo_servicio as tipo_servicio, " +
																"getnombretiposervicio(dg.tipo_servicio) as nom_tipo_servicio," +
																"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS esta_b_d,  " +
																"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS es_eliminada, " +
																"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS usado, " +
																" dg.liquidar_por As liquidarpor "+
																"FROM grupos g " +
																"INNER JOIN detalle_grupos dg on (g.codigo=dg.codigo_grupo) " +
																"INNER JOIN tipos_asocio ta ON (ta.codigo=dg.asocio) " +
																"INNER JOIN tipos_liquidacion_soat tl ON (tl.codigo=dg.tipo_liquidacion) " ;
																
																

	
	
	/**
	 * 
	 */
	private static String cadenaConsultaDetCodigosGrupos="SELECT tarof.codigo as codigotarifario,tarof.nombre as nomtarifario,coalesce(dcg.codigo,'') as valor,case when dcg.codigo is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as estabd from tarifarios_oficiales tarof left outer join det_codigos_grupos dcg on(dcg.codigo_tarifario=tarof.codigo and dcg.cod_detalle_grupo=? )";
	
	/**
	 * order by de grupos
	 */
	private static String orderByGruposStr=	" ORDER BY grupo,  nombre_tipo_servicio";
	
	/**
	 * Order by por grupos Tarea 1047
	 */
	private static String strOrderBy = " ORDER BY nom_tipo_servicio, grupo ";
	
	/**
	 * Modifica un grupo
	 */
	private static String modificarGruposStr= 	"UPDATE detalle_grupos " +
																	"SET grupo=?, " +
																	"asocio=?, " +
																	"tipo_liquidacion=?, " +
																	"unidades=?, " +
																	"valor=?, " +
																	"tipo_servicio=?," +
																	"liquidar_por=? " +
																	"WHERE " +
																	"codigo=? ";
	
	/**
	 * Modificar vigencias de los convenios de los grupos
	 */
	private static String modificarGrupoMaestroStr="UPDATE grupos SET " +
																		"fecha_inicial=?," +
																		"fecha_final=?" +
																" WHERE " +
																		"convenio=? AND " +
																		"codigo=?";
																	
	/**
	 * Elimina un grupo
	 */
	private final static String eliminarGrupoStr=	"DELETE FROM detalle_grupos WHERE codigo= ?";
	
	/**
	 * Cadena para insertar el detalle de los codigos por grupo
	 */
	private final static String insertarDetCodigosGruposStr="INSERT INTO det_codigos_grupos (cod_detalle_grupo,codigo_tarifario,codigo) VALUES (?,?,?)";
	
	/**
	 * Cadena para la actualizacion de los codigos por grupo
	 */
	private final static String actualizarDetaCodigosGrupoStr="UPDATE det_codigos_grupos SET codigo=? WHERE cod_detalle_grupo=? and codigo_tarifario=?";
	
	/**
	 * Cadena para la actualizacion de los codigos por grupo
	 */
	private final static String eliminarDetaCodigosGrupoStr="DELETE FROM det_codigos_grupos WHERE cod_detalle_grupo=?";
	
	/**
	 * 
	 */
	private final static String eliminarGrupoMaestroStr="DELETE FROM grupos WHERE codigo=? AND convenio=?";
	
	/**
	 * 
	 */
	private final static String insertarGruposStr="insert into grupos(codigo," +
														" esquema_tarifario," +
														" convenio," +
														" fecha_inicial," +
														" fecha_final," +
														" institucion," +
														" fecha_modifica," +
														" hora_modifica," +
														"usuario_modifica," +
														
														// para todo soat
														"esq_tar_general)" +
														
														
														
													"values(?,?,?,?,?,?,?,?,?,?) "; 
	
    /**
	 * Inserta un grupo
	 */
	private final static String insertarDetalleGruposStr= 	"INSERT INTO detalle_grupos " +
																			"(codigo, " +
																			" codigo_grupo," +
																			"grupo, " +
																			"asocio, " +
																			"tipo_liquidacion, " +
																			"unidades, " +
																			"valor, " +
																			"tipo_servicio," +
																			"fecha_modifica, " +
																			"hora_modifica," +
																			"usuario_modifica," +
																			"liquidar_por) " +
																			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?,?)";
	
	/**
	 * Consulta los rangos de fecha por el convenio dado
	 */
	private final static String consultarListaVigenciasConvenioStr="SELECT codigo as codigogrupo, to_char(fecha_inicial,'DD/MM/YYYY') as fechainicial, to_char(fecha_final,'DD/MM/YYYY') as fechafinal,'"+ConstantesBD.acronimoSi+"' as estabd FROM grupos WHERE convenio=?";
	
	/**
	 * 
	 */
	private final static String consultarcodigoMaestro="SELECT codigo FROM grupos WHERE institucion = ?";

	
	
	/**
	 * Método para la insercion del detalle de grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @param fechaFinal 
	 * @param fechaInicial 
	 * @param usuario 
	 * @param insertarStr
	 * @return
	 */
	public static int   insertarXesquemaTarifario(		Connection con,
														int grupo,
														int codigoAsocio,
														int codigoEsquemaTarifario,
														int codigoTipoLiquidacion,
														String unidades,
														double valor,
														int codigoInstitucion,
														String convenio,
														String tipoServicio, 
														String fechaInicial, 
														String fechaFinal, 
														String usuario,
														int codigoGrupo,
														boolean esquemaGeneral,
														String liquidarPor
	)
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			if(codigoGrupo<=0)
				codigoGrupo=insertarVigenciaGrupo(con,codigoEsquemaTarifario,convenio,fechaInicial,fechaFinal,codigoInstitucion,usuario,esquemaGeneral);
			if(codigoGrupo>0)
			{
				int codigoDetalleGrupo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_detalle_grupos");
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoDetalleGrupo);
				ps.setInt(2, codigoGrupo);
				ps.setInt(3, grupo);
				ps.setInt(4, codigoAsocio);
				ps.setInt(5, codigoTipoLiquidacion);
				ps.setString(6, unidades);
				ps.setDouble(7, valor);
				if(UtilidadTexto.isEmpty(tipoServicio))
					ps.setObject(8, null);
				else
					ps.setString(8, tipoServicio);
				ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				ps.setString(10, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual(con)));
				ps.setString(11, usuario);
				//liquidar por
				ps.setObject(12, liquidarPor);
				if(ps.executeUpdate()>0)
				    return codigoDetalleGrupo;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseGruposDao "+e.toString() );
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param convenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @param usuario 
	 * @param esquemaGeneral 
	 * @return
	 */
	public static int insertarVigenciaGrupo(Connection con, int codigoEsquemaTarifario, String convenio, String fechaInicial, String fechaFinal, int codigoInstitucion, String usuario, boolean esquemaGeneral) 
	{
		int codigoMaestro = ConstantesBD.codigoNuncaValido;
		try
		{
			boolean puedoInsertar = true;
			
			if(codigoEsquemaTarifario>0)
			{
				String consulta = consultarcodigoMaestro;
				if(esquemaGeneral)
					consulta += " AND esq_tar_general = "+codigoEsquemaTarifario;
				else
					consulta += " AND esquema_tarifario = "+codigoEsquemaTarifario;
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoInstitucion);
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					codigoMaestro = rs.getInt("codigo");
					puedoInsertar = false; //ya no se puede insertar porque ya existe un encabezado
				}
			}
			
			
			//Si se puede insertar se continúa
			if(puedoInsertar)
			{
			
				codigoMaestro=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_grupos");
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoMaestro);
				
				
				// para todo soat
				if(codigoEsquemaTarifario>0)
				{
					//if (codigoEsquemaTarifario != 3)
					//verificar si el codigo tarifario seleccionado es un codigo tarifario general referente a todos soat
					if (codigoEsquemaTarifario != ConstantesBD.codigoEsqTarifarioGeneralTodosSoat)
					{
						ps.setInt(2, codigoEsquemaTarifario);
						//para todo soat
						ps.setObject(10, null);					
					}				
					else
					{	// es un codigo tarifario general para todos soat.
						ps.setInt(10, codigoEsquemaTarifario);
						//para todo soat
						ps.setObject(2, null);
					}
						
				}
				else
				{
					ps.setObject(2, null);
					ps.setNull(10,Types.INTEGER);
				}
				if(UtilidadTexto.isEmpty(convenio))
					ps.setObject(3, null);
				else
					ps.setInt(3, Utilidades.convertirAEntero(convenio));
				if(UtilidadTexto.isEmpty(fechaInicial))
					ps.setObject(4, null);
				else 
					ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
				if(UtilidadTexto.isEmpty(fechaFinal))
					ps.setObject(5, null);
				else 
					ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
				ps.setInt(6, codigoInstitucion);
				ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual(con)));
				ps.setString(9, usuario);
				if(ps.executeUpdate()<=0)
					codigoMaestro = ConstantesBD.codigoNuncaValido;
			}
		}
		catch(SQLException e)
		{
			logger.info("Error insertando la vigencia de la tabla grupos: "+e);
			codigoMaestro = ConstantesBD.codigoNuncaValido;
		}
	    return codigoMaestro;
	}

	/**
	 * Método para la modioficacion de un grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoPKGrupo
	 * @return
	 */
	public static boolean modificar(		Connection con,
														int grupo,
														int codigoAsocio,
														int codigoTipoLiquidacion,
														String unidades,
														double valor,
														int codigoPKGrupo,
														String tipoServicio,
														String liquidarPor
													) 
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
				
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, grupo);
			ps.setInt(2, codigoAsocio);
			ps.setInt(3, codigoTipoLiquidacion);
			ps.setString(4, unidades);
			ps.setDouble(5, valor);
			if(UtilidadTexto.isEmpty(tipoServicio))
				ps.setObject(6, null);
			else
				ps.setString(6, tipoServicio);
			//liquidar por
			ps.setObject(7, liquidarPor);
			
			ps.setInt(8, codigoPKGrupo);
			if(ps.executeUpdate()>0)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseGruposDao "+e.toString());
			return false;			
		}	
	}
	
	/**
	 * Metodo para eliminar un grupo
	 * @param con
	 * @param codigoPKGrupo
	 * @return
	 */
	public static boolean eliminar(	Connection con, 
												int codigoPKGrupo
												) 
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarGrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPKGrupo);
			if(ps.executeUpdate()>0)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBaseGruposDao "+e.toString());
			return false;			
		}	
	}
	
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public static HashMap listadoGrupos(Connection con, int codigoInstitucion, int codigoEsquemaTarifario,int codigoGrupo, boolean esEsquemaGeneral)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		try
		{
			String consulta=listadoGruposStr2+" WHERE g.institucion=?  ";

	        if(codigoEsquemaTarifario<0)
	        {
	        	/**
	        	 * enviar el codigo del grupo, seleccionado desde las vigencias.
	        	 */
	        	if(codigoGrupo<0)
	        	{
	        		consulta=consulta+" "+strOrderBy;
	        	}
	        	else
	        	{
	        		consulta=consulta+" AND g.codigo="+codigoGrupo+" "+strOrderBy;
	        	}
	        }
	        else
	        {
	        	// para todos soat	        	
	        	if (esEsquemaGeneral)
	        	{
	        		// consultar todos los registros de soat.
	        		consulta=consulta+" AND g.esq_tar_general = ? AND g.esquema_tarifario is null " +strOrderBy;
	        	}	        		
	        	else
	        	{
	        		consulta=consulta+" AND g.esquema_tarifario=? AND g.esq_tar_general is null " +strOrderBy;
	        	}
	        }
	        logger.info("--->"+consulta);
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			 logger.info("isntitucion  -->"+codigoInstitucion);
			//para todo soat
			if(codigoEsquemaTarifario>0)
			{
			    cargarStatement.setInt(2, codigoEsquemaTarifario);
			    logger.info("isntitucion  -->"+codigoEsquemaTarifario);
			}
				
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()),true,true);
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				PreparedStatementDecorator psTar= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetCodigosGrupos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTar.setObject(1, mapa.get("codigoPKGrupo_"+i)+"");
				ResultSetDecorator rs=new ResultSetDecorator(psTar.executeQuery());
				while (rs.next())
				{
					String codTarifario=rs.getObject("codigotarifario")+"";
					mapa.put("valorTarifario_"+i+"_"+codTarifario, rs.getObject("valor"));
					mapa.put("estabd_"+i+"_"+codTarifario, rs.getObject("estabd"));
				}
			}
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de : SqlBaseGruposDao "+e.toString());
			e.printStackTrace();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	/**
	 * metodo para la busqueda avanzada de los grupos 
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap busquedaAvanzadaGrupos(	Connection con, 
	        																	int grupo,
																				int codigoAsocio,
																				int codigoTipoLiquidacion,
																				int codigoInstitucion,
																				String tipoServicio,
																				int codigoEsquemaTarifario,
																				String convenio,
																				String fechainicial,
																				String fechafinal,
																				String liquidarPor,
																				String cups
																				)
	{
		HashMap mapa=new HashMap();
	    String consulta=listadoGruposStr;
	    String where=" WHERE g.institucion=?  ";
	    String inner="";
	    
	    if(codigoEsquemaTarifario>0)
	    	where+=" AND g.esquema_tarifario="+codigoEsquemaTarifario;
	    if(!UtilidadTexto.isEmpty(convenio))
	    	where+=" AND g.convenio="+convenio;
	    if(grupo>0)
	    	where+=" AND dg.grupo = "+grupo;
	    if(codigoAsocio>0)
	    	where+= " AND dg.asocio= "+codigoAsocio;
	    if(codigoTipoLiquidacion>0)
	    	where+=" AND dg.tipo_liquidacion = "+codigoTipoLiquidacion;
	    if(!UtilidadTexto.isEmpty(tipoServicio))
	    	where+=" AND dg.tipo_servicio='"+tipoServicio+"'";
	    if(!UtilidadTexto.isEmpty(fechainicial))
	    	where+=" AND g.fecha_inicial='"+fechainicial+"'";
	    if(!UtilidadTexto.isEmpty(fechafinal))
	    	where+=" AND g.fecha_final='"+fechafinal+"'";
	    
	    if (UtilidadCadena.noEsVacio(liquidarPor))
	    	where+=" AND dg.liquidar_por='"+liquidarPor+"'";
	    
	    if (UtilidadCadena.noEsVacio(cups))
	    {
	    	inner+=" INNER JOIN det_codigos_grupos dcg ON (dcg.cod_detalle_grupo=dg.codigo and dcg.codigo_tarifario="+ConstantesBD.codigoTarifarioCups+")";
	    	where+=" AND dcg.codigo='"+cups+"'";
	    }
	    
	    where+=orderByGruposStr;
	    consulta+=inner+where;
	    logger.info("\n la consulta de la busqueda avanzada es "+consulta);
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()),true,true);
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				PreparedStatementDecorator psTar= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetCodigosGrupos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTar.setObject(1, mapa.get("codigoPKGrupo_"+i)+"");
				ResultSetDecorator rs=new ResultSetDecorator(psTar.executeQuery());
				while (rs.next())
				{
					String codTarifario=rs.getObject("codigotarifario")+"";
					mapa.put("valorTarifario_"+i+"_"+codTarifario, rs.getObject("valor"));
					mapa.put("estabd_"+i+"_"+codTarifario, rs.getObject("estabd"));
				}
			}
		}	
		catch(SQLException e)
		{
			logger.warn(e+" Error en la busqueda Avanzada de : SqlBaseGruposDao "+e.toString());
		}
		return mapa;
	}
	
	/**
	 * Mapa que carga los tipos de servicios
	 * @param con
	 * @return
	 */
	public static ArrayList listarTiposServicio(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		String consulta="SELECT acronimo,nombre FROM tipos_servicio WHERE es_qx="+ValoresPorDefecto.getValorTrueParaConsultas(); 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("acronimo",rs.getObject(1));
				mapa.put("nombre",rs.getObject(2));
				resultado.add(i, mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * Metodo que lista los tarifarios oficiales para ser parametrizados en lugar de solo pedir el codigo soat y cups
	 * @param con
	 * @return
	 */
	public static ArrayList listarTarifariosOficiales(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado=new ArrayList<HashMap<String, Object>>();
		String consulta="SELECT codigo,nombre FROM tarifarios_oficiales ORDER BY nombre";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject(1));
				mapa.put("nombre",rs.getObject(2));
				resultado.add(i, mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	/**
	 * Metodo para obtener el ultimo codigo ingresado de grupo en la bd
	 * @param con
	 * @return
	 */
	public static int codigoUltimoGrupo(Connection con)
	{
		PreparedStatementDecorator ps;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement("SELECT max(codigo) FROM detalle_grupos",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return Integer.parseInt(rs.getString("max"));
		}
		catch (SQLException e)
		{
			logger.error("Error hallando el último artículo "+e);
			return 0;
		}
		
	}

	public static boolean actualizarDetCodigosGrupos(Connection con, HashMap<String, Object> vo) 
	{
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(vo.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.isEmpty(vo.get("valorTarifario_"+i)+"")&&UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					String cadenaEliminacionCodigoIndividual=eliminarDetaCodigosGrupoStr+" AND codigo_tarifario=?";
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionCodigoIndividual,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setObject(1, vo.get("codigoPKGrupo")+"");
					ps.setObject(2, vo.get("codigotarifario_"+i)+"");
					ps.executeUpdate();
				}
				else if(!UtilidadTexto.isEmpty(vo.get("valorTarifario_"+i)+"")&&UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDetaCodigosGrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setObject(1,  vo.get("valorTarifario_"+i)+"");
					ps.setObject(2, vo.get("codigoPKGrupo")+"");
					ps.setObject(3, vo.get("codigotarifario_"+i)+"");
					ps.executeUpdate();
	
				}
				if(!UtilidadTexto.isEmpty(vo.get("valorTarifario_"+i)+"")&&!UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetCodigosGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setObject(1, vo.get("codigoPKGrupo")+"");
					ps.setObject(2, vo.get("codigotarifario_"+i)+"");
					ps.setObject(3,  vo.get("valorTarifario_"+i)+"");
					ps.executeUpdate();
				}
			}
			return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	public static HashMap listadoGruposLLave(Connection con, String tempoCodPKGrupo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String consulta=listadoGruposStr+" WHERE dg.codigo=?  ";
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setObject(1, tempoCodPKGrupo);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()),true,true);
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				PreparedStatementDecorator psTar= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetCodigosGrupos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTar.setObject(1, mapa.get("codigoPKGrupo_"+i)+"");
				ResultSetDecorator rs=new ResultSetDecorator(psTar.executeQuery());
				while (rs.next())
				{
					String codTarifario=rs.getObject("codigotarifario")+"";
					mapa.put("valorTarifario_"+i+"_"+codTarifario, rs.getObject("valor"));
					mapa.put("estabd_"+i+"_"+codTarifario, rs.getObject("estabd"));
				}
			}
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de : SqlBaseGruposDao "+e.toString());
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean eliminarCodigosGruposTotal(Connection con,HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetaCodigosGrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, vo.get("codigoPKGrupo")+"");
			if(ps.executeUpdate()>0)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en eliminar de datos: SqlBaseGruposDao "+e.toString());
			return false;			
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static HashMap listarVigenciasConvenio(Connection con,int convenio)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarListaVigenciasConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, convenio);
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
	 * @param con
	 * @param vo
	 */
	public static boolean modificarVigenciasConvenio(Connection con,HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarGrupoMaestroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainicial")+""));
			ps.setObject(2, UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinal")+""));
			ps.setObject(3, vo.get("convenio"));
			ps.setObject(4, vo.get("codigogrupo"));
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param codigo
	 * @throws SQLException
	 */
	public static boolean eliminarGrupoMaestro(Connection con,int convenio,int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarGrupoMaestroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigo);
			ps.setObject(2, convenio);
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
