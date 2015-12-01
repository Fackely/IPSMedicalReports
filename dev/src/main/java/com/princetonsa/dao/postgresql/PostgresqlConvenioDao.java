/*
 * @(#)PostgresqlConvenioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.ConvenioDao;
import com.princetonsa.dao.sqlbase.SqlBaseConvenioDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoMediosAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Implementaci�n postgresql de las funciones de acceso a la fuente de datos
 * para un convenio
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */

public class PostgresqlConvenioDao implements ConvenioDao  
{
	/**
	 * obtiene el numero de solicitud a insertar para evitar problemas de consurrencia
	 */
	private static final String getPKConvenioAInsertarStr="SELECT nextval('seq_convenios') as codigo";
	
	/**
	 * Cadena para insertar un nuevo correo Electronico asociado al convenio
	 */
	private final static String insertarCorreoElectronicoStr="INSERT INTO correos_convenio "+
															"(codigo_pk, " +
															"email, "+
															"convenio, "+
															"fecha_modifica, "+
                                                            "hora_modifica, "+
                                                            "usuario_modifica) "+
															"VALUES (nextval('seq_correos_conv'), ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";
	
	
	/**
	 * Inserta un convenio
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
							boolean convenioManejaMontoCobro,
							int tipoContrato,
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
							//*************	Cambios Anexo 753 Decreto 4747******************
							String repInconsistBD,
							String repAtencIniUrg,
							String generAutoValAteniniUrg,
							String requiereAutorizacionServicio,
        					String formatoAutorizacion,
							//**************************************************************
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
							String tipoLiquidacionPool,
							String manejaPresupCapitacion,
        					String capitacionSubcontratada
							
//        					*****************************************
	                        )
										
	{
		int codigoConvenioAInsertar=this.getPKConvenioAinsertar(con);
		if(codigoConvenioAInsertar>0)
			return SqlBaseConvenioDao.insertar(	con, usuario, empresa,
												tipoRegimen,
												nombre,
												observaciones,
												planBeneficios,
												codigoMinSalud,
												formatoFactura,
												activa,
												convenioManejaMontoCobro,
												tipoContrato, 
												checkInfoAdicCuenta, 
												codigoConvenioAInsertar, 
												pyp, 
												unificarPyp,
												numerDiasVencimiento, 
												requiereJustificacionServicios, 
												requiereJustificacionArticulos, 
												requiereJustArtNoposDifMed,
												manejaComplejidad, 
												semanasMinimasCotizacion, 
												requiereCarnet, 
												tipoConvenio, 
												tipoCodigo, 
												tipoCodigoArt,
												ajusteServicios, 
												ajusteArticulos, 
												interfaz, 
												institucion,					
												cantidadMaxCirugia,
												cantidadMaxAyudpag,
												tipoLiquidacionScx,
												tipoLiquidacionDyt,
												tipoLiquidacionGcx,
												tipoLiquidacionGdyt,
												tipoTarifaLiqMateCx,
												tipoTarifaLiqMateDyt,
												tipoFechaLiqTiemPcx,
												tipoFechaLiqTiempDyt,
												liquidacionTmpFracAdd,
												planEspecial,
												excentoDeudor,
												excentoDocumentoGarantia,
												vip,
												radicarCuentasNegativas,
												asignarFactValorPacValorAbono,
												encabezadoFactura,
												pieFactura,empresaInstitucion,
												cantCPO,
												diasCPO,
												aseguradora, 
												codigoAseguradora,
												valorLetrasFactura,
												//*************	Cambios Anexo 753 Decreto 4747******************
												repInconsistBD,
												repAtencIniUrg,
												generAutoValAteniniUrg,
												requiereAutorizacionServicio,
												formatoAutorizacion,
												//*****************Anexo 791 Decreto*******************************
					        					manejaMultasPorIncumplimiento,
					        					valorMultaPorIncumplimientoCitas,
					                            //*****************************************************************
					        					
//					        					*********** Anexo 809 *******************
					        					ccContable ,
					        					cenAtencContable, 
					        					manejaBonos,
					        					requiereBono,
					        					manejaPromociones,
					        					esTargetaCliene, 
					        					ingresoBdValido,
					        					ingresoPacienteReqAutorizacion,
					        					reqIngresoValido,
					        					tipoAtencion,
					        					tipoLiquidacionPool,
					        					manejaPresupCapitacion,
					        					capitacionSubcontratada
//					        					*****************************************
			                                   );
		return 0;
	}											
	
	/**
	 * M�todo que  carga  los datos de un convenio seg�n los datos
	 * que lleguen del c�digo del convenio para mostrarlos en el resumen
	 * en una BD Oracle
	 */
	public boolean cargarResumen(Connection con, int codigo, Convenio convenio) throws SQLException, BDException
	{
		return SqlBaseConvenioDao.cargarResumen(con,codigo, convenio);
	}
	
	
	/**
	 * Metodo que carga los Medios de Envio de los reportes de un convenio
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public  HashMap<String, String> cargarMediosEnvio(Connection con, int codigo) throws SQLException, BDException
	{
		return SqlBaseConvenioDao.cargarMediosEnvio(con,codigo);
	}
	
	
	/**Carga el �ltimo convenio insertado**/
	public ResultSetDecorator cargarUltimoCodigo(Connection con)
	{
		return SqlBaseConvenioDao.cargarUltimoCodigo(con);
	}
	/**
	 * Modifica un convenio dado su c�digo con los param�tros dados.
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
	public  int modificar(	Connection con,
							UsuarioBasico usuario, 
							int codigo, 
							int empresa,
							String tipoRegimen,
							String nombre, 
							String  observaciones, 
							String planBeneficios,  
							String codigoMinSalud,	
							int formatoFactura,			
							boolean activa,
							boolean convenioManejaMontoCobro,
							int tipoContrato,
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
							//*************	Cambios Anexo 753 Decreto 4747******************
							String repInconsistBD,
							String repAtencIniUrg,
							String generAutoValAteniniUrg,
							String[] mediosEnvio,
							HashMap correos,
							String requiereAutorizacionServicio,
							String formatoAutorizacion,
							//**************************************************************
//        					************ Anexo 791 ************
							String manejomanejaMultasPorIncumplimiento,
							String valorMultaPorIncumplimientoCitas,
//        					***********************************
//        					******* anexo 809 *******
							int ccContable,
//        					*************************
							int cenAtencContable, // anexo 50
							
							String manejaBonos,
							String requiereBono,
							String manejaPromociones,
							String esTargetaCliene,
							String ingresoBdValido,
							String ingresoPacienteReqAutorizacion,
							String reqIngresoValido,
							String tipoAtencion,
							String tipoLiquidacionPool, //anexo 961
							String capitacionSubcontratada,
        					String manejaPresupCapitacion
							
						) throws BDException

	{
		return SqlBaseConvenioDao.modificar(con,
				                            usuario,
											codigo,
											empresa,
											tipoRegimen,
											nombre,
											observaciones,
											planBeneficios,
											codigoMinSalud,
											formatoFactura,
											activa,
											convenioManejaMontoCobro,
											tipoContrato,
											pyp,
											unificarPyp,
											numerDiasVencimiento,
											requiereJustificacionServicios,
											requiereJustificacionArticulos,
											requiereJustArtNoposDifMed,
											manejaComplejidad,
											semanasMinimasCotizacion,
											requiereCarnet,
											tipoConvenio,
											tipoCodigo,
											tipoCodigoArt,
											ajusteServicios,
											ajusteArticulos,
											interfaz,
											
											cantidadMaxCirugia,
											cantidadMaxAyudpag,
											tipoLiquidacionScx,
											tipoLiquidacionDyt,
											tipoLiquidacionGcx,
											tipoLiquidacionGdyt,
											tipoTarifaLiqMateCx,
											tipoTarifaLiqMateDyt,
											tipoFechaLiqTiemPcx,
											tipoFechaLiqTiempDyt,
											liquidacionTmpFracAdd,
											encabezadoFactura,
											pieFactura,
											empresaInstitucion,
											planEspecial, 
											excentoDeudor, 
											excentoDocumentoGarantia,
											vip,
											radicarCuentasNegativas,
											asignarFactValorPacValorAbono,
											cantCPO,
											diasCPO,
											aseguradora,
											codigoAseguradora,
											valorLetrasFactura,
										//*************	Cambios Anexo 753 Decreto 4747******************
											repInconsistBD,
											repAtencIniUrg,
											generAutoValAteniniUrg,
											mediosEnvio,
											correos,
											requiereAutorizacionServicio,
											formatoAutorizacion,
										//******************************************************************
											insertarCorreoElectronicoStr,
//											************ Anexo 791 ************
				        					manejomanejaMultasPorIncumplimiento,
				        					valorMultaPorIncumplimientoCitas,
//				        					***********************************
//				        					******* anexo 809 *******
				        					ccContable,
				        					cenAtencContable,
				        					manejaBonos,
				        					requiereBono,
				        					manejaPromociones,
				        					esTargetaCliene,
				        					ingresoBdValido,
				        					ingresoPacienteReqAutorizacion,
				        					reqIngresoValido,
				        					tipoAtencion,
				        					tipoLiquidacionPool,
//				        					*************************
				        					capitacionSubcontratada,
				        					manejaPresupCapitacion
											);									 
	}
	
	
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla convenios
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla convenios
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int codigoInstitucion,String tipoAtencion, boolean ambos) throws SQLException
	{
		return SqlBaseConvenioDao.listado(con, codigoInstitucion, tipoAtencion,  ambos);
	}
	
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
									String tipoLiquidacionPool) throws SQLException
	{
		return SqlBaseConvenioDao.busqueda(	con,
				codigoConvenio,
				razonSocial,
				nombreTipoRegimen,
				nombre,
				observaciones,
		        planBeneficios,
		        codigoMinSalud,
		        nombreFormatoFactura, 
		        activaAux,
		        nombreTipoContrato,
		        codigoInstitucion,
		        pypAux,
		        unificarPyp,
		        interfaz,
		        
		        cantidadMaxCirugia,
				cantidadMaxAyudpag,
				tipoLiquidacionScx,
				tipoLiquidacionDyt,
				tipoLiquidacionGcx,
				tipoLiquidacionGdyt,
				tipoTarifaLiqMateCx,
				tipoTarifaLiqMateDyt,
				tipoFechaLiqTiemPcx,
				tipoFechaLiqTiempDyt,
				liquidacionTmpFracAdd,
				empresaInstitucion, 
				planEspecial,
				radicarCuentasNegativas,
				asignarFactValorPacValorAbono,
				manejaBonos,
				requiereBono,
				manejaPromociones,
				esTargetaCliene,
				ingresoBdValido,
				ingresoPacienteReqAutorizacion,
				reqIngresoValido,
				tipoAtencion,
				tipoLiquidacionPool);													
	}
	
	/**
	 * M�todo para actualizar segun lo ingresado en parametros Generales
	 * @param con
	 * @param infoAdicIngresoConvenios
	 * @return
	 */
	public int modificarInfoAdicConvenio(Connection con, boolean infoAdicIngresoConvenios)
	{
		return SqlBaseConvenioDao.modificarInfoAdicConvenio(con, infoAdicIngresoConvenios);
	}
	
	/**
	 * Obtiene el codigo PK del convenio a insertar
	 * @param con
	 * @param consulta
	 * @return
	 */
	public int getPKConvenioAinsertar(Connection con)
	{
		return SqlBaseConvenioDao.getPKConvenioAinsertar(con, getPKConvenioAInsertarStr);
	}
	
	/**
	 * metodo que indica si un convenio en el tipo de contrato es capitado o no
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean esConvenioCapitado(Connection con, String idCuenta)
	{
		return SqlBaseConvenioDao.esConvenioCapitado(con, idCuenta);
	}
	
	/**
	 * M�todo que consulta los contratos vigentes del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarContratosVigentesConvenio(Connection con,int codigoConvenio)
	{
		return SqlBaseConvenioDao.consultarContratosVigentesConvenio(con, codigoConvenio);
	}
	
	/**
	 * metodo que indica si el convenio maneja o no complejidad
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio) throws BDException
	{
		return SqlBaseConvenioDao.convenioManejaComplejidad(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosConvenio( Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.existeVerificacionDerechosConvenio(con, codigoConvenio);
	}
	
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
	public boolean actualizarConveniosPacientes(	Connection con, int codigoPaciente, int codigoConvenio, int estratoSocial, String acronimoTipoAfiliado, String fechaAfiliacion)
	{
		return SqlBaseConvenioDao.actualizarConveniosPacientes(con, codigoPaciente, codigoConvenio, estratoSocial, acronimoTipoAfiliado, fechaAfiliacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public int obtenerNumeroDiasVencimiento(Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.obtenerNumeroDiasVencimiento(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCuentaContableConvenio(Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.obtenerCuentaContableConvenio(con, codigoConvenio);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNroIdentificacionConvenio(Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.obtenerNroIdentificacionConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public double obtenerEmpresaInstitucionConvenio( Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.obtenerEmpresaInstitucionConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 */
	public ArrayList cargarPlanEspecial(Connection con, String codigoInstitucion) 
	{
		return SqlBaseConvenioDao.cargarPlanEspecial(con, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public ArrayList consultarCcContable(Connection con, String codigoInstitucion) 
	{
		return SqlBaseConvenioDao.consultarCcContable(con, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public String cargarNombreCentroCostoPlanEspecial(Connection con, String codigoPlanEspecial) 
	{
		return SqlBaseConvenioDao.cargarNombreCentroCostoPlanEspecial(con, codigoPlanEspecial);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public int obtenerRadicarCuentasNegativas(Connection con, int codigoConvenio)
	{
		return SqlBaseConvenioDao.obtenerRadicarCuentasNegativas(con, codigoConvenio);
	}
	
	/**
	 * 
	 */
	
	public HashMap consultaUsuariosGlosasConvenio (Connection con)
	{
		return SqlBaseConvenioDao.consultaUsuariosGlosasConvenio(con);
	}
	
	public boolean insertarUsuariosGlosasConvenio (Connection connection, HashMap datos)
	{
		return SqlBaseConvenioDao.insertarUsuariosGlosasConvenio(connection, datos);
	}
	
	public boolean modificarUsuariosGlosasConvenio (Connection connection, HashMap datos)
	{
		return SqlBaseConvenioDao.modificarUsuariosGlosasConvenio(connection, datos);
	}
	
	/**
	 * Metodo encargado de consultar los usuarios de Glosa por Convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap consultaUsuariosGlosasPorConvenio(Connection con, String convenio)
	{
		return SqlBaseConvenioDao.consultaUsuariosGlosasPorConvenio(con, convenio);
	}

	@Override
	public HashMap consultarCorreosElectronicos(Connection con,
			int codigoConvenio) {
		
		return SqlBaseConvenioDao.consultarCorreosElectronicos(con, codigoConvenio);
	}
	
	/**
	 * consultar indicadores del convenio
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarIndicadoresConvenio(Connection con, HashMap parametros)
	{
		return SqlBaseConvenioDao.consultarIndicadoresConvenio(con, parametros);
	}
	
	@Override
	public double insertarMedioAutorizacion(
		DtoMediosAutorizacion medioAutorizacion , Connection con) {
		
		return SqlBaseConvenioDao.insertarMedioAutorizacion(medioAutorizacion ,  con);
	}
	
	
	
	@Override
	public ArrayList<DtoMediosAutorizacion> cargarMediosAutorizacion(
			Connection con, int convenio) throws BDException{
		
		 return SqlBaseConvenioDao.cargarMediosAutorizacion(con, convenio, "");
	}

	@Override
	public boolean eliminarMedioAutorizacion(int convenio, Connection con) {
		return SqlBaseConvenioDao.eliminarMediosAutorizacion(convenio,con);
		
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param mediosEnvio
	 * @return
	 */
	public int insertarMediosEnvio(Connection con, UsuarioBasico usuario,int codConvenio, String[] mediosEnvio)
	{
		return SqlBaseConvenioDao.insertarMediosEnvio(con, usuario, codConvenio, mediosEnvio);
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param codConvenio
	 * @param correos
	 * @return
	 */
	public int insertarCorreosElectronicos(Connection con,UsuarioBasico usuario, int codConvenio, HashMap correos)
	{
		return SqlBaseConvenioDao.insertarCorreosElectronicos(con, usuario, codConvenio, correos, insertarCorreoElectronicoStr);
	}
	
	
	
	/**
	 * 
	 */
	public boolean insertarDocumentoAdjuntoConvenio(Connection con,
			int convenio, String nombreArchivo, String nombreOriginal) {
		
		return SqlBaseConvenioDao.insertarDocumentoAdjuntoConvenio(con, convenio, nombreArchivo, nombreOriginal);
	}


	@Override
	public HashMap cargarAdjuntos(Connection con, int convenio)
			throws SQLException {
		
		return SqlBaseConvenioDao.cargarAdjuntos(con, convenio);
	}

	@Override
	public boolean eliminarAjuntos(Connection con, int convenio)
			throws SQLException {
		
		return SqlBaseConvenioDao.eliminarAjuntos(con, convenio);
	}

	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public boolean manejaPromociones(int convenio)
	{
		return SqlBaseConvenioDao.manejaPromociones(convenio);
	}
	

	
	
	/**
	 * 
	 * @param convenio
	 * @return
	 */
	public String obtenerTipoAtencion(int convenio) throws BDException
	{
		return SqlBaseConvenioDao.obtenerTipoAtencion(convenio);
	}
	
	@Override
	public ArrayList<Convenio> consultarConveniosPacienteUltimoIngreso(Connection con, int codigoPersona, boolean filtrarOdontologicos) {
		return SqlBaseConvenioDao.consultarConveniosPacienteUltimoIngreso(con, codigoPersona, filtrarOdontologicos);
	}

	@Override
	public boolean existeConvenioOdontologico(String tipoAtencion, int institucion) {
		
		return SqlBaseConvenioDao.existeConvenioOdontologico(tipoAtencion, institucion);
	}

	@Override
	public int cargarCentroAtencionContable(Connection con, int codigo) throws BDException{
		return SqlBaseConvenioDao.cargarCentroAtencionContable(con, codigo);
	}

	@Override
	public String obtenerTipoLiquidacionPool(int convenio)
	{
		return SqlBaseConvenioDao.obtenerTipoLiquidacionPool(convenio);
	}
	
	@Override
	public ArrayList<DtoConvenio> cargarConveniosArrayList(DtoConvenio dto) {
		
		return SqlBaseConvenioDao.cargarConveniosArrayList(dto);
	}

	/**
	 * 
	 */
	public boolean esConvenioTipoConventioEventoCatTrans(int codigoConvenio)
	{
		return SqlBaseConvenioDao.esConvenioTipoConventioEventoCatTrans(codigoConvenio);
	}
}