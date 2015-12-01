/*
 * @author artotor
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;


/***
 * @author artotor
 */
public class SqlBasePacientesConEgresoPorFacturarDao {
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(SqlBasePacientesConEgresoPorFacturarDao.class);
	
	private static String restricciones=" where " +
										"(" +
												"c.estado_cuenta = " +ConstantesBD.codigoEstadoCuentaActiva+" "+ 
												"OR " +
												"(c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" AND getcuentafinal(c.id) IS NULL) " +

										")" +
										" and c.via_ingreso in ("+
														ConstantesBD.codigoViaIngresoHospitalizacion+","+
														ConstantesBD.codigoViaIngresoUrgencias+
													") " +
										" and i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
										" and cc.centro_atencion=? " +
										" and cc.institucion=? ";

	private static String restriccionesBA=" where " +
										"(" +
												"c.estado_cuenta = " +ConstantesBD.codigoEstadoCuentaActiva+" "+ 
												"OR " +
												"(c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" AND getcuentafinal(c.id) IS NULL) " +
									
										")" +
										" and i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
										" and cc.institucion=? ";

	//private static String orden=" order by getnomcentrocosto(c.area),getnombrepersona(c.codigo_paciente) ";
	private static String orden=" order by area,fecha_egreso,hora_egreso ";
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param consultaPacientesConEgresoPorFacturar 
	 * @return
	 */
	public static HashMap cargarPacientesConEgresoPorFacturar(Connection con, UsuarioBasico usuario, String consultaPacientesConEgresoPorFacturar) 
	{
		String consulta=consultaPacientesConEgresoPorFacturar+restricciones+orden;
		logger.info("consulta "+consulta);
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(usuario.getCodigoCentroAtencion()+""));
			ps.setInt(2,Utilidades.convertirAEntero(usuario.getCodigoInstitucion()));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs);
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR EN CONSULTA [SqlBasePacientesConEgresoPorFacturarDao] ");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param consultaPacientesConEgresoPorFacturar
	 * @param dtoFiltro
	 * @return
	 */
	public static HashMap cargarPacientesConEgresoPorFacturarAvanzado(Connection con, UsuarioBasico usuario,String consultaPacientesConEgresoPorFacturar,DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro) 
	{
		String condiciones=restriccionesBA;
		if(!dtoFiltro.getConsecutivoAdmision().isEmpty())
		{
			condiciones=condiciones+" and (ah.consecutivo='"+dtoFiltro.getConsecutivoAdmision()+"' or au.consecutivo='"+dtoFiltro.getConsecutivoAdmision()+"')";
		}
		if(!dtoFiltro.getFechaAdmisionIncial().isEmpty() && !dtoFiltro.getFechaAdmisionFinal().isEmpty())
		{
			condiciones=condiciones+" and ((to_char(ah.fecha_admision,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionIncial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionFinal())+"') or (to_char(au.fecha_admision,'yyyy-mm-dd')  between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionIncial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaAdmisionFinal())+"'))";
		}
		if(!dtoFiltro.getFechaEgresoInicial().isEmpty() && !dtoFiltro.getFechaEgresoFinal().isEmpty())
		{
			condiciones=condiciones+" and (to_char(e.fecha_egreso,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaEgresoInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaEgresoFinal())+"')";
		}
		if(dtoFiltro.getViaIngreso()>0)
		{
			condiciones=condiciones+" and c.via_ingreso="+dtoFiltro.getViaIngreso();
			if(!dtoFiltro.getTipoPaciente().isEmpty())
			{
				condiciones=condiciones+" and c.tipo_paciente='"+dtoFiltro.getTipoPaciente()+"'";
			}
		}
		
		if(dtoFiltro.getCentroCosto()>0)
		{
			condiciones=condiciones+" and c.area="+dtoFiltro.getCentroCosto();
		}
		if(!dtoFiltro.getTipoId().isEmpty())
		{
			condiciones=condiciones+" and p.tipo_identificacion='"+dtoFiltro.getTipoId()+"'";
		}
		if(!dtoFiltro.getNumeroId().isEmpty())
		{
			condiciones=condiciones+" and p.numero_identificacion='"+dtoFiltro.getNumeroId()+"'";
		}
		if(dtoFiltro.getCodigoMedico()>0)
		{
			condiciones=condiciones+" and e.codigo_medico="+dtoFiltro.getCodigoMedico();
		}

		String consulta=consultaPacientesConEgresoPorFacturar+condiciones+orden;
		logger.info("consulta "+consulta);
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(usuario.getCodigoInstitucion()));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs);
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR EN CONSULTA [SqlBasePacientesConEgresoPorFacturarDao] ");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}
}
