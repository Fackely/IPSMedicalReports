package com.princetonsa.action.parametrizacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Errores;
import util.InfoDatosStr;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.parametrizacion.CentrosAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.enu.administracion.EmunTipoConsecutivo;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.CategoriaAtencion;
import com.princetonsa.mundo.manejoPaciente.RegionesCobertura;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;




public class CentrosAtencionAction extends Action 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CentrosAtencionAction.class);
	
	
	CentroAtencion centroAtencionGlobal = new CentroAtencion();
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("unused")
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
			{
		UsuarioBasico usuario;
		Connection con = null;
		CentrosAtencionForm centrosAtencionForm;

		try {
			if (form instanceof CentrosAtencionForm) {
				centrosAtencionForm = (CentrosAtencionForm)form;
				String estado=centrosAtencionForm.getEstado();
				ActionErrors errores = new  ActionErrors();

				centrosAtencionForm.reset2();
				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				centrosAtencionForm.setManejaConsecutivoFactura(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencion(usuario.getCodigoInstitucionInt()));
				/*
				 * Indica si se maneja Consecutivos de Recibos de Caja por Centro de Atención
				 */
				centrosAtencionForm.setManejaConsecutivoTesoreria(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(usuario.getCodigoInstitucionInt()));

				centrosAtencionForm.setManejaConsecutivoFacturaVariaPorCentoAtencion(ValoresPorDefecto.getManejaConsecutivoFacturasVariasPorCentroAtencion(usuario.getCodigoInstitucionInt()));
				centrosAtencionForm.setManejoEspecialInstitOdonto(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt()));

				//centrosAtencionForm.setManejaConsecutivoFacturaVariaPorCentoAtencion(ConstantesBD.acronimoSi);

				//definirTipoConsecutivo(centrosAtencionForm);

				centrosAtencionForm.setManejaConsecutivoNotasPacientesCentroAtencion(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(usuario.getCodigoInstitucionInt()));

				try	{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	

					if( usuario == null ) {
						if( logger.isDebugEnabled() ) {
							logger.debug("Usuario no válido (null)");
						}


						UtilidadBD.cerrarConexion(con);                
						centrosAtencionForm.clean();                   

						request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
						return mapping.findForward("paginaError");              
					}
					if( estado == null ) {
						if( logger.isDebugEnabled() )
						{
							logger.debug("estado no valido dentro del flujo de valoración (null) ");
						}

						UtilidadBD.cerrarConexion(con);                
						centrosAtencionForm.clean();

						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						return mapping.findForward("descripcionError");             
					}
					else if(estado.equals("empezar"))
					{
						return this.accionEmpezar(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("consultar"))
					{
						return this.accionConsultar(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("ingresar"))
					{
						return this.accionIngresar(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("editar"))
					{
						return this.accionEditar(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("guardarNuevo"))
					{
						return this.accionGuardarNuevo(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("guardarCambios"))
					{
						return this.accionGuardarCambios(centrosAtencionForm, mapping, con, usuario, request);

					}
					else if(estado.equals("eliminar"))
					{
						return this.accionEliminar(centrosAtencionForm, mapping, con, usuario, request);
					}
					else if(estado.equals("cambiarOrden"))
					{
						return this.accionCambiarOrden(centrosAtencionForm, mapping, con);
					}
					else if(estado.equals("recargarCiudad"))
					{
						centrosAtencionForm.setEstado(centrosAtencionForm.getEstadoAnterior());
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("principal");
					}

					//<<<<<<<< 	ESTADO PARA EL MANEJO DE LOS CONSECUTIVOS >>>>>>>>>>>>><<


					else if(estado.equals("cargarHistoricoTesoria"))
					{
						return  cargarHistoricosConsecutivos(centrosAtencionForm, mapping);	
					}
					else if(estado.equals("cargarHistorioFacturacion"))
					{
						return cargarConsecutivoHistoricoFacturacion(centrosAtencionForm, usuario, mapping);
					}
					else if(estado.equals("cargarHistoricoFacturasVarias"))
					{
						return cargarConsecutivosHistoricoFacturasVarias(centrosAtencionForm, usuario, mapping);
					}

					//<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<

					else if(estado.equals("modificarDetalleConsecutivo"))
					{
						return accionModificarConsecutivo(centrosAtencionForm, usuario, mapping);
					}

					else if(estado.equals("eliminarConsecutivo"))
					{
						return accionEliminarConsecutivo(centrosAtencionForm, usuario, mapping);
					}


					else if(estado.equals("guardarConsecutivo"))
					{
						return accionGuardarConsecutivo(centrosAtencionForm, usuario, mapping, request, errores);
					}

					else if(estado.equals("recargarConsecutivo"))
					{
						this.ordenarListaHistoricosConsecutivos(centrosAtencionForm);
						logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
						logger.info(" 	RECARGA CENTRO DE ATENCION");
						logger.info("      \n\n\n\n\n \t\t\t  forma  getPostConsecutivo---->>>>>"+centrosAtencionForm.getPostConsecutivo());
						centrosAtencionForm.setEstado(centrosAtencionForm.getEstadoAnterior());
						return mapping.findForward("principal");
					}

					//>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<< NUEVO CONSECUTIVO >>>>>>>>>>>>>>>>>>>><<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>><

					else if(estado.equals("nuevoConsecutivo"))
					{
						return this.accionNuevoConsecutivo(centrosAtencionForm, usuario, mapping); 
					}
					else if(estado.equals("guardarNuevoConsecutivo"))
					{
						return this.accionAdicionarNuevoConsecutivo(centrosAtencionForm, usuario, mapping, errores, request);
					}


					//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<MODIFICAR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<

					else if(estado.equals("modificarHistorico"))
					{
						return this.modificarHistorica(centrosAtencionForm, usuario, mapping);
					}


					else if (estado.equals("guardarModificacionHistorico"))
					{
						return this.accionGuardarModificarHistorica(centrosAtencionForm, usuario, mapping, errores, request);
					}



					//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<ELMINAR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><

					else if(estado.equals("eliminarHistorico"))
					{
						return	this.eliminarHistoricos(centrosAtencionForm, usuario, mapping, errores, request);
					}


					else if(estado.equals("cargarTodosConsecutivos"))
					{
						centrosAtencionForm.setConsecutivo( Utilidades.convertirAEntero(centrosAtencionForm.getCodigoConsecutivoCentro()));
						cargarConsecutivos(centrosAtencionForm, usuario.getCodigoInstitucionInt());
						centrosAtencionForm.setEsConsulta(Boolean.TRUE);
						return mapping.findForward("consultarConsecutivo");

					}
					else if(estado.equals("prueba1"))
					{
						return mapping.findForward("detConsecutivo");
					}
					//>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<< NO HAY ESTADO >>>>>>>>>>>>>>>>>>>>><<<<<<<<<<>>>>>>>>>>>>>><
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}

				} catch(SQLException e) {
					logger.warn("No se pudo abrir la conexión"+e.toString());
					centrosAtencionForm.clean();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");

				} finally {

					UtilidadBD.closeConnection(con);  
				} 
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}


	
	/**
	 * 	GUARDAR EN MEMORIA CONSECUTIVOS CENTROS DE ATENCION
	 * @param centrosAtencionForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarConsecutivo(	CentrosAtencionForm forma, UsuarioBasico usuario,		ActionMapping mapping, HttpServletRequest request , ActionErrors errores ) 
	{
		
		//EVENTO ENVIAR 
		
		//SETTEO ANIOS
		forma.setAnioTmpDto(Utilidades.convertirAEntero(forma.getDtoConsecutivo().getAnio()));
		forma.setAnioSistema(utilidadAnio());
		
		logger.info(" \n\n\n\n");
		logger.info("\t\t anio "+forma.getDtoConsecutivo().getAnio());		
		logger.info("\t\t Consecutivo"+forma.getDtoConsecutivo().getConsecutivo());
		logger.info(" \n\n\n\n");
		
		//VALIDACION DE ANIO MENOR AL SISTEMA
		if( accionMostrarErrorConsecutivoMenor(errores, request, forma))
		{
			forma.setEstado(forma.getEstadoAnterior());
			return mapping.findForward("principal");
		}
		
		// HACER LA VALIDACION RESPECTIVA
		Boolean sePuedeAdcionar= Boolean.TRUE;
		sePuedeAdcionar = accionValidarAnioConsecutivo(forma, sePuedeAdcionar); // VALIDA QUE NO SE REPITA EL CONSECUTIVO
		
		// EMPEZAR LA MODIFICARCION
		if(sePuedeAdcionar)
		{
			// ADICIONAR UN NUEVO CONSECUTIVO 
			adicionarListaConsecutivos(forma);
			// ADICIONAR UN HISTORICO
			adicionarNewDtoHistorico(forma);
		}
		else
		{
			if(forma.isIdSePuedeModificar())
			{
				//MODIFICAR LA LISTA CONSECUTIVOS 
				modificarConsecutivosEventoEnviar(forma);
				//MODIFICAR LA LISTA DE HISTORICOS
				accionModificarListaHistoricosEventoEnviar(forma);
			}
			else
			{
				errores.add("", new ActionMessage("errors.notEspecific", "No Se Puede Guardar El Consecutivo"));
				saveErrors(request, errores);
			}
		}
		
		
		//forma.setEstado("recargarConsecutivo");
		
		//return mapping.findForward("principal");
		forma.setEstado(forma.getEstadoAnterior());
		this.ordenarListaHistoricosConsecutivos(forma);
    	return mapping.findForward("principal");
	}
	
	
	
	
	
	
//	/**
//	 * 
//	 * @param forma
//	 */
//	private void accionModificarListaHistoricosGuardar( CentrosAtencionForm forma)
//	{
//			int tmp=0;
//			logger.info("RECORRER LISTA HISTORICOS ");
//			
//			for(int i=0; i<forma.getListaHistoricos().size(); i++) 
//			{	
//				if( forma.getListaHistoricos().get(i).getEsVisible().equals(ConstantesBD.acronimoSi) && forma.getListaHistoricos().get(i).getActivo().equals(ConstantesBD.acronimoSi) )
//				{
//				
//					if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()) )
//					{
//						logger.info("SI EXISTE MODIFICAR CONSECUTIVO ");
//						if( forma.getListaHistoricos().get(i).getAnio().equals(forma.getDtoConsecutivo().getAnio()))
//						{
//							forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
//							forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
//							forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
//						}
//					}
//					else
//					{
//						// CUANDO NO TIENE ANIO
//						//SOLO APLICA PARA UN CONSECUTIVO
//						logger.info(" ----------------------MODIFICAR EL CONSECUTIVO------------ ");
//						forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
//						forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
//						forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
//					}
//					tmp++;
//				}
//			}
//	}
		
	
		
	
	
	/**
	 * ACCION MODIFICAR EL CONSECUTIVO  DE LA LISTA DE HISTORICOS
	 * @param forma
	 */
	private void accionModificarListaHistoricosEventoEnviar( CentrosAtencionForm forma)
	{
		
		
			for(int i=0; i<forma.getListaHistoricos().size(); i++) 
			{	
				if( forma.getListaHistoricos().get(i).getEsVisible().equals(ConstantesBD.acronimoSi) && forma.getListaHistoricos().get(i).getActivo().equals(ConstantesBD.acronimoSi) )
				{
				
					//1.BUSCAR EL ANIO PARA EL CONSECUTIVO MODIFICANDO 
					if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()) )
					{
						logger.info("SI EXISTE MODIFICAR CONSECUTIVO ");
					
						//1.1. MODIFICACION DEL CONSECUTIVO 
						if( forma.getListaHistoricos().get(i).getAnio().equals(forma.getDtoConsecutivo().getAnio())) // COMPARAMOS POR ANIO
						{
							forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
							forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
							forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
						}
						//1.2 MODIFICACION DEL ANIO
						else if(forma.getListaHistoricos().get(i).getAnio().equals(String.valueOf(forma.getAnioSistema()))) //COMPARAMOS CON ANIO EN VIGENCIA
						{
							forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
							forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
							forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
						}
						
						
						
					}
					
					// 2. MODIFICAR SOLAMENTE UN CONSECUTIVO, APLICANDO PARA ID ANUAL EN SI   
					if( forma.getIdConsecutivoAnual().equals(ConstantesBD.acronimoSi))
					{
						
						forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
						forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
						forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
					}
					//3. MODIFICAR CONSECUTIVO, APLICANDO PARA ID ANUAL EN NO 
					if (  (UtilidadTexto.isEmpty(forma.getListaHistoricos().get(i).getAnio())) && (forma.getIdConsecutivoAnual().equals(ConstantesBD.acronimoNo)) )
					{
						forma.getListaHistoricos().get(i).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
						forma.getListaHistoricos().get(i).setAnio(forma.getDtoConsecutivo().getAnio());
						forma.getListaHistoricos().get(i).setIdAnual(forma.getIdConsecutivoAnual());
					}
						
				}
			}
			
			
			
	}
	
	
	/**
	 * 
	 * MODIFICAR LA LISTA DE LOS CONSECUTIVOS DE FACTURACION Y TESORERIA
	 * @param forma
	 * Busca en toda la lista para modificar los consecutivos de facuturacion o tesoreria
	 */
	private void modificarConsecutivos(CentrosAtencionForm forma)
	{
		
		for(DtoConsecutivoCentroAtencion dto: forma.getListaHistoricos())
		{
			if( dto.getActivo().equals(ConstantesBD.acronimoSi) && dto.getEsVisible().equals(ConstantesBD.acronimoSi))
			{
				if ( dto.getAnio().equals(forma.getDtoConsecutivo().getAnio()))
				{
					adicionarListaConsecutivosModificar(forma);
				}
				if( UtilidadTexto.isEmpty(dto.getAnio()) && dto.getConsecutivo().doubleValue()>0)
				{
					adicionarListaConsecutivosModificar(forma);
				}
			}
			
		}
	}

	/**
	 * 
	 * MODIFICAR LA LISTA DE LOS CONSECUTIVOS DE FACTURACION Y TESORERIA
	 * @param forma
	 * Busca en toda la lista para modificar los consecutivos de facuturacion o tesoreria
	 */
	private void modificarConsecutivosEventoEnviar(CentrosAtencionForm forma)
	{
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString()) 
		   || forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			this.modificarListaFacturacionOFacturaVaria(forma);
		} 
		else if( forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Tesoreria.toString()))
		{
			this.modificarListaTesoreria(forma);
			
		}
	}
	

	/**
	 * ADICIONAR A LA LISTA DE CONSECUTIVOS
	 *	DEPENDIENDO DEL TIPO DE CONSECUTIVO SE ADICCIONA A LA LISTA  
	 * @param forma
	 * @param request
	 * @param errores
	 * @param anio
	 * @param anio2
	 */
	private void adicionarListaConsecutivos(CentrosAtencionForm forma) 
	{
		
		// ADICIONAR CONSECUTIVO PARA LAS LISTA DE FACTURACION O TESORIA
		
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString())
			|| forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			adicionarAListaFacturaOFacturasVarias(forma);
		} 
		else if( forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Tesoreria.toString()))
		{
			adicionarListaTesoreria( forma);
		}
	}
	

	/**
	 * ADICIONAR A LA LISTA DE CONSECUTIVOS
	 *	DEPENDIENDO DEL TIPO DE CONSECUTIVO SE ADICCIONA A LA LISTA  
	 * @param forma
	 
	 	 
	 */
	private void adicionarListaConsecutivosModificar(CentrosAtencionForm forma) 
	{
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString())
		 || forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			adicionarAListaFacturaOFacturasVariasModificar(forma);
		
		}else if( forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Tesoreria.toString()))
		{
			adicionarListaTesoreriaModificar( forma);
			//accionMostrarErrorAnio(errores, request, anio, anio2);
		}
	
	}

	
	/**
	 * ADICCIONAR A LA LISTA DE FACTURACION o
	 * a la lista de Facturas Varias
	 * @param forma
	 * @param anio
	 * @param anio2
	 */
	private void adicionarAListaFacturaOFacturasVarias(CentrosAtencionForm forma)
	{
		/**
		 * CUANDO EXISTEN ANIO 
		 */
		//1. SE ADICIONA  CON ANIO, SOLO SI EL ANIO ES ACTUAL
		if(forma.getAnioTmpDto()>0)
		{	
			if(forma.getAnioSistema()==forma.getAnioTmpDto())
			{
				guardarListaFacturacionOFacturaVaria(forma);
			}
		}
		//2. SE ADICIONA 
		// VALIDAR EL ID ANUAL
		else
		{
			logger.info("****************** ADICION-------????????  FACTURACION *************************");
			guardarListaFacturacionOFacturaVaria(forma);
		}
	
	}

	/**
	 * ADICCIONAR A LA LISTA DE FACTURACION
	 * @param forma
	 * @param tipo 
	 * @param anio
	 * @param anio2
	 */
	private void adicionarAListaFacturaOFacturasVariasModificar(CentrosAtencionForm forma)
	{
		/*
		 * SOLO MODIFICAR SI TIENE EL ANIO ACTUAL
		 */

		if(forma.getAnioSistema()==forma.getAnioTmpDto())
		{
			if(!forma.isExisteAnioRegistrado())
			{
				guardarListaFacturacionOFacturaVaria(forma);
			}
		}
		else
		{
		
			if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
			{
				
				if(forma.getListaHistoricos().get(forma.getPostHistoricio()).getAnio().equals(String.valueOf(forma.getAnioSistema()))) 
				{
					if(!forma.isExisteAnioRegistrado()){
						
						if(forma.isConsecutivoFactura()){
							
							forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio("");
							forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
							forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo); 
							
						}else if (forma.isConsecutivoFacturaVaria()){
							
							forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio("");
							forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
							forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo); 
						}
					}
				}
			}
			else
			{
		
				if(!forma.isExisteAnioRegistrado())
				{
					logger.info("\n\n\n\n\n MODIFICAR CENTRO FACTURA \n\n\n\n\n ");
					guardarListaFacturacionOFacturaVaria(forma);
				}
			}
		}
	}


	/***************************************************************************************************************************
	 * MODIFICAR LISTA FACTURACION
	 * @param forma
	 */
	private void modificarListaFacturacionOFacturaVaria(CentrosAtencionForm forma)
	{
		// VALIDAMOS QUE LA MODIFICACION APLIQUE PARA EL ANIO ACTUAL
		
		forma.setAnioTmpDto(Utilidades.convertirAEntero(forma.getDtoConsecutivo().getAnio()));
		forma.setAnioSistema(utilidadAnio());
		
		
		// CASO 1. APLICA CUANDO EL ANIO ES IGUAL AL ANIO DEL SISTEMA
		if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			if(forma.getAnioTmpDto()==forma.getAnioSistema())
			{
				guardarListaFacturacionOFacturaVaria(forma);
			}
			else
			{	
				//LIMPIAR CONSECUTIVO
				
				if(forma.isConsecutivoFactura()){
				
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo( BigDecimal.ZERO);
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio("");
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
					
				}else if(forma.isConsecutivoFacturaVaria()){
					
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo( BigDecimal.ZERO);
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio("");
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
				}
			}
		}
		else
		{
			// CASO 2. CUANDO EL CONSECUTIVO APLICA PARA EL ID ANUAL 
			guardarListaFacturacionOFacturaVaria(forma);
		}
		
	}

	
	private void guardarListaFacturacionOFacturaVaria(CentrosAtencionForm forma) 
	{
		if(forma.isConsecutivoFactura()){
			
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
			
		}else if(forma.isConsecutivoFacturaVaria()){
			
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
		}
	}

	
	/***************************************************************************************************************************
	 * MODIFICAR LISTA FACTURACION
	 * @param forma
	 */
	private void modificarListaTesoreria(CentrosAtencionForm forma)
	{
		
		if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			if(forma.getAnioTmpDto()==forma.getAnioSistema())
			{
				guardarListaTesoreria(forma);
			}
			else
			{ 
				//LIMPIAR DATOS
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio("");
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
			}
		}
		else
		{
			guardarListaTesoreria(forma);
		}
	}


	private void guardarListaTesoreria(CentrosAtencionForm forma) {
		
		forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
		forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
		forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
	}
	
	
	
	/**
	 * ADICCIONAR A LA LISTA DE TESORERIA
	 * @param forma
	 * @param anio
	 * @param anio2
	 */
	private void adicionarListaTesoreriaModificar(CentrosAtencionForm forma)
	{
		if(forma.getAnioSistema()==forma.getAnioTmpDto())
		{
			if(!forma.isExisteAnioRegistrado())
			{
				guardarListaTesoreria(forma);
			}
		}
		else
		{
		
			if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
			{
			
				if(forma.getListaHistoricos().get(forma.getPostHistoricio()).getAnio().equals(String.valueOf(forma.getAnioSistema())))
				{
					if(!forma.isExisteAnioRegistrado())
					{
						forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio("");
						forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
						forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
					}
				}
		
			}
			else
			{
				guardarListaTesoreria(forma);
			}
		}
	}

	/**
	 * ADICIONAR A LA LISTA DE TESORERIA
	 * @param forma
	 * @param anio
	 * @param anio2
	 */
	private void adicionarListaTesoreria(CentrosAtencionForm forma)
	{
		//1. ANIO ACTUAL 
		if(forma.getAnioTmpDto()>0)
		{
			if(forma.getAnioSistema()==forma.getAnioTmpDto())
			{
				guardarListaTesoreria(forma);
			}
		}
		//2. ADICIONAR NORMAL
		else
		{	
			guardarListaTesoreria(forma);
		}
	}


	/**
	 * CARGAR HISTORICO CONSECUTIVO HISTORICO FACTURACION
	 * @param centrosAtencionForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarConsecutivoHistoricoFacturacion(CentrosAtencionForm forma, UsuarioBasico usuario, 	ActionMapping mapping) 
	{
		
		logger.info(" \n\n\tconcecutivo -------->>>>>>>>>>>>>><<<"+forma.getPostConsecutivo());
		forma.setDtoConsecutivo(forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).clone());
		forma.setTipoConsecutivo(EmunTipoConsecutivo.Facturacion.toString());
		
		//Cambio Sonria
		if(forma.getConsecutivoModificado()==null || forma.getConsecutivoModificado().longValue()<=0)
		{
			forma.setConsecutivoModificado(forma.getDtoConsecutivo().getConsecutivo());
		}

		definirTipoConsecutivo(forma);
		
		forma.setTmpNombreBDConsecutivo(forma.getDtoConsecutivo().getNombreConsecutivo());
		forma.setEstado("cargarConsecutivo");
		
		forma.setIdConsecutivoAnual(ConstantesBD.acronimoNo);
		forma.setIdConsecutivoAnual(forma.getDtoConsecutivo().getIdAnual()); 
		
		this.filtra(forma);
		return mapping.findForward("detConsecutivo"); 
	}
	
	/**
	 * CARGAR HISTORICO CONSECUTIVO HISTORICO FACTURACION
	 * @param centrosAtencionForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarConsecutivosHistoricoFacturasVarias(CentrosAtencionForm forma, UsuarioBasico usuario, 	ActionMapping mapping) 
	{
		
		logger.info(" \n\n\tconcecutivo -------->>>>>>>>>>>>>><<<"+forma.getPostConsecutivo());
		forma.setDtoConsecutivo(forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).clone());
		forma.setTipoConsecutivo(EmunTipoConsecutivo.FacturasVarias.toString());
		
		definirTipoConsecutivo(forma);
		
		forma.setTmpNombreBDConsecutivo(forma.getDtoConsecutivo().getNombreConsecutivo());
		forma.setEstado("cargarConsecutivo");
		
		forma.setIdConsecutivoAnual(ConstantesBD.acronimoNo);
		forma.setIdConsecutivoAnual(forma.getDtoConsecutivo().getIdAnual()); 
		
		this.filtra(forma);
		return mapping.findForward("detConsecutivo"); 
	}

	/**
	 * LIMPIA LA INFORMACION DEL DTO CONSECUTIVO
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionNuevoConsecutivo(CentrosAtencionForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		String nombreConsecutivoInterfazGrafica = forma.getDtoConsecutivo().getNombreConsecutivoInterfazGrafica();
		forma.setDtoConsecutivo(new DtoConsecutivoCentroAtencion());
		forma.getDtoConsecutivo().setNombreConsecutivoInterfazGrafica(nombreConsecutivoInterfazGrafica);
		this.ordenarListaHistoricosConsecutivos(forma);
		return mapping.findForward("detConsecutivo");
	}
	

	
	/***************************************************************************************************************************************
	 * ACCION ADICIONAR NUEVO CONSECUTIVOS 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAdicionarNuevoConsecutivo(CentrosAtencionForm forma, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores, HttpServletRequest request)
	{
	
		//EVENTO GUARDAR CONSECUTIVO
		
		errores = new ActionErrors();
		forma.setEstado("mostrarLista");
		boolean sePuedeAdcionar= Boolean.TRUE;
		
		if( accionMostrarErrorConsecutivoMenor(errores, request, forma))
		{
			return mapping.findForward("detConsecutivo");
		}
		
		if(forma.getDtoConsecutivo().getConsecutivo()==null ||
				forma.getDtoConsecutivo().getConsecutivo().intValue() <=0){
				
			forma.setEstado("Error");
			errores.add("", new ActionMessage("errors.integerMayorQue", "El Consecutivo", "0"));
			saveErrors(request, errores);
				
			return mapping.findForward("detConsecutivo");
		}
				
		forma.setAnioSistema(utilidadAnio());
		forma.setAnioTmpDto(Utilidades.convertirAEntero(forma.getDtoConsecutivo().getAnio()));
		forma.getDtoConsecutivo().setNombreConsecutivo(forma.getTmpNombreBDConsecutivo());
		
		sePuedeAdcionar = accionValidarAnioConsecutivoNuevo(forma, sePuedeAdcionar);

		
		if(sePuedeAdcionar)
		{
			modificarConsecutivosPostulacion(forma); // MODIFICAR EL CONSECUTIVO 
			adicionarNewDtoHistorico(forma); // ADICIONAR NEW DTO HISTORICOS
			String nombreConsecutivoInterfazGrafica = forma.getDtoConsecutivo().getNombreConsecutivoInterfazGrafica();
			forma.setDtoConsecutivo(new DtoConsecutivoCentroAtencion());
			forma.getDtoConsecutivo().setNombreConsecutivoInterfazGrafica(nombreConsecutivoInterfazGrafica);
		}
		else
		{
			if(forma.isExisteConsecutivoAnual())
			{
				forma.setEstado("Error");
				errores.add("", new ActionMessage("errors.notEspecific", "Ya Existe Un Consecutivo Anual"));
				saveErrors(request, errores);
			}
			else
			{
				accionMostrarErrorMismoAnio(errores, request, forma);
			}
		}
		
		return mapping.findForward("detConsecutivo");
	}


	/**
	 * NO SE PERMITE INGRESAR ANIOS MENORES QUE EL SISTEMA
	 * @param errores
	 * @param request
	 */
	private boolean accionMostrarErrorConsecutivoMenor(ActionErrors errores, HttpServletRequest request, CentrosAtencionForm forma) 
	{
		boolean retorna=Boolean.FALSE;
		
		logger.info(" EXISTE UN CONSECUTIVO ERROR ?");
		
		
		if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			logger.info("\n\n\n\n\n\n\n\n\n");
			logger.info("Dto anio================>>>>"+forma.getDtoConsecutivo().getAnio()+"  forma.getAnioSistema()================>>>>"+forma.getAnioSistema());
			logger.info("\n\n\n\n\n\n\n\n\n");
			
			if( Integer.parseInt(forma.getDtoConsecutivo().getAnio())<forma.getAnioSistema())
			{
				forma.setEstado("Error");
				retorna=Boolean.TRUE;
				errores.add("", new ActionMessage("errors.notEspecific", " Año Es Menor Que El Año Del Sistema"));
				saveErrors(request, errores);
			}
			if( forma.getDtoConsecutivo()!=null &&  forma.getDtoConsecutivo().getConsecutivo()!=null &&
					forma.getDtoConsecutivo().getConsecutivo().longValue() < forma.getConsecutivoModificado().longValue())
			{
				forma.setEstado("Error");
				retorna=Boolean.TRUE;
				errores.add("", new ActionMessage("errors.notEspecific", "Consecutivo Es Menor al anterior"));
				saveErrors(request, errores);
			}
		}
		return retorna;
	}



	
	/**
	 * MUESTRA LOS ERRORES QUE PUEDE ENCONTRAR
	 * @param errores
	 * @param request
	 */
	private void accionMostrarErrores(ActionErrors errores, HttpServletRequest request, CentrosAtencionForm forma, String error) 
	{
		if(!UtilidadTexto.isEmpty(error))
		{
			forma.setEstado("Error");
			errores.add("", new ActionMessage("errors.notEspecific", error));
			saveErrors(request, errores);
			
		}
	}
	
	/**
	 * 
	 * @param errores
	 * @param request
	 */
	private void accionMostrarErrorMismoAnio(ActionErrors errores, HttpServletRequest request, CentrosAtencionForm forma) 
	{
		logger.info(" EXISTE UN CONSECUTIVO ERROR ");
		forma.setEstado("Error");
		errores.add("", new ActionMessage("errors.notEspecific", "No Se Puede Guardar Un Consecutivo Con el Mismo Año de Vigencia"));
		saveErrors(request, errores);
	}






	/**
	 * 	ACCION MOSTRAR MOSTRAR ERRORES ANIO
	 * @param errores
	 * @param request
	 * @param anio
	 * @param anio2
	 */
	
	@SuppressWarnings("unused")
	private void accionMostrarErrorAnio(CentrosAtencionForm forma ,ActionErrors errores,	HttpServletRequest request) {
	
		
		if( !UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			if(forma.getAnioTmpDto()<forma.getAnioSistema())
			{
				logger.info("ANIO MAL INGRESADO ");
				errores.add("", new ActionMessage("errors.notEspecific", "No Se Puede Guardar Un Consecutivo Con Un Año Menor Al Del Sistema "));
				saveErrors(request, errores);
			}
		}
	}

	
	/**
	 * ADICIONAR CONSECUTIVO HISTORIAL
	 * @param forma
	 */
	@SuppressWarnings("unused")
	private void accionAdicionarConsecutivoHistoria(CentrosAtencionForm forma) {
			adicionarHistorico(forma);
	}



	/**-******************************************************************************************************************************
	 * 	ADICIONAR HISTORICOS
	 * @param forma
	 */
	
	private void adicionarHistorico(CentrosAtencionForm forma) {
		
		adicionarNewDtoHistorico(forma);
	}

	/**
	 * 
	 * @param forma
	 */
	private void adicionarNewDtoHistorico(CentrosAtencionForm forma) {
		
		logger.info("ADICIONAR UN NUEVO CONSECUTIVO HISTORICO");
		forma.getDtoConsecutivo().setEsVisible(ConstantesBD.acronimoSi);
		forma.getDtoConsecutivo().setActivo(ConstantesBD.acronimoSi);
		forma.getDtoConsecutivo().setNombreConsecutivo(forma.getTmpNombreBDConsecutivo());
		forma.getDtoConsecutivo().setIdAnual(forma.getIdConsecutivoAnual());
		forma.getListaHistoricos().add(forma.getDtoConsecutivo());
	}



	/****************************************************************************************************************************************
	 * ACCION VALIDAR CONSECUTIVO ANIO
	 * 
	 * VALIDA SI SE PUEDE INSERTAR O SE PUEDE MODIFICAR
	 * @param forma
	 * @param sePuedeAdcionar
	 * @return
	 */
	private boolean accionValidarAnioConsecutivo(CentrosAtencionForm forma,	boolean sePuedeAdcionar) 
	{
		
		//VALIDACION PARA ENVIAR
		int tmp=0;
		
		
		// 1. SE MODIFICA PARA SOLO PARA EL ANIO DE VIGENCIA
		for(DtoConsecutivoCentroAtencion dto: forma.getListaHistoricos())
		{
			if( dto.getActivo().equals(ConstantesBD.acronimoSi) && dto.getEsVisible().equals(ConstantesBD.acronimoSi) )
			{
				  if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()) )
				  {
						// SOLO SE MODIFICA EL ANIO EN VIGENCIA
					  	if(     ( dto.getAnio().equals(forma.getDtoConsecutivo().getAnio()))     &&      (dto.getAnio().equals(forma.getAnioSistema()+""))   ) //Ya Existe
						{
							 sePuedeAdcionar=Boolean.FALSE; //NO SE PUEDE ADICIONAR ; 
							 forma.setIdSePuedeModificar(Boolean.TRUE);//  SE PUEDE MODIFICAR
						}
					  	//VALIDA QUE NO SE REPITAN ANIOS DIFERENTES DE AL ACTUAL
					  	if( !dto.getAnio().equals(forma.getAnioSistema()+"")) 
					  	{		
					  		if( dto.getAnio().equals(forma.getDtoConsecutivo().getAnio())  )
					  		{
					  			sePuedeAdcionar=Boolean.FALSE;
					  			forma.setIdSePuedeModificar(Boolean.FALSE);
					  		}
					  	}
					  	
				  }
				  tmp++;
			}
		}
		
		// 2. SE PUEDE MODIFICAR SOLO SI NO EXISTEN MAS ANIOS
		if( tmp==1)
		{	
			sePuedeAdcionar=Boolean.FALSE; // NO SE PUEDE ADICIONAR 
			forma.setIdSePuedeModificar(Boolean.TRUE); // SE PUEDE MODIFICAR
		}
	
		else if(tmp>1 && UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			sePuedeAdcionar=Boolean.FALSE; // NO SE PUEDE ADICIONAR 
			forma.setIdSePuedeModificar(Boolean.FALSE); // NO SE PUEDE MODIFICAR
		}
		
		
		
		return sePuedeAdcionar;
	}
	
	
	
	
	/****************************************************************************************************************************************
	 * ACCION VALIDAR CONSECUTIVO ANIO
	 * 
	 * VALIDA SI SE PUEDE INSERTAR UN NUEVO CONSECUTIVO
	 * @param forma
	 * @param sePuedeAdcionar TRUE Y/O FALSE
	 * @return
	 */
	private boolean accionValidarAnioConsecutivoNuevo(CentrosAtencionForm forma,	boolean sePuedeAdcionar) 
	{
		
		int tmp=0; 
		//VALIDACION PARA ENVIAR
		forma.setExisteConsecutivoAnual(Boolean.FALSE);
		forma.setAplicaConsecutivoAnaul(Boolean.FALSE);
		
		// 1. SE MODIFICA PARA SOLO PARA EL ANIO DE VIGENCIA
		for(DtoConsecutivoCentroAtencion dto: forma.getListaHistoricos())
		{
			if( dto.getActivo().equals(ConstantesBD.acronimoSi) && dto.getEsVisible().equals(ConstantesBD.acronimoSi) )
			{
				if(!UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()) )
				{
					if(dto.getAnio().equals(forma.getDtoConsecutivo().getAnio())) //Ya Existe anios registrados
					{
						sePuedeAdcionar=Boolean.FALSE; //NO SE PUEDE ADICIONAR ; 
					}
				}

				 //SI EXISTE  UN CONSECUTIVOS ANUAL YA REGISTRADO NO SE PUEDE ADICIONAR MAS
				if(dto.getIdAnual().equals(ConstantesBD.acronimoNo))
				{
					// 2. SE PUEDE MODIFICAR SOLO SI NO EXISTEN MAS ANIOS 
					 forma.setExisteConsecutivoAnual(Boolean.TRUE);//
					 sePuedeAdcionar=Boolean.FALSE;//
				 }
				
			tmp++;
			}
		}
		/**
		 * 
		 */
		
		
		if(tmp>0 &&  UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			sePuedeAdcionar=Boolean.FALSE;//
		}
		
		return sePuedeAdcionar;
	}
	
	

	/*************************************************************************************************************************************
	 * 	ADICIONAR O/MODIFICAR CONSECUTIVOS DE TESORIA O FACTURACION
	 */
	private void modificarConsecutivosPostulacion(CentrosAtencionForm forma)
	{
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString())
			|| forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			if(forma.getAnioSistema()==forma.getAnioTmpDto())
			{
				if(forma.isConsecutivoFactura()){
					
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
					forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
					
				}else if (forma.isConsecutivoFacturaVaria()){
					
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
					forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
				}
			}
			else 
			{
				if(	UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()) )
				{
					// TODO MODIFICAR SI EXISTE CONSECUTIVO IGUAL EN EL SISTEMA.
					
					if(forma.isConsecutivoFactura()){
						
						forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
						forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
						forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
					
					}else if(forma.isConsecutivoFacturaVaria()){
						
						forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
						forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
						forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
					}
				}		
			}
		}
		else if( forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Tesoreria.toString()))
		{
			if(forma.getAnioSistema()==forma.getAnioTmpDto())
			{
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
				forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
			}
			else 
			{
				// TODO MODIFICAR SI EXISTE CONSECUTIVO IGUAL EN EL SISTEMA.
				if(UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
				{
					forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
					forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio(forma.getDtoConsecutivo().getAnio());
					forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(forma.getIdConsecutivoAnual());
				}
			}
		}
	}
	
	
	
	
	/**
	 * CARGAR EN DTO CON EL LA INFORMACION DE LA LISTA DE HISTORICOS 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward modificarHistorica(CentrosAtencionForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setDtoConsecutivo(forma.getListaHistoricos().get(forma.getPostHistoricio()).clone());
		this.ordenarListaHistoricosConsecutivos(forma);
		return mapping.findForward("detConsecutivo");
	}
	
	
	
	
	/**
	 * MODIFICAR LA LISTA DE HISTORICOS EN MEMORIA
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarModificarHistorica(CentrosAtencionForm forma, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores , HttpServletRequest request)
	{
		
		forma.setEstado("mostrarLista");
		String tmpErrores;
		
		//VALIDACION DEL ANIO DEL SISTEMA
		if( accionMostrarErrorConsecutivoMenor(errores, request, forma))
		{
			return mapping.findForward("detConsecutivo");
		}
		
		
		forma.setExisteAnioRegistrado(Boolean.FALSE); //limpiamos
		Boolean sePuedeModificar=Boolean.TRUE;
		
		
		sePuedeModificar = accionSePuedeModificar(forma, sePuedeModificar);
		
		forma.setAnioSistema(utilidadAnio());
		forma.setAnioTmpDto(Utilidades.convertirAEntero(forma.getDtoConsecutivo().getAnio()));
		
		/*
		 * ADICIONAR SI SE PUEDE MODIFICAR
		 */
		if(sePuedeModificar)
		{
			adicionarListaConsecutivosModificar(forma); //adicionar si aplica para anio vigencia
			tmpErrores= accionModificarLIstaHisotoricos(forma); //modificar lista
			String nombreConsecutivoInterfazGrafica = forma.getDtoConsecutivo().getNombreConsecutivoInterfazGrafica();
			forma.setDtoConsecutivo(new DtoConsecutivoCentroAtencion());
			forma.getDtoConsecutivo().setNombreConsecutivoInterfazGrafica(nombreConsecutivoInterfazGrafica);
			
		}
		else
		{
			logger.info("sePuedeModificar->>>>>>"+sePuedeModificar);
			/*
			 * MODIFICAR LA LISTA DE LOS CONSECUTIVOS DE FACTURACION Y TESORERIA
			 */
			tmpErrores=accionModificarLIstaHisotoricos(forma);
			
			if(UtilidadTexto.isEmpty(tmpErrores))
			{	
				modificarConsecutivos(forma);
			}
		}
		
		accionMostrarErrores(errores, request, forma, tmpErrores);//VALIDAR SI EXISTEN ERRORES PARA MODIFICAR CONSECUTIVOS 
		this.ordenarListaHistoricosConsecutivos(forma); // ORDENAMIENTO DE LISTA
		
		return mapping.findForward("detConsecutivo");
	}


	/**
	 * ACCION MODIFICAR LISTA DE HISTORICOS
	 * @param forma
	 */
	private String accionModificarLIstaHisotoricos(CentrosAtencionForm forma) 
	{
		
		logger.info(" ACCION MODIFICAR CONSECUTIVOS");
		
		String erroresModificar="";
		
		ArrayList<Integer> numeros= new ArrayList<Integer>();
		
		if(forma.getAnioTmpDto()>=forma.getAnioSistema())
		{
			if(!forma.isExisteAnioRegistrado())
			{	
				logger.info("\n\n\n\n >>>>>>>>>>>>>>>LOGGER MODIFICA HISTORICOS<<<<<<<<<<<<<<<<<<----------- ");
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setAnio(forma.getDtoConsecutivo().getAnio());
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setIdAnual(forma.getIdConsecutivoAnual());
			}
			else
			{
				
				logger.info(" \t\t\t\t\t EXISTE ANIO REGISTRADO");
				erroresModificar=" Existe Un Año Registrado";
			}
		}
		else
		{
			if(UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
			{	
				int tmp=0;
				for(int i=0; i<forma.getListaHistoricos().size(); i++) 
				{	
					if( forma.getListaHistoricos().get(i).getEsVisible().equals(ConstantesBD.acronimoSi) && forma.getListaHistoricos().get(i).getActivo().equals(ConstantesBD.acronimoSi) )
					{
						tmp++;
						numeros.add(i);
					}
				}
			}
		}
		
		
		if(numeros.size()>1)
		{
			logger.info(" NO SE PUEDEN MODIFICAR ");
			erroresModificar="No Se Puede Insertar o Modificar Consecutivos Con Años nulos, ya Existe Consecutivos ";
			//TODO NO SE PUEDE MODIFICAR NI INSERTAR CONSECUTIVOS CON ANIO NULOS  EXISTEN CONSECUTIVOS 
		}
		else
		{
			if(!forma.isExisteAnioRegistrado())
			{
				logger.info("\n\n\n\n >>>>>>>>>>>>>>>LOGGER MODIFICA HISTORICOS<<<<<<<<<<<<<<<<<<----------- ");
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setConsecutivo(forma.getDtoConsecutivo().getConsecutivo());
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setAnio(forma.getDtoConsecutivo().getAnio());
				forma.getListaHistoricos().get(forma.getPostHistoricio()).setIdAnual(forma.getIdConsecutivoAnual());
			}
		}
		
		
		
		return erroresModificar;
		
		
		
	}
	
	



	/**
	 **
		VALIDAR QUE NO SE REPITA PARA EL MISMO ANIO
	 * @param forma
	 * @param sePuedeModificar
	 * @return
	 */
	private Boolean accionSePuedeModificar(CentrosAtencionForm forma,	Boolean sePuedeModificar) 
	{
		int tmp=0;
		for(DtoConsecutivoCentroAtencion dto: forma.getListaHistoricos())
		{
			if(dto.getActivo().equals(ConstantesBD.acronimoSi) && dto.getEsVisible().equals(ConstantesBD.acronimoSi)) // ACTIVO Y VISIBLE
			{
				if(forma.getPostHistoricio()!=tmp) //NO SE VALIDAN LOS MISMOS INDICES 
				{
					if(dto.getAnio().equals(forma.getDtoConsecutivo().getAnio())) // YA EXISTE UN MISMO ANIO
					{
						sePuedeModificar=Boolean.FALSE;
						forma.setExisteAnioRegistrado(Boolean.TRUE);
					}
					if(	UtilidadTexto.isEmpty(dto.getAnio())	&&	 dto.getConsecutivo().doubleValue()>0 ) //SI EXISTE UN CONSECUTIVO SIN ANIO 
					{
						sePuedeModificar=Boolean.FALSE;
					}
				}
			}
			tmp++;//indice
		}
		
		//////////////////
		
		
		
		// 2. SE PUEDE MODIFICAR SOLO SI NO EXISTEN MAS ANIOS
		if( tmp==1)
		{	
			sePuedeModificar=Boolean.FALSE; // NO SE PUEDE ADICIONAR 
			forma.setIdSePuedeModificar(Boolean.TRUE); // SE PUEDE MODIFICAR
		}
	
		else if(tmp>1 && UtilidadTexto.isEmpty(forma.getDtoConsecutivo().getAnio()))
		{
			sePuedeModificar=Boolean.FALSE; // NO SE PUEDE ADICIONAR 
			forma.setIdSePuedeModificar(Boolean.FALSE); // NO SE PUEDE MODIFICAR
		}
	
		return sePuedeModificar;
	}
	
	
	
	
	/**
	 * ELIMINA DE LA LISTA SI NO ESTA EN LA BASE DE DATOS
	 * SI ESTA EN LA BASE DE DATOS SE CAMBIA EL ESTADO A INACTIVO
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param errores 
	 * @return
	 */
	private ActionForward eliminarHistoricos(CentrosAtencionForm forma, UsuarioBasico usuario, ActionMapping mapping, ActionErrors errores, HttpServletRequest request)
	{
		ResultadoBoolean validacion = permitirEliminarConsecutivoPorCentroAtencion(forma.getListaHistoricos().get(forma.getPostHistoricio()), usuario.getCodigoInstitucionInt());
		
		if(validacion.isResultado()){
		
		int anio = utilidadAnio();
		
		if( forma.getListaHistoricos().get(forma.getPostHistoricio()).getCodigoPk().doubleValue()<=0 )//SINO ESTAN EN BASE DE DATOS
		{
			if(forma.getListaHistoricos().get(forma.getPostHistoricio()).getAnio().equals(String.valueOf(anio)))
			{
				accionlimpiarlistas(forma);
			}
			else 
			{
				if(validarSiExistenConsecutivosEliminar(forma)==1)
				{
					accionlimpiarlistas(forma);
				}
			}
			
			//TODO VALIDAR SI EXISTEN MAS CONSECUTIVOS
			forma.getListaHistoricos().remove(forma.getPostHistoricio());
		}
		else
		{
			forma.getListaHistoricos().get(forma.getPostHistoricio()).setActivo(ConstantesBD.acronimoNo);
			forma.getListaHistoricos().get(forma.getPostHistoricio()).setEsVisible(ConstantesBD.acronimoNo);
			accionlimpiarlistas(forma);
		}
			
			forma.setConsecutivoModificado(new BigDecimal(ConstantesBD.codigoNuncaValido));

		}else{
			
			mostrarErrorEliminarConsecutivo(forma, errores, request, validacion);
		}
		
		return mapping.findForward("detConsecutivo");
	}




	
	/**
	 *  validar Si Existen Consecutivos para Eliminar
	 * 
	 * @param centro
	 */
	private int  validarSiExistenConsecutivosEliminar(CentrosAtencionForm forma)
	{
		int retorna=0;
		for(DtoConsecutivoCentroAtencion dto: forma.getListaHistoricos())
			{
				if( dto.getActivo().equals(ConstantesBD.acronimoSi) && dto.getEsVisible().equals(ConstantesBD.acronimoSi) )
				{
					retorna++;
				}
			}
		return retorna;
	}
	
	

	/**
	 * 
	 * @return
	 */
	private int utilidadAnio() {
		String tmpAnio[]= UtilidadFecha.getFechaActual().split("/");
		int anio=Utilidades.convertirAEntero(tmpAnio[2]);
		return anio;
	}






	
	
	/**
	 * ACCION LIMPIAR CONSECUTIVOS
	 * @param forma
	 */
	private void accionlimpiarlistas(CentrosAtencionForm forma) {
		
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString()))
		{
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setAnio("");
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
			forma.getListaConsecutivosCentroFacturacion().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
		
		}else if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setAnio("");
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
			forma.getListaConsecutivosCentroFacturasVarias().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
			
		}else if( forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Tesoreria.toString()))
		{
			forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setAnio("");
			forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setConsecutivo(BigDecimal.ZERO);
			forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).setIdAnual(ConstantesBD.acronimoNo);
		}
	}
	
	
	
	
	
	/**
	 * ACCION ELIMINAR CONSECUTIVO HISTORICO
	 * @param centrosAtencionForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConsecutivo( CentrosAtencionForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
			
			ConsecutivosCentroAtencion objConsecutivo = new ConsecutivosCentroAtencion();
			DtoConsecutivoCentroAtencion dto = new DtoConsecutivoCentroAtencion();
			dto.setConsecutivo(forma.getListaHistoricos().get(forma.getPostConsecutivo()).getCodigoPk());
			objConsecutivo.eliminar(dto);
			return mapping.findForward("detConsecutivo"); 
	}

	/**
	 * Método que se encarga de cargar el Error cuando no es posible
	 * eliminar un consecutivo específico
	 * 
	 * @param forma
	 * @param errores
	 * @param request
	 * @param validacion 
	 */
	private void mostrarErrorEliminarConsecutivo(CentrosAtencionForm forma, ActionErrors errores, HttpServletRequest request, ResultadoBoolean validacion) {
	
		forma.setEstado("Error");
		errores.add("", new ActionMessage("errors.notEspecific", validacion.getDescripcion()));
		saveErrors(request, errores);
	}
	
	/**
	 * Método que permite determinar si se puede eliminar un registro de Consecutivo.
	 * Teniendo en cuenta que si el consecutivo esta definido para un Año específico, no se encuentre usado.
	 * En el caso de no manejar Año, se tiene en cuenta que el valor a ingresar sea mayor que el último
	 * consecutivo usado.
	 * 
	 * @param dtoConsecutivoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	private ResultadoBoolean permitirEliminarConsecutivoPorCentroAtencion(DtoConsecutivoCentroAtencion dtoConsecutivoCentroAtencion, int codigoInstitucion) {

		ResultadoBoolean validacion = UtilidadBD.isUtilizadoConsecutivoPorAnioCentroAten(dtoConsecutivoCentroAtencion.getNombreConsecutivo(), 
				codigoInstitucion, dtoConsecutivoCentroAtencion.getCentroAtencion().getCodigo(), dtoConsecutivoCentroAtencion.getAnio());
		
		MessageResources mensages=MessageResources.getMessageResources("com.servinte.mensajes.manejopaciente.CentroAtencionForm");
		
		if(validacion!=null && !validacion.isResultado()){
			
			if(UtilidadTexto.isEmpty(dtoConsecutivoCentroAtencion.getAnio())){
				
				/*
				 * Se tiene en cuenta que cuando no se utiliza consecutivo por año.
				 * Se permite modificar solo si el valor a ingresar es mayor que el 
				 * último consecutivo usado
				 */
				if(UtilidadTexto.isNumber(validacion.getDescripcion())){
					
					int ultimoValorUsado = Integer.parseInt(validacion.getDescripcion());
					
					if(dtoConsecutivoCentroAtencion.getConsecutivo()!=null &&
							dtoConsecutivoCentroAtencion.getConsecutivo().intValue() > ultimoValorUsado){
						
						validacion.setResultado(true);
						
					}else{
						
						validacion.setDescripcion(mensages.getMessage("CentroAtencionForm.mensajeInformativo.noPermiteEliminarConsecutivoMenor"));
					}
				}
			}else{
				
				validacion.setDescripcion(mensages.getMessage("CentroAtencionForm.mensajeInformativo.noPermiteEliminarConsecutivoUsado"));
			}
		}
	
		return validacion;
	}	
	
	
	
	


	/**
	 * ACCCION MODIFICAR CONSECUTIVO HISTORICO
	 * @param centrosAtencionForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarConsecutivo( CentrosAtencionForm forma, UsuarioBasico usuario,	ActionMapping mapping) {
		forma.setDtoConsecutivo(forma.getListaHistoricos().get(forma.getPostConsecutivo()));
		return mapping.findForward("detConsecutivo"); // LLenar por JAVA SCRIPT ???????????????
	}

	
	
	
	
	

	/**
	 * CARGAR UN POPUP CON LOS HISTORICOS DE LOS CONSECUTIVOS DE TESORERIA
	 * @param centrosAtencionForm
	 * @param usuario
	 * @return
	 */
	private ActionForward cargarHistoricosConsecutivos(	CentrosAtencionForm forma, ActionMapping mapping) {
		
		forma.setDtoConsecutivo(new DtoConsecutivoCentroAtencion());
		forma.setDtoConsecutivo(forma.getListaConsecutivosCentroTesoreria().get(forma.getPostConsecutivo()).clone());

		if(forma.getConsecutivoModificado()==null || forma.getConsecutivoModificado().longValue()<=0)
		{
			forma.setConsecutivoModificado(forma.getDtoConsecutivo().getConsecutivo());
		}

		forma.setIdConsecutivoAnual(ConstantesBD.acronimoNo);
		forma.setIdConsecutivoAnual(forma.getDtoConsecutivo().getIdAnual()); 
		
		forma.setTipoConsecutivo(EmunTipoConsecutivo.Tesoreria.toString());
		forma.setTmpNombreBDConsecutivo(forma.getDtoConsecutivo().getNombreConsecutivo()); // NOMBRE TMP PARA GUARDAR EL CONSECUTIVO QUE SE ESTA ADMINISTRANDO
		this.filtra(forma);
		
		this.ordenarListaHistoricosConsecutivos(forma);
		forma.setEstado("cargarConsecutivo");
		
		
		return mapping.findForward("detConsecutivo");
	}
	
	
	
	
	/**
	 * FILTRA LA LISTA DE LOS HISTORICIOS DE LOS CONSECUTIVOS POR LE NOMBRE DEL CONSECUTIVO CARGADO
	 */
	public void filtra( CentrosAtencionForm forma)
	{
		/**
		 * LIMPIAR TODOS LOS DTO VISIBLES 
		 */
		for(DtoConsecutivoCentroAtencion listDto : forma.getListaHistoricos())
		{
			listDto.setEsVisible(ConstantesBD.acronimoNo);
		}
		
		/**
		 * COLOCA VISIBLE LOS CONSECUTIVOS
		 */
		for(DtoConsecutivoCentroAtencion listDto: forma.getListaHistoricos())
		{
			if( listDto.getNombreConsecutivo().equals(forma.getTmpNombreBDConsecutivo()) )
			{
				//listDto.setActivo(ConstantesBD.acronimoSi); //por mirar
				if(!listDto.getActivo().equals(ConstantesBD.acronimoNo)) //MOSTRAR SOLO LO ACTIVOS
				{
					listDto.setEsVisible(ConstantesBD.acronimoSi);
				}
			}
		}
	}
	

	/**
	 * Este método especifica las acciones a realizar en el estado empezar de CentrosAtencion
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{ 
			centrosAtencionForm.clean();
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));			
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));				
    		centrosAtencionForm.setEstado("empezar");	
    		UtilidadBD.cerrarConexion(con);
    	}
		
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
    		centrosAtencionForm.setEstado("empezar");
		}
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principal");
	}
	

	/**
	 * CARGAR CONSECUTIVOS CENTRO DE ATENCION
	 * @param codigoCentroAtencion
	 * @param forma
	 * @param codigoInstitucion 
	 */

	@SuppressWarnings("unchecked")
	private	void cargarConsecutivos(CentrosAtencionForm forma, int codigoInstitucion)
	{
		logger.info("\n\t\t\t CARGAR CONSECUTIVOS \n");
		DtoConsecutivoCentroAtencion dto = new DtoConsecutivoCentroAtencion();
		
		//TODO PROBLEMA CON EL CON CONSECUTIVO OJO
		if(forma.getConsecutivo()>0)
		{
			dto.getCentroAtencion().setCodigo(forma.getConsecutivo());	
		}
		
		dto.setCodigoInstitucion(codigoInstitucion);
		CentroAtencion mundoCentro = new CentroAtencion();
		mundoCentro.cargarConsecutivosCentroAtencion(dto);
		
		forma.setListaConsecutivosCentroFacturacion((ArrayList<DtoConsecutivoCentroAtencion>)mundoCentro.getListaConsecutivosCentroFacturacion().clone());
		forma.setListaConsecutivosCentroFacturasVarias((ArrayList<DtoConsecutivoCentroAtencion>)mundoCentro.getListaConsecutivosCentroFacturasVarias().clone());
		forma.setListaConsecutivosCentroTesoreria((ArrayList<DtoConsecutivoCentroAtencion>)mundoCentro.getListaConsecutivosCentroTesoria().clone());
		forma.setListaHistoricos((ArrayList<DtoConsecutivoCentroAtencion>)mundoCentro.getListaHistorialConsecutivos().clone());
	}
	
	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucionInt
	 */
	@SuppressWarnings("deprecation")
	private void postularPais(Connection con,
			CentrosAtencionForm forma, int codigoInstitucionInt) {
		
		
			ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
			mundoInstitucion.cargar(con, codigoInstitucionInt);
			if(UtilidadTexto.isEmpty(forma.getPais()))
				forma.setPais(mundoInstitucion.getPais().getCodigo());
			
			if(UtilidadTexto.isEmpty(forma.getCiudad().getCodigo()))
				forma.getCiudad().setCodigo(mundoInstitucion.getCiudad().getCodigo());

	}
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionConsultar(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			centrosAtencionForm.clean();
			centrosAtencionForm.setEsConsulta(Boolean.TRUE);
			
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));			
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
    		centrosAtencionForm.setEstado("consultar");		
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
    		centrosAtencionForm.setEstado("consultar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionIngresar(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			CentroAtencion centroAtencion = new CentroAtencion();
			centrosAtencionForm.clean();
			centrosAtencionForm.setActivo("true");
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
		//
			centrosAtencionForm.setCmbUpgd( centroAtencion.cargarVigiUnidadesPrimarias(con,usuario.getCodigoInstitucionInt(),0));
			centrosAtencionForm.setNumCmbUpgd(Integer.parseInt(centrosAtencionForm.getCmbUpgd("numRegistros").toString()));
		    
			DtoRegionesCobertura dtoRegiones = new DtoRegionesCobertura();
			dtoRegiones.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
			
			DtoCategoriaAtencion dtoCategorias = new DtoCategoriaAtencion();
			dtoCategorias.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
			
			centrosAtencionForm.setArrayCategorias(CategoriaAtencion.cargar(dtoCategorias));
			centrosAtencionForm.setArrayRegiones(RegionesCobertura.cargar(dtoRegiones));
			this.postularPais(con,centrosAtencionForm,usuario.getCodigoInstitucionInt());
			centrosAtencionForm.setEstado("ingresar");
    		
    		//CARGA LOS CONSECUTIVOS 
    		if(  (centrosAtencionForm.getManejaConsecutivoFactura().equals(ConstantesBD.acronimoSi)) || (centrosAtencionForm.getManejaConsecutivoTesoreria().equals(ConstantesBD.acronimoSi)) ||
    				centrosAtencionForm.getManejaConsecutivoNotasPacientesCentroAtencion().equals(ConstantesBD.acronimoSi))
    		{
    			cargarConsecutivos(centrosAtencionForm, usuario.getCodigoInstitucionInt());
    		}
    	}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
    		centrosAtencionForm.setEstado("empezar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCambiarOrden(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con) throws SQLException
	{
		logger.info("\n entre a ordenar");
		centrosAtencionForm.ordenarCentrosAtencion();
		centrosAtencionForm.setEstado(centrosAtencionForm.getEstadoAnterior());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	
	
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEditar(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			CentroAtencion centroAtencion = new CentroAtencion();
			
			centroAtencion.consultar(con, centrosAtencionForm.getConsecutivo());
		
			centrosAtencionForm.clean();
			centrosAtencionForm.setConsecutivo(centroAtencion.getConsecutivo());
			centrosAtencionForm.setCodigo(centroAtencion.getCodigo());
			
			// CARGAR LOS CONSECUTIVOS DEL CENTRO DE ATENCION
			if( (centrosAtencionForm.getManejaConsecutivoFactura().equals(ConstantesBD.acronimoSi)) || (centrosAtencionForm.getManejaConsecutivoTesoreria().equals(ConstantesBD.acronimoSi)) ||
					centrosAtencionForm.getManejaConsecutivoNotasPacientesCentroAtencion().equals(ConstantesBD.acronimoSi))
    		{
				cargarConsecutivos(centrosAtencionForm, usuario.getCodigoInstitucionInt());
    		}
			
			centrosAtencionForm.setDescripcion(centroAtencion.getDescripcion());
			centrosAtencionForm.setCodUpgd(centroAtencion.getCodUpgd());
			centrosAtencionForm.setActivo(centroAtencion.getActivo());
			logger.info("*************************  +"+ "Cat"+ centroAtencion.getCategoriaAtencion()+ "????"+ "region="+ centroAtencion.getRegionCobertura());
		    centrosAtencionForm.setArrayCategorias(CategoriaAtencion.cargar(new DtoCategoriaAtencion()));
			centrosAtencionForm.setArrayRegiones(RegionesCobertura.cargar(new DtoRegionesCobertura()));
			centrosAtencionForm.setCategoriaAtencion(centroAtencion.getCategoriaAtencion());
			centrosAtencionForm.setRegionCobertura(centroAtencion.getRegionCobertura());
			this.postularPais(con,centrosAtencionForm,usuario.getCodigoInstitucionInt());
			
			logger.info("****************** ciudad "+centroAtencion.getCiudad() + " desc:" + centroAtencion.getDescripcionCiudad());
			centrosAtencionForm.setCiudad(new InfoDatosStr(centroAtencion.getCiudad(),centroAtencion.getDescripcionCiudad()));
			centrosAtencionForm.setDepartamento(centroAtencion.getDepartamento());
			centrosAtencionForm.setTelefono(centroAtencion.getTelefono());
			
			
			//Anexo 959
			centrosAtencionForm.setCodigoInterfaz(centroAtencion.getCodigoInterfaz());
			centrosAtencionForm.setPiePaginaPresupuestoOdon(centroAtencion.getPiePaginaPresupuestoOdon());
			centrosAtencionForm.setResolucion(centroAtencion.getResolucion());
			centrosAtencionForm.setPrefFactura(centroAtencion.getPrefFactura());
			centrosAtencionForm.setRangoInicialFactura(centroAtencion.getRangoInicialFactura());
			centrosAtencionForm.setRangoFinalFactura(centroAtencion.getRangoFinalFactura());
			
			
			centrosAtencionForm.setResolucionFacturaVaria(centroAtencion.getResolucionFacturaVaria()) ;
			centrosAtencionForm.setPrefFacturaVaria(centroAtencion.getPrefFacturaVaria()) ;
			centrosAtencionForm.setRangoInicialFacturaVaria(centroAtencion.getRangoInicialFacturaVaria()) ;
			centrosAtencionForm.setRangoFinalFacturaVaria(centroAtencion.getRangoFinalFacturaVaria());
			
			//-------------------------------------------------------------------------------
			// modificado por la tarea 77564
			centrosAtencionForm.setDireccion(centroAtencion.getDireccion());
			//-------------------------------------------------------------------------------
			centrosAtencionForm.setCodigoInstSirc(centroAtencion.getCodInstSirc());
			centrosAtencionForm.setCodigoEntidadSubcontratada(centroAtencion.getCodigoEntidadSubcontratada());
			
			
			
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));			
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
						
			if(!centroAtencion.getCodInstSirc().equals(ConstantesBD.codigoNuncaValido+"") && !centroAtencion.getCodInstSirc().equals("0") && !centroAtencion.getCodInstSirc().equals(""))
			{
				int numRegistros = Integer.parseInt(centrosAtencionForm.getCmb_inst_sirc("numRegistros").toString());
				centrosAtencionForm.setCmb_inst_sirc("codigo_"+numRegistros,centrosAtencionForm.getCodigoInstSirc());
				centrosAtencionForm.setCmb_inst_sirc("descripcion_"+numRegistros,centroAtencion.getDescripcionInstSirc());
				centrosAtencionForm.setCmb_inst_sirc("numRegistros",numRegistros+1);
			}	
			
			centrosAtencionForm.setEmpresaInstitucion(centroAtencion.getEmpresaInstitucion());
			
			//
			centrosAtencionForm.setCmbUpgd( centroAtencion.cargarVigiUnidadesPrimarias(con,usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setNumCmbUpgd(Integer.parseInt(centrosAtencionForm.getCmbUpgd("numRegistros").toString()));
			//
			
			
			centroAtencionGlobal = new CentroAtencion();
			
	
			centroAtencionGlobal.setConsecutivo(centrosAtencionForm.getConsecutivo());
			centroAtencionGlobal.setCodigo(centrosAtencionForm.getCodigo());
			centroAtencionGlobal.setDescripcion(centrosAtencionForm.getDescripcion());
			centroAtencionGlobal.setActivo(UtilidadTexto.getBoolean(centrosAtencionForm.getActivo())?"Sí":"No");
			centroAtencionGlobal.setDireccion(centrosAtencionForm.getDireccion());
			centroAtencionGlobal.setCodInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
			
			centroAtencionGlobal.setCodUpgd(centrosAtencionForm.getCodUpgd());
			centroAtencionGlobal.setCodInstSirc((centrosAtencionForm.getCodigoInstSirc()));
			centroAtencionGlobal.setCodigoEntidadSubcontratada((centrosAtencionForm.getCodigoEntidadSubcontratada()));
			centroAtencionGlobal.setEmpresaInstitucion(centrosAtencionForm.getEmpresaInstitucion());
			centroAtencionGlobal.setDepartamento(centrosAtencionForm.getDepartamento());
            String[] vec = centrosAtencionForm.getCiudadFormato().split(ConstantesBD.separadorSplit) ;
			 
			if(vec.length>0)
				centroAtencionGlobal.setCiudad(vec[1]);
			
			centroAtencionGlobal.setCategoriaAtencion(centrosAtencionForm.getCategoriaAtencion());
			centroAtencionGlobal.setRegionCobertura(centrosAtencionForm.getRegionCobertura());
			
			centroAtencionGlobal.setCodigoInterfaz(centrosAtencionForm.getCodigoInterfaz());
			centroAtencionGlobal.setPiePaginaPresupuestoOdon(centrosAtencionForm.getPiePaginaPresupuestoOdon());
		
			centrosAtencionForm.setEstado("editar");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
    		centrosAtencionForm.setEstado("empezar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminar(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			CentroAtencion centroAtencion = new CentroAtencion();
			centroAtencion.consultar(con, centrosAtencionForm.getConsecutivo());
			centroAtencion.eliminar(con);
			
			centroAtencion.getRegionCobertura().setInstitucion(usuario.getCodigoInstitucionInt());
			centroAtencion.getCategoriaAtencion().setInstitucion(usuario.getCodigoInstitucionInt());
			UtilidadLog.generarLog(usuario, centroAtencion ,null, ConstantesBD.tipoRegistroLogEliminacion, ConstantesBD.logCentrosAtencionCodigo);
			
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setMensaje("¡Centro de Atención eliminado con éxito!");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
		}
		centrosAtencionForm.setEstado(centrosAtencionForm.getEstadoAnterior());
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarNuevo(
			CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			CentroAtencion centroAtencion = new CentroAtencion();
			centroAtencion.setCodigo(centrosAtencionForm.getCodigo());
			centroAtencion.setDescripcion(centrosAtencionForm.getDescripcion());
			centroAtencion.setActivo(centrosAtencionForm.getActivo());
			centroAtencion.setCodInstitucion(usuario.getCodigoInstitucionInt());
			centroAtencion.setCodUpgd(centrosAtencionForm.getCodUpgd());			
			centroAtencion.setCodInstSirc(centrosAtencionForm.getCodigoInstSirc());	
			centroAtencion.setCodigoEntidadSubcontratada(centrosAtencionForm.getCodigoEntidadSubcontratada());
			
			centroAtencion.setEmpresaInstitucion(centrosAtencionForm.getEmpresaInstitucion());
			//-------modificado 77564----------------------------------------------
			
			centroAtencion.setDireccion(centrosAtencionForm.getDireccion());
			centroAtencion.setTelefono(centrosAtencionForm.getTelefono());
			centroAtencion.setPais(centrosAtencionForm.getPais());
			centroAtencion.setDepartamento(centrosAtencionForm.getDepartamento());
			centroAtencion.setCiudad(centrosAtencionForm.getCiudad().getCodigo());
			centroAtencion.setCategoriaAtencion(centrosAtencionForm.getCategoriaAtencion());
			centroAtencion.setRegionCobertura(centrosAtencionForm.getRegionCobertura());
			
			
			//Agregados por Anexo 959
			centroAtencion.setCodigoInterfaz(centrosAtencionForm.getCodigoInterfaz());
			centroAtencion.setPiePaginaPresupuestoOdon(centrosAtencionForm.getPiePaginaPresupuestoOdon());
			centroAtencion.setResolucion(centrosAtencionForm.getResolucion());
			centroAtencion.setPrefFactura(centrosAtencionForm.getPrefFactura());
			centroAtencion.setRangoInicialFactura(centrosAtencionForm.getRangoInicialFactura());
			centroAtencion.setRangoFinalFactura(centrosAtencionForm.getRangoFinalFactura());
			
			
			centroAtencion.setResolucionFacturaVaria(centrosAtencionForm.getResolucionFacturaVaria());
			centroAtencion.setPrefFacturaVaria(centrosAtencionForm.getPrefFacturaVaria());
			centroAtencion.setRangoInicialFacturaVaria(centrosAtencionForm.getRangoInicialFacturaVaria());
			centroAtencion.setRangoFinalFacturaVaria(centrosAtencionForm.getRangoFinalFacturaVaria());
			
			
			// INSERTAR LA LISTA DE LOS CONSECUTIVOS DE CENTRO DE ATENCION POR FACTURACION Y POR TESORERIA
			centroAtencion.setListaConsecutivosCentroFacturacion(centrosAtencionForm.getListaConsecutivosCentroFacturacion());
			centroAtencion.setListaConsecutivosCentroTesoria(centrosAtencionForm.getListaConsecutivosCentroTesoreria());
			centroAtencion.setListaConsecutivosCentroFacturasVarias(centrosAtencionForm.getListaConsecutivosCentroFacturasVarias());
			centroAtencion.setListaHistorialConsecutivos(centrosAtencionForm.getListaHistoricos());
			
			
			//----------------------------------------------------------------------
			centroAtencion.insertar(con, usuario);
			centrosAtencionForm.clean();
			centrosAtencionForm.setMensaje("¡Centro de Atención guardado con éxito!");
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
			
			centrosAtencionForm.setEstado("empezar");
		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
			centrosAtencionForm.setEstado("ingresar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param centrosAtencionForm
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarCambios(CentrosAtencionForm centrosAtencionForm, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		try
		{
			
			CentroAtencion centroAtencion = new CentroAtencion();
			centroAtencion.setConsecutivo(centrosAtencionForm.getConsecutivo());
			centroAtencion.setCodigo(centrosAtencionForm.getCodigo());
			centroAtencion.setDescripcion(centrosAtencionForm.getDescripcion());
			centroAtencion.setActivo(centrosAtencionForm.getActivo());
			centroAtencion.setCodInstitucion(usuario.getCodigoInstitucionInt());
			centroAtencion.setCodUpgd(centrosAtencionForm.getCodUpgd());			
			centroAtencion.setDireccion(centrosAtencionForm.getDireccion());			
			centroAtencion.setCodInstSirc(centrosAtencionForm.getCodigoInstSirc());
			centroAtencion.setCodigoEntidadSubcontratada(centrosAtencionForm.getCodigoEntidadSubcontratada());
			centroAtencion.setEmpresaInstitucion(centrosAtencionForm.getEmpresaInstitucion());
			centroAtencion.setTelefono(centrosAtencionForm.getTelefono());
			centroAtencion.setPais(centrosAtencionForm.getPais());
			centroAtencion.setDepartamento(centrosAtencionForm.getDepartamento());
			centroAtencion.setCiudad(centrosAtencionForm.getCiudad().getCodigo());
			centroAtencion.setRegionCobertura(centrosAtencionForm.getRegionCobertura());
			centroAtencion.setCategoriaAtencion(centrosAtencionForm.getCategoriaAtencion());
			
			//Anexo 959
			centroAtencion.setCodigoInterfaz(centrosAtencionForm.getCodigoInterfaz());
			centroAtencion.setPiePaginaPresupuestoOdon(centrosAtencionForm.getPiePaginaPresupuestoOdon());
			
			
			centroAtencion.setResolucion(centrosAtencionForm.getResolucion()) ;
			centroAtencion.setPrefFactura(centrosAtencionForm.getPrefFactura()) ;
			centroAtencion.setRangoInicialFactura(centrosAtencionForm.getRangoInicialFactura()) ;
			centroAtencion.setRangoFinalFactura(centrosAtencionForm.getRangoFinalFactura());
						
			
			centroAtencion.setResolucionFacturaVaria(centrosAtencionForm.getResolucionFacturaVaria()) ;
			centroAtencion.setPrefFacturaVaria(centrosAtencionForm.getPrefFacturaVaria()) ;
			centroAtencion.setRangoInicialFacturaVaria(centrosAtencionForm.getRangoInicialFacturaVaria()) ;
			centroAtencion.setRangoFinalFacturaVaria(centrosAtencionForm.getRangoFinalFacturaVaria());
			
			
			/**
			 * LISTA PARA LOS CONSECUTIVOS
			 */
			centroAtencion.setListaConsecutivosCentroFacturacion(centrosAtencionForm.getListaConsecutivosCentroFacturacion());
			centroAtencion.setListaConsecutivosCentroTesoria(centrosAtencionForm.getListaConsecutivosCentroTesoreria());
			centroAtencion.setListaConsecutivosCentroFacturasVarias(centrosAtencionForm.getListaConsecutivosCentroFacturasVarias());
			centroAtencion.setListaHistorialConsecutivos(centrosAtencionForm.getListaHistoricos());


			centroAtencion.actualizar(centrosAtencionForm.getEmpresaInstitucion(),centrosAtencionForm.getConsecutivo(), usuario);
			

			String[] vec = centrosAtencionForm.getCiudadFormato().split(ConstantesBD.separadorSplit) ;
			 
			centroAtencion.setCiudad(vec[1]);
			centroAtencion.getRegionCobertura().setInstitucion(usuario.getCodigoInstitucionInt());
			centroAtencion.getCategoriaAtencion().setInstitucion(usuario.getCodigoInstitucionInt());

			
			UtilidadLog.generarLog(usuario, centroAtencionGlobal ,centroAtencion, ConstantesBD.tipoRegistroLogModificacion, ConstantesBD.logCentrosAtencionCodigo);
			centrosAtencionForm.clean();
			centrosAtencionForm.setMensaje("¡Centro de Atención modificado con éxito!");
			centrosAtencionForm.setCentrosAtencion(CentroAtencion.consultarCentrosAtencionInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setCmb_inst_sirc(CentroAtencion.cargarInstitucionesSIRC(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEntidadesSubcontratadas(CentroAtencion.cargarEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt(), centrosAtencionForm.getConsecutivo()));
			centrosAtencionForm.setEmpresasInstitucionMap(ParametrizacionInstitucion.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
			centrosAtencionForm.setEstado("empezar");

		}
		catch(Errores e)
		{
			ActionErrors errores = new ActionErrors();
			errores.add(e.getMessage(), e.getActionMessage());
			saveErrors(request, errores);
            logger.warn(e);
			centrosAtencionForm.setEstado("editar");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * Método que se encarga de determinar cual es el
	 * tipo de consecutivo que se va a ingresar.
	 * 
	 * @param forma
	 * @return
	 */
	private void definirTipoConsecutivo(CentrosAtencionForm forma){
		
		if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.Facturacion.toString()))
		{
			forma.setConsecutivoFactura(true);
			forma.setConsecutivoFacturaVaria(false);
			
		}else if(forma.getTipoConsecutivo().equals(EmunTipoConsecutivo.FacturasVarias.toString()))
		{
			forma.setConsecutivoFacturaVaria(true);
			forma.setConsecutivoFactura(false);
		}
	}
	
	
	/**
	 * ORDENAR LISTA DE HISTORICOS DE CONSECUTIVOS
	 * @param forma
	 */
	public void ordenarListaHistoricosConsecutivos(CentrosAtencionForm forma)
	{
		ConsecutivosCentroAtencion mundoConsecutivo = new ConsecutivosCentroAtencion();
		mundoConsecutivo.ordenarListaPorAnio(forma.getListaHistoricos());
	}
}
