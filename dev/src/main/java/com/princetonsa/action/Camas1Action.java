/*
 * @(#)Camas1Action.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.Camas1Form;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.ServiciosCamas1;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   c, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Junio 1, 2005
 * @author wrios
 */
public class Camas1Action extends Action
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(Camas1Action.class);
		
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
		Connection con = null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof Camas1Form)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				Camas1Form camas1Form =(Camas1Form)form;
				HttpSession session=request.getSession();		
				UsuarioBasico user = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=camas1Form.getEstado(); 
				logger.warn("\n estado-->"+estado+" accion=="+camas1Form.getAccion());

				if(estado == null)
				{
					camas1Form.reset();	
					logger.warn("Estado no valido dentro del flujo de registro de camas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				//************************ESTADOS FUNCIONALIDAD MANEJO DE CAMAS*******************************************
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(camas1Form,mapping, con, user);
				}
				else if (estado.equals("ingresarModificar"))
				{
					if(camas1Form.getLimpiarMapaBool())
						camas1Form.resetMapa();

					if(camas1Form.getAccion().equals("limpiar"))
					{    
						camas1Form.resetInsertarNueva();
						camas1Form.setAccion("iniciarInsercion");
					}    
					if(camas1Form.getAccion().equals("redireccion"))
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(camas1Form.getLinkSiguiente());
						return null;
					}
					else 
						if (camas1Form.getAccion().equals("eliminiarServicio"))
							return eliminarServicio(con, camas1Form, mapping,user);
						else if (camas1Form.getAccion().equals("ordenar"))
							return this.accionOrdenar(camas1Form, mapping, request, con);
						else
							return this.accionIngresarModificar(camas1Form,mapping, con, user);



				}
				else if (estado.equals("guardarNueva"))
				{
					return this.accionGuardarNueva(camas1Form, mapping, request, user, con);
				}
				else if (estado.equals("resumen"))
				{
					//los dos estados hacen los mismos cargar pero en la pagina se muestran diferentes
					if(camas1Form.getLimpiarMapaBool())
						camas1Form.resetMapa();
					camas1Form.setMostrarVentanaInsertarNueva(false);
					if(camas1Form.getAccion().equals("redireccion"))
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(camas1Form.getLinkSiguiente());
						return null;
					}
					else if (camas1Form.getAccion().equals("ordenar"))
						return this.accionOrdenar(camas1Form, mapping, request, con);
					else 
						return this.accionResumenOModificar(camas1Form,mapping, con, user);
				}
				else if (estado.equals("resumen") || estado.equals("modificar"))
				{
					//los dos estados hacen los mismos cargar pero en la pagina se muestran diferentes
					if(camas1Form.getLimpiarMapaBool())
						camas1Form.resetMapa();
					camas1Form.setMostrarVentanaInsertarNueva(false);
					if(camas1Form.getAccion().equals("redireccion"))
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(camas1Form.getLinkSiguiente());
						return null;
					}
					else if (camas1Form.getAccion().equals("ordenar"))
						return this.accionOrdenar(camas1Form, mapping, request, con);
					if (camas1Form.getAccion().equals("eliminiarServicio"))
						return eliminarServicio(con, camas1Form, mapping,user);
					else if (camas1Form.getAccion().equals("empezar"))
					{    
						camas1Form.setAccion("ninguna");
						return this.accionResumenOModificar(camas1Form,mapping, con, user);
					}    
					else
						return this.accionModificar(camas1Form, mapping, con, user);
				}
				else if (estado.equals("guardarModificar"))
				{
					return this.accionGuardarModificar(camas1Form, mapping, request, user, con);
				}
				else if(estado.equals("busquedaAvanzada") || estado.equals("consultar"))
				{
					if(camas1Form.getAccion()==null)
						camas1Form.setAccion("");

					if(camas1Form.getAccion().equals("redireccion") && estado.equals("busquedaAvanzada") )
					{
						UtilidadBD.cerrarConexion(con);
						response.sendRedirect(camas1Form.getLinkSiguiente());
						return null;
					}
					else if (camas1Form.getAccion().equals("ordenar"))
						return this.accionOrdenar(camas1Form, mapping, request, con);
					else
						return this.accionBusquedaAvanzada(camas1Form,mapping, con, user);
				}
				//estado que se activa cuando se cambia el centro de atencion en el listado de camas
				else if(estado.equals("recargarListadoCamas")||estado.equals("recargarListadoCamasConsulta"))
				{
					return accionRecargarListadoCamas(con,mapping,camas1Form,user);
				}
				//estado que se usa para recargar el listado de camas al dar boton volver
				else if (estado.equals("recargarIngresar")||estado.equals("recargarConsultar"))
				{
					return accionRecargar(con,mapping,camas1Form);
				}
				//**************************************************************************************************
				//**********************ESTADOS FUNCIONALIDAD ACTIVACION CAMAS*************************************
				else if( estado.equals("cambiarEstadoCamaDesinfeccion") )
				{
					return this.accionCambiarEstadoCamaDesinfeccion(camas1Form,mapping, con);
				}
				else if( estado.equals("listarCamasDesinfeccion"))
				{
					return accionListarCamasDesinfeccion(con,camas1Form,mapping,user);

				}
				else if (estado.equals("ordenarActivacion"))
				{
					return accionOrdenar(con,camas1Form,mapping);
				}
				else if (estado.equals("redireccionActivacion"))
				{
					return accionRedireccion(con,camas1Form,response,mapping,request);
				}
				//******************************************************************************************************
				//****************ESTADO PARA FILTROS AJAX***************************************
				else if(estado.equals("filtroHabitaciones"))
				{
					return accionFiltroHabitaciones(con,camas1Form,user,response);
				}
				else if (estado.equals("filtroPisos"))
				{
					return accionFiltroPisos(con,camas1Form,user,response);
				}
				else if (estado.equals("filtroTipoHabitacion"))
				{
					return accionFiltroTipoHabitacion(con,camas1Form,response);
				}
				//*******************************************************************************
				else
					if (estado.equals("eliminiarServicio"))
					{
						return eliminarServicio(con, camas1Form, mapping,user);
					}
					else
					{
						camas1Form.reset();
						logger.warn("Estado no valido dentro del flujo de registro camas (null) ");
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
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarServicio (Connection connection,  Camas1Form forma, ActionMapping mapping,UsuarioBasico usuario) throws SQLException
	{
		String [] indices={"codigoTablaServiciosCama_","nombreTipoMonitoreo_","nombreServicio_","codigoServicio_","codigoTipoMonitoreo_"};
		
		int codigoTablaServiciosCama=Utilidades.convertirAEntero(forma.getServiciosCama1Map("codigoTablaServiciosCama_"+forma.getIndexEliminar())+"");
		
		
		if (codigoTablaServiciosCama<0)
		{
			//se elimina el servicio del mapa que se encuentra en memoria
			forma.setServiciosCama1Map(Listado.eliminarRegistroMapa(forma.getServiciosCama1Map(), indices, forma.getIndexEliminar(), forma.getNumeroFilasMapa()));
		}
		else
		{
			//se elimina el servicio que se encuentra en la BD asociado a esta cama.
			ServiciosCamas1 mundoServiciosCamas1 = new ServiciosCamas1();
            //llenamos los datos necesarios para el eliminar 
            mundoServiciosCamas1.setCodigo(codigoTablaServiciosCama);
            mundoServiciosCamas1.eliminarServiciosCamas1Transaccional(connection, ConstantesBD.continuarTransaccion);
            
            forma.setAccion("ninguna");
            this.accionResumenOModificar(forma,mapping, connection, usuario);
		}
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Método que realiza la recarga de camas sin consultarlas de nuevo
	 * @param con
	 * @param mapping
	 * @param camas1Form
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, ActionMapping mapping, Camas1Form camas1Form) 
	{
		if(camas1Form.getEstado().equals("recargarConsultar"))
			camas1Form.setEstado("consultar");
		else
			camas1Form.setEstado("ingresarModificar");
		
		camas1Form.setAccion("");
			
	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que realiza la paginacion del listado de camas en desinfección
	 * @param con
	 * @param camas1Form
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, Camas1Form camas1Form, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.closeConnection(con);
			response.sendRedirect(camas1Form.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en accionRedireccion", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que realiza la ordenacion del listado de camas en desinfección
	 * @param con
	 * @param camas1Form
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, Camas1Form camas1Form, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"seleccion_",
				"piso_",
				"habitacion_",
				"numero_cama_",
				"descripcion_",
				"codigo_",
				"nombre_centro_costo_"
			};

		int numRegistros = Integer.parseInt(camas1Form.getServiciosCama1Map("numRegistros").toString());
		
		camas1Form.setServiciosCama1Map(Listado.ordenarMapa(indices,
				camas1Form.getColumna(),
				camas1Form.getUltimaPropiedad(),
				camas1Form.getServiciosCama1Map(),
				numRegistros));
		
		camas1Form.setServiciosCama1Map("numRegistros",numRegistros+"");
		
		camas1Form.setUltimaPropiedad(camas1Form.getColumna());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaLiberarCamaDesinfeccion");
	}

	/**
	 * Método implementado para listar las camas que están en desinfeccion
	 * @param con
	 * @param camas1Form
	 * @param mapping
	 * @param user 
	 * @return
	 */
	private ActionForward accionListarCamasDesinfeccion(Connection con, Camas1Form camas1Form, ActionMapping mapping, UsuarioBasico user) 
	{
		camas1Form.reset();
		camas1Form.resetMapa();
		
		//Se consultan las camas en desinfeccion
		camas1Form.setServiciosCama1Map(
			UtilidadesManejoPaciente.obtenerCamas(
				con, 
				user.getCodigoInstitucion(), 
				"", 
				"", 
				ConstantesBD.codigoEstadoCamaDesinfeccion+"", 
				"", 
				user.getCodigoCentroAtencion()+"", 
				"", 
				"", 
				"",
				"",
				"",
				"",
				"",
				"",
				"")
			);
		
		camas1Form.setNumeroCamasPosibleLiberacion(Integer.parseInt(camas1Form.getServiciosCama1Map("numRegistros").toString()));
		
		camas1Form.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(user.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaLiberarCamaDesinfeccion");
	}

	/**
	 * Método que realiza el filtro de los tipos de habitacion
	 * @param con
	 * @param camas1Form
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroTipoHabitacion(Connection con, Camas1Form camas1Form, HttpServletResponse response) 
	{
		
		String resultado = "<respuesta>";
		
		
		boolean encontro = false;
		
		for(int i=0;i<camas1Form.getHabitaciones().size();i++)
		{
			HashMap elemento = (HashMap)camas1Form.getHabitaciones().get(i);
			if(elemento.get("codigo").toString().equals(camas1Form.getHabitacionStr()))
			{
				encontro = true;
				resultado += "<tipo-habitacion>" +elemento.get("tipoHabitacion")+"</tipo-habitacion>";
				camas1Form.setTipoHabitacion(elemento.get("tipoHabitacion").toString());
			}
		}
		
		if(!encontro)
		{
			resultado += "<tipo-habitacion></tipo-habitacion>";
			camas1Form.setTipoHabitacion("");
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroTipoHabitacion: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para realizar el filtro de los pisos
	 * @param con
	 * @param camas1Form
	 * @param user
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroPisos(Connection con, Camas1Form camas1Form, UsuarioBasico user, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		camas1Form.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, user.getCodigoInstitucionInt(), camas1Form.getCodigoCentroAtencion()));
		arregloAux = camas1Form.getPisos();
		
		//Revision de los pisos segun centroatencion seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			resultado += "<piso>" +
				"<codigo>"+elemento.get("codigo")+"</codigo>"+
				"<codigo-piso>"+elemento.get("codigopiso")+"</codigo-piso>"+
				"<nombre-piso>"+elemento.get("nombre")+"</nombre-piso>"+
			 "</piso>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroPisos: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para realizar el filtro de las habitaciones
	 * @param con
	 * @param camas1Form
	 * @param user
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroHabitaciones(Connection con, Camas1Form camas1Form, UsuarioBasico user, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		camas1Form.setHabitaciones(UtilidadesManejoPaciente.obtenerHabitaciones(con, user.getCodigoInstitucionInt(), camas1Form.getCodigoCentroAtencion(), Integer.parseInt(camas1Form.getPiso())));
		arregloAux = camas1Form.getHabitaciones();
		
		
		//Revision de las habitaciones segun piso seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			resultado += "<habitacion>" +
				"<codigo>"+elemento.get("codigo")+"</codigo>"+
				"<codigo-habitacion>"+elemento.get("codigoHabitacion")+"</codigo-habitacion>"+
				"<nombre-habitacion>"+elemento.get("nombre")+"</nombre-habitacion>"+
			 "</habitacion>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml;charset=iso-8859-1");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroHabitaciones: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para recargar el listado de camas
	 * @param con
	 * @param mapping
	 * @param camas1Form
	 * @param user 
	 * @return
	 */
	private ActionForward accionRecargarListadoCamas(Connection con, ActionMapping mapping, Camas1Form camas1Form, UsuarioBasico user) 
	{
		Camas1 mundoCamas1= new Camas1();
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
				
		mundoCamas1.setCentroAtencion(camas1Form.getCodigoCentroAtencion());
		logger.info("valir del centro de atencion con que se busca >> "+mundoCamas1.getCentroAtencion());
		if(camas1Form.getEstado().equals("recargarListadoCamas"))
			camas1Form.setEstado("ingresarModificar");
		else
		{
			camas1Form.setEstado("consultar");
			camas1Form.setAccion("iniciarBusqueda");
		}
		camas1Form.setCol(mundoCamas1.listadoCamas1(con));
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param camas1Form Camas1Form
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "convenio.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	Camas1Form camas1Form, 
																ActionMapping mapping, 
																Connection con, UsuarioBasico user) throws SQLException
	{
		//Limpiamos lo que venga del form
		camas1Form.reset();
		Camas1 mundoCamas1= new Camas1();
		
		//1) Se consultan las camas-----------------------------------------------------
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		mundoCamas1.setCentroAtencion(user.getCodigoCentroAtencion());
		camas1Form.setCodigoCentroAtencion(user.getCodigoCentroAtencion());
		camas1Form.setCol(mundoCamas1.listadoCamas1(con));
		
		//2) Se prepara el estado inicial --------------------------------------------
		camas1Form.setEstado("ingresarModificar");
		
		//3) Se cargan las estructuras para los combos---------------------
		camas1Form.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, user.getCodigoInstitucionInt(),user.getCodigoCentroAtencion()));
		camas1Form.setTiposHabitaciones(UtilidadesManejoPaciente.obtenerTiposHabitaciones(con, user.getCodigoInstitucionInt()));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Metodo que especifica las acciones a realizar en el estado ingresarModificar
	 * @param camas1Form
	 * @param mapping
	 * @param con
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionIngresarModificar(	Camas1Form camas1Form, 
																			ActionMapping mapping, 
																			Connection con, UsuarioBasico user) throws SQLException
	{
		Camas1 mundoCamas1= new Camas1();
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		//camas1Form.setCol(mundoCamas1.listadoCamas1(con));
		logger.info("llega para ir a principal");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarNueva.
	 * Se copian las propiedades del objeto camas1
	 * en el objeto mundo
	 * 
	 * @param camas1Form Camas1Form
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "camas1.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionGuardarNueva(	Camas1Form camas1Form,
															 			ActionMapping mapping,
															 			HttpServletRequest request, 
															 			UsuarioBasico user,
															 			Connection con) throws SQLException
	{
		Camas1 mundoCamas1=new Camas1 ();  
		llenarMundo(camas1Form, mundoCamas1,user);
		boolean inserto=false;
		
		
		//Se verifica si la cama ya existe para el mismo centro de atencion
		if(Camas1.existeCamaEnCentroAtencion(con,mundoCamas1.getHabitacion(),mundoCamas1.getNumeroCama(),user.getCodigoInstitucionInt(),mundoCamas1.getCentroCosto().getCodigo()))
		{
			ActionErrors errores = new ActionErrors();
		 	errores.add("actualización Camas1", new ActionMessage("errors.yaExisten","La habitación y número de cama"));
			logger.warn("error en la actualización de los datos de la Cama");
			saveErrors(request, errores);	
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		
		
		inserto=mundoCamas1.insertarCama1Transaccional(con, ConstantesBD.inicioTransaccion);
		
		if(!inserto)
		{
		    ActionErrors errores = new ActionErrors();
		 	errores.add("actualización Camas1", new ActionMessage("errors.ingresoDatos","los datos de la Cama (Transacción)"));
			logger.warn("error en la actualización de los datos de la Cama");
			saveErrors(request, errores);	
			mundoCamas1.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		int codigoCamaTemp=mundoCamas1.getCodigo();
		if(codigoCamaTemp<=0)
		{
		    ActionErrors errores = new ActionErrors();
			errores.add("actualización Camas1", new ActionMessage("errors.ingresoDatos","los datos de la Cama (Transacción)"));
			logger.warn("error en la cargar los datos de ls secuencia  Camas1");
			saveErrors(request, errores);	
			mundoCamas1.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		camas1Form.setCodigo(codigoCamaTemp);
		logger.info("********************VAMOS A INSERTAR UNA NUEVA CAMA******************************");
		inserto=this.guardarServiciosCama(con, camas1Form);
		if(!inserto)
		{
		    ActionErrors errores = new ActionErrors();
			errores.add("actualización Camas1", new ActionMessage("errors.ingresoDatos","los datos de la Cama (Transacción)"));
			logger.warn("error en la actualización de los SERVICIOS de la Cama");
			saveErrors(request, errores);	
			mundoCamas1.abortarTransaccion(con);
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		camas1Form.setEstado("resumen");
		//*********************************************************
		consultarNombreCentroAtencion(con,camas1Form);
		//************************************************************
		mundoCamas1.terminarTransaccion(con);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	
	/**
	 * Método implementado para consultar el nombre del centro de atencion
	 * @param con
	 * @param camas1Form
	 */
	private void consultarNombreCentroAtencion(Connection con, Camas1Form camas1Form) 
	{
		try
		{
			//Se carga informacion del centro de atencion
			CentroAtencion centroAtencion = new CentroAtencion();
			centroAtencion.consultar(con,camas1Form.getCodigoCentroAtencion());
			camas1Form.setNombreCentroAtencion(centroAtencion.getDescripcion());
		}
		catch(Exception e)
		{
			camas1Form.setNombreCentroAtencion("");
		}
		
		
	}

	/**
	 * Metodo que se encarga de insertar el - los servicio(s) de una cama 
	 * @param con
	 * @param camas1Form
	 * @param user
	 * @return
	 */
	private boolean guardarServiciosCama (Connection con, Camas1Form camas1Form)
	{
		logger.info("Entró a guardarServiciosCama ************************************************");
	    int numeroFilasReal=0;
	    int temporalCodigoServico=0;
	    int temporalCodigoTipoMonitoreo=0;
	    String temporalServicio="";
	    String temporalTipoMonitoreo="";
	    boolean inserto=false;
	    if(camas1Form.getEsUci()==0)
	        numeroFilasReal= camas1Form.getNumeroFilasMapaCasoNoUci();
	    else if(camas1Form.getEsUci()==1)
	        numeroFilasReal= camas1Form.getNumeroFilasMapa();
	    logger.info("Numero de Filas=> "+numeroFilasReal);
	    for(int x=0; x<numeroFilasReal; x++)
	    {
	        try
	        {
	        	temporalServicio=camas1Form.getServiciosCama1Map("codigoServicio_"+x)+"";
	        	if(temporalServicio.equals("")||temporalServicio.equals("null"))
	        		temporalCodigoServico=-1;
	        	else
	        		temporalCodigoServico=Integer.parseInt(temporalServicio);
	        }
	        catch(NumberFormatException e)
	        {
	            logger.warn("error en la actualización de los SERVICIOS de la Cama el codigo del servicio es nulo index=="+x);
	            return false;
	        }
	        try
	        {
	            if(camas1Form.getEsUci()==1)
	            {
	            	temporalTipoMonitoreo=camas1Form.getServiciosCama1Map("codigoTipoMonitoreo_"+x)+"";
	            	if(temporalTipoMonitoreo.equals("")||temporalTipoMonitoreo.equals("null"))
	            		temporalCodigoTipoMonitoreo=-1;
	                else
	                	temporalCodigoTipoMonitoreo=Integer.parseInt(temporalTipoMonitoreo);
	            }
	        }
	        catch(NumberFormatException e)
	        {
	            logger.warn("error en la actualización de los SERVICIOS de la Cama el codigo del tipo de monitoreo es nulo index=="+x);
	            return false;
	        }
	        try
	        {
	            ServiciosCamas1 mundoServiciosCamas1 = new ServiciosCamas1();
	            llenarMundoServiciosCamas1(temporalCodigoServico, temporalCodigoTipoMonitoreo, camas1Form, mundoServiciosCamas1);
	            inserto=mundoServiciosCamas1.insertarServiciosCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
	            if(!inserto)
	            {
	                logger.warn("error en la actualización de los SERVICIOS de la Cama no se pudo hacer el insert retorno false "+x);
		            return false;
	            }
	        }
	        catch (SQLException e)
	        {
	            logger.warn("error en la actualización de los SERVICIOS de la Cama no se pudo hacer el insert "+x);
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Llena los datos pertienentes del mundo servicioscamas1 
	 * @param temporalCodigoServico
	 * @param temporalCodigoTipoMonitoreo
	 * @param camas1Form
	 * @param mundoServiciosCamas1
	 * @param user
	 */
	protected void llenarMundoServiciosCamas1(	int temporalCodigoServico, int temporalCodigoTipoMonitoreo, 
	        															Camas1Form camas1Form, ServiciosCamas1 mundoServiciosCamas1)
	{
	    mundoServiciosCamas1.setCodigoCama(camas1Form.getCodigo());
	    mundoServiciosCamas1.setServicio(new InfoDatosInt(temporalCodigoServico, ""));
	    mundoServiciosCamas1.setTipoMonitoreo(new InfoDatosInt(temporalCodigoTipoMonitoreo,""));
	}
		
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form Camas1Form para el mundo de Camas1
	 * @param camas1Form Camas1Form (forma)
	 * @param mundoCamas1 Camas1 (mundo)
	 */
	protected void llenarMundo(Camas1Form camas1Form, Camas1 mundoCamas1, UsuarioBasico user)
	{
	    try
	    {
	        mundoCamas1.setCodigo(camas1Form.getCodigo());
	        mundoCamas1.setHabitacion(Integer.parseInt(camas1Form.getHabitacionStr()));
	    }
	    catch(NumberFormatException e)
	    {
	        mundoCamas1.setHabitacion(ConstantesBD.codigoNuncaValido);
	    }
		mundoCamas1.setNumeroCama(camas1Form.getNumeroCama());
		mundoCamas1.setDescripcionCama(camas1Form.getDescripcionCama());
		mundoCamas1.setEstadoCama(new InfoDatosInt(camas1Form.getCodigoEstadoCama(), camas1Form.getNombreEstadoCama()));
		if(camas1Form.getEsUci()==1)
		    mundoCamas1.setEsUci(true);
		else if (camas1Form.getEsUci()==2)
		    mundoCamas1.setEsUci(false);
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		mundoCamas1.setTipoUsuarioCama(new InfoDatosInt(camas1Form.getCodigoTipoUsuarioCama(), ""));
		mundoCamas1.setCentroCosto(new InfoDatosInt(camas1Form.getCodigoCentroCosto(), ""));
		mundoCamas1.setCentroAtencion(camas1Form.getCodigoCentroAtencion());
		mundoCamas1.setPiso(camas1Form.getPiso());
		mundoCamas1.setTipoHabitacion(camas1Form.getTipoHabitacion());
		mundoCamas1.setAsignableAdmision(camas1Form.getAsignableAdmision());
	}
	
	/**
	 * Metodo que especifica las acciones a realizar para el ordenamiento  
	 * @param camas1Form
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenar(	Camas1Form camas1Form,
															ActionMapping mapping,
															HttpServletRequest request, 
															Connection con) throws SQLException
	{
		try
		{
			camas1Form.setCol(Listado.ordenarColumna(new ArrayList(camas1Form.getCol()),camas1Form.getUltimaPropiedad(),camas1Form.getColumna()));
			camas1Form.setUltimaPropiedad(camas1Form.getColumna());
			UtilidadBD.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de camas1 ");
			UtilidadBD.cerrarConexion(con);
			camas1Form.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado camas1");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		camas1Form.setAccion("");
		return mapping.findForward("principal")	;
	}	
		
	/**
	 * Metodo que especifica las acciones a realizar en el estado resumen
	 * @param camas1Form
	 * @param mapping
	 * @param con
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResumenOModificar(	Camas1Form camas1Form, 
																				ActionMapping mapping, 
																				Connection con, UsuarioBasico user) throws SQLException
	{
		Camas1 mundoCamas1= new Camas1();
		///se llenan los datos para cargar el detalle
		mundoCamas1.setCodigo(camas1Form.getCodigo());
		mundoCamas1.detalleCama1(con);
		camas1Form.setMostrarVentanaInsertarNueva(false);
		llenarForm(camas1Form, mundoCamas1);
		//se consulta el nombre del centro de atencion
		camas1Form.setNombreCentroAtencion(Utilidades.obtenerNombreCentroAtencion(con, camas1Form.getCodigoCentroAtencion()));
		
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		//camas1Form.setCol(mundoCamas1.listadoCamas1(con));
		
		//se carga el detalle de los servicios
		ServiciosCamas1 mundoServiciosCamas1 = new ServiciosCamas1();
		llenarMundoServiciosCamas1(-1,-1, camas1Form, mundoServiciosCamas1);
		if(camas1Form.getEsUci()==0)
		{
		    //cargamos el mapa para mostrarlo
		    camas1Form.setServiciosCama1Map(mundoServiciosCamas1.cargarServicioCama1CasoNoEsUci(con));
		    //cargamos otro mapa con la info original de la BD
		    camas1Form.setTempBDServiciosCama1Map(mundoServiciosCamas1.cargarServicioCama1CasoNoEsUci(con));
		    camas1Form.setEsUciAntesDeModificar(camas1Form.getEsUci());
		}    
		else if(camas1Form.getEsUci()==1)
		{
		    //cargamos el mapa para mostrarlo
		    camas1Form.setServiciosCama1Map(mundoServiciosCamas1.cargarServiciosCama1CasoEsUci(con));
		    //cargamos otro mapa con la info original de la BD
		    camas1Form.setTempBDServiciosCama1Map(mundoServiciosCamas1.cargarServiciosCama1CasoEsUci(con));
		    camas1Form.setEsUciAntesDeModificar(camas1Form.getEsUci());
		}    
		logger.info("\n\n Mapa resultante de la consulta del detalle===="+camas1Form.getServiciosCama1Map());
		
		//en caso de que sea modificacion cargo el numerodefilasMapaAntesModificar, para posteriormente saber cuantos tengo
		//que actualizar y cuales son nuevos, o si debo eliminar.
		camas1Form.setNumeroFilasMapaAntesModificar(camas1Form.getNumeroFilasMapa());
		
		//Se cargan las estructuras
		camas1Form.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, user.getCodigoInstitucionInt(), camas1Form.getCodigoCentroAtencion()));
		
		if(!camas1Form.getPiso().equals("")&&Integer.parseInt(camas1Form.getPiso())>0)
			camas1Form.setHabitaciones(UtilidadesManejoPaciente.obtenerHabitaciones(con, user.getCodigoInstitucionInt(), camas1Form.getCodigoCentroAtencion(), Integer.parseInt(camas1Form.getPiso())));
		camas1Form.setTiposHabitaciones(UtilidadesManejoPaciente.obtenerTiposHabitaciones(con, user.getCodigoInstitucionInt()));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param camas1Form (form)
	 * @param mundoCamas1 (mundo)
	 */
	protected void llenarForm(Camas1Form camas1Form, Camas1 mundoCamas1)
	{
	    camas1Form.setCodigo(mundoCamas1.getCodigo());
	    camas1Form.setHabitacionStr(mundoCamas1.getHabitacion()+"");
	    camas1Form.setNombreHabitacion(mundoCamas1.getNombreHabitacion());
	    camas1Form.setPiso(mundoCamas1.getPiso());
	    camas1Form.setNombrePiso(mundoCamas1.getNombrePiso());
	    camas1Form.setTipoHabitacion(mundoCamas1.getTipoHabitacion());
	    camas1Form.setNumeroCama(mundoCamas1.getNumeroCama());
	    camas1Form.setDescripcionCama(mundoCamas1.getDescripcionCama());
	    camas1Form.setCodigoEstadoCama(mundoCamas1.getEstadoCama().getCodigo());
	    camas1Form.setNombreEstadoCama(mundoCamas1.getEstadoCama().getNombre());
	    camas1Form.setCodigoTipoUsuarioCama(mundoCamas1.getTipoUsuarioCama().getCodigo());
	    camas1Form.setNombreTipoUsuarioCama(mundoCamas1.getTipoUsuarioCama().getNombre());
	    camas1Form.setCodigoCentroCosto(mundoCamas1.getCentroCosto().getCodigo());
	    camas1Form.setNombreCentroCosto(mundoCamas1.getCentroCosto().getNombre());
	    camas1Form.setPuedoModificarEstadoCama(mundoCamas1.getPuedoModificarEstadoCama());
	    camas1Form.setAsignableAdmision(mundoCamas1.getAsignableAdmision());
	    if(mundoCamas1.getEsUci())
	        camas1Form.setEsUci(1);
	    else
	        camas1Form.setEsUci(0);
	    camas1Form.setCodigoCentroAtencion(mundoCamas1.getCentroAtencion());
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificar.
	 * Se copian las propiedades del objeto camas1
	 * en el objeto mundo
	 * 
	 * @param camas1Form Camas1Form
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "camas1.do?"
	 * @throws SQLException
	*/
	private ActionForward accionGuardarModificar(	Camas1Form camas1Form,
																 			ActionMapping mapping,
																 			HttpServletRequest request, 
																 			UsuarioBasico user,
																 			Connection con) throws SQLException
	{
		
		Camas1 mundoCamaAnterior = new Camas1();
		mundoCamaAnterior.setCodigo(camas1Form.getCodigo());
		mundoCamaAnterior.detalleCama1(con);
		
		Camas1 mundoCamas1=new Camas1 ();  
		llenarMundo(camas1Form, mundoCamas1,user);
		boolean modifico=false;
		
	
		modifico = fueModificadaCama(mundoCamaAnterior,camas1Form);
		if(modifico)
		{
			modifico=mundoCamas1.modificarCamas1Transaccional(con, ConstantesBD.inicioTransaccion);
			
			if(!modifico)
			{
			    ActionErrors errores = new ActionErrors();
				errores.add("actualización Camas1", new ActionMessage("errors.ingresoDatos","los datos de la Cama (Transacción)"));
				logger.warn("error en la actualización de los datos de la Cama");
				saveErrors(request, errores);	
				mundoCamas1.abortarTransaccion(con);
				UtilidadBD.cerrarConexion(con);									
				return mapping.findForward("principal");
			}
			modifico=this.guardarModificarServiciosCama(con, camas1Form);
			if(!modifico)
			{
			    ActionErrors errores = new ActionErrors();
				errores.add("eliminación Camas1", new ActionMessage("errors.ingresoDatos","los datos de la Cama (Transacción)"));
				logger.warn("error en la eliminación de los SERVICIOS de la Cama");
				saveErrors(request, errores);	
				mundoCamas1.abortarTransaccion(con);
				UtilidadBD.cerrarConexion(con);									
				return mapping.findForward("principal");
			}
			camas1Form.setEstado("resumen");
			mundoCamas1.terminarTransaccion(con);
			
			
			
			if(modifico)
			{
				mundoCamas1.detalleCama1(con);
				this.generarLog(con,mundoCamaAnterior,mundoCamas1,camas1Form,user);
			}
		}
		//**************************************************
		consultarNombreCentroAtencion(con,camas1Form);
		
		//***************************************************
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	
	/**
	 * Método implementado para generar log de modificación
	 * @param con 
	 * @param mundoCamaAnterior
	 * @param mundoCamas1 
	 * @param camas1Form
	 * @param user
	 */
	private void generarLog(Connection con, Camas1 mundoCamaAnterior, Camas1 mundoCamas1, Camas1Form camas1Form, UsuarioBasico user) 
	{
		String log = "";
		
		 log="\n            ====INFORMACION ORIGINAL TIPO DE LA CAMA===== " +
		 	"\n*  Código Cama ["+mundoCamaAnterior.getCodigo()+"] "+
			"\n*  Centro Atención [" +Utilidades.obtenerNombreCentroAtencion(con,mundoCamaAnterior.getCentroAtencion())+"] "+
			"\n*  Piso [" +mundoCamaAnterior.getNombrePiso()+"] "+
			"\n*  Habitación [" +mundoCamaAnterior.getNombreHabitacion()+"] "+
			"\n*  Tipo Habitación [" +mundoCamaAnterior.getTipoHabitacion()+"] "+
			"\n*  Descripción Cama ["+mundoCamaAnterior.getDescripcionCama()+"] " +
			"\n*  Estado Cama ["+mundoCamaAnterior.getEstadoCama().getNombre()+"] " +
			"\n*  Tipo Usuario ["+mundoCamaAnterior.getTipoUsuarioCama().getNombre()+"] " +
			"\n*  Centro Costo ["+mundoCamaAnterior.getCentroCosto().getNombre()+"] " +
			"\n*  es UCI ? ["+(mundoCamaAnterior.getEsUci()?"Sí":"No")+"] " ;
		 
		if(mundoCamaAnterior.getEsUci())
		{
			int numRegistros = camas1Form.getTempBDServiciosCama1Map().size()/5;
			for(int i=0;i<numRegistros;i++)
			{
				log+="\n*  Servicio N° "+(i+1)+" ["+camas1Form.getTempBDServiciosCama1Map("codigoServicio_"+i)+" "+camas1Form.getTempBDServiciosCama1Map("nombreServicio_"+i)+"] "+
					"\n*  Tipo Monitoreo N° "+(i+1)+" ["+camas1Form.getTempBDServiciosCama1Map("nombreTipoMonitoreo_"+i)+"] ";
			}
		}
		else
		{
			log+="\n*  Servicio ["+camas1Form.getTempBDServiciosCama1Map("codigoServicio_0")+" "+camas1Form.getTempBDServiciosCama1Map("nombreServicio_0")+"] ";
		}
		 
			
	  log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DE LA CAMA===== " +
	  "\n*  Código Cama ["+camas1Form.getCodigo()+"] "+
		"\n*  Centro Atención [" +Utilidades.obtenerNombreCentroAtencion(con,mundoCamas1.getCentroAtencion())+"] "+
		"\n*  Piso [" +mundoCamas1.getNombrePiso()+"] "+
		"\n*  Habitación [" +mundoCamas1.getNombreHabitacion()+"] "+
		"\n*  Tipo Habitación [" +mundoCamas1.getTipoHabitacion()+"] "+
		"\n*  Descripción Cama ["+mundoCamas1.getDescripcionCama()+"] " +
		"\n*  Estado Cama ["+mundoCamas1.getEstadoCama().getNombre()+"] " +
		"\n*  Tipo Usuario ["+mundoCamas1.getTipoUsuarioCama().getNombre()+"] " +
		"\n*  Centro Costo ["+mundoCamas1.getCentroCosto().getNombre()+"] " +
		"\n*  es UCI ? ["+(mundoCamas1.getEsUci()?"Sí":"No")+"] " ;
		
		  
	  if(mundoCamas1.getEsUci())
		{
			int numRegistros = camas1Form.getServiciosCama1Map().size()/5;
			for(int i=0;i<numRegistros;i++)
			{
				log+="\n*  Servicio N° "+(i+1)+" ["+camas1Form.getServiciosCama1Map("codigoServicio_"+i)+" "+camas1Form.getServiciosCama1Map("nombreServicio_"+i)+"] "+
					"\n*  Tipo Monitoreo N° "+(i+1)+" ["+camas1Form.getServiciosCama1Map("nombreTipoMonitoreo_"+i)+"] ";
			}
		}
		else
		{
			log+="\n*  Servicio ["+camas1Form.getServiciosCama1Map("codigoServicio_0")+" "+camas1Form.getServiciosCama1Map("nombreServicio_0")+"] ";
				
		}
	  	log += "\n\n" ;
		
	  	LogsAxioma.enviarLog(ConstantesBD.logModificacionCamaCodigo,log,ConstantesBD.tipoRegistroLogModificacion,user.getLoginUsuario());
		
	}

	/**
	 * Método usado para verificar si una cama fue modificada
	 * @param mundoCamaAnterior
	 * @param camas1Form
	 * @return
	 */
	private boolean fueModificadaCama(Camas1 mundoCamaAnterior, Camas1Form camas1Form) 
	{
		boolean fueModificada = false;
		
		if(mundoCamaAnterior.getCentroAtencion()!=camas1Form.getCodigoCentroAtencion())
			fueModificada = true;
		if(!mundoCamaAnterior.getDescripcionCama().equals(camas1Form.getDescripcionCama()))
			fueModificada = true;
		if(mundoCamaAnterior.getHabitacion()!=Integer.parseInt(camas1Form.getHabitacionStr()))
			fueModificada = true;
		if(mundoCamaAnterior.getEstadoCama().getCodigo()!=camas1Form.getCodigoEstadoCama())
			fueModificada = true;
		if(mundoCamaAnterior.getTipoUsuarioCama().getCodigo()!=camas1Form.getCodigoTipoUsuarioCama())
			fueModificada = true;
		if(mundoCamaAnterior.getCentroCosto().getCodigo()!=camas1Form.getCodigoCentroCosto())
			fueModificada = true;
		
		if(mundoCamaAnterior.getAsignableAdmision()!=camas1Form.getAsignableAdmision())
			fueModificada = true;
		
		if(mundoCamaAnterior.getEsUci()!=UtilidadTexto.getBoolean(camas1Form.getEsUci()+""))
			fueModificada = true;
		else
		{
			if(!UtilidadTexto.getBoolean(camas1Form.getEsUci()+""))
			{
				if(Integer.parseInt(camas1Form.getServiciosCama1Map("codigoServicio_0").toString())!=Integer.parseInt(camas1Form.getTempBDServiciosCama1Map("codigoServicio_0").toString()))
					fueModificada = true;
			}
			else
			{
				if((camas1Form.getServiciosCama1Map().size()/5)!=(camas1Form.getTempBDServiciosCama1Map().size()/5))
					fueModificada = true;
				else
				{
					int numRegistros = camas1Form.getServiciosCama1Map().size() / 5;
					for(int i=0;i<numRegistros;i++)
					{
						if(Integer.parseInt(camas1Form.getServiciosCama1Map("codigoTipoMonitoreo_"+i).toString())!=Integer.parseInt(camas1Form.getTempBDServiciosCama1Map("codigoTipoMonitoreo_"+i).toString())||
								Integer.parseInt(camas1Form.getServiciosCama1Map("codigoServicio_"+i).toString())!=Integer.parseInt(camas1Form.getTempBDServiciosCama1Map("codigoServicio_"+i).toString()))
							fueModificada = true;
					}
				}
				
			}
				
		}
		
		return fueModificada;
	}

	/**
	 * Metodo que se encarga de modificar - actualizar - eliminar el - los servicio(s) de una cama 
	 * @param con
	 * @param camas1Form
	 * @param user
	 * @return
	 */
	private boolean guardarModificarServiciosCama (Connection con, Camas1Form camas1Form)
	{
	    int numeroFilasRealMapaOriginalBD=0;
	    int temporalCodigoTablaServiciosCama=0;
	    boolean elimino=false;
	    if(camas1Form.getEsUciAntesDeModificar()==0)
	        numeroFilasRealMapaOriginalBD= camas1Form.getNumeroFilasMapaCasoNoUciTemporal();
	    else if(camas1Form.getEsUciAntesDeModificar()==1)
	        numeroFilasRealMapaOriginalBD= camas1Form.getNumeroFilasMapaTemporal();
	    
	    //modificación para verificar que si el numero de filas es 0
	    //es que no se ha ingresado servicio a la cama
	    logger.info("Número de Filas Modificar=> "+numeroFilasRealMapaOriginalBD);
	    if(numeroFilasRealMapaOriginalBD==0)
	    	elimino=true;
	    // en este punto se hace eliminacion de todos los servicios para luego nuevamente insertarlos,
        for(int x=0; x<numeroFilasRealMapaOriginalBD; x++)
	    {
	        try
	        {
	            temporalCodigoTablaServiciosCama=Integer.parseInt(camas1Form.getTempBDServiciosCama1Map("codigoTablaServiciosCama_"+x)+"");
	        }
	        catch(NumberFormatException e)
	        {
	            logger.warn("error en la actualización de los SERVICIOS de la Cama el codigo del servicio es nulo index=="+x);
	            return false;
	        }
	        try
	        {
	            ServiciosCamas1 mundoServiciosCamas1 = new ServiciosCamas1();
	            //llenamos los datos necesarios para el eliminar 
	            mundoServiciosCamas1.setCodigo(temporalCodigoTablaServiciosCama);
	            elimino=mundoServiciosCamas1.eliminarServiciosCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
	            if(!elimino)
	            {
	                logger.warn("error en la eliminación de los SERVICIOS de la Cama no se pudo hacer el delete retorno false "+x);
	               return false;
	            }
	        }
	        catch (SQLException e)
	        {
	            logger.warn("error en la eliminación de los SERVICIOS de la Cama no se pudo hacer el delete "+x);
	            return false;
	        }
	    }
        //finalmente se vuelve ha hacer el insert, //NOTA no se hizo un update porque en caso de que sea UCI se pueden tener 
        //N servicios y tambien variaba el tamanio del mapa con el tipo de monitoreo, nunca existia una equivalencia exacta del
        //tamanio de los dos mapas
	    if(elimino)
	        return this.guardarServiciosCama(con, camas1Form);
	    else
	        return false;
	}
	
	/**
	 * Metodo que especifica las acciones a realizar en el estado modificar
	 * @param camas1Form
	 * @param mapping
	 * @param con
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionModificar(	Camas1Form camas1Form, 
																ActionMapping mapping, 
																Connection con, UsuarioBasico user) throws SQLException
	{
		Camas1 mundoCamas1= new Camas1();
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		camas1Form.setCol(mundoCamas1.listadoCamas1(con));
		logger.info("llega para ir a principal");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Metodo que especifica las acciones a realizar en el estado ingresarModificar
	 * @param camas1Form
	 * @param mapping
	 * @param con
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	Camas1Form camas1Form, 
																				ActionMapping mapping, 
																				Connection con, UsuarioBasico user) throws SQLException
	{
		Camas1 mundoCamas1= new Camas1();
		mundoCamas1.setCodigoInstitucion(user.getCodigoInstitucionInt());
		mundoCamas1.setCentroAtencion(user.getCodigoCentroAtencion());
		if (camas1Form.getAccion().equals("realizarBusqueda"))
		{
		    llenarMundo(camas1Form, mundoCamas1, user);
		    camas1Form.setCol(mundoCamas1.busquedaAvanzada(con, camas1Form.getEsUci(), camas1Form.getCodigoServicioStr(), camas1Form.getDescripcionServicio()));
		    camas1Form.reset();
		    
		    camas1Form.setAccion("");
		}
		else
		{    
		    camas1Form.reset();
		    
		    camas1Form.setCol(mundoCamas1.listadoCamas1(con));
			camas1Form.setAccion("iniciarBusqueda");
		}  
		camas1Form.setCodigoCentroAtencion(mundoCamas1.getCentroAtencion());
	    camas1Form.setPisos(UtilidadesManejoPaciente.obtenerPisos(con,user.getCodigoInstitucionInt(), camas1Form.getCodigoCentroAtencion()));
	    camas1Form.setTiposHabitaciones(UtilidadesManejoPaciente.obtenerTiposHabitaciones(con, user.getCodigoInstitucionInt()));
		
		
		logger.info("llega para ir a principal");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Metodo que especifica las acciones a realizar en el estado cambiarEstadoCamaDesinfeccion
	 * @param camas1Form
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCambiarEstadoCamaDesinfeccion(	Camas1Form camas1Form, 
																									ActionMapping mapping, 
																									Connection con) throws SQLException
	{
	    logger.info("El mapa de camas =======>"+camas1Form.getServiciosCama1Map());
	    Camas1 mundoCamas1= new Camas1();
	    boolean yaComenzoTransaccion=false;
	    int camasLiberadas = 0;
	    
	    for(int i=0; i<camas1Form.getNumeroCamasPosibleLiberacion(); i++)
		{
			if( UtilidadTexto.getBoolean(camas1Form.getServiciosCama1Map("seleccion_"+i)+"") )
			{
			    try
			    {
				    mundoCamas1.setCodigo(Integer.parseInt(camas1Form.getServiciosCama1Map("codigo_"+i).toString()));
				    
				    mundoCamas1.detalleCama1(con);
				    mundoCamas1.setEstadoCama(new InfoDatosInt(ConstantesBD.codigoEstadoCamaDisponible));
				    if(!yaComenzoTransaccion)
				    {    
				        mundoCamas1.modificarCamas1Transaccional(con, ConstantesBD.inicioTransaccion);
				    }    
				    else
				    {
				        mundoCamas1.modificarCamas1Transaccional(con, ConstantesBD.continuarTransaccion);
				    }    
			    }
			    catch(NumberFormatException e)
			    {
			        logger.warn("el codigo cargado de la cama no es int "+e);	
			    }    
			    catch(SQLException sqle)
			    {
			        logger.warn("error actualizando el estado de la cama "+sqle);	
			    }
			    camasLiberadas ++;
			}
		}
	    //SE guarda el número de camas liberadas
	    camas1Form.setServiciosCama1Map("numCamasLiberadas", camasLiberadas);
	    
	    mundoCamas1.terminarTransaccion(con);
		UtilidadBD.cerrarConexion(con);	
	    return mapping.findForward("paginaResumen");
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