/*
 * Created on Sep 1, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.TipoSalasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.TipoSalas;

/**
 * @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Tipos de Salas 
 */
public class TipoSalasAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(TipoSalasAction.class);
	
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
			if(form instanceof TipoSalasForm)
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
				TipoSalasForm tipoSalasForm =(TipoSalasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=tipoSalasForm.getEstado(); 
				logger.warn("[TipoSalasAction] estado->"+estado);


				if(estado == null)
				{
					tipoSalasForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Tipos de Salas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,tipoSalasForm,mapping,usuario,request);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,tipoSalasForm,mapping,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,tipoSalasForm,mapping,usuario,request);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,tipoSalasForm,mapping,usuario,request);
				}
				else
				{
					tipoSalasForm.reset();
					logger.warn("Estado no valido dentro del flujo de tipos de salas (null) ");
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
	 * Método usado para eliminar un registro del listado de tipos de salas
	 * @param con
	 * @param tipoSalasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, TipoSalasForm tipoSalasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//nuevo registro que se eliminará
		HashMap registroEliminacion=new HashMap();	
		int resp=0; //variable para almacenar el resultado de la eliminación
		
		//se instancia objeto de Tipos de Salas
		TipoSalas tipos=new TipoSalas();
		//se llena el mundo con los datos del formulario
		this.llenarMundo(tipoSalasForm,tipos,0);
		
		//se verifica que sea un registro existente 
		if(tipos.getCodigo()>0)
		{
				//se cargan los datos del registro a eliminar
				registroEliminacion=tipos.cargarTipoSala(con);
				//se realiza la eliminación
				resp=tipos.eliminarTipoSala(con);
				//se verifica resultado de la transacción
				if(resp<=0)
				{
					logger.error("No se pudo eliminar el registro");
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.sinEliminar", true);
				}
				//se inserta LOG
				this.generarLog(registroEliminacion,null,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			
		}
		
		//se corren posiciones dentro del mapa para borrar el registro
		for(int i=tipoSalasForm.getPos();i<(tipoSalasForm.getNumRegistros()-1);i++)
		{
			tipoSalasForm.setTipoSalas("codigo_"+i,tipoSalasForm.getTipoSalas("codigo_"+(i+1)));
			tipoSalasForm.setTipoSalas("nombre_"+i,tipoSalasForm.getTipoSalas("nombre_"+(i+1)));
			tipoSalasForm.setTipoSalas("quirurgica_"+i,tipoSalasForm.getTipoSalas("quirurgica_"+(i+1)));
			tipoSalasForm.setTipoSalas("urgencias_"+i,tipoSalasForm.getTipoSalas("urgencias_"+(i+1)));
			tipoSalasForm.setTipoSalas("es_usada_"+i,tipoSalasForm.getTipoSalas("es_usada_"+(i+1)));
		}
		
		
		//se disminuye el número de registros
		int numRegistros=tipoSalasForm.getNumRegistros();
		numRegistros--;
		//se actualiza
		tipoSalasForm.setNumRegistros(numRegistros);
		tipoSalasForm.setTipoSalas("numRegistros",numRegistros+"");
		
			
		try
		{
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEliminar de TipoSalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.problemasDatos", true);
		}
		
		
	}

	/**
	 * Método uado para insertar nuevos registros o modificar
	 * registros en la tabla tipos_salas
	 * @param con
	 * @param tipoSalasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, TipoSalasForm tipoSalasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		try
		{
			//se inicia transacción
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
			boolean exitoActualizacion=true;
			boolean exitoInsercion=true;
			
			int resp=0;
			//se instancia objeto TipoSalas
			TipoSalas tipos=new TipoSalas();
			//se itera arreglo de registros
			for(int i=0;i<tipoSalasForm.getNumRegistros();i++)
			{
				this.llenarMundo(tipoSalasForm,tipos,i);
				//se verifica que el registro sea nuevo o de modificación
				// -1 => registro nuevo
				if(tipos.getCodigo()>0)
				{
					
					//obtener registro viejo
					registroAntiguo=tipos.cargarTipoSala(con);
					if(fueModificado(registroAntiguo,tipos))
					{
						//se actualizan los datos
						resp=tipos.actualizarTipoSala(con);
						//se revisa estado actualizacion
						if(resp<=0)
							exitoActualizacion=false;
						else
							this.generarLog(registroAntiguo,tipos,ConstantesBD.tipoRegistroLogModificacion,usuario);	
					}
				}
				//se inserta nuevo registro
				else
				{
					//se insertan los datos
					resp=tipos.insertarTipoSala(con,usuario.getCodigoInstitucionInt());
					//se revisa estado actualizacion
					if(resp<=0)
						exitoInsercion=false;
					else
						//se le añade nuevo código al nuevo registro en el arreglo
						tipoSalasForm.setTipoSalas("codigo_"+i,resp+"");
				}
			}
			
			//verificación de la transacción
			if(exitoActualizacion&&exitoInsercion)
			{
				//éxito!!!!
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				return accionEmpezar(con,tipoSalasForm,mapping,usuario,request);
			}
			//el problema fue en la insercion
			else if(!exitoInsercion)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.sinIngresar",true);
			}
			// o el problema fue en la actualización
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.sinActualizar",true);
			}
			
		}
		catch(SQLException e)
		{
			try
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			}
			catch(SQLException e1)
			{}
			logger.error("Error en accionGuardar de TipoSalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método usado para registrar el LOG modificvacion/eliminacion
	 * de los tipos de salas
	 * @param registro (registro antiguo)
	 * @param tipos (regitro nuevo)
	 * @param tipo (Log)
	 * @param usuario
	 */
	private void generarLog(HashMap registro, TipoSalas tipos, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL TIPO DE SALA===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("nombre_0")+"] " +
			""  ;
		    if(UtilidadTexto.getBoolean(registro.get("quirurgica_0")+""))
		    	log+="\n*  Es Quirúrgica? [SI]";
		    else
		    	log+="\n*  Es Quirúrgica? [NO]";
		    
		    if(UtilidadTexto.getBoolean(registro.get("urgencias_0")+""))
		    	log+="\n*  Es Urgencias? [SI]";
		    else
		    	log+="\n*  Es Urgencias? [NO]";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL TIPO DE SALA===== " +
			"\n*  Código [" +tipos.getCodigo()+"] "+
			"\n*  Descripción ["+tipos.getNombre()+"] " +
			""  ;
		    if(tipos.isQuirurgica())
		    	log+="\n*  Es Quirúrgica? [SI]";
		    else
		    	log+="\n*  Es Quirúrgica? [NO]";
		    if(tipos.isUrgencias())
		    	log+="\n*  Es Urgencias? [SI]";
		    else
		    	log+="\n*  Es Urgencias? [NO]";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE TIPO DE SALA===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("nombre_0")+"] " +
			""  ;
		    if(UtilidadTexto.getBoolean(registro.get("quirurgica_0")+""))
		    	log+="\n*  Es Quirúrgica? [SI]";
		    else
		    	log+="\n*  Es Quirúrgica? [NO]";
		    		
		    if(UtilidadTexto.getBoolean(registro.get("urgencias_0")+""))
		    	log+="\n*  Es Urgencias? [SI]";
		    else
		    	log+="\n*  Es Urgencias? [NO]";
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logTipoSalasCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	

	/**
	 * Método que verifica si un registro fue modificado
	 * @param registro (registro antiguo)
	 * @param tipos (registro nuevo)
	 * @return
	 */
	private boolean fueModificado(HashMap registro, TipoSalas tipos)
	{
		boolean cambio=false;
		//revision de las descripciones
		if(tipos.getNombre().compareTo(registro.get("nombre_0")+"")!=0)
			cambio=true;
		//revisión de campo quirurgica
		if(tipos.isQuirurgica()!=UtilidadTexto.getBoolean(registro.get("quirurgica_0")+""))
			cambio=true;
		
		if(tipos.isUrgencias()!=UtilidadTexto.getBoolean(registro.get("urgencias_0")+""))
			cambio=true;
		
		return cambio;
	}

	/**
	 * Método usado para cargar los datos de la forma al mundo
	 * @param tipoSalasForm
	 * @param tipos
	 * @param posicion del registro
	 */
	private void llenarMundo(TipoSalasForm tipoSalasForm, TipoSalas tipos, int pos) 
	{
		int codigo=0;
		if(tipoSalasForm.getEstado().equals("guardar"))
		{
			//se verifica si el codigo existe
			if((tipoSalasForm.getTipoSalas("codigo_"+pos)+"").equals("")||(tipoSalasForm.getTipoSalas("codigo_"+pos)+"").equals("null"))
				codigo=-1;
			else
				codigo=Integer.parseInt(tipoSalasForm.getTipoSalas("codigo_"+pos)+"");
			tipos.setCodigo(codigo);
			tipos.setNombre(tipoSalasForm.getTipoSalas("nombre_"+pos)+"");
			tipos.setQuirurgica(UtilidadTexto.getBoolean(tipoSalasForm.getTipoSalas("quirurgica_"+pos)+""));
			tipos.setUrgencias(UtilidadTexto.getBoolean(tipoSalasForm.getTipoSalas("urgencias_"+pos)+""));
		}
		else if(tipoSalasForm.getEstado().equals("eliminar"))
		{
			//se asigna el código Axioma
			tipoSalasForm.setCodigoRegistro(tipoSalasForm.getTipoSalas("codigo_"+tipoSalasForm.getPos())+"");
			
			if(tipoSalasForm.getCodigoRegistro().equals("")||tipoSalasForm.getEstado().equals("null"))
				tipos.setCodigo(-1);
			else
				tipos.setCodigo(Integer.parseInt(tipoSalasForm.getCodigoRegistro()));
		}
		
	}

	/**
	 * Método usado para generar un nuevo registro en el mapa 
	 * de los tipos de salas
	 * @param con
	 * @param tipoSalasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, TipoSalasForm tipoSalasForm, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			//se obtiene nueva posicion
			int pos=tipoSalasForm.getNumRegistros();
			
			//se crea un nuevo registro
			tipoSalasForm.setTipoSalas("codigo_"+pos,""); //se agrega código vacío
			tipoSalasForm.setTipoSalas("nombre_"+pos,""); //sin descripción
			tipoSalasForm.setTipoSalas("quirurgica_"+pos,"false"); //campo de chequeo FALSE
			tipoSalasForm.setTipoSalas("urgencias_"+pos,"false"); //campo de chequeo FALSE
			tipoSalasForm.setTipoSalas("es_usada_"+pos,"0"); //un tipo sala nuevo no se está usando
			
			//se aumenta número de registros
			pos++;
			
			//se actualiza tamaño del arreglo en el formulario
			tipoSalasForm.setTipoSalas("numRegistros",pos+"");
			tipoSalasForm.setNumRegistros(pos);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionNuevo de TipoSalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método usado para realizar los procesos
	 * del estado "empezar" en tipos de salas
	 * @param con
	 * @param tipoSalasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, TipoSalasForm tipoSalasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		try
		{
			//se reinicia información
			String estado=tipoSalasForm.getEstado();
			String registrosUsados=tipoSalasForm.getRegistrosUsados();
			tipoSalasForm.reset();
			tipoSalasForm.setEstado(estado);
			tipoSalasForm.setRegistrosUsados(registrosUsados);
			//****se instancia objeto de Tipos de Salas ********************
			TipoSalas tipos=new TipoSalas();
			//***se cargan los tipos de salas actuales **************
			tipoSalasForm.setTipoSalas(tipos.cargarTiposSalas(con,usuario.getCodigoInstitucionInt()));
			//***se almacena el número de registros consultados **************
			tipoSalasForm.setNumRegistros(Integer.parseInt(tipoSalasForm.getTipoSalas("numRegistros")+""));
			
			HashMap<String, Object> 
				criterios = new HashMap<String, Object>(),
				mapa = tipoSalasForm.getTipoSalas();
			int numRegistrosTipoSalas = tipoSalasForm.getNumRegistros(),
				codigo=0;
			
			logger.info("===> Numero registros de Tipos Salas: "+numRegistrosTipoSalas);
			logger.info("===> Tipos Salas: "+mapa);
			
			/*
			 * Llenamos los datos con el codigo
			 * Si el codigo es null quiere decir que NO se está usando en los grupos de servicios
			 * Si el codigo viene con un codigo, quiere decir que ese codigo es el que está siendo usado en los grupos de servicios
			 */
			for(int i=0; i<numRegistrosTipoSalas; i++)
			{
				codigo = Integer.parseInt(mapa.get("codigo_"+i)+"");
				criterios.put(i+"", tipos.consultaTipoSalasGruposServicios(con, codigo).get("cod_0"));
				/*
				logger.info("===> Antes !!! tuvoGrupoServicio = "+tipoSalasForm.getTipoSalas("tuvoGrupoServicio_"+i));*/
				String tuvoGrupoServicio = (tipoSalasForm.getTipoSalas("tuvoGrupoServicio_"+i)+"");/*
				if(tuvoGrupoServicio.equals("si"))
				{
					logger.info("===> Vamos a poner es usada en 0");
					tipoSalasForm.setTipoSalas("es_usada_"+i, "0");
					//logger.info("===> Mapa: "+tipoSalasForm.getTipoSalas());
				}
				*/
				if(criterios.get(i+"")!=null)
				{
					logger.info("===> El registro "+i+" SI tiene dato");
					tipoSalasForm.setTipoSalas("es_usada_"+i, "0");
					tipoSalasForm.setTipoSalas("tuvoGrupoServicio_"+i, "si");
					
					logger.info("===> Después !!! tuvoGrupoServicio = "+tipoSalasForm.getTipoSalas("tuvoGrupoServicio_"+i));
					//logger.info("===> Mapa: "+tipoSalasForm.getTipoSalas());
					
					/*
					 * Solución de la tarea 52097
					 */
					
				}
				else
				{
					logger.info("===> No se va a hacer Nada ");
					/*
					logger.info("===> El registro "+i+" NO tiene dato");
					logger.info("===> tuvoGrupoServicio = "+tipoSalasForm.getTipoSalas("tuvoGrupoServicio_"+i));
					tipoSalasForm.setTipoSalas("tuvoGrupoServicio_"+i, "no");
					tuvoGrupoServicio = (tipoSalasForm.getTipoSalas("tuvoGrupoServicio_"+i)+"");
					if(tuvoGrupoServicio.equals("si"))
					{
						logger.info("===> Vamos a poner es usada en 0");
						tipoSalasForm.setTipoSalas("es_usada_"+i, "0");
						//logger.info("===> Mapa: "+tipoSalasForm.getTipoSalas());
					}
					*/
				}
			}
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEmpezar de TipoSalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TipoSalasAction", "errors.problemasDatos", true);
		}
	}
}
