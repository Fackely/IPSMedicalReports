/*
 * @(#)PostgresqlIngresoGeneralDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoIngresosFactura;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.IngresoGeneralDao;
import com.princetonsa.dao.sqlbase.SqlBaseIngresoGeneralDao;
/**
 * Esta clase implementa el contrato estipulado en <code>IngresoGeneralDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>IngresoGeneral</code>
 *
 * A diferencia de paciente o usuario, en esta clase no se usan transacciones, dado que
 * las inserciones / modificaciones / borrado consisten en una única operacion (por lo cual
 * el auto commit por defecto de las conexiones de JDBC funciona perfectamente)
 *
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class PostgresqlIngresoGeneralDao implements IngresoGeneralDao {



	
	/**
	 * Cadena con el estatement necesario para ingresar la informacion adicional de una poliza 
	 * cuando el convenio lo requiera
	 */
	private static final String ingresarInfoAdicionalPolizaStr=" INSERT INTO informacion_poliza " +
															   " (codigo, " +
															   " fecha_autorizacion, " +
															   " numero_autorizacion," +
															   " valor_monto_autorizado, " +
															   " fecha_grabacion, " +
															   " hora_grabacion, " +
															   " usuario, " +
															   " cuenta," +
															   " convenio) " +
															   " VALUES (nextval('seq_informacion_poliza'),?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	

	/**
	 * Inserta en la base de datos PostgreSQL un nuevo Ingreso General. Tiene que tener mucho cuidado en
	 * siempre insertar primero el ingreso y DESPUES la cuenta.
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param tipoId tipo de identificación del paciente a ser insertado
	 * @param numeroId número de identificación del paciente
	 * @param anioIngreso año en que se efectúa el ingreso
	 * @param mesIngreso mes en que se efectúa el ingreso
	 * @param diaIngreso día en que se efectúa el ingreso
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param institucion nombre de la institución en la que se va a insertar este ingreso
	 * @return número de ingresos efectuados (debe ser 1)
	 */
	public int insertarIngreso(Connection con, int codigoPaciente, String institucion, String estado, String usuario,String consecutivo, String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String transplante) throws SQLException
	{
	    return SqlBaseIngresoGeneralDao.insertarIngreso(con, codigoPaciente, institucion, estado, usuario,consecutivo,anioConsecutivo, centroAtencion,pacEntidadSubcontratada,fechaIngreso,horaIngreso,"nextval('seq_ingresos')",transplante) ;
	}

	/**
	 * Inserta en la base de datos PostgreSQL un nuevo Ingreso General
	 * especificando el estado de la transacción.
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoPaciente Código que identifica al paciente
	 * en el sistema
	 * @param institucion Código de la institución donde se realiza
	 * este ingreso
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return número de ingresos efectuados (debe ser 1)
	 */
	public int insertarIngresoTransaccional(Connection con, int codigoPaciente, String institucion, String estado, String estadoIngreso, String usuario,String consecutivo, String anioConsecutivo,int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String transplante) throws SQLException
	{
	    return SqlBaseIngresoGeneralDao.insertarIngresoTransaccional(con, codigoPaciente, institucion, estado, estadoIngreso,usuario,consecutivo,anioConsecutivo, centroAtencion,pacEntidadSubcontratada,fechaIngreso,horaIngreso,"nextval('seq_ingresos')",transplante) ;
	}

	public int empezarTransaccion(Connection con) throws SQLException
	{
	    return SqlBaseIngresoGeneralDao.empezarTransaccion(con);
	}

	/**
	 * Carga la información relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param idIngreso número de identificación del ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexión abierta con la base de datos PostgreSQL
	 */
	public Answer cargarIngreso(Connection con,String idIngreso) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.cargarIngreso(con,idIngreso) ;
	}

	/**
	 * Carga la información relevante a un ingreso general y la almacena en su objeto respectivo.
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de identificación del paciente
	 * @param codigoInstitucion código de la institución desde la cual se va a cargar este ingreso
	 * @return un objeto <code>Answer</code> con el <code>ResultSet</code> con los datos del ingreso
	 * y una conexión abierta con la base de datos PostgreSQL
	 */
	public Answer cargarIngreso(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.cargarIngreso(con, codigoPaciente, codigoInstitucion) ;
	}

	/**
	 * Modifica la información de un Ingreso General en la base de datos PostgreSQL.
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param codigoTipoPaciente código del tipo de paciente
	 * @param codigoViaIngreso código de la vía de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param idIngreso número de identificación del ingreso
	 * @return número de ingresos modificados en la base de datos PostgreSQL
	 */
	public int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, String idIngreso) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.modificarIngreso(con, anioEgreso, mesEgreso, diaEgreso, horaEgreso, idIngreso) ;
	}
	
	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public int modificarFechaHoraIngreso(Connection con, String idIngreso)
	{
	    return SqlBaseIngresoGeneralDao.modificarFechaHoraIngreso(con, idIngreso);
	}

	/**
	 * Modifica la información de un Ingreso General en la base de datos PostgreSQL.
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param horaIngreso hora en que se efectúa el ingreso
	 * @param codigoTipoPaciente código del tipo de paciente
	 * @param codigoViaIngreso código de la vía de ingreso
	 * @param estadoIngreso estado del ingreso
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de identificación del paciente
	 * @param codigoInstitucion código de la institución de la cual se desea modificar un ingreso
	 * @return número de ingresos modificados en la base de datos PostgreSQL
	 */
	public int modificarIngreso(Connection con, String anioEgreso, String mesEgreso, String diaEgreso, String horaEgreso, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.modificarIngreso(con, anioEgreso, mesEgreso, diaEgreso, horaEgreso, codigoPaciente, codigoInstitucion) ;
	}

	/**
	 * Este metodo me permite buscar un id de ingreso abierto , dado el codigo del paciente.
	 * OJO debe ser usado con cuidado y siempre junto a una validación importante.
	 * Si no existe un ingreso abierto para este paciente retorna 0
	 * @param con una conexión abierta con una base de datos PostgreSQL
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param tipoIdentificacionPaciente tipo de la identificación del paciente
	 * @param codigoInstitucion código de la institución en la cual se va a buscar un ingreso
	 * @return número de identificación del ingreso correspondiente a los datos del paciente.
	 */
	private static int buscarIdIngreso (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.buscarIdIngreso (con, codigoPaciente, codigoInstitucion) ;
	}

	/**
	 * Implementación de la consulta de codigos de ingresos asociados a un paciente en una institucion
	 * @see com.princetonsa.dao.IngresoGeneralDao#getCodigosIngresosPaciente (con, String, String, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */	
	public ResultSetDecorator getCodigosIngresosPaciente(Connection con, int codigoPaciente, int codigoInstitucion) throws SQLException
	{
		return SqlBaseIngresoGeneralDao.getCodigosIngresosPaciente(con, codigoPaciente, codigoInstitucion) ;
	}
	
	/**
	 * metodo que deja vacias (abierta) la fecha y hora de egreso en la table ingresos
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean reversarFechaHoraEgreso(Connection con, int idCuenta)
	{
	    return SqlBaseIngresoGeneralDao.reversarFechaHoraEgreso(con, idCuenta);
	}
	
	
	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario) 
	{
		return SqlBaseIngresoGeneralDao.ingresarInfoAdicionalPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, ingresarInfoAdicionalPolizaStr);
	}
	
	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPolizaBasica(Connection con, int codigoConvenio, int idCuenta, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario)
	{
		return SqlBaseIngresoGeneralDao.ingresarInfoAdicionalPolizaBasica(con, codigoConvenio, idCuenta, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, ingresarInfoAdicionalPolizaStr);
	}
	
	/**
	 * Método para consultar los datos del titular de la poliza
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarInformacionTitularPoliza(Connection con, int idCuenta)
	{
		return SqlBaseIngresoGeneralDao.consultarInformacionTitularPoliza(con, idCuenta);
	}
	
	/**
	 * Metodo para consultar los datos de autorizacion de la poliza
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarDatosAutorizacionPoliza(Connection con, int codigoConvenio,int idCuenta)
	{
		return SqlBaseIngresoGeneralDao.consultarDatosAutorizacionPoliza(con, codigoConvenio, idCuenta);
	}
	
	/**
	 * Meodo para actualizar los datos del titular de la poliza segun la cuenta
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param usuario
	 * @return
	 */
	public int actualizarInfoTitularPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono) 
	{
		return SqlBaseIngresoGeneralDao.actualizarInfoTitularPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono);
	}
	
	/**
    * Método apra actualizar los datos de la autorizacion de la poliza segun una cuenta dada
    * @param con
    * @param idCuenta
    * @param fechaAutorizacion
    * @param numeroAutorizacion
    * @param valorMonto
    * @return
    */
	public int actualizarDatosAutorizacion(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux)
	{
		return SqlBaseIngresoGeneralDao.actualizarDatosAutorizacion(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, codigo, numeroAutoAux,  montoAux, ingresarInfoAdicionalPolizaStr);
	}
	/**
	 * Método para actualizar los datos de la poliza
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int actualizarInformacionPoliza(Connection con, int idCuenta, int codigoConvenio, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux) 
	{
		return SqlBaseIngresoGeneralDao.actualizarInformacionPoliza(con, idCuenta, codigoConvenio, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, codigo, numeroAutoAux,  montoAux, ingresarInfoAdicionalPolizaStr);
	}
	/**
	 * Elmimanr un monto autrorizado
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarDatosInformacionPoliza(Connection con, int codigo)
	{
		return SqlBaseIngresoGeneralDao.eliminarDatosInformacionPoliza(con, codigo);
	}
	
	/**
	 * retorna el codigo de persona
	 * @param con
	 * @param idIngreso
	 */
	public int cargarPacienteDadoIngreso(Connection con, String idIngreso)
	{
		return SqlBaseIngresoGeneralDao.cargarPacienteDadoIngreso(con, idIngreso);
	}
	
	/**
	 * metodo para cargar los ingresos y cuentas corresponduientes dados los estados,
	 * key={idingreso, nombreestadoingreso, codigoestadocuenta, nombreestadocuenta, codigoviaingreso, nombreviaingreso}
	 * @param con
	 * @param estadosIngresoVector
	 * @param estadosCuentaVector
	 * @return
	 */
	public HashMap obtenerIngresosCuenta(Connection con, Vector estadosIngresoVector, Vector estadosCuentaVector)
	{
		return SqlBaseIngresoGeneralDao.obtenerIngresosCuenta(con, estadosIngresoVector, estadosCuentaVector);
	}
	
	/**
	 * Método que carga un ingreso incompleto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap<String, Object> cargarIngresoIncompleto(Connection con,String codigoPaciente)
	{
		return SqlBaseIngresoGeneralDao.cargarIngresoIncompleto(con, codigoPaciente);
	}
	
	/**
	 * Método que realiza la anulacion de un ingreso que estaba incompleto por garantías
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anulacionIngresoIncompleto(Connection con,HashMap campos)
	{
		return SqlBaseIngresoGeneralDao.anulacionIngresoIncompleto(con, campos);
	}
	
	/**
	 * Método que consulta el consecutivo del ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public String getConsecutivoIngreso(Connection con,String idIngreso)
	{
		return SqlBaseIngresoGeneralDao.getConsecutivoIngreso(con, idIngreso);
	}
	
	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public boolean actualizarEstadoIngreso(Connection con, String idIngreso, String estado, String loginUsuario)
	{
		return SqlBaseIngresoGeneralDao.actualizarEstadoIngreso(con, idIngreso, estado, loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean existeIngresoAbierto(Connection con, String codigoPaciente, String idIngresoRestriccion)
	{
		return SqlBaseIngresoGeneralDao.existeIngresoAbierto(con, codigoPaciente, idIngresoRestriccion);
	}
	
	/**
	 * Metodo encargado de consultar si un ingreso fue por
	 * entidades subcontratadas o no.
	 * Adicionado por Jhony Alexander Duque A.
	 * 21/01/2008
	 * @param connection
	 * @param codigoIngreso
	 * @return S/N
	 * 
	 */
	public String esIngresoComoEntidadSubContratada (Connection connection, int codigoIngreso)
	{
		return SqlBaseIngresoGeneralDao.esIngresoComoEntidadSubContratada(connection, codigoIngreso, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaRestricciones
	 * @return
	 */
	public HashMap<Object, Object> cargarListadoIngresos(Connection con, HashMap<Object, Object> mapaRestricciones)
	{
		///PRIMERO OBTENEMOS LOS INGRESOS CON CUENTAS SIN ASOCIOS
		String consulta="";
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
		
		consulta+=" SELECT tabla.* FROM " +
										"(" +
											"(" +
												"SELECT " +
													"i.id as ingreso, " +
													"c.via_ingreso as viaingreso, " +
													"getnombreviaingreso(c.via_ingreso) as nombreviaingreso, " +
													"c.tipo_paciente as tipopaciente, "+
													"getnombretipopaciente(c.tipo_paciente) as nombretipopaciente, " +
													"i.consecutivo as numeroingreso, " +
													"getintegridaddominio(i.estado) as nombreestadoingreso, " +
													"to_char(i.fecha_ingreso, 'DD/MM/YYYY') as fechaingreso, " +
													"substr(i.hora_ingreso, 1,5) as horaingreso, " +
													"getnomcentrocosto(c.area) as nombrecentrocostoingreso, " +
													"getnomcentroatencion(i.centro_atencion) as nombrecentroatencion, " +
													"to_char(e.fecha_grabacion, 'DD/MM/YYYY') as fechaegreso, " +
													"substr(e.hora_grabacion, 1, 5) as horaegreso,  " +
													"getnomcentrocostoegreso(c.id) as nombrecentrocostoegreso, "+
													"'"+ConstantesBD.acronimoNo+"' as escuentaasocio " +
												"FROM " +
													"cuentas c " +
													"INNER JOIN ingresos i ON(c.id_ingreso=i.id) " +
													"LEFT OUTER JOIN egresos e ON(e.cuenta=c.id and e.destino_salida is not null) " +
												"WHERE " +
													"i.codigo_paciente=? " +
													"and getEsCuentaAsocio(c.id)='"+ConstantesBD.acronimoNo+"' " +
													"and i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
													"and " +
														"(" +
															"(" +
																"c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+"" +
															") " +
															"or " +
															"( " +
																"c.via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" " +
																"and " +
																	"(" +
																		"getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirCamaObservacion+")='"+ConstantesBD.acronimoSi+"' " +
																		"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria+")='"+ConstantesBD.acronimoSi+"' " +
																		"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirHospitalizarPiso+")='"+ConstantesBD.acronimoSi+"' " +
																		"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial+")='"+ConstantesBD.acronimoSi+"'" +
																	")" +
															")" +
														") ";
			
												if(!UtilidadTexto.isEmpty(mapaRestricciones.get("centroatencion")+""))
												{
													if(Integer.parseInt(mapaRestricciones.get("centroatencion")+"")>0)
													{	
														consulta+=	"and i.centro_atencion= "+mapaRestricciones.get("centroatencion")+"";
													}	
												}
		//SEGUNDO CUENTAS CON ASOCIOS, (LOS DATOS DEL INGRESO CORRESPONDEN A LA PRIMERA CUENTA Y LOS DATOS DEL EGRESO A LOS DE LA ULTIMA CUENTA)
								consulta+=" ) " +
											"UNION " +
											"(" +
												"SELECT " +
													"i.id as ingreso, " +
													""+ConstantesBD.codigoNuncaValido+" as viaingreso, " +
													"getnombreviaingreso(getInfoAsocio( i.id, 1 )::integer) as nombreviaingreso, " +
													"'' as tipopaciente, " +
													"getnombretipopaciente(getInfoAsocio( i.id, 2)) as nombretipopaciente, " +
													"i.consecutivo as numeroingreso, " +
													"getintegridaddominio(i.estado) as nombreestadoingreso, " +
													"to_char(i.fecha_ingreso, 'DD/MM/YYYY') as fechaingreso, " +
													"substr(i.hora_ingreso, 1,5) as horaingreso, " +
													"getnomcentrocosto(getInfoAsocio( i.id, 3)::integer) as nombrecentrocostoingreso, " +
													"getnomcentroatencion(i.centro_atencion) as nombrecentroatencion, " +
													"getInfoAsocio( i.id, 4 ) as fechaegreso, " +
													"getInfoAsocio( i.id, 5) as horaegreso,  " +
													"getInfoAsocio( i.id, 6) as nombrecentrocostoegreso,  " +
													"'"+ConstantesBD.acronimoSi+"' as escuentaasocio " +
												"FROM " +
													"cuentas c " +
													"INNER JOIN ingresos i ON(c.id_ingreso=i.id) " +
												"WHERE " +
													"i.codigo_paciente=? " +
													"and getEsCuentaAsocio(c.id)='"+ConstantesBD.acronimoSi+"' "+
													"and i.estado in('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
													"and " +
													"(" +
														"(" +
															"c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+"" +
														") " +
														"or " +
														"( " +
															"c.via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" " +
															"and " +
																"(" +
																	"getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirCamaObservacion+")='"+ConstantesBD.acronimoSi+"' " +
																	"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria+")='"+ConstantesBD.acronimoSi+"' " +
																	"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirHospitalizarPiso+")='"+ConstantesBD.acronimoSi+"' " +
																	"or getExisteConductaSeguirUrg(i.id, "+ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial+")='"+ConstantesBD.acronimoSi+"'" +
																")" +
														")" +
													")";
		
										
												if(!UtilidadTexto.isEmpty(mapaRestricciones.get("centroatencion")+""))
												{
													if(Integer.parseInt(mapaRestricciones.get("centroatencion")+"")>0)
													{
														consulta+=	"and i.centro_atencion= "+mapaRestricciones.get("centroatencion")+"";
													}	
												}	
												consulta+=" group by 1,2,3,4,5,6,7,8,9,10,11 " +
											")" +
										")tabla order by numeroingreso desc ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Integer.parseInt(mapaRestricciones.get("codigopaciente").toString()));
			ps.setInt(2, Integer.parseInt(mapaRestricciones.get("codigopaciente").toString()));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;										
	}

	public boolean actualizarControlPostOperatorioCx(Connection con, String idIngreso, String estado, String loginUsuario) {
		return SqlBaseIngresoGeneralDao.actualizarControlPostOperatorioCx(con, idIngreso, estado, loginUsuario);
	}
	
	/**
	 * Metodo encargado de actualizar el campo transplante de la
	 * tabla ingresos. 
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * --transplante --> puede ser (DONANT,RECEPT)
	 * --ingreso
	 * @return true/false
	 */
	public boolean actualizarTransplante (Connection connection, HashMap criterios)
	{
		return SqlBaseIngresoGeneralDao.actualizarTransplante(connection, criterios);
	}
	
	@Override
	public ArrayList<DtoIngresosFactura> obtenerIngresosFacturaOdontologica(
			int codigoPaciente) {
		return SqlBaseIngresoGeneralDao.obtenerIngresosFacturaOdontologica(codigoPaciente);
	}
}