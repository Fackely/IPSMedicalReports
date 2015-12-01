package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.DescuentoOdontologicoForm;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.DescuentoOdontologico;
import com.princetonsa.mundo.odontologia.DetalleDescuentoOdontologico;
import com.princetonsa.mundo.odontologia.HistoricoDescuentoOdontologico;
import com.princetonsa.mundo.odontologia.HistoricoDetalleDescuentoOdontologico;
import com.princetonsa.mundo.odontologia.TiposUsuario;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * 
 * @author axioma
 *
 */
public class DescuentoOdontologicoAction extends Action{
	
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(DescuentoOdontologicoAction.class);
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof DescuentoOdontologicoForm) 
		{
			DescuentoOdontologicoForm forma = (DescuentoOdontologicoForm) form;
			logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxestado-->" + forma.getEstado());
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			/**
			 * 
			 *  estado empezar
			 */
			if (forma.getEstado().equals("empezar")) 
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ingresar")) 
			{
				return accionIngresar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ingresar_nuevo")) 
			{
				return accionIngresarNuevo(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ingresar_nuevo_detalle")) 
			{
				return accionIngresarNuevoDetalle(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("guardar"))
				
			{
				return accionGuardar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("guardar_detalle")) 
			{
				return accionGuardarDetalle(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("modificar")) 
			{
				return accionModificar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("modificar_detalle")) 
			{
				return accionModificarDetalle(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("mostrarErroresGuardar")) 
			{
				 if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
				   {
					return mapping.findForward("paginaPrincipal");
				   }else
				   {
					   return mapping.findForward("paginaAtencion");
				   }
			}
			else if (forma.getEstado().equals("mostrarErroresGuardarModificar") || forma.getEstado().equals("mostrarErroresIngresar")) 
			{
			  if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
			   {
				  return mapping.findForward("paginaPrincipal");
			   }else
			   {
				  return mapping.findForward("paginaAtencion");
			   }
			}
			
			else if (forma.getEstado().equals("mostrarErroresEliminar") ) 
			{
			  
				  return mapping.findForward("paginaPrincipal");
			  
			}
			else if (forma.getEstado().equals("mostrarErroresGuardarModificarDetalle")) 
			{
				
				return mapping.findForward("paginaDetalle");
			}
			else if (forma.getEstado().equals("mostrarErroresGuardarDetalle")) 
			{
				return accionIngresarNuevoDetalle(mapping,
						 forma,  usuario);
			}		
			
			else if (forma.getEstado().equals("guardarModificar")) 
			{
				return accionguardarModificar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("guardarModificarDetalle")) 
			{
				return accionguardarModificarDetalle(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("eliminar")) 
			{
				return accionEliminar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("eliminar_detalle")) 
			{
				return accionEliminarDetalle(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("ordenar")) 
			{
				return accionOrdenar(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("ordenar_detalle")) 
			{
				return accionOrdenarDetalle(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ordenar_detalle_historico")) 
			{
				return accionOrdenarDetalleHistorico(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ordenar_historico")) 
			{
				return accionOrdenarHistorico(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("empezarDetalle")) 
			{
				return accionEmpezarDetalle(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("historico")) 
			{
				return accionHistorico(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("empezarDetalleHistorico")) 
			{
				return accionHistoricoDetalle(mapping, forma, usuario);
			}
			
			else if (forma.getEstado().equals("resumen")) 
			{
				if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
				   {
					  return mapping.findForward("paginaPrincipal");
				   }else
				   {
					  return mapping.findForward("paginaAtencion");
				   }
			}
			
			else if (forma.getEstado().equals("resumenDetalle")) 
			{
				return mapping.findForward("paginaDetalle");
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresar(	ActionMapping mapping,
											DescuentoOdontologicoForm forma, 
											UsuarioBasico usuario) 
	{
		Connection con = UtilidadBD.abrirConexion();
		forma.getDtoDescuentoOdontologico().setCentroAtencion(new InfoDatosInt(forma.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo() ,Utilidades.obtenerNombreCentroAtencion(con, forma.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo())));
		
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		{	
			DtoDescuentosOdontologicos dtoCargar = new DtoDescuentosOdontologicos();
			logger.info(forma.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo());
			dtoCargar.setCentroAtencion(forma.getDtoDescuentoOdontologico().getCentroAtencion());
			
			forma.setListaDescuentos(DescuentoOdontologico.cargar(dtoCargar));
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_descuentos_odon");
			forma.getDtoDescuentoOdontologico().setConsecutivo(secuencia);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
		}
		else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
		{
			
			DtoDescuentoOdontologicoAtencion dtoCargar = new DtoDescuentoOdontologicoAtencion();
			logger.info(forma.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo());
			forma.getDtoDescuentoOdontologicoAtencion().setCentroAtencion(forma.getDtoDescuentoOdontologico().getCentroAtencion());
			dtoCargar.setCentroAtencion(forma.getDtoDescuentoOdontologico().getCentroAtencion());			
			forma.setListaDescuentosAtencion(DescuentoOdontologico.cargarAtencion(dtoCargar));
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_descuentos_odon_aten");
			forma.getDtoDescuentoOdontologicoAtencion().setConsecutivo(secuencia);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaAtencion");
			
		}
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarNuevo(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
	   {	
			forma.getDtoDescuentoOdontologico().setFechaInicioVigencia("");
			forma.getDtoDescuentoOdontologico().setFechaFinVigencia("");
			return mapping.findForward("paginaPrincipal");
	   }
		else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
		{
		
			forma.getDtoDescuentoOdontologicoAtencion().reset2();
			DtoTiposDeUsuario dtoWhere =new  DtoTiposDeUsuario();
			dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
			forma.setListaTiposUsuario(TiposUsuario.cargar(dtoWhere));
			return mapping.findForward("paginaAtencion");
		}
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarNuevoDetalle(	ActionMapping mapping,
														DescuentoOdontologicoForm forma, 
														UsuarioBasico usuario) 
	{
		DtoTiposDeUsuario dtoWhere =new  DtoTiposDeUsuario();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaTiposUsuario(TiposUsuario.cargar(dtoWhere));
		forma.getDtoDetalleDescuentoOdontologico().setValorMaximoPresupuesto(ConstantesBD.codigoNuncaValido);
		forma.getDtoDetalleDescuentoOdontologico().setValorMinimoPresupuesto(ConstantesBD.codigoNuncaValido);
		forma.getDtoDetalleDescuentoOdontologico().setPorcentajeDescuento(ConstantesBD.codigoNuncaValido);
		forma.getDtoDetalleDescuentoOdontologico().setTipoUsuarioAutoriza(new InfoDatosStr(String.valueOf(ConstantesBD.codigoNuncaValido),""));
		forma.getDtoDetalleDescuentoOdontologico().setDiasVigencia(ConstantesBD.codigoNuncaValido);
		return mapping.findForward("paginaDetalle");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarDetalleHistorico(	ActionMapping mapping,
															DescuentoOdontologicoForm forma, 
															UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaHistoricoDetalleDescuento(),sortG);
		forma.setEstado("empezarDetalleHistorico");
		return mapping.findForward("paginaHistorialDetalle");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarHistorico(	ActionMapping mapping,
													DescuentoOdontologicoForm forma, 
													UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		{
		Collections.sort(forma.getListaHistoricoDescuento(),sortG);
		forma.setEstado("historico");
		return mapping.findForward("paginaHistorial");
		}else
		{
			Collections.sort(forma.getListaHistoricoDescuentoAtencion(),sortG);
			forma.setEstado("historico");
			return mapping.findForward("paginaHistorialAtencion");
		}
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionHistoricoDetalle(	ActionMapping mapping,
													DescuentoOdontologicoForm forma, 
													UsuarioBasico usuario) 
	{
		DtoHistoricoDetalleDescuentoOdontologico dtoWhereHistorico = new DtoHistoricoDetalleDescuentoOdontologico();
		dtoWhereHistorico.setDetalle(forma.getListaDetalleDescuento().get(forma.getPosArrayDetalle()).getCodigo());
		forma.setListaHistoricoDetalleDescuento(HistoricoDetalleDescuentoOdontologico.cargar(dtoWhereHistorico));
	    logger.info(dtoWhereHistorico);
		return mapping.findForward("paginaHistorialDetalle");	
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionHistorico(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {	
				DtoHistoricoDescuentoOdontologico dtoWhereHistorico = new DtoHistoricoDescuentoOdontologico();
				dtoWhereHistorico.setConsecutivo(forma.getListaDescuentos().get(forma.getPosArray()).getConsecutivo());
				
			    forma.setListaHistoricoDescuento(HistoricoDescuentoOdontologico.cargar(dtoWhereHistorico));
			    logger.info(dtoWhereHistorico);
			    
				return mapping.findForward("paginaHistorial");	
		   }
			else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
			{
			
				DtoHistoricoDescuentoOdontologicoAtencion dtoWhereHistorico = new DtoHistoricoDescuentoOdontologicoAtencion();
				dtoWhereHistorico.setConsecutivo(forma.getListaDescuentosAtencion().get(forma.getPosArray()).getConsecutivo());
				
			    forma.setListaHistoricoDescuentoAtencion(HistoricoDescuentoOdontologico.cargarAtencion(dtoWhereHistorico));
			    
			    
				return mapping.findForward("paginaHistorialAtencion");	
				
			}
		return mapping.findForward("paginaHistorial");	
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDetalle(ActionMapping mapping,
												DescuentoOdontologicoForm forma, 
												UsuarioBasico usuario) 
	{
		DtoHistoricoDetalleDescuentoOdontologico dtoWhereHistoricoDetalle = new DtoHistoricoDetalleDescuentoOdontologico();     
		
		DtoDetalleDescuentoOdontologico dtoWhere= new DtoDetalleDescuentoOdontologico();
		dtoWhere.setCodigo(forma.getListaDetalleDescuento().get(forma.getPosArrayDetalle()).getCodigo());
		dtoWhereHistoricoDetalle.setConsecutivoDescuento(dtoWhere.getConsecutivoDescuento());
		dtoWhereHistoricoDetalle.setDetalle(dtoWhere.getCodigo());
		DtoHistoricoDetalleDescuentoOdontologico dtoNuevoHistoricoDetalle = new DtoHistoricoDetalleDescuentoOdontologico();
		dtoNuevoHistoricoDetalle.setEliminado(ConstantesBD.acronimoSi);
		
		DetalleDescuentoOdontologico.eliminar(dtoWhere);
		HistoricoDetalleDescuentoOdontologico.modificar(dtoNuevoHistoricoDetalle, dtoWhereHistoricoDetalle);
		
		forma.setEstado("resumenDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarModificarDetalle(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		DtoDetalleDescuentoOdontologico dtoWhere= new DtoDetalleDescuentoOdontologico(); 
		dtoWhere.setCodigo(forma.getListaDetalleDescuento().get(forma.getPosArrayDetalle()).getCodigo());
		DtoDetalleDescuentoOdontologico dtoNuevo= new DtoDetalleDescuentoOdontologico(); 		
		
		dtoNuevo=forma.getDtoDetalleDescuentoOdontologico();
		logger.info("Viejo"+UtilidadLog.obtenerString(dtoWhere, true));
		logger.info("Nuevo"+UtilidadLog.obtenerString(dtoNuevo, true));
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoNuevo.setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setValorMaximoPresupuestoMod(forma.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setValorMinimoPresupuestoMod(forma.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setPorcentajeDescuentoMod(forma.getDtoDetalleDescuentoOdontologico().getPorcentajeDescuento());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setDiasVigenciaMod(forma.getDtoDetalleDescuentoOdontologico().getDiasVigencia());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setTipoUsuarioAutorizaMod(forma.getDtoDetalleDescuentoOdontologico().getTipoUsuarioAutoriza());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setDetalle(forma.getDtoDetalleDescuentoOdontologico().getCodigo());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
		logger.info(UtilidadLog.obtenerString(forma.getDtoDetalleHistoricoDescuentoOdontologico(),true));
	    DetalleDescuentoOdontologico.modificar(dtoNuevo, dtoWhere);
	    HistoricoDetalleDescuentoOdontologico.guardar(forma.getDtoDetalleHistoricoDescuentoOdontologico());
	    forma.setEstado("resumenDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarDetalle(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		DtoTiposDeUsuario dtoWhere =new  DtoTiposDeUsuario();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaTiposUsuario(TiposUsuario.cargar(dtoWhere));
		logger.info("Para --->"+UtilidadLog.obtenerString(forma.getListaTiposUsuario(), true));
		forma.setDtoDetalleDescuentoOdontologico((DtoDetalleDescuentoOdontologico) forma.getListaDetalleDescuento().get(forma.getPosArrayDetalle()).clone());
		
		
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setConsecutivoDescuento(forma.getDtoDetalleDescuentoOdontologico().getConsecutivoDescuento());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setEliminado(ConstantesBD.acronimoNo);
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setValorMaximoPresupuesto(forma.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setValorMinimoPresupuesto(forma.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setPorcentajeDescuento(forma.getDtoDetalleDescuentoOdontologico().getPorcentajeDescuento());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setDiasVigencia(forma.getDtoDetalleDescuentoOdontologico().getDiasVigencia());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setTipoUsuarioAutoriza( forma.getListaDetalleDescuento().get(forma.getPosArrayDetalle()).getTipoUsuarioAutoriza());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleHistoricoDescuentoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
		
		logger.info("Por --->"+UtilidadLog.obtenerString(forma.getDtoDetalleDescuentoOdontologico(), true));
		return mapping.findForward("paginaDetalle");	
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaDetalleDescuento(),sortG);
		forma.setEstado("empezarDetalle");
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarDetalle(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		forma.getDtoDetalleDescuentoOdontologico().setConsecutivoDescuento(forma.getDtoDescuentoOdontologico().getConsecutivo());
		forma.getDtoDetalleDescuentoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoDetalleDescuentoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleDescuentoOdontologico().setUsuarioModifica(usuario.getLoginUsuario());
	
		forma.getDtoDetalleDescuentoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
		
		logger.info(UtilidadLog.obtenerString(forma.getDtoDetalleDescuentoOdontologico(), true));
		logger.info("CODDDDDDDDDDDDD"+forma.getDtoDetalleDescuentoOdontologico().getTipoUsuarioAutoriza().getCodigo());
		DetalleDescuentoOdontologico.guardar(forma.getDtoDetalleDescuentoOdontologico());
		forma.setEstado("resumenDetalle");
		return accionEmpezarDetalle(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarDetalle(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		forma.setDtoDescuentoOdontologico(forma.getListaDescuentos().get(forma.getPosArray()));
		DtoDetalleDescuentoOdontologico dtoWhere = new DtoDetalleDescuentoOdontologico();
		dtoWhere.setConsecutivoDescuento(forma.getDtoDescuentoOdontologico().getConsecutivo());
		forma.setListaDetalleDescuento(DetalleDescuentoOdontologico.cargar(dtoWhere));
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		{
		
			
		    Collections.sort(forma.getListaDescuentos(),sortG);
			forma.setEstado("ingresar");
			return mapping.findForward("paginaPrincipal");
		}
		else
		{
			
			Collections.sort(forma.getListaDescuentosAtencion(),sortG);
			forma.setEstado("ingresar");
			return mapping.findForward("paginaAtencion");
		}
		
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
	    {
			DtoHistoricoDescuentoOdontologico dtoWhereHistorico = new DtoHistoricoDescuentoOdontologico();
			DtoDescuentosOdontologicos dtoWhere= new DtoDescuentosOdontologicos();
			
			dtoWhere.setConsecutivo(forma.getListaDescuentos().get(forma.getPosArray()).getConsecutivo());
			dtoWhereHistorico.setConsecutivo(dtoWhere.getConsecutivo());
			
			DtoHistoricoDescuentoOdontologico dtoNuevoHistorico = new DtoHistoricoDescuentoOdontologico();
			dtoNuevoHistorico.setEliminado(ConstantesBD.acronimoSi);
			
			DescuentoOdontologico.eliminar(dtoWhere);
			HistoricoDescuentoOdontologico.modificar(dtoNuevoHistorico, dtoWhereHistorico);
			forma.setEstado("resumen");
			
		}else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
		{
			DtoHistoricoDescuentoOdontologicoAtencion dtoWhereHistorico = new DtoHistoricoDescuentoOdontologicoAtencion();
			DtoDescuentoOdontologicoAtencion dtoWhere= new DtoDescuentoOdontologicoAtencion();
			
			logger.info("SIZEEEEEEEEEEEEEEEEEEEE"+forma.getListaDescuentosAtencion().size());
			
			dtoWhere.setConsecutivo(forma.getListaDescuentosAtencion().get(forma.getPosArray()).getConsecutivo());
			dtoWhereHistorico.setConsecutivo(dtoWhere.getConsecutivo());
			
			DtoHistoricoDescuentoOdontologicoAtencion dtoNuevoHistorico = new DtoHistoricoDescuentoOdontologicoAtencion();
			dtoNuevoHistorico.setEliminado(ConstantesBD.acronimoSi);
			
			DescuentoOdontologico.eliminarAtencion(dtoWhere);
			HistoricoDescuentoOdontologico.modificarAtencion(dtoNuevoHistorico, dtoWhereHistorico);
			forma.setEstado("resumen");	
		}
		return accionIngresar(mapping, forma, usuario);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarModificar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {	
				DtoDescuentosOdontologicos dtoWhere= new DtoDescuentosOdontologicos(); 
				dtoWhere.setConsecutivo(forma.getListaDescuentos().get(forma.getPosArray()).getConsecutivo());
				DtoDescuentosOdontologicos dtoNuevo= new DtoDescuentosOdontologicos(); 		
				
				dtoNuevo=forma.getDtoDescuentoOdontologico();
				forma.getDtoHistoricoDescuentoOdontologico().setFechaInicioVigenciaMod(dtoNuevo.getFechaInicioVigenciaFromatoBD());
				forma.getDtoHistoricoDescuentoOdontologico().setFechaFinVigenciaMod(dtoNuevo.getFechaFinVigenciaFromatoBD());
				forma.getDtoHistoricoDescuentoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
				forma.getDtoHistoricoDescuentoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
				logger.info("Viejo"+UtilidadLog.obtenerString(dtoWhere, true));
				logger.info("Nuevo"+UtilidadLog.obtenerString(dtoNuevo, true));
			    
				
				DescuentoOdontologico.modificar(dtoNuevo, dtoWhere);
			   
			    
			    HistoricoDescuentoOdontologico.guardar(forma.getDtoHistoricoDescuentoOdontologico());
			    logger.info(forma.getDtoHistoricoDescuentoOdontologico());	     
			    forma.setDtoHistoricoDescuentoOdontologico(new DtoHistoricoDescuentoOdontologico());	   
			    forma.setEstado("resumen");
		   }
			else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
			{
			
				DtoDescuentoOdontologicoAtencion dtoWhere= new DtoDescuentoOdontologicoAtencion(); 
				dtoWhere.setConsecutivo(forma.getListaDescuentosAtencion().get(forma.getPosArray()).getConsecutivo());
				DtoDescuentoOdontologicoAtencion dtoNuevo= new DtoDescuentoOdontologicoAtencion(); 		
				
				dtoNuevo=forma.getDtoDescuentoOdontologicoAtencion();
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setPorcentajeDescuentoMod(dtoNuevo.getPorcentajeDescuento());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setNivelAutorizacionMod(dtoNuevo.getNivelAutorizacion());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setDiasVigenciaMod(dtoNuevo.getDiasVigencia());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setFechaModifica(UtilidadFecha.getFechaActual());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setHoraModifica(UtilidadFecha.getHoraActual());
				logger.info("Viejo"+UtilidadLog.obtenerString(dtoWhere, true));
				logger.info("Nuevo"+UtilidadLog.obtenerString(dtoNuevo, true));
			    
				
				DescuentoOdontologico.modificarAtencion(dtoNuevo, dtoWhere);
			    
			    
			    HistoricoDescuentoOdontologico.guardarAtencion(forma.getDtoHistoricoDescuentoOdontologicoAtencion());
			    logger.info(forma.getDtoHistoricoDescuentoOdontologicoAtencion());	     
			    forma.setDtoHistoricoDescuentoOdontologicoAtencion(new DtoHistoricoDescuentoOdontologicoAtencion());	   
			    forma.setEstado("resumen");
			}
		
		return accionIngresar(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {	
			forma.setDtoDescuentoOdontologico((DtoDescuentosOdontologicos) forma.getListaDescuentos().get(forma.getPosArray()).clone());
			forma.getDtoHistoricoDescuentoOdontologico().setCentroAtencion(forma.getDtoDescuentoOdontologico().getCentroAtencion());
			forma.getDtoHistoricoDescuentoOdontologico().setConsecutivo(forma.getDtoDescuentoOdontologico().getConsecutivo());
			forma.getDtoHistoricoDescuentoOdontologico().setEliminado(ConstantesBD.acronimoNo);
			forma.getDtoHistoricoDescuentoOdontologico().setFechaFinVigencia(forma.getDtoDescuentoOdontologico().getFechaFinVigenciaFromatoBD());
			forma.getDtoHistoricoDescuentoOdontologico().setFechaInicioVigencia(forma.getDtoDescuentoOdontologico().getFechaInicioVigenciaFromatoBD());
			forma.getDtoHistoricoDescuentoOdontologico().setFechaModifica(forma.getDtoDescuentoOdontologico().getFechaModifica());
			forma.getDtoHistoricoDescuentoOdontologico().setHoraModifica(forma.getDtoDescuentoOdontologico().getHoraModifica());
			forma.getDtoHistoricoDescuentoOdontologico().setUsuarioModifica(forma.getDtoDescuentoOdontologico().getUsuarioModifica());
			forma.getDtoHistoricoDescuentoOdontologico().setInstitucion(forma.getDtoDescuentoOdontologico().getInstitucion());
			forma.getDtoDetalleDescuentoOdontologico().getTipoUsuarioAutoriza().getCodigo().concat(".0");
			return mapping.findForward("paginaPrincipal");
		   }
			else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
			{
			
				DtoTiposDeUsuario dtoWhere =new  DtoTiposDeUsuario();
				dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
				forma.setListaTiposUsuario(TiposUsuario.cargar(dtoWhere));				
				forma.setDtoDescuentoOdontologicoAtencion(((DtoDescuentoOdontologicoAtencion) forma.getListaDescuentosAtencion().get(forma.getPosArray())).clone());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setCentroAtencion(forma.getDtoDescuentoOdontologicoAtencion().getCentroAtencion());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setConsecutivo(forma.getDtoDescuentoOdontologicoAtencion().getConsecutivo());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setEliminado(ConstantesBD.acronimoNo);
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setPorcentajeDescuento(forma.getDtoDescuentoOdontologicoAtencion().getPorcentajeDescuento());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setDiasVigencia(forma.getDtoDescuentoOdontologicoAtencion().getDiasVigencia());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setFechaModifica(forma.getDtoDescuentoOdontologico().getFechaModifica());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setHoraModifica(forma.getDtoDescuentoOdontologicoAtencion().getHoraModifica());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setUsuarioModifica(forma.getDtoDescuentoOdontologicoAtencion().getUsuarioModifica());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setInstitucion(forma.getDtoDescuentoOdontologicoAtencion().getInstitucion());
				forma.getDtoHistoricoDescuentoOdontologicoAtencion().setNivelAutorizacion((InfoDatosDouble)forma.getDtoDescuentoOdontologicoAtencion().getNivelAutorizacion().clone());
				
				return mapping.findForward("paginaAtencion");
			}
		return mapping.findForward("paginaPrincipal");
	}

	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
			DescuentoOdontologicoForm forma, UsuarioBasico usuario) 
	{
		
		if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {	
			forma.getDtoDescuentoOdontologico().setFechaModifica(UtilidadFecha.getFechaActual());
			forma.getDtoDescuentoOdontologico().setHoraModifica(UtilidadFecha.getHoraActual());
			forma.getDtoDescuentoOdontologico().setUsuarioModifica(usuario.getLoginUsuario());
			forma.getDtoDescuentoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
			
			logger.info(UtilidadLog.obtenerString(forma.getDtoDescuentoOdontologico(), true));
			DescuentoOdontologico.guardar(forma.getDtoDescuentoOdontologico());
				
		   }
			else if(forma.getTipo().equals(ConstantesIntegridadDominio.acronimoPorAtencion))
			{
			
				forma.getDtoDescuentoOdontologicoAtencion().setFechaModifica(UtilidadFecha.getFechaActual());
				forma.getDtoDescuentoOdontologicoAtencion().setHoraModifica(UtilidadFecha.getHoraActual());
				forma.getDtoDescuentoOdontologicoAtencion().setUsuarioModifica(usuario.getLoginUsuario());
				forma.getDtoDescuentoOdontologicoAtencion().setInstitucion(usuario.getCodigoInstitucionInt());
				
				logger.info(UtilidadLog.obtenerString(forma.getDtoDescuentoOdontologico(), true));
				DescuentoOdontologico.guardarAtencion(forma.getDtoDescuentoOdontologicoAtencion());
				
			}
			
		
		forma.setEstado("resumen");
		return accionIngresar(mapping, forma, usuario);
	}
		
}