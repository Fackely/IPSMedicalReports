/*
 * @(#)AsocioCuentaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.EgresoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;

/**
 * Clase que define el control de la funcionalidad de
 * asocio de cuenta
 *	@version 1.0, Aug 14, 2003
 */
public class AsocioCuentaAction extends Action
{

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	private Logger logger = Logger.getLogger(AsocioCuentaAction.class);
	
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

		try {
		//Como no se necesitan datos propios vamos a usar una forma
		//previamente existente (la de egreso) - Solo se necesita el
		//estado
		if (form instanceof EgresoForm)
		{

			if (response==null); //Para evitar que salga el warning
			if( logger.isDebugEnabled() )
			{
				logger.debug("Entro al Action de Asocio de cuenta");
			}
			EgresoForm egresoForm=(EgresoForm)form;
			String estado=egresoForm.getEstado();

			try
			{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//No se cierra conexión porque si llega aca ocurrió un
				//error al abrirla
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			//Lo primero que vamos a hacer es validar que se
			//cumplan las condiciones.
			
			HttpSession session=request.getSession();		
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


			/**
			 * Validar concurrencia
			 * Si ya está en proceso de facturación, no debe dejar entrar
			 **/
			if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
			}
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de distribucion, no debe dejar entrar
			 **/
			else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
			}
			
			logger.info("Valor del Estado >> "+estado);
			
			//Primera Condición: El usuario debe existir
			//la validación de si es médico o no solo se hace en insertar
			if( usuario == null )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("No existe el usuario");
				}
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				return mapping.findForward("paginaError");				
			}
			else
			if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
			{
				//Segunda Condición: Debe haber un paciente cargado
				if( logger.isDebugEnabled() )
				{
					logger.debug("paciente null o sin  id");
				}
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				return mapping.findForward("paginaError");
			}
			else
			if (UtilidadValidacion.igualEstadoCuenta(con, paciente.getCodigoCuenta(), ConstantesBD.codigoEstadoCuentaAsociada))
			{				
				//Reviso si es caso des-asocio
				//el método se encarga de toda la funcionalidad, si encuentra
				//que lo debe manejar devuelve un ActionForward tomando el
				//control, si no envia un nulo y continuamos la ejecución trad.
				ActionForward posibleDesvio=this.manejoPosibleDesAsocio(mapping, request, paciente, usuario, con, estado);
				if (posibleDesvio!=null)
				{
					return posibleDesvio;
				}
				//Se verifica si el paciente tiene ingreso abierto en otro centro de atencion
				String centroAtencion = Utilidades.getCentroAtencionIngresoAbiertoPaciente(con,paciente.getCodigoPersona()+"",usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
				if(!centroAtencion.equals(""))
				{
					UtilidadBD.cerrarConexion(con);
					ActionErrors errores = new ActionErrors();
					errores.add("Paciente con otro ingreso en otro CA",new ActionMessage("errores.paciente.ingresoAbiertoCentroAtencion",centroAtencion));
					saveErrors(request,errores);
					return mapping.findForward("paginaErroresActionErrors");
				}
				
				//El paciente no tiene cuenta, saco error
				if( logger.isDebugEnabled() )
				{
					logger.debug("paciente sin cuenta");
				}
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				request.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoAbierta");
				return mapping.findForward("paginaError");
			}
			else
			if (estado==null||estado.equals(""))
			{
				egresoForm.reset();
				if( logger.isDebugEnabled() )
				{
					logger.debug("La accion específicada no esta definida ");
				}
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else
			if (estado.equals("empezar"))
			{					
				//El último paso de las validaciones
				CuentasPaciente cuentas = new CuentasPaciente(); 
					
				if(paciente.getCodigoCuenta()>0)	
					cuentas = (CuentasPaciente)paciente.getCuentasPacienteArray(paciente.getPosCuentaPaciente(paciente.getCodigoCuenta()));
				
				ResultadoBoolean resp=UtilidadValidacion.validacionAsocioCuenta(
						con, 
						Integer.parseInt(cuentas.getCodigoCuenta()),
						cuentas.getCodigoViaIngreso(), 
						cuentas.getCodigoTipoPaciente()
						);
				
				if (!resp.isTrue())
				{
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
			
					request.setAttribute("codigoDescripcionError", resp.getDescripcion());
					return mapping.findForward("paginaError");
				}
				
				//Valida que el ingreso del paciente no se hubiera realizado por Entidades SubContratadas				
				if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
				{					
					
					request.setAttribute("descripcionError","Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""));
					
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
					
					return mapping.findForward("paginaError");										
				}
					
				
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				return mapping.findForward("confirmarAsocioCuenta");
			}
			else
			if (estado.equals("salir"))
			{
				//Aca se debe hacer el proceso de cambiar el estado de la cuenta
				Cuenta cuenta=new Cuenta();
				cuenta.init(System.getProperty("TIPOBD"));
				try
				{
					if (cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), usuario.getLoginUsuario()) <=0)
					{
						if (con!=null&&!con.isClosed())
						{
		                    UtilidadBD.closeConnection(con);
						}
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
				}
				catch (Exception e)
				{
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				
				// Código necesario para notificar a todos los observadores que la cuentadel paciente en sesión pudo haber cambiado
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());

				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				return mapping.findForward("resultadoAsocioCuenta");
			}
			else
			{
				//Si entra acá es porque el estado fué invalido
				if (con!=null&&!con.isClosed())
				{
                    UtilidadBD.closeConnection(con);
				}
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");				
			}
		}
		else
		{
			//Todavía no existe conexión, por eso no se cierra
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	private ActionForward manejoPosibleDesAsocio (ActionMapping mapping, HttpServletRequest request,PersonaBasica paciente, UsuarioBasico usuario, Connection con, String estado) throws Exception
	{ 
		
		logger.info("EXISTE ASOCIO? "+paciente.getExisteAsocio());
		logger.info("CODIGO CUENTA ASOCIO: "+paciente.getCodigoCuentaAsocio());
		//Reviso si es caso des-asocio
		//y que no haya creado todavía cuenta
		if (paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio()==0)
		{
			if (estado!=null)
			{
				if (estado.equals("empezar"))
				{
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
					return mapping.findForward("confirmarDesAsocioCuenta");
				}
				else if (estado.equals("salirDesAsocio"))
				{

					//Aca se debe hacer el proceso de cambiar el estado de la cuenta
					Cuenta cuenta=new Cuenta();
					cuenta.init(System.getProperty("TIPOBD"));
					try
					{
						CuentasPaciente cuentas = (CuentasPaciente)paciente.getCuentasPacienteArray(paciente.getPosCuentaPaciente(paciente.getCodigoCuenta()));
						
						UtilidadBD.iniciarTransaccion(con);
						if (cuenta.desAsociarCuenta(con, usuario.getLoginUsuario(),paciente.getCodigoIngreso()+"",cuentas.getCodigoViaIngreso()) <=0)
						{
							UtilidadBD.abortarTransaccion(con);
							if (con!=null&&!con.isClosed())
							{
			                    UtilidadBD.closeConnection(con);
							}
							request.setAttribute("codigoDescripcionError", "errors.problemasBd");
							return mapping.findForward("paginaError");
						}
						else
							UtilidadBD.finalizarTransaccion(con);
					}
					catch (Exception e)
					{
						if (con!=null&&!con.isClosed())
						{
		                    UtilidadBD.closeConnection(con);
						}
						request.setAttribute("codigoDescripcionError", "errors.problemasBd");
						return mapping.findForward("paginaError");
					}
				
					// Código necesario para notificar a todos los observadores que la cuentadel paciente en sesión pudo haber cambiado
					UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());

					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
					return mapping.findForward("resultadoDesAsocioCuenta");

				}
			}
		}

		return null;
	}
}
