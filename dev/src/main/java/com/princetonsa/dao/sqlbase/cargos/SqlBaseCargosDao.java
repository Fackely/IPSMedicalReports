/**
 * 
 */
package com.princetonsa.dao.sqlbase.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.cargos.DtoDetalleCargoArticuloConsumo;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseCargosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCargosDao.class);
	
	/**
	 * CADENA para insertar el detalle del cargo
	 */
	private static String insertarDetalleCargoStr=" INSERT INTO det_cargos " +
														"(" +
															"codigo_detalle_cargo, " +	//1
															"sub_cuenta, " +			//2
															"convenio, " +				//3
															"esquema_tarifario, " +		//4
															"cantidad_cargada, " +		//5
															"valor_unitario_tarifa, " +	//6
															"valor_unitario_cargado, " +//7
															"valor_total_cargado, " +	//8
															"porcentaje_cargado, " +	//9
															"porcentaje_recargo, " +	//10
															"valor_unitario_recargo, " +//11
															"porcentaje_dcto, " +		//12
															"valor_unitario_dcto, " +	//13
															"valor_unitario_iva, " +	//14
															"requiere_autorizacion, " +	//15
															/*"nro_autorizacion, " +		//16*/
															"estado, " +				//16
															"cubierto, " +				//17
															"tipo_distribucion, " +		//18
															"solicitud, " +				//19
															"servicio, " +				//20
															"articulo, " +				//21
															"servicio_cx, " +			//22
															"tipo_asocio, " +			//23
															"facturado, " +				//24
															"tipo_solicitud, " +		//25	
															"paquetizado, " +			//26
															"cargo_padre, " +			//27
															"usuario_modifica, " +		//28
															"cod_sol_subcuenta, " +		//29
															"observaciones, "+			//30
															"contrato, "+				//31
															"fecha_modifica, " +				
															"hora_modifica," +
															"det_cx_honorarios, " +		//32		
															"det_asocio_cx_salas_mat, "+//33
															"es_portatil, "			+	//34
															"dejar_excento, " 		+	//35
															"porcentaje_dcto_prom_serv, " +//36
															"valor_descuento_prom_serv, " +//37
															"porc_honorario_prom_serv, " +//38
															"valor_honorario_prom_serv, "+//39
															"programa, " +				//40	
															"porcentaje_dcto_bono_serv, " +//41
															"valor_descuento_bono_serv," +//42
															"porcentaje_dcto_odontologico, " +//43
															"valor_descuento_odontologico, " +//44
															"det_paq_odon_convenio "+//45
														") values(" +
																	"?,?," +//2
																	"?,?," +//4
																	"?,?," +//6
																	"?,?," +//8
																	"?,?," +//10
																	"?,?," +//12
																	"?,?," +//14
																	"?,?," +//16
																	"?,?," +//18
																	"?,?," +//20
																	"?,?," +//22
																	"?,?," +//24
																	"?,?," +//26
																	"?,?," +//28
																	"?,?," +//30
																	"?, " +//31
																	"CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
																	"?,?," +//33
																	"?,?," +//35
																	"?,?," +//37
																	"?,?, " +//39
																	"?,?, " +//41
																	"?,?, " +//43
																	"?,? "+	//45
																") ";
	
	/**
	 * 
	 */
	private static String insertarDetalleCargosConsumoArt= "INSERT INTO facturacion.det_cargos_art_consumo " +
															"( codigo , " +				//1
															"det_cargo , " +			//2
															"articulo , " +				//3
															"cantidad , " +				//4
															"valor_unitario , " +		//5
															"valor_total , " +			//6
															"porcentaje , " +			//7
															"usuario_modifica , " +		//8
															"fecha_modifica , " +		
															"hora_modifica ) " +
															"values " +
															"( ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+" )";//8 			
	
	/**
	 * inserta error en errores_det_cargos
	 */
	private final static String insertarErrorDetalleCargoStr="insert into errores_det_cargos (codigo, codigo_detalle_cargo, error) values(?,?,?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del servicio de una solicitud de interconsulta en una BD
	 * Genérica
	 */
	private final static String busquedaServicioEnInterconsultaStr="SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_inter where numero_solicitud=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del servicio de una solicitud de procedimiento en una BD
	 * Genérica
	 */
	private final static String busquedaServicioEnProcedimientosStr="SELECT codigo_servicio_solicitado as codigoServicio from sol_procedimientos where numero_solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del servicio PORTATIL de una solicitud de procedimiento en una BD
	 * Genérica
	 */
	private final static String busquedaServicioPORTATILEnProcedimientosStr="SELECT portatil_asociado as codigoServicio from sol_procedimientos where numero_solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del servicio de una cita en una BD Genérica
	 */
	private final static String busquedaServicioEnCitaStr="SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_consulta where numero_solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del servicio de una evolución en una BD
	 * Genérica
	 */
	private final static String busquedaServicioEnEvolucionStr="SELECT servicio as codigoServicio from det_cargos where solicitud= (SELECT valoracion from evoluciones where codigo=?) AND eliminado='"+ConstantesBD.acronimoNo+"'  UNION SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_inter where numero_solicitud=(SELECT valoracion from evoluciones where codigo=?)";

	
	
	private static String consultarConveniosPlanEspecialPacienteStr = "SELECT count(con.centro_costo_plan_especial) as planEspecialConvenio " +
																			"from convenios con INNER JOIN sub_cuentas scu " +
																			"ON(scu.convenio=con.codigo) " +
																			"where scu.ingreso=? ";
	
	
	//*****************************************************SERVICIOS*****************************************************************************/
	
	/**
	 * metodo que obtiene la tarifa base x servicio
	 * @param con
	 * @param codigoTipoTarifario
	 * @param codigoServicio
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static InfoTarifaVigente obtenerTarifaBaseServicio(Connection con, int codigoTipoTarifario, int codigoServicio, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		InfoTarifaVigente infoTarifa= new InfoTarifaVigente();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerTarifaBaseServicio");
			String consulta="";
			
			String fecha=UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia;
		//mt6587  se modifica condición de la fecha
			///si es SOAT
			if(codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat){
				consulta=	"(SELECT codigo, coalesce(valor_tarifa,0) AS valor, codigo_tipo_liquidacion as tipoliq, liq_asocios as liqasocios,fecha_vigencia as fecha_vigencia FROM view_tarifas_soat WHERE codigo_servicio = ? AND codigo_esquema_tarifario = ? AND fecha_vigencia<=to_date('"+fecha+"','dd/MM/yyyy')) " +
								"UNION ALL " +
							"(SELECT codigo, coalesce(valor_tarifa,0) AS valor, tipo_liquidacion as tipoliq, liq_asocios as liqasocios,fecha_vigencia as fecha_vigencia FROM tarifas_soat WHERE servicio = ? AND esquema_tarifario = ? AND fecha_vigencia IS NULL ) ";
			}
			//Si no es porque es ISS o ISS2000o TARIFAS PROPIAS dado que 
			//todas estas tarifas se guardan en el esquema de las TARIFAS_ISS
			else{
				consulta=	"(SELECT codigo, coalesce(valor_tarifa,0) AS valor, codigo_tipo_liquidacion as tipoliq, " +
						" liq_asocios as liqasocios,fecha_vigencia as fecha_vigencia " +
								" FROM facturacion.view_tarifas_iss WHERE codigo_servicio = ? AND codigo_esquema_tarifario = ? " +
								" AND fecha_vigencia<=to_date('"+fecha+"','dd/MM/yyyy')) " +
								" UNION ALL " +
							"(SELECT codigo, coalesce(valor_tarifa,0) AS valor, tipo_liquidacion as tipoliq, liq_asocios as liqasocios, " +
							"fecha_vigencia as fecha_vigencia FROM facturacion.tarifas_iss " +
									"WHERE servicio = ? " +
									"AND esquema_tarifario = ? AND fecha_vigencia IS NULL ) ";
			}
			if(!consulta.equals(""))
			{
				pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1,codigoServicio);
				pst.setInt(2,codigoEsquemaTarifario);
				pst.setInt(3,codigoServicio);
				pst.setInt(4,codigoEsquemaTarifario);
				rs = pst.executeQuery();
				logger.info(""+consulta);
				if(rs.next())
				{	
					infoTarifa.setCodigo(rs.getInt("codigo"));
					infoTarifa.setLiquidarAsocios(UtilidadTexto.getBoolean(rs.getString("liqasocios")));
					infoTarifa.setTipoLiquidacion(rs.getInt("tipoliq"));
					infoTarifa.setValorTarifa(rs.getDouble("valor"));
					infoTarifa.setFechaVigencia((rs.getDate("fecha_vigencia")==null?"":rs.getDate("fecha_vigencia")).toString());
					infoTarifa.setExiste(true);
				}	
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerTarifaBaseServicio");
		return infoTarifa;
	}
	
	
	
	/**
	 * METODO QUE OBTIENE EL VALOR DE LA EXCEPCION DE TARIFAS X SERVICIO, 
	 * DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de eellos contiene el valor
	 * @param con (REQUERIDO)
	 * @param codigoViaIngreso (REQUERIDO)
	 * @param codigoTipoComplejidad= -1= NO DEFINIDO
	 * @param contrato (REQUERIDO)
	 * @param codigoServicio (REQUERIDO)
	 * @param fechaCalculoVigencia 
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public static InfoTarifa obtenerExcepcionesTarifasServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia, int centroAtencion ) throws BDException
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		
		try {
			//Las excepciones se toman verificando los niveles de prioridad, y estos se definen por nivel de detalle
			int codigoExcepcionGeneral= obtenerCodigoExcepcionGeneral(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoInstitucion,fechaCalculoVigencia, centroAtencion);
				
			if(codigoExcepcionGeneral<1)
			{
				//no se debe seguir buscando no existe excepciones de tarifas para el servicio mandamos el objeto vacio
				loggerInfoTarifa(excepcionInfo);
				return excepcionInfo;
			}
			
			//si llega aca entonces se debe evaluar por servicio especifico
			excepcionInfo= obtenerExcepcionServicioEspecifico(con, codigoExcepcionGeneral, codigoServicio,fechaCalculoVigencia);
			if(excepcionInfo.getExiste())
			{
				//no se debe seguir buscando existe excepciones de tarifas para el servicio mandamos el objeto 
				loggerInfoTarifa(excepcionInfo);
				return excepcionInfo;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			excepcionInfo= obtenerExcepcionServicioAgrupado(con, codigoExcepcionGeneral, codigoServicio,fechaCalculoVigencia);
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return excepcionInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoTipoComplejidad
	 * @param codigoContrato
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	public static InfoTarifa obtenerExcepcionesTarifasServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia,String fechaFinalVigencia, int centroAtencion )
	{
		//logger.info("\n\n\n******************OBTENER EXCEPCIONES TARIFARIAS*********************************");
		//Las excepciones se toman verificando los niveles de prioridad, y estos se definen por nivel de detalle
		int codigoExcepcionGeneral= obtenerCodigoExcepcionGeneral(con, codigoViaIngreso, tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoInstitucion,fechaCalculoVigencia,fechaFinalVigencia, centroAtencion);
		//logger.info("codigo General-->"+codigoExcepcionGeneral);
		
		InfoTarifa excepcionInfo= new InfoTarifa();
		if(codigoExcepcionGeneral<1)
		{
			//no se debe seguir buscando no existe excepciones de tarifas para el servicio mandamos el objeto vacio
			loggerInfoTarifa(excepcionInfo);
			return excepcionInfo;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		excepcionInfo= obtenerExcepcionServicioEspecifico(con, codigoExcepcionGeneral, codigoServicio,fechaCalculoVigencia,fechaFinalVigencia);
		if(excepcionInfo.getExiste())
		{
			//no se debe seguir buscando existe excepciones de tarifas para el servicio mandamos el objeto 
			loggerInfoTarifa(excepcionInfo);
			return excepcionInfo;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		excepcionInfo= obtenerExcepcionServicioAgrupado(con, codigoExcepcionGeneral, codigoServicio,fechaCalculoVigencia,fechaFinalVigencia);
		
		return excepcionInfo;
	}

	

	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoTipoComplejidad
	 * @param contrato
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static int obtenerCodigoExcepcionGeneral(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoInstitucion, String fechaCalculoVigencia, int centroAtencion) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoExcepcionGeneral");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			String consulta="SELECT codigo as codigo, to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia FROM facturacion.excep_tarifas_contrato " +
					"WHERE " +
					"(centro_atencion IS NULL or centro_atencion=?) AND " +
					"(via_ingreso IS NULL or via_ingreso=?) AND " +
					"(tipo_paciente IS NULL or tipo_paciente=?) AND " +
					"(tipo_complejidad IS NULL or tipo_complejidad=?) AND " +
					"codigo_contrato=? and institucion=? AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc, centro_atencion asc, via_ingreso asc, tipo_paciente asc, tipo_complejidad asc   ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, centroAtencion);
			pst.setInt(2, codigoViaIngreso);
			pst.setString(3, tipoPaciente);
			//si el codigo del tipo de complejidad es no requerido entonces llega en -1 y debe buscar todos=null
			pst.setInt(4,codigoTipoComplejidad);
			pst.setInt(5,codigoContrato);
			pst.setInt(6,codigoInstitucion);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoExcepcionGeneral");
		return valor;
	}
	
	private static int obtenerCodigoExcepcionGeneral(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoInstitucion, String fechaCalculoVigencia,String fechaFinalVigencia, int centroAtencion) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		try 
		{
			logger.info("############## Inicio obtenerCodigoExcepcionGeneral");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			String consulta="SELECT codigo as codigo, to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia FROM excep_tarifas_contrato " +
					"WHERE " +
					"(centro_atencion IS NULL or centro_atencion=?) AND " +
					"(via_ingreso IS NULL or via_ingreso=?) AND " +
					"(tipo_paciente IS NULL or tipo_paciente=?) AND " +
					"(tipo_complejidad IS NULL or tipo_complejidad=?) AND " +
					"codigo_contrato=? and institucion=? AND to_char(fecha_vigencia, 'YYYY-MM-DD')>='"+fecha+"'  and to_char(fecha_vigencia, 'YYYY-MM-DD') <='"+fechaFinalVigencia+"'  order by fecha_vigencia desc, centro_atencion asc, via_ingreso asc, tipo_paciente asc, tipo_complejidad asc ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, centroAtencion);
			pst.setInt(2, codigoViaIngreso);
			pst.setString(3, tipoPaciente);
			//si el codigo del tipo de complejidad es no requerido entonces llega en -1 y debe buscar todos=null
			pst.setInt(4,codigoTipoComplejidad);
			pst.setInt(5,codigoContrato);
			pst.setInt(6,codigoInstitucion);
			rs=pst.executeQuery();
			if(rs.next())
			{
				valor=rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigoExcepcionGeneral",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigoExcepcionGeneral", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerCodigoExcepcionGeneral");
		return valor;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionServicioEspecifico(Connection con, int codigoExcepcionGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionServicioEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			String consulta="SELECT codigo as codigo,  coalesce(valor_ajuste||'','') as valorajuste, " +
							"coalesce(nueva_tarifa||'','') as nuevatarifa, to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia " +
							"FROM ser_exce_tari_cont " +
							"WHERE codigo_excepcion=? " +
							"and codigo_servicio=? " +
							"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, Utilidades.convertirALong(codigoExcepcionGeneral+""));
			//si el codigo del tipo de complejidad es no requerido entonces llega en -1 y debe buscar todos=null
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesExcepcionServicio(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionServicioEspecifico");
		return excepcionInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionServicioEspecifico(Connection con, int codigoExcepcionGeneral, int codigoServicio, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerExcepcionServicioEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			String consulta="SELECT codigo as codigo,  coalesce(valor_ajuste||'','') as valorajuste, " +
							"coalesce(nueva_tarifa||'','') as nuevatarifa, to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia " +
							"FROM ser_exce_tari_cont " +
							"WHERE codigo_excepcion=? " +
							"and codigo_servicio=? " +
							"AND to_char(fecha_vigencia, 'YYYY-MM-DD')>='"+fecha+"'  and to_char(fecha_vigencia, 'YYYY-MM-DD') <='"+fechaFinalVigencia+"' order by fecha_vigencia desc";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, Utilidades.convertirALong(codigoExcepcionGeneral+""));
			//si el codigo del tipo de complejidad es no requerido entonces llega en -1 y debe buscar todos=null
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesExcepcionServicio(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionServicioEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionServicioEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionServicioEspecifico");
		return excepcionInfo;
	}
	/**
	 * metodo que devuelve los n porcentajes de excepcion de un servicio
	 * @param con
	 * @param codigoExcepcionServicio
	 * @return
	 */
	private static Vector<String> obtenerPorcentajesExcepcionServicio(Connection con, double codigoExcepcionServicio)
	{
		String  consulta= "SELECT porcentaje_excepcion FROM porcentaje_ser_exce WHERE ser_exce_tari_cont=? order by prioridad ";
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		Vector<String> vector= new Vector<String>();
		try 
		{
			//logger.info("obtenerPorcentajesExcepcionServicio->"+consulta+" cod->"+codigoExcepcionServicio);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoExcepcionServicio);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vector.add(rs.getDouble(1)+"");
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerCodigoExcepcionServicioEspecifico 1");
			e.printStackTrace();
		}
		return vector;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionServicioAgrupado(Connection con, int codigoExcepcionGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionServicioAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			String consulta="SELECT " +
								"codigo as codigo, " +
								"coalesce(valor_ajuste||'','') as valorajuste, " +
								"coalesce(nueva_tarifa||'','') as nuevatarifa, " +
								"to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia " +
							"from " +
								"facturacion.agru_ser_exce_tari_cont " +
							"where " +
								"codigo_excepcion=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next()){
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesGrupoExcepcionServicio(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionServicioAgrupado");
		return excepcionInfo;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionServicioAgrupado(Connection con, int codigoExcepcionGeneral, int codigoServicio, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerExcepcionServicioAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			String consulta="SELECT " +
								"codigo as codigo, " +
								"coalesce(valor_ajuste||'','') as valorajuste, " +
								"coalesce(nueva_tarifa||'','') as nuevatarifa, " +
								"to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia " +
							"from " +
								"agru_ser_exce_tari_cont " +
							"where " +
								"codigo_excepcion=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')>='"+fecha+"'  and to_char(fecha_vigencia, 'YYYY-MM-DD') <='"+fechaFinalVigencia+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next()){
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesGrupoExcepcionServicio(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionServicioAgrupado",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionServicioAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionServicioAgrupado");
		return excepcionInfo;
	}
	
	/**
	 * metodo que devuelve los n porcentajes de excepcion de un servicio
	 * @param con
	 * @param codigoExcepcionServicio
	 * @return
	 */
	private static Vector<String> obtenerPorcentajesGrupoExcepcionServicio(Connection con, double codigoExcepcionGrupoServicio)
	{
		String  consulta= "SELECT porcentaje_excepcion FROM porcentaje_agru_ser_exce WHERE agru_ser_exce_tari_cont=? order by prioridad ";
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		Vector<String> vector= new Vector<String>();
		try 
		{
			//logger.info("obtenerPorcentajesExcepcionServicio->"+consulta+" cod->"+codigoExcepcionGrupoServicio);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoExcepcionGrupoServicio);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vector.add(rs.getDouble(1)+"");
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerCodigoExcepcionServicioEspecifico 1");
			e.printStackTrace();
		}
		return vector;
	}
	
	
	
	/**
	 * 
	 * @param infoTarifa
	 */
	private static void loggerInfoTarifa(InfoTarifa infoTarifa)
	{
		logger.info("info tarifa -> existe->"+infoTarifa.getExiste()+" nueva tarifa-> "+infoTarifa.getNuevaTarifa()+" % "+infoTarifa.getPorcentajes()+" valor-> "+infoTarifa.getValor());
	}
	
	/**
	 * METODO QUE OBTIENE EL RECARGO DEL SERVICIO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param contrato
	 * @param codigoTipoSolicitud
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoTarifa obtenerRecargoServicio (Connection con, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoContrato, int codigoTipoSolicitud, int numeroSolicitud) throws BDException
	{
		InfoTarifa recargoInfo= new InfoTarifa();
		int tipoRecargo=0;
		String busquedaTipoRecargoStr="";
		if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
		{
			busquedaTipoRecargoStr="SELECT tipo_recargo as tipoRecargo from valoraciones_consulta where numero_solicitud=?";
		}
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
		{
			busquedaTipoRecargoStr="SELECT tipo_recargo as tipoRecargo from res_sol_proc where numero_solicitud=?";
		}
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion)
		{
			busquedaTipoRecargoStr="SELECT tipo_recargo as tipoRecargo from valoraciones_consulta where numero_solicitud=?";
		}
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialUrgencias)
		{
			busquedaTipoRecargoStr="SELECT tipo_recargo as tipoRecargo from valoraciones_consulta where numero_solicitud=?";
		}
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion)
		{
		    //El código dado es el número de la solicitud
			busquedaTipoRecargoStr="SELECT ev.recargo as tipoRecargo from evoluciones ev INNER JOIN solicitudes_evolucion solev ON (ev.codigo=solev.evolucion) where solev.numero_solicitud=?";
		}
		else if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios)
		{
			busquedaTipoRecargoStr="SELECT tipo_recargo as tipoRecargo from cargos_directos where numero_solicitud=?";
		}
		else
		{
			//Para el resto de casos no hay recargo
		    // en este flujo se va el caso de estancia automatica debido a que no existe recargo
			//retorna recargoInfo con el objeto totalmente vacio
			return recargoInfo;
		}
		
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerRecargoServicio");
			String calcularRecargoStr=	"SELECT " +
					"coalesce(porcentaje||'', '') as porcentaje, " +
					"coalesce(valor||'','') as valor " +
				"from " +
					"recargos_tarifas " +
				"where " +
					"contrato=? " +
					"and tipo_recargo=? " +
					"and (via_ingreso=? or via_ingreso is null) " +
					"and (tipo_paciente=? or tipo_paciente is null) " +
					"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
					"and (servicio=? or servicio is null) " +
				"order by servicio, especialidad, via_ingreso ";
			pst = con.prepareStatement(busquedaTipoRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			if (rs.next())
			{
				tipoRecargo=rs.getInt("tipoRecargo");
				pst2 = con.prepareStatement(calcularRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setInt(1, codigoContrato);
				pst2.setInt(2, tipoRecargo);
				pst2.setInt(3, codigoViaIngreso);
				pst2.setString(4, tipoPaciente);
				pst2.setInt(5, codigoServicio);
				pst2.setInt(6, codigoServicio);
				rs2=pst2.executeQuery();
				if (rs2.next()){
					recargoInfo.setExiste(true);
					Vector<String> porcentajes= new Vector<String>();
					porcentajes.add(rs2.getString("porcentaje"));
					recargoInfo.setPorcentajes(porcentajes);
					recargoInfo.setValor(rs2.getString("valor"));
				}
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs2!=null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerRecargoServicio");
		return recargoInfo;
	}
	
	/**
	 * METODO QUE OBTIENE EL DESCUENTO COMERCIAL X CONVENIO SERVICIO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		
		try {
			int codigoDescuentoComercialGeneral= obtenerCodigoDescuentoComercialGeneral(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoInstitucion,fechaCalculoVigencia);
				
			if(codigoDescuentoComercialGeneral<1)
			{
				//no se debe seguir buscando no existe descuento comercial 
				loggerInfoTarifa(descuentoInfo);
				return descuentoInfo;
			}
			
			//si llega aca entonces se debe evaluar por servicio especifico
			descuentoInfo= obtenerDescuentoComercialServicioEspecifico(con, codigoDescuentoComercialGeneral, codigoServicio,fechaCalculoVigencia);
			if(descuentoInfo.getExiste())
			{
				//no se debe seguir buscando existe descuento comercial para el servicio especifico mandamos el objeto cargado
				loggerInfoTarifa(descuentoInfo);
				return descuentoInfo;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			descuentoInfo= obtenerDescuentoComercialServicioAgrupado(con, codigoDescuentoComercialGeneral, codigoServicio,fechaCalculoVigencia);
			
			return descuentoInfo;
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoContrato
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioServicio(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia,String fechaFinalVigencia ) throws BDException
	{
		int codigoDescuentoComercialGeneral= obtenerCodigoDescuentoComercialGeneral(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoInstitucion,fechaCalculoVigencia);
		InfoTarifa descuentoInfo= new InfoTarifa();
		if(codigoDescuentoComercialGeneral<1)
		{
			//no se debe seguir buscando no existe descuento comercial 
			loggerInfoTarifa(descuentoInfo);
			return descuentoInfo;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		descuentoInfo= obtenerDescuentoComercialServicioEspecifico(con, codigoDescuentoComercialGeneral, codigoServicio,fechaCalculoVigencia,fechaFinalVigencia);
		if(descuentoInfo.getExiste())
		{
			//no se debe seguir buscando existe descuento comercial para el servicio especifico mandamos el objeto cargado
			loggerInfoTarifa(descuentoInfo);
			return descuentoInfo;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		descuentoInfo= obtenerDescuentoComercialServicioAgrupado(con, codigoDescuentoComercialGeneral, codigoServicio,fechaCalculoVigencia,fechaFinalVigencia);
		
		return descuentoInfo;
	}

	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static int obtenerCodigoDescuentoComercialGeneral(Connection con, int codigoViaIngreso, String tipoPaciente,  int codigoContrato, int codigoInstitucion, String fechaCalculoVigencia) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoDescuentoComercialGeneral");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			String consulta="SELECT codigo FROM desc_com_convcont " +
					"WHERE (via_ingreso IS NULL or via_ingreso=?) " +
					"AND (tipo_paciente IS NULL or tipo_paciente=?) " +
					"AND codigo_contrato=? AND institucion=? " +
					"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoViaIngreso);
			pst.setString(2, tipoPaciente);
			pst.setInt(3,codigoContrato);
			pst.setInt(4,codigoInstitucion);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoDescuentoComercialGeneral");
		return valor;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialServicioEspecifico(Connection con, int codigoDescuentoComercialGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerDescuentoComercialServicioEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT coalesce(porcentaje||'','') as porcentaje, coalesce(valor||'','') as valor " +
					"FROM serv_desc_com_convxcont " +
					"WHERE codigo_descuento=? and " +
					"codigo_servicio=? AND " +
					"to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' " +
					"order by fecha_vigencia desc ";
			
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerDescuentoComercialServicioEspecifico");
		return descuentoInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialServicioEspecifico(Connection con, int codigoDescuentoComercialGeneral, int codigoServicio, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerDescuentoComercialServicioEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT coalesce(porcentaje||'','') as porcentaje, coalesce(valor||'','') as valor " +
					"FROM serv_desc_com_convxcont " +
					"WHERE codigo_descuento=? and " +
					"codigo_servicio=? AND " +
					" to_char(fecha_vigencia, 'YYYY-MM-DD')>='"+fecha+"'  and to_char(fecha_vigencia, 'YYYY-MM-DD') <='"+fechaFinalVigencia+"' " +
					"order by fecha_vigencia desc ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerDescuentoComercialServicioEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerDescuentoComercialServicioEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerDescuentoComercialServicioEspecifico");
		return descuentoInfo;
	}
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialServicioAgrupado(Connection con, int codigoDescuentoComercialGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException 
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerDescuentoComercialServicioAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			String consulta="SELECT " +
								"coalesce(porcentaje||'','') as porcentaje, " +
								"coalesce(valor||'','') as valor " +
							"from " +
								"agru_ser_desc_com_convxcont " +
							"where " +
								"codigo_descuento=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerDescuentoComercialServicioAgrupado");
		return descuentoInfo;
	}

	/**
	 * 
	 */
	private static InfoTarifa obtenerDescuentoComercialServicioAgrupado(Connection con, int codigoDescuentoComercialGeneral, int codigoServicio, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerDescuentoComercialServicioAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			String consulta="SELECT " +
								"coalesce(porcentaje||'','') as porcentaje, " +
								"coalesce(valor||'','') as valor " +
							"from " +
								"agru_ser_desc_com_convxcont " +
							"where " +
								"codigo_descuento=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')>='"+fecha+"'  and to_char(fecha_vigencia, 'YYYY-MM-DD') <='"+fechaFinalVigencia+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerDescuentoComercialServicioAgrupado",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerDescuentoComercialServicioAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerDescuentoComercialServicioAgrupado");
		return descuentoInfo;
	}
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static InfoDatosString obtenerInclusionExclusionXConvenioServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException
	{
		InfoDatosString incluyeYCantidad = new InfoDatosString();
		
		try{
			//primero obtenemos lo de la excepcion
			incluyeYCantidad=obtenerExcepcionInclusionExclusionServicio(con, codigoContrato, codigoCentroCostoSolicita, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
			
			if(!incluyeYCantidad.getAcronimo().equals(""))
			{
				//no se debe seguir buscando existe en la excepcion
				return incluyeYCantidad;
			}
			//de lo contrario se busca en las inclusiones/exclusiones
			incluyeYCantidad= obtenerInclusionExclusionServicio(con, codigoContrato, codigoCentroCostoSolicita, codigoServicio, codigoInstitucion,fechaCalculoVigencia);
		return incluyeYCantidad;
	
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * metodo para obtener Excepcion InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static InfoDatosString obtenerExcepcionInclusionExclusionServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException
	{
		InfoDatosString incluyeYCantidad =  new InfoDatosString();
		
		try {
			int codigoExcepcionInclusionExclusionGeneral= obtenerCodigoExcepcionInclusionExclusionGeneral(con,codigoContrato, codigoInstitucion, codigoCentroCostoSolicita,fechaCalculoVigencia);
			if(codigoExcepcionInclusionExclusionGeneral<1){
				//no se debe seguir buscando no existe  
				return incluyeYCantidad;
			}
			//si llega aca entonces se debe evaluar por servicio especifico
			incluyeYCantidad= obtenerExcepcionInclusionExclusionServicioEspecifico(con, codigoExcepcionInclusionExclusionGeneral, codigoServicio,fechaCalculoVigencia);
			if(!incluyeYCantidad.getAcronimo().equals("")){
				//no se debe seguir buscando existe excepciones para el servicio mandamos el String cargado
				return incluyeYCantidad;
			}
			//de lo contrario debe seguir buscando en los grupos
			incluyeYCantidad= obtenerExcepcionInclusionExclusionServicioAgrupado(con, codigoExcepcionInclusionExclusionGeneral, codigoServicio,fechaCalculoVigencia);
			return incluyeYCantidad;
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param codigoInstitucion
	 * @param codigoCentroCostoSolicita
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static int obtenerCodigoExcepcionInclusionExclusionGeneral(Connection con, int codigoContrato, int codigoInstitucion, int codigoCentroCostoSolicita, String fechaCalculoVigencia) throws BDException
	{
		//para darle prioridad a los not null entonces hacemos un order by por el centro costo
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoExcepcionInclusionExclusionGeneral");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			String consulta="SELECT codigo FROM excep_incluexclu_contr " +
					"WHERE codigo_contrato=? and institucion=? " +
					"and (codigo_centro_costo is null or codigo_centro_costo=?) AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
			consulta+=" ORDER BY fecha_vigencia desc, codigo_centro_costo ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoContrato);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,codigoCentroCostoSolicita);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigo");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoExcepcionInclusionExclusionGeneral");
		return valor;
	}
	
	/**
	 * metodo para obtener Excepcion InclusionExclusion Servicio Especifico, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoExcepcionInclusionExclusionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerExcepcionInclusionExclusionServicioEspecifico(Connection con, int codigoExcepcionInclusionExclusionGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException 
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionInclusionExclusionServicioEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT incluye as incluye, CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad FROM ser_incluexclu_econt " +
					"WHERE codigo_excepcion=? AND codigo_servicio=? AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionInclusionExclusionGeneral);
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionInclusionExclusionServicioEspecifico");
		return incluyeYCantidad;
	}
	
	/**
	 * metodo para obtener Excepcion InclusionExclusion Servicio agrupado, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoExcepcionInclusionExclusionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerExcepcionInclusionExclusionServicioAgrupado(Connection con, int codigoExcepcionInclusionExclusionGeneral, int codigoServicio, String fechaCalculoVigencia) throws BDException  
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;

		try {
			Log4JManager.info("############## Inicio obtenerExcepcionInclusionExclusionServicioAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			String consulta="SELECT " +
								"incluye as incluye, " +
								"CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad " +
							"from " +
								"agru_ser_incluexclu_econt " +
							"where " +
								"codigo_excepcion=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionInclusionExclusionGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionInclusionExclusionServicioAgrupado");
		return incluyeYCantidad;
	}

	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static InfoDatosString obtenerInclusionExclusionServicio(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoServicio, int codigoInstitucion, String fechaCalculoVigencia) 
	{
		//logger.info("**********************OBTENER INCLUSIONES EXCLUSIONES (NO EXISTE EXCEPCION)***************************");
		InfoDatosString incluyeYCantidad  = new InfoDatosString();
		Vector codigosInclusionExclusionGeneralCC= obtenerCodigosInclusionesExclusionesGeneralCC(con,codigoContrato, codigoInstitucion, codigoCentroCostoSolicita,fechaCalculoVigencia);
		
		if(codigosInclusionExclusionGeneralCC.size()<1)
		{
			//no se debe seguir buscando no existe  
			logger.info("->NO EXISTE INCLUSIONES EXCLUSIONES!!!!");
			return incluyeYCantidad;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico e iterando los codigos de inclusiones qu vienen ordenados por prioridad
		for(int w=0; w<codigosInclusionExclusionGeneralCC.size();w++)
		{
			incluyeYCantidad= obtenerInclusionExclusionServicioEspecifico(con, Integer.parseInt(codigosInclusionExclusionGeneralCC.get(w).toString()), codigoServicio);
			//si es diferente de vacio deja esa y debe salir del for
			if(!incluyeYCantidad.getAcronimo().equals(""))
			{
				//no se debe seguir buscando existe para el servicio mandamos el String cargado
				logger.info("Existe inclusiones esclusione por servicio especifico!!!! INCLUYE?->"+incluyeYCantidad.getAcronimo());
				return incluyeYCantidad;
			}
		}
		
		//de lo contrario debe seguir buscando en los grupos
		for(int w=0; w<codigosInclusionExclusionGeneralCC.size();w++)
		{
			incluyeYCantidad= obtenerInclusionExclusionServicioAgrupado(con, Integer.parseInt(codigosInclusionExclusionGeneralCC.get(w).toString()), codigoServicio);
			//si es diferente de vacio deja esa y debe salir del for
			if(!incluyeYCantidad.getAcronimo().equals(""))
			{
				//no se debe seguir buscando existe para el servicio mandamos el String cargado
				logger.info("Existe inclusiones esclusione por servicio especifico!!!! INCLUYE?->"+incluyeYCantidad.getAcronimo());
				return incluyeYCantidad;
			}
		}	
		
		return incluyeYCantidad;
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param codigoInstitucion
	 * @param codigoCentroCostoSolicita
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Vector<String> obtenerCodigosInclusionesExclusionesGeneralCC(Connection con, int codigoContrato, int codigoInstitucion, int codigoCentroCostoSolicita, String fechaCalculoVigencia) 
	{
		Vector codigos= new Vector<String>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerCodigosInclusionesExclusionesGeneralCC");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			String consulta="SELECT " +
								"iecc.codigo as codigo " +
							"FROM " +
								"inclu_exclu_cc iecc " +
								"inner join incluexclu_contrato iecon on (iecon.codigo_inclu_exclu_cc=iecc.codigo and iecon.institucion=iecc.institucion and iecon.codigo_contrato=?)  " +
							"WHERE  iecc.institucion=? and (iecc.codigo_centro_costo is null or iecc.codigo_centro_costo=?) AND to_char(iecon.fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' " +
							"order by iecon.fecha_vigencia desc, iecc.codigo_centro_costo, iecon.prioridad ";
			//el order by me organiza todo para evaluarlo por prioridad.
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoContrato);
			pst.setInt(2,codigoInstitucion);
			pst.setInt(3,codigoCentroCostoSolicita);
			rs=pst.executeQuery();
			while(rs.next())
			{
				codigos.add(rs.getInt("codigo")+"");
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerCodigosInclusionesExclusionesGeneralCC",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCodigosInclusionesExclusionesGeneralCC", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerCodigosInclusionesExclusionesGeneralCC");
		return codigos;
	}

	
	/**
	 * metodo para obtener InclusionExclusion Servicio Especifico, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoInclusionExclusionGeneral
	 * @param codigoServicio
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerInclusionExclusionServicioEspecifico(Connection con, int codigoInclusionExclusionGeneral, int codigoServicio) 
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerInclusionExclusionServicioEspecifico");
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT incluye as incluye, CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad FROM ser_incluexclu_cc WHERE codigo_inclu_exclu_cc=? AND codigo_servicio=?";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoInclusionExclusionGeneral+"");
			pst.setInt(2, codigoServicio);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerInclusionExclusionServicioEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerInclusionExclusionServicioEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerInclusionExclusionServicioEspecifico");
		return incluyeYCantidad;
	}
	
	/**
	 * metodo para obtener InclusionExclusion Servicio agrupado, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoInclusionExclusionGeneral
	 * @param codigoServicio
	 * @return
	 */
	private static InfoDatosString obtenerInclusionExclusionServicioAgrupado(Connection con, int codigoInclusionExclusionGeneral, int codigoServicio) 
	{
		InfoDatosString incluyeYCantidad = null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		String consulta = "";
		try 
		{
			logger.info("############## Inicio obtenerInclusionExclusionServicioAgrupado");
			//la prioridad en este punto va primero por la especialidad, luego x tipo servicio, luego grupo servicio, luego tipo pos
			consulta="SELECT " +
								"incluye as incluye, " +
								"CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad " +
							"from " +
								"agru_ser_incluexclu_cc " +
							"where " +
								"codigo_inclu_exclu_cc=? " +
								"and (especialidad=(select s.especialidad from servicios s where s.codigo=?) or especialidad is null) " +
								"and (tipo_servicio=(select s.tipo_servicio from servicios s where s.codigo=?) or tipo_servicio is null) " +
								"and (grupo_servicio=(select s.grupo_servicio from servicios s where s.codigo=?) or grupo_servicio is null) "+
								"and (pos=(select case when s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end from servicios s where s.codigo=?) or pos is null) ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY especialidad, tipo_servicio, grupo_servicio, pos ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoInclusionExclusionGeneral);
			pst.setInt(2, codigoServicio);
			pst.setInt(3, codigoServicio);
			pst.setInt(4, codigoServicio);
			pst.setInt(5, codigoServicio);
			
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerInclusionExclusionServicioAgrupado" + "\n" + consulta + "\n" + codigoInclusionExclusionGeneral + "\n" + codigoServicio, sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerInclusionExclusionServicioAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerInclusionExclusionServicioAgrupado");
		return incluyeYCantidad;
	}
	
	
	
	
	//***************************************************FIN SERVICIOS ************************************************************************************/
	
	
	//****************************************************ARTICULOS***************************************************************************************/
	
	/**
	 * metodo para obtener la tarifa base de un articulo 
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static double obtenerTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		double valor=-1;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerTarifaBaseArticulo");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			//logger.info("**********************TARIFA BASE articulos***************************");
			String consulta=	"(SELECT valor_tarifa AS valor from facturacion.view_tarifas_inv where articulo=? and esquema_tarifario=? AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"')" +
									"UNION ALL " +
								"(SELECT valor_tarifa AS valor from tarifas_inventario where articulo=? and esquema_tarifario=? AND fecha_vigencia IS NULL) " ;
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoArticulo);
			pst.setInt(2,codigoEsquemaTarifario);
			pst.setInt(3,codigoArticulo);
			pst.setInt(4,codigoEsquemaTarifario);
			rs= pst.executeQuery();
			if(rs.next()){
				valor=rs.getDouble("valor");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerTarifaBaseArticulo");
		return valor;
	}
	
	/**
	 * metodo para obtener el TIPO de tarifa base de un articulo
	 * @param con
	 * @param codigoArticulo
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static String obtenerTipoTarifaBaseArticulo(Connection con, int codigoArticulo, int codigoEsquemaTarifario, String fechaCalculoVigencia) throws BDException
	{
		String tipoTarifa="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerTipoTarifaBaseArticulo");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			
			String consulta=	"(SELECT tipo_tarifa AS tipotarifa from facturacion.view_tarifas_inv where articulo=? and esquema_tarifario=? AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"') " +
									"UNION ALL " +
								"(SELECT tipo_tarifa AS tipotarifa from tarifas_inventario where articulo=? and esquema_tarifario=? AND fecha_vigencia IS NULL) " ;
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoArticulo);
			pst.setInt(2,codigoEsquemaTarifario);
			pst.setInt(3,codigoArticulo);
			pst.setInt(4,codigoEsquemaTarifario);
			rs= pst.executeQuery();
			if(rs.next()){
				tipoTarifa=rs.getString("tipotarifa");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerTipoTarifaBaseArticulo");
		return tipoTarifa;
	}
	
	/**
	 * METODO QUE OBTIENE EL VALOR DE LA EXCEPCION DE TARIFAS X Articulo, 
	 * DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de eellos contiene el valor
	 * @param con (REQUERIDO)
	 * @param codigoViaIngreso (REQUERIDO)
	 * @param codigoTipoComplejidad= -1= NO DEFINIDO
	 * @param contrato (REQUERIDO)
	 * @param codigoArticulo (REQUERIDO)
	 * @param fechaCalculoVigencia 
	 * @return DEVUELVE UN OBJETO <-InfoTarifa-> 
	 */
	public static InfoTarifa obtenerExcepcionesTarifasArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario, String fechaCalculoVigencia, int centroAtencion ) throws BDException
	{
		try{
			//Las excepciones se toman verificando los niveles de prioridad, y estos se definen por nivel de detalle
			int codigoExcepcionGeneral= obtenerCodigoExcepcionGeneral(con, codigoViaIngreso,tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoInstitucion,fechaCalculoVigencia, centroAtencion);
			
			InfoTarifa excepcionInfo= new InfoTarifa();
			if(codigoExcepcionGeneral<1){
				//no se debe seguir buscando no existe excepciones de tarifas para el articulo mandamos el objeto vacio
				return excepcionInfo;
			}
			
			String baseTarifa= obtenerTipoTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
			
			//Se evalua si es requerido la base de tarifa (baseExcepcion) DCU-412 Excepciones de Tarifas
			boolean requiereBaseExcepcion=obtenerBaseExcepcion(con,codigoExcepcionGeneral,baseTarifa);
			
			//si llega aca entonces se debe evaluar por articulo especifico
			excepcionInfo= obtenerExcepcionArticuloEspecifico(con, codigoExcepcionGeneral, codigoArticulo, baseTarifa,fechaCalculoVigencia, requiereBaseExcepcion);
			if(excepcionInfo.getExiste()){
				//no se debe seguir buscando existe excepciones de tarifas para el articulo mandamos el objeto 
				return excepcionInfo;
			}
			//de lo contrario debe seguir buscando en los grupos
			excepcionInfo= obtenerExcepcionArticuloAgrupado(con, codigoExcepcionGeneral, codigoArticulo, baseTarifa,fechaCalculoVigencia, requiereBaseExcepcion);
			return excepcionInfo;
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoTipoComplejidad
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param codigoEsquemaTarifario
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	public static InfoTarifa obtenerExcepcionesTarifasArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoTipoComplejidad, int codigoContrato, int codigoArticulo, int codigoInstitucion, int codigoEsquemaTarifario, String fechaCalculoVigencia,String fechaFinalVigencia, int centroAtencion ) throws BDException
	{
		//Las excepciones se toman verificando los niveles de prioridad, y estos se definen por nivel de detalle
		int codigoExcepcionGeneral= obtenerCodigoExcepcionGeneral(con, codigoViaIngreso,tipoPaciente, codigoTipoComplejidad, codigoContrato, codigoInstitucion,fechaCalculoVigencia,fechaFinalVigencia, centroAtencion);
		InfoTarifa excepcionInfo= new InfoTarifa();
		if(codigoExcepcionGeneral<1){
			//no se debe seguir buscando no existe excepciones de tarifas para el articulo mandamos el objeto vacio
			return excepcionInfo;
		}
		String baseTarifa= obtenerTipoTarifaBaseArticulo(con, codigoArticulo, codigoEsquemaTarifario, fechaCalculoVigencia);
		
		//Se evalua si es requerido la base de tarifa (baseExcepcion) DCU-412 Excepciones de Tarifas
		boolean requiereBaseExcepcion=obtenerBaseExcepcion(con,codigoExcepcionGeneral,baseTarifa);
		
		//si llega aca entonces se debe evaluar por articulo especifico
		excepcionInfo= obtenerExcepcionArticuloEspecifico(con, codigoExcepcionGeneral, codigoArticulo, baseTarifa,fechaCalculoVigencia,fechaFinalVigencia, requiereBaseExcepcion);
		if(excepcionInfo.getExiste()){
			//no se debe seguir buscando existe excepciones de tarifas para el articulo mandamos el objeto 
			return excepcionInfo;
		}
		//de lo contrario debe seguir buscando en los grupos
		excepcionInfo= obtenerExcepcionArticuloAgrupado(con, codigoExcepcionGeneral, codigoArticulo, baseTarifa,fechaCalculoVigencia,fechaFinalVigencia, requiereBaseExcepcion);
		return excepcionInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionArticuloEspecifico(Connection con, int codigoExcepcionGeneral, int codigoArticulo, String baseExcepcion, String fechaCalculoVigencia,boolean requiereBaseExcepcion) throws BDException
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionArticuloEspecifico");
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));

			
			if(baseExcepcion.equals(ConstantesIntegridadDominio.acronimoValorFijo))
				baseExcepcion = ConstantesIntegridadDominio.acronimoTarifa;
			
			String consulta="SELECT codigo as codigo, coalesce(valor_ajuste||'','') as valorajuste, coalesce(nueva_tarifa||'','') as nuevatarifa, " +
								"to_char(fecha_vigencia, 'YYYY-MM-DD') AS fecha_vigencia " +
								"FROM art_exce_tari_cont " +
								"WHERE codigo_excepcion=? " +
								"and codigo_articulo=? ";
			if(requiereBaseExcepcion){
				consulta +="and base_excepcion=? ";
			}
			consulta +="AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc ";
			

			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoArticulo);
			if(requiereBaseExcepcion){
				pst.setString(3, baseExcepcion);
			}
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesExcepcionArticulo(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionArticuloEspecifico");
		return excepcionInfo;
	}
	
	/**
	 * metodo que devuelve los n porcentajes de excepcion de un servicio
	 * @param con
	 * @param codigoExcepcionServicio
	 * @return
	 */
	private static Vector<String> obtenerPorcentajesExcepcionArticulo(Connection con, double codigoExcepcionArticulo)
	{
		String  consulta= "SELECT porcentaje_excepcion FROM porcentaje_art_exce WHERE art_exce_tari_cont=? order by prioridad ";
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		Vector<String> vector= new Vector<String>();
		try 
		{
			//logger.info("obtenerPorcentajesExcepcionArticulo->"+consulta+" cod->"+codigoExcepcionArticulo);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoExcepcionArticulo);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vector.add(rs.getDouble(1)+"");
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerPorcentajesExcepcionArticulo 1");
			e.printStackTrace();
		}
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionArticuloAgrupado(Connection con, int codigoExcepcionGeneral, int codigoArticulo, String baseExcepcion, String fechaCalculoVigencia,boolean requiereBaseExcepcion) throws BDException
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionArticuloAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));

			if(baseExcepcion.equals(ConstantesIntegridadDominio.acronimoValorFijo)){
				baseExcepcion = ConstantesIntegridadDominio.acronimoTarifa;
			}
			
			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			String consulta="SELECT " +
								"codigo as codigo, " +
								"coalesce(valor_ajuste||'','') as valorajuste, " +
								"coalesce(nueva_tarifa||'','') as nuevatarifa " +
							"from " +
								"agru_art_exce_tari_cont " +
							"where " +
								"codigo_excepcion=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) ";
			if(requiereBaseExcepcion){
				consulta += "and base_excepcion=? ";
			}	
			consulta +=	"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, subgrupo, grupo, clase, naturaleza ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			if(requiereBaseExcepcion){
				pst.setString(6, baseExcepcion);
			}
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesGrupoExcepcionArticulo(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionArticuloAgrupado");
		return excepcionInfo;
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoArticulo
	 * @param baseExcepcion
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionArticuloAgrupado(Connection con, int codigoExcepcionGeneral, int codigoArticulo, String baseExcepcion, String fechaCalculoVigencia,String fechaFinalVigencia, boolean requiereBaseExcepcion) 
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerExcepcionArticuloAgrupado");
			if(baseExcepcion.equals(ConstantesIntegridadDominio.acronimoValorFijo)){
				baseExcepcion = ConstantesIntegridadDominio.acronimoTarifa;
			}
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));

			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			String consulta="SELECT " +
								"codigo as codigo, " +
								"coalesce(valor_ajuste||'','') as valorajuste, " +
								"coalesce(nueva_tarifa||'','') as nuevatarifa " +
							"from " +
								"agru_art_exce_tari_cont " +
							"where " +
								"codigo_excepcion=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) ";
			if(requiereBaseExcepcion){
				consulta +="and base_excepcion=? ";
			}
			consulta+="AND to_char(fecha_vigencia, 'YYYY-MM-DD') between '"+fecha+"' and '"+fechaFinalVigencia+"' ";
			
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, subgrupo, grupo, clase, naturaleza ";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			if(requiereBaseExcepcion){
				pst.setString(6, baseExcepcion);
			}
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesGrupoExcepcionArticulo(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionArticuloAgrupado",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionArticuloAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionArticuloAgrupado");
		return excepcionInfo;
	}
	
	/**
	 * metodo que devuelve los n porcentajes de excepcion de un servicio
	 * @param con
	 * @param codigoExcepcionServicio
	 * @return
	 */
	private static Vector<String> obtenerPorcentajesGrupoExcepcionArticulo(Connection con, double codigoExcepcionGrupoArticulo)
	{
		String  consulta= "SELECT porcentaje_excepcion FROM porcentaje_agru_art_exce WHERE agru_art_exce_tari_cont=? order by prioridad ";
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		Vector<String> vector= new Vector<String>();
		try 
		{
			//logger.info("obtenerPorcentajesGrupoExcepcionArticulo->"+consulta+" cod->"+codigoExcepcionGrupoArticulo);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoExcepcionGrupoArticulo);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vector.add(rs.getDouble(1)+"");
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN obtenerPorcentajesGrupoExcepcionArticulo 1");
			e.printStackTrace();
		}
		return vector;
	}
	
	
	
	/**
	 * METODO QUE OBTIENE EL DESCUENTO COMERCIAL X CONVENIO ARTICULO, DEVUELVE UN OBJETO <-InfoTarifa-> que contiene los atributos 
	 * <-nueva tarifa, %, valor, existe-> PARA ESTE CASO NO APLICA nueva tarifa, 
	 * si existe es false entonces todos los valores son vacios 
	 * de lo contrario uno de ellos contiene el valor 
	 * @param con
	 * @param codigoViaIngreso
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException
	{
		try{
			//logger.info("**********************OBTENER DESCUENTOS COMERCIALES articulos***************************");
			int codigoDescuentoComercialGeneral= obtenerCodigoDescuentoComercialGeneral(con, codigoViaIngreso, tipoPaciente, codigoContrato, codigoInstitucion,fechaCalculoVigencia);
			InfoTarifa descuentoInfo= new InfoTarifa();
			if(codigoDescuentoComercialGeneral<1)
			{
				//no se debe seguir buscando no existe descuento comercial 
				loggerInfoTarifa(descuentoInfo);
				return descuentoInfo;
			}
			
			//si llega aca entonces se debe evaluar por ARTICULO especifico
			descuentoInfo= obtenerDescuentoComercialArticuloEspecifico(con, codigoDescuentoComercialGeneral, codigoArticulo,fechaCalculoVigencia);
			if(descuentoInfo.getExiste())
			{
				//no se debe seguir buscando existe descuento comercial para el articulo especifico mandamos el objeto cargado
				loggerInfoTarifa(descuentoInfo);
				return descuentoInfo;
			}
			
			//de lo contrario debe seguir buscando en los grupos
			descuentoInfo= obtenerDescuentoComercialArticuloAgrupado(con, codigoDescuentoComercialGeneral, codigoArticulo,fechaCalculoVigencia);
			
			return descuentoInfo;
		}
		catch (BDException bde) {
			throw bde;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoContrato
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia
	 * @param fechaFinalVigencia
	 * @return
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioArticulo(Connection con, int codigoViaIngreso, String tipoPaciente, int codigoContrato, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia,String fechaFinalVigencia ) throws BDException
	{
		//logger.info("**********************OBTENER DESCUENTOS COMERCIALES articulos***************************");
		int codigoDescuentoComercialGeneral= obtenerCodigoDescuentoComercialGeneral(con, codigoViaIngreso, tipoPaciente, codigoContrato, codigoInstitucion,fechaCalculoVigencia);
		InfoTarifa descuentoInfo= new InfoTarifa();
		if(codigoDescuentoComercialGeneral<1)
		{
			//no se debe seguir buscando no existe descuento comercial 
			loggerInfoTarifa(descuentoInfo);
			return descuentoInfo;
		}
		
		//si llega aca entonces se debe evaluar por ARTICULO especifico
		descuentoInfo= obtenerDescuentoComercialArticuloEspecifico(con, codigoDescuentoComercialGeneral, codigoArticulo,fechaCalculoVigencia,fechaFinalVigencia);
		if(descuentoInfo.getExiste())
		{
			//no se debe seguir buscando existe descuento comercial para el articulo especifico mandamos el objeto cargado
			loggerInfoTarifa(descuentoInfo);
			return descuentoInfo;
		}
		
		//de lo contrario debe seguir buscando en los grupos
		descuentoInfo= obtenerDescuentoComercialArticuloAgrupado(con, codigoDescuentoComercialGeneral, codigoArticulo,fechaCalculoVigencia,fechaFinalVigencia);
		
		return descuentoInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialArticuloEspecifico(Connection con, int codigoDescuentoComercialGeneral, int codigoArticulo, String fechaCalculoVigencia) throws BDException
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerDescuentoComercialArticuloEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT coalesce(porcentaje||'','') as porcentaje, coalesce(valor||'','') as valor " +
					"FROM art_desc_com_convxcont " +
					"WHERE codigo_descuento=? " +
					"and codigo_articulo=? " +
					"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' " +
					"order by fecha_vigencia desc";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerDescuentoComercialArticuloEspecifico");
		return descuentoInfo;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialArticuloEspecifico(Connection con, int codigoDescuentoComercialGeneral, int codigoArticulo, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerDescuentoComercialArticuloEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT coalesce(porcentaje||'','') as porcentaje, coalesce(valor||'','') as valor " +
					"FROM art_desc_com_convxcont " +
					"WHERE codigo_descuento=? " +
					"and codigo_articulo=? " +
					"AND to_char(fecha_vigencia, 'YYYY-MM-DD') between '"+fecha+"' and '"+fechaFinalVigencia+"'" +
					"order by fecha_vigencia desc";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerDescuentoComercialArticuloEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerDescuentoComercialArticuloEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerDescuentoComercialArticuloEspecifico");
		return descuentoInfo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoExcepcionGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @param fechaFinalVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerExcepcionArticuloEspecifico(Connection con, int codigoExcepcionGeneral, int codigoArticulo, String baseExcepcion, String fechaCalculoVigencia, String fechaFinalVigencia, boolean requiereBaseExcepcion) 
	{
		InfoTarifa excepcionInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Fin obtenerExcepcionArticuloEspecifico");
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso o tipo complejidad
			
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));

			
			if(baseExcepcion.equals(ConstantesIntegridadDominio.acronimoValorFijo))
				baseExcepcion = ConstantesIntegridadDominio.acronimoTarifa;
			
			String consulta="SELECT codigo as codigo, coalesce(valor_ajuste||'','') as valorajuste, coalesce(nueva_tarifa||'','') as nuevatarifa, " +
								"to_char(fecha_vigencia, 'YYYY-MM-DD') AS fecha_vigencia " +
								"FROM art_exce_tari_cont " +
								"WHERE codigo_excepcion=? " +
								"and codigo_articulo=? ";
			if(requiereBaseExcepcion){					
				consulta+="and base_excepcion=? ";
			}
			consulta+="AND to_char(fecha_vigencia, 'YYYY-MM-DD') between '"+fecha+"' and '"+fechaFinalVigencia+"' order by fecha_vigencia desc ";
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionGeneral);
			pst.setInt(2, codigoArticulo);
			if(requiereBaseExcepcion){
				pst.setString(3, baseExcepcion);
			}
			rs=pst.executeQuery();
			if(rs.next())
			{
				excepcionInfo.setExiste(true);
				excepcionInfo.setNuevaTarifa(rs.getString("nuevatarifa"));
				excepcionInfo.setValor(rs.getString("valorajuste"));
				excepcionInfo.setPorcentajes(obtenerPorcentajesExcepcionArticulo(con, rs.getDouble("codigo")));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerExcepcionArticuloEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerExcepcionArticuloEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerExcepcionArticuloEspecifico");
		return excepcionInfo;
	}
	/**
	 * 
	 * @param con
	 * @param codigoDescuentoComercialGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoTarifa obtenerDescuentoComercialArticuloAgrupado(Connection con, int codigoDescuentoComercialGeneral, int codigoArticulo, String fechaCalculoVigencia) throws BDException 
	{
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerDescuentoComercialArticuloAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			String consulta="SELECT " +
								"coalesce(porcentaje||'','') as porcentaje, " +
								"coalesce(valor||'','') as valor " +
							"from " +
								" agrup_art_desc_com_convcont " +
							"where " +
								"codigo_descuento=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
								
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, subgrupo, grupo, clase, naturaleza ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerDescuentoComercialArticuloAgrupado");
		return descuentoInfo;
	}
	
	
	
	private static InfoTarifa obtenerDescuentoComercialArticuloAgrupado(Connection con, int codigoDescuentoComercialGeneral, int codigoArticulo, String fechaCalculoVigencia,String fechaFinalVigencia) 
	{
		
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerDescuentoComercialArticuloAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			String consulta="SELECT " +
								"coalesce(porcentaje||'','') as porcentaje, " +
								"coalesce(valor||'','') as valor " +
							"from " +
								" agrup_art_desc_com_convcont " +
							"where " +
								"codigo_descuento=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD') between'"+fecha+"' and '"+fechaFinalVigencia+"' ";
								
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, subgrupo, grupo, clase, naturaleza ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoDescuentoComercialGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				descuentoInfo.setValor(rs.getString("valor"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerDescuentoComercialArticuloAgrupado",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerDescuentoComercialArticuloAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerDescuentoComercialArticuloAgrupado");
		return descuentoInfo;
	}
	
	
	/**
	 * metodo para obtener InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	public static InfoDatosString obtenerInclusionExclusionXConvenioArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia ) throws BDException
	{
		InfoDatosString incluyeYCantidad = new InfoDatosString();
		//La filosofia de inclusiones /exclusiones es primero buscar las EXCEPCIONES de esta y luego buscar las inclusiones/exclusiones
		//logger.info("**********************OBTENER INCLUSIONES EXCLUSIONES ARTICULOS***************************");
		//primero obtenemos lo de la excepcion
		incluyeYCantidad=obtenerExcepcionInclusionExclusionArticulo(con, codigoContrato, codigoCentroCostoSolicita, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
		
		if(!incluyeYCantidad.getAcronimo().equals(""))
		{
			//no se debe seguir buscando existe en la excepcion 
			return incluyeYCantidad;
		}
		//de lo contrario se busca en las inclusiones/exclusiones
		//logger.info("no existia en las excpciones por lo tanto se busca en las inclusiones/exclusiones");
		incluyeYCantidad = obtenerInclusionExclusionArticulo(con, codigoContrato, codigoCentroCostoSolicita, codigoArticulo, codigoInstitucion,fechaCalculoVigencia);
		return incluyeYCantidad;
	}

	/**
	 * metodo para obtener Excepcion InclusionExclusion , devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerExcepcionInclusionExclusionArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia) throws BDException 
	{
		InfoDatosString incluyeYCantidad =  new InfoDatosString();
		
		try {
			int codigoExcepcionInclusionExclusionGeneral= obtenerCodigoExcepcionInclusionExclusionGeneral(con,codigoContrato, codigoInstitucion, codigoCentroCostoSolicita,fechaCalculoVigencia);
			if(codigoExcepcionInclusionExclusionGeneral<1){
				//no se debe seguir buscando no existe  
				return incluyeYCantidad;
			}
			//si llega aca entonces se debe evaluar por articulo especifico
			incluyeYCantidad= obtenerExcepcionInclusionExclusionArticuloEspecifico(con, codigoExcepcionInclusionExclusionGeneral, codigoArticulo,fechaCalculoVigencia);
			if(!incluyeYCantidad.getAcronimo().equals("")){
				//no se debe seguir buscando existe excepciones para el articulo mandamos el String cargado
				return incluyeYCantidad;
			}
			//de lo contrario debe seguir buscando en los grupos
			incluyeYCantidad = obtenerExcepcionInclusionExclusionArticuloAgrupado(con, codigoExcepcionInclusionExclusionGeneral, codigoArticulo,fechaCalculoVigencia);
			return incluyeYCantidad;
		}
		catch (BDException bde) {
			throw bde;
		}	
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	
	}
	
	
	/**
	 * metodo para obtener Excepcion InclusionExclusion Articulo Especifico, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoExcepcionInclusionExclusionGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerExcepcionInclusionExclusionArticuloEspecifico(Connection con, int codigoExcepcionInclusionExclusionGeneral, int codigoArticulo, String fechaCalculoVigencia) throws BDException 
	{
		InfoDatosString  incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionInclusionExclusionArticuloEspecifico");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT incluye as incluye , CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad " +
					"FROM art_incluexclu_econt " +
					"WHERE codigo_excepcion=? " +
					"AND codigo_articulo=? " +
					"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' order by fecha_vigencia desc";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionInclusionExclusionGeneral);
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();

			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionInclusionExclusionArticuloEspecifico");
		return incluyeYCantidad;
	}
	
	/**
	 * metodo para obtener Excepcion InclusionExclusion Articulo agrupado, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoExcepcionInclusionExclusionGeneral
	 * @param codigoArticulo
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	private static InfoDatosString obtenerExcepcionInclusionExclusionArticuloAgrupado(Connection con, int codigoExcepcionInclusionExclusionGeneral, int codigoArticulo, String fechaCalculoVigencia) throws BDException 
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio obtenerExcepcionInclusionExclusionArticuloAgrupado");
			String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			String consulta="SELECT " +
								"incluye as incluye, CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad " +
							"from " +
								"agru_art_incluexclu_econt " +
							"where " +
								"codigo_excepcion=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) " +
								"AND to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' ";
								
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY fecha_vigencia desc, subgrupo, grupo, clase, naturaleza ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcionInclusionExclusionGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerExcepcionInclusionExclusionArticuloAgrupado");
		return incluyeYCantidad;
	}
	
	/**
	 * metodo para obtener InclusionExclusion Articulo, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoContrato
	 * @param codigoCentroCostoSolicita
	 * @param codigoArticulo
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static InfoDatosString obtenerInclusionExclusionArticulo(Connection con, int codigoContrato, int codigoCentroCostoSolicita, int codigoArticulo, int codigoInstitucion, String fechaCalculoVigencia) 
	{
		InfoDatosString incluyeYCantidad = new InfoDatosString();
		
		Vector codigosInclusionExclusionGeneralCC= obtenerCodigosInclusionesExclusionesGeneralCC(con,codigoContrato, codigoInstitucion, codigoCentroCostoSolicita,fechaCalculoVigencia);
		if(codigosInclusionExclusionGeneralCC.size()<1){
			//no se debe seguir buscando no existe  
			return incluyeYCantidad;
		}
		//si llega aca entonces se debe evaluar por articulo especifico e iterando los codigos de inclusiones qu vienen ordenados por prioridad
		for(int w=0; w<codigosInclusionExclusionGeneralCC.size();w++)
		{
			incluyeYCantidad = obtenerInclusionExclusionArticuloEspecifico(con, Integer.parseInt(codigosInclusionExclusionGeneralCC.get(w).toString()), codigoArticulo);
			//si es diferente de vacio deja esa y debe salir del for
			if(!incluyeYCantidad.getAcronimo().equals("")){
				//no se debe seguir buscando existe para el articulo mandamos el String cargado
				return incluyeYCantidad;
			}
		}
		//de lo contrario debe seguir buscando en los grupos
		for(int w=0; w<codigosInclusionExclusionGeneralCC.size();w++)
		{
			incluyeYCantidad = obtenerInclusionExclusionArticuloAgrupado(con, Integer.parseInt(codigosInclusionExclusionGeneralCC.get(w).toString()), codigoArticulo);
			//si es diferente de vacio deja esa y debe salir del for
			if(!incluyeYCantidad.getAcronimo().equals("")){
				//no se debe seguir buscando existe para el articulo mandamos el String cargado
				return incluyeYCantidad;
			}
		}	
		return incluyeYCantidad;
	}
	
	
	/**
	 * metodo para obtener InclusionExclusion Articulo Especifico, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoInclusionExclusionGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoDatosString obtenerInclusionExclusionArticuloEspecifico(Connection con, int codigoInclusionExclusionGeneral, int codigoArticulo) 
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			logger.info("############## Inicio obtenerInclusionExclusionArticuloEspecifico");
			//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
			String consulta="SELECT incluye as incluye, CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad FROM art_incluexclu_cc WHERE codigo_inclu_exclu_cc=? AND codigo_articulo=?";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoInclusionExclusionGeneral);
			pst.setInt(2, codigoArticulo);
			rs=pst.executeQuery();
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerInclusionExclusionArticuloEspecifico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerInclusionExclusionArticuloEspecifico", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerInclusionExclusionArticuloEspecifico");
		return incluyeYCantidad;
	}
	
	/**
	 * metodo para obtener InclusionExclusion Articulo agrupado, devuelve un string con 'N' 'S' o ''->no existe
	 * @param con
	 * @param codigoInclusionExclusionGeneral
	 * @param codigoArticulo
	 * @return
	 */
	private static InfoDatosString obtenerInclusionExclusionArticuloAgrupado(Connection con, int codigoInclusionExclusionGeneral, int codigoArticulo) 
	{
		InfoDatosString incluyeYCantidad = null;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		String consulta = "";
		
		try 
		{
			logger.info("############## Inicio obtenerInclusionExclusionArticuloAgrupado");
			//la prioridad en este punto va primero por la subgrupo, luego x grupo, luego clase, luego naturaleza
			consulta="SELECT " +
								"incluye as incluye , CASE WHEN cantidad IS NULL  THEN "+ConstantesBD.codigoNuncaValido+" ELSE cantidad END as cantidad " +
							"from " +
								"agru_art_incluexclu_cc " +
							"where " +
								"codigo_inclu_exclu_cc=? " +
								"and (subgrupo=(select s.subgrupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or subgrupo is null) " +
								"and (grupo=(select s.grupo from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or grupo is null) " +
								"and (clase=(select s.clase from subgrupo_inventario s where s.codigo=(select a.subgrupo from articulo a where a.codigo=?)) or clase is null) " +
								"and (naturaleza=(select a.naturaleza from articulo a where a.codigo=?) or naturaleza is null) ";
								
			//para que la consulta nos entregue los resultados con prioridad que no sean null solo es requerido hacerle un order by
			consulta+=" ORDER BY subgrupo, grupo, clase, naturaleza ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoInclusionExclusionGeneral);
			pst.setInt(2, codigoArticulo);
			pst.setInt(3, codigoArticulo);
			pst.setInt(4, codigoArticulo);
			pst.setInt(5, codigoArticulo);
			rs=pst.executeQuery();			
			
			incluyeYCantidad = new InfoDatosString();
			
			if(rs.next()){
				incluyeYCantidad.setAcronimo(rs.getString("incluye"));
				incluyeYCantidad.setValueInt(rs.getInt("cantidad"));
			}
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerInclusionExclusionArticuloAgrupado",sqe);
			logger.error("QUERY obtenerInclusionExclusionArticuloAgrupado -->"+consulta+" --codigoInclusionExclusionGeneral:"+codigoInclusionExclusionGeneral+" codigoArticulo:"+codigoArticulo);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerInclusionExclusionArticuloAgrupado", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin obtenerInclusionExclusionArticuloAgrupado");
		return incluyeYCantidad;
	}
	
	//////////////////////////////////////////////////FIN CALCULOS TARIFAS
	
	/**
	 * metodo para insertar el detalle de los cargos
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public static double insertarDetalleCargos(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario ) throws BDException
	{
		int secuenciaDetalleCargos=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		
		try {
			Log4JManager.info("############## Inicio insertarDetalleCargos");
			secuenciaDetalleCargos=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_cargos");
			pst =  con.prepareStatement(insertarDetalleCargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1, secuenciaDetalleCargos);
			pst.setLong(2, (long)detalleCargo.getCodigoSubcuenta());
			
			if(detalleCargo.getCodigoConvenio()>0){
				pst.setInt(3, detalleCargo.getCodigoConvenio());
			}
			else{
				pst.setObject(3,null);
			}
				
			if(detalleCargo.getCodigoEsquemaTarifario()>0){
				pst.setInt(4, detalleCargo.getCodigoEsquemaTarifario());
			}
			else{
				pst.setObject(4,null);
			}
			
			if(detalleCargo.getCantidadCargada()>0){
				pst.setInt(5, Utilidades.convertirAEntero(detalleCargo.getCantidadCargada()+""));
			}
			else{
				pst.setObject(5,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioTarifa()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(6, detalleCargo.getValorUnitarioTarifa());
			}
			else{
				pst.setObject(6,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(7, detalleCargo.getValorUnitarioCargado());
			}
			else{
				pst.setObject(7,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorTotalCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(8, detalleCargo.getValorTotalCargado());
			}
			else{
				pst.setObject(8,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(9, detalleCargo.getPorcentajeCargado());
			}
			else{
				pst.setObject(9,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeRecargo()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(10, detalleCargo.getPorcentajeRecargo());
			}
			else{
				pst.setObject(10,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioRecargo()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(11, detalleCargo.getValorUnitarioRecargo());
			}
			else{
				pst.setObject(11,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeDescuento()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(12, detalleCargo.getPorcentajeDescuento());
			}
			else{
				pst.setObject(12,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioDescuento()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(13, detalleCargo.getValorUnitarioDescuento());
			}
			else{
				pst.setObject(13,null);
			}
			
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioIva()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(14, detalleCargo.getValorUnitarioIva());
			}
			else{
				pst.setObject(14,null);
			}
			if(!UtilidadTexto.isEmpty(detalleCargo.getRequiereAutorizacion())){
				pst.setString(15, detalleCargo.getRequiereAutorizacion());
			}
			else{
				pst.setObject(15,null);
			}
			
			pst.setInt(16, detalleCargo.getEstado());
			pst.setString(17, detalleCargo.getCubierto());
			
			if(!UtilidadTexto.isEmpty(detalleCargo.getTipoDistribucion())){
				pst.setString(18, detalleCargo.getTipoDistribucion());
			}
			else{
				pst.setObject(18,null);
			}
			
			pst.setInt(19, detalleCargo.getNumeroSolicitud());
			
			if(detalleCargo.getCodigoServicio()>0){
				pst.setInt(20, detalleCargo.getCodigoServicio());
			}
			else{
				pst.setObject(20,null);
			}
							
			if(detalleCargo.getCodigoArticulo()>0){
				pst.setInt(21, detalleCargo.getCodigoArticulo());
			}
			else{
				pst.setObject(21,null);
			}
				
			if(detalleCargo.getCodigoServicioCx()>0){
				pst.setInt(22, detalleCargo.getCodigoServicioCx());
			}
			else{
				pst.setObject(22,null);
			}
				
			if(detalleCargo.getCodigoTipoAsocio()>0){
				pst.setInt(23, detalleCargo.getCodigoTipoAsocio());
			}
			else{
				pst.setObject(23,null);
			}
			
			pst.setString(24, detalleCargo.getFacturado());
		
			if(detalleCargo.getCodigoTipoSolicitud()>0){
				pst.setInt(25, detalleCargo.getCodigoTipoSolicitud());
			}
			else{
				pst.setObject(25,null);
			}
				
			if(!UtilidadTexto.isEmpty(detalleCargo.getPaquetizado())){
				pst.setString(26, detalleCargo.getPaquetizado());
			}
			else{
				pst.setObject(26,null);
			}
			
			if(detalleCargo.getCargoPadre()>0){
				pst.setLong(27, (long)detalleCargo.getCargoPadre());
			}
			else{
				pst.setObject(27,null);
			}
			
			pst.setString(28, loginUsuario);
			
			if(detalleCargo.getCodigoSolicitudSubCuenta()>0){
				pst.setLong(29, (long)detalleCargo.getCodigoSolicitudSubCuenta());
			}
			else{
				pst.setObject(29,null);
			}
			
			if(!UtilidadTexto.isEmpty(detalleCargo.getObservaciones())){
				pst.setString(30, detalleCargo.getObservaciones());
			}
			else{
				pst.setObject(30,null);
			}
			
			if(detalleCargo.getCodigoContrato()>0){
				pst.setLong(31, (long)detalleCargo.getCodigoContrato());
			}
			else{
				pst.setObject(31,null);
			}
				
			if(detalleCargo.getDetCxHonorarios()>0){
				pst.setInt(32, detalleCargo.getDetCxHonorarios());
			}
			else{
				pst.setObject(32,null);
			}
			
			if(detalleCargo.getDetAsocioCxSalasMat()>0){
				pst.setInt(33, detalleCargo.getDetAsocioCxSalasMat());
			}
			else{
				pst.setObject(33,null);
			}
			
			if(UtilidadTexto.getBoolean(detalleCargo.getEsPortatil())){
				pst.setString(34, ConstantesBD.acronimoSi);
			}
			else{
				pst.setString(34, ConstantesBD.acronimoNo);
			}
			
			if(UtilidadTexto.getBoolean(detalleCargo.getDejarExcento())){
				pst.setString(35, ConstantesBD.acronimoSi);
			}
			else{
				pst.setObject(35,ConstantesBD.acronimoNo);
			}
			
			if(detalleCargo.getPorcentajeDctoPromocionServicio()>0){
				pst.setDouble(36, detalleCargo.getPorcentajeDctoPromocionServicio());
			}
			else{
				pst.setNull(36, Types.DOUBLE);
			}
			
			if(detalleCargo.getValorDescuentoPromocionServicio().doubleValue()>0){
				pst.setBigDecimal(37, detalleCargo.getValorDescuentoPromocionServicio());
			}
			else{
				pst.setNull(37, Types.NUMERIC);
			}
			
			if(detalleCargo.getPorcentajeHonorarioPromocionServicio()>0){
				pst.setDouble(38, detalleCargo.getPorcentajeHonorarioPromocionServicio());
			}
			else{
				pst.setNull(38, Types.DOUBLE);
			}
			
			if(detalleCargo.getValorHonorarioPromocionServicio().doubleValue()>0){
				pst.setBigDecimal(39, detalleCargo.getValorHonorarioPromocionServicio());
			}
			else{
				pst.setNull(39, Types.NUMERIC);
			}
			
			if(detalleCargo.getPrograma()>0){
				pst.setLong(40, (long)detalleCargo.getPrograma());
			}
			else{
				pst.setNull(40, Types.DOUBLE);
			}
			
			if(detalleCargo.getPorcentajeDctoBono()>0){
				pst.setDouble(41, detalleCargo.getPorcentajeDctoBono());
			}
			else{
				pst.setNull(41, Types.DOUBLE);
			}
			if(detalleCargo.getValorDescuentoBono().doubleValue()>0){
				pst.setBigDecimal(42, detalleCargo.getValorDescuentoBono());
			}
			else{
				pst.setNull(42, Types.NUMERIC);
			}
			
			if(detalleCargo.getPorcentajeDctoOdontologico()>0){
				pst.setDouble(43, detalleCargo.getPorcentajeDctoOdontologico());
			}
			else{
				pst.setNull(43, Types.DOUBLE);
			}
			if(detalleCargo.getValorDescuentoOdontologico().doubleValue()>0){
				pst.setBigDecimal(44, detalleCargo.getValorDescuentoOdontologico());
			}
			else{
				pst.setNull(44, Types.NUMERIC);
			}
			
			if(detalleCargo.getDetallePaqueteOdontoConvenio()>0){
				pst.setInt(45, detalleCargo.getDetallePaqueteOdontoConvenio());
			}
			else{
				pst.setNull(45, Types.INTEGER);
			}
			
			if(pst.executeUpdate()<=0){
				secuenciaDetalleCargos=ConstantesBD.codigoNuncaValido;
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarDetalleCargos");
		return secuenciaDetalleCargos;
	}
	
	/**
	 * metodo que elimina el detalle cargo x codigo de detalle, debe tener encuenta que primero debe eliminar los
	 * errores_det_cargos si existen para que no saque errores de dependencias @see: eliminarErroresDetalleCargoXCodigoDetalleCargo
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static boolean eliminarDetalleCargoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException
	{
		PreparedStatement pst= null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		boolean exito=false;
		
		try {
			Log4JManager.info("############## Inicio eliminarDetalleCargoXCodigoDetalle");
			String eliminarErroresDetCargo="DELETE FROM errores_det_cargos WHERE codigo_detalle_cargo=?";
			String eliminarArtConsumo="DELETE FROM facturacion.det_cargos_art_consumo WHERE det_cargo=?";
			String eliminarDetCargos="DELETE FROM det_cargos WHERE codigo_detalle_cargo=?";
			con.setAutoCommit(false);
			pst = con.prepareStatement(eliminarErroresDetCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, (long)codigoDetalleCargo);
			pst.executeUpdate();
			
			pst2 = con.prepareStatement(eliminarArtConsumo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst2.setLong(1, (long)codigoDetalleCargo);
			pst2.executeUpdate();
			
			pst3 = con.prepareStatement(eliminarDetCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst3.setLong(1, (long)codigoDetalleCargo);
			pst3.executeUpdate();
			
			con.commit();
			exito=true;
		} 
		catch (SQLException sqe) 
		{
			if (con != null) {
		        try {
		          con.rollback();
		        } catch(SQLException excep) {
		        	Log4JManager.error(excep);
		        }
		    }
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
	    } 
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(con != null){
					con.setAutoCommit(true);
				}
			}
			catch (SQLException se) {
				Log4JManager.error("############## Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarDetalleCargoXCodigoDetalle");
		return exito;
	}
	
	/**
	 * metodo que elimina el detalle cargo art consumo x codigo de detalle, 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static boolean eliminarDetalleCargoArticuloConsumoXCodigoDetalle(Connection con, double codigoDetalleCargo) throws BDException
	{
		PreparedStatement pst=null;
		boolean exito=false;
		
		try {
			Log4JManager.info("############## Inicio eliminarDetalleCargoArticuloConsumoXCodigoDetalle");
			String eliminarArtConsumo="DELETE FROM facturacion.det_cargos_art_consumo WHERE det_cargo=?";
			pst =  con.prepareStatement(eliminarArtConsumo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, (long)codigoDetalleCargo);
			pst.executeUpdate();
			exito=true;
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarDetalleCargoArticuloConsumoXCodigoDetalle");
		return exito;
	}
	
	/**
	 * metodo que realiza una busqueda de los atributos codigo_detalle_cargo,  
	 * y realiza filtros (si existen de) key="subCuenta,solicitud,servicio,articulo" 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector busquedaCodigosDetalleCargos(Connection con, HashMap criteriosBusquedaMap)
	{
		Vector vector= new Vector();
		String consulta=" SELECT " +
								"codigo_detalle_cargo as codigodetallecargo " +
						"FROM " +
								"det_cargos " +
						"WHERE " +
								"eliminado='"+ConstantesBD.acronimoNo+"' ";
		
		if(criteriosBusquedaMap.containsKey("subCuenta"))
		{
			consulta+=" and sub_cuenta= "+Long.valueOf(criteriosBusquedaMap.get("subCuenta").toString());
		}
		if(criteriosBusquedaMap.containsKey("solicitud"))
		{
			consulta+=" and solicitud= "+criteriosBusquedaMap.get("solicitud");
		}
		if(criteriosBusquedaMap.containsKey("servicio"))
		{
			consulta+=" and servicio= "+criteriosBusquedaMap.get("servicio");
		}
		if(criteriosBusquedaMap.containsKey("articulo"))
		{
			consulta+=" and articulo= "+criteriosBusquedaMap.get("articulo");
		}
		if(criteriosBusquedaMap.containsKey("facturado"))
		{
			consulta+=" and facturado='"+criteriosBusquedaMap.get("facturado")+"' ";
		}
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
				vector.add(rs.getDouble("codigodetallecargo"));
			rs.close();
			ps.close();
				
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return vector;
	}
	
	/**
	 * Método que se encarga de búscar el código del servicio para un numero de solicitud y tipo
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitud
	 * @return
	 */
	public static int busquedaCodigoServicioXSolicitud (Connection con, int numeroSolicitud, int codigoTipoSolicitud, String esPortatil) throws BDException
	{
		String consulta="";
		if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
			consulta=busquedaServicioEnInterconsultaStr;
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento && !UtilidadTexto.getBoolean(esPortatil))
			consulta=busquedaServicioEnProcedimientosStr;
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento && UtilidadTexto.getBoolean(esPortatil))
			consulta=busquedaServicioPORTATILEnProcedimientosStr;
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
			consulta=busquedaServicioEnCitaStr;
		//No existe servicio, luego retornamos -1
		else if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion||codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialUrgencias)
			return ConstantesBD.codigoServicioNoDefinido;
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio busquedaCodigoServicioXSolicitud");
			pst=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigoServicio");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin busquedaCodigoServicioXSolicitud");
		return valor;
	}
	
	/**
	 * Método que se encarga de búscar el código del servicio para una 
	 * evolución
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 * @throws SQLException
	 */
	public static int busquedaServicioEnEvolucion (Connection con, int codigoEvolucion) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try {
			Log4JManager.info("############## Inicio busquedaServicioEnEvolucion");
			pst=con.prepareStatement(busquedaServicioEnEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoEvolucion);
			pst.setInt(2, codigoEvolucion);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigoServicio");
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin busquedaServicioEnEvolucion");
		return valor;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param error
	 * @return
	 */
	public static double insertarErrorDetalleCargo(Connection con, double codigoDetalleCargo, String error) throws BDException
	{
		int secuenciaErrorDetalleCargo=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		
		try {
			Log4JManager.info("############## Inicio insertarErrorDetalleCargo");
			secuenciaErrorDetalleCargo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_errores_det_cargos");
			pst = con.prepareStatement(insertarErrorDetalleCargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, secuenciaErrorDetalleCargo);
			pst.setLong(2, (long)codigoDetalleCargo);
			pst.setString(3, error);
			
			if(pst.executeUpdate()<=0)
			{
				secuenciaErrorDetalleCargo=ConstantesBD.codigoNuncaValido;
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarErrorDetalleCargo");
		return secuenciaErrorDetalleCargo;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicioArticulo
	 * @param esServicio
	 * @return
	 */
	public static int obtenerNumeroResponsablesSolicitudCargo(Connection con, int numeroSolicitud,int codigoServicioArticulo, boolean esServicio, String fromYtabla)
	{
		//PUEDE OCURRIR QUE EL SERVICIO NO EXISTA EN LA SOLICITUD PERO SI LO ENVIEN DESDE LA GENERACION DE CARGOS PENDIENTES
		String consulta="select facturacion.getNumRespSolicitud(?) "+fromYtabla;
		int numeroResponsablesXSolicitud=0;
		int numeroResponsablesXSolicitudServicioArticulo=0;
		
		if(esServicio)
		{	
			PreparedStatementDecorator ps;
			ResultSetDecorator rs;
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, numeroSolicitud);
				//logger.info("obtenerNumeroResponsablesSolicitudCargo-->"+consulta+ " ->numsol->"+numeroSolicitud);
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
					numeroResponsablesXSolicitud= rs.getInt(1);
				rs.close();
				ps.close();
			} 
			catch (SQLException e) 
			{
				logger.error("error en obtenerNumeroResponsablesSolicitudCargo==> "+e);
				e.printStackTrace();
			}
		}	
		
		if(codigoServicioArticulo>0)
		{	
			if(esServicio)
				consulta= "select facturacion.getnumrespsolservicio(?,?) "+fromYtabla;
			else
				consulta= "select facturacion.getnumrespsolarticulo(?,?) "+fromYtabla;
			
			PreparedStatementDecorator ps1;
			ResultSetDecorator rs1;
			try 
			{
				ps1 =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setInt(1, numeroSolicitud);
				//logger.info("obtenerNumeroResponsablesSolicitudCargo-->"+consulta+ " ->numsol->"+numeroSolicitud+" codServicioArt->"+codigoServicioArticulo+" esServ->"+esServicio);
				if(codigoServicioArticulo>0)
					ps1.setInt(2, codigoServicioArticulo);
				rs1 =  new ResultSetDecorator(ps1.executeQuery());
				if(rs1.next())
					numeroResponsablesXSolicitudServicioArticulo= rs1.getInt(1);
				rs1.close();
				ps1.close();
			} 
			catch (SQLException e) 
			{
				logger.error("error en obtenerNumeroResponsablesSolicitudCargo==> "+e);
				e.printStackTrace();
			}
		}	
		
		if(numeroResponsablesXSolicitudServicioArticulo>0)
			return numeroResponsablesXSolicitudServicioArticulo;
		else if(numeroResponsablesXSolicitud>0)
			return numeroResponsablesXSolicitud;
		else
			return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * metodo que carga la informacion del detalle de cargo
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static DtoDetalleCargo cargarDetalleCargo(Connection con, BigDecimal codigoDetalleCargo) throws BDException
	{
		DtoDetalleCargo dto= new DtoDetalleCargo();
		dto.setCodigoDetalleCargo(codigoDetalleCargo.doubleValue());
		
		ArrayList<DtoDetalleCargo> lista= cargarDetalleCargos(con, dto);
		
		if(lista.size()>0)
		{
			dto= lista.get(0);
		}
		return dto;
	}
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public static ArrayList<DtoDetalleCargo> cargarDetalleCargos(Connection con, DtoDetalleCargo criteriosBusquedaDtoDetalleCargo) throws BDException
	{
		ArrayList<DtoDetalleCargo> arrayDtoDCargo= new ArrayList<DtoDetalleCargo>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			Log4JManager.info("############## Inicio cargarDetalleCargos");
			String consultaDetalle= "SELECT " +
						"codigo_detalle_cargo, " +
						"sub_cuenta, " +
						"COALESCE(convenio,"+ConstantesBD.codigoNuncaValido+") AS convenio, " +
						"COALESCE(contrato,"+ConstantesBD.codigoNuncaValido+") AS contrato, " +
						"COALESCE(esquema_tarifario,"+ConstantesBD.codigoNuncaValido+") AS esquema_tarifario, " +
						"COALESCE(cantidad_cargada, "+ConstantesBD.codigoNuncaValido+") AS cantidad_cargada, " +
						"COALESCE(valor_unitario_tarifa, "+ConstantesBD.codigoNuncaValido+") AS valor_unitario_tarifa, " +
						"COALESCE(valor_unitario_cargado, "+ConstantesBD.codigoNuncaValido+") AS valor_unitario_cargado, " +
						"COALESCE(valor_total_cargado, "+ConstantesBD.codigoNuncaValido+") AS  valor_total_cargado, " +
						"COALESCE(porcentaje_cargado, "+ConstantesBD.codigoNuncaValido+") AS  porcentaje_cargado, " +
						"COALESCE(porcentaje_recargo, "+ConstantesBD.codigoNuncaValido+") AS  porcentaje_recargo, " +
						"COALESCE(valor_unitario_recargo, "+ConstantesBD.codigoNuncaValido+") AS  valor_unitario_recargo, " +
						"COALESCE(porcentaje_dcto, "+ConstantesBD.codigoNuncaValido+") AS porcentaje_dcto, " +
						"COALESCE(valor_unitario_dcto, "+ConstantesBD.codigoNuncaValido+") AS valor_unitario_dcto, " +
						"COALESCE(valor_unitario_iva, "+ConstantesBD.codigoNuncaValido+") AS valor_unitario_iva, " +
						/*"COALESCE(nro_autorizacion,'') AS nro_autorizacion, " +*/
						"estado, " +
						"cubierto, " +
						"COALESCE(tipo_distribucion,'') AS tipo_distribucion, " +
						"solicitud, " +
						"COALESCE(servicio, "+ConstantesBD.codigoNuncaValido+") AS servicio, " +
						"CASE WHEN servicio IS NOT NULL THEN getnombreservicio(servicio, "+ConstantesBD.codigoTarifarioCups+") else '' end as nombre_servicio, " +
						"COALESCE(articulo, "+ConstantesBD.codigoNuncaValido+") AS articulo,  " +
						"COALESCE(servicio_cx, "+ConstantesBD.codigoNuncaValido+") AS servicio_cx, " +
						"COALESCE(tipo_asocio,"+ConstantesBD.codigoNuncaValido+") AS tipo_asocio, " +
						"facturado, " +
						"tipo_solicitud, " +
						"paquetizado, " +
						"COALESCE(cargo_padre, "+ConstantesBD.codigoNuncaValido+") AS  cargo_padre, " +
						"COALESCE(cod_sol_subcuenta, "+ConstantesBD.codigoNuncaValido+") AS cod_sol_subcuenta, " +
						"COALESCE(observaciones,'') AS observaciones, " +
						"requiere_autorizacion, " +
						"coalesce(det_cx_honorarios, "+ConstantesBD.codigoNuncaValido+") as det_cx_honorarios, " +
						"coalesce(det_asocio_cx_salas_mat, "+ConstantesBD.codigoNuncaValido+") as det_asocio_cx_salas_mat, " +
						"es_portatil as esportatil, " +
						"dejar_excento as dejarexcento, " +
						"coalesce(porcentaje_dcto_prom_serv,0) AS porcentaje_dcto_prom_serv, "+
						"coalesce(valor_descuento_prom_serv,0) AS valor_descuento_prom_serv, "+
						"coalesce(porc_honorario_prom_serv,0) AS porc_honorario_prom_serv, "+
						"coalesce(valor_honorario_prom_serv,0) AS valor_honorario_prom_serv, " +
						"coalesce(programa,"+ConstantesBD.codigoNuncaValido+") as programa , "+
						"coalesce(porcentaje_dcto_bono_serv, 0) as porcentaje_dcto_bono_serv, "+  
						"coalesce(valor_descuento_bono_serv, 0) as valor_descuento_bono_serv, "+ 
						"coalesce(porcentaje_dcto_odontologico, 0) as porcentaje_dcto_odontologico, "+ 
						"coalesce(valor_descuento_odontologico, 0) as valor_descuento_odontologico, " +
						"fecha_modifica as fechamodifica, " +
						"hora_modifica as horamodifica, " +
						"usuario_modifica as usuariomodifica, " +
						"coalesce(det_paq_odon_convenio,0) as det_paq_odon_convenio "+  
					"FROM " +
						"det_cargos " +
						"WHERE estado<>"+ConstantesBD.codigoEstadoFAnulada+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			
			if(criteriosBusquedaDtoDetalleCargo.getCodigoDetalleCargo()>0){
				consultaDetalle+=" AND codigo_detalle_cargo = "+(long)criteriosBusquedaDtoDetalleCargo.getCodigoDetalleCargo();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoSubcuenta()>0){
				consultaDetalle+=" AND sub_cuenta = "+(long)criteriosBusquedaDtoDetalleCargo.getCodigoSubcuenta();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoConvenio()>0){
				consultaDetalle+=" AND convenio = "+criteriosBusquedaDtoDetalleCargo.getCodigoConvenio();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoContrato()>0){
				consultaDetalle+=" AND contrato = "+criteriosBusquedaDtoDetalleCargo.getCodigoContrato();		
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoEsquemaTarifario()>0){
				consultaDetalle+=" AND esquema_tarifario = "+criteriosBusquedaDtoDetalleCargo.getCodigoEsquemaTarifario();
			}
			if(criteriosBusquedaDtoDetalleCargo.getEstado()>0){
				consultaDetalle+=" AND estado = "+criteriosBusquedaDtoDetalleCargo.getEstado();
			}
			if(!UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getCubierto())){
				consultaDetalle+=" AND cubierto = '"+criteriosBusquedaDtoDetalleCargo.getCubierto()+"' ";
			}
			if(!UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getTipoDistribucion())){
				consultaDetalle+=" AND tipo_distribucion = '"+criteriosBusquedaDtoDetalleCargo.getTipoDistribucion()+"' ";
			}
			if(criteriosBusquedaDtoDetalleCargo.getNumeroSolicitud()>0){
				consultaDetalle+=" AND solicitud = "+criteriosBusquedaDtoDetalleCargo.getNumeroSolicitud();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoServicio()>0){
				consultaDetalle+=" AND servicio = "+criteriosBusquedaDtoDetalleCargo.getCodigoServicio();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoArticulo()>0){
				consultaDetalle+=" AND articulo = "+criteriosBusquedaDtoDetalleCargo.getCodigoArticulo();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoServicioCx()>0){
				consultaDetalle+=" AND servicio_cx = "+criteriosBusquedaDtoDetalleCargo.getCodigoServicioCx();
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoTipoAsocio()>0){
				consultaDetalle+=" AND tipo_asocio = "+criteriosBusquedaDtoDetalleCargo.getCodigoTipoAsocio();
			}
			if(!UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getFacturado())){
				consultaDetalle+=" AND facturado = '"+criteriosBusquedaDtoDetalleCargo.getFacturado()+"' ";
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoTipoSolicitud()>0){
				consultaDetalle+=" AND tipo_solicitud = "+criteriosBusquedaDtoDetalleCargo.getCodigoTipoSolicitud();
			}
			if(!UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getPaquetizado())){
				consultaDetalle+=" AND paquetizado = '"+criteriosBusquedaDtoDetalleCargo.getPaquetizado()+"'";
			}
			if(criteriosBusquedaDtoDetalleCargo.getCargoPadre()>0){
				consultaDetalle+=" AND cargo_padre = '"+criteriosBusquedaDtoDetalleCargo.getCodigoTipoSolicitud()+"' ";
			}
			if(criteriosBusquedaDtoDetalleCargo.getCodigoSolicitudSubCuenta()>0){
				consultaDetalle+=" AND cod_sol_subcuenta = '"+(long)criteriosBusquedaDtoDetalleCargo.getCodigoSolicitudSubCuenta()+"' ";
			}
			if(!UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getRequiereAutorizacion())){
				consultaDetalle+=" AND requiere_autorizacion = '"+criteriosBusquedaDtoDetalleCargo.getRequiereAutorizacion()+"'";
			}
			if(criteriosBusquedaDtoDetalleCargo.getFiltrarSoloCantidadesMayoresCero()){
				consultaDetalle+=" AND cantidad_cargada > 0 "; 
			}
			if( !UtilidadTexto.isEmpty(criteriosBusquedaDtoDetalleCargo.getEsPortatil())){
				consultaDetalle+=" AND es_portatil = '"+criteriosBusquedaDtoDetalleCargo.getEsPortatil()+"' ";
			}
			if(criteriosBusquedaDtoDetalleCargo.getDetallePaqueteOdontoConvenio()>0){
			consultaDetalle+=" AND det_paq_odon_convenio= "+criteriosBusquedaDtoDetalleCargo.getDetallePaqueteOdontoConvenio()+" ";
			}

			pst = con.prepareStatement(consultaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			while(rs.next())
			{
				DtoDetalleCargo dtoDCargo= new DtoDetalleCargo();
				dtoDCargo.setCodigoDetalleCargo(rs.getDouble("codigo_detalle_cargo"));
				dtoDCargo.setCodigoSubcuenta(rs.getDouble("sub_cuenta"));
				dtoDCargo.setCodigoConvenio(rs.getInt("convenio"));
				dtoDCargo.setCodigoContrato(rs.getInt("contrato"));
				dtoDCargo.setCodigoEsquemaTarifario(rs.getInt("esquema_tarifario"));
				dtoDCargo.setCantidadCargada(rs.getInt("cantidad_cargada"));
				dtoDCargo.setValorUnitarioTarifa(rs.getDouble("valor_unitario_tarifa"));
				dtoDCargo.setValorUnitarioCargado(rs.getDouble("valor_unitario_cargado"));
				dtoDCargo.setValorTotalCargado(rs.getDouble("valor_total_cargado"));
				dtoDCargo.setPorcentajeCargado(rs.getDouble("porcentaje_cargado"));
				dtoDCargo.setPorcentajeRecargo(rs.getDouble("porcentaje_recargo"));
				dtoDCargo.setValorUnitarioRecargo(rs.getDouble("valor_unitario_recargo"));
				dtoDCargo.setPorcentajeDescuento(rs.getDouble("porcentaje_dcto"));
				dtoDCargo.setValorUnitarioDescuento(rs.getDouble("valor_unitario_dcto"));
				dtoDCargo.setValorUnitarioIva(rs.getDouble("valor_unitario_iva"));
				//dtoDCargo.setNumeroAutorizacion(rs.getString("nro_autorizacion"));
				dtoDCargo.setEstado(rs.getInt("estado"));
				dtoDCargo.setCubierto(rs.getString("cubierto"));
				dtoDCargo.setTipoDistribucion(rs.getString("tipo_distribucion"));
				dtoDCargo.setNumeroSolicitud(rs.getInt("solicitud"));
				dtoDCargo.setCodigoServicio(rs.getInt("servicio"));
				dtoDCargo.setNombreServicio(rs.getString("nombre_servicio"));
				dtoDCargo.setCodigoArticulo(rs.getInt("articulo"));
				dtoDCargo.setCodigoServicioCx(rs.getInt("servicio_cx"));
				dtoDCargo.setCodigoTipoAsocio(rs.getInt("tipo_asocio"));
				dtoDCargo.setFacturado(rs.getString("facturado"));
				dtoDCargo.setCodigoTipoSolicitud(rs.getInt("tipo_solicitud"));
				dtoDCargo.setPaquetizado(rs.getString("paquetizado"));
				dtoDCargo.setCargoPadre(rs.getDouble("cargo_padre"));
				dtoDCargo.setCodigoSolicitudSubCuenta(rs.getDouble("cod_sol_subcuenta"));
				dtoDCargo.setObservaciones(rs.getString("observaciones")==null?"":rs.getString("observaciones"));
				dtoDCargo.setRequiereAutorizacion(rs.getString("requiere_autorizacion"));
				dtoDCargo.setDetCxHonorarios(rs.getInt("det_cx_honorarios"));
				dtoDCargo.setDetAsocioCxSalasMat(rs.getInt("det_asocio_cx_salas_mat"));
				dtoDCargo.setEsPortatil(rs.getString("esportatil"));
				dtoDCargo.setDejarExcento(rs.getString("dejarexcento"));
			
				dtoDCargo.setPorcentajeDctoPromocionServicio(rs.getDouble("porcentaje_dcto_prom_serv"));
				dtoDCargo.setValorDescuentoPromocionServicio(rs.getBigDecimal("valor_descuento_prom_serv"));
				dtoDCargo.setPorcentajeHonorarioPromocionServicio(rs.getDouble("porc_honorario_prom_serv"));
				dtoDCargo.setValorHonorarioPromocionServicio(rs.getBigDecimal("valor_honorario_prom_serv"));
				
				dtoDCargo.setPrograma(rs.getDouble("programa"));
				dtoDCargo.setPorcentajeDctoBono(rs.getDouble("porcentaje_dcto_bono_serv"));
				dtoDCargo.setValorDescuentoBono(rs.getBigDecimal("valor_descuento_bono_serv"));
				dtoDCargo.setPorcentajeDctoOdontologico(rs.getDouble("porcentaje_dcto_odontologico"));
				dtoDCargo.setValorDescuentoOdontologico(rs.getBigDecimal("valor_descuento_odontologico"));
				
				dtoDCargo.setFHU(new DtoInfoFechaUsuario(rs.getString("horamodifica"), rs.getString("fechamodifica"), rs.getString("usuariomodifica")));
				dtoDCargo.setDetallePaqueteOdontoConvenio(rs.getInt("det_paq_odon_convenio"));
				
				arrayDtoDCargo.add(dtoDCargo);
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargarDetalleCargos");
		return arrayDtoDCargo;
	}
	
	/**
	 * actualizar el detalle del cargo
	 * @param con
	 * @param detalleCargo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean updateDetalleCargo(Connection con, DtoDetalleCargo detalleCargo, String loginUsuario) throws BDException
	{
		PreparedStatement pst=null;
		boolean resultado=false;
		
		try {
			Log4JManager.info("############## Inicio updateDetalleCargo");
			String actualizarCargo= "UPDATE det_cargos set " +	
					"sub_cuenta=?, " +			//1
					"convenio=?, " +			//2
					"esquema_tarifario=?, " +	//3
					"cantidad_cargada=?, " +	//4
					"valor_unitario_tarifa=?, "+//5
					"valor_unitario_cargado=?,"+//6
					"valor_total_cargado=?, " +	//7
					"porcentaje_cargado=?, " +	//8
					"porcentaje_recargo=?, " +	//9
					"valor_unitario_recargo=?,"+//10
					"porcentaje_dcto=?, " +		//11
					"valor_unitario_dcto=?, " +	//12
					"valor_unitario_iva=?, " +	//13
					"requiere_autorizacion=?, "+//14
					/*"nro_autorizacion=?, " +	//15*/
					"estado=?, " +				//15
					"cubierto=?, " +			//16
					"tipo_distribucion=?, " +	//17
					"solicitud=?, " +			//18
					"servicio=?, " +			//19
					"articulo=?, " +			//20
					"servicio_cx=?, " +			//21
					"tipo_asocio=?, " +			//22
					"facturado=?, " +			//23
					"tipo_solicitud=?, " +		//24	
					"paquetizado=?, " +			//25
					"cargo_padre=?, " +			//26
					"usuario_modifica=?, " +	//27
					"cod_sol_subcuenta=?, " +	//28
					"observaciones=?, "+		//29
					"contrato=?, "+				//30
					"fecha_modifica= CURRENT_DATE, " +		
					"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
					"det_cx_honorarios=?, " +		//31
					"det_asocio_cx_salas_mat=? " +//32	
					"where codigo_detalle_cargo= ? "; //33
			
			pst = con.prepareStatement(actualizarCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, (long)detalleCargo.getCodigoSubcuenta());
			if(detalleCargo.getCodigoConvenio()>0){
				pst.setInt(2, detalleCargo.getCodigoConvenio());
			}
			else{
				pst.setNull(2, Types.INTEGER);
			}	
			if(detalleCargo.getCodigoEsquemaTarifario()>0){
				pst.setInt(3, detalleCargo.getCodigoEsquemaTarifario());
			}
			else{
				pst.setNull(3, Types.INTEGER);
			}
			if(detalleCargo.getCantidadCargada()>0){
				pst.setInt(4, detalleCargo.getCantidadCargada());
			}
			else{
				pst.setNull(4, Types.INTEGER);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioTarifa()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(5, detalleCargo.getValorUnitarioTarifa());
			}
			else{
				pst.setNull(5, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(6, detalleCargo.getValorUnitarioCargado());
			}
			else{
				pst.setNull(6, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorTotalCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(7, detalleCargo.getValorTotalCargado());
			}
			else{
				pst.setNull(7, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeCargado()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(8, detalleCargo.getPorcentajeCargado());
			}
			else{
				pst.setNull(8, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeRecargo()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(9, detalleCargo.getPorcentajeRecargo());
			}
			else{
				pst.setNull(9, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioRecargo()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(10, detalleCargo.getValorUnitarioRecargo());
			}
			else{
				pst.setNull(10, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getPorcentajeDescuento()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(11, detalleCargo.getPorcentajeDescuento());
			}
			else{
				pst.setNull(11, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioDescuento()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(12, detalleCargo.getValorUnitarioDescuento());
			}
			else{
				pst.setNull(12, Types.DOUBLE);
			}
			//el valor puede ser cero
			if(detalleCargo.getValorUnitarioIva()>ConstantesBD.codigoNuncaValidoDouble){
				pst.setDouble(13, detalleCargo.getValorUnitarioIva());
			}
			else{
				pst.setNull(13, Types.DOUBLE);
			}
			if(!UtilidadTexto.isEmpty(detalleCargo.getRequiereAutorizacion())){
				pst.setString(14, detalleCargo.getRequiereAutorizacion());
			}
			else{
				pst.setNull(14, Types.VARCHAR);
			}
			/*
			if(!UtilidadTexto.isEmpty(detalleCargo.getNumeroAutorizacion()))
				ps.setString(15, detalleCargo.getNumeroAutorizacion());
			else
				ps.setNull(15, Types.VARCHAR);
			*/
			
			pst.setInt(15, detalleCargo.getEstado());
			pst.setString(16, detalleCargo.getCubierto());
			
			if(!UtilidadTexto.isEmpty(detalleCargo.getTipoDistribucion())){
				pst.setString(17, detalleCargo.getTipoDistribucion());
			}
			else{
				pst.setNull(17, Types.VARCHAR);
			}
			pst.setInt(18, detalleCargo.getNumeroSolicitud());
			
			if(detalleCargo.getCodigoServicio()>0){
				pst.setInt(19, detalleCargo.getCodigoServicio());
			}
			else{
				pst.setNull(19, Types.INTEGER);
			}
			if(detalleCargo.getCodigoArticulo()>0){
				pst.setInt(20, detalleCargo.getCodigoArticulo());
			}
			else{
				pst.setNull(20, Types.INTEGER);
			}
			if(detalleCargo.getCodigoServicioCx()>0){
				pst.setInt(21, detalleCargo.getCodigoServicioCx());
			}
			else{
				pst.setNull(21, Types.INTEGER);
			}
			if(detalleCargo.getCodigoTipoAsocio()>0){
				pst.setInt(22, detalleCargo.getCodigoTipoAsocio());
			}
			else{
				pst.setNull(22, Types.INTEGER);
			}
			pst.setString(23, detalleCargo.getFacturado());
		
			if(detalleCargo.getCodigoTipoSolicitud()>0){
				pst.setInt(24, detalleCargo.getCodigoTipoSolicitud());
			}
			else{
				pst.setNull(24, Types.INTEGER);
			}
			if(!UtilidadTexto.isEmpty(detalleCargo.getPaquetizado())){
				pst.setString(25, detalleCargo.getPaquetizado());
			}
			else{
				pst.setNull(25, Types.VARCHAR);
			}
			//logger.info(detalleCargo.getCargoPadre());
			if(detalleCargo.getCargoPadre()>0){
				pst.setLong(26, (long)detalleCargo.getCargoPadre());
			}
			else{
				pst.setNull(26, Types.DOUBLE);
			}
			pst.setString(27, loginUsuario);
			if(detalleCargo.getCodigoSolicitudSubCuenta()>0){
				pst.setLong(28, (long)detalleCargo.getCodigoSolicitudSubCuenta());
			}
			else{
				pst.setNull(28, Types.DOUBLE);
			}
			if(!UtilidadTexto.isEmpty(detalleCargo.getObservaciones())){
				pst.setString(29, detalleCargo.getObservaciones());
			}
			else{
				pst.setNull(29, Types.VARCHAR);
			}
			if(detalleCargo.getCodigoContrato()>0){
				pst.setLong(30, (long)detalleCargo.getCodigoContrato());
			}
			else{
				pst.setNull(30, Types.DOUBLE);
			}
			if(detalleCargo.getDetCxHonorarios()>0){
				pst.setInt(31, detalleCargo.getDetCxHonorarios());
			}
			else{
				pst.setNull(31, Types.INTEGER);
			}
			if(detalleCargo.getDetAsocioCxSalasMat()>0){
				pst.setInt(32, detalleCargo.getDetAsocioCxSalasMat());
			}
			else{
				pst.setNull(32, Types.INTEGER);
			}
			pst.setLong(33, (long)detalleCargo.getCodigoDetalleCargo());
			
			if(pst.executeUpdate() > 0)
			{
				resultado= true;
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin updateDetalleCargo");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param esServicio
	 * @return
	 */
	public static boolean updateCantidadesCargo(Connection con, int cantidad, int numeroSolicitud, double subCuenta, int codigoArticuloServicio, boolean esServicio) 
	{
		String consulta="update det_cargos set cantidad_cargada ="+cantidad+" where solicitud="+numeroSolicitud+" ";
		if(subCuenta>0)
			consulta+=" and sub_cuenta= "+(long)subCuenta+" ";
		if(esServicio)
			consulta=" and servicio ="+codigoArticuloServicio;
		else
			consulta=" and articulo ="+codigoArticuloServicio;
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			boolean valor=ps.executeUpdate()>0;
			ps.close();
			return valor;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * metodo que obtiene los cargos en estado pendiente - cargado para poderles posteriormente recalcular los cargos  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector obtenerCodigosArticulosXSolicitudACargar (Connection con, int numeroSolicitud)
	{
		String consultaStr="SELECT articulo as articulo FROM det_cargos WHERE solicitud=? AND eliminado='"+ConstantesBD.acronimoNo+"' and estado in("+ConstantesBD.codigoEstadoFPendiente+","+ConstantesBD.codigoEstadoFCargada+") and articulo is not null and (facturado='"+ConstantesBD.acronimoNo+"' or facturado is null) and (paquetizado='"+ConstantesBD.acronimoNo+"' or paquetizado is null) ";
		Vector vector= new Vector();
		//logger.info("obtenerCodigosAritculosXSolicitud-->"+consultaStr);
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
				vector.add(rs.getInt("articulo")+"");
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return vector;
	}
	
	/**
	 * existe cargos pendientes
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existenCargosPendientesXSolicitud(Connection con, String numeroSolicitud)
	{
		String consulta="select codigo_detalle_cargo from det_cargos where solicitud=? and eliminado='"+ConstantesBD.acronimoNo+"' and estado= "+ConstantesBD.codigoEstadoFPendiente;
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				resultado.close();
				statement.close();
				return true;
			}
			resultado.close();
			statement.close();
		}
		catch (SQLException e)
		{
			logger.error("Error existeCargoDadaNumSolicitud (SqlBaseCargoMedicamentosDao): "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estado
	 * @return
	 */
	public static boolean modificarEstadoCargo(Connection con, double codigoDetalleCargo, int estado) throws BDException
	{
		PreparedStatement pst = null;
		boolean resultado=false;
		
		try	{
			Log4JManager.info("############## Inicio modificarEstadoCargo");
			String updateStr= "UPDATE det_cargos set estado=? where codigo_detalle_cargo=?";
			pst= con.prepareStatement(updateStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, estado);
			pst.setLong(2, (long)codigoDetalleCargo);
			if(pst.executeUpdate()>0){
				resultado=true;
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin modificarEstadoCargo");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codigoDetalleCargoPadre
	 * @return
	 */
	public static boolean modificarCargoPadre(Connection con, double codigoDetalleCargo, double codigoDetalleCargoPadre)
	{
		String updateStr= "UPDATE det_cargos set cargo_padre=?, paquetizado='"+ConstantesBD.acronimoSi+"' where codigo_detalle_cargo=?";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(updateStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoDetalleCargoPadre);
			ps.setLong(2, (long)codigoDetalleCargo);
			//logger.info("llega a insertar modificarCargopadre---------->"+updateStr+" ----codigoDetalleCargo== "+codigoDetalleCargo+" padre-->"+codigoDetalleCargoPadre);
			boolean valor=ps.executeUpdate()>0;
			ps.close();
			return valor;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en modificarCargoPadre : SqlBaseCargoDao "+e.toString() );
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param facturado
	 * @param portatil ('S', 'N', '')
	 * @return
	 */
	public static double obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(Connection con, int numeroSolicitud, double subCuenta, String facturado, String paquetizado, String portatil ) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try	{
			Log4JManager.info("############## Inicio obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio");
			String consulta="SELECT codigo_detalle_cargo as codigo from det_cargos where solicitud =? AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			if(!facturado.equals("")){
				consulta+=" and facturado ='"+facturado+"' ";
			}
			if(subCuenta>0){
				consulta+=" and sub_cuenta="+(long)subCuenta;
			}
			if(!UtilidadTexto.isEmpty(paquetizado)){
				consulta+=" and paquetizado= '"+paquetizado+"' ";
			}
			if(UtilidadTexto.getBoolean(portatil)){
				consulta+=" and es_portatil = '"+ConstantesBD.acronimoSi+"' ";
			}
			else{
				consulta+=" and es_portatil = '"+ConstantesBD.acronimoNo+"' ";
			}
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			
			if(rs.next()){
				valor=rs.getInt("codigo");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio");
		return valor;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @param codigoArticulo
	 * @param facturado
	 * @return
	 */
	public static double obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo(Connection con, int numeroSolicitud, double subCuenta, int codigoArticulo, String facturado, String paquetizado) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try	{
			Log4JManager.info("############## Inicio obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo");
			String consulta="SELECT codigo_detalle_cargo as codigo from det_cargos where solicitud =? AND eliminado='"+ConstantesBD.acronimoNo+"' and articulo="+codigoArticulo;
			if(!facturado.equals(""))
				consulta+= " and facturado ='"+facturado+"' ";
			if(subCuenta>0)
				consulta+=" and sub_cuenta="+(long)subCuenta;
			if(!UtilidadTexto.isEmpty(paquetizado))
				consulta+=" and paquetizado= '"+paquetizado+"' ";
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("codigo");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCodigoDetalleCargoXSolicitudSubCuentaArticulo");
		return valor;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector obtenerErroresDetalleCargo(Connection con, double codigoDetalleCargo)
	{
		String consulta="SELECT error as error from errores_det_cargos where codigo_detalle_cargo =?";
		Vector vertor=new Vector();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoDetalleCargo);
			//logger.info("llega a obtenerErroresDetalleCargo---------->"+consulta+" ----codigoDetalleCargo== "+codigoDetalleCargo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vertor.add(rs.getString("error"));
			}
			
			rs.close();
			ps.close();
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en modificarEstadoCargo : SqlBaseCargoDao "+e.toString() );
		}
		return vertor;	
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @return
	 */
	public static int obtenerTotalAdminFarmaciaXResponsable(Connection con, int codigoArticulo, int numeroSolicitud, double codigoSubcuenta, String consulta) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int valor=ConstantesBD.codigoNuncaValido;
		
		try	{
			Log4JManager.info("############## Inicio obtenerTotalAdminFarmaciaXResponsable");
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				valor=rs.getInt("cant");
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerTotalAdminFarmaciaXResponsable");
		return valor; 
	}

	/**
	 * 
	 * @param con
	 * @param numSolicitud
	 * @param codServArt
	 * @param esServicio
	 * @return
	 */
	public static boolean eliminarDetalleCargoXSolicitudServArt(Connection con, int numSolicitud, String codServArt, boolean esServicio) 
	{
		PreparedStatement pst=null;
		boolean resultado=false;
		try 
		{
			logger.info("############## Inicio eliminarDetalleCargoXSolicitudServArt");
			String consulta="DELETE FROM det_cargos WHERE solicitud=? ";
			if(esServicio)
				consulta=consulta+" AND servicio="+codServArt;
			else
				consulta=consulta+" AND articulo="+codServArt;
			pst = con.prepareStatement(consulta+" AND facturado='"+ConstantesBD.acronimoNo+"'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numSolicitud);
			pst.executeUpdate();
			resultado=true;
		} 
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR eliminarDetalleCargoXSolicitudServArt",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR eliminarDetalleCargoXSolicitudServArt", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		logger.info("############## Fin eliminarDetalleCargoXSolicitudServArt");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numSolicitud
	 * @param codServArt
	 * @param codigoServicioAsocio 
	 * @param estadoFacturado
	 * @param esServicio
	 * @param numeroFactura 
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoDetalleCargo(Connection con, double subCuenta, int numSolicitud, int codServArt, int codigoServicioAsocio, String estadoFacturado, boolean esServicio, int numeroFactura)
	{
		String estadoAnterior="";
		if(estadoFacturado.equals(ConstantesBD.acronimoSi))
			estadoAnterior=ConstantesBD.acronimoNo;
		else
			estadoAnterior=ConstantesBD.acronimoSi;
		
		String consulta="UPDATE det_cargos SET facturado='"+estadoFacturado+"'";
		if(numeroFactura>0)
		{
			consulta=consulta+" , codigo_factura= "+numeroFactura;
		}
		else
		{
			consulta=consulta+" , set codigo_factura=null ";
		}
		consulta=consulta+" WHERE sub_cuenta=? and solicitud=? and  ";
		if(esServicio)
		{
			if(codigoServicioAsocio>ConstantesBD.codigoNuncaValido)
			{
				consulta+="servicio="+codigoServicioAsocio+" and servicio_cx= "+codServArt;
			}
			else
			{
				consulta+="servicio= "+codServArt;
			}
		}
		else
			consulta+="articulo= "+codServArt;
		consulta+=" and facturado='"+estadoAnterior+"' ";
		
		//logger.info("consulta actualizarEstadoFacturadoDetalleCargo->"+consulta);
		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)subCuenta);
			ps.setInt(2, numSolicitud);
			ps.executeUpdate();
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizarEstadoFacturadoDetalleCargo ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoSubCuenta(Connection con, double subCuenta, String estado)
	{
		String consulta="UPDATE sub_cuentas SET facturado='"+estado+"' WHERE sub_cuenta=?";
		//logger.info("consulta actualizarEstadoFacturadoSubCuenta->"+consulta+" subcuenta->"+subCuenta);
		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)subCuenta);
			ps.executeUpdate();
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizarEstadoFacturadoSubCuenta ");
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * metodo para obtener los codigos det cargo de los componenetes de un paquete
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector obtenerCargosComponentesPaquete(Connection con, double codigoDetalleCargo) throws BDException
	{
		Vector vertor=new Vector();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerCargosComponentesPaquete");
			String consulta="SELECT codigo_detalle_cargo as codi from det_cargos where cargo_padre =? AND eliminado='"+ConstantesBD.acronimoNo+"' ";
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setLong(1, (long)codigoDetalleCargo);
			rs=pst.executeQuery();
			while(rs.next())
			{
				vertor.add(rs.getString("codi"));
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCargosComponentesPaquete");
		return vertor;	
	}
	
	
	/**
	 * metodo para actualizar el cargo padre de los componentes de un paquete
	 * @param con
	 * @param cargoPadre
	 * @param codigosDetalleCargoVector
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean actualizarCargoPadreComponentesPaquetes(Connection con, double cargoPadre, Vector codigosDetalleCargoVector) throws BDException
	{
		PreparedStatement pst=null;
		boolean resultado=false;
		
		try	{
			Log4JManager.info("############## Inicio actualizarCargoPadreComponentesPaquetes");
			String consulta="UPDATE det_cargos SET cargo_padre="+(long)cargoPadre+" WHERE codigo_detalle_cargo in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(codigosDetalleCargoVector, false)+") ";
			if(cargoPadre<1){
				consulta="UPDATE det_cargos SET cargo_padre = null WHERE codigo_detalle_cargo in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(codigosDetalleCargoVector, false)+") ";
			}
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.executeUpdate();
			resultado=true;
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin actualizarCargoPadreComponentesPaquetes");
		return resultado;
	}
	
	/**
	 * Solo se puede borrar el cargo del portatil cuando esta en estado solicitada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean borrarCargoPortatil(Connection con, int numeroSolicitud)
	{
		double solicitudSubcuenta= obtenerSolicitudSubCuentaPortatil(con, numeroSolicitud);
		String consulta="DELETE FROM det_cargos WHERE solicitud=? and es_portatil='"+ConstantesBD.acronimoSi+"'"; 
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			//logger.info("llega a borrarCargoPortatil---------->"+consulta+" ----numsol== "+numeroSolicitud);
			if(ps.executeUpdate()>0)
			{
				consulta="DELETE FROM solicitudes_subcuenta WHERE codigo="+(long)solicitudSubcuenta;
				PreparedStatementDecorator ps1=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				boolean valor= ps1.executeUpdate()>0;
				ps1.close();
				ps.close();
				return valor;
			}
			ps.close();		
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en modificarEstadoCargo : SqlBaseCargoDao "+e.toString() );
		}
		return false;
	}
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 *  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static double obtenerSolicitudSubCuentaPortatil(Connection con, int numeroSolicitud)
	{
		String consulta=" select cod_sol_subcuenta from det_cargos where solicitud=? and es_portatil='"+ConstantesBD.acronimoSi+"' ";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			double valor=ConstantesBD.codigoNuncaValido;
			if(rs.next())
				valor= rs.getDouble(1);
			rs.close();
			ps.close();
			return valor;
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en obtenerSolicitudSubCuentaPortatil : SqlBaseCargoDao "+e.toString() );
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerFechaCalculoCargo(Connection con, int numeroSolicitud) throws BDException
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		String valor=UtilidadFecha.getFechaActual(con);
		
		try {
			Log4JManager.info("############## Inicio obtenerFechaCalculoCargo");
			String cadena=" SELECT tipo,to_char(fecha_solicitud,'dd/mm/yyyy') as fechasolicitud,estado_historia_clinica as estado from solicitudes where numero_solicitud="+numeroSolicitud;
			
			pst= con.prepareStatement(cadena);
			rs=pst.executeQuery();
			if(rs.next())
			{
				int tipoSol=rs.getInt(1);
				int estado=rs.getInt("estado");
				if(tipoSol==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos||(tipoSol!=ConstantesBD.codigoTipoSolicitudCargosDirectosServicios&&(estado==ConstantesBD.codigoEstadoHCSolicitada||estado==ConstantesBD.codigoEstadoHCDespachada)))
				{
					valor=rs.getString("fechasolicitud");
				}
				else if(tipoSol==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios)
				{
					cadena="SELECT to_char(fecha_ejecucion,'dd/mm/yyyy') as fechaejecucion from cargos_directos where numero_solicitud = "+numeroSolicitud;
					pst2= con.prepareStatement(cadena);
					rs2=pst2.executeQuery();
					if(rs2.next())
					{
						if(rs2.getObject(1)!=null&&!UtilidadTexto.isEmpty(rs2.getString(1))){
							valor=rs2.getString(1);
						}
					}
				}
				else if((estado==ConstantesBD.codigoEstadoHCRespondida||estado==ConstantesBD.codigoEstadoHCInterpretada)&&tipoSol==ConstantesBD.codigoTipoSolicitudProcedimiento)
				{
					cadena="SELECT to_char(fecha_ejecucion,'dd/mm/yyyy') as fecha from res_sol_proc where numero_solicitud = "+numeroSolicitud;
					pst2= con.prepareStatement(cadena);
					rs2=pst2.executeQuery();
					if(rs2.next())
					{
						if(rs2.getObject(1)!=null&&!UtilidadTexto.isEmpty(rs2.getString(1))){
							valor=rs2.getString(1);
						}
					}
				}
				else if(estado==ConstantesBD.codigoEstadoHCAdministrada)
				{
					cadena=" SELECT to_char(fecha_grabacion,'dd/mm/yyyy') AS fecha from admin_medicamentos where numero_solicitud ="+numeroSolicitud+" order by 1 desc";
					pst2= con.prepareStatement(cadena);
					rs2=pst2.executeQuery();
					if(rs2.next())
					{
						if(rs2.getObject(1)!=null&&!UtilidadTexto.isEmpty(rs2.getString(1))){
							valor=rs2.getString(1);
						}
					}
				}
				//esto incluye las interconsultas diferetes a solicitidad. en el documento no se especifica entonces retorno la fecha de solicitud.
				else 
				{
					valor=rs.getString("fechasolicitud");
				}
			}
			
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerFechaCalculoCargo");
		return valor;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		int valor=ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarConveniosPlanEspecialPacienteStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				valor=rs.getInt("planEspecialConvenio");
			rs.close();
			ps.close();
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return valor;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoSubCuentasTotalCero(	Connection con, int codigoIngreso) 
	{
		String consulta="UPDATE sub_cuentas set facturado='"+ConstantesBD.acronimoNo+"' WHERE ingreso=? and facturado='"+ConstantesBD.acronimoSi+"' and (SELECT count(1) from facturas f WHERE f.sub_cuenta=sub_cuentas.sub_cuenta and f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+")=0 "  ;
		
		logger.info("\n\n*****ACTUALIZAR EL ESTADO DE FACTURADO DE LAS SUBCUENTAS CON TOTAL CERO --->"+ consulta+"->ing="+codigoIngreso);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * Actualizar el numero de autorizacion de la tabla det_cargos
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean actualizarNumeroAutorizacion(Connection con, HashMap parametros) 
	{
		String consulta="UPDATE det_cargos set nro_autorizacion= '"+parametros.get("numero_autorizacion").toString()+"' WHERE codigo_detalle_cargo = ? ";
		
		logger.info("\n\n*****ACTUALIZAR EL ESTADO NUMERO DE AUTORIZACION --->"+ consulta+" ->codigo_detalle_cargo="+parametros.get("codigo_det_cargo").toString());
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, Utilidades.convertirALong(parametros.get("codigo_det_cargo").toString()));
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizacionesEstancia
	 * @return
	 */
	public static boolean actualizarCargoDetAutorizacionesEstancia(	Connection con, double codigoDetalleCargo, ArrayList<Integer> codDetalleAutorizacionesEstancia) throws BDException
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try	{
			Log4JManager.info("############## Inicio actualizarCargoDetAutorizacionesEstancia");
			if(codDetalleAutorizacionesEstancia.size()>0)
			{	
				String codigo= codigoDetalleCargo>0?codigoDetalleCargo+"":"null";
				String consulta="UPDATE manejopaciente.det_autorizaciones_estancia set det_cargo= "+Utilidades.convertirALong(codigo)+" WHERE codigo in ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codDetalleAutorizacionesEstancia)+") ";
				pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.executeUpdate();
			}
			resultado=true;
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin actualizarCargoDetAutorizacionesEstancia");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @param codDetalleAutorizaciones
	 * @return
	 */
	public static boolean actualizarCargoDetAutorizaciones(Connection con,double codigoDetalleCargo,ArrayList<Integer> codDetalleAutorizaciones) throws BDException
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try	{
			Log4JManager.info("############## Inicio actualizarCargoDetAutorizaciones");
			if(codDetalleAutorizaciones.size()>0)
			{	
				String codigo= codigoDetalleCargo>0?codigoDetalleCargo+"":"null";
				String consulta="UPDATE manejopaciente.det_autorizaciones set det_cargo= "+Utilidades.convertirALong(codigo)+" WHERE codigo in ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codDetalleAutorizaciones)+") ";
				pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.executeUpdate();
			}
			resultado=true;
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin actualizarCargoDetAutorizaciones");
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static ArrayList<Integer> cargarDetAutorizacionesEstanciaXCargo(Connection con, double codigoDetalleCargo) throws BDException
	{
		ArrayList<Integer> array= new ArrayList<Integer>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio cargarDetAutorizacionesEstanciaXCargo");
			String consulta="SELECT codigo FROM manejopaciente.det_autorizaciones_estancia where det_cargo= "+(long)codigoDetalleCargo;
			
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs= pst.executeQuery();
			while(rs.next())
			{
				array.add(rs.getInt(1));
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargarDetAutorizacionesEstanciaXCargo");
		return array;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static ArrayList<Integer> cargarDetAutorizacionesXCargo(Connection con, double codigoDetalleCargo) throws BDException
	{
		ArrayList<Integer> array= new ArrayList<Integer>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio cargarDetAutorizacionesXCargo");
			String consulta="SELECT codigo FROM manejopaciente.det_autorizaciones where det_cargo= "+(long)codigoDetalleCargo;
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs= pst.executeQuery();
			while(rs.next())
			{
				array.add(rs.getInt(1));
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargarDetAutorizacionesXCargo");
		return array;
	}
	
	/**
	 * Insertar cargos articulos consumo
	 * @param con
	 * @param dto
	 * @param loginUsuario
	 * @return
	 */
	public static double insertarDetalleCargosArtConsumos(Connection con, DtoDetalleCargoArticuloConsumo dto, String loginUsuario ) throws BDException
	{
		int secuenciaDetalleCargos=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		
		try {
			Log4JManager.info("############## Inicio insertarDetalleCargosArtConsumos");
			secuenciaDetalleCargos=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_det_cargos_art_consumo");
			pst =  con.prepareStatement(insertarDetalleCargosConsumoArt,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, secuenciaDetalleCargos);
			pst.setLong(2, (long)dto.getDetalleCargo());
			pst.setInt(3, dto.getCodigoArticulo());
			pst.setInt(4, dto.getCantidad());
			pst.setDouble(5, dto.getValorUnitario());
			pst.setDouble(6, dto.getValorTotal());
			
			if(dto.getPorcentaje()>0){
				pst.setDouble(7, dto.getPorcentaje());
			}
			else{
				pst.setNull(7, Types.NUMERIC);
			}
			pst.setString(8, loginUsuario);
			
			if(pst.executeUpdate()<=0){
				secuenciaDetalleCargos=ConstantesBD.codigoNuncaValido;
			}	
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarDetalleCargosArtConsumos");
		
		return secuenciaDetalleCargos;
	}
	
	
	/**
	 * metodo que carga la informacion del detalle de cargo dato el mismo dto como criterio de busqueda
	 * @param con
	 * @param criteriosBusquedaDtoDetalleCargo
	 * @return
	 */
	public static ArrayList<DtoDetalleCargoArticuloConsumo> cargarDetalleCargosArticuloConsumo(Connection con, DtoDetalleCargoArticuloConsumo criteriosBusquedaDtoDetalleCargoArtConsumo)
	{
		ArrayList<DtoDetalleCargoArticuloConsumo> arrayDtoDCargo= new ArrayList<DtoDetalleCargoArticuloConsumo>();
		String consultaDetalle= "SELECT " +
										"codigo as codigo, " +					//1
										"det_cargo as det_cargo , " +			//2
										"articulo as articulo, " +				//3
										"cantidad as cantidad, " +				//4
										"valor_unitario as valor_unitario, " +	//5
										"valor_total as valor_total, " +		//6
										"coalesce(porcentaje, "+ConstantesBD.codigoNuncaValido+") as porcentaje " +			//7
									"FROM " +
										"facturacion.det_cargos_art_consumo " +
									"WHERE 1=1 ";
		
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getCodigo()>0)
			consultaDetalle+=" AND codigo = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getCodigo();
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getDetalleCargo()>0)
			consultaDetalle+=" AND det_cargo = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getDetalleCargo();
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getCodigoArticulo()>0)
			consultaDetalle+=" AND articulo = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getCodigoArticulo();
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getCantidad()>0)
			consultaDetalle+=" AND cantidad = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getCantidad();
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getValorUnitario()>0)
			consultaDetalle+=" AND valor_unitario = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getValorUnitario();
		if(criteriosBusquedaDtoDetalleCargoArtConsumo.getValorTotal()>0)
			consultaDetalle+=" AND valor_total = "+criteriosBusquedaDtoDetalleCargoArtConsumo.getValorTotal();
		
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CONSULTA DETALLE CARGO-->"+consultaDetalle);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetalleCargoArticuloConsumo dtoDCargo= new DtoDetalleCargoArticuloConsumo();
				dtoDCargo.setCodigo(rs.getDouble("codigo"));
				dtoDCargo.setDetalleCargo(rs.getDouble("det_cargo"));
				dtoDCargo.setCodigoArticulo(rs.getInt("articulo"));
				dtoDCargo.setCantidad(rs.getInt("cantidad"));
				dtoDCargo.setValorUnitario(rs.getDouble("valor_unitario"));
				dtoDCargo.setValorTotal(rs.getDouble("valor_total"));
				dtoDCargo.setPorcentaje(rs.getDouble("porcentaje"));
				arrayDtoDCargo.add(dtoDCargo);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error en cargarCargos==> "+e);
			e.printStackTrace();
		}
		return arrayDtoDCargo;
	}
	
	/**
	 * 
	 * 
	 * @param numeroSolicitud
	 * @param estado
	 * @param con
	 * @return
	 */
	public static boolean  modificarEstadosCargosCitas(int numeroSolicitud, int estado, String facturado , String eliminado ,Connection con)
	{
		
		boolean retorna= false;
		String consultaStr= "update facturacion.det_cargos set estado="+estado+" where solicitud = "+numeroSolicitud+" and facturado = '"+facturado+"' and eliminado='"+eliminado+"'";
		
		logger.info("*****************************************************************************************");
		logger.info("********************************************-	MODIFCAR ESTADO cargos citas  -*********************************************");
		logger.info("Consulta"+consultaStr);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			retorna=ps.executeUpdate()>0;
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}	
		return retorna;
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCentroAtencionCargoSolicitud(Connection con,int numeroSolicitud) throws BDException
	{
		int retorna= 0;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerCentroAtencionCargoSolicitud");
			String consultaStr="select cc.centro_atencion from ordenes.solicitudes s INNER JOIN administracion.centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) where s.numero_solicitud=? ";
			
			pst= con.prepareStatement(consultaStr);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			if(rs.next()){
				retorna= rs.getInt(1);
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerCentroAtencionCargoSolicitud");
		return retorna;
	}
	
	/**
	 * 
	 * @param centroCosto
	 * @return
	 */
	public static int obtenerCentroAtencionCentroCostoSolicitadoCargo(int centroCosto)
	{
		int retorna= 0;
		String consultaStr="select cc.centro_atencion from administracion.centros_costo cc where cc.codigo=? ";
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, centroCosto);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getInt(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return retorna;
	}


	/**
	 * 
	 * Metodo para obtener los cargos a partir del numero solicitudes
	 * @param con
	 * @param listaSolicitudes
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<BigDecimal> obtenerCodigosPkCargosDadoSolicitudes(Connection con, ArrayList<BigDecimal> listaSolicitudes) 
	{
		ArrayList<BigDecimal> retorna= new ArrayList<BigDecimal>();
		String consultaStr="select codigo_detalle_cargo from det_cargos where solicitud in("+UtilidadTexto.convertirArrayBigDecimalACodigosSeparadosXComas(listaSolicitudes)+") ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna.add(rs.getBigDecimal(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return retorna;
	}
	
	/**
	 * Metodo que se encarga de obtener el (valor_ajuste) de acuerdo al codigo_excepcion
	 * y asi saber si el campo base_excepcion es requerido para otras consultas.
	 *  
	 * @param codigo_excepcion
	 * @return true or false
	 * @author Camilo Gómez
	 */
	public static boolean obtenerBaseExcepcion(Connection con,int codigoExcepcion,String baseExcepcion) throws BDException
	{
		boolean retorna= false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerBaseExcepcion");
			String consulta="select valor_ajuste from agru_art_exce_tari_cont where codigo_excepcion=? and base_excepcion=? ";
			
			if(baseExcepcion.equals(ConstantesIntegridadDominio.acronimoValorFijo))
				baseExcepcion = ConstantesIntegridadDominio.acronimoTarifa;
			
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoExcepcion);
			pst.setString(2, baseExcepcion);
			rs=pst.executeQuery();
			if(rs.next())
			{
				retorna=true;
			}
		} 
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerBaseExcepcion");
		return retorna;
	}
	
	/**
	 * Método obtiene todas las subcuentas del paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @return
	 */
	public static ArrayList<Integer> consultarSubCuentasConvenio(Connection con, int codigoSubcuenta){
		
		ArrayList<Integer> subCuentas = new ArrayList<Integer>();
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
			
			String consultaSubCuentas="SELECT sc.sub_cuenta as subcuenta FROM sub_cuentas sc,(SELECT ingreso as ingreso,convenio as convenio FROM sub_cuentas WHERE sub_cuenta=?) sc1 WHERE sc.ingreso = sc1.ingreso and sc.convenio=sc1.convenio";

			ps = new PreparedStatementDecorator(con.prepareStatement(consultaSubCuentas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoSubcuenta);
			rs = new ResultSetDecorator(ps.executeQuery());
			logger.info("consulta CantidadInclusionesOrden (subcuentas)-> " + consultaSubCuentas + "--" + codigoSubcuenta);
			
				if(rs.next())
					subCuentas.add(rs.getInt("subcuenta"));
			
		} catch (SQLException e) {
			logger.error("ERROR SQLException - consultarSubCuentasConvenio: " + e);
		}catch(Exception e){
			logger.error("ERROR Exception - consultarSubCuentasConvenio ", e);
		}finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		return subCuentas;
	}
	
	/**
	 * Método obtiene cantidad de Inclusiones de un articulo por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoServicio
	 * @return
	 */
	public static int consultarCantidadInclusionesExclusionesOrdenArticulo(Connection con, int codigoSubcuenta, int codigoArticulo){
		
		ArrayList<Integer> subCuentas = null;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;

		try {		

			subCuentas = consultarSubCuentasConvenio(con, codigoSubcuenta);
			
			if(subCuentas.size() > 0){
				
				String consultaCantidadInclusiones = "SELECT CASE WHEN SUM(cantidad_cargada) IS NULL THEN 0 ELSE SUM(cantidad_cargada) END AS cantidad_total FROM det_cargos WHERE sub_cuenta IN ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(subCuentas)+") AND articulo = ? AND estado="+ConstantesBD.codigoEstadoFExento;
				
				ps = new PreparedStatementDecorator(con.prepareStatement(consultaCantidadInclusiones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoArticulo);
				rs = new ResultSetDecorator(ps.executeQuery());
				logger.info("consulta CantidadInclusionesOrdenArticulo (cantidadCargada)-> " + consultaCantidadInclusiones + "--" + codigoArticulo);

					if(rs.next()){
						logger.info("total-> " + rs.getInt("cantidad_total"));
						return rs.getInt("cantidad_total");}
			}

		} catch (SQLException e) {
			logger.error("ERROR SQLException - consultarCantidadInclusionesExclusionesOrdenArticulo: " + e);
		}catch(Exception e){
			logger.error("ERROR Exception - consultarCantidadInclusionesExclusionesOrdenArticulo ", e);
		}finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método obtiene cantidad de Inclusiones de un servicio por paciente
	 * @param con
	 * @param codigoSubcuenta
	 * @param codigoServicio
	 * @return
	 */
	public static int consultarCantidadInclusionesExclusionesOrdenServicio(Connection con, int codigoSubcuenta, int codigoServicio){
		
		ArrayList<Integer> subCuentas = null;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
			
			subCuentas = consultarSubCuentasConvenio(con, codigoSubcuenta);

			if(subCuentas.size() > 0){
				
				String consultaCantidadInclusiones = "SELECT CASE WHEN SUM(cantidad_cargada) IS NULL THEN 0 ELSE SUM(cantidad_cargada) END AS cantidad_total FROM det_cargos WHERE sub_cuenta IN ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(subCuentas)+") AND servicio = ? AND estado="+ConstantesBD.codigoEstadoFExento;
				
				ps = new PreparedStatementDecorator(con.prepareStatement(consultaCantidadInclusiones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoServicio);
				rs = new ResultSetDecorator(ps.executeQuery());
				logger.info("consulta CantidadInclusionesOrdenServicio (cantidadCargada)-> " + consultaCantidadInclusiones + "--" + codigoServicio);

					if(rs.next()){
						logger.info("total-> " + rs.getInt("cantidad_total"));
						return rs.getInt("cantidad_total");}
			}

		} catch (SQLException e) {
			logger.error("ERROR SQLException - consultarCantidadInclusionesExclusionesOrdenServicio: " + e);
		}catch(Exception e){
			logger.error("ERROR Exception - consultarCantidadInclusionesExclusionesOrdenServicio ", e);
		}finally {
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return ConstantesBD.codigoNuncaValido;
	}
}