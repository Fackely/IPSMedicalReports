/*
 * Creado   22/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.facturacion;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.cargos.RecargoTarifasAction;
import com.princetonsa.actionform.facturacion.ParamInstitucionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.servicio.fabrica.odontologia.contrato.ContratoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IContratoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresasServicio;
import com.servinte.axioma.vista.odontologia.contrato.ParametrizacionInstHelper;

/**
 * Clase para el manejo de parametrización de institución, ingreso,
 * modificación, eliminación y consulta
 * 
 * @version 1.0, 22/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class ParamInstitucionAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(RecargoTarifasAction.class);
	
	
	
	
	
	IContratoOdontologicoServicio  serContratoOdontologico=ContratoFabricaServicio.crearContratoOdontologico();
	IFirmaContratoMultiEmpresasServicio serFirmaMultiempresa= ContratoFabricaServicio.crearFirmaMultiempresa();
	
	
	/**
	 * Indices del mapa
	 */
	String[] indices={"tipo_moneda_","tipo_moneda_viejo_","fecha_inicial_","estabd_","institucion_"};

	/**
	 * Método execute del action
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection con = null;
		try{
			if (response == null)
				; // Para evitar que salga el warning
			if (form instanceof ParamInstitucionForm) 
			{



				try {
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

				}
				catch (SQLException e) 
				{
					logger.warn("No se pudo abrir la conexión -ParamInstituciónAction"+ e.toString());
				}


				ParametrizacionInstitucion mundo = new ParametrizacionInstitucion();
				ParamInstitucionForm institucionForm = (ParamInstitucionForm) form;
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				HttpSession session = request.getSession();
				String estado = institucionForm.getEstado();
				institucionForm.setMensaje(new ResultadoBoolean(false));


				/*
				 * VALIDACION 
				 */




				logger.info("\n ***************************************************************************");
				logger.info("\n estado =" + estado);
				logger.info("\n ***************************************************************************");
				//se almacena si maneja o no multiempresa
				institucionForm.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));

				if (estado == null) {
					logger
					.warn("Estado no valido dentro del flujo de Recargo Tarifas (null) ");
					request.setAttribute("codigoDescripcionError",
					"errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				} 
				else 
					if (estado.equals("empezar"))
					{
						institucionForm.reset();
						mundo.reset();
						institucionForm.setColInstitucion(mundo.consultarInstitucion(con, Integer.parseInt(usuario.getCodigoInstitucion()),"-1","-1", "-1", false));

						Iterator iterador = institucionForm.getColInstitucion().iterator();
						HashMap encabezado = (HashMap) iterador.next();

						Utilidades.imprimirMapa(encabezado);
						institucionForm.setAccion("empezar");
						institucionForm.setDepartamento(encabezado.get("depto") + "");
						institucionForm.setPais(encabezado.get("pais")+"");
						institucionForm.setCiudad(encabezado.get("ciudad") + "");
						institucionForm.setTipoIns(encabezado.get("tipo_institucion").toString());
						institucionForm.setUbicacionLogo(encabezado.get("ubicacion_logo_reportes")+"");
						institucionForm.setRepresentanteLegal(encabezado.get("representante_legal")+"");
						institucionForm.setNiveLogo(encabezado.get("nivel_logo")+"");


						//problema?

						this.accionCargarTiposMonedaInstitucion(con,institucionForm,mundo,usuario);
						this.cerrarConexion(con);

						accionConsultaContratoOdontologico(institucionForm, usuario);

						accionValidacionConsecutivoEspecialidad(institucionForm, usuario);

						return mapping.findForward("ingresarModificar");// Accion que
						// envia a la
						// pagina de
						// insertar/modificar.
					} else if (estado.equals("recargar")) {
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("ingresarModificar");
					} else if (estado.equals("insertar"))// estado para insertar.
					{
						return this.accionEmpezarInsertar(mapping, con,
								institucionForm, request, usuario);
					} else if (estado.equals("consultar"))// estado para consultar.
					{
						return this.accionEmpezarConsultar(mapping, con,
								institucionForm, usuario);
					}
					else 
						if (estado.equals("modificar"))// estado para consultar.
						{
							this.accionGuardarTiposMoneda(con,institucionForm,mundo,mapping,usuario);
							return this.accionEmpezarModificar(mapping, con,institucionForm, request, usuario, session);
						}
						else if(estado.equals("consultaDeptos"))
						{
							institucionForm.setEstado("empezar");
							institucionForm.setDepartamento("-");
							return mapping.findForward("ingresarModificar");
						}
						else if(estado.equals("consultaCiudades"))
						{
							institucionForm.setEstado("empezar");
							//institucionForm.setCiudad("-");
							return mapping.findForward("ingresarModificar");
						}
						else if(estado.equals("nuevoTipoMoneda"))
						{
							this.accionNuevoTipoMoneda(institucionForm);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("ingresarModificar");
						}
						else if(estado.equals("eliminarMoneda"))
						{
							UtilidadBD.closeConnection(con);
							Utilidades.eliminarRegistroMapaGenerico(institucionForm.getTiposMonedaInstitucionMap(), institucionForm.getTiposMonedaInstitucionEliminadosMap(), institucionForm.getIndiceEliminado(), indices, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
							return mapping.findForward("ingresarModificar");
						}
						else if (estado.equals("subirLogo") || estado.equals("cerrarArchivo"))
						{				
							UtilidadBD.closeConnection(con);				
							return mapping.findForward("subirLogo");
						}




				/************************
				 * ESTADOS PARA EL MANEJO DE CONTRATO ODONTOLOGICOS
				 *************************/

						else if(estado.equals("consultaContratoOdontologico"))
						{

							/*
							 * IMPRIMIR POPUP
							 */
							return mapping.findForward("contratoOdontologico");
						}


						else if(estado.equals("adicionaFirma"))
						{

							DtoFirmasContOtrosInstOdont dto = new DtoFirmasContOtrosInstOdont();
							institucionForm.getListaFirmasContrato().add(dto);

							institucionForm.setListaMayorCuatro(ParametrizacionInstHelper.validacionTamanioFirmas(institucionForm.getListaFirmasContrato()));

							return mapping.findForward("contratoOdontologico");
						}


						else if(estado.equals("eliminarFirma"))
						{

							DtoFirmasContOtrosInstOdont dtoFirmas= institucionForm.getListaFirmasContrato().get( institucionForm.getPostArraylist())  ;

							if(dtoFirmas.getCodigoPk()>0)
							{
								dtoFirmas.setActivo(Boolean.FALSE);
							}
							else
							{
								institucionForm.getListaFirmasContrato().remove(institucionForm.getPostArraylist());
							}

							institucionForm.setListaMayorCuatro(ParametrizacionInstHelper.validacionTamanioFirmas(institucionForm.getListaFirmasContrato()));


							return mapping.findForward("contratoOdontologico");
						}


						else if(estado.equals("cerrarContrato"))
						{
							institucionForm.setExistePorUnaFirma(ParametrizacionInstHelper.validarExisteUnaFirma(institucionForm.getListaFirmasContrato()));
							return mapping.findForward("contratoOdontologico");
						}




						else if(estado.equals("validacionImprimeFirmas") )
						{
							//dtoContrato.imprimirFirmasInsti
							if( institucionForm.getDtoContrato().getImprimirFirmasInsti().equals(ConstantesBD.acronimoNo) )
							{


								institucionForm.setListaFirmasContrato(ParametrizacionInstHelper.eliminarFirmasContratoInstitucion(institucionForm.getListaFirmasContrato()));
							}
							institucionForm.setListaMayorCuatro(ParametrizacionInstHelper.validacionTamanioFirmas(institucionForm.getListaFirmasContrato()));

							return mapping.findForward("contratoOdontologico");
						}

				/****************************************************************
				 * 		Estados adicionados para el manejo de las empresas
				 ****************************************************************/

						else
							/*----------------------------------
							 * 	ESTADO ==> LISTAREMPRESAS
				  -----------------------------------*/ 
							if (estado.equals("listarEmpresas"))
							{
								return ParametrizacionInstitucion.listarEmpresas(con, institucionForm, mapping, usuario);
							}
							else
								/*----------------------------------
								 * 	ESTADO ==> CARGAREMPRESA
					  -----------------------------------*/ 
								if (estado.equals("cargarEmpresa"))
								{
									String codigoTmpEmpresa= ParametrizacionInstitucion.cargarEmpresa(con, institucionForm, mapping);
									accionCargarMultiEmpresa(institucionForm,codigoTmpEmpresa);
									return mapping.findForward("principal");
								}




								else if (estado.equals("eliminarFirmaMultiEmpresa"))
								{

									accionEliminarFirmasMultiEmpresa(institucionForm);
									return mapping.findForward("principal"); 
								}


								else if(estado.equals("adicionarFirmaMultiempresa"))
								{
									DtoFirmasContOtrsiMultiEmpresa dto = new DtoFirmasContOtrsiMultiEmpresa();
									dto.setFirmaDigital(ConstantesBD.acronimoNo);
									institucionForm.getListaDtoFirmEmpresa().add(dto);


									return mapping.findForward("principal");
								}







								else
									/*----------------------------------
									 * 	ESTADO ==> SUBIRLOGO
						  -----------------------------------*/ 
									if (estado.equals("subirLogo") || estado.equals("cerrarArchivo"))
									{
										return mapping.findForward("subirLogo");
									}
									else
										/*----------------------------------
										 * 	ESTADO ==> CAMBIARCIUDAD
							  -----------------------------------*/ 
										if (estado.equals("cambiarCiudad"))
										{
											ParametrizacionInstitucion.cargarCiudadXpais(con, institucionForm);
											return mapping.findForward("principal");
										}
										else
											/*----------------------------------
											 * 	ESTADO ==> CAMBIARCIUDAD
								  -----------------------------------*/ 
											if (estado.equals("guardarEmpresa"))
											{
												institucionForm.setMensaje(new ResultadoBoolean(false));
												return ParametrizacionInstitucion.accionGuardar(con, institucionForm, mapping, usuario);
											}
											else
												/*----------------------------------
												 * 	ESTADO ==> nuevaEmpresa
									  -----------------------------------*/ 
												if (estado.equals("nuevaEmpresa"))
												{

													return ParametrizacionInstitucion.accionNuevo(con, institucionForm, mapping);
												}
												else
													/*----------------------------------
													 * 	ESTADO ==> ELIMINAREMPRESA
										  -----------------------------------*/ 
													if (estado.equals("eliminarEmpresa"))
													{

														return ParametrizacionInstitucion.accionEliminarCampo(con, institucionForm, mapping, usuario, request, response);
													}
													else
														/*----------------------------------
														 * 	ESTADO ==> GUARDAReLIMINACION
											  -----------------------------------*/ 
														if (estado.equals("guardarEliminacion"))
														{

															return ParametrizacionInstitucion.accionEliminarEmpresa(con, institucionForm, mapping);
														}
														else if(estado.equals("cargarfirmaMultiEmpresa"))
														{

															/*
															 * DESPLIEGA LA SECCION PARA ADICIONAR FIRMAS
															 */

															/*
															 * DESACTIVA LA DTO 
															 */
															institucionForm.setListaDtoFirmEmpresa(ParametrizacionInstHelper.eliminarListaFirmasContratoEmpresaInstitucion(institucionForm.getListaDtoFirmEmpresa()));
															return mapping.findForward("principal");
														}





				/****************************************************************
				 * 		final Estados adicionados para el manejo de las empresas
				 ****************************************************************/

			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}



	
	

	/**
	 * VALIDACION CONSECUTIVOS 
	 * @author Edgar Carvajal Ruiz
	 * @param institucionForm
	 * @param usuario
	 */
	private void accionValidacionConsecutivoEspecialidad(ParamInstitucionForm institucionForm, UsuarioBasico usuario) 
	{
		
		if( ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi) )
		{
			institucionForm.setManejaConsecutivosFactCent(Boolean.TRUE);
		}
		
		
		if(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
		{
			institucionForm.setManejaEspecialidadInsticionOdon(Boolean.TRUE);
		}
		
		if(ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias.equals(ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt())))
		{
			institucionForm.setConsecutivoPropioFacturasVarias(Boolean.TRUE);
			
		}else{
			
			institucionForm.setConsecutivoPropioFacturasVarias(Boolean.FALSE);
		}
		
		institucionForm.setManejaConsecutivoFacturasVariasPorCentroAtencion(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivoFacturasVariasPorCentroAtencion(usuario.getCodigoInstitucionInt())));

	}


	
	
	/**
	 * CARGAR FIRMAS  MULTIEMPRESA  
	 * @author Edgar Carvajal Ruiz
	 * @param institucionForm
	 * @param codigoTmpEmpresa
	 */
	private void accionCargarMultiEmpresa(ParamInstitucionForm institucionForm,	String codigoTmpEmpresa) 
	{
		
		
		if( institucionForm.getEmpresa().size()>0)
		 {
			 
			 if(!UtilidadTexto.isEmpty(codigoTmpEmpresa) )
			 {
				 
				 
				 FirmasContOtrsiempr firmaMultiEmpresa = new FirmasContOtrsiempr();
				 firmaMultiEmpresa.setEmpresasInstitucion(new EmpresasInstitucion());
				 firmaMultiEmpresa.getEmpresasInstitucion().setCodigo(Utilidades.convertirAEntero(codigoTmpEmpresa));
				 
				 
				 /*
				  *CARGA LISTA ENTIDAD 
				  */
				 List<FirmasContOtrsiempr>  listTmp= serFirmaMultiempresa.cargarFirmasPorInstucion(firmaMultiEmpresa);
				 
				 
				 
				 /*	
				  *CARGA LISTA DTO
				  */
				 if(listTmp!=null &&  listTmp.size()>0)
				 {
					institucionForm.setListaDtoFirmEmpresa(ParametrizacionInstHelper.cargarDtoFirmaMultiEmpresa(listTmp));
				 }
				 
				 
				 
			 }
			 
		 }
	}


	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param institucionForm
	 */
	private void accionEliminarFirmasMultiEmpresa(ParamInstitucionForm institucionForm) 
	{
		
		DtoFirmasContOtrsiMultiEmpresa tmp= institucionForm.getListaDtoFirmEmpresa(). get(institucionForm.getPostArrayMultiEmpresa());
		
		 if(tmp.getCodigoPk()>0)
		 {
			institucionForm.getListaDtoFirmEmpresa(). get(institucionForm.getPostArrayMultiEmpresa()).setActivo(Boolean.FALSE);
		 }
		 else 
		 {
			institucionForm.getListaDtoFirmEmpresa().remove(institucionForm.getPostArrayMultiEmpresa());
		 }
		 
	}

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param institucionForm
	 * @param usuario
	 */
	private void accionConsultaContratoOdontologico(ParamInstitucionForm institucionForm, UsuarioBasico usuario) {
		
		
		
		/*
		 *CONTRATO  
		 */
		ContratoOdontologico contrato= new ContratoOdontologico();
		contrato.setInstituciones(new Instituciones());
		contrato.getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		
		
		/*
		 * CONSULTAR SERVICIO 
		 */
		ContratoOdontologico tmpContrato= serContratoOdontologico.consultarAvanzadaContratoOdon(contrato);
		
		/*
		 * ARMADO DE LA LISTA DTO FIRMAS 
		 */
		if(tmpContrato!=null)
		{
			institucionForm.setDtoContrato(tmpContrato);
			institucionForm.setListaFirmasContrato(ParametrizacionInstHelper.cargarDtoFirmas(tmpContrato));
		}
		
	}

	
	
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param institucionForm
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarModificar(ActionMapping mapping,
			Connection con, ParamInstitucionForm institucionForm,
			HttpServletRequest request, UsuarioBasico usuario,
			HttpSession session) {
		ParametrizacionInstitucion mundo = new ParametrizacionInstitucion();
		logger.info("===>Logo: "+institucionForm.getLogo());
		logger.info("===>Logo Consultado en el Mapa: "+institucionForm.getLogo());
		mundo.reset();
		institucionForm.setColInstitucion(mundo.consultarInstitucion(con,
				Integer.parseInt(usuario.getCodigoInstitucion()),"-1", "-1", "-1",
				false));
		boolean modificado = verificarModificacion(institucionForm);

		if (modificado) {
			llenarloghistorial(institucionForm);
			institucionForm.resetCollection();
			boolean siModifico = false;

			// los rangos no son requeridos entonces se hizo el siguiente parche
			if (institucionForm.getRangoInicialFactura().equals(""))
				institucionForm
						.setRangoInicialFactura(ConstantesBD.codigoNuncaValido
								+ "");
			if (institucionForm.getRangoFinalFactura().equals(""))
				institucionForm
						.setRangoFinalFactura(ConstantesBD.codigoNuncaValido
								+ "");
			
			if (institucionForm.getRangoInicialFacturaVaria() == null){
				
				institucionForm
				.setRangoInicialFacturaVaria(new BigDecimal(ConstantesBD.codigoNuncaValido));
			}
				
			if (institucionForm.getRangoFinalFacturaVaria() == null){
				
				institucionForm
				.setRangoFinalFacturaVaria(new BigDecimal(ConstantesBD.codigoNuncaValido));
			}
				
			institucionForm.getArchivo();
			
			//Guarda la infomracion del logo
			accionAdjuntarArchivo(institucionForm);
			// //////////////////////////////////////////////////////////////////////////			
			siModifico = mundo.modificarDatosInst(
					con, 
					Integer.parseInt(institucionForm.getCodigo()), 
					institucionForm.getIdentificacion(),
					institucionForm.getDigitoVerificacion(),
					institucionForm.getAcronimoNit(),
					institucionForm.getRazonSocial(),
					institucionForm.getDepartamento(), institucionForm.getCiudad(),
					institucionForm.getDireccion(), 
					institucionForm.getTelefono(), 
					institucionForm.getCodMinSalud(),
					institucionForm.getActividadEconomica(), 
					institucionForm.getResolucion(),
					institucionForm.getPrefijoFacturas(),
					Integer.parseInt(institucionForm.getRangoInicialFactura()),
					Integer.parseInt(institucionForm.getRangoFinalFactura()),
					institucionForm.getPath(),
					institucionForm.getEncabezado(),
					institucionForm.getPie(), 
					institucionForm.getPieHistoriaClinica(),
					institucionForm.getPais(),
					institucionForm.getLogo(),
					institucionForm.getTipoIns(),
					institucionForm.getCodEmpTransEsp(),
					institucionForm.getUbicacionLogo(),
					institucionForm.getIndicativo(),
					institucionForm.getExtension(),
					institucionForm.getCelular(), 
					institucionForm.getCodigoInterfaz(),
					institucionForm.getRepresentanteLegal(),
					institucionForm.getNiveLogo(),
					institucionForm.getResolucionFacturaVaria(), 
					institucionForm.getPrefijoFacturaVaria(),
					institucionForm.getRangoInicialFacturaVaria(),
					institucionForm.getRangoFinalFacturaVaria(),
					institucionForm.getEncabezadoFacturaVaria(),
					institucionForm.getPieFacturaVaria(),
					institucionForm.getPieAmbMedicamentos());

			if (siModifico) 
			{
				institucionForm.setColInstitucion(mundo.consultarInstitucion(con, 
																			Integer.parseInt(institucionForm.getCodigo()),
																			"-1","-1", "-1", false));
				generarLog(institucionForm, session);
				
				institucionForm.setAccion("registroModificado");

				// Se vuelve a cargar en sesion la Intitucion con las Modificaciones Realizadas
				InstitucionBasica institucionBasica= new InstitucionBasica();
				institucionBasica.cargar(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
				session.setAttribute("institucionBasica", institucionBasica);
				//------------------------------------------------------------------------------
			}
			
			
			
			
			/*---------------------------------------------------------------------------------
			 *	MODIFICAR CONTRATO ODONTOLOGICO
			 *--------------------------------------------------------------------------------- 
			 */
			
			if(siModifico)
			{
				serContratoOdontologico.insertarModificarEliminar(institucionForm.getDtoContrato(), institucionForm.getListaFirmasContrato(), usuario);
			}
			
			
			
			

			if (!siModifico) {
				institucionForm.resetCollection();
				ActionErrors errores = new ActionErrors();
				errores.add("modificación institución", new ActionMessage("prompt.noSeGraboInformacion"));
				logger.warn("No se modifico la institucion");
				saveErrors(request, errores);
				this.cerrarConexion(con);
				return mapping.findForward("ingresarModificar");

			}
		}
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificar");
	}

	/**
	 * 
	 * @param institucionForm
	 * @return
	 */
	private boolean verificarModificacion(ParamInstitucionForm institucionForm) {
		Iterator iterador = institucionForm.getColInstitucion().iterator();
		HashMap encabezado = (HashMap) iterador.next();
		boolean siModifico = false;

		// institucionForm.getRangoInicialFactura()->"+institucionForm.getRangoInicialFactura());
		if (!institucionForm.getRazonSocial().equals("")) {
			if (!(encabezado.get("razon") + "").equals(institucionForm
					.getRazonSocial())) {
				siModifico = true;
			}
		}
		if (!institucionForm.getIdentificacion().equals("")) {
			if (!(encabezado.get("nit") + "").equals(institucionForm
					.getIdentificacion())) {
				siModifico = true;
			}
		}
		if (!institucionForm.getAcronimoNit().equals("")) {
			if (!(encabezado.get("tipo_nit") + "").equals(institucionForm
					.getAcronimoNit())) {
				siModifico = true;
			}
		}
		if (!(encabezado.get("digv") + "").equals(institucionForm
				.getDigitoVerificacion())) {
			siModifico = true;
		}
		if (!institucionForm.getDepartamento().equals("0")) {
			if (!(encabezado.get("depto") + "").equals(institucionForm
					.getDepartamento())) {
				siModifico = true;
			}
		}
		if (!institucionForm.getCiudad().equals("0")) {
			if (!(encabezado.get("ciudad") + "").equals(institucionForm
					.getCiudad())) {
				siModifico = true;
			}
		}
		if (!institucionForm.getDireccion().equals("")) {
			if (!(encabezado.get("direccion") + "").equals(institucionForm
					.getDireccion())) {
				siModifico = true;
			}
		}
		if (!institucionForm.getTelefono().equals("")) {
			if (!(encabezado.get("telefono") + "").equals(institucionForm
					.getTelefono())) {
				siModifico = true;
			}
		}
		// if(! institucionForm.getCodMinSalud().equals(""))
		// {
		if (!(encabezado.get("minsalud") + "").equals(institucionForm
				.getCodMinSalud())) {
			siModifico = true;
		}
		if (!(encabezado.get("cod_emp_trans_esp") + "").equals(institucionForm
				.getCodEmpTransEsp())) {
			siModifico = true;
		}
		// }
		// if(! institucionForm.getActividadEconomica().equals(""))
		// {
		if (!(encabezado.get("actividad") + "").equals(institucionForm
				.getActividadEconomica())) {
			siModifico = true;
		}
		// }

		// if(! institucionForm.getResolucion().equals(""))
		// {
		if (!(encabezado.get("resolucion") + "").equals(institucionForm
				.getResolucion())) {
			siModifico = true;
		}
		// }
		// if(! institucionForm.getPrefijoFacturas().equals(""))
		// {
		if (!(encabezado.get("prefijo") + "").equals(institucionForm
				.getPrefijoFacturas())) {
			siModifico = true;
		}
		// }
		if (!institucionForm.getRangoInicialFactura().equals("")) {
			if (!(encabezado.get("rango_inicial") + "").equals(institucionForm
					.getRangoInicialFactura())) {
				// if(!(encabezado.get("rango_inicial")+"").equals("null"))
				siModifico = true;
			}
		}
		if (!institucionForm.getRangoFinalFactura().equals("")) {
			if (!(encabezado.get("rango_final") + "").equals(institucionForm
					.getRangoFinalFactura())) {
				// if(!(encabezado.get("rango_final")+"").equals("null"))
				siModifico = true;
			}
		}
		// if(!institucionForm.getPath().equals(""))
		// {
		if (!(encabezado.get("path") + "").equals(institucionForm.getPath())) {
			siModifico = true;
		}
		// }
		/*
		 * if(!institucionForm.getEncabezado().equals("")) {
		 */
		if (!(encabezado.get("encabezado") + "").equals(institucionForm
				.getEncabezado())) {
			siModifico = true;
		}
		// }
		/*
		 * if(!institucionForm.getPie().equals("")) {
		 */
		if (!(encabezado.get("pie") + "").equals(institucionForm.getPie())) {
			siModifico = true;
		}
		
		
		if (!(encabezado.get("pie_his_cli") + "").equals(institucionForm.getPieHistoriaClinica())) {
			siModifico = true;
		}
		
		if (!(encabezado.get("pie_amb_amb") + "").equals(institucionForm.getPieAmbMedicamentos())) {
			siModifico = true;
		}		
		// }
		if (!institucionForm.getPais().equals("0")) {
			if (!(encabezado.get("pais") + "").equals(institucionForm
					.getPais())) {
				siModifico = true;
			}
		}
		
		if (!(encabezado.get("logo") + "").equals(institucionForm.getLogo())) {
			siModifico = true;
		}
		
		if (!(encabezado.get("codinterfaz") + "").equals(institucionForm.getCodigoInterfaz())) {
			siModifico = true;
		}
		

		if (!(encabezado.get("ubicacion_logo_reportes") + "").equals(institucionForm.getUbicacionLogo())) {
			siModifico = true;
		}
		
		if (!(encabezado.get("resolucion_factura_varia") + "").equals(institucionForm
				.getResolucionFacturaVaria())) {
			siModifico = true;
		}

		if (!(encabezado.get("pref_factura_varia") + "").equals(institucionForm
				.getPrefijoFacturaVaria())) {
			siModifico = true;
		}
		
//		TODO Mirar como se debe validar esto
//		if (!institucionForm.getRangoInicialFacturaVaria().equals("")) {
//			if (!(encabezado.get("rgo_inic_fact_varia") + "").equals(institucionForm
//					.getRangoInicialFacturaVaria())) {
//				
//				siModifico = true;
//			}
//		}
//		
//		if (!institucionForm.getRangoFinalFacturaVaria().equals("")) {
//			if (!(encabezado.get("rgo_fin_fact_varia") + "").equals(institucionForm
//					.getRangoFinalFacturaVaria())) {
//				
//				siModifico = true;
//			}
//		}
		
		if (!(encabezado.get("encabezado_factura_varia") + "").equals(institucionForm
				.getEncabezadoFacturaVaria())) {
			siModifico = true;
		}
		
		if (!(encabezado.get("pie_factura_varia") + "").equals(institucionForm.getPieFacturaVaria())) {
			siModifico = true;
		}
		
		
		
		return siModifico;
	}

	/**
	 * Metodo implementado para realizar la consulta de institución.
	 * 
	 * @param mapping,
	 *            Mapping para manejar la navegación.
	 * @param con,
	 *            Connection con la fuente de datos.
	 * @param institucionForm,
	 *            ParamInstitucionForm formulario.
	 * @param request,
	 *            HttpServletRequest para obtner datos de la session.
	 * @return findForward
	 */
	private ActionForward accionEmpezarConsultar(ActionMapping mapping,
			Connection con, ParamInstitucionForm institucionForm,
			UsuarioBasico usuario) {
		ParametrizacionInstitucion mundo = new ParametrizacionInstitucion();
		mundo.reset();
		institucionForm.resetCollection();
		institucionForm.setColInstitucion(mundo.consultarInstitucion(con,
				Integer.parseInt(usuario.getCodigoInstitucion()), "-1","-1", "-1",
				false));
		institucionForm.setTiposMonedaInstitucionMap(mundo.consultarTiposMonedaInstitucion(con, usuario.getCodigoInstitucionInt()));
		this.cerrarConexion(con);
		return mapping.findForward("paginaConsulta");
	}

	/**
	 * Metodo implementado para insertar los datos de una institución
	 * 
	 * @param mapping,
	 *            Mapping para manejar la navegación
	 * @param con,
	 *            Connection con la fuente de datos
	 * @param institucionForm,
	 *            ParamInstitucionForm formulario
	 * @param request,
	 *            HttpServletRequest para obtener datos de la session
	 * @return findForward,
	 * @see struts-config, ACTION Parametrización Institución
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarInsertar(ActionMapping mapping,
			Connection con, ParamInstitucionForm institucionForm,
			HttpServletRequest request , UsuarioBasico usuario) throws SQLException {
		ParametrizacionInstitucion mundo = new ParametrizacionInstitucion();
		mundo.reset();
		institucionForm.resetCollection();
		boolean siInserto = false;

		//Guarda la infomracion del logo
		accionAdjuntarArchivo(institucionForm);
		
		siInserto = mundo.insertarDatosInst(
				con, 
				Integer.parseInt(institucionForm.getCodigo()),
				institucionForm.getIdentificacion(), 
				institucionForm.getRazonSocial(),
				institucionForm.getDepartamento(), 
				institucionForm.getCiudad(),
				institucionForm.getDireccion(),
				institucionForm.getTelefono(),
				institucionForm.getCodMinSalud(),
				institucionForm.getActividadEconomica(),
				institucionForm.getResolucion(), 
				institucionForm.getPrefijoFacturas(),
				Integer.parseInt(institucionForm.getRangoInicialFactura()),
				Integer.parseInt(institucionForm.getRangoFinalFactura()),
				institucionForm.getPath(),
				institucionForm.getEncabezado(),
				institucionForm.getPie(), 
				institucionForm.getPais(),
				institucionForm.getLogo(),
				institucionForm.getTipoIns(),
				institucionForm.getCodEmpTransEsp(),
				institucionForm.getUbicacionLogo(),
				institucionForm.getIndicativo(),
				institucionForm.getExtension(),
				institucionForm.getCelular(),
				institucionForm.getCodigoInterfaz(),
				institucionForm.getRepresentanteLegal(),
				institucionForm.getNiveLogo(),
				institucionForm.getResolucionFacturaVaria(), 
				institucionForm.getPrefijoFacturaVaria(),
				institucionForm.getRangoInicialFacturaVaria(),
				institucionForm.getRangoFinalFacturaVaria(),
				institucionForm.getEncabezadoFacturaVaria(),
				institucionForm.getPieFacturaVaria()
				);

		if (siInserto) {
						institucionForm.setColInstitucion(mundo.consultarInstitucion(con,Integer.parseInt(institucionForm.getCodigo()), 
														"-1","-1", "-1",
														false));
						
			
						
				/*---------------------------------------------------------------------------------
				 *	INSERTAR CONSTRATO CONTRATO ODONTOLOGICO
				 *--------------------------------------------------------------------------------- 
				 */
				institucionForm.getDtoContrato().setInstituciones(new Instituciones());
				if(!UtilidadTexto.isEmpty(institucionForm.getCodigo()))
				{
					institucionForm.getDtoContrato().getInstituciones().setCodigo(Utilidades.convertirAEntero(institucionForm.getCodigo()));
					serContratoOdontologico.insertarModificarEliminar(institucionForm.getDtoContrato(), institucionForm.getListaFirmasContrato(), usuario);
				}
									
						
		}
		
		if (!siInserto) {
			institucionForm.resetCollection();
			System.out
					.println("Error insertando institución(ParamInstitucionAction - accionEmpezarInsertar");
			ActionErrors errores = new ActionErrors();
			errores.add("inserción institucion", new ActionMessage(
					"prompt.noSeGraboInformacion"));
			logger.warn("No se inserto la institucion");
			saveErrors(request, errores);
			this.cerrarConexion(con);
			return mapping.findForward("ingresarModificar");

		}
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificar");
	}
	
	/**
	 * Método implementado para adjuntar el archivo seleccionado
	 * @param con
	 * @param ParamInstitucionForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private void accionAdjuntarArchivo(ParamInstitucionForm forma) 
	{
		if(!UtilidadTexto.isEmpty(forma.getLogo()))
		{
			String rutaImagen ="";
			if(UtilidadFileUpload.validarExistePath(ValoresPorDefecto.getDirectorioImagenes()))
				rutaImagen = ValoresPorDefecto.getDirectorioImagenes();
			else if(UtilidadFileUpload.validarExistePath(ValoresPorDefecto.getFilePath()))
				rutaImagen = ValoresPorDefecto.getFilePath();
			
			logger.info("===>Ruta Archivo: "+rutaImagen);
			//logger.info("\n\n ruta archivo >> "+rutaImagen+" >>  "+forma.getArchivo().getFileName());
			
			//if(!rutaImagen.equals("") && !forma.getArchivo().getFileName().toString().equals(""))
			if (forma.getArchivo()!=null && forma.getArchivo().getFileSize()>0 && !rutaImagen.equals(""))
			{
				File prueba = UtilidadFileUpload.guardarArchivo(forma.getArchivo(),rutaImagen);
				forma.setLogo(prueba.getAbsolutePath());
				logger.info("\n\n logo final >> "+forma.getLogo());
			}					
		}	
		else		
			logger.info("\n\n no existe informacion del logo");
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo recursos), usado ante
	 * todo al momento de hacer un forward
	 * 
	 * @param con
	 *            Conexión con la fuente de datos
	 */
	public void cerrarConexion(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				UtilidadBD.closeConnection(con);
			}
		} catch (Exception e) {
			logger
					.error("Error al tratar de cerrar la conexion con la fuente de datos ParamInstitucionAction. \n Excepcion: "
							+ e);
		}
	}

	/**
	 * Metodo para generar el Log, y añadir los cambios realizados.
	 * 
	 * @param institucionForm,
	 *            Instancia del Form ParamInstitucionForm.
	 * @param session,
	 *            Session para obtener el usuario.
	 */
	private void generarLog(ParamInstitucionForm institucionForm,
			HttpSession session) {
		UsuarioBasico usuario;
		String[] datos = null;
		datos = pasarColArray(institucionForm);
		String log = institucionForm.getLog()
				+ "\n            ====INFORMACION DESPUES DE LA MODIFICACION===== "
				+ "\n*  Código de la Institución ["
				+ institucionForm.getCodigo()
				+ "] "
				+ "\n*  Razon Social ["
				+ institucionForm.getRazonSocial()
				+ "] "
				+ "\n*  Identificacion ["
				+ institucionForm.getIdentificacion()
				+ "] "
				+ "\n*  Tipo Identificacion ["
				+ institucionForm.getAcronimoNit()
				+ "] "
				+ "\n*  Departamento ["
				+ datos[14]
				+ "] "
				+ "\n*  Ciudad ["
				+ datos[15]
				+ "] "
				+ "\n*  Dirección ["
				+ institucionForm.getDireccion()
				+ "] "
				+ "\n*  Indicativo ["
				+ institucionForm.getIndicativo()
				+ "] "
				+ "\n*  Numero ["
				+ institucionForm.getTelefono()
				+ "] "
				+ "\n*  Extension ["
				+ institucionForm.getExtension()
				+ "] "
				+ "\n*  Telefono Celular ["
				+ institucionForm.getCelular()
				+ "] "
				+ "\n*  Código MinSalud ["
				+ institucionForm.getCodMinSalud()
				+ "] "
				+ "\n*  Cod. Habitación Empresa Transporte Especial ["
				+ institucionForm.getCodEmpTransEsp()
				+ "] "
				+ "\n*  Actividad Economica ["
				+ institucionForm.getActividadEconomica()
				+ "] "
				+ "\n*  Resolución ["
				+ institucionForm.getResolucion()
				+ "] "
				+ "\n*  Prefijo Facturación ["
				+ institucionForm.getPrefijoFacturas()
				+ "] "
				+ "\n*  Rango Inicial Facturación ["
				+ institucionForm.getRangoInicialFactura()
				+ "] "
				+ "\n*  Rango Final Facturación ["
				+ institucionForm.getRangoFinalFactura()
				+ "] "
				+ "\n*  Logo ["
				+ institucionForm.getLogo()
				+ "] "
				+ "\n*  Resolución Factura Varia ["
				+ institucionForm.getResolucionFacturaVaria()
				+ "] "
				+ "\n*  Prefijo Factura Varia ["
				+ institucionForm.getPrefijoFacturaVaria()
				+ "] "
				+ "\n*  Rango Inicial Factura Varia ["
				+ institucionForm.getRangoInicialFacturaVaria()
				+ "] "
				+ "\n*  Rango Final Factura Varia ["
				+ institucionForm.getRangoFinalFacturaVaria()
				+ "] "
				+ "\n========================================================\n\n\n ";
		usuario = (UsuarioBasico) session.getAttribute("usuarioBasico");
		LogsAxioma.enviarLog(ConstantesBD.logParamInstitucionCodigo, log,
				ConstantesBD.tipoRegistroLogModificacion, usuario
						.getLoginUsuario());

	}

	/**
	 * Metodo para cargar el Log con los datos actuales, antes de la
	 * modificación.
	 * 
	 * @param institucionForm,
	 *            Instancia del Form ParamInstitucionForm.
	 */
	private void llenarloghistorial(ParamInstitucionForm institucionForm) {
		String[] datos = null;
		datos = pasarColArray(institucionForm);

		institucionForm.setLog("\n            ====INFORMACION ORIGINAL===== "
				+ "\n*  Código de la Institución [" + datos[0] + "] "
				+ "\n*  Razon Social [" + datos[3] + "] "
				+ "\n*  Identificacion [" + datos[1] + "] "
				+ "\n*  Tipo Identificacion [" + datos[2] + "] "
				+ "\n*  Departamento [" + datos[14] + "] " + "\n*  Ciudad ["
				+ datos[15] + "] " + "\n*  Dirección [" + datos[6] + "] "
				+ "\n*  Indicativo [" + datos[18] + "] "
				+ "\n*  Numero [" + datos[7] + "] "
				+ "\n*  Extension [" + datos[19] + "] "
				+ "\n*  Telefono Celular [" + datos[20] + "] "
				+ "\n*  Código MinSalud [" + datos[8] + "] "
				+ "\n*  Cod. Habitación Empresa Transporte Especial [" + datos[17] + "] "
				+ "\n*  Actividad Economica [" + datos[9] + "] "
				+ "\n*  Resolución [" + datos[10] + "] "
				+ "\n*  Prefijo Facturación [" + datos[11] + "] "
				+ "\n*  Rango Inicial Facturación [" + datos[12] + "] "
				+ "\n*  Rango Final Facturación [" + datos[13] + "] "
				+ "\n*  Resolución Factura Varia[" + datos[10] + "] "
				+ "\n*  Prefijo Factura varia [" + datos[11] + "] "
				+ "\n*  Rango Inicial Factura Varia [" + datos[12] + "] "
				+ "\n*  Rango Final Factura Varia [" + datos[13] + "] "
				+ "\n*  Logo[ "  + datos[16] + "]  \n\n");

	}

	/**
	 * Metodo para pasar los datos de una colecci&oacute;n a un Array
	 * 
	 * @param institucionForm,
	 *            ParamInstitucionForm
	 * @return stringArray
	 */
	protected String[] pasarColArray(ParamInstitucionForm institucionForm) {
		ArrayList arrayList = new ArrayList();
		String[] stringArray = null;
		HashMap dyn;

		String[] columns = { "codigo", // 0
				"nit", // 1
				"tipo_nit", // 2
				"razon", // 3
				"depto", // 4
				"ciudad", // 5
				"direccion", // 6
				"telefono", // 7
				"minsalud", // 8
				"actividad", // 9
				"resolucion", // 10
				"prefijo", // 11
				"rango_inicial", // 12
				"rango_final", // 13
				"nombredepto", // 14
				"nombreciudad", // 15
				"logo", // 16
				"cod_emp_trans_esp", // 17
				"indicativo", // 18				
				"extension", // 19
				"celular", //19
				"codinterfaz", // 20
				"resolucion_factura_varia", // 21
				"pref_factura_varia",// 22
				"rgo_inic_fact_varia", // 23
				"rgo_fin_fact_varia"  // 24
		};

		int i, k;
		if (institucionForm.getColInstitucion() == null) {
			stringArray[0] = null;
		}
		Iterator it = institucionForm.getColInstitucion().iterator();
		k = 0;
		while (it.hasNext()) {
			dyn = (HashMap) it.next();
			for (i = 0; i < columns.length; i++) {
				arrayList.add(k, dyn.get(columns[i]));
				k++;
			}
		}
		stringArray = new String[arrayList.size()];
		for (i = 0; i < arrayList.size(); i++)
			stringArray[i] = arrayList.get(i) + "";

		return stringArray;
	}
	
	/**
	 * Cargar los tipos de monedas existentes hasta ahora para esta institucion
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarTiposMonedaInstitucion(Connection con,ParamInstitucionForm forma,ParametrizacionInstitucion mundo, UsuarioBasico usuario)
	{
		forma.setTiposMonedaInstitucionMap(mundo.consultarTiposMonedaInstitucion(con, usuario.getCodigoInstitucionInt()));
	}
	
	/**
	 * Inicializar campos para un nuevo tipo de moneda
	 * @param forma
	 */
	private void accionNuevoTipoMoneda(ParamInstitucionForm forma)
	{
		int pos=Utilidades.convertirAEntero(forma.getTiposMonedaInstitucionMap().get("numRegistros")+"");
		forma.setTiposMonedaInstitucionMap("tipo_moneda_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setTiposMonedaInstitucionMap("tipo_moneda_viejo_"+pos,ConstantesBD.codigoNuncaValido);
		forma.setTiposMonedaInstitucionMap("fecha_inicial_"+pos,UtilidadFecha.getFechaActual());
		forma.setTiposMonedaInstitucionMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setTiposMonedaInstitucionMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 */
	private void accionGuardarTiposMoneda(Connection con, ParamInstitucionForm forma,ParametrizacionInstitucion mundo, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getTiposMonedaInstitucionMap("numRegistros")+"");
		//ELIMINAR
		for(int a=0;a<Integer.parseInt(forma.getTiposMonedaInstitucionEliminadosMap().get("numRegistros")+"");a++)
		{
			transaccion=mundo.eliminarTiposMonedaInstitucion(con,Utilidades.convertirAEntero(forma.getTiposMonedaInstitucionEliminadosMap().get("tipo_moneda_"+a)+""));
		}
		for(int i=0;i<numReg;i++)
		{
			
			if(UtilidadTexto.getBoolean(forma.getTiposMonedaInstitucionMap("estabd_"+i)+""))
			{
				//MODIFICAR TIPOS DE MONEDA POR INSTITUCION
				HashMap vo=new HashMap();
				vo.put("tipo_nuevo", forma.getTiposMonedaInstitucionMap("tipo_moneda_"+i));
				vo.put("fecha_inicial", forma.getTiposMonedaInstitucionMap("fecha_inicial_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("institucion", usuario.getCodigoInstitucionInt());
				vo.put("tipo_viejo", forma.getTiposMonedaInstitucionMap().get("tipo_moneda_viejo_"+i));
				transaccion=mundo.modificarTiposMonedaInstitucion(con, vo);
			}
			else
			{
				//INSERTAR TIPOS DE MONEDAS POR INSTITUCION
				HashMap vo=new HashMap();
				vo.put("institucion", usuario.getCodigoInstitucionInt());
				vo.put("tipo_moneda", forma.getTiposMonedaInstitucionMap("tipo_moneda_"+i));
				vo.put("fecha_inicial", forma.getTiposMonedaInstitucionMap("fecha_inicial_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarTiposMonedaInstitucion(con, vo);
			}
		}
		if(transaccion)
		{
			logger.info("OPERACION REALIZADA CON EXITO!!!");
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			logger.info("NO SE PUDO INSERTAR EL REGISTRO.");
			UtilidadBD.abortarTransaccion(con);
		}
	}

}
