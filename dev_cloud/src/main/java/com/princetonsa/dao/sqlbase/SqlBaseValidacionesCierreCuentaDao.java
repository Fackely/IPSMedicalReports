/*
 * Created on May 5, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * @author sebastián gómez
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseValidacionesCierreCuentaDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseValidacionesCierreCuentaDao.class);
	
	/**
	 * cadena para consultar el estado de la cuenta según su ID
	 */
	private static final String validarEstadoCuentaStr="SELECT estado_cuenta AS estado FROM cuentas WHERE id=?";
	
	/**
	 * cadena para contar las solicitudes con cargos mayores a $0.00 y que tengan estado diferente a Inactiva
	 */
	private static final String validarCargosSeviciosXCuentaStr="SELECT count(1) AS numero_registros "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN det_cargos dc ON(dc.solicitud=s.numero_solicitud) "+ 
		"WHERE s.cuenta=? AND " +
		" dc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+ 
		"dc.estado NOT IN ("+ConstantesBD.codigoEstadoFInactiva+","+ConstantesBD.codigoEstadoFExento+","+ConstantesBD.codigoEstadoFacturacionAnulada+") "+ 
		"and dc.valor_total_cargado > 0";
	
	
	/**
	 * cadena para consultar el número de medicamentos despachados directamente al paciente
	 * con cargo generado en toda la cuenta
	 */
	private static final String medicamentosDespachadosAPacienteStr="SELECT " +
			"sum(coalesce(dd.cantidad,0)) AS articulos " +
			"FROM solicitudes s " +
			"INNER JOIN despacho d ON(d.numero_solicitud=s.numero_solicitud AND d.es_directo="+ValoresPorDefecto.getValorTrueParaConsultas()+") " +
			"INNER JOIN detalle_despachos dd ON(d.orden=dd.despacho) " +
			"INNER JOIN det_cargos dc ON(dc.solicitud=d.numero_solicitud AND dc.articulo=dd.articulo AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" and dc.cantidad_cargada > 0)" +
			" WHERE s.cuenta=? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' ";
			
	
	/**
	 * cadena para consultar el número de medicamentos administrados 
	 * con cargo generado en toda la cuenta
	 */
	private static final String medicamentosAdministradosStr="SELECT " +
			"CASE WHEN sum(da.cantidad) IS NULL THEN 0 ELSE sum(da.cantidad) END AS articulos " +
			"FROM solicitudes s " +
			"INNER JOIN admin_medicamentos am ON(am.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN detalle_admin da ON(da.administracion=am.codigo) " +
			"INNER JOIN det_cargos dc ON(dc.solicitud=am.numero_solicitud AND dc.articulo=da.articulo AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" and dc.cantidad_cargada > 0)" +
			" WHERE s.cuenta=? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' ";
	
	/**
	 * cadena para consultar el número de medicamentos devueltos con cargo generado (no automática)
	 * en toda la cuenta
	 */
	private static final String medicamentosDevueltosStr="SELECT " +
			"CASE WHEN sum(ddm.cantidad) IS NULL THEN 0 ELSE sum(ddm.cantidad) END AS devoluciones " +
			"FROM solicitudes s " +
			"INNER JOIN detalle_devol_med ddm ON(ddm.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN devolucion_med dm ON(dm.codigo=ddm.devolucion AND dm.tipo_devolucion="+ConstantesBD.codigoTipoDevolucionManual+") " +
			"INNER JOIN det_cargos dc ON(dc.solicitud=ddm.numero_solicitud AND dc.articulo=ddm.articulo AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" and dc.cantidad_cargada > 0)" +
			" WHERE " +
			"s.cuenta=? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' " ;
	
	/**
	 * cadena para consultar el número de solicitudes que tienen estado
	 * de facturación pendiente
	 */
	private static final String solicitudesPendientesStr="SELECT count(1) as pendientes "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN det_cargos dc ON(dc.solicitud=s.numero_solicitud) "+ 
		"WHERE "+ 
		"s.cuenta = ? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+ 
		"dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+") AND "+ 
		"dc.tipo_solicitud NOT IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") ";
	

	
	/**
	 * cadena para consultar el número de citas pendientes de una cuenta
	 * cuya vía de ingreso es Consulta Externa
	 */
	private static final String validarSolicitudesConsultaExternaStr="SELECT count(1) AS citas "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN solicitudes_consulta sc ON(sc.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN servicios_cita serc ON(serc.numero_solicitud=sc.numero_solicitud) "+
		"INNER JOIN cita c ON(c.codigo=serc.codigo_cita) "+ 
		"INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) "+ 
		"WHERE "+ 
		"s.cuenta=? AND "+ 
		"c.estado_cita IN("+ConstantesBD.codigoEstadoCitaAsignada+","+ConstantesBD.codigoEstadoCitaReservada+","+ConstantesBD.codigoEstadoCitaReprogramada+") AND "+ 
		"esSolServicioCargado(sc.numero_solicitud,sc.codigo_servicio_solicitado) = '"+ConstantesBD.acronimoSi+"' AND "+ 
		"a.fecha>=CURRENT_DATE AND "+ 
		//"(CASE WHEN a.fecha=CURRENT_DATE THEN a.hora_inicio>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" ELSE 1=1 END) "; 
		"((a.fecha=CURRENT_DATE and a.hora_inicio>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") " +
				" OR (a.fecha<>CURRENT_DATE AND 1=1)) ";
	
	/**
	 * Cadena para consultar la presencia de una solicitud de valoración
	 * con estado pendiente en cuenta sde urgencias y/o hospitalización
	 */
	private static final String solicitudValoracionPendienteStr="SELECT count(1) as pendientes "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN det_cargos dc ON(dc.solicitud=s.numero_solicitud) "+ 
		"WHERE "+ 
		"s.cuenta = ? AND "+ 
		" dc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+
		"dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+") AND "+ 
		"dc.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") ";
	
	/**
	 * Método que verifica si la cuenta está abierta para que
	 * se pueda cerrar.
	 * @param con
	 * @param idCuenta
	 * @return true=> cuenta abierta , false=> otro estado o error
	 */
	public static boolean validarEstadoCuenta(Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(validarEstadoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next()){
				int estado=rs.getInt("estado");
				//se verifica que el estado de la cuenta sea 'Abierta' o 'Asociada'
				if(estado==ConstantesBD.codigoEstadoCuentaActiva||estado==ConstantesBD.codigoEstadoCuentaAsociada){
					return true;
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		catch(SQLException e){
			logger.error("Error en validarEstadoCuenta de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método para validar si los cargos de servicios de la cuenta tienen valores pendientes
	 * por cobrar
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay cargos pendientes por cobrar)
	 */
	public static boolean validarCargosSeviciosXCuenta(Connection con,int idCuenta)
	{
		try{
			//variables
			int numReg01=0;
			
			//validacion con solicitudes 
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(validarCargosSeviciosXCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				numReg01=rs.getInt("numero_registros");
			
			
			
			if(numReg01>0)
				return false;
			else
				return true;
		
		}
		catch(SQLException e){
			logger.error("Error en validarCargosServiciosXCuenta de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método para validar si los cargos de medicamentos de la cuenta tienen valores pendientes 
	 * por cobrar, esto se hace calculando el número de medicamentos despachados y el número de devoluciones.
	 * @param con
	 * @param idCuenta
	 * @return true=> válido, false=> inválido (todavía hay ordenes de medicamentos)
	 */
	public static boolean validarCargosMedicamentosXCuenta(Connection con,int idCuenta){
		try{
			int numMedicamentosDespachadosAPaciente=0;
			int numMedicamentosAdministrados=0;
			int numMedicamentosDevueltos=0;
			int calculo=0;
			
			
			//CONSULTAR EL NÚMERO DE MEDICAMENTOS DESPACHADOS DIRECTAMENTE AL PACIENTE
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(medicamentosDespachadosAPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numMedicamentosDespachadosAPaciente=rs.getInt("articulos");
			
			//CONSULTAR EL NÚMERO DE MEDICAMENTOS ADMINISTRADOS
			pst= new PreparedStatementDecorator(con.prepareStatement(medicamentosAdministradosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numMedicamentosAdministrados=rs.getInt("articulos");
			
			//CONSULTAR EL NÚMERO DE MEDICAMENTOS DEVUELTOS
			pst= new PreparedStatementDecorator(con.prepareStatement(medicamentosDevueltosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numMedicamentosDevueltos=rs.getInt("devoluciones");
			
			calculo=numMedicamentosDespachadosAPaciente+numMedicamentosAdministrados-numMedicamentosDevueltos;
			if(calculo==0)
				return true;
			else
				return false;
		
		}
		catch(SQLException e){
			logger.error("Error en validarCargosMedicamentosXCuenta de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
		
	}
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadosFactSolicitudes(Connection con,int idCuenta){
		try{
			//El objetivo es saber cuantas solicitudes hay en estaod pendiente
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(solicitudesPendientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next()){
				int pendientes=rs.getInt("pendientes");
				if(pendientes>0)
					return false;
				else
					return true;
				
			}
			else{
				return true;
			}
		}
		catch(SQLException e){
			logger.error("Error en validarEstadosFactSolicitudes de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
	}
	
	
	/**
	 * Método para verificar si la cuenta de Consulta Externa tiene citas pendientes
	 * @param con
	 * @param idCuenta
	 * @return true=> hay citas pendientes, false=>no hay citas pendientes
	 */
	public static boolean validarSolicitudesConsultaExterna(Connection con,int idCuenta){
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(validarSolicitudesConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				int citas=rs.getInt("citas");
				
				if(citas>0)
					return true;
				else
					return false;
			}
			else{
				return true;
			}
		}
		catch(SQLException e){
			logger.error("Error en validarSolicitudesConsultaExterna de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que verifica si la cuenta de urgencias u hospitalización 
	 * tiene la solicitud de valoración en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadoFactSolicitudValoracion(Connection con,int idCuenta)
	{
		try{
			//El objetivo es saber cuantas solicitudes hay en estaod pendiente
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(solicitudValoracionPendienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next()){
				int pendientes=rs.getInt("pendientes");
				if(pendientes>0)
					return false;
				else
					return true;
				
			}
			else{
				return true;
			}
		}
		catch(SQLException e){
			logger.error("Error en validarEstadoFactSolicitudValoracion de SqlBaseValidacionesCierreCuentaDao: "+e);
			return false;
		}
	}
	
}
