/*
 * Creado el 19-may-2004
 *
 * AXIOMA
 *
 * Autor: Juan David Ramírez
 * juan@princetonSA.com
 * 
 * EsquemasTarifariosAction.java
 */
package com.princetonsa.action.cargos;

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

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.cargos.EsquemasTarifariosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Juan David Ramírez
 *
 * juan@princetonSA.com
 */
public class EsquemasTarifariosAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Esquemas Tarifarios 
	 */
	private Logger logger = Logger.getLogger(EsquemasTarifariosAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		Connection con=null;
		try{
		
		if(form instanceof EsquemasTarifariosForm)
		{
			EsquemasTarifariosForm esquemasTarifariosForm=(EsquemasTarifariosForm)form;

			if( logger.isDebugEnabled() )
			{
				logger.debug("Entro al Action de Egreso");
			}

			
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
				logger.error("Problemas abriendo la conexión en ServiciosAction");
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			//Lo primero que vamos a hacer es validar que se
			//cumplan las condiciones.
			
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


			String estado=esquemasTarifariosForm.getEstado();
			logger.warn("estado->"+estado);
			try
			{
				if (estado==null||estado.equals(""))
				{
					return this.accionSalirEstadoInvalido(mapping, request, con);
				}
				else if (estado.equals("insertar"))
				{
					esquemasTarifariosForm.reset();
					return this.accionComunEmpezar(mapping, esquemasTarifariosForm, con, "insertar");
				}
				else if(estado.equals("agregarValor"))
				{
					if(esquemasTarifariosForm.getTarifarioOficial()==ConstantesBD.codigoTarifarioISS)
						esquemasTarifariosForm.setCantidad(Float.parseFloat(ValoresPorDefecto.getValorUVR(usuario.getCodigoInstitucionInt())));
					esquemasTarifariosForm.setEstado("salir");
					this.cerrarConexion(con);
					return mapping.findForward("esquemaTarifario");
					
				}
				else if (estado.equals("empezarModificar"))
				{
					esquemasTarifariosForm.reset();
					esquemasTarifariosForm.setAccionAFinalizar("modificar");
					this.cerrarConexion(con);
					return mapping.findForward("busqueda");
				}
				else if (estado.equals("empezarConsultar"))
				{
					esquemasTarifariosForm.reset();
					esquemasTarifariosForm.setAccionAFinalizar("consultar");
					this.cerrarConexion(con);
					return mapping.findForward("busqueda");
				}
				else if (estado.equals("modificar"))
				{
					return this.accionModificarOResumen(mapping, request, con, esquemasTarifariosForm, usuario, true, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("resumen"))
				{
					return this.accionModificarOResumen(mapping, request, con, esquemasTarifariosForm, usuario, false, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("salir"))
				{
					if (esquemasTarifariosForm.getAccionAFinalizar()==null)
					{
						return this.accionSalirEstadoInvalido(mapping, request, con);
					}
					else
					if (esquemasTarifariosForm.getAccionAFinalizar().equals("insertar"))
					{
						return this.accionSalirInsertar(mapping, request, con, esquemasTarifariosForm, usuario);
					}
					else
					if (esquemasTarifariosForm.getAccionAFinalizar().equals("modificar"))
					{
						return this.accionSalirModificar(mapping, request, con, esquemasTarifariosForm, usuario);
					}
					else
					{
						return this.accionSalirEstadoInvalido(mapping, request, con);
					}
				}
				else if (estado.equals("busqueda"))
				{
					this.cerrarConexion(con);
					return mapping.findForward("busqueda");
				}
				else if (estado.equals("inicioBusqueda"))
				{
					esquemasTarifariosForm.reset();
					this.cerrarConexion(con);
					return mapping.findForward("inicioBusqueda");
				}
				else if (estado.equals("paginaBusqueda"))
				{
					this.cerrarConexion(con);
					return mapping.findForward("paginaBusqueda");
				}
				else if (estado.equals("resultadoBusqueda"))
				{
					return accionBuscarEsquemaTarifario(mapping, con, esquemasTarifariosForm, usuario.getCodigoInstitucionInt());
				}
				else
				{
					return this.accionSalirEstadoInvalido(mapping, request, con);
				}
			}
			catch(SQLException e)
			{
				//No se cierra conexión porque si llega aca ocurrió un
				//error al abrirla
				logger.error("Problemas en la BD " + e);
				return this.enviarPaginaError(mapping, request, con, "errors.problemasBd", true);
			}
		}
		else
		{
			//Todavía no existe conexión, por eso no se cierra
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	 * Método que implementa las acciones comunes a empezar
	 * para la mayoría de operaciones de Esquemas Tarifarios
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param esquemasTarifariosForm Objeto Form de 
	 * Esquemas tarifarios
	 * @param con Conexión con la fuente de datos
	 * @param textoAccionAFinalizar Cual es la acción que en
	 * el futuro se finalizará
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionComunEmpezar (ActionMapping mapping, EsquemasTarifariosForm esquemasTarifariosForm, Connection con, String textoAccionAFinalizar) throws SQLException
	{
		esquemasTarifariosForm.setAccionAFinalizar(textoAccionAFinalizar);
				
		esquemasTarifariosForm.setEstado("salir");
		this.cerrarConexion(con);
		return mapping.findForward("esquemaTarifario");
	}

	/**
	 * Método que se llama siempre que el usuario haya dado
	 * un estado o una accion a finalizar invalida
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionSalirEstadoInvalido(ActionMapping mapping,HttpServletRequest request, Connection con) throws Exception
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug("La accion a finalizar no esta definida (EsquemasTarifariosAction)");
		}
		return this.enviarPaginaError(mapping, request, con, "errors.estadoInvalido", true);
	}

	/**
	* Método en que se cierra la conexión (Buen manejo
	* recursos), usado ante todo al momento de hacer
	* un forward
	* @param con Conexión con la fuente de datos
	* @throws SQLException
	*/
	private void cerrarConexion (Connection con) throws SQLException
	{
		if (con!=null&&!con.isClosed())
		{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método que llena el objeto de Esquemas Tarifarios con los datos 
	 * presentes en el Form de Esquemas Tarifarios 
	 * 
	 * @param esquemasTarifariosForm Form específico para Esquemas
	 * Tarifarios
	 * @param esquemaTarifario Objeto que maneja Esquemas Tarifarios
	 */
	private void llenarObjetoConForm (EsquemasTarifariosForm esquemasTarifariosForm, EsquemaTarifario esquemaTarifario)
	{
		//Cuando llenamos el objeto en inserción el código no importa
		esquemaTarifario.setCodigo(esquemasTarifariosForm.getCodigo());
		esquemaTarifario.setEsInventario(esquemasTarifariosForm.getEsInventario());
		esquemaTarifario.setMetodoAjuste(new InfoDatos (esquemasTarifariosForm.getMetodoAjuste()+"",""));
		esquemaTarifario.setNombre(esquemasTarifariosForm.getNombre());
		if(!esquemaTarifario.isEsInventario())
			esquemaTarifario.setTarifarioOficial(new InfoDatosInt(esquemasTarifariosForm.getTarifarioOficial(), ""));
		else
			esquemaTarifario.setTarifarioOficial(new InfoDatosInt(-1, ""));
		esquemaTarifario.setInventarioAux(esquemasTarifariosForm.getInventarioAux());
		esquemaTarifario.setCantidad(esquemasTarifariosForm.getCantidad());
				
		esquemaTarifario.setActivo(esquemasTarifariosForm.getActivo());
	}

	/**
	 * Método que llena el Form de Esquemas Tarifarios con los datos 
	 * presentes en el objeto de Esquemas Tarifarios 
	 * 
	 * @param esquemasTarifariosForm Form específico para Esquemas
	 * Tarifarios
	 * @param esquemaTarifario Objeto que maneja Esquemas Tarifarios
	 */
	private void llenarFormConObjeto (EsquemasTarifariosForm esquemasTarifariosForm, EsquemaTarifario esquemaTarifario)
	{
		esquemasTarifariosForm.setCodigo(esquemaTarifario.getCodigo());
		esquemasTarifariosForm.setEsInventario(esquemaTarifario.isEsInventario());
		esquemasTarifariosForm.setNombre(esquemaTarifario.getNombre());

		esquemasTarifariosForm.setMetodoAjuste(esquemaTarifario.getMetodoAjuste().getAcronimo().charAt(0));
		esquemasTarifariosForm.setNombreMetodoAjuste(esquemaTarifario.getMetodoAjuste().getNombre());

		esquemasTarifariosForm.setTarifarioOficial(esquemaTarifario.getTarifarioOficial().getCodigo());
		esquemasTarifariosForm.setNombreTarifarioOficial(esquemaTarifario.getTarifarioOficial().getNombre());
		
		esquemasTarifariosForm.setActivo(esquemaTarifario.getActivo());
		esquemasTarifariosForm.setCantidad(esquemaTarifario.getCantidad());
	}
	
	/**
	 * Método que me lleva a la página de error
	 * 
	 * @param mapping Mapping de Struts 
	 * @param request Request de la aplicación web
	 * @param con Conexión con la fuente de datos
	 * @param error Error a mostrar
	 * @param errorTipoCodigo Define si el error corresponde
	 * a un código en el ApplicationResources
	 * @return
	 * @throws SQLException
	 */
	private ActionForward enviarPaginaError (ActionMapping mapping, HttpServletRequest request, Connection con, String error, boolean errorTipoCodigo) throws SQLException
	{
		this.cerrarConexion(con);
		if (errorTipoCodigo)
		{
			request.setAttribute("codigoDescripcionError", error);
		}
		else
		{
			request.setAttribute("descripcionError", error);
		}
		return mapping.findForward("paginaError");
	}
	
	/**
	 * Acciones comunes que se realizan al modificar o mostrar un
	 * resumen de Esquema Tarifario
	 * 
	 * @param mapping Mapping de Struts 
	 * @param request Request de la aplicación web
	 * @param con Conexión con la fuente de datos
	 * @param esquemasTarifariosForm Form específico para Esquemas
	 * Tarifarios
	 * @param usuario Usuario que esta en la funcionalidad
	 * @param esModificar Boolean que indica si me encuentro
	 * en medio de una modificación o tan solo de un resumen
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionModificarOResumen (ActionMapping mapping, HttpServletRequest request, Connection con, EsquemasTarifariosForm esquemasTarifariosForm, UsuarioBasico usuario, boolean esModificar, int institucion) throws SQLException, IPSException
	{
		esquemasTarifariosForm.reset();
		String codigoEsquemaTarifarioString=request.getParameter("codigoEsquemaTarifario");
		
		//Si no va como parametro, puede ir como atributo
		if (codigoEsquemaTarifarioString==null)
		{
			codigoEsquemaTarifarioString=(String)request.getAttribute("codigoEsquemaTarifario");
		}
		
		int codigoEsquemaTarifarioInt=0;
		try
		{
			codigoEsquemaTarifarioInt=Integer.parseInt(codigoEsquemaTarifarioString);
		}
		catch(Exception e)
		{
			logger.error("El código del esquema tarifario entregado por el usuario " + usuario.getLoginUsuario() + " no es válido . Excepción: " +e);
			return this.enviarPaginaError(mapping, request, con, "error.esquematarifario.noExiste", true);
		}
		EsquemaTarifario esquemaTarifario=new EsquemaTarifario ();
		esquemaTarifario.setCodigo(codigoEsquemaTarifarioInt);
		if (esquemaTarifario.cargar(con, institucion).isTrue())
		{
			this.llenarFormConObjeto(esquemasTarifariosForm, esquemaTarifario);
			if (esModificar)
			{
				return this.accionComunEmpezar(mapping, esquemasTarifariosForm, con, "modificar");
			}
			else
			{
				return this.accionComunEmpezar(mapping, esquemasTarifariosForm, con, "resumen");
			}
		}
		else
		{
			logger.error("El código del esquema tarifario entregado por el usuario " + usuario.getLoginUsuario() + " no es válido (" + codigoEsquemaTarifarioInt + ")");
			return this.enviarPaginaError(mapping, request, con, "error.esquematarifario.noExiste", true);
		}
	}
	

	/**
	 * Método que realiza toda la funcionalidad correspondiente
	 * a salir después de llenar los datos de insertar
	 * 
	 * @param mapping Mapping de Struts 
	 * @param request Request de la aplicación web
	 * @param con Conexión con la fuente de datos
	 * @param esquemasTarifariosForm Form específico para Esquemas
	 * Tarifarios
	 * @param usuario Usuario que esta en la funcionalidad
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalirInsertar(ActionMapping mapping, HttpServletRequest request, Connection con, EsquemasTarifariosForm esquemasTarifariosForm, UsuarioBasico usuario) throws SQLException
	{
		EsquemaTarifario esquemaTarifario=new EsquemaTarifario ();
		this.llenarObjetoConForm(esquemasTarifariosForm, esquemaTarifario);
		ResultadoBoolean resp=esquemaTarifario.insertar(con, usuario.getCodigoInstitucion());
		if (resp.isTrue())
		{
			this.cerrarConexion(con);
			request.setAttribute("codigoEsquemaTarifario", resp.getDescripcion());
			return mapping.findForward("resumen");
		}
		else
		{
			logger.error("No se pudo insertar en EsquemaTarifario, return de insertar dió false. Usuario : " + usuario.getLoginUsuario());
			return this.enviarPaginaError(mapping, request, con, "errors.problemasBd", true);
		}
	}


	/**
	 * Método que realiza toda la funcionalidad correspondiente
	 * a salir después de llenar los datos de modificar
	 * 
	 * @param mapping Mapping de Struts 
	 * @param request Request de la aplicación web
	 * @param con Conexión con la fuente de datos
	 * @param esquemasTarifariosForm Form específico para Esquemas
	 * Tarifarios
	 * @param usuario Usuario que esta en la funcionalidad
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalirModificar (ActionMapping mapping, HttpServletRequest request, Connection con, EsquemasTarifariosForm esquemasTarifariosForm, UsuarioBasico usuario) throws SQLException, IPSException
	{
	    	    
		EsquemaTarifario mundoEsquemaTarifario=new EsquemaTarifario ();
		mundoEsquemaTarifario.cargarXcodigo(con,esquemasTarifariosForm.getCodigo(), usuario.getCodigoInstitucionInt());
		String log=generarCabeceraLog(mundoEsquemaTarifario);
		if(compararObjetos(mundoEsquemaTarifario,esquemasTarifariosForm))
		{
			mundoEsquemaTarifario.clean();
			this.llenarObjetoConForm(esquemasTarifariosForm, mundoEsquemaTarifario);
			ResultadoBoolean resp=mundoEsquemaTarifario.modificar(con);
			if (resp.isTrue())
			{
				mundoEsquemaTarifario.cargarXcodigo(con,mundoEsquemaTarifario.getCodigo(), usuario.getCodigoInstitucionInt());
				 log+="\n            ====INFORMACION DESPUES DE LA MODIFICACION ===== " +
					"\n*  Código Esquema Tarifario [" +mundoEsquemaTarifario.getCodigo() +"] ";
					if(mundoEsquemaTarifario.isEsInventario())
						log += "\n*  Es Inventario [ SI ]" ;
					else
						log += "\n*  Es Inventario  [ NO ]";
					
					log+="\n*  Nombre ["+mundoEsquemaTarifario.getNombre()+"] "+					
						  "\n*  Tarifa Oficial ["+mundoEsquemaTarifario.getTarifarioOficial().getNombre()+"] "+
						  "\n*  Metodo Ajuste ["+mundoEsquemaTarifario.getMetodoAjuste().getNombre()+"]";
					
					if(mundoEsquemaTarifario.getTarifarioOficial().getCodigo()==ConstantesBD.codigoTarifarioISS)
						log += "\n*  UVRs ["+mundoEsquemaTarifario.getCantidad()+"]";
					else
						log += "\n*  Salario Mínimo ["+mundoEsquemaTarifario.getCantidad()+"]";
					
					if(mundoEsquemaTarifario.getActivo())
						log += "\n*  Activo  [ SI ]";
					else
						log += "\n*  Activo [ NO ]";
					
				log+="\n========================================================\n\n\n " ;
				LogsAxioma.enviarLog(ConstantesBD.logEsquemasTarifariosCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
			
				this.cerrarConexion(con);
				request.setAttribute("codigoEsquemaTarifario",esquemasTarifariosForm.getCodigo()+"");
				return mapping.findForward("resumen");
			}
			else
			{
				logger.error("No se pudo modificar en EsquemaTarifario, return de insertar dió false. Usuario : " + usuario.getLoginUsuario());
				return this.enviarPaginaError(mapping, request, con, "errors.problemasBd", true);
			}
		}
		request.setAttribute("codigoEsquemaTarifario",esquemasTarifariosForm.getCodigo()+"");
		this.cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	
	/**
	 * Metodo que compara objetos del mundo y el form de los esquemas tarifarios
	 * @param mundoEsquemaTarifario
	 * @param esquemasTarifariosForm
	 * @return true o si fue exitosa la comparacion
	 */
	private boolean compararObjetos(EsquemaTarifario mundoEsquemaTarifario, EsquemasTarifariosForm esquemasTarifariosForm)
	{
		return (!mundoEsquemaTarifario.getNombre().equals(esquemasTarifariosForm.getNombre())||
				!mundoEsquemaTarifario.getMetodoAjuste().getAcronimo().equals(esquemasTarifariosForm.getMetodoAjuste()+"")||
				mundoEsquemaTarifario.isEsInventario()!=esquemasTarifariosForm.getEsInventario()||
				mundoEsquemaTarifario.getActivo()!=esquemasTarifariosForm.getActivo()||
				mundoEsquemaTarifario.getCantidad()!=esquemasTarifariosForm.getCantidad());
	}

	/**
	 * Metodo que genera la informacion original de los esquemas tarifarios, enviandola a los logs
	 * @param mundoEsquemaTarifario
	 * @return log
	 */
	private String generarCabeceraLog(EsquemaTarifario mundoEsquemaTarifario)
	{
		String log="\n            ====INFORMACION ORIGINAL===== " +
		"\n*  Código Esquema Tarifario [" +mundoEsquemaTarifario.getCodigo() +"] ";
		if(mundoEsquemaTarifario.isEsInventario())
			log += "\n*  Es Inventario [ SI ]" ;
		else
			log += "\n*  Es Inventario  [ NO ]";
		
		log+="\n*  Nombre ["+mundoEsquemaTarifario.getNombre()+"] "+
			  "\n*  Tarifa Oficial ["+mundoEsquemaTarifario.getTarifarioOficial().getNombre()+"] "+
			  "\n*  Metodo Ajuste ["+mundoEsquemaTarifario.getMetodoAjuste().getNombre()+"]";
		
		if(mundoEsquemaTarifario.getTarifarioOficial().getCodigo()==ConstantesBD.codigoTarifarioISS)
			log += "\n*  UVRs ["+mundoEsquemaTarifario.getCantidad()+"]";
		else
			log += "\n*  Salario Mínimo ["+mundoEsquemaTarifario.getCantidad()+"]";
		
		if(mundoEsquemaTarifario.getActivo())
			log += "\n*  Activo  [ SI ]";
		else
			log += "\n*  Activo [ NO ]";
		
		
		
		return log;
	}

	private ActionForward accionBuscarEsquemaTarifario(ActionMapping mapping, Connection con, EsquemasTarifariosForm esquemasTarifariosForm, int institucion) throws SQLException
	{
		EsquemaTarifario esquemaTarifario=new EsquemaTarifario();
		this.llenarObjetoConForm(esquemasTarifariosForm, esquemaTarifario);
		esquemasTarifariosForm.setResultadosBusqueda(esquemaTarifario.busqueda(con, institucion));
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");
	}
}
