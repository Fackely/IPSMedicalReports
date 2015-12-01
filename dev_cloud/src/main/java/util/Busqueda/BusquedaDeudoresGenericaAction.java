/*
 * JUnio 25, 2009
 */
package util.Busqueda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaDeudoresGenerica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez Rivillas
 * Action, controla todas las opciones dentro de la busqueda de deudores genérica 
 * incluyendo los posibles casos de error y los casos de flujo.
 *
 */
public class BusquedaDeudoresGenericaAction extends Action 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(BusquedaDeudoresGenericaAction.class);
    
    
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
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof BusquedaDeudoresGenericaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				BusquedaDeudoresGenericaForm busquedaForm =(BusquedaDeudoresGenericaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=busquedaForm.getEstado(); 
				logger.warn("estado BusquedaDeudoresGenericaAction-->"+estado);

				if( usuario==null || paciente==null)
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("cerrarDiagnostico");
				}

				if(estado == null)
				{
					busquedaForm.reset(request);	
					logger.warn("Estado no valido dentro del flujo de Busqueda Deudores Genérica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,busquedaForm,usuario,mapping,request);
				}
				else if (estado.equals("buscar"))
				{
					return accionBuscar(con,busquedaForm,mapping);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,busquedaForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,busquedaForm,response,mapping);
				}
				else
				{
					busquedaForm.reset(request);
					UtilidadBD.closeConnection(con);
					logger.warn("Estado no valido dentro del flujo de Busqueda Diagnosticos Generica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					return mapping.findForward("paginaError");
				}
			}			
			return null;	
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método implementado para realizar la redireccion del listado de deudores
	 * @param con
	 * @param busquedaForm
	 * @param response
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con,
			BusquedaDeudoresGenericaForm busquedaForm,
			HttpServletResponse response, ActionMapping mapping) 
	{
		
		UtilidadBD.closeConnection(con);
		try 
		{
			response.sendRedirect(busquedaForm.getLinkSiguiente());
			return null;
		} 
		catch (IOException e) 
		{
			logger.error("Problemas realizando la redireccion : "+e);
			return mapping.findForward("principal");
		}
		
	}

	/**
	 * Método para realizar la ordenacion del listado de deudores encontrados
	 * @param con
	 * @param busquedaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con,BusquedaDeudoresGenericaForm busquedaForm, ActionMapping mapping) 
	{
		int numRegistros = busquedaForm.getNumRegistros();
		busquedaForm.setListado(Listado.ordenarMapa(BusquedaDeudoresGenerica.indicesListado, busquedaForm.getIndice(), busquedaForm.getUltimoIndice(), busquedaForm.getListado(), numRegistros));
		busquedaForm.setNumRegistros(numRegistros);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para realizar la búsqueda de los deudores según la captura de los parámetros
	 * @param con
	 * @param busquedaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscar(Connection con,
			BusquedaDeudoresGenericaForm busquedaForm, ActionMapping mapping) 
	{
		busquedaForm.setListado(BusquedaDeudoresGenerica.busqueda(
																	con, 
																	busquedaForm.getTipoDeudor(), 
																	busquedaForm.getCodigoTipoIdentificacion(), 
																	busquedaForm.getNumeroIdentificacion(), 
																	busquedaForm.getPrimerApellido(),
																	busquedaForm.getPrimerNombre(), 
																	busquedaForm.getDescripcion(),
																	busquedaForm.isNuevoDeudor(),
																	busquedaForm.isDeudoresActivos()));
		
		//Se valida si el usuario es de tipo paciente y no existe como deudor
		int numRegistros = busquedaForm.getNumRegistros();
		if(numRegistros == 0 && busquedaForm.getTipoDeudor().equalsIgnoreCase("PACI")){
			//Se busca pero esta vez solomente paciente
			busquedaForm.setListado(BusquedaDeudoresGenerica.busqueda(  con, 
					busquedaForm.getTipoDeudor(), 
					busquedaForm.getCodigoTipoIdentificacion(), 
					busquedaForm.getNumeroIdentificacion(), 
					busquedaForm.getPrimerApellido(),
					busquedaForm.getPrimerNombre(), 
					busquedaForm.getDescripcion(),
					true,
					false));
			
			numRegistros = busquedaForm.getNumRegistros();
			//en este caso si numRegistros = 1 es porque existe el paciente pero no como deudor.
			if(numRegistros > 0){
				busquedaForm.setPacienteNoDeudor(true);
			}else{
				busquedaForm.setPacienteNoDeudor(false);
			}
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal"); 
	}

	/**
	 * Método para iniciar el flujo de la busqueda genérica de deudores
	 * @param con
	 * @param busquedaForm
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,
			BusquedaDeudoresGenericaForm busquedaForm, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{
		busquedaForm.reset(request);
		
		
		busquedaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		//***********Se carga el arreglo de los tipos de identificacion***********************
		busquedaForm.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente", usuario.getCodigoInstitucionInt()));
		//*************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
