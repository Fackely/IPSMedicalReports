/*
 * Mayo 31, 2006
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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.historiaClinica.DestinoTriageForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.DestinoTriage;
/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Destinos Triage
 */
public class DestinoTriageAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(DestinoTriageAction.class);
	
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
			if(form instanceof DestinoTriageForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				//OBJETOS A USAR
				DestinoTriageForm destinoForm =(DestinoTriageForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=destinoForm.getEstado(); 
				logger.warn("estado DestinoTriageAction-->"+estado);


				if(estado == null)
				{
					destinoForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Destino Triage (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					//Se resetea forma
					destinoForm.reset();
					return accionEmpezar(con,destinoForm,mapping,usuario);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,destinoForm,mapping,usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,destinoForm,mapping);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,destinoForm,mapping,usuario,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,destinoForm,mapping,usuario,request);
				}
				else
				{
					destinoForm.reset();
					logger.warn("Estado no valido dentro del flujo de DestinoTriageAction (null) ");
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
	 * Método implementado para guardar los cambios realizados en el listado
	 * de destinos triage (insertando nuevos o modificando)
	 * @param con
	 * @param destinoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, DestinoTriageForm destinoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables que se van a utilizar
		boolean esBD = false;
		ActionErrors errores = new ActionErrors();
		//se instancia objeto DestinoTriage
		DestinoTriage destino = new DestinoTriage();
		
		
		for(int i=0;i<destinoForm.getNumRegistros();i++)
		{
			esBD = UtilidadTexto.getBoolean(destinoForm.getDestinosMap("es_bd_"+i).toString());
			
			destino.setInstitucion(destinoForm.getDestinosMap("institucion_"+i).toString());
			destino.setCodigo(destinoForm.getDestinosMap("codigo_"+i).toString());
			this.cargarMundo(destino,destinoForm.getDestinosMap(),i);
			
			
			//se verifica si el registro ya existía en la BD
			if(esBD)
			{
				//se instancia objeto para datos historicos
				//Se carga destino antiguo *********************************
				DestinoTriage destinoAntiguo = new DestinoTriage();
				destinoAntiguo.setInstitucion(destinoForm.getDestinosMap("institucion_"+i).toString());
				destinoAntiguo.setCodigo(destinoForm.getDestinosMap("codigo_"+i).toString());
				HashMap registro = destinoAntiguo.cargar(con);
				this.cargarMundo(destinoAntiguo,registro,0);
				//************************************************************
				
				//Se verifica si registro fue modificado******************
				boolean modificado = this.fueModificado(destinoAntiguo,destino);
				//*******************************************************
				
				if(modificado)
				{
					if(destino.modificar(con)>0)
					{
						this.generarLog(destinoAntiguo,destino,ConstantesBD.tipoRegistroLogModificacion,usuario);
					}
					else
					{
						errores.add("Error al modificar", new ActionMessage("errors.noSeGraboInformacion",
								"DEL REGISTRO CON CÓDIGO "+destino.getCodigo()));
					}
				}
			}
			else
			{
				//Como no hace parte de la BD es registro nuevo y se debe insertar
				if(destino.insertar(con)<=0)
				{
					errores.add("Error al insertar",
						new ActionMessage("errors.noSeGraboInformacion",
							"DEL REGISTRO CON CÓDIGO "+destino.getCodigo()));
				}
					
			}
				
		}
		
		//Se verifica si hubo errores
		if(!errores.isEmpty())
		{
			saveErrors(request,errores);
			destinoForm.setEstado("empezar");
		}
		
		//Se redirecciona al principio
		return accionEmpezar(con,destinoForm,mapping,usuario);
	}

	/**
	 * Método implementado para verificar si el destino fue modificado
	 * @param destinoAntiguo
	 * @param destino
	 * @return
	 */
	private boolean fueModificado(DestinoTriage destinoAntiguo, DestinoTriage destino) 
	{
		boolean fueModificado = false;
		
		if(!destinoAntiguo.getNombre().equals(destino.getNombre()))
			fueModificado = true;
		if(destinoAntiguo.isIndicadorAdminUrgencia()!=destino.isIndicadorAdminUrgencia())
			fueModificado = true;
		
		return fueModificado;
	}

	/**
	 * Método implementado para eliminar un registro destino triage
	 * @param con
	 * @param destinoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, DestinoTriageForm destinoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		int pos = destinoForm.getPosicion();
		boolean esBD = UtilidadTexto.getBoolean(destinoForm.getDestinosMap("es_bd_"+pos).toString());
		boolean exito = true;
		
		//Si el registro existe en la base de datos, se debe eliminar
		if(esBD)
		{
			//Se instancia objeto de DestinoTriage
			DestinoTriage destino = new DestinoTriage();
			this.cargarMundo(destino,destinoForm.getDestinosMap(),pos);
			
			if(destino.eliminar(con)>0)
			{
				//generar el log
				this.generarLog(null,destino,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			}
			else
			{
				exito = false;
				ActionErrors errores = new ActionErrors();
				errores.add("Error al eliminar registro",new ActionMessage("errors.sinEliminar"));
				saveErrors(request,errores);
			}
		}
		
		//se elimina registro del mapa
		if(exito)
		{
			for(int i=pos;i<(destinoForm.getNumRegistros()-1);i++)
			{
				destinoForm.setDestinosMap("codigo_"+i,destinoForm.getDestinosMap("codigo_"+(i+1)));
				destinoForm.setDestinosMap("nombre_"+i,destinoForm.getDestinosMap("nombre_"+(i+1)));
				destinoForm.setDestinosMap("indicador_admi_urg_"+i,destinoForm.getDestinosMap("indicador_admi_urg_"+(i+1)));
				destinoForm.setDestinosMap("institucion_"+i,destinoForm.getDestinosMap("institucion_"+(i+1)));
				destinoForm.setDestinosMap("es_bd_"+i,destinoForm.getDestinosMap("es_bd_"+(i+1)));
				destinoForm.setDestinosMap("es_usado_"+i,destinoForm.getDestinosMap("es_usado_"+(i+1)));
			}
			pos = destinoForm.getNumRegistros();
			destinoForm.getDestinosMap().remove("codigo_"+pos);
			destinoForm.getDestinosMap().remove("nombre_"+pos);
			destinoForm.getDestinosMap().remove("indicador_admi_urg_"+pos);
			destinoForm.getDestinosMap().remove("institucion_"+pos);
			destinoForm.getDestinosMap().remove("es_bd_"+pos);
			destinoForm.getDestinosMap().remove("es_usado_"+pos);
			pos--;
			destinoForm.setNumRegistros(pos);
			destinoForm.setDestinosMap("numRegistros",pos+"");
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para generar LOG
	 * @param destinoAntiguo
	 * @param destino
	 * @param tipoRegistroLogEliminacion
	 * @param usuario
	 */
	private void generarLog(DestinoTriage destinoAntiguo, DestinoTriage destino, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL DESTINO TRIAGE===== " +
		    "\n*  Código [" +destinoAntiguo.getCodigo()+"] "+
			"\n*  Destino ["+destinoAntiguo.getNombre()+"] " +
			"\n*  Indicativo Admisión Urgencias [" + (destinoAntiguo.isIndicadorAdminUrgencia()?"Activo":"Inactivo") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DESTINO TRIAGE===== " +
		    "\n*  Código [" +destino.getCodigo()+"] "+
			"\n*  Destino ["+destino.getNombre()+"] " +
			"\n*  Indicativo Admisión Urgencias [" + (destino.isIndicadorAdminUrgencia()?"Activo":"Inactivo") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE DESTINO TRIAGE===== " +
			"\n*  Código [" +destino.getCodigo()+"] "+
			"\n*  Destino ["+destino.getNombre()+"] " +
			"\n*  Indicativo Admisión Urgencias [" + (destino.isIndicadorAdminUrgencia()?"Activo":"Inactivo") +"]"+
		    "\n*  Institucion ["+usuario.getInstitucion()+"] ";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logDestinoTriageCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método implemntado para cargar los datos de un registro en el
	 * objeto destino Triage
	 * @param destino
	 * @param destinosMap
	 * @param pos
	 */
	private void cargarMundo(DestinoTriage destino, HashMap destinosMap, int pos) 
	{
		destino.setCodigo(destinosMap.get("codigo_"+pos).toString());
		destino.setNombre(destinosMap.get("nombre_"+pos).toString());
		destino.setIndicadorAdminUrgencia(UtilidadTexto.getBoolean(destinosMap.get("indicador_admi_urg_"+pos).toString()));
		destino.setInstitucion(destinosMap.get("institucion_"+pos).toString());
	}

	/**
	 * Método implementado para ordenar el listado de Destinos Triage
	 * @param con
	 * @param destinoForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, DestinoTriageForm destinoForm, ActionMapping mapping) 
	{
		String[] indices={
				"codigo_",
				"nombre_",
				"indicador_admi_urg_",
				"institucion_",
				"es_bd_",
				"es_usado_"
			};
		
		
		
		destinoForm.setDestinosMap(Listado.ordenarMapa(indices,
				destinoForm.getIndice(),
				destinoForm.getUltimoIndice(),
				destinoForm.getDestinosMap(),
				destinoForm.getNumRegistros()));
		
		destinoForm.setDestinosMap("numRegistros",destinoForm.getNumRegistros()+"");
		destinoForm.setUltimoIndice(destinoForm.getIndice());
		
		destinoForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para postular un nuevo registro de destino Triage
	 * 
	 * @param con
	 * @param destinoForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, DestinoTriageForm destinoForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = destinoForm.getNumRegistros();
		
		destinoForm.setDestinosMap("codigo_"+pos,"");
		destinoForm.setDestinosMap("nombre_"+pos,"");
		destinoForm.setDestinosMap("indicador_admi_urg_"+pos,"false");
		destinoForm.setDestinosMap("institucion_"+pos,usuario.getCodigoInstitucion());
		destinoForm.setDestinosMap("es_bd_"+pos,"false");
		destinoForm.setDestinosMap("es_usado_"+pos,"false");
		
		pos++;
		destinoForm.setDestinosMap("numRegistros",pos+"");
		destinoForm.setNumRegistros(pos);
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para cargar los destinos triage
	 * actualmente parametrizados y postular el inicio de la
	 * funcionalidad
	 * @param con
	 * @param destinoForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, DestinoTriageForm destinoForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		
		
		//Se instancia objeto DestinoTRiage
		DestinoTriage destino = new DestinoTriage();
		destino.setInstitucion(usuario.getCodigoInstitucion());
		//no se buscará por un destino específico sino todos los de la institución
		destino.setCodigo("");
		//se consultan los destinos actuales
		destinoForm.setDestinosMap(destino.cargar(con));
		destinoForm.setNumRegistros(Integer.parseInt(destinoForm.getDestinosMap("numRegistros").toString()));
		
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
