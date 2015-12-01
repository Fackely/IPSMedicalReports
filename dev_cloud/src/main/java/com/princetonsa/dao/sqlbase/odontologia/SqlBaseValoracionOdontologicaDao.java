package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.atencion.Diagnostico;

public class SqlBaseValoracionOdontologicaDao
{
	private static Logger logger = Logger.getLogger(SqlBaseValoracionOdontologicaDao.class);
	
	private static String strIngresarValoracionesOdonto 	=	"INSERT INTO " +
																	"odontologia.valoraciones_odonto " +
																"( " +
																	"codigo_pk," +			//1
																	"cita," +				//2
																	"fecha_consulta," +		//3
																	"hora_consulta," +		//4
																	"edad_paciente," +		//5
																	"motivo_consulta," +	//6
																	"enfermedad_actual," +	//7
																	"tipo_diagnostico," +	//8
																	"causa_externa," +		//9
																	"finalidad_consulta," +	//10
																	"observaciones," +		//11
																	"fecha_modifica," +		//12
																	"hora_modifica," +		//13
																	"usuario_modifica " +	//14
																") " +
																"VALUES " +
																	"(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena usada para la modificacion de la valoracion de odontologia
	 */
	private static String strModificarValoracionesOdonto = "UPDATE " +
		"odontologia.valoraciones_odonto SET " +
		"fecha_consulta = ?, " +
		"hora_consulta = ?," +
		"edad_paciente = ?, " +
		"motivo_consulta = ?, " +
		"enfermedad_actual = ?, " +
		"tipo_diagnostico = ?," +
		"causa_externa=?," +
		"finalidad_consulta=?," +
		"observaciones =?," +
		"usuario_modifica = ?, " +
		"fecha_modifica = current_date, " +
		"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where codigo_pk = ?";
	
	
	
	private static String strIngresarDiagnosticos	=	"INSERT INTO " +
															"odontologia.val_diagnosticos_odo " +
														"(" +
															"valoracion_odo," +		//1
															"acronimo_diagnostico," +	//2
															"tipo_cie_diagnostico," +	//3
															"principal," +				//4
															"fecha_modifica," +			//5
															"hora_modifica," +			//6
															"usuario_modifica" +		//7
														")" +
														"VALUES " +
															"(?,?,?,?,?,?,?)";
	
	private static final String strEliminarDiagnosticos = "DELETE FROM odontologia.val_diagnosticos_odo WHERE valoracion_odo = ? ";
	
	private static String strConsultaDiagnosticosValOdo	=	"SELECT " +
																"valoracion_odo," +		//1
																"acronimo_diagnostico," +	//2
																"tipo_cie_diagnostico," +	//3
																"principal," +				//4
																"fecha_modifica," +			//5
																"hora_modifica," +			//6
																"usuario_modifica, " + //7
																"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) AS nomdiagnostico " +//8
																" " +
															"FROM " +
																"odontologia.val_diagnosticos_odo " +
															"WHERE " +
																"valoracion_odo=?";
	
	private static String consultarPacienteTieneValoracion	=	"SELECT " +
																	"vo.codigo_pk AS codigovaloracion," +
																	"vo.cita," +
																	"vo.fecha_consulta AS fecha," +
																	"vo.hora_consulta AS hora," +
																	"coalesce(vo.motivo_consulta,'') AS motivo," +
																	"coalesce(vo.enfermedad_actual,'') AS enfermedad," +
																	"vo.tipo_diagnostico AS diagnostico," +
																	"vo.causa_externa AS causa," +
																	"vo.finalidad_consulta AS finalidad," + // NO PNER COALESCE, ESTOY PONIENDO VALIDACIÓN CON EL NULO (JUAN DAVID RAMÍREZ)
																	"coalesce(vo.observaciones,'') AS observaciones," +
																	"pi.codigo_pk AS codigoplantillaingreso," +
																	"pi.codigo_paciente AS paciente," +
																	"pi.plantilla AS plantilla," +
																	"pi.ingreso AS ingreso," +
																	"pi.valoracion_odonto AS valoracion " +
																"FROM " +
																	"odontologia.valoraciones_odonto vo " +
																"INNER JOIN " +
																	"historiaclinica.plantillas_ingresos pi ON (pi.valoracion_odonto=vo.codigo_pk) " +
																"WHERE " +
																	"pi.codigo_paciente=? " +
																"AND " +
																	"pi.plantilla=? "+	
																"AND " +
																	"vo.cita=? ";
	
	private static String borrarDx=	"DELETE FROM odontologia.val_diagnosticos_odo WHERE valoracion_odo=? ";
	
	private static String actualizarValoracion=		"UPDATE " +
														"odontologia.valoraciones_odonto " +
													"SET " +
														"(fecha_consulta=?," +//1
														"hora_consulta=?," +//2
														"motivo_consulta=?," +//3
														"enfermedad_actual=?," +//4
														"tipo_diagnostico=?," +//5
														"causa_externa=?," +//6
														"finalidad_consulta=?," +//7
														"observaciones=?," +//8
														"fecha_modifica=?," +//9
														"hora_modifica=?," +//10
														"usuario_modifica=?) " +//11
													"WHERE " +
														"codigo_pk=?";//12
	
	
	/**
	 * Cadena para consultar la informacion encabezado de la valoracion
	 */
	private static final String consultarValoracionStr = "SELECT "+ 
		"vo.codigo_pk as codigo_pk, "+
		"vo.cita as cita, "+
		"coalesce(to_char(vo.fecha_consulta,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_consulta, "+
		"coalesce(vo.hora_consulta,'') as hora_consulta, "+
		"vo.edad_paciente as edad_paciente, "+
		"coalesce(vo.motivo_consulta,'') as motivo_consulta, "+
		"coalesce(vo.enfermedad_actual,'') as enfermedad_actual, "+
		"coalesce(vo.tipo_diagnostico,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_diagnostico, "+
		"coalesce(facturacion.getnombretipodiag(vo.tipo_diagnostico),'') as nombre_tipo_diagnostico, "+
		"coalesce(vo.causa_externa,"+ConstantesBD.codigoNuncaValido+") as codigo_causa_externa, "+
		"coalesce(facturacion.getnombrecausaexterna(vo.causa_externa),'') as nombre_causa_externa, "+
		"coalesce(vo.finalidad_consulta,'') as codigo_finalidad_consulta, "+
		"coalesce(facturacion.getnomfinalidadconsulta(vo.finalidad_consulta),'') as nombre_finalidad_consulta, "+
		"coalesce(vo.observaciones,'') as observaciones "+ 
		"FROM odontologia.valoraciones_odonto vo "+ 
		"WHERE vo.codigo_pk = ?";
				
	public static boolean actualizarValoracion (DtoValoracionesOdonto dto)
	{
		boolean transaccionExitosa=false;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con,actualizarValoracion);
			
			if(psd.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null, con);
				transaccionExitosa=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null, con);
				transaccionExitosa=false;
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizarValoracion==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	public static boolean borrarDx (Connection con,double valoracion)
	{
		boolean transaccionExitosa=false;
		try 
		{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, borrarDx);
			psd.setDouble(1,valoracion);
			
			
			logger.info("LA CONSULTA DE ELIMINAR DX------>"+psd);
			
			if(psd.executeUpdate()>=0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null,null);
				transaccionExitosa=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,null,null);
				transaccionExitosa=false;
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN borrarDx==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	public static DtoValoracionesOdonto consultarValoracionPaciente (int codigoPaciente,double cita, int plantilla)
	{
		DtoValoracionesOdonto dto=new DtoValoracionesOdonto();
		String consulta=consultarPacienteTieneValoracion;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
			psd.setInt(1,codigoPaciente);
			psd.setInt(2,plantilla);
			psd.setDouble(3, cita);
			logger.info("\n\n*****************CONSULTA SI EL PACIENTE POSEE VALORACION------>\n\n*****************");
			
			logger.info("\n\n\nLA CONSUTLA------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			while(rs.next())
			{
				dto.setCodigoPk(rs.getDouble("codigovaloracion"));
				dto.setCita(rs.getDouble("cita"));
				dto.setFechaConsulta(rs.getString("fecha"));
				dto.setHoraConsulta(rs.getString("hora"));
				dto.setMotivoConsulta(rs.getString("motivo"));
				dto.setEnfermedadActual(rs.getString("enfermedad"));
				dto.setTipoDiagnostico(rs.getInt("diagnostico"));
				dto.setCausaExterna(rs.getInt("causa"));
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setFinalidadConsulta(rs.getString("finalidad"));
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,rs,con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarValoracionPaciente==> "+e);
		}
		
		return dto;
	}
	
	public static DtoPlantillasIngresos consultarPacienteTieneValoracion (int codigoPaciente, double cita, int plantilla)
	{
		DtoPlantillasIngresos dto=new DtoPlantillasIngresos();
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultarPacienteTieneValoracion);
			psd.setInt(1, codigoPaciente);
			psd.setInt(2,plantilla);
			psd.setDouble(3, cita);
			
			logger.info("\n\n\nLA CONSUTLA Paciente Valoracion------->"+psd);
			
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			while(rs.next())
			{
				dto.setCodigoPK(rs.getDouble("codigoplantillaingreso"));
				dto.setCodigoPaciente(rs.getInt("paciente"));
				dto.setPlantilla(rs.getInt("plantilla"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setValoracionOdonto(rs.getDouble("valoracion"));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd,rs,con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarPacienteTieneValoracion==> "+e);
		}
		return dto;
	}
	
	public static ArrayList<DtoValDiagnosticosOdo> consultaDiagnosticosValOdo(double valoracion)
	{
		ArrayList<DtoValDiagnosticosOdo> listadoDiagnosticos = new ArrayList<DtoValDiagnosticosOdo>();
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, strConsultaDiagnosticosValOdo);
			psd.setDouble(1, valoracion);
			logger.info("LA CONSUTLA DE DX ES-->"+psd);
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			
			while(rs.next())
			 {
				DtoValDiagnosticosOdo dto=new DtoValDiagnosticosOdo();
				dto.setValoracionOdo(rs.getDouble("valoracion_odo"));
				dto.setAcronimoDiagnostico(rs.getString("acronimo_diagnostico"));
				dto.setTipoCleDiagnostico(rs.getInt("tipo_cie_diagnostico"));
				dto.setPrincipal(rs.getString("principal"));
				dto.setUsuario(rs.getString("fecha_modifica"));
				dto.setFecha(rs.getString("hora_modifica"));
				dto.setHora(rs.getString("usuario_modifica"));
				dto.setNombreDiagnostico(rs.getString("nomdiagnostico"));
				listadoDiagnosticos.add(dto);
			 }
			UtilidadBD.cerrarObjetosPersistencia(psd, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaDiagnosticosValOdo==> "+e);
		}
		
		return listadoDiagnosticos;
	}
	
	/**
	 * Método implementado para ingresar los diagnósticos de la valoracion
	 * @param con
	 * @param diagnostico
	 * @param diagnosticosRelacionados
	 * @param valoracion
	 * @return
	 */
	public static boolean ingresarDiagnosticos (Connection con,ArrayList<Diagnostico> diagnostico, HashMap diagnosticosRelacionados, double valoracion)
	{
		boolean transaccionExitosa=false;
		
		//Primero inserto el diagnóstico principal
		try 
		{
			//Primero se trata de eliminar los diagnosticos
			if(valoracion>0)
			{
				PreparedStatementDecorator psd = new PreparedStatementDecorator(con,strEliminarDiagnosticos);
				psd.setDouble(1, valoracion);
				psd.executeUpdate();
				logger.info("eliminar diagnostico: "+psd);
				UtilidadBD.cerrarObjetosPersistencia(psd, null, null);
			}
			
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, strIngresarDiagnosticos);

			String loginUsuario=diagnosticosRelacionados.get("loginUsuario")+"";
			
			psd.setDouble(1, valoracion);
			psd.setString(2, diagnostico.get(0).getAcronimo());
			psd.setInt(3, diagnostico.get(0).getTipoCIE());
			psd.setString(4, ConstantesBD.acronimoSi);
			psd.setString(5,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			psd.setString(6,UtilidadFecha.getHoraActual());
			psd.setString(7,loginUsuario);
			
			logger.info("LOS DATOS DE CONSULTA--->"+psd);
			
			if(psd.executeUpdate()>0)
			{
				UtilidadBD.cerrarObjetosPersistencia(psd,null, null);
				transaccionExitosa=true;
			}
			else
			{
				UtilidadBD.cerrarObjetosPersistencia(psd,null, null);
				transaccionExitosa=false;
			}
			
			//Se insertan los diagnosticos relacionados
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ingresarDiagnosticos==> ",e);
		}
		
		
		//Luego se realiza la isnercion de los diagnosticos relacionados
		if (transaccionExitosa)
		{
			int numRegistros=Utilidades.convertirAEntero(diagnosticosRelacionados.get("numRegistros")+"");
			//Si hay registros en el mapa de diagnosticos relacionados
			if (numRegistros!=0)
			{
				for (int i=0;i<numRegistros;i++)
				{
					if (UtilidadTexto.getBoolean(diagnosticosRelacionados.get("checkbox_"+i)))
					{
						try 
						{
							//Se ingresa elemento por elemento de los diagnosticos relacionados que se sacan del mapa			
							
							String loginUsuario=diagnosticosRelacionados.get("loginUsuario")+"";
							String arrayDiagnosticos[]= diagnosticosRelacionados.get(""+i+"").toString().split(ConstantesBD.separadorSplit);
							
							PreparedStatementDecorator psd=new PreparedStatementDecorator(con, strIngresarDiagnosticos);
							
							
							psd.setDouble(1, valoracion);
							psd.setString(2, arrayDiagnosticos[0]);
							psd.setInt(3, Utilidades.convertirAEntero(arrayDiagnosticos[1]));
							psd.setString(4, ConstantesBD.acronimoNo);
							psd.setString(5,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							psd.setString(6,UtilidadFecha.getHoraActual());
							psd.setString(7,loginUsuario);
							logger.info("insercion del diagnostico relacinoado_"+i+": "+psd);
							if(psd.executeUpdate()>0)
							{
								UtilidadBD.cerrarObjetosPersistencia(psd,null, null);
								transaccionExitosa=true;
							}
							else
							{
								UtilidadBD.cerrarObjetosPersistencia(psd,null, null);
								transaccionExitosa=false;
							}
						}
						catch (SQLException e) 
						{
							logger.error("ERROR EN ingresarValoracionesOdonto==> "+e);
						}
					}
				}
			}
		}
		
		
		
		Utilidades.imprimirMapa(diagnosticosRelacionados);
		
		return transaccionExitosa;
	}
	
	public static double ingresarValoracionesOdonto (Connection con,DtoValoracionesOdonto dto)
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
			logger.info("valor del codigo pk: "+dto.getCodigoPk());
			if(dto.getCodigoPk()<=0)
			{
			
				
				//*******************INSERCIÓN DE LA VALORACION *******************************************************
				secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_valoraciones_odonto");
				
	            PreparedStatementDecorator psd=new PreparedStatementDecorator(con, strIngresarValoracionesOdonto);
	            psd.setDouble(1, secuencia);
	            psd.setDouble(2, dto.getCita());
	            if(!dto.getFechaConsulta().trim().equals(""))
	            {
	            	psd.setString(3, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaConsulta()));
	            }
	            else
	            {
	            	psd.setNull(3,Types.DATE);
	            }
	            
	            psd.setString(4, dto.getHoraConsulta());
	            psd.setString(5, dto.getEdadPaciente());
	            psd.setString(6, dto.getMotivoConsulta());
	            psd.setString(7, dto.getEnfermedadActual());
	            if(dto.getTipoDiagnostico()>0)
	            {
	            	psd.setInt(8, dto.getTipoDiagnostico());
	            }
	            else
	            {
	            	psd.setNull(8,Types.INTEGER);
	            }
	            
	            if(dto.getCausaExterna()>0)
	            {
	            	psd.setInt(9, dto.getCausaExterna());
	            }
	            else
	            {
	            	psd.setNull(9,Types.INTEGER);
	            }
	            
	            if(!dto.getFinalidadConsulta().trim().equals(""))
	            {
	            	psd.setString(10, dto.getFinalidadConsulta());
	            }
	            else
	            {
	            	psd.setNull(10,Types.CHAR);
	            }
	            
	            psd.setString(11, dto.getObservaciones());
	            psd.setString(12, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
	            psd.setString(13, dto.getHoraModifica());
	            psd.setString(14, dto.getUsuarioModifica());
				
				if(psd.executeUpdate()<=0)
				{
					logger.info("no fue exitosa la inserción!!");
					secuencia = 0;
				}
				logger.info("\n\n\n\nse insertará valoracion "+psd+"\n\n\n\n\n");
				psd.close();
				//*************************************************************************************
			}
			else
			{
				//******************MODIFICACIÓN DE LA VALORACION *****************************************************
				secuencia = dto.getCodigoPk();
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con,strModificarValoracionesOdonto);
				if(!dto.getFechaConsulta().trim().equals(""))
				{
					pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaConsulta())));
				}
				else
				{
					pst.setNull(1,Types.DATE);
				}
				
				if(!dto.getHoraConsulta().trim().equals(""))
				{
					pst.setString(2,dto.getHoraConsulta());
				}
				else
				{
					pst.setNull(2,Types.VARCHAR);
				}
				
				pst.setString(3,dto.getEdadPaciente());
				pst.setString(4,dto.getMotivoConsulta());
				pst.setString(5,dto.getEnfermedadActual());
				if(dto.getTipoDiagnostico()>0)
				{
					pst.setInt(6,dto.getTipoDiagnostico());
				}
				else
				{
					pst.setNull(6,Types.INTEGER);
				}
				
				 if(dto.getCausaExterna()>0)
	            {
	            	pst.setInt(7, dto.getCausaExterna());
	            }
	            else
	            {
	            	pst.setNull(7,Types.INTEGER);
	            }
		            
	            if(!dto.getFinalidadConsulta().trim().equals(""))
	            {
	            	pst.setString(8, dto.getFinalidadConsulta());
	            }
	            else
	            {
	            	pst.setNull(8,Types.CHAR);
	            }
		            
		        pst.setString(9, dto.getObservaciones());
		        pst.setString(10, dto.getUsuarioModifica());
		        pst.setDouble(11, dto.getCodigoPk());
		        
		        if(pst.executeUpdate()<=0)
		        {
		        	logger.info("no fue exitosa la actualización!!");
		        	secuencia = 0;
		        }
		        logger.info("se actualizará valoracion");
				pst.close();
				
				//******************************************************************************************************
				
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN ingresarValoracionesOdonto==> "+e);
		}
		
		return secuencia;
	}
	
	/**
	 * Método implementado para consultar la información de la valoracion odontologica
	 * @param con
	 * @param valoracionOdo
	 */
	public static void consultar(Connection con,DtoValoracionesOdonto valoracionOdo)
	{
		try
		{
			//********************SE CONSULTA PRIMERO EL ENCABEZADO DE LA VALORACION***********************
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consultarValoracionStr);
			pst.setBigDecimal(1,new BigDecimal(valoracionOdo.getCodigoPk()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				valoracionOdo.setCita(rs.getDouble("cita"));
				valoracionOdo.setFechaConsulta(rs.getString("fecha_consulta"));
				valoracionOdo.setHoraConsulta(rs.getString("hora_consulta"));
				valoracionOdo.setEdadPaciente(rs.getString("edad_paciente"));
				valoracionOdo.setMotivoConsulta(rs.getString("motivo_consulta"));
				valoracionOdo.setEnfermedadActual(rs.getString("enfermedad_actual"));
				valoracionOdo.setTipoDiagnostico(rs.getInt("codigo_tipo_diagnostico"));
				valoracionOdo.setNombreTipoDiagnostico(rs.getString("nombre_tipo_diagnostico"));
				valoracionOdo.setCausaExterna(rs.getInt("codigo_causa_externa"));
				valoracionOdo.setNombreCausaExterna(rs.getString("nombre_causa_externa"));
				valoracionOdo.setFinalidadConsulta(rs.getString("codigo_finalidad_consulta"));
				valoracionOdo.setNombreFinalidadConsulta(rs.getString("nombre_finalidad_consulta"));
				valoracionOdo.setObservaciones(rs.getString("observaciones"));
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
			
			//************************************************************************************************
			
			//****************SE CONSULTA LOS DIAGNOSTICOS DE LA VALORACION*********************************************
			valoracionOdo.setDiagnosticos(consultaDiagnosticosValOdo(valoracionOdo.getCodigoPk()));
			//************************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en consultar: ",e);
		}
	}
	
	public static String obtenerNombreDiagnosticoPrincipal(int codigoDx)
	{
		
		String consulta="SELECT nombre FROM manejopaciente.tipos_diagnostico WHERE codigo=?";
		String nombreDx="";
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoDx);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				nombreDx=rs.getString("nombre");
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("Error en la consulta del diagnostico: ",e);
		}
		return nombreDx;
		
		
	}
	
	
	
}