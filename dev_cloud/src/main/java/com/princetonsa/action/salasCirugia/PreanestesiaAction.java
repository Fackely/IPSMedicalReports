/*
 * Creado en Oct 26, 2005
 */
package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.PreanestesiaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.salasCirugia.Preanestesia;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PreanestesiaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PreanestesiaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
															ActionForm form,
															HttpServletRequest request,
															HttpServletResponse response ) throws Exception
															{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning

			if(form instanceof PreanestesiaForm)
			{


				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				PreanestesiaForm preanestesiaForm =(PreanestesiaForm)form;
				HttpSession session=request.getSession();

				String estado=preanestesiaForm.getEstado(); 
				logger.warn("PreanestesiaAction Estado-->"+estado);

				if(estado == null)
				{
					preanestesiaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de registro preanestesia (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				//Código de la especialidad anestesiología parametrizado en valores por defecto
				int especialidadAnestesiologia=Integer.parseInt(ValoresPorDefecto.getEspecialidadAnestesiologia(medico.getCodigoInstitucionInt(),true));

				if( medico == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else if (!UtilidadValidacion.esProfesionalSalud(medico))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO personal de la salud trató de ingresar a la Preanestesia ","errors.usuario.noAutorizado", true) ;
				}
				//Se realiza la validación que el usuario sea médico si no es resumen 
				else if (!UtilidadValidacion.esMedico(medico).equals("") &&  !Boolean.parseBoolean(request.getParameter("esResumen")+""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO es Médico ",UtilidadValidacion.esMedico(medico), true) ;
				}
				//Se realiza la validación que el medico es anestesiólogo si no se abre la preanestesia en un popup si no es resumen
				else if(!UtilidadValidacion.esMedicoEspecialidad(con, medico.getCodigoPersona(), especialidadAnestesiologia) && estado.equals("empezar") && !Boolean.parseBoolean(request.getParameter("esResumen")+""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO es Anestesiólogo ","errors.noEsAnestesiologo", true) ;
				}
				else if (estado.equals("empezar"))
				{
					String nroPeticion = (request.getParameter("nroPeticion") + "");
					int estadoSolicitud = Integer.parseInt((request.getParameter("estadoSolicitud") + ""));
					int cabezote = Integer.parseInt((request.getParameter("ocultarCabezote") + ""));

					if(request.getParameter("mostrarMenuHojaAnestesia")!= null)
					{
						preanestesiaForm.setMostrarMenuHojaAnestesia(request.getParameter("mostrarMenuHojaAnestesia").toString());
						preanestesiaForm.setNumeroSolicitud(request.getParameter("numeroSolicitud").toString());
					}
					else
						preanestesiaForm.setMostrarMenuHojaAnestesia(ConstantesBD.acronimoNo);

					//Variable para indicar si se debe cargar la fecha y hora de preanestesia, sin permitir la modificación
					boolean cargarFechaPreanestesia=false;
					if(cabezote==1)
					{
						cargarFechaPreanestesia=true;
					}

					//Si el estado de la solicitud es -1 es porque viene del detalle de la petición de cirugía, por lo tanto
					//se consulta el estado de la orden de cirugía si la petición está asociada
					if(estadoSolicitud==-1)
					{
						//Se obtiene el número de la solicitud a la cuál está asociada la petición
						int nroSolicutud=UtilidadValidacion.estaPeticionAsociada(con, Integer.parseInt(nroPeticion))[1];

						//Si la petición está asociada a una solicitud se consulta el estado de la orden de cirugía
						if(nroSolicutud!=-1 && nroSolicutud!=0)
						{
							HashMap parametros = new HashMap();
							parametros.put("solicitud",nroSolicutud);
							estadoSolicitud = Integer.parseInt(((HashMap)HojaQuirurgica.consultaSolicitud(con,parametros) ).get("estado15_0").toString());									
						}
					}//if estadoSolicitud==-1
					return this.accionEmpezar(preanestesiaForm, mapping, con, medico, Integer.parseInt(nroPeticion), estadoSolicitud, cargarFechaPreanestesia);
				}
				else if(estado.equals("salir"))
				{
					return this.accionSalir(preanestesiaForm, mapping, con, medico, UtilidadTexto.agregarTextoAObservacion(null, null, medico, false));
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}		//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado empezar
	 * @param preanestesiaForm PreanestesiaForm para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param medico
	 * @param nroPeticion
	 * @param estadoSolicitud
	 * @param cargarFechaPreanestesia -> Para indicar si se debe cargar la fecha y hora de preanestesia y no permitir la modificación
	 * @return ActionForward a la página principal "ingresarPreanestesia.jsp"
	 * @throws SQLException
	 */

	private ActionForward accionEmpezar	(PreanestesiaForm preanestesiaForm, ActionMapping mapping, Connection con, UsuarioBasico  medico, int nroPeticion, int estadoSolicitud, boolean cargarFechaPreanestesia) throws SQLException
	{
		Preanestesia mundoPreanestesia=new Preanestesia();
		
		//Limpiamos lo que venga del form
		preanestesiaForm.reset();
		
		//Consultar los tipos de Examenes de Laboratorio de Preanestesia parametrizados para la Institución
		//preanestesiaForm.setListadoTiposExamenesLab (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 1));
		
		// Consultar los examenes de laboratorio de la institución o peticion
		HashMap mapaMundo=mundoPreanestesia.consultarExamenesLaboratorio(con, medico.getCodigoInstitucionInt(), nroPeticion);

		int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaMundo.put("numeroExamenesLab", numeroRegistros+"");
		
		preanestesiaForm.setMapaTipoExamenesLab(mapaMundo);
		
				
		//Consultar los tipos de Exámenes físicos de Preanestesia parametrizados para la Institución
		preanestesiaForm.setListadoTiposExamFisicosText (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 2));
		
		//Consultar los tipos de Exámenes físicos de Preanestesia parametrizados para la Institución
		preanestesiaForm.setListadoTiposExamFisicosArea (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 3));
		
		//Consultar los tipos de Conclusiones de Preanestesia parametrizados para la Institución
		preanestesiaForm.setListadoTiposConclusiones (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 4));
		
		//Se carga la información general de la petición en la preanestesia
		mundoPreanestesia.cargarInfoPeticion(con, nroPeticion);
		
		//Se carga las observaciones generales de la Preanestesia
		if(mundoPreanestesia.cargarPreanestesia(con, nroPeticion, cargarFechaPreanestesia))
			{
			//Se carga la información de los exámenes de laboratorio de la preanestesia
			mundoPreanestesia.cargarExamenesLabPre(con, nroPeticion, medico.getCodigoInstitucionInt());
			
			//Se carga la información de las conclusiones de preanestesia
			mundoPreanestesia.cargarConclusiones(con, nroPeticion);

			//-Cargar los examenes fisicos registrados 
			mundoPreanestesia.cargarExamenesFisicos(con, nroPeticion);
			}
		else
		{
			cargarFechaPreanestesia=false;
		}
		
		
		logger.info("\n\n\n\n\n VALOR DE LA PETICION >> "+mundoPreanestesia.getPeticionCirugia());
		
		//Se llena el form con la información del mundo
		llenarForm(preanestesiaForm, mundoPreanestesia, estadoSolicitud, cargarFechaPreanestesia);
		
		
		preanestesiaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado salir.
	 * Se copian las propiedades del objeto preanestesia en el objeto mundo
	 * @param preanestesiaForm PreanestesiaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param medico
	 * @param datosMedico
	 * @return ActionForward preanestesia.do?estado=salir"
	 * @throws SQLException
	*/
		
	private ActionForward accionSalir( PreanestesiaForm preanestesiaForm,
									   								ActionMapping mapping,  Connection con, UsuarioBasico medico, String datosMedico) throws SQLException
	{
		int peticionQx=0;
		Preanestesia mundoPreanestesia = new Preanestesia();
		
		llenarMundo(preanestesiaForm, mundoPreanestesia, medico);
		
		//Inserción o actualización de la valoración de preanestesia
		peticionQx=mundoPreanestesia.insertarValoracionPreanestesia(con, medico.getLoginUsuario(), datosMedico);
		
		//Inserción de los exámenes de laboratorio
		mundoPreanestesia.insertarExamenLabPreanestesia(con, peticionQx, datosMedico);
		
		//-Inserción de examenes fisicos ...
		mundoPreanestesia.insertarExamenFisico(con, peticionQx, medico);  
		
		//Inserción de las conclusiones
		mundoPreanestesia.insertarConclusionesPreanestesia(con, peticionQx);
		
		//Consultar los tipos de Examenes de Laboratorio de Preanestesia parametrizados para la Institución
		//preanestesiaForm.setListadoTiposExamenesLab (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), peticionQx, 1));
		
		//Consultar los examenes de laboratorio de la institución o peticion
		HashMap mapaMundo=mundoPreanestesia.consultarExamenesLaboratorio(con, medico.getCodigoInstitucionInt(), peticionQx);

		int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaMundo.put("numeroExamenesLab", numeroRegistros+"");
		
		preanestesiaForm.setMapaTipoExamenesLab(mapaMundo);

		//Consultar los tipos de Exámenes físicos de Preanestesia parametrizados para la Institución
		preanestesiaForm.setListadoTiposExamFisicosText (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), peticionQx, 2));
		
		//-Cargar los examenes fisicos registrados 
		mundoPreanestesia.cargarExamenesFisicos(con, peticionQx);
		
		//Se carga la información de los exámenes de laboratorio de la preanestesia
		mundoPreanestesia.cargarExamenesLabPre(con, peticionQx, medico.getCodigoInstitucionInt());

		preanestesiaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que llena el mundo, con los datos del form
	 * @param preanestesiaForm 
	 * @param mundoPreanestesia
	 * @param usuario
	 */
	private void llenarMundo(PreanestesiaForm preanestesiaForm, Preanestesia mundoPreanestesia, UsuarioBasico usuario)
	{
		//------------------------Información General de Preanestesia--------------------------------------//
		mundoPreanestesia.setPeticionCirugia(preanestesiaForm.getPeticionCirugia());
		mundoPreanestesia.setFechaPreanestesia(UtilidadFecha.conversionFormatoFechaABD(preanestesiaForm.getFechaPreanestesia()));
		mundoPreanestesia.setHoraPreanestesia(preanestesiaForm.getHoraPreanestesia());
		
		//------------Observaciones Generales----------------------//
		if (!preanestesiaForm.getObservacionesGralesNueva().trim().equals(""))
		{
			preanestesiaForm.setObservacionesGrales(UtilidadTexto.agregarTextoAObservacion(preanestesiaForm.getObservacionesGrales(),preanestesiaForm.getObservacionesGralesNueva(),usuario, false));
			mundoPreanestesia.setObservacionesGrales(preanestesiaForm.getObservacionesGrales());
		}
		else
		{
			mundoPreanestesia.setObservacionesGrales(preanestesiaForm.getObservacionesGrales());
		}
		
		//-Pasarle el tipo de anestesia
		mundoPreanestesia.setTipoAnestesia(preanestesiaForm.getTipoAnestesia());
		
		//Pasarle el hashmap al mundo con los datos del form de preanestesia 	
		mundoPreanestesia.setMapaCompleto(preanestesiaForm.getMapa());
		mundoPreanestesia.setMapaExamenFisico(preanestesiaForm.getMapaExamenFisico());
		
//----------------------------Se llena el mundo de las conclusiones de preanestesia----------------------------------------------//
		if(preanestesiaForm.getMapaConclusion("codigosConclusiones") != null)
		{
			Vector codigosConclu=(Vector) preanestesiaForm.getMapaConclusion("codigosConclusiones");
			
			for (int i=0; i<codigosConclu.size(); i++)
			{
				int conclusionPreInst=Integer.parseInt(codigosConclu.elementAt(i)+"");
				
				//Valor de la conclusion Nueva
				String valorConclusionNueva=(String)preanestesiaForm.getMapaConclusion("conclusionNueva_"+conclusionPreInst);
				//Valor de la conclusion
				String valorConclusion=(String)preanestesiaForm.getMapaConclusion("conclusion_"+conclusionPreInst);
				
				if (!valorConclusionNueva.trim().equals(""))
					{
						preanestesiaForm.setMapaConclusion("conclusion_"+conclusionPreInst, UtilidadTexto.agregarTextoAObservacion(valorConclusion, valorConclusionNueva, usuario, false));
						mundoPreanestesia.setMapaConclusion("conclusion_"+conclusionPreInst, preanestesiaForm.getMapaConclusion("conclusion_"+conclusionPreInst));
					}
				else
					{
						mundoPreanestesia.setMapaConclusion("conclusion_"+conclusionPreInst, valorConclusion);
					}
				
			}//for
			mundoPreanestesia.setMapaConclusion("codigosConclusiones", preanestesiaForm.getMapaConclusion("codigosConclusiones"));
		}//if codigos != null
		
	}
	
	/**
	 * Método para llenar el form con los datos del mundo
	 * @param preanestesiaForm
	 * @param mundoPreanestesia
	 * @param estadoSolicitud
	 * @param cargarFechaPreanestesia -> Indica si se debe cargar la fecha y hora de preanestesia
	 */
	private void llenarForm(PreanestesiaForm preanestesiaForm, Preanestesia mundoPreanestesia, int estadoSolicitud, boolean cargarFechaPreanestesia) 
	{
		//Se carga la fecha y hora de preanestesia consultada en el mundo
		if(cargarFechaPreanestesia)
		{
			preanestesiaForm.setFechaPreanestesia(UtilidadFecha.conversionFormatoFechaAAp(mundoPreanestesia.getFechaPreanestesia()));
			preanestesiaForm.setHoraPreanestesia(mundoPreanestesia.getHoraPreanestesia());
			preanestesiaForm.setCargarFechaHoraPreanestesia(true);
		}
		
	//-----------------------------------------Datos Generales de la Preanestesia-------------------------------------------------//
	preanestesiaForm.setObservacionesGrales(mundoPreanestesia.getObservacionesGrales());
	preanestesiaForm.setTipoAnestesia(mundoPreanestesia.getTipoAnestesia());
	
	
	//-------------------------------------------------LLENAR EL MAPA------------------------------------------------------------------------//
	preanestesiaForm.setMapa(mundoPreanestesia.getMapaCompleto());
	preanestesiaForm.setMapaConclusion(mundoPreanestesia.getMapaConclusionCompleto());
		
	//---llenar el mapa de examenes de laboratorio
	preanestesiaForm.setMapaExamenFisico(mundoPreanestesia.getMapaExamenFisico());
	
	//-----------Llenar la información general de la petición de cirugía------------------------------------------//
	preanestesiaForm.setPeticionCirugia(mundoPreanestesia.getPeticionCirugia());
	preanestesiaForm.setFechaPeticion(UtilidadFecha.conversionFormatoFechaAAp(mundoPreanestesia.getFechaPeticion()));
	preanestesiaForm.setHoraPeticion(mundoPreanestesia.getHoraPeticion());
	preanestesiaForm.setNombreEstadoPeticion(mundoPreanestesia.getNombreEstadoPeticion());
	preanestesiaForm.setCodigoEstadoPeticion(mundoPreanestesia.getCodigoEstadoPeticion());
	preanestesiaForm.setFechaCirugia(mundoPreanestesia.getFechaCirugia());
	preanestesiaForm.setDuracionCirugia(mundoPreanestesia.getDuracionCirugia());
	preanestesiaForm.setNombreSolicitante(mundoPreanestesia.getNombreSolicitante());
	
	//---------------Se carga el estado de la solicitud de la orden de cirugía-----------------------------//
	preanestesiaForm.setEstadoSolicitud(estadoSolicitud);
	
	preanestesiaForm.setTipoAnestesia(mundoPreanestesia.getTipoAnestesia());
	
	
	logger.info("\n\n valor de peticion cirugia >> "+preanestesiaForm.getPeticionCirugia());
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
	} 

}
