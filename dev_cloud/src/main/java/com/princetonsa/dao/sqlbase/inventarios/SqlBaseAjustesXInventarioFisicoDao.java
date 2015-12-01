package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;


public class SqlBaseAjustesXInventarioFisicoDao {
	
	// --------------- ATRIBUTOS
	
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
													" a.subgrupo = si.codigo " +
													" AND a.estado= " +ValoresPorDefecto.getValorTrueParaConsultas()+
													" AND a.codigo = dapa.articulo " +
													" AND dapa.codigo_art_por_almacen = apa.codigo_pk  " +
													" AND ci.codigo = gi.clase " +
													" AND gi.codigo = si.grupo "; 

	/**
	 * Consulta el codigo de preparacion segun el codigo de conteo
	 */
	private static String consultarCodPreparacionStr = "SELECT codigo_preparacion FROM registro_conteo_inventario WHERE codigo=?";
	
	/**
	 * Consulta la descripcion de una transaccion segun su codigo
	 */
	private static String  consultarDescripcionAjusteStr = "SELECT descripcion FROM tipos_trans_inventarios WHERE consecutivo=?";
	
	/**
	 *  Insertar registro de ajuste
	 */
	private static String insertarRegistroAjusteStr = "INSERT INTO ajustes_x_inv_fisico (" +
															"codigo, " +
															"cod_transaccion, " +
															"articulo, " +
															"lote, " +
															"ajuste, " +
															"institucion, " +
															"almacen, " +
															"usuario_modifica, " +
															"fecha_modifica, " +
															"hora_modifica) " +
														"VALUES " +
															"(?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	private static String consultarCodigoAjusteStr = "SELECT codigo FROM ajustes_x_inv_fisico WHERE " +
														"cod_transaccion =? " +
														"AND articulo=? " +
														"AND lote=? " +
														"AND ajuste=? " +
														"AND institucion=? " +
														"AND almacen=? ";

	private static String finalizarPreparacionStr = "UPDATE preparacion_toma_inventario SET estado='FIN' WHERE almacen=? AND articulo=?";
	
	
	// --------------- METODOS

	
	public static int registrarAjuste(Connection con, AjustesXInventarioFisico axif){
		
		HashMap mapa = axif.getArticulosFiltradosMap();
		int x = axif.getIndexMap();
		PreparedStatementDecorator ps, psc;
		int codigo = ConstantesBD.codigoNuncaValido;
		
		String cadena = "SELECT codigo FROM ajustes_x_inv_fisico WHERE  ";

		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRegistroAjusteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO ajustes_x_inv_fisico (" +
															"codigo, " +
															"cod_transaccion, " +
															"articulo, " +
															"lote, " +
															"ajuste, " +
															"institucion, " +
															"almacen, " +
															"usuario_modifica, " +
															"fecha_modifica, " +
															"hora_modifica) " +
														"VALUES " +
															"(?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")
			 */
			
			
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ajustes_x_inv_fisico")+""));
						
			if(Utilidades.convertirAEntero(mapa.get("cant_ajuste_"+x).toString())>0){
				ps.setInt(2, axif.getCodTransaccionEntrada());
				cadena+= " cod_transaccion = "+axif.getCodTransaccionEntrada();				
			}
			else {
				ps.setInt(2, axif.getCodTransaccionSalida());
				cadena+= " cod_transaccion = "+axif.getCodTransaccionSalida();
			}	
			
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigo_"+x).toString()));			
			cadena+= " AND articulo = "+mapa.get("codigo_"+x).toString();
			
			if(Utilidades.convertirAEntero(mapa.get("codigo_lote_"+x).toString())==-1){
				ps.setNull(4, Types.INTEGER);
				cadena+= " AND lote IS NULL " ;				
			}
			else{	
				ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigo_lote_"+x).toString()));
				cadena+= " AND lote = "+mapa.get("codigo_lote_"+x).toString() ;
			}	

			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("cant_ajuste_"+x).toString()));
			ps.setInt(6, axif.getInstitucion());
			ps.setInt(7, axif.getAlmacen());
			ps.setString(8, axif.getUsuarioModifica());
			
			//---
			cadena+= " AND ajuste = "+mapa.get("cant_ajuste_"+x).toString() ;					
			cadena+= " AND institucion = "+axif.getInstitucion() ;
			cadena+= " AND almacen = "+axif.getAlmacen() ;			
			
			psc =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cadena sql >> "+cadena);
			//---
		
			if(ps.executeUpdate()>0)
			{					
				ResultSetDecorator rs = new ResultSetDecorator(psc.executeQuery());
				if (rs.next()){
					codigo = rs.getInt("codigo");
				
					String actualizarRegistroConteoStr = "UPDATE registro_conteo_inventario SET codigo_ajuste="+codigo+" WHERE codigo_preparacion IN ("+traerCodigosPreparaciones(con, mapa, x)+"-1)";
					ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarRegistroConteoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
					
				}
				else{
					logger.info("no encontro consecutivo");
					return ConstantesBD.codigoNuncaValido;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		logger.info("CODIGO AJUSTE "+codigo);
		
		return codigo;
	}
	
	public static String traerCodigosPreparaciones (Connection con, HashMap mapa, int pos){
		String codigos="";
		String consultarCodigosStr = "SELECT codigo FROM preparacion_toma_inventario WHERE articulo = "+mapa.get("codigo_"+pos)+" AND estado='PEN' ";
		if(Utilidades.convertirAEntero(mapa.get("codigo_lote_"+pos).toString())<=-1)
			consultarCodigosStr += "AND lote is null ";
		else
			consultarCodigosStr += "AND lote = "+mapa.get("codigo_lote_"+pos);
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarCodigosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
				codigos += rs.getInt("codigo")+", ";
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return codigos;
	}
	
	
	public static int consultarCodPrep(Connection con, int conteo){
		int codPrep = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarCodPreparacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, conteo);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				codPrep = rs.getInt("codigo_preparacion");
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return codPrep;
	}
	
	public static String consultarDescripcionAjuste (Connection con, int tipAjuste){
		String descAjuste = "";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarDescripcionAjusteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipAjuste);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				descAjuste = rs.getString("descripcion");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return descAjuste;
	}
	
	public static boolean finalizarPreparacion(Connection con, int codAlmacen, int codArticulo, int codLote){
		try
		{
			String FinalizarPrep = finalizarPreparacionStr;
			
			if (codLote != ConstantesBD.codigoNuncaValido)
				FinalizarPrep += " AND lote=?";

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(FinalizarPrep,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codAlmacen);
			ps.setInt(2, codArticulo);
			if (codLote != ConstantesBD.codigoNuncaValido)
				ps.setInt(3, codLote);

			ps.executeUpdate();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}	
		return true;
	}
	
	public static HashMap transaccionesValidas(Connection con, AjustesXInventarioFisico axif, String paramAlmacenConsig){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String valoresTransacciones = ConstantesBD.codigoNuncaValido+"";
		if (ValoresPorDefecto.getCodigoTransDevolucionPedidos(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransDevolucionPedidos(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransDevolPacientes(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransDevolPacientes(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransSoliPacientes(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransSoliPacientes(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransaccionPedidos(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransaccionPedidos(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransCompra(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransCompra(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransDevolCompra(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransDevolCompra(axif.getInstitucion(), true);
		if (ValoresPorDefecto.getCodigoTransTrasladoAlmacenes(axif.getInstitucion(), true)!="")
			valoresTransacciones += ","+ValoresPorDefecto.getCodigoTransTrasladoAlmacenes(axif.getInstitucion(), true);
		
		String consultarTransaccionesValidasStr = "SELECT DISTINCT " +
				"tti.descripcion AS desc_tipo_transaccion, " +
				"tti.codigo AS cod_tipo_transaccion, " +
				"tti.consecutivo AS conse_tipo_transaccion, " +
				"tci.codigo AS tipo " +
			"FROM " +
				"tipos_trans_inventarios tti " +
			"INNER JOIN " +
				"trans_validas_x_cc_inven tvc on (tvc.tipos_trans_inventario=tti.consecutivo) " +
			"INNER JOIN " +
				"tipos_conceptos_inv tci ON (tci.codigo = tti.tipos_conceptos_inv) " +
			"WHERE  " +
				"(tti.indicativo_consignacion =?) " +
				"AND tvc.centros_costo=? " +
				"AND tvc.institucion=? " +
				"AND tti.consecutivo NOT IN("+valoresTransacciones+") " +
		"UNION " +
				"SELECT DISTINCT " +
				"tti.descripcion AS desc_tipo_transaccion, " +
				"tti.codigo AS cod_tipo_transaccion, " +
				"tti.consecutivo AS conse_tipo_transaccion, " +
				"tci.codigo AS tipo " +
			"FROM " +
				"tipos_trans_inventarios tti " +
			"INNER JOIN " +
				"trans_validas_x_cc_inven tvc on (tvc.tipos_trans_inventario=tti.consecutivo) " +
			"INNER JOIN " +
				"tipos_conceptos_inv tci ON (tci.codigo = tti.tipos_conceptos_inv)" +
			"INNER JOIN " +
				"almacen_parametros ap ON (ap.codigo = tvc.centros_costo AND ap.tipo_consignac = 'S')" +
			"WHERE " +
				"tti.indicativo_consignacion =? " +
				"AND tvc.centros_costo=? " +
				"AND tvc.institucion=? " +
				"AND tti.consecutivo NOT IN("+valoresTransacciones+")";
		
		try
		{
			logger.info("CONSULTA TRANSACCIONES VALIDAS ->"+consultarTransaccionesValidasStr);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTransaccionesValidasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("ALMACEN ->"+axif.getAlmacen()+"<-");
			logger.info("INSTITUCION ->"+axif.getInstitucion()+"<-");
			logger.info("VALORES TRANSACCIONALES ->"+valoresTransacciones+"<-");
			
			ps.setString(1, paramAlmacenConsig);
			ps.setInt(2, axif.getAlmacen());
			ps.setInt(3, axif.getInstitucion());
			ps.setString(4, paramAlmacenConsig);
			ps.setInt(5, axif.getAlmacen());
			ps.setInt(6, axif.getInstitucion());
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("transaccionesInvalidas", valoresTransacciones);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
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
	
	public static HashMap filtrarArticulos(Connection con, AjustesXInventarioFisico axif){
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String filtro = filtrarArticulosStr;
		String codigosFiltrados;
		String consultaArticulosPreparados="";

		try
		{ 
			if(axif.getAlmacen()>0)
				filtro+= " AND apa.almacen = "+axif.getAlmacen();
			
			if(axif.getCentroAtencion()>0)
				filtro+= " AND apa.centro_atencion = "+axif.getCentroAtencion();
			
			if (axif.getCodigosArticulos().equals("")){	
				
				if (axif.getClase() > 0){
					filtro += " AND ci.codigo = "+axif.getClase();
					if (axif.getGrupo() >0){
						filtro += " AND gi.codigo = "+axif.getGrupo();
						if (axif.getSubgrupo()>0){
							filtro += " AND si.codigo = "+axif.getSubgrupo();
						}
					}
				}	
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(filtro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				//codigosFiltrados = crearCadena(mapa);
				consultaArticulosPreparados="SELECT articulo AS codigo FROM preparacion_toma_inventario WHERE articulo IN ("+filtro+") AND estado='PEN'";	
			} 
			else {
				codigosFiltrados = axif.getCodigosArticulos();
				consultaArticulosPreparados="SELECT articulo AS codigo FROM preparacion_toma_inventario WHERE articulo IN ("+codigosFiltrados+"-1) AND estado='PEN'";
			}
			
			logger.info("consulta ->"+consultaArticulosPreparados);
			PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPreparados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			String codigosPreparados=crearCadena(UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery())));
			
			String consultarArticulosStr = "SELECT " +
												"t.descripcion AS descripcion, " +
												"t.codigo," +
												"t.unidad_medida," +
												"t.codigo_interfaz," +
												"t.lote," +
												"t.existencia," +
												"t.conteo," +
												"t.costo_promedio," +
												"t.codigo_lote," +
												"to_char(t.fecha_vencimiento,'yyyy-mm-dd') as fecha_vencimiento," +
												"t.claseart as clase," +
												"t.grupoart as grupo," +
												"getPosibilidadDeAjuste(t.codigo, "+ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(axif.getInstitucion())+") AS posibilidadajuste, " +
												"MAX(getCodRegistroConteoInv(t.conteo, t.cod_prep)) AS cod_conteo," +
												"SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) AS cant_conteo," +
												"(SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) - t.existencia) AS cant_ajuste," +
												"(SUM(getCantRegistroConteo(t.cod_prep, t.conteo)) - t.existencia) * t.costo_promedio AS costo_total " +
											"FROM (" +
												"SELECT " +
													"getdescarticulosincodigo(y.codigo) as descripcion," +
													"y.codigo," +
													"getunidadmedidaarticulo(y.codigo) as unidad_medida," +
													"y.codigo_interfaz," +
													"y.lote," +
													"y.fecha_vencimiento," +
													"y.costo_promedio," +
													"y.codigo_lote," +
													"MAX(y.conteo) as conteo," +
													"y.codigo_dapa," +
													"MAX(getExistenciasArticulo(y.codigo,y.cod_prep )) as existencia," +
													"y.cod_prep, " +
													"y.claseart, " +
													"y.grupoart " +
												"FROM ( " +
													"( SELECT " +
															"a.descripcion," +
															"a.codigo," +
															"a.unidad_medida," +
															"a.codigo_interfaz," +
															"l.lote," +
															"l.fecha_vencimiento," +
															"l.codigo as codigo_lote," +
															"a.concentracion," +
															"a.forma_farmaceutica," +
															"a.naturaleza," +
															"a.costo_promedio as costo_promedio," +
															"pti.codigo_dapa," +
															"rci.numero_conteo AS conteo," +
															"MAX(rci.codigo_preparacion) AS cod_prep, " +
															"inventarios.getclasearticulo(a.codigo) as claseart," +
															"inventarios.getgrupoarticulo(a.codigo) as grupoart " +
														"FROM " +
															"articulo a," +
															"articulo_almacen_x_lote l " +
														"INNER JOIN " +
															"preparacion_toma_inventario pti ON (l.articulo=pti.articulo  and l.codigo=pti.lote)" +
														"INNER JOIN " +
															"registro_conteo_inventario rci ON (rci.articulo=pti.articulo and rci.codigo_preparacion = pti.codigo)" +
														"WHERE " +
															"a.codigo=l.articulo and a.maneja_lotes='S' " +
															"AND pti.estado = 'PEN' " +
															"AND rci.articulo IN ("+codigosPreparados+"-1) " +
															"AND rci.almacen = "+axif.getAlmacen()+" "+
														"GROUP BY " +
															"a.descripcion," +
															"a.codigo," +
															"a.unidad_medida," +
															"a.codigo_interfaz," +
															"l.lote," +
															"l.fecha_vencimiento," +
															"l.codigo," +
															"a.concentracion," +
															"a.forma_farmaceutica," +
															"a.naturaleza," +
															"a.costo_promedio," +
															"pti.codigo_dapa," +
															"rci.numero_conteo " +
														") " +
													"UNION ALL " +
														"(SELECT " +
															"a.descripcion," +
															"a.codigo," +
															"a.unidad_medida," +
															"a.codigo_interfaz," +
															"'No aplica' as lote," +
															"NULL as fecha_vencimiento," +
															"-1 as codigo_lote," +
															"a.concentracion," +
															"a.forma_farmaceutica," +
															"a.naturaleza," +
															"a.costo_promedio as costo_promedio," +
															"pti.codigo_dapa," +
															"rci.numero_conteo AS conteo," +
															"MAX(rci.codigo_preparacion) AS cod_prep, " +
															"inventarios.getclasearticulo(a.codigo) as claseart," +
															"inventarios.getgrupoarticulo(a.codigo) as grupoart " +
														"FROM " +
															"articulos_almacen ex " +
														"INNER JOIN " +
															"articulo a ON(a.codigo=ex.articulo) " +
														"INNER JOIN " +
															"preparacion_toma_inventario pti ON(pti.articulo=a.codigo) " +
														"INNER JOIN " +
															"registro_conteo_inventario rci ON(rci.articulo=pti.articulo and rci.codigo_preparacion = pti.codigo) " +
														"WHERE " +
															"a.maneja_lotes = 'N' " +
															"AND pti.estado = 'PEN' " +
															"AND rci.almacen ="+axif.getAlmacen()+" "+
															"AND rci.articulo  IN ("+codigosPreparados+"-1) " +
														"GROUP BY " +
															"a.descripcion," +
															"a.codigo," +
															"a.unidad_medida," +
															"a.codigo_interfaz," +
															"a.concentracion," +
															"a.forma_farmaceutica," +
															"a.naturaleza," +
															"a.costo_promedio," +
															"pti.codigo_dapa," +
															"rci.numero_conteo " +
														")" +
													") y " +
												"GROUP BY " +
													"y.descripcion," +
													"y.concentracion," +
													"y.forma_farmaceutica," +
													"y.naturaleza," +
													"y.codigo," +
													"y.unidad_medida," +
													"y.codigo_interfaz," +
													"y.lote," +
													"y.fecha_vencimiento," +
													"y.cod_prep," +
													"y.codigo_dapa," +
													"y.costo_promedio," +
													"y.codigo_lote, " +
													"y.claseart, " +
													"y.grupoart " +
												") t " +
												"GROUP BY " +
													"t.descripcion," +
													"t.codigo," +
													"t.unidad_medida," +
													"t.codigo_interfaz," +
													"t.lote," +
													"t.existencia," +
													"t.costo_promedio," +
													"t.conteo," +
													"t.codigo_lote," +
													"t.fecha_vencimiento, " +
													"t.claseart," +
													"t.grupoart " +
												"ORDER BY " +
													"t.descripcion";
			
			logger.info("CONSULTA:        >>> "+consultarArticulosStr);
			
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(consultarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()));
			
			mapa = organizarMapa(mapa, codigosPreparados);
			Utilidades.imprimirMapa(mapa);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * Crear un mapa solo con los registros que cumplen las validaciones para ajustar
	 * @param mapa
	 * @param codigosPreparados
	 * @return
	 */
	private static HashMap organizarMapa(HashMap mapa, String codigosPreparados) {
		HashMap mapaNuevo = new HashMap();
		boolean mostrarMensaje=false;
		int numRegistros = 0;
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++){
			if(mapa.get("posibilidadajuste_"+i).toString().equals(ConstantesBD.acronimoSi)){
				mapaNuevo.put("descripcion_"+numRegistros, mapa.get("descripcion_"+i));
				mapaNuevo.put("codigo_"+numRegistros, mapa.get("codigo_"+i));
				mapaNuevo.put("unidad_medida_"+numRegistros, mapa.get("unidad_medida_"+i));
				mapaNuevo.put("codigo_interfaz_"+numRegistros, mapa.get("codigo_interfaz_"+i));
				mapaNuevo.put("lote_"+numRegistros, mapa.get("lote_"+i));
				mapaNuevo.put("existencia_"+numRegistros, mapa.get("existencia_"+i));
				mapaNuevo.put("conteo_"+numRegistros, mapa.get("conteo_"+i));
				mapaNuevo.put("costo_promedio_"+numRegistros, mapa.get("costo_promedio_"+i));
				mapaNuevo.put("codigo_lote_"+numRegistros, mapa.get("codigo_lote_"+i));
				mapaNuevo.put("fecha_vencimiento_"+numRegistros, mapa.get("fecha_vencimiento_"+i));
				mapaNuevo.put("clase_"+numRegistros, mapa.get("clase_"+i));
				mapaNuevo.put("grupo_"+numRegistros, mapa.get("grupo_"+i));
				mapaNuevo.put("cod_conteo_"+numRegistros, mapa.get("cod_conteo_"+i));
				mapaNuevo.put("cant_conteo_"+numRegistros, mapa.get("cant_conteo_"+i));
				mapaNuevo.put("cant_ajuste_"+numRegistros, mapa.get("cant_ajuste_"+i));
				mapaNuevo.put("costo_total_"+numRegistros, mapa.get("costo_total_"+i));
				numRegistros++;
			} else {
				mostrarMensaje = true;
			}
		}
		if(mostrarMensaje)
			mapaNuevo.put("mostrar_mensaje_conteos_minimos", true);
		mapaNuevo.put("numRegistros", numRegistros);
		mapaNuevo.put("codigos_preparados", codigosPreparados);
		return mapaNuevo;
	}

	private static String crearCadena(HashMap mapa) 
	{
    	String cadena = "";
		try
		{
	    	int x;
	    	for(x=0; x<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); x++){
	    		cadena += mapa.get("codigo_"+x);
	    		cadena += ",";
	    	}
		}
		catch (Exception e) 
		{
			cadena = "";
			logger.error("ERROR  ",e);
		}
		
    	return cadena;
	}
	
	/**
	 * Consultar el Parametro Consignacion del Almacen seleccionado.
	 * @param con
	 * @param codigoAlmacen
	 * @return
	 */
	public static String consultarParametroAlmacenConsignacion(Connection con, int codigoAlmacen) 
	{
		try
		{
			String consultaParametroAlmacen= "SELECT tipo_consignac from almacen_parametros where codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaParametroAlmacen, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoAlmacen);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("tipo_consignac");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
}