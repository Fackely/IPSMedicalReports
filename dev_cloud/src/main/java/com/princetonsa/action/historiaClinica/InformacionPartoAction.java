/*
 * Junio 06, 2006
 */
package com.princetonsa.action.historiaClinica;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.InformacionPartoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.InformacionParto;
import com.princetonsa.mundo.historiaClinica.InformacionRecienNacidos;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Informacion del Parto
 */
public class InformacionPartoAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(InformacionPartoAction.class);
	
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
			if(form instanceof InformacionPartoForm)
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
				InformacionPartoForm partoForm =(InformacionPartoForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado=partoForm.getEstado(); 
				logger.warn("estado InformacionPartoAction-->"+estado);

				//*********VALIDACIONES****************************************
				if(!UtilidadValidacion.esProfesionalSalud(usuario))
				{
					return ComunAction.accionSalirCasoError(mapping,request,con,logger,"No es profesional de la salud","errors.noProfesionalSalud",true);
				}
				//**************************************************************


				if(estado == null)
				{
					partoForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Informacion Parto (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				//*********ESTADOS OPCION PACIENTE****************
				else if (estado.equals("empezarPaciente"))
				{
					return accionEmpezarPaciente(con,partoForm,mapping,usuario,paciente,request);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,partoForm,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,partoForm,mapping);
				}
				else if (estado.equals("detalle"))
				{
					return accionDetalle(con,partoForm,mapping,usuario,paciente);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,partoForm,mapping,request,usuario,paciente);
				}
				//******************************************
				//*********ESTADOS OPCION PERIODO****************
				else if (estado.equals("empezarPeriodo"))
				{
					return empezarPeriodo(con,partoForm,mapping);
				}
				else if (estado.equals("buscar"))
				{
					return accionBuscar(con,partoForm,mapping,usuario,request);
				}
				else if(estado.equals("nuevoElementoVigilanciaMapa"))
				{
					return this.accionNuevoElementoVigilanciaMapa(partoForm,con, mapping);
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresar");
				}
				//*********ESTADO EXTRA (AJAX)********************
				else if (estado.equals("blanquearEnfermedades"))
				{
					return this.accionBlanquearEnfermedades(con,partoForm);
				}
				else
				{
					partoForm.reset();
					logger.warn("Estado no valido dentro del flujo de InformacionPartoAction (null) ");
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
	 * Método implementado para borrar el campo valor en la seccion de enfermedades, con el fin
	 * de que se haga efectivo el cambio en los disable de los radios HTML
	 * @param con
	 * @param partoForm
	 * @return
	 */
	private ActionForward accionBlanquearEnfermedades(Connection con, InformacionPartoForm partoForm) 
	{
		//Se toma el mapa de enfermedades
		HashMap mapTemp = (HashMap)partoForm.getInfoParto("mapaEnfermedades");
		int numRegistros = Integer.parseInt(mapTemp.get("numRegistros").toString());
		//Se borra el campo nulo
		for(int i=0;i<numRegistros;i++)
			mapTemp.put("valor_"+i,null);
		partoForm.setInfoParto("mapaEnfermedades", mapTemp);
		UtilidadBD.closeConnection(con);
		return null;
		
	}

	/**
	 * Método implementad para buscar admisiones para informacion de parto por rango de fechas
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, InformacionPartoForm partoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		//*******************************************************************************
		
		//Consulta de la solicitudes para la informacion del parto
		InformacionParto parto = new InformacionParto();
		partoForm.setListadoSolicitudes(parto.cargarSolicitudesInformacionParto(con,"",partoForm.getFechaInicial(),partoForm.getFechaFinal(),usuario.getCodigoCentroAtencion()));
		partoForm.setNumSolicitudes(Integer.parseInt(partoForm.getListadoSolicitudes("numRegistros").toString()));
		
		if(partoForm.getNumSolicitudes()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"Paciente sin admisiones","error.validacionessolicitud.sinAdmisionesInfoPartoPeriodo",true);
		}
		else
		{
			partoForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listado");
		}
	}

	/**
	 * Método que postula el inicio de la opcion periodo
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @return
	 */
	private ActionForward empezarPeriodo(Connection con, InformacionPartoForm partoForm, ActionMapping mapping) 
	{
		partoForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busqueda");
	}

	/**
	 * Método implementado para ingresar la informacion del parto
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, InformacionPartoForm partoForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		//Se llenan las observaciones (si se ingresaron)
		String tempObservaciones = partoForm.getInfoParto("observacionesNuevas").toString(); 
		if(!partoForm.getInfoParto("observacionesNuevas").toString().equals(""))
			partoForm.setInfoParto("observacionesNuevas",UtilidadTexto.agregarTextoAObservacion(partoForm.getInfoParto("observacionesAnteriores").toString(), partoForm.getInfoParto("observacionesNuevas").toString(), usuario, false));
		else if(!partoForm.getInfoParto("observacionesAnteriores").toString().equals(""))
			partoForm.setInfoParto("observacionesNuevas",partoForm.getInfoParto("observacionesAnteriores").toString());
		
		//hermorhu - MT5765
		if(partoForm.getInfoParto("esAborto").toString().isEmpty()) {
			partoForm.setInfoParto("parto", ConstantesBD.acronimoSi);
		} else {
			partoForm.setInfoParto("parto", ConstantesBD.acronimoNo);
		}
		
		
		//se instancia el objeto del InformacionParto
		InformacionParto parto = new InformacionParto();
		this.llenarMundo(parto,partoForm);
		
		
		
		//*****************VALIDACIONES PRE-GUARDAR************************************************************
		if(!partoForm.getInfoParto("numeroEmbarazo").toString().equals("")&&!UtilidadTexto.getBoolean(partoForm.getInfoParto("controlPrenatal").toString()))
		{
			if(parto.existeNumeroEmbarazoHojaObstetrica(con, partoForm.getInfoParto("codigoPaciente").toString(), Integer.parseInt(partoForm.getInfoParto("numeroEmbarazo").toString())))
			{
				//Ya existe un numero de embarazo en la hoja obstétrica
				UtilidadBD.closeConnection(con);
				errores.add("Ya existe numero de embarazo",new ActionMessage("errors.yaExisteAmplio","El número de embarazo","en la información de hoja obstétrica. Por favor realizar el cambio"));
				saveErrors(request, errores);
				//se reeestablecen las observaciones
				partoForm.setInfoParto("observacionesNuevas",tempObservaciones);
				return mapping.findForward("ingresar");
			}
		}
		//******************************************************************************************************
		
		
		
		UtilidadBD.iniciarTransaccion(con);
		boolean exito = parto.insertar(con,ConstantesBD.continuarTransaccion);
		
		if(exito)
		{
			exito= parto.modificarVigilanciaParto(con, usuario.getLoginUsuario());
		}
		if(exito)
		{
			//se inserta la vig clinica del trabajo de parto
			exito= parto.insertarVigilenciaClinica(con, usuario.getLoginUsuario());
		}
		if(exito)
		{
			exito= parto.insertarPartogramaGeneral(con, parto.getConsecutivo(), partoForm.getPartogramaMap(), usuario.getLoginUsuario());
		}
		if(exito)
		{
			UtilidadBD.finalizarTransaccion(con);
			return accionDetalle(con, partoForm, mapping, usuario,paciente);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			errores.add("Error en la base de datos",new ActionMessage("errors.noSeGraboInformacion","DEL PARTO"));
			saveErrors(request, errores);
			partoForm.setEstado("detalle");
			//se reeestablecen las observaciones
			partoForm.setInfoParto("observacionesNuevas",tempObservaciones);
			return mapping.findForward("ingresar");
		}
		
	}

	

	/**
	 * Método que llena el mundo con los datos de la forma
	 * @param parto
	 * @param partoForm
	 */
	private void llenarMundo(InformacionParto parto, InformacionPartoForm partoForm) 
	{
		parto.setInfoParto(partoForm.getInfoParto());
		parto.getInfoParto().put("mapaIdentificacion", partoForm.getMapaIdentificacion());
		parto.getInfoParto().put("mapaEnfermedades", partoForm.getMapaEnfermedades());
		parto.getInfoParto().put("mapaMedicacion", partoForm.getMapaMedicacion());
		parto.getInfoParto().put("mapaTransfusion", partoForm.getMapaTransfusion());
		parto.setVigilanciaClinicaMap(partoForm.getVigilanciaClinicaMap());
	}

	/**
	 * Método implementado para cargar los datos de la información del parto a la forma
	 * @param parto
	 * @param partoForm
	 */
	private void llenarForma(InformacionParto parto, InformacionPartoForm partoForm) 
	{
		partoForm.setInfoParto(parto.getInfoParto());
		partoForm.setMapaIdentificacion((HashMap)parto.getInfoParto().get("mapaIdentificacion"));
		partoForm.setMapaEnfermedades((HashMap)parto.getInfoParto().get("mapaEnfermedades"));
		partoForm.setMapaMedicacion((HashMap)parto.getInfoParto().get("mapaMedicacion"));
		partoForm.setMapaTransfusion((HashMap)parto.getInfoParto().get("mapaTransfusion"));
		
	}

	/**
	 * Método implementado para postular el detalle de una admisión para ingresar
	 * su respectiva información de parto
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @param paciente
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, InformacionPartoForm partoForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		//Se toma posicion del registro seleccionado
		int pos = partoForm.getPos();
		// se toma el número de la solicitud
		partoForm.setNumeroSolicitud(partoForm.getListadoSolicitudes("numero_solicitud_"+pos).toString());
		partoForm.setLoginUsuario(usuario.getLoginUsuario());
		partoForm.setInfoParto(new HashMap());
		partoForm.inicializarInfoParto();
		//Se instacnia objeto de InformacionParto
		InformacionParto parto = new InformacionParto();
		
		//Se consulta registro existente de informacion de parto*****************************************************************************
		parto.setCodigoCirugia(parto.obtenerCodigoCirugiaParto(con,partoForm.getNumeroSolicitud()));
		parto.cargar(con,usuario.getCodigoInstitucion());
		this.llenarForma(parto, partoForm);
		
		//Se consultan datos del egreso de la admision (SI LO TIENE)******************************************************************************
		String egreso = parto.obtenerFechaHoraEgresoAdmisionParto(con,partoForm.getListadoSolicitudes("cuenta_"+pos).toString());
		
		if(!egreso.equals(""))
		{
			if(partoForm.getInfoParto("fechaEgreso").toString().equals(""))
				partoForm.setInfoParto("fechaEgreso",UtilidadFecha.conversionFormatoFechaAAp(egreso.split(ConstantesBD.separadorSplit)[0]));
			if(partoForm.getInfoParto("horaEgreso").toString().equals(""))
				partoForm.setInfoParto("horaEgreso",egreso.split(ConstantesBD.separadorSplit)[1]);
			partoForm.setFechaEgreso(egreso.split(ConstantesBD.separadorSplit)[0]);
			partoForm.setHoraEgreso(UtilidadFecha.convertirHoraACincoCaracteres(egreso.split(ConstantesBD.separadorSplit)[1]));
		}
		else
		{
			partoForm.setFechaEgreso("");
			partoForm.setHoraEgreso("");
		}
		
		//Se asignan los datos básicos e iniciales del paciente y la informacion del parto********************************************************
		partoForm.setInfoParto("apellidosPaciente",partoForm.getListadoSolicitudes("apellidos_paciente_"+pos));
		partoForm.setInfoParto("nombresPaciente",partoForm.getListadoSolicitudes("nombres_paciente_"+pos));
		partoForm.setInfoParto("tipoIdPaciente",partoForm.getListadoSolicitudes("tipo_id_"+pos));
		partoForm.setInfoParto("numeroIdPaciente",partoForm.getListadoSolicitudes("numero_id_"+pos));
		partoForm.setInfoParto("ordenCirugia",partoForm.getListadoSolicitudes("orden_"+pos));
		partoForm.setInfoParto("codigoPaciente",partoForm.getListadoSolicitudes("codigo_paciente_"+pos));
		partoForm.setInfoParto("fechaAdmision",UtilidadFecha.conversionFormatoFechaAAp(partoForm.getListadoSolicitudes("fecha_admision_"+pos).toString()));
		partoForm.setInfoParto("admision", partoForm.getListadoSolicitudes("numero_admision_"+pos));
		partoForm.setInfoParto("numeroSolicitud",partoForm.getListadoSolicitudes("numero_solicitud_"+pos));
		partoForm.setInfoParto("solicitud", Utilidades.getConsecutivoOrdenesMedicas(con, Integer.parseInt(partoForm.getListadoSolicitudes("numero_solicitud_"+pos)+"")));
		partoForm.setInfoParto("horaAdmision",UtilidadFecha.convertirHoraACincoCaracteres(partoForm.getListadoSolicitudes("hora_admision_"+pos).toString()));
		partoForm.setInfoParto("usuario",usuario.getLoginUsuario());
		partoForm.setInfoParto("institucion",usuario.getCodigoInstitucion());
		partoForm.setInfoParto("codigoCirugia",parto.getCodigoCirugia());
		partoForm.setInfoParto("idCuenta", partoForm.getListadoSolicitudes("cuenta_"+pos));
		
		//Se consulta la fecha de ingreso (en el caso que no haya)***********************************************************************************
		//Se verifica el estado de la cuenta
		if(Integer.parseInt(partoForm.getListadoSolicitudes("estado_cuenta_"+pos).toString())==ConstantesBD.codigoEstadoCuentaAsociada)
		{
			String[] fechasIngreso = InformacionParto.consultarFechaIngresoCasoAsocio(con, partoForm.getInfoParto("idCuenta").toString());
			if(fechasIngreso.length==2)
			{
				partoForm.setInfoParto("fechaIngreso", fechasIngreso[0]);
				partoForm.setInfoParto("horaIngreso", fechasIngreso[1]);
			}
			//Quiere decir que es una cuenta asociada que todavía no tiene la admision de hospitalizacion
			else
			{
				partoForm.setInfoParto("fechaIngreso", partoForm.getInfoParto("fechaAdmision"));
				partoForm.setInfoParto("horaIngreso", partoForm.getInfoParto("horaAdmision"));
			}
		}
		else
		{
			partoForm.setInfoParto("fechaIngreso", partoForm.getInfoParto("fechaAdmision"));
			partoForm.setInfoParto("horaIngreso", partoForm.getInfoParto("horaAdmision"));
		}
		
		
		//Se verifica si existe embarazo no finalizado en la hoja obstétrica***********************************************************************
		int embarazo = parto.consultarNumeroEmbarazoHojaObstetrica(con, partoForm.getInfoParto("codigoPaciente").toString());
		if(embarazo>0)
		{
			//se revisa que no se haya insertado un numero de embarazo anteriormente
			if(partoForm.getInfoParto("numeroEmbarazo").toString().equals(""))
			{
				partoForm.setInfoParto("numeroEmbarazo", embarazo+"");
				partoForm.setInfoParto("controlPrenatal", ConstantesBD.acronimoSi);
				partoForm.setInfoParto("consultasPrenatal", parto.obtenerConsultasPrenatalesHojaObstetrica(con, partoForm.getInfoParto("codigoPaciente").toString(), embarazo));
			}
		}
		else
			partoForm.setInfoParto("controlPrenatal", ConstantesBD.acronimoNo);
		
		//se carga la informacion de la seccion de vigilancia clinica del trabajo de parto y partograma***********************************************
		if(!partoForm.getInfoParto("consecutivo").toString().equals(""))
		{
			partoForm.setVigilanciaClinicaMap(parto.cargarVigilanciaClinica(con, partoForm.getInfoParto("consecutivo").toString()));
			partoForm.setPartogramaMap(parto.cargarPartograma(con, partoForm.getInfoParto("consecutivo").toString()));
			partoForm.inicializarTagsPartograma();
		}	
		//**************************************************************************************************************************************
		
		//Se consulta el número de recién nacidos ingresados*************************************************************************************
		Vector recienNacidos = InformacionRecienNacidos.obtenerConsecutivosInfoRecienNacidoDadoCx(con, partoForm.getInfoParto("codigoCirugia").toString(),"");
		partoForm.setInfoParto("cantidadRecienNacidos", recienNacidos.size()+"");
		if(recienNacidos.size()>Integer.parseInt(partoForm.getInfoParto("cantidadHijos").toString()))
			partoForm.setInfoParto("cantidadHijos", recienNacidos.size()+"");
		
		String forward = "ingresar";
		if(UtilidadTexto.getBoolean(partoForm.getInfoParto("esFinalizado").toString()))
			forward = "resumen";
		
		///*****SE CARGA PACIENTE EN SESIÓN****************
		if(partoForm.getEstado().equals("detalle"))
		{
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.cargar(con, Integer.parseInt(partoForm.getInfoParto("codigoPaciente").toString()));
				paciente.cargarPaciente2(con, Integer.parseInt(partoForm.getInfoParto("codigoPaciente").toString()), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error en accionDetalle: "+e);
			}
			observable.addObserver(paciente);
			UtilidadSesion.notificarCambiosObserver(Integer.parseInt(partoForm.getInfoParto("codigoPaciente").toString()),servlet.getServletContext());
		}
		//**********************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward(forward);
	}

	/**
	 * Método implementado para ordenar el listado de admisiones
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, InformacionPartoForm partoForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"numero_admision_",
				"fecha_admision_",
				"hora_admision_",
				"orden_",
				"numero_solicitud_",
				"medico_interpreto_",
				"fecha_hora_admision_",
				"paciente_",
				"apellidos_paciente_",
				"nombres_paciente_",
				"tipo_id_",
				"numero_id_",
				"tipo_n_id_",
				"codigo_paciente_",
				"cuenta_",
				"estado_cuenta_"
			};
		
		int numeroElementos=partoForm.getNumSolicitudes();
		
		partoForm.setListadoSolicitudes(Listado.ordenarMapa(indices,
				partoForm.getIndice(),
				partoForm.getUltimoIndice(),
				partoForm.getListadoSolicitudes(),
				numeroElementos));
		
		partoForm.setListadoSolicitudes("numRegistros",numeroElementos+"");
		
		
		partoForm.setUltimoIndice(partoForm.getIndice());
		
		this.cerrarConexion(con);
		return mapping.findForward("listado");
	}

	/**
	 * Método implementado para paginar el listado de admisiones
	 * @param con
	 * @param partoForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, InformacionPartoForm partoForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    this.cerrarConexion(con);
			response.sendRedirect(partoForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ConsultaLiquidacionQxAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConsultaLiquidacionQxAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para iniciar la opcion Paciente
	 * @param con
	 * @param partoForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionEmpezarPaciente(Connection con, InformacionPartoForm partoForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		partoForm.reset();
		//*******VALIDACIONES DE EMPEZAR PACIENTE*********************************+
		//verificar si es null (Paciente está cargado)
		if((paciente==null || paciente.getTipoIdentificacionPersona().equals(""))){
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
		}
		else if(paciente.getCodigoSexo()!=ConstantesBD.codigoSexoFemenino)
		{
			return ComunAction.accionSalirCasoError(mapping,request, con, logger, "Paciente no es mujer", "errors.paciente.noEsFemenino",true);
		}	
		//*******************************************************************************
		
		//Consulta de la solicitudes para la informacion del parto
		InformacionParto parto = new InformacionParto();
		
		String mensajeError = parto.validacionInformacionPartoPaciente(con,paciente.getCodigoPersona()+"",usuario.getCodigoCentroAtencion());
		
		if(mensajeError.equals(""))
		{
		
			partoForm.setListadoSolicitudes(parto.cargarSolicitudesInformacionParto(con,paciente.getCodigoPersona()+"","","",usuario.getCodigoCentroAtencion()));
			partoForm.setNumSolicitudes(Integer.parseInt(partoForm.getListadoSolicitudes("numRegistros").toString()));
			
			if(partoForm.getNumSolicitudes()<=0)
			{
				return ComunAction.accionSalirCasoError(mapping,request,con,logger,"Paciente sin admisiones","error.validacionessolicitud.sinAdmisionesInfoParto",true);
			}
			else
			{
				partoForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.cerrarConexion(con);
				return mapping.findForward("listado");
			}
		}
		else
			return ComunAction.accionSalirCasoError(mapping,request,con,logger,"Paciente sin partos",mensajeError,true);		
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
	
	/**
	 * 
	 * @param partoForm
	 * @param con
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevoElementoVigilanciaMapa(InformacionPartoForm forma, Connection con, ActionMapping mapping )
	{
		int numRegistros= Integer.parseInt(forma.getVigilanciaClinicaMap("numRegistros").toString());
		forma.setSeccionVigilanciaClinica(true);
	    forma.setVigilanciaClinicaMap("consecutivo_"+numRegistros,"-2");
	    forma.setVigilanciaClinicaMap("consecutivoinfoparto_"+numRegistros, forma.getInfoParto("consecutivo").toString());
	    forma.setVigilanciaClinicaMap("hora_"+numRegistros,UtilidadFecha.getHoraActual());
	    forma.setVigilanciaClinicaMap("fechabd_"+numRegistros,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
	    forma.setVigilanciaClinicaMap("fecha_"+numRegistros,UtilidadFecha.getFechaActual());
	    forma.setVigilanciaClinicaMap("acronimoposicionmaterna_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("posicionmaterna_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("tensionarterial_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("tensionarterial1_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("pulsomaterno_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("acronimofreccardiacafetal_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("freccardiacafetal_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("duracioncontracciones_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("freccontracciones_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("acronimointensidaddolor_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("intensidaddolor_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("acronimolocalizaciondolor_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("localizaciondolor_"+numRegistros, "");
	    forma.setVigilanciaClinicaMap("estabd_"+numRegistros,"false");
	    forma.setVigilanciaClinicaMap("fueeliminado_"+numRegistros, "false");
	    forma.setVigilanciaClinicaMap("fcf_"+numRegistros,"");
	    forma.setVigilanciaClinicaMap("estacion_"+numRegistros,"");
	    
	    numRegistros+=1;
	    forma.setVigilanciaClinicaMap("numRegistros", numRegistros+"");
	    
	    UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresar");
	}
}
