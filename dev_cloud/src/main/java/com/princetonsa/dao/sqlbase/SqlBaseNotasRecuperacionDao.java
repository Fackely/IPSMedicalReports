/*
 * Creado en Nov 23, 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.clonacion.UtilidadClonacion;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.salascirugia.CampoNotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.TipoCampoNotaRecuperacionDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 23 /Nov/ 2005
 */
public class SqlBaseNotasRecuperacionDao
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseNotasRecuperacionDao.class);
	
	/**
	 *  Sentencia SQL para actualizar una nota de recuperacion
	 */
	private final static String actualizarNotaRecuperacionStr =" UPDATE notas_recuperacion_general " +
															   " SET medicamentos =?," +
															   " observaciones=?  "+
															   " WHERE numero_solicitud=? ";
	
	private final static String insertarNotaRecuperacionBasicaStr=" INSERT INTO notas_recuperacion_general "+
																  " (numero_solicitud, " +
																  " medicamentos, " +
																  " observaciones) "+
																  " VALUES (?,?,?) ";
	
	
	public final static String existeSolicitudStr=" SELECT count(1)  as solicitud "+
												  " FROM notas_recuperacion_general " +
												  " WHERE numero_solicitud=? ";
	
	/**
	 * Cadena con el statement necesario para consultar el historico de las notas de recuperacion
	 */
	private static final String consultarHistoricoNotasRecuperacionStr=" SELECT dnr.numero_solicitud as numeroSolicitud, " +
																	   " to_char(dnr.fecha_recuperacion, '"+ConstantesBD.formatoFechaAp+"') as fechaRecuperacion, " +
																	   " dnr.hora_recuperacion as horaRecuperacion, " +
																	   " (to_char(dnr.fecha_recuperacion,'"+ConstantesBD.formatoFechaAp+"')||' - '||substr(dnr.hora_recuperacion, 0,6)) as fechaHoraRecuperacion, "+
																	   " dnr.nota_recup as codigoRelacion, " +
																	   " CASE WHEN dnr.valor IS NULL OR dnr.valor LIKE'%null%' THEN   ' ' ELSE dnr.valor  END as valorNota , "+
																	   " to_char(dnr.fecha_grabacion, '"+ConstantesBD.formatoFechaAp+"') as fechaGrabacion, " +
																	   " dnr.hora_grabacion as horaGrabacion, " +
																	   " administracion.getnombremedico(dnr.codigo_enfermera) as enfermera " +
																	   " FROM det_notas_recuperacion dnr " +
																	   " WHERE dnr.nota_recup=? " +
																	   " AND dnr.numero_solicitud=? " +
																	   " ORDER BY dnr.fecha_recuperacion DESC, dnr.hora_recuperacion DESC ";
	
	/**
	 * Cadena para consultar los tipo de notas de recuperacion parametrrizados
	 */
	private static final String consultarTiposNotasRecuperacionStr=" SELECT pnr.codigo as codigoRelacion, " +
																   " pnr.tipo as codigoTipo, " +
																   " tnr.descripcion as nombreTipo, " +
																   " pnr.campo as codigoCampo, " +
																   " (SELECT (select count(1) from param_notas_recup_inst pnri where pnri.tipo=pnr.tipo) from param_notas_recup_inst p where p.codigo=pnr.codigo) as \"rows\", "+
																   " cnr.descripcion as nombreCampo " +
																   " FROM param_notas_recup_inst pnr " +
																   " INNER JOIN tipos_notas_recuperacion tnr ON(pnr.tipo=tnr.codigo) " +
																   " INNER JOIN campos_notas_recuperacion cnr ON(pnr.campo=cnr.codigo) " +
																   " WHERE pnr.institucion=? AND " +
																   " pnr.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
																   " ORDER BY pnr.codigo ";
	
	
	/***
	 * Statement para consultar las observaciones ingresadas en las notas de recuperacion
	 */
	private static final String consultarObservacionesGeneralesStr=" SELECT nrg.observaciones  as observacionesGenerales " +
																   " FROM notas_recuperacion_general nrg " +
																   " WHERE nrg.numero_solicitud=? ";
	
	/**
	 * Statement para consultar los medicamentos ingresados en las notas de recuperacion
	 */
	private static final String consultarMedicamentosGeneralesStr=" SELECT nrg.medicamentos as medicamentosGenerales " +
																  " FROM notas_recuperacion_general nrg "+
																  " WHERE nrg.numero_solicitud=? ";
	
	/**
	 * Statement para consultar todas las fechas donde se han hecho notas de recuperacion
	 */
	private static final String consultarFechasRecuperacionStr=" SELECT (to_char(dnr.fecha_recuperacion,'DD/MM/YYYY')||' - '||dnr.hora_recuperacion) as fechaHoraRecuperacion  FROM det_notas_recuperacion dnr  WHERE dnr.numero_solicitud=? AND dnr.nota_recup=1 ORDER BY  dnr.fecha_recuperacion DESC, dnr.hora_recuperacion DESC ";
	
											/**" SELECT (to_char(dnr.fecha_recuperacion,'"+ConstantesBD.formatoFechaAp+"')||' - '||dnr.hora_recuperacion) as fechaHoraRecuperacion " +
													           " FROM det_notas_recuperacion as dnr "+
															   " WHERE dnr.numero_solicitud=? "+
															   " GROUP BY dnr.fecha_recuperacion,dnr.hora_recuperacion ORDER BY  dnr.fecha_recuperacion DESC, dnr.hora_recuperacion DESC ";**/
	
	/**
	 * Método para insertar una nota de recuperación básica
	 * @param con
	 * @param numeroSolicitud
	 * @param medicamentos
	 * @param observacionesGrales
	 * @return
	 */
	public static int insertarNotaRecuperacion( Connection con, int numeroSolicitud, String medicamentos, String observacionesGrales) throws SQLException
	{
		int resp=0;
		ResultSetDecorator rs;
		int temp=0;
		try
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(existeSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,numeroSolicitud);
				rs=new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					temp=rs.getInt("solicitud");
				}
				
				if(temp>0)
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(actualizarNotaRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, medicamentos);
					ps.setString(2, observacionesGrales);
					ps.setInt(3, numeroSolicitud);
					resp=ps.executeUpdate();

				}
				else
				{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarNotaRecuperacionBasicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, numeroSolicitud);
					ps.setString(2, observacionesGrales);
					ps.setString(3, medicamentos);
					resp=ps.executeUpdate();
				}
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la inserción de datos basicos de un nota [SqlBaseNotasRecuperacionDao]:"+e.toString() );
				resp=0;
			}
			return resp;
	}
	
	
	/**
	 * Método para insertar el detalle completo de una nota de recuperación
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaRecuperacion
	 * @param horaRecuperacion
	 * @param notaRecuperacion
	 * @param valorNota
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param codigoEnfermera
	 * @param institucion
	 * @param insertarDetalleNotasRecuperacionStr
	 * @return
	 */
	public static int insertarDetalleNotaRecuperacion(Connection con, int numeroSolicitud, String fechaRecuperacion, String horaRecuperacion, int notaRecuperacion, String valorNota, String fechaGrabacion, String horaGrabacion, int codigoEnfermera, int institucion, String insertarDetalleNotasRecuperacionStr) throws SQLException
	{
		int resp=0;
		try
		{
				
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleNotasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, numeroSolicitud);
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaRecuperacion)));
				ps.setString(3, horaRecuperacion);
				ps.setInt(4, notaRecuperacion);
				UtilidadBD.ingresarDatoAStatement(ps, valorNota, 5,Types.VARCHAR, true, false);
				ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaGrabacion)));
				ps.setString(7, UtilidadFecha.convertirHoraACincoCaracteres(horaGrabacion));
				ps.setInt(8, codigoEnfermera);
				ps.setInt(9, institucion);
				
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos del detalle de las notas de recuperacion [SqlBaseNotasRecuperacionDao]: "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	
	
	/**
	 * Método para consultar los tipos de notas de recuperacion parametrizados a la institucion
	 * y que se encuentren en estado activa=true
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTiposNotasRecuperacion (Connection con, int institucion, int numeroSolicitud) throws SQLException
	{
		HashMap parametricos=new HashMap();
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarTiposNotasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("consulta 1 -->"+consultarTiposNotasRecuperacionStr);
			pst.setInt(1, institucion);
			parametricos=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			
			int numReg=Integer.parseInt(parametricos.get("numRegistros")+"");
			for(int i = 0 ; i < numReg; i ++) 
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarHistoricoNotasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//logger.info("consulta 2 -->"+consultarHistoricoNotasRecuperacionStr+"---"+Utilidades.convertirAEntero(parametricos.get("codigorelacion_"+i)+"")+"--"+numeroSolicitud);
				ps.setInt(1,Utilidades.convertirAEntero(parametricos.get("codigorelacion_"+i)+""));
				ps.setInt(2, numeroSolicitud);
				parametricos.put("detNota_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			}
			return parametricos;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipo de notas recuperacion parametrizados a la institución [SqlBaseNotasRecuperacionDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Métodod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarFechaRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarFechasRecuperacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			logger.info("--->"+consultarFechasRecuperacionStr+"--"+numeroSolicitud);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las fechas de las notas de recuperacion [SqlBaseNotasRecuperacionDao]: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar las obervaciones de una nota de recuperacion segun el
	 * número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public static String consultarObservacionesGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		String observaciones="";
		ResultSetDecorator rs;
		try
			{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarObservacionesGeneralesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, numeroSolicitud);
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					String temp=rs.getString("observacionesGenerales")+"";
					if(!temp.trim().equals(null) && !temp.trim().equals("null"))
					{
						observaciones=rs.getString("observacionesGenerales");
					}
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la consulta de las obervaciones de una nota [SqlBaseNotasRecuperacionDao]:"+e.toString() );
					observaciones="";
			}
			return observaciones;
	}
	
	/**
	 * Método para consultar los medicamentos ingresados en una nota
	 * segun el número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public static String consultarMedicamentosGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		String medicamentos="";
		ResultSetDecorator rs;
		try
			{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarMedicamentosGeneralesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, numeroSolicitud);
				
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					String temp=rs.getString("medicamentosGenerales")+"";
					if(!temp.equals(null) && !temp.trim().equals("null"))
					{
						medicamentos=rs.getString("medicamentosGenerales");
					}
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la consulta de los medicamentos de una nota [SqlBaseNotasRecuperacionDao]:"+e.toString() );
					medicamentos="";
			}
			return medicamentos;
	}


	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public static List<NotaRecuperacionDto> listaNotasRecuperacion(Connection con, int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws BDException{

		List<NotaRecuperacionDto> listaNotasEstructura = cargarEstructuraParametrizada(con, codigoInstitucion);
		List<NotaRecuperacionDto> listaNotasRecuperacion = new ArrayList<NotaRecuperacionDto>();
		
		String ordenamiento="DESC";
		if(esAscendente){
			ordenamiento="ASC";
		}
		
		if(soloEstructuraParametrizada){
			listaNotasRecuperacion=listaNotasEstructura;
		}else{
	
			StringBuffer consultaFechas=new StringBuffer(" SELECT distinct dnr.nota_recuperacion as notaRecuperacion, dnr.fecha_recuperacion as fechaRecuperacion, dnr.hora_recuperacion as horaRecuperacion, ")
													.append(" dnr.numero_solicitud as numeroSolicitud, ")
													.append(" dnr.codigo_enfermera as codigoenfermera, ")
													.append(" administracion.getnombremedico(dnr.codigo_enfermera) as nombreEnfermera ")
													.append(" FROM salascirugia.det_notas_recuperacion dnr ")
													.append(" JOIN salascirugia.notas_recuperacion dr on dnr.nota_recuperacion=dr.codigo ")
													.append(" WHERE dnr.numero_solicitud=? ")
													.append("  ORDER BY  dnr.nota_recuperacion, dnr.fecha_recuperacion "+ordenamiento+", dnr.hora_recuperacion "+ordenamiento+" ");

			
			StringBuffer consultaHistoricoNotas=new StringBuffer(" SELECT dnr.numero_solicitud as numeroSolicitud, ")
												.append(" dnr.fecha_recuperacion as fechaRecuperacion, ")
												.append(" dnr.nota_recuperacion as notaRecuperacion, ")
												.append(" dnr.hora_recuperacion as horaRecuperacion, ")
												.append(" dnr.nota_recup as codigoRelacion, ")
												.append(" CASE WHEN dnr.valor IS NULL OR dnr.valor LIKE'%null%' THEN   ' ' ELSE dnr.valor  END as valorNota , ")
												.append(" dnr.fecha_grabacion as fechaGrabacion, ")
												.append(" dnr.hora_grabacion as horaGrabacion, ")
												.append(" dnr.codigo_enfermera as codigoEnfermera, ")
												.append(" nrg.observaciones as observaciones, ")
												.append(" administracion.getnombremedico(dnr.codigo_enfermera) as nombreEnfermera, ")
												.append(" administracion.getespecialidadesmedico1(dnr.codigo_enfermera, ', ') as especialidadesMedico, ")
												.append(" administracion.getdatosmedico1(dnr.codigo_enfermera) as datosMedico ")
												.append(" FROM det_notas_recuperacion dnr ")
												.append(" JOIN notas_recuperacion_general nrg on (dnr.numero_solicitud=nrg.numero_solicitud) ")
												.append(" WHERE dnr.numero_solicitud=? ")
												.append(" AND dnr.nota_recuperacion=? ")
												.append(" ORDER BY dnr.fecha_recuperacion "+ordenamiento+", dnr.hora_recuperacion "+ordenamiento+" ");
												
			NotaRecuperacionDto  dtoNotaEstructura = listaNotasEstructura.get(0);
			//para las fechas
			String cadenaConsultaFechas=consultaFechas.toString();
			PreparedStatementDecorator ps=null;
			ResultSetDecorator rs = null;
			//para los registros
			String cadenaConsultaHistorico=consultaHistoricoNotas.toString();
			PreparedStatementDecorator psh=null;
			ResultSetDecorator rsh = null;
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaFechas));
				ps.setInt(1, numeroSolicitud);
				rs =new ResultSetDecorator(ps.executeQuery());
				
				//recorre las notas
				while(rs.next()){

					NotaRecuperacionDto  dtoNota = (NotaRecuperacionDto) UtilidadClonacion.clonar(dtoNotaEstructura);				
					
					dtoNota.setFechaNota(rs.getDate("fechaRecuperacion"));
					dtoNota.setHoraNota(rs.getString("horaRecuperacion"));
					dtoNota.setId_nota(rs.getInt("notaRecuperacion"));
					dtoNota.setSolicitud(rs.getInt("numeroSolicitud"));
					dtoNota.setEnfermero(rs.getInt("codigoenfermera"));
					dtoNota.setNombreEnfermero(rs.getString("nombreEnfermera"));
					
					psh= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaHistorico));
					psh.setInt(1, numeroSolicitud);
					psh.setInt(2, dtoNota.getId_nota());				

					rsh =new ResultSetDecorator(psh.executeQuery());					
					//recorre campos (detalle) de la nota
					while(rsh.next()){
						dtoNota.setObservaciones(rsh.getString("observaciones"));
						dtoNota.setEspecialidadesMedico(rsh.getString("especialidadesMedico"));
						dtoNota.setDatosMedico(rsh.getString("datosMedico"));
						for (TipoCampoNotaRecuperacionDto dtoTipo : dtoNota.getTiposCampoNotaRecuperacion()) {
							for(CampoNotaRecuperacionDto dtoCampo : dtoTipo.getCamposNotaRecuperacion()){
								if(dtoCampo.getCodigoRelacion()==rsh.getInt("codigoRelacion")){
									dtoCampo.setValor(rsh.getString("valorNota"));
								}
							}
						}	
					}					
					listaNotasRecuperacion.add(dtoNota);
				}
			}
			catch(SQLException e)
			{
				logger.warn("Error en la consulta de las notas  : SqlBaseNotasGeneralesEnfermeriaDao "+e.toString());
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(psh, rsh, null);
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);	
			}
		}

		return listaNotasRecuperacion;
	}


	/**
	 * Carga la estructura parametrizada de Tipos de campo y campo
	 * @param con
	 * @param codigoInstitucion
	 * @return listaNotasRecuperacion (objeto tipo NotaRecuperacionDto con la estructura parametrizada)
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	private static List<NotaRecuperacionDto> cargarEstructuraParametrizada(Connection con, int codigoInstitucion) throws BDException{
		
		List<NotaRecuperacionDto> listaNotasRecuperacion = new ArrayList<NotaRecuperacionDto>();
		NotaRecuperacionDto  dtoNota = new NotaRecuperacionDto();
		dtoNota.setTiposCampoNotaRecuperacion(new ArrayList<TipoCampoNotaRecuperacionDto>());
		
		StringBuffer consulta=new StringBuffer(" SELECT pnr.codigo as codigoRelacion, ")
									.append(" pnr.tipo as codigoTipo, ")
									.append(" tnr.descripcion as nombreTipo, ")
									.append(" pnr.campo as codigoCampo, ")
									.append(" (SELECT (select count(1) from param_notas_recup_inst pnri where pnri.tipo=pnr.tipo) from param_notas_recup_inst p where p.codigo=pnr.codigo) as \"rows\", ")
									.append(" cnr.descripcion as nombreCampo ")
									.append(" FROM param_notas_recup_inst pnr ")
									.append(" INNER JOIN tipos_notas_recuperacion tnr ON(pnr.tipo=tnr.codigo) ")
									.append(" INNER JOIN campos_notas_recuperacion cnr ON(pnr.campo=cnr.codigo) ")
									.append(" WHERE pnr.institucion=? AND ")
									.append(" pnr.activo=")
									.append(ValoresPorDefecto.getValorTrueParaConsultas())
									.append(" ORDER BY pnr.codigo ");

		String cadena=consulta.toString();
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, codigoInstitucion);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			TipoCampoNotaRecuperacionDto dtoTipo = null;
			
			while(rs.next()){
				
				if(dtoTipo==null||dtoTipo.getCodigoTipo()!=rs.getInt("codigoTipo")){
					dtoTipo = new TipoCampoNotaRecuperacionDto();
					dtoTipo.setCodigoTipo(rs.getInt("codigoTipo"));
					dtoTipo.setNombreTipo(rs.getString("nombreTipo"));
					
					CampoNotaRecuperacionDto dtoCampo = new CampoNotaRecuperacionDto();
					dtoCampo.setNombre(rs.getString("nombreCampo"));
					dtoCampo.setCodigo(rs.getInt("codigoCampo"));
					dtoCampo.setCodigoRelacion(rs.getInt("codigoRelacion"));
					
					dtoTipo.getCamposNotaRecuperacion().add(dtoCampo);
					
				}
				else if (dtoTipo.getCodigoTipo()==rs.getInt("codigoTipo")){
					CampoNotaRecuperacionDto dtoCampo = new CampoNotaRecuperacionDto();
					dtoCampo.setNombre(rs.getString("nombreCampo"));
					dtoCampo.setCodigo(rs.getInt("codigoCampo"));
					dtoCampo.setCodigoRelacion(rs.getInt("codigoRelacion"));
					
					dtoTipo.getCamposNotaRecuperacion().add(dtoCampo);
				}
				if(!dtoNota.getTiposCampoNotaRecuperacion().contains(dtoTipo)){
					dtoNota.getTiposCampoNotaRecuperacion().add(dtoTipo);
				}
			}
			listaNotasRecuperacion.add(dtoNota);
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de las notas  : SqlBaseNotasGeneralesEnfermeriaDao "+e.toString());
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}

		return listaNotasRecuperacion;
	}
	
	
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param con
	 * @param dto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public static void guardarNotaRecuperacion(Connection con, NotaRecuperacionDto dto)throws BDException{
		
		StringBuffer existeNota=new StringBuffer(" SELECT nr.codigo as nota  ")
											.append(" FROM salascirugia.notas_recuperacion_general nrg ")
											.append(" JOIN salascirugia.notas_recuperacion nr on (nrg.numero_solicitud=nr.numero_solicitud) ")
											.append(" WHERE nrg.numero_solicitud=? ");

		StringBuffer updateNotaGeneral=new StringBuffer(" UPDATE notas_recuperacion_general ")
												.append(" SET observaciones=? ")
												.append(" WHERE numero_solicitud=? ");
		
		StringBuffer insertNotaGeneral=new StringBuffer(" INSERT INTO notas_recuperacion_general ")
												.append(" (numero_solicitud, ")
												.append(" observaciones) ")
												.append(" VALUES (?,?) ");
		

		PreparedStatement psExisteNota=null;
		ResultSetDecorator rsExisteNota = null;
		String cadenaExisteNota=existeNota.toString();
		
		PreparedStatement psUpdateNotaGeneral=null;
		ResultSetDecorator rsUpdateNotaGeneral = null;
		String cadenaUpdateNotaGeneral=updateNotaGeneral.toString();
		
		PreparedStatement psInsertNotaGeneral=null;
		ResultSetDecorator rsInsertNotaGeneral = null;
		String cadenaInsertNotaGeneral=insertNotaGeneral.toString();
		try
		{
			psExisteNota= new PreparedStatementDecorator(con.prepareStatement(cadenaExisteNota));
			psExisteNota.setInt(1, dto.getSolicitud());
			rsExisteNota =new ResultSetDecorator(psExisteNota.executeQuery());

			//ya existe una nota_genral para esta solicitud, 
			//por lo que solo se inserta el detalle (campos), se actualiza observaciones, se inserta nota (relacion).
			if(rsExisteNota.next()){
				//update nota_general (observaciones)
				psUpdateNotaGeneral=con.prepareStatement(cadenaUpdateNotaGeneral);
				psUpdateNotaGeneral.setString(1, dto.getObservaciones());
				psUpdateNotaGeneral.setInt(2, dto.getSolicitud());
		
				int resultado=psUpdateNotaGeneral.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			//si no existe nota se inserta nota_general + nota + detalle
			else{
				//insert nota_general
				psInsertNotaGeneral=con.prepareStatement(cadenaInsertNotaGeneral);
				psInsertNotaGeneral.setInt(1, dto.getSolicitud());
				psInsertNotaGeneral.setString(2, dto.getObservaciones());
				
				int resultado=psInsertNotaGeneral.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			//en ambos casos se inserta nota + detalle
			//insert Nota ¡nueva tabla!
			int codigo_pk_nota=guardarNota(con, dto);
			//insert detalle
			guardarDetalleNota(con, dto, codigo_pk_nota);
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(psExisteNota, rsExisteNota, null);
			UtilidadBD.cerrarObjetosPersistencia(psUpdateNotaGeneral, rsUpdateNotaGeneral, null);
			UtilidadBD.cerrarObjetosPersistencia(psInsertNotaGeneral, rsInsertNotaGeneral, null);
		}
	}
	
	/**
	 * Metodo que guarda nota de revuperacion.
	 * @param con
	 * @param dto
	 * @return
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 08/07/2013
	 */
	public static int guardarNota(Connection con, NotaRecuperacionDto dto)throws BDException{
		
		StringBuffer insertNota=new StringBuffer(" INSERT INTO salascirugia.notas_recuperacion ")
										.append(" (codigo, ")
										.append(" numero_solicitud) ")
										.append(" VALUES (?,?) ");
		
		
		int seq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "salascirugia.seq_notas_recuperacion");
		
		PreparedStatement psInsertNota=null;
		ResultSetDecorator rsInsertNota = null;
		String cadenaInsertNota=insertNota.toString();
		try{
			psInsertNota=con.prepareStatement(cadenaInsertNota);
			psInsertNota.setInt(1, seq);
			psInsertNota.setInt(2, dto.getSolicitud());
			
			int resultado=psInsertNota.executeUpdate();
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		}catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(psInsertNota, rsInsertNota, null);
}
		return seq;
	}
	
	/**
	 * Metodo que guarda el detalle de una nota de recuperacion
	 * @param con
	 * @param dto
	 * @param sintaxisSeq
	 * @param codigo_pk_nota
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 08/07/2013
	 */
	public static void guardarDetalleNota(Connection con, NotaRecuperacionDto dto, int codigo_pk_nota)throws BDException{
		
		StringBuffer insertDetale=new StringBuffer(" INSERT INTO det_notas_recuperacion ")
										.append(" (codigo, ")
										.append(" numero_solicitud, ")
										.append(" fecha_recuperacion, ")
										.append(" hora_recuperacion, ")
										.append(" nota_recup, ")
										.append(" valor, ")
										.append(" fecha_grabacion, ")
										.append(" hora_grabacion, ")
										.append(" codigo_enfermera, ")
										.append(" institucion,  ")
										.append(" nota_recuperacion ) ")
										.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?) ");

		int seq=ConstantesBD.codigoNuncaValido;
		
		PreparedStatement psInsertDet=null;
		ResultSetDecorator rsInsertDet = null;
		String cadenaInsertDet=insertDetale.toString();
		try{
			for(TipoCampoNotaRecuperacionDto tipo : dto.getTiposCampoNotaRecuperacion()){
				for(CampoNotaRecuperacionDto campo : tipo.getCamposNotaRecuperacion()){
					seq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "salascirugia.seq_not_recuperacion");
					psInsertDet=con.prepareStatement(cadenaInsertDet);
					psInsertDet.setInt(1, seq);
					psInsertDet.setInt(2, dto.getSolicitud());
					psInsertDet.setDate(3, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
					psInsertDet.setString(4, UtilidadFecha.getHoraActual(con));
					
					psInsertDet.setInt(5, campo.getCodigo());
					psInsertDet.setString(6, campo.getValor());
					
					psInsertDet.setDate(7, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
					psInsertDet.setString(8, UtilidadFecha.getHoraActual(con));
					psInsertDet.setInt(9, dto.getEnfermero());
					psInsertDet.setInt(10, dto.getInstitucion());
					psInsertDet.setInt(11, codigo_pk_nota);
					
					psInsertDet.executeUpdate();
				}
			}

		}catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(psInsertDet, rsInsertDet, null);
		}
	}
}