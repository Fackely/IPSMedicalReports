package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.PreparacionTomaInventario;

public class SqlBasePreparacionTomaInvDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * Consulta todos los grupos segun el codigo de una clase
	 */
	private static String consultarGruposStr= "SELECT codigo, nombre FROM grupo_inventario WHERE clase = ? ORDER BY nombre";
	
	/**
	 * Consulta todos los subgrupos segun el codigo del clase y el codigo del grupo
	 */
	private static String consultarSubgruposStr= "SELECT codigo, nombre FROM subgrupo_inventario WHERE clase = ? AND grupo = ? ORDER BY nombre";
	
	/**
	 * Consulta para filtrar articulos segun lo parametros dados
	 */
	private static String filtrarArticulosStr = "SELECT DISTINCT  " +
													"a.codigo AS codigo " +
												"FROM " +
													"articulo a, " +
													"subgrupo_inventario si, " +
													"det_articulos_por_almacen dapa, " +
													"articulos_por_almacen apa, " +
													"clase_inventario ci, " +
													"grupo_inventario gi  " +
												"WHERE " +
													"a.subgrupo = si.codigo " +
													"AND a.estado=  " +ValoresPorDefecto.getValorTrueParaConsultas()+" "+
													"AND a.codigo = dapa.articulo " +
													"AND dapa.codigo_art_por_almacen = apa.codigo_pk  " +
													"AND ci.codigo = gi.clase " +
													"AND gi.codigo = si.grupo "; 
	
	/**
	 * Cadena para insertar la informacion de cada articulo preparado en la tabla preparacion_tom_inventario
	 */
	private static String insertarPreparacionStr = "INSERT INTO " +
														"preparacion_toma_inventario " +
														"(institucion, " +
														"centro_atencion, " +
														"almacen, " +
														"seccion, " +
														"subseccion, " +
														"clase, " +
														"grupo, " +
														"subgrupo, " +
														"articulo, " +
														"codigo_dapa, " +
														"lote, " +
														"existencia, " +
														"fecha_toma, " +
														"hora_toma, " +
														"usuario_modifica, " +
														"fecha_modifica," +
														"hora_modifica, " +
														"codigo_preparacion," +
														"codigo) " +
													"VALUES " +
														"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	
	
	private static String actualizarPreparacionStr= "UPDATE " +
														"preparacion_toma_inventario " +
													"SET " +
														"institucion=?, " +
														"centro_atencion=?, " +
														"almacen=?, " +
														"seccion=?, " +
														"subseccion=?, " +
														"clase=?, " +
														"grupo=?, " +
														"subgrupo=?, " +
														"articulo=?, " +
														"codigo_dapa=?, " +
														"lote=?, " +
														"existencia=?, " +
														"fecha_toma=?, " +
														"hora_toma=?, " +
														"usuario_modifica=?, " +
														"codigo_preparacion=?, " +
														"fecha_modifica=CURRENT_DATE, " +
														"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
													"WHERE " +
														"articulo=? and almacen=? and seccion=? and subseccion=? AND estado='PEN' AND codigo_dapa=?";
	
	// --------------- METODOS
	
	/** 
	 * Consulta todos los grupos segun una el codigo de clase
	 * @param con
	 * @param seccion
	 * @return
	 */	
	public static HashMap consultarGrupos(Connection con, int clase){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/** 
	 * Consulta todos los subgrupos segun el codigo del clase y el codigo del grupo
	 * @param con
	 * @param seccion
	 * @return
	 */	
	public static HashMap consultarSubgrupos(Connection con, int clase, int grupo){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarSubgruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	public static HashMap filtrarArticulos(Connection con, PreparacionTomaInventario pti){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String filtro = filtrarArticulosStr;
		String codigosFiltrados;
	
		String consultaArticulosPreparados="";
		try
		{
			if(pti.getAlmacen()>0)
				filtro+= " AND apa.almacen = "+pti.getAlmacen();
			
			if(pti.getCentroAtencion()>0)
				filtro+= " AND apa.centro_atencion = "+pti.getCentroAtencion();
			
			if (pti.getCodigosArticulos().equals("")){	
				
				if (!pti.getSeccion().equals("") && !pti.getSeccion().equals("null")){
					filtro+=" AND apa.seccion IN ("+pti.getSeccion()+")";
					if (pti.getSubseccion()>0){
						filtro+= " AND apa.subseccion = "+pti.getSubseccion();
					}
				}
				
				if (pti.getClase() > 0){
					filtro += " AND ci.codigo = "+pti.getClase();
					if (pti.getGrupo() >0){
						filtro += " AND gi.codigo = "+pti.getGrupo();
						if (pti.getSubgrupo()>0){
							filtro += " AND si.codigo = "+pti.getSubgrupo();
						}
					}
				}	
				logger.info("Sen >>>>>>"+filtro);
				consultaArticulosPreparados="SELECT articulo as codigo from preparacion_toma_inventario where articulo IN ("+filtro+")";				
				
			} 
			else {
				codigosFiltrados = pti.getCodigosArticulos();
				consultaArticulosPreparados="SELECT articulo as codigo from preparacion_toma_inventario where articulo IN ("+filtro+")";
			}
			
			
			logger.info("consulta cod preparados"+consultaArticulosPreparados);
			PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPreparados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap codi=new HashMap();
			codi.put("numRegistros", "0");
			String codigosPreparados=crearCadena(codi=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery())));
			
			
			String consultarArticulosStr = "SELECT " +
												"t.descripcion, " +
												"t.codigo, " +
												"getunidadmedidaarticulo(t.codigo) as unidad_medida, " +
												"t.codigo_interfaz, " +
												"coalesce(t.lote, 'No aplica') as lote, " +
												"t.codigo_lote, " +
												"coalesce(t.fecha_vencimiento, 'No aplica') as fecha_vencimiento, " +
												"t.existencias, " +
												"t.concentracion, " +
												"getnomformafarmaceutica(t.forma_farmaceutica) as forma_farmaceutica, " +
												"getnaturalezaarticulo(t.codigo) as naturaleza, " +
												"t.subgrupo AS subgrupocon, " +
												"se.descripcion || ' - ' || " + "subs.descripcion AS seccion_subseccion, " +
												"se.codigo_pk AS seccion, " +
												"subs.codigo_subseccion AS subseccion, " +
												"si.subgrupo, " +
												"si.grupo, " +
												"si.clase, "+
												"dapa.codigo_det_pk " +
											"FROM " +
											"((" +
												"SELECT " +
													"a.descripcion, " +
													"a.codigo, " +
													"a.unidad_medida, " +
													"a.codigo_interfaz, " +
													"l.lote, " +
													"l.codigo as codigo_lote, " +
													"to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento, " +
													"l.existencias, " +
													"a.concentracion, " +
													"a.forma_farmaceutica, " +
													"a.naturaleza, " +
													"a.subgrupo " +
												"FROM " +
													"articulo a " +
												"INNER JOIN " +
													"articulo_almacen_x_lote l ON a.codigo=l.articulo " +
												"WHERE " +
													"a.maneja_lotes='S' " +
													"AND a.codigo in ("+filtro+") " +
													" AND  l.almacen="+pti.getAlmacen()+" "+
													") " +
												"UNION" +
												" (" +
												"SELECT " +
													"a.descripcion, " +
													"a.codigo, " +
													"a.unidad_medida, " +
													"a.codigo_interfaz, " +
													"'No aplica' as lote, " +
													"-1 as codigo_lote, " +
													"'No aplica' as fecha_vencimiento, " +
													"ex.existencias, " +
													"a.concentracion,  " +
													"a.forma_farmaceutica, " +
													"a.naturaleza, " +
													"a.subgrupo  " +
												"FROM " +
													"articulo a, " +
													"articulos_almacen ex " +
												"WHERE " +
													"ex.articulo=a.codigo " +
													"AND a.maneja_lotes='N' " +
													"AND a.codigo IN ("+filtro+") AND ex.almacen="+pti.getAlmacen()+" "+
											")) t " +
											"INNER JOIN " +
												"det_articulos_por_almacen dapa ON(dapa.articulo=t.codigo) " +
											"INNER JOIN " +
												"articulos_por_almacen apa ON(apa.codigo_pk = dapa.codigo_art_por_almacen) " +
											"INNER JOIN " +
												"secciones se ON (se.codigo_pk = apa.seccion) " +
											"INNER JOIN " +
												"subsecciones subs ON(subs.codigo_pk_seccion=apa.seccion AND subs.codigo_subseccion = apa.subseccion) " +
											"INNER JOIN subgrupo_inventario si on(si.codigo=t.subgrupo)" +
												"where 1=1 and apa.almacen="+pti.getAlmacen()+" ";
											
			
			if (!pti.getSeccion().equals("") && !pti.getSeccion().equals("null")){
				consultarArticulosStr+=" AND apa.seccion IN ("+pti.getSeccion()+")";
				if (pti.getSubseccion()>0){
					consultarArticulosStr+= " AND apa.subseccion = "+pti.getSubseccion();
				}
			}
			
			
			consultarArticulosStr +=" ORDER BY seccion_subseccion,descripcion";
			
			
			logger.info(">>>>>><"+consultarArticulosStr);
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
			mapa.put("codigos_preparados", codigosPreparados);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	private static String crearCadena(HashMap mapa) {
    	String cadena = "";
    	int x;
    	for(x=0; x<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); x++){
    		cadena += mapa.get("codigo_"+x);
    		cadena += ",";
    	}
    	
    	
    	return cadena;
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static boolean confirmarPreparacion(Connection con, PreparacionTomaInventario pti)
	{
		try
		{
			HashMap art=new HashMap();
			art.put("numRegistros", "0");
			
			
			int codPrepMax=CodigoPreparacionMax(con, pti);
				
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarPreparacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			

			for (int x=0; x<Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("numRegistros").toString()); x++)
			{
				String Consultamodificar="select pti.articulo from preparacion_toma_inventario pti left join articulo_almacen_x_lote aal on (pti.articulo=aal.articulo) where pti.articulo=? and pti.almacen=? and pti.seccion=? and pti.subseccion=? and pti.codigo_dapa=? and estado='PEN'";
				
				String actualizarPreparacionStrAux = actualizarPreparacionStr;
				
				
				
				
				if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")>0)
					{
				
					Consultamodificar+=" and pti.lote=?";
					}
				PreparedStatementDecorator ps3 =  new PreparedStatementDecorator(con.prepareStatement(Consultamodificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")>0)
				{
					actualizarPreparacionStrAux+=" AND lote=?";
				}
				PreparedStatementDecorator ps5 =  new PreparedStatementDecorator(con.prepareStatement(actualizarPreparacionStrAux,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				logger.info(">>>>>>>>>>>>>>"+pti.getArticulosFiltradosMap().get("codigo_"+x));
				logger.info(">>>>>>>>>>>>>>"+pti.getAlmacen());
				logger.info(">>>>>>>>>>>>>>"+pti.getArticulosFiltradosMap().get("seccion_"+x));
				logger.info(">>>>>>>>>>>>>>"+pti.getArticulosFiltradosMap().get("subseccion_"+x));
				logger.info(">>>>>>>>>>>>>>"+pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x));
				logger.info(">>>>>>>>>>>>>>"+pti.getArticulosFiltradosMap().get("codigo_lote_"+x));
				
				
				
				
				ps3.setInt(1, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
				ps3.setInt(2, pti.getAlmacen());
				ps3.setInt(3, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
				ps3.setInt(4, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
				ps3.setInt(5, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x)+""));
			
				if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")>0)
				{
				
				ps3.setInt(6, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
				}
				
				
				logger.info("CONSULTAMODIFICA>>>>>>><"+Consultamodificar);
				
				
				art=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps3.executeQuery()));

				
				logger.info("NUM REGISTROS ART>>>>>>>"+Utilidades.convertirAEntero(art.get("numRegistros")+""));
				
				if(Utilidades.convertirAEntero(art.get("numRegistros")+"")==0)
				{
				
				logger.info("-----------------------"+pti.getInstitucion());
				logger.info("-----------------------"+pti.getCentroAtencion());
				logger.info("-----------------------"+pti.getArticulosFiltradosMap().get("clase_"+x));
				logger.info("-----------------------"+pti.getArticulosFiltradosMap().get("grupo_"+x));
				logger.info("-----------------------"+pti.getArticulosFiltradosMap().get("subgrupo_"+x));
				logger.info("-----------------------"+pti.getArticulosFiltradosMap().get("codigo_"+x));
				logger.info(">>>>>>>>>>>>>>>>>>>>>>>)"+pti.getArticulosFiltradosMap().get("codigo_lote_"+x));
				logger.info("-----------------------"+pti.getArticulosFiltradosMap().get("existencias_"+x));
				logger.info("-----------------------"+pti.getFecha_toma());
				logger.info("-----------------------"+pti.getHora_toma());
				logger.info("-----------------------"+pti.getUsuarioModifica());
				
				
				ps.setInt(1, pti.getInstitucion());
				ps.setInt(2, pti.getCentroAtencion());
				ps.setInt(3, pti.getAlmacen());
				ps.setInt(4, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
				ps.setInt(5, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
				ps.setInt(6, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("clase_"+x)+""));
				ps.setInt(7, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("grupo_"+x)+""));
				ps.setInt(8, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subgrupocon_"+x)+""));
				ps.setInt(9, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
				ps.setInt(10, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x)+""));
				if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")<=0)
					ps.setNull(11, Types.INTEGER);
				else
					ps.setInt(11, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
				
				ps.setInt(12, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("existencias_"+x)+""));
				ps.setDate(13, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(pti.getFecha_toma())));
				ps.setString(14, UtilidadFecha.convertirHoraACincoCaracteres(pti.getHora_toma()));
				ps.setString(15, pti.getUsuarioModifica());
				ps.setInt(16, codPrepMax);
				ps.setDouble(17, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_preparacion_toma_invent")+""));

				ps.executeUpdate();
				
				logger.info("-------------------------------- "+insertarPreparacionStr);
				}
				else
				{
					
					logger.info("CODIGO PREPARACION >>>>>>><<"+codPrepMax);
		
					logger.info("+++++++++++++++++"+pti.getArticulosFiltradosMap().get("codigo_"+x));
					logger.info("+++++++++++++++++"+pti.getAlmacen());
					logger.info("+++++++++++++++++"+pti.getArticulosFiltradosMap().get("seccion_"+x));
					logger.info("+++++++++++++++++"+pti.getArticulosFiltradosMap().get("subseccion_"+x));
					logger.info("+++++++++++++++++)"+pti.getArticulosFiltradosMap().get("codigo_lote_"+x));
					logger.info("+++++++++++++++++"+pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x));				
					
					
					
					
					ps5.setInt(1, pti.getInstitucion());
					ps5.setInt(2, pti.getCentroAtencion());
					ps5.setInt(3, pti.getAlmacen());
					ps5.setInt(4, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
					ps5.setInt(5, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
					ps5.setInt(6, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("clase_"+x)+""));
					ps5.setInt(7, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("grupo_"+x)+""));
					ps5.setInt(8, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subgrupocon_"+x)+""));
					ps5.setInt(9, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
					ps5.setInt(10, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x)+""));
					
					if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")<=0)
						ps5.setNull(11, Types.INTEGER);
					else
						ps5.setInt(11, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
					
					ps5.setInt(12, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("existencias_"+x)+""));
					ps5.setDate(13,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( pti.getFecha_toma())));
					ps5.setString(14, pti.getHora_toma());
					ps5.setString(15, pti.getUsuarioModifica());
					ps5.setInt(16, codPrepMax);
					ps5.setInt(17, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
					ps5.setInt(18, pti.getAlmacen());
					ps5.setInt(19, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
					ps5.setInt(20, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
					ps5.setInt(21, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_det_pk_"+x)+""));					
					if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")>0)
					{
						ps5.setInt(22, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
					}
					
									logger.info("UPDATE>>>>>>>>>>"+actualizarPreparacionStrAux);
					
					ps5.executeUpdate();
					
				}
			}	
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return false;
	}
	
	
	public static int CodigoPreparacionMax (Connection con, PreparacionTomaInventario pti)
	{	
		int codPrepMax = ConstantesBD.codigoNuncaValido;
		try{
		
			String CodigoPreparacionMaxStr = "SELECT MAX(codigo_preparacion) as cod_prep FROM preparacion_toma_inventario";
			PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(CodigoPreparacionMaxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps6.executeQuery());
		
			if (rs.next())
				codPrepMax = rs.getInt("cod_prep");
			
			codPrepMax++;
		
		}
		catch(SQLException e)
		{
			logger.error("ERROR", e);
		}
		
		return codPrepMax;
	}
	
}