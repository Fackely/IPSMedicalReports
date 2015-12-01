/*
 * @(#)SqlBaseNotasGeneralesEnfermeriaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */


package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de las Notas generales de enfermeria
 */
public class SqlBaseNotasGeneralesEnfermeriaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseNotasGeneralesEnfermeriaDao.class);
	
	/**
	 * Statement para consultar las notas generales de enfermeria segun el numero de solicitud de cirugia
	 */
	private final static String consultarNotasPacienteStr=" SELECT ne.codigo as codigo, " +
														  " ne.numero_solicitud as numeroSolicitud, "+
														  " (to_char(ne.fecha_nota,'"+ConstantesBD.formatoFechaAp +"')||' - '||ne.hora_nota) as fechaHoraNota, "+
														  " ne.nota as nota, "+
														  " getnombrepersona(ne.codigo_enfermera) as enfermera "+
														  " FROM notas_enfermeria ne "+
														  " WHERE ne.numero_solicitud=? "+
														  " ORDER BY ne.fecha_nota desc ";
	
	
	/**
	 * Método para cargar todas las notas asociadas a un solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarNotasPaciente(Connection con, int numeroSolicitud)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarNotasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, numeroSolicitud);
			logger.info("-->"+consultarNotasPacienteStr+"----"+numeroSolicitud);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de las notas  : SqlBaseNotasGeneralesEnfermeriaDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para insertar una nota
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaNota
	 * @param horaNota
	 * @param nota
	 * @param codigoEnfermera
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @return
	 */
	public static int insertarNota(Connection con, int numeroSolicitud, String fechaNota, String horaNota, String nota, int codigoEnfermera, int institucion, String fechaGrabacion, String horaGrabacion, String insertarNotaStr)
	{
	   int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarNotaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, numeroSolicitud);
					ps.setDate(2, Date.valueOf(fechaNota));
					ps.setString(3, horaNota);
					ps.setString(4, nota);
					ps.setInt(5, codigoEnfermera);
					ps.setInt(6, institucion);
					ps.setDate(7, Date.valueOf(fechaGrabacion));
					ps.setString(8, horaGrabacion);
					
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de la Nota: SqlBaseNotasGeneralesEnfermeriaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * Metodo que consulta lista de notas enfermeria para una solicitud dada
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public static List<NotaEnfermeriaDto> listaNotasEnfermeria(Connection con, Integer numeroSolicitud, boolean esAscendente) throws BDException {
		
		String ordenamiento="DESC";
		if(esAscendente){
			ordenamiento="ASC";
		}
	
		List<NotaEnfermeriaDto> listaNotasEnfermeria = new ArrayList<NotaEnfermeriaDto>();
		StringBuffer consulta=new StringBuffer(" SELECT ne.codigo as codigo, ")
										.append(" ne.numero_solicitud as numeroSolicitud, ")
										//.append(" (to_char(ne.fecha_nota,'"+ConstantesBD.formatoFechaAp +"')||' - '||ne.hora_nota) as fechaHoraNota, ")
										.append(" ne.fecha_nota as fechaNota, ")
										.append(" ne.hora_nota as horaNota, ")
										.append(" ne.fecha_grabacion as fechaGrabacion, ")
										.append(" ne.hora_grabacion as horaGrabacion, ")
										.append(" ne.nota as nota, ")
										.append(" getnombrepersona(ne.codigo_enfermera) as enfermera, ")
										.append(" administracion.getespecialidadesmedico1(ne.codigo_enfermera,', ') as especialidadesMedico, ")
										.append(" administracion.getdatosmedico1(ne.codigo_enfermera) as datosMedico ")
										.append(" FROM notas_enfermeria ne ")
										.append(" WHERE ne.numero_solicitud=? ")
										.append(" ORDER BY ne.fecha_nota "+ordenamiento+" ");
		
		String cadena=consulta.toString();
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, numeroSolicitud);
			logger.info("-->"+cadena+"----"+numeroSolicitud);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next()){
				NotaEnfermeriaDto nota = new NotaEnfermeriaDto(
						rs.getInt("codigo"),
						rs.getInt("numeroSolicitud"), 
						rs.getString("nota") , 
						rs.getDate("fechaNota") , 
						rs.getString("horaNota"), 
						rs.getDate("fechaGrabacion"),
						rs.getString("horaGrabacion"),
						rs.getString("enfermera"),
						rs.getString("especialidadesMedico"),
						rs.getString("datosMedico"));
				listaNotasEnfermeria.add(nota);
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de las notas  : SqlBaseNotasGeneralesEnfermeriaDao "+e.toString());
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return listaNotasEnfermeria;
	}
	
	
	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param actualizacion
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public static void guardarNotaEnfermeria(Connection con, NotaEnfermeriaDto dto)throws BDException{

		
		StringBuffer insert=new StringBuffer(" INSERT INTO notas_enfermeria ")
										.append(" (codigo, numero_solicitud, fecha_nota, hora_nota, nota, codigo_enfermera, institucion, fecha_grabacion, hora_grabacion) ")
										.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )");

		PreparedStatement ps=null;
	
		String cadena=insert.toString();
		try {
			ps=con.prepareStatement(cadena);
			
			int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "enfermeria.seq_not_enfermeria");
			
			ps.setInt(1, secuencia);
			ps.setInt(2, dto.getNumeroSolicitud());
			ps.setDate(3, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));			
			ps.setString(4, UtilidadFecha.getHoraActual(con));
			ps.setString(5, dto.getDescripcion());
			ps.setInt(6, dto.getCodEnfermera());
			ps.setInt(7, dto.getInstitucion());
			ps.setDate(8, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
			ps.setString(9, UtilidadFecha.getHoraActual(con));
			
			int resultado=ps.executeUpdate();
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}	
}