
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0 01 /Jun/ 2006
 * Clase para las transacciones de la Consulta de Pacientes pendientes de Atencion de Triage
 */
public class SqlBaseConsultaPacientesTriageDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaPacientesTriageDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los pacientes pendentes de triage
	 */
	private static final String consultarPacientesTriageStr = " SELECT " +
															  "pt.codigo as codigo, " +
															  " pt.codigo_paciente as codigopaciente, " +
															  " getnombrepersona(pt.codigo_paciente) as nombrepaciente, " +
															  " ti.acronimo as tipoid, " +
															  " per.numero_identificacion as numeroid, " +
															  " ti.nombre as nombretipoid, " +
															  " tii.es_consecutivo as esconsecutivo," +
															  " to_char(per.fecha_nacimiento,'yyyy-mm-dd') as fechanacimiento, " +
															  " per.sexo as sexo, " +
															  " to_char(pt.fecha,'yyyy-mm-dd') as fechaingreso, " +
															  " pt.hora as horaingreso," +
															  " clatri.descripcion as descclatriage " +
															  " FROM pacientes_triage pt " +
															  " INNER JOIN personas per ON(pt.codigo_paciente=per.codigo) " +
															  " INNER JOIN centro_atencion cc ON(cc.consecutivo=pt.centro_atencion) "+
															  " INNER JOIN tipos_id_institucion tii ON(per.tipo_identificacion=tii.acronimo and tii.institucion=cc.cod_institucion) " +
															  " INNER JOIN tipos_identificacion ti ON(per.tipo_identificacion=ti.acronimo) " +
															  " left outer join historiaclinica.clasificacion_triage clatri on (pt.clasificacion_triage=clatri.codigo)" +
															  " WHERE pt.centro_atencion = ? " +
															  " AND pt.atendido = "+ValoresPorDefecto.getValorFalseParaConsultas()+
															  " ORDER BY  clatri.descripcion,pt.fecha ASC, pt.hora ASC ";
	
	/**
	 * Cadena que consulta los datos de un paciente para triage
	 */
	private static final String consultarDatosPacienteTriageStr = " SELECT "+ 
		"pt.codigo, "+
		"pt.codigo_paciente AS codigo_persona, "+
		"p.primer_apellido, "+
		"p.segundo_apellido, "+
		"p.primer_nombre, "+
		"p.segundo_nombre, "+
		"to_char(p.fecha_nacimiento,'DD/MM/YYYY') AS fecha_nacimiento," +
		"pt.clasificacion_triage as codigoclastriage," +
		"clatri.descripcion as descclatriage "+ 
		"FROM historiaclinica.pacientes_triage pt "+ 
		"INNER JOIN administracion.personas p ON(p.codigo=pt.codigo_paciente) "+ 
		"left outer join historiaclinica.clasificacion_triage clatri on (pt.clasificacion_triage=clatri.codigo)  " +
		"WHERE "+ 
		"pt.codigo_paciente = ? and "+ 
		"pt.centro_atencion = ? and "+
		"pt.atendido = "+ValoresPorDefecto.getValorFalseParaConsultas()+" and "+ 
		"pt.consecutivo_triage IS NULL and pt.consecutivo_fecha_triage IS NULL "; 
	
	/**
	 * Metodo para cargar los pacientes que estan pendientes de atencion triage
	 * para un centro de atencion determinado
	 * @param con
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarPacientesTriage (Connection con, int centroAtencion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarPacientesTriageStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroAtencion);
			return new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarPacientesTriage : [SqlBaseConsultaPacientesTriageDao] "+e.toString() );
			return null;
		}	    
	}
	
	/**
	 * Método implementado para cargar los datos de un paciente regitrado para triage
	 * y que se encuentre pendiente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap consultarDatosPacienteTriage (Connection con,int codigoPaciente,int codigoCentroAtencion,String restriccion)
	{
		try
		{
			String consulta = consultarDatosPacienteTriageStr + restriccion;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			pst.setInt(2,codigoCentroAtencion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosPacienteTriage de SqlBaseConsultaPacientesTriageDao: "+e);
			return null;
		}
		
		
	}
	
	
}