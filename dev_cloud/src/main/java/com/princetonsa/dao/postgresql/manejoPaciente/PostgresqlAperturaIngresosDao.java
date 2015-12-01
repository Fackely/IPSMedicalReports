package com.princetonsa.dao.postgresql.manejoPaciente;
import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dao.manejoPaciente.AperturaIngresosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAperturaIngresosDao;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class PostgresqlAperturaIngresosDao implements AperturaIngresosDao
{
	
	private static final String strConsultaIngresosXPaciente = " SELECT " +
			  " consecutivo As consecutivo0, " +
			  " to_char(fecha_ingreso,'DD/MM/YYYY') As fecha_ingreso1, " +
			  " hora_ingreso As hora_ingreso2, " +
			  " to_char(ci.fecha_cierre,'DD/MM/YYYY') As fecha_cierre3, " +
			  " hora_cierre As hora_cierrre4, " +
			  " getnombreusuario(ci.usuario_cierre) As nombre_usuario5, " +
			  " id As codigo_ingreso6, " +
			  " codigo_paciente As codigo_paciente7, " +
			  " getnomcentroatencion(ing.centro_atencion) As centro_atencion8, " +
			  " ing.centro_atencion As codigo_centro_atencion9, " +
			  " getcuentaxestado(ing.id) as cuenta10," +
			  " getviaingresocuenta(getcuentaxestado(ing.id)) As via_ingreso12, " +
			  " getnombreviaingreso(getviaingresocuenta(getcuentaxestado(ing.id))) As nom_via_ingreso13," +
			  " getdescmotivoingreso(ci.motivo_cierre) As nom_motivo_cierre15," +
			  " getnombreestadocuentaxcuenta(getcuentaxestado(ing.id)) As estado_cuenta16," +
			  " ci.codigo As codigo_cierre17," +
			  " coalesce(ing.reingreso||'','"+ConstantesBD.codigoNuncaValido+"') As reingreso18," +
			  " getcuentaxestado(ing.reingreso) as cuenta_reingreso19, " +
			  " coalesce(getconsecutivoingreso(ing.reingreso),'"+ConstantesBD.codigoNuncaValido+"') As consecutivo_reingreso20," +
			  " CASE WHEN preingreso='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As preingreso21 " +
		" FROM ingresos ing " +
		" INNER JOIN cierre_ingresos ci ON (ci.id_ingreso=ing.id)" +
		" WHERE ing.codigo_paciente=? AND  ing.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' " +
		" AND (select cuentas.estado_cuenta from cuentas where id_ingreso=ing.id order by id desc "+ValoresPorDefecto.getValorLimit1()+" 1) in(0,3,6) " +
		" AND ing.cierre_manual='"+ConstantesBD.acronimoSi+"'" +
		" AND ci.activo='"+ConstantesBD.acronimoSi+"' " +
		" AND (ing.control_post_operatorio_cx <> '"+ConstantesBD.acronimoSi+"' or ing.control_post_operatorio_cx is null ) " +
		" AND getcuentaxestado(ing.id)>0 ";
	
	/**
	 * Metodo encargado de listar los ingresos de un paciente
	 * @param connection
	 * @param codigoPaciente
	 * ------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- consecutivo0_
	 * -- fechaIngreso1_
	 * -- horaIngreso2_
	 * -- fechaCierre3_
	 * -- horaCierrre4_
	 * -- nombreUsuario5_
	 * -- codigoIngreso6_
	 * -- codigoPaciente7_
	 * -- centroAtencion8_
	 * -- codigoCentroAtencion9_
	 * -- cuenta10_
	 */
	public HashMap cargarListadoIngresos (Connection connection,String codigoPaciente,String ingreso)
	{
		return SqlBaseAperturaIngresosDao.cargarListadoIngresos(connection, codigoPaciente,ingreso,strConsultaIngresosXPaciente);
	}
	
	
	/**
	 * Metodo encargado de actualizar el estado del cierre.
	 * @param connection
	 * @param datosCierre
	 * ----------------------------
	 * KEY'S DEL MAPA CIERRE
	 * ----------------------------
	 * -- activo
	 * -- codigo
	 */
	public  boolean ActualizarEstadoCierre (Connection connection,HashMap datosCierre)
	{
		return SqlBaseAperturaIngresosDao.ActualizarEstadoCierre(connection, datosCierre);
	}
	
	
	/**
	 * Metodo encargado de hacer una apertera de un ingreso cerrado.
	 * @param connection
	 * @param datosIngreso
	 * ------------------------------
	 * KEY'S DEL MAPA DATOSINGRESO
	 * -------------------------------
	 * -- estado
	 * -- cierreManual
	 * -- cierreManual
	 * 
	 */
	public  boolean ActualizarEstadoIngreso (Connection connection,HashMap datosIngreso)
	{
		return SqlBaseAperturaIngresosDao.ActualizarEstadoIngreso(connection, datosIngreso);
	}
	
}