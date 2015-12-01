/*
 * @(#)FacturaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 
 *
 */
package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

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
import util.ElementoApResource;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.ResultadoInteger;
import util.ResultadoString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.FacturaForm;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.PagosPaciente;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.mundo.facturacion.ConsultaFacturas;
import com.princetonsa.mundo.facturacion.DistribucionCuenta;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;
import com.princetonsa.mundo.manejoPaciente.AperturaIngresos;
import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;
import com.princetonsa.pdf.FacturacionPdf;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Clase encargada del control de la funcionalidad 
 * que crea la facturación
 *
 * @version 1.0 Jul 17, 2007
 */
public class FacturaAction extends Action
{
	/**
	 * Objeto para almacenar los logs  
	 */
	private Logger logger = Logger.getLogger(FacturaAction.class); 
	
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
		Connection con= null;
		try{
	    if (form instanceof FacturaForm)
	    {
			if (response==null); //Para evitar que salga el warning
			
			FacturaForm forma=(FacturaForm) form;
			String estado=forma.getEstado();
			logger.warn("\n\n[FacturacionAction]  --> "+estado);

			con= UtilidadBD.abrirConexion();
			
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			//Primera Condición: DEBE EXISTIR UN INGRESO (abierto-cerrado) CON UNA CUENTA VALIDA (Activa, Facturada Parcial, Asociada (Incompleta cuenta_final=null) )
			if(!estado.equals("resumen") && !estado.equals("imprimirFacturas") && !estado.equals("imprimirAnexoS") && !estado.equals("imprimirAnexoC") && !estado.equals("imprimirAnexoI"))
			{	
				ActionForward forward= ValidacionesFactura.esIngresoCuentaValidoCargado(con, paciente, usuario, mapping, request, logger);
				if(forward!=null)
					return forward;
			}	
			
			if (estado.equals("empezar"))
			{
				return this.accionEmpezar(mapping, request, forma, con, paciente, usuario, response, session.getId());
			}
			else if(estado.equals("seleccionConvenios") || estado.equals("volverSeleccionConvenios"))
			{
				return this.accionSeleccionSubCuentas(mapping, request, forma, con, paciente, usuario, response, session.getId());
			}
			else if(estado.equals("generarTotales"))
			{
				return this.accionGenerarTotales(mapping, request, forma, con, paciente, usuario, response, session.getId());
			}
			else if(estado.equals("volverAbonosYDescuentos"))
			{
				return mapping.findForward("abonosYDescuentos");
			}
			else if(estado.equals("validarAbonosYDescuentos"))
			{
				return this.accionValidarAbonosYDescuentos(con, mapping, request, forma, paciente, usuario);
			}
			else if(estado.equals("prefactura"))
			{
				return this.accionPrefactura(con, mapping);
			}
			else if(estado.equals("detallePrefactura"))
			{
				return this.accionDetallePrefactura(con, mapping, forma, usuario, Utilidades.convertirAEntero(institucion.getCodigo()));
			}
			else if(estado.equals("ordenarDetallePrefactura"))
			{
				return this.accionOrdenarDetallePrefactura(con, mapping, forma);
			}
			else if (estado.equals("imprimirPrefactura"))
			{
				return this.accionImprimirPrefactura(mapping,forma,request,paciente,usuario);
			}
			else if(estado.equals("generarFactura"))
			{
				return this.accionGenerarFactura(con, mapping, request, forma, paciente, usuario, response, session.getId());
			}
			else if (estado.equals("resumen"))
			{
			    return this.accionResumen(con, forma, mapping, request, paciente);
			}
			else if (estado.equals("facturacionCancelada"))
			{
				return this.accionFacturacionCancelada(con, paciente, forma, mapping, response, session.getId());
			}
			else if(estado.equals("imprimirFacturas"))
			{
				request.getSession().setAttribute("piePaginaImpresion","original" );
				request.getSession().setAttribute("estadoRetornarFactura","resumenFacturas");
				return this.accionImprimirFacturas(con, mapping, forma, request, paciente, usuario,response);
			}
			else if(estado.equals("insertarFlujoFacturarCuentaSubCuentas"))
			{
				return this.accionInsertarFlujoFacturarCuentaSubCuentas(con, mapping, request, forma, paciente, response, usuario, session.getId());
			}
			else if(estado.equals("facturacionCanceladaFlujoFacturarCuentaSubCuentas"))
			{
				return this.accionFacturacionCanceladaFlujoFacturarCuentaSubCuentas(con, mapping, request, forma, paciente, response, session.getId());
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
	 * @param con
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionFacturacionCancelada(Connection con, PersonaBasica paciente, FacturaForm forma, ActionMapping mapping, HttpServletResponse response, String idSesion) 
	{
		boolean noRedirecciono=cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion); 
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

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, HttpServletRequest request, FacturaForm forma, Connection con, PersonaBasica paciente, UsuarioBasico usuario, HttpServletResponse response, String idSesion) 
	{
		try{
			//reseteamos los atributos del form
			forma.reset();
			
			//validar si el mismo usuario entro a generar la factura entonces toca evaluar es el estado
			UtilidadBD.iniciarTransaccion(con);
			
			forma.setInstitucion(usuario.getCodigoInstitucionInt());
			
			if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), idSesion) )
			{
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
			}
			if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), null) )
			{
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
			}
			
			
			ValidacionesFactura validaciones= new ValidacionesFactura();
			
			//SOLO SE EVALUAN LOS CONSECUTIVOS  DE INSTITUCION EN ESTE PUNTO CUANDO NO ES MULTIEMPRESA
			//Y CUANDO NO ESTAN ACTIVOS LOS CONSECUTIVOS DE INTERFAZ
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
			{
				if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazConsecutivoFacturasOtroSistema(usuario.getCodigoInstitucionInt())))
				{
					//1. EVALUAMOS LOS CONSECUTIVOS DE FACTURA Y EL RANGO AUTORIZADO
					validaciones.esConsecutivoFacturaDentroRangoInstitucion(con, usuario);
				}
				else
				{
					//1. EVALUAMOS CONSECUTIVOS INTERFAZ
					validaciones.existeConsecutivoInterfaz(con, usuario);
				}
				if (validaciones.getErroresFactura().size()>0)
				{
					UtilidadBD.abortarTransaccion(con);
		            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
				}
			}	
	        
			//2. ASIGNAMOS LA(S) CUENTA(S) A LA FORMA PARA HACER LAS VALIDACIONES 
			forma.setCuentas(ValidacionesFactura.obtenerCuentasAValidar(con, paciente));
			
			//VALIDAMOS EL CENTRO DE ATENCION
			validaciones.validacionCentroAtencion(con, usuario, forma.getCuentas());
			if (validaciones.getErroresFactura().size()>0)
			{
				UtilidadBD.abortarTransaccion(con);
	            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
			}
			
			//verificamos que pueda hacer corte de factura
			forma.setPermiteCorteFactura(ValidacionesFactura.viaIngresoPermiteCorteFactura(con, Integer.parseInt(forma.getCuentas().get(0)+"")));
			
			//cambiamos el estado de la cuenta
			this.empezarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), usuario.getLoginUsuario(), paciente, idSesion);
			
			UtilidadBD.finalizarTransaccion(con);
		}
		catch (Exception e) {
			Log4JManager.error("ERROR empezar", e);
			UtilidadBD.abortarTransaccion(con);
		}
		return mapping.findForward("seleccionTipoFacturacion");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionGenerarFactura(Connection con, ActionMapping mapping, HttpServletRequest request, FacturaForm forma, PersonaBasica paciente, UsuarioBasico usuario, HttpServletResponse response, String idSesion) throws IPSException 
	{
		//Validaciones DCU 433 V1.60------------------------------------------------
		ActionErrors errors= new ActionErrors();
		HashMap encabezado = (HashMap) forma.getAbonosYDescuentosMap().get("encabezado");
		int numRegistros = Integer.parseInt(forma.getAbonosYDescuentosMap().get("numRegistros").toString());
		boolean error=false;
		if(UtilidadTexto.getBoolean(
				ValoresPorDefecto.getHacerRequeridoValorAbonoAplicadoAlFacturar(usuario.getCodigoInstitucionInt())))
		{
			
			double abonoDis=Utilidades.convertirAEntero(encabezado.get("abonoDisponible")+"");
			double totalAbono=0;
			for(int w=0;w<numRegistros;w++)
			{
				double neto=0, abonoApli=ConstantesBD.codigoNuncaValidoDoubleNegativo, valPac=0, dctoValor=0;
				
				if(!UtilidadTexto.isEmpty(forma.getAbonosYDescuentosMap().get("vlrPaciente_"+w)+""))
				    valPac = Utilidades.convertirADouble(forma.getAbonosYDescuentosMap().get("vlrPaciente_"+w)+"");
				
				neto=valPac;
				
				if(!UtilidadTexto.isEmpty(forma.getAbonosYDescuentosMap().get("dctoValor_"+w)+""))
					dctoValor=Utilidades.convertirADouble(forma.getAbonosYDescuentosMap().get("dctoValor_"+w)+"");
				
				neto=neto-dctoValor;
				
				if(!UtilidadTexto.isEmpty(forma.getAbonosYDescuentosMap().get("abonoAplicado_"+w)+""))
				{
					abonoApli=Utilidades.convertirADouble(forma.getAbonosYDescuentosMap().get("abonoAplicado_"+w)+"");
					neto=neto-abonoApli;
				}
				
				if(abonoDis>0 && valPac > 0 && neto >0)
				{
					if(abonoApli==ConstantesBD.codigoNuncaValidoDoubleNegativo)
					{	
						error=true;
					}
				}
				if(!error)
					totalAbono+=abonoApli;
			}
			if(!error&&totalAbono==0&&abonoDis>0)
				error=true;
			else
				if(!error&&totalAbono>abonoDis)
					error=true;
		}
		if(error)
		{
			errors.add("Error valor abono", new ActionMessage("error.facturacion.errorValorAbonoAplicado"));
			saveErrors(request, errors);
			return mapping.findForward("abonosYDescuentos");
		}
		//------------------------------------------------------------------------
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		////se resetean los errores, en la generacion solamente se muestran los warning
		forma.setErroresFactura(new ArrayList<ElementoApResource>());
		
		//1.iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//2. PASAMOS LOS VALORES ASIGNADOS EN LOS ABONOS Y DESCUENTOS A LOS OBJETOS DE FACTURA
		pasarValoresAbonosAObjetoFactura(forma, usuario, Utilidades.convertirAEntero(institucion.getCodigo()));
		
		//3. ASIGNAMOS LOS CONSECUTIVOS DE FACTURA
		ResultadoBoolean  resultadoBoolean=validarAsignarConsecutivosFactura(con, forma, usuario);


		boolean inserto=resultadoBoolean.isTrue();
		
		logger.info("1  --> "+inserto);
		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", resultadoBoolean.getDescripcion(), true);
		}
		
		//4. SE ACTUALIZA EL INGRESO
		inserto= this.actualizarEstadoIngreso(con, forma, paciente, usuario);
		logger.info("2");
		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//5. SE INSERTA LA FACTURA - LOS DETALLES - ASOCIOS 
		resultadoBoolean= this.insertarFacturas(con, forma);
		inserto=resultadoBoolean.isTrue();
		logger.info("3");
		
		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
			if(resultadoBoolean.getDescripcion().equals("") || resultadoBoolean.getDescripcion().equals("error.facturacion.noGuardoFactura"))
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
			else
				if(resultadoBoolean.getDescripcion().equals("error.facturacion.noGuardoFactura.consecutivoRepetido"))
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar, consecutivo ya asignado", "error.facturacion.noGuardoFactura.consecutivoRepetido", true);	
		}
		
		//6. SE INSERTAN LOS LOGS DE LOS ABONOS
		AbonosYDescuentos.generarLogHistorial(usuario, forma.getAbonosYDescuentosMap());
		
		//7. SE ACTUALIZA LOS ABONOS DEL PACIENTE APLICADOS A LA FACTURA COMO FACTURADOS
		inserto= this.insertarMovimientosAbonos(con, forma, paciente, usuario);
		
		logger.info("4");

		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//8. SE ACTUALIZAN LOS TOPES X PACIENTE
		inserto= this.insertarTopesPaciente(con, forma, usuario, paciente);
		logger.info("5");

		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//9. SE ACTUALIZA EL VALOR ACUMULADO DEL CONTRATO
		logger.info("6");

		inserto= this.actualizarVrAcumuladoContrato(con, forma, paciente);
		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//10. SE ACTUALIZA 'FACTURADO' EL CENTINELA DE LOS CARGOS
		logger.info("7");
		inserto= Factura.actualizarEstadoFacturadoDetalleCargo(con, forma.getFacturas());
		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//11. SE ACTUALIZA EL ESTADO DE LA CUENTA
		inserto= this.actualizarEstadoCuentaYSubCuentas(con, forma, paciente);
		logger.info("8");

		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//12. SE ACTUALIZA LA INFORMACION DE convenios_paciente (para que la func ingreso paciente , se postule la informacion de los convenios que maneja el paciente)
		inserto=this.actualizarConveniosPaciente(con, forma, paciente);
		logger.info("9");

		if(!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		
		//13. Manejamos la interfaz contable de facturas - shaio
		ResultadoBoolean resultBol= UtilidadBDInterfaz.insertarInterfazContableFactura(con, forma.getFacturas(), usuario, paciente.getConsecutivoIngreso()+"", paciente.getNumeroIdentificacionPersona(), false);
		
		inserto=resultBol.isTrue();
		
		if (!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
			 return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", resultBol.getDescripcion(), false);
		}
		
		//InterfazERPOracle interfazERP=new InterfazERPOracle();
		//interfazERP.insertarInterfazContableFactura(con, forma.getFacturas(), usuario, paciente.getCodigoIngreso()+"", paciente.getNumeroIdentificacionPersona());
		
		//14. manejamos la interfaz de tesoreria - shaio
		resultadoBoolean= insertarInterfazTesoreria(con, forma, usuario, paciente);
		
		inserto=resultadoBoolean.isTrue();
		
		if (!inserto)
		{
			finalizarConsecutvios(forma,ConstantesBD.acronimoNo,ConstantesBD.acronimoNo,usuario.getCodigoInstitucionInt(), con);
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", resultadoBoolean.getDescripcion(), false);
		}
		
		//15 insertamos los historicos de la distribucion en las tablas historico_subcuentas, historico_filtro_distribucion
		inserto=Factura.insertarHistoricosSubCuentas(con, forma.getFacturas());

		logger.info("INSERTO 100%");
		finalizarConsecutvios(forma,ConstantesBD.acronimoSi,ConstantesBD.acronimoSi,usuario.getCodigoInstitucionInt(), con);

		//16 finalizamos el proceso facturacion
		this.finalizarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), ConstantesBD.continuarTransaccion, idSesion);
		
		//UtilidadBD.abortarTransaccion(con);
		
		//TOCA ASIGNAR Y ACTUALIZAR EL CONSECUTIVO AL FINAL, YA QUE SHAIO NO MANEJA LA FILOSOFIA DE MARCAR COMO USADO Y FINALIZADO.
		/*
		 * toca asingar el consecutivo en el metodo centrazilado ya que se hacen validaciones en el cuerpo de la factura.
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazConsecutivoFacturasOtroSistema(usuario.getCodigoInstitucionInt())))
		{
			logger.info("ACTUALIZANDO CONSECUTIVOS DESDE INTERFAZ.");
			if(!this.acutalizarConsecutivosFacturaInterfaz(con,forma,usuario))
			{
				UtilidadBD.abortarTransaccion(con);
				this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response);
		        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
			}
		}
		*/
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumen");
		//ahora verificamos la posibilidad de generar el recibo de caja automatico o de ir de una a la pagina de resumen
	    return accionReciboCajaAutomatico(con, forma, usuario, paciente, request, mapping, response);
	}

	/**
	 * 
	 * @param con
	 * @param facturas
	 * @param codigoInstitucionInt
	 * @return
	 */
	private ResultadoBoolean acutalizarConsecutivosFacturaInterfaz(Connection con, FacturaForm forma, UsuarioBasico usuario) 
	{
		boolean retornado=false;
		ResultadoString resultadoString=null;
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			resultadoString=UtilidadBDInterfaz.obtenerConsecutivoFacturas(usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), true);
			String consecutivo=resultadoString.getResultado();
			
			
			if(!UtilidadTexto.isEmpty(consecutivo)&&!Factura.actualizarConsecutivoFactura(con, forma.getFacturas().get(w).getCodigo(),consecutivo))
			{
				//generrar log de modificacion del consecutivo valor actual consecutivo valor anterior (Utilidades.convertirADouble(consecutivo)-1)
				forma.getFacturas().get(w).setConsecutivoFactura(Utilidades.convertirADouble(consecutivo));
				retornado=true;
			}
			else
			{
			
				logger.error("error actualizando el consecutivo de la interfaz");
				return new ResultadoBoolean(false,resultadoString.getDescripcion());
			}
		}
		logger.info("Retornado --->"+retornado);
		return   new ResultadoBoolean(retornado,"");	
	}

	/**
	 * 
	 * @param forma
	 * @param usado
	 * @param finalizado
	 * @param institucion
	 * @param con Conexión con la BD
	 */
	private void finalizarConsecutvios(FacturaForm forma, String usado,String finalizado, int institucion, Connection con) 
	{
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazConsecutivoFacturasOtroSistema(institucion)))
		{
			for(int i=0;i<forma.getFacturas().size();i++)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturas, institucion, forma.getFacturas().get(i).getConsecutivoFactura()+"", usado, finalizado);
				
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ResultadoBoolean insertarInterfazTesoreria(Connection con, FacturaForm forma, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			if(Utilidades.convertirADouble(forma.getFacturas().get(w).getValorAbonos()+"")>0)
			{
				DtoInterfazAbonos dtoInterfazAbonos= new DtoInterfazAbonos();		
				dtoInterfazAbonos.setCodigoPaciente(paciente.getCodigoPersona()+"");
				dtoInterfazAbonos.setEstadoRegistro(ConstantesBDInterfaz.codigoEstadoProcesadoAxioma+"");
				dtoInterfazAbonos.setInstitucion(usuario.getCodigoInstitucionInt());
				dtoInterfazAbonos.setNumeroDocumento(forma.getFacturas().get(w).getConsecutivoFactura()+"");
				dtoInterfazAbonos.setSigno("-");
				dtoInterfazAbonos.setTipoMov(ConstantesBDInterfaz.codigoTipoMovimientoGenerarFactura+"");
				dtoInterfazAbonos.setValor(forma.getFacturas().get(w).getValorAbonos()+"");
				dtoInterfazAbonos.setTipoIdentificacion(paciente.getCodigoTipoIdentificacionPersona());
				dtoInterfazAbonos.setNumIdentificacion(paciente.getNumeroIdentificacionPersona());
				
				ResultadoBoolean resultadoBoolean = UtilidadBDInterfaz.insertarInterfazTesoreria(usuario, dtoInterfazAbonos);
				
				if(!resultadoBoolean.isTrue())
				{
					logger.error("No inserto la interfaz tesoreria");
					return new ResultadoBoolean(false,resultadoBoolean.getDescripcion());
				}
			}	
		}
		return new ResultadoBoolean(true,"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionReciboCajaAutomatico(Connection con, FacturaForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping, HttpServletResponse response)
	{
		forma.setNumeroReciboCajaGenerado("");
		logger.info("-----------------------El numero de facturas generadas es--->"+forma.getFacturas().size());
		
		if(forma.getFacturas().size()==1)
		{
			boolean esViaRCAutomatico=UtilidadValidacion.esReciboCajaAutomaticoViaIngresoDada(con, forma.getFacturas().get(0).getViaIngreso().getCodigo());
			logger.info("neto paciente->"+forma.getFacturas().get(0).getValorNetoPaciente()+" via automatic="+esViaRCAutomatico+" via->"+forma.getFacturas().get(0).getViaIngreso().getCodigo());
			
			if(esViaRCAutomatico && forma.getFacturas().get(0).getValorNetoPaciente()>0){
				
				forma.setNumeroReciboCajaGenerado("");
				Vector<Object> tiposIngresoTesoreria= new Vector<Object>();
				tiposIngresoTesoreria.add(ConstantesBD.codigoTipoIngresoTesoreriaPacientes);
				//String[] codigoDescripcionConcepto=UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), forma.getFacturas().get(0).getTipoRegimenAcronimo(), tiposIngresoTesoreria);
				boolean existenConceptos=(Utilidades.convertirAEntero(UtilidadValidacion.getCodigoDescripcionConceptoTipoRegimen(con, usuario.getCodigoInstitucionInt(), forma.getFacturas().get(0).getTipoRegimenAcronimo(), tiposIngresoTesoreria, true).get("numRegistros")+"")>0);
				
				setObservable(paciente, request, false);
				//primero se valida el acceso a recibos de caja 
				ValidacionesFactura validacionesFactura= new ValidacionesFactura();
				validacionesFactura.validacionesReciboCajaAutoMatico(con, usuario, existenConceptos);
				if(validacionesFactura.getWarningsFactura().size()<=0)
				{
					String rutaRC="recibosCaja/recibosCaja.do?estado=generarEstructuraDesdeFacturaSeleccion" +
									"&tipoRegimenAcronimo="+forma.getFacturas().get(0).getTipoRegimenAcronimo()+""+
									//"&codigoConceptoIngTesoreriaFacturaUnica="+codigoDescripcionConcepto[0]+"" +
									//"&descConceptoIngTesoreriaFacturaUnica="+codigoDescripcionConcepto[1]+"" +
									"&consecutivoFact="+UtilidadTexto.formatearExponenciales(forma.getFacturas().get(0).getConsecutivoFactura())+"&esWorflowFactura=true";
					
					//en caso de que no existan errores entonces enviar de una a la 
					//pagina de recibos de caja
					UtilidadBD.closeConnection(con);

					/* La validación del número de cajas disponibles quedó centralizada en recibos de caja
						//si no esta cargada entonces ir a la pagina de seleccion comun, ya se valido q el usuario tenga una
						if(usuario.getCodigoCaja()==ConstantesBD.codigoNuncaValido)
						{
							request.setAttribute("ruta", rutaRC);
						    return mapping.findForward("cargarCajaUsuario");
						}
						
						//de lo contrario carga de una la pagina de RC
						
					 */
					try 
				    {
						response.sendRedirect("../"+rutaRC);
						return null;
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
						return null;
					}
				}
				//asignamos entonces los warnings
				else
				{
					for(int w=0; w<validacionesFactura.getWarningsFactura().size(); w++)
					{
						forma.setWarningFactura(validacionesFactura.getWarningFactura(w));
					}
				}
			}
		}
		//si llega a este punto es que no pudo entrar a recibos de caja entonces se tiene 
		// que direccionar a la pagina de resumen
		
		UtilidadBD.closeConnection(con);
		try 
	    {
			response.sendRedirect("../facturacion/facturacion.do?estado=resumen");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private boolean actualizarConveniosPaciente(Connection con, FacturaForm forma, PersonaBasica paciente) 
	{
		for(int w=0; w<forma.getFacturas().size(); w++)
	    {
	    	DtoFactura dtoFactura= forma.getFacturas().get(w);
	    	if(!Convenio.actualizarConveniosPacientes(con, paciente.getCodigoPersona(), dtoFactura.getConvenio().getCodigo(), dtoFactura.getEstratoSocial().getCodigo(), dtoFactura.getTipoAfiliado(), ""))
	    		return false;
	    }	
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @return
	 */
	private boolean actualizarVrAcumuladoContrato(Connection con, FacturaForm forma, PersonaBasica paciente )
	{
	    for(int w=0; w<forma.getFacturas().size(); w++)
	    {
	    	DtoFactura dtoFactura= forma.getFacturas().get(w);
	    	Vector subCuentas= new Vector();
	    	subCuentas.add(String.valueOf((long)dtoFactura.getSubCuenta()));
	    	Vector contrato= ValidacionesFactura.obtenerContratosSubCuentas(con, paciente.getCodigoIngreso()+"", subCuentas, "" /*facturado*/);
	    	
	    	if(contrato.size()==1)
	    	{
	    		logger.info("******Contrato  *******codigoContrato=== "+contrato.get(0));
		        logger.info("*************valorTotal=== "+dtoFactura.getValorTotal());
		        logger.info("*************valorConvenio=== "+dtoFactura.getValorConvenio());
		        if(Contrato.actualizarValorAcumulado(con, Integer.parseInt(contrato.get(0).toString()), dtoFactura.getValorConvenio())<0)
		        {
		        	logger.info("no actualizo el valor del contrato ->"+contrato.get(0)+" verificar!!!");
		        	return false;
		        }	
	    	}
	    	else
	    	{
	    		logger.info("existe mas de un contrato para la subcuenta "+dtoFactura.getSubCuenta()+" por favor verificar para actualizar el acumulado");
	    		return false;	
	    	}	
	    }
	    return true;
	}    
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private boolean actualizarEstadoFacturadoSubCuenta(Connection con, FacturaForm forma, PersonaBasica paciente) throws IPSException
	{
		ArrayList<DtoSubCuentas> subCuentasArray= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, paciente.getCodigoIngreso(),true, new String[0],false, "" /*subCuenta*/,paciente.getCodigoUltimaViaIngreso());
		
		for(int w=0; w<subCuentasArray.size(); w++)
		{	
			if(!Cargos.actualizarEstadoFacturadoSubCuenta(con, subCuentasArray.get(w).getSubCuentaDouble(), ConstantesBD.acronimoSi))
			{
				logger.info("no actualizo a facturado la sub cuenta "+subCuentasArray.get(w).getSubCuentaDouble());
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param usuario 
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionDetallePrefactura(Connection con, ActionMapping mapping, FacturaForm forma, UsuarioBasico usuario, int institucion) 
	{
		//mostramos los detalles de esa factura
		DtoFactura dtoFactura= forma.getFacturas().get(forma.getIndex());
		pasarValoresAbonosAObjetoFactura(forma, usuario, institucion); 
		forma.setMapaPrefacturas((HashMap)Factura.proponerPreFactura(con, dtoFactura).clone());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePrefactura");
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarDetallePrefactura(Connection con,  ActionMapping mapping, FacturaForm forma) 
	{
		String[] indices= {"centrocosto_", "nombrecentrocosto_", "codarticuloservicio_", "descarticuloservicio_", "cantidadcargo_", "valortotal_", "valorunitario_"};
		int numReg=Integer.parseInt(forma.getMapaPrefacturas("numRegistros")+"");
		forma.setMapaPrefacturas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPrefacturas(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaPrefacturas("numRegistros",numReg+"");
		forma.setEstado("detallePrefactura");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePrefactura");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionPrefactura(Connection con, ActionMapping mapping) 
	{
		//primero se muestra un listado con las posibles facturas
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPrefactura");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionSeleccionSubCuentas(ActionMapping mapping, HttpServletRequest request, FacturaForm forma, Connection con, PersonaBasica paciente, UsuarioBasico usuario, HttpServletResponse response, String idSesion) throws IPSException 
	{
		ValidacionesFactura validaciones= new ValidacionesFactura();	
		validaciones.esEgresoPacienteValido(con, forma.getTipoFacturacion(), forma.getCuentas(), usuario.getCodigoInstitucionInt());
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(usuario.getCodigoInstitucionInt())))
		{
			validaciones.validacionAccidentesTransito(con, paciente, forma.getTipoFacturacion());
			validaciones.validacionEventosCatastroficos(con, paciente, forma.getTipoFacturacion());
		}
		validaciones.validacionProcesoAsocio(con, paciente);
		
		if(forma.getTipoFacturacion()==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			validaciones.validarCierreNotasEnfermeriaGenerarFactura(con, paciente);
			validaciones.validarCierreEpicrisisFinaGenerarFactura(con,forma.getCuentas(),paciente.getCodigoIngreso());
		}
		
		if (validaciones.getErroresFactura().size()>0)
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
		}
		//cargamos la informacion de las subcuentas - y la informacion de la ultima distribucion
		forma.setSubCuentasMap(Factura.obtenerSubCuentasAFacturar(con, paciente.getCodigoIngreso()+""));
		
		//1. PRIMERO EVALUAMOS SI EXISTEN SOLICITUDES PENDIENTES, EN CASO DE QUE todo SEA CERO ENTONCES SE ENVIA POR FLUJO DE CAMBIAR LA CUENTA A ESTADO FACTURADO
		if(forma.getTipoFacturacion()==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{	
			ActionForward forward= flujoFacturarCuentaSubCuentas(con, mapping, forma);
			if(forward!=null)
				return forward;
		}	
		
		HashMap mapaDistribucion=DistribucionCuenta.consultarEncabezadoUltimaDistribucionEstatico(con, paciente.getCodigoIngreso());
		
		logger.info("Mapa dist->"+mapaDistribucion);
		forma.setSubCuentasMap("distribucion", mapaDistribucion);
		
		//ESTO YA NO APLICA X CONVENIOS CAPITADOS
		//se valida que no se dupliquen los convenios a facturar, esto puede suceder porque en la facturacion anterior se podia tener el mismo responsable x cuenta, y en
		//esta nueva implementacion se tiene es por ingreso, entonces en la actualizacion de bd se puede tener el mismo responsable mas de una vez por ingreso, si ocurre el caso
		//de que alguno de ellos no este facturado entonces toca indicarles que realicen el proceso por distribucion de la cuenta
		/*validaciones.validacionResponsableRepetido1(forma.getSubCuentasMap());
		if (validaciones.getErroresFactura().size()>0)
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response);
            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
		}*/
		
		forma.resetAbonos();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seleccionConvenios");
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward flujoFacturarCuentaSubCuentas(Connection con, ActionMapping mapping, FacturaForm forma) 
	{
		//verificamos para todos los responsables que todas las solicitudes esten facturadas, eso quiere decir q esten en cargos 'F' y que no existan solicitudes de cx sin liquidar  
		if(!ValidacionesFactura.existenSolicitudesNoFacturadas(con, forma.getCuentas(), forma.getSubCuentasVector()))
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("flujoFacturarCuentaSubCuentas");
		}
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param response
	 * @return
	 */
	private ActionForward accionInsertarFlujoFacturarCuentaSubCuentas(Connection con, ActionMapping mapping, HttpServletRequest request, FacturaForm forma, PersonaBasica paciente, HttpServletResponse response, UsuarioBasico usuario, String idSesion) throws IPSException 
	{
		UtilidadBD.iniciarTransaccion(con);
		if(!this.actualizarEstadoCuentaYSubCuentas(con, forma, paciente))
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		if(!this.actualizarEstadoIngreso(con, forma, paciente, usuario))
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar", "error.facturacion.noGuardoFactura", true);
		}
		setObservable(paciente, request, true);
		//finalizamos el proceso facturacion
		this.finalizarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), ConstantesBD.continuarTransaccion, idSesion);
		logger.info("INSERTO 100%");
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("insertarFlujoFacturarCuentaSubCuentasExitoso");
		return mapping.findForward("flujoFacturarCuentaSubCuentas");
	}

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param response
	 * @return
	 */
	private ActionForward accionFacturacionCanceladaFlujoFacturarCuentaSubCuentas(Connection con, ActionMapping mapping, HttpServletRequest request, FacturaForm forma, PersonaBasica paciente, HttpServletResponse response, String idSesion) 
	{
		this.accionFacturacionCancelada(con, paciente, forma, mapping, response, idSesion);
		ValidacionesFactura validaciones= new ValidacionesFactura();
		ElementoApResource elem=new ElementoApResource("label.facturacion.facturacionCancelada");
		ArrayList<ElementoApResource> array= new ArrayList<ElementoApResource>();
		array.add(elem);
		validaciones.setErroresFactura(array);
		return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
	}
	
	/**
     * Metodo para Imprimir la Factura Detallado por solicitud
     * @param con
     * @param consultaFacturasForm
     * @param medico
     * @param request
     * @param mapping
     * @param paciente
     * @return
     */
    private String generarReporteAnexoSolicitud(Connection con, FacturaForm forma, UsuarioBasico medico, HttpServletRequest request, ActionMapping mapping, PersonaBasica paciente, int w) 
    {
    	String path="";
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	//Modificado por la Tarea 57806. Ya que no se ve la importancia del IF
    	//porque cuando es el check de Todos estaba sacando los reportes en blanco
    	//por lo de codigo de la factura
    	/*if(forma.getCheckTodos().equals("1"))
    		path = EstadoCuenta.imprimirFacturaDetalladoSolicitud(con, request, medico, paciente, "-1", paciente.getNombreCentroAtencionPYP(), "", "", "", "", "", "", "", "", "");
    	else
    	{*/
    		mapa=ConsultaFacturas.consultaDatosFactura(con, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString());
			path = EstadoCuenta.imprimirFacturaDetalladoSolicitud(con, request, medico, paciente, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString(), mapa.get("centroatencion_0").toString(),  mapa.get("entidadsub_0").toString(), mapa.get("estadocuenta_0").toString(), mapa.get("tipomonto_0").toString(), mapa.get("estadoingreso_0").toString(), mapa.get("natupaciente_0").toString(), mapa.get("nombreconvenio_0").toString(), mapa.get("clasecon_0").toString(), mapa.get("subcuenta_0").toString());
    	//}
    	if(!path.equals(""))
    		return path;
    	else
    		return "";
    }
    
    /**
     * Metodo para Imprimir la Factura Resumido por Centro de Costo
     * @param con
     * @param forma
     * @param medico
     * @param request
     * @param mapping
     * @param paciente
     * @return
     */
    private String generarReporteAnexoCC(Connection con, FacturaForm forma, UsuarioBasico medico, HttpServletRequest request, ActionMapping mapping, PersonaBasica paciente, int w) 
    {
    	String path="";
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	Vector archivosGenerados=new Vector();
    	//Modificado por la Tarea 57806. Ya que no se ve la importancia del IF
    	//porque cuando es el check de Todos estaba sacando los reportes en blanco
    	//por lo de codigo de la factura
    	/*if(forma.getCheckTodos().equals("1"))
    		path = EstadoCuenta.imprimirFacturaResumidoCC(con, request, medico, paciente, "-1", paciente.getNombreCentroAtencionPYP(), "", "", "", "", "", "", "", "", "");
    	else
    	{*/
	    	mapa=ConsultaFacturas.consultaDatosFactura(con, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString());
			path = EstadoCuenta.imprimirFacturaResumidoCC(con, request, medico, paciente, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString(), mapa.get("centroatencion_0").toString(),  mapa.get("entidadsub_0").toString(), mapa.get("estadocuenta_0").toString(), mapa.get("tipomonto_0").toString(), mapa.get("estadoingreso_0").toString(), mapa.get("natupaciente_0").toString(), mapa.get("nombreconvenio_0").toString(), mapa.get("clasecon_0").toString(), mapa.get("subcuenta_0").toString());
    	//}
    	if(!path.equals(""))
    		return path;
    	else
    		return "";
    }
    
    /**
     * Metodo para Imprimir la Factura Detallado por Item
     * @param con
     * @param forma
     * @param medico
     * @param request
     * @param mapping
     * @param paciente
     * @return
     */
    private String generarReporteAnexoIT(Connection con, FacturaForm forma, UsuarioBasico medico, HttpServletRequest request, ActionMapping mapping, PersonaBasica paciente, int w) 
    {
    	String path="";
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	Vector archivosGenerados = new Vector();
    	//Modificado por la Tarea 57806. Ya que no se ve la importancia del IF
    	//porque cuando es el check de Todos estaba sacando los reportes en blanco
    	//por lo de codigo de la factura
    	/*if(forma.getCheckTodos().equals("1"))
    		path = EstadoCuenta.imprimirFacturaDetalladoIT(con, request, medico, paciente, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString(), paciente.getNombreCentroAtencionPYP(), "", "", "", "", "", "", "", "", "", "");
    	else
    	{*/
    		mapa = ConsultaFacturas.consultaDatosFactura(con, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString());
			path = EstadoCuenta.imprimirFacturaDetalladoIT(con, request, medico, paciente, forma.getCodigosFacturasImprimir("codigoFactura_"+w).toString(), mapa.get("centroatencion_0").toString(), mapa.get("entidadsub_0").toString(), mapa.get("estadocuenta_0").toString(), mapa.get("tipomonto_0").toString(), mapa.get("estadoingreso_0").toString(), mapa.get("natupaciente_0").toString(), mapa.get("codigoconvenio_0").toString(), mapa.get("nombreconvenio_0").toString(), mapa.get("clasecon_0").toString(), mapa.get("subcuenta_0").toString());
    	//}
    	if(!path.equals(""))
    		return path;
    	else
    		return "";
    }
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param response
	 * @return
	 * @throws IPSException 
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionGenerarTotales(ActionMapping mapping, HttpServletRequest request, FacturaForm forma, Connection con, PersonaBasica paciente, UsuarioBasico usuario, HttpServletResponse response, String idSesion) throws IPSException 
	{
		ValidacionesFactura validaciones= new ValidacionesFactura();
		
		validaciones.esConvenioInterfazValido(con, forma.getSubCuentasMap(), usuario.getCodigoInstitucionInt());
		
		//valiacion de pacientes capitados
		validaciones.esPacienteCapitadoVigente(con, forma.getSubCuentasMap(), paciente.getCodigoPersona(), paciente.getCodigoIngreso());
		
		//PARA EL CASO DE MULTIEMPRESA DEBEMOS VALIDAR LOS RANGOS DE FACTURAS DE LA EMPRESA INSTITUCION X SUBCUENTA
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{
			validaciones.sonConsecutivosFacturasMultiempresaDentroRango(con, forma.getEmpresasInstitucionSeleccionadasVector());
			if (validaciones.getErroresFactura().size()>0)
			{
				UtilidadBD.abortarTransaccion(con);
				this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
	            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
			}
		}
		
		//1. validamos la verificacion de derechos
		validaciones.esVerificacionDerechosValida(con, paciente.getCodigoIngreso()+"", forma.getSubCuentasMap(), forma.getCuentas());
		
		//2.validamos los estados de las solicitudes
		validaciones.obtenerSolicitudesEstadosInvalidos(con, usuario, forma.getCuentas(), forma.getSubCuentasSeleccionadasVector(), forma.getTipoFacturacion());
		
		//3. validamos la info de los pooles
		validaciones.esPoolValidoXSolicitud(con, forma.getCuentas(), forma.getSubCuentasSeleccionadasVector(), usuario.getCodigoInstitucionInt());
		
		//4. validamos la vigencia de los contratos dependiendo del parametro general
		//si el parametro general no esta activo entonces se mostrara en los warnings los convenios
		
		/**
		 * MT 765 Version Cambio 1.61
		 * Diana Ruiz
		 */
		
		if(!paciente.isConvenioCapitado()){
			validaciones.esContratosVigentesYTopeValido(con, paciente.getCodigoIngreso()+"", forma.getSubCuentasSeleccionadasVector(), forma.getCuentas(), usuario, forma.getSubCuentasSeleccionadasYSinInfoAdicVenezuelaVector());
		}
		
		//9. validamos que las solicitudes de pyp sean cubiertas por el convenio
		validaciones.analisisSolicitudesPYP(con, forma.getCuentas(), forma.getSubCuentasSeleccionadasVector());
		
		//validamos las autorizaciones
		validaciones.validacionesAutorizacionesYCobertura(con, paciente.getCodigoIngreso()+"", forma.getSubCuentasSeleccionadasVector(), forma.getCuentas());
		
		//5. verificamos que al menos un contrato tenga un valor total a facturar mayor a cero.
		//si todos son cero entonces se muestra un warning
		//el metodo devuelve el mapa convenios pero cuando el valor es cero entonces lo coloca como seleccionado_w como N para 
		//que NO tener en cuenta una factura con valor cero (0) agrega tambien key valorcargototal_w
		forma.setSubCuentasMap(validaciones.esValorCargoTotalMayorCero(con, forma.getCuentas(), forma.getSubCuentasMap()));
		
		//6. se verifica la naturaleza del paciente x subcuenta  y se evalua si tiene excepcion, en el caso de que sea Si indica
		//que no se le debe cobrar ningun valor al paciente y que todo el valor de los cargos le corresponde al convenio responsable
		//el metodo devuelve un mapa convenios con el valor actualizado de pacientetieneexcepcionnaturaleza
		
		/**
		 * Inc. 1238
		 * Se elimina la validación de la naturaleza de montos de cobro por el tema de franquicias.
		 * DCU 433 Versión 1.62
		 * @author Diana Ruiz
		 * @since 19/08/2011
		 */
		//forma.setSubCuentasMap(validaciones.obtenerNaturalezaPaciente(con, forma.getSubCuentasMap(), paciente));
		
		//7. Obtenemos los valores a cargo del paciente y del convenio sin aplicar topes
		forma.setSubCuentasMap(validaciones.obtenerValorPacienteYConvenioSinAplicarTopes(con, forma.getSubCuentasMap(), paciente.getCodigoIngreso()+""));
		
		//8. verificamos la parametrizacion del parametro general de topes
		validaciones.esParametroGeneralTopesValido(usuario);
		
		//9. Verificamos si se permite facturar reingresos independientes
		validaciones.facturarReingresosIndependientes(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoIngreso());
		
		//10. validamos el tipo liquidacion 
		//validaciones.existeTipoLiquidacionPoolXConvenios(con, forma.getConveniosSeleccionadosList(), usuario.getCodigoInstitucionInt());
		validaciones.existeTipoLiquidacionXMedicoResponde(con, forma.getCuentas(), forma.getSubCuentasSeleccionadasVector(), usuario.getCodigoInstitucionInt());
		
		if (validaciones.getErroresFactura().size()>0)
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
		}
	
		//si no existen errores entonces se cargan los warnings
		forma.setWarningsFactura(validaciones.getWarningsFactura());
		
		//como no existen errores entonces podemos proponer las posibles facturas
		try{
			forma.setFacturas(Factura.proponerFacturas(con, forma.getCuentas(), forma.getSubCuentasMap(), usuario, paciente));
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
            return ComunAction.envioMultiplesErrores(mapping, request, con, CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO.toString(), new ArrayList(), logger);
		}
		
		//despues de propuestas las facturas entonces se evalua que el porcentaje pool de las solicitudes - asocios - paquetes existan, 
		//ACLARACION: EN EL CASO DE CX Y PAQUETES SOLO SE EVALUA EL PORCENTAJE POOL PARA LOS COMPONENTES Y ASOCIOS, DESDE EL SQL PARA ESTOS CASOS EN LA PROPIA SOLICITUD VIENEN EN CERO CUANDO NO EXISTEN 
		validaciones.existePorcentajeOValorPoolXSolicitud(con,forma.getFacturas(), usuario.getCodigoInstitucionInt());
		
		//se evalua que no tengan consumos de materiales pendientes
		validaciones.validacionesConsumosMaterialesCx(con, forma.getFacturas());
		
		if (validaciones.getErroresFactura().size()>0)
		{
			UtilidadBD.abortarTransaccion(con);
			this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);
            return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCancelada", validaciones.getErroresFactura(), logger);
		}
		
		//verificamos si todas las solicitudes son de pyp para cobrarle 0 pesos al paciente
		forma.setFacturas(validaciones.recalcularValorPacienteXPYP(con, forma.getFacturas(), usuario, paciente, forma.getTipoFacturacion()));
		
		//ahora podemos aplicar los topes porque ya tenemos cargados los diagnosticos x detalle_factura para el caso de consulta externa
		// o tenemos en el encabezado de factura el dx de egreso
		forma.setFacturas(validaciones.recalcularValorPacienteXTopes(con, forma.getFacturas(), usuario, paciente, forma.getTipoFacturacion()));
		
		//se evalua el parametro del convenio -> Asignar Valor Paciente con el Valor de los Abonos = Si
		forma.setFacturas(validaciones.recalcularValorPacienteXParametroAsigValPacienteValAbonos(con, forma.getFacturas()));
		
		//validamos la informacion para venezuela
		forma.setFacturas(validaciones.validacionInformacionVenezuela(forma.getFacturas()));
		
		//si no existen errores entonces se cargan los warnings de venezuela
		forma.setWarningsFactura(validaciones.getWarningsFactura());
		
		//cargamos la estructura de abonos y descuentos
		AbonosYDescuentos abonosDescuentos= new AbonosYDescuentos();
		abonosDescuentos.cargarAbonosYDescuentos(con, forma.getFacturas(), paciente, usuario.getCodigoInstitucionInt());
		forma.setAbonosYDescuentosMap((HashMap)abonosDescuentos.getMapaGeneral().clone());
		
		//tiene que ser paciente capitado 
		if(paciente.isConvenioCapitado()){
 
			// se valida el parametro getRequiereAutorizacionCapitacionSubcontratadaParaFacturar para saber si se debe validar 
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(usuario.getCodigoInstitucionInt()))){

				//se valida si cada una de las solucitudes tiene una autorización asiganda
				validaciones.validarSolicitudesSinAutorizaciones(con, forma.getFacturas());

				//si existen alguna solucitud sin autorizacion asociada se muestra mensaje de error y se cancela la facturacion
				if (validaciones.getErroresFactura().size()>0)
				{
					//se cierra la conexion 
					UtilidadBD.abortarTransaccion(con);

					//se cancela el proceso de la facturacion
					this.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCuentas().get(0).toString()), forma, response, idSesion);

					//se envia mensaje a mostrar en pantalla
					return ComunAction.envioMultiplesErrores(mapping, request, con, "label.facturacion.facturacionCanceladaAutorizacionespendiente", validaciones.getErroresFactura(), logger);
				}

			}
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("abonosYDescuentos");
	}

	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionValidarAbonosYDescuentos(Connection con, ActionMapping mapping, HttpServletRequest request, FacturaForm forma, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		if(!forma.getEstadoAux().equals("volverSeleccionConvenios"))
		{
			ValidacionesFactura validaciones= new ValidacionesFactura();
			validaciones.validacionesAbonosYDescuentosVlrPacienteAbonos(con, forma.getAbonosYDescuentosMap());
			
			if(validaciones.getErroresFactura().size()>0)
			{
				forma.setErroresFactura(validaciones.getErroresFactura());
				UtilidadBD.closeConnection(con);
				forma.setEstadoAux("volverAbonosYDescuentos");
				forma.setEstado(forma.getEstadoAux());
				return mapping.findForward("paginaMentira");	
			}
			
			/**
			 * MT 765 Version Cambio 1.61
			 * Diana Ruiz - Camilo
			 */
			if(!paciente.isConvenioCapitado()){
				validaciones.validacionesAbonosYDescuentosValorContrato(con, forma.getAbonosYDescuentosMap(), paciente, usuario);
			}

			//si la validacion del valor contrato esta como un error entonces se cancela el proceso de facturacion
			if (validaciones.getErroresFactura().size()>0)
			{
				forma.setErroresFactura(validaciones.getErroresFactura());
				UtilidadBD.closeConnection(con);
				forma.setEstadoAux("volverAbonosYDescuentos");
				forma.setEstado(forma.getEstadoAux());
				return mapping.findForward("paginaMentira");
	       }
			//de lo contrario se adicionan los warnings si existen y se mostraran en la siguiente pagina
			if(validaciones.getWarningsFactura().size()>0)
			{
				forma.setWarningFactura(validaciones.getWarningFactura(0));
			}
			forma.setEstado(forma.getEstadoAux());
		}
		logger.info("llega!!!!!");
		forma.setEstado(forma.getEstadoAux());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaMentira");
	}
	


	/**
	 * 
	 * @param con
	 * @param dtoFacturaArray
	 * @return
	 */
	public ResultadoBoolean insertarFacturas( Connection con, FacturaForm forma)
	{
		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
		nf = new DecimalFormat("#.##");
		
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			if(forma.getFacturas().get(w).getDetallesFactura().size()>0)
			{
				double totalFactura=0;
				for(int i = 0; i < forma.getFacturas().get(w).getDetallesFactura().size(); i++)
				{
					logger.info("\n\n\nvalor de los asocios en la facturas >> "+forma.getFacturas().get(w).getDetallesFactura().get(i).getAsociosDetalleFactura().size());
					totalFactura=totalFactura+forma.getFacturas().get(w).getDetallesFactura().get(i).getValorTotal();
				}
				if(forma.getFacturas().get(w).getCentroAtencion().getCodigo()>0)
				{
					if(Double.parseDouble(nf.format(totalFactura)) == forma.getFacturas().get(w).getValorTotal())
					{
						if(forma.getFacturas().get(w).getHistoricoEncabezado().getCodigoPk()>0)
						{
							UtilidadTransaccion.getTransaccion().begin();
							
							IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
							
							historicoEncabezadoMundo.insertar(forma.getFacturas().get(w).getHistoricoEncabezado());
							
							UtilidadTransaccion.getTransaccion().commit();
						}
								
						ResultadoInteger resultadoInteger=null;
						int codigoFactura=ConstantesBD.codigoNuncaValido;
						boolean banderaInsertarFactura=false;
						
						if(forma.getFacturas().size()-1 == w){
							banderaInsertarFactura=true;
						}else{
							for(int j=w+1; j<forma.getFacturas().size(); j++){
								if(forma.getFacturas().get(w).getConsecutivoFactura()==forma.getFacturas().get(j).getConsecutivoFactura()){					
									return new ResultadoBoolean(false, "error.facturacion.noGuardoFactura.consecutivoRepetido");
								}else{
									banderaInsertarFactura=true;
								}
							}
						}
	
						if(banderaInsertarFactura)
							resultadoInteger=Factura.insertar(con,	forma.getFacturas().get(w));

						codigoFactura=resultadoInteger.getResultado();
						
						if(codigoFactura<=0)
							return new ResultadoBoolean(false, resultadoInteger.getDescripcion());
						else
						{
							forma.getFacturas().get(w).setCodigo(codigoFactura);
						}				
					}
					else
					{
						logger.error("ERROR, EL TOTAL DE LA FACTURA ES DIFERENTE A LA SUMA DEL TOTAL DE LOS CARGOS.");
						return new ResultadoBoolean(false, "");
					}
				}
				else
				{
					logger.error("ERROR, NO TIENE ASOCIADO EL CENTRO DE ATENCION.");
					return new ResultadoBoolean(false, "");
				}
			}
			else
			{
				logger.error("ERROR, LA FACTURA NO TIENE CARGOS ASOCIADOS");
				return new ResultadoBoolean(false, "");
			}
		}
		return new ResultadoBoolean(true, "");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public boolean insertarMovimientosAbonos( Connection con, FacturaForm forma, PersonaBasica paciente, UsuarioBasico usuario)
	{
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			if(AbonosYDescuentos.insertarMovimientoAbonos(con, paciente.getCodigoPersona(), forma.getFacturas().get(w).getCodigo(), ConstantesBD.tipoMovimientoAbonoFacturacion, forma.getFacturas().get(w).getValorAbonos(), usuario.getCodigoInstitucionInt(), paciente.getCodigoIngreso(), usuario.getCodigoCentroAtencion())<1)
			{
				logger.info("No inserto los movimientos abonos de la factura->"+forma.getFacturas().get(w).getCodigo());
				return false;
			}	
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public boolean insertarTopesPaciente( Connection con, FacturaForm forma, UsuarioBasico usuario, PersonaBasica paciente)
	{
		logger.info("\n\n\n\n\n*************************************************LLEGA A INSERTAR TOPES*************************************************************");
		for(int w=0; w<forma.getFacturas().size(); w++)
		{	
			DtoFactura dtoFactura=forma.getFacturas().get(w);
			/*DATOS COMUNES EN LA ACTUALIZACION DE LOS TOPES POR PACIENTE*/
			PagosPaciente mundoTopes= new PagosPaciente();
	        mundoTopes.setEntidad(usuario.getInstitucion());
	        mundoTopes.setInstitucion(usuario.getCodigoInstitucionInt());
	        mundoTopes.setTipoMonto(dtoFactura.getTipoMonto().getCodigo());
	        logger.info("*\n\n\n\n\nactualizarTopes==consecutivoFactura= "+dtoFactura.getConsecutivoFactura()+"\n\n\n");
	        mundoTopes.setDocumento(UtilidadTexto.formatearExponenciales(dtoFactura.getConsecutivoFactura()));
	        mundoTopes.setFecha(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
	        mundoTopes.setDescripcion(ConstantesBD.descripcionGenracionFacturaAutomatica);
	        mundoTopes.setOrigen(ConstantesBD.codigoOrigenPagoInterno);
	        mundoTopes.setUsuario(usuario.getLoginUsuario()); 
	        mundoTopes.setTipoRegimen(dtoFactura.getTipoRegimenAcronimo());
			/*FIN DATOS COMUNES*/
	        
	        //CASO URGENCIAS U HOSPITALIZACION --  el diagnóstico y el 
	        //valor se deben tomar de la factura
	    	if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion 
	    	    || dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias)
			{
	    		if(!UtilidadTexto.isEmpty(dtoFactura.getDiagnosticoEgresoAcronimoTipoCie()))
	    		{	
		    	    mundoTopes.setTipoCie(dtoFactura.getDiagnosticoTipoCie());
			        mundoTopes.setDiagnostico(dtoFactura.getDiagnosticoAcronimo());
	    		}
	    		else
	    		{
	    			mundoTopes.setTipoCie(0);
	            	mundoTopes.setDiagnostico(null);
	    		}
		        mundoTopes.setValor(dtoFactura.getValorBrutoPac() - dtoFactura.getValorDescuentoPaciente());
		        logger.info("----TOPES CUENTA DE URGENCIA U HOSPITAL----");
		        try 
		        {
					if(mundoTopes.insertarTopesPacienteTransaccional(con, paciente.getCodigoPersona(), ConstantesBD.continuarTransaccion)<=0)
					{
						logger.warn("error en insertarTopesPaciente inicioTransaccion");
						return false;
					}
				} 
		        catch (SQLException e) 
		        {
		        	logger.warn("error en insertarTopesPaciente");
					e.printStackTrace();
					return false;
				}    
		        logger.info("inserta bien!!!");
			}
	    	//FIN CASO URGENCIAS - HOSPITALIZACION
	        
	        //CASO DE CONSULTA EXTERNA O DE AMBULATORIOS-- el diagnóstico y el valor se toman del detalle de la factura 
	    	else if(forma.getFacturas().get(w).getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoConsultaExterna 
				|| forma.getFacturas().get(w).getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				double valorPaciente=forma.getFacturas().get(w).getValorBrutoPac()-forma.getFacturas().get(w).getValorDescuentoPaciente();
				for(int x=0; x<forma.getFacturas().get(w).getDetallesFactura().size(); x++)
				{
					DtoDetalleFactura dtoDetalle=forma.getFacturas().get(w).getDetallesFactura().get(x);
					if(!UtilidadTexto.isEmpty(dtoDetalle.getDiagnosticoAcronimoTipoCie()))
					{
						if(dtoDetalle.getDiagnosticoTipoCie()<0)
							mundoTopes.setTipoCie(0);
						else
							mundoTopes.setTipoCie(dtoDetalle.getDiagnosticoTipoCie());
						if(mundoTopes.getTipoCie()==0)
							mundoTopes.setDiagnostico(null);
						else
							mundoTopes.setDiagnostico(dtoDetalle.getDiagnosticoAcronimo());
		            }
		            else
		            {
		            	mundoTopes.setTipoCie(0);
		            	mundoTopes.setDiagnostico(null);
		            }
		            mundoTopes.setValor(dtoDetalle.getValorTotal()*valorPaciente/dtoFactura.getValorTotal());
		            logger.info("\nAQUI\n\nvalor servicio: "+dtoDetalle.getValorTotal()+" valorPaciente "+valorPaciente+" valorTotal "+dtoFactura.getValorTotal()+" = "+mundoTopes.getValor()+"\nAQUI\n\n");
			        logger.info("info a insertar=== tipoMonto = "+mundoTopes.getTipoMonto()+" documento= "+mundoTopes.getDocumento()+" fecha="+mundoTopes.getFecha()+" tipoCie="+mundoTopes.getTipoCie()+" diagnostico="+mundoTopes.getDiagnostico()+" descrp="+mundoTopes.getDescripcion()+" valor="+mundoTopes.getValor()+" origen= "+mundoTopes.getOrigen()+" user?="+mundoTopes.getUsuario());
				    try 
				    {
						if(mundoTopes.insertarTopesPacienteTransaccional(con, paciente.getCodigoPersona(), ConstantesBD.continuarTransaccion)<=0)
						{
							logger.warn("error en insertarTopesPaciente inicioTransaccion");
						    return false;
						}
					} 
				    catch (SQLException e) 
				    {
				    	logger.warn("error en insertarTopesPaciente");
						e.printStackTrace();
						return false;
					}    
				    logger.info("inserta bien!!!");
		        }
			} 
		}
		logger.info("*************************************************FINNNNNNN LLEGA A INSERTAR TOPES*************************************************************\n\n\n\n\n");
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ResultadoBoolean  validarAsignarConsecutivosFactura(Connection con, FacturaForm forma, UsuarioBasico usuario) 
	{

		//si se maneja el consecutivo desde la interfaz, debe hacer esto.
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazConsecutivoFacturasOtroSistema(usuario.getCodigoInstitucionInt())))
		{
			for(int w=0; w<forma.getFacturas().size(); w++)
			{
				ResultadoString resultadoString=UtilidadBDInterfaz.obtenerConsecutivoFacturas(usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), true);
				
				String consecutivo=resultadoString.getResultado();
				
				logger.info("------->"+consecutivo);
				if(!UtilidadTexto.isEmpty(consecutivo))
				{
					//generrar log de modificacion del consecutivo valor actual consecutivo valor anterior (Utilidades.convertirADouble(consecutivo)-1)
					forma.getFacturas().get(w).setConsecutivoFactura(Utilidades.convertirADouble(consecutivo));
				}
				else
				{
					logger.error("error actualizando el consecutivo de la interfaz");
					return  new ResultadoBoolean(false,"error.facturacion.consecutivofacturainterfaz");
				}
			}
			return new ResultadoBoolean(true,"");
		}
		
		
		ValidacionesFactura validaciones= new ValidacionesFactura();
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
			{
				if(validaciones.esConsecutivoFacturaDentroRangoInstitucion(con, usuario))
				{
					boolean consecutivoValido=false;
					double valorConsecutivo=Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
					consecutivoValido=esConsecutivoValido(valorConsecutivo,usuario.getCodigoInstitucionInt(), con);
					if(!consecutivoValido)
					{
						int contIntentos=0;
						valorConsecutivo=0;
						while(valorConsecutivo<=0&&contIntentos<100)
						{
							valorConsecutivo=Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
							contIntentos++;
							consecutivoValido=esConsecutivoValido(valorConsecutivo,usuario.getCodigoInstitucionInt(), con);
							if(!consecutivoValido)
								valorConsecutivo=0;
						}
					}
					if(valorConsecutivo>0)
					{
						forma.getFacturas().get(w).setConsecutivoFactura(valorConsecutivo);
					}
					else
					{
						logger.info("Error obteniendo el valor del conseuctivo w->"+valorConsecutivo);
						return new ResultadoBoolean(false,"error.facturacion.noGuardoFactura");
					}
				}
				else
				{
					logger.info("consecutivos de facturas ya completos!!!!! w->"+w);
					return new ResultadoBoolean(false,"error.facturacion.noGuardoFactura");
				}
			}
			else
			{
				//////FALTA HACER UN METODO PARA EL CONSECUTIVO DE MULTIEMPRESAS
				if(validaciones.esConsecutivoFacturaMultiempresaDentroRango(con, forma.getFacturas().get(w).getEmpresaInstitucion()))
				{
					forma.getFacturas().get(w).setConsecutivoFactura(validaciones.obtenerSiguientePosibleNumeroFacturaMultiempresa(con, forma.getFacturas().get(w).getEmpresaInstitucion()));
					if(!ValidacionesFactura.incrementarConsecutivoFacturaMultiempresa(con, forma.getFacturas().get(w).getEmpresaInstitucion(), 1))
					{
						logger.info("no actualizo el consecutico de multiempresa ");
						return new ResultadoBoolean(false,"error.facturacion.noGuardoFactura");
					}
				}
				else
				{
					logger.info("consecutivos de facturas ya completos!!!!! w->"+w);
					return new ResultadoBoolean(false,"error.facturacion.noGuardoFactura");
				}
			}
		}
		return new ResultadoBoolean(true,"");
	}

	/**
	 * Metodo para hacer las posibles validaciones que puede tener el sistema.
	 * @param con
	 * @param valorConsecutivo
	 * @param con Conexión con la BD
	 * @param codigoInstitucionInt
	 * @return
	 */
	private boolean esConsecutivoValido(double valorConsecutivo, int institucion, Connection con) 
	{
		//valida si el consecutivo ya se asigno a otra factura para evitar facturas repetidas.
		if(UtilidadesFacturacion.esConsecutvioAsignadoFactura(institucion, String.valueOf(new Double(valorConsecutivo).longValue())))
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturas, institucion, valorConsecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			return false;
		}
		return true;

	}

	/**
	 * metodo para asignar los valores de los abonos en la factura
	 * @param forma
	 * @param usuario 
	 * @param institucion 
	 */
	private void pasarValoresAbonosAObjetoFactura(FacturaForm forma, UsuarioBasico usuario, int institucion) 
	{
		logger.info("Abonos->"+forma.getAbonosYDescuentosMap()+"    size fact->"+forma.getFacturas().size());
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			//tiene que ser el mismo numero de los abonos
			if(forma.getAbonosYDescuentosMap("abonoAplicado_"+w).toString().isEmpty())
				forma.getFacturas().get(w).setValorAbonos(0);
			else			
				forma.getFacturas().get(w).setValorAbonos(Double.parseDouble(forma.getAbonosYDescuentosMap("abonoAplicado_"+w).toString()));
			forma.getFacturas().get(w).setValorNetoPaciente(Double.parseDouble(forma.getAbonosYDescuentosMap("netoPaciente_"+w).toString()));
			forma.getFacturas().get(w).setValorConvenio(Double.parseDouble(forma.getAbonosYDescuentosMap("vlrConvenio_"+w).toString()));
			forma.getFacturas().get(w).setValorCartera(Double.parseDouble(forma.getAbonosYDescuentosMap("vlrConvenio_"+w).toString()));
			forma.getFacturas().get(w).setValorBrutoPac(Double.parseDouble(forma.getAbonosYDescuentosMap("vlrPaciente_"+w).toString()));
			forma.getFacturas().get(w).setValorLiquidadoPaciente(Double.parseDouble(forma.getAbonosYDescuentosMap("vlrLiquidadoPaciente_"+w).toString()));
			if(!UtilidadTexto.isNumber(forma.getAbonosYDescuentosMap("dctoValor_"+w).toString()))
				forma.getFacturas().get(w).setValorDescuentoPaciente(0);
			else
				forma.getFacturas().get(w).setValorDescuentoPaciente(Double.parseDouble(forma.getAbonosYDescuentosMap("dctoValor_"+w).toString()));
			
			if(forma.getFacturas().get(w).getValorNetoPaciente()== 0)
	        {   
				forma.getFacturas().get(w).setEstadoPaciente(new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionPacienteSinValorPaciente));
	        }    
	        else if (forma.getFacturas().get(w).getValorNetoPaciente() > 0)
	        {
	        	forma.getFacturas().get(w).setEstadoPaciente(new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionPacientePorCobrar));
	        }    
	        else if (forma.getFacturas().get(w).getValorNetoPaciente() < 0)
	        {
	        	forma.getFacturas().get(w).setEstadoPaciente(new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionPacienteConDevolucion));
	        } 
			
			//la parte de multiempresa, ya se hace desde el proponer factura
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	private boolean actualizarEstadoCuentaYSubCuentas(Connection con, FacturaForm forma, PersonaBasica paciente) throws IPSException 
	{
		logger.info("TIPO FACTURACION->"+forma.getTipoFacturacion()+"<-");
		if(forma.getTipoFacturacion()==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			Cuenta mundoCuenta= new Cuenta();
			int actualizo=0;
			boolean existeConveniosCargosPendientes=false;
			int codigoEstadoCuenta= ConstantesBD.codigoEstadoCuentaFacturada;
			
			//verificamos para todos los responsables que todas las solicitudes esten facturadas, eso quiere decir q esten en cargos 'F' y que no existan solicitudes de cx sin liquidar  
			existeConveniosCargosPendientes=ValidacionesFactura.existenSolicitudesNoFacturadas(con, forma.getCuentas(), forma.getSubCuentasVector());
			
			if(existeConveniosCargosPendientes)
				codigoEstadoCuenta=ConstantesBD.codigoEstadoCuentaFacturadaParcial;
			
			for(int w=0; w<forma.getCuentas().size();w++)
			{
				try 
				{
					actualizo = mundoCuenta.cambiarEstadoCuentaTransaccional(con, codigoEstadoCuenta, Integer.parseInt(forma.getCuentas().get(w).toString()), ConstantesBD.continuarTransaccion);
					if(actualizo>0 && codigoEstadoCuenta==ConstantesBD.codigoEstadoCuentaFacturada)
					{
						logger.info("\n\n***SE VA HA ACTUALIZAR A FACTURADO LAS SUBCUENTAS RESPONSABLES**************\n\n");
						//11. SE ACTUALIZA 'FACTURADO' EL CENTINELA DE LAS SUBCUENTAS
						if(!this.actualizarEstadoFacturadoSubCuenta(con, forma, paciente))
						{
							logger.warn("error en actualizar subcuentas");
			                return false;
						}
					}
				} 
				catch (NumberFormatException e) 
				{
					e.printStackTrace();
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
	            if(actualizo<0)
	            {    
	                logger.warn("error en actualizaEstadoCuentasAsociadasTransaccional");
	                return false;
	            }   
	        }
		}
		else if(forma.getTipoFacturacion()==ConstantesBD.codigoTipoFacturacionPacienteContinuaProcesoAtencion)
		{
			Cuenta mundoCuenta= new Cuenta();
			int actualizo=0;
			int codigoEstadoCuenta= ConstantesBD.codigoEstadoCuentaFacturadaParcial;
			for(int w=0; w<forma.getCuentas().size(); w++)
			{
				logger.info("\n\n ACTUALIZACION CUENTA------>"+forma.getCuentas().get(w)+"\n\n");
				try 
				{
					actualizo = mundoCuenta.cambiarEstadoCuentaTransaccional(con, codigoEstadoCuenta, Integer.parseInt(forma.getCuentas().get(w).toString()), ConstantesBD.continuarTransaccion);
				} 
				catch (NumberFormatException e) 
				{
					e.printStackTrace();
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
	            if(actualizo<0)
	            {    
	                logger.warn("error en actualizaEstadoCuentasAsociadasTransaccional");
	                return false;
	            }   
	        }
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	private boolean actualizarEstadoIngreso(Connection con, FacturaForm forma, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		if(forma.getTipoFacturacion()==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			IngresoGeneral.modificarFechaHoraEgresoIngreso(con, paciente.getCodigoIngreso()+"");
			//Identificar el ingreso con cierre generado automaticamente
			HashMap datosIngreso = new HashMap();
			datosIngreso.put("estado", ConstantesIntegridadDominio.acronimoEstadoCerrado);
			datosIngreso.put("ingreso", paciente.getCodigoIngreso());
			datosIngreso.put("cierreManual", ConstantesBD.acronimoNo);
			datosIngreso.put("usuarioModifica", usuario.getLoginUsuario());
			return AperturaIngresos.ActualizarEstadoIngreso(con, datosIngreso);
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionResumen(Connection con, FacturaForm forma, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
	    //llenamos el mapa facturas a imprimir con el atributo seleccionado en no
		proponerMapaFacturasImprimir(forma);
		//Se pone en true este atributo para mostrar mensaje exitoso de generacion de recibo MT-1322
		forma.setProcesoExitoso(true);
		//Si salio todo bien anunciamos el cambio al observable
	    setObservable(paciente, request, true);
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("resumenFacturas");
	}
	
	
	/**
	 * Método para iniciar el proceso de faturación
	 * @param con
	 * @param idCuenta
	 * @return true si fue iniciado correctamente
	 */
	private boolean empezarProcesoFacturacion(Connection con, int idCuenta, String loginUsuario, PersonaBasica paciente, String idSesion)
	{
		if(!UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "" /*idSesion*/))
    	{
			Factura fact=new Factura();
	    	return fact.empezarProcesoFacturacion(con, idCuenta, loginUsuario, idSesion);
    	}
		else
			return false;
	}
	
	/**
	 * Método para finalizar el proceso de facturacion
	 * @param con
	 * @param idCuenta
	 * @param fact
	 * @return
	 */
	private boolean finalizarProcesoFacturacion(Connection con, int idCuenta, String estado, String idSesion)
	{
		Factura fact = new Factura();
		return fact.finalizarProcesoFacturacionTransaccional(con, idCuenta, estado, idSesion);
	}
	
	/**
	 * Método para cancelar el proceso de facturacion
	 * @param con
	 * @param idCuenta
	 * @param fact
	 * @return
	 */
	private boolean cancelarProcesoFacturacion(Connection con, int idCuenta, FacturaForm forma, HttpServletResponse response, String idSesion)
	{
		Factura fact= new Factura();
		boolean resultado=fact.cancelarProcesoFacturacion(con, idCuenta, idSesion);
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
	
	/**
	 * Método para hacer que el paciente
	 * pueda ser visto por todos los usuario en la aplicacion
	 * @param paciente
	 */
	private void setObservable(PersonaBasica paciente, HttpServletRequest request, boolean cargarPaciente)
	{
		if(cargarPaciente)
		{	
			/**para cargar el paciente que corresponda**/
			Connection con=UtilidadBD.abrirConexion();
			try {
				paciente.cargar(con,paciente.getCodigoPersona());
			} catch (SQLException e) {
				// @TODO Auto-generated catch block
				e.printStackTrace();
			}
			UtilidadBD.closeConnection(con);
		}	
		
		//Código necesario para registrar este paciente como Observer
		ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			paciente.setObservable(observable);
			// Si ya lo habíamos añadido, la siguiente línea no hace nada
			observable.addObserver(paciente);
		}
		//Se sube a sesión el paciente activo
		request.getSession().setAttribute("pacienteActivo", paciente);
		
	    if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}		
	
	  /**
     * Metodo para imprimir la prefacutra
     * @param con
     * @param mapping
     * @param formaFacturacion
     * @param request
     * @param pac
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirPrefactura(ActionMapping mapping, FacturaForm forma, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
    {
    	DtoFactura dtoFactura= (DtoFactura) forma.getFacturas().get(forma.getIndex());
    	Vector nombresArchivos=new Vector();
		nombresArchivos=FacturacionPdf.imprimirPrefactura(paciente,usuario,forma.getMapaPrefacturas(), dtoFactura, forma.getCuentas().get(0)+"");
        request.setAttribute("archivos", nombresArchivos);
		request.setAttribute("nombreVentana", "IMPRIMIR PREFACTURA");
		forma.setEstado("detallePrefactura");
		return mapping.findForward("abrirNPdf");
	}
    
    /**
     * 
     * @param forma
     */
    private void proponerMapaFacturasImprimir(FacturaForm forma) 
	{
		for(int w=0; w<forma.getFacturas().size(); w++)
		{
			if(w==0)
				forma.setFormatoConvenio(forma.getFacturas().get(w).getFormatoImpresion().getCodigo()+"");
			forma.setCodigosFacturasImprimir("codigoFactura_"+w, forma.getFacturas().get(w).getCodigo());
			forma.setCodigosFacturasImprimir("formatoImpresion_"+w, forma.getFacturas().get(w).getFormatoImpresion().getCodigo());
			forma.setCodigosFacturasImprimir("tipoAnexo_"+w, "");
			forma.setCodigosFacturasImprimir("seleccionadoImprimir_"+w, ConstantesBD.acronimoNo);
			forma.setCodigosFacturasImprimir("empresaInstitucion_"+w, forma.getFacturas().get(w).getEmpresaInstitucion()+"");
			//Validamos si el país es Venezuela y proponemos el valor por defecto para el radio button de tipo de impresion Venezuela
			if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
				forma.setCodigosFacturasImprimir("formatoImpresionVenezuela_"+w, "media");
		}
		forma.setCodigosFacturasImprimir("numRegistros", forma.getFacturas().size());
	}
    
    /**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param paciente
     * @param usuario
     * @return
     * @throws IPSException 
     */	
    private ActionForward accionImprimirFacturas(Connection con, ActionMapping mapping, FacturaForm forma,HttpServletRequest request,PersonaBasica paciente, UsuarioBasico usuario,HttpServletResponse response) throws IPSException 
	{
    	String path="";
        HashMap mapaImprimir=new HashMap();
      //MT6142 se instancia la clase y se crea variable para guardar codigo de factura
        ConsultaFacturas mundo = new ConsultaFacturas();
        String codigoFacturaM="";
        //
        
		int contadorSeleccionado=0;
		for(int w=0; w< Integer.parseInt(forma.getCodigosFacturasImprimir("numRegistros").toString()); w++)
		{
		 codigoFacturaM=forma.getCodigosFacturasImprimir("codigoFactura_"+w)+"";
			if(forma.getCodigosFacturasImprimir("seleccionadoImprimir_"+w).toString().equals(ConstantesBD.acronimoSi))
			{	
				mapaImprimir.put("codigoFactura_"+contadorSeleccionado,forma.getCodigosFacturasImprimir("codigoFactura_"+w));
				
				if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA")){
					mapaImprimir.put("formatoImpresion_"+contadorSeleccionado,ConstantesBD.codigoFormatoImpresionVenezuela+"");
					mapaImprimir.put("formatoImpresionVenezuela_"+contadorSeleccionado,forma.getCodigosFacturasImprimir("formatoImpresionVenezuela_"+contadorSeleccionado));
				}
				else
				{
					if(forma.getCodigosFacturasImprimir("formatoImpresion_"+w)!=null && !forma.getCodigosFacturasImprimir("formatoImpresion_"+w).toString().equals(""))
						mapaImprimir.put("formatoImpresion_"+contadorSeleccionado,forma.getCodigosFacturasImprimir("formatoImpresion_"+w));
					else
						mapaImprimir.put("formatoImpresion_"+contadorSeleccionado,"1");
				}
				
				mapaImprimir.put("empresaInstitucion_"+contadorSeleccionado, forma.getCodigosFacturasImprimir("empresaInstitucion_"+w));
				mapaImprimir.put("tipoimpresion_"+contadorSeleccionado, forma.getTipoImpresion());
				
				if(forma.isImprimirAnexo())
				{
					mapaImprimir.put("impAnexoArticulo_"+contadorSeleccionado,"true");
					mapaImprimir.put("tipoAnexo_"+contadorSeleccionado,forma.getTipoAnexo());
				}
				else	
				{
					mapaImprimir.put("impAnexoArticulo_"+contadorSeleccionado,"false");
				}
				if(!forma.getAnexoC().equals("") /* //Tarea 56120 && !forma.getCheckTodos().equals("1")*/)
		    	{
					if(forma.getAnexoC().equals("sol"))
						path=generarReporteAnexoSolicitud(con, forma, usuario, request, mapping, paciente, w);
		    		if(forma.getAnexoC().equals("cen"))
		    			path=generarReporteAnexoCC(con, forma, usuario, request, mapping, paciente, w);
		    		if(forma.getAnexoC().equals("ite"))
		    			path=generarReporteAnexoIT(con, forma, usuario, request, mapping, paciente, w);
		    	}
				if(!path.equals(""))
					mapaImprimir.put("tipoanexo_"+contadorSeleccionado, path);
				contadorSeleccionado++;
			}
		}
		
		
	//MT6142
	// traer prefijo de factura
		try {
			forma.setMapaDetalleFactura(mundo.cargarDetalleFactura(con,Utilidades.convertirADouble(codigoFacturaM)));
		} catch (SQLException e) {
			Log4JManager.error(e.getMessage(), e);
		}
		
  	mapaImprimir.put("prefijoFactura_0",forma.getMapaDetalleFactura("prefijoFactura_0") == null ? "" : forma.getMapaDetalleFactura("prefijoFactura_0"));
	 // Traer Número de autorización
  	forma.setNumeroAutorizacion(ConsultaFacturas.consultarNumeroAutorizacion(Utilidades.convertirAEntero(forma.getMapaDetalleFactura("cuenta_0")+""),Utilidades.convertirAEntero(forma.getMapaDetalleFactura("subcuenta_0")+""))+"");
	mapaImprimir.put("nroAutorizacion_0", forma.getNumeroAutorizacion());		
   //MT fin		
		
		//Tarea 56120
		/*path="";
		if(forma.getCheckTodos().equals("1") && !forma.getAnexoC().equals(""))
		{
			if(forma.getAnexoC().equals("sol"))
				path=generarReporteAnexoSolicitud(con, forma, usuario, request, mapping, paciente, -1);
    		if(forma.getAnexoC().equals("cen"))
    			path=generarReporteAnexoCC(con, forma, usuario, request, mapping, paciente, -1);
    		if(forma.getAnexoC().equals("ite"))
    			path=generarReporteAnexoIT(con, forma, usuario, request, mapping, paciente, -1);
    		if(!path.equals(""))
    		{
    			mapaImprimir.put("tipoanexo_"+contadorSeleccionado, path);
				//contadorSeleccionado++;
    		}
		}*/
		logger.info("\n\n\n\n\ntipo anexo-->"+mapaImprimir+"\n\n\n\n\n");
		mapaImprimir.put("numRegistros", contadorSeleccionado);
		forma.setEstado("resumen");
		
		HashMap archivosGeneradosBirt=new HashMap(0);
		archivosGeneradosBirt.put("numRegistros", "0");
		request.setAttribute("archivosBirt",archivosGeneradosBirt);
		request.setAttribute("archivos", new Vector());
		
		ActionForward forward= FacturacionPdf.imprimirFacturas(paciente,usuario,mapaImprimir,con,request,"IMPRIMIR FACTURAS",mapping,"resumenFacturas",response);
		forma.setNombreArchivoGenerado(FacturacionPdf.nombreReporteAgrupado);
		
		request.getSession().setAttribute("piePaginaImpresion","copia" );
		
		Connection connection=UtilidadBD.abrirConexion();;
			forward= FacturacionPdf.imprimirFacturas(paciente,usuario,mapaImprimir,connection,request,"IMPRIMIR FACTURAS COPIA",mapping,"resumenFacturas",response);
			forma.setNombreArchivoGeneradoCopia(FacturacionPdf.nombreReporteAgrupado);
		UtilidadBD.closeConnection(connection);
		
		return forward;
 	}
   
}
