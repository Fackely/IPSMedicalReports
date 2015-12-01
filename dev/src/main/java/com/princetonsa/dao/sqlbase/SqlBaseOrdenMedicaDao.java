/*
 * Creado en Jun 7, 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.manejoPaciente.DtoHistoricoResultadoLaboratorios;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.dto.manejoPaciente.DtoResultadoLaboratorio;
import com.princetonsa.dto.ordenesmedicas.DtoHemodialisis;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripDialFechaHora;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Andr�s Mauricio Ruiz V�lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class SqlBaseOrdenMedicaDao
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseOrdenMedicaDao.class);
	
	/**
	 *  Sentencia SQL para insertar una orden m�dica
	 */
	/*private final static String insertarOrdenMedicaStr = 	"INSERT INTO ordenes_medicas (codigo, " +
																					  	 "cuenta, " +
																						 "observaciones_generales, " +
																						 "fecha_grabacion, " +
																						 "hora_grabacion, " +
																						 "fecha_orden, " +
																						 "hora_orden, " +
																						 "login, " +
																						 "datos_medico, " +
																						 "finalizar_soporte, " +
																						 "finalizar_dieta, "+
																						 "descripcion_soporte, " +
																						 "descripcion_dieta_oral )"+
																						 "VALUES " +
																						 "( ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?, ?, ?, ?, ?, ?) ";*/

	
	
	
	
	
	
	/**
	 * Cadena encargada de suspender una mezcla
	 */
	private static String strSuspenderMezcla="UPDATE orden_dieta SET finaliza_sol=?,fecha_supencion=?,hora_supencion=?,usuario_suspencion=?  WHERE  codigo_histo_enca=?";
	
	/**
	 * Cadena encargada de Finalizar una mezcla
	 */
	private static String strFinalizarMezcla="UPDATE orden_dieta SET suspendido=?, fecha_finaliza=?, hora_finaliza=?, usuario_finaliza=?  WHERE  codigo_histo_enca=?";
	
	/**
	 * Cadena actualiza la descripcion del soporte respiratorio 
	 */
	private static String strActualizarDescripcionSoporteRespiratorio = "UPDATE orden_soporte_respira SET descripcion = ? WHERE codigo_histo_enca = ?";
	
	/**
	 * Cadena consulta el historico de la descripcion del soporte respiratorio 
	 */
	private static String strConsultarDescripcionSoporteRespiratorio = "SELECT osr.descripcion AS descripcion FROM orden_soporte_respira osr " +
																			"INNER JOIN encabezado_histo_orden_m ehom ON(ehom.codigo = osr.codigo_histo_enca) " +
																			"INNER JOIN ordenes_medicas om ON(om.codigo = ehom.orden_medica) " +
																			"WHERE om.cuenta = ? " +
																			"ORDER BY  osr.codigo_histo_enca DESC ";
	
	/**
	 * Metodo encargado de Finalizar una Mezcla, esto se hace desde el registro de enfermeria.
	 * Se actualiza el campo que se llama suspendido en true, y se llenan los campos de fecha, hora y usuario
	 * finaliza
	 * @param connection
	 * @param finalizar
	 * @param codigoHistoEnca
	 * @param usuario
	 * @return
	 */
	public static boolean finalizarMezcla (Connection connection,String finalizar,int codigoHistoEnca,String usuario )
	{
		logger.info("\n entre a  finalizarMezcla finaliza -->"+finalizar+"   codigoHistoEnca -->"+codigoHistoEnca);
		
		try 
		{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(connection.prepareStatement(strFinalizarMezcla, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//suspendido
				ps.setBoolean(1, UtilidadTexto.getBoolean(finalizar));
				//fecha_finaliza
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				//hora_supencion
				ps.setString(3, UtilidadFecha.getHoraActual());
				//hora_finaliza
				ps.setString(4, usuario);
				//usuario_finaliza
				ps.setInt(5, codigoHistoEnca);
				
				if(ps.executeUpdate()>0)
					return  true;
				else
					return false;
				
		} 
		catch (Exception e)
		{
			logger.info("\n problema en el metodo suspenderMezcla "+e);
		}
		
		
		return false;
	}
	
	
	
	
	
	/**
	 * Metodo encargado de suspender una mezcla, esta mezcla puede ser suspendida desde ordenes de medicamentos,
	 * para suspender una mezcla se debe poner el el campo finaliza_sol en true.
	 * @param connection
	 * @param supender
	 * @param codigoHistoEnca
	 * @return
	 */
	public static boolean suspenderMezcla (Connection connection, String supender,int codigoHistoEnca,String usuario)
	{
		logger.info("\n entre a  suspenderMezcla finaliza -->"+supender+"   codigoHistoEnca -->"+codigoHistoEnca);
		
		try 
		{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(connection.prepareStatement(strSuspenderMezcla, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//finaliza_sol
				ps.setBoolean(1, UtilidadTexto.getBoolean(supender));
				//fecha_supencion
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				//hora_supencion
				ps.setString(3, UtilidadFecha.getHoraActual());
				//usuario_suspencion
				ps.setString(4, usuario);
				//codigo_histo_enca
				ps.setInt(5, codigoHistoEnca);
				
				if(ps.executeUpdate()>0)
					return  true;
				else
					return false;
				
		} 
		catch (Exception e)
		{
			logger.info("\n problema en el metodo suspenderMezcla "+e);
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	/**
	 * M�todo para obtener el codigo de la orden que est� asociado a una secuencia
	 * @param con -> conexion
	 * @param secuencia -> string que tiene la secuencia
	 * @return codigoOrden
	 */
	public static int obtenerCodigoOrden (Connection con, String secuencia)
	{
		String consultaSecuencia="SELECT "+secuencia;
		
		logger.info(" Consulta Obtener Codigo Orden" +consultaSecuencia);
		try
		{
			
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			return 0;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del codigo de la orden medica: SqlBaseOrdenMedicaDao "+e.toString());
			return 0;
		}
	}

	/**
	 * M�todo para saber si existe o no una orden m�dica
	 * @param con -> conexion
	 * @param cuenta
	 * @return codigoOrden si existe sino retorna -1
	 */
	public static int existeOrdenMedica(Connection con, int cuenta)
	{
		String consultaSecuencia="SELECT codigo as codigo from ordenes_medicas where cuenta = ? ";
		
		try
		{
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerCodigoStatement.setInt(1, cuenta);
			
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Vericando Existencia Orden Medica : SqlBaseOrdenMedicaDao "+e.toString());
			return -1;
		}
	}


public static DtoResultado insertarOrdenMedica(	Connection con,
											String codigoOrden,
											int cuenta,
											String descripcionSoporteRespiratorio,
											String descripcionDieta,
											String observacionesGenerales,
											String descripcionDietaParenteral, String codigoDetalleObservacion , Boolean tieneDatos)
{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String modOrden = "";
		Integer pkDetalleObservacion = new Integer(0);
		DtoResultado resultado=null;
		try
		{			
			PreparedStatement ps3 = con.prepareStatement(codigoDetalleObservacion);
			ResultSet rs3 = ps3.executeQuery();
			if (rs3.next()) {
				pkDetalleObservacion=rs3.getInt(1);
				}
			
			rs3.close();
			ps3.close();
			codigo = existeOrdenMedica(con,cuenta); 
			if ( codigo != -1  )  
			{  
				 //-Si existe se modifica
				modOrden = " UPDATE ordenes_medicas " +
						   " SET descripcion_soporte = ?, " +
						   " descripcion_dieta_oral = ?, " +
						   " observaciones_generales = ?, " +
						   " descripcion_dieta_par = ? " +
						   " WHERE codigo = ? ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(modOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));					
				ps.setString(1,null);//descripcionSoporteRespiratorio
				ps.setString(2,descripcionDieta);					
				ps.setString(3,"");
				ps.setString(4,descripcionDietaParenteral);
				ps.setInt(5, codigo);
				resp = ps.executeUpdate();
//				observacionesGenerales=observacionesGenerales.replace("\n", "");
				//				observacionesGenerales=observacionesGenerales.replace("\r", "");
				String[] observacionesArray = observacionesGenerales.split(ConstantesBD.separadorSplit);

				if(tieneDatos){
					List<String> tmp = new ArrayList<String>();
					for (int i = 0; i < observacionesArray.length; i++) {
							if(!observacionesArray[i].equals("") && !observacionesArray[i].equals("\n") ){
								tmp.add(observacionesArray[i]);
							}
						}
	
					String fechaCreacion = "";
					String fechaorden="";
					String observacion="";
					String datosMedico="";
					
					if(tmp.get(0)!=null && !tmp.get(0).contains("Fecha Orden:")){
						if(tmp.get(0)!=null){
							fechaCreacion=tmp.get(1);
							}
						
						if(tmp.get(1)!=null){
							fechaorden=tmp.get(2);
							}
						
						if(tmp.get(2)!=null){
							observacion=tmp.get(3);
							}
						
						
						if(tmp.get(3)!=null){
							datosMedico=tmp.get(4);
							}
						
					}else{
						if(tmp.get(0)!=null && tmp.get(0).contains("Fecha Orden:")){
							if(tmp.get(1)!=null){
								fechaCreacion=tmp.get(1);
								}
							
							if(tmp.get(2)!=null){
								fechaorden=tmp.get(2);
								}
							
							if(tmp.get(3)!=null){
								observacion=tmp.get(3);
								}
							
							
							if(tmp.get(4)!=null){
								datosMedico=tmp.get(4);
								}
							
						}
						}
					String insertDetalleObservaciones="insert  INTO ordenes.DETALLE_OBSERVACION_ORDEN_MED VALUES (?,?, ?, ?, ?,?,?)";
					PreparedStatement ps2 = con.prepareStatement(insertDetalleObservaciones);
					ps2.setInt(1, pkDetalleObservacion);
					ps2.setString(2, fechaCreacion);
					ps2.setString(3, fechaorden);
					ps2.setString(4, observacion);
					ps2.setString(5, datosMedico);
					ps2.setInt(6, codigo);
					ps2.setNull(7, Types.NUMERIC);
					ps2.execute();
					ps2.close();
				}
				
				
			}
			else
			{
				codigo = obtenerCodigoOrden (con, codigoOrden);
				modOrden = " INSERT INTO ordenes_medicas " +
						   " (codigo,cuenta,descripcion_soporte,descripcion_dieta_oral,observaciones_generales, descripcion_dieta_par) " +
						   " VALUES (?, ?, ?, ?, ?, ?) ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(modOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigo);				
				ps.setInt(2, cuenta);								
				ps.setString(3,null);//descripcionSoporteRespiratorio 
				ps.setString(4,descripcionDieta);					
				ps.setString(5,""); //observacionesGenerales
				ps.setString(6,descripcionDietaParenteral);					
				resp = ps.executeUpdate();
				
//				observacionesGenerales=observacionesGenerales.replace("\n", "");
				//				observacionesGenerales=observacionesGenerales.replace("\r", "");
				String[] observacionesArray = observacionesGenerales.split(ConstantesBD.separadorSplit);
				
				if(tieneDatos){
					List<String> tmp = new ArrayList<String>();
					for (int i = 0; i < observacionesArray.length; i++) {
						if(!observacionesArray[i].equals("") && !observacionesArray[i].equals("\n") ){
							tmp.add(observacionesArray[i]);
							}
						}
					String fechaCreacion = "";
					String fechaorden="";
					String observacion="";
					String datosMedico="";
					if(tmp.get(0)!=null){
						fechaCreacion=tmp.get(1);
						}
				
					if(tmp.get(1)!=null){
						fechaorden=tmp.get(2);
						}
					
					if(tmp.get(2)!=null){
						observacion=tmp.get(3);
						}
					
					
					if(tmp.get(3)!=null){
						datosMedico=tmp.get(4);
						}
					
					
					String insertDetalleObservaciones="insert INTO ordenes.DETALLE_OBSERVACION_ORDEN_MED VALUES (?,?, ?, ?, ?,?,?)";
					
					PreparedStatement ps2 = con.prepareStatement(insertDetalleObservaciones);
					ps2.setInt(1, pkDetalleObservacion);
					ps2.setString(2, fechaCreacion);
					ps2.setString(3, fechaorden);
					ps2.setString(4, observacion);
					ps2.setString(5, datosMedico);
					ps2.setInt(6, codigo);
					ps2.setNull(7, Types.NUMERIC);
					ps2.execute();
					ps2.close();
				}
				
				
			}
			
			
			ps.close();
			if (resp > 0){			
				resultado = new DtoResultado();
				resp=codigo;
				resultado.setPk(String.valueOf(resp));
				if(tieneDatos){
					resultado.setPk2(pkDetalleObservacion.toString());
				}
				else{
					resultado.setPk2(null);
				}
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserci�n de datos Orden Medica General : SqlBaseOrdenMedicaDao "+e.toString() );
		}
		
		return resultado;
	}

	/**
	 * Metodo que inserta la informacion general para las ordenes (nut parenteral, oral - tipo_monitoreo - soporterespiratorio)
	 * @param con
	 * @param secuencia
	 * @param codigoOrden
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param fechaOrden
	 * @param horaOrden
	 * @param login
	 * @param datosMedico
	 * @return
	 */
	public static int insertarEncabezadoOrdenMedica(Connection con, String secuencia, int codigoOrden, String fechaOrden, String horaOrden, String login, String datosMedico)
	{
		PreparedStatementDecorator ps = null;
		int resp=0, codigo=0;
	
		String insertarEnc = 	"INSERT INTO encabezado_histo_orden_m(codigo, " +
																	"orden_medica, " +
																	"fecha_grabacion, " +
																	"hora_grabacion, " +
																	"fecha_orden, " +
																	"hora_orden, " +
																	"login, " +
																	"datos_medico) " +
																	" VALUES " +
																	" (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTimeConSegundos()+", ?, ?, ?, ?) ";
		
		
		try	{					
						DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						codigo=myFactory.incrementarValorSecuencia(con, secuencia);
												
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarEnc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codigo);
						ps.setInt(2, codigoOrden);
						ps.setString(3, fechaOrden);
						ps.setString(4, horaOrden);
						ps.setString(5,login);
						ps.setString(6,datosMedico);
					
						resp = ps.executeUpdate();
						
						if (resp > 0){
							resp = codigo;
						}
							
		} catch(SQLException e){
			Log4JManager.error(e);
						resp = 0;
		} catch (Exception e) {
			Log4JManager.error(e);
		}finally {
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
				}
		}
								
		return resp;
	}

	
	
	/**
	 * M�todo para insertar el tipo de monitoreo a una orden m�dica
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @param tipoMonitoreo
	 * @param secuencia
	 * @return
	 */
	public static int insertarOrdenTipoMonitoreo(Connection con, int codigoEncabezado, int tipoMonitoreo)
	{
		int resp=0;
		String insertarOrdenTipoMonitoreoStr = 	"INSERT INTO orden_tipo_monitoreo (codigo_histo_encabezado, " +
																				  " tipo_monitoreo) " +
																				  " VALUES (?, ?) ";
			try{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarOrdenTipoMonitoreoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setInt(1, codigoEncabezado);
					ps.setInt(2, tipoMonitoreo);
					
					
	

					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserci�n de datos: SqlBaseOrdenMedicaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * M�todo para insertar el soporte respiratorio a una orden m�dica
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param equipoElemento
	 * @param cantidadSoporteRespiratorio
	 * @param oxigenoTerapia
	 * @param descripcionSoporteRespiratorio
	 * @return
	 */
	
	
	public static int insertarOrdenSoporteRespiratorio(Connection	con, int codigoEncabezado, int equipoElemento, float cantidadSoporteRespiratorio, String oxigenoTerapia, String descripcionSoporteRespiratorio)
	{
		int resp=0;
		String equipoElementoString="";
		String insertarOrdenSoporteRespiratorioStr = 	"INSERT INTO orden_soporte_respira ( codigo_histo_enca, " +
																							"equipo_elemento_cc_inst, " +
																							"cantidad, " +
																							"oxigeno_terapia, " +
																							"descripcion ) " +
																							" VALUES " +
																							" (?,?,?,?,?) ";
		try
			{
					if (con == null || con.isClosed()) 
					{
						DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						con = myFactory.getConnection();
					}
					
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarOrdenSoporteRespiratorioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					equipoElementoString=equipoElemento+"";
					
					ps.setInt(1, codigoEncabezado);
					
					if(equipoElementoString.equals("-1") || cantidadSoporteRespiratorio==0 || UtilidadTexto.isEmpty(oxigenoTerapia))
					{
						ps.setString(2, null);
					}
					else
					{
						ps.setString(2, equipoElementoString);	
					}
					ps.setFloat(3, cantidadSoporteRespiratorio);
										
					if(oxigenoTerapia.equals(""))
						ps.setString(4, null);
					else
						if(oxigenoTerapia.equals(ValoresPorDefecto.getValorTrueParaConsultas()))
							ps.setString(4, ValoresPorDefecto.getValorTrueParaConsultas());
						else
							ps.setString(4, ValoresPorDefecto.getValorFalseParaConsultas());
											
					if(descripcionSoporteRespiratorio.equals("")) {
						ps.setString(5, null);
					} else {
						ps.setString(5, descripcionSoporteRespiratorio);
					}
					
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserci�n de datos: SqlBaseOrdenMedicaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}


	/**
	 * Funcion Para retornar una collecion con el listado de los tipos de nutricion Oral
	 * @param con
	 * @param mezcla
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param inicioEncabezado @todo
	 * @param finEncabezado @todo
	 * @param TIPO_BD 
	 * @param codigo de la institucion
	 * @param codigo del centro_costo
	 * @param Nro Consulta parametro que indica la informacion a sacar
	 *        1  Listado de tipos de nutricion Oral        
	 * 		  2  Listado de tipos de nutricion parenteral
	 * 		  3  Listado de los tipos de cuidados de emfermeria
	 * 		  4  Listado de tipos de elementos en el soporte respiratorio
	 *  	  5  Listado de tipos de monitoreo	
	 * @return Collection 
	 */

	public static Collection consultarTipos(
			Connection con,
			int institucion,
			int centro_costo, int Nro_consulta, int mezcla, int codigoCuenta, int cuentaAsocio, int inicioEncabezado, int finEncabezado, int TIPO_BD)
	{
		logger.info("Consulta los tipos. Numero de Consulta >> "+Nro_consulta);
		StringBuffer consultaBuffer=new StringBuffer();
		String cuenta = "";
		
		
		if (cuentaAsocio!=0)
			cuenta = " WHERE om.cuenta IN ( "+ codigoCuenta+" , " + cuentaAsocio +" ) ";
		else
			cuenta = " WHERE om.cuenta = "+ codigoCuenta;
		
		
			//-Seleccionar el tipo de informacion a consultar
		  
		    switch(Nro_consulta)
			{
		        case 1:
		        	consultaBuffer.append(" SELECT " +
		        						  " tno.codigo as codigo_tipo_oral, " +
		        						  " tabla.codigo as codigo, " +
		        						  " tno.nombre as descripcion " +
		        						  "			FROM tipo_nutricion_oral tno " +
										  "				INNER JOIN ( SELECT codigo as codigo, nutricion_oral as codigo_tipo " +
										  "									FROM nutricion_oral_cc_inst " +
										  "							 			 WHERE activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										  "									  	   AND institucion = ? " +									
										  "									  	   AND centro_costo =  ? " +									
										  "							 UNION " +
										  "							 SELECT ono.nutricion_oral_cc_inst as codigo, noci.nutricion_oral as codigo_tipo " +
										  "									FROM orden_nutricion_oral ono" +
										  "										 INNER JOIN nutricion_oral_cc_inst noci ON ( ono.nutricion_oral_cc_inst = noci.codigo) 		" +
										  "										 INNER JOIN orden_dieta od ON  ( od.codigo_histo_enca = ono.codigo_historico_dieta )		" +
										  "										 INNER JOIN encabezado_histo_orden_m ehom ON ( ehom.codigo = od.codigo_histo_enca ) 		" +
										  "										 INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo ) " +
										  "										 " + cuenta + 
										  "									  	   GROUP BY ono.nutricion_oral_cc_inst, noci.nutricion_oral 	" +
										  "						   ) tabla ON ( tabla.codigo_tipo = tno.codigo  )   ");
				break;
				case 2:
					consultaBuffer.append(" 	" +
												"SELECT * FROM"+ 
												"("+ 
												"		SELECT"+ 
												"			nat.es_pos as espos, " +
												"			art.codigo as codigo, " +
												"			coalesce(art.descripcion,'') ||' : '||coalesce(art.concentracion,'')||' : '||coalesce(art.forma_farmaceutica,'')||' : '||coalesce(art.unidad_medida,'')||' : '||coalesce(nat.nombre,'') as descripcion, " +
												"           nat.es_medicamento as esmedicamento " +
												"		FROM"+ 
												"			articulo art"+ 
												"		INNER JOIN"+ 
												"			naturaleza_articulo nat	ON (art.naturaleza=nat.acronimo)"+ 
												"		INNER JOIN"+ 
												"			articulos_por_mezcla apm ON(art.codigo=apm.articulo)"+ 
												"		WHERE"+ 
												"			apm.mezcla="+mezcla+											 
												"	UNION"+ 
												"		SELECT"+ 
												"			nat.es_pos as espos, " +
												"			art.codigo as codigo, " +
												"			coalesce(art.descripcion,'') ||' : '||coalesce(art.concentracion,'')||' : '||coalesce(art.forma_farmaceutica,'')||' : '||coalesce(art.unidad_medida,'')||' : '||coalesce(nat.nombre,'') as descripcion," +
												" 		    nat.es_medicamento as esmedicamento " +																							 
												"		FROM"+ 
												"			orden_nutricion_parente onp"+ 
												"		INNER JOIN"+ 
												"			articulo art ON (art.codigo=onp.articulo)"+ 
												"		INNER JOIN"+ 
												"			naturaleza_articulo nat ON (art.naturaleza=nat.acronimo)"+ 
												"		INNER JOIN"+ 
												"		("+ 
												"			SELECT"+ 
												"				articulo"+ 
												"			FROM"+ 
												"				orden_nutricion_parente n"+ 
												"			INNER JOIN"+ 
												"				orden_dieta od ON (codigo_historico_dieta=codigo_histo_enca)"+ 
												"			WHERE"+ 
												"				articulo NOT IN"+ 
												"				("+ 
												"					SELECT articulo FROM articulos_por_mezcla WHERE mezcla="+mezcla+ 
												"				)"+ 
												"				AND mezcla="+mezcla
											);

											if(inicioEncabezado>=0 && finEncabezado >0)
											{
												consultaBuffer.append(
													"			AND od.codigo_histo_enca BETWEEN "+inicioEncabezado+" AND "+finEncabezado
												);
											}
											else
											{
												consultaBuffer.append(
													"			AND od.codigo_histo_enca >=maxHistoricoFinDieta("+mezcla+", "+codigoCuenta+", "+cuentaAsocio+")"
												);
													
											}
											consultaBuffer.append(
												"				GROUP BY articulo"+ 
												"		) articulos_sin_asocio ON(articulos_sin_asocio.articulo=art.codigo) "+ 
												" GROUP BY nat.es_pos,art.codigo,unidad_volumen, art.descripcion,art.concentracion,art.forma_farmaceutica,art.unidad_medida,nat.nombre,nat.es_medicamento)art" +
												" ORDER BY art.descripcion, art.esmedicamento "
											);

					    /*consultaStr = 	" SELECT  art.codigo as codigo, art.descripcion as descripcion FROM articulo art " +
								        "         INNER JOIN nutricion_parent_cc_inst npci ON (art.codigo = npci.articulo) " +
										"		        WHERE npci.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										"		          AND npci.institucion = ?  AND npci.centro_costo = ? ORDER BY codigo";*/
				break;
				case 3:				
					consultaBuffer.append(	"SELECT * FROM (SELECT  ceci1.codigo as codigo, tce.descripcion  as descripcion, 0 as tipo,tce.control_especial as controlespecial  FROM tipo_cuidado_enfermeria tce " +
											"						INNER JOIN ( " +	
											"										SELECT ceci.codigo as codigo, ceci.cuidado_enfermeria as codigo_tipo " +
											"											   FROM cuidado_enfer_cc_inst ceci  " +
											"													WHERE ceci.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + 
											"												   	  AND ceci.institucion = " + institucion +
											"						   							  AND ceci.centro_costo = " + centro_costo +  
											"										UNION " +
											"										SELECT dce.cuidado_enfer_cc_inst as codigo, ceci.cuidado_enfermeria as codigo_tipo " +
											"											   FROM detalle_cuidado_enfer dce  " +
											"													 INNER JOIN encabezado_histo_orden_m eho ON (eho.codigo=dce.cod_histo_enca) " + 
											"													 INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo)  " +
											"												     INNER JOIN cuidado_enfer_cc_inst ceci ON (dce.cuidado_enfer_cc_inst = ceci.codigo " +
											"													 AND ceci.centro_costo = " + centro_costo +")");

							if(cuentaAsocio != 0)
							  	consultaBuffer.append(" WHERE om.cuenta IN ( " + codigoCuenta + " ,  "+cuentaAsocio + ") ");
							  else
							  	consultaBuffer.append(" WHERE om.cuenta=" +codigoCuenta);					
							
					consultaBuffer.append(	"									 		 	    GROUP BY dce.cuidado_enfer_cc_inst, ceci.cuidado_enfermeria  " +
											"								  )   ceci1 ON (tce.codigo = ceci1.codigo_tipo) " +     
											" 	UNION ALL " +  //---La Informaci�n no parametrizada
											"	SELECT toce.codigo as codigo, toce.descripcion as descripcion, 1 as tipo,'N' as controlespecial  " +
											"		   FROM tipo_otro_cuidado_enf toce INNER JOIN detalle_otro_cuidado_enf doce ON (doce.otro_cual=toce.codigo) " +
											"				INNER JOIN encabezado_histo_orden_m eho ON (eho.codigo=doce.cod_histo_cuidado_enfer) " +
											"				INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo) ");
											
						 if(cuentaAsocio != 0)
						  	consultaBuffer.append(" WHERE om.cuenta IN (?,?) GROUP BY toce.codigo,toce.descripcion)ce ");
						  else
						  	consultaBuffer.append(" WHERE om.cuenta=? GROUP BY toce.codigo,toce.descripcion)ce ");					
							
						 consultaBuffer.append(" Order By ce.controlespecial desc,ce.descripcion ");
						 
				break;
				case 4:
					consultaBuffer.append(" SELECT  tabla.codigo as codigo, eq.descripcion  as descripcion " +
										  "			FROM equipo_elemento eq " +
										//"	        INNER JOIN equipo_elemento_cc_inst eeci ON (eq.codigo = eeci.equipo_elemento) 
											"       INNER JOIN ( SELECT codigo as codigo, equipo_elemento as codigo_tipo " +
											"							FROM equipo_elemento_cc_inst " +
											"								 WHERE activo = " +ValoresPorDefecto.getValorTrueParaConsultas() +
											"	    					       AND institucion = ? " +
											"								   AND centro_costo = ? " +
											"					UNION" +
											"					SELECT osr.equipo_elemento_cc_inst as codigo, eeci.equipo_elemento as codigo_tipo   " +
											"						   FROM orden_soporte_respira osr " +
											"								INNER JOIN encabezado_histo_orden_m ehom ON ( ehom.codigo = osr.codigo_histo_enca ) " +
											"								INNER JOIN ordenes_medicas om ON (  ehom.orden_medica = om.codigo )" +
											"								INNER JOIN equipo_elemento_cc_inst eeci ON ( osr.equipo_elemento_cc_inst = eeci.codigo ) " +
											"									 	" + cuenta + ") tabla ON ( tabla.codigo_tipo = eq.codigo  ) ");
				break;
				case 5:
					/*consultaBuffer.append(" select codigo as codigo, nombre as nombre from tipo_monitoreo where  " +
							" (SELECT cm.es_uci as es_uci FROM camas1 cm " +
			   				"		  INNER JOIN admisiones_hospi ah ON (cm.codigo=ah.cama) " +
			   				"		  INNER JOIN cuentas cu ON (cu.id=ah.cuenta) " + 
			   			    "				WHERE cu.codigo_paciente = " +	codigoPersona +					   			    
			  				"	 UNION    						 		" +
			  				"											" +	
							"	 SELECT cm.es_uci as es_uci FROM camas1 cm  		" +
			  				"		 INNER JOIN admisiones_urgencias au ON (cm.codigo=au.cama_observacion) " +
			   				"		 INNER JOIN cuentas cu ON (cu.id=au.cuenta) " +
			   				"			   WHERE cu.codigo_paciente=" + codigoPersona + ")");*/

				/*	consultaBuffer.append(" SELECT codigo as codigo, nombre as nombre from tipo_monitoreo where  " +
								  		  " 	   (SELECT cm.es_uci as es_uci FROM camas1 cm " +
							   			  "				  INNER JOIN admisiones_hospi ah ON (cm.codigo=ah.cama) " +
							   			  "			 	  INNER JOIN cuentas cu ON (cu.id=ah.cuenta) " + 
							   			  "						WHERE cu.codigo_paciente = " +	codigoPersona +					   			    
							  			  "			UNION    						 		" +
							  			  "											" +	
										  "			SELECT cm.es_uci as es_uci FROM camas1 cm  		" +
							  			  "				   INNER JOIN admisiones_urgencias au ON (cm.codigo=au.cama_observacion) " +
							   			  "			 	   INNER JOIN cuentas cu ON (cu.id=au.cuenta) " +
							   			  "				  		 WHERE cu.codigo_paciente=" + codigoPersona + ")" +
							   			  "							   AND institucion = ? ");*/
					
					consultaBuffer.append("SELECT codigo AS codigo, nombre AS nombre " +
															  "		FROM tipo_monitoreo " +
															  "			WHERE "+ValoresPorDefecto.getValorTrueParaConsultas()+" IN "+
															  "				(" +
															  "					(SELECT cam.es_uci " +
															  "							FROM traslado_cama tc " +
															  "								INNER JOIN camas1 cam ON (tc.codigo_nueva_cama=cam.codigo) ");
																	if(cuentaAsocio != 0)
																	  	consultaBuffer.append(" WHERE tc.cuenta IN (" + codigoCuenta + ", " + cuentaAsocio + ")  ");
																	  else
																	  	consultaBuffer.append(" WHERE tc.cuenta=" + codigoCuenta);
																	
																	
																			consultaBuffer.append(" ) UNION " +
																"				(SELECT cm.es_uci as es_uci " +
																"					FROM camas1 cm  		" +
																"						   INNER JOIN admisiones_urgencias au ON (au.cama_observacion=cm.codigo) ");  
																	if(cuentaAsocio != 0)
																	  	consultaBuffer.append(" WHERE au.cuenta IN (" + codigoCuenta + ", " + cuentaAsocio + ")  ");
																	  else
																	  	consultaBuffer.append(" WHERE au.cuenta=" + codigoCuenta);
																	
																	switch (TIPO_BD) {
																	case DaoFactory.ORACLE:
																		
																		consultaBuffer.append(")" +
																		") AND institucion = ? and rownum = 1 ORDER BY codigo DESC ");
																		break;
																	case DaoFactory.POSTGRESQL:
																		consultaBuffer.append(")" +
																		") AND institucion = ? ORDER BY codigo DESC "+ValoresPorDefecto.getValorLimit1()+" 1");
																		break;

																	default:
																		break;
																	}
																	
					
				break;
				case 6:
					consultaBuffer.append("" +
								"SELECT " +
									"od.mezcla AS codigo, " +
									"m.codigo AS codigo_mezcla, " +
									"m.nombre AS nombre_mezcla " +
								"FROM " +
									"orden_dieta od " +
									"INNER JOIN " +
										"encabezado_histo_orden_m enc " +
										"ON(enc.codigo=od.codigo_histo_enca) " +
									"INNER JOIN " +
										"ordenes_medicas om " +
										"ON(enc.orden_medica=om.codigo) " +
									"INNER JOIN mezcla m " +
										"ON(od.mezcla=m.consecutivo) " +
									"WHERE " +
											"via_parenteral="+ValoresPorDefecto.getValorFalseParaConsultas()+" " +
										"AND " +
											"cuenta IN ("+codigoCuenta+","+cuentaAsocio+") " +
										"GROUP BY " +
											"od.mezcla, " +
											"m.codigo, " +
											"m.nombre" 
										);
				break;
				default :
				{
					logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-5] El valor recibido es "+ Nro_consulta + "\n\n" );
					return null;
				}
			}
		    
		try
		{
         	logger.info("\n\n\n [" + Nro_consulta + "]  La sentencia SQL BASE (tipos) \n" + consultaBuffer.toString() + " \n\n\n");
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			//--Consultar tipos de monitoreo
			if (Nro_consulta==5)
			{
				consultarNov.setInt(1, institucion);
			}

			if (Nro_consulta!=5 && Nro_consulta!=2 && Nro_consulta!=6 && Nro_consulta!=3)
			{
				consultarNov.setInt(1, institucion);
				consultarNov.setInt(2, centro_costo);
				logger.info("Institucion: "+institucion);
				logger.info("Centro de Costo: "+centro_costo);
			}	
			if(Nro_consulta == 3)
			{
				
				consultarNov.setInt(1, codigoCuenta);
				
				if(cuentaAsocio != 0)
					consultarNov.setInt(2, cuentaAsocio);
			}
		
		   
	 	   return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Tipos de nutricion ---: "+e.toString());
			return null;
		}
	}

	/**
	 * Metodo para insertar la nutricion oral y parenteral
	 * @param con
	 * @param ordenMedica
	 * @param tipoNutricion
	 * @return
	 * @throws SQLException
	 */
	public static int insertarNutricion(
			Connection con,
			int codigoEncabezado,
			int tipoNutricion,
			float volumen,
			String unidadVolumen,
			int tipoNut,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			String esMedicamento) throws IPSException
	{
		int resp=0;
		PreparedStatementDecorator ps;
		String insertarTipoNut = "INSERT INTO orden_nutricion_oral ( codigo_historico_dieta, " +
																	"nutricion_oral_cc_inst) " +
																	"VALUES (?, ?) ";
		
		

		String insertarTipoNutParent = "INSERT INTO orden_nutricion_parente (codigo_historico_dieta, " +
																			"articulo, " +
																			"volumen," +
																			"unidad_volumen) " +
																			"VALUES (?, ?, ?,?) ";
		try	{
			
					if (tipoNut ==  1) //-Insercion de nutricion Oral 
					{
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarTipoNut,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigoEncabezado);
						ps.setInt(2, tipoNutricion);
		 			    resp=ps.executeUpdate();
					}
					else       		   //-Para insertar datos en nutricion parenteral 
					{
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarTipoNutParent,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigoEncabezado);
						ps.setInt(2, tipoNutricion);
						ps.setFloat(3, volumen);
						ps.setString(4,unidadVolumen);
						
		 			    resp=ps.executeUpdate(); 

						ps= new PreparedStatementDecorator(con.prepareStatement("SELECT numero_solicitud AS numero_solicitud FROM solicitudes_medicamentos WHERE orden_dieta=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigoEncabezado);
						ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
						int numeroSolicitud=0;
						if(resultado.next())
						{
							numeroSolicitud=resultado.getInt("numero_solicitud");
						}
						else
						{
							return 0;
						}
						
						//Solo se genera solicitudes de medicamentos para cantidades mayores a cero
						if(volumen > 0)
						{
							logger.info("\n\n\n\n [Entro Insertar Mezcla Articulos (insetar detalle_solicitudes)] \n\n\n\n");
							PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("INSERT INTO detalle_solicitudes (numero_solicitud, articulo, dosis, cantidad) VALUES (?,?,?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1, numeroSolicitud);
							pst.setInt(2, tipoNutricion);
							
							//Cambio para el despacho de medicamentos e insumos
							if(esMedicamento.equals(ConstantesBD.acronimoNo))
							{
								pst.setNull(3,Types.VARCHAR);
								pst.setInt(4,UtilidadTexto.aproximarSiguienteUnidad(volumen+""));							
							}
							else
							{
								pst.setString(3,volumen+"");
								pst.setInt(4,0);						
							}
							
							pst.executeUpdate();
							pst.close();
							
							Cargos cargoArticulos= new Cargos();
							cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(con, 
										usuario, 
										paciente, 
										numeroSolicitud,
										tipoNutricion,
										0,/*cantidad*/ 
										true/*dejarPendiente*/, 
										ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
										ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
										SqlBaseSolicitudDao.obtenerCodigoCentroCostoSolicitado(con, numeroSolicitud+""),
										ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
										Utilidades.esSolicitudPYP(con, numeroSolicitud),"", false /*tarifaNoModificada*/);
						}
					}
					
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserci�n de datos en Nutrici�n: SqlBaseOrdenMedicaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}

	/**
	 *  Metodo para insertar los datos de Orden Dieta 
	 * @param con
	 * @param codigoEncabezado
	 * @param velocidadInfusion
	 * @param farmacia
	 * @param nutricionOral
	 * @param nutricionParenteral
	 * @param finalizarDieta
	 * @param mezcla
	 * @param numeroSolicitud @todo
	 * @param dosificacion 
	 * @param volumentTotal
	 * @return
	 */
	public static int insertarOrdenDieta(
			Connection con,
			int codigoEncabezado,			
			String volumenTotal,
			String unidadVolumenTotal,
			String velocidadInfusion, 
			int farmacia,
			boolean nutricionOral,
			boolean nutricionParenteral,
			boolean finalizarDieta,
			int mezcla,			
			int numeroSolicitud,
			String codigoInstitucion,
			String dosificacion) 
	{	
		PreparedStatementDecorator ps;
		int resp=0;
		String centroCostoPrincipal = Utilidades.obtenerCentroCostoPrincipal(con, farmacia+"", codigoInstitucion);
		
		logger.info("Codigo Encabezado ->"+codigoEncabezado);
		logger.info("Volumen Total ->"+volumenTotal);
		logger.info("Velocidad Infusion ->"+velocidadInfusion);
		logger.info("Farmacia ->"+farmacia);
		logger.info("Centro Costo Principal ->"+centroCostoPrincipal);
		logger.info("Nutricion Oral ->"+nutricionOral);
		logger.info("Nutricion Parental ->"+nutricionParenteral);
		logger.info("Finalizar Dieta ->"+finalizarDieta);
		
		String insertarOrdenDieta = 	"INSERT INTO orden_dieta(codigo_histo_enca, " +
																"volumen_total, " +
																"velocidad_infusion, " +
																"farmacia, " +
																"via_oral, " +
																"via_parenteral, " +
																"finalizar_dieta," +
																"mezcla," +
																"suspendido," +
																"finaliza_sol," +
																"dosificacion," +
																"unidad_volumen_total) " +
																"VALUES " +
																"(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?) ";
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarOrdenDieta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoEncabezado);
			
			if(!volumenTotal.equals(""))
				ps.setString(2, volumenTotal);
			else
				ps.setNull(2,Types.VARCHAR);
			
			if(!velocidadInfusion.equals(""))			
				ps.setString(3, velocidadInfusion);
			else
				ps.setNull(3,Types.VARCHAR);
			
			if(farmacia >= 0)			
				ps.setInt(4, farmacia);
			else
				ps.setNull(4,Types.VARCHAR);
				
			ps.setBoolean(5, true); //La ORDEN DIETA SIEMPRE VA HA SER ORAL ---para cambios futuros a no Oral utilizar la variable nutricionOral 
			ps.setBoolean(6, nutricionParenteral);
			ps.setBoolean(7, finalizarDieta);			
			
			if(mezcla<=0)			
				ps.setNull(8,Types.INTEGER);			
			else			
				ps.setInt(8,mezcla);			
			
			ps.setBoolean(9, false);
			ps.setBoolean(10, nutricionParenteral);
			ps.setString(11, dosificacion);			
			ps.setString(12,unidadVolumenTotal);
	
			resp = ps.executeUpdate();
			if(numeroSolicitud>0 && resp>0)
			{
				try{
					String pendienteCompletar=""; 
					if(!velocidadInfusion.equals(""))
					{
						pendienteCompletar=ConstantesBD.acronimoNo;
					}
					else
					{
						pendienteCompletar=ConstantesBD.acronimoSi;
					}
					String insertarSolicitudMezcla="INSERT INTO solicitudes_medicamentos(numero_solicitud, orden_dieta, centro_costo_principal,pendiente_completar) VALUES(?,?,?,?)";
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(insertarSolicitudMezcla,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setInt(1, numeroSolicitud);
					stm.setInt(2, codigoEncabezado);
					stm.setObject(3, centroCostoPrincipal);
					stm.setString(4, pendienteCompletar);
					if(stm.executeUpdate()<=0)
					{
						logger.error("Error ingresando la solicitud de mezcla");
					}
				}
				catch (SQLException e) {
					logger.error("Error ingresando la solicitud de mezcla "+e);
					return -1;
				}
			}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserci�n de datos (Orden Dieta) : SqlBaseOrdenMedicaDao "+e.toString() );
				resp = 0;
		}
		logger.info("--->"+resp);
		return resp;
	}

	
	
	/**
	 * Metodo Para Insertar Los cuidados especiales de emfermeria 
	 * @param con
	 * @param codigoOrden
	 * @param login
	 * @param string
	 * @param string2
	 * @param datosMedico
	 * @return
	 */
	public static int insertarOrdenCuidadoEnf(Connection con,String codigoCuidadoEnf, int codigoOrden, String login, String datosMedico)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
	
		String insertarOrdenDieta = 	"INSERT INTO orden_cuidado_enfermeria(codigo, " +
																			"orden_medica, " +
																			"login, " +
																			"fecha_grabacion, " +
																			"hora_grabacion, " +		
																			"datos_medico) " +
																			"VALUES " +
																			"(?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?) ";
		
		
		try	{
			
			            //-Obtener el codigo de la dieta 
						codigo = obtenerCodigoOrden (con, codigoCuidadoEnf);
			            
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarOrdenDieta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codigo);
						ps.setInt(2, codigoOrden);
						ps.setString(3,login);
						ps.setString(4,datosMedico);
					
						resp = ps.executeUpdate();					
						if (resp > 0)
						{
							resp = codigo;
						}
							
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserci�n de datos (Orden Dieta) : SqlBaseOrdenMedicaDao "+e.toString() );
						resp = 0;
				}
				return resp;
	}

	/**
	 * Metodo para insertar el detalle de cuidado emfermeria  
	 * @param con
	 * @param codigoEncabezado
	 * @param tipoCuidado
	 * @param presenta
	 * @param descripcion
	 * @param OtroCuidadoEnf
	 * @return
	 */
	
	public static int insertarDetalleOrdenCuidadoEnf(Connection con, int codigoEncabezado, int tipoCuidado, String presenta, String descripcion, int OtroCuidadoEnf)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarDetCuidadoEnf = 	"";
		
		try	{
			
			
			//----verificar si escribieron algun en el campo cual de la opcion Otro 
			if (tipoCuidado == -1)
			{
				//-Para Insertar En detalle 
				insertarDetCuidadoEnf = "INSERT INTO detalle_otro_cuidado_enf (cod_histo_cuidado_enfer, " +
																			  "otro_cual, " +
																			  "presenta," +
																			  "descripcion) VALUES (?, ?, ?, ?) ";
				

				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetCuidadoEnf,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setInt(1, codigoEncabezado);
				ps.setInt(2, OtroCuidadoEnf);
				
				if (!UtilidadCadena.noEsVacio(presenta) ) 
				{
					ps.setString(3,null);
				}
				else if (presenta.equals("si"))
				{
					ps.setBoolean(3,true);		
				}
				else if (presenta.equals("no"))
				{
					ps.setBoolean(3,false);		
				}
				
				ps.setString(4,descripcion);
				
			}
			else		
			{            
						//-Para Insertar En detalle 
						insertarDetCuidadoEnf = "INSERT INTO detalle_cuidado_enfer (cod_histo_enca, " +
																					"cuidado_enfer_cc_inst, " +
																					"presenta, " +
																					"descripcion)" +
																					"VALUES " +
																					"(?, ?, ?, ?) ";

						
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetCuidadoEnf,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						ps.setInt(1, codigoEncabezado);
						ps.setInt(2, tipoCuidado);
						
						if (!UtilidadCadena.noEsVacio(presenta) ) 
						{
							ps.setString(3,null);
						}
						else if (presenta.equals("si"))
						{
							ps.setBoolean(3,true);		
						}
						else if (presenta.equals("no"))
						{
							ps.setBoolean(3,false);		
						}
						
						ps.setString(4,descripcion);
					}
			logger.info("\n=====>Insert Orden Medica: "+insertarDetCuidadoEnf+" ====>Codigo Encabezado: "+codigoEncabezado+" ====>Tipo Cuidado: "+tipoCuidado+" ====>Presenta: "+presenta+" ====>Descripcion: "+descripcion);
			//-----Ejecutar la insercion 
			resp = ps.executeUpdate();			
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en Insertar Detalle cuidados Enfermeria : SqlBaseOrdenMedicaDao "+e.toString() );
				resp = 0;
		}
		logger.info("\n=====>Orden Medica: "+resp);
		return resp;
	}
	
	/**
	 * Funcion para cargar los datos de la orden medica General 
	 * @param con
	 * @param codigoCuenta
	 * @return Collection con los datos de la orden m�dica
	 */
	public static Collection cargarOrdenMedica(Connection con, int codigoCuenta)
	{
		String consultaStr = "SELECT  om.codigo as codigo, to_char(eh.fecha_orden,'dd/mm/yyyy') as fecha_orden, eh.hora_orden as hora_orden, om.observaciones_generales as observaciones_generales, " +
							 "        om.descripcion_soporte as descripcion_soporte, om.descripcion_dieta_oral as descripcion_dieta_oral, om.descripcion_dieta_par AS descripcion_dieta_par " +
							 "		  FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eh ON (eh.orden_medica=om.codigo) " +
							 "			   WHERE eh.codigo=  (SELECT MAX(eh.codigo) FROM encabezado_histo_orden_m eh " +
							 								" INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
															" WHERE om.cuenta=?)";
		
		
		try
		{
			
			//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
         	PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoCuenta);

 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Orden Medica General :"+e.toString());
			return null;
		}
	}

	public static List<DtoObservacionesGeneralesOrdenesMedicas> consultarObservacionesOrdenMedica(Connection con, int codigoCuenta) throws SQLException{
		List<DtoObservacionesGeneralesOrdenesMedicas> listaObservaciones = new ArrayList<DtoObservacionesGeneralesOrdenesMedicas>();
		
		String consulta = " SELECT dom.fecha_generacion,dom.fecha_orden,dom.observacion,dom.datos_medico "+
		" FROM ordenes_medicas om "+
		" JOIN ordenes.detalle_observacion_orden_med dom "+
			" ON (om.codigo  = dom.id_orden_medica) "+
			" WHERE om.cuenta=? order by dom.id asc ";
		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, codigoCuenta);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			DtoObservacionesGeneralesOrdenesMedicas dtoObservacionesGeneralesOrdenesMedicas= new DtoObservacionesGeneralesOrdenesMedicas();
			dtoObservacionesGeneralesOrdenesMedicas.setFechaGeneracion(rs.getString("fecha_generacion"));
			dtoObservacionesGeneralesOrdenesMedicas.setFechaOrden(rs.getString("fecha_orden"));
			dtoObservacionesGeneralesOrdenesMedicas.setObservaciones(rs.getString("observacion"));
			dtoObservacionesGeneralesOrdenesMedicas.setDatosMedico(rs.getString("datos_medico"));
			listaObservaciones.add(dtoObservacionesGeneralesOrdenesMedicas);
			}
		rs.close();
		ps.close();
		
		return listaObservaciones;
		
		
	}
	
	/**
	 * Funcion para retornar la informacion historica de la nutricion parenteral 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param mezcla @todo
	 * @return
	 */
	
	public static Collection consultarNutricionParentHisto(Connection con, int codigoCuenta, int cuentaAsocio, int mezcla)
	{
		logger.info("\n entre a  consultarNutricionParentHisto cuenta-->"+codigoCuenta+"   asocio -->"+cuentaAsocio+" mezcla -->"+mezcla );
		
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append(" SELECT " +
									" to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, " +
									" od.codigo_histo_enca as coddieta, art.codigo as codnut," +
									" onp.volumen as volumen, "+ 
									" coalesce(od.volumen_total,'') as volumen_total, " +
									" coalesce(od.velocidad_infusion,'') as velocidad_infusion," +
									" od.dosificacion," +
									" sm.numero_solicitud," +
									" s.centro_costo_solicitado as centrocostosolicitado, " +
									" s.estado_historia_clinica," +
									" coalesce(onp.unidad_volumen,'') AS unidad_volumen," +
									" coalesce(od.unidad_volumen_total,'') AS unidad_volumen_total, " +
									" coalesce(od.dosificacion, '') as dosificacion, " +
									" od.finaliza_sol  As finalizasol "+
								" FROM orden_dieta od" +
								" LEFT OUTER JOIN orden_nutricion_parente onp on (od.codigo_histo_enca=onp.codigo_historico_dieta) " +
								" LEFT OUTER JOIN articulo art ON (art.codigo=onp.articulo) " + 				  
								" INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = od.codigo_histo_enca) "+
								" INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
								" INNER JOIN solicitudes_medicamentos sm ON (sm.orden_dieta =od.codigo_histo_enca) " +
								" INNER JOIN solicitudes s on (s.numero_solicitud=sm.numero_solicitud) " +
								" LEFT OUTER JOIN " +
									" (" +
									" SELECT articulo" +
									" FROM" +
										" articulos_por_mezcla apm" +
									" WHERE mezcla="+mezcla +
									" UNION"+
									" SELECT " +
										" articulo" +
									" FROM orden_nutricion_parente n" +
									" INNER JOIN orden_dieta o ON (codigo_historico_dieta=codigo_histo_enca)" +
									" WHERE mezcla="+mezcla+" AND articulo NOT IN(SELECT articulo FROM articulos_por_mezcla WHERE mezcla="+mezcla+")" +
									" GROUP BY articulo"+
									" ) articulos_sin_asocio ON(articulos_sin_asocio.articulo=art.codigo)"+
								"");
		
		if(cuentaAsocio != 0)
			consultaBuffer.append(" WHERE om.cuenta IN (?,?) ");
		else
			consultaBuffer.append(" WHERE om.cuenta=? ");
		
		consultaBuffer.append(
				"AND od.codigo_histo_enca IN "+
				"("+
					" SELECT od.codigo_histo_enca " +
					" FROM" +
						" orden_dieta od"+
					" INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = od.codigo_histo_enca)"+
					" INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo)"+
					/* El cero est� reservado para ponerle la cuenta de asocio*/
					" WHERE om.cuenta IN("+codigoCuenta+", 0) "+// AND finaliza_sol=" + ValoresPorDefecto.getValorFalseParaConsultas()+
					" AND od.mezcla="+mezcla+
				") " +
				" AND od.mezcla="+mezcla
		);
		
		consultaBuffer.append("  ORDER BY enc.fecha_grabacion DESC, enc.hora_grabacion DESC "); 
								
		logger.info("\n\n\n\n\n\n\n\n --->"+consultaBuffer.toString().replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");

		try
		{
			
			
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoCuenta);
			
			if(cuentaAsocio != 0)
				consultarNov.setInt(2, cuentaAsocio);

 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Historicos Nutricion Parenteral :"+e.toString());
			return null;
		}
	}


	/**
	 * Metodo para consultar los historicos del tipo de monitoreo en la orden medica     
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio 
	 * @param cadenaHoraOrden TODO
	 * @return
	 */
	public static Collection consultarMonitoreosHisto(Connection con, int codigoCuenta, int cuentaAsocio, String cadenaHoraOrden)
	{
		StringBuffer consultaBuffer=new StringBuffer();
				
		consultaBuffer.append("SELECT  tm.nombre as tipo_monitoreo, " +  
							 "		  to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha, "+cadenaHoraOrden+" AS hora, " +
							 "		  per.primer_nombre || ' ' || per.segundo_nombre ||' '|| per.primer_apellido ||' '|| per.segundo_apellido as medico " + 
							 "		  FROM orden_tipo_monitoreo otm " + 
							 "				INNER JOIN tipo_monitoreo tm ON (otm.tipo_monitoreo=tm.codigo) " +
			 	  			 "				INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = otm.codigo_histo_encabezado) " + 
							 "				INNER JOIN usuarios us ON (enc.login=us.login) " +
							 " 				INNER JOIN personas per  ON (us.codigo_persona=per.codigo) " +
							 "				INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) ");
		
		if(cuentaAsocio != 0)
			consultaBuffer.append(" WHERE om.cuenta IN (?, ?) ");
		else	
			consultaBuffer.append( " WHERE om.cuenta = ? ");
		
		consultaBuffer.append(" ORDER BY enc.fecha_grabacion DESC, enc.hora_grabacion DESC");
		 				
				try
		{
			
			
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoCuenta);
		
			
			//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			
			if(cuentaAsocio != 0)
				consultarNov.setInt(2, cuentaAsocio);

 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Historicos Tipo Monitoreo :"+e.toString());
			return null;
		}
	}
	
	/**
	 * Metodo para consultar los historicos del soporte respiratorio
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param cadenaHoraOrden TODO
	 * @param cadenaHoraGrabacion TODO
	 * @return
	 */
	
	public static Collection consultarSoporteRespiraHisto(Connection con, int codigoCuenta, int cuentaAsocio, String cadenaHoraOrden, String cadenaHoraGrabacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append(" SELECT * FROM (SELECT osr.oxigeno_terapia as oxigeno_terapia, osr.descripcion as descripcion_soporte, "+
		   					 "		  osr.cantidad as cantidad,	ee.descripcion as equipo_elemento,  " + 
							 "		  to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha, "+cadenaHoraOrden+" AS hora, " +
							 "		  to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, "+cadenaHoraGrabacion+" AS hora_grabacion, " +
							 "		  per.primer_nombre || ' ' || per.segundo_nombre ||' '|| per.primer_apellido ||' '|| per.segundo_apellido as medico " + 
							 "		  FROM orden_soporte_respira osr "+	 	 
							 "			  INNER JOIN equipo_elemento_cc_inst eeci ON (osr.equipo_elemento_cc_inst=eeci.codigo) " + 				  			  
							 "			  INNER JOIN equipo_elemento ee ON (eeci.equipo_elemento=ee.codigo) " + 				  
							 "		 	  INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = osr.codigo_histo_enca) " + 
							 "			  INNER JOIN usuarios us ON (enc.login=us.login) " +
							 "			  INNER JOIN personas per  ON (us.codigo_persona=per.codigo) " +
							 "			  INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) ");
							
		if(cuentaAsocio != 0)
			consultaBuffer.append("  WHERE om.cuenta IN (?, ?) ");
		else
			consultaBuffer.append("  WHERE om.cuenta = ?  ");
		
		consultaBuffer.append("					    AND eeci.activo =  "  + ValoresPorDefecto.getValorTrueParaConsultas() +
									 "	UNION " + 
									 "	SELECT osr.oxigeno_terapia as oxigeno_terapia, osr.descripcion as descripcion_soporte, " +
									 "		   osr.cantidad as cantidad, '' as equipo_elemento, " +
									 "			to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha, "+cadenaHoraOrden+" AS hora, " +
									 "		  to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, "+cadenaHoraGrabacion+" AS hora_grabacion, " +
									 "		    per.primer_nombre || ' ' || per.segundo_nombre ||' '|| per.primer_apellido ||' '|| per.segundo_apellido as medico " + 
									 "	 	 FROM orden_soporte_respira osr	 " +
									 "		 	  INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = osr.codigo_histo_enca) " + 
									 "			  INNER JOIN usuarios us ON (enc.login=us.login) " +
									 "			  INNER JOIN personas per  ON (us.codigo_persona=per.codigo) " +
									 "			  INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) ");
		
		if(cuentaAsocio != 0)
			consultaBuffer.append("  WHERE om.cuenta IN (?, ?) ");
		else
			consultaBuffer.append("  WHERE om.cuenta = ?  ");
		
		consultaBuffer.append("					  AND osr.equipo_elemento_cc_inst IS NULL) x " +
		 							"							ORDER BY fecha_grabacion DESC, hora_grabacion DESC");		
		
		
		try
		{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (cuentaAsocio != 0)
			{
				consultarNov.setInt(1, codigoCuenta);
				consultarNov.setInt(2, cuentaAsocio);
				consultarNov.setInt(3, codigoCuenta);
				consultarNov.setInt(4, cuentaAsocio);
			}
			else
			{
				consultarNov.setInt(1, codigoCuenta);
				consultarNov.setInt(2, codigoCuenta);
			}

 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Historicos Soporte respiratorio :"+e.toString());
			return null;
		}
	}

	/**
	 * Metodo para consultar los historicos de la dieta nutricional  de un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param cadenaHoraOrden TODO
	 * @param cadenaHoraGrabacion TODO
	 * @return
	 */
	
	public static Collection consultarNutricionOralHisto(Connection con, int codigoCuenta, int cuentaAsocio, String cadenaHoraOrden, String cadenaHoraGrabacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String query="SELECT * FROM (SELECT od.codigo_histo_enca AS codigo, od.via_oral as via_oral, od.finalizar_dieta as finalizar_dieta, tno.nombre as nutricion_oral, " +
				  "to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha_orden, " +
				  cadenaHoraOrden+" as hora_orden," +
				  "to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, " +
				  cadenaHoraGrabacion+" as hora_grabacion," +
				  "per.primer_nombre || ' ' || per.segundo_nombre ||' '||  per.primer_apellido ||' '|| per.segundo_apellido as medico " +
				  "FROM orden_nutricion_oral ono " +
				  "INNER JOIN nutricion_oral_cc_inst noci ON (ono.nutricion_oral_cc_inst=noci.codigo)  " +
				  "INNER JOIN tipo_nutricion_oral tno ON (noci.nutricion_oral=tno.codigo) " +
				  "INNER JOIN orden_dieta od ON  (od.codigo_histo_enca=ono.codigo_historico_dieta)  " +
				  "INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo =  od.codigo_histo_enca)  " +
				  "INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
				  "INNER JOIN usuarios us ON  (enc.login=us.login) " +
				  "INNER JOIN personas per ON (us.codigo_persona=per.codigo) ";

		
		consultaBuffer.append(query);
		/*consultaBuffer.append("SELECT * FROM (SELECT od.codigo_histo_enca AS codigo, od.via_oral as via_oral, od.finalizar_dieta as finalizar_dieta, tno.nombre as nutricion_oral, " +
											  "to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha_orden, " +
											  ""+cadenaHoraOrden+" AS hora_orden," +
											  "to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, " +
											  ""+cadenaHoraGrabacion+" AS hora_grabacion," +
											  "per.primer_nombre || ' ' || per.segundo_nombre ||' '||  per.primer_apellido ||' '|| per.segundo_apellido as medico " +
											  "FROM orden_nutricion_oral ono " +
											  "INNER JOIN nutricion_oral_cc_inst noci ON (ono.nutricion_oral_cc_inst=noci.codigo)  " +
											  "INNER JOIN tipo_nutricion_oral tno ON (noci.nutricion_oral=tno.codigo) " +
											  "INNER JOIN orden_dieta od ON  (od.codigo_histo_enca=ono.codigo_historico_dieta)  " +
											  "INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo =  od.codigo_histo_enca)  " +
											  "INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
											  "INNER JOIN usuarios us ON  (enc.login=us.login) " +
											  "INNER JOIN personas per ON (us.codigo_persona=per.codigo) "); 
	   */								  
			  if(cuentaAsocio != 0)
			  	consultaBuffer.append(" WHERE om.cuenta IN (?, ?) ");
			  else
			  	consultaBuffer.append(" WHERE om.cuenta = ? ");

			  consultaBuffer.append( " AND noci.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
										  " UNION ALL " +
										  "SELECT od.codigo_histo_enca AS codigo, od.via_oral as via_oral, od.finalizar_dieta as finalizar_dieta, ono.descripcion as nutricion_oral, " +
										  "to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha_orden, "+cadenaHoraOrden+" AS hora_orden," +
										  "to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, "+cadenaHoraGrabacion+" AS hora_grabacion," +
										  "per.primer_nombre || ' ' || per.segundo_nombre ||' '||  per.primer_apellido ||' '|| per.segundo_apellido as medico " +
										  "FROM detalle_otro_nutri_oral don INNER JOIN otro_nutricion_oral ono ON (ono.codigo=don.otro_nutricion_oral) " +
										  "INNER JOIN orden_dieta od ON (od.codigo_histo_enca=don.codigo_historico_dieta)  " +
										  "INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo =  od.codigo_histo_enca)  " +
										  "INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
										  "INNER JOIN usuarios us ON  (enc.login=us.login) " +
										  "INNER JOIN personas per ON (us.codigo_persona=per.codigo) ");
			 
			 if(cuentaAsocio != 0)
			 	consultaBuffer.append(" WHERE om.cuenta IN (?, ?) ");
			 else
			 	consultaBuffer.append(" WHERE om.cuenta = ? ");
			 
			 consultaBuffer.append(" UNION SELECT od.codigo_histo_enca AS codigo, od.via_oral as via_oral, od.finalizar_dieta as finalizar_dieta, '' as nutricion_oral, " +
			 															"to_char(enc.fecha_orden, 'DD/MM/YYYY') as fecha_orden, "+cadenaHoraOrden+" AS hora_orden," +
			 															"to_char(enc.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, "+cadenaHoraGrabacion+" AS hora_grabacion," +
			 															"per.primer_nombre || ' ' || per.segundo_nombre ||' '||  per.primer_apellido ||' '|| per.segundo_apellido as medico " +
			 															"FROM orden_dieta od INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo =  od.codigo_histo_enca) " +
			 															"INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
			 															"INNER JOIN usuarios us ON  (enc.login=us.login) " +
			 															"INNER JOIN personas per ON (us.codigo_persona=per.codigo)  " +
																		"WHERE od.finalizar_dieta=" + ValoresPorDefecto.getValorTrueParaConsultas());
																		
			 if(cuentaAsocio != 0)
			 	consultaBuffer.append(" AND om.cuenta IN (?, ?))x ");
			 else
			 	consultaBuffer.append(" AND om.cuenta = ?)x ");
			 
			 consultaBuffer.append(" ORDER BY fecha_grabacion DESC, hora_grabacion DESC");
			 
		
		try
		{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			if (cuentaAsocio != 0)
			{
				consultarNov.setInt(1, codigoCuenta);
				consultarNov.setInt(2, cuentaAsocio);
				consultarNov.setInt(3, codigoCuenta);
				consultarNov.setInt(4, cuentaAsocio);
				consultarNov.setInt(5, codigoCuenta);
				consultarNov.setInt(6, cuentaAsocio);
				logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString());
				logger.info("codigo cuenta: "+codigoCuenta);
				logger.info("codigo cuenta: "+cuentaAsocio);
			}
			else
			{
				consultarNov.setInt(1, codigoCuenta);
				consultarNov.setInt(2, codigoCuenta);
				consultarNov.setInt(3, codigoCuenta);
				logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString());
				logger.info("codigo cuenta: "+codigoCuenta);
			}
			
 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Historicos Nutricion Oral :"+e.toString());
			return null;
		}
	}
	
	
	 
		/**
		 * Metodo para consultar los historicos de cuidados de enfermeria 
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @param fechaInicial 
		 * @param fechaFinal 
		 * @param consultaBuffer TODO
		 * @return
		 */
		
		public static Collection consultarCuidadosEnfHisto(Connection con, int codigoCuenta, int cuentaAsocio, int institucion, int centroCosto, String fechaInicial, String fechaFinal, StringBuffer consultaBuffer)
		{



			try
			{
				
				logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString());
				logger.info("cuenta       -->"+codigoCuenta);
				logger.info("cuentaAsocio -->"+cuentaAsocio);
				logger.info("\n\n\n\n\n\n\n");
				
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				if (cuentaAsocio != 0)
				{
					consultarNov.setInt(1, codigoCuenta);
					consultarNov.setInt(2, cuentaAsocio);
					consultarNov.setInt(3, codigoCuenta);
					consultarNov.setInt(4, cuentaAsocio);
					//consultarNov.setInt(2, institucion);
					//consultarNov.setInt(3, ConstantesBD.codigoViaIngresoUrgencias);
					//consultarNov.setInt(4, centroCosto);
				}
				else
				{
					consultarNov.setInt(1, codigoCuenta);
					consultarNov.setInt(2, codigoCuenta);
				}

	 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
				logger.error("Error Consultado Historicos Cuidado Enfermeria :"+e.toString());
				return null;
			}
		}
		
		/**
	     * Metodo para consultar el tipo de monitoreo para una orden m�dica
	     * @param con
	     * @param codigoOrden
	     * @return Collection con los datos del tipo de Monitoreo
		 */
		public static Collection cargarTipoMonitoreo(Connection con, int codigoCuenta)
		{
			String  consultaStr =  " SELECT tm.codigo FROM tipo_monitoreo tm " +
								   "		  	     INNER JOIN orden_tipo_monitoreo otmon ON (otmon.tipo_monitoreo = tm.codigo) "+ 
								   "		  	     INNER JOIN (SELECT MAX(otm.codigo_histo_encabezado) as codmax "+
								   "							  FROM orden_tipo_monitoreo otm "+
								   "				 			  INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = otm.codigo_histo_encabezado) " +
								   "							  INNER JOIN ordenes_medicas om ON (enc.orden_medica = om.codigo) "+
								   "							  WHERE om.cuenta = ? ) tabla ON (tabla.codmax = otmon.codigo_histo_encabezado) ";

			try
			{
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
				consultarNov.setInt(1, codigoCuenta);

				//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
	 		    return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
				logger.error("Error Consultado el Ultimo Tipo de Monitoreo :"+e.toString());
				return null;
			}
		}
		
		 /**
	     * Metodo para consultar y cargar la informaci�n en la secci�n soporte respiratorio 
	     * @param con
	     * @param codigoCuenta
	     * @param asocio
	     * @param institucion
	     * @param centroCosto
	     * @return Collection con los datos del soporte respiratorio
	     */
	    public static Collection cargarSoporteRespiratorio(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto)
	    {
	    	String  consultaStr="";
	    	if (!asocio)
	    	{
	    	consultaStr ="SELECT os.codigo_histo_enca AS codigo_histo_enca, os.cantidad AS cantidad,os.equipo_elemento_cc_inst AS equipo_elemento,os.oxigeno_terapia AS oxigeno_terapia, os.descripcion AS descripcion " +
                                                    " FROM orden_soporte_respira os " +
                                                    "           WHERE os.codigo_histo_enca = " +
                                                    "			(SELECT MAX (codigo_histo_enca) as codigo_histo FROM orden_soporte_respira os " +
                                                    "			INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=os.codigo_histo_enca)" +
                                                    "			 INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
                                                    "			WHERE om.cuenta=?)";
	    	}
	    	else
	    	{
	    		consultaStr ="SELECT os.codigo_histo_enca AS codigo_histo_enca, os.cantidad AS cantidad, getCodigoParamSoporCCIns(?, ?, ?, os.equipo_elemento_cc_inst) AS equipo_elemento,os.oxigeno_terapia AS oxigeno_terapia, os.descriopcion AS descripcion " +
                " FROM orden_soporte_respira os " +
                "           WHERE os.codigo_histo_enca = " +
                "			(SELECT MAX (codigo_histo_enca) as codigo_histo FROM orden_soporte_respira os " +
                "			INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=os.codigo_histo_enca)" +
                "			 INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
                "			WHERE om.cuenta=?)";
	    	}

			try
				{
				
				//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				if (!asocio)
					consultarNov.setInt(1, codigoCuenta);
				else
				{
					consultarNov.setInt(1, institucion);
					consultarNov.setInt(2, ConstantesBD.codigoViaIngresoUrgencias);
					consultarNov.setInt(3, centroCosto);
					consultarNov.setInt(4, codigoCuenta);
				}
					
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
				} 
			catch (SQLException e)
				{
				logger.error("Error Consultado el Soporte respiratorio de la Orden Medica :"+e.toString());
				return null;
				}
		}
	    
	    /**
	     * Metodo para consultar y cargar la informaci�n de la dieta
	     * @param con
	     * @param codigoCuenta
	     * @return Collection con los datos de la �ltima dieta
	     */
	    public static Collection cargarDieta(Connection con, int codigoCuenta)
	    {
	    	String consultaStr = "SELECT od.codigo_histo_enca AS codigo_histo_dieta, od.via_oral AS nutricion_oral, "+
	    										    "od.via_parenteral AS nutricion_parenteral, od.finalizar_dieta AS finalizar_dieta," +
	    										    "od.mezcla AS mezcla, od.suspendido_enfermeria AS suspendido_enfermeria " +
	    										    "FROM orden_dieta od " +
	    										    " WHERE od.codigo_histo_enca = " +
	    										    "	(SELECT MAX (codigo_histo_enca) as codigo_histo FROM orden_dieta od " +
                                                    "		INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca)" +
                                                    "	     INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
                                                    "			WHERE om.cuenta=?)";
	    	
	    	
			try
			{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoCuenta);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado la �ltima dieta de la Orden Medica :"+e.toString());
			return null;
			}    	
	    }
	    
	    /**
	     * Metodo para consultar y cargar la informaci�n de la Nutrici�n Oral
	     * @param con
	     * @param codigoCuenta
	     * @param asocio -> true si es de una cuenta de asocio sino es false
	     * @param institucion -> Intituci�n del m�dico
	     * @param centroCosto -> Centro de costo con el que se hizo el asocio de cuentas
	     * @return Collection con los datos de la �ltima nutrici�n oral
	     */
	    public static Collection cargarNutricionOral(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto)
	    {
	    	/*String  consultaStr =  "SELECT nutricion_oral_cc_inst AS nutricion_oral FROM orden_nutricion_oral " +
	    												" WHERE codigo_historico_dieta=?";*/
	    	
	    	String consultaStr="";
	    	
	    	//---------------Si no se trata de una cuenta de asocio, se realiza una consulta normal--------------//
	    	if (!asocio)
	    	{
	    	consultaStr = "SELECT nutricion_oral_cc_inst AS codigo, '' AS descripcion FROM orden_nutricion_oral " +
	    											"WHERE codigo_historico_dieta=(SELECT MAX (codigo_historico_dieta) as codigo_histo FROM orden_nutricion_oral ono " +
	    											"INNER JOIN orden_dieta od ON (ono.codigo_historico_dieta=od.codigo_histo_enca) " +
	    											"INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca)  " +
	    											"INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
	    											"WHERE om.cuenta=?)";
	    	}
	    	//-------------Cuando se trata de un asocio de cuentas se obtiene el c�digo equivalente de una parametrizaci�n a otra------------//
	    	else
	    	{
	    		consultaStr = "SELECT getcodigoparamnutorccins(?, ?, ?, nutricion_oral_cc_inst)  AS codigo, '' AS descripcion FROM orden_nutricion_oral " +
										"WHERE codigo_historico_dieta=(SELECT MAX (codigo_historico_dieta) as codigo_histo FROM orden_nutricion_oral ono " +
										"INNER JOIN orden_dieta od ON (ono.codigo_historico_dieta=od.codigo_histo_enca) " +
										"INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca)  " +
										"INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
										"WHERE om.cuenta=?)";
	    	}

			
			try
			{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//logger.info("\n\n\n\n\n\n\n\n CARGAR NUTRICION ORAL--->"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			if (!asocio)
				consultarNov.setInt(1, codigoCuenta);
			else
	    	{
				consultarNov.setInt(1, institucion);
				consultarNov.setInt(2, ConstantesBD.codigoViaIngresoUrgencias);
				consultarNov.setInt(3, centroCosto);
				consultarNov.setInt(4, codigoCuenta);
	    	}
						
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado la �ltima nutrici�n oral de la Orden Medica :"+e.toString());
			return null;
			}    	
	    }
	    
	    /**
		 * Metodo para insertar otra nutrici�n oral
		 * @param con
		 * @param secuencia
		 * @param otroNutOral
		  * @return codigo
		 */
		public static int insertarOtroNutricionOral(Connection con, String secuencia, String otroNutOral)
		{
			PreparedStatementDecorator ps;
			int resp=0, codigo=0;
		
			String insertarStr = 	"	INSERT INTO otro_nutricion_oral (codigo, " +
																		"descripcion) " +
																		" VALUES " +
																		" (?, ?)";
			
				
			try	{
							codigo = obtenerCodigoOrden (con, secuencia);
							ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
											
							ps.setInt(1, codigo);
							ps.setString(2, otroNutOral);
												
							resp = ps.executeUpdate();
													
							if (resp > 0)
							{
								resp = codigo;
							}
								
					}
					catch(SQLException e)
					{
							logger.warn(e+" Error en la inserci�n de datos en Otra Nutrici�n Oral : insertarOtroNutricionOral "+e.toString() );
							resp = 0;
					}
					return resp;
		}
		
		/**
		 * Metodo para insertar el detalle de otro nutrici�n oral
		 * @param con
		 * @param codigoDieta
		 * @param codigoOtroOral
		  * @return
		 **/
		public static int insertarDetalleOtroNutriOral(Connection con, int codigoDieta, int codigoOtroOral)
		{
			PreparedStatementDecorator ps;
			int resp=0;
		
			String insertarStr = 	"INSERT INTO detalle_otro_nutri_oral(codigo_historico_dieta, " +
																														"otro_nutricion_oral) " +
																														" VALUES " +
																														" (?, ?) ";
			
			
			try	{
							ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
											
							ps.setInt(1, codigoDieta);
							ps.setInt(2, codigoOtroOral);
													
							resp = ps.executeUpdate();
							
					}
					catch(SQLException e)
					{
							logger.warn(e+" Error en la inserci�n de datos en el detalle de otra nutrici�n oral : insertarDetalleOtroNutriOral "+e.toString() );
							resp = 0;
					}
					return resp;
		}
		
		/**
	 	 * Funcion que retorna una collecion con el listado de los otros tipos de nutrici�n oral a una cuenta espec�fica
	 	 * @param con
	 	 * @param cuenta
	 	 * @param cuentaAsocio
	 	 * @return Collection 
	 	 */
	      public static Collection consultarOtrosNutricionOral(Connection con, int cuenta, int cuentaAsocio)
	      {
	      	StringBuffer consultaBuffer=new StringBuffer();
	      	
	      	consultaBuffer.append("SELECT ono.codigo AS codigo, ono.descripcion AS descripcion FROM otro_nutricion_oral ono " +
												  "INNER JOIN detalle_otro_nutri_oral dno ON (dno.otro_nutricion_oral=ono.codigo) " +
												  "INNER JOIN orden_dieta od ON (od.codigo_histo_enca=dno.codigo_historico_dieta) " +
												  "INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca) " +
												  "INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) ");
	      	
	      	if (cuentaAsocio != 0)
	      		consultaBuffer.append(" WHERE om.cuenta IN (?, ?) ");     		
	      	else
	      		consultaBuffer.append(" WHERE om.cuenta=? ");
	      		
	      	
	      	consultaBuffer.append(" GROUP BY ono.codigo, ono.descripcion "+
												  			" ORDER BY ono.codigo")
															; 
			try
			{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", cuenta+"")+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consultarNov.setInt(1, cuenta);
			
			if (cuentaAsocio != 0)
				consultarNov.setInt(2, cuentaAsocio);
					
				
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado otros tipos de nutrici�n oral :"+e.toString());
			return null;
			}	      	
	      }
	      
	      /**
	       * Metodo para consultar y cargar la informaci�n de otros tipos de nutrici�n oral ingresados
	       * @param con
	       * @param codigoCuenta
	       * @return Collection con los datos de los �ltimos tipos de nutrici�n oral ingresados
	       */
	      public static Collection cargarOtroNutricionOral(Connection con, int codigoCuenta)
	      {
		      	/*String consultaStr = "SELECT otro_nutricion_oral AS nutricion_oral " +
		      											"FROM detalle_otro_nutri_oral WHERE codigo_historico_dieta=" +
		      											"(SELECT MAX (codigo_historico_dieta) as codigo_histo FROM detalle_otro_nutri_oral don " +
		      											"INNER JOIN orden_dieta od ON (don.codigo_historico_dieta=od.codigo_histo_enca) " +
		      											"INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca)  " +
		      											"INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica) " +
		      												"WHERE om.cuenta=?)";*/
	      	
	      	String consultaStr = "SELECT otro_nutricion_oral AS nutricion_oral " +
				"FROM detalle_otro_nutri_oral WHERE codigo_historico_dieta=" +
				"(SELECT MAX (codigo_historico_dieta) as codigo_histo FROM orden_nutricion_oral ono " +
	    											"INNER JOIN orden_dieta od ON (ono.codigo_historico_dieta=od.codigo_histo_enca) " +
	    											"INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca)  " +
	    											"INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
	    											"WHERE om.cuenta=?)";
	      	
	
				
				try
				{
				logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
				consultarNov.setInt(1, codigoCuenta);
								
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
				} 
				catch (SQLException e)
				{
				logger.error("Error Consultado los �ltimos tipos de nutrici�n oral ingresados a la Orden Medica :"+e.toString());
				return null;
				}    	
	      }
	      
	      /**
	       * Metodo para cargar la informaci�n ingresada en la cuenta de asocio, referente a descripci�n del soporte, dieta
	       * y observaciones generales 
	       * @param con
	       * @param cuentaAsocio
	       * @return Collection con los datos de la cuenta de asocio
	       */
	      
	      public static Collection cargarDatosUrgencias(Connection con, int cuentaAsocio)
	      {
	      	String consultaStr = "SELECT  om.codigo as codigo, om.observaciones_generales as observaciones_generales, " +
												 "        om.descripcion_soporte as descripcion_soporte, om.descripcion_dieta_oral as descripcion_dieta_oral " +
												 "		  FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eh ON (eh.orden_medica=om.codigo) " +
												 "			   WHERE eh.codigo=  (SELECT MAX(eh.codigo) FROM encabezado_histo_orden_m eh " +
												 								" INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
																				" WHERE om.cuenta=?)";

			
			try
			{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", cuentaAsocio+"")+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, cuentaAsocio);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado los datos de urgencias de cuenta de asocio :"+e.toString());
			return null;
			}
	      }
	      
	      /**
	     	 * Funcion que retorna una collecion con el listado de los otros tipos de cuidados de enfermer�a  a una(s) cuenta(s) espec�fica
	     	 * @param con
	     	 * @param cuenta
	     	 * @param cuentaAsocio
	     	 * @return Collection 
	     	 */
	          public static Collection consultarOtrosCuidadosEnfer(Connection con, int cuenta, int cuentaAsocio )
	          {
	          	StringBuffer consultaBuffer=new StringBuffer();
		      	
		      	consultaBuffer.append("SELECT doci.cod_histo_cuidado_enfer AS codigo, doci.otro_cual AS descripcion " +
					      									"FROM detalle_otro_cuidado_enf doci " +
															"INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=doci.cod_histo_cuidado_enfer) " +
					      									"INNER JOIN ordenes_medicas om ON  (eh.orden_medica=om.codigo) " );
					      									      	
		      	if (cuentaAsocio != 0)
		      		consultaBuffer.append(" WHERE om.cuenta IN (?, ?) ");     		
		      	else
		      		consultaBuffer.append(" WHERE om.cuenta=? ");
		      		
		      	
		      	consultaBuffer.append(" GROUP BY doci.cod_histo_cuidado_enfer,doci.otro_cual "+
													  			" ORDER BY doci.cod_histo_cuidado_enfer"); 
				try
				{
				
				//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", cuenta+"")+"\n\n\n\n\n\n\n");
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				consultarNov.setInt(1, cuenta);
				
				if (cuentaAsocio != 0)
					consultarNov.setInt(2, cuentaAsocio);
						
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
				} 
				catch (SQLException e)
				{
				logger.error("Error Consultado otros tipos de cuidados de enfermeria :"+e.toString());
				return null;
				}	      	
	          }
	          
       /**
    	 *  Metodo para insertar otro tipo de cuidado de enfermer�a
    	 * @param con
    	 * @param otroCuidadoEnf
    	 * @param secuencia
    	  * @return codigo
    	 **/
    	public static int insertarOtroTipoCuidadoEnf(Connection con, String otroCuidadoEnf)
    	{
    		PreparedStatementDecorator ps;
			int resp=0, codigo=0;
		
			String insertarStr = 	"	INSERT INTO tipo_otro_cuidado_enf (codigo, " +
	    																		"descripcion) " +
	    																		" VALUES " +
	    																		" (?, ?)";
			
				
			try	{
						codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "enfermeria.seq_cuidado_enfer_cc_inst");
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										
						ps.setInt(1, codigo);
						ps.setString(2, otroCuidadoEnf);
											
						resp = ps.executeUpdate();
												
						if (resp > 0)
						{
							resp = codigo;
						}
					}
					catch(SQLException e)
					{
						logger.warn(e+" Error en la inserci�n de datos en Otra Cuidado Enfermeria : insertarOtroTipoCuidadoEnf "+e.toString() );
						resp = 0;
					}
					return resp;
    	}
    	
     	/**
         * Metodo para cargar la informaci�n de la orden de la hoja
         * neurol�gica
         * @param con
         * @param codigoOrden
         * @return true si existe orden hoja neurol�gica
         */
    	public static Collection cargarOrdenHojaNeurologica(Connection con, int codigoOrden)
    	{
    		String  consultaStr =  "SELECT presenta AS presenta, observaciones AS observaciones, finalizada AS finalizada " +
    															" FROM hoja_neurologica_orden_m WHERE orden_medica=?";

			try
			{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoOrden);
			
			//logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoOrden+"")+"\n\n\n\n\n\n\n");
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado la orden de la hoja neurol�gica :"+e.toString());
			return null;
			}
    	}
    	
     	/**
    	 * M�todo que inserta o modifica la orden de la Hoja Neurol�gica
    	 * @param con
     	 * @param codigoOrden
     	 * @param presenta
     	 * @param observaciones
     	 * @param finalizada
     	 * @param fechaFin @todo
     	 * @param horaFin @todo
     	 * @param login @todo
     	 * @return
    	 */
    	public static int insertarModificarOrdenHojaNeurologica(Connection con, int codigoOrden, boolean presenta, String observaciones, boolean finalizada, String fechaFin, String horaFin, String login)
    	{
    		PreparedStatementDecorator ps;
    		int resp=0, codigo=0;
    		String consultaStr = "";
    						
    		try
    			{
    				codigo = Utilidades.nroRegistrosConsulta(con, "SELECT * FROM hoja_neurologica_orden_m WHERE orden_medica="+codigoOrden); 
    				if ( codigo > 0   )  
    				{  
    					 //-Si existe se modifica
    					consultaStr = " UPDATE hoja_neurologica_orden_m SET presenta=?, observaciones=?, finalizada=?, fecha_fin=?, hora_fin=?, fecha_grabacion=CURRENT_DATE, hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", login=? "+
    											" 	WHERE orden_medica=?";
    					
    					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    					
    					ps.setBoolean(1, presenta);
    					ps.setString(2, observaciones);					
    					ps.setBoolean(3,finalizada);
    					if(!finalizada)
    					{
    						ps.setNull(4, Types.DATE);
    						ps.setNull(5, Types.TIME);
    					}
    					else
    					{
    						ps.setString(4, fechaFin);
    						ps.setString(5, horaFin);
    					}
    					ps.setString(6, login);
    					ps.setInt(7, codigoOrden);
    				}
    				else
    				{
    					
    					consultaStr = " INSERT INTO hoja_neurologica_orden_m (orden_medica,presenta,observaciones,finalizada, fecha_fin, hora_fin, fecha_grabacion, hora_grabacion, login) " +
    														"VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?) ";
    					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    					
    					ps.setInt(1, codigoOrden);				
    					ps.setBoolean(2, presenta);								
    					ps.setString(3, observaciones);
    					ps.setBoolean(4,finalizada);
    					if(!finalizada)
    					{
    						ps.setNull(5, Types.DATE);
    						ps.setNull(6, Types.TIME);
    					}
    					else
    					{
    						ps.setString(5, fechaFin);
    						ps.setString(6, horaFin);
    					}
    					ps.setString(7, login);
    					
    				}
    				
    				
    				resp = ps.executeUpdate();
    				
    				if (resp > 0)
    				{
    					resp = codigoOrden;
    				}
    						
    			}
    			catch(SQLException e)
    			{
    					logger.warn(e+" Error en la inserci�n/modificaci�n de datos Orden Hoja Neurol�gica : SqlBaseOrdenMedicaDao "+e.toString() );
    					resp = 0;
    			}
    			return resp;
    	}
    	
        /**
         * M�todo que consulta las mezclas parenterales y los articulos con su correspondiente
         * informaci�n de detalle
         * @param con
         * @param codigoCuenta
         * @param codigoCuentaAsocio
         * @param nroConsulta 
         * @return
         */
    	public static Collection consultarMezclasParenteral(Connection con, int codigoCuenta, int codigoCuentaAsocio, int nroConsulta)
    	{
    		StringBuffer consultaBuffer=new StringBuffer();
	      	
    		switch(nroConsulta)
 			{
    		 	//------Se consultan la mezclas finalizadas en parenteral ----------------------//
 		        case 1:
 		        	 		consultaBuffer.append("SELECT od.codigo_histo_enca AS codigo_histo, to_char(ehom.fecha_grabacion, 'DD/MM/YYYY') AS fecha_finalizacion, " +
 		        	 																	"ehom.hora_grabacion AS hora_finalizacion, " +
 		        	 																	"mez.nombre || '-' || tm.nombre AS nombre_mezcla," +
 		        	 																	"od.mezcla AS codigo_mezcla, " +
 		        	 																	"getdatosmedico(ehom.login) AS medico, " +
 		        	 																	"getespecialidadesmedico(ehom.login, ' - ') AS especialidades_medico ,"+
 		        	 																	"getUltimoParenteFinalizado(od.codigo_histo_enca) AS anterior " +
 		        	 														"FROM orden_dieta od " +
 		        	 															"INNER JOIN encabezado_histo_orden_m ehom ON (ehom.codigo=od.codigo_histo_enca) " +
 		        	 															"INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo) " +
 		        	 															"INNER JOIN mezcla mez ON (od.mezcla=mez.consecutivo) " +
 		        	 															"INNER JOIN tipo_mezcla tm ON (mez.cod_tipo_mezcla=tm.codigo) ");
 		        	 															 		        	 		
 		        	 		if (codigoCuentaAsocio != 0)
 		       	      			consultaBuffer.append(" WHERE om.cuenta IN ("+codigoCuenta+", "+codigoCuentaAsocio+") ");     		
 		        	 		else
 		       	      			consultaBuffer.append(" WHERE om.cuenta="+codigoCuenta);
 		        	 		
 		        	 		//esta sentencia indica que la mezcla ya fue finalizada en el registro de enfermeria
 		        	 		//se cambio la metodologia en suspendido por finalizado hace algun tiempo. consultaBuffer.append(" AND od.suspendido="+ValoresPorDefecto.getValorTrueParaConsultas());
 		        	 		consultaBuffer.append(" AND od.finaliza_sol="+ValoresPorDefecto.getValorTrueParaConsultas());
 		        	 		
 		        	 		consultaBuffer.append(" ORDER BY ehom.fecha_grabacion || '-' || ehom.hora_grabacion");
 		        	 		
 		        	 		break;
 		       case 2:
        	 		consultaBuffer.append(" ");
        	 		break;
 		        	 		
 		       default :
				{
					logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-2] El valor recibido es "+ nroConsulta + "\n\n" );
					return null;
				}
 			}//switch
	      	
    		 
	      	try
			{
	      	
	      	logger.info("\n\n\n\n\n\n\n\n consulta de consultarMezclasParenteral -->"+consultaBuffer.toString()+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado consultarMezclasParenteral NroConsulta ->"+nroConsulta+" SqlBaseOrdenMedicaDao :"+e.toString());
			return null;
			}	      	
    	}
    	
    	  /**
         * M�todo que consulta la informaci�n de los articulos asociados a la mezcla en el ver anteriores
         * @param con
         * @param codigoCuenta
         * @param codigoCuentaAsocio
         * @param codMezcla
         * @param codEncaMin
         * @param codEncabezadoAnterior
         * @return
         */
    	public static Collection consultarDetalleMezclaAnteriores(Connection con, int codigoCuenta, int codigoCuentaAsocio, int codMezcla, int codEncaMin, int codEncabezadoAnterior)
    	{
    		
    		StringBuffer consultaStr = new StringBuffer();
    		
    		consultaStr.append("SELECT " +
    						   "to_char(enc.fecha_grabacion, 'DD/MM/YYYY') || '-' || enc.hora_grabacion AS fecha_grabacion," +
    						   "onp.codigo_historico_dieta as coddieta, " +
    						   "art.codigo as codnut, " +
    						   "onp.volumen as volumen, " +
    						   "coalesce(od.volumen_total,'') as volumen_total," +
    						   "coalesce(od.velocidad_infusion,'') as velocidad_infusion, " +
    						   "sol.consecutivo_ordenes_medicas AS numero_orden," +    															
    						   "coalesce(onp.unidad_volumen,'') AS unidad_volumen, "+
    						   "coalesce(od.unidad_volumen_total,'') AS unidad_volumen_total, "+
    						   "coalesce(od.dosificacion, '') as dosificacion " +
    						   "	FROM orden_nutricion_parente onp "+
    						   "		INNER JOIN articulo art ON (art.codigo=onp.articulo) "+
    						   "		INNER JOIN orden_dieta od ON (od.codigo_histo_enca=onp.codigo_historico_dieta) "+
    						   "		INNER JOIN encabezado_histo_orden_m enc ON (enc.codigo = od.codigo_histo_enca) "+
    						   "		INNER JOIN ordenes_medicas om ON (enc.orden_medica=om.codigo) " +
    						   "		INNER JOIN solicitudes_medicamentos sm ON (od.codigo_histo_enca=sm.orden_dieta) " +
    						   "		INNER JOIN solicitudes sol ON (sol.numero_solicitud=sm.numero_solicitud) "+
    						   "		INNER JOIN "+
    						   "		( "+
    						   "				SELECT articulo "+
    						   "				FROM articulos_por_mezcla apm "+
    						   "				WHERE mezcla="+codMezcla +
    						   "			UNION "+
    						   "				SELECT articulo "+
    						   "				FROM orden_nutricion_parente n "+
    						   "				INNER JOIN orden_dieta o ON (codigo_historico_dieta=codigo_histo_enca) "+
    						   "				WHERE "+
    						   "					mezcla="+codMezcla+
    						   "					AND articulo NOT IN "+
    						   "					( "+
    						   "						SELECT articulo "+
    						   "						FROM articulos_por_mezcla "+
    						   "						WHERE mezcla="+codMezcla+
    						   "					)"+
    						   "				GROUP BY articulo "+
    						   "		) articulos_sin_asocio ON(articulos_sin_asocio.articulo=art.codigo) ");
														
								if(codigoCuentaAsocio!= 0)	
									consultaStr.append(" WHERE om.cuenta IN("+codigoCuenta+","+codigoCuentaAsocio+")");
								else
									consultaStr.append(" WHERE om.cuenta ="+codigoCuenta+"");
										
								consultaStr.append(" AND od.codigo_histo_enca BETWEEN "+codEncaMin+" AND "+codEncabezadoAnterior+
																	" AND od.mezcla="+codMezcla+" AND od.finaliza_sol="+ValoresPorDefecto.getValorFalseParaConsultas()+
																						" ORDER BY enc.fecha_grabacion ASC, enc.hora_grabacion ASC");

			try
			{
			//logger.info("\n\n\n\n\n\n\n\n"+consultaStr+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
			catch (SQLException e)
			{
			logger.error("Error Consultado el detalle de la mezcla finalizada: SqlBaseOrdenMedicaDao "+e.toString());
			return null;
			}
    	}

	/**
	 * @param con
	 * @param codHistoricosParent
	 * @return
	 */
	public static int finalizarParenteral(Connection con, Vector codHistoricosParent)
	{
		String consulta="UPDATE orden_dieta SET via_parenteral="+ValoresPorDefecto.getValorTrueParaConsultas()+" WHERE codigo_histo_enca=?";
		int numResultados=0;
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(int i=0; i<codHistoricosParent.size();i++)
			{
				stm.setInt(1, (Integer)codHistoricosParent.elementAt(i));
				numResultados+=stm.executeUpdate();
			}
			return numResultados;
		}
		catch (SQLException e)
		{
			logger.error("Error finalizando la dieta "+e);
		}
		return -1;
	}

	/**
	 * 
	 * @param con 
	 * @param orden
	 * @param solicitud
	 * @return
	 */
	public static HashMap consultarMezclaModificar(Connection con, String orden, String solicitud)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadenaConsultaMezcla="SELECT " +
											" codigo_histo_enca as codigoencabezado," +
											" od.mezcla as codigomezcla," +
											" m.nombre as nombremezcla," +
											" od.farmacia as codigofarmacia," +
											" getnomcentrocosto(od.farmacia) as nombrefarmacia," +
											" coalesce(volumen_total,'') as volumentotal," +
											" coalesce(velocidad_infusion,'') as velocidadinfusion," +
											" coalesce(dosificacion, '') as dosificacion," +
											" coalesce(unidad_volumen_total,'') AS unidadvolumentotal " +										 
									" from orden_dieta od " +
									" inner join mezcla m on(m.consecutivo=od.mezcla) " +																		
									" where codigo_histo_enca=?";
		
		String cadenaConsultaDetalleMezcla= " SELECT articulo     ,"+
												  "nombrearticulo     ,"+
												  "volumen            ,"+
												  "volumenold         ,"+
												  "tiporegistro       ,"+
												  "unidad_volumen     ,"+
												  "codigojustificacion,"+
												  "espos              ,"+
												  "esmedicamento " +
											" FROM " +
											"(" +
												" SELECT " +
												" onp.articulo," +
												" getdescarticulosincodigo(onp.articulo) as nombrearticulo," +
												" volumen||'' as volumen," +
												" volumen||'' as volumenold," +
												" 'BD' as tiporegistro," +
												" coalesce(onp.unidad_volumen,'') AS unidad_volumen," +
												" CASE WHEN jus.codigo IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE jus.codigo END AS codigojustificacion," +
												" CASE WHEN getespos(onp.articulo) = '1' THEN 'true' ELSE 'false' END AS espos," +
												" CASE WHEN getesmedicamento(onp.articulo) = '1' THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS esmedicamento "+
												" from orden_nutricion_parente onp " +
												" LEFT OUTER JOIN justificacion_art_sol jus ON (jus.numero_solicitud = ? AND jus.articulo = onp.articulo) " +
												" where onp.codigo_historico_dieta = ? " +
												
												" UNION " +
												
												" SELECT " +
												" onp.articulo," +
												" getdescarticulosincodigo(onp.articulo) as nombrearticulo," +
												" '' as volumen," +
												" '' as volumenold," +
												" 'MEM' as tiporegistro, " +
												" coalesce(onp.unidad_volumen,'') AS unidad_volumen, "+
												" CASE WHEN jus.codigo IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE jus.codigo END AS codigojustificacion, "+
												" CASE WHEN getespos(onp.articulo) = '1' THEN 'true' ELSE 'false' END AS espos, "+
												" CASE WHEN getesmedicamento(onp.articulo) = '1' THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS esmedicamento "+
												" from encabezado_histo_orden_m ehom " +
												" inner join orden_dieta od on(od.codigo_histo_enca=ehom.codigo) " +
												" left outer join orden_nutricion_parente onp on (onp.codigo_historico_dieta=ehom.codigo) " +
												" LEFT OUTER JOIN justificacion_art_sol jus ON (jus.numero_solicitud = ? AND jus.articulo = onp.articulo) " +
												" where onp.codigo_historico_dieta !=? and " +
												" orden_medica = (select orden_medica from encabezado_histo_orden_m where codigo=?) and " +
												" onp.articulo NOT IN (select ordenp.articulo from orden_nutricion_parente ordenp where ordenp.codigo_historico_dieta = ?) and " +
												" od.mezcla=?" +
											")tabla" +
											" GROUP BY tabla.articulo     ,"+
											  "tabla.nombrearticulo     ,"+
											  "tabla.volumen            ,"+
											  "tabla.volumenold         ,"+
											  "tabla.tiporegistro       ,"+
											  "tabla.unidad_volumen     ,"+
											  "tabla.codigojustificacion,"+
											  "tabla.espos              ,"+
											  "tabla.esmedicamento";
			
		PreparedStatementDecorator ps;
		try
		{
			logger.info((cadenaConsultaMezcla+"").replace("?", orden));
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaMezcla,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, orden);
			
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			logger.info("valor del detalle >> "+cadenaConsultaDetalleMezcla);
			
			while (rs.next())
			{				
				//numeroMezcla
				logger.info("solicitud "+solicitud);
				logger.info("codigoencabezado "+rs.getString("codigoencabezado"));
				logger.info("codigomezcla "+rs.getString("codigomezcla"));
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleMezcla,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, solicitud);
				ps.setString(2, rs.getString("codigoencabezado"));
				ps.setString(3, solicitud);
				ps.setString(4, rs.getString("codigoencabezado"));
				ps.setString(5, rs.getString("codigoencabezado"));
				ps.setString(6, rs.getString("codigoencabezado"));
				ps.setString(7, rs.getString("codigomezcla"));
				
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				mapa.put("codigoEncabezado", rs.getString("codigoencabezado"));
				mapa.put("codigoMezcla", rs.getString("codigomezcla"));
				mapa.put("nombreMezcla", rs.getString("nombremezcla"));
				mapa.put("codigoFarmacia", rs.getString("codigofarmacia"));
				mapa.put("nombreFarmacia", rs.getString("nombrefarmacia"));
				mapa.put("volumenTotal", rs.getString("volumentotal"));
				mapa.put("unidadVolumenTotal", rs.getString("unidadvolumentotal"));
				mapa.put("velocidadInfusion", rs.getString("velocidadinfusion"));
				mapa.put("dosificacion", rs.getString("dosificacion"));
			}
			rs.close();
			ps.close();
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
	 * @param mezclaModificar
	 * @return
	 */
	public static boolean guardarModificacionMezcla(Connection con, HashMap mapa, UsuarioBasico medico, PersonaBasica paciente) throws IPSException
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		PreparedStatementDecorator ps=null;
		String temp = "";
		boolean crearSol = false;
		
		if(enTransaccion)
		{			
			temp = "";
			if(mapa.containsKey("unidadVolumenTotal"))
				temp = mapa.get("unidadVolumenTotal").toString(); 
			
			String updateOrdenDieta="UPDATE orden_dieta set volumen_total='"+(mapa.get("volumenTotal")+"").toString()+"', unidad_volumen_total='"+temp+"', velocidad_infusion='"+(mapa.get("velocidadInfusion")+"")+"', dosificacion='"+(mapa.get("dosificacion")+"")+"' WHERE codigo_histo_enca="+mapa.get("numeroOrden");		
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(updateOrdenDieta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				enTransaccion=ps.executeUpdate()>0;
				
				if(!UtilidadTexto.isEmpty(mapa.get("velocidadInfusion")+""))
				{
					logger.info("\n\n\n\n\n\n\n\npendiente de completar = NO\n\n\n\n\n\n\n\n");
					String updateSolicitudes="UPDATE solicitudes_medicamentos set pendiente_completar='"+ConstantesBD.acronimoNo+"' WHERE numero_solicitud="+mapa.get("numeroSolicitud");
					ps= new PreparedStatementDecorator(con.prepareStatement(updateSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
				}
				else
				{
					logger.info("\n\n\n\n\n\n\n\npendiente de completar = SI\n\n\n\n\n\n\n\n");
					String updateSolicitudes="UPDATE solicitudes_medicamentos set pendiente_completar='"+ConstantesBD.acronimoSi+"' WHERE numero_solicitud="+mapa.get("numeroSolicitud");
					ps= new PreparedStatementDecorator(con.prepareStatement(updateSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.executeUpdate();
				}
				
				if(enTransaccion)
				{
					for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
					{
						if((mapa.get("tiporegistro_"+i)+"").equalsIgnoreCase("BD"))
						{
							if(!(mapa.get("volumen_"+i)+"").trim().equals(""))
							{
								temp = "";
								if(mapa.containsKey("unidad_volumen_"+i))
									temp = mapa.get("unidad_volumen_"+i).toString();
									
								String updateONP="UPDATE orden_nutricion_parente SET volumen="+mapa.get("volumen_"+i)+", unidad_volumen='"+temp+"' WHERE codigo_historico_dieta="+mapa.get("numeroOrden") +" AND articulo="+mapa.get("articulo_"+i);
								ps= new PreparedStatementDecorator(con.prepareStatement(updateONP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								enTransaccion=ps.executeUpdate()>0;
								
								///////ACTUALIZAR EL DETALLE_SOLICITUDES
								if(enTransaccion)
								{
									String updateDS = "";
									//**************************************************************************************************
									crearSol = false;
									
									//Verifica si el registro ya existe para la actualizacion o si no para su insercion				
									updateDS = "SELECT COUNT(numero_solicitud) FROM detalle_solicitudes WHERE numero_solicitud="+mapa.get("numeroSolicitud") +" AND articulo="+mapa.get("articulo_"+i);
									ps= new PreparedStatementDecorator(con.prepareStatement(updateDS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
									
									if(rs.next())
									{
										if(rs.getInt(1)>0)
											crearSol = false;
										else
											crearSol = true;
									}
									else
										crearSol = false;
									//***************************************************************************************************
									
									if(crearSol)
									{										
										//Solo inserta cantidad mayores a cero
										if(Utilidades.convertirAEntero(mapa.get("volumen_"+i).toString()) > 0)
										{
											logger.info("\n\n\n\n [Entro guardarModificacionMezcla (insetar detalle_solicitudes)] \n\n\n\n");
											
											String insertDS = "";
											if(mapa.containsKey("esmedicamento_"+i) 
													&&  mapa.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoNo))
											{
												insertDS="INSERT INTO detalle_solicitudes (numero_solicitud,articulo,cantidad) values("+mapa.get("numeroSolicitud")+","+mapa.get("articulo_"+i)+","+UtilidadTexto.aproximarSiguienteUnidad(mapa.get("volumen_"+i).toString())+")";									
											}
											else
											{
												insertDS="INSERT INTO detalle_solicitudes (numero_solicitud,articulo,dosis,cantidad) values("+mapa.get("numeroSolicitud")+","+mapa.get("articulo_"+i)+","+mapa.get("volumen_"+i)+",0)";									
											}								
											
											ps= new PreparedStatementDecorator(con.prepareStatement(insertDS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
											enTransaccion=ps.executeUpdate()>0;
											if(enTransaccion)
											{
												Cargos cargoArticulos= new Cargos();
												enTransaccion=cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(con, 
															medico, 
															paciente, 
															Utilidades.convertirAEntero(mapa.get("numeroSolicitud")+""),
															Utilidades.convertirAEntero(mapa.get("articulo_"+i)+""),
															0,/*cantidad*/ 
															true/*dejarPendiente*/, 
															ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
															ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
															SqlBaseSolicitudDao.obtenerCodigoCentroCostoSolicitado(con, mapa.get("numeroSolicitud")+""),
															ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
															Utilidades.esSolicitudPYP(con, Utilidades.convertirAEntero(mapa.get("numeroSolicitud")+"")),"",false /*tarifaNoModificada*/);
											}
										}
									}
									else
									{									
										updateDS = "";
										if(Utilidades.convertirAEntero(mapa.get("volumen_"+i).toString()) > 0)
										{
											if(mapa.containsKey("esmedicamento_"+i) 
													&&  mapa.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoNo))
												updateDS="UPDATE detalle_solicitudes SET cantidad="+UtilidadTexto.aproximarSiguienteUnidad(mapa.get("volumen_"+i).toString())+" WHERE numero_solicitud="+mapa.get("numeroSolicitud") +" AND articulo="+mapa.get("articulo_"+i);
											else
												updateDS="UPDATE detalle_solicitudes SET dosis="+mapa.get("volumen_"+i)+" WHERE numero_solicitud="+mapa.get("numeroSolicitud") +" AND articulo="+mapa.get("articulo_"+i);
										}
										else
										{
											//Si la cantidad del articulo viene en cero, y ya se encontraba dentro del detalle de la solicitud se elimina el registro
											logger.info("Eliminando el registro en detalle_solicitudes. se ha modificado la cantidad en cero. sol >> "+mapa.get("numeroSolicitud")+" >> articulo >> "+mapa.get("articulo_"+i));									
											updateDS="DELETE FROM detalle_solicitudes WHERE numero_solicitud = "+mapa.get("numeroSolicitud") +" AND articulo="+mapa.get("articulo_"+i);
										}
										
										ps= new PreparedStatementDecorator(con.prepareStatement(updateDS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										
										if(Utilidades.convertirAEntero(mapa.get("volumen_"+i).toString()) > 0)
											enTransaccion=ps.executeUpdate()>0;
										else
										{
											//En el caso de no eliminar o sacar error de integridad, continua el flujo con normalidad
											ps.executeUpdate();
											enTransaccion = true;
										}										
									}
								}
							}
							else
							{
								String updateONP="DELETE FROM orden_nutricion_parente WHERE codigo_historico_dieta="+mapa.get("numeroOrden") +" AND articulo="+mapa.get("articulo_"+i);
								ps= new PreparedStatementDecorator(con.prepareStatement(updateONP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								enTransaccion=ps.executeUpdate()>0;
								///////BORRAR EL DETALLE_SOLICITUDES
								if(enTransaccion)
								{
									String updateDS="DELETE FROM detalle_solicitudes WHERE numero_solicitud="+mapa.get("numeroSolicitud") +" AND articulo="+mapa.get("articulo_"+i);
									ps= new PreparedStatementDecorator(con.prepareStatement(updateDS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									enTransaccion=ps.executeUpdate()>0;
								}
							}
						}
						else if((mapa.get("tiporegistro_"+i)+"").equalsIgnoreCase("MEM")&&!(mapa.get("volumen_"+i)+"").equals(""))
						{
							temp = "";
							if(mapa.containsKey("unidad_volumen_"+i))
								temp = mapa.get("unidad_volumen_"+i).toString();
							
							String insertONP="INSERT INTO orden_nutricion_parente (codigo_historico_dieta,articulo,volumen,unidad_volumen) values("+mapa.get("numeroOrden")+","+mapa.get("articulo_"+i)+","+mapa.get("volumen_"+i)+",'"+temp+"')";
							ps= new PreparedStatementDecorator(con.prepareStatement(insertONP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							enTransaccion=ps.executeUpdate()>0;
							///////insertar en EL DETALLE_SOLICITUDES
							if(enTransaccion)
							{
								//Solo inserta cantidad mayores a cero
								if(Utilidades.convertirAEntero(mapa.get("volumen_"+i).toString()) > 0)
								{
									logger.info("\n\n\n\n [Entro guardarModificacionMezcla (insetar detalle_solicitudes)] \n\n\n\n");
									
									String insertDS = "";
									if(mapa.containsKey("esmedicamento_"+i) 
											&&  mapa.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoNo))
									{
										insertDS="INSERT INTO detalle_solicitudes (numero_solicitud,articulo,cantidad) values("+mapa.get("numeroSolicitud")+","+mapa.get("articulo_"+i)+","+UtilidadTexto.aproximarSiguienteUnidad(mapa.get("volumen_"+i).toString())+")";									
									}
									else
									{
										insertDS="INSERT INTO detalle_solicitudes (numero_solicitud,articulo,dosis,cantidad) values("+mapa.get("numeroSolicitud")+","+mapa.get("articulo_"+i)+","+mapa.get("volumen_"+i)+",0)";									
									}								
									
									ps= new PreparedStatementDecorator(con.prepareStatement(insertDS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									enTransaccion=ps.executeUpdate()>0;
									if(enTransaccion)
									{
									Cargos cargoArticulos= new Cargos();
									enTransaccion=cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(con, 
												medico, 
												paciente, 
												Utilidades.convertirAEntero(mapa.get("numeroSolicitud")+""),
												Utilidades.convertirAEntero(mapa.get("articulo_"+i)+""),
												0,/*cantidad*/ 
												true/*dejarPendiente*/, 
												ConstantesBD.codigoTipoSolicitudMedicamentos/*codigoTipoSolicitudOPCIONAL*/, 
												ConstantesBD.codigoNuncaValido/*codigoCuentaOPCIONAL*/, 
												SqlBaseSolicitudDao.obtenerCodigoCentroCostoSolicitado(con, mapa.get("numeroSolicitud")+""),
												ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
												Utilidades.esSolicitudPYP(con, Utilidades.convertirAEntero(mapa.get("numeroSolicitud")+"")),"", false /*tarifaNoModificada*/);
									}
								}								
							}
						}
						
						
						if(!enTransaccion)
						{
							i=Integer.parseInt(mapa.get("numRegistros")+"");
						}
					}
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		
		if(enTransaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return enTransaccion;
	}

	/**
	 * 
	 * @param con
	 * @param mezcla
	 * @return
	 */
	public static boolean accionAnularMezcla(Connection con, HashMap mapa)
	{
		String updateONP="UPDATE solicitudes SET estado_historia_clinica="+ConstantesBD.codigoEstadoHCAnulada+" WHERE numero_solicitud="+mapa.get("numeroSolicitud");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(updateONP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(ps.executeUpdate()>0)
			{
				updateONP="UPDATE det_cargos SET estado="+ConstantesBD.codigoEstadoFAnulada+" WHERE solicitud="+mapa.get("numeroSolicitud");
				ps= new PreparedStatementDecorator(con.prepareStatement(updateONP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return ps.executeUpdate()>0;
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
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @param codigoInstitucionInt
	 * @param codigoArea
	 * @return
	 */
	public static HashMap consultarFechasHistoCuidadosEspe(Connection con, int codigoCuenta, int cuentaAsocio)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		

		
		StringBuffer consultaBuffer=new StringBuffer();
		
		String cue = "";
		
		if ( cuentaAsocio != 0) 
		{ 
			cue = "	 IN ("+ codigoCuenta + ", " + cuentaAsocio + " )"; 
		}
		else
		{ 
			cue = "	 = " + codigoCuenta; 
		}
		
		
		
		consultaBuffer.append( " SELECT * FROM (" +
							   					" (SELECT " +
							   						" to_char (enc.fecha_orden,'DD/MM/YYYY') as fecha_reg," +
							   						" enc.fecha_orden as fecha " +
							   					" FROM ordenes_medicas om " + 
							   					" INNER JOIN encabezado_histo_orden_m enc ON (enc.orden_medica=om.codigo) " +
							   					" INNER JOIN detalle_cuidado_enfer dce ON (enc.codigo = dce.cod_histo_enca) " +
							   					" WHERE om.cuenta " +cue);
				
						consultaBuffer.append(" UNION " +
												   " SELECT " +
												   		" to_char (enc.fecha_orden,'DD/MM/YYYY') as fecha_reg," +
												   		" enc.fecha_orden as fecha " +
												   	" FROM ordenes_medicas om " + 
												   	" INNER JOIN encabezado_histo_orden_m enc ON (enc.orden_medica=om.codigo) " + 
												   	" INNER JOIN detalle_otro_cuidado_enf doce ON (enc.codigo=doce.cod_histo_cuidado_enfer) " +
								   					" WHERE om.cuenta " +cue);

						consultaBuffer.append(") UNION ( "); 
						
						consultaBuffer.append(" SELECT  " +
													" to_char (ehre.fecha_registro,'DD/MM/YYYY')  as fecha_reg," +
													" ehre.fecha_registro as fecha " +
												" FROM registro_enfermeria re " +			
												" INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = re.codigo ) " +
												" INNER JOIN detalle_cuidado_reg_enfer dcre ON ( ehre.codigo = dcre.codigo_histo_enfer ) " +	
												" WHERE re.cuenta " +cue+" "+
												" UNION " +
												" SELECT " +
													" to_char (ehre.fecha_registro,'DD/MM/YYYY')  as fecha_reg," +
													" ehre.fecha_registro as fecha " +
											  " FROM registro_enfermeria re " +  
											  " INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = re.codigo ) " +
											  " INNER JOIN detalle_otro_cuidado_renf docr ON ( ehre.codigo = docr.codigo_histo_enfer ) " + 
												" WHERE re.cuenta " +cue+" "+
												"	) " +			   	   
			"	) tabla order by fecha DESC ");


			
		//logger.info("\n\n\n\n\n\n\n\n"+consultaBuffer.toString().replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * Metodo para consultar el estado del Parametro Interfaz Nutricion
	 * @param con
	 * @return
	 */
	public static String consultarParametroInterfaz(Connection con) 
	{
		try
		{
			String consultaStr="SELECT valor from valores_por_defecto where parametro=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Metodo para consultar el Piso al que pertence una Cama
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String consultarPisoCama(Connection con, int codigoCama) 
	{
		try
		{
			String consultaStr="SELECT pi.codigo_piso AS piso from camas1 cam " +
													"INNER JOIN habitaciones hab ON (hab.codigo=cam.habitacion) " +
													"INNER JOIN pisos pi ON (pi.codigo=hab.piso) where cam.codigo=? ";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoCama);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("piso");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Metodo para consultar el Numero de la cama comun para la interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String consultarNumeroCama(Connection con, int codigoCama) 
	{
		try
		{
			String consultaStr="SELECT numero_cama from camas1 where codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoCama);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("numero_cama");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Metodo para consultar la Fecha de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public static String consultarFechaDieta(Connection con, int codigoEncabezado) 
	{
		try
		{
			String consultaStr="SELECT to_char(fecha_orden,'dd/mm/yyyy') AS fecha_orden from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoEncabezado);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("fecha_orden");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Metodo para consultar la Hora de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public static String consultarHoraDieta(Connection con, int codigoEncabezado) 
	{
		try
		{
			String consultaHoraStr="SELECT hora_orden from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaHoraStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoEncabezado);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("hora_orden");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 *  Metodo para consultar la Fecha de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public static String consultarFechaGrabacion(Connection con, int codigoEncabezado) 
	{
		try
		{
			String consultaStr="SELECT fecha_grabacion from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoEncabezado);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("fecha_grabacion");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Metodo para consultar la Hora de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public static String consultarHoraGrabacion(Connection con, int codigoEncabezado) 
	{
		try
		{
			String consultaStr="SELECT hora_grabacion from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoEncabezado);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("hora_grabacion");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Metodo para Consultar el campo VIP del Convenio asociado al Ingreso del Paciente
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String consultarConvenioVip(Connection con, int codigoConvenio) 
	{
		try
		{
			String consultaStr="SELECT vip from convenios where codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("vip");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * Metodo para Consultar los tipos de dieta activos para la dieta actual del paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String consultaStr = "SELECT o.nutricion_oral_cc_inst AS codigo, '' AS descripcion, " +
							"tno.codigo_interfaz as codigoInterfaz FROM orden_nutricion_oral o INNER JOIN nutricion_oral_cc_inst NOCCI " +
							"on(NOCCI.codigo=o.nutricion_oral_cc_inst) inner join tipo_nutricion_oral tno " +
							"on(tno.codigo=NOCCI.nutricion_oral) WHERE " +
							"codigo_historico_dieta=(SELECT MAX (codigo_historico_dieta) as codigo_histo " +
							"FROM orden_nutricion_oral ono INNER JOIN orden_dieta od ON " +
							"(ono.codigo_historico_dieta=od.codigo_histo_enca) INNER JOIN encabezado_histo_orden_m eh ON " +
							"(eh.codigo=od.codigo_histo_enca)  INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo)  " +
							"WHERE om.cuenta=? ) "; 
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * Metodo para Consultar la Descripcion de la Dieta del Paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String consultarDescripcionDieta(Connection con, int codigoCuenta) 
	{
		try
		{
			String consultaStr="SELECT replace(descripcion_dieta_oral, E'\r', '&&&&&') as descripcion_dieta_oral from ordenes_medicas where cuenta =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("descripcion_dieta_oral");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * M�todo para consultar el arreglo de un campo de la seccion prescripcion di�lisis seg�n tipo de consulta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarArregloPrescripcionDialisis(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> arreglo = new ArrayList<HashMap<String,Object>>();
		try
		{
			//***********SE TOMAN LOS PAR�METROS********************************
			int tipoConsulta = Integer.parseInt(campos.get("tipoConsulta").toString());
			String parametroAdicional1 = campos.get("parametroAdicional1").toString();
			//******************************************************************
			
			String consulta = "";
			Statement st = null;
			
			//Tipo consulta FILTRO
			if(tipoConsulta == DtoPrescripcionDialisis.tipoConsultaFiltro&&!parametroAdicional1.equals(""))
			{
				consulta = "SELECT fh.codigo,fh.nombre from filtro_hemodialisis fh inner join tipos_mem_filtro_hemo tmfh ON(tmfh.codigo_filtro = fh.codigo) where tmfh.codigo_tipo_membrana = "+parametroAdicional1+" and tmfh.activo= '"+ConstantesBD.acronimoSi+"' and fh.activo = '"+ConstantesBD.acronimoSi+"' order by fh.nombre";
			}
			//Tipo consulta FLUJO BOMBA
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaFlujoBomba)
			{
				consulta = "SELECT codigo,nombre FROM flujos_bomba WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			//Tipo Consulta FLUJO DIALIZADO
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaFlujoDializado)
			{
				consulta = "SELECT codigo,nombre FROM flujos_dializado WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			//Tipo Consulta ACCESO VASCULAR
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaAccesoVascular)
			{
				consulta = "SELECT codigo,nombre FROM hemo_accesos_vasc WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			//Tipo Consulta RECAMBIO
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaRecambio)
			{
				consulta = "SELECT codigo,nombre FROM recambio_capd_apd WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			//Tipo consulta VOLUMEN
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaVolumen)
			{
				consulta = "SELECT codigo,nombre FROM volumen_capd_apd WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			//Tipo consulya TIPOS MEMBRANA
			else if (tipoConsulta == DtoPrescripcionDialisis.tipoConsultaTiposMembrana)
			{
				consulta = "SELECT codigo,nombre FROM tipos_membrana WHERE activo = '"+ConstantesBD.acronimoSi+"' order by nombre";
			}
			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				arreglo.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarArregloPrescripcionDialisis: "+e);
		}
		return arreglo;
	}
	
	/**
	 * M�todo para insertar una prescripcion de di�lisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public static int insertarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
	{
		int respuesta = ConstantesBD.codigoNuncaValido;
		try
		{
			//************SE INSERTA EL ENCABEZADO***********************************
			String consulta = "INSERT INTO prescripcion_dialisis " +
				"(codigo_histo_enca,modalidad_terapia,fecha_modifica,hora_modifica,usuario_modifica) " +
				"VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(dialisis.getCodigoHistoEnca()));
			pst.setString(2,dialisis.getModalidadTerapia());
			pst.setString(3,dialisis.getProfesional().getLoginUsuario());
			
			respuesta = pst.executeUpdate();
			//***********************************************************************
			
			if(respuesta>0)
			{
				//************INSERCI�N DEL DETALLE**************************************
				//***********MODALIDAD TERAPIA HEMODIALISIS************************************************
				if(dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis))
				{
					consulta = "INSERT INTO hemodialisis (" +
						"codigo," +
						"codigo_histo_enca," +
						"tiempo," +
						"peso_seco," +
						"filtro," +
						"flujo_bomba," +
						"flujo_dializado," +
						"up," +
						"acceso_vascular," +
						"anticoagulacion," +
						"peso_pre," +
						"peso_pos," +
						"fecha_modifica," +
						"hora_modifica," +
						"usuario_modifica," +
						"tipo_membrana) " +
						"VALUES " +
						"(?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?) ";
					
					for(DtoHemodialisis hemo:dialisis.getHemodialisis())
						if(respuesta>0)
						{
							pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							int consecutivoHemodialisis = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_hemodialisis");
							pst.setInt(1,consecutivoHemodialisis);
							pst.setInt(2,Integer.parseInt(dialisis.getCodigoHistoEnca()));
							if(!hemo.getTiempo().equals(""))
								pst.setString(3, hemo.getTiempo());
							else
								pst.setNull(3,Types.VARCHAR);
							if(!hemo.getPesoSeco().equals(""))
								pst.setFloat(4, Float.parseFloat(hemo.getPesoSeco()));
							else
								pst.setNull(4,Types.NUMERIC);
							if(hemo.getCodigoFiltro()>0)
								pst.setInt(5,hemo.getCodigoFiltro());
							else
								pst.setNull(5,Types.INTEGER);
							if(hemo.getCodigoFlujoBomba()>0)
								pst.setInt(6,hemo.getCodigoFlujoBomba());
							else
								pst.setNull(6,Types.INTEGER);
							if(hemo.getCodigoFlujoDializado()>0)
								pst.setInt(7,hemo.getCodigoFlujoDializado());
							else
								pst.setNull(7,Types.INTEGER);
							if(!hemo.getUp().equals(""))
								pst.setString(8,hemo.getUp());
							else
								pst.setNull(8,Types.VARCHAR);
							if(hemo.getCodigoAccesoVascular()>0)
								pst.setInt(9,hemo.getCodigoAccesoVascular());
							else
								pst.setNull(9,Types.INTEGER);
							if(!hemo.getAnticoagulacion().equals(""))
								pst.setString(10,hemo.getAnticoagulacion());
							else
								pst.setNull(10,Types.VARCHAR);
							if(!hemo.getPesoPre().equals(""))
								pst.setFloat(11,Float.parseFloat(hemo.getPesoPre()));
							else
								pst.setNull(11,Types.NUMERIC);
							if(!hemo.getPesoPos().equals(""))
								pst.setFloat(12,Float.parseFloat(hemo.getPesoPos()));
							else
								pst.setNull(12,Types.NUMERIC);
							pst.setString(13,dialisis.getProfesional().getLoginUsuario());
							if(hemo.getCodigoTipoMembrana()>0)
								pst.setInt(14,hemo.getCodigoTipoMembrana());
							else
								pst.setNull(14,Types.INTEGER);
							respuesta = pst.executeUpdate();
						}
				}
				//*********MODALIDAD TERAPIA CAPD O APD****************************************************
				else if(dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoCAPD)||
						dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoAPD))
				{
					consulta = "INSERT INTO capd_apd (codigo,codigo_histo_enca,recambio,volumen,unidades,fecha_modifica,hora_modifica,usuario_modifica) " +
						" VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
					int consecutivoCapdApd = ConstantesBD.codigoNuncaValido;
					
					for(HashMap<String, Object> volumen:dialisis.getVolumenes())
						for(HashMap<String, Object> recambio:dialisis.getRecambios())
							if(respuesta > 0)
							{
								consecutivoCapdApd = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_capd_apd");
								int cod_recambio = Integer.parseInt(recambio.get("codigo").toString());
								int cod_volumen = Integer.parseInt(volumen.get("codigo").toString());
								
								pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
								pst.setInt(1, consecutivoCapdApd);
								pst.setInt(2,Integer.parseInt(dialisis.getCodigoHistoEnca()));
								pst.setInt(3,cod_recambio);
								pst.setInt(4,cod_volumen);
								if(!UtilidadTexto.isEmpty(dialisis.getMapa(cod_recambio+"_"+cod_volumen)+""))
									pst.setInt(5,Integer.parseInt(dialisis.getMapa(cod_recambio+"_"+cod_volumen).toString()));
								else
									pst.setNull(5,Types.INTEGER);
								pst.setString(6,dialisis.getProfesional().getLoginUsuario());
								respuesta = pst.executeUpdate();
							}
				}
				//***********************************************************************
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPrescripcionDialisis: "+e);
		}
		return respuesta;
	}
	
	/**
	 * M�todo implementado para cargar el hist�rico de prescripciones di�lisis de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoPrescripcionDialisis> getHistoricoPrescripcionDialisis(Connection con,HashMap campos)
	{
		ArrayList<DtoPrescripcionDialisis> listado = new ArrayList<DtoPrescripcionDialisis>();
		try
		{
			//**************SE TOMAN LOS PAR�METROS***********************************************
			int codigoHistoEnca = Utilidades.convertirAEntero(campos.get("codigoHistoEnca").toString());
			int codigoPaciente = Integer.parseInt(campos.get("codigoPaciente").toString());
			//**************************************************************************************
			
			String consulta = "";
			//Si se ingres� un consecutivo de encabezado de historial orden m�dica, se consulta una sola prescripcion
			if(codigoHistoEnca>0)
				consulta += "SELECT " +
					"'' as fecha_orden, " +
					"'' as hora_orden, "+
					"pd.codigo_histo_enca, "+
					"pd.modalidad_terapia, "+
					"coalesce(to_char(pd.fecha_fin_dialisis,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_fin_dialisis, "+
					"coalesce(pd.hora_fin_dialisis,'') as hora_fin_dialisis, " +
					"pd.usuario_modifica," +
					"coalesce(pd.finalizado,'') as finalizado " +
					"FROM prescripcion_dialisis pd " +
					"WHERE " +
					"pd.codigo_histo_enca = "+codigoHistoEnca;
			else
				consulta += "SELECT " +
					"to_char(ehom.fecha_orden,'"+ConstantesBD.formatoFechaAp+"') as fecha_orden, " +
					"substr(ehom.hora_orden,0,6) as hora_orden, "+ 
					"pd.codigo_histo_enca, "+
					"pd.modalidad_terapia, "+
					"coalesce(to_char(pd.fecha_fin_dialisis,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_fin_dialisis, "+
					"coalesce(pd.hora_fin_dialisis,'') as hora_fin_dialisis, " +
					"pd.usuario_modifica, "+
					"coalesce(pd.finalizado,'') as finalizado " +
					"FROM cuentas c "+ 
					"INNER JOIN ordenes_medicas om ON(om.cuenta = c.id) "+ 
					"INNER JOIN encabezado_histo_orden_m ehom ON(ehom.orden_medica = om.codigo) "+ 
					"INNER JOIN prescripcion_dialisis pd ON(pd.codigo_histo_enca = ehom.codigo) "+ 
					"WHERE " +
					"c.codigo_paciente = "+codigoPaciente+" ORDER BY ehom.codigo desc";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				DtoPrescripcionDialisis dialisis = new DtoPrescripcionDialisis();
				
				dialisis.setCodigoHistoEnca(rs.getString("codigo_histo_enca"));
				dialisis.setModalidadTerapia(rs.getString("modalidad_terapia"));
				dialisis.setFechaFinalDialisis(rs.getString("fecha_fin_dialisis"));
				dialisis.setHoraFinalDialisis(rs.getString("hora_fin_dialisis"));
				dialisis.getProfesional().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				dialisis.setFechaOrden(rs.getString("fecha_orden"));
				dialisis.setHoraOrden(rs.getString("hora_orden"));
				dialisis.setFinalizado(UtilidadTexto.getBoolean(rs.getString("finalizado")));
				dialisis.setManejoFinalizado(dialisis.isFinalizado());
				
				
				
				//Se consultan las fechas/horas iniciales que se hayan registrado
				String consulta3 = "SELECT " +
					"consecutivo," +
					"to_char(fecha_inicio_dialisis,'"+ConstantesBD.formatoFechaAp+"') as fecha_inicio_dialisis," +
					"hora_inicio_dialisis," +
					"usuario_modifica " +
					"from prescrip_dial_fecha_hora " +
					"where codigo_histo_enca = "+dialisis.getCodigoHistoEnca()+" order by fecha_inicio_dialisis desc, hora_inicio_dialisis desc";
				
				Statement st3 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs3 = new ResultSetDecorator(st3.executeQuery(consulta3));
				while(rs3.next())
				{
					DtoPrescripDialFechaHora fechaHora = new DtoPrescripDialFechaHora();
					fechaHora.setConsecutivo(rs3.getString("consecutivo"));
					fechaHora.setFechaInicialDialisis(rs3.getString("fecha_inicio_dialisis"));
					fechaHora.setHoraInicialDialisis(rs3.getString("hora_inicio_dialisis"));
					fechaHora.getProfesional().cargarUsuarioBasico(con, rs3.getString("usuario_modifica"));
					dialisis.getFechasHorasIniciales().add(fechaHora);
				}
				
				
				
				
				String consulta2 = "";
				
				//Seg�n la modalidad de la terapia se carga un detalle espec�fico
				if(dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis))
				{
					//************************HEMODIALISIS***********************************************
					consulta2 = "SELECT "+ 
						"h.codigo, "+
						"h.codigo_histo_enca, "+
						"coalesce(h.tiempo,'') as tiempo, "+
						"coalesce(h.peso_seco||'','') as peso_seco, "+
						"coalesce(h.filtro,"+ConstantesBD.codigoNuncaValido+") as codigo_filtro, "+ 
						"coalesce(fh.nombre,'') as nombre_filtro, "+ 
						"coalesce(h.flujo_bomba,"+ConstantesBD.codigoNuncaValido+") as codigo_flujo_bomba, "+
						"coalesce(fb.nombre,'') as nombre_flujo_bomba, "+ 
						"coalesce(h.flujo_dializado,"+ConstantesBD.codigoNuncaValido+") as codigo_flujo_dializado, "+
						"coalesce(fd.nombre,'') as nombre_flujo_dializado, "+ 
						"coalesce(h.up,'') as up, "+ 
						"coalesce(h.acceso_vascular,"+ConstantesBD.codigoNuncaValido+") as codigo_acceso_vascular, "+ 
						"coalesce(ha.nombre,'') as nombre_acceso_vascular, "+ 
						"coalesce(h.anticoagulacion,'') as anticoagulacion, "+ 
						"coalesce(h.peso_pre||'','') as peso_pre, "+
						"coalesce(h.peso_pos||'','') as peso_pos, "+ 
						"coalesce(h.hemodialisis_padre||'','') as hemodialisis_padre, "+ 
						"h.usuario_modifica," +
						"to_char(h.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica," +
						"h.hora_modifica as hora_modifica, " +
						"coalesce(h.tipo_membrana,"+ConstantesBD.codigoNuncaValido+") as codigo_tipo_membrana, " +
						"coalesce(tm.nombre,'') as nombre_tipo_membrana "+ 
						"FROM hemodialisis h "+ 
						"LEFT OUTER JOIN filtro_hemodialisis fh ON(fh.codigo = h.filtro) "+ 
						"LEFT OUTER JOIN flujos_bomba fb ON(fb.codigo = h.flujo_bomba) "+ 
						"LEFT OUTER JOIN flujos_dializado fd ON(fd.codigo = h.flujo_dializado) "+ 
						"LEFT OUTER JOIN hemo_accesos_vasc ha ON(ha.codigo = h.acceso_vascular) "+ 
						"LEFT OUTER JOIN tipos_membrana tm ON(tm.codigo = h.tipo_membrana) "+
						"WHERE "+ 	
						"h.codigo_histo_enca = "+dialisis.getCodigoHistoEnca();
					
						
					consulta2+=" order by h.codigo desc";
					
					Statement st2 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					ResultSetDecorator rs2 = new ResultSetDecorator(st2.executeQuery(consulta2));
					
					while(rs2.next())
					{
						DtoHemodialisis hemo = new DtoHemodialisis();
						hemo.setConsecutivoHemodialisis(rs2.getString("codigo"));
						hemo.setTiempo(rs2.getString("tiempo"));
						hemo.setPesoSeco(rs2.getString("peso_seco"));
						hemo.setPesoSecoAnterior(hemo.getPesoSeco());
						hemo.setCodigoFiltro(rs2.getInt("codigo_filtro"));
						hemo.setNombreFiltro(rs2.getString("nombre_filtro"));
						hemo.setCodigoFiltroAnterior(hemo.getCodigoFiltro());
						hemo.setNombreFiltroAnterior(hemo.getNombreFiltro());
						hemo.setCodigoFlujoBomba(rs2.getInt("codigo_flujo_bomba"));
						hemo.setNombreFlujoBomba(rs2.getString("nombre_flujo_bomba"));
						hemo.setCodigoFlujoDializado(rs2.getInt("codigo_flujo_dializado"));
						hemo.setNombreFlujoDializado(rs2.getString("nombre_flujo_dializado"));
						hemo.setUp(rs2.getString("up"));
						hemo.setUpAnterior(hemo.getUp());
						hemo.setCodigoAccesoVascular(rs2.getInt("codigo_acceso_vascular"));
						hemo.setNombreAccesoVascular(rs2.getString("nombre_acceso_vascular"));
						hemo.setAnticoagulacion(rs2.getString("anticoagulacion"));
						hemo.setPesoPre(rs2.getString("peso_pre"));
						hemo.setPesoPreAnterior(hemo.getPesoPre());
						hemo.setPesoPos(rs2.getString("peso_pos"));
						hemo.setPesoPosAnterior(hemo.getPesoPos());
						hemo.setConsecutivoHemodialisisPadre(rs2.getString("hemodialisis_padre"));
						hemo.getUsuarioModifica().cargarUsuarioBasico(con, rs2.getString("usuario_modifica"));
						hemo.setFechaModifica(rs2.getString("fecha_modifica"));
						hemo.setHoraModifica(rs2.getString("hora_modifica"));
						hemo.setCodigoTipoMembrana(rs2.getInt("codigo_tipo_membrana"));
						hemo.setNombreTipoMembrana(rs2.getString("nombre_tipo_membrana"));
						
						dialisis.getHemodialisis().add(hemo);
					}
					//*************************************************************************************
				}
				else
				{
					//********************CAPD O APD*********************************************************
					consulta2 = "SELECT "+ 
						"ca.codigo, "+
						"ca.codigo_histo_enca, "+
						"ca.recambio as codigo_recambio, "+
						"ca.volumen as codigo_volumen, "+
						"coalesce(ca.unidades||'','') as unidades "+  
						"FROM capd_apd ca " +
						"WHERE ca.codigo_histo_enca = "+dialisis.getCodigoHistoEnca();
					
					Statement st2 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					ResultSetDecorator rs2 = new ResultSetDecorator(st2.executeQuery(consulta2));
					
					while(rs2.next())
					{
						dialisis.setMapa(rs2.getInt("codigo_recambio")+"_"+rs2.getInt("codigo_volumen"), rs2.getString("unidades"));
					}
					//Se cargan los arreglos
					consulta2 = "SELECT DISTINCT "+ 
						"ca.recambio as codigo, "+
						"rca.nombre as nombre "+ 
						"FROM capd_apd ca "+ 
						"INNER JOIN recambio_capd_apd rca ON(rca.codigo = ca.recambio) "+ 
						"WHERE ca.codigo_histo_enca = "+dialisis.getCodigoHistoEnca()+" ORDER BY rca.nombre";
					st2 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					rs2 = new ResultSetDecorator(st2.executeQuery(consulta2));
					
					while(rs2.next())
					{
						HashMap<String, Object> elemento= new HashMap<String, Object>();
						elemento.put("codigo", rs2.getString("codigo"));
						elemento.put("nombre", rs2.getString("nombre"));
						dialisis.getRecambios().add(elemento);
					}
					
					consulta2 = "SELECT DISTINCT "+
						"ca.volumen as codigo, "+
						"vca.nombre as nombre "+ 
						"FROM capd_apd ca "+ 
						"INNER JOIN volumen_capd_apd vca ON(vca.codigo = ca.volumen) "+ 
						"WHERE ca.codigo_histo_enca = "+dialisis.getCodigoHistoEnca()+" ORDER BY vca.nombre";
					st2 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					rs2 = new ResultSetDecorator(st2.executeQuery(consulta2));
					
					while(rs2.next())
					{
						HashMap<String, Object> elemento = new HashMap<String, Object>();
						elemento.put("codigo", rs2.getString("codigo"));
						elemento.put("nombre", rs2.getString("nombre"));
						dialisis.getVolumenes().add(elemento);
					}
					//***************************************************************************************
				}
				
				
				listado.add(dialisis);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getHistoricoPrescripcionDialisis: "+e);
		}
		return listado;
	}
	
	/**
	 * M�todo implementado para modificar una prescripci�n de dialisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public static int modificarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
	{
		int resultado = 0;
		try
		{
			String consulta = "UPDATE prescripcion_dialisis SET fecha_fin_dialisis = ?, hora_fin_dialisis = ?, finalizado = ?,usuario_fin_dialisis = ? WHERE codigo_histo_enca = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			if(!dialisis.getFechaFinalDialisis().equals("")&&dialisis.isManejoFinalizado())
				pst.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dialisis.getFechaFinalDialisis())));
			else
				pst.setNull(1,Types.DATE);
			if(!dialisis.getHoraFinalDialisis().equals("")&&dialisis.isManejoFinalizado())
				pst.setString(2,dialisis.getHoraFinalDialisis());
			else
				pst.setNull(2,Types.VARCHAR);
			if(dialisis.isManejoFinalizado())
				pst.setString(3,ConstantesBD.acronimoSi);
			else
				pst.setString(3,ConstantesBD.acronimoNo);
			
			if(!dialisis.getProfesionalFinaliza().getLoginUsuario().equals(""))
				pst.setString(4,dialisis.getProfesionalFinaliza().getLoginUsuario());
			else
				pst.setNull(4,Types.VARCHAR);
			pst.setInt(5, Integer.parseInt(dialisis.getCodigoHistoEnca()));
			
			resultado = pst.executeUpdate();
			
			
			//Si la actualizaci�n es exitosa se ingresa la fecha/hora inicial de la dialisis
			if(resultado>0)
			{
				consulta = "INSERT INTO prescrip_dial_fecha_hora (consecutivo,fecha_inicio_dialisis,hora_inicio_dialisis,codigo_histo_enca,fecha_modifica,hora_modifica,usuario_modifica) values (?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
				for(DtoPrescripDialFechaHora fechaHora:dialisis.getFechasHorasIniciales())
					//Si no hay consecutivo quiere decir que se ingresa nuevo registro
					if(resultado>0&&fechaHora.getConsecutivo().equals("")&&!fechaHora.getFechaInicialDialisis().equals("")&&!fechaHora.getHoraInicialDialisis().equals(""))
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_prescrip_dial_f_h"));
						pst.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaHora.getFechaInicialDialisis())));
						pst.setString(3,fechaHora.getHoraInicialDialisis());
						pst.setInt(4, Integer.parseInt(dialisis.getCodigoHistoEnca()));
						pst.setString(5,fechaHora.getProfesional().getLoginUsuario());
						resultado = pst.executeUpdate();
					}
			}
			
			
			//Si la modalidad de la terpaia es HEMODIALISIS, se continua con la modificacion
			if(resultado>0 && dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis) &&
				(
					(!dialisis.getHemodialisis().get(0).getPesoSeco().equals(dialisis.getHemodialisis().get(0).getPesoSecoAnterior())&&
					!dialisis.getHemodialisis().get(0).getPesoSeco().equals(""))
					||
					(dialisis.getHemodialisis().get(0).getCodigoFiltro()!=dialisis.getHemodialisis().get(0).getCodigoFiltroAnterior()&&
					dialisis.getHemodialisis().get(0).getCodigoFiltro()>0)
					||
					(!dialisis.getHemodialisis().get(0).getUp().equals(dialisis.getHemodialisis().get(0).getUpAnterior())&&
					!dialisis.getHemodialisis().get(0).getUp().equals(""))
					||
					(!dialisis.getHemodialisis().get(0).getPesoPre().equals(dialisis.getHemodialisis().get(0).getPesoPreAnterior())&&
					!dialisis.getHemodialisis().get(0).getPesoPre().equals(""))
					||
					(!dialisis.getHemodialisis().get(0).getPesoPos().equals(dialisis.getHemodialisis().get(0).getPesoPosAnterior())&&
					!dialisis.getHemodialisis().get(0).getPesoPos().equals(""))
				)
			)
			{
				consulta = "INSERT INTO hemodialisis (" +
					"codigo," +
					"codigo_histo_enca," +
					"tiempo," +
					"peso_seco," +
					"filtro," +
					"flujo_bomba," +
					"flujo_dializado," +
					"up," +
					"acceso_vascular," +
					"anticoagulacion," +
					"peso_pre," +
					"peso_pos," +
					"fecha_modifica," +
					"hora_modifica," +
					"usuario_modifica," +
					"hemodialisis_padre," +
					"tipo_membrana" +
					") " +
					"SELECT " +
					UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_hemodialisis")+", " +
					"h.codigo_histo_enca," +
					"h.tiempo," +
					(dialisis.getHemodialisis().get(0).getPesoSeco().equals("")?"null":dialisis.getHemodialisis().get(0).getPesoSeco())+", " +
					(dialisis.getHemodialisis().get(0).getCodigoFiltro()>1?dialisis.getHemodialisis().get(0).getCodigoFiltro()+"":"null")+", " +
					"h.flujo_bomba," +
					"h.flujo_dializado," +
					(dialisis.getHemodialisis().get(0).getUp().equals("")?"null":"'"+dialisis.getHemodialisis().get(0).getUp()+"'")+", " +
					"h.acceso_vascular," +
					"h.anticoagulacion," +
					(dialisis.getHemodialisis().get(0).getPesoPre().equals("")?"null":dialisis.getHemodialisis().get(0).getPesoPre())+", " +
					(dialisis.getHemodialisis().get(0).getPesoPos().equals("")?"null":dialisis.getHemodialisis().get(0).getPesoPos())+", " +
					"CURRENT_DATE," +
					""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
					"'"+dialisis.getProfesional().getLoginUsuario()+"'," +
					"h.codigo, " +
					"h.tipo_membrana " +
					"FROM hemodialisis h " +
					"WHERE h.codigo_histo_enca = ? and h.hemodialisis_padre is null";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(dialisis.getCodigoHistoEnca()));
				resultado = pst.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarPrescripcionDialisis: "+e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mezcla
	 * @return
	 */
	public static String consultarDosificacionMezcla(Connection con, int mezcla,int codigoCuenta, int cuentaAsocio)
	{
		try
		{
			String consultaStr="SELECT dosificacion from orden_dieta where mezcla="+mezcla+" AND codigo_histo_enca >=maxHistoricoFinDieta("+mezcla+", "+codigoCuenta+", "+cuentaAsocio+")";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+consultaStr);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Consulta las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap obtenerObservacionesMezcla(Connection con, HashMap parametros)
	{		
		String cadena = "SELECT " +
						"o.codigo, " +
						"o.descripcion_dieta_par," +
						"o.descripcion_dieta_enfermera " +
						"FROM ordenes_medicas o " +
						"INNER JOIN solicitudes s ON (s.numero_solicitud = ? AND o.cuenta = s.cuenta) ";
		
		try
		{
			//logger.info("valor de parametros >> "+parametros);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{	
			e.printStackTrace();
		}
		
		return new HashMap();		
	}
	
	/**
	 * Actualiza las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarObservacionesMezcla(Connection con, HashMap parametros)
	{		
		String cadena = "UPDATE ordenes_medicas SET descripcion_dieta_enfermera = ? WHERE codigo = ? ";
		
		try
		{
			logger.info("valor de los parametros >> "+parametros);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1,parametros.get("descripcionDietaEnfermera").toString());		
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));			
			
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
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @return
	 */
	public static ArrayList<Object> cargarResultadoLaboratorios(Connection con, int ingresoPaciente, int cuentaPaciente,int centroCostoPaciente, boolean cargarParametrizacion,boolean historico) 
	{
		ArrayList<Object> resultado=new ArrayList<Object>();
		Connection conLocal=null;
		//cargar lo que ya se ha llenado al paciente.
		String consulta="";
		String where1="";
		if(!historico)
		{
			consulta="SELECT DISTINCT " +
								" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
								" ETIQUIETA_CAMPO as etiquetacampo," +
								" ORDEN as orden," +
								" CENTRO_COSTO as centrocosto," +
								" CAMPO_OTRO as campootro" +
						" FROM manejopaciente.resultado_laboratorio_orden rlo " ;
		
			if(cuentaPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM encabezado_histo_orden_m eh INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo)  WHERE om.cuenta="+cuentaPaciente+")" ;
			else if(ingresoPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM encabezado_histo_orden_m eh INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo)  WHERE om.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+")) " ;
			consulta=consulta+" "+where1;
			consulta=consulta+" UNION ";
			consulta=consulta+ " SELECT DISTINCT " +
							" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
							" ETIQUIETA_CAMPO as etiquetacampo," +
							" ORDEN as orden," +
							" CENTRO_COSTO as centrocosto," +
							" CAMPO_OTRO as campootro" +
					" FROM manejopaciente.resultado_laboratorio_regenf rle " ;
				
				if(cuentaPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM enca_histo_registro_enfer eh INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo)  WHERE re.cuenta="+cuentaPaciente+")";
				else if(ingresoPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM enca_histo_registro_enfer eh INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo)  WHERE re.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+"))";

			if(cargarParametrizacion)
			{
				//cargar lo parametrizado, pero que no tiene informacion.
				consulta=consulta+" UNION select  "+
									" codigo as codigocampo," +
									" etiqueta_campo as etiquetacampo," +
									" orden as orden," +
									" centro_costo as centrocosto," +
									" 'N' as campootro " +
								" FROM  manejopaciente.param_resultados_lab where centro_costo = "+centroCostoPaciente+" and codigo not in (select codigo_campo from manejopaciente.resultado_laboratorio_orden "+where1+")"; 
				
			}
		}
		else
		{
			consulta="SELECT " +
							" CODIGO_HISTO_ENCA as codigohistoenca," +
							" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
							" ETIQUIETA_CAMPO as etiquetacampo," +
							" ORDEN as orden," +
							" CENTRO_COSTO as centrocosto," +
							" VALOR as valor," +
							" CAMPO_OTRO as campootro," +
							" eh.fecha_orden as fecha," +
							" substr(eh.hora_orden,0,5) as hora," +
							" login as loginusuario," +
							" getdatosmedico(login) as nombreusuario " +
					" FROM manejopaciente.resultado_laboratorio_orden rlo " +
					" inner join encabezado_histo_orden_m eh on (rlo.CODIGO_HISTO_ENCA=eh.codigo) " +
					" INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) ";
					if(cuentaPaciente>0)
						where1="  WHERE om.cuenta="+cuentaPaciente;
					else if(ingresoPaciente>0)
						where1=" where om.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+")";
					consulta=consulta+" "+where1;
			consulta=consulta + " UNION ";
			consulta=consulta+" SELECT " +
									" CODIGO_HISTO_ENCA as codigohistoenca," +
									" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
									" ETIQUIETA_CAMPO as etiquetacampo," +
									" ORDEN as orden," +
									" CENTRO_COSTO as centrocosto," +
									" VALOR as valor," +
									" CAMPO_OTRO as campootro," +
									" ere.fecha_registro as fecha," +
									" substr(ere.hora_registro,0,5) as hora," +
									" usuario as loginusuario," +
									" getdatosmedico(usuario) as nombreusuario " +
							" FROM manejopaciente.resultado_laboratorio_regenf rle " +
							" inner join enca_histo_registro_enfer ere on (rle.CODIGO_HISTO_ENCA=ere.codigo) " +
							" INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) ";
			
							if(cuentaPaciente>0)
								where1="  WHERE re.cuenta="+cuentaPaciente;
							else if(ingresoPaciente>0)
								where1=" where re.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+")";
							consulta=consulta+" "+where1;
		}
		if(con==null)
		{
			conLocal=UtilidadBD.abrirConexion();
		}
		else
		{
			conLocal=con;
		}
		if(!historico)
		{
			consulta="SELECT * FROM ("+consulta+") tabla order by campootro ASC, orden asc ";
		}
		else
		{
			consulta="SELECT * FROM ("+consulta+") tabla order by fecha desc,hora desc,campootro desc, orden asc ";
		}
		PreparedStatementDecorator ps=new PreparedStatementDecorator(conLocal, consulta);
		try
		{
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int codigoEncaAnterior=ConstantesBD.codigoNuncaValido;
			while(rs.next())
			{
				DtoResultadoLaboratorio dto=new DtoResultadoLaboratorio();
				dto.setCodigoCampo(rs.getInt("codigocampo"));
				dto.setEtiquietaCampo(rs.getString("etiquetacampo"));
				dto.setOrden(rs.getInt("orden"));
				dto.setCentroCosto(rs.getInt("centrocosto"));
				dto.setCampoOtro(rs.getString("campootro"));
				if(historico)
				{
					dto.setCodigoHistoEnca(rs.getInt("codigohistoenca"));
					dto.setValor(rs.getString("valor"));
					
					//cuadrando historicos.
					DtoHistoricoResultadoLaboratorios histo=null;
					if(codigoEncaAnterior!=rs.getInt("codigohistoenca"))
					{
						histo=new DtoHistoricoResultadoLaboratorios();
						Date fechaRegistro=rs.getDate("fecha");
						if(fechaRegistro!=null){
							SimpleDateFormat format=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
							String fechaString=format.format(fechaRegistro);
							histo.setFecha(fechaString);
						}
						histo.setHora(rs.getString("hora"));
						histo.setLoginUsuario(rs.getString("loginusuario"));
						histo.setCodigoHistoEnca(rs.getInt("codigohistoenca"));
						histo.setNombreUsuario(rs.getString("nombreusuario"));
						
					}
					else
					{
						histo=((DtoHistoricoResultadoLaboratorios)resultado.get(resultado.size()-1));
					}
					histo.getResultados().add(dto);
					if(codigoEncaAnterior!=rs.getInt("codigohistoenca"))
					{
						resultado.add(histo);
					}
					codigoEncaAnterior=rs.getInt("codigohistoenca");
				}
				else
				{
					resultado.add(dto);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		if(con==null)
		{
			UtilidadBD.closeConnection(conLocal);
		}
		return resultado;
	}





	public static int insertaResultadosLaboratorios(Connection con,
			int codigoEncabezado,
			ArrayList<Object> resultadoLaboratorios) 
	{
		String cadena="insert into manejopaciente.resultado_laboratorio_orden (codigo_pk,CODIGO_HISTO_ENCA,CODIGO_CAMPO,ETIQUIETA_CAMPO,ORDEN,CENTRO_COSTO,VALOR,CAMPO_OTRO) values(?,?,?,?,?,?,?,?)";
		// TODO Auto-generated method stub
		for(int i=0;i<resultadoLaboratorios.size();i++)
		{
			try
			{
				DtoResultadoLaboratorio dto=(DtoResultadoLaboratorio)resultadoLaboratorios.get(i);
				if(!dto.getValor().isEmpty())
				{
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadena);
					int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_res_lab_orden");
					ps.setInt(1, codigo);
					ps.setInt(2, codigoEncabezado);
					if(!UtilidadTexto.getBoolean(dto.getCampoOtro()))
						ps.setInt(3, dto.getCodigoCampo());
					else
						ps.setObject(3, null);
					ps.setString(4, dto.getEtiquietaCampo());
					ps.setInt(5, dto.getOrden());
					ps.setInt(6, dto.getCentroCosto());
					ps.setString(7, dto.getValor());
					ps.setString(8, dto.getCampoOtro());
					ps.executeUpdate();
				}
			}
			catch(Exception e)
			{
				Log4JManager.error("error",e);
				return ConstantesBD.codigoNuncaValido;
			}
			
		}
		return 1;
	}
	
	

    /**
     * @param con
     * @param codigoCuenta
     * @return
     */
    public static ArrayList<String> cargarOtrosDietaReproteHC(Connection con, int codigoCuenta)
    {
	
    	ArrayList<String> otrasDietas = new ArrayList<String>();
    	ArrayList<String> otrasDietasSuspension = new ArrayList<String>();
    	
    	
    	String consultaStr="SELECT ono.codigo,ono.descripcion FROM otro_nutricion_oral ono WHERE ono.codigo IN (SELECT otro_nutricion_oral AS nutricion_oral FROM detalle_otro_nutri_oral" +
    			" WHERE codigo_historico_dieta= (SELECT MAX (codigo_historico_dieta) AS codigo_histo  FROM orden_nutricion_oral ono INNER JOIN orden_dieta od " +
    			" ON (ono.codigo_historico_dieta=od.codigo_histo_enca)  INNER JOIN encabezado_histo_orden_m eh   ON (eh.codigo=od.codigo_histo_enca)  INNER JOIN ordenes_medicas om " +
    			"ON (eh.orden_medica=om.codigo) WHERE om.cuenta    =?  )  )";
    	
    	
    	String consultaFinalizarDieta ="  SELECT od.finalizar_dieta as suspender FROM orden_nutricion_oral ono  INNER JOIN orden_dieta od  ON (ono.codigo_historico_dieta=od.codigo_histo_enca)" +
    			" INNER JOIN encabezado_histo_orden_m eh  ON (eh.codigo=od.codigo_histo_enca) INNER JOIN ordenes_medicas om  ON (eh.orden_medica=om.codigo) " +
    			"WHERE om.cuenta    =?";

			
			try
			{
			logger.info("\n\n\n\n\n\n\n\n"+consultaStr.replace("?", codigoCuenta+"")+"\n\n\n\n\n\n\n");
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, codigoCuenta);
			ResultSet res = consultarNov.executeQuery();
			
			while(res.next()){
				otrasDietas.add(String.valueOf(res.getString("descripcion")));
			}
			
			
			
			PreparedStatementDecorator finalizarDietaPrepared =  new PreparedStatementDecorator(con.prepareStatement(consultaFinalizarDieta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			finalizarDietaPrepared.setInt(1, codigoCuenta);
			ResultSet res2 = finalizarDietaPrepared.executeQuery();
			Integer pos = new Integer (0);
			while(res2.next()){
				if(otrasDietas.size()>0){
					if(pos<otrasDietas.size()){
						String dato =otrasDietas.get(pos);
						otrasDietasSuspension.add(dato+"--"+String.valueOf(res2.getInt("suspender")));
					}else{
						otrasDietasSuspension.add(String.valueOf(res2.getInt("suspender")));
					}
				}else{
					otrasDietasSuspension.add(String.valueOf(res2.getInt("suspender")));
				}
				pos++;
			}
			
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado los �ltimos tipos de nutrici�n oral ingresados a la Orden Medica :"+e.toString());
			
			}    	
			
			return otrasDietasSuspension;
    }
	
	
    /**
	 * M�todo para asociar el encabezado historico de la orden
	 * con el detalle de la observaci�n de la orden para poder manejar
	 * las observaciones cronol�gicamente
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPkDetalleObservacion
	 * @param codigoPkEncabezado
	 * @throws SQLException
	 */	
	public static void asociarDetalleObservacionEncabezado (Connection con, int codigoPkDetalleObservacion, int codigoPkEncabezado) throws SQLException 
	{
		String cadena = "UPDATE ordenes.detalle_observacion_orden_med SET encabezado_histo_orden = ? WHERE id = ? ";
		PreparedStatement pst=null;
		try
		{
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);			
			pst.setInt(1, codigoPkEncabezado);		
			pst.setInt(2, codigoPkDetalleObservacion);			
			pst.executeUpdate();
		}
		catch(SQLException e){	
			Log4JManager.error(e);
			throw e;
		}	
		finally{
			if(pst!=null){
				pst.close();
			}
		}
	}
	
	/**
	 * Metodo encargado de actualizar la descripcion de un soporte respiratorio
	 * @param con
	 * @param codigoEncabezadoSoporteRespira
	 * @param descripcionSoporteRespiratorio
	 * @return
	 * @author hermorhu
	 */
	public static boolean actualizarDescripcionSoporteRespiratorio (Connection con, int codigoEncabezadoSoporteRespira, String descripcionSoporteRespiratorio) {
		
		PreparedStatementDecorator ps = null;  
				
		try {		
				
			ps = new PreparedStatementDecorator(con.prepareStatement(strActualizarDescripcionSoporteRespiratorio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//descripcion
			ps.setString(1, descripcionSoporteRespiratorio);
			//codigo Encabezado 
			ps.setInt(2, codigoEncabezadoSoporteRespira);
		
			if(ps.executeUpdate()>0)
				return  true;
			else
				return false;
		
		} catch (SQLException e){
			Log4JManager.error(e);
		} catch (Exception e) {
			Log4JManager.error(e);
		} finally {
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close PreparedStatement", se);
			}
		} 
		
		return false;
	}
	
	/**
	 * Metodo encargado de consultar el historico de descripciones de soporte respiratorio
	 * @param con
	 * @param codigoCuenta
	 * @return
	 * @author hermorhu
	 */
	public static String consultarDescripcionSoporteRespiratorio (Connection con, int codigoCuenta) {
		String resultado = "";
		PreparedStatementDecorator ps = null; 
		ResultSetDecorator rs = null;
		
		try {

			ps = new PreparedStatementDecorator(con.prepareStatement(strConsultarDescripcionSoporteRespiratorio, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) {
				resultado += rs.getString("descripcion"); 
			}
			
		} catch (SQLException e){
			Log4JManager.error(e);
		} catch (Exception e) {
			Log4JManager.error(e);
		} finally {
			try{
				if(ps != null){
					ps.close();
				}
				if(rs != null){
					rs.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet-PreparedStatement", se);
			}
		} 
		
		return resultado;
	}
}