package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.manejoPaciente.InfoIngresoCuenta;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoLogProcAutoCitas;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.odontologia.DtoLogProcAutoFact;
import com.princetonsa.dto.odontologia.DtoLogProcAutoServCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.manejoPaciente.AperturaIngresos;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseProcesosAutomaticosOdontologia 
{
	private static Logger logger = Logger.getLogger(SqlBaseProcesosAutomaticosOdontologia.class);
    
    /**
     *
     */
    private static String inserccionProcAutoEst = "INSERT INTO odontologia.log_proc_auto_estados(   " +
                                                                                                    "codigo_pk , " +//1
                                                                                                    "inconsistencia , " +//2
                                                                                                    "fecha , " +//3
                                                                                                    "hora , " +//4
                                                                                                    "presupuesto , " +//5
                                                                                                    "institucion , " +//6
                                                                                                    "plan_tratamiento  ) " +//7
                                                                                              "values (" +
                                                                                                    "? , " +//1
                                                                                                    "? , " +//2
                                                                                                    "? , " +//3
                                                                                                    "? , " +//4
                                                                                                    "? , " +//5
                                                                                                    "? , " +//6
                                                                                                    "? ) ";//7
	
	/**
	 * 
	 */
	private static String insertLogProcCitas = " insert into odontologia.log_proc_auto_citas("+
																						"	codigo_pk ,"+
																						"	fecha_ejecucion ,"+
																						"	hora_ejecucion ,"+
																						"	cita_odontologica ,"+
																						"	estado_inicial_cita ,"+
																						"	institucion)"+
																						"	values (?, ? , ? , ? , ?, ? )";
	
	/**
	 * 
	 */
	private static String insertLogAutoServCita = "	insert into odontologia.log_proc_auto_serv_cita ( "+
																							" codigo_pk ,"+
																							" log_proc_auto_cita ,"+
																							" servicio_cita_odo ,"+
																							" estado_inicial_hc," +
																							" estado_inicial_fact )"+ 		
																							" values "+
																							" (?, ?, ? , ?, ?)";
	
	/**
	 * 
	 */
	private static String insertlogProcActoFact="insert into odontologia.log_proc_auto_fact (" +
																								"codigo_pk ," +
																								"log_proc_auto_serv_cita, " +
																								"det_cargo , " +
																								"estado_inicial_fact )" +
																								"values " +
																								"(?, ?, ? , ? , ?)";
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarLogProcAutoFact(DtoLogProcAutoFact dto, Connection con)
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
		//Objetos de Conexion
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_proc_auto_fact"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertlogProcActoFact);
			
			ps.setDouble(1,secuencia);
			ps.setBigDecimal(2, dto.getLogProcAutoServCita());
			ps.setBigDecimal(3, dto.getDetCargo());
			ps.setInt(4, dto.getEstadoInicialFact());
			
			logger.info("LOGGER DE INSERTAR LOG "+ps);
			
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
	public static double guardarLogProcCitas(DtoLogProcAutoCitas dto, Connection con)
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
		//Objetos de Conexion
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_proc_auto_cit"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertLogProcCitas);
			
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getFechaEjecucion());
			ps.setString(3, dto.getHoraEjecucion());
			ps.setBigDecimal(4, dto.getCitaOdontologica());
			ps.setString(5, dto.getEstadoInicialCita());
			ps.setInt(6, dto.getInstitucion());
			
			
			logger.info("LOGGER DE INSERTAR LOG "+ps);
			
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
	public static double guardarLogAutoServCita(DtoLogProcAutoServCita dto, Connection con)
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
		//Objetos de Conexion
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_proc_auto_ser"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertLogAutoServCita);
			
			ps.setDouble(1,secuencia);
			ps.setBigDecimal(2, dto.getLogProcAutoCita());
			ps.setBigDecimal(3, dto.getServicioCitaOdo());
			ps.setInt(4, dto.getEstadoInicialHc());
			ps.setInt(5, dto.getEstadoInicialFact());
			
			logger.info("LOGGER DE INSERTAR LOG "+ps);
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
	 * @param codigosPk
	 * @param estado
	 * @param con
	 * @return
	 */
	public static boolean modificarCita(ArrayList<Integer> codigosPk, String estado, Connection con)
	{
		if(codigosPk.size()<=0)
		{
			return false;
		}
		logger.info("************************************************************************************************************************");
		logger.info("**************************************** CAMBIANDO ESTADO CITA *********************************************************");
		
		String consultaStr="";
		
		if (estado.trim().equals(ConstantesIntegridadDominio.acronimoNoAsistio)) {
			consultaStr="update odontologia.citas_odontologicas set estado='"+estado+"', " +
					"fecha_modifica=CURRENT_DATE, " +
					"hora_modifica='"+UtilidadFecha.getHoraActual()+"', " +
					"indicativo_cambio_estado='"+ConstantesIntegridadDominio.acronimoAutomatica+"'" +
					"where codigo_pk in ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codigosPk)+") ";
		}else{
			consultaStr="update odontologia.citas_odontologicas set estado='"+estado+"', fecha_modifica=CURRENT_DATE, hora_modifica='"+UtilidadFecha.getHoraActual()+"' where codigo_pk in ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codigosPk)+") ";
		}
		
		
		logger.info("\n\n\n\n\n");
		logger.info(consultaStr);
		logger.info("\n\n\n\n\n\n");
		boolean retornar=false;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			logger.info("LOGGER DE UPDATE LOG "+ps);
			retornar=ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);		
		}

		return retornar;
	}
	
	
	
	/**
	 * CONSULTA LA SOLICITUDES DE CITAS
	 * @author Edgar Carvajal Ruiz
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<InfoDatosDouble> cargarProcAutoServCita(DtoServicioCitaOdontologica dtoWhere)
	{
		logger.info("\n\n\n\n\n************************************************************************************************************************");
		logger.info("**************************************** CONSULTANDO LOD PRO SER CITA  *********************************************************");
		
		boolean bandera =Boolean.FALSE;
		
		ArrayList<InfoDatosDouble> arrayDatos = new ArrayList<InfoDatosDouble>(); 
		String consultaStr=" " +
				" select lpsc.codigo_pk as codigo_pk , " +
				" lpsc.numero_solicitud as numero_solicitud , " +
				" lhc.estado_historia_clinica as estado  " +
				" from " +
				" odontologia.servicios_cita_odontologica lpsc " +
				"	INNER JOIN " +
				" ordenes.solicitudes lhc on (lpsc.numero_solicitud = lhc.numero_solicitud)  where 1=1  ";
		
		if(dtoWhere.getCodigoPk() > 0)
		{
			consultaStr+= " and lpsc.codigo_pk = "+dtoWhere.getCodigoPk();
			bandera=Boolean.TRUE;
		}
		if(dtoWhere.getCitaOdontologica() > 0)
		{
			consultaStr+= " and lpsc.cita_odontologica  = "+dtoWhere.getCitaOdontologica();
			bandera=Boolean.TRUE;
		}
		
		/*
		 * METODO PARA DE SEGURIDAD
		 */
		if(!bandera)
		{
			return  new ArrayList<InfoDatosDouble>();
		}
		
		
		logger.info("*****************************************");
		logger.info(consultaStr);
		logger.info("/////////////////////////////////////////");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				InfoDatosDouble datos = new InfoDatosDouble();
				datos.setCodigo(rs.getDouble("codigo_pk"));
				datos.setNombre(String.valueOf(rs.getDouble("numero_solicitud")));
				datos.setDescripcion(String.valueOf(rs.getInt("estado")));
				arrayDatos.add(datos);
				
			 }
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}	
		catch (SQLException e) 
		{
			    
				logger.error("error en carga==> "+e);
		}
		
		return arrayDatos;
	}
	

	/**
	 *
	 * @param dto
	 * @return
	 */
	public static double guardarProcAutoEstados(DtoLogProcAutoEstados dto , Connection con)
	{
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;

		try
		{
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_log_pro_aut_est");
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionProcAutoEst,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);                                
			ps.setString(2, dto.getInconsistencia());                    
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()));
			ps.setString(4, dto.getHora());
			ps.setInt(5, dto.getPresupuesto());
			ps.setInt(6, dto.getInstitucion());
			ps.setInt(7, dto.getPlanTratamiento());

			if (ps.executeUpdate() > 0)
			{
				ps.close();
				return dto.getCodigoPk();
			}
			ps.close();

		}
		catch (SQLException e)
		{
			logger.error("ERROR en insert proc aut est " + e + dto.getCodigoPk());
		}
		return dto.getCodigoPk();
	}

	/**
	 * 
	 * Metodo para cerrar los ingresos
	 * @param con
	 * @param institucion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public static boolean cerrarIngresosOdontologicos(Connection con, int institucion)
	{
		ArrayList<InfoIngresoCuenta> listaIngresos= obtenerIngresosAbiertosConPlanTerminado(con, institucion);
		for(InfoIngresoCuenta info: listaIngresos)
		{
			boolean puedoCerrarIngreso=true;
			
			//SI EXISTEN CARGOS PENDIENTES ENTONCES NO HACEMOS NADA
			if(obtenerContadorEstadoCargo(con, info.getIngreso(), ConstantesBD.codigoEstadoFPendiente, "", false)>0)
			{
				puedoCerrarIngreso= false;
			}
			//SI EXISTE CARGADOS SIN FACTURA (CON VALOR > 0 ) TAMPOCO HACEMOS NADA
			if(puedoCerrarIngreso && obtenerContadorEstadoCargo(con, info.getIngreso(), ConstantesBD.codigoEstadoFCargada, ConstantesBD.acronimoNo, true)>0)
			{
				puedoCerrarIngreso= false;
			}
			Log4JManager.info("PUEDO CERRAR INGRESO-->"+puedoCerrarIngreso);
			if(puedoCerrarIngreso)
			{	
				Log4JManager.info("LLEGA!!!!! 1");
				IngresoGeneral.modificarFechaHoraEgresoIngreso(con, info.getIngreso()+"");
				//Identificar el ingreso con cierre generado automaticamente
				HashMap datosIngreso = new HashMap();
				datosIngreso.put("estado", ConstantesIntegridadDominio.acronimoEstadoCerrado);
				datosIngreso.put("ingreso", info.getIngreso());
				datosIngreso.put("cierreManual", ConstantesBD.acronimoNo);
				datosIngreso.put("usuarioModifica", "axioma");
				
				Log4JManager.info("LLEGA!!!!! 2");
				if(!AperturaIngresos.ActualizarEstadoIngreso(con, datosIngreso))
				{
					Log4JManager.info("LLEGA!!!!! 3");
					return false;
				}
				Log4JManager.info("LLEGA!!!!! 4");
				//SI EXISTEN CARGOS CARGADOS CON FACTURA ENTONCES LA CUENTA QUEDA FACTURADA Y EL INGRESO CERRADO
				if(obtenerContadorEstadoCargo(con, info.getIngreso(), ConstantesBD.codigoEstadoFCargada, ConstantesBD.acronimoSi, false)>0)
				{
					Log4JManager.info("LLEGA!!!!! 5");
					Cuenta mundoCuenta= new Cuenta();
					try 
					{
						Log4JManager.info("LLEGA!!!!! 6");
						if(mundoCuenta.cambiarEstadoCuentaTransaccional(con, ConstantesBD.codigoEstadoCuentaFacturada, info.getCuenta().intValue(), ConstantesBD.continuarTransaccion)<=0)
						{
							Log4JManager.info("LLEGA!!!!! 7");
							return false;
						}
					} 
					catch (SQLException e) 
					{
						Log4JManager.info("LLEGA!!!!! 8");
						e.printStackTrace();
					}
				}
				//DE LO CONTRARIO SI TODOS LOS CARGOS SON EXCENTOS ENTONCES LA CUENTA Y EL INGRESO QUEDAN CERRADOS
				else if(obtenerContadorEstadoCargo(con, info.getIngreso(), ConstantesBD.codigoEstadoFExento, "", false)>0)
				{
					Log4JManager.info("LLEGA!!!!! 9");
					Cuenta mundoCuenta= new Cuenta();
					try 
					{
						if(mundoCuenta.cambiarEstadoCuentaTransaccional(con, ConstantesBD.codigoEstadoCuentaCerrada, info.getCuenta().intValue(), ConstantesBD.continuarTransaccion)<=0)
						{
							Log4JManager.info("LLEGA!!!!! 10");
							return false;
						}
					} 
					catch (SQLException e) 
					{
						e.printStackTrace();
					}
					Log4JManager.info("LLEGA!!!!! 11");
				}
			}	
			Log4JManager.info("LLEGA!!!!! 12");
		}
		Log4JManager.info("LLEGA!!!!! 13");
		return true;
	}

	/**
	 * 
	 * Metodo para obtener los ingresos odontologicos abiertos con plan tratamiento terminado
	 * @param con
	 * @param institucion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<InfoIngresoCuenta> obtenerIngresosAbiertosConPlanTerminado(Connection con, int institucion) 
	{
		ArrayList<InfoIngresoCuenta> lista = new ArrayList<InfoIngresoCuenta>();
		String consulta="SELECT " +
							"DISTINCT " +
							"pt.ingreso as ingreso, " +
							"c.id as cuenta " +
						"FROM " +
							"odontologia.plan_tratamiento pt " +
							"INNER JOIN manejopaciente.ingresos i on(i.id=pt.ingreso) " +
							"INNER JOIN manejopaciente.cuentas c on(c.id_ingreso=i.id) " +
						"where " +
							"pt.estado='"+ConstantesIntegridadDominio.acronimoTerminado+"' " +
							"and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
		
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			Log4JManager.info("LISTA DE INGRESOS --->"+ps);
			while(rs.next())
			{
				InfoIngresoCuenta info= new InfoIngresoCuenta(rs.getBigDecimal("ingreso"), rs.getBigDecimal("cuenta"));
				lista.add(info);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR en actualizacion ingresos " + e );
		}
		return lista;
	}
	
	/**
	 * 
	 * Metodo para obtener la cantidad de cargos en un estado dado, facturado o no
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @param facturado
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static int obtenerContadorEstadoCargo(Connection con, BigDecimal ingreso, int estado, String facturado, boolean cargosMayoresCero) 
	{
		int retorna= 0;
		String consulta="select " +
							"count(1) as contador " +
						"FROM " +
							"manejopaciente.ingresos i " +
							"INNER JOIN manejopaciente.sub_cuentas sc ON(sc.ingreso=i.id) " +
							"INNER JOIN facturacion.det_cargos dc ON(dc.sub_cuenta=sc.sub_cuenta) " +
						"WHERE " +
							"i.id=? " +
							"and dc.estado=? ";
		
		consulta+=(cargosMayoresCero)?" and (dc.valor_total_cargado is not null and dc.valor_total_cargado>0) ":" ";
		
		consulta+=(!facturado.isEmpty())?" and dc.codigo_factura is "+(UtilidadTexto.getBoolean(facturado)?" not ":" ")+" null ": " ";
		
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1,ingreso);
			ps.setInt(2, estado);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("contar----->"+ps);
			if(rs.next())
			{
				retorna= rs.getInt("contador");
				Log4JManager.info("RETORNA CONTADOR-->"+retorna);
			}
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR en actualizacion ingresos " + e );
		}
		return retorna;
	}
}
