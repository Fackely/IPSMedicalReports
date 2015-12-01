/*
 * Created on Feb 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.historiaClinica.DtoClasificacionesTriage;
import com.princetonsa.dto.historiaClinica.DtoTriage;
import com.princetonsa.dto.historiaClinica.DtoTriage.DtoSignosVitalesTriage;

/**
 * @author sebacho
 *
 * Clase para las transacciones de Triage
 */
public class SqlBaseTriageDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTriageDao.class);
	/**
	 * Para insertar un signo vital del ingreso del triage
	 */
	private final static String insertarSignosStr="insert into signos_vitales_triage(consecutivo_triage,consecutivo_triage_fecha,codigo_signo,valor) values(?,to_char(current_date,'yyyy'),?,?)";
	
	/**
	 * SQL para obtener el consecutivo del triage
	 */
	private final static String obtenerConsecutivoStr="select max(consecutivo) from triage where consecutivo_fecha=to_char(current_date,'yyyy')";
	
	/**
	 * Carga el resumen del triage ingresado
	 */
	private final static String obtenerResumenTriageStr=" select " +
														" vt.consecutivo, " +
														" vt.consecutivo_fecha, " +
														" vt.fecha," +
														" vt.hora, " +
														" vt.primer_nombre, " +
														" case when vt.segundo_nombre is null then '' else vt.segundo_nombre end as segundo_nombre, " +
														" vt.primer_apellido, " +
														" case when vt.segundo_apellido is null then '' else vt.segundo_apellido end as segundo_apellido, "+
														" case when vt.fecha_nacimiento is null then '' else to_char(vt.fecha_nacimiento,'DD/MM/YYYY') end as fecha_nacimiento, " +
														" vt.acro_tipo_identificacion, " +
														" vt.tipo_identificacion, " +
														" vt.numero_identificacion, " +
														" case when vt.convenio is null then '' else vt.convenio end as convenio, " +
														" case when vt.otro_convenio is null then '' else vt.otro_convenio end as otro_convenio," +
														" case when vt.tipo_afiliado is null then '' else vt.tipo_afiliado end as tipo_afiliado," +
														" case when vt.id_cotizante is null then '' else vt.id_cotizante end as id_cotizante, "+
														" case when vt.motivo_consulta is null then '' else vt.motivo_consulta end as motivo_consulta, " +
														" case when vt.numero_triage is null then 0 else vt.numero_triage end as numero_triage, " +
														" case when vt.categoria_triage is null then '' else vt.categoria_triage end as categoria_triage, " +
														" vt.antecedentes, " +
														" case when vt.destino is null then '' else vt.destino end as destino," +
														" vt.observaciones_generales, " +
														" vt.login, " +
														" vt.usuario, " +
														" vt.institucion," +
														" vt.nombresala as sala," +
														" case when vt.signosintoma is null then '' else vt.signosintoma end as signo_sintoma," +
														" case when vt.colornombre is null then '' else vt.colornombre end as colornombre, " +
														" getnomcentroatencion(vt.codigocentroatencion) AS centro_atencion," +
														" vt.accidente_trabajo," +
														" vt.codigo_convenio_arp," +
														" vt.nombre_convenio_arp," +
														" ct.codigo as codigoclastriage," +
														" ct.descripcion as descclastriage  "  +
														" from view_triage vt " +
														" left outer join pacientes_triage pt on (pt.consecutivo_triage=vt.consecutivo and pt.consecutivo_fecha_triage=vt.consecutivo_fecha) " +
														" left outer join clasificacion_triage ct on (ct.codigo=pt.clasificacion_triage) " +
														" where consecutivo=? " +
														" AND consecutivo_fecha=to_char(current_date,'yyyy') ";
	/**
	 * 
	 * Carga de todos los triage
	 */
	private final static String cargarTriageStr=" select consecutivo, " +
												" consecutivo_fecha, " +
												" to_char(fecha, 'YYYY-MM-DD') as fecha, " +
												" hora, " +
												" primer_nombre, " +
												" segundo_nombre, " +
												" primer_apellido, " +
												" segundo_apellido, " +
												" case when fecha_nacimiento is null then '' else to_char(fecha_nacimiento,'DD/MM/YYYY') end as fecha_nacimiento, " +
												" acro_tipo_identificacion, " +
												" tipo_identificacion, " +
												" numero_identificacion, " +
												" case when convenio is null then '' else convenio end as convenio, " +
												" case when codigo_convenio is null then 0 else codigo_convenio end as codigo_convenio, " +
												" case when otro_convenio is null then '' else otro_convenio end as otro_convenio, " +
												" tipo_afiliado, " +
												" codigo_tipo_afiliado, " +
												" id_cotizante, " +
												" case when motivo_consulta is null then '' else motivo_consulta end as motivo_consulta, " +
												" case when numero_triage is null then 0 else numero_triage end as numero_triage, " +
												" case when categoria_triage is null then '' else categoria_triage end as categoria_triage, " +
												" antecedentes, " +
												" case when destino is null then '' else destino end as destino, " +
												" codigo_destino, " +
												" observaciones_generales, " +
												" login, " +
												" usuario, " +
												" case when colornombre is null then '' else colornombre end as colornombre, " +
												" case when signosintoma is null then '' else signosintoma end as signosintoma, " +
												" case when codigosala is null then 0 else codigosala end as codigosala, " +
												" case when nombresala is null then '' else nombresala end as nombresala, " +
												" case when urgencias is null then '' else urgencias end as urgencias, " +
												" numeroadmision, " +
												" institucion, " +
												" getnomcentroatencion(codigocentroatencion) AS centro_atencion," +
												" accidente_trabajo," +
												" codigo_convenio_arp," +
												" nombre_convenio_arp "  +
												" from view_triage " +
												" WHERE 1 = 1 " ;
												
	
	/**
	 * Carga el detalle del triage específico
	 * 
	 */
	private final static String obtenerdetalleTriageStr="select no_responde_llamado, " +
														" consecutivo," +
														" consecutivo_fecha, " +
														" fecha,hora, " +
														" primer_nombre, " +
														" segundo_nombre, " +
														" primer_apellido, " +
														" segundo_apellido, "+
														" case when fecha_nacimiento is null then '' else to_char(fecha_nacimiento,'DD/MM/YYYY') end as fecha_nacimiento, " +
														" acro_tipo_identificacion, " +
														" tipo_identificacion, " +
														" numero_identificacion, " +
														" convenio, " +
														" otro_convenio, " +
														" tipo_afiliado, " +
														" id_cotizante"+
														",case when motivo_consulta is null then '' else motivo_consulta end as motivo_consulta, " +
														" case when numero_triage is null then 0 else numero_triage end as numero_triage, " +
														" case when categoria_triage is null then '' else categoria_triage end as categoria_triage, " +
														" antecedentes, " +
														" case when destino is null then '' else destino end as destino, " +
														" observaciones_generales, " +
														" login, " +
														" usuario," +
														" case when colornombre is null then '' else colornombre end as colornombre," +
														" case when signosintoma is null then '' else signosintoma end as signosintoma," +
														" urgencias, " +
														" codigosala, " +
														" case when nombresala is null then '' else nombresala end as nombresala," +
														" institucion, " +
														" accidente_trabajo," +
														" codigo_convenio_arp," +
														" nombre_convenio_arp "  +
														" from view_triage " +
														" where consecutivo=? and consecutivo_fecha=?";
	/**
	 * 
	 * Actualiza la modificación de Observación general
	 */
	private final static String actualizarTriageStr = " UPDATE triage " +
												      "	SET observaciones_generales=? ," +
												      " no_responde_llamado = ? " +
												 	   " WHERE consecutivo=? and consecutivo_fecha=?";
	/**
	 * Cadena implementada para insertar Triage
	 */
	private final static String insertarTriageStr = "INSERT INTO triage " +
		"(consecutivo," +
		"consecutivo_fecha," +
		"fecha," +
		"hora," +
		"primer_nombre," +
		"segundo_nombre," +
		"primer_apellido," +
		"segundo_apellido," +
		"fecha_nacimiento," +
		"tipo_identificacion," +
		"numero_identificacion," +
		"convenio," +
		"otro_convenio," +
		"tipo_afiliado," +
		"id_cotizante," +
		"categoria_triage," +
		"antecedentes," +
		"destino," +
		"observaciones_generales," +
		"login," +
		"usuario," +
		"no_responde_llamado," +
		"signo_sintoma," +
		"sala_espera," +
		"institucion," +
		"centro_atencion," +
		"accidente_trabajo," +
		"convenio_arp_afiliado) " +
		"VALUES " +
		"(?,to_char(CURRENT_DATE,'YYYY'),CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que actualiza el estado del paciente que estuvo registrado para Triage
	 */
	private final static String actualizarPacienteTriageStr = " UPDATE " +
			"pacientes_triage SET " +
			"atendido = "+ValoresPorDefecto.getValorTrueParaConsultas()+", " +
			"consecutivo_triage = ?, " +
			"consecutivo_fecha_triage = to_char(current_date,'YYYY') " +
			"WHERE codigo = ?";
	
	/**
	 * Cadena implementada para cargar los signos vitales triage
	 */
	private final static String cargarSignosVitalesTriageStr = " SELECT " +
		"t.codigo_signo As codigo, " +
		"s.nombre," +
		"s.unidad_medida," +
		"t.valor " +
		"FROM " +
		"signos_vitales_triage t " +
		"INNER JOIN signos_vitales s ON(s.codigo=t.codigo_signo) " +
		"WHERE t.consecutivo_triage = ? AND t.consecutivo_triage_fecha = ?";
	/**
	 * @param con
	 * @param codigo
	 * @param consecutivo
	 * @param valor
	 * @return
	 */
	public static int insertarSignosVitalesTriage(Connection con, int codigo,
			String consecutivo, String valor,String estado) {
		
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;
		try
		{
			//inicio de transacción
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			
			
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(insertarSignosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setObject(1,consecutivo);
			statement.setInt(2,codigo);
			statement.setString(3,valor);
			
			resp=statement.executeUpdate();
			//fin de transacción
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resp;

		}
		catch(SQLException e)
		{
			
			logger.error("Error insertando signo vital triage - SqlBaseTriageDao: "+e);
			return -1;
		}
	}
	
	
	/**
	 * Función que carga el consecutivo del último ingreso del triage
	 * @param con
	 * @return
	 */
	public static int obtenerConsecutivo(Connection con){
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultSetDecorator rs=null;
		int consecutivo=-1;
			try {
				
				Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				rs=new ResultSetDecorator(st.executeQuery(obtenerConsecutivoStr));
				if(rs.next()){
						consecutivo=rs.getInt(1);
					}
				return consecutivo;
				
			} catch (SQLException e) {
				try {
					myFactory.abortTransaction(con);
				} catch (SQLException e1) {
					e1.printStackTrace();}
				logger.error("Error obteniendo consecutivo SQLBaseTriageDao: "+e);
				return -1;
			}
		}
	
	/**
	 * Carga resumen de ingreso de triage
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static Collection resumenTriage(Connection con,String consecutivo)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultSetDecorator rs=null;
		try 
		{
			//si el consecutivo es -1 carga todos los triage
			if(consecutivo.equals(""))
			{
				Statement stt=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				rs=new ResultSetDecorator(stt.executeQuery(cargarTriageStr+"order by consecutivo_fecha desc, consecutivo desc" ));
			}
			//de lo contrario solo consulta el triage deacuerdo a un consecutivo específico
			else
			{
				
				PreparedStatementDecorator st= new PreparedStatementDecorator(con.prepareStatement(obtenerResumenTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				st.setObject(1,consecutivo);
				rs=new ResultSetDecorator(st.executeQuery());
			}
			
			return UtilidadBD.resultSet2Collection(rs);
		} 
		catch (SQLException e) 
		{
			try 
			{
				myFactory.abortTransaction(con);
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			logger.error("Error cargando resumen de triage SQLBaseTriageDao: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo para buscar un triage por medio de busqueda avanzada
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCalificacion
	 * @param codigoDestino
	 * @param admision
	 * @param codigoSala
	 * @param centroAtencion 
	 * @return
	 */
	public static Collection buscarTriage(Connection con, String fechaInicial, String fechaFinal, int codigoCalificacion, int codigoDestino, String admision, int codigoSala,int centroAtencion )
	{
		PreparedStatementDecorator ps = null;
		try 
		{
			String avanzadaStr = "";
			
			avanzadaStr += " AND codigocentroatencion = "+centroAtencion;
			
			if(!fechaInicial.equals("") && !fechaFinal.equals(""))
			{
				avanzadaStr+=" AND to_char(fecha , 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
			}
			if(codigoCalificacion != -1)
			{
				avanzadaStr+=" AND numero_triage="+codigoCalificacion;
			}
			if(codigoDestino != -1)
			{
				avanzadaStr+=" AND codigo_destino="+codigoDestino;
			}
			if(admision.equals("true"))
			{
				avanzadaStr+=" AND numeroadmision IS NOT NULL";
			}
			if(admision.equals("false"))
			{
				avanzadaStr+=" AND numeroadmision IS NULL";
			}
			if(codigoSala != -1)
			{
				avanzadaStr+=" AND codigoSala="+codigoSala;
			}
			String consulta= cargarTriageStr + avanzadaStr +" order by consecutivo_fecha desc, consecutivo desc ";
			logger.info("Consulta=======> "+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.error("Error buscando triage [SQLBaseTriageDao] : "+e);
			return null;
		}
	}
	/**
	 * Carga el detalle de un triage seleccionado de la lista de consulta
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @return
	 */
	public static Collection cargarTriage(Connection con,String consecutivo,String consecutivo_fecha){
			ResultSetDecorator rs=null;
			try {
				PreparedStatementDecorator st= new PreparedStatementDecorator(con.prepareStatement(obtenerdetalleTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				st.setString(1,consecutivo);
				st.setString(2,consecutivo_fecha);
				rs=new ResultSetDecorator(st.executeQuery());
				return UtilidadBD.resultSet2Collection(rs);
			} catch (SQLException e) {
					logger.error("Error cargando detalle de triage SQLBaseTriageDao: "+e);
					return null;
				}
		}
	/**
	 * Función que actualiza las observaciones generales de un triage específico
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @param observacion
	 * @return
	 */
	public static int actualizarTriage(Connection con,String consecutivo,String consecutivo_fecha,String observacion, String noRespondioLLamado)
	{
		
		try 
		{
			PreparedStatementDecorator st= new PreparedStatementDecorator(con.prepareStatement(actualizarTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			st.setString(1,observacion);
			if(noRespondioLLamado.trim().equals("true"))
			{
				st.setBoolean(2, true);
			}
			else
			{
				st.setBoolean(2, false);
			}
			st.setString(3,consecutivo);
			st.setString(4,consecutivo_fecha);
			return st.executeUpdate();
			
		} catch (SQLException e) {
				logger.error("Error actualizando triage SQLBaseTriageDao: "+e);
				return -1;
			}
		}
	
	/**
	 * Función que toma los datos de búsqueda necesaria para 
	 * realizar el reporte de triage
	 * @param con
	 * @param fechainicial
	 * @param fechafinal
	 * @param usuario
	 * @param responsable
	 * @param centroAtencion
	 * @return
	 */
	public static Collection reporteTriage(Connection con,String fechainicial,String fechafinal,String usuario,String responsable,int centroAtencion)
	{
		String reporteTriageStr="SELECT usuario,fecha,fecha_nacimiento,numero_triage FROM view_triage  WHERE numero_triage IS NOT NULL ";
		int hay_fechas=0;
		int hay_usuario=0;
		int hay_responsable=0;
		int contador=1;
		ResultSetDecorator rs=null;
		try 
		{
			reporteTriageStr+= " AND codigocentroatencion = "+centroAtencion;
			
			if(!fechainicial.equals("")){
					reporteTriageStr+=" AND fecha BETWEEN ? AND ?";
					hay_fechas=1;
				}
			if(!usuario.equals("0")&&!usuario.equals("Todos")){
				reporteTriageStr+=" AND login=?";
				hay_usuario=1;
				}
			if(responsable.equals("Otra")){
				reporteTriageStr+=" AND convenio is null AND otro_convenio IS NOT NULL ";
				responsable=" ";
			}
			if(!responsable.equals(" ")){
				reporteTriageStr+=" AND convenio=?";
				hay_responsable=1;
			}
			reporteTriageStr+=" ORDER BY fecha";
			PreparedStatementDecorator st= new PreparedStatementDecorator(con.prepareStatement(reporteTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(hay_fechas==1){
				st.setString(contador,UtilidadFecha.conversionFormatoFechaABD(fechainicial));
				contador++;
				st.setString(contador,UtilidadFecha.conversionFormatoFechaABD(fechafinal));
				contador++;
			}
			if(hay_usuario==1){
				st.setString(contador,usuario);
				contador++;
				}
			if(hay_responsable==1){
				st.setString(contador,responsable);
				}
			rs=new ResultSetDecorator(st.executeQuery());
			return UtilidadBD.resultSet2Collection(rs);
		} 
		catch (SQLException e) 
		{
				logger.error("Error cargando datos del reporte triage SQLBaseTriageDao: "+e);
				return null;
		}
	}
	
	
	
	/**
	 * Método implementado para insertar el triage
	 * @param con
	 * @param datosTriage
	 * @param estado
	 * @return
	 */
	public static int insertarTriage(Connection con,HashMap datosTriage,String estado)
	{
		try
		{
			//	inicio de transacción
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				UtilidadBD.iniciarTransaccion(con);
			}
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,datosTriage.get("consecutivo"));
			pst.setObject(2,datosTriage.get("primerNombre"));
			
			if(datosTriage.get("segundoNombre").toString().equals(""))
				pst.setNull(3,Types.VARCHAR);
			else
				pst.setObject(3,datosTriage.get("segundoNombre"));
			
			pst.setObject(4,datosTriage.get("primerApellido"));
			
			if(datosTriage.get("segundoApellido").toString().equals(""))
				pst.setNull(5,Types.VARCHAR);
			else
				pst.setObject(5,datosTriage.get("segundoApellido"));
			if(datosTriage.get("fechaNacimiento").toString().equals(""))
				pst.setNull(6,Types.DATE);
			else
				pst.setObject(6,datosTriage.get("fechaNacimiento"));
			
			pst.setObject(7,datosTriage.get("tipoId"));
			pst.setObject(8,datosTriage.get("numeroId"));
			
			if(datosTriage.get("convenio").toString().equals(""))
				pst.setNull(9,Types.INTEGER);
			else
				pst.setObject(9,datosTriage.get("convenio"));
			
			if(datosTriage.get("convenio").toString().equals("")&&!datosTriage.get("otroConvenio").toString().equals(""))
				pst.setObject(10,datosTriage.get("otroConvenio"));
			else
				pst.setNull(10,Types.VARCHAR);
			
			if(datosTriage.get("tipoAfiliado").toString().equals(""))
				pst.setNull(11,Types.VARCHAR);
			else
				pst.setObject(11,datosTriage.get("tipoAfiliado"));
			
			if(!UtilidadTexto.isEmpty(datosTriage.get("idCotizante")+""))
				pst.setObject(12,datosTriage.get("idCotizante"));
			else
				pst.setNull(12,Types.VARCHAR);
			
			if(datosTriage.get("categoriaTriage").toString().equals(""))
				pst.setNull(13,Types.NUMERIC);
			else
				pst.setObject(13,datosTriage.get("categoriaTriage"));
			
			pst.setObject(14,datosTriage.get("antecedentes"));
			
			if(datosTriage.get("destino").toString().equals(""))
				pst.setNull(15,Types.NUMERIC);
			else
				pst.setObject(15,datosTriage.get("destino"));
			
			pst.setObject(16,datosTriage.get("observacionesGenerales"));
			pst.setObject(17,datosTriage.get("login"));
			pst.setObject(18,datosTriage.get("usuario"));
			pst.setBoolean(19,UtilidadTexto.getBoolean(datosTriage.get("NoRespondioLlamado").toString()));
			
			if(datosTriage.get("signoSintoma").toString().equals(""))
				pst.setNull(20,Types.NUMERIC);
			else
				pst.setObject(20,datosTriage.get("signoSintoma"));
			
			if(datosTriage.get("sala").toString().equals(""))
				pst.setNull(21,Types.INTEGER);
			else
				pst.setObject(21,datosTriage.get("sala"));
			
			pst.setObject(22,datosTriage.get("institucion"));
			pst.setObject(23,datosTriage.get("centroAtencion"));
			pst.setString(24,datosTriage.get("accidenteTrabajo").toString());
			if(!datosTriage.get("codigoArpAfiliado").toString().equals(""))
				pst.setInt(25,Integer.parseInt(datosTriage.get("codigoArpAfiliado").toString()));
			else
				pst.setNull(25,Types.INTEGER);
			int resp = pst.executeUpdate();
			
			if(resp>0)
			{
				//fin de transacción
				if(estado.equals(ConstantesBD.finTransaccion))
				{
					UtilidadBD.finalizarTransaccion(con);
				}
				return resp;
			}
			else
				return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarTriage de SqlBaseTriageDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para actualizar el estado del paciente que estuvo
	 * registrado para Triage cambiando su estado a atendido e ingresando su consecutivo triage
	 * respectivo
	 * @param con
	 * @param codigo
	 * @param consecutivoTriage
	 * @return
	 */
	public static int actualizarPacienteParaTriage(Connection con,String codigo,String consecutivoTriage)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarPacienteTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,consecutivoTriage);
			pst.setObject(2,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPacienteParaTriage de SqlBaseTriageDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para cargar los signos vitales del Triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @return
	 */
	public static HashMap cargarSignosVitalesTriage(Connection con,String consecutivo,String consecutivo_fecha)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarSignosVitalesTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,consecutivo);
			pst.setString(2,consecutivo_fecha);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSignosVitalesTriage de SQlBaseTriageDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método que consulta los datos del triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoTriage obtenerDatosTriage(Connection con,HashMap campos)
	{
		DtoTriage dtoTriage = new DtoTriage();
		
		try
		{
			//******************SE TOMAN LOS PARÁMETROS**************************
			String consecutivoTriage = campos.get("consecutivoTriage").toString();
			String consecutivoFechaTriage = campos.get("consecutivoFechaTriage").toString();
			//*******************************************************************
			
			String consulta = "SELECT " +
				"primer_nombre," +
				"coalesce(segundo_nombre,'') as segundo_nombre," +
				"primer_apellido," +
				"coalesce(segundo_apellido,'') as segundo_apellido," +
				"coalesce(to_char(fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_nacimiento " +
				"from triage " +
				"WHERE consecutivo = ? and consecutivo_fecha = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(consecutivoTriage));
			pst.setString(2,consecutivoFechaTriage);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				dtoTriage.setPrimerNombre(rs.getString("primer_nombre"));
				dtoTriage.setSegundoNombre(rs.getString("segundo_nombre"));
				dtoTriage.setPrimerApellido(rs.getString("primer_apellido"));
				dtoTriage.setSegundoApellido(rs.getString("segundo_apellido"));
				dtoTriage.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				dtoTriage.setConsecutivoTriage(consecutivoTriage);
				dtoTriage.setConsecutivoFechaTriage(consecutivoFechaTriage);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosTriage: "+e);
		}
		
		return dtoTriage;
	}
	
	/**
	 * 
	 * @param consecutivoTriage
	 * @param consecutivoFecha
	 * @return
	 */
	public static DtoTriage consultarInfoResumenTriagePorCuenta(int codigoCuenta)
	{
		DtoTriage dto=new DtoTriage();
		String cadena=" select t.consecutivo, " +
								" t.consecutivo_fecha as consecutivofecha," +
								" to_char(t.fecha,'dd/mm/yyyy') as fecha," +
								" t.hora," +
								" t.usuario as profesional," +
								" historiaclinica.getmotivoconsultatriage(t.signo_sintoma) AS motivoconsulta," +
								" historiaclinica.getnombrecategoriatriage(t.categoria_triage) as categoriatriage," +
								" col.nombre as colornombre," +
								" t.observaciones_generales as observaciones " +
						" from admisiones_urgencias au " +
						" inner join historiaclinica.triage t on (t.numero_admision=au.codigo) " +
						" LEFT OUTER JOIN historiaclinica.categorias_triage clast ON(t.categoria_triage=clast.consecutivo) " +
						" LEFT OUTER JOIN historiaclinica.colores_triage col ON(clast.color=col.codigo) " +
						" where au.cuenta="+codigoCuenta;
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadena);
		
		try
		{
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				dto.setConsecutivoTriage(rs.getString("consecutivo"));
				dto.setConsecutivoFechaTriage(rs.getString("consecutivofecha"));
				dto.setFechaTriage(rs.getString("fecha"));
				dto.setHoraTriage(rs.getString("hora"));
				dto.setProfesional(rs.getString("profesional"));
				dto.setMotivoConsulta(rs.getString("motivoconsulta"));
				dto.setCategoriaTriage(rs.getString("categoriatriage"));
				dto.setNombreColor(rs.getString("colornombre"));
				dto.setObservaciones(rs.getString("observaciones"));
				
				//cargar signos vitales
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,cargarSignosVitalesTriageStr);
				pst.setString(1,dto.getConsecutivoTriage());
				pst.setString(2,dto.getConsecutivoFechaTriage());
				ResultSetDecorator rst=new ResultSetDecorator(pst.executeQuery());
				while(rst.next())
				{
					DtoSignosVitalesTriage dtoSignos=dto.new DtoSignosVitalesTriage();
					dtoSignos.setCodigoSigno(rst.getInt("codigo"));
					dtoSignos.setNombreSigno(rst.getString("nombre"));
					dtoSignos.setValor(rst.getString("valor"));
					dtoSignos.setUnidadMedida(rst.getString("unidad_medida"));
					dto.getSignosVitales().add(dtoSignos);
				}
				rst.close();
				pst.close();				
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			logger.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return dto;
	}


	/**
	 * 
	 * @return
	 */
	public static ArrayList<DtoClasificacionesTriage> consultarClasificacionesTriage() 
	{
		ArrayList<DtoClasificacionesTriage> resultado=new ArrayList<DtoClasificacionesTriage>();
		String consulta="SELECT codigo,descripcion,activo from historiaclinica.clasificacion_triage where activo='"+ConstantesBD.acronimoSi+"'";
		Connection con =UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoClasificacionesTriage dto=new DtoClasificacionesTriage();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setActivo(rs.getString("activo"));
				resultado.add(dto);
			}
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}


	public static Collection buscarTriage(Connection con, String tipoID,String numeroID) 
	{
		PreparedStatementDecorator ps = null;
		try 
		{
			String avanzadaStr = "";
			avanzadaStr += " AND tipo_identificacion = '"+tipoID+"' AND numero_identificacion = '"+numeroID+"'";
			
			String consulta= cargarTriageStr + avanzadaStr +" order by consecutivo_fecha desc, consecutivo desc ";
			logger.info("Consulta=======> "+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.error("Error buscando triage [SQLBaseTriageDao] : "+e);
			return null;
		}
	}
	
	
	
	
	
}
