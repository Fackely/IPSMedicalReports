/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.action.enfermeria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
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
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;
import util.laboratorios.UtilidadLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.enfermeria.RegistroEnfermeriaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.interfaz.DtoInterfazNutricion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoLaboratorio;
import com.princetonsa.dto.manejoPaciente.DtoValoracionEnfermeria;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripDialFechaHora;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


/**
 * @author Andrï¿½s Mauricio Ruiz Vï¿½lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class RegistroEnfermeriaAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(RegistroEnfermeriaAction.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof RegistroEnfermeriaForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión "+e.toString());
				} 

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				HttpSession session=request.getSession();	
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				RegistroEnfermeriaForm forma = (RegistroEnfermeriaForm) form;
				String estado=forma.getEstado();

				String horaInicioPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
				String horaFinUltimoTurno=ValoresPorDefecto.getHoraFinUltimoTurno(usuario.getCodigoInstitucionInt());

				logger.warn(" Estado RegistroEnfermeriaAction [" + estado + "]\n\n\n");

				logger.info("-------------------------------------");
				logger.info("En RegistroEnfermeriaAction el Estado es  >> "+forma.getEstado());
				logger.info("-------------------------------------");

				forma.setAncla("");
				if(estado.equals("empezar"))
				{

					Collection erroresEncontrados=new ArrayList();
					if(horaInicioPrimerTurno==null || horaInicioPrimerTurno.trim().equals(""))
					{
						ElementoApResource elem= new ElementoApResource("error.parametrosGenerales.faltaDefinirParametro");
						elem.agregarAtributo("Hora inicio Primer Turno");
						erroresEncontrados.add(elem);
					}
					if(horaFinUltimoTurno==null || horaFinUltimoTurno.trim().equals(""))
					{
						ElementoApResource elem= new ElementoApResource("error.parametrosGenerales.faltaDefinirParametro");
						elem.agregarAtributo("Hora fin último Turno");
						erroresEncontrados.add(elem);
					}
					if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getTiempoMaximoGrabacion(usuario.getCodigoInstitucionInt())) || ValoresPorDefecto.getTiempoMaximoGrabacion(usuario.getCodigoInstitucionInt()).equals("-1"))
					{
						ElementoApResource elem= new ElementoApResource("error.parametrosGenerales.faltaDefinirParametro");
						elem.agregarAtributo("Tiempo Mï¿½ximo Grabaciï¿½n");
						erroresEncontrados.add(elem);
					}
					if(erroresEncontrados.size()>0)
					{
						request.setAttribute("conjuntoErroresObjeto", erroresEncontrados);
						request.setAttribute("codigoDescripcionError", "error.registroEnfermeria.cancelado");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
					return accionEmpezar(con, mapping, usuario, paciente, forma, request);
				}
				//****************ESTADOS RELACIONADOS CON LA OPCION POR AREA***********************************
				//Muestra la pï¿½gina de selecciï¿½n del centro de costo
				else if(estado.equals("centroCosto"))
				{
					return accionCentroCosto(con,forma,mapping,usuario);
				}
				else if (estado.equals("filtroHabitaciones"))
				{
					return accionFiltroHabitaciones(con,forma,usuario,response);
				}
				else if (estado.equals("filtroCamas"))
				{
					return accionFiltroCamas(con,forma,response);
				}
				else if (estado.equals("ordenarPacientesCentroCosto"))  //-Ordenar el listado de pacientes en centro de costo
				{			    
					return accionOrdenarPacientesCentroCosto(forma, mapping, con);
				}
				else if(estado.equals("listadoPacientesCentroCosto"))
				{
					return accionListadoPacientesCentroCosto(con, mapping, forma, usuario.getCodigoInstitucionInt(),request);
				}
				//**************************************************************************************************
				else if(estado.equals("paciente"))
				{
					forma.resetSecciones();
					forma.setConsultaXArea(false);
					return accionPaciente(con, mapping, usuario, paciente, forma, request, false);
				}
				else if(estado.equals("cargarSeccionDiagnosticosNanda"))
				{
					accionCargarSeccionDiagnosticosNanda(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionDieta"))
				{
					accionCargarSeccionDieta(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionMezclas"))
				{
					accionCargarSeccionMezclas(con, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}			
				else if(estado.equals("cargarSeccionControlLiquidos"))
				{
					accionCargarSeccionControlLiquidos(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionSignosVitales"))
				{
					accionCargarSeccionSignosVitales(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionSoporteRespiratorio"))
				{
					accionCargarSeccionSoporteRespiratorio(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionCateterSonda"))
				{
					accionCargarSeccionCateterSonda(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionHojaNeurologica"))
				{
					accionCargarSeccionSignosVitales(con, usuario, paciente,forma);
					accionCargarSeccionHojaNeurologica(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionExamenesFisicos"))
				{
					accionCargarSeccionExamenesFisicos(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				if(estado.equals("adicionarCampoVE"))
				{
					if(!forma.getEtiquetaCampoOtro().isEmpty())
					{
						DtoValoracionEnfermeria dtoResultado=new DtoValoracionEnfermeria();
						dtoResultado.setEtiquietaCampo(forma.getEtiquetaCampoOtro());
						dtoResultado.setCentroCosto(paciente.getCodigoArea());
						dtoResultado.setCampoOtro(ConstantesBD.acronimoSi);
						dtoResultado.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
						forma.getValoracionEnfermeria().add(dtoResultado);
						forma.setAncla("adicionarCampoVE");
					}
					forma.setEtiquetaCampoOtro("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("adicionarCampo"))
				{
					if(!forma.getEtiquetaCampoOtroResLab().isEmpty())
					{
						DtoResultadoLaboratorio dtoResultado=new DtoResultadoLaboratorio();
						dtoResultado.setEtiquietaCampo(forma.getEtiquetaCampoOtroResLab());
						dtoResultado.setCentroCosto(paciente.getCodigoArea());
						dtoResultado.setCampoOtro(ConstantesBD.acronimoSi);
						forma.getResultadoLaboratorios().add(dtoResultado);
						forma.setAncla("adicionarCampo");
					}
					forma.setEtiquetaCampoOtroResLab("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("cambiarPosicionPagerSecionVE"))
				{
					forma.setAncla("pagerSecionResultadosVE");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cambiarPosicionPagerSecionResulLab"))
				{
					forma.setAncla("pagerSecionResultados");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarSeccionAnotacionesEnfer"))
				{
					accionCargarSeccionAnotacionesEnfer(con, usuario, paciente,forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");  
				}
				else if(estado.equals("cargarSeccionCuidadosEspeciales"))
				{
					accionCargarSeccionCuidadosEspecialesEnfer(con, usuario, paciente, forma);
					forma.setIndiceCuidadoEnf(0);
					this.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("accionCargarSeccionCuidadosEspecialesEnferInd"))
				{
					forma.resetSecciones();
					return accionCargarSeccionCuidadosEspecialesEnferIndependiente(con, usuario, paciente, mapping, request, forma);
				}
				else if(estado.equals("accionGuardarSeccionCuidadosEnfer"))
				{
					return accionGuardarSeccionCuidadosEnfer(con, mapping, paciente, usuario, forma, request);
				}
				else if(estado.equals("cargarSeccionTomaMuestra"))
				{
					return accionCargarTomasMuestra(mapping, con, usuario, paciente,forma);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}			
				//Se carga el registro de enfermerï¿½a del paciente con el nï¿½mero de la cuenta
				else if (estado.equals("cargarRegEnfermeria")) 
				{			    
					return accionCargarPaciente(con, mapping, usuario, paciente, forma, request);
				}
				else if(estado.equals("consultarHistoSoporte"))
				{
					return accionConsultarHistoSoporte(con, forma, new RegistroEnfermeria(), forma.getFechaAnterioresSoporte(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), mapping, usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("histoSignosVitales"))
				{
					accionCargarSeccionSignosVitales(con, usuario, paciente,forma);	
					forma.setFechasHistoSignosVitales(new RegistroEnfermeria().consultarSignosVitalesHistoTodos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"") ,usuario.getCodigoInstitucionInt(), "", ""));
					this.cerrarConexion(con);
					return mapping.findForward("historicoSignosVitales");
				}
				else if(estado.equals("histoSoporte"))
				{
					accionCargarSeccionSoporteRespiratorio(con, usuario, paciente,forma);	

					forma.setFechasSoporteRespiratorio(new RegistroEnfermeria().consultarFechasHistoricoSoporte(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
					this.cerrarConexion(con);
					return mapping.findForward("historicoSoporte");
				}
				else if(estado.equals("histoNanda"))
				{
					forma.setFechasNanda(new RegistroEnfermeria().consultarFechasHistoricoNanda(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
					this.cerrarConexion(con);
					return mapping.findForward("historicoNanda");
				}
				else if(estado.equals("histoDieta"))
				{
					return cargarHistoricosDieta(con, mapping, usuario, paciente, forma, 0); //-Cargar las fechas del historico 
				}
				else if(estado.equals("historicoDieta"))
				{
					return cargarHistoricosDieta(con, mapping, usuario, paciente, forma, 1);  //-Cargar registros de una dieta determinada. 
				}
				else if(estado.equals("historicoDietaOrden"))
				{
					return cargarHistoricosDietaOrden(con, mapping, paciente, forma);   
				}
				else if(estado.equals("consultarHistoNanda"))
				{
					return accionConsultarHistoNanda(con, forma, new RegistroEnfermeria(), forma.getFechaAnterioresNanda(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), mapping, usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("historicoCuidadosEspeciales"))
				{
					return cargarHistoricoCuidadosEspeciales(con, mapping, paciente, forma);
				}
				else if(estado.equals("historicoAnotacionesEnfermeriaFechas"))
				{
					return cargarHistoricoAnotacionesEnfermeriaFechas(con, mapping, paciente, forma);
				}
				else if(estado.equals("historicoAnotacionesEnfermeria"))
				{
					return cargarHistoricoAnotacionesEnfermeria(con, mapping, paciente, forma,usuario);
				}
				else if(estado.equals("historicoTomaMuestras"))
				{
					return cargarHistoricoTomaMuestras(con, mapping, paciente, forma,usuario);
				}
				else if(estado.equals("cargarHistoricoCuidadosEspecialesFecha"))
				{
					return cargarHistoricoCuidadosEspecialesFecha(con, mapping, paciente, usuario,forma);
				}
				else if(estado.equals("volverListadoCuidadosEspeciales"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("historicoCuidadosEspecialesFechas");
				}
				else if(estado.equals("volverListadoAnotacionesEnfermeria"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("historicoAnotacionesEnfermeriaFechas");
				}
				else if (estado.equals("ingresarNanda")) 
				{			    
					this.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					return accionGuardar(con, mapping, paciente, usuario, forma, request,false);
				}
				else if(estado.equals("consultarHistoSignoVital"))
				{
					return accionConsultarHistoSignoVital(con, mapping, paciente, usuario, forma);
				}
				else if(estado.equals("volverFechasSignosVitales"))
				{
					/*request.getSession().setAttribute("pacienteActivo", paciente);

				response.sendRedirect("anterioresSignosVitales.jsp?param=signosVitalesAnteriores");*/

					this.cerrarConexion(con);
					return mapping.findForward("historicoSignosVitales");
				}

				else if(estado.equals("imprimir"))
				{
					this.generarReporte(con, usuario, request);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimirControlLiquidos"))
				{
					return accionImprimirControlLiquidos(con, request,  mapping, paciente, usuario, forma);
				}
				else if(estado.equals("cerrarNotas"))
				{
					//Valida si el codigo ya existe
					if(forma.getValidacionesCierreAperturaNotasMap("codigoCierreNota").toString().equals(ConstantesBD.codigoNuncaValido+""))
						return accionGuardar(con, mapping, paciente, usuario, forma, request,true);
					else
					{
						return modificarCierreNotas(con, mapping, usuario, paciente, forma, request,true,true);
					}					
				}
				else if(estado.equals("abrirNotas"))
				{
					return modificarCierreNotas(con, mapping, usuario, paciente, forma, request,false,true);				
				}
				//***********ESTADOS SECCION PRESCRIPCION DIALISIS*************************************
				else if (estado.equals("cargarSeccionPrescripcionDialisis"))
				{
					accionCargarSeccionPrescripcionDialisis(con,mapping,usuario,paciente,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("historicoPrescripcionDialisis"))
				{
					return accionHistoricoPrescripcionDialisis(con,mapping,usuario,paciente,forma);
				}
				else if(estado.equals("histoSignosVitalesDummy"))
				{
					//se obtienen los datos que se le envian por el request
					obtenerDatosRequest(forma, request);

					accionCargarSeccionSignosVitalesDummy(con, usuario,forma);	
					forma.setFechasHistoSignosVitales(new RegistroEnfermeria().consultarSignosVitalesHistoTodos(con, forma.getCuentas() ,usuario.getCodigoInstitucionInt(), "", ""));
					this.cerrarConexion(con);
					return mapping.findForward("historicoSignosVitales");
				} else if(estado.equals("volverBusqueda")) {
					return mapping.findForward("resultadoPacientesCentroCosto");
				}


				//************************************************************************************
				this.cerrarConexion(con);

			}//if

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	
	//*************************************************************************************************
	
	/**
	 * Mï¿½todo implementado para cargar el histï¿½rico de las prescripciones de diï¿½lisis
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private ActionForward accionHistoricoPrescripcionDialisis(Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		if(forma.getHistoricoDialisis().size()==0)
			forma.setHistoricoDialisis(OrdenMedica.getHistoricoPrescripcionDialisisStatic(con, paciente.getCodigoPersona()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoPrescripcionDialisis");
	}
	
	//*************************************************************************************************

	/**
	 * Mï¿½todo implementado para cargar la seccion de prescripciï¿½n de diï¿½lisis
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionPrescripcionDialisis(
			Connection con, ActionMapping mapping, UsuarioBasico usuario,
			PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{	
		
		//Si no existï¿½a una prescripcion cargada se trata de cargar
		if(forma.getDialisis().getModalidadTerapia().equals(""))
		{
			if(forma.getHistoricoDialisis().size()==0)
				forma.setHistoricoDialisis(OrdenMedica.getHistoricoPrescripcionDialisisStatic(con, paciente.getCodigoPersona()));
			
			if(forma.getHistoricoDialisis().size()>0)
			{
				DtoPrescripcionDialisis ultimaDialisis = new DtoPrescripcionDialisis();
				ultimaDialisis = forma.getHistoricoDialisis().get(0);
				forma.setDialisis(ultimaDialisis);
				
				//Si la modalidad terapia fue hemodialisis se carga el arreglo de filtros
				if(forma.getDialisis().getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis))
					forma.getDialisis().setFiltros(OrdenMedica.cargarArregloPrescripcionDialisis(con, DtoPrescripcionDialisis.tipoConsultaFiltro,ultimaDialisis.getHemodialisis().get(0).getCodigoTipoMembrana()+""));
				
				
				if(!forma.getDialisis().isFinalizado())
				{
					//Se genera un nuevo registro en blanco para registrar fecha/hora inicio dialisis
					DtoPrescripDialFechaHora fechaHora = new DtoPrescripDialFechaHora();
					fechaHora.setProfesional(usuario);
					fechaHora.setFechaInicialDialisis(UtilidadFecha.getFechaActual(con));
					fechaHora.setHoraInicialDialisis(UtilidadFecha.getHoraActual(con));
					forma.getDialisis().getFechasHorasIniciales().add(0,fechaHora); //Se aï¿½ade el elemento al inicio
				}			
			}
		}	
	}
	
	//*************************************************************************************************


	/**
	 * Mï¿½todo que realiza el filtro de camas por habitacion
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroCamas(Connection con, RegistroEnfermeriaForm forma, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		forma.setCamas(UtilidadesManejoPaciente.obtenerCamasXHabitacion(con, forma.getCodigoHabitacion()));
		arregloAux = forma.getCamas();
		
		
		//Revision de las habitaciones segun piso seleccionado
		for(int i=0;i<arregloAux.size();i++)
		{
			HashMap elemento = (HashMap)arregloAux.get(i);
			resultado += "<cama>" +
				"<codigo>"+elemento.get("codigo")+"</codigo>"+
				"<numero-cama>"+elemento.get("numeroCama")+"</numero-cama>"+
				"<nombre>"+elemento.get("descripcion")+"</nombre>"+
			 "</cama>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltroCamas: "+e);
		}
		return null;
	}
	
	//*************************************************************************************************


	/**
	 * Mï¿½todo que realiza el filtro de las habitaciones
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroHabitaciones(Connection con, RegistroEnfermeriaForm forma, UsuarioBasico usuario, HttpServletResponse response) 
	{
		String resultado = "<respuesta>";
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		forma.setHabitaciones(UtilidadesManejoPaciente.obtenerHabitaciones(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), Integer.parseInt(forma.getCodigoPiso())));
		arregloAux = forma.getHabitaciones();
		
		
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
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroHabitaciones: "+e);
		}
		return null;
	}
	
	//*************************************************************************************************

	/**
	 * Mï¿½todo que inicia el flujo del registro de enfermeria por ï¿½rea
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionCentroCosto(Connection con, RegistroEnfermeriaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.resetBusqueda();
		//Se cargan los datos de seleccion
		forma.setAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, 0, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(),""));
		forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()));
		forma.setCentroCostoSeleccionado(usuario.getCodigoCentroCosto());
		forma.setConsultaXArea(true);
		
		this.cerrarConexion(con);
		return mapping.findForward("centroCosto");
	}
	
	//*************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param forma
	 * @param usuario 
	 * @return
	 */
	private ActionForward cargarHistoricoAnotacionesEnfermeria(Connection con, ActionMapping mapping, PersonaBasica paciente, RegistroEnfermeriaForm forma, UsuarioBasico usuario)
	{
		
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		
		String fechaInicio="";
		String fechaFin="";
		String horaPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
		fechaInicio=forma.getFechaInicio()+"-"+horaPrimerTurno;
		
		fechaFin=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicio()), 1, false))+"-"+horaPrimerTurno;
		
	 	forma.setHistoricoAnotacionesEnfermeria(mundo.consultarAnotacionesEnfermeria(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, fechaFin));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoAnotacionesEnfermeria");
	}
	
	//*************************************************************************************************
	
	@SuppressWarnings("unchecked")
	private ActionForward cargarHistoricoTomaMuestras(Connection con, ActionMapping mapping, PersonaBasica paciente, RegistroEnfermeriaForm forma, UsuarioBasico usuario)
	{
		
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		
		HashMap mp = new HashMap();
		
		mp.put("cuentas",UtilidadesManejoPaciente.obtenerCuentasXIngreso(con,paciente.getCodigoIngreso()+""));

	 	forma.setHistoricoTomaMuestras(mundo.consultarTomaMuestrasHistorico(con,mp));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoTomaMuestras");
	}
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private ActionForward cargarHistoricoAnotacionesEnfermeriaFechas(Connection con, ActionMapping mapping, PersonaBasica paciente, RegistroEnfermeriaForm forma)
	{
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		forma.setHistoricoAnotacionesEnfermeriaFechas(mundo.consultarAnotacionesEnfermeriaFechas(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoAnotacionesEnfermeriaFechas");
	}

	//*************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private ActionForward cargarHistoricoCuidadosEspeciales(Connection con, ActionMapping mapping, PersonaBasica paciente, RegistroEnfermeriaForm forma)
	{
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		forma.setMapaColsCuiEspHistoricoFechas(mundo.consultarColsCuidadosEspeciales(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),"", ""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoCuidadosEspecialesFechas");
		
	}
	
	//*************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param usuario 
	 * @param forma
	 * @return
	 */
	private ActionForward cargarHistoricoCuidadosEspecialesFecha(Connection con, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, RegistroEnfermeriaForm forma)
	{
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		
		String fechaInicio="";
		String fechaFin="";
		String horaPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
		fechaInicio=forma.getFechaInicio()+"-"+horaPrimerTurno;
		
		fechaFin=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaInicio()), 1, false))+"-"+horaPrimerTurno;
		
		
		forma.setMapaColsCuidadosEspecialesHistorico(mundo.consultarColsCuidadosEspeciales(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, fechaFin));
	 	forma.setMapaCuidadosEspecialesHistorico(mundo.consultarCuidadosEnfermeria(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, fechaFin));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoCuidadosEspeciales");
		
	}
	
	//*************************************************************************************************

	/**
	 * Funcion para consultar las solicitudes del paciente en estado solicitada.
	 * @param mapping 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private ActionForward accionCargarTomasMuestra(ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma)
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		HashMap mp = new HashMap();
		
		mp.put("cuentas",UtilidadesManejoPaciente.obtenerCuentasXIngreso(con,paciente.getCodigoIngreso()+""));
		forma.setMapaMuestra( mundo.consultarTomaMuestras(con, mp) );
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");  
	}
	
	//*************************************************************************************************

	/**
	 * Funcion para generar el PDF de control de liquidos.
	 * @param con
	 * @param request 
	 * @param mapping
	 * @param paciente
	 * @param usuario  
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionImprimirControlLiquidos(Connection con, HttpServletRequest request, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, RegistroEnfermeriaForm forma) throws SQLException
	{
		//RegistroEnfermeria mundo = new RegistroEnfermeria();
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
        DesignEngineApi comp;        
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"interfaz/","programacionRangos.rptdesign");
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        Vector v=new Vector();
        

        int nroFilas = 5;
        v.add("                 "); v.add("  Instituciï¿½n	 : " + usuario.getInstitucion() ); 	 v.add("                 ");		
        v.add("                 "); v.add("  				   ");		 						 v.add("                 ");
        v.add("                 "); v.add("  				   ");		 						 v.add("                 ");		
       	v.add("                 ");	v.add("Parametros de Busqueda ");  v.add("                 ");
		v.add("				    "); v.add("                		  ");  v.add("                 ");
        	
        //-En la columna 1 Fila 0 del Grid de Encabezado se insertara una tabla de 3 columna y 3 Filas 
        comp.insertGridHeaderOfMasterPage(0, 1, 3, nroFilas);
        comp.insertLabelInGridOfMasterPage(0, 1, v);
                
	    //--Insertar EL Nombre Del Usuario Que Imprimio El Reporte. 
	    comp.insertarUsuarioImprimio(usuario.getNombreUsuario());
	    
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        request.setAttribute("isOpenReport", "true");
        request.setAttribute("newPathReport", newPathReport);
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("principal");
	}
	
	//*************************************************************************************************
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n Diagnï¿½sticos Nanda
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionDiagnosticosNanda(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		/*
		 * Consultar el histï¿½rico de los diagnï¿½sticos de enfermerï¿½a
		 */
		manejoDiagnosticosEnfermeria(con, mundo, forma, usuario, paciente);
	}
	
	//*************************************************************************************************	
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n dieta
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionDieta(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
				
		mundo.cargarDietaOrdenMedica(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),  paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaInicio);
			
		//-Informacion de finalizacion de la dieta.	
		forma.setFechaGrabacionDietaOrden(mundo.getFechaGrabacionDietaOrden());
		forma.setFechaRegistroDietaOrden(mundo.getFechaRegistroDietaOrden());
		forma.setMedicoDietaOrden(mundo.getMedicoDietaOrden());
		forma.setObservacionDietaParenteOrden(mundo.getObservacionDietaParenteOrden());
				
		//---------Informacion de Dieta
		forma.setHayDieta(mundo.getHayDieta());
		forma.setNutricionOral(mundo.getNutricionOral());
		forma.setNutricionParenteral(mundo.getNutricionParenteral());
		forma.setTiposNutricionOral(mundo.getTiposNutricionOral());
		
		if(mundo.getSuspendidoEnfermeria() != null)
		{
			if(mundo.getSuspendidoEnfermeria().equals(ConstantesBD.acronimoSi))
				forma.setFinalizarDietaEnfermeria(true);
			else
				if(mundo.getSuspendidoEnfermeria().equals(ConstantesBD.acronimoNo))
					forma.setFinalizarDietaEnfermeria(false);
		}
		else
			forma.setFinalizarDietaEnfermeria(false);
		
		forma.setFinalizarDietaEnfermeriaAnt(forma.isFinalizarDietaEnfermeria());
		forma.setObservacionesEnfermeria(mundo.getObservacionesEnfermeria());
	}
	
	//*************************************************************************************************
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n dieta
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionMezclas(Connection con, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		forma.setMapaMezclas(mundo.mezclasPendientesPaciente(con,paciente.getCuentasPacienteArray()));
	}


	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n dieta
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionControlLiquidos(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		
		mundo.cargarDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),  paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaInicio);
				
		forma.setMapaDieta(mundo.getMapaDieta());
		forma.setMapaDietaHistorico(mundo.getMapaDietaHistorico());
		forma.setMapaDietaOrdenes(mundo.getMapaDietaOrdenes());
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n de signos vitales
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionSignosVitales(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		logger.info("ESTOY CARGANDO LOS SIGNOS VITALES!!!!!");
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		
		
		
		//-----------Se cargan los tipos de signos vitales parametrizados por instituciï¿½n centro de costo-----------//
	 	forma.setSignosVitalesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), 1));
	 	//----Se quitan los tipos de signos vitales repetidos en la colecciï¿½n------//
	 	forma.setSignosVitalesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
	 	
	 	logger.info("EL TAMAÑO DE ESTO ES----->"+forma.getSignosVitalesInstitucionCcosto().size());
		
	 	//------------- Consulta el histï¿½rico de los signos vitales fijos--------------------------//
	 	forma.setSignosVitalesFijosHisto(mundo.consultarSignosVitalesFijosHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), fechaInicio, ""));
	 	
	 	logger.info("\n pase!!!  -->"+forma.getSignosVitalesInstitucionCcosto().size());
	 	//------------- Consulta el histï¿½rico de los signos vitales pamatrizados por institucion centro costo--------------------------//
	 	if(forma.getSignosVitalesInstitucionCcosto().size()>0)
	 		forma.setSignosVitalesParamHisto(mundo.consultarSignosVitalesParamHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), fechaInicio, ""));
	 	else
	 		forma.setSignosVitalesParamHisto(new ArrayList ());
	 	
	 	logger.info("\n pase2!!!  -->"+forma.getSignosVitalesFijosHisto().size()+"   -- "+forma.getSignosVitalesParamHisto().size());
	 	//------------- Consulta los cï¿½digos histï¿½ricos, fecha registro y hora registro de los signos vitales fijos y parametrizados--------------------------//
	 	if(forma.getSignosVitalesFijosHisto().size()>0 || forma.getSignosVitalesParamHisto().size()>0)
	 		forma.setSignosVitalesHistoTodos(mundo.consultarSignosVitalesHistoTodos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), fechaInicio, ""));
	 	
	 	//----------- Consulta fechas histï¿½ricas de los signos vitales fijos y parametrizados para mostrar en ver anteriores----------------------//
	 	//forma.setFechasHistoSignosVitales(mundo.consultarSignosVitalesHistoTodos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"") ,usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), "", ""));	
	}
	
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n de signos vitales
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionSignosVitalesDummy(Connection con, UsuarioBasico usuario, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		
		//-----------Se cargan los tipos de signos vitales parametrizados por instituciï¿½n centro de costo-----------//
	 	forma.setSignosVitalesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), forma.getCuentas(), 1));
	 	//----Se quitan los tipos de signos vitales repetidos en la colecciï¿½n------//
	 	forma.setSignosVitalesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
		
	 	//------------- Consulta el histï¿½rico de los signos vitales fijos--------------------------//
	 	forma.setSignosVitalesFijosHisto(mundo.consultarSignosVitalesFijosHisto(con, forma.getCuentas(), fechaInicio, ""));
	 	
	 	logger.info("\n pase!!!  -->"+forma.getSignosVitalesInstitucionCcosto().size());
	 	//------------- Consulta el histï¿½rico de los signos vitales pamatrizados por institucion centro costo--------------------------//
	 	if(forma.getSignosVitalesInstitucionCcosto().size()>0)
	 		forma.setSignosVitalesParamHisto(mundo.consultarSignosVitalesParamHisto(con, forma.getCuentas(), usuario.getCodigoInstitucionInt(), fechaInicio, ""));
	 	else
	 		forma.setSignosVitalesParamHisto(new ArrayList ());
	 	
	 	logger.info("\n pase2!!!  -->"+forma.getSignosVitalesFijosHisto().size()+"   -- "+forma.getSignosVitalesParamHisto().size());
	 	//------------- Consulta los cï¿½digos histï¿½ricos, fecha registro y hora registro de los signos vitales fijos y parametrizados--------------------------//
	 	if(forma.getSignosVitalesFijosHisto().size()>0 || forma.getSignosVitalesParamHisto().size()>0)
	 		forma.setSignosVitalesHistoTodos(mundo.consultarSignosVitalesHistoTodos(con, forma.getCuentas(), usuario.getCodigoInstitucionInt(), fechaInicio, ""));
	 	
	 	//----------- Consulta fechas histï¿½ricas de los signos vitales fijos y parametrizados para mostrar en ver anteriores----------------------//
	 	//forma.setFechasHistoSignosVitales(mundo.consultarSignosVitalesHistoTodos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"") ,usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), "", ""));	
	}
	
	
	/**
	 * Metodo encargado resivir los datos provenientes de otra funcionalidad mediante request
	 * @param forma
	 * @param request
	 */
	public static void obtenerDatosRequest (RegistroEnfermeriaForm forma,HttpServletRequest request)
	{
		//los valores enviados por request
		//---------------------------------------------------------------
		
		//1) cuenta
		forma.setCuentas(request.getParameter("cuentas")!=null?request.getParameter("cuentas"):"");
		
					
		//---------------------------------------------------------------
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n de soporte respiratorio
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionSoporteRespiratorio(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		
		forma.setOpcionesSoportesRespiratorioInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"") , 6));
		llenarHistoricoSoporteRespiratorio(con, forma, mundo, fechaInicio, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""));
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n de catetor sonda
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionCateterSonda(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		//String fechaInicio=obtenerFechasHistorico(usuario, true);
		
		//-----------Se cargan las columnas de catï¿½teres y sondas parametrizadas por instituciï¿½n centro de costo-----------//
		forma.setColCateteresSondaInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), 3));
	 	//----Se quitan las columnas de cateter sonda repetidas en la colecciï¿½n------//
	 	forma.setColCateteresSondaInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getColCateteresSondaInstitucionCcosto(), "codigo_tipo"));
	 	
	 	//---- Se cargan los articulos (insumos) cateter sonda para la instituciï¿½n y que se les hizo una solicitud de medicamentos
	 	//-----y se encuentran despachados para el pacient
	 	forma.setArticulosDespachadosCatSonda(mundo.consultarCateterSondaDespachados(con, paciente.getCodigoCuenta(), usuario.getCodigoInstitucionInt()));
	 	
	 	//---------------- Consulta del histï¿½rico de los cateter sonda fijos --------------------// 
	 	forma.setCateterSondaFijosHisto(mundo.consultarCateterSondaFijosHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
	 	
	 	//	 	---------------- Consulta del histï¿½rico de los cateter sonda parametrizados --------------------//
	 	if(forma.getColCateteresSondaInstitucionCcosto().size()>0)
	 		forma.setCateterSondaParamHisto(mundo.consultarCateterSondaParamHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), paciente.getCodigoArea()));
	 	
	 	//	 	---------------- Consulta del histï¿½rico de los cateter sonda parametrizados --------------------//
	 	if(forma.getCateterSondaFijosHisto().size()>0 || forma.getCateterSondaParamHisto().size()>0)
	 		forma.setCateterSondaHistoTodos(mundo.consultarCateterSondaTodosHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), paciente.getCodigoArea()));
	 	
	 	HashMap mapaCateterHisto=new HashMap();
	 	mapaCateterHisto=formarHistoricoCateterSonda(forma);
	 	
	 	//----------Se verifica si hay registros en el histï¿½rico de cateter sonda para ordenarlo----------//
	 	if(Integer.parseInt(mapaCateterHisto.get("numRegistros")+"")>0)
		 	{
		 	//-------Se ordena el histï¿½rico por la fecha de inserciï¿½n del cateter sonda -----------// 	
		 	ordenarHistoricoCateterSonda(forma, mapaCateterHisto);
		 	}
	 	else
	 	{
	 		forma.setMapaHistoricoCateterSonda(mapaCateterHisto);
	 	}
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n hoja neurolï¿½gica
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionHojaNeurologica(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		
	 	forma.setTamanosPupilas(mundo.consultarTiposInstitucionCCosto(con, -1, "-1", 10));
	 	
	 	forma.setReaccionesPupilas(mundo.consultarTiposInstitucionCCosto(con, -1, "-1", 11));
	 	
	 	//-----------Se consultan las caracterï¿½sticas y especificaciones Glasgow por instituciï¿½n centro de costo-----------//
	 	forma.setEspecificacionesGlasgowCcIns(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), 9));
	 	
	 	//----Se quitan los tipos de escala glasgow en la colecciï¿½n------//
	 	forma.setEspecificacionesGlasgowCcIns(Utilidades.coleccionSinRegistrosRepetidos(forma.getEspecificacionesGlasgowCcIns(), "codigo"));
	 		 	
	 	//----------Se consulta el histï¿½rico de la escala glasgow -------------------------------//
	 	forma.setHistoricoEscalaGlasgow(mundo.consultarHistoricoEscalaGlasgow(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
	  		 	
	 	forma.setMapaPupilas(mundo.consultarHistoricoPupilas(con, fechaInicio, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
	 	
	 	forma.setTiposConvulsiones((HashMap)mundo.obtenerTiposConvulsiones(con).clone());
		forma.setHistoricoConvulsiones((HashMap)mundo.obtenerHistoricosConvulsiones(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")).clone());
		
		//Informacion de Control ed esfintetres
		forma.setHistoricoControlEsfinteres((HashMap)mundo.consultarHistoricoControlEsfinteres(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));
		//se llena la informacion de fuerza muscular
		forma.setMapaTiposFuerzaMuscular(mundo.consultarTiposFuerzaMuscular(con));
		forma.setMapaFuerzaMuscular(mundo.obtenerHistoricosFuerzaMuscular(con, paciente.getCodigoCuenta()+""));
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n Exï¿½menes Fï¿½sicos
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionExamenesFisicos(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		//----Se quitan los exï¿½mes fï¿½sicos repetidos en la colecciï¿½n------//
	 	forma.setExamenesFisicosInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getExamenesFisicosInstitucionCcosto(), "codigo_tipo"));
	 	
	 	//--------- Consulta los exï¿½menes fï¿½sicos ingresados ---------//
	 	mundo.cargarExamenesFisicos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""));
	 	
	 	//------------------------- Exï¿½menes fï¿½sicos--------------------------//
		forma.setMapaExamenesFisicos(mundo.getMapaExamenesFisicos());
	}
	
	/**
	 * Mï¿½todo que consulta la informaciï¿½n de la secciï¿½n Anotaciones de Efnermerï¿½a
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 */
	private void accionCargarSeccionAnotacionesEnfer(Connection con, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma) 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		Collection colMensaje=mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), 8);
	 	Iterator iterador=colMensaje.iterator();
	 	while(iterador.hasNext())
	 	{
	 		HashMap fila=(HashMap)iterador.next();
	 		forma.setMensajeAnotaciones((String)fila.get("mensaje"));
	 	}
	 	
	 	//---------- Consulta de las anotaciones de enfermerï¿½a ingresadas ---------------------//
		String fechaInicio=obtenerFechasHistorico(usuario, true);
	 	forma.setHistoricoAnotacionesEnfermeria(mundo.consultarAnotacionesEnfermeria(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, ""));
	}
	
	/**
	 * Metodo para car4gar los historicos de la dieta 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param tipoInfomacion : es el tipo de informacion que se va a cargar.
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward cargarHistoricosDietaOrden(Connection con, ActionMapping mapping, PersonaBasica paciente, RegistroEnfermeriaForm forma) throws SQLException
	{
		//-Se Necesita El Mundo de Orden Medica para traer el historico de dieta.  
		//---Consultar Historicos de detalle de nutricion Oral DE ORDENES MEDICAS ...   
		OrdenMedica mundoOrdenMedica = new OrdenMedica();  
		forma.setListadoNutOralHisto( mundoOrdenMedica.consultarNutricionOralHisto(con,paciente.getCodigoCuenta(),paciente.getCodigoCuentaAsocio()));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("historicoDietaOrden");
	}
	
	
	
	
	/**
	 * Metodo para car4gar los historicos de la dieta 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param tipoInfomacion : es el tipo de informacion que se va a cargar.
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward cargarHistoricosDieta(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma, int tipoInfomacion) throws SQLException
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaFin = "";
		

		
		if (tipoInfomacion == 0) ///-Cargar solamente las fechas iniciales de historicos
		{
			fechaFin = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) +"-" + ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
			//forma.setMapaDietaHistorico(mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 0, paciente.getCodigoCuentaAsocio()));
			forma.setMapaDietaHistorico(mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 0));
			forma.setMapaDietaHistorico("fechaHistoricoDieta","0");
			forma.setMapaDietaHistorico("paginadorLiqAdmin","0");
			forma.setMapaDietaHistorico("paginadorLiqElim","0");
		}

		if (tipoInfomacion == 1) ///-Cargar los registros historicos de una fecha determinada.
		{
			//---Incrementar un dia para Mostrar Todos los Historicos a partir de una fecha determinada. 
			fechaFin = forma.getMapaDietaHistorico("fechaHistoricoDieta")+"";
			fechaFin = fechaFin + "&&&&" +  UtilidadFecha.incrementarDiasAFecha(fechaFin, 1, true) + "-" + ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
			
			HashMap mp = new HashMap();
			
			//----------------------------Consultar los nombres de los medicamentos Administrados  
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 1, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 1);
			forma.setMapaDietaHistorico("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 2, paciente.getCodigoCuentaAsocio());			
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 2);			
			forma.setMapaDietaHistorico("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar la informacion registrada de liquidos administados ( parametrizables y no parametrizables ) y eliminados (parametrizados).  
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 3, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 3);
			forma.setMapaDietaHistorico("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//----Carga el numero de registros eliminados en el mapa.
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 4, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 4);
			forma.setMapaDietaHistorico("nroRegBalLiqElim", mp.get("numRegistros")+"");

			forma.setMapaDietaHistorico("fechaHistoricoDieta","1");
			forma.setMapaDietaHistorico("paginadorLiqAdmin","0");
			forma.setMapaDietaHistorico("paginadorLiqElim","0");
		}
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("historicoDieta");
	}
	
	/**
	 * Mï¿½todo para generar el reporte de las anotaciones de enfermeria cuando
	 * se consulta el registro de enfermeria
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 */
	private void generarReporte(Connection con, UsuarioBasico usuario, HttpServletRequest request) 
    {
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"enfermeria/","AnotacionesEnfermeria.rptdesign");
	    comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
	    comp.insertGridHeaderOfMasterPage(0,1,1,4);
	    Vector v = new Vector();
	    v.add(institucionBasica.getRazonSocial());
	    v.add(institucionBasica.getTipoIdentificacion()+"     "+institucionBasica.getNit());  
	    
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    comp.insertLabelInGridPpalOfHeader(1,0,"");
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);    
	    
	    if(!newPathReport.equals(""))
	    {
	        request.setAttribute("isOpenReport", "true");
	        request.setAttribute("newPathReport", newPathReport);
	    }
	    
	    
	    
	    //por ultimo se modifica la conexion a BD
	    comp.updateJDBCParameters(newPathReport);
    }
	
	/**
	 * Mï¿½todo para consultar el histï¿½rico de los diagnï¿½sticos de enfermerï¿½a
	 * @param con
	 * @param forma
	 * @param enfermeria
	 * @param fechaAnterioresNanda
	 * @param codigoCuenta
	 * @param cuentaAsocio @todo
	 * @param mapping
	 * @return Pï¿½gina de listado de diagnï¿½sticos
	 */
	private ActionForward accionConsultarHistoNanda(Connection con, RegistroEnfermeriaForm forma, RegistroEnfermeria enfermeria, String fechaAnterioresNanda, String cuentas, ActionMapping mapping, int codigoInstitucion)
	{
		forma.resetNanda();
		String fechaFin=UtilidadFecha.incrementarDiasAFecha(forma.getFechaAnterioresNanda(), 1, true);
		HashMap historico=new RegistroEnfermeria().consultarDiagnosticosNanda(con, cuentas, codigoInstitucion, forma.getFechaAnterioresNanda(), fechaFin);
		forma.setAnterioresDiagnosticosNandaHistorico(historico);
		this.cerrarConexion(con);
	 	return mapping.findForward("historicoNanda");
	}
	
	/**
	 * Mï¿½todo para guardar el registro de enfermerï¿½a
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @return request
	 * @throws SQLException
	 */
	private ActionForward accionGuardarSeccionCuidadosEnfer(
			Connection con, 
			ActionMapping mapping, 
			PersonaBasica paciente, 
			UsuarioBasico usuario, 
			RegistroEnfermeriaForm forma, 
			HttpServletRequest request)  throws SQLException
	{		
		int respCuidadosEnf=0;
				
		RegistroEnfermeria mundo=new RegistroEnfermeria();
		this.llenarMundo(forma, mundo, usuario);	
		
		//se limpian las alertas 
		forma.resetInterfaz ();
		//-Primero de se debe insertar el registro de de enfermetia.
		int codRegEnfer = mundo.insertarRegistroEnfermeria(con, paciente.getCodigoCuenta());
		
		//-Si se inserto el registro de enfermeria correctamente
		if (codRegEnfer > 0)
		{
			//-Segundo se debe insertar el encabezado
			int codEncabezado = mundo.insertarEncabezadoRegistroEnfermeria(con, codRegEnfer, usuario.getLoginUsuario(), UtilidadTexto.agregarTextoAObservacion(null, null, usuario, false) );
			
			//-Si se inserto el registro de enfermeria correctamente
			if (codEncabezado > 0)
			{		
				//------------------------------------------------------------------------------------
				//Almacena la informaciï¿½n
				if(RegistroEnfermeria.insertarModificarCuidadosEnfermeria(
						con, 
						paciente.getCodigoIngreso(),
						forma.getMapaCuidadosEspeciales(), 
						forma.getArrayFrecuenciasCuidadoEnfer(), 
						usuario.getLoginUsuario()))
				{
					respCuidadosEnf = verificarDatosCuidadosEnfer(forma);
					if (respCuidadosEnf >0)
					{
						//----------Inserciï¿½n de los cuidados especiales de enfermerï¿½a-------------//
						mundo.insertarCuidadosEspeciales(con, codEncabezado, usuario);
					}
				}
				else
					respCuidadosEnf = ConstantesBD.codigoNuncaValido;
				//-------------------------------------------------------------------------------------							
			}			
		}
		
		return accionCargarSeccionCuidadosEspecialesEnferIndependiente(con, usuario, paciente, mapping, request, forma); 			
	}
	

	/**
	 * Mï¿½todo para guardar el registro de enfermerï¿½a
	 * @param con
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @return request
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, RegistroEnfermeriaForm forma, HttpServletRequest request,boolean CerrarNota) throws SQLException
	{
		int respSv=0;
		int respCatSonda=0;
		int respCuidadosEnf=0;
				
		RegistroEnfermeria mundo=new RegistroEnfermeria();
		this.llenarMundo(forma, mundo, usuario);
		
		logger.info("Se crea el mundo correctamente");
		
		//se limpian las alertas 
		forma.resetInterfaz ();
		//-Primero de se debe insertar el registro de de enfermetia.
		int codRegEnfer = mundo.insertarRegistroEnfermeria(con, paciente.getCodigoCuenta());
		
		//-Si se inserto el registro de enfermeria correctamente
		if (codRegEnfer > 0)
		{
			//-Segundo se debe insertar el encabezado
			int codEncabezado = mundo.insertarEncabezadoRegistroEnfermeria(con, codRegEnfer, usuario.getLoginUsuario(), UtilidadTexto.agregarTextoAObservacion(null, null, usuario, false) );
			
			//-Si se inserto el registro de enfermeria correctamente
			if (codEncabezado > 0)
			{
				//Se verifica si se deben insertar los signos vitales fijos y parametrizados
				respSv=verificarInsertaronDatosSignoVital(forma);
				
				if(respSv>0)
				{
					mundo.insertarSignosVitales(con, codEncabezado, respSv);
				}
				
				String fechaInicio=obtenerFechasHistorico(usuario, true);
				//-Insertar la dieta ( seccion Liquidos Medicamentos / Infusiï¿½n  )	
				//mundo.insertarDieta(con, codRegEnfer, paciente.getCodigoCuenta(), codEncabezado, usuario.getLoginUsuario(),  usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaInicio, paciente.getCodigoCuentaAsocio());
				
				//insertar respuestalaboratorio.
				mundo.insertarValoracionEnfermeria(con, codEncabezado,forma.getValoracionEnfermeria()) ;

				//insertar respuestalaboratorio.
				mundo.insertaResultadosLaboratorios(con, codEncabezado,forma.getResultadoLaboratorios());

			
				
				logger.info("**********  INSERTAR DIETA *********");
				
				logger.info("CODIGO DE ENCABEZADO Y ESTAMOS HECHOS ->"+codEncabezado+"<-");
				
				mundo.insertarDieta(con, codRegEnfer, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), codEncabezado, usuario.getLoginUsuario(),  paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaInicio);
				forma.setFinalizarTurno(false); 
				
				logger.info("CODIGO CUENTA PACIENTE ->"+paciente.getCodigoCuenta());
				
				int codOrdenDieta = mundo.consultarUltimaDietaActiva(con, paciente.getCodigoCuenta());
				
				logger.info("-------CODIGO ULTIMA ORDEN DIETA----"+codOrdenDieta);
				
				// -------------------ACTUALIZAR ESTADO Y OBSERVACION ENFERMERIA DE LA ORDEN DIETA ----
				
				String observacionesEnfermera = usuario.getLoginUsuario()+"-"+UtilidadFecha.getFechaActual()+"-"+UtilidadFecha.getHoraActual()+" -> "+forma.getNuevaObservDietaEnfermeria()+"\n"+forma.getObservacionesEnfermeria();
				
				boolean updateOrden = mundo.actualizarOrdenDieta(con, codOrdenDieta, forma.isFinalizarDietaEnfermeria(), observacionesEnfermera);
				
				// -------------------------------------------------------------------------------------
				
				//---------Se verifica si insertaron algï¿½n dato en la secciï¿½n cateter sonda ------//
				respCatSonda = verificarDatosCateterSonda(forma);
				if (respCatSonda >0)
					{
					//----------- Inserciï¿½n de los catï¿½teres y sonda ------------------------//
					mundo.insertarCateteresSonda(con,codEncabezado, usuario, respCatSonda);
					}
				
				//--------------- Insertar las Anotaciones de Enfermerï¿½a ---------------------//
				if(UtilidadCadena.noEsVacio(forma.getAnotacionEnfermeria()))
				{
					mundo.insertarAnotacionEnfermeria(con, codEncabezado);
				}
				
				if(mundo.getDiagnosticosEnfermeria().size()>0)
				{
					mundo.ingresarDiagnosticosNanda(con, codEncabezado);
				}

				if(mundo.getSoporteRespiratorio().size()>0)
				{
					mundo.ingresarSoporteRespiratorio(con, codEncabezado);
				}
				
				//-------Se verifica si insertaron Exï¿½menes Fï¿½sicos ----------//
				if (forma.isInsertaronExamenesFisicos())
				{
					//-------------- Inserciï¿½n de los exï¿½menes fisicos --------------------//
					mundo.insertarExamenesFisicos(con,  codRegEnfer);
				}				
				//------------------------------------------------------------------------------------
				//Almacena la informaciï¿½n
				if(RegistroEnfermeria.insertarModificarCuidadosEnfermeria(
						con, 
						paciente.getCodigoIngreso(),
						forma.getMapaCuidadosEspeciales(), 
						forma.getArrayFrecuenciasCuidadoEnfer(), 
						usuario.getLoginUsuario()))
				{
					respCuidadosEnf = verificarDatosCuidadosEnfer(forma);
					if (respCuidadosEnf >0)
					{
						//----------Inserciï¿½n de los cuidados especiales de enfermerï¿½a-------------//
						mundo.insertarCuidadosEspeciales(con, codEncabezado, usuario);
					}
				}
				else
					respCuidadosEnf = ConstantesBD.codigoNuncaValido;
				//-------------------------------------------------------------------------------------				
				//guardar seccion hoja neurologica -- subseccion convulsiones
				if(!forma.getConvulsion().trim().equals(""))
				{
					this.accionGuardarConvulsiones(con,codEncabezado,forma,mundo);
				}
				//guardar seccion hoja neurologica -- sub seccion control de esfinteres
				if(forma.getCodigoControlEsfinteres() != ConstantesBD.codigoNuncaValido)
				{
					this.accionGuardarControlEsfinteres(con, codEncabezado, forma, mundo);
				}
				//guardar seccion hoja neurologica -- subseccion fuerza muscular
				if(puedoInsertarFuerzaMuscular(forma))
				{
					this.accionGuardarFuerzaMuscular(con, codEncabezado+"", forma, mundo);
				}

				
				if(verificarIngresoPupilas(forma.getMapaPupilas()) && UtilidadTexto.getBoolean(forma.getExisteHojaNeurologica()))
				{
					if(this.accionGuardarPupila(con, codEncabezado, forma.getMapaPupilas(), mundo, usuario)<1)
					{
						logger.error("Error guardando la pupila");
					}
				}
				
				//---------------------Inserciï¿½n de la Escala Glasgow ------------------------------//
				mundo.insertarEscalaGlasgow(con, codEncabezado, usuario);
			
				
//				-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*	
				//---------------------------------------------------------	I N T E R F A Z      N U T R I C I O N -------------------------------------------------------
				
				logger.info("ESTADO DIETA ANTERIOR ->"+forma.isFinalizarDietaEnfermeriaAnt());
				logger.info("  ESTADO DIETA ACTUAL ->"+forma.isFinalizarDietaEnfermeria());
				
				//COMPARO SI HUBO CAMBIO EN EL ESTADO DE LA DIETA PARA ACTIVAR LA INTERFAZ
				if(forma.isFinalizarDietaEnfermeria() != forma.isFinalizarDietaEnfermeriaAnt())
				{
					//CAPTURO EL PARAMETRO GENERAL DE INTERFAZ NUTRICION
					forma.setInterfazNutricion(mundo.consultarParametroInterfazNutricion(con));
					
					logger.info("\n\n\n\n PARAMETRO GENERAL INTERFAZ NUTRICION ->"+forma.getInterfazNutricion()+"<-");
					
					
					//VALIDO EL PARAMETRO GENERAL INTERFAZ NUTRICION PARA ENVIAR INFORMACION AL SISTEMA EXTERNO
					
					if(forma.getInterfazNutricion().equals(ConstantesBD.acronimoSi))
					{
						
						//Se instancia el DTO
						DtoInterfazNutricion dto = new DtoInterfazNutricion();
						
						//Se resetea el HashMap
						forma.setNutricionOralMap(new HashMap()); 
						forma.setNutricionOralMap(mundo.tiposNutricionOralActivo(con, paciente.getCodigoCuenta()));
						int registrosNutricionOral =Utilidades.convertirAEntero(forma.getNutricionOralMap().get("numRegistros").toString());
						
						logger.info("MAPA DE TIPOS NUTRICION ORAL ->"+forma.getNutricionOralMap());
						
						//Cargo el DTO
						dto.setIngreso(paciente.getConsecutivoIngreso()+"");
						dto.setNumhis(paciente.getHistoriaClinica());
						
						dto.setCoddie(codOrdenDieta+""); //este codigo si es el nuevo campo de la tabla tipo_nutricion_oral
						String descripcionDieta = forma.getNuevaObservDietaEnfermeria();
						int size = descripcionDieta.length();
						
						String descrip1="";
						String descrip2="";
						
						size= descripcionDieta.length();
						
						if(size>220)
						{
							descrip1 = descripcionDieta.substring(0, 220);
							descrip2 = descripcionDieta.substring(220, size);
							
						}
						else
						{
							descrip1 = descripcionDieta.substring(0, size);
						}
						logger.info("\n\n\n DESCRIPCION 1 ->"+descrip1);
						logger.info("\n\n \n DESCRIPCION 2 ->"+descrip2);
						
						dto.setDescr1(descrip1);
						dto.setDescr2(descrip2);
						
						dto.setRegusu(usuario.getLoginUsuario());
						
						dto.setEstreg(ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado+"");
					
						// CONSULTAR LA FECHA DE LA DIETA
						String fechaDie = mundo.consultarFechaDieta(con, codOrdenDieta);
						String fechasplit[] = fechaDie.split("-");
						fechaDie=fechasplit[0]+fechasplit[1]+fechasplit[2];
						
						dto.setFecdie(fechaDie);
						
						// CONSULTAR LA HORA DE LA DIETA
						String horaDie = mundo.consultarHoraDieta(con, codOrdenDieta);
						String horasplit[] = horaDie.split(":");
						horaDie = horasplit[0]+horasplit[1]+horasplit[2];
						
						dto.setHordie(horaDie);
						
						// CONSULTAR LA FECHA DE GRABACION
						String FechaEnv = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()); //PARA REVISAR 
						logger.info("*-*-**-*-*-*-*-*-*-FECHA DE GRABACION -*-*-*->"+FechaEnv+"<-");
						fechasplit=FechaEnv.split("-");
						FechaEnv = fechasplit[0]+fechasplit[1]+fechasplit[2];
						
						dto.setFecenv(FechaEnv);
						
						// CONSULTAR LA HORA DE GRABACION
						String horaEnv = UtilidadFecha.getHoraSegundosActual();
						logger.info("*-*-**-*-*-*-*-*-*-HORA DE GRABACION -*-*-*->"+horaEnv+"<-");
						horasplit=horaEnv.split(":");
						horaEnv = horasplit[0]+horasplit[1]+horasplit[2];
						
						dto.setHorenv(horaEnv);
						
						//CAMBIO, Adicion de Campo a la tabla Ax_nutri. Agosto 25 de 2008 por aesilva
						dto.setViaing(paciente.getCodigoUltimaViaIngreso()+"");
						
						dto.setTipopac(paciente.getCodigoTipoPaciente());
						
						if(forma.isFinalizarDietaEnfermeria() == true)
						{
							//CUANDO FINALIZAR DIETA ES TRUE SIGNIFICA QUE ESTA SUSPENDIDA
							dto.setEstdie(ConstantesBDInterfaz.estadoDietaSuspendido+"");
						}
						else
						{
							dto.setEstdie(ConstantesBDInterfaz.estadoDietaActivo+"");
						}
						
						ResultadoBoolean inserto;
						
						//VALIDO VIA DE INGRESO HOSPITALIZACION 
						// Se retira la restriccion de validar que exista cama, && paciente.getCodigoCama()>0	 Enero 27 de 2009
						if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion)
						{
							logger.info("----------------> PACIENTE POR VIA INGRESO HOSPITALIZACION <-------------");
							logger.info("----------------------> SE ENVIA INFORMACION A SHAIO <-------------------");
							
							dto.setIdvia(ConstantesBDInterfaz.interfazNutricionHospitalizacion);
							dto.setSecama(mundo.consultarPisoCama(con, paciente.getCodigoCama()));
							dto.setNucama(mundo.consultarNumeroCama(con, paciente.getCodigoCama()));
							dto.setPacvip(" ");
							
							// Recorro todos los tipos de Dieta para enviar registro por cada uno
							for(int x=0; x<registrosNutricionOral;x++)
							{
								dto.setCoddie(forma.getNutricionOralMap().get("codigointerfaz_"+x)+"");
								UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
								
								inserto = interfaz.insertarInterfazNutricion(dto, paciente.getTipoInstitucion(),false); //SE INSERTA EN AS400 SHAIO

								if (!inserto.isTrue()) {
									ArrayList alertas= new ArrayList();
									alertas.add(inserto.getDescripcion());
									forma.setMensajes(alertas);
								}
							}
							
						}
						else // VALIDO VIA INGRESO URGENCIAS EN OBSERVACION
							// Se retira la restriccion de validar que exista cama, && paciente.getCodigoCama()>0	 Enero 27 de 2009
							if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias )
							{
								logger.info("-----------> PACIENTE POR VIA INGRESO URGENCIAS CON OBSERVACION <--------");
								logger.info("----------------------> SE ENVIA INFORMACION A SHAIO <-------------------");
								
								dto.setIdvia(ConstantesBDInterfaz.interfazNutricionUrgenciasObserva);
								dto.setSecama(mundo.consultarPisoCama(con, paciente.getCodigoCama()));
								dto.setNucama(mundo.consultarNumeroCama(con, paciente.getCodigoCama()));
								
								String vip = mundo.consultarConvenioVip(con, paciente.getCodigoConvenio());
								
								if(vip.equals(ConstantesBD.acronimoSi))
									dto.setPacvip(ConstantesBDInterfaz.vipConvenioSi);
								if(vip.equals(ConstantesBD.acronimoNo))
									dto.setPacvip(ConstantesBDInterfaz.vipConvenioNo);
								
								// Recorro todos los tipos de Dieta para enviar registro por cada uno
								for(int x=0; x<registrosNutricionOral;x++)
								{
									dto.setCoddie(forma.getNutricionOralMap().get("codigointerfaz_"+x)+"");
									UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
									
									inserto = interfaz.insertarInterfazNutricion(dto, paciente.getTipoInstitucion(),false); //SE INSERTA EN AS400 SHAIO
									
									if (!inserto.isTrue()) {
										ArrayList alertas= new ArrayList();
										alertas.add(inserto.getDescripcion());
										forma.setMensajes(alertas);
									}
								}
							}
							else
							{
								logger.info("--------------------------------------------------------------------------------");
								logger.info("--EL PACIENTE NO SE ENCUENTRA EN LA VIA DE INGRESO REQUERIDA PARA LA INTERFAZ---");
								logger.info("--------------------------------------------------------------------------------");
							}
					}
				}
				//-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
				
				
				
				//Evlaua si debe cerrar la Nota de Enfermeria
				if(CerrarNota)
					modificarCierreNotas(con, mapping, usuario, paciente, forma, request,true,false);
				
				//SE verifica si se modificï¿½ la prescripcion dialisis
				logger.info("FUE MODIFICADO  EN ENFERMERIA: "+forma.getDialisis().fueModificadoEnEnfermeria());
				if(forma.isDeboAbrirPrescripcionDialisis()&&forma.getDialisis().fueModificadoEnEnfermeria())
				{
					forma.getDialisis().setProfesional(usuario);
					int respDialisis = OrdenMedica.modificarPrescripcionDialisis(con, forma.getDialisis());
					if(respDialisis<=0)
					{
						UtilidadBD.abortarTransaccion(con);
						ActionErrors errores = new ActionErrors();
						errores.add("", new ActionMessage("errors.noPudoActualizar","información de prescripción diálisis. Por favor reportar el error al administrador del sistema. Transacción cancelada"));
						saveErrors(request, errores);
						return mapping.findForward("paginaErroresActionErrors"); 
					}
				}
				
				
			}//if codEncabezado > 0
			
			/**
			 * Si se marca el check de revisado se modifican los registros de alerta inactivandolos
			 * hasta que se cree un nuevo registro de órden médica
			 */
			if (forma.isRevisado()) {
				RegistroEnfermeria.actualizarRegistroAlertaOrdenesMedicas(con, forma.getListaAlertasOrdenesMed(), 
						forma.getFechaInicioRegistro(), forma.getHoraInicioRegistro(), 
						new Long(codRegEnfer), 
						usuario.getLoginUsuario());
			}
			
		}	// if codRegEnfer > 0

		
		
		/* 
		 * insertarTomaMuestra(con, mundo, forma, usuario.getLoginUsuario());
		 * 
		 * Esto se modificó en la revisión 48875 del SVN (jearias),
		 * según el comentario es por el anexo 738,
		 * revisé el anexo y no veo por que se modificó, lo voy a organizar
		 * según anexo 328
		 * (Juan David Ramírez)
		 */
		if(validarInertarTomaMuestra(forma))
		{
			insertarTomaMuestra(con, mundo, forma, usuario.getLoginUsuario());
		}
				
		return accionPaciente(con, mapping, usuario, paciente, forma, request,true);
		//return mapping.findForward("principal");
	}

	public boolean validarInertarTomaMuestra(RegistroEnfermeriaForm forma)
	{
		int numRows = util.UtilidadCadena.vInt(forma.getMapaMuestra("numRegistros")+"");
		for(int j=0; j<numRows;j++) 
		{
			if ( UtilidadTexto.getBoolean(forma.getMapaMuestra("tm_" +j)+"") )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Actualiza el indicador de Cierre de Notas
	 * @param Connection con
	 * @param ActionMapping mapping
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param RegistroEnfermeriaForm forma
	 * @param HttpServletRequest request
	 * @param boolean esCerrar
	 * */
	public ActionForward modificarCierreNotas(
			Connection con,
			ActionMapping mapping,
			UsuarioBasico usuario,
			PersonaBasica paciente,			
			RegistroEnfermeriaForm forma,
			HttpServletRequest request,
			boolean esCerrar,
			boolean recargarPaciente)
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		//Actualiza las Notas de Enfermeria
		mundo.actualizarRegistroEnfermeria(con,forma.getValidacionesCierreAperturaNotasMap("codigoCierreNota").toString(),esCerrar);
		
		try
		{
			if(recargarPaciente)
				return accionPaciente(con, mapping, usuario, paciente, forma, request,true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo para insertar los registros de Tomas de Muestras de laboratorios.
	 * @param con
	 * @param mundo 
	 * @param forma 
	 * @param login 
	 * @throws SQLException 
	 */
	private void insertarTomaMuestra(Connection con, RegistroEnfermeria mundo, RegistroEnfermeriaForm forma, String login) throws SQLException
	{
		//solo puede pasar a toma de muestras si antes tenia un estado solicitada
		
		int numRows = util.UtilidadCadena.vInt(forma.getMapaMuestra("numRegistros")+"");
		for(int j=0; j<numRows;j++) 
		{
			//logger.info("Check de muestras    -------------------"+forma.getMapaMuestra("tm_" +j)+"     "+UtilidadTexto.getBoolean(forma.getMapaMuestra("tm_" +j)+""));
			if ( UtilidadTexto.getBoolean(forma.getMapaMuestra("tm_" +j)+"") )
			{
				String 	fecha= forma.getMapaMuestra("fecha_ing_"+j)+"";
				String 	hora = forma.getMapaMuestra("hora_ing_"+j)+"";
				//logger.info("Número de solicitud toma muestra "+forma.getMapaMuestra("solicitud_"+j));
				UtilidadLaboratorios.pasarSolicitudATomaMuestras(con,forma.getMapaMuestra("solicitud_"+j)+"",fecha,hora,login,ConstantesBD.codigoEstadoHCTomaDeMuestra);
			}
		}
	}

	/**
	 * @param con
	 * @param codEncabezado
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private int accionGuardarPupila(Connection con, int codEncabezado, HashMap mapaPupilas, RegistroEnfermeria mundo, UsuarioBasico usuario)
	{
		int tamanioD=0;
		int tamanioI=0;
		String reaccionD=null;
		String reaccionI=null;
		String obsDerecha="";
		String obsIzquierda="";
		String textoPupila=(String)mapaPupilas.get("tamanio_d");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			tamanioD=Integer.parseInt(textoPupila);
		}
		textoPupila=(String)mapaPupilas.get("tamanio_i");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			tamanioI=Integer.parseInt(textoPupila);
		}
		textoPupila=(String)mapaPupilas.get("reaccion_d");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			reaccionD=textoPupila;
		}
		textoPupila=(String)mapaPupilas.get("reaccion_i");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			reaccionI=textoPupila;
		}
		textoPupila=(String)mapaPupilas.get("obsDerecha");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			obsDerecha=UtilidadTexto.agregarTextoAObservacionFechaGrabacion(null, textoPupila, usuario, true);
			
		}
		textoPupila=(String)mapaPupilas.get("obsIzquierda");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			obsIzquierda=UtilidadTexto.agregarTextoAObservacionFechaGrabacion(null, textoPupila, usuario, true);
		}
		return mundo.accionGuardarPupila(con, codEncabezado, tamanioD, tamanioI, reaccionD, reaccionI, obsDerecha, obsIzquierda);
	}


	/**
	 * @param mapaPupilas
	 * @return
	 */
	private boolean verificarIngresoPupilas(HashMap mapaPupilas)
	{
		String textoPupila=(String)mapaPupilas.get("tamanio_d");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			int valor=Integer.parseInt(textoPupila);
			if(valor!=0)
			{
				return true;
			}
		}
		textoPupila=(String)mapaPupilas.get("tamanio_i");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			int valor=Integer.parseInt(textoPupila);
			if(valor!=0)
			{
				return true;
			}
		}
		textoPupila=(String)mapaPupilas.get("reaccion_d");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			return true;
		}
		textoPupila=(String)mapaPupilas.get("reaccion_i");
		if(UtilidadCadena.noEsVacio(textoPupila))
		{
			return true;
		}
		return false;
	}


	/**
	 * Mï¿½todo que devuelve la fecha y hora del inicio de registros historicos
	 * @param usuario
	 * @param formatoBD
	 * @return
	 */
	private String obtenerFechasHistorico(UsuarioBasico usuario, boolean formatoBD)
	{
		String fechaActual=UtilidadFecha.getFechaActual();
		if(formatoBD)
		{
			fechaActual=UtilidadFecha.conversionFormatoFechaABD(fechaActual);
		}
		String horaActual=UtilidadFecha.getHoraActual();
		String fechaInicio="";
		//String fechaFin="";

		String horaPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
		//String horaUltimoTurno=ValoresPorDefecto.getHoraFinUltimoTurno(usuario.getCodigoInstitucionInt());
		if(horaActual.equals(horaPrimerTurno))
		{
			fechaInicio=fechaActual+"-"+horaActual;
		}
		else if(horaActual.compareTo(horaPrimerTurno)>0)
		{
			fechaInicio=fechaActual+"-"+horaPrimerTurno;
		}
		else if(horaActual.compareTo(horaPrimerTurno)<0)
		{
			if(formatoBD)
			{
				fechaInicio=UtilidadFecha.incrementarDiasAFecha(fechaActual, -1, formatoBD)+"-"+horaPrimerTurno;
			}
			else
			{
				fechaInicio=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, formatoBD)+"-"+horaPrimerTurno;
			}
		}
		return fechaInicio;
	}

	/**
	 * Mï¿½todo para pasar los datos de la forma al mundo
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void llenarMundo(RegistroEnfermeriaForm forma, RegistroEnfermeria mundo, UsuarioBasico usuario)
	{
		Vector diagnosticosEnfermeria=new Vector();
		int numeroDiagnosticosEnfermeria=Integer.parseInt((String)forma.getDiagnosticosNanda().get("numRegistros"));
		//boolean insercionDatosNanda=false;
		for(int i=0; i<numeroDiagnosticosEnfermeria;i++)
		{
			Vector diagnostico=new Vector();
			diagnostico.add((String)forma.getDiagnosticosNanda().get("codigo_"+i));
			diagnostico.add((String)forma.getDiagnosticosNanda().get("observacion_"+i));
			diagnosticosEnfermeria.add(diagnostico);
			//insercionDatosNanda=true;
		}

		/*
		 * Llenado de los datos de soporte respiratorio para se mandados a la BD
		 */
		//boolean insercionDatosSoporte=false;
		HashMap soporteRespiratorio=forma.getSoporteRespiratorio();
		Vector soporteRespiratorioValores=new Vector();
		for(int i=0; i<forma.getSoportesRespiratorioInstitucionCcosto().size(); i++)
		{
			String valor=(String)soporteRespiratorio.get("valor_"+i);
			if(valor!=null && !valor.trim().equals(""))
			{
				Vector soporteRespFila=new Vector();
				int codElemento=Integer.parseInt(soporteRespiratorio.get("codigo_"+i)+"");
				int tipoDato=Integer.parseInt(soporteRespiratorio.get("tipo_"+i)+"");
				soporteRespFila.add(new Integer(codElemento));
				soporteRespFila.add(new Integer(tipoDato));
				if(tipoDato==ConstantesBD.codigoTipoDatoLista)
				{
					soporteRespFila.add(new Integer(Integer.parseInt(valor)));
				}
				else if(tipoDato==ConstantesBD.codigoTipoDatoTexto)
				{
					soporteRespFila.add(valor);
				}
				//insercionDatosSoporte=true;
				soporteRespiratorioValores.add(soporteRespFila);
			}
		}
		// Las observaciones son generales para todos los tipos de soporte
		String obsSoporte=soporteRespiratorio.get("observaciones")+"";
		
		mundo.setSoporteRespiratorio(soporteRespiratorioValores);
		mundo.setObsSoporte(obsSoporte);
		
		
		mundo.setDiagnosticosEnfermeria(diagnosticosEnfermeria);
	
		mundo.setFechaRegistro(forma.getFechaRegistro());
		mundo.setHoraRegistro(forma.getHoraRegistro());
		
		
		//-----------------------Seccion Dieta -----------------------------------------------------------------------------//
		mundo.setMapaDieta(forma.getMapaDieta());
		mundo.setFinalizarTurno(forma.getFinalizarTurno());
		
		//---------------------- Seccion Signos Vitales --------------------------------------//
		mundo.setFrecuenciaCardiaca(forma.getFrecuenciaCardiaca());
		mundo.setFrecuenciaRespiratoria(forma.getFrecuenciaRespiratoria());
		mundo.setPresionArterialDiastolica(forma.getPresionArterialDiastolica());
		mundo.setPresionArterialSistolica(forma.getPresionArterialSistolica());
		mundo.setPresionArterialMedia(forma.getPresionArterialMedia());
		mundo.setTemperaturaPaciente(forma.getTemperaturaPaciente());
		mundo.setMapaSignosVitales(forma.getMapaSignosVitales());
		
		//----------------------------Se llena el mundo de los Exï¿½menes Fï¿½sicos----------------------------------------------//
		if(forma.getMapaExamenesFisicos("codExamenesFisicos") != null && forma.getMapaExamenesFisicos("codExamenesFisicosTipo") != null)
		{
			Vector codExamenesFisicos=(Vector) forma.getMapaExamenesFisicos("codExamenesFisicos");
			Vector codExamenesFisicosTipo=(Vector) forma.getMapaExamenesFisicos("codExamenesFisicosTipo");
			
			for (int i=0; i<codExamenesFisicos.size(); i++)
			{
				int examenFisicoCcIns=Integer.parseInt(codExamenesFisicos.elementAt(i)+"");
				int examenFisicoTipo=Integer.parseInt(codExamenesFisicosTipo.elementAt(i)+"");
				
				//Valor del exï¿½men fisico nuevo
				String valorExamenFisicoNuevo=(String)forma.getMapaExamenesFisicos("examenFisicoNuevo_"+examenFisicoCcIns);
				//Valor del exï¿½men fisico anterior
				String valorExamenFisico=(String)forma.getMapaExamenesFisicos("examenFisico_"+examenFisicoTipo);
				
				
				if (!valorExamenFisicoNuevo.trim().equals(""))
					{
						String examenFisicoNuevo="Fecha/Hora Registro: "+forma.getFechaRegistro() + " "+forma.getHoraRegistro()+ "\n"+valorExamenFisicoNuevo;
						forma.setMapaExamenesFisicos("examenFisico_"+examenFisicoTipo, UtilidadTexto.agregarTextoAObservacionFechaGrabacion(valorExamenFisico, examenFisicoNuevo, usuario, false));
						mundo.setMapaExamenesFisicos("examenFisico_"+examenFisicoTipo, forma.getMapaExamenesFisicos("examenFisico_"+examenFisicoTipo));
						forma.setInsertaronExamenesFisicos(true);
					}
				else 
					{
						mundo.setMapaExamenesFisicos("examenFisico_"+examenFisicoTipo, valorExamenFisico);
					}
				
			}//for
			mundo.setMapaExamenesFisicos("codExamenesFisicos", forma.getMapaExamenesFisicos("codExamenesFisicos"));
			mundo.setMapaExamenesFisicos("codExamenesFisicosTipo", forma.getMapaExamenesFisicos("codExamenesFisicosTipo"));
		}//if codigos != null
      //----------------------------------- Fin de llenar mundo exï¿½menes fï¿½sicos ------------------------------------------------//
		
		//------------------- Seccion Anotaciones de Enfermerï¿½a ----------------------------------//
		mundo.setAnotacionEnfermeria(forma.getAnotacionEnfermeria());
		
		//-----------------Secciï¿½n Cateter Sonda ------------------------------//
		mundo.setMapaCateterSonda(forma.getMapaCateterSonda());
		
		//--------------- Secciï¿½n Cuidados Especiales de Enfermerï¿½a -------------------------//
		mundo.setMapaCuidadosEspeciales(forma.getMapaCuidadosEspeciales());
		
		//------------------------- Secciï¿½n Escala Glasgow --------------------------------------//
		mundo.setMapaEscalaGlasgow(forma.getMapaEscalaGlasgow());

		mundo.setMapaMuestra(forma.getMapaMuestra());
		
	}

	/**
	 * Metodo que carga el paciente en session de acuerdo a lo seleccionado en el listado de pacientes en cama
	 * para el centro de costo, despues carga el registro de enfermerï¿½a si tiene.
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCargarPaciente(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma, HttpServletRequest request) throws SQLException 
	{
		//----------Cargar el paciente en session-------------------//
		TipoNumeroId idPaci = new TipoNumeroId();
		idPaci.setNumeroId(forma.getNumeroId());
		idPaci.setTipoId(forma.getTipoId());
		
		paciente.cargar(con, idPaci);
		
		paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		
		
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		mundo.cargarDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),  paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaInicio);
		//
		
		// Cï¿½digo necesario para registrar este paciente como Observer
		Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			paciente.setObservable(observable);
			// Si ya lo habï¿½amos aï¿½adido, la siguiente lï¿½nea no hace nada
			observable.addObserver(paciente);
		}
		//Validaciï¿½n de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
		

		request.getSession().setAttribute("pacienteActivo", paciente);
		
        ActionErrors errores = new ActionErrors();
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
		}
		
		if(errores.isEmpty())
		{
			forma.resetSecciones();
			return accionPaciente(con, mapping, usuario, paciente, forma, request, false);
		}
		else
		{
			saveErrors(request, errores);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("resultadoPacientesCentroCosto");
		}
		
	}

	/**
	 * Metodo para entregar la informacion consultada la Form  
	 * @param forma
	 * @param mundo
	 */
	private void llenarForm(RegistroEnfermeriaForm forma, RegistroEnfermeria mundo) 
	{
		//------Informaciï¿½n de la Orden Mï¿½dica--------------//
		forma.setDescripcionSoporteOrdenMedica(mundo.getDescripcionSoporteOrdenMedica());
		if(forma.getDescripcionSoporteOrdenMedica()==null)
			forma.setDescripcionSoporteOrdenMedica("");
		forma.setObservacionesOrdenMedica(mundo.getObservacionesOrdenMedica());
		forma.setDescripcionDieta(mundo.getDescripcionDieta());
		
		//------------------------------------ Hoja Neurolï¿½gica ------------------------------------------------------------------//
		forma.setExisteHojaNeurologica(mundo.getExisteHojaNeurologica());
		forma.setFinalizadaHojaNeurologica(mundo.getFinalizadaHojaNeurologica());
		forma.setFechaFinalizacionHNeurologica(mundo.getFechaFinalizacionHNeurologica());
		forma.setMedicoHNeurologica(mundo.getMedicoHNeurologica());
		forma.setFechaGrabacionHojaNeuro(mundo.getFechaGrabacionHojaNeuro());
		
	}

	/**
	 * Mï¿½todo que empieza el flujo de la funcionalidad
	 * @param con Conexiï¿½n con la BD
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param request Manejo de errores
	 * @return Menu paciente, centro de costo, o pï¿½gina de error en caso de no cumplir con las validaciones
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma, HttpServletRequest request)
	{
		//RegistroEnfermeria mundo = new RegistroEnfermeria();

		
		forma.reset();
		forma.resetBusqueda();
		if(!UtilidadValidacion.esProfesionalSalud(usuario))
		{
			this.cerrarConexion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
		}
		else
		{
			//----------Se verifica si la ocupaciï¿½n del profesional es enfermera o auxiliar de enfermera para permitir
			//----------la modificaciï¿½n o no del registro de enfermerï¿½a----------------//
			if (UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario)))
				forma.setEsConsulta(true);
			else
				forma.setEsConsulta(false);
		}
		
		/*String fechaInicio=obtenerFechasHistorico(usuario, true);
		mundo.cargarDieta(con, paciente.getCodigoCuenta(),  paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaInicio, paciente.getCodigoCuentaAsocio());*/
		//mundo.cargarDieta(con, paciente.getCodigoCuenta(),  usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaInicio, paciente.getCodigoCuentaAsocio());

		//-Se Necesita El Mundo de Orden Medica para traer el historico de dieta.  
		//---Consultar Historicos de detalle de nutricion Oral DE ORDENES MEDICAS ...   
		/*OrdenMedica mundoOrdenMedica = new OrdenMedica();  
		forma.setListadoNutOralHisto( mundoOrdenMedica.consultarNutricionOralHisto(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"")));*/

		
		this.cerrarConexion(con);
		return mapping.findForward("menu");
	}
	
	
	/**
	 * Mï¿½todo que muestra el listado de pacientes que estï¿½n en una cama del centro
	 * de costo seleccionado
	 * @param con Conexiï¿½n con la BD
	 * @param mapping
	 * @param forma
	 * @param request Manejo de errores
	 * @param institucion
	 * @param request 
	 * @return listado
	 */
	private ActionForward accionListadoPacientesCentroCosto (Connection con, ActionMapping mapping, RegistroEnfermeriaForm forma, int institucion, HttpServletRequest request)
	{
		//*****************VALIDACIONES*******************************************************************
		ActionErrors errores = new ActionErrors();
		
		//Se verifica de que se haya parametrizado al menos alguno de los  campos de busqueda
		if(forma.getCentroCostoSeleccionado()==-1&&forma.getCodigoCama().equals("")&&forma.getCodigoHabitacion().equals("")&&forma.getCodigoPiso().equals(""))
			errores.add("",new ActionMessage("error.registroEnfermeria.minimoCampos"));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("centroCosto");
		}
		//***********************************************************************************************
		
		RegistroEnfermeria mundoRegistroEnfermeria = new RegistroEnfermeria();
		
		//Se obtiene el nï¿½mero de registros por pï¿½gina que se tiene parametrizado
		forma.setMaxPageItems( ValoresPorDefecto.getMaxPageItemsInt(institucion));		
		
		forma.setMapaConsultaPacientesCentroCosto( 
				mundoRegistroEnfermeria.consultarPacientesXArea(con, forma.getCentroCostoSeleccionado(),
						forma.getCodigoPiso(),forma.getCodigoHabitacion(),forma.getCodigoCama(), forma.isPacientesNuevaInformacion()));
		
		//Se analiza el tipo de rompimiento
		if(forma.getCentroCostoSeleccionado()!=ConstantesBD.codigoNuncaValido)
			forma.setTipoRompimiento("centro_costo_");
		else
			forma.setTipoRompimiento("piso_habitacion_");
		
		forma.setConsultaXArea(true);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoPacientesCentroCosto");
	}
	
	/**
	 * Mï¿½todo que ordena el mapa que contiene el listado de pacientes en cama para
	 * el centro de costo
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private ActionForward accionOrdenarPacientesCentroCosto (RegistroEnfermeriaForm forma, ActionMapping mapping, Connection con)
	{
		
		
		String[] indices = {
				"nro_cuenta_", 
				"numero_cama_",  
				"descripcion_cama_", 
				"centro_costo_",  
				"paciente_",  
				"tipo_id_",  
				"nro_id_", 
				"edad_",  
				"sexo_",  
				"fecha_ingreso_",
				"piso_habitacion_", 
				"cod_centro_costo_",
				"alerta_"};

		int num = Integer.parseInt(forma.getMapaConsultaPacientesCentroCosto().get("numRegistros").toString());
		
		//Se pasa a la fecha tipo BD
		for(int i=0;i<num;i++)
			forma.setMapaConsultaPacientesCentroCosto("fecha_ingreso_"+i,UtilidadFecha.conversionFormatoFechaABD(forma.getMapaConsultaPacientesCentroCosto("fecha_ingreso_"+i).toString()));
		
		forma.setMapaConsultaPacientesCentroCosto(Listado.ordenarMapaRompimiento(indices,
															forma.getPatronOrdenar(),
										                    forma.getUltimoPatron(),
										                    forma.getMapaConsultaPacientesCentroCosto(),
										                    forma.getTipoRompimiento() ));
		
		
		//Se vuelve y se pasa la fecha a tipo Aplicacion
		for(int i=0;i<num;i++)
			forma.setMapaConsultaPacientesCentroCosto("fecha_ingreso_"+i,UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaConsultaPacientesCentroCosto("fecha_ingreso_"+i).toString()));
        
        forma.getMapaConsultaPacientesCentroCosto().put("numRegistros", num+"");
        forma.setEstado("");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        this.cerrarConexion(con);
        return mapping.findForward("resultadoPacientesCentroCosto");
	}

	/**
	 * Metodo para registrar informacion cuando el paciente esta cargado en sesion. 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param cargarInfoHistorica
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionPaciente(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, RegistroEnfermeriaForm forma, HttpServletRequest request, boolean cargarInfoHistorica) throws SQLException 
	{
		//logger.info("INCIO DEL METODO ACCION PACIENTE");
		//se verifica si es resumen de atenciones
		boolean esResumenAt = forma.isEsResumenAt();
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		if(forma.isEsResumenAt())
			paciente.setCodigoCuenta(Integer.parseInt(forma.getIdCuenta()));					
				
		//Realiza las validaciones para ingreso por paciente
		ActionForward forward = new ActionForward();
		forward = mundo.validacionesAccionPaciente(con, mapping, request, usuario, paciente, forma);
		if(forward != null)
			return forward;
		
		//logger.info("TERMINA VALIDACION");
		forma.reset();	
	 	forma.setEsResumenAt(esResumenAt);
	 	if(esResumenAt)
	 	{
	 		forma.setEsconderPYP(true);
	 		forma.setEsResumenAtenciones("si");
	 	}
	 	else
	 	{
	 		forma.setEsconderPYP(false);
	 		forma.setEsResumenAtenciones("no");
	 	}
	 	
	 	String cuentasXIngreso = UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+"");
	 	forma.setCodigoRegEnfer(Utilidades.obtenerCodigoRegistroEnfermeria(con, paciente.getCodigoCuenta()));			 	
	 	forma.setExamenesFisicosInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), cuentasXIngreso, 4));
	 	//Busqueda de los soportes respiratorios
	 	forma.setSoportesRespiratorioInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(),  cuentasXIngreso, 2));
	 	//---------- Se cargan los articulos (insumos) cateter sonda para la instituciï¿½n --------------------------//	 	
	 	forma.setArticulosCateterSondaIns(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(), cuentasXIngreso, 5));
	 	
	 	//----- Se consulta los tipos de cuidados especiales de enfermerï¿½a por instituciï¿½n centro de costo-------------//
	 	forma.setColCuidadosEspecialesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(),cuentasXIngreso, 7));
	 	//----Se quitan los tipos de cuidados repetidos en la colecciï¿½n------//
	 	forma.setColCuidadosEspecialesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getColCuidadosEspecialesInstitucionCcosto(), "codigo_tipo"));
	 	
	 	//Consulta la informaciï¿½n de los tipo de frecuencia 
	 	if(!forma.getColCuidadosEspecialesInstitucionCcosto().isEmpty())
	 	{
	 		//Verifica que los cuidados especiales se encuentren almacenados en frecuencias de cuidados de enfermeria
	 		RegistroEnfermeria.actualizarCodigoPkFrecCuidadosEnfer(con,paciente.getCodigoIngreso(),forma.getColCuidadosEspecialesInstitucionCcosto(),usuario.getLoginUsuario());
	 		forma.setArrayFrecuenciasCuidadoEnfer(ProgramacionCuidadoEnfer.consultarFrecuenciaCuidado(con,paciente.getCodigoIngreso()+"",ConstantesBD.codigoNuncaValido,true));
	 		forma.setArrayTipoFrecuencias(ProgramacionCuidadoEnfer.consultarTipoFrecuenciaInst(con,usuario.getCodigoInstitucionInt()));	 		 		
	 		forma.setMapaCuidadosEspeciales(RegistroEnfermeria.cargarFrecPeriodoCuidadosEnfer(forma.getMapaCuidadosEspeciales(), forma.getArrayFrecuenciasCuidadoEnfer()));
	 	}
			 	
	 	//---------Se carga el nombre del ï¿½ltimo tipo de monitoreo si el paciente estï¿½ en Cama UCI------------//	 	
	 	forma.setNombreTipoMonitoreo(Utilidades.obtenerUltimoTipoMonitoreo(con, paciente.getCodigoCuenta(), usuario.getCodigoInstitucionInt(), true));

	 	//------------------------ Se carga la informaciï¿½n guardada en la Orden Mï¿½dica para el paciente ---------------//
	 	mundo.cargarInfoOrdenMedica(con, cuentasXIngreso);
	 				 	
		
	 	forma.setResultadoLaboratorios(OrdenMedica.cargarResultadoLaboratorios(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, paciente.getCodigoArea(), true,false));
		forma.setResultadoLaboratoriosHistoricos(OrdenMedica.cargarResultadoLaboratorios(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false,true));
		
		
		//cargar los resultados laboratorios.
		forma.setValoracionEnfermeria(mundo.cargarValoracionEnfermeria(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, paciente.getCodigoArea(), true,false));
		forma.setValoracionEnfermeriaHistoricos(mundo.cargarValoracionEnfermeria(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false,true));
		
		
	 	//----------------------------------------------- SECCION HOJA NEUROLOGICA----------------- ------------------------------------//

	 	//--------Se consulta si existe o no, si estï¿½ finalizada la hoja neurolï¿½gica en la orden mï¿½dica ---------------//
	 	mundo.cargarInfoHojaNeurologica(con, cuentasXIngreso);
	 		 	
	 	//---------Se verifica si se debe cargar la informaciï¿½n histï¿½rica para la secciï¿½n de acuerdo
	 	//--------a si estï¿½ desplegada o no la secciï¿½n	 	
	 	if(cargarInfoHistorica)
	 	{
	 		//logger.info("----INICIO CARGA DATOS INFORMACION HISTORICA");
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionNanda")+""))
	 		{
	 			accionCargarSeccionDiagnosticosNanda(con, usuario, paciente, forma);
	 		}
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionSignosVitales")+""))
	 		{		 	
	 			accionCargarSeccionSignosVitales(con, usuario, paciente,forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionRespiratorio")+""))
	 		{		 	
	 			accionCargarSeccionSoporteRespiratorio(con, usuario, paciente, forma);
	 		}
			
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionDieta")+""))
	 		{		 	
	 			accionCargarSeccionDieta(con, usuario, paciente,forma);
	 		}
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionMezclas")+""))
	 		{		 	
	 			accionCargarSeccionMezclas(con, paciente,forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionControlLiquidos")+""))
	 		{		 	
	 			accionCargarSeccionControlLiquidos(con, usuario, paciente,forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionCateter")+""))
	 		{		 	
	 			accionCargarSeccionCateterSonda(con, usuario, paciente, forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionFisico")+""))
	 		{		 	
	 			accionCargarSeccionExamenesFisicos(con, usuario, paciente, forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionHojaNeurologica")+""))
	 		{		 	
	 			accionCargarSeccionHojaNeurologica(con, usuario,paciente, forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionNotas")+""))
	 		{		 	
	 			accionCargarSeccionAnotacionesEnfer(con, usuario, paciente, forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionCuidados")+""))
	 		{
	 			accionCargarSeccionCuidadosEspecialesEnfer(con, usuario, paciente, forma);
	 		}
	 		
	 		if(UtilidadTexto.getBoolean(forma.getMapaSecciones("seccionPrescripcionDialisis")+""))
	 		{
	 			accionCargarSeccionPrescripcionDialisis(con, mapping, usuario, paciente, forma);
	 			
	 		}
	 	}
	 	
	 	//-------Cargar la informacion de Toma de Muestra.
	 	HashMap mp = new HashMap();
	 	mp.put("cuentas",cuentasXIngreso);
		forma.setMapaMuestra( mundo.consultarTomaMuestras(con, mp) );
	 	
		
		/**
		 * Se consultan las alertas para las secciones del registro de enfermería según la cuenta
		 */
		forma.setFechaInicioRegistro(Utilidades.capturarFechaBD(con));
		forma.setHoraInicioRegistro(Utilidades.capturarHoraBD(con));
		forma.setListaAlertasOrdenesMed(RegistroEnfermeria.consultarAlertaOrdenMedicaCuenta(con, paciente.getCodigoCuenta()));
		
		
		//-Pasar la informacion anteriormente consultada
	 	llenarForm(forma, mundo);		
				
		//*****************************************************************************************
		//Valida la muestra del boton de cerrar	
		ResultadoBoolean resultCierra = mundo.validacionesBotonCierrreNota(con, paciente);
		
		//Valida si las notas de enfermeria se encuentran cerrradas
		InfoDatosInt finalizadaInfo = mundo.existeRegistroEnfermeria(con,paciente.getCodigoCuenta());
		forma.setValidacionesCierreAperturaNotasMap("esCerradaNota",finalizadaInfo.getDescripcion());		
		forma.setValidacionesCierreAperturaNotasMap("codigoCierreNota",finalizadaInfo.getCodigo());		
		
		forma.setValidacionesCierreAperturaNotasMap("esModCampoAnotaciones",ConstantesBD.acronimoSi);
		if(finalizadaInfo.getDescripcion().equals(ConstantesBD.acronimoSi))
		{
			forma.setValidacionesCierreAperturaNotasMap("esModCampoAnotaciones",mundo.validarModificacionCerradaNota(con, paciente).isTrue()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);

			//la informaciï¿½n es de solo consulta
			forma.setEsConsulta(true);
		}		
		else
		{
			//Verificacion si es resumen de atenciones
		 	if (UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario)))
				forma.setEsConsulta(true);
			else
				forma.setEsConsulta(false);
		 	
			if(forma.isEsResumenAt())
				forma.setEsConsulta(true);		
		}
		
		if(resultCierra.isTrue() && finalizadaInfo.getDescripcion().equals(ConstantesBD.acronimoNo))
			forma.setValidacionesCierreAperturaNotasMap("mostrarBtnCierre",ConstantesBD.acronimoSi);
		else			
		{
			forma.setValidacionesCierreAperturaNotasMap("mostrarBtnCierre",ConstantesBD.acronimoNo);			
			logger.info("\n\n no se muestra boton cerrar >> "+resultCierra.getDescripcion()+" >> valor del nota_finalizada >> "+finalizadaInfo.getDescripcion());
		}
		
		//*****************************************************************************************		
		
		//Valida la muestra del boton abrir		
		ResultadoBoolean resulApertura = mundo.validacionesBotonAperturaNota(con, paciente);
		forma.setValidacionesCierreAperturaNotasMap("mostrarBtnApertura",ConstantesBD.acronimoNo);
		
		if(resulApertura.isTrue() && finalizadaInfo.getDescripcion().equals(ConstantesBD.acronimoSi))		
			forma.setValidacionesCierreAperturaNotasMap("mostrarBtnApertura",ConstantesBD.acronimoSi);		
		else
			logger.info("\n\nno se muestra boton abrir >> "+resulApertura.isTrue()+" >> "+resulApertura.getDescripcion()+" >> valor del nota_finalizada >> "+finalizadaInfo.getDescripcion());		

		Utilidades.imprimirMapa(forma.getValidacionesCierreAperturaNotasMap());		
		//******************************************************************************************
		//Validacion de prescripciï¿½n dialisis
		forma.setDeboAbrirPrescripcionDialisis(UtilidadesOrdenesMedicas.deboAbrirPrescripcionDialisis(con, paciente.getCodigoArea(), usuario.getCodigoInstitucionInt()));
		//*******************************************************************************************		
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");  
	}
	
	
	
	/**
	 * metodo WILSON
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param forma
	 */
	private void accionCargarSeccionCuidadosEspecialesEnfer(
			Connection con,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			RegistroEnfermeriaForm forma)
	{
		
		RegistroEnfermeria mundo= new RegistroEnfermeria();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
	 	forma.setMapaColsCuidadosEspeciales(mundo.consultarColsCuidadosEspeciales(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, ""));
	 	forma.setMapaCuidadosEspeciales(mundo.consultarCuidadosEnfermeria(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),fechaInicio, ""));
	 	
	 	//Consulta la informaciï¿½n de los tipo de frecuencia 
	 	if(!forma.getColCuidadosEspecialesInstitucionCcosto().isEmpty())
	 	{
	 		forma.setMapaCuidadosEspeciales(RegistroEnfermeria.cargarFrecPeriodoCuidadosEnfer(forma.getMapaCuidadosEspeciales(), forma.getArrayFrecuenciasCuidadoEnfer()));
	 	}
	 	
	 	logger.info("valor del mapa >> "+forma.getMapaColsCuidadosEspeciales());
	}
	
	/**
	 * Carga la informacion de la seccion cuidados de enfermeria de forma independiente
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param RegistroEnfermeriaForm forma
	 * @param RegistroEnfermeria mundo
	 * */
	private ActionForward accionCargarSeccionCuidadosEspecialesEnferIndependiente(
																				Connection con,
																				UsuarioBasico usuario,
																				PersonaBasica paciente,
																				ActionMapping mapping,
																				HttpServletRequest request,
																				RegistroEnfermeriaForm forma)
	{
		
		//Realiza las validaciones para ingreso por paciente
		ActionForward forward = new ActionForward();
		RegistroEnfermeria mundo  = new RegistroEnfermeria();
		forward = mundo.validacionesAccionPaciente(con, mapping, request, usuario, paciente, forma);
		if(forward != null)
			return forward;
		
		forma.reset();	
		forma.resetBusqueda();
		//----- Se consulta los tipos de cuidados especiales de enfermerï¿½a por instituciï¿½n centro de costo-------------//
	 	forma.setColCuidadosEspecialesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, usuario.getCodigoInstitucionInt(),UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), 7));
	 	//----Se quitan los tipos de cuidados repetidos en la colecciï¿½n------//
	 	forma.setColCuidadosEspecialesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getColCuidadosEspecialesInstitucionCcosto(), "codigo_tipo"));
	 	
	 	//Consulta la informaciï¿½n de los tipo de frecuencia 
	 	if(!forma.getColCuidadosEspecialesInstitucionCcosto().isEmpty())
	 	{
	 		//Verifica que los cuidados especiales se encuentren almacenados en frecuencias de cuidados de enfermeria
	 		RegistroEnfermeria.actualizarCodigoPkFrecCuidadosEnfer(con,paciente.getCodigoIngreso(),forma.getColCuidadosEspecialesInstitucionCcosto(),usuario.getLoginUsuario());
	 		forma.setArrayFrecuenciasCuidadoEnfer(ProgramacionCuidadoEnfer.consultarFrecuenciaCuidado(con,paciente.getCodigoIngreso()+"",ConstantesBD.codigoNuncaValido,true));
	 		forma.setArrayTipoFrecuencias(ProgramacionCuidadoEnfer.consultarTipoFrecuenciaInst(con,usuario.getCodigoInstitucionInt()));	 		 		
	 		forma.setMapaCuidadosEspeciales(RegistroEnfermeria.cargarFrecPeriodoCuidadosEnfer(forma.getMapaCuidadosEspeciales(), forma.getArrayFrecuenciasCuidadoEnfer()));
	 	}	
	 	
	 	accionCargarSeccionCuidadosEspecialesEnfer(con, usuario, paciente, forma);
	 	UtilidadBD.closeConnection(con);
	 	return mapping.findForward("seccionCuidadosEnferIndep");
	}

	/**
	 * Mï¿½todo para organizar en el mapa todos los histï¿½ricos de soporte respiratorio
	 * @param con Conexiï¿½n con la BD
	 * @param forma
	 * @param mundo
	 * @param fechaInicio
	 * @param codigoCuenta Cuenta del paciente
	 * @param codigoCuentaAsocio @todo
	 */
	private void llenarHistoricoSoporteRespiratorio(Connection con, RegistroEnfermeriaForm forma, RegistroEnfermeria mundo, String fechaInicio, String cuentas)
	{
		HashMap orden=mundo.consultarSoporteOrden(con, cuentas, fechaInicio, false, null);
		HashMap historicoSoporte=mundo.consultarHistoricoSoporteEnfer(con, cuentas, fechaInicio, null);
	 	HashMap nuevoMapaSoporte=new HashMap();
	 	Collection tipos=forma.getSoportesRespiratorioInstitucionCcosto();
	 	
	 	String fechaGrabacionHistorico=(String)historicoSoporte.get("fecha_grabacion_0");
	 	String fechaGrabacionOrden=(String)orden.get("fecha_grabacion_0");
	 	
	 	if(fechaGrabacionOrden!=null && fechaGrabacionHistorico!=null && fechaGrabacionHistorico.compareTo(fechaGrabacionOrden)<0)
	 	{
	 		orden=mundo.consultarSoporteOrden(con, cuentas, fechaGrabacionHistorico, true, null);
	 	}
	 	int numRegOrden=Integer.parseInt(orden.get("numRegistros")+"");
	 	
	 	//no se estï¿½ usuando la variable
	 	//int numeroHisoricosSoporte=Integer.parseInt(historicoSoporte.get("numRegistros")+"");
	 	
	 	/*
	 	 * Aquï¿½ me aseguro que se muestren todos los registros
	 	 * asï¿½ no exista un registro en la orden mï¿½dica
	 	 * Ningï¿½n equipo debe tener el codigo 0
	 	 * Eso va desde la parametrizaciï¿½n
	 	 */
 		orden.put("fecha_orden_-1", "01/01/0001 - 00:00");
 		orden.put("codigo_equipo_-1", 0);
 		orden.put("codigo_encabezado_-1", "0");
 		orden.put("cantidad_-1", "0");
 		orden.put("fecha_grabacion_-1", "0001-01-01-00:00");
 		orden.put("oxigeno_terapia_-1", false);
 		orden.put("equipo_-1", "");
 		orden.put("medico_-1", "");
 		orden.put("no_mostrar_-1", true);
 		
 		int numRegSop=Integer.parseInt(historicoSoporte.get("numRegistros")+"");
 		if(numRegOrden>0 && numRegSop==0)
 		{
 			orden.put("registrosHistoricos", (numRegSop+1));
 		}
 		else
 		{
 			orden.put("registrosHistoricos", numRegSop);
 		}

 		for(int i=-1; i<numRegOrden;i++)
	 	{
 			String fechaGrabacionSigOrden=(String)orden.get("fecha_grabacion_"+(i+1));
	 		fechaGrabacionOrden=(String)orden.get("fecha_grabacion_"+i);
	 		//int numRegSop=Integer.parseInt(historicoSoporte.get("numRegistros")+"");
	 		int codigoEquipo=Integer.parseInt(orden.get("codigo_equipo_"+i)+"");
	 		int contadorRegistros=0;
	 		int codigoEncabezadoAnterior=0;
	 		for(int j=0; j<numRegSop; j++)
	 		{
	 			String fechaRegistro=historicoSoporte.get("fecha_grabacion_"+j)+"";
	 			if(fechaRegistro.compareTo(fechaGrabacionOrden)>=0 && (fechaGrabacionSigOrden==null || fechaRegistro.compareTo(fechaGrabacionSigOrden)<0))
	 			{
		 			int numTipos=tipos.size();
		 			int codigoEncabezado=Integer.parseInt(historicoSoporte.get("codigo_encabezado_"+j)+"");
		 			if(codigoEncabezado!=codigoEncabezadoAnterior)
		 			{
		 				codigoEncabezadoAnterior=codigoEncabezado;
		 				contadorRegistros++;
		 			}
		 			int indice=contadorRegistros-1;
		 			nuevoMapaSoporte.put("hora_"+codigoEquipo+"_"+indice, historicoSoporte.get("fecha_registro_"+j));
		 			nuevoMapaSoporte.put("horag_"+codigoEquipo+"_"+indice, historicoSoporte.get("fecha_insercion_"+j));
		 			
	 				Iterator iterador=tipos.iterator();
		 			for(int k=0; k<numTipos; k++)
		 			{
		 				HashMap fila=(HashMap)iterador.next();
		 				int codigoSoporteParam=Integer.parseInt(fila.get("codigo_tipo")+"");
		 				int codigoSoporteHist=Integer.parseInt(historicoSoporte.get("tipo_soporte_"+j)+"");
		 				if(codigoSoporteParam==codigoSoporteHist)
		 				{
		 					nuevoMapaSoporte.put("codSop_"+k+"_"+codigoEquipo+"_"+indice, codigoSoporteHist+"");
		 					nuevoMapaSoporte.put("valSop_"+k+"_"+codigoEquipo+"_"+indice, historicoSoporte.get("valor_"+j)+"");
		 					nuevoMapaSoporte.put("tipSop_"+k+"_"+codigoEquipo+"_"+indice, fila.get("tipo_dato")+"");
		 					break;
		 				}
		 			}
		 			nuevoMapaSoporte.put("observaciones_"+codigoEquipo+"_"+indice, (String)historicoSoporte.get("observaciones_"+j));
		 			nuevoMapaSoporte.put("medico_"+codigoEquipo+"_"+indice, historicoSoporte.get("medico_"+j)+"");
		 			nuevoMapaSoporte.put("profesional_salud_"+codigoEquipo+"_"+indice, historicoSoporte.get("profesional_salud_"+j)+"");
	 			}
	 		}
		 	nuevoMapaSoporte.put("numRegistros_"+codigoEquipo, contadorRegistros+"");
	 	}
 		
	 	forma.setSoporteRespiratorio(nuevoMapaSoporte);
	 	forma.setSoporteRespiratorioHistorico(orden);
	}

	/**
	 * Mï¿½todo para organizar en el mapa todos los histï¿½ricos de soporte respiratorio
	 * vistos desde el link de ver anteriors
	 * @param con Conexiï¿½n con la BD
	 * @param forma
	 * @param mundo
	 * @param fechaInicio
	 * @param codigoCuenta Cuenta del paciente
	 * @param mapping hacer forward
	 * @param cuentaAsocio @todo
	 */
	private ActionForward accionConsultarHistoSoporte(Connection con, RegistroEnfermeriaForm forma, RegistroEnfermeria mundo, String fechaInicio, String cuentas, ActionMapping mapping, int institucion)
	{
		String fechaFin=UtilidadFecha.incrementarDiasAFecha(fechaInicio, 1, true)+"-"+ValoresPorDefecto.getHoraInicioPrimerTurno(institucion);
		HashMap orden=mundo.consultarSoporteOrden(con, cuentas, fechaInicio, false, fechaFin);
		HashMap historicoSoporte=mundo.consultarHistoricoSoporteEnfer(con, cuentas, fechaInicio, fechaFin);
	 	HashMap nuevoMapaSoporte=new HashMap();
	 	Collection tipos=forma.getSoportesRespiratorioInstitucionCcosto();
	 	
	 	String fechaGrabacionHistorico=(String)historicoSoporte.get("fecha_grabacion_0");
	 	String fechaGrabacionOrden=(String)orden.get("fecha_grabacion_0");
	 	
	 	if(fechaGrabacionOrden!=null && fechaGrabacionHistorico!=null && fechaGrabacionHistorico.compareTo(fechaGrabacionOrden)<0)
	 	{
	 		orden=mundo.consultarSoporteOrden(con, cuentas, fechaGrabacionHistorico, true, fechaFin);
	 	}
	 	int numRegOrden=Integer.parseInt(orden.get("numRegistros")+"");
	 	//no se estï¿½ usuando la variable
	 	//int numeroHisoricosSoporte=Integer.parseInt(historicoSoporte.get("numRegistros")+"");
	 	
	 	/*
	 	 * Aquï¿½ me aseguro que se muestren todos los registros
	 	 * asï¿½ no exista un registro en la orden mï¿½dica
	 	 * Ningï¿½n equipo debe tener el codigo 0
	 	 * Eso va desde la parametrizaciï¿½n
	 	 */
 		orden.put("fecha_orden_-1", "01/01/0001 - 00:00");
 		orden.put("codigo_equipo_-1", 0);
 		orden.put("codigo_encabezado_-1", "0");
 		orden.put("cantidad_-1", "0");
 		orden.put("fecha_grabacion_-1", "0001-01-01-00:00");
 		orden.put("oxigeno_terapia_-1", false);
 		orden.put("equipo_-1", "");
 		orden.put("medico_-1", "");
 		orden.put("no_mostrar_-1", true);
 		//orden.put("numRegistros", (numRegOrden+1)+"");

 		for(int i=-1; i<numRegOrden;i++)
	 	{
	 		String fechaGrabacionSigOrden=(String)orden.get("fecha_grabacion_"+(i+1));
	 		fechaGrabacionOrden=(String)orden.get("fecha_grabacion_"+i);
	 		int numRegSop=Integer.parseInt(historicoSoporte.get("numRegistros")+"");
	 		int codigoEquipo=Integer.parseInt(orden.get("codigo_equipo_"+i)+"");
	 		int contadorRegistros=0;
	 		int codigoEncabezadoAnterior=0;
	 		for(int j=0; j<numRegSop; j++)
	 		{
	 			String fechaRegistro=historicoSoporte.get("fecha_grabacion_"+j)+"";
	 			if(fechaRegistro.compareTo(fechaGrabacionOrden)>=0 && (fechaGrabacionSigOrden==null || fechaRegistro.compareTo(fechaGrabacionSigOrden)<0))
	 			{
		 			int numTipos=tipos.size();
		 			int codigoEncabezado=Integer.parseInt(historicoSoporte.get("codigo_encabezado_"+j)+"");
		 			if(codigoEncabezado!=codigoEncabezadoAnterior)
		 			{
		 				codigoEncabezadoAnterior=codigoEncabezado;
		 				contadorRegistros++;
		 			}
		 			int indice=contadorRegistros-1;
		 			nuevoMapaSoporte.put("hora_"+codigoEquipo+"_"+indice, historicoSoporte.get("fecha_registro_"+j));
		 			nuevoMapaSoporte.put("horag_"+codigoEquipo+"_"+indice, historicoSoporte.get("fecha_insercion_"+j));
		 			
		 			Iterator iterador=tipos.iterator();
		 			for(int k=0; k<numTipos; k++)
		 			{
		 				HashMap fila=(HashMap)iterador.next();
		 				int codigoSoporteParam=Integer.parseInt(fila.get("codigo_tipo")+"");
		 				int codigoSoporteHist=Integer.parseInt(historicoSoporte.get("tipo_soporte_"+j)+"");
		 				if(codigoSoporteParam==codigoSoporteHist)
		 				{
		 					nuevoMapaSoporte.put("codSop_"+k+"_"+codigoEquipo+"_"+indice, codigoSoporteHist+"");
		 					nuevoMapaSoporte.put("valSop_"+k+"_"+codigoEquipo+"_"+indice, historicoSoporte.get("valor_"+j)+"");
		 					nuevoMapaSoporte.put("tipSop_"+k+"_"+codigoEquipo+"_"+indice, fila.get("tipo_dato")+"");
		 					break;
		 				}
		 			}
		 			nuevoMapaSoporte.put("observaciones_"+codigoEquipo+"_"+indice, (String)historicoSoporte.get("observaciones_"+j));
		 			nuevoMapaSoporte.put("medico_"+codigoEquipo+"_"+indice, historicoSoporte.get("medico_"+j)+"");
		 			nuevoMapaSoporte.put("profesional_salud_"+codigoEquipo+"_"+indice, historicoSoporte.get("profesional_salud_"+j)+"");
	 			}
	 		}
		 	nuevoMapaSoporte.put("numRegistros_"+codigoEquipo, contadorRegistros+"");
	 	}
	 	forma.setAnterioresSoporteRespiratorio(nuevoMapaSoporte);
	 	forma.setAnterioresSoporteRespiratorioOrden(orden);
	 	this.cerrarConexion(con);
	 	return mapping.findForward("historicoSoporte");
	}

	/**
	 * Mï¿½todo para el manejo de diagnosticos de enfermerï¿½a
	 * (Historicos y postulaciones)
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void manejoDiagnosticosEnfermeria(Connection con, RegistroEnfermeria mundo, RegistroEnfermeriaForm forma, UsuarioBasico usuario, PersonaBasica paciente)
	{
		forma.resetNanda();
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		HashMap historico=mundo.consultarDiagnosticosNanda(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), fechaInicio, null);
		forma.setDiagnosticosNandaHistorico(historico);
		int numeroHistoricos=Integer.parseInt(historico.get("numRegistros")+"");
		
		String fechaHora="";
		int i=0;
		boolean esPrimero=true;
		// Con esto me doy cuenta cual es el primero del ï¿½ltimo juego de diagnï¿½sticos
		// para saber desde donde empiezo a postular
		for(i=numeroHistoricos-1; i>0;i--)
		{
			String fechaHoraRegistro=historico.get("fecha_registro_"+i)+"-"+historico.get("hora_grabacion_"+i);
			if(!fechaHoraRegistro.equals(fechaHora) && !esPrimero)
			{
				break;
			}
			if(esPrimero)
			{
				esPrimero=false;
			}
			fechaHora=fechaHoraRegistro;
		}
		int contadorPostulados=0;
		HashMap diagPostulados=new HashMap();

		String codigosInsertados="";
		if(i<0)
		{
			i=0;
		}
		if(i>0)
		{
			i++;
		}
		/* Debido a la tarea de XPlanner oid=19913
		* se decidiï¿½ con Margarita no postular los diagnï¿½sticos
		*
		for(;i<numeroHistoricos;i++)
		{
			String codigo=historico.get("codigo_"+i)+"";
			diagPostulados.put("codigo_"+contadorPostulados,codigo);
			diagPostulados.put("nombre_"+contadorPostulados,historico.get("codigo_nanda_"+i)+" - "+historico.get("nombre_nanda_"+i));
			contadorPostulados++;
			if(codigosInsertados.equals(""))
			{
				codigosInsertados=codigo;
			}
			else
			{
				codigosInsertados+=","+codigo;
			}
		}*/
		diagPostulados.put("numRegistros",contadorPostulados+"");
		diagPostulados.put("codigosInsertados",codigosInsertados+"");
		
		forma.setDiagnosticosNanda(diagPostulados);
	}

	/**
	 * Mï¿½todo que verifica si ingresaron datos en la secciï¿½n signos vitales
	 * @param forma
	 * @return 0 -> Si no ingresaron datos en los signos vitales
	 * 					 1 -> Si ingresaron datos en los signos vitales fijos
	 * 					 2 -> Si ingresaron datos en los signos vitales parametrizados
	 * 				 	 3 -> Si ingresaron datos en los signos vitales fijos y parametrizados
	 */
	public int verificarInsertaronDatosSignoVital(RegistroEnfermeriaForm forma)
	{
		int cont=0;
		Vector codSignosVitales =(Vector) forma.getMapaSignosVitales("codSignosVitales");
		
		if(UtilidadCadena.noEsVacio(forma.getFrecuenciaCardiaca()) || UtilidadCadena.noEsVacio(forma.getFrecuenciaRespiratoria()) 
			|| UtilidadCadena.noEsVacio(forma.getPresionArterialDiastolica()) || UtilidadCadena.noEsVacio(forma.getPresionArterialSistolica()) 
			|| UtilidadCadena.noEsVacio(forma.getPresionArterialMedia()) || UtilidadCadena.noEsVacio(forma.getTemperaturaPaciente()))
		{
			cont+=1;
		}
		
		if(codSignosVitales != null)
		{
			for(int i=0; i<codSignosVitales.size();i++)
			{
				//------El codigo del tipo de cuidado de enfermeria 
				int tipoSignoVital = Integer.parseInt(codSignosVitales.elementAt(i)+"");
				
				//-----Valor del signo vital-------------//
				String valor=(String)forma.getMapaSignosVitales("valor_"+tipoSignoVital);
				
				//----Verificar si se ingresï¿½n informaciï¿½n en el signo vital parametrizado
				if (UtilidadCadena.noEsVacio(valor))
				{
					cont+=2;
					break;
				}	
				
			}//for
		}//if
		
		return cont;
	}
	
	/**
	 * Mï¿½todo para consultar los histï¿½ricos de los signos vitales en una fecha dada
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * 
	 */
	private ActionForward accionConsultarHistoSignoVital(Connection con, ActionMapping mapping,PersonaBasica paciente,  UsuarioBasico usuario, RegistroEnfermeriaForm forma)
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		
		String fechaInicio="";
		String fechaFin="";
		String horaPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
		fechaInicio=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVerAnterioresSVital())+"-"+horaPrimerTurno;
		
		fechaFin=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(forma.getFechaVerAnterioresSVital(), 1, false))+"-"+horaPrimerTurno;
		
		
		forma.setSignosVitalesFijosFechaAnt(mundo.consultarSignosVitalesFijosHisto(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), fechaInicio, fechaFin));
		forma.setSignosVitalesParamFechaAnt(mundo.consultarSignosVitalesParamHisto(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(),  fechaInicio, fechaFin ));
		forma.setSignosVitalesTodosFechaAnt(mundo.consultarSignosVitalesHistoTodos(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), usuario.getCodigoInstitucionInt(), fechaInicio, fechaFin));
		
		this.cerrarConexion(con);
		return mapping.findForward("signosVitalesAnteriores");
		
	}
	
	/**
	 * Mï¿½todo que realiza la mezcla de la consulta de los histï¿½ricos de los cateter sonda fijos
	 * y parametrizados, y los llena en un HashMap para despues mezclarlos
	 * @param forma
	 * @return HashMap
	 */
	private HashMap formarHistoricoCateterSonda(RegistroEnfermeriaForm forma)
	{
		//-------------- Se guarda en una matriz los cateteres sonda fijos -----------------------------//
		Vector[] matrizCateteresFijos = new Vector[forma.getCateterSondaFijosHisto().size()];
		if(forma.getCateterSondaFijosHisto().size() > 0)
	       {
		       Iterator iterador1=forma.getCateterSondaFijosHisto().iterator();
		       for (int numFila=0; numFila<forma.getCateterSondaFijosHisto().size(); numFila++)
		       {
		    	   HashMap fila1=(HashMap)iterador1.next();
			       	matrizCateteresFijos[numFila]=new Vector();
			       	matrizCateteresFijos[numFila].add(fila1.get("cateter_sonda_reg_enfer"));
			       	matrizCateteresFijos[numFila].add(fila1.get("via_insercion"));
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("fecha_insercion")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("fecha_insercion"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			    	if (UtilidadCadena.noEsVacio(fila1.get("hora_insercion")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("hora_insercion"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("fecha_retiro")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("fecha_retiro"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			       	if (UtilidadCadena.noEsVacio(fila1.get("hora_retiro")+""))
			       		matrizCateteresFijos[numFila].add(fila1.get("hora_retiro"));
			       	else
			       		matrizCateteresFijos[numFila].add("");
			       	
			     			       	
			       	matrizCateteresFijos[numFila].add(fila1.get("curaciones"));
			       	matrizCateteresFijos[numFila].add(fila1.get("observaciones"));
			       	
			       	matrizCateteresFijos[numFila].add(fila1.get("fecha_grabacion"));
			       	matrizCateteresFijos[numFila].add(fila1.get("hora_grabacion"));
			       	matrizCateteresFijos[numFila].add(fila1.get("fecha_registro"));
			       	matrizCateteresFijos[numFila].add(fila1.get("hora_registro"));
			       	matrizCateteresFijos[numFila].add(fila1.get("nombre_usuario"));
		       }
	       }
	
		//-------------- Se guarda en una matriz los cateteres sonda parametrizados -----------------------------//
		Vector[] matrizCateteresParam = new Vector[forma.getCateterSondaParamHisto().size()];
		if(forma.getCateterSondaParamHisto().size() > 0)
	       {
		       Iterator iterador2=forma.getCateterSondaParamHisto().iterator();
		       for (int numFila2=0; numFila2<forma.getCateterSondaParamHisto().size(); numFila2++)
		       {
		    	   HashMap fila2=(HashMap)iterador2.next();
			       	matrizCateteresParam[numFila2]=new Vector();
			       	matrizCateteresParam[numFila2].add(fila2.get("cateter_sonda_reg_enfer"));
			       	matrizCateteresParam[numFila2].add(fila2.get("codigo_tipo"));
			       	matrizCateteresParam[numFila2].add(fila2.get("col_cateter_sonda_cc_ins"));
			       	matrizCateteresParam[numFila2].add(fila2.get("valor"));
		       }
	       }
		
		//--------------- Se recorre el hisorico de cateteres fijos y parametrizados y se va guardando en el hashMap
		HashMap nuevoMapaCateterHisto=new HashMap();
		if(forma.getCateterSondaHistoTodos().size() > 0)
	       {
		       Iterator iterador3=forma.getCateterSondaHistoTodos().iterator();
		       
		       for (int numFila3=0; numFila3<forma.getCateterSondaHistoTodos().size(); numFila3++)
		       {
		    	   HashMap fila3=(HashMap)iterador3.next();
			    	
			    	nuevoMapaCateterHisto.put("cateterSondaRegEnfer_"+numFila3, fila3.get("cateter_sonda_reg_enfer"));
			    	nuevoMapaCateterHisto.put("nombreArticulo_"+numFila3, fila3.get("nombre_articulo"));
			    	nuevoMapaCateterHisto.put("codigoArticuloCcIns_"+numFila3, fila3.get("codigo_articulo_cc_ins"));
			    	for(int fil1=0; fil1<matrizCateteresFijos.length; fil1++)
						{
						if(matrizCateteresFijos[fil1].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")))
							{
								nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(1)+"");
								nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(2)+"");
								nuevoMapaCateterHisto.put("horaInsercion_"+numFila3,matrizCateteresFijos[fil1].elementAt(3)+"");
								nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(4)+"");
								nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(5)+"");
								nuevoMapaCateterHisto.put("curaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(6)+"");
								nuevoMapaCateterHisto.put("observaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(7)+"");
								nuevoMapaCateterHisto.put("fecha_grabacion_"+numFila3,matrizCateteresFijos[fil1].elementAt(8)+"");
								nuevoMapaCateterHisto.put("hora_grabacion_"+numFila3,matrizCateteresFijos[fil1].elementAt(9)+"");
								nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(10)+"");
								nuevoMapaCateterHisto.put("hora_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(11)+"");
								nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,matrizCateteresFijos[fil1].elementAt(12)+"");
								break;
							}
						//--------Si llegï¿½ hasta aquï¿½ entonces no tiene valor
						if(fil1==matrizCateteresFijos.length-1)
							{
							nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("horaInsercion_"+numFila3, "");
							nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,"");
							nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,"");
							nuevoMapaCateterHisto.put("curaciones_"+numFila3,"");
							nuevoMapaCateterHisto.put("observaciones_"+numFila3,"");
							nuevoMapaCateterHisto.put("fecha_grabacion_"+numFila3,"");
							nuevoMapaCateterHisto.put("hora_grabacion_"+numFila3,"");
							nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,"");
							nuevoMapaCateterHisto.put("hora_registro_"+numFila3,"");
							nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,"");
							}
						}//for
			    	
			    	//----------- Se agregan los valores histï¿½ricos de los cateteres sonda paremetrizados -----------------// 
			    	if(forma.getColCateteresSondaInstitucionCcosto().size() > 0)
				       {
					       Iterator iterador4=forma.getColCateteresSondaInstitucionCcosto().iterator();
					       
					       for (int numFila4=0; numFila4<forma.getColCateteresSondaInstitucionCcosto().size(); numFila4++)
					       {
					    	   HashMap fila4=(HashMap)iterador4.next();
						       	for(int fil2=0; fil2<matrizCateteresParam.length; fil2++)
									{
										if(matrizCateteresParam[fil2].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")) && matrizCateteresParam[fil2].elementAt(1).equals(fila4.get("codigo_tipo")))
											{
												nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, matrizCateteresParam[fil2].elementAt(3)+"");
												break;
											}
										//--------Si llegï¿½ hasta aquï¿½ entonces no tiene valor
										if(fil2==matrizCateteresParam.length-1)
											{
												nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, "");
											}
									}//for
					       }//for
				       } //if  
		       }//for numFila3
		       
		       nuevoMapaCateterHisto.put("numRegistros", new Integer(forma.getCateterSondaHistoTodos().size()));
	       }  //if 
		else
		{
			nuevoMapaCateterHisto.put("numRegistros", new Integer(0));
		}
		
		return nuevoMapaCateterHisto;
	}
	
	/**
	 * Mï¿½todo que ordena el mapa que contiene el histï¿½rico de cateter sonda
	 * el centro de costo
	 * @param forma
	 * @param mapa
	 * @return
	 */
	
	private boolean ordenarHistoricoCateterSonda (RegistroEnfermeriaForm forma, HashMap mapa)
	{
		int indicesFijos=15;
		int numIndices=forma.getColCateteresSondaInstitucionCcosto().size()+indicesFijos;
		
		//------- Se construye el vector de strings con los indices del mapa para poder ordenarlo ----//
		String[] indices=new String[numIndices];
		
		indices[0]="cateterSondaRegEnfer_";
		indices[1]="nombreArticulo_";
		indices[2]="codigoArticuloCcIns_";
		indices[3]="viaInsercion_";
		indices[4]="fechaInsercion_";
		indices[5]="horaInsercion_";
		indices[6]="fechaRetiro_";
		indices[7]="horaRetiro_";
		indices[8]="curaciones_";
		indices[9]="observaciones_";
		indices[10]="fecha_grabacion_";
		indices[11]="hora_grabacion_";
		indices[12]="fecha_registro_";
		indices[13]="hora_registro_";
		indices[14]="nombre_usuario_";
		
		if(forma.getColCateteresSondaInstitucionCcosto().size() > 0)
	       {
		       Iterator iterador1=forma.getColCateteresSondaInstitucionCcosto().iterator();
		       for (int numFila=0; numFila<forma.getColCateteresSondaInstitucionCcosto().size(); numFila++)
		       {
		    	   HashMap fila1=(HashMap)iterador1.next();
			       	indices[indicesFijos]="valorParam_"+fila1.get("codigo_tipo")+"_";
			       	indicesFijos++;
		       }
	       }
		
		//-----Numero de registros del mapa------------//
		int num = forma.getCateterSondaHistoTodos().size();
		
		
		forma.setMapaHistoricoCateterSonda(Listado.ordenarMapa(indices,
															"fechaInsercion_",
										                    "",
										                    mapa,
										                    num));
        
        forma.getMapaHistoricoCateterSonda().put("numRegistros", new Integer(num));
        
		
		return true;
	}
	
	/**
	 * Mï¿½todo que verifica si ingresaron datos en la secciï¿½n cateter sonda.
	 * @param forma
	 * @return 0 --> Si no ingresaron ningï¿½n dato en la secciï¿½n cateter sonda
	 * 					 1 --> Si sï¿½lo ingresaron algï¿½n dato en el histï¿½rico de cateters sonda
	 * 					 2 --> Si sï¿½lo ingresaron algï¿½n dato en los nuevos cateter sonda
	 * 					 3 --> Si ingresaron datos en el histï¿½rico y nuevos cateter sonda 
	 */
	public int verificarDatosCateterSonda (RegistroEnfermeriaForm forma)
	{
		int cont=0;
		if (forma.getMapaCateterSonda("codigosCateterSonda") != null)
		{
			Vector codigosCateterSonda=(Vector) forma.getMapaCateterSonda("codigosCateterSonda");
			for (int c=0; c<codigosCateterSonda.size(); c++)
			{
				//int catSondaRegEnfer=Integer.parseInt(forma.getMapaCateterSonda("cateterSondaRegEnfer_"+codigosCateterSonda.elementAt(c))+"");
				String curacionesNueva=(String)forma.getMapaCateterSonda("curacionesNueva_"+codigosCateterSonda.elementAt(c));
				String observacionesNueva=(String)forma.getMapaCateterSonda("observacionesNueva_"+codigosCateterSonda.elementAt(c));
				String fechaInsercion=(String)forma.getMapaCateterSonda("fechaInsercion_"+codigosCateterSonda.elementAt(c));
				String fechaRetiro=(String)forma.getMapaCateterSonda("fechaRetiro_"+codigosCateterSonda.elementAt(c));
				
				//---------Se verifica si insertaron un nuevo dato en las curaciones de un cateter sonda histï¿½rico--------//
				if (UtilidadCadena.noEsVacio(curacionesNueva) || UtilidadCadena.noEsVacio(observacionesNueva) || UtilidadCadena.noEsVacio(fechaInsercion) || UtilidadCadena.noEsVacio(fechaRetiro))
				{
					cont=1;
					break;
				}
				
			}//for 
		}//if codigosCateterSonda!=null
		
		if (forma.getMapaCateterSonda("codsNuevosCateterSonda") != null)
		{
			String codNuevosCateter=forma.getMapaCateterSonda("codsNuevosCateterSonda")+"";
			String[] vecNuevosCateteres=codNuevosCateter.split("-");
		
			a:
			for (int i=0; i<vecNuevosCateteres.length; i++)
			{
				//-------- Codigo de la fila del nuevo artï¿½culo-------------//
				int codFilaArticuloNuevo=Integer.parseInt(vecNuevosCateteres[i]);
				
				//Valor del tipo de articulo seleccionado
				int tipoArticulo = Integer.parseInt(forma.getMapaCateterSonda("tipoCateterSonda_"+codFilaArticuloNuevo)+"");
				
				//-----------Si el tipo de articulo es != Seleccione -------------//
				if (tipoArticulo != -1)
				{
					String viaInsercion = forma.getMapaCateterSonda("viaInsercion_"+codFilaArticuloNuevo)+"";
					String fechaInsercion = forma.getMapaCateterSonda("fechaInsercion_"+codFilaArticuloNuevo)+"";
					String horaInsercion = forma.getMapaCateterSonda("horaInsercion_"+codFilaArticuloNuevo)+"";
					String fechaRetiro = forma.getMapaCateterSonda("fechaRetiro_"+codFilaArticuloNuevo)+"";
					String horaRetiro = forma.getMapaCateterSonda("horaRetiro_"+codFilaArticuloNuevo)+"";
					String curaciones = forma.getMapaCateterSonda("curaciones_"+codFilaArticuloNuevo)+"";
					String observaciones = forma.getMapaCateterSonda("observaciones_"+codFilaArticuloNuevo)+"";
					
					//-----Se verifica si ingresaron algï¿½n dato en las columnas fijas de cateter sonda ----------//
					if (UtilidadCadena.noEsVacio(viaInsercion) || UtilidadCadena.noEsVacio(fechaInsercion) || UtilidadCadena.noEsVacio(horaInsercion) || UtilidadCadena.noEsVacio(fechaRetiro) || UtilidadCadena.noEsVacio(horaRetiro) || UtilidadCadena.noEsVacio(curaciones) || UtilidadCadena.noEsVacio(observaciones))
					{
						cont+=2;
						break;
					}
					
					//---- Se verifica si ingresaron algï¿½n dato en las columnas parametrizadas de cateter sonda ---//
					if (forma.getMapaCateterSonda("colsCateterSonda") != null)
					{
						Vector colsCateterSondaParam=(Vector) forma.getMapaCateterSonda("colsCateterSonda");
						for (int z=0; z<colsCateterSondaParam.size(); z++)
							{
								int colCateterCcIns = Integer.parseInt(colsCateterSondaParam.elementAt(z)+"");
								
								//------- Valor del cateter sonda parametrizado ---------------//
								String valorColCateterCcIns = (String)forma.getMapaCateterSonda("colCateter_"+codFilaArticuloNuevo+"_"+colCateterCcIns);
								
								//------------ Se verifica si ingresaron algï¿½n dato en la columna cateter sonda parametrizada--------//
								if (UtilidadCadena.noEsVacio(valorColCateterCcIns))
									{
									cont+=2;
									break a;
									}
							}//for
					}//if colsCateterSonda != null
					
				}//if tipoArticulo != -1
			}//for
		}
		return cont;
	}
	
	/**
	 * Mï¿½todo que verifica si ingresaron datos en la secciï¿½n cuidados 
	 * especiales de enfermerï¿½a
	 * @param forma
	 * @return 0 --> Si no ingresaron ningï¿½n dato en la secciï¿½n cuidados enfermerï¿½a
	 *  	   1 --> Si ingresaron algï¿½n dato en la secciï¿½n cuidados enfermerï¿½a
	 */
	public int verificarDatosCuidadosEnfer (RegistroEnfermeriaForm forma)
	{
		int resp=0;
		//-----------Codigo de los cuidados de enfermerï¿½a parametrizados por instituciï¿½n centro costo y otros----------//
		Vector codigosCuidadosEnfer=(Vector) forma.getMapaCuidadosEspeciales("codigosCuidadoEnf");
		
		if (codigosCuidadosEnfer != null)
		{
			for(int i=0; i<codigosCuidadosEnfer.size();i++)
			{
				//------El codigo por instituciï¿½n centro del tipo de cuidado de enfermeria parametrizado u otro-----// 
				int tipoCuidado = Integer.parseInt(codigosCuidadosEnfer.elementAt(i)+"");
				
				//------Radio que indica si presenta o no ---------//
				String presenta = (String)forma.getMapaCuidadosEspeciales("presentaCuidado_"+tipoCuidado);
				
				//--- Descripciï¿½n del tipo de cuidado de enfermerï¿½a-------//
				String descripcion = (String)forma.getMapaCuidadosEspeciales("descripcionCuidado_"+tipoCuidado);
				
				//-Verificar que se haya seleccionado el tipo de cuidado
				if (UtilidadCadena.noEsVacio(presenta) || (UtilidadCadena.noEsVacio(descripcion)) )
				{
					return 1;
				}
			}
		}
		
		return resp;
	}
	
	/**
	 * Mï¿½todo para cerrar la conexiï¿½n
	 * @param con Conexiï¿½n a cerrar
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			if(!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexiï¿½n : "+e);
		}
	}
	//**********************METODOS PARA CONVULSIONES ************************************/
	private void accionGuardarConvulsiones(Connection con, int codEncabezado, RegistroEnfermeriaForm forma, RegistroEnfermeria mundo) 
	{
		HashMap vo=new HashMap();
		vo.put("codEncabezado",codEncabezado+"");
		vo.put("tipoConvulstion",forma.getConvulsion());
		vo.put("observacion", forma.getObservcionConvulstion());
		mundo.guardarConvulsiones(con,vo);
	}
	////////////////////////////FIN METODOS CONVULSIONES///////////////////////////////////////////
	
	//**********************METODOS PARA CONTROL DE ESFINTERES ************************************/
	private void accionGuardarControlEsfinteres(Connection con, int codEncabezado, RegistroEnfermeriaForm forma, RegistroEnfermeria mundo) 
	{
		mundo.insertarDetControlEsfinteres(con, codEncabezado, forma.getCodigoControlEsfinteres(), forma.getObservacionControlEsfinteres()+"<br>");
	}
	//***********************FIN METODOS CONTROL ESFINTERES********************************************
	
	//**********************METODOS PARA CONVULSIONES ************************************/
	private void accionGuardarFuerzaMuscular(Connection con, String codEncabezado, RegistroEnfermeriaForm forma, RegistroEnfermeria mundo) 
	{
		forma.setMapaNewFuerzaMuscular("codEncabezado", codEncabezado);
		mundo.guardarFuerzaMuscular(con, forma.getMapaNewFuerzaMuscular());
	}
	
	/**
	 * evalua que los datos requeridos esten en el mapa para insertarlos
	 * @param forma
	 * @return
	 */
	private boolean puedoInsertarFuerzaMuscular(RegistroEnfermeriaForm forma)
	{
		boolean centinelaExisteDato=false;
		boolean contieneKeys=false;
		if(forma.getMapaNewFuerzaMuscular().containsKey("superiorDerecho"))
		{
			if(!forma.getMapaNewFuerzaMuscular("superiorDerecho").equals(""))
			{
				centinelaExisteDato=true;
			}
			if(forma.getMapaNewFuerzaMuscular().containsKey("superiorIzquierdo"))
			{
				if(!forma.getMapaNewFuerzaMuscular("superiorIzquierdo").equals(""))
				{
					centinelaExisteDato=true;
				}
				if(forma.getMapaNewFuerzaMuscular().containsKey("inferiorDerecho"))
				{
					if(!forma.getMapaNewFuerzaMuscular("inferiorDerecho").equals(""))
					{
						centinelaExisteDato=true;
					}
					if(forma.getMapaNewFuerzaMuscular().containsKey("inferiorIzquierdo"))
					{
						if(!forma.getMapaNewFuerzaMuscular("inferiorIzquierdo").equals(""))
						{
							centinelaExisteDato=true;
						}
						contieneKeys= true;
					}
				}
			}
		}
		if(centinelaExisteDato && contieneKeys)
			return true;
		return false;
	}
	////////////////////////////FIN METODOS CONVULSIONES///////////////////////////////////////////
	
}

