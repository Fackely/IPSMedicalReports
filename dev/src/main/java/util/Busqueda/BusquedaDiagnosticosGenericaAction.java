/*
 * Ene 16, 2007 
 */
package util.Busqueda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaDiagnosticosGenerica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.sysmedica.util.UtilidadFichas;

/**
 * @author Sebastián Gómez Rivillas
 * Action, controla todas las opciones dentro de la busqueda de diagnósticos genérica 
 * incluyendo los posibles casos de error y los casos de flujo.
 *
 */
public class BusquedaDiagnosticosGenericaAction extends Action 
{
	
	/**
    * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(BusquedaDiagnosticosGenericaAction.class);
	
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
			if(form instanceof BusquedaDiagnosticosGenericaForm)
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
				BusquedaDiagnosticosGenericaForm busquedaForm =(BusquedaDiagnosticosGenericaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=busquedaForm.getEstado(); 
				logger.warn("estado BusquedaDiagnosticosGenericaAction-->"+estado);

				if( usuario==null || paciente==null)
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("cerrarDiagnostico");
				}

				if(estado == null)
				{
					busquedaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Busqueda Diagnósticos Genérica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("busqueda"))
				{
					return accionBusqueda(con,busquedaForm,usuario,paciente,mapping,response);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,busquedaForm,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,busquedaForm,mapping);
				}
				else if (estado.equals("asignar"))
				{
					return accionAsignar(con,busquedaForm,mapping,usuario,paciente,response);
				}
				/**
				 * Estado destinado para procesar la respuesta
				 * enviada por la ficha epidemiológica
				 */ 
				else if (estado.equals("resultado"))
				{
					return accionResultado(con,busquedaForm,mapping);
				}
				else
				{
					busquedaForm.reset();
					UtilidadBD.closeConnection(con);
					logger.warn("Estado no valido dentro del flujo de Busqueda Diagnosticos Generica (null) ");
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
	 * Método implementado para procesar la respuesta enviada desde la funcionalidad
	 * de fichas epidemiológicas
	 * @param con
	 * @param busquedaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionResultado(Connection con, BusquedaDiagnosticosGenericaForm busquedaForm, ActionMapping mapping) 
	{
		//Se inicializa el estado de la ficha epidemiológica
		busquedaForm.setEstadoEpidemiologia(0);
		UtilidadBD.closeConnection(con);
		
		//Se verifica si hubo resultado exitoso
		if(!busquedaForm.getResultadoInsercionFicha().equals(""))
			return mapping.findForward("asignarDiagnostico");
		else
			return mapping.findForward("listarDiagnosticos");
	}

	/**
	 * Método implementado para asignar un diagnostico seleccionado al formulario desde donde se llamó
	 * la busqueda de diagnosticos
	 * @param con
	 * @param busquedaForm
	 * @param mapping
	 * @param paciente 
	 * @param response 
	 * @return
	 */
	private ActionForward accionAsignar(Connection con, BusquedaDiagnosticosGenericaForm busquedaForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) 
	{
		//Se inicia resultado ingreso ficha
		busquedaForm.setResultadoInsercionFicha("");
		
		//Se edita el diagnostico seleccionado
		busquedaForm.setValorDiagnostico(
			busquedaForm.getDiagnosticos("acronimo_"+busquedaForm.getPos())+ConstantesBD.separadorSplit+
			busquedaForm.getDiagnosticos("tipoCie_"+busquedaForm.getPos())+ConstantesBD.separadorSplit+
			busquedaForm.getDiagnosticos("nombre_"+busquedaForm.getPos())+ConstantesBD.separadorSplit+
			busquedaForm.getDiagnosticos("codigoEnfermedad_"+busquedaForm.getPos())
		);
		
		if(!busquedaForm.getPathJavaScriptAsignacion().toString().trim().equals(""))
		{
			UtilidadBD.closeConnection(con);
			try
			{
				response.sendRedirect(busquedaForm.getPathJavaScriptAsignacion());
			} 
			catch (IOException e)
			{
				logger.error("Error en accionAsignar: "+e);
			}
			return null;
		}
		
		//******SE VERIFICA SI ES DE EPIDEMIOLOGÍA**************
		if(busquedaForm.isEpidemiologia())
		{
			if (Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),ConstantesBD.codigoFuncionalidadNotificarCasos)) 
			{
				String[] vectorDx = busquedaForm.getValorDiagnostico().split(ConstantesBD.separadorSplit);
				
				int codigoEnfermedadNotificable = 0;
				if(vectorDx.length>=4)
					codigoEnfermedadNotificable = Integer.parseInt(vectorDx[3]);
				String acronimo = vectorDx[0];
				
				//************VALIDACION DIAGNOSTICO NOTIFICABLE*******************************************************************************************
				int estadoSiguienteEpidemiologia = UtilidadFichas.siguienteEstadoEpidemiologia(con,codigoEnfermedadNotificable,paciente.getCodigoPersona(),acronimo);
				
				//se consulta el codigo  del convenio
				int codigoConvenio = obtenerCodigoConvenio(con,busquedaForm.getNumSolDx());
				
				busquedaForm.setEstadoEpidemiologia(estadoSiguienteEpidemiologia);
				busquedaForm.setUrlEpidemiologia(UtilidadFichas.obtenerUrlEpidemiologia(con,codigoEnfermedadNotificable,paciente.getCodigoPersona(),codigoConvenio,acronimo,usuario.getLoginUsuario()));
				
				if (estadoSiguienteEpidemiologia>0) 
				{
					String url = busquedaForm.getUrlEpidemiologia();
					url += "&valorDiagnostico="+busquedaForm.getValorDiagnostico()+
						"&idDiagnostico="+busquedaForm.getIdDiagnostico()+
						"&propiedadDiagnostico="+busquedaForm.getPropiedadDiagnostico()+
						"&idDiv="+busquedaForm.getIdDiv()+
						"&idCheckBox="+busquedaForm.getIdCheckBox()+
						"&idHiddenCheckBox="+busquedaForm.getIdHiddenCheckBox()+
						"&propiedadHiddenCheckBox="+busquedaForm.getPropiedadHiddenCheckBox()+
						"&tipoDiagnostico="+busquedaForm.getTipoDiagnostico()+
						"&numero="+busquedaForm.getNumero()+
						"&idNumero="+busquedaForm.getIdNumero()+
						"&diagnosticosSeleccionados="+busquedaForm.getDiagnosticosSeleccionados()+
						"&idDiagSeleccionados="+busquedaForm.getIdDiagSeleccionados()+
						"&idValorFicha="+busquedaForm.getIdValorFicha()+
						"&epidemiologia="+busquedaForm.isEpidemiologia()+
						"";
					
					UtilidadBD.closeConnection(con);
					try 
					{
						response.sendRedirect(url);
					} 
					catch (IOException e) 
					{
						logger.error("Error en accionAsignar: "+e);
					}
		    		return null;
				}
				//********************************************************************************************************************************************
			}
		}
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("asignarDiagnostico");
	}

	/**
	 * Método que consulta el codigo del convenio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private int obtenerCodigoConvenio(Connection con, String numeroSolicitud) 
	{
		int idCuenta = Utilidades.getCuentaSolicitud(con, Utilidades.convertirAEntero(numeroSolicitud));
		int codigoConvenio = Utilidades.obtenerCodigoConvenio(con, idCuenta);
		
		return codigoConvenio;
	}

	/**
	 * Método que realiza la ordenacion del listado de diagnósticos
	 * @param con
	 * @param busquedaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, BusquedaDiagnosticosGenericaForm busquedaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"acronimo_",
				"tipoCie_",
				"nombre_",
				"nombreSexo_",
				"edadInicial_",
				"edadFinal_",
				"esPrincipal_",
				"esMuerte_",
				"codigoEnfermedad_"
			};

		
		
		busquedaForm.setDiagnosticos(Listado.ordenarMapa(indices,
				busquedaForm.getIndice(),
				busquedaForm.getUltimoIndice(),
				busquedaForm.getDiagnosticos(),
				busquedaForm.getNumDiagnosticos()));
		
		
		busquedaForm.setDiagnosticos("numRegistros",busquedaForm.getNumDiagnosticos()+"");
		
		busquedaForm.setUltimoIndice(busquedaForm.getIndice());
		busquedaForm.setEstado("busqueda");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listarDiagnosticos");
	}

	/**
	 * Método implementado para hacer la paginación del listado de diagnósticos
	 * @param con
	 * @param busquedaForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, BusquedaDiagnosticosGenericaForm busquedaForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			busquedaForm.setEstado("busqueda");
		    UtilidadBD.closeConnection(con);
			response.sendRedirect(busquedaForm.getLinkSiguiente());
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en accionRedireccion de BusquedaDiagnosticosGenericaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en BusquedaDiagnosticosGenericaAction", "errors.problemasDatos", true);
		}
		
	}

	/**
	 * Método implementado para realizar la busqueda de diagnósticos
	 * @param con
	 * @param busquedaForm
	 * @param usuario 
	 * @param paciente
	 * @param mapping
	 * @param response 
	 * @return
	 */
	private ActionForward accionBusqueda(Connection con, BusquedaDiagnosticosGenericaForm busquedaForm, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletResponse response) 
	{
		
		busquedaForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		busquedaForm.setCodigoCie(ConstantesBD.codigoNuncaValido+"");
		busquedaForm.setEstadoEpidemiologia(0);
		String fechaAValidar = "";
		
		//******************CONSULTAR LA FECHA DE VALIDACION*******************************************************
		if (paciente == null || paciente.getCodigoCuenta()<=0)
		{
			//Caso admisión Hospitalización
			fechaAValidar=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
		}
		else if (paciente.getCodigoAdmision()>0)
		{
			//En este punto buscamos la fecha de la 
			//admisión, a través de la cual vamos
			//a buscar el tipo de Cie Válido
			try 
			{
				fechaAValidar=UtilidadValidacion.getFechaAdmision(con, paciente.getCodigoCuenta());
			} 
			catch (SQLException e) 
			{
				logger.error("Error obteniendo la fecha admision en accionBusqueda: "+e);
				fechaAValidar = "";
			}
		}
		else if( (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)||
				 (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios))
		{
			//Si no tiene admisión, nos encontramos
			//en consulta externa, en cuyo caso debemos
			//buscar la cita
			if(busquedaForm.getNumeroSolicitud()==null)
				fechaAValidar  = Utilidades.getFechaAperturaCuenta(con, paciente.getCodigoCuenta());
			else
			{
				fechaAValidar=BusquedaDiagnosticosGenerica.consultaFechaCita(con, busquedaForm.getNumeroSolicitud());
				//-Esta Condicion se hizo para que ha paciente de consulta externa 
				//-y ambulatorios la busqueda de servicios del JSP le sanga correctamente
				//-debido a que no pueden tener una cita registrada en la agenda ( Tarea: Xplanner 2, Proyecto : Enero 13 2006 Tarea : 8495)
				if ( !UtilidadCadena.noEsVacio(fechaAValidar) )   
				{
					if (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
						fechaAValidar = Utilidades.getFechaSolicitud(con, Integer.parseInt(busquedaForm.getNumeroSolicitud()));
					
					if (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
						fechaAValidar  = Utilidades.getFechaAperturaCuenta(con, paciente.getCodigoCuenta());
				}
			}
		}
		else
			//Caso en el cual se le dió egreso a un paciente de urgencias o de hospitalización
			fechaAValidar=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
		//*********************************************************************************************************
		
		
		//***********CONSULTAR EL CODIGO DEL CIE VIGENTE****************************************************
		if (fechaAValidar!=null&&!fechaAValidar.equals(""))
			busquedaForm.setCodigoCie(BusquedaDiagnosticosGenerica.consultaCieVigente(con, fechaAValidar)+"");
		
		
		//****CALCULAR LA EDAD DEL PACIENTE EN DÍAS************************************
		int edad = 0;
		if(busquedaForm.isFiltrarEdad())
		{
			if(busquedaForm.getEdad().equals(""))
				edad = UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(),UtilidadFecha.getFechaActual());
			else
				edad = Integer.parseInt(busquedaForm.getEdad());
		}
		
		//**********SE REALIZA LA BUSQUEDA DE DIAGNÓSTICOS****************************************
		busquedaForm.setDiagnosticos(BusquedaDiagnosticosGenerica.consultaDiagnosticos(
			con, 
			busquedaForm.getCriterioBusqueda(), 
			busquedaForm.isBuscarTexto(), 
			busquedaForm.getCodigoCie(), 
			busquedaForm.isEsPrincipal(), 
			busquedaForm.isEsMuerte(), 
			busquedaForm.isFiltrarSexo(), 
			busquedaForm.getSexo().equals("")?paciente.getCodigoSexo()+"":busquedaForm.getSexo(), 
			busquedaForm.isFiltrarEdad(), 
			edad+"",
			busquedaForm.getTipoDiagnostico()+"",
			busquedaForm.getDiagnosticosSeleccionados(),
			busquedaForm.getCodigoFiltro()+"",
			usuario.getCodigoInstitucion()
			)
		);
		busquedaForm.setNumDiagnosticos(Integer.parseInt(busquedaForm.getDiagnosticos("numRegistros").toString()));
		
		//*******SE VERIFICA SI LA BUSQUEDA ARROJÓ UN SOLO RESULTADO*****************************
		if(busquedaForm.getNumDiagnosticos()==1)
		{
			busquedaForm.setPos(0);
			//se va directamente a asignar el diagnostico
			return accionAsignar(con, busquedaForm, mapping, usuario, paciente,response);
		}
		//***************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listarDiagnosticos");
	}
}
