package com.princetonsa.dao.sqlbase.cargos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Errores;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoBonoDcto;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoOdontologico;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoProgramaServicio;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseCargosOdonDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCargosDao.class);
	
	/**
	 * Metodo para obtener el esquema tarifario ligado al presupuesto
	 * @param servicio
	 * @param contrato
	 * @param fechaCalculoVigencia
	 * @return
	 */
	public static InfoDatosInt obtenerEsquemaTarifario(int servicio, int contrato, String fechaCalculoVigencia)
	{
		logger.info("**************************************************************************************************************");
		logger.info("OBTENER ESQUEMA TARIFARIO!!!!!");
		String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual():fechaCalculoVigencia));
		InfoDatosInt info= new InfoDatosInt();
		try
		{
			Connection con= UtilidadBD.abrirConexion(); 
			String cadena="SELECT grupo_servicio as gruposervicio from servicios where codigo = "+servicio;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("1-->"+cadena);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				int grupo=rs.getInt(1);
				cadena="SELECT esquema_tarifario as codigo, getnombreesquematarifario(esquema_tarifario) as nombre  from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and grupo_servicio="+grupo+" order by fecha_vigencia desc";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				logger.info("2-->"+cadena);
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					info.setCodigo(rs.getInt(1));
					info.setNombre(rs.getString(2));
				}
				else
				{
					cadena="SELECT esquema_tarifario as codigo, getnombreesquematarifario(esquema_tarifario) as nombre from esq_tar_procedimiento_contrato where fecha_vigencia <='"+fecha+"' and contrato="+contrato+" and grupo_servicio is null order by fecha_vigencia desc";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					logger.info("3-->"+cadena);
					rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					{
						info.setCodigo(rs.getInt(1));
						info.setNombre(rs.getString(2));
					}
				}
			}
			///se cierran conexiones
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		logger.info("**************************************************************************************************************");
		return info;
	}
	
	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public static HashMap<String, String> obtenerFiltrosPromociones(final BigDecimal cuenta)
	{
		HashMap<String, String> mapa= new HashMap<String, String>();
		
		//PRIMERO AGREGAMOS LA INFO DEL PACIENTE
		String consultaStr="SELECT " +
								"getedad(p.fecha_nacimiento) as edad, " +
								"p.sexo as sexo, " +
								"p.estado_civil as estadocivil, " +
								"pac.ocupacion as ocupacion, " +
								"coalesce(pac.nro_hijos, 0) as nro_hijos " +
							"from " +
								"cuentas c " +
								"INNER JOIN personas p ON (p.codigo=c.codigo_paciente) " +
								"INNER JOIN pacientes pac ON (p.codigo=pac.codigo_paciente) " +
							"where " +
								"c.id= "+cuenta.intValue();
		
		mapa.put("numRegistros", "0");
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n  obtenerFiltrosPromociones--->"+consultaStr);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				mapa.put("edad", rs.getInt("edad")+"");
				mapa.put("sexo", rs.getInt("sexo")+"");
				mapa.put("estadocivil", rs.getString("estadocivil"));
				mapa.put("ocupacion", rs.getInt("ocupacion")+"");
				mapa.put("nro_hijos", rs.getInt("nro_hijos")+"");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		//2.DESPUES AGRAGAMOS LA INFO DEL CENTRO DE ATENCION
		int consecutivoCentroAtencionCuenta= UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(cuenta.intValue()).getCodigo();
		
		CentroAtencion centroAtencion= new CentroAtencion();
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			centroAtencion.consultar(con, consecutivoCentroAtencionCuenta);
		}
		catch (Errores e)
		{
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		
		mapa.put("regionCentroAntencion", centroAtencion.getRegionCobertura().getCodigo()+"");
		mapa.put("ciudadCentroAtencion", centroAtencion.getCiudad());
		mapa.put("deptoCentroAtencion", centroAtencion.getDepartamento());
		mapa.put("centroAtencion", consecutivoCentroAtencionCuenta+"");
		
		Utilidades.imprimirMapa(mapa);
		
		return mapa;
		
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public static int obtenerContrato(int ingreso, int convenio)
	{
		String consultaStr="select contrato from manejopaciente.sub_cuentas where ingreso=? and convenio=?  ";
		int retorna=0;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion(); 
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ingreso);
			ps.setInt(2, convenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getInt(1);
			}
			///se cierran conexiones
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return retorna;
	}

	/**
	 * Obtener el valor de descuento de los bonos
	 * @param cuenta Serial del bono a consultar
	 * @param institucion Institución a la cual pertenece el usuario
	 * @param valorTarifa Valor de la tarifa del servicio para comparaciones
	 * @param programaExcluyente Programa al cual está asociado el bono
	 * @param servicioExcluyente Servicio al cual está asociado el bono
	 * @param convenio Convenio en el cual se desea contratar el presupuesto
	 * @param fechaApp Fecha actual para verificar vigencia de los bonos
	 * @return {@link InfoBonoDcto} Tarifa cargada con el bono
	 */
	public static InfoBonoDcto obtenerDescuentosBono(final int cuenta, final int institucion, BigDecimal valorTarifa, double programaExcluyente, double servicioExcluyente, final int convenio, String fechaApp)
	{
		InfoBonoDcto info= new InfoBonoDcto();
		String consultaStr=
				"SELECT " +
						"b.codigo_pk AS codigo_pk, " +
						"b.numero_serial AS numero_serial, " +
						"b.valor_descuento AS valor, " +
						"b.porcentaje_descuentos AS porcentaje, " +
						"b.usuario_modifica AS usuario_modifica, " +
						"b.fecha_modifica AS fecha_modifica, " +
						"b.hora_modifica AS hora_modifica " +
					"FROM " +
						"facturacion.bonos_conv_ing_pac b " +
					"INNER JOIN " +
						"facturacion.convenios_ingreso_paciente cip " +
							"ON(cip.codigo_pk=b.conve_ing_pac) " +
					"INNER JOIN " +
						"cuentas c " +
							"ON(c.codigo_paciente=cip.paciente) " +
					"INNER JOIN " +
						"emision_bonos_desc ebd " +
							"ON(b.emision=ebd.codigo)" +
					"WHERE " +
							"b.utilizado='"+ConstantesBD.acronimoNo+"' " +
						"AND " +
							"c.id=? " +
						"AND " +
							"ebd.institucion=? " +
						"AND " +
							"ebd.convenio_patrocinador=? " +
						"AND " +
							"? BETWEEN ebd.fecha_vigencia_inicial AND ebd.fecha_vigencia_final ";
		
		int codigoProgramaOServicio=ConstantesBD.codigoNuncaValido;
		if(programaExcluyente>0)
		{
			consultaStr+="AND ebd.programa=? ";
			codigoProgramaOServicio=((Double)programaExcluyente).intValue();
		}
		else if(servicioExcluyente>0)
		{
			//OJO ESTE NO EXISTE EN DOCUMENTACION
			consultaStr+="AND ebd.servicio=? ";
			codigoProgramaOServicio=((Double)servicioExcluyente).intValue();
		}
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, cuenta);
			ps.setInt(2, institucion);
			ps.setInt(3, convenio);
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaApp)));
			if(codigoProgramaOServicio!=ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(5, codigoProgramaOServicio);
			}
			Log4JManager.info("obtener BONO-->"+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				info.setBonoPaciente(rs.getInt("codigo_pk"));
				info.setSerial(rs.getBigDecimal("numero_serial"));
				info.setValorDcto(rs.getBigDecimal("valor"));
				info.setPorcentajeDescuento(rs.getDouble("porcentaje"));
				
				if(info.getValorDcto()!=null && info.getValorDcto().doubleValue()>0)
				{
					info.setSeleccionadoPorcentaje(false);
					if(info.getValorDcto().doubleValue()>valorTarifa.doubleValue())
					{	
						info.setValorDctoCALCULADO(valorTarifa);
						info.setPorcentajeDescuento(100);
					}	
					else
					{	
						info.setValorDctoCALCULADO(info.getValorDcto());
						info.setPorcentajeDescuento(info.getValorDcto().multiply(new BigDecimal(100)).divide(valorTarifa, 5, RoundingMode.HALF_EVEN).doubleValue());
					}	
				}
				else if(rs.getDouble("porcentaje")>0)
				{
					info.setSeleccionadoPorcentaje(true);
					info.setValorDctoCALCULADO(valorTarifa.multiply(new BigDecimal(rs.getDouble("porcentaje")/100)));
				}
			}
			///se cierran conexiones
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return info;
	}
	

	/**
	 * Liberar el bono al cancelar el presupuesto odontológico
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @param serial
	 * @param programa
	 * @return
	 */
	public static boolean liberarSerialBono(Connection con, final int convenio, final int ingreso, BigDecimal serial, int programa)
	{
		boolean resultado= false;
		String consultaStr=	"UPDATE " ;
								
		
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consultaStr);
			resultado=ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error liberando los bonos", e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param centroAtencion
	 * @param fechaCalculoVigencia
	 * @param valorPresupuestoXConvenio
	 * @return
	 */
	public static InfoDctoOdontologicoPresupuesto obtenerDescuentoOdon(final int centroAtencion, final String fechaCalculoVigencia, BigDecimal valorPresupuesto)
	{
		InfoDctoOdontologicoPresupuesto info= new InfoDctoOdontologicoPresupuesto();
	
		String consultaStr=	" SELECT " +
								"ddo.codigo as codigo, " +
								"ddo.porcentaje_descuento as porcentaje_descuento " +
							"FROM " +
								"odontologia.descuentos_odon do1 " +
								"INNER JOIN odontologia.det_descuentos_odon ddo ON (ddo.consecutivo_descuento=do1.consecutivo) " +
							"where " +
								"do1.centro_atencion="+centroAtencion+" " +
								"and ('"+UtilidadFecha.conversionFormatoFechaABD(fechaCalculoVigencia)+"' between do1.fecha_ini_vigencia and do1.fecha_fin_vigencia) " +
								"and ("+valorPresupuesto+" between ddo.valor_min_presupuesto and ddo.valor_max_presupuesto) ";
		
		logger.info("DESCUENTO ODONTY------------------------>"+consultaStr+" $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		try
		{
			Connection con= UtilidadBD.abrirConexion(); 
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				info.setDetalleDctoOdon(rs.getBigDecimal("codigo"));
				if(rs.getBigDecimal("porcentaje_descuento").doubleValue()>0)
				{	
					info.setValorDctoCALCULADO(valorPresupuesto.multiply(rs.getBigDecimal("porcentaje_descuento").divide(new BigDecimal(100),  5, RoundingMode.HALF_EVEN)));
				}
				info.setPorcentajeDcto(rs.getDouble("porcentaje_descuento"));
			}
			///se cierran conexiones
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
			
		return info;
	}
	
	
	/**
	 * Método para cargar la información del presupuesto contratado de un servicio especifico
	 * @param con
	 * @param codigoPresuOdoProgSer
	 * @param infoRespCobertura
	 * @param infoTarifa
	 * @param servicio
	 */
	public static void obtenerInfoPresupuestoContratadoProgSer(	Connection con,
																int codigoPresuOdoProgSer,
																InfoResponsableCobertura infoRespCobertura,
																InfoTarifaServicioPresupuesto infoTarifa, 
																boolean utilizaProgramasInstitucion, 
																int servicio,
																BigDecimal codigoPkPlanTratamiento)
	{
		try
		{
			String consulta =(!utilizaProgramasInstitucion)?
			"SELECT " +
				"poc.valor_unitario as valor_unitario," +
				"coalesce(poc.valor_descuento_prom,0) as valor_dcto_prom, " +
				"coalesce(poc.valor_descuento_bono,0) as valor_dcto_bono,"+
				"coalesce(poc.dcto_comercial_unitario,0) as valor_dcto_comercial, " +
				"coalesce(poc.valor_honorario_prom,0) as valor_honorario_prom_serv, " +
				"coalesce(poc.porcentaje_descuento_prom, 0) as porcentaje_dcto_prom_serv, "+
				"coalesce(poc.porcentaje_dcto_bono, 0) as porcentaje_dcto_bono_serv, "+
				"0 as porc_honorario_prom_serv, "+ // FIXME, Este campo no se para que se utiliza
				"poc.convenio as codigo_convenio," +
				"poc.contrato as codigo_contrato, " +
				"coalesce(dpoc.codigo_pk,0) as detallepaqueteconvenio, " +
				"coalesce(dpoc.esquema_tarifario,0) as esquematar " +
			"from " +
				"odontologia.presupuesto_odo_convenio poc " +
				"LEFT OUTER JOIN odontologia.presupuesto_paquetes pp ON(pp.codigo_pk=poc.presupuesto_paquete) " +
				"LEFT OUTER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk=pp.det_paq_odon_convenio) " +
			"WHERE " +
				"poc.presupuesto_odo_prog_serv = ? " +
				"and poc.contratado = '"+ConstantesBD.acronimoSi+"' "
			:
			"SELECT " +
				"p.valor_unitario_servicio as valor_unitario," +
				"coalesce(p.valor_descuento_prom_serv,0) as valor_dcto_prom, " +
				"coalesce(p.valor_descuento_bono_serv,0) as valor_dcto_bono,"+
				"coalesce(p.dcto_comercial_unitario,0) as valor_dcto_comercial,"+
				"coalesce(p.valor_honorario_prom_serv,0) as valor_honorario_prom_serv, " +
				"coalesce(p.porcentaje_dcto_prom_serv, 0) as porcentaje_dcto_prom_serv, "+
				"coalesce(p.porcentaje_dcto_bono_serv, 0) as porcentaje_dcto_bono_serv, "+
				"coalesce(p.porc_honorario_prom_serv, 0) as porc_honorario_prom_serv, "+
				"poc.convenio as codigo_convenio," +
				"poc.contrato as codigo_contrato, " +
				"coalesce(dpoc.codigo_pk,0) as detallepaqueteconvenio, " +
				"coalesce(dpoc.esquema_tarifario,0) as esquematar " +
			"from " +
				"odontologia.presupuesto_odo_convenio poc " +
				"INNER JOIN odontologia.presu_odo_conv_det_serv_prog p ON(p.presupuesto_odo_convenio=poc.codigo_pk)" +
				"LEFT OUTER JOIN odontologia.presupuesto_paquetes pp ON(pp.codigo_pk=poc.presupuesto_paquete) " +
				"LEFT OUTER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk=pp.det_paq_odon_convenio) " +
			"WHERE " +
				"poc.presupuesto_odo_prog_serv = ? " +
				"and poc.contratado = '"+ConstantesBD.acronimoSi+"' " +
				"and p.servicio= "+servicio	;
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoPresuOdoProgSer);
			logger.info("\n\nConsulta Presupuesto Contratado >>> "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				infoRespCobertura.getDtoSubCuenta().setContrato(rs.getInt("codigo_contrato"));
				infoRespCobertura.getDtoSubCuenta().getConvenio().setCodigo(rs.getInt("codigo_convenio"));
				
				infoRespCobertura.getDtoSubCuenta().setSubCuenta(obtenerSubCuentaPlan(con, codigoPkPlanTratamiento, infoRespCobertura.getDtoSubCuenta().getContrato()));
				
				infoTarifa.setEstadoFacturacion(ConstantesBD.codigoEstadoFCargada);
				infoTarifa.setValorDescuentoBonoUnitario(new BigDecimal(rs.getDouble("valor_dcto_bono")));
				infoTarifa.setValorDescuentoPromocionUnitario(new BigDecimal(rs.getDouble("valor_dcto_prom")));
				infoTarifa.setValorDescuentoComercial(new BigDecimal(rs.getDouble("valor_dcto_comercial")));
				infoTarifa.setValorTarifaUnitaria(new BigDecimal(rs.getDouble("valor_unitario")));
				infoTarifa.setPorcentajeDecuentoBonoUnitario(rs.getDouble("porcentaje_dcto_bono_serv"));
				infoTarifa.setPorcentajeDescuentoPromocionUnitario(rs.getDouble("porcentaje_dcto_prom_serv"));
				infoTarifa.setPorcentajeHonorarioPromocion(rs.getDouble("porc_honorario_prom_serv"));
				
				if(infoTarifa.getValorDescuentoPromocionUnitario().doubleValue()<=0 && infoTarifa.getValorDescuentoBonoUnitario().doubleValue()<=0)
				{	
					infoTarifa.setPorcentajeDctoOdontologicoCALCULADO(SqlBasePresupuestoOdontologico.obtenerDctoOdontologicoPresupuestoXPlan(codigoPkPlanTratamiento));
				}	
				else
				{
					infoTarifa.setPorcentajeDctoOdontologicoCALCULADO(0);
				}
				
				infoTarifa.setDetallePaqueteOdonConvenio(rs.getInt("detallepaqueteconvenio"));
				infoTarifa.setEsquemaTarifarioPaquete(rs.getInt("esquematar"));
			}
			else
			{
				infoTarifa.setError("No se pudo encontrar tarifa en el presupuesto contratado");
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerInfoPresupuestoContratadoProgSer",e);
		}
	}
	
	/**
	 * 
	 * Metodo para obtener la subcuenta del plan de tratamiento
	 * @param codigoPkPlanTratamiento
	 * @param contrato
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static String obtenerSubCuentaPlan(Connection con, BigDecimal codigoPkPlanTratamiento, int contrato) 
	{
		String resultado="";
		String consulta="SELECT sc.sub_cuenta as subcuenta FROM sub_cuentas sc WHERE sc.ingreso = (select ingreso from odontologia.plan_tratamiento where codigo_pk=?) and contrato=?";
		
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
			ps.setBigDecimal(1,codigoPkPlanTratamiento);
			ps.setInt(2, contrato);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado= rs.getString("subcuenta");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSubCuentaPlan ",e);
		}
		return resultado;
	}

	/**
	 * Método para saber si la cita procesda es la priemra cita del plan de tratamietno
	 * @param con
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean esPrimeraCitaPlanTratamiento(Connection con,int codigoPlanTratamiento)
	{
		boolean esPrimeraCita = false;
		try
		{
			String consulta = "SELECT count(1) as cuenta FROM "+
				"( "+
					"SELECT "+ 
					"co.codigo_pk "+ 
					"FROM odontologia.plan_tratamiento pt "+ 
					"INNER JOIN odontologia.programas_hallazgo_pieza php on(php.plan_tratamiento = pt.codigo_pk) "+
					"INNER JOIN odontologia.servicios_cita_odontologica sco ON(sco.programa_hallazgo_pieza = php.codigo_pk) "+ 
					"INNER JOIN odontologia.citas_odontologicas co ON(co.codigo_pk = sco.cita_odontologica) "+ 
					"INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) "+
					"INNER JOIN odontologia.det_plan_tratamiento dpt on(sxp.det_plan_trata = dpt.codigo_pk) "+ 
					"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.servicio=sco.servicio) "+ 
					"WHERE " +
					"pt.codigo_pk = ? and " +
					//solo aplica para contratado
					"pt.estado = '"+ConstantesIntegridadDominio.acronimoContratado+"' and " +
					"co.estado in ('"+ConstantesIntegridadDominio.acronimoReservado+"','"+ConstantesIntegridadDominio.acronimoAsignado+"','"+ConstantesIntegridadDominio.acronimoReprogramado+"','"+ConstantesIntegridadDominio.acronimoAtendida+"') "+ 
					"GROUP BY co.codigo_pk "+
				") t";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoPlanTratamiento);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")==1)
				{
					esPrimeraCita = true;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en esPrimeraCitaPlanTratamiento ",e);
		}
		return esPrimeraCita;
	}
	
	/**
	 * Método para actualizar el número de pacientes atendidos por contrato
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static ResultadoBoolean actualizarNumeroPacientesAtendidosContrato(Connection con,int codigoContrato)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			String consulta = "UPDATE facturacion.control_anticipos_contrato set num_pac_atendidos = num_pac_atendidos +1 WHERE contrato = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoContrato);
			if(pst.executeUpdate()<=0)
			{
				resultado.setResultado(false);
				resultado.setDescripcion("Problemas en base de datos al tratar de actualizar el número de pacientes atendidos para el contrato con codigo: "+codigoContrato+". Favor reportar el error al proveedor del sistema");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarNumeroPacientesAtendidosContrato ",e);
			resultado.setResultado(false);
			resultado.setDescripcion("Problemas en base de datos al tratar de actualizar el número de pacientes atendidos para el contrato con codigo: "+codigoContrato+". "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoContrato
	 * @param codigoPrograma
	 * @param codigoInstitucion
	 * @param fechaCalculoVigencia
	 * @return
	 */
	public static InfoTarifa obtenerDescuentoComercialXConvenioPrograma(
			Connection con, int codigoViaIngreso, String tipoPaciente,
			int codigoContrato, Double codigoPrograma, int codigoInstitucion,
			String fechaCalculoVigencia) throws BDException
	{
		logger.info("**********************OBTENER DESCUENTOS COMERCIALES***************************");
		int codigoDescuentoComercialGeneral= SqlBaseCargosDao.obtenerCodigoDescuentoComercialGeneral(con, codigoViaIngreso,tipoPaciente, codigoContrato, codigoInstitucion,fechaCalculoVigencia);
		InfoTarifa descuentoInfo= new InfoTarifa();
		if(codigoDescuentoComercialGeneral<1)
		{
			//no se debe seguir buscando no existe descuento comercial 
			return descuentoInfo;
		}
		
		//si llega aca entonces se debe evaluar por servicio especifico
		descuentoInfo= obtenerDescuentoComercialProgramaEspecifico(con, codigoDescuentoComercialGeneral, codigoPrograma,fechaCalculoVigencia);
		if(descuentoInfo.getExiste())
		{
			//no se debe seguir buscando existe descuento comercial para el servicio específico mandamos el objeto cargado
			return descuentoInfo;
		}
		
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
	private static InfoTarifa obtenerDescuentoComercialProgramaEspecifico(Connection con, int codigoDescuentoComercialGeneral, Double codigoPrograma, String fechaCalculoVigencia) 
	{
		String fecha=UtilidadFecha.conversionFormatoFechaABD((UtilidadTexto.isEmpty(fechaCalculoVigencia)?UtilidadFecha.getFechaActual(con):fechaCalculoVigencia));
		
		//la prioridad en este punto va para las que tienen un insert null en la via ingreso 
		String consulta="SELECT coalesce(porcentaje||'','') as porcentaje " +
				"FROM facturacion.prog_desc_com_convxcont " +
				"WHERE codigo_descuento=? and " +
				"programa=? AND " +
				"to_char(fecha_vigencia, 'YYYY-MM-DD')<='"+fecha+"' " +
				"order by fecha_vigencia desc ";
		
		
		InfoTarifa descuentoInfo= new InfoTarifa();
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try 
		{
			logger.info("obtenerDescuentoComercialServicioEspecifico->"+consulta+" codGeneral->"+codigoDescuentoComercialGeneral+" codProg->"+codigoPrograma);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoDescuentoComercialGeneral+""));
			ps.setDouble(2, codigoPrograma);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				descuentoInfo.setExiste(true);
				Vector<String> porcentajes= new Vector<String>();
				porcentajes.add(rs.getString("porcentaje"));
				descuentoInfo.setPorcentajes(porcentajes);
				rs.close();
				ps.close();
				
				return descuentoInfo;
			}
			rs.close();
			ps.close();
			
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN obtenerDescuentoComercialServicioEspecifico", e);
		}
		return new InfoTarifa();
	}

	/**
	 * Retorna el código PK de la tabla presupuesto_odo_prog_serv
	 * @param con Conexión con la BD
	 * @param presupuesto Presupuesto asociado
	 * @param programa Programa buscado
	 * @return entero con el codigo_pk buscado, -1 de lo contrario
	 */
	public static int obtenerPresupuestoOdoProgSer(Connection con, int presupuesto, DtoProgramaServicio programa)
	{
		
		String sentencia;
		if(programa.isPrograma())
		{
			sentencia=
				"SELECT " +
					"codigo_pk AS codigo_pk " +
				"FROM " +
					"odontologia.presupuesto_odo_prog_serv " +
				"WHERE " +
						"presupuesto=? " +
					"AND " +
						"programa=?";
		}
		else
		{
			sentencia=
				"SELECT " +
					"codigo_pk AS codigo_pk " +
				"FROM " +
					"odontologia.presupuesto_odo_prog_serv " +
				"WHERE " +
						"presupuesto=? " +
					"AND " +
						"servicio=?";
		}
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);

		try
		{
			psd.setInt(1, presupuesto);
			psd.setInt(2, programa.getCodigo());
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				int codigoPk=rsd.getInt("codigo_pk");
				rsd.close();
				return codigoPk;
			}
		} catch (SQLException e)
		{
			Log4JManager.error("Error buscando el codigo_pk de la tabla presupuesto_odo_prog_serv");
		}
		finally{
			psd.cerrarPreparedStatement();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
}
