/*
 * Created on 3/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * 
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.cartera.AplicacionPagosEmpresaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AplicacionPagosEmpresa;

/**
 * @version 1.0, 3/11/2005 
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class AplicacionPagosEmpresaAction extends Action
{

	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Servicios
	 */
	private static Logger logger = Logger.getLogger(AplicacionPagosEmpresaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
	{
		Connection con = null;
		try{
		if(form instanceof AplicacionPagosEmpresaForm)
		{
		    
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = util.UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			AplicacionPagosEmpresaForm forma=(AplicacionPagosEmpresaForm)form;
			AplicacionPagosEmpresa mundo= new AplicacionPagosEmpresa();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			String estado = forma.getEstado();
			logger.warn("[Aplicacion Pagos]-->Estado: "+estado);
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
			    this.accionConsultarPagosGeneralEmpresa(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("ordenar"))
			{
			    this.ordenarListadoPAgos(forma);
			    UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("busquedaAvanzada"))
			{
			    this.accionBusquedaAvanzada(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("conceptos"))
			{
			    forma.setPorCXC(false);
			    forma.setPorFacturas(false);
			    this.accionCargarConceptosPagos(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("conceptos");
			}
			else if(estado.equals("nuevoConcepto"))
			{
			    this.accionAdicionarNuevoConcepto(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("conceptos");
			}
			else if(estado.equals("guardarAplicacionConceptos"))
			{
			    this.accionValidarGuardarConceptos(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("eliminarConcepto"))
			{
			    this.accionEliminarConceptos(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("conceptos");
			}
			else if(estado.equals("anularAplicacion"))
			{
			    this.anularAplicacionPago(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("volverConceptos"))
			{
			    forma.setPorCXC(false);
			    forma.setPorFacturas(false);
			    this.accionCargarConceptosPagos(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("conceptos");
			}
			else if(estado.equals("porFacturas"))
			{
			    forma.setPorCXC(false);
			    forma.setPorFacturas(true);
			    this.accionConsultarPagosFacturasGlobal(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("busquedaFacturas"))
			{
			    forma.setBusquedaNivelFacturas3(false);
			    forma.setFacturaBusquedaAvanzada("");
			    this.accionBuscarFacturas(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("busquedaFacturasCXC"))
			{
			    forma.setBusquedaNivelFacturas3(true);
			    forma.setFacturaBusquedaAvanzada("");
			    this.accionBuscarFacturasCXC(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePagoFacturas");
			}
			else if(estado.equals("adicionarFactura"))
			{
			    this.accionAdicionarFactura(forma);
			    UtilidadBD.cerrarConexion(con);
			    return null;
			}
			else if(estado.equals("ordenarMapaBusFacturas"))
			{
			    this.accionOrdenarMapaFacturas(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("popUpBusFacturas");
			}
			else if (estado.equals("redireccion"))// estado para mantener los datos del pager
			{			    
			    UtilidadBD.cerrarConexion(con);
			    forma.getLinkSiguiente();
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}
			else if(estado.equals("ordenarFacturasDetalle"))
			{
				this.accionOrdenarMapaFacturasDetalle(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePagoFacturas");
			}
			else if(estado.equals("busquedaAvanzadaFactura"))
			{
			    this.accionBusquedaAvanzadaFacturas(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("popUpBusFacturas");
			}
			else if(estado.equals("eliminarFactura"))
			{
			    this.accionEliminarFactura(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("porCXC"))
			{
			    forma.setPorFacturas(false);
			    forma.setPorCXC(true);
			    this.accionConsultarPagosCuentaCobro(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("busquedaCXC"))
			{
			    forma.setCxcBusAvanzada("");
			    this.accionBuscarCuentasCobroConvenio(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("adicionarCXC"))
			{
			    this.accionAdicionarCXC(forma);
			    UtilidadBD.cerrarConexion(con);
			    return null;
			}
			else if(estado.equals("recargar"))
			{
			    UtilidadBD.cerrarConexion(con);
			    if(forma.isBusquedaNivelFacturas3())
			        return mapping.findForward("detallePagoFacturas");
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("ordenarMapaBusCXC"))
			{
			    this.accionOrdenarMapaCXC(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("popUpBusCXC");
			}
			else if(estado.equals("eliminarCXC"))
			{
			    this.accionEliminarCXC(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("busquedaAvanzadaCXC"))
			{
			    this.accionBusquedaAvanzadaCuentaCobro(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("popUpBusCXC");
			}
			else if(estado.equals("guardarAplicacionCXC"))
			{
			    this.accionGuardarAplicacionCXC(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
			}
			else if(estado.equals("detalleCXC"))
			{
			    this.accionConsultarPagosFacturas(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePagoFacturas");
			}
			else if(estado.equals("eliminarFacturaCXC"))
			{
			    this.accionEliminarFacturaCXC(forma);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePagoFacturas");
			}
			else if(estado.equals("guardarAplicacionFacturas"))
			{
			    this.accionGuardarAplicacionPagosFacturas(con,forma,mundo);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePagoFacturas");
			}
			else if(estado.equals("guardarAplicacionFacturasCasoFacturas"))
			{
			    this.accionGuardarAplicacionFacturasCasoFacturas(con,forma,mundo,usuario);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detallePago");
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
			logger.error("El form no es compatible");
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
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionValidarGuardarConceptos(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        int numeroRegistrios=0;
        for(int i=0;i<Utilidades.convertirAEntero(forma.getConceptosAplicacionPagos("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getConceptosAplicacionPagos("eliminado_"+i)+""))
            {
                numeroRegistrios++;
            }
        }
        if(numeroRegistrios>0||UtilidadTexto.getBoolean(forma.getDocumentosPagos("modificacion_"+forma.getIndexPago())+""))
            this.accionGuardarConceptos(con,forma,mundo,usuario);

    }



    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionBuscarFacturasCXC(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        HashMap mapa=new HashMap();
        mapa.put("convenio",forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago()));
        mapa.put("institucion", forma.getInstitucion()+"");
        mapa.put("cxc",forma.getPagosCXC("cxc_"+forma.getIndexCXC()));
        mapa.put("busxcxc","true");
        String facturas="-1";
        for(int i=0;i<Integer.parseInt(forma.getMapaFacturasCXC("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getMapaFacturasCXC("eliminado_"+i)+""))
                facturas=facturas+","+forma.getMapaFacturasCXC("codigofactura_"+i);
        }
        mapa.put("facturas",facturas);
        forma.setMapaBusFacturas(mundo.buscarFacturasConvenio(con,mapa));
    }

    /**
     * @param forma
     */
    private void accionEliminarFacturaCXC(AplicacionPagosEmpresaForm forma)
    {
        forma.setMapaFacturasCXC("eliminado_"+forma.getConEliminar(),"true");
    }

    /**
     * @param forma
     */
    private void accionEliminarCXC(AplicacionPagosEmpresaForm forma)
    {
        forma.setPagosCXC("eliminado_"+forma.getConEliminar(),"true");
    }

    /**
     * @param forma
     */
    private void accionEliminarFactura(AplicacionPagosEmpresaForm forma)
    {
         forma.setPagosFactura("eliminado_"+forma.getConEliminar(),"true");
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionGuardarAplicacionFacturasCasoFacturas(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        int numeroRegistrios=0;
        for(int i=0;i<Integer.parseInt(forma.getPagosFactura("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getPagosFactura("eliminado_"+i)+""))
            {
                numeroRegistrios++;
            }
        }
        if(numeroRegistrios>0&&!forma.isGuardadoEncabezado())
            this.accionGuardarConceptos(con, forma, mundo, usuario);
        Utilidades.imprimirMapa(forma.getDocumentosPagos());
        forma.setPagosFactura("codaplicacion",forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
        mundo.guardarAplicacionFacturasCasoFacturas(con,forma.getPagosFactura());
        forma.getPagosFactura().clear();
        this.accionConsultarPagosFacturasGlobal(con, forma, mundo); 
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionBusquedaAvanzadaFacturas(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        if(forma.getFacturaBusquedaAvanzada().trim().equals(""))
        {
            if(!forma.isBusquedaNivelFacturas3())
                this.accionBuscarFacturas(con,forma,mundo);
            else 
                this.accionBuscarFacturasCXC(con, forma, mundo);
        }
        else
        {
            if(!forma.isBusquedaNivelFacturas3())
            {
	            String facturas="-1";
	            for(int i=0;i<Integer.parseInt(forma.getPagosFactura("numRegistros")+"");i++)
	            {
	                if(!UtilidadTexto.getBoolean(forma.getPagosFactura("eliminado_"+i)+""))
	                    facturas=facturas+","+forma.getPagosFactura("codigofactura_"+i);
	            }
                forma.setMapaBusFacturas(mundo.buscarFacturaLLave(con,forma.getInstitucion(),Integer.parseInt(forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago())+""),forma.getFacturaBusquedaAvanzada(),facturas));
            }
            else
            {
                String facturas="-1";
	            for(int i=0;i<Integer.parseInt(forma.getMapaFacturasCXC("numRegistros")+"");i++)
	            {
	                if(!UtilidadTexto.getBoolean(forma.getMapaFacturasCXC("eliminado_"+i)+""))
	                    facturas=facturas+","+forma.getMapaFacturasCXC("codigofactura_"+i);
	            }
                forma.setMapaBusFacturas(mundo.buscarFacturaLLave(con,forma.getInstitucion(),Integer.parseInt(forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago())+""),Double.parseDouble(forma.getPagosCXC("cxc_"+forma.getIndexCXC())+""),forma.getFacturaBusquedaAvanzada(),facturas));
            }
        }
    }


    /**
     * @param forma
     */
    private void accionOrdenarMapaFacturas(AplicacionPagosEmpresaForm forma)
    {
        String[] indices={
        		"cxc_",
        		"codigofactura_",
        		"consecutivofactura_",
        		"fecha_",
        		"saldo_",
        		"facturasistema_",
        		"codigocentroatencion_",
        		"nombrecentroatencion_"
        		};
		int numReg=Integer.parseInt(forma.getMapaBusFacturas("numRegistros")+"");
		forma.setMapaBusFacturas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaBusFacturas(),numReg));
		forma.setMapaBusFacturas("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }


    /**
     * @param forma
     */
    private void accionOrdenarMapaFacturasDetalle(AplicacionPagosEmpresaForm forma)
    {
        String[] indices={
        		"cxc_",
        		"codigofactura_",
        		"consecutivofactura_",
        		"fecha_",
        		"saldo_",
        		"facturasistema_",
        		"codigocentroatencion_",
        		"nombrecentroatencion_",
        		"institucion_",
        		"valpago_",
        		"bd_"
        		};
		int numReg=Integer.parseInt(forma.getMapaFacturasCXC("numRegistros")+"");
		forma.setMapaFacturasCXC(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaFacturasCXC(),numReg));
		forma.setMapaFacturasCXC("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }



    /**
     * @param forma
     */
    private void accionAdicionarFactura(AplicacionPagosEmpresaForm forma)
    {
       for(int i=0;i<Integer.parseInt(forma.getMapaBusFacturas("numRegistros")+"");i++)
        {
            if(UtilidadTexto.getBoolean(forma.getMapaBusFacturas("seleccionado_"+i)+""))
            {
                if(!forma.isBusquedaNivelFacturas3())
                {
	                int numReg=Integer.parseInt(forma.getPagosFactura("numRegistros")+"");
	                forma.setPagosFactura("cxc_"+numReg, forma.getMapaBusFacturas("cxc_"+i));
	                forma.setPagosFactura("codigofactura_"+numReg, forma.getMapaBusFacturas("codigofactura_"+i));
	                forma.setPagosFactura("consecutivofactura_"+numReg, forma.getMapaBusFacturas("consecutivofactura_"+i));
	                forma.setPagosFactura("codigocentroatencion_"+numReg, forma.getMapaBusFacturas("codigocentroatencion_"+i));
	                forma.setPagosFactura("nombrecentroatencion_"+numReg, forma.getMapaBusFacturas("nombrecentroatencion_"+i));
	                forma.setPagosFactura("facturasistema_"+numReg,forma.getMapaBusFacturas("facturasistema_"+i));
	                forma.setPagosFactura("institucion_"+numReg, forma.getInstitucion()+"");
	                forma.setPagosFactura("fecha_"+numReg, forma.getMapaBusFacturas("fecha_"+i));
	                forma.setPagosFactura("saldo_"+numReg, forma.getMapaBusFacturas("saldo_"+i));
	                forma.setPagosFactura("valpago_"+numReg, "0");
	                forma.setPagosFactura("bd_"+numReg,"false");
	                forma.setPagosFactura("numRegistros", (numReg+1)+"");
	            }
                else
                {
                    int numReg=Integer.parseInt(forma.getMapaFacturasCXC("numRegistros")+"");
	                forma.setMapaFacturasCXC("cxc_"+numReg, forma.getMapaBusFacturas("cxc_"+i));
	                forma.setMapaFacturasCXC("codigofactura_"+numReg, forma.getMapaBusFacturas("codigofactura_"+i));
	                forma.setMapaFacturasCXC("consecutivofactura_"+numReg, forma.getMapaBusFacturas("consecutivofactura_"+i));
	                forma.setMapaFacturasCXC("codigocentroatencion_"+numReg, forma.getMapaBusFacturas("codigocentroatencion_"+i));
	                forma.setMapaFacturasCXC("nombrecentroatencion_"+numReg, forma.getMapaBusFacturas("nombrecentroatencion_"+i));
	                forma.setMapaFacturasCXC("institucion_"+numReg, forma.getInstitucion()+"");
	                forma.setMapaFacturasCXC("fecha_"+numReg, forma.getMapaBusFacturas("fecha_"+i));
	                forma.setMapaFacturasCXC("saldo_"+numReg, forma.getMapaBusFacturas("saldo_"+i));
	                forma.setMapaFacturasCXC("facturasistema_"+numReg,forma.getMapaBusFacturas("facturasistema_"+i));
	                forma.setMapaFacturasCXC("valpago_"+numReg, "0");
	                forma.setMapaFacturasCXC("bd_"+numReg,"false");
	                forma.setMapaFacturasCXC("numRegistros", (numReg+1)+"");
                }
	        }
        }
    }




    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionBuscarFacturas(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        HashMap mapa=new HashMap();
        mapa.put("convenio",forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago()));
        mapa.put("institucion", forma.getInstitucion()+"");
        String facturas="-1";
        for(int i=0;i<Integer.parseInt(forma.getPagosFactura("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getPagosFactura("eliminado_"+i)+""))
                facturas=facturas+","+forma.getPagosFactura("codigofactura_"+i);
        }
        mapa.put("facturas",facturas);
        forma.setMapaBusFacturas(mundo.buscarFacturasConvenio(con,mapa));
    }



    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionGuardarAplicacionPagosFacturas(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        forma.setMapaFacturasCXC("codaplicacion",forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
        forma.setMapaFacturasCXC("cxc",forma.getPagosCXC("cxc_"+forma.getIndexCXC()));
        mundo.guardarAplicacionPagosFacturas(con,forma.getMapaFacturasCXC());
        forma.getMapaFacturasCXC().clear();
        this.accionConsultarPagosFacturas(con, forma, mundo);
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionConsultarPagosFacturas(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        forma.setMapaFacturasCXC(mundo.consultarPagosFacturas(con,Integer.parseInt(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago())+""),Double.parseDouble(forma.getPagosCXC("cxc_"+forma.getIndexCXC())+"")));
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionConsultarPagosFacturasGlobal(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        forma.setPagosFactura(mundo.consultarPagosFacturas(con,Utilidades.convertirAEntero(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago())+"")));
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionGuardarAplicacionCXC(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        int numeroRegistrios=0;
        for(int i=0;i<Integer.parseInt(forma.getPagosCXC("numRegistros")+"");i++)
        {
            if(!UtilidadTexto.getBoolean(forma.getPagosCXC("eliminado_"+i)+""))
            {
                numeroRegistrios++;
            }
        }
        if(numeroRegistrios>0&&!forma.isGuardadoEncabezado())
        {
            this.accionGuardarConceptos(con, forma, mundo, usuario);
            for(int i=0;i<Integer.parseInt(forma.getPagosCXC("numRegistros")+"");i++)
            {
                if(!UtilidadTexto.getBoolean(forma.getPagosCXC("eliminado_"+i)+""))
                {
                    forma.setPagosCXC("codaplicacion_"+i,forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
                }
            }

        }
        mundo.guardarAplicacionCXC(con,forma.getPagosCXC());
        forma.getPagosCXC().clear();
        this.accionConsultarPagosCuentaCobro(con,forma,mundo);
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionBusquedaAvanzadaCuentaCobro(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
        if(forma.getCxcBusAvanzada().trim().equals(""))
        {
            this.accionBuscarCuentasCobroConvenio(con,forma,mundo);
        }
        else
        {
            String cxc="-1";
            for(int i=0;i<Integer.parseInt(forma.getPagosCXC("numRegistros")+"");i++)
            {
                if(!UtilidadTexto.getBoolean(forma.getPagosCXC("eliminado_"+i)+""))
                    cxc=cxc+","+forma.getPagosCXC("cxc_"+i);
            }
            forma.setMapaBusCXC(mundo.buscarCuentasCobroLLave(con,forma.getInstitucion(),Integer.parseInt(forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago())+""),forma.getCxcBusAvanzada(),cxc));
        }
    }


    /**
     * @param forma
     * @param mundo
     */
    private void accionOrdenarMapaCXC(AplicacionPagosEmpresaForm forma)
    {
        String[] indices={
        		"cxc_",
        		"fecharadicacion_",
        		"saldocxc_"
        		};
		int numReg=Integer.parseInt(forma.getMapaBusCXC("numRegistros")+"");
		forma.setMapaBusCXC(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaBusCXC(),numReg));
		forma.setMapaBusCXC("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }


    /**
     * @param forma
     */
    private void accionAdicionarCXC(AplicacionPagosEmpresaForm forma)
    {
        for(int i=0;i<Integer.parseInt(forma.getMapaBusCXC("numRegistros")+"");i++)
        {
            if(UtilidadTexto.getBoolean(forma.getMapaBusCXC("seleccionado_"+i)+""))
            {
                int numReg=Integer.parseInt(forma.getPagosCXC("numRegistros")+"");
                forma.setPagosCXC("codaplicacion_"+numReg, forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
                forma.setPagosCXC("cxc_"+numReg, forma.getMapaBusCXC("cxc_"+i));
                forma.setPagosCXC("fecharadicacion_"+numReg, forma.getMapaBusCXC("fecharadicacion_"+i));
                forma.setPagosCXC("saldocxc_"+numReg, forma.getMapaBusCXC("saldocxc_"+i));
                forma.setPagosCXC("institucion_"+numReg, forma.getInstitucion()+"");
                forma.setPagosCXC("codmetodopago_"+numReg, ConstantesBD.tipoMetodoPagoAutomatico+"");
                forma.setPagosCXC("valorpagofacturas_"+numReg, "0");
                forma.setPagosCXC("bd_"+numReg,"false");
                forma.setPagosCXC("numRegistros", (numReg+1)+"");
            }
        }
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionBuscarCuentasCobroConvenio(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
       HashMap mapa=new HashMap();
       mapa.put("convenio",forma.getDocumentosPagos("codigoconvenio_"+forma.getIndexPago()));
       mapa.put("institucion", forma.getInstitucion()+"");
       String cxc="-1";
       for(int i=0;i<Integer.parseInt(forma.getPagosCXC("numRegistros")+"");i++)
       {
           if(!UtilidadTexto.getBoolean(forma.getPagosCXC("eliminado_"+i)+""))
               cxc=cxc+","+forma.getPagosCXC("cxc_"+i);
       }
       mapa.put("cuentas",cxc);
       forma.setMapaBusCXC(mundo.buscarCuentasCobroConvenio(con,mapa));
    }


    /**
     * @param mundo
     * @param forma
     * @param con
     * 
     */
    private void accionConsultarPagosCuentaCobro(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
       forma.setPagosCXC(mundo.consultarPagosCuentaCobro(con,Utilidades.convertirAEntero(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago())+"")));
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void anularAplicacionPago(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        HashMap mapa=new HashMap();
        mapa.put("numeropago",forma.getDocumentosPagos("codigo_"+forma.getIndexPago()));
        mapa.put("codigoaplicacion",forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
        mapa.put("institucion",forma.getInstitucion()+"");
        mapa.put("loginusuario",usuario.getLoginUsuario());
        mapa.put("fecha", UtilidadFecha.getFechaActual());
        mapa.put("hora",UtilidadFecha.getHoraActual());
        mapa.put("motivo",forma.getMotivoAnulacion());
        mundo.anularAplicacionPago(con,mapa);
        this.accionConsultarPagosGeneralEmpresa(con, forma, mundo, usuario);
    }


    /**
     * @param forma
     */
    private void accionEliminarConceptos(AplicacionPagosEmpresaForm forma)
    {
        forma.setConceptosAplicacionPagos("eliminado_"+forma.getConEliminar(),"true");
    }


    /**
     * @param mundo
     * @param forma
     * @param con
     * @param usuario
     * 
     */
    private void accionGuardarConceptos(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        HashMap mapa=(HashMap)forma.getConceptosAplicacionPagos().clone();
        mapa.put("fechaaplicacion", forma.getFechaApliacion());
        mapa.put("observaciones",forma.getObservaciones());
        mapa.put("estadoaplicacion",ConstantesBD.codigoEstadoAplicacionPagosPendiente+"");
        mapa.put("estadopago",ConstantesBD.codigoEstadoPagosAplicado+"");
        mapa.put("numeroaplicacion",forma.getDocumentosPagos("numeroaplicacion_"+forma.getIndexPago()));
        mapa.put("pagogeneraempresa",forma.getDocumentosPagos("codigo_"+forma.getIndexPago()));
        mapa.put("modificacion",forma.getDocumentosPagos("modificacion_"+forma.getIndexPago()));
        mapa.put("codigoaplicacion",forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago()));
        mapa.put("institucion",forma.getInstitucion()+"");
        mapa.put("usuario",usuario.getLoginUsuario());
        mapa.put("fechagrabacion",UtilidadFecha.getFechaActual());
        mapa.put("horagrabacion",UtilidadFecha.getHoraActual());
        mundo.guardarConceptosAplicacionPagos(con,mapa);
        forma.setConceptosAplicacionPagos("actualizado", "true");
        this.accionConsultarPagosGeneralEmpresa(con,forma,mundo,usuario);
        forma.setGuardadoEncabezado(true);
    }


    /**
     * @param forma
     */
    private void accionAdicionarNuevoConcepto(AplicacionPagosEmpresaForm forma)
    {
        int pos=Utilidades.convertirAEntero(forma.getConceptosAplicacionPagos("numRegistros")+"");
        logger.info("VALOR DE POS------>"+pos);
        if (pos<0)
        	pos=0;
      
        
        forma.setConceptosAplicacionPagos("codigoconcepto_"+pos,"-1");
        forma.setConceptosAplicacionPagos("desconcepto_"+pos,"");
        forma.setConceptosAplicacionPagos("tipoconcepto_"+pos,"-1");
        forma.setConceptosAplicacionPagos("valorbase_"+pos,forma.getDocumentosPagos("valporaplicar_"+forma.getIndexPago()));
        forma.setConceptosAplicacionPagos("porcentaje_"+pos,"0");
        forma.setConceptosAplicacionPagos("valorconcepto_"+pos,"0");
       
       	forma.setConceptosAplicacionPagos("numRegistros",(pos+1)+"");
        
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionCargarConceptosPagos(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo)
    {
    	forma.setConceptosAplicacionPagos(mundo.cargarConceptosPagos(con,Utilidades.convertirAEntero(forma.getDocumentosPagos("aplicacionactual_"+forma.getIndexPago())+"")));
	    forma.setObservaciones(forma.getDocumentosPagos("observaciones_"+forma.getIndexPago())+"");
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionBusquedaAvanzada(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        HashMap vo=new HashMap();
        vo.put("tipo",forma.getTipoDocBusqueda()+"");
        vo.put("documento",forma.getDocumentoBusqueda()+"");
        vo.put("fecha",forma.getFechaDocBusqueda()+"");
        vo.put("convenio",forma.getConvenioBusqueda()+"");
        vo.put("institucion",usuario.getCodigoInstitucionInt()+"");
        forma.setDocumentosPagos(mundo.busquedaAvanzada(con,vo));
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionConsultarPagosGeneralEmpresa(Connection con, AplicacionPagosEmpresaForm forma, AplicacionPagosEmpresa mundo, UsuarioBasico usuario)
    {
        forma.setDocumentosPagos(mundo.consultarPagosGeneralEmpresa(con,usuario.getCodigoInstitucionInt()));
    }
    
    /**
     * Metodo para ordenar el HashMap, por la columna
     * que se elija en el formulario.
     * @param con Connection, conexión con la fuente de datos
     * @param conceptosForm ConceptosCarteraForm
     * @param mapping ActionMapping
     * @return findForward ActionForward
     */
    private void ordenarListadoPAgos(AplicacionPagosEmpresaForm forma) 
    {
        String[] indices={
                		"codigo_",
                		"codigoconvenio_",
                		"nombreconvenio_",
                		"codigotipo_",
                		"destipo_",
                		"acronimotipo_",
                		"consecutivodoc_",  
                		"documento_",
                		"codigoestado_",
                		"desestado_",
                		"valordocumento_",
                		"fechadocumento_",
                		"valaplicado_",
                		"valconceppagoactual_",
                		"aplicacionactual_",
                		"numeroaplicacion_",
                        "modificacion_",
                        "valporaplicar_"
                		};
       int numReg=Integer.parseInt(forma.getDocumentosPagos("numRegistros")+"");
       forma.setDocumentosPagos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getDocumentosPagos(),numReg));
       forma.setDocumentosPagos("numRegistros", numReg+"");
       forma.setUltimoPatron(forma.getPatronOrdenar());
    }
	
}
