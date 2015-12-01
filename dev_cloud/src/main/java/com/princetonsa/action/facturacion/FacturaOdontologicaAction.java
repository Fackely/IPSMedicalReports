package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.FacturaOdontologicaForm;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.princetonsa.dto.facturacion.DtoResumenFacturaOdontologica;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.FacturaOdontologica;
import com.princetonsa.mundo.facturacion.ValidacionesFacturaOdontologica;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * WorkFlow de la Factura Odontologica 
 * @author axioma
 *
 */
public class FacturaOdontologicaAction extends Action 
{
	/**
	 * Objeto para almacenar los logs  
	 */
	private Logger logger = Logger.getLogger(FacturaOdontologicaAction.class); 
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)throws Exception
	{
	    if (form instanceof FacturaOdontologicaForm)
	    {
			if (response==null); //Para evitar que salga el warning
			
			FacturaOdontologicaForm forma=(FacturaOdontologicaForm) form;
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			logger.info("\n\n-------------------------------------------------------------------------");
			logger.info("--------------------FacturaOdontologicaAction--ESTADO---"+forma.getEstado()+"-------------------------------");
			logger.info("-------------------------------------------------------------------------\n\n");

			if(forma.getEstado().equals("empezar"))
			{
				return accionEmpezar(mapping, request, forma, paciente, usuario);
			}
			else if(forma.getEstado().equals("generarTotalesFactura"))
			{
				return accionGenerarTotales(mapping, request, forma, paciente,usuario);
			}
			else if(forma.getEstado().equals("volverListadoIngresos"))
			{
				return accionVolverListadoIngresos(mapping, forma, response, request.getSession().getId());
			}
			else if (forma.getEstado().equals("facturacionCancelada"))
			{
				return this.accionFacturacionCancelada(paciente, forma, mapping, response, request);
			}
			else if(forma.getEstado().equals("generarFactura"))
			{
				return this.accionGenerarFactura(paciente, forma, mapping, response, request, usuario, institucion);
			}
			else if(forma.getEstado().equals("imprimirFactura"))
			{
				return this.accionImprimirFactura(paciente, forma, mapping, response, request, usuario);
			}
	    }
	    return null;
	}

	/**
	 * Metodo que realiza la impresion la factura en formato POS
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimirFactura(	PersonaBasica paciente,
													FacturaOdontologicaForm forma, 
													ActionMapping mapping,
													HttpServletResponse response, 
													HttpServletRequest request,
													UsuarioBasico usuario) 
	{
		String nombreRptDesign="FacturaSonriaPOS.rptdesign";
		String newPathReport = "";
    	
		for(DtoResumenFacturaOdontologica dto: forma.getListaResumenFacturas())
		{	
			if(dto.getImprimir())
			{	
				DesignEngineApi comp;
				comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "facturacion/", nombreRptDesign);
					
				newPathReport = comp.saveReport1(false);
				comp.updateJDBCParameters(newPathReport);
				
				//Envio Parametros
				newPathReport += 	"&codigoFactura="+dto.getCodigoPkFactura()+
									"&usuarioReimprime="; // No es requerido
				
				if (!newPathReport.equals("")) 
				{
					dto.setPathImpresion(com.princetonsa.util.birt.reports.ParamsBirtApplication.getBirtViewerPath()+"&__report="+newPathReport);
				}
			}
			else
			{
				dto.setPathImpresion("");
			}
		}	
		
		return mapping.findForward("resumenFactura");
	}

	/**
	 * Metodo que inicia el flujo de facturacion odontologica, hace las siguientes validaciones:
	 * 1. Exista Paciente cargado
	 * 2. Consecutivos Disponibles
	 * 3. Rangos Autorizados de facturacon
	 * 4. Estados Ingreso - Cuenta
	 * 5. Concurrencia cuentas proceso facturacion
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(	ActionMapping mapping,
											HttpServletRequest request, 
											FacturaOdontologicaForm forma,
											PersonaBasica paciente, 
											UsuarioBasico usuario) 
	{
		//1. VALIDAMOS QUE EXISTA PACIENTE CARGADO
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		if(!validaciones.esPacienteCargado(paciente))
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//2. VALIDAMOS LOS CONSECUTIVOS DISPONIBLES
		if(!validaciones.existeConsecutivoFactura(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()))
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//3. VALIDAMOS LOS RANGOS AUTORIZADOS DE FACTURACION
		if(!validaciones.esConsecutivoEnRangoAutorizado(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()))
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//4. VALIDAMOS QUE EXISTA AL MENOS UNA CUENTA-INGRESO A FACTURAR
		forma.setListaIngresosFactura(IngresoGeneral.obtenerIngresosFacturaOdontologica(paciente.getCodigoPersona()));
		if(forma.getListaIngresosFactura().size()<=0)
		{
			validaciones.setearErrorEstadosIngresosCuentaInvalido();
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//5. MANEJO DE CONCURRENCIA
		if(UtilidadValidacion.estaEnProcesofacturacion(paciente.getCodigoPersona(), request.getSession().getId()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
			
		return mapping.findForward("seleccionCuentaIngreso");
	}
	
	/**
	 * Metodo para hacer calculos de los totales para generar la factura
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 * @throws BDException 
	 */
	private ActionForward accionGenerarTotales(	ActionMapping mapping,
												HttpServletRequest request, 
												FacturaOdontologicaForm forma,
												PersonaBasica paciente, 
												UsuarioBasico usuario) throws BDException 
	{
		//1. CARGAMOS LOS RESPONSABLES DE LA CUENTA CON SUS RESPECTIVAS SOLICITUDES
		forma.setListaResponsablesConSolicitudes(FacturaOdontologica.cargarResponsables(forma.getCuenta(), usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
		
		//2. VALIDAMOS LA INFORMACION  POOLES Y LIQUIDACION DE HONORARIOS
		ValidacionesFacturaOdontologica validaciones = new ValidacionesFacturaOdontologica();
		validaciones.validacionesPoolesYLiquidacionHonorarios(forma.getListaResponsablesConSolicitudes(), usuario.getCodigoInstitucionInt());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		
		//2. validamos la vigencia de los contratos dependiendo del parametro general
		//si el parametro general no esta activo entonces se mostrara en los warnings los convenios
		validaciones.esContratosVigentesYTopeValido(forma.getCuenta(), forma.getContratosVector(), usuario.getCodigoInstitucionInt());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}

		//3. VALIDAMOS QUE EL TOTAL A FACTURAR SEA MAYOR QUE CERO
		validaciones.esValorCargoTotalMayorCero(forma.getListaResponsablesConSolicitudes());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//4. REMOVEMOS LOS RESPONSABLES CON VALOR MENOR IGUAL A CERO PARA NO FACTURARLOS
		
		Iterator<DtoResponsableFacturaOdontologica> iterador= forma.getListaResponsablesConSolicitudes().iterator();
		
		while(iterador.hasNext())
		{
			if(iterador.next().getValorTotalNetoCargosEstadoCargado().doubleValue()<=0)
			{
				iterador.remove();
			}
		}
		
		//5. VERIFICACION DEL ANTICIPO O SALDO 
		validaciones.esValorAnticipoConvenioOSaldoPacienteCorrecto(paciente.getCodigoPersona(), forma.getListaResponsablesConSolicitudes());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//6. REALIZAMOS EL BLOQUEO DE LAS CUENTAS EN PROCESO DE FACTURACION
		if(!UtilidadValidacion.estaEnProcesofacturacion(paciente.getCodigoPersona(), ""))
		{
			if(!FacturaOdontologica.empezarProcesoFacturacion(forma.getCuenta().intValue(), usuario.getLoginUsuario(), request.getSession().getId()))
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "", "errors.problemasBd", true);
			}
		}
		
		
		return mapping.findForward("generarTotalesFactura");
	}

	/**
	 * Metodo que regresa de la pagina generar totales a el listado de ingresos
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionVolverListadoIngresos(ActionMapping mapping,
			FacturaOdontologicaForm forma, HttpServletResponse response, String idSesion) 
	{
		forma.setListaResponsablesConSolicitudes(new ArrayList<DtoResponsableFacturaOdontologica>());
		cancelarProcesoFacturacion(null, forma.getCuenta().intValue(), forma, response, idSesion);
		
		if(forma.getListaIngresosFactura().size()>1)
		{	
			return mapping.findForward("seleccionCuentaIngreso");
		}
		//esto envia a la pagina del menu del segund nivel
		return mapping.findForward("menuInicial");
	}

	/**
	 * Metodo para generar la factura odontologica
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @param request
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionGenerarFactura(	PersonaBasica paciente,
												FacturaOdontologicaForm forma, 
												ActionMapping mapping,
												HttpServletResponse response, 
												HttpServletRequest request,
												UsuarioBasico usuario, InstitucionBasica institucion) 
	{
		//1.iniciamos la transaccion
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		
		//2. LLENAMOS LOS DTOS DE FACTURACION 
		ArrayList<DtoFactura> listaFacturas= FacturaOdontologica.proponerEncabezadoFactura(con, forma.getListaResponsablesConSolicitudes(), paciente.getCodigoPersona(), new BigDecimal(paciente.getCodigoCuenta()), usuario, Utilidades.convertirAEntero(institucion.getCodigo()));
		
		ArrayList<Double> listaConsecutivos= new ArrayList<Double>();
		
		//3 LLENAMOS LOS CONSECUTIVOS DE FACTURA
		for(DtoFactura dtoFactura: listaFacturas)
		{
			int tipoFuenteDatos = validaciones.obtenerTipoFuenteDatosFactura(usuario);
			
			dtoFactura.setConsecutivoFactura( validaciones.obtenerSiguienteConsecutivoFactura(con, usuario, tipoFuenteDatos).doubleValue());
			
			validaciones.obtenerDatosParametrizacionParaFactura(dtoFactura.getHistoricoEncabezado(), tipoFuenteDatos, usuario.getCodigoCentroAtencion());
			
			listaConsecutivos.add(dtoFactura.getConsecutivoFactura());
			
			//3.1 VALIDAMOS LOS RANGOS AUTORIZADOS DE FACTURACION
			if(!validaciones.esConsecutivoEnRangoAutorizadoDadoValor(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), new BigDecimal(dtoFactura.getConsecutivoFactura())))
			{
				UtilidadBD.abortarTransaccion(con);
				ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
				this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
				return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionOdontologicaCancelada", validaciones.getErroresFactura(), logger);
			}
		}
		
		//4 INSERTAMOS LA FACTURA
		if(!FacturaOdontologica.insertarFacturas(con, listaFacturas))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//ACTUALIZAR EL ACUMULADO DEL CONTRATO
		if(!FacturaOdontologica.actualizarAcumuladoContratos(con, listaFacturas))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//5.ACTUALIZAMOS EL ESTADO DE LA CUENTA
		if(!FacturaOdontologica.actualizarEstadoCuenta(con, forma.getCuenta()))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//6.ACTUALIZAMOS A FACTURADOS EL ESTADO DE LOS CARGOS
		if(!Factura.actualizarEstadoFacturadoDetalleCargo(con, listaFacturas))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//7.ACTUALIZAMOS LOS ABONOS
		/*
		if(!FacturaOdontologica.insertarAbonos(con, listaFacturas, paciente.getCodigoIngreso(), usuario))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		*/
		
		//8.ACTUALIZACION DE ANTICIPOS
		if(!FacturaOdontologica.insertarAnticipos(con, listaFacturas))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//9.INSERTAMOS LOS HISTORICOS
		if(!Factura.insertarHistoricosSubCuentas(con, listaFacturas))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//10. FINALIZAMOS LOS CONSECUTIVOS DE INSTITUCION
		ValidacionesFacturaOdontologica.finalizarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
		
		//11. eliminamos la cuenta del proceso de facturacion
		if(!Factura.eliminarCuentaProcesoFacturacion(con, forma.getCuenta(), request.getSession().getId()))
		{
			UtilidadBD.abortarTransaccion(con);
			ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			this.cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		cargarListaFacturasResumen(listaFacturas, forma);
		
		//UtilidadBD.abortarTransaccion(con);
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("resumenFactura");
		
	}

	
	
	/**
	 * 
	 * @param listaFacturas
	 * @param forma
	 */
	private void cargarListaFacturasResumen(ArrayList<DtoFactura> listaFacturas, FacturaOdontologicaForm forma) 
	{
		forma.setListaResumenFacturas(new ArrayList<DtoResumenFacturaOdontologica>());
		for(DtoFactura dtoFactura: listaFacturas)
		{
			DtoResumenFacturaOdontologica dtoResumen= new DtoResumenFacturaOdontologica();
			dtoResumen.setCodigoPkFactura(new BigDecimal(dtoFactura.getCodigo()));
			dtoResumen.setConsecutivoFactura(new BigDecimal(dtoFactura.getConsecutivoFactura()));
			dtoResumen.setResponsable(dtoFactura.getConvenio().getNombre());
			dtoResumen.setTotalCargos(new BigDecimal(dtoFactura.getValorTotal()));
			forma.getListaResumenFacturas().add(dtoResumen);
		}
	}

	/**
	 * Metodo para calcelar la cuenta que quedo en proceso de facturacion
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @param request
	 * @return
	 */
	private ActionForward accionFacturacionCancelada(	PersonaBasica paciente,
														FacturaOdontologicaForm forma, 
														ActionMapping mapping,
														HttpServletResponse response, 
														HttpServletRequest request) 
	{
		Connection con=UtilidadBD.abrirConexion();
		try{
			
			
			boolean noRedirecciono=cancelarProcesoFacturacion(con, forma.getCuenta().intValue(), forma, response, request.getSession().getId());
			UtilidadBD.closeConnection(con);
			if(noRedirecciono)
		    {
		    	return mapping.findForward("facturacionCancelada");
		    }
		    else
		    {
		    	return null;
		    }
		}
		catch (Exception e) {
			Log4JManager.info(e.getMessage());
		}
		
		String paginaSiguiente=forma.getSiguientePagina();
		
		if(!paginaSiguiente.equals(""))
		{
			try
			{
				logger.info("paginaSiguiente "+paginaSiguiente);
				response.sendRedirect(paginaSiguiente);
			}
			catch (IOException e)
			{
				logger.error("Error redireccionando a la página seleccionada "+e);
			}
		}
		
		
		return null;
	}
	
	/**
	 * Método para cancelar el proceso de facturacion
	 * @param con
	 * @param idCuenta
	 * @param fact
	 * @return
	 */
	private boolean cancelarProcesoFacturacion(Connection con, int idCuenta, FacturaOdontologicaForm forma, HttpServletResponse response, String idSesion)
	{
		if(con==null)
		{
			con=UtilidadBD.abrirConexion();
		}
		Factura fact= new Factura();
		boolean resultado=fact.cancelarProcesoFacturacion(con, idCuenta, idSesion);
		UtilidadBD.closeConnection(con);
		String paginaSiguiente=forma.getSiguientePagina();
		if(!paginaSiguiente.equals(""))
		{
			try
			{
				//logger.info("paginaSiguiente "+paginaSiguiente);
				response.sendRedirect(paginaSiguiente);
				return false;
			}
			catch (IOException e)
			{
				logger.error("Error redireccionando a la página seleccionada "+e);
			}
		}
		return resultado;
	}
}
