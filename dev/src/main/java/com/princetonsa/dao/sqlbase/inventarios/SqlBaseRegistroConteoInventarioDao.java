package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.RegistroConteoInventario;




/**
 * 
 * @author axioma
 *
 */
public class SqlBaseRegistroConteoInventarioDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSeccionesDao.class);
	
	/**
	 * Consulta todos los grupos segun el codigo de una clase
	 */
	private static String consultarGruposStr= "SELECT codigo, nombre FROM grupo_inventario WHERE clase = ?";
	
	/**
	 * Consulta todos los subgrupos segun el codigo del clase y el codigo del grupo
	 */
	private static String consultarSubgruposStr= "SELECT codigo, nombre FROM subgrupo_inventario WHERE clase = ? AND grupo = ?";
	
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
													"AND a.estado= " +ValoresPorDefecto.getValorTrueParaConsultas()+
													" AND a.codigo = dapa.articulo " +
													" AND dapa.codigo_art_por_almacen = apa.codigo_pk " +
													" AND ci.codigo = gi.clase " +
													" AND gi.codigo = si.grupo "; 
	
	/**
	 * Cadena para insertar la informacion de cada articulo preparado en la tabla preparacion_tom_inventario
	 */
	private static String insertarStr = "INSERT INTO " +
														"registro_conteo_inventario " + 
														"(" +
														"numero_conteo," +				//1	
														"codigo_preparacion," +			//2
														"centro_atencion," +			//3
														"almacen," +					//4
														"seccion," +					//5
														"subseccion," +					//6
														"articulo," +					//7
														"lote," +						//8
														"fecha_vencimiento," +			//9
														"clase," +						//10
														"grupo," +						//11
														"subgrupo," +					//12
														"usuario_responsable," +		//13
														"cantidad," +					//14
														"estado," +						//15
														"ind_diferencia_conteo," +		//16
														"usuario_modifica," +			//17
														"institucion," +				//18
														"fecha_modifica," +				//19
														"hora_modifica," +				//20
														"fecha_finaliza," +				//21
														"hora_finaliza," +				//22
														"usuario_finaliza," +			//23
														"codigo," +						//24
														"codigo_preparacion_nopk " +	//25
														 ")" +
													"VALUES " +
														"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?)";
	
	
	
	private static String modificarStr= "UPDATE  " +
														" registro_conteo_inventario " +
												" SET " +
														"numero_conteo=?, " +
														"codigo_preparacion=?, " +
														"centro_atencion=?, " +
														"almacen=?, " +
														"seccion=?, " +
														"subseccion=?, " +
														"articulo=?, " +
														"lote=?, " +
														"fecha_vencimiento=?, " +
														"clase=?, " +
														"grupo=?, " +
														"subgrupo=?, " +
														"usuario_responsable=?, " +
														"cantidad=?, " +
														"estado=?, " +
														"ind_diferencia_conteo=?, " +
														"usuario_modifica=?, " +
														"institucion=?," +
														"fecha_finaliza=?," +
														"hora_finaliza=?, " +
														"usuario_finaliza=?," +
														"fecha_modifica=CURRENT_DATE, " +
														"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
												" WHERE " +
														" articulo=? "+
														" and codigo_preparacion=? "+
														" and centro_atencion=? "+
														" and almacen=? "+
														" and seccion=? "+
														" and subseccion=? "+
														" and clase=? "+
														" and grupo=? "+
														" and subgrupo=? " +
														" and estado='PEN' ";
	
	// --------------- METODOS
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static boolean anularConteos(Connection con, RegistroConteoInventario pti, String estado){
		String consulta = "DELETE FROM registro_conteo_inventario WHERE codigo_preparacion IN ("+pti.getCodigosPreparaciones()+") AND estado='"+estado+"'";
		logger.info("ELIMINAR CONTEOS >> "+consulta);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
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
	 * @param pti
	 * @return
	 */
	public static HashMap filtrarArticulos(Connection con, RegistroConteoInventario pti){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String filtro = filtrarArticulosStr;
		String codigosFiltrados;
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
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(filtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				codigosFiltrados = crearCadena(mapa);
				
			} 
			else 
				filtro = pti.getCodigosArticulos();
			
			
					
			String 	consultaArticulosPreparados="SELECT " +
														"articulo as codigo, " +
														"codigo as codigo_preparacion, " +
														"centro_atencion, " +
														"almacen, " +
														"seccion, " +
														"subseccion, " +
														"clase, " +
														"grupo, " +
														"subgrupo " +
												"FROM " +
													"preparacion_toma_inventario " +
												"WHERE " +
													"articulo IN ("+filtro+")" +
													"and centro_atencion="+pti.getCentroAtencion()+" "+
													"and almacen="+pti.getAlmacen()+" ";
			
			
			
													if (!pti.getSeccion().equals("") && !pti.getSeccion().equals("null")){
														consultaArticulosPreparados+=" AND seccion IN ("+pti.getSeccion()+")";
														if (pti.getSubseccion()>0){
															consultaArticulosPreparados+= " AND subseccion = "+pti.getSubseccion();
														}
													}
													
													if (pti.getClase() > 0){
														consultaArticulosPreparados += " AND clase = "+pti.getClase();
														if (pti.getGrupo() >0){
															consultaArticulosPreparados += " AND grupo = "+pti.getGrupo();
															if (pti.getSubgrupo()>0){
																consultaArticulosPreparados += " AND subgrupo = "+pti.getSubgrupo();
															}
														}
													}
						
			PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPreparados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap codi=new HashMap();
			codi.put("numRegistros", "0");
			codi=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery()));
			String codigosPreparados=crearCadena(codi);
			
			if (pti.getIndArticulo().toString().equals("S"))
				codigosPreparados=crearNuevaCadenaArticulosConDiferencias(con, codi, pti);
			String consultarArticulosStr = "SELECT " +
											" DISTINCT " +
												"t.descripcion as descripcion, " +
												"t.codigo, " +
												"t.unidad_medida, " +
												"getunidadmedidaarticulo(t.codigo) as nomunidadmedida, " +
												"coalesce(t.codigo_interfaz, '') as codigo_interfaz, " +
												"coalesce(t.lote, 'No aplica') as lote," +
												"t.codigo_lote, " +
												"coalesce(t.fecha_vencimiento, 'No aplica') as fecha_vencimiento, " +
												"t.existencias, " +
												"t.concentracion, " +
												"t.forma_farmaceutica, " +
												"getnomformafarmaceutica(t.forma_farmaceutica) as nombreff, " +
												"getnaturalezaarticulo(t.codigo) as nomnat, " +
												"t.naturaleza, " +
												"t.subgrupo AS subgrupocon, " +
												"se.descripcion || ' - ' || " + "subs.descripcion AS seccion_subseccion, " +
												"se.codigo_pk AS seccion, " +
												"subs.codigo_subseccion AS subseccion, " +
												"si.subgrupo, " +
												"si.grupo, " +
												"si.clase, "+
												"dapa.codigo_det_pk, " +
												"t.codigo_preparacion, " +
												"t.codigo_preparacion_nopk, " +
												"dapa.ubicacion," +
												"t.codigo_dapa  " +
											" FROM " +
											"((" +
												"SELECT " +
													" distinct " +
													" a.descripcion, " +
													" a.codigo," +
													" a.unidad_medida," +
													" a.codigo_interfaz, " +
													" l.lote," +
													" l.codigo as codigo_lote,to_char(l.fecha_vencimiento, 'dd/mm/yyyy') as fecha_vencimiento," +
													" l.existencias," +
													" a.concentracion," +
													" a.forma_farmaceutica," +
													" a.naturaleza," +
													" a.subgrupo," +
													" pti.codigo as codigo_preparacion," +
													" pti.codigo_dapa," +
													" pti.codigo_preparacion as codigo_preparacion_nopk " +
												" FROM " +
													" preparacion_toma_inventario pti " +
												"inner join " +
														" articulos_almacen aa on (pti.articulo=aa.articulo and pti.almacen=aa.almacen)" +
												" INNER JOIN" +
													" articulo_almacen_x_lote l ON (aa.articulo=l.articulo and aa.almacen=l.almacen and pti.lote=l.codigo)" +
												"  INNER JOIN" +
													" articulo a ON l.articulo=a.codigo" +
												" WHERE "+
													" a.maneja_lotes='S' " +
													" AND pti.articulo IN (-1,"+codigosPreparados+"-1) " +
													" and pti.estado='PEN' " +
													" and pti.almacen="+pti.getAlmacen()+" " +
													") " +
												" UNION " +
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
													"a.subgrupo," +
													"pti.codigo as codigo_preparacion," +
													"pti.codigo_dapa, " +
													"pti.codigo_preparacion as codigo_preparacion_nopk " +
												"FROM " +
													"articulos_almacen ex, " +
													"articulo a " +
												"INNER JOIN  preparacion_toma_inventario pti ON a.codigo=pti.articulo " +
												"WHERE " +
													"ex.articulo=a.codigo " +
													"AND a.maneja_lotes='N' " +
													"AND pti.articulo IN (-1,"+codigosPreparados+"-1) " +
													"AND ex.almacen="+pti.getAlmacen()+" " +
													"AND pti.estado='PEN' "+
											")) t " +
											"INNER JOIN " +
												"det_articulos_por_almacen dapa ON(dapa.articulo=t.codigo and dapa.codigo_det_pk=t.codigo_dapa) " +
											"INNER JOIN " +
												"articulos_por_almacen apa ON(apa.codigo_pk = dapa.codigo_art_por_almacen) " +
											"INNER JOIN " +
												"secciones se ON (se.codigo_pk = apa.seccion) " +
											"INNER JOIN " +
												"subsecciones subs ON(subs.codigo_pk_seccion=apa.seccion AND subs.codigo_subseccion = apa.subseccion) " +
											"INNER JOIN " +
												"subgrupo_inventario si on(si.codigo=t.subgrupo) " +
											"INNER JOIN " +
												"preparacion_toma_inventario pti on (t.codigo=pti.articulo and pti.codigo_dapa=dapa.codigo_det_pk and pti.seccion=se.codigo_pk AND pti.subseccion = subs.codigo_subseccion) " ;
				
				if (pti.getIndArticulo().toString().equals("S"))
				{
					consultarArticulosStr+=" LEFT OUTER JOIN " +
												"registro_conteo_inventario rci ON (pti.articulo=rci.articulo  AND rci.codigo_preparacion=pti.codigo AND rci.seccion=pti.seccion AND rci.subseccion = pti.subseccion) " +
											" WHERE " +
													"1=1 " +
													" AND (rci.ind_diferencia_conteo='S' OR (rci.ind_diferencia_conteo IS NULL AND rci.numero_conteo<"+ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(pti.getInstitucion())+")) " +
													" AND t.codigo in ("+codigosPreparados+"-1) " +
													//" AND pti.estado='PEN' " +
													" AND pti.almacen="+pti.getAlmacen()+
													" AND (rci.numero_conteo=1 OR rci.numero_conteo IS NULL) ";
				}
				else
				{
					consultarArticulosStr+=" WHERE " +
												"1=1 " +
												" AND  t.codigo in (-1,"+codigosPreparados+"-1) and pti.estado='PEN' " +
												" AND pti.almacen="+pti.getAlmacen();
				}											
			if (!pti.getSeccion().equals("") && !pti.getSeccion().equals("null")){
				consultarArticulosStr+=" AND pti.seccion IN ("+pti.getSeccion()+")";
				if (pti.getSubseccion()>0){
					consultarArticulosStr+= " AND pti.subseccion = "+pti.getSubseccion();
				}
			}
			consultarArticulosStr +=" ORDER BY se.descripcion|| ' - '||subs.descripcion ASC, t.descripcion";
			//consultarArticulosStr +=" ORDER BY seccion_subseccion ASC,"+pti.getOrdenar();
												/*"descripcion," +
												"t.codigo, " +
												"t.unidad_medida, " +
												"t.codigo_interfaz, " +
												"lote, " +
												"t.codigo_lote, " +
												"fecha_vencimiento, " +
												"t.existencias, " +
												"t.concentracion, " +
												"t.forma_farmaceutica, " +
												"t.naturaleza, " +
												"t.subgrupo , " +
												"se.codigo_pk, " +
												"subs.codigo_subseccion, " +
												"si.subgrupo, " +
												"si.grupo, " +
												"si.clase, "+
												"dapa.codigo_det_pk, " +
												"t.codigo_preparacion, " +
												"dapa.ubicacion," +
												"t.codigo_dapa  " ;*/
			
			
			logger.info("SENTENCIA>>>"+consultarArticulosStr);
			
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
			
			mapa.put("codigos_preparados", codigosPreparados);
			for(int w=0; w<Utilidades.convertirAEntero( mapa.get("numRegistros").toString()); w++)
			{
				mapa.put("conteo_"+w, ultimoConteoCantidad(con, mapa, w, pti));
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
	 * @param con
	 * @param pti
	 * @param codi
	 * @return
	 */
	private static String crearNuevaCadenaArticulosConDiferencias(Connection con, HashMap codi,RegistroConteoInventario pti) {
		
	HashMap codigos=new HashMap();
	codigos.put("numRegistros", "0");
	int  conteoMinimo=Utilidades.convertirAEntero(ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(pti.getInstitucion()));
	int p=0;
	try{
			for (int x=0; x<Utilidades.convertirAEntero(codi.get("numRegistros").toString()); x++)
			{
				String str="SELECT " +
								"articulo as codigo " +
							"FROM " +
								"registro_conteo_inventario " +
							"WHERE " +
								"articulo="+codi.get("codigo_"+x)+" " +
								"AND ind_diferencia_conteo='N' " +
								"AND numero_conteo>="+conteoMinimo+" " +
								"AND estado='FIN'" +
								"AND codigo_preparacion="+codi.get("codigo_preparacion_"+x)+" "+
								"AND centro_atencion="+codi.get("centro_atencion_"+x)+" "+
								"AND almacen="+codi.get("almacen_"+x)+" "+
								"AND seccion="+codi.get("seccion_"+x)+" "+
								"AND subseccion="+codi.get("subseccion_"+x)+" "+
								"AND clase="+codi.get("clase_"+x)+" "+
								"AND grupo="+codi.get("grupo_"+x)+" "+
								"AND codigo_preparacion_nopk="+codi.get("codigo_preparacion_nopk_"+x)+" "+
								"AND subgrupo="+codi.get("subgrupo_"+x)+" ";
				
				PreparedStatementDecorator ps7 =  new PreparedStatementDecorator(con.prepareStatement(str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(ps7.executeQuery());
				if (rs.next()){
					codigos.put("codigo_"+p, rs.getInt("codigo")+"");
					p++;
				}
			}
	}
	
	catch (SQLException e)
	{
		e.printStackTrace();	
	}
	String cadenaPreparada=crearCadena(codi);
	codigos.put("numRegistros", p);
	for (int x=0; x<Utilidades.convertirAEntero(codigos.get("numRegistros").toString()); x++)
	{
		if(x>0)
			cadenaPreparada=cadenaPreparada.replaceFirst(","+codigos.get("codigo_"+x).toString()+",", ",-1,");
		else
			cadenaPreparada=cadenaPreparada.replaceFirst(""+codigos.get("codigo_"+x).toString()+",", "-1,");
	}
	return cadenaPreparada;	
		
	}
	
	
	/**
	 * 
	 * @param mapa
	 * @return
	 */
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
	public static boolean guardarConteo(Connection con, RegistroConteoInventario pti)
	{
		int numConteo=0;
		int registro=0;
		String estado="PEN",dif="S";
		String fechaf="";
		String horaf=null;
		String usuariof=null;
		String[] indices = {"descripcion_","codigo_","unidad_medida_","codigo_interfaz_","lote_","codigo_lote_","fecha_vencimiento_","existencias_","concentracion_","forma_farmaceutica_","naturaleza_", "seccion_","subseccion_","subgrupo_","grupo_","clase_","codigo_det_pk_","ubicacion_","conteo_","codigo_preparacion_"};
		HashMap mapa= new HashMap();
		try
		{
		if (pti.getEstado().toString().equals("finalizar"))
			{
				estado="FIN";
				fechaf=UtilidadFecha.getFechaActual();
				horaf=UtilidadFecha.getHoraActual();
				usuariof=pti.getUsuario_responsable();
			}
		for (int x=0; x<Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("numRegistros").toString()); x++)
			{
				numConteo=numeroUltimoConteo(con,pti.getArticulosFiltradosMap(), pti, x);
				dif=diferenciaConteo(con,pti,x);
				registro=hayRegistroPen(con,pti,x);
				if(registro==0 && UtilidadCadena.noEsVacio(pti.getArticulosFiltradosMap().get("conteo_"+x).toString())==true)
				{
						
					/**
					
					INSERT INTO " +
														"registro_conteo_inventario " + 
														"(" +
														"numero_conteo," +				//1	
														"codigo_preparacion," +			//2
														"centro_atencion," +			//3
														"almacen," +					//4
														"seccion," +					//5
														"subseccion," +					//6
														"articulo," +					//7
														"lote," +						//8
														"fecha_vencimiento," +			//9
														"clase," +						//10
														"grupo," +						//11
														"subgrupo," +					//12
														"usuario_responsable," +		//13
														"cantidad," +					//14
														"estado," +						//15
														"ind_diferencia_conteo," +		//16
														"usuario_modifica," +			//17
														"institucion," +				//18
														"fecha_modifica," +				//19
														"hora_modifica," +				//20
														"fecha_finaliza," +				//21
														"hora_finaliza," +				//22
														"usuario_finaliza," +			//23
														"codigo" +						//24
														 ")" +
													"VALUES " +
														"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?)
					**/
					
						PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, numConteo);
						ps.setInt(2, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_preparacion_"+x)+""));
						ps.setInt(3, pti.getCentroAtencion());
						ps.setInt(4,pti.getAlmacen());
						ps.setInt(5, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
						ps.setInt(6, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
						ps.setInt(7, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
						if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")<=0)
							ps.setNull(8, Types.INTEGER);
						else
							ps.setInt(8, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
								
						if (!pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString().equals("No aplica") && !pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString().equals(""))
							{
							ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x)+"")));					
							}
						else
							{
							ps.setNull(9, Types.DATE);
							}
						ps.setInt(10, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("clase_"+x)+""));
						ps.setInt(11, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("grupo_"+x)+""));
						ps.setInt(12, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subgrupocon_"+x)+""));
						ps.setString(13, pti.getUsuario_responsable());
						ps.setInt(14, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("conteo_"+x)+""));
						ps.setString(15, estado);
						ps.setString(16,dif);
						ps.setString(17, pti.getUsuarioModifica());
						ps.setInt(18, pti.getInstitucion());
						if (fechaf.equals(""))
						{
							ps.setNull(19, Types.DATE);
							ps.setNull(20,Types.CHAR);
							ps.setNull(21, Types.VARCHAR);
						}
						else
						{
							ps.setString(19, UtilidadFecha.conversionFormatoFechaABD(fechaf));
							ps.setString(20, horaf);
							ps.setString(21, usuariof);
								
						}
						
						ps.setInt(22, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_registro_conteo_inventario"));
						ps.setInt(23, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_preparacion_nopk_"+x)+""));
						logger.info("FECHA F >>>>>"+fechaf);
						logger.info("FECHA VENCIMIENTO >>>>>"+UtilidadFecha.conversionFormatoFechaABD(pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x)+""));
						
						
						ps.executeUpdate();
						}
						
				else if(registro>0 && UtilidadCadena.noEsVacio(pti.getArticulosFiltradosMap().get("conteo_"+x).toString())==true)
				{
							PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, numConteo);
							ps.setInt(2, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_preparacion_"+x)+""));
							ps.setInt(3, pti.getCentroAtencion());
							ps.setInt(4,pti.getAlmacen());
							ps.setInt(5, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
							ps.setInt(6, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
							ps.setInt(7, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
							if(Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+"")<=0)
								ps.setNull(8, Types.INTEGER);
							else
								ps.setInt(8, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_lote_"+x)+""));
									
							if (!pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString().equals("No aplica") && !pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x).toString().equals(""))
								{
								ps.setString(9, UtilidadFecha.conversionFormatoFechaABD(pti.getArticulosFiltradosMap().get("fecha_vencimiento_"+x)+""));					
								}
							else
								{
								ps.setNull(9, Types.DATE);
								}
							ps.setInt(10, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("clase_"+x)+""));
							ps.setInt(11, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("grupo_"+x)+""));
							ps.setInt(12, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subgrupocon_"+x)+""));
							ps.setString(13, pti.getUsuario_responsable());
							ps.setInt(14, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("conteo_"+x)+""));
							ps.setString(15, estado);
							ps.setString(16,dif);
							ps.setString(17, pti.getUsuarioModifica());
							ps.setInt(18, pti.getInstitucion());				
							if (estado.equals("PEN"))
								ps.setNull(19, Types.DATE);		
							else
								ps.setString(19, UtilidadFecha.conversionFormatoFechaABD(fechaf));
							ps.setString(20, horaf);
							ps.setString(21, usuariof);
							ps.setInt(22, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_"+x)+""));
							ps.setInt(23, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("codigo_preparacion_"+x)+""));
							ps.setInt(24, pti.getCentroAtencion());
							ps.setInt(25,pti.getAlmacen());
							ps.setInt(26, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("seccion_"+x)+""));
							ps.setInt(27, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subseccion_"+x)+""));
							ps.setInt(28, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("clase_"+x)+""));
							ps.setInt(29, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("grupo_"+x)+""));
							ps.setInt(30, Utilidades.convertirAEntero(pti.getArticulosFiltradosMap().get("subgrupocon_"+x)+""));
							
							
							
							ps.executeUpdate();
					
				}
					
							
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
	 * @param pti
	 * @param x
	 * @return
	 */
	private static int hayRegistroPen(Connection con, RegistroConteoInventario pti, int i) {
		
		int cuantos=0;
		String diferenciaConteoStr = "SELECT count(*) as cuantos FROM registro_conteo_inventario where " +
										"articulo="+pti.getArticulosFiltradosMap().get("codigo_"+i)+" "+
										"and codigo_preparacion="+pti.getArticulosFiltradosMap().get("codigo_preparacion_"+i)+" "+
										"and centro_atencion="+pti.getCentroAtencion()+" "+
										"and almacen="+pti.getAlmacen()+" "+
										"and seccion="+pti.getArticulosFiltradosMap().get("seccion_"+i)+" "+
										"and subseccion="+pti.getArticulosFiltradosMap().get("subseccion_"+i)+" "+
										"and clase="+pti.getArticulosFiltradosMap().get("clase_"+i)+" "+
										"and grupo="+pti.getArticulosFiltradosMap().get("grupo_"+i)+" "+
										"and subgrupo="+pti.getArticulosFiltradosMap().get("subgrupocon_"+i)+" " +
										"and codigo_preparacion_nopk="+pti.getArticulosFiltradosMap().get("codigo_preparacion_nopk_"+i)+" "+
										"and estado='PEN'";
		try{
		PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(diferenciaConteoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ps6.executeQuery());
		if (rs.next()){
			 cuantos= rs.getInt("cuantos");
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return cuantos;
	}
	
		
	

	/**
	 * 
	 * @param i
	 * @return
	 */
	private static int ultimoConteoCantidad(Connection con, HashMap mapa, int i, RegistroConteoInventario pti) {
		
		int numConteo=0;
		int cantidad=0;
		numConteo=numeroUltimoConteo(con, mapa, pti,i);
		try{
			String ultimoConteo = "SELECT " +
										"cantidad " +
									"FROM registro_conteo_inventario " +
									"where " +
										"articulo="+mapa.get("codigo_"+i)+" "+
										" and numero_conteo="+numConteo+" "+
										" and codigo_preparacion="+mapa.get("codigo_preparacion_"+i)+" "+
										" and centro_atencion="+pti.getCentroAtencion()+" "+
										" and almacen="+pti.getAlmacen()+" "+
										" and seccion="+mapa.get("seccion_"+i)+" "+
										" and subseccion="+mapa.get("subseccion_"+i)+" "+
										" and clase="+mapa.get("clase_"+i)+" "+
										" and grupo="+mapa.get("grupo_"+i)+" "+
										" and subgrupo="+mapa.get("subgrupocon_"+i)+" " +
										" and codigo_preparacion_nopk="+mapa.get("codigo_preparacion_nopk_"+i)+" "+
										" and estado='PEN' ";
			
			if(!mapa.get("lote_"+i).toString().equals("No aplica") && !mapa.get("lote_"+i).toString().equals("null"))
			{
				ultimoConteo+="and lote="+mapa.get("codigo_lote_"+i)+" ";
			}
			PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(ultimoConteo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps6.executeQuery());
			if (rs.next()){
				cantidad = rs.getInt("cantidad");
			}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		
		
		
		return cantidad;
	}

	
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static int numeroUltimoConteo (Connection con, HashMap mapa,RegistroConteoInventario pti, int i)
	{	
		int codPrepMax = ConstantesBD.codigoNuncaValido;
		try{
			String CodigoPreparacionMaxStr = "SELECT MAX(numero_conteo) as max FROM registro_conteo_inventario where " +
											"articulo="+mapa.get("codigo_"+i)+" "+
											" and codigo_preparacion="+mapa.get("codigo_preparacion_"+i)+" "+
											" and centro_atencion="+pti.getCentroAtencion()+" "+
											" and almacen="+pti.getAlmacen()+" "+
											" and seccion="+mapa.get("seccion_"+i)+" "+
											" and subseccion="+mapa.get("subseccion_"+i)+" "+
											" and clase="+mapa.get("clase_"+i)+" "+
											" and grupo="+mapa.get("grupo_"+i)+" "+
											" and subgrupo="+mapa.get("subgrupocon_"+i)+"" +
											" and codigo_preparacion_nopk="+mapa.get("codigo_preparacion_nopk_"+i)+" "+
											" and estado='FIN' ";
			
			logger.info("SQL / CodigoPreparacionMaxStr / "+CodigoPreparacionMaxStr);
	
			PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(CodigoPreparacionMaxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps6.executeQuery());
		
			if (rs.next())
				codPrepMax = rs.getInt("max");
			codPrepMax++;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return codPrepMax;
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @param conteo
	 * @return
	 */
	public static String diferenciaConteo (Connection con,  RegistroConteoInventario pti, int i)
	{	
		String dif="S";
		int cuantos=0;
		int  conteoMinimo=Utilidades.convertirAEntero(ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(pti.getInstitucion()));
		
		String diferenciaConteoStr = "SELECT count(*) as cuantos FROM registro_conteo_inventario where " +
										"articulo="+pti.getArticulosFiltradosMap().get("codigo_"+i)+" "+
										" and codigo_preparacion="+pti.getArticulosFiltradosMap().get("codigo_preparacion_"+i)+" "+
										" and centro_atencion="+pti.getCentroAtencion()+" "+
										" and almacen="+pti.getAlmacen()+" "+
										" and seccion="+pti.getArticulosFiltradosMap().get("seccion_"+i)+" "+
										" and subseccion="+pti.getArticulosFiltradosMap().get("subseccion_"+i)+" "+
										" and clase="+pti.getArticulosFiltradosMap().get("clase_"+i)+" "+
										" and grupo="+pti.getArticulosFiltradosMap().get("grupo_"+i)+" "+
										" and subgrupo="+pti.getArticulosFiltradosMap().get("subgrupocon_"+i)+" " +
										" and cantidad="+pti.getArticulosFiltradosMap().get("conteo_"+i)+" " +
										" and codigo_preparacion_nopk="+pti.getArticulosFiltradosMap().get("codigo_preparacion_nopk_"+i)+" "+
										" and estado='FIN'" +
										" and numero_conteo>="+(conteoMinimo-1);
		
		logger.info("\n"+diferenciaConteoStr+"\n");
		
		try{
		PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(diferenciaConteoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
		if (!pti.getArticulosFiltradosMap().get("conteo_"+i).toString().equals(""))
		{
			ResultSetDecorator rs = new ResultSetDecorator(ps6.executeQuery());	
			if (rs.next()){
				 cuantos= rs.getInt("cuantos");
			}
		}
		
		if(cuantos>0)
			{
			modificarIndiceDifContRegistroArticulos(con,pti,i);
			return "N";
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return dif;
	}

	
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @param i
	 */
	private static void modificarIndiceDifContRegistroArticulos(Connection con, RegistroConteoInventario pti, int i) {
		
		String diferenciaConteoStr = "UPDATE registro_conteo_inventario set ind_diferencia_conteo='N' where " +
		"articulo="+pti.getArticulosFiltradosMap().get("codigo_"+i)+" "+
		" and codigo_preparacion="+pti.getArticulosFiltradosMap().get("codigo_preparacion_"+i)+" "+
		" and centro_atencion="+pti.getCentroAtencion()+" "+
		" and almacen="+pti.getAlmacen()+" "+
		" and seccion="+pti.getArticulosFiltradosMap().get("seccion_"+i)+" "+
		" and subseccion="+pti.getArticulosFiltradosMap().get("subseccion_"+i)+" "+
		" and clase="+pti.getArticulosFiltradosMap().get("clase_"+i)+" "+
		" and grupo="+pti.getArticulosFiltradosMap().get("grupo_"+i)+" "+
		" and subgrupo="+pti.getArticulosFiltradosMap().get("subgrupocon_"+i)+" " +
		" and codigo_preparacion_nopk="+pti.getArticulosFiltradosMap().get("codigo_preparacion_nopk_"+i)+" " +
		" and estado='FIN'";
		try
		{
			PreparedStatementDecorator ps6 =  new PreparedStatementDecorator(con.prepareStatement(diferenciaConteoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps6.executeUpdate();
		
		}

		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

	
}



