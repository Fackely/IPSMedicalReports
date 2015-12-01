package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Víctor Hugo Gómez L.
 */

public class SqlBaseEspecialidadesDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseEspecialidadesDao.class);
	
	/**
	 * Cadena de Insercion de Especialidad
	 */
	private static final String strInsertEspecialidad = "INSERT INTO especialidades (" +
						"codigo," +
						"nombre," +
						"consecutivo," +
						"institucion," +
						"centro_costo_honorario," +
						"fecha_grabacion," +
						"hora_grabacion," +
						"tipo_especialidad," +
						"usuario_grabacion)" +
						"VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena de Modificacion Especialidad
	 */
	private static final String strUpdateEspecialidad = "UPDATE especialidades SET " +
						"consecutivo = ?, nombre = ?, centro_costo_honorario = ?, usuario_grabacion = ?, tipo_especialidad = ?, " +
						"hora_grabacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" , fecha_grabacion = CURRENT_DATE " +
						"WHERE codigo = ?";
	
	/**
	 * Cadena Consulta las especialidades
	 */
	private static final String strConsultaEspecialidades = "SELECT " + 
						"esp.codigo AS codigo, " +
						"esp.nombre AS descripcion, " + 
						"esp.consecutivo AS consecutivo, " +
						"esp.institucion AS institucion, " +
						"coalesce(esp.centro_costo_honorario,-1) AS cod_cen_costo, " +
						"esp.tipo_especialidad AS tipo_especialidad, " +
						"coalesce(cc.nombre,'') AS nombre_centro_costo " +
						"FROM especialidades esp " +
						"LEFT OUTER JOIN centros_costo cc ON (cc.codigo = esp.centro_costo_honorario) " + 
						"WHERE esp.institucion = ? AND esp.codigo>0 ";
	
	/**
	 * Cadena Busca Especialidad en especialidades_medicos
	 */
	private static final String strConsultaEspecialidadesMedicos = "SELECT " +
			"COUNT(codigo_especialidad) AS numregistros_esp_med " +
			"FROM especialidades_medicos " +
			"WHERE codigo_especialidad = ? ";
	
	/**
	 * Cadena Busca Especialidad en servicio
	 */
	private static final String strConsultaServicio = "SELECT " +
			"COUNT(especialidad) AS numregistros_ser " +
			"FROM servicios " +
			"WHERE especialidad = ? ";
	
	/**
	 * Cadena Busca Especialidad en Unidades Consulta
	 */
	private static final String strConsultaUnidadesConsulta = "SELECT " +
			"COUNT(especialidad) AS numregistros_uni_con " +
			"FROM unidades_consulta " +
			"WHERE especialidad = ? ";
	
	/**
	 * Cadena de Eliminacion de una Especificacion
	 */
	private static final String strDeleteEspecificacion = "DELETE FROM especialidades WHERE codigo = ?";
	
	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoEspecialidades dtoEspecialidades
	 */
	public static int insertarEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades) 
	{
		
		try{
			PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(strInsertEspecialidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_especialidades");
			pst.setInt(1,codigo);
			pst.setString(2, dtoEspecialidades.getDescripcion());
			pst.setString(3, dtoEspecialidades.getConsecutivo());
			pst.setInt(4, dtoEspecialidades.getCodigoInstitucion());
			
			if(dtoEspecialidades.getCodigoCentroCostoHonorario()!=ConstantesBD.codigoNuncaValido)
				pst.setInt(5, dtoEspecialidades.getCodigoCentroCostoHonorario());
			else
				pst.setNull(5,Types.INTEGER);
			
			pst.setString(6, dtoEspecialidades.getTipoEspecialidad());
			
			pst.setString(7, dtoEspecialidades.getUsuarioGrabacion());
						
			if(pst.executeUpdate()>0){
				pst.close();
				return codigo;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso de la Epecialidad !!!!!!!!!");
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;	
	}
	
	/**
	 * Modificacion Especialidad
	 * @param Connection con
	 * @param DtoEspecialidades dtoEpecialidades
	 * 
	 */
	public static int updateEspecialidad(Connection con, DtoEspecialidades dtoEspecialidades) 
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		try{
			PreparedStatementDecorator pst=  new PreparedStatementDecorator(con.prepareStatement(strUpdateEspecialidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoEspecialidades.getConsecutivo());
			pst.setString(2, dtoEspecialidades.getDescripcion());
			if(dtoEspecialidades.getCodigoCentroCostoHonorario()!=ConstantesBD.codigoNuncaValido)
				pst.setInt(3, dtoEspecialidades.getCodigoCentroCostoHonorario());
			else
				pst.setNull(3, Types.INTEGER);
			pst.setString(4, dtoEspecialidades.getUsuarioGrabacion());
			pst.setString(5, dtoEspecialidades.getTipoEspecialidad());
			pst.setInt(6, dtoEspecialidades.getCodigo());
			if(pst.executeUpdate()>0){
				pst.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion de la Especialidad !!!!!!!!!\n Query: "+strUpdateEspecialidad);
			logger.info("datos >>> consecutivo="+dtoEspecialidades.getConsecutivo()+" - descripcion="+dtoEspecialidades.getDescripcion()+" - codigo_centro_costo="+dtoEspecialidades.getCodigoCentroCostoHonorario()+" - codigo="+dtoEspecialidades.getCodigo()+" - usuario >>>"+dtoEspecialidades.getUsuarioGrabacion());
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoEspecialidades> cargarListadoEspecialidades(Connection con, HashMap parametros) 
	{
		ArrayList<DtoEspecialidades> list = new ArrayList<DtoEspecialidades>();
		String cadena = strConsultaEspecialidades+" ORDER BY esp.nombre ASC";
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{			
				DtoEspecialidades dto = new DtoEspecialidades();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setCodigoInstitucion(rs.getInt("institucion"));
				dto.setCodigoCentroCostoHonorario(rs.getInt("cod_cen_costo"));
				dto.setNombreCentroCosto(rs.getString("nombre_centro_costo"));
				dto.setTipoEspecialidad(rs.getString("tipo_especialidad"));
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarListadoEspecilidades !!!!!!\n La Consulta: "+cadena);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Metodo que Elimina  una Especialida 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static int deleteEspecialidades(Connection con, HashMap parametros) 
	{
		ArrayList<DtoEspecialidades> list = new ArrayList<DtoEspecialidades>();		
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strDeleteEspecificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("codigo_especialidad")+""));
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
			pst.close();
		}catch(SQLException e){
			logger.info("Error Eliminado Especialidad !!!!!!\n La Consulta: "+strDeleteEspecificacion);
			logger.info("datos >>>>> codigo Especialidad="+parametros.get("codigo_especialidad"));
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Consulta listado de Especialidades 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoEspecialidades> busquedaAvanzadaEspecialidades(Connection con, HashMap parametros) 
	{
		ArrayList<DtoEspecialidades> list = new ArrayList<DtoEspecialidades>();
		String cadena = strConsultaEspecialidades;
		String order = " ORDER BY esp.nombre ASC";
		try
		{
			if(parametros.containsKey("consecutivo"))
				cadena+=" AND esp.consecutivo = '"+parametros.get("consecutivo")+"'";
			if(parametros.containsKey("descripcion"))
				cadena+=" AND esp.nombre LIKE '%"+parametros.get("descripcion").toString().toUpperCase()+"%'";
			if(parametros.containsKey("centro_costo"))
				cadena+=" AND esp.centro_costo_honorario= "+parametros.get("centro_costo");
			if(parametros.containsKey("tipoEspecialidad"))
				cadena+=" AND esp.tipo_especialidad= '"+parametros.get("tipoEspecialidad")+"'";
			cadena+=order;
			logger.info("cadeana >>> "+cadena);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{			
				DtoEspecialidades dto = new DtoEspecialidades();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setCodigoInstitucion(rs.getInt("institucion"));
				dto.setCodigoCentroCostoHonorario(rs.getInt("cod_cen_costo"));
				dto.setNombreCentroCosto(rs.getString("nombre_centro_costo"));
				dto.setTipoEspecialidad(rs.getString("tipo_especialidad"));
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarListadoEspecilidades !!!!!!\n La Consulta: "+cadena);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Consulta listado de Especialidades y Verifica que especialidad esta Siendo Usada 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoEspecialidades> verificarEspecialidadesUsadas(Connection con, ArrayList<DtoEspecialidades> list) 
	{
		try
		{
			for(int i=0;i<list.size();i++)
			{
				PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaEspecialidadesMedicos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, list.get(i).getCodigo());
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next()){
					if(rs.getInt("numregistros_esp_med")>0){
						list.get(i).setMostrar(ConstantesBD.acronimoNo);
					}else{
						PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con.prepareStatement(strConsultaServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst1.setInt(1, list.get(i).getCodigo());
						ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
						if(rs1.next()){
							if(rs1.getInt("numregistros_ser")>0){
								list.get(i).setMostrar(ConstantesBD.acronimoNo);
							}else{
								PreparedStatementDecorator pst2 = new PreparedStatementDecorator(con.prepareStatement(strConsultaUnidadesConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								pst2.setInt(1, list.get(i).getCodigo());
								ResultSetDecorator rs2 = new ResultSetDecorator(pst2.executeQuery());
								if(rs2.next()){
									if(rs2.getInt("numregistros_uni_con")>0){
										list.get(i).setMostrar(ConstantesBD.acronimoNo);
									}
								}
							}
						}
					}
				}
			}
		}catch(SQLException e){
			logger.info("Error verificarEspecialidadesUsadas !!!!!!\n La Consulta: ");
			logger.error(e);			
		}
		return list;
	}
}
