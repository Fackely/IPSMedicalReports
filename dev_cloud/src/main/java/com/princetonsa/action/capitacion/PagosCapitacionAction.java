/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.capitacion.PagosCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.PagosCapitacion;
import com.princetonsa.pdf.PagosCapitacionPdf;

/**
 * Action de Aplicación de Pagos de Capitación
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Jul 6, 2006
 */
public class PagosCapitacionAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(PagosCapitacionAction.class);
	
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
		
		if(form instanceof PagosCapitacionForm)
		{
			
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				PagosCapitacionForm forma =(PagosCapitacionForm)form;
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String estado=forma.getEstado(); 
				logger.warn("[PagosCapitacionAction] --> "+estado);
				
				forma.setMostrarMensaje(new ResultadoBoolean(false,""));
				if(estado == null)
				{
						forma.reset();	
						logger.warn("Estado no valido dentro del flujo de Aplicación Pagos Capitación (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaError");
				}
				//---Estado que empieza con la aplicación de pagos de capitación
				else if(estado.equals("empezarAplicacionPagos"))
				{
					return accionEmpezarAplicacionPagos (con, mapping, forma, usuario);
				}
				//--Para realizar la búsqueda avanzada de documentos -------//
				else if(estado.equals("busquedaAvanzadaDocumentos"))
				{
					return accionBusquedaAvanzadaDocumentos (con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//----Se ordena el listado de documentos de pagos --------//
				else if(estado.equals("ordenarListadoDocumentos"))
				{
					return accionOrdenarListadoDocumentos(forma, mapping, con);
				}
				//Estado para mantener los datos del page del listado de documentos
				else if (estado.equals("redireccionDocumentos"))
				{			    
					UtilidadBD.closeConnection(con);
				    response.sendRedirect(forma.getLinkSiguiente());
				    return null;
				}
				//-----Muestra los conceptos de pago del documento si los tiene--//
				else if(estado.equals("conceptosPago"))
				{
				    this.accionConceptosPago(con, forma, false, usuario.getCodigoInstitucionInt());
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("conceptosPago");
				}
				//------Se agrega un nuevo concepto --------//
				else if(estado.equals("nuevoConcepto"))
				{
				    this.accionAdicionarNuevoConcepto(forma);
				    UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("conceptosPago");
				}
				//---Se guardan los conceptos de pago ------//
				else if(estado.equals("guardarAplicacionConceptos"))
				{
				    this.accionGuardarAplicacionConceptos (con, forma, usuario);
				    UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("detallePagoCxC");
				}
				//------Se elimina el concepto de pago --------//
				else if(estado.equals("eliminarConcepto"))
				{
				    this.accionEliminarConceptos(forma);
				    UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("conceptosPago");
				}
				//---Se anula la aplicación de pagos -------------------//
				else if(estado.equals("anularAplicacion"))
				{
				    this.accionAnularAplicacionPago(con,forma,usuario);
				    UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("principal");
				}
				//---Para regresar a la página de conceptos de pago
				else if(estado.equals("volverConceptos"))
				{
					this.accionConceptosPago(con, forma, true, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("conceptosPago");
				}
				//-------Se realiza la búsqueda de las cuentas de cobro de capitación para el convenio capitado
				else if(estado.equals("busquedaCuentasCobro"))
				{
				    forma.setCuentaCobroBuscar("");
				    this.accionBuscarCuentasCobroConvenio(con,forma, usuario.getCodigoInstitucionInt());
				    UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("detallePagoCxC");
				}
				//-------Se realiza la búsqueda de la cuenta de cobro que se digita en la página de detalle de pago
				else if(estado.equals("busquedaCxCCapitacion"))
				{
				    return this.accionBuscarCuentaCobro(con,forma, usuario.getCodigoInstitucionInt(), mapping, request);
				}
				//-------Recargar la página de detalle de pago ----------//
				else if(estado.equals("recargar"))
				{
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("detallePagoCxC");
				}
				//--Se realiza la búsqueda de la cuenta de cobro desde el popup de CxC
				else if(estado.equals("busquedaAvanzadaCXC"))
				{
				    this.accionBusquedaAvanzadaCuentaCobro(con,forma, usuario.getCodigoInstitucionInt());
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("popupBusquedaCXC");
				}
				//----Se ordena el listado de cuentas de cobro mostradas en el popup --------//
				else if(estado.equals("ordenarCuentasCobro"))
				{
					return accionOrdenarCuentasCobro(forma, mapping, con);
				}
				//-----Se adiciona las cuentas de cobro seleccionadas al mapa de pagos cxc
				else if(estado.equals("adicionarCXC"))
				{
				    this.accionAdicionarCXCSeleccionado(forma, usuario.getCodigoInstitucionInt());
				    UtilidadBD.closeConnection(con);
				    return null;
				}
				//-----Se elimina la cuenta de cobro ------//
				else if(estado.equals("eliminarCXC"))
				{
				    this.accionEliminarCXC(forma);
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("detallePagoCxC");
				}
				//---Se  guardan los pagos de cuentas de cobro a la aplicación respectiva -------//
				else if(estado.equals("guardarAplicacionPagosCxC"))
				{
				    this.accionGuardarAplicacionPagosCxC(con,forma,usuario);
				    forma.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON EXITO."));
				    UtilidadBD.closeConnection(con);
				    return mapping.findForward("detallePagoCxC");
				}
//------------------------------------------------------APROBACIÓN DE PAGOS DE CAPITACIÓN --------------------------------------------------------------//
				//---Estado que empieza con la aprobación de pagos de capitación
				else if(estado.equals("empezarAprobacionPagos"))
				{
					//----Se resetea el form de Pagos Capitación ---------//
					forma.reset();
					UtilidadBD.closeConnection(con);
				    return mapping.findForward("principalAprobacion");
				}
				//--Para realizar la búsqueda de las aplicaciones pendientes de acuerdo a parámetros de búsqueda
				else if(estado.equals("buscarPagosAprobar"))
				{
					return accionBusquedaPagosParaAprobar (con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//--------Se aprueba la aplicación del pago ----------------------//
				else if(estado.equals("aprobarAplicacionPago"))
				{
					return this.accionAprobarAplicacionPago(con, mapping, request, forma, usuario);
				}
				//-----Imprime el pago aprobado -----------------------//
				else if(estado.equals("imprimirAprobacionPago"))
				{
					return accionImprimirAprobacionPago(con, mapping, forma, usuario, request);
				}
//----------------------------------------------------- CONSULTA DE PAGOS DE CAPITACIÓN ---------------------------------------------------------//
				//---Estado que empieza con la consulta de pagos de capitación
				else if(estado.equals("empezarConsultaPagos"))
				{
					//----Se resetea el form de Pagos Capitación ---------//
					forma.reset();
					UtilidadBD.closeConnection(con);
				    return mapping.findForward("principalConsulta");
				}		
				//--Para realizar la búsqueda de los pagos de acuerdo a los parámetros de búsqueda
				else if(estado.equals("buscarPagosConsulta"))
				{
					return accionBusquedaPagosConsulta (con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//-----Imprime la aplicación de pago consulta -----------------------//
				else if(estado.equals("imprimirPagoConsultado"))
				{
					return accionImprimirConsultaAplicacionPago(con, mapping, forma, usuario, request);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
		}//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	/**
	 * Método que muestra la página de inicio para la aplicación de pagos
	 * de capitación
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarAplicacionPagos(Connection con, ActionMapping mapping, PagosCapitacionForm forma, UsuarioBasico usuario) 
	{
		PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
		
		//----------Se resetea el forma
		forma.reset();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt());
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		forma.setMaxPageItems(Integer.parseInt(numItems) );		
		
		//----Se consulta el listado de documentos pendientes de aplicar aprobar pagos de convenios capitados---//
		forma.setMapaDocumentos(mundoPagosCapitacion.consultarDocumentosPagos(con, usuario.getCodigoInstitucionInt()));

		 UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que realiza la búsqueda avanzada de los documentos de pago
	 * de acuerdo a los parámetros de búsqueda
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBusquedaAvanzadaDocumentos(Connection con, ActionMapping mapping, PagosCapitacionForm forma, int codigoInstitucion) throws SQLException
	{
		PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
		
		forma.setMapaDocumentos(mundoPagosCapitacion.busquedaAvanzadaDocumentos (con, forma.getTipoDocBusqueda(), forma.getDocumentoBusqueda(), forma.getFechaDocBusqueda(), forma.getConvenioBusqueda(), codigoInstitucion));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que ordena el listado de documentos de pago de capitación
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarListadoDocumentos(PagosCapitacionForm forma, ActionMapping mapping, Connection con) 
	{
		String[] indices={
        		"codigo_",
        		"codigo_convenio_",
        		"nombre_convenio_",
        		"codigo_tipo_doc_",
        		"descripcion_tipo_doc_",
        		"acronimo_tipo_doc_",
        		"documento_",
        		"codigo_estado_",
        		"descripcion_estado_",
        		"valor_documento_",
        		"fecha_documento_", 
        		"aplicacion_actual_",
        		"numero_aplicacion_",
                "modificacion_",
                "valor_pagos_cxc_",
                "valor_x_aplicar_",
                "observaciones_"
        		};
			
		int numReg=Integer.parseInt(forma.getMapaDocumentos("numRegistros")+"");
		
		forma.setMapaDocumentos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaDocumentos(),numReg));
		forma.setMapaDocumentos("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		
		UtilidadBD.closeConnection(con);
        return mapping.findForward("principal");
	}
	
	/**
	 * Método para mostrar/agregar los conceptos de pago del documento seleccionado 
	 * @param con
	 * @param forma
	 * @param consultarDocs 
	 * @param institucion 
	 * @return
	 */
	private void accionConceptosPago(Connection con, PagosCapitacionForm forma, boolean consultarDocs, int institucion)
	{
		PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
		
		/* Se consulta los documento nuevamente cuando se da volver en el detalle de pagos,
		 		ya que se debe actualizar el valor_pagos_cxc*/
		if(consultarDocs)
			{
			//----Se consulta el listado de documentos pendientes de aplicar aprobar pagos de convenios capitados---//
			forma.setMapaDocumentos(mundoPagosCapitacion.consultarDocumentosPagos(con, institucion));
			}
		
		
		//MT 6689
		String aplicacion_actual = forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago()) + "";
		aplicacion_actual = !UtilidadTexto.isEmpty(aplicacion_actual) ? aplicacion_actual : "-1";
		//Fin MT 6689
		
		//-----Se consultan los conceptos de pago para la apliación del pago --------------//
		forma.setMapaConceptosPago(mundoPagosCapitacion.consultarConceptosPago(con, Integer.parseInt(aplicacion_actual)));
		//----Se consultan los conceptos de pago del documento de pago con estado aprobado, estos no 
		//---permitirán su modificación
		forma.setMapaConceptosPagoAprobados(mundoPagosCapitacion.consultarConceptosPagoAprobadosDoc(con, Integer.parseInt(forma.getMapaDocumentos("codigo_"+forma.getIndiceDocumentoPago())+"")));
		
		forma.setObservacionesAplicacion(forma.getMapaDocumentos("observaciones_"+forma.getIndiceDocumentoPago())+"");
	}
	
	/**
	 * Método que agrega un nuevo concepto a la aplicación de pago de capitación
     * @param forma
     */
    private void accionAdicionarNuevoConcepto(PagosCapitacionForm forma)
    {
        int pos=Integer.parseInt(forma.getMapaConceptosPago("numRegistros")+"");
        
        //forma.setMapaConceptosPago("codigo_concepto_"+pos,"-1");
        //forma.setMapaConceptosPago("desconcepto_"+pos,"");
        forma.setMapaConceptosPago("concepto_pago_"+pos,"-1");
        forma.setMapaConceptosPago("valor_base_"+pos,forma.getMapaDocumentos("valor_x_aplicar_"+forma.getIndiceDocumentoPago())+"");
        forma.setMapaConceptosPago("porcentaje_"+pos,"0");
        forma.setMapaConceptosPago("valor_concepto_"+pos,"0");
        forma.setMapaConceptosPago("numRegistros",(pos+1)+"");
    }
    
    /**
     * Método que guarda la aplicación y los conceptos de pago respectivos
     * @param con
     * @param forma
     * @param usuario
     */
	private void accionGuardarAplicacionConceptos(Connection con, PagosCapitacionForm forma, UsuarioBasico usuario)
	{
		  int numeroRegistros=0;
	      
		  //-----Se cuenta el número de conceptos que no están eliminados
		  for(int i=0;i<Integer.parseInt(forma.getMapaConceptosPago("numRegistros")+"");i++)
	        {
	            if(!UtilidadTexto.getBoolean(forma.getMapaConceptosPago("eliminado_"+i)+""))
	            {
	                numeroRegistros++;
	            }
	        }
		  
	      // if(numeroRegistros>0 || UtilidadTexto.getBoolean(forma.getMapaDocumentos("modificacion_"+forma.getIndiceDocumentoPago())+""))
	      this.accionGuardarConceptos(con,forma, usuario);
		
	}

	/**
	 * Método que guarda/elimina los concetos de pago y actualiza/inserta la aplicación de pagos
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void accionGuardarConceptos(Connection con, PagosCapitacionForm forma, UsuarioBasico usuario)
	{
		PagosCapitacion mundo=new PagosCapitacion();
		HashMap mapa=(HashMap)forma.getMapaConceptosPago().clone();
		
		mapa.put("fecha_aplicacion", forma.getFechaAplicacion());
        mapa.put("observaciones",forma.getObservacionesAplicacion());
        mapa.put("estado_aplicacion",ConstantesBD.codigoEstadoAplicacionPagosPendiente+"");
        mapa.put("estado_pago",ConstantesBD.codigoEstadoPagosAplicado+"");
        mapa.put("numero_aplicacion",forma.getMapaDocumentos("numero_aplicacion_"+forma.getIndiceDocumentoPago()));
        mapa.put("pago_general_empresa",forma.getMapaDocumentos("codigo_"+forma.getIndiceDocumentoPago()));
        mapa.put("modificacion",forma.getMapaDocumentos("modificacion_"+forma.getIndiceDocumentoPago()));
        mapa.put("codigo_aplicacion",forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago()));
        mapa.put("institucion",usuario.getCodigoInstitucionInt()+"");
        mapa.put("usuario",usuario.getLoginUsuario());
        mapa.put("fecha_grabacion",UtilidadFecha.getFechaActual());
        mapa.put("hora_grabacion",UtilidadFecha.getHoraActual());
        
        mundo.guardarConceptosAplicacionPagos(con,mapa);
        /*forma.setConceptosAplicacionPagos("actualizado", "true");
        this.accionConsultarPagosGeneralEmpresa(con,forma,mundo,usuario);
        forma.setGuardadoEncabezado(true);*/
        
        //----Se consulta el listado de documentos pendientes de aplicar aprobar pagos de convenios capitados---//
		forma.setMapaDocumentos(mundo.consultarDocumentosPagos(con, usuario.getCodigoInstitucionInt()));
		
		//-----Se consultan los pagos realizados en la aplicación de pago con cuentas de cobro-------//
		//MT 6689
		String aplicacion_actual = forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago()) + "";
		aplicacion_actual = !UtilidadTexto.isEmpty(aplicacion_actual) ? aplicacion_actual : "-1";
		//Fin MT 6689
		forma.setMapaPagosCXC(mundo.consultarPagosCuentaCobro(con, Integer.parseInt(aplicacion_actual)));
	}
	
	/**
	 * Método que elimina un concepto de apgo
     * @param forma
     */
    private void accionEliminarConceptos(PagosCapitacionForm forma)
    {
        forma.setMapaConceptosPago("eliminado_"+forma.getConEliminar(),"true");
    }
    
    /**
     * Método que realiza todo el proceso de anulación de una aplicación de pago
     * @param con
     * @param forma
     * @param usuario
     */
	private void accionAnularAplicacionPago(Connection con, PagosCapitacionForm forma, UsuarioBasico usuario)
	{
		PagosCapitacion mundo=new PagosCapitacion();
		 HashMap mapa=new HashMap();
		 
		mapa.put("numero_pago",forma.getMapaDocumentos("codigo_"+forma.getIndiceDocumentoPago()));
	    mapa.put("codigo_aplicacion",forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago()));
	    mapa.put("institucion",usuario.getCodigoInstitucionInt()+"");
        mapa.put("login_usuario",usuario.getLoginUsuario());
        mapa.put("fecha", UtilidadFecha.getFechaActual());
        mapa.put("hora",UtilidadFecha.getHoraActual());
        mapa.put("motivo",forma.getMotivoAnulacion());
        
        //---------Se anula la aplicación del pago --------//
        mundo.anularAplicacionPago(con,mapa);
        
        //----Se consulta el listado de documentos pendientes de aplicar aprobar pagos de convenios capitados---//
		forma.setMapaDocumentos(mundo.consultarDocumentosPagos(con, usuario.getCodigoInstitucionInt()));
		
	}
	
	/**
	 * Método que consulta la(s) cuentas de cobro del convenio capitado, que están radicadas y
	 * que la cuenta de cobro tenga saldo mayor a cero
	 * @param con
	 * @param forma
	 * @param institucion
	 */
	private boolean accionBuscarCuentasCobroConvenio(Connection con, PagosCapitacionForm forma, int institucion)
	{
		PagosCapitacion mundo=new PagosCapitacion(); 
		HashMap mapa=new HashMap();
		 
	     mapa.put("convenio",forma.getMapaDocumentos("codigo_convenio_"+forma.getIndiceDocumentoPago()));
	     mapa.put("institucion", institucion+"");
	     mapa.put("cuentaCobroBuscar", forma.getCuentaCobroBuscar()+"");
	     
	     //------Se agrega en una cadena las cuentas de cobro que ya se adicionaron al detalle de pago y no las busque
	     String cxc="-1";
	     	     
	      for(int i=0;i<Integer.parseInt(forma.getMapaPagosCXC("numRegistros")+"");i++)
	       {
	           if(!UtilidadTexto.getBoolean(forma.getMapaPagosCXC("eliminado_"+i)+""))
	               cxc=cxc+","+forma.getMapaPagosCXC("cxc_"+i);
	       }
	       mapa.put("cuentas",cxc);
	       
	       forma.setMapaBusquedaCXC(mundo.buscarCuentasCobroConvenio(con,mapa));
	       
	       //---------Se verifica si la consulta obtuvo resultados ----------//
	       if(Integer.parseInt(forma.getMapaBusquedaCXC("numRegistros")+"")>0)
	    	   return true;
	       else
	    	   return false;
	}
	
	
	/**
	 * Método que realiza la búsqueda de la cuenta de cobro que se digita en la página de detalle de pago
	 * @param con
	 * @param forma
	 * @param codigoInstitucionInt
	 * @param mapping
	 * @param request
	 */
	private ActionForward accionBuscarCuentaCobro(Connection con, PagosCapitacionForm forma, int codigoInstitucionInt, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();
		
		//--------Si encuentra alguna cuenta de cobro se adiciona a la página de detalle------------//
		if(this.accionBuscarCuentasCobroConvenio(con,forma, codigoInstitucionInt))
		{
			forma.setMapaBusquedaCXC("seleccionado_0", "true");
			accionAdicionarCXCSeleccionado(forma, codigoInstitucionInt);
		}
		else
		{
			errores.add("noHayCuentasCobro", new ActionMessage("error.capitacion.pagoCapitacion.noExistenCuentasCobro"));
			saveErrors(request, errores);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePagoCxC");
		
	}
	
    /**
     * Método que agrega una cuenta de cobro al detalle de pagos por 
     * cuentas de cobro
     * @param forma
     * @param institucion
     */
    private void accionAdicionarCXCSeleccionado (PagosCapitacionForm forma, int institucion)
    {
    	for(int i=0;i<Integer.parseInt(forma.getMapaBusquedaCXC("numRegistros")+"");i++)
        {
            if(UtilidadTexto.getBoolean(forma.getMapaBusquedaCXC("seleccionado_"+i)+""))
            {
                int numReg=Integer.parseInt(forma.getMapaPagosCXC("numRegistros")+"");
                forma.setMapaPagosCXC("cod_aplicacion_"+numReg, forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago())+"");
                forma.setMapaPagosCXC("cxc_"+numReg, forma.getMapaBusquedaCXC("cxc_"+i)+"");
                forma.setMapaPagosCXC("fecha_radicacion_"+numReg, forma.getMapaBusquedaCXC("fecha_radicacion_"+i)+"");
                forma.setMapaPagosCXC("saldo_cxc_"+numReg, forma.getMapaBusquedaCXC("saldo_cxc_"+i)+"");
                forma.setMapaPagosCXC("institucion_"+numReg, institucion+"");
                //-------Se coloca por defecto el tipo pago total------------------//
                forma.setMapaPagosCXC("tipo_pago_"+numReg, ConstantesBD.tipoPagoTotal.getAcronimo());
                
                //-------Se asigna el valor del saldo para que el tipo de pago sea total ----------//
                forma.setMapaPagosCXC("valor_pago_cxc_"+numReg, forma.getMapaBusquedaCXC("saldo_cxc_"+i)+"");
                forma.setMapaPagosCXC("bd_"+numReg,"false");
                forma.setMapaPagosCXC("numRegistros", (numReg+1)+"");
            }
        }
    }
    
    /**
     * Método que realiza la búsqueda avanzada de la cuenta de cobro desde el popup
     * de cuentas de cobro
     * @param con
     * @param forma
     * @param codigoInstitucionInt
     */
	private void accionBusquedaAvanzadaCuentaCobro(Connection con, PagosCapitacionForm forma, int codigoInstitucionInt)
	{
		//-----Se pasa el valor del form de cxcBusAvanzada a cuentaCobroBuscar ----//
		forma.setCuentaCobroBuscar(forma.getCxcBusAvanzada());
		
		this.accionBuscarCuentasCobroConvenio(con,forma, codigoInstitucionInt);
	}
	
	/**
	 * Método que ordena las cuentas de cobro mostradas en el popup
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarCuentasCobro(PagosCapitacionForm forma, ActionMapping mapping, Connection con)
	{
		String[] indices={
        		"cxc_",
        		"fecha_radicacion_",
        		"saldo_cxc_",
        		"seleccionado_"
        		};
			
		int numReg=Integer.parseInt(forma.getMapaBusquedaCXC("numRegistros")+"");
		
		forma.setMapaBusquedaCXC(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaBusquedaCXC(),numReg));
		forma.setMapaBusquedaCXC("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		
		UtilidadBD.closeConnection(con);
        return mapping.findForward("popupBusquedaCXC");
	}
	
    /**
     * Método que elimina una cuenta de cobro del detalle de pagos
     * @param forma
     */
    private void accionEliminarCXC(PagosCapitacionForm forma)
    {
        forma.setMapaPagosCXC("eliminado_"+forma.getConEliminar(),"true");
    }
    

    /**
     * Método que guarda las cuentas de cobro agregadas al pago de la aplicación
     * de capitación
     * @param con
     * @param forma
     * @param usuario
     */
	private void accionGuardarAplicacionPagosCxC(Connection con, PagosCapitacionForm forma, UsuarioBasico usuario)
	{
		PagosCapitacion mundo=new PagosCapitacion();
        int numeroRegistros=0;

        //------Se mira cuáles pagos por cuenta de cobro han sido eliminados --------//
        for(int i=0;i<Integer.parseInt(forma.getMapaPagosCXC("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getMapaPagosCXC("eliminado_"+i)+""))
            {
                numeroRegistros++;
            }
        }
        
        if(numeroRegistros>0)
        {
            //this.accionGuardarConceptos(con, forma, mundo, usuario);
            for(int i=0;i<Integer.parseInt(forma.getMapaPagosCXC("numRegistros")+"");i++)
            {
                if(!UtilidadTexto.getBoolean(forma.getMapaPagosCXC("eliminado_"+i)+""))
                {
                    forma.setMapaPagosCXC("cod_aplicacion_"+i,forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago())+"");
                }
            }

        }
        mundo.guardarAplicacionPagosCXC(con,forma.getMapaPagosCXC());
        forma.getMapaPagosCXC().clear();
        
        //----Se consultan los pagos realizados en la aplicación de pago con cuentas de cobro-------//
		forma.setMapaPagosCXC(mundo.consultarPagosCuentaCobro(con, Integer.parseInt(forma.getMapaDocumentos("aplicacion_actual_"+forma.getIndiceDocumentoPago())+"")));
        
        //this.accionConsultarPagosCuentaCobro(con,forma,mundo);
	}
	
	/**
	 * Método que realiza la búsqueda de las aplicaciones de pago de capitación pendientes
	 * para realizarles la aprobación
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param institucion
	 * @return
	 */
	private ActionForward accionBusquedaPagosParaAprobar(Connection con, ActionMapping mapping, PagosCapitacionForm forma, int institucion)
	{
		PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
		
		forma.setMapaApliPagos(mundoPagosCapitacion.busquedaPagosParaAprobar (con, forma.getTipoDocBusqueda(), forma.getDocumentoBusqueda(), forma.getConsecutivoPagoBusqueda(), forma.getConvenioBusqueda(), institucion));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("aprobacionPagos");
	}
	
	/**
	 * Método que realiza la aprobación de la aplicación del pago seleccionada
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAprobarAplicacionPago(Connection con, ActionMapping mapping, HttpServletRequest request, PagosCapitacionForm forma, UsuarioBasico usuario)
	{
		ActionErrors errores= new ActionErrors();
		PagosCapitacion mundo=new PagosCapitacion();
		
		//---------Se realizan las validaciones de la fecha de aprobación del pago ------------------//
		String fechaAprobacion=forma.getFechaAprobacionPago();
		String fechaAplicacion=forma.getMapaApliPagos("fecha_aplicacion_"+forma.getIndiceAprobacionPago())+"";
		boolean errorFecha=false;
		
		//----------Se valida la fecha de aplicación de pagos ------------//
		if (!UtilidadCadena.noEsVacio(fechaAprobacion))
			{
			errores.add("Fecha Aprobación vacio", new ActionMessage("errors.required","La Fecha de Aprobación"));
			errorFecha=true;
			}
		else if (!UtilidadFecha.validarFecha(fechaAprobacion))
			{
			errores.add("Fecha Aprobación Invalido", new ActionMessage("errors.formatoFechaInvalido", " de Aprobación"));
			errorFecha=true;
			}
		else
		{
		//---- Validar que la fecha de aprobación sea menor o igual a la fecha del sistema-----//
		if((UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fechaAprobacionMenor", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "de aprobación", "actual"));
			}
		//---- Validar que la fecha de aprobación sea mayor o igual a la fecha de aplicación del pago-----//
		if((UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaAplicacion))<0)
			{
				errores.add("fechaAplicacionMayor", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de aprobación", "de aplicación"));
			}
		}
		
		//------Si no hay error de la fecha de aprobación -------//
		if(!errorFecha)
		{
		/*
		 * Se valida que el mes/año del aprobación del pago sea mayor que el mes/año del 
		 * cierre saldo inicial de capitación,cuando exista este cierre
		 */
			String fechaAnioCierre[]=UtilidadValidacion.obtenerFechaMesCierreSaldoCapitacion(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCierreSaldoInicialStr);
			if(UtilidadCadena.noEsVacio(fechaAnioCierre[0]) && UtilidadCadena.noEsVacio(fechaAnioCierre[1]))
			{
				String fechaAproba[]=fechaAprobacion.split("/");
				if(Integer.parseInt(fechaAnioCierre[1])<10)
				{
					fechaAnioCierre[1]="0"+fechaAnioCierre[1];
				}

				if((fechaAnioCierre[0]+fechaAnioCierre[1]).compareTo((fechaAproba[2]+fechaAproba[1])) >= 0)
						{
							errores.add("fechaAprobacionMenorCierre", new ActionMessage("error.capitacion.pagoCapitacion.fechaAprobacionMenorFechaAnioCierre"));
						}
				
			}//if no esVació fechaAnioCierre
		}//if errorFecha
			
		
		//-------Se obtiene el nuevo saldo del documento ----------------------//
		float valorInicialDocumento=0;
		int codigoPago=0;
		int codigoAplicacion=0;
		
		if(UtilidadCadena.noEsVacio(forma.getMapaApliPagos("valor_inicial_documento_"+forma.getIndiceAprobacionPago())+""))
		{
			valorInicialDocumento=Float.parseFloat(forma.getMapaApliPagos("valor_inicial_documento_"+forma.getIndiceAprobacionPago())+"");
		}
		if(UtilidadCadena.noEsVacio(forma.getMapaApliPagos("codigo_pago_"+forma.getIndiceAprobacionPago())+""))
		{
			codigoPago=Integer.parseInt(forma.getMapaApliPagos("codigo_pago_"+forma.getIndiceAprobacionPago())+"");
		}
		if(UtilidadCadena.noEsVacio(forma.getMapaApliPagos("codigo_aplicacion_pago_"+forma.getIndiceAprobacionPago())+""))
		{
			codigoAplicacion=Integer.parseInt(forma.getMapaApliPagos("codigo_aplicacion_pago_"+forma.getIndiceAprobacionPago())+"");
		}
		
		//--------Se calcula el nuevo saldo del documento ---------------//
		double nuevoSaldoDocumento=mundo.calcularNuevoSaldoDocumento (con, valorInicialDocumento, codigoPago, codigoAplicacion);
		
		
		if(nuevoSaldoDocumento<0)
			{
			errores.add("nuevoSaldoMenorCero", new ActionMessage("error.capitacion.pagoCapitacion.nuevoSaldoDocumentoMenorCero", nuevoSaldoDocumento));
			}
		
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("aprobacionPagos");
		}
		else
		{
			
			//--------Se cargan el detalle de pagos por CxC de la aplicación -------------------//
			forma.setMapaDetallePagos(mundo.consultarPagosCxCAplicacion (con, codigoAplicacion));
			
			HashMap mapa=(HashMap)forma.getMapaDetallePagos().clone();
			
			mapa.put("codigo_aplicacion", forma.getCodigoAplicacionAprobar());
			mapa.put("usuario", usuario.getLoginUsuario());
			mapa.put("fecha_aprobacion", forma.getFechaAprobacionPago());
			
			
			
			//-------Se realiza el proceso de aprobación de la aplicación de pago --------------//
			mundo.guardarAprobacionAplicacionPago(con, mapa);
			
			//--------Se cambia el estado para que se abra el popup para imprimir aprobación --------//
			forma.setEstado("abrirImprimirAprobacion");
			
	        //-------Se cambia el estado para que no se abra la página de imprimir nuevamente -----------//
	        //forma.setEstadoAnterior("imprimirAprobacionPago");
	        
			
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("aprobacionPagos");
		}
	}
	
	/**
	 * Método que realiza la búsqueda de los pagos de acuerdo a los parámetros
	 * de búsqueda de la consulta
	 * para realizarles la aprobación
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param institucion
	 * @return
	 */
	private ActionForward accionBusquedaPagosConsulta(Connection con, ActionMapping mapping, PagosCapitacionForm forma, int institucion)
	{
		PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
		
		forma.setMapaConsultaPagos(mundoPagosCapitacion.busquedaPagosConsulta (con, forma.getTipoDocBusqueda(), forma.getDocumentoBusqueda(), forma.getConsecutivoPagoBusqueda(), forma.getEstadoPagoBusqueda() ,forma.getConvenioBusqueda(), institucion));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPagos");
	}
	
	/**
	 * Metodo para Imprimir la aprobación de un pago de capitación   
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirAprobacionPago(Connection con, ActionMapping mapping, PagosCapitacionForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Aprobacion_Pago_Capitacion_" + r.nextInt()  +".pdf";
   
        PagosCapitacionPdf.pdfAprobacionPago(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request);
        
        //-------Se cambia el estado para que no se abra la página de imprimir nuevamente -----------//
        forma.setEstadoAnterior("imprimirAprobacionPago");
        
                
        UtilidadBD.closeConnection(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Aprobación Pago Capitación");
	    return mapping.findForward("abrirPdf");	      
	  }
	
	/**
	 * Metodo para Imprimir la consulta de aplicación de pago   
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirConsultaAplicacionPago(Connection con, ActionMapping mapping, PagosCapitacionForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Consulta_Aplicacion_Pago_" + r.nextInt()  +".pdf";
        
        PagosCapitacion mundoPagosCapitacion=new PagosCapitacion();
        
        int codigoAplicacion=0;
        
        if(UtilidadCadena.noEsVacio(forma.getMapaConsultaPagos("codigo_aplicacion_pago_"+forma.getIndiceImprimirPago())+""))
        {
        	codigoAplicacion=Integer.parseInt(forma.getMapaConsultaPagos("codigo_aplicacion_pago_"+forma.getIndiceImprimirPago())+"");
        }
        
        //---------Se consulta el detalle de pagos CxC de la aplicación para mostrar en la impresión ----------//
        forma.setMapaDetallePagos(mundoPagosCapitacion.consultarPagosCxCAplicacion (con, codigoAplicacion));
   
        PagosCapitacionPdf.pdfConsultaAplicacionPago(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request);	
        
        UtilidadBD.closeConnection(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Aprobación Pago Capitación");
	    return mapping.findForward("abrirPdf");	      
	  }
	
}
