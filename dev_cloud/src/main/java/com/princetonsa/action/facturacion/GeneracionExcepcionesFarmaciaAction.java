
/*
 * Creado   7/12/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
import util.UtilidadValidacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.GeneracionExcepcionesFarmaciaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.GeneracionExcepcionesFarmacia;

/**
 * Clase para el manejo de generación de excepciones de farmacia, 
 * ingreso, modificación, eliminación y consulta 
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class GeneracionExcepcionesFarmaciaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GeneracionExcepcionesFarmaciaAction.class);
	
	/**
	 * Método execute del action
	 */	
	
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{
		Connection con = null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof GeneracionExcepcionesFarmaciaForm)
			{


				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión -GeneracionExcepcionesFarmaciaAction"+e.toString());
				}

				GeneracionExcepcionesFarmaciaForm generacionForm = (GeneracionExcepcionesFarmaciaForm)form;
				HttpSession session=request.getSession();
				PersonaBasica paciente =(PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				ActionForward validacionesGenerales = this.validacionesUsuario(con,mapping, request, paciente);
				String estado = generacionForm.getEstado();
				logger.info("estado ->"+estado);

				if (validacionesGenerales != null)
				{
					this.cerrarConexion(con);
					return validacionesGenerales ;
				}

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de GeneracionExcepcionesFarmaciaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{			
					generacionForm.reset();
					generacionForm.setAccion("");
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de distribucion, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}

					if(paciente.getExisteAsocio())
					{
						this.cerrarConexion(con);
						return mapping.findForward("paginaSeleccionCuenta"); 
					}
					else
					{	
						return this.accionListar(con,mapping,generacionForm,paciente);
					}   
				}
				else if(estado.equals("volver"))
				{
					generacionForm.reset();
					generacionForm.setEstado("empezar");

					if(paciente.getExisteAsocio())
					{
						this.cerrarConexion(con);
						return mapping.findForward("paginaSeleccionCuenta"); 
					}
					else
					{	
						return this.accionListar(con,mapping,generacionForm,paciente);
					}   
				}
				else if(estado.equals("continuarFlujoAsocio"))
				{
					return this.accionListar(con,mapping,generacionForm,paciente);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(generacionForm.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("empezarConsulta"))
				{
					return this.accionConsultar(con,mapping,generacionForm,paciente);
				}
				else if(estado.equals("ordenar"))
				{
					// return this.accionOrdenar(generacionForm,mapping,request,con);
				}
				else if(estado.equals("continuarFlujoConsulta"))
				{
					return this.accionContinuarConsulta(con,mapping,generacionForm,paciente);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(generacionForm, mapping, con, true);
				}
				else if(estado.equals("resultadoBusquedaAvanzada") )
				{
					return this.accionResultadoBusquedaAvanzada(generacionForm,mapping,request,con,true );
				}
				else if(estado.equals("busquedaAvanzadaFlujoConsulta"))
				{
					return this.accionBusquedaAvanzada(generacionForm, mapping, con, false);
				}
				else if(estado.equals("resultadoBusquedaAvanzadaFlujoConsulta") )
				{
					return this.accionResultadoBusquedaAvanzada(generacionForm,mapping,request,con,false );
				}
				else if(estado.equals("genAutomatica") )
				{
					return this.accionGeneracionAutomatica(con,generacionForm,mapping,paciente);
				}
				else if(estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(generacionForm,mapping,request,usuario,paciente,con);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(con,mapping,generacionForm,paciente);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con,generacionForm,mapping);
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
	 * @param con
	 * @param generacionForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, 
														GeneracionExcepcionesFarmaciaForm generacionForm, 
														ActionMapping mapping) 
	{
		logger.info("mapa-->"+generacionForm.getMapaGeneracion());
		String[] indices={
							"numeroSolicitud_",
							"orden_",
							"codigoArea_", 
							"nombreArea_", 
							"codigoArticulo_",
							"descripcionArticulo_",
							"porcentajeNoCubiertoGenerado_",
							"esNuloEnBD_"
						};
		
		String numRegistros= generacionForm.getMapaGeneracion("numRegistros").toString();
		
		generacionForm.setMapaGeneracion(Listado.ordenarMapa(indices,
																	generacionForm.getPatronOrdenar(),
																	generacionForm.getUltimoPatron(),
																	generacionForm.getMapaGeneracion(),
																	generacionForm.getNumeroElementos()));
		
		generacionForm.setMapaGeneracionNoModificado(Listado.ordenarMapa(indices,
																		generacionForm.getPatronOrdenar(),
																		generacionForm.getUltimoPatron(),
																		generacionForm.getMapaGeneracionNoModificado(),
																		generacionForm.getNumeroElementos()));
		
		generacionForm.setUltimoPatron(generacionForm.getPatronOrdenar());
		generacionForm.setMapaGeneracion("numRegistros", numRegistros);
		generacionForm.setMapaGeneracionNoModificado("numRegistros", numRegistros);
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");  
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salirGuardar.
	 * 
	 * @param ParticipacionPoolXTarifasForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward 
	 * @throws SQLException
	 */
	private ActionForward accionSalirGuardar (	GeneracionExcepcionesFarmaciaForm generacionForm, 
																	ActionMapping mapping,
																	HttpServletRequest request, 
																	UsuarioBasico usuario,
																	PersonaBasica paciente,
																	Connection con) throws SQLException
	{
		boolean validarActualizacionTransaccional= actualizarTransaccional(generacionForm, usuario, con);
		
		if(!validarActualizacionTransaccional)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("actualización Geneneración Excepciones Farmacia", new ActionMessage("errors.excepcionSQL.registroYaActualizado","Generación Excepciones Farmacia"));
			logger.warn("error en la actualización de los datos de la generacion de excepciones de farmacia por concurrencia");
			saveErrors(request, errores);	
			this.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
		return this.accionResumen(con, mapping, generacionForm, paciente);
	}
	
	  /**
	 * @param con
	 * @param mapping
	 * @param request
	 * @param generacionForm
	 * @return
	 */
	private ActionForward accionResumen (	Connection con, 
																ActionMapping mapping, 
																GeneracionExcepcionesFarmaciaForm generacionForm,
																PersonaBasica paciente) 
	{
		GeneracionExcepcionesFarmacia mundo = new GeneracionExcepcionesFarmacia ();
		mundo.reset();
		generacionForm.setEstado("resumen");
		generacionForm.setMapaGeneracion(mundo.listadoSolicitudesFarmacia(con,paciente.getCodigoCuenta()));
		generacionForm.setNumeroElementos((generacionForm.getMapaGeneracion().size() / 8));
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	
	/**
	 * Método transaccional que actualiza los datos de la BD, en caso de que 
	 * el usuario deje un porcentajeNoCubiertoGenerado_X=0 esto signica que 
	 * no se debe almacenar - en caso de que el esNuloEnBD_X sea t, eso
	 * quiere decir que sí tenía un % entonces toca eliminarlo ya que es cero.
	 * De lo contrario se almacenan los valores
	 * 
	 * @param ParticipacionPoolXTarifasForm forma
	 * @param con
	 * @throws SQLException
	 * 
	 * @return true cuando todo salió bien 
	 */
	private boolean actualizarTransaccional(	GeneracionExcepcionesFarmaciaForm generacionForm, 
																	UsuarioBasico usuario,
																	Connection con) throws SQLException
	{
		GeneracionExcepcionesFarmacia mundo= new GeneracionExcepcionesFarmacia();
		boolean yaComenzoTransaccion=false;
		int numeroInsertados=0;
		
		logger.info("INSERTAR EXC MAPA->"+generacionForm.getMapaGeneracion());
		
		
		for(int k=0; k<generacionForm.getNumeroElementos(); k++)
		{
			if( (generacionForm.getMapaGeneracion("esNuloEnBD_"+k)+"").equals("f"))
			 {
				/*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
				 if((generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)+"").equals("0"))
				 {
					 if( (generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("numeroSolicitud_"+k)+"")))
					 {
						 if( (generacionForm.getMapaGeneracion("codigoArticulo_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("codigoArticulo_"+k)+"")))
						 {
							 numeroInsertados=0;
							 mundo.setNumeroSolicitud(Integer.parseInt(generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+""));
							 mundo.setCodigoArticulo(Integer.parseInt(generacionForm.getMapaGeneracion("codigoArticulo_"+k)+""));
								
							 if(!yaComenzoTransaccion)
							 {	
								 numeroInsertados=mundo.eliminarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.inicioTransaccion);
								 if(numeroInsertados<=0)
									 return false;
								 else
									generarLog(generacionForm, k, usuario);
								 yaComenzoTransaccion=true;
							 }	
							 else
							 {	
								 numeroInsertados=mundo.eliminarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion);
							 	 if(numeroInsertados<=0)
							 		 return false;
							 	 else
									generarLog(generacionForm, k, usuario);
							 }	 
						 }
					 }	
				 }
				 /*SEGUNDA PARTE, LA MODIFICACIÓN */
				 else
				 {
					 numeroInsertados=0;
					 
					 if( !(generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("porcentajeNoCubiertoGenerado_"+k)+"")) )
					 {
						 if( (generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("numeroSolicitud_"+k)+"")))
						 {
							 if( (generacionForm.getMapaGeneracion("codigoArticulo_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("codigoArticulo_"+k)+"")))
							 {
								 mundo.setNumeroSolicitud(Integer.parseInt(generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+""));
								 mundo.setCodigoArticulo(Integer.parseInt(generacionForm.getMapaGeneracion("codigoArticulo_"+k)+""));
								 mundo.setPorcentajeNoCubierto(Double.parseDouble(generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)+""));
								 
								 if(!yaComenzoTransaccion)
								 {	
									 numeroInsertados=mundo.modificarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.inicioTransaccion, usuario.getLoginUsuario());
									 if(numeroInsertados<=0)
										 return false;
									 else
										generarLog(generacionForm, k, usuario);
									  yaComenzoTransaccion=true;
								 }	
								 else
								 {	
									 numeroInsertados=mundo.modificarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());	
									 if(numeroInsertados<=0)
										 return false;
									 else
										 generarLog(generacionForm, k, usuario);
								  }	
							 }
						 }
					 }
				 }
			 }
			/*TERCERA PARTE DE INSERCIÓN PARA CUANDO esNuloEnBD==true y el %>0*/
			else
			{
				if(!(generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)+"").equals("0"))
				{
					if( (generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("numeroSolicitud_"+k)+"")))
					{
						if( (generacionForm.getMapaGeneracion("codigoArticulo_"+k)+"").equals((generacionForm.getMapaGeneracionNoModificado("codigoArticulo_"+k)+"")))
						{
							mundo.setNumeroSolicitud(Integer.parseInt(generacionForm.getMapaGeneracion("numeroSolicitud_"+k)+""));
	   					 	mundo.setCodigoArticulo(Integer.parseInt(generacionForm.getMapaGeneracion("codigoArticulo_"+k)+""));
	   					 	mundo.setPorcentajeNoCubierto(Double.parseDouble(generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)+""));
	   					 	
	   					 	logger.info("NumSol-->"+generacionForm.getMapaGeneracion("numeroSolicitud_"+k));
	   					 	logger.info("CodArt-->"+generacionForm.getMapaGeneracion("codigoArticulo_"+k));
	   						logger.info("%-->"+generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k));
	   					 	
	   					 	
		   					if(!yaComenzoTransaccion)
		   					{	
		   						 numeroInsertados=mundo.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.inicioTransaccion, usuario.getLoginUsuario());
		   						 if(numeroInsertados<=0)
		   							 return false;
		   						 else
		   							generarLog(generacionForm, k, usuario);
		   						  yaComenzoTransaccion=true;
		   					 }	
		   					 else
		   					 {	
		   						 numeroInsertados=mundo.insertarGenExcepcionesFarmaciaTransaccional(con, ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());	
		   						 if(numeroInsertados<=0)
		   							 return false;
		   						 else
		   							 generarLog(generacionForm, k, usuario);
		   					  }	
						 }
					}
				}
			}
		}
		
		// SI LA TRANSACCIÓN YA FUÉ INICIADA ENTONCES QUE LA FINALICE
		if(yaComenzoTransaccion)
			mundo.terminarTransaccion(con); 
		return true;
	}
	
	/**
	 * Método que genera los Logs de Modificación  
	 * @param forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLog(	GeneracionExcepcionesFarmaciaForm generacionForm,  int indexKeyCodigoMapaMod, UsuarioBasico usuario)
	{
		String log;
		
		log="\n			====INFORMACION ORIGINAL GENERACION EXCEPCIONES FARMACIA===== " +
		"\n*  Id Cuenta [" +generacionForm.getIdCuenta() +"] "+
		"\n*  # Solicitud ["+generacionForm.getMapaGeneracionNoModificado("numeroSolicitud_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Código Área ["+generacionForm.getMapaGeneracionNoModificado("codigoArea_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Nombre Área ["+generacionForm.getMapaGeneracionNoModificado("nombreArea_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Código Artículo ["+generacionForm.getMapaGeneracionNoModificado("codigoArticulo_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Descripción Artículo ["+generacionForm.getMapaGeneracionNoModificado("descripcionArticulo_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Porcentaje No Cubierto ["+generacionForm.getMapaGeneracionNoModificado("porcentajeNoCubiertoGenerado_"+indexKeyCodigoMapaMod)+"] " +
		""  ;
		
		log+="\n		  =====INFORMACION DESPUES DE LA MODIFICACION EXCEPCIONES FARMACIA===== " +
		"\n*  Id Cuenta [" +generacionForm.getIdCuenta() +"] "+
		"\n*  # Solicitud ["+generacionForm.getMapaGeneracion("numeroSolicitud_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Código Área ["+generacionForm.getMapaGeneracion("codigoArea_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Nombre Área ["+generacionForm.getMapaGeneracion("nombreArea_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Código Artículo ["+generacionForm.getMapaGeneracion("codigoArticulo_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Descrpción Artículo ["+generacionForm.getMapaGeneracion("descripcionArticulo_"+indexKeyCodigoMapaMod)+"] " +
		"\n*  Porcentaje No Cubierto ["+generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+indexKeyCodigoMapaMod)+"] " +
		""  ;
		log+="\n========================================================\n\n\n " ;	
		
		
		
		LogsAxioma.enviarLog(ConstantesBD.logGeneracionExcepcionesFarmaciaCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud());
	}
	
	
	
	
	 /**
	  * Metodo para la generación automatica de las excepciones de farmacia
	  * si existen.
	 * @param con, Connection con la fuente de datos
	 * @param generacionForm, Forma
	 * @param mapping, para la navegación
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGeneracionAutomatica(Connection con, 
																		GeneracionExcepcionesFarmaciaForm generacionForm, 
																		ActionMapping mapping, 
																		PersonaBasica paciente)  
	{
		GeneracionExcepcionesFarmacia mundo = new GeneracionExcepcionesFarmacia ();
		mundo.reset();
		ResultSetDecorator rs = null ;
		boolean hayExcepcionFarmacia = false; 
		
		rs = mundo.consultaExcepcionesFarmacia(con,paciente.getCodigoConvenio(),-1,-1,true,false);
		
		try 
		{
			if(rs.next())
			{
			
				for(int k = 0; k < generacionForm.getNumeroElementos(); k++)
				{
				   try
				   {
						rs  = mundo.consultaExcepcionesFarmacia(con,paciente.getCodigoConvenio(),
																	Integer.parseInt(generacionForm.getMapaGeneracion("codigoArea_"+k)+""),
																	Integer.parseInt(generacionForm.getMapaGeneracion("codigoArticulo_"+k)+""),
																	false,true);
						while(rs.next())
						{
							generacionForm.setMapaGeneracion("porcentajeNoCubiertoGenerado_"+k,rs.getInt("no_cubreEF")+"");
							hayExcepcionFarmacia = true;
						}
						
						if (! hayExcepcionFarmacia)
						{
							generacionForm.setMapaGeneracion("porcentajeNoCubiertoGenerado_"+k,0+"");  
							hayExcepcionFarmacia = false;
						}
				   
				   }
				   catch(SQLException e)
				   {
					   logger.warn("error sql "+e);
				   }
							 
				}
				
				generacionForm.setNumeroElementos((generacionForm.getMapaGeneracion().size() / 8));
			}
			
			else
			{
				logger.warn("No existe parametrización de Excepción de farmacia x convenio"); 
				generacionForm.setAccion("advertencia");
				logger.warn("No se realizo la generación automatica");
				this.cerrarConexion(con);																	   
				return mapping.findForward("paginaPrincipal");	
			}
		} 
		 catch (SQLException e) 
		 {
			 logger.warn("error sql "+e);			
		}
			  
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		 
	}


	/**
	 * @param con
	 * @param mapping
	 * @param generacionForm
	 * @param paciente
	 * @return
	 */
	private ActionForward accionContinuarConsulta(Connection con, 
																	ActionMapping mapping, 
																	GeneracionExcepcionesFarmaciaForm generacionForm, 
																	PersonaBasica paciente) 
	{
		GeneracionExcepcionesFarmacia mundo = new GeneracionExcepcionesFarmacia ();
		mundo.reset();
		
		generacionForm.setMapaGeneracion(mundo.listadoSolicitudesFarmaciaGeneradas(con,paciente.getCodigoCuenta()));
		generacionForm.setNumeroElementos(Integer.parseInt(generacionForm.getMapaGeneracion("numRegistros")+""));
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaConsulta");	   
	}


	/**
	 * @param con
	 * @param mapping
	 * @param generacionForm
	 * @param paciente
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, 
															ActionMapping mapping,
															GeneracionExcepcionesFarmaciaForm generacionForm, 
															PersonaBasica paciente) 
	{
		GeneracionExcepcionesFarmacia mundo = new GeneracionExcepcionesFarmacia ();
		mundo.reset();
				
		generacionForm.setColGeneracion(mundo.consultaExcepcionFarmaciaLinks(con, paciente.getCodigoPersona()));
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaLinks"); 
		
	}


	/**
	 * @param con
	 * @param mapping
	 * @param request
	 * @param generacionForm
	 * @return
	 */
	private ActionForward accionListar(Connection con, 
														ActionMapping mapping, 
														GeneracionExcepcionesFarmaciaForm generacionForm,
														PersonaBasica paciente) 
	{
		int idCuenta=generacionForm.getIdCuenta();
		if(idCuenta==0)
		{
			idCuenta=paciente.getCodigoCuenta();
		}
		GeneracionExcepcionesFarmacia mundo = new GeneracionExcepcionesFarmacia ();
		mundo.reset();
			  
		
		generacionForm.setMapaGeneracion(mundo.listadoSolicitudesFarmacia(con,idCuenta));
		generacionForm.setMapaGeneracionNoModificado(mundo.listadoSolicitudesFarmacia(con,idCuenta));
		
		generacionForm.setNumeroElementos(Integer.parseInt(generacionForm.getMapaGeneracion().get("numRegistros")+""));
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Método que implementa las validaciones generales de entrada 
	 * @param map ActionMapping
	 * @param req HttpServletRequest
	 * @return ActionForward a la paginaError si existe un acceso errado
	 */
	protected ActionForward validacionesUsuario(Connection con,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente)
	{
			if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
			{
					logger.warn("El paciente no es válido (null)");
					request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
			}
			else
			if(paciente.getCodigoCuenta() == 0) 
			{
				logger.warn("El paciente no tiene cuenta abierta");
				request.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoAbierta");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else 
			{
				try
				{
					Cuenta cuenta=new Cuenta();
					cuenta.cargarCuenta(con,paciente.getCodigoCuenta()+"");
					
					if(paciente.getExisteAsocio())
					{
						boolean validar=UtilidadValidacion.cuentaOSubcuentaTieneEstadoDefinido(con,paciente.getCodigoCuenta(),ConstantesBD.codigoEstadoCuentaActiva, false);
						if(!validar)
						{
							logger.warn("la cuenta de hospitalización tiene asocio y debe estar en estado abierta"); 
							ArrayList array=new ArrayList();
							array.add("hospitalización");
							array.add("abierta");
							request.setAttribute("codigoDescripcionError","error.asociocuenta.estadosCuentaAsociada");
							request.setAttribute("atributosError",array);
							this.cerrarConexion(con);																	   
							return mapping.findForward("paginaError");		 
						}
						validar=UtilidadValidacion.cuentaOSubcuentaTieneEstadoDefinido(con,paciente.getCodigoCuentaAsocio(),ConstantesBD.codigoEstadoCuentaAsociada, false);
						if(!validar)
						{
							logger.warn("la cuenta de urgencias debe tener estado asociada"); 
							ArrayList array=new ArrayList();
							array.add("urgencias");
							array.add("asociada");
							request.setAttribute("codigoDescripcionError","error.asociocuenta.estadosCuentaAsociada");
							request.setAttribute("atributosError",array);
							this.cerrarConexion(con);
							return mapping.findForward("paginaError");		 
						}
					}
				}
				catch(SQLException e)
				{
					logger.warn("no se pudo cargar la cuenta");
					request.setAttribute("errorBD","errors.problemasBd");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
				
		return null;
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param GeneracionExcepcionesFarmaciaForm generacionForm,
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "busquedaAvanzada.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	GeneracionExcepcionesFarmaciaForm generacionForm, 
																				ActionMapping mapping, 
																				Connection con,
																				boolean esActionIngresar) throws SQLException
	{
		if(esActionIngresar)
		{
			generacionForm.setEstado("busquedaAvanzada");
			this.cerrarConexion(con);
			return mapping.findForward("paginaPrincipal");
		}
		else
		{
			generacionForm.setEstado("busquedaAvanzadaFlujoConsulta");
			this.cerrarConexion(con);
			return mapping.findForward("paginaConsulta");
		}
	}
	
	/*
	 * Acción que ordena según la columna escogida por el usuario
	 * @param generacionForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 
	private ActionForward accionOrdenar(	GeneracionExcepcionesFarmaciaForm generacionForm,
															ActionMapping mapping,
															HttpServletRequest request, 
															Connection con) throws SQLException
	{
		try
		{
			//generacionForm.setCol(Listado.ordenarColumna(new ArrayList(terceroForm.getCol()),terceroForm.getUltimaPropiedad(),terceroForm.getColumna()));
			//generacionForm.setMapaGeneracion(Listado.ordenarColumna(new ArrayList(generacionForm.getMapaGeneracion()), generacionForm.getUltimaPropiedad(), generacionForm.getColumna()));
			ArrayList arrayTemp=Listado.ordenarColumna(convertirMapa_Array(generacionForm), generacionForm.getUltimaPropiedad(), generacionForm.getColumna());
			generacionForm.setMapaGeneracion(convertirArray_Mapa(arrayTemp));
			generacionForm.setUltimaPropiedad(generacionForm.getColumna());
			this.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el ordenamiento de la generación de excepciones de farmacia");
			this.cerrarConexion(con);
			generacionForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("Listado generación de excepciones farmacia");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		return mapping.findForward("paginaPrincipal")	;
	}	
	
	/**
	 * Método de conversión Mapa - Array 
	 * @param generacionForm
	 * @return
	/
	private ArrayList convertirMapa_Array(GeneracionExcepcionesFarmaciaForm generacionForm)
	{
		ArrayList array= new ArrayList();
		int pos=0;
		for(int k=0; k<generacionForm.getNumeroElementos(); k++)
		{
			array.add(pos,generacionForm.getMapaGeneracion("numeroSolicitud_"+k)); pos++;
			array.add(pos,generacionForm.getMapaGeneracion("codigoArea_"+k)); pos++;	
			array.add(pos,generacionForm.getMapaGeneracion("nombreArea_"+k)); pos++;
			array.add(pos,generacionForm.getMapaGeneracion("codigoArticulo_"+k)); pos++;
			array.add(pos,generacionForm.getMapaGeneracion("descripcionArticulo_"+k)); pos++;
			array.add(pos,generacionForm.getMapaGeneracion("porcentajeNoCubiertoGenerado_"+k)); pos++;
			array.add(pos,generacionForm.getMapaGeneracion("esNuloEnBD_"+k)); pos++;
		}
		return array;
	}
	
	/**
	 * Método de conversión Array - Mapa 
	 * @param generacionForm
	 * @return
	/
	private HashMap convertirArray_Mapa(ArrayList array)
	{
		 HashMap mapa= new HashMap();
		 for(int k=0; k<array.size(); k++)
		 {
			 mapa.put("numeroSolicitud_"+k, array.get("numero"))
		 }
		 return mapa;
	}*/
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param GeneracionExcepcionesFarmaciaForm generacionForm,
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param esActionIngresar, undica si el flujo debe ser para Ingreso o Consulta
	 * @return ActionForward a la página principal
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(		GeneracionExcepcionesFarmaciaForm generacionForm,
																								ActionMapping mapping, 
																								HttpServletRequest request,
																								Connection con,
																								boolean esActionIngresar) throws SQLException
	{
		GeneracionExcepcionesFarmacia mundo= new GeneracionExcepcionesFarmacia();
		mundo.setIdCuenta(generacionForm.getIdCuenta());
		mundo.setCodigoArea(generacionForm.getCodigoArea());
		
		if(!generacionForm.getDescripcionArticulo().equals(""))
		{	
			if(generacionForm.getEsBusquedaPorCodigo())
			{
				try
				{
					mundo.setDescripcionArticulo(Integer.parseInt(generacionForm.getDescripcionArticulo())+"");
				} 
				catch(NumberFormatException e)
				{
					if(esActionIngresar)
						generacionForm.setEstado("busquedaAvanzada");
					else
						generacionForm.setEstado("busquedaAvanzadaFlujoConsulta"); 
					mundo.setDescripcionArticulo("");
					ActionErrors errores = new ActionErrors();
					errores.add("Código del artículo", new ActionMessage("errors.integer", "El Código del Artículo"));
					logger.warn("error enel formato del cód del artículo para la búsqueda de generación de excepciones Farmacia");
					saveErrors(request, errores);	
					this.cerrarConexion(con);
					if(esActionIngresar)
						return mapping.findForward("paginaPrincipal");
					else
						return mapping.findForward("paginaConsulta");
				}
				catch(NullPointerException e)
				{
					if(esActionIngresar)
						generacionForm.setEstado("busquedaAvanzada");
					else
						generacionForm.setEstado("busquedaAvanzadaFlujoConsulta"); 
					mundo.setDescripcionArticulo("");
					ActionErrors errores = new ActionErrors();
					errores.add("Código del artículo", new ActionMessage("errors.integer", "El Código del Artículo"));
					logger.warn("error enel formato del cód del artículo para la búsqueda de generación de excepciones Farmacia");
					saveErrors(request, errores);	
					this.cerrarConexion(con);									
					if(esActionIngresar)
						return mapping.findForward("paginaPrincipal");
					else
						return mapping.findForward("paginaConsulta");
				}
			}
			else
				mundo.setDescripcionArticulo(generacionForm.getDescripcionArticulo());
		}	
		
		if(!generacionForm.getPorcentajeNoCubiertoString().equals(""))
		{
			try
			{
				mundo.setPorcentajeNoCubierto(Double.parseDouble(generacionForm.getPorcentajeNoCubiertoString()));
				if(mundo.getPorcentajeNoCubierto()>100 || mundo.getPorcentajeNoCubierto()<0)
				{
					if(esActionIngresar)
						generacionForm.setEstado("busquedaAvanzada");
					else
						generacionForm.setEstado("busquedaAvanzadaFlujoConsulta"); 
					mundo.setPorcentajeNoCubierto(-1);
					ActionErrors errores = new ActionErrors();
					errores.add("% no cubierto", new ActionMessage("errors.range", "El % no cubierto debe ser entero o decimal y", "cero (0)", " cien (100)"));
					logger.warn("error en el formaro del % en la gen de exc farmacia");
					saveErrors(request, errores);	
					this.cerrarConexion(con);									
					if(esActionIngresar)
						return mapping.findForward("paginaPrincipal");
					else
						return mapping.findForward("paginaConsulta");
				}
			}
			catch(NumberFormatException e)
			{
				if(esActionIngresar)
					generacionForm.setEstado("busquedaAvanzada");
				else
					generacionForm.setEstado("busquedaAvanzadaFlujoConsulta"); 
				mundo.setPorcentajeNoCubierto(-1);
				ActionErrors errores = new ActionErrors();
				errores.add("% no cubierto", new ActionMessage("errors.range", "El % no cubierto debe ser entero o decimal y", "cero (0)", " cien (100)"));
				logger.warn("error en el formaro del % en la gen de exc farmacia");
				saveErrors(request, errores);	
				this.cerrarConexion(con);									
				if(esActionIngresar)
					return mapping.findForward("paginaPrincipal");
				else
					return mapping.findForward("paginaConsulta");
			}
			catch(NullPointerException e)
			{
				generacionForm.setEstado("busquedaAvanzada");
				mundo.setPorcentajeNoCubierto(-1);
				ActionErrors errores = new ActionErrors();
				errores.add("% no cubierto", new ActionMessage("errors.range", "El % no cubierto debe ser entero o decimal y", "cero (0)", " cien (100)"));
				logger.warn("error en el formaro del % en la gen de exc farmacia");
				saveErrors(request, errores);	
				this.cerrarConexion(con);									
				if(esActionIngresar)
					return mapping.findForward("paginaPrincipal");
				else
					return mapping.findForward("paginaConsulta");
			}
		}
		else
		{
			mundo.setPorcentajeNoCubierto(-1);
		}
		
		generacionForm.setMapaGeneracion(mundo.busquedaGenExcepcionesFarmaciaHashMap(con, generacionForm.getEsBusquedaPorCodigo()));
		generacionForm.setMapaGeneracionNoModificado(mundo.busquedaGenExcepcionesFarmaciaHashMap(con, generacionForm.getEsBusquedaPorCodigo()));
		generacionForm.setNumeroElementos(Integer.parseInt(generacionForm.getMapaGeneracion().get("numRegistros")+""));
		this.cerrarConexion(con);
		
		if(!esActionIngresar)
			return mapping.findForward("paginaConsulta");
		/*Para el caso de Ingreso -Modificación-de datos en esta parte se trabaha con Hash Map*/
		else
			return mapping.findForward("paginaPrincipal");	
   }
	
	
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
			try{
				if (con!=null&&!con.isClosed())
				{
					UtilidadBD.closeConnection(con);
				}
			}
			catch(Exception e){
				logger.error("Error al tratar de cerrar la conexion con la fuente de datos GeneracionExcepcionesFarmaciaAction. \n Excepcion: " +e);
			}
	}
	
}
