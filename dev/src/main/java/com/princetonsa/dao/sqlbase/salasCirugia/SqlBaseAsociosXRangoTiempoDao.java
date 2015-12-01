package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.salasCirugia.DtoAsociosXRangoTiempo;
import com.princetonsa.dto.salasCirugia.DtoDetalleAsociosXRangoTiempo;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;



/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */
public class SqlBaseAsociosXRangoTiempoDao
{
	/*---------------------------------------------
	 * 		 ATRIBUTO PARA EL MANEJO DE LOGS
	 ---------------------------------------------*/
	private static Logger logger = Logger.getLogger(SqlBaseAsociosXRangoTiempoDao.class);
	
	/*---------------------------------------------
	 *    FIN ATRIBUTO PARA EL MANEJO DE LOGS
	 ---------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 * 					  ATRIBUTOS ASOCIOXRANGOTIEMPO
	 -------------------------------------------------------------------*/
	
	
				/*------------------------------------------
				 * 		CADENAS DE INSERCION
				 ------------------------------------------*/
	
	private static String strCadenaInsercion0="INSERT INTO cabeza_asoc_x_ran_tiem  ( convenio,esquema_tarifario,fecha_inicial,fecha_final," +
				"institucion,usuario_modifica,fecha_modifica,hora_modifica,esquema_tarifario_general,consecutivo) VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 *cadena de insercion de asocios por tiempo 
	 *Tabla (asocios_x_rango_tiempo).
	 */
	private static String strCadenaInsercion1="INSERT INTO asocios_x_rango_tiempo (codigo_encab,tipo_servicio,tipo_cirugia," +
			"tipo_anestesia,asocio,tipo_tiempo_base,minutos_rango_inicial,minutos_rango_final," +
			"min_frac_adicional,valor_frac_adicional,usuario_modifica,fecha_modifica,hora_modifica," +
			"servicio,valor_asocio,codigo,liquidarpor) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que verifica si ya existe encabezado del registro del asocio
	 */
	private static String strCadenaVerificacionInsercion1 = "SELECT consecutivo as consecutivo from cabeza_asoc_x_ran_tiem ";
	/**
	 * cadena de insercion del detalle asocios por tiempo;
	 * aqui se asocian los diferentes codigos de tarifarios
	 * oficiales.
	 * Tabla (det_asoc_x_ran_tiem)
	 */
	private static String strCadenaInsercion2="INSERT INTO det_asoc_x_ran_tiem (codigo_asocxrangtiem,codigo_tarfio_oficial,codigo) VALUES (?,?,?)";
	
		
				/*------------------------------------------
				 * 		CADENAS DE MODIFICACION
				 ------------------------------------------*/
	
	/**
	 * Cadena de modificacion del encabezado de asocios por
	 * rango de tiempo, modificacion en la Tabla (cabeza_asoc_x_ran_tiem).
	 */
	private static String strCadenaModificacion0="UPDATE cabeza_asoc_x_ran_tiem SET fecha_inicial=?, fecha_final=? WHERE consecutivo=?" +
												 "AND convenio=? AND institucion=?";
	
	
	/**
	 * Cadena de Modificacion de asocios por tiempo
	 * Tabla (asocios_x_rango_tiempo)
	 */
	private static String strCadenaModificacion1="UPDATE asocios_x_rango_tiempo SET tipo_servicio=?,servicio=?, tipo_cirugia=?, tipo_anestesia=?," +
				"asocio=?, tipo_tiempo_base=?, minutos_rango_inicial=?, minutos_rango_final=?, min_frac_adicional=?, valor_frac_adicional=?," +
				"usuario_modifica=?, fecha_modifica=?, hora_modifica=?, valor_asocio=?, liquidarpor = ?  WHERE codigo=? AND codigo_encab=?";
	
	/**
	 * Cadena de modificacion detalle asocios por tiempo;
	 * aqui se modifican el codigo de los tarifarios oficiales
	 * Tabla (detfec_asoc_ran_tiem) 
	 */
	private static String strCadenaModificacion2="UPDATE det_asoc_x_ran_tiem SET codigo=? WHERE codigo_asocxrangtiem=? AND codigo_tarfio_oficial=?" ;
	
	
	
	
	
	
				/*------------------------------------------
				 * 		CADENAS DE ELIMINACION
				 ------------------------------------------*/
				
	/**
	 * Cadena de eliminacion del encabezado de la tabla cabeza_asoc_x_ran_tiem
	 */
	private static String strCadenaEliminacion0="DELETE FROM cabeza_asoc_x_ran_tiem WHERE consecutivo=? AND institucion=?";
	
	/**
	 * Cadena de eliminacion de asocios por tiempo;
	 * en el caso de que la fecha inicial ya halla pasado
	 * entonces no se hace una eliminacion sino que se 
	 * actualiza la fecha final a la fecha actual donde se 
	 * se desea eliminar.
	 */
	private static String strCadenaEliminacion1="DELETE FROM asocios_x_rango_tiempo WHERE codigo=? AND codigo_encab=?";
	
	
	/**
	 * Cadena de consulta de fechas en las cuales se encuentran asocios
	 */
	private static String strCadenaEliminacion2="DELETE FROM det_asoc_x_ran_tiem WHERE codigo_asocxrangtiem=?";
	
	/**
	 * Cadena de consulta de fechas en las cuales se encuentran asocios
	 */
	private static String strCadenaEliminacion3="DELETE FROM det_asoc_x_ran_tiem WHERE codigo_asocxrangtiem=? AND codigo_tarfio_oficial=? ";
	
	
	private static String strCadenaEstaUtilizada="SELECT COUNT (*) FROM asocios_x_rango_tiempo WHERE codigo_encab=?";
	
	
				/*------------------------------------------
				 * 		CADENAS DE CONSULTA
				 ------------------------------------------*/
	
	
	private static String strCadenaConsultaEqmtarfio = "SELECT " +
				"cab.consecutivo AS consecutivo " +
				" FROM cabeza_asoc_x_ran_tiem cab " +
				" 	WHERE esquema_tarifario =? AND institucion=?";
	
	private static String strCadenaConsultaEqmtarfioGeneral = "SELECT " +
				"cab.consecutivo AS consecutivo " +
				" FROM cabeza_asoc_x_ran_tiem cab " +
				" 	WHERE esquema_tarifario_general =? AND institucion=?";
	
	private static String strCadenaConsultaFechas ="SELECT " +
				"cab.consecutivo AS consecutivo," +
				"CASE WHEN  cab.fecha_inicial IS NULL THEN '' ELSE to_char(cab.fecha_inicial, 'DD/MM/YYYY') END AS fec_ini_asoc," +
				"CASE WHEN  cab.fecha_final IS NULL THEN '' ELSE to_char(cab.fecha_final, 'DD/MM/YYYY') END AS fec_fin_asoc," +
				"COALESCE(cab.convenio,"+ConstantesBD.codigoNuncaValido+")  AS convenio," +
				"COALESCE(cab.esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") AS esq_tar," +
				"'"+ConstantesBD.acronimoSi+"' AS esta_bd, " +
				"cab.usuario_modifica AS usuario_modifica, " +			
				"cab.institucion AS institucion "+
				" FROM cabeza_asoc_x_ran_tiem cab" +
				" WHERE convenio=?";
	/**
	 * Cadena de Consulta de los datos de asocios por tiempo
	 * esta consulta se hace en la tabla
	 * --asocios_x_rango_tiempo
	 */
	private static String strCadenaConsulta1="SELECT " +
				"art.codigo AS codigo," +
				"art.codigo_encab AS codigo_encabe," +
				"COALESCE(art.tipo_servicio,'') AS tipo_servicio," +
				"COALESCE(art.servicio,"+ConstantesBD.codigoNuncaValido+") AS servicio," +
				"getnombreservicio(art.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio," +
				"COALESCE(art.tipo_cirugia,'')  AS tipo_cirugia," +
				"COALESCE(art.tipo_anestesia,"+ConstantesBD.codigoNuncaValido+") AS tipo_anestesia," +
				"art.asocio AS asocio," +
				"art.tipo_tiempo_base AS tipo_tiempo_base," +
				"art.minutos_rango_inicial AS min_rang_ini," +
				"art.minutos_rango_final AS min_rang_fin," +
				"art.min_frac_adicional AS min_frac_adic," +
				"art.valor_frac_adicional AS valor_frac_adic," +
				"art.valor_asocio AS valor_asocio, " +
				"'"+ConstantesBD.acronimoSi+"' AS esta_bd," +
				"liquidarpor AS liquidarpor " +
			" FROM asocios_x_rango_tiempo art ";
		
	
	/**
	 * Cadena que consulta el detalle de 
	 * los ocdigos de tarifarios oficiales en la 
	 * tabla det_asoc_x_ran_tiem
	 */
	private static String strCadenaConsulta2="SELECT " +
				"dart.codigo_tarfio_oficial AS cod_tar_ofi," +
				"getnombretarfioofi(dart.codigo_tarfio_oficial) AS nombre_tarfio," +
				"codigo_asocxrangtiem AS cod_asocio," +
				"dart.codigo AS valor_codigo " +
			"FROM det_asoc_x_ran_tiem dart " +
			"WHERE dart.codigo_asocxrangtiem=?";

	
	private static String strCadenaConsultaTipo ="SELECT  CASE WHEN COUNT (esquema_tarifario) = 0 THEN COUNT (esquema_tarifario_general)  ELSE COUNT (esquema_tarifario) END FROM cabeza_asoc_x_ran_tiem WHERE consecutivo=?";
	/*---------------------------------------------------------
	 * 		ARREGLOS CON LOS INDICES PARA LAS CONSULTAS
	 ---------------------------------------------------------*/
	private static String [] indicesMapaDetalle = {"codigo_","codigoEncabe_","tipoServicio_","servicio_","nombreServicio_","tipoCirugia_",
											"tipoAnestesia_","asocio_","tipoTiempoBase_","minRangIni_","minRangFin_","tipoEsq_",
											"minFracAdic_","valorFracAdic_","estaBd_","valorAsocio_","detalle_","esqTar_","institucion_","liquidarpor_"};

	private static String [] indicesMapaDetalleCod = {"codTarOfi_","nombreTarfio_","codAsocio_", "valorCodigo_"};
	
	private static String [] indicesFechas ={"consecutivo_","fecIniAsoc_","fecFinAsoc_","convenio_","esqTar_","institucion_","estaBd_","esUsa_","usuarioModifica_"};
	
	/*-------------------------------------------------------------------
	 * 					FIN ATRIBUTOS ASOCIOXRANGOTIEMPO
	 -------------------------------------------------------------------*/
	
	/*--------------------------------------------------------------------
	 *   METODOS PARA EL MEJO DE DATOS DE ASOCIO POR RANGO DE TIEMPO
	 -------------------------------------------------------------------*/
	
	
	
	/**
	 * Metodo encargado de insertar datos del encabezado,
	 * a la Tabla (cabeza_asoc_x_ran_tiem).
	 * ----------------------------------
	 *             PARAMETROS
	 * ----------------------------------
	 * @connection	Connection
	 * @encabezado	HashMap
	 * ------------------------------------
	 * 		   KEY'S DE ENCABEZADO
	 * -------------------------------------
	 * --convenio --> Opcional
	 * --esqTar --> Opcional
	 * --fecIniAsoc --> Opcional
	 * --fecFinAsoc --> Opcional
	 * --institucion --> Requerido
	 * --usuarioModifica --> Requerido
	 * ------------------------------------
	 * 		RETORNA UN BOOLEANO
	 * ------------------------------------
	 */
	public static boolean insertarencazado (Connection connection, HashMap encabezado)
	{
		logger.info("\n\n::::::ENTRE AL SQLBASE A INSERTAR ENCABEZADO ::::::::\n\n");
		logger.info("\n\n el valor del hashmap es "+encabezado);
		String cadena = strCadenaInsercion0;		
				
		try 
		{
			logger.info("\n Cadena de busqueda es "+cadena);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO cabeza_asoc_x_ran_tiem  ( convenio,esquema_tarifario,fecha_inicial,fecha_final," +
				"institucion,usuario_modifica,fecha_modifica,hora_modifica,esquema_tarifario_general,consecutivo) VALUES (?,?,?,?,?,?,?,?,?,?)
			 */
			
			
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_cab_asoc_x_ran_tiem");
			//Validacion de los datos que vienen***********************************************
			
			//1)se verifica que el convenio venga con datos apropiados,
			//de no ser si se inserta null
			if (encabezado.containsKey("convenio") && !encabezado.get("convenio").equals("") && Utilidades.convertirAEntero(encabezado.get("convenio")+"")>0)
				ps.setInt(1, Utilidades.convertirAEntero(encabezado.get("convenio")+""));
			else
				ps.setNull(1, Types.INTEGER);
			
			//2) se verifica que el esquema tarifario venga con datos apropiados,
			//de no ser asi se inserta un null
			if(encabezado.containsKey("tipoEsq") && (encabezado.get("tipoEsq")+"").equals("1"))
			{
				if (encabezado.containsKey("esqTar") && !encabezado.get("esqTar").equals("") && Utilidades.convertirAEntero(encabezado.get("esqTar")+"")>0)
					ps.setInt(2, Utilidades.convertirAEntero(encabezado.get("esqTar")+""));
				else
					ps.setNull(2, Types.INTEGER);
				ps.setNull(9, Types.INTEGER);
			}
			else
				if(encabezado.containsKey("tipoEsq") && (encabezado.get("tipoEsq")+"").equals("0"))
				{
					if (encabezado.containsKey("esqTar") && !encabezado.get("esqTar").equals("") && Utilidades.convertirAEntero(encabezado.get("esqTar")+"")>0)
						ps.setInt(9, Utilidades.convertirAEntero(encabezado.get("esqTar")+""));
					else
						ps.setNull(9, Types.INTEGER);
					
					ps.setNull(2, Types.INTEGER);
				}
				else
				{
					ps.setNull(2, Types.INTEGER);
					ps.setNull(9, Types.INTEGER);
				}
					
				
			//3) se verifica que la fecha inicial venga con datos apropiados,
			//de no se asi se le inserta un null
			if (encabezado.containsKey("fecIniAsoc") && !encabezado.get("fecIniAsoc").equals("") )
				ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fecIniAsoc")+"")));
			else
				ps.setNull(3, Types.DATE);
			
			//4) se verifica que la fehca final venga con datos apropiados,
			//de no ser asi se le inserta un null
			if (encabezado.containsKey("fecFinAsoc") && !encabezado.get("fecFinAsoc").equals("") )
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fecFinAsoc")+"")));
			else
				ps.setNull(4, Types.DATE);
			
			/*5) los siguientes datos son not null, por tal motivo 
			 * es necesario que vengan en el hashmap:
			 *
			 *	institucion       | integer               | not null
			 *	usuario_modifica  | character varying(30) | not null
			 *	fecha_modifica    | date                  | not null
			 *	hora_modifica     | character varying(5)  | not null
			 */
			
			ps.setInt(5, Utilidades.convertirAEntero(encabezado.get("institucion")+""));
			ps.setString(6, encabezado.get("usuarioModifica")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.setInt(10, consecutivo);
			
			if (ps.executeUpdate()>0)
				return true; 
			

		}
		catch (SQLException e)
		{
		 logger.error("\n\n :::::::::::Problema insertando datos en la tabla cabeza_asoc_x_ran_tiem "+e);
		}

		return false;
	}
	
	
	
	/**
	 *Metodo en cargado de insertar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public static boolean insertarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		logger.info("\n\n::::::ENTRE A INSERTAR CUERPO SQLBASE::::::::\n\n");
		boolean operacionTrue=false, existeEncabezado = false;;
		String cadena1=strCadenaInsercion1;

		try 
		{
			logger.info("\n esquema ratifario es "+asociosXrangoTiempo.getEsqTar());
			if (asociosXrangoTiempo.getEsqTar()>0)
			{
				
				String cadena2 = strCadenaVerificacionInsercion1 + " WHERE institucion = "+asociosXrangoTiempo.getInstitucion();
				
				if(asociosXrangoTiempo.getTipoEsq()==1)
					cadena2 += " and esquema_tarifario = "+asociosXrangoTiempo.getEsqTar();
				else
					cadena2 += " and esquema_tarifario_general = "+asociosXrangoTiempo.getEsqTar();
				Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(cadena2));
				if(rs.next())
				{
					asociosXrangoTiempo.setCodigoCabeza(rs.getInt("consecutivo"));
					existeEncabezado = true;
				}
				
				if(!existeEncabezado)
				{
					HashMap tmp = new HashMap();
					
					tmp.put("esqTar", asociosXrangoTiempo.getEsqTar());
					tmp.put("institucion", asociosXrangoTiempo.getInstitucion());
					tmp.put("usuarioModifica", asociosXrangoTiempo.getUsuarioModifica());
					tmp.put("tipoEsq", asociosXrangoTiempo.getTipoEsq());
				
					if(	insertarencazado(connection, tmp))
						asociosXrangoTiempo.setCodigoCabeza(consultarSqtarfio(connection, asociosXrangoTiempo.getEsqTar(), asociosXrangoTiempo.getInstitucion(),asociosXrangoTiempo.getTipoEsq()));
				}
				
			}
			
			
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena1, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO asocios_x_rango_tiempo (codigo_encab,tipo_servicio,tipo_cirugia," +
					"tipo_anestesia,asocio,tipo_tiempo_base,minutos_rango_inicial,minutos_rango_final," +
					"min_frac_adicional,valor_frac_adicional,usuario_modifica,fecha_modifica,hora_modifica," +
					"servicio,valor_asocio,codigo,liquidarpor) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
				 */
				
				//codigo
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_asoc_x_ran_tiem");
				
				if (asociosXrangoTiempo.getCodigoCabeza()>0)
				ps.setInt(1, asociosXrangoTiempo.getCodigoCabeza());
				
				if (!asociosXrangoTiempo.getTipoServicio().equals(""))
					ps.setString(2, asociosXrangoTiempo.getTipoServicio());
				else
					ps.setNull(2, Types.VARCHAR);
				
				if (!asociosXrangoTiempo.getTipoCirugia().equals(""))
					ps.setString(3, asociosXrangoTiempo.getTipoCirugia());
				else
					ps.setNull(3, Types.VARCHAR);
				
				if (asociosXrangoTiempo.getTipoAnestesia()>0)
					ps.setInt(4, asociosXrangoTiempo.getTipoAnestesia());
				else
					ps.setNull(4, Types.INTEGER );
				
				ps.setInt(5, asociosXrangoTiempo.getAsocio());
				ps.setString(6, asociosXrangoTiempo.getTipoTiempoBase());
				ps.setInt(7, asociosXrangoTiempo.getMinutosRangoInicial());
				ps.setInt(8, asociosXrangoTiempo.getMinutosRangoFinal());
				
				if (asociosXrangoTiempo.getMinFracAdicional()>0)
					ps.setInt(9, asociosXrangoTiempo.getMinFracAdicional());
				else
					ps.setNull(9, Types.INTEGER );
			
				if (!asociosXrangoTiempo.getValorFracAdiconal().equals(""))
					ps.setDouble(10, Utilidades.convertirADouble(asociosXrangoTiempo.getValorFracAdiconal()));
				else
					ps.setNull(10, Types.NUMERIC);
						
				
				ps.setString(11, asociosXrangoTiempo.getUsuarioModifica());
				ps.setDate(12, Date.valueOf(asociosXrangoTiempo.getFechaModifica()));
				ps.setString(13, asociosXrangoTiempo.getHoraModifica());
				
				if (asociosXrangoTiempo.getServicio()>0) 
					ps.setInt(14, asociosXrangoTiempo.getServicio());
				else
					ps.setNull(14, Types.INTEGER);			
				
				
				 ps.setDouble(15, Utilidades.convertirADouble(asociosXrangoTiempo.getValorAsocio()));
				 //codigo
				 ps.setInt(16, codigo);
				 //logger.info("valor de liquidar por >> "+asociosXrangoTiempo.getLiquidarPor());
				 //liquidar por
				 ps.setString(17, asociosXrangoTiempo.getLiquidarPor());
				 
					 if (ps.executeUpdate()>0)
					 {
						 	int secuencia=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_asoc_x_ran_tiem");
							operacionTrue=insertarDetalleTarifarioOficial(connection, asociosXrangoTiempo,secuencia);
							
							return operacionTrue;
							
						}
			
			
		} 
		catch (SQLException e) 
		{
			logger.error("\n\n *** Error ingresando los datos en la tabla asocios_x_rango_tiempo"+e);
		}
		
		return operacionTrue;
	}
	
	
	/**
	 * Metodo encargado de insertar el detalle detarifa oficial
	 * @param connection
	 * @param asociosXRangoTiempo
	 * @param secuencia
	 * @return "true" para indicar que inserto sin problema
	 */
	public static boolean insertarDetalleTarifarioOficial (Connection connection, DtoAsociosXRangoTiempo asociosXRangoTiempo,int secuencia)
	{
		logger.info("\n\n:::::::::::::ENTRE A INSERTAR DETALLE SQLBASE::::::::");
		boolean operacionTrue = true;
		String cadena=strCadenaInsercion2;
		try 
		{
			//logger.info("\n:::: 1  el valor de size del arrray ::::: "+asociosXRangoTiempo.getDetalleAsociosRangoTiempo().size()+" la secuencia es :"+secuencia);
			for (int i=0;i< asociosXRangoTiempo.getDetalleAsociosRangoTiempo().size();i++)
			{
				/*logger.info("\n:::: numero ==> "+asociosXRangoTiempo.getDetalleAsociosRangoTiempo().get(i).getNumeroCodigoTarifarioOficial());
				logger.info("\n:::: numeroOld ==> "+asociosXRangoTiempo.getDetalleAsociosRangoTiempo().get(i).getNumeroCodigoTarifarioOficialOld());
				logger.info("\n:::: codigo ==> "+asociosXRangoTiempo.getDetalleAsociosRangoTiempo().get(i).getCodigoDetAsocXRangTiem());
				logger.info("\n:::: tarifario ==> "+asociosXRangoTiempo.getDetalleAsociosRangoTiempo().get(i).getCodigoTarifarioOficial());*/
			}
			for (DtoDetalleAsociosXRangoTiempo det:asociosXRangoTiempo.getDetalleAsociosRangoTiempo())
			 {
					if (!det.getNumeroCodigoTarifarioOficial().equals(det.getNumeroCodigoTarifarioOficialOld()))
					{
						//logger.info("\n:::: 2 :::::");
						 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					
						 /**
						  * INSERT INTO det_asoc_x_ran_tiem (codigo_asocxrangtiem,codigo_tarfio_oficial,codigo) VALUES (?,?,?)
						  */
						 
						 //	logger.info("el asocio es "+secuencia);
						 ps.setInt(1, secuencia);
						// logger.info("el codigo es "+det.getCodigoTarifarioOficial());
						 ps.setInt(2, det.getCodigoTarifarioOficial());
						// logger.info("el valor es "+det.getNumeroCodigoTarifarioOficial());
						 ps.setString(3, det.getNumeroCodigoTarifarioOficial());
					 
						 if (ps.executeUpdate()<0)
						return false;
					}
			}
			
		} catch (SQLException e) 
		{
			operacionTrue=false;
			logger.error("\n\n *** Error ingresando datos en la tabla det_asoc_x_ran_tiem"+e);
		}
		//logger.info("\n:::: 3 :::::");
		return operacionTrue;
	}
	
	
	/**
	 * Metodo encargado de obtener las vigencias de los convenios.
	 * este metodo recibe como parametros:
	 * ----------------------------------------
	 * 				PARAMETROS
	 * ----------------------------------------
	 * -- connection Connection
	 * -- convenio	 int
	 * 
	 * Este metodo retorna un mapa con los siguientes key's
	 * ---------------------------------------
	 * 			KEY'S MAPA QUE RETORNA
	 * ---------------------------------------
	 * --consecutivo_
	 * --fecIniAsoc_
	 * --fecFinAsoc_
	 * --institucion_
	 * --convenio_
	 * --esqTar_
	 * --numRegistros
	 */
	public static HashMap obtenerFechasAsocios (Connection connection, int convenio)
	{
		logger.info("\n\n:::::::::::::ENTRE A obtenerFechasAsocios SQLBASE::::::::");
		logger.info("el valor del convenio es "+convenio);
		HashMap mapa = new HashMap ();
		String cadena =strCadenaConsultaFechas;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, convenio);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			for (int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
				mapa.put("esUsa_"+i, estaUtilizado(connection, Utilidades.convertirAEntero(mapa.get("consecutivo_"+i)+"")));
		} 
		catch (SQLException e) 
		{
			logger.error("\n problema consultando las fechas de asocios "+e);
		} 
		
		
		//logger.info("\n el resultado de la consulta es "+mapa);
		mapa.put("INDICES_MAPA", indicesFechas);
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los asocios por rango de fecha.
	 * ---------------------------------------
	 * 				PARAMETROS
	 * ---------------------------------------
	 * @param connection
	 * @param criteriosBusqueda
	 * 
	 * Este metodo devuelve un HashMap con los siguiente key's
	 * ----------------------------------------
	 * 		KEY'S DEL MAPA QUE DEVUELVE
	 * ----------------------------------------
	 * --codigo_
	 * --codigoEncabe_
	 * --tipoServicio_
	 * --servicio_
	 * --nombreServicio_
	 * --tipoCirugia_
	 * --tipoAnestesia_
	 * --asocio_
	 * --tipoTiempoBase_
	 * --minRangIni_
	 * --minRangFin_
	 * --minRangFin_
	 * --valorFracAdic_
	 * --estaBd_
	 * --valorAsocio_
	 * --detalle_
	 * @return
	 */
	public static HashMap consultarAsociosPorRangoFecha (Connection connection, DtoAsociosXRangoTiempo criteriosBusqueda)
	{
		logger.info("\n\n:::::::::::::ENTRE A consultarAsociosPorRangoFecha SQLBASE::::::::");
		HashMap mapa = new HashMap ();
		/*logger.info("\n\n************************************");
		logger.info("\n el valor del convenio es "+cabeza.get("convenio"));
		logger.info("\n el valor del esquema tarifario es "+cabeza.get("esqTar"));
		logger.info("\n************************************\n\n");*/
		logger.info("el valor del codigo d ela cabeza es "+criteriosBusqueda.getCodigoCabeza());
		String cadena = strCadenaConsulta1,where=" WHERE 1=1 ";
	
		if (criteriosBusqueda.getAsocio()>0)
			where+=" AND asocio="+criteriosBusqueda.getAsocio();
		
		if (criteriosBusqueda.getCodigo()>0)
			where+=" AND codigo="+criteriosBusqueda.getCodigo();
		
		if (criteriosBusqueda.getCodigoCabeza()>0)
			where+=" AND codigo_encab="+criteriosBusqueda.getCodigoCabeza();
		
		if (!criteriosBusqueda.getTipoServicio().equals(""))
			where+=" AND tipo_servicio='"+criteriosBusqueda.getTipoServicio()+"'";
		
		if (criteriosBusqueda.getServicio()>0)
			where+=" AND servicio="+criteriosBusqueda.getServicio();
		
		if (!criteriosBusqueda.getTipoCirugia().equals(""))
			where +=" AND tipo_cirugia='"+criteriosBusqueda.getTipoCirugia()+"'";
		
		if (criteriosBusqueda.getTipoAnestesia()>0)
			where+=" AND tipo_anestesia="+criteriosBusqueda.getTipoAnestesia();
		
		if (!criteriosBusqueda.getTipoTiempoBase().equals(""))
			where+=" AND tipo_tiempo_base='"+criteriosBusqueda.getTipoTiempoBase()+"'";
		
		if (criteriosBusqueda.getMinutosRangoInicial()>0)
			where+=" AND minutos_rango_inicial="+criteriosBusqueda.getMinutosRangoInicial();
		
		if (criteriosBusqueda.getMinutosRangoFinal()>0)
			where+=" AND minutos_rango_final="+criteriosBusqueda.getMinutosRangoFinal();
		
		if (criteriosBusqueda.getMinFracAdicional()>0)
			where+=" AND min_frac_adicional="+criteriosBusqueda.getMinFracAdicional();
		
		if (!criteriosBusqueda.getValorFracAdiconal().equals(""))
			where+=" AND valor_frac_adicional='"+criteriosBusqueda.getValorFracAdiconal()+"'";
		
			
	  try 
		{
		   cadena+=where+" ORDER BY tipo_servicio, tipo_cirugia, asocio ";
		   
		    logger.info("\n\n************************************************************");
		  	logger.info("\n la cadena de consulta es ==> "+cadena);
		  	logger.info("\n************************************************************\n\n");
		  	
		  	PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		
			logger.info("\n \n la respuesta del mapa es "+mapa+" \n\n");
			
			for (int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
				mapa.put("detalle_"+i, consultarDetalleTarifarioOficial(connection, Utilidades.convertirAEntero(mapa.get("codigo_"+i)+"")));
			
			
			
		} catch (SQLException e) {
			logger.error("Error consutando la tabla asocios_x_rango_tiempo "+e);
		}
		
		
		mapa.put("INDICES_MAPA", indicesMapaDetalle);
		
		//logger.info("\n\n el resultado de la consulta es "+mapa);
		return mapa;
	}
	
	
	/**
	 *Metodo encargado de consultar el detalle del tarifario oficial 
	 * @param connection
	 * @param codigo_asocxrangtiem
	 * @return
	 */
	public static HashMap consultarDetalleTarifarioOficial (Connection connection,int codigo_asocxrangtiem)
	{
		logger.info("\n\n:::::::::::::ENTRE A consultarDetalleTarifarioOficial SQLBASE::::::::");
		HashMap mapa = new HashMap();
		String cadena = strCadenaConsulta2;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo_asocxrangtiem);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		} catch (SQLException e) {
			logger.error("\n\n\n *** Error consultando la tabla det_asoc_x_ran_tiem "+e);
		}
		
		return mapa;
	}
	
	/**
	 * Metodo que se encarga de verificar si se borra
	 * el encabezado o no en las tablas.
	 * @param connection
	 * @param codcab
	 * @param codigodet
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection,int codcab, int codigodet, int institucion)
	{
		boolean operacionTrue =false;
		
		int tipo=0;
		tipo=queTipoEs(connection, codcab);
		
		if (tipo>0)
		{
		
			if (estaUtilizado(connection, codcab)>1)
				{
					operacionTrue=eliminar(connection, codigodet, codcab);
				}
			else
			{
				operacionTrue=eliminar(connection, codigodet, codcab);
				operacionTrue=elimiarcabezado(connection, codcab, institucion);
			}
		}
		else
		{
			eliminar(connection, codigodet, codcab);
		}
		
		return operacionTrue;
	}
	
	
	public static boolean elimiarcabezado (Connection connection, int codigo , int institucion)
	{
		logger.info("\n\n:::::::::::::ENTRE A elimiarcabezado SQLBASE::::::::");
		
		
		boolean operacionTrue = false;
		String cadena = strCadenaEliminacion0;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, institucion);
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.error("\n Problema al eliminar datos en la tabla cabeza_asoc_x_ran_tiem "+e);
		}
		
		
		return operacionTrue;
	}
	
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla 
	 * asocios_x_rango_tiempo filtrandolos por codigo e institucion.
	 * @param connection
	 * @param codigo
	 * @param codigoEncab
	 * @return
	 */
	public static boolean eliminar (Connection connection,int codigo,int codigoEncab)
	{
		boolean operacionTrue=true;
		String cadena=strCadenaEliminacion1;
		
		try 
		{
			//se elimina el detalle
			operacionTrue=eliminarDetalle1(connection, codigo);
			if (operacionTrue)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigo);
				ps.setInt(2, codigoEncab);
				if(ps.executeUpdate()>0)
					return true;
				else
					return false;
			}	
			
	
		} catch (SQLException e) 
		{	
				operacionTrue=false;
				logger.error("\n\n *** Error al eliminar los datos de la tabla asocios_x_rango_tiempo "+e);
		}
		
		
		
		return operacionTrue;
	}
	
	/**
	 *Metodo encargado de eliminar el de talle de la tabla 
	 *asocios_x_rango_tiempo que se encuentra en la tabla 
	 *det_asoc_x_ran_tiem 
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarDetalle1 (Connection connection, int codAsocio)
	{
		logger.info("\n\n:::::::::::::ENTRE A ELIMIAR DETALLE SQLBASE::::::::");
		boolean operacionTrue=true;
		String cadena=strCadenaEliminacion2;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codAsocio);
		
			if(ps.executeUpdate()>0)
				return true;
	
		} catch (SQLException e) 
		{
			operacionTrue=false;
			logger.error("\n\n *** Error al eliminar los datos de la tabla det_asoc_x_ran_tiem "+e);
		}
		
		
		return operacionTrue;
	}
	
	
	/**
	 *Metodo encargado de eliminar el de talle de la tabla 
	 *asocios_x_rango_tiempo que se encuentra en la tabla 
	 *det_asoc_x_ran_tiem 
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarDetalle2 (Connection connection, int codAsocio,int codTarfio)
	{
		logger.info("\n\n:::::::::::::ENTRE A ELIMIAR DETALLE SQLBASE::::::::");
		boolean operacionTrue=true;
		String cadena=strCadenaEliminacion3;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codAsocio);
			ps.setInt(2, codTarfio);
			if(ps.executeUpdate()>0)
				return true;
	
		} catch (SQLException e) 
		{
			operacionTrue=false;
			logger.error("\n\n *** Error al eliminar los datos de la tabla det_asoc_x_ran_tiem "+e);
		}
		
		
		return operacionTrue;
	}
	
	/**
	 * Metodo encargado de modificar los datos del encabezado.
	 * @param connection
	 * @param encab
	 * @return
	 */
	public static boolean modificarEncabezado (Connection connection,HashMap encab)
	{
		logger.info("\n el valor del mapa al entrar a modificarEncabezado "+encab);
		String cadena = strCadenaModificacion0;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE cabeza_asoc_x_ran_tiem SET fecha_inicial=?, fecha_final=? WHERE consecutivo=?" +
											"AND convenio=? AND institucion=?
			 */
			
			ps.setDate(1,  Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(encab.get("fecIniAsoc")+"")));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(encab.get("fecFinAsoc")+"")));
			ps.setInt(3, Utilidades.convertirAEntero(encab.get("consecutivo")+""));
			ps.setInt(4, Utilidades.convertirAEntero(encab.get("convenio")+""));
			ps.setInt(5, Utilidades.convertirAEntero(encab.get("institucion")+""));
			if(ps.executeUpdate()>0)
				return true;
			
			
		}
		catch (SQLException e)
		{
			logger.error("\n Problema modificando los datos de la tabla cabeza_asoc_x_ran_tiem "+e);
		}
		
		return false;
	}
	
	
	/**
	 *Metodo en cargado de modificar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public static boolean modificarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		logger.info("\n\n:::::::::::::ENTRE A MODIFICAR SQLBASE::::::::");
		boolean operacionTrue=false;
		String cadena1=strCadenaModificacion1;

		try 
		{
			
		
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena1, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
			/**
			 * UPDATE asocios_x_rango_tiempo SET tipo_servicio=?,servicio=?, tipo_cirugia=?, tipo_anestesia=?," +
				"asocio=?, tipo_tiempo_base=?, minutos_rango_inicial=?, minutos_rango_final=?, min_frac_adicional=?, valor_frac_adicional=?," +
				"usuario_modifica=?, fecha_modifica=?, hora_modifica=?, valor_asocio=?, liquidarpor = ?  WHERE codigo=? AND codigo_encab=?
			 */
			
			if (!asociosXrangoTiempo.getTipoServicio().equals(""))
				ps.setString(1, asociosXrangoTiempo.getTipoServicio());
			else
				ps.setNull(1, Types.VARCHAR);
			
			if (asociosXrangoTiempo.getServicio()>0) 
				ps.setInt(2, asociosXrangoTiempo.getServicio());
			else
				ps.setNull(2, Types.INTEGER);
			
			if (!asociosXrangoTiempo.getTipoCirugia().equals(""))
				ps.setString(3, asociosXrangoTiempo.getTipoCirugia());
			else
				ps.setNull(3, Types.VARCHAR);
			
			if (asociosXrangoTiempo.getTipoAnestesia()>0)
				ps.setInt(4, asociosXrangoTiempo.getTipoAnestesia());
			else
				ps.setNull(4, Types.INTEGER);
			
			ps.setInt(5, asociosXrangoTiempo.getAsocio());
			ps.setString(6, asociosXrangoTiempo.getTipoTiempoBase());
			ps.setInt(7, asociosXrangoTiempo.getMinutosRangoInicial());
			ps.setInt(8, asociosXrangoTiempo.getMinutosRangoFinal());
			
			if (asociosXrangoTiempo.getMinFracAdicional()>0)
				ps.setInt(9, asociosXrangoTiempo.getMinFracAdicional());
			else
				ps.setNull(9, Types.INTEGER);
		
			if (!asociosXrangoTiempo.getValorFracAdiconal().equals(""))
				ps.setDouble(10, Utilidades.convertirADouble(asociosXrangoTiempo.getValorFracAdiconal()+""));
			else
				ps.setNull(10, Types.NUMERIC);
			
				
		
			ps.setString(11, asociosXrangoTiempo.getUsuarioModifica());
			ps.setDate(12, Date.valueOf(asociosXrangoTiempo.getFechaModifica()));
			ps.setString(13, asociosXrangoTiempo.getHoraModifica());
			ps.setDouble(14, Utilidades.convertirADouble(asociosXrangoTiempo.getValorAsocio()));
			ps.setString(15, asociosXrangoTiempo.getLiquidarPor());
			ps.setInt(16, asociosXrangoTiempo.getCodigo());
			ps.setInt(17, asociosXrangoTiempo.getCodigoCabeza());
			
				 
			if (ps.executeUpdate()>0)
			{
				
				operacionTrue=fachadaDetalle(connection, asociosXrangoTiempo);
				
				return operacionTrue;
				
			}
		} 
		catch (SQLException e) 
		{
			logger.error("\n\n *** Error modificando los datos en la tabla asocios_x_rango_tiempo "+e);
		}
		
		return operacionTrue;
	}
	
	
	/**
	 * Metodo encargado de modificar el detalle detarifa oficial
	 * @param connection
	 * @param asociosXRangoTiempo
	 * @param secuencia
	 * @return "true" para indicar que inserto sin problema
	 */
	public static boolean modificarDetalleTarifarioOficial (Connection connection, DtoAsociosXRangoTiempo asociosXRangoTiempo)
	{
		logger.info("\n\n:::::::::::::ENTRE A MODIFICAR DETALLE SQLBASE::::::::");
		boolean operacionTrue = true;
		String cadena=strCadenaModificacion2;
				
		try 
		{
			 for (DtoDetalleAsociosXRangoTiempo det:asociosXRangoTiempo.getDetalleAsociosRangoTiempo())
			 {
				 
				 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				 
				 /**
				  * UPDATE det_asoc_x_ran_tiem SET codigo=? WHERE codigo_asocxrangtiem=? AND codigo_tarfio_oficial=?
				  */
				 
				// logger.info("numeroCodigoTarfio "+det.getNumeroCodigoTarifarioOficial());
				 ps.setString(1, det.getNumeroCodigoTarifarioOficial());
				 //logger.info("Codigoasoc "+det.getCodigoDetAsocXRangTiem());
				 ps.setInt(2, det.getCodigoDetAsocXRangTiem());
				 //logger.info("CodigoTarfio "+ det.getCodigoTarifarioOficial());
				 ps.setInt(3, det.getCodigoTarifarioOficial());
				
				
				if (ps.executeUpdate()<0)
					operacionTrue=false;
				 
			}
			 
			 
			
		} catch (SQLException e) 
		{
			operacionTrue=false;
			logger.error("\n\n *** Error modificando datos en la tabla det_asoc_x_ran_tiem "+e);
		}
		
		return operacionTrue;
	}
	
	
	
	/**
	 * Metodo encargado de identificar que se va ha hacer con la tabla de detalle. 
	 * @param connection
	 * @param asociosXRangoTiempo
	 * @return
	 */
	public static boolean fachadaDetalle (Connection connection,DtoAsociosXRangoTiempo asociosXRangoTiempo)
	{
		logger.info("\n\n:::::::::::::ENTRE A FACHADA ::::::::");
		boolean operaciontrue=true;
		
		 for (DtoDetalleAsociosXRangoTiempo det:asociosXRangoTiempo.getDetalleAsociosRangoTiempo())
		 {
			 
			 if (det.getCodigoDetAsocXRangTiem() > 0 && det.getNumeroCodigoTarifarioOficial().equals(""))
			 {
				 operaciontrue=eliminarDetalle2(connection, det.getCodigoDetAsocXRangTiem(),det.getCodigoTarifarioOficial());
			 }
			 else
				 if (det.getCodigoDetAsocXRangTiem() > 0 && !det.getNumeroCodigoTarifarioOficial().equals("") && !det.getNumeroCodigoTarifarioOficial().equals(det.getNumeroCodigoTarifarioOficialOld()))
				 {
					 logger.info("\n:::entre al if 1");
					 logger.info("\n\n ::: numeroCodigoTarfio "+det.getNumeroCodigoTarifarioOficial());
					 logger.info("\n\n :::: numeroCodigoTarfioold "+det.getNumeroCodigoTarifarioOficialOld());
					 operaciontrue=modificarDetalleTarifarioOficial(connection, asociosXRangoTiempo);
				 }
				else
					if (det.getCodigoDetAsocXRangTiem() < 0 && det.getNumeroCodigoTarifarioOficial().equals(""))
					{
						operaciontrue=true;
					}
					else
						if (det.getCodigoDetAsocXRangTiem() < 0 && !det.getNumeroCodigoTarifarioOficial().equals("") && !det.getNumeroCodigoTarifarioOficial().equals(det.getNumeroCodigoTarifarioOficialOld()))
						{
							logger.info("\n:::entre al if 2");
							logger.info("\n ::: getCodigoDetAsocXRangTiem "+det.getCodigoDetAsocXRangTiem());
							 logger.info("\n\n ::: numeroCodigoTarfio "+det.getNumeroCodigoTarifarioOficial());
							 logger.info("\n\n :::: numeroCodigoTarfioold "+det.getNumeroCodigoTarifarioOficialOld());
							operaciontrue=insertarDetalleTarifarioOficial(connection, asociosXRangoTiempo,asociosXRangoTiempo.getCodigo());
						}
			
			 
		}
		
		
		
		return operaciontrue;
	}
	
	
	/**
	 * Metodo que devuelve la cantidad de detalles
	 * que tiene un registro en el encabezado.
	 * @param connection
	 * @param esqtarfio
	 * @return
	 */
	public static int estaUtilizado (Connection connection, int codcab)
	{
		String cadena = strCadenaEstaUtilizada;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codcab);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e) 
		{
		  logger.info("\n problema al consultar los datos en la tabla asocios_x_rango_tiempo "+e);
		}
		
		return 0;
	}
	
	/**
	 * Metodo encargado de consultar el codigo del consecutivo
	 * de la cabeza del esquema tarifario.
	 * -- El parametro tipo hace referencia a si es de tipo
	 * particular (1) o genreal (0)
	 * @param connection
	 * @param esqtarfio
	 * @tipo
	 * @return
	 */
	public static int consultarSqtarfio (Connection connection, int esqtarfio,int institucion, int tipo)
	{
		logger.info("\n\n:::::::::::::ENTRE A consultarSqtarfio SQLBASE ::::::::");
		logger.info("\n el tipo es ==> "+tipo);
		int codCabeza = 0;
		String cadena = "";
		if (tipo==1)
			cadena = strCadenaConsultaEqmtarfio;
		else
			if(tipo==0)
				cadena = strCadenaConsultaEqmtarfioGeneral;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, esqtarfio);
			ps.setInt(2, institucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				codCabeza = rs.getInt(1);
		
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando la tabla cabeza_asoc_x_ran_tiem "+e);
		}
		
		
		return codCabeza;
	}
	
	
	/**
	 * Metodo encargado de identificar que tipo de encabezado es (convenio o esquema_tarifario) 
	 * @param connection
	 * @param consecutivo
	 * @return
	 */
	public static int queTipoEs (Connection connection,int consecutivo)
	{
		String cadena=strCadenaConsultaTipo;
		int tipo =0;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) );
			ps.setInt(1, consecutivo);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				tipo = rs.getInt(1);
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema consultando en la tabla cabeza_asoc_x_ran_tiem "+e);
		}
		
		return tipo;
	}
	
		
		/*--------------------------------------------------------------------
	
	 *  FIN METODOS PARA EL MEJO DE DATOS DE ASOCIO POR RANGO DE TIEMPO
	 -------------------------------------------------------------------*/
	
}