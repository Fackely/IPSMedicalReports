
/*
 * Creado   11/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.cartera;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import java.util.HashMap;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cartera.CuentasCobroForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.CuentasCobro;
import com.princetonsa.mundo.cartera.MovimientosCuentaCobro;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;
import com.princetonsa.pdf.CuentasCobroPdf;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/** 
 * Clase para manejar la generación de CXC, 
 * inactivación CXC, radicación CXC y Movimientos CXC. 
 * Los Movimientos de las CXC son definidos como
 * la modificación, anulación y/o devolución. 
 *
 * @version 1.0, 11/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:armando@PrincetonSA.com">Armando Osorio</a>
 * @author <a href="mailto:dramirez@PrincetonSA.com">Diego Ramirez</a>
 * @author <a href="mailto:sgomez@PrincetonSA.com">Sebastian Gomez</a>
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class CuentasCobroAction extends Action 

{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CuentasCobroAction.class);
	
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{
	    if(form instanceof CuentasCobroForm )
	    {
	        
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			CuentasCobroForm cxcForm = (CuentasCobroForm)form;
			logger.info("forma--------------------------->"+cxcForm.getMotivoAnulacion());
			HttpSession sesion = request.getSession();
			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			CuentasCobro mundoCxC= new CuentasCobro();
			String estado = cxcForm.getEstado();
			logger.info("[CuentasCobroAction] estado->"+estado);
			cxcForm.setMostrarPopUpFacturasMismoConsecutivo(false);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de CuentasCobroAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("inactivacion")){
				//alistar datos para inactivación de factura
				cxcForm.reset();
				//Se remueven atributos de error
				if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
					request.getSession().removeAttribute("conjuntoErroresObjeto");
				this.cerrarConexion(con);
				return mapping.findForward("paginaInactivacion");
				
			}
			else if(estado.equals("empezarGeneracion"))
			{
		        
                if(!Utilidades.isDefinidoConsecutivo(con,usuario.getCodigoInstitucionInt(),ConstantesBD.nombreConsecutivoCuentasCobro))
                {
                	logger.warn("FALTA DEFINIR EL COSECUTIVO CUENTA COBRO Verifique. Proceso Cancelado.");
                	request.setAttribute("codigoDescripcionError", "error.faltaConsecutivoCuentaCobro");
                	this.cerrarConexion(con);
                	return mapping.findForward("paginaError");
                }
                else
                {
    				String numTras=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentasCobro,usuario.getCodigoInstitucionInt());
    		        if(Integer.parseInt(numTras.trim().equals("")?ConstantesBD.codigoNuncaValido+"":numTras)==ConstantesBD.codigoNuncaValido)
    		        {            
                    	logger.warn("FALTA DEFINIR EL COSECUTIVO CUENTA COBRO Verifique. Proceso Cancelado.");
                    	request.setAttribute("codigoDescripcionError", "error.faltaConsecutivoCuentaCobro");
                    	this.cerrarConexion(con);
                    	return mapping.findForward("paginaError");
    		        }
                }
				cxcForm.reset();
				this.cerrarConexion(con);
				return mapping.findForward("generarCuentaCobro");
				
			}
			else if(estado.equals("volverPaginaPrincipal"))
			{
				this.cerrarConexion(con);
				return mapping.findForward("generarCuentaCobro");
			}
			else if(estado.equals("continuarGeneracion"))
			{
				accionContinuarGeneracion(con,mundoCxC,cxcForm,usuario);
				cerrarConexion(con);
				if(cxcForm.getNumeroFacturasCxC()==0 && cxcForm.getNumeroFacturasNoAptas()==0)
				{
					cxcForm.setMostrarMensaje(true);
					response.sendRedirect("generacionCuentasCobroXRango.jsp");
					return null;
				}
				return mapping.findForward("facturasEncontradas");
			}
			else if(estado.equals("generarCuenta"))
			{
				return accionGenerarCuenta(con,mundoCxC,cxcForm,usuario,response,request,mapping);
			}
			else if(estado.equals("resumenCuentaCobro"))
			{
				accionResumenGeneralCuentaCobro(con,mundoCxC,cxcForm,usuario,true);
				cerrarConexion(con);
				return mapping.findForward("resumenIngresoCuentaCobro");
			}
			else if(estado.equals("detalleCuentaResumen"))
			{
				ValoresPorDefecto.cargarValoresIniciales(con);
				cerrarConexion(con);
				cxcForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("resumenIngresoCuentaCobroDetalle");
			}
			else if(estado.equals("detalleCuenta"))
			{
				ValoresPorDefecto.cargarValoresIniciales(con);
				cerrarConexion(con);
				cxcForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("detalleFacturasConvenio");
			}
			else if(estado.equals("seleccionarTodos"))
			{
				cerrarConexion(con);
				return accionSeleccionarTodasFacturas(response,cxcForm);
			}
			else if(estado.equals("volverFacturasEncontradas"))
			{
				int cont=0;
				for(int i=0;i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+"");i++)
				{
					if(UtilidadTexto.getBoolean(cxcForm.getFacturasCxC().get("seleccionado_"+i).toString()))
					{
						cont++;
					}
				}
				cxcForm.setNumeroFacturasSeleccionadas(cont);
				cerrarConexion(con);//
				response.sendRedirect("facturasEncontradas.jsp");
				return null;
			}
			///////////estados para genera la cuenta de cobro para facturas/////////////
			else if(estado.equals("empezarXFactura"))
			{   
                if(!Utilidades.isDefinidoConsecutivo(con,usuario.getCodigoInstitucionInt(),ConstantesBD.nombreConsecutivoCuentasCobro))
                {
                	logger.warn("FALTA DEFINIR EL CONSECUTIVO CUENTA COBRO Verifique. Proceso Cancelado.");
                	request.setAttribute("codigoDescripcionError", "error.faltaConsecutivoCuentaCobro");
                	this.cerrarConexion(con);
                	return mapping.findForward("paginaError");
                }
                else
                {
    				String numTras=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentasCobro,usuario.getCodigoInstitucionInt());
    		        if(Integer.parseInt(numTras.trim().equals("")?ConstantesBD.codigoNuncaValido+"":numTras)==ConstantesBD.codigoNuncaValido)
    		        {            
                    	logger.warn("FALTA DEFINIR EL COSECUTIVO CUENTA COBRO Verifique. Proceso Cancelado.");
                    	request.setAttribute("codigoDescripcionError", "error.faltaConsecutivoCuentaCobro");
                    	this.cerrarConexion(con);
                    	return mapping.findForward("paginaError");
    		        }
                }
				cxcForm.reset();
				this.cerrarConexion(con);
				return mapping.findForward("generarCuentaCobro");
			}
			else if(estado.equals("cargarFactura"))
			{
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
				{					
					cxcForm.setFacturasMismoConsecutivo(Utilidades.consultarFacturasMismoConsecutivo(con,Integer.parseInt(cxcForm.getConsecutivoFac()),usuario.getCodigoInstitucionInt()));
									
					if(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("numRegistros")+"")>1)
					{
						cxcForm.setMostrarPopUpFacturasMismoConsecutivo(true);
						this.cerrarConexion(con);
						return mapping.findForward("generarCuentaCobro");
					}
					else
					{
						cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_0")+""));
						cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_0")+"");
						return accionCargarFacturas(con,mapping,mundoCxC,cxcForm,usuario,request);
					}
				}				
				else
				{
					return accionCargarFacturaCuentaCobroXFactura(con,mapping,mundoCxC,cxcForm,usuario,request);
				}
				
			}
			else if(estado.equals("cargarFactura_1"))
			{
				cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_"+cxcForm.getIndiceConsecutivoCargar())+""));
				cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_"+cxcForm.getIndiceConsecutivoCargar())+"");
				return accionCargarFacturas(con,mapping,mundoCxC,cxcForm,usuario,request);
				
			}
			else if(estado.equals("cargarFactura_radicacion"))
			{
				cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_"+cxcForm.getIndiceConsecutivoCargar())+""));
				cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_"+cxcForm.getIndiceConsecutivoCargar())+"");
				return accionBuscarCuentaCobroFacturaFija(con, cxcForm, mapping, request, estado, usuario);
				
			}
			else if(estado.equals("generarXFacturas"))
			{
				return acccionGuardarCuentaCobroXFactura(con,mundoCxC,cxcForm,usuario,response,request,mapping);
			}
			else if(estado.equals("resumenCuentaCobroFactura"))
			{
				//manejamos una variable temporal para almacenar el consecutivo
				//de la factura, ya que si no lo hacemos asi toca iterar la coleccion
				//de facturas para sacar el consecutivo, y como siempre va ha se una 
				//sola factura es una perdida de tiempo  y recursos la iteracion.
				String consecutivoFactura=cxcForm.getConsecutivoFac();
				accionResumenGeneralCuentaCobro(con,mundoCxC,cxcForm,usuario,true);
				cxcForm.setConsecutivoFac(consecutivoFactura);
				cerrarConexion(con);
				return mapping.findForward("resumenCuentaCobroFactura");
			}
			else if(estado.equals("imprimirDetallado"))
			{
			    return this.accionImprimirDetalle(mapping, request, con, cxcForm, usuario, mundoCxC);
			}
			//////FIN estados para genera la cuenta de cobro para facturas/////////////
			else if(estado.equals("empezarModificacion"))
			{				
				cxcForm.reset();
				this.cerrarConexion(con);
				return mapping.findForward("paginaModificacion");
				
			}
			else if(estado.equals("buscarFactura"))
			{
				//Se Valida si el Estado las Glosas correspondientes a la Factura son Aprobadas o Respondidas
				
				cxcForm.setEstadoGlosaFacturaMap(CuentasCobro.estadoGlosasFacturas(con, cxcForm.getConsecutivoFactura()));			
				int numRegistros = Utilidades.convertirAEntero(cxcForm.getEstadoGlosaFacturaMap("numRegistros")+"");
				
				if(numRegistros>0)
				{
					ActionErrors errores = new ActionErrors();;
					for (int i=0;i<numRegistros;i++)
					{
						if (((!cxcForm.getEstadoGlosaFacturaMap("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada))
							||(!cxcForm.getEstadoGlosaFacturaMap("estado_"+i).equals(ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida)))
							&&UtilidadTexto.getBoolean(ValoresPorDefecto.getAjustarCuentaCobroRadicada(Utilidades.convertirAEntero(usuario.getCodigoInstitucion()))))
						{
							errores.add("",new ActionMessage("error.facturaPermiteAjustarCXCRadicadasEstadoGlosaNoAproNoResp"));
							saveErrors(request, errores);
							this.cerrarConexion(con);
							return mapping.findForward("paginaInactivacion");
						}
					}	
				}
				
				//Fin Validación de los Estados de las Glosas
				
				//Validacion Anexo 712
				//Valida si el parametro general Requiere Glosa Inactivar se encuentra en SI. Si es así, luego valida que la factura a inactivar debe poseer una glosa en estado respondida tipo devoculión
				//y que además no tenga ajustes asociados, y si los tiene que estén en estado ANULADO.
				int resultado=CuentasCobro.requiereGlosaInactivar(con, cxcForm.getConsecutivoFactura());
				if (resultado>0)
				{
					String mensaje="";
					if(resultado==1)
						mensaje="Es requerido que la factura tenga registrada una Glosa en estado Respondida tipo Devolución.";
					else
						mensaje="La factura tiene ajustes asociados a la glosa en estado diferente a anulado.";
					ActionErrors errores = new ActionErrors();
					errores.add("",new ActionMessage("error.errorEnBlanco",mensaje));
					saveErrors(request, errores);
					this.cerrarConexion(con);
					return mapping.findForward("paginaInactivacion");
				}
				//Fin Validacion Anexo 712
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
				{
					cxcForm.setFacturasMismoConsecutivo(Utilidades.consultarFacturasMismoConsecutivo(con,cxcForm.getConsecutivoFactura(),usuario.getCodigoInstitucionInt()));
					if(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("numRegistros")+"")>1)
					{
						cxcForm.setMostrarPopUpFacturasMismoConsecutivo(true);
						this.cerrarConexion(con);
						return mapping.findForward("paginaInactivacion");
					}
					else
					{
						cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_0")+""));
						cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_0")+"");
						return accionBuscarFactura(con,cxcForm,request,mapping,estado,usuario,false);
					}
					
				}
				else
				{
					return accionBuscarFactura(con,cxcForm,request,mapping,estado,usuario,true);
				}
				
				
			}
			else if(estado.equals("buscarFactura_Inactivacion"))
			{
				cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_"+cxcForm.getIndiceConsecutivoCargar())+""));
				cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_"+cxcForm.getIndiceConsecutivoCargar())+"");
				cxcForm.setEstado("buscarFactura");
				return accionBuscarFactura(con,cxcForm,request,mapping,cxcForm.getEstado(),usuario,false);
				
			}
			else if(estado.equals("guardarInactivacion"))
			{
				return accionGuardarInactivacion(con,cxcForm,mapping,request,estado,usuario);
				
			}
			else if(estado.equals("radicacion"))
			{
				cxcForm.reset();
				//Se remueven atributos de error
				if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
					request.getSession().removeAttribute("conjuntoErroresObjeto");
				this.cerrarConexion(con);
				return mapping.findForward("paginaRadicacion");
			}
			else if(estado.equals("buscarCuentaCobro"))
			{
				return accionBuscarCuentaCobro(con,cxcForm,mapping,request,estado,usuario);
				
			}
			else if(estado.equals("guardarRadicacion"))
			{
				return accionGuardarRadicacion(con,cxcForm,estado,usuario,mapping,request);
				
			}
			else if(estado.equals("cerrarPopUp")) {
				//se remueven otras variables en sesion
				this.removerDatosSessionPopUp(request);
				
				this.cerrarConexion(con);
				return mapping.findForward("paginaRadicacion");
			}
			else if(estado.equals("iniciarBusqueda"))
			{
				cxcForm.reset();
				this.cerrarConexion(con);
				return mapping.findForward("paginaConsulta");
			}
			
			else if(estado.equals("imprimir"))
			{
				return this.accionImprimir(mapping, request, con, cxcForm, usuario, mundoCxC);
			}
			else if(estado.equals("continuarMovimiento"))
			{
			  return this.liberarRelacionarFacturas (con,mapping,cxcForm,usuario);		
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.cerrarConexion(con);
				response.sendRedirect(cxcForm.getLinkSiguiente());
				return null;
			}
			else if (estado.equals("modificarMovimiento"))
			{
			    return this.guardarSalirModificaciones (con,mapping,cxcForm,usuario,request);
			}
			else if (estado.equals("volverMovimiento"))
			{			    
			    this.cerrarConexion(con);
				return mapping.findForward("paginaModificacion");
			}	
			else if (estado.equals("seleccTodasMovimientos"))
			{			   				
			    return this.seleccionarMovimientoTodas (con,mapping,cxcForm);			    
			}
			else if (estado.equals("anularMovimiento"))
			{			    
			    return this.anularCuentaCobro (con,mapping,cxcForm,request,usuario,true,false);			    	    
			}
			else if (estado.equals("actualizarPaginaMovimiento"))
			{			   				
			    return this.validacionesDevolucion(con,mapping,cxcForm,usuario);	   			    
			}	
			else if (estado.equals("devolverMovimiento"))
			{			   				
			    return this.anularCuentaCobro (con,mapping,cxcForm,request,usuario,false,true);
			}
			else if(estado.equals("recargarBusqueda"))
			{
				return accionRecargarBusqueda(con,cxcForm,mundoCxC,mapping,usuario);
				
			}
			else if(estado.equals("detalleBusqueda"))
			{
				return this.detalleBusqueda( mapping,  con, cxcForm);
			}
			else if(estado.equals("busquedaDetalleFacturas"))
			{
				return this.busquedaDetalleFacturas( mapping, con, cxcForm, mundoCxC,usuario);
			}
			else if(estado.equals("aprobarCuentaCobro"))
			{
				return this.accionAprobarCuentaCobro(con,cxcForm,mundoCxC,mapping,usuario);
			}
			else
			{
				UtilidadBD.cerrarConexion(con);
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
	    }
	    else
		{
			logger.error("El form no es compatible con el form de CuentasCobro");
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
	 * Método que realiza la busqueda de cuenta de cobro
	 * @param con
	 * @param cxcForm
	 * @param mundoCxC
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
    private ActionForward accionAprobarCuentaCobro(Connection con,CuentasCobroForm forma, CuentasCobro mundo, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	logger.info("\n\nnumero cuenta cobro::: "+forma.getNumCuentaCobro());
    	mundo.aprobarCuentaCobro(con,Utilidades.convertirAEntero(forma.getNumCuentaCobro()+""),usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
		
    	mundo.reset();
 	    forma.reset();			  
    	return mapping.findForward("paginaModificacion");
	}
	
	private ActionForward accionCargarFacturas(Connection con, ActionMapping mapping, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{
			ActionErrors errores = new ActionErrors();
			String[] estadoVector=Utilidades.obtenerEstadoFactura(cxcForm.getCodigoFactura()).split(ConstantesBD.separadorSplit);
			if(Integer.parseInt(estadoVector[0])==ConstantesBD.codigoEstadoFacturacionFacturada)
			{
				if(ValidacionesFactura.facturaTieneAjustesPendientes(cxcForm.getCodigoFactura()))
				{
					errores.add("factura con ajustes", new ActionMessage("error.facturaConAjustesPendientes"));
				}
				if(!ValidacionesFactura.facturaTieneSaldoPendiente(cxcForm.getCodigoFactura()))
				{
					errores.add("factura saldo cero", new ActionMessage("error.facturaSaldoPendienteCero"));
				}
				if(Utilidades.obtenerCuentaCobroFactura(cxcForm.getCodigoFactura())!=ConstantesBD.codigoNuncaValido)
				{
					errores.add("tiene cuenta de cobro", new ActionMessage("error.facturaTienCuentaCobro"));
				}
			}
			else
			{
				errores.add("estado diferente", new ActionMessage("error.facturaEstadoDiferenteFacturada",estadoVector[1]));
			}
			//si encontro errores en la factura.
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				this.cerrarConexion(con);
				return mapping.findForward("generarCuentaCobro");
			}
		}

		
		if(UtilidadValidacion.esConvenioCapitado(con,Utilidades.obtenerConvenioFactura(con,cxcForm.getCodigoFactura()).getCodigo()))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Factura Convenio Capitado", new ActionMessage("error.facturaConvenioCapitado"));
			cerrarConexion(con);
			saveErrors(request, errores);
            return mapping.findForward("generarCuentaCobro");
		}
		mundoCxC.cargarFacutraParaCxC(con,cxcForm.getCodigoFactura());
		cxcForm.setConsecutivoFac(mundoCxC.getConsecutivoFac());
		cxcForm.setFechaElaboracionFactura(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaElaboracionFactura()));
		cxcForm.setValorFactura(mundoCxC.getValInicialCuenta()+"");
		cxcForm.setConvenio(mundoCxC.getConvenio().getCodigo()+ConstantesBD.separadorSplit+mundoCxC.getConvenio().getNombre());
		cxcForm.setViaIngresoFactura(mundoCxC.getViaIngreso().getCodigo()+ConstantesBD.separadorSplit+mundoCxC.getViaIngreso().getNombre());
		cxcForm.setCodigoCentroAtencion(mundoCxC.getCodigoCentroAtencion());
		cxcForm.setNombreCentroAtencion(mundoCxC.getNombreCentroAtencion());
		this.cerrarConexion(con);
		return mapping.findForward("generarCuentaCobro");
	}


	/**
	 * Método que realiza la busqueda de cuenta de cobro
	 * @param con
	 * @param cxcForm
	 * @param mundoCxC
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
    private ActionForward accionRecargarBusqueda(Connection con, CuentasCobroForm cxcForm, CuentasCobro mundoCxC, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	cxcForm.setNumCuentaCobro(Double.parseDouble(cxcForm.getMapMovimientos("codigoCXC")+""));
    	String nombreConvenio = cxcForm.getMapMovimientos("nombreConvenio")+"";
		accionResumenGeneralCuentaCobro(con, mundoCxC, cxcForm, usuario, false);
		mundoCxC.cargarMotivoAnulacion(con,cxcForm.getNumCuentaCobro());
		cxcForm.setEstado("recargarBusqueda");
		this.llenarForm(mundoCxC,cxcForm,cxcForm.getEstado());
		cxcForm.setConvenio(nombreConvenio);
		this.cerrarConexion(con);
		return mapping.findForward("paginaConsulta");
	}


	/**
     * @param mapping
     * @param request
     * @param con
     * @param cxcForm
     * @param usuario
     * @param mundoCxC
     * @return
     */
    private ActionForward accionImprimirDetalle(ActionMapping mapping, HttpServletRequest request, Connection con, CuentasCobroForm cxcForm, UsuarioBasico usuario, CuentasCobro mundoCxC) 
    {
    	cxcForm.setMapMovimientos("codigoCXC",cxcForm.getNumCuentaCobro()+"");
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		cxcForm.setImpresionAnexos(false);
		CuentasCobroPdf.imprimir(cxcForm, usuario, con, mundoCxC, ValoresPorDefecto.getFilePath()+ nombreArchivo,null, request);
		this.cerrarConexion(con);
		
			
		request.setAttribute("nombreArchivo",nombreArchivo);
		request.setAttribute("nombreVentana", "Cuentas de Cobro X Factura");
		UtilidadBD.closeConnection(con);	
		return mapping.findForward("abrirPdf");
		
    }


    /**
     * Método para radicar una cuenta de cobro
	 * @param con
	 * @param cxcForm
	 * @param estado
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarRadicacion(Connection con, CuentasCobroForm cxcForm, String estado, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		//estado reservado para el registro de radicación de cuenta de cobro
		MovimientosCuentaCobro movimientosCuentaCobro=new MovimientosCuentaCobro();
		this.llenarMundo(movimientosCuentaCobro,cxcForm,estado,usuario);
		
		//se registra la radicacion
		int resp=movimientosCuentaCobro.insertarRadicacion(con,usuario.getCodigoInstitucionInt());
		
		//revisión si el ingreso fue exitoso
		if(resp<=0){
			//Se gestiona error de fallido de inactivacion
			Collection errores=new ArrayList();
			//Se agrega la etiqueta de error a un objeto ElementoApResource
			ElementoApResource elem=new ElementoApResource("error.radicacion.noGuardoRadicacion");
			errores.add(elem);
			//Se suben errores a la sesión
			request.getSession().setAttribute("conjuntoErroresObjeto",errores);
		}
		
		//se cierra la conexion
		this.cerrarConexion(con);
		return mapping.findForward("resumenRadicacion");
	
	}



	/**
     * Método para cargar en el formulario una cuenta de cobro que va a ser radicada
	 * @param con
	 * @param cxcForm
	 * @param mapping
	 * @param request
	 * @param estado
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarCuentaCobro(Connection con, CuentasCobroForm cxcForm, ActionMapping mapping, HttpServletRequest request, String estado, UsuarioBasico usuario) {
		//		Se remueven atributos de error
		if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
			request.getSession().removeAttribute("conjuntoErroresObjeto");
		//se remueven otras variables en sesion
		this.removerDatosSessionPopUp(request);
		//*******SECCIÓN DE VALIDACIONES************************
		//variables a usar
		MovimientosCuentaCobro movimientosCuentaCobro=new MovimientosCuentaCobro();
		String mensaje_error="";
		
		//verificar cuál fue el criterio de busqueda
		if(cxcForm.getNumCuentaCobro()==0.0)
		{
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
			{
				cxcForm.setFacturasMismoConsecutivo(Utilidades.consultarFacturasMismoConsecutivo(con,cxcForm.getConsecutivoFactura(),usuario.getCodigoInstitucionInt()));
				if(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("numRegistros")+"")>1)
				{
					cxcForm.setMostrarPopUpFacturasMismoConsecutivo(true);
					this.cerrarConexion(con);
					return mapping.findForward("paginaRadicacion");
				}
				else
				{
					cxcForm.setCodigoFactura(Utilidades.convertirAEntero(cxcForm.getFacturasMismoConsecutivo("codigo_0")+""));
					cxcForm.setNombreEntidadFactura(cxcForm.getFacturasMismoConsecutivo("entidad_0")+"");
					mensaje_error=prepararRadicacion(con,movimientosCuentaCobro,cxcForm,usuario,mensaje_error,false);
				}
			}
			else
			{
				mensaje_error=prepararRadicacion(con,movimientosCuentaCobro,cxcForm,usuario,mensaje_error,false);
			}
		}
		else
		{
			cxcForm.setMostrarCentroAtencion(false);
			//validando cuando se busca por numero de cuenta de cobro
			movimientosCuentaCobro.setNumCuentaCobro(cxcForm.getNumCuentaCobro());
			mensaje_error=movimientosCuentaCobro.validacionRadicacion(con,usuario.getCodigoInstitucionInt());
		}
		//*********SECCIÓN DE INGRESO DE LOS ERRORES *******************************
		//ingreso de los errores:
		//si no hay errores entonces se cargan los datos
		logger.info("2222-mensaje_error----------------->"+mensaje_error);
		if(!this.editarErrores(mensaje_error,request)){
			//búsqueda de los datos de la cuenta a radicar
			if(movimientosCuentaCobro.cargarCuentaCobro(con,usuario.getCodigoInstitucionInt())>0)
				this.llenarForm(movimientosCuentaCobro,cxcForm,estado);
		}
		//de lo contrario se inicia la radicación
		else{
			cxcForm.setEstado("radicacion");
			cxcForm.setFechaRadicacion("");
			cxcForm.setNumeroRadicacion("");
			cxcForm.setObservacionesRadicacion("");
			cxcForm.setNumCuentaCobro(0);
		}
		//**************************************************************
		this.cerrarConexion(con);
		return mapping.findForward("paginaRadicacion");
	}


	/**
     * Método para cargar en el formulario una cuenta de cobro que va a ser radicada
	 * @param con
	 * @param cxcForm
	 * @param mapping
	 * @param request
	 * @param estado
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarCuentaCobroFacturaFija(Connection con, CuentasCobroForm cxcForm, ActionMapping mapping, HttpServletRequest request, String estado, UsuarioBasico usuario) 
	{
		cxcForm.setEstado("buscarCuentaCobro");
		//Se remueven atributos de error
		if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
			request.getSession().removeAttribute("conjuntoErroresObjeto");
		//se remueven otras variables en sesion
		this.removerDatosSessionPopUp(request);
		//*******SECCIÓN DE VALIDACIONES************************
		//variables a usar
		MovimientosCuentaCobro movimientosCuentaCobro=new MovimientosCuentaCobro();
		String mensaje_error="";
		
		InfoDatosInt centroA = Factura.obtenerCentroAtencionFactura(con,cxcForm.getCodigoFactura());
		if(centroA.getCodigo()>0&&!centroA.getNombre().trim().equals(""))
		{
			cxcForm.setCentroAtencion(centroA.getCodigo()+ConstantesBD.separadorSplit+centroA.getNombre());
			cxcForm.setMostrarCentroAtencion(false);
		}
		
		//se verifica si se obtuvo el numero de la cuenta de cobro
		double numCuentaCobro=Factura.obtenerCuentaCobro(con,cxcForm.getCodigoFactura());
		if(numCuentaCobro>0)
		{
			cxcForm.setNumCuentaCobro(numCuentaCobro);
			//se alistan datos del numero de cuenta de cobro
			movimientosCuentaCobro.setNumCuentaCobro(cxcForm.getNumCuentaCobro());
			mensaje_error=movimientosCuentaCobro.validacionRadicacion(con,usuario.getCodigoInstitucionInt());
		}
		else
		{
			if(cxcForm.getCodigoFactura()>0)
			{
				//la factura no posee cuenta de cobro
				mensaje_error+="error.radicacion.facturaSinCuentaCobro@"+cxcForm.getConsecutivoFactura();
			}
			else
			{
				//la factura no existe
				mensaje_error+="errors.noExiste@La Factura "+cxcForm.getConsecutivoFactura();
			}
		}
		movimientosCuentaCobro.setConsecutivoFactura(cxcForm.getConsecutivoFactura());

		//*********SECCIÓN DE INGRESO DE LOS ERRORES *******************************
		//ingreso de los errores:
		//si no hay errores entonces se cargan los datos
		if(!this.editarErrores(mensaje_error,request)){
			//búsqueda de los datos de la cuenta a radicar
			if(movimientosCuentaCobro.cargarCuentaCobro(con,usuario.getCodigoInstitucionInt())>0)
			{
				this.llenarForm(movimientosCuentaCobro,cxcForm,cxcForm.getEstado());
			}
		}
		//de lo contrario se inicia la radicación
		else{
			cxcForm.setEstado("radicacion");
			cxcForm.setFechaRadicacion("");
			cxcForm.setNumeroRadicacion("");
			cxcForm.setObservacionesRadicacion("");
			cxcForm.setNumCuentaCobro(0);
		}
		//**************************************************************
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaRadicacion");
	}

	private String prepararRadicacion(Connection con, MovimientosCuentaCobro movimientosCuentaCobro, CuentasCobroForm cxcForm, UsuarioBasico usuario, String mensaje_error, boolean buscarCodCuentaCobro) 
	{
		// Modificado por tarea 142151
		cxcForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(cxcForm.getConsecutivoFactura(),usuario.getCodigoInstitucionInt()));
		/*if(buscarCodCuentaCobro)
			cxcForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(cxcForm.getConsecutivoFactura(),usuario.getCodigoInstitucionInt()));*/
		
		InfoDatosInt centroA = Factura.obtenerCentroAtencionFactura(con,cxcForm.getCodigoFactura());
		if(centroA.getCodigo()>0&&!centroA.getNombre().trim().equals(""))
		{
			cxcForm.setCentroAtencion(centroA.getCodigo()+ConstantesBD.separadorSplit+centroA.getNombre());
			cxcForm.setMostrarCentroAtencion(false);
		}
		
		//se verifica si se obtuvo el numero de la cuenta de cobro
		double numCuentaCobro=Factura.obtenerCuentaCobro(con,cxcForm.getCodigoFactura());
		if(numCuentaCobro>0)
		{
			cxcForm.setNumCuentaCobro(numCuentaCobro);
			//se alistan datos del numero de cuenta de cobro
			movimientosCuentaCobro.setNumCuentaCobro(cxcForm.getNumCuentaCobro());
			mensaje_error=movimientosCuentaCobro.validacionRadicacion(con,usuario.getCodigoInstitucionInt());
			logger.info("-mensaje_error----------------->"+mensaje_error);
		}
		else
		{
			if(cxcForm.getCodigoFactura()>0)
			{
				//la factura no posee cuenta de cobro
				mensaje_error+="error.radicacion.facturaSinCuentaCobro@"+cxcForm.getConsecutivoFactura();
			}
			else
			{
				//la factura no existe
				mensaje_error+="errors.noExiste@La Factura "+cxcForm.getConsecutivoFactura();
			}
		}
		movimientosCuentaCobro.setConsecutivoFactura(cxcForm.getConsecutivoFactura());
		return mensaje_error;
	}


	/**
     * Método para inactivar una factura de una cuenta de cobro
	 * @param con
	 * @param cxcForm
	 * @param mapping
	 * @param request
	 * @param estado
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarInactivacion(Connection con, CuentasCobroForm cxcForm, ActionMapping mapping, HttpServletRequest request, String estado, UsuarioBasico usuario) {
		//Se carga el usuario que hace la inactivación
		cxcForm.setUsuarioMovimiento(usuario.getLoginUsuario());
		//Se remueven atributos de error
		if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
			request.getSession().removeAttribute("conjuntoErroresObjeto");
		
		//Se toman los datos del formulario (preparación del mundo)
		MovimientosCuentaCobro movimientosCuentaCobro=new MovimientosCuentaCobro();
		this.llenarMundo(movimientosCuentaCobro,cxcForm,estado,usuario);
		
		UtilidadBD.iniciarTransaccion(con);
		
		//se realiza inserción de inactivación de Factura
		int resp=movimientosCuentaCobro.insertarInactivacionFactura(con,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
		//verificación de estado de la transacción
		if(resp>0){
			//se actualiza la inactivación de factura en la cuenta de cobro
			resp=movimientosCuentaCobro.actualizarInactivacionEnCuentaCobro(con,usuario.getCodigoInstitucionInt(),ConstantesBD.continuarTransaccion);
		}
		//verificación de estado de la transacción
		if(resp>0)
		{
			//SE desasigna la cuenta de cobro a la factura
			resp=Factura.desasignarCuentaCobro(con, cxcForm.getCodigoFactura()+"", usuario.getCodigoInstitucionInt());
		}
		
		UtilidadBD.finalizarTransaccion(con);
		
		//Se verifica si la inserción fue exitosa
		if(resp<=0){
			//Se gestiona error de fallido de inactivacion
			Collection errores=new ArrayList();
			//Se agrega la etiqueta de error a un objeto ElementoApResource
			ElementoApResource elem=new ElementoApResource("error.inactivacion.noGuardoInactivacion");
			errores.add(elem);
			//Se suben errores a la sesión
			request.getSession().setAttribute("conjuntoErroresObjeto",errores);
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("resumenInactivacion");
	}



	/**
     * Método para consultar la factura que será inactivada
	 * @param con
	 * @param cxcForm
	 * @param request
	 * @param mapping
     * @param estado
	 * @param usuario
	 * @param asignarCodFactura 
	 * @return
	 */
	private ActionForward accionBuscarFactura(Connection con, CuentasCobroForm cxcForm, HttpServletRequest request, ActionMapping mapping, String estado, UsuarioBasico usuario, boolean asignarCodFactura) {
		//		Se remueven atributos de error
		if(request.getSession().getAttribute("conjuntoErroresObjeto")!=null)
			request.getSession().removeAttribute("conjuntoErroresObjeto");
		
		if(asignarCodFactura)
			cxcForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(cxcForm.getConsecutivoFactura(), usuario.getCodigoInstitucionInt()));
		MovimientosCuentaCobro movimientosCuentaCobro=new MovimientosCuentaCobro();
		movimientosCuentaCobro.setCodigoFactura(cxcForm.getCodigoFactura());
		//Validación de la búsqueda de factura
		String mensajes_error=movimientosCuentaCobro.validacionInactivacionFacturas(con);
		
		
		//ingreso de los errores:
		//si no hay errores entonces se cargan los datos
		if(!this.editarErrores(mensajes_error,request)){
			//búsqueda de los datos de la factura a inactivar
			movimientosCuentaCobro.cargarDatosFactura(con);
			this.llenarForm(movimientosCuentaCobro,cxcForm,estado);
		}
		//de lo contrario se inicia la inactivacion
		else{
			cxcForm.setEstado("inactivacion");
		}
		this.cerrarConexion(con);
		
		return mapping.findForward("paginaInactivacion");
	}



	/**
	 * @param con
	 * @param mundoCxC
	 * @param cxcForm
	 * @param usuario
	 * @param response
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward acccionGuardarCuentaCobroXFactura(Connection con, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) 
	{
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt());
		mundoCxC.setNumeroCuentaCobro(Utilidades.convertirADouble(consecutivo));
		if(UtilidadValidacion.esNumeroCuentaCobroUsado(con,mundoCxC.getNumeroCuentaCobro()))
		{
			ActionErrors errores = new ActionErrors();
			String cuentaCobroTemp=(mundoCxC.getNumeroCuentaCobro()+"").replace(",", "");
			if(cuentaCobroTemp.indexOf(".")>0)
			{
				cuentaCobroTemp=cuentaCobroTemp.substring(0, cuentaCobroTemp.indexOf("."));
			}
			errores.add("", new ActionMessage("error.existeNumeroCuentaCobroEnBD",cuentaCobroTemp));
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);
				cerrarConexion(con);
				saveErrors(request, errores);
	            return mapping.findForward("paginaErroresActionErrors");
			}
		}
		cxcForm.setNumCuentaCobro(mundoCxC.getNumeroCuentaCobro());
		mundoCxC.setFechaElaboracion(UtilidadFecha.getFechaActual());
		mundoCxC.setHoraElaboracion(UtilidadFecha.getHoraActual());
		mundoCxC.setObservacionesGen(cxcForm.getObservaciones());
		mundoCxC.setFechaInicial(cxcForm.getFechaElaboracionFactura());
		mundoCxC.setFechaFinal(cxcForm.getFechaElaboracionFactura());
		mundoCxC.setValInicialCuenta(Double.parseDouble(cxcForm.getValorFactura()));
		mundoCxC.setSaldoCuenta(Double.parseDouble(cxcForm.getValorFactura()));
		String[] conv=cxcForm.getConvenio().split(ConstantesBD.separadorSplit);
		mundoCxC.setConvenio(new InfoDatosInt(Integer.parseInt(conv[0]),conv[1]));
		String[] vias=cxcForm.getViaIngresoFactura().split(ConstantesBD.separadorSplit);
		mundoCxC.setViaIngreso(new InfoDatosInt(Integer.parseInt(vias[0]),vias[1]));
		mundoCxC.setCodigoFactura(cxcForm.getCodigoFactura());
		mundoCxC.setCodigoCentroAtencion(cxcForm.getCodigoCentroAtencion());
		mundoCxC.setNombreCentroAtencion(cxcForm.getNombreCentroAtencion());
		UtilidadBD.iniciarTransaccion(con);
		ArrayList filtro=new ArrayList();
		filtro.add(cxcForm.getCodigoFactura());
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
		ActionErrors errores = new ActionErrors();
		String consecutivoFactura=cxcForm.getConsecutivoFac();
		String[] estadoVector=Utilidades.obtenerEstadoFactura(cxcForm.getCodigoFactura()).split(ConstantesBD.separadorSplit);
		if(Integer.parseInt(estadoVector[0])==ConstantesBD.codigoEstadoFacturacionFacturada)
		{
			if(ValidacionesFactura.facturaTieneAjustesPendientes(con,cxcForm.getCodigoFactura()))
			{
				errores.add("factura con ajustes", new ActionMessage("error.facturaConAjustesPendientes",consecutivoFactura));
			}
			if(!ValidacionesFactura.facturaTieneSaldoPendiente(con, cxcForm.getCodigoFactura()))
			{
				errores.add("factura saldo cero", new ActionMessage("error.facturaCodSaldoPendienteCero",consecutivoFactura));
			}
			if(Utilidades.obtenerCuentaCobroFactura(con,cxcForm.getCodigoFactura())!=ConstantesBD.codigoNuncaValido)
			{
				errores.add("tiene cuenta de cobro", new ActionMessage("error.facturaCodTienCuentaCobro",consecutivoFactura));
			}
		}
		else
		{
			errores.add("estado diferente", new ActionMessage("error.facturaEstado",consecutivoFactura,estadoVector[1]));
		}
		if(!errores.isEmpty())
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			UtilidadBD.abortarTransaccion(con);
			cerrarConexion(con);
			saveErrors(request, errores);
            return mapping.findForward("paginaErroresActionErrors");
		}
		boolean transaccion=mundoCxC.insertarCuentaCobroPorFactura(con,usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
		if(transaccion)
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			UtilidadBD.finalizarTransaccion(con);
			cerrarConexion(con);
		}
		else
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			UtilidadBD.abortarTransaccion(con);
			cerrarConexion(con);
		}
		try {
			response.sendRedirect("generacionCuentasCobro.do?estado=resumenCuentaCobroFactura");
		} catch (IOException e) {
			e.printStackTrace();
		}
		UtilidadBD.abortarTransaccion(con);
		cerrarConexion(con);
		return null;
	}


	/**
	 * @param con
	 * @param mapping
	 * @param mundoCxC
	 * @param cxcForm
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarFacturaCuentaCobroXFactura(Connection con, ActionMapping mapping, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		
		if(UtilidadValidacion.esConvenioCapitado(con,Utilidades.obtenerConvenioFactura(con,cxcForm.getCodigoFactura()).getCodigo()))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Factura Convenio Capitado", new ActionMessage("error.facturaConvenioCapitado"));
			cerrarConexion(con);
			saveErrors(request, errores);
            return mapping.findForward("generarCuentaCobro");
		}
		
		ActionErrors errores = new ActionErrors();
		int facturasEnProcesoAudi=0;
		cxcForm.setCodigoFactura(Utilidades.obtenerCodigoFactura(Integer.parseInt(cxcForm.getConsecutivoFac()),usuario.getCodigoInstitucionInt()));
		
		facturasEnProcesoAudi=mundoCxC.consultarFacturaEnProcesoAudi(cxcForm.getCodigoFactura());
		
		if(facturasEnProcesoAudi > 0)
			errores.add("descripcion",new ActionMessage("prompt.generico","La Factura se Encuentra Asociada a una Pre Glosa Pendiente."));
		
		if (!errores.isEmpty())
			saveErrors(request, errores);
		else {
			mundoCxC.cargarFacutraParaCxC(con,cxcForm.getCodigoFactura());
			cxcForm.reset();
			cxcForm.setConsecutivoFac(mundoCxC.getConsecutivoFac());
			cxcForm.setCodigoFactura(mundoCxC.getCodigoFactura());
			cxcForm.setFechaElaboracionFactura(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaElaboracionFactura()));
			cxcForm.setValorFactura(mundoCxC.getValInicialCuenta()+"");
			cxcForm.setConvenio(mundoCxC.getConvenio().getCodigo()+ConstantesBD.separadorSplit+mundoCxC.getConvenio().getNombre());
			cxcForm.setViaIngresoFactura(mundoCxC.getViaIngreso().getCodigo()+ConstantesBD.separadorSplit+mundoCxC.getViaIngreso().getNombre());
			cxcForm.setCodigoCentroAtencion(mundoCxC.getCodigoCentroAtencion());
			cxcForm.setNombreCentroAtencion(mundoCxC.getNombreCentroAtencion());
			this.cerrarConexion(con);
		}
		return mapping.findForward("generarCuentaCobro");
		
	}


	/**
	 * @param response
	 * @param cxcForm
	 * @return
	 */
	private ActionForward accionSeleccionarTodasFacturas(HttpServletResponse response, CuentasCobroForm cxcForm) 
	{	try 
		{
			for(int i=0;i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+"");i++)
			{
				if(UtilidadTexto.getBoolean(cxcForm.getFacturaCxC("mostrar_"+i)))
					cxcForm.setFacturasCxC("seleccionado_"+i,cxcForm.isSelecTodas()+"");
				
			}
			if(cxcForm.isSelecTodas())
			{
				cxcForm.setValorTotalFacturas(Double.parseDouble((cxcForm.getFacturaCxC("valor_total")+"")!=null&&!(cxcForm.getFacturaCxC("valor_total")+"").equals("null")?(cxcForm.getFacturaCxC("valor_total")+""):"0"));
				cxcForm.setNumeroFacturasSeleccionadas(cxcForm.getNumeroFacturasCxC());
			}
			else
			{
				cxcForm.setValorTotalFacturas(0);
				cxcForm.setNumeroFacturasSeleccionadas(0);
			}
			//evaluar si el url es generado por el pager sino eviarlo a detalleFacturas.jsp
			response.sendRedirect(cxcForm.getLinkSiguiente().indexOf("pager")<0?"detalleFacturas.jsp":cxcForm.getLinkSiguiente());
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param con
	 * @param mundoCxC
	 * @param cxcForm
	 * @param usuario
	 * @param hacerReset 
	 * @return
	 */
	private void accionResumenGeneralCuentaCobro(Connection con, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario, boolean hacerReset) 
	{
		mundoCxC.reset();
		mundoCxC.cargarCuentaCobroCompleta(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());
		if(hacerReset)
			cxcForm.reset();
		cxcForm.setNumCuentaCobro(mundoCxC.getNumeroCuentaCobro());
		cxcForm.setNumeroFacturasCxC(mundoCxC.getNumeroFacturas());
		cxcForm.setValorInicialCuenta(mundoCxC.getValInicialCuenta());
		cxcForm.setConvenio(mundoCxC.getConvenio().getCodigo()+ConstantesBD.separadorSplit+mundoCxC.getConvenio().getNombre());
		cxcForm.setNitConvenio(mundoCxC.getAcronimoIdResponsable());
		cxcForm.setDireccionConvenio(mundoCxC.getDireccionConvenio());
		cxcForm.setTelefonoConvenio(mundoCxC.getTelefonoConvenio());
		ArrayList vias=mundoCxC.getViasIngreso();
		int i=0;
		for(i=0;i<vias.size();i++)
		{
			InfoDatosInt viasIngreso = (InfoDatosInt) vias.get(i);
			cxcForm.setCuentasCobroMapa("codigoViaIngreso_"+i,viasIngreso.getCodigo()+"");
			cxcForm.setCuentasCobroMapa("nombreViaIngreso_"+i,viasIngreso.getNombre()+"");
		}
		cxcForm.setCuentasCobroMapa("numeroViasIngreso",i+"");
		cxcForm.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaInicial()));
		cxcForm.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaFinal()));
		cxcForm.setFechaElaboracion(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaElaboracion()));
		cxcForm.setObservaciones(mundoCxC.getObservacionesGen());
		cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
		cxcForm.setCodigoCentroAtencion(mundoCxC.getCodigoCentroAtencion());
		cxcForm.setNombreCentroAtencion(mundoCxC.getNombreCentroAtencion());
		cxcForm.setEstado("cuentaGenerada");
		cxcForm.setEstado("resumenCuentaCobro");
		
	}


	/**
	 * @param con
	 * @param mundoCxC
	 * @param cxcForm
	 * @param usuario
	 * @param response
	 * @param mapping 
	 * @param request 
	 * @return
	 */
	private ActionForward accionGenerarCuenta(Connection con, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping) 
	{
		try 
		{
			boolean enTransaccion=true;
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt());
			mundoCxC.setNumeroCuentaCobro(Utilidades.convertirADouble(consecutivo));
			if(UtilidadValidacion.esNumeroCuentaCobroUsado(con,mundoCxC.getNumeroCuentaCobro()))
			{
				ActionErrors errores = new ActionErrors();
				String cuentaCobroTemp=(mundoCxC.getNumeroCuentaCobro()+"").replace(",", "");
				if(cuentaCobroTemp.indexOf(".")>0)
				{
					cuentaCobroTemp=cuentaCobroTemp.substring(0, cuentaCobroTemp.indexOf("."));
				}
				errores.add("", new ActionMessage("error.existeNumeroCuentaCobroEnBD",cuentaCobroTemp));
				if(!errores.isEmpty())
				{
					UtilidadBD.abortarTransaccion(con);
					cerrarConexion(con);
					saveErrors(request, errores);
		            return mapping.findForward("paginaErroresActionErrors");
				}
			}
			cxcForm.setNumCuentaCobro(mundoCxC.getNumeroCuentaCobro());
			cargarMundoCuentaCobroIngresar(con,mundoCxC,cxcForm);
			ActionErrors errores = validarGrupoFacturasCXC(con,mundoCxC);
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);
				cerrarConexion(con);
				saveErrors(request, errores);
	            return mapping.findForward("paginaErroresActionErrors");
			}
			enTransaccion=mundoCxC.insertarCuentaCobroPorRangos(con,usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt(),false);
			if(enTransaccion)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			if(!enTransaccion)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			}
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			cxcForm.setValorInicialCuenta(mundoCxC.getValInicialCuenta());
			cerrarConexion(con);
			/*
			//si se quiere ir al resuem de una.
			try {
				response.sendRedirect("generacionCuentasCobro.do?estado=resumenCuentaCobro");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
			*/
			return mapping.findForward("resumenCuentaCobroGenerada");
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		cerrarConexion(con);
		return null;
	}


	/**
	 * 
	 * @param con
	 * @param mundoCxC
	 */
	private ActionErrors validarGrupoFacturasCXC(Connection con, CuentasCobro mundoCxC) 
	{
		ActionErrors errores = new ActionErrors();
		for(int i=0;i<Integer.parseInt(mundoCxC.getFacturaCxC("cantidadFacturas")+"");i++)
		{
			String consecutivoFactura=Utilidades.obtenerConsecutivoFactura(con,Integer.parseInt(mundoCxC.getFacturaCxC("codigo_"+i)+""));
			mundoCxC.getFacturaCxC("codigo_"+i);
			String[] estadoVector=Utilidades.obtenerEstadoFactura(con,Integer.parseInt(mundoCxC.getFacturaCxC("codigo_"+i)+"")).split(ConstantesBD.separadorSplit);
			if(Integer.parseInt(estadoVector[0])==ConstantesBD.codigoEstadoFacturacionFacturada)
			{
				if(ValidacionesFactura.facturaTieneAjustesPendientes(con,Integer.parseInt(mundoCxC.getFacturaCxC("codigo_"+i)+"")))
				{
					errores.add("factura con ajustes", new ActionMessage("error.facturaConAjustesPendientes",consecutivoFactura));
				}
				if(!ValidacionesFactura.facturaTieneSaldoPendiente(con,Integer.parseInt(mundoCxC.getFacturaCxC("codigo_"+i)+"")))
				{
					errores.add("factura saldo cero", new ActionMessage("error.facturaCodSaldoPendienteCero",consecutivoFactura));
				}
				if(Utilidades.obtenerCuentaCobroFactura(con,Integer.parseInt(mundoCxC.getFacturaCxC("codigo_"+i)+""))!=ConstantesBD.codigoNuncaValido)
				{
					errores.add("tiene cuenta de cobro", new ActionMessage("error.facturaCodTienCuentaCobro",consecutivoFactura));
				}
				if (!ValidacionesFactura.validarPreglosaRespondida(con,mundoCxC.getFacturaCxC("codigo_"+i)+""))
				{
					errores.add("no tiene preglosa en estado respondida",new ActionMessage("error.facturaCodNoTienePreglosaRespondida",consecutivoFactura));
				}
			}
			else
			{
				errores.add("estado diferente", new ActionMessage("error.facturaEstado",consecutivoFactura,estadoVector[1]));
			}
		}
		return errores;
		
	}


	/**
	 * Metodo para cargar el mundo con los datos que deben
	 * ser ingresados a la Base de Datos.
	 * @param con 
	 * @param mundoCxC
	 * @param cxcForm
	 */
	private void cargarMundoCuentaCobroIngresar(Connection con, CuentasCobro mundoCxC, CuentasCobroForm cxcForm) 
	{
		mundoCxC.setConvenio(new InfoDatosInt(Integer.parseInt((cxcForm.getConvenio().split(ConstantesBD.separadorSplit))[0])));
		mundoCxC.setFechaInicial(cxcForm.getFechaInicial());
		mundoCxC.setFechaFinal(cxcForm.getFechaFinal());
		mundoCxC.setObservacionesGen(cxcForm.getObservaciones());
		mundoCxC.setFechaElaboracion(UtilidadFecha.getFechaActual());
		mundoCxC.setHoraElaboracion(UtilidadFecha.getHoraActual());
		mundoCxC.setCodigoCentroAtencion(cxcForm.getCodigoCentroAtencion());
		mundoCxC.setNombreCentroAtencion(cxcForm.getNombreCentroAtencion());
		ArrayList viasIngreso=new ArrayList();
		mundoCxC.setViasIngreso(new ArrayList());
		for(int i=0;i<Integer.parseInt(cxcForm.getCuentasCobroMapa("numeroViasIngreso")+"");i++)
		{
			//int facViaIngreso=Integer.parseInt(cxcForm.getCuentasCobroMapa("cantidadFacturasXViaIngreso_"+i)+"");
			//if(facViaIngreso>0&&UtilidadTexto.getBoolean(cxcForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))
			if(UtilidadTexto.getBoolean(cxcForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))
				viasIngreso.add(cxcForm.getCuentasCobroMapa("codigoViaIngreso_"+i));
		}
		
		mundoCxC.setViasIngreso(viasIngreso);
		int nF=0;
		double valorTotal=0;
		mundoCxC.getFacturasCxC().clear();
		for(int f=0;f<cxcForm.getNumeroFacturasCxC();f++)
		{
			if(UtilidadTexto.getBoolean(cxcForm.getFacturaCxC("seleccionado_"+f)+""))
			{
				mundoCxC.setFacturaCxC("codigo_"+nF,cxcForm.getFacturaCxC("codigo_"+f));
				//ArrayList filtro=new ArrayList();
				//filtro.add(cxcForm.getFacturaCxC("codigo_"+f));
				//UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
				valorTotal=valorTotal+Double.parseDouble(cxcForm.getFacturaCxC("valor_"+f)+"");
				nF++;
			}
		}
		cxcForm.setValorTotalFacturas(valorTotal);
		mundoCxC.setValInicialCuenta(cxcForm.getValorTotalFacturas());
		//en el momento de generar la cuenta de cobro el saldo es igual al valor
		mundoCxC.setSaldoCuenta(cxcForm.getValorTotalFacturas());
		mundoCxC.setFacturaCxC("cantidadFacturas",nF+"");
	}
	
	
	/**
	 * Metodo para realizar las validaciones para 
	 * anular una CXC.
     * @param con Connection, conexión con la fuente de datos
     * @param mapping ActionMapping para la navegación
     * @param request HttpServletRequest manejo de peticiones
     * @param cxcForm CuentasCobroForm
	 * @param usuario
     * @return findForward paginaModificacion
     */
    private ActionForward validacionesDevolucion(Connection con, 
														            ActionMapping mapping, 
														            CuentasCobroForm cxcForm, UsuarioBasico usuario) 
    {
        CuentasCobro mundoCXC = new CuentasCobro ();  
        cxcForm.setMapValidaciones("numErrores",-1+"");
        String numCXCStr=cxcForm.getNumCuentaCobro()+"";
        int index=numCXCStr.indexOf(".");
        if(index!=-1)
        {
            String eliminarDecimal = numCXCStr.substring(0,index);                
            cxcForm.setMapMovimientos("codigoCXC",eliminarDecimal);
        }
        
        if(Integer.parseInt(cxcForm.getMapMovimientos("codigoEstadoCXC")+"") == ConstantesBD.codigoEstadoCarteraRadicada)
        {
	        int contErr = 0;
	        boolean existeError=false,existeError3=false;
	        boolean existeError4=false,existeError5=false,existeError6=false;
	        mundoCXC.validacionesDevolucionCxc(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());	        
	        cxcForm.getMapValidaciones().putAll(mundoCXC.getMapMovimientos());
	        
	        for(int k=0;k<=Integer.parseInt(cxcForm.getMapValidaciones("numFact")+"");k++)
		    {	
	            if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("existeGlosa_"+k)+"") && !existeError)
		          {
		              cxcForm.setMapValidaciones("descripcionError_"+contErr,"La cuenta de cobro "+cxcForm.getNumCuentaCobro()+" posee factura(s) con glosa(s) registrada(s)"+"");
		              cxcForm.setMapValidaciones("numErrores",contErr+"");
		              existeError = true;
		              contErr++;	              
		          }
		          if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("existeMovimientoPago_"+k)+"") && !existeError3)
		          {
		              cxcForm.setMapValidaciones("descripcionError_"+contErr,"La cuenta de cobro "+cxcForm.getNumCuentaCobro()+" posee factura(s) con movimiento(s) de pago(s) registrado(s)"+"");
		              cxcForm.setMapValidaciones("numErrores",contErr+"");
		              existeError3 = true;
		              contErr++;	             
		          }
		          if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("existeAjustePendiente_"+k)+"") && !existeError4)
		          {
		              cxcForm.setMapValidaciones("descripcionError_"+contErr,"La cuenta de cobro "+cxcForm.getNumCuentaCobro()+" posee factura(s) con ajuste(s) pendiente(s) por aprobar(s)"+"");
		              cxcForm.setMapValidaciones("numErrores",contErr+"");
		              existeError4 = true;
		              cxcForm.setCuentaCobroTieneAjusPen(true);
		              contErr++;	             
		          }
		          if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("ajustesDifCero_"+k)+"") && !existeError5)
		          {
		              cxcForm.setMapValidaciones("descripcionError_"+contErr,"La cuenta de cobro "+cxcForm.getNumCuentaCobro()+" posee factura(s) con ajuste(s) aprobado(s) despues de radicar la cuenta, y la suma de los ajustes credito y debito es diferente de cero"+"");
		              cxcForm.setMapValidaciones("numErrores",contErr+"");
		              existeError5 = true;
		              contErr++;	             
		          }       
		          if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("existeCastigo_"+k)+"") && !existeError6)
		          {
		              cxcForm.setMapValidaciones("descripcionError_"+contErr,"La cuenta de cobro "+cxcForm.getNumCuentaCobro()+" posee factura(s) con castigos de cartera."+"");
		              cxcForm.setMapValidaciones("numErrores",contErr+"");
		              existeError6 = true;
		              contErr++;	             
		          }    
		    }
	        
        }
        else  if(Integer.parseInt(cxcForm.getMapMovimientos("codigoEstadoCXC")+"") == ConstantesBD.codigoEstadoCarteraGenerado)
        {
	        mundoCXC.validacionesDevolucionCxc(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());	        
	        cxcForm.getMapValidaciones().putAll(mundoCXC.getMapMovimientos());
	        
	        for(int k=0;k<=Integer.parseInt(cxcForm.getMapValidaciones("numFact")+"");k++)
		    {	
		          if( UtilidadTexto.getBoolean(cxcForm.getMapValidaciones("existeAjustePendiente_"+k)+"") )
		          {
		              cxcForm.setCuentaCobroTieneAjusPen(true);
		              k=Integer.parseInt(cxcForm.getMapValidaciones("numFact")+"");
		          }
		    }
	        
        }
        
        this.cerrarConexion(con);
		return mapping.findForward("paginaModificacion"); 		
    }
    
	/**
	 * Metodo para anular una cuenta de cobro.
     * @param con, Connection con la fuente de datos
     * @param mapping
     * @param cxcForm
     * @param anulacion boolean, true para anular la cuenta de cobro
     * @param devolucion boolean, true para devolver la cuenta de cobro
     * @return findForward
     */
    private ActionForward anularCuentaCobro(Connection con, 
												            ActionMapping mapping, 
												            CuentasCobroForm cxcForm,
												            HttpServletRequest request,
												            UsuarioBasico usuario,
												            boolean anulacion,
												            boolean devolucion) 
    {
        CuentasCobro mundoCXC = new CuentasCobro ();
        boolean esAnulado=false;
        
        if(anulacion)
            esAnulado  = mundoCXC.anularCuentaCobro(con,
											                cxcForm.getNumCuentaCobro(),
											                ConstantesBD.inicioTransaccion,
											                usuario.getLoginUsuario(),
											                UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
											                UtilidadFecha.getHoraActual(),
											                cxcForm.getMotivoAnulacion(),
											                ConstantesBD.codigoAnulacionCuentaCobro,
															usuario,usuario.getCodigoInstitucionInt());
        if(devolucion)
        {
        	cxcForm.setFacturasCxC(mundoCXC.consultaFacturasCxc(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt()));
            esAnulado  = mundoCXC.anularCuentaCobro(con,
											                cxcForm.getNumCuentaCobro(),
											                ConstantesBD.inicioTransaccion,
											                usuario.getLoginUsuario(),
											                UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
											                UtilidadFecha.getHoraActual(),
											                cxcForm.getMotivoAnulacion(),
											                ConstantesBD.codigoDevolucionCuentaCobro,
															usuario,usuario.getCodigoInstitucionInt());
            
            if(esAnulado)
            {	
            	for(int i=0;i<(Utilidades.convertirAEntero(cxcForm.getFacturaCxC("numRegistros")+""));i++)
            	{
            		esAnulado= mundoCXC.guardarDetMovimientosCxc(con, Utilidades.convertirAEntero(cxcForm.getFacturaCxC("codigo_"+i)+""),cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());            		
            	}
            }
        }
        if(!esAnulado)
        {
            logger.warn("[anularCXC]No se actualizo el estado de la CXC :(");
            ActionErrors errores = new ActionErrors();
            errores.add("anular CXC", new ActionMessage("prompt.noSeGraboInformacion"));            
            saveErrors(request, errores);              
        }
        
        mundoCXC.reset();
	    cxcForm.reset();			    
	    this.cerrarConexion(con);
		return mapping.findForward("paginaModificacion"); 		
        
    }




	/**
	 * Metodo implementado para guardar los cambios realizados 
	 * a la cuenta de cobro, facturas adicionadas o
	 * retiradas de las ya existentes.
	 * 
     * @param con Connection con la fuente de datos 
     * @param mapping ActionMapping
     * @param cxcForm CuentasCobroForm
     * @return findForward
     */
    private ActionForward guardarSalirModificaciones(Connection con,
															            ActionMapping mapping, 
															            CuentasCobroForm cxcForm,
															            UsuarioBasico usuario,
															            HttpServletRequest request) 
    {
        boolean existeModificacion=verificarModificaciones(con, cxcForm,usuario);
        CuentasCobro mundoCXC = new CuentasCobro ();
        if(existeModificacion)
        {
	       
	        ArrayList vectorViasIngresoAdc=new ArrayList(),vectorViasIngresoMod=new ArrayList();
	        int viaIngresoMod=0,pos1=0,pos2=0;
	        double sumaValInicial = 0.0;
	        mundoCXC.getMapMovimientos().clear();
	        mundoCXC.setMapMovimientos(cxcForm.getFacturasCxC());
	        mundoCXC.setFechaInicial(cxcForm.getFechaInicial());
	        mundoCXC.setFechaFinal(cxcForm.getFechaFinal());
	        mundoCXC.setFechaElaboracion(""); //la fecha de elaboracion no se modifica, esta se modifica en modificacion saldo inicial, por ello se manda vacia
	        mundoCXC.setObservacionesGen(cxcForm.getObservacionMovimiento());
	        
	        if(!(cxcForm.getMapMovimientos("numViasIngresoMod")+"").equals("null"))//en caso de que no se hallan modificado vias de ingreso, evitar errores
	        {
		        /*se valida que vias de ingreso fueron adicionadas o eliminadas
		         *para realizar la respectiva transaccion sobre la tabla vias_ingreso_cxc,
		         *estos index provienen del metodo del action <ordenarValidarHashMap>*/
		        for(int k=0; k < Integer.parseInt(cxcForm.getMapMovimientos("numViasIngresoMod")+"") ; k++)
		        {
		          for(int i=0; i<Integer.parseInt(cxcForm.getMapMovimientos("numViasIngreso")+""); i++)
			  	  {
			        int viaIngreso = Integer.parseInt(cxcForm.getMapMovimientos("codViaIngreso_"+i)+"");
			        //para que solo se ingresen las vias de ingreso que tienen facturas, index <viaIngresoSinFacturas_> proviene del metodo liberarRelacionarFacturas
			        if(!UtilidadTexto.getBoolean(cxcForm.getMapMovimientos("viaIngresoSinFacturas_"+i)+""))
			        {
				        if((cxcForm.getMapMovimientos("esEliminarRelacionVI_"+k)+"").equals(true+"") )
					    {
				          viaIngresoMod=Integer.parseInt(cxcForm.getMapMovimientos("codViaIngresoEliminarRelacionVI_"+k)+"");
					      if(viaIngresoMod == viaIngreso)
					      {   
					        vectorViasIngresoMod.add(pos1,viaIngresoMod+"");			       
					        pos1++;			        
					      }
					    }
				        
				        if((cxcForm.getMapMovimientos("esAdicionarRelacionVI_"+k)+"").equals(true+""))
					    {
				          viaIngresoMod=Integer.parseInt(cxcForm.getMapMovimientos("codViaIngresoAdicionarRelacionVI_"+k)+"");
				          if(viaIngresoMod == viaIngreso)
					      {   
					        vectorViasIngresoAdc.add(pos2,viaIngresoMod+"");			        
					        pos2++;			        
					      }
					    }
			        }
		           } 
		        }
	        }
	        /*Se suman todos los valores de las facturas que estan seleccionadas, para guardar el 
	         * valor inicial y saldo de la CXC*/
	        
	        for(int i=0; i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+""); i++)
	        {  
	          if( (cxcForm.getFacturaCxC("activarCheckBox_"+i)+"").equals("on") )
	          {   
	              sumaValInicial = sumaValInicial + Double.parseDouble(cxcForm.getFacturaCxC("valor_"+i)+"");
	          }
	        }  
	        
	        boolean noExisteError=mundoCXC.liberarRelacionarFacturas(con,
	                								cxcForm.getNumCuentaCobro(),
													vectorViasIngresoMod,
													vectorViasIngresoAdc,true,true,true,
												 	ConstantesBD.inicioTransaccion,
													usuario.getLoginUsuario(),
													sumaValInicial,
													usuario.getCodigoInstitucionInt(),
													false);
	        if(noExisteError)
	        {
	            this.generarLog(cxcForm,usuario.getLoginUsuario(),cxcForm.getNumCuentaCobro()); 
	            cargarResumen(con,cxcForm,true,usuario);
	        }
	        if(!noExisteError)
	        {
	            logger.warn("No se ejecuto toda la transaccion de Modficación Cuenta Cobro");            
	            ActionErrors errores = new ActionErrors();
	            errores.add("anular CXC", new ActionMessage("prompt.noSeGraboInformacion"));            
	            saveErrors(request, errores);   
	        }
	        
	        this.cerrarConexion(con);
			return mapping.findForward("paginaResumenMovimientos");	
        }
        else
        {
        	accionResumenGeneralCuentaCobro(con, mundoCXC, cxcForm, usuario,false);
            this.cerrarConexion(con);
			return mapping.findForward("paginaDetalleMovimientos");	  
        }
    }
    
    /**
     * metodo para validar si hubo modificaciones en la 
     * cuenta de cobro ó en las facturas.
     * @param con Connection, conexión con la fuente de datos
     * @param cxcForm CuentasCobroForm
     * @param usuario
     * @return boolean, true si hay modificaciones
     */
    private boolean verificarModificaciones (Connection con,CuentasCobroForm cxcForm, UsuarioBasico usuario)
    {
        cargarResumen(con,cxcForm,false,usuario);
        boolean existeModificacion = false,codViaIngresoDif = false,codFacturaDif=false;
        int codVia1 = -1,codVia2 = -1,codVia3 = -1;
        int codFact1 = -1,codFact2 = -1,codFact3 = -1;
        
        if(!cxcForm.getFechaInicial().equals(cxcForm.getMapValidaciones("fechaInicial")+""))
        {
            existeModificacion = true;
        }
        if(!cxcForm.getFechaFinal().equals(cxcForm.getMapValidaciones("fechaFinal")+""))
        {
            existeModificacion = true;
        }
        if(!cxcForm.getObservacionMovimiento().equals(cxcForm.getMapValidaciones("observaciones")+""))
        {
            existeModificacion = true;
        }
        
        for(int k=0; k < Integer.parseInt(cxcForm.getMapMovimientos("numViasIngreso")+"") ; k++)
        {
            if((cxcForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("off"))
                codVia1=Integer.parseInt(cxcForm.getMapMovimientos("codViaIngreso_"+k)+"");
                
            if((cxcForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("on"))
            {
                codVia2=Integer.parseInt(cxcForm.getMapMovimientos("codViaIngreso_"+k)+"");                
            }
            
            for(int i=0; i <= Integer.parseInt(cxcForm.getMapValidaciones("numViasIngreso")+"") ; i++)
            {
                codVia3=Integer.parseInt(cxcForm.getMapValidaciones("codViaIngreso_"+i)+"");
                
                if(codVia1!=-1)
	                if(codVia1 == codVia3)
	                  existeModificacion = true;	                    
	                
	            if(codVia2!=-1)
	                if(codVia2 != codVia3)
	                  codViaIngresoDif = true;
	                	                
                if(!codViaIngresoDif && i == Integer.parseInt(cxcForm.getMapValidaciones("numViasIngreso")+""))
                    existeModificacion = true;
                
                if(codViaIngresoDif && i == Integer.parseInt(cxcForm.getMapValidaciones("numViasIngreso")+""))
                    existeModificacion = true;
            }
            codVia1 = -1;
            codVia2 = -1;
        }
                
        for(int k=0; k < Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+"") ; k++)
        {
            if((cxcForm.getFacturaCxC("activarCheckBox_"+k)+"").equals("off"))
            {
                codFact1= Integer.parseInt(cxcForm.getFacturaCxC("codigo_"+k)+"");
                if(Integer.parseInt(cxcForm.getMapValidaciones("numFact")+"") == 0)
                    existeModificacion = true;//si no se encuentra seleccionada ninguna fact antes, y selecciona una              
            }
            
            if((cxcForm.getFacturaCxC("activarCheckBox_"+k)+"").equals("on"))
               codFact2=Integer.parseInt(cxcForm.getFacturaCxC("codigo_"+k)+"");  
                
            for(int i=0; i < Integer.parseInt(cxcForm.getMapValidaciones("numFact")+"") ; i++)
            {
                codFact3=Integer.parseInt(cxcForm.getMapValidaciones("codFact_"+i)+"");
                
                if(codFact1!=-1)
	                if(codFact1 == codFact3)
	                  existeModificacion = true;        
	                
	            if(codFact2!=-1)
	                if(codFact2 != codFact3)
	                    codFacturaDif = true;
	              	                
                if(!codFacturaDif && i == Integer.parseInt(cxcForm.getMapValidaciones("numFact")+""))
                    existeModificacion = true;
            }
            codFact1 = -1;
            codFact2 = -1;
        }
        
        
        return existeModificacion;  
    }
    
    /**
     * Metodo implementado para generar el resumen 
     * despues de las modificaciones, ó ates de ser
     * realizadas por motivos de validaciones de las
     * modificaciones realizadas.
     * @param con Connection, conexión con la fuente de datos
     * @param cxcForm CuentasCobroForm
     * @param esReset boolean, true para reset el mundo y el form
     * @param usuario
     */
    private void cargarResumen (Connection con,CuentasCobroForm cxcForm,boolean esReset, UsuarioBasico usuario)
    {
        CuentasCobro mundoCXC = new CuentasCobro ();
        Iterator it;
        DtoFactura fact;
        int i=0;
        
        if(esReset)
          mundoCXC.reset();
        
        mundoCXC.cargarCuentaCobroCompleta(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());
        
        if(esReset)
         cxcForm.reset(); 
        cxcForm.setNumCuentaCobro(mundoCXC.getNumeroCuentaCobro());
		cxcForm.setNumeroFacturasCxC(mundoCXC.getNumeroFacturas());
		cxcForm.setValorInicialCuenta(mundoCXC.getValInicialCuenta());
		cxcForm.setConvenio(mundoCXC.getConvenio().getCodigo()+ConstantesBD.separadorSplit+mundoCXC.getConvenio().getNombre());
		cxcForm.setNitConvenio(mundoCXC.getAcronimoIdResponsable());
		cxcForm.setDireccionConvenio(mundoCXC.getDireccionConvenio());
		cxcForm.setTelefonoConvenio(mundoCXC.getTelefonoConvenio());
		cxcForm.setFechaInicial(UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaInicial()));
		cxcForm.setFechaElaboracion(UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaElaboracion()));
		cxcForm.setFechaFinal(UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaFinal()));
		cxcForm.setObservaciones(mundoCXC.getObservacionesGen());
		cxcForm.setDetalleFacturas(mundoCXC.getFacturas());
        
        String numCXCStr=mundoCXC.getNumeroCuentaCobro()+"";
        int index=numCXCStr.indexOf(".");
        if(index!=-1)
        {
            String eliminarDecimal = numCXCStr.substring(0,index);           
            cxcForm.setMapValidaciones("numCXC",eliminarDecimal);            
        }               
		cxcForm.setMapValidaciones("numFact",mundoCXC.getNumeroFacturas()+"");
		cxcForm.setMapValidaciones("valor",mundoCXC.getValInicialCuenta()+"");	
		cxcForm.setMapValidaciones("convenio",mundoCXC.getConvenio().getNombre()+"");
		cxcForm.setMapValidaciones("codConvenio",mundoCXC.getConvenio().getCodigo()+"");
		for(int k=0;k<mundoCXC.getViasIngreso().size();k++)
		{
		    cxcForm.setMapValidaciones("viaIngreso_"+k,((InfoDatosInt)(mundoCXC.getViasIngreso().get(k))).getNombre()+"");
		    cxcForm.setMapValidaciones("codViaIngreso_"+k,((InfoDatosInt)(mundoCXC.getViasIngreso().get(k))).getCodigo()+"");
		    cxcForm.setMapValidaciones("numViasIngreso",k+"");
		}
		
		it=mundoCXC.getFacturas().iterator();
		while(it.hasNext())
		{		    
			fact = (DtoFactura) it.next();
		    cxcForm.setMapValidaciones("codFact_"+i,fact.getCodigo()+"");
		    i++;
		}
		cxcForm.setMapValidaciones("fechaInicial",UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaInicial()+""));
		cxcForm.setMapValidaciones("fechaFinal",UtilidadFecha.conversionFormatoFechaAAp(mundoCXC.getFechaFinal()+""));
		cxcForm.setMapValidaciones("observaciones",mundoCXC.getObservacionesGen()+"");
    }
	
	/**
	 * Metodo implementado para liberar las facturas
	 * correspondientes a una cuenta de cobro especifica,
	 * y relacionarlas nuevamente segun los parametros
	 * que han sido seleccionados, por Vía de Ingreso,
	 * por Fecha Inicial-Fecha Final.
	 * 
     * @param con Connection con la fuente de datos
     * @param mapping
     * @param request
     * @param cxcForm
	 * @param usuario
     * @return
     */
    private ActionForward liberarRelacionarFacturas(Connection con, 
														            ActionMapping mapping,														            
														            CuentasCobroForm cxcForm, UsuarioBasico usuario) 
    {
        CuentasCobro mundoCXC = new CuentasCobro ();
        mundoCXC.getMapMovimientos().clear();//limpiar el mapa del mundo
        cxcForm.getFacturasCxC().clear();//limpiar el mapa de facturas
        cxcForm.setNumeroFacturasCxC(0);//inicializar el numero de facturas
        int viasIngresoConFacturas = 0;
        
        String codigoFactRelacionAntigua="",codigoFactRelacionNueva="";
        
        ordenarValidarHashMap(cxcForm);
        mundoCXC.consultarFactALiberar(con,cxcForm.getNumCuentaCobro(),usuario.getCodigoInstitucionInt());
        mundoCXC.setConvenio(new InfoDatosInt(cxcForm.getCodigoConvenio()));
        mundoCXC.setFechaInicial(cxcForm.getFechaInicial());
        mundoCXC.setFechaFinal(cxcForm.getFechaFinal());    
        mundoCXC.setObservacionesGen(cxcForm.getObservacionMovimiento());
        mundoCXC.setNumeroCuentaCobro(cxcForm.getNumCuentaCobro());
        
        for(int k=0; k < Integer.parseInt(cxcForm.getMapMovimientos("numViasIngreso")+"") ; k++)
        {
            if((cxcForm.getMapMovimientos("esCheckActivo_"+k)+"").equals("on"))
            {                
                cxcForm.setNumeroFacturasCxC(mundoCXC.consultarFacturasViaIngreso(con,cxcForm.getMapMovimientos("codViaIngreso_"+k)+"",usuario.getCodigoInstitucionInt(),cxcForm.getNumeroFacturasCxC()));
                //validar que la via de ingreso tenga facturas, si tiene true, de lo contrario false
                //para no guardar vias de ingreso que no posean relacion con facturas
                if(viasIngresoConFacturas == cxcForm.getNumeroFacturasCxC())
                    cxcForm.setMapMovimientos("viaIngresoSinFacturas_"+k,"true");
                else
                    cxcForm.setMapMovimientos("viaIngresoSinFacturas_"+k,"false");
                
                viasIngresoConFacturas = cxcForm.getNumeroFacturasCxC();
            }
        }
        
        cxcForm.setFacturasCxC(mundoCXC.getFacturasCxC());        
        cxcForm.getFacturasCxC().putAll(mundoCXC.getMapMovimientos());
        /*
        for(int k=0; k < Integer.parseInt(cxcForm.getMapMovimientos("numViasIngreso")+"") ; k++)
        {
        	logger.info("via ingreso->"+cxcForm.getMapMovimientos("codViaIngreso_"+k));
        	logger.info("tiene facturas->"+cxcForm.getMapMovimientos("viaIngresoSinFacturas_"+k));
        	logger.info("numViasIngreso->"+cxcForm.getMapMovimientos("numViasIngreso"));
        	logger.info("numViasIngresoMod->"+cxcForm.getMapMovimientos("numViasIngresoMod"));
        }
        */
        /*para validar que facturas tenian relacion antes con la CXC y seleccionarlas
         * en los checkBox de la vista, y adicionar las nuevas facturas mostrandolas
         * deseleccionadas en los checkbox*/
        
        if(Integer.parseInt(cxcForm.getFacturaCxC("numeroFactConRelacionCXC")+"") != 0)
        {
        	double valorTotalCartera=0,valorTotalAjustes=0,valor=0;
        	
	        for(int k =0; k<Integer.parseInt(cxcForm.getFacturaCxC("numeroFactConRelacionCXC")+""); k++)
	        {	
	            /*el index <codigoFactConRelacionCXC_> proviene del metodo del mundo <consultarFactALiberar>
	             * que es donde se consultan las facturas que estaban relacionadas a la CXC, antes
	             * de ser liberadas con el codigo de la cuenta de cobro en null*/
	            codigoFactRelacionAntigua = (cxcForm.getFacturaCxC("codigoFactConRelacionCXC_"+k)+"");
	            
	            /*el index <numFacturas> proviene del metodo del mundo <consultarFacturasViaIngreso>
	             *que es cuando se consultan las facturas segun el rango de busqueda en la CXC*/
		        for(int i=0; i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+""); i++)
		        {
		          codigoFactRelacionNueva = (cxcForm.getFacturaCxC("codigo_"+i)+"");		          
		          if(codigoFactRelacionAntigua.equals(codigoFactRelacionNueva))
		          {
		            cxcForm.setFacturasCxC("activarCheckBox_"+i,"on");
		            valorTotalCartera=valorTotalCartera+Double.parseDouble(cxcForm.getFacturaCxC("valor_cartera_"+i)+"");
		            valorTotalAjustes=valorTotalAjustes+Double.parseDouble(cxcForm.getFacturaCxC("valor_ajustes_"+i)+"");
		            valor=valor+Double.parseDouble(cxcForm.getFacturaCxC("valor_"+i)+"");
		          }
		          else
		          {
		            if(!(cxcForm.getFacturaCxC("activarCheckBox_"+i)+"").equals("on"))
		            {
			          cxcForm.setFacturasCxC("activarCheckBox_"+i,"off");			         
		            }
		          }
		        }   
	        }
	        cxcForm.setFacturasCxC("valor_total_cartera",valorTotalCartera+"");
	        cxcForm.setFacturasCxC("valor_total_ajustes",valorTotalAjustes+"");
	        cxcForm.setFacturasCxC("valor_",valor+"");
        }
        else if(Integer.parseInt(cxcForm.getFacturaCxC("numeroFactConRelacionCXC")+"") == 0)
        {
            /*el index <numFacturas> proviene del metodo del mundo <consultarFacturasViaIngreso>
             *que es cuando se consultan las facturas segun el rango de busqueda en la CXC*/
          for(int i=0; i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+""); i++)
	      {
            cxcForm.setFacturasCxC("activarCheckBox_"+i,"off"); 
	      }
        }  
        ValoresPorDefecto.cargarValoresIniciales(con);
        cxcForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
        this.cerrarConexion(con);
		return mapping.findForward("paginaDetalleMovimientos");
    }
    
    /**
     * Metodo implementado para seleccionar todas
     * las facturas en los checkbox de la vista.
     * @param con Connection, conexion con la fuente de datos
     * @param mapping
     * @param cxcForm
     * @return findForward
     */
    private ActionForward seleccionarMovimientoTodas(Connection con, ActionMapping mapping, CuentasCobroForm cxcForm) 
    {
        /*Para realizar la seleccion de todas las facturas, cuando se elige esta opcion
         * en la vista*/
        for(int i=0; i<Integer.parseInt(cxcForm.getFacturaCxC("numFacturas")+""); i++)
        {
          if((cxcForm.getMapMovimientos("checkSelAll")+"").equals("on"))
            cxcForm.setFacturasCxC("activarCheckBox_"+i,"on");  
          else if( (cxcForm.getMapMovimientos("checkSelAll")+"").equals("off") )
            cxcForm.setFacturasCxC("activarCheckBox_"+i,"off");  
        }   
        
        this.cerrarConexion(con);
		return mapping.findForward("paginaDetalleMovimientos");        
    }
    
	/**
	 * Metodo implementado para ordenar los registros del HashMap,
	 * que provienen de la página de modificaciónCuentaCobro y
	 * de busquedaCuentaCobro.
	 * 
	 * @param cxcForm CuentasCobroForm
	 */
	private boolean ordenarValidarHashMap (CuentasCobroForm cxcForm)
	{
	    
	    boolean eliminarRelacion = false,adicionarRelacion=false,existeModificacion=false;
	    int posHashMap=0;//para regular la secuencia de los indices del HashMap de forma ordenada.
	    int posHashMap2=0;
	    
	       for(int k = 0; k < Integer.parseInt(cxcForm.getMapMovimientos("ultimoIndex")+"") ; k++)
	        {
	         /*esta comparación porque no se conocen los indices de las llaves en el mapa, entonces se busca
	          *la llave iterando los elementos del mapa, hasta la ultima posicion que se inserto desde
	          *la pagina de busquedaCuentaCobro, la propiedad containsKey retornara true cuando la encuentre.
	          *Como existe la propiedad pero para algunas posiciones los valores son null, 
              *porque asi se producen en la pagina, evitar errores */
	           if(cxcForm.getMapMovimientos().containsKey("codViaIngresoTemp_"+k) 
	                    && cxcForm.getMapMovimientos().containsKey("esCheckActivoTemp_"+k)
	                    && !cxcForm.getMapMovimientos("codViaIngresoTemp_"+k).equals(null))
	            {               
		          /*Si se modifico una via de ingreso que estaba relacionada a la cuenta de cobro, 
		           * y por lo tanto a las facturas,y ya no debe de haber relacion */
		          if( cxcForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k).equals("on") 
		                     && cxcForm.getMapMovimientos("esCheckActivoTemp_"+k).equals("off") )
		          {
		             cxcForm.setMapMovimientos("codViaIngresoEliminarRelacionVI_"+posHashMap2,cxcForm.getMapMovimientos("codViaIngresoTemp_"+k));	                    
		             eliminarRelacion = true;
		             cxcForm.setMapMovimientos("esEliminarRelacionVI_"+posHashMap2,eliminarRelacion+"");
		          }
		                
		          /*Si se modifico una via de ingreso que NO estaba relacionada a la cuenta de cobro, 
		           * y por lo tanto a las facturas,y debe de haber relacion */
		          if( cxcForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k).equals("off") 
		                     && cxcForm.getMapMovimientos("esCheckActivoTemp_"+k).equals("on") )
		          {
		            cxcForm.setMapMovimientos("codViaIngresoAdicionarRelacionVI_"+posHashMap2,cxcForm.getMapMovimientos("codViaIngresoTemp_"+k));
		            adicionarRelacion=true;
		            cxcForm.setMapMovimientos("esAdicionarRelacionVI_"+posHashMap2,adicionarRelacion+"");
		          }
	              
		          /*para ordenar el mapa, con un orden de indices secuencial, puesto que antes contenia
	               * indices en desorden(ej.11,8,0, ...), y queda(ej. 0,1,2, ...)*/
		          cxcForm.setMapMovimientos("codViaIngreso_"+posHashMap,cxcForm.getMapMovimientos("codViaIngresoTemp_"+k));
		          cxcForm.setMapMovimientos("esCheckActivo_"+posHashMap,cxcForm.getMapMovimientos("esCheckActivoTemp_"+k));
		          cxcForm.setMapMovimientos("existeViaIngresoCXC_"+posHashMap,cxcForm.getMapMovimientos("existeViaIngresoCXCTemp_"+k));	                
		          cxcForm.getMapMovimientos().remove("codViaIngresoTemp_"+k);
		          cxcForm.getMapMovimientos().remove("esCheckActivoTemp_"+k);
		          cxcForm.getMapMovimientos().remove("existeViaIngresoCXCTemp_"+k);		          
		          cxcForm.setMapMovimientos("numViasIngreso",(posHashMap+1)+"");
		          posHashMap ++;
		            
	              if ( eliminarRelacion )                   
		          {
	                existeModificacion = true;
	                eliminarRelacion = false;                
	                cxcForm.setMapMovimientos("numViasIngresoMod",(posHashMap2+1)+"");
	                posHashMap2 ++;
		          }	              
	              if( adicionarRelacion )
	              {
		            existeModificacion = true;
		            adicionarRelacion=false;
		            cxcForm.setMapMovimientos("numViasIngresoMod",(posHashMap2+1)+"");
		            posHashMap2 ++;	              }
	            }
	        }          
	    return existeModificacion;
	}
	
	
	/**
	 * Metodo para generar el log tipo archivo de
	 * la cuenta de cobro modificada
	 * @param cxcForm CuentasCobroForm
	 * @param usuario, usuario que modifico
	 * @param numCxc, double numero de la cuenta de cobro
	 */
	private void generarLog(CuentasCobroForm cxcForm,String usuario,double numCxc)
	    {
        
		    cxcForm.setLog(
		            					"\n============INFORMACION CUENTA DE COBRO MODIFICADA===== " +
		            					"\n*  Usuario [" +usuario+"] "+
		            					"\n*  Fecha ["+UtilidadFecha.getFechaActual()+"] " +
		            					"\n*  Hora  ["+UtilidadFecha.getHoraActual()+"] "+
		            					"\n*  Número Cuenta Cobro  ["+numCxc+"] "+	
										"\n========================================================\n\n\n ") ;
	        
	        LogsAxioma.enviarLog(ConstantesBD.logCuentasCobroCodigo,cxcForm.getLog(),ConstantesBD.tipoRegistroLogModificacion,usuario);  
	     	        
	    }
	
	
	/**
	 * Accion en la que se realiza una pre cuenta.
	 * @param con
	 * @param mapping
	 * @param mundoCxC
	 * @param cxcForm
	 * @param usuario
	 * @return
	 */
	private void accionContinuarGeneracion(Connection con, CuentasCobro mundoCxC, CuentasCobroForm cxcForm, UsuarioBasico usuario) 
	{
		mundoCxC.reset();
		cxcForm.setNumeroFacturasCxC(0);
		mundoCxC.setConvenio(new InfoDatosInt(Integer.parseInt((cxcForm.getConvenio().split(ConstantesBD.separadorSplit))[0])));
		mundoCxC.setFechaInicial(cxcForm.getFechaInicial());
		mundoCxC.setFechaFinal(cxcForm.getFechaFinal());
		mundoCxC.setCodigoCentroAtencion(cxcForm.getCodigoCentroAtencion());
		mundoCxC.setNombreCentroAtencion(cxcForm.getNombreCentroAtencion());
		//no tenemos cuenta de cobro la cargamos con codigonuncavalido(-1) para realizar la busqueda de las facturas.
		mundoCxC.setNumeroCuentaCobro(ConstantesBD.codigoNuncaValido);
		int numeroViasIngreso=Integer.parseInt(cxcForm.getCuentasCobroMapa("numeroViasIngreso")+"");
		
		/*for(int i=0;i<numeroViasIngreso;i++)
		{
			boolean seleccionada=UtilidadTexto.getBoolean(cxcForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+"");
			int numeroFacturaXViaIngreso=0;
			if(seleccionada)
			{
				numeroFacturaXViaIngreso=cxcForm.getNumeroFacturasCxC();
				cxcForm.setNumeroFacturasCxC(mundoCxC.consultarFacturasViaIngreso(con,Integer.parseInt(cxcForm.getCuentasCobroMapa("codigoViaIngreso_"+i)+""),usuario.getCodigoInstitucionInt(),cxcForm.getNumeroFacturasCxC()));
				numeroFacturaXViaIngreso=cxcForm.getNumeroFacturasCxC()-numeroFacturaXViaIngreso;
			}
			cxcForm.setCuentasCobroMapa("cantidadFacturasXViaIngreso_"+i,numeroFacturaXViaIngreso+"");
		}*/
		
		String viasIngresoSeleccionadas=ConstantesBD.codigoNuncaValido+"";
		for(int i=0;i<numeroViasIngreso;i++) {
			if(UtilidadTexto.getBoolean(cxcForm.getCuentasCobroMapa("viaIngresoSeleccionada_"+i)+""))
				viasIngresoSeleccionadas+=", "+cxcForm.getCuentasCobroMapa("codigoViaIngreso_"+i);
		}
		cxcForm.setNumeroFacturasCxC(mundoCxC.consultarFacturasViaIngreso(con,viasIngresoSeleccionadas,usuario.getCodigoInstitucionInt(),cxcForm.getNumeroFacturasCxC()));
		
		cxcForm.setNumeroFacturasSeleccionadas(cxcForm.getNumeroFacturasCxC());
				
		cxcForm.setFacturasCxC(mundoCxC.getFacturasCxC());		
		
		int factSel=0;
		/*
		 *ESTE ARREGLO SE DEBE HACER A NIVEL DE CONSULTA, NO SE DEBEN TENER EN CUENTA LAS QUE ESTAN EN PROCESO DE AUDITORIA 
		for(int k=0;k<(Utilidades.convertirAEntero(cxcForm.getFacturaCxC("numFacturas")+""));k++)
		{
			int facturasProceso=mundoCxC.consultarFacturaEnProcesoAudi(Utilidades.convertirAEntero(cxcForm.getFacturaCxC("codigo_"+k)+""));
			if(facturasProceso > 0)
			{
				cxcForm.setFacturasCxC("seleccionado_"+k, false);
				cxcForm.setFacturasCxC("mostrar_"+k, false);
			}
			else
			{
				factSel++;
				cxcForm.setFacturasCxC("mostrar_"+k, true);
			}
		}
		 
		
		cxcForm.setNumeroFacturasSeleccionadas(factSel);
		*/
		cxcForm.setNumeroFacturasNoAptas(Integer.parseInt(cxcForm.getFacturaCxC("numFacRechazadas")+""));
		cxcForm.setValorTotalFacturas(Double.parseDouble((cxcForm.getFacturaCxC("valor_total")+"")!=null&&!(cxcForm.getFacturaCxC("valor_total")+"").equals("null")?(cxcForm.getFacturaCxC("valor_total")+""):"0"));
	}


	/**
	 * Método que elimina los datos nen sessión que se establecieron desde el
	 * popUp de radicación de cuentas y que ya no son necesarios
	 * @param request
	 */
	private void removerDatosSessionPopUp(HttpServletRequest request) {
		if(request.getSession().getAttribute("codigo_convenio")!=null)
			request.getSession().removeAttribute("codigo_convenio");
		if(request.getSession().getAttribute("ultimaColumna")!=null)
			request.getSession().removeAttribute("ultimaColumna");
		if(request.getSession().getAttribute("sentido")!=null)
			request.getSession().removeAttribute("sentido");
		
	}




	/**
	 * Acción de imprimir en la consulta de Cuentas de cobro
	 * ya sea de manera detallada o resumida
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param cxcForm
	 * @param usuario
	 * @param mundoCxC
	 * @return
	 */
	private ActionForward accionImprimir(ActionMapping mapping, HttpServletRequest request, Connection con, CuentasCobroForm cxcForm, UsuarioBasico usuario, CuentasCobro mundoCxC)
	{
		cxcForm.setMapMovimientos("codigoCXC",cxcForm.getNumCuentaCobro()+"");
		String nombreArchivo;
		String nombreArchivo2;
		
		Random r= new Random();
		nombreArchivo= "aBorrarDetallado" + r.nextInt() + ".pdf";
		nombreArchivo2= "aBorrarResumido" + r.nextInt() + ".pdf";
		Boolean detalle=false;
		Boolean resumen=false;
		
		
		
		CuentasCobroPdf.imprimir(cxcForm, usuario, con, mundoCxC, ValoresPorDefecto.getFilePath()+ nombreArchivo,ValoresPorDefecto.getFilePath()+nombreArchivo2, request);
		detalle=CuentasCobroPdf.existeReporteDetallado(mundoCxC);
		resumen=CuentasCobroPdf.existeReporteResumen(con, mundoCxC, cxcForm);
		
		this.cerrarConexion(con);
		
		if(cxcForm.isImpresionAnexos())
		{
			if(detalle && !resumen){
				request.setAttribute("nombreArchivo1", String.valueOf(ConstantesBD.codigoNuncaValido));
				request.setAttribute("nombreVentana1", "Cuentas de Cobro Resumido");
				request.setAttribute("nombreArchivo2","/"+nombreArchivo);
				request.setAttribute("nombreVentana2", "Cuentas de Cobro");
				cxcForm.setExisteReporteDetallado("existeReportes");
				return mapping.findForward("abrir2Pdf");
			}else  if(!detalle && resumen){
				request.setAttribute("nombreArchivo1", "/"+nombreArchivo2);
				request.setAttribute("nombreVentana1", "Cuentas de Cobro Resumido");
				request.setAttribute("nombreArchivo2",String.valueOf(ConstantesBD.codigoNuncaValido));
				request.setAttribute("nombreVentana2", "Cuentas de Cobro");
				cxcForm.setExisteReporteDetallado("existeReportes");
				return mapping.findForward("abrir2Pdf");
			}else if(detalle && resumen){
				request.setAttribute("nombreArchivo2","/"+nombreArchivo);
				request.setAttribute("nombreVentana2", "Cuentas de Cobro");
				request.setAttribute("nombreArchivo1", "/"+nombreArchivo2);
				request.setAttribute("nombreVentana1", "Cuentas de Cobro Resumido");
				cxcForm.setExisteReporteDetallado("existeReportes");
				return mapping.findForward("abrir2Pdf");
			}else{
				request.setAttribute("nombreArchivo2",String.valueOf(ConstantesBD.codigoNuncaValido));
				request.setAttribute("nombreVentana2", "Cuentas de Cobro");
				request.setAttribute("nombreArchivo1",String.valueOf(ConstantesBD.codigoNuncaValido));
				request.setAttribute("nombreVentana1", "Cuentas de Cobro Resumido");
				cxcForm.setExisteReporteDetallado("noExisteReportes");
				return mapping.findForward("paginaConsulta");
			}
		}
		else
		{
			if(detalle){
				request.setAttribute("nombreArchivo","/"+nombreArchivo);
				request.setAttribute("nombreVentana", "Cuentas de Cobro");
				cxcForm.setExisteReporteDetallado("existeSoloDetalle");
				return mapping.findForward("abrirPdf");
			}else{
				cxcForm.setExisteReporteDetallado("noExisteSoloDetalle");
				return mapping.findForward("paginaConsulta");
			}
		}
	}
	
	
	
	/**
	 * Accion para el detalle de una cuenta de cobro despues de la busqueda en la funcionalidad
	 * Consultar/Imprimir Cuenta Cobro
	 * @param mapping
	 * @param con
	 * @param cxcForm
	 * @return
	 */
	@SuppressWarnings("null")
	private ActionForward detalleBusqueda(ActionMapping mapping, Connection con, CuentasCobroForm cxcForm)
	{
		cxcForm.setMaxPageItems(cxcForm.getDetalleFacturas().size());
		// Incidencia 5072, las facturas se mostraban con su valor bruto sin tener en cuenta los ajustes
		Object[] cole = cxcForm.getDetalleFacturas().toArray();
		DtoFactura arre = new DtoFactura() ;
		HashMap ajusta= new HashMap();
		Collection <DtoFactura> newdet=  new ArrayList();
			
		for (int i=0; i<cxcForm.getDetalleFacturas().size();i++)
		{
		String busqAjustes="SELECT DISTINCT " +
					" ae.tipo_ajuste as tipo_ajuste, " +
					" getNombreTipoAjuste(ae.tipo_ajuste) as nombre_tipo_ajuste, " +
					" ae.valor_ajuste as valor_ajuste " +
					" from ajustes_empresa ae " +
	" left outer join ajus_fact_empresa afe on (afe.codigo=ae.codigo) " +
	" left outer join cuentas_cobro cc on (cc.numero_cuenta_cobro=ae.cuenta_cobro and cc.institucion=ae.institucion)  " +
	" left outer join facturas f on (f.codigo=afe.factura) " +
	" where ae.institucion=2 ";
		arre= (DtoFactura) cole[i];
		arre.getConsecutivoFactura();
		busqAjustes += " AND ae.cuenta_cobro is null and  f.consecutivo_factura ="+arre.getConsecutivoFactura();
		if(arre.getAjustesCredito()==-1.0 && arre.getAjustesDebito()==-1.0 )
		{
		try{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(busqAjustes));
			ajusta=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			try{
				double valorT=arre.getValorTotal();
				for (int j=0; j<Integer.parseInt(ajusta.get("numRegistros").toString()+"");j++)
				{
					if(Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==2 || Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==4)
					{
						valorT=valorT-Double.parseDouble(ajusta.get("valor_ajuste_"+j).toString());
					}
					if(Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==1 || Integer.parseInt(ajusta.get("tipo_ajuste_"+j+"").toString())==3)
					{
						valorT=valorT+Double.parseDouble(ajusta.get("valor_ajuste_"+j).toString());
					}
				}
				arre.setValorCartera(valorT);
				
			}
			catch(Exception e){
				logger.error("ERROR aiterar el resultado de la busqueda de ajustes \n"+e.getMessage());
			}
		}
		catch(Exception e){
			logger.error("ERROR al buscar los ajustes de las facturas de la cuenta \n"+e.getMessage());
		}
		}
		newdet.add(arre);
		cxcForm.setDetalleFacturas(newdet);
		}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("resumenIngresoCuentaCobroDetalle");
	}
	
	
	/**
	 * Accion para la busqueda en  elmomento de ver el detalle de una cuenta de cobro
	 * en la funcionaluidad de Consultar/Imprimir Cuenta Cobro
	 * @param mapping
	 * @param con
	 * @param cxcForm
	 * @param mundoCxC
	 * @param usuario
	 * @return
	 */
	private ActionForward busquedaDetalleFacturas(ActionMapping mapping, Connection con, CuentasCobroForm cxcForm, CuentasCobro mundoCxC, UsuarioBasico usuario)
	{
		if(!cxcForm.getTextoBusqueda().trim().equals(""))
		{
			if(cxcForm.getCriterio().equals("Nro.Factura"))
			{
				Collection facturas=new ArrayList();
				int numero_factura=0;
				try 
				{
					numero_factura=mundoCxC.cargarFacturaPorConsecutivo(con, cxcForm.getNumCuentaCobro(),(Integer.parseInt(cxcForm.getTextoBusqueda())));
					
				}
				catch (Exception e)
				{
					numero_factura=-1;
				}
				DtoFactura fact=Factura.cargarFactura(con, numero_factura+"", false);
				
				facturas.add(fact);
				if(fact.getCodigo()>0)
					cxcForm.setDetalleFacturas(facturas);
				else
					cxcForm.setDetalleFacturas(new ArrayList());
				cxcForm.setMaxPageItems(facturas.size());
			}
			else if(cxcForm.getCriterio().equals("Fecha Elaboracion"))
			{
				String fecha=cxcForm.getTextoBusqueda();
				if(UtilidadFecha.validarFecha(fecha))
				{
					
					mundoCxC.cargarFacturasPorFecha(con, cxcForm.getNumCuentaCobro(),UtilidadFecha.conversionFormatoFechaABD(fecha));
					cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
					cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
				}
				else
				{
					cxcForm.setDetalleFacturas(new ArrayList());
				}
				
			}
			else if(cxcForm.getCriterio().equals("Via de Ingreso"))
			{
				mundoCxC.cargarFacturasPorViaIngreso(con, cxcForm.getNumCuentaCobro(),cxcForm.getTextoBusqueda());
				cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
				cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
			}
			else if(cxcForm.getCriterio().trim().equals(""))
			{
				double cuenta_cobro=Double.parseDouble(cxcForm.getMapMovimientos("codigoCXC")+"");
				mundoCxC.cargarCuentaCobroCompleta(con,cuenta_cobro,usuario.getCodigoInstitucionInt());
				cxcForm.setNumCuentaCobro(cuenta_cobro);
				cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
				cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
			}
		}
		else
		{
			cxcForm.setCriterio("");
			double cuenta_cobro=Double.parseDouble(cxcForm.getMapMovimientos("codigoCXC")+"");
			mundoCxC.cargarCuentaCobroCompleta(con,cuenta_cobro,usuario.getCodigoInstitucionInt());
			cxcForm.setNumCuentaCobro(cuenta_cobro);
			cxcForm.setDetalleFacturas(mundoCxC.getFacturas());
			cxcForm.setMaxPageItems(mundoCxC.getFacturas().size());
		}
			this.cerrarConexion(con);
			return mapping.findForward("resumenIngresoCuentaCobroDetalle");
	}

	/**
	 * Método para llenar el mundo
	 * @param movimientosCuentaCobro
	 * @param cxcForm
	 * @param estado
	 * @param usuario
	 */
	private void llenarMundo(MovimientosCuentaCobro movimientosCuentaCobro, CuentasCobroForm cxcForm, String estado, UsuarioBasico usuario) {
		if(estado.equals("guardarRadicacion")){
			movimientosCuentaCobro.setNumCuentaCobro(cxcForm.getNumCuentaCobro());
			movimientosCuentaCobro.setNumRadicacion(cxcForm.getNumeroRadicacion());
			movimientosCuentaCobro.setFechaRadicacion(cxcForm.getFechaRadicacion());
			movimientosCuentaCobro.setUsuarioMovimiento(usuario.getLoginUsuario());
			movimientosCuentaCobro.setObservacionesRadicacion(cxcForm.getObservacionesRadicacion());
		}
		else if(estado.equals("guardarInactivacion")){
			movimientosCuentaCobro.setNumCuentaCobro(cxcForm.getNumCuentaCobro());
			movimientosCuentaCobro.setCodigoFactura(cxcForm.getCodigoFactura());
			movimientosCuentaCobro.setUsuarioMovimiento(cxcForm.getUsuarioMovimiento());
			movimientosCuentaCobro.setObservaciones(cxcForm.getObservaciones());
			movimientosCuentaCobro.setValor(cxcForm.getValor());
		}
	}
		
	
	private void llenarForm(CuentasCobro mundoCxC, CuentasCobroForm cxcForm, String estado)
	{
		if(estado.equals("recargarBusqueda"))
		{
			cxcForm.setTipoAnulacion(mundoCxC.getTipoAnulacion());
			cxcForm.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp(mundoCxC.getFechaAnulacion()));
			cxcForm.setHoraAnulacion(mundoCxC.getHoraAnulacion());
			cxcForm.setUsuarioAnulacion(mundoCxC.getUsuarioAnulacion());
			cxcForm.setMotivoAnulacion(mundoCxC.getMotivoAnulacion());
		}

	}

	/**
	 * Método para editar los errores recibidos de la validación de inactivación
	 * de facturas y de la validación de radicación de cuentas de cobro
	 * @param mensajes_error
	 * @param request
	 * @return indica true si hubo errores de lo cotnrario false
	 */
	private boolean editarErrores(String mensajes_error, HttpServletRequest request) {
		Collection errores=new ArrayList();
		//Se revisa que si se hayan editado errores
		
		if(!mensajes_error.equals("")&&mensajes_error!=null){
			
			//Se divide la cadena de errores en un vector
			String[] cadena_errores=mensajes_error.split(ConstantesBD.separadorTags);
			
			for(int i=0;i<cadena_errores.length;i++){
				//división para obtener los atributos
				String[] sub_cadena=cadena_errores[i].split("@");
				
				//se revisa si el errore tiene atributos
				if(sub_cadena.length>1){
					//Se agrega la etiqueta a un objeto ElementoApResource
					ElementoApResource elem=new ElementoApResource(sub_cadena[0]);
					for(int j=1;j<sub_cadena.length;j++)
						elem.agregarAtributo(sub_cadena[j]);
					errores.add(elem);
				}
				else{
					//Se agrega las etiquetas a un objeto ElemwentoApResource
					ElementoApResource elem=new ElementoApResource(cadena_errores[i]);
					errores.add(elem);
				}
			}
			//SE suben errores a la sesión
			request.getSession().setAttribute("conjuntoErroresObjeto",errores);
			return true;
		}
		else{
			return false;
		}
		
		
	}
	




	/**
	 * Método para cargar los datos del mundo al formulario dependiendo
	 * del estado manejado
	 * @param movimientosCuentaCobro
	 * @param cxcForm
	 * @param estado
	 */
	private void llenarForm(MovimientosCuentaCobro movimientosCuentaCobro, CuentasCobroForm cxcForm, String estado) {
		if(estado.equals("buscarFactura")){
			//reservado para inactivacion
			cxcForm.setFechaElaboracion(UtilidadFecha.conversionFormatoFechaAAp(movimientosCuentaCobro.getFechaElaboracion()));
			cxcForm.setCodigoFactura(movimientosCuentaCobro.getCodigoFactura());
			cxcForm.setConvenio(movimientosCuentaCobro.getConvenio());
			cxcForm.setFechaRadicacion(UtilidadFecha.conversionFormatoFechaAAp(movimientosCuentaCobro.getFechaRadicacion()));
			cxcForm.setValor(movimientosCuentaCobro.getValor());	
			cxcForm.setNumCuentaCobro(movimientosCuentaCobro.getNumCuentaCobro());
			
			//carga la fecha de aprobacion
			cxcForm.setFechaAprobacion(movimientosCuentaCobro.getFechaAprobacion());
		}
		else if(estado.equals("buscarCuentaCobro")){

			//reservado para radicación
			cxcForm.setNumCuentaCobro(movimientosCuentaCobro.getNumCuentaCobro());
			cxcForm.setFechaElaboracion(movimientosCuentaCobro.getFechaElaboracion());
			cxcForm.setHoraElaboracion(UtilidadFecha.convertirHoraACincoCaracteres(movimientosCuentaCobro.getHoraElaboracion()));
			//solo se carga el nombre del covnenio
			cxcForm.setConvenio((movimientosCuentaCobro.getConvenio().split(ConstantesBD.separadorTags))[1]);
			cxcForm.setCodigoConvenio(Integer.parseInt((movimientosCuentaCobro.getConvenio().split(ConstantesBD.separadorTags))[0]));
			//se refiere al valor inicial de la cuenta de cobro
			cxcForm.setValor(movimientosCuentaCobro.getValor());
			
			//carga la fecha de aprobacion
			cxcForm.setFechaAprobacion(movimientosCuentaCobro.getFechaAprobacion());
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
					logger.warn("Problemas con la base de datos al abrir la conexion");
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
						logger.error("Error al tratar de cerrar la conexion con la fuente de datos MedicosXPoolAction. \n Excepcion: " +e);
					}
			}		 
}
