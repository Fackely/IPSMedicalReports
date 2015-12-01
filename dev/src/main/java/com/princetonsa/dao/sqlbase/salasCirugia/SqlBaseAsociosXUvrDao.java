package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseAsociosXUvrDao {
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAsociosXUvrDao.class);
	
	/**
	 * Cadena para insertar el encabezado de un registro de asocio por uvr que va por encabezado o por convenio
	 */
	private static String insertarAsociosXUvrStr="INSERT INTO asocios_x_uvr" +
														"(codigo," +			//1
														"esq_tar_general," +	//2
														"esq_tar_particular," +	//3
														"convenio," +			//4
														"fecha_inicial," +		//5
														"fecha_final," +		//6
														"institucion," +		//7
														"fecha_modifica," +		
														"hora_modifica," +		
														"usuario_modifica)" +	//8
													" VALUES (?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Cadena para insertar el segundo encabezado de un asocio por uvr que va por tipo de asocio
	 */
	private static String insertarAsocioXUvrTipoSalaStr="INSERT INTO asocios_x_uvr_tipo_sala" +
																"(codigo," +			//1
																"tipo_asocio," +		//2
																"tipo_sala," +			//3
																"cod_asocio_x_uvr," +	//4
																"fecha_modifica," +		
																"hora_modifica," +		
																"usuario_modifica," +
																"liquidar_por )" +	//5
															" VALUES (?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena para insertar el detalle de cada registro de asocio por uvr
	 */
	private static String insertarDetalleAsociosXUvrStr="INSERT INTO detalle_asocios_x_uvr" +
																"(codigo," +					//1
																"cod_asocio_x_uvr_tipo_sala," +	//2
																"tipo_servicio," +				//3
																"tipo_anestesia," +				//4
																"ocupacion," +					//5
																"especialidad," +				//6
																"tipo_especialista," +			//7
																"rango1," +						//8
																"rango2," +						//9
																"tipo_liquidacion," +			//10
																"valor," +						//11
																"fecha_modifica," +				//12			
																"hora_modifica," +				
																"usuario_modifica," +
																// campo de unidades, adicionado el miercoles 27 de febrero del 2008
																"unidades," +
																// campo de unidades, adicionado el miercoles 27 de Agosto del 2008
																// por tarea 36145
																" valor_unidades)" +
															" VALUES (?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * Cadena para insertar el detalle de los codigos tarifarios por cada detalle de asocio por uvr
	 */
	private static String insertarDetalleCodigosTarifariosStr="INSERT INTO det_cod_asocios_x_uvr" +
																	"(cod_detalle_asocio_x_uvr," +
																	"codigo_tarifario," +
																	"codigo)" +
																" VALUES (?,?,?)";
	
	
	/**
	 * Cadena que consulta el maestro intermedio de asocios uvr que es por tipo sala cuando es por esquema tarifario
	 */
	private static String consultarAsociosXUvrTipoSalaStr="SELECT " +
																"ts.codigo as codigoasociouvrtiposala," +
																"ts.tipo_asocio as codigotipoasocio," +
																"coalesce(ts.tipo_sala,"+ConstantesBD.codigoNuncaValido+") as codigotiposala," +
																"ts.cod_asocio_x_uvr as codigoasociouvr," +
																"getnomtipoasocio(tipo_asocio) as nombretipoasocio," +
																"getnombretiposala(tipo_sala) as nombretiposala," +
																" getnombreasocio(tipo_asocio) As nombreasocio, "+
																// para obtener la cantidad del esquema tarifario seleccionado
																"et.codigo as codigoesquematarifario, " +
																"coalesce(et.cantidad,0) as cantidadesquematarifario, " +
																" ts.liquidar_por As liquidarpor, " +
																
																"'"+ConstantesBD.acronimoSi+"' as estabd " +
															"FROM asocios_x_uvr_tipo_sala ts " +
															"INNER JOIN asocios_x_uvr axuvr on(ts.cod_asocio_x_uvr=axuvr.codigo) " +
															
															// OBTENER LA CANTIDAD DEL ESQUEMA_TARIFARIO_SELECCIONADO
															" LEFT OUTER JOIN esquemas_tarifarios et ON (et.codigo = axuvr.esq_tar_particular) ";
	
	/**
	 * Cadena para la consulta de los detalles por el maestro intermedio que es asocios uvr por tipo sala
	 */
	private static String consultarDetalleAsociosXUvrStr="SELECT " +
																"daxuvr.codigo AS codigodetalle," +
																"daxuvr.cod_asocio_x_uvr_tipo_sala as codigoasocio," +
																"daxuvr.tipo_servicio as codigotiposervicio," +
																"daxuvr.tipo_anestesia as codigotipoanestesia," +
																"daxuvr.ocupacion as codigoocupacion," +
																"daxuvr.especialidad as codigoespecialidad," +
																"daxuvr.tipo_especialista as codigotipoespecialista," +
																"daxuvr.rango1 as rango1," +
																"daxuvr.rango2 as rango2," +
																"case when (daxuvr.tipo_liquidacion="+ConstantesBD.codigoTipoLiquidacionSoatValor+" AND daxuvr.unidades is not null ) then "+ConstantesBD.codigoTipoLiquidacionValorUnidades+" else daxuvr.tipo_liquidacion end as codigotipoliquidacion," +
																"coalesce(daxuvr.valor,0) as valor," +
																" ts.liquidar_por As liquidarpor, " +
																
																
																// unidades campo adicionado el miercoles 27 de febrero del 2008															
																"daxuvr.unidades as unidades, " +
																// unidades campo adicionado el miercoles 27 de Agosto del 2008
																//por tarea 36141
																"daxuvr.valor_unidades as valorunidades, " +	
																
																
																"getnombretiposervicio(daxuvr.tipo_servicio) as nombretiposervicio," +
																"getnombretipoanestesia(daxuvr.tipo_anestesia) as nombretipoanestesia," +
																"getnombreocupacion(daxuvr.ocupacion) as nombreocupacion," +
																"getnombreespecialidad(daxuvr.especialidad) as nombreespecialidad," +
																"getnomtipoliquidacion(daxuvr.tipo_liquidacion) as nombretipoliquidacion," +
																"'"+ConstantesBD.acronimoSi+"' as estabd " +
															"FROM detalle_asocios_x_uvr daxuvr " +
															"INNER JOIN asocios_x_uvr_tipo_sala ts on (daxuvr.cod_asocio_x_uvr_tipo_sala=ts.codigo) " +
															"INNER JOIN asocios_x_uvr axuvr on (ts.cod_asocio_x_uvr=axuvr.codigo) " +
															"WHERE daxuvr.cod_asocio_x_uvr_tipo_sala=? ";
	
	/**
	 * Cadena para la consulta de las vigencias existentes por convenio y por tipo asocio
	 */
	private static String consultarVigenciasXCovenioStr="SELECT " +
																"codigo as codigoasociouvr," +
																"to_char(fecha_inicial,'DD/MM/YYYY') as fechainicial," +
																"to_char(fecha_final,'DD/MM/YYYY') as fechafinal," +
																"'"+ConstantesBD.acronimoSi+"' as estabd " +
														"FROM asocios_x_uvr  " +
														"WHERE convenio=?";
	
	/**
	 * 
	 */
	private static String cadenaConsultaDetCodigosGrupos="SELECT tarof.codigo as codigotarifario,tarof.nombre as nomtarifario,coalesce(dcaxuvr.codigo,'') as valor,case when dcaxuvr.codigo is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as estabd from tarifarios_oficiales tarof left outer join det_cod_asocios_x_uvr dcaxuvr on(dcaxuvr.codigo_tarifario=tarof.codigo and dcaxuvr.cod_detalle_asocio_x_uvr=? )";
	
	/**
	 * Cadena para la eliminacion de los codigos tarifarios oficialis del detalle de asocios por uvr 
	 */
	private static String eliminarDetCodigosAsociosXUvr="DELETE FROM det_cod_asocios_x_uvr WHERE cod_detalle_asocio_x_uvr=? AND codigo_tarifario=?";
	
	private static String eliminarDetCodigosAsociosXUvrTotal=" DELETE FROM det_cod_asocios_x_uvr WHERE cod_detalle_asocio_x_uvr=?";
	
	/**
	 * Cadena para la actualizacion de los codigos tarifarios por registro de detalle de asocio por uvr 
	 */
	private static String actualizarDetCodigosAsociosUvr="UPDATE det_cod_asocios_x_uvr SET codigo=? WHERE cod_detalle_asocio_x_uvr=? AND codigo_tarifario=?";
	
	private static String modificarDetalleStr="UPDATE detalle_asocios_x_uvr " +
												"SET " +
													"tipo_servicio=?," +		//1
													"tipo_anestesia=?," +		//2
													"ocupacion=?," +			//3
													"especialidad=?," +			//4
													"tipo_especialista=?," +	//5
													"rango1=?," +				//6
													"rango2=?," +				//7
													"tipo_liquidacion=?," +		//8
													"valor=?," +				//9
													"fecha_modifica=CURRENT_DATE," +
													"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
													"usuario_modifica=?, " + //10
													"unidades=?, " +		//11
													"valor_unidades=? " +		//12
												"WHERE codigo=?";		//13
	
	/**
	 * Cadena para la eliminacion del detalle de asocio por uvr
	 */
	private static String eliminarDetalleStr="DELETE FROM detalle_asocios_x_uvr WHERE codigo=?";
	
	/**
	 * Cadena para la eliminacion del segundo maestro
	 */
	private static String eliminarAsocioUvrXTipoAsocioStr="DELETE FROM asocios_x_uvr_tipo_sala WHERE codigo=?";
	
	/**
	 * Cadena para eliminar un asocio por uvr por convenio
	 */
	private static String eliminarAsocioUvrXConvenioStr="DELETE FROM asocios_x_uvr WHERE codigo=?";
	
	private static String modificarVigenciasStr="UPDATE asocios_x_uvr SET " +
														"fecha_inicial=?," +
														"fecha_final=? " +
													"WHERE codigo=?";
	
	
	private static String modificarTipoSala =" UPDATE asocios_x_uvr_tipo_sala SET tipo_asocio=?, tipo_sala=?,liquidar_por=? WHERE codigo=?";
	
	
	/**
	 * Metodo encargado de mopdificar asocios por sala
	 * @param connection
	 * @param tipoAsocio
	 * @param tipoSala
	 * @param codigo
	 * @return
	 */
	public static boolean modificarAsociosUvrSala (Connection connection,String tipoAsocio, String tipoSala, String codigo,String liquidarPor)
	{
		logger.info("\n entro a modificarAsociosUvrSala tipo Asocio--> "+tipoAsocio+" tipo sala -->"+tipoSala+" codigo -->"+codigo);
		
		String cadena=modificarTipoSala;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 *  UPDATE asocios_x_uvr_tipo_sala SET tipo_asocio=?, tipo_sala=?,liquidar_por=? WHERE codigo=?
			 */
			
			//tipo de asocio
			ps.setInt(1, Utilidades.convertirAEntero(tipoAsocio));
			//tipo de sala
			if (!tipoSala.equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(2, Utilidades.convertirAEntero(tipoSala));
			else
				ps.setNull(2, Types.INTEGER);
			
			//liquidar_por
			ps.setString(3, liquidarPor);
			// codigo
			ps.setInt(4, Utilidades.convertirAEntero(codigo));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("\n problema actualizando los datos en la tabla asocios_x_uvr_tipo_sala "+e);
		}
		
		return false;
	}
	
	
	
	
	/**
	 * Metodo que inserta en las tablas asocios_x_uvr y asocios_x_uvr_tipo_sala que son las dos maestras
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarAsociosUvrMaestro(Connection con,HashMap vo,boolean guardarXTipoAsocio)
	{
		logger.info("\n entre a insertarAsociosUvrMaestro "+vo);
		
		int codigoAsocioUvr=ConstantesBD.codigoNuncaValido;
		int esquema=ConstantesBD.codigoNuncaValido;
		boolean respuesta=false;
		if(vo.containsKey("esquemageneral"))
			esquema=Utilidades.convertirAEntero(vo.get("esquemageneral")+"");
		else if(vo.containsKey("esquemaparticular"))
			esquema=Utilidades.convertirAEntero(vo.get("esquemaparticular")+"");
		if (esquema>0)
			codigoAsocioUvr=consultarCodigoXEsquemaOXConvenio(con, esquema, Utilidades.convertirAEntero(vo.get("convenio")+""));
		if(codigoAsocioUvr<0)
		{
			try
			{
				codigoAsocioUvr=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_aso_uvr");
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarAsociosXUvrStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO asocios_x_uvr" +
														"(codigo," +			//1
														"esq_tar_general," +	//2
														"esq_tar_particular," +	//3
														"convenio," +			//4
														"fecha_inicial," +		//5
														"fecha_final," +		//6
														"institucion," +		//7
														"fecha_modifica," +		
														"hora_modifica," +		
														"usuario_modifica)" +	//8
													" VALUES (?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
				 */
				
				ps.setInt(1, codigoAsocioUvr);
				if(Utilidades.convertirAEntero(vo.get("esquemageneral")+"")>0)
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("esquemageneral")+""));
				else
					ps.setNull(2, Types.INTEGER);
				if(Utilidades.convertirAEntero(vo.get("esquemaparticular")+"")>0)
					ps.setInt(3, Utilidades.convertirAEntero(vo.get("esquemaparticular")+""));
				else
					ps.setNull(3, Types.INTEGER);
				if(Utilidades.convertirAEntero((vo.get("convenio")+""))>0)
					ps.setInt(4, Utilidades.convertirAEntero(vo.get("convenio")+""));
				else
					ps.setNull(4, Types.INTEGER);
				if(UtilidadCadena.noEsVacio(vo.get("fechainicial")+""))
					ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainicial")+"")));
				else
					ps.setNull(5, Types.DATE);;
				if(UtilidadCadena.noEsVacio(vo.get("fechafinal")+""))
					ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinal")+"")));
				else
					ps.setNull(6, Types.DATE);
				ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
				ps.setString(8, vo.get("usuariomodifica")+"");
				if(ps.executeUpdate()>0)
				{
					respuesta=true;
					logger.info("INSERTANDO ASOCIOS X UVR");
				}
				else
				{
					respuesta=false;
					logger.info("ERROR INSERTANDO ASOCIOS POR UVR");
				}
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
				logger.info("ERROR INSERTANDO ASOCIOS POR UVR MAESTRO");
			}
		}
		else
			respuesta=true;
		if(guardarXTipoAsocio)
			insertarAsociosXUvrXTipoAsocio(con, vo, codigoAsocioUvr);
		return respuesta;
	}
	
	/**
	 * Metodo que inserta un registro de tipo maestro despues de escoger el tipo de asocio
	 * @param con
	 * @param vo
	 * @param codigoAsocioUvr
	 * @return
	 */
	public static boolean insertarAsociosXUvrXTipoAsocio(Connection con,HashMap vo,int codigoAsocioUvr)
	{
		boolean respuesta=false;
		int codigoAsocioUvrTipoSala=ConstantesBD.codigoNuncaValido;
		try
		{
			codigoAsocioUvrTipoSala=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_asocios_x_uvr_tipo_sala");
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(insertarAsocioXUvrTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO asocios_x_uvr_tipo_sala" +
																"(codigo," +			//1
																"tipo_asocio," +		//2
																"tipo_sala," +			//3
																"cod_asocio_x_uvr," +	//4
																"fecha_modifica," +		
																"hora_modifica," +		
																"usuario_modifica," +
																"liquidar_por )" +	//5
															" VALUES (?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)
			 */
			
			
			ps2.setInt(1, codigoAsocioUvrTipoSala);
			ps2.setInt(2, Utilidades.convertirAEntero(vo.get("codigotipoasocio")+""));
			if(Utilidades.convertirAEntero((vo.get("codigotiposala")+""))>0)
				ps2.setInt(3, Utilidades.convertirAEntero(vo.get("codigotiposala")+""));
			else
				ps2.setNull(3, Types.INTEGER);
			ps2.setInt(4, codigoAsocioUvr);
			ps2.setString(5, vo.get("usuariomodifica")+"");
			//liquidarpor
			ps2.setString(6, vo.get("liquidarpor")+"");
			
			if(ps2.executeUpdate()>0)
			{
				respuesta=true;
				logger.info("INSERTANDO ASOCIOS X UVR TIPO SALA");
			}
			else
			{
				respuesta=false;
				logger.info("ERROR INSERTANDO ASOCIOS POR UVR TIPO SALA");
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("ERROR INSERTANDO ASOCIOS POR UVR MAESTRO");
		}
		return respuesta;
	}
	
	/**
	 * Metodo que inserta el detalle de los registros asocios por uvr el cual sirve si se inserta por convenio o esquema tarifario
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarDetalleAsocioXUvr(Connection con, HashMap vo)
	{
		logger.info("\n entro a  insertarDetalleAsocioXUvr "+vo);
		try
		{
			int codigoDetalleAsocioUvr=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_detalle_asocios_x_uvr");
			logger.info("valor secuencia: "+codigoDetalleAsocioUvr);
			CallableStatement ps=con.prepareCall(insertarDetalleAsociosXUvrStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			/**
			 * INSERT INTO detalle_asocios_x_uvr" +
																"(codigo," +					//1
																"cod_asocio_x_uvr_tipo_sala," +	//2
																"tipo_servicio," +				//3
																"tipo_anestesia," +				//4
																"ocupacion," +					//5
																"especialidad," +				//6
																"tipo_especialista," +			//7
																"rango1," +						//8
																"rango2," +						//9
																"tipo_liquidacion," +			//10
																"valor," +						//11
																"fecha_modifica," +				//12			
																"hora_modifica," +				
																"usuario_modifica," +
																// campo de unidades, adicionado el miercoles 27 de febrero del 2008
																"unidades," +
																// campo de unidades, adicionado el miercoles 27 de Agosto del 2008
																// por tarea 36145
																" valor_unidades)" +
															" VALUES (?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)
			 */
			
			
			ps.setInt(1, codigoDetalleAsocioUvr);
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigoasociouvrtiposala")+""));
			ps.setString(3, vo.get("codigotiposervicio")+"");
			if(Utilidades.convertirAEntero(vo.get("codigotipoanestesia")+"")>0)
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("codigotipoanestesia")+""));
			else
				ps.setNull(4, Types.INTEGER);
			if(!UtilidadTexto.isEmpty(vo.get("codigoocupacion")+"")||Utilidades.convertirAEntero(vo.get("codigoocupacion")+"")>0)
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("codigoocupacion")+""));
			else
				ps.setNull(5, Types.INTEGER);
			if(!UtilidadTexto.isEmpty(vo.get("codigoespecialidad")+"")||Utilidades.convertirAEntero(vo.get("codigoespecialidad")+"")>0)
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigoespecialidad")+""));
			else
				ps.setNull(6, Types.INTEGER);
			if(!UtilidadTexto.isEmpty(vo.get("codigotipoespecialista")+""))
				ps.setString(7, vo.get("codigotipoespecialista")+"");
			else
				ps.setNull(7, Types.VARCHAR);
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("rango1")+""));
			ps.setInt(9, Utilidades.convertirAEntero(vo.get("rango2")+""));
			ps.setInt(10, Utilidades.convertirAEntero(vo.get("codigotipoliquidacion")+""));
			
			if ( Utilidades.convertirAEntero(vo.get("valor")+"")>0)
				ps.setDouble(11, Utilidades.convertirADouble(vo.get("valor")+""));
			else
				ps.setNull(11, Types.NUMERIC);
		
			ps.setString(12, vo.get("usuariomodifica")+"");
			
			// campo unidades, adicionado el miercoles 27 de febrero del 2008
			if(Utilidades.convertirAEntero(vo.get("unidades")+"")>0)
				ps.setInt(13, Utilidades.convertirAEntero(vo.get("unidades")+""));
			else
				ps.setNull(13, Types.INTEGER);
			

			// campo unidades, adicionado el miercoles 27 de Agosto del 2008
			//por tarea 36145
			if(Utilidades.convertirAEntero(vo.get("valorunidades")+"")>0)
				ps.setDouble(14, Utilidades.convertirADouble(vo.get("valorunidades")+""));
			else
				ps.setNull(14, Types.NUMERIC);
			
			
			if(ps.executeUpdate()>0)
			{
				logger.info("INSERTANDO DETALLE ASOCIOS X UVR");
				return codigoDetalleAsocioUvr;
			}
			else
			{
				logger.info("ERROR INSERTANDO DETALLE ASOCIOS POR UVR");
				return ConstantesBD.codigoNuncaValido;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que consulta el maestro de los asocios por uvr de tipo sala de donde se desprenderan los detalles sea por esquema tarifario o por convenio
	 * @param con
	 * @param esquemaTarifario
	 * @param convenio
	 * @return
	 */
	public static HashMap consultarAsociosXUvrTipoSala(Connection con,int esquemaTarifario,int convenio, int codigoAsocioXUvr)
	{
		logger.info("\n entre a consultarAsociosXUvrTipoSala codigo --> "+codigoAsocioXUvr);
		HashMap mapa=new HashMap();
		String cadenaConsulta=consultarAsociosXUvrTipoSalaStr , where="";
		if(esquemaTarifario>0)
			where+=" WHERE axuvr.esq_tar_general="+esquemaTarifario+" OR axuvr.esq_tar_particular="+esquemaTarifario;
		//////////////////////////////////////////////////////////
		//modificado por tarea 35172 
		if(codigoAsocioXUvr>0)
			where+=" WHERE axuvr.codigo="+codigoAsocioXUvr;
		//////////////////////////////////////////////////////////
		cadenaConsulta+=where+" ORDER BY nombreasocio asc" ;
		
		logger.info("\n cadena -->"+cadenaConsulta);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo que carga en un mapa la informacion del detalle de los asocios por uvr validando si viene por convenio o por esquema tarifario y por el codigo del asocio uvr para validar las vigencias cuando viene por convenio
	 * @param con
	 * @param codigoAsocioUvr
	 * @param tipoAsocio
	 * @param esquemaTarifario
	 * @param convenio
	 * @return
	 */
	public static HashMap consultarDetalleAsociosXUvr(Connection con,int codigoAsocioUvr,int tipoAsocio,int esquemaTarifario,int convenio,String tipoServicio,int tipoAnestesia,String ocupacion,String especialidad,String tipoEspecialista,int tipoLiquidacion)
	{
		logger.info("entre a  consultarDetalleAsociosXUvr codigoAsocioUvr -->"+codigoAsocioUvr+" tipo Asocio -->"+tipoAsocio+" esquematarifario -->"+esquemaTarifario+" convenio -->"+convenio+" tipoServicio --> "+tipoServicio+ " tipo anestesia -->"+tipoAnestesia+" " +
				"	ocupacion --> "+ocupacion+" especialidad --> "+especialidad+" tipoEspecialista --> "+tipoEspecialista+ " tipoLiquidacion -->"+tipoLiquidacion);
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadenaConsulta=consultarDetalleAsociosXUvrStr;
		if(codigoAsocioUvr>0)
			cadenaConsulta+=" AND axuvr.codigo="+codigoAsocioUvr;
		/*if(esquemaTarifario>0)
			cadenaConsulta+=" AND axuvr.esq_tar_general="+esquemaTarifario+" OR axuvr.esq_tar_particular="+esquemaTarifario;*/
		if(convenio>0)
			cadenaConsulta+=" AND axuvr.convenio="+convenio;
		if(!UtilidadTexto.isEmpty(tipoServicio))
			cadenaConsulta+=" AND daxuvr.tipo_servicio='"+tipoServicio+"'";
		if(tipoAnestesia>0)
			cadenaConsulta+=" AND daxuvr.tipo_anestesia="+tipoAnestesia;
		if(!UtilidadTexto.isEmpty(ocupacion))
			cadenaConsulta+=" AND daxuvr.ocupacion="+ocupacion;
		if(!UtilidadTexto.isEmpty(especialidad))
			cadenaConsulta+=" AND daxuvr.especialidad="+especialidad;
		if(!UtilidadTexto.isEmpty(tipoEspecialista))
			cadenaConsulta+=" AND daxuvr.tipo_especialista='"+tipoEspecialista+"'";
		if(tipoLiquidacion>0)
			cadenaConsulta+=" AND daxuvr.tipo_liquidacion="+tipoLiquidacion;
		cadenaConsulta+=" ORDER BY daxuvr.tipo_servicio,daxuvr.especialidad,daxuvr.rango1";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1, tipoAsocio);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				PreparedStatementDecorator psTar= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetCodigosGrupos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTar.setInt(1, Utilidades.convertirAEntero(mapa.get("codigodetalle_"+i)+""));
				ResultSetDecorator rs=new ResultSetDecorator(psTar.executeQuery());
				while (rs.next())
				{
					String codTarifario=rs.getObject("codigotarifario")+"";
					mapa.put("valortarifario_"+i+"_"+codTarifario, rs.getObject("valor"));
					mapa.put("estabd_"+i+"_"+codTarifario, rs.getObject("estabd"));
				}
			}
			logger.info("\n\nCONSULTA AVANZADA-->>"+cadenaConsulta);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo que segun la accion sobre los codigos de tarifarios oficiales los inserta actualiza o elimina
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean actualizarDetCodigosAsociosXUvr(Connection con, HashMap<String, Object> vo) 
	{
		logger.info("\n entre a  actualizarDetCodigosAsociosXUvr "+vo);
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(vo.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.isEmpty(vo.get("valortarifario_"+i)+"")&&UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetCodigosAsociosXUvr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * DELETE FROM det_cod_asocios_x_uvr WHERE cod_detalle_asocio_x_uvr=? AND codigo_tarifario=?
					 */
					
					ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigodetalleasociouvr")+""));
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigotarifario_"+i)+""));
					ps.executeUpdate();
				}
				else if(!UtilidadTexto.isEmpty(vo.get("valortarifario_"+i)+"")&&UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDetCodigosAsociosUvr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * UPDATE det_cod_asocios_x_uvr SET codigo=? WHERE cod_detalle_asocio_x_uvr=? AND codigo_tarifario=?
					 */
					
					ps.setString(1,  vo.get("valortarifario_"+i)+"");
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigodetalleasociouvr")+""));
					ps.setInt(3, Utilidades.convertirAEntero(vo.get("codigotarifario_"+i)+""));
					ps.executeUpdate();
				}
				if(!UtilidadTexto.isEmpty(vo.get("valortarifario_"+i)+"")&&!UtilidadTexto.getBoolean(vo.get("estabd_"+i)+""))
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetalleCodigosTarifariosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * INSERT INTO det_cod_asocios_x_uvr" +
																	"(cod_detalle_asocio_x_uvr," +
																	"codigo_tarifario," +
																	"codigo)" +
																" VALUES (?,?,?)
					 */
					
					ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigodetalleasociouvr")+""));
					ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigotarifario_"+i)+""));
					ps.setString(3,  vo.get("valortarifario_"+i)+"");
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
	
	/**
	 * Metodo que carga un mapa con las vigencias existentes segun el convenio y el tipo de asocio
	 * @param con
	 * @param convenio
	 * @param tipoAsocio
	 * @return
	 */
	public static HashMap consultarVigenciasXConvenio(Connection con,int convenio)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarVigenciasXCovenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, convenio);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Metodo que consulta si ya existe un codigo maestro sea por esquema tarifario o por convenio
	 * @param con
	 * @param esquema
	 * @param convenio
	 * @return
	 */
	public static int consultarCodigoXEsquemaOXConvenio(Connection con,int esquema,int convenio)
	{
		int codigo=ConstantesBD.codigoNuncaValido;
		String cadena="SELECT codigo from asocios_x_uvr ";
		if(esquema>0)
			cadena+=" WHERE esq_tar_general="+esquema+" OR esq_tar_particular="+esquema;
		if(convenio>0)
			cadena+=" WHERE convenio="+convenio;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codigo=rs.getInt("codigo");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			codigo=ConstantesBD.codigoNuncaValido;
		}
		return codigo;
	}
	
	/**
	 * Metodo para la actualizacion de los detalles de asocios por uvr
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarDetalle(Connection con,HashMap vo)
	{
		logger.info("\n entrea modificarDetalle --> "+vo);
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE detalle_asocios_x_uvr " +
												"SET " +
													"tipo_servicio=?," +		//1
													"tipo_anestesia=?," +		//2
													"ocupacion=?," +			//3
													"especialidad=?," +			//4
													"tipo_especialista=?," +	//5
													"rango1=?," +				//6
													"rango2=?," +				//7
													"tipo_liquidacion=?," +		//8
													"valor=?," +				//9
													"fecha_modifica=CURRENT_DATE," +
													"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
													"usuario_modifica=?, " + //10
													"unidades=?, " +		//11
													"valor_unidades=? " +		//12
												"WHERE codigo=?
			 */
			
			ps.setString(1, vo.get("codigotiposervicio")+"");
			
			if(Utilidades.convertirAEntero(vo.get("codigotipoanestesia")+"")>0)
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigotipoanestesia")+""));
			else
				ps.setNull(2, Types.INTEGER);
			
			if(!UtilidadTexto.isEmpty(vo.get("codigoocupacion")+""))
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("codigoocupacion")+""));
			else
				ps.setNull(3, Types.INTEGER);
			if(!UtilidadTexto.isEmpty(vo.get("codigoespecialidad")+""))
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("codigoespecialidad")+""));
			else
				ps.setNull(4, Types.INTEGER);
			if(!UtilidadTexto.isEmpty(vo.get("codigotipoespecialista")+""))
				ps.setString(5, vo.get("codigotipoespecialista")+"");
			else
				ps.setNull(5,Types.VARCHAR);
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("rango1")+""));
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("rango2")+""));
			
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("codigotipoliquidacion")+""));
			
			if ( Utilidades.convertirADouble(vo.get("valor")+"")>0)
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("valor")+""));
			else
				ps.setNull(9, Types.NUMERIC);
		
			
			ps.setString(10, vo.get("usuariomodifica")+"");
			
			
			if(Utilidades.convertirAEntero(vo.get("unidades")+"")>0)
				ps.setInt(11, Utilidades.convertirAEntero(vo.get("unidades")+""));
			else
				ps.setNull(11, Types.INTEGER);
			
			
			if(Utilidades.convertirAEntero(vo.get("valorunidades")+"")>0)
				ps.setDouble(12, Utilidades.convertirADouble(vo.get("valorunidades")+""));
			else
				ps.setNull(12, Types.NUMERIC);
			
			
			ps.setInt(13, Utilidades.convertirAEntero(vo.get("codigodetalle")+""));
			if(ps.executeUpdate()>0)
				respuesta=true;
		}
		catch (SQLException e) 
		{
			
			logger.info("ERROR ACTUALIZANDO EL DETALLE DE ASOCIOS POR UVR "+e);
		}
		return respuesta;
	}
	
	/**
	 * Metodo para la eliminacion del detalle de asocios por uvr
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarDetalle(Connection con,int codigo)
	{
		logger.info("\n entre a eliminarDetalle codigo -->"+codigo);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetCodigosAsociosXUvrTotal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo que elimina un registro del segundo maestro que es asocio por uvr por tipo de asocio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarAsocioXUvrTipoAsocio(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAsocioUvrXTipoAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean eliminarAsocioUvrXConvenio(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAsocioUvrXConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo que modifica las vigencias por convenio
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarVigencias(Connection con,HashMap vo)
	{
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarVigenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE asocios_x_uvr SET " +
														"fecha_inicial=?," +
														"fecha_final=? " +
													"WHERE codigo=?
			 */
			
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainicial")+"")));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinal")+"")));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("codigoasociouvr")+""));
			if(ps.executeUpdate()>0)
				respuesta=true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return respuesta;
	}

}
