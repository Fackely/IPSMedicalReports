package com.princetonsa.action.cartera;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.cartera.AjustesEmpresaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AjustesEmpresa;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;
  
/** 
 * 
 * CONTROLADOR DE LA FUNCIONALIDA
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez(artotor)</a>
 * 
 */
public class AjustesEmpresaAction extends Action
{

	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Servicios
	 */
	private Logger logger = Logger.getLogger(AjustesEmpresaAction.class); 
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
		if(form instanceof AjustesEmpresaForm)
	    {	        
		    
		   
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con);
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			AjustesEmpresaForm ajustesForm=(AjustesEmpresaForm) form;
			AjustesEmpresa mundo=new AjustesEmpresa();
			HttpSession sesion = request.getSession();
			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			
			String estado = ajustesForm.getEstado();
			logger.warn("[AjustesEmpresaActrion]-->Estado: "+estado);
			ajustesForm.setMostrarPopUpFacturasMismoConsecutivo(false);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo estado is NULL");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				return accionEmpezar(con,ajustesForm,mundo,usuario,request,mapping);
			}
			else if(estado.equals("cargarAjuste"))
			{
				ajustesForm.setInformacionCargada(false);
				mundo.reset();
				return this.accionCargarAjuste(con,ajustesForm,mundo,mapping,request);
			}
			else if(estado.equals("cargarFactura"))
			{
				ajustesForm.resetBasico();
				mundo.reset();
				ajustesForm.setModificacion(false);
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
				{
					//falta el diseño de multiempresa.
					ajustesForm.setFacturasMismoConsecutivo(Utilidades.consultarFacturasMismoConsecutivo(con,ajustesForm.getFactura(),usuario.getCodigoInstitucionInt()));
					if(Utilidades.convertirAEntero(ajustesForm.getFacturasMismoConsecutivo("numRegistros")+"")>1)
					{
						ajustesForm.setMostrarPopUpFacturasMismoConsecutivo(true);
						this.cerrarConexion(con);
						return mapping.findForward("ingresarModificarAjustes");
					}
					else
					{
						ajustesForm.setCodigoFactura(Utilidades.convertirAEntero(ajustesForm.getFacturasMismoConsecutivo("codigo_0")+""));
						ajustesForm.setNombreEntidadFactura(ajustesForm.getFacturasMismoConsecutivo("entidad_0")+"");
						if(!ajustesForm.isCastigoCartera())
						{
							//no postular datos
							ajustesForm.setTipoAjusteSeccion3(ConstantesBD.codigoNuncaValido+"");
							ajustesForm.setConceptoAjuste(ConstantesBD.codigoNuncaValido+"");
							ajustesForm.setMetodoAjuste(ConstantesBD.codigoNuncaValido+"");
							ajustesForm.setValorAjusteStr("");
						}
						return accionCargarFacturas(con,ajustesForm,mundo,mapping,request);
					}
				}
				else
				{
					return accionCargarFacturaEspecifica(con,ajustesForm,mundo,usuario,mapping,request);
				}
			}
			else if(estado.equals("cargarFactura_1"))
			{
				ajustesForm.setCodigoFactura(Utilidades.convertirAEntero(ajustesForm.getFacturasMismoConsecutivo("codigo_"+ajustesForm.getIndiceConsecutivoCargar())+""));
				ajustesForm.setNombreEntidadFactura(ajustesForm.getFacturasMismoConsecutivo("entidad_"+ajustesForm.getIndiceConsecutivoCargar())+"");
				if(!ajustesForm.isCastigoCartera())
				{
					//no postular datos
					ajustesForm.setTipoAjusteSeccion3(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setConceptoAjuste(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setMetodoAjuste(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setValorAjusteStr("");
				}
				return accionCargarFacturas(con,ajustesForm,mundo,mapping,request);
				
			}
			else if(estado.equals("cargarCuentaCobro"))
			{
				ajustesForm.setAjustePorFactura(false);
				ajustesForm.setAjustePorCuentaCobro(true);
				ajustesForm.resetBasico();
				mundo.reset();
				ajustesForm.setModificacion(false);
				if(!ajustesForm.isCastigoCartera())
				{
					//no postular datos
					ajustesForm.setTipoAjusteSeccion3(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setConceptoAjuste(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setMetodoAjuste(ConstantesBD.codigoNuncaValido+"");
					ajustesForm.setValorAjusteStr("");
				}
				return accionCargarCuentCobro(con,ajustesForm,mundo,mapping);
				
			}
			else if(estado.equals("guardarAjusteGeneral"))
			{ 
				ajustesForm.setFacturasPopUp(new HashMap());
				ajustesForm.setNumeroFacturasPopUp(0);
				ActionForward forward=null;
				boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
				forward=this.accionGuardarAjusteGeneral(con,ajustesForm,mundo,usuario,mapping,enTransaccion,request);
				if(forward==null)
				{
					forward=mapping.findForward("paginaErroresActionErrors");
					UtilidadBD.abortarTransaccion(con);					
				}
				else
				{
					UtilidadBD.finalizarTransaccion(con);
				}
				UtilidadBD.cerrarConexion(con);
				return forward;
			}
			else if(estado.equals("guardarDetalle"))
			{
				ActionForward forward=null;
				boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
				forward=this.accionGuardarDetalle(con,ajustesForm,mundo,mapping,enTransaccion);
				if(forward==null)
				{
					forward=mapping.findForward("paginaErroresActionErrors");
					UtilidadBD.abortarTransaccion(con);					
				}
				else
				{
					ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
					UtilidadBD.finalizarTransaccion(con);
				}
				UtilidadBD.cerrarConexion(con);
				return forward;
			}
			else if(estado.equals("guardarAjustesFacturas"))
			{
				if(!ajustesForm.isConfirmacionModificacionAjusteFactura())
				{
					existenModificaciones(con,ajustesForm,mundo);
					if((ajustesForm.isExisteModifiacionMetodoAjusteAF()||ajustesForm.isExisteModifiacionValorAjusteAF()))
					{
						cerrarConexion(con);
						return mapping.findForward("ajustesFactura");
					}
				}
				ActionForward forward=null;
				boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
				forward=this.accionGuardarAjusteFactura(con,ajustesForm,mundo,mapping,enTransaccion);
				if(forward==null)
				{
					forward=mapping.findForward("paginaErroresActionErrors");
					UtilidadBD.abortarTransaccion(con);					
				}
				else
				{
					ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
					UtilidadBD.finalizarTransaccion(con);
				}
				UtilidadBD.cerrarConexion(con);
				return forward;
			}
			else if(estado.equals("adicionarFacturas"))
			{
				this.cargarFacturasCuentaCobroManual(ajustesForm,mundo);
				cerrarConexion(con);
				return mapping.findForward("ajustesFactura");
			}
			else if(estado.equals("detalleFactura"))
			{
				ajustesForm.setServiciosFacturasPopUp(new HashMap());
				ajustesForm.setNumeroserviciosFacturasPopUp(0);
				return this.accionDetalleFactura(con,ajustesForm,mundo,mapping);
			}
			else if(estado.equals("buscarFacturasPopUp"))
			{
				return this.accionBuscarFacturasPopUp(con,ajustesForm,mundo,mapping,false);
			}
			else if(estado.equals("buscarFacturasAvanzadaPopUp"))
			{
				return this.accionBuscarFacturasPopUp(con,ajustesForm,mundo,mapping,true);
			}			
			else if(estado.equals("cargarFacturasDesdePopUp"))
			{
				return this.accionCargarMapaFacturasSeleccionadas(con,ajustesForm,mapping);
			}
			else if(estado.equals("buscarServiciosArticulosPopUp"))
			{
				return this.accionBuscarServiciosArticulosPopUp(con,ajustesForm,mundo,mapping,false);
			}
			else if(estado.equals("buscarServiciosArticulosPopUpAvanzada"))
			{
				return this.accionBuscarServiciosArticulosPopUp(con,ajustesForm,mundo,mapping,true);
			}			
			else if(estado.equals("cargarServiciosArticulosDesdePopUp"))
			{
				return this.accionCargarMapaServiciosSeleccionados(con,ajustesForm,mapping);
				
			}
			else if(estado.equals("eliminarDetalleServArt"))
			{
				return this.accionEliminarServiciosSArticulo(con,ajustesForm,mapping);
			}
			else if(estado.equals("accionEliminarDisAsocios"))
			{
				return this.accionEliminarDisAsocios(con, ajustesForm, mapping);
			}
			else if(estado.equals("ordenarMapaFacturasPopUp"))
			{
            	String indices[]={
            			"codigofactura_",
            			"seleccionado_",
            			"consecutivofactura_",
            			"codigocentroatencion_",
            			"nombrecentroatencion_",
            			"saldofactura_",
            			"totalfactura_",
            			"facturasistema_",
            			"valorajuste_",
            			"concepto_",
            			"metodoajuste_"
            			};
            	ajustesForm.setFacturasPopUp(Listado.ordenarMapa(indices,ajustesForm.getIndice(),ajustesForm.getUltimoIndice(),ajustesForm.getFacturasPopUp(),ajustesForm.getNumeroFacturasPopUp()));
            	ajustesForm.setUltimoIndice(ajustesForm.getIndice());
            	cerrarConexion(con);
            	return mapping.findForward("buscarFacturasPopUp");
			}
			else if(estado.equals("ordenarMapaServiciosPopUp"))
			{
				String indices[]={
						"codigodetalle_",
						"seleccionado_",
						"solicitud_",
						"escirugia_",
						"codigoaxioma_",
						"codigoservart_",
						"nombreservart_",
						"esservicio_",
						"loginmedico_",
						"codigomedico_",
						"nombremedico_",
						"codigopool_",
						"nombrepool_",
						"saldo_",
						"valorajuste_",
						"valorajusteoriginal_",
						"valorajustepool_",
						"valorajusteinstitucion_",
						"concepto_"
						};
            	ajustesForm.setServiciosFacturasPopUp(Listado.ordenarMapa(indices,ajustesForm.getIndiceServicios(),ajustesForm.getUltimoIndiceServicios(),ajustesForm.getServiciosFacturasPopUp(),ajustesForm.getNumeroserviciosFacturasPopUp()));
            	ajustesForm.setUltimoIndiceServicios(ajustesForm.getIndiceServicios());
            	cerrarConexion(con);
            	return mapping.findForward("buscarServiciosArticulosPopUp");
			}
			else if(estado.equals("volverFacturas"))
			{
				cerrarConexion(con);
            	return mapping.findForward("ajustesFactura");
				
			}
			else if(estado.equals("volverAjusteGeneral"))
			{
				ajustesForm.setTipoAjuste(ajustesForm.getTipoAjusteSeccion3());
				ajustesForm.setNumeroAjusteStr(ajustesForm.getConsecutivoAjuste());
				mundo.reset();
				return this.accionCargarAjuste(con,ajustesForm,mundo,mapping,request);
			}
			else if(estado.equals("volverAjusteGeneral"))
			{
				mundo.reset();
				return this.accionCargarAjuste(con,ajustesForm,mundo,mapping,request);
			}
			else if(estado.equals("anularAjuste"))
			{
				ActionForward forward=null;
				forward=this.accionAnularAjuste(con,ajustesForm,mundo,mapping,usuario,request);
				UtilidadBD.cerrarConexion(con);
				return forward;
			}
            else if(estado.equals("detalleAsociosServicios"))
            {
                ajustesForm.setServiciosAsocios(new HashMap());
                ajustesForm.setServiciosAsocios("numRegistros","0");
                if(!mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
                {
                	if (ajustesForm.getServiciosFacturas("codigopk_"+ajustesForm.getIndex())!=null)
                	{
                	mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigopk_"+ajustesForm.getIndex())+""));
                	}
                	else
                	{
                		mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(0);
                   
                	}
                	mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
                    mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(ajustesForm.getCodigoFactura());
                    mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setDetFactSolicitud(Integer.parseInt(ajustesForm.getServiciosFacturas("codigodetalle_"+ajustesForm.getIndex())+""));
                    this.accionDetalleServiciosAsocios(con,ajustesForm,mundo);    
                }
                cerrarConexion(con);
                return mapping.findForward("asociosServicios");
            }
            else if(estado.equals("guardarDetalleAsocios"))
            {
				boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
				if(this.accionGuardarDetalleAsocios(con,ajustesForm,mundo,enTransaccion))
					UtilidadBD.finalizarTransaccion(con);
				else
					UtilidadBD.abortarTransaccion(con);
					
                UtilidadBD.cerrarConexion(con);
                cerrarConexion(con);
                return mapping.findForward("asociosServicios");
            }
            else if(estado.equals("volverDetalleServicios"))
            {
                ajustesForm.setServiciosFacturasPopUp(new HashMap());
                ajustesForm.setNumeroserviciosFacturasPopUp(0);
               // return this.accionDetalleFactura(con,ajustesForm,mundo,mapping);
                this.cerrarConexion(con);
                return mapping.findForward("detalleFactura");
            }
            else if(estado.equals("buscarServiciosCirugiaPopUp"))
            {
                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setDetFactSolicitud(Integer.parseInt(ajustesForm.getServiciosFacturas("codigodetalle_"+ajustesForm.getIndex())+""));
                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(ajustesForm.getCodigoFactura());
                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setInstitucion(ajustesForm.getInstitucion());
                if(ajustesForm.getServiciosFacturas("codigopk_"+ajustesForm.getIndex())==null)
                {
                	  mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(0);
                 }
                else
                {  mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(Integer.parseInt(ajustesForm.getServiciosFacturas("codigopk_"+ajustesForm.getIndex())+""));
                }
                ajustesForm.setServiciosAsociosPopUp(new HashMap());
                ajustesForm.setServiciosAsociosPopUp("numRegistros","0");
                ajustesForm.setServiciosAsociosPopUp((HashMap)mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarAsociosServiciosCirugia(con).clone());
                cerrarConexion(con);
                return mapping.findForward("buscarServiciosCirugiaPopUp");
            }
            else if(estado.equals("cargarServiciosCirugiaDesdePopUp"))
            {
                this.accionCargarMapaServiciosCirugiaSeleccionados(ajustesForm);
                cerrarConexion(con);
                return mapping.findForward("asociosServicios");
            }
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				cerrarConexion(con);
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
	 * 
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param usuario 
	 * @param mapping
	 * @param request 
	 * @return
	 */
    private ActionForward accionCargarFacturaEspecifica(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
    {
    	ajustesForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(ajustesForm.getFactura(),usuario.getCodigoInstitucionInt()));
		if(!ajustesForm.isCastigoCartera())
		{
			//no postular datos
			ajustesForm.setTipoAjusteSeccion3(ConstantesBD.codigoNuncaValido+"");
			ajustesForm.setConceptoAjuste(ConstantesBD.codigoNuncaValido+"");
			ajustesForm.setMetodoAjuste(ConstantesBD.codigoNuncaValido+"");
			ajustesForm.setValorAjusteStr("");
		}
		return accionCargarFacturas(con,ajustesForm,mundo,mapping,request);
	}

	/**
     * 
     * @param con
     * @param ajustesForm
     * @param mundo
     */
    private void accionCargarMapaServiciosCirugiaSeleccionados(AjustesEmpresaForm ajustesForm)
    {
        int index=Utilidades.convertirAEntero(ajustesForm.getServiciosAsocios("numRegistros").toString()); 
        
        for(int i=0;i<Integer.parseInt(ajustesForm.getServiciosAsociosPopUp("numRegistros").toString());i++)
        {
            if(UtilidadTexto.getBoolean(ajustesForm.getServiciosAsociosPopUp("seleccionado_"+i)+""))
            {

                ajustesForm.setServiciosAsocios("consecitivoasodetfac_"+index,ajustesForm.getServiciosAsociosPopUp("consecitivoasodetfac_"+i));
                ajustesForm.setServiciosAsocios("codigodetfactura_"+index,ajustesForm.getServiciosAsociosPopUp("codigodetfactura_"+i));
                ajustesForm.setServiciosAsocios("codigoservicio_"+index,ajustesForm.getServiciosAsociosPopUp("codigoservicio_"+i));
                ajustesForm.setServiciosAsocios("codigoaxioma_"+index,ajustesForm.getServiciosAsociosPopUp("codigoaxioma_"+i));
                ajustesForm.setServiciosAsocios("acronimoasocio_"+index,ajustesForm.getServiciosAsociosPopUp("acronimoasocio_"+i));
                ajustesForm.setServiciosAsocios("nombreasocio_"+index,ajustesForm.getServiciosAsociosPopUp("nombreasocio_"+i));
                ajustesForm.setServiciosAsocios("nombreservicio_"+index,ajustesForm.getServiciosAsociosPopUp("nombreservicio_"+i));
                ajustesForm.setServiciosAsocios("codigomedico_"+index,ajustesForm.getServiciosAsociosPopUp("codigomedico_"+i));
                ajustesForm.setServiciosAsocios("nombremedico_"+index,ajustesForm.getServiciosAsociosPopUp("nombremedico_"+i));
                ajustesForm.setServiciosAsocios("loginmedico_"+index,ajustesForm.getServiciosAsociosPopUp("loginmedico_"+i));
                ajustesForm.setServiciosAsocios("codigopool_"+index,ajustesForm.getServiciosAsociosPopUp("codigopool_"+i));
                ajustesForm.setServiciosAsocios("descpool_"+index,ajustesForm.getServiciosAsociosPopUp("nombrepool_"+i));
                ajustesForm.setServiciosAsocios("saldo_"+index,ajustesForm.getServiciosAsociosPopUp("saldo_"+i));
                ajustesForm.setServiciosAsocios("eshonorarios_"+index,ajustesForm.getServiciosAsociosPopUp("eshonorarios_"+i));
                ajustesForm.setServiciosAsocios("porcentajepool_"+index,ajustesForm.getServiciosAsociosPopUp("porcentajepool_"+i));
                ajustesForm.setServiciosAsocios("porcentajemedico_"+index,ajustesForm.getServiciosAsociosPopUp("porcentajemedico_"+i));
                ajustesForm.setServiciosAsocios("valorajuste_"+index,ajustesForm.getServiciosAsociosPopUp("valorajuste_"+i)==null?"0":ajustesForm.getServiciosAsociosPopUp("valorajuste_"+i));
                ajustesForm.setServiciosAsocios("concepto_"+index,ajustesForm.getServiciosFacturas("concepto_"+ajustesForm.getIndex()));
                //verificar si el asocio ya existe, si existe, el concepto debe estar en blanco.
				for(int a=0;a<index;a++)
				{
					if(
							(ajustesForm.getServiciosAsociosPopUp("codigoaxioma_"+i)+"").equals(ajustesForm.getServiciosAsocios("codigoaxioma_"+a)+"")
					)
					{
						logger.info("**************EEENNNNTTTTRRRREEEE*************");
						a=index;
						ajustesForm.setServiciosAsocios	("concepto_"+index,"0");
					}
				}
                index++;
            }
        }
        ajustesForm.setServiciosAsocios("numRegistros",index+"");
    }

    /**
     * 
     * @param con
     * @param ajustesForm
     * @param mundo
     * @param enTransaccion 
     */
	private boolean accionGuardarDetalleAsocios(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, boolean enTransaccion)
    {
		mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(Integer.parseInt(ajustesForm.getServiciosFacturas("codigopk_"+ajustesForm.getIndex())+""));
        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(ajustesForm.getCodigoFactura());
        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setDetFactSolicitud(Integer.parseInt(ajustesForm.getServiciosFacturas("codigodetalle_"+ajustesForm.getIndex())+""));
        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicioAsocios(con);
        ajustesForm.setServiciosAsocios("codigoajuste",ajustesForm.getCodigo()+"");
        ajustesForm.setServiciosAsocios("factura",ajustesForm.getCodigoFactura()+"");
        ajustesForm.setServiciosAsocios("institucion",ajustesForm.getInstitucion()+"");
        ajustesForm.setServiciosAsocios("codigopkserart",mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getCodigopk());
        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setAsociosServicio(ajustesForm.getServiciosAsocios());
        enTransaccion=calcularDistribucionAutoPorcentualNivelAsociosCx(mundo,false);
        enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAsociosServicio(con);
        enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateValAjusteInstPoolSerCx(con);
        ajustesForm.setServiciosAsocios(new HashMap());
        ajustesForm.setServiciosAsocios("numRegistros","0");
        this.accionDetalleServiciosAsocios(con,ajustesForm,mundo);
        return enTransaccion;
    }

    /**
     * 
     * @param con
     * @param ajustesForm
     * @param mundo
	 */
	private void accionDetalleServiciosAsocios(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo)
    {
        ajustesForm.setServiciosAsocios(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().consultarServiciosAsociosAjustes(con));
    }


    /**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @param usuario
     * @param request 
     * @param enTransaccion 
	 * @return
	 */
	private ActionForward accionAnularAjuste(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		ArrayList filtro = new ArrayList();
		filtro.add(ajustesForm.getCodigo()+"");
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteGeneralDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteFacturaDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteDetFacturaDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteAsocioDetFacturaDeterminado,filtro);
		String[] estadoAjuste=Utilidades.obtenerestadoAjuste(ajustesForm.getCodigo()).split(ConstantesBD.separadorSplit);
		if(Integer.parseInt(estadoAjuste[0])==ConstantesBD.codigoEstadoCarteraAnulado)
		{
			ActionErrors errores=new ActionErrors();
			errores.add("AJUSTE YA FUE ANULADO",new ActionMessage("error.cartera.ajustes.ajusteAnuladoOtroUsuario"));
			saveErrors(request, errores);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);	   	        
	    	return mapping.findForward("paginaErroresActionErrors");  
		}
		mundo.setCodigo(ajustesForm.getCodigo());
		enTransaccion=mundo.anularAjuste(con,ajustesForm.getMotivoAnulacion(),usuario.getLoginUsuario(),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual());
		String temp=ajustesForm.getNumeroAjusteStr();
		ajustesForm.reset(usuario.getCodigoInstitucionInt());
		ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"EL AJUSTE "+temp+" FUE ANULADO EXITOSAMENTE"));
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
			UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);	
		return mapping.findForward("ingresarModificarAjustes");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarAjuste(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, HttpServletRequest request) 
	{
		ajustesForm.setModificacion(true);
		mundo.setConsecutivoAjuste(ajustesForm.getNumeroAjusteStr());
		mundo.setTipoAjusteStr(ajustesForm.getTipoAjuste());
		mundo.setInstitucion(ajustesForm.getInstitucion());
		mundo.setEstado(ConstantesBD.codigoEstadoCarteraGenerado);
		mundo.cargarEncabezadoAjuste(con);
		if(mundo.getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			//en caso que exista un error en la carga del ajuste y no se cargue informacion, 
			//se muestra el siguiente error.
			String nomAjuste="";
			if(ajustesForm.getTipoAjuste().equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
			{
				nomAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getNombre();
			}
			else if(ajustesForm.getTipoAjuste().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				nomAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getNombre();
			}
			ActionErrors error=new ActionErrors();
			error.add("No existe Ajuste", new ActionMessage("error.ajusteInexistente",nomAjuste,ajustesForm.getNumeroAjusteStr()));
			saveErrors(request,error);
			this.cerrarConexion(con);
			return mapping.findForward("ingresarModificarAjustes");
		}
		ajustesForm.setCodigo(mundo.getCodigo());
		ajustesForm.setCastigoCarteraS3(mundo.isCastigoCartera());
		ajustesForm.setConceptoCastigoCarteraS3(mundo.getConceptoCastigoCartera()==null?ConstantesBD.codigoNuncaValido+"":mundo.getConceptoCastigoCartera());
		ajustesForm.setTipoAjusteSeccion3(mundo.getTipoAjusteStr());
		ajustesForm.setFechaAjuste(mundo.getFechaAjuste());
		ajustesForm.setCuentaCobro(mundo.getCuentaCobro());
		double cueCobro=ConstantesBD.codigoNuncaValido;
		if(ajustesForm.getCuentaCobro()>0)
		{
			cueCobro=ajustesForm.getCuentaCobro();
			ajustesForm.setAjustePorCuentaCobro(true);
			ajustesForm.setAjustePorFactura(false);
			ajustesForm.setInformacionCargada(mundo.cargarCuentaCobro(con,ajustesForm.getCuentaCobro(),ajustesForm.isCastigoCartera(),UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())),true));
			ajustesForm.setMostrarMensaje(new ResultadoBoolean(false));
			if(ajustesForm.isInformacionCargada())
			{

				this.cargarInformacionGeneralToForm(ajustesForm,mundo);
			}
			//ajustesForm.setFacturaCuentaCobroRadicada()
		}
		else
		{
			ajustesForm.setAjustePorCuentaCobro(false);
			ajustesForm.setAjustePorFactura(true);
			ajustesForm.setCodigoFactura(mundo.obtenerCodigoFacturaAjuste(con,ajustesForm.getCodigo()));
			ajustesForm.setInformacionCargada(mundo.cargarUnaFactura(con,ajustesForm.getCodigoFactura(),ajustesForm.isCastigoCartera(),UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())),true,mundo.getCodigo()));
			ajustesForm.setMostrarMensaje(new ResultadoBoolean(false));
			if(ajustesForm.isInformacionCargada())
			{
				if(!mundo.getAjustesFacturaEmpresa().isFacturaSistema())
				{
					ajustesForm.getMostrarMensaje().setResultado(true);
					ajustesForm.getMostrarMensaje().setDescripcion("Factura Externa de Saldos Iniciales Cartera.");
				}
				this.cargarInformacionGeneralToForm(ajustesForm,mundo);
			}
			cueCobro=Utilidades.obtenerCuentaCobroFactura(ajustesForm.getCodigoFactura());
		}
		ajustesForm.setFacturaCuentaCobroRadicada(Utilidades.obtenerEstadoCuentaCobro(con,cueCobro,ajustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada);
		ajustesForm.setConceptoAjuste(mundo.getConceptoAjuste());
		ajustesForm.setMetodoAjuste(mundo.getMetodoAjuste());
		ajustesForm.setValorAjuste(mundo.getValorAjuste());
		ajustesForm.setObservaciones(mundo.getObservaciones());
        ajustesForm.setConsecutivoAjuste(mundo.getConsecutivoAjuste());
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarAjustes");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 */
	private void existenModificaciones(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo) 
	{
		ajustesForm.setExisteModifiacionMetodoAjusteAF(false);
		ajustesForm.setExisteModifiacionValorAjusteAF(false);
		mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
		mundo.getAjustesFacturaEmpresa().setFactura(ajustesForm.getCodigoFactura());
		mundo.setCodigo(ajustesForm.getCodigo());
		for(int k = 0 ; k < Integer.parseInt(ajustesForm.getFacturas("numeroregistros")+"") ; k ++)
		{
			mundo.getAjustesFacturaEmpresa().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
			if(mundo.getAjustesFacturaEmpresa().existeAjuste(con))
			{
				if(mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"valor_ajuste",(ajustesForm.getFacturas("valorajuste_"+k)+"").equals("")?"0":ajustesForm.getFacturas("valorajuste_"+k)+""))
				{
					ajustesForm.setExisteModifiacionMetodoAjusteAF(false);
					ajustesForm.setExisteModifiacionValorAjusteAF(true);
					k=Integer.parseInt(ajustesForm.getFacturas("numeroregistros")+"");
				}
				else if(mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"metodo_ajuste",ajustesForm.getFacturas("metodoajuste_"+k)+""))
				{
					mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
					mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
					if(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().existeAjuste(con))
					{
						ajustesForm.setExisteModifiacionMetodoAjusteAF(true);
						ajustesForm.setExisteModifiacionValorAjusteAF(false);
					}
				}
			}
		}
		
	}



	/**
	 * @param con
	 * @param ajustesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarMapaServiciosSeleccionados(Connection con, AjustesEmpresaForm ajustesForm, ActionMapping mapping) 
	{
		int numReg=Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"");
		int i=0;
		double totalAjustes=0,totalSaldo=0;
		
		if(numReg>0)
		{
			totalSaldo=Double.parseDouble(ajustesForm.getServiciosFacturas("totalsaldo")+"");
			totalAjustes=Double.parseDouble(ajustesForm.getServiciosFacturas("totalajustes")+"");
		}
		
		for(i=0;i<ajustesForm.getNumeroserviciosFacturasPopUp();i++)
		{
			if(UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturasPopUp("seleccionado_"+i)+""))
			{
				 
				{
					ajustesForm.setServiciosFacturas("codigodetalle_"+numReg,ajustesForm.getServiciosFacturasPopUp("codigodetalle_"+i));
					ajustesForm.setServiciosFacturas("solicitud_"+numReg,ajustesForm.getServiciosFacturasPopUp("solicitud_"+i));
	                ajustesForm.setServiciosFacturas("escirugia_"+numReg,ajustesForm.getServiciosFacturasPopUp("escirugia_"+i));
	                ajustesForm.setServiciosFacturas("codigoaxioma_"+numReg,ajustesForm.getServiciosFacturasPopUp("codigoaxioma_"+i));
					ajustesForm.setServiciosFacturas("codigoservart_"+numReg,ajustesForm.getServiciosFacturasPopUp("codigoservart_"+i));
					ajustesForm.setServiciosFacturas("nombreservart_"+numReg,ajustesForm.getServiciosFacturasPopUp("nombreservart_"+i));
					ajustesForm.setServiciosFacturas("esservicio_"+numReg,ajustesForm.getServiciosFacturasPopUp("esservicio_"+i));
					ajustesForm.setServiciosFacturas("loginmedico_"+numReg,ajustesForm.getServiciosFacturasPopUp("loginmedico_"+i));
					ajustesForm.setServiciosFacturas("codigomedico_"+numReg,ajustesForm.getServiciosFacturasPopUp("codigomedico_"+i));
					ajustesForm.setServiciosFacturas("nombremedico_"+numReg,ajustesForm.getServiciosFacturasPopUp("nombremedico_"+i));
					ajustesForm.setServiciosFacturas("codigopool_"+numReg,ajustesForm.getServiciosFacturasPopUp("codigopool_"+i));
					ajustesForm.setServiciosFacturas("nombrepool_"+numReg,ajustesForm.getServiciosFacturasPopUp("nombrepool_"+i));
					ajustesForm.setServiciosFacturas("saldo_"+numReg,ajustesForm.getServiciosFacturasPopUp("saldo_"+i));
					ajustesForm.setServiciosFacturas("valorajuste_"+numReg,ajustesForm.getServiciosFacturasPopUp("valorajuste_"+i));
	                ajustesForm.setServiciosFacturas("valorajusteoriginal_"+numReg,ajustesForm.getServiciosFacturasPopUp("valorajusteoriginal_"+i));
					ajustesForm.setServiciosFacturas("valorajustepool_"+numReg,ajustesForm.getServiciosFacturasPopUp("valorajustepool_"+i));
					ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+numReg,ajustesForm.getServiciosFacturasPopUp("valorajusteinstitucion_"+i));
					ajustesForm.setServiciosFacturas("concepto_"+numReg,ajustesForm.getServiciosFacturasPopUp("concepto_"+i));
					//verificar si el detalle ya existe, si existe, el concepto debe estar en blanco.
					for(int a=0;a<numReg;a++)
					{
						logger.info("sol--->"+Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("solicitud_"+i)+""));
						logger.info("sol--->"+Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("solicitud_"+a)+""));
						
						logger.info("serart--->"+Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("codigoservart_"+i)+""));
						logger.info("serart--->"+Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigoservart_"+a)+""));
						
						logger.info(" SOLICITUDES IGUALES --->"+(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("solicitud_"+i)+"")==Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("solicitud_"+a)+""))+"<---");
						logger.info(" SERART IGUALES --->"+(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("codigoservart_"+i)+"")==Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigoservart_"+a)+""))+"<---");
						if(
								(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("solicitud_"+i)+"")==Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("solicitud_"+a)+""))&&
								(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturasPopUp("codigoservart_"+i)+"")==Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigoservart_"+a)+""))
						)
						{
							logger.info("**************EEENNNNTTTTRRRREEEE*************");
							a=numReg;
							ajustesForm.setServiciosFacturas("concepto_"+numReg,"0");
						}
					}
					totalSaldo=totalSaldo+Double.parseDouble(ajustesForm.getServiciosFacturasPopUp("saldo_"+i)+"");
					totalAjustes=totalAjustes+Double.parseDouble(ajustesForm.getServiciosFacturasPopUp("valorajuste_"+i)==null?"0":ajustesForm.getServiciosFacturasPopUp("valorajuste_"+i).toString());
					numReg++;
					ajustesForm.setServiciosFacturas("numeroregistros",numReg+"");
				}
			}
			
		}
		ajustesForm.setServiciosFacturas("totalsaldo",totalSaldo+"");
		ajustesForm.setServiciosFacturas("totalajustes",totalAjustes+"");
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
	}
	
	//eliminarDetalleServArt accionEliminarServiciosSArticulo

	
	/**
	 * 
	 */
	private ActionForward accionEliminarDisAsocios(Connection con, AjustesEmpresaForm ajustesForm, ActionMapping mapping) 
	{
		int numReg=Integer.parseInt(ajustesForm.getServiciosAsocios("numRegistros")+"");
		int i=0;
		double totalAjustes=0,totalSaldo=0;
		
		
		int pos=ajustesForm.getRegEliminar();
		totalSaldo=totalSaldo-Double.parseDouble(ajustesForm.getServiciosAsocios("saldo_"+pos)+"");
		totalAjustes=totalAjustes-Double.parseDouble(ajustesForm.getServiciosAsocios("valorajuste_"+pos)==null?"0":ajustesForm.getServiciosAsocios("valorajuste_"+pos).toString());
		
		int ulPos=numReg-1;
		for(int cont=pos;cont<ulPos;cont++)
		{
			ajustesForm.setServiciosAsocios("consecitivoasodetfac_"+cont,ajustesForm.getServiciosAsocios("consecitivoasodetfac_"+(cont+1)));
			ajustesForm.setServiciosAsocios("codigodetfactura_"+cont,ajustesForm.getServiciosAsocios("codigodetfactura_"+(cont+1)));
            ajustesForm.setServiciosAsocios("codigoservicio_"+cont,ajustesForm.getServiciosAsocios("codigoservicio_"+(cont+1)));
            ajustesForm.setServiciosAsocios("codigoaxioma_"+cont,ajustesForm.getServiciosAsocios("codigoaxioma_"+(cont+1)));
			ajustesForm.setServiciosAsocios("acronimoasocio_"+cont,ajustesForm.getServiciosAsocios("acronimoasocio_"+(cont+1)));
			ajustesForm.setServiciosAsocios("nombreasocio_"+cont,ajustesForm.getServiciosAsocios("nombreasocio_"+(cont+1)));
			ajustesForm.setServiciosAsocios("nombreservicio_"+cont,ajustesForm.getServiciosAsocios("nombreservicio_"+(cont+1)));
			ajustesForm.setServiciosAsocios("codigomedico_"+cont,ajustesForm.getServiciosAsocios("codigomedico_"+(cont+1)));
			ajustesForm.setServiciosAsocios("nombremedico_"+cont,ajustesForm.getServiciosAsocios("nombremedico_"+(cont+1)));
			ajustesForm.setServiciosAsocios("loginmedico_"+cont,ajustesForm.getServiciosAsocios("loginmedico_"+(cont+1)));
			ajustesForm.setServiciosAsocios("codigopool_"+cont,ajustesForm.getServiciosAsocios("codigopool_"+(cont+1)));
			ajustesForm.setServiciosAsocios("descpool_"+cont,ajustesForm.getServiciosAsocios("nombdescpool_repool_"+(cont+1)));
			ajustesForm.setServiciosAsocios("saldo_"+cont,ajustesForm.getServiciosAsocios("saldo_"+(cont+1)));
			ajustesForm.setServiciosAsocios("eshonorarios_"+cont,ajustesForm.getServiciosAsocios("eshonorarios_"+(cont+1)));
            ajustesForm.setServiciosAsocios("porcentajepool_"+cont,ajustesForm.getServiciosAsocios("porcentajepool_"+(cont+1)));
			ajustesForm.setServiciosAsocios("porcentajemedico_"+cont,ajustesForm.getServiciosAsocios("porcentajemedico_"+(cont+1)));
			ajustesForm.setServiciosAsocios("valorajuste_"+cont,ajustesForm.getServiciosAsocios("valorajuste_"+(cont+1)));
			ajustesForm.setServiciosAsocios("concepto_"+cont,ajustesForm.getServiciosAsocios("concepto_"+(cont+1))==null?"0":ajustesForm.getServiciosAsocios("concepto_"+(cont+1)));
		}
		
		ajustesForm.getServiciosAsocios().remove("consecitivoasodetfac_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("codigodetfactura_"+ulPos);
        ajustesForm.getServiciosAsocios().remove("codigoservicio_"+ulPos);
        ajustesForm.getServiciosAsocios().remove("codigoaxioma_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("acronimoasocio_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("nombreasocio_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("nombreservicio_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("codigomedico_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("nombremedico_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("loginmedico_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("codigopool_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("descpool_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("saldo_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("eshonorarios_"+ulPos);
        ajustesForm.getServiciosAsocios().remove("porcentajepool_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("porcentajemedico_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("valorajuste_"+ulPos);
		ajustesForm.getServiciosAsocios().remove("concepto_"+ulPos);

		numReg=ulPos;
		ajustesForm.setServiciosAsocios("numRegistros",numReg);
		
		this.cerrarConexion(con);
		return mapping.findForward("asociosServicios");
		
	}
	
	/**
	 * 
	 */
	private ActionForward accionEliminarServiciosSArticulo(Connection con, AjustesEmpresaForm ajustesForm, ActionMapping mapping) 
	{
		int numReg=Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"");
		int i=0;
		double totalAjustes=0,totalSaldo=0;
		
		if(numReg>0)
		{
			totalSaldo=Double.parseDouble(ajustesForm.getServiciosFacturas("totalsaldo")+"");
			totalAjustes=Double.parseDouble(ajustesForm.getServiciosFacturas("totalajustes")+"");
		}
		int pos=ajustesForm.getRegEliminar();
		totalSaldo=totalSaldo-Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+pos)+"");
		totalAjustes=totalAjustes-Double.parseDouble(ajustesForm.getServiciosFacturas("valorajuste_"+pos)==null?"0":ajustesForm.getServiciosFacturas("valorajuste_"+pos).toString());
		
		int ulPos=numReg-1;
		for(int cont=pos;cont<ulPos;cont++)
		{
			ajustesForm.setServiciosFacturas("codigodetalle_"+cont,ajustesForm.getServiciosFacturas("codigodetalle_"+(cont+1)));
			ajustesForm.setServiciosFacturas("solicitud_"+cont,ajustesForm.getServiciosFacturas("solicitud_"+(cont+1)));
            ajustesForm.setServiciosFacturas("escirugia_"+cont,ajustesForm.getServiciosFacturas("escirugia_"+(cont+1)));
            ajustesForm.setServiciosFacturas("codigoaxioma_"+cont,ajustesForm.getServiciosFacturas("codigoaxioma_"+(cont+1)));
			ajustesForm.setServiciosFacturas("codigoservart_"+cont,ajustesForm.getServiciosFacturas("codigoservart_"+(cont+1)));
			ajustesForm.setServiciosFacturas("nombreservart_"+cont,ajustesForm.getServiciosFacturas("nombreservart_"+(cont+1)));
			ajustesForm.setServiciosFacturas("esservicio_"+cont,ajustesForm.getServiciosFacturas("esservicio_"+(cont+1)));
			ajustesForm.setServiciosFacturas("loginmedico_"+cont,ajustesForm.getServiciosFacturas("loginmedico_"+(cont+1)));
			ajustesForm.setServiciosFacturas("codigomedico_"+cont,ajustesForm.getServiciosFacturas("codigomedico_"+(cont+1)));
			ajustesForm.setServiciosFacturas("nombremedico_"+cont,ajustesForm.getServiciosFacturas("nombremedico_"+(cont+1)));
			ajustesForm.setServiciosFacturas("codigopool_"+cont,ajustesForm.getServiciosFacturas("codigopool_"+(cont+1)));
			ajustesForm.setServiciosFacturas("nombrepool_"+cont,ajustesForm.getServiciosFacturas("nombrepool_"+(cont+1)));
			ajustesForm.setServiciosFacturas("saldo_"+cont,ajustesForm.getServiciosFacturas("saldo_"+(cont+1)));
			ajustesForm.setServiciosFacturas("valorajuste_"+cont,ajustesForm.getServiciosFacturas("valorajuste_"+(cont+1)));
            ajustesForm.setServiciosFacturas("valorajusteoriginal_"+cont,ajustesForm.getServiciosFacturas("valorajusteoriginal_"+(cont+1)));
			ajustesForm.setServiciosFacturas("valorajustepool_"+cont,ajustesForm.getServiciosFacturas("valorajustepool_"+(cont+1)));
			ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+cont,ajustesForm.getServiciosFacturas("valorajusteinstitucion_"+(cont+1)));
			ajustesForm.setServiciosFacturas("concepto_"+cont,ajustesForm.getServiciosFacturas("concepto_"+(cont+1))==null?"0":ajustesForm.getServiciosFacturas("concepto_"+(cont+1)));
		}
		
		ajustesForm.getServiciosFacturas().remove("codigodetalle_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("solicitud_"+ulPos);
        ajustesForm.getServiciosFacturas().remove("escirugia_"+ulPos);
        ajustesForm.getServiciosFacturas().remove("codigoaxioma_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("codigoservart_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("nombreservart_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("esservicio_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("loginmedico_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("codigomedico_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("nombremedico_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("codigopool_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("nombrepool_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("saldo_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("valorajuste_"+ulPos);
        ajustesForm.getServiciosFacturas().remove("valorajusteoriginal_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("valorajustepool_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("valorajusteinstitucion_"+ulPos);
		ajustesForm.getServiciosFacturas().remove("concepto_"+ulPos);

		numReg=ulPos;
		ajustesForm.setServiciosFacturas("numeroregistros",numReg);
		
		ajustesForm.setServiciosFacturas("totalsaldo",totalSaldo+"");
		ajustesForm.setServiciosFacturas("totalajustes",totalAjustes+"");
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
		
	}

	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarServiciosArticulosPopUp(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping,boolean busquedaAvanzada) 
	{
		ResultSetDecorator rs=null;
		int index=0;
		try 
		{
			///consulta de servicios/////////////////////////////////////////////////////////////////////////
			if(!busquedaAvanzada)
			{
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFactura(con,ajustesForm.getCodigoFactura(),true);
			}
			else 
			{
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFacturaAvanzada(con,ajustesForm.getCodigoFactura(),ajustesForm.getCampoBusqueda(),ajustesForm.getValorCampoBusqueda(),true);
			}
			while(rs.next())
			{
				ajustesForm.setServiciosFacturasPopUp("seleccionado_"+index,"false");
				ajustesForm.setServiciosFacturasPopUp("codigodetalle_"+index,rs.getInt("codigodetalle")+"");
				ajustesForm.setServiciosFacturasPopUp("solicitud_"+index,rs.getInt("solicitud")+"");
				ajustesForm.setServiciosFacturasPopUp("escirugia_"+index,rs.getString("escirugia")+"");
				ajustesForm.setServiciosFacturasPopUp("codigoaxioma_"+index,rs.getString("codigoaxioma")+"");
				ajustesForm.setServiciosFacturasPopUp("codigoservart_"+index,rs.getInt("codigoservart")+"");
				ajustesForm.setServiciosFacturasPopUp("nombreservart_"+index,rs.getString("nombreservart"));
				ajustesForm.setServiciosFacturasPopUp("esservicio_"+index,rs.getBoolean("esservicio")+"");
				ajustesForm.setServiciosFacturasPopUp("loginmedico_"+index,rs.getString("loginmedico"));
				ajustesForm.setServiciosFacturasPopUp("codigomedico_"+index,rs.getInt("codigomedico")+"");
				ajustesForm.setServiciosFacturasPopUp("nombremedico_"+index,rs.getString("nombremedico"));
				ajustesForm.setServiciosFacturasPopUp("codigopool_"+index,rs.getInt("codigopool")+"");
				ajustesForm.setServiciosFacturasPopUp("nombrepool_"+index,rs.getString("nombrepool"));
				ajustesForm.setServiciosFacturasPopUp("saldo_"+index,rs.getDouble("saldo")+"");
				ajustesForm.setServiciosFacturasPopUp("valorajuste_"+index,"0");
				ajustesForm.setServiciosFacturasPopUp("valorajustepool_"+index,"0");
				ajustesForm.setServiciosFacturasPopUp("valorajusteinstitucion_"+index,"0");
				ajustesForm.setServiciosFacturasPopUp("concepto_"+index,ajustesForm.getConceptoAjusteFactura());
				index++;
			}
			///consulta de aritulos/////////////////////////////////////////////////////////////////////////
			boolean busArticulos=false;
			if(!busquedaAvanzada)
			{
				try {rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFactura(con,ajustesForm.getCodigoFactura(),false);
				busArticulos=true;
				}
				catch (Exception e){}
			}
			else if(ajustesForm.getCampoBusqueda().equals("nomServicioArticulo"))
			{
				busArticulos=true;
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFacturaAvanzada(con,ajustesForm.getCodigoFactura(),ajustesForm.getCampoBusqueda(),ajustesForm.getValorCampoBusqueda(),false);
			}
			if(busArticulos)
			{
				try{

				
				while(rs.next())
				{
					ajustesForm.setServiciosFacturasPopUp("seleccionado_"+index,"false");
					ajustesForm.setServiciosFacturasPopUp("codigodetalle_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("solicitud_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("escirugia_"+index,rs.getString("escirugia")+"");
                    ajustesForm.setServiciosFacturasPopUp("codigoaxioma_"+index,rs.getString("codigoaxioma")+"");
					ajustesForm.setServiciosFacturasPopUp("codigoservart_"+index,rs.getInt("codigoservart")+"");
					ajustesForm.setServiciosFacturasPopUp("nombreservart_"+index,rs.getString("nombreservart"));
					ajustesForm.setServiciosFacturasPopUp("esservicio_"+index,rs.getBoolean("esservicio")+"");
					ajustesForm.setServiciosFacturasPopUp("loginmedico_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("codigomedico_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("nombremedico_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("codigopool_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("nombrepool_"+index,"");
					ajustesForm.setServiciosFacturasPopUp("saldo_"+index,rs.getDouble("saldo")+"");
					ajustesForm.setServiciosFacturasPopUp("valorajuste_"+index,"0");
					ajustesForm.setServiciosFacturasPopUp("valorajustepool_"+index,"0");
					ajustesForm.setServiciosFacturasPopUp("valorajusteinstitucion_"+index,"0");
					ajustesForm.setServiciosFacturasPopUp("concepto_"+index,ajustesForm.getConceptoAjusteFactura());
					index++;
				}
				}	catch (Exception e){}
				ajustesForm.setNumeroserviciosFacturasPopUp(index);
				}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null ){
					rs.close();
				}
				cerrarConexion(con);
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return mapping.findForward("buscarServiciosArticulosPopUp");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarMapaFacturasSeleccionadas(Connection con, AjustesEmpresaForm ajustesForm, ActionMapping mapping) 
	{
		
		int numReg=Integer.parseInt(ajustesForm.getFacturas("numeroregistros")+"");
		int i=0;
		double totalAjustes=0,totalSaldo=0;
		
		
		if(numReg>0)
		{
			totalSaldo=Double.parseDouble(ajustesForm.getFacturas("totalsaldo")+"");
			totalAjustes=Double.parseDouble(ajustesForm.getFacturas("totalajustes")+"");
		}
		
		for(i=0;i<ajustesForm.getNumeroFacturasPopUp();i++)
		{
			if(UtilidadTexto.getBoolean(ajustesForm.getFacturasPopUp("seleccionado_"+i)+""))
			{
				boolean existe=false;
				for(int k=0;k<numReg;k++)
				{
					if((ajustesForm.getFacturas("codigofactura_"+k)+"").equals(ajustesForm.getFacturasPopUp("codigofactura_"+i)+""))
					{
						existe=true;
						k=numReg;
					}
				}
				if (!existe) 
				{
					ajustesForm.setFacturas("codigofactura_"+numReg,ajustesForm.getFacturasPopUp("codigofactura_"+i)+"");
					ajustesForm.setFacturas("consecutivofactura_"+numReg,ajustesForm.getFacturasPopUp("consecutivofactura_"+i)+"");
					ajustesForm.setFacturas("codigocentroatencion_"+numReg,ajustesForm.getFacturasPopUp("codigocentroatencion_"+i)+"");
					ajustesForm.setFacturas("nombrecentroatencion_"+numReg,ajustesForm.getFacturasPopUp("nombrecentroatencion_"+i)+"");
					ajustesForm.setFacturas("saldofactura_"+numReg,ajustesForm.getFacturasPopUp("saldofactura_"+i)+"");
	                ajustesForm.setFacturas("totalfactura_"+numReg,ajustesForm.getFacturasPopUp("totalfactura_"+i)+"");
					ajustesForm.setFacturas("facturasistema_"+numReg,ajustesForm.getFacturasPopUp("facturasistema_"+i)+"");
					ajustesForm.setFacturas("concepto_"+numReg,ajustesForm.getFacturasPopUp("concepto_"+i)+"");
					ajustesForm.setFacturas("metodoajuste_"+numReg,ajustesForm.getFacturasPopUp("metodoajuste_"+i)+"");
					ajustesForm.setFacturas("valorajuste_"+numReg,ajustesForm.getFacturasPopUp("valorajuste_"+i)+"");
					totalSaldo=totalSaldo+Double.parseDouble(ajustesForm.getFacturasPopUp("saldofactura_"+i)+"");
					totalAjustes=totalAjustes+Double.parseDouble(ajustesForm.getFacturasPopUp("valorajuste_"+i)+"");
					numReg++;
					ajustesForm.setFacturas("numeroregistros",numReg+"");

				}
			}
			else
			{
				boolean existe=false;
				int pos=0;
				for(int k=0;k<numReg;k++)
				{
					if((ajustesForm.getFacturas("codigofactura_"+k)+"").equals(ajustesForm.getFacturasPopUp("codigofactura_"+i)+""))
					{
						existe=true;
						pos=k;
						k=numReg;
					}
				}
				if (existe) 
				{
					int ultPos=numReg-1;
					
					totalSaldo=totalSaldo-Double.parseDouble(ajustesForm.getFacturas("saldofactura_"+pos)+"");
					totalAjustes=totalAjustes-Double.parseDouble(ajustesForm.getFacturas("valorajuste_"+pos)+"");
					
					for(int cont=pos;cont<ultPos;cont++)
					{
						ajustesForm.setFacturas("codigofactura_"+cont,ajustesForm.getFacturas("codigofactura_"+(cont+1))+"");
						ajustesForm.setFacturas("consecutivofactura_"+cont,ajustesForm.getFacturas("consecutivofactura_"+(cont+1))+"");
						ajustesForm.setFacturas("codigocentroatencion_"+cont,ajustesForm.getFacturas("codigocentroatencion_"+(cont+1))+"");
						ajustesForm.setFacturas("nombrecentroatencion_"+cont,ajustesForm.getFacturas("nombrecentroatencion_"+(cont+1))+"");
						ajustesForm.setFacturas("saldofactura_"+cont,ajustesForm.getFacturas("saldofactura_"+(cont+1))+"");
		                ajustesForm.setFacturas("totalfactura_"+cont,ajustesForm.getFacturas("totalfactura_"+(cont+1))+"");
						ajustesForm.setFacturas("facturasistema_"+cont,ajustesForm.getFacturas("facturasistema_"+(cont+1))+"");
						ajustesForm.setFacturas("concepto_"+cont,ajustesForm.getFacturas("concepto_"+(cont+1))+"");
						ajustesForm.setFacturas("metodoajuste_"+cont,ajustesForm.getFacturas("metodoajuste_"+(cont+1))+"");
						ajustesForm.setFacturas("valorajuste_"+cont,ajustesForm.getFacturas("valorajuste_"+(cont+1))==null?"0":ajustesForm.getFacturas("valorajuste_"+(cont+1))+"");
					}
					ajustesForm.getFacturas().remove("codigofactura_"+ultPos);
					ajustesForm.getFacturas().remove("consecutivofactura_"+ultPos);
					ajustesForm.getFacturas().remove("codigocentroatencion_"+ultPos);
					ajustesForm.getFacturas().remove("nombrecentroatencion_"+ultPos);
					ajustesForm.getFacturas().remove("saldofactura_"+ultPos);
	                ajustesForm.getFacturas().remove("totalfactura_"+ultPos);
					ajustesForm.getFacturas().remove("facturasistema_"+ultPos);
					ajustesForm.getFacturas().remove("concepto_"+ultPos);
					ajustesForm.getFacturas().remove("metodoajuste_"+ultPos);
					ajustesForm.getFacturas().remove("valorajuste_"+ultPos);

					numReg=ultPos;
					ajustesForm.setFacturas("numeroregistros",numReg+"");
				}
			}
		}
		ajustesForm.setFacturas("totalajustes",totalAjustes+"");
		ajustesForm.setFacturas("totalsaldo",totalSaldo+"");
		this.cerrarConexion(con);
		return mapping.findForward("ajustesFactura");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarFacturasPopUp(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping,boolean avanzada) 
	{
		ResultSetDecorator rs=null;
		int index=0;
		try 
		{
			mundo.setCuentaCobro(ajustesForm.getCuentaCobro());
			mundo.setInstitucion(ajustesForm.getInstitucion());
			if(!avanzada)
				rs=mundo.cargarFacturasCuentaCobro(con);
			else
				rs=mundo.cargarFacturasCuentaCobroAvanzada(con,ajustesForm.getValorCampoBusqueda());
			while(rs.next())
			{
				ajustesForm.setFacturasPopUp("codigofactura_"+index,rs.getString("codigofactura"));
				ajustesForm.setFacturasPopUp("consecutivofactura_"+index,rs.getString("consecutivofactura"));
				ajustesForm.setFacturasPopUp("codigocentroatencion_"+index,rs.getString("codigocentroatencion"));
				ajustesForm.setFacturasPopUp("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
				ajustesForm.setFacturasPopUp("saldofactura_"+index,rs.getString("saldofactura"));
                ajustesForm.setFacturasPopUp("totalfactura_"+index,rs.getString("totalfactura"));
                ajustesForm.setFacturasPopUp("facturasistema_"+index,rs.getString("facturasistema"));
				ajustesForm.setFacturasPopUp("concepto_"+index,ajustesForm.getConceptoAjuste());
				ajustesForm.setFacturasPopUp("metodoajuste_"+index,ajustesForm.getMetodoAjuste());
				ajustesForm.setFacturasPopUp("valorajuste_"+index,"0");
				index++;
			}
			ajustesForm.setNumeroFacturasPopUp(index);
			int numReg=Integer.parseInt((ajustesForm.getFacturas("numeroregistros")==null||(ajustesForm.getFacturas("numeroregistros")+"").equals(""))?"0":(ajustesForm.getFacturas("numeroregistros")+""));
			for(int j=0;j<index;j++)
			{
				for(int i=0;i<numReg;i++)
				{
					if((ajustesForm.getFacturas("codigofactura_"+i)+"").equals(ajustesForm.getFacturasPopUp("codigofactura_"+j)+""))
					{
						ajustesForm.setFacturasPopUp("seleccionado_"+j,"true");
						ajustesForm.setFacturasPopUp("concepto_"+j,ajustesForm.getFacturas("concepto_"+i)+"");
						ajustesForm.setFacturasPopUp("metodoajuste_"+j,ajustesForm.getFacturas("metodoajuste_"+i)+"");
						ajustesForm.setFacturasPopUp("valorajuste_"+j,ajustesForm.getFacturas("valorajuste_"+i)+"");
					}
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			try{
				if(rs != null ){
					rs.close();
				}
				cerrarConexion(con);
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return mapping.findForward("buscarFacturasPopUp");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleFactura(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping) 
	{
		ajustesForm.setCodigoFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+ajustesForm.getIndex())+""));
		ajustesForm.setConsecutivoFac(ajustesForm.getFacturas("consecutivofactura_"+ajustesForm.getIndex())+"");
		ajustesForm.setMetodoAjusteFactura(ajustesForm.getFacturas("metodoajuste_"+ajustesForm.getIndex())+"");
		if (ajustesForm.getFacturas("valorajuste_"+ajustesForm.getIndex()).toString().equals("0"))
		{
			ajustesForm.setValorAjusteFacturaStr(String.valueOf(ajustesForm.getValorAjusteFactura()));
		}
		else
		{
		ajustesForm.setValorAjusteFacturaStr(ajustesForm.getFacturas("valorajuste_"+ajustesForm.getIndex())+""); 
		}
		ajustesForm.setConceptoAjusteFactura(ajustesForm.getFacturas("concepto_"+ajustesForm.getIndex())+"");
        ajustesForm.setSaldoFacturaStr(ajustesForm.getFacturas("saldofactura_"+ajustesForm.getIndex())+"");
		mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
		mundo.getAjustesFacturaEmpresa().setFactura(ajustesForm.getCodigoFactura());
		mundo.getAjustesFacturaEmpresa().setMetodoAjuste(ajustesForm.getMetodoAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setValorAjuste(ajustesForm.getValorAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setConceptoAjuste(ajustesForm.getConceptoAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setInstitucion(ajustesForm.getInstitucion());
		cargarDetalleFactura(con,ajustesForm,mundo);
		this.cerrarConexion(con);
		return mapping.findForward("detalleFactura");
	}


	/**
	 * @param ajustesForm
	 * @param mundo
	 */
	private void cargarFacturasCuentaCobroManual(AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo) 
	{
		mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
		mundo.setCodigo(ajustesForm.getCodigo());
		double saldoFac=0;
		double totalAjuste=0;
		int i=0;
		String[] facturas,detFacturas;
		facturas=ajustesForm.getFacturasRelacionada().split(ConstantesBD.separadorSplit);
		for(i=0;i<facturas.length;i++)
		{
			detFacturas=facturas[i].split("@");
			ajustesForm.setFacturas("codigofactura_"+i,detFacturas[0]);
			ajustesForm.setFacturas("consecutivofactura_"+i,detFacturas[1]);
			ajustesForm.setFacturas("saldofactura_"+i,detFacturas[2]);
            ajustesForm.setFacturas("totalfactura_"+i,detFacturas[2]);
			ajustesForm.setFacturas("facturasistema_"+i,detFacturas[3]);
			ajustesForm.setFacturas("concepto_"+i,ajustesForm.getConceptoAjuste());
			ajustesForm.setFacturas("metodoajuste_"+i,ajustesForm.getMetodoAjuste());
			ajustesForm.setFacturas("valorajuste_"+i,"0");
			
			saldoFac=saldoFac+Double.parseDouble(detFacturas[2]);
		}
		ajustesForm.setFacturas("totalajustes",totalAjuste+"");
		ajustesForm.setFacturas("totalsaldo",saldoFac+"");
		ajustesForm.setFacturas("numeroregistros",i+"");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param enTransaccion 
	 * @return
	 */
	private ActionForward accionGuardarAjusteFactura(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, boolean enTransaccion) 
	{
		
		mundo.setCodigo(ajustesForm.getCodigo());
		//mundo.eliminarAjuste(con,2);
		if(guardarAjusteFactura(con,ajustesForm,mundo,enTransaccion))
		{
			cargarMapaFacturasCuentaCobro(con,ajustesForm,mundo);
			//ya se modifico, poner las variables nuevamente en false;
			ajustesForm.setConfirmacionModificacionAjusteFactura(false);
			ajustesForm.setExisteModifiacionMetodoAjusteAF(false);
			ajustesForm.setExisteModifiacionValorAjusteAF(false);
			//se cierra en el execute
			//UtilidadBD.closeConnection(con);	
			return mapping.findForward("ajustesFactura");
		}
		else 
		{
			ajustesForm.setConfirmacionModificacionAjusteFactura(false);
			ajustesForm.setExisteModifiacionMetodoAjusteAF(false);
			ajustesForm.setExisteModifiacionValorAjusteAF(false);
			logger.error("ERROR ALMACENANDO LOS DATOS.");
			//se cierra en el execute
			//UtilidadBD.closeConnection(con);	
			return null;
		}
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param enTransaccion 
	 * @param usuario
	 * @return
	 */
	private boolean guardarAjusteFactura(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, boolean enTransaccion) 
	{
		if(!enTransaccion)
		{	
			enTransaccion=UtilidadBD.iniciarTransaccion(con);
		}
		boolean isModificacion=false;
		boolean ingresar=false;
		String codFacturas="";
		if(enTransaccion)
		{
			mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
			mundo.setCodigo(ajustesForm.getCodigo());
			for(int k = 0 ; k < Integer.parseInt(ajustesForm.getFacturas("numeroregistros")+"") ; k ++)
			{
				if(enTransaccion)
				{
					if(k>0)
						codFacturas+=",";
					codFacturas+=ajustesForm.getFacturas("codigofactura_"+k)+"";
					mundo.getAjustesFacturaEmpresa().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
					mundo.getAjustesFacturaEmpresa().setMetodoAjuste(ajustesForm.getFacturas("metodoajuste_"+k)+"");
					mundo.getAjustesFacturaEmpresa().setValorAjuste(Double.parseDouble((ajustesForm.getFacturas("valorajuste_"+k)+"").equals("")?"0":(ajustesForm.getFacturas("valorajuste_"+k)+"")));
					mundo.getAjustesFacturaEmpresa().setConceptoAjuste(ajustesForm.getFacturas("concepto_"+k)+"");
					mundo.getAjustesFacturaEmpresa().setInstitucion(ajustesForm.getInstitucion());
					isModificacion=mundo.getAjustesFacturaEmpresa().existeAjuste(con);
					//toca mirar si el ajuste a sufrido una modificacion antes de actualizarlo, para saber que modificacion es si ajuste o metodo.
					if(isModificacion&&mundo.getAjustesFacturaEmpresa().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
					{
						if(mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"valor_ajuste",(ajustesForm.getFacturas("valorajuste_"+k)+"").equals("")?"0":ajustesForm.getFacturas("valorajuste_"+k)+""))
						{
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicio(con);
						}
						if(ajustesForm.isRecalcular()&&mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"metodo_ajuste",ajustesForm.getFacturas("metodoajuste_"+k)+""))
						{
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
							mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicio(con);
						}
					}
					if(!mundo.getAjustesFacturaEmpresa().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
					{
						if(isModificacion)
						{
							if(ajustesForm.isRecalcular()&&mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"metodo_ajuste",ajustesForm.getFacturas("metodoajuste_"+k)+""))
							{
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicio(con);
								//variable que me indica que el flju entro aca, y por tanto se debe generar un ingreso en la BD,pero despues que se ingrese el ajuste factura
								ingresar=true;
							}
							if(mundo.getAjustesFacturaEmpresa().existeModificacionAjusteFacturaCampo(con,"valor_ajuste",(ajustesForm.getFacturas("valorajuste_"+k)+"").equals("")?"0":ajustesForm.getFacturas("valorajuste_"+k)+""))
							{
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicio(con);
								//variable que me indica que el flju entro aca, y por tanto se debe generar un ingreso en la BD,pero despues que se ingrese el ajuste factura
								ingresar=true;
							}
						}
					}
					if(!isModificacion)
					{
						enTransaccion=mundo.getAjustesFacturaEmpresa().insertarAjusteFacturaEmpresa(con);
					}
					else
					{
						enTransaccion=mundo.getAjustesFacturaEmpresa().updateAjusteFacturaEmpresa(con);
					}
					if(!mundo.getAjustesFacturaEmpresa().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
					{
						if(isModificacion)
						{
							if(ingresar)
							{
								ajustesForm.setValorAjusteFacturaStr(mundo.getAjustesFacturaEmpresa().getValorAjuste()+"");
								ajustesForm.setSaldoFacturaStr(ajustesForm.getFacturas("saldofactura_"+k)+"");
                                ajustesForm.setTotalFactura(Double.parseDouble(ajustesForm.getFacturas("totalfactura_"+k)+""));
								ajustesForm.setConceptoAjusteFactura(mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
								ajustesForm.setMetodoAjusteFactura(mundo.getAjustesFacturaEmpresa().getMetodoAjuste());
								ajustesForm.setCodigoFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
								cargarDetalleFactura(con,ajustesForm,mundo);
								enTransaccion=this.insertarAjusteNiverDetalleAutomaticoPorcentual(con,ajustesForm,mundo,enTransaccion);
							}
						}
						else
						{
							ajustesForm.setValorAjusteFacturaStr(mundo.getAjustesFacturaEmpresa().getValorAjuste()+"");
							ajustesForm.setSaldoFacturaStr(ajustesForm.getFacturas("saldofactura_"+k)+"");
                            ajustesForm.setTotalFactura(Double.parseDouble(ajustesForm.getFacturas("totalfactura_"+k)+""));
							ajustesForm.setConceptoAjusteFactura(mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
							ajustesForm.setMetodoAjusteFactura(mundo.getAjustesFacturaEmpresa().getMetodoAjuste());
							ajustesForm.setCodigoFactura(Integer.parseInt(ajustesForm.getFacturas("codigofactura_"+k)+""));
							cargarDetalleFactura(con,ajustesForm,mundo);
							enTransaccion=this.insertarAjusteNiverDetalleAutomaticoPorcentual(con,ajustesForm,mundo,enTransaccion);
						}
					}
					
				}
				else
				{
					k=Integer.parseInt(ajustesForm.getFacturas("numeroregistros")+"");
				}
			}
			if(ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
			{
				mundo.getAjustesFacturaEmpresa().eliminarAjusteFacturaNoEstan(con,codFacturas);
			}
		}
		return enTransaccion;
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarCuentCobro(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping) 
	{
		ajustesForm.setAjustePorFactura(false);
		ajustesForm.setAjustePorCuentaCobro(true);
		//cargar la cuenta de cobro con sus facturas
		//cargar solo los datos necesarios para evitar el alto consumo de memoria.
		mundo.setInstitucion(ajustesForm.getInstitucion());
		ajustesForm.setInformacionCargada(mundo.cargarCuentaCobro(con,ajustesForm.getCuentaCobro(),ajustesForm.isCastigoCartera(),UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())),false));
		ajustesForm.setMostrarMensaje(new ResultadoBoolean(false));
		if(ajustesForm.isInformacionCargada())
		{

			this.cargarInformacionGeneralToForm(ajustesForm,mundo);
		}
		else
		{
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())))
			{
				if(Utilidades.obtenerEstadoCuentaCobro(con,ajustesForm.getCuentaCobro(),ajustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
				{
					ajustesForm.getMostrarMensaje().setResultado(true);
					ajustesForm.getMostrarMensaje().setDescripcion("No se permiten ajustes a Cuentas de Cobro radicadas.");
				}
				else
				{
					ajustesForm.getMostrarMensaje().setResultado(true);
					ajustesForm.getMostrarMensaje().setDescripcion("No se encontro informacion para los parámetros de Busqueda.");
				}
			}
			else
			{
				ajustesForm.getMostrarMensaje().setResultado(true);
				ajustesForm.getMostrarMensaje().setDescripcion("No se encontro informacion para los parámetros de Busqueda.");
			}
		}
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarAjustes");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param request
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		ajustesForm.reset(usuario.getCodigoInstitucionInt());
		mundo.reset();
		ValoresPorDefecto.cargarValoresIniciales(con);
		if(((ValoresPorDefecto.getAjustarCuentaCobroRadicada(usuario.getCodigoInstitucionInt())).equals("")))
			{
			logger.warn("FALTA DEFINIR PARAMETRO PERMITIR AJUSTES A CARTERA RADICADA Verifique. Proceso Cancelado.");
			request.setAttribute("codigoDescripcionError", "error.ajustesEmpresa.parametroPermitirAjustes");
			this.cerrarConexion(con);
			return mapping.findForward("paginaError");
			}
		if(!Utilidades.isDefinidoConsecutivoAjustesDebito(con,usuario.getCodigoInstitucionInt())||!Utilidades.isDefinidoConsecutivoAjustesCredito(con,usuario.getCodigoInstitucionInt()))
			{
			logger.warn("FALTA DEFINIR LOS CONSECUTIVOS DE AJUSTES Verifique. Proceso Cancelado.");
			request.setAttribute("codigoDescripcionError", "error.ajustesEmpresa.faltaDefinirConsecutivosAjustes");
			this.cerrarConexion(con);
			return mapping.findForward("paginaError");
			}
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarAjustes");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarFacturas(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, HttpServletRequest request)
	{
		//validaciones en caso de multiempresa
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(ajustesForm.getInstitucion())))
		{
			ActionErrors errores = new ActionErrors();
			String[] estadoVector=Utilidades.obtenerEstadoFactura(ajustesForm.getCodigoFactura()).split(ConstantesBD.separadorSplit);
			if(Integer.parseInt(estadoVector[0])!=ConstantesBD.codigoEstadoFacturacionFacturada)
			{
				errores.add("estado diferente", new ActionMessage("error.facturaEstadoDiferenteFacturada",estadoVector[1]));
			}
			
			ajustesForm.setFacturaExterna(ValidacionesFactura.esFacturaExterna(ajustesForm.getCodigoFactura()));
			if(ValidacionesFactura.facturaTieneAjustesPendientes(ajustesForm.getCodigoFactura()))
			{
				errores.add("Ajustes Pendientes", new ActionMessage("error.facturaConAjustesPendientes"));
			}
			if(ValidacionesFactura.facturaTienePagosPendientes(ajustesForm.getCodigoFactura()))
			{
				errores.add("Pagos Pendientes", new ActionMessage("error.facturaConPagosPendientes"));
			}
			if(ValidacionesFactura.esFacturaCerrada(ajustesForm.getCodigoFactura()))
			{
				errores.add("Factura Cerrada", new ActionMessage("error.ajustesEmpresa.facturaCerrada",ajustesForm.getConsecutivoFac()+""));
			}
			//si encontro errores en la factura.
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				this.cerrarConexion(con);
				return mapping.findForward("ingresarModificarAjustes");
			}
		}
		
		ajustesForm.setAjustePorFactura(true);
		ajustesForm.setAjustePorCuentaCobro(false);
		//cargar los datos de una factura
		//cargar solo los datos necesarios para evitar el alto consumo de memoria.
		//falta validar cuando la factura existe pero no cumple con las validaciones de la busqueda.
		ajustesForm.setInformacionCargada(mundo.cargarUnaFactura(con,ajustesForm.getFactura(),ajustesForm.isCastigoCartera(),UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())),false,ConstantesBD.codigoNuncaValido));
		ajustesForm.setMostrarMensaje(new ResultadoBoolean(false));
		if(ajustesForm.isInformacionCargada())
		{
			if(!mundo.getAjustesFacturaEmpresa().isFacturaSistema())
			{
				ajustesForm.getMostrarMensaje().setResultado(true);
				ajustesForm.getMostrarMensaje().setDescripcion("Factura Externa de Saldos Iniciales Cartera.");
			}
			this.cargarInformacionGeneralToForm(ajustesForm,mundo);
			
		}
		else
		{
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(ajustesForm.getInstitucion())))
			{
				
				if(Utilidades.obtenerEstadoCuentaCobro(con,Utilidades.obtenerCuentaCobroFactura(ajustesForm.getCodigoFactura()),ajustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
				{
					ajustesForm.getMostrarMensaje().setResultado(true);
					ajustesForm.getMostrarMensaje().setDescripcion("No se permiten ajustes a Cuentas de Cobro radicadas.");
				}
				else
				{
					ajustesForm.getMostrarMensaje().setResultado(true);
					ajustesForm.getMostrarMensaje().setDescripcion("No se encontro informacion para los parámetros de Busqueda.");
				}
			}
			else
			{
				ajustesForm.getMostrarMensaje().setResultado(true);
				ajustesForm.getMostrarMensaje().setDescripcion("No se encontro informacion para los parámetros de Busqueda.");
			}
		}
		//ajustesForm.setCuentaCobro(ConstantesBD.codigoNuncaValido);
		this.cerrarConexion(con);
		return mapping.findForward("ingresarModificarAjustes");
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param enTransaccion 
	 * @return
	 */
	private ActionForward accionGuardarDetalle(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, boolean enTransaccion) 
	{

		if(guardarDetalle(con, ajustesForm, mundo, enTransaccion))
		{
			cargarDetalleFactura(con,ajustesForm,mundo);
			//se cierra en el execute
			//UtilidadBD.closeConnection(con);	
			return mapping.findForward("detalleFactura");
		}
		else
		{
			logger.error("ERROR ALMACENANDO LOS DATOS.");
			//se cierra en el execute
			//UtilidadBD.closeConnection(con);	
			return null;
		}
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param estaEnTransaccion
	 * @return
	 */
	private boolean guardarDetalle(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, boolean estaEnTransaccion) 
	{
		boolean enTransaccion=false;
		if(estaEnTransaccion)
			enTransaccion=estaEnTransaccion;
		else
			enTransaccion=UtilidadBD.iniciarTransaccion(con);
		boolean isModificacion=false;
		if(enTransaccion)
		{
			mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
			mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(ajustesForm.getCodigoFactura());
			mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicio(con); 
			isModificacion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().existeAjuste(con);
			
			for(int k = 0 ; k < Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"") ; k ++)
			{
				if(enTransaccion)
				{
					if(UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturas("esservicio_"+k)+""))
					{
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigopk_"+k)+""));
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setDetFactSolicitud(Integer.parseInt(ajustesForm.getServiciosFacturas("codigodetalle_"+k)+""));
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigoPool(Integer.parseInt(ajustesForm.getServiciosFacturas("codigopool_"+k)+""));
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigoMedicoResponde(Integer.parseInt(ajustesForm.getServiciosFacturas("codigomedico_"+k)+""));
                        mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setSaldo(Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+k)+""));
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setMetodoAjuste(ajustesForm.getMetodoAjusteFactura());
						double valAjuste=Double.parseDouble((ajustesForm.getServiciosFacturas("valorajuste_"+k)+"").equals("")?"0":(ajustesForm.getServiciosFacturas("valorajuste_"+k)+""));
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjuste(valAjuste);
                        double valAjustePool=0;
                        double valAjusteInstitucion=0;
                        if(!UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturas("escirugia_"+k)+""))
                        {
                            valAjustePool=valAjuste*Utilidades.obtenerPorcentajePoolFacturacion(con, mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getDetFactSolicitud())/100;
                            valAjusteInstitucion=valAjuste-valAjustePool;
                        }
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjustePool(valAjustePool);
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjusteInstitucion(valAjusteInstitucion);
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setConceptoAjuste(ajustesForm.getServiciosFacturas("concepto_"+k)+"");
						mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setInstitucion(ajustesForm.getInstitucion());
						if(!isModificacion)
						{
							enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAjuste(con);
							//@todo, hacer la misma distribucion para la parte de paquetes.
                            if(UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturas("escirugia_"+k)+"")&&(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico)||mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual)))
                            {
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarAsociosServiciosCirugia(con);
                                enTransaccion=this.calcularDistribucionAutoPorcentualNivelAsociosCx(mundo,true);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAsociosServicio(con);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateValAjusteInstPoolSerCx(con);
                            }
                            else {
                            	if (UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturas("escirugia_"+k)+"")){
                            	  mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarAsociosServiciosCirugia(con);
                            	  mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAsociosServicio(con);
                                  mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateValAjusteInstPoolSerCx(con);
                            }}
                            /*
                             * SE QUITA ESTA PARTE POR CAMBIOS EN ANEXO 809 
                             *
                            else if(Utilidades.esSolicitudTipo(con,ajustesForm.getServiciosFacturas("solicitud_"+k)+"",ConstantesBD.codigoTipoSolicitudPaquetes))
                            {
                            	mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetallePaquete(con);
                            	enTransaccion=this.calcularDistribucionDetallePaquetes(mundo);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarDetallePaquetes(con);
                            }
                            */
                            	
						}
						else
						{
							enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateAjuste(con);
                            if((UtilidadTexto.getBoolean(ajustesForm.getServiciosFacturas("escirugia_"+k)+""))&&Double.parseDouble(ajustesForm.getServiciosFacturas("valorajusteoriginal_"+k)+"")!=Double.parseDouble(ajustesForm.getServiciosFacturas("valorajuste_"+k)+"")&&(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico)||mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual)))
                            {
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteServicioAsocios(con);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarAsociosServiciosCirugia(con);
                                enTransaccion=this.calcularDistribucionAutoPorcentualNivelAsociosCx(mundo,true);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAsociosServicio(con);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateValAjusteInstPoolSerCx(con);
                            }
                            /*
                             * SE QUITA ESTA PARTE POR CAMBIOS EN ANEXO 809
                            else if(Utilidades.esSolicitudTipo(con,ajustesForm.getServiciosFacturas("solicitud_"+k)+"",ConstantesBD.codigoTipoSolicitudPaquetes))
                            {
                            	mundo.getAjustesFacturaEmpresa().getAjustesDetalle().eliminarAjusteDetallePaquetes(con);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetallePaquete(con);
                                enTransaccion=this.calcularDistribucionDetallePaquetes(mundo);
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarDetallePaquetes(con);
                            }
                            */
						}
                        
					}
					else
					{
						//articulos.
						try
						{
							ResultSetDecorator rs=null;
							rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFacturasArticuloAgrupado(con,ajustesForm.getCodigoFactura(),Integer.parseInt(ajustesForm.getServiciosFacturas("codigoservart_"+k)+""));
							final int ESCALA = 10;
							BigDecimal totalAjusteDet = new BigDecimal(0).setScale(ESCALA, RoundingMode.HALF_UP);
							while (rs.next())
							{
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigopk(Utilidades.convertirAEntero(ajustesForm.getServiciosFacturas("codigopk_"+k)+""));
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setDetFactSolicitud(rs.getInt("codigodetalle"));
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigoPool(ConstantesBD.codigoNuncaValido);
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigoMedicoResponde(ConstantesBD.codigoNuncaValido);
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setMetodoAjuste(ajustesForm.getMetodoAjusteFactura());
                                mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setSaldo(Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+k)+""));
								//double ajusteDet=0;
								BigDecimal ajusteDet = new BigDecimal(0).setScale(ESCALA, RoundingMode.HALF_UP);

								//como los articulos estan agrupados debe calcular a que equivale el ajuste para cada arrticulo
								String valAjusteStr=UtilidadTexto.isEmpty(ajustesForm.getServiciosFacturas("valorajuste_"+k)+"")?"0":ajustesForm.getServiciosFacturas("valorajuste_"+k)+"";
								
								BigDecimal bdValAjusteStr = new BigDecimal(valAjusteStr);
								
								String saldoStr=UtilidadTexto.isEmpty(ajustesForm.getServiciosFacturas("saldo_"+k)+"")?"0":ajustesForm.getServiciosFacturas("saldo_"+k)+"";
								
								/*
								 * Nota javrammo: Mirando MT 6322, está pendiente revisar porque esta condición existe, ya que si "existe" un articulo con saldo 0
								 * y quieren hacerle una ajuste debito, nunca lo va a aplicar
								 */								
								if(Double.parseDouble(saldoStr)>0){
									//ajusteDet=rs.getDouble("saldo")*Double.parseDouble(valAjusteStr)/Double.parseDouble(saldoStr);
									ajusteDet = ajusteDet.add(new BigDecimal(rs.getDouble("saldo")*Double.parseDouble(valAjusteStr)/Double.parseDouble(saldoStr)).setScale(ESCALA, RoundingMode.HALF_UP));
								}
								
								totalAjusteDet = totalAjusteDet.add(ajusteDet);
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjuste(ajusteDet.doubleValue());
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjustePool(0);//para farmacia debe ser cero(0)
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjusteInstitucion(ajusteDet.doubleValue());//para farmacia la institucion debe tener el valor.
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setConceptoAjuste(ajustesForm.getServiciosFacturas("concepto_"+k)+"");
								mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setInstitucion(ajustesForm.getInstitucion());
								
								/*
								 * MT 6322 (javrammo)
								 * Si es el ultimo registro y la suma total del ajuste aplicado es menor/mayor al valor de ajuste ingresado
								 * y si realmente aplica ajuste (ver nota javrammmo)
								 * , suma diferencial con el fin de evitar desigualdad por decimales
								 */								
								if(rs.isLast() && ajusteDet.doubleValue() != 0){
									BigDecimal diferencia = new BigDecimal(totalAjusteDet.doubleValue() - bdValAjusteStr.doubleValue()).setScale(ESCALA, RoundingMode.HALF_UP);
									if(diferencia.doubleValue() > 0.0){
										ajusteDet = ajusteDet.subtract(diferencia.abs());
									}
									else if(diferencia.doubleValue() < 0.0){
										ajusteDet = ajusteDet.add(diferencia.abs());
									}																		
									mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjuste(ajusteDet.doubleValue());
									mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setValorAjusteInstitucion(ajusteDet.doubleValue());//para farmacia la institucion debe tener el valor.									
								}
								
								if(!isModificacion)
								{
									enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().insertarAjuste(con);
								}
								else
								{
									enTransaccion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().updateAjuste(con);
								}
							}

						}
						catch(SQLException e)
						{
							enTransaccion=false;
							logger.error("ERROR CONSULTANDO LOS ARTICULOS AGRUPADOS",e);
							
						}
					}
				}
				else
				{
					enTransaccion=false;
					k=Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"");
				}
			}
		}
		return enTransaccion;
	}


	
	
    /**
     * 
     * @param mundo
     * @param ponerConceptoMadre, Cuando es insercion, no modificacion, debe ponder el concepto del servicio madre.
     */
	private boolean calcularDistribucionAutoPorcentualNivelAsociosCx(AjustesEmpresa mundo,boolean ponerConceptoMadre)
    {
     
		logger.info("\n\n\n\n\n\n\n\n\n\n");
		logger.info("CALCUANDO PORCENTAJE");
        double valAjusteTotal=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getValorAjuste();
        double temSaldo=0;
        int valAjusteInt;
        //hace referencia a la misma direccion de memoria, en tonces lo que se haga en la 
        //variable mapa queda reflejado en el mundo.
        logger.info("--->"+valAjusteTotal);
        try
        {
            HashMap mapa=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getAsociosServicio();
            if(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico))
            {
                for(int k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
                {
                    temSaldo=Double.parseDouble(mapa.get("saldo_"+k)+"");
                    if((valAjusteTotal-temSaldo)>0)
                    {
                    	logger.info("1");
                        mapa.put("valorajuste_"+k,temSaldo+"");
                        valAjusteTotal=valAjusteTotal-temSaldo;
                    }
                    else if(valAjusteTotal>0)
                    {
                    	logger.info("2");
                        mapa.put("valorajuste_"+k,valAjusteTotal+"");
                        valAjusteTotal=0;
                    }
                    else
                    {
                    	logger.info("1");
                        mapa.put("valorajuste_"+k,"0");
                    }
                    logger.info("valor ajuste -->"+mapa.get("valorajuste_"+k));
                }
            }
            else if(mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual))
            {
                int k=0;
                for(k = 0 ; k < (Integer.parseInt(mapa.get("numRegistros")+"")-1) ; k ++)
                {
                	logger.info("CALUCLO");
                	logger.info("-->"+mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getValorAjuste());
                	logger.info("-->"+mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getSaldo());
                	logger.info("-->"+Double.parseDouble(mapa.get("saldo_"+k)+""));
                    valAjusteInt=(int)((mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getValorAjuste()/mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getSaldo())*Double.parseDouble(mapa.get("saldo_"+k)+""));
                    mapa.put("valorajuste_"+k,valAjusteInt+"");
                    valAjusteTotal=valAjusteTotal-valAjusteInt;
                    logger.info("valor ajuste -->"+mapa.get("valorajuste_"+k));
                }
                //poner al ultimo servicio/articulo el residuo.
                mapa.put("valorajuste_"+k,valAjusteTotal+"");
                logger.info("valor ajuste -->"+mapa.get("valorajuste_"+k));
            }
            for(int k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
            {
                double valAjustePool=0;
                double valAjusteInstitucion=Double.parseDouble(mapa.get("valorajuste_"+k)+"");//la institucion inicia con todo el valor solo se cambia en caso que sea honorarios.
                if(UtilidadTexto.getBoolean(mapa.get("eshonorarios_"+k)+""))
                {
                    double valAjuste=Double.parseDouble(mapa.get("valorajuste_"+k)+"");
                    double pool=Double.parseDouble(mapa.get("porcentajepool_"+k)+"");
                    valAjustePool=valAjuste*pool/100;
                    valAjusteInstitucion=valAjuste-valAjustePool;
                }
                mapa.put("valajustepool_"+k, valAjustePool+"");
                mapa.put("valajusteinstitucion_"+k, valAjusteInstitucion+"");
                if(ponerConceptoMadre)
                    mapa.put("concepto_"+k, mundo.getAjustesFacturaEmpresa().getAjustesDetalle().getConceptoAjuste());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
        
    }


    /**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param usuario
	 * @param mapping
     * @param enTransaccion 
     * @param request 
	 * @return
	 */
	private ActionForward accionGuardarAjusteGeneral(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, UsuarioBasico usuario, ActionMapping mapping, boolean enTransaccion, HttpServletRequest request) 
	{
		if(!ajustesForm.isModificacion())
		{
			if(ajustesForm.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				ajustesForm.setNombreConsecutivo(ConstantesBD.nombreConsecutivoAjustesCredito);
			}
			else if(ajustesForm.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
			{
				ajustesForm.setNombreConsecutivo(ConstantesBD.nombreConsecutivoAjustesDebito);
			}
			String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ajustesForm.getNombreConsecutivo(),ajustesForm.getInstitucion());
			ajustesForm.setConsecutivoAjuste(consecutivo);
			ajustesForm.setFechaElaboracion(UtilidadFecha.getFechaActual());
			ajustesForm.setHoraElaboracion(UtilidadFecha.getHoraActual());
			ajustesForm.setEstadoAjuste(ConstantesBD.codigoEstadoCarteraGenerado);
			logger.info("insertando el ajuste");
			if(ajustesForm.getConsecutivoAjuste().equals(""))
			{
				logger.error("ERROR OBTENIENDO EL VALOR DEL COSECUTIVO");
				//la conexion se cierra en el execute
				//UtilidadBD.closeConnection(con);	
				return null;
			}
			mundo.setNombreConsecutivo(ajustesForm.getNombreConsecutivo());
			mundo.setConsecutivoAjuste(ajustesForm.getConsecutivoAjuste());
			this.cargarAjusteGeneralToMundo(ajustesForm,mundo,usuario);
			if(!mundo.insertarAjusteGeneral(con))
			{
				logger.error("ERROR EN LA INSERCION");
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ajustesForm.getNombreConsecutivo(), ajustesForm.getInstitucion(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
//				la conexion se cierra en el execute
				//UtilidadBD.closeConnection(con);	
				return null;
			}
			ajustesForm.setCodigo(mundo.getCodigo());
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ajustesForm.getNombreConsecutivo(), ajustesForm.getInstitucion(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			logger.info("ajuste por factura-->"+ajustesForm.isAjustePorFactura());
			if(ajustesForm.isAjustePorFactura())
			{
				ajustesForm.setSaldoFactura(ajustesForm.getSaldo());
                ajustesForm.setValorAjusteFactura(ajustesForm.getValorAjuste());
				ajustesForm.setMetodoAjusteFactura(ajustesForm.getMetodoAjuste());
				ajustesForm.setConceptoAjusteFactura(ajustesForm.getConceptoAjuste());
				ArrayList filtro=new ArrayList();
				filtro.add(ajustesForm.getCodigoFactura()+"");
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
				ActionErrors errores= new ActionErrors();
				
				logger.info("--->"+ajustesForm.getCodigoFactura());
				
				if(ValidacionesFactura.facturaTieneAjustesPendientes(ajustesForm.getCodigoFactura()))
				{
					errores.add("Ajustes Pendientes", new ActionMessage("error.facturaConAjustesPendientes"));
				}
				if(!errores.isEmpty())
				{
			    	 saveErrors(request, errores);
			    	 //la conexion se cierra en el execute
					//UtilidadBD.abortarTransaccion(con);
			    	 //UtilidadBD.closeConnection(con);	   	        
			    	 return null;  
				}
				ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
				return guardarAjusteFacturaCasoPorFacturas(con,ajustesForm,mundo,mapping,enTransaccion);
			}
			else
			{
				ArrayList filtro=new ArrayList();
				filtro.add(ajustesForm.getCuentaCobro()+"");
				UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCuentaCobroDeterminada,filtro);
				ActionErrors errores= new ActionErrors();
				if(UtilidadValidacion.cuentaCobroConFactEnAjustesPendiente(ajustesForm.getCuentaCobro(),ajustesForm.getInstitucion()))
				{
					errores.add("cuenta cobro con facturas en estados pendiente",new ActionMessage("error.cartera.ajustes.cuentaCobroFacturasAjustesPendientas",ajustesForm.getCuentaCobro()));
				}
				if(!errores.isEmpty())
				{
			    	 saveErrors(request, errores);
			    	 //la conexion se cierra en el execute
					//UtilidadBD.abortarTransaccion(con);
				     //UtilidadBD.closeConnection(con);	   	        
				     return null;  
				}
				ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
				return cargarFacturasRelacionadasCuentaCobro(con,ajustesForm,mundo,mapping,enTransaccion);			
			}
		}
		else
		{
			logger.info("GUARDANDO EL AJUSTE - Modificacion");
			this.cargarAjusteGeneralToMundo(ajustesForm,mundo,usuario);
			mundo.setCodigo(ajustesForm.getCodigo());
			mundo.actualizarAjuste(con);
			if(ajustesForm.isRecalcular())
			{
				//ajustesForm.setFechaElaboracion(UtilidadFecha.getFechaActual());
				//ajustesForm.setHoraElaboracion(UtilidadFecha.getHoraActual());
				//ajustesForm.setEstadoAjuste(ConstantesBD.codigoEstadoCarteraGenerado);
				logger.info("ELIMINAR AJUSTES --> NIVEL 2");
				mundo.eliminarAjuste(con,2);
				ajustesForm.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
				if(ajustesForm.isAjustePorFactura())
				{
					if(ajustesForm.getCodigoFactura()<=0)
						ajustesForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(Utilidades.convertirAEntero(ajustesForm.getConsecutivoFac()),usuario.getCodigoInstitucionInt()));
					ajustesForm.setSaldoFactura(ajustesForm.getSaldo());
					ajustesForm.setValorAjusteFactura(ajustesForm.getValorAjuste());
					ajustesForm.setMetodoAjusteFactura(ajustesForm.getMetodoAjuste());
					ajustesForm.setConceptoAjusteFactura(ajustesForm.getConceptoAjuste());
					ArrayList filtro=new ArrayList();
					filtro.add(ajustesForm.getCodigoFactura()+"");
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
					ActionErrors errores= new ActionErrors();
					if(ValidacionesFactura.facturaTieneAjustesPendientesDiferentesAjusteActual(ajustesForm.getCodigoFactura(),ajustesForm.getCodigo()))
					{
						errores.add("Ajustes Pendientes", new ActionMessage("error.facturaConAjustesPendientes"));
					}
					if(!errores.isEmpty())
					{
				    	 saveErrors(request, errores);
				    	 //la conexion se cierra en el execute
						//UtilidadBD.abortarTransaccion(con);
					     //UtilidadBD.closeConnection(con);	   	        
					     return null;  
					}
					return guardarAjusteFacturaCasoPorFacturas(con,ajustesForm,mundo,mapping,enTransaccion);
				}
				else
				{
					ArrayList filtro=new ArrayList();
					filtro.add(ajustesForm.getCuentaCobro()+"");
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCuentaCobroDeterminada,filtro);
					ActionErrors errores= new ActionErrors();
					//if(UtilidadValidacion.cuentaCobroConFactEnAjustesPendiente(ajustesForm.getCuentaCobro(),ajustesForm.getInstitucion()))
					if(UtilidadValidacion.cuentaCobroConFactEnAjustesPendienteDiferenteActual(ajustesForm.getCuentaCobro(),ajustesForm.getInstitucion(),ajustesForm.getCodigo()))
					{
						errores.add("cuenta cobro con facturas en estados pendiente",new ActionMessage("error.cartera.ajustes.cuentaCobroFacturasAjustesPendientas",ajustesForm.getCuentaCobro()));
					}
					if(!errores.isEmpty())
					{
				    	 saveErrors(request, errores);
				    	 //la conexion se cierra en el execute
						//UtilidadBD.abortarTransaccion(con);
					     //UtilidadBD.closeConnection(con);	   	        
					     return null;  
					}
					return cargarFacturasRelacionadasCuentaCobro(con,ajustesForm,mundo,mapping,enTransaccion);			
				}
			}
			//realizar la actualizacion de los campos que no hacern recalcular.
			//no hay que recalcular solo cambiar valores en el ajuste general.
			if(ajustesForm.isAjustePorFactura())
			{
				ajustesForm.setSaldoFactura(ajustesForm.getSaldo());
				ajustesForm.setValorAjusteFactura(ajustesForm.getValorAjuste());
				ajustesForm.setMetodoAjusteFactura(ajustesForm.getMetodoAjuste());
				ajustesForm.setConceptoAjusteFactura(ajustesForm.getConceptoAjuste());
				
				
				//lo siguiente solo se hace para manejo de concurrencia, es decir impedir que
				//este modificando el ajuste y se cruce con un ajuste pendiente
				{
					ArrayList filtro=new ArrayList();
					filtro.add(ajustesForm.getCodigoFactura()+"");
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
					ActionErrors errores= new ActionErrors();
					
					if(ValidacionesFactura.facturaTieneAjustesPendientesDiferentesAjusteActual(ajustesForm.getCodigoFactura(),ajustesForm.getCodigo()))
					{
						errores.add("Ajustes Pendientes", new ActionMessage("error.facturaConAjustesPendientes"));
					}
					if(!errores.isEmpty())
					{
				    	 saveErrors(request, errores);
				    	 //la conexion se cierra en el execute
						//UtilidadBD.abortarTransaccion(con);
					     //UtilidadBD.closeConnection(con);	   	        
					     return null;  
					}
				}
				if(ajustesForm.isFacturaExterna())
				{
//					la conexion se cierra en el execute
					//this.cerrarConexion(con);
					return mapping.findForward("ingresarModificarAjustes");
				}
				else
				{
					cargarDetalleFactura(con,ajustesForm,mundo);
//					la conexion se cierra en el execute
					//this.cerrarConexion(con);
					return mapping.findForward("detalleFactura");
				}
			}
			else
			{
				ResultSetDecorator rs=null;
				mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
				mundo.setCodigo(ajustesForm.getCodigo());
				//lo siguiente solo se hace para manejo de concurrencia, es decir impedir que
				//este modificando el ajuste y se cruce con un ajuste pendiente
				{
					ArrayList filtro=new ArrayList();
					filtro.add(ajustesForm.getCuentaCobro()+"");
					UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoCuentaCobroDeterminada,filtro);
					ActionErrors errores= new ActionErrors();
					if(UtilidadValidacion.cuentaCobroConFactEnAjustesPendienteDiferenteActual(ajustesForm.getCuentaCobro(),ajustesForm.getInstitucion(),ajustesForm.getCodigo()))
					{
						errores.add("cuenta cobro con facturas en estados pendiente",new ActionMessage("error.cartera.ajustes.cuentaCobroFacturasAjustesPendientas",ajustesForm.getCuentaCobro()));
					}
					if(!errores.isEmpty())
					{
				    	 saveErrors(request, errores);
				    	 //la conexion se cierra en el execute
						//UtilidadBD.abortarTransaccion(con);
					     //UtilidadBD.closeConnection(con);	   	        
				    	 return null;    
					}
				}
				double saldoFac=0;
				double totalAjuste=0;
				int index=0; 
				try 
				{
					rs=mundo.cargarFacturasCuentaCobroAjuste(con);
					while(rs.next())
					{
						ajustesForm.setFacturas("codigofactura_"+index,rs.getString("codigofactura"));
						ajustesForm.setFacturas("consecutivofactura_"+index,rs.getString("consecutivofactura"));
						ajustesForm.setFacturas("codigocentroatencion_"+index,rs.getString("codigocentroatencion"));
						ajustesForm.setFacturas("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
						ajustesForm.setFacturas("saldofactura_"+index,rs.getString("saldofactura"));
                        ajustesForm.setFacturas("totalfactura_"+index,rs.getString("totalfactura"));
                        ajustesForm.setFacturas("facturasistema_"+index,rs.getString("facturasistema"));
						ajustesForm.setFacturas("concepto_"+index,rs.getString("conceptoajuste"));
						ajustesForm.setFacturas("metodoajuste_"+index,rs.getString("metodoajuste"));
						ajustesForm.setFacturas("valorajuste_"+index,rs.getString("valorajuste"));
						totalAjuste=totalAjuste+rs.getDouble("valorajuste");
						saldoFac=saldoFac+rs.getDouble("saldofactura");
						index++;
					}
					ajustesForm.setFacturas("totalajustes",totalAjuste+"");
					ajustesForm.setFacturas("totalsaldo",saldoFac+"");
					ajustesForm.setFacturas("numeroregistros",index+"");
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
//				la conexion se cierra en el execute
				//this.cerrarConexion(con);
				return mapping.findForward("ajustesFactura");
			}
		}
	}


	/**
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @param enTransaccion 
	 * @param usuario
	 * @return
	 */
	private ActionForward cargarFacturasRelacionadasCuentaCobro(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, boolean enTransaccion) 
	{
		//MT 5012 
		if(ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico)||ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual) || ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
		{
			mundo.setInstitucion(ajustesForm.getInstitucion());
			mundo.setCuentaCobro(ajustesForm.getCuentaCobro());
			cargarMapaFacturasCuentaCobro(con,ajustesForm,mundo);
			if(guardarAjusteFactura(con,ajustesForm,mundo,enTransaccion))
			{
				//UtilidadBD.finalizarTransaccion(con);
				cargarMapaFacturasCuentaCobro(con,ajustesForm,mundo);
//				la conexion se cierra en el execute
				//this.cerrarConexion(con);
				return mapping.findForward("ajustesFactura");
			}
			else 
			{
				//UtilidadBD.abortarTransaccion(con);
				logger.error("ERROR ALMACENANDO LOS DATOS.");
//				la conexion se cierra en el execute
				//this.cerrarConexion(con);
				return null;
			}
		}
		else
		{
			ajustesForm.setFacturas("numeroregistros","0");
//			la conexion se cierra en el execute
			//this.cerrarConexion(con);
			return mapping.findForward("ajustesFactura");
		}
	}


	/**
	 * @param ajustesForm
	 * @param mundo
	 */
	private void cargarMapaFacturasCuentaCobro(Connection con,AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo) 
	{
		ResultSetDecorator rs=null;
		mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
		mundo.setCodigo(ajustesForm.getCodigo());
		boolean isModificacion = mundo.getAjustesFacturaEmpresa().existeAjuste(con);
		//rs=mundo.cargarFacturasCuentaCobro(con);
		double saldoFac=0;
		double totalAjuste=0;
		int index=0;
		try 
		{
			if(!isModificacion)
				rs=mundo.cargarFacturasCuentaCobro(con);
			else
				rs=mundo.cargarFacturasCuentaCobroAjuste(con);
			while(rs.next())
			{
				ajustesForm.setFacturas("codigofactura_"+index,rs.getString("codigofactura"));
				ajustesForm.setFacturas("consecutivofactura_"+index,rs.getString("consecutivofactura"));
				ajustesForm.setFacturas("codigocentroatencion_"+index,rs.getString("codigocentroatencion"));
				ajustesForm.setFacturas("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
				ajustesForm.setFacturas("saldofactura_"+index,rs.getString("saldofactura"));
                ajustesForm.setFacturas("totalfactura_"+index,rs.getString("totalfactura"));
                ajustesForm.setFacturas("facturasistema_"+index,rs.getString("facturasistema"));
                ajustesForm.setFacturas("codigocentroatencion_"+index,rs.getString("codigocentroatencion"));
                ajustesForm.setFacturas("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
				if(!isModificacion)
				{

					ajustesForm.setFacturas("concepto_"+index,ajustesForm.getConceptoAjuste());
					ajustesForm.setFacturas("metodoajuste_"+index,ajustesForm.getMetodoAjuste());
					ajustesForm.setFacturas("valorajuste_"+index,"0");
				}
				else
				{
					ajustesForm.setFacturas("concepto_"+index,rs.getString("conceptoajuste"));
					ajustesForm.setFacturas("metodoajuste_"+index,rs.getString("metodoajuste"));
					ajustesForm.setFacturas("valorajuste_"+index,rs.getString("valorajuste"));
					totalAjuste=totalAjuste+rs.getDouble("valorajuste");
				}
				saldoFac=saldoFac+rs.getDouble("saldofactura");
				index++;
			}
			///////////////////////////////////////////////////////////////////DISTRIBUCION DEL AJUSTE/////////////////////////////////////////////////////////////////
			if(!isModificacion)
			{
				double temSaldo=0,
				valAjusteTotal=ajustesForm.getValorAjuste();
				int valAjusteInt=0;
				if(ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico))
				{
					for(int k = 0 ; k < index ; k ++)
					{
						temSaldo=Double.parseDouble(ajustesForm.getFacturas("saldofactura_"+k)+"");
						if((valAjusteTotal-temSaldo)>0)
						{
							ajustesForm.setFacturas("valorajuste_"+k,temSaldo+"");
							valAjusteTotal=valAjusteTotal-temSaldo;
						}
						else if(valAjusteTotal>0)
						{
							ajustesForm.setFacturas("valorajuste_"+k,valAjusteTotal+"");
							valAjusteTotal=0;
						}
						else
						{
							ajustesForm.setFacturas("valorajuste_"+k,"0");
						}
					}
				}
				else if(ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual))
				{
					int k=0;
					for(k = 0 ; k < (index-1) ; k ++)
					{
						valAjusteInt=(int)((ajustesForm.getValorAjuste()/ajustesForm.getSaldo())*Double.parseDouble(ajustesForm.getFacturas("saldofactura_"+k)+""));
						ajustesForm.setFacturas("valorajuste_"+k,valAjusteInt+"");
						valAjusteTotal=valAjusteTotal-valAjusteInt;
					}
					//poner al ultimo servicio/articulo el residuo.
					ajustesForm.setFacturas("valorajuste_"+k,((int)valAjusteTotal)+"");
				}
				totalAjuste=ajustesForm.getValorAjuste();
			}
			////////////////////////////////////////////////////////////////////////////////////////////////
			ajustesForm.setFacturas("totalajustes",totalAjuste+"");
			ajustesForm.setFacturas("totalsaldo",saldoFac+"");
			ajustesForm.setFacturas("numeroregistros",index+"");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}


	/**
	 * @param ajustesForm
	 * @param mundo
	 * @param mapping
	 * @param enTransaccion 
	 * @param usuario
	 * @return
	 */
	private ActionForward guardarAjusteFacturaCasoPorFacturas(Connection con,AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, ActionMapping mapping, boolean enTransaccion)  
	{
		mundo.getAjustesFacturaEmpresa().setCodigo(ajustesForm.getCodigo());
		mundo.getAjustesFacturaEmpresa().setFactura(ajustesForm.getCodigoFactura());
		mundo.getAjustesFacturaEmpresa().setMetodoAjuste(ajustesForm.getMetodoAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setValorAjuste(ajustesForm.getValorAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setConceptoAjuste(ajustesForm.getConceptoAjusteFactura());
		mundo.getAjustesFacturaEmpresa().setInstitucion(ajustesForm.getInstitucion());
		boolean insercionExitosa=mundo.getAjustesFacturaEmpresa().insertarAjusteFacturaEmpresa(con);
		if(ajustesForm.isFacturaExterna()&&insercionExitosa)
		{
//			la conexion se cierra en el execute
			//this.cerrarConexion(con);
			return mapping.findForward("ingresarModificarAjustes");
		}
		else if(insercionExitosa)
		{
			cargarDetalleFactura(con,ajustesForm,mundo);
			if(ajustesForm.getMetodoAjuste().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
			{
//				la conexion se cierra en el execute
				//this.cerrarConexion(con);
				//Mt 5011
				cargarDetalleFactura(con,ajustesForm,mundo);
				return mapping.findForward("detalleFactura");
			}
			else
			{
				this.insertarAjusteNiverDetalleAutomaticoPorcentual(con,ajustesForm,mundo,enTransaccion);
				//se debe cargar nuevamente la distribucion como quedo despues de guardar
				cargarDetalleFactura(con,ajustesForm,mundo);
//				la conexion se cierra en el execute
				//this.cerrarConexion(con);
				return mapping.findForward("detalleFactura");
			}
		}
		logger.error("ERROR INGRESANDO EL AJUSTE PARA LA FACTURA");
//		la conexion se cierra en el execute
		//this.cerrarConexion(con);
		return null;
	}


	/**
	 * Metodo para guardar una factura o un servicio con ajuste porcentual o automatico.
	 * es requerdico que se carge en el form el valor ajusteFactura, el saldo factura y el metodo de ajuste.
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param enTransaccion
	 */
	private boolean insertarAjusteNiverDetalleAutomaticoPorcentual(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo, boolean enTransaccion) 
	{
		double valAjusteTotal=ajustesForm.getValorAjusteFactura();
		double temSaldo=0;
		int valAjusteInt;
		ajustesForm.setServiciosFacturas("totalajustes",valAjusteTotal+"");
		if(ajustesForm.getMetodoAjusteFactura().equals(ConstantesBD.tipoMetodoAjusteCarteraAutomatico))
		{
			for(int k = 0 ; k < Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"") ; k ++)
			{
				temSaldo=Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+k)+"");
				if((valAjusteTotal-temSaldo)>0)
				{
					ajustesForm.setServiciosFacturas("valorajuste_"+k,temSaldo+"");
					valAjusteTotal=valAjusteTotal-temSaldo;
				}
				else if(valAjusteTotal>0)
				{
					ajustesForm.setServiciosFacturas("valorajuste_"+k,valAjusteTotal+"");
					valAjusteTotal=0;
				}
				else
				{
					ajustesForm.setServiciosFacturas("valorajuste_"+k,"0");
				}
			}
		}
		else if(ajustesForm.getMetodoAjusteFactura().equals(ConstantesBD.tipoMetodoAjusteCarteraPorcentual))
		{
			int k=0;
			for(k = 0 ; k < (Integer.parseInt(ajustesForm.getServiciosFacturas("numeroregistros")+"")-1) ; k ++)
			{
				//valAjusteInt=(int)((ajustesForm.getValorAjusteFactura()/ajustesForm.getSaldoFactura())*Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+k)+""));
                valAjusteInt=(int)((ajustesForm.getValorAjusteFactura()/ajustesForm.getTotalFactura())*Double.parseDouble(ajustesForm.getServiciosFacturas("saldo_"+k)+""));
				ajustesForm.setServiciosFacturas("valorajuste_"+k,valAjusteInt+"");
				valAjusteTotal=valAjusteTotal-valAjusteInt;
			}
			//poner al ultimo servicio/articulo el residuo.
			ajustesForm.setServiciosFacturas("valorajuste_"+k,valAjusteTotal+"");
		}
		//transaccion superior
		if(enTransaccion)
		{
			return guardarDetalle(con, ajustesForm, mundo, enTransaccion);
		}
		/*
		else
		{
			if(guardarDetalle(con, ajustesForm, mundo, enTransaccion))
			{
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
			}
		}
		*/
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param paginaDetalle, indica si el metodo es llamado desde la paginaDetalle.
	 */
	private void cargarDetalleFactura(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo ) 
	{
		
		mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setCodigo(ajustesForm.getCodigo());
		mundo.getAjustesFacturaEmpresa().getAjustesDetalle().setFactura(ajustesForm.getCodigoFactura());
		this.cargarDetalleFacturaAutoPorce(con,ajustesForm,mundo);
	}


	/**
	 * Metodo que carga el detalle de una factura para automatico o porcentual.
	 * Cuando el metodo de ajuste es manual no se debe cargar nada ya que los
	 * servicios se seleccionan por up pop-up.
	 * @param con
	 * @param ajustesForm
	 * @param mundo
	 * @param paginaDetalle
	 */
	private void cargarDetalleFacturaAutoPorce(Connection con, AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo) 
	{
		boolean isModificacion=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().existeAjuste(con);
		double totalSaldo=0;
		double totalAjustes=0;
		ajustesForm.setServiciosFacturas("numeroservicios","0");
		ajustesForm.setServiciosFacturas("numeroarticulos","0");
		ResultSetDecorator rs=null;
		int index=0;
		try 
		{
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
			logger.info("es modificacion --->"+isModificacion);
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n");
			///consulta de servicios/////////////////////////////////////////////////////////////////////////
			if(!isModificacion&&!ajustesForm.getMetodoAjusteFactura().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFactura(con,ajustesForm.getCodigoFactura(),true);
			else
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleAjusteFactura(con,ajustesForm.getCodigo(),ajustesForm.getCodigoFactura(),true);
			while(rs.next())
			{
				
				ajustesForm.setServiciosFacturas("codigodetalle_"+index,rs.getInt("codigodetalle")+"");
				ajustesForm.setServiciosFacturas("solicitud_"+index,rs.getInt("solicitud")+"");
                ajustesForm.setServiciosFacturas("escirugia_"+index,rs.getString("escirugia")+"");
                ajustesForm.setServiciosFacturas("codigoaxioma_"+index,rs.getString("codigoaxioma")+"");
				ajustesForm.setServiciosFacturas("codigoservart_"+index,rs.getInt("codigoservart")+"");
				ajustesForm.setServiciosFacturas("nombreservart_"+index,rs.getString("nombreservart"));
				ajustesForm.setServiciosFacturas("esservicio_"+index,rs.getBoolean("esservicio")+"");
				ajustesForm.setServiciosFacturas("loginmedico_"+index,rs.getString("loginmedico"));
				ajustesForm.setServiciosFacturas("codigomedico_"+index,rs.getInt("codigomedico")+"");
				ajustesForm.setServiciosFacturas("nombremedico_"+index,rs.getString("nombremedico"));
				ajustesForm.setServiciosFacturas("codigopool_"+index,rs.getInt("codigopool")+"");
				ajustesForm.setServiciosFacturas("nombrepool_"+index,rs.getString("nombrepool"));
				ajustesForm.setServiciosFacturas("saldo_"+index,rs.getDouble("saldo")+"");
				if(!isModificacion)
				{
				ajustesForm.setServiciosFacturas("valorajuste_"+index,"0");
                ajustesForm.setServiciosFacturas("valorajusteoriginal_"+index,"0");
				ajustesForm.setServiciosFacturas("valorajustepool_"+index,"0");
				ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+index,"0");
				ajustesForm.setServiciosFacturas("concepto_"+index,mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
				ajustesForm.setServiciosFacturas("codigopk_"+index,"0");
				}
				else
				{
					ajustesForm.setServiciosFacturas("codigopk_"+index,rs.getInt("codigopk")+"");
					ajustesForm.setServiciosFacturas("valorajuste_"+index,rs.getString("valorajuste"));
                    ajustesForm.setServiciosFacturas("valorajusteoriginal_"+index,rs.getString("valorajusteoriginal"));
					ajustesForm.setServiciosFacturas("valorajustepool_"+index,rs.getString("valorajustepool"));
					ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+index,rs.getString("valorajusteinstitucion"));
					//posiblemente puedo quetar la variable paginaDetalle pues isModificacion me lo dice todo.
					//if(paginaDetalle||isModificacion)
					if(isModificacion)
					{
						ajustesForm.setServiciosFacturas("concepto_"+index,rs.getString("conceptoajuste"));
					}
					else
					{
						ajustesForm.setServiciosFacturas("concepto_"+index,mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
					}
					totalAjustes=totalAjustes+rs.getDouble("valorajuste");
				}
				totalSaldo=totalSaldo+rs.getDouble("saldo");
				index++;
			}
			ajustesForm.setServiciosFacturas("numeroservicios",index+"");
			///consulta de aritulos/////////////////////////////////////////////////////////////////////////
			int articulos=0;
			if(!isModificacion&&!ajustesForm.getMetodoAjusteFactura().equals(ConstantesBD.tipoMetodoAjusteCarteraManual))
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleFactura(con,ajustesForm.getCodigoFactura(),false);
			else
				rs=mundo.getAjustesFacturaEmpresa().getAjustesDetalle().cargarDetalleAjusteFactura(con,ajustesForm.getCodigo(),ajustesForm.getCodigoFactura(),false);
			while(rs.next())
			{
				ajustesForm.setServiciosFacturas("codigodetalle_"+index,"");
				ajustesForm.setServiciosFacturas("solicitud_"+index,"");
				ajustesForm.setServiciosFacturas("escirugia_"+index,rs.getString("escirugia")+"");
                ajustesForm.setServiciosFacturas("codigoaxioma_"+index,rs.getString("codigoaxioma")+"");
				ajustesForm.setServiciosFacturas("codigoservart_"+index,rs.getInt("codigoservart")+"");
				ajustesForm.setServiciosFacturas("nombreservart_"+index,rs.getString("nombreservart"));
				ajustesForm.setServiciosFacturas("esservicio_"+index,rs.getBoolean("esservicio")+"");
				ajustesForm.setServiciosFacturas("loginmedico_"+index,"");
				ajustesForm.setServiciosFacturas("codigomedico_"+index,"");
				ajustesForm.setServiciosFacturas("nombremedico_"+index,"");
				ajustesForm.setServiciosFacturas("codigopool_"+index,"");
				ajustesForm.setServiciosFacturas("nombrepool_"+index,"");
				ajustesForm.setServiciosFacturas("saldo_"+index,rs.getDouble("saldo")+"");
				if(!isModificacion)
				{
					ajustesForm.setServiciosFacturas("codigopk_"+index,"");
					ajustesForm.setServiciosFacturas("valorajuste_"+index,"0");
					ajustesForm.setServiciosFacturas("valorajusteoriginal_"+index,"0");
					ajustesForm.setServiciosFacturas("valorajustepool_"+index,"0");
					ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+index,"0");
					ajustesForm.setServiciosFacturas("concepto_"+index,mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
				}
				else
				{
					ajustesForm.setServiciosFacturas("codigopk_"+index,rs.getInt("codigopk")+"");
					ajustesForm.setServiciosFacturas("valorajuste_"+index,rs.getString("valorajuste"));
                    ajustesForm.setServiciosFacturas("valorajusteoriginal_"+index,rs.getString("valorajusteoriginal"));
					ajustesForm.setServiciosFacturas("valorajustepool_"+index,rs.getString("valorajustepool"));
					ajustesForm.setServiciosFacturas("valorajusteinstitucion_"+index,rs.getString("valorajusteinstitucion"));
					//if(paginaDetalle)
					if(isModificacion)
						ajustesForm.setServiciosFacturas("concepto_"+index,rs.getString("conceptoajuste"));
					else
						ajustesForm.setServiciosFacturas("concepto_"+index,mundo.getAjustesFacturaEmpresa().getConceptoAjuste());
					totalAjustes=totalAjustes+rs.getDouble("valorajuste");
				}
				totalSaldo=totalSaldo+rs.getDouble("saldo");
				articulos++;
				index++;
			}
			////////////////////////////////////////////////////////////////////////////////////////////////
			ajustesForm.setServiciosFacturas("numeroarticulos",articulos+"");
			ajustesForm.setServiciosFacturas("numeroregistros",index+"");
			ajustesForm.setServiciosFacturas("totalsaldo",totalSaldo+"");
			ajustesForm.setServiciosFacturas("totalajustes",totalAjustes+"");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}		
	}


	/**
	 * Metodo para pasar la informacion general de un ajuste al mundo.
	 * @param ajustesForm
	 * @param mundo
	 */
	private void cargarAjusteGeneralToMundo(AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo,UsuarioBasico usuario)
	{
		if(ajustesForm.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
		{
			if(Utilidades.obtenerEstadoCuentaCobro(Integer.parseInt(ajustesForm.getCuentaCobroStr()),ajustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteCreditoCuentaCobro);
			}
			else
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteCreditoFactura);
			}
		}
		else if(ajustesForm.getTipoAjusteSeccion3().equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
		{
			if(Utilidades.obtenerEstadoCuentaCobro(Integer.parseInt(ajustesForm.getCuentaCobroStr()),ajustesForm.getInstitucion())==ConstantesBD.codigoEstadoCarteraRadicada)
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteDebitoCuentaCobro);
			}
			else
			{
				mundo.setTipoAjuste(ConstantesBD.codigoAjusteDebitoFactura);
			}			
		}
		mundo.setInstitucion(ajustesForm.getInstitucion());
		mundo.setCastigoCartera(ajustesForm.isCastigoCarteraS3());
		mundo.setConceptoCastigoCartera((!ajustesForm.isCastigoCarteraS3())?"":ajustesForm.getConceptoCastigoCarteraS3());
		mundo.setFechaAjuste(ajustesForm.getFechaAjuste());
		mundo.setFechaElaboracion(ajustesForm.getFechaElaboracion());
		mundo.setHoraElaboracion(ajustesForm.getHoraElaboracion());
		mundo.setUsuario(usuario.getLoginUsuario());
		mundo.setCuentaCobro(ajustesForm.getCuentaCobro());
		mundo.setConceptoAjuste(ajustesForm.getConceptoAjuste());
		mundo.setMetodoAjuste(ajustesForm.getMetodoAjuste());
		mundo.setValorAjuste(ajustesForm.getValorAjuste());
		mundo.setObservaciones(ajustesForm.getObservaciones());
		mundo.setEstado(ajustesForm.getEstadoAjuste());
	}
	/**
	 * este metodo solo carga los datos consultados, no se cargar los datos que ya se capturaron, no es nesesario.
	 * @param ajustesForm
	 * @param mundo
	 * @param indice, indice de la posicion en el mapa.
	 * @return nuevo valor para el indice, o numero de facturas en el mapa.
	 */
	private void cargarInformacionGeneralToForm(AjustesEmpresaForm ajustesForm, AjustesEmpresa mundo) 
	{

		if(ajustesForm.isAjustePorCuentaCobro())
		{

			ajustesForm.setCuentaCobro(mundo.getCuentaCobro());
			ajustesForm.setConvenio(new InfoDatosInt(mundo.getConvenio().getCodigo(),mundo.getConvenio().getNombre()));
			ajustesForm.setSaldo(mundo.getSaldoCuentCobro()); 
			ajustesForm.setConceptoCastigoCarteraS3(mundo.getConceptoCastigoCartera()==null?ConstantesBD.codigoNuncaValido+"":mundo.getConceptoCastigoCartera());
		}
		else if(ajustesForm.isAjustePorFactura())
		{
			ajustesForm.setConvenio(new InfoDatosInt(mundo.getAjustesFacturaEmpresa().getConvenio().getCodigo(),mundo.getAjustesFacturaEmpresa().getConvenio().getNombre()));
			ajustesForm.setSaldo(mundo.getAjustesFacturaEmpresa().getSaldoFactura());
            ajustesForm.setTotalFactura(mundo.getAjustesFacturaEmpresa().getTotalFactura());
            ajustesForm.setCuentaCobro(ConstantesBD.codigoNuncaValido);
			//ajustesForm.setCuentaCobro(Utilidades.obtenerCuentaCobroFactura(ajustesForm.getCodigoFactura()));
			ajustesForm.setConceptoCastigoCarteraS3(mundo.getConceptoCastigoCartera()==null?ConstantesBD.codigoNuncaValido+"":mundo.getConceptoCastigoCartera());
			ajustesForm.setConsecutivoFac(mundo.getAjustesFacturaEmpresa().getConsecutivoFactura()+"");
			ajustesForm.setCodigoCentroAtencion(mundo.getAjustesFacturaEmpresa().getCodigoCentroAtencion());
			ajustesForm.setNombreCentroAtencion(mundo.getAjustesFacturaEmpresa().getNombreCentroAtencion());
			
		}
		
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
