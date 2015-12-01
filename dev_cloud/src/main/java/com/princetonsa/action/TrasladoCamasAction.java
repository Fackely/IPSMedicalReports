/*
 * @(#)TrasladoCamasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
import org.apache.struts.action.ActionServlet;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.TrasladoCamasForm;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.pdf.TrasladoCamasPdf;
import com.servinte.axioma.fwk.exception.IPSException;
 
/**
 * Clase encargada del control de la funcionalidad de Traslado de Camas

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 6 /Jul/ 2005
 */

public class TrasladoCamasAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(TrasladoCamasAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
		if(form instanceof TrasladoCamasForm)
	    {
	        
		    
		    /**intentamos abrir una conexion con la fuente de datos**/ 
			con = UtilidadBD.abrirConexion(); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			TrasladoCamasForm trasladoCamasForm=(TrasladoCamasForm)form;
			TrasladoCamas trasladoCamas=new TrasladoCamas();
			
			HttpSession session = request.getSession();
			PersonaBasica paciente= (PersonaBasica)session.getAttribute("pacienteActivo");
			UsuarioBasico medico= (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			String estado = trasladoCamasForm.getEstado();
			logger.warn("[TrasladoCamasAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de TrasladoCamasAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			
			//******************ESTADOS TRASLADO POR PACIENTE****************************************************************
			else if(estado.equals("empezarPaciente"))
			{
				return this.validarPaciente(con, trasladoCamasForm, trasladoCamas, paciente, mapping, request, medico);
						
							
			}
			else if(estado.equals("guardarPaciente"))
			{
				return this.accionGuardarTrasladoPaciente(trasladoCamasForm, mapping, con, paciente, medico, request, false, getServlet());
			}
			//***************************************************************************************************
			//*****************ESTADOS TRASLADO POR AREA**********************************************************
			else if(estado.equals("empezarArea"))
			{
				return this.accionEmpezarArea(mapping, trasladoCamasForm, con);
			}
			else if(estado.equals("resultadoBusqueda"))
			{
				return this.accionResultadoBusquedaTrasladoArea(trasladoCamasForm,  mapping,   con,medico,request,response);
			}
			else if(estado.equals("ordenarColumnaArea"))
			{
				return this.accionOrdenarColumnaArea(con, trasladoCamasForm, mapping);
			}
			else if(estado.equals("detalleTrasladoArea"))
			{
				return this.accionDetalleTrasladoArea(trasladoCamasForm,  mapping, con, medico, paciente, request);
			}
			else if(estado.equals("guardarTrasladoArea"))
			{
				return this.accionGuardarTrasladoArea(trasladoCamasForm, mapping, con, paciente, medico, request);
			}
			else if(estado.equals("guardarTrasladoArea"))
			{
				return this.accionGuardarTrasladoArea(trasladoCamasForm, mapping, con, paciente, medico, request);
			}			
			else if (estado.equals("redireccionArea"))
			{
				 UtilidadBD.closeConnection(con);
				 response.sendRedirect(trasladoCamasForm.getLinkSiguiente());
				 return null;
			}
			//**************************************************************************************
			//**************ESTADOS CONSULTA TRASLADO X PACIENTE************************************
			else if(estado.equals("consultarPaciente"))
			{
				/**Debe haber un paciente cargado en session para poder consultar los traslados por paciente**/
				if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
				}
				else/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
					if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
					{
						request.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
						return mapping.findForward("paginaError");
					}
					else
					{
						return this.accionConsultarTrasladoPaciente(trasladoCamasForm, mapping, con, paciente);
					}
			}
			else if(estado.equals("ordenarColumnaPaciente"))
			{
				return this.accionOrdenarColumnaTrasladosPaciente(con, trasladoCamasForm, mapping);
			}
			else if(estado.equals("detalleTrasladoPaciente"))
			{
				return this.accionDetalleTrasladoPaciente(trasladoCamasForm, mapping, con);
			}
			else if (estado.equals("redireccionPaciente"))
			{
				 UtilidadBD.closeConnection(con);
				 response.sendRedirect(trasladoCamasForm.getLinkSiguiente());
				 return null;
			}
			else if(estado.equals("ingresosAnteriores"))
			{
				return this.accionConsultarIngresosAnteriores(trasladoCamasForm, mapping, con, paciente);
			}
			else if(estado.equals("detalleTrasladoAnteriorPaciente"))
			{
				return this.accionDetalleTrasladoAnteriorPaciente(trasladoCamasForm, mapping, con, paciente);
			}
			else if(estado.equals("ordenarColumnaIngAnteriores"))
			{
				return this.accionOrdenarColumnaIngAnteriores(con, trasladoCamasForm, mapping);
			}
			//**************************************************************************************
			//**************ESTADOS CONSULTA TRASLADOS X FECHA************************************
			else if(estado.equals("consultaTrasladoFecha"))
			{
				return this.accionConsultarTrasladoFecha(trasladoCamasForm, mapping, con,medico);
			}
			else if(estado.equals("buscarTrasladoFecha"))
			{
				return this.accionConsultarTrasladoFechaBusqueda(trasladoCamasForm, mapping, con);
			}
			else if (estado.equals("redireccionFecha"))
			{
				 UtilidadBD.closeConnection(con);
				 UtilidadSesion.redireccionar(trasladoCamasForm.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(medico.getCodigoInstitucionInt()),Integer.parseInt(trasladoCamasForm.getMapaTrasladosFecha("numRegistros").toString()), response, request, "trasladoArea.jsp",true);
				// response.sendRedirect(trasladoCamasForm.getLinkSiguiente());
				 return null;
			}
			else if(estado.equals("detalleTrasladoFecha"))
			{
				return this.accionDetalleTrasladoFecha(trasladoCamasForm,mapping,con);
			}
			else if(estado.equals("ordenarColumnaFecha"))
			{
				return this.accionOrdenarColumnaFecha(con,trasladoCamasForm,mapping);
			}
			else if(estado.equals("imprimir"))
			{
				return this.accionImprimir( mapping,  request,  con,  trasladoCamasForm,  medico, paciente);
			}
			//************************************************************************************
			/*---------------------------------------------------------------------------
			 * Adicionado Por Jhony Alexander Duque A.
			 * Estado adicionado para el manejo de llamadas a esta funcionalidad.
			 * ========================---LLAMADOTRASLADARCAMA---========================
			 ----------------------------------------------------------------------------*/
			else
				if (estado.equals("llamadoTrasladarCama"))
				{
					return this.accionFormatearTraslado(con, trasladoCamasForm, trasladoCamas, mapping, medico, request, paciente);
				}
	    }
		else
		{
			logger.error("El form no es compatible con el form de Consulta de Facturas");
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

	
	
	/*------------------------------------------------------------------
	 * Adicionado Por  Jhony Alexander Duque A.
	 * Metodo encargado de formatear los valores para el traslado de 
	 * camas.
	 * Este metodo es utilizado cuando el traslado de camas es llada 
	 * desde otra funcionalidad; para esto se apoya en un parametro 
	 * que indica de donde ha sido llamada, este es "origenLlamado"
	 ------------------------------------------------------------------*/
	private ActionForward accionFormatearTraslado (Connection con,TrasladoCamasForm forma,TrasladoCamas trasladoCamas,ActionMapping mapping, UsuarioBasico medico,HttpServletRequest request, PersonaBasica paciente) throws Exception
	{
		if(forma.getOrigenLlamado().equals("censoCamas"))
		{
			//se crea un paciente con el codigo del paciente que viene por parametros
			paciente.setCodigoPersona(forma.getCodigoPaciente());
			//se carga el paciente creado
			UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
			//se indica a la jsp que debe de ocultar el cabezote
			forma.setOcultarEncabezado(true);
			
		}
		
		return this.validarPaciente(con, forma, trasladoCamas, paciente, mapping, request, medico);
	}
	
	/**
	 * Metodo encargado de hacer las validaciones del paciente
	 * para que se pueda ingresar al trslado de camas.
	 * @throws Exception 
	 */
	private ActionForward validarPaciente (Connection con,TrasladoCamasForm forma, TrasladoCamas trasladoCamas,PersonaBasica paciente, ActionMapping mapping,HttpServletRequest request,UsuarioBasico medico) throws Exception
	{
		
		RespuestaValidacion validacion = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		ActionErrors errores = new ActionErrors();
		
		/**SE REALIZAN LAS VALIDACINOES GENERALES DEL PACIENTE**/
		if(!validacion.puedoSeguir)
		{
			errores.add("",new ActionMessage(validacion.textoRespuesta));
		}
		/**Se verifica que no sea un ingreso de hospital día*/
		else if(paciente.isHospitalDia())
		{
			errores.add("",new ActionMessage("errores.paciente.ingresoHospitalDia"));
		}
		/**Debe tener un ingreso abierto**/
		else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			errores.add("",new ActionMessage("errors.paciente.noIngreso"));
		}
		/**Se verifica que el ingreso no sea atraves de entidades subcontratadas*/
		else if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
		{
				errores.add("",new ActionMessage("error.ingresoEntidadSubContratada"));
		}
		/**La via de ingreso debe ser hospitalizacion**/
		else if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			errores.add("",new ActionMessage("errors.paciente.noCuentaHospitalizacion"));
		}
		else if(!paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
		{
			errores.add("hospitalizado", new ActionMessage("errors.paciente.tipoPacienteNoAut") );
		}
		/**Se verifica que no tenga egreso**/
		else if(UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()))
		{
			errores.add("",new ActionMessage("error.trasladocama.egresoAbierto"));
		}
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			forma.reset();
			if (request.getParameter("origenLlamado")!=null)
				forma.setOrigenLlamado(request.getParameter("origenLlamado"));
			else
				forma.setOrigenLlamado("");
			if(forma.getOrigenLlamado().equals(""))
			{
				
				return mapping.findForward("paginaErroresActionErrors");
			}
			else
			{
				
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				
			}
		}
		return this.accionEmpezarPaciente(mapping, forma, con, paciente, trasladoCamas,medico,request);
	}

	/**
	 * Método que ordena el listado de traslados en la busqueda por fecha
	 * @param con
	 * @param trasladoCamasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumnaFecha(Connection con, TrasladoCamasForm trasladoCamasForm, ActionMapping mapping) 
	{

		String[] indices={
				"hora_",
	            "habitacionActual_",
	            "pisoCamaActual_",
	            "tipoHabitacionCamaActual_",
	            "camaActual_",
	            "descripcionCamaActual_",
	            "paciente_",
	            "tipoId_",
	            "fechaIngreso_",
	            "responsable_",
	            "habitacionAnterior_",
	            "camaAnterior_",
	            "pisoCamaAnterior_",
	            "tipoHabitacionCamaAnterior_",
	            "descripcionCamaAnterior_",
	            "fechaTraslado_",
	            "codigoPaciente_",
	            "codigoTraslado_"
    		};
		int tmp=Integer.parseInt(trasladoCamasForm.getMapaTrasladosFecha("numRegistros")+"");
		trasladoCamasForm.setMapaTrasladosFecha(Listado.ordenarMapa(indices,trasladoCamasForm.getPatronOrdenar(),trasladoCamasForm.getUltimoPatron(),trasladoCamasForm.getMapaTrasladosFecha(),Integer.parseInt(trasladoCamasForm.getMapaTrasladosFecha("numRegistros")+"")));
		trasladoCamasForm.setUltimoPatron(trasladoCamasForm.getPatronOrdenar());
		trasladoCamasForm.setMapaTrasladosFecha("numRegistros",tmp+"");
		trasladoCamasForm.setEstado("buscarTrasladoFecha");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("trasladosFecha");  
	}



		/**
		 * Accion de empezar el traslado por paciente, cargando la cama actual en la que se encuentra el paciente
		 * @param mapping
		 * @param trasladoCamasForm
		 * @param con
		 * @param paciente
		 * @param trasladoCamas
		 * @return
		 * @throws Exception
		 */
		private ActionForward accionEmpezarPaciente(ActionMapping mapping, TrasladoCamasForm trasladoCamasForm, Connection con, PersonaBasica paciente, TrasladoCamas trasladoCamas, UsuarioBasico usuario,HttpServletRequest request) throws Exception
		{
			trasladoCamasForm.reset();
			trasladoCamasForm.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
			if (request.getParameter("origenLlamado")!=null) {
				trasladoCamasForm.setOrigenLlamado(request.getParameter("origenLlamado"));
			}
			else {
				trasladoCamasForm.setOrigenLlamado("");
			}

			logger.info("Origen del Llamado: " + trasladoCamasForm.getOrigenLlamado() + " ** " );
			logger.info("codigo Paciente: " + paciente.getCodigoPersona());
			
			
			trasladoCamasForm.setCentroCosto("");
			trasladoCamasForm.setHoraTraslado(UtilidadFecha.getHoraActual());
			trasladoCamasForm.setMapaCamaActualPaciente(trasladoCamas.cargarCamaActualPaciente(con, paciente.getCodigoPersona()));
			
			
			
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.info("Cama: " + trasladoCamasForm.getCama());
			logger.info("Cama Antigua: " + trasladoCamasForm.getCamaAntigua());
			logger.info("Cama Reservada: " + trasladoCamasForm.getCamaReservada());
			
			trasladoCamasForm.setFechaAdmision(Utilidades.obtenerFechaAdmisionHosp(con, paciente.getCodigoCuenta()));
			trasladoCamasForm.setCodigoPaciente(paciente.getCodigoPersona());
			trasladoCamasForm.setMapaCamaNuevaPaciente(trasladoCamas.cargarCamaReservadaPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion()));

			
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.info("Cama: " + trasladoCamasForm.getCama());
			logger.info("Cama Antigua: " + trasladoCamasForm.getCamaAntigua());
			logger.info("Cama Reservada: " + trasladoCamasForm.getCamaReservada());
			

			Utilidades.imprimirMapa(trasladoCamasForm.getMapaCamaActualPaciente());
			Utilidades.imprimirMapa(trasladoCamasForm.getMapaCamaNuevaPaciente());
			Utilidades.imprimirMapa(trasladoCamasForm.getMapaDetalleTrasladoFecha());
			Utilidades.imprimirMapa(trasladoCamasForm.getMapaTrasladosFecha());
			
			
			
			//se verifica si el paciente tenia una cama reservada, de sera asi
			//se saca el codigo en una variable en el form par aluego verificar
			//si tomo esta cama o la cambio
			if (trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")!= null && !trasladoCamasForm.getMapaCamaNuevaPaciente("codigo").equals(""))
				{
					trasladoCamasForm.setCama(trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")+"");
					trasladoCamasForm.setCamaReservada(trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")+"");
					
					if (trasladoCamasForm.getMapaCamaNuevaPaciente("esUci").equals("SI"))
						trasladoCamasForm.setMapaCamaNuevaPaciente("tipoMonitoreo", trasladoCamasForm.getMapaCamaNuevaPaciente("esUci")+" ("+trasladoCamasForm.getMapaCamaNuevaPaciente("tipoMonitoreo")+")");
					else
						trasladoCamasForm.setMapaCamaNuevaPaciente("tipoMonitoreo", "No");
				}
				
			UtilidadBD.closeConnection(con);
			return mapping.findForward("trasladoPaciente");
		}
		
		
		/**
		 * Accion para empezar la busqueda de traslados de camas por area
		 * @param mapping
		 * @param trasladoCamasForm
		 * @return
		 * @throws Exception
		 */
		private ActionForward accionEmpezarArea(ActionMapping mapping, TrasladoCamasForm trasladoCamasForm, Connection con) throws Exception
		{
			trasladoCamasForm.reset();
			trasladoCamasForm.setCentroCosto("");
			trasladoCamasForm.setHoraTraslado(UtilidadFecha.getHoraActual());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("empezarArea");
		}
		
	 
		/**
		 * Accion que me muestra el resultado de la busqeuda de traslados de camas por centros de costo
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionResultadoBusquedaTrasladoArea (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping,  Connection con,UsuarioBasico usuario ,HttpServletRequest request,HttpServletResponse response) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    trasladoCamasForm.resetpager();
			trasladoCamasForm.setHoraTraslado(UtilidadFecha.getHoraActual());
		    trasladoCamasForm.setMapaTrasladosArea(trasladoCamas.cargarTrasladosArea(con,(Integer.parseInt(trasladoCamasForm.getCentroCosto()))));
			UtilidadBD.closeConnection(con);
			mapping.findForward("busquedaArea");	
			return 	UtilidadSesion.redireccionar(trasladoCamasForm.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("numRegistros").toString()), response, request, "trasladoArea.jsp", false);
			
		}

		/**
		 * Acción que permite el ordenamiento por cualquiera de las columnas
		 * en el listado de traslados de camas por Area
		 * @param con
		 * @param trasladoCamasForm
		 * @param mapping
		 * @return
		 */
	    private ActionForward accionOrdenarColumnaArea(Connection con, TrasladoCamasForm trasladoCamasForm, ActionMapping mapping) 
	    {
	        String[] indices={
	        					"hora_",
					            "habitacion_", 
					            "cama_", 
					            "descripcionCama_", 
					            "paciente_",
					            "tipoId_",
								"fechaIngreso_",
								"responsable_",
								"codigoPaciente_",
								"piso_",
								"tipo_habitacion_"
		            		};
	        
			
				
			
	        int tmp=Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("numRegistros")+"");
	        trasladoCamasForm.setMapaTrasladosArea(Listado.ordenarMapa(indices,trasladoCamasForm.getPatronOrdenar(),trasladoCamasForm.getUltimoPatron(),trasladoCamasForm.getMapaTrasladosArea(),Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("numRegistros")+"")));
	        trasladoCamasForm.setUltimoPatron(trasladoCamasForm.getPatronOrdenar());
	        trasladoCamasForm.setMapaTrasladosArea("numRegistros",tmp+"");
	        trasladoCamasForm.setEstado("resultadoBusqueda");
	        UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaArea");  
	    }
	    
	    
	    /**
	     * Accion para ver elñ detalle de un paciente con su cama actual y poder asignarle una nueva
	     * @param trasladoCamasForm
	     * @param mapping
	     * @param con
	     * @param request 
	     * @return
	     * @throws SQLException
	     */
		private ActionForward accionDetalleTrasladoArea(TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, UsuarioBasico medico, PersonaBasica paciente, HttpServletRequest request) throws SQLException
		{
			int posicion=trasladoCamasForm.getPosicionMapa();
			TrasladoCamas trasladoCamas= new TrasladoCamas();
			//trasladoCamasForm.setCentroCosto("");
			/**Cargamos la informacion de la cama actual del paciente dado su codigo**/
			trasladoCamasForm.setMapaCamaActualPaciente(trasladoCamas.cargarCamaActualPaciente(con,(Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("codigoPaciente_"+posicion)+""))));
			
			/**para cargar el paciente que corresponda al traslado**/
			paciente.setCodigoPersona((Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("codigoPaciente_"+posicion)+"")));
			paciente.cargar(con,(Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("codigoPaciente_"+posicion)+"")));
			paciente.cargarPaciente(con, (Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("codigoPaciente_"+posicion)+"")),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
			this.setObservable(paciente, getServlet());
			
			
			if(UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()))
			{
				return ComunAction.accionSalirCasoError(mapping, request, con,logger, "Paciente con egreso","error.trasladocama.egresoAbierto2", true);
			}
			
			trasladoCamasForm.setMapaCamaNuevaPaciente(trasladoCamas.cargarCamaReservadaPaciente(con, Integer.parseInt(trasladoCamasForm.getMapaTrasladosArea("codigoPaciente_"+posicion)+""), medico.getCodigoCentroAtencion()));
			//se verifica si el paciente tenia una cama reservada, de sera asi
			//se saca el codigo en una variable en el form par aluego verificar
			//si tomo esta cama o la cambio
			if (trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")!= null && !trasladoCamasForm.getMapaCamaNuevaPaciente("codigo").equals(""))
				{
					trasladoCamasForm.setCama(trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")+"");
					trasladoCamasForm.setCamaReservada(trasladoCamasForm.getMapaCamaNuevaPaciente("codigo")+"");
					
					if (trasladoCamasForm.getMapaCamaNuevaPaciente("esUci").equals("SI"))
						trasladoCamasForm.setMapaCamaNuevaPaciente("tipoMonitoreo", trasladoCamasForm.getMapaCamaNuevaPaciente("esUci")+" ("+trasladoCamasForm.getMapaCamaNuevaPaciente("tipoMonitoreo")+")");
					else
						trasladoCamasForm.setMapaCamaNuevaPaciente("tipoMonitoreo", "No");
				
				}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleNuevoTrasladoArea");		
		}
		
		
		/**
		 * Accion que carga todos los traslados de un paciente cargado en session
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionConsultarTrasladoPaciente (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, PersonaBasica paciente) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    /**Cargamos todos los traslados de un paciente**/
			trasladoCamasForm.setMapaTrasladosPaciente(trasladoCamas.cargarTrasladosPaciente(con,paciente.getCodigoPersona()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("trasladosPaciente");		
		}
		
		
		
		/**
		 * Acción que permite el ordenamiento por cualquiera de las columnas
		 * en el listado de traslados de camas por Area
		 * @param con 
		 * @param trasladoCamasForm
		 * @param mapping
		 * @return  
		 */ 
	    private ActionForward accionOrdenarColumnaTrasladosPaciente(Connection con, TrasladoCamasForm trasladoCamasForm, ActionMapping mapping) 
	    {
	        String[] indices={
	        		            "fechaHoraTraslado_",
								"habitacion_", 
								"cama_", 
								"descripcionCama_", 
								"tipoUsuario_",
								"codigoTraslado_",
								"piso_",
								"tipo_habitacion_"
		            		};

	        int tmp=Integer.parseInt(trasladoCamasForm.getMapaTrasladosPaciente("numRegistros")+"");
	        trasladoCamasForm.setMapaTrasladosPaciente(Listado.ordenarMapa(indices,trasladoCamasForm.getPatronOrdenar(),trasladoCamasForm.getUltimoPatron(),trasladoCamasForm.getMapaTrasladosPaciente(),Integer.parseInt(trasladoCamasForm.getMapaTrasladosPaciente("numRegistros")+"")));
	        trasladoCamasForm.setUltimoPatron(trasladoCamasForm.getPatronOrdenar());
	        trasladoCamasForm.setMapaTrasladosPaciente("numRegistros",tmp+"");
	        UtilidadBD.closeConnection(con);
			return mapping.findForward("trasladosPaciente");  
	    }
	    
	    
	    /**
		 * Acción que permite el ordenamiento por cualquiera de las columnas
		 * en el listado de traslados de camas por Area
		 * @param con 
		 * @param trasladoCamasForm
		 * @param mapping
		 * @return  
		 */ 
	    private ActionForward accionOrdenarColumnaIngAnteriores(Connection con, TrasladoCamasForm trasladoCamasForm, ActionMapping mapping) 
	    {
	        String[] indices={
	        					"centro_atencion_",
	        					"cuenta_",
	        					"estadoCuenta_",
	        					"codigoviaIngreso_",
	        					"viaIngreso_", 
	        					"fechaHoraIngreso_", 
	        					"fechaHoraEgreso_", 
	        					"especialidad_"
		            		};

	        int tmp=Integer.parseInt(trasladoCamasForm.getMapaTrasladosAnterioresPaciente("numRegistros")+"");
	        trasladoCamasForm.setMapaTrasladosAnterioresPaciente(Listado.ordenarMapa(indices,trasladoCamasForm.getPatronOrdenar(),trasladoCamasForm.getUltimoPatron(),trasladoCamasForm.getMapaTrasladosAnterioresPaciente(),Integer.parseInt(trasladoCamasForm.getMapaTrasladosAnterioresPaciente("numRegistros")+"")));
	        trasladoCamasForm.setUltimoPatron(trasladoCamasForm.getPatronOrdenar());
	        trasladoCamasForm.setMapaTrasladosAnterioresPaciente("numRegistros",tmp+"");
	        UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresosAnteriores");  
	    }
		
		
		
		/**
		 * Accion para guardar el traslado de un paciente
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @param paciente
		 * @param medico
		 * @param actionServlet 
		 * @return
		 * @throws SQLException
		 */
		public ActionForward accionGuardarTrasladoPaciente (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, PersonaBasica paciente, UsuarioBasico medico, HttpServletRequest request, boolean transaccionExterna, ActionServlet actionServlet) throws SQLException
		{	
			
			TrasladoCamas trasladoCamas= new TrasladoCamas();
			HashMap parametros = new HashMap();
			boolean cambioReserva = false;
			
		    /**Iniciamos la transaccion**/
			if(!transaccionExterna)
		    {
				UtilidadBD.iniciarTransaccion(con);
		    }
		    
			/**Para tomar el estado definido en valores por defecto para una cama despues de desocuparla**/
		    int tmp=Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt()));
			
		    /**Cargo el paciente**/
		    trasladoCamasForm.setCodigoPaciente(paciente.getCodigoPersona());

		    /**Cargo la informacion de la cama actual del paciente**/
		    trasladoCamasForm.setMapaCamaActualPaciente(trasladoCamas.cargarCamaActualPaciente(con, paciente.getCodigoPersona()));
		   
		    /**Codigo de la cama actual del paciente**/
		    int codigoCamaActual=Integer.parseInt(trasladoCamasForm.getMapaCamaActualPaciente("codigo_0").toString());
		    /**Codigo de la nueva cama que le voy a asignar al paciente**/
		    int codigoCamaNueva=Integer.parseInt(trasladoCamasForm.getCama());
		    /**Estado de la cama actual**/
		    int estadoCamaActual = Utilidades.obtenerCodigoEstadoCama(con,codigoCamaActual);
		    
		    logger.info("\n\n ** el valor de la cama actua es "+codigoCamaActual+" el valor de la cama nueva es "+codigoCamaNueva);
		    ArrayList filtro = new ArrayList();
		    filtro.add(codigoCamaActual+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
		    filtro.clear();
		    filtro.add(codigoCamaNueva+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
		    if(Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaDisponible && Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaReservada)
			{
				ActionErrors errores = new ActionErrors();  
                errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
                saveErrors(request, errores);
                UtilidadBD.abortarTransaccion(con);
                UtilidadBD.closeConnection(con);            
                return mapping.findForward("trasladoPaciente"); 
			}
		    
		    /**Actualizo la fecha y hora de finalizacion de ocupacion de la cama actual del paciente**/
		    logger.info("\nactualizarfechoahoraFinalizacion--->"+trasladoCamas.actualizarFechaHoraFinalizacionNoTransaccional(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado()),trasladoCamasForm.getHoraTraslado(),""));
		    
		    /**Inserscion del tralado de cama**/
		   logger.info("\nInserciontraslado--->"+trasladoCamas.insertarTrasladoCamaPaciente(con, 
				   UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado()),
				   trasladoCamasForm.getHoraTraslado(),
				   codigoCamaNueva,
				   codigoCamaActual,
				   Integer.parseInt(medico.getCodigoInstitucion()),
				   medico.getLoginUsuario(),
				   paciente.getCodigoPersona(),
				   paciente.getCodigoCuenta(),
				   paciente.getCodigoConvenio(),""));
		   
		    /**Modifico el estado de la cama anterior al estado definido en Parametros Generales**/
		   logger.info("\nModifcarestadocama--->"+trasladoCamas.modificarEstadoCama(con,tmp,codigoCamaActual,medico.getCodigoInstitucionInt()));
	    	
		   //MT-4670 si la cama anterior está en estado "con salida", entonces la nueva cama deberá quedar en este mismo estado 
		   /**Modifico el estado de la cama a la cual se le traslada el paciente a estado adecuado**/
		   if(estadoCamaActual == ConstantesBD.codigoEstadoCamaConSalida)
			   logger.info("\nModifcar a con salida--->"+trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaConSalida,codigoCamaNueva,medico.getCodigoInstitucionInt()));
		   else
			   logger.info("\nModifcar a ocupada--->"+trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaOcupada,codigoCamaNueva,medico.getCodigoInstitucionInt()));
		   //FIN MT-467
		   
		    /**************************************************************************************
		     * ESTO FUE AGREGADA POR JHONY ALEXANDER DUQUE PARA EL MANEJO DE LAS CAMAS RESERVADAS
		     ***************************************************************************************/
		    
//		  aqui se valida si el paciente tiene cama reservada,
			//se ser asi entonces se valida si tomo o no la reserva.
			if (!trasladoCamasForm.getCamaReservada().equals(""))
			{
				
				if (trasladoCamasForm.getCamaReservada().equals(trasladoCamasForm.getCama()+""))
				{
					logger.info("\n\n *********** el paciente SI tomo la cama reservada ");
					//se le ingresan los parametros requeridos para que pueda cambiar el estado
					parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoOcupado);
					parametros.put("institucion",medico.getCodigoInstitucionInt());
					parametros.put("codigoCama",trasladoCamasForm.getCamaReservada());
					parametros.put("codigoPaciente",paciente.getCodigoPersona());
					parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
					parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaOcupada);
					cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
				}
				else
				{
					logger.info("\n\n *********** el paciente NO tomo la cama reservada ");
//					se le ingresan los parametros requeridos para que pueda cambiar el estado
					parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoCancelado);
					parametros.put("institucion",medico.getCodigoInstitucionInt());
					parametros.put("codigoCama",trasladoCamasForm.getCamaReservada());
					parametros.put("codigoPaciente",paciente.getCodigoPersona());
					parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
					parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaDisponible);
					cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
				
				}
				if (!cambioReserva)
				{
					logger.info("ocurrio un problema en el cambio de la reserva!!");
				}
			}	
			else
			{
				logger.info("\n\n *********** el paciente No tiene cama reservada ");
			}
				
		    
		    //paciente.cargarPaciente2(con, paciente.getCodigoPersona(), medico.getCodigoInstitucion());
			if(!transaccionExterna)
		    {
				UtilidadBD.finalizarTransaccion(con);
			    UtilidadBD.closeConnection(con);
		    }
		    this.setObservable(paciente, actionServlet);
			return mapping.findForward("guardarTrasladoPaciente");
	    	
		}
	
		
		/**
		 * Accion apra guardar el traslado de cama de un paciente cuando se ingresa por traslados de area
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @param paciente
		 * @param medico
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionGuardarTrasladoArea (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, PersonaBasica paciente, UsuarioBasico medico, HttpServletRequest request) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    HashMap parametros = new HashMap ();
		    boolean cambioReserva = false; 
		    /**Iniciamos la transaccion**/
		    UtilidadBD.iniciarTransaccion(con);
		    
		    /**Para tomar el estado definido en valores por defecto para una cama despues de desocuparla**/
		    int tmp=Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt()));
		    
		    trasladoCamasForm.setCodigoPaciente(paciente.getCodigoPersona());
		    
		    trasladoCamasForm.setFechaAdmision(Utilidades.obtenerFechaAdmisionHosp(con, paciente.getCodigoCuenta()));
		    
		    /**Cargo la informacion de la cama actual del paciente**/
		    trasladoCamasForm.setMapaCamaActualPaciente(trasladoCamas.cargarCamaActualPaciente(con, paciente.getCodigoPersona()));
		    
		    /**Codigo de la cama actual del paciente**/
		    int codigoCamaActual=Integer.parseInt(trasladoCamasForm.getMapaCamaActualPaciente("codigo_0").toString());
		    /**Codigo de la nueva cama que le voy a asignar al paciente**/
		    int codigoCamaNueva=Integer.parseInt(trasladoCamasForm.getCama());
		    /**Estado de la cama actual**/
		    int estadoCamaActual = Utilidades.obtenerCodigoEstadoCama(con,codigoCamaActual);
		    
		    /**Validacion de concurrencia en el momento de asignar la cama**/
		    ArrayList filtro = new ArrayList();
		    filtro.add(codigoCamaActual+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
		    filtro.clear();
		    filtro.add(codigoCamaNueva+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
		    if(Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaDisponible && Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaReservada)
			{
				ActionErrors errores = new ActionErrors();  
                errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
                saveErrors(request, errores);
                UtilidadBD.abortarTransaccion(con);
                UtilidadBD.closeConnection(con);            
                return mapping.findForward("detalleNuevoTrasladoArea"); 
			}
		    
		    /**Actualizo la fecha y hora de finalizacion de ocupacion de la cama actual del paciente**/
		    trasladoCamas.actualizarFechaHoraFinalizacionNoTransaccional(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado()),trasladoCamasForm.getHoraTraslado(),"");
		    
		    /**Inserscion del tralado de cama**/
		    trasladoCamas.insertarTrasladoCamaPaciente(con, UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado()), trasladoCamasForm.getHoraTraslado(),codigoCamaNueva,codigoCamaActual,Integer.parseInt(medico.getCodigoInstitucion()), medico.getLoginUsuario(), paciente.getCodigoPersona(),paciente.getCodigoCuenta(), paciente.getCodigoConvenio(),"");
		    
		    /**Modifico el estado de la cama anterior al estado definido en Parametros Generales**/
	    	trasladoCamas.modificarEstadoCama(con,tmp,codigoCamaActual,medico.getCodigoInstitucionInt());
	    	
	    	//MT-4670 si la cama anterior está en estado "con salida", entonces la nueva cama deberá quedar en este mismo estado 
			   /**Modifico el estado de la cama a la cual se le traslada el paciente a estado adecuado**/
			   if(estadoCamaActual == ConstantesBD.codigoEstadoCamaConSalida)
				   trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaConSalida,codigoCamaNueva,medico.getCodigoInstitucionInt());
			   else
				   trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaOcupada,codigoCamaNueva,medico.getCodigoInstitucionInt());
			//FIN MT-4670	    	  
		    
		    /**************************************************************************************
			  * ESTA FUE AGREGADA POR JHONY ALEXANDER DUQUE PARA EL MANEJO DE LAS CAMAS RESERVADAS
			  ***************************************************************************************/
			    
//			  aqui se valida si el paciente tiene cama reservada,
				//se ser asi entonces se valida si tomo o no la reserva.
				if (!trasladoCamasForm.getCamaReservada().equals(""))
				{
					
					if (trasladoCamasForm.getCamaReservada().equals(trasladoCamasForm.getCama()+""))
					{
						logger.info("\n\n *********** el paciente SI tomo la cama reservada ");
						//se le ingresan los parametros para requeridos para que pueda cambiar el estado
						parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoOcupado);
						parametros.put("institucion",medico.getCodigoInstitucionInt());
						parametros.put("codigoCama",trasladoCamasForm.getCamaReservada());
						parametros.put("codigoPaciente",paciente.getCodigoPersona());
						parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
						parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaOcupada);
						cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
					}
					else
					{
						logger.info("\n\n *********** el paciente NO tomo la cama reservada ");
//						se le ingresan los parametros para requeridos para que pueda cambiar el estado
						parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoCancelado);
						parametros.put("institucion",medico.getCodigoInstitucionInt());
						parametros.put("codigoCama",trasladoCamasForm.getCamaReservada());
						parametros.put("codigoPaciente",paciente.getCodigoPersona());
						parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
						parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaDisponible);
						cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
					
					}
					if (!cambioReserva)
						logger.info("ocurrio un problema en el cambio de la reserva!!");
				}	
				else
					logger.info("\n\n *********** el paciente No tiene cama reservada ");
					
		    
		    UtilidadBD.finalizarTransaccion(con);
		    this.setObservable(paciente, getServlet());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("guardarTrasladoArea");		
			
		}
		
		/**
		 * Accion para poder ver el detalle de un traslado realizado a un paciente
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionDetalleTrasladoPaciente(TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con) throws SQLException
		{
			int posicion=trasladoCamasForm.getPosicionMapa();
			TrasladoCamas trasladoCamas= new TrasladoCamas();
			/**Cargamos el detalle de un traslado dada la cama actual del paciente**/
			trasladoCamasForm.setMapaDetalleTrasladoPaciente(trasladoCamas.cargarDetalleTrasladoPaciente(con,Integer.parseInt(trasladoCamasForm.getMapaTrasladosPaciente("codigoTraslado_"+posicion)+"")));
			
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleIngresoAnteriorPaciente");		
		}
		
		/**
		 * Accion para poder ver el detalle de un traslado realizado a un paciente
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 * @throws IPSException 
		 */
		private ActionForward accionDetalleTrasladoAnteriorPaciente(TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, PersonaBasica paciente) throws IPSException
		{
			
//			return accionConsultarTrasladoPaciente(trasladoCamasForm, mapping, con, paciente);
//			int posicion=trasladoCamasForm.getPosicionMapa();
//			TrasladoCamas trasladoCamas= new TrasladoCamas();
//			/**Cargamos los Traslados anteriores**/
//			trasladoCamasForm.setMapaTrasladosAnterioresPaciente(trasladoCamas.cargarIngresosAnteriores(con, paciente.getCodigoPersona()));
//			/**Cargamos el detalle de un traslado dada la cuenta**/
//			trasladoCamasForm.setMapaDetalleTrasladoPaciente(trasladoCamas.cargarDetalleTrasladoAnteriorPaciente(con,Integer.parseInt(trasladoCamasForm.getMapaTrasladosAnterioresPaciente("cuenta_"+posicion)+"")));
//
//			UtilidadBD.closeConnection(con);
//			return mapping.findForward("detalleIngresoAnteriorPaciente");
			
			int posicion=trasladoCamasForm.getPosicionMapa();
			int cuenta = Utilidades.convertirAEntero(trasladoCamasForm.getMapaTrasladosAnterioresPaciente("cuenta_"+posicion)+"");
			TrasladoCamas trasladoCamas= new TrasladoCamas();
			trasladoCamasForm.setMapaTrasladosPaciente(trasladoCamas.cargarTrasladosPacientePorCuenta(con,cuenta));
			return mapping.findForward("trasladosPaciente");
		}
		
		
		/**
		 * Accion para ver los ingreso anteriores de un paciente cargado en session
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @param paciente
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionConsultarIngresosAnteriores (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, PersonaBasica paciente) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    /**Cargamos la informacion de los ingresos de anteriores de un paciente**/
			trasladoCamasForm.setMapaTrasladosAnterioresPaciente(trasladoCamas.cargarIngresosAnteriores(con,paciente.getCodigoPersona()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresosAnteriores");		
			
		}
		
		
		
		
		
		/**
		 * Accion que carga todos los traslados de una fecha
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @param medico 
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionConsultarTrasladoFecha (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con, UsuarioBasico medico) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    
	     	trasladoCamasForm.setFechaTraslado(UtilidadFecha.getFechaActual());
	     	trasladoCamasForm.setCodigoCentroAtencion(medico.getCodigoCentroAtencion());
		    /**Cargamos toda la informacion de los traslados**/
	    	trasladoCamasForm.setMapaTrasladosFecha(trasladoCamas.cargarTrasladosFecha(con,UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado()),trasladoCamasForm.getCodigoCentroAtencion()));
	    	UtilidadBD.closeConnection(con);
			return mapping.findForward("trasladosFecha");		
			
		}
		
		/**
		 * Accion de busuqeda de traslado por fecha
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionConsultarTrasladoFechaBusqueda (TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con) throws SQLException
		{	
		    TrasladoCamas trasladoCamas= new TrasladoCamas();
		    String fechaTmp=UtilidadFecha.conversionFormatoFechaABD(trasladoCamasForm.getFechaTraslado());
		    trasladoCamasForm.setFechaTraslado(UtilidadFecha.conversionFormatoFechaAAp(fechaTmp));
		    if(trasladoCamasForm.getFechaTraslado().equals(""))
		    {
		    	trasladoCamasForm.setMapaTrasladosFecha(trasladoCamas.cargarTrasladosFecha(con,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),trasladoCamasForm.getCodigoCentroAtencion()));
		    }
		    else
		    {
		    	trasladoCamasForm.setMapaTrasladosFecha(trasladoCamas.cargarTrasladosFecha(con,fechaTmp,trasladoCamasForm.getCodigoCentroAtencion()));
		    }
			UtilidadBD.closeConnection(con);
			return mapping.findForward("trasladosFecha");		
			
		}
		
		/**
		 * Accion para poder ver el detalle de un traslado realizado a un paciente
		 * @param trasladoCamasForm
		 * @param mapping
		 * @param con
		 * @return
		 * @throws SQLException
		 */
		private ActionForward accionDetalleTrasladoFecha(TrasladoCamasForm trasladoCamasForm, ActionMapping mapping, Connection con) throws SQLException
		{
			int posicion=trasladoCamasForm.getPosicionMapa();
			TrasladoCamas trasladoCamas= new TrasladoCamas();
			trasladoCamasForm.setMapaDetalleTrasladoPaciente(trasladoCamas.cargarDetalleTrasladoPaciente(con, Integer.parseInt(trasladoCamasForm.getMapaTrasladosFecha("codigoTraslado_"+posicion)+"")));
						
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleTrasladoFecha");		
		}
		
		
		
		
		
		
		/**
		 * Acción de Imprimir El listado de facturas despues de la busqueda
		 * @param mapping
		 * @param request
		 * @param con
		 * @param consultaFacturasForm
		 * @param medico
		 * @return
		 * @throws Exception
		 */
		private ActionForward accionImprimir(ActionMapping mapping, HttpServletRequest request, Connection con, TrasladoCamasForm trasladoCamasForm, UsuarioBasico medico, PersonaBasica paciente) throws Exception
		{
			String nombreArchivo;
			Random r= new Random();

			nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
			
			TrasladoCamasPdf.pdfTrasladoCamasFechas(ValoresPorDefecto.getFilePath()+ nombreArchivo,trasladoCamasForm,medico,paciente );			
			
			UtilidadBD.closeConnection(con);
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Traslado Camas");
			return mapping.findForward("abrirPdf");
		}

		
	 /**
		* Método para hacer que el paciente
		* pueda ser visto por todos los usuario en la aplicacion
		* @param paciente
		*/
		private void setObservable(PersonaBasica paciente, ActionServlet servlet)
		{
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			if (observable != null) {
				synchronized (observable) {
					observable.setChanged();
					observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
				}
			}
		}

}