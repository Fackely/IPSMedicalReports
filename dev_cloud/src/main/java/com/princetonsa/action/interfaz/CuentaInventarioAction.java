/*
 * Creado en Apr 18, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.sql.SQLException;

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
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CuentaInventarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CuentaInventario;


public class CuentaInventarioAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CuentaInventarioAction.class);
	
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

			if (form instanceof CuentaInventarioForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CuentaInventarioForm cuentaInventarioForm = (CuentaInventarioForm) form;
				String estado=cuentaInventarioForm.getEstado();

				logger.warn("Estado CuentaInventarioAction [" + estado + "]");

				if(estado == null)
				{
					cuentaInventarioForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Cuenta Inventario (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezarClaseInventario(cuentaInventarioForm,mapping, con);
				}
				else if (estado.equals("cargarClasesInventario"))
				{
					return accionCargarClasesInventario (con, mapping, cuentaInventarioForm, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("cargarGruposInventario"))
				{
					return accionCargarGruposInventario (con, mapping, cuentaInventarioForm);
				}
				else if (estado.equals("cargarSubGruposInventario"))
				{
					return accionCargarSubGruposInventario (con, mapping, cuentaInventarioForm);
				}
				else if (estado.equals("cargarArticulosInventario"))
				{
					return accionCargarArticulosInventario (con, mapping, cuentaInventarioForm, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("guardarClaseInventario"))
				{
					return accionGuardarClaseInventario(con, mapping, usuario, cuentaInventarioForm);
				}
				else if (estado.equals("guardarGrupoInventario"))
				{
					return accionGuardarGrupoInventario(con, mapping, usuario, cuentaInventarioForm);
				}
				else if (estado.equals("guardarSubgrupoInventario"))
				{
					return accionGuardarSubGrupoInventario(con, mapping, usuario, cuentaInventarioForm);
				}
				else if (estado.equals("guardarArticuloInventario"))
				{
					return accionGuardarArticuloInventario(con, mapping, usuario, cuentaInventarioForm);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.closeConnection(con);
					response.sendRedirect(cuentaInventarioForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("eliminarClaseInventario"))
				{
					return accionEliminarClaseInventario(mapping, cuentaInventarioForm, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("eliminarGrupoInventario"))
				{
					return accionEliminarGrupoInventario(mapping, cuentaInventarioForm, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("eliminarSubGrupoInventario"))
				{
					return accionEliminarSubGrupoInventario(mapping, cuentaInventarioForm, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else if (estado.equals("eliminarArticuloInventario"))
				{
					return accionEliminarArticuloInventario(mapping, cuentaInventarioForm, con, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());
				} 
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
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
	 * empezar de la Clase de Inventario
	 * @param cuentaInventarioForm CuentaInventarioForm 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "claseInventario.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarClaseInventario	(CuentaInventarioForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		//----Limpiamos lo que venga del form
		forma.reset();
		
		forma.setEstado("empezar");		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Método que carga las clases de inventario parametrizadas para el centro de costo seleccionado y la
	 * institución del médico
	 * @param con
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param institucion
	 * @return 
	 */
	private ActionForward accionCargarClasesInventario (Connection con, ActionMapping  mapping, CuentaInventarioForm cuentaInventarioForm, int institucion)
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		int centroCostoSeleccionado=cuentaInventarioForm.getCentroCostoSeleccionado();
		
		try
		{
			cuentaInventarioForm.setNombreCentroCosto(UtilidadValidacion.getNombreCentroCosto(con, centroCostoSeleccionado));
		} 
		catch (SQLException e)
		{
			logger.error("Error cargando el nombre del centro de costo seleccionado en accionCargarClasesInventario" );
		}
		
		cuentaInventarioForm.setMapaClaseInventarios(mundoCuentaInventario.consultarClaseInventariosCuentaIngreso (con, centroCostoSeleccionado, institucion));
		
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que carga los grupos de inventario para el centro de costo seleccionado y la 
	 * clase de inventario seleccionada
	 * @param con
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @return 
	 */
	private ActionForward accionCargarGruposInventario(Connection con, ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm)
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		int centroCostoSeleccionado=cuentaInventarioForm.getCentroCostoSeleccionado();
		int claseInventarioSeleccionado=cuentaInventarioForm.getCodigoClaseInventario();
		
		
		
		cuentaInventarioForm.setMapaGrupoInventarios(mundoCuentaInventario.consultarGrupoInventariosCuentaIngreso (con, centroCostoSeleccionado, claseInventarioSeleccionado));
		
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("gruposInventario");
	}

	/**
	 * Método que carga los subgrupos de inventario para el centro de costo seleccionado y el 
	 * grupo de inventario seleccionado
	 * @param con
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @return 
	 */
	private ActionForward accionCargarSubGruposInventario(Connection con, ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm)
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		int centroCostoSeleccionado=cuentaInventarioForm.getCentroCostoSeleccionado();
		int grupoInventarioSeleccionado=cuentaInventarioForm.getCodigoGrupoInventario();
		
		cuentaInventarioForm.setMapaSubGrupoInventarios(mundoCuentaInventario.consultarSubGrupoInventariosCuentaIngreso (con, centroCostoSeleccionado, grupoInventarioSeleccionado));
		
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("subGruposInventario");
	}
	
	/**
	 * Método que carga los articulos de inventario para el centro de costo seleccionado y el 
	 * subgrupo de inventario seleccionado
	 * @param con
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param institucion
	 * @return
	 */
	private ActionForward accionCargarArticulosInventario(Connection con, ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm, int institucion)
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		int centroCostoSeleccionado=cuentaInventarioForm.getCentroCostoSeleccionado();
		int subgrupoInventarioSel=cuentaInventarioForm.getCodigoSubGrupoInventario();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(institucion);
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		cuentaInventarioForm.setMaxPageItems( Integer.parseInt(numItems) );		
		
		cuentaInventarioForm.setMapaArticuloInventarios(mundoCuentaInventario.consultarArticulosInventarioCuentaIngreso (con, centroCostoSeleccionado, subgrupoInventarioSel));
		
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("articulosInventario");
	}
	
	/**
	 * Método que guarda la información de cuenta de ingreso de las
	 * clases de inventario para el centro de costo seleccionado 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param cuentaInventarioForm
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarClaseInventario(Connection con, ActionMapping mapping, UsuarioBasico usuario, CuentaInventarioForm cuentaInventarioForm) throws SQLException
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.insertarClasesInventario (con, usuario.getLoginUsuario());
		
		cuentaInventarioForm.setEstado("cargarClasesInventario");
		return accionCargarClasesInventario(con, mapping, cuentaInventarioForm, usuario.getCodigoInstitucionInt());
	}
	
	/**
	 * Método que guarda la información de cuenta de ingreso de los
	 * grupos de inventario para el centro de costo y clase de inventario seleccionado 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param cuentaInventarioForm
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarGrupoInventario(Connection con, ActionMapping mapping, UsuarioBasico usuario, CuentaInventarioForm cuentaInventarioForm) throws SQLException
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.insertarGruposInventario (con, usuario.getLoginUsuario());
		
		cuentaInventarioForm.setEstado("cargarGruposInventario");
		return accionCargarGruposInventario(con, mapping, cuentaInventarioForm);
	}
	
	/**
	 * Método que guarda la información de cuenta de ingreso de los
	 * subGrupos de inventario para el centro de costo, clase de inventario y grupo de inventario seleccionado 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param cuentaInventarioForm
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarSubGrupoInventario(Connection con, ActionMapping mapping, UsuarioBasico usuario, CuentaInventarioForm cuentaInventarioForm) throws SQLException
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.insertarSubGruposInventario (con, usuario.getLoginUsuario());
		
		cuentaInventarioForm.setEstado("cargarSubGruposInventario");
		return accionCargarSubGruposInventario(con, mapping, cuentaInventarioForm);
	}
	
	/**
	 * Método que guarda la información de cuenta de ingreso de los
	 * articulos de inventario para el centro de costo, clase, grupo y subgrupo de inventario seleccionado 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param cuentaInventarioForm
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarArticuloInventario(Connection con, ActionMapping mapping, UsuarioBasico usuario, CuentaInventarioForm cuentaInventarioForm) throws SQLException
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.insertarArticulosInventario (con, usuario.getLoginUsuario());
		
		cuentaInventarioForm.setEstado("cargarArticulosInventario");
		return accionCargarArticulosInventario(con, mapping, cuentaInventarioForm, usuario.getCodigoInstitucionInt());
	}
	
	/**
	 * Método que elimina la cuenta contable de la clase de inventario
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	private ActionForward accionEliminarClaseInventario (ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm, Connection con, int codigoInstitucionInt, String loginUsuario) throws SQLException 
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.setClaseInventarioEliminar(cuentaInventarioForm.getClaseInventarioEliminar());
		mundoCuentaInventario.setNombreCuentaEliminar(cuentaInventarioForm.getNombreCuentaEliminar());
		mundoCuentaInventario.setNombreCuentaVigenciaAnteriorEliminar(cuentaInventarioForm.getNombreCuentaVigenciaAnteriorEliminar());
		mundoCuentaInventario.setPosicionEliminar(cuentaInventarioForm.getPosicionEliminar());
		
		/* Se elimina la cuenta contable de la clase de inventario generando el log respectivo 
		 * (1) Indica que se eliminará la cuenta contable de clase inventario
		 */
		mundoCuentaInventario.eliminarCuentaContable (con, 1, loginUsuario);
		
		cuentaInventarioForm.setEstado("cargarClasesInventario");
		return accionCargarClasesInventario(con, mapping, cuentaInventarioForm, codigoInstitucionInt);
	}
	
	/**
	 * Método que elimina la cuenta contable del grupo de inventario
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	private ActionForward accionEliminarGrupoInventario (ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm, Connection con, int codigoInstitucionInt, String loginUsuario) throws SQLException 
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.setGrupoInventarioEliminar(cuentaInventarioForm.getGrupoInventarioEliminar());
		mundoCuentaInventario.setNombreCuentaEliminar(cuentaInventarioForm.getNombreCuentaEliminar());
		mundoCuentaInventario.setNombreCuentaVigenciaAnteriorEliminar(cuentaInventarioForm.getNombreCuentaVigenciaAnteriorEliminar());
		mundoCuentaInventario.setPosicionEliminar(cuentaInventarioForm.getPosicionEliminar());
		
		/* Se elimina la cuenta contable del grupo inventario generando el log respectivo 
		 * (2) Indica que se eliminará la cuenta contable del grupo nventario
		 */
		mundoCuentaInventario.eliminarCuentaContable (con, 2, loginUsuario);
		
		cuentaInventarioForm.setEstado("cargarGruposInventario");
		return accionCargarGruposInventario(con, mapping, cuentaInventarioForm);
	}
	
	/**
	 * Método que elimina la cuenta contable del sub-grupo de inventario
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	private ActionForward accionEliminarSubGrupoInventario (ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm, Connection con, int codigoInstitucionInt, String loginUsuario) throws SQLException 
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.setSubGrupoInventarioEliminar(cuentaInventarioForm.getSubGrupoInventarioEliminar());
		mundoCuentaInventario.setNombreCuentaEliminar(cuentaInventarioForm.getNombreCuentaEliminar());
		mundoCuentaInventario.setNombreCuentaVigenciaAnteriorEliminar(cuentaInventarioForm.getNombreCuentaVigenciaAnteriorEliminar());
		mundoCuentaInventario.setPosicionEliminar(cuentaInventarioForm.getPosicionEliminar());
		
		/* Se elimina la cuenta contable del sub-grupo inventario generando el log respectivo 
		 * (3) Indica que se eliminará la cuenta contable del sub-grupo nventario
		 */
		mundoCuentaInventario.eliminarCuentaContable (con, 3, loginUsuario);
		
		cuentaInventarioForm.setEstado("cargarSubGruposInventario");
		return accionCargarSubGruposInventario(con, mapping, cuentaInventarioForm);
	}
	
	/**
	 * Método que elimina la cuenta contable del articulo de inventario
	 * @param mapping
	 * @param cuentaInventarioForm
	 * @param con
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	private ActionForward accionEliminarArticuloInventario (ActionMapping mapping, CuentaInventarioForm cuentaInventarioForm, Connection con, int codigoInstitucionInt, String loginUsuario) throws SQLException 
	{
		CuentaInventario mundoCuentaInventario = new CuentaInventario();
		this.llenarMundo (cuentaInventarioForm, mundoCuentaInventario);
		
		mundoCuentaInventario.setArticuloInventarioEliminar(cuentaInventarioForm.getArticuloInventarioEliminar());
		mundoCuentaInventario.setNombreCuentaEliminar(cuentaInventarioForm.getNombreCuentaEliminar());
		mundoCuentaInventario.setNombreCuentaVigenciaAnteriorEliminar(cuentaInventarioForm.getNombreCuentaVigenciaAnteriorEliminar());
		mundoCuentaInventario.setPosicionEliminar(cuentaInventarioForm.getPosicionEliminar());
		
		/* Se elimina la cuenta contable del articulo inventario generando el log respectivo 
		 * (4) Indica que se eliminará la cuenta contable del articulo nventario
		 */
		mundoCuentaInventario.eliminarCuentaContable (con, 4, loginUsuario);
		
		cuentaInventarioForm.setEstado("cargarArticulosInventario");
		return accionCargarArticulosInventario(con, mapping, cuentaInventarioForm, codigoInstitucionInt);
	}
	
	/**
	 * Método para pasar los datos de la forma al mundo
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo (CuentaInventarioForm forma, CuentaInventario mundo)
	{
		mundo.setCentroCostoSeleccionado(forma.getCentroCostoSeleccionado());
		mundo.setCodigoClaseInventario(forma.getCodigoClaseInventario());
		mundo.setMapaClaseInventarios(forma.getMapaClaseInventarios());
		mundo.setMapaGrupoInventarios(forma.getMapaGrupoInventarios());
		mundo.setMapaSubGrupoInventarios(forma.getMapaSubGrupoInventarios());
		mundo.setMapaArticuloInventarios(forma.getMapaArticuloInventarios());
	}
	
}

