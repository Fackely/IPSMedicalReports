package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;


public class SqlBaseEstadisticasServiciosDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseCalidadAtencionDao.class);
	
	/**
	 * Consultar los eventos adversos
	 */
	private static String ingresarStr ="INSERT INTO " +
											"estadisticas_servicios (codigo, usuario_modifica, fecha_modifica, hora_modifica) " +
										"VALUES " +
											"(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizados(Connection con, EstadisticasServicios mundo) {
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (to_char(dc.fecha_modifica, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"') "; 
       
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().toString().equals(ConstantesBD.codigoNuncaValido+""))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
    	
    	// Tipo de servicio
    	if(mundo.getFiltrosMap().containsKey("tipoServicio"))
    		if (!mundo.getFiltrosMap("tipoServicio").equals(""))
    			where += "AND serv.tipo_servicio='"+mundo.getFiltrosMap("tipoServicio").toString().split(ConstantesBD.separadorSplit)[0]+"'";
    	
    	// Convenio
    	if(mundo.getFiltrosMap().containsKey("convenio"))
    		if (!mundo.getFiltrosMap("convenio").toString().equals("") && !mundo.getFiltrosMap("convenio").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND dc.convenio IN ("+mundo.getFiltrosMap("convenio")+") ";
    	
    	// Especialidad
    	if(mundo.getFiltrosMap().containsKey("especialidad"))
    		if (!mundo.getFiltrosMap("especialidad").toString().equals("") && !mundo.getFiltrosMap("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND serv.especialidad IN ("+mundo.getFiltrosMap("especialidad").toString().split(ConstantesBD.separadorSplit)[0]+") ";
    	
    	// Grupo de Servicio
    	if(mundo.getFiltrosMap().containsKey("grupo"))
    		if (!mundo.getFiltrosMap("grupo").equals("") && !mundo.getFiltrosMap("grupo").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND serv.grupo_servicio IN ("+mundo.getFiltrosMap("grupo").toString().split(ConstantesBD.separadorSplit)[0]+") ";
    	
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizadosXConvenio(Connection con, EstadisticasServicios mundo) {
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (dc.fecha_modifica BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"', 'YYYY-MM-DD') and to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"', 'YYYY-MM-DD')) "; 
       
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().equals(ConstantesBD.codigoNuncaValido+""))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
    	
    	// Tipo de servicio
    	if(mundo.getFiltrosMap().containsKey("tipoServicio"))
    		if (!mundo.getFiltrosMap("tipoServicio").equals(""))
    			where += "AND serv.tipo_servicio='"+mundo.getFiltrosMap("tipoServicio").toString().split(ConstantesBD.separadorSplit)[0]+"' ";
    	
    	// Sexo
    	if(mundo.getFiltrosMap().containsKey("sexo"))
    		if (!mundo.getFiltrosMap("sexo").equals(""))
    			where += "AND p.sexo='"+mundo.getFiltrosMap("sexo").toString().split(ConstantesBD.separadorSplit)[0]+"' ";
    	
    	// Convenio
    	if(mundo.getFiltrosMap().containsKey("convenio"))
    		if (!mundo.getFiltrosMap("convenio").toString().equals("") && !mundo.getFiltrosMap("convenio").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND dc.convenio IN ("+mundo.getFiltrosMap("convenio")+") ";
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereServiciosRealizadosXEspecialidad(Connection con, EstadisticasServicios mundo) {
		// Centro de Atención
        String where = "cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND (dc.fecha_modifica BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"','YYYY-MM-DD') and to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"','YYYY-MM-DD')) "; 
       
    	//Se verifica que hayan ocurrido egresos
    	/*where += "AND (eg.fecha_egreso is null or eg.fecha_egreso BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"','YYYY-MM-DD') AND to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"','YYYY-MM-DD'))" +
    			"  AND (eg.fecha_egreso is null or to_char(eg.fecha_egreso,'MM-YY')=TO_CHAR(dc.fecha_modifica,'MM')||'-'||TO_CHAR(dc.fecha_modifica,'YY'))";*/
    	
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().equals(ConstantesBD.codigoNuncaValido+""))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
    	
    	// Tipo de servicio
    	if(mundo.getFiltrosMap().containsKey("tipoServicio"))
    		if (!mundo.getFiltrosMap("tipoServicio").equals(""))
    			where += "AND serv.tipo_servicio='"+mundo.getFiltrosMap("tipoServicio").toString().split(ConstantesBD.separadorSplit)[0]+"' ";
    	
    	// Especialidad
    	if(mundo.getFiltrosMap().containsKey("especialidad"))
    		if (!mundo.getFiltrosMap("especialidad").toString().equals("") && !mundo.getFiltrosMap("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND serv.especialidad IN ("+mundo.getFiltrosMap("especialidad").toString().split(ConstantesBD.separadorSplit)[0]+") ";
    	
    	// Grupo de Servicio
    	if(mundo.getFiltrosMap().containsKey("grupo"))
    		if (!mundo.getFiltrosMap("grupo").equals("") && !mundo.getFiltrosMap("grupo").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			where += "AND serv.grupo_servicio IN ("+mundo.getFiltrosMap("grupo").toString().split(ConstantesBD.separadorSplit)[0]+") ";
    	
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearCampoEgresosPorMes(Connection con, EstadisticasServicios mundo) {
		//***************************** Crear WHERE
		// Rango de fechas
		 String where = "eg.fecha_egreso BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"','YYYY-MM-DD') and to_date('"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"','YYYY-MM-DD') "; 
       
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().equals("-1"))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
		
    	// Centro de Atención
        where += "AND cc.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
    	
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean registrarGeneracionReporte(Connection con, EstadisticasServicios mundo) {
		boolean operacionExitosa=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ingresarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_estadisticas_calidad")+""));
			ps.setString(2, mundo.getUsuario());
			if(ps.executeUpdate()>0)
				operacionExitosa = true;
		}
		catch (SQLException e)
		{
			logger.warn("registrarGeneracionReporte --> "+e);
		}
		return operacionExitosa;
	}
	
}