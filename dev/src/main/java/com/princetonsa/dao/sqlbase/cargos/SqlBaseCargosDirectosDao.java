package com.princetonsa.dao.sqlbase.cargos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.ordenes.DtoProcedimiento;



/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad CARGOS DIRECTOS 
 */
public class SqlBaseCargosDirectosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCargosDirectosDao.class);
	
	/**
	 * Cadena para insertar información del cargo directo de historia clínica
	 */
	private static final String insertarCargoDirectoHC_Str = "INSERT INTO cargos_directos_hc (" +
		" codigo," +
		" fecha_solicitud," +
		" hora_solicitud," +
		" codigo_servicio," +
		" tipo_servicio," +
		" causa_externa," +
		" maneja_rips," +
		" finalidad_consulta," +
		" finalidad_procedimiento," +
		" observaciones," +
		" institucion," +
		" usuario_modifica," +
		" fecha_modifica," +
		" hora_modifica," +
		" tipo, " +
		" personal_atiende, " +
		" forma_realizacion " +
		") values(" +
		" ?,?,?,?,?,?,?,?,?," +
		" ?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para actualizar la informacion del cargo directo de historia clinica
	 */
	private static final String actualizarCargoDirectoHC_Str = "UPDATE cargos_directos_hc SET " +
		"fecha_solicitud = ?, " +
		"hora_solicitud = ?, " +
		"causa_externa = ?, " +
		"maneja_rips = ?, " +
		"finalidad_consulta = ?, " +
		"finalidad_procedimiento = ?, " +
		"institucion = ?, " +
		"usuario_modifica = ?, " +
		"fecha_modifica = current_date, " +
		"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
		"WHERE codigo = ?";
	
	/**
	 * Cadena implementada para realizar la eliminación de los diagnósitcos de un cargo direcot
	 */
	private static final String eliminarDiagnosticoCargoDirectoHC_Str = "DELETE FROM diag_cargos_directos_hc WHERE codigo_cargo_directo = ?";
	
	/**
	 * Cadena implementada para realizar la inserción de los diagnósticos de un cargo directo
	 */
	private static final String insertarDiagnosticoCargoDirectoHC_Str = "INSERT INTO diag_cargos_directos_hc (" +
		"codigo," +
		"codigo_cargo_directo," +
		"acronimo_diagnostico," +
		"tipo_cie_diagnostico," +
		"tipo_diagnostico," +
		"principal," +
		"complicacion) " +
		"values (?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar un cargo directo de servicio
	 */
	private static final String insertarCargoDirecto_Str = "INSERT INTO cargos_directos " +
								"(numero_solicitud, " +
								"usuario, " +
								"tipo_recargo, " +
								"servicio_solicitado, " +
								"codigo_datos_hc, " +
								"afecta_inventarios," +
								"fecha_ejecucion) VALUES (?, ?, ?, ?, ?, ? , ?)";
	
	/**
	 * Cadena para actualizar un cargo directo de servicio
	 */
	private static final String actualizarCargoDirecto_Str = "UPDATE cargos_directos SET codigo_datos_hc = ? WHERE numero_solicitud = ?";
	
	
	/**
	 * 
	 */
	private static final String insertarEspecialidadProfesionalRespondeStr = "UPDATE solicitudes SET especialidad_solicitada = ?  WHERE numero_solicitud = ? ";
	
	/**
	 * Cadena para cargar la informacion HC del cargo directo
	 */
	private static final String cargarInfoHCCargoDirecto_Str = "SELECT "+ 
		"cdh.codigo AS codigo, "+ 
		"to_char(cdh.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud, "+
		"cdh.hora_solicitud as hora_solicitud, "+
		"cdh.codigo_servicio as codigo_servicio, "+
		"cdh.tipo_servicio as tipo_servicio, "+
		"coalesce(cdh.causa_externa,"+ConstantesBD.codigoNuncaValido+") as codigo_causa_externa, "+
		"coalesce(getnombrecausaexterna(cdh.causa_externa),'') as nombre_causa_externa, "+
		"cdh.maneja_rips as maneja_rips, "+ 
		"coalesce(cdh.finalidad_consulta,'') AS codigo_finalidad_consulta, "+
		"coalesce(getnomfinalidadconsulta(cdh.finalidad_consulta),'') as nombre_finalidad_consulta, "+
		"coalesce(cdh.finalidad_procedimiento,"+ConstantesBD.codigoNuncaValido+") as codigo_finalidad_procedimiento, "+
		"coalesce(getnomfinalidadservicio(cdh.finalidad_procedimiento),'') AS nombre_finalidad_procedimiento, "+
		"coalesce(cdh.observaciones,'') as observaciones, "+
		"cdh.institucion as codigo_institucion, "+
		"cdh.tipo as tipo, "+ 
		"coalesce(cdh.personal_atiende,"+ConstantesBD.codigoNuncaValido+") AS personal_atiende, "+
		"coalesce(cdh.forma_realizacion,"+ConstantesBD.codigoNuncaValido+") AS forma_realizacion "+ 
		"FROM cargos_directos_hc cdh "+ 
		"INNER JOIN cargos_directos cd ON(cd.codigo_datos_hc=cdh.codigo) "+ 
		"WHERE "+ 
		"cd.numero_solicitud = ?";
	
	/**
	 * Cadena para cargar los diagnósticos del cargo directo
	 */
	private static final String cargarDiagnosticosCargoDirecto_Str = "SELECT " +
		"codigo," +
		"acronimo_diagnostico," +
		"tipo_cie_diagnostico, " +
		"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) AS nombre_diagnostico, " +
		"coalesce(tipo_diagnostico,'') as tipo_diagnostico," +
		"principal," +
		"complicacion " +
		"from diag_cargos_directos_hc " +
		"WHERE codigo_cargo_directo = ?";
	
	/**
	 * Método implementado para insertar regitros de cargos directos incluyendo su información de historia clínica
	 * @param con
	 * @param cargoDirecto
	 * @param cargoDirectoHC
	 * @return
	 */
	public static int insertar(Connection con,ArrayList<DtoCargoDirecto> arregloCargoDirecto,DtoCargoDirectoHC cargoDirectoHC)
	{
		int resp0 = 0, resp1 = 0;
		try
		{
			PreparedStatementDecorator pst = null;
			
			//***********INSERCIÓN DE LOS DATOS CARGOS DIRECTOS DE HISTORIA CLINICA*********************************************
			if(cargoDirectoHC.isCargado())
			{
				int codigoCargoDirecto = ConstantesBD.codigoNuncaValido;
				
				//Si no existe en la base de datos se inserta
				if(!cargoDirectoHC.isExisteBaseDatos())
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarCargoDirectoHC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					codigoCargoDirecto = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cargos_directos_hc");
					
					pst.setDouble(1,Utilidades.convertirADouble(codigoCargoDirecto+""));
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargoDirectoHC.getFechaSolicitud())));
					pst.setString(3,cargoDirectoHC.getHoraSolicitud());
					pst.setInt(4,cargoDirectoHC.getCodigoServicio());
					pst.setString(5,cargoDirectoHC.getCodigoTipoServicio());
					if(cargoDirectoHC.getCodigoCausaExterna()>0)
						pst.setInt(6, cargoDirectoHC.getCodigoCausaExterna());
					else
						pst.setNull(6,Types.INTEGER);
					pst.setString(7,cargoDirectoHC.isManejaRips()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					if(!cargoDirectoHC.getCodigoFinalidadConsulta().equals(""))
						pst.setString(8, cargoDirectoHC.getCodigoFinalidadConsulta());
					else
						pst.setNull(8,Types.VARCHAR);
					if(cargoDirectoHC.getCodigoFinalidadProcedimiento()>0)
						pst.setInt(9,cargoDirectoHC.getCodigoFinalidadProcedimiento());
					else
						pst.setNull(9,Types.INTEGER);
					pst.setString(10,cargoDirectoHC.getObservaciones());
					pst.setInt(11,cargoDirectoHC.getCodigoInstitucion());
					pst.setString(12,cargoDirectoHC.getLoginUsuarioModifica());
					if(!cargoDirectoHC.getFechaModifica().equals(""))
						pst.setDate(13,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargoDirectoHC.getFechaModifica())));
					else
						pst.setDate(13, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
					if(!cargoDirectoHC.getHoraModifica().equals(""))
						pst.setString(14,cargoDirectoHC.getHoraModifica());
					else
						pst.setString(14, UtilidadFecha.getHoraActual(con));
					pst.setString(15,cargoDirectoHC.getTipo());
					if(cargoDirectoHC.getPersonalAtiende()>0)
						pst.setInt(16,cargoDirectoHC.getPersonalAtiende());
					else
						pst.setNull(16,Types.INTEGER);
					if(cargoDirectoHC.getFormaRealizacion()>0)
						pst.setInt(17,cargoDirectoHC.getFormaRealizacion());
					else
						pst.setNull(17,Types.INTEGER);
				}
				//Si ya existe se actualiza
				else
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarCargoDirectoHC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					
					pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargoDirectoHC.getFechaSolicitud())));
					pst.setString(2,cargoDirectoHC.getHoraSolicitud());
					if(cargoDirectoHC.getCodigoCausaExterna()>0)
						pst.setInt(3, cargoDirectoHC.getCodigoCausaExterna());
					else
						pst.setNull(3,Types.INTEGER);
					pst.setString(4,cargoDirectoHC.isManejaRips()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					if(!cargoDirectoHC.getCodigoFinalidadConsulta().equals(""))
						pst.setString(5, cargoDirectoHC.getCodigoFinalidadConsulta());
					else
						pst.setNull(5,Types.VARCHAR);
					if(cargoDirectoHC.getCodigoFinalidadProcedimiento()>0)
						pst.setInt(6,cargoDirectoHC.getCodigoFinalidadProcedimiento());
					else
						pst.setNull(6,Types.INTEGER);
					pst.setInt(7,cargoDirectoHC.getCodigoInstitucion());
					pst.setString(8,cargoDirectoHC.getLoginUsuarioModifica());
					pst.setDouble(9, Utilidades.convertirADouble(cargoDirectoHC.getCodigo()));
					codigoCargoDirecto = Utilidades.convertirAEntero(cargoDirectoHC.getCodigo());
				}
				
				resp0 = pst.executeUpdate();
				
				
				pst.close();
				
				
				if(resp0>0)
				{
					
					//Si el cargo directo ya existía en la base de datos, se eliminan sus diagnósticos antiguos
					if(cargoDirectoHC.isExisteBaseDatos())
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarDiagnosticoCargoDirectoHC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setDouble(1,Utilidades.convertirADouble(codigoCargoDirecto+""));
						pst.executeUpdate();
						pst.close();
					}
					
					
					//Se insertan los diagnósticos
					for(DtoDiagnosticosCargoDirectoHC diagTemp:cargoDirectoHC.getDiagnosticos())
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticoCargoDirectoHC_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						int codDiagCargoDirecto = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_diag_cargos_dir_hc");
						
						pst.setDouble(1,Utilidades.convertirADouble(codDiagCargoDirecto+""));
						pst.setDouble(2,Utilidades.convertirADouble(codigoCargoDirecto+""));
						pst.setString(3,diagTemp.getAcronimoDiagnostico());
						pst.setInt(4,diagTemp.getCieDiagnostico());
						
						if(!diagTemp.getCodigoTipoDiagnostico().equals(""))
							pst.setString(5,diagTemp.getCodigoTipoDiagnostico());
						else
							pst.setNull(5,Types.VARCHAR);						
						
						pst.setString(6,diagTemp.isPrincipal()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						pst.setString(7,diagTemp.isComplicacion()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						
						//Si sucede un error se deja de realizar las inserciones de diagnosticos
						if(resp0>0)
							resp0 = pst.executeUpdate();
						pst.close();
					}
					
					//Se verifica si hasta ahora todo ha sido exitoso
					if(resp0>0)
					{
						//Se alimentan todos los elementos de cargos directos con el nuevo codigo de cargo directo hc
						for(DtoCargoDirecto cargoTemp:arregloCargoDirecto)
							cargoTemp.setCodigoDatosHC(codigoCargoDirecto+"");
						
						resp0 = codigoCargoDirecto;
					}
					
					
				}
			}
			else
				resp0 = 1;
			//***************************************************************************************************
			
			//Solo se continua si todo va bien
			if(resp0>0)
			{
				//************INSERCION DE LOS CARGOS DIRECTOS*********************************************
				resp1 = 1;
				for(DtoCargoDirecto cargoTemp:arregloCargoDirecto)
				{
					if(!cargoTemp.isExisteBaseDatos())
					{
					
						pst =  new PreparedStatementDecorator(con.prepareStatement(insertarCargoDirecto_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
						pst.setInt(1,Utilidades.convertirAEntero(cargoTemp.getNumeroSolicitud()));
						pst.setString(2,cargoTemp.getLoginUsuario());
						if(cargoTemp.getCodigoTipoRecargo()>0)
							pst.setInt(3,cargoTemp.getCodigoTipoRecargo());
						else
							pst.setNull(3,Types.INTEGER);
						if(cargoTemp.getCodigoServicioSolicitado()>0)
							pst.setInt(4,cargoTemp.getCodigoServicioSolicitado());
						else
							pst.setNull(4,Types.INTEGER);
						if(!cargoTemp.getCodigoDatosHC().equals(""))
							pst.setDouble(5,Utilidades.convertirADouble(cargoTemp.getCodigoDatosHC()));
						else
							pst.setNull(5,Types.NUMERIC);
						//Cuando es un cargo de articulos se debe insertar afecta inventarios
						if(cargoTemp.getCodigoServicioSolicitado()<=0)
							pst.setString(6, cargoTemp.isAfectaInventarios()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						else
							pst.setNull(6,Types.VARCHAR);
						if(!UtilidadTexto.isEmpty(cargoTemp.getFechaEjecucion()))
							pst.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(cargoTemp.getFechaEjecucion())));
						else
							pst.setObject(7, null);
					}
					else
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarCargoDirecto_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setDouble(1,Utilidades.convertirADouble(cargoTemp.getCodigoDatosHC()));
						pst.setInt(2,Utilidades.convertirAEntero(cargoTemp.getNumeroSolicitud()));
					}
					
					//Se verifica si hasta ahora todo ha sido exitoso
					if(resp1>0){
						resp1 = pst.executeUpdate();
					}
					pst.close();
						
				}
				
				//Si no fue exitoso se cambia el valor de la variable resp0
				if(resp1<=0)
					resp0 = 0;
				//*****************************************************************************************
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar: "+e);
			resp0 = 0;
		}
		
		return resp0;
	}
	
	/**
	 * Método para cargar la información e historia clínica de un cargo directo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoCargoDirectoHC consultarInformacionHC(Connection con,String numeroSolicitud)
	{
		DtoCargoDirectoHC dtoCargo = new DtoCargoDirectoHC();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarInfoHCCargoDirecto_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				dtoCargo.setCargado(true);
				dtoCargo.setExisteBaseDatos(true);
				dtoCargo.setCodigo(rs.getString("codigo"));
				dtoCargo.setFechaSolicitud(rs.getString("fecha_solicitud"));
				dtoCargo.setHoraSolicitud(rs.getString("hora_solicitud"));
				dtoCargo.setCodigoServicio(rs.getInt("codigo_servicio"));
				dtoCargo.setCodigoTipoServicio(rs.getString("tipo_servicio"));
				dtoCargo.setCodigoCausaExterna(rs.getInt("codigo_causa_externa"));
				dtoCargo.setNombreCausaExterna(rs.getString("nombre_causa_externa"));
				dtoCargo.setManejaRips(UtilidadTexto.getBoolean(rs.getString("maneja_rips")));
				dtoCargo.setCodigoFinalidadConsulta(rs.getString("codigo_finalidad_consulta"));
				dtoCargo.setNombreFinalidadConsulta(rs.getString("nombre_finalidad_consulta"));
				dtoCargo.setCodigoFinalidadProcedimiento(rs.getInt("codigo_finalidad_procedimiento"));
				dtoCargo.setNombreFinalidadProcedimiento(rs.getString("nombre_finalidad_procedimiento"));
				dtoCargo.setObservaciones(rs.getString("observaciones"));
				dtoCargo.setCodigoInstitucion(rs.getInt("codigo_institucion"));
				dtoCargo.setTipo(rs.getString("tipo"));
				dtoCargo.setPersonalAtiende(rs.getInt("personal_atiende"));
				dtoCargo.setFormaRealizacion(rs.getInt("forma_realizacion"));
				
				PreparedStatementDecorator pst1 =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosCargoDirecto_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst1.setDouble(1, Utilidades.convertirADouble(dtoCargo.getCodigo()));
				ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
				
				while(rs1.next())
				{
					DtoDiagnosticosCargoDirectoHC diagCargo = new DtoDiagnosticosCargoDirectoHC();
					
					diagCargo.setCodigo(rs1.getString("codigo"));
					diagCargo.setCodigoCargoDirecto(dtoCargo.getCodigo());
					diagCargo.setAcronimoDiagnostico(rs1.getString("acronimo_diagnostico"));
					diagCargo.setCieDiagnostico(rs1.getInt("tipo_cie_diagnostico"));
					diagCargo.setNombreDiagnostico(rs1.getString("nombre_diagnostico"));
					diagCargo.setCodigoTipoDiagnostico(rs1.getString("tipo_diagnostico"));
					diagCargo.setPrincipal(UtilidadTexto.getBoolean(rs1.getString("principal")));
					diagCargo.setComplicacion(UtilidadTexto.getBoolean(rs1.getString("complicacion")));
					dtoCargo.getDiagnosticos().add(diagCargo);
				}
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarInformacionHC: "+e);
		}
		return dtoCargo;
	}
	
	/**
	 * M&eacute;todo encargado de buscar los servicios asociados a las 
	 * solicitudes generadas de cargos directos de sevicios
	 * @author Diana Carolina G
	 */
	public static DtoProcedimiento buscarServiciosCargosDirectos(Connection con, int numeroSolicitud, int codigoTarifario)
	{
		DtoProcedimiento servicioSolicitud= new DtoProcedimiento();
		
		String consulta="select  dcarg.cantidad_cargada as cantidadCargada, " +
				"dcarg.servicio as codigoPkServicio, " +
				"rs.descripcion as descripcionServicio, " +
				"rs.codigo_propietario as codigoPropietarioServicio, " +
				"dcarg.solicitud as numeroSolicitud " +
				"from det_cargos dcarg left outer join " +
				"servicios serv on (dcarg.servicio=serv.codigo) " +
				"inner join referencias_servicio rs on (rs.servicio=serv.codigo and rs.tipo_tarifario = " + codigoTarifario + ") " +
				"where dcarg.solicitud = " + numeroSolicitud;
		
		try {
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				servicioSolicitud.setNumeroSolicitud(rs.getInt("numeroSolicitud"));
				servicioSolicitud.setCodigoServicioSolicitado(rs.getInt("codigoPkServicio"));
				servicioSolicitud.setNombreServicioSolicitado(rs.getString("descripcionServicio"));
				servicioSolicitud.setCantidadServicioSolicitado(rs.getInt("cantidadCargada"));
			}
			
		} catch (Exception e) {
			logger.warn(e + " Error en buscarServiciosCargosDirectos de : SqlBaseCargosDirectosDao "+e.toString());
		}
		
		return servicioSolicitud;
		
		
	}
}
