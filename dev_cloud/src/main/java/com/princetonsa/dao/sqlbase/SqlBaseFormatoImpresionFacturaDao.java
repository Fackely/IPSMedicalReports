 /*
 * @(#)SqlBaseFormatoImpresionFacturaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.sqlbase;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import java.util.HashMap;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.PreparedStatementDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a> Clase para las transacciones de los formatos de impresión de la factura
 */
public class SqlBaseFormatoImpresionFacturaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseFormatoImpresionFacturaDao.class);

	/**
	 * Cadena con el statement necesario para consultar los formatos de Impresion de factura existentes
	 */
	private static final String consultarFormatosExistentesStr=" SELECT fif.codigo as codigo, " + 
																" fif.nombre_formato as nombreFormato " + 
																" FROM formato_impresion_factura fif " + 
																" WHERE fif.institucion=? " + 
																" ORDER BY fif.nombre_formato";

	/**
	 * Cadena con el statement necesario para actualizar el formatro de impresion de factura Basico
	 */
	private static final String actualizarFormatoImpresionBasicoStr=" UPDATE formato_impresion_factura " + 
																	" SET nombre_formato =? ," + 
																	" formato_preimpreso = ? , " + 
																	" cod_imprimir_servicios = ? , " + 
																	" cod_imprimir_articulos = ?  " + 
																	" WHERE codigo = ? ";

	/**
	 * Cadena con el statement necesario para consultar los datos basicos del formato de impresion de factura
	 */
	private static final String consultarFormatoFacturaBasicoStr=" SELECT fif.codigo as codigo, " + 
																" fif.nombre_formato as nombreFormato, " + 
																" fif.formato_preimpreso as formatoPreimpreso, " + 
																" fif.cod_imprimir_servicios as codigoCodServ, " +
																" tof.nombre as nomCodServ, " + 
																" fif.cod_imprimir_articulos as codigoCodArt, " + 
																" cia.descripcion as nomCodArt " + 
																" FROM formato_impresion_factura fif " + 
																" LEFT OUTER JOIN tarifarios_oficiales tof ON(fif.cod_imprimir_servicios=tof.codigo) " + 
																" LEFT OUTER JOIN codificacion_imp_articulo cia ON(fif.cod_imprimir_articulos=cia.codigo) " + 
																" WHERE fif.institucion=? ";

	/**
	 * Cadena con el statement necesario para consultar los campos de la seccion ppal para un formato de impresion nuevo
	 */
	private static final String consultarNuevoCamposSecPpalStr=" SELECT cdfif.codigo as codigo,  " +
																" cdfif.descripcion as campo " +
																" FROM cols_det_formato_imp_fact cdfif";
	
	/**
	 * Cadena con el statement necesario para consultar los campos de la seccion prinripal de formato de impresion de factura
	 */
	private static final String consultarCamposSecPpalStr="SELECT cdfif.codigo as codigo, " + 
															" cdfif.descripcion as campo, " + 
															" dfif.descripcion as valor, " + 
															" dfif.imprimir as imprimir, " + 
															" dfif.orden as orden  " + 
															" FROM cols_det_formato_imp_fact cdfif " + 
															" FULL OUTER JOIN det_formato_imp_fact dfif ON(cdfif.codigo=dfif.columna)";

	/**
	 * Cadena con el statement necesario para consultar los datos de la seccion principal del fomarto de impresion de factura
	 */
	private static final String consultarDatosSecPpalStr=" SELECT dfif.codigo_formato as codigoFormato, " + 
															" dfif.columna as codColumna, " + 
															" dfif.descripcion as valor, " + 
															" dfif.imprimir as imprimir, " + 
															" dfif.orden as orden " + 
															" FROM det_formato_imp_fact dfif " + 
															" WHERE dfif.columna = ? ";

	/**
	 * Cadena con el statement necesario pra insertar los datos de la seccion principal para un formato de impresion de factura
	 */
	private static final String insertarDatosSecPpalStr=" INSERT INTO det_formato_imp_fact " + 
														" (codigo_formato, " + 
														" columna, " + 
														" descripcion, " + 
														" imprimir, " + 
														" orden)" + 
														" VALUES(?, ?, ?, ?, ?)";

	/**
	 * Cadena con el statement necesario para actualizar los datos de la seccion principal del formato
	 */
	private static final String actualizarDatosSecPpalStr=" UPDATE det_formato_imp_fact " + 
															" SET descripcion = ? , " + 
															" imprimir = ? , " + 
															" orden = ? " + 
															" WHERE codigo_formato= ? " +
															" AND columna = ? ";

	/**
	 * Cadena con el statement necesario para consultar los datos basicos de la seccion de encabezado
	 */
	private static final String consultaBasicaSecEncabezadoStr=" SELECT se.codigo_formato as codigoFormato, " + 
																" se.observaciones_iniciales as obsIniciales, " + 
																" se.observaciones_inst as obsInstitucion" + 
																" FROM sec_encabe_formato_imp_fac se ";

	/**
	 * Cadena con el statement necesario para insertar los datos basicos del la seccion encabezado
	 */
	private static final String insertarDatosBasicosSecEncabezadoStr=" INSERT INTO sec_encabe_formato_imp_fac" + 
																	" (codigo_formato, " + 
																	" observaciones_iniciales, " + 
																	" observaciones_inst) " + 
																	" VALUES(?, ?, ?)";

	/**
	 * Cadena con el statement necesario para actualizar los datos basicos de la seccion encabezado del formato
	 */
	private static final String actualizarDatosBasicosSecEncabezadoStr=" UPDATE sec_encabe_formato_imp_fac" + 
																		" SET  observaciones_iniciales = ? , " + 
																		" observaciones_inst = ? " + 
																		" WHERE codigo_formato= ? ";

	/**
	 * Cadena con el statement necesario para consultar las sub_secciones de la seccion del encabezado
	 */
	private static final String consultarSeccionFormatoStr=" SELECT sf.codigo as codSeccion,  " +
															" sf.descripcion as descripcion, " +
															" sse.prioridad as prioridad" +
															" FROM sub_sec_encabezado sse " +
															" INNER JOIN seccion_formato_imp_fact sf ON(sf.codigo=sse.codigo_seccion) " +
															" WHERE sse.codigo_formato=? ORDER BY sf.codigo";

	/**
	 * 
	 */
	private static final String consultarSeccionFormatoNuevoStr=" SELECT sf.codigo as codSeccion,  " +
																" sf.descripcion as descripcion " +
																" FROM seccion_formato_imp_fact sf";
	/**
	 * Cadena con el statement necesario para insertar los datos de las sub secciones del encabezado
	 */
	private static final String insertarDatosSeccionFormatoStr=" INSERT INTO sub_sec_encabezado " + 
																" (codigo_formato, " + 
																" codigo_seccion, " + 
																" prioridad) " + 
																" VALUES(?, ?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos de las sub secciones
	 */
	private static final String actualizarDatosSeccionFormatoStr=" UPDATE sub_sec_encabezado " + 
																" SET  prioridad = ? " + 
																" WHERE codigo_formato= ? " +
																" AND codigo_seccion = ?  ";

	/**
	 * Cadena con el statement necesario para consultar los campos dad una sub seccion del encabezado
	 */
	private static final String consultarCamposXSeccionNuevoStr=" SELECT DISTINCT sxc."+ValoresPorDefecto.getNumeroFilaParaConsultas() +"," +
															" ce.codigo as codCampo,  " + 
															" sxc.codigo_seccion as codSeccion, " + 
															" ce.descripcion as campo " + 
															" FROM campos_encabezado ce  " + 
															" INNER JOIN seccion_x_campo sxc ON(ce.codigo=sxc.campo)  " + 
															" INNER JOIN seccion_formato_imp_fact sf ON(sxc.codigo_seccion=sf.codigo) " + 
															" WHERE sxc.codigo_seccion = ? ORDER BY sxc."+ValoresPorDefecto.getNumeroFilaParaConsultas();

	/**
	 * Cadena con el statement necesario para consultar los campos dad una sub seccion del encabezado
	 */
	private static final String consultarCamposXSeccionStr="SELECT sxc."+ValoresPorDefecto.getNumeroFilaParaConsultas() +", " +
															" ce.codigo as codCampo,  " +
															" sxc.codigo_seccion as codSeccion, " +
															" ce.descripcion as campo, " +
															" dsc.imprimir as imprimir, " +
															" dsc.orden as orden " +
															" FROM det_sub_sec_encabezado dsc " +
															" INNER JOIN seccion_x_campo sxc ON(dsc.campo=sxc.campo and dsc.codigo_seccion=sxc.codigo_seccion) " +
															" INNER JOIN campos_encabezado ce on (sxc.campo=ce.codigo) " +
															" WHERE dsc.codigo_seccion=? AND dsc.codigo_formato=?  order by sxc."+ValoresPorDefecto.getNumeroFilaParaConsultas();
	
	/**
	 * Cadena con el statement necesario para consultar el valor de cada campo dado el codigo del campo
	 */
	private static final String consultarValorCamposXSeccionStr=" SELECT dse.codigo_formato as codigoFormato, " + 
																" dse.codigo_seccion as codSeccion, " + 
																" dse.campo as codCampo, " + 
																" dse.imprimir as imprimir, " + 
																" dse.orden as orden " + 
																" FROM det_sub_sec_encabezado dse " + 
																" WHERE dse.campo=? ";

	/**
	 * Cadena con el statement necesario para insertar el valor de los campos de las sub secciones del encabezado
	 */
	private static final String insertarDatosCamposSubSeccionStr=" INSERT INTO det_sub_sec_encabezado " + 
																" (codigo_formato, " + 
																" codigo_seccion, " + 
																" campo," + 
																" imprimir, " + 
																" orden) " + 
																" VALUES(?, ?, ?, ?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos de los campos de las subsecciones de la seccion de un encabezado
	 */
	private static final String actualizarDatosCamposSubSeccionStr=" UPDATE det_sub_sec_encabezado " + 
																	" SET imprimir = ?, " + 
																	" orden= ? " + 
																	" WHERE codigo_formato= ? " +
																	" AND codigo_seccion = ? " + 
																	" AND campo = ? " ; 

	/**
	 * Cadena con el statement necesario para la consulta basica de los datos de la seccion de articulos
	 */
	private static final String consultaBasicaSecArticuloStr=" SELECT sa.codigo_formato as codigoFormato, " + 
															" sa.detalle_articulos as codDetArticulos, " + 
															" tda.descripcion as nomDetArticulos " + 
															" FROM sec_art_formato_imp_fact sa " + 
															" FULL OUTER JOIN tipos_det_art_formato_fact tda ON(sa.detalle_articulos=tda.codigo) ";

	/**
	 * Cadena con el statement necesario para insertar los datos basicos de las seccion de articulos del formato de impresion de factura
	 */
	public static final String insertarDatosBasicosSecArtStr=" INSERT INTO sec_art_formato_imp_fact " + 
															" (codigo_formato," + 
															" detalle_articulos) " + 
															" VALUES(?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos basicos de la seccion de articulos de un formato
	 */
	public static final String actualizarDatosBasicosSecArtStr=" UPDATE sec_art_formato_imp_fact " + 
																" SET detalle_articulos = ? " + 
																" WHERE codigo_formato = ? ";

	/**
	 * Cadena con el statement necesario para consultar los nivels de rompimiento de la seccion de articulos
	 */
	private static final String consultarNivelesArticulosStr=" SELECT nra.codigo as codNivel, " + 
															" nra.descripcion as nomNivel " + 
															" FROM nivel_rompimiento_art nra ";

	/**
	 * Cadena con el statement necesario para consultar los datos de la seccion de articulos
	 */
	private static final String consultaDatosSecArticulosStr=" SELECT DISTINCT dsa.codigo_formato as codigoFormato, " + 
															" nra.codigo as codNivel, " + 
															" nra.descripcion as nomNivel, " + 
															" dsa.tipo_rompimiento as codTipoRompimiento, " + 
															" dsa.imprimir_detalle as impDetalleArt, " + 
															" dsa.imprimir_subtotal as impSubTotArt " + 
															" FROM det_art_formato_fact dsa " + 
															" INNER JOIN nivel_rompimiento_art nra on (dsa.nivel_rompimiento=nra.codigo) where dsa.codigo_formato=?";

	/**
	 * Cadena con el statement necesario para insertar los datos de la seccion de articulos del formato de impresion de factura
	 */
	private static final String insertarDatosSecArticulosStr=" INSERT INTO det_art_formato_fact " + 
															" (codigo_formato, " + 
															" nivel_rompimiento, " + 
															" tipo_rompimiento, " + 
															" imprimir_detalle, " + 
															" imprimir_subtotal) " + 
															" VALUES(?, ?, ?, ?, ?)";

	/**
	 * Cadena con el statement necesario para actualizar los datos de la seccion de articulos
	 */
	private static final String actualizarDatosSecArticulosStr=" UPDATE det_art_formato_fact " + 
																" SET tipo_rompimiento = ? , " + 
																" imprimir_detalle = ? , " + 
																" imprimir_subtotal = ? " + 
																" WHERE codigo_formato = ? " +
																" AND nivel_rompimiento = ?  " ;

	/**
	 * Cadena con el statement necesario para consultar los datos basicos de la seccion de servicio
	 */
	private static final String consultaBasicaSecServicioStr=" SELECT ss.codigo_formato as codigoFormato, " + 
															" ss.mostrar_det_serv_qx as mostrarDetalle " + 
															" FROM sec_serv_formato_imp_fact ss ";

	/**
	 * Cadena con el statement necesario para insertar los datos basicos de las seccion de servicio del formato de impresion de factura
	 */
	public static final String insertarDatosBasicosSecServStr=" INSERT INTO sec_serv_formato_imp_fact " + 
															" (codigo_formato," + 
															" mostrar_det_serv_qx) " + 
															" VALUES(?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos basicos de la seccion de servicios
	 */
	public static final String actualizarDatosBasicosSecServStr=" UPDATE sec_serv_formato_imp_fact " + 
																" SET mostrar_det_serv_qx = ? " + 
																" WHERE codigo_formato =? ";

	/**
	 * Cadena con el statement necesario para consultar los niveles de rompimiento de la seccion de servicios
	 */
	private static final String consultarNivelesServiciosStr=" SELECT nrs.codigo as codNivel, " + 
															" nrs.descripcion as nomNivel " + 
															" FROM nivel_rompimiento_serv nrs ";

	/**
	 * Cadena con el statement necesario para consultar los datos de la seccin de servicios dado el codigo de formato y el nivel de rompimiento
	 */
	private static final String consultaDatosSecServiciosStr=" SELECT dss.codigo_formato as codigoFormato, " + 
															" nrs.codigo as codNivel, " + 
															" nrs.descripcion as nomNivel, " + 
															" dss.tipo_rompimiento as codTipoRompimiento, " + 
															" dss.imprimir_detalle as impDetalleServ, " + 
															" dss .imprimir_subtotal  as impSubTotServ" + 
															" FROM det_sec_serv_formato_fact dss " + 
															" INNER JOIN nivel_rompimiento_serv nrs ON (dss.nivel_rompimiento=nrs.codigo) " +
															" WHERE dss.codigo_formato=?";
	/**
	 * 
	 */
	private static final String consultaDatosSecServiciosNuevosStr="SELECT codigo as codnivel,descripcion as nomnivel from nivel_rompimiento_serv";
	
	/**
	 * Cadena con el statement necesario para insertar los datos de la seccion de servicios del formato de impresion de factura
	 */
	private static final String insertarDatosSecServiciosStr=" INSERT INTO det_sec_serv_formato_fact " + 
															" (codigo_formato, " + 
															" nivel_rompimiento, " + 
															" tipo_rompimiento,  " + 
															" imprimir_detalle, " + 
															" imprimir_subtotal) " + 
															" VALUES(?, ?, ?, ?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos de la seccion de servicios
	 */
	public static final String actualizarDatosSecServiciosStr=" UPDATE det_sec_serv_formato_fact " + 
															" SET tipo_rompimiento = ? ,  " + 
															" imprimir_detalle = ? , " + 
															" imprimir_subtotal = ? " + 
															" WHERE codigo_formato = ? " +
															" AND nivel_rompimiento = ? " ;

	/**
	 * Cadena con el statement necesario para consultar los datos basicos de la seccion de totales del formato de impreion de factura dado un codigo de formato
	 */
	private static final String consultaBasicaSecTotalesStr=" SELECT st.codigo_formato as codigoFormato, " + 
															" st.imprimir_valor_letras as impValLetras, " + 
															" st.valor_en_letras as codValLetras, " + 
															" vl.descripcion as nomValLetras " + 
															" FROM sec_totales_formato_fact st " + 
															" FULL OUTER JOIN valor_en_letras vl ON(st.valor_en_letras=vl.codigo) ";

	/**
	 * Cadena con el statement necesario para insertar los datos basciso de la seccion de totales del formato
	 */
	private static final String insertarDatosBasicosSecTotalesStr=" INSERT INTO  sec_totales_formato_fact " + 
																	" (codigo_formato, " + 
																	" imprimir_valor_letras, " + 
																	" valor_en_letras) " + 
																	" VALUES(?, ?, ?)";

	/**
	 * Cadena con el statement necesario para actualizar los datos basicos de la seccion de totales
	 */
	private static final String actualizarDatosBasicosSecTotalesStr=" UPDATE sec_totales_formato_fact " +
																	" SET imprimir_valor_letras = ? , " +
																	" valor_en_letras = ? " +
																	" WHERE codigo_formato = ? ";

	/**
	 * Cadena con el statement necesario para consultar los campos de la seccion de totales de un formato de impresion de factura
	 */
	private static final String consultarCamposSecTotalesStr=" SELECT DISTINCT cst.codigo as codCampo, " + 
															" cst.descripcion as nomCampo " + 
															" FROM campos_sec_tot_formato_fact cst ";

	/**
	 * Cadena con el statement necesario para consultar los datos de la seccion de totales segun un codigo de formato y el codigo del campo especificamente
	 */
	private static final String consultaDatosSecTotalesStr=" SELECT dst.codigo_formato as codigoFormato, " +
															" cst.codigo as codCampo," + 
															" cst.descripcion as nomCampo, " + 
															" dst.valor as valorCampo " + 
															" FROM det_sec_tot_formato_fact dst " + 
															" INNER JOIN campos_sec_tot_formato_fact cst ON(dst.campo=cst.codigo) where dst.codigo_formato=?";

	/**
	 * 
	 */
	private static final String consultaDatosSecTotalesNuevosStr=" SELECT codigo as codCampo, " +
																" descripcion as nomCampo " +
																" from campos_sec_tot_formato_fact";
	
	/**
	 * Cadena con el statement necesario para insertar los valores de la seccion de totales del formato
	 */
	private static final String insertarDatosSecTotalesStr=" INSERT INTO det_sec_tot_formato_fact " + 
															" (codigo_formato, " + 
															" campo, " + 
															" valor) " +
															" VALUES(?, ? , ?) ";

	/**
	 * Cadena con el statement necesario para actualizar los datos de la seccion de totales de un formato
	 */
	private static final String actualizarDatosSecTotalesStr=" UPDATE det_sec_tot_formato_fact " + 
																" SET valor = ? " + 
																" WHERE codigo_formato = ? " +
																" AND campo = ?  "; 

	/**
	 * Cadena con el statement necesario para consultar los datos basicos de la seccion de nota de pie de pagina
	 */
	private static final String consultaBasicaSecNotaPieStr=" SELECT snp.codigo_formato as codigoFormato, " + 
															" snp.obs_actividad_economica as obsActEcono, " + 
															" snp.imprimir_firmas as impFirmas " + 
															" FROM sec_nota_pie_formato_fact snp ";

	/**
	 * Cadena con el statement necesario para insertar los datos basicos de la seccion de nota de pie de pagina de un formato
	 */
	private static final String insertarDatosBasicosSecNotaPieStr=" INSERT INTO sec_nota_pie_formato_fact " + 
																	" (codigo_formato," + 
																	" obs_actividad_economica," + 
																	" imprimir_firmas) " + 
																	" VALUES(?, ?, ?)";

	/**
	 * Cadena con el statement necesario para actualizar los datos basicos de la seccion de nota de pie de pagina
	 */
	private static final String actualizarDatosBasicosSecNotaPieStr=" UPDATE sec_nota_pie_formato_fact " + 
																	" SET obs_actividad_economica = ? ," + 
																	" imprimir_firmas = ? " + 
																	" WHERE codigo_formato = ? ";

	/**
	 * Cadena con el statement necesario para consultar los datos del detalle de la seccion nota de pie de pagina
	 */
	private static final String consultaDatosSecNotaPieStr=" SELECT dsn.codigo_formato as codigoFormato, " + 
															" dsn.firma as codFirma, " + 
															" tf.descripcion as nomFirma, " + 
															" dsn.desc_firma as descripcionFirma " + 
															" FROM det_sec_nota_formato_fact dsn " + 
															" INNER JOIN tipo_firma tf ON(dsn.firma=tf.codigo) ";

	/**
	 * Cadena con el statement necesario para insertar las firmas de la seccion de nota de pie de pagina de un formato
	 */
	private static final String insertarFirmasSecNotaPieStr=" INSERT INTO det_sec_nota_formato_fact" + 
															" (codigo_formato, " + 
															" firma, " + 
															" desc_firma) " + 
															" VALUES(?, ?, ?) ";

	/**
	 * Cadena con el statement necesario para actualizar las firmas de la seccion de nota de pie de pagina del formato
	 */
	private static final String actualizarFirmasSecNotaPieStr=" UPDATE det_sec_nota_formato_fact" + 
																" SET desc_firma = ? " + 
																" WHERE codigo_formato = ? AND firma = ? ";
	/**
	 * Cadena con el statement necesario para saber si existe o no una firma
	 */
	private static final String existeFirmaSecNotaPieStr="SELECT count(1) as cantidad FROM det_sec_nota_formato_fact where codigo_formato = ? AND firma = ?";
	
	/**
	 * Cadena para consultar una subsección del encabezado
	 */
	private static final String consultarSubSeccionEncabezadoStr = " SELECT DISTINCT d."+ValoresPorDefecto.getNumeroFilaParaConsultas() +", " + 
		"d.codigo_formato AS codigoformato, "+
		"d.campo AS codigocampo, "+
		"c.descripcion AS descripcion, "+
		"d.imprimir AS imprimir, "+
		"d.orden AS orden "+ 
		"FROM det_sub_sec_encabezado d "+
		"INNER JOIN campos_encabezado c ON(c.codigo=d.campo) "+ 
		"WHERE d.codigo_formato = ? and d.codigo_seccion = ?";
	
	/**
	 * Cadena para consultar las prioridades de la seccion encabezado
	 */
	private static final String consultarPrioridadesSeccionEncabezadoStr = " SELECT "+ 
		"codigo_seccion AS codigo_seccion, "+
		"prioridad AS prioridad "+ 
		"FROM sub_sec_encabezado "+ 
		"WHERE "+ 
		"codigo_formato = ? AND "+ 
		"codigo_seccion != "+ConstantesBD.codigoSeccionFormatoFacturaInstitucion+" AND "+ 
		"prioridad IS NOT NULL "+ 
		"ORDER BY prioridad";

	/**
	 * Cadena con el statement necesario para cosnultar los datos parametrizados de la seccion ppal de un formato de impresion dado.
	 */
	private static final String consultaDatosSecPpalParametrizadosStr=" SELECT case when dfif.descripcion is null or dfif.descripcion = '' then '' else dfif.descripcion end  as descripcion, " +
																	  " dfif.columna as columna, " +
																	  " dfif.imprimir as imprimir, " +
																	  " dfif.orden as orden " +
																	  " FROM det_formato_imp_fact dfif " +
																	  " WHERE dfif.codigo_formato=? " +
																	  " AND dfif.imprimir=true " +
																	  " ORDER BY orden";
	
	/**
	 * Cadena con el statement necesario para consultar los campos y los valores correspondientes de la seccion
	 *  de totales de un formato de impresion de factura
	 */
	private static final String consultaCamposValoresSecTotalesStr=" SELECT dst.campo as codigocampo," +
																   " case when dst.valor='' or dst.valor is null then cst.descripcion ELSE dst.valor END as descripcioncampo, " +
																   " gettotalesfacturaxcampoformato(?,dst.campo) as total " +
																   " FROM det_sec_tot_formato_fact dst " +
																   " INNER JOIN campos_sec_tot_formato_fact cst ON(dst.campo=cst.codigo)" +
																   " WHERE dst.codigo_formato = ? ";
	
	
	/**
	 * Cadena con el statement necesario para consultar los campos y los valores correspondientes de la seccion
	 *  de totales de un formato de impresion de factura
	 */
	private static final String consultaValorTotalXFacturaXCampoStr=" SELECT gettotalesfacturaxcampoformato(?,?) as total ";

	
	
	/**
	 * Método para consultar los formatos de impresion de factura existentes en el sistema
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFormatosExistentes(Connection con, int institucion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarFormatosExistentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando formatos de impresion de factura existentes [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar los datos estaticos de la seccion de encabezado
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDatosSecEncabezado(Connection con, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst;
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecEncabezadoStr + " WHERE se.codigo_formato=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
	        pst.close();
			return mapaRetorno;
			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los datos estaticos de la seccion e encabezado [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar los datos del formato de impresion de factura basicos
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFormatoFacturaBasico(Connection con, int institucion, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst;

			pst= new PreparedStatementDecorator(con.prepareStatement(consultarFormatoFacturaBasicoStr + " AND fif.codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			pst.setInt(2, codigoFormato);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el formato de factura basico [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar los datos de la seccion principal
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDatosSeccionPpal(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst;
			if(codigoFormato == 0)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarNuevoCamposSecPpalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarCamposSecPpalStr + " WHERE dfif.codigo_formato = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
				parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			}

			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando lo datos de la seccion principal del formato [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar las subsecciones de la seccion de encabezado del formato de impresion de factura asi mismo trae los campos y el valor de cada uno de ellos
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarTodoSubSecciones(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst=null;
			if(codigoFormato == 0)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarSeccionFormatoNuevoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("consultarSeccionFormatoNuevoStr>>>>>>>IF>>>>>>>>>>>>>"+consultarSeccionFormatoNuevoStr);
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultarSeccionFormatoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("consultarSeccionFormatoNuevoStr>>>>>>>>>ELSE>>>>>>>>>>>"+consultarSeccionFormatoNuevoStr);
				pst.setInt(1, codigoFormato);
			}
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			int numReg=Integer.parseInt(parametricos.get("numRegistros").toString());
			for (int i=0; i < numReg; i++)
			{
				PreparedStatementDecorator ps;
				if(codigoFormato == 0)
				{
					logger.info("**********************************************"+ValoresPorDefecto.getNumeroFilaParaConsultas());
					ps= new PreparedStatementDecorator(con.prepareStatement(consultarCamposXSeccionNuevoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("consultarCamposXSeccionNuevoStr>>>>>>>>>>>>>>>>>>>>>>>>>>>if>>>>>>>>>>>>>>"+consultarCamposXSeccionNuevoStr);
					ps.setInt(1, Integer.parseInt(parametricos.get("codseccion_" + i) + ""));
				}
				else
				{
					ps= new PreparedStatementDecorator(con.prepareStatement(consultarCamposXSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("consultarCamposXSeccionNuevoStr>>>>>>>>>>>>>>>>>>>>>>>>>>>else>>>>>>>>>>>>>>"+consultarCamposXSeccionNuevoStr);
					ps.setInt(1, Integer.parseInt(parametricos.get("codseccion_" + i) + ""));
					ps.setInt(2, codigoFormato);
				}
				HashMap mapaTemp=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("><><><><><><><><><><>"+mapaTemp);
				
				mapaTemp.put("indiceSeccion", i + "");
				parametricos.put("camposeccion_" + i, (HashMap)mapaTemp.clone());
			}
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando todo lo de las sub secciones [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar todo lo de la seccion de articulos
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarSecArticulos(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst;
			if(codigoFormato == 0)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecArticuloStr + " WHERE sa.codigo_formato= ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
			}
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
			PreparedStatementDecorator p;
			if(codigoFormato == 0)
			{
				p= new PreparedStatementDecorator(con.prepareStatement(consultarNivelesArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			}
			else
			{
				p= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				p.setInt(1, codigoFormato);
			}

			parametricos.put("detallenivel", UtilidadBD.cargarValueObject(new ResultSetDecorator(p.executeQuery())));
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando todo de la seccion de articulos [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar todo lo relacionado con la seccion de servicios del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarSecServicios(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst;
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecServicioStr + " WHERE ss.codigo_formato = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
			PreparedStatementDecorator p;
			if(codigoFormato == 0)
			{
				p= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecServiciosNuevosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				p= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				p.setInt(1, codigoFormato);
			}
			parametricos.put("detallenivel", UtilidadBD.cargarValueObject(new ResultSetDecorator(p.executeQuery())));
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando todo de la seccion de servicios [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para consultar los datos de la seccion de totales de formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarSecTotales(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst;
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecTotalesStr + " WHERE st.codigo_formato = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);

			PreparedStatementDecorator ps;
			if(codigoFormato == 0)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecTotalesNuevosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoFormato);
			}

			parametricos.put("detallecampo", UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando todo de la seccion de totales [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para la consulta de los datos de la seccion de nota de pie pagina del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarDatosSecNotaPie(Connection con, int codigoFormato) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst;
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaBasicaSecNotaPieStr + " WHERE snp.codigo_formato = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando lo datos basicos de la seccion Nota de pie de pagina del formato [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Metodo para cosultar las firmas dado un codigo de formato
	 * 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFirmas(Connection con, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst;
			if(codigoFormato == 0)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecNotaPieStr + " WHERE dsn.codigo_formato = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
			}
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las firmas de la seccion Nota de pie de pagina del formato [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return null;
		}
	}

	/**
	 * Método para insertar un formato de impresion basico
	 * 
	 * @param con
	 * @param mapaFormatoBasico
	 * @return
	 * @throws SQLException
	 */
	public static int insertarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int institucion, String insertarFormatoImpresionBasicoStr) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarFormatoImpresionBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, mapaFormatoBasico.get("nombreformato").toString());
			tmp=mapaFormatoBasico.get("formatopreimpreso").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(2, true);
			}
			else
			{
				pst.setBoolean(2, false);
			}
			if(mapaFormatoBasico.get("codigocodserv") == null || (mapaFormatoBasico.get("codigocodserv").toString()).equals("-1") || (mapaFormatoBasico.get("codigocodserv").toString()).equals("null"))
			{
				pst.setObject(3, null);
			}
			else
			{
				pst.setInt(3, Integer.parseInt(mapaFormatoBasico.get("codigocodserv").toString()));
			}
			if(mapaFormatoBasico.get("codigocodart") == null || (mapaFormatoBasico.get("codigocodart").toString()).equals("-1") || (mapaFormatoBasico.get("codigocodserv").toString()).equals("null"))
			{
				pst.setObject(4, null);
			}
			else
			{
				pst.setInt(4, Integer.parseInt(mapaFormatoBasico.get("codigocodart").toString()));
			}
			pst.setInt(5, institucion);
			return pst.executeUpdate();

		}
		catch (SQLException e)
		{
			logger.error("Error insertando el formato de impresion de factura basico [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos del formato de impresion de factura basico
	 * 
	 * @param con
	 * @param mapaFormatoBasico
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int codigoFormato) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarFormatoImpresionBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, mapaFormatoBasico.get("nombreformato").toString());
			tmp=mapaFormatoBasico.get("formatopreimpreso").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(2, true);
			}
			else
			{
				pst.setBoolean(2, false);
			}
			if(mapaFormatoBasico.get("codigocodserv") == null || (mapaFormatoBasico.get("codigocodserv").toString()).equals("-1"))
			{
				pst.setObject(3, null);
			}
			else
			{
				pst.setInt(3, Integer.parseInt(mapaFormatoBasico.get("codigocodserv").toString()));
			}
			if(mapaFormatoBasico.get("codigocodart") == null || (mapaFormatoBasico.get("codigocodart").toString()).equals("-1"))
			{
				pst.setObject(4, null);
			}
			else
			{
				pst.setInt(4, Integer.parseInt(mapaFormatoBasico.get("codigocodart").toString()));
			}
			pst.setInt(5, codigoFormato);
			return pst.executeUpdate();

		}
		catch (SQLException e)
		{
			logger.error("Error actualizando el formato de impresion de factura basico [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar los datos de la seccion principal del formato de impresion de factura
	 * 
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public static int insertarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException
	{
		String tmp="";
		try
		{	
			for (int i=0; i < Integer.parseInt(mapaSeccionPpal.get("numRegistros").toString()); i++)
			{

				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosSecPpalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
				pst.setInt(2, Integer.parseInt(mapaSeccionPpal.get("codigo_" + i).toString()));
				pst.setString(3, mapaSeccionPpal.get("valor_" + i).toString());
				tmp=mapaSeccionPpal.get("imprimir_" + i).toString();
				if(tmp.equals("true"))
				{
					pst.setBoolean(4, true);
				}
				else
				{
					pst.setBoolean(4, false);
				}
				if(mapaSeccionPpal.get("orden_" + i) == null || (mapaSeccionPpal.get("orden_" + i).toString()).equals(""))
				{
					pst.setObject(5, null);
				}
				else
				{
					pst.setInt(5, Integer.parseInt(mapaSeccionPpal.get("orden_" + i).toString()));
				}
				pst.executeUpdate();

			}
			return 1;

		}
		catch (SQLException e)
		{
			logger.error("Error insertando los datos de la seccion principal del formato de impresión de factura [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos de la seccion principal del formato de impresion de factura
	 * 
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public static int actualizarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException
	{
		String tmp="";
		try
		{
			for (int i=0; i < Integer.parseInt(mapaSeccionPpal.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosSecPpalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1, mapaSeccionPpal.get("valor_" + i).toString());
				tmp=mapaSeccionPpal.get("imprimir_" + i).toString();
				if(tmp.equals("true"))
				{
					pst.setBoolean(2, true);
				}
				else
				{
					pst.setBoolean(2, false);
				}
				if(mapaSeccionPpal.get("orden_" + i) == null || (mapaSeccionPpal.get("orden_" + i).toString()).equals(""))
				{
					pst.setObject(3, null);
				}
				else
				{
					pst.setInt(3, Integer.parseInt(mapaSeccionPpal.get("orden_" + i).toString()));
				}
				pst.setInt(4, codigoFormato);
				pst.setInt(5, Integer.parseInt(mapaSeccionPpal.get("codigo_" + i).toString()));
				pst.executeUpdate();
			}
			return 1;

		}
		catch (SQLException e)
		{
			logger.error("Error actualizando los datos de la seccion principal del formato de impresión de factura [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * 
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public static int insertarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosBasicosSecEncabezadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			pst.setString(2, mapaDatosSecEncabezado.get("obsiniciales").toString());
			pst.setString(3, mapaDatosSecEncabezado.get("obsinstitucion").toString());
			return pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error insertando los datos de la seccion principal del formato de impresión de factura [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * 
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public static int actualizarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosBasicosSecEncabezadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, mapaDatosSecEncabezado.get("obsiniciales").toString());
			pst.setString(2, mapaDatosSecEncabezado.get("obsinstitucion").toString());
			pst.setInt(3, codigoFormato);
			return pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando los datos de la seccion principal del formato de impresión de factura [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public static int insertarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException
	{
		String tmp="";
		try
		{
			for (int i=0; i < Integer.parseInt(mapaSeccionEncabezado.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosSeccionFormatoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
				pst.setInt(2, Integer.parseInt(mapaSeccionEncabezado.get("codseccion_" + i).toString()));
				
				if(mapaSeccionEncabezado.get("prioridad_" + i) == null || (mapaSeccionEncabezado.get("prioridad_" + i).toString()).equals(""))
				{
					pst.setObject(3, null);
				}
				else
				{
					pst.setInt(3, Integer.parseInt(mapaSeccionEncabezado.get("prioridad_" + i).toString()));
				}
				pst.executeUpdate();
				HashMap mapaCamposSeccion=(HashMap)((HashMap)mapaSeccionEncabezado.get("camposeccion_" + i)).clone();
				int numReg=Integer.parseInt(mapaCamposSeccion.get("numRegistros").toString());
				for (int j = 0 ; j < numReg ; j++)
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDatosCamposSubSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoFormato);
					ps.setInt(2, Integer.parseInt(mapaCamposSeccion.get("codseccion_" + j).toString()));
					ps.setInt(3, Integer.parseInt(mapaCamposSeccion.get("codcampo_" + j).toString()));
					tmp=mapaCamposSeccion.get("imprimir_" + j).toString();
					if(tmp.equals("true"))
					{
						ps.setBoolean(4, true);
					}
					else
					{
						ps.setBoolean(4, false);
					}
					if(i < 1)
					{
						tmp=mapaCamposSeccion.get("orden_" + j).toString();
						if(!tmp.equals(""))
						{
							ps.setInt(5, Integer.parseInt(tmp));
						}
						else
						{
							ps.setObject(5, null);
						}
					}
					else
					{
						ps.setObject(5, null);
					}
					ps.executeUpdate();
				}
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando todo lo de las sub secciones [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException
	{
		String tmp="";
		try
		{
			for (int i=0; i < Integer.parseInt(mapaSeccionEncabezado.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosSeccionFormatoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(mapaSeccionEncabezado.get("prioridad_" + i) == null || (mapaSeccionEncabezado.get("prioridad_" + i).toString()).equals(""))
				{
					pst.setObject(1, null);
				}
				else
				{
					pst.setInt(1, Integer.parseInt(mapaSeccionEncabezado.get("prioridad_" + i).toString()));
				}
				pst.setInt(2, codigoFormato);
				pst.setInt(3, Integer.parseInt(mapaSeccionEncabezado.get("codseccion_" + i).toString()));
				pst.executeUpdate();
				
				HashMap mapaCamposSeccion=(HashMap)((HashMap)mapaSeccionEncabezado.get("camposeccion_" + i)).clone();
				int numReg=Integer.parseInt(mapaCamposSeccion.get("numRegistros").toString());
				for (int j=0; j < numReg; j++)
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosCamposSubSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					tmp=mapaCamposSeccion.get("imprimir_" + j).toString();
					if(tmp.equals("true"))
					{
						ps.setBoolean(1, true);
					}
					else
					{
						ps.setBoolean(1, false);
					}
					
					if(i == 0)
					{
						if(mapaCamposSeccion.get("orden_" + j)!=null&&!mapaCamposSeccion.get("orden_" + j).equals(""))
						{
							ps.setInt(2, Integer.parseInt(mapaCamposSeccion.get("orden_" + j).toString()));
						}
						else
						{
							ps.setObject(2, null);
						}
					}
					else
					{
						ps.setObject(2, null);
					}
					ps.setInt(3, codigoFormato);
					ps.setInt(4, Integer.parseInt(mapaCamposSeccion.get("codseccion_" + j).toString()));
					ps.setInt(5, Integer.parseInt(mapaCamposSeccion.get("codcampo_" + j).toString()));
					ps.executeUpdate();
				}
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de las sub secciones [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Metodo para insertar los datos de la seccin de servicios del formato de impresion de presupuesto
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public static int insertarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosBasicosSecServStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			tmp=mapaSecServicios.get("mostrardetalle").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(2, true);
			}
			else
			{
				pst.setBoolean(2, false);
			}
			pst.executeUpdate();
			HashMap mapaDetSecServ=(HashMap)((HashMap)mapaSecServicios.get("detallenivel")).clone();
			for (int i=0; i < Integer.parseInt(mapaDetSecServ.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDatosSecServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoFormato);
				ps.setInt(2, Integer.parseInt(mapaDetSecServ.get("codnivel_" + i).toString()));
				if(mapaDetSecServ.get("codtiporompimiento_" + i)==null||(mapaDetSecServ.get("codtiporompimiento_" + i).toString()).equals("-1"))
				{
					ps.setObject(3, null);
				}
				else
				{
					ps.setInt(3, Integer.parseInt(mapaDetSecServ.get("codtiporompimiento_" + i).toString()));
				}
				tmp=mapaDetSecServ.get("impdetalleserv_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(4, true);
				}
				else
				{
					ps.setBoolean(4, false);
				}
				tmp=mapaDetSecServ.get("impsubtotserv_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(5, true);
				}
				else
				{
					ps.setBoolean(5, false);
				}
				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando todo lo de la seccion de servicios [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Metodo para actualizar los datos de la seccion de servicios del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosBasicosSecServStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tmp=mapaSecServicios.get("mostrardetalle").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(1, true);
			}
			else
			{
				pst.setBoolean(1, false);
			}
			pst.setInt(2, codigoFormato);
			pst.executeUpdate();
			HashMap mapaDetSecServ=(HashMap)((HashMap)mapaSecServicios.get("detallenivel")).clone();
			for (int i=0; i < Integer.parseInt(mapaDetSecServ.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosSecServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(mapaDetSecServ.get("codtiporompimiento_" + i)==null||(mapaDetSecServ.get("codtiporompimiento_" + i).toString()).equals("-1"))
				{
					ps.setObject(1, null);
				}
				else
				{
					ps.setInt(1, Integer.parseInt(mapaDetSecServ.get("codtiporompimiento_" + i).toString()));
				}
				tmp=mapaDetSecServ.get("impdetalleserv_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(2, true);
				}
				else
				{
					ps.setBoolean(2, false);
				}
				tmp=mapaDetSecServ.get("impsubtotserv_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(3, true);
				}
				else
				{
					ps.setBoolean(3, false);
				}
				ps.setInt(4, codigoFormato);
				ps.setInt(5, Integer.parseInt(mapaDetSecServ.get("codnivel_" + i).toString()));

				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de la seccion de servicios [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar los datos de la seccion de articulos del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public static int insertarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosBasicosSecArtStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			if(mapaSecArticulos.get("coddetarticulos")==null||(mapaSecArticulos.get("coddetarticulos")+"").equals("")||(mapaSecArticulos.get("coddetarticulos")+"").equalsIgnoreCase("null"))
			{
				pst.setObject(2, null);
			}
			else
			{
				pst.setInt(2, Integer.parseInt(mapaSecArticulos.get("coddetarticulos").toString()));
			}
			pst.executeUpdate();
			HashMap mapaDetSecArt=(HashMap)((HashMap)mapaSecArticulos.get("detallenivel")).clone();
			for (int i = 0 ; i < Integer.parseInt(mapaDetSecArt.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDatosSecArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoFormato);
				ps.setInt(2, Integer.parseInt(mapaDetSecArt.get("codnivel_" + i).toString()));
				if(mapaDetSecArt.get("codtiporompimiento_" + i)==null||(mapaDetSecArt.get("codtiporompimiento_" + i).toString()).equals("-1"))
				{
					ps.setObject(3, null);
				}
				else
				{
					ps.setInt(3, Integer.parseInt(mapaDetSecArt.get("codtiporompimiento_" + i).toString()));
				}
				tmp=mapaDetSecArt.get("impdetalleart_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(4, true);
				}
				else
				{
					ps.setBoolean(4, false);
				}
				tmp=mapaDetSecArt.get("impsubtotart_" + i).toString();
				if(tmp.equals("true"))
				{
					ps.setBoolean(5, true);
				}
				else
				{
					ps.setBoolean(5, false);
				}
				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando todo lo de la seccion de articulos [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos de la seccion de articulos del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosBasicosSecArtStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(mapaSecArticulos.get("coddetarticulos")==null||(mapaSecArticulos.get("coddetarticulos")+"").equals("")||(mapaSecArticulos.get("coddetarticulos")+"").equalsIgnoreCase("null"))
			{
				pst.setObject(1, null);
			}
			else
			{
				pst.setInt(1, Integer.parseInt(mapaSecArticulos.get("coddetarticulos").toString()));
			}
			pst.setInt(2, codigoFormato);
			pst.executeUpdate();

			HashMap mapaDetSecArt=(HashMap)((HashMap)mapaSecArticulos.get("detallenivel")).clone();
			for (int i = 0 ; i < Integer.parseInt(mapaDetSecArt.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosSecArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(mapaDetSecArt.get("codtiporompimiento_" + i)==null||(mapaDetSecArt.get("codtiporompimiento_" + i).toString()).equals("-1"))
				{
					ps.setObject(1, null);
				}
				else
				{
					ps.setInt(1, Integer.parseInt(mapaDetSecArt.get("codtiporompimiento_" + i).toString()));
				}
				if(mapaDetSecArt.get("impdetalleserv_" + i)!=null)
				{
					tmp=mapaDetSecArt.get("impdetalleserv_" + i).toString();
					if(tmp.equals("true"))
					{
						ps.setBoolean(2, true);
					}
					else
					{
						ps.setBoolean(2, false);
					}
				}
				else
				{
					ps.setBoolean(2, false);
				}
				if(mapaDetSecArt.get("impsubtotserv_" + i)!=null)
				{
					tmp=mapaDetSecArt.get("impsubtotserv_" + i).toString();
					if(tmp.equals("true"))
					{
						ps.setBoolean(3, true);
					}
					else
					{
						ps.setBoolean(3, false);
					}
				}
				else
				{
					ps.setBoolean(3, false);
				}
				ps.setInt(4, codigoFormato);
				ps.setInt(5, Integer.parseInt(mapaDetSecArt.get("codnivel_" + i).toString()));
				
				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error actualizando todo lo de la seccion de articulos [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar los datos de la seccion de totales de un formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public static int insertarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosBasicosSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			tmp=mapaSecTotales.get("impvalletras").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(2, true);
			}
			else
			{
				pst.setBoolean(2, false);
			}
			if(mapaSecTotales.get("codvalletras")==null||(mapaSecTotales.get("codvalletras").toString()).equals("-1"))
			{
				pst.setObject(3, null);
			}
			else
			{
				pst.setInt(3, Integer.parseInt(mapaSecTotales.get("codvalletras").toString()));
			}
			pst.executeUpdate();
			HashMap mapaDetSecTotales=(HashMap)((HashMap)mapaSecTotales.get("detallecampo")).clone();
			for (int i=0; i < Integer.parseInt(mapaDetSecTotales.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDatosSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoFormato);
				ps.setInt(2, Integer.parseInt(mapaDetSecTotales.get("codcampo_" + i).toString()));
				ps.setString(3, mapaDetSecTotales.get("valorcampo_" + i).toString());
				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando todo lo de la seccion de totales [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos de la seccion de totales de un formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosBasicosSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tmp=mapaSecTotales.get("impvalletras").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(1, true);
			}
			else
			{
				pst.setBoolean(1, false);
			}
			if(mapaSecTotales.get("codvalletras")==null||(mapaSecTotales.get("codvalletras").toString()).equals("")||(mapaSecTotales.get("codvalletras").toString()).equalsIgnoreCase("null")||(mapaSecTotales.get("codvalletras").toString()).equals("-1"))
			{
				pst.setObject(2, null);
			}
			else
			{
				pst.setInt(2, Integer.parseInt(mapaSecTotales.get("codvalletras").toString()));
			}
			pst.setInt(3, codigoFormato);
			pst.executeUpdate();
			HashMap mapaDetSecTotales=(HashMap)((HashMap)mapaSecTotales.get("detallecampo")).clone();
			for (int i=0; i < Integer.parseInt(mapaDetSecTotales.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, mapaDetSecTotales.get("valorcampo_" + i).toString());
				ps.setInt(2, codigoFormato);
				ps.setInt(3, Integer.parseInt(mapaDetSecTotales.get("codcampo_" + i).toString()));
				ps.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de la seccion de totales [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar los datos basicos de la seccion de nota de pie de pagina
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public static int insertarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatosBasicosSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFormato);
			pst.setString(2, mapaSecNotaPie.get("obsactecono").toString());
			tmp=mapaSecNotaPie.get("impfirmas").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(3, true);
			}
			else
			{
				pst.setBoolean(3, false);
			}
			pst.executeUpdate();
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando todo lo de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar los datos basicos de la seccion de nota de pie de pagina
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie) throws SQLException
	{
		String tmp="";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosBasicosSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, mapaSecNotaPie.get("obsactecono").toString());
			tmp=mapaSecNotaPie.get("impfirmas").toString();
			if(tmp.equals("true"))
			{
				pst.setBoolean(2, true);
			}
			else
			{
				pst.setBoolean(2, false);
			}
			pst.setInt(3, codigoFormato);
			pst.executeUpdate();
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para insertar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public static int insertarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas) throws SQLException
	{
		try
		{
			for (int i=0; i < Integer.parseInt(mapaFirmas.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarFirmasSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoFormato);
				pst.setInt(2, Integer.parseInt(mapaFirmas.get("codfirma_" + i).toString()));
				pst.setString(3, mapaFirmas.get("nomfirma_" + i).toString());
				pst.executeUpdate();
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando las firmas de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}

	/**
	 * Método para actualizar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * 
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas) throws SQLException
	{
		ResultSetDecorator rs;
		int temporal=0;
		try
		{
			for (int i=0; i < Integer.parseInt(mapaFirmas.get("numRegistros").toString()); i++)
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existeFirmaSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoFormato);
				ps.setInt(2, Integer.parseInt(mapaFirmas.get("codfirma_" + i).toString()));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					temporal=rs.getInt("cantidad");
				}
				if(temporal>0)
				{
					PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarFirmasSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setString(1, mapaFirmas.get("nomfirma_" + i).toString());
					pst.setInt(2, codigoFormato);
					pst.setInt(3, Integer.parseInt(mapaFirmas.get("codfirma_" + i).toString()));
					pst.executeUpdate();
				}
				else
				{
					PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarFirmasSecNotaPieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, codigoFormato);
					pst.setInt(2, Integer.parseInt(mapaFirmas.get("codfirma_" + i).toString()));
					pst.setString(3, mapaFirmas.get("nomfirma_" + i).toString());
					pst.executeUpdate();
				}
				
			}
			return 1;
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando las firmas de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para consultar los campos específicos de una
	 * subseccion del encabezado con su configuración
	 * @param con
	 * @param codigoFormato
	 * @param codigoSeccion
	 * @param soloImprimir (para filtrar que solo se consulten los campos para imprimir)
	 * @return
	 */
	public static HashMap consultarSubSeccionEncabezado(Connection con,int codigoFormato,int codigoSeccion,boolean soloImprimir)
	{
		try
		{
			String consulta = consultarSubSeccionEncabezadoStr;
			
			//se verifica si se deben filtrar solo los que se van a imprimir
			if(soloImprimir)
				consulta += " AND d.imprimir = " + ValoresPorDefecto.getValorTrueParaConsultas();
			
			if(codigoSeccion==ConstantesBD.codigoSeccionFormatoFacturaInstitucion)
				consulta += " ORDER BY d.orden ";
			else
				consulta += " ORDER BY d."+ValoresPorDefecto.getNumeroFilaParaConsultas();
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFormato);
			pst.setInt(2,codigoSeccion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarSubSeccionEncabezado de SqlBaseFormatoImpresionFacturaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar las prioridades activas de las secciones del encabezado
	 * y las retorna ordenadas
	 * Nota * Se excluye la seccion de institucion y las secciones cuya prioridad no se encuentra definida
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public static HashMap consultarPrioridadesSeccionEncabezado(Connection con,int codigoFormato)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarPrioridadesSeccionEncabezadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFormato);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPrioridadesSeccionEncabezado de SqlBaseFormatoInpresionFacturaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los datos parametrizados de la seccion principal dado un codigo de formato
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public static HashMap consultarDatosSecPpalParametrizados(Connection con,int codigoFormato) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosSecPpalParametrizadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFormato);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosSecPpalParametrizados de SqlBaseFormatoInpresionFacturaDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public static HashMap consultaCuerpoImpresionFacturaServicios(Connection con, String cadenaConsultaStr)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCuerpoImpresionFacturaServicios de SqlBaseFormatoInpresionFacturaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura para la seccion de articulos
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public static HashMap consultaCuerpoImpresionFacturaArticulos(Connection con, String cadenaConsultaStr)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCuerpoImpresionFacturaArticulos de SqlBaseFormatoInpresionFacturaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los campos, las descripciones parametrizadas y el valor correspondiente de cada campo 
	 * para la seccion de totales de el formato de impresion de factura dado el consecutivo de factura y el codigo
	 * del formato
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoFormato
	 * @return
	 */
	public static HashMap consultaCamposValoresSecTotales(Connection con, int consecutivoFactura, int codigoFormato)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCamposValoresSecTotalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, consecutivoFactura);
			pst.setInt(2, codigoFormato);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCamposValoresSecTotales de SqlBaseFormatoInpresionFacturaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método para saber el total de una factira segun el codigo del campo
	 * Campo es el codigo de que total se quiere obtener
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoCampo
	 * @return
	 */
	public static String consultaValorTotalXFacturaXCampo(Connection con, int consecutivoFactura, int codigoCampo)
	{
		ResultSetDecorator rs = null;
		String total = "0";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaValorTotalXFacturaXCampoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, consecutivoFactura);
			pst.setInt(2, codigoCampo);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				total = rs.getString("total");
			}
			if(total!=null && !total.equals("null") && !total.equals(""))
			{
				return total;
			}
			else
			{
				total = "0";
				return total;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaValorTotalXFacturaXCampo de SqlBaseFormatoInpresionFacturaDao: "+e);
			return  "0";
		}
	}
	
	/**
	 * Método para obtener todos los servicios Qx dado el codigo de la factura
	 * Dentro de serviciosQx(hashMap) esta otro mapa con el detalle de los asocios correspondientes a esa
	 * solicitud
	 * @param con
	 * @param codigoFactura
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarQx(Connection con, int codigoFactura) throws SQLException
	{
		HashMap serviciosQx = new HashMap();
		serviciosQx.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst=null;
			String consultaServiciosQxStr = " SELECT dfs.factura as codigofactura, " +
										    " dfs.solicitud as numerosolicitud, " +
										    " dfs.servicio as codigoservicio, " +
										    " getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") as nombreservicio, " +
										    " dfs.valor_cargo as valorsolicitud " +
										    " FROM det_factura_solicitud dfs " +
										    " INNER JOIN solicitudes sol on(dfs.solicitud = sol.numero_solicitud) " +
										    " WHERE dfs.factura = ? " +
										    " AND dfs.servicio is not null " +
										    " AND sol.tipo =  "+ConstantesBD.codigoTipoSolicitudCirugia; 
			
			String consultaServiciosAsociosQxStr=" SELECT adf.servicio_asocio as codigoasocioservicio, " +
												 " getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombreasocio, " +
												 " adf.valor_cargo as valorasocio, " +
												 " '' as nombregrupoasocio, " +
												 " ta.acronimo_tipo_serv as acronimotiposerv, " +
												 " ts.nombre as tiposervicio " +
												 " FROM asocios_det_factura adf " +
												 " inner join tipos_asocio ta on(adf.tipo_asocio=ta.codigo) " +
												 " inner join tipos_servicio ts ON(ta.acronimo_tipo_serv=ts.acronimo) " +
												 " inner join det_factura_solicitud dfs ON(adf.codigo=dfs.codigo) " +
												 " inner join sol_cirugia_por_servicio scxps on(dfs.solicitud=scxps.numero_solicitud and dfs.servicio=scxps.servicio) "+
												 " where dfs.factura = ? " +
												 " and dfs.solicitud = ? " +
												 " and scxps.servicio = ? " +
												 " AND adf.valor_total>0";
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaServiciosQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFactura);
			serviciosQx = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, false);
			int numReg = Integer.parseInt(serviciosQx.get("numRegistros")+"");
			for (int i = 0 ; i < numReg ; i++)
			{
				PreparedStatementDecorator ps;
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaServiciosAsociosQxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Integer.parseInt(serviciosQx.get("codigofactura_"+i)+""));
				ps.setInt(2, Integer.parseInt(serviciosQx.get("numerosolicitud_"+i)+""));
				ps.setInt(3, Integer.parseInt(serviciosQx.get("codigoservicio_"+i)+""));
				
				HashMap mapaTemp = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
				serviciosQx.put("detalleasocio_" + i, (HashMap)mapaTemp.clone());
			}
			return serviciosQx;
		}
		catch (SQLException e)
		{
			logger.error("Error consultarQx [SqlBaseFormatoImpresionFacturaDao]: " + e);
			return serviciosQx;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String[] obtenerEncabezadoPieFactura(Connection con, String codigoFactura, double empresaInstitucion)
	{
		String condicion="";
		if(empresaInstitucion>0)
		{
			condicion=" and empresa_institucion="+empresaInstitucion;
		}
		
		String consulta="(" +
							"SELECT " +
								"coalesce(encabezado_factura,'') as encabezado, " +
								"coalesce(pie_factura,'') as pie " +
							"from " +
								"convenios " +
							"where " +
								"codigo= (select convenio from facturas where codigo="+codigoFactura+" "+condicion+") " +
						")" ;
		
		if(empresaInstitucion>0)
		{
			consulta+=	"UNION ALL " +
						"(" +
							"SELECT " +
								"coalesce(encabezado,'') as encabezado, " +
								"coalesce(pie,'') as pie " +
							"FROM " +
								"empresas_institucion " +
							"where " +
								"codigo= "+empresaInstitucion +
						") ";
		}
		else
		{	
			consulta+=	"UNION ALL " +
						"(" +
							"SELECT " +
								"coalesce(encabezado,'') as encabezado, " +
								"coalesce(pie,'') as pie " +
							"FROM " +
								"instituciones " +
							"where " +
								"codigo=(select institucion from facturas where codigo="+codigoFactura+")" +
						") ";
		}
		
		logger.info("\n\nCONSULTA ENCABEZADO PIE FACTURA-->"+consulta+"\n\n");
		String[] retornar= {"",""};
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				retornar[0]= rs.getString("encabezado");
				retornar[1]= rs.getString("pie");
				
				if(!UtilidadTexto.isEmpty(retornar[0]) || !UtilidadTexto.isEmpty(retornar[1]))
				{
					return retornar;
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando pie y encabezado de impresión de factura [SqlBaseFormatoImpresionFacturaDao]: " + e);
		}
		return retornar;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerFechaAperturaCuentaFactura(Connection con,	String codigoFactura) 
	{
		String consulta="SELECT to_char(i.fecha_ingreso, 'DD/MM/YYYY')||' '||substr(i.hora_ingreso,1,5) FROM cuentas c INNER JOIN ingresos i ON(i.id=c.id_ingreso) WHERE c.id=(SELECT cuenta FROM facturas WHERE codigo=?)";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoFactura);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerFechaFactura(Connection con, String codigoFactura) 
	{
		String consulta="SELECT to_char(fecha, 'DD/MM/YYYY')||' '||substr(hora,1,5) FROM facturas  WHERE codigo=?";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigoFactura);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando todo lo de la seccion de nota de pie de pagina [SqlBaseFormatoImpresionFacturaDao]: " + e);
		}
		return "";
	}
	
}