/*
 * Enero 22, 2008
 */
package com.princetonsa.action.glosas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.glosas.GenerarGlosaAuditoriaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.Glosas;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Generar Glosa de Auditoria (GLOSAS)
 */
public class GenerarGlosaAuditoriaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(GenerarGlosaAuditoriaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @author Sebastián Gómez
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response ) throws Exception
		{

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof GenerarGlosaAuditoriaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				GenerarGlosaAuditoriaForm generarForm =(GenerarGlosaAuditoriaForm)form;

				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


				String estado=generarForm.getEstado();
				logger.info("**********************************************");
				logger.warn("===> Estado GenerarGlosaAuditoriaAction--> "+estado);
				logger.info("**********************************************");

				if(estado == null)
				{
					generarForm.reset();	
					logger.warn("Estado no valido dentro del flujo de GENERAR GLOSAS DE AUDITORIA (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, generarForm, mapping, usuario);
				}
				else if (estado.equals("filtrarContratos"))
				{
					return accionFiltrarContratos(con,generarForm, response); //método ajax
				}
				else if (estado.equals("buscar"))
				{
					return accionBuscar(con,generarForm,mapping,usuario, request, response);
				}
				/*else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				logger.info("valor del getLinkSiguiente >> "+generarForm.getLinkSiguiente());
				response.sendRedirect(generarForm.getLinkSiguiente());
				return null;
			}*/
				else if (estado.equals("redireccion")){
					UtilidadBD.closeConnection(con);
					logger.info(generarForm.getLinkSiguiente());
					response.sendRedirect(generarForm.getLinkSiguiente());
					return null;
				}else if (estado.equals("redireccionSolicitud")){
					UtilidadBD.closeConnection(con);
					logger.info(generarForm.getLinkSiguienteSolicitudes());
					response.sendRedirect(generarForm.getLinkSiguienteSolicitudes());
					return null;
				}else if (estado.equals("redireccionAsocios")){
					UtilidadBD.closeConnection(con);
					logger.info(generarForm.getLinkSiguienteAsocios());
					response.sendRedirect(generarForm.getLinkSiguienteAsocios());
					return null;
				}



				else if (estado.equals("ordenarFacturas"))
				{
					return accionOrdenarFacturas(con, generarForm, mapping);
				}
				else if (estado.equals("detalleFactura"))
				{
					return accionDetalleFactura(con,generarForm,mapping,usuario,request);
				}
				else if (estado.equals("volverListadoFacturas"))
				{
					return accionVolverListadoFacturas(con,generarForm,mapping);
				}
				else if (estado.equals("eliminarDetalle")) //estado para eliminar un detalle de la factura
				{
					return accionEliminarDetalle(con,generarForm,mapping,usuario);
				}
				else if (estado.equals("eliminarConceptoDetalle")) //estado para eliminaar un concepto del detalle
				{
					return accionEliminarConceptoDetalle(con,generarForm,mapping);
				}
				else if (estado.equals("eliminarConceptoDetalleAsocio")) //estado para eliminar un concepto de un asocio de un detalle
				{
					return accionEliminarConceptoDetalleAsocio(con,generarForm,mapping);
				}
				else if (estado.equals("guardarConceptoDetalle")) //Estado para almacenar el concepto de un detalle factua (LLAMADO DESDE POPUP)
				{
					return accionGuardarConceptoDetalle(con,generarForm,mapping,usuario);
				}
				else if (estado.equals("guardarConceptoDetalleAsocio")) //Estado para almacenar el concepto de un detalle de asocio (LLAMADO DESDE POPUP)
				{
					return accionGuardarConceptoDetalleAsocio(con,generarForm,mapping,usuario);
				}
				else if (estado.equals("cambioConceptoDevolucion"))
				{
					return accionCambioConceptoDevolucion(con,generarForm,mapping,usuario);
				}
				else if (estado.equals("recargarFrameDetalle"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				}
				else if (estado.equals("recargarDetalleAsocios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("popUpSeleccionAsociosFactura");
				}
				else if (estado.equals("guardarAsocios"))
				{
					return accionGuardarAsocios(con,generarForm,mapping,usuario,request);
				}
				else if(estado.equals("guardarFrameDetalle"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("frameDetalleSolicitud");
				}
				else if (estado.equals("guardarDetalle"))
				{
					return accionGuardarDetalle(con,generarForm,mapping,usuario,request);
				}
				///***********ESTADOS PARA LA BUSQUEDA AVANZADA DE SOLICITUDES*************************
				else if (estado.equals("iniciarBusquedaSolicitudes")||estado.equals("realizarBusquedaSolicitudes"))
				{
					return accionBusquedaSolicitudes(con,generarForm,mapping,usuario, response);
				}
				else if (estado.equals("seleccionarBusquedaSolicitudes"))
				{
					return accionSeleccionarBusquedaSolicitudes(con,generarForm,mapping,usuario);
				}
				else if(estado.equals("ordenarSolicitudesFactura"))
				{
					return this.accionOrdenarSolicitudesFactura(generarForm, con, mapping, response);
				}
				//************************************************************************************** 
				else
				{
					generarForm.reset();
					logger.warn("Estado no valido dentro del flujo de GenerarGlosaAuditoriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
     * 
     * @param forma
     */    
    private ActionForward accionOrdenarSolicitudesFactura(GenerarGlosaAuditoriaForm forma, Connection con, ActionMapping mapping, HttpServletResponse response) 
    {
    	String[] indices = Glosas.indicesSolicitudesFacturas;
		int numReg = Utilidades.convertirAEntero(forma.getGlosa().getBusquedaSolicitudes("numRegistros")+"");
		forma.getGlosa().setBusquedaSolicitudes(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoIndice(), forma.getGlosa().getBusquedaSolicitudes(), numReg));
		forma.setUltimoIndice(forma.getPatronOrdenar());
		forma.getGlosa().setBusquedaSolicitudes("numRegistros",numReg+"");
		
		UtilidadBD.closeConnection(con);
		if(UtilidadTexto.isEmpty(forma.getLinkSiguienteSolicitudes()))
		{		
			return mapping.findForward("busquedaAvanzadaSolicitudes");
		}
		else
		{
			forma.setLinkSiguiente(forma.getLinkSiguienteSolicitudes().replaceAll("pager.offset=\\d+", "pager.offset=0"));
			logger.info(forma.getLinkSiguienteSolicitudes());
			try 
			{
				response.sendRedirect(forma.getLinkSiguienteSolicitudes());
			} 
			catch (IOException e) 
			{
				logger.info(""+e.toString());
			}
			return null;
		}
    }
	
	/**
	 * Método implementado para agregar las solicitudes a la glosa
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionarBusquedaSolicitudes(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		Glosas mundoGlosa = new Glosas();
		generarForm.setGlosa(mundoGlosa.agregarNuevosDetalleFacturaBusqueda(con,generarForm.getGlosa()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzadaSolicitudes");
	}

	/**
	 * Método para preparar o realizar la busqueda avanzada de solicitudes
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaSolicitudes(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletResponse response) 
	{
		int posicionFactura = generarForm.getGlosa().getPosicionFactura();
		int codigoTarifarioOficial = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()),true);
		
		if(generarForm.getEstado().equals("iniciarBusquedaSolicitudes"))
		{
			//Si la busqueda apenas empeiza se inicializan los campos de busqueda
			generarForm.getGlosa().setNumeroSolicitudBusqueda("");
			generarForm.getGlosa().setCodigoServicioBusqueda("");
			generarForm.getGlosa().setDescripcionServicioBusqueda("");
			generarForm.getGlosa().setCodigoArticuloBusqueda("");
			generarForm.getGlosa().setDescripcionArticuloBusqueda("");
			generarForm.getGlosa().setSeleccionTodosBusqueda(ConstantesBD.acronimoNo);
		}
		
		String codigoEstandarBusquedaArticulo = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt());
		
		Glosas mundoGlosa = new Glosas();
		generarForm.getGlosa().setBusquedaSolicitudes(mundoGlosa.cargarSolicitudesFactura(
			con, 
			Integer.parseInt(generarForm.getGlosa().getFacturas().get(posicionFactura).getCodigoFactura()), //codigo de la factura 
			generarForm.getGlosa().getFacturas().get(posicionFactura).obtenerCodigosDetalleFactura(),  //codigos de los detalles de factura ingresados
			codigoTarifarioOficial, //codigo tarifario oficial
			Utilidades.convertirAEntero(generarForm.getGlosa().getNumeroSolicitudBusqueda()), //numero de la solicitud 
			generarForm.getGlosa().getCodigoServicioBusqueda(), //codigo servicio
			generarForm.getGlosa().getDescripcionServicioBusqueda(), //descripcion servicio 
			Utilidades.convertirAEntero(generarForm.getGlosa().getCodigoArticuloBusqueda()), //codigo articulo 
			generarForm.getGlosa().getDescripcionArticuloBusqueda(), //descripcoin articulo
			codigoEstandarBusquedaArticulo) //Codigo Estandar Busqueda por Articulo
		);
		generarForm.getGlosa().setTarifarioOficialBusqueda(UtilidadesFacturacion.obtenerNombreTarifarioOficial(con,codigoTarifarioOficial));
		UtilidadBD.closeConnection(con);
		
		if(UtilidadTexto.isEmpty(generarForm.getLinkSiguienteSolicitudes())) //()) || generarForm.getLinkSiguiente().contains("Asocios"))
		{		
			generarForm.setLinkSiguiente("");
			return mapping.findForward("busquedaAvanzadaSolicitudes");
		}
		else
		{
			logger.info("\n\nentro!!!");
			generarForm.setLinkSiguiente(generarForm.getLinkSiguienteSolicitudes().replaceAll("pager.offset=\\d+", "pager.offset=0"));
			logger.info(generarForm.getLinkSiguienteSolicitudes());
			try 
			{
				response.sendRedirect(generarForm.getLinkSiguienteSolicitudes());
			} 
			catch (IOException e) 
			{
				logger.info(""+e.toString());
			}
			return null;
		}
	}

	/**
	 * Método para guardar la informacion de la glosa
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.iniciarTransaccion(con);
		//Para verificar si la glosa es nueva o es una modificacion
		boolean esNueva = generarForm.getGlosa().getGlosaSistema().equals("")?true:false;
		
		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(generarForm.getGlosa());
		mundoGlosa.guardarGlosa(con, usuario, "GenerarAuditoria");
		
		if(mundoGlosa.getErrores().isEmpty())
		{
			UtilidadBD.finalizarTransaccion(con);
			generarForm.setGlosa(mundoGlosa.cargarGlosaAuditoria(con, generarForm.getCodigoAuditoria(), "", usuario,true));
			generarForm.getGlosa().setEsResumen(true);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, mundoGlosa.getErrores());
			generarForm.setEstado("");
			//Si la glosa es nueva, se finaliza el uso del consecutivo
			if(esNueva&&!mundoGlosa.getGlosa().getGlosaSistema().equals(""))
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoGlosas,usuario.getCodigoInstitucionInt(), mundoGlosa.getGlosa().getGlosaSistema(),ConstantesBD.acronimoNo, ConstantesBD.acronimoNo );
		}
		
		generarForm.setCodigoFac(mundoGlosa.getGlosa().getFacturas().get(mundoGlosa.getGlosa().getPosicionFactura()).getCodigoFactura());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método implementado para guardar la informacion de los asocios de la cirugia en memoria
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarAsocios(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		int posicionFactura = generarForm.getGlosa().getPosicionFactura();
		int posicionDetalle = generarForm.getGlosa().getFacturas().get(posicionFactura).getPosicionDetalle();
		
		//**************VALIDACIONES DATOS DE LOS ASOCIOS*********************************************************
		Glosas mundoGlosa = new Glosas();
		mundoGlosa.setGlosa(generarForm.getGlosa());
		generarForm.getGlosa().getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).setValorGlosaStr(generarForm.getSumatoriaValorGlosa()+"");
		mundoGlosa.validacionCamposAsocio(posicionFactura, posicionDetalle, true, false, "GenerarAuditoria");
		errores = mundoGlosa.getErrores();
		//**********************************************************************************************************
			
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			generarForm.setEstado("");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}

	/**
	 * Método implementado para procesar el cambio de seleccion/deseleccion del concepto de devolución de una factura
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambioConceptoDevolucion(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		
		//Se toma la informacion del concepto Seleccionado
		for(HashMap<String, Object> elemento:generarForm.getConceptosDevolucion())
			if(generarForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).getCodigoConcepto().equals(elemento.get("codigo").toString()))
			{
				generarForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setDescripcion(elemento.get("nombre").toString());
				generarForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setTipo(elemento.get("tipoConcepto").toString());
				generarForm.getGlosa().getFacturas().get(posFactura).getConceptos().get(0).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			}
		
		Glosas mundoGlosa = new Glosas();
		generarForm.setGlosa(mundoGlosa.procesarCambioConceptoDevolucion(con, generarForm.getGlosa(), "GenerarGlosaAuditoria"));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método implementado para guardar el concepto de un asocio
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalleAsocio(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		boolean existeConcepto = false;
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		int posDetalle = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posAsocio = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionAsocio();
		
		//Se limpia mensaje de error
		generarForm.getGlosa().getFacturas().get(posFactura).setMensajeError("");
		
		//***********VALIDACION DE LA SELECCION DEL CONCEPTO***************************
		for(DtoConceptoGlosa concepto:generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos())
			if(concepto.getCodigoConcepto().equals(generarForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto())&&!concepto.isEliminado())
				existeConcepto = true;
		//*****************************************************************************
		
		//Si el concepto no fue seleccionado se agrega
		if(!existeConcepto)
		{
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(generarForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setDescripcion(generarForm.getGlosa().getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(generarForm.getGlosa().getConceptoSeleccionado().getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			
			generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos().add(nuevoConcepto);
		}
		else
			generarForm.getGlosa().getFacturas().get(posFactura).setMensajeError("El concepto "+generarForm.getGlosa().getConceptoSeleccionado().getDescripcion()+" ya fue seleccionado");
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleAsocio");
	}

	/**
	 * Método implementado para guardar el concepto de un detalle
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarConceptoDetalle(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		boolean existeConcepto = false;
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		int posDetalle = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		
		//Se limpia mensaje de error
		generarForm.getGlosa().getFacturas().get(posFactura).setMensajeError("");
		
		//***********VALIDACION DE LA SELECCION DEL CONCEPTO***************************
		for(DtoConceptoGlosa concepto:generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos())
			if(concepto.getCodigoConcepto().equals(generarForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto())&&!concepto.isEliminado())
				existeConcepto = true;
		//*****************************************************************************
		
		//Si el concepto no fue seleccionado se agrega
		if(!existeConcepto)
		{
			DtoConceptoGlosa nuevoConcepto = new DtoConceptoGlosa();
			nuevoConcepto.setCodigoConcepto(generarForm.getGlosa().getConceptoSeleccionado().getCodigoConcepto());
			nuevoConcepto.setDescripcion(generarForm.getGlosa().getConceptoSeleccionado().getDescripcion());
			nuevoConcepto.setTipo(generarForm.getGlosa().getConceptoSeleccionado().getTipo());
			nuevoConcepto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			
			generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
			//cambio temporal para arreglo de ajustes en la respuesta de glosa
			//generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().clear();
			//generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().add(nuevoConcepto);
		}
		else
			generarForm.getGlosa().getFacturas().get(posFactura).setMensajeError("El concepto "+generarForm.getGlosa().getConceptoSeleccionado().getDescripcion()+" ya fue seleccionado");
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionConceptoDetalleFactura");
	}

	/**
	 * Método para eliminar el concepto de un asocio
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalleAsocio(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping) 
	{
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		int posDetalle = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posAsocio = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionAsocio();
		int posConcepto = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionConcepto();
		
		
		generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getAsocios().get(posAsocio).getConceptos().get(posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popUpSeleccionAsociosFactura");
	}

	/**
	 * Método para eliminar el concepto de un detalle de factura
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConceptoDetalle(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping) 
	{
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		int posDetalle = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		int posConcepto = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionConcepto();
		
		generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).getConceptos().get(posConcepto).setEliminado(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para eliminar el detalle de una factura
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDetalle(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		int posFactura = generarForm.getGlosa().getPosicionFactura();
		int posDetalle = generarForm.getGlosa().getFacturas().get(posFactura).getPosicionDetalle();
		
		generarForm.getGlosa().getFacturas().get(posFactura).getDetalle().get(posDetalle).setEliminado(true);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameDetalleSolicitud");
	}

	/**
	 * Método para volver al listado de facturas
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverListadoFacturas(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}

	/**
	 * Método para pasar al detalle de la factura
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleFactura(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,	UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		int posicion = generarForm.getPosicion();
		String codigoGlosa = generarForm.getListadoFacturas("codigoGlosa_"+posicion)+"";
		
		logger.info("===> La posición es: "+posicion);
		logger.info("===> El codigo de glosa es: "+codigoGlosa);
		
		Glosas mundoGlosa = new Glosas();
		DtoGlosa glosa = new DtoGlosa();
		glosa = mundoGlosa.cargarGlosaAuditoria(con, codigoGlosa, "", usuario, true);
		if(glosa.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoRegistrado))
			glosa.setEsResumen(true);
		generarForm.setGlosa(glosa);
		generarForm.setCodigoAuditoria(codigoGlosa);
		
		//Si la glosa no tenía consecutivo entonces se valida que se haya parametrizado consecutivo disponible de glosas
		if(glosa.getGlosaSistema().trim().equals(""))
		{
			///*************VALIDACIONES INGRESO DETALLE*****************************************************************
			//Se verifica que se tenga parametrizado consecutivo disponible de glosas
			String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoGlosas, usuario.getCodigoInstitucionInt());
			if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
				errores.add("Falta consecutivo disponible",new ActionMessage("error.faltaDefinirConsecutivo","GLOSAS"));
			else if(Utilidades.convertirAEntero(consecutivo)==ConstantesBD.codigoNuncaValido)
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de glosas"));
			//**********************************************************************************************************
		}
		

		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaFacturas");
		}
		
		//Se llena arreglo de conceptos de devolucion
		generarForm.setConceptosDevolucion(Utilidades.obtenerConceptosGlosa(con, ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleFacturas");
	}

	/**
	 * Método implementado para ordenar el listado de facturas
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarFacturas(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping) 
	{
		String[] indices = Glosas.indicesListadoFacturasAuditadas;
		int numFacturas = generarForm.getNumListadoFacturas();
		
		generarForm.setListadoFacturas(Listado.ordenarMapa(indices,
				generarForm.getIndice(),
				generarForm.getUltimoIndice(),
				generarForm.getListadoFacturas(),
				numFacturas));
		
		generarForm.setListadoFacturas("numRegistros",numFacturas+"");
		
		generarForm.setUltimoIndice(generarForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}

	/**
	 * Método para buscar las facturas auditadas
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con,
			GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		//******************VALIDACIONES CAMPOS BUSQUEDA ************************************************
		ActionErrors errores = validacionBusqueda(con,generarForm); 
		//*************************************************************************************************
		
		logger.info("errores totales: "+errores.size());
		
		if(!errores.isEmpty())
		{
			generarForm.setRealizoBusqueda(false);
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaFacturas");
		}
		else
		{
			generarForm.setRealizoBusqueda(true);
			generarForm.setListadoFacturas(Glosas.consultarFacturasAuditadas(
				con, 
				generarForm.getFechaAuditoriaInicial(), 
				generarForm.getFechaAuditoriaFinal(), 
				generarForm.getFacturaInicial(), 
				generarForm.getFacturaFinal(), 
				generarForm.getCodigoConvenio(), 
				generarForm.getCodigoContrato(), 
				generarForm.getNumeroPreGlosa(), 
				usuario.getCodigoInstitucionInt(),
				"GenerarGlosaAuditoria",
				Glosas.cargarListadoConveniosUsuario(usuario, ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor, true)));
			UtilidadBD.closeConnection(con);
			return UtilidadSesion.redireccionar("", generarForm.getMaxPageItems(), generarForm.getNumListadoFacturas(), response, request, "busquedaFacturas.jsp", false);
		}
		
		
	}

	/**
	 * Método para realizar las validaciones de busqueda
	 * @param con
	 * @param generarForm
	 * @return
	 */
	private ActionErrors validacionBusqueda(Connection con,GenerarGlosaAuditoriaForm generarForm) 
	{
		ActionErrors errores = new ActionErrors();
		boolean ingresoInfo = false;
		boolean fechaInicialValida = false;
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		
		//Fecha auditoria inicial ***********************************************************
		if(!generarForm.getFechaAuditoriaInicial().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(generarForm.getFechaAuditoriaInicial()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","auditoría inicial"));
			else
			{
				fechaInicialValida = true;
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, generarForm.getFechaAuditoriaInicial()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","auditoría inicial","del sistema: "+fechaSistema));
			}
			
			if(generarForm.getFechaAuditoriaFinal().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha auditoría final"));
		}
		
		//Fecha auditoria final***********************************************************
		if(!generarForm.getFechaAuditoriaFinal().equals(""))
		{
			ingresoInfo = true;
			if(!UtilidadFecha.validarFecha(generarForm.getFechaAuditoriaFinal()))
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","auditoría final"));
			else
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaSistema, generarForm.getFechaAuditoriaFinal()))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","auditoría final","del sistema: "+fechaSistema));
				
				if(fechaInicialValida&&UtilidadFecha.esFechaMenorQueOtraReferencia(generarForm.getFechaAuditoriaFinal(), generarForm.getFechaAuditoriaInicial()))
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","auditoría final","auditoría inicial"));
				else if(fechaInicialValida&&UtilidadFecha.numeroDiasEntreFechas(generarForm.getFechaAuditoriaInicial(), generarForm.getFechaAuditoriaFinal())>180)
					errores.add("", new ActionMessage("error.facturasVarias.consultaFacturasVarias","de auditoría"));
					
				
			}
			
			if(generarForm.getFechaAuditoriaInicial().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La fecha auditoría inicial"));
		}
		
		//Factura inicial *******************************************************************
		if(!generarForm.getFacturaInicial().equals(""))
		{
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(generarForm.getFacturaInicial())<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","La factura inicial","0"));
			
			if(generarForm.getFacturaFinal().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La factura final"));
		}
		
		//Factura final **********************************************************************
		if(!generarForm.getFacturaFinal().equals(""))
		{
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(generarForm.getFacturaFinal())<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","La factura final","0"));
			else if(Utilidades.convertirAEntero(generarForm.getFacturaFinal(),true)<Utilidades.convertirAEntero(generarForm.getFacturaInicial(), true))
				errores.add("", new ActionMessage("errors.MayorIgualQue","La factura final","la factura inicial"));
			
			if(generarForm.getFacturaInicial().trim().equals(""))
				errores.add("", new ActionMessage("errors.required","La factura inicial"));
		}
		
		if(generarForm.getCodigoConvenio()>0||generarForm.getCodigoContrato()>0)
			ingresoInfo = true;
		
		//PRE-GLOSA ************************************************************************************
		if(!generarForm.getNumeroPreGlosa().equals(""))
		{
			if(Utilidades.convertirAEntero(generarForm.getNumeroPreGlosa()) <=0 )
			{
				errores.add("", new ActionMessage("errors.integerMayorQue","El campo pre-glosa","0"));
			}
			ingresoInfo = true;
			if(Utilidades.convertirAEntero(generarForm.getNumeroPreGlosa())==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.integer","El campo pre-glosa"));
		}
		
		if(!ingresoInfo)
			errores.add("", new ActionMessage("errors.minimoCampos","ingresar un campo","búsqueda de facturas"));
				
		return errores;
	}

	/**
	 * Método para filtrar los contratos al cambiar el convenio
	 * @param con
	 * @param generarForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarContratos(Connection con,
			GenerarGlosaAuditoriaForm generarForm, HttpServletResponse response) 
	{

		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoContrato</id-select>" +
				"<id-arreglo>contrato</id-arreglo>" +
			"</infoid>" ;
		
		
		generarForm.setArregloContratos(Utilidades.obtenerContratos(con, generarForm.getCodigoConvenio(), false, true));
		
		for(HashMap elemento:generarForm.getArregloContratos())
		{
			resultado += "<contrato>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("numerocontrato")+" Vig: "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechainicialbd")+"")+" - "+UtilidadFecha.conversionFormatoFechaAAp(elemento.get("fechafinal")+"")+"</descripcion>";
			resultado += "</contrato>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarContratos: "+e);
		}
		
		UtilidadBD.closeConnection(con);
		return null;
	}

	/**
	 * Método para iniciar el flujo de generacion de glosas de auditoria
	 * @param con
	 * @param generarForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,GenerarGlosaAuditoriaForm generarForm, ActionMapping mapping,UsuarioBasico usuario) 
	{
		generarForm.reset();
		generarForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		/*
		 * Se cargan los arreglos
		 */
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt())))
			generarForm.setArregloConvenios(UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa, true));
		else
			generarForm.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaFacturas");
	}
}
