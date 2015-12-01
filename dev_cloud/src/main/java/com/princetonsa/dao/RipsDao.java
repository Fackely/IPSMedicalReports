/*
 * Created on Jun 10, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface RipsDao {
	
	/**
	 * Método para consultar los datos del archivo AF por Factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAF(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion, boolean esAxRips, String numeroFactura);
	
	/**
	 * Método para consultar los datos del archivo AF por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAF(Connection con,double numeroCuentaCobro,int codigoInstitucion);
	
	/**
	 * Método para consultar los registros del archivo AD por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAD(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los registros del archivo AD por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAD(Connection con,double numeroCuentaCobro,int codigoInstitucion);
	
	/**
	 * Método para consultar los registros del archivo US por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaUS(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los datos del archivo US por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroUS(Connection con,double numeroCuentaCobro,int codigoInstitucion);
	
	/**
	 * Método para consultar los datos del archivo AC por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @para tarifarioOficial
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAC(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,int tarifarioOficial, boolean esAxRips, String numeroFactura);
	
	/**
	 * Método para consultar los datos del archivo AC por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param tarifarioOficial
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAC(Connection con,double numeroCuentaCobro,int codigoInstitucion,int tarifarioOficial);
	
	/**
	 * Método para consultar los datos del archivo AH 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuenta cobro
	 * @param convenio
	 * @param esFactura
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAH(Connection con,String fechaInicial,String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los datos del archivo AU
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAU(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los datos del archivo AM
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAM(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los datos del archivo AT
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAT(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura,boolean esAxRips, String nroFacura);
	
	/**
	 * Método para consultar los datos del archivo AP
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	/*public HashMap consultaAP(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura);*/
	@SuppressWarnings("rawtypes")
	public HashMap consultaAP(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura, boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los datos del archivo AN
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAN(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura,boolean esAxRips, String nroFactura);
	
	/**
	 * Método para consultar los registros del RIPS por Rangos
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosPorRangos(Connection con,int codigoConvenio,String fechaInicial,String fechaFinal);
	
	/**
	 * Método usado para cargar la información rips de un cita.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsCita(Connection con,int numeroSolicitud);
	
	/**
	 * Método usado para cargar parte de los datos rips de un solicitud diferente a cita que tiene cargada un servicio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsNoCita(Connection con,int numeroSolicitud);
	
	/**
	 * Método usado para consultar servicios por codigo CUPS
	 * @param con
	 * @param codigoCups
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicios(Connection con,String codigoCups);
	
	/**
	 * Método para insertar los datos Rips de un registro tipo cita
	 * retorna el consecutivo automático del registro en la tabla 
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param tipoDiagnostico
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRel1
	 * @param cieRel1
	 * @param diagRel2
	 * @param cieRel2
	 * @param diagRel3
	 * @param cieRel3
	 * @param causaExterna
	 * @param finalidadConsulta
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @param paciente
	 * @param cuenta
	 * @param solicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatorRipsCita(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,int tipoDiagnostico,String diagPrincipal,int ciePrincipal,
			String diagRel1,int cieRel1,String diagRel2,int cieRel2,String diagRel3,int cieRel3,
			int causaExterna,String finalidadConsulta,double valorTotal,double valorCopago,
			double valorEmpresa,String autorizacion,int paciente,int cuenta,int solicitud,int institucion,String fechaAtencion);
	
	/**
	 * Método usado para insertar un registro RIPS que nos ea cita
	 * pero que tiene un servicio asociado, retorna el consecutivo automático
	 * del registro
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRelacionado
	 * @param cieRelacionado
	 * @param diagComplicacion
	 * @param cieComplicacion
	 * @param valorTotal
	 * @param autorizacion
	 * @param ambitoRealizacion
	 * @param personalAtiende
	 * @param formaRealizacion
	 * @param paciente
	 * @param cuenta
	 * @param solicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatosRipsNoCita(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,String diagPrincipal,int ciePrincipal,String diagRelacionado,
			int cieRelacionado,String diagComplicacion,int cieComplicacion,double valorTotal,
			String autorizacion,int ambitoRealizacion,int personalAtiende,int formaRealizacion,
			int paciente,int cuenta,int solicitud,int institucion,String fechaAtencion);
	
	/**
	 * Método para insertar un registro de una solicitud, o no solicitud
	 * ú registro OTRO que no tenían un servicio asociado
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRelacionado1
	 * @param cieRelacionado1
	 * @param diagRelacionado2
	 * @param cieRelacionado2
	 * @param diagRelacionado3
	 * @param cieRelacionado3
	 * @param causaExterna
	 * @param finalidadConsulta
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @param paciente
	 * @param cuenta
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatosRipsSinServicio(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,int tipoDiagnostico,String diagPrincipal,int ciePrincipal,String diagRelacionado1,
			int cieRelacionado1,String diagRelacionado2,int cieRelacionado2,
			String diagRelacionado3,int cieRelacionado3,int causaExterna,String finalidadConsulta,
			double valorTotal,double valorCopago,double valorEmpresa,String autorizacion,
			int paciente,int cuenta,int numeroSolicitud,int institucion,String fechaAtencion);
	
	/**
	 * Método que consulta la tarifa del servicio y calcula el valor del copago según la cuenta
	 * @param con
	 * @param codigoServicio
	 * @param idCuenta
	 * @return valor total (separadorSplit) valor copago
	 */
	public String consultarValoresServicio(Connection con,int codigoServicio,int idCuenta);
	
	/**
	 * Método para consultar los servicio por descripcion
	 * @param con
	 * @param criterio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicioXNombre(Connection con,String criterio);
	
	/**
	 * Método para cargar el resumen de un registro RIPS
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarRegistroRips(Connection con,int consecutivo);
	
	/**
	 * Método para actualizar los registros RIPS que van a hacer parte de la generación
	 * de archivos (tienen el checkbox RIPS activado)
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param fechaGeneracion
	 * @param login
	 * @return
	 */
	public int actualizarDatosRips(Connection con,int consecutivo, String numeroFactura,String fechaFactura,String fechaRemision,String fechaAtencion,String login);
	
	/**
	 * Método para aumentar el número de secuencia de la remisión
	 * @param con
	 * @return
	 */
	public int siguienteSecuenciaRemision(Connection con);
	
	/**
	 * Método para consultar los registros del archivo AF
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAF(Connection con,String numeroFactura,String numeroRemision);
	
	/**
	 * Método para consultar los registros del archivo AD
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAD(Connection con,String numeroFactura,String numeroRemision);
	
	/**
	 * Método para consultar los registros del archivo US
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosUS(Connection con,String numeroFactura,String numeroRemision);
	
	/**
	 * Método para consultar los registros del archivo AC
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAC(Connection con,String numeroFactura,String numeroRemision);
	
	/**
	 * Método para consultar los registros del archivo AP
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAP(Connection con,String numeroFactura,String numeroRemision);
	
	/**
	 * Método usado para consultar la generación de RIPS consultorios
	 * @param con
	 * @param codigoConvenio
	 * @param fechaFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaGeneracionRips(Connection con,int codigoConvenio,String fechaFactura);
	
	/**
	 * Método para actualizar los datos de un registro RIPS ya insertado
	 * @param con
	 * @param consecutivo
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @return
	 */
	public int actualizarDatosRips(Connection con,int consecutivo,double valorTotal,
			double valorCopago,double valorEmpresa,String autorizacion);
	
	/**
	 * Método para verificar si una solicitud ya está registrada en RIPS
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int verificarExistenciaRegistroRips(Connection con,int numeroSolicitud);
	
	/**
	 * Método para consultar los datos de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicio(Connection con,int codigoServicio);
	
	/**
	 * Método usado para insertar una excepcion de rips consultorios
	 * desde la valoración
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param rips
	 * @return
	 */
	public int insertarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion,boolean rips);
	
	/**
	 * Método para consultar la excepcion rips de un registro
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean consultarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion);
	
	/**
	 * Método para consultar los registros del RIPS por Rangos Paciente
	 * @param con
	 * @param idCuenta
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosRangosPaciente(Connection con,int idCuenta);
	
	/**
	 * Método que consulta de datos de la cuenta de cobro de capitacion
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarDatosCxCCapitacion(Connection con,HashMap campos);
	
	/**
	 * Método para consultar los RIPS del archivo AD en Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAD(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los RIPS del archivo US Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionUS(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los RIPS del archivo AC Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAC(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los rips AP Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAP(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los rips del archivo AM Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAM(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los rips AT Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAT(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los rips de AU Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAU(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los rips AH Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAH(Connection con,HashMap campos);
	
	/**
	 * Método que consulta los rips del archivo AN
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAN(Connection con,HashMap campos);
	
	
	/**
	 * Metodo de consulta del convenio para el numero de factura consultado de la tabla de interfaz ax_rips
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public int consulartConvenioXNroFactura(Connection con,int nroFactura);
	
	/**
	 * Metodo de consulta del convenio de una factura
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public int consultarConvenioFactura(Connection con,int nroFactura);
	
	/**
	 * Nombre del Tercero para mostrar como Entidad
	 * @param con
	 * @param nitTercero
	 * @return
	 */
	public String consultarNombreTercero(Connection con, String nitTercero);
	
	/**
	 * Nit del Convenio 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String consultarNitConvenio(Connection con, int codigoConvenio);

	
}
