/*
 * @(#)ActualizacionAutorizacionAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

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
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ActualizacionAutorizacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.ActualizacionAutorizacion;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;

/**
 * Clase encargada del control de la funcionalidad de Actualizacion de Autorizaciones

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 24 /May/ 2005
 */
public class ActualizacionAutorizacionAction extends Action 
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ActualizacionAutorizacionAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof ActualizacionAutorizacionForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ActualizacionAutorizacionForm actualizacionAutorizacionForm = (ActualizacionAutorizacionForm)form;
				HttpSession session = request.getSession();
				PersonaBasica paciente= (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
				ActualizacionAutorizacion actualizacionAutorizacion= new ActualizacionAutorizacion();

				String estado = actualizacionAutorizacionForm.getEstado();
				logger.warn("[ActualizacionAutorizacionAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ActualizacionAutorizacionAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
				}
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping,actualizacionAutorizacionForm,con, paciente, actualizacionAutorizacion,usuario);
				}
				else if(estado.equals("volverEmpezar"))
				{
					return this.accionVolverEmpezar(con,actualizacionAutorizacionForm,mapping);
				}
				else if(estado.equals("actualizarUrHosp"))
				{
					return this.actualizacionPrincipal(mapping,  con, actualizacionAutorizacionForm, actualizacionAutorizacion);
				}
				else if(estado.equals("iniciarBusqueda"))
				{
					actualizacionAutorizacionForm.setFechaSolicitud("");
					actualizacionAutorizacionForm.setConsecutivo("");
					actualizacionAutorizacionForm.setDescripcionServicio("");
					this.cerrarConexion(con);
					return mapping.findForward("busquedaAvanzada");
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, actualizacionAutorizacionForm,mapping);
				}
				else if(estado.equals("ordenarColumnaMedicamentos"))
				{
					return this.accionOrdenarColumnaMedicamentos(con, actualizacionAutorizacionForm,mapping);
				}
				else if(estado.equals("detalleSolicitudes"))
				{
					return this.accionDetalleSolicitudes(actualizacionAutorizacionForm, mapping, con);
				}
				else if(estado.equals("volverDetalleSolicitudes"))
				{
					return this.accionVolverDetalleSolicitudes(actualizacionAutorizacionForm,mapping,con);
				}
				else if(estado.equals("detalleMedicamentos"))
				{
					return this.accionDetalleMedicamentos(actualizacionAutorizacionForm, mapping, con);
				}
				else if(estado.equals("guardarSolicitudes"))
				{
					return this.accionGuardarSolicitudes(actualizacionAutorizacionForm,mapping, usuario,con,request);
				}
				else if(estado.equals("guardarMedicamentos"))
				{
					return this.accionGuardarMedicamentos(actualizacionAutorizacionForm, request, mapping, usuario,con);
				}
				else if(estado.equals("resultadoBusqueda"))
				{
					return this.accionBusquedaAvanzada(actualizacionAutorizacionForm,mapping, con, actualizacionAutorizacionForm.isEsServicio());
				}
				else if (estado.equals("redireccionSolicitudes"))
				{
					actualizacionAutorizacionForm.getMapaActualizacion();
					this.cerrarConexion(con);
					response.sendRedirect(actualizacionAutorizacionForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("redireccionMedicamentos"))
				{
					actualizacionAutorizacionForm.getMapaMedicamentos();
					this.cerrarConexion(con);
					response.sendRedirect(actualizacionAutorizacionForm.getLinkSiguiente());
					return null;
				}
				//**ESTADOS PARA LAS SOLICITUDES DE CIRUGÍA********************
				else if (estado.equals("detalleCirugias"))
				{
					return this.accionDetalleCirugias(con,actualizacionAutorizacionForm,mapping);
				}
				else if (estado.equals("guardarCirugias"))
				{
					return this.accionGuardarCirugias(con,actualizacionAutorizacionForm,mapping,usuario,paciente,request);
				}
				//**************************************************************

			}
			else
			{
				logger.error("El form no es compatible con el form de Actualizacion Autorizacion");
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
	 * Método implementado para regresar al listado de la scuentas sin alterar el estado actual
	 * del mapa de cuentas
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverEmpezar(Connection con, ActualizacionAutorizacionForm forma, ActionMapping mapping) 
	{
		forma.setEstado("empezar");
		this.cerrarConexion(con);
		
		return mapping.findForward("listadoCuentas");
	}



	/**
	 * Método implementado para volver al detalle de las solicitudes
	 * cuando se encuentra en el detalle de las cirugias sin que el mapa de solicitudes
	 * sufra alguna modificación
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionVolverDetalleSolicitudes(ActualizacionAutorizacionForm forma, ActionMapping mapping, Connection con) 
	{
		forma.setEstado("detalleSolicitudes");
		this.cerrarConexion(con);
		return mapping.findForward("listadoSolicitudes");
	}



	/**
	 * Método que guarda los números de autorizacion de las cirugias
	 * @param con
	 * @param actualizacionAutorizacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarCirugias(Connection con, ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		String auxS0 = "",auxS1 = "",auxS2 = "";
		boolean error = false;
		int auxI0 = 0;
		ActionErrors errores = new ActionErrors();
		//se cargan los atributos de la justificación que actualmente están parametrizados
		Vector codigosNombresJustificaciones=Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, false);
		
		//Se instancia objeto de solicitud
		Solicitud solicitud = new Solicitud();
		try
		{
			solicitud.cargar(con,actualizacionAutorizacionForm.getNumSolCx());
		}
		catch(SQLException e)
		{
			logger.error("Error al cargar solicitud en accionGuardarCirugias: "+e);
		}
		
		
		//Se instancia objeto de ActualizacionAutorizacion
		ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
		
	    	for(int k=0; k<actualizacionAutorizacionForm.getNumCirugias();k++)
	        {
	            
	    		//*********ACTUALIZACIÓN DE LAS AUTORIZACIONES************************************
	            auxS0=actualizacionAutorizacionForm.getCirugias("autorizacion_"+k) + "";
	            if(!auxS0.equals("") && !auxS0.equals("null") )
	            {
	                
	                mundo.setNumeroAutorizacion(actualizacionAutorizacionForm.getCirugias("autorizacion_"+k)+"");
	                mundo.setCodigoCirugia(Integer.parseInt(actualizacionAutorizacionForm.getCirugias("codigo_"+k)+""));
	                
	                
	                int pudoMod=mundo.modificarNumeroAutorizacionCirugia(con);
	                if(pudoMod>0)
	                {
	                	//se asigna el numero de la cuenta
	                	actualizacionAutorizacionForm.setCuenta(paciente.getCodigoCuenta());
	                    generarLogCx(actualizacionAutorizacionForm, k, usuario,solicitud);
	                }
	                
	            }
	            //************************************************************************
	            
	            //*******INSERCIÓN DE LAS JUSTIFICACIONES*********************************
	            if(actualizacionAutorizacionForm.isEsMedico())
	            {
	            	auxS0 = actualizacionAutorizacionForm.getCirugias("esJustificado_"+k) + "";
	            	//se verifica si requiere justificación
					if(!UtilidadTexto.getBoolean(auxS0))
					{
						//se verifica si se le llenó justificacion
						auxS1 = actualizacionAutorizacionForm.getCirugias("justificado_"+k) + "";
						if(UtilidadTexto.getBoolean(auxS1))
						{
							error = false;
							//se inicia transaccion
							UtilidadBD.iniciarTransaccion(con);
							
							for(int z=0; z<codigosNombresJustificaciones.size(); z++)
							{
								Vector atributo=(Vector)codigosNombresJustificaciones.elementAt(z);
								//se verifica si el atributo fue llenado
								if(actualizacionAutorizacionForm.getJusCirugias(atributo.elementAt(0)+"_"+k)!=null)
								{
									//se toma la descripción de la justificacion
									auxS2 = actualizacionAutorizacionForm.getJusCirugias(atributo.elementAt(0)+"_"+k)+"";
									
									//el atributo no debe hacer parte de los atributos de NUMERO AUTORIZACION
									if(!Utilidades.verificarAutorizacion(con,((Integer)atributo.elementAt(0)).intValue())&&!auxS2.equals(""))
									{
										//se inserta atributo de la justificacion
									    auxI0 = solicitud.ingresarAtributoTransaccional(con,
									    	Integer.parseInt(actualizacionAutorizacionForm.getCirugias("codigoServicio_"+k)+""),
											((Integer)atributo.elementAt(0)).intValue(), 
											auxS2, 
											ConstantesBD.continuarTransaccion);
									    if(auxI0 <= 0)
									    {
									    	error = true;
									    	errores.add("Error justificando",
									    		new ActionMessage("errors.ingresoDatos","la justificación de la cirugía "+
									    			actualizacionAutorizacionForm.getCirugias("codigoServicio_"+k)));
									    }
									}
								}
							}
							
							//se verifica error
							if(!error)
								UtilidadBD.finalizarTransaccion(con);
							else
								UtilidadBD.abortarTransaccion(con);
						}
					}
	            	
	            }
	            //************************************************************************
	        }
	    
	    if(!errores.isEmpty())
	    	saveErrors(request,errores);
	    	
	    return this.accionDetalleCirugias(con,actualizacionAutorizacionForm,mapping);
	}



	/**
	 * Método implementado para carga las cirugias de una orden quirúrgica
	 * @param con
	 * @param actualizacionAutorizacionForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleCirugias(Connection con, ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping) 
	{
		String auxS0 = "";
		//se obtiene el número de la cuenta
		int cuenta = Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+actualizacionAutorizacionForm.getPosicionMapa())+"");
		int contrato = 0;
		boolean auxB0 = false;
		
		//Se instancia objeto solicitud
		Solicitud solicitud = new Solicitud();
		
		//se carga datos generales de la solicitud
		try
		{
			solicitud.cargar(con,actualizacionAutorizacionForm.getNumSolCx());
		}
		catch(SQLException e)
		{
			logger.error("Error al cargar la solicitud en accionDetalleCirugias: "+e);
		}
		
		//Se instancia objeto SolicitudesCx
		SolicitudesCx solicitudCx = new SolicitudesCx();
		//se cargan las cirugias de las solicitud
		actualizacionAutorizacionForm.setCirugias(
				solicitudCx.cargarServiciosXSolicitudCx(
						con,
						actualizacionAutorizacionForm.getNumSolCx()+"",
						false));
		//se asigna el tamaño del mapa
		actualizacionAutorizacionForm.setNumCirugias(Integer.parseInt(actualizacionAutorizacionForm.getCirugias("numRegistros")+""));
		
		
		for(int i=0;i<actualizacionAutorizacionForm.getNumCirugias();i++)
		{
			//***********REVISIÓN DE LOS NÚMEROS DE AUTORIZACION******************************
			auxS0 = actualizacionAutorizacionForm.getCirugias("autorizacion_"+i) + "";
			
			//se asigna el numero de autorizacion al mapa antiguo
			actualizacionAutorizacionForm.setAutorizacionesAntiguasCx("autorizacion_"+i,auxS0);
			
			//si la orden tiene autorizacion y la cirugias no tiene autorizacion
			//se le asigna la autorizacion de la orden
			
			/*
			 * toda esta parte de revision de numero de autorizacion se debe quitar
			if(!solicitud.getNumeroAutorizacion().equals("")&&
				auxS0.equals(""))
				actualizacionAutorizacionForm.setCirugias("autorizacion_"+i,solicitud.getNumeroAutorizacion());
			*/
			//***********************************************************************************************
			
			//********REVISION DE LAS JUSTIFICACIONES DE CIRUGIAS*************************
			//se consulta el contrato de la cuenta
			contrato = this.cargarContratoCuenta(con,cuenta);
			//se verifica si la cirugia requiere justificacion
			auxB0 = UtilidadValidacion.cirugiaRequiereJustificacion(
				con,
				actualizacionAutorizacionForm.getNumSolCx(),
				Integer.parseInt(actualizacionAutorizacionForm.getCirugias("codigoServicio_"+i)+""),
				contrato);
			//la cirugia requiere justificacion?
			if(auxB0)
				actualizacionAutorizacionForm.setCirugias("esJustificado_"+i,"false");
			else
				actualizacionAutorizacionForm.setCirugias("esJustificado_"+i,"true");
			//***************************************************************************
			
			
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("listadoCirugias");
	}



		/**
		 * Método que maneja la primera acción del usuario al acceder a la
		 * funcionalidad, empezar. En esta funcionalidad simplemente se carga el
		 * listado de las cuentas
		 * 
		 * @param mapping
		 * @param estadoCuentaForm
		 * @param con
		 * @param paciente
		 * @param usuario
		 * @param estadoCuenta
		 */
		private ActionForward accionEmpezar(ActionMapping mapping, ActualizacionAutorizacionForm actualizacionAutorizacionForm, Connection con, PersonaBasica paciente, ActualizacionAutorizacion actualizacionAutorizacion, UsuarioBasico usuario) throws Exception
		{
			actualizacionAutorizacionForm.reset();
			actualizacionAutorizacionForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			actualizacionAutorizacionForm.setMapaCuentas(actualizacionAutorizacion.cargarDatosPacienteCuenta(con, paciente.getCodigoPersona()));
			
			
			//Verificación si es Profesional de la Salud con ocupación Médico
			actualizacionAutorizacionForm.setEsMedico(UtilidadValidacion.esMedico(usuario).equals(""));
			
			this.cerrarConexion(con);
			return mapping.findForward("listadoCuentas");
		}
		
	 
	 /**
	     * @param con
	     * @param actualizacionAutorizacionForm
	     * @param mapping
	     * @param request
	     * @return
	     */
	    private ActionForward accionOrdenarColumna(Connection con, ActualizacionAutorizacionForm actualizacionAutorizacionForm,ActionMapping mapping) 
	    {
	        String[] indices={
					            "fechaSolicitud_", 
					            "horaSolicitud_", 
					            "consecutivo_", 
					            "descripcionServicio_",
					            "numeroAutorizacion_",
								"numeroSolicitud_",
								"tipo_",
								"esJustificado_",
								"justificado_",
								"codigoServicio_"
		            		};
	        int numRegistros = Integer.parseInt(actualizacionAutorizacionForm.getMapaActualizacion("numRegistros")+"");
	        
	        actualizacionAutorizacionForm.setMapaActualizacion(
	        	Listado.ordenarMapa(indices,actualizacionAutorizacionForm.getPatronOrdenar(),
	        		actualizacionAutorizacionForm.getUltimoPatron(),
					actualizacionAutorizacionForm.getMapaActualizacion(),numRegistros));
	        actualizacionAutorizacionForm.setUltimoPatron(actualizacionAutorizacionForm.getPatronOrdenar());
	        actualizacionAutorizacionForm.setMapaActualizacion("numRegistros",numRegistros+"");
	        this.cerrarConexion(con);
			return mapping.findForward("listadoSolicitudes");  
	    }
	    
	    /**
	     * @param con
	     * @param actualizacionAutorizacionForm
	     * @param mapping
	     * @param request
	     * @return
	     */
	    private ActionForward accionOrdenarColumnaMedicamentos(Connection con, ActualizacionAutorizacionForm actualizacionAutorizacionForm,ActionMapping mapping) 
	    {
	        String[] indices={
					            "fechaSolicitud_", 
					            "horaSolicitud_", 
					            "consecutivo_", 
					            "descripcionServicio_",
					            "numeroAutorizacion_",
								"numeroSolicitud_",
								"tipo_",
								"codigoServicio_",
								"esJustificado_",
								"justificado_"
		            		};
	        
	        //se toma el tamaño del mapa
	        int numRegistros = Integer.parseInt(actualizacionAutorizacionForm.getMapaMedicamentos("numRegistros")+"");
	        
	        actualizacionAutorizacionForm.setMapaMedicamentos(Listado.ordenarMapa(indices,actualizacionAutorizacionForm.getPatronOrdenar(),actualizacionAutorizacionForm.getUltimoPatron(),actualizacionAutorizacionForm.getMapaMedicamentos(),numRegistros));
	        actualizacionAutorizacionForm.setUltimoPatron(actualizacionAutorizacionForm.getPatronOrdenar());
	        actualizacionAutorizacionForm.setMapaMedicamentos("numRegistros",numRegistros+"");
	        this.cerrarConexion(con);
			return mapping.findForward("listadoMedicamentos");  
	    }
	 
	    /**
		 * Método que genera los Logs de Modificación para las cirugias 
		 * @param forma
		 * @param pos, indice de la llave.
		 * @param usuario, user
	     * @param solicitud
		 */
		private void generarLogCx(ActualizacionAutorizacionForm actualizacionAutorizacionForm,  int pos, UsuarioBasico usuario, Solicitud solicitud)
		{
			String log;
		    
			    log="\n            ====INFORMACION ORIGINAL ACTUALIZACION DE AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+solicitud.getFechaSolicitud()+"] " +
				"\n*  Hora ["+solicitud.getHoraSolicitud()+"] " +
				"\n*  Orden ["+solicitud.getConsecutivoOrdenesMedicas()+"] " +
				"\n*  Cirugía Nº "+actualizacionAutorizacionForm.getCirugias("numeroServicio_"+pos)+" " +
					"["+actualizacionAutorizacionForm.getCirugias("descripcionServicio_"+pos)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getAutorizacionesAntiguasCx("autorizacion_"+pos)+"] " +
				""  ;
			    
			    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION ACTUALIZACION AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+solicitud.getFechaSolicitud()+"] " +
				"\n*  Hora ["+solicitud.getHoraSolicitud()+"] " +
				"\n*  Orden ["+solicitud.getConsecutivoOrdenesMedicas()+"] " +
				"\n*  Cirugía Nº "+actualizacionAutorizacionForm.getCirugias("numeroServicio_"+pos)+" " +
					"["+actualizacionAutorizacionForm.getCirugias("descripcionServicio_"+pos)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getCirugias("autorizacion_"+pos)+"] " +
				""  ;
				log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logActualizacionAutorizacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
		}
	    
	    
	    /**
		 * Método que genera los Logs de Modificación  
		 * @param forma
		 * @param indexKeyCodigoMapaMod, indice de la llave.
		 * @param usuario, user
		 */
		private void generarLog(ActualizacionAutorizacionForm actualizacionAutorizacionForm,  int indexKeyCodigoMapaMod, UsuarioBasico usuario, boolean esServicio)
		{
			String log;
		    if(esServicio)
			{
		    
			    log="\n            ====INFORMACION ORIGINAL ACTUALIZACION DE AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("fechaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Hora ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("horaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Orden ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("consecutivo_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Descripción ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("descripcionServicio_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("numeroAutorizacion_"+indexKeyCodigoMapaMod)+"] " +
				""  ;
			    
			    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION ACTUALIZACION AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+actualizacionAutorizacionForm.getMapaActualizacion("fechaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Hora ["+actualizacionAutorizacionForm.getMapaActualizacion("horaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Orden ["+actualizacionAutorizacionForm.getMapaActualizacion("consecutivo_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Descripción ["+actualizacionAutorizacionForm.getMapaActualizacion("descripcionServicio_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getMapaActualizacion("numeroAutorizacion_"+indexKeyCodigoMapaMod)+"] " +
			    ""  ;
				log+="\n========================================================\n\n\n " ;	
			}
		    else
		    {
		    	log="\n            ====INFORMACION ORIGINAL ACTUALIZACION DE AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("fechaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Hora ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("horaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Orden ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("consecutivo_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Descripción ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("descripcionServicio_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getMapaActualizacionNoModificado("numeroAutorizacion_"+indexKeyCodigoMapaMod)+"] " +
				""  ;
			    
			    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION ACTUALIZACION AUTORIZACION===== " +
				"\n*  No. Cuenta [" +actualizacionAutorizacionForm.getCuenta() +"] "+
				"\n*  Fecha ["+actualizacionAutorizacionForm.getMapaMedicamentos("fechaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Hora ["+actualizacionAutorizacionForm.getMapaMedicamentos("horaSolicitud_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Orden ["+actualizacionAutorizacionForm.getMapaMedicamentos("consecutivo_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  Descripción ["+actualizacionAutorizacionForm.getMapaMedicamentos("descripcionServicio_"+indexKeyCodigoMapaMod)+"] " +
				"\n*  No. Autorización ["+actualizacionAutorizacionForm.getMapaMedicamentos("numeroAutorizacion_"+indexKeyCodigoMapaMod)+"] " +
			    ""  ;
				log+="\n========================================================\n\n\n " ;
		    }
			
			LogsAxioma.enviarLog(ConstantesBD.logActualizacionAutorizacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
		}
	    
	    
		/**
		 * @param mapping
		 * @param con
		 * @param actualizacionAutorizacionForm
		 * @param actualizacionAutorizacion
		 * @param paciente
		 * @return
		 */
		private ActionForward actualizacionPrincipal(ActionMapping mapping, Connection con, ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActualizacionAutorizacion actualizacionAutorizacion) throws Exception
		{
				int posicion=actualizacionAutorizacionForm.getPosicionMapa();
				if(actualizacionAutorizacionForm.getCriterio().equals("soloAdmision"))
				{
					actualizacionAutorizacion.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));
					actualizacionAutorizacion.setNumeroAutorizacion(actualizacionAutorizacionForm.getMapaCuentas("numeroAutorizacion_"+posicion)+"");
					
					if((actualizacionAutorizacionForm.getMapaCuentas("codigoviaingreso_"+posicion)+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
					{
						actualizacionAutorizacion.modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(con, ConstantesBD.continuarTransaccion);
					}
					else
					{
						actualizacionAutorizacion.modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(con, ConstantesBD.continuarTransaccion);
					}
				}
				
				else if(actualizacionAutorizacionForm.getCriterio().equals("admisionOrdenes"))
				{
					actualizacionAutorizacion.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));
					actualizacionAutorizacion.setNumeroAutorizacion(actualizacionAutorizacionForm.getMapaCuentas("numeroAutorizacion_"+posicion)+"");
					actualizacionAutorizacion.modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(con, ConstantesBD.inicioTransaccion);
					if((actualizacionAutorizacionForm.getMapaCuentas("codigoviaingreso_"+posicion)+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
					{
						actualizacionAutorizacion.modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(con, ConstantesBD.finTransaccion);
					}
					else
					{
						actualizacionAutorizacion.modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(con, ConstantesBD.finTransaccion);
					}
				}
				else if(actualizacionAutorizacionForm.getCriterio().equals("otrasViaIngreso"))
				{
					
					actualizacionAutorizacion.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));
					actualizacionAutorizacion.setNumeroAutorizacion(actualizacionAutorizacionForm.getMapaCuentas("numeroAutorizacion_"+posicion)+"");
					actualizacionAutorizacion.modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(con, ConstantesBD.continuarTransaccion);
				}
				else if(actualizacionAutorizacionForm.getCriterio().equals("cancelar"))
				{
					actualizacionAutorizacionForm.setMapaCuentas("numeroAutorizacion_"+posicion,actualizacionAutorizacionForm.getMapaCuentas("numeroAutorizacionOld_"+posicion)+"");
					this.cerrarConexion(con);
					return mapping.findForward("listadoCuentas");
				}
			
				this.cerrarConexion(con);
				return mapping.findForward("listadoCuentas");
		}
		
		/**
		 * Modificar en la base de Datos los numero de autorizacion de todas las Solicitudes
		 * @param actualizacionAutorizacionForm
		 * @param usuario
		 * @param con
		 * @throws SQLException
		 */
		private void modificarAntiguosBD(ActualizacionAutorizacionForm actualizacionAutorizacionForm, UsuarioBasico usuario, Connection con) throws SQLException
	    {
			ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
			
		    	for(int k=0; k<Integer.parseInt(actualizacionAutorizacionForm.getMapaActualizacion("numRegistros")+"");k++)
		        {
		            
		            String tempoKeyCod=actualizacionAutorizacionForm.getMapaActualizacion("numeroAutorizacion_"+k)+"".trim();
		            if(tempoKeyCod!=null && !tempoKeyCod.equals("") && !tempoKeyCod.equals("null") && !tempoKeyCod.equals("vacio"))
		            {
		                
		                mundo.setNumeroAutorizacion(actualizacionAutorizacionForm.getMapaActualizacion("numeroAutorizacion_"+k)+"");
		                mundo.setNumeroSolicitud(Integer.parseInt(actualizacionAutorizacionForm.getMapaActualizacion("numeroSolicitud_"+k)+""));
		                int pudoMod=mundo.modificarNumeroAutorizacionSolicitudes(con);
		                if(pudoMod>0)
		                {
		                    generarLog(actualizacionAutorizacionForm, k, usuario, true);
		                }
		                
		            }
		    }
	    }
		
		/**
		 * Modificar en la base de Datos los numero de autorizacion de todas las Solicitudes de Medicamentos
		 * @param actualizacionAutorizacionForm
		 * @param usuario
		 * @param con
		 * @throws SQLException
		 */
		private void modificarAntiguosMedicamentosBD(ActualizacionAutorizacionForm actualizacionAutorizacionForm, UsuarioBasico usuario, Connection con) throws SQLException
	    {
			ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
			
		    	for(int k=0; k<actualizacionAutorizacionForm.getMapaMedicamentos().size()/6;k++)
		        {
		            
		            String tempoKeyCod=actualizacionAutorizacionForm.getMapaMedicamentos("numeroAutorizacion_"+k)+"".trim();
		            if(tempoKeyCod!=null && !tempoKeyCod.equals("") && !tempoKeyCod.equals("null") && !tempoKeyCod.equals("vacio"))
		            {
		                
		                mundo.setNumeroAutorizacion(actualizacionAutorizacionForm.getMapaMedicamentos("numeroAutorizacion_"+k)+"");
		                mundo.setNumeroSolicitud(Integer.parseInt(actualizacionAutorizacionForm.getMapaMedicamentos("numeroSolicitud_"+k)+""));
		                int pudoMod=mundo.modificarNumeroAutorizacionMedicamentos(con);
		                if(pudoMod>0)
		                {
		                    generarLog(actualizacionAutorizacionForm, k, usuario, false);
		                }
		                
		            }
		    }
	    }
		
		/**
 		 * Este método especifica las acciones a realizar en el estado
		 * guardar
		 * @param actualizacionAutorizacionForm
		 * @param mapping
		 * @param request
		 * @param usuario
		 * @param con
		 * @param request
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionGuardarSolicitudes (ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping, UsuarioBasico usuario, Connection con, HttpServletRequest request) throws SQLException
		{	
			//******ACTUALIZACIÓN DE LOS NÚMEROS DE AUTORZACION****************************
		    modificarAntiguosBD(actualizacionAutorizacionForm,usuario, con);
		    //****************************************************************************
		    //*****INSERCIÓN DE LAS JUSTIFICACIONES**************************************
		    if(actualizacionAutorizacionForm.isEsMedico())
		    	this.justificarServicios(con,actualizacionAutorizacionForm,usuario,request);
		    //***************************************************************************
		    try
			{
		    	return  this.accionDetalleSolicitudes(actualizacionAutorizacionForm, mapping ,con);
			}
		    
		   catch(Exception e)
		   {
		   		return null;
		   }
		   
		}
		
		/**
		 * Método implementado para insertar la justificación de los servicios que lo requerían
		 * @param con
		 * @param forma
		 * @param usuario
		 * @param request
		 */
		private void justificarServicios(Connection con, ActualizacionAutorizacionForm forma, UsuarioBasico usuario, HttpServletRequest request) 
		{
			//se instancia mundo de Solicitud
			Solicitud mundo = new Solicitud();
			//se cargan los atributos de la justificación que actualmente están parametrizados
			Vector codigosNombresJustificaciones=Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, false);
			//numero de registros
			int numRegistros = Integer.parseInt(forma.getMapaActualizacion("numRegistros")+"");
			String auxS0 = ""; //variable auxiliar
			int auxI0 = 0;
			boolean error = false;
			ActionErrors errores = new ActionErrors();
			
			
			for(int i=0;i<numRegistros;i++)
			{
				//se asigna el numero de la solicitud
				mundo.setNumeroSolicitud(Integer.parseInt(forma.getMapaActualizacion("numeroSolicitud_"+i)+""));
				//se verifica si requiere justificación
				if(!UtilidadTexto.getBoolean(forma.getMapaActualizacion("esJustificado_"+i) + ""))
				{
					//se verifica si se le llenó justificacion
					if(UtilidadTexto.getBoolean(forma.getMapaActualizacion("justificado_"+i) + ""))
					{
						//se inicia transaccion
						UtilidadBD.iniciarTransaccion(con);
						error = false;
						
						for(int z=0; z<codigosNombresJustificaciones.size(); z++)
						{
							Vector atributo=(Vector)codigosNombresJustificaciones.elementAt(z);
							//se verifica si el atributo fue llenado
							if(forma.getJustificacion(atributo.elementAt(0)+"_"+i)!=null)
							{
								//se toma descripción de la justificacion
								auxS0 = forma.getJustificacion(atributo.elementAt(0)+"_"+i)+"";
								
								//el atributo no debe hacer parte de los atributos de NUMERO AUTORIZACION
								if(!Utilidades.verificarAutorizacion(con,((Integer)atributo.elementAt(0)).intValue())&&!auxS0.equals(""))
								{
								    auxI0 = mundo.ingresarAtributoTransaccional(con, Integer.parseInt(forma.getMapaActualizacion("codigoServicio_"+i)+""), ((Integer)atributo.elementAt(0)).intValue(), auxS0, ConstantesBD.continuarTransaccion);
								    if(auxI0 <= 0)
								    {
								    	error = true;
								    	errores.add("Error justificando",
								    		new ActionMessage("errors.ingresoDatos","la justificación de la orden "+
								    			forma.getMapaActualizacion("consecutivo_"+i)));
								    }
								}
							}
						}
						
						//se verifica transaccion
						if(error)
							UtilidadBD.abortarTransaccion(con);
						else
							UtilidadBD.finalizarTransaccion(con);
					}
				}
			}
			
			if(!errores.isEmpty())
				saveErrors(request,errores);
			
			
		}



		/**
 		 * Este método especifica las acciones a realizar en el estado
		 * guardar
		 * @param actualizacionAutorizacionForm
		 * @param mapping
		 * @param request
		 * @param usuario
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionGuardarMedicamentos (ActualizacionAutorizacionForm actualizacionAutorizacionForm, HttpServletRequest request,   ActionMapping mapping, UsuarioBasico usuario, Connection con) throws SQLException
		{	
			//*** ACTUALIZACIÓN DE LOS NÚMEROS DE AUTORIZACION******************************
		    this.modificarAntiguosMedicamentosBD(actualizacionAutorizacionForm,usuario, con);
		    //******************************************************************************
		    //**** INSERCIÓN DE LAS JUSTIFICACIONES DE ARTÍCULOS**************************
		    this.justificarMedicamentos(con,actualizacionAutorizacionForm,usuario,request);
		    //****************************************************************************
		    try
			{
		    	
		    	return  this.accionDetalleMedicamentos(actualizacionAutorizacionForm,mapping,con);
			}
		    
		   catch(Exception e)
		   {
		   		return null;
		   }
		   
		}
		
		
		/**
		 * Método implementado para justificar los medicamentos
		 * @param con
		 * @param forma
		 * @param usuario
		 * @param request
		 */
		private void justificarMedicamentos(Connection con, ActualizacionAutorizacionForm forma, UsuarioBasico usuario, HttpServletRequest request) 
		{
			//se instancia mundo de SolicitudMedicamentos
			SolicitudMedicamentos mundo = new SolicitudMedicamentos();
			//se cargan los atributos de la justificación que actualmente están parametrizados
			Vector codigosNombresJustificaciones=Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, true);
			//numero de registros
			int numRegistros = Integer.parseInt(forma.getMapaMedicamentos("numRegistros")+"");
			String auxS0 = ""; //variable auxiliar
			int auxI0 = 0;
			boolean error = false;
			ActionErrors errores = new ActionErrors();
			
			for(int i=0;i<numRegistros;i++)
			{
				//se asigna el numero de la solicitud
				mundo.setNumeroSolicitud(Integer.parseInt(forma.getMapaMedicamentos("numeroSolicitud_"+i)+""));
				//se verifica si requiere justificación
				if(!UtilidadTexto.getBoolean(forma.getMapaMedicamentos("esJustificado_"+i) + ""))
				{
					//se verifica si se le llenó justificacion
					if(UtilidadTexto.getBoolean(forma.getMapaMedicamentos("justificado_"+i) + ""))
					{
						//se inicia transaccion
						UtilidadBD.iniciarTransaccion(con);
						error = false;
						
						for(int z=0; z<codigosNombresJustificaciones.size(); z++)
						{
							Vector atributo=(Vector)codigosNombresJustificaciones.elementAt(z);
							//se verifica si el atributo fue llenado
							if(forma.getJusMedicamentos(atributo.elementAt(0)+"_"+i)!=null)
							{
								//se toma descripción de la justificacion
								auxS0 = forma.getJusMedicamentos(atributo.elementAt(0)+"_"+i)+"";
								
								//el atributo no debe hacer parte de los atributos de NUMERO AUTORIZACION
								/*
								if(!Utilidades.verificarAutorizacion(con,((Integer)atributo.elementAt(0)).intValue())&&!auxS0.equals(""))
								{
									auxI0 = mundo.insertarUnicamenteAtributoSolicitudMedicamentos(con,mundo.getNumeroSolicitud(),Integer.parseInt(forma.getMapaMedicamentos("codigoServicio_"+i)+""),((Integer)atributo.elementAt(0)).intValue(),auxS0);
								    if(auxI0 <= 0)
								    {
								    	error = true;
								    	errores.add("Error justificando",
								    		new ActionMessage("errors.ingresoDatos","la justificación del medicamento "+
								    			forma.getMapaMedicamentos("codigoServicio_"+i)+" en la orden "+
								    			forma.getMapaMedicamentos("consecutivo_"+i)));
								    }
								}
								*/
							}
						}
						
						//se verifica transaccion
						if(error)
							UtilidadBD.abortarTransaccion(con);
						else
							UtilidadBD.finalizarTransaccion(con);
					}
				}
			}
			
			if(!errores.isEmpty())
				saveErrors(request,errores);
			
			
		}



		/**
		 * Este método especifica las acciones a realizar en el estado
		 * detalleSolicitudes.
		 * @param motivosAnulacionFacturasForm
		 * @param mapping
		 * @param usuario
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionDetalleSolicitudes(ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping, Connection con) throws SQLException
		{
			int posicion=actualizacionAutorizacionForm.getPosicionMapa();
			actualizacionAutorizacionForm.setEstado("detalleSolicitudes");
			ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
			mundo.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));
			HashMap resultados=mundo.listadoSolicitudesOrdenes(con);
			actualizacionAutorizacionForm.setMapaActualizacion((HashMap)resultados.clone());
			actualizacionAutorizacionForm.setMapaActualizacionNoModificado((HashMap)resultados.clone());
			
			this.cerrarConexion(con);
			return mapping.findForward("listadoSolicitudes");		
		}
		
	/**
		 * Este método especifica las acciones a realizar en el estado
		 * detalleMedicamentos.
		 * @param motivosAnulacionFacturasForm
		 * @param mapping
		 * @param usuario
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionDetalleMedicamentos(ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping, Connection con) throws SQLException
		{
			int posicion=actualizacionAutorizacionForm.getPosicionMapa();
			actualizacionAutorizacionForm.setEstado("detalleMedicamentos");
			ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
			mundo.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));
			HashMap resultados=mundo.listadoMedicamentos(con);
			actualizacionAutorizacionForm.setMapaMedicamentos((HashMap)resultados.clone());
			actualizacionAutorizacionForm.setMapaActualizacionNoModificado((HashMap)resultados.clone());
			
			this.cerrarConexion(con);
			return mapping.findForward("listadoMedicamentos");		
		}
		
		/**
		 * Accion para la Busqueda Avanzada
		 * @param actualizacionAutorizacionForm
		 * @param mapping
		 * @param usuario
		 * @param paciente
		 * @param actualizacionAutorizacion
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionBusquedaAvanzada (ActualizacionAutorizacionForm actualizacionAutorizacionForm, ActionMapping mapping,  Connection con, boolean esServicio) throws SQLException
		{	
		    int posicion=actualizacionAutorizacionForm.getPosicionMapa();
			ActualizacionAutorizacion mundo= new ActualizacionAutorizacion();
			mundo.setCuenta((Integer.parseInt(actualizacionAutorizacionForm.getMapaCuentas("cuenta_"+posicion)+"")));

			if(esServicio)
			{
				HashMap resultados=mundo.busquedaAvanzada(con, actualizacionAutorizacionForm.getFechaSolicitud(), actualizacionAutorizacionForm.getHoraSolicitud(), actualizacionAutorizacionForm.getConsecutivo(),actualizacionAutorizacionForm.getDescripcionServicio(),actualizacionAutorizacionForm.getNumeroAutorizacion(),true);
				actualizacionAutorizacionForm.setMapaActualizacion((HashMap)resultados.clone());
				actualizacionAutorizacionForm.setMapaActualizacionNoModificado((HashMap)resultados.clone());
				this.cerrarConexion(con);
				return mapping.findForward("listadoSolicitudes");		
			}
			else
			{
				HashMap resultados=mundo.busquedaAvanzada(con, actualizacionAutorizacionForm.getFechaSolicitud(),actualizacionAutorizacionForm.getHoraSolicitud(), actualizacionAutorizacionForm.getConsecutivo(),actualizacionAutorizacionForm.getDescripcionServicio(),actualizacionAutorizacionForm.getNumeroAutorizacion(),false);
				actualizacionAutorizacionForm.setMapaMedicamentos((HashMap)resultados.clone());
				actualizacionAutorizacionForm.setMapaActualizacionNoModificado((HashMap)resultados.clone());
				this.cerrarConexion(con);
				return mapping.findForward("listadoMedicamentos");
			}
		}
	    
	
	

		
	/**
	 * 
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
		{

			if(con != null)
				return con;
			
			try{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion");
				return null;
			}
		
			return con;
		}
	 
	 			 
	 
	 /**
	  * Método en que se cierra la conexión (Buen manejo
	  * recursos), usado ante todo al momento de hacer un forward
	  * @param con Conexión con la fuente de datos
	  */
	 public void cerrarConexion (Connection con)
		{
				try
				{
					if (con!=null&&!con.isClosed())
					{
						UtilidadBD.closeConnection(con);
					}
				}
				catch(Exception e){
					logger.error("Error al tratar de cerrar la conexion con la fuente de datos MedicosXPoolAction. \n Excepcion: " +e);
				}
		}	
	 
	 /**
		 * Método implementado para cargar el contrato de la cuenta
		 * @param con
		 * @param cuenta
		 * @return
		 */
		private int cargarContratoCuenta(Connection con,int cuenta) 
		{
			Cuenta mundoCuenta = new Cuenta();
			Contrato mundoContrato = new Contrato();
			int contrato = 0;
			try
			{
				mundoCuenta.cargarCuenta(con,cuenta+"");
				//Se verifica si la cuenta está distribuida
				if(mundoCuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial+""))
					contrato = mundoContrato.cargarCodigoContrato(con,cuenta,false);
				else
					contrato = mundoContrato.cargarCodigoContrato(con,cuenta,true);
					
				
			}
			catch(Exception e)
			{
				logger.error("Error al consultar el contrato de la cuenta en ActualizacionAutorizacionesAction: "+e);
			}
			
			return contrato;
		}

}