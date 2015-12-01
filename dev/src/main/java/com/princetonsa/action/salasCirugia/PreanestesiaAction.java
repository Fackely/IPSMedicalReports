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
 * @author Andr�s Mauricio Ruiz V�lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PreanestesiaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PreanestesiaAction.class);
	
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
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
					logger.warn("No se pudo abrir la conexi�n"+e.toString());
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

				//C�digo de la especialidad anestesiolog�a parametrizado en valores por defecto
				int especialidadAnestesiologia=Integer.parseInt(ValoresPorDefecto.getEspecialidadAnestesiologia(medico.getCodigoInstitucionInt(),true));

				if( medico == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else if (!UtilidadValidacion.esProfesionalSalud(medico))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO personal de la salud trat� de ingresar a la Preanestesia ","errors.usuario.noAutorizado", true) ;
				}
				//Se realiza la validaci�n que el usuario sea m�dico si no es resumen 
				else if (!UtilidadValidacion.esMedico(medico).equals("") &&  !Boolean.parseBoolean(request.getParameter("esResumen")+""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO es M�dico ",UtilidadValidacion.esMedico(medico), true) ;
				}
				//Se realiza la validaci�n que el medico es anestesi�logo si no se abre la preanestesia en un popup si no es resumen
				else if(!UtilidadValidacion.esMedicoEspecialidad(con, medico.getCodigoPersona(), especialidadAnestesiologia) && estado.equals("empezar") && !Boolean.parseBoolean(request.getParameter("esResumen")+""))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO es Anestesi�logo ","errors.noEsAnestesiologo", true) ;
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

					//Variable para indicar si se debe cargar la fecha y hora de preanestesia, sin permitir la modificaci�n
					boolean cargarFechaPreanestesia=false;
					if(cabezote==1)
					{
						cargarFechaPreanestesia=true;
					}

					//Si el estado de la solicitud es -1 es porque viene del detalle de la petici�n de cirug�a, por lo tanto
					//se consulta el estado de la orden de cirug�a si la petici�n est� asociada
					if(estadoSolicitud==-1)
					{
						//Se obtiene el n�mero de la solicitud a la cu�l est� asociada la petici�n
						int nroSolicutud=UtilidadValidacion.estaPeticionAsociada(con, Integer.parseInt(nroPeticion))[1];

						//Si la petici�n est� asociada a una solicitud se consulta el estado de la orden de cirug�a
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
	 * Este m�todo especifica las acciones a realizar en el estado empezar
	 * @param preanestesiaForm PreanestesiaForm para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegaci�n
	 * @param con Conexi�n con la fuente de datos
	 * @param medico
	 * @param nroPeticion
	 * @param estadoSolicitud
	 * @param cargarFechaPreanestesia -> Para indicar si se debe cargar la fecha y hora de preanestesia y no permitir la modificaci�n
	 * @return ActionForward a la p�gina principal "ingresarPreanestesia.jsp"
	 * @throws SQLException
	 */

	private ActionForward accionEmpezar	(PreanestesiaForm preanestesiaForm, ActionMapping mapping, Connection con, UsuarioBasico  medico, int nroPeticion, int estadoSolicitud, boolean cargarFechaPreanestesia) throws SQLException
	{
		Preanestesia mundoPreanestesia=new Preanestesia();
		
		//Limpiamos lo que venga del form
		preanestesiaForm.reset();
		
		//Consultar los tipos de Examenes de Laboratorio de Preanestesia parametrizados para la Instituci�n
		//preanestesiaForm.setListadoTiposExamenesLab (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 1));
		
		// Consultar los examenes de laboratorio de la instituci�n o peticion
		HashMap mapaMundo=mundoPreanestesia.consultarExamenesLaboratorio(con, medico.getCodigoInstitucionInt(), nroPeticion);

		int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaMundo.put("numeroExamenesLab", numeroRegistros+"");
		
		preanestesiaForm.setMapaTipoExamenesLab(mapaMundo);
		
				
		//Consultar los tipos de Ex�menes f�sicos de Preanestesia parametrizados para la Instituci�n
		preanestesiaForm.setListadoTiposExamFisicosText (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 2));
		
		//Consultar los tipos de Ex�menes f�sicos de Preanestesia parametrizados para la Instituci�n
		preanestesiaForm.setListadoTiposExamFisicosArea (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 3));
		
		//Consultar los tipos de Conclusiones de Preanestesia parametrizados para la Instituci�n
		preanestesiaForm.setListadoTiposConclusiones (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), nroPeticion, 4));
		
		//Se carga la informaci�n general de la petici�n en la preanestesia
		mundoPreanestesia.cargarInfoPeticion(con, nroPeticion);
		
		//Se carga las observaciones generales de la Preanestesia
		if(mundoPreanestesia.cargarPreanestesia(con, nroPeticion, cargarFechaPreanestesia))
			{
			//Se carga la informaci�n de los ex�menes de laboratorio de la preanestesia
			mundoPreanestesia.cargarExamenesLabPre(con, nroPeticion, medico.getCodigoInstitucionInt());
			
			//Se carga la informaci�n de las conclusiones de preanestesia
			mundoPreanestesia.cargarConclusiones(con, nroPeticion);

			//-Cargar los examenes fisicos registrados 
			mundoPreanestesia.cargarExamenesFisicos(con, nroPeticion);
			}
		else
		{
			cargarFechaPreanestesia=false;
		}
		
		
		logger.info("\n\n\n\n\n VALOR DE LA PETICION >> "+mundoPreanestesia.getPeticionCirugia());
		
		//Se llena el form con la informaci�n del mundo
		llenarForm(preanestesiaForm, mundoPreanestesia, estadoSolicitud, cargarFechaPreanestesia);
		
		
		preanestesiaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Este m�todo especifica las acciones a realizar en el estado salir.
	 * Se copian las propiedades del objeto preanestesia en el objeto mundo
	 * @param preanestesiaForm PreanestesiaForm
	 * @param mapping Mapping para manejar la navegaci�n
	 * @param con Conexi�n con la fuente de datos
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
		
		//Inserci�n o actualizaci�n de la valoraci�n de preanestesia
		peticionQx=mundoPreanestesia.insertarValoracionPreanestesia(con, medico.getLoginUsuario(), datosMedico);
		
		//Inserci�n de los ex�menes de laboratorio
		mundoPreanestesia.insertarExamenLabPreanestesia(con, peticionQx, datosMedico);
		
		//-Inserci�n de examenes fisicos ...
		mundoPreanestesia.insertarExamenFisico(con, peticionQx, medico);  
		
		//Inserci�n de las conclusiones
		mundoPreanestesia.insertarConclusionesPreanestesia(con, peticionQx);
		
		//Consultar los tipos de Examenes de Laboratorio de Preanestesia parametrizados para la Instituci�n
		//preanestesiaForm.setListadoTiposExamenesLab (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), peticionQx, 1));
		
		//Consultar los examenes de laboratorio de la instituci�n o peticion
		HashMap mapaMundo=mundoPreanestesia.consultarExamenesLaboratorio(con, medico.getCodigoInstitucionInt(), peticionQx);

		int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
		mapaMundo.put("numeroExamenesLab", numeroRegistros+"");
		
		preanestesiaForm.setMapaTipoExamenesLab(mapaMundo);

		//Consultar los tipos de Ex�menes f�sicos de Preanestesia parametrizados para la Instituci�n
		preanestesiaForm.setListadoTiposExamFisicosText (mundoPreanestesia.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), peticionQx, 2));
		
		//-Cargar los examenes fisicos registrados 
		mundoPreanestesia.cargarExamenesFisicos(con, peticionQx);
		
		//Se carga la informaci�n de los ex�menes de laboratorio de la preanestesia
		mundoPreanestesia.cargarExamenesLabPre(con, peticionQx, medico.getCodigoInstitucionInt());

		preanestesiaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * M�todo que llena el mundo, con los datos del form
	 * @param preanestesiaForm 
	 * @param mundoPreanestesia
	 * @param usuario
	 */
	private void llenarMundo(PreanestesiaForm preanestesiaForm, Preanestesia mundoPreanestesia, UsuarioBasico usuario)
	{
		//------------------------Informaci�n General de Preanestesia--------------------------------------//
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
	 * M�todo para llenar el form con los datos del mundo
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
	
	//-----------Llenar la informaci�n general de la petici�n de cirug�a------------------------------------------//
	preanestesiaForm.setPeticionCirugia(mundoPreanestesia.getPeticionCirugia());
	preanestesiaForm.setFechaPeticion(UtilidadFecha.conversionFormatoFechaAAp(mundoPreanestesia.getFechaPeticion()));
	preanestesiaForm.setHoraPeticion(mundoPreanestesia.getHoraPeticion());
	preanestesiaForm.setNombreEstadoPeticion(mundoPreanestesia.getNombreEstadoPeticion());
	preanestesiaForm.setCodigoEstadoPeticion(mundoPreanestesia.getCodigoEstadoPeticion());
	preanestesiaForm.setFechaCirugia(mundoPreanestesia.getFechaCirugia());
	preanestesiaForm.setDuracionCirugia(mundoPreanestesia.getDuracionCirugia());
	preanestesiaForm.setNombreSolicitante(mundoPreanestesia.getNombreSolicitante());
	
	//---------------Se carga el estado de la solicitud de la orden de cirug�a-----------------------------//
	preanestesiaForm.setEstadoSolicitud(estadoSolicitud);
	
	preanestesiaForm.setTipoAnestesia(mundoPreanestesia.getTipoAnestesia());
	
	
	logger.info("\n\n valor de peticion cirugia >> "+preanestesiaForm.getPeticionCirugia());
	}
	
	/**
	 * M�todo en que se cierra la conexi�n (Buen manejo recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexi�n con la fuente de datos
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
