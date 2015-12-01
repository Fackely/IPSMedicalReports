package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.mundo.manejoPaciente.CalidadAtencion;


public class SqlBaseCalidadAtencionDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseCalidadAtencionDao.class);
	
	/**
	 * Consultar los eventos adversos
	 */
	private static String ingresarStr ="INSERT INTO " +
											" estadisticas_calidad (codigo, usuario_modifica, fecha_modifica, hora_modifica) " +
										"VALUES " +
											"(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereSatisfaccionGeneral(Connection con, CalidadAtencion mundo) {
		// Centro de Atención
        String where = "i.centro_atencion= "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND ( to_char(ec.fecha_modifica, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"') "; 
       
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().equals("-1"))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
    	
    	// Centro de Costo
    	if(mundo.getFiltrosMap().containsKey("centroCosto"))
    		if (!mundo.getFiltrosMap("centroCosto").equals(""))
    			where += "AND ec.area="+mundo.getFiltrosMap("centroCosto");
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereMotCalificacionCalidadAtencion(Connection con, CalidadAtencion mundo) {
		// Centro de Atención
        String where = "i.centro_atencion = "+mundo.getFiltrosMap("centroAtencion")+" ";
      
        // Rango de fechas
    	where += "AND ( to_char(ec.fecha_modifica, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFiltrosMap("fechaFinal")+"")+"') "; 
       
    	// Via de ingreso - tipo paciente
    	if(!mundo.getViasDeIngreso().equals("") && !mundo.getViasDeIngreso().equals("-1"))
    		where += "AND c.via_ingreso IN ("+mundo.getViasDeIngreso()+") AND c.tipo_paciente IN ("+mundo.getTiposPaciente()+") ";
    	
    	// Tipo de motivo
    	if(mundo.getFiltrosMap().containsKey("tipoMotivo"))
    		if (!mundo.getFiltrosMap("tipoMotivo").equals(""))
    			where += "AND ec.calificacion='"+mundo.getFiltrosMap("tipoMotivo")+"'";
    	return where;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean registrarGeneracionReporte(Connection con, CalidadAtencion mundo) {
		boolean operacionExitosa=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ingresarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_estadisticas_calidad"));
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