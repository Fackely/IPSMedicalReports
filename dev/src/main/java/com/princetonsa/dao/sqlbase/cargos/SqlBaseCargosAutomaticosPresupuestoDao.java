package com.princetonsa.dao.sqlbase.cargos;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;


public class SqlBaseCargosAutomaticosPresupuestoDao 
{
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap cargarServiciosAutomaticos(Connection con, int codigoIngreso) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		
		try
		{
			/*String cadena="SELECT DISTINCT" +
			" s.codigo as codigoservicio," +
			" getnombreservicio(s.codigo,0) as nombreservicio," +
			" ps.cantidad as cantidad," +
			" ps.valor_unitario as valorunitario," +
			" s.grupo_servicio as gruposervicio " +
		" from servicios s " +
		" inner join presupuesto_servicios ps on(s.codigo=ps.servicio) " +
		" inner join presupuesto_paciente pp on(pp.consecutivo=ps.presupuesto) " +
		" where pp.paciente=? and s.automatico='"+ConstantesBD.acronimoSi+"' and s.codigo NOT IN (select cd.servicio_solicitado from cargos_directos cd INNER JOIN solicitudes sol ON (sol.numero_solicitud=cd.numero_solicitud) INNER JOIN cuentas c ON(sol.cuenta=c.id) WHERE c.codigo_paciente=pp.paciente and c.estado_cuenta<>1)";*/
		
			String cadena="SELECT DISTINCT" +
									" s.codigo as codigoservicio," +
									" getnombreservicio(s.codigo,0) as nombreservicio," +
									" ps.cantidad as cantidad," +
									" ps.valor_unitario as valorunitario," +
									" s.grupo_servicio as gruposervicio " +
								" from servicios s " +
								" inner join presupuesto_servicios ps on(s.codigo=ps.servicio) " +
								" inner join presupuesto_paciente pp on(pp.consecutivo=ps.presupuesto) " +
							" where pp.ingreso=? and s.automatico='"+ConstantesBD.acronimoSi+"'" +
								" and s.codigo not in(select dc.servicio from det_cargos dc INNER JOIN solicitudes sol ON (sol.numero_solicitud=dc.solicitud) INNER JOIN cuentas c ON(sol.cuenta=c.id) WHERE c.codigo_paciente=pp.paciente and c.estado_cuenta<>1 and dc.servicio is not null)"; 
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public static HashMap obtenerCentrosCosto(Connection con, String servicios) 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		
		try
		{
		
			String cadena="SELECT DISTINCT" +
									" cgs.centro_costo as codigocentro," +
									" getnomcentrocosto(cgs.centro_costo) as nombrecentro" +
									
								" from centro_costo_grupo_ser cgs " +
								" inner join grupos_servicios gs on(gs.codigo=cgs.grupo_servicio) " +
								" inner join servicios s on(s.grupo_servicio=gs.codigo) " +
								" where s.codigo=? ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(servicios));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public static HashMap cargarTarifas(Connection con, int numeroSolicitud, int servicio) 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		
		try
		{
		
			String cadena="SELECT DISTINCT" +
											" dc.valor_unitario_cargado as valorunitario," +
											" dc.valor_total_cargado as valortotal" +
											
										" from det_cargos dc " +
										
										" where dc.solicitud=? and servicio=? and eliminado='"+ConstantesBD.acronimoNo+"'";

			
			
		
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, servicio);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * Metodo que valida si el paciente tiene asiciado un presupuesto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean pacienteTienePresupuesto(Connection con,int codigoIngreso)
	{
		boolean resp=false;
		try
		{
			String cadena="SELECT paciente FROM presupuesto_paciente WHERE ingreso="+codigoIngreso;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resp=true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			resp=false;
		}
		return resp;
	}
	
}
