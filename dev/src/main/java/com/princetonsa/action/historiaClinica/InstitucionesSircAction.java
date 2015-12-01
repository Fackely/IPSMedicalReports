/*
 * Nov 09, 2006
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.InstitucionesSircForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.InstitucionesSirc;
/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Innstituciones SIRC
 */
public class InstitucionesSircAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(InstitucionesSircAction.class);
	
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
			if(form instanceof InstitucionesSircForm)
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
				InstitucionesSircForm institucionesForm =(InstitucionesSircForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=institucionesForm.getEstado(); 
				logger.warn("estado InstitucionesSircAction-->"+estado);


				if(estado == null)
				{
					institucionesForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Instituciones SIRC (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//***********OPCION INGRESAR/MODIFICAR***************************************
				else if (estado.equals("empezar"))
				{
					//Se resetea forma
					institucionesForm.reset();
					return accionEmpezar(con,institucionesForm,mapping,usuario);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,institucionesForm,usuario,request,response);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,institucionesForm,mapping);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,institucionesForm,request,response);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,institucionesForm,mapping,usuario,request);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,institucionesForm,mapping,request,response);
				}
				//**********OPCION CONSULTAR**************************************************
				else if (estado.equals("consultar"))
				{
					//Se resetea forma
					institucionesForm.reset();
					return accionConsultar(con,institucionesForm,mapping,usuario);
				}
				else
				{
					institucionesForm.reset();
					logger.warn("Estado no valido dentro del flujo de InstitucionesSircAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	 * Método implementado para realizar la consulta de las instituciones SIRC
	 * @param con
	 * @param institucionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, InstitucionesSircForm institucionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto InstitucionesSirc
		InstitucionesSirc instituciones = new InstitucionesSirc();
		
		//se cargan objetos necesarios para la vista
		institucionesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		//Se cargan las instituciones SIRC existentes
		institucionesForm.setInstituciones(instituciones.cargarInstituciones(con,usuario.getCodigoInstitucionInt()));
		logger.warn("\n\n valor del mapa "+institucionesForm.getInstituciones()+"\n\n");
		institucionesForm.setNumRegistros(Integer.parseInt(institucionesForm.getInstituciones("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que pagina el listado de instituciones SRC
	 * @param con
	 * @param institucionesForm
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, InstitucionesSircForm institucionesForm, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(institucionesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de InstitucionesSircAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en InstitucionesSircAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que realiza la inserción/modificacion/eliminacion de registros del listado de instituciones SIRC
	 * @param con
	 * @param institucionesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, InstitucionesSircForm institucionesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//objeto de InstitucionesSirc
		InstitucionesSirc institucion = new InstitucionesSirc();
		InstitucionesSirc institucionAntigua = new InstitucionesSirc();
		//Variables auxiliares
		ActionErrors errores = new ActionErrors();
		int resp = 0;
		
		/*********************ELIMINACION****************************************************/
		for(int i=0;i<institucionesForm.getNumEliminados();i++)
		{
			institucion.clean();
			resp = 0;
			
			
			institucion.setCodigo(institucionesForm.getEliminados("codigo_"+i).toString());
			institucion.setInstitucion(Integer.parseInt(institucionesForm.getEliminados("institucion_"+i).toString()));
			institucion.setCampos("codigo",institucion.getCodigo());
			institucion.setCampos("institucion",institucion.getInstitucion()+"");
			institucion.cargarInstitucion(con);
			
			resp = institucion.eliminar(con,institucion.getCodigo(),institucion.getInstitucion());
			
			if(resp<=0)
				errores.add("error al eliminar",new ActionMessage("errors.noSeGraboInformacion","AL ELIMINAR LA INSTITUCION "+institucion.getCodigo()));
			else
			{
				this.generarLog(null,institucion,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			}
		}
		/***********************************************************************************/
		
		
		for(int i=0;i<institucionesForm.getNumRegistros();i++)
		{
			institucion.clean();
			resp = 0;
			
			/*********************MODIFICACION************************************************************/
			//Se verifica si el registro ya existía en la BD para entrar a la modificacion
			if(UtilidadTexto.getBoolean(institucionesForm.getInstituciones("existe_"+i).toString()))
			{
				this.llenarMundo(institucion,institucionesForm,i);
				//se carga la institución desde la BD
				institucionAntigua.clean();
				institucionAntigua.setCampos("codigo",institucion.getCodigo());
				institucionAntigua.setCampos("institucion",institucion.getInstitucion()+"");				
				institucionAntigua.cargarInstitucion(con);
				
				//Se verifica si fue modificado
				if(fueModificado(institucion,institucionAntigua))
				{
					resp = institucion.modificar(con);
					if(resp<=0)
						errores.add("error al modificar",new ActionMessage("errors.noSeGraboInformacion","AL MODIFICAR LA INSTITUCION "+institucion.getCodigo()));
					else
						this.generarLog(institucionAntigua,institucion,ConstantesBD.tipoRegistroLogModificacion,usuario);
				}
				
			}
			/***********************INSERCIÓN**********************************************************************/
			else
			{
				this.llenarMundo(institucion,institucionesForm,i);
				resp = institucion.insertar(con);
				if(resp<=0)
					errores.add("error al insertar",new ActionMessage("errors.noSeGraboInformacion","AL INSERTAR LA INSTITUCION "+institucion.getCodigo()));
			}
			
		}
		
		if(!errores.isEmpty())
		{
			saveErrors(request,errores);
			institucionesForm.setEstado("empezar");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		
		institucionesForm.setInstituciones(new HashMap());
		institucionesForm.setNumRegistros(0);
		institucionesForm.setEliminados(new HashMap());
		institucionesForm.setNumEliminados(0);
		return accionEmpezar(con,institucionesForm,mapping,usuario);
	}

	/**
	 * Método implementado para generar el log
	 * @param institucionAntigua
	 * @param institucion
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(InstitucionesSirc institucionAntigua, InstitucionesSirc institucion, int tipo, UsuarioBasico usuario) 
	{
String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL INSTITUCION SIRC===== " +
		    "\n*  Código [" +institucionAntigua.getCodigo()+"] "+
			"\n*  Descripción ["+institucionAntigua.getDescripcion()+"] " +
			"\n*  Nivel [" + institucionAntigua.getNivel().getNombre() +"]"+
		    "\n*  Red ["+institucionAntigua.getTipoRed()+"] "+
		    "\n*  Tipo Referencia ["+institucionAntigua.getTipoReferencia()+"] "+
		    "\n*  Tipo Ambulancia ["+institucionAntigua.getTipoAmbulancia()+"] "+
		    "\n*  Activo ["+institucionAntigua.getActivo()+"] ";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN INSTITUCION SIRC===== " +
		    "\n*  Código [" +institucion.getCodigo()+"] "+
			"\n*  Descripción ["+institucion.getDescripcion()+"] " +
			"\n*  Nivel [" + institucion.getNivel().getNombre() +"]"+
		    "\n*  Red ["+institucion.getTipoRed()+"] "+
		    "\n*  Tipo Referencia ["+institucion.getTipoReferencia()+"] "+
		    "\n*  Tipo Ambulancia ["+institucion.getTipoAmbulancia()+"] "+
		    "\n*  Activo ["+institucion.getActivo()+"] ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE INSTITUCION SIRC===== " +
		    "\n*  Código [" +institucion.getCodigo()+"] "+
			"\n*  Descripción ["+institucion.getDescripcion()+"] " +
			"\n*  Nivel [" + institucion.getNivel().getNombre() +"]"+
		    "\n*  Red ["+institucion.getTipoRed()+"] "+
		    "\n*  Tipo Referencia ["+institucion.getTipoReferencia()+"] "+
		    "\n*  Tipo Ambulancia ["+institucion.getTipoAmbulancia()+"] "+
		    "\n*  Activo ["+institucion.getActivo()+"] ";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logInstitucionesSircCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método que verifica si el registro fue modificado
	 * @param institucion
	 * @param institucionAntigua
	 */
	private boolean fueModificado(InstitucionesSirc institucion, InstitucionesSirc institucionAntigua) 
	{
		boolean fueModificado = false;
		
										
		if(!institucion.getDescripcion().equals(institucionAntigua.getDescripcion()))
			fueModificado = true;
				
		if(institucion.getNivel().getCodigo()!=institucionAntigua.getNivel().getCodigo())
			fueModificado = true;
		
		if(!institucion.getTipoRed().equals(institucionAntigua.getTipoRed()))
			fueModificado = true;
				
		if(!institucion.getTipoReferencia().equals(institucionAntigua.getTipoReferencia()))
			fueModificado = true;
				
		if(!institucion.getTipoAmbulancia().equals(institucionAntigua.getTipoAmbulancia()))
			fueModificado = true;
						
		if(!institucion.isActivo().equals(institucionAntigua.isActivo()))
			fueModificado = true;	
				
		return fueModificado;
	}

	/**
	 * Metodo implementado para llenar el mundo institucionesSirc con los datos de la forma
	 * @param institucion
	 * @param institucionesForm
	 * @param pos
	 */
	private void llenarMundo(InstitucionesSirc institucion, InstitucionesSircForm institucionesForm, int pos) 
	{		
		institucion.setCodigo(institucionesForm.getInstituciones("codigo_"+pos).toString());
		institucion.setDescripcion(institucionesForm.getInstituciones("descripcion_"+pos).toString());
		institucion.setInstitucion(Integer.parseInt(institucionesForm.getInstituciones("institucion_"+pos).toString()));
		
		InfoDatosInt nivel = new InfoDatosInt(Integer.parseInt(institucionesForm.getInstituciones("codigo_nivel_"+pos).toString()),institucionesForm.getInstituciones("nombre_nivel_"+pos).toString());
		institucion.setNivel(nivel);
		
		
		institucion.setTipoRed(institucionesForm.getInstituciones("codigo_tipo_red_"+pos).toString());		
		institucion.setTipoReferencia(institucionesForm.getInstituciones("codigo_tipo_refe_"+pos).toString());
		institucion.setTipoAmbulancia(institucionesForm.getInstituciones("codigo_tipo_ambu_"+pos).toString());		
		institucion.setActivo(institucionesForm.getInstituciones("activo_"+pos).toString());
		
		institucion.setCampos("codigo",institucion.getCodigo());
		institucion.setCampos("descripcion",institucion.getDescripcion());
		institucion.setCampos("institucion",institucion.getInstitucion()+"");
		institucion.setCampos("nivel",institucion.getNivel().getCodigo()+"");
		institucion.setCampos("red",institucion.getTipoRed()+"");
		institucion.setCampos("referencia",institucion.getTipoReferencia()+"");
		institucion.setCampos("ambulancia",institucion.getTipoAmbulancia()+"");
		institucion.setCampos("activo",institucion.isActivo()+"");				
	}

	/**
	 * Método que postula la eliminacion de un registro del listado de instituciones
	 * @param con
	 * @param institucionesForm
	 * @param response 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, InstitucionesSircForm institucionesForm, HttpServletRequest request, HttpServletResponse response) 
	{
		int pos = institucionesForm.getPosicion();
		
		if(UtilidadTexto.getBoolean(institucionesForm.getInstituciones("existe_"+pos).toString()))
		{
			int posE = institucionesForm.getNumEliminados();
			//Si existe en la BD se debe almacenar en el mapa de eliminados			
			institucionesForm.setEliminados("codigo_"+posE,institucionesForm.getInstituciones("codigo_"+pos));
			institucionesForm.setEliminados("institucion_"+posE,institucionesForm.getInstituciones("institucion_"+pos));
			posE++;
			institucionesForm.setEliminados("numRegistros",posE+"");
			institucionesForm.setNumEliminados(posE);
		}
		
		for(int i=pos;i<(institucionesForm.getNumRegistros()-1);i++)
		{
			institucionesForm.setInstituciones("codigo_"+i,institucionesForm.getInstituciones("codigo_"+(i+1)));
			institucionesForm.setInstituciones("institucion_"+i,institucionesForm.getInstituciones("institucion_"+(i+1)));
			institucionesForm.setInstituciones("descripcion_"+i,institucionesForm.getInstituciones("descripcion_"+(i+1)));
			institucionesForm.setInstituciones("codigo_nivel_"+i,institucionesForm.getInstituciones("codigo_nivel_"+(i+1)));
			institucionesForm.setInstituciones("nombre_nivel_"+i,institucionesForm.getInstituciones("nombre_nivel_"+(i+1)));
			institucionesForm.setInstituciones("codigo_tipo_red_"+i,institucionesForm.getInstituciones("codigo_tipo_red_"+(i+1)));			
			institucionesForm.setInstituciones("codigo_tipo_refe_"+i,institucionesForm.getInstituciones("codigo_tipo_refe_"+(i+1)));
			institucionesForm.setInstituciones("codigo_tipo_ambu_"+i,institucionesForm.getInstituciones("codigo_tipo_ambu_"+(i+1)));
			institucionesForm.setInstituciones("activo_"+i,institucionesForm.getInstituciones("activo_"+(i+1)));
			institucionesForm.setInstituciones("existe_"+i,institucionesForm.getInstituciones("existe_"+(i+1)));
			institucionesForm.setInstituciones("es_usado_"+i,institucionesForm.getInstituciones("es_usado_"+(i+1)));
		}
		
		int contador = institucionesForm.getNumRegistros();
		contador--;
		
		institucionesForm.getInstituciones().remove("codigo_"+contador);
		institucionesForm.getInstituciones().remove("institucion_"+contador);
		institucionesForm.getInstituciones().remove("descripcion_"+contador);
		institucionesForm.getInstituciones().remove("codigo_nivel_"+contador);
		institucionesForm.getInstituciones().remove("nombre_nivel_"+contador);
		institucionesForm.getInstituciones().remove("codigo_tipo_red_"+contador);		
		institucionesForm.getInstituciones().remove("codigo_tipo_refe_"+contador);
		institucionesForm.getInstituciones().remove("codigo_tipo_ambu_"+contador);
		institucionesForm.getInstituciones().remove("activo_"+contador);
		institucionesForm.getInstituciones().remove("existe_"+contador);
		institucionesForm.getInstituciones().remove("es_usado_"+contador);
		
		institucionesForm.setInstituciones("numRegistros",contador+"");
		institucionesForm.setNumRegistros(contador);
		
		//***SE valida que en la posición del pager hayan registros****
		if(institucionesForm.getOffset()>=contador)
		{
			int maxPageItems=institucionesForm.getMaxPageItems();
			int offset=contador-maxPageItems;
			if(offset<0)
				offset=0;
			institucionesForm.setOffset(offset);
		}
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(institucionesForm.getLinkSiguiente(),institucionesForm.getMaxPageItems(),Integer.parseInt(institucionesForm.getInstituciones("numRegistros").toString()), response, request, "ingresarInstitucionesSirc.jsp",true);
		//return mapping.findForward("principal");
	}

	/**
	 * Método implementado para ordenar el listado de instituciones 
	 * @param con
	 * @param institucionesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, InstitucionesSircForm institucionesForm, ActionMapping mapping) 
	{
		String[] indices={				
				"codigo_",
				"institucion_",
				"descripcion_",				
				"codigo_nivel_",
				"nombre_nivel_",
				"codigo_tipo_red_",
				"codigo_tipo_refe_",
				"codigo_tipo_ambu_",
				"activo_",
				"existe_",
				"es_usado_"
			};
		
		
		
		institucionesForm.setInstituciones(Listado.ordenarMapa(indices,
				institucionesForm.getIndice(),
				institucionesForm.getUltimoIndice(),
				institucionesForm.getInstituciones(),
				institucionesForm.getNumRegistros()));
		
		institucionesForm.setInstituciones("numRegistros",institucionesForm.getNumRegistros()+"");
		institucionesForm.setUltimoIndice(institucionesForm.getIndice());
		
		institucionesForm.setEstado("empezar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para postular un nuevo registro de instituciones SIRC
	 * @param con
	 * @param institucionesForm
	 * @param usuario
	 * @param response 
	 * @param request 
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, InstitucionesSircForm institucionesForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		int pos = institucionesForm.getNumRegistros();
		
		institucionesForm.setInstituciones("codigo_"+pos,"");
		institucionesForm.setInstituciones("institucion_"+pos,usuario.getCodigoInstitucion());
		institucionesForm.setInstituciones("descripcion_"+pos,"");
		institucionesForm.setInstituciones("codigo_nivel_"+pos,"0");
		institucionesForm.setInstituciones("nombre_nivel_"+pos,"");
		institucionesForm.setInstituciones("codigo_tipo_red_"+pos,ConstantesIntegridadDominio.acronimoRA);
		institucionesForm.setInstituciones("codigo_tipo_refe_"+pos,ConstantesBD.acronimoNo);
		institucionesForm.setInstituciones("codigo_tipo_ambu_"+pos,ConstantesBD.acronimoNo);
		institucionesForm.setInstituciones("activo_"+pos,"S");
		institucionesForm.setInstituciones("existe_"+pos,"false");
		institucionesForm.setInstituciones("eliminado_"+pos,"false");
		institucionesForm.setInstituciones("es_usado_"+pos,"false");
		
		pos++;
		institucionesForm.setInstituciones("numRegistros",pos+"");
		institucionesForm.setNumRegistros(pos);
		
		if(pos>(institucionesForm.getOffset()+institucionesForm.getMaxPageItems()))
        {
        	int offset = pos- institucionesForm.getMaxPageItems();
        	while(offset%institucionesForm.getMaxPageItems()!=0)
        		offset++;
	       institucionesForm.setOffset(offset);
        }
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(institucionesForm.getLinkSiguiente(),institucionesForm.getMaxPageItems(),Integer.parseInt(institucionesForm.getInstituciones("numRegistros").toString()), response, request, "ingresarInstitucionesSirc.jsp",true);
		
		/**if(pos>(institucionesForm.getOffset()+institucionesForm.getMaxPageItems()))
        {
        	int offset = pos- institucionesForm.getMaxPageItems();
        	while(offset%institucionesForm.getMaxPageItems()!=0)
        		offset++;
	       institucionesForm.setOffset(offset);
        }
	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");**/
	}

	/**
	 * Método que inicia el flujo de ingresar/modificar instituciones SIRC
	 * @param con
	 * @param institucionesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, InstitucionesSircForm institucionesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto InstitucionesSirc
		InstitucionesSirc instituciones = new InstitucionesSirc();
		
		//se cargan objetos necesarios para la vista
		institucionesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		institucionesForm.setNiveles(instituciones.cargarNivelesServicio(con,usuario.getCodigoInstitucionInt()));
		institucionesForm.setNumNiveles(Integer.parseInt(institucionesForm.getNiveles("numRegistros").toString()));		
		//institucionesForm.setNumTiposRed(Integer.parseInt(institucionesForm.getTiposRed("numRegistros").toString()));
		
		//Se cargan las instituciones SIRC existentes
		institucionesForm.setInstituciones(instituciones.cargarInstituciones(con,usuario.getCodigoInstitucionInt()));		
		institucionesForm.setNumRegistros(Integer.parseInt(institucionesForm.getInstituciones("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
