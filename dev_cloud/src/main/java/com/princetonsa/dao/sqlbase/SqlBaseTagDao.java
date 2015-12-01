/*
 * @(#)SqlBaseTagDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.Encoder;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a TagDao
 *
 *	@version 1.0, Mar 26, 2004
 */
public class SqlBaseTagDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTagDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar
	 * un valor por defecto en una BD genérica.
	 * (TagValoresPorDefecto)
	 */
	private static final String consultaTagValoresPorDefectoStr  = "SELECT valor, nombre FROM valores_por_defecto WHERE parametro = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para mostrar
	 * errores por usuario inactivo (login) en una BD genérica.
	 * (TagErroresIngreso)
	 */
	private static final String consultaTagErroresIngresoStr="SELECT count(1) as numResultados from usuarios_inactivos where login =? and password=?";

	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * si se debe mostrar el motivo de reversion en una BD genérica.
	 * (TagDeboMostrarDatosMotivoReversion)
	 */
	private static final String consultaTagDeboMostrarDatosMotivoReversionStr = "SELECT eg.mostrar_motivo_rev_epicrisis as deboMostrarMotivo, eg.motivo_reversion as motivoReversion, eg.fecha_reversion_egreso as fechaReversion, eg.hora_reversion_egreso as horaReversion, per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido as nombreCompleto  from egresos eg INNER JOIN personas per ON (eg.codigo_persona_reversion=per.codigo)  where eg.evolucion_genero_motivo_rev=?" ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * todos los barrios de una ciudad particular en una BD genérica.
	 * (TagMuestraBarriosStr)
	 */
	private static final String consultaTagMuestraBarriosStr="SELECT codigo_barrio as codigoBarrio, nombre as barrio from barrios where codigo_departamento=? and codigo_ciudad=? and nombre like UPPER(?) order by nombre";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el login de un usuario particular en una BD genérica.
	 * (TagBusquedaPersonas)
	 */
	private static final String consultaTagBusquedaPersonas_LoginStr="select us.login from usuarios us, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=us.codigo_persona";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * datos de salida en una BD genérica.
	 * (TagMuestaDatosSalidaEvolucion)
	 */
	private static final String consultaTagMuestaDatosSalidaEvolucionStr ="SELECT eg.estado_salida as estadoSalida, diag.nombre as diagnosticoMuerte, diagnostico_muerte as acronimoMuerte, diagnostico_muerte_cie as tipoCIEMuerte, eg.destino_salida as codigoDestinoSalida, eg.otro_destino_salida as otroDestinoSalida, destsal.nombre as destinoSalida from egresos eg, diagnosticos diag, destinos_salida destsal where eg.codigo_medico is not null and eg.diagnostico_muerte=diag.acronimo and eg.diagnostico_muerte_cie=diag.tipo_cie and eg.destino_salida=destsal.codigo and eg.evolucion=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * datos de salida en una BD genérica.
	 * (TagMuestaDatosSalidaEvolucion)
	 */
	private static final String consultaTagMuestraDiagnosticosStr = "SELECT diag.acronimo, diag.nombre, diag.tipo_cie from diagnosticos diag, admisiones_hospi adh where diag.acronimo=adh.diagnostico_admision and diag.tipo_cie=adh.diagnostico_cie_admision and codigo=?" ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el diagnostico de ingreso dado en admisión en una BD genérica.
	 * (TagMuestraDiagnosticoValoracionHospitalariaIngreso)
	 */
	private static final String consultaTagMuestraDiagnosticoValoracionHospitalariaIngresoStr = "SELECT diag.acronimo, diag.tipo_cie, diag.nombre from val_hospitalizacion valh, solicitudes sol, diagnosticos diag, cuentas cue where cue.id=sol.cuenta and sol.numero_solicitud=valh.numero_solicitud and valh.diagnostico_ingreso=diag.acronimo and valh.diagnostico_cie_ingreso=diag.tipo_cie and cue.id_ingreso=?" ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el diagnostico de ingreso dado en val. hosp. en una BD genérica.
	 * (TagMuestraDiagnosticoValoracionHospitalaria)
	 */
	private static final String consultaTagMuestraDiagnosticoValoracionHospitalariaStr = "SELECT diag.acronimo, diag.tipo_cie, diag.nombre from val_hospitalizacion valh, solicitudes sol, diagnosticos diag where sol.numero_solicitud=valh.numero_solicitud and valh.diagnostico_ingreso=diag.acronimo and valh.diagnostico_cie_ingreso=diag.tipo_cie and sol.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * las ciudades del sistema en una BD genérica.
	 * (TagMuestraDosTablasCiudadDepartamento)
	 */
	private static final String consultaTagMuestraDosTablasCiudadDepartamentoStr = "SELECT c.descripcion as ciudad, d.descripcion as departamento, c.codigo_departamento as codigo_departamento, c.codigo_ciudad as codigo_ciudad FROM departamentos d, ciudades c where d.codigo_departamento=c.codigo_departamento order by c.descripcion";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * estratos sociales (dadas ciertas restricciones) en una BD genérica.
	 * (TagMuestraEstratoSocial)
	 */
	private static final String consultaTagMuestraEstratoSocialStr="SELECT codigo, descripcion from estratos_sociales where tipo_regimen=? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * estratos sociales (dadas ciertas restricciones) para la subcuentas, arrojando como primer
	 * registro el de la madre.
	 * (TagMuestraEstratoSocial)
	 */
	private static final String consultaTagMuestraEstratoSocialMadreStr="SELECT codigo, descripcion from estratos_sociales where tipo_regimen=? and codigo =? and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " UNION ALL SELECT codigo, descripcion from estratos_sociales where tipo_regimen=? and codigo <> ? and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " order by descripcion ASC";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * las posibles especialidades en las cuales se puede pedir una interconsulta
	 * en una BD genérica.
	 * (TagMuestraPosiblesInterconsultas)
	 */
	private static final String consultaTagMuestraPosiblesInterconsultasStr="SELECT codigo, nombre from centros_costo where codigo!=0 and codigo NOT IN (select centro_costo from solicitudes_consulta where estado=" + ValoresPorDefecto.getValorFalseParaConsultas() + " and id_cuenta=?) and codigo!=8 and codigo!=3 and codigo!=4";

	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * las tipos de afiliado en el sistema en una BD genérica.
	 * (TagMuestraTipoAfiliado)
	 */
	private static final String consultaTagMuestraTipoAfiliadoStr="SELECT acronimo, nombre from tipos_afiliado";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la fecha y numero de solicitud de la valoración hospitalaria dada la
	 * cuenta en una BD genérica.
	 * (TagMuestraAsocioEvolucionValoracion)
	 */
	private static final String consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalariasStr="SELECT val.numero_solicitud as numeroSolicitud, val.fecha_valoracion as fechaValoracion from solicitudes sol, val_hospitalizacion valh, valoraciones val where sol.numero_solicitud=valh.numero_solicitud and sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la fecha y numero de solicitud de la valoración de urgencias dada la
	 * cuenta en una BD genérica.
	 * (TagMuestraAsocioEvolucionValoracion)
	 */
	private static final String consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgenciasStr="SELECT val.numero_solicitud as numeroSolicitud, val.fecha_valoracion as fechaValoracion from solicitudes sol, valoraciones_urgencias valu, valoraciones val where sol.numero_solicitud=valu.numero_solicitud and sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la fecha y numero de solicitud de las valoraciones de interconsulta
	 * dada la cuenta en una BD genérica.
	 * (TagMuestraAsocioEvolucionValoracion)
	 */
	private static final String consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStr="SELECT val.numero_solicitud as numeroSolicitud, val.fecha_valoracion as fechaValoracion from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and val.numero_solicitud NOT IN (select valh.numero_solicitud from solicitudes sol2, val_hospitalizacion valh where sol2.numero_solicitud=valh.numero_solicitud and sol2.cuenta=?) and val.numero_solicitud NOT IN (select valu.numero_solicitud from solicitudes sol3, valoraciones_urgencias valu where sol3.numero_solicitud=valu.numero_solicitud and sol3.cuenta=?) and sol.cuenta=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * el código del tipo de cie válido para una fecha dada
	 * (consultaTagBusquedaDiagnosticos_Cie)
	 */
	private static final String consultaTagBusquedaDiagnosticos_CieStr="SELECT codigo from tipos_cie where vigencia<=? order by vigencia desc";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * la fecha de la agenda, para una cita, dado el número de la
	 * solicitud
	 * (consultaTagBusquedaDiagnosticos_FechaCita)
	 */
	private static final String consultaTagBusquedaDiagnosticos_FechaCitaStr=" SELECT to_char(ag.fecha, 'YYYY-MM-DD') as fecha from agenda ag INNER JOIN cita ci ON (ag.codigo=ci.codigo_agenda) where ci.numero_solicitud=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * los datos necesarios de todas las valoraciones a las que se pueda asociar
	 * una evolución
	 * (consultaTagMuestraAsocioEvolucionValoracion)
	 */
	private static final String consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStr="" +
		/*SQL para todas*/
		" SELECT val.numero_solicitud as numeroSolicitud, val.fecha_valoracion as fechaValoracion, per.numero_identificacion as numIdMedico, per.tipo_identificacion as tipoIdMedico, 'interconsulta' as tipoValoracion, esp.nombre as especialidad from solicitudes sol  INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud)  INNER JOIN personas per ON (per.codigo=val.codigo_medico)  INNER JOIN solicitudes_inter solinter ON (sol.numero_solicitud=solinter.numero_solicitud and sol.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta+ " AND ( solinter.manejo_conjunto_finalizado=" + ValoresPorDefecto.getValorFalseParaConsultas() + " or (select count(1) from tratantes_cuenta tc where tc.hora_fin is null and tc.solicitud=sol.numero_solicitud)>0 ) )  INNER JOIN servicios ser ON (solinter.codigo_servicio_solicitado=ser.codigo)  INNER JOIN especialidades esp ON (ser.especialidad=esp.codigo) where  sol.cuenta=? " +
		" UNION " +
		" SELECT val2.numero_solicitud as numeroSolicitud, val2.fecha_valoracion as fechaValoracion, 'GJ' as numIdMedico, 'GK' as tipoIdMedico, 'urgencias' as tipoValoracion, 'vacio' as especialidad from solicitudes sol4, valoraciones_urgencias valu2, valoraciones val2 where sol4.numero_solicitud=valu2.numero_solicitud and sol4.numero_solicitud=val2.numero_solicitud and sol4.cuenta=? " +
		" UNION " +
		" SELECT val3.numero_solicitud as numeroSolicitud, val3.fecha_valoracion as fechaValoracion, 'GJ' as numIdMedico, 'GK' as tipoIdMedico, 'hospitalizacion' as tipoValoracion, 'vacio' as especialidad from solicitudes sol5, val_hospitalizacion valh2, valoraciones val3 where sol5.numero_solicitud=valh2.numero_solicitud and sol5.numero_solicitud=val3.numero_solicitud and sol5.cuenta=? " ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * los datos necesarios de la valoracion (y solicitud) a la que está asociada
	 * una evolución
	 * (consultaTagMuestraAsocioEvolucionValoracion2)
	 */
	private static final String consultaTagMuestraAsocioEvolucionValoracion2Str="" +
		" SELECT val1.fecha_valoracion as fechaValoracion, 'GJ' as numIdMedico, 'GK' as tipoIdMedico, 'hospitalizacion' as tipoValoracion, 'vacio' as especialidad from valoraciones val1, val_hospitalizacion valh1 where val1.numero_solicitud=valh1.numero_solicitud and val1.numero_solicitud=?   " +
		" UNION " +
		" SELECT val2.fecha_valoracion as fechaValoracion, 'GJ' as numIdMedico, 'GK' as tipoIdMedico, 'urgencias' as tipoValoracion, 'vacio' as especialidad from valoraciones val2, valoraciones_urgencias valu1 where val2.numero_solicitud=valu1.numero_solicitud and val2.numero_solicitud=?  " +
		" UNION " +
		" SELECT val3.fecha_valoracion as fechaValoracion, per2.numero_identificacion as numIdMedico, per2.tipo_identificacion as tipoIdMedico, 'interconsulta' as tipoValoracion, esp.nombre as especialidad from valoraciones val3  INNER JOIN personas per2 ON (val3.codigo_medico=per2.codigo) INNER JOIN solicitudes sol ON (val3.numero_solicitud=sol.numero_solicitud and sol.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta + " ) INNER JOIN  solicitudes_inter solinter ON (val3.numero_solicitud=solinter.numero_solicitud)  INNER JOIN servicios serv ON (solinter.codigo_servicio_solicitado=serv.codigo)  INNER JOIN especialidades esp ON (serv.especialidad=esp.codigo) where val3.numero_solicitud=?";
	/*
	 *  Esta es la consulta, por si en algún momento es necesario sacar valores
	 * reales de hospitalización. En cada línea se saca un tipo de valoración
	 * (urgencias, hospitalizados, interconsulta)
	 *
	 * (   SELECT val1.fecha_valoracion as fechaValoracion,val1.numero_identificacion_medico as numIdMedico, val1.tipo_identificacion_medico as tipoIdMedico, esp.nombre as especialidad, 'hospitalizacion' as tipoValoracion from valoraciones val1, val_hospitalizacion valh1,especialidades esp, solicitudes_consulta solcon where val1.numero_solicitud=solcon.numero_solicitud and val1.numero_solicitud=valh1.numero_solicitud and solcon.especialidad=esp.codigo and val1.numero_solicitud=1 )
	 * UNION
	 * (   SELECT val2.fecha_valoracion as fechaValoracion,val2.numero_identificacion_medico as numIdMedico, val2.tipo_identificacion_medico as tipoIdMedico, esp.nombre as especialidad, 'urgencias' as tipoValoracion from valoraciones val2, valoraciones_urgencias valu1, especialidades esp, solicitudes_consulta solcon where val2.numero_solicitud=solcon.numero_solicitud and val2.numero_solicitud=valu1.numero_solicitud and solcon.especialidad=esp.codigo and val2.numero_solicitud=1)
	 * UNION
	 * (  SELECT val3.fecha_valoracion as fechaValoracion,val3.numero_identificacion_medico as numIdMedico, val3.tipo_identificacion_medico as tipoIdMedico, esp.nombre as especialidad, 'interconsulta' as tipoValoracion from valoraciones val3, especialidades esp, solicitudes_consulta solcon where val3.numero_solicitud=solcon.numero_solicitud and solcon.especialidad=esp.codigo and val3.numero_solicitud NOT IN (select numero_solicitud from val_hospitalizacion) and val3.numero_solicitud NOT IN (select numero_solicitud from valoraciones_urgencias) and val3.numero_solicitud=1 )
	 */

	private static final String consultaTagMuestraAdmisiones_consultaAdmisionHospitalaria = "SELECT adh.codigo, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, adh.cuenta as codigoCuenta, ing.fecha_egreso as fechaEgreso, ing.hora_egreso as horaEgreso, estadm.nombre as estadoAdmision from admisiones_hospi adh, cuentas cue, ingresos ing, estados_admision estadm, personas per where adh.cuenta=cue.id and cue.id_ingreso=ing.id and adh.estado_admision=estadm.codigo and ing.codigo_paciente=per.codigo and per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? order by adh.fecha_admision DESC";

	private static final String consultaTagMuestraAdmisiones_consultaAdmisionUrgencias = "SELECT adh.codigo, adh.anio, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, adh.cuenta as codigoCuenta, ing.fecha_egreso as fechaEgreso, ing.hora_egreso as horaEgreso, cue.estado_cuenta as estadoCuenta from admisiones_urgencias adh, cuentas cue, ingresos ing, personas per where per.codigo=ing.codigo_paciente and adh.cuenta=cue.id and cue.id_ingreso=ing.id and per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? order by adh.fecha_admision DESC";

	private static final String consultaTagMuestraAdmisiones_consultaTodasAdmisiones =
	 "SELECT adh.codigo, adh.anio, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, adh.cuenta as codigoCuenta, ing.fecha_egreso as fechaEgreso, ing.hora_egreso as horaEgreso, cue.estado_cuenta as estadoCuenta, 'Sin estado' as estadoAdmision from admisiones_urgencias adh, cuentas cue, ingresos ing, personas per where per.codigo=ing.codigo_paciente and adh.cuenta=cue.id and cue.id_ingreso=ing.id and per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? " +
		"UNION " +
	 "SELECT adh.codigo, 0 as anio, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, adh.cuenta as codigoCuenta, ing.fecha_egreso as fechaEgreso, ing.hora_egreso as horaEgreso, -28 as estadoCuenta, estadm.nombre as estadoAdmision from admisiones_hospi adh, cuentas cue, ingresos ing, estados_admision estadm, personas per2 where per2.codigo=ing.codigo_paciente and adh.cuenta=cue.id and cue.id_ingreso=ing.id and adh.estado_admision=estadm.codigo and per2.numero_identificacion=? and per2.tipo_identificacion=? and ing.institucion=?  order by fechaAdmision DESC";

	private static String consultaTagMuestraEnfermerasActivasStr="";
	    
	
	
	
	
	/**
	 * Dada una conexion abierta con una base de datos genérica (si no existe, la crea) y una cadena con una
	 * consulta arbitraria, la ejecuta y retorna el <code>ResultSet</code> y la conexion en un objeto <code>Answer</code>.
	 * @param con una conexion abierta con una base de datos genérica
	 * @param consulta una cadena de texto con una consulta arbitraria
	 * @return un objeto <code>Answer</code> con el resultado de la consulta y la conexion abierta
	*/
	
	public static Answer resultadoConsulta (Connection con, String textoConsulta) throws SQLException
	{
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, textoConsulta);
		ResultSetDecorator resultado = new ResultSetDecorator(psd.executeQuery());
		//psd.cerrarPreparedStatement();
		return new Answer(resultado, con);
	}
	
	/* Version Tomcat 5
	public static Answer resultadoConsulta (Connection con, String textoConsulta) throws SQLException
	{
		if (con == null || con.isClosed()) {
			DaoFactory myFactory = DaoFactory.getDaoFactory(genéricaDaoFactory.tipoBD);
			con = myFactory.getConnection();
		}
		PreparedStatementDecorator ejecucionConsulta =  new PreparedStatementDecorator(con.prepareStatement(textoConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator resultado=ejecucionConsulta.executeQuery());

		int contadorResultados=0;
		while(resultado.next())
			contadorResultados++;
		resultado.setFetchSize(contadorResultados);
		resultado.beforeFirst();	
		return new Answer(resultado, con);
	}*/

	/**
	 * Dada una conexion abierta con una base de datos genérica (si no existe, la crea) y una cadena con una
	 * actualización arbitraria, ejecuta esta actualización
	 *
	 * @param con una conexion abierta con una base de datos genérica
	 * @param actualizacion una cadena de texto con una actualización arbitraria
	 * @return un <code>int</code> con el nùmero de actualizaciones hechas
	*/
	public static int resultadoActualizacion (Connection con, String actualizacion) throws SQLException
	{
		PreparedStatementDecorator ejecucionConsulta =  new PreparedStatementDecorator(con.prepareStatement(actualizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return ejecucionConsulta.executeUpdate();
	}


	/**
	 * Métodos particulares para cada Tag
	 */


	/**
	 * Implementación de la búsqueda de un valor por defecto
	 * en una BD genérica
	 * (TagValoresPorDefecto)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagValoresPorDefecto (Connection , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagValoresPorDefecto (Connection con, String parametro) throws SQLException
	{
		PreparedStatementDecorator consultaTagValoresPorDefectoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagValoresPorDefectoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagValoresPorDefectoStatement.setString(1, parametro);
		return new ResultSetDecorator(consultaTagValoresPorDefectoStatement.executeQuery());
	}

	/**
	 * Implementación del método para mostrar errores por usuario
	 * inactivo en una BD genérica
	 * (TagErroresIngreso)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagErroresIngreso (Connection , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagErroresIngreso (Connection con, String login, String passwordHasheado) throws SQLException
	{
		PreparedStatementDecorator consultaTagErroresIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagErroresIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagErroresIngresoStatement.setString(1, login);
		consultaTagErroresIngresoStatement.setString(2, passwordHasheado);

		return new ResultSetDecorator(consultaTagErroresIngresoStatement.executeQuery());
	}



	/**
	 * Implementación del método busca el login de un usuario
	 * dada su identificación en una BD genérica
	 * (TagBusquedaPersonas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonas_Login (Connection , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagBusquedaPersonas_Login (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion) throws SQLException
	{
		PreparedStatementDecorator consultaTagBusquedaPersonas_LoginStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaPersonas_LoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagBusquedaPersonas_LoginStatement.setString(1, numeroIdentificacion);
		consultaTagBusquedaPersonas_LoginStatement.setString(2, codigoTipoIdentificacion);

		return new ResultSetDecorator(consultaTagBusquedaPersonas_LoginStatement.executeQuery());
	}

	/**
	 * Implementación del método que revisa si en la evolución especificada el
	 * usuario vio el motivo de reversión en una BD genérica
	 * (TagDeboMostrarDatosMotivoReversion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagDeboMostrarDatosMotivoReversion(Connection con, int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagDeboMostrarDatosMotivoReversion(Connection con, int idEvolucion) throws SQLException
	{
		PreparedStatementDecorator consultaTagDeboMostrarDatosMotivoReversionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagDeboMostrarDatosMotivoReversionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagDeboMostrarDatosMotivoReversionStatement.setInt(1, idEvolucion);
		return new ResultSetDecorator(consultaTagDeboMostrarDatosMotivoReversionStatement.executeQuery());
	}
	
	/**
	 * Implementación del método que busca información básica de
	 * las admisiones de un paciente en una BD genérica
	 * (TagMuestraAdmisiones)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAdmisiones (Connection , String , String , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAdmisiones (Connection con, String numeroIdBusqueda, String tipoIdBusqueda, String modo, String codigoInstitucion) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAdmisionesStatement;

		//La consulta cambia dependiendo de si se quiere mostrar las
		//admisiones de urgencias las de hospitalización o todas.
		if (modo==null)
		{
			throw new SQLException("Tipo de admision invalido en (consultaTagMuestraAdmisiones )");
		}
		if (modo.equals("urgencias"))
		{
			consultaTagMuestraAdmisionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAdmisiones_consultaAdmisionUrgencias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTagMuestraAdmisionesStatement.setString(1, numeroIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(2, tipoIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(3, codigoInstitucion);

			return new ResultSetDecorator(consultaTagMuestraAdmisionesStatement.executeQuery());
		}
		else if (modo.equals("hospitalarias"))
		{
			consultaTagMuestraAdmisionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAdmisiones_consultaAdmisionHospitalaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTagMuestraAdmisionesStatement.setString(1, numeroIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(2, tipoIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(3, codigoInstitucion);

			return new ResultSetDecorator(consultaTagMuestraAdmisionesStatement.executeQuery());
		}
		else if (modo.equals ("general"))
		{
			consultaTagMuestraAdmisionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAdmisiones_consultaTodasAdmisiones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTagMuestraAdmisionesStatement.setString(1, numeroIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(2, tipoIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(3, codigoInstitucion);
			consultaTagMuestraAdmisionesStatement.setString(4, numeroIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(5, tipoIdBusqueda);
			consultaTagMuestraAdmisionesStatement.setString(6, codigoInstitucion);

			return new ResultSetDecorator(consultaTagMuestraAdmisionesStatement.executeQuery());
		}
		else
		{
			throw new SQLException("Tipo de admision invalido en (consultaTagMuestraAdmisiones)");
		}
	}

	/**
	 * Implementación del método que busca admisiones hospitalarias
	 * para un paciente / ingreso en una BD genérica
	 * (TagMuestraAdmisionesHospitalarias)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAdmisionesHospitalarias (Connection , String , String , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAdmisionesHospitalarias (Connection con, String idBusqueda, String tipoIdBusqueda, String numeroIdBusqueda, String codigoInstitucion) throws SQLException
	{
		final String consultaPorPaciente = "SELECT adh.cuenta as codigoCuenta, adh.codigo as codigoAdmision, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, oradh.nombre as origenAdmision, per.primer_nombre as primerNombreMedico, per.segundo_nombre as segundoNombreMedico, per.primer_apellido as primerApellidoMedico, per.segundo_apellido as segundoApellidoMedico, cexter.nombre as causaExterna, diag.nombre as diagnostico, adh.diagnostico_admision as codigoDiagnostico, adh.diagnostico_cie_admision as codigoCIEDiagnostico, cam.numero_cama as numeroCama, ccosto.nombre as centroCosto, estcam.nombre as estadoCama, tipuscam.nombre as tipoUsuarioCama, cam.descripcion as descripcionCama, adh.numero_autorizacion as numeroAutorizacion, adh.login_usuario as loginUsuario, estadm.nombre as estadoAdmision from admisiones_hospi adh, ori_admision_hospi oradh, personas per, causas_externas cexter, diagnosticos diag, camas1 cam, centros_costo ccosto, estados_cama as estcam, tipos_usuario_cama tipuscam, estados_admision estadm where adh.origen_admision_hospitalaria=oradh.codigo and per.codigo=adh.codigo_medico and adh.causa_externa=cexter.codigo and adh.diagnostico_admision=diag.acronimo and adh.diagnostico_cie_admision=diag.tipo_cie and adh.cama=cam.codigo and cam.centro_costo=ccosto.codigo and cam.estado=estcam.codigo and cam.tipo_usuario_cama=tipuscam.codigo and adh.estado_admision=estadm.codigo and adh.cuenta IN (SELECT id from cuentas where id_ingreso IN (SELECT id from ingresos ing2, personas per2 where per2.numero_identificacion ='" + numeroIdBusqueda + "' and per2.tipo_identificacion='" + tipoIdBusqueda+ "' and ing2.institucion=" + codigoInstitucion +  " and ing2.codigo_paciente=per.codigo)) order by adh.fecha_admision DESC";
		final String consultaPorAdmision = "SELECT adh.cuenta as codigoCuenta, adh.codigo as codigoAdmision, adh.fecha_admision as fechaAdmision, adh.hora_admision as horaAdmision, oradh.nombre as origenAdmision, per.primer_nombre as primerNombreMedico, per.segundo_nombre as segundoNombreMedico, per.primer_apellido as primerApellidoMedico, per.segundo_apellido as segundoApellidoMedico, cexter.nombre as causaExterna, diag.nombre as diagnostico, adh.diagnostico_admision as codigoDiagnostico, adh.diagnostico_cie_admision as codigoCIEDiagnostico, cam.numero_cama as numeroCama, ccosto.nombre as centroCosto, estcam.nombre as estadoCama, tipuscam.nombre as tipoUsuarioCama, cam.descripcion as descripcionCama, adh.numero_autorizacion as numeroAutorizacion, adh.login_usuario as loginUsuario, estadm.nombre as estadoAdmision from admisiones_hospi adh, ori_admision_hospi oradh, personas per, causas_externas cexter, diagnosticos diag, camas1 cam, centros_costo ccosto, estados_cama as estcam, tipos_usuario_cama tipuscam, estados_admision estadm where adh.origen_admision_hospitalaria=oradh.codigo and per.codigo=adh.codigo_medico and adh.causa_externa=cexter.codigo and adh.diagnostico_admision=diag.acronimo and adh.diagnostico_cie_admision=diag.tipo_cie and adh.cama=cam.codigo and cam.centro_costo=ccosto.codigo and cam.estado=estcam.codigo and cam.tipo_usuario_cama=tipuscam.codigo and adh.estado_admision=estadm.codigo and adh.codigo=" + idBusqueda+  " and (SELECT institucion from ingresos where id=(SELECT id_ingreso from cuentas where id=adh.cuenta))=" + codigoInstitucion +  " order by adh.fecha_admision DESC";

		String consultaTagMuestraAdmisionesHospitalariasStr = new String();

		if (idBusqueda!=null&&!idBusqueda.equals(""))
		{
			consultaTagMuestraAdmisionesHospitalariasStr = consultaPorAdmision;
		}
		else if (numeroIdBusqueda!=null&&tipoIdBusqueda!=null&&!numeroIdBusqueda.equals("")&&!tipoIdBusqueda.equals(""))
		{
			consultaTagMuestraAdmisionesHospitalariasStr = consultaPorPaciente;
		}
		else
		{
			throw new SQLException("Los criteriosbusqueda deben estar completos (Ya sea por admision o por persona) ");
		}

		PreparedStatementDecorator consultaTagMuestraAdmisionesHospitalariasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAdmisionesHospitalariasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraAdmisionesHospitalariasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los barrios
	 * de una ciudad específica en una BD genérica
	 * (TagMuestraBarrios)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraBarrios (Connection , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraBarrios (Connection con, String codDepartamento, String codCiudad,String nombre) 
	{
		try{
				PreparedStatementDecorator consultaTagMuestraBarriosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraBarriosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				consultaTagMuestraBarriosStatement.setString(1, codDepartamento);
				consultaTagMuestraBarriosStatement.setString(2, codCiudad);
				consultaTagMuestraBarriosStatement.setString(3, "%"+nombre+"%");
				
				return new ResultSetDecorator(consultaTagMuestraBarriosStatement.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error listando barrios en SqlBaseTagDao: "+e);
			return null;
			}
	}

	/**
	 * Implementación del método que busca camas que
	 * cumplan ciertas restricciones en una BD genérica
	 * (TagMuestraCamas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamas (Connection , String , String , boolean ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraCamas (
		Connection con, String sexo, String sinUrgencias, boolean conDesinfeccion, String codigoInstitucion, 
		String fechaMovimiento, String horaMovimiento, String centroAtencion, String viaIngreso) throws SQLException
	{
		String consultaTagMuestraCamasStr="SELECT " +
			"cam.codigo as codigo, " +
			"cam.numero_cama as numeroCama, " +
			"cam.centro_costo as centroCosto, " +
			"estcam.nombre as estadoCama, " +
			"ccosto.nombre as nombreCentroCosto, " +
			"tipuscam.nombre as tipoUsuario, " +
			"cam.descripcion as descripcion, " +
			"cam.habitacion AS habitacion, " +
			"CASE WHEN cam.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS esUci, " +
			"CASE WHEN cam.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN getnombretipomonitoreo(sc.tipo_monitoreo) ELSE '' END AS tipoMonitoreo " +
			"from camas1 cam " +
			"INNER JOIN estados_cama estcam on(cam.estado=estcam.codigo) " +
			"INNER JOIN centros_costo ccosto ON(cam.centro_costo=ccosto.codigo) " +
			"INNER JOIN tipos_usuario_cama tipuscam ON(cam.tipo_usuario_cama=tipuscam.codigo and cam.institucion=tipuscam.institucion) " +
			"LEFT OUTER JOIN servicios_cama sc ON(sc.codigo_cama=cam.codigo) " +
			"where " ;

		//El caso en que no se quiere mostrar con desinfección
		if (!conDesinfeccion)
		{
			consultaTagMuestraCamasStr +=
				"cam.estado="+ConstantesBD.codigoEstadoCamaDisponible+" and " +
				"cam.institucion = "+codigoInstitucion;
		}
		else
		{
			consultaTagMuestraCamasStr+=
					"(cam.estado="+ConstantesBD.codigoEstadoCamaDisponible+" or cam.estado="+ConstantesBD.codigoEstadoCamaDesinfeccion+") and " +
					"codigoInstitucion= "+codigoInstitucion;
		}

		//Dado que no hay una clasificación en la Base de datos
		//(de donde podamos saber si un tipo de usuario aplica para hombres  o por edad)

		//Si es hombre no puede ocupar camas de mujeres y viceversa
		if (sexo.equals("1"))
		{
			consultaTagMuestraCamasStr=consultaTagMuestraCamasStr + " and cam.tipo_usuario_cama != 1";
		}
		else
		{
			consultaTagMuestraCamasStr=consultaTagMuestraCamasStr + " and cam.tipo_usuario_cama != 2";
		}
		/*Si es menor de edad no puede estar pensionado y si es mayor no puede ser niño
		Criterio eliminado por incapacidad de definir a ciencia cierta casos extremos Ej. Joven
		17 años
		int edadEntero=Integer.parseInt(edad);
		if (edadEntero<18)
		{
			consulta=consulta + " and tipo_usuario_cama != 4";
		}
		else
		{
			consulta=consulta + " and tipo_usuario_cama != 3";
		}*/

		//Solo mostramos las que no sean de urgencias si la variable
		//"sinUrgencias" tiene como valor "true"

		if (sinUrgencias!=null&&sinUrgencias.equals("true"))
		{
			consultaTagMuestraCamasStr=consultaTagMuestraCamasStr + " and cam.centro_costo!=" +ValoresPorDefecto.getCentroCostoUrgencias(Integer.parseInt(codigoInstitucion));
		}

		if(fechaMovimiento!=null && !fechaMovimiento.equals(""))
		{
		    consultaTagMuestraCamasStr+=" AND cam.codigo NOT IN (SELECT codigo_nueva_cama from traslado_cama where ((fecha_asignacion>'"+UtilidadFecha.conversionFormatoFechaABD(fechaMovimiento)+"') or (fecha_asignacion='"+fechaMovimiento+"' AND hora_asignacion>='"+UtilidadFecha.conversionFormatoFechaABD(horaMovimiento)+"')))";
		}
		
		if(centroAtencion!=null&&!centroAtencion.equals("")&&viaIngreso!=null&&!viaIngreso.equals(""))
			consultaTagMuestraCamasStr += " AND cam.centro_costo IN (" +
				"SELECT c.codigo " +
				"FROM centros_costo c " +
				"INNER JOIN centro_costo_via_ingreso cv ON(c.codigo=cv.centro_costo) " +
				"WHERE cv.via_ingreso = "+viaIngreso+" and c.centro_atencion = "+centroAtencion+") ";
		    
		consultaTagMuestraCamasStr = consultaTagMuestraCamasStr + " ORDER BY cam.habitacion, cam.numero_cama";
		PreparedStatementDecorator consultaTagMuestraCamasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraCamasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraCamasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca camas que
	 * cumplan ciertas restricciones en una BD genérica
	 * (TagMuestraCamasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamasStruts (Connection , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraCamasStruts (
		Connection con, String sexo, String codigoCentroCosto, String codEstado, 
		String codigoInstitucion,String centroAtencion,String viaIngreso) throws SQLException
	{
		String consultaTagMuestraCamasStrutsStr="SELECT "+ 
			"cam.codigo as codigo, "+ 
			"cam.numero_cama as numeroCama, "+ 
			"cam.centro_costo as centroCosto, "+ 
			"estcam.nombre as estadoCama, "+ 
			"getnomcentrocosto(cam.centro_costo) as nombreCentroCosto, "+ 
			"tipuscam.nombre as tipoUsuario, "+ 
			"cam.descripcion as descripcion, "+ 
			"cam.habitacion as habitacion "+ 
			"from camas1 cam "+ 
			"inner join estados_cama estcam on(cam.estado=estcam.codigo) "+ 
			"inner join tipos_usuario_cama tipuscam on(cam.tipo_usuario_cama=tipuscam.codigo) "+ 
			"where cam.institucion="+codigoInstitucion;

		//Dado que no hay una clasificación en la Base de datos
		//(de donde podamos saber si un tipo de usuario aplica para hombres  o por edad)


		//Si es hombre no puede ocupar camas de mujeres y viceversa
		if( UtilidadCadena.noEsVacio(sexo) )
		{
			if (sexo.equals("1"))
			{
				consultaTagMuestraCamasStrutsStr=consultaTagMuestraCamasStrutsStr + " and cam.tipo_usuario_cama != 1";
			}
			else
			{
				consultaTagMuestraCamasStrutsStr=consultaTagMuestraCamasStrutsStr + " and cam.tipo_usuario_cama != 2";
			}
		}

		if( UtilidadCadena.noEsVacio(codEstado) )
		{
			consultaTagMuestraCamasStrutsStr = consultaTagMuestraCamasStrutsStr + " AND cam.estado = "+codEstado;
		}
		else
		{
			consultaTagMuestraCamasStrutsStr = consultaTagMuestraCamasStrutsStr + " AND cam.estado = 0";
		}
		/*Si es menor de edad no puede estar pensionado y si es mayor no puede ser niño
		Criterio eliminado por incapacidad de definir a ciencia cierta casos extremos Ej. Joven
		17 años
		int edadEntero=Integer.parseInt(edad);
		if (edadEntero<18)
		{
			consulta=consulta + " and tipo_usuario_cama != 4";
		}
		else
		{
			consulta=consulta + " and tipo_usuario_cama != 3";
		}*/
		if (!codigoCentroCosto.equals(""))
		{
			consultaTagMuestraCamasStrutsStr=consultaTagMuestraCamasStrutsStr + " and cam.centro_costo= "+codigoCentroCosto;
		}
		
		if(centroAtencion!=null&&!centroAtencion.equals("")&&viaIngreso!=null&&!viaIngreso.equals(""))
			consultaTagMuestraCamasStrutsStr+= " and cam.centro_costo IN ( "+
				"SELECT c.codigo " +
				"FROM centros_costo c " +
				"INNER JOIN centro_costo_via_ingreso cv ON(c.codigo=cv.centro_costo) " +
				"WHERE cv.via_ingreso = "+viaIngreso+" and c.centro_atencion = "+centroAtencion+") ";
		
		else if(centroAtencion!=null&&!centroAtencion.equals("")&&(viaIngreso==null||viaIngreso.equals("")))
			consultaTagMuestraCamasStrutsStr+= " and cam.centro_costo IN ( "+
				"SELECT codigo " +
				"FROM centros_costo " +
				"WHERE centro_atencion = "+centroAtencion+") ";

		consultaTagMuestraCamasStrutsStr = consultaTagMuestraCamasStrutsStr + " ORDER BY cam.habitacion, cam.numero_cama";

		PreparedStatementDecorator consultaTagMuestraCamasStrutsStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraCamasStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		return new ResultSetDecorator(consultaTagMuestraCamasStrutsStatement.executeQuery());

	}

	/**
	 * Implementación del método que busca datos de orden de
	 * salida llenados por una evoluciónen una BD genérica
	 * (TagMuestaDatosSalidaEvolucion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestaDatosSalidaEvolucion (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestaDatosSalidaEvolucion (Connection con, int idEvolucion) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestaDatosSalidaEvolucionStatement = new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestaDatosSalidaEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestaDatosSalidaEvolucionStatement.setInt(1, idEvolucion);
		return new ResultSetDecorator(consultaTagMuestaDatosSalidaEvolucionStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los diagnosticos llenados
	 * en la admisión hospitalaria en una BD genérica
	 * (TagMuestraDiagnosticosAdmisionHospitalizacion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraDiagnosticosAdmisionHospitalizacion (Connection con, int codigoAdmision) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraDiagnosticosStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraDiagnosticosStatement.setInt(1, codigoAdmision);
		return new ResultSetDecorator(consultaTagMuestraDiagnosticosStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los diagnosticos llenados
	 * en la valoración hospitalaria en una BD genérica
	 * (TagMuestraDiagnosticoValoracionHospitalariaIngreso)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaIngreso (Connection con, int codigoIngreso) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraDiagnosticoValoracionHospitalariaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraDiagnosticoValoracionHospitalariaIngresoStatement.setInt(1, codigoIngreso);
		return new ResultSetDecorator(consultaTagMuestraDiagnosticoValoracionHospitalariaIngresoStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los diagnosticos llenados
	 * en la valoración hospitalaria en una BD genérica
	 * (TagMuestraDiagnosticoValoracionHospitalaria)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraDiagnosticoValoracionHospitalaria (Connection con, int codigoCuenta) throws SQLException
	{

		String tieneCuentaAdmisionHospitalizacionStr="SELECT codigo from admisiones_hospi where cuenta =?";
		PreparedStatementDecorator tieneCuentaAdmisionHospitalizacionStatement= new PreparedStatementDecorator(con.prepareStatement(tieneCuentaAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneCuentaAdmisionHospitalizacionStatement.setInt(1, codigoCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(tieneCuentaAdmisionHospitalizacionStatement.executeQuery());
		if (!rs.next())
		{
			String buscarCuentaHospitalizadoStr="SELECT c2.id from cuentas c1, cuentas c2 where c1.id!=c2.id and c1.id_ingreso=c2.id_ingreso and c1.id=?";
			PreparedStatementDecorator buscarCuentaHospitalizadoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCuentaHospitalizadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCuentaHospitalizadoStatement.setInt(1, codigoCuenta);
			rs=new ResultSetDecorator(buscarCuentaHospitalizadoStatement.executeQuery());
			if (rs.next())
			{
				codigoCuenta=rs.getInt("id");
			}
		}
		rs.close();

		PreparedStatementDecorator consultaTagMuestraDiagnosticoValoracionHospitalariaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraDiagnosticoValoracionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		consultaTagMuestraDiagnosticoValoracionHospitalariaStatement.setInt(1, codigoCuenta);
		return new ResultSetDecorator(consultaTagMuestraDiagnosticoValoracionHospitalariaStatement.executeQuery());
		
	}

	/**
	 * Implementación del método que busca las ciudades
	 * presentes en el sistema en una BD genérica
	 * (TagMuestraDosTablasCiudadDepartamento)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraDosTablasCiudadDepartamento (Connection ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraDosTablasCiudadDepartamento (Connection con) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraDosTablasCiudadDepartamentoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraDosTablasCiudadDepartamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraDosTablasCiudadDepartamentoStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los estratos
	 * sociales dadas ciertas restricciones en una BD genérica
	 * (TagMuestraEstratoSocial)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraEstratoSocial (Connection , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraEstratoSocial (Connection con, String codigoTipoRegimen, boolean restringirPorActivo,int codigoEstratoMadre) throws SQLException
	{
		String consultaRealStr;
		PreparedStatementDecorator consultaTagMuestraEstratoSocialStatement;
		if (codigoEstratoMadre==0)
		{
			consultaRealStr=consultaTagMuestraEstratoSocialStr;
			if (restringirPorActivo)
			{
				consultaRealStr=consultaRealStr + " and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " order by descripcion ASC";
			}
			consultaTagMuestraEstratoSocialStatement= new PreparedStatementDecorator(con.prepareStatement(consultaRealStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTagMuestraEstratoSocialStatement.setString(1, codigoTipoRegimen);
		}
		else
		{
			consultaRealStr=consultaTagMuestraEstratoSocialMadreStr;
			consultaTagMuestraEstratoSocialStatement= new PreparedStatementDecorator(con.prepareStatement(consultaRealStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaTagMuestraEstratoSocialStatement.setString(1,codigoTipoRegimen);
			consultaTagMuestraEstratoSocialStatement.setInt(2,codigoEstratoMadre);
			consultaTagMuestraEstratoSocialStatement.setString(3,codigoTipoRegimen);
			consultaTagMuestraEstratoSocialStatement.setInt(4,codigoEstratoMadre);
		}
		return new ResultSetDecorator(consultaTagMuestraEstratoSocialStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca las epicrisis
	 * de un paciente en una BD genérica
	 * (TagMuestraIngresosEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraIngresosEpicrisis (Connection , String , String , int , int , String , boolean , boolean, boolean, String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraIngresosEpicrisis (Connection con, PersonaBasica paciente, int codigoCentroCostoMedico, String modo, boolean mostrarHospitalizacion, boolean mostrarSoloActual, boolean mostrarSoloNoActual, String codigoInstitucion) throws SQLException
	{
		String consultaTagMuestraIngresosEpicrisisStr = "";

		//Estos String definen el texto de la restricción sobre que epicrisis mostrar
		//(Si no se definio si se queria abierta o cerrada estas restricciones quedan
		//en su valor vacío

		String restriccionEpicrisisSoloActual="", restriccionEpicrisisSoloNoActual="";

		if (mostrarSoloActual)
		{
			restriccionEpicrisisSoloActual=" and cue.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+") ";
		}
		else if (mostrarSoloNoActual)
		{
			restriccionEpicrisisSoloNoActual=" and cue.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+") ";
		}
		if (modo.equals("accesoTotal"))
		{
			//Si el modo es accesoTotal, utilizamos restricciones suaves
			if (mostrarHospitalizacion)
			{
				consultaTagMuestraIngresosEpicrisisStr="SELECT " +
					"ing.id as id_ingreso, " +
					"cue.id as id_cuenta " +
					"from ingresos ing " +
					"INNER JOIN cuentas cue ON(cue.id_ingreso=ing.id) " +
					"INNER JOIN epicrisis epi ON(epi.ingreso=cue.id_ingreso) " +
					"where  " +
					"ing.codigo_paciente=" + paciente.getCodigoPersona() + " and " +
					"cue.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" "+ 
					restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual  ;
			}
			else
			{
				consultaTagMuestraIngresosEpicrisisStr="SELECT " +
					"ing.id as id_ingreso, " +
					"cue.id as id_cuenta " +
					"from ingresos ing " +
					"INNER JOIN cuentas cue ON(cue.id_ingreso=ing.id) " +
					"INNER JOIN epicrisis epi ON(epi.ingreso=ing.id) " +
					"INNER JOIN solicitudes sol ON(sol.cuenta=cue.id) " +
					"INNER JOIN valoraciones_urgencias valu ON(valu.numero_solicitud=sol.numero_solicitud and valu.codigo_conducta_valoracion="+ConstantesBD.codigoConductaSeguirCamaObservacion+") " +
					"where ing.codigo_paciente=" + paciente.getCodigoPersona()  + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual ;
			}
		}
		else if (modo.equals("accesoRestringido"))
		{
			int codigoCentroCostoTratante=UtilidadValidacion.getCodigoCentroCostoTratante(con, paciente, Integer.parseInt(codigoInstitucion));
			if (mostrarHospitalizacion)
			{
				//Si el modo es accesoRestringido, utilizamos restricciones fuertes

				if (codigoCentroCostoTratante==codigoCentroCostoMedico)
				{
					consultaTagMuestraIngresosEpicrisisStr =
						"SELECT " +
						"cue.id as id_cuenta, " +
						"cue.id_ingreso " +
						"from cuentas cue  " +
						"INNER JOIN ingresos ing ON(ing.id=cue.id_ingreso) " +
						"where  " +
						"cue.id = "+paciente.getCodigoCuenta()+" and " +
						"cue.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and "+
						"cue.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+") and " +
						"ing.institucion=" + codigoInstitucion + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual  ; 
					
				}
				else
				{
					consultaTagMuestraIngresosEpicrisisStr =
						"SELECT " +
						"cue.id as id_cuenta, " +
						"cue.id_ingreso " +
						"from cuentas cue  " +
						"INNER JOIN ingresos ing ON(ing.id=cue.id_ingreso) " +
						"where  " +
						"cue.id = "+paciente.getCodigoCuenta()+" and " +
						"cue.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and "+
						"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" and " +
						"ing.institucion=" + codigoInstitucion + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual  ;
				}

				//Consulta Anterior - (Revisaba directamente si era tratante, ahora esto se hace con un método)
				//consultaTagMuestraIngresosEpicrisisStr = "(SELECT cue.id as id_cuenta, cue.id_ingreso from cuentas cue, ingresos ing where ing.id=cue.id_ingreso " + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual  + " and cue.id IN (SELECT concuen.id_cuenta from consultas_cuenta concuen, solicitudes_consulta solcon, val_hospitalizacion valh where concuen.id_cuenta=solcon.id_cuenta and concuen.centro_costo=solcon.centro_costo and solcon.numero_solicitud=valh.numero_solicitud and concuen.id_cuenta=" + paciente.getCodigoCuenta()+ " and concuen.centro_costo=" + codigoCentroCostoMedico + " and concuen.tratante=" + ValoresPorDefecto.getValorTrueParaConsultas() + ") and (cue.estado_cuenta=0 or cue.estado_cuenta=3) and ing.institucion=" + codigoInstitucion +")  UNION (SELECT cuentasPer.id_cuenta, cuentasPer.id_ingreso from val_hospitalizacion valh, solicitudes_consulta solcon, (SELECT cue.id_ingreso as id_ingreso, cue.id as id_cuenta from cuentas cue, ingresos ing where cue.id_ingreso=ing.id and cue.estado_cuenta=1 " + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual +  " and cue.numero_identificacion_paciente='" + paciente.getNumeroIdentificacionPersona() + "' and cue.tipo_identificacion_paciente='" + paciente.getCodigoTipoIdentificacionPersona()+ "' and ing.institucion=" + codigoInstitucion + ") cuentasPer where valh.numero_solicitud=solcon.numero_solicitud and solcon.id_cuenta=cuentasPer.id_cuenta)";
			}
			else
			{
				if (codigoCentroCostoTratante==codigoCentroCostoMedico)
				{
					consultaTagMuestraIngresosEpicrisisStr = 
					" SELECT "+
					"cue.id As id_cuenta, "+
					"cue.id_ingreso "+ 
					"FROM cuentas cue "+ 
					"INNER JOIN ingresos ing ON(ing.id = cue.id_ingreso) "+ 
					"INNER JOIN solicitudes sol ON(sol.cuenta = cue.id) "+ 
					"INNER JOIN valoraciones_urgencias valu ON(valu.numero_solicitud=sol.numero_solicitud AND valu.codigo_conducta_valoracion="+ConstantesBD.codigoConductaSeguirCamaObservacion+") "+ 
					"WHERE "+ 
					"cue.id = "+paciente.getCodigoCuenta()+" and "+
					"cue.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturada+","+ConstantesBD.codigoEstadoCuentaAsociada+")  AND "+ 
					"ing.institucion=" + codigoInstitucion + 
					restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual ;
				}
				else
				{
					consultaTagMuestraIngresosEpicrisisStr =
					" SELECT "+ 
					"cue.id As id_cuenta, "+
					"cue.id_ingreso "+ 
					"FROM cuentas cue "+ 
					"INNER JOIN ingresos ing ON(ing.id = cue.id_ingreso) "+ 
					"INNER JOIN solicitudes sol ON(sol.cuenta = cue.id) "+ 
					"INNER JOIN valoraciones_urgencias valu ON(valu.numero_solicitud=sol.numero_solicitud AND valu.codigo_conducta_valoracion="+ConstantesBD.codigoConductaSeguirCamaObservacion+") "+ 
					"WHERE "+ 
					"cue.id = "+paciente.getCodigoCuenta()+" and "+
					"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+"  AND "+ 
					"ing.institucion=" + codigoInstitucion +" "+ 
					restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual; 
				}

				//ConsultaAnterior: Cuando se revisaba directamente si era el tratante o no
				//consultaTagMuestraIngresosEpicrisisStr = "(SELECT cue.id as id_cuenta, cue.id_ingreso from cuentas cue, ingresos ing where ing.id=cue.id_ingreso " + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual + " and cue.id IN (SELECT concuen.id_cuenta from consultas_cuenta concuen, solicitudes_consulta solcon, valoraciones_urgencias valu where concuen.id_cuenta=solcon.id_cuenta and concuen.centro_costo=solcon.centro_costo and solcon.numero_solicitud=valu.numero_solicitud and valu.codigo_conducta_valoracion=3 and concuen.id_cuenta=" + paciente.getCodigoCuenta()+ " and concuen.centro_costo=" + codigoCentroCostoMedico + " and concuen.tratante=" + ValoresPorDefecto.getValorTrueParaConsultas() + ") and (cue.estado_cuenta=0 or cue.estado_cuenta=3) and ing.institucion=" + codigoInstitucion +")  UNION  (SELECT cuentasPer.id_cuenta, cuentasPer.id_ingreso from valoraciones_urgencias valu, solicitudes sol, (SELECT cue.id_ingreso as id_ingreso, cue.id as id_cuenta from cuentas cue, ingresos ing where cue.id_ingreso=ing.id and cue.estado_cuenta=1 " + restriccionEpicrisisSoloActual +  restriccionEpicrisisSoloNoActual + " and cue.numero_identificacion_paciente='" + paciente.getNumeroIdentificacionPersona()+ "' and cue.tipo_identificacion_paciente='" + paciente.getCodigoTipoIdentificacionPersona() + "' and ing.institucion=" + codigoInstitucion + ") cuentasPer where valu.numero_solicitud=sol.numero_solicitud and sol.cuenta=cuentasPer.id_cuenta and valu.codigo_conducta_valoracion=3)";
			}

		}
		//Con los resultados vamos generando la
		//consulta que nos darán las fechas/horas
		//de ingreso a admision y egreso (Si no existe
		//egreso la consulta retorna '1810-05-05'
		//caso que manejamos en el tag)

		//La parte dinamica de la consulta
		String consultaFechasDinamicoEg="(";
		String consultaFechasDinamicoAdmision="(";
		String consultaFechasDinamicoSinNada="(";

		
		
		//Se pasan los resultados a un HashMap
		HashMap ingresos = UtilidadBD.cargarValueObject(new ResultSetDecorator(resultadoConsulta(con, consultaTagMuestraIngresosEpicrisisStr).getResultSet()), true, false);
		
		//Mientras haya resultados de ingreso se van
		//agregando a la consulta

		boolean existeAlMenosUnResultado=false;
		
		//Única diferencia entre Hsqldb y Postgresql, y dado
		//que esta consulta es de las más completas, se centralizó
		//con excepción de estas líneas
		int tamanioArreglo=0;
		
		for (int i=0;i<Integer.parseInt(ingresos.get("numRegistros").toString());i++)
		{
			tamanioArreglo++;
		}
		
		
		
		//Volvemos a ejecutar la consulta
		int codigosIngreso[]=new int[tamanioArreglo];
		int codigosCuenta[]=new int[codigosIngreso.length];

		int i=0;
		for (i=0;i<codigosIngreso.length;i++)
		{
			codigosIngreso[i]=Integer.parseInt(ingresos.get("id_ingreso_"+i).toString());
			codigosCuenta[i]= Integer.parseInt(ingresos.get("id_cuenta_"+i).toString());

			existeAlMenosUnResultado=true;

			if (i==0)
			{
				consultaFechasDinamicoEg += " eg.cuenta=" + codigosCuenta[i];
				if (mostrarHospitalizacion)
					consultaFechasDinamicoAdmision+=" adh.cuenta=" + codigosCuenta[i];
				else
					consultaFechasDinamicoAdmision+=" adu.cuenta=" + codigosCuenta[i];
				consultaFechasDinamicoSinNada+= " cuenta="+ codigosCuenta[i];
			}
			else
			{
				consultaFechasDinamicoEg += " or eg.cuenta=" + codigosCuenta[i];
				if (mostrarHospitalizacion)
				{
					consultaFechasDinamicoAdmision += "or adh.cuenta=" + codigosCuenta[i];
				}
				else
				{
					consultaFechasDinamicoAdmision+=" or adu.cuenta=" + codigosCuenta[i];
				}
				consultaFechasDinamicoSinNada+= " or cuenta="+ codigosCuenta[i];
			}

		}

		if (existeAlMenosUnResultado)
		{
			consultaFechasDinamicoEg=consultaFechasDinamicoEg + ")";
			consultaFechasDinamicoAdmision=consultaFechasDinamicoAdmision+ ")";
			consultaFechasDinamicoSinNada=consultaFechasDinamicoSinNada + ")";
			
			if (consultaFechasDinamicoEg==null||consultaFechasDinamicoEg.equals(""))
			{
				consultaFechasDinamicoEg="eg.codigo_medico is not null";
			}

			String consultaFechas="";

			String consultaIngresosReversionEgreso="";

			if (mostrarHospitalizacion)
			{
				//La consulta preliminar muestra las consultas que están reversión de egreso
				consultaIngresosReversionEgreso=
					"( "+
						"SELECT "+ 
						"'1810-05-05' as fecha_egreso, "+ 
						"'9:35' as hora_egreso, "+
						"adh.fecha_admision, "+ 
						"adh.hora_admision, "+ 
						"cue.id_ingreso as codigo_ingreso "+ 
						"from egresos eg "+ 
						"INNER JOIN admisiones_hospi adh ON(adh.cuenta=eg.cuenta) "+ 
						"INNER JOIN cuentas cue ON(cue.id=adh.cuenta) "+ 
						"where  "+ consultaFechasDinamicoEg+" and eg.codigo_medico is null "+ 
					")";
				consultaFechas= consultaIngresosReversionEgreso +
					" UNION " +
					"( "+
						"SELECT "+ 
						"eg.fecha_egreso, "+ 
						"eg.hora_egreso, "+ 
						"adh.fecha_admision, "+ 
						"adh.hora_admision, "+  
						"cue.id_ingreso as codigo_ingreso "+
						"from egresos eg "+ 
						"INNER JOIN admisiones_hospi adh ON(adh.cuenta=eg.cuenta) "+  
						"INNER JOIN cuentas cue ON(cue.id=adh.cuenta) "+ 
						" where "+ consultaFechasDinamicoEg + " and eg.codigo_medico is not null "+ 
					") "+
					"UNION "+ 
					"( "+
						"SELECT "+ 
						"'1810-05-05' as fecha_egreso, "+ 
						"'9:35' as hora_egreso, "+ 
						"adh.fecha_admision, "+ 
						"adh.hora_admision, "+  
						"cue.id_ingreso as codigo_ingreso "+  
						"from admisiones_hospi adh "+
						"INNER JOIN cuentas cue ON(cue.id=adh.cuenta) "+ 
						"where "+  
						consultaFechasDinamicoAdmision + 
						" and adh.cuenta not in (SELECT cuenta from egresos where " + consultaFechasDinamicoSinNada + ") "+
					") "; 
				
				//Consultas Antes de Nueva BD
				//consultaIngresosReversionEgreso=" (SELECT '1810-05-05' as fecha_egreso, '9:35' as hora_egreso, adh.fecha_admision, adh.hora_admision, cue.id_ingreso as codigo_ingreso from egresos eg, admisiones_hospi adh, cuentas cue where cue.id=eg.cuenta and eg.cuenta=adh.cuenta and eg.codigo_medico is null and "+ consultaFechasDinamicoEg + ")";
				//consultaFechas="(" + consultaIngresosReversionEgreso +" UNION (SELECT eg.fecha_egreso, eg.hora_egreso, adh.fecha_admision, adh.hora_admision,  cue.id_ingreso as codigo_ingreso from egresos eg, admisiones_hospi adh, cuentas cue where cue.id=eg.cuenta and eg.cuenta=adh.cuenta and eg.codigo_medico is not null and "+ consultaFechasDinamicoEg + ") UNION (SELECT '1810-05-05' as fecha_egreso, '9:35' as hora_egreso, adh.fecha_admision, adh.hora_admision,  cue.id_ingreso as codigo_ingreso  from admisiones_hospi adh, cuentas cue where cue.id=adh.cuenta and adh.cuenta not in (SELECT cuenta from egresos where " + consultaFechasDinamicoSinNada + ") and " + consultaFechasDinamicoAdmision + " ) )";
			}
			else
			{
				consultaIngresosReversionEgreso=
					"( "+
						"SELECT "+ 
						"'1810-05-05' as fecha_egreso, "+ 
						"'9:35' as hora_egreso, "+ 
						"adu.fecha_admision, "+
						"adu.hora_admision, "+  
						"cue.id_ingreso as codigo_ingreso "+ 
						"from egresos eg "+ 
						"INNER JOIN admisiones_urgencias adu ON(adu.cuenta=eg.cuenta) "+ 
						"INNER JOIN cuentas cue ON(cue.id=adu.cuenta) "+ 
						"where "+ 
						consultaFechasDinamicoEg +" and eg.codigo_medico is null "+ 
					") ";

				consultaFechas= consultaIngresosReversionEgreso +" UNION "+  
					"( "+ 
						"SELECT "+ 
						"eg.fecha_egreso, "+ 
						"eg.hora_egreso, "+ 
						"adu.fecha_admision, "+ 
						"adu.hora_admision, "+  
						"cue.id_ingreso as codigo_ingreso "+ 
						"from egresos eg "+
						"INNER JOIN admisiones_urgencias adu ON(adu.cuenta=eg.cuenta) "+ 
						"INNER JOIN cuentas cue ON(cue.id=adu.cuenta) where "+  
						consultaFechasDinamicoEg + " and eg.codigo_medico is not null "+ 
					") "+
					"UNION "+ 
					"( "+
						"SELECT "+ 
						"'1810-05-05' as fecha_egreso, "+ 
						"'9:35' as hora_egreso, "+ 
						"adu.fecha_admision, "+ 
						"adu.hora_admision, "+  
						"cue.id_ingreso as codigo_ingreso "+  
						"from admisiones_urgencias adu "+ 
						"INNER JOIN cuentas cue ON(cue.id=adu.cuenta) "+ 
						"where "+ 
						consultaFechasDinamicoAdmision + " AND "+ 
						"adu.cuenta not in (SELECT cuenta from egresos where " + consultaFechasDinamicoSinNada + ") "+
					")" ;
				
				//Mismos cambios
			}

			//Para que queden ordenadas descendentemente

			consultaFechas=consultaFechas + " order by fecha_admision desc, hora_admision desc";

			PreparedStatementDecorator consultaTagMuestraIngresosEpicrisisStatement= new PreparedStatementDecorator(con.prepareStatement(consultaFechas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return new ResultSetDecorator(consultaTagMuestraIngresosEpicrisisStatement.executeQuery());
		}
		else
		{
			return null;
		}

		
		
	}

	/**
	 * Implementación del método que busca todas las instituciones
	 * válidas en una BD genérica
	 * (TagMuestraInstituciones)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraInstituciones (con) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraInstituciones (Connection con) throws SQLException
	{
		//Lo único especial de esta consulta es que no muestra la institución inactiva (En ninguna parte se debería ver)
		String consultaTagMuestraInstitucionesStr="select emp.codigo,ins.razon_social as nombre,  facturacion.getNombreContacto(emp.codigo)  as nombre_contacto from empresas emp, instituciones ins where emp.codigo=ins.codigo and ins.codigo!=0";
		PreparedStatementDecorator consultaTagMuestraInstitucionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraInstitucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraInstitucionesStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca todas las posibles
	 * naturalezas de un paciente, sin incluir embarazada (caso
	 * hombres) en una BD genérica
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezas (Connection con, String sexo) throws SQLException
	{
		String consultaTagMuestraNaturalezaPacienteStr;
		if (sexo.equals("2"))
		{
			consultaTagMuestraNaturalezaPacienteStr="SELECT codigo, nombre from naturaleza_pacientes";
		}
		else
		{
			consultaTagMuestraNaturalezaPacienteStr="SELECT codigo, nombre from naturaleza_pacientes where nombre!='Embarazada'";
		}
		PreparedStatementDecorator consultaTagMuestraNaturalezaPacienteStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraNaturalezaPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraNaturalezaPacienteStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca la naturaleza de un
	 * paciente, que maneja el código 1 en una BD genérica
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza1 (Connection con) throws SQLException
	{
		String consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr="select codigo, nombre from naturaleza_pacientes where codigo=1";
		PreparedStatementDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca la naturaleza de un
	 * paciente, que maneja el código 0 en una BD genérica
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Naturaleza1 (con) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Naturaleza0 (Connection con) throws SQLException
	{
		String consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr="select codigo, nombre from naturaleza_pacientes where codigo=0";
		PreparedStatementDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca la naturaleza de un
	 * paciente, que maneja el código 3 en una BD genérica
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Mayores65 (Connection con) throws SQLException
	{
		String consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr="select codigo, nombre from naturaleza_pacientes where codigo=3";
		PreparedStatementDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca la naturaleza de un
	 * paciente, que maneja el código 2 en una BD genérica
	 * (TagMuestraNaturalezaPaciente)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraNaturalezaPaciente_Bebe (Connection con) throws SQLException
	{
		String consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr="select codigo, nombre from naturaleza_pacientes where codigo=2";
		PreparedStatementDecorator consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraNaturalezaPaciente_PosiblesNaturalezasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca los examenes físicos dadas las
	 * especialidades de la valoración en una BD genérica
	 * (TagMuestraOpcionesExFisicosStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraOpcionesExFisicosStruts (Connection , String ,String ,String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraOpcionesExFisicosStruts (Connection con, String codigoEspecialidad,String codigoEspecialidad2,String codigoEspecialidad3) throws SQLException
	{
		String consultaTagMuestraOpcionesExFisicosStrutsStr="";

		consultaTagMuestraOpcionesExFisicosStrutsStr = "SELECT efo.codigo AS codigoExam, efo.nombre AS nombreExam, tef.codigo, tef.nombre, tef.verdadero, tef.falso ";
		consultaTagMuestraOpcionesExFisicosStrutsStr += "FROM exam_fisicos_op efo, tipos_examenes_fisicos tef ";
		consultaTagMuestraOpcionesExFisicosStrutsStr += "WHERE (tef.codigo_especialidad IN ("+codigoEspecialidad+") ";

		if( !codigoEspecialidad2.equals("") )
			consultaTagMuestraOpcionesExFisicosStrutsStr += "OR tef.codigo_especialidad IN ("+codigoEspecialidad2+") ";

		if( !codigoEspecialidad3.equals("") )
			consultaTagMuestraOpcionesExFisicosStrutsStr += "OR tef.codigo_especialidad IN ("+codigoEspecialidad3+") )";
		else
			consultaTagMuestraOpcionesExFisicosStrutsStr += ") ";

		consultaTagMuestraOpcionesExFisicosStrutsStr += "AND tef.codigo = efo.codigo_tipo_examen ";
		consultaTagMuestraOpcionesExFisicosStrutsStr += "ORDER BY efo.codigo_tipo_examen ";

		PreparedStatementDecorator consultaTagMuestraOpcionesExFisicosStrutsStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraOpcionesExFisicosStrutsStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraOpcionesExFisicosStrutsStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca personas en el sistema
	 * dado su tipo en una BD genérica
	 * (TagMuestraPersonasBasicasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasBasicasStruts (Connection , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraPersonasBasicasStruts (Connection con, String tipoPersona, String codigoInstitucion) throws SQLException
	{
		final String consultaPacientes = "SELECT p.numero_identificacion  AS 	numeroIdentificacionPersona, p.tipo_identificacion AS codigoTipoIdentificacionPersona, p.primer_nombre AS primerNombrePersona,	p.segundo_nombre AS segundoNombrePersona, p.primer_apellido AS primerApellidoPersona,	p.segundo_apellido AS segundoApellidoPersona FROM personas p, pacientes pa, pacientes_instituciones pacins WHERE p.codigo= pa.codigo_paciente AND pa.codigo_paciente= pacins.codigo_paciente AND pacins.codigo_institucion = " + codigoInstitucion + " ORDER BY p.primer_apellido, p.segundo_apellido, p.primer_nombre, p.segundo_nombre ";

		final String consultaMedicos = "SELECT p.numero_identificacion AS numeroIdentificacionPersona, p.tipo_identificacion AS codigoTipoIdentificacionPersona, p.primer_nombre AS primerNombrePersona, p.segundo_nombre AS segundoNombrePersona, p.primer_apellido AS primerApellidoPersona, p.segundo_apellido AS segundoApellidoPersona " +
			"FROM personas p, medicos m, medicos_instituciones medins, ocupaciones_medicas cat " +
			"WHERE p.codigo = m.codigo_medico " +
			"AND p.codigo = medins.codigo_medico " +
			"AND medins.codigo_institucion = " + codigoInstitucion + "" +
			"AND cat.codigo = m.ocupacion_medica "+
			"AND cat.nombre like '%Medico%' "+
			"ORDER BY p.primer_apellido, p.segundo_apellido, p.primer_nombre, p.segundo_nombre";
		final String consultaUsuarios = "SELECT p.numero_identificacion AS numeroIdentificacionPersona, p.tipo_identificacion AS codigoTipoIdentificacionPersona, p.primer_nombre AS primerNombrePersona, p.segundo_nombre AS segundoNombrePersona, p.primer_apellido AS primerApellidoPersona, p.segundo_apellido AS segundoApellidoPersona, u.login AS loginUsuario FROM personas p, usuarios u WHERE p.codigo = u.codigo_persona AND u.institucion = " + codigoInstitucion + " ORDER BY p.primer_apellido, p.segundo_apellido, p.primer_nombre, p.segundo_nombre ";
		final String consultaAcompanantes = "SELECT numero_identificacion AS numeroIdentificacionPersona, tipo_identificacion AS codigoTipoIdentificacionPersona, nombre AS nombreCompletoPersona FROM acompanantes ORDER BY nombre ";
		final String consultaResponsables = "SELECT numero_identificacion AS numeroIdentificacionPersona, tipo_identificacion AS codigoTipoIdentificacionPersona, nombre AS nombreCompletoPersona FROM deudorco ORDER BY nombre ";

		String consultaTagMuestraPersonasBasicasStrutsStr = new String();

		if (tipoPersona.equals("paciente")) {
			consultaTagMuestraPersonasBasicasStrutsStr = consultaPacientes;
		}
		else if (tipoPersona.equals("medico")) {
			consultaTagMuestraPersonasBasicasStrutsStr = consultaMedicos;
		}
		else if (tipoPersona.equals("usuario")) {
			consultaTagMuestraPersonasBasicasStrutsStr = consultaUsuarios;
		}
		else if (tipoPersona.equals("acompanante")) {
			consultaTagMuestraPersonasBasicasStrutsStr = consultaAcompanantes;
		}
		else if (tipoPersona.equals("responsable")) {
			consultaTagMuestraPersonasBasicasStrutsStr = consultaResponsables;
		}
		else
		{
			throw new SQLException("Valor Desconocido en tipoPersona : " + tipoPersona);
		}

		PreparedStatementDecorator consultaTagMuestraPersonasBasicasStrutsStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraPersonasBasicasStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraPersonasBasicasStrutsStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca personas en el sistema
	 * dado su tipo y la acción a realizar (dados en un mismo parámetro)
	 * en una BD genérica
	 * (TagMuestraPersonasNombreApellidoStruts)
	 * @param x 
	 * @param mostrarSoloActivos 
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasNombreApellidoStruts (Connection , String , String ) throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector<String> consultaTagMuestraPersonasNombreApellidoStruts (Connection con, String tipoPersonaBuscada, String codigoInstitucion,String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos, String separador) throws SQLException
	{
		/*
		 * En este caso definimos (en el caso del médico) si queremos o no
		 * que salgan todos los médicos o todos los profesionales de la salud,
		 * si este parametro está en false solo muestra los médicos, si esta en
		 * true o no lo pasa el usuario, muestra a todos los profesionales
		 */
		Vector<String> resultados = new Vector (10,5);
		
		PreparedStatementDecorator consultaTagMuestraPersonasNombreApellidoStrutsStatement = null;
		ResultSetDecorator rs = null;
		
		String consultaTagMuestraPersonasNombreApellidoStrutsStr = null;
		String s = null;
		
		try {
			if (tipoPersonaBuscada.equals("MostrarSoloMedicos")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||
					tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea") || tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
			{
				String restMedicosUrgencias="";
				String restMedicosConsultaExterna="";

				consultaTagMuestraPersonasNombreApellidoStrutsStr =
					"SELECT DISTINCT "	+	"per.primer_apellido,"			+
					"per.segundo_apellido,"					+
					"per.primer_nombre,"					+
					"per.segundo_nombre,"					+
					"per.tipo_identificacion,"				+
					"per.numero_identificacion,"			+
					"med.codigo_medico "					+
					"FROM "		+	"administracion.personas "				+ "per,"	+
					"administracion.medicos_instituciones "	+ "medins, "+
					"administracion.medicos "				+ "med ";

				if (tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas")){
					consultaTagMuestraPersonasNombreApellidoStrutsStr+=
						"INNER JOIN consultaexterna.agenda age ON (age.codigo_medico=med.codigo_medico) " +
						"INNER JOIN consultaexterna.cita cit ON (cit.codigo_agenda=age.codigo and to_char(cit.fecha_gen, 'YYYY-MM-DD')>=to_char(current_date, 'YYYY-MM-DD')) ";
				}


				consultaTagMuestraPersonasNombreApellidoStrutsStr+=
					"WHERE "	+	"per.codigo"				+ "=med.codigo_medico"		+	" AND "	+
					"med.codigo_medico"			+ "=medins.codigo_medico"	+	" AND "	+
					restMedicosUrgencias +
					restMedicosConsultaExterna +
					"medins.codigo_institucion"	+ "=" + codigoInstitucion + " ";

				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) ";
				}

				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||
						tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea"))
				{
					///opción usada para mostrar sólo médicos activos en la admision hospitalaria
					//teniendo en cuento al parámetro general de ocupación médica
					//OJO: si se piensa reutilizar esta opción se debe validar que los parámetros 
					// ocupacion médica general y especialista deben estar definidos
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) and " +
						"med.ocupacion_medica IN("+ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(Integer.parseInt(codigoInstitucion),true)+","+ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(Integer.parseInt(codigoInstitucion),true)+") ";
				}
				else if(!tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas")){
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.ocupacion_medica IN("+ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(Integer.parseInt(codigoInstitucion),true)+","+ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(Integer.parseInt(codigoInstitucion),true)+") ";
				}
				//Se filtran médicos por el area de sus usuarios
				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea"))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr += " AND med.codigo_medico IN (SELECT u.codigo_persona FROM administracion.usuarios u INNER JOIN administracion.centros_costo_usuario cu ON(cu.usuario=u.login) WHERE cu.centro_costo = "+codigoCentroCosto+") ";
				}

				consultaTagMuestraPersonasNombreApellidoStrutsStr +=
					"ORDER BY "	+	"per.primer_apellido,"		+
					"per.segundo_apellido,"		+
					"per.primer_nombre,"		+
					"per.segundo_nombre";
			}
			/**Adicion Carlos. Esta opcion muestra todos los prefionales de la salud.
		Validacion necesaria para el modulo de consulta externa**/
			else if(tipoPersonaBuscada.equals("TodosProfesionalesSalud"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr =" SELECT per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre,per.tipo_identificacion,per.numero_identificacion, med.codigo_medico " +
				" FROM administracion.personas per, administracion.medicos  med, administracion.medicos_instituciones medins " +
				" WHERE per.codigo=med.codigo_medico AND med.codigo_medico=medins.codigo_medico AND medins.codigo_institucion=" + codigoInstitucion + 
				" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if(tipoPersonaBuscada.equals("TodosProfesionalesSaludActivos"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr =" SELECT per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre,per.tipo_identificacion,per.numero_identificacion, med.codigo_medico " +
				" FROM administracion.personas per, administracion.medicos  med, administracion.medicos_instituciones medins " +
				" WHERE per.codigo=med.codigo_medico AND med.codigo_medico=medins.codigo_medico AND medins.codigo_institucion=" + codigoInstitucion +
				" AND med.codigo_medico NOT IN (SELECT mi.codigo_medico FROM administracion.medicos_inactivos mi WHERE mi.codigo_institucion=" + codigoInstitucion +")"+
				" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if (tipoPersonaBuscada.equals("MostrarAcompanantes") )
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido, acomp.tipo_identificacion, acomp.numero_identificacion from administracion.personas per, acompanantes_pacientes acomp where acomp.codigo_paciente=per.codigo order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarMedicos") )
			{
				//Por anexo 958 se agrega validacion de que no se muestren medicos con indicativo interfaz=N
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo, med.indicativo_interfaz  from administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico  and medins.codigo_institucion=" + codigoInstitucion +" order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if (tipoPersonaBuscada.equals("MostrarMedicosDesactivacion"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select " +
				"per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, " +
				"per.numero_identificacion, per.codigo  " +
				"from administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins " +
				"where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico  and " +
				"medins.codigo_institucion=" + codigoInstitucion + " and " +
				"med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) " +
				"order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}

			//La única diferencia entre MostrarMedicos y MostrarMedicosActivación
			//es que esta última muestra los médicos que estan desactivados
			else if( tipoPersonaBuscada.equals("MostrarMedicosActivacion") )
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo  from administracion.personas per, administracion.medicos med, administracion.medicos_inactivos medins where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarPacientes"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo from administracion.personas per, manejopaciente.pacientes pac, manejopaciente.pacientes_instituciones pacins where pac.codigo_paciente=per.codigo and pac.codigo_paciente=pacins.codigo_paciente  and pacins.codigo_institucion=" + codigoInstitucion + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarUsuarios") || tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, us.login, per.codigo,us.activo as activo from administracion.usuarios us, administracion.personas per where us.codigo_persona=per.codigo and us.institucion=" + codigoInstitucion;
				if(!mostrarSoloActivos.isEmpty()&&mostrarSoloActivos.equals(ConstantesBD.acronimoSi))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" and us.activo='"+ConstantesBD.acronimoSi+"'";
				}
				if(!mostrarSoloInactivos.isEmpty()&&mostrarSoloInactivos.equals(ConstantesBD.acronimoSi))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" and us.activo='"+ConstantesBD.acronimoNo+"'";
				}
				consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarUsuariosActivacion") ||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, us.login, per.codigo from administracion.usuarios us, administracion.personas per where us.codigo_persona=per.codigo  and us.institucion=0 ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre ";
			}
			else
			{
				throw new SQLException ("Tipo de persona seleccionado en  no es válido");
			}

			logger.info("\n\n\n consultaTagMuestraPersonasNombreApellidoStrutsStr-------> "+consultaTagMuestraPersonasNombreApellidoStrutsStr);
			consultaTagMuestraPersonasNombreApellidoStrutsStatement = new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraPersonasNombreApellidoStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			rs = new ResultSetDecorator(consultaTagMuestraPersonasNombreApellidoStrutsStatement.executeQuery());
			//SI SE ESTA BUSCANDO UN USUARIO, RETORNA UN STRING CON LOGIN-NOMBRE
			//SI ES OTRA PERSONA DEVUELVE CODTIPOID-NUMID-NOMBRE

			while (rs.next())
			{

				if (tipoPersonaBuscada.equals("MostrarUsuariosActivacion")|| tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
				{
					s=rs.getString("login")  +  separador + rs.getString("primer_nombre") + " " + rs.getString("primer_apellido")+  separador+ rs.getString("codigo");
				}
				else if(tipoPersonaBuscada.equals("MostrarSoloMedicos")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
				{
					s = rs.getString("tipo_identificacion") + separador + rs.getString("numero_identificacion") +  separador +rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + separador + rs.getString("codigo_medico");					
				}
				/**Adicion Carlos. Esta opcion muestra todos los prefionales de la salud.
			Validacion necesaria para el modulo de consulta externa**/
				else if(tipoPersonaBuscada.equals("TodosProfesionalesSalud") || tipoPersonaBuscada.equals("TodosProfesionalesSaludActivos"))
				{
					s = rs.getString("tipo_identificacion") + separador + rs.getString("numero_identificacion") +  separador +rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + separador + rs.getString("codigo_medico");					
				}
				else if(tipoPersonaBuscada.equals("MostrarUsuarios"))
				{
					s=rs.getString("login")  +  separador + rs.getString("primer_nombre") + separador + rs.getString("primer_apellido")+  separador+ rs.getString("codigo")+  separador;

					String activo = rs.getString("activo");

					if (!UtilidadTexto.isEmpty(activo)) {
						s+=activo;
					}else{
						s+= " ";
					}


				}
				else if(tipoPersonaBuscada.equals("MostrarMedicos"))
				{
					s = rs.getString("tipo_identificacion") + separador + rs.getString("numero_identificacion") +  separador +rs.getString("primer_apellido") + separador + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) + separador + rs.getString("primer_nombre") + separador + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + separador + rs.getString("codigo")+ separador + rs.getString("indicativo_interfaz");					
				}
				else 
				{
					s =rs.getString("tipo_identificacion") + separador + rs.getString("numero_identificacion")  +  separador +rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre")) + " " + rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido")) ;
				}

				resultados.add(s);
			}
		} catch (Exception e) {
			Log4JManager.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Consultado TagMuestraPersonasNombreApellidoStruts <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<: " + e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(consultaTagMuestraPersonasNombreApellidoStrutsStatement != null) {
					consultaTagMuestraPersonasNombreApellidoStrutsStatement.close();
				}
			} catch (Exception e2) {
				Log4JManager.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Cerrando PreparedStatement - Resulset <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<: " + e2);
			}
		}
		return resultados;
	}

	/**
	 * Implementación del método que busca personas en el sistema
	 * dado su tipo y la acción a realizar (dados en un mismo parámetro)
	 * en una BD genérica
	 * (TagMuestraPersonasNombreApellidoStruts)
	 * @param x 
	 * @param mostrarSoloActivos 
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPersonasNombreApellidoStruts (Connection , String , String ) throws SQLException
	 */
	public static String consultaTagMuestraPersonasNombreApellido (Connection con, String tipoPersonaBuscada, String codigoInstitucion,String codigoCentroCosto, String mostrarSoloActivos, String mostrarSoloInactivos) throws SQLException
	{
		/*
		 * En este caso definimos (en el caso del médico) si queremos o no
		 * que salgan todos los médicos o todos los profesionales de la salud,
		 * si este parametro está en false solo muestra los médicos, si esta en
		 * true o no lo pasa el usuario, muestra a todos los profesionales
		 */
		
		PreparedStatementDecorator consultaTagMuestraPersonasNombreApellidoStrutsStatement = null;
		ResultSetDecorator rs = null;
		
		String consultaTagMuestraPersonasNombreApellidoStrutsStr = null;
		String s = null;
		
		try {
			if (tipoPersonaBuscada.equals("MostrarSoloMedicos")||tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||
					tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea") || tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
			{
				String restMedicosUrgencias="";
				String restMedicosConsultaExterna="";

				consultaTagMuestraPersonasNombreApellidoStrutsStr =
					"SELECT DISTINCT "	+	"per.primer_apellido,"			+
					"per.segundo_apellido,"					+
					"per.primer_nombre,"					+
					"per.segundo_nombre,"					+
					"per.tipo_identificacion,"				+
					"per.numero_identificacion,"			+
					"med.codigo_medico "					+
					"FROM "		+	"administracion.personas "				+ "per,"	+
					"administracion.medicos_instituciones "	+ "medins, "+
					"administracion.medicos "				+ "med ";

				if (tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas")){
					consultaTagMuestraPersonasNombreApellidoStrutsStr+=
						"INNER JOIN consultaexterna.agenda age ON (age.codigo_medico=med.codigo_medico) " +
						"INNER JOIN consultaexterna.cita cit ON (cit.codigo_agenda=age.codigo and to_char(cit.fecha_gen, 'YYYY-MM-DD')>=to_char(current_date, 'YYYY-MM-DD')) ";
				}


				consultaTagMuestraPersonasNombreApellidoStrutsStr+=
					"WHERE "	+	"per.codigo"				+ "=med.codigo_medico"		+	" AND "	+
					"med.codigo_medico"			+ "=medins.codigo_medico"	+	" AND "	+
					restMedicosUrgencias +
					restMedicosConsultaExterna +
					"medins.codigo_institucion"	+ "=" + codigoInstitucion + " ";

				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas"))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) ";
				}

				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacion")||
						tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea"))
				{
					///opción usada para mostrar sólo médicos activos en la admision hospitalaria
					//teniendo en cuento al parámetro general de ocupación médica
					//OJO: si se piensa reutilizar esta opción se debe validar que los parámetros 
					// ocupacion médica general y especialista deben estar definidos
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) and " +
						"med.ocupacion_medica IN("+ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(Integer.parseInt(codigoInstitucion),true)+","+ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(Integer.parseInt(codigoInstitucion),true)+") ";
				}
				else if(!tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionConCitas")){
					consultaTagMuestraPersonasNombreApellidoStrutsStr +=
						" AND med.ocupacion_medica IN("+ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(Integer.parseInt(codigoInstitucion),true)+","+ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(Integer.parseInt(codigoInstitucion),true)+") ";
				}
				//Se filtran médicos por el area de sus usuarios
				if(tipoPersonaBuscada.equals("MostrarMedicosDesactivacionOcupacionArea"))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr += " AND med.codigo_medico IN (SELECT u.codigo_persona FROM administracion.usuarios u INNER JOIN administracion.centros_costo_usuario cu ON(cu.usuario=u.login) WHERE cu.centro_costo = "+codigoCentroCosto+") ";
				}

				consultaTagMuestraPersonasNombreApellidoStrutsStr +=
					"ORDER BY "	+	"per.primer_apellido,"		+
					"per.segundo_apellido,"		+
					"per.primer_nombre,"		+
					"per.segundo_nombre";
			}
			/**Adicion Carlos. Esta opcion muestra todos los prefionales de la salud.
		Validacion necesaria para el modulo de consulta externa**/
			else if(tipoPersonaBuscada.equals("TodosProfesionalesSalud"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr =" SELECT per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre,per.tipo_identificacion,per.numero_identificacion, med.codigo_medico " +
				" FROM administracion.personas per, administracion.medicos  med, administracion.medicos_instituciones medins " +
				" WHERE per.codigo=med.codigo_medico AND med.codigo_medico=medins.codigo_medico AND medins.codigo_institucion=" + codigoInstitucion + 
				" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if(tipoPersonaBuscada.equals("TodosProfesionalesSaludActivos"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr =" SELECT per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre,per.tipo_identificacion,per.numero_identificacion, med.codigo_medico " +
				" FROM administracion.personas per, administracion.medicos  med, administracion.medicos_instituciones medins " +
				" WHERE per.codigo=med.codigo_medico AND med.codigo_medico=medins.codigo_medico AND medins.codigo_institucion=" + codigoInstitucion +
				" AND med.codigo_medico NOT IN (SELECT mi.codigo_medico FROM administracion.medicos_inactivos mi WHERE mi.codigo_institucion=" + codigoInstitucion +")"+
				" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if (tipoPersonaBuscada.equals("MostrarAcompanantes") )
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido, acomp.tipo_identificacion, acomp.numero_identificacion from administracion.personas per, acompanantes_pacientes acomp where acomp.codigo_paciente=per.codigo order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarMedicos") )
			{
				//Por anexo 958 se agrega validacion de que no se muestren medicos con indicativo interfaz=N
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo, med.indicativo_interfaz  from administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico  and medins.codigo_institucion=" + codigoInstitucion +" order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if (tipoPersonaBuscada.equals("MostrarMedicosDesactivacion"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select " +
				"per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, " +
				"per.numero_identificacion, per.codigo  " +
				"from administracion.personas per, administracion.medicos med, administracion.medicos_instituciones medins " +
				"where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico  and " +
				"medins.codigo_institucion=" + codigoInstitucion + " and " +
				"med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT codigo_medico || '-' || codigo_institucion from administracion.medicos_inactivos) " +
				"order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}

			//La única diferencia entre MostrarMedicos y MostrarMedicosActivación
			//es que esta última muestra los médicos que estan desactivados
			else if( tipoPersonaBuscada.equals("MostrarMedicosActivacion") )
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo  from administracion.personas per, administracion.medicos med, administracion.medicos_inactivos medins where per.codigo=med.codigo_medico and med.codigo_medico=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarPacientes"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, per.tipo_identificacion, per.numero_identificacion, per.codigo from administracion.personas per, manejopaciente.pacientes pac, manejopaciente.pacientes_instituciones pacins where pac.codigo_paciente=per.codigo and pac.codigo_paciente=pacins.codigo_paciente  and pacins.codigo_institucion=" + codigoInstitucion + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarUsuarios") || tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, us.login, per.codigo,us.activo as activo from administracion.usuarios us, administracion.personas per where us.codigo_persona=per.codigo and us.institucion=" + codigoInstitucion;
				if(!mostrarSoloActivos.isEmpty()&&mostrarSoloActivos.equals(ConstantesBD.acronimoSi))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" and us.activo='"+ConstantesBD.acronimoSi+"'";
				}
				if(!mostrarSoloInactivos.isEmpty()&&mostrarSoloInactivos.equals(ConstantesBD.acronimoSi))
				{
					consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" and us.activo='"+ConstantesBD.acronimoNo+"'";
				}
				consultaTagMuestraPersonasNombreApellidoStrutsStr=consultaTagMuestraPersonasNombreApellidoStrutsStr+" ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";
			}
			else if( tipoPersonaBuscada.equals("MostrarUsuariosActivacion") ||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
			{
				consultaTagMuestraPersonasNombreApellidoStrutsStr = "select per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre, us.login, per.codigo from administracion.usuarios us, administracion.personas per where us.codigo_persona=per.codigo  and us.institucion=0 ORDER BY per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre ";
			}
			else
			{
				throw new SQLException ("Tipo de persona seleccionado en  no es válido");
			}

			logger.info("\n\n\n consultaTagMuestraPersonasNombreApellidoStrutsStr-------> "+consultaTagMuestraPersonasNombreApellidoStrutsStr);
			consultaTagMuestraPersonasNombreApellidoStrutsStatement = new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraPersonasNombreApellidoStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			rs = new ResultSetDecorator(consultaTagMuestraPersonasNombreApellidoStrutsStatement.executeQuery());
			//SI SE ESTA BUSCANDO UN USUARIO, RETORNA UN STRING CON LOGIN-NOMBRE
			//SI ES OTRA PERSONA DEVUELVE CODTIPOID-NUMID-NOMBRE

			while (rs.next()) {
				if ( tipoPersonaBuscada.equals("MostrarUsuarios")|| tipoPersonaBuscada.equals("MostrarUsuariosActivacion")|| tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
					s += "<option value=\"" + rs.getString("login")  +  "\">" + rs.getString("primer_apellido") + " " + Encoder.encode(rs.getString("primer_nombre") +  "(" + rs.getString("login")) + ")</option>";
				else
					s += "<option value=\"" + rs.getString("codigo") +  "\">" + Encoder.encode(rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null ?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null ?"":rs.getString("segundo_nombre")) ) + "</option>";
			}
		} catch (Exception e) {
			Log4JManager.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Consultado TagMuestraPersonasNombreApellidoStruts <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<: " + e);
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if(consultaTagMuestraPersonasNombreApellidoStrutsStatement != null) {
					consultaTagMuestraPersonasNombreApellidoStrutsStatement.close();
				}
			} catch (Exception e2) {
				Log4JManager.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Cerrando PreparedStatement - Resulset <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<: " + e2);
			}
		}
		return s;
	}
	
	/**
	 * Implementación del método que busca los posibles centros de
	 * costo a los que se puede solicitar interconsulta en una BD genérica
	 * (TagMuestraPosiblesInterconsultas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraPosiblesInterconsultas (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraPosiblesInterconsultas (Connection con, int codigoCuenta) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraPosiblesInterconsultasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraPosiblesInterconsultasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraPosiblesInterconsultasStatement.setInt(1, codigoCuenta);
		return new ResultSetDecorator(consultaTagMuestraPosiblesInterconsultasStatement.executeQuery());
	}

	

	/**
	 * Implementación del método que busca los tipos de afiliado
	 * disponibles en el sistema en una BD genérica
	 * (TagMuestraTipoAfiliado)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraTipoAfiliado (Connection ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraTipoAfiliado (Connection con) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraTipoAfiliadoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraTipoAfiliadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraTipoAfiliadoStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca la cuenta de un
	 * ingreso especificando si se quiere la correspondiente a la de
	 * urgencias o a la de hospitalizacion en una BD genérica
	 * (TagMuestraLinkEvolucionesCuentaEnEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis (Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException
	{
		String consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStr="";
		if (esHospitalizacion)
		{
			consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStr="SELECT cue.id as idCuenta from cuentas cue, admisiones_hospi adh where cue.id=adh.cuenta and cue.id_ingreso=?";
		}
		else
		{
			consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStr="SELECT cue.id as idCuenta from cuentas cue, admisiones_urgencias adu where cue.id=adu.cuenta and cue.id_ingreso=?";
		}
		PreparedStatementDecorator consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStatement.setInt(1, idIngreso);
		return new ResultSetDecorator(consultaTagMuestraLinkEvolucionesCuentaEnEpicrisisStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca ciertos datos de las
	 * valoraciones hospitalarias que pueden ser asociadas a una
	 * evolución en una BD genérica
	 * (TagMuestraLinkEvolucionesCuentaEnEpicrisis)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalarias (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalariasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalariasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalariasStatement.setInt(1, idCuenta);
		return new ResultSetDecorator(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesHospitalariasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca ciertos datos de las
	 * valoraciones de urgencias que pueden ser asociadas a una
	 * evolución en una BD genérica
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgencias (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgenciasStatement.setInt(1, idCuenta);
		return new ResultSetDecorator(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesUrgenciasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca ciertos datos de las
	 * valoraciones de interconsulta que pueden ser asociadas a una
	 * evolución en una BD genérica
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStatement.setInt(1, idCuenta);
		consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStatement.setInt(2, idCuenta);
		consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStatement.setInt(3, idCuenta);
		logger.info("\n\n\n consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStr-->"+consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStr+" -->"+idCuenta);
		return new ResultSetDecorator(consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultasStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca ciertos datos de todas las
	 * valoraciones (hospitalización, interconsulta, urgencias) que pueden
	 * ser asociadas a una evolución en una BD genérica
	 * (TagMuestraAsocioEvolucionValoracion)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion_obtenerValoracionesInterconsultas (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones (Connection con, int idCuenta, int idCuentaAsociada) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement.setInt(1, idCuenta);
		if(idCuentaAsociada>0)
		{
			consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement.setInt(2, idCuentaAsociada);
		}
		else
		{
			consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement.setInt(2, idCuenta);
		}
		consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement.setInt(3, idCuenta);
		logger.info("\n\n consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoraciones-->"+consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStr+" -->idCuenta="+idCuenta+ " idCuentaAsociada-->"+idCuentaAsociada);
		return new ResultSetDecorator(consultaTagMuestraAsocioEvolucionValoracion_obtenerTodasValoracionesStatement.executeQuery());
	}

	/**
	 * Implementación del método que busca ciertos datos de una
	 * valoracion asociada a una evolución en una BD genérica
	 * (TagMuestraAsocioEvolucionValoracion2)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraAsocioEvolucionValoracion2 (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraAsocioEvolucionValoracion2 (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator consultaTagMuestraAsocioEvolucionValoracion2Statement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraAsocioEvolucionValoracion2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagMuestraAsocioEvolucionValoracion2Statement.setInt(1, numeroSolicitud);
		consultaTagMuestraAsocioEvolucionValoracion2Statement.setInt(2, numeroSolicitud);
		consultaTagMuestraAsocioEvolucionValoracion2Statement.setInt(3, numeroSolicitud);
		return new ResultSetDecorator(consultaTagMuestraAsocioEvolucionValoracion2Statement.executeQuery());
	}

	/**
	 * Implementación del método que proporciona la informacion de campos dinamicos
	 * para edicion
	 * (TagEditaCamposDinamicos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagEditaCamposDinamicos (Connection con, String idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	 */
	public static ResultSetDecorator consultaTagEditaCamposDinamicos (Connection con, int idMedico, String idFuncionalidad, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		String consultaTagEditaCamposDinamicosStr=
			" SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.codigo_medico="+idMedico+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceMedico+ 
		    " UNION ALL " +
			" SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.centro_costo="+codigoCentroCosto+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceCentroCosto+
		    " UNION ALL " +
			" SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.institucion="+codigoInstitucion+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceInstitucion+
			" ORDER BY seccion,orden";
		PreparedStatementDecorator consultaTagEditaCamposDinamicosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagEditaCamposDinamicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagEditaCamposDinamicosStatement.executeQuery());
	}

	/**
	 * Implementación del método que proporciona la informacion de campos dinamicos por seccion
	 * para edicion
	 * (TagEditaCamposDinamicosSeccion)
	 *
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagEditaCamposDinamicosSeccion (Connection con, String idMedico, String idFuncionalidad, String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	 */
	public static ResultSetDecorator consultaTagEditaCamposDinamicosSeccion (Connection con, int idMedico, String idFuncionalidad, String idSeccion, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		String consultaTagEditaCamposDinamicosSeccionStr=
		    " SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.codigo_medico="+idMedico+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.seccion="+idSeccion+" AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceMedico+ 
		    " UNION ALL " +
		    " SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.centro_costo="+codigoCentroCosto+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.seccion="+idSeccion+" AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceCentroCosto+ 
		    " UNION ALL " +
		    " SELECT pa.tipo, pa.orden , pa.nombre, pa.seccion from  param_asociadas pa, seccion_parametriza sp where pa.institucion="+codigoInstitucion+" AND pa.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " AND pa.seccion=sp.codigo AND sp.funcionalidad="+idFuncionalidad+ " AND pa.seccion="+idSeccion+" AND pa.alcance_campo=" + ConstantesBD.campoParametrizableAlcanceInstitucion+ 
			" ORDER BY seccion,orden";
		
		PreparedStatementDecorator consultaTagEditaCamposDinamicosSeccionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagEditaCamposDinamicosSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagEditaCamposDinamicosSeccionStatement.executeQuery());
	}

	/**
	 * Implementación del método que proporciona la informacion de campos dinamicos
	 * para edicion
	 * (TagMuestraCamposDinamicos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamposDinamicos (Connection con, String idMedico, String idFuncionalidad) throws SQLException;
	 */
	public static ResultSetDecorator consultaTagMuestraCamposDinamicos (Connection con, String idFuncionalidad) throws SQLException
	{
		String consultaTagMuestraCamposDinamicosStr=
		    " SELECT pa.nombre as nombre, ip.valor as info, pa.tipo as tipo, pa.seccion as seccion from info_parametrizada ip, param_asociadas pa where ip.codigo_tabla=" +idFuncionalidad+"AND ip.parametrizacion_asociada=pa.codigo " +
		    " order by pa.seccion, pa.orden";
		PreparedStatementDecorator consultaTagMuestraCamposDinamicosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraCamposDinamicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraCamposDinamicosStatement.executeQuery());
	}

	/**
	 * Implementación del método que proporciona la informacion de campos dinamicos por seccion
	 * para edicion
	 * (TagMuestraCamposDinamicosSeccion)
	 *
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagMuestraCamposDinamicosSeccion (Connection con, String idMedico, String idFuncionalidad, String idSeccion) throws SQLException;
	 */

	public static ResultSetDecorator consultaTagMuestraCamposDinamicosSeccion (Connection con, String idFuncionalidad,String idSeccion) throws SQLException
	{
		String consultaTagMuestraCamposDinamicosSeccionStr="select pa.nombre as nombre, ip.valor as info, pa.tipo as tipo, pa.seccion as seccion from info_parametrizada ip, param_asociadas pa where ip.codigo_tabla=" +idFuncionalidad+" AND ip.parametrizacion_asociada=pa.codigo AND pa.seccion="+idSeccion+" order  by pa.seccion, pa.orden";
		PreparedStatementDecorator consultaTagMuestraCamposDinamicosSeccionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraCamposDinamicosSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagMuestraCamposDinamicosSeccionStatement.executeQuery());
	}

	/**
	 * Implementación del método busca personas
	 * en una BD genérica
	 * (TagBusquedaPersonasStruts)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonasStruts (Connection , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagBusquedaPersonasStruts (
		Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, 
		String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, 
		String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, 
		String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, 
		String codigoInstitucion,String numeroHistoriaClinica, String codigoPais, String codigoPaisIdentificacion,String fichaUsuarioCapitado,String accion,String numeroIngreso) throws SQLException
	{
		Vector pseudoExpRegular;
		String  textoCompletoExpRegular;
		int j;
		String consultaTagBusquedaPersonasStrutsStr="";
		String consultaTagBusquedaCapitadosStr = "";
		String where=" where pacins.codigo_institucion="+ codigoInstitucion;
		
		if(tipoPersonaBuscada.equals("paciente") ||tipoPersonaBuscada.equals("pacienteEncabezado") )
		{
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, " +
					" coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, " +
					" coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, " +
					" per.tipo_identificacion as tipoIdentificacionPersona," +
					" getnombretipoidentificacion(per.tipo_identificacion) AS nombreTipoIdPersona," +
					" '"+ConstantesBD.acronimoNo+"' as capitado" +
					" from personas per" +
					" inner join pacientes pac on (pac.codigo_paciente=per.codigo) " +
					" inner join pacientes_instituciones pacins on (pacins.codigo_paciente=pac.codigo_paciente) " ;
			

			if (UtilidadCadena.noEsVacio(numeroIngreso))
				consultaTagBusquedaPersonasStrutsStr+=" inner join ingresos i on (i.codigo_paciente=per.codigo) ";
			
								
			//Busqueda de capitado solo si la accion viene del ingreso del paciente o reserva de cita y se han parametrizado ciertos campos
			if((accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))&&
				(!fichaUsuarioCapitado.equals("")||
					!codigoTipoIdentificacion.equals("")||
					!numeroIdentificacion.equals("")||
					!primerApellidoPersona.equals("")||
					!segundoApellidoPersona.equals("")||
					!primerNombrePersona.equals("")||
					!segundoNombrePersona.equals("")||
					!fechaNacimiento.equals("")||
					!direccion.equals("")
				)
			)
			{
				consultaTagBusquedaCapitadosStr = "SELECT "+ 
					"0 AS codigo, "+
					"uc.primer_apellido AS primerApellidoPersona, "+
					"coalesce(uc.segundo_apellido,'') AS segundoApellidoPersona, "+
					"uc.primer_nombre AS primerNombrePersona, "+
					"coalesce(uc.segundo_nombre,'') AS segundoNombrePersona, "+
					"uc.numero_identificacion AS numeroIdentificacionPersona, "+
					"uc.tipo_identificacion AS tipoIdentificacionPersona," +
					"getnombretipoidentificacion(uc.tipo_identificacion) AS nombreTipoIdPersona, "+
					"'"+ConstantesBD.acronimoSi+"' as capitado "+ 
					"FROM usuarios_capitados uc ";
					
					/*if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(Utilidades.convertirAEntero(codigoInstitucion)).equals(ConstantesBD.acronimoSi))
						consultaTagBusquedaCapitadosStr += " INNER JOIN conv_usuarios_capitados conv IN (conv.usuario_capitado = uc.codigo AND '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' BETWEEN conv.fecha_inicial AND conv.fecha_final ) ";*/						
										
					consultaTagBusquedaCapitadosStr += "WHERE";
			}
		}
		else if(tipoPersonaBuscada.equals("usuario") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona, us.login as login from personas per, usuarios us where per.codigo=us.codigo_persona and institucion=" + codigoInstitucion ;
		else if(tipoPersonaBuscada.equals("medico") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona FROM personas per, medicos med, medicos_instituciones medins where per.codigo=med.codigo_medico and per.codigo=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion;
		else if (tipoPersonaBuscada.equals("usuarioInactivo") )
			consultaTagBusquedaPersonasStrutsStr = "SELECT per.codigo as codigo, per.primer_apellido as primerApellidoPersona, coalesce(per.segundo_apellido,'') as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, coalesce(per.segundo_nombre,'') as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona, us.login as login from usuarios us, personas per where us.codigo_persona=per.codigo and us.institucion="+codigoInstitucion;
		else
			throw new SQLException("Tipo de Personas invalido");

		//Ahora empezamos a colgarle arandelas a la consulta,
		//dependiendo de las opciones que nos hayan dado

		//se añade el where a la cadena de consulta
		consultaTagBusquedaPersonasStrutsStr+=where;
		
		if (criteriosBusqueda != null) 
		{
			for (int i = 0; i < criteriosBusqueda.length; i++) 
			{
				if(criteriosBusqueda[i].equals("numeroHistoriaClinica"))
				{
					consultaTagBusquedaPersonasStrutsStr += " pac.historia_clinica = '"+numeroHistoriaClinica+"' ";
				}
				if (criteriosBusqueda[i].equals("fichaUsuarioCapitado"))
				{
					consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.numero_ficha = '"+fichaUsuarioCapitado+"'";
				}
				
				else if (criteriosBusqueda[i].equals("numeroIdentificacion")&&numeroIdentificacion != null && !numeroIdentificacion.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.numero_identificacion = '" + numeroIdentificacion + "'";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.numero_identificacion = '"+numeroIdentificacion+"'";
				
				}
				else if (criteriosBusqueda[i].equals("tipoIdentificacion")&&!codigoTipoIdentificacion.equals("")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.tipo_identificacion='" + codigoTipoIdentificacion + "'";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.tipo_identificacion = '"+codigoTipoIdentificacion+"'";
				}
				else if (criteriosBusqueda[i].equals("primerNombrePersona")&&primerNombrePersona != null && !primerNombrePersona.equals("")) 
				{
					
					pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(primerNombrePersona);
					textoCompletoExpRegular=" and (";

					for (j=0;j<pseudoExpRegular.size();j++)
					{
						if (j!=0)
						{
							textoCompletoExpRegular=textoCompletoExpRegular+" or ";
						}
						textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.primer_nombre) like '%" +pseudoExpRegular.get(j) + "%' ";
						
						//Adicion  para la busqueda desde el encabezado del paciente
						if(tipoPersonaBuscada.equals("pacienteEncabezado"))
							textoCompletoExpRegular += " OR UPPER(per.segundo_nombre) like '%" +pseudoExpRegular.get(j) + "%' ";
					}
					
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + textoCompletoExpRegular + ")";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.primer_nombre) LIKE UPPER('%" + primerNombrePersona + "%')) ";
					
				}
				else if (criteriosBusqueda[i].equals("segundoNombrePersona")&&segundoNombrePersona != null && !segundoNombrePersona.equals("")) 
				{
					pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(segundoNombrePersona );
					textoCompletoExpRegular=" and (";

					for (j=0;j<pseudoExpRegular.size();j++)
					{
						if (j!=0)
						{
							textoCompletoExpRegular=textoCompletoExpRegular+" or ";
						}
						textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.segundo_nombre) like '%" +pseudoExpRegular.get(j) + "%' ";
						
						
					}
					
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + textoCompletoExpRegular + ")";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.segundo_nombre) LIKE UPPER('%" + segundoNombrePersona + "%')) ";
					
				}
				else if (criteriosBusqueda[i].equals("primerApellidoPersona") && primerApellidoPersona != null && !primerApellidoPersona.equals("")) 
				{
					pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(primerApellidoPersona );
					textoCompletoExpRegular=" and (";

					for (j=0;j<pseudoExpRegular.size();j++)
					{
						if (j!=0)
						{
							textoCompletoExpRegular=textoCompletoExpRegular+" or ";
						}
						textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.primer_apellido) like '%" +pseudoExpRegular.get(j) + "%' ";
						//Adicion  para la busqueda desde el encabezado del paciente
						if(tipoPersonaBuscada.equals("pacienteEncabezado"))
							textoCompletoExpRegular += " OR UPPER(per.segundo_apellido) like '%" +pseudoExpRegular.get(j) + "%' ";
					}
					
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + textoCompletoExpRegular + ")";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.primer_apellido) LIKE UPPER('%" + primerApellidoPersona + "%')) ";
					
				}
				else if (criteriosBusqueda[i].equals("segundoApellidoPersona") && segundoApellidoPersona != null && !segundoApellidoPersona.equals("")) 
				{
					pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(segundoApellidoPersona );
					textoCompletoExpRegular=" and (";

					for (j=0;j<pseudoExpRegular.size();j++)
					{
						if (j!=0)
						{
							textoCompletoExpRegular=textoCompletoExpRegular+" or ";
						}
						textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.segundo_apellido) like '%" +pseudoExpRegular.get(j) + "%' "; 
					}
					
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + textoCompletoExpRegular + ")";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "( UPPER(uc.segundo_apellido) LIKE UPPER('%" + segundoApellidoPersona + "%')) ";
					
				}
				else if (criteriosBusqueda[i].equals("codigoPaisIdentificacion"))
				{
					consultaTagBusquedaPersonasStrutsStr += " and per.codigo_pais_nacimiento = '"+codigoPaisIdentificacion+"' ";
				}
				else if (criteriosBusqueda[i].equals("ciudadIdentificacion")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.codigo_departamento_nacimiento='" + codigoDepartamentoIdentificacion + "' and codigo_ciudad_nacimiento='" + codigoCiudadIdentificacion+"' ";
				}
				else if (criteriosBusqueda[i].equals("fechaNacimiento")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.fecha_nacimiento='" + fechaNacimiento + "'";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "uc.fecha_nacimiento='" + fechaNacimiento + "' ";
				}
				else if (criteriosBusqueda[i].equals("direccion")&&direccion != null && !direccion.equals("")) 
				{
					pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(direccion);
					textoCompletoExpRegular=" and (";

					for (j=0;j<pseudoExpRegular.size();j++)
					{
						if (j!=0)
						{
							textoCompletoExpRegular=textoCompletoExpRegular+" or ";
						}
						textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.direccion) like '%" +pseudoExpRegular.get(j) + "%' "; 
					}
					
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + textoCompletoExpRegular + ")";
					
					//Busqueda de capitado
					if(accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))
						consultaTagBusquedaCapitadosStr += (consultaTagBusquedaCapitadosStr.endsWith("WHERE")?" ":" AND ") + "(UPPER(uc.direccion) LIKE UPPER('%" + direccion + "%'))";
					
				}
				else if (criteriosBusqueda[i].equals("codigoPaisVivienda"))
				{
					consultaTagBusquedaPersonasStrutsStr += " and per.codigo_pais_vivienda = '"+codigoPais+"' ";
				}
				else if (criteriosBusqueda[i].equals("ciudadVivienda")) 
				{
					consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and per.codigo_departamento_vivienda= '" + codigoDepartamento + "' and codigo_ciudad_vivienda='" + codigoCiudad+"' ";
				}
				else if (criteriosBusqueda[i].equals("telefono")) {
					if (telefono != null && !telefono.equals(""))
					{
						consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.telefono) like UPPER('%" + telefono + "%')";
					}
				}
				else if (criteriosBusqueda[i].equals("email")) {
					if (email != null && !email.equals(""))
					{
						consultaTagBusquedaPersonasStrutsStr = consultaTagBusquedaPersonasStrutsStr + " and UPPER(per.email) like UPPER('%" + email + "%')";
					}
				}
			}
			//se evalua si el  criterio de busqueda viene vacio
			if (UtilidadCadena.noEsVacio(numeroIngreso))
				consultaTagBusquedaPersonasStrutsStr+=" and i.consecutivo='"+numeroIngreso+"'";
		}

		//consultaTagBusquedaPersonasStrutsStr += " order by primerapellidopersona, segundoapellidopersona, primernombrepersona, segundonombrepersona ";
		
		PreparedStatementDecorator consultaTagBusquedaPersonasStrutsStatement=null;
		//cuando la accion es de IngresoPaciente o Reserva de Cita la forma de consultar es diferente
		//Si se editó consulta para usuarios capitados se prosigue a la validacion
		if((accion.equals("IngresoPaciente")||accion.equals("ReservaCita"))&&!consultaTagBusquedaCapitadosStr.equals("")&&!consultaTagBusquedaCapitadosStr.endsWith("WHERE"))
		{

			//Si no hay busqueda por ficha de usuario capitado también se realiza la busqueda con la estructura de personas, de lo contrario solo con la estructura de usuarios capitados			
			if(fichaUsuarioCapitado.equals(""))
			{
					String consulta = "(" + consultaTagBusquedaPersonasStrutsStr + ") union ("+
				
					consultaTagBusquedaCapitadosStr + " )order by primerapellidopersona, segundoapellidopersona, primernombrepersona, segundonombrepersona";
					
					consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					logger.info("\n\nConsulta del paciente >>  "+consulta);
				
			}
			else
			{
				consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaCapitadosStr + " order by uc.primer_apellido, uc.segundo_apellido, uc.primer_nombre, uc.segundo_nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				logger.info("\n\nConsulta del paciente >>  "+consultaTagBusquedaCapitadosStr);
			}

		}
		else
		{
			consultaTagBusquedaPersonasStrutsStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaPersonasStrutsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		}
		
		return new ResultSetDecorator(consultaTagBusquedaPersonasStrutsStatement.executeQuery());

	}


	/**
	 * Implementación del método busca personas
	 * en una BD Genérica
	 * (TagBusquedaPersonas)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaPersonas(Connection , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagBusquedaPersonas(Connection con, String tipoPersonaBuscada, String criteriosBusqueda[], String numeroIdentificacion, String codigoTipoIdentificacion, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String fechaNacimiento, String direccion, String telefono, String codigoCiudad, String codigoCiudadIdentificacion, String codigoDepartamento, String codigoDepartamentoIdentificacion, String email, String codigoInstitucion) throws SQLException
	{
		Vector pseudoExpRegular;
		String  textoCompletoExpRegular;
		int j;

		String consultaTagBusquedaPersonasStr="";

		if (tipoPersonaBuscada.equals("IngresoPaciente") || tipoPersonaBuscada.equals("ModificarPaciente")|| tipoPersonaBuscada.equals("MostrarPacienteClinica")|| tipoPersonaBuscada.equals("solicitudLaboratorio") || tipoPersonaBuscada.equals("solicitudProcedimiento") || tipoPersonaBuscada.equals("MostrarPacienteFacturacion")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from personas per, pacientes pac, pacientes_instituciones pacins where per.codigo=pac.codigo_paciente and pac.codigo_paciente=pacins.codigo_paciente and pacins.codigo_institucion" + codigoInstitucion ;
		}
		else if (tipoPersonaBuscada.equals("ModificarUsuario")||tipoPersonaBuscada.equals("MostrarUsuario")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from personas per, usuarios us where per.codigo=us.codigo_persona and institucion=" + codigoInstitucion ;
		}
		else if (tipoPersonaBuscada.equals("ModificarMedico")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from personas per, medicos med, medicos_instituciones medins where per.codigo=med.codigo_medico and per.codigo=medins.codigo_medico and medins.codigo_institucion=" + codigoInstitucion;
		}
		else if (tipoPersonaBuscada.equals("MostrarUsuariosInactivos")) {
			consultaTagBusquedaPersonasStr = "SELECT per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.numero_identificacion as numeroIdentificacionPersona, per.tipo_identificacion as tipoIdentificacionPersona from usuarios us, personas per where us.codigo_persona=per.codigo and us.institucion=0";
		}

		//Ahora empezamos a colgarle arandelas a la consulta,
		//dependiendo de las opciones que nos hayan dado

		if (criteriosBusqueda != null) 
		{
			for (int i = 0; i < criteriosBusqueda.length; i++) 
			{
				if (criteriosBusqueda[i].equals("numeroIdentificacion")) 
				{
					if (numeroIdentificacion != null && !numeroIdentificacion.equals(""))
					{
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.numero_identificacion = '" + numeroIdentificacion + "'";
					}
				}
				else if (criteriosBusqueda[i].equals("tipoIdentificacion")) 
				{
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.tipo_identificacion='" + codigoTipoIdentificacion + "'";
				}
				else if (criteriosBusqueda[i].equals("primerNombrePersona")) 
				{
					//Las expresiones regulares necesitan que llegue algo
					//Luego si esto viene nulo o con '', toca ponerle al
					//menos un espacio
					if (primerNombrePersona != null && !primerNombrePersona.equals(""))
					{
						pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(primerNombrePersona);
						textoCompletoExpRegular=" and (";

						for (j=0;j<pseudoExpRegular.size();j++)
						{
							if (j!=0)
							{
								textoCompletoExpRegular=textoCompletoExpRegular+" or ";
							}
							textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.primer_nombre) like '%" +pseudoExpRegular.get(j) + "%' "; 
						}
						
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + textoCompletoExpRegular + ")";
					}
				}
				else if (criteriosBusqueda[i].equals("segundoNombrePersona")) 
				{
					if (segundoNombrePersona != null && !segundoNombrePersona.equals(""))
					{
						pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(segundoNombrePersona );
						textoCompletoExpRegular=" and (";

						for (j=0;j<pseudoExpRegular.size();j++)
						{
							if (j!=0)
							{
								textoCompletoExpRegular=textoCompletoExpRegular+" or ";
							}
							textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.segundo_nombre) like '%" +pseudoExpRegular.get(j) + "%' "; 
						}
						
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + textoCompletoExpRegular + ")";
					}
				}
				else if (criteriosBusqueda[i].equals("primerApellidoPersona")) 
				{
					if (primerApellidoPersona != null && !primerApellidoPersona.equals(""))
					{
						pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(primerApellidoPersona );
						textoCompletoExpRegular=" and (";

						for (j=0;j<pseudoExpRegular.size();j++)
						{
							if (j!=0)
							{
								textoCompletoExpRegular=textoCompletoExpRegular+" or ";
							}
							textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.primer_apellido) like '%" +pseudoExpRegular.get(j) + "%' "; 
						}
						
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + textoCompletoExpRegular + ")";
					}
				}
				else if (criteriosBusqueda[i].equals("segundoApellidoPersona")) 
				{
					if (segundoApellidoPersona != null && !segundoApellidoPersona.equals(""))
					{
						pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(segundoApellidoPersona );
						textoCompletoExpRegular=" and (";

						for (j=0;j<pseudoExpRegular.size();j++)
						{
							if (j!=0)
							{
								textoCompletoExpRegular=textoCompletoExpRegular+" or ";
							}
							textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.segundo_apellido) like '%" +pseudoExpRegular.get(j) + "%' "; 
						}
						
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + textoCompletoExpRegular + ")";
					}
				}
				else if (criteriosBusqueda[i].equals("ciudadIdentificacion")) 
				{
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.codigo_departamento_nacimiento=" + codigoDepartamentoIdentificacion + " and codigo_ciudad_nacimiento=" + codigoCiudadIdentificacion;
				}
				else if (criteriosBusqueda[i].equals("fechaNacimiento")) 
				{
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.fecha_nacimiento='" + fechaNacimiento + "'";
				}
				else if (criteriosBusqueda[i].equals("direccion")) 
				{
					if (direccion != null && !direccion.equals(""))
					{
						pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(direccion);
						textoCompletoExpRegular=" and (";

						for (j=0;j<pseudoExpRegular.size();j++)
						{
							if (j!=0)
							{
								textoCompletoExpRegular=textoCompletoExpRegular+" or ";
							}
							textoCompletoExpRegular= textoCompletoExpRegular + " UPPER(per.direccion) like '%" +pseudoExpRegular.get(j) + "%' "; 
						}
						
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + textoCompletoExpRegular + ")";
					}
				}
				else if (criteriosBusqueda[i].equals("ciudadVivienda")) 
				{
					consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and per.codigo_departamento_vivienda=" + codigoDepartamento + " and codigo_ciudad_vivienda=" + codigoCiudad;
				}
				else if (criteriosBusqueda[i].equals("telefono")) 
				{
					if (telefono != null && !telefono.equals(""))
					{
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.telefono) like UPPER('%" + telefono + "%')";
					}
				}
				else if (criteriosBusqueda[i].equals("email")) 
				{
					if (email != null && !email.equals(""))
					{
						consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " and UPPER(per.email) like UPPER('%" + email + "%')";
					}
				}
			}
		}

		consultaTagBusquedaPersonasStr = consultaTagBusquedaPersonasStr + " order by per.primer_apellido, per.segundo_apellido, per.primer_nombre, per.segundo_nombre";

		PreparedStatementDecorator consultaTagBusquedaPersonasStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaPersonasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagBusquedaPersonasStatement.executeQuery());
	}

	/**
	 * Implementación del método auxiliar para la búsqueda
	 * de fecha en el caso de estar trabajando con una
	 * valoración de consulta externa, para una BD Genérica
	 * (TagBusquedaDiagnosticos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos_FechaCita (Connection , int ) throws SQLException
	 */
	public static String consultaTagBusquedaDiagnosticos_FechaCita (Connection con, int numeroSolicitud) throws SQLException
	{
	    PreparedStatementDecorator consultaTagBusquedaDiagnosticos_FechaCitaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaDiagnosticos_FechaCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    consultaTagBusquedaDiagnosticos_FechaCitaStatement.setInt(1, numeroSolicitud);
	    
	    try {

		    ResultSetDecorator rs=new ResultSetDecorator(consultaTagBusquedaDiagnosticos_FechaCitaStatement.executeQuery());
		    if (rs.next())
		    {
		        String fecha=rs.getString("fecha");
		        rs.close();
		        return fecha;
		    }

		} catch (Exception e) {
			return "";
		}

		return "";
	}
	
	/**
	 * Implementación del método auxiliar para la búsqueda
	 * del tipo de cie válido dada una fecha en una BD 
	 * Génerica
	 * (TagBusquedaDiagnosticos)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos_Cie (Connection , String ) throws SQLException
	 */
	public static int consultaTagBusquedaDiagnosticos_Cie (Connection con, String fecha) throws SQLException
	{
	    PreparedStatementDecorator consultaTagBusquedaDiagnosticos_CieStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaDiagnosticos_CieStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    consultaTagBusquedaDiagnosticos_CieStatement.setString(1, fecha);
	    ResultSetDecorator rs=new ResultSetDecorator(consultaTagBusquedaDiagnosticos_CieStatement.executeQuery());
	    if (rs.next())
	    {
	        int respuesta=rs.getInt("codigo");
	        rs.close();
	        return respuesta;
	    }
	    else
	    {
	        return ConstantesBD.codigoNuncaValido;
	    }
	}
	
	/**
	 * Implementación del método busca diagnosticos
	 * en una BD Génerica
	 * (TagBusquedaDiagnosticos)
	 * @param codigoTipoCie @todo
	 * @param codigoFiltro @todo
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaDiagnosticos(Connection , String , boolean ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagBusquedaDiagnosticos(Connection con, String criterioBusquedaDiagnostico, boolean buscarTexto, int codigoTipoCie, int codigoFiltro) throws SQLException
	{
		Vector pseudoExpRegular;
		int j;
		//Si me ponen un dato vacio, las expresiones regulares no funcionan y además
		//no son necesarias (no hay criterio)

		String consultaTagBusquedaDiagnosticosStr="";
		if (criterioBusquedaDiagnostico==null||criterioBusquedaDiagnostico.equals(""))
		{
			consultaTagBusquedaDiagnosticosStr="SELECT acronimo, tipo_cie, nombre from diagnosticos where tipo_cie=" + codigoTipoCie +" and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + "";
		}
		else if ( buscarTexto  )
		{
 			pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(criterioBusquedaDiagnostico);

			consultaTagBusquedaDiagnosticosStr="SELECT acronimo, tipo_cie, nombre from diagnosticos where tipo_cie=" + codigoTipoCie +" and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and (";
			for (j=0;j<pseudoExpRegular.size();j++)
			{
				if (j!=0)
				{
					consultaTagBusquedaDiagnosticosStr=consultaTagBusquedaDiagnosticosStr+" or ";
				}
				consultaTagBusquedaDiagnosticosStr= consultaTagBusquedaDiagnosticosStr+ " UPPER(nombre) like '%" +pseudoExpRegular.get(j) + "%' "; 
			}
			consultaTagBusquedaDiagnosticosStr= consultaTagBusquedaDiagnosticosStr+ " )";
		}
		else if ( !buscarTexto )
		{
			consultaTagBusquedaDiagnosticosStr="SELECT acronimo, tipo_cie, nombre from diagnosticos where tipo_cie=" + codigoTipoCie +" and activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and UPPER(acronimo) like UPPER('%" + criterioBusquedaDiagnostico + "%')";
		}
		PreparedStatementDecorator consultaTagBusquedaDiagnosticosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTagBusquedaDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(consultaTagBusquedaDiagnosticosStatement.executeQuery());

	}

	/**
	 * Implementación del método que busca convenios
	 * en una BD Genérica
	 * (TagBusquedaConvenios)
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaConvenios (Connection , String ) throws SQLException
	 */
	public static ResultSetDecorator consultaTagBusquedaConvenios (Connection con, String codigoInstitucion,String fechaReferencia,String capitado) throws SQLException
	{
		//*******Se arma la consulta si hay fecha de referencia************************************
		String consulta = "SELECT " +
			"conv.nombre, " +
			"conv.codigo, " +
			"conv.tipo_regimen, " +
			"conv.pyp "+
			"from convenios conv " +
			"INNER JOIN empresas emp ON (emp.codigo=conv.empresa) " +
		 	"INNER JOIN terceros  ter ON (emp.tercero=ter.codigo and ter.institucion=?) " +
			"WHERE conv.codigo IN (SELECT convenio FROM contratos WHERE fecha_inicial <= ";
		if(!fechaReferencia.equals(""))
			consulta += " '"+UtilidadFecha.conversionFormatoFechaABD(fechaReferencia)+"' ";
		else
			consulta += " CURRENT_DATE ";
		
		consulta += " and fecha_final >= ";
		
		if(!fechaReferencia.equals(""))
			consulta += " '"+UtilidadFecha.conversionFormatoFechaABD(fechaReferencia)+"' ";
		else
			consulta += " CURRENT_DATE ";
		 consulta += " ) and conv.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + "  " ;
		 	
		 
		 if(!capitado.equals(""))
		 {
			 if(UtilidadTexto.getBoolean(capitado))
				 consulta += "AND conv.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado;
			 else
				 consulta += "AND conv.tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado;
		 }
		 
		consulta+=" order by conv.nombre ";
		
		PreparedStatementDecorator consultaTagBusquedaConvenioStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaTagBusquedaConvenioStatement .setString(1, codigoInstitucion);
		return new ResultSetDecorator(consultaTagBusquedaConvenioStatement.executeQuery());
	}

	/**
	 * Implementación del método busca servicios
	 * en una BD Genérica
	 * (TagBusquedaServicios)
	 * @param porCodigoCUPS 
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagBusquedaServicios(Connection , char , String , boolean , int , boolean , boolean , int , int , boolean , int , String ) throws SQLException
	 */
/* Esto no se está usando en nigún lado.....................
	public static HashMap consultaTagBusquedaServicios(Connection con, char tipoServicioBuscado, String criterioBusqueda, boolean esPorNombre, boolean buscarEnTodosLosTarifarios, boolean buscarConFormulario, int codigoSexo, int codigoContrato, boolean restringirPorFormulario, int idCuenta, String ocupacionSolicitada, boolean porCodigoCUPS, int codigoInstitucion) throws SQLException
	{
	    String filtroCriterio="";
		
		//Cuando es de tipo Interconsulta, también debemos
		//buscar los de consultar externa
		String restriccionTipoServicio;
		
		if (tipoServicioBuscado!=ConstantesBD.codigoServicioInterconsulta)
		{
		    restriccionTipoServicio = " ser.tipo_servicio='" + tipoServicioBuscado + "'";
		}
		else
		{
		    restriccionTipoServicio = " (ser.tipo_servicio='" + tipoServicioBuscado + "' or ser.tipo_servicio='" + ConstantesBD.codigoServicioAntiguoInterconsulta+ "')";;
		}
		

		//La siguiente Restricción SOLO? aplica
		//para solicitudes de interconsulta (por medio
		//del join - necesario nos aseguramos de esto,por
		//eso no hay restricción de tipo de solicitud en
		//la consulta) , no se necesita sub-select con 
		//anuladas porque las anuladas quedan con otro 
		//estado HC!
		String restriccionNoMismaEspecialidadEnInterconsulta="";
		String restriccionInterconsultaPermitidaSistema="";
		
		//Las dos restricciones anteriores solo aplican para interconsulta
		if (tipoServicioBuscado==ConstantesBD.codigoServicioInterconsulta)
		{
			restriccionNoMismaEspecialidadEnInterconsulta=" and ser.especialidad NOT IN (SELECT serv.especialidad from solicitudes sol INNER JOIN solicitudes_inter solinter ON (sol.numero_solicitud=solinter.numero_solicitud)  INNER JOIN servicios serv ON (solinter.codigo_servicio_solicitado=serv.codigo) where sol.cuenta=" + idCuenta +" and sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada + ")";
			
			//Solo se debe permitir la búsqueda en interconsulta
			//de lo permitido por la tabla interconsu_perm
			restriccionInterconsultaPermitidaSistema=" and ser.especialidad IN (SELECT especialidad from interconsu_perm where ocupacion_medica =" + ocupacionSolicitada + " or ocupacion_medica=" +ConstantesBD.codigoOtros + ")";
		}
		
		//Si el usuario genera un caso que aborte la consulta, ej
		//en el campo de código no hay un entero, se cancela
		//la búsqueda (se compensa el tiempo de manejo de la
		//excepción con el ahorro en la consulta)
		boolean cancelarBusqueda=false;
		String busquedaCasoCancelar = "SELECT numero_solicitud from valoraciones where numero_solicitud < -50";

		//La consulta interna nos busca el "core" del asunto
		
		if (esPorNombre)
		{
			Vector pseudoExpRegular;
			int j;
			if (criterioBusqueda!=null)
			{
				pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(criterioBusqueda);
				filtroCriterio="(";

				for (j=0;j<pseudoExpRegular.size();j++)
				{
					if (j!=0)
					{
						filtroCriterio=filtroCriterio+" or ";
					}
					filtroCriterio= filtroCriterio+ " UPPER(refser.descripcion) like '%" +pseudoExpRegular.get(j) + "%' "; 
				}
				
				filtroCriterio=filtroCriterio+" ) and ";
			}
		}
		else
		{
			if(porCodigoCUPS)
			{
				filtroCriterio="refser.codigo_propietario='" + criterioBusqueda + "' and refser.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups + " and ";
			}
			else
			{
				try
				{
					Integer.parseInt(criterioBusqueda);
				}
				catch (NumberFormatException e)
				{
					cancelarBusqueda=true;
				}
				filtroCriterio=" ser.codigo=" + criterioBusqueda + " and ";
			}
		}

		if (!cancelarBusqueda)
		{
			String filtroPorFormulario1="",filtroPorFormulario2="";
			if (restringirPorFormulario)
			{
				if (buscarConFormulario)
				{
					filtroPorFormulario1= " ser.codigo IN (SELECT servicio from form_resp_serv) and ";
				}
				else
				{
					filtroPorFormulario1= " ser.codigo NOT IN (SELECT servicio from form_resp_serv) and ";
				}
			}
			else
			{
				filtroPorFormulario1="";
			}
			if (restringirPorFormulario)
			{
				if (buscarConFormulario)
				{
					filtroPorFormulario2= " ser.codigo IN (SELECT servicio from form_resp_serv) and ";
				}
				else
				{
					filtroPorFormulario2= " ser.codigo NOT IN (SELECT servicio from form_resp_serv) and ";
				}
			}
			else
			{
				filtroPorFormulario2="";
			}
			String consultaSuperior, filtroSexo=" (ser.sexo is null or (ser.sexo="  + codigoSexo + ")) and ";

			if (buscarEnTodosLosTarifarios)
			{
				//En el caso de todos los tarifarios NO aplica lo de excepciones
				String consultaInternaIncompletaConJoinEspecialidades;
				
				if (tipoServicioBuscado==ConstantesBD.codigoServicioFalso)
				{
					consultaInternaIncompletaConJoinEspecialidades=" from servicios ser, referencias_servicio refser, especialidades esp where " + filtroCriterio + filtroPorFormulario1 + filtroSexo + " ser.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + restriccionInterconsultaPermitidaSistema + restriccionNoMismaEspecialidadEnInterconsulta + " and refser.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups;
				}
				else
				{
					consultaInternaIncompletaConJoinEspecialidades=" from servicios ser, referencias_servicio refser, especialidades esp where " + filtroCriterio + filtroPorFormulario1 + filtroSexo + restriccionTipoServicio +" and ser.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + restriccionInterconsultaPermitidaSistema + restriccionNoMismaEspecialidadEnInterconsulta + " and refser.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups;
				}
				
				consultaSuperior="select ser.codigo, ser.espos, ser.especialidad as especialidad, refser.descripcion as nombre, " + ValoresPorDefecto.getValorFalseParaConsultas() + " as esExcepcion, 'N/A' as codigoPropietario, esp.nombre as nombreEspecialidad " + consultaInternaIncompletaConJoinEspecialidades;
			}
			else
			{
				String busquedaExcepciones="SELECT servicio from excepciones_servicios where contrato=" + codigoContrato;
				
				consultaSuperior="(SELECT " +
						"ser.codigo as codigo, " +
						"ser.espos as espos, " +
						"ser.especialidad as especialidad,"+
						restriccionNoMismaEspecialidadEnInterconsulta.replaceAll("ser2","ser")+", "+
						"getnombreservicio(ser.codigo,"+ConstantesBD.codigoTarifarioCups+") as nombre, " +
						"true as esExcepcion, " +
						//"CASE WHEN refser.tipo_tarifario="+codigoTarifarioBusqueda++" THEN as codigoPropietario, " +
						" getcodigopropservicio(ser.codigo,getEsquemaTarifarioSerArt(-1,"+codigoContrato+",ser.codigo,'S'::integer)) as codigoPropietario, "+
						"esp.nombre as nombreEspecialidad," +
						"ser.grupo_servicio as gruposervicio, " +
						"ser.toma_muestra as tomamuestra," +
						"ser.respuesta_multiple as respuestamultiple "+
						"FROM servicios ser, referencias_servicio refser, especialidades esp " +
						"WHERE "+
						filtroCriterio+
						filtroPorFormulario1 + 
						filtroSexo +
						restriccionTipoServicio + 
						" and ser.activo=true and ser.codigo=refser.servicio and ser.especialidad=esp.codigo " + 
						restriccionInterconsultaPermitidaSistema+
						" and refser.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+
						" and ser.codigo IN (" + busquedaExcepciones +") "+
						" ORDER BY refser.descripcion) "+
						" UNION "+
						"(SELECT " +
						"ser2.codigo as codigo, " +
						"ser2.espos as espos, " +
						"ser2.especialidad as especialidad,"+
						restriccionNoMismaEspecialidadEnInterconsulta+", "+
						"getnombreservicio(ser2.codigo,"+ConstantesBD.codigoTarifarioCups+") as nombre, " +
						"false as esExcepcion, " +
						//"CASE WHEN refser.tipo_tarifario="+codigoTarifarioBusqueda++" THEN as codigoPropietario, " +
						" getcodigopropservicio(ser2.codigo,getEsquemaTarifarioSerArt(-1,"+codigoContrato+",ser2.codigo,'S')::integer) as codigoPropietario, "+
						"esp2.nombre as nombreEspecialidad ," +
						"ser2.grupo_servicio as gruposervicio , " +
						"ser2.toma_muestra as tomamuestra," +
						"ser2.respuesta_multiple as respuestamultiple "+
						"FROM servicios ser2, referencias_servicio refser2, especialidades esp2 " +
						"WHERE "+
						filtroCriterio.replaceAll("ser.","ser2.")+
						filtroPorFormulario2 + 
						filtroSexo.replaceAll("ser.","ser2.") +
						restriccionTipoServicio.replaceAll("ser.","ser2.").replaceAll("ser2.icio","servicio") + 
						" and ser2.activo=true and ser2.codigo=refser2.servicio and ser2.especialidad=esp2.codigo " + 
						restriccionInterconsultaPermitidaSistema.replaceAll("ser.","ser2.")+
						" and refser2.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+
						" and ser2.codigo NOT IN (" + busquedaExcepciones +") "+
						" ORDER BY refser2.descripcion) "; 
			}
			
			PreparedStatementDecorator statementConsulta= new PreparedStatementDecorator(con.prepareStatement(consultaSuperior,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultadoDecorator=new ResultSetDecorator(statementConsulta.executeQuery());
			HashMap resultado=UtilidadBD.cargarValueObject(resultadoDecorator);
			resultadoDecorator.close();
			statementConsulta.close();
			
			logger.info("Consulta Superior ________________ "+consultaSuperior);
			return resultado;
			
			
		}
		else
		{
			PreparedStatementDecorator statementConsulta= new PreparedStatementDecorator(con.prepareStatement(busquedaCasoCancelar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultadoDecorator=new ResultSetDecorator(statementConsulta.executeQuery());
			HashMap resultado=UtilidadBD.cargarValueObject(resultadoDecorator);
			resultadoDecorator.close();
			statementConsulta.close();
			
			logger.info("Consulta Superior ________________ "+busquedaCasoCancelar);
			return resultado;
		}
	}
*/

	/**
	 * Implementación del método busca servicios sin restricciones
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.TagDao#busquedaSencillaServicios (Connection , String , int , boolean , boolean ) throws SQLException
	 */
	public static ResultSetDecorator busquedaSencillaServicios (Connection con, String criterioBusqueda, int codigoTarifarioBusqueda, boolean esPorNombre, boolean buscarSoloActivos) throws SQLException
	{
		
		String filtroCriterio="";
		String consultaBaseStr="SELECT refser1.descripcion as nombreServicio, refser1.servicio as codigoServicio, serv.especialidad as codigoEspecialidad, serv.activo, esp.nombre as nombreEspecialidad, serv.tipo_servicio As tipoServicio from referencias_servicio refser1  INNER JOIN referencias_servicio refser2 ON (refser1.servicio=refser2.servicio) INNER JOIN servicios serv ON (serv.codigo=refser1.servicio) INNER JOIN especialidades esp ON (serv.especialidad=esp.codigo) where refser1.tipo_tarifario=" + ConstantesBD.codigoTarifarioCups + " and refser2.tipo_tarifario=" + codigoTarifarioBusqueda; 
		if (esPorNombre)
		{
			Vector pseudoExpRegular;
			int j;
			if (criterioBusqueda!=null)
			{
				pseudoExpRegular=UtilidadTexto.generarVectorExpresionRegularMayuscula(criterioBusqueda);
				filtroCriterio=" and (";

				for (j=0;j<pseudoExpRegular.size();j++)
				{
					if (j!=0)
					{
						filtroCriterio=filtroCriterio+" or ";
					}
					filtroCriterio= filtroCriterio+ " UPPER(refser1.descripcion) like '%" +pseudoExpRegular.get(j) + "%' "; 
				}
				
				filtroCriterio=filtroCriterio+" ) ";
			}
		}
		else
		{
			//Por código
			try
			{
				filtroCriterio= " and serv.codigo=" + Integer.parseInt(criterioBusqueda);
			}
			catch (Exception e)
			{
				//Si me dijeron que era por código y no era un número
				//no puedo agregarlo a la consulta o me saca error, la dejo
				//igual
			}
		}
		
		if (buscarSoloActivos)
		{
			filtroCriterio=filtroCriterio +" and serv.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + "";
		}
		
		consultaBaseStr=consultaBaseStr + filtroCriterio;
		logger.info("\n cadena -->"+consultaBaseStr);
		PreparedStatementDecorator busquedaSencillaServiciosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaBaseStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(busquedaSencillaServiciosStatement.executeQuery());
	}

	public static ResultadoCollectionDB funcionalidadesADibujar(Connection con, String login, int funcionalidadPadre, String fincionalidadesHijas, boolean modo)
	{
		String consulta="SELECT DISTINCT(etiqueta_func) AS nombreFuncionalidad," +
								  " archivo_func AS path from roles_usuarios ru" +
								  " INNER JOIN usuarios u ON(ru.login=u.login)" +
								  " INNER JOIN roles_funcionalidades rf ON(rf.nombre_rol=ru.nombre_rol)" +
								  " INNER JOIN funcionalidades f ON(f.codigo_func=rf.codigo_func)" +
								  " INNER JOIN dependencias_func df ON(df.funcionalidad_padre=f.codigo_func AND";

		try
		{
			PreparedStatementDecorator pst=null;
			if(funcionalidadPadre!=0 && modo)
			{
				consulta+=" df.funcionalidad_hija="+funcionalidadPadre+")";
			}
			else if(fincionalidadesHijas!=null && !fincionalidadesHijas.equals("") && !modo)
			{
				consulta+=" df.funcionalidad_padre IN("+fincionalidadesHijas+"))";
			}
			consulta+=" WHERE u.login=? AND f.debo_imprimir=" + ValoresPorDefecto.getValorFalseParaConsultas();
			
			consulta+=" ORDER BY nombreFuncionalidad ";
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,login);
					
			return new ResultadoCollectionDB(true,"",UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery())));
		}
		catch(Exception e)
		{
			logger.error("Error consultando las funcionalidades del tercer nivel para el usuario "+login+": "+e);
		}
		return new ResultadoCollectionDB(true);
	}
	
	/**
	 * Metodo que busca los atributos de la justificacion de una solicitud
	 * @param con
	 * @param mostrarAtributosArticulo
	 * @param institucion Institución para el filtrado
	 * @return Collection con los resultados de la búsqueda
	 */
	public static Collection consultaTagMuestraAtributos(Connection con, boolean mostrarAtributosArticulo, int institucion)
	{
		String restriccion;
		String seleccionRequerido;
		if(mostrarAtributosArticulo)
		{
			restriccion="es_articulo";
			seleccionRequerido="es_requerido_art";
		}
		else
		{
			restriccion="es_servicio";
			seleccionRequerido="es_requerido_ser";
		}
		String consultaTagStr="SELECT codigo AS codigo, nombre AS nombre, "+seleccionRequerido+" AS esrequerido, validar_autorizacion AS validarAutorizacion FROM atributos_solicitud WHERE "+restriccion+"=? AND institucion=?";
		try
		{
			
			PreparedStatementDecorator consulta= new PreparedStatementDecorator(con.prepareStatement(consultaTagStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setBoolean(1, true);
			consulta.setInt(2, institucion);
			Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
			consulta.close();
			return col;
			
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los atributos de la solicitud : "+e);
			return null;
		}
	}
	/**
	 * Consulta en la base de datos las enfermeras que están activas en el sistema
	 * @param con
	 * @param restriccion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultaTagMuestraEnfermerasActivas (Connection con, String restriccion, String codigoInstitucion)
	{
	    consultaTagMuestraEnfermerasActivasStr=
	    "SELECT tablaTemp.nombres, tablaTemp.codigo_medico FROM (" +
	    "SELECT per.primer_apellido || ' ' || per.segundo_apellido || ' ' || per.primer_nombre || ' ' || per.segundo_nombre AS nombres," +
	    " med.codigo_medico FROM personas per INNER JOIN medicos med ON(per.codigo=med.codigo_medico) " +
	    "INNER JOIN medicos_instituciones medins on(medins.codigo_medico=med.codigo_medico) " +
	    " WHERE med.ocupacion_medica="+ ConstantesBD.codigoOcupacionMedicaEnfermera + " " +
	    		"AND medins.codigo_institucion=? AND med.codigo_medico NOT IN (SELECT codigo_medico FROM medicos_inactivos) " +
	    		"ORDER BY per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre) AS tablaTemp ";

	    
	    
		PreparedStatementDecorator consultaTagEnfermerasActivasStatement;
		
        try {
            if(restriccion!=null)
            {
                consultaTagMuestraEnfermerasActivasStr+=restriccion;
            }
            consultaTagEnfermerasActivasStatement =  new PreparedStatementDecorator(con.prepareStatement(consultaTagMuestraEnfermerasActivasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultaTagEnfermerasActivasStatement.setString(1, codigoInstitucion);
            
            return new ResultSetDecorator(consultaTagEnfermerasActivasStatement.executeQuery());
        } catch (SQLException e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
	}

	/**
	 * Método para buscar los diagnoticos NANDA
	 * @param con
	 * @param esPorCodigo
	 * @param criterio
	 * @param institucion @todo
	 * @param codigosNoBuscados @todo
	 * @return Vector con los resultados de la búsqueda
	 */
	public static Vector buscarNanda(Connection con, boolean esPorCodigo, String criterio, int institucion, String codigosNoBuscados)
	{
		String restriccion;
		if(esPorCodigo)
		{
			restriccion="codigo";
		}
		else
		{
			restriccion="descripcion";
		}
		String consulta="SELECT i.codigo AS codigo, d.codigo || ' - ' || d.descripcion AS descripcion FROM diagnosticos_nanda d INNER JOIN diagnosticos_nanda_inst i ON(i.diagnostico_nanda=d.codigo) WHERE UPPER(d."+restriccion+") LIKE UPPER('%"+criterio+"%') AND i.institucion=? AND i.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
		if(codigosNoBuscados!=null && !codigosNoBuscados.trim().equals(""))
		{
			consulta+=" AND i.codigo NOT IN("+codigosNoBuscados+")";
		}
		consulta+=" ORDER BY d.codigo || ' - ' || d.descripcion";
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, institucion);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			Vector resultados=new Vector();
			while(resultado.next())
			{
				Vector fila=new Vector();
				fila.add(resultado.getString("codigo"));
				fila.add(resultado.getString("descripcion"));
				resultados.add(fila);
			}
			return resultados;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los diagnósticos nanda: "+e);
			return null;
		}
	}

	
	/**
	 * Metodo para consultar si un diagnostico es de importancia en salud publica y debe ser diligenciado
	 * en una ficha de vigilancia epidemiologica
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static ResultSetDecorator consultaTagDiagnosticoSaludPublica(Connection con, String acronimo)
	{
	    String consultaStr="SELECT codigoEnfermedadesNotificables FROM diagnosticos WHERE acronimo=?";
	    ResultSetDecorator rs=null;
	    
	    try {
	        PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,acronimo);
	        rs =new ResultSetDecorator(ps.executeQuery());
	       
	        return rs;
	    }
	    catch (SQLException sqle) {
	        logger.error("Error consultando el campo codigoEnfermedadesNotificables");
	        
	        return null;
	    }
	}
	

	/**
	 * Metodo para consultar si hay fichas de vigilancia epidemiologicas pendientes por completar, hacer seguimiento
	 * o notificar.
	 * @param con
	 * @param codigoUsuario
	 * @return
	 */
	public static ResultSetDecorator consultaHayFichasPendientes(Connection con, String loginUsuario)
	{

	    String consultaStr="SELECT estado FROM " +

	    						"(" +
		    						"SELECT " +
		    							"codigoficharabia as codigo," +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigificharabia " +
		    						"UNION " +
		    						
			    					"SELECT " +
		    							"codigofichasarampion as codigo," +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichasarampion " +
		    						"UNION " +
		    							
		    						"SELECT " +
		    							"codigofichavih as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichavih " +
	    							"UNION " +
	    							
		    						"SELECT " +
		    							"codigofichadengue as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichadengue " +
		    						"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaparalisis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaparalisis " +
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichasifilis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichasifilis " +
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichatetanos as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichatetanos " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichagenerica as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichagenerica " +
		    							
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichatuberculosis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichatuberculosis " +
		    							
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichamortalidad as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichamortalidad " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichabrotes as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichabrotes " +
		    							
		    						"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichainfecciones as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichainfecciones " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichalesiones as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichalesiones " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaIntoxicacion as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaintoxicacion " +
		    							
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigoficharubcongenita as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigificharubcongenita " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaofidico as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaofidico " +

	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichalepra as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichalepra " +

	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichadifteria as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichadifteria " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaeasv as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaeasv " +

	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaesi as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaesi " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaetas as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaetas " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichahepatitis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichahepatitis " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichaleishmaniasis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichaleishmaniasis " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichamalaria as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichamalaria " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichameningitis as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichameningitis " +
		    						
	    							"UNION " +
		    						
		    						"SELECT " +
		    							"codigofichatosferina as codigo, " +
		    							"loginusuario, " +
		    							"estado " +
		    						"FROM " +
		    							"epidemiologia.vigifichatosferina " +
	    						") fichas " +

	    					"WHERE " +
	    						"loginUsuario=? " +
	    					"AND " +
	    						"estado="+Integer.toString(ConstantesBD.codigoEstadoFichaIncompleta);
	    
	    ResultSetDecorator rs = null;
	    
	    try {
	        PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,loginUsuario);
	        rs =new ResultSetDecorator(ps.executeQuery());
	        
	        return rs;
	    }
	    catch (SQLException sqle) {
	        logger.error("Error consultando las notificaciones pendientes:"+sqle.getMessage());
	        
	        return null;
	    }
	}
	
	
	
	/**
	 * Metodo para consultar los usuarios de la funcionalidad administrador de Epidemiologia
	 * @param con
	 * @param codigoFuncionalidadEpidemiologia
	 * @return
	 */
	public static ResultSetDecorator consultaUsuariosEpidemiologia(Connection con)
	{
	    String consultaStr = "SELECT DISTINCT " +
	    						"usuarios.login," +
	    						"personas.primer_nombre," +
	    						"personas.segundo_nombre," +
	    						"personas.primer_apellido " +
	    					"FROM " +
	    						"personas," +
	    						"usuarios," +
	    						"roles_funcionalidades," +
	    						"roles_usuarios " +
    						"WHERE " +
    							"roles_funcionalidades.codigo_func>=30000 " +
    						"AND " +
    							"roles_funcionalidades.codigo_func<=30001 " +
    						"AND " +
    							"roles_usuarios.nombre_rol=roles_funcionalidades.nombre_rol " +
    						"AND " +
    							"personas.codigo=usuarios.codigo_persona " +
    						"AND " +
    							"roles_usuarios.login=usuarios.login ";
	    
	    ResultSetDecorator rs = null;
	    
	    try {
	        PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        rs =new ResultSetDecorator(ps.executeQuery());
	        
	        return rs;
	    }
	    catch (SQLException sqle) {
	    	
	        logger.error("Error consultando los usuarios de epidemiologia");
	        return null;
	    }
	}
}
