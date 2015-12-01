/*
 * Creado en Sep 21, 2005
 */
package com.princetonsa.action.hojaOftalmologica;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Imagen;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.hojaOftalmologica.HojaOftalmologicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.hojaOftalmologica.HojaOftalmologica;
import com.princetonsa.pdf.HojaOftalmologicaPdf;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaOftalmologicaAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(HojaOftalmologicaAction.class);
	
	/**
	 * Para cargar el los datos actuales de la sección estrabismo
	 */
	HojaOftalmologicaForm formAuxHOftal = new HojaOftalmologicaForm();  
	
	
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

			if(form instanceof HojaOftalmologicaForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				HojaOftalmologicaForm hojaOftalmologicaForm =(HojaOftalmologicaForm)form;
				HttpSession session=request.getSession();		

				String estado=hojaOftalmologicaForm.getEstado(); 
				String estadoAnterior=hojaOftalmologicaForm.getEstadoAnterior();

				logger.warn("HojaOftalmologicaAction Estado-->"+estado);
				logger.warn("HojaOftalmologicaAction Estado Anterior-->"+estadoAnterior);

				if(request.getParameter("mostrarSeccion")==null && estado.equals("empezar"))
					hojaOftalmologicaForm.setMostrarSeccion(0);

				if(estado == null)
				{
					hojaOftalmologicaForm.reset(false);	
					logger.warn("Estado no valido dentro del flujo de registro hoja oftalmológica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				if( medico == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
				{
					//Valida que el  paciente esté cargado
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}

					//Validar que el usuario se un profesional de la salud
					if(!UtilidadValidacion.esProfesionalSalud(medico))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
					}

					//Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);


					//----Si el estado es diferente a cargarEstrabismo y cargarEstrabismoActual se coloca en false para que no abra la sección
					if(!estado.equals("cargarEstrabismo") && !estado.equals("cargarEstrabismoActual"))
					{
						hojaOftalmologicaForm.setAbrirSeccionEstrabismo(false);
					}

					if (estado.equals("empezar") || estadoAnterior.equals("imprimir"))
					{		
						return this.accionEmpezar(hojaOftalmologicaForm, mapping, con, medico, paciente);
					}
					else if(estado.equals("salir") && !estadoAnterior.equals("imprimir"))
					{
						return this.accionSalir(hojaOftalmologicaForm, mapping, con, paciente, medico);
					}
					else if(estado.startsWith("guardarImagen"))
					{
						cerrarConexion(con);
						return this.accionGuardarImagen(hojaOftalmologicaForm, mapping, estado);
					}
					else if(estado.equals("cargarEstrabismo"))
					{
						return this.accionCargarEstrabismo(hojaOftalmologicaForm, mapping, con, medico);
					}
					//Se carga el form de la hoja oftalmológica con el form clonado
					else if(estado.equals("cargarEstrabismoActual") && estadoAnterior.equals("cargarEstrabismo"))
					{
						UtilidadBD.cerrarConexion(con);
						return this.accionCargarEstrabismoActual(hojaOftalmologicaForm, mapping, medico.getNombreRegistroMedico());
					}
					//Como viene vacío el form clonado se llama al estado empezar
					else if(estado.equals("cargarEstrabismoActual") && !estadoAnterior.equals("cargarEstrabismo"))
					{
						return this.accionEmpezar(hojaOftalmologicaForm, mapping, con, medico, paciente);
					}
					else if (estado.equals("imprimirEstrabismo"))
					{
						return this.imprimirEstrabismo(hojaOftalmologicaForm,mapping, request, medico, paciente, con);
					}
					else if (estado.equals("imprimirSegmentoAnterior"))
					{
						return this.imprimirSegmentoAnterior(hojaOftalmologicaForm,mapping, request, medico, paciente, con);
					}
					else if (estado.equals("imprimirOrbitaAnexos"))
					{
						return this.imprimirOrbitaAnexos(hojaOftalmologicaForm,mapping, request, medico, paciente, con);
					}
					else if (estado.equals("imprimirRetinaVitreo"))
					{
						return this.imprimirRetinaVitreo(hojaOftalmologicaForm,mapping, request, medico, paciente, con);
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
				}//else	
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
	 * Este método especifica las acciones a realizar en el estado empezar
	 * @param hojaOftalmologicaForm HojaOftalmologicaForm para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param medico
	 * @param paciente
	 * @return ActionForward a la página principal "ingresoHojaOftalmologica.jsp"
	 * @throws SQLException
	 */

	private ActionForward accionEmpezar	(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, Connection con, UsuarioBasico  medico, PersonaBasica paciente) throws SQLException
	{
		formAuxHOftal.reset(false);
		HojaOftalmologica mundoHojaOftalmologica=new HojaOftalmologica();
		
		int seccionTemp=0;
		
		//Si mostrar sección es diferente de cero se guarda en un temporal la sección
		if(hojaOftalmologicaForm.getMostrarSeccion()!=0)
		{
			seccionTemp=hojaOftalmologicaForm.getMostrarSeccion();
		}
		
		//Limpiamos lo que venga del form
		hojaOftalmologicaForm.reset(false);
		hojaOftalmologicaForm.setMostrarSeccion(seccionTemp);
		
		//Consultar los tipos de ppc paramétrizados en la institución
		hojaOftalmologicaForm.setListadoTiposPpc( mundoHojaOftalmologica.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), 1));
		
		//Consultar los tipos de segmento anterior paramétrizados en la institución
		hojaOftalmologicaForm.setListadoTiposSegmentoAnt( mundoHojaOftalmologica.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), 2));
		
		//Consultar los tipos de orbitas y anexos paramétrizados en la institución
		hojaOftalmologicaForm.setListadoTiposOrbitaAnexos( mundoHojaOftalmologica.consultarTipoParametrizado(con, medico.getCodigoInstitucionInt(), 3));
		
		//Consultar el histórico de las fechas en la sección Estrabismo
		hojaOftalmologicaForm.setListadoHistoEstrabismo (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 1));
		
		//Consultar el histórico de la sección Segmento Anterior
		hojaOftalmologicaForm.setListadoHistoSegmentoAnt (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 2));
		
        //Consultar el histórico de la sección Retina Vitreo
        hojaOftalmologicaForm.setListadoHistoRetinaVitreo (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 3));

        //Consultar el histórico de la sección Orbita y Anexos
		hojaOftalmologicaForm.setListadoHistoOrbitaAnexos (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 4));
		
		//Se carga las observaciones de la hoja oftalmologica en el formulario
		if (mundoHojaOftalmologica.cargarHojaOftalmologica(con, paciente.getCodigoPersona()))
		{
			llenarForm(hojaOftalmologicaForm, mundoHojaOftalmologica);
		}
		
		hojaOftalmologicaForm.setEstadoAnterior("empezar");
		hojaOftalmologicaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado salir.
	 * Se copian las propiedades del objeto hojaOftalmologica en el objeto mundo
	 * @param hojaOftalmologicaForm HojaOftalmologicaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param paciente
	 * @param medico
	 * @return ActionForward hojaOftalmologica.do?estado=salir"
	 * @throws SQLException
	*/
		
	private ActionForward accionSalir( HojaOftalmologicaForm hojaOftalmologicaForm,
									   								ActionMapping mapping,  Connection con, PersonaBasica paciente, 
									   								UsuarioBasico  medico) throws SQLException
	{
		int codHojaOftal=0, codEncaHisto=0, codEstrabismo=0,codSegAnt=0 ;
		HojaOftalmologica mundoHojaOftalmologica = new HojaOftalmologica();
		
		llenarMundo(hojaOftalmologicaForm, mundoHojaOftalmologica, medico, true);
		
		//Se obtiene un valor para saber en que sección(es) se ingresaron datos
		if (verificarInsercionHojaOftalmologica(hojaOftalmologicaForm))
		{
			codHojaOftal=mundoHojaOftalmologica.insertarHojaOftalmologica(con, paciente.getCodigoPersona());
			
			//logger.info("Cod hoja oftalmologica->"+codHojaOftal+"\n");
			
			if (codHojaOftal != 0)
			{
				codEncaHisto=mundoHojaOftalmologica.insertarEncabezadoHistoHojaOftalmologica(con, codHojaOftal, medico.getNombreRegistroMedico());
				logger.info("Cod encabezado histo->"+codEncaHisto+"\n");
				
				if(codEncaHisto != 0)
				{
					if (verificarInsercionEstrabismo(hojaOftalmologicaForm))
					{
						codEstrabismo=mundoHojaOftalmologica.insertarEstrabismo(con, codEncaHisto);	
						logger.info("Cod Estrabismo->"+codEstrabismo+"\n");
						mundoHojaOftalmologica.insertarPrismaCerca(con, codEstrabismo);
					}
					
					if (verificarInsercionSegmentoAnt(hojaOftalmologicaForm))
					{
						codSegAnt=mundoHojaOftalmologica.insertarSegmentoAnterior(con, codEncaHisto);
						mundoHojaOftalmologica.insertarDetalleSegmentoAnt(con, codSegAnt);
					}
					
					if (verificarInsercionOrbitaAnexos(hojaOftalmologicaForm))
					{
						mundoHojaOftalmologica.insertarOrbitaAnexos(con, codEncaHisto);
					}
                    
                    if(verificarInsercionRetinaVitreo(hojaOftalmologicaForm))
                    {
                        mundoHojaOftalmologica.insertarRetinaVitreo(con, codEncaHisto);
                    }
				}
			}
		}
		
		//Se resetea el formulario
		hojaOftalmologicaForm.reset(true);
		
		//Consultar el histórico de las fechas en la sección Estrabismo
		hojaOftalmologicaForm.setListadoHistoEstrabismo (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 1));
		
		//Consultar el histórico de la sección Segmento Anterior
		hojaOftalmologicaForm.setListadoHistoSegmentoAnt (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 2));
		
        //Consultar el histórico de la sección Retina Vitreo
        hojaOftalmologicaForm.setListadoHistoRetinaVitreo (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 3));

		//Consultar el histórico de la sección Orbita y Anexos
		hojaOftalmologicaForm.setListadoHistoOrbitaAnexos (mundoHojaOftalmologica.consultarTipoHistorico(con, paciente.getCodigoPersona(), 4));
		
		hojaOftalmologicaForm.setEstadoAnterior("salir");
		hojaOftalmologicaForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado cargarEstrabismo, cuando seleccionan
	 * una fecha histórica en el formulario
	  * @param hojaOftalmologicaForm HojaOftalmologicaForm
	  * @param mapping Mapping para manejar la navegación
	  * @param con Conexión con la fuente de datos
	  * @param medico
	 * @throws SQLException
	*/
	private ActionForward accionCargarEstrabismo(HojaOftalmologicaForm hojaOftalmologicaForm, 
				ActionMapping mapping,Connection con, UsuarioBasico  medico) throws SQLException
	{
		HojaOftalmologica mundoHojaOftalmologica=new HojaOftalmologica();
		
		llenarMundo(hojaOftalmologicaForm, mundoHojaOftalmologica, medico, false);
		
		logger.info("Estado Estrabismo->"+hojaOftalmologicaForm.getEstadoEstrabismo()+"\n");
		
		//Si el estado de la sección estrabismo es actual se clona los datos del form
		if (hojaOftalmologicaForm.getEstadoEstrabismo().equals("actual"))
			formAuxHOftal  =(HojaOftalmologicaForm)hojaOftalmologicaForm.clone();
		
		//Se resetea el formulario
		hojaOftalmologicaForm.reset(true);
		
		if(mundoHojaOftalmologica.cargarHistoricoEstrabismo(con, hojaOftalmologicaForm.getCodHistoEstrabismo()))
		{
			mundoHojaOftalmologica.cargarHistoricoPrismaCerca(con, hojaOftalmologicaForm.getCodHistoEstrabismo());
						
			llenarForm(hojaOftalmologicaForm, mundoHojaOftalmologica);
		}
		
		logger.info("Codigo historico de estrabismo a consultar->"+hojaOftalmologicaForm.getCodHistoEstrabismo()+"\n\n");
		
		hojaOftalmologicaForm.setEstadoAnterior("cargarEstrabismo");
		hojaOftalmologicaForm.setEstado("cargarEstrabismo");
		hojaOftalmologicaForm.setAbrirSeccionEstrabismo(true);
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Este método carga la información actual de la sección Estrabismo cuando dan click en el botón Actual
	 * @param hojaOftalmologicaForm HojaOftalmologicaForm
	  * @param mapping Mapping para manejar la navegación
	  * @param datosMedico
	 * @throws SQLException
	*/
	private ActionForward accionCargarEstrabismoActual(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, String datosMedico) throws SQLException
	{
		if(formAuxHOftal!=null)
			{
			try 
				{
				//Pasar los valores del form auxiliar al form de hoja oftalmológica
				PropertyUtils.copyProperties(hojaOftalmologicaForm,formAuxHOftal);
				} 
				catch (IllegalAccessException e) 
					{
					System.out.print("Error Copiando el FORM");
					}
				catch (InvocationTargetException e) 
					{
					System.out.print("Error Copiando el FORM");
					}
				catch (NoSuchMethodException e) 
					{
					System.out.print("Error Copiando el FORM");
					}
			}
	
		hojaOftalmologicaForm.setFechaEstrabismo(UtilidadFecha.getFechaActual());
		hojaOftalmologicaForm.setDatosMedico(datosMedico);
		
		hojaOftalmologicaForm.setEstadoAnterior("cargarEstrabismoActual");
		hojaOftalmologicaForm.setEstado("cargarEstrabismoActual");
		hojaOftalmologicaForm.setAbrirSeccionEstrabismo(true);
		return mapping.findForward("principal");
	}
	
	/**
	 * Funcion para imprimir la Sección Estrabismo de la Hoja Oftalmológica 
	 * @param hojaOftalmologicaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	 private ActionForward imprimirEstrabismo(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, Connection con) throws SQLException 
	 {
		String nombreArchivo;
	    Random r=new Random();
	    nombreArchivo="/Estrabismo" + r.nextInt()  +".pdf";
	   		
	    HojaOftalmologicaPdf.pdfEstrabismo(ValoresPorDefecto.getFilePath() + nombreArchivo,hojaOftalmologicaForm, medico, paciente);	
		
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Hoja Oftalmologica");
		
		cerrarConexion(con);		
		return mapping.findForward("abrirPdf");
	}
	 
	/**
	 * Funcion para imprimir la Sección Segmento Anterior de la Hoja Oftalmológica 
	 * @param hojaOftalmologicaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	 private ActionForward imprimirSegmentoAnterior(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, Connection con) throws SQLException 
	 {
		String nombreArchivo;
	    Random r=new Random();
	    nombreArchivo="/Segmento Anterior" + r.nextInt()  +".pdf";
	    
	    //Guarda el estado anterior imprimir para que no vuelva ha entrar al estado salir al hacer el back
	    hojaOftalmologicaForm.setEstadoAnterior("imprimir");
	    
	    HojaOftalmologicaPdf.pdfSegmentoAnterior(ValoresPorDefecto.getFilePath() + nombreArchivo,hojaOftalmologicaForm, medico, paciente);	
		
	    request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Hoja Oftalmologica");
		
		cerrarConexion(con);
		return mapping.findForward("abrirPdf");
	}
         
	/**
	 * Funcion para imprimir la Sección Órbita y Anexos de la Hoja Oftalmológica 
	 * @param hojaOftalmologicaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param con 
	 * @return
	 * @throws SQLException
	 */
	 private ActionForward imprimirOrbitaAnexos(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, Connection con) throws SQLException 
	 {
		String nombreArchivo;
	    Random r=new Random();
	    nombreArchivo="/Orbita Anexos" + r.nextInt()  +".pdf";
	    
	    //Guarda el estado anterior imprimir para que no vuelva ha entrar al estado salir al hacer el back
	    hojaOftalmologicaForm.setEstadoAnterior("imprimir");
	    
	    HojaOftalmologicaPdf.pdfOrbitaAnexos(ValoresPorDefecto.getFilePath() + nombreArchivo,hojaOftalmologicaForm, medico, paciente);	
		
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Hoja Oftalmologica");
		cerrarConexion(con);
						
		return mapping.findForward("abrirPdf");
	}
			 
    /**
	 * Funcion para imprimir la Sección Retina y Vítreo de la Hoja Oftalmológica 
	 * @param hojaOftalmologicaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	 private ActionForward imprimirRetinaVitreo (HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, Connection con) throws SQLException 
	 {
		String nombreArchivo;
	    Random r=new Random();
	    nombreArchivo="/Retina Vitreo" + r.nextInt()  +".pdf";
	    
	    //Guarda el estado anterior imprimir para que no vuelva ha entrar al estado salir al hacer el back
	    hojaOftalmologicaForm.setEstadoAnterior("imprimir");
	    
	    HojaOftalmologicaPdf.pdfRetinaVitreo(ValoresPorDefecto.getFilePath() + nombreArchivo,hojaOftalmologicaForm, medico, paciente);	
		
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Hoja Oftalmologica");
		
		cerrarConexion(con);
		return mapping.findForward("abrirPdf");
     }

     /**
      * Método que se ejecuta al exportar una imagen desde el jsp y la convierte a formato jpg
      * @param hojaOftalmologicaForm
      * @param mapping
      * @param estado
      * @return
      */
     public ActionForward accionGuardarImagen(HojaOftalmologicaForm hojaOftalmologicaForm, ActionMapping mapping, String estado)
     {
         try
         {
             String opcion=estado.replaceAll("guardarImagen", "");
             logger.info(">>>>>>>Nueva opcion:"+opcion);
            
             if(opcion.equals("SegmentoAntOD"))
             {
                File archivo = File.createTempFile("tempoSegmentoAntOD", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenSegmentoAntOD(),
                        hojaOftalmologicaForm.getAnchoImagenSegmentoAntOD(),
                        hojaOftalmologicaForm.getAltoImagenSegmentoAntOD(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenSegmentoAntOD(nombreArchivo);
            }
            else if(opcion.equals("SegmentoAntOS"))
            {
                File archivo = File.createTempFile("tempoSegmentoAntOS", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenSegmentoAntOS(),
                        hojaOftalmologicaForm.getAnchoImagenSegmentoAntOS(),
                        hojaOftalmologicaForm.getAltoImagenSegmentoAntOS(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenSegmentoAntOS(nombreArchivo);
            }
            else if(opcion.equals("RetinaOD"))
            {
                File archivo = File.createTempFile("tempoRetinaOD", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenRetinaOD(),
                        hojaOftalmologicaForm.getAnchoImagenRetinaOD(),
                        hojaOftalmologicaForm.getAltoImagenRetinaOD(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenRetinaOD(nombreArchivo);
            }
            else if(opcion.equals("RetinaOS"))
            {
                File archivo = File.createTempFile("tempoRetinaOS", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenRetinaOS(),
                        hojaOftalmologicaForm.getAnchoImagenRetinaOS(),
                        hojaOftalmologicaForm.getAltoImagenRetinaOS(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenRetinaOS(nombreArchivo);
            }
            else if(opcion.equals("VitreoOD"))
            {
                File archivo = File.createTempFile("tempoVitreoOD", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenVitreoOD(),
                        hojaOftalmologicaForm.getAnchoImagenVitreoOD(),
                        hojaOftalmologicaForm.getAltoImagenVitreoOD(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenVitreoOD(nombreArchivo);
            }
            else if(opcion.equals("VitreoOS"))
            {
                File archivo = File.createTempFile("tempoVitreoOS", ".jpg", new File(ValoresPorDefecto.getFotosPath()));
                archivo.deleteOnExit();
                String nombreArchivo = archivo.getName();
                logger.info("Nombre archivo:"+archivo.getName());
                if(!Imagen.exportar(
                        hojaOftalmologicaForm.getImagenVitreoOS(),
                        hojaOftalmologicaForm.getAnchoImagenVitreoOS(),
                        hojaOftalmologicaForm.getAltoImagenVitreoOS(),                    
                        archivo))
                    logger.info("Error exportando la imagen");
                else
                    hojaOftalmologicaForm.setImagenVitreoOS(nombreArchivo);
            }
             return mapping.findForward("principal");
         }
         catch(IOException ioe)
         {
             logger.info("Error exportando la imagen: "+ioe.getMessage());
             return mapping.findForward("principal");
         }
    }
		
		 
	/**
	 * Método para llenar el form con los datos del mundo
	 * @param hojaOftalmologicaForm
	 * @param mundoHojaOftalmologica
	 */
	private void llenarForm(HojaOftalmologicaForm hojaOftalmologicaForm, HojaOftalmologica mundoHojaOftalmologica) 
	{
		//-----------------------------------------Datos Generales de la Hoja Oftalmologica-------------------------------------------------//
		hojaOftalmologicaForm.setObservacionEstrabismo(mundoHojaOftalmologica.getObservacionEstrabismo());
        
		hojaOftalmologicaForm.setObservacionSegmentoAnt(mundoHojaOftalmologica.getObservacionSegmentoAnt());
        
		hojaOftalmologicaForm.setObservacionRetinaVitreo(mundoHojaOftalmologica.getObservacionRetinaVitreo());
        
		hojaOftalmologicaForm.setObservacionOrbitaAnexos(mundoHojaOftalmologica.getObservacionOrbitaAnexos());
		
		//---------------------------------------------------Sección de Estrabismo-----------------------------------------------------------------//
		hojaOftalmologicaForm.setPpm(mundoHojaOftalmologica.getPpm());
		hojaOftalmologicaForm.setCoverTestCercaSc(mundoHojaOftalmologica.getCoverTestCercaSc());
		hojaOftalmologicaForm.setCoverTestCercaCc(mundoHojaOftalmologica.getCoverTestCercaCc());
		hojaOftalmologicaForm.setCoverTestLejosSc(mundoHojaOftalmologica.getCoverTestLejosSc());
		hojaOftalmologicaForm.setCoverTestLejosCc(mundoHojaOftalmologica.getCoverTestLejosCc());
		hojaOftalmologicaForm.setOjoFijador(mundoHojaOftalmologica.getOjoFijador());
		hojaOftalmologicaForm.setPpcInstitucion(mundoHojaOftalmologica.getPpcInstitucion());
		hojaOftalmologicaForm.setPrismaCcLejos(mundoHojaOftalmologica.getPrismaCcLejos());
		hojaOftalmologicaForm.setPrismaScLejos(mundoHojaOftalmologica.getPrismaScLejos());
		hojaOftalmologicaForm.setDuccionesVersiones(mundoHojaOftalmologica.getDuccionesVersiones());
		hojaOftalmologicaForm.setTestVisionBinocular(mundoHojaOftalmologica.getTestVisionBinocular());
		hojaOftalmologicaForm.setEstereopsis(mundoHojaOftalmologica.getEstereopsis());
		hojaOftalmologicaForm.setAmplitudFusionCercaMas(mundoHojaOftalmologica.getAmplitudFusionCercaMas());
		hojaOftalmologicaForm.setAmplitudFusionCercaMenos(mundoHojaOftalmologica.getAmplitudFusionCercaMenos());
		hojaOftalmologicaForm.setAmplitudFusionLejosMas(mundoHojaOftalmologica.getAmplitudFusionLejosMas());
		hojaOftalmologicaForm.setAmplitudFusionLejosMenos(mundoHojaOftalmologica.getAmplitudFusionLejosMenos());
		hojaOftalmologicaForm.setPrismaCompensadorCerca(mundoHojaOftalmologica.getPrismaCompensadorCerca());
		hojaOftalmologicaForm.setPrismaCompensadorLejos(mundoHojaOftalmologica.getPrismaCompensadorLejos());
		hojaOftalmologicaForm.setDatosMedico(mundoHojaOftalmologica.getDatosMedico());
		hojaOftalmologicaForm.setFechaEstrabismo(mundoHojaOftalmologica.getFechaEstrabismo());
		
		//-------------------------------------------------LLENAR EL MAPA------------------------------------------------------------------------//
		hojaOftalmologicaForm.setMapaCompleto(mundoHojaOftalmologica.getMapaCompleto());
		//logger.info("mapa mundo   "+mundoHojaOftalmologica.getMapaCompleto());
}
	
	/**
	 * Método que llena el mundo, con los datos del form
	 * @param hojaOftalmologicaForm
	 * @param mundoHojaOftalmologica
	 * @param usuario
	 * @param 
	 */
	private void llenarMundo(HojaOftalmologicaForm hojaOftalmologicaForm, HojaOftalmologica mundoHojaOftalmologica, UsuarioBasico usuario, boolean accion)
	{
        //logger.info("Ingreso a llenar mundo");
		//-----------------------------------------Datos Generales de la Hoja Oftalmologica-------------------------------------------------//
		if (!hojaOftalmologicaForm.getObservacionEstrabismoNueva().trim().equals(""))
		{
			hojaOftalmologicaForm.setObservacionEstrabismo(UtilidadTexto.agregarTextoAObservacion(hojaOftalmologicaForm.getObservacionEstrabismo(),hojaOftalmologicaForm.getObservacionEstrabismoNueva(),usuario, false));
			mundoHojaOftalmologica.setObservacionEstrabismo(hojaOftalmologicaForm.getObservacionEstrabismo());
		}
		else
		{
			mundoHojaOftalmologica.setObservacionEstrabismo(hojaOftalmologicaForm.getObservacionEstrabismo());
		}
		
		if (!hojaOftalmologicaForm.getObservacionSegmentoAntNueva().trim().equals(""))
		{
			hojaOftalmologicaForm.setObservacionSegmentoAnt(UtilidadTexto.agregarTextoAObservacion(hojaOftalmologicaForm.getObservacionSegmentoAnt(),hojaOftalmologicaForm.getObservacionSegmentoAntNueva(),usuario, false));
			mundoHojaOftalmologica.setObservacionSegmentoAnt(hojaOftalmologicaForm.getObservacionSegmentoAnt());
		}
		else
		{
			mundoHojaOftalmologica.setObservacionSegmentoAnt(hojaOftalmologicaForm.getObservacionSegmentoAnt());
		}
		
		/*
		 * Utilizado para relacionar la valoracion oftalmológica con la hoja oftalmológica
		 */
		mundoHojaOftalmologica.setNumeroSolicitud(hojaOftalmologicaForm.getNumeroSolicitud());
        
        //------------------------------------------ACTUALIZACION DE LAS IMAGENES---------------------------------------------------------------//
        //logger.info(">>>>>>>Alto:"+hojaOftalmologicaForm.getAltoImagenSegmentoAntOD());
        //logger.info(">>>>>>>Ancho:"+hojaOftalmologicaForm.getAnchoImagenSegmentoAntOD());
        //logger.info(">>>>>>>Imagen:"+UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOD()));
        
        if(hojaOftalmologicaForm.getAltoImagenSegmentoAntOD()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenSegmentoAntOD()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOD()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("segmentoAntOD", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenSegmentoAntOD();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenSegmentoAntOD(nuevoNombreArchivo);
        }
        if(hojaOftalmologicaForm.getAltoImagenSegmentoAntOS()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenSegmentoAntOS()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOS()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("segmentoAntOS", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenSegmentoAntOS();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenSegmentoAntOS(nuevoNombreArchivo);
        }
        
        if(hojaOftalmologicaForm.getAltoImagenRetinaOD()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenRetinaOD()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOD()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("retinaOD", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenRetinaOD();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenRetinaOD(nuevoNombreArchivo);
        }

        if(hojaOftalmologicaForm.getAltoImagenRetinaOS()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenRetinaOS()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOS()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("retinaOS", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenRetinaOS();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenRetinaOS(nuevoNombreArchivo);
        }

        if(hojaOftalmologicaForm.getAltoImagenVitreoOD()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenVitreoOD()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOD()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("vitreoOD", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenVitreoOD();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenVitreoOD(nuevoNombreArchivo);
        }

        if(hojaOftalmologicaForm.getAltoImagenVitreoOS()!=-1 && 
                hojaOftalmologicaForm.getAnchoImagenVitreoOS()!=-1 &&
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOS()) )
        {
            String nuevoNombreArchivo = Imagen.obtenerNombreUnicoArchivo("vitreoOS", "jpg");
            String nombreAnteriorArchivo = hojaOftalmologicaForm.getImagenVitreoOS();
            File archivoAnterior = new File(ValoresPorDefecto.getFotosPath()+nombreAnteriorArchivo);
            archivoAnterior.renameTo(new File(ValoresPorDefecto.getFotosPath()+nuevoNombreArchivo));
            mundoHojaOftalmologica.setImagenVitreoOS(nuevoNombreArchivo);
        }

        //---------------------------------------------------------------------------------------------------------//
		if (!hojaOftalmologicaForm.getObservacionRetinaVitreoNueva().trim().equals(""))
		{
			hojaOftalmologicaForm.setObservacionRetinaVitreo(UtilidadTexto.agregarTextoAObservacion(hojaOftalmologicaForm.getObservacionRetinaVitreo(),hojaOftalmologicaForm.getObservacionRetinaVitreoNueva(),usuario, false));
			mundoHojaOftalmologica.setObservacionRetinaVitreo(hojaOftalmologicaForm.getObservacionRetinaVitreo());
		}
		else
		{
			mundoHojaOftalmologica.setObservacionRetinaVitreo(hojaOftalmologicaForm.getObservacionRetinaVitreo());
		}
		
		if (!hojaOftalmologicaForm.getObservacionOrbitaAnexosNueva().trim().equals(""))
		{
			hojaOftalmologicaForm.setObservacionOrbitaAnexos(UtilidadTexto.agregarTextoAObservacion(hojaOftalmologicaForm.getObservacionOrbitaAnexos(),hojaOftalmologicaForm.getObservacionOrbitaAnexosNueva(),usuario, false));
			mundoHojaOftalmologica.setObservacionOrbitaAnexos(hojaOftalmologicaForm.getObservacionOrbitaAnexos());
		}
		else
		{
			mundoHojaOftalmologica.setObservacionOrbitaAnexos(hojaOftalmologicaForm.getObservacionOrbitaAnexos());
		}
				
		if(accion)
		{
		//---------------------------------------------------Sección de Estrabismo-----------------------------------------------------------------//
		mundoHojaOftalmologica.setPpm(hojaOftalmologicaForm.getPpm());
		mundoHojaOftalmologica.setCoverTestCercaSc(hojaOftalmologicaForm.getCoverTestCercaSc());
		mundoHojaOftalmologica.setCoverTestCercaCc(hojaOftalmologicaForm.getCoverTestCercaCc());
		mundoHojaOftalmologica.setCoverTestLejosSc(hojaOftalmologicaForm.getCoverTestLejosSc());
		mundoHojaOftalmologica.setCoverTestLejosCc(hojaOftalmologicaForm.getCoverTestLejosCc());
		mundoHojaOftalmologica.setOjoFijador(hojaOftalmologicaForm.getOjoFijador());
		mundoHojaOftalmologica.setPpcInstitucion(hojaOftalmologicaForm.getPpcInstitucion());
		mundoHojaOftalmologica.setPrismaCcLejos(hojaOftalmologicaForm.getPrismaCcLejos());
		mundoHojaOftalmologica.setPrismaScLejos(hojaOftalmologicaForm.getPrismaScLejos());
		mundoHojaOftalmologica.setDuccionesVersiones(hojaOftalmologicaForm.getDuccionesVersiones());
		mundoHojaOftalmologica.setTestVisionBinocular(hojaOftalmologicaForm.getTestVisionBinocular());
		mundoHojaOftalmologica.setEstereopsis(hojaOftalmologicaForm.getEstereopsis());
		mundoHojaOftalmologica.setAmplitudFusionCercaMas(hojaOftalmologicaForm.getAmplitudFusionCercaMas());
		mundoHojaOftalmologica.setAmplitudFusionCercaMenos(hojaOftalmologicaForm.getAmplitudFusionCercaMenos());
		mundoHojaOftalmologica.setAmplitudFusionLejosMas(hojaOftalmologicaForm.getAmplitudFusionLejosMas());
		mundoHojaOftalmologica.setAmplitudFusionLejosMenos(hojaOftalmologicaForm.getAmplitudFusionLejosMenos());
		mundoHojaOftalmologica.setPrismaCompensadorCerca(hojaOftalmologicaForm.getPrismaCompensadorCerca());
		mundoHojaOftalmologica.setPrismaCompensadorLejos(hojaOftalmologicaForm.getPrismaCompensadorLejos());
		
		//Pasarle el hashmap al mundo con los datos de la hoja oftalmológica 	
		mundoHojaOftalmologica.setMapaCompleto(hojaOftalmologicaForm.getMapaCompleto());
		}
	}
	
	/**
	 * Método para verificar si insertaron algún dato en las secciones de la hoja oftalmológica, devolviendo así un entero
	 * que indica en cual(es) secciones se insetaron datos
	 * @param hojaOftalmologicaForm
	 * @return entero dependiendo de la sección(es) donde insertaron datos. 
	 * -1=>No insertaron nuevos datos
	 * 0=> Sólo adicionaron nuevas observaciones a la hoja oftalmológica
	 * 1=> Insertaron en la seccion estrabismo
	 * 2=> Insertaron en la seccion segmento anterior
	 * 3=> Insertaron en las secciones estrabismo y segmento anterior
	 * 4=> Insertaron en las seccion orbita y anexos
	 * 5=> Insertaron en las secciones estrabismo y orbita y anexos
	 * 6=> Insertaron en las secciones segmento anterior y orbita y anexos
	 * 7=> Insertaron en las secciones estrabismo, segmento anterior y orbita y anexos
	 */
	private boolean verificarInsercionHojaOftalmologica (HojaOftalmologicaForm hojaOftalmologicaForm)
	{
		if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getObservacionEstrabismoNueva()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getObservacionSegmentoAntNueva())
				|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getObservacionRetinaVitreoNueva()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getObservacionOrbitaAnexosNueva())  )
			return true;
		
		if (verificarInsercionEstrabismo(hojaOftalmologicaForm))
			return true;
		
		if(verificarInsercionSegmentoAnt(hojaOftalmologicaForm))
            return true;
        
		if(verificarInsercionOrbitaAnexos(hojaOftalmologicaForm))
            return true;
		
        if(verificarInsercionRetinaVitreo(hojaOftalmologicaForm))
            return true;
        
		return false;
	}
	
	
	
	/**
	 * Método para verificar si insertaron algún dato en la sección de estrabismo
	 * @param hojaOftalmologicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */
	private boolean verificarInsercionEstrabismo (HojaOftalmologicaForm hojaOftalmologicaForm)
	{
		//Se verifica si alguno de los campos de la sección estrabismo se insertó algún texto
		if (UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getPpm()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getCoverTestCercaSc()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getCoverTestCercaCc()) 
					|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getCoverTestLejosSc()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getCoverTestLejosCc()) ||  hojaOftalmologicaForm.getOjoFijador()!=-1 
					|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getPrismaScLejos()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getPrismaCcLejos())
					|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getDuccionesVersiones()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getTestVisionBinocular()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getEstereopsis()) 
					|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getAmplitudFusionCercaMenos()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getAmplitudFusionCercaMas()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getAmplitudFusionLejosMenos()) 
					|| UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getAmplitudFusionLejosMas()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getPrismaCompensadorCerca()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getPrismaCompensadorLejos())  )
			{
			logger.info("ppm->"+hojaOftalmologicaForm.getPpm());
				return true;
			}
		else
		{
			for (int seccion=1; seccion<=9; seccion++)
			{
				String valorPrismaCercaSc=(String)hojaOftalmologicaForm.getMapa("prismaCercaSc_"+seccion);
				String valorPrismaCercaCc=(String)hojaOftalmologicaForm.getMapa("prismaCercaCc_"+seccion);
				
				if (UtilidadCadena.noEsVacio(valorPrismaCercaSc) || UtilidadCadena.noEsVacio(valorPrismaCercaCc))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Método para verificar si insertaron algún dato en la sección segmento anterior
	 * @param hojaOftalmologicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */
	private boolean verificarInsercionSegmentoAnt (HojaOftalmologicaForm hojaOftalmologicaForm)
	{
		Vector codigos=(Vector)hojaOftalmologicaForm.getMapa("codigosSegmentoAnt");
		
		if(codigos != null)
		{
			for (int i=0; i<codigos.size(); i++)
			{
				int segmentoAntInst=Integer.parseInt(codigos.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("segmentoAntOd_"+segmentoAntInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("segmentoAntOs_"+segmentoAntInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
				{
					return true;
				}
				
			}
		}
        
        if(UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOD()) || UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenSegmentoAntOS()))
            return true;
		
		return false;
	}
    
	/**
	 * Método para verificar si insertaron algún dato en la sección retina y vítreo
	 * @param hojaOftalmologicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */
    private boolean verificarInsercionRetinaVitreo(HojaOftalmologicaForm hojaOftalmologicaForm)
    {
        if(     UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOD()) || 
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenRetinaOS()) ||
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOD()) ||
                UtilidadCadena.noEsVacio(hojaOftalmologicaForm.getImagenVitreoOS()) )
            return true;
        
        return false;
    }
	
	/**
	 * Método para verificar si insertaron algún dato en la sección de órbitas y anexos
	 * @param hojaOftalmologicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */
	private boolean verificarInsercionOrbitaAnexos(HojaOftalmologicaForm hojaOftalmologicaForm)
	{
		Vector codigos=(Vector)hojaOftalmologicaForm.getMapa("codigosOrbitaAnexos");
		
		if(codigos != null)
		{
			for (int i=0; i<codigos.size(); i++)
			{
				int orbitaAnexosInst=Integer.parseInt(codigos.elementAt(i)+"");
				//Valor del ojo derecho
				String valorOd=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOd_"+orbitaAnexosInst);
				//Valor del ojo izquierdo
				String valorOs=(String)hojaOftalmologicaForm.getMapa("orbitaAnexoOs_"+orbitaAnexosInst);
				
				if(UtilidadCadena.noEsVacio(valorOd) || UtilidadCadena.noEsVacio(valorOs))
				{
					return true;
				}
				
			}
		}
		
		return false;
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
