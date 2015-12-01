package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoPresupuestoPrecontratado;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseControlAnticiposContratoDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoCitasPresupuestoOdo;
import com.princetonsa.dto.odontologia.DtoConcurrenciaPresupuesto;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoFirmasContratoOtrosiInst;
import com.princetonsa.dto.odontologia.DtoHistoricoIngresoPresupuesto;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPrespuesto;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuContratoOdoImp;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.enu.general.EnumTipoModificacion;


/**
 * 
 * @author axioma
 *
 */
public class SqlBasePresupuestoOdontologico {

	
	
private static Logger logger = Logger.getLogger(SqlBasePresupuestoOdontologico.class);
	

	/*
	 *INSERTAR PRESUPUESTO  
	 */
	private static String insertarStr=" insert into odontologia.presupuesto_odontologico " +
									" ( codigo_pk , "+ //1
									"	estado ,"+ //2
									"	motivo ,"+ //3
									"	especialidad, "+ //4
									"	fecha_modifica , "+ //5
									"	hora_modifica , "+ //6
									"	usuario_modifica ,"+ //7
									"	fecha_generacion ,"+ //8
									"	hora_generacion ,"+ //9
									"	usuario_generacion ,"+ //10
									"	consecutivo , "+  //11
									"	codigo_paciente , "+ //12
									"	ingreso  , "+ //13
									"	cuenta  , "+ //14
									"	plan_tratamiento , "+//15
									"	institucion, "+// 16
									"	centro_atencion ) "+//17
										" values " +
								    " ( ? , ? , ? , ? , ? , ? ,? , ? , ? , ? , ?, ? , ?, ? , ? , ? , ? ) ";								    

	
	
	/*
	 * INSERTAR DETALLE ODONTOLOGICO PRESUPUESTO PROGRAMA SERVICIO 
	 */

	private static String insertStrDetalleProgramaServicio=" insert into odontologia.presupuesto_odo_prog_serv (" +
			"codigo_pk ," + //1
			" programa ," + //2
			" servicio ," + //3
			" presupuesto, " + //4
			" fecha_modifica, " + //5
			" hora_modifica , " + //6
			" usuario_modifica , " + //7
			" cantidad, " +		//8
			" inclusion ) " +//9
 			" values " +
 			"(? , ? , ?, ? , ? , ?, ? , ?, ? )"; //9
	
	
	
	
	/*
	 * INSERTAR PROGRAMAS CONVENIOS ODONTOLOGICOS
	 */
	private static String insertStrDetallePresupuestoConvenio= " insert into odontologia.presupuesto_odo_convenio ("+
																" codigo_pk ,"+ //1
																" presupuesto_odo_prog_serv , "+//2
																" valor_unitario , "+//3
																" convenio ,  "+//4
																" fecha_modifica , "+//5
																" hora_modifica , "+//6
																" usuario_modifica , "+//7
																" contratado," + //8
																" det_promocion ," + //9
																" valor_descuento_prom ," + //10
																"porcentaje_descuento_prom , " + //11
																"valor_honorario_prom , " + //12
																" advertencia_prom ," + //13
																"dcto_comercial_unitario ," + //14
														        "error_calculo_tarifa," +//15
														        "contrato, " +//16
														        "serial_bono, "+//17
														        "valor_descuento_bono, "+//18
														        "adventencia_bono, " +//19
														        "porcentaje_dcto_bono, " +//20
														        "reserva_anticipo," +//21
														        "sel_porcent_prom," +//22
														        "sel_porcent_bono, " +//23
														        "presupuesto_paquete)"+ //24
																" values "+ 
																" ( " +
																"? , ? , " +//2
																"? , ? , " +//4
																"? , ? , " +//6
																"? , ? , " +//8
																"? , ? , " +//10
																"? , ? , " +//12
																"? , ? , " +//14
																"? , ? , " +//16
																"? , ? , " +//18
																"? , ? , " +//20
																"? , ? ," +//22
																"? , ?) ";	//24 
		
	
	/*
	 * INSERTAR PRESUPUESTO PIEZAS  
	 */
	
	private static String insertarStrPresupuestoPiezas= " insert into odontologia.presupuesto_piezas "+
														" (codigo_pk  , "+ //1
														" presupuesto_odo_prog_serv , "+ //2
														" pieza, "+ //3
														" hallazgo, "+ //4
														" seccion, "+ //5
														" superficie , "+ //6
														" fecha_modifica , "+ //7
														" hora_modifica , "+ //8
														" usuario_modifica, " +//9
														" num_superficies )" + //10
														" values" +
														" (? , ? , ? , ?  ,?  , ? ,?  ,? , ?, ? )";
														

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	private static String insertarStrPresupuestoDescuento = "insert into odontologia.presupuesto_dcto_odon(  codigo_pk , " + //1
														" 	fecha_solicitad , " +//2
														"	hora_solicita , " + //3
														" 	usuario_solicitad ," +//4
														" 	presupuesto , " +//5
														" 	det_dcto_odo , " +//6
														"  	fecha_modifica  , " +//7
														"	hora_modifica ," +//8
														"  	usuario_modifica," + //9
														"	valor_descuento, " + //10
														"	estado,  " + //11
														"	consecutivo, "+ // 12
														"	porcentaje_dcto, "+//13
														"	motivo, "+//14
														"	observaciones, "+//15
														"	det_dcto_odo_aten, " +//16
														"	inclusion"+//17
														" ) values ("+ 
														"? ," +//1
														" ? ," +//2
														" ? ," +//3
														" ? ," +//4
														" ? ," +//5
														" ? ," +//6
														" ? ," +//7
														" ? ," +//8
														" ? ," +//9
														" ? ," +//10
														" ? ," +//11
														" ? ," +//12
														" ? ," +//13
														" ? ," +//14
														" ? ," +//15
														" ?, " +//16
														" ?)";//17
					
	
	/**
	 * insert del plan tratamiento del presupuesto
	 */
	private static final String insertarPlanTtoPresupuesto= "INSERT INTO odontologia.presu_plan_tto_prog_ser " +
															"(" +
																"codigo_pk, " +//1
																"det_plan_tratamiento, " +//2
																"programa, " +//3
																"servicio, " +//4
																"presupuesto, " +//5
																"usuario_modifica, " +//6
																"fecha_modifica, " +//7
																"hora_modifica, " +//8
																"programa_servicio_plan_t, " +//9
																"activo "+//10
															") values " +
															"(" +
															"?,?," +//2
															"?,?," +//4	
															"?,?," +//6
															"?,?," +//8
															"?,? " +//10
															")";
	
	/**
	 * 	
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double guardarPresupuesto(DtoPresupuestoOdontologico dto , Connection con)
	{
		logger.info("-***********************************************************************************************");
		logger.info("--------------------------------------GUARDAR PRESUPUESTOS-----------------------------");
		
		//logger.info(UtilidadLog.obtenerString(dto, true));
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presupuesto_odontologico"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getEstado());
			if(dto.getMotivo().getCodigo()> 0)
			{
			ps.setInt(3, dto.getMotivo().getCodigo());
			}
			else
			{
			ps.setNull(3, Types.INTEGER);	
			}
			
			if(dto.getEspecialidad().getCodigo()> 0)
			{
			ps.setInt(4, dto.getEspecialidad().getCodigo());	
			}
			else
			{
				ps.setNull(4, Types.INTEGER);
			}	
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(8, dto.getFechaUsuarioGenera().getFechaModificaFromatoBD());
			ps.setString(9, dto.getFechaUsuarioGenera().getHoraModifica());
			ps.setString(10, dto.getFechaUsuarioGenera().getUsuarioModifica());
			
			ps.setDouble(11, secuencia);
			ps.setInt(12, dto.getCodigoPaciente().getCodigo());
			
			if(dto.getIngreso().intValue()> 0)
			{
				ps.setInt(13, dto.getIngreso().intValue());
			}
			else 
			{
				ps.setNull(13, Types.INTEGER);
			}
			ps.setInt(14, dto.getCuenta().intValue());
			//PLAN DE TRATAMIENTO
			ps.setBigDecimal(15,dto.getPlanTratamiento());
			ps.setInt(16, dto.getInstitucion());
			ps.setInt(17, dto.getCentroAtencion().getCodigo());
			
			if(ps.executeUpdate()>0)
				{
					ps.close();
					return secuencia;
				}
			
		
			ps.close();
		}
		catch (SQLException e) 
		{
			
			logger.error("ERROR en insert " + e);
		}
		
		
		return secuencia;
		
	}

	/**
	 * 
	 * @param dto
	 * @param estadosPresupuesto 
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto ( Connection  con, DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto, boolean estadosNotIn ){
	 	ArrayList<DtoPresupuestoOdontologico> arrayDto= new ArrayList<DtoPresupuestoOdontologico>();
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS-----------------------------------------");
	 	
	 	//logger.info("DTO PRESUPUESTO--->"+UtilidadLog.obtenerString(dto, true));
	 	
	 	String consultaStr=" select " +
								 	"  po.codigo_pk as codigoPk, "+ //1
									"	po.estado as estado ,"+ //2
									"	po.motivo as motivo  ,"+ //3
									"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=po.motivo ) as nombreMotivo  ,"+ //4
									"	po.especialidad as especialidad, "+ //4
									"	po.fecha_modifica as fechaModifica , "+ //5
									"	po.hora_modifica as horaModifica, "+ //6
									"	po.usuario_modifica as usuarioModifica,"+ //7
									"	po.consecutivo as consecutivo, "+  //8
									"	po.codigo_paciente as codigoPaciente , "+ //9
									"	po.ingreso as ingreso  , "+ //10
									"	po.cuenta  as cuenta, "+ //11
									"	po.plan_tratamiento as planTramiento  , "+//12
									"	po.institucion as institucion , "+// 13
									"	po.centro_atencion as centroAtencion,  " +//14
									"	to_char(po.fecha_generacion, 'DD/MM/YYYY') as fecha_generacion, " +//15
									"	po.hora_generacion as hora_generacion, " +//16
									"	po.usuario_generacion as usuario_generacion, "+//17
									 "	administracion.getnombreusuario(po.usuario_modifica) as nombre_usuario " + // 18 
										" from " +
										"" +
										"odontologia.presupuesto_odontologico po " +
																" 	where " +
									"	1=1 ";
	 	
	 	consultaStr+=(dto.getCodigoPK().doubleValue()> 0)?" AND po.codigo_pk="+dto.getCodigoPK():"";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getEstado())?"":" AND po.estado='"+dto.getEstado()+"'";
		consultaStr+=(dto.getMotivo().getCodigo()> 0 )?" AND po.motivo="+dto.getMotivo().getCodigo():"";
		consultaStr+=(dto.getEspecialidad().getCodigo()> 0 )?" AND po.especialidad="+dto.getEspecialidad().getCodigo():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModifica())?"":" AND po.fecha_modifica||''<='"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" AND po.hora_modifica='"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" AND po.usuario_modifica='"+dto.getUsuarioModifica()+"'";
		consultaStr+=(dto.getConsecutivo().doubleValue()> 0 )?" AND po.consecutivo ="+dto.getConsecutivo().doubleValue():"";
		consultaStr+=(dto.getCodigoPaciente().getCodigo() > 0 )?" AND po.codigo_paciente ="+dto.getCodigoPaciente().getCodigo():"";
		consultaStr+=(dto.getCuenta().intValue() > 0)?" AND po.cuenta ="+dto.getCuenta().intValue():"";
		consultaStr+=(dto.getInstitucion() > 0 )?"  AND po.institucion ="+dto.getInstitucion():"";
		consultaStr+=(dto.getIngreso().intValue() > 0)?" AND po.ingreso ="+dto.getIngreso().intValue():"";
		consultaStr+=(dto.getPlanTratamiento().doubleValue()>0)?" AND  po.plan_tratamiento="+dto.getPlanTratamiento():"";
		
		if(estadosPresupuesto.size()>0)
		{
			if(estadosNotIn)
			{
				consultaStr+=" AND po.estado not in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosPresupuesto)+") ";
			}
			else
			{
				consultaStr+=" AND po.estado in ("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estadosPresupuesto)+") ";	
			}
			
		}
		
	    logger.info("\n\n\n\n\n SQL CARGAR PRESUPUESTO::: / "+consultaStr+"\n\n\n\n\n\n\n\n");
	    
	    try 
	    {
			
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by po.consecutivo desc, po.fecha_modifica desc, po.hora_modifica desc ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	    	while(rs.next())
	    	{
	    		DtoPresupuestoOdontologico newdto = new DtoPresupuestoOdontologico();
	    		
	    		newdto.setCodigoPK( new BigDecimal(rs.getDouble("codigoPk")));
				newdto.setEstado(rs.getString("estado"));
				newdto.setMotivo(new InfoDatosInt(rs.getInt("motivo"),rs.getString("nombreMotivo")));
				newdto.getEspecialidad().setCodigo(rs.getInt("especialidad"));
				newdto.getUsuarioModifica().setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaModifica")));
				newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.getUsuarioModifica().setNombreUsuarioModifica(rs.getString("nombre_usuario"));
				
				
				
				newdto.setConsecutivo(rs.getBigDecimal("consecutivo"));
				newdto.getCodigoPaciente().setCodigo(rs.getInt("codigoPaciente"));
				newdto.setIngreso(new BigDecimal(rs.getInt("ingreso")));
				newdto.setCuenta(new BigDecimal(rs.getInt("cuenta")));
				newdto.setInstitucion(rs.getInt("institucion"));
				newdto.getCentroAtencion().setCodigo(rs.getInt("centroAtencion"));
				newdto.setPlanTratamiento(rs.getBigDecimal("planTramiento"));
				newdto.setInstitucion(rs.getInt("institucion"));
				newdto.setCentroAtencion(new InfoDatosInt(rs.getInt("centroAtencion")));
				newdto.setFechaUsuarioGenera(new DtoInfoFechaUsuario(rs.getString("hora_generacion"), rs.getString("fecha_generacion"), rs.getString("usuario_generacion")));
				
				String consultaCentroAtencion=
						"SELECT " +
							"co.centro_costo AS centro_costo " +
						"FROM " +
								"odontologia.plan_tratamiento pt " +
							"INNER JOIN " +
								"odontologia.citas_odontologicas co " +
									"ON(co.codigo_pk=pt.cita) " +
							"INNER JOIN " +
								"odontologia.agenda_odontologica ao " +
									"ON(ao.codigo_pk=co.agenda) " +
							"WHERE pt.codigo_pk=?";
				PreparedStatementDecorator psCentroCosto = new PreparedStatementDecorator(con, consultaCentroAtencion);
				psCentroCosto.setInt(1, newdto.getPlanTratamiento().intValue());
				ResultSetDecorator rsCentroCosto = new ResultSetDecorator(psCentroCosto.executeQuery());
				if(rsCentroCosto.next())
				{
					newdto.setCentroCosto(rsCentroCosto.getInt("centro_costo"));
				}
				else
				{
					/*
					 * Log4JManager.error("No se encontró relación de la cita evolucionada");
					 * return null;
					 */
					
				}
				
				arrayDto.add(newdto);
	    	}
	    	SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> ",e);
		}
		
		return arrayDto;
	}
	
	
	
	
	/*------------------------------------------------------
	 * CARGA LOS TOTALES DEL LOS CONVENIOS DEL PRESUPUESTO
	 * **************************************************
	 */
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoTotalConvenio> cargarTotalesPresupuetoConvenio ( DtoPresupuestoTotalConvenio dto){
		ArrayList<DtoPresupuestoTotalConvenio> arrayDto= new ArrayList<DtoPresupuestoTotalConvenio>();
		
		String consultaStr=
			"SELECT " +
				"poc.convenio as convenio, "+ 
				"poc.contrato as contrato, "+
				"facturacion.getnombreconvenio(poc.convenio) as nombre_convenio, "+ 
				"poc.valor_subtotal_sin_contratar as valor_subtotal_sin_contratar, "+ 
				"poc.presupuesto as presupuesto, "+ 
				"poc.valor_subtotal_contratado as valor_subtotal_contratado, " +
				"poc.valor_sub_cont_no_inclusiones AS valor_sub_cont_no_inclusiones, " +
				"con.es_conv_tar_cli AS es_conv_tar_cli "+
			" FROM odontologia.view_presupuesto_totales_conv poc " +
			" INNER JOIN " +
				"convenios con " +
					"ON(con.codigo=poc.convenio) " +
			"WHERE " +
				"1=1 ";
		
		consultaStr+=" AND poc.presupuesto="+dto.getPresupuesto();
		consultaStr+=" ORDER BY facturacion.getnombreconvenio(poc.convenio)  ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info(consultaStr);
			
			while(rs.next())
			{
				DtoPresupuestoTotalConvenio newdto = new DtoPresupuestoTotalConvenio();
				newdto.setConvenio(new InfoDatosInt(rs.getInt("convenio"),rs.getString("nombre_convenio")));
				newdto.setValorSubTotalSinContratar(rs.getBigDecimal("valor_subtotal_sin_contratar"));
				newdto.setSubTotalContratadoSinDescuentos(rs.getBigDecimal("valor_sub_cont_no_inclusiones"));
				newdto.setPresupuesto(rs.getBigDecimal("presupuesto"));
				newdto.setContrato(rs.getInt("contrato"));
				newdto.setValorSubTotalContratado(rs.getBigDecimal("valor_subtotal_contratado"));
				newdto.setEsConvenioTarjetaCliente(UtilidadTexto.getBoolean(rs.getString("es_conv_tar_cli")));
				if(newdto.getPresupuesto().intValue()>0 && newdto.getValorSubTotalContratado().intValue()>0)
					newdto.setValorTotalContratado(obtenerTotalPresupuestoConDctoOdon(newdto.getPresupuesto()));
				else
					newdto.setValorTotalContratado(rs.getBigDecimal("valor_subtotal_contratado"));	
				
				arrayDto.add(newdto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga de los convenios del presupuesto",e);
		}
		
		return arrayDto;
	}
	


	/**
	 * Método que devuelve el valor inicial contratado en el presupuesto
	 * 
	 * @param codigoPresupuesto
	 * @return valor inicial del presupuesto
	 */
	public static BigDecimal obtenerValorContratadoInicialPresupuesto (long codigoPresupuesto){
		
		BigDecimal valorInicialPresupuesto = new BigDecimal(0);
		
		String consultaStr=
			"SELECT " +
				"poc.valor_total_presu_inicial AS valor_total_presu_inicial " +
			" FROM odontologia.view_presupuesto_totales_conv poc " +
			"WHERE " +
				"poc.presupuesto="+codigoPresupuesto;
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info(consultaStr);
			
			while(rs.next())
			{
				valorInicialPresupuesto = valorInicialPresupuesto.add(rs.getBigDecimal("valor_total_presu_inicial"));
			}

			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga de los convenios del presupuesto",e);
		}
		
		return valorInicialPresupuesto;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuesto( DtoPresupuestoOdontologico dto)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------ELIMINAR PRESUPUESTO-----------------------------------------");
		
		String consultaStr="DELETE FROM  odontologia.presupuesto_odontologico WHERE 1=1 ";
		

		consultaStr+=(dto.getCodigoPK().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigoPK():"";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getEstado())?"":" AND estado='"+dto.getEstado()+"'";
		consultaStr+=(dto.getMotivo().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND motivo="+dto.getMotivo().getCodigo():"";
		consultaStr+=(dto.getEspecialidad().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND especialidad="+dto.getEspecialidad().getCodigo():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModifica())?"":" AND fecha_modifica='"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" AND hora_modifica='"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" AND usuario_modifica='"+dto.getUsuarioModifica()+"'";
		consultaStr+=(dto.getConsecutivo().doubleValue()>ConstantesBD.codigoNuncaValido)?" AND consecutivo ="+dto.getConsecutivo().doubleValue():"";
		consultaStr+=(dto.getCodigoPaciente().getCodigo() >ConstantesBD.codigoNuncaValido)?" AND codigo_paciente ="+dto.getCodigoPaciente().getCodigo():"";
		consultaStr+=(dto.getCuenta().intValue() >ConstantesBD.codigoNuncaValido)?" AND cuenta ="+dto.getCuenta().intValue():"";
		consultaStr+=(dto.getInstitucion() >ConstantesBD.codigoNuncaValido)?"  AND institucion ="+dto.getInstitucion():"";
	    
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ");
			e.printStackTrace();
		}
			return false;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuesto( DtoPresupuestoOdontologico dto , Connection con , ArrayList<String> arrayEstados)
	{
		logger.info("-****************************************************************************************************");
		//logger.info("---------------------------------------MODIFICAR PRESUPUESTO-----------------------------------------"+UtilidadLog.obtenerString(dto, true));
		
		boolean retorna=false;
		
		String consultaStr ="UPDATE odontologia.presupuesto_odontologico set" +
				                                                        
		                                                                " codigo_pk=codigo_pk , "+
																		"	estado=? ,"+ //1
																		"	motivo=? ,"+ //2
																		"	especialidad=?, "+ //3
																		"	fecha_modifica=? , "+ //4
																		"	hora_modifica=? , "+ //5
																		"	usuario_modifica=? ,"+ //6
																		"	consecutivo=? , "+  //7
																		"	codigo_paciente=? , "+ //8
																		"	ingreso=?  , "+ //9
																		"	cuenta=?  , "+ //10
																		"	plan_tratamiento=? , "+//11
																		"	institucion=?, "+// 12
																		"	centro_atencion=?   "+
																		"  WHERE  1=1 " ;
        if(dto.getCodigoPK().doubleValue() > 0)
        {	
	     consultaStr+=	" and codigo_pk ="+dto.getCodigoPK();//14
        }
        if(dto.getPlanTratamiento().doubleValue() > 0)
        {
          	consultaStr+= " and plan_tratamiento="+dto.getPlanTratamiento();
        }
		
        if(arrayEstados.size() > 0)
		{
        	consultaStr+= " and estado in ("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(arrayEstados)+") ";
        }
	    try 
		{
		
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con,consultaStr);
			
			ps.setString(1, dto.getEstado());
			logger.info("1-->"+dto.getEstado());
			
			if(dto.getMotivo().getCodigo()> 0)
			{
				ps.setInt(2, dto.getMotivo().getCodigo());
			}
			else
			{
				ps.setNull(2, Types.INTEGER);	
			}
			logger.info("2-->"+dto.getMotivo().getCodigo());
			
			if(dto.getEspecialidad().getCodigo()>ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(3, dto.getEspecialidad().getCodigo());	
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}
			logger.info("LA FECHA EN EL MODIFICAR ES*******************************" + dto.getUsuarioModifica().getFechaModificaFromatoBD());
			
			ps.setString(4, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(5, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(6, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setDouble(7, dto.getConsecutivo().doubleValue());
			ps.setInt(8, dto.getCodigoPaciente().getCodigo());
			
			if(dto.getIngreso().intValue()>ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(9, dto.getIngreso().intValue());
			}
			else 
			{
				ps.setNull(9, Types.INTEGER);
			}
			ps.setInt(10, dto.getCuenta().intValue());
			//PLAN DE TRATAMIENTO
			ps.setBigDecimal(11,dto.getPlanTratamiento());
			ps.setInt(12, dto.getInstitucion());
			ps.setInt(13, dto.getCentroAtencion().getCodigo());
			
			
			logger.info("Update  PRESUPUESTO---> \n\n\n"+ps+"\n\n\n");
			retorna=ps.executeUpdate() > 0;
			logger.info("retorna -------------------------------------------------------------------->"+retorna);
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma ", e);
		}
		return retorna;
		
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS PARA LE DETALLE PRESUPUESTO ODONTOLOGICOS PROGRAMA - SERVICIOS
	 * **********************************************************************************************************************************
	 */
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarPresupuestoOdoProgramaServicio(DtoPresuOdoProgServ dto , Connection con)
	{
		
		logger.info("***********************************************************************************************");
		logger.info("********************GUARDAR PRESUPUESTOS PROGRAMA SERVICIO**********************");
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presupuesto_prog_serv"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertStrDetalleProgramaServicio ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuencia);
			
			if(dto.getPrograma().getCodigo()>0)
			{
				ps.setDouble(2, dto.getPrograma().getCodigo());
			}
			else
			{
				ps.setNull(2, Types.NUMERIC);
			}
			
			if(dto.getServicio().getCodigo()>0)
			{
				ps.setInt(3, dto.getServicio().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);	
			}
			
			if(dto.getPresupuesto().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)
			{
				ps.setDouble(4, dto.getPresupuesto().doubleValue());
			}
			else
			{
				ps.setNull(4, Types.DOUBLE);
			}
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setInt(8, dto.getCantidad());
			
			if(dto.getInclusion().doubleValue()>0)
			{
				ps.setBigDecimal(9, dto.getInclusion());
			}
			else
			{
				ps.setNull(9, Types.NUMERIC);
			}
			
			if(ps.executeUpdate()>0)
			{
			    ps.close();
				return secuencia;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			 e.printStackTrace();
		}
		
		return secuencia;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ ( DtoPresuOdoProgServ dto, Connection con, DtoPresupuestoPiezas dtoPieza){
	 	
		ArrayList<DtoPresuOdoProgServ> arrayDto= new ArrayList<DtoPresuOdoProgServ>();
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS-----------------------------------------");
	 	
	 	//logger.info("DTO PRESUPUESTO--->"+UtilidadLog.obtenerString(dto, true));
	 	
	 	String consultaStr=" select " +
	 								" ps.codigo_pk as codigoPk," +
	 								" ps.programa as programa , " +
	 								" ps.servicio as servicio , " +

									"(select pro.nombre from odontologia.programas pro where pro.codigo  = ps.programa) as nombre_programa ,"+ 
									"facturacion.getnombreservicio(ps.servicio,0)  as nombre_servicio , " +
									"(select pro.codigo_programa from odontologia.programas pro where pro.codigo  = ps.programa) as codigo_programa ,"+ 
								
									"facturacion.getcodigoservicio(ps.servicio,0)  as codigo_servicio , " +
									
	 								" ps.presupuesto  as presupuesto , " +
	 								" ps.fecha_modifica as fechaModifica ," +
	 								" ps.hora_modifica as horaModifica ," +
	 								" ps.usuario_modifica as usuarioModifica, " +
	 								" ps.cantidad as cantidad, " +
	 								" case when ps.programa is not null then (select administracion.getnombreespecialidad(p.especialidad) from odontologia.programas p where p.codigo=ps.programa) else (select administracion.getnombreespecialidad(s.especialidad) from facturacion.servicios s where s.codigo=ps.servicio) end as nombreespecialidad, " +
	 								" coalesce(ps.inclusion,0) as inclusion " +
	 								" from " +
									" odontologia.presupuesto_odo_prog_serv  ps " +
									" @replaceJoin@ "+
									" 	where 1=1 " +
									"  ";
	 	
	 	consultaStr+=(dto.getCodigoPk().doubleValue() > 0)?" AND ps.codigo_pk="+dto.getCodigoPk():"";
	 	consultaStr+=(dto.getPrograma().getCodigo()> 0)?" AND programa="+dto.getPrograma().getCodigo():"";
		consultaStr+=(dto.getServicio().getCodigo()> 0)?" AND servicio="+dto.getServicio().getCodigo():"";
		consultaStr+=(dto.getPresupuesto().doubleValue() > 0)?" AND presupuesto="+dto.getPresupuesto():"";
		consultaStr+=(dto.getCantidad()>ConstantesBD.codigoNuncaValido)?" AND cantidad="+dto.getCantidad():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModificaFromatoBD())?"":" AND fecha_modifica= '"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" AND  hora_modifica= '"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" AND usuario_modifica= '"+dto.getUsuarioModifica().getUsuarioModifica()+"'";
		consultaStr+=(dto.getInclusion().doubleValue()>0)?" AND inclusion= "+dto.getInclusion()+" ":" ";
		
	    if(dtoPieza!=null)
	    {
	    	consultaStr+=(dtoPieza.getPieza().doubleValue()>0)?" and pp.pieza ="+dtoPieza.getPieza()+" ":" and pp.pieza is null ";
	    	consultaStr+=(dtoPieza.getHallazgo().doubleValue()>0)?" and pp.hallazgo ="+dtoPieza.getHallazgo()+" ":" ";
	    	consultaStr+=(dtoPieza.getSuperficie().doubleValue()>0)?" and pp.superficie ="+dtoPieza.getSuperficie()+" ":" and pp.superficie is null ";
	    	consultaStr+=(!UtilidadTexto.isEmpty(dtoPieza.getSeccion()))?" and pp.seccion ='"+dtoPieza.getSeccion()+"' ":" ";
	    	consultaStr= consultaStr.replaceAll("@replaceJoin@", " INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=ps.codigo_pk) ");
	    }
	    else
	    {
	    	consultaStr= consultaStr.replaceAll("@replaceJoin@", " ");
	    }
	    
	    Log4JManager.info("\n\n\n LA CONSULTA PROGRAMAS SERVICIO PRESUPUESTO--->"+consultaStr+"\n\n\n");
	    
	    try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPresuOdoProgServ newdto = new DtoPresuOdoProgServ();
				newdto.setCodigoPk(new BigDecimal(rs.getDouble("codigoPk")));
				newdto.setPrograma(new InfoDatosDouble(rs.getDouble("programa"),rs.getString("nombre_programa") , rs.getString("codigo_programa")) );
				
			    newdto.setServicio(new InfoDatosInt(rs.getInt("servicio"),rs.getString("nombre_servicio"),rs.getString("codigo_servicio")));
				
				newdto.setPresupuesto( new BigDecimal(rs.getDouble("presupuesto")));
				newdto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setCantidad(rs.getInt("cantidad"));
				newdto.setTipoModificacion(EnumTipoModificacion.CARGADO_NO_MODIFICADO);
				newdto.setEspecialidad(rs.getString("nombreespecialidad"));
				newdto.setInclusion(rs.getBigDecimal("inclusion"));
				
				arrayDto.add(newdto);
			 }
			rs.close();
			ps.close();
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ",e);
		}
		
		return arrayDto;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto, Connection con )
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------MODIFICAR PRESUPUESTO PROGRAMA SERVICIO -----------------------------------------");
		boolean retorna=false;
		
		String consultaStr ="UPDATE odontologia.presupuesto_odo_prog_serv set codigo_pk=codigo_pk ";
		consultaStr+=(dto.getPrograma().getCodigo().doubleValue()>0)?",programa="+dto.getPrograma().getCodigo().doubleValue():"";
		consultaStr+=(dto.getServicio().getCodigo()>0)?",servicio="+dto.getServicio().getCodigo():"";
		consultaStr+=" ,fecha_modifica =CURRENT_DATE ";
		consultaStr+=" ,hora_modifica ="+ValoresPorDefecto.getSentenciaHoraActualBD(); //4
		consultaStr+=" ,usuario_modifica ='"+dto.getUsuarioModifica().getUsuarioModifica()+"'";
		consultaStr+=(dto.getCantidad()>0)?",cantidad="+dto.getCantidad():"";
		consultaStr+=" WHERE " +
						" codigo_pk= "+dto.getCodigoPk();
																		
	    logger.info("Update "+consultaStr);
		   
	    try 
		{
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma "+e);
		}
		return retorna;
		
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto, Connection con)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------ELIMINAR PRESUPUESTO PROGRAMA SERVICIO-----------------------------------------");
		
		String consultaStr="DELETE FROM  odontologia.presupuesto_odo_prog_serv WHERE 1=1 ";
		
		consultaStr+=(dto.getCodigoPk().doubleValue()>0)?" AND codigo_pk="+dto.getCodigoPk():"";
	 	consultaStr+=(dto.getPrograma().getCodigo()>0)?" AND programa="+dto.getPrograma().getCodigo():"";
		consultaStr+=(dto.getServicio().getCodigo()>0)?" AND servicio="+dto.getServicio().getCodigo():"";
		consultaStr+=(dto.getPresupuesto().doubleValue()>0)?" AND presupuesto="+dto.getPresupuesto().doubleValue():"";
		consultaStr+=(dto.getCantidad()>0)?" AND cantidad="+dto.getCantidad():"";
		consultaStr+=(dto.getInclusion().doubleValue()>0)?" AND inclusion="+dto.getInclusion():"";
		logger.info("Eliminar -->"+consultaStr);
		
	    try 
	    {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ");
			e.printStackTrace();
		}
		return false;
	}
	

	/*
	 * PRESUPUESTO ODONTOLOGIA CONVENIO 
	 * *****************************************************************************************************************************
	 * 
	 */
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarPresupuestoOdoConvenio(DtoPresupuestoOdoConvenio dto , Connection con)
	{
	
		logger.info("***********************************************************************************************");
		logger.info("********************GUARDAR PRESUPUESTOS PROGRAMA SERVICIO**********************");
	
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
 	
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_presupuesto_odo_convenio"); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertStrDetallePresupuestoConvenio ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuencia);
			
			if(dto.getPresupuestoOdoProgServ().doubleValue() >ConstantesBD.codigoNuncaValidoDouble)
			{
				ps.setDouble(2, dto.getPresupuestoOdoProgServ().doubleValue());
			}
			else
			{
				ps.setNull(2, Types.NUMERIC);
			}
			
			if(dto.getValorUnitario().doubleValue()>ConstantesBD.codigoNuncaValido)
			{
			 ps.setDouble(3, dto.getValorUnitario().doubleValue() );	
			}
			else
			{
				ps.setNull(3, Types.NUMERIC);
			}
			
			if(dto.getConvenio().getCodigo() >ConstantesBD.codigoNuncaValido)
			{
				ps.setDouble(4, dto.getConvenio().getCodigo());
			}
			else
			{
				ps.setNull(4, Types.INTEGER);
			}
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(8, dto.getContratado());
						
			if(dto.getSeleccionadoPromocion())
			{	
				if(dto.getDetallePromocion() > 0)
				{
					ps.setDouble(9, dto.getDetallePromocion());
				}
				else
				{
					ps.setNull(9, Types.NUMERIC);
				}
				
				if(dto.getValorDescuentoPromocion().doubleValue()>0)
				{
					ps.setBigDecimal(10, dto.getValorDescuentoPromocion());
				}
				else
				{
					ps.setNull(10, Types.NUMERIC);
				}
				
				if(dto.getPorcentajePromocion() > 0)
				{
					ps.setDouble(11, dto.getPorcentajePromocion());
				}
				else
				{
					ps.setNull(11, Types.NUMERIC);
				}
				
				if(dto.getValorHonorarioPromocion().doubleValue()>0)
				{
					ps.setBigDecimal(12, dto.getValorHonorarioPromocion());
				}
				else if(dto.getPorcentajeHonorarioPromocion()>0)
				{
					ps.setBigDecimal(12, dto.getValorDescuentoPromocion().multiply(new BigDecimal(dto.getPorcentajeHonorarioPromocion())).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN));
				}
				else
				{
					ps.setNull(12, Types.NUMERIC);
				}
				
				if(! UtilidadTexto.isEmpty(dto.getAdvertenciaPromocion()))
				{
					ps.setString(13, dto.getAdvertenciaPromocion());
				}
				else
				{
					ps.setNull(13,Types.CHAR);
				}
			}
			else
			{
				ps.setNull(9, Types.NUMERIC);
				ps.setNull(10, Types.NUMERIC);
				ps.setNull(11, Types.NUMERIC);
				ps.setNull(12, Types.NUMERIC);
				ps.setNull(13,Types.CHAR);
			}
			
			if( UtilidadOdontologia.esBigDecimalMayorACero(dto.getDescuentoComercialUnitario())){
				ps.setBigDecimal(14,dto.getDescuentoComercialUnitario());
	        }else{
				
				ps.setNull(14, Types.NUMERIC);
			}
		
			if( ! UtilidadTexto.isEmpty(dto.getErrorCalculoTarifa())){
				ps.setString(15,dto.getErrorCalculoTarifa());
	        }else{
				
				ps.setNull(15, Types.CHAR);
			}
			ps.setInt(16, dto.getContrato().getCodigo());
		
			if(dto.getSerialBono().doubleValue()>0)
			{
				ps.setBigDecimal(17, dto.getSerialBono());
			}
			else
			{
				ps.setNull(17, Types.NUMERIC);
			}    
		    
			if(dto.getSeleccionadoBono())
			{	
				if(dto.getValorDescuentoBono().doubleValue()>0)
				{
					ps.setBigDecimal(18, dto.getValorDescuentoBono());
				}
				else
				{
					ps.setNull(18, Types.NUMERIC);
				}    
				
				if(!UtilidadTexto.isEmpty(dto.getAdvertenciaBono()))
				{
					ps.setString(19, dto.getAdvertenciaBono());
				}
				else
				{
					ps.setNull(19, Types.VARCHAR);
				}
				
				if(dto.getPorcentajeDctoBono()>0)
				{
					ps.setDouble(20, dto.getPorcentajeDctoBono());
				}
				else
				{
					ps.setNull(20, Types.DOUBLE);
				}
			}
			else
			{
				ps.setNull(18, Types.NUMERIC);
				ps.setNull(19, Types.VARCHAR);
				ps.setNull(20, Types.DOUBLE);
			}
			
			if(dto.getReservaAnticipo())
			{
				ps.setString(21, ConstantesBD.acronimoSi);
			}
			else
			{
				ps.setString(21, ConstantesBD.acronimoNo);
			}
			
			if(dto.getSeleccionadoPromocion())
			{
				ps.setString(22, UtilidadTexto.convertirSN(dto.isSeleccionadoPorcentajePromocion()+""));
			}
			else
			{
				ps.setNull(22, Types.VARCHAR);
			}
			if(dto.getSeleccionadoBono())
			{
				ps.setString(23, UtilidadTexto.convertirSN(dto.isSeleccionadoPorcentajeBono()+""));
			}
			else
			{
				ps.setNull(23, Types.VARCHAR);
			}
			
			if(dto.getDtoPresupuestoPaquete().getCodigoPk().doubleValue()>0)
			{
				ps.setBigDecimal(24, dto.getDtoPresupuestoPaquete().getCodigoPk());
			}
			else
			{
				ps.setNull(24, Types.NULL);
			}
			
			if(ps.executeUpdate()>0)
			{
			    ps.close();
				return secuencia;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return secuencia;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio ( DtoPresupuestoOdoConvenio dto, Connection con){
	 	
		ArrayList<DtoPresupuestoOdoConvenio> arrayDto= new ArrayList<DtoPresupuestoOdoConvenio>();
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS CONVENIO-----------------------------------------");
			
	 	String consultaStr=" select " +
	 								" 	pos.codigo_pk as codigoPk, "+  //1
									"	pos.presupuesto_odo_prog_serv as presupuestoOdoProgServ, "+//2
									"	pos.valor_unitario as valorUnitario,  "+ //3
									"	pos.convenio as convenio , "+ //4
									"	pos.fecha_modifica as fechaModifica, "+ //5
									"	pos.hora_modifica as horaModifica, "+ //6
									"	pos.usuario_modifica as  usuarioModifica, "+//7 
									"	pos.contratado as contratado ," + //8
									"   coalesce(pos.det_promocion, "+ConstantesBD.codigoNuncaValido+") as det_promocion ,"+//9
									"   coalesce(pos.valor_descuento_prom, 0) as valor_descuento_prom ,"+//10
									"   coalesce(pos.porcentaje_descuento_prom,0) as porcentaje_descuento_prom ,"+//11
									"   coalesce(pos.valor_honorario_prom,0) as  valor_honorario_prom ,"+//12
									"   coalesce(pos.advertencia_prom, '') as advertencia_prom,   "+//13
									"   coalesce(pos.dcto_comercial_unitario, 0) as dcto_comercial_unitario  ," +//14 
			                        "   coalesce(pos.error_calculo_tarifa, '') as error_calculo_tarifa,  " +//15
			                        "   pos.contrato as contrato, "+//16
			                        "   coalesce(pos.serial_bono, 0) as serial_bono, "+//17
			                        "   coalesce(pos.valor_descuento_bono, 0) as valor_descuento_bono, "+//18
			                        "   coalesce(pos.adventencia_bono, '') as advertencia_bono, "+//19
			                        "   coalesce(pos.porcentaje_dcto_bono,0) as porcentaje_dcto_bono, "+//20
			                        "	reserva_anticipo as reservaanticipo, "+//21
			                        "	coalesce(sel_porcent_prom, '"+ConstantesBD.acronimoNo+"') as sel_porcent_prom, "+//22
			                        "	coalesce(sel_porcent_bono, '"+ConstantesBD.acronimoNo+"') as sel_porcent_bono, " +//23
			                        "   coalesce(pos.presupuesto_paquete, 0) as presupuesto_paquete "+//24
			                        " 	from " +
									" 	odontologia.presupuesto_odo_convenio  pos" +
									" 	where 1=1 " +
									"  ";
	 	
	 	
	 
	 	
	 	consultaStr+=(dto.getCodigoPK().doubleValue() > 0)?" AND pos.codigo_pk="+dto.getCodigoPK():"";
	 	consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue() > 0)?" AND pos.presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ():"";
	 	//consultaStr+=(!UtilidadTexto.isEmpty(dto.getContratado()))?" AND pos.contratado='"+dto.getContratado()+"' ":" ";
	 	
	 	consultaStr+=" ORDER BY facturacion.getnombreconvenio(pos.convenio) ";
	 	logger.info("\n\n\n\n\n SQL CARGAR PRESUPUESTO::: / "+consultaStr);
	    
	    try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPresupuestoOdoConvenio newdto = new DtoPresupuestoOdoConvenio();
				newdto.setCodigoPK(new BigDecimal(rs.getDouble("codigoPk")));
				newdto.setPresupuestoOdoProgServ(new BigDecimal(rs.getDouble("presupuestoOdoProgServ")));
				newdto.setValorUnitario(rs.getBigDecimal("valorUnitario"));
				newdto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setContratado(rs.getString("contratado"));
				newdto.getConvenio().setCodigo(rs.getInt("convenio"));
				newdto.setDetallePromocion(rs.getDouble("det_promocion"));
				newdto.setValorDescuentoPromocion(rs.getBigDecimal("valor_descuento_prom"));
				newdto.setPorcentajePromocion(rs.getDouble("porcentaje_descuento_prom"));
				newdto.setValorHonorarioPromocion(rs.getBigDecimal("valor_honorario_prom"));
				
				newdto.setAdvertenciaPromocion(rs.getString("advertencia_prom"));
				newdto.setErrorCalculoTarifa(rs.getString("error_calculo_tarifa"));
				newdto.setDescuentoComercialUnitario(rs.getBigDecimal("dcto_comercial_unitario"));
				newdto.setContrato(new InfoDatosInt(rs.getInt("contrato")));
				
				newdto.setSerialBono(rs.getBigDecimal("serial_bono"));
				newdto.setValorDescuentoBono(rs.getBigDecimal("valor_descuento_bono"));
				newdto.setAdvertenciaBono(rs.getString("advertencia_bono"));
				newdto.setPorcentajeDctoBono(rs.getDouble("porcentaje_dcto_bono"));
				
				if (newdto.getValorDescuentoBono().doubleValue() > 0)
				{
					newdto.setSeleccionadoBono(true);
					logger.info("SELECCIONADO BONO ---- > true"+newdto.getCodigoPK());
				}
				
				if (newdto.getValorDescuentoPromocion().doubleValue() > 0)
				{
					newdto.setSeleccionadoPromocion(true);
					logger.info("SELECCIONADO PROMOCION ---- > true"+newdto.getCodigoPK());
				}
				
				newdto.setSeleccionadoPorcentajeBono(UtilidadTexto.getBoolean(rs.getString("sel_porcent_bono")));
				newdto.setSeleccionadoPorcentajePromocion(UtilidadTexto.getBoolean(rs.getString("sel_porcent_prom")));
				
				newdto.setReservaAnticipo(UtilidadTexto.getBoolean(rs.getString("reservaanticipo")));
				
				if(rs.getBigDecimal("presupuesto_paquete").doubleValue()>0)
				{	
					newdto.setDtoPresupuestoPaquete(SqlBasePresupuestoPaquetesDao.cargar(con, new DtoPresupuestoPaquetes(rs.getBigDecimal("presupuesto_paquete"))).get(0));
				}	
				
				//logger.info(UtilidadLog.obtenerString(newdto, true));
				arrayDto.add(newdto);
				
			}
			rs.close();
			ps.close();
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public static boolean modificarPresupuestoConvenio( DtoPresupuestoOdoConvenio dto , Connection con)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------MODIFICAR PRESUPUESTO PROGRAMA SERVICIO -----------------------------------------");
		boolean retorna=false;
		
		String consultaStr ="UPDATE odontologia.presupuesto_odo_convenio set codigo_pk=codigo_pk ,"+
							
																
																" presupuesto_odo_prog_serv = presupuesto_odo_prog_serv , "+
																" valor_unitario=? , "+//1
																" convenio= convenio ,  "+
																" fecha_modifica =? , "+//2
																" hora_modifica =?, "+//3
																" usuario_modifica=? , "+//4
																" contratado=?  ," + //5
																" det_promocion=?  ," + //6
																" valor_descuento_prom=?  ," + //7
																" porcentaje_descuento_prom=?  ," + //8
																" valor_descuento_bono=?  ," + //9
																" porcentaje_dcto_bono=?  ," + //10
																" valor_honorario_prom=?  ," + //11
																" advertencia_prom=?,  " +//12
																" reserva_anticipo=?, "+ //13
																" contrato= contrato "  +
																" Where 1=1 "; 
																
																
		consultaStr+=(dto.getCodigoPK().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigoPK().doubleValue():"";
		consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ().doubleValue():"";
		
																	
	    logger.info("Update "+consultaStr);
		   
	    try 
		{
			
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con,consultaStr);
			
			if(dto.getValorUnitario().doubleValue()>ConstantesBD.codigoNuncaValido)
			{
			 ps.setDouble(1, dto.getValorUnitario().doubleValue() );	
			}
			else
			{
				ps.setNull(1, Types.NUMERIC);
			}
			
			ps.setString(2, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(3,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(4, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(5, dto.getContratado());
			
	        if(dto.getDetallePromocion() > 0){
				
				ps.setDouble(6, dto.getDetallePromocion());
			}else{
				
				ps.setNull(6, Types.NUMERIC);
			}
			
			if(UtilidadOdontologia.esBigDecimalMayorACero(dto.getValorDescuentoPromocion())){
				ps.setBigDecimal(7, dto.getValorDescuentoPromocion());
				
			}else{
				
				ps.setNull(7, Types.NUMERIC);
			}
			
			if(dto.getPorcentajeHonorarioPromocion() > 0){
				ps.setDouble(8, dto.getPorcentajeHonorarioPromocion());
				
			}else{
				
				ps.setNull(8, Types.NUMERIC);
			}
			
			
			
			if(dto.getValorDescuentoBono().doubleValue() > 0){
				ps.setBigDecimal(9, dto.getValorDescuentoBono());
				
			}else{
				
				ps.setNull(9, Types.NUMERIC);
			}
			
			if(dto.getPorcentajeDctoBono() > 0){
				ps.setDouble(10, dto.getPorcentajeDctoBono());
				
			}else{
				
				ps.setNull(10, Types.NUMERIC);
			}
			
			
			if(UtilidadOdontologia.esBigDecimalMayorACero(dto.getValorHonorarioPromocion())){
				ps.setBigDecimal(11, dto.getValorHonorarioPromocion());
				
			}else{
				
				ps.setNull(11, Types.NUMERIC);
			}
			
			if(! UtilidadTexto.isEmpty(dto.getAdvertenciaPromocion())){
				ps.setString(12, dto.getAdvertenciaPromocion());
				
			}else{
				
				ps.setNull(12,Types.CHAR);
			}
			
			if(dto.getReservaAnticipo())
			{
				ps.setString(13, ConstantesBD.acronimoSi);
			}
			else
			{
				ps.setString(13, ConstantesBD.acronimoNo);
			}
			
			Log4JManager.info("Modificacion->"+ps);
		
			retorna=ps.executeUpdate() >0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma ",e);
		}
		return retorna;
		
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuestoOdoConvenio( DtoPresupuestoOdoConvenio  dto, Connection con)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------ELIMINAR PRESUPUESTO PROGRAMA SERVICIO-----------------------------------------");
		String consultaStr="DELETE FROM  odontologia.presupuesto_odo_convenio WHERE 1=1 ";
			
		//obj.setPresupuestoOdoConvenio(dto)
		
		consultaStr+=(dto.getCodigoPK().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigoPK():"";
		consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ():"";
		
		logger.info("Eliminar "+consultaStr);
	    try 
	    {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ");
			e.printStackTrace();
		}
			return false;
	}

	/*
	 *-------------------------------------------------------------------------------------------------------------------------------
	 * PRESUPUESTO PIEZAS ODONTOLOGICAS 
	 * ******************************************************************************************************************************
	 */


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarPresupuestoPiezas(DtoPresupuestoPiezas  dto , Connection con)
	{
		logger.info("***********************************************************************************************");
		logger.info("********************GUARDAR PRESUPUESTOS PROGRAMA SERVICIO**********************");
		//logger.info(UtilidadLog.obtenerString(dto, true));
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
			
		try 
		{
		
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presupuesto_piezas"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStrPresupuestoPiezas ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuencia);
			if(dto.getPresupuestoOdoProgServ().doubleValue() > 0)
			{
				ps.setDouble(2, dto.getPresupuestoOdoProgServ().doubleValue());
			}
			else
			{
				ps.setNull(2, Types.NUMERIC);
			}
			
			
			if(dto.getPieza().intValue() >0 )
			{
			 ps.setDouble(3, dto.getPieza().intValue());	
			}
			else
			{
				ps.setNull(3, Types.NUMERIC);
			}
			
			if(dto.getHallazgo().intValue() > 0)
			{
				ps.setDouble(4, dto.getHallazgo().intValue());
			}
			else
			{
				ps.setNull(4, Types.INTEGER);
			}
			ps.setString(5, dto.getSeccion());
			
			if(dto.getSuperficie().intValue()> 0)
			{
				ps.setInt(6, dto.getSuperficie().intValue());
			}
			else
			{
				ps.setNull(6,Types.INTEGER);
			}
			
			ps.setString(7, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(8,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(9, dto.getUsuarioModifica().getUsuarioModifica());
			
			if(dto.getNumSuperficies()>0)
			{	
				ps.setInt(10, dto.getNumSuperficies());
			}
			else
			{
				ps.setNull(10, Types.INTEGER);
			}
			if(ps.executeUpdate()>0)
				{
				    ps.close();
					return secuencia;
				}
		
			ps.close();
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		return secuencia;
		
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoPiezas> cargarPresupuestoPieza ( DtoPresupuestoPiezas dto, boolean inactivarNoPendientesPlanTratamiento, boolean utilizaProgramas)
	{
		ArrayList<DtoPresupuestoPiezas> arrayDto= new ArrayList<DtoPresupuestoPiezas>();
		logger.info("-***********************************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS PIEZA-----------------------------------------");
	 	
	 	//logger.info("DTO PRESUPUESTO--->"+UtilidadLog.obtenerString(dto, true));
	 	
		String consultaStr=" select  pp.codigo_pk as codigoPk  , "+ //1
									" pp.presupuesto_odo_prog_serv as presupuestoOdoProgServ  , "+ //2
									" coalesce(pp.pieza, 0) as pieza , "+ //3
									" pp.hallazgo as hallazgo , "+ //4
									" pp.seccion as seccion   , " +
									" coalesce(pp.superficie,0) as superficie , "+ //5
									" pp.fecha_modifica as fechaModifica , "+ //6
									" pp.hora_modifica as horaModifica , "+ //7
									" pp.usuario_modifica as usuarioModifica, " +//8
									" coalesce(pp.num_superficies,0) as numsuperficies, "+//9
									" pops.presupuesto as presupuesto, " +
									" coalesce(pops.programa,0) as programa, " +
									" coalesce(pops.servicio,0) as servicio "+
									" from " +
									" odontologia.presupuesto_piezas pp " +
									" inner join odontologia.presupuesto_odo_prog_serv pops on (pops.codigo_pk=pp.presupuesto_odo_prog_serv) " +
								" WHERE 1=1  ";
		
		consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue() >ConstantesBD.codigoNuncaValidoDouble)?" AND pp.presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ().doubleValue():"";
		consultaStr+=(dto.getPieza().intValue()>ConstantesBD.codigoNuncaValido)? " and pp.pieza="+dto.getPieza().intValue(): "";
		consultaStr+=(dto.getHallazgo().intValue()>ConstantesBD.codigoNuncaValido)? " and pp.hallazgo="+dto.getHallazgo().intValue(): "";
		consultaStr+=(dto.getSuperficie().intValue()>ConstantesBD.codigoNuncaValido)? " and pp.superficie="+dto.getSuperficie().intValue(): "";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModificaFromatoBD())?"":" and pp.fecha_modifica= '"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" and pp.hora_modifica= '"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" and pp.usuario_modifica= '"+dto.getUsuarioModifica().getUsuarioModifica()+"'";
		
		
	    logger.info("\n\n\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& SQL CARGAR PRESUPUESTO PIEZAS ::: / "+consultaStr);
	
	    
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPresupuestoPiezas newdto = new DtoPresupuestoPiezas();
				newdto.setCodigoPk(new BigDecimal(rs.getDouble("codigoPk")));
				newdto.setPieza(new BigDecimal(rs.getDouble("pieza")));
				newdto.setHallazgo(new BigDecimal(rs.getDouble("hallazgo")));
				newdto.setSuperficie(new BigDecimal(rs.getDouble("superficie")));
				newdto.setPresupuestoOdoProgServ(new BigDecimal(rs.getDouble("presupuestoOdoProgServ")));
				newdto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
				newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setSeccion(rs.getString("seccion"));
				newdto.setNumSuperficies(rs.getInt("numsuperficies"));
				
				if(inactivarNoPendientesPlanTratamiento)
				{
					newdto.setActivo( SqlBaseValidacionesPresupuestoDao.existeProgramaServicioPresupuestoEnPlanTratamiento(newdto.getCodigoPk(), ConstantesIntegridadDominio.acronimoEstadoPendiente, utilizaProgramas).doubleValue()>0);
					
					//si no existe en el plan de tratamiento original, entonces buscamos si pertenece al plan tratamiento del presupuesto
					if(!newdto.getActivo())
					{
						BigDecimal programaServicio= rs.getBigDecimal("programa").doubleValue()>0? rs.getBigDecimal("programa"): rs.getBigDecimal("servicio");
						newdto.setActivo(SqlBaseValidacionesPresupuestoDao.esProgramaServicioAdicionPlanTtoPresupuesto(newdto.getPieza(), newdto.getSuperficie(), newdto.getHallazgo(), newdto.getSeccion(), rs.getBigDecimal("presupuesto"), utilizaProgramas,  programaServicio).doubleValue()>0);
					}
				}
				else
				{
					newdto.setActivo(true);
				}
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuestoPieza( DtoPresupuestoPiezas dto )
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------MODIFICAR PRESUPUESTO PROGRAMA SERVICIO -----------------------------------------");
		boolean retorna=false;
		
		String consultaStr ="UPDATE  odontologia.presupuesto_piezas  set codigo_pk= codigo_pk , "+
																	" presupuesto_odo_prog_serv=? , "+ //1
																	" pieza=? , "+ //2
																	" hallazgo=? , "+ //3
																	" superficie=? , "+ //4
																	" fecha_modifica=? , "+ //5
																	" hora_modifica=? , "+ //6
																	" usuario_modifica=?, " + //7
																	" num_superficies=? "+ //8
																	" values" +
																	" WHERE 1=1  ";
		
		consultaStr+=(dto.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigoPk():"";
		consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue() >ConstantesBD.codigoNuncaValidoDouble)?" AND presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ().doubleValue():"";
		consultaStr+=(dto.getPieza().intValue()>ConstantesBD.codigoNuncaValido)? " and pieza="+dto.getPieza().intValue(): "";
		consultaStr+=(dto.getHallazgo().intValue()>ConstantesBD.codigoNuncaValido)? " and hallazgo="+dto.getHallazgo().intValue(): "";
		consultaStr+=(dto.getSuperficie().intValue()>ConstantesBD.codigoNuncaValido)? " and superficie="+dto.getSuperficie().intValue(): "";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModificaFromatoBD())?"":" and fecha_modifica= '"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" and hora_modifica= '"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" and usuario_modifica= '"+dto.getUsuarioModifica().getUsuarioModifica()+"'";
		logger.info("Update "+consultaStr);
	   
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(dto.getPresupuestoOdoProgServ().doubleValue() >ConstantesBD.codigoNuncaValidoDouble)
			{
				ps.setDouble(1, dto.getPresupuestoOdoProgServ().doubleValue());
			}
			else
			{
				ps.setNull(1, Types.NUMERIC);
			}
			
			
			if(dto.getPieza().intValue() >ConstantesBD.codigoNuncaValido)
			{
			 ps.setInt(2, dto.getPieza().intValue());	
			}
			else
			{
				ps.setNull(2, Types.NUMERIC);
			}
			
			ps.setDouble(3, dto.getHallazgo().intValue());
			if(dto.getSuperficie().intValue()>ConstantesBD.codigoNuncaValido)
			{
			 ps.setInt(4, dto.getSuperficie().intValue());	
			}
			else
			{
				ps.setNull(4, Types.INTEGER);
			}
			
			
			
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6,dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getUsuarioModifica().getUsuarioModifica());
			
			if(dto.getNumSuperficies()>0)
			{	
				ps.setInt(8, dto.getNumSuperficies());
			}	
			else
			{
				ps.setNull(8, Types.INTEGER);
			}
			
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma "+e);
		}
		return retorna;
	
	}

	/**
	* 
	* @param dto
	* @return
	*/
	public static boolean eliminarPresupuestoPieza( DtoPresupuestoPiezas  dto , Connection con)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------ELIMINAR PRESUPUESTO PROGRAMA SERVICIO-----------------------------------------");
		String consultaStr="DELETE FROM  odontologia.presupuesto_piezas WHERE 1=1 ";
		
		consultaStr+=(dto.getCodigoPk().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigoPk():"";
		consultaStr+=(dto.getPresupuestoOdoProgServ().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND presupuesto_odo_prog_serv="+dto.getPresupuestoOdoProgServ():"";
	    try 
	    {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			
			logger.info("Eliminar "+consultaStr);
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoLogPresupuestoOdontologico> cargarLogPresupuesto ( DtoLogPresupuestoOdontologico dto)
	{
	 	ArrayList<DtoLogPresupuestoOdontologico> arrayDto= new ArrayList<DtoLogPresupuestoOdontologico>();
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS-----------------------------------------");
	 	
	 	//logger.info("DTO PRESUPUESTO--->"+UtilidadLog.obtenerString(dto, true));
	 	
	 	String consultaStr=" select " +
								 	"  lpr.codigo_pk as codigoPk, "+ //1
								 	"  lpr.presupuesto as presupuestoOdo, "+ //2
									"	lpr.estado as estado ,"+ //3
									"	lpr.motivo as motivo  ,"+ //4
									"	( select mt.nombre from odontologia.motivos_atencion mt where mt.codigo_pk=lpr.motivo ) as nombreMotivo  ,"+ //4
									"	lpr.especialidad as especialidad, "+ //5
									"getnombreespecialidad(lpr.especialidad) as nombreEspecialidad  ,"+
									"	lpr.fecha_modifica as fechaModifica , "+ //6
									"	lpr.hora_modifica as horaModifica, "+ //7
									"	lpr.usuario_modifica as usuarioModifica, "+ //8
									"	administracion.getnombreusuario2(lpr.usuario_modifica) as nomusuario "+ //9
									" from " +
									" odontologia.log_presupuesto_odonto lpr" +
									" 	where 1=1 ";
	 	
	 	
	 	consultaStr+=(dto.getCodigoPK().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpr.codigo_pk="+dto.getCodigoPK():"";
	 	consultaStr+=(dto.getCodigoPresupuesto().doubleValue()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpr.presupuesto="+dto.getCodigoPresupuesto():"";
	 	consultaStr+=UtilidadTexto.isEmpty(dto.getEstado())?"":" AND estado='"+dto.getEstado()+"'";
		consultaStr+=(dto.getMotivo().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpr.motivo="+dto.getMotivo().getCodigo():"";
		consultaStr+=(dto.getEspecialidad().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)?" AND lpr.especialidad="+dto.getEspecialidad().getCodigo():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getFechaModifica())?"":" AND lpr.fecha_modifica='"+dto.getUsuarioModifica().getFechaModificaFromatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getHoraModifica())?"":" AND lpr.hora_modifica='"+dto.getUsuarioModifica().getHoraModifica()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica().getUsuarioModifica())?"":" AND lpr.usuario_modifica='"+dto.getUsuarioModifica()+"'";
		
	    logger.info("\n\n\n\n\n SQL CARGAR PRESUPUESTO::: / "+consultaStr);
	
	    
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+"  ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoLogPresupuestoOdontologico newdto = new DtoLogPresupuestoOdontologico();
				
				newdto.setCodigoPK( new BigDecimal(rs.getDouble("codigoPk")));
				newdto.setCodigoPresupuesto(rs.getBigDecimal("presupuestoOdo"));
				newdto.setEstado(rs.getString("estado"));
				newdto.setMotivo(new InfoDatosInt(rs.getInt("motivo"), rs.getString("nombreMotivo")));
				newdto.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"),rs.getString("nombreEspecialidad")));
				newdto.getUsuarioModifica().setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaModifica")));
				newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));
				newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.getUsuarioModifica().setNombreUsuarioModifica(rs.getString("nomusuario"));
				
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 * descuentos
	 * 
	 */
	public static ArrayList<DtoPresupuestoOdontologicoDescuento> cargarPresupuestoDescuentos ( DtoPresupuestoOdontologicoDescuento dto, ArrayList<String> listaEstados)
	{
	 	ArrayList<DtoPresupuestoOdontologicoDescuento> arrayDto= new ArrayList<DtoPresupuestoOdontologicoDescuento>();
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------CONSULTAR PRESUPUESTO ODONTOLOGICOS-----------------------------------------");
	 	
	 	//logger.info("DTO PRESUPUESTO--->"+UtilidadLog.obtenerString(dto, true));
	 	
	 	String consultaStr=" select " +
								 	"  	lpd.codigo_pk as codigo_pk, "+ //1
								 	"  	lpd.presupuesto as presupuesto, "+ //2
									"	lpd.fecha_solicitad as fecha_solicitad ,"+ //3
									"	lpd.hora_solicita as hora_solicita  ,"+ //4
									"	lpd.usuario_solicitad as usuario_solicitad  ,"+ //4
									"   coalesce(lpd.det_dcto_odo,0) as det_dcto_odo ,"+
									"	lpd.fecha_modifica as fecha_modifica , "+ //6
									"	lpd.hora_modifica as hora_modifica, "+ //7
									"	lpd.estado as estado ," +
									"	coalesce(lpd.valor_descuento,0) as valordescuento ,"+
									"	lpd.usuario_modifica as usuario_modifica,  "+ //8
									"	lpd.consecutivo as consecutivo, "+ //8
									"	coalesce(lpd.porcentaje_dcto,0) as porcentaje_dcto, "+//9
									"	coalesce(lpd.motivo,0) as motivo," +
									"	(select descripcion  from odontologia.motivos_descuentos_odon where codigo_pk= lpd.motivo ) as  nombreMotivo , "+//10
									"	lpd.observaciones as observaciones, "+//11
									"   coalesce(lpd.det_dcto_odo_aten,0) as det_dcto_odo_aten " +
									"	 " +
									"	"+//12
									"	 from  " +
									"   odontologia.presupuesto_dcto_odon lpd" +
									" 	 where 1=1 ";
	 	
	 	
	 	consultaStr+=(dto.getCodigo().doubleValue() > 0)?" AND lpd.codigo_pk="+dto.getCodigo():"";
	 	consultaStr+=(dto.getPresupuesto().doubleValue() > 0)?" AND lpd.presupuesto="+dto.getPresupuesto():"";
	 	consultaStr+=(!UtilidadTexto.isEmpty(dto.getEstado()))?" AND lpd.estado='"+dto.getEstado()+"' ":"";
	 	
	 	if(listaEstados.size()>0)
	 	{
	 		consultaStr+=" AND lpd.estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(listaEstados)+") ";
	 	}
	 	
	    logger.info("\n\n\n\n\n SQL CARGAR PRESUPUESTO::: / "+consultaStr);
	
	    
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by lpd.codigo_pk desc ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPresupuestoOdontologicoDescuento newdto = new DtoPresupuestoOdontologicoDescuento();
				
				newdto.setCodigo( new BigDecimal(rs.getDouble("codigo_pk")));
				newdto.setPresupuesto(rs.getBigDecimal("presupuesto"));
				newdto.setDetalleDescuentoOdontologico(rs.getDouble("det_dcto_odo"));
				newdto.getUsuarioFechaModifica().setFechaModifica(rs.getString("fecha_modifica"));
				newdto.getUsuarioFechaModifica().setHoraModifica(rs.getString("hora_modifica"));
				newdto.getUsuarioFechaModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				newdto.getUsuarioFechaSolicitud().setFechaModifica(rs.getString("fecha_solicitad"));
				newdto.getUsuarioFechaSolicitud().setHoraModifica(rs.getString("hora_solicita"));
				newdto.getUsuarioFechaSolicitud().setUsuarioModifica(rs.getString("usuario_solicitad"));
				newdto.setEstado(rs.getString("estado"));
				newdto.setValorDescuento(rs.getBigDecimal("valordescuento"));
				newdto.setConsecutivo(rs.getBigDecimal("consecutivo"));
				newdto.setPorcentajeDcto(rs.getDouble("porcentaje_dcto"));
				newdto.setMotivo(rs.getDouble("motivo"));
				newdto.setObservaciones(rs.getString("observaciones"));
				newdto.setDetalleDescuentoOdontologicoAtencion(rs.getDouble("det_dcto_odo_aten"));
				newdto.setNombreMotivo(rs.getString("nombreMotivo"));
				
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}

	/**
	 * 
	 */
	public static double guardarPresupuestoDescuento(DtoPresupuestoOdontologicoDescuento dto ,Connection con)
	{
		logger.info("-***********************************************************************************************");
		logger.info("--------------------------------------GUARDAR PRESUPUESTOS LOG-----------------------------");
		
		//logger.info(UtilidadLog.obtenerString(dto, true));
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{	
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presupuesto_dcto"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStrPresupuestoDescuento ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getUsuarioFechaSolicitud().getFechaModificaFromatoBD());
			ps.setString(3, dto.getUsuarioFechaSolicitud().getHoraModifica());
			ps.setString(4, dto.getUsuarioFechaSolicitud().getUsuarioModifica());
			ps.setBigDecimal(5, dto.getPresupuesto());
			if(dto.getDetalleDescuentoOdontologico()>0)
			{	
				ps.setDouble(6,dto.getDetalleDescuentoOdontologico());
			}
			else
			{
				ps.setNull(6, Types.NUMERIC);
			}
			ps.setString(7, dto.getUsuarioFechaModifica().getFechaModificaFromatoBD());
			ps.setString(8, dto.getUsuarioFechaModifica().getHoraModifica());
			ps.setString(9, dto.getUsuarioFechaModifica().getUsuarioModifica());
			
			if(dto.getValorDescuento().doubleValue()>0)
			{	
				ps.setBigDecimal(10, dto.getValorDescuento());
			}
			else
			{
				ps.setNull(10, Types.NUMERIC);
			}
			ps.setString(11, dto.getEstado());
			ps.setBigDecimal(12, dto.getConsecutivo());
	
			if(dto.getPorcentajeDcto()>0)
			{	
				ps.setDouble(13, dto.getPorcentajeDcto());
			}
			else
			{
				ps.setNull(13, Types.NUMERIC);
			}
			if(dto.getMotivo()>0)
			{	
				ps.setDouble(14, dto.getMotivo());
			}	
			else
			{	
				ps.setNull(14, Types.NUMERIC);
			}
			if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
			{	
				ps.setString(15, dto.getObservaciones());
			}	
			else
			{
				ps.setNull(15, Types.VARCHAR);
			}
			if(dto.getDetalleDescuentoOdontologicoAtencion()>0)
			{
				ps.setDouble(16, dto.getDetalleDescuentoOdontologicoAtencion());
			}
			else
			{
				ps.setNull(16, Types.NUMERIC);
			}
			if(dto.isInclusion())
			{
				ps.setString(17, ConstantesBD.acronimoSi);
			}
			else
			{
				ps.setString(17, ConstantesBD.acronimoNo);
			}
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return secuencia;
			}
			ps.close();
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		
		return secuencia;
		
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuestoDescuento( DtoPresupuestoOdontologicoDescuento  dto)
	{
		logger.info("-****************************************************************************************************");
		logger.info("---------------------------------------ELIMINAR PRESUPUESTO DESCUENTO ----------------------------------------");
		String consultaStr="DELETE FROM  odontologia.presupuesto_dcto_odon WHERE 1=1 ";
		
		consultaStr+=(dto.getCodigo().doubleValue() > ConstantesBD.codigoNuncaValidoDouble)?" AND codigo_pk="+dto.getCodigo():"";
		consultaStr+=(dto.getPresupuesto().doubleValue() > ConstantesBD.codigoNuncaValidoDouble)?" AND presupuesto="+dto.getPresupuesto().doubleValue():"";
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * 
	 * 
	 */
	public static  boolean aplicaPromocion(int contrato)
	{
		
		String consultaStr="select count(1) from odontologia.presupuesto_odo_convenio po where po.contrato="+contrato; 
		//+" AND po.valor_pago_inicial_prom > 0 AND po.valor_pago_inicial_prom < (select coalesce(va.valor_ant_rec_convenio,0) - coalesce(va.valor_ant_res_pre_cont_pac,0) from facturacion.control_anticipos_contrato va where va.contrato="+contrato+" ) ";
		boolean retorna= false;
		  try 
		  {
			  logger.info("*****************************************************************************************");
			 
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
							
				retorna= true;
					
			}
		 
		 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
			
		}
		return retorna;
		
	}


	/**
	 * 
	 * @param piezaOPCIONAL
	 * @param superficieOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param codigoPkProgramaServicio
	 * @param utilizaProgramas
	 * @param codigoPkPresupuesto 
	 * @return
	 */
	public static BigDecimal existeProgramaServicioPlanTratamientoEnPresupuesto(
			int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,
			BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPresupuesto, String seccion)
	{
		BigDecimal retorna= new BigDecimal(0);
		String consultaStr=		" SELECT " +
									"pops.codigo_pk " +
								"FROM " +
									"odontologia.presupuesto_odo_prog_serv pops " +
									"INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"WHERE " +
									"pops.presupuesto="+codigoPkPresupuesto+" ";
		
		if(utilizaProgramas)
		{
			if(codigoPkPresupuesto.doubleValue()>0)
			{	
				consultaStr+=" and pops.programa="+codigoPkProgramaServicio;
			}	
			else
			{
				consultaStr+="and pops.programa is null ";
			}
		}
		else
		{
			if(codigoPkPresupuesto.doubleValue()>0)
			{	
				consultaStr+=" and pops.servicio="+codigoPkProgramaServicio;
			}	
			else
			{
				consultaStr+="and pops.servicio is null ";
			}
		}
		
		if(piezaOPCIONAL>0)
		{	
			consultaStr+=" and pp.pieza="+piezaOPCIONAL; 
		}
		else
		{
			consultaStr+=" and pp.pieza is null" ;
		}
		
		if(superficieOPCIONAL>0)
		{	
			consultaStr+=" and pp.superficie="+superficieOPCIONAL; 
		}
		else
		{
			consultaStr+=" and pp.superficie is null" ;
		}
		
		consultaStr+=" and pp.hallazgo= "+hallazgoREQUERIDO;
		consultaStr+=" and pp.seccion='"+seccion+"' ";
		
		logger.info("consulta--->"+consultaStr);
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				retorna=rs.getBigDecimal(1);
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Presupuesto  ", e);
		}
		
		return retorna;
	}

	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static int existeProgramaServicioPresupuestoEnPlanTratamiento(
			int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,
			BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPlan, String estadoOPCIONAL)
	{
		int retorna= 0;
		String consultaStr=		" SELECT distinct " +
									"odpl.codigo_pk  as codigo_pk   " +
								"FROM " +
									"odontologia.det_plan_tratamiento odpl " +
									"INNER JOIN odontologia.plan_tratamiento opl ON(odpl.plan_tratamiento=opl.codigo_pk) " +
									"INNER JOIN odontologia.programas_servicios_plan_t opst ON(opst.det_plan_tratamiento=odpl.codigo_pk) " +
									"INNER JOIN odontologia.presupuesto_odontologico pr ON(pr.plan_tratamiento=opl.codigo_pk) " +
								"WHERE " +
									"opl.codigo_pk="+codigoPkPlan+" " +
									"AND odpl.activo='"+ConstantesBD.acronimoSi+"' ";
		                      
		
		if(utilizaProgramas)
		{
				
				consultaStr+=" and opst.programa="+codigoPkProgramaServicio;
			
		}
		else
		{
			
				consultaStr+=" and opst.servicio="+codigoPkProgramaServicio;
			
			
		}
		
		if(piezaOPCIONAL>0)
		{	
			consultaStr+=" and odpl.pieza_dental="+piezaOPCIONAL; 
		}
		else
		{
			consultaStr+=" and odpl.pieza_dental is null" ;
		}
		
		if(superficieOPCIONAL>0)
		{	
			consultaStr+=" and odpl.superficie="+superficieOPCIONAL; 
		}
		else
		{
			consultaStr+=" and odpl.superficie is null" ;
		}
		
		if(!UtilidadTexto.isEmpty(estadoOPCIONAL))
		{
			if(utilizaProgramas)
			{
				consultaStr+=" AND opst.estado_programa = '"+estadoOPCIONAL+"' ";
			}
			else
			{
				consultaStr+=" AND opst.estado_servicio = '"+estadoOPCIONAL+"' ";
			}
		}
		
		consultaStr+=" and odpl.hallazgo= "+hallazgoREQUERIDO;
		
		logger.info("consulta--->"+consultaStr);
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
			    retorna = rs.getInt("codigo_pk");
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static ArrayList<InfoConvenioContratoPresupuesto> obtenerConveniosContratosPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		ArrayList<InfoConvenioContratoPresupuesto> array= new ArrayList<InfoConvenioContratoPresupuesto>();
/*		String consultaStr="SELECT distinct " +
								"poc.convenio AS codigo_convenio, " +
								"getnombreconvenio(poc.convenio) as nombre_convenio, " +
								"poc.contrato as codigo_contrato, " +
								"getnumerocontrato(poc.contrato) as numero_contrato  " +
							"from " +
								"odontologia.presupuesto_odo_convenio poc " +
								"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv) " +
							"WHERE " +
								"pops.presupuesto=? " +
							"order by 2,4 ";*/

		String consultaStr=
						"SELECT " +
							"cont.convenio AS codigo_convenio, " +
							"getnombreconvenio(cont.convenio) as nombre_convenio, " +
							"cip.contrato AS codigo_contrato, " +
							"getnumerocontrato(cip.contrato) as numero_contrato  " +
						"FROM " +
							"convenios_ingreso_paciente cip " +
						"INNER JOIN " +
							"contratos cont " +
								"ON(cont.codigo=cip.contrato) " +
						"INNER JOIN " +
							"convenios conv " +
								"ON(conv.codigo=cont.convenio) " +
						"WHERE paciente=" +
							"(" +
								"SELECT " +
									"ppto.codigo_paciente " +
								"FROM " +
									"presupuesto_odontologico ppto " +
								"WHERE " +
									"ppto.codigo_pk=?" +
							") " +
						"AND conv.activo=" + ValoresPorDefecto.getValorTrueParaConsultas()+" "+
						"ORDER BY nombre_convenio, numero_contrato";

		logger.info("consulta--->"+consultaStr);
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPkPresupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int indice=0;
			while(rs.next())
			{
				InfoConvenioContratoPresupuesto info= new InfoConvenioContratoPresupuesto();
				info.setActivo(true);
				info.setContrato(new InfoDatosInt(rs.getInt("codigo_contrato"), rs.getString("numero_contrato")));
				info.setConvenio(new InfoDatosInt(rs.getInt("codigo_convenio"), rs.getString("nombre_convenio")));
				
				//info.setActivo(existeResponsablePresupuesto(con, codigoPkPresupuesto, rs.getInt("codigo_contrato")));
				info.setIndice(indice);
				array.add(info);
				indice++;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR ", e);
		}
		return array;
	}
	
	/**
	 * 
	 * Metodo para verificar si el convenio/contrato es un responsable en el presupuesto
	 * @param codigoPkPresupuesto
	 * @param int1
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	@SuppressWarnings("unused")
	private static boolean existeResponsablePresupuesto(Connection con, BigDecimal codigoPkPresupuesto, int contrato) 
	{
		boolean existe= false;
		String consultaStr=" select " +
								"count(1) as contador " +
							"from " +
								"manejopaciente.sub_cuentas " +
							"where " +
								"ingreso= (" +
											"select " +
												"ingreso " +
											"from " +
												"odontologia.presupuesto_odontologico " +
											"where " +
												"codigo_pk=?) " +
								"and contrato= ?";
		
		try 
	    {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoPkPresupuesto);
			ps.setInt(2, contrato);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				existe=rs.getInt("contador")>0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR ", e);
		}
		return existe;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuestoREQUERIDO
	 * @param programaOservicioREQUERIDO
	 * @param utilizaProgramas
	 * @param piezaDentalOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param superficieOPCIONAL
	 * @return
	 */
	
    public static String consultaReporteDetalle(int codigoPresupuesto,
			String tipoRelacion) 
	{
		 String sql= "SELECT DISTINCT 	" +
		 		"coalesce(pops.cantidad,0) * (coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0) ) as valor_total, " +
  		"	    po.codigo_pk as presupuesto," +
  		" 		poc.convenio as convenio, " +
  		"       getnombreconvenio(poc.convenio) as nombreConvenio, " + 
  		"		(coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0) ) as valor_unitario , " +
  		"		coalesce(pops.cantidad,0) as cantidad , " +
  		"		poc.contrato as contrato , " +
  		//"		coalesce(pdo.valor_descuento,0) as valor_descuento,  " +
  		"		0 as valor_descuento,  " +
  		"		(coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0) ) as valorConDescuento, "+
  		//"		coalesce(pdo.estado, '') as estado_descuento, " ;
  		"		'' as estado_descuento, " ;
			 
			if(tipoRelacion.equals("Programa"))
			{
			 
			sql+="	coalesce(pops.programa) as programa ,  " +
  		"	 	coalesce(pops.servicio) as servicio ,  " +
  		"		coalesce((select pro.nombre from odontologia.programas pro where pro.codigo  = pops.programa)) as nombre_programa , " +
  		"		coalesce(facturacion.getnombreservicio(pops.servicio,0))  as nombre_servicio ,  " +
  		"		coalesce((select pro.codigo_programa from odontologia.programas pro where pro.codigo  = pops.programa)) as codigo_programa ,	" + 
  		"		coalesce(facturacion.getcodigoservicio(pops.servicio,0))  as codigo_servicio , " ;
			}else
			{
				
	     		sql+="	 coalesce(pops.servicio) as programa ,  " +	     		
	     		"		coalesce(facturacion.getnombreservicio(pops.servicio,0))  as nombre_programa ,  " +	     		
	     		"		coalesce(facturacion.getcodigoservicio(pops.servicio,0))  as codigo_programa , " ;
				
			}
			sql+="	CASE WHEN po.estado='COT' THEN	coalesce( " +
  		"					( " +
  		"						SELECT  " +
  		"							sum((poc_interno.valor_unitario - coalesce(poc_interno.valor_descuento_prom, 0) - coalesce(poc_interno.valor_descuento_bono, 0) - coalesce(poc_interno.dcto_comercial_unitario, 0) )*pops_interno.cantidad)  " +
  		"						FROM   " +
  		"							odontologia.presupuesto_odo_prog_serv pops_interno  " +
  		"							INNER JOIN odontologia.presupuesto_odo_convenio poc_interno ON (poc_interno.presupuesto_odo_prog_serv=pops_interno.codigo_pk)  " +
  		"						WHERE  " +
  		"							pops_interno.presupuesto=pops.presupuesto  " +
  		"							and poc_interno.convenio=poc.convenio  " +
  		"							and poc_interno.contratado='"+ConstantesBD.acronimoSi+"' " +
  		"					),  " +
  		"					0 " +
  		"				)" ;
  		
  		sql+=" 	ELSE 	coalesce( " +
  		"       		( " +
  		"						SELECT  " +
  		"							sum((poc_interno.valor_unitario - coalesce(poc_interno.valor_descuento_prom, 0) - coalesce(poc_interno.valor_descuento_bono, 0) - coalesce(poc_interno.dcto_comercial_unitario, 0) )*pops_interno.cantidad)  " +
  		"						FROM   " +
  		"							odontologia.presupuesto_odo_prog_serv pops_interno  " +
  		"							INNER JOIN odontologia.presupuesto_odo_convenio poc_interno ON (poc_interno.presupuesto_odo_prog_serv=pops_interno.codigo_pk)  " +
  		"						WHERE  " +
  		"							pops_interno.presupuesto=pops.presupuesto  " +
  		"							and poc_interno.convenio=poc.convenio " +
  		"					), " +
  		"					0 " +
  		"				) END  as  valor_subtotal_contratado,  " ;
  		
  		if(tipoRelacion.equals("Programa"))
		{
			sql+= " (select administracion.getnombreespecialidad(p.especialidad) from odontologia.programas p where p.codigo=pops.programa) as nombreespecialidad ";	
		}
		else
		{
			sql+=" (select administracion.getnombreespecialidad(s.especialidad) from facturacion.servicios s where s.codigo=pops.servicio) as nombreespecialidad ";
		}
  		
  		sql+= "	FROM  " +
  		"		odontologia.presupuesto_odontologico po  " +
  		"		INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.presupuesto=po.codigo_pk)  " +
  		"		INNER JOIN odontologia.presupuesto_odo_convenio poc ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv)  ";
  			 
			 sql+= " where po.codigo_pk="+codigoPresupuesto;
			 
			 return sql;
		
	}	
		
	
	
	
	
	
	
	public static String consultaReporteContratar(String tipoRelacion , int codigoPresupuesto)
	{
		 String sql= "SELECT DISTINCT 	" +
		 		"coalesce(pops.cantidad,0) * (coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0)) as valor_total, " +
 		"	    po.codigo_pk as presupuesto," +
 		" 		poc.convenio as convenio, " +
 		"       getnombreconvenio(poc.convenio) as nombreConvenio, " + 
 		"		(coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0)) as valor_unitario , " +
 		"		coalesce(pops.cantidad,0) as cantidad , " +
 		"		poc.contrato as contrato , " +
 		//"		coalesce(pdo.valor_descuento,0) as valor_descuento,  " +
 		"		0 as valor_descuento,  " +
 		"		(coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) -  coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0)) as valorConDescuento, "+
 		//"		coalesce(pdo.estado, '') as estado_descuento, " ;
 		"		'' as estado_descuento, " ;
		 
		if(tipoRelacion.equals("Programa"))
		{
		 
		sql+="	    coalesce(pops.programa) as programa ,  " +
 		"	 	coalesce(pops.servicio) as servicio ,  " +
 		"		coalesce((select pro.nombre from odontologia.programas pro where pro.codigo  = pops.programa)) as nombre_programa , " +
 		"		coalesce(facturacion.getnombreservicio(pops.servicio,0))  as nombre_servicio ,  " +
 		"		coalesce((select pro.codigo_programa from odontologia.programas pro where pro.codigo  = pops.programa)) as codigo_programa ,	" + 
 		"		coalesce(facturacion.getcodigoservicio(pops.servicio,0))  as codigo_servicio , " ;
		}else
		{
			
     		sql+="	 coalesce(pops.servicio) as programa ,  " +	     		
     		"		coalesce(facturacion.getnombreservicio(pops.servicio,0))  as nombre_programa ,  " +	     		
     		"		coalesce(facturacion.getcodigoservicio(pops.servicio,0))  as codigo_programa , " ;
			
		}
		sql+="			 		coalesce( " +
 		"       		( " +
 		"						SELECT  " +
 		"							sum((poc_interno.valor_unitario - coalesce(poc_interno.valor_descuento_prom, 0) - coalesce(poc_interno.valor_descuento_bono, 0) - coalesce(poc_interno.dcto_comercial_unitario, 0) )*pops_interno.cantidad)  " +
 		"						FROM   " +
 		"							odontologia.presupuesto_odo_prog_serv pops_interno  " +
 		"							INNER JOIN odontologia.presupuesto_odo_convenio poc_interno ON (poc_interno.presupuesto_odo_prog_serv=pops_interno.codigo_pk)  " +
 		"						WHERE  " +
 		"							pops_interno.presupuesto=pops.presupuesto  " +
 		"							and poc_interno.convenio=poc.convenio " +
 		"					), " +
 		"					0 " +
 		"				) as valor_subtotal_sin_contratar, " +
 		"		coalesce( " +
 		"					( " +
 		"						SELECT  " +
 		"							sum((poc_interno.valor_unitario - coalesce(poc_interno.valor_descuento_prom, 0) - coalesce(poc_interno.valor_descuento_bono, 0) - coalesce(poc_interno.dcto_comercial_unitario, 0) )*pops_interno.cantidad)  " +
 		"						FROM   " +
 		"							odontologia.presupuesto_odo_prog_serv pops_interno  " +
 		"							INNER JOIN odontologia.presupuesto_odo_convenio poc_interno ON (poc_interno.presupuesto_odo_prog_serv=pops_interno.codigo_pk)  " +
 		"						WHERE  " +
 		"							pops_interno.presupuesto=pops.presupuesto  " +
 		"							and poc_interno.convenio=poc.convenio  " +
 		"							and poc_interno.contratado='"+ConstantesBD.acronimoSi+"' " +
 		"					),  " +
 		"					0 " +
 		"				) as  valor_subtotal_contratado, ";
		
		if(tipoRelacion.equals("Programa"))
		{
			sql+= " (select administracion.getnombreespecialidad(p.especialidad) from odontologia.programas p where p.codigo=pops.programa) as nombreespecialidad ";	
		}
		else
		{
			sql+=" (select administracion.getnombreespecialidad(s.especialidad) from facturacion.servicios s where s.codigo=pops.servicio) as nombreespecialidad ";
		}
		
 		sql +="	FROM  " +
 		"		odontologia.presupuesto_odontologico po  " +
 		"		INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.presupuesto=po.codigo_pk)  " +
 		"		INNER JOIN odontologia.presupuesto_odo_convenio poc ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv)  ";
 		 
		 sql+= " where po.codigo_pk="+codigoPresupuesto + " and  poc.contratado='"+ConstantesBD.acronimoSi+"'";
		 
		 return sql;
	}


	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuestoREQUERIDO
	 * @param programaOservicioREQUERIDO
	 * @param utilizaProgramas
	 * @param piezaDentalOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param superficieOPCIONAL
	 * @return
	 */
	public static boolean eliminarProgramaServicioPresupuestoCascada(	Connection con, 
																		BigDecimal codigoPkPresupuestoREQUERIDO,  
																		int programaOservicioREQUERIDO, 
																		boolean utilizaProgramas, 
																		int piezaDentalOPCIONAL, 
																		int hallazgoREQUERIDO, 
																		int superficieOPCIONAL,
																		String seccionREQUERIDA,
																		String loginUsuario,
																		boolean eliminarFilaProg)
	{
		boolean retorna=false;
		///LO PRIMERO ES ELIMINAR LA PIEZA
		//obtenemos el codigoPk de la pieza
		String consultaStr="SELECT " +
								"pp.codigo_pk as codigo_pk_pieza, " +
								"pops.codigo_pk as codigo_pk_prog_serv, " +
								"pops.cantidad as cantidad, " +
								"coalesce(pp.num_superficies,0) as numsuperficies " +
							"from " +
								"odontologia.presupuesto_piezas pp " +
								"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
							"WHERE " +
								"pops.presupuesto="+codigoPkPresupuestoREQUERIDO+" ";
		
		consultaStr+=(utilizaProgramas)?" and pops.programa = "+programaOservicioREQUERIDO+" ":" and pops.servicio= "+programaOservicioREQUERIDO+" ";
		consultaStr+=(piezaDentalOPCIONAL>0)?" and pp.pieza= "+piezaDentalOPCIONAL+" ":" and pp.pieza is null ";
		consultaStr+=" and pp.hallazgo= "+hallazgoREQUERIDO+" ";
		consultaStr+=(superficieOPCIONAL>0)?" and pp.superficie= "+superficieOPCIONAL+" ":" and pp.superficie is null ";
		consultaStr+=" and pp.seccion= '"+seccionREQUERIDA+"' ";

		logger.info("consulta -->"+consultaStr);
		try 
	    {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				DtoPresupuestoPiezas dtoPieza= new DtoPresupuestoPiezas();
				dtoPieza.setCodigoPk(rs.getBigDecimal("codigo_pk_pieza"));
				
				if(!eliminarPresupuestoPieza(dtoPieza, con))
				{
					logger.error("NO ELIMINA LA PIEZA");
					rs.close();
					ps.close();
					return false;
				}
				else
				{
					///de lo contrario verificamos si la cantidad es mayor a uno para restarle uno, pero si es igual a uno entonces debemos eliminar todo
					if(rs.getInt("cantidad")==1)
					{
						//ELIMINAMOS DETALLE SERVICIOS DEL PROGRAMA DEL CONVENIO
						DtoPresupuestoOdoConvenio dtoConv= new DtoPresupuestoOdoConvenio();
						dtoConv.setPresupuestoOdoProgServ(rs.getBigDecimal("codigo_pk_prog_serv"));
						ArrayList<DtoPresupuestoOdoConvenio> listaConv= cargarPresupuestoConvenio(dtoConv, con);
						for(DtoPresupuestoOdoConvenio dtoCon1: listaConv)
						{	
							DtoPresupuestoDetalleServiciosProgramaDao dtoDet= new DtoPresupuestoDetalleServiciosProgramaDao();
							dtoDet.setPresupuestoOdoConvenio(dtoCon1.getCodigoPK());
							
							if(!eliminarPresupuestoDetalleServiciosProgramaDao(dtoDet, con))
							{
								logger.error("NO ELIMINA CONVENIO");
								rs.close();
								ps.close();
								return false;
							}
						}	
						
						DtoPresupuestoOdoConvenio dtoConvenio= new DtoPresupuestoOdoConvenio();
						dtoConvenio.setPresupuestoOdoProgServ(rs.getBigDecimal("codigo_pk_prog_serv"));
						if(!eliminarPresupuestoOdoConvenio(dtoConvenio, con))
						{
							logger.error("NO ELIMINA CONVENIO");
							rs.close();
							ps.close();
							return false;
						}
						DtoPresuOdoProgServ dtoProgServ= new DtoPresuOdoProgServ();
						dtoProgServ.setCodigoPk(rs.getBigDecimal("codigo_pk_prog_serv"));
						
						
						if(eliminarFilaProg)
						{	
							if(!eliminarPresupuestoProgramaServicio(dtoProgServ, con))
							{
								logger.error("NO ELIMINA programa/servicio ");
								rs.close();
								ps.close();
								return false;
							}
						}	
					}
					else
					{
						logger.info("debemos restarle -1 a la cantidad -->"+rs.getInt("cantidad"));
						
						DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
						dto.setCodigoPk(rs.getBigDecimal("codigo_pk_prog_serv"));
						dto.setCantidad(rs.getInt("cantidad")-1);
						dto.setUsuarioModifica(new DtoInfoFechaUsuario(loginUsuario));
						if(!modificarPresupuestoProgramaServicio(dto, con))
						{
							logger.error("NO ACTUALIZA CANTIDAD programa/servicio ");
							rs.close();
							ps.close();
							return false;
						}
						
					}
				}
				retorna=true;
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR ", e);
			retorna=false;
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param idSesionOPCIONAL
	 * @return
	 */
	public static DtoConcurrenciaPresupuesto estaEnProcesoPresupuesto(Connection con, int cuenta, String idSesionOPCIONAL, boolean igualSession)
	{
		DtoConcurrenciaPresupuesto dto= new DtoConcurrenciaPresupuesto();
		String validacionStr="select " +
								"cuenta as cuenta, " +
								"usuario as usuario, " +
								"getnombreusuario2(usuario) AS nombre_usuario, " +
								"to_char(fecha,'DD/MM/YYYY') as fecha, " +
								"hora as hora, " +
								"id_sesion as id_sesion, " +
								"es_flujo_inclusiones as es_flujo_inclusiones " +
							"from " +
								"odontologia.cuentas_proceso_presupuesto " +
							"WHERE " +
								"cuenta= "+cuenta+" ";
		
		if(!UtilidadTexto.isEmpty(idSesionOPCIONAL))
		{
			if(!igualSession)
			{	
				validacionStr+=" AND id_sesion<> '"+idSesionOPCIONAL+"' ";
			}	
			else
			{
				validacionStr+=" AND id_sesion = '"+idSesionOPCIONAL+"' ";
			}
				
		}
		
		try
		{
			logger.info("estaEnProcesoPresupuesto-->"+validacionStr);
			
			PreparedStatementDecorator verificacion= new PreparedStatementDecorator(con.prepareStatement(validacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(verificacion.executeQuery());
			if(rs.next())
			{
				logger.info("retorna true");
				dto.setCuenta(rs.getDouble("cuenta"));
				dto.setDtoFHU(new DtoInfoFechaUsuario(rs.getString("hora"), rs.getString("fecha"), rs.getString("usuario")));
				dto.setEsFlujoInclusiones(UtilidadTexto.getBoolean(rs.getString("es_flujo_inclusiones")));
				dto.setIdSesion(rs.getString("id_sesion"));
				dto.setNombresUsuario(rs.getString("nombre_usuario"));
			}
			rs.close();
			verificacion.close();
		}
		catch (SQLException e)
		{
			logger.error("Error verificando si la cuenta se encuentra en proceso de facturación "+e);
		}
		return dto;
	}
	
	/**
	 * 
	 * @param idCuenta
	 * @param loginUsuario
	 * @param idSesion
	 * @return
	 */
	public static boolean empezarBloqueoPresupuesto(	int idCuenta,
														String loginUsuario, 
														String idSesion,
														boolean esFlujoInclusiones) 
	{
		boolean retorna=false;
		String procesoStr = "INSERT INTO " +
									"odontologia.cuentas_proceso_presupuesto (cuenta, usuario, fecha, hora, id_sesion, es_flujo_inclusiones ) " +
								"VALUES(?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?)";
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(procesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, idCuenta);
			ps.setString(2,loginUsuario);
			ps.setString(3, idSesion);
			ps.setString(4, (esFlujoInclusiones)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo );
			retorna= ps.executeUpdate()>=0;
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			logger.error("Error insertando el estado de la cuenta en la tabla de proceso de facturación", e);
			e.printStackTrace();
			retorna=false;
		}
		return retorna;
	}


	/**
	 * 
	 * @param con
	 * @return
	 */
	public static int cancelarTodosLosProcesosDePresupuesto(Connection con)
	{
		int retorna=0;
		String consultaStr="DELETE FROM odontologia.cuentas_proceso_presupuesto ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna= ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("Error ", e);
			retorna=0;
		}
		return retorna;
	}



	/**
	 * 
	 * @param idCuenta
	 * @param idSesion
	 * @return
	 */
	public static boolean cancelarProcesoPresupuesto(int idCuenta,
			String idSesion)
	{
		boolean retorna=false;
		String consultaStr="DELETE FROM odontologia.cuentas_proceso_presupuesto WHERE cuenta="+idCuenta+" and id_sesion='"+idSesion+"' ";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna= ps.executeUpdate()>0;
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			logger.error("Error ", e);
			retorna=false;
		}
		return retorna;
	}
	
	
	/**
	 * Método par aobtener el codigo del presupuesto de un plan de tratamiento
	 * por un estado específico
	 * @param codigoPlanTratamiento
	 * @param estado
	 * @return
	 */
	public static BigDecimal consultarCodigoPresupuestoXPlanTratamiento(BigDecimal codigoPlanTratamiento,String estado)
	{
		BigDecimal codigoPresupuesto = new BigDecimal(ConstantesBD.codigoNuncaValido);
		Connection con = UtilidadBD.abrirConexion();
		
		
		
		
		String consulta = "SELECT codigo_pk as codigo_pk FROM odontologia.presupuesto_odontologico WHERE plan_tratamiento = ? ";
		
		consulta+= !UtilidadTexto.isEmpty(estado)?" and estado= '"+estado+"' ": " ";
		
		try
		{
			
		
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setLong(1,codigoPlanTratamiento.longValue());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoPresupuesto = new BigDecimal(rs.getLong("codigo_pk"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCodigoPresupuestoXPlanTratamiento: ",e);
		}
		return codigoPresupuesto;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */

	public static ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarPresupuestoDetalleServicosPrograma(DtoPresupuestoDetalleServiciosProgramaDao dto )
	{
		ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> listaDto = new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
		
		String consultaStr=" select "+
		" opdcs.codigo_pk as codigoPk , "+
			" opdcs.presupuesto_odo_convenio as presupuestoOdoConvenio, "+  
			" opdcs.programa as programa, "+                  
			" opdcs.servicio as servicio , "+     
			" opdcs.valor_unitario_servicio as valorUnitarioServicio ,"+    
			" opdcs.porcentaje_dcto_prom_serv as porcentajeDctoPromServ ,"+  
			" opdcs.valor_descuento_prom_serv as valorDescuentoPromServ ,"+ 
			" opdcs.porcentaje_dcto_bono_serv as porcentajeDctoBonoServ ,"+  
			" opdcs.valor_descuento_bono_serv as valorDescuentoBonoServ ,"+ 
			" opdcs.dcto_comercial_unitario  as dctoComercialUnitario ,"+     
			" opdcs.fecha_modifica as fechaModifica ,"+          
			" opdcs.hora_modifica as horaModifica,"+            
			" opdcs.usuario_modifica as usuarioModifica ,"+          
			" opdcs.error_calculo_tarifa as errorCalculoTarifa, " +
			" opdcs.valor_honorario_prom_serv as valor_honorario_prom_serv, " +
			" opdcs.porc_honorario_prom_serv as porc_honorario_prom_serv "+
			" from odontologia.presu_odo_conv_det_serv_prog opdcs where 1=1 ";
			
		consultaStr+= ( dto.getCodigoPk().doubleValue()>0) ?" and opdcs.codigo_pk="+dto.getCodigoPk().doubleValue(): "";
		consultaStr+= (dto.getPresupuestoOdoConvenio().doubleValue()>0) ?" and opdcs.presupuesto_odo_convenio="+dto.getPresupuestoOdoConvenio().doubleValue(): "";
		consultaStr+= (dto.getPrograma().doubleValue()>0)?" and opdcs.programa = "+dto.getPrograma().doubleValue():"";
		consultaStr+= (dto.getServicio()>0)?" and opdcs.servicio = "+dto.getServicio():"";
			

		
		
		try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPresupuestoDetalleServiciosProgramaDao newdto = new DtoPresupuestoDetalleServiciosProgramaDao();
				
				newdto.setCodigoPk(rs.getBigDecimal("codigoPk"));
				newdto.setPresupuestoOdoConvenio(rs.getBigDecimal("presupuestoOdoConvenio"));
				newdto.setPrograma(rs.getDouble("programa"));
				newdto.setServicio(rs.getInt("servicio"));
				newdto.setValorUnitarioServicio(rs.getBigDecimal("valorUnitarioServicio"));
				
				newdto.setPorcentajeDctoPromocionServicio(rs.getDouble("porcentajeDctoPromServ"));
				newdto.setValorDctoPromocionServicio(rs.getBigDecimal("valorDescuentoPromServ"));
				
				newdto.setPorcentajeDctoBonoServicio(rs.getDouble("porcentajeDctoBonoServ"));
				newdto.setValorDctoBonoServicio(rs.getBigDecimal("valorDescuentoBonoServ"));
				
				newdto.setDctoComercialUnitario(rs.getBigDecimal("dctoComercialUnitario"));
				newdto.getFHU().setFechaModifica(rs.getString("fechaModifica"));
				newdto.getFHU().setHoraModifica(rs.getString("horaModifica"));
				newdto.getFHU().setUsuarioModifica(rs.getString("usuarioModifica"));
			
				newdto.setErrorCalculoTarifa(rs.getString("errorCalculoTarifa"));
				newdto.setValorHonorarioDctoPromocionServicio(rs.getBigDecimal("valor_honorario_prom_serv"));
				newdto.setPorcentajeHonorarioDctoPromocionServicio(rs.getDouble("porc_honorario_prom_serv"));
				
				listaDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		return  listaDto;
		
		
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double insertPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto , Connection con)
	{
		String insertStr="" +
				"insert into odontologia.presu_odo_conv_det_serv_prog  (" +
																			 " codigo_pk ,"+   //1               
																			 " presupuesto_odo_convenio ,"+ //2   
																			 " programa ,"+                 //3
																			 " servicio ,"+                //4
																			 " valor_unitario_servicio ,"+ //5  
																			 " porcentaje_dcto_prom_serv ,"+ //6
																			 " valor_descuento_prom_serv ,"+  //7
																			 " porcentaje_dcto_bono_serv,"+  //8
																			 " valor_descuento_bono_serv,"+  //9
																			 " dcto_comercial_unitario ,"+    //10
																			 " fecha_modifica,"+            //11
																			 " hora_modifica,"+             //12
																			 " usuario_modifica ,"+          //13
																			 " error_calculo_tarifa, " +	//14	
																			 " valor_honorario_prom_serv, " +//15
																			 " porc_honorario_prom_serv," + //16
																			 " esquema_tarifario )" +//17
																			 
																			 "values ( ? , ?, ?, ?, ?, ?, ?, ?, ?, ?" +
																			 			" , ? , ? , ?, ?, ?, ?, ? )"; //16
		
		
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("logger ->"+dto.loggerPromocion());
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		logger.info("=============================================================================================================");
		
		
		double secuencia= ConstantesBD.codigoNuncaValidoDouble;
		
		try{
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con, insertStr);
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_odo_conv_serv_prog"); 
			
			ps.setDouble(1, secuencia);
			
			logger.info("EL CODIGO A ENTRAR ------------->"+dto.getPresupuestoOdoConvenio());
			ps.setBigDecimal(2, dto.getPresupuestoOdoConvenio());
			ps.setDouble(3, dto.getPrograma());
			ps.setInt(4, dto.getServicio());
			ps.setBigDecimal(5, dto.getValorUnitarioServicio());
			
			
			if(dto.getPorcentajeDctoPromocionServicio()>0)
			{
				ps.setDouble(6, dto.getPorcentajeDctoPromocionServicio());
			}
			else
			{
				ps.setNull(6, Types.NUMERIC);
			}
			
			
			if(dto.getValorDctoPromocionServicio().doubleValue()>0)
			{
				ps.setBigDecimal(7, dto.getValorDctoPromocionServicio());
				
			}
			else
			{
				ps.setNull(7, Types.NUMERIC);
			}
			
			
			if(dto.getPorcentajeDctoBonoServicio()>0)
			{
				ps.setDouble(8, dto.getPorcentajeDctoBonoServicio());
			}
			else
			{
				ps.setNull(8, Types.NUMERIC);
			}
			
			if ( dto.getValorDctoBonoServicio().doubleValue()>0 )
			{
				ps.setBigDecimal(9, dto.getValorDctoBonoServicio());
				
			}
			else
			{
				ps.setNull(9, Types.NUMERIC);
			}
			
			
			if(dto.getDctoComercialUnitario().doubleValue()>0)
			{
				ps.setBigDecimal(10, dto.getDctoComercialUnitario());
			}
			else
			{
				ps.setNull(10, Types.NUMERIC);
			}
			
			ps.setString(11, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(12, dto.getFHU().getHoraModifica());
			ps.setString(13, dto.getFHU().getUsuarioModifica());
			
			if(UtilidadTexto.isEmpty(dto.getErrorCalculoTarifa()))
			{
				ps.setString(14, dto.getErrorCalculoTarifa());
			}
			else
			{
				ps.setNull(14,Types.VARCHAR );
			}
			
			if (dto.getValorHonorarioDctoPromocionServicio().doubleValue()>0 )
			{
				ps.setBigDecimal(15, dto.getValorHonorarioDctoPromocionServicio());
			}
			else
			{
				ps.setNull(15, Types.NUMERIC);
			}
			
			if (dto.getPorcentajeHonorarioDctoPromocionServicio()>0 )
			{
				ps.setDouble(16, dto.getPorcentajeHonorarioDctoPromocionServicio());
			}
			else
			{
				ps.setNull(16, Types.DOUBLE);
			}
			
			if (dto.getEsquemaTarifario()>0 )
			{
				ps.setDouble(17, dto.getEsquemaTarifario());
			}
			else
			{
				ps.setNull(17, Types.INTEGER);
			}
			
			if(ps.executeUpdate()>0)
			{
				dto.setCodigoPk(new BigDecimal(secuencia));
				
				ps.close();
				
				return secuencia;
			}
			
			ps.close();
				
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN DETa PRO ", e);
			secuencia=0;
		}

		return secuencia;												
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto, Connection con )
	{
		String eliminarStr=" delete from odontologia.presu_odo_conv_det_serv_prog where 1=1 ";
		eliminarStr+= (dto.getCodigoPk().doubleValue()>0 )?"  and codigo_pk="+dto.getCodigoPk().doubleValue() :" ";
		eliminarStr+= (dto.getPresupuestoOdoConvenio().doubleValue()>0)? " and  presupuesto_odo_convenio="+dto.getPresupuestoOdoConvenio().doubleValue() :"";
			
		logger.info("elim prog/serv detalle contratado-->"+eliminarStr);
		try 
	    {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarStr);
			ps.executeUpdate();
			ps.close();
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Programas  ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoParaDescuento(BigDecimal codigoPresupuesto)
	{
		// Juan David, Este método seguramente va a cambiar, ya que está teniendo en cuenta las inclusiones
		// Implantación me dice que no trabaje mas en este caso (113 - Xplanner2010 20012)
		BigDecimal total= new BigDecimal(0);
		String consulta=" SELECT " +
							"coalesce(sum( (poc.valor_unitario - coalesce(poc.dcto_comercial_unitario, 0)) *pops.cantidad),0) as valor " +
						"from " +
							"odontologia.presupuesto_odo_convenio poc " +
							"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv) " +
						"WHERE " +
							"pops.presupuesto=? " +
							"and poc.contratado='"+ConstantesBD.acronimoSi+"' " +
							"and valor_descuento_prom is null " +
							"and valor_descuento_bono is null " +
							"and porcentaje_descuento_prom is null " +
							"and porcentaje_dcto_bono is null " +
							"and poc.presupuesto_paquete is null " +
							"and pops.inclusion is null";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoPresupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				total= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("cargarTotalPresupuestoParaDescuento ", e);
		}
		return total;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoSinDctoOdon(BigDecimal codigoPresupuesto)
	{
		BigDecimal total= new BigDecimal(0);
		String consulta=" SELECT " +
							"sum( (poc.valor_unitario - coalesce(poc.valor_descuento_prom,0) - coalesce(poc.valor_descuento_bono,0) - coalesce(poc.dcto_comercial_unitario, 0) ) * pops.cantidad) as valor " +
						"from " +
							"odontologia.presupuesto_odo_convenio poc " +
							"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv) " +
						"WHERE " +
							"pops.presupuesto=? " +
							"and poc.contratado='"+ConstantesBD.acronimoSi+"' ";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoPresupuesto);
		    logger.info("CONSULTAAAAAAAA obtenerTotalPresupuestoSinDctoOdon >> "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				total= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("cargarTotalPresupuestoParaDescuento ", e);
		}
		return total;
	}
	
	
	/**
	 * Método para obtener el Valor Total de Presupuesto con Descuento cuando el Presupuesto esta contratado
	 * @param codigoPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoConDctoOdon(BigDecimal codigoPresupuesto)
	{
		BigDecimal totalSinDescuento= new BigDecimal(0);
		BigDecimal totalDescuento = new BigDecimal(0);
		BigDecimal totalConDescuento= new BigDecimal(0);
		totalSinDescuento=obtenerTotalPresupuestoSinDctoOdon(codigoPresupuesto);
		totalDescuento=obtenerValorDctoOdontologicoPresupuesto(codigoPresupuesto);
		if(totalDescuento!=null && totalDescuento.intValue()>0)  
			totalConDescuento=totalSinDescuento.subtract(totalDescuento);
		else
			totalConDescuento=totalSinDescuento;
		//--
		return totalConDescuento;
		
	}
	
	
	/**
	 * Metodo para consultar el valor del descuento que tiene un Presupuesto
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerValorDctoOdontologicoPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		BigDecimal retorna= new BigDecimal(0);
		String consultaStr=	" SELECT coalesce(pdo.valor_descuento, 0) as valor " +
				            "  FROM odontologia.presupuesto_odontologico po " +
				            "   INNER JOIN odontologia.presupuesto_dcto_odon pdo on(pdo.presupuesto=po.codigo_pk) " +
				            "  WHERE po.codigo_pk=? " +
				            "        AND pdo.estado ='"+ConstantesIntegridadDominio.acronimoContratado1+"' " +
				            "        AND po.estado='"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' " ;
				
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoPkPresupuesto);
			logger.info("CONSULTAAAAAAAA obtenerTotalPresupuestoSinDctoOdon >> "+consultaStr +"  presupuesto>>"+codigoPkPresupuesto);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna = rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarDescuentoPresupuesto(Connection con, DtoPresupuestoOdontologicoDescuento dto)
	{
		boolean retorna=false;
		String modificarStr = "update odontologia.presupuesto_dcto_odon set  " +
														"  	fecha_modifica=?  , " +//1
														"	hora_modifica=? ," +//2
														"  	usuario_modifica=? ," + //3
														" 	det_dcto_odo=? , " +//4				
														"	valor_descuento=? , " + //5		
														"	estado=?,  " + //6
														"	porcentaje_dcto=?, "+//7
														"	motivo=?, "+//8
														"	observaciones=?, "+//9
														"	det_dcto_odo_aten=? "+//10
														" WHERE 1=1 ";	
		
		if(dto.getCodigo().doubleValue()>0 || dto.getPresupuesto().doubleValue()>0)
		{	
			modificarStr+= (dto.getCodigo().doubleValue()>0)? " and codigo_pk= "+dto.getCodigo().doubleValue()+" ":" "; 
			modificarStr+= (dto.getPresupuesto().doubleValue()>0)? " and presupuesto= "+dto.getPresupuesto().doubleValue()+" ":" ";												
		
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, modificarStr);
				ps.setDate(1, Date.valueOf(dto.getUsuarioFechaModifica().getFechaModificaFromatoBD()));
				ps.setString(2, dto.getUsuarioFechaModifica().getHoraModifica());
				ps.setString(3, dto.getUsuarioFechaModifica().getUsuarioModifica());
				
				if(dto.getDetalleDescuentoOdontologico()>0)
				{
					ps.setDouble(4, dto.getDetalleDescuentoOdontologico());
				}
				else
				{
					ps.setNull(4, Types.NUMERIC);
				}
				if(dto.getValorDescuento().doubleValue()>0)
				{	
					ps.setBigDecimal(5, dto.getValorDescuento());
				}
				else
				{
					ps.setNull(5, Types.NUMERIC);
				}
				ps.setString(6, dto.getEstado());
				
				if(dto.getPorcentajeDcto()>0)
				{
					ps.setDouble(7, dto.getPorcentajeDcto());
				}
				else
				{
					ps.setNull(7, Types.NUMERIC);
				}
				if(dto.getMotivo()>0)
				{
					ps.setDouble(8, dto.getMotivo());
				}
				else
				{
					ps.setNull(8, Types.NUMERIC);
				}
				if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
				{
					ps.setString(9, dto.getObservaciones());
				}
				else
				{
					ps.setNull(9, Types.VARCHAR);
				}
				if(dto.getDetalleDescuentoOdontologicoAtencion()>0)
				{
					ps.setDouble(10, dto.getDetalleDescuentoOdontologicoAtencion());
				}
				else
				{
					ps.setNull(10, Types.NUMERIC);
				}
				
				retorna= ps.executeUpdate()>0;
				ps.close();
			}
			catch (SQLException e) 
			{
				logger.error("modificarDescuentoPresupuesto ", e);
			}
		
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @param loginUsuario
	 * @param codigoMotivo
	 * @return
	 */
	public static boolean anularDescuentoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto, String loginUsuario, double codigoMotivo)
	{
		boolean retorna=false;
		String modificarStr = "update odontologia.presupuesto_dcto_odon set  " +
														"  	fecha_modifica=current_date  , " +
														"	hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" ," +
														"  	usuario_modifica='"+loginUsuario+"' ," + 
														"	estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado+"',  " +
														"	motivo="+codigoMotivo+", "+
														"	observaciones='Solicitud anulada desde el proceso de cancelación del Presupuesto' "+
														" WHERE presupuesto= ? ";
		
		logger.info("anularDescuentoPresupuesto-->"+modificarStr);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, modificarStr);
			ps.setBigDecimal(1, codigoPkPresupuesto);
			retorna= ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("modificarDescuentoPresupuesto ", e);
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fhuModifica
	 * @param estado
	 * @param codigoPk
	 * @return
	 */
	public static boolean cambiarEstadoDescuentoPresupuesto(Connection con, DtoInfoFechaUsuario fhuModifica, String estado, BigDecimal codigoPk)
	{
		boolean retorna=false;
		String modificarStr = "update odontologia.presupuesto_dcto_odon set  " +
														"  	fecha_modifica=?  , " +//1
														"	hora_modifica=? ," +//2
														"  	usuario_modifica=? ," + //3
														"	estado=?  " + //4
														" WHERE codigo_pk= ?"; //5
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, modificarStr);
			ps.setDate(1, Date.valueOf(fhuModifica.getFechaModificaFromatoBD()));
			ps.setString(2, fhuModifica.getHoraModifica());
			ps.setString(3, fhuModifica.getUsuarioModifica());
			ps.setString(4, estado);
			ps.setBigDecimal(5, codigoPk);
			
			retorna= ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("cambiarEstadoDescuentoPresupuesto ", e);
		}
		return retorna;
	}
	
	/**
	 * Metodo que carga los Presupuesto PreContratados 
	 * Un presupuesto tiene solicitud de Descuento si tiene registro en la tabla odontologia.presupuesto_dcto_odon, 
	 * Sin importar el estado en el que se encuentre la solicitud de autorización de descuentos Odontológico.
	 * 
	 * @param ingreso
	 * @return
	 */
	public static ArrayList<InfoPresupuestoPrecontratado> cargarPresupuestosPresontratados(int ingreso, BigDecimal presupuestoNotIn)
	{
		ArrayList<InfoPresupuestoPrecontratado> lista= new ArrayList<InfoPresupuestoPrecontratado>();
		String consulta=" SELECT " +
							"po.codigo_pk as codigo_pk, " +
							"coalesce(sum( (poc.valor_unitario - coalesce(poc.dcto_comercial_unitario, 0))  *pops.cantidad),0) as valorpresupuestoparadcto, " +
							"pdo.estado as estadosoldcto, " +
							"coalesce(pdo.valor_descuento, 0) as valorautorizadodcto,   " +
							"pdo.codigo_pk as codigopkdcto " +
						"from " +
							"odontologia.presupuesto_odontologico po " +
							"inner join odontologia.presupuesto_odo_prog_serv pops on(pops.presupuesto=po.codigo_pk) " +
							"INNER JOIN odontologia.presupuesto_odo_convenio poc ON(pops.codigo_pk=poc.presupuesto_odo_prog_serv) " +
							//"inner join odontologia.presupuesto_dcto_odon pdo on(pdo.presupuesto=po.codigo_pk and pdo.estado not in ('"+ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado+"')) " +
							"inner join odontologia.presupuesto_dcto_odon pdo on(pdo.presupuesto=po.codigo_pk ) "+
						"where " +
							"po.ingreso=? " +
							"and po.estado='"+ConstantesIntegridadDominio.acronimoPrecontratado+"'  " +
							"and poc.contratado='"+ConstantesBD.acronimoSi+"' " +
							"AND poc.valor_descuento_prom is null " +
							"and poc.valor_descuento_bono is null " +
							"and poc.porcentaje_descuento_prom is null " +
							"and poc.porcentaje_dcto_bono is null " +
							"and poc.presupuesto_paquete is null " +
							"and pdo.codigo_pk = (" +
													"select " +
														"max(pdo_interno.codigo_pk) " +
													"from " +
														"odontologia.presupuesto_dcto_odon pdo_interno " +
													"where " +
														"pdo.presupuesto=pdo_interno.presupuesto" +
												") " +
							"and po.codigo_pk<>"+presupuestoNotIn+"  " +
							"group by po.codigo_pk, pdo.estado, pdo.valor_descuento, pdo.codigo_pk  ";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, ingreso);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoPresupuestoPrecontratado info= new InfoPresupuestoPrecontratado();
				info.setCodigoPkPresuDctoOdo(rs.getBigDecimal("codigopkdcto"));
				info.setCodigoPkPresupuesto(rs.getBigDecimal("codigo_pk"));
				info.setConsecutivo(rs.getBigDecimal("codigo_pk"));
				info.setEstadoSolicitudAutorizacionDcto(rs.getString("estadosoldcto"));
				info.setValorAutorizadoDcto(rs.getBigDecimal("valorautorizadodcto"));
				info.setValorPresupuesto(rs.getBigDecimal("valorpresupuestoparadcto"));
				lista.add(info);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("cambiarEstadoDescuentoPresupuesto ", e);
		}
		return lista;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param estadoNuevo
	 * @return
	 */
	public static boolean cambiarEstadoPresupuesto(Connection con, BigDecimal codigoPk, String estadoNuevo)
	{
		boolean retorna=false;
		String consulta=" update odontologia.presupuesto_odontologico set estado='"+estadoNuevo+"', fecha_modifica=current_date, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where codigo_pk=? ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoPk);
			retorna= ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("cambiarEstadoDescuentoPresupuesto ", e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean modificarIndicativoContratadoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		boolean retorna= true;
		String consulta=" update odontologia.presupuesto_odo_convenio set contratado='"+ConstantesBD.acronimoNo+"' " +
							"where " +
								"codigo_pk in (" +
													"select " +
														"poc.codigo_pk " +
													"FROM " +
														"odontologia.presupuesto_odontologico po " +
														"INNER JOIN odontologia.presupuesto_odo_prog_serv pops on(pops.presupuesto= po.codigo_pk) " +
														"inner join odontologia.presupuesto_odo_convenio poc on(poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
													"where " +
														"po.codigo_pk=? " +
														"and poc.contratado='"+ConstantesBD.acronimoSi+"' " +
												") ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			logger.info("\n\n\n modificarIndicativoContratadoPresupuesto-->"+consulta+" ->"+codigoPkPresupuesto);
			ps.setBigDecimal(1, codigoPkPresupuesto) ;
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("cambiarEstadoDescuentoPresupuesto ", e);
			retorna= false;
		}
		return retorna;
		
	}

	/**
	 * 
	 * @param codigoPaciente
	 * @param estados
	 * @return
	 */
	public static boolean existePresupuestoXPaciente(int codigoPaciente, ArrayList<String> estados) 
	{
		boolean retorna=false;
		String consultaStr="SELECT count(1) AS existe FROM odontologia.presupuesto_odontologico WHERE codigo_paciente=?";
		if(estados!= null && estados.size() > 0)
		{
			consultaStr += " AND estado IN("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		}

		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, codigoPaciente);
			ResultSetDecorator rsd=new ResultSetDecorator(ps.executeQuery());
			if(rsd.next())
			{
				retorna= rsd.getInt("existe")>0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rsd, con);
		}
		catch (SQLException e) {
			logger.error("Error consultando existencia presupuesto paciente "+codigoPaciente,e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean reversarAnticiposPresupuestoContratado(Connection con, BigDecimal codigoPkPresupuesto)
	{
		boolean retorna=true;
		//primero obtenemos la informacion del presupuesto que tenga el indicativo de reserva anticipo
		String consultaStr= "select distinct " +
								"poc.contrato as contrato, " +
								"(coalesce(poc.valor_unitario,0) - coalesce(poc.valor_descuento_prom, 0) - coalesce(poc.valor_descuento_bono, 0) - coalesce(poc.dcto_comercial_unitario, 0)) as valor, " +
								"coalesce(pp.num_superficies,1) as numerosuperficies, " +
								"pp.hallazgo as hallazgo, " +
								"pp.superficie as superficie, " +
								"pp.pieza as pieza, " +
								"pops.programa as programa, " +
								"(" +
									"(" +
										"select sum(1) from " +
											"(" +
												"select " +
													"distinct " +
													"pspt.det_plan_tratamiento, " +
													"pspt.programa " +
												"from  " +
													"odontologia.presupuesto_odontologico po1 " +
													"INNER JOIN odontologia.plan_tratamiento pt on(pt.codigo_pk=po1.plan_tratamiento) " +
													"INNER JOIN odontologia.det_plan_tratamiento dpt ON(dpt.plan_tratamiento=pt.codigo_pk) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON (pspt.det_plan_tratamiento=dpt.codigo_pk) " +
												"where " +
													"po1.codigo_pk=po.codigo_pk " +
													"and pspt.estado_programa='"+ConstantesIntegridadDominio.acronimoContratado+"' " +
													"and pspt.programa=pops.programa " +
													"and dpt.hallazgo=pp.hallazgo " +
													"and dpt.seccion=pp.seccion " +
													"and " +
														"(" +
															"(" +
																"dpt.pieza_dental=pp.pieza " +
																"and (dpt.superficie=pp.superficie OR dpt.superficie IS NULL) " +
																"and dpt.seccion in('"+ConstantesIntegridadDominio.acronimoOtro+"', '"+ConstantesIntegridadDominio.acronimoDetalle+"')" +
															") " +
															"or " +
															"(" +
																"dpt.seccion='"+ConstantesIntegridadDominio.acronimoBoca+"'" +
															")" +
														")" +
												")" +
												"tabla" +
										")" +
								") as cantidad_plan_contratada " +
								"from  " +
									"odontologia.presupuesto_odontologico po " +
									"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.presupuesto=po.codigo_pk) " +
									"INNER JOIN odontologia.presupuesto_odo_convenio poc ON(poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
									"INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"where " +
									"po.codigo_pk=? " +
									"and poc.contratado='"+ConstantesBD.acronimoSi+"' " +
									"and poc.reserva_anticipo='"+ConstantesBD.acronimoSi+"' ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ REVERSAR ANTICIPOS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			ps.setBigDecimal(1, codigoPkPresupuesto) ;
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			logger.info("\n");
			logger.info(" Reversar Anticipos PresupuestoContratado"+ps);
			logger.info("\n");
			
			
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				BigDecimal cantidadExactaContratadaPlanT= (new BigDecimal(rs.getDouble("cantidad_plan_contratada")).divide(new BigDecimal(rs.getInt("numerosuperficies")), 5, RoundingMode.HALF_EVEN));
				logger.info(" Cantidad Contratada == "+cantidadExactaContratadaPlanT);
				if(cantidadExactaContratadaPlanT.doubleValue()>0)
				{
					SqlBaseControlAnticiposContratoDao.modificarValorAnticipoReservadoPresupuesto(con, rs.getInt("contrato"), rs.getBigDecimal("valor").multiply(cantidadExactaContratadaPlanT).multiply(new BigDecimal(-1)));
				}
			}
			rs.close();
			ps.close();
			
		}
		catch (SQLException e) 
		{
			logger.error("cambiarEstadoDescuentoPresupuesto ", e);
			retorna= false;
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static double obtenerPorcentajeDctoOdonContratadoPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		double porcentaje= 0;
		String consultaStr=" SELECT " +
								"coalesce(pdo.porcentaje_dcto,0) as porcentaje_dcto, " +
								"coalesce(pdo.valor_descuento,0) as valor_descuento, " +
								"(" +
									"SELECT " +
										"sum( coalesce(v.valor_subtotal_contratado,0)) " +
									"FROM " +
										"odontologia.view_presupuesto_totales_conv v " +
									"WHERE " +
										"v.presupuesto=pdo.presupuesto" +
								") as valortotalpresupuesto " +
							"FROM " +
								"odontologia.presupuesto_dcto_odon pdo " +
							"WHERE " +
								"pdo.presupuesto=? and estado='"+ConstantesIntegridadDominio.acronimoContratado1+"'";
		
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ obtenerDctoOdonContratadoPresupuesto $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			logger.info("\n\n\n obtenerDctoOdonContratadoPresupuesto-->"+consultaStr+" ->"+codigoPkPresupuesto);
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			ps.setBigDecimal(1, codigoPkPresupuesto) ;
			ResultSetDecorator rs= new ResultSetDecorator( ps.executeQuery());
			
			if(rs.next())
			{
				if(rs.getDouble("porcentaje_dcto")>0)
				{
					porcentaje= rs.getDouble("porcentaje_dcto");
				}
				else
				{
					if(rs.getBigDecimal("valor_descuento").doubleValue()>0)
					{
						porcentaje= rs.getBigDecimal("valor_descuento").multiply(new BigDecimal(100)).divide(rs.getBigDecimal("valortotalpresupuesto"), 2, RoundingMode.HALF_EVEN).doubleValue();
					}	
				}
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("obtenerDctoOdonContratadoPresupuesto ", e);
		}
		return porcentaje;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal insertarPlanTtoPresupuesto(Connection con, DtoPlanTratamientoPresupuesto dto, boolean utilizaProgramas)
	{
		BigDecimal codigoPk= BigDecimal.ZERO;
		codigoPk= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_plan_tto_prog_ser"));
		dto.setCodigoPk(codigoPk);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarPlanTtoPresupuesto);
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ insertarPlanTtoPresupuesto $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			logger.info("\n\n\n insertarPlanTtoPresupuesto-->"+insertarPlanTtoPresupuesto+" ->");
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			ps.setBigDecimal(1, dto.getCodigoPk()) ;
			ps.setBigDecimal(2, dto.getDetPlanTratamiento());
			if(utilizaProgramas)
			{
				ps.setBigDecimal(3, dto.getPrograma());
			}
			else
			{
				ps.setNull(3, Types.NUMERIC);
			}
			ps.setInt(4, dto.getServicio());
			ps.setBigDecimal(5, dto.getPresupuesto());
			
			ps.setString(6, dto.getFHU().getUsuarioModifica());
			ps.setDate(7, Date.valueOf(dto.getFHU().getFechaModificaFromatoBD()));
			ps.setString(8, dto.getFHU().getHoraModifica());
			
			if(dto.getProgramaServicioPlanTratamientoFK().doubleValue()>0)
			{	
				ps.setBigDecimal(9, dto.getProgramaServicioPlanTratamientoFK());
			}	
			else
			{
				ps.setNull(9, Types.NUMERIC);
			}
			
			ps.setString(10, UtilidadTexto.convertirSN(dto.isActivo()+""));
			
			if(ps.executeUpdate()<=0)
			{
				codigoPk= BigDecimal.ZERO;
			}
			
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("insertarPlanTtoPresupuesto ", e);
			codigoPk= BigDecimal.ZERO;
		}
		return codigoPk;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean eliminarPlanTtoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		boolean retorna= false;
		String consultaStr="delete from odontologia.presu_plan_tto_prog_ser where presupuesto=? ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPkPresupuesto) ;
			ps.executeUpdate();
			ps.close();
			retorna=true;
		}
		catch (SQLException e) 
		{
			logger.error("obtenerDctoOdonContratadoPresupuesto ", e);
		}
		return retorna;
	}

	/**
	 * 
	 * @param codigoPkProgramaServicioPlanTto
	 * @param detPlanTratamiento
	 * @param programa
	 * @param servicio
	 * @return
	 */
	public static boolean actualizarPlanTtoPresupuestoProgServ(
			Connection con,
			BigDecimal codigoPkProgramaServicioPlanTto,
			BigDecimal detPlanTratamiento, InfoDatosDouble programa,
			int servicio, BigDecimal codigoPkPresupuesto) 
	{
		boolean retorna= false;
		String consultaStr="update odontologia.presu_plan_tto_prog_ser set programa_servicio_plan_t="+codigoPkProgramaServicioPlanTto+" " +
							"where " +
								"presupuesto=? " +
								"and det_plan_tratamiento="+detPlanTratamiento+" "+
								"and servicio= "+servicio+" " +
								"and programa_servicio_plan_t is null " ;
								consultaStr+=(programa.getCodigo()>0)? "and programa="+programa.getCodigo()+" ":"and programa is null ";
								
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPkPresupuesto) ;
			ps.executeUpdate();
			ps.close();
			retorna=true;
		}
		catch (SQLException e) 
		{
			logger.error("actualizarPlanTtoPresupuestoProgServ ", e);
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPKPresupuestoConvenio
	 * @param advertenciaBono
	 * @param valorDescuentoBono
	 * @param porcentajeDctoBono
	 * @param serialBono
	 * @return
	 */
	public static boolean modificarBonos(	Connection con,
			DtoPresupuestoOdoConvenio dtoConvenio) 
	{

		boolean retorna= false;
		//1. si no es seleccionado entonces todo es null
		String consultaStr= "UPDATE odontologia.presupuesto_odo_convenio " +
								"SET serial_bono=?, " +
								"valor_descuento_bono=?, " +
								"adventencia_bono=?, " +
								"porcentaje_dcto_bono=?, " +
								"sel_porcent_bono=? " +
							"where codigo_pk=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			if(!dtoConvenio.getSeleccionadoBono() || dtoConvenio.getSerialBono().doubleValue()<=0)
			{
				ps.setNull(1, Types.NUMERIC);
			}
			else
			{
				ps.setBigDecimal(1, dtoConvenio.getSerialBono());
			}
			if(!dtoConvenio.getSeleccionadoBono() || dtoConvenio.getValorDescuentoBono().doubleValue()<=0)
			{
				ps.setNull(2, Types.NUMERIC);
			}
			else
			{
				ps.setBigDecimal(2, dtoConvenio.getValorDescuentoBono());
			}
			if(!dtoConvenio.getSeleccionadoBono() || UtilidadTexto.isEmpty(dtoConvenio.getAdvertenciaBono()))
			{
				ps.setNull(3, Types.VARCHAR);
			}
			else
			{
				ps.setString(3, dtoConvenio.getAdvertenciaBono());
			}
			if(!dtoConvenio.getSeleccionadoBono() || dtoConvenio.getPorcentajeDctoBono()<=0)
			{
				ps.setNull(4, Types.DOUBLE);
			}
			else
			{
				ps.setDouble(4, dtoConvenio.getPorcentajeDctoBono());
			}
			if(!dtoConvenio.getSeleccionadoBono())
			{
				ps.setNull(5, Types.VARCHAR);
			}
			else
			{
				ps.setString(5, UtilidadTexto.convertirSN( dtoConvenio.isSeleccionadoPorcentajeBono()+""));
			}
			
			ps.setBigDecimal(6, dtoConvenio.getCodigoPK());
			ps.executeUpdate();
			ps.close();
			
			retorna= modificarBonosHijos(con, dtoConvenio.getCodigoPK(), dtoConvenio.getPorcentajeDctoBono(), dtoConvenio.getSeleccionadoBono());
			
		}
		catch (SQLException e) 
		{
			logger.error("modificarBonos ", e);
			e.printStackTrace();
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPKPresupuestoConvenio
	 * @param porcentajeDctoBono
	 * @return
	 */
	private static boolean modificarBonosHijos(	Connection con,	
												BigDecimal codigoPKPresupuestoConvenio, 
												double porcentajeDctoBono,
												boolean seleccionado) 
	{
		boolean retorna= false;
		String consultaStr="UPDATE odontologia.presu_odo_conv_det_serv_prog set " +
								"porcentaje_dcto_bono_serv=?, ";
								
		consultaStr+= (seleccionado)?"valor_descuento_bono_serv=( (valor_unitario_servicio - coalesce(dcto_comercial_unitario, 0)) *"+porcentajeDctoBono+"/100)":"valor_descuento_bono_serv=null ";
	
		consultaStr+= "where " +
						"presupuesto_odo_convenio=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			
			if(!seleccionado || porcentajeDctoBono<=0)
			{
				ps.setNull(1, Types.DOUBLE);
			}
			else
			{
				ps.setDouble(1, porcentajeDctoBono);
			}
			ps.setBigDecimal(2, codigoPKPresupuestoConvenio);
			ps.executeUpdate();
			ps.close();
			
			retorna=true;
			
		}
		catch (SQLException e) 
		{
			logger.error("modificarBonosHijos ", e);
			e.printStackTrace();
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPKPresupuestoConvenio
	 * @param advertenciaPromocion
	 * @param valorDescuentoPromocion
	 * @param porcentajePromocion
	 * @param valorHonorarioPromocion
	 * @param porcentajeHonorarioPromocion
	 * @return
	 */
	public static boolean modificarPromociones(	Connection con,
												DtoPresupuestoOdoConvenio	dtoConvenio) 
	{
		
		boolean retorna= false;
		//1. si no es seleccionado entonces todo es null
		String consultaStr= "UPDATE odontologia.presupuesto_odo_convenio " +
								"SET det_promocion=?, " +
								"porcentaje_descuento_prom=?, " +
								"advertencia_prom=?, " +
								"valor_honorario_prom=?, " +
								"valor_descuento_prom=?, " +
								"sel_porcent_prom=? " +
							"where " +
								"codigo_pk=?";
		
		BigDecimal detallePromocion = new BigDecimal( dtoConvenio.getDetallePromocion());
		
		{
			
		}
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			if(!dtoConvenio.getSeleccionadoPromocion() || detallePromocion.doubleValue()<=0)
			{
				ps.setNull(1, Types.NUMERIC);
			}
			else
			{
				ps.setBigDecimal(1, detallePromocion);
			}
			if(!dtoConvenio.getSeleccionadoPromocion() || dtoConvenio.getPorcentajePromocion()<=0)
			{
				ps.setNull(2, Types.DOUBLE);
			}
			else
			{
				ps.setDouble(2, dtoConvenio.getPorcentajePromocion());
			}
			if(!dtoConvenio.getSeleccionadoPromocion() || UtilidadTexto.isEmpty(dtoConvenio.getAdvertenciaPromocion()))
			{
				ps.setNull(3, Types.VARCHAR);
			}
			else
			{
				ps.setString(3, dtoConvenio.getAdvertenciaPromocion());
			}
			if(!dtoConvenio.getSeleccionadoPromocion() || dtoConvenio.getValorHonorarioPromocion().doubleValue()<=0)
			{
				ps.setNull(4, Types.NUMERIC);
			}
			else
			{
				ps.setBigDecimal(4, dtoConvenio.getValorHonorarioPromocion());
			}
			if(!dtoConvenio.getSeleccionadoPromocion() || dtoConvenio.getValorDescuentoPromocion().doubleValue()<=0)
			{
				ps.setNull(5, Types.NUMERIC);
			}
			else
			{
				ps.setBigDecimal(5, dtoConvenio.getValorDescuentoPromocion());
			}
			
			if(!dtoConvenio.getSeleccionadoPromocion())
			{
				ps.setNull(6, Types.VARCHAR);
			}
			else
			{
				ps.setString(6, UtilidadTexto.convertirSN( dtoConvenio.isSeleccionadoPorcentajePromocion()+""));
			}
			
			
			ps.setBigDecimal(7, dtoConvenio.getCodigoPK());
			ps.executeUpdate();
			ps.close();
			retorna=modificarPromocionesHijas(con,  dtoConvenio.getCodigoPK(), dtoConvenio.getPorcentajePromocion(), dtoConvenio.getPorcentajeHonorarioPromocion(), dtoConvenio.getSeleccionadoPromocion());
		}
		catch (SQLException e) 
		{
			logger.error("modificarPromociones ", e);
			e.printStackTrace();
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPKPresupuestoConvenio
	 * @param porcentajePromocion
	 * @param porcentajeHonorarioPromocion
	 * @param seleccionado
	 * @return
	 */
	private static boolean modificarPromocionesHijas(	Connection con,
														BigDecimal codigoPKPresupuestoConvenio, 
														double porcentajePromocion,
														Double porcentajeHonorarioPromocion, 
														boolean seleccionado) 
	{
		boolean retorna= false;
		String consultaStr="UPDATE odontologia.presu_odo_conv_det_serv_prog set " +
								"porcentaje_dcto_prom_serv=?, " +
								"porc_honorario_prom_serv=?, ";
								
		consultaStr+= (seleccionado)?"valor_descuento_prom_serv=( (valor_unitario_servicio - coalesce(dcto_comercial_unitario, 0)) *"+porcentajePromocion+"/100), ":"valor_descuento_prom_serv=null, ";
		consultaStr+= (seleccionado)?"valor_honorario_prom_serv=( (valor_unitario_servicio - coalesce(dcto_comercial_unitario, 0)) *"+porcentajeHonorarioPromocion+"/100)":"valor_honorario_prom_serv=null ";
		
		consultaStr+= "where " +
						"presupuesto_odo_convenio=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			
			if(!seleccionado || porcentajePromocion<=0)
			{
				ps.setNull(1, Types.DOUBLE);
			}
			else
			{
				ps.setDouble(1, porcentajePromocion);
			}
			if(!seleccionado || porcentajeHonorarioPromocion<=0)
			{
				ps.setNull(2, Types.DOUBLE);
			}
			else
			{
				ps.setDouble(2, porcentajeHonorarioPromocion);
			}
			ps.setBigDecimal(3, codigoPKPresupuestoConvenio);
			ps.executeUpdate();
			ps.close();
			
			retorna=true;
			
		}
		catch (SQLException e) 
		{
			logger.error("modificarPromocionesHijas ", e);
			e.printStackTrace();
		}
		return retorna;
	}
	
	/**
	 * metodo para cargar las citas odontologicas que se deben cancelar para poder cancelar un presupuesto
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoCitasPresupuestoOdo> cargarCitasPresupuestoOdontologico(int codigoPaciente)
	{
		ArrayList<DtoCitasPresupuestoOdo> array= new ArrayList<DtoCitasPresupuestoOdo>();
		
		String consultaStr= " SELECT " +
								"c.codigo_pk as codigo_pk, " +
								"getintegridaddominio(c.tipo) as tipo, " +
								"a.centro_atencion as codigocentroaten, " +
								"getnomcentroatencion(a.centro_atencion) as nomcentroaten, " +
								"c.estado as acronimoestado, " +
								"getintegridaddominio(c.estado) as estado, " +
									"CASE WHEN " +
										"c.estado='"+ConstantesIntegridadDominio.acronimoProgramado+"' THEN to_char(c.fecha_programacion, 'DD/MM/YYYY') " +
									"ELSE " +
										"to_char(a.fecha,'DD/MM/YYYY') ||' '|| c.hora_inicio  end as fechahoracita, " +
									"CASE WHEN " +
										"c.estado='"+ConstantesIntegridadDominio.acronimoProgramado+"' THEN " +
												"(" +
													"SELECT " +
														"CASE WHEN " +
															"m.codigo_medico is null then 'No asignado' " +
														"else p.primer_nombre||' '||p. segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido end as nombre " +
													"from " +
														"usuarios s " +
														"INNER JOIN personas p ON(s.codigo_persona=p.codigo) " +
														"LEFT OUTER JOIN medicos m ON(m.codigo_medico=p.codigo) " +
													"where " +
														"s.login=c.usuario_modifica" +
												") " +
										"else  " +
												"(" +
													"SELECT " +
														"CASE WHEN " +
															"a.codigo_medico is null then 'No asignado' " +
														"else p.primer_nombre||' '||p. segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido end as nombre " +
													"from " +
														"personas p " +
													"WHERE " +
														"p.codigo=a.codigo_medico" +
												") " +
										"end as profesional   " +
								"FROM " +
									"odontologia.citas_odontologicas c  " +
									"LEFT OUTER JOIN odontologia.agenda_odontologica a ON(a.codigo_pk= c.agenda) " +
								"where " +
									"c.codigo_paciente=? " +
									"and c.estado in('"+ConstantesIntegridadDominio.acronimoProgramado+"', '"+ConstantesIntegridadDominio.acronimoReservado+"', '"+ConstantesIntegridadDominio.acronimoAsignado+"', '"+ConstantesIntegridadDominio.acronimoAreprogramar+"') ";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, codigoPaciente);
			
			
			logger.info("----------------------------------"+ps.toString());
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoCitasPresupuestoOdo dto= new DtoCitasPresupuestoOdo();
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("codigocentroaten"), rs.getString("nomcentroaten")));
				dto.setCodigo(rs.getBigDecimal("codigo_pk"));
				dto.setEstado(rs.getString("estado"));
				dto.setFechaHora(rs.getString("fechahoracita"));
				dto.setProfesional(rs.getString("profesional"));
				dto.setTipo(rs.getString("tipo"));
				dto.setAcronimoEstado(rs.getString("acronimoestado"));
				
				array.add(dto);
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("modificarPromocionesHijas ", e);
			e.printStackTrace();
		}
		
		return array;
	}

	/**
	 * 
	 * @param utilizaProgramas
	 * @param codigoPKPresupuesto
	 * @return
	 */
	public static boolean puedoTerminarPresupuesto(boolean utilizaProgramas,BigDecimal codigoPKPresupuesto) 
	{
		boolean retorna= false;
		String consultaStr=	"SELECT " +
								"po.plan_tratamiento as plan, ";
								consultaStr+=(!utilizaProgramas)
								?"pops.servicio as servprog, "
								:"pops.programa as servprog, ";
								consultaStr+=
								"coalesce(pp.pieza, "+ConstantesBD.codigoNuncaValido+") as pieza, " +
								"pp.hallazgo as hallazgo, " +
								"coalesce(pp.superficie, "+ConstantesBD.codigoNuncaValido+") as superficie, " +
								"pp.seccion as seccion " +
							"FROM " +
								"odontologia.presupuesto_odontologico po " +
								"INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.presupuesto=po.codigo_pk) " +
								"INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"INNER JOIN odontologia.presupuesto_odo_convenio poc on(poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
							"where " +
								"pops.presupuesto=? and poc.contratado='"+ConstantesBD.acronimoSi+"' ";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPKPresupuesto);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			boolean existeTerminado= false;
			boolean existePendienteOContratado= false;
			
			while(rs.next())
			{
				String estado= obtenerEstadoProgramaServicioPlanT(rs.getInt("pieza"), rs.getInt("superficie"), rs.getInt("hallazgo"), rs.getBigDecimal("servprog"), utilizaProgramas, rs.getBigDecimal("plan"));
				
				if(	estado.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) 
					|| estado.equals(ConstantesIntegridadDominio.acronimoContratado) 
					|| estado.equals(ConstantesIntegridadDominio.acronimoEnProceso) 
					|| estado.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) 
					)
					
				{
					existePendienteOContratado=true; // no puede terminar prespuesto
					break;
				}
				else if(estado.equals(ConstantesIntegridadDominio.acronimoTerminado) || estado.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
				{
					existeTerminado=true;
				}
				
			}
			//no debe existir pendiente o contratado y debe existir al menos uno terminado
			logger.info("existePendienteOContratado-->"+existePendienteOContratado+" existeTerminado->"+existeTerminado);
			retorna= (!existePendienteOContratado) && existeTerminado;
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("modificarPromocionesHijas ", e);
			e.printStackTrace();
		}
		
		/*
		 * METODO ANTIGUO 
	select   opst.estado_servicio from 
	odontologia.plan_tratamiento opl 
	INNER JOIN odontologia.det_plan_tratamiento odpl ON(odpl.plan_tratamiento=opl.codigo_pk) 
	inner join odontologia.programas_servicios_plan_t opst ON(opst.det_plan_tratamiento=odpl.codigo_pk and  opst.estado_servicio not in  ('COT') ) 
	inner join odontologia.programas_servicios_plan_t ps2 on (ps2.det_plan_tratamiento=odpl.codigo_pk and ps2.estado_servicio in ('PEN') )  
	where    opl.codigo_pk=877;
		 */
		
		return retorna;
	}
	
	
	

	
	/**
	 * 
	 * @param piezaOPCIONAL
	 * @param superficieOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param codigoPkProgramaServicio
	 * @param utilizaProgramas
	 * @param codigoPkPlan
	 * @param estadoOPCIONAL
	 * @return
	 */
	private static String obtenerEstadoProgramaServicioPlanT(int piezaOPCIONAL, 
															int superficieOPCIONAL, 
															int hallazgoREQUERIDO,
															BigDecimal codigoPkProgramaServicio, 
															boolean utilizaProgramas, 
															BigDecimal codigoPkPlan)
	{
		String retorna= "";
		String consultaStr=		" SELECT distinct ";
									consultaStr+=
									utilizaProgramas
									?"opst.estado_programa "
									:"opst.estado_servicio ";
								consultaStr+=	
								"FROM " +
									"odontologia.plan_tratamiento opl  " +
									"INNER JOIN odontologia.det_plan_tratamiento odpl ON(odpl.plan_tratamiento=opl.codigo_pk) " +
									"INNER JOIN odontologia.programas_servicios_plan_t opst ON(opst.det_plan_tratamiento=odpl.codigo_pk) " +
								"WHERE " +
									"opl.codigo_pk="+codigoPkPlan+" " +
									"AND odpl.activo='"+ConstantesBD.acronimoSi+"' ";
		
		consultaStr+=(utilizaProgramas)?" and opst.programa="+codigoPkProgramaServicio:" and opst.servicio="+codigoPkProgramaServicio;
		consultaStr+=(piezaOPCIONAL>0)?" and odpl.pieza_dental="+piezaOPCIONAL:" and odpl.pieza_dental is null "; 
				
		if(superficieOPCIONAL > 0){
		
			if (superficieOPCIONAL > 2){
				
				consultaStr+=" and odpl.superficie="+superficieOPCIONAL; 
			
			}else{
				
				consultaStr+=" and (odpl.superficie="+superficieOPCIONAL+" OR odpl.superficie IS NULL)"; 
			}
			
		}else{
			
			consultaStr+=" and odpl.superficie is null " ;
		}
		
		consultaStr+=" and odpl.hallazgo= "+hallazgoREQUERIDO;
		logger.info("consulta--->"+consultaStr);
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
			    retorna = rs.getString(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static double obtenerDctoOdontologicoPresupuestoXPlan(BigDecimal codigoPkPlanTratamiento)
	{
		double retorna=0;
		String consultaStr=	"SELECT " +
								"coalesce(pdo.porcentaje_dcto, 0) as porcentaje " +
							"from " +
								"odontologia.presupuesto_odontologico po " +
								"inner join odontologia.presupuesto_dcto_odon pdo on(pdo.presupuesto=po.codigo_pk) " +
							"WHERE " +
								"po.plan_tratamiento=? " +
								"and pdo.estado = '"+ConstantesIntegridadDominio.acronimoContratado1+"' " +
								"and po.estado='"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' ";
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoPkPlanTratamiento);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna = rs.getDouble(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
		
	}
	
	/**
	 * CARGAR EL HISTORICO LOG PRESUPUESTO 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static DtoLogPrespuesto cargarDtoLogPresupuesto(DtoLogPrespuesto dto)
	{
		
		
		DtoLogPrespuesto dtoLog = new DtoLogPrespuesto();
		
		String consultaStr= "select  distinct " +
				"codigo_pk as codigoPk , " +
				"presupuesto as presupuesto ," +
				"estado as estado," +
				"consecutivo as consecutivo ,  " +
				"fecha_generacion as fechaGeneracion " +
				"from  odontologia.log_presupuesto_odonto " +
				"WHERE 1=1";
		
		/*
		 * FILTRO
		 */
		consultaStr+=" AND plan_tratamiento = "+dto.getCodigoPkPlantTratamiento()+" ";
		consultaStr+=" ORDER BY codigo_pk desc limit 1";
		
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			Log4JManager.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoLog.setCodigoPk(rs.getBigDecimal("codigoPk"));
				dtoLog.setCodigoPkPresupuesto(rs.getBigDecimal("presupuesto"));
				//dtoLog.setCodigoPkPlantTratamiento(rs.getBigDecimal("planTratamiento"));
				dtoLog.setFechaGeneracion(rs.getString("fechaGeneracion"));
				//dtoLog.setHoraGeneracion(rs.getString("horaGeneracion"));
				dtoLog.setEstado(rs.getString("estado"));
				dtoLog.setConsecutivo(rs.getBigDecimal("consecutivo"));
			}
			
		}
		catch(SQLException e )
		{
			Log4JManager.info(e);
		}
		
		
		return dtoLog;
	}
	
	
	
	
	
	
	/**
	 * METODO QUE GUARDA LA CLAUSULA DE CONTRATO 
	 * NOTA SE COLOCO DE UN MUCHOS CON  PRESUPUESTO CONTRATADO PARA LA FLEXIBILIDAD ->PERO SU FUNCIONAMIENTO DE UNO A UNO 
	 * TAMBIEN EN INSTITUCION SE PUEDE COLOCAR MUCHOS CONTRATOS ODONTOLOGICOS
	 *  
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean guardarContratoPresupuestoClausula(Connection con, DtoPresuContratoOdoImp dto)
	
	{
	
		boolean retorno=Boolean.FALSE;
		
		
		String insertStr=" insert into" +
					" odontologia.presu_contrato_odo_imp " +
					"(" +
					" codigo_pk , "+
					" codigo_presu_contrato_odo ,"+
					" clausulas  ,"+
					" pie_pagina , "+
					" usuario_modifica , "+ 
					" fecha_modifica , "+
					" hora_modifica  ) " +
					"values " +
					"(  ?, ? , ? , ? , ? , ?, ?  )"; 

		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertStr);
			
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_contrato_odo_imp"); // Por ejecutar 
			
			ps.setDouble(1, secuencia);
			
			Double tmpCodigoPk = new Double(secuencia);
			dto.setCodigoPk(tmpCodigoPk.longValue());
			
			ps.setDouble(2, dto.getCodigoPresuContratoOdo());
			
			if(!UtilidadTexto.isEmpty(dto.getClausulas()))
			{
				ps.setString(3, dto.getClausulas());
			}
			else
			{
				ps.setNull(3, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(dto.getPiePagina()))
			{
				ps.setString(4,dto.getPiePagina());
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			
			ps.setString(5, dto.getUsuarioModifica());
			ps.setString(6, dto.getFechaModifica());
			ps.setString(7, dto.getHoraModifica());
			
			
			//Log4JManager.info(ps);
			
			if(ps.executeUpdate()>0)
			{
				retorno=Boolean.TRUE;
			}
			
			
			/*
			 * GUARDAR EL DETALLE
			 */
			if(retorno)
			{
				for(DtoFirmasContratoOtrosiInst  dtoFirmas : dto.getListaFirmasContrato())
				{
					dtoFirmas.setContratoOdo(dto.getCodigoPk());
					guadarFirmasContrato(con , dtoFirmas );
				}
			}
			
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null , null);
	
		}
		catch(SQLException e)
		{
			Log4JManager.error(e);
		}
		
		
		
		return retorno;
			
	}
	
	
	
	
	
	/**
	 * METODO PARA GUARDAR LA LISTA DE PRESUPUESTO FIRMA CONTRATO
	 * PARAMETRIZADA EN INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param dtoFirmas
	 * @return
	 */
	public static boolean    guadarFirmasContrato(Connection con , DtoFirmasContratoOtrosiInst  dtoFirmas )
	{
		boolean retorno=Boolean.FALSE;
		
		String  insertStr =" " +
						" insert into odontologia.presu_firmas_contrato " +
						"" +
						"( codigo_pk  ,"+
						" cpresu_contrato_odo ,"+
						" numero ,"+
						" label_debaja_firma ,"+
						" firma_digital ,"+
						" adjunto_firma ,"+
						" empresa_institucion " +
						")" +
						"values ( ? , ? , ? , ? ,?, ?, ? )";
		
		
		
		try 
		{
			double secuencia=ConstantesBD.codigoNuncaValidoDouble;
			
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertStr);
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_firmas_contrato"); // Por ejecutar 
			
			
			ps.setDouble(1, secuencia);
			ps.setDouble(2, dtoFirmas.getContratoOdo());
			ps.setInt(3, dtoFirmas.getNumero());
			
			
			if(!UtilidadTexto.isEmpty(dtoFirmas.getLabelDebajoFirma()) )
			{
				ps.setString(4, dtoFirmas.getLabelDebajoFirma());
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			
			
			ps.setString(5, dtoFirmas.getFirmaDigital());
			
			
			if(!UtilidadTexto.isEmpty(dtoFirmas.getAdjuntoFirma()))
			{
				ps.setString(6, dtoFirmas.getAdjuntoFirma());
			}
			else
			{
				ps.setNull(6, Types.VARCHAR);
			}
			
			
			ps.setString(7, dtoFirmas.getEmpresaInstitucion());
			
			Log4JManager.info(ps);
			
			if(ps.executeUpdate()>0)
			{
				retorno=Boolean.TRUE;
			}
			
			
			
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null , null);
			
			
		
		}
		
		catch(SQLException e)
		{
			Log4JManager.error(e);
			Log4JManager.info(e);
			
		}
			
		
		return retorno;
	}
	
	
	
	
	
	
	
	/**
	 *	Metodo que cargar el Valor del Descuento para el Presupuesto.
	 *	Recibe un el codigo_pk del Presupuesto y retorna el Valor de Descuento 
	 * @author Edgar Carvajal Ruiz
	 * @param codigoPkPresupuesto
	 */
	public static  BigDecimal cargarValorPresupuesto( BigDecimal codigoPkPresupuesto )
	{
		
		String consulta="select  pdo.valor_descuento as valorDescuento   from  odontologia.presupuesto_dcto_odon  pdo  where pdo.presupuesto=? and estado not in ('"+ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAnulado+"')";
		
		
		BigDecimal valorPresupuesto= BigDecimal.ZERO;
		
		
		if(codigoPkPresupuesto!=null && codigoPkPresupuesto.doubleValue()<=0)
		{
				
		}
		
		
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoPkPresupuesto);
		
			Log4JManager.info("Consultar Valor de descuento Presupuesto Sql \n "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				valorPresupuesto=rs.getBigDecimal("valorDescuento");
			}
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
			
		}
		catch(SQLException e )
		{
			Log4JManager.info(e);
		}
		finally {
			
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		}
		
		
		return valorPresupuesto;
	}
	
	
	
	

									

	/**
	 * Metodo para cargar el historicos de los ingreso del un paciente
	 * Este metodo carga lo ingreso de un paciente que tiene presupuesto
	 * @author Edgar Carvajal Ruiz
	 * @param paciente
	 * @param viaIngreso
	 * @param institucion
	 * @return
	 */
	public static List<DtoHistoricoIngresoPresupuesto> listaIngresoHistoricosPresupuesto(int paciente , int viaIngreso , int institucion ){
		
		
			List<DtoHistoricoIngresoPresupuesto> listaIngreso= new ArrayList<DtoHistoricoIngresoPresupuesto>();
			
			
			String consultaStr =" select  "+
								" distinct ig.id as ingreso ,"+ 
								" ig.consecutivo as ingresoConsecutivo , "+
								" ig.estado as estadoIngreso , "+
								" ig.fecha_ingreso as fechaIngreso , "+
								" ig.hora_ingreso as horaIngreso , "+
								" getnombreestadocuenta(cu.estado_cuenta) as estadoCuenta, "+
								" po.centro_atencion as codCentroAtencion, " +
								
								" (select ca.descripcion from administracion.centro_atencion ca where ca.consecutivo= po.centro_atencion) as nombreCentroAtencion "+
								
								
								"" +
								" from "+ 
								"		odontologia.presupuesto_odontologico po "+ 
								"	inner join "+ 
								"		manejopaciente.ingresos  ig on(po.ingreso=ig.id) "+ 
								"	inner join "+ 
								"		manejoPaciente.cuentas cu ON(ig.id=cu.id_ingreso) "+ 
								
								" where cu.via_ingreso= ? and ig.institucion= ? and  ig.codigo_paciente=? " +
								
								" order by ig.fecha_ingreso desc ";
			
			
			
			
			
			Connection con=null;
			
			try 
			{
					con= UtilidadBD.abrirConexion();
			    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			    	
			    	ps.setInt(1, viaIngreso);
			    	ps.setInt(2 , institucion);
			    	ps.setInt(3,  paciente);
			    	
			    	Log4JManager.info(ps);
			    	
			    	ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			    	while(rs.next())
			    	{
			    		DtoHistoricoIngresoPresupuesto dto = new DtoHistoricoIngresoPresupuesto();
			    		dto.setNoIngreso(rs.getInt("ingreso")+"");
			    		dto.setConsecutivoIngreso(rs.getString("ingresoConsecutivo"));
			    		dto.setEstadoIngreso(rs.getString("estadoIngreso"));
			    		dto.setEstadoCuenta(rs.getString("estadoCuenta"));
			    		dto.setFechaIngreso(rs.getString("fechaIngreso"));
			    		dto.setCentroAtencion(rs.getString("nombreCentroAtencion"));
			    	
			    		
			    		listaIngreso.add(dto);
			    	}
			    	UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			    	
			}
			catch (Exception e) 
			{
			
			}
			finally{
				
				UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			}
			
			
			return listaIngreso;
		
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> obtenerPresupuestoOdoContratadoPromo(DtoReportePresupuestosOdontologicosContratadosConPromocion dto) 
	{
		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> dtoResultado=new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
		Connection con = UtilidadBD.abrirConexion();
		String consulta="SELECT distinct" +
								" centro_atencion_duenio as centroatencion," +
								" getnomcentroatencion(centro_atencion_duenio) as nombrecentroatencion," +
								" per.tipo_identificacion as tipoidentificacion," +
								" prom.codigo_pk as codigopromocion," +
								" prom.nombre as nombrepromocion," +
								" prom.fecha_inicial_vigencia as fechainicialvigencia," +
								" prom.fecha_final_vigencia as fechafinalvigencia," +
								" prog.codigo_programa as codigoprograma," +
								" prog.nombre as nombreprograma," +
								" per.numero_identificacion as numeroidentificacion," +
								" per.primer_nombre as prinom," +
								" per.segundo_nombre as segnom," +
								" per.primer_apellido as priap," +
								" per.segundo_apellido as segap," +
								" getnombrepersona(pac.codigo_paciente) as paciente," +
								" poc.fecha_modifica as fechacontrato," +
								" po.codigo_pk as numeropresupuesto," +
								" cont.numero_contrato as numerocontrato," +
								" getintegridaddominio(po.estado) as estadopresupuesto," +
								" getnombreusuario(poc.usuario_modifica) as nombreusuario," +
								" pers.primer_nombre as prinomuscont," +
								" pers.segundo_nombre as segnomuscont," +
								" pers.primer_apellido as priapuscont," +
								" pers.segundo_apellido as segapuscont," +
								//" vista.valor_subtotal_contratado as totalcontratado," +
								" pops.cantidad*poc.valor_descuento_prom as valorpromocion,"+
								" ins.razon_social as razsolinstitucion," +
								" ins.ubicacion_logo_reportes as ublogoreporteins," +
								" inm.razon_social as razsolinstempresa," +
								" inm.ubicacion_logo_reportes as ublogoreporteinstemp," +
								" ca.consecutivo as centAtenContCon," +
								" ca.descripcion as centAtenCont," +
								" pa.descripcion as pais," +
								" rc.descripcion as region, " +
								" ins.razon_social, " +
								" inm.razon_social, " +
								" ca.descripcion, " +
								" prog.codigo_programa, " +
								" poc.fecha_modifica, " +
								" po.estado," +
								" poc.convenio," +
								" poc.contrato," +
								" getnombreciudad(ca.pais,ca.departamento,ca.ciudad) as nombreciudad " +
								
						//"from view_presupuesto_totales_conv vista " +
						//"inner join presupuesto_odontologico po on po.codigo_pk=vista.presupuesto " +
						"from presupuesto_odontologico po  " +
						"inner join odontologia.presupuesto_odo_prog_serv pops on pops.presupuesto=po.codigo_pk " +
						"inner join presupuesto_odo_convenio poc on (poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
						"inner join usuarios usu on (usu.login=poc.usuario_modifica) " +
						"inner join personas pers on(usu.codigo_persona=pers.codigo) " +
						"inner join pacientes pac on  (pac.codigo_paciente=po.codigo_paciente) " +
						"inner join personas per on(pac.codigo_paciente=per.codigo) " +
						"inner join contratos cont on (cont.codigo=poc.contrato) " +
						"inner join programas prog on (prog.codigo=pops.programa) " +
						"inner join centro_atencion ca on ca.consecutivo=po.centro_atencion " +
						"inner join regiones_cobertura rc on ca.region_cobertura=rc.codigo " +
						"inner join paises pa on ca.pais=pa.codigo_pais " +
						"inner join det_promociones_odo dpo on(prog.codigo=dpo.programa_odontologico) " +
						"inner join promociones_odontologicas prom on(dpo.promocion_odontologia=prom.codigo_pk) " +
						"left outer join instituciones ins on(ins.codigo=ca.cod_institucion) " +
						"left outer join empresas_institucion inm on(inm.codigo=ca.empresa_institucion) " +
						"where poc.contratado='"+ConstantesBD.acronimoSi+"' and poc.valor_descuento_prom is not null and po.estado!='"+ConstantesIntegridadDominio.acronimoInactivo+"'"; 
		
		try
		{
			// and vista.presupuesto=
			// fecha
			if(!UtilidadTexto.isEmpty(dto.getFechaInicialFormateada()) && !UtilidadTexto.isEmpty(dto.getFechaFinalFormateada()))
				consulta=consulta+" and poc.fecha_modifica between '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicialFormateada())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinalFormateada())+"'";
			//ciudad
			if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + "")) {
				
				String vec[]= dto.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
				dto.setCodigoCiudad(vec[0]);
				dto.setCodigoDpto(vec[1]);
				dto.setCodigoPais(vec[2]);
				consulta=consulta+" and ca.ciudad='"+dto.getCodigoCiudad()+"' and ca.pais='"+dto.getCodigoPais()+"' and ca.departamento='"+dto.getCodigoDpto()+"'";
			} 
			//PAIS
			if (!UtilidadTexto.isEmpty(dto.getCodigoPaisResidencia())&&!dto.getCodigoPaisResidencia().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
				consulta=consulta+" and ca.pais='"+dto.getCodigoPaisResidencia()+"'";
			}
			//region
			if (dto.getCodigoRegion() > 0) 
			{
				consulta=consulta+" and ca.region_cobertura='"+dto.getCodigoRegion()+"'";
			}
			//institucion multi-empresa
			if(dto.isEsMultiempresa()==true){
				if (dto.getCodigoEmpresaInstitucion()> 0) {
				consulta=consulta+" and ca.empresa_institucion='"+dto.getCodigoEmpresaInstitucion()+"'";
				}
			}else{
				if (dto.getCodigoInstitucion()> 0) {
				consulta=consulta+" and ca.cod_institucion='"+dto.getCodigoEmpresaInstitucion()+"'";
				}
			}
			//centro de atencion
			if (dto.getConsecutivoCentroAtencion() > 0) {
				consulta=consulta+" and ca.consecutivo='"+dto.getConsecutivoCentroAtencion()+"'";
			}
			//indicativocontrato
			if(!UtilidadTexto.isEmpty(dto.getIndicativoContrato()) && !dto.getIndicativoContrato().equals(ConstantesBD.codigoNuncaValido + "")){
				consulta=consulta+" and po.estado='"+dto.getIndicativoContrato()+"'";
			}
			//promocion
			if(dto.getCodigoPromocionOdontologica() > 0){
				consulta=consulta+" and prom.codigo_pk='"+dto.getCodigoPromocionOdontologica()+"'";
			}
			//programa
			if((dto.getCodigoPrograma()!=ConstantesBD.codigoNuncaValidoLong) && (dto.getCodigoPrograma()>0)){
				consulta=consulta+" and prog.codigo='"+dto.getCodigoPrograma()+"'";
			}
			//profesionalque contrato
			if(!UtilidadTexto.isEmpty(dto.getLoginProfesionalContrato()) && !dto.getLoginProfesionalContrato().equals(ConstantesBD.codigoNuncaValido + "")){
				consulta=consulta+" and poc.usuario_modifica='"+dto.getLoginProfesionalContrato()+"'";
			}
			
			consulta=consulta+" ORDER BY ins.razon_social, inm.razon_social, ca.consecutivo, prom.codigo_pk, prog.codigo_programa, poc.fecha_modifica, po.estado";
			
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			Log4JManager.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoConsolidadoPresupuestoContratadoPorPromocion dtoInterno=new DtoConsolidadoPresupuestoContratadoPorPromocion();
				dtoInterno.setDescCentroAtencionDuenio(rs.getString("nombrecentroatencion"));
				dtoInterno.setTipoId(rs.getString("tipoidentificacion"));
				dtoInterno.setCodigoPkPromocion(rs.getInt("codigopromocion"));
				dtoInterno.setNombrePromocion(rs.getString("nombrepromocion"));
				dtoInterno.setFechaInicialvige(rs.getDate("fechainicialvigencia"));
				dtoInterno.setFechaFinalvige(rs.getDate("fechafinalvigencia"));
				dtoInterno.setCodigoPrograma(rs.getString("codigoprograma"));
				dtoInterno.setNombrePrograma(rs.getString("nombreprograma"));
				dtoInterno.setNumeroId(rs.getString("numeroidentificacion"));
				dtoInterno.setNombreCompletoPaciente(rs.getString("paciente"));
				dtoInterno.setFechaContrato(rs.getDate("fechacontrato"));
				dtoInterno.setConsecutivoPresupuesto(rs.getLong("numeropresupuesto"));
				dtoInterno.setNumeroContrato(rs.getString("numerocontrato"));
				dtoInterno.setAyudanteEstadoPresupuesto(rs.getString("estadopresupuesto"));
				dtoInterno.setNombreCompletoProfesionalContrato(rs.getString("nombreusuario"));
				dtoInterno.setValorDescuentoProm(rs.getBigDecimal("valorpromocion"));
				dtoInterno.setConsCentroAtencionContrato(rs.getInt("centAtenContCon"));
				dtoInterno.setDescCentroAtencionContrato(rs.getString("centAtenCont"));
				dtoInterno.setDescripcionPais(rs.getString("pais"));
				dtoInterno.setDescripcionRegionCobertura(rs.getString("region"));
				dtoInterno.setDescripcionCiudad(rs.getString("nombreciudad"));
				dtoInterno.setPrimerNombre(rs.getString("prinom"));
				dtoInterno.setSegundoNombre(rs.getString("segnom"));
				dtoInterno.setPrimerApellido(rs.getString("priap"));
				dtoInterno.setSegundoApellido(rs.getString("segap"));
				dtoInterno.setPrimerNombreProfesionalContrato(rs.getString("prinomuscont"));
				dtoInterno.setSegundoNombreProfesionalContrato(rs.getString("segnomuscont"));
				dtoInterno.setPrimerApellidoProfesionalContrato(rs.getString("priapuscont"));
				dtoInterno.setSegundoNombreProfesionalContrato(rs.getString("segapuscont"));
				//institucion multi-empresa
				if(dto.isEsMultiempresa()==true){
					dtoInterno.setDescripcionEmpresaInstitucion(rs.getString("razsolinstempresa"));
					//dtoInterno.setUbicacionLogoReporte(rs.getString("ublogoreporteinstemp"));
					
				}else{
					dtoInterno.setDescripcionEmpresaInstitucion(rs.getString("razsolinstitucion"));
					//dtoInterno.setUbicacionLogoReporte(rs.getString("ublogoreporteins"));
				}
				
				//consultar el total contratado
				//dtoInterno.setValorPresupuesto(rs.getBigDecimal("totalcontratado"));
				String consultaInterna="SELECT valor_subtotal_contratado from view_presupuesto_totales_conv where presupuesto="+rs.getLong("numeropresupuesto")+" and convenio="+rs.getInt("convenio")+" and contrato="+rs.getInt("contrato");
				Log4JManager.info("consulta interna - "+consultaInterna);
				PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,consultaInterna);
				ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				if(rsInterno.next())
				{
					dtoInterno.setValorPresupuesto(rsInterno.getBigDecimal(1));
				}
				else
				{
					dtoInterno.setValorPresupuesto(new BigDecimal(0));
				}

				
				///cargar todo el dto.
				//
				dtoResultado.add(dtoInterno);
			}
		}
		catch(Exception e)
		{
			Log4JManager.error("ERROR",e);
			
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return dtoResultado;
	}
}