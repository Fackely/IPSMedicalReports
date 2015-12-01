/*
 * Mayo 15, 2007
 */
package com.princetonsa.action.historiaClinica;

import java.io.IOException;
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
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ReferenciaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Referencia;
import com.princetonsa.mundo.manejoPaciente.TiposAmbulancia;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Referencia
 */
public class ReferenciaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ReferenciaAction.class);
	
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
			if(form instanceof ReferenciaForm)
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
				ReferenciaForm referenciaForm =(ReferenciaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado=referenciaForm.getEstado(); 
				logger.warn("estado ReferenciaAction-->"+estado);

				if(estado == null)
				{
					referenciaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de REFERENCIA (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				//**********Flujo inicial si se entra directamente por el menu de referencia y llamado a creacion del paciente*********************
				else if(estado.equals("ingresarPaciente")||estado.equals("recargarPaciente"))
				{
					return accionIngresarPaciente(con,referenciaForm,mapping,usuario);
				}
				else if (estado.equals("validarPaciente")||estado.equals("empezarReferencia"))
					//El estadio empezarReferencia solo se usa cuando referencia es llamado desde otras funcionalidades
				{
					return accionValidarPaciente(con,referenciaForm,mapping,request,paciente,usuario);
				}
				//******************************************************************************************************************************
				//*********Estados del flujo propio de referencia******************************************************************************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, referenciaForm, mapping, paciente,usuario,request);
				}
				else if (estado.equals("busquedaInstitucionSirc")) //estado para realizar la busqueda de instituciones SIRC
				{
					return accionBusquedaInstitucionSirc(con,referenciaForm,mapping,usuario);
				}
				else if (estado.equals("busquedaDatosConvenio")) //estado para buscar los datos del convenio (uso de AJAX)
				{
					return accionBusquedaDatosConvenio(con,referenciaForm,response,usuario);
				}
				else if (estado.equals("busquedaTiposAmbulancia"))
				{
					return accionBusquedaTiposAmbulancia(con,referenciaForm,mapping,usuario);
				}
				else if (estado.equals("redireccion")) //para paginar listados
				{
					return accionRedireccion(con,referenciaForm,response,mapping,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,referenciaForm,request,usuario,mapping,paciente);
				}
				//*****************************************************************************************************************************
				//**********Estados para manejo del listado de servicios sirc******************************************************************
				else if (estado.equals("busquedaServiciosSirc"))
				{
					return accionBusquedaServiciosSirc(con,referenciaForm,mapping,usuario);
				}
				else if (estado.equals("ordenarServiciosSirc"))
				{
					return accionOrdenarServiciosSirc(con,referenciaForm,mapping);
				}
				else if (estado.equals("eliminarServiciosSirc"))
				{
					return accionEliminarServiciosSirc(con,referenciaForm,mapping);
				}
				//****************************************************************************************************************************
				//**********Estados para manejo del listado de resultados examanes diagnosticos**********************************************
				else if (estado.equals("busquedaResultadosExamenes"))
				{
					return accionBusquedaResultadosExamenes(con,referenciaForm,mapping,usuario);
				}
				else if (estado.equals("ordenarResultadosExamenes"))
				{
					return accionOrdenarResultadosExamenes(con,referenciaForm,mapping);
				}
				else if (estado.equals("eliminarResultadosExamenes"))
				{
					return accionEliminarResultadosExamenes(con,referenciaForm,mapping);
				}
				//****************************************************************************************************************************
				//**********Estado especial que es invocado desde HISTORIA DE ATENCIONES******************************************************
				else if (estado.equals("historiaAtenciones"))
				{
					return accionHistoriaAtenciones(con,referenciaForm,mapping,usuario,paciente);
				}
				//*****************************************************************************************************************************
				//*****************************************************************************************************************************
				else if(estado.equals("consultaCiudadSolicita"))
				{
					referenciaForm.setEstado("empezar");
					referenciaForm.setReferencia("codigoCiudad_", " "+ConstantesBD.separadorSplit+" "+ConstantesBD.separadorSplit+" ");
					return mapping.findForward("principal");
				}
				else
				{
					referenciaForm.reset();
					logger.warn("Estado no valido dentro del flujo de ReferenciaAction (null) ");
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
	 * Método que consulta la informacion de una referencia llamado desde HISTORIA DE ATENCIONES
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionHistoriaAtenciones(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		//se mantienen los datos principales
		String tipoReferencia = referenciaForm.getTipoReferencia();
		String estado = referenciaForm.getEstado();
		
		//se blanquea formulario
		referenciaForm.reset();
		referenciaForm.setTipoReferencia(tipoReferencia);
		referenciaForm.setEstado(estado);
		
		Referencia referencia = new Referencia();
		//Consulta de la referencia
		referenciaForm.setReferencia(referencia.cargar(con, 0, 0, "",referenciaForm.getNumeroReferencia()));
		
		//Se prepara la informacion
		prepararMapas(con,referenciaForm,usuario,false,0,paciente);
		
		//Todos es de lectura
		referenciaForm.setReferencia("editable", ConstantesBD.acronimoNo);
		referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoNo);
		referenciaForm.setReferencia("puedoAnular", ConstantesBD.acronimoNo);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método imlementado para guardar la informacion de la referencia
	 * @param con
	 * @param referenciaForm
	 * @param request
	 * @param usuario
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ReferenciaForm referenciaForm, HttpServletRequest request, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente) 
	{
		this.prepararInformacion(referenciaForm,usuario);
		
		ActionErrors errores = new ActionErrors();
		//Se realizan validaciones de consecutivos disponibles (SOLO APLICA PARA INTERNAS)
		if(!UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
			errores = this.validacionConsecutivoDisponible(con, referenciaForm, usuario, errores);
		
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReferencia, usuario.getCodigoInstitucionInt());
		String anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoReferencia, usuario.getCodigoInstitucionInt(),consecutivo);
		
		referenciaForm.setConsecutivoAnio(consecutivo);
		referenciaForm.setConsecutivoInt(Utilidades.convertirAEntero(anioConsecutivo));

		
		//*******************PROCESO ADICIONAL PARA EL MANEJO DEL CONSECUTIVO *****************************************************
		//Si es interna se vuelven y se asignan los consecutivos
		if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna)&&
			!UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
		{
			referenciaForm.setReferencia("anioConsecutivo", referenciaForm.getConsecutivoAnio());
			referenciaForm.setReferencia("consecutivoPuntoAtencion", referenciaForm.getConsecutivoInt()+"");
		}
		//*********************************************************************************************************************
	
		
		
		
		//***************PROCESO ADICIONAL PARA LA RESERVA DE CAMAS*******************************************************
		//Se verifica si es referencia externa
		if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna)&&
			referenciaForm.getViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Si se requiere reserva de cama entonces se verifica si hay reserva
			if(UtilidadTexto.getBoolean(referenciaForm.getReferencia("requiereReservarCama").toString()))
			{
				
				//Se verifica si ya existia una reserva de cama
				if(referenciaForm.getReferencia("reservaCama").toString().equals(""))
				{
					HashMap reservaCama = UtilidadesManejoPaciente.consultarReservaCama(con, paciente.getCodigoPersona()+"", usuario.getCodigoInstitucionInt());
					//Se verifica que el paciente tenga reserva
					if(Integer.parseInt(reservaCama.get("numRegistros").toString())>0)
						referenciaForm.setReferencia("reservaCama",reservaCama.get("codigo"));
					else
						//Se añade error
						errores.add("Paciente sin reserva de cama",new ActionMessage("error.historiaClinica.referencia.pacienteSinReservaCama"));
						
				}
				
			}
		}
		
		//Si no hay errores se prosigue a guardar
		if(errores.isEmpty())
		{
			boolean exito1 = false, exito2 = false;
			///Se instancia objeto de Referencia
			Referencia referencia = new Referencia();
		
			
			UtilidadBD.iniciarTransaccion(con);
			

			//Se guarda la informacion de la referencia
			exito1 = referencia.guardar(con, referenciaForm.getReferencia());
			
			//Se verifica si todo va bien hasta el momento
			if(exito1)
			{
//				Si la referencia es interna se debe actualizar el consecutivo
				if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna)&&
					!UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
				{
					exito2 = UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReferencia,usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				}
				else
					exito2 = true;
						
			}
			
			if(exito1&&exito2)
			{
				UtilidadBD.finalizarTransaccion(con);
				return accionEmpezar(con, referenciaForm, mapping, paciente, usuario, request);
			}
			else
			{
				exito2 = UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReferencia,usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.abortarTransaccion(con);
				errores.add("Error al guardar la informacion",new ActionMessage("errors.noSeGraboInformacion","DE LA REFERENCIA"));
				saveErrors(request, errores);
				referenciaForm.setEstado("empezar");
			}
		}
		else
		{
			referenciaForm.setEstado("empezar");
			saveErrors(request, errores);
		}
		
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para preparar el mapa de referencia 
	 * @param referenciaForm
	 * @param usuario 
	 */
	private void prepararInformacion(ReferenciaForm referenciaForm, UsuarioBasico usuario) 
	{
		//***********+Asignacion del centro de costo******************************************
		if(referenciaForm.getCodigoArea()>0)
			referenciaForm.setReferencia("codigoCentroCosto",referenciaForm.getCodigoArea()+"");
		else
			referenciaForm.setReferencia("codigoCentroCosto","");
		
		//***********Asignacion del estado de la referencia y el id de inrgeso dependiendo del tipo de referencia*****************
		if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			referenciaForm.setReferencia("estado", ConstantesIntegridadDominio.acronimoEstadoSolicitado);
			referenciaForm.setReferencia("idIngreso", referenciaForm.getIdIngreso()+"");
		}
		else if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			//Si tiene area quiere decir que ya tiene cuenta abierta
			if(referenciaForm.getCodigoArea()>0)
			{
				referenciaForm.setReferencia("estado", ConstantesIntegridadDominio.acronimoEstadoAdmitido);
				referenciaForm.setReferencia("idIngreso", referenciaForm.getIdIngreso()+"");
			}
			else
			{
				referenciaForm.setReferencia("estado", ConstantesIntegridadDominio.acronimoEstadoEnTramite);
				referenciaForm.setReferencia("idIngreso", "");
			}
				
		}
		
		//***********PREPARACION MAPA DIAGNOSTICOS***************************
		int numDiag = 0;
		HashMap mapDiag = new HashMap();
		String[] diag = new String[0];
		//Se toma el diagnostico principal
		mapDiag.put("codigo_"+numDiag,referenciaForm.getMapaDiagnosticos("codigoPrincipal"));
		mapDiag.put("numeroReferencia_"+numDiag,referenciaForm.getMapaDiagnosticos("numeroReferenciaPrincipal"));
		mapDiag.put("existe_"+numDiag,referenciaForm.getMapaDiagnosticos("existePrincipal"));
		mapDiag.put("principal_"+numDiag,ConstantesBD.acronimoSi);
		mapDiag.put("chequeado_"+numDiag,ConstantesBD.acronimoSi);
		diag = referenciaForm.getMapaDiagnosticos("valorDiagnosticoPrincipal").toString().split(ConstantesBD.separadorSplit);
		mapDiag.put("acronimoDiagnostico_"+numDiag,diag[0]);
		mapDiag.put("tipoCie_"+numDiag,diag[1]);
		mapDiag.put("nombreDiagnostico_"+numDiag,diag[2]);
		numDiag++;
		
		//Se toman los diagnosticos relacionados (si los hay)
		for(int i=0;i<Integer.parseInt(referenciaForm.getMapaDiagnosticos("numRegistros").toString());i++)
		{
			mapDiag.put("codigo_"+numDiag,referenciaForm.getMapaDiagnosticos("codigo_"+i));
			mapDiag.put("numeroReferencia_"+numDiag,referenciaForm.getMapaDiagnosticos("numeroReferencia_"+i));
			mapDiag.put("existe_"+numDiag,referenciaForm.getMapaDiagnosticos("existe_"+i)==null?ConstantesBD.acronimoNo:referenciaForm.getMapaDiagnosticos("existe_"+i));
			mapDiag.put("principal_"+numDiag,ConstantesBD.acronimoNo);
			mapDiag.put("chequeado_"+numDiag,referenciaForm.getMapaDiagnosticos("chequeado_"+i));
			diag = referenciaForm.getMapaDiagnosticos("valorDiagnostico_"+i).toString().split(ConstantesBD.separadorSplit);
			mapDiag.put("acronimoDiagnostico_"+numDiag,diag[0]);
			mapDiag.put("tipoCie_"+numDiag,diag[1]);
			mapDiag.put("nombreDiagnostico_"+numDiag,diag[2]);
			numDiag++;
		}
		
		
		
		mapDiag.put("numRegistros",numDiag+"");
		referenciaForm.setReferencia("mapaDiagnosticos", mapDiag);
		//*****************************************************************************
		
		//***********SE ASIGNA EL RESTO DE MAPAS A LA REFERENCIA**************************************
		referenciaForm.setReferencia("mapaServicios", referenciaForm.getMapaServicios());
		referenciaForm.setReferencia("mapaServiciosEliminados", referenciaForm.getMapaServiciosEliminados());
		referenciaForm.setReferencia("mapaSignosVitales", referenciaForm.getMapaSignosVitales());
		referenciaForm.setReferencia("mapaResultados", referenciaForm.getMapaResultados());
		referenciaForm.setReferencia("mapaResultadosEliminados", referenciaForm.getMapaResultadosEliminados());
		//********************************************************************************************
		
		//***********ASIGNACION INFORMACION DE REGISTRO******************************
		referenciaForm.setReferencia("fechaModifica", UtilidadFecha.getFechaActual());
		referenciaForm.setReferencia("horaModifica", UtilidadFecha.getHoraActual());
		referenciaForm.setReferencia("usuarioModifica", usuario.getLoginUsuario());
		//***************************************************************************
		
		//********SE ALISTA LA INFORMACION DE LA ANULACION (SI HAY)******************
		if(UtilidadTexto.getBoolean(referenciaForm.getReferencia("anulacion").toString()))
		{
			referenciaForm.setReferencia("fechaAnulacion", UtilidadFecha.getFechaActual());
			referenciaForm.setReferencia("horaAnulacion", UtilidadFecha.getHoraActual());
			referenciaForm.setReferencia("usuarioAnulacion", usuario.getLoginUsuario());
			referenciaForm.setReferencia("estado", ConstantesIntegridadDominio.acronimoEstadoAnulado);
		}
		//**************************************************************************
		
	}

	

	/**
	 * Método implementado para realizar la busqueda de los tipos de ambulancia
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaTiposAmbulancia(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia objeto de tipos de ambulancia
		TiposAmbulancia tipoAmbulancia = new TiposAmbulancia();
		
		referenciaForm.setTiposAmbulancia(tipoAmbulancia.consultarTiposAmbulancia(con, usuario.getCodigoInstitucionInt()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaTiposAmbulancia");
	}

	/**
	 * Método que elimina un resultado de examen diagnostico
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarResultadosExamenes(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping) 
	{
		//Se toma la posicion que se va a eliminar
		int pos = referenciaForm.getPosicion();
		int numRegistros = Integer.parseInt(referenciaForm.getMapaResultados("numRegistros").toString());
		
		if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			//Se borra el registro de procedimientosInsertados (Si EStá)
			String numeroSolicitud = referenciaForm.getMapaResultados("numeroSolicitud_"+pos).toString();
			
			String procedimientosInsertados = referenciaForm.getMapaResultados("procedimientosInsertados")==null?"":referenciaForm.getMapaResultados("procedimientosInsertados").toString();
			logger.info("PROCEDIMIENTOS INSERTADOS ANTES "+procedimientosInsertados);
			procedimientosInsertados = procedimientosInsertados.replaceAll(numeroSolicitud+",", "");
			procedimientosInsertados = procedimientosInsertados.replaceAll(","+numeroSolicitud, "");
			procedimientosInsertados = procedimientosInsertados.replaceAll(numeroSolicitud, "");
			logger.info("PROCEDIMIENTOS INSERTADOS DESPUES "+procedimientosInsertados);
			referenciaForm.setMapaResultados("procedimientosInsertados", procedimientosInsertados);
		}
		
		
		
		
		//Se verifica si ya existe
		if(UtilidadTexto.getBoolean(referenciaForm.getMapaResultados("existe_"+pos).toString()))
		{
			int posEliminados = Integer.parseInt(referenciaForm.getMapaResultadosEliminados("numRegistros").toString());
			referenciaForm.setMapaResultadosEliminados("codigo_"+posEliminados, referenciaForm.getMapaResultados("codigo_"+pos));
			posEliminados++;
			referenciaForm.setMapaResultadosEliminados("numRegistros", posEliminados+"");
		}
		
		//Se quita registro del mapa
		for(int i=pos;i<(numRegistros-1);i++)
		{
			referenciaForm.setMapaResultados("codigo_"+i, referenciaForm.getMapaResultados("codigo_"+(i+1)));
			referenciaForm.setMapaResultados("numeroReferencia_"+i, referenciaForm.getMapaResultados("numeroReferencia_"+(i+1)));
			referenciaForm.setMapaResultados("descripcion_"+i, referenciaForm.getMapaResultados("descripcion_"+(i+1)));
			referenciaForm.setMapaResultados("interpretacion_"+i, referenciaForm.getMapaResultados("interpretacion_"+(i+1)));
			referenciaForm.setMapaResultados("numeroSolicitud_"+i, referenciaForm.getMapaResultados("numeroSolicitud_"+(i+1)));
			referenciaForm.setMapaResultados("existe_"+i, referenciaForm.getMapaResultados("existe_"+(i+1)));
			
		}
		
		numRegistros--;
		
		referenciaForm.getMapaResultados().remove("numeroReferencia_"+numRegistros);
		referenciaForm.getMapaResultados().remove("codigo_"+numRegistros);
		referenciaForm.getMapaResultados().remove("descripcion_"+numRegistros);
		referenciaForm.getMapaResultados().remove("interpretacion_"+numRegistros);
		referenciaForm.getMapaResultados().remove("numeroSolicitud_"+numRegistros);
		referenciaForm.getMapaResultados().remove("existe_"+numRegistros);
		
		referenciaForm.setMapaResultados("numRegistros", numRegistros+"");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que realiza la ordenacion del listado de resultados examenes diagnosticos consultados
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarResultadosExamenes(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"numero_solicitud_",
				"fecha_solicitud_",
				"hora_solicitud_",
				"codigo_servicio_",
				"descripcion_",
				"interpretacion_",
				"seleccionado_"
			};

		int numRegistros = Integer.parseInt(referenciaForm.getProcedimientos("numRegistros").toString());
		
		//Se pasa la fecha de solicitud a fecha tipo BD
		for(int i=0;i<numRegistros;i++)
			referenciaForm.setProcedimientos("fecha_solicitud_"+i, UtilidadFecha.conversionFormatoFechaABD(referenciaForm.getProcedimientos("fecha_solicitud_"+i).toString()));
		
		referenciaForm.setProcedimientos(Listado.ordenarMapa(indices,
				referenciaForm.getIndice(),
				referenciaForm.getUltimoIndice(),
				referenciaForm.getProcedimientos(),
				numRegistros));
		
		//Se pasa la fecha de solicitud a fecha tipo Aplicacion
		for(int i=0;i<numRegistros;i++)
			referenciaForm.setProcedimientos("fecha_solicitud_"+i, UtilidadFecha.conversionFormatoFechaAAp(referenciaForm.getProcedimientos("fecha_solicitud_"+i).toString()));
		
		referenciaForm.setProcedimientos("numRegistros",numRegistros+"");
		
		referenciaForm.setUltimoIndice(referenciaForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaResultadosExamenes");
	}

	/**
	 * Método que consulta los resultados de examanes diagnosticos
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaResultadosExamenes(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se inician parámetros del pager
		referenciaForm.setIndice("");
		referenciaForm.setUltimoIndice("");
		referenciaForm.setOffset(0);
		
		logger.info("TIPO DE REFERENCIA===>  "+referenciaForm.getTipoReferencia());
		
		//Segun tipo de referencia se toman las acciones adecuadas
		if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			//Si es interna se debe consultar la informacion del ingreso
			referenciaForm.setProcedimientos(UtilidadesHistoriaClinica.obtenerInterpretacionProcedimientosIngreso(con, referenciaForm.getIdIngreso(), usuario.getCodigoInstitucionInt(),referenciaForm.getProcedimientosInsertados()));
		}
		
		//Se asigna el maxPageItems
		referenciaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaResultadosExamenes");
	}

	/**
	 * Método implementado para efectuar la redireccion de paginadores
	 * @param con
	 * @param referenciaForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ReferenciaForm referenciaForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(referenciaForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ReferenciaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ReferenciaAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para eliminar los servicios SIRC
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarServiciosSirc(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping) 
	{
		//Se toma la posicion que se va a eliminar
		int pos = referenciaForm.getPosicion();
		int numRegistros = Integer.parseInt(referenciaForm.getMapaServicios("numRegistros").toString());
		
		//Se borra el registro de serviciosInsertados (Si EStá)
		String servicio = referenciaForm.getMapaServicios("codigoServicioSirc_"+pos)+"-"+referenciaForm.getMapaServicios("codigoServicio_"+pos);
		String serviciosInsertados = referenciaForm.getMapaServicios("serviciosInsertados")==null?"":referenciaForm.getMapaServicios("serviciosInsertados").toString();
		serviciosInsertados = serviciosInsertados.replaceAll(servicio+",", "");
		serviciosInsertados = serviciosInsertados.replaceAll(","+servicio, "");
		serviciosInsertados = serviciosInsertados.replaceAll(servicio, "");
		referenciaForm.setMapaServicios("serviciosInsertados",serviciosInsertados);
		
		
		//Se verifica si ya existe
		if(UtilidadTexto.getBoolean(referenciaForm.getMapaServicios("existe_"+pos).toString()))
		{
			int posEliminados = Integer.parseInt(referenciaForm.getMapaServiciosEliminados("numRegistros").toString());
			referenciaForm.setMapaServiciosEliminados("numeroReferencia_"+posEliminados, referenciaForm.getMapaServicios("numeroReferencia_"+pos));
			referenciaForm.setMapaServiciosEliminados("institucion_"+posEliminados, referenciaForm.getMapaServicios("institucion_"+pos));
			referenciaForm.setMapaServiciosEliminados("codigoServicioSirc_"+posEliminados, referenciaForm.getMapaServicios("codigoServicioSirc_"+pos));
			referenciaForm.setMapaServiciosEliminados("codigoServicio_"+posEliminados, referenciaForm.getMapaServicios("codigoServicio_"+pos));
			posEliminados++;
			referenciaForm.setMapaServiciosEliminados("numRegistros", posEliminados+"");
		}
		
		//Se quita registro del mapa
		for(int i=pos;i<(numRegistros-1);i++)
		{
			referenciaForm.setMapaServicios("numeroReferencia_"+i, referenciaForm.getMapaServicios("numeroReferencia_"+(i+1)));
			referenciaForm.setMapaServicios("institucion_"+i, referenciaForm.getMapaServicios("institucion_"+(i+1)));
			referenciaForm.setMapaServicios("codigoServicioSirc_"+i, referenciaForm.getMapaServicios("codigoServicioSirc_"+(i+1)));
			referenciaForm.setMapaServicios("nombreServicioSirc_"+i, referenciaForm.getMapaServicios("nombreServicioSirc_"+(i+1)));
			referenciaForm.setMapaServicios("codigoServicio_"+i, referenciaForm.getMapaServicios("codigoServicio_"+(i+1)));
			referenciaForm.setMapaServicios("codigoCups_"+i, referenciaForm.getMapaServicios("codigoCups_"+(i)));
			referenciaForm.setMapaServicios("observaciones_"+i, referenciaForm.getMapaServicios("observaciones_"+(i+1)));
			referenciaForm.setMapaServicios("existe_"+i, referenciaForm.getMapaServicios("existe_"+(i+1)));
		}
		
		numRegistros--;
		
		referenciaForm.getMapaServicios().remove("numeroReferencia_"+numRegistros);
		referenciaForm.getMapaServicios().remove("institucion_"+numRegistros);
		referenciaForm.getMapaServicios().remove("codigoServicioSirc_"+numRegistros);
		referenciaForm.getMapaServicios().remove("nombreServicioSirc_"+numRegistros);
		referenciaForm.getMapaServicios().remove("codigoServicio_"+numRegistros);
		referenciaForm.getMapaServicios().remove("codigoCups_"+numRegistros);
		referenciaForm.getMapaServicios().remove("observaciones_"+numRegistros);
		referenciaForm.getMapaServicios().remove("existe_"+numRegistros);
		
		referenciaForm.setMapaServicios("numRegistros", numRegistros+"");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que realiza la ordenacion de servicios SIRC
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarServiciosSirc(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigo_",
				"descripcion_",
				"servicio_",
				"codigo_cups_",
				"nombre_cups_",
				"seleccionado_"
			};

		int numRegistros = Integer.parseInt(referenciaForm.getServiciosSirc("numRegistros").toString()); 
		referenciaForm.setServiciosSirc(Listado.ordenarMapa(indices,
				referenciaForm.getIndice(),
				referenciaForm.getUltimoIndice(),
				referenciaForm.getServiciosSirc(),
				numRegistros));
		
		referenciaForm.setServiciosSirc("numRegistros",numRegistros+"");
		
		referenciaForm.setUltimoIndice(referenciaForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaServiciosSirc");
	}

	/**
	 * Método implementado para realizar la búsqueda de los servicios SIRC
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaServiciosSirc(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		referenciaForm.setIndice("");
		referenciaForm.setUltimoIndice("");
		referenciaForm.setOffset(0);
		
		//Se consultan los servicios SIRC
		referenciaForm.setServiciosSirc(UtilidadesHistoriaClinica.obtenerServiciosSirc(con, usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSi,referenciaForm.getServiciosInsertados()));
		
		//Se asigna el maxPageItems
		referenciaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaServiciosSirc");
	}

	/**
	 * Método implementado para consultar los datos del convenio elegido y postular su informacion correspondiente
	 * de tipo der egimen y estratos sociales
	 * @param con
	 * @param referenciaForm
	 * @param response
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionBusquedaDatosConvenio(Connection con, ReferenciaForm referenciaForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		//Se consulta el tipo de regimen
		String[] vector = Utilidades.obtenerTipoRegimenConvenio(con, referenciaForm.getCodigoConvenio()+"").split("-");
		referenciaForm.setReferencia("codigoTipoRegimen", vector[0]);
		referenciaForm.setReferencia("nombreTipoRegimen", vector[1]);
		
		//Se consultan los estratos sociales del tipo de regimen
		referenciaForm.setEstratos(
				UtilidadesFacturacion.cargarEstratosSociales(con, usuario.getCodigoInstitucionInt(), 
						ConstantesBD.acronimoSi, vector[0],referenciaForm.getCodigoConvenio(),
						ConstantesBD.codigoNuncaValido, Utilidades.capturarFechaBD()));
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			String respuesta = "<respuesta>";
			respuesta += "<codigo-tiporegimen>"+vector[0]+"</codigo-tiporegimen>"+
				"<nombre-tiporegimen>"+vector[1]+"</nombre-tiporegimen>"+
				"<numero-estratos>"+referenciaForm.getEstratos("numRegistros")+"</numero-estratos>"+
				"<estratos>";
			for(int i=0;i<Integer.parseInt(referenciaForm.getEstratos("numRegistros").toString());i++)
			{
				respuesta += "<codigo>"+referenciaForm.getEstratos("codigo_"+i)+"</codigo>";
				respuesta += "<descripcion>"+referenciaForm.getEstratos("descripcion_"+i)+"</descripcion>";
			}
			respuesta += "</estratos></respuesta>";
	        response.getWriter().write(respuesta);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionBusquedaDatosConvenio: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para realizar la búsqueda de la instituciones SIRC
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaInstitucionSirc(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		Referencia referencia = new Referencia();
		
		referenciaForm.setInstitucionesSirc(referencia.busquedaInstitucionesSirc(con, referenciaForm.getReferencia("tipoReferencia").toString(), usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaInstitucionesSirc");
	}

	/**
	 * Método implementado para validar los datos ingresados por el paciente
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionValidarPaciente(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		boolean existePaciente = false; //verifica si existe el paciente
		
		//Estas validaciones solo aplican para la funcionalidad de referencia
		if(referenciaForm.getEstado().equals("validarPaciente"))
		{
			//Cuando es por el flujo normal de la referencia esta variable debe estar apagada
			referenciaForm.setDeboAbrirAsignacionCita(false);
			//No se debe filtar el campo tipo referencia
			referenciaForm.setFiltrarTipoReferencia(false);
		
			//************VALIDACION GENERAL DE LOS CAMPOS*************************
			//1) Validacion del tipo de identificacion
			boolean automatico = false;
			if(referenciaForm.getTipoIdentificacion().equals(""))
				errores.add("Tipo Identificacion Requerido",new ActionMessage("errors.required","El tipo de identificación"));
			else
				automatico = UtilidadTexto.getBoolean(referenciaForm.getTipoIdentificacion().split("-")[2]);
			
			//2) Validacion del número de identificacion
			if(referenciaForm.getNumeroIdentificacion().equals("")&&!automatico)
				errores.add("Número de identificacion Requerido",new ActionMessage("errors.required","El número de identificación"));
			
			//3) Validacion del tipo de referencia
			if(referenciaForm.getTipoReferencia().equals(""))
				errores.add("Tipo de Referencia Requerida", new ActionMessage("errors.required","El tipo de referencia"));
			//**********************************************************************
		}
		else
		{
			referenciaForm.setDeboAbrirAsignacionCita(UtilidadTexto.getBoolean(request.getParameter("deboAbrirAsignacionCita")));
			//Se toma el path de la funcionalidad ASignacion de citas
			if(referenciaForm.isDeboAbrirAsignacionCita())
			{
				String funcionalidad = UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con,usuario.getLoginUsuario(),46);
				if(funcionalidad.length()>0)
					referenciaForm.setPathFuncAsignacionCita("../"+funcionalidad.split(ConstantesBD.separadorSplit)[1]);
				else
					referenciaForm.setPathFuncAsignacionCita("");
			}
			else
				referenciaForm.setPathFuncAsignacionCita("");
			
			//Se verifica si se debe filtrar el campo tipo referencia (por  unicamente los valores remitir a otra institucion y transferencia tecnológica)
			referenciaForm.setFiltrarTipoReferencia(UtilidadTexto.getBoolean(request.getParameter("filtrarTipoReferencia")));
			
			
			
		}
		logger.info("Desde al ActionREferencia deboAbrirAsignacionCita=> "+referenciaForm.isDeboAbrirAsignacionCita());
		logger.info("Desde al ActionREferencia pathFuncAsignacionCita=> "+referenciaForm.getPathFuncAsignacionCita());
		
		
		//********OTRAS VALIDACIONES**********************************************
		if(errores.isEmpty())
		{
			//********VALIDACIONES RESPECTO AL PACIENTE**********************************************
			//Se verifica si existe 
			try
			{
				existePaciente=UtilidadValidacion.existePaciente(con, referenciaForm.getTipoIdentificacion().split("-")[0], referenciaForm.getNumeroIdentificacion());
			}
			catch(SQLException e)
			{
				logger.error("Error consultando si existe el paciente: "+e);
				existePaciente = false;
			}
			
			
			//1) No se puede ingresar referencia interna para pacientes que no existen
			if(!existePaciente&&referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
				errores.add("REferencia Interna sin paciente",new ActionMessage("error.historiaClinica.referencia.internaSinPaciente"));
			//****************************************************************************************************
			
			//*****VALIDACIONES DEL PARÁMETRO DE CADUCIDAD DE HORAS*************************************************
			//Validacion de las horas de caducidad (SOLO APLICA PARA EXTERNA)
			if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				String horasCaducidad = ValoresPorDefecto.getHorasCaducidadReferenciasExternas(usuario.getCodigoInstitucionInt());
				
				if((horasCaducidad==null||horasCaducidad.equals("")))
					errores.add("Horas de caducidad referencia externa",new ActionMessage("error.historiaClinica.referencia.faltaDefinirHorasCaducidad"));
			}
			//******************************************************************************************************
		}
		
		//***************************************************************************************
		
		
		
		//Si no hay errores
		if(errores.isEmpty())
		{
			//si no existe paciente se llama a la funcionalidad de Ingresar Paciente
			if(!existePaciente)
			{
				UtilidadBD.closeConnection(con);
				return new ActionForward(
					"/ingresarPacienteDummy/ingresarPacienteDummy.do"			+
						"?estado=decisionIngresoPacienteSistema"	+
						"&numeroIdentificacion="					+
						referenciaForm.getNumeroIdentificacion()	+
						"&tipoIdentificacion="						+
						referenciaForm.getTipoIdentificacion()	+
						"&ingresoDesdeReferencia=true"				+
						"&ingresoDesdeReservaCita=false"				+
						"&Submit=submit",
					true
				);
			}
			else
			{
				//Se carga el paciente en sesion***********************************************************************
				ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
				try 
				{
					paciente.cargar(con,new TipoNumeroId(referenciaForm.getTipoIdentificacion().split("-")[0],referenciaForm.getNumeroIdentificacion()));
					paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
				} 
				catch (Exception e) 
				{
					logger.info("Error en accionDetalle: "+e);
				}
				observable.addObserver(paciente);
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
				//**************************************************************************************************
				
				//Se hace llamado al formulario de la referencia
				return accionEmpezar(con,referenciaForm,mapping,paciente,usuario,request);
			}
		}
		//Si hay errores
		saveErrors(request, errores);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingreso");
	}

	
	/**
	 * Método que inicia el flujo de de referencia mostrando el formulario de captura
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		boolean cuentaAbierta = false;
		
		//se mantienen los datos principales
		String tipoIdentificacion = paciente.getCodigoTipoIdentificacionPersona() + "-" + paciente.getTipoIdentificacionPersona();
		//variable usada para el llamado a la reserva de camas
		String codigoTipoIdentificacion = paciente.getCodigoTipoIdentificacionPersona() + ConstantesBD.separadorSplit + ConstantesBD.acronimoNo + ConstantesBD.separadorSplit + paciente.getTipoIdentificacionPersona(false);
		String numeroIdentificacion = paciente.getNumeroIdentificacionPersona();
		String tipoReferencia = referenciaForm.getTipoReferencia();
		String estado = referenciaForm.getEstado();
		//datos segun llamado de la funcionalidad de asignacion citas
		//(solo aplica para la creación de cuentas de consulta externa)
		boolean deboAbrirAsignacionCita = referenciaForm.isDeboAbrirAsignacionCita();
		String pathFuncAsignacionCita = referenciaForm.getPathFuncAsignacionCita();
		
		//se blanquea formulario
		referenciaForm.reset();
		referenciaForm.setTipoReferencia(tipoReferencia);
		referenciaForm.setTipoIdentificacion(tipoIdentificacion);
		referenciaForm.setCodigoTipoIdentificacion(codigoTipoIdentificacion);
		referenciaForm.setNumeroIdentificacion(numeroIdentificacion);
		referenciaForm.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente",usuario.getCodigoInstitucionInt()));
		referenciaForm.setInstitucion(usuario.getCodigoInstitucion());
		referenciaForm.setEstado(estado);
		referenciaForm.setDeboAbrirAsignacionCita(deboAbrirAsignacionCita);
		referenciaForm.setPathFuncAsignacionCita(pathFuncAsignacionCita);
		
		
		
		//*******************VALIDACIONES **************************************************************************************
		//Validacion del estado de la cuenta activa (SOLO APLICA PARA INTERNA)
		int idCuenta = Utilidades.obtenerIdUltimaCuenta(con, paciente.getCodigoPersona());
		//Se verifica si el paciente tiene cuenta activa
		if(UtilidadValidacion.igualEstadoCuenta(con, idCuenta, ConstantesBD.codigoEstadoCuentaActiva)||
			UtilidadValidacion.igualEstadoCuenta(con, idCuenta, ConstantesBD.codigoEstadoCuentaAsociada))
			cuentaAbierta = true;
		//Si no tiene cuenta abierta no puede continuar (SOLO APLICA PARA INTERNA)
		if(!cuentaAbierta&&referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
			errores.add("Estado de cuenta inválido",new ActionMessage("error.paciente.estadoInvalido","Activa y Asociada"));
		
		//Validacion de las horas de caducidad (SOLO APLICA PARA EXTERNA)
		if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			String horasCaducidad = ValoresPorDefecto.getHorasCaducidadReferenciasExternas(usuario.getCodigoInstitucionInt());
			
			if((horasCaducidad==null||horasCaducidad.equals("")))
				errores.add("Horas de caducidad referencia externa",new ActionMessage("error.historiaClinica.referencia.faltaDefinirHorasCaducidad"));
			else
				//Se realiza la anulacion de las referencias externas caducadas
				Referencia.anularReferenciasExternasCaducadas(con, paciente.getCodigoPersona()+"", horasCaducidad, usuario.getLoginUsuario());
		}
		//**************************************************************************************************************************
		
		//se verifica si hubo errores
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
		//Solo si la cuenta está abierta se carga el ingreso
		if(cuentaAbierta)
			referenciaForm.setIdIngreso(UtilidadValidacion.obtenerIngreso(con, idCuenta));
		
		Referencia referencia = new Referencia();
		//Consulta de la referencia
		referenciaForm.setReferencia(referencia.cargar(con, paciente.getCodigoPersona(), referenciaForm.getIdIngreso(), referenciaForm.getTipoReferencia(),""));
		
		///Validacion del consecutivo disponible (SOLO APLICA PARA INTERNA NUEVA)
		if(!UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
			errores = this.validacionConsecutivoDisponible(con,referenciaForm,usuario,errores);
		
		
		//se verifica de nuevo si hubo errores
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
		//***********************CARGA DE INFORMACION************************************************
		this.cargarInformacion(con,referenciaForm,cuentaAbierta,idCuenta,usuario);
		//******************************************************************************************
		prepararMapas(con,referenciaForm,usuario,cuentaAbierta,idCuenta,paciente);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que realiza la validación del consecutivo disponible
	 * @param con
	 * @param referenciaForm
	 * @param usuario
	 * @param errores 
	 * @return
	 */
	private ActionErrors validacionConsecutivoDisponible(Connection con, ReferenciaForm referenciaForm, UsuarioBasico usuario, ActionErrors errores) 
	{
		if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			
			String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoReferencia, usuario.getCodigoInstitucionInt());
			
			logger.info("paso por aqui en validacion de consecutivo disponible!!!!!!!*"+consecutivo+"*");
			if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
				errores.add("Falta consecutivo disponible",new ActionMessage("error.historiaClinica.referencia.faltaDefinirConsecutivo"));
			else
			{
				try
				{
					//se asigna el proximo consecutivo
					referenciaForm.setConsecutivoInt(Integer.parseInt(consecutivo));
				}
				catch(Exception e)
				{
					logger.error("Error en validacionConsecutivoDisponible:  "+e);
					errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la referencia"));
				}
			}
			
			
			
		}
		return errores;
	}

	/**
	 * Método implementado para cargar la forma con los datos necesarios para la captura de datos en el formulario
	 * @param con
	 * @param referenciaForm
	 * @param cuentaAbierta
	 * @param idCuenta
	 * @param usuario
	 */
	private void cargarInformacion(Connection con, ReferenciaForm referenciaForm, boolean cuentaAbierta, int idCuenta, UsuarioBasico usuario) 
	{
		//Se cargan las ciudades*************************************************************
		referenciaForm.setCiudades(Utilidades.obtenerCiudades(con));
		
		//Se cargan las especialidades******************************************************************
		if(referenciaForm.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			int contador = 0;
			//Solo aplica cuando no existe referencia
			if(referenciaForm.getReferencia("codigoEspecialidad").toString().equals(""))
			{
				InfoDatosInt[] espeUsuario = usuario.getEspecialidades();
				for(int i=0;i<espeUsuario.length;i++)
				{
					if(espeUsuario[i].getActivo())
					{
						referenciaForm.setEspecialidades("codigo_"+contador, espeUsuario[i].getCodigo()+"");
						referenciaForm.setEspecialidades("descripcion_"+contador, espeUsuario[i].getNombre());
						contador++;
					}
					
				}
				referenciaForm.setEspecialidades("numRegistros", contador+"");
			}
			else
			{
				referenciaForm.setEspecialidades("codigo_"+contador, referenciaForm.getReferencia("codigoEspecialidad").toString());
				referenciaForm.setEspecialidades("descripcion_"+contador, referenciaForm.getReferencia("nombreEspecialidad").toString());
				contador++;
				referenciaForm.setEspecialidades("numRegistros", contador+"");
			}
			
		}
		else
			referenciaForm.setEspecialidades(Utilidades.obtenerEspecialidades());
		
		//Se cargan los motivos SIRC***********************************************************************************************************************
		referenciaForm.setMotivos(UtilidadesHistoriaClinica.obtenerMotivosSirc(con, usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoTipoMotivoReferencia, ConstantesBD.acronimoSi));
		
		//Se carga el convenio**********************************************************************************************************
		//Se cargan todos los convenios
		referenciaForm.setConvenios(Utilidades.obtenerConvenios(con, "", "",false,"",false));
		if(cuentaAbierta)
		{
			Cuenta cuenta = new Cuenta();
			cuenta.cargarCuenta(con, idCuenta+"");
			
			//Se asigna el convenio de la cuenta activa solo si no existe 
			if(referenciaForm.getReferencia("codigoConvenio").toString().equals(""))
			{	
				referenciaForm.setReferencia("codigoConvenio", cuenta.getCodigoConvenio());
				//Se asigna el tipo de regimen de la cuenta activa
				referenciaForm.setReferencia("codigoTipoRegimen", cuenta.getCodigoTipoRegimen());
				referenciaForm.setReferencia("nombreTipoRegimen", cuenta.getTipoRegimen());
				//Se asigna el estrato social
				referenciaForm.setEstratos("codigo_0", cuenta.getCodigoEstrato());
				referenciaForm.setEstratos("descripcion_0", cuenta.getEstrato());
				referenciaForm.setEstratos("numRegistros", "1");
				referenciaForm.setReferencia("codigoEstratoSocial", cuenta.getCodigoEstrato());
			}
			
			//Se asigna la vía de ingreso
			referenciaForm.setViaIngreso(Integer.parseInt(cuenta.getCodigoViaIngreso()));
			//se consulta la fecha de ingreso
			referenciaForm.setFechaIngreso(Utilidades.getFechaIngreso(con, idCuenta+"", Integer.parseInt(cuenta.getCodigoViaIngreso())));
			//se consulta la hora de ingreso
			referenciaForm.setHoraIngreso(UtilidadesManejoPaciente.getHoraIngreso(con, idCuenta, Integer.parseInt(cuenta.getCodigoViaIngreso())));
			//se obtiene el codigo del area		
			referenciaForm.setCodigoArea(cuenta.getCodigoArea());
		}
		else
		{
			
			//se cargan todos los estratos
			referenciaForm.setEstratos("numRegistros", "0");
			//se deja sin via de ingreso porque no tiene cuenta abierta
			referenciaForm.setViaIngreso(0);
			//se deja sin ingreso porque no tiene cuenta abierta
			referenciaForm.setFechaIngreso("");
			referenciaForm.setHoraIngreso("");
			//se deja sin area porque no tiene cuenta abierta
			referenciaForm.setCodigoArea(0);
		}
		
		//Si ya existe un tipo de regimen entonces se consultan los estratos sociales de ese convenios
		if(!referenciaForm.getReferencia("codigoTipoRegimen").toString().equals(""))
			referenciaForm.setEstratos(
					UtilidadesFacturacion.cargarEstratosSociales(con, usuario.getCodigoInstitucionInt(), 
							ConstantesBD.acronimoSi, referenciaForm.getReferencia("codigoTipoRegimen").toString(), 
							Utilidades.convertirAEntero(referenciaForm.getReferencia("codigoConvenio")+""),
							ConstantesBD.codigoNuncaValido, Utilidades.capturarFechaBD()));
		
		//Se cargan los estados de conciencia****************************************************************************
		referenciaForm.setEstadosConciencia(UtilidadesHistoriaClinica.obtenerEstadosConciencia(con, false));
		
		
	}

	/**
	 * Método implementado para cargar la forma con los datos del mundo
	 * @param con 
	 * @param con 
	 * @param referenciaForm
	 * @param usuario 
	 * @param cuentaAbierta 
	 * @param idCuenta 
	 * @param paciente 
	 * @param referencia
	 */
	private void prepararMapas(Connection con, ReferenciaForm referenciaForm, UsuarioBasico usuario, boolean cuentaAbierta, int idCuenta, PersonaBasica paciente) 
	{
		referenciaForm.setMapaServicios((HashMap)referenciaForm.getReferencia("mapaServicios"));
		referenciaForm.setMapaSignosVitales((HashMap)referenciaForm.getReferencia("mapaSignosVitales"));
		referenciaForm.setMapaResultados((HashMap)referenciaForm.getReferencia("mapaResultados"));
		
		//***********SE VERIFICA SI SE PUEDE INGRESAR EDAD GESTACIONAL*****************************************
		if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
			referenciaForm.setReferencia("editarEdadGestacional", ConstantesBD.acronimoSi);
		else
			referenciaForm.setReferencia("editarEdadGestacional", ConstantesBD.acronimoNo);
		
		//************PREPARACION DEL MAPA DE DIAGNOSTICOS******************************************************
		HashMap mapaDiag = (HashMap)referenciaForm.getReferencia("mapaDiagnosticos");
		int numDiagnosticos = Integer.parseInt(mapaDiag.get("numRegistros").toString());
		/**
		 * Nota * El mapaDiagnosticos debe ser modificado así:
		 * 1. Aislar el diagnostico principal de los demas diagnosticos en el caso de que exista
		 * 2. Unificar las keys acronimoDiagnostico,tipoCie,nombreDiagnostico en una sola llave valorDiagnostico para acomodar
		 * la informacion de diagnosticos a la búsqueda genérica de diagnosticos.
		 */
		int contador = 0;
		for(int i=0;i<numDiagnosticos;i++)
		{
			//se saca el principal
			if(UtilidadTexto.getBoolean(mapaDiag.get("principal_"+i).toString()))
			{
				referenciaForm.setMapaDiagnosticos("codigoPrincipal", mapaDiag.get("codigo_"+i));
				referenciaForm.setMapaDiagnosticos("numeroReferenciaPrincipal", mapaDiag.get("numeroReferencia_"+i));
				//unificacion de la informacion del diagnosticos acronimo + tipoCie + nombre
				referenciaForm.setMapaDiagnosticos("valorDiagnosticoPrincipal", 
					mapaDiag.get("acronimoDiagnostico_"+i)+ConstantesBD.separadorSplit+
					mapaDiag.get("tipoCie_"+i)+ConstantesBD.separadorSplit+
					mapaDiag.get("nombreDiagnostico_"+i));
				referenciaForm.setMapaDiagnosticos("existePrincipal", mapaDiag.get("existe_"+i));
			}
			//se sacan los relacionados
			else
			{
				referenciaForm.setMapaDiagnosticos("codigo_"+contador, mapaDiag.get("codigo_"+i));
				referenciaForm.setMapaDiagnosticos("numeroReferencia_"+contador, mapaDiag.get("numeroReferencia_"+i));
				//unificacion de la informacion del diagnosticos acronimo + tipoCie + nombre
				referenciaForm.setMapaDiagnosticos("valorDiagnostico_"+contador, 
					mapaDiag.get("acronimoDiagnostico_"+i)+ConstantesBD.separadorSplit+
					mapaDiag.get("tipoCie_"+i)+ConstantesBD.separadorSplit+
					mapaDiag.get("nombreDiagnostico_"+i));
				referenciaForm.setMapaDiagnosticos("existe_"+contador, mapaDiag.get("existe_"+i));
				referenciaForm.setMapaDiagnosticos("chequeado_"+contador, mapaDiag.get("chequeado_"+i));
				contador++;
			}
		}
		//El número de registros solo aplica para los relacionados
		referenciaForm.setMapaDiagnosticos("numRegistros", contador+"");
		//*********************************************************************************************************
		
		//Si es una referencia Nueva:
		if(!UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
		{
			// se asigna el código de la institucion
			referenciaForm.setReferencia("codigoInstitucion", usuario.getCodigoInstitucion());
			
			//Si es interna se postula cierta informacion
			if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				//1) Se asigna el consecutivo de consecutivos disponibles
				referenciaForm.setReferencia("consecutivoPuntoAtencion", referenciaForm.getConsecutivoInt()+"");
				if(!referenciaForm.getConsecutivoAnio().equals(""))
					referenciaForm.setReferencia("anioConsecutivo", referenciaForm.getConsecutivoAnio());
				
				//2)Se toma la informacion del profesional de la salud
				referenciaForm.setReferencia("profesionalSalud", Utilidades.obtenerNombreUsuarioXLogin(con,usuario.getLoginUsuario(),true));
				referenciaForm.setReferencia("codigoProfesional", usuario.getCodigoPersona()+"");
				referenciaForm.setReferencia("registroMedico", usuario.getNumeroRegistroMedico());
				
				//3)Se verifica si hay ciudad residencia en parámetros generales para postularla
				String ciudadGeneral = ValoresPorDefecto.getCiudadVivienda(usuario.getCodigoInstitucionInt());
				if(UtilidadCadena.noEsVacio(ciudadGeneral)&&!ciudadGeneral.equals(" - - "))
				{
					String[] vector = ciudadGeneral.split("-");
					referenciaForm.setReferencia("codigoCiudad", vector[0] + ConstantesBD.separadorSplit + vector[1]);
				}
				
				//4) Se consulta la ultima anamnesis del ingreso
				referenciaForm.setReferencia("anamnesis", UtilidadesHistoriaClinica.getUltimaAnamnesisIngreso(con, referenciaForm.getIdIngreso()));
				
			}
			//como la referencia no existe no se puede anular
			referenciaForm.setReferencia("puedoAnular", ConstantesBD.acronimoNo);
		}
		else
		{
			//********************VALIDACION DE SI SE PUEDE ANULAR (SOLO APLICA PARA EXISTENTES)****************************************
			try
			{
				//Verifico si puedo anular
				if(
				  //para interna: cuenta abierta, el usuario debe ser tratante y los estados de la referencia deben ser solicitada y en trámite
				  (
					referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna)&&
				    cuentaAbierta&&
				    UtilidadValidacion.esMedicoTratante(con, usuario, idCuenta).equals("")&&
				    (referenciaForm.getReferencia("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado)||referenciaForm.getReferencia("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite))
				  )
				  ||
				  //para externa: solo importa el estado en trámite
				  (
				     referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna)&&
				     referenciaForm.getReferencia("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite)
				   )
				  )
				     referenciaForm.setReferencia("puedoAnular", ConstantesBD.acronimoSi);
				else
					referenciaForm.setReferencia("puedoAnular", ConstantesBD.acronimoNo);
					
			}
			catch(SQLException e)
			{
				logger.error("Error haciendo la validacion si se pueda anular referencia: "+e);
				referenciaForm.setReferencia("puedoAnular", ConstantesBD.acronimoNo);
			}
			//****************************************************************************************************************
		}
		
		//********VALIDACION PERMISOS SECCION DE HISTORIA CLINICA***********************************************+
		//Se verifica si la sección es editable o no segun tipo de referencia y usuario
		if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			//Verifico si el usuario es profesional de la salud
			if(UtilidadValidacion.esProfesionalSalud(usuario))
				referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoSi);
			else
				referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoNo);
		}
		else
			referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoSi);
		//***************************************************************************************************
		
		//******SE VERIFICA SI LA OPCION SERÁ DE EDICION O LECTURA************************************
		//primero se pregunta si la referencia existe
		if(UtilidadTexto.getBoolean(referenciaForm.getReferencia("existe").toString()))
		{
			//Segun tipo de referencia
			if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				//Segun estado de la referencia interna
				if(referenciaForm.getReferencia("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
					referenciaForm.setReferencia("editable", ConstantesBD.acronimoSi);
				else
				{
					referenciaForm.setReferencia("editable", ConstantesBD.acronimoNo);
					//tampoco se permite editar la seccion de historia clinica aunque sea el profesional de la salud
					referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoNo);
				}
			}
			else if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				//Se gun estado de la referencia externa
				if(referenciaForm.getReferencia("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite))
					referenciaForm.setReferencia("editable", ConstantesBD.acronimoSi);
				else
				{
					referenciaForm.setReferencia("editable", ConstantesBD.acronimoNo);
					//tampoco se permite editar la seccion de historia clinica aunque sea el profesional de la salud
					referenciaForm.setReferencia("editarHistoriaClinica", ConstantesBD.acronimoNo);
				}
			}
		}
		else
			referenciaForm.setReferencia("editable", ConstantesBD.acronimoSi);
		//********************************************************************************************
		
		//********SE LLENAN LOS RESULTADOS EXAMENES DIAGNOSTICOS YA INSERTADOS (SOLO APLICA PARA INTERNA)*********************************
		if(referenciaForm.getReferencia("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			String procedimientosInsertados = "";
			for(int i=0;i<Integer.parseInt(referenciaForm.getMapaResultados("numRegistros").toString());i++)
			{
				if(referenciaForm.getMapaResultados("numeroSolicitud_"+i).toString().equals(""))
				{
					if(!procedimientosInsertados.equals(""))
						procedimientosInsertados += ",";
					procedimientosInsertados += referenciaForm.getMapaResultados("numeroSolicitud_"+i);
				}
			}
			referenciaForm.setProcedimientosInsertados(procedimientosInsertados);
			referenciaForm.setMapaResultados("procedimientosInsertados", procedimientosInsertados);
		}
		//**************************************************************************************************************
		
		//********SE LLENAN LOS SERVICIOS SIRC YA INSERTADOS***********************************************************
		String serviciosInsertados = "";
		for(int i=0;i<Integer.parseInt(referenciaForm.getMapaServicios("numRegistros").toString());i++)
		{
			if(!serviciosInsertados.equals(""))
				serviciosInsertados += ",";
			serviciosInsertados += referenciaForm.getMapaServicios("codigoServicioSirc_"+i) + "-" + referenciaForm.getMapaServicios("codigoServicio_"+i);
		}
		referenciaForm.setServiciosInsertados(serviciosInsertados);
		referenciaForm.setMapaServicios("serviciosInsertados", serviciosInsertados);
		//*************************************************************************************************************
		
		
	}

	/**
	 * Método que inicia el flujo de la referencia cuando se ingresa directamente por el menú
	 * @param con
	 * @param referenciaForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIngresarPaciente(Connection con, ReferenciaForm referenciaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(referenciaForm.getEstado().equals("ingresarPaciente"))
			referenciaForm.reset();
		//se consultan los tipos de identificacion para le ingreso de paciente
		referenciaForm.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "ingresoPaciente",usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingreso");
	}
}
