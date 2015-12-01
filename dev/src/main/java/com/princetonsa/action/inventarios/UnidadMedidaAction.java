/*
 * Creado en May 31, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.inventarios;

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

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.UnidadMedidaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.UnidadMedida;

public class UnidadMedidaAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(UnidadMedidaAction.class);
	
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

			if (form instanceof UnidadMedidaForm)
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
				UnidadMedidaForm unidadMedidaForm = (UnidadMedidaForm) form;
				String estado=unidadMedidaForm.getEstado();

				logger.warn("Estado UnidadMedidaAction [" + estado + "]");

				if(estado == null)
				{
					unidadMedidaForm.reset(true);	
					logger.warn("Estado no valido dentro del flujo de Unidad de Medida (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					/*
					 * Reseteamos el Mensaje
					 */
					unidadMedidaForm.resetMensaje();
					return accionEmpezar(con, mapping, unidadMedidaForm, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("agregarRegistro"))
				{
					return accionAgregarUnidadMedida(con, mapping, unidadMedidaForm);
				}
				else if (estado.equals("guardar"))
				{
					unidadMedidaForm.setPatronOrdenar("");
					unidadMedidaForm.setUltimoPatron("");
					return this.accionGuardar(con, mapping, unidadMedidaForm, usuario);
				}
				else if (estado.equals("eliminarUnidadMedida"))
				{
					return accionEliminar (con, mapping, unidadMedidaForm, usuario);
				}
				else if (estado.equals("ordenar"))  //-Ordenar el listado de unidades de medida 
				{			    
					return accionOrdenar(unidadMedidaForm, mapping, con);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{
					/*
					 * Reseteamos el Mensaje
					 */
					unidadMedidaForm.resetMensaje();
					UtilidadBD.closeConnection(con);
					response.sendRedirect(unidadMedidaForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("consultar"))
				{			    
					return accionConsultar(con, mapping, unidadMedidaForm, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("ordenarConsulta"))  //-Ordenar el listado de unidades de medida 
				{			    
					return accionOrdenarConsulta(unidadMedidaForm, mapping, con);
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
		 * Método que carga las unidades de medida parametrizadas a la institución,
		 * para permitir su modificación y eliminación
		 * @param con
		 * @param mapping
		 * @param unidadMedidaForm
		 * @param institucion
		 * @return 
		 */
		private ActionForward accionEmpezar (Connection con, ActionMapping  mapping, UnidadMedidaForm unidadMedidaForm, int institucion)
		{
			/*
			 * Reseteamos el mensaje para evitar que se muestre en la pantalla cuando el estado es empezar:
			 * Explicacion de Variable mostrarMensaje:
			 * mostrarMensaje == true, SI se muestra el mensaje (quiere decir que se hizo una Modificación o Eliminación)
			 * mostrarMensaje == false, NO se muestra el mensaje (quiere decir que está entrando a la funcionalidad por vez primera)
			 * Solución Tarea: 33584
			 * Al guardar información en esta funcionalidad, el sistema no presenta los mensajes de confimación, que describen proceso realizado 
			 * con exito, los cuales se solicitan ya que son de importancia para los usuarios certifiquen que el proceso realizado es guardado en 
			 * el sistema.
			 * EL MENSAJE SE ESTA QUEDANDO PEGADO, AL ENTRAR A LA FUNCIONALIDAD SE ESTA MOSTRANDO
			 * Favor revisar,
			 * Gracias.
			 * Luis Felipe Jaramillo.
			 * Acceptor: 	Luis Felipe Perez Granda
			 * Created: 	2008-08-05
			 *//*
			boolean mostrarMensaje = unidadMedidaForm.getMostrarMensaje();
			if(mostrarMensaje == true)
			{
				logger.info("===> SI Se va a mostrar el Mensaje !!! ");
			}
			else
			{
				logger.info("===> NO Se va a mostrar el Mensaje !!! ");
				unidadMedidaForm.resetMensaje();
			}
			*/
			UnidadMedida mundoUnidadMedida = new UnidadMedida();
			
			//--------------Se resetea el form -----------//
			unidadMedidaForm.reset(true);
			
			//Se obtiene el número de registros por página que se tiene parametrizado
			String numItems=ValoresPorDefecto.getMaxPageItems(institucion);
			if(numItems==null || numItems.trim().equals(""))
			{
				numItems="20";
			}
			unidadMedidaForm.setMaxPageItems( Integer.parseInt(numItems) );		
		
		//--------Se consultan las unidades de medida parametrizadas a la institución -------//
		unidadMedidaForm.setMapa(mundoUnidadMedida.consultarUnidadesMedidaInstitucion (con));
		
		unidadMedidaForm.setPatronOrdenar("");
		unidadMedidaForm.setUltimoPatron("");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
	
	/**
	 * Método que agrega al mapa la información de la unidad de medida
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionAgregarUnidadMedida(Connection con, ActionMapping mapping, UnidadMedidaForm forma)
	{
		//----------Número de registros en el mapa --------------------//
		int numRegistros=Integer.parseInt(forma.getMapa("numRegistros")+"");
				
		String acronimo=forma.getAcronimo();
		String nombreUnidad=forma.getNombreUnidad();
		String unidosis=forma.getUnidosis();
		String activo=forma.getActivo();
		
		
		//---------Se agrega al mapa el nuevo registro de unidad de medida ------//
		forma.setMapa("acronimo_"+numRegistros, acronimo);
		forma.setMapa("acronimoAnt_"+numRegistros, acronimo);
		forma.setMapa("unidad_"+numRegistros, nombreUnidad);
		forma.setMapa("unidadAnt_"+numRegistros, nombreUnidad);
		forma.setMapa("unidosis_"+numRegistros, unidosis);
		forma.setMapa("unidosisAnt_"+numRegistros, unidosis);
		forma.setMapa("activo_"+numRegistros, activo);
		forma.setMapa("activoAnt_"+numRegistros, activo);
		forma.setMapa("esta_grabado_"+numRegistros, "0");
		forma.setPatronOrdenar("");
		forma.setUltimoPatron("");
		
		numRegistros++;
		
		forma.setMapa("numRegistros", numRegistros+"");
		
		//------ Se resetea los atributos del form excepto el mapa -------//
		forma.reset(false);
				
		UtilidadBD.closeConnection(con);
		/*
		 * Vamos a mostrar un mensaje para cuando se adicione un registro a la tabla cargada
		 */
		logger.info("===> Voy a mostrar EL REGISTRO SE AGREGÓ CORRECTAMENTE !!!");
		forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO SE AGREGÓ CORRECTAMENTE !!!"));
		return mapping.findForward("principal");	
	}
	
	/**
	 * Método que guarda las nuevas unidades de medida y modifica
	 * las unidades de medida ya ingresadas 
	 * insertados 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, UnidadMedidaForm forma, UsuarioBasico usuario) throws SQLException
	{
		UnidadMedida mundoUnidadMedida = new UnidadMedida();
		
		//-----Se pasa el mapa del form al mundo ------------//
		mundoUnidadMedida.setMapa(forma.getMapa());
		
		//-----------Se insertan los registros agregados al mapa --------------------//
		
		boolean inserto = false;
		
		inserto = mundoUnidadMedida.insertarModificarUnidadMedida (con, usuario);
		
		if(inserto == true)
		{
			logger.info("===> Voy a mostrar Proceso Realizado Con Éxito");
			forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON ÉXITO !!!"));
		}
		return accionEmpezar(con, mapping, forma, usuario.getCodigoInstitucionInt());
	}
	
	/**
	 * Método que elimina el registro seleccionado del mapa, en el caso que ya esté grabado se borra
	 * de la base de datos sino simplemete se borra del mapa
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 * @throws SQLException
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, UnidadMedidaForm forma, UsuarioBasico usuario) throws SQLException
	{
		int indiceEliminado=Integer.parseInt(forma.getMapa("indiceRegEliminar")+"");
		
		/*Se verifica si el registro está grabado para borrarlo de la base de datos, 
		sino se borra el registro del mapa*/
		if (Integer.parseInt(forma.getMapa("esta_grabado_"+indiceEliminado)+"")==1)
		{
			UnidadMedida mundoUnidadMedida = new  UnidadMedida();
			String acronimo=forma.getMapa("acronimo_"+indiceEliminado)+"";
			
			//----------Se elimina la unidad de medida ---------//
			if (mundoUnidadMedida.eliminarUnidadMedida (con, acronimo))
			{
				//-----------------------------------------------GENERACION DEL LOG AL ELIMINAR UNIDAD DE MEDIDA --------------------------------------------------//
				StringBuffer log=new StringBuffer();
				log.append("\n===============ELIMINACIÓN DE UNIDAD DE MEDIDA================");
				String nombreUnidad=(String)forma.getMapa("unidad_"+indiceEliminado);
									
				log.append("\n CÒDIGO O ACRÒNONIMO :"+acronimo);
				log.append("\n NOMBRE UNIDAD :"+nombreUnidad);
									
				log.append("\n======================================================================");
				   //-Generar el log 
				LogsAxioma.enviarLog(ConstantesBD.logUnidadMedidaCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
				
				//-------Se elimina el registro del mapa -----------//
				forma.eliminarRegistroMapa ();
			}
		}
	//----------Cuando el registro no ha sido guardado se borra del mapa ----------------//
	else
		{
			forma.eliminarRegistroMapa ();
		}
		UtilidadBD.cerrarConexion(con);
		/*
		 * Vamos a mostrar un mensaje para cuando se elimine un registro a la tabla cargada
		 */
		logger.info("===> Voy a mostrar EL REGISTRO SE ELIMINÓ CORRECTAMENTE !!!");
		forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO SE ELIMINÓ CORRECTAMENTE !!!"));
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que ordena el mapa que el listado de unidades de medida
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenar (UnidadMedidaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"acronimo_", "acronimoAnt_", "unidad_", "unidadAnt_", "unidosis_", "unidosisAnt_", "activo_","activoAnt_", "esta_grabado_"};

		Integer num = Integer.parseInt(forma.getMapa("numRegistros")+"");
		
		forma.setMapa(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapa(),
									num.intValue() ));
        
        forma.getMapa().put("numRegistros", num+"");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("principal");
	}
	
	/**
	 * Método que ordena el mapa que el listado de unidades de medida en la consulta
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionOrdenarConsulta (UnidadMedidaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"acronimo_", "unidad_", "unidosis_", "activo_", "esta_grabado_"};

		Integer num = Integer.parseInt(forma.getMapa("numRegistros")+"");
		
		forma.setMapa(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapa(),
									num.intValue() ));
        
        forma.getMapa().put("numRegistros", num+"");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("consulta");
	}

	
	/**
	 * Método que consulta las unidades de medida parametrizadas a la institución,
	 * @param con
	 * @param mapping
	 * @param unidadMedidaForm
	 * @return 
	 */
	private ActionForward accionConsultar (Connection con, ActionMapping  mapping, UnidadMedidaForm unidadMedidaForm, int institucion)
	{
		UnidadMedida mundoUnidadMedida = new UnidadMedida();
		
		//--------------Se resetea el form -----------//
		unidadMedidaForm.reset(true);
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(institucion);
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		unidadMedidaForm.setMaxPageItems( Integer.parseInt(numItems) );		
	
	//--------Se consultan las unidades de medida parametrizadas a la institución -------//
	unidadMedidaForm.setMapa(mundoUnidadMedida.consultarUnidadesMedidaInstitucion (con));
	
	unidadMedidaForm.setPatronOrdenar("");
	unidadMedidaForm.setUltimoPatron("");
	
	UtilidadBD.closeConnection(con);
	return mapping.findForward("consulta");
	
}

}
