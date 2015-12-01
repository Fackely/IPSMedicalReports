package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseContratoCargueDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseContratoCargueDao.class);

	/**
	 * Método que inserta un nuevo contrato_cargue
	 * @param con
	 * @param codigoContrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param totalPacientes
	 * @param valorTotal
	 * @param upc
	 * @param cuentaCobro= si es "" inserta null
	 * @throws SQLException
	 */
	public static int insertarContratoCargue(
			Connection con, int codigoContrato, String fechaCargue, String fechaInicial, String fechaFinal, int totalPacientes, double valorTotal, double upc, String cuentaCobro, int codigoInstitucion,String fechaFinalModificada) throws SQLException
	{
		/**
		 * Sentencia para insertar el contrato cargue
		 */
		String insertarContratoCargueStr=
			"insert into capitacion.contrato_cargue " +
			"(" +
				"codigo, " +  		//1
				"contrato, " +  	//2
				"fecha_cargue, " +	//3
				"fecha_inicial, " +	//4
				"fecha_final, " +	//5
				"total_pacientes, "+//6
				"valor_total, " +	//7
				"upc, " +			//8
				"ajustes_debito, " +// valor 0 
				"ajustes_credito, " +// valor 0
				"anulado, " +		//9
				"cuenta_cobro," +//10
				"institucion,fecha_final_modificada " +	//11,12	
			") " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 0, ?, ?, ?,?)";
		PreparedStatementDecorator ps = null;
		try
		{
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_contrato_cargue");
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarContratoCargueStr));
	        ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
	        ps.setInt(2, codigoContrato);
	        ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCargue)));
	        ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
	        ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
	        ps.setDouble(6, Utilidades.convertirADouble(totalPacientes+""));
	        ps.setDouble(7, valorTotal);
	        ps.setDouble(8, upc);
	        ps.setString(9, ValoresPorDefecto.getValorFalseParaConsultas());
	        if(cuentaCobro.equals(""))
	        	ps.setObject(10, null);
	        else
	        	ps.setDouble(10, Utilidades.convertirADouble(cuentaCobro));
	        ps.setInt(11, codigoInstitucion);
	        if(fechaFinalModificada.equals(""))
	        	ps.setObject(12, null);
	        else
	        	ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinalModificada)));

	        ps.executeUpdate();

	        return codigo;
		} 
		catch(SQLException se)
		{
			logger.error("Error ingresando Contrato Cargue: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método para modificar los datos modificables de un contrato_cargue existente
	 * @param con
	 * @param codigoContratoCargue
	 * @param totalPacientes
	 * @param upc
	 * @throws SQLException
	 */
	public static void modificarContratoCargue(Connection con, int codigoContratoCargue, int totalPacientes, double upc, double valorTotal,String fechaFinalModificada) throws SQLException
	{
		/**
		 * Sentencia para modificar el contrato cargue
		 */
		String modificarContratoCargueStr=
			"update capitacion.contrato_cargue " +
			"set total_pacientes=?, upc=?, valor_total=?,fecha_final_modificada=? where codigo = ?";
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(modificarContratoCargueStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(totalPacientes+""));
	        sentencia.setDouble(2, upc);
	        sentencia.setDouble(3, valorTotal);
	        if(fechaFinalModificada.equals(""))
	        	sentencia.setObject(4, null);
	        else
	        	sentencia.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinalModificada)));
	        sentencia.setDouble(5, Utilidades.convertirADouble(codigoContratoCargue+""));
	        sentencia.executeUpdate();
	   
		}
		catch(SQLException se)
		{
			logger.error("Error modificando Contrato Cargue: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * Método para anular un contrato_cargue
	 * @param con
	 * @param codigoContratoCargue
	 * @throws SQLException
	 */
	public static void anularContratoCargue(Connection con, int codigoContratoCargue) throws SQLException
	{
		/**
		 * Sentencia para anular el contrato cargue
		 */
		String anularContratoCargueStr=
			"update capitacion.contrato_cargue " +
			"set anulado=? where codigo=?";
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(anularContratoCargueStr));
	        sentencia.setBoolean(1, true);
	        sentencia.setDouble(2, Utilidades.convertirADouble(codigoContratoCargue+""));
	        sentencia.executeUpdate();
	      
		}
		catch(SQLException se)
		{
			logger.error("Error anulando Contrato Cargue: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que informa si un contrato cargue se encuentra anulado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public static boolean estaAnuladoContratoCargue(Connection con, int codigoContratoCargue) throws SQLException
	{
		/**
		 * Sentencia que consulta si un contrato cargue esta anulado
		 */
		String consultarEstaAnuladoContratoCargueStr=
			"select anulado as anulado from contrato_cargue where codigo = ?";

		boolean resultadoB=false;
		PreparedStatementDecorator sentencia = null;
		ResultSetDecorator resultado = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarEstaAnuladoContratoCargueStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(codigoContratoCargue+""));
	        resultado = new ResultSetDecorator(sentencia.executeQuery());
	        if(resultado.next())
	        {
	        	resultadoB=resultado.getBoolean("anulado");
	        }
	        else
	        {
	          	throw new SQLException("El contrato cargue no existe:"+codigoContratoCargue);
	        }
	       
	        return resultadoB;
		}
		catch(SQLException se)
		{
			logger.error("Error buscando Contratos Cargue: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			try {
				if (resultado!=null){
					resultado.close();
				}
				
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Verifica si existe un contrato cargue para el periodo especificado que no se encuentre anulado
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoContrato
	 * @return
	 * @throws SQLException
	 */
	public static boolean existenContratosCargueConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException
	{
		/**
		 * Sentencia que consulta si un contrato cargue esta anulado
		 */
		String consultarExisteContratoCarguePeriodoStr=
			"select cc.codigo from " +
			"contrato_cargue cc, " +
			"contratos c " +
			"where cc.contrato = c.codigo and to_char(cc.fecha_inicial,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and to_char(cc.fecha_final,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' and c.convenio = "+codigoConvenio+" and anulado<>"+ValoresPorDefecto.getValorTrueParaConsultas();

	
		boolean resultadoB=false;
		PreparedStatementDecorator sentencia = null;
		
		ResultSetDecorator  resultado = null;
		try
		{
			logger.info("--->"+consultarExisteContratoCarguePeriodoStr);
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarExisteContratoCarguePeriodoStr));
	        resultado = new ResultSetDecorator(sentencia.executeQuery());
	        if(resultado.next())
	        {
	        	resultadoB=true;
	        }
	        return resultadoB;
		}
		catch(SQLException se)
		{
			logger.error("Error buscando existencia contratos cargue: ",se);
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			try {
				if (resultado!=null){
					resultado.close();
				}
				
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Verifica si existen cuentas de cobro para los cargues del convenio y el periodo especificado
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoContrato
	 * @return
	 * @throws SQLException
	 */
	public static boolean existenCuentasCobroConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException
	{
		/**
		 * Sentencia que consulta si hay cuentas de cobro
		 */
		String consultarExistenCuentasCobraConvenioPeriodoStr=
			"select cc.codigo from " +
			"contrato_cargue cc, " +
			"contratos c " +
			"where cc.contrato = c.codigo and to_char(cc.fecha_inicial,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and to_char(cc.fecha_final,'yyyy-mm-dd') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' and c.convenio = "+codigoConvenio;
		PreparedStatementDecorator sentencia = null;
		ResultSetDecorator  resultado = null;
		boolean resultadoB=false;
		try
		{
			logger.info("existenCuentasCobroConvenioPeriodo--->"+consultarExistenCuentasCobraConvenioPeriodoStr);
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarExistenCuentasCobraConvenioPeriodoStr));
	        resultado = new ResultSetDecorator(sentencia.executeQuery());
	        if(resultado.next())
	        {
	        	resultadoB=true;
	        }
	   
	        return resultadoB;
		}
		catch(SQLException se)
		{
			logger.error("Error buscando existencia contratos cargue con cuenta cobro: ",se);
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			try {
				if (resultado!=null){
					resultado.close();
				}
				
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Método para buscar contratos_cargue
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws SQLException
	 */
	public static HashMap buscarContratosCargue(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException
	{
		/**
		 * Sentencia para buscar el contrato cargue
		 */
		String buscarContratosCargueStr=
			"select " +
			"cc.codigo as codigo, " +
			"cc.contrato as codigoContrato, " +
			"getnumerocontrato(cc.contrato) as numeroContrato, " +
			"to_char(cc.fecha_cargue,'DD/MM/YYYY') as fechaCargue, " +
			"to_char(cc.fecha_inicial,'DD/MM/YYYY') as fechaInicial, " +
			"to_char(cc.fecha_final,'DD/MM/YYYY') as fechaFinal, " +
			"coalesce(to_char(cc.fecha_final_modificada,'DD/MM/YYYY'),to_char(current_date,'DD/MM/YYYY')) as fechaFinalModificada, " +
			"cc.total_pacientes as totalPacientes, " +
			"cc.valor_total as valorTotal, " +
			"tipo_pago as tipoPago, " +
			"cc.upc as upc, " +
			"anulado as anulado, " +
			"anulado as anuladoEnBD," +
			"'"+ValoresPorDefecto.getValorTrueParaConsultas()+"' as enbd, " +
			"cc.cuenta_cobro as cuentaCobro " +
			"from contrato_cargue cc, contratos where " +
			"cc.contrato = contratos.codigo and " +
			"contratos.convenio = ? and " +
			"cc.fecha_inicial >= ? and " +
			"cc.fecha_final <= ?";
		
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(buscarContratosCargueStr));
	        sentencia.setInt(1, codigoConvenio);
	        sentencia.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
	        sentencia.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()),true,false);
			sentencia.close();
			return mapaRetorno;
		}
		catch(SQLException se)
		{
			logger.error("Error buscando Contratos Cargue: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * Busca los logs de subir paciente según los parametros especificados
	 * @param con
	 * @param convenio que correspondan al convenio especificado
	 * @param fechaInicial cuya fecha de cargue sea mayor o igual a esta fecha inicial
	 * @param fechaFinal cuya fecha de cargue sea menor o igual a esta fecha final
	 * @param usuario que corresponda al usuario especificado
	 * @return
	 * @throws SQLException
	 */
	public static Collection buscarLogsSubirPacientes(Connection con, String convenio, String fechaInicial, String fechaFinal, String usuario) throws SQLException
	{
		int contadorParametro=1;
		
		String buscarLogsSubirPacienteStr=
			"select " +
			"contratos.convenio as codigoConvenio, " +
			"convenios.nombre as nombreConvenio, " +
			"contratos.codigo as codigoContrato, " +
			"contratos.numero_contrato as numeroContrato, " +
			"to_char(contratos.fecha_inicial,'DD/MM/YYYY') as vigenciaFechaInicial, " +
			"to_char(contratos.fecha_final,'DD/MM/YYYY') as vigenciaFechaFinal, " +
			"to_char(fecha_cargue,'DD/MM/YYYY') as fechaCargue, " +
			"total_leidos as totalLeidos, " +
			"total_grabados as totalGrabados, " +
			"usuario as usuario, " +
			"archivo_inconsistencias as archivoInconsistencias " +
			"from log_subir_pacientes, contratos, convenios where " +
			"log_subir_pacientes.contrato = contratos.codigo and " +
			"contratos.convenio = convenios.codigo and 1=1 ";
		
		
		if(UtilidadCadena.noEsVacio(convenio))
			buscarLogsSubirPacienteStr+="and convenios.codigo=? ";
		if(UtilidadCadena.noEsVacio(fechaInicial))
			buscarLogsSubirPacienteStr+="and fecha_cargue>=? ";
		if(UtilidadCadena.noEsVacio(fechaFinal))
			buscarLogsSubirPacienteStr+="and fecha_cargue<=? ";
		if(UtilidadCadena.noEsVacio(usuario))
			buscarLogsSubirPacienteStr+="and usuario=? ";

		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(buscarLogsSubirPacienteStr));

			if(UtilidadCadena.noEsVacio(convenio))
		        sentencia.setInt(contadorParametro++, Integer.parseInt(convenio));
			if(UtilidadCadena.noEsVacio(fechaInicial))
		        sentencia.setDate(contadorParametro++, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			if(UtilidadCadena.noEsVacio(fechaFinal))
		        sentencia.setDate(contadorParametro++, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			if(UtilidadCadena.noEsVacio(usuario))
		        sentencia.setString(contadorParametro++, usuario);
			
			Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(sentencia.executeQuery()));
			sentencia.close();
			return col;
		}
		catch(SQLException se)
		{
			logger.error("Error buscando Logs Subir Paciente: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que actualiza el upc y el total a pagar=upc*total_usuarios de los cargues_grupo_etareo correspondientes al cargue indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @param upc
	 * @throws SQLException
	 */
	public static void actualizarTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue, double upc) throws SQLException
	{
		/**
		 * Sentencia para actualizar los cargue_grupo_etareo 
		 */
		String actualizarCargueGrupoEtareoStr=
			"update capitacion.cargue_grupo_etareo set upc=?, total_a_pagar=total_usuarios*? where contrato_cargue=?";
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(actualizarCargueGrupoEtareoStr));
	        sentencia.setDouble(1, upc);
	        sentencia.setDouble(2, upc);
	        sentencia.setDouble(3, Utilidades.convertirADouble(codigoContratoCargue+""));
	        sentencia.executeUpdate();
	   
		}
		catch(SQLException se)
		{
			logger.error("Error actualizando Cargue Grupo Etareo: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Calcula sumatoria de total_a_pagar de los cargues grupo etareo del contrato indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public static double calcularSumatoriaTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue) throws SQLException
	{
		/**
		 * Sentencia para obtener la sumatoria de los total_a_pagar de los cargue_grupo_etareo 
		 */
		String sumatoriaStr=
			"select sum(total_a_pagar) as suma from cargue_grupo_etareo where contrato_cargue=?";
		PreparedStatementDecorator sentencia = null;
		 ResultSetDecorator resultado = null;
		double res=0;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(sumatoriaStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(codigoContratoCargue+""));
	        resultado = new ResultSetDecorator(sentencia.executeQuery());
	        if(resultado.next())
	        {
	        	res=resultado.getDouble("suma");
	        }
	        else
	        {
				logger.error("Error consultando sumatoria Cargue Grupo Etareo");
				resultado.close();
				sentencia.close();
				throw new SQLException("Error consultando sumatoria Cargue Grupo Etareo");
	        }
	       
			sentencia.close();
			return res;
		}
		catch(SQLException se)
		{
			logger.error("Error consultando sumatoria Cargue Grupo Etareo: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			try{
				if(resultado!=null){
					resultado.close();				
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
			 
			
		}
	}
	
	/**
	 * Consulta los cargue_grupo_etareo correspondientes al contrato_cargue indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCarguesGrupoEtareoContrato(Connection con, int codigoContratoCargue) throws SQLException
	{
		/**
		 * Sentencia para la consulta de los cargue_grupo_etareo correspondientes al contrato_cargue indicado
		 */
		String consultaStr=
			"select " +
			"consecutivo as consecutivo, " +
			"grupo_etareo as grupoEtareo, " +
			"total_usuarios as totalUsuarios, " +
			"upc as upc " +
			"from cargue_grupo_etareo where contrato_cargue=?";
		PreparedStatementDecorator sentencia = null;
		try
		{
			sentencia =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			sentencia.setDouble(1, Utilidades.convertirADouble(codigoContratoCargue+""));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()),true,false);

			return mapaRetorno;
		}
		catch(SQLException se)
		{
			logger.error("Error consultando Cargue Grupo Etareo: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Permite consultar los usuarios cargados en un periodo específico para un contrato dado y con determinados parametros de
	 * busqueda como el tipo de identificacion, numero de identificacion, nombres y apellidos
	 * @param con
	 * @param codigoContrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoId
	 * @param numeroId
	 * @param nombre
	 * @param apellido
	 * @param numeroFicha 
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarUsuariosCargados(Connection con, int codigoContrato, String fechaInicial, String fechaFinal, String tipoId, String numeroId, String nombre, String apellido, String numeroFicha) throws SQLException
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		/**
		 * Sentencia para la consulta de los usuario_x_convenio cargados
		 */
		String consultaPersonasStr=
			"select " +
			"uxc.consecutivo as consecutivo, " +
			"uxc.contrato as contrato, " +
			"to_char(uxc.fecha_inicial,'DD/MM/YYYY') as fechaInicial, " +
			"to_char(uxc.fecha_final,'DD/MM/YYYY') as fechaFinal, " +
			"uxc.tipo_cargue as tipoCargue, " +
			"p.tipo_identificacion as tipoIdentificacion, " +
			"p.numero_identificacion as numeroIdentificacion, " +
			"p.primer_nombre as primerNombre, " +
			"p.segundo_nombre as segundoNombre, " +
			"p.primer_apellido as primerApellido, " +
			"p.segundo_apellido as segundoApellido, " +
			"p.sexo as codSexo, " +
			"to_char(p.fecha_nacimiento,'DD/MM/YYYY') as fechaNacimiento, " +
			"sexo.nombre as nomSexo, " +
			"uxc.activo as activo, " +
			"'"+ConstantesBD.acronimoNo+"' as capitado " +
			"from usuario_x_convenio uxc, personas p, sexo where " +
			"uxc.persona = p.codigo and " +
			"p.sexo = sexo.codigo and " +
			"uxc.contrato = ? and " +
			"to_char(uxc.fecha_inicial, 'YYYY-MM-DD') = ? and " +
			"to_char(uxc.fecha_final, 'YYYY-MM-DD') = ?";
		
		String consultaUsuariosCapitadosStr=
			"select " +
			"cuc.consecutivo as consecutivo, " +
			"cuc.contrato as contrato, " +
			"to_char(cuc.fecha_inicial,'DD/MM/YYYY') as fechaInicial, " +
			"to_char(cuc.fecha_final,'DD/MM/YYYY') as fechaFinal, " +
			"cuc.tipo_cargue as tipoCargue, " +
			"uc.tipo_identificacion as tipoIdentificacion, " +
			"uc.numero_identificacion as numeroIdentificacion, " +
			"uc.primer_nombre as primerNombre, " +
			"uc.segundo_nombre as segundoNombre, " +
			"uc.primer_apellido as primerApellido, " +
			"uc.segundo_apellido as segundoApellido, " +
			"uc.sexo as codSexo, " +
			"to_char(uc.fecha_nacimiento,'DD/MM/YYYY') as fechaNacimiento, " +
			"sexo.nombre as nomSexo, " +
			"cuc.activo as activo, " +
			"'"+ConstantesBD.acronimoSi+"' as capitado " +
			"from usuarios_capitados uc, conv_usuarios_capitados cuc, sexo where " +
			"cuc.usuario_capitado = uc.codigo and " +
			"uc.sexo = sexo.codigo and " +
			"cuc.contrato = ? and " +
			"to_char(cuc.fecha_inicial, 'YYYY-MM-DD') = ? and " +
			"to_char(cuc.fecha_final, 'YYYY-MM-DD') = ?";

		if(UtilidadCadena.noEsVacio(tipoId))
		{
			consultaPersonasStr+=" and p.tipo_identificacion = '"+tipoId+"'";
			consultaUsuariosCapitadosStr+=" and uc.tipo_identificacion = '"+tipoId+"'";
		}
		if(UtilidadCadena.noEsVacio(numeroId))
		{
			consultaPersonasStr+=" and p.numero_identificacion like '"+numeroId+"%'";
			consultaUsuariosCapitadosStr+=" and uc.numero_identificacion like '"+numeroId+"%'";
		}
		if(UtilidadCadena.noEsVacio(nombre))
		{
			consultaPersonasStr+=" and (upper(p.primer_nombre) like upper('"+nombre+"%') or upper(p.segundo_nombre) like upper('"+nombre+"%'))";
			consultaUsuariosCapitadosStr+=" and (upper(uc.primer_nombre) like upper('"+nombre+"%') or upper(uc.segundo_nombre) like upper('"+nombre+"%'))";
		}
		if(UtilidadCadena.noEsVacio(apellido))
		{
			consultaPersonasStr+=" and (upper(p.primer_apellido) like upper('"+apellido+"%') or upper(p.segundo_apellido) like upper('"+apellido+"%'))";
			consultaUsuariosCapitadosStr+=" and (upper(uc.primer_apellido) like upper('"+apellido+"%') or upper(uc.segundo_apellido) like upper('"+apellido+"%'))";
		}
		if(UtilidadCadena.noEsVacio(numeroFicha))
		{
			consultaUsuariosCapitadosStr+=" and numero_ficha ='"+numeroFicha+"'";
		}
		PreparedStatementDecorator sentencia = null;
		try
		{
			sentencia =  new PreparedStatementDecorator(con.prepareStatement(consultaPersonasStr+" union "+consultaUsuariosCapitadosStr));
			
			logger.info(">>>>>>>>>>>"+consultaPersonasStr);
			logger.info(">>>>>>>>>>>"+consultaUsuariosCapitadosStr);
			logger.info(">>>>>>>>>>>"+codigoContrato);
			logger.info(">>>>>>>>>>>"+fechaInicial);
			logger.info(">>>>>>>>>>>"+fechaFinal);
			
			sentencia.setInt(1, codigoContrato);
	        sentencia.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
	        sentencia.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
	        
	        sentencia.setInt(4, codigoContrato);
	        sentencia.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
	        sentencia.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));

	        mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()));
	     }
		catch(SQLException se)
		{
			logger.error("Error consultando Usuarios Cargados: "+se.getMessage());
			throw se;
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	/**
	 * Inserta un nuevo cargue_grupo_etareo 
	 * @param con
	 * @param codigoContratoCargue
	 * @param codigoGrupoEtareoConvenio
	 * @param totalUsuarios
	 * @param upc
	 * @param totalPagar
	 * @throws SQLException
	 */
	public static void insertarCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareoConvenio, 
			int totalUsuarios, double upc, double totalPagar) throws SQLException
	{
		/**
		 * Sentencia para insertar un cargue_grupo_etareo
		 */
		String insertarStr=
			"insert into cargue_grupo_etareo (" +
			"consecutivo, " +
			"contrato_cargue, " +
			"grupo_etareo, " +
			"total_usuarios, " +
			"upc, " +
			"total_a_pagar" +
			") values (?, ?, ?, ?, ?, ?)";
		PreparedStatementDecorator sentencia = null;
		try
		{
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "capitacion.seq_cargue_grupo_etareo");
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(insertarStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
	        sentencia.setDouble(2, Utilidades.convertirADouble(codigoContratoCargue+""));
	        sentencia.setDouble(3, Utilidades.convertirADouble(codigoGrupoEtareoConvenio+""));
	        sentencia.setDouble(4, Utilidades.convertirADouble(totalUsuarios+""));
	        sentencia.setDouble(5, upc);
	        sentencia.setDouble(6, totalPagar);
	        sentencia.executeUpdate();
	        
		}
		catch(SQLException se)
		{
			logger.error("Error insertando Cargue Grupo Etareo:"+se.getMessage());
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Actualiza los valores del cargue_grupo_etareo indicado
	 * @param con
	 * @param codigoCargueGrupoEtareo
	 * @param totalUsuarios
	 * @param upc
	 * @param totalPagar
	 * @throws SQLException
	 */
	public static void actualizarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo, int totalUsuarios, double upc, double totalPagar) throws SQLException
	{
		/**
		 * Sentencia para actualizar un cargue_grupo_etareo
		 */
		String actualizarStr=
			"update cargue_grupo_etareo set " +
			"total_usuarios=?, " +
			"upc=?, " +
			"total_a_pagar=? where consecutivo = ?";
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(actualizarStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(totalUsuarios+""));
	        sentencia.setDouble(2, upc);
	        sentencia.setDouble(3, totalPagar);
	        sentencia.setDouble(4, Utilidades.convertirADouble(codigoCargueGrupoEtareo+""));
	        sentencia.executeUpdate();
	     
		}
		catch(SQLException se)
		{
			logger.error("Error insertando Cargue Grupo Etareo:"+se.getMessage());
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Elimina un cargue_grupo_etareo especificado
	 * @param con
	 * @param codigoCargueGrupoEtareo
	 * @throws SQLException
	 */
	public static void eliminarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo) throws SQLException
	{
		/**
		 * Sentencia para eliminar un cargue_grupo_etareo
		 */
		String eliminarStr=
			"delete from cargue_grupo_etareo " +
			"where consecutivo = ?";
		PreparedStatementDecorator sentencia = null;
		try
		{
	        sentencia= new PreparedStatementDecorator(con.prepareStatement(eliminarStr));
	        sentencia.setDouble(1, Utilidades.convertirADouble(codigoCargueGrupoEtareo+""));
	        sentencia.executeUpdate();
	   
		}
		catch(SQLException se)
		{
			logger.error("Error eliminar Cargue Grupo Etareo:"+se.getMessage());
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param contratosCargueEliminados
	 * @return
	 */
	public static boolean eliminarContratos(Connection con, HashMap contratosCargueEliminados) 
	{
		String cadenaEliminarCCGE="DELETE FROM cargue_grupo_etareo where contrato_cargue = ?";
		String cadenaEliminarCC="DELETE FROM contrato_cargue WHERE codigo =?";
		PreparedStatementDecorator sentencia = null; 
	     PreparedStatementDecorator psInsert=  null;
	     PreparedStatementDecorator ps= null;
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(contratosCargueEliminados.get("numRegistros")+"");i++)
			{
				String codigoConCar=contratosCargueEliminados.get("codigo_"+i)+"";
				
		        sentencia= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarCCGE));
		        sentencia.setDouble(1, Utilidades.convertirADouble(codigoConCar));
		        sentencia.executeUpdate();
		        sentencia.close();
		        
		        ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarCC));
		        ps.setDouble(1, Utilidades.convertirADouble(codigoConCar));
		        ps.executeUpdate();
	
		        String cadenaInsertarLog="INSERT INTO log_elim_contrato_cargue (" +
		        											" codigo," +
		        											" contrato," +
		        											" convenio," +
		        											" fecha_inicial," +
		        											" fecha_final," +
		        											" total_pacientes," +
		        											" upc," +
		        											" valor_total," +
		        											" fecha_eliminacion," +
		        											" hora_eliminacion," +
		        											" usuario_eliminacion) " +
		        										" values(?,?,?,?,?,?,?,?,current_date,?,?)";
		        psInsert= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarLog));
		        psInsert.setDouble(1, Utilidades.convertirADouble(contratosCargueEliminados.get("codigo_"+i)+""));
		        psInsert.setInt(2, Utilidades.convertirAEntero(contratosCargueEliminados.get("codigocontrato_"+i)+""));
		        psInsert.setInt(3, Utilidades.convertirAEntero(contratosCargueEliminados.get("convenio")+""));
		        psInsert.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(contratosCargueEliminados.get("fechainicial_"+i)+"")));
		        psInsert.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(contratosCargueEliminados.get("fechafinal_"+i)+"")));
		        psInsert.setDouble(6, Utilidades.convertirADouble(contratosCargueEliminados.get("totalpacientes_"+i)+""));
		        psInsert.setDouble(7, Utilidades.convertirADouble(contratosCargueEliminados.get("upc_"+i)+""));
		        psInsert.setDouble(8, Utilidades.convertirADouble(contratosCargueEliminados.get("valortotal_"+i)+""));
		        psInsert.setObject(9, UtilidadFecha.getHoraActual()+"");
		        psInsert.setObject(10, contratosCargueEliminados.get("usuario")+"");
		        psInsert.executeUpdate();
		        
		        Utilidades.imprimirMapa(contratosCargueEliminados);
		        psInsert.close();
		        return true;
			}
		}
		catch(SQLException se)
		{
			logger.error("Error eliminar Cargue Grupo Etareo:"+se.getMessage());
		}finally{
			try{
				if(sentencia!=null){
					sentencia.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
			try{
				if(psInsert!=null){
					psInsert.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo 
	 * @return
	 */
	public static boolean inactivarUsuarios(Connection con, String consecutivo, String activo) 
	{
		String cadenaActualizacionEstadoUsuarioStr=" UPDATE conv_usuarios_capitados SET activo=? where consecutivo=? ";
		
		logger.info(">>>>>>>>>>"+cadenaActualizacionEstadoUsuarioStr);
		PreparedStatementDecorator ps = null;
		boolean res=false;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionEstadoUsuarioStr));
			
			logger.info("activo >>>>>"+activo);
			logger.info("consecutivo >>>>"+consecutivo);
			
			ps.setString(1, activo);
			ps.setDouble(2, Utilidades.convertirADouble(consecutivo));
			
			res=ps.executeUpdate()>0;
	
			return res;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @return
	 */
	public static boolean inactivarUsuariosActivos(Connection con, String consecutivo, String activo) 
	{
		String cadenaActualizacionEstadoUsuarioStr=" UPDATE usuario_x_convenio SET activo=? where consecutivo=? ";
		
		logger.info(">>>>>>>>>>"+cadenaActualizacionEstadoUsuarioStr);
		PreparedStatementDecorator ps = null;
		boolean res=false;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionEstadoUsuarioStr));
			
			logger.info("activo >>>>>"+activo);
			logger.info("consecutivo >>>>"+consecutivo);
			
			ps.setString(1, activo);
			ps.setDouble(2, Utilidades.convertirADouble(consecutivo));
			
			res=ps.executeUpdate()>0;
		
			return res;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarUsuarios(Connection con, String consecutivo) 
	{
		String cadenaEliminacionUsuarioStr=" DELETE FROM conv_usuarios_capitados WHERE consecutivo =? ";
		
		logger.info(">>>>>>>>>>"+cadenaEliminacionUsuarioStr);
		PreparedStatementDecorator ps = null;
		boolean res=false;
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionUsuarioStr));
			
			logger.info("consecutivo >>>>"+consecutivo);
			
			ps.setDouble(1, Utilidades.convertirADouble(consecutivo));
			
			res=ps.executeUpdate()>0;

			return res;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap consultarDatosEliminado(Connection con, String consecutivo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadenaConsultaDatosElimnado = "SELECT cu.convenio as convenio, cu.contrato as contrato, cu.fecha_inicial as fechainicial, cu.fecha_final as fechafinal, u.numero_identificacion as numeroidentificacion, u.tipo_identificacion as tipoidentificacion, u.primer_nombre as primernombre, u.segundo_nombre as segundonombre, u.primer_apellido as primerapellido, u.segundo_apellido as segundoapellido FROM conv_usuarios_capitados cu INNER JOIN usuarios_capitados u ON(u.codigo=cu.usuario_capitado) WHERE cu.consecutivo=? ";
		PreparedStatementDecorator ps = null; 
		try
		{
			
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDatosElimnado));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDatosElimnado);
				logger.info("consecutivo >>>>>>>>"+consecutivo);
				
				ps.setDouble(1, Utilidades.convertirADouble(consecutivo));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE USUARIOS AUTORIZADOS PARA AUTORIZAR "+e);
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarLogEliminacion(Connection con, HashMap vo) 
	{
		
		String cadenaInsercionLogEliminados = "INSERT INTO log_usuarios_capitados(codigo,convenio,contrato,fecha_inicial_cargue,fecha_final_cargue,tipo_identificacion,numero_identificacion,primer_nombre,segundo_nombre,primer_apellido,segundo_apellido,fecha_proceso,hora_proceso,usuario_proceso) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatementDecorator ps = null;
		boolean res=false;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionLogEliminados));
			
			ps.setDouble(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_usuarios_capitados"));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("convenio")+""));
			
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("contrato")+""));
			
			ps.setDate(4, Date.valueOf(vo.get("fechainicial")+""));
			
			ps.setDate(5, Date.valueOf(vo.get("fechafinal")+""));
			
			ps.setString(6, vo.get("tipoid")+"");
			
			ps.setString(7, vo.get("numeroid")+"");
			
			ps.setString(8, vo.get("primernombre")+"");
			
			ps.setString(9, vo.get("segundonombre")+"");
			
			ps.setString(10, vo.get("primerapellido")+"");
			
			ps.setString(11, vo.get("segundoapellido")+"");
			
			ps.setDate(12, Date.valueOf(vo.get("fechaproceso")+""));
			
			ps.setString(13, vo.get("horaproceso")+"");
			
			ps.setString(14, vo.get("usuarioproceso")+"");
			
			
			return res;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap consultarDaotosInactivar(Connection con, String consecutivo) 
	{	
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadenaConsultaDatosElimnado = "select c.convenio as convenio, uc.contrato as contrato, uc.fecha_inicial as fechainicial, uc.fecha_final as fechafinal, p.numero_identificacion as numeroidentificacion, p.tipo_identificacion as tipoidentificacion, p.primer_nombre as primernombre, p.segundo_nombre as segundonombre, p.primer_apellido as primerapellido, p.segundo_apellido as segundoapellido FROM usuario_x_convenio uc INNER JOIN contratos c ON(c.codigo=uc.contrato) INNER JOIN personas p ON(p.codigo=uc.persona) WHERE consecutivo=? ";
		PreparedStatementDecorator ps = null;
		try
		{
			
				ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDatosElimnado));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDatosElimnado);
				logger.info("consecutivo >>>>>>>>"+consecutivo);
				
				ps.setDouble(1, Utilidades.convertirADouble(consecutivo));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
				
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE USUARIOS AUTORIZADOS PARA AUTORIZAR "+e);
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	
}
