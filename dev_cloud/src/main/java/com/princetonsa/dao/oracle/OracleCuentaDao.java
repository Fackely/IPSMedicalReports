/*
 * @(#)OracleCuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import util.Answer;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.CuentaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseCuentaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoDatosResponsableFacturacion;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoInstitucion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
/**
 * Esta clase implementa el contrato estipulado en <code>CuentaDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>Cuenta</code>
 *
 * A diferencia de paciente o usuario, en esta clase no inicia una transacciï¿½n, pero si la puede
 * terminar en caso de error.
 *
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleCuentaDao implements CuentaDao{//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Cadena constante con el <i>statement</i> necesario para dejar una cuenta en estado asociado usando una BD Genï¿½rica.
	 */
	private static final String crearEntradaAsocioCuentaStr="INSERT INTO asocios_cuenta (codigo, fecha, hora, cuenta_inicial, usuario, activo, ingreso) values (seq_asocios_cuenta.nextval, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, " + ValoresPorDefecto.getValorTrueParaConsultas() + ",?)";
	
	




	/**
	 * Carga una cuenta desde la base de datos OracleSQL.
	 * @param con una conexion abierta con una base de datos OracleSQL
	 * @param idCuenta nï¿½mero de identificaciï¿½n de esta cuenta en la base de datos OracleSQL
	 * @return objeto <code>Answer</code> con la nformaciï¿½n solicitada y una conexiï¿½n abierta con la base de datos OracleSQL
	 */
	public Answer cargarCuenta(Connection con, String idCuenta) throws SQLException
	{
		return SqlBaseCuentaDao.cargarCuenta(con, idCuenta) ;
	}
	
	/**
	 * Carga informacion basica de la cuenta 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta numero de identificacion de esta cuenta en la fuente de datos
	 * @param idIngreso numero de ingreso de la cuenta en la fuente de datos
	 * @param idConvenio numero de convenio de la sub cuenta en la fuente de datos
	 * @return objeto <code>Answer</code> con la nformaciï¿½n solicitada y una conexiï¿½n abierta con la fuente de datos
	 */
	public Answer cargarAfiliacionClasificacionSocEconomica(Connection con, String idCuenta,String idIngreso, String idConvenio)throws SQLException{
		return SqlBaseCuentaDao.cargarAfiliacionClasificacionSocEconomica(con, idCuenta, idIngreso,idConvenio) ;
	}

	/**
	 * Implementaciï¿½n de la inserciï¿½n de la busqueda de la fecha
	 * de creaciï¿½n de la cuenta en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.CuentaDao#buscarFechaCreacionCuenta (Connection, int)
	 */
	public String buscarFechaCreacionCuenta (Connection con, int idCuenta) throws SQLException
	{
		return SqlBaseCuentaDao.buscarFechaCreacionCuenta (con, idCuenta) ;
	}

	/**
	 * Implementaciï¿½n de la inserciï¿½n de la busqueda de la fecha
	 * de creaciï¿½n de la cuenta en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.CuentaDao#asociarCuenta (con, int, String ) throws SQLException
	 */
	public int asociarCuenta (Connection con, int idCuenta,int IdIngreso, String usuario) throws SQLException
	{
		return SqlBaseCuentaDao.asociarCuenta (con, idCuenta,IdIngreso, usuario, crearEntradaAsocioCuentaStr);
	}
	
	/**
	 * Implementaciï¿½n de la funcionalidad de asociar 
	 * cuenta en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.CuentaDao#asociarCuenta (con, int, String ) throws SQLException
	 */
	public int desAsociarCuenta (Connection con, String usuario, String idIngreso, String viaIngreso) throws SQLException
	{
		return SqlBaseCuentaDao.desAsociarCuenta (con, usuario, idIngreso, viaIngreso);
	}

	/**
	 * Implementaciï¿½n del mï¿½todo que completa el asocio de cuenta
	 * transaccional en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.CuentaDao#completarAsocioCuentaTransaccional(Connection , int , int , String ) throws SQLException
	 */
	public int completarAsocioCuentaTransaccional(Connection con, int nuevaCuenta, String estado, String idIngreso, String viaIngreso) throws SQLException
	{
		return SqlBaseCuentaDao.completarAsocioCuentaTransaccional(con, nuevaCuenta, estado, idIngreso, viaIngreso) ;
	}
	
	/**
	 * Implementaciï¿½n de la consulta de codigos de cuentas asociados a un numero de ingreso dado
	 * @see com.princetonsa.dao.CuentaDao#getCodigosCuentasIngreso (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public ResultSetDecorator getCodigosCuentasIngreso(Connection con, int idIngreso) throws SQLException
	{
		return SqlBaseCuentaDao.getCodigosCuentasIngreso(con, idIngreso) ;
	}
	
	/**
	 * Implementaciï¿½n de la consulta del centro de costo tratante para un numero de cuenta dado
	 * @see com.princetonsa.dao.CuentaDao#getCodigosCuentasIngreso (con, int ) throws SQLException
	 * @version Sept. 23 de 2003
	 */	
	public String getCentroCostoTratante(Connection con, int idCuenta, int codigoCentroCostoTratante) throws SQLException
	{
		return SqlBaseCuentaDao.getCentroCostoTratante(con, idCuenta, codigoCentroCostoTratante) ;
	}
	
	/**
	 * Mï¿½todo que cambia el estado de la cuenta 
	 * @param con
	 * @param codigoEstadoCuenta
	 * @param idCuenta
	 * @return
	 */
	public int cambiarEstadoCuenta(	Connection con, int codigoEstadoCuenta, int idCuenta)
	{
	    return SqlBaseCuentaDao.cambiarEstadoCuenta(con, codigoEstadoCuenta, idCuenta);
	}

	/**
	 * Metodo que retorna el valor total de una cuenta.
	 * @param con, Conexion
	 * @param idCuenta, id Cuenta.
	 * @return
	 */
	public float getValorTotalCuenta(Connection con, int idCuenta)
	{
		return SqlBaseCuentaDao.getValorTotalCuenta(con,idCuenta);
	}
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para cargar la ï¿½ltima cuenta asociada de urgencias del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String cargarCuentaUrgenciasAsociada(Connection con,int codigoIngreso){
		return SqlBaseCuentaDao.cargarCuentaUrgenciasAsociada(con,codigoIngreso);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para registrar el LOG tipo base de datos del cierre de la cuenta
	 * en el mï¿½dulo de facturaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @return
	 */
	public int registrarCierreCuenta(Connection con,int idCuenta, String usuario, String motivo, String estado){
		return SqlBaseCuentaDao.registrarCierreCuenta(con,idCuenta,usuario,motivo,estado);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return listado de cuentas cerradas
	 */
	public ResultSetDecorator consultarCuentasCerradasPaciente(Connection con,int codigoPaciente){
		return SqlBaseCuentaDao.consultarCuentasCerradasPaciente(con,codigoPaciente);
	}
	
	/**
	 *  Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas por rangos de fechas y/o id's de cuentas
	 * @param con
	 * @param fechaCierreInicial
	 * @param fechaCierreFinal
	 * @param idCuentaInicial
	 * @param idCuentaFinal
	 * @param String codigoEntidadSub
	 * @return listado de cuentas cerradas (cuenta,paciente(nombre,id,numero),via_ingreso,responsable,fecha_apertura,fecha_hora_cierre,usuario,motivo)
	 */
	public ResultSetDecorator consultarCuentasCerradas(Connection con,String fechaCierreInicial,String fechaCierreFinal,int idCuentaInicial,int idCuentaFinal, int centroAtencion, String codigoEntidadSub)
	{
		return SqlBaseCuentaDao.consultarCuentasCerradas(con,fechaCierreInicial,fechaCierreFinal,idCuentaInicial,idCuentaFinal, centroAtencion,codigoEntidadSub); 
	}
	
	/**
	 * Mï¿½todo que deshace el asocio en la facturacion independiente
	 * @param con
	 * @param codigoCuentaFinal
	 * @param usuario
	 * @return
	 */
	public int desAsociarCuentaFacturacion(Connection con, int codigoCuentaFinal, String usuario)
	{
		return SqlBaseCuentaDao.desAsociarCuentaFacturacion(con, codigoCuentaFinal, usuario);
	}
	
	/**
	 * Mï¿½todo implementado para consultar el listado de cuentas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaInicial
	 * @param cuentaFinal
	 * @param estadoCuenta
	 * @param usuario
	 * @param viaIngreso
	 * @param convenio
	 * @param String codigoEntidadSubContratada
	 * @return
	 */
	public HashMap consultarCuentas(Connection con,String fechaInicial,String fechaFinal,int cuentaInicial,int cuentaFinal,int estadoCuenta,String usuario,int viaIngreso,String tipoPaciente,int convenio, int centroAtencion,String codigoTipoEvento, String codigoEntidadSubContratada)
	{
		return SqlBaseCuentaDao.consultarCuentas(con,fechaInicial,fechaFinal,cuentaInicial,cuentaFinal,estadoCuenta,usuario,viaIngreso,tipoPaciente,convenio, centroAtencion, codigoTipoEvento, codigoEntidadSubContratada);
	}
	
	/**
	 * Mï¿½todo implementado para consultar las cuentas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarCuentas(Connection con,int codigoPaciente)
	{
		return SqlBaseCuentaDao.consultarCuentas(con,codigoPaciente);
	}

	
	
	/**
	 * Método implementado para guardar la información de la cuenta
	 * @param con
	 * @return
	 */
	public ResultadoBoolean guardar(Connection con,DtoCuentas cuenta)
	{
		return SqlBaseCuentaDao.guardar(con, cuenta, "seq_cuentas.nextval", "seq_sub_cuentas.nextval","seq_informacion_poliza.nextval","seq_responsables_pacientes.nextval", DaoFactory.ORACLE);
	}
	
	/**
	 * Método que carga la informacion de la cuenta con sus convenios
	 * @param con
	 * @param idCuenta
	 * @return
	 * Nota * Si no se encuentra la cuenta el atributo idCuenta es vacío
	 */
	public DtoCuentas cargar(Connection con,String idCuenta)
	{
		return SqlBaseCuentaDao.cargar(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoViaIngresoCuenta(Connection con, String idCuenta) throws BDException
	{
		return SqlBaseCuentaDao.obtenerCodigoViaIngresoCuenta(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerTipoComplejidad (Connection con, String idCuenta)
	{
		return SqlBaseCuentaDao.obtenerTipoComplejidad(con, idCuenta);
	}
	
	/**
	 * Método que consulta los datos generales de las cuentas de un asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> listarCuentasAsocio(Connection con,String idIngreso)
	{
		return SqlBaseCuentaDao.listarCuentasAsocio(con, idIngreso);
	}
	
	/**
	 * Método que consulta el tipo de evento de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerCodigoTipoEventoCuenta(Connection con,String idCuenta)
	{
		return SqlBaseCuentaDao.obtenerCodigoTipoEventoCuenta(con, idCuenta);
	}
	
	/**
	 * Método que actualiza el tipo evento de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarTipoEventoCuenta(Connection con,HashMap campos)
	{
		return SqlBaseCuentaDao.actualizarTipoEventoCuenta(con, campos);
	}
	
	/**
	 * Método que verifica si se puede modificar el área de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean puedoModificarAreaCuenta(Connection con,HashMap campos)
	{
		return SqlBaseCuentaDao.puedoModificarAreaCuenta(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCentroAtencionCuenta (Connection con, String idCuenta)
	{
		return SqlBaseCuentaDao.obtenerCentroAtencionCuenta(con, idCuenta);
	}
	
	/**
	 * Método para actualizar el convenio arp afiliado de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarConvenioArpAfiliadoCuenta(Connection con,HashMap campos)
	{
		return SqlBaseCuentaDao.actualizarConvenioArpAfiliadoCuenta(con, campos);
	}
	
	@Override
	public int actualizarNroPolizaConvenioAccidenteTransito(Connection con, int idIngreso, int convenio, String nroPoliza)
	{
		return SqlBaseCuentaDao.actualizarNroPolizaConvenioAccidenteTransito(con, idIngreso, convenio, nroPoliza);
	}
	
	/**
	 * Método que actualiza el convenio en los cargos, cuando se realiza modificacion del convenio
	 * en la modificacion de cuentas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarConvenioEnCargos(Connection con,HashMap campos)
	{
		return SqlBaseCuentaDao.actualizarConvenioEnCargos(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codCuenta
	 * @return
	 */
	public boolean esCuentaFinalAsocio(Connection con, int codCuenta)
	{
		return SqlBaseCuentaDao.esCuentaFinalAsocio(con, codCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param esAsocio
	 * @return
	 */
	public HashMap<Object, Object> obtenerViasIngresoTipoPacDadoIngreso(Connection con, int numeroIngreso, String esAsocio)
	{
		return SqlBaseCuentaDao.obtenerViasIngresoTipoPacDadoIngreso(con, numeroIngreso, esAsocio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param viasIngreso
	 * @return
	 */
	public Vector<String> obtenerCuentasIngreso(Connection con, int numeroIngreso, boolean epicrisis)
	{
		return SqlBaseCuentaDao.obtenerCuentasIngreso(con, numeroIngreso, epicrisis);
	}
	
	
	 /**
	  * Metodo encargado de trasladarle una cuenta a un ingreso
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * -----------------------------
	  * KEY'S DEL MAPA DATOS
	  * -----------------------------
	  * -- ingresoTraslado (Requerido)--> ingreso a donde se va a trasladar la cuenta
	  * -- estadoCuenta (Requerido) --> estado en el cual queda la cuenta 
	  * -- ingresoCuenta (Requerido) -->  ingreso donde se encuentra la cuenta a trasladar
	  * 
	  * @return
	  */
	 public boolean trasladarCuentaAingreso (Connection connection,HashMap datos) throws IPSException
	 {
		 return SqlBaseCuentaDao.trasladarCuentaAingreso(connection, datos);
	 }

	 
	 /**
	  * Metodo encargado de realizar un asocio de cuentas 
	  * ingresando la cuenta inicial y final 
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * ------------------------------
	  * KEY'S DEL MAPA DATOS
	  * ------------------------------
	  * -- usuario (Requerido)  
	  * -- cuentaInicial (Requerido)  
	  * -- cuentaFinal (Requerido)  
	  * -- activo (Requerido) 
	  * -- ingreso (Requerido) 
	  * 
	  * @return
	  */
	 public boolean asociosCuentaTotal (Connection connection,HashMap datos)
	 {
		 return SqlBaseCuentaDao.asociosCuentaTotal(connection, datos);
	 }
	 
	/**
	 * 
	 */
	public String obtenerFechaVigenciaTopeCuenta(String idCuenta) 
	{
		return SqlBaseCuentaDao.obtenerFechaVigenciaTopeCuenta(idCuenta);
	}

	@Override
	public String[] consultarAreaYViaIngreso(Connection con, int codigoCuenta) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return SqlBaseCuentaDao.consultarAreaYViaIngreso(con,codigoCuenta);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerIngresoXNumeroFactura(java.sql.Connection, java.lang.String)
	 */
	public String obtenerIngresoXNumeroFactura(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerIngresoXNumeroFactura(con, numeroFactura);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#consultarDatosInstitucionXFactura(java.sql.Connection, java.lang.String)
	 */
	public    DtoInstitucion consultarDatosInstitucionXFactura(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.consultarDatosInstitucionXFactura(con, numeroFactura);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerDatosAnulacionFactura(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerDatosAnulacionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerDatosAnulacionFactura(con, numeroFactura);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerCentroAtencionFactura(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerCentroAtencionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerCentroAtencionFactura(con, numeroFactura);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerNumeroFacturaAsociada(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerNumeroFacturaAsociada(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerNumeroFacturaAsociada(con, numeroFactura);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerResponsable(java.sql.Connection, java.lang.String)
	 */
	public  DtoDatosResponsableFacturacion obtenerResponsable(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerResponsable(con, numeroFactura);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerFechaGeneracionFactura(java.sql.Connection, java.lang.String)
	 */
	public  String obtenerFechaGeneracionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return SqlBaseCuentaDao.obtenerFechaGeneracionFactura(con, numeroFactura);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#articulosFacturaAgrupada(java.sql.Connection, java.lang.String)
	 */
	public  List<DtoFacturaAgrupada> articulosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return SqlBaseCuentaDao.articulosFacturaAgrupada(con, numeroFactura);
	}

	/**
	 * @see com.princetonsa.dao.CuentaDao#serviciosFacturaAgrupada(java.sql.Connection, java.lang.String)
	 */
	public  List<DtoFacturaAgrupada> serviciosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return SqlBaseCuentaDao.serviciosFacturaAgrupada(con, numeroFactura);
	}

	/**
	 * @see com.princetonsa.dao.CuentaDao#cirugiasFacturaAgrupada(java.sql.Connection, java.lang.String)
	 */
	public  List<DtoFacturaAgrupada> cirugiasFacturaAgrupada(Connection con,String numeroFactura,Integer codigoInstitucion)throws SQLException
	{
		return SqlBaseCuentaDao.cirugiasFacturaAgrupada(con, numeroFactura, codigoInstitucion);
	}
	

	/**
	 * @see com.princetonsa.dao.CuentaDao#paquetesFacturaAgrupada(java.sql.Connection, java.lang.String)
	 */
	public  List<DtoFacturaAgrupada> paquetesFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return SqlBaseCuentaDao.paquetesFacturaAgrupada(con, numeroFactura);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#formatoValoresFormatoFacturaAgrupada(java.sql.Connection, java.lang.String)
	 */
	public  boolean formatoValoresFormatoFacturaAgrupada(Connection con,String codigoConvenio)throws SQLException
	{
		return SqlBaseCuentaDao.formatoValoresFormatoFacturaAgrupada(con, codigoConvenio);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#consultarNumeroAutorizacion(java.sql.Connection, java.lang.Integer, java.lang.Integer)
	 */
	public  Integer consultarNumeroAutorizacion(Connection con,Integer convenioId, Integer ingresoConsecutivo) throws SQLException
	{
		return SqlBaseCuentaDao.consultarNumeroAutorizacion(con, convenioId, ingresoConsecutivo);
	}

	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerDatosCentroCostoXSolcitud(Connection, int)
	 */
	@Override
	public InfoDatosInt obtenerDatosCentroCostoXSolcitud(Connection con,int idSolicitud) throws IPSException {
		return SqlBaseCuentaDao.obtenerDatosCentroCostoXSolcitud(con,idSolicitud);
	}
	
}
