/*
 * Creado en Jun 13, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import util.ElementoApResource;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.CuentaCobroCapitacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.CuentaCobroCapitacion;
import com.princetonsa.pdf.CuentasCobroCapitacionPdf;


public class CuentaCobroCapitacionAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CuentaCobroCapitacionAction.class);
	
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
		
		if(form instanceof CuentaCobroCapitacionForm)
		{
			
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				CuentaCobroCapitacionForm forma =(CuentaCobroCapitacionForm)form;
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String estado=forma.getEstado(); 
				logger.warn("[CuentaCobroCapitacionAction] Estado --> "+estado );
				
				if(estado == null)
				{
						forma.reset();	
						logger.warn("Estado no valido dentro del flujo de Cuenta de Cobro Capitación (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				//---Estado que muestra la página de busqueda de cargues por periodo ------//
				else if(estado.equals("empezarCarguesPeriodo"))
				{
					return accionEmpezarCarguesPeriodo (con, mapping, forma, request, usuario);
				}
				//---Se consulta los cargues que cumplen con los datos de busqueda ingresados para periodo------//
				else if(estado.equals("busquedaCarguesPeriodo"))
				{
					return accionConsultarCarguesPeriodo(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//estado para mantener los datos del pager en cada una de las paginas
				else if (estado.equals("redireccion"))
				{			    
					 UtilidadBD.cerrarConexion(con);
				    response.sendRedirect(forma.getLinkSiguiente());
				    return null;
				}
				 //-Ordenar el listado de cargues en la generación de cuentas de cobro
				else if (estado.equals("ordenarListadoCarguesGeneracion")) 
				{			    
					return accionOrdenarListadoCarguesGeneracion(forma, mapping, con);
				}
				//---Se generan las cuentas de cobro para los cargues seleccionados agrupando por el convenio------//
				else if(estado.equals("generarCuentaCobroCapitacion"))
				{
					return accionGenerarCuentaCobroCapitacion(con, mapping, request, forma, usuario);
				}
				//---Se consulta las cuentas de cobro por convenio------//
				else if(estado.equals("cuentasCobroConvenio"))
				{
					return accionCuentasCobroConvenio(con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//---Se consultan los contratos asociados a la cuenta de cobro, en el detalle------//
				else if(estado.equals("detalleCuentaCobro"))
				{
					return accionDetalleCuentaCobro(con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//---Estado que muestra la página de busqueda de cargues por convenio ------//
				else if(estado.equals("empezarCarguesConvenio"))
				{
					return accionEmpezarCarguesConvenio(con, mapping, forma, request, usuario);
				}
				//---Se consulta los cargues que cumplen con los datos de busqueda ingresados por convenio------//
				else if(estado.equals("busquedaCarguesConvenio"))
				{
					return accionConsultarCarguesConvenio(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//---------Empieza la modificación de las cuentas de cobro --------------------------//
				else if(estado.equals("empezarModificarCxC"))
				{
					return accionEmpezarModificarCuentaCobro (con, mapping, forma, usuario, request);
				}
				//------------Se realiza la bùsqueda de la cuenta de cobro por el número de cuenta de cobro ----------//
				else if (estado.equals("busquedaCxCModificar"))
				{
					return accionBuscarCxCModificar(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//------------Se muestra la página para realizar la búsqueda avanzada en modificar ----------//
				else if (estado.equals("empezarBusquedaAvanzada"))
				{
					return accionEmpezarBusquedaAvanzada(con, mapping, forma);
				}
				//------------Se realiza la bùsqueda de la(s) cuenta(s) de cobro por los parámetros en la bùsqueda avanzada ----------//
				else if (estado.equals("consultaBusquedaAvanzada"))
				{
					return accionConsultaBusquedaAvanzada(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//-Ordenar el listado de CxC Modificar Busqueda avanzada
				else if (estado.equals("ordenarListadoCxCModificar")) 
				{			    
					return accionOrdenarListadoCxCModificar(forma, mapping, con);
				}
				//-Se anula la cuenta de cobro seleccionada y se liberan sus cargues
				else if (estado.equals("anularCuentaCobro")) 
				{			    
					return accionAnularCuentaCobro (con, mapping, forma, request, usuario);
				}
				//-Cuando se da continuar en modifcar cuenta de cobro
				else if (estado.equals("continuarModificarCxC")) 
				{			    
					return accionContinuarModificarCxC (con, mapping, forma, request, usuario);
				}
				//-Ordenar el listado de cargues en el modificar
				else if (estado.equals("ordenarListadoCarguesModificar")) 
				{			    
					return accionOrdenarListadoCarguesModificar(forma, mapping, con);
				}
				//---Se modifica la cuenta de cobro y los cargues respectivos
				else if(estado.equals("modificarCuentaCobroCapitacion"))
				{
					return accionModificarCuentaCobroCapitacion(con, mapping, request, forma, usuario);
				}
				//---------Empieza la consulta de las cuentas de cobro --------------------------//
				else if(estado.equals("empezarConsultarCxC"))
				{
					return accionEmpezarConsultarCuentaCobro (con, mapping, forma);
				}
				//------------Se realiza la bùsqueda de la cuenta de cobro por el número de cuenta de cobro con cualquier estado----------//
				else if (estado.equals("busquedaCxCConsultar"))
				{
					return accionBuscarCxCConsultar(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//------------Se realiza la bùsqueda de la(s) cuenta(s) de cobro por los parámetros en la bùsqueda avanzada ----------//
				else if (estado.equals("busquedaAvanzadaConsultar"))
				{
					return accionBusquedaAvanzadaConsultar(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//---Se consultan los cargues asociados a la cuente de cobro------//
				else if(estado.equals("detalleCuentaCobroConsultar"))
				{
					//return accionDetalleCuentaCobroConsultar(con, mapping, forma, usuario.getCodigoInstitucionInt());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				//--Se Imprimiran las cuentas de cobro--Convenio --//
				else if(estado.equals("impresionCuentasCobro"))
				{
					return accionImpresionCuentasCobro(con, mapping, forma, request, usuario);
				}
				//---Se consultan las facturas asociadas a la cuenta de cobro------//
				else if(estado.equals("detalleFacturasCxC"))
				{
					return accionDetalleFacturasCxC(con, mapping, request, forma, usuario.getCodigoInstitucionInt());
				}
				//------------Se muestra la página para realizar la búsqueda avanzada de facturas ----------//
				else if (estado.equals("empezarBusquedaFacturas"))
				{
					return accionEmpezarBusquedaFacturas(con, mapping, forma);
				}
				//----------Se realiza la consulta de facturas de acuerdo a los parámetros de búsqueda---------//
				else if (estado.equals("busquedaAvanzadaFacturas"))
				{
					return accionBusquedaAvanzadaFacturas(con, mapping, forma, request, usuario.getCodigoInstitucionInt());
				}
				//--Se imprime la cuenta de cobro consultada con el detalle de cargues --//
				else if(estado.equals("imprimirCuentaCobro"))
				{
					return accionImprimirCuentaCobro(con, mapping, forma, request, usuario, false);
				}
				//--Se imprime la cuenta de cobro generada por convenio --//
				else if(estado.equals("imprimirCuentaCobroConvenio"))
				{
					return accionImprimirCuentaCobro(con, mapping, forma, request, usuario, true);
				}
				//--Se imprime la cuenta de cobro modificada con el detalle de cargues --//
				else if(estado.equals("imprimirCxCModificacion"))
				{
					return accionImprimirCuentaCobroModificacion(con, mapping, forma, request, usuario);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	 * Método para empezar la bùsqueda de la generaciòn de cuentas de cobro
	 * de capitación por periodo
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarCarguesPeriodo(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws Exception
	{
		Collection futurosErrores=new ArrayList();
		
		//Se obtiene el valor del parámetro tipo de consecutivo capitación -----//
		String tipoConsecutivo=ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt());
		
		int topeConsecutivo=0;
		int consecutivoCxCCapi=0;
		
		//------Se verifica si está definido el parámetro tipo consecutivo capitación -----//
		if(!UtilidadCadena.noEsVacio(tipoConsecutivo) || tipoConsecutivo.equals("-1"))
		{
			ElementoApResource elem=new ElementoApResource("error.capitacion.faltaParametroTipoConsecutivo");
			futurosErrores.add(elem);
		}
		else
		{
			if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera+""))
			{
				String consecutivoCxCCapitacion=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentaCobroCapitacion, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivoCxCCapitacion) ||  consecutivoCxCCapitacion.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.capitacion.consecutivoCxCCapitacionNoDisponible");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoCxCCapitacion);
				}
			}
			else if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera+""))
			{
				String consecutivoCartera=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivoCartera) || consecutivoCartera.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.faltaConsecutivoCuentaCobro");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoCartera);
				}
			}
			else if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoFacturaPaciente+""))
			{
				String consecutivoFactura=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivoFactura) || consecutivoFactura.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.facturacion.consecutivoNoDisponible");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoFactura);
				}
			}
			else
			{
				ElementoApResource elem=new ElementoApResource("error.capitacion.consecutivoSinDefinir");
				futurosErrores.add(elem);
			}
		}
		
		//Se valida el parametro de topes Consecutivo CxC Saldo Inicial Capitación
    	if(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
	    {
    		ElementoApResource elem=new ElementoApResource("error.capitacion.faltaParametroTopeConsecutivoCxCSaldoInicial");
			futurosErrores.add(elem);
        }
    	
    	if (futurosErrores.isEmpty())
		{
    		topeConsecutivo=Utilidades.convertirAEntero(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt())+"");
    		if((consecutivoCxCCapi+1) <= topeConsecutivo)
    		{
    			ElementoApResource elem=new ElementoApResource("error.capitacion.topeConsecutivoSaldoInicialMayorIgualCuentaCobro");
    			futurosErrores.add(elem);
    		}
		}

		if (!futurosErrores.isEmpty())
		{
		return ComunAction.envioMultiplesErrores(mapping, request, con, "label.capitacion.generacionCxCCapitacion", futurosErrores, logger);
		}
		
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busquedaCarguesPeriodo");
	}
	
	/**
	 * Método para empezar la bùsqueda de la generaciòn de cuentas de cobro
	 * de capitación por periodo
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezarCarguesConvenio(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws Exception
	{
		Collection futurosErrores=new ArrayList();
				
		
		//Se obtiene el valor del parámetro tipo de consecutivo capitación -----//
		String tipoConsecutivo=ValoresPorDefecto.getTipoConsecutivoCapitacion(usuario.getCodigoInstitucionInt());
		
		int topeConsecutivo=0;
		int consecutivoCxCCapi=0;
		
		//------Se verifica si está definido el parámetro tipo consecutivo capitación -----//
		if(!UtilidadCadena.noEsVacio(tipoConsecutivo) || tipoConsecutivo.equals("-1"))
		{
			ElementoApResource elem=new ElementoApResource("error.capitacion.faltaParametroTipoConsecutivo");
			futurosErrores.add(elem);
		}
		else
		{
			if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionDiferenteCartera+""))
			{
				String consecutivoCxCCapitacion=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentaCobroCapitacion, usuario.getCodigoInstitucionInt());
								
				if(!UtilidadCadena.noEsVacio(consecutivoCxCCapitacion) ||  consecutivoCxCCapitacion.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.capitacion.consecutivoCxCCapitacionNoDisponible");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoCxCCapitacion);
				}
			}
			else if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoCapitacionUnicoCartera+""))
			{
				String consecutivoCartera=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoCuentasCobro, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivoCartera) || consecutivoCartera.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.faltaConsecutivoCuentaCobro");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoCartera);
				}
			}
			else if (tipoConsecutivo.equals(ConstantesBD.codigoTipoConsecutivoFacturaPaciente+""))
			{
				String consecutivoFactura=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt());
				
				if(!UtilidadCadena.noEsVacio(consecutivoFactura) || consecutivoFactura.equals("-1"))
				{
					ElementoApResource elem=new ElementoApResource("error.facturacion.consecutivoNoDisponible");
					futurosErrores.add(elem);
				}
				else
				{
					consecutivoCxCCapi=Integer.parseInt(consecutivoFactura);
				}
			}
			else
			{
				ElementoApResource elem= new ElementoApResource("error.capitacion.consecutivoSinDefinir");
				futurosErrores.add(elem);
			}
		}
		
		//Se valida el parametro de topes Consecutivo CxC Saldo Inicial Capitación
    	if(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
	    {
    		ElementoApResource elem=new ElementoApResource("error.capitacion.faltaParametroTopeConsecutivoCxCSaldoInicial");
			futurosErrores.add(elem);
        }
		
    	if (futurosErrores.isEmpty())
		{
    		
    		topeConsecutivo=Utilidades.convertirAEntero(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt())+"");
    		if((consecutivoCxCCapi+1) <= topeConsecutivo)
    		{
    			ElementoApResource elem=new ElementoApResource("error.capitacion.topeConsecutivoSaldoInicialMayorIgualCuentaCobro");
    			futurosErrores.add(elem);
    		}
		}
    	
		if (!futurosErrores.isEmpty())
		{
		return ComunAction.envioMultiplesErrores(mapping, request, con, "label.capitacion.generacionCxCCapitacion", futurosErrores, logger);
		}
		
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busquedaCarguesConvenio");
	}
	
	
	/**
	 * Método que consulta los cargues que cumplen con los requirimientos,
	 * de acuerdo a los parámetros de busqueda por periodo
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsultarCarguesPeriodo(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int institucion) throws SQLException
	{
		//---------------------------Se realizan las validaciones para pode realizar la busqueda ----------------------------------//
		ActionErrors errores= new ActionErrors();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(institucion);
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		forma.setMaxPageItems( Integer.parseInt(numItems) );		
		//forma.setMaxPageItems(5);
		
		CuentaCobroCapitacion mundoCxCCapitacion = new CuentaCobroCapitacion();
		
		//-----------Se consultan los cargues por periodo ------------------------//
		forma.setMapaCargues( mundoCxCCapitacion.consultarCargues(con, UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalGeneracion()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialGeneracion()), -1));
		
		//---------Si no se encuentran cargues de acuerdo a los parámetros de búsqueda ------------//
		if(Integer.parseInt(forma.getMapaCargues("numRegistros")+"")==0)
		{
			errores.add("noHayCargues", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noExistenCargues", forma.getCuentaCobroBuscar()));
			saveErrors(request, errores);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaCarguesPeriodo");
		}
		else
		{
			forma.setMapaCargues("total_cargue",0);
		}
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que consulta los cargues que cumplen con los requirimientos,
	 * de acuerdo a los parámetros de busqueda por convenio
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsultarCarguesConvenio(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int institucion) throws SQLException
	{
		ActionErrors errores= new ActionErrors();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(institucion);
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		forma.setMaxPageItems( Integer.parseInt(numItems) );		

		CuentaCobroCapitacion mundoCxCCapitacion = new CuentaCobroCapitacion();
		
		//-----------Se consultan los cargues por convenio ------------------------//
		forma.setMapaCargues( mundoCxCCapitacion.consultarCargues(con, UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalGeneracion()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialGeneracion()), forma.getConvenioCapitado()));
		
		forma.setMapaMensajeError(mundoCxCCapitacion.consultarNumeroCargue(con, UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalGeneracion()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialGeneracion()), forma.getConvenioCapitado()));
		
		//--------------Se obtiene el nombre del convenio ----------------------------//
		forma.setNombreConvenio(Utilidades.obtenerNombreConvenioOriginal(con, forma.getConvenioCapitado()));
		
		if(Utilidades.convertirAEntero(forma.getMapaMensajeError().get("numRegistros")+"")>0)
		{	
			//---------Si no se encuentran cargues de acuerdo a los parámetros de búsqueda ------------//
			if(Integer.parseInt(forma.getMapaCargues("numRegistros")+"")==0)
			{
				errores.add("noHayCargues", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noExistenCargues", forma.getMapaMensajeError().get("cuenta_cobro_0")));
				saveErrors(request, errores);
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("busquedaCarguesConvenio");
			}
			else
			{
				double total=0;
				for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaCargues("numRegistros")+"");i++)
				{
					total=total+Utilidades.convertirADouble(forma.getMapaCargues("valor_total_"+i)+"");
				}
				forma.setMapaCargues("total_cargue",total);
			}
		}
		else
		{
			errores.add("noHayCargues", new ActionMessage("errors.notEspecific", "No existen cargues para los parametros de busqueda seleccionados"));
			saveErrors(request, errores);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaCarguesConvenio");
		}
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que genera la cuenta de cobro para cada uno de los cargues seleccionados
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */
	
	private ActionForward accionGenerarCuentaCobroCapitacion(Connection con, ActionMapping mapping, HttpServletRequest request, CuentaCobroCapitacionForm forma, UsuarioBasico usuario) throws SQLException 
	{
		ActionErrors errores = new ActionErrors();
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------------Se verifica que haya sido seleccionado al menos un cargue ---------------------//
		if( forma.getMapaCargues() != null)
		{
			int numRegistros=Integer.parseInt(forma.getMapaCargues("numRegistros")+"");
			int contSeleccionados=0;
			
			for (int i=0; i<numRegistros; i++)
				{
				String seleccionado= (String)forma.getMapaCargues("cargue_"+i);
				
				//--------Se verifica si fué seleccionado el cargue ----------//
					if (UtilidadTexto.getBoolean(seleccionado))
					{
						contSeleccionados++;
						
						//-------Se verifica que el contrato del cargue tenga definido el tipo de pago -------//
						if(!UtilidadCadena.noEsVacio(forma.getMapaCargues("tipo_pago_"+i)+""))
						{
							errores.add("error.capitacion.contratoSinTipoPago", new ActionMessage("error.capitacion.contratoSinTipoPago",forma.getMapaCargues("contrato_"+i)+ "", forma.getMapaCargues("nombre_convenio_"+i)+ ""));
						}
						//break;
					}
				}//for
			
			//-----Si contSeleccionados=0 es porque no se seleccionò ningún cargue ------//
			if (contSeleccionados==0)
			{
				errores.add("noHayCarguesSeleccionados", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noHayCarguesSeleccionados"));
				saveErrors(request, errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
			}
		}//if mapaCargues != null
		
		//------Se resetean los valores del mundo ----------//
		mundoCxCCapitacion.reset();
		
		//---Pasar los datos del form al mundo
		llenarMundo(forma, mundoCxCCapitacion);
		
		//------------Se generan las cuentas de cobro para cada convenio -------------------------------------//
		String cuentasCobro=mundoCxCCapitacion.generarCuentaCobroCapitacion (con, usuario);
		
		//-------Si numeroCuentaCobro es mayor a cero se generó satisfactoriamente la(s) cuenta(s) de cobro -------//
		if (!cuentasCobro.trim().equals(""))
		{
			if (forma.getOrigenGeneracionCuentaCobro().equals("porPeriodo"))
				return accionCuentasCobroConvenio(con, mapping, forma, usuario.getCodigoInstitucionInt(),cuentasCobro);
			else 
				return accionDetalleCxCGeneracionConvenio(con, mapping, forma, Integer.parseInt(cuentasCobro), usuario.getCodigoInstitucionInt());
		}
		else
		{
		errores.add("procesoNoExitoso", new ActionMessage("errors.procesoNoExitoso"));
		saveErrors(request, errores);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoInstitucionInt
	 * @param cuentasCobro
	 * @return
	 */
	private ActionForward accionCuentasCobroConvenio(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, int institucion, String cuentasCobro) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consultan las cuenta de cobro para cada convenio-----------------------//
		forma.setMapaCuentasCobroConvenio(mundoCxCCapitacion.consultarCuentasCobroConvenio(con, institucion,cuentasCobro));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("cuentasCobroConvenio");
	}


	/**
	 * Método que consulta las cuentas de cobro para cada convenio de lo generado
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param institucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionCuentasCobroConvenio(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, int institucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consultan las cuenta de cobro para cada convenio-----------------------//
		forma.setMapaCuentasCobroConvenio(mundoCxCCapitacion.consultarCuentasCobroConvenio(con, forma.getFechaInicialGeneracion(), forma.getFechaFinalGeneracion(), institucion));
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("cuentasCobroConvenio");
	}
	
	/**
	 * Método que consulta los contratos asociados a la cuenta de cobro seleccionada
	 * en el ver detalle
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleCuentaCobro (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consultan los contratos cargue para la cuenta de cobro-----------------------//
		forma.setMapaDetalleCuentaCobro(mundoCxCCapitacion.consultarDetalleCuentaCobro(con, forma.getNumeroCuentaCobro(), codigoInstitucion));
		
	   //-------Se obtiene el valor total de la cuenta de cobro ---------------//
		double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, forma.getNumeroCuentaCobro(), codigoInstitucion);
		
		if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
			  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
			else
			  forma.setTotalCuentaCobro(valorInicialCuenta+"");
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleCuentaCobro");
	}
	
	/**
	 * Método para empezar la modificación de la cuenta de cobro 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */	
	private ActionForward accionEmpezarModificarCuentaCobro(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, UsuarioBasico usuario, HttpServletRequest request) throws SQLException
	{
		
		//Se valida el parametro de topes Consecutivo CxC Saldo Inicial Capitación para poder realizar la validación
		//del tipo de cuenta de cobro
    	if(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(usuario.getCodigoInstitucionInt()).trim().equals(""))
	    {
    		request.setAttribute("codigoDescripcionError", "error.capitacion.faltaParametroTopeConsecutivoCxCSaldoInicial");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
        }
    	
    	forma.reset();
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que consulta una cuenta de cobro de acuerdo al valor ingresado
	 * en el número de la cuenta de cobro
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBuscarCxCModificar(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consulta la cuenta de cobro-----------------------//
		forma.setMapaInfoCuentaCobro(mundoCxCCapitacion.consultarCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion, ConstantesBD.codigoNuncaValido));
		
		//---------Si no se encuentran informaciòn para el número de cuenta de cobro se muestra el error ------------//
		if(Integer.parseInt(forma.getMapaInfoCuentaCobro("numRegistros")+"")==0)
		{
			ActionErrors errores= new ActionErrors();
			
			errores.add("noHayCargues", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noExisteCuentaCobro", forma.getCuentaCobroBuscar()));
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda modificar-----------------//
			forma.setEstado("empezarModificarCxC");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else if(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_estado_0")+"")!=ConstantesBD.codigoEstadoCarteraGenerado)
		{
			ActionErrors errores= new ActionErrors();
			errores.add("noHayCargues", new ActionMessage("message.capitacion.cuentaCobroCapitacion.enEstado", forma.getCuentaCobroBuscar(),forma.getMapaInfoCuentaCobro("nombre_estado_0")+""));
			saveErrors(request, errores);
			forma.setEstado("empezarModificarCxC");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else 
		{
			//-----------------Se guarda lo del mapa en el correspondiente campo-------------------//
			forma.setNombreEstadoCxCModificar(forma.getMapaInfoCuentaCobro("nombre_estado_0")+"");
			forma.setCodigoEstadoModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_estado_0")+""));
			forma.setNombreConvenioModificar(forma.getMapaInfoCuentaCobro("nombre_convenio_0")+"");
			forma.setCodigoConvenioModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_convenio_0")+""));
			forma.setFechaElaboracionModificar(forma.getMapaInfoCuentaCobro("fecha_elaboracion_0")+"");
			forma.setFechaInicialModificar(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
			forma.setFechaInicialModAnterior(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
			forma.setFechaFinalModificar(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
			forma.setFechaFinalModAnterior(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
			forma.setObservacionesModificar(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
			forma.setObservacionesModAnterior(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
			forma.setNitConvenio(forma.getMapaInfoCuentaCobro("nit_convenio_0")+"");
			forma.setFechaRadicacion(forma.getMapaInfoCuentaCobro("fecha_radicacion_0")+"");
			forma.setNumeroRadicacion(forma.getMapaInfoCuentaCobro("numero_radicacion_0")+"");
			
			forma.setHoraElaboracion(forma.getMapaInfoCuentaCobro("hora_elaboracion_0")+"");
			
			Utilidades.imprimirMapa(forma.getMapaInfoCuentaCobro());
			
			/*Se verifica que tipo de cuenta de cobro es, si es menor al parámetro de tope Consecutivo CxC Saldo Inicial Capitación
			 * entonces es de tipo saldo inicial sino es una cuenta de cobro normal
			 */
			int topeConsecutivo=Integer.parseInt(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(codigoInstitucion));
			
			if(Integer.parseInt(forma.getCuentaCobroBuscar()+"")<topeConsecutivo)
				forma.setTipoCuentaCobro(ConstantesBD.codigoTipoCuentaCobroCapitacionSaldosIniciales);
			else
				forma.setTipoCuentaCobro(ConstantesBD.codigoTipoCuentaCobroCapitacionNormal);
			
			//-------Se obtiene el valor total de la cuenta de cobro ---------------//
			double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion);
			
			//-----------Se guarda el valor inicial de la cuenta de cobro en el atributo -----------//
			if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
				  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######"));
			else
				  forma.setTotalCuentaCobro(valorInicialCuenta+"");
		}
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método para empezar con la bùsqueda avanzada 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */	
	private ActionForward accionEmpezarBusquedaAvanzada (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma) throws SQLException
	{
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busquedaAvanzada");
	}
	
	/**
	 * Método que consulta la(s) cuenta(s) de cobro que cumplen con los
	 * parámetros de la búsqueda avanzada
	 * en el número de la cuenta de cobro
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsultaBusquedaAvanzada (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		ActionErrors errores= new ActionErrors();
		boolean errorFechaInicial=false, errorFechaFinal=false;
		
		//---------------------Se realiza la validación de los parámetros de búsqueda avanzada ----------------------------------------//
		if (!UtilidadCadena.noEsVacio(forma.getCuentaCobroBuscar()) && forma.getConvenioCapitado()==-1 && !UtilidadCadena.noEsVacio(forma.getFechaElaboracionModificar()) && forma.getEstadoCuentaCobroBuscar()==-1 && !UtilidadCadena.noEsVacio(forma.getFechaInicialModificar()) && !UtilidadCadena.noEsVacio(forma.getFechaFinalModificar()))
		{
			errores.add("noHayParametros", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "UN"));
		}
		else
		{
			//---------Validación de la fecha de elaboración-----------------//
			if (UtilidadCadena.noEsVacio(forma.getFechaElaboracionModificar()))
			{
				//---------Se verifica el formato de la fecha de elaboración---------------//
				if (!UtilidadFecha.validarFecha(forma.getFechaElaboracionModificar()))
				{
				errores.add("Fecha Elaboracion Invalido", new ActionMessage("errors.formatoFechaInvalido", " Elaboración"));
				}
				//--------------Se verifica que la fecha de elaboración sea menor o igual a la fecha del sistema -----------//
				else if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaElaboracionModificar())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fechaElaboracion", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Elaboración", "actual"));
							}
			}
			
			//------------------Validación de la fecha Inicial -------------------//
			if (UtilidadCadena.noEsVacio(forma.getFechaInicialModificar()))
			{
				//---------Se verifica el formato de la fecha inicial---------------//
				if (!UtilidadFecha.validarFecha(forma.getFechaInicialModificar()))
				{
				errorFechaInicial=true;
				errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
				}
				//--------------Se verifica que la fecha inicial sea menor a la fecha del sistema -----------//
				else if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialModificar())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>=0)
				{
					errores.add("fechaInicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "actual"));
				}
				//------------------Validación que sea requerida la fecha final -------------------//
				if (!UtilidadCadena.noEsVacio(forma.getFechaFinalModificar()))
				{
					errorFechaFinal=true;
					errores.add("Fecha Final vacio", new ActionMessage("errors.required","La Fecha Final"));
				}
			}//if fechaInicial no es vacío
			
			//------------------Validación de la fecha final -------------------//
			if (UtilidadCadena.noEsVacio(forma.getFechaFinalModificar()))
			{
				//---------Se verifica el formato de la fecha final---------------//
				if (!UtilidadFecha.validarFecha(forma.getFechaFinalModificar()))
				{
				errorFechaFinal=true;
				errores.add("Fecha Final Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final"));
				}
				//------------------Validación que sea requerida la fecha inicial -------------------//
				if (!UtilidadCadena.noEsVacio(forma.getFechaInicialModificar()))
				{
					errorFechaInicial=true;
					errores.add("Fecha Inicial vacio", new ActionMessage("errors.required","La Fecha Inicial"));
				}
				
				//---------Se verifica que la fecha final sea mayor a la fecha inicial ----------//
				if (!errorFechaInicial && !errorFechaFinal)
					{
					//--------------Se verifica que la fecha final sea menor o igual a la fecha del sistema -----------//
					if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialModificar())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaFinal", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Final", "actual"));
					}
					
					//---- Validar que la fecha final sea mayor a la fecha inicial-----//
					if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalModificar())).compareTo(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialModificar()))<=0)
						{
							errores.add("fechaFinal", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Final", "Inicial"));
						}
					}
			}//if fechaFinal no es vacío
			
		}
		
		//-------------Si existen errores en los parámetros de búsqueda --------------//
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda modificar-----------------//
			forma.setEstado("empezarBusquedaAvanzada");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaAvanzada");
		}
		
		//-----------Se consulta la cuenta de cobro-----------------------//
		forma.setMapaCuentasCobroConvenio(mundoCxCCapitacion.consultaAvanzadaCuentaCobro(con, forma.getCuentaCobroBuscar(), "-1", forma.getConvenioCapitado(), forma.getFechaElaboracionModificar(), forma.getEstadoCuentaCobroBuscar(), forma.getFechaInicialModificar(), forma.getFechaFinalModificar(), codigoInstitucion));
		
		int numRegistros=Integer.parseInt(forma.getMapaCuentasCobroConvenio("numRegistros")+"");

		//---------Si no se encuentran informaciòn para los parámetros de búsqueda avanzada se muestra error------------//
		if(numRegistros==0)
		{
			errores.add("noHayCargues", new ActionMessage("error.noExisteInformacionBusqueda"));
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda modificar-----------------//
			forma.setEstado("empezarBusquedaAvanzada");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaAvanzada");
		}
		//---------Si encuentra una sóla cuenta de cobro se muestra la información en la página de modificar directamente----------//
		else if (numRegistros==1) 
		{
			String cuentaCobroTemp=forma.getMapaCuentasCobroConvenio("numero_cuenta_cobro_0")+"";
			
			//-------------------Se resetea el form------------//
			forma.reset();
			
			//--------------Se setea la varible cuenta de cobro buscar para que se muestra la información de este----------//
			forma.setCuentaCobroBuscar(cuentaCobroTemp);
			
			//-----------Se muestra la información de la cuenta de cobro en la pàgina de modificar-------------//
			return accionBuscarCxCModificar(con, mapping, forma, request, codigoInstitucion);
		}
		//------------Si la consulta arrojó mas de 1 registro se muestra el listado de cuentas de cobro --------------//
		else 
		{
			//Se obtiene el número de registros por página que se tiene parametrizado
			String numItems=ValoresPorDefecto.getMaxPageItems(codigoInstitucion);
			if(numItems==null || numItems.trim().equals(""))
			{
				numItems="20";
			}
			forma.setMaxPageItems( Integer.parseInt(numItems) );		
			//forma.setMaxPageItems(5);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("resultadoBusquedaAvanzada");	
		}
		
	}
	
	/**
	 * Método que ordena el mapa del listado de cuentas de cobro en la 
	 * búsqueda avanzada en modificar
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenarListadoCxCModificar (CuentaCobroCapitacionForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"numero_cuenta_cobro_", "nombre_convenio_", "fecha_elaboracion_", "periodo_", "nombre_estado_", "saldo_cuenta_"};

		int num = Integer.parseInt(forma.getMapaCuentasCobroConvenio("numRegistros")+"");
		
		forma.setMapaCuentasCobroConvenio(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapaCuentasCobroConvenio(),
									num ));
        
        forma.getMapaCuentasCobroConvenio().put("numRegistros", new String(num+""));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("resultadoBusquedaAvanzada");
	}
	
	/**
	 * Método que ordena el mapa del listado de cargues en
	 * la generación de las cuentas de cobro
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenarListadoCarguesGeneracion (CuentaCobroCapitacionForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"codigo_", "cargue_", "nombre_convenio_", "contrato_", "fecha_inicial_", "fecha_final_", "fecha_cargue_", "total_usuarios_", "upc_", "valor_total_", "tipo_pago_", "codigo_convenio_"};

		int num = Integer.parseInt(forma.getMapaCargues("numRegistros")+"");
		double totalCargue=0;
		
		if(UtilidadCadena.noEsVacio(forma.getMapaCargues("total_cargue")+""))
		{
			totalCargue=Double.parseDouble(forma.getMapaCargues("total_cargue")+"");
		}
				
		forma.setMapaCargues(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapaCargues(),
									num ));
        
        forma.getMapaCargues().put("numRegistros", new String(num+""));
        forma.getMapaCargues().put("total_cargue", new String(totalCargue+""));
        
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("principal");
	}
	
	/**
	 * Método que ordena el mapa del listado de cargues en 
	 * el modificar cuenta de cobro
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenarListadoCarguesModificar(CuentaCobroCapitacionForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {
				"codigo_", 
				"cargue_", 
				"contrato_", 
				"fecha_inicial_", 
				"fecha_final_", 
				"fecha_cargue_", 
				"total_pacientes_", 
				"upc_", 
				"valor_total_", 
				"tipo_pago_",
				"tiene_ajustes_",
				"numero_contrato_"
				
				};

		int num = Integer.parseInt(forma.getMapaCargues("numRegistros")+"");
		
		double totalCargue=0;
		
		if(UtilidadCadena.noEsVacio(forma.getMapaCargues("total_cargue")+""))
		{
			totalCargue=Double.parseDouble(forma.getMapaCargues("total_cargue")+"");
		}
		
		forma.setMapaCargues(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapaCargues(),
									num ));
        
        forma.getMapaCargues().put("numRegistros", new String(num+""));
        forma.getMapaCargues().put("total_cargue", new String(totalCargue+""));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listadoCarguesModificar");
	}

	
	/**
	 * Método que realiza la anulación de la cuenta de cobro, modificando su estado y
	 * liberando los cargues asociados a ella (se ponen en null)
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionAnularCuentaCobro(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//---------Se guarda en el mundo el motivo de anulación-----------//
		mundoCxCCapitacion.setMotivoAnulacion(forma.getMotivoAnulacion());
		
		//-------Si tuvo éxito la anulación de la cuenta de cobro se muestra la información ----------//
		if (mundoCxCCapitacion.anularCuentaCobro (con, forma.getCuentaCobroBuscar(),usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()))
			{
			String cuentaCobroTemp=forma.getCuentaCobroBuscar();
			
			//-------------------Se resetea el form------------//
			forma.reset();
			
			//--------------Se setea la varible cuenta de cobro buscar para que se muestra la información de este----------//
			forma.setCuentaCobroBuscar(cuentaCobroTemp);
			
			//-----------Se consulta la cuenta de cobro-----------------------//
			forma.setMapaInfoCuentaCobro(mundoCxCCapitacion.consultarCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), usuario.getCodigoInstitucionInt(), 2));
			
			
			
			//-----------------Se guarda lo del mapa en el correspondiente campo-------------------//
				forma.setNombreEstadoCxCModificar(forma.getMapaInfoCuentaCobro("nombre_estado_0")+"");
				forma.setCodigoEstadoModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_estado_0")+""));
				forma.setNombreConvenioModificar(forma.getMapaInfoCuentaCobro("nombre_convenio_0")+"");
				forma.setFechaElaboracionModificar(forma.getMapaInfoCuentaCobro("fecha_elaboracion_0")+"");
				forma.setFechaInicialModificar(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
				forma.setFechaFinalModificar(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
				forma.setObservacionesModificar(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
				forma.setNitConvenio(forma.getMapaInfoCuentaCobro("nit_convenio_0")+"");
				forma.setFechaRadicacion(forma.getMapaInfoCuentaCobro("fecha_radicacion_0")+"");
				forma.setNumeroRadicacion(forma.getMapaInfoCuentaCobro("numero_radicacion_0")+"");
				
				
				forma.setHoraElaboracion(forma.getMapaInfoCuentaCobro("hora_elaboracion_0")+"");
			
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("resumenAnulacion");
			}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("procesoNoExitoso", new ActionMessage("errors.procesoNoExitoso"));
			saveErrors(request, errores);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Método que muestra el listado de cargues de acuerdo a si se modificó o no algún parámetro
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionContinuarModificarCxC(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		//---------------------------Se realizan las validaciones para pode realizar la busqueda ----------------------------------//
		ActionErrors errores= new ActionErrors();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt());
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		forma.setMaxPageItems( Integer.parseInt(numItems) );		
		//forma.setMaxPageItems(5);
		
		CuentaCobroCapitacion mundoCxCCapitacion = new CuentaCobroCapitacion();
		
		//------------Se verifica si se modificò alguno de los paràmetros de generaciòn, fecha inicial o final---------//
		if (!forma.getFechaInicialModificar().equals(forma.getFechaInicialModAnterior()) || !forma.getFechaFinalModificar().equals(forma.getFechaFinalModAnterior()))
			{
				forma.setMapaCargues(new HashMap());
				//-----------Se consultan los cargues para el nuevo período ------------------------//
				forma.setMapaCargues( mundoCxCCapitacion.consultarCarguesModificar(con, Integer.parseInt(forma.getCuentaCobroBuscar()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalModificar()), -1));
			}
		else
			{
			forma.setMapaCargues(new HashMap());
			//-----------Se consultan los cargues asociados a la cuenta de cobro mas los que cumplen con los parámetros de bùsqueda ------------------------//
			forma.setMapaCargues( mundoCxCCapitacion.consultarCarguesModificar(con,  Integer.parseInt(forma.getCuentaCobroBuscar()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalModificar()), forma.getCodigoConvenioModificar()));
			}
		
		//---------Si no se encuentran cargues de acuerdo a los parámetros de búsqueda ------------//
		if(Integer.parseInt(forma.getMapaCargues("numRegistros")+"")==0)
		{
			errores.add("noHayCargues", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noExistenCargues", forma.getCuentaCobroBuscar()));
			saveErrors(request, errores);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoCarguesModificar");
	}
	
	/**
	 * Método que realiza la modificación de la cuenta de cobro y sus cargues, de acuerdo a
	 * los parámetros
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionModificarCuentaCobroCapitacion(Connection con, ActionMapping mapping, HttpServletRequest request, CuentaCobroCapitacionForm forma, UsuarioBasico usuario) throws SQLException
	{
		ActionErrors errores = new ActionErrors();
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------------Se verifica que haya sido seleccionado al menos un cargue ---------------------//
		if( forma.getMapaCargues() != null)
		{
			int numRegistros=Integer.parseInt(forma.getMapaCargues("numRegistros")+"");
			int contSeleccionados=0;
			
			for (int i=0; i<numRegistros; i++)
				{
				String seleccionado= (String)forma.getMapaCargues("cargue_"+i);
				
				//--------Se verifica si fué seleccionado el cargue ----------//
					if (UtilidadTexto.getBoolean(seleccionado))
					{
						contSeleccionados++;
						break;
					}
				}//for
			
			//-----Si contSeleccionados=0 es porque no se seleccionò ningún cargue ------//
			if (contSeleccionados==0)
			{
				errores.add("noHayCarguesSeleccionados", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noHayCarguesSeleccionados"));
				saveErrors(request, errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("listadoCarguesModificar");
			}
		}//if mapaCargues != null
		
		//------Se resetean los valores del mundo ----------//
		mundoCxCCapitacion.reset();
		
		//---Pasar los datos del form al mundo
		llenarMundoModificar(forma, mundoCxCCapitacion);
		
		//------------Se generan las cuentas de cobro para cada convenio -------------------------------------//
		if (mundoCxCCapitacion.modificarCuentaCobroCapitacion (con, usuario, forma.getFechaInicialModAnterior(), forma.getFechaFinalModAnterior(), forma.getObservacionesModAnterior()))
		{
			return accionCuentaCobroDetalleCargues(con, mapping, forma, usuario.getCodigoInstitucionInt());
		}
		else
		{
		errores.add("procesoNoExitoso", new ActionMessage("errors.procesoNoExitoso"));
		saveErrors(request, errores);
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoCarguesModificar");
		}
	}
	
	/**
	 * Método que consulta la cuenta de cobro y los cargues asociados a ella
	 * que cumplen con el parámetro de la fecha final
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionCuentaCobroDetalleCargues (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma,int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consulta la información de la cuenta de cobro-----------------------//
		forma.setMapaInfoCuentaCobro(mundoCxCCapitacion.consultarCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion, 1));
		
		//-----------------Se guarda lo del mapa en el correspondiente campo-------------------//
		forma.setNombreEstadoCxCModificar(forma.getMapaInfoCuentaCobro("nombre_estado_0")+"");
		forma.setNombreConvenioModificar(forma.getMapaInfoCuentaCobro("nombre_convenio_0")+"");
		forma.setFechaElaboracionModificar(forma.getMapaInfoCuentaCobro("fecha_elaboracion_0")+"");
		forma.setFechaInicialModificar(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
		forma.setFechaFinalModificar(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
		forma.setObservacionesModificar(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
		forma.setNitConvenio(forma.getMapaInfoCuentaCobro("nit_convenio_0")+"");
		forma.setFechaRadicacion(forma.getMapaInfoCuentaCobro("fecha_radicacion_0")+"");
		forma.setNumeroRadicacion(forma.getMapaInfoCuentaCobro("numero_radicacion_0")+"");
		forma.setDireccionConvenio(forma.getMapaInfoCuentaCobro("direccion_convenio_0")+"");
		forma.setTelefonoConvenio(forma.getMapaInfoCuentaCobro("telefono_convenio_0")+"");
		forma.setValorTotalCuentaCobro(forma.getMapaInfoCuentaCobro("valor_inicial_cuenta_0")+"");
		forma.setPagosCuentaCobro(forma.getMapaInfoCuentaCobro("valor_pagos_0")+"");
		forma.setValorAjusteDebito(forma.getMapaInfoCuentaCobro("ajuste_debito_0")+"");
		forma.setValorAjusteCredito(forma.getMapaInfoCuentaCobro("ajuste_credito_0")+"");
		forma.setSaldoTotalCuentaCobro(forma.getMapaInfoCuentaCobro("saldo_cuenta_0")+"");
		
		forma.setHoraElaboracion(forma.getMapaInfoCuentaCobro("hora_elaboracion_0")+"");
		
		//---------Se consultan los cargues asociados a la cuenta de cobro y que la fecha final sea menor o igual--------//
		forma.setMapaCargues( mundoCxCCapitacion.consultarCarguesModificar(con, Integer.parseInt(forma.getCuentaCobroBuscar()), UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalModificar()), -1));
		
		//-------Se obtiene el valor total de la cuenta de cobro ---------------//
		double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion);
		
		//-----------Se guarda el valor inicial de la cuenta de cobro en el atributo -----------//
		//forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
		
		if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
			  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
			else
			  forma.setTotalCuentaCobro(valorInicialCuenta+"");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleCarguesCuentaCobro");
	}
	
	/**
	 * Método para empezar la consulta de las cuentas de cobro
	 * de capitación 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */	
	private ActionForward accionEmpezarConsultarCuentaCobro(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma) throws SQLException
	{
		forma.reset();
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que consulta una cuenta de cobro de acuerdo al valor ingresado
	 * en el número de la cuenta de cobro
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBuscarCxCConsultar(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int codigoInstitucion) throws SQLException
	{
		ActionErrors errores= new ActionErrors();
		
		//---------------Se verifica que no esté vacío el número de la cuenta de cobro a buscar ----------------//
		if (!UtilidadCadena.noEsVacio(forma.getCuentaCobroBuscar()))
				{
				errores.add("Cuenta Cobro vacio", new ActionMessage("errors.required","La Cuenta por Cobrar"));
				saveErrors(request, errores);
				
				//---------Se cambia el estado a empezar busqueda consultar-----------------//
				forma.setEstado("empezarConsultarCxC");
				
				 UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");
				}
		
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consulta la cuenta de cobro-----------------------//
		forma.setMapaInfoCuentaCobro(mundoCxCCapitacion.consultarCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion, -1));
		
		//---------Si no se encuentran informaciòn para el número de cuenta de cobro se muestra el error ------------//
		if(Integer.parseInt(forma.getMapaInfoCuentaCobro("numRegistros")+"")==0)
		{
			errores.add("noHayCargues", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noExisteCuentaCobro", forma.getCuentaCobroBuscar()));
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda consultar-----------------//
			forma.setEstado("empezarConsultarCxC");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else 
		{
			//-----------------Se guarda lo del mapa en el correspondiente campo-------------------//
			forma.setPrefijoFactura(forma.getMapaInfoCuentaCobro("prefijo_factura_0")+"");
			forma.setNombreEstadoCxCModificar(forma.getMapaInfoCuentaCobro("nombre_estado_0")+"");
			forma.setCodigoEstadoModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_estado_0")+""));
			forma.setNombreConvenioModificar(forma.getMapaInfoCuentaCobro("nombre_convenio_0")+"");
			forma.setCodigoConvenioModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_convenio_0")+""));
			forma.setFechaElaboracionModificar(forma.getMapaInfoCuentaCobro("fecha_elaboracion_0")+"");
			forma.setFechaInicialModificar(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
			forma.setFechaFinalModificar(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
			forma.setObservacionesModificar(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
			forma.setNitConvenio(forma.getMapaInfoCuentaCobro("nit_convenio_0")+"");
			forma.setFechaRadicacion(forma.getMapaInfoCuentaCobro("fecha_radicacion_0")+"");
			forma.setNumeroRadicacion(forma.getMapaInfoCuentaCobro("numero_radicacion_0")+"");
			forma.setDireccionConvenio(forma.getMapaInfoCuentaCobro("direccion_convenio_0")+"");
			forma.setTelefonoConvenio(forma.getMapaInfoCuentaCobro("telefono_convenio_0")+"");
			forma.setValorTotalCuentaCobro(forma.getMapaInfoCuentaCobro("valor_inicial_cuenta_0")+"");
			forma.setPagosCuentaCobro(forma.getMapaInfoCuentaCobro("valor_pagos_0")+"");
			forma.setSaldoTotalCuentaCobro(forma.getMapaInfoCuentaCobro("saldo_cuenta_0")+"");
			forma.setValorAjusteCredito(forma.getMapaInfoCuentaCobro("ajuste_credito_0")+"");
			forma.setValorAjusteDebito(forma.getMapaInfoCuentaCobro("ajuste_debito_0")+"");
			
			forma.setHoraElaboracion(forma.getMapaInfoCuentaCobro("hora_elaboracion_0")+"");
			
			
			/*double temp=205000000+Double.parseDouble(forma.getMapaInfoCuentaCobro("valor_pagos_0")+"");
			forma.setMapaInfoCuentaCobro("valor_pagos_0",temp+"");*/
			if(UtilidadTexto.numeroEsExponencial(forma.getMapaInfoCuentaCobro("valor_pagos_0")+""))
			{
				  forma.setPagosCuentaCobro(UtilidadTexto.formatearValores(Double.parseDouble(forma.getMapaInfoCuentaCobro("valor_pagos_0")+""), "######"));
			}
			else
				  forma.setPagosCuentaCobro(forma.getMapaInfoCuentaCobro("valor_pagos_0")+"");
			
			//-----------Se consultan los contratos cargue para la cuenta de cobro-----------------------//
			forma.setMapaDetalleCuentaCobro(mundoCxCCapitacion.consultarDetalleCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion));
		
			//-------Se obtiene el valor total de la cuenta de cobro ---------------//
			double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion);
			
			//-----Se agrega validación si devuelve si el estado de cuenta de cobro es anulado devuelve -1 y no trae el valor de la cuenta----///
			if(valorInicialCuenta!=ConstantesBD.codigoNuncaValido){
				//-----------Se guarda el valor inicial de la cuenta de cobro en el atributo -----------//
				//forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
				if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
					  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######"));
				else
					  forma.setTotalCuentaCobro(valorInicialCuenta+"");
			}else
			{
				forma.setTotalCuentaCobro(forma.getValorTotalCuentaCobro());
			}
		}
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que realiza la búsqueda avanzada en el consultar
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBusquedaAvanzadaConsultar(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		ActionErrors errores= new ActionErrors();
		
		//-----------Se consulta la cuenta de cobro-----------------------//
		forma.setMapaCuentasCobroConvenio(mundoCxCCapitacion.consultaAvanzadaCuentaCobro(con, forma.getCuentaCobroBuscar(), forma.getCuentaCobroFinal(), forma.getConvenioCapitado(), forma.getFechaElaboracionModificar(), forma.getEstadoCuentaCobroBuscar(), forma.getFechaInicialModificar(), forma.getFechaFinalModificar(), codigoInstitucion));
		
		int numRegistros=Integer.parseInt(forma.getMapaCuentasCobroConvenio("numRegistros")+"");

		//---------Si no se encuentran informaciòn para los parámetros de búsqueda avanzada se muestra error------------//
		if(numRegistros==0)
		{
			errores.add("noHayCargues", new ActionMessage("error.noExisteInformacionBusqueda"));
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda modificar----------accionBusquedaAvanzadaConsultar-------//
			forma.setEstado("empezarBusquedaAvanzada");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaAvanzada");
		}
		//---------Si encuentra una sóla cuenta de cobro se muestra la información en la página de consultar/imprimir directamente----------//
		else if (numRegistros==1) 
		{
			String cuentaCobroTemp=forma.getMapaCuentasCobroConvenio("numero_cuenta_cobro_0")+"";
			
			//-------------------Se resetea el form------------//
			forma.reset();
			
			//--------------Se setea la varible cuenta de cobro buscar para que se muestra la información de este----------//
			forma.setCuentaCobroBuscar(cuentaCobroTemp);
			
			//-----------Se muestra la información de la cuenta de cobro en la pàgina de modificar-------------//
			return accionBuscarCxCConsultar(con, mapping, forma, request, codigoInstitucion);
		}
		//------------Si la consulta arrojó mas de 1 registro se muestra el listado de cuentas de cobro --------------//
		else 
		{
			//Se obtiene el número de registros por página que se tiene parametrizado
			String numItems=ValoresPorDefecto.getMaxPageItems(codigoInstitucion);
			if(numItems==null || numItems.trim().equals(""))
			{
				numItems="20";
			}
			forma.setMaxPageItems( Integer.parseInt(numItems) );		
			//forma.setMaxPageItems(5);
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("resultadoBusquedaAvanzada");	
		}

	}
	
	/**
	 * Método que consulta los contratos asociados a la cuenta de cobro seleccionada
	 * en el ver detalle
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleCuentaCobroConsultar (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consultan los contratos cargue para la cuenta de cobro-----------------------//
		forma.setMapaDetalleCuentaCobro(mundoCxCCapitacion.consultarDetalleCuentaCobro(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion));
		
	
		//-------Se obtiene el valor total de la cuenta de cobro ---------------//
		double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion);
		
		//-----------Se guarda el valor inicial de la cuenta de cobro en el atributo -----------//
		//forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
		if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
			  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
			else
			  forma.setTotalCuentaCobro(valorInicialCuenta+"");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que consulta los contratos asociados a la cuenta de cobro seleccionada
	 * en el ver detalle
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param numeroCuentaCobro
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleCxCGeneracionConvenio (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, int numeroCuentaCobro, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consulta la cuenta de cobro-----------------------//
		forma.setMapaInfoCuentaCobro(mundoCxCCapitacion.consultarCuentaCobro(con, numeroCuentaCobro, codigoInstitucion, -1));
		
		//-----------------Se guarda lo del mapa en el correspondiente campo-------------------//
		forma.setCuentaCobroBuscar(numeroCuentaCobro+"");
		forma.setNombreEstadoCxCModificar(forma.getMapaInfoCuentaCobro("nombre_estado_0")+"");
		forma.setCodigoEstadoModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_estado_0")+""));
		forma.setNombreConvenioModificar(forma.getMapaInfoCuentaCobro("nombre_convenio_0")+"");
		forma.setCodigoConvenioModificar(Integer.parseInt(forma.getMapaInfoCuentaCobro("codigo_convenio_0")+""));
		forma.setFechaElaboracionModificar(forma.getMapaInfoCuentaCobro("fecha_elaboracion_0")+"");
		forma.setFechaInicialModificar(forma.getMapaInfoCuentaCobro("fecha_inicial_0")+"");
		forma.setFechaFinalModificar(forma.getMapaInfoCuentaCobro("fecha_final_0")+"");
		forma.setObservacionesModificar(forma.getMapaInfoCuentaCobro("obs_generacion_0")+"");
		forma.setNitConvenio(forma.getMapaInfoCuentaCobro("nit_convenio_0")+"");
		forma.setFechaRadicacion(forma.getMapaInfoCuentaCobro("fecha_radicacion_0")+"");
		forma.setNumeroRadicacion(forma.getMapaInfoCuentaCobro("numero_radicacion_0")+"");
		forma.setDireccionConvenio(forma.getMapaInfoCuentaCobro("direccion_convenio_0")+"");
		forma.setTelefonoConvenio(forma.getMapaInfoCuentaCobro("telefono_convenio_0")+"");
		forma.setValorTotalCuentaCobro(forma.getMapaInfoCuentaCobro("valor_inicial_cuenta_0")+"");
		forma.setPagosCuentaCobro(forma.getMapaInfoCuentaCobro("valor_pagos_0")+"");
		forma.setSaldoTotalCuentaCobro(forma.getMapaInfoCuentaCobro("saldo_cuenta_0")+"");
		forma.setValorAjusteCredito(forma.getMapaInfoCuentaCobro("ajuste_credito_0")+"");
		forma.setValorAjusteDebito(forma.getMapaInfoCuentaCobro("ajuste_debito_0")+"");
		
		forma.setHoraElaboracion(forma.getMapaInfoCuentaCobro("hora_elaboracion_0")+"");
		
		//-----------Se consultan los contratos cargue para la cuenta de cobro-----------------------//
		forma.setMapaDetalleCuentaCobro(mundoCxCCapitacion.consultarDetalleCuentaCobro(con, numeroCuentaCobro, codigoInstitucion));
	
		//-------Se obtiene el valor total de la cuenta de cobro ---------------//
		double valorInicialCuenta=Utilidades.obtenerValorInicialCuentaCobroCapitacion(con, numeroCuentaCobro, codigoInstitucion);
		
		//-----------Se guarda el valor inicial de la cuenta de cobro en el atributo -----------//
		//forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
		if(UtilidadTexto.numeroEsExponencial(valorInicialCuenta+""))
			  forma.setTotalCuentaCobro(UtilidadTexto.formatearValores(valorInicialCuenta, "######.00"));
			else
			  forma.setTotalCuentaCobro(valorInicialCuenta+"");
		
		 UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleCuentaCobro");
	}
	
	/**
	 * Método que consulta el listado de las facturas asociadas a la cuenta de cobro
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalleFacturasCxC(Connection con, ActionMapping mapping, HttpServletRequest request,CuentaCobroCapitacionForm forma, int codigoInstitucion) throws SQLException
	{
		ActionErrors errores= new ActionErrors();
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		//-----------Se consultan las facturas asociadas a la cuenta de cobro-----------------------//
		forma.setMapaFacturasCxC(mundoCxCCapitacion.consultarFacturasCxC(con, Integer.parseInt(forma.getCuentaCobroBuscar()), codigoInstitucion));
		
		if(Integer.parseInt(forma.getMapaFacturasCxC("numRegistros")+"")==0)
		{
			errores.add("noHayFacturas", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noHayFacturasCuentaCobro"));
			saveErrors(request, errores);
			
			//---------Se cambia el estado detalle cuenta cobro consultar-----------------//
			forma.setEstado("detalleCuentaCobroConsultar");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleFacturasCxC");
	}
	
	/**
	 * Método que muestra la página de busqueda avanzada de facturas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezarBusquedaFacturas(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma) throws SQLException
	{
		forma.resetBusquedaFacturas();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busquedaFacturas");
	}
	
	/**
	 * Método que consulta la(s) factura(s) que cumplen con los
	 * parámetros de la búsqueda avanzada
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBusquedaAvanzadaFacturas (Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, int codigoInstitucion) throws SQLException
	{
		CuentaCobroCapitacion mundoCxCCapitacion=new CuentaCobroCapitacion();
		
		ActionErrors errores= new ActionErrors();
		
		//---------------------Se realiza la validación de los parámetros de búsqueda avanzada ----------------------------------------//
		if (!UtilidadCadena.noEsVacio(forma.getNumeroFacturaBuscar()) && forma.getViaIngresoBuscar()==-1 && !UtilidadCadena.noEsVacio(forma.getFechaFacturaBuscar()))
		{
			errores.add("noHayParametros", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "UN"));
		}
		else
		{
			//---------Validación de la fecha de factura-----------------//
			if (UtilidadCadena.noEsVacio(forma.getFechaFacturaBuscar()))
			{
				//---------Se verifica el formato de la fecha de factura---------------//
				if (!UtilidadFecha.validarFecha(forma.getFechaFacturaBuscar()))
				{
				errores.add("Fecha Factura Invalido", new ActionMessage("errors.formatoFechaInvalido", " Factura"));
				}
				//--------------Se verifica que la fecha de factura sea menor o igual a la fecha del sistema -----------//
				else if((UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFacturaBuscar())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fechaFactura", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Factura", "actual"));
							}
			}//if
		}//else
		
		//-------------Si existen errores en los parámetros de búsqueda --------------//
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda facturas-----------------//
			forma.setEstado("empezarBusquedaFacturas");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaFacturas");
		}
	
		//-----------Se consultan las facturas de acuerdo a los parámetros de búsqueda-----------------------//
		forma.setMapaFacturasCxC(mundoCxCCapitacion.consultaAvanzadaFacturasCxC(con, forma.getNumeroFacturaBuscar(), forma.getFechaFacturaBuscar(),forma.getViaIngresoBuscar(), codigoInstitucion, forma.getCuentaCobroBuscar()));		
		int numRegistros=Integer.parseInt(forma.getMapaFacturasCxC("numRegistros")+"");

		//---------Si no se encuentran informaciòn para los parámetros de búsqueda avanzada se muestra error------------//
		if(numRegistros==0)
		{
			errores.add("noHayFacturas", new ActionMessage("error.capitacion.cuentaCobroCapitacion.noHayFacturasCuentaCobro"));
			saveErrors(request, errores);
			
			//---------Se cambia el estado a empezar busqueda modificar-----------------//
			forma.setEstado("empezarBusquedaFacturas");
			
			 UtilidadBD.cerrarConexion(con);
			return mapping.findForward("busquedaFacturas");
		}
		else
		{
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("detalleFacturasCxC");
		}
	}
	
	/**
	 * Método que pasa la información de la forma al mundo
	 * @param forma
	 * @param mundoCxCCapitacion
	 */
	private void llenarMundo(CuentaCobroCapitacionForm forma, CuentaCobroCapitacion mundoCxCCapitacion)
	{
		mundoCxCCapitacion.setFechaInicialGeneracion(forma.getFechaInicialGeneracion());
		mundoCxCCapitacion.setFechaFinalGeneracion(forma.getFechaFinalGeneracion());
		mundoCxCCapitacion.setObservacionesCuentaCobro(forma.getObservacionesCuentaCobro());
		mundoCxCCapitacion.setMapaCargues(forma.getMapaCargues());
		
	}
	
	/**
	 * Método que pasa la información de la forma al mundo
	 * @param forma
	 * @param mundoCxCCapitacion
	 */
	private void llenarMundoModificar(CuentaCobroCapitacionForm forma, CuentaCobroCapitacion mundoCxCCapitacion)
	{
		mundoCxCCapitacion.setMapaCargues(forma.getMapaCargues());
		mundoCxCCapitacion.setFechaInicialModificar(forma.getFechaInicialModificar());
		mundoCxCCapitacion.setFechaFinalModificar(forma.getFechaFinalModificar());
		mundoCxCCapitacion.setObservacionesModificar(forma.getObservacionesModificar());
		mundoCxCCapitacion.setCuentaCobroModificar(Integer.parseInt(forma.getCuentaCobroBuscar()));
	}

	
	/**
	 * Metodo Para Imprimir las cuentas de cobro seleccionadas en el listado 
	 * de cuentas de cobro generadas, cuando se realiza por período
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImpresionCuentasCobro(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
        Random r=new Random();
        java.util.Vector archivos = new java.util.Vector();
        int numRandom = r.nextInt();
        String nombreArchivo="/Listado_Cuenta_Cobro_Capitacion_" + numRandom  +".pdf";
        String nombreArchivoCopia="/Listado_Cuenta_Cobro_Capitacion_" + numRandom  +"copia.pdf";
        CuentaCobroCapitacion mundo=new CuentaCobroCapitacion();
   
        
        String listadoCxCImpresion="";
        
        if(forma.getMapaCuentasCobroConvenio() != null && UtilidadCadena.noEsVacio(forma.getMapaCuentasCobroConvenio("numRegistros")+""))
        {
        	listadoCxCImpresion=obtenerCuentasCobroImpresion(forma.getMapaCuentasCobroConvenio());
        }
        
        
        //---- Se consulta el detalle de cargues de las cuentas de cobro ----------//
        if(UtilidadCadena.noEsVacio(listadoCxCImpresion))
        {
        forma.setMapaDetImpresion(mundo.consultarDetalleCuentasCobroImpresion(con, listadoCxCImpresion));
        }
        
        String listadoContratosCargue="";
        
        listadoContratosCargue=obtenerContratosCargueGrupoEtareo(forma.getMapaDetImpresion(), "contrato_cargue_");
        
        //--Se consulta el detalle de los cargues de tipo grupo etáreo para mostrar en la impresiòn
        if(UtilidadCadena.noEsVacio(listadoContratosCargue))
        {
        	forma.setMapaCarguesGrupoEtareo(mundo.consultarDetalleGrupoEtareo(con, listadoContratosCargue));
        }
               
        CuentasCobroCapitacionPdf.pdfListadoCuentasCobro(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request, "Original");
        CuentasCobroCapitacionPdf.pdfListadoCuentasCobro(ValoresPorDefecto.getFilePath() + nombreArchivoCopia, forma, usuario, con, request, "Copia");
        
        UtilidadBD.closeConnection(con);
        
        archivos.add(nombreArchivoCopia);
	    archivos.add(nombreArchivo);
	    request.setAttribute("archivos", archivos);
	    request.setAttribute("nombreVentana1", "Aprobación Pago Capitación");
	    return mapping.findForward("abrirNPdf");      	
	}
	
	/**
	 * Método que obtiene la lista de las cuentas de cobro y las
	 * organiza en un String separado por comas y poderlo enviar a la consulta de detalle
	 * @param mapaCuentasCobroConvenio
	 * @return listadoCxCImpresion
	 */
	private String obtenerCuentasCobroImpresion(HashMap mapaCuentasCobroConvenio) 
	{
	StringBuffer listadoCxCImpresion=new StringBuffer();
	if(mapaCuentasCobroConvenio != null && UtilidadCadena.noEsVacio(mapaCuentasCobroConvenio.get("numRegistros")+""))
	{
		int numRegistros=Integer.parseInt(mapaCuentasCobroConvenio.get("numRegistros")+"");
		int cont=0;
		
		for(int i=0; i<numRegistros; i++)
		{
			if(UtilidadTexto.getBoolean(mapaCuentasCobroConvenio.get("imprimir_"+i)+""))
			{
				if(cont==0)
				{
					listadoCxCImpresion.append(mapaCuentasCobroConvenio.get("cuenta_cobro_"+i)+"");
				}
				else
				{
					listadoCxCImpresion.append(","+mapaCuentasCobroConvenio.get("cuenta_cobro_"+i)+"");
				}
			  cont++;
			}
		}//for
		return listadoCxCImpresion.toString();
	}//if mapaCuentasCobroConvenio!=nul
	else
		return "";
	}


	/**
	 * Metodo para imprimir la cuenta de cobro actual que 
	 * se muestra en pantalla, en la consulta
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirCuentaCobro(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario, boolean generadaXConvenio) throws SQLException
	{
		CuentaCobroCapitacion mundo=new CuentaCobroCapitacion();
        Random r=new Random();
        int numRandom = r.nextInt();
        java.util.Vector archivos = new java.util.Vector();
        String nombreArchivo="/Consulta_Cuenta_Cobro_Capitacion_"+numRandom+".pdf";
        String nombreArchivoCopia="/Consulta_Cuenta_Cobro_Capitacion_"+numRandom+"copia.pdf";
        String listadoContratosCargue="";
        
        listadoContratosCargue=obtenerContratosCargueGrupoEtareo(forma.getMapaDetalleCuentaCobro(), "cargue_");
        
        //--Se consulta el detalle de los cargues de tipo grupo etáreo para mostrar en la impresiòn
        if(UtilidadCadena.noEsVacio(listadoContratosCargue))
        {
        	forma.setMapaCarguesGrupoEtareo(mundo.consultarDetalleGrupoEtareo(con, listadoContratosCargue));
        	forma.setMapaDetalleCuentaCobro("esGrupoEtareo",true);
        }
        else
        {
        	forma.setMapaDetalleCuentaCobro("esGrupoEtareo",false);
        }
        
        forma.setPrefijoFactura(UtilidadesFacturacion.obtenerPrefijoFacturaXInstitucion(con, usuario.getCodigoInstitucionInt()));
        
        CuentasCobroCapitacionPdf.pdfCuentaCobroCapitacion(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request, "Original");
        CuentasCobroCapitacionPdf.pdfCuentaCobroCapitacion(ValoresPorDefecto.getFilePath() + nombreArchivoCopia, forma, usuario, con, request, "Copia");
        
        UtilidadBD.closeConnection(con);
        archivos.add(nombreArchivoCopia);
	    archivos.add(nombreArchivo);
	    request.setAttribute("archivos", archivos);
	    request.setAttribute("nombreVentana1", "Consulta Cuenta Cobro Capitación");
	    return mapping.findForward("abrirNPdf");	    
	}
	
	/**
	 * Método para obtener la lista de cargues que el contrato es de tipo pago grupo etáreo para
	 * realizar la consulta respectiva y mostrar en la impresiòn
	 * @param mapaCargues
	 * @param nombreKey
	 * @return
	 */
	private String obtenerContratosCargueGrupoEtareo(HashMap mapaCargues, String nombreKey) 
	{
	StringBuffer listadoContratosCargue=new StringBuffer();
	if(mapaCargues != null && UtilidadCadena.noEsVacio(mapaCargues.get("numRegistros")+""))
	{
		int numRegistros=Integer.parseInt(mapaCargues.get("numRegistros")+"");
		int cont=0;
		
		for(int i=0; i<numRegistros; i++)
		{
			//-----Se verifica si upc es igual a -1 lo cuál significa que es de tipo grupo etáreo ------//
			if((mapaCargues.get("upc_"+i)+"").equals("-1"))
				{
				if(cont==0)
					listadoContratosCargue.append(mapaCargues.get(nombreKey+i)+"");
				else
					listadoContratosCargue.append(","+mapaCargues.get(nombreKey+i)+"");
				cont++;
			}//if es grupo etáreo
		}//for
		return listadoContratosCargue.toString();
	}//if mapaCuentasCobroConvenio!=null
	else
		return "";
	}
	
	/**
	 * Metodo para imprimir la cuenta de cobro actual que 
	 * se muestra en pantalla, en modificar
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirCuentaCobroModificacion(Connection con, ActionMapping mapping, CuentaCobroCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		CuentaCobroCapitacion mundo=new CuentaCobroCapitacion();
        Random r=new Random();
        int numRandom = r.nextInt();
        java.util.Vector<String> archivos = new java.util.Vector<String>();
        String nombreArchivo="/Modificacion_Cuenta_Cobro_Capitacion_" + numRandom+".pdf";
        String nombreArchivoCopia="/Modificacion_Cuenta_Cobro_Capitacion_" + numRandom+"copia.pdf";
        
        String listadoContratosCargue="";
        
        //Se asigna la informacion al mapa que es usado por el objeto de impresion
        forma.setMapaDetalleCuentaCobro(forma.getMapaCargues());
        //Se cambia el campo código por cargue
        int numRegistros = UtilidadCadena.noEsVacio(forma.getMapaDetalleCuentaCobro("numRegistros")+"") ? Integer.parseInt(forma.getMapaDetalleCuentaCobro("numRegistros")+"") : 0;
        for(int i=0;i<numRegistros;i++)
        	forma.setMapaDetalleCuentaCobro("cargue_"+i, forma.getMapaDetalleCuentaCobro("codigo_"+i));
        
        listadoContratosCargue=obtenerContratosCargueGrupoEtareo(forma.getMapaDetalleCuentaCobro(), "cargue_");
        
        //--Se consulta el detalle de los cargues de tipo grupo etáreo para mostrar en la impresiòn
        if(UtilidadCadena.noEsVacio(listadoContratosCargue))
        {
        	forma.setMapaCarguesGrupoEtareo(mundo.consultarDetalleGrupoEtareo(con, listadoContratosCargue));
        	forma.setMapaCargues("esGrupoEtareo",true);
        }
        else
        {
        	forma.setMapaCargues("esGrupoEtareo",false);
        }
   
        CuentasCobroCapitacionPdf.pdfCuentaCobroCapitacion(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuario, con, request, "Original");
        CuentasCobroCapitacionPdf.pdfCuentaCobroCapitacion(ValoresPorDefecto.getFilePath() + nombreArchivoCopia, forma, usuario, con, request, "Copia");
        
        UtilidadBD.closeConnection(con);
        archivos.add(nombreArchivoCopia);
	    archivos.add(nombreArchivo);
	    request.setAttribute("archivos", archivos);
	    request.setAttribute("nombreVentana1", "Consulta Cuenta Cobro Capitación");
	    return mapping.findForward("abrirNPdf");	    
	}

}
