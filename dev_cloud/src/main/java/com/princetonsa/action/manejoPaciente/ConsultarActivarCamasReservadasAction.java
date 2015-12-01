package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ConsultarActivarCamasReservadasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultarActivarCamasReservadas;
import com.princetonsa.pdf.ConsultarActivarCamasReservadasPdf;



/**
 * @author Jhony Alexander Duque A,
 * jduque@princetonsa.com
 */
public class ConsultarActivarCamasReservadasAction extends Action
{
	/**
	 * Para manjar los logger de la clase ConsultarActivarCamasReservadasAction
	 */
	Logger logger = Logger.getLogger(ConsultarActivarCamasReservadasAction.class);
	
	
	
	
	public ActionForward execute (ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception
{
		Connection connection = null;
		try{
		if (form instanceof ConsultarActivarCamasReservadasForm)
		{
			
			
			connection = UtilidadBD.abrirConexion();
			
			//se verifica si la conexion esta nula
			if (connection == null)
			{
				// de ser asi se envia a una pagina de error. 
				request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				return mapping.findForward("paginaError");
			}
//			se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//optenemos el valor de la forma.
			ConsultarActivarCamasReservadasForm forma = (ConsultarActivarCamasReservadasForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			 permisoRol(connection, forma, usuario);
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE ConsultarActivarCamasReservadasForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 					ESTADOS PARA LA CONSULTA DE CAMAS RESERVADAS			
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
				
				logger.warn("Estado no Valido dentro del Flujo de Censo de Camas (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
			else
				if (estado.equals("inicial"))
				{ 
					//se limpia el paciente en sesion para la busqueda.
					paciente.setCodigoPersona(0);
					UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);
					logger.info("si entre al inicial");
					//limpiamos el pager
					forma.resetPager();
								
					return this.initBusquedaCamasReservadas(connection, forma, mapping, usuario);
				}
			//****************ESTADOS PARA LOS FILTROS AJAX*******************************************
				else
					if (estado.equals("filtroCentroAtencion"))
					{
						return this.accionFiltrar(connection, forma, response, usuario);
					}
			//*****************************************************************************************
					else
						if (estado.equals("consultarcamasreservadas"))
						{ 	
							paciente.setCodigoPersona(0);
							UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);
							return this.buscarCamasReservadas(connection, forma, mapping,usuario);
						}
			
						else/*----------------------------------------------
							 * ESTADO =================>>>>>  ORDENAR
							 ---------------------------------------------*/
							if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap de la consulta de camas reservadas.
							{
								
								forma.setConsultarActivarCamasReservadasMAP(this.accionOrdenarMapa(forma.getConsultarActivarCamasReservadasMAP(),forma));				 
								UtilidadBD.closeConnection(connection);
								return mapping.findForward("principal");	
						
							}
							else /*----------------------------------------------
								 * ESTADO =================>>>>>  REDIRECCION
								 ---------------------------------------------*/
								if (estado.equals("redireccion"))
								{
									UtilidadBD.closeConnection(connection);
									response.sendRedirect(forma.getLinkSiguiente());
									return null;
								}
								else/*----------------------------------------------
									 * ESTADO =================>>>>>  ACTIVARCAMA
									 ---------------------------------------------*/
									if (estado.equals("activarcama"))
									{
									
										return this.cargaDatosReserva(connection, forma, mapping, usuario,paciente,request);
									}
									else/*----------------------------------------------
										 * ESTADO =================>>>>>  VOLVER
										 ---------------------------------------------*/
										if (estado.equals("volver"))
										{
											
											return this.volver(forma, mapping);
										}
										else/*----------------------------------------------
											 * ESTADO =================>>>>>  GUARDARCANCELACION
											 ---------------------------------------------*/
											if (estado.equals("guardar"))
											{
												return this.cancelarReserva(connection, forma, usuario, mapping);
											}
											else/*----------------------------------------------
												 * ESTADO =================>>>>>  IMPRIMIR
												 ---------------------------------------------*/
												if (estado.equals("imprimir"))
												{
													return this.accionImprimir(mapping, request, connection, forma, usuario);
												}
			
			
			
			
			
			
			/*-------------------------------------------------------------------
			 * 			FIN ESTADOS PARA LA CONSULTA DE CAMAS RESERVADAS
			 -------------------------------------------------------------------*/
			
			
			
			
		}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	
	/**
	 * Metodo encargado de Verificar si el usuario tiene un rol espesifico
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public void permisoRol(Connection  connection,ConsultarActivarCamasReservadasForm forma,UsuarioBasico usuario)
	{
		
		forma.setPermisoModificar(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(),639));
	
		//logger.info("\n\n *************** entro a permisoRol y es"+forma.isPermisoModificar());
	}
	
	
	
	
	/**
	 * Metodo encargado de imprimir el reporte de las camas Reservadas
	 * @param mapping
	 * @param request
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimir(ActionMapping mapping, HttpServletRequest request, Connection connection, ConsultarActivarCamasReservadasForm forma, UsuarioBasico usuario)
	{
		
		String nombreArchivo;

		Random r= new Random();
		nombreArchivo= "/consultarActivarCamasReservadas"+ r.nextInt() + ".pdf";
		
		ConsultarActivarCamasReservadasPdf.imprimir(connection, ValoresPorDefecto.getFilePath()+nombreArchivo, forma, usuario, request);


		UtilidadBD.closeConnection(connection);
		
	
			request.setAttribute("nombreArchivo",nombreArchivo);
			request.setAttribute("nombreVentana", "LISTADO DE RESERVAS DE CAMAS");
			return mapping.findForward("abrirPdf");
		
	}
	
	
	
	
	/**
	 * Metodo encargado de volver a la consulta, 
	 * ademas de colocar en el Hashmap el estado de 
	 * caselado a la reserva si se cancelo.
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public ActionForward volver (ConsultarActivarCamasReservadasForm forma,ActionMapping mapping)
	{
		if (forma.isOperacionTrue())
	
			forma.setConsultarActivarCamasReservadasMAP("estadoreserva_"+forma.getIndexConsultarActivarCamasReservadasMAP(), "CAN");
	
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de cancelar la reserva de camas.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	public ActionForward cancelarReserva (Connection connection, ConsultarActivarCamasReservadasForm forma,UsuarioBasico usuario,ActionMapping mapping)
	{
		HashMap parametros = new HashMap ();
	
		
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		parametros.put("motivocancelacion", forma.getMotivoCanselacion());
		parametros.put("codigoreserva", forma.getDatosReservaMap("codigoreserva"));
		parametros.put("codigocama", forma.getDatosCamaMap("codigocama"));
		forma.setOperacionTrue(ConsultarActivarCamasReservadas.cancelarReserva(connection, parametros));
		
		if (forma.isOperacionTrue())
		{
			
			
			parametros.put("codigopaciente", forma.getDatosReservaMap("codigopaciente"));
			parametros.put("codigocentroatencion", forma.getDatosReservaMap("codigocentroatencion"));
			parametros.put("codigocentrocosto", forma.getDatosCamaMap("centrocosto"));
			parametros.put("codigopiso", forma.getDatosCamaMap("codigopisoint"));//forma.getConsultarActivarCamasReservadasMAP("codigo_"+forma.getIndexConsultarActivarCamasReservadasMAP()));
			parametros.put("codigohabitacion", forma.getDatosCamaMap("codigohabitacion"));
			parametros.put("codigocama", forma.getDatosCamaMap("codigocama"));
			parametros.put("codigoreservacancelada", forma.getDatosReservaMap("codigoreserva"));
			parametros.put("codigousuariocancela", usuario.getLoginUsuario());
			ConsultarActivarCamasReservadas.logActivacionCamas(connection, parametros);
		}
		
	
	
			
		
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Metodo encargado de Buscar los datos de la reserva
	 * y de la cama.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public ActionForward cargaDatosReserva (Connection connection,ConsultarActivarCamasReservadasForm forma, ActionMapping mapping, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request)
	{
		forma.initcanse();
		if (request.getParameter("origenLlamado")!=null)
			forma.setOrigenllamada(request.getParameter("origenLlamado"));
		if (request.getParameter("ocultarEncabezado")!=null)
			forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
		HashMap parametros = new HashMap ();
		
		
		
		//se cargan los parametros para la busqueda de los datos de la reserva
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		
		if(forma.getOrigenllamada().equals("censoCamas"))
		{
			if (request.getParameter("codigoCama")!=null)
				parametros.put("codigoCama", request.getParameter("codigoCama"));
		}
		else
			parametros.put("reservaCamaId", forma.getReservaCamaId());
		//se hace la busqueda de la reserva.
		forma.setDatosReservaMap(ConsultarActivarCamasReservadas.consultaDatosReserva(connection, parametros));
		//se carga el codigo de la cama en los parametros para la busqueda 
		//de los datos de la cama reservada.
		parametros.put("codigocama", forma.getDatosReservaMap("codigocama"));
		//se hace la busqueda de los datos de la cama.
		forma.setDatosCamaMap(ConsultarActivarCamasReservadas.consultaDatoscama(connection, parametros));
		
		paciente.setCodigoPersona(Integer.parseInt(forma.getDatosReservaMap("codigopaciente").toString()));
		UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("principal");
	}
	
	
	
	
	
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public HashMap accionOrdenarMapa (HashMap mapaOrdenar, ConsultarActivarCamasReservadasForm forma)
	{
	
		String [] indices = ((String[])mapaOrdenar.get("INDICES_MAPA"));
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));	
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);
		
	return mapaOrdenar;
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public ActionForward buscarCamasReservadas (Connection connection, ConsultarActivarCamasReservadasForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		logger.info("los criterios de busuqedas son "+forma.getCriteriosBusqueda());
		
		HashMap parametros = new HashMap();
		String [] nombres,apellidos;
		parametros = forma.getCriteriosBusqueda();
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		nombres=parametros.get("nombres").toString().split(" ");
		apellidos=parametros.get("apellidos").toString().split(" ");
		//logger.info("cantidad de nombres "+nombres.length);
		if (nombres.length==1)
		{
			parametros.put("primernombre", nombres[0]);
			parametros.put("segundonombre", "");
		}
		else
			if (nombres.length==2)
			{
				parametros.put("primernombre", nombres[0]);
				parametros.put("segundonombre", nombres[1]);
			}
		
		if (apellidos.length==1)
		{
			parametros.put("primerapellido", apellidos[0]);
			parametros.put("segundoapellido", "");
		}
		else
			if (apellidos.length==2)
			{
				parametros.put("primerapellido", apellidos[0]);
				parametros.put("segundoapellido", apellidos[1]);
			}
		
		
		forma.setConsultarActivarCamasReservadasMAP(ConsultarActivarCamasReservadas.consultarCamasReservadas(connection, parametros));
				
		UtilidadBD.closeConnection(connection);
	
		logger.info("la respuesta del mapa es ==>> "+forma.getConsultarActivarCamasReservadasMAP());
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Metodo encargado de inicializar los criterios de busqueda,
	 * para consultar las camas reservadas.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public ActionForward initBusquedaCamasReservadas (Connection connection, ConsultarActivarCamasReservadasForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		forma.initBusquedaCensoCamas(connection, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCriteriosBusqueda("centroatencion", usuario.getCodigoCentroAtencion());
		
		forma.initRespuesta();
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Método que realiza el filtro de los Centros de Costo y Pisos
	 * por Centro de Atencion e interactua con el ajax.
	 * @param connection
	 * @param forma
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrar(Connection connection, ConsultarActivarCamasReservadasForm forma, HttpServletResponse response,UsuarioBasico usuario) 
	{
		String resultado = "<respuesta>";
		String centroAtencionId = "";
		HashMap parametros = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> arregloAuxP = new ArrayList<HashMap<String,Object>>();
		
		//Se filtran las ciudades segun estado
		if(forma.getEstado().equals("filtroCentroAtencion"))
		{
			centroAtencionId = forma.getCriteriosBusqueda("centroatencion").toString();
			logger.info("centro atencion "+centroAtencionId);
			parametros.put("institucion", usuario.getCodigoInstitucion());
			//logger.info("calor del centro de atencion "+centroAtencionId);
			
			//se pregunta si el centro de atencion viene vacio
			//para si llenar los arrayList con toda la informacion de la consulta
			if (centroAtencionId.equals(""))
			{
				//logger.info("valor del centro de atencion ifual"+centroAtencionId);
				
				//esta consulta nos devuelve todos los centros de costo, esto es porque 
				//en el ultimo parametro lleva el Cero.
				arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, Integer.parseInt(parametros.get("institucion").toString()), "", true, 0);
				//aqui tambien se cargan todos los pisos porque el unico parametro que 
				//lleva es la institucion.
				arregloAuxP=UtilidadesManejoPaciente.obtenerPisos(connection, parametros);
				//logger.info("valor de centros costo "+arregloAux);
				//logger.info("\nvalor de pisos "+arregloAuxP);
				
				//Se carga toda la informacion de centros de costo en formato 
				//XML para armar la respuesta que recibira el ajax.
				for(int i=0;i<arregloAux.size();i++)
				{
					HashMap elemento = (HashMap)arregloAux.get(i);
				    //se formatea a modo de XML
					resultado += "<centrocosto>" +
							"<codigo-centrocosto>"+elemento.get("codigo")+"</codigo-centrocosto>"+
							"<nombre-centrocosto>"+elemento.get("nomcentrocosto")+'('+elemento.get("nomcentroatencion")+')'+"</nombre-centrocosto>"+
						
						 "</centrocosto>";
				}
				//Se carga toda la informacion de los pisos en formato 
				//XML para armar la respuesta que recibira el ajax.
				for(int j=0;j<arregloAuxP.size();j++)
				{
					HashMap elemento = (HashMap)arregloAuxP.get(j);
					//se formatea a modo de XML
					resultado += "<piso>" +
						"<codigo-piso>"+elemento.get("codigopiso")+"</codigo-piso>"+
						"<nombre-piso>"+elemento.get("nombre")+'('+ elemento.get("nombrecentroatencion")+')'+"</nombre-piso>"+
					
					 "</piso>";
				}
				
							
			}
			else
				//se pregunta si el centro de atencion viene lleno
				//de ser asi se filtran las consulta segun el id del
				//centro de atencion.
				if (!centroAtencionId.equals(""))
				{
					//logger.info("valor del centro de atencion diferente "+centroAtencionId);
					
					//se ingresa el criterio de busqueda centro de atencion al hashmap parametros
					parametros.put("centroatencion", centroAtencionId);
					
					//se realizan las busquedas de centros de costo y de pisos
					//que pertenescan a ese centro de atencion
					arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), "", true, Integer.parseInt(parametros.get("centroatencion").toString()));
					arregloAuxP=UtilidadesManejoPaciente.obtenerPisos(connection, parametros);
						
					//Se carga toda la informacion de centros de costo filtrado por el centro de atencion
					//en formato XML para armar la respuesta que recibira el ajax.
					for(int i=0;i<arregloAux.size();i++)
					{
						HashMap elemento = (HashMap)arregloAux.get(i);
						//se verifica nuevamente si el centro de atencion coincide
						if(elemento.get("codcentroatencion").toString().equals(centroAtencionId))
							//se formatea a modo de XML
							resultado += "<centrocosto>" +
								"<codigo-centrocosto>"+elemento.get("codigo")+"</codigo-centrocosto>"+
								"<nombre-centrocosto>"+elemento.get("nomcentrocosto")+"</nombre-centrocosto>"+
							
							 "</centrocosto>";
					}
					//Se carga toda la informacion de los pisos filtrado por el centro de atencion
					//en formato XML para armar la respuesta que recibira el ajax.
					for(int j=0;j<arregloAuxP.size();j++)
					{
						HashMap elemento = (HashMap)arregloAuxP.get(j);
						//se verifica nuevamente si el centro de atencion coincide
						if(elemento.get("idcentroatencion").toString().equals(centroAtencionId))
							//se formatea a modo de XML
							resultado += "<piso>" +
							"<codigo-piso>"+elemento.get("codigopiso")+"</codigo-piso>"+
							"<nombre-piso>"+elemento.get("nombre")+"</nombre-piso>"+
						
						 "</piso>";
					}
				}
		
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(connection);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{   
			//se indica de que tipo es la respuesta
			//y se envia la respuesta hacia la jsp
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
	        //logger.info("ajax "+resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrar: "+e);
		}
		return null;
	}
	
	
	
	
	
	
	
	
}