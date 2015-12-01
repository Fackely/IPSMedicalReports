package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturasVarias.PagosFacturasVariasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.PagosFacturasVarias;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class PagosFacturasVariasAction extends Action 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PagosFacturasVariasAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof PagosFacturasVariasForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PagosFacturasVariasForm forma = (PagosFacturasVariasForm) form;
				PagosFacturasVarias mundo = new PagosFacturasVarias();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				logger.warn("[PagosFacturasVariasAction]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					this.accionConsultarPagosGeneralFacturasVarias(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordenar"))
				{
					this.ordenarListado(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("conceptos"))
				{
					this.accionCargarConceptosPagosFacturasVarias(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("conceptos");
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					this.accionBusquedaAvanzada(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					//Se hace la redirección a la primera página
					forma.setLinkSiguiente("../pagosFacturasVarias/pagosFacturasVarias.jsp?pager.offset=0");
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevoConcepto"))
				{
					this.accionAdicionarNuevoConcepto(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("conceptos");
				}
				else if(estado.equals("guardarAplicacionConceptos"))
				{
					return this.accionAplicacionConceptos(con, forma, mundo, usuario, request, mapping);
				}
				else if(estado.equals("eliminarConcepto"))
				{
					this.accionEliminarConceptos(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("conceptos");
				}
				else if(estado.equals("volverConceptos"))
				{
					this.accionCargarConceptosPagosFacturasVarias(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("conceptos");
				}
				else if(estado.equals("volverAplicacion"))
				{
					this.accionConsultarPagosGeneralFacturasVarias(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					if(!forma.getLinkSiguiente().equals(""))
					{
						response.sendRedirect(forma.getLinkSiguiente());
						return null;//mapping.findForward("principal");
					}
					else
					{
						return mapping.findForward("principal");
					}

				}
				else if(estado.equals("busquedaFacturas"))
				{
					forma.setFacturaBusquedaAvanzadaFacturasVarias("");
					this.accionBuscarFacturas(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detallePago");
				}
				else if(estado.equals("ordenarMapaBusFacturas"))
				{
					this.accionOrdenarMapaFacturas(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("popUpBusFacturas");
				}
				else if(estado.equals("busquedaAvanzadaFactura"))
				{
					this.accionBusquedaAvanzadaFacturas(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("popUpBusFacturas");
				}
				else if(estado.equals("adicionarFactura"))
				{
					this.accionAdicionarFactura(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("popUpBusFacturas");
				}
				else if(estado.equals("recargar"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detallePago");
				}
				else if(estado.equals("eliminarFactura"))
				{
					this.accionEliminarFactura(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detallePago");
				}
				else if(estado.equals("guardarAplicacionFacturasCasoFacturas"))
				{
					this.accionGuardarAplicacionFacturasCasoFacturas(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de PagosFacturasVariasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Método que valida la información de los conceptos
	 * validando si se guardará alguna o no?
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward accionAplicacionConceptos(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		//Guardar Aplicacion Conceptos
		int numeroRegistros = 0;
        ActionErrors errores = new ActionErrors();
        Utilidades.imprimirMapa(forma.getConceptosAplicacionPagosFacturasVarias());
        for(int i=0; i<Integer.parseInt(forma.getConceptosAplicacionPagosFacturasVarias("numRegistros")+""); i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getConceptosAplicacionPagosFacturasVarias("eliminado_"+i)+""))
        	    numeroRegistros++;
        }
		if(numeroRegistros>0 || UtilidadTexto.getBoolean(forma.getDocumentosPagos("modificacion_"+forma.getIndexPagoFacturasVarias())+""))
        {
        	//Si se va a guardar algún concepto validar el campo de la fecha de aprobación
        	if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaAplicacionFacturasVarias()+"", UtilidadFecha.getFechaActual()+""))
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "de Aplicación "+forma.getFechaAplicacionFacturasVarias(), "Sistema "+UtilidadFecha.getFechaActual()));
    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getDocumentosPagos("fechadocumento_"+forma.getIndexPagoFacturasVarias())+""), forma.getFechaAplicacionFacturasVarias()+""))
    			errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Aplicación "+forma.getFechaAplicacionFacturasVarias(), "del Documento "+UtilidadFecha.conversionFormatoFechaAAp(forma.getDocumentosPagos("fechadocumento_"+forma.getIndexPagoFacturasVarias())+"")));
    		/*
    		 * esta repetida la validacion
    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getDocumentosPagos("fechadocumento_"+forma.getIndexPagoFacturasVarias())+""), forma.getFechaAplicacionFacturasVarias()+""))
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "de Aplicación "+forma.getFechaAplicacionFacturasVarias(), "del Documento "+UtilidadFecha.conversionFormatoFechaAAp(forma.getDocumentosPagos("fechadocumento_"+forma.getIndexPagoFacturasVarias())+"")));
    		*/
    		if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				return mapping.findForward("conceptos");
			}
        	else
        	{
        		this.accionGuardarConceptos(con, forma, mundo, usuario, true);
        		return mapping.findForward("detallePago");
        	}
        }
        //Sino se guardo ningún concepto
        return mapping.findForward("detallePago");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarPagosGeneralFacturasVarias(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario)
    {
		forma.setDocumentosPagos(mundo.consultarPagosGeneralFacturasVarias(con, usuario.getCodigoInstitucionInt()));
    }
	
	/**
	 * @param forma
	 */
	private void ordenarListado(PagosFacturasVariasForm forma)
	{
		String[] indices={
                		"codigo_",
                		"codigodeudor_",
                		"nombredeudor_",
                		"identificaciondeudor_",
                		"codigotipo_",
                		"destipo_",
                		"acronimotipo_",
                		"documento_",
                		"codigoestado_",
                		"desestado_",
                		"valordocumento_",
                		"fechadocumento_",
                		"aplicacionactual_",
                		"numeroaplicacion_",
                		"modificacion_",
                		"valaplicado_",
                		"valconceppagoactual_",
                		"valfacactual_",
                		"valporaplicar_",
                		"observaciones_"
                		};
		int numReg=Integer.parseInt(forma.getDocumentosPagos("numRegistros")+"");
		forma.setDocumentosPagos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getDocumentosPagos(), numReg));
		forma.setDocumentosPagos("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }
	
	/**
	 * @param forma
	 */
	private void accionOrdenarMapaFacturas(PagosFacturasVariasForm forma)
    {
        String[] indices={
        		"codigofactura_",
        		"consecutivofactura_",
        		"fecha_",
        		"saldo_",
        		};
		int numReg=Integer.parseInt(forma.getMapaBusFacturas("numRegistros")+"");
		forma.setMapaBusFacturas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaBusFacturas(),numReg));
		forma.setMapaBusFacturas("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionCargarConceptosPagosFacturasVarias(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario)
    {
		forma.setConceptosAplicacionPagosFacturasVarias(mundo.cargarConceptosPagosFacturasVarias(con,Integer.parseInt(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPagoFacturasVarias())+"")));
		forma.setObservacionesFacturasVarias(forma.getDocumentosPagos("observaciones_"+forma.getIndexPagoFacturasVarias())+"");
		//forma.setConceptosPagos(Utilidades.obtenerConceptosPagos(con, usuario.getCodigoInstitucionInt()));
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionBusquedaAvanzada(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario)
    {
		HashMap vo = new HashMap();
		vo.put("tipo", forma.getTipoDocBusquedaFacturasVarias()+"");
		vo.put("documento", forma.getDocumentoBusquedaFacturasVarias()+"");
		vo.put("fecha", forma.getFechaDocBusquedaFacturasVarias()+"");
		vo.put("deudor", forma.getDeudorBusquedaFacturasVarias()+"");
		vo.put("institucion", usuario.getCodigoInstitucionInt()+"");
		forma.setDocumentosPagos(mundo.busquedaAvanzada(con,vo));
    }
	
	/**
	 * @param forma
	 */
	private void accionAdicionarNuevoConcepto(PagosFacturasVariasForm forma)
    {
        int pos = Integer.parseInt(forma.getConceptosAplicacionPagosFacturasVarias("numRegistros")+"");
        forma.setConceptosAplicacionPagosFacturasVarias("selConceptos_"+pos,"-1");
        forma.setConceptosAplicacionPagosFacturasVarias("codigoconcepto_"+pos,"-1");
        forma.setConceptosAplicacionPagosFacturasVarias("desconcepto_"+pos,"");
        forma.setConceptosAplicacionPagosFacturasVarias("tipoconcepto_"+pos,"-1");
        forma.setConceptosAplicacionPagosFacturasVarias("valorbase_"+pos, forma.getDocumentosPagos("valporaplicar_"+forma.getIndexPagoFacturasVarias()));
        forma.setConceptosAplicacionPagosFacturasVarias("porcentaje_"+pos,"0");
        forma.setConceptosAplicacionPagosFacturasVarias("valorconcepto_"+pos,"0");
        forma.setConceptosAplicacionPagosFacturasVarias("numRegistros",(pos+1)+"");
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esConsulta 
	 */
	private String accionGuardarConceptos(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario, boolean esConsulta)
    {
		String codigoAplicacion = "";
        HashMap mapa = (HashMap)forma.getConceptosAplicacionPagosFacturasVarias().clone();
        mapa.put("fechaaplicacion", forma.getFechaAplicacionFacturasVarias());
        mapa.put("observaciones", forma.getObservacionesFacturasVarias());
        mapa.put("estadoaplicacion", ConstantesBD.codigoEstadoAplicacionPagosPendiente+"");
        mapa.put("estadopago", ConstantesBD.codigoEstadoPagosAplicado+"");
        mapa.put("numeroaplicacion", forma.getDocumentosPagos("numeroaplicacion_"+forma.getIndexPagoFacturasVarias()));
        mapa.put("pagogeneraempresa", forma.getDocumentosPagos("codigo_"+forma.getIndexPagoFacturasVarias()));
        mapa.put("modificacion", forma.getDocumentosPagos("modificacion_"+forma.getIndexPagoFacturasVarias()));
        mapa.put("codigoaplicacion", forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPagoFacturasVarias()));
        mapa.put("institucion", forma.getInstitucionFacturasVarias()+"");
        mapa.put("usuario", usuario.getLoginUsuario());
        mapa.put("fechagrabacion", UtilidadFecha.getFechaActual());
        mapa.put("horagrabacion", UtilidadFecha.getHoraActual());
        codigoAplicacion = mundo.guardarConceptosAplicacionPagosFacturasVarias(con, mapa);
        forma.setConceptosAplicacionPagosFacturasVarias("actualizado", "true");
        //this.accionConsultarPagosGeneralFacturasVarias(con, forma, mundo, usuario);
        forma.setGuardadoEncabezado(true);
        
        this.accionConsultarPagosFacturasGlobal(con, forma, mundo, codigoAplicacion, esConsulta);
        return codigoAplicacion;
    }

	/**
	 * @param forma
	 */
	private void accionEliminarConceptos(PagosFacturasVariasForm forma)
    {
        forma.setConceptosAplicacionPagosFacturasVarias("eliminado_"+forma.getConEliminarFacturasVarias(),"true");
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionBuscarFacturas(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo)
    {
        HashMap mapa = new HashMap();
        mapa.put("deudor", forma.getDocumentosPagos("codigodeudor_"+forma.getIndexPagoFacturasVarias()));
        mapa.put("institucion", forma.getInstitucionFacturasVarias()+"");
        String facturas="'-1'";
        for(int i=0;i<Integer.parseInt(forma.getPagosFacturaFacturasVarias("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getPagosFacturaFacturasVarias("eliminado_"+i)+""))
                facturas=facturas+",'"+forma.getPagosFacturaFacturasVarias("codigofactura_"+i)+"'";
        }
        mapa.put("facturas",facturas);
        forma.setMapaBusFacturas(mundo.buscarFacturasDeudor(con, mapa));
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionBusquedaAvanzadaFacturas(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo)
    {
        if(forma.getFacturaBusquedaAvanzadaFacturasVarias().trim().equals(""))
            this.accionBuscarFacturas(con, forma, mundo);
        else
        {
        	String facturas="'"+ConstantesBD.codigoNuncaValido+"'";
        	for(int i=0;i<Integer.parseInt(forma.getPagosFacturaFacturasVarias("numRegistros")+"");i++)
        	{
        		if(!UtilidadTexto.getBoolean(forma.getPagosFacturaFacturasVarias("eliminado_"+i)+""))
        			facturas=facturas+",'"+forma.getPagosFacturaFacturasVarias("codigofactura_"+i)+"'";
        	}
        	forma.setMapaBusFacturas(mundo.buscarFacturaLLave(con, forma.getInstitucionFacturasVarias(), Integer.parseInt(forma.getDocumentosPagos("codigodeudor_"+forma.getIndexPagoFacturasVarias())+""), forma.getFacturaBusquedaAvanzadaFacturasVarias(), facturas));
        }
    }
	
	/**
	 * @param forma
	 */
	private void accionAdicionarFactura(PagosFacturasVariasForm forma)
    {
       for(int i=0;i<Integer.parseInt(forma.getMapaBusFacturas("numRegistros")+"");i++)
       {
    	   if(UtilidadTexto.getBoolean(forma.getMapaBusFacturas("seleccionado_"+i)+""))
    	   {
    		   int numReg = Integer.parseInt(forma.getPagosFacturaFacturasVarias("numRegistros")+"");
    		   forma.setPagosFacturaFacturasVarias("codigofactura_"+numReg, forma.getMapaBusFacturas("codigofactura_"+i));
    		   forma.setPagosFacturaFacturasVarias("consecutivofactura_"+numReg, forma.getMapaBusFacturas("consecutivofactura_"+i));
    		   forma.setPagosFacturaFacturasVarias("codigocentroatencion_"+numReg, forma.getMapaBusFacturas("codigocentroatencion_"+i));
    		   forma.setPagosFacturaFacturasVarias("nombrecentroatencion_"+numReg, forma.getMapaBusFacturas("nombrecentroatencion_"+i));
    		   forma.setPagosFacturaFacturasVarias("codigodeudor_"+numReg, forma.getMapaBusFacturas("deudor_"+i));
    		   forma.setPagosFacturaFacturasVarias("institucion_"+numReg, forma.getInstitucionFacturasVarias()+"");
    		   forma.setPagosFacturaFacturasVarias("fecha_"+numReg, forma.getMapaBusFacturas("fecha_"+i));
    		   forma.setPagosFacturaFacturasVarias("fechaaprobacion_"+numReg, forma.getMapaBusFacturas("fechaaprobacion_"+i));
    		   forma.setPagosFacturaFacturasVarias("saldo_"+numReg, forma.getMapaBusFacturas("saldo_"+i));
    		   //Cargamos por defecto el Tipo Pago Total ---> Pedido en la Tarea 42418
    		   forma.setPagosFacturaFacturasVarias("tipoPago_"+numReg, ConstantesBD.acronimoTipoPagoTotal);
    		   forma.setPagosFacturaFacturasVarias("valpago_"+numReg, forma.getMapaBusFacturas("saldo_"+i));
    		   forma.setPagosFacturaFacturasVarias("bd_"+numReg, "false");
    		   forma.setPagosFacturaFacturasVarias("numRegistros", (numReg+1)+"");
    	   }
       }
       Utilidades.imprimirMapa(forma.getPagosFacturaFacturasVarias());
    }
	
	/**
	 * @param forma
	 */
	private void accionEliminarFactura(PagosFacturasVariasForm forma)
    {
         forma.setPagosFacturaFacturasVarias("eliminado_"+forma.getConEliminarFacturasVarias(),"true");
    }

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarAplicacionFacturasCasoFacturas(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario)
    {
        int numRegistros = 0, guardarAplicacion = 0;
        double valorTotal = 0, diferencia = 0;
       
        for(int i=0;i<Integer.parseInt(forma.getPagosFacturaFacturasVarias("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getPagosFacturaFacturasVarias("eliminado_"+i)+""))
            {
            	numRegistros++;
            }
        }
        
        logger.info("numRegistros a guardar: "+numRegistros);
        logger.info("¿debo guardar encabezado? "+forma.isGuardadoEncabezado());
        
        //************************REGISTRO DE LOS CONCEPTOS DEL PAGO**********************************************
        if(numRegistros>0 && !forma.isGuardadoEncabezado())
        {
        	forma.setPagosFacturaFacturasVarias("codaplicacion",this.accionGuardarConceptos(con, forma, mundo, usuario,false));
        	forma.setDocumentosPagos("aplicacionactual_"+forma.getIndexPagoFacturasVarias(),forma.getPagosFacturaFacturasVarias("codaplicacion"));
        }
        else
        {
        	forma.setPagosFacturaFacturasVarias("codaplicacion", forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPagoFacturasVarias()));
        }
        //************************REGISTRO DE LOS PAGOS******************************************************************************
        logger.info("Voy a realizar la apliacion del pago de la aplicacion: "+forma.getPagosFacturaFacturasVarias("codaplicacion"));
        logger.info(":::MAPA DE PAGOS FACTURAS VARIAS:::");
        Utilidades.imprimirMapa(forma.getPagosFacturaFacturasVarias());
        if(!UtilidadTexto.isEmpty(forma.getPagosFacturaFacturasVarias("codaplicacion").toString()))
        {
        	guardarAplicacion = mundo.guardarAplicacionFacturas(con, forma.getPagosFacturaFacturasVarias());
        }
        else
        {
        	guardarAplicacion = ConstantesBD.codigoNuncaValido;
        }
        
        
        
        
        //Consultamos los pagos de facturas aplicados para ser impresos en el resumen
        forma.getPagosFacturaFacturasVarias().clear();
        this.accionConsultarPagosFacturasResumen(con, forma, mundo, usuario);
        //Recorro los registros de facturas aplicadas para realizar el totalizado
        for(int i=0; i<Utilidades.convertirAEntero(forma.getPagosFacturaFacturasVarias("numRegistros")+""); i++)
        	valorTotal = valorTotal + Utilidades.convertirADouble(forma.getPagosFacturaFacturasVarias("valpago_"+i)+"");
        
        diferencia = Utilidades.convertirADouble(forma.getConceptosAplicacionPagosFacturasVarias("netoaplicar")+"") - valorTotal;
        
        forma.setPagosFacturaFacturasVarias("diferencia", diferencia);
        forma.setPagosFacturaFacturasVarias("valorTotal", valorTotal);
        //Verifico si se guardo la aplicación de pago para mostrar el mensaje adecuado
        if(guardarAplicacion != ConstantesBD.codigoNuncaValido)
        	forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN REALIZADO CON ÉXITO"));
        else
        	forma.setMensaje(new ResultadoBoolean(true, "NO SE GUARDO LA APLICACIÓN DE PAGOS"));
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigoAplicacion 
	 * @param esConsulta 
	 */
	private void accionConsultarPagosFacturasGlobal(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, String codigoAplicacion, boolean esConsulta)
    {
		if(!UtilidadTexto.isEmpty(codigoAplicacion))
		{
	        HashMap aplicacionPagos = mundo.consultarPagosFacturas(con,Integer.parseInt(codigoAplicacion));
	        if(Utilidades.convertirAEntero(aplicacionPagos.get("numRegistros")+"") > 0)
	        {
	        	//Guardamos la información original de las Facturas Aplicación de Facturas Varias seleccionado en la variable LOG en la variable de la forma
	    		String log = "\n       ====INFORMACIÓN ORIGINAL=====       ";
	    		for(int i=0; i<Utilidades.convertirAEntero(aplicacionPagos.get("numRegistros")+""); i++)
	    		{
	    			log += "\n*  Consecutivo Factura ["+aplicacionPagos.get("consecutivofactura_"+i)+"] "+
					 	   "\n*  Fecha Aprobación ["+aplicacionPagos.get("fechaaprobacion_"+i)+"] "+
					 	   "\n*  Saldo Factura ["+aplicacionPagos.get("saldo_"+i)+"] "+
					 	   "\n*  Valor Pago ["+aplicacionPagos.get("valpago_"+i)+"] ";
	    		}
	    		forma.setLog(log);
	        }
	        if(esConsulta)
	        {
	        	//********EVALUIACION DE LOS CAMPOS DE LA CONSULTA*********************+
	        	for(int i=0;i<Utilidades.convertirAEntero(aplicacionPagos.get("numRegistros")+""); i++)
	        	{
	        		logger.info("valpago_"+i+": "+aplicacionPagos.get("valpago_"+i));
	        		logger.info("saldo_"+i+": "+aplicacionPagos.get("saldo_"+i));
	        		if(Utilidades.convertirADouble(aplicacionPagos.get("valpago_"+i)+"", true)<Utilidades.convertirADouble(aplicacionPagos.get("saldo_"+i)+"", true))
	        		{
	        			aplicacionPagos.put("tipoPago_"+i, ConstantesBD.acronimoTipoPagoParcial);
	        		}
	        		else
	        		{
	        			aplicacionPagos.put("tipoPago_"+i, ConstantesBD.acronimoTipoPagoTotal);
	        		}
	        	}
	        	//***********************************************************************
	        	
	        	forma.setPagosFacturaFacturasVarias(aplicacionPagos);
	        }
	    }
		
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionConsultarPagosFacturasResumen(Connection con, PagosFacturasVariasForm forma, PagosFacturasVarias mundo, UsuarioBasico usuario)
    {
        forma.setPagosFacturaFacturasVarias(mundo.consultarPagosFacturas(con,Integer.parseInt(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPagoFacturasVarias())+"")));
        if(Utilidades.convertirAEntero(forma.getPagosFacturaFacturasVarias("numRegistros")+"") > 0)
        {
        	String log = forma.getLog() +
        			   "\n       ====INFORMACIÓN DESPUÉS DE LA MODIFICACIÓN=====       ";
        	for(int i=0; i<Utilidades.convertirAEntero(forma.getPagosFacturaFacturasVarias("numRegistros")+""); i++)
        	{
        		//Guardamos la información modificada de las Facturas de la Aplicación de Pagos de Facturas Varias
    			log += "\n*  Consecutivo Factura ["+forma.getPagosFacturaFacturasVarias("consecutivofactura_"+i)+"] " +
    				   "\n*  Fecha Aprobación ["+forma.getPagosFacturaFacturasVarias("fechaaprobacion_"+i)+"] " +
    				   "\n*  Saldo Factura ["+forma.getPagosFacturaFacturasVarias("saldo_"+i)+"] " +
    				   "\n*  Valor Pago ["+forma.getPagosFacturaFacturasVarias("valpago_"+i)+"] ";
        	}
        	log += "\n========================================================\n\n\n ";
        	LogsAxioma.enviarLog(ConstantesBD.logAplicacionPagosFacturasVariasCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
        	logger.info("===>Se creo el log correctamente");
        }
    }
	
}