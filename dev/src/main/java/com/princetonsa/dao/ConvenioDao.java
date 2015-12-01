/*
 * @(#)ConvenioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
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

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoMediosAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.BDException;

/**
 *  Interfaz para el acceder a la fuente de datos de un convenio
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface ConvenioDao
{
	/**
	 * 
	 * @param convenioManejaMontoCobro 
	 * @param ccContable 
	 * @param cenAtencContable 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param empresa, int, c�digo de la empresa (tabla empresas - estado activo)
	 * @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param nombre, String, nombre del convenio
	 * @param observaciones, String, observaciones del convenio 
	 * @param planBeneficios, String, descripci�n del plan de beneficios
	 * @param codigoMinSalud, String, codigo Minsalud
	 * @param formatoFactura, int, selecciona el tipo de formato de factura 
	 * 				que utiliza el convenio
	 * @param activo, boolean, si el convenio est� activo en el sistema o no
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
							UsuarioBasico usuario,
							int empresa,
							String tipoRegimen,
							String nombre,
							String observaciones,
							String planBeneficios,
							String codigoMinSalud,
							int formatoFactura,
							boolean activa,
							boolean convenioManejaMontoCobro, int tipoContrato,
							String checkInfoAdicCuenta,
							String pyp, 
							String unificarPyp,
							String numerDiasVencimiento, 
							String requiereJustificacionServicios, 
							String requiereJustificacionArticulos,
							String requiereJustArtNoposDifMed,
							String manejaComplejidad,
							String semanasMinimasCotizacion, 
							String requiereCarnet,
							String tipoConvenio, 
							int tipoCodigo,
							int tipoCodigoArt, 
							String ajusteServicios, 
							String ajusteArticulos, 
							String interfaz, 
							int institucion, 
							
							int cantidadMaxCirugia,
							int cantidadMaxAyudpag,
							String tipoLiquidacionScx,
							String tipoLiquidacionDyt,
							String tipoLiquidacionGcx,
							String tipoLiquidacionGdyt,
							String tipoTarifaLiqMateCx,
							String tipoTarifaLiqMateDyt,
							String tipoFechaLiqTiemPcx,
							String tipoFechaLiqTiempDyt,
							String liquidacionTmpFracAdd,
							int planEspecial,
							String excentoDeudor,
							String excentoDocumentoGarantia,
							String vip,
							String radicarCuentasNegativas,
							String asignarFactValorPacValorAbono,
							
							String encabezadoFactura,
							String pieFactura,
							String empresaInstitucion,
							int cantCPO,
							int diasCPO, 
							String aseguradora, 
							String codigoAseguradora,
							String valorLetrasFactura,
							//*************	Cambios Anexo 753 Decreto 4747************************************
							String repInconsistBD,
							String repAtencIniUrg,
							String generAutoValAteniniUrg,
							String requiereAutorizacionServicio,
        					String formatoAutorizacion,
							
        					//*************	Cambios Anexo 753 Decreto 4747*************************************************
        					//*****************Anexo 791 Decreto*******************************
        					String manejaMultasPorIncumplimiento,
        					String valorMultaPorIncumplimientoCitas,
                            //*****************************************************************
//        					*********** Anexo 809 *******************
        					int ccContable,
        					
        					int cenAtencContable, // anexo 50
        					String manejaBonos,
							String requiereBono,
							String manejaPromociones,
							String esTargetaCliene,
							String ingresoBdValido,
							String ingresoPacienteReqAutorizacion,
							String reqIngresoValido,
							String tipoAtencion,
							String tipoLiquidacionPool, //ANEXO 961
							String manejaPresupCapitacion,
        					String capitacionSubcontratada
//        					*****************************************
	                       );										
	
	/**
	 * M�todo que  carga  los datos de un convenio seg�n los datos
	 * que lleguen del c�digo del convenio para mostrarlos en el resumen
	 * en una BD Oracle
	 */
	
	public double insertarMedioAutorizacion(DtoMediosAutorizacion medioAutorizacion , Connection con);
	
	public boolean eliminarMedioAutorizacion(int convenio,Connection con);
	
	
	public  ArrayList<DtoMediosAutorizacion> cargarMediosAutorizacion(Connection con, int convenio) throws BDException;
	
	public boolean cargarResumen(Connection con, int codigo, Convenio convenio) throws SQLException, BDException;
	
	/**
	 * Metodo que carga los medios de envio de los reportes de un convenio
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, String> cargarMediosEnvio(Connection con, int codigo) throws SQLException, BDException;
	
	public HashMap cargarAdjuntos(Connection con, int convenio) throws SQLException;
	
	public  boolean eliminarAjuntos(Connection con , int convenio) throws SQLException; 
	
	
	/**Carga el �ltimo convenio insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con);
	
	/**
	 * Modifica un convenio dado su c�digo con los param�tros dados.
	 * @param convenioManejaMontoCobro 
	 * @param ccContable 
	 * @param cenAtencContable 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo del convenio
	 * @param empresa, int, c�digo de la empresa (tabla empresas - estado activo)
	 *  @param tipoRegimen, String, r�gimen de acuerdo a los previam/t
	 * 				ingresados en el sistema
	 * @param nombre, String, nombre del convenio
	 * @param observaciones, String, observaciones del convenio 
	 * @param planBeneficios, String, descripci�n del plan de beneficios
	 * @param codigoMinSalud, String, codigo Minsalud
	 * @param formatoFactura, int, selecciona el tipo de formato de factura 
	 * 				que utiliza el convenio
	 * @param activo, boolean, si el convenio est� activo en el sistema o no
	 * 
	 * @param Cantidad max cirugia adicionales
	 * @param Cantidad Max ayudantes que paga
	 * @param Tipo de Liquidacion Salas Cirugia
	 * @param Tipo de Liquidacion Salas No Cruentos
	 * @param Tipo de Liquidacion General Cirugias
	 * @param Tipo de Liquidacion General No Cruetos
	 * @param Tipo de Tarifa para liquidacion materiales Cirugia
	 * @param Tipo de Tarifa para liquidacion materiales No Cruentos
	 * @param Tipo de Fecha liquidacion Tiempos Cirugia
	 * @param Tipo de Fecha para Liquidacion Tiempos No Cruentos
	 * @param Liquidacion de Tiempos x Fraccion Adicional Cumplida
	 * 
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public  int modificar(	Connection con,UsuarioBasico usuario, 
							int codigo, 
							int empresa,
							String tipoRegimen,
							String nombre, 
							String  observaciones, 
							String planBeneficios,  
							String codigoMinSalud,	
							int formatoFactura,			
							boolean activa,
							boolean convenioManejaMontoCobro, int tipoContrato,
							String pyp, 
							String unificarPyp,
							String numerDiasVencimiento,
							String requiereJustificacionServicios, 
							String requiereJustificacionArticulos,
							String requiereJustArtNoposDifMed,
							String manejaComplejidad, 
							String semanasMinimasCotizacion, 
							String requiereCarnet, 
							String tipoConvenio, 
							int tipoCodigo, 
							int tipoCodigoArt, 
							String ajusteServicios, 
							String ajusteArticulos, 
							String interfaz,
							
							int cantidadMaxCirugia,
							int cantidadMaxAyudpag,
							String tipoLiquidacionScx,
							String tipoLiquidacionDyt,
							String tipoLiquidacionGcx,
							String tipoLiquidacionGdyt,
							String tipoTarifaLiqMateCx,
							String tipoTarifaLiqMateDyt,
							String tipoFechaLiqTiemPcx,
							String tipoFechaLiqTiempDyt,
							String liquidacionTmpFracAdd,
							
							String encabezadoFactura,
							String pieFactura,
							String empresaInstitucion,
							int planEspecial, 
							String excentoDeudor, 
							String excentoDocumentoGarantia, 
							String vip,
							String radicarCuentasNegativas,
							String asignarFactValorPacValorAbono,
							int cantCPO,
        					int diasCPO,
        					String aseguradora,
        					String codigoAseguradora,
        					String valorLetrasFactura,
        					//*************	Cambios Anexo 753 Decreto 4747************************************
        					String repInconsistBD,
    						String repAtencIniUrg,
    						String generAutoValAteniniUrg,
        					String[] medioEnvios,
        					HashMap correos,
        					String requiereAutorizacionServicio,
        					String formatoAutorizacion,
        					//**********************************************************************************
//        					************ Anexo 791 ************
        					String manejomanejaMultasPorIncumplimiento,
        					String valorMultaPorIncumplimientoCitas,
//        					***********************************
//        					********* cambios anexo 809 ********
        					int ccContable,
        					int cenAtencContable,
        					String manejaBonos,
							String requiereBono,
							String manejaPromociones,
							String esTargetaCliene,
							String ingresoBdValido,
							String ingresoPacienteReqAutorizacion,
							String reqIngresoValido,
							String tipoAtencion,
							String tipoLiquidacionPool,
//        					************************************
							String capitacionSubcontratada,
        					String manejaPresupCapitacion
        					) throws BDException;
	
	
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla convenios
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla convenios
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int codigoInstitucion,String tipoAtencion, boolean ambos) throws SQLException;
	
	/**
	 * M�todo que contiene el Resulset de todas los convenios
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla convenios
	 * @throws SQLException
	 */
	public   ResultSetDecorator busqueda(	Connection con,
									int codigoConvenio,
									String  razonSocial,
									String nombreTipoRegimen,
									String nombre,
									String observaciones,
									String planBeneficios,
									String codigoMinSalud,
									String nombreFormatoFactura,
									int activaAux,
									String nombreTipoContrato,
									int codigoInstitucion,
									int pypAux,
									String unificarPyp, 
									String interfaz,
									int cantidadMaxCirugia,
									int cantidadMaxAyudpag,
									String tipoLiquidacionScx,
									String tipoLiquidacionDyt,
									String tipoLiquidacionGcx,
									String tipoLiquidacionGdyt,
									String tipoTarifaLiqMateCx,
									String tipoTarifaLiqMateDyt,
									String tipoFechaLiqTiemPcx,
									String tipoFechaLiqTiempDyt,
									String liquidacionTmpFracAdd,
									String empresaInstitucion, 
									int planEspecial,
									String radicarCuentasNegativas,
									String asignarFactValorPacValorAbono,
									String manejaBonos,
									String requiereBono,
									String manejaPromociones,
									String esTargetaCliene,
									String ingresoBdValido,
									String ingresoPacienteReqAutorizacion,
									String reqIngresoValido,
									String tipoAtencion,
									String 	tipoLiquidacionPool
									) throws SQLException;
	
	/**
	 * M�todo para actualizar segun lo ingresado en parametros Generales
	 * @param con
	 * @param infoAdicIngresoConvenios
	 * @return
	 */
	public int modificarInfoAdicConvenio(Connection con, boolean infoAdicIngresoConvenios);
	
	/**
	 * Obtiene el codigo PK del convenio a insertar
	 * @param con
	 * @param consulta
	 * @return
	 */
	public int getPKConvenioAinsertar(Connection con);
	
	/**
	 * metodo que indica si un convenio en el tipo de contrato es capitado o no
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean esConvenioCapitado(Connection con, String idCuenta);
	
	/**
	 * M�todo que consulta los contratos vigentes del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarContratosVigentesConvenio(Connection con,int codigoConvenio);
	
	/**
	 * metodo que indica si el convenio maneja o no complejidad
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio) throws BDException;
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosConvenio( Connection con, int codigoConvenio);
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @param estratoSocial
	 * @param acronimoTipoAfiliado
	 * @param fechaAfiliacion
	 * @return
	 */
	public boolean actualizarConveniosPacientes(	Connection con, int codigoPaciente, int codigoConvenio, int estratoSocial, String acronimoTipoAfiliado, String fechaAfiliacion);
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public int obtenerNumeroDiasVencimiento(Connection con, int codigoConvenio);
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public int obtenerRadicarCuentasNegativas(Connection con, int codigoConvenio);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCuentaContableConvenio(Connection con, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNroIdentificacionConvenio(Connection con, int codigoConvenio);	
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public double obtenerEmpresaInstitucionConvenio( Connection con, int codigoConvenio);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList cargarPlanEspecial(Connection con, String codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList consultarCcContable(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoPlanEspecial
	 * @return
	 */
	public String cargarNombreCentroCostoPlanEspecial(Connection con, String codigoPlanEspecial);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultaUsuariosGlosasConvenio(Connection con);
	
	/**
	 * M�todo encargado de insertar un usuario en usuarios_glosas_conv
	 * @param con 
	 * 		La conexion que tengo para el flujo de datos
	 * @param datos 
	 * 		El HashMap con los datos a insertar
	 * @return true/false
	 */
	public boolean insertarUsuariosGlosasConvenio(Connection con, HashMap datos);

	/**
	 * M�todo encargado de modificar un usuario usuarios_glosas_conv
	 * @param con
	 * 		La conexion que tengo para el flujo de datos
	 * @param datos 
	 * 		El HashMap con los datos a modificar
	 * @return
	 */
	public boolean modificarUsuariosGlosasConvenio(Connection con, HashMap datos);
	
	/**
	 * Metodo encargado de consultar los usuarios de Glosa por Convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap consultaUsuariosGlosasPorConvenio(Connection con, String convenio);
    
	/**
	 * Metodo encargado de realizar la consulta de los Correos electronicos asosciados a un Convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarCorreosElectronicos(Connection con,int codigoConvenio);
	
	/**
	 * consultar indicadores del convenio
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarIndicadoresConvenio(Connection con, HashMap parametros);

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param mediosEnvio
	 * @return
	 */
	public int insertarMediosEnvio(Connection con, UsuarioBasico usuario,int codConvenio, String[] mediosEnvio);

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param correos
	 * @return
	 */
	public int insertarCorreosElectronicos(Connection con,UsuarioBasico usuario, int codConvenio, HashMap correos);
	
	/****
	 * 
	 * 
	 * 
	 */
	
	public  boolean existeConvenioOdontologico(String tipoAtencion, int institucion);
	
	/**
	 * 
	 * 
	 * 
	 */
	
	public  boolean insertarDocumentoAdjuntoConvenio(Connection con, int convenio, String nombreArchivo, String nombreOriginal);
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public boolean manejaPromociones(int convenio);
	

	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public String obtenerTipoAtencion(int convenio) throws BDException;

	public ArrayList<Convenio> consultarConveniosPacienteUltimoIngreso(Connection con, int codigoPersona, boolean filtrarOdontologicos);

	/**
	 * M�todo que carga el nectro de atenci�n contable del convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int cargarCentroAtencionContable(Connection con, int codigo) throws BDException;

	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public String obtenerTipoLiquidacionPool(int convenio);
	
	
	/**
	 * CARGAR CONVENIOS
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoConvenio> cargarConveniosArrayList(DtoConvenio dto);

	
	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esConvenioTipoConventioEventoCatTrans(int codigoConvenio);
}