/*
 * @(#)ListadoCitasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004 Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ListadoCitasDao;
import com.princetonsa.dao.sqlbase.SqlBaseListadoCitasDao;

import util.ConstantesBD;
import util.ResultadoCollectionDB;

/**
 * Interfaz para acceder a la fuente de datos los listados definidos de citas 
 *
 * @version 1.0, Marzo 25 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlListadoCitasDao implements ListadoCitasDao
{
	//MT 4128 ponenen en la consulta los campos:
	//" getNombreUsuario(c.usuario_modifica_estado)  AS usu_cita,"+ para traer el nombre del usuario 
	//" a.hora_inicio AS hora_inicio," + hora de inicio de la visita
	//" c.fecha_modifica AS fecha_modifica,"+ hora de modificacion de la cita
	//" c.hora_modifica AS hora_modifica"+  hora de modificacion de la cita
	private static final String consultaCitasPorPaciente=
		" SELECT DISTINCT c.unidad_consulta AS codigoUnidadConsulta, "+
        " getnombrepersona(pm.codigo) AS nombreCompletoMedico, "+
		" pm.codigo AS codigoMedico, "+
		" ec.nombre AS nombreEstadoCita, "+
		" c.estado_cita AS codigoEstadoCita, "+
		" uc.descripcion AS nombreUnidadConsulta, "+
		" to_char(a.fecha, '"+ConstantesBD.formatoFechaBD+"') AS fechaInicio, "+
		" c.codigo_paciente AS codigoPaciente," +
		" a.centro_atencion as codigocentroatencion," +
		" getnomcentroatencion(a.centro_atencion) as nombrecentroatencion, "+
		" a.hora_inicio AS horaInicio, "+
		" el.nombre AS nombreEstadoLiquidacion, " +
		" getcuentasolicitudcita(c.codigo) AS cuenta,"+																						
		" c.codigo AS codigoCita, "+
		" co.descripcion AS nombreConsultorio, "+
		" pp.numero_identificacion AS numeroId, "+
		" pp.tipo_identificacion AS tipoId, "+
		" getnombrepersona(pp.codigo) AS nombreCompletoPersona, "+														
		" coalesce(cont.numero_contrato,'') AS contrato,   			" +
		
		// Modificado x Tarea 127984
		"coalesce(  ( " +
			"SELECT " +
				"conv2.nombre " +
			"FROM " +
				"servicios_cita sc2 " +
			"INNER JOIN " +
				"solicitudes sol2 on (sol2.numero_solicitud = sc2.numero_solicitud) " +
			"INNER JOIN " +
				"cuentas c2 on (c2.id = sol2.cuenta) " +
			"INNER JOIN " +
				"sub_cuentas scu2 on (scu2.ingreso = c2.id_ingreso) " +
			"INNER JOIN " +
				"convenios conv2 ON (conv2.codigo = scu2.convenio) " +
			"WHERE " +
				"sc2.codigo_cita = c.codigo and scu2.nro_prioridad=1 limit 1 " +
			"), " +
		"coalesce(conv.nombre, '')) as convenio, "+
		
		" coalesce(conv.nombre, '') AS convenio,  								" +
		" coalesce(estra.descripcion,'') AS clasificacion_social,  		" +
		" coalesce(tipa.nombre, '') AS tipo_afiliado," +
		" CASE WHEN tieneCitaControlPost(c.codigo) = '"+ConstantesBD.acronimoSi+"' THEN 1 ELSE "+ConstantesBD.codigoNuncaValido+" END as indpo," +
		" coalesce(c.motivo_noatencion,'') AS motivo_noatencion, "+
		" coalesce(c.observaciones_cancelacion||'       '||c.usuario_cancela||'  '||to_char(c.fecha_cancela,'dd/mm/yyyy')||'  '||c.hora_cancela,'') AS observacionescancelacion," +
		" coalesce(getnumhistocli(c.codigo_paciente),'') as numero_historia_clinica," +
		" co.codigo_consultorio AS codigo_consultorio_usua," +
		" coalesce(getregmedico(pm.codigo),'') AS registro_medico," +
		" c.usuario AS login_usu_cita," +
		" getNombreUsuario(c.usuario_modifica_estado)    AS usu_cita,"+
		" coalesce(pp.telefono,'') AS telefono, " +
		" pp.telefono_fijo  AS telefono_fijo, " +
		" pp.telefono_celular  AS telefono_celular, " +
		" a.fecha AS fecha," +
		" a.hora_inicio AS hora_inicio," +
		" c.fecha_modifica AS fecha_modifica,"+
		" c.hora_modifica AS hora_modifica"+
		" FROM cita c "+
		" INNER JOIN agenda a ON(c.codigo_agenda=a.codigo) "+
		" INNER JOIN consultorios co ON(a.consultorio=co.codigo) " +
		" INNER JOIN unidades_consulta uc ON(c.unidad_consulta=uc.codigo) " +
		" INNER JOIN personas pp ON(c.codigo_paciente=pp.codigo) "+
		" INNER JOIN estados_liquidacion el ON(c.estado_liquidacion=el.acronimo) " +										
		" INNER JOIN estados_cita ec ON(c.estado_cita=ec.codigo) " +
		" LEFT OUTER JOIN personas pm ON(a.codigo_medico=pm.codigo) "+
		" LEFT OUTER JOIN contratos cont ON (cont.codigo = c.contrato) " + 
		" LEFT OUTER JOIN convenios conv ON (conv.codigo = c.convenio) "+
		" LEFT OUTER JOIN estratos_sociales estra ON (estra.codigo = c.estrato_social) "+
		" LEFT OUTER JOIN tipos_afiliado tipa ON (tipa.acronimo = c.tipo_afiliado) " +
		" LEFT OUTER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo AND control_post_operatorio_cx IS NOT NULL)" ;
	
	public HashMap listarCitasPorAtenderMedico(	Connection con,HashMap campos)
	{
		return SqlBaseListadoCitasDao.listarCitasPorAtenderMedico(con, campos);
	}
	
	public ResultadoCollectionDB listarCitasPorPaciente(	Connection con,int codPaciente,int codMedico,String fechaInicio,String fechaFin,String horaInicio,String horaFin,int unidadConsulta, String estadoLiquidacion,int consultorio,String[] estadosCita, String centroAtencion,String postO,String tipoOrdenamiento) throws SQLException
	{
		return SqlBaseListadoCitasDao.listarCitas(con,codPaciente,codMedico,fechaInicio,fechaFin,horaInicio,horaFin,unidadConsulta, estadoLiquidacion,consultorio,estadosCita,centroAtencion,postO,tipoOrdenamiento, consultaCitasPorPaciente);
	}
	
	public ResultadoCollectionDB listarCitas(Connection con,int codPaciente, int idCuenta) throws SQLException
	{
		return SqlBaseListadoCitasDao.listarCitas(con, codPaciente, idCuenta, consultaCitasPorPaciente);
	}
	
	/**
	 * Consulta los servicios asociados a la cita
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap serviciosCita(Connection con, HashMap parametros)
	{
		return SqlBaseListadoCitasDao.serviciosCita(con, parametros);
	}
	
	/**
	 * Método implementado para obtener le fecha de la primera cita del paciente
	 * en estado asignada o reservada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerFechaPrimeraCitaPaciente(Connection con,int codigoPaciente)
	{
		return SqlBaseListadoCitasDao.obtenerFechaPrimeraCitaPaciente(con, codigoPaciente, DaoFactory.POSTGRESQL);
	}
}
