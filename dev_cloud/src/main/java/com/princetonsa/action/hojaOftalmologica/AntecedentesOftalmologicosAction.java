/*
 * Creado el 23-sep-2005
 * por Julian Montoya
 */
package com.princetonsa.action.hojaOftalmologica;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.hojaOftalmologica.AntecedentesOftalmologicosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.hojaOftalmologica.AntecedentesOftalmologicos;
import com.princetonsa.pdf.AntecedentesOftalmologicosPdf;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class AntecedentesOftalmologicosAction extends Action  {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(HojaOftalmologicaAction.class);
	
	/**
	 * Método encargado del flujo y control de la funcionalidad
	 */
	
	public ActionForward execute(ActionMapping mapping,	 ActionForm form,
								 HttpServletRequest request, HttpServletResponse response ) throws Exception
								 {

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof AntecedentesOftalmologicosForm) 
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				AntecedentesOftalmologicosForm antecedentesOftalmologicosForm =(AntecedentesOftalmologicosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");

				int institucion =  usuario.getCodigoInstitucionInt();
				String estado=antecedentesOftalmologicosForm.getEstado(); 
				logger.warn("AntecedentesOftalmologicosAction Estado [ "+estado+" ]");

				if(estado == null)
				{
					antecedentesOftalmologicosForm.reset();	
					logger.warn("Estado no valido dentro del flujo de antecedentes Oftalmológicos  (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}else
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					else
						if(!UtilidadValidacion.esProfesionalSalud(usuario))
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es profesional de la salud", "errors.usuario.noAutorizado", true);
						}else
							if(antecedentesOftalmologicosForm.getEstado().trim().equals("empezar"))
							{
								//-resetear variables 				
								antecedentesOftalmologicosForm.reset();
								return accionEmpezar(antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona());			   
							}

				if(antecedentesOftalmologicosForm.getEstado().trim().equals("empezarFamiliar"))
				{
					return accionEmpezarFamiliar(antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona());
				}

				if (antecedentesOftalmologicosForm.getEstado().trim().equals("imprimir"))
				{
					return accionImprimir (antecedentesOftalmologicosForm, mapping, con, institucion, paciente, usuario, request);
				}

				if(antecedentesOftalmologicosForm.getEstado().trim().equals("salir"))
				{
					if (antecedentesOftalmologicosForm.getPaginaSiguiente().equals(""))
					{
						return accionSalir (antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona(), usuario);	
					}
					else
					{
						return accionGuardarRedireccionar(antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona(), usuario, response);
					}
				}

				if(antecedentesOftalmologicosForm.getEstado().trim().equals("salirFamiliar"))
				{
					if (antecedentesOftalmologicosForm.getPaginaSiguiente().equals(""))
					{
						return accionSalirFamiliar(antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona(), usuario);
					}
					else
					{
						return accionGuardarRedireccionarFamiliar(antecedentesOftalmologicosForm, mapping, con, institucion, paciente.getCodigoPersona(), usuario, response);
					}
				}		
				if (antecedentesOftalmologicosForm.getEstado().trim().equals("imprimirFamiliar"))
				{
					return accionImprimirFamiliar (antecedentesOftalmologicosForm, mapping, con, institucion, paciente, usuario, request);
				}
				if (antecedentesOftalmologicosForm.getEstado().trim().equals("cancelar"))
				{
					cerrarConexion(con);
					response.sendRedirect(antecedentesOftalmologicosForm.getPaginaSiguiente());
					return null;
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
	 * Funcion para guardar cambios y redireccionar a otra funcionalidad
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion
	 * @param codigoPersona
	 * @param usuario
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private ActionForward accionGuardarRedireccionar(AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
													 ActionMapping mapping, 
													 Connection con, int institucion, int paciente, UsuarioBasico usuario, HttpServletResponse response) throws SQLException, IOException 
	{
		int  pacAux=0;
		AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();
		llenarMundo(antecedentesOftalmologicosForm, mundoAntecedentesOftalmologicos, usuario);
		
		//-Verificar si el paciente tiene antecedentes oftalmologicos 
		antecedentesOftalmologicosForm.setCodAuxPaciente(mundoAntecedentesOftalmologicos.verificarAntOftalPersonales(con, paciente));
		
		pacAux = mundoAntecedentesOftalmologicos.insertarEnferOftalPersonales(con, paciente, antecedentesOftalmologicosForm.getCodAuxPaciente());
		if (pacAux > 0) 
		{
			//--Insertar el detalle de la informacion de las enfermedades 
			//--oftalmologicas parametrizas y las adicionadas por el paciente.  
			mundoAntecedentesOftalmologicos.insertarEnferOftalPersoMedicos(con, paciente);

			//--Insertar el detalle de los antecedentes de los procedimientos
			//--quirurgicos del paciente...    
			mundoAntecedentesOftalmologicos.insertarEnferOftalPersoQuirur(con, paciente);
		}
		
		//--------------------------------------------------------------------------------------------------------------------------------------
		cerrarConexion(con);
		response.sendRedirect(antecedentesOftalmologicosForm.getPaginaSiguiente());
		return null;
	}

	/**
	 * Funcion Que se llama inicialmente al empezar en la funcionalidad Antecedentes Oftalmologicos
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion : codigo de la institución para generar el listado parametrizable de enfermedades  
	 * @return
	 * @throws SQLException
	 */
	
	private ActionForward accionEmpezar	(AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										ActionMapping mapping, 
										Connection con, int institucion, int paciente) throws SQLException
	{		
		AntecedentesOftalmologicos  antecedentesOftalmologicos = new AntecedentesOftalmologicos(); 
		
		//-Limpiar  
		antecedentesOftalmologicosForm.reset();

		//-Verificar si el paciente tiene antecedentes oftalmologicos 
		antecedentesOftalmologicosForm.setCodAuxPaciente(antecedentesOftalmologicos.verificarAntOftalPersonales(con, paciente));
		
		//-Cargar las observaciones generales 
		antecedentesOftalmologicosForm.setObservacionesPersonales(antecedentesOftalmologicos.cargarObservaciones(con, paciente));
		
		
				
		//Asignamos a los tipos de enfermedades personales por institución 
		antecedentesOftalmologicosForm.setListadoEnferPerso(antecedentesOftalmologicos.consultarTiposEnferOftalPersonales(con,institucion, paciente));

		//-Consultar las enfermedades del usuario registradas anteriormente
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoPaciente(antecedentesOftalmologicos.consutarEnferOftalPersoPaciente(con, paciente)); 

		//-Consultar los procedimientos quirurgicos oftalmologicos del paciente 
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoQuirurPaciente(antecedentesOftalmologicos.consutarEnferOftalPersoQuirurPaciente(con, paciente)); 

		antecedentesOftalmologicosForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Funcion Que se llama inicialmente al empezar en la funcionalidad Antecedentes Oftalmologicos Familiares
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion : codigo de la institución para generar el listado parametrizable de enfermedades  
	 * @return
	 * @throws SQLException
	 */
	
	private ActionForward accionEmpezarFamiliar (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										ActionMapping mapping, 
										Connection con, int institucion, int paciente) throws SQLException
	{		
		AntecedentesOftalmologicos  antecedentesOftalmologicos = new AntecedentesOftalmologicos(); 
		
		//-Limpiar  
		antecedentesOftalmologicosForm.reset();

		//-Verificar si el paciente tiene antecedentes oftalmologicos 
		antecedentesOftalmologicosForm.setCodAuxPaciente(antecedentesOftalmologicos.verificarAntOftalFamiliares(con, paciente));
				
		//Asignamos a los tipos de enfermedades personales por institución  
		antecedentesOftalmologicosForm.setListadoEnferFam(antecedentesOftalmologicos.consultarTiposEnferOftalfamiliares(con,institucion, paciente)); //esta 2

		//Asignamos a los tipos de parestescos registrados   
		antecedentesOftalmologicosForm.setListadoParentesco(antecedentesOftalmologicos.consultarTiposParentesco(con)); 

		//-Consultar las enfermedades oftalmológicas familiares registradas anteriormente 
		antecedentesOftalmologicosForm.setListadoEnferFamPadece(antecedentesOftalmologicos.consutarEnferOftalFamPadece(con, paciente));  //esta 1

		//-Cargar las observaciones generales 
		antecedentesOftalmologicosForm.setObservacionesFamiliares(antecedentesOftalmologicos.cargarObservacionesFamiliares(con, paciente));
		
		
		//-Consultar las enfermedades del usuario registradas anteriormente
		/*antecedentesOftalmologicosForm.setListadoEnferOftaPersoPaciente(antecedentesOftalmologicos.consutarEnferOftalPersoPaciente(con, paciente)); 

		//-Consultar los procedimientos quirurgicos oftalmologicos del paciente 
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoQuirurPaciente(antecedentesOftalmologicos.consutarEnferOftalPersoQuirurPaciente(con, paciente)); 

	 	*/
		antecedentesOftalmologicosForm.setEstado("empezarFamiliar");
		cerrarConexion(con);
		return mapping.findForward("principalFamiliar");
	}

	/**
	 * Funcion que guarda la información ingresada por el usuario en los antecedentes Oftalmologicos Familiares
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion codigo de la 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalirFamiliar (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										ActionMapping mapping, 
										Connection con, int institucion, int paciente, UsuarioBasico usuario) throws SQLException
	{		
		int pacAux=0;
		AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();
		llenarMundo(antecedentesOftalmologicosForm, mundoAntecedentesOftalmologicos, usuario);
		
		pacAux = mundoAntecedentesOftalmologicos.insertarEnferOftalFamiliares(con, paciente, antecedentesOftalmologicosForm.getCodAuxPaciente());
		
		if (pacAux > 0) 
		{

			//-insertar el detalle de la informacion de las enfermedades oftalmologicas parametrizas y las adicionadas por el paciente  
			mundoAntecedentesOftalmologicos.insertarEnferOftalFamDetalle(con, paciente); 

			//-insertar el detalle de los antecedentes de los procedimientos quirurgicos del paciente    
			//mundoAntecedentesOftalmologicos.insertarEnferOftalPersoQuirur(con, paciente);
		}
		
		//-Verificar si el paciente tiene antecedentes oftalmologicos 
		antecedentesOftalmologicosForm.setCodAuxPaciente(mundoAntecedentesOftalmologicos.verificarAntOftalFamiliares(con, paciente));
		
		//-Consultar las enfermedades oftalmológicas familiares registradas anteriormente 
		antecedentesOftalmologicosForm.setListadoEnferFamPadece(mundoAntecedentesOftalmologicos.consutarEnferOftalFamPadece(con, paciente)); 

		//Asignamos a los tipos de enfermedades personales por institución  
		antecedentesOftalmologicosForm.setListadoEnferFam(mundoAntecedentesOftalmologicos.consultarTiposEnferOftalfamiliares(con,institucion, paciente));

		//Asignamos a los tipos de enfermedades personales por institucion y las del paciente especifico
		/*antecedentesOftalmologicosForm.setListadoEnferPerso(mundoAntecedentesOftalmologicos.consultarTiposEnferOftalPersonales(con,institucion, paciente));

		//-Consultar las enfermedades del usuario registradas anteriormente
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoPaciente(mundoAntecedentesOftalmologicos.consutarEnferOftalPersoPaciente(con, paciente)); 

		//-Consultar los procedimientos quirurgicos oftalmologicos del paciente 
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoQuirurPaciente(mundoAntecedentesOftalmologicos.consutarEnferOftalPersoQuirurPaciente(con, paciente)); 
		*/
		
		antecedentesOftalmologicosForm.setEstado("empezarFamiliar");
		cerrarConexion(con);
		return mapping.findForward("principalFamiliar");
	}

	
	/**
	 * Funcion para guardar los cambios y redireccionar a otra funcionalidad 
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion
	 * @param paciente
	 * @param usuario
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionGuardarRedireccionarFamiliar (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
															  ActionMapping mapping, 
															  Connection con, int institucion, int paciente, UsuarioBasico usuario, HttpServletResponse response) throws SQLException, IOException
	{		
		int pacAux=0;
		AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();
		llenarMundo(antecedentesOftalmologicosForm, mundoAntecedentesOftalmologicos, usuario);
		
		pacAux = mundoAntecedentesOftalmologicos.insertarEnferOftalFamiliares(con, paciente, antecedentesOftalmologicosForm.getCodAuxPaciente());
		if (pacAux > 0) 
		{
			//-insertar el detalle de la informacion de las enfermedades oftalmologicas parametrizas y las adicionadas por el paciente  
			mundoAntecedentesOftalmologicos.insertarEnferOftalFamDetalle(con, paciente); 
		}
		
		//--------------------------------------------------------------------------------------------------------------------------------------
		cerrarConexion(con);
		response.sendRedirect(antecedentesOftalmologicosForm.getPaginaSiguiente());
		return null;
	}
	
	/**
	 * Funcion que guarda la información ingresada por el usuario
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion codigo de la 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalir (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										ActionMapping mapping, 
										Connection con, int institucion, int paciente, UsuarioBasico usuario) throws SQLException
	{		
		int  pacAux=0;
		AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();
		llenarMundo(antecedentesOftalmologicosForm, mundoAntecedentesOftalmologicos, usuario);
		
		//-Verificar si el paciente tiene antecedentes oftalmologicos 
		antecedentesOftalmologicosForm.setCodAuxPaciente(mundoAntecedentesOftalmologicos.verificarAntOftalPersonales(con, paciente));
	
		
		pacAux = mundoAntecedentesOftalmologicos.insertarEnferOftalPersonales(con, paciente, antecedentesOftalmologicosForm.getCodAuxPaciente());
		if (pacAux > 0) 
		{
			//--insertar el detalle de la informacion de las enfermedades 
			//--oftalmologicas parametrizas y las adicionadas por el paciente.  
			mundoAntecedentesOftalmologicos.insertarEnferOftalPersoMedicos(con, paciente);

			//--insertar el detalle de los antecedentes de los procedimientos
			//--quirurgicos del paciente...    
			mundoAntecedentesOftalmologicos.insertarEnferOftalPersoQuirur(con, paciente);
		}
		
		//Asignamos a los tipos de enfermedades personales por institucion y las del paciente especifico
		antecedentesOftalmologicosForm.setListadoEnferPerso(mundoAntecedentesOftalmologicos.consultarTiposEnferOftalPersonales(con,institucion, paciente));

		//-Consultar las enfermedades del usuario registradas anteriormente
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoPaciente(mundoAntecedentesOftalmologicos.consutarEnferOftalPersoPaciente(con, paciente)); 

		//-Consultar los procedimientos quirurgicos oftalmologicos del paciente 
		antecedentesOftalmologicosForm.setListadoEnferOftaPersoQuirurPaciente(mundoAntecedentesOftalmologicos.consutarEnferOftalPersoQuirurPaciente(con, paciente)); 
		
		//--------------------------------------------------
		antecedentesOftalmologicosForm.setEstado("empezar");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	/**
	 * Funcion que imprime los antecedente oftalmologicos 
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion  
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionImprimir (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										  ActionMapping mapping, Connection con, int institucion, 
										  PersonaBasica paciente, UsuarioBasico usuario,  HttpServletRequest request) throws SQLException
	{		
		//AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();

		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Antecedentes_Oftalmologicos_Personales" + r.nextInt()  +".pdf";
   
        AntecedentesOftalmologicosPdf.pdfAntecedentesOftalmoloficosPersonales(ValoresPorDefecto.getFilePath() + nombreArchivo, antecedentesOftalmologicosForm, usuario, paciente, request);	
        
        UtilidadBD.cerrarConexion(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Antecedentes Oftalmologicos Personales");
	    return mapping.findForward("abrirPdf");	           
	}

	/**
	 * Funcion que imprime los antecedente oftalmologicos 
	 * @param antecedentesOftalmologicosForm
	 * @param mapping
	 * @param con
	 * @param institucion  
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionImprimirFamiliar (AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, 
										  		  ActionMapping mapping, Connection con, int institucion, 
												  PersonaBasica paciente, UsuarioBasico usuario,  HttpServletRequest request) throws SQLException
	{		
		//AntecedentesOftalmologicos  mundoAntecedentesOftalmologicos = new AntecedentesOftalmologicos();

		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/Antecedentes_Oftalmologicos_Familiares" + r.nextInt()  +".pdf";
   
        AntecedentesOftalmologicosPdf.pdfAntecedentesOftalmoloficosFamiliares(ValoresPorDefecto.getFilePath() + nombreArchivo, antecedentesOftalmologicosForm, usuario, paciente, request);	
        
        UtilidadBD.cerrarConexion(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    request.setAttribute("nombreVentana", "Antecedentes Oftalmologicos Familiares");
	    return mapping.findForward("abrirPdf");	           
	}

	
	/**
	 * Funcion para almacenar la informacion y  
	 * posteriormente enviarla a la base de datos    
	 */
	public void llenarMundo(AntecedentesOftalmologicosForm  antecedentesOftalmologicosForm, AntecedentesOftalmologicos  antecedentesOftalmologicos, UsuarioBasico usuario)
	{
		
		antecedentesOftalmologicos.setMapaCompleto(antecedentesOftalmologicosForm.getMapaCompleto());
		
		//---informacion de la "otro" enfermedad de antecedentes personales
		antecedentesOftalmologicos.setOtroEnferPerso(antecedentesOftalmologicosForm.getOtroEnferPerso());	
		antecedentesOftalmologicos.setOtroDesdeCuando(antecedentesOftalmologicosForm.getOtroDesdeCuando());	
		antecedentesOftalmologicos.setOtroTratamiento(antecedentesOftalmologicosForm.getOtroTratamiento());


		if (antecedentesOftalmologicosForm.getEstado().equals("salir"))
		{
			if (!antecedentesOftalmologicosForm.getObservacionesPersonalesNueva().trim().equals(""))
			{
				antecedentesOftalmologicosForm.setObservacionesPersonales(UtilidadTexto.agregarTextoAObservacion(antecedentesOftalmologicosForm.getObservacionesPersonales(),antecedentesOftalmologicosForm.getObservacionesPersonalesNueva(),usuario, false));
				antecedentesOftalmologicos.setObservacionesPersonales(antecedentesOftalmologicosForm.getObservacionesPersonales());
			}
			else
			{
				antecedentesOftalmologicos.setObservacionesPersonales(antecedentesOftalmologicosForm.getObservacionesPersonales());
			}
		}	

		if (antecedentesOftalmologicosForm.getEstado().equals("salirFamiliar"))
		{
			if (!antecedentesOftalmologicosForm.getObservacionesFamiliaresNueva().trim().equals(""))
			{
				antecedentesOftalmologicosForm.setObservacionesFamiliares(UtilidadTexto.agregarTextoAObservacion(antecedentesOftalmologicosForm.getObservacionesFamiliares(),antecedentesOftalmologicosForm.getObservacionesFamiliaresNueva(),usuario, false));
				antecedentesOftalmologicos.setObservacionesFamiliares(antecedentesOftalmologicosForm.getObservacionesFamiliares());
			}
			else
			{
				antecedentesOftalmologicos.setObservacionesFamiliares(antecedentesOftalmologicosForm.getObservacionesFamiliares());
			}
		}
	}

	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
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
