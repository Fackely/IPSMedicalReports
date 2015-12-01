package com.princetonsa.dao.sqlbase.enfermeria;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ibm.icu.util.ULocale.Type;
import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoDetalleCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseProgramacionCuidadoEnferDao 
{		
	
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseProgramacionCuidadoEnferDao.class);
	
	
	/**
	 * Cadena que consulta los tipos de frecuencia por institución
	 * */	
	private static final String strConsultaTiposFrecInst = "SELECT " +
														   "tf.codigo," +														   
														   "tf.nombre " +
														   "FROM tipo_frec_institucion tfi " +
														   "INNER JOIN tipos_frecuencia tf ON (tf.codigo = tfi.tipo_frecuencia) " +
														   "WHERE tfi.institucion = ? " +
														   "ORDER BY tf.nombre ";
	
	/**
	 * Consulta las frecuencias de los cuidados de enfermeria 
	 * */
	private static final String strConsultaFrecuenciaCuidadoEnfer	=  "SELECT " +
																	   "f.codigo," +
																	   "f.ingreso," +
																	   "f.otro_cuidado," +
																	   "f.frecuencia," +
																	   "coalesce(f.tipo_frecuencia,"+ConstantesBD.codigoNuncaValido+") AS tipo_frecuencia," +
																	   "f.activo," +
																	   "f.periodo," +
																	   "coalesce(f.tipo_frec_periodo,"+ConstantesBD.codigoNuncaValido+") AS tipo_frec_periodo, " +
																	   "f.cuidado_enfer_cc_inst," +
																	   "tce.descripcion AS nombre_cuidado_enfer, " +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frecuencia),'') AS nombre_tipo_frec," +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frec_periodo),'') AS nombre_tipo_frec_per," +
																	   "cecc.cuidado_enfermeria," +
																	   "'false' AS esotro," +
																	   "(SELECT coalesce(MAX(enfer.frec_cuidado_enfer),0) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= ?) " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo AND f.activo = '"+ConstantesBD.acronimoSi+"') AS cuantodetalle "+
																	   "FROM frec_cuidados_enfer f " +
																	   "INNER JOIN cuidado_enfer_cc_inst cecc ON (cecc.codigo = f.cuidado_enfer_cc_inst) " +
																	   "INNER JOIN tipo_cuidado_enfermeria tce ON (tce.codigo = cecc.cuidado_enfermeria) " +
																	   "WHERE f.otro_cuidado IS NULL ?=? " +
																	   "" +
																	   "UNION " +
																	   "" +
																	   "SELECT " +
																	   "f.codigo," +
																	   "f.ingreso," +
																	   "f.otro_cuidado," +
																	   "f.frecuencia," +
																	   "coalesce(f.tipo_frecuencia,"+ConstantesBD.codigoNuncaValido+") AS tipo_frecuencia," +
																	   "f.activo," +
																	   "f.periodo," +
																	   "coalesce(f.tipo_frec_periodo,"+ConstantesBD.codigoNuncaValido+") AS tipo_frec_periodo, " +
																	   "f.cuidado_enfer_cc_inst," +
																	   "toc.descripcion AS nombre_cuidado_enfer, " +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frecuencia),'') AS nombre_tipo_frec," +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frec_periodo),'') AS nombre_tipo_frec_per," +
																	   ConstantesBD.codigoNuncaValido+" AS cuidado_enfermeria, "+																	   
																	   "'true' AS esotro, "+
																	   "(SELECT coalesce(MAX(enfer.frec_cuidado_enfer),0) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= ?) " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo AND f.activo = '"+ConstantesBD.acronimoSi+"') AS cuantodetalle "+																	   
																	   "FROM frec_cuidados_enfer f " +
																	   "INNER JOIN tipo_otro_cuidado_enf toc ON (toc.codigo = f.otro_cuidado) " +																	   
																	   "WHERE f.otro_cuidado IS NOT NULL ?=? ";
																	   
	/**
	 * Inserta Registros de frecuencias de los cuidados de enfermeria 
	 * */
	private static final String strInsertarFrecuenciaCuidadoEnfer = "INSERT INTO frec_cuidados_enfer " +
																	   "(" +
																	   "codigo," +
																	   "ingreso," +
																	   "cuidado_enfer_cc_inst," +
																	   "otro_cuidado," +
																	   "frecuencia," +
																	   "tipo_frecuencia," +
																	   "activo," +
																	   "periodo," +
																	   "tipo_frec_periodo," +
																	   "fecha_modifica," +
																	   "hora_modifica," +
																	   "usuario_modifica) " +
																	   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ";
		
	/**
	 * Actualiza Registros de frecuencias de los cuidados de enfermeria en el registro de enfermeria
	 *  */ 
	private static final String strActualizarFrecuenciaCuidadoRegEnfer = "UPDATE frec_cuidado_reg_enfer SET " +
																		 "frecuencia = ?," +
																		 "tipo_frecuencia = ?," +
																		 "fecha_modifica = ?, " +
																		 "hora_modifica = ?, " +
																		 "usuario_modifica = ? " +
																		 "WHERE " +
																		 "registro_enfer = ? AND " +
																		 "cuidado_enfer_cc_inst = ? ";
	
	/**
	 * 
	 * */
	private static final String strActualizarEstadoFrecuenciaCuidados = "UPDATE frec_cuidados_enfer SET " +
																		"activo = ?, " +
																		"fecha_modifica = ?, " +
																		"hora_modifica = ?, " +
																		"usuario_modifica = ? " +
																		"WHERE codigo =  ? ";

	/**
	 * Elimina registros de frecuencias Cuidados en Registro de enfermeria solo si 
	 * no posee programacion
	 * */
	private static final String strEliminarFrecuenciaCuidadoRegEnferCod = "DELETE " +
																	   	  "FROM frec_cuidados_enfer " +
																	      "WHERE codigo = ? " +
																	      "AND (SELECT COUNT(p.codigo) FROM prog_cuidados_enfer p WHERE p.frec_cuidado_enfer = ?) <= 0 ";
	
	/**
	 * Elimina registros de frecuencias Cuidados en Registro de enfermeria
	 * */
	private static final String strEliminarFrecuenciaCuidadoRegEnfer = "DELETE " +
																	   "FROM frec_cuidado_reg_enfer " +
																	   "WHERE registro_enfer = ? AND cuidado_enfer_cc_inst = ? ";
		
	
	/**
	 * 
	 */
	private static final String cadenaConsultaPacienteAreaProgramadosStr="	SELECT distinct getnombrepersona(c.codigo_paciente) as paciente, " +
																		"getidpaciente(c.codigo_paciente) as tipoid, " +
																		"c.codigo_paciente as codigopaciente, " +
																		"g.id as codigoingreso,  " +
																		"g.consecutivo as ingreso, " +
																		"c.id as cuenta, " +
																		"p.historia_clinica as historiaclinica, " +
																		"getnombreviaingreso(c.via_ingreso) as viaingreso, " +
																		"getnombreconvenioxingreso(g.id) as convenio, " +
																		"getdiagnosticopaciente(c.id, c.via_ingreso) as diagnostico, " +
																		"getcamacuenta(c.id, c.via_ingreso) as cama " +
																		"FROM cuentas c " +
																		"inner join pacientes p on(p.codigo_paciente=c.codigo_paciente) " +
																		"inner join ingresos g on(g.id=c.id_ingreso) " +
																		"inner join frec_cuidados_enfer fc on(fc.ingreso=g.id ) " +
																		"WHERE c.area=? and g.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") ";
																																		
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaPacientePisoStr=" SELECT distinct getnombrepersona(c.codigo_paciente) as paciente, " +
																	   "getidpaciente(c.codigo_paciente) as tipoid, " +
																	   "c.codigo_paciente as codigopaciente, " +
																	   "g.id as codigoingreso, " +
																	   "g.consecutivo as ingreso, " +
																	   "c.id as cuenta, " +
																	   "p.historia_clinica as historiaclinica, " +
																	   "getnombreviaingreso(c.via_ingreso) as viaingreso, " +
																	   "getnombreconvenioxingreso(g.id) as convenio, " +
																	   "getdiagnosticopaciente(c.id, c.via_ingreso) as diagnostico, " +
																	   "getcamacuenta(c.id, c.via_ingreso) as cama " +
																"FROM cuentas c " +
																		"inner join pacientes p on(p.codigo_paciente=c.codigo_paciente) " +
																		"inner join his_camas_cuentas hc on(hc.cuenta=c.id) " +
																		"inner join ingresos g on(g.id=c.id_ingreso) " +
																		"inner join frec_cuidados_enfer fc on(fc.ingreso=g.id ) " +
																"WHERE  g.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") ";
																
	
	/**
	 * Consulta la programación de cuidados de enfermeria
	 * */
	private static final String strConsultaProgCuidadosEnfer = "SELECT " +
															   "p.codigo," +
															   "p.frec_cuidado_enfer," +
															   "to_char(p.fecha_inicio,'dd/mm/yyyy') as fecha_inicio," +
															   "p.hora_inicio," +
															   "coalesce(p.observaciones,'') as observaciones," +
															   "getnombreusuario(p.usuario_programacion) as usuario_programacion," +
															   "frec.activo," + 
															   "CASE WHEN frec.otro_cuidado IS NULL THEN coalesce(getNombreCuidadoEnfer(frec.cuidado_enfer_cc_inst,0),'') ELSE coalesce(getNombreCuidadoEnfer(frec.otro_cuidado,1),'') END AS nombrecuidado," +
															   "coalesce(frec.frecuencia||'','') AS frecuencia," +
															   "coalesce(frec.periodo||'','') AS periodo," +
															   "coalesce(getnombretipofrecuencia(frec.tipo_frecuencia),'') AS nombre_tipo_frec," +
															   "coalesce(getnombretipofrecuencia(frec.tipo_frec_periodo),'') AS nombre_tipo_frec_per," +
															   "frec.codigo AS codigo_pk_frec_cuid_enfer," +
															   "CASE WHEN frec.otro_cuidado IS NULL THEN 'false' ELSE 'true' END AS esotro," +
															   "coalesce(frec.cuidado_enfer_cc_inst||'','') AS cuidado_enfer_cc_inst," +
															   "coalesce(frec.otro_cuidado||'','') AS otro_cuidado, " +
															   ConstantesBD.separadorSplit+" AS fechasanidadas " +
															   "FROM " +
															   "prog_cuidados_enfer p " +
															   "INNER JOIN frec_cuidados_enfer frec ON (frec.codigo = p.frec_cuidado_enfer) ";
	
	/**
	 * Consulta del detalle de programación de cuidados de enfermeria
	 * */
	private static final String strConsultaDetalleProgCuidadosEnfer = "SELECT " +
																	  "codigo," +
																	  "prog_cuidado_enfer," +
																	  "to_char(fecha,'dd/mm/yyyy') as fecha," +
																	  "hora," +
																	  "activo " +
																	  "FROM " +
																	  "det_prog_cuidados_enfer " +
																	  "WHERE prog_cuidado_enfer = ? " +
																	  "ORDER BY fecha,hora ASC ";
	
	/**
	 * inserta el encabezado de programacion de cuidados de enfermeria
	 * */
	private static final String strInsertarCuidadosEnfer =  "INSERT INTO prog_cuidados_enfer(" +
															"codigo," +
															"frec_cuidado_enfer," +
															"fecha_inicio," +
															"hora_inicio," +
															"observaciones," +
															"usuario_programacion) " +
															"VALUES (?,?,?,?,?,?) ";
	
	/**
	 * inserta el detalle de programacion de cuidados de enfermeria 
	 * */
	private static final String strInsertarDetProgCuidEnfer =   "INSERT INTO det_prog_cuidados_enfer(" +
																"codigo," +
																"prog_cuidado_enfer," +
																"fecha," +
																"hora," +
																"activo) " +
																"VALUES(?,?,?,?,?) ";
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * Consulta el listado de los tipo frecuencia por institución
	 * @param Connection con
	 * @param HashMap parametros 
	 * */	
	public static ArrayList<HashMap<String,Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		HashMap datos = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposFrecInst,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				datos = new HashMap();
				datos.put("codigo",rs.getString(1));
				datos.put("descripcion",rs.getString(2));
				array.add(datos);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
		
		return array;
	}
	
	//**********************************************************************************************************************
	
	/**
	 * Consulta la informacion de las Frecuencias de los cuidados de enfermeria
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con,HashMap parametros)
	{
		ArrayList<DtoFrecuenciaCuidadoEnferia> array = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		DtoFrecuenciaCuidadoEnferia dto = new DtoFrecuenciaCuidadoEnferia();
				
		String cadena = strConsultaFrecuenciaCuidadoEnfer;
		String cadenaRelleno = " AND f.ingreso = "+parametros.get("ingreso").toString()+" AND f.activo = '"+parametros.get("activo").toString()+"' "; 
		
		if(parametros.containsKey("codigoPkFrecCuidadoEnfer") && 
			Utilidades.convertirAEntero(parametros.get("codigoPkFrecCuidadoEnfer").toString())>=0)
				cadenaRelleno += " AND f.codigo = "+parametros.get("codigoPkFrecCuidadoEnfer");			
		
		cadena = cadena.replace("?=?",cadenaRelleno);
		
		cadena += " ORDER BY nombre_cuidado_enfer ASC ";
		logger.info(UtilidadFecha.getHoraActual()+" valor de la cadena >> "+cadena);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,UtilidadFecha.getHoraActual());
			ps.setString(2,UtilidadFecha.getHoraActual());
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				dto = new DtoFrecuenciaCuidadoEnferia();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoCuidadoEnferCcInst(rs.getInt("cuidado_enfer_cc_inst"));
				dto.setCodigoOtroCuidado(rs.getInt("otro_cuidado"));
				dto.setFrecuencia(rs.getInt("frecuencia"));
				dto.setTipoFrecuencia(rs.getInt("tipo_frecuencia"));
				dto.setActivo(rs.getString("activo"));
				dto.setNombreCuidadoEnfer(rs.getString("nombre_cuidado_enfer"));
				dto.setNombreTipoFrecuencia(rs.getString("nombre_tipo_frec"));
				dto.setNombreTipoFrecuPeriodo(rs.getString("nombre_tipo_frec_per"));
				dto.setCodigoIngreso(rs.getInt("ingreso"));						
				dto.setCodigoCuidadoEnfermeria(rs.getInt("cuidado_enfermeria"));				
				dto.setEsOtroCuidado(UtilidadTexto.getBoolean(rs.getString("esotro")));
				dto.setTipoFrecuenciaPeriodo(rs.getInt("tipo_frec_periodo"));
				dto.setPeriodo(rs.getInt("periodo"));
				dto.setTieneProgramacion(rs.getInt("cuantodetalle")>0?true:false);
				array.add(dto);
			}
			
		}catch (Exception e) {
			logger.info("valor de la cadena que causa el error >> "+cadena);
			e.printStackTrace();	
		}
		return array;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param areaFiltro
	 * @param pisoFiltro
	 * @param habitacionFiltro
	 * @param programados
	 * @return
	 */
	public static HashMap consultarListadoPacientes(Connection con, String areaFiltro, String pisoFiltro, String habitacionFiltro, String programados) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena="";
		
		if(!areaFiltro.equals(""))
		{
			
			cadena=cadenaConsultaPacienteAreaProgramadosStr;
			
			if(!programados.equals(ConstantesBD.acronimoSi))
			{
				
				cadena+=" and fc.codigo not in(select p1.frec_cuidado_enfer from prog_cuidados_enfer p1 where p1.frec_cuidado_enfer=fc.codigo) ";
				
			}
			try
			{
				PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				busqueda.setInt(1, Utilidades.convertirAEntero(areaFiltro));
				logger.info("cadena >>>>>>> "+cadena);
				
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			
			cadena=cadenaConsultaPacientePisoStr;
			
			if(!programados.equals(ConstantesBD.acronimoSi))
			{	
			
				cadena+=" and fc.codigo not in(select p1.frec_cuidado_enfer from prog_cuidados_enfer p1 where p1.frec_cuidado_enfer=fc.codigo) ";
			}
			
			if(habitacionFiltro.equals(""))
				cadena+=" and hc.codigopkpiso="+pisoFiltro;
			else
				cadena+=" and hc.codigopkpiso="+pisoFiltro+" and hc.codigopkhabitacion="+habitacionFiltro;
			
			
			try
			{
				PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("cadena >>>>>>> "+cadena);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
			
		}
		
		
		return mapa;
	}
	
	//**********************************************************************************************************************
		
	/**
	 * Inserta registros de frecuencias de cuidados en Registro de Enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int insertarFrecuenciasCuidados(Connection con, HashMap parametros)
	{
		DtoFrecuenciaCuidadoEnferia dto = new DtoFrecuenciaCuidadoEnferia();
		dto = (DtoFrecuenciaCuidadoEnferia)parametros.get("DtoFrecuenciaCuidadoEnferia");
		int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_frec_cuidado_enfer");
		
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarFrecuenciaCuidadoEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,codigoPk);
			ps.setInt(2,dto.getCodigoIngreso());
			
			if(dto.getCodigoCuidadoEnferCcInst() > 0)
				ps.setInt(3,dto.getCodigoCuidadoEnferCcInst());
			else
				ps.setNull(3,Types.INTEGER);
			
			if(dto.getCodigoOtroCuidado() > 0)
				ps.setInt(4,dto.getCodigoOtroCuidado());
			else
				ps.setNull(4,Types.INTEGER);
			
			if(dto.getFrecuencia() > 0)	
				ps.setInt(5,dto.getFrecuencia());
			else
				ps.setNull(5,Types.INTEGER);
			
			if(dto.getTipoFrecuencia() > 0 && dto.getFrecuencia() > 0)
				ps.setInt(6,dto.getTipoFrecuencia());
			else
				ps.setNull(6,dto.getTipoFrecuencia());
			
			ps.setString(7,dto.getActivo());
			
			if(dto.getPeriodo() > 0)	
				ps.setInt(8,dto.getPeriodo());
			else
				ps.setNull(8,Types.INTEGER);
			
			if(dto.getTipoFrecuenciaPeriodo() > 0 && dto.getPeriodo() > 0)
				ps.setInt(9,dto.getTipoFrecuenciaPeriodo());
			else
				ps.setNull(9,Types.INTEGER);			
			
			ps.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(11,UtilidadFecha.getHoraActual());
			ps.setString(12,parametros.get("usuarioModifica").toString());
			
			if(ps.executeUpdate()>0)
				return codigoPk;
			else
				return ConstantesBD.codigoNuncaValido;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;			
	}
	
	//**********************************************************************************************************************
		
	/**
	 * Actualiza el estado de activo de las frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarEstadoFrecuenciasCuidadosRegEnfer(Connection con,HashMap parametros)
	{		
		try
		{	
			//logger.info("\n\n\n eliminar frecuencias >> "+parametros.get("codigoPk").toString()+" "+strEliminarFrecuenciaCuidadoRegEnferCod);
			//Borra la información de la frecuencia en el caso de que no tenga informacion de programacion
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarFrecuenciaCuidadoRegEnferCod,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
		
			if(ps.executeUpdate()>0)
				return true;
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarEstadoFrecuenciaCuidados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("activo").toString());
			ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(3,UtilidadFecha.getHoraActual());
			ps.setString(4,parametros.get("usuarioModifica").toString());				
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			
			return ps.executeUpdate()>0;
						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//**********************************************************************************************************************
	
	/**
	 * actualiza la informacion de frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarFrecuenciasCuidadosRegEnfer(Connection con, HashMap parametros)
	{
		DtoFrecuenciaCuidadoEnferia dto = new DtoFrecuenciaCuidadoEnferia();
		dto = (DtoFrecuenciaCuidadoEnferia)parametros.get("DtoFrecuenciaCuidadoEnferia");
		
		try
		{			
			//si inicializaron la frecuencia se elimina de la base de datos
			if(dto.getFrecuencia() > 0 && dto.getTipoFrecuencia() > 0)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarFrecuenciaCuidadoRegEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,dto.getFrecuencia());				
				ps.setInt(2,dto.getTipoFrecuencia());				
				ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				ps.setString(4,UtilidadFecha.getHoraActual());
				ps.setString(5,parametros.get("usuarioModifica").toString());				
				
				ps.setInt(7,dto.getCodigoCuidadoEnferCcInst());		
				
				return ps.executeUpdate()>0;
			}
			else if(dto.getFrecuencia() <= 0 && dto.getTipoFrecuencia() <= 0)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarFrecuenciaCuidadoRegEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));				
			
				ps.setInt(2,dto.getCodigoCuidadoEnferCcInst());
				
				return ps.executeUpdate()>0;
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//**********************************************************************************************************************
		
	/**
	 * Retorna la cadena sql de consulta
	 *  @param HashMap parametros
	 * */
	public static HashMap getStringSqlConsulProgCuidEnfer(HashMap parametros, String fechasAnidadas)
	{
		HashMap mapa = new HashMap();
		String cadena = strConsultaProgCuidadosEnfer;
		
		cadena +=" WHERE 1=1 ";
		
		//--------------
		if(parametros.containsKey("mostrarFechasAnidadas") && 
			parametros.get("mostrarFechasAnidadas").toString().equals(ConstantesBD.acronimoSi))
		{
			String fechasani = fechasAnidadas;
			cadena = cadena.replace(ConstantesBD.separadorSplit,fechasani);
		}
		else
		{
			cadena = cadena.replace(ConstantesBD.separadorSplit,"''");
		}
		
		//--------------		
		if(parametros.containsKey("validarRangoFecha") && 
				parametros.get("validarRangoFecha").toString().equals(ConstantesBD.acronimoSi))
		{
			if(!parametros.get("fechaHoraFiltro").toString().equals(""))			
				cadena += " AND ('"+parametros.get("fechaHoraFiltro").toString()+"' BETWEEN  to_char(p.fecha_inicio,'YYYY-MM-DD') || ' ' || p.hora_inicio and getUltFechadetprogcui(p.codigo)) ";			
		}

		//--------------
		if(parametros.containsKey("codigoCuidadoEnfer") &&
				!parametros.get("codigoCuidadoEnfer").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codigoCuidadoEnfer").toString()) >= 0)
		{
			if(parametros.get("esOtroCuidados").toString().equals(ConstantesBD.acronimoSi))			
				cadena += " AND frec.otro_cuidado = "+parametros.get("codigoCuidadoEnfer")+" ";		
			else			
				cadena += " AND frec.cuidado_enfer_cc_inst = "+parametros.get("codigoCuidadoEnfer")+" ";	
		}
						
		//--------------
		if(parametros.containsKey("ingreso") && 
				Utilidades.convertirAEntero(parametros.get("ingreso").toString()) >= 0)
			cadena += " AND frec.ingreso = "+parametros.get("ingreso").toString();

		//--------------
		if(parametros.containsKey("codigoPk") && 
				Utilidades.convertirAEntero(parametros.get("codigoPk").toString()) >= 0)
			cadena += " AND p.codigo = "+parametros.get("codigoPk").toString();
		
		//--------------
		if(parametros.containsKey("frecCuidadoEnfer") 
				&& Utilidades.convertirAEntero(parametros.get("frecCuidadoEnfer").toString()) >= 0)
			cadena += " AND frec_cuidado_enfer = "+parametros.get("frecCuidadoEnfer").toString();
		
		cadena +=" ORDER BY p.codigo DESC ";
		
		mapa.put("sql",cadena);
		return mapa;
	}
	
	//**********************************************************************************************************************
	
	/**
	 * Consulta la informacion de los programas de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoCuidadosEnfermeria> consultarProgCuidadosEnfer(Connection con, HashMap parametros)
	{
		ArrayList<DtoCuidadosEnfermeria> array = new ArrayList<DtoCuidadosEnfermeria>();		
		String cadena = strConsultaProgCuidadosEnfer;
		
		cadena +=" WHERE 1=1 ";
		//--------------
		if(parametros.containsKey("mostrarFechasAnidadas") && 
				parametros.get("mostrarFechasAnidadas").toString().equals(ConstantesBD.acronimoSi))
		{
			String fechasani = " (SELECT list(getnombremes(to_char(det.fecha,'mm')) || ' ' || to_char(det.fecha,'dd-yyyy') || ' ' || det.hora) FROM det_prog_cuidados_enfer det WHERE det.prog_cuidado_enfer = p.codigo) ";
			cadena = cadena.replace(ConstantesBD.separadorSplit,fechasani);
		}
		else
		{
			cadena = cadena.replace(ConstantesBD.separadorSplit,"''");
		}
		
		//--------------		
		if(parametros.containsKey("validarRangoFecha") && 
				parametros.get("validarRangoFecha").toString().equals(ConstantesBD.acronimoSi))
		{
			if(!parametros.get("fechaHoraFiltro").toString().equals(""))			
				cadena += " AND ('"+parametros.get("fechaHoraFiltro").toString()+"' BETWEEN p.fecha_inicio || ' ' || p.hora_inicio and getUltFechadetprogcui(p.codigo)) ";			
		}
		
		//--------------
		if(parametros.containsKey("codigoCuidadoEnfer") &&
				!parametros.get("codigoCuidadoEnfer").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codigoCuidadoEnfer").toString()) >= 0)
		{
			if(parametros.get("esOtroCuidados").toString().equals(ConstantesBD.acronimoSi))
				cadena += " AND frec.otro_cuidado = "+parametros.get("codigoCuidadoEnfer")+" ";
			else
				cadena += " AND frec.cuidado_enfer_cc_inst = "+parametros.get("codigoCuidadoEnfer")+" ";
		}
		
		//--------------
		if(parametros.containsKey("ingreso") && 
				Utilidades.convertirAEntero(parametros.get("ingreso").toString()) >= 0)
			cadena += " AND frec.ingreso = "+parametros.get("ingreso").toString();

		if(parametros.containsKey("codigoPk") && 
				Utilidades.convertirAEntero(parametros.get("codigoPk").toString()) >= 0)
			cadena += " AND p.codigo = "+parametros.get("codigoPk").toString();
		
		//--------------
		if(parametros.containsKey("frecCuidadoEnfer") 
				&& Utilidades.convertirAEntero(parametros.get("frecCuidadoEnfer").toString()) >= 0)
			cadena += " AND frec_cuidado_enfer = "+parametros.get("frecCuidadoEnfer").toString();
		
		if(parametros.containsKey("activo") 
				&& !parametros.get("activo").toString().equals(""))
			cadena += " AND activo = '"+parametros.get("activo").toString()+"' ";
		
		cadena +=" ORDER BY p.codigo DESC ";
		
		logger.info("\n\n\n\n valor de la cadena >> "+cadena);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDetalleProgCuidadosEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			ResultSetDecorator rs1;
			
			while(rs.next())
			{
				DtoCuidadosEnfermeria dto = new DtoCuidadosEnfermeria();
				dto.setCodigoPkProgramacion(rs.getInt("codigo"));
				dto.setCodigoPkFrecCuidadoEnfer(rs.getInt("frec_cuidado_enfer"));
				dto.setFechaInicio(rs.getString("fecha_inicio").toString());
				dto.setHoraInicio(rs.getString("hora_inicio").toString());
				dto.setObservaciones(rs.getString("observaciones").toString());
				dto.setUsuarioProgramacion(rs.getString("usuario_programacion"));
								
				//Almancena la informacion de la frecuencia con la que fue parametrizada el cuidados
				dto.getFrecuenciaCuidadoEnfer().setNombreCuidadoEnfer(rs.getString("nombrecuidado"));
				dto.getFrecuenciaCuidadoEnfer().setFrecuencia(Utilidades.convertirAEntero(rs.getString("frecuencia")));
				dto.getFrecuenciaCuidadoEnfer().setPeriodo(Utilidades.convertirAEntero(rs.getString("periodo")));
				dto.getFrecuenciaCuidadoEnfer().setNombreTipoFrecuencia(rs.getString("nombre_tipo_frec"));
				dto.getFrecuenciaCuidadoEnfer().setNombreTipoFrecuPeriodo(rs.getString("nombre_tipo_frec_per"));
				
				dto.getFrecuenciaCuidadoEnfer().setEsOtroCuidado(UtilidadTexto.getBoolean(rs.getString("esotro")));
				dto.getFrecuenciaCuidadoEnfer().setCodigoCuidadoEnferCcInst(Utilidades.convertirAEntero(rs.getString("cuidado_enfer_cc_inst").toString()));
				dto.getFrecuenciaCuidadoEnfer().setCodigoOtroCuidado(Utilidades.convertirAEntero(rs.getString("otro_cuidado").toString()));				
				dto.getFrecuenciaCuidadoEnfer().setCodigoCuidadoEnfermeria(Utilidades.convertirAEntero(rs.getString("codigo_pk_frec_cuid_enfer").toString()));
				
				
				//Indica si se debe cargar el datalle
				if(parametros.get("cargarDetalle").toString().equals(ConstantesBD.acronimoSi))
				{
					ps1.setInt(1,dto.getCodigoPkProgramacion());
					rs1 = new ResultSetDecorator(ps1.executeQuery());
					
					while(rs1.next())
					{
						DtoDetalleCuidadosEnfermeria dtod = new DtoDetalleCuidadosEnfermeria();
						dtod.setCodigoPkDetalleProgramacion(rs1.getInt("codigo"));
						dtod.setFecha(rs1.getString("fecha"));
						dtod.setHora(rs1.getString("hora"));
						dtod.setActivo(rs1.getString("activo"));
						dto.getDetalleCuidados().add(dtod);
					}
					
					if(dto.getDetalleCuidados().size() > 0)
					{
						//activa el indicador de inactivo para mostrar al usuario que la programacion ha sido cambiada o que 
						//el tiempo de la ultima ejecucion del cuidado ha vencido
						//compara la ultima fecha de la programacion y si es menor que la fecha/hora actual indica que esta inactivo
						logger.info("valor de la fecha >> "+dto.getDetalleCuidados().get(dto.getDetalleCuidados().size()-1).getFecha()+" "+dto.getDetalleCuidados().get(dto.getDetalleCuidados().size()-1).getHora());
						if(dto.isActivo() && 
								!UtilidadFecha.compararFechas(dto.getDetalleCuidados().get(dto.getDetalleCuidados().size()-1).getFecha(),dto.getDetalleCuidados().get(dto.getDetalleCuidados().size()-1).getHora(),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual()).isTrue())
						{
							dto.setActivo(false);
							logger.info("valor >> "+dto.isActivo());
						}
					}						
				}
								
				array.add(dto);
			}
			
			//la unica programacion activa es la ultima realizada. esto sin evaluar las fechas/horas, esta
			//operacion se realiza en el detalle
			for(int i = 1; i < array.size(); i++)							
				array.get(i).setActivo(false);			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	//**********************************************************************************************************************
	
	/**
	 * Inserta el encabezado de una programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int insertarProgCuidadosEnfer(Connection con, HashMap parametros)
	{
		int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_prog_cuidados_enfer");
		
		logger.info("\n\nConsulta Insercion Cuidado Enfermeria >>> "+strInsertarCuidadosEnfer);
		logger.info("Datos >>>  codFrecuencia "+parametros.get("codigoPkFrecCuidadoEnfer")+" fechaInicio "+parametros.get("fechaInicio")+"   horaInicio "+parametros.get("observacion")+"");
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarCuidadosEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPk);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPkFrecCuidadoEnfer").toString()));
			ps.setDate(3,Date.valueOf(parametros.get("fechaInicio").toString()));
			ps.setString(4,parametros.get("horaInicio").toString());
			
			if(!parametros.get("observacion").toString().trim().equals(""))
				ps.setString(5,parametros.get("observacion").toString());
			else
				ps.setNull(5,Types.VARCHAR);
			
			ps.setString(6,parametros.get("usuarioProgramacion").toString());					
			
			if(ps.executeUpdate()>0)
				return codigoPk;
		}
		catch (Exception e) {
			logger.info("");
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	//**********************************************************************************************************************
	
	/**
	 * Inserta el detalle de programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int insertarDetalleProgCuidadosEnfer(Connection con, HashMap parametros)
	{
		int codigoPk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_prog_cuidados_enfer");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetProgCuidEnfer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPk);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPkProCuidadosEnfer").toString()));
			ps.setDate(3,Date.valueOf(parametros.get("fecha").toString()));
			ps.setString(4,parametros.get("hora").toString());
			
			if(parametros.get("activo").toString().trim().equals(""))
				ps.setString(5,ConstantesBD.acronimoNo);
			else
				ps.setString(5,parametros.get("activo").toString());						
			
			if(ps.executeUpdate()>0)
				return codigoPk;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************
	//**********************************************************************************************************************	
}