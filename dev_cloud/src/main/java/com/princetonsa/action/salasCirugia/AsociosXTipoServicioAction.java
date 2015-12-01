package com.princetonsa.action.salasCirugia;

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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.salasCirugia.AsociosXTipoServicioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsociosXTipoServicio;



/**
 * 
 * @author Juan Sebastian Castaño
 * Clase de manejo de las acciones realizadas por la
 * funcionalidad Asocios por tipo de servicio
 */
public class AsociosXTipoServicioAction extends Action {
	
	/**
	 * Log de la clase
	 */

	private Logger logger = Logger.getLogger(AsociosXTipoServicioAction.class);
	
	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
	{
		Connection con = null;
		try{
		/**
		 * Verificar si el form es una correcta instancia del archivo form asociado
		 */
		if (form instanceof AsociosXTipoServicioForm) {
		    //SE ABRE CONEXION
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
			
			AsociosXTipoServicioForm asociosServicioForm = (AsociosXTipoServicioForm)form;
			HttpSession session = request.getSession();
			UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			String estado = asociosServicioForm.getEstado();
			
			logger.info("\n *********************************************");
			logger.info("\n el estado es --> "+estado);
			logger.info("\n *********************************************");
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de AsociosXTipoServicio (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");				
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				return this.accionEmpezar(mapping, asociosServicioForm,con,usuario);
			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenar (con, asociosServicioForm , mapping);
			}
			else if (estado.equals("nuevo"))
			{
				return accionNuevo(con,asociosServicioForm,usuario,request,response, mapping);
			}
			else if (estado.equals("guardar"))
			{
				return accionGuardar(con, asociosServicioForm ,usuario,request,mapping);
			}
			
			else if (estado.equals("eliminar"))
			{
				return accionEliminar (con, asociosServicioForm, mapping, usuario, request);
			}
			
			else if (estado.equals("modificar"))
			{
				return accionModificar (con, asociosServicioForm, mapping, usuario, request);
			}
			else if(estado.equals("guardarModificado"))
			{
				return accionGuardarModificado(con, asociosServicioForm, mapping, usuario, request);
			}
			else
			{
				asociosServicioForm.clean();
				logger.warn("Estado no valido dentro del flujo de servicios asocios (null) ");
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
	 * Metodo que registra los cambios realizados sobre un registro de Asosios por tipo de servicio
	 * @param con
	 * @param asociosServicioForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificado(Connection con, AsociosXTipoServicioForm asociosServicioForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{	
		ActionErrors errores = new ActionErrors();
		
		AsociosXTipoServicio mundoAsoServ = new AsociosXTipoServicio();		
		// cargar la posicion del registro que desea modificar
		//mundoAsoServ.setCodigoModificarPos(Integer.parseInt(asociosServicioForm.getCodigoModificarPos()));
		
		int pos = Integer.parseInt(asociosServicioForm.getCodigoModificarPos());
		/*
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n valor de pos " + asociosServicioForm.getCodigoModificarPos() + " \n\n\n\n\n\n\n\n\n\n");
		
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo a modificar " + asociosServicioForm.getMapaServAsocios("codigo_"+pos) + " \n\n\n\n\n\n\n\n\n\n");

		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo a servicio " + asociosServicioForm.getMapaServAsocios("codigoTipoServicio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
		
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo a asocio " + asociosServicioForm.getMapaServAsocios("codigoTipoAsocio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
		
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo servicio " + asociosServicioForm.getMapaServAsocios("servicio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
		
		*/
		this.llenarMundo(asociosServicioForm, mundoAsoServ, pos, usuario);
		
		
		if (!mundoAsoServ.modificarServAsocio(con))
			errores.add("",new ActionMessage("errors.sinActualizar"));
		
			
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			asociosServicioForm.setEstado("");
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principalCargarGuardado");		
	}

	
	/**
	 * Metodo que carga la posicion del registro a modificar, para cargarlo en pantalla y permitir que el usuario realice los cambios.
	 * @param con
	 * @param asociosServicioForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificar(Connection con, AsociosXTipoServicioForm asociosServicioForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		AsociosXTipoServicio mundoAsoServ = new AsociosXTipoServicio();		
		// cargar la posicion del registro que desea modificar
		
		if (!asociosServicioForm.getCodigoModificarPos().equals(""))
			mundoAsoServ.setCodigoModificarPos(Integer.parseInt(asociosServicioForm.getCodigoModificarPos()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Metodo de eliminacion de un registro de asocio por tipo de servicio
	 * @param con
	 * @param asociosServicioForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, AsociosXTipoServicioForm asociosServicioForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		// Cargar posicion en el mapa del registro a eliminar
		int pos = Integer.parseInt(asociosServicioForm.getCodigoEliminarPos());		
		// En la posicion del mapa etiquetar el registro como eliminado.
		asociosServicioForm.setMapaServAsocios("eliminar_"+pos,ConstantesBD.acronimoSi);
		AsociosXTipoServicio mundoAsoServ = new AsociosXTipoServicio();		
		
		// cargar valores de datos a eliminar
		mundoAsoServ.setCodigo(Integer.parseInt(asociosServicioForm.getMapaServAsocios("codigo_"+pos).toString()));
		mundoAsoServ.setInstitucion(usuario.getCodigoInstitucionInt());
		
		//******************ELIMINACION DE UN REGISTRO DE ASOCIOS POR TIPO DE SERVICIO  ***********************//
		if(!mundoAsoServ.eliminarServAsocio(con))
			errores.add("",new ActionMessage("errors.problemasGenericos","al eliminar el registro N° "+(pos)));
		else
		{
			//*************** creacion del Log de registro de la informacion eliminada *********************///
			String temp3[] = {"codigo_","tipoServicio_","tipoAsocio_","servicio_","institucion_","codigoTipoServicio_","codigoTipoAsocio_","tiposServicioAsocio_"};
			//Utilidades.generarLogGenerico(asociosServicioForm.getMapaServAsocios(), null, usuario.getLoginUsuario(), true, pos, ConstantesBD.logServiciosAsocioCodigo,(String[])asociosServicioForm.getMapaServAsocios("INDICES"));
			Utilidades.generarLogGenerico(asociosServicioForm.getMapaServAsocios(), null, usuario.getLoginUsuario(), true, pos, ConstantesBD.logServiciosAsocioCodigo,(String[])temp3);						
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			asociosServicioForm.setEstado("");
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
			
		asociosServicioForm.setEstado("empezar");				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo de registro de datos para la clase servicios_asocios
	 * @param con
	 * @param asociosServicioForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, AsociosXTipoServicioForm asociosServicioForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		int pos = Utilidades.convertirAEntero(asociosServicioForm.getMapaServAsocios("numRegistros")+"");
		AsociosXTipoServicio mundoAsoServ = new AsociosXTipoServicio();		
		
		
		UtilidadBD.iniciarTransaccion(con);
		logger.info("\n el valor de numregistros es -->"+asociosServicioForm.getMapaServAsocios());
		// El nuevo registro siempres se ubicara en la ultima posicion del mapa
		if(asociosServicioForm.getMapaServAsocios("nuevoRegistro_"+(pos - 1)).equals(ConstantesBD.acronimoSi))
		{
			this.llenarMundo(asociosServicioForm, mundoAsoServ, (pos-1), usuario);
			
			/************ INSERCION DE ASOCIOS POR TIPO DE SERVICIO ***********/
			if (!mundoAsoServ.insertarTiposAsocios(con))
				errores.add("",new ActionMessage("errors.ingresoDatos","el registro. Proceso Cancelado"));	
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			asociosServicioForm.setEstado("");
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		// Para cargar el nuevo registro insertado			
		return mapping.findForward("principalCargarGuardado"); 
	}
	
	/**
	 * Llenar mundo pero solo para insercion
	 * @param asociosServicioForm
	 * @param mundoAsoServ
	 * @param pos
	 * @param usuario
	 */
	private void llenarMundo(AsociosXTipoServicioForm asociosServicioForm, AsociosXTipoServicio mundoAsoServ, int pos, UsuarioBasico usuario)
	{
		System.out.print(" \n entre a  llenarMundo con --> "+asociosServicioForm.getMapaServAsocios());
		
		//asociosServicioForm.getMapaServAsocios("codigo_"+pos)
		if (!asociosServicioForm.getMapaServAsocios("codigo_"+pos).toString().equals(""))
			mundoAsoServ.setCodigo(Integer.parseInt(asociosServicioForm.getMapaServAsocios("codigo_"+pos).toString()));
		
		// verificar si el asocio viene de un nuevo registro o de un registro de modificacion en el cual el campo codigoTipoAsocio_ tendra un valor
		if (!asociosServicioForm.getMapaServAsocios("codigoTipoAsocio_"+pos).toString().equals(""))
		{
			//modificacion			
			mundoAsoServ.setTipo_servicio(asociosServicioForm.getMapaServAsocios("codigoTipoServicio_"+pos).toString());
			mundoAsoServ.setAsocio(Integer.parseInt(asociosServicioForm.getMapaServAsocios("codigoTipoAsocio_"+pos).toString().trim()));
			mundoAsoServ.setServicio(Integer.parseInt(asociosServicioForm.getMapaServAsocios("servicio_"+pos).toString()));
		}
		else
		{	//insercion
			mundoAsoServ.setTipo_servicio(asociosServicioForm.getMapaServAsocios("codigoTipoServicio_"+pos).toString());
			mundoAsoServ.setAsocio(Integer.parseInt(asociosServicioForm.getMapaServAsocios("codigoTipoAsocio_"+pos).toString().trim()));			
			mundoAsoServ.setServicio(Integer.parseInt(asociosServicioForm.getMapaServAsocios("servicio_"+pos).toString()));
			
			/*	
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo _ servicio " + asociosServicioForm.getMapaServAsocios("tipoServicio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
			
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n Codigo _ asocio " + asociosServicioForm.getMapaServAsocios("tipoAsocio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
			
			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n _ servicio " + asociosServicioForm.getMapaServAsocios("servicio_"+pos) + " \n\n\n\n\n\n\n\n\n\n");
			
			*/
			
			
		}
		
		mundoAsoServ.setUsuario(usuario.getLoginUsuario());
		mundoAsoServ.setInstitucion(usuario.getCodigoInstitucionInt());
				
	}


	/**
	 * Metodo de cargado d un nuevo registro en el mapa
	 * @param con
	 * @param asociosServicioForm
	 * @param usuario
	 * @param request
	 * @param response
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, AsociosXTipoServicioForm asociosServicioForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		logger.info("\n entre a nuevo-->"+asociosServicioForm.getMapaServAsocios());
		
		int pos = asociosServicioForm.getNumMapaServAsocios();
	
				logger.info("\n 2 entreeeeeeeeeeeeee");
				AsociosXTipoServicio mundoAsocios = new AsociosXTipoServicio();
				asociosServicioForm.setMapaServAsocios("codigo_"+pos, "");
				asociosServicioForm.setMapaServAsocios("codigoTipoServicio_"+pos, "");
				asociosServicioForm.setMapaServAsocios("codigoTipoAsocio_"+pos, ConstantesBD.codigoNuncaValido+"");
				asociosServicioForm.setMapaServAsocios("servicio_"+pos, "");
				//asociosServicioForm.setMapaServAsocios("codigoTipoAsocio_"+pos, "");
				
				
				//Campo para saber si es un nuevo registro y no imprimirlo en el jsp.
				asociosServicioForm.setMapaServAsocios("nuevoRegistro_"+pos, ConstantesBD.acronimoSi);
				
				//asociosServicioForm.setMapaServAsocios("eliminar_"+pos, ConstantesBD.acronimoNo);
				
				
				asociosServicioForm.setMapaServAsocios("numRegistros", (pos+1));
				
				/*
				// Cargar el mapa de tipos de servicio
				asociosServicioForm.setMapaTiposServicio(mundoAsocios.cargarTiposServicio(con));
				
				// Cargar el mapa de tipos de asocio
				mundoAsocios.setInstitucion(usuario.getCodigoInstitucionInt());
				asociosServicioForm.setMapaTiposAsocio(mundoAsocios.cargarTiposAsocios(con));
				
				*/
				
				
				UtilidadBD.closeConnection(con);	
			
	//			return UtilidadSesion.redireccionar("",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),asociosServicioForm.getNumMapaServAsocios(), response, request, "asociosXTipoServicio.jsp",true);
	
		return mapping.findForward("principal");
		
	}


	/**
	 * Metodo de ordenacion por columna seleccionada
	 * @param con
	 * @param asociosServicioForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, AsociosXTipoServicioForm asociosServicioForm, ActionMapping mapping) {
		
		String[] indices = (String[])asociosServicioForm.getMapaServAsocios("INDICES");
		
		int temp = asociosServicioForm.getNumMapaServAsocios();
		
		asociosServicioForm.setMapaServAsocios(Listado.ordenarMapa(indices,asociosServicioForm.getIndice(),
																	asociosServicioForm.getUltimoIndice(),
																	asociosServicioForm.getMapaServAsocios(), temp));
		
		asociosServicioForm.setMapaServAsocios("numRegistros", temp+"");
		
		asociosServicioForm.setUltimoIndice(asociosServicioForm.getIndice());		
		
		asociosServicioForm.setMapaServAsocios("INDICES",indices);		
		
		UtilidadBD.closeConnection(con);
		asociosServicioForm.setEstado("empezar");
		return mapping.findForward("principal");		
	}


	private ActionForward accionEmpezar(ActionMapping mapping, AsociosXTipoServicioForm asociosServicioForm, Connection con, UsuarioBasico usuario) {
		// instancia mundo
		AsociosXTipoServicio mundoAsocioServicio = new AsociosXTipoServicio();
		
		// Cargar el codigo de la institucion
		mundoAsocioServicio.setInstitucion(usuario.getCodigoInstitucionInt());
		
		// realizar consulta de datos
		asociosServicioForm.setMapaServAsocios(mundoAsocioServicio.cargarServiciosAsocios(con));
		
		
		//Cargar el mapa de tipos de servicio
		asociosServicioForm.setMapaTiposServicio(mundoAsocioServicio.cargarTiposServicio(con));
		
		// Cargar el mapa de tipos de asocio
		mundoAsocioServicio.setInstitucion(usuario.getCodigoInstitucionInt());
		asociosServicioForm.setMapaTiposAsocio(mundoAsocioServicio.cargarTiposAsocios(con));
		
		
		UtilidadBD.closeConnection(con);
		
		// retornar a la pagina jsp
		return mapping.findForward("principal");
	}
}
