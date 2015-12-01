/*
 * @(#)TerceroAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.cargos;

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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.cargos.TerceroForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConceptosRetencion;
import com.princetonsa.mundo.cargos.Tercero;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   terceros, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Junio 11, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class TerceroAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(TerceroAction.class);
		
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		
		if (response==null); //Para evitar que salga el warning
		if(form instanceof TerceroForm)
		{
				
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				TerceroForm terceroForm =(TerceroForm)form;
				Tercero mundo = new Tercero();
				
				String estado=terceroForm.getEstado(); 
				
				logger.info("-------------------------------------");
				logger.info("Valor del Estado  >> "+estado);
				logger.info("-------------------------------------");
				
				if(
						estado.equals("conceptosRetTercero") || 
						estado.equals("cambioTipoRetencion") || 
						estado.equals("modificarC") || 
						estado.equals("nuevo") || 
						estado.equals("modificarC")||
						estado.equals("guardarConceptoRet")
				)
				{
					logger.info("estado ant "+terceroForm.getEstadoAnt());
					terceroForm.setEstado(terceroForm.getEstadoAnt());
				}
				else
				{
					terceroForm.setEstadoAnt(terceroForm.getEstado());
				}
				
				if(estado == null)
				{
						terceroForm.reset();	
						logger.warn("Estado no valido dentro del flujo de registro tercero (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
					logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());	
					return this.accionEmpezar(terceroForm,mapping, con, usuario);
				}
				else if(estado.equals("salir"))
				{
					logger.info("**************** Valor Tipo Tercero --->"+terceroForm.getCodigoTipoTercero());	
					return this.accionSalir(terceroForm,mapping,request,usuario,con);
				}
				else if(estado.equals("resumen"))
				{
						return this.accionResumen(terceroForm,mapping,request, con);
				}
				else if(estado.equals("listar"))
				{
					terceroForm.setIndiceTercero("");
					terceroForm.setAccion("consulta");					
					return this.accionListarTerceros(terceroForm,mapping,con,estado, usuario);
				}
				else if (estado.equals("listarModificar"))
				{
					return this.accionListarTerceros(terceroForm,mapping,con,estado, usuario);
				}
				else if(estado.equals("ordenar"))
				{
					   return this.accionOrdenar(terceroForm,mapping,request,con);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
					logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());	
					return this.accionBusquedaAvanzada(terceroForm,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
						return this.accionResultadoBusquedaAvanzada(terceroForm,mapping,con, usuario, false);
				}
				else if(estado.equals("resultadoBusquedaAvanzadaModificar"))
				{
						return this.accionResultadoBusquedaAvanzada(terceroForm,mapping,con, usuario, true);
				}
				else if(estado.equals("imprimir"))
				{
						this.cerrarConexion(con);
						return mapping.findForward("imprimir");	
				}				
				else if(estado.equals("resumenModificar"))
				{
						return this.accionResumenModificar(terceroForm,mapping,request, con);
				}
				else if(estado.equals("modificar"))
				{
						terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
						logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());	
						return this.accionModificar(terceroForm,request,mapping,con,usuario);
				}
				else if(estado.equals("guardarModificacion"))
				{
						terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
						logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());
						return this.accionGuardarModificacion(terceroForm,mundo,mapping,request,usuario,con);
				}
				else if(estado.equals("conceptosRetTercero"))
				{
					terceroForm.setMensaje(new ResultadoBoolean(false));
					return this.accionConceptosRetTercero(terceroForm,mundo,mapping,request,usuario,con);					
				}					
				else if(estado.equals("nuevoConcepto"))
				{
					terceroForm.resetConceptos();
					return mapping.findForward("conceptosRetTercero");	
				}
				else if(estado.equals("nuevo") || estado.equals("modificarC"))
				{
					terceroForm.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
					return mapping.findForward("conceptosRetTercero");	
				}
				else if(estado.equals("guardarConceptoRet"))
				{					
					return this.accionGuardarConceptoRet(terceroForm,mundo,mapping,request,usuario,con);
				}
				else if(estado.equals("modificarConcepto"))
				{
					return this.accionModificarConcepto(terceroForm,mundo,mapping,request,usuario,con);
				}
				else if(estado.equals("guardarModConcepto") || estado.equals("eliminarConcepto"))
				{
					return this.accionGuardarModConcepto(terceroForm,mundo,mapping,request,usuario,con);
				}
				else if(estado.equals("cambioTipoRetencion"))
				{
					return accionCambioTipoRetencion(terceroForm, mundo, mapping, request, usuario, con);
				}
				else
				{
					terceroForm.reset();
					logger.warn("Estado no valido dentro del flujo de registro tercero (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
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
	 * este metodo guarda todos los registros de conceptos de retencion de terceros
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionConceptosRetTercero(TerceroForm terceroForm,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con) 
	{
		if(terceroForm.getEstadoAnt().equals("listar") || terceroForm.getEstadoAnt().equals("resultadoBusquedaAvanzada"))
			terceroForm.setListaConceptosRetencionTercero(mundo.consultarConceptosRetTercero(Utilidades.convertirAEntero(terceroForm.getIndiceTercero())));
		if(terceroForm.getDescripcionTipoRet() == null)
			terceroForm.setDescripcionTipoRet("");
		if(terceroForm.getConsecutivoTipoRet() == null)
			terceroForm.setConsecutivoTipoRet("-1");
		if(terceroForm.getAgenteRet() == null)
			terceroForm.setAgenteRet("N");
		if(terceroForm.getAccion() == null)
			terceroForm.setAccion("");
			
		return mapping.findForward("conceptosRetTercero");
	}
	
	/**
	 * este metodo guarda todos los registros de conceptos de retencion de terceros
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward guardarConceptosRetTercero(TerceroForm forma,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con) 
	{
		ActionErrors errores = new ActionErrors();
		logger.info("\n\ntercero:: "+forma.getCodigo());
		for(int k=0;k<forma.getListaConceptosRetencionTercero().size();k++)
		{
			DtoConceptosRetencionTercero dto=forma.getListaConceptosRetencionTercero().get(k);
			logger.info("dto.isEnBD() "+dto.isEnBD());
			logger.info("dto.isModificado() "+dto.isModificado());
			if(!dto.isEnBD())
			{
				logger.info("\n\n..::::::::.. ");
				logger.info("\nregistro:::.. "+k);
				logger.info("\nconcepto_retencion:::.. "+dto.getConceptosRet().getConsecutivo());
				logger.info("\ntipo_aplicacion:::.. "+dto.getTipoAplicacion());
				logger.info("\nind_agente_retenedor:::.. "+dto.getIndAgenteRetenedor());
				logger.info("\nfecha_modifica:::.. "+dto.getFechaModificacion());
				logger.info("\nhora_modifica:::.. "+dto.getHoraModificacion());
				logger.info("\nusuario_modifica:::.. "+dto.getUsuarioModificacion());
				logger.info("\nactivo:::.. "+dto.getActivo());
				logger.info("\nfecha_inac:::.. "+dto.getFechaInactivacion());
				logger.info("\nhora_inac:::.. "+dto.getHoraInactivacion());
				logger.info("\nusuario_inac:::.. "+dto.getUsuarioInactivacion());
				logger.info("\n..::::::::.. ");
				
				if(!mundo.insertarConceptoRetencion(dto, forma.getCodigo()))
					errores.add("descripcion",new ActionMessage("prompt.generico","El Concepto de Retencion numero "+k+" no se inserto correctamente"));
				else
					logger.info("\n\ninserto ok!!!");
				
			}
			else if(dto.isModificado())
			{
				if(!mundo.modificarConceptoRetencion(dto));
					errores.add("descripcion",new ActionMessage("prompt.generico","El Concepto de Retencion numero "+k+" no se modificó correctamente"));
			}
		}
		
		if(!errores.isEmpty())		
		{
			forma.setEstado("nuevoRegistro");
			saveErrors(request,errores);
		}
		else
		{	
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		
		return mapping.findForward("funcionalidadResumenTercero");
	}
	
	/**
	 * este metodo guarda la modificacion de concepto de retencion
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionGuardarModConcepto(TerceroForm forma,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con) 
	{
		logger.info("indice detalle "+forma.getIndiceDetalle());
		int i= Utilidades.convertirAEntero(forma.getIndiceDetalle());
		
		DtoConceptosRetencionTercero dto= new DtoConceptosRetencionTercero();

		dto=forma.getListaConceptosRetencionTercero().get(i);
		
		forma.setModifico(true);
		dto.setModificado(true);

		if(forma.getAccion().equals("eliminar"))
		{
			dto.setActivo(ConstantesBD.acronimoNo);
			dto.setFechaInactivacion(UtilidadFecha.getFechaActual());
			dto.setHoraInactivacion(UtilidadFecha.getHoraActual());
			dto.setUsuarioInactivacion(usuario.getLoginUsuario());
		}
		else if(forma.getAccion().equals("modificar"))
		{
			dto.setTipoAplicacion(forma.getTipoAplicacion());
			dto.setIndAgenteRetenedor(forma.getAgenteRet());
			for(int j=0; j<forma.getListaConceptosRetencion().size(); j++)
			{
				DtoConceptosRetencion conceptosRetencion=forma.getListaConceptosRetencion().get(j);
				logger.info(
						"cons "+Utilidades.convertirAEntero(conceptosRetencion.getConsecutivo()+"")+ 
						"consConRet "+Utilidades.convertirAEntero(forma.getConsecutivoConRet()));
				if(Utilidades.convertirAEntero(conceptosRetencion.getConsecutivo()+"") == Utilidades.convertirAEntero(forma.getConsecutivoConRet()))
				{
					dto.setConceptosRet(conceptosRetencion);
				}
			}
		}
				
		return mapping.findForward("conceptosRetTercero");		
	}
	
	/**
	 * este metodo guarda la modificacion de concepto de retencion
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionModificarConcepto(TerceroForm forma,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con) 
	{
		int i= Utilidades.convertirAEntero(forma.getIndiceDetalle());
		
		DtoConceptosRetencionTercero dto= new DtoConceptosRetencionTercero();
		
		dto=forma.getListaConceptosRetencionTercero().get(i);
		
		forma.setConsecutivoConRet(dto.getConceptosRet().getConsecutivo()+"");
		forma.setTipoAplicacion(dto.getTipoAplicacion());
		forma.setAgenteRet(dto.getIndAgenteRetenedor());
		logger.info(dto.getConceptosRet().getTipoRet().getCodigo());
		forma.setDescripcionTipoRet(dto.getConceptosRet().getTipoRet().getCodigo()+""+dto.getConceptosRet().getTipoRetencionDesc()+""+dto.getConceptosRet().getTipoRetencionSigla());
		forma.setConsecutivoTipoRet(dto.getConceptosRet().getTipoRet().getConsecutivo()+"");
		
		return mapping.findForward("conceptosRetTercero");	
	}
	
	private ActionForward accionCambioTipoRetencion(TerceroForm forma,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con)
	{
		return mapping.findForward("conceptosRetTercero");
	}
	
	/**
	 * este metodo guarda un nuevo registro de concepto de retencion
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionGuardarConceptoRet(TerceroForm forma,Tercero mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario, Connection con) 
	{			
		ActionErrors errores = new ActionErrors();
				
		if(forma.getConsecutivoTipoRet().equals("-1"))
			errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Retención"));
		if(forma.getConsecutivoConRet().equals("-1"))
			errores.add("descripcion",new ActionMessage("errors.required","El Concepto de Retención"));
		if(forma.getTipoAplicacion().equals("-1"))
			errores.add("descripcion",new ActionMessage("errors.required","El Tipo Aplicación"));
		if(forma.getAgenteRet().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Indicativo de Agente Retenedor"));
		
		if(errores.isEmpty())
		{
			for(int i=0; i<forma.getListaConceptosRetencionTercero().size();i++)
			{
				if(forma.getListaConceptosRetencionTercero().get(i).getActivo().equals(ConstantesBD.acronimoSi))
				{
					if(Utilidades.convertirAEntero(
							forma.getListaConceptosRetencionTercero().get(i).getConceptosRet().getTipoRetencionConsecutivo()+"") 
							== Utilidades.convertirAEntero(forma.getConsecutivoTipoRet()) 
							&& Utilidades.convertirAEntero(forma.getListaConceptosRetencionTercero().get(i).getConceptosRet().getConsecutivo()+"") 
							== Utilidades.convertirAEntero(forma.getConsecutivoConRet()))
						errores.add("descripcion",new ActionMessage("prompt.generico","El Concepto de Retención del Tercero ya fue insertado."));
				}
			}
		}
		
		if(!errores.isEmpty())		
		{
			logger.info("guardando forma.getEstadoAnt() "+forma.getEstadoAnt());
			forma.setEstado(forma.getEstadoAnt());
			saveErrors(request,errores);
		}
		else
		{						
			DtoConceptosRetencionTercero dto= new DtoConceptosRetencionTercero();
			
			dto.setActivo(ConstantesBD.acronimoSi);
			dto.setIndAgenteRetenedor(forma.getAgenteRet());
			dto.setTipoAplicacion(forma.getTipoAplicacion());
			logger.info("\n\naccion:::::: "+forma.getAccion());
			if(!forma.getAccion().equals("eliminar"))
			{
				dto.setFechaModificacion(UtilidadFecha.getFechaActual());
				dto.setHoraModificacion(UtilidadFecha.getHoraActual());
				dto.setUsuarioModificacion(usuario.getLoginUsuario());
			}
			
			DtoConceptosRetencion dtoConceptos= new DtoConceptosRetencion();
			
			for(int j=0; j<forma.getListaConceptosRetencion().size(); j++)
			{
				logger.info(
						"cons "+Utilidades.convertirAEntero(forma.getListaConceptosRetencion().get(j).getConsecutivo()+"")+ 
						"consConRet "+Utilidades.convertirAEntero(forma.getConsecutivoConRet()));
				if(Utilidades.convertirAEntero(forma.getListaConceptosRetencion().get(j).getConsecutivo()+"") == Utilidades.convertirAEntero(forma.getConsecutivoConRet()))
				{
					dtoConceptos.setDescripcion(forma.getListaConceptosRetencion().get(j).getDescripcion());
					dtoConceptos.setConsecutivo(forma.getConsecutivoConRet());
					dtoConceptos.setTipoRetencionDesc(forma.getListaConceptosRetencion().get(j).getTipoRetencionDesc());
					dtoConceptos.setTipoRetencionConsecutivo(Utilidades.convertirAEntero(forma.getConsecutivoTipoRet()));
					dtoConceptos.setTipoRetencion(forma.getListaConceptosRetencion().get(j).getTipoRet().getCodigo());
					dtoConceptos.setTipoRetencionSigla(forma.getListaConceptosRetencion().get(j).getTipoRetencionSigla());
				}
			}
						
			
			dto.setConceptosRet(dtoConceptos);
			if(forma.getEstadoAnt().equals("nuevoConcepto"))
			{
				dto.setEnBD(false);
			}
			
			if(forma.getAccion().equals(""))
			{
				forma.getListaConceptosRetencionTercero().add(dto);
			}
			forma.setModifico(true);

		}				
		return mapping.findForward("conceptosRetTercero");	
	}
	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param terceroForm TerceroForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param usuario
	 * @return ActionForward a la página principal "tercero.jsp"
	 * 
	 */
	private ActionForward accionEmpezar(TerceroForm terceroForm, 
																	 ActionMapping mapping, 
																	 Connection con, UsuarioBasico usuario) throws SQLException 
	{
		//Limpiamos lo que venga del form
		terceroForm.reset();
		terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
		logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());
		
		terceroForm.setEstado("empezar");
		//se asigna institucion
		terceroForm.setInstitucion(usuario.getCodigoInstitucion());
		terceroForm.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto tercero
	 * en el objeto mundo
	 * 
	 * @param terceroForm TerceroForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "tercero.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionSalir(TerceroForm terceroForm,
															 ActionMapping mapping,
															 HttpServletRequest request, 
															 UsuarioBasico usuario,
															 Connection con) throws SQLException
	{
		Tercero mundoTercero=new Tercero();  
		llenarMundo(terceroForm, mundoTercero,usuario);
		int insertoBien=mundoTercero.insertarTercero(con);
		logger.info("===>Código Tercero Insertado: "+insertoBien);
		if(insertoBien<=0)
		{    
		    ActionErrors errores = new ActionErrors();
			errores.add("error tipo y numero id repetidos", new ActionMessage("errors.yaExiste", "El Número de Identificación "));
			logger.warn("El tipo y numero Id - son unique (TerceroAction) "+terceroForm.getCodigo());
			saveErrors(request, errores);	
			terceroForm.setEstado("empezar");
			this.cerrarConexion(con);									
			return mapping.findForward("principal");
		}	
		terceroForm.setCodigo(insertoBien);
		this.guardarConceptosRetTercero(terceroForm, mundoTercero, mapping, request, usuario, con);
		this.cerrarConexion(con);									
		return mapping.findForward("funcionalidadResumenTercero");
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param terceroForm TerceroForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	TerceroForm terceroForm, 
																	 	ActionMapping mapping, 
																		HttpServletRequest request, 
																	 	Connection con) throws SQLException
	{
		Tercero mundoTercero=new Tercero();  	
		boolean validarCargarUltimoCodigo=mundoTercero.cargarUltimoCodigo(con);
		if(validarCargarUltimoCodigo)
		{ 
			boolean validarCargar=mundoTercero.cargarResumen(con,mundoTercero.getCodigo());
			if(validarCargar)
			{
				llenarForm(terceroForm,mundoTercero);
				this.cerrarConexion(con);		
				return mapping.findForward("paginaResumenTercero");
			}
			else
			{
				logger.warn("Código tercero inválido "+terceroForm.getCodigo());
				this.cerrarConexion(con);
				terceroForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código tercero ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
			}
		}
		else
		{
				logger.warn("(Cargar última inserción)Código tercero inválido "+terceroForm.getCodigo());
				this.cerrarConexion(con);
				terceroForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código tercero");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
		}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar terceros
	 * @param terceroForm TerceroForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarTerceros	(	TerceroForm terceroForm, ActionMapping mapping,Connection con,String estado, UsuarioBasico usuario) throws SQLException 
	{
		Tercero mundoTercero = new Tercero();
		terceroForm.setEstado(estado);
		terceroForm.setInstitucion(usuario.getCodigoInstitucion());
		terceroForm.setCol(mundoTercero.listadoTercero(con, usuario.getCodigoInstitucionInt()));
		terceroForm.setListaConceptosRetencion(ConceptosRetencion.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaListar")	;	
	}	

	private ActionForward accionOrdenar(	TerceroForm terceroForm,
																		ActionMapping mapping,
																		HttpServletRequest request, 
																		Connection con) throws SQLException
	{		
		try
		{
			
			terceroForm.setCol(Listado.ordenarColumna(new ArrayList(terceroForm.getCol()),terceroForm.getUltimaPropiedad(),terceroForm.getColumna()));
			terceroForm.setUltimaPropiedad(terceroForm.getColumna());
			this.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de terceros ");
			this.cerrarConexion(con);
			terceroForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado terceros");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
			return mapping.findForward("paginaListar")	;
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param terceroForm TerceroForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarTercero "busquedaTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	TerceroForm terceroForm, 
																						ActionMapping mapping, 
																						Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		terceroForm.reset();
		terceroForm.setMapaTipoTercero(UtilidadesFacturacion.tipoTercero(con));
		logger.info("MAPA TIPOS TERCEROS ===>"+terceroForm.getMapaTipoTercero());
		terceroForm.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param terceroForm TerceroForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarTercero "busquedaTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(TerceroForm terceroForm, 
																										ActionMapping mapping, 
																										Connection con,
																										UsuarioBasico usuario,
																										boolean accionModificar) throws SQLException
	{
		Tercero mundoTercero= new Tercero();
		mundoTercero.reset();
		terceroForm.setAccion("consulta");
		enviarItemsSeleccionadosBusqueda(terceroForm, mundoTercero);
		terceroForm.resetCriteriosBusqueda();
		terceroForm.setCol(mundoTercero.resultadoBusquedaAvanzada(con, Integer.parseInt(usuario.getCodigoInstitucion())));
		if(accionModificar)
		    terceroForm.setEstado("listarModificar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");
	}
	
	private void enviarItemsSeleccionadosBusqueda(TerceroForm terceroForm, Tercero mundoTercero)
	{
		String bb[]= terceroForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			try
			{
				if(bb[i].equals("numeroIdentificacion"))
					mundoTercero.setNumeroIdentificacion(terceroForm.getNumeroIdentificacion());
				if(bb[i].equals("descripcion"))
					mundoTercero.setDescripcion(terceroForm.getDescripcion());
				if(bb[i].equals("activaAux"))
					mundoTercero.setActivaAux(terceroForm.getActivaAux());
				if(bb[i].equals("codigoTipoTercero"))
					mundoTercero.setCodigoTipoTercero(terceroForm.getCodigoTipoTercero());
				if(bb[i].equals("digitoVerificacion"))
					mundoTercero.setDigitoVerificacion(terceroForm.getDigitoVerificacion());
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar tercero
	 * @param terceroForm TerceroForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param usuario
	 * @return ActionForward  a la página "modificarTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(TerceroForm terceroForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con, UsuarioBasico usuario) throws SQLException
	{
		//se asigna la institucion por si se hace un reset de la busqueda avanzada
		terceroForm.setInstitucion(usuario.getCodigoInstitucion());
		
		Tercero mundoTercero=new Tercero();
		mundoTercero.reset();
	
		mundoTercero.cargarResumen(con,terceroForm.getCodigo());
		
		llenarForm(terceroForm,mundoTercero);
		terceroForm.setEstado("modificar");
		terceroForm.setAccion("modificar");
		
		if(terceroForm.getAccion().equals("consulta"))
		{
			terceroForm.setCodigo(Utilidades.convertirAEntero(terceroForm.getIndiceTercero()));						
		}
		if(terceroForm.getCodigo() > 0)
		{
			terceroForm.setListaConceptosRetencionTercero(mundoTercero.consultarConceptosRetTercero(terceroForm.getCodigo()));
		}
		else
		{
			if(terceroForm.getListaConceptosRetencionTercero() == null)
			{
				ArrayList<DtoConceptosRetencionTercero> lista =new ArrayList<DtoConceptosRetencionTercero>();
				terceroForm.setListaConceptosRetencionTercero(lista);
			}
		}

		terceroForm.setHanUtilizadoTerceros(UtilidadValidacion.hanUtilizadoTercero(con,terceroForm.getCodigo()));
		
		if(terceroForm.getCodigo()<0)
		{
			logger.warn("No se pudo cargar el Tercero: "+terceroForm.getCodigo());
			this.cerrarConexion(con);
			terceroForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El código del tercero ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
				String log="\n            ====INFORMACION ORIGINAL===== " +
				"\n*  Código Tercero [" +mundoTercero.getCodigo() +"] "+
				"\n*  Número de Identificación ["+mundoTercero.getNumeroIdentificacion()+"] " +
				"\n*  Descripción ["+mundoTercero.getDescripcion()+"] " +
				"\n*  Tipo Tercero ["+mundoTercero.getCodigoTipoTercero()+"] " +
				"\n*  Dígito Verificación ["+mundoTercero.getDigitoVerificacion()+"] ";
				
				if(mundoTercero.getActivo())
					log += "\n*  Activa [ SI ]";
				else
					log += "\n*  Activa [ NO ]";
				
			terceroForm.setLogInfoOriginalTercero(log);
				
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param terceroForm TerceroForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "tercero.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(TerceroForm terceroForm, Tercero mundo,
																						ActionMapping mapping, 
																						HttpServletRequest request, 
																						UsuarioBasico usuario,
																						Connection con)	
																						throws SQLException
	{
	    boolean existioModificacion= existeModificacion(terceroForm, con);
		logger.info("existioModificacion datos "+existioModificacion);
		logger.info("existioModificacion terceros "+terceroForm.isModifico());
	    if(existioModificacion || terceroForm.isModifico())
		{
	    	if(existioModificacion)
	    	{
				Tercero mundoTercero=new Tercero ();
				llenarMundo(terceroForm, mundoTercero, usuario);
				int a= mundoTercero.modificarTercero(con);
				if(a>0)
				{
					String log=terceroForm.getLogInfoOriginalTercero()+
							"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
							"\n*  Código Tercero [" +mundoTercero.getCodigo() +"] "+
							"\n*  Número de Identificación ["+mundoTercero.getNumeroIdentificacion()+"] " +
							"\n*  Descripción ["+mundoTercero.getDescripcion()+"] "+
							"\n*  Tipo Tercero ["+mundoTercero.getCodigoTipoTercero()+"] " +
							"\n*  Digito Verificacion ["+mundoTercero.getDigitoVerificacion()+"] ";
							
					if(mundoTercero.getActivo())
						log += "\n*  Activa [ SI ]";
					else
						log += "\n*  Activa [ NO ]";
						
						log+="\n*  Código de la Institución ["+mundoTercero.getInstitucion()+"] ";
					
					log+="\n========================================================\n\n\n " ;		
					
					LogsAxioma.enviarLog(ConstantesBD.logTerceroCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
				}
				else
				{   
				    ActionErrors errores = new ActionErrors();
					errores.add("error tipo y numero id repetidos", new ActionMessage("errors.yaExiste", "El número de identificacion "));
					logger.warn("El tipo y numero Id - son unique (TerceroAction) "+terceroForm.getCodigo());
					saveErrors(request, errores);	
					terceroForm.setEstado("modificar");
					this.cerrarConexion(con);									
					return mapping.findForward("principal");	
		
				}			
	    	}
			if(terceroForm.isModifico())
			{
				this.guardarConceptosRetTercero(terceroForm, mundo, mapping, request, usuario, con);
			}
			this.cerrarConexion(con);									
			return mapping.findForward("funcionalidadResumenModificarTercero");
		}    
	    this.cerrarConexion(con);
	    terceroForm.setEstado("modificar");
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que evalua si existio o no modificacion en el convenio
	 * @param terceroForm
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private boolean existeModificacion (TerceroForm terceroForm, Connection con)throws SQLException
	{
	    Tercero mundoTercero=new Tercero();
		mundoTercero.reset();
		mundoTercero.cargarResumen(con,terceroForm.getCodigo());
		
	    if(!mundoTercero.getNumeroIdentificacion().equals(terceroForm.getNumeroIdentificacion()))
			return true;
	    if((mundoTercero.getCodigoTipoTercero())!=(terceroForm.getCodigoTipoTercero()))
    		return true;
    	if(!(mundoTercero.getDigitoVerificacion()).equals(terceroForm.getDigitoVerificacion()))
    		return true;
    	if(!(mundoTercero.getDescripcion()).equals(terceroForm.getDescripcion()))
    		return true;
        if(mundoTercero.getActivo()!=terceroForm.getActivo())
        	return true;
        if(!mundoTercero.getDireccion().equals(terceroForm.getDireccion()))
        	return true;
        if(!mundoTercero.getTelefono().equals(terceroForm.getTelefono()))
        	return true;
        
    	return false;
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenModificar
	 * @param terceroForm TerceroForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumenModificar(		TerceroForm terceroForm, 
																							ActionMapping mapping, 
																							HttpServletRequest request, 
																							Connection con) throws SQLException
	{
		Tercero mundoTercero=new Tercero();  	
		boolean validarCargar=mundoTercero.cargarResumen(con,terceroForm.getCodigo());
		if(validarCargar)
		{
			llenarForm(terceroForm,mundoTercero);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenTercero");
		}
		else
		{
			logger.warn("Código tercero inválido "+terceroForm.getCodigo());
			this.cerrarConexion(con);
			terceroForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Código tercero");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param terceroForm (form)
	 * @param mundoTercero (mundo)
	 */
	protected void llenarForm(TerceroForm terceroForm, Tercero mundoTercero)
	{		
		terceroForm.setCodigo(mundoTercero.getCodigo());
		terceroForm.setNumeroIdentificacion(mundoTercero.getNumeroIdentificacion());
		terceroForm.setNumeroIdentificacionNuevo(mundoTercero.getNumeroIdentificacion());
		terceroForm.setDescripcion(mundoTercero.getDescripcion());
		terceroForm.setCodigoTipoTercero(mundoTercero.getCodigoTipoTercero());
		terceroForm.setDigitoVerificacion(mundoTercero.getDigitoVerificacion());
		terceroForm.setDireccion(mundoTercero.getDireccion());
		terceroForm.setTelefono(mundoTercero.getTelefono());
		
		terceroForm.setActivo(mundoTercero.getActivo());
	}
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form TerceroForm para el mundo de Tercero
	 * @param terceroForm TerceroForm (forma)
	 * @param mundoTercero Tercero (mundo)
	 */
	protected void llenarMundo(TerceroForm terceroForm, Tercero mundoTercero, UsuarioBasico usuario)
	{
		mundoTercero.setCodigo(terceroForm.getCodigo());
		mundoTercero.setNumeroIdentificacion(terceroForm.getNumeroIdentificacion());
		mundoTercero.setDescripcion(terceroForm.getDescripcion());
		mundoTercero.setActivo(terceroForm.getActivo());
		mundoTercero.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		mundoTercero.setCodigoTipoTercero(terceroForm.getCodigoTipoTercero());
		mundoTercero.setDigitoVerificacion(terceroForm.getDigitoVerificacion());
		mundoTercero.setDireccion(terceroForm.getDireccion());
		mundoTercero.setTelefono(terceroForm.getTelefono());
	}	
	
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
	}
	
	
}
