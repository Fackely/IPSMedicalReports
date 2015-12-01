/*
 * @(#)CuentasProcesoFacturacionAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.CuentasProcesoFacturacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CuentasProcesoFacturacion;
import com.princetonsa.mundo.facturacion.Factura;

/**
 *   Action, controla todas las opciones dentro de la restauracion
 *   cuentas proceso facturacion, incluyendo los posibles casos de error. 
 * 	 Y los casos de flujo.
 * @version 1.0, Julio 6, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CuentasProcesoFacturacionAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CuentasProcesoFacturacionAction.class);
		
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
			if(form instanceof CuentasProcesoFacturacionForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				CuentasProcesoFacturacionForm forma =(CuentasProcesoFacturacionForm)form;

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=forma.getEstado(); 
				logger.warn("Estado Cuentas Proceso Facturacion-->"+estado);

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Restablecer Cuentas Proceso Facturacion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezarXTodos"))
				{
					ActionForward validacionesGenerales = this.validacionesParametrosGenerales(usuario, mapping, request);
					if (validacionesGenerales != null)
					{
						UtilidadBD.cerrarConexion(con);
						return validacionesGenerales ;
					}
					return this.accionEmpezarXTodos(forma,mapping, con, usuario);
				}
				else if (estado.equals("empezarXPaciente"))
				{
					ActionForward validacionesGenerales = this.validacionesAcceso(paciente, mapping, request);
					if (validacionesGenerales != null)
					{
						UtilidadBD.cerrarConexion(con);
						return validacionesGenerales ;
					}
					return this.accionEmpezarXPaciente(forma,mapping, con, usuario, paciente);
				}
				else if(estado.equals("restaurarCuenta"))
				{
					return this.accionRestaurarCuenta(forma, mapping, con, usuario, paciente, request);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de restablecer Cuentas Proceso Facturacion");
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
	 * Este método especifica las acciones a realizar en el estado
	 * empezarXTodos.
	 * 
	 * @param forma CuentasProcesoFacturacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal ".jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarXTodos(	CuentasProcesoFacturacionForm forma, 
												ActionMapping mapping, 
												Connection con,
												UsuarioBasico usuario) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		CuentasProcesoFacturacion mundo = new CuentasProcesoFacturacion();
		forma.setMapa(mundo.listadoXTodos(con,usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt()));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principalXTodos");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarXTodos.
	 * 
	 * @param forma CuentasProcesoFacturacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal ".jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarXPaciente(	CuentasProcesoFacturacionForm forma, 
													ActionMapping mapping, 
													Connection con,
													UsuarioBasico usuario,
													PersonaBasica paciente) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		CuentasProcesoFacturacion mundo = new CuentasProcesoFacturacion();
		forma.setMapa(mundo.listadoXPaciente(con,usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona()+""));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principalXPaciente");		
	}

	/**
	 * estado restaurar cuenta
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionRestaurarCuenta(	CuentasProcesoFacturacionForm forma, 
													ActionMapping mapping, 
													Connection con,
													UsuarioBasico usuario,
													PersonaBasica paciente,
													HttpServletRequest request
												) throws SQLException
	{
		boolean esFlujoXpaciente=false;
		if(forma.getMapa().containsKey("esXPaciente"))
			esFlujoXpaciente=true;
		Cuenta objectCuentaActual= new Cuenta();
		objectCuentaActual.cargarCuenta(con, forma.getCodigoCuenta());
		if(!objectCuentaActual.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaProcesoFacturacion+""))
		{
			UtilidadBD.cerrarConexion(con);
			logger.warn(" cuenta modificada por otro usuario "+forma.getCodigoCuenta());
			ArrayList atributosError = new ArrayList();
			atributosError.add("Cuenta");
			request.setAttribute("codigoDescripcionError", "errors.excepcionSQL.registroYaActualizado");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		// de lo contrario puedo actualizar
		Factura fact= new Factura();
		fact.cancelarProcesoFacturacion(con, Integer.parseInt(forma.getCodigoCuenta()), forma.getIdSesion());
		String mensaje="SE RESTAURO SATISFACTORIAMENTE LA CUENTA NUMERO "+forma.getCodigoCuenta();
		this.generarLog(usuario, forma);
		
		// flujo pacientes
		forma.reset();
		CuentasProcesoFacturacion mundo = new CuentasProcesoFacturacion();
		
		if(esFlujoXpaciente)
			forma.setMapa(mundo.listadoXPaciente(con,usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona()+""));
		else
			forma.setMapa(mundo.listadoXTodos(con,usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt()));
		
		forma.setMapa("mensaje", mensaje);
		UtilidadBD.cerrarConexion(con);
		
		if(esFlujoXpaciente)
			return mapping.findForward("principalXPaciente");
		else 
			return mapping.findForward("principalXTodos");
	}
	
	
	/**
	 * acceso
	 * @param paciente
	 * @param map
	 * @param req
	 * @return
	 */
	protected ActionForward validacionesAcceso(PersonaBasica paciente, ActionMapping map, HttpServletRequest req)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return map.findForward("paginaError");
		}
		return null;
	}
	
	/**
	 * acceso
	 * @param usuario
	 * @param map
	 * @param req
	 * @return
	 */
	protected ActionForward validacionesParametrosGenerales(UsuarioBasico usuario, ActionMapping map, HttpServletRequest req)
	{
		String minutosEspera = ValoresPorDefecto.getModificarMinutosEsperaCuentasProcFact(usuario.getCodigoInstitucionInt());
		if(minutosEspera.equals(""))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Sin minutos de espera para restaurar cuentas en proc de facturacion en parámetros generales",new ActionMessage("errors.notEspecific", /*la coloque aca porque no podia pasar el message actualizacion suba urg*/"Falta definir minutos de espera para restaurar cuentas en proceso facturacion. Revisar Parámetros Generales "));
			saveErrors(req, errores);
	        return map.findForward("paginaErroresActionErrors");
		}
		return null;
	}
	
	
	/**
	 * Método que genera los Logs de restauracion
	 * @param forma
	 * @param usuario, user
	 */
	private void generarLog( UsuarioBasico usuario, CuentasProcesoFacturacionForm forma	)
	{
	    String log;
	        		    
		log="\n           ======INFORMACION RESTAURACION CUENTAS PROCESO FACTURACION===== " +
		"\n*  Usuario                    [" +usuario.getNombreUsuario()+"] " +
		"\n*  Fecha/Hora Sistema         ["+UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()+"] "+
		"\n*  Numero Cuenta              ["+forma.getCodigoCuenta()+"] " +
		"\n*  Estado Actualizado         ["+forma.getNombreEstadoCuenta()+"]";
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logCuentasProcesoFacturacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud()); 
	    
	}
	
	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, CuentasProcesoFacturacionForm forma, ActionMapping mapping) 
    {
		String[] indices = {
				            "numero_cuenta_", 
				            "via_ingreso_", 
				            "estado_anterior_", 
							"usuario_",
				            "fecha_",
				            "hora_",
							"tipo_no_id_paciente_",
							"paciente_",
							"idsesion_"
		  };
        
		int tmp = Integer.parseInt(forma.getMapa("numRegistros")+"");
		boolean esFlujoXpaciente=false;
		if(forma.getMapa().containsKey("esXPaciente"))
			esFlujoXpaciente=true;
		forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),Integer.parseInt(forma.getMapa("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapa("numRegistros",tmp+"");
        if(esFlujoXpaciente)
        	forma.setMapa("esXPaciente", true);
        if(forma.getMapa().containsKey("esXPaciente"))
		{
			UtilidadBD.closeConnection(con);
	        return mapping.findForward("principalXPaciente");
		}
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principalXTodos");
		}
    }
	
}
