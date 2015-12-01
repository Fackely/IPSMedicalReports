/*
 * Julio 16 del 2007
 */
package com.princetonsa.action.facturacion;

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
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.facturacion.CondicionesXServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CondicionesXServicios;

/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de hoja de Gastos
 */
public class CondicionesXServiciosAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CondicionesXServiciosAction.class);
	
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
			if(form instanceof CondicionesXServiciosForm)
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
				CondicionesXServiciosForm hojaForm =(CondicionesXServiciosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=hojaForm.getEstado(); 
				logger.warn("estado CondicionesXServiciosAction-->"+estado);


				if(estado == null)
				{
					hojaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Condición de Servicios (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,hojaForm,mapping);
				}
				else if (estado.equals("ingresarNuevoServicio"))
				{
					return accionIngresarNuevoServicio(con,hojaForm,mapping,usuario);
				}
				else if (estado.equals("ingresarNuevaCondicion"))
				{
					return accionIngresarNuevaCondicion(con,hojaForm,mapping);
				}
				else if (estado.equals("eliminarServicio"))
				{
					return accionEliminarServicio(con,hojaForm,mapping);
				}	
				else if (estado.equals("eliminarCondicion"))
				{
					return accionEliminarCondicion(con,hojaForm,mapping);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,hojaForm,mapping,usuario,request);
				}
				else
				{
					hojaForm.reset();
					logger.warn("Estado no valido dentro del flujo de CondicionesXServiciosAction (null) ");
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
	 * Método implementado para guardar la información de condiciones X Servicios
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Se instancia objeto CondicionesXServicios
		CondicionesXServicios hoja = new CondicionesXServicios();
		HashMap datosAntiguos = new HashMap();
		int resp = 0,tipoLog = 0;
		
		logger.info("\n mapa hojaForm.getMapaServicios() "+hojaForm.getMapaServicios());
		logger.info("\n mapa hojaForm.getMapaCondiciones() "+hojaForm.getMapaCondiciones());
		int numServiciosAEliminar = obtenerNumRegistrosAEliminar(hojaForm.getMapaServicios(), hojaForm.getNumServicios());
		int numCondicionesAEliminar = obtenerNumRegistrosAEliminar(hojaForm.getMapaCondiciones(), hojaForm.getNumCondiciones());
		
		//Se analiza el tipo de log que se debe generar----------------------------------------
		if(hojaForm.getConsecutivo()<=0)
			tipoLog = ConstantesBD.tipoRegistroLogInsercion;
		else if(hojaForm.getConsecutivo()>0&&hojaForm.getNumCondiciones()==numCondicionesAEliminar&&hojaForm.getNumServicios()==numServiciosAEliminar)
		{
			tipoLog = ConstantesBD.tipoRegistroLogEliminacion;
			hoja.consultarCondicionServiciosXConsecutivo(con, hojaForm.getConsecutivo());
			datosAntiguos.put("mapaServicios",hoja.getMapaServicios());
			datosAntiguos.put("numServicios",hoja.getNumServicios()+"");
			datosAntiguos.put("mapaCondiciones",hoja.getMapaCondiciones());
			datosAntiguos.put("numCondiciones",hoja.getNumCondiciones()+"");
		}
		else
		{
			datosAntiguos = hoja.huboModificacion(con,hojaForm.getConsecutivo(),hojaForm.getMapaServicios(),hojaForm.getNumServicios(),hojaForm.getMapaCondiciones(),hojaForm.getNumCondiciones());
			
			//Se verifica si fue modificado
			if(UtilidadTexto.getBoolean(datosAntiguos.get("modificado").toString()))
				tipoLog = ConstantesBD.tipoRegistroLogModificacion;
		}
		
		//si se encontró un tipo de log quiere decir que se debe hacer operación en la base de datos
		if(tipoLog>0)
		{
			//se carga el mundo de hoja de condicion X Servicios-------------------------------------------------
			hoja.setMapaServicios(hojaForm.getMapaServicios());
			hoja.setNumServicios(hojaForm.getNumServicios());
			hoja.setMapaCondiciones(hojaForm.getMapaCondiciones());
			hoja.setNumCondiciones(hojaForm.getNumCondiciones());
			
			//se guarda informacion -----------------------------------------------------------------
			resp = hoja.guardar(con, hojaForm.getConsecutivo(), usuario.getCodigoInstitucion());
			
			//si la operacion es exitosa, vuelve y se consulta la informacion
			if(resp>0)
			{
				hojaForm.reset();
				hoja.consultarCondicionServiciosXConsecutivo(con, resp);
				hojaForm.setConsecutivo(resp);
				hojaForm.setMapaCondiciones(hoja.getMapaCondiciones());
				hojaForm.setNumCondiciones(hoja.getNumCondiciones());
				hojaForm.setMapaServicios(hoja.getMapaServicios());
				hojaForm.setNumServicios(hoja.getNumServicios());
				
				hojaForm.setEstado("guardar");
				
				//Se genera el log tipo archivo
				this.generarLog(datosAntiguos, hojaForm, tipoLog, usuario);
				
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("Problema guardando datos",new ActionMessage("errors.noSeGraboInformacion","DE CONDICION DE SERVICIOS"));
				saveErrors(request, errores);
				hojaForm.setEstado("empezar");
			}
		}
		else
			resp=1;
		
		
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método implementado para generar el log
	 * @param institucionAntigua
	 * @param institucion
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(HashMap datosAntiguos, CondicionesXServiciosForm hojaForm, int tipo, UsuarioBasico usuario) 
	{
		String log="";
		HashMap mapaServicios = new HashMap();
		int numServicios = 0;
		HashMap mapaCondiciones = new HashMap();
		int numCondiciones = 0;
		
		if(tipo==ConstantesBD.tipoRegistroLogModificacion||tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			mapaServicios = (HashMap)datosAntiguos.get("mapaServicios");
			numServicios = Integer.parseInt(datosAntiguos.get("numServicios").toString());
			mapaCondiciones = (HashMap)datosAntiguos.get("mapaCondiciones");
			numCondiciones = Integer.parseInt(datosAntiguos.get("numCondiciones").toString());
		}
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			
		    log="\n            ====INFORMACION ORIGINAL CONDICION DE SERVICIOS ===== " +
		    "\n   SERVICIOS ";
		    
		    for(int i=0;i<numServicios;i++)
		    	log+="\n      * "+mapaServicios.get("descripcionServicio_"+i);
		    
		    log+="\n   CONDICIONES ";
		    
		    for(int i=0;i<numCondiciones;i++)
		    	log+="\n      * "+mapaCondiciones.get("codigoCondicion_"+i)+" - "+
		    		mapaCondiciones.get("descripcionCondicion_"+i)+"\n        ";

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN de CONDICION X SERVICIOS===== " +
		    "\n   SERVICIOS ";
			
		    for(int i=0;i<hojaForm.getNumServicios();i++)
		    	log+="\n      * "+hojaForm.getMapaServicios("descripcionServicio_"+i);
		    
		    log+="\n   CONDICIONES ";
		    
		    for(int i=0;i<hojaForm.getNumCondiciones();i++)
		    	log+="\n      * "+hojaForm.getMapaCondiciones("codigoCondicion_"+i)+" - "+
		    		hojaForm.getMapaCondiciones("descripcionCondicion_"+i)+"\n        ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE CONDICION X SERVICIOS===== " +
		    "\n   SERVICIOS ";
		    
		    for(int i=0;i<numServicios;i++)
		    	log+="\n      * "+mapaServicios.get("descripcionServicio_"+i);
		    
		    log+="\n   CONDICIONES ";
		    
		    for(int i=0;i<numCondiciones;i++)
		    	log+="\n      * "+mapaCondiciones.get("codigoCondicion_"+i)+" - "+
		    		mapaCondiciones.get("descripcionCondicion_"+i)+"\n        ";
		}
		else if(tipo==ConstantesBD.tipoRegistroLogInsercion)
		{
			log="\n\n            ====INFORMACION INGRESADA DE CONDICION POR SERVICIOS===== " +
		    "\n   SERVICIOS ";
			
		    for(int i=0;i<hojaForm.getNumServicios();i++)
		    	log+="\n      * "+hojaForm.getMapaServicios("descripcionServicio_"+i);
		    
		    log+="\n   CONDICIONES ";
		    
		    for(int i=0;i<hojaForm.getNumCondiciones();i++)
		    	log+="\n      * "+hojaForm.getMapaCondiciones("codigoCondicion_"+i)+" - "+
		    		hojaForm.getMapaCondiciones("descripcionCondicion_"+i)+"        ";// +
		    	//	"Cantidad ["+hojaForm.getMapaCondiciones("cantidad_"+i)+"]";
		}
		log+="\n========================================================\n\n\n " ;
	//	LogsAxioma.enviarLog(ConstantesBD.logCondicionesXServicios Codigo, log, tipo,usuario.getLoginUsuario());
		
	}
	
	
	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private int obtenerNumRegistrosAEliminar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}
	
	/**
	 * Método que obtiene el número de servicios que se van a ingresar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private int obtenerNumRegistrosAIngresar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(!UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}

	/**
	 * Método implementado para eliminar un articulo de la hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarCondicion(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getPos();
		String codigoCondicionInsertado = hojaForm.getCodigosCondicionInsertados();
		String codigoCondicion = hojaForm.getMapaCondiciones("codigoExamenNuevo").toString();
		//se quita del listado de servicios insertados
		codigoCondicionInsertado = codigoCondicionInsertado.replaceAll(codigoCondicion+",", "");
		hojaForm.setCodigosCondicionInsertados(codigoCondicionInsertado);
		
		//se verifica si el registro existe en la base de datos
		if(UtilidadTexto.getBoolean(hojaForm.getMapaCondiciones("existeBd_"+pos).toString()))
			//se cambia el valor del atributo eliminar en true
			hojaForm.setMapaCondiciones("eliminar_"+pos, "true");
		else
		{
			int numCondiciones = hojaForm.getNumCondiciones() - 1;
			//como no existe en la base de datos se puede borrar del mapa
			for(int i=pos;i<numCondiciones;i++)
			{
				hojaForm.setMapaCondiciones("codigoExamenNuevo", hojaForm.getMapaCondiciones("codigoExamenNuevo"+(pos+1)));
				hojaForm.setMapaCondiciones("descripcionExamenNuevo", hojaForm.getMapaCondiciones("descripcionExamenNuevo"+(pos+1)));
				hojaForm.setMapaCondiciones("numeroElementosCondicion", hojaForm.getMapaCondiciones("numeroElementosCondicion"+(pos+1)));
				
			}
			
			hojaForm.getMapaCondiciones().remove("codigoExamenNuevo"+numCondiciones);
			hojaForm.getMapaCondiciones().remove("descripcionExamenNuevo"+numCondiciones);
			hojaForm.getMapaCondiciones().remove("numeroElementosCondicion"+numCondiciones);
	
			
			hojaForm.setNumCondiciones(numCondiciones);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para eliminar un servicio de la hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getPos();
		String codigosServiciosInsertados = hojaForm.getCodigosServiciosInsertados();
		String codigoServicios = hojaForm.getMapaServicios("codigo_"+pos).toString();
		//se quita del listado de servicios insertados
		codigosServiciosInsertados = codigosServiciosInsertados.replaceAll(codigoServicios+",", "");
		hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
		
		//se verifica si el registro existe en la base de datos
		if(UtilidadTexto.getBoolean(hojaForm.getMapaServicios("existeBd_"+pos).toString()))
		{
			//se cambia el valor del atributo eliminar en true
			hojaForm.setMapaServicios("eliminar_"+pos, "true");
			
			logger.info("\n\nValor del Atributo Eliminar   -->   "+hojaForm.getMapaServicios("eliminar_"+pos)+"\n\n");
		}	
		else
		{
			int numServicios = hojaForm.getNumServicios() - 1;
			//como no existe en la base de datos se puede borrar del mapa
			for(int i=pos;i<numServicios;i++)
			{
				hojaForm.setMapaServicios("codigo_"+pos, hojaForm.getMapaServicios("codigo_"+(pos+1)));
				hojaForm.setMapaServicios("descripcionServicio_"+pos, hojaForm.getMapaServicios("descripcionServicio_"+(pos+1)));
				hojaForm.setMapaServicios("existeBd_"+pos, hojaForm.getMapaServicios("existeBd_"+(pos+1)));
				hojaForm.setMapaServicios("eliminar_"+pos, hojaForm.getMapaServicios("eliminar_"+(pos+1)));
			}
			
			hojaForm.getMapaServicios().remove("codigo_"+numServicios);
			hojaForm.getMapaServicios().remove("descripcionServicio_"+numServicios);
			hojaForm.getMapaServicios().remove("existeBd_"+numServicios);
			hojaForm.getMapaServicios().remove("eliminar_"+numServicios);
			
			hojaForm.setNumServicios(numServicios);
		}
		
		//Si se borran todos los registros y es registro nuevo, se inicializa  todo
		if(obtenerNumRegistrosAIngresar(hojaForm.getMapaServicios(), hojaForm.getNumServicios())<=0&&hojaForm.getConsecutivo()<=0)
		{
			hojaForm.reset();
		}
			
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para ingresar un nuevo articulo a la parametrizacion de hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarNuevaCondicion(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getNumCondiciones() ;
		String codigoCondicionInsertado = hojaForm.getCodigosCondicionInsertados();
		
		//se adiciona informacion de control
		hojaForm.setMapaCondiciones("codigoCondicion_"+pos, hojaForm.getMapaCondiciones("codigoExamenNuevo"));
		hojaForm.setMapaCondiciones("descripcionCondicion_"+pos, hojaForm.getMapaCondiciones("descripcionExamenNuevo"));
		
		hojaForm.setMapaCondiciones("existeBd_"+pos, "false");
		hojaForm.setMapaCondiciones("eliminar_"+pos, "false");
				
		//se registra la condicion como insertado
		codigoCondicionInsertado += hojaForm.getMapaCondiciones("codigoExamenNuevo").toString() + ",";
		
		logger.info("\n\n\n Posicion --->   "+pos);
		logger.info("\n");
		logger.info("\n\n\n El codigo de la Condicion --->   "+codigoCondicionInsertado);
		logger.info("\n");
		hojaForm.setCodigosCondicionInsertados(codigoCondicionInsertado);
		
		
		//se actualiza tamaño del mapa
		pos++;
		hojaForm.setNumCondiciones(pos);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
		
		
	}

	/**
	 * Método implementado para ingresar un nuevo servicio a  la parametrizacion de hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIngresarNuevoServicio(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int pos = hojaForm.getNumServicios() - 1;
		String codigosServiciosInsertados = hojaForm.getCodigosServiciosInsertados();
		String codigoServicio = hojaForm.getMapaServicios("codigo_"+pos).toString() ;
		
		
		//**************SE DEBE VERIFICAR SI ESE SERVICIO YA TIENE PARAMETRIZACION DE HOJA DE GASTOS********************
		int consecutivo = CondicionesXServicios.consultarConsecutivoXServicio(con, codigoServicio, usuario.getCodigoInstitucion());
		if(consecutivo>0&&pos<=0) //si es el primer servicio que se busca se postula lo ya ingresado
		{
			CondicionesXServicios hoja = new CondicionesXServicios();
			//Se consulta lo que ya está parametrizado
			hojaForm.setConsecutivo(consecutivo);
			hoja.consultarCondicionServiciosXConsecutivo(con, hojaForm.getConsecutivo());
			hojaForm.setMapaCondiciones(hoja.getMapaCondiciones());
			hojaForm.setNumCondiciones(hoja.getNumCondiciones());
			hojaForm.setMapaServicios(hoja.getMapaServicios());
			hojaForm.setNumServicios(hoja.getNumServicios());
			
			//Imprimo en consola toda la Informacion que me trae los mapas 
			logger.info("\n\nImprimo mapa Servicios   -->   "+hojaForm.getMapaServicios()+"\n\n");
			logger.info("\n\nImprimo mapa Condiciones   -->   "+hojaForm.getMapaCondiciones()+"\n\n");
			
			//se saca codigos de servicios insertados
			for(int i=0;i<hojaForm.getNumServicios();i++)
				codigosServiciosInsertados += hojaForm.getMapaServicios("codigo_"+i).toString() + ",";
			hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
			
			//se saca codigos de condicion insertados
			String codigoCondicionInsertado = "";
			for(int i=0;i<hojaForm.getNumCondiciones();i++)
				codigoCondicionInsertado += hojaForm.getMapaCondiciones("codigoCondicion_"+i).toString() + ",";
			hojaForm.setCodigosCondicionInsertados(codigoCondicionInsertado);
		}
		else if(consecutivo>0&&pos>0&&consecutivo!=hojaForm.getConsecutivo()) //ya se tenían parametrizados servicios (no se puede postular) y el consecutivo es diferente al actual 
		{
			hojaForm.setMensaje("El Servicio "+hojaForm.getMapaServicios("descripcionServicio_"+pos)+" ya está parametrizada en otra lista de Condiciones y no se debe volver a adicionar");
			
			//se borra informacion seleccionada
			hojaForm.getMapaServicios().remove("codigo_"+pos);
			hojaForm.getMapaServicios().remove("descripcionServicio_"+pos);
			hojaForm.getMapaServicios().remove("fueEliminadoServicio_"+pos);
			hojaForm.getMapaServicios().remove("codigoCups_"+pos);
			hojaForm.getMapaServicios().remove("esPos_"+pos);
			hojaForm.getMapaServicios().remove("finalidad_"+pos);
			hojaForm.getMapaServicios().remove("cantidad_"+pos);
			hojaForm.getMapaServicios().remove("urgente_"+pos);
			
			hojaForm.setNumServicios(pos);
			
			
		}
		else
		{
			//se borra informacion innecesario proveniente de la busqueda genérica de servicios
			hojaForm.getMapaServicios().remove("fueEliminadoServicio_"+pos);
			hojaForm.getMapaServicios().remove("codigoCups_"+pos);
			hojaForm.getMapaServicios().remove("esPos_"+pos);
			hojaForm.getMapaServicios().remove("finalidad_"+pos);
			hojaForm.getMapaServicios().remove("cantidad_"+pos);
			hojaForm.getMapaServicios().remove("urgente_"+pos);
			
			//se adiciona informacion de control
			hojaForm.setMapaServicios("existeBd_"+pos, "false");
			hojaForm.setMapaServicios("eliminar_"+pos, "false");
			
			//se registra el servicio como insertado
			codigosServiciosInsertados += codigoServicio + ",";
			hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
		}
		//***************************************************************************************************************
		
		
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que inicia el flujo de la funcionalidad de la condicion de Servicios
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, CondicionesXServiciosForm hojaForm, ActionMapping mapping) 
	{
		hojaForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}
