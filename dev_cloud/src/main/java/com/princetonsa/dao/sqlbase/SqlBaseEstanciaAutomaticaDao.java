/*
 * Created on Aug 20, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author sebacho
 *
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a la Estancia Autom�tica
 *
 */
public class SqlBaseEstanciaAutomaticaDao {
	/**consultaCuentasEstanciaPorArea
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseEstanciaAutomaticaDao.class);
	
	/**
	 * Cadena usada para hacer la consulta inicial de las cuentas a las
	 * cuales se les generar� la estancia autom�tica
	 */
	private static final String consultaCuentasEstanciaPorAreaStr="SELECT DISTINCT "+ 
		"c.id as cuenta, " +
		"c.id_ingreso As ingreso, "+
		"gettienetraslados(c.id) AS traslados, "+
		"c.area as centro_costo_solicitante, "+
		"p.primer_apellido ||' '|| p.segundo_apellido ||' '|| p.primer_nombre ||' '|| p.segundo_nombre AS paciente, "+
		"c.codigo_paciente as codigo_paciente "+
		"FROM cuentas c " +
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
		"INNER JOIN admisiones_hospi ah ON(ah.cuenta=c.id) "+ 
		"INNER JOIN personas p ON(c.codigo_paciente=p.codigo) "+
		"WHERE "+ 
		"c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") AND " +
		"c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" AND " +
		"c.hospital_dia = '"+ConstantesBD.acronimoNo+"' AND "+ 	//no pueden ser ingresos de hospital d�a
		"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') AND "+ 
		"ah.fecha_admision<=? AND "+
		//se valida que sobre la cuenta no exista solicitud de estancia ya generada
		"c.id NOT IN (" +
			"select cuenta from solicitudes s where s.cuenta=c.id AND " +
			"tipo="+ConstantesBD.codigoTipoSolicitudEstancia+" AND " +
			"fecha_solicitud=?" +
		") AND "+
		//se valida que no exista evolucion con orden de salida para la fecha de estancia
		"c.id NOT IN (" +
			" select s.cuenta from solicitudes s " +
			" INNER JOIN evoluciones e ON (s.numero_solicitud=e.valoracion)" +
			" INNER JOIN egresos eg ON (e.codigo=eg.evolucion) where " +
			" s.tipo="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" AND " +
			" e.fecha_evolucion=? AND e.orden_salida=" +ValoresPorDefecto.getValorTrueParaConsultas()+
			" AND eg.fecha_reversion_egreso is null AND s.cuenta=c.id"+
		") AND " +
		"cc.centro_atencion = ? ";
	
	/**
	 * Cadena que consulta los traslados cama de la cuenta en el d�a de la
	 * estancia ordenados descendentemente por hora de asignacion
	 */
	private static final String consultaTrasladosCuentaDiaEstanciaStr="SELECT "+ 
		"tc.codigo_nueva_cama AS cama,"+
		"c.centro_costo AS centro_costo," +
		"cc.tipo_entidad_ejecuta AS tipo_entidad_ejecuta, "+
		"c.es_uci AS es_uci "+ 
		"FROM traslado_cama tc, camas1 c, centros_costo cc "+ 
		"WHERE "+ 
		"c.codigo=tc.codigo_nueva_cama AND "+ 
		"c.centro_costo = cc.codigo AND "+
		"tc.fecha_asignacion=? AND "+ 
		"tc.cuenta=? ";
	
	
	/**
	 * Cadena que consulta los tipos de monitoreo de los encabezados hist�ricos
	 * de la orden m�dica, de acuerdo a la cuenta del paciente y al d�a de estancia
	 */
	private static final String consultaTiposMonitoreoDiaEstanciaStr="SELECT "+ 
		"t.codigo AS tipo_monitoreo,"+
		"t.prioridad_cobro AS prioridad,"+
		"t.servicio AS servicio "+  
		"FROM ordenes_medicas om "+ 
		"INNER JOIN encabezado_histo_orden_m eh ON(om.codigo=eh.orden_medica) "+ 
		"INNER JOIN orden_tipo_monitoreo ot ON(ot.codigo_histo_encabezado=eh.codigo) "+ 
		"INNER JOIN tipo_monitoreo t ON(t.codigo=ot.tipo_monitoreo) "+
		"WHERE om.cuenta=? AND eh.fecha_orden=? ORDER BY eh.hora_orden DESC";
	
	
	/**
	 * Cadena usada para consultar el servicio de la cama directamente de
	 * la tabla servicios_cama, tomando en cuenta o no el tipo de monitoreo
	 */
	private static final String consultarServicioCamaStr="SELECT servicio AS servicio FROM servicios_cama WHERE codigo_cama=?";
	
	/**
	 * Cadena que sirve de consulta de la cuenta del paciente 
	 * y de su verificaci�n para saber si es posible generar estancia autom�tica
	 * seg�n el d�a de la estancia
	 */
	private static final String consultaCuentaEstanciaPorPacienteStr="SELECT DISTINCT "+ 
		"c.id as cuenta," +
		"c.id_ingreso AS ingreso, "+
		"c.area as centro_costo_solicitante, " +
		"c.codigo_paciente as codigo_paciente "+ 
		"FROM cuentas c "+ 
		"INNER JOIN admisiones_hospi ah ON(ah.cuenta=c.id) "+ 
		"INNER JOIN traslado_cama tc ON(tc.cuenta=c.id) "+
		"INNER JOIN camas1 ca ON(ca.codigo=tc.codigo_nueva_cama) "+ 
		"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
		"WHERE "+ 
		"c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") AND " +
		"c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" AND "+
		"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') AND "+ 
		"ah.fecha_admision<=? AND "+
		//se valida que sobre la cuenta no exista solicitud de estancia ya generada
		"c.id NOT IN (" +
			"select cuenta from solicitudes s where s.cuenta=c.id AND " +
			"tipo="+ConstantesBD.codigoTipoSolicitudEstancia+" AND " +
			"fecha_solicitud=?" +
		") AND "+
		//se valida que no exista evolucion con orden de salida para la fecha de estancia
		"c.id NOT IN (" +
			" select s.cuenta from solicitudes s" +
			" INNER JOIN evoluciones e ON (s.numero_solicitud=e.valoracion)" +
			" INNER JOIN egresos eg ON (e.codigo=eg.evolucion) where " +
			" s.tipo="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"  AND " +
			" e.fecha_evolucion=? AND e.orden_salida=" +ValoresPorDefecto.getValorTrueParaConsultas()+
			" AND eg.fecha_reversion_egreso is null AND s.cuenta=c.id"+
		") AND "+
		"c.id=? AND i.institucion=?";
	
	/**
	 * Cadena que consulta el n�mero de estancias del paciente
	 */
	private static final String numeroEstanciasPacienteStr = "select count(1) as cuenta " +
			"from solicitudes where " +
			"tipo="+ConstantesBD.codigoTipoSolicitudEstancia+" AND " +
			"cuenta = ? AND " +
			"fecha_solicitud between ? and ?";
	
	
	/**
	 * M�todo usado para consultar las cuentas a las cuales se les va a
	 * generar una solicitud de estancia y su cargo, en la opci�n
	 * de Estancia Autom�tica Por �rea
	 * @param con
	 * @param centroCosto
	 * @param fechaEstancia
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultaCuentasEstanciaPorArea(Connection con,String fechaEstancia,int centroAtencion, int institucion,int codigoCentroCosto)
	{
		///columnas del listado
		String[] columnas={
				"cuenta",
				"ingreso",
				"traslados",
				"centro_costo_solicitante",
				"paciente",
				"codigo_paciente"
			};
		
		try
		{
			
			

			String consulta=consultaCuentasEstanciaPorAreaStr;

			if (!ValoresPorDefecto.getIncluirTipoPacienteCirugiaAmbulatoria(institucion).equals(ConstantesBD.acronimoSi))
				consulta+=" and c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' ";
			
			if(codigoCentroCosto>0)
				consulta+=" and c.area = "+codigoCentroCosto+" ";
				
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setInt(4,centroAtencion);
			logger.info("CONSULTA CUENTAS ESTANCIA=> "+consulta+", fecha=> "+UtilidadFecha.conversionFormatoFechaABD(fechaEstancia)+", centroAtencion=> "+centroAtencion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCuentasEstanciaPorArea de SqlBaseEstanciaAutomaticaDao: "+e);
			return null;
		}
	}
	
	/**
	 * M�todo que consulta los traslados cama de la cuenta que
	 * va a generar solicitud de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @return
	 */
	public static HashMap consultarTrasladosCuenta(Connection con,int idCuenta,String fechaEstancia,int centroCosto,String consultaSinEstancia)
	{
		//columnas del listado
		String[] columnas={
				"cama",
				"centro_costo",
				"tipo_entidad_ejecuta",
				"es_uci"
			};	
		try
		{
			String consulta=consultaTrasladosCuentaDiaEstanciaStr;
			if(centroCosto!=0){
				consulta+="AND c.centro_costo="+centroCosto;
			}
			consulta+=" ORDER BY hora_asignacion DESC";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setInt(2,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			RespuestaHashMap listado;
			//se verifica si la cuenta tiene traslados para la fecha de la estancia
			if(rs.next())
			{
				listado=UtilidadBD.resultSet2HashMap(columnas,rs,false,true);
				return listado.getMapa();
			}
			else
			{
				//en el caso de que no hayan, se consultan traslados anteriores
				//a la fecha de la estancia
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaSinEstancia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
				pst.setInt(2,idCuenta);
				listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
				return listado.getMapa();
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTrasladosCuenta de SqlBaseEstanciaAutomaticaDao: "+e);
			return null;
		}
	}
	
	/**
	 * M�todo usado para consultar los tipos de monitoreo de una cuenta 
	 * teniendo como referencia de b�squeda la fecha de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @return
	 */
	public static HashMap consultarTiposMonitoreo(Connection con,int idCuenta,String fechaEstancia,String consultaSinDiaEstanciaStr)
	{
		//columnas del listado
		String[] columnas={
				"tipo_monitoreo",
				"prioridad",
				"servicio"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaTiposMonitoreoDiaEstanciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			RespuestaHashMap listado;
			//se verifica si hab�an tipos monitoreo para el d�a de la estancia
			if(rs.next())
			{
				listado=UtilidadBD.resultSet2HashMap(columnas,rs,false,true);
				return listado.getMapa();
			}
			else
			{
				//en el caso de que no hayan tipos monitoreo, se consultan
				//en d�as anteriores a la fecha de la estancia
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaSinDiaEstanciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,idCuenta);
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
				listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
				return listado.getMapa();
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTiposMonitoreo de SqlBaseEstanciaAutomaticaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que consulta el servicio del cargo en la tabla
	 * servicios_cama
	 * @param con
	 * @param cama
	 * @param tipoMonitoreo
	 * @return
	 */
	public static int consultarServicioCama(Connection con,int cama,int tipoMonitoreo)
	{
		try
		{
			String consulta=consultarServicioCamaStr;
			//se verifica si se debe validar el tipo de monitoreo en la b�squeda
			if(tipoMonitoreo==0)
				consulta+=" AND tipo_monitoreo IS NULL";
			else
				consulta+=" AND tipo_monitoreo="+tipoMonitoreo;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,cama);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("servicio");
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarServicioCama de SqlBaseEstanciaAutomaticaDao: "+e);
			return 0;
		}
	}
	
	/**
	 * M�todo usado para registrar el LOG tipo Base de Datos del proceso
	 * de generaci�n de Estancia Autom�tica
	 * @param con
	 * @param tipo
	 * @param fechaInicialEstancia
	 * @param fechaFinalEstancia
	 * @param usuario
	 * @param institucion
	 * @param area
	 * @param paciente
	 * @param centroAtencion
	 * @param inconsistencia
	 * @param reporte
	 * @param consulta
	 * @return
	 */
	public static int insertarLogEstanciaAutomatica(Connection con,int tipo,String fechaInicialEstancia,String fechaFinalEstancia,String usuario,int institucion,int area,int paciente,int centroAtencion,boolean inconsistencia,String reporte,String consulta,String indica)
	{
		try
		{
			logger.info("datos para la insercion >>> \n "+tipo+"-"+usuario+"-"+institucion+"-"+area+"-"+paciente+"-"+centroAtencion+"-"+indica+"-"+fechaFinalEstancia+"-"+fechaInicialEstancia+"-"+inconsistencia+"-"+reporte);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,tipo);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicialEstancia));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinalEstancia));
			pst.setString(4,usuario);
			pst.setInt(5,institucion);
			if(paciente>0)
				pst.setInt(6,paciente);
			else
				pst.setNull(6,Types.INTEGER);
			if(area>=0)
				pst.setInt(7,area);
			else
				pst.setNull(7,Types.INTEGER);
			pst.setBoolean(8,inconsistencia);
			pst.setString(9,reporte);
			pst.setInt(10,centroAtencion);
			
			if (indica.equals("Automatico"))
				indica="AUTO";
			else
				if(indica.equals("Manual"))
					indica="MANU";
			
			pst.setString(11, indica);
			
			 
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogEstanciaAutomatica de SqlBaseEstanciaAutomaticaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * M�todo que consulta y verifica que sobre la cuenta se
	 * pueda generar estancia autom�tica para el d�a asignado.
	 * En el caso de que no se pueda generar estancia para ese d�a
	 * el m�todo retorna un HashMap vac�o
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public static HashMap consultaCuentaEstanciaPorPaciente(Connection con,int idCuenta,String fechaEstancia,int institucion)
	{
		logger.info("\n entre a consultaCuentaEstanciaPorPaciente fechaEstancia -->"+fechaEstancia+" idCuenta-->"+idCuenta);
		//columnas del listado
		String[] columnas={
				"cuenta",
				"ingreso",
				"centro_costo_solicitante",
				"codigo_paciente"
			};	
		try
		{
			String consulta=consultaCuentaEstanciaPorPacienteStr;
			
			
			if (!ValoresPorDefecto.getIncluirTipoPacienteCirugiaAmbulatoria(institucion).equals(ConstantesBD.acronimoSi))
			{
				consulta+=" and c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' ";
			}
			
			logger.info("\n consulta de estancia automatica --> "+consulta);
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaEstancia));
			pst.setInt(4,idCuenta);
			pst.setInt(5,institucion);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaCuentaEstanciaPorPaciente de SqlBaseEstanciaAutomaticaDao: "+e);
			return null;
		}
	}
	
	/**
	 * M�todo usaod para consultar los registros del registro LOG
	 * de Estancia Autom�tica
	 * @param con
	 * @param anio
	 * @param mes
	 * @param area
	 * @param tipo
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultaLogEstanciaAutomatica(Connection con,int anio,int mes,int area,int tipo,String usuario,int centroAtencion,String consultaLogEstanciaAutomaticaStr, String indica)
	{
		//columnas del listado
		String[] columnas={
				"tipo",
				"fecha_inicial",
				"fecha_final",
				"fecha_grabacion",
				"area",
				"paciente",
				"usuario",
				"inconsistencia",
				"reporte",
				"ind_gen_est"
			};	
		try
		{
			String consulta=consultaLogEstanciaAutomaticaStr;
			Date fechaInicial;
			Date fechaFinal;
			int nuevoDia=0;
			
			consulta += " AND e.centro_atencion = "+centroAtencion;
			
			//******SE REVISA ESTADO DE PAR�METROS ENTRANTES**********
			//si se parametriza la b�suqeda por usuario
			if(!usuario.equals(""))
				consulta+=" AND e.usuario='"+usuario+"'";
			//si se realiza b�squeda por paciente
			if(tipo==ConstantesBD.codigoTipoEstanciaPorPaciente)
				consulta+=" AND e.tipo="+tipo;
			//si se realiza b�squeda por �rea
			if(tipo==ConstantesBD.codigoTipoEstanciaPorArea)
			{
				//se debe verificar si se parametriz� �rea
				if(area<0)
					consulta+=" AND e.tipo="+tipo;
				else
					consulta+=" AND e.tipo="+tipo+" AND e.area="+area;
			}
//			si se parametriza la b�suqeda por Indicativo generacion
			if(!indica.equals(""))
				consulta+=" AND e.ind_gen_est='"+indica+"'";
			
			
			
			
			//*****SE EDITA LA FECHA DE CONSULTA*******************
			//fecha inicial
			GregorianCalendar calendario=new GregorianCalendar(anio,mes-1,1);
			fechaInicial=calendario.getTime();
			//fecha final
			nuevoDia=calendario.getMaximum(Calendar.DAY_OF_MONTH);
			calendario=new GregorianCalendar(anio,mes-1,nuevoDia);
			fechaFinal=calendario.getTime();
			//***********************************************************
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			
			
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaLogEstanciaAutomatica de SqlBaseEstanciaAutomaticaDao:"+e);
			return null;
		}
	}
	
	/**
	 * M�todo que consulta el numero de estancias generadas para una cuenta 
	 * en un rango de fechas determinado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int numeroEstanciasPaciente(Connection con,HashMap campos)
	{
		try
		{
			int numero = 0;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(numeroEstanciasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("idCuenta"));
			pst.setObject(2,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString()));
			pst.setObject(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				numero = rs.getInt("cuenta");
			
			return numero;
		}
		catch(SQLException e)
		{
			logger.error("Error en numeroEstanciasPaciente de SqlBaseEstanciaAutomaticaDao: "+e);
			return 0;
		}
	}

}
