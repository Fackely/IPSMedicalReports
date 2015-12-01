/*
 * Ago 08, 2006
 */
package com.princetonsa.action.pyp;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pyp.ProgramasActividadesConvenioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ProgramasActividadesConvenio;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Programas y Actividades por Convenio
 */
public class ProgramasActividadesConvenioAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ProgramasActividadesConvenioAction.class);
	
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
			if(form instanceof ProgramasActividadesConvenioForm)
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
				ProgramasActividadesConvenioForm programasForm =(ProgramasActividadesConvenioForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=programasForm.getEstado(); 
				logger.warn("estado ProgramasActividadesConvenioAction-->"+estado);


				if(estado == null)
				{
					programasForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Programas y Actividades por Convenio PYP (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,programasForm,mapping,usuario);
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con,programasForm,mapping,usuario);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,programasForm,mapping,usuario);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,programasForm,mapping,usuario,request);
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con,programasForm,mapping,usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,programasForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,programasForm,response,mapping,request);
				}
				else if (estado.equals("busqueda"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busquedaActividad");
				}
				else
				{
					programasForm.reset();
					logger.warn("Estado no valido dentro del flujo de ProgramasActividadesConvenioAction (null) ");
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
	 * Método implementado para paginar el listado de actividades de un convenio
	 * y programa específicos
	 * @param con
	 * @param programasForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ProgramasActividadesConvenioForm programasForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(programasForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ProgramasActividadesConvenioAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ProgramasActividadesConvenioAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para ordenar el listado de actividades de un programa y un convenio específicos
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"consecutivo_",
				"codigo_convenio_",
				"nombre_convenio_",
				"codigo_app_",
				"codigo_programa_",
				"nombre_programa_",
				"codigo_actividad_",
				"descripcion_actividad_",
				"activo_",
				"institucion_",
				"es_usado_"
			};

		programasForm.setListado(Listado.ordenarMapa(indices,
				programasForm.getIndice(),
				programasForm.getUltimoIndice(),
				programasForm.getListado(),
				programasForm.getNumRegistros()));
		
		programasForm.setListado("numRegistros",programasForm.getNumRegistros());
		
		programasForm.setUltimoIndice(programasForm.getIndice());
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para eliminar un registro del mapa Listado (Actividades de un programa y convenio específico) 
	 * y si existe en la BD se borra el registro de la tabla
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de ProgramasActividadesConvenio
		ProgramasActividadesConvenio programas = new ProgramasActividadesConvenio();
		
		//se toma posición del registro a eliminar
		int pos = programasForm.getPosicion();
		int resp = 1;
		String auxS0 = programasForm.getListado("consecutivo_"+pos).toString();
		
		
		
		//se verifica si el registro existe en la BD
		if(!auxS0.equals(""))
		{
			//Se consulta registro dela BD
			programas.clean();
			programas.setCampos("consecutivo",auxS0);
			HashMap registroDB = programas.consultar(con);
			//se llenan datos faltantes	
			programasForm.setListado("nombre_convenio_"+pos,registroDB.get("nombre_convenio_0"));
			programasForm.setListado("nombre_programa_"+pos,registroDB.get("nombre_programa_0"));
			
			//El registro existe por tal motivo se debe eliminar
			this.llenarMundo(programas,programasForm,pos);
			resp = programas.eliminar(con);
			if(resp>0)
				this.generarLog(null,programasForm,pos,ConstantesBD.tipoRegistroLogEliminacion,usuario);
		}
		
		//si el proceso es exitoso entonces se elimina del mapa y se envía el LOG
		if(resp>0)
		{
			//Se borra el registro del mapa
			for(int i=pos;i<(programasForm.getNumRegistros()-1);i++)
			{
				programasForm.setListado("consecutivo_"+i,programasForm.getListado("consecutivo_"+(i+1)));
				programasForm.setListado("codigo_convenio_"+i,programasForm.getListado("codigo_convenio_"+(i+1)));
				programasForm.setListado("nombre_convenio_"+i,programasForm.getListado("nombre_convenio_"+(i+1)));
				programasForm.setListado("codigo_app_"+i,programasForm.getListado("codigo_app_"+(i+1)));
				programasForm.setListado("codigo_programa_"+i,programasForm.getListado("codigo_programa_"+(i+1)));
				programasForm.setListado("nombre_programa_"+i,programasForm.getListado("nombre_programa_"+(i+1)));
				programasForm.setListado("codigo_actividad_"+i,programasForm.getListado("codigo_actividad_"+(i+1)));
				programasForm.setListado("descripcion_actividad_"+i,programasForm.getListado("descripcion_actividad_"+(i+1)));
				programasForm.setListado("activo_"+i,programasForm.getListado("activo_"+(i+1)));
				programasForm.setListado("institucion_"+i,programasForm.getListado("institucion_"+(i+1)));
				programasForm.setListado("es_usado_"+i,programasForm.getListado("es_usado_"+(i+1)));
				
			}
			
			//se elimina ultima posicion
			pos = programasForm.getNumRegistros();
			pos--;
			
			programasForm.getListado().remove("consecutivo_"+pos);
			programasForm.getListado().remove("codigo_convenio_"+pos);
			programasForm.getListado().remove("nombre_convenio_"+pos);
			programasForm.getListado().remove("codigo_app_"+pos);
			programasForm.getListado().remove("codigo_programa_"+pos);
			programasForm.getListado().remove("nombre_programa_"+pos);
			programasForm.getListado().remove("codigo_actividad_"+pos);
			programasForm.getListado().remove("descripcion_actividad_"+pos);
			programasForm.getListado().remove("activo_"+pos);
			programasForm.getListado().remove("institucion_"+pos);
			programasForm.getListado().remove("es_usado_"+pos);
			
			
			
			programasForm.setNumRegistros(pos);
			programasForm.setListado("numRegistros",pos);
			programasForm.setEstado("guardar");
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Error al eliminar",new ActionMessage("error.pyp.actividadesPYP.error","al eliminar",programasForm.getListado("descripcion_actividad_"+pos)));
			programasForm.setEstado("parametrizar");
		}
		
		//se verifica el offset
		if(programasForm.getNumRegistros()==programasForm.getOffset()&&programasForm.getOffset()>0)
		{
			int offset = programasForm.getOffset();
			offset = offset - programasForm.getMaxPageItems();
			programasForm.setOffset(offset);
		}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para guardar un nuevos registros o modificar los existentes 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		////variables auxiliares
		String auxS0 = "";
		int resp = 0;
		ActionErrors errores = new ActionErrors();
		
		
		//Se instancia objeto de ProgramasActividadesConvenio
		ProgramasActividadesConvenio programas = new ProgramasActividadesConvenio();
		
		for(int i=0;i<programasForm.getNumRegistros();i++)
		{
			auxS0 = programasForm.getListado("consecutivo_"+i).toString();
			
			//Se verifica estado del registro
			if(auxS0.equals(""))
			{
				//******INSERCIÓN DE REGISTRO *********************************
				this.llenarMundo(programas,programasForm,i);
				resp = programas.insertar(con);
				if(resp<=0)
					errores.add("Error insertando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al ingresar",programasForm.getListado("descripcion_actividad_"+i)));
				else
					programasForm.setListado("consecutivo_"+i,resp);
				//*************************************************************
			}
			else
			{
				//*****MODIFICACION DE REGISTRO******************************
				//Se consulta registro dela BD
				programas.clean();
				programas.setCampos("consecutivo",programasForm.getListado("consecutivo_"+i));
				HashMap registroDB = programas.consultar(con);
				
				
				this.llenarMundo(programas,programasForm,i);
				
				//Se verifica si el registro fue modificado
				if(this.fueModificado(registroDB,programasForm,i))
				{
					//se llenan datos faltantes
					programasForm.setListado("nombre_convenio_"+i,registroDB.get("nombre_convenio_0"));
					programasForm.setListado("nombre_programa_"+i,registroDB.get("nombre_programa_0"));
					
					resp = programas.modificar(con);
					if(resp<=0)
						errores.add("Error modificando actividad",new ActionMessage("error.pyp.actividadesPYP.error","al modificar",programasForm.getListado("descripcion_actividad_"+i)));
					else
						this.generarLog(registroDB,programasForm,i,ConstantesBD.tipoRegistroLogModificacion,usuario);
				}
				//***********************************************************
			}
		}
		
		if(errores.isEmpty())
		{
			programasForm.setEstado("guardar");
		}
		else
		{
			saveErrors(request,errores);
			programasForm.setEstado("parametrizar");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para generar LOG Axioma 
	 * @param registroDB
	 * @param programasForm
	 * @param pos
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(HashMap registroDB, ProgramasActividadesConvenioForm programasForm, int pos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL PROGRAMAS Y ACTIVIDADES POR CONVENIO===== " +
		    "\n*  Consecutivo [" +registroDB.get("consecutivo_0")+"] "+
		    "\n*  Convenio [" +registroDB.get("nombre_convenio_0")+"] "+
			"\n*  Programa ["+registroDB.get("nombre_programa_0")+"] " +
			"\n*  Actividad ["+registroDB.get("descripcion_actividad_0")+"] " +
			"\n*  Activo [" + (UtilidadTexto.getBoolean(registroDB.get("activo_0").toString())?"Sí":"No") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN PROGRAMAS Y ACTIVIDADES POR CONVENIO===== " +
		    "\n*  Consecutivo [" +programasForm.getListado("consecutivo_"+pos)+"] "+
		    "\n*  Convenio [" +programasForm.getListado("nombre_convenio_"+pos)+"] "+
			"\n*  Programa ["+programasForm.getListado("nombre_programa_"+pos)+"] " +
			"\n*  Actividad ["+programasForm.getListado("descripcion_actividad_"+pos)+"] " +
			"\n*  Activo [" + (UtilidadTexto.getBoolean(programasForm.getListado("activo_"+pos).toString())?"Sí":"No") +"]"+
			"\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE PROGRAMAS Y ACTIVIDADES POR CONVENIO===== " +
		    "\n*  Consecutivo [" +programasForm.getListado("consecutivo_"+pos)+"] "+
		    "\n*  Convenio [" +programasForm.getListado("nombre_convenio_"+pos)+"] "+
			"\n*  Programa ["+programasForm.getListado("nombre_programa_"+pos)+"] " +
			"\n*  Actividad ["+programasForm.getListado("descripcion_actividad_"+pos)+"] " +
			"\n*  Activo [" + (UtilidadTexto.getBoolean(programasForm.getListado("activo_"+pos).toString())?"Sí":"No") +"]"+
			"\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logProgramasActividadesConvenioCodigo, log, tipo,usuario.getLoginUsuario());
		
	}


	/**
	 * Método que verifica si un registro fue modificado comparándolo con la base de datos
	 * @param registroDB
	 * @param programasForm
	 * @param pos
	 * @return
	 */
	private boolean fueModificado(HashMap registroDB, ProgramasActividadesConvenioForm programasForm, int pos) 
	{
		boolean fueModificado = false;
		
		if(UtilidadTexto.getBoolean(programasForm.getListado("activo_"+pos).toString())!=UtilidadTexto.getBoolean(registroDB.get("activo_0").toString()))
			fueModificado = true;
		
		return fueModificado;
	}


	/**
	 * Método que carga el mundo con los datos de la forma
	 * @param programas
	 * @param programasForm
	 * @param pos
	 */
	private void llenarMundo(ProgramasActividadesConvenio programas, ProgramasActividadesConvenioForm programasForm, int pos) 
	{
		programas.clean();
		programas.setCampos("consecutivo",programasForm.getListado("consecutivo_"+pos));
		programas.setCampos("convenio",programasForm.getListado("codigo_convenio_"+pos));
		programas.setCampos("programa",programasForm.getListado("codigo_programa_"+pos));
		programas.setCampos("institucion",programasForm.getListado("institucion_"+pos));
		programas.setCampos("codigoProgramaActividad",programasForm.getListado("codigo_app_"+pos));
		programas.setCampos("activo",programasForm.getListado("activo_"+pos));
		
	}


	/**
	 * Método implementado para postular un nuevo registro 
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = programasForm.getNumRegistros();
		programasForm.setListado("consecutivo_"+pos,"");
		programasForm.setListado("codigo_convenio_"+pos,programasForm.getConvenio());
		programasForm.setListado("nombre_convenio_"+pos,"");
		programasForm.setListado("codigo_app_"+pos,"");
		programasForm.setListado("codigo_programa_"+pos,programasForm.getPrograma());
		programasForm.setListado("nombre_programa_"+pos,"");
		programasForm.setListado("codigo_actividad_"+pos,"");
		programasForm.setListado("descripcion_actividad_"+pos,"");
		programasForm.setListado("activo_"+pos,"true");
		programasForm.setListado("institucion_"+pos,programasForm.getInstitucion());
		programasForm.setListado("es_usado_"+pos,"false");
		
		pos++;
		programasForm.setListado("numRegistros",pos+"");
		programasForm.setNumRegistros(pos);
		
		//se ubica el pager en el último registro
		if(programasForm.getNumRegistros()>programasForm.getMaxPageItems())
		{
			int numReg = programasForm.getNumRegistros();
			int maxPag = programasForm.getMaxPageItems();
			programasForm.setOffset(numReg-(numReg%maxPag==0?maxPag:numReg%maxPag));
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para consultar las actividades y programas antes parametrizados y
	 * brindar la posibilidad de ingresar un nuevo registro
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionParametrizar(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//incializa atributos del pager
		programasForm.reset_pager();
		
		//Instanciar objeto de ProgramasActividadesConvenio
		ProgramasActividadesConvenio programas = new ProgramasActividadesConvenio();
		
		//Se consultan los registros existentes
		programas.setCampos("convenio",programasForm.getConvenio());
		programas.setCampos("programa",programasForm.getPrograma());
		programas.setCampos("institucion",programasForm.getInstitucion());
		programasForm.setListado(programas.consultar(con));
		programasForm.setNumRegistros(Integer.parseInt(programasForm.getListado("numRegistros").toString()));
		
		//Se consultan las actividades del programa
		programasForm.setActividades(programas.cargarActividadesPrograma(con));
		
		//Se asigna el maxPageItems
		programasForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para postular el inicio de la funcionalidad de programas y actividades por convenio
	 * @param con
	 * @param programasForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ProgramasActividadesConvenioForm programasForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se resetea forma
		programasForm.reset();
		programasForm.setEstado("empezar");
		int institucion=usuario.getCodigoInstitucionInt();
		programasForm.setInstitucion(usuario.getCodigoInstitucion());
		programasForm.setListadoProgramas(Utilidades.listarProgramasPyP(true, institucion));
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
