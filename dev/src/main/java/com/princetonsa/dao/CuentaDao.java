/*
 * @(#)CuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import util.Answer;
import util.InfoDatosInt;
import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoDatosResponsableFacturacion;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoInstitucion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Cuenta</code>.
 *
 * @version 1.0, Oct 7, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface CuentaDao {

	
	/**
	 * Carga una cuenta desde la fuente de datos.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta n�mero de identificaci�n de esta cuenta en la fuente de datos
	 * @return objeto <code>Answer</code> con la nformaci�n solicitada y una conexi�n abierta con la fuente de datos
	 */
	public Answer cargarCuenta(Connection con, String idCuenta) throws SQLException;
	
	/**
	 * Carga informacion basica de la cuenta 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta numero de identificacion de esta cuenta en la fuente de datos
	 * @param idIngreso numero de ingreso de la cuenta en la fuente de datos
	 * @param idConvenio numero de convenio de la sub cuenta en la fuente de datos
	 * @return objeto <code>Answer</code> con la nformaci�n solicitada y una conexi�n abierta con la fuente de datos
	 */
	public Answer cargarAfiliacionClasificacionSocEconomica(Connection con, String idCuenta,String idIngreso, String idConvenio)throws SQLException;

	
	/**
	 * M�todo que retorna la fecha de creaci�n de una cuenta
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta N�mero de la cuenta a la cual se le busca
	 * la fecha de creaci�n
	 * @return
	 * @throws SQLException
	 */
	public String buscarFechaCreacionCuenta (Connection con, int idCuenta) throws SQLException;
	
	/**
	 * M�todo que cambia el estado de una cuenta a asociada,
	 * creando un registro de asocios cuenta
	 * (Todo el proceso interno debe ser una transacci�n, no requiere
	 * unir otros procesos a esta transacci�n)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta N�mero de la cuenta a la cual se quiere
	 * dejar en estado asociado
	 * @param usuario Login del usuario que realiza el asocio de
	 * cuenta
	 * @return
	 * @throws SQLException
	 */
	public int asociarCuenta (Connection con, int idCuenta, int idIngreso, String usuario) throws SQLException;
	
	/**
	 * M�todo que deshace  el cambio hecho al asociar una cuenta,
	 * inactivando el registro de asocios cuenta
	 * (Todo el proceso interno debe ser una transacci�n, no requiere
	 * unir otros procesos a esta transacci�n)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente C�digo del paciente sobre el que se
	 * desea realizar la operaci�n
	 * @param usuario Login del usuario que realiza el asocio de
	 * cuenta
	 * @return
	 * @throws SQLException
	 */
	public int desAsociarCuenta (Connection con, String usuario, String idIngreso, String viaIngreso) throws SQLException;

	/**
	 * M�todo que completa el proceso de asocio de cuenta (almacenar
	 * la cuenta final), soportando definici�n de estado de transacci�n en
	 * una transacci�n mayor. Retorna el n�mero de la cuenta original
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente C�digo del paciente sobre el que se
	 * desea realizar la operaci�n
	 * @param nuevaCuenta C�digo de la cuenta final
	 * @param estado Estado de la transacci�n
	 * @return
	 * @throws SQLException
	 */
	public int completarAsocioCuentaTransaccional(Connection con, int nuevaCuenta, String estado, String idIngreso, String viaIngreso) throws SQLException;	

	/**
	 * M�todo que busca todas las cuentas para un ingreso en 
	 * particular
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso C�digo del ingreso para el que se desean
	 * buscar cuentas
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator getCodigosCuentasIngreso(Connection con, int idIngreso) throws SQLException;
	
	/**
	 * M�todo que devuelve el nombre del tratante para la cuenta
	 * especificada
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta
	 * @param codigoCentroCostoTratante
	 * @return
	 * @throws SQLException
	 */
	public String getCentroCostoTratante(Connection con, int idCuenta, int codigoCentroCostoTratante) throws SQLException;
	
	/**
	 * M�todo que cambia el estado de la cuenta 
	 * @param con
	 * @param codigoEstadoCuenta
	 * @param idCuenta
	 * @return
	 */
	public int cambiarEstadoCuenta(	Connection con, int codigoEstadoCuenta, int idCuenta);
	
	
	/**
	 * Metodo que retorna el valor total de una cuenta.
	 * @param con, Conexion
	 * @param idCuenta, id Cuenta.
	 * @return
	 */
	public float getValorTotalCuenta(Connection con, int idCuenta);
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo usado para cargar la �ltima cuenta de urgencias del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String cargarCuentaUrgenciasAsociada(Connection con,int codigoIngreso);
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo para registrar el LOG tipo base de datos del cierre de la cuenta
	 * en el m�dulo de facturaci�n
	 * @param con
	 * @param idCuenta
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @return
	 */
	public int registrarCierreCuenta(Connection con,int idCuenta, String usuario, String motivo, String estado);
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo para consultar las cuentas cerradas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return listado de cuentas cerradas
	 */
	public ResultSetDecorator consultarCuentasCerradasPaciente(Connection con,int codigoPaciente);
	
	/** 
	 * Adici�n Sebasti�n
	 * M�todo para consultar las cuentas cerradas por rangos de fechas y/o id's de cuentas
	 * @param con
	 * @param fechaCierreInicial
	 * @param fechaCierreFinal
	 * @param idCuentaInicial
	 * @param idCuentaFinal
	 * @param centroAtencion 
	 * @param codigoEntidadSub
	 * @return listado de cuentas cerradas (cuenta,paciente(nombre,id,numero),via_ingreso,responsable,fecha_apertura,fecha_hora_cierre,usuario,motivo)
	 */
	public ResultSetDecorator consultarCuentasCerradas(Connection con,String fechaCierreInicial,String fechaCierreFinal,int idCuentaInicial,int idCuentaFinal, int centroAtencion,String codigoEntidadSub);

	/**
	 * M�todo que deshace el asocio en la facturacion independiente
	 * @param con
	 * @param codigoCuentaFinal
	 * @param usuario
	 * @return
	 */
	public int desAsociarCuentaFacturacion(Connection con, int codigoCuentaFinal, String usuario);
	
	/**
	 * M�todo implementado para consultar el listado de cuentas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaInicial
	 * @param cuentaFinal
	 * @param estadoCuenta
	 * @param usuario
	 * @param viaIngreso
	 * @param convenio
	 * @param centroAtencion 
	 * @paran codigoEntidadSubContratada
	 * @return
	 */
	public HashMap consultarCuentas(Connection con,String fechaInicial,String fechaFinal,int cuentaInicial,int cuentaFinal,int estadoCuenta,String usuario,int viaIngreso,String tipoPaciente,int convenio, int centroAtencion,String codigoTipoEvento,String codigoEntidadSub);
	
	/**
	 * M�todo implementado para consultar las cuentas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarCuentas(Connection con,int codigoPaciente);
	
	
	/**
	 * M�todo implementado para guardar la informaci�n de la cuenta
	 * @param con
	 * @return
	 */
	public ResultadoBoolean guardar(Connection con,DtoCuentas cuenta);
	
	/**
	 * M�todo que carga la informacion de la cuenta con sus convenios
	 * @param con
	 * @param idCuenta
	 * @return
	 * Nota * Si no se encuentra la cuenta el atributo idCuenta es vac�o
	 */
	public DtoCuentas cargar(Connection con,String idCuenta);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoViaIngresoCuenta(Connection con, String idCuenta) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerTipoComplejidad (Connection con, String idCuenta);
	
	/**
	 * M�todo que consulta los datos generales de las cuentas de un asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> listarCuentasAsocio(Connection con,String idIngreso);
	
	/**
	 * M�todo que consulta el tipo de evento de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerCodigoTipoEventoCuenta(Connection con,String idCuenta);
	
	/**
	 * M�todo que actualiza el tipo evento de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarTipoEventoCuenta(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si se puede modificar el �rea de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean puedoModificarAreaCuenta(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCentroAtencionCuenta (Connection con, String idCuenta);
	
	/**
	 * M�todo para actualizar el convenio arp afiliado de la cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarConvenioArpAfiliadoCuenta(Connection con,HashMap campos);
	
	/**
	 * Actualizar el n�mero de la p�liza en el registro de accidentes de tr�nsito
	 * @param con Conexi�n con la BD
	 * @param idIngreso c�digo del ingreso
	 * @param convenio c�digo del convenio
	 * @param nroPoliza N�mero de p�liza a actualizar
	 * @return N�mero de elementos actualizados
	 */
	public int actualizarNroPolizaConvenioAccidenteTransito(Connection con, int idIngreso, int convenio, String nroPoliza);
	
	/**
	 * M�todo que actualiza el convenio en los cargos, cuando se realiza modificacion del convenio
	 * en la modificacion de cuentas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarConvenioEnCargos(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param codCuenta
	 * @return
	 */
	public boolean esCuentaFinalAsocio(Connection con, int codCuenta);
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param esAsocio
	 * @return
	 */
	public HashMap<Object, Object> obtenerViasIngresoTipoPacDadoIngreso(Connection con, int numeroIngreso, String esAsocio);

	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param viasIngreso
	 * @return
	 */
	public Vector<String> obtenerCuentasIngreso(Connection con, int numeroIngreso, boolean epicrisis);
	
	
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
	 public boolean trasladarCuentaAingreso (Connection connection,HashMap datos) throws IPSException;
	 
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
	 public boolean asociosCuentaTotal (Connection connection,HashMap datos);

	 /**
	  * 
	  * @param idCuenta
	  * @return
	  */
	 public String obtenerFechaVigenciaTopeCuenta(String idCuenta);

	 /**
		 * Consultar area(centro de costos) y via ingreso de una cuenta
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @return [idCentroCosto,nombreCentroCosto,idViaIngreso,nombreViaIngreso]
	 * @throws Exception 
	 * @throws SQLException 
		 */
	public String[] consultarAreaYViaIngreso(Connection con, int codigoCuenta) throws SQLException, Exception;
	 
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public String obtenerIngresoXNumeroFactura(Connection con,String numeroFactura) throws SQLException;	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public    DtoInstitucion consultarDatosInstitucionXFactura(Connection con,String numeroFactura) throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerDatosAnulacionFactura(Connection con,String numeroFactura) throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerCentroAtencionFactura(Connection con,String numeroFactura) throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerNumeroFacturaAsociada(Connection con,String numeroFactura) throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  DtoDatosResponsableFacturacion obtenerResponsable(Connection con,String numeroFactura) throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerFechaGeneracionFactura(Connection con,String numeroFactura) throws SQLException;
	
	
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> articulosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> serviciosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException;
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> cirugiasFacturaAgrupada(Connection con,String numeroFactura,Integer codigoInstitucion)throws SQLException;
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> paquetesFacturaAgrupada(Connection con,String numeroFactura)throws SQLException;
	
	
	/**
	 * @param con
	 * @param codigoConvenio
	 * @return
	 * @throws SQLException
	 */
	public  boolean formatoValoresFormatoFacturaAgrupada(Connection con,String codigoConvenio)throws SQLException;
	
	/**
	 * @param con
	 * @param convenioId
	 * @param ingresoConsecutivo
	 * @return
	 * @throws SQLException
	 */
	public  Integer consultarNumeroAutorizacion(Connection con,Integer convenioId, Integer ingresoConsecutivo) throws SQLException;

	/**
	 * MT 5568
	 * Retorna un {@link InfoDatosInt} con los datos del centro de costo (o area) en la cual est� el 
	 * paciente
	 * @param con
	 * @param idSolicitud
	 * @return
	 * @throws IPSException
	 */
	public InfoDatosInt obtenerDatosCentroCostoXSolcitud(Connection con,int idSolicitud) throws IPSException;
	
}