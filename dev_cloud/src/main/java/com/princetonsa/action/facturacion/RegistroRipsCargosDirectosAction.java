/*
 * 11 Abril, 2008
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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.RegistroRipsCargosDirectosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.facturacion.RegistroRipsCargosDirectos;


/**
 * @author Sebastián Gómez R. 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Registro Rips Cargos Directos 
 */
public class RegistroRipsCargosDirectosAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RegistroRipsCargosDirectosAction.class);
	
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
			if(form instanceof RegistroRipsCargosDirectosForm)
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
				RegistroRipsCargosDirectosForm cargosForm =(RegistroRipsCargosDirectosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado=cargosForm.getEstado(); 
				logger.warn("estado RegistroRipsCargosDirectosAction-->"+estado+"*");

				if(estado == null)
				{
					cargosForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Registro Rips Cargos Directos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//********************ESTADOS INICIALES************************************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,cargosForm,mapping);
				}
				else if (estado.equals("empezarPaciente"))
				{
					return accionEmpezarPaciente(con,cargosForm,mapping,request,paciente);
				}
				else if (estado.equals("empezarPeriodo"))
				{
					return accionEmpezarPeriodo(con, cargosForm, mapping, usuario);
				}
				//Estado especial cuando se busca por periodo
				else if (estado.equals("buscarPeriodo"))
				{
					return accionBuscarPeriodo(con,cargosForm,mapping, request);
				}
				//*********************************************************************************
				else if (estado.equals("ordenarListadoCuenta")||estado.equals("ordenarListadoSolicitud"))
				{
					return accionOrdenarListado(con,cargosForm,mapping);
				}
				else if (estado.equals("volverCuentas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuentas");
				}
				else if (estado.equals("detalleCuenta"))
				{
					return accionDetalleCuenta(con,cargosForm,mapping,usuario,request,paciente);
				}
				else if (estado.equals("volverSolicitudes"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudes");
				}
				else if (estado.equals("detalleSolicitud"))
				{
					return accionDetalleSolicitud(con, cargosForm, mapping, usuario, paciente, request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,cargosForm, mapping, usuario, request, paciente);
				}
				//Estado para el flujo de la sección de recién nacidos
				else if (estado.equals("verificarHijos"))
				{
					return accionVerificarHijos(con, cargosForm, mapping);
				}
				//***********************************************************************************************************

				else
				{
					cargosForm.reset();
					logger.warn("Estado no valido dentro del flujo de CargosDirectosCxDytAction (null) ");
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
	 * Método implementado para realizar la búsqueda por periodo
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionBuscarPeriodo(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		//*******************************VALIDACIONES DE CAMPOS REQUERIDOS**************************************************
		//Validacion centro de atencion
		if(cargosForm.getParametros("centroAtencion").toString().equals(""))
			errores.add("",new ActionMessage("errors.required","El centro de atención"));
		
		
		boolean fechaInicialValida = false, fechaFinalValida = false;
		String fechaInicial = cargosForm.getParametros("fechaInicial").toString(), fechaFinal = cargosForm.getParametros("fechaFinal").toString();
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		
		//Validacion fecha inicial
		if(fechaInicial.equals(""))
			errores.add("",new ActionMessage("errors.required","La fecha inicial cta."));
		else if(!UtilidadFecha.validarFecha(fechaInicial))
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","inicial cta."));
		else
		{
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial, fechaSistema))
				errores.add("",new ActionMessage("errors.fechaPosteriorIgualActual","inicial cta.","del sistema: "+fechaSistema));
			fechaInicialValida = true;
		}
		
		//Validacion fecha final
		if(fechaFinal.equals(""))
			errores.add("",new ActionMessage("errors.required","La fecha final cta."));
		else if(!UtilidadFecha.validarFecha(fechaFinal))
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","final cta."));
		else
		{
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFinal, fechaSistema))
				errores.add("",new ActionMessage("errors.fechaPosteriorIgualActual","final cta.","del sistema: "+fechaSistema));
			
			if(fechaInicialValida&&!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial, fechaFinal))
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","final cta.","inicial cta."));
			else
				fechaFinalValida = true;
		}
		
		if(fechaInicialValida&&fechaFinalValida)
			if(UtilidadFecha.numeroDiasEntreFechas(fechaInicial, fechaFinal)>60)
				errores.add("",new ActionMessage("errors.rangoMayorMeses","","DOS"));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("periodo");
		}
		//*****************************************************************************************************************
		//Se consulta el listado de cuentas
		cargosForm.setListadoCuentas(RegistroRipsCargosDirectos.listadoCuentas(
			con, 
			ConstantesBD.codigoNuncaValido,
			Integer.parseInt(cargosForm.getParametros("centroAtencion").toString()),
			cargosForm.getParametros("fechaInicial").toString(),
			cargosForm.getParametros("fechaFinal").toString(),
			Utilidades.convertirAEntero(cargosForm.getParametros("estadoCuenta").toString()),
			Utilidades.convertirAEntero(cargosForm.getParametros("viaIngreso").toString()),
			Utilidades.convertirAEntero(cargosForm.getParametros("convenio").toString())));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoCuentas");
	}

	/**
	 * Método implementado para iniciar el flujo de la opcion Por Periodo
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezarPeriodo(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		cargosForm.setPeriodo(true);
		//Se postula el centro de atencion de la sesión
		cargosForm.setParametros(new HashMap<String, Object>());
		cargosForm.setParametros("centroAtencion", usuario.getCodigoCentroAtencion());
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		cargosForm.setParametros("fechaInicial", fechaSistema);
		cargosForm.setParametros("fechaFinal", fechaSistema);
		
		cargosForm.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		cargosForm.setEstadosCuenta(UtilidadesManejoPaciente.obtenerEstadosCuenta(con));
		cargosForm.setViasIngreso(Utilidades.obtenerViasIngreso(con,""));
		cargosForm.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("periodo");
	}

	/**
	 * Método para rexcargar la página del detalle de cirugías para ingresar un nuevo hijo
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVerificarHijos(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping) 
	{
		int nroHijos = Utilidades.convertirAEntero(cargosForm.getInfoRecienNacidos("nroHijos")+"");
		cargosForm.setInfoRecienNacidos("nroHijos", nroHijos);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleCirugia");
	}

	/**
	 * Método implementado para guardar la información  RIPS dependiendo del tipo de servicio
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) 
	{
		String forward = "";
		
		//******************VALIDACIONES DATOS REQUERIDOS******************************************************
		ActionErrors errores = new ActionErrors();
		
		if(cargosForm.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			forward = "detalleConsulta";
			errores = this.validacionesServicioConsulta(errores,cargosForm);
		}
		else if(cargosForm.getTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
		{
			forward = "detalleProcedimiento";
			errores = this.validacionesServicioProcedimiento(errores,cargosForm);
		}
		//De lo contrario sería Cirugias, Partos y Cesáreas o DYT
		else
		{
			forward = "detalleCirugia";
			errores = this.validacionesServicioCirugias(con,errores,cargosForm);
		}
		
		
		//******************************************************************************************************
		
		UtilidadBD.iniciarTransaccion(con);
		if(errores.isEmpty())
		{
			
			RegistroRipsCargosDirectos mundoRips = new RegistroRipsCargosDirectos();
			mundoRips.setTipoServicio(cargosForm.getTipoServicio());
			mundoRips.setNumeroSolicitud(cargosForm.getNumeroSolicitud());
			mundoRips.setTipoSolicitud(cargosForm.getTipoSolicitud());
			mundoRips.setDatos(cargosForm.getDatos());
			mundoRips.setInfoRecienNacidos(cargosForm.getInfoRecienNacidos());
			
			mundoRips.guardar(con, usuario);
			
			errores = mundoRips.getErrores();
		}
		
		
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
			cargosForm.setEstado("detalleSolicitud");
			UtilidadBD.closeConnection(con);
			return mapping.findForward(forward);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
			return accionDetalleSolicitud(con, cargosForm, mapping, usuario, paciente, request);
		}
			
		
		
	}

	/**
	 * Método implementado para realizar las validaciones del servicio de cirugias
	 * @param con 
	 * @param errores
	 * @param cargosForm
	 * @return
	 */
	private ActionErrors validacionesServicioCirugias(Connection con, ActionErrors errores, RegistroRipsCargosDirectosForm cargosForm) 
	{
		//Se itera cada servicio
		for(int i=0;i<Integer.parseInt(cargosForm.getDatos("numRegistros").toString());i++)
		{
			if(cargosForm.getDatos("finalidad_"+i).toString().equals(""))
				errores.add("", new ActionMessage("errors.required","La finalidad del servicio N°"+(i+1)));
			
			if(UtilidadTexto.isEmpty(cargosForm.getDatos("diagPrincipal_"+i)+""))
				errores.add("", new ActionMessage("errors.required","La diagnóstico principal del servicio N°"+(i+1)));
		}
		
		//Se valida la información de recién nacidos , (si la hay)
		if(UtilidadTexto.getBoolean(cargosForm.getInfoRecienNacidos("abrirSeccion").toString()))
		{
			//Se valida la edad gestacional
			if(Utilidades.convertirAEntero(cargosForm.getInfoRecienNacidos("edadGestacional").toString())<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","La edad gestacional","0"));
			
			//Control prenatal
			if(UtilidadTexto.isEmpty(cargosForm.getInfoRecienNacidos("controlPrenatal")+""))
				errores.add("", new ActionMessage("errors.required","El control prenatal"));
			
			//Se valida el número de hijos
			int nroHijos = Utilidades.convertirAEntero(cargosForm.getInfoRecienNacidos("nroHijos").toString());
			if(nroHijos<=0)
				errores.add("", new ActionMessage("errors.integerMayorQue","El número de hijos","0"));
			
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			String horaSistema = UtilidadFecha.getHoraActual(con);
			
			for(int i=0;i<nroHijos;i++)
			{
				boolean fechaValida = false, horaValida = false;
				
				//Validacion de la fecha de nacimiento
				String fecha = cargosForm.getInfoRecienNacidos("fechaNacimiento_"+i)+"";
				if(UtilidadTexto.isEmpty(fecha))
					errores.add("",new ActionMessage("errors.required","La fecha de nacimiento del hijo N°"+(i+1)));
				else if(!UtilidadFecha.validarFecha(fecha))
					errores.add("",new ActionMessage("errors.formatoFechaInvalido","de nacimiento del hijo N°"+(i+1)));
				else
					fechaValida = true;
				
				//Validacion de la hora de nacimiento
				String hora = cargosForm.getInfoRecienNacidos("horaNacimiento_"+i)+"";
				if(UtilidadTexto.isEmpty(hora))
					errores.add("",new ActionMessage("errors.required","La hora de nacimiento del hijo N°"+(i+1)));
				else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
					errores.add("",new ActionMessage("errors.formatoHoraInvalido","de nacimiento del hijo N°"+(i+1)));
				else
					horaValida = true;
				
				if(fechaValida&&horaValida)
				{
					if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, fecha, hora).isTrue())
						errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de nacimiento del hijo N°"+(i+1),"del sistema: "+fechaSistema+" - "+horaSistema));
					
					if(!UtilidadFecha.compararFechas(fecha, hora, cargosForm.getDatos("fechaInicioAtencion").toString(), cargosForm.getDatos("horaInicioAtencion").toString()).isTrue())
						errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de nacimiento del hijo N°"+(i+1),"inicio atención: "+cargosForm.getDatos("fechaInicioAtencion")+" - "+cargosForm.getDatos("horaInicioAtencion")));
				}
				
				//Validación del sexo
				if(UtilidadTexto.isEmpty(cargosForm.getInfoRecienNacidos("sexo_"+i)+""))
					errores.add("",new ActionMessage("errors.required","el sexo del hijo N°"+(i+1)));
				else
					cargosForm.setInfoRecienNacidos("nombreSexo_"+i, Utilidades.getDescripcionSexo(con, Integer.parseInt(cargosForm.getInfoRecienNacidos("sexo_"+i).toString())));
				
				//Validación del peso
				if(Utilidades.convertirAEntero(cargosForm.getInfoRecienNacidos("peso_"+i)+"",true)<=0)
					errores.add("",new ActionMessage("errors.required","el peso del hijo N°"+(i+1)));
				
				//Validación del diagnóstico recién nacido
				if(UtilidadTexto.isEmpty(cargosForm.getInfoRecienNacidos("diagnosticoRN_"+i)+""))
					errores.add("",new ActionMessage("errors.required","el diagnóstico de recién nacido del hijo N°"+(i+1)));
				
				//Validación de la fecha/hora muerte
				if(!UtilidadTexto.isEmpty(cargosForm.getInfoRecienNacidos("diagnosticoMuerte_"+i)+""))
				{
					fechaValida = false; horaValida = false;
					
					//Validacion de la fecha de muerte
					fecha = cargosForm.getInfoRecienNacidos("fechaMuerte_"+i)+"";
					if(UtilidadTexto.isEmpty(fecha))
						errores.add("",new ActionMessage("errors.required","La fecha de muerte del hijo N°"+(i+1)));
					else if(!UtilidadFecha.validarFecha(fecha))
						errores.add("",new ActionMessage("errors.formatoFechaInvalido","de muerte del hijo N°"+(i+1)));
					else
						fechaValida = true;
					
					//Validacion de la hora de muerte
					hora = cargosForm.getInfoRecienNacidos("horaMuerte_"+i)+"";
					if(UtilidadTexto.isEmpty(hora))
						errores.add("",new ActionMessage("errors.required","La hora de muerte del hijo N°"+(i+1)));
					else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
						errores.add("",new ActionMessage("errors.formatoHoraInvalido","de muerte del hijo N°"+(i+1)));
					else
						horaValida = true;
					
					if(fechaValida&&horaValida)
					{
						if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, fecha, hora).isTrue())
							errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de muerte del hijo N°"+(i+1),"del sistema: "+fechaSistema+" - "+horaSistema));
						
						if(!UtilidadFecha.compararFechas(fecha, hora, cargosForm.getDatos("fechaInicioAtencion").toString(), cargosForm.getDatos("horaInicioAtencion").toString()).isTrue())
							errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de muerte del hijo N°"+(i+1),"inicio atención: "+cargosForm.getDatos("fechaInicioAtencion")+" - "+cargosForm.getDatos("horaInicioAtencion")));
					}
					
				}
			}
		}
		
		return errores;
	}

	/**
	 * Método implementado para las validaciones del servicio de procedimientos
	 * @param errores
	 * @param cargosForm
	 * @return
	 */
	private ActionErrors validacionesServicioProcedimiento(ActionErrors errores, RegistroRipsCargosDirectosForm cargosForm) 
	{
		if(UtilidadTexto.isEmpty(cargosForm.getDatos("finalidad")+""))
			errores.add("", new ActionMessage("errors.required","La finalidad del procedimiento"));
		
		return errores;
	}

	/**
	 * Método implementado para realizar las validaciones del servicio de consulta
	 * @param errores
	 * @param cargosForm
	 * @return
	 */
	private ActionErrors validacionesServicioConsulta(ActionErrors errores, RegistroRipsCargosDirectosForm cargosForm) 
	{
		if(UtilidadTexto.isEmpty(cargosForm.getDatos("causaExterna")+""))
			errores.add("", new ActionMessage("errors.required","La causa externa"));
		
		if(UtilidadTexto.isEmpty(cargosForm.getDatos("finalidad")+""))
			errores.add("", new ActionMessage("errors.required","La finalidad consulta"));
		
		if(UtilidadTexto.isEmpty(cargosForm.getDatos("diagPrincipal")+""))
			errores.add("", new ActionMessage("errors.required","El Dx. Principal"));
		
		if(UtilidadTexto.isEmpty(cargosForm.getDatos("tipoDiagnostico")+""))
			errores.add("", new ActionMessage("errors.required","El tipo Dx. Principal"));
		
		return errores;
	}

	/**
	 * Método implementado para entrar al detalle de la solicitud dependiendo del tipo de servicio
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param usuario 
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalleSolicitud(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		//Se toma el tipo de servicio seleccionado
		cargosForm.setTipoServicio(cargosForm.getListadoSolicitudes("codigoTipoServicio_"+cargosForm.getIndexSolicitud()).toString());
		//Se toma el número de la solicitud
		cargosForm.setNumeroSolicitud(cargosForm.getListadoSolicitudes("numeroSolicitud_"+cargosForm.getIndexSolicitud()).toString());
		//Se toma el tipo de solicitud
		cargosForm.setTipoSolicitud(Integer.parseInt(cargosForm.getListadoSolicitudes("tipoSolicitud_"+cargosForm.getIndexSolicitud()).toString()));
		
		
		cargosForm.resetDetalle();
		cargosForm.setDatos("orden", cargosForm.getListadoSolicitudes("orden_"+cargosForm.getIndexSolicitud()));
		cargosForm.setDatos("descripcionServicio", cargosForm.getListadoSolicitudes("nombreServicio_"+cargosForm.getIndexSolicitud()));
		cargosForm.setDatos("fechaSolicitud", cargosForm.getListadoSolicitudes("fechaSolicitud_"+cargosForm.getIndexSolicitud()));
		cargosForm.setDatos("horaSolicitud", cargosForm.getListadoSolicitudes("horaSolicitud_"+cargosForm.getIndexSolicitud()));
		cargosForm.setDatos("codigoServicio", cargosForm.getListadoSolicitudes("codigoServicio_"+cargosForm.getIndexSolicitud()));
		cargosForm.setDatos("numDiagnosticos", "0");
		
		logger.info("TIPO SERVICIO SELECCIONADO=>"+cargosForm.getTipoServicio()+".");
		//1) FLUJO PARA SERVICIOS DE TIPO CONSULTA
		if(cargosForm.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			cargosForm.setCausasExternas(UtilidadesHistoriaClinica.obtenerCausasExternas(con,false));
			cargosForm.setFinalidades(UtilidadesHistoriaClinica.obtenerFinalidadesConsulta(con));
			cargarDatosExistentesCargoServicios(con,cargosForm);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleConsulta");
		}
		//2) FLUJO PARA SERVICIOS DE TIPO PROCEDIMIENTOS
		else if(cargosForm.getTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
		{
			cargosForm.setFinalidades(Utilidades.obtenerFinalidadesServicio(con, Integer.parseInt(cargosForm.getDatos("codigoServicio").toString()), usuario.getCodigoInstitucionInt()));
			cargarDatosExistentesCargoServicios(con,cargosForm);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleProcedimiento");
		}
		//3) FLUJO PARA SERVICIOS QUIRURGICOS, PARTOS Y CESÁREAS , NO CRUENTOS
		else
		{
			cargarDatosExistentesCargoCirugias(con, cargosForm, usuario);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleCirugia");
		}
		
	}

	/**
	 * Método implementado para cargar la información
	 * @param con
	 * @param cargosForm
	 * @param usuario 
	 */
	private void cargarDatosExistentesCargoCirugias(Connection con, RegistroRipsCargosDirectosForm cargosForm, UsuarioBasico usuario) 
	{
		
		
		//******************************SOLICITUD DE TPO CARGOS DIRECTOS DE SERVICIOS********************************************************
		if(cargosForm.getTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios)
		{
			int numRegistros = 0;
			CargosDirectos mundoCargosDirectos = new CargosDirectos();
			DtoCargoDirectoHC dtoCargoExistente = mundoCargosDirectos.consultarInformacionHC(con, cargosForm.getNumeroSolicitud());
			cargosForm.setDatos("descripcionServicio_"+numRegistros, cargosForm.getDatos("descripcionServicio"));
			cargosForm.setDatos("codigoServicio_"+numRegistros, cargosForm.getDatos("codigoServicio"));
			cargosForm.setDatos("comboFinalidades_"+numRegistros, Utilidades.obtenerFinalidadesServicio(con, Integer.parseInt(cargosForm.getDatos("codigoServicio").toString()), usuario.getCodigoInstitucionInt()));
			cargosForm.setDatos("fechaInicioAtencion", cargosForm.getDatos("fechaSolicitud"));
			cargosForm.setDatos("horaInicioAtencion", cargosForm.getDatos("horaSolicitud"));
			
			if(dtoCargoExistente.isExisteBaseDatos())
			{
				cargosForm.setDatos("codigo_"+numRegistros,dtoCargoExistente.getCodigo());
				cargosForm.setDatos("finalidad_"+numRegistros, dtoCargoExistente.getCodigoFinalidadProcedimiento());
				
				//Se toma el diagnostico principal
				for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
					if(diagTemp.isPrincipal())
						cargosForm.setDatos("diagPrincipal_"+numRegistros, diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());
				
				//Se toma el diagnostico relacionado
				int cont = 0;
				String diagSeleccionados = "";
				for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
					if(!diagTemp.isPrincipal()&&!diagTemp.isComplicacion())
					{
						diagSeleccionados += (diagSeleccionados.length()>0?",":"") + "'"+diagTemp.getAcronimoDiagnostico()+"'";
						cargosForm.setDatos("diagRelacionado_"+numRegistros+"_"+cont,diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());
						cargosForm.setDatos("checkDiagRel_"+numRegistros+"_"+cont,"true");
						cont++;
					}
				cargosForm.setDatos("numDiagnosticos_"+numRegistros, cont+"");
				cargosForm.setDatos("diagSeleccionados_"+numRegistros, diagSeleccionados);
				//Se toma el diagnostico complicacion
				for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
					if(diagTemp.isComplicacion())
						cargosForm.setDatos("diagComplicacion_"+numRegistros, diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());	
			}
			numRegistros++;
			cargosForm.setDatos("numRegistros", numRegistros+"");
			
			//por defecto no se abre la seccion de información recién nacidos
			cargosForm.setInfoRecienNacidos("abrirSeccion", ConstantesBD.acronimoNo);
		}
		//*************************************SOLICITUD DE TIPO CIRUGIA************************************************************************
		else
		{
			RegistroRipsCargosDirectos mundoRips = new RegistroRipsCargosDirectos();
			mundoRips.setNumeroSolicitud(cargosForm.getNumeroSolicitud());
			
			mundoRips.consultarInformacionCirugia(con, usuario);
			
			String orden = cargosForm.getDatos("orden").toString();
			cargosForm.setDatos(mundoRips.getDatos());
			cargosForm.setDatos("orden", orden);
			
			cargosForm.setInfoRecienNacidos(mundoRips.getInfoRecienNacidos());
			
			//Se cargan los sexos
			cargosForm.setSexos(Utilidades.obtenerSexos(con));
			
		}
		//***************************************************************************************************************
		
	}

	/**
	 * Método para cargar los datos existentes de la información rips del cargo directo del servicio
	 * @param con
	 * @param cargosForm
	 */
	private void cargarDatosExistentesCargoServicios(Connection con, RegistroRipsCargosDirectosForm cargosForm) 
	{
		CargosDirectos mundoCargosDirectos = new CargosDirectos();
		DtoCargoDirectoHC dtoCargoExistente = mundoCargosDirectos.consultarInformacionHC(con, cargosForm.getNumeroSolicitud());
		
		if(dtoCargoExistente.isExisteBaseDatos())
		{
			cargosForm.setDatos("codigo", dtoCargoExistente.getCodigo());
			
			if(cargosForm.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
			{
				cargosForm.setDatos("causaExterna", dtoCargoExistente.getCodigoCausaExterna());
				cargosForm.setDatos("finalidad", dtoCargoExistente.getCodigoFinalidadConsulta());
			}
			else
				cargosForm.setDatos("finalidad", dtoCargoExistente.getCodigoFinalidadProcedimiento());
			
			//Se toma el diagnostico principal
			for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
				if(diagTemp.isPrincipal())
				{
					cargosForm.setDatos("diagPrincipal", diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());
					cargosForm.setDatos("tipoDiagnostico", diagTemp.getCodigoTipoDiagnostico());
				}
			
			//Se toma el diagnostico relacionado
			int cont = 0;
			String diagSeleccionados = "";
			for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
				if(!diagTemp.isPrincipal()&&!diagTemp.isComplicacion())
				{
					diagSeleccionados += (diagSeleccionados.length()>0?",":"") + "'"+diagTemp.getAcronimoDiagnostico()+"'";
					cargosForm.setDatos("diagRelacionado_"+cont,diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());
					cargosForm.setDatos("checkDiagRel_"+cont,"true");
					cont++;
				}
			cargosForm.setDatos("numDiagnosticos", cont+"");
			cargosForm.setDatos("diagSeleccionados", diagSeleccionados);
			//Se toma el diagnostico complicacion
			for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoExistente.getDiagnosticos())
				if(diagTemp.isComplicacion())
					cargosForm.setDatos("diagComplicacion", diagTemp.getAcronimoDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getCieDiagnostico()+ConstantesBD.separadorSplit+diagTemp.getNombreDiagnostico());
		}
		
	}

	/**
	 * Método para consultar las solicitudes de cargos directos de una cuenta
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalleCuenta(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) 
	{
		//Se toma el código del paciente
		paciente.setCodigoPersona(Integer.parseInt(cargosForm.getListadoCuentas("codigoPaciente_"+cargosForm.getIndexCuenta()).toString()));
		
		cargosForm.setIdCuenta(cargosForm.getListadoCuentas("idCuenta_"+cargosForm.getIndexCuenta()).toString());
		cargosForm.setUltimoIndice(""); //se reinicia el ultimo indice de la ordenacion
		
		//**************************CARGAR EL PACIENTE*******************************************************************************
		ObservableBD observable = (ObservableBD)request.getSession().getServletContext().getAttribute("observable");
		try
		{
			logger.info("**********CODIGO DEL INGRESO:"+cargosForm.getListadoCuentas("idIngreso_"+cargosForm.getIndexCuenta())+"*******************************");
			paciente.cargar(con, paciente.getCodigoPersona());
			if(UtilidadValidacion.esCuentaValida(con, Integer.parseInt(cargosForm.getIdCuenta()))>0)
				paciente.cargarPacienteXingreso(con, cargosForm.getListadoCuentas("idIngreso_"+cargosForm.getIndexCuenta()).toString(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		}
		catch(Exception e)
		{
			logger.error("Error cargando el paciente "+e);
		}
		paciente.setObservable(observable);
		observable.addObserver(paciente);
		//**********************************************************************************************************************
		
		cargosForm.setListadoSolicitudes(RegistroRipsCargosDirectos.listadoSolicitudes(con, cargosForm.getIdCuenta()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudes");
	}

	/**
	 * Método implementado para realizar la ordenación de listados
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarListado(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping) 
	{
		String[] indices = null;
		int numRegistros = 0;
		
		if(cargosForm.getEstado().equals("ordenarListadoCuenta"))
		{
			indices = (String[])RegistroRipsCargosDirectos.camposListadoCuentas;
			numRegistros = cargosForm.getNumListadoCuentas();
			
			//se pasan la fecha de orden a Formato BD
			for(int i=0;i<numRegistros;i++)
			{
				cargosForm.setListadoCuentas("fechaIngreso_"+i,UtilidadFecha.conversionFormatoFechaABD(cargosForm.getListadoCuentas("fechaIngreso_"+i).toString()));
				cargosForm.setListadoCuentas("fechaApertura_"+i,UtilidadFecha.conversionFormatoFechaABD(cargosForm.getListadoCuentas("fechaApertura_"+i).toString()));
			}
			
			cargosForm.setListadoCuentas(Listado.ordenarMapa(indices,
					cargosForm.getIndice(),
					cargosForm.getUltimoIndice(),
					cargosForm.getListadoCuentas(),
					numRegistros));
			
			cargosForm.setListadoCuentas("numRegistros",numRegistros+"");
			
			///se pasan las fechas a formato aplicacion
			for(int i=0;i<numRegistros;i++)
			{
				cargosForm.setListadoCuentas("fechaIngreso_"+i,UtilidadFecha.conversionFormatoFechaAAp(cargosForm.getListadoCuentas("fechaIngreso_"+i).toString()));
				cargosForm.setListadoCuentas("fechaApertura_"+i,UtilidadFecha.conversionFormatoFechaAAp(cargosForm.getListadoCuentas("fechaApertura_"+i).toString()));
			}
			
			cargosForm.setUltimoIndice(cargosForm.getIndice());
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoCuentas");
		}
		else
		{
			indices = (String[])RegistroRipsCargosDirectos.camposListadoSolicitudes;
			numRegistros = cargosForm.getNumListadoSolicitudes();
			
			cargosForm.setListadoSolicitudes(Listado.ordenarMapa(indices,
					cargosForm.getIndice(),
					cargosForm.getUltimoIndice(),
					cargosForm.getListadoSolicitudes(),
					numRegistros));
			
			cargosForm.setListadoSolicitudes("numRegistros",numRegistros+"");
			
			cargosForm.setUltimoIndice(cargosForm.getIndice());
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoSolicitudes");
		}
		
		
	}

	/**
	 * 
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param paciente 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezarPaciente(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		cargosForm.setPeriodo(false);
		ActionErrors errores = new ActionErrors();
		//**************VALIDACIONES INICIAL**************************************************************
		//Validacion del paciente cargado en sesión
		if(paciente.getCodigoPersona()<1)
			errores.add("",new ActionMessage("errors.paciente.noCargado"));
		//************************************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("opciones");
		}
		
		//Se consulta el listado de cuentas
		cargosForm.setListadoCuentas(RegistroRipsCargosDirectos.listadoCuentas(con, paciente.getCodigoPersona(),ConstantesBD.codigoNuncaValido,"","",ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoCuentas");
	}

	/**
	 * Método implementado para iniciar el flujo de registro rips cargos directos
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, RegistroRipsCargosDirectosForm cargosForm, ActionMapping mapping) 
	{
		cargosForm.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("opciones");
	}
}
