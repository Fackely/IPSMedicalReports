package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.sql.SQLException;

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

import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.facturasVarias.DeudoresForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.Deudores;

/**
 * 
 * @author Juan Sebastian Castaño
 * Clase action de la funcionalidad deudores
 */

public class DeudoresAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DeudoresAction.class);
	
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

			if(form instanceof DeudoresForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();				
				}
				catch(SQLException e)
				{
					logger.warn("\n\n\n\n\n\n  No se pudo abrir la conexión en DeudoresAction "+e.toString());
				}

				//OBJETOS A USAR
				DeudoresForm deudoresForm = (DeudoresForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				// obtener el estado de accion
				String estado= deudoresForm.getEstado(); 
				logger.info("\n ***********************************************");
				logger.info("\n  el estado es ->"+estado);
				logger.info("\n ***********************************************");
				if(estado == null)
				{
					deudoresForm.reset();
					logger.warn("Estado no valido dentro del flujo de DEUDORES (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	

				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,deudoresForm,usuario, mapping);
				}

				// cargar tercero y formulario de ingreso como deudor
				else if (estado.equals("consultarDeudor"))
				{
					return accionConsultarDeudor(con,deudoresForm,usuario, mapping);
				}

				else if (estado.equals("guardar"))
				{
					return accionGuardarDeudor(con,deudoresForm,usuario, mapping, request);
				}

				else if (estado.equals("consultarDeudorSeleccionado"))
				{
					return accionConsultarDeudorSeleccionado(con,deudoresForm, mapping);
				}
				else if (estado.equals("modificarDeudor"))
				{
					return accionModificarDeudor(con,deudoresForm,usuario, mapping, request);
				}
				else
				{
					deudoresForm.reset();
					logger.warn("Estado no valido dentro del flujo de DEUDORES (null) ");
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
	 * Método para cargar el deudor seleccionado
	 * @param con
	 * @param deudoresForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarDeudorSeleccionado(Connection con, DeudoresForm deudoresForm, ActionMapping mapping) 
	{
		deudoresForm.setDeudor(Deudores.cargar(con, deudoresForm.getCodigoDeudorModificar()));
		deudoresForm.setDeudorAnterior(Deudores.cargar(con, deudoresForm.getCodigoDeudorModificar()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Metodo de modificacion de los datos de un deudor
	 * @param con
	 * @param deudoresForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionModificarDeudor(Connection con, DeudoresForm deudoresForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		ResultadoBoolean resultado = Deudores.modificar(con, deudoresForm.getDeudor());
		
		
		if(!resultado.isTrue())
		{
			errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		}
		
		if(errores.isEmpty())
		{	
			Deudores.generarLogArchivoDeudores(deudoresForm.getDeudorAnterior(), deudoresForm.getDeudor(), usuario);
			
		}
		else
		{
			saveErrors(request, errores);
			deudoresForm.setEstado("consultarDeudorSeleccionado");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}





	/**
	 * Metodo de registro de un nuevo deudor
	 * @param con
	 * @param deudoresForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDeudor(Connection con, DeudoresForm deudoresForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		
		//Se asignan los datos que faltan para registrar el deudor
		deudoresForm.getDeudor().setUsuarioModifica(usuario);
		deudoresForm.getDeudor().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		
		
		
		
		ResultadoBoolean resultado = Deudores.ingresar(con, deudoresForm.getDeudor());
		if(!resultado.isTrue())
		{
			errores.add("",new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
		}

		if( errores.isEmpty())
		{
			deudoresForm.setEstado("consultarDeudorModificar");
			
			deudoresForm.setDeudor(Deudores.cargar(con, deudoresForm.getDeudor().getCodigo()));
			
		}
		else
		{
			saveErrors(request, errores);
			deudoresForm.setEstado("");
		}
		
					
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	

	
	

	/**
	 * 
	 * @param con
	 * @param deudoresForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarDeudor(Connection con, DeudoresForm deudoresForm, UsuarioBasico usuario, ActionMapping mapping) 
	{		
		//Se carga la informacion del nuevo deudor
		deudoresForm.setDeudor(Deudores.cargarInformacionNuevoDeudor(con, deudoresForm.getCodigoTerceroSeleccionado()+"", deudoresForm.getTipoDeudorSeleccionado()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * estado inicial de la funcionalidad
	 * @param con
	 * @param deudoresForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, DeudoresForm deudoresForm, UsuarioBasico usuario, ActionMapping mapping) {
		Deudores mundoDeudores = new Deudores();
		
		// para salvar el estado antes de resetear el form
		String estado = deudoresForm.getEstado();
		// reiniciar el form
		deudoresForm.reset();		
		
		// cargar solo el estado en el  uevo form
		deudoresForm.setEstado(estado);
		
		// cargar la institucion
		mundoDeudores.setInstitucion(usuario.getCodigoInstitucionInt());
		// realizar la consulta de terceros
		//deudoresForm.setMapaTerceros(mundoDeudores.cargarTerceros(con));		
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}

}
